#/bin/bash

mkdir -p ../../results/outputs/Experiment_Binary_Aff

rm ../../results/outputs/Experiment_Binary_Aff/*

GUROBI_PATHH="~/opt/gurobi903/linux64/lib/gurobi.jar"
JAR_PATH="../../target/stable-marriage-1.0.jar"
OUT_PATH="../../results/outputs/Experiment_Binary_Aff/"
SUF=""

impl="PriorityMatch"
t=50
n=1000
c=5

for i in {1..50}
do
	for m in 5 10 20 30
	do
		java -cp ${JAR_PATH}:${GUROBI_PATHH} cslab.ntua.gr.algorithms.$impl -n "$n" -m "$m" -s "../../datasets/BinaryUniformAff/students${i}_n${n}_t${t}_m${m}_c${c}${SUF}" -u "../../datasets/BinaryUniformAff/schools${i}_n${n}_t${t}_m${m}_c${c}${SUF}" -a "../../datasets/BinaryUniformAff/affiliates${i}_n${n}_t${t}_m${m}_c${c}${SUF}" >> "${OUT_PATH}outBU_${m}"
	done
done
