#/bin/bash

mkdir -p ../../results/outputs/Experiment_Binary_n

rm ../../results/outputs/Experiment_Binary_n/*

GUROBI_PATHH="~/opt/gurobi903/linux64/lib/gurobi.jar"
JAR_PATH="../../target/stable-marriage-1.0.jar"
OUT_PATH="../../results/outputs/Experiment_Binary_n/"
SUF=""

impl="PriorityMatch"
t=50
m=5
c=5

for i in {1..50}
do
	for n in 10 250 500 1000 2000 4000
	do
		java -cp ${JAR_PATH}:${GUROBI_PATHH} cslab.ntua.gr.algorithms.$impl -n "$n" -m "$m" -s "../../datasets/BinaryUniformn/students${i}_n${n}_t${t}_m${m}_c${c}${SUF}" -u "../../datasets/BinaryUniformn/schools${i}_n${n}_t${t}_m${m}_c${c}${SUF}" -a "../../datasets/BinaryUniformn/affiliates${i}_n${n}_t${t}_m${m}_c${c}${SUF}" >> "${OUT_PATH}outBU_${n}"
	done
done
