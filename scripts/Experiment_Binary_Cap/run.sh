#/bin/bash

mkdir -p ../../results/outputs/Experiment_Binary_Cap

rm ../../results/outputs/Experiment_Binary_Cap/*

GUROBI_PATHH="~/opt/gurobi903/linux64/lib/gurobi.jar"
JAR_PATH="../../target/stable-marriage-1.0.jar"
OUT_PATH="../../results/outputs/Experiment_Binary_Cap/"
SUF=""

impl="PriorityMatch"
n=1000
t=50
m=5

for i in {1..50}
do
	for c in 10 100 250 500
	do
		java -cp ${JAR_PATH}:${GUROBI_PATHH} cslab.ntua.gr.algorithms.$impl -n "$n" -m "$m" -s "../../datasets/BinaryUniformCap/students${i}_n${n}_t${t}_m${m}_c${c}${SUF}" -u "../../datasets/BinaryUniformCap/schools${i}_n${n}_t${t}_m${m}_c${c}${SUF}" -a "../../datasets/BinaryUniformCap/affiliates${i}_n${n}_t${t}_m${m}_c${c}${SUF}" >> "${OUT_PATH}outBU_${c}"
	done
done
