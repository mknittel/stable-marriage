#/bin/bash

mkdir -p ../../results/outputs/Experiment_Binary_Thresh

rm ../../results/outputs/Experiment_Binary_Thresh/*

GUROBI_PATHH="~/opt/gurobi903/linux64/lib/gurobi.jar"
JAR_PATH="../../target/stable-marriage-1.0.jar"
OUT_PATH="../../results/outputs/Experiment_Binary_Thresh/"
SUF=""

impl="PriorityMatch"
n=1000
m=5
c=5

for i in {1..50}
do
	for t in 10 20 30 40 50 60 70 80 90
	do
		java -cp ${JAR_PATH}:${GUROBI_PATHH} cslab.ntua.gr.algorithms.$impl -n "$n" -m "$m" -s "../../datasets/BinaryUniformThresh/students${i}_n${n}_t${t}_m${m}_c${c}${SUF}" -u "../../datasets/BinaryUniformThresh/schools${i}_n${n}_t${t}_m${m}_c${c}${SUF}" -a "../../datasets/BinaryUniformThresh/affiliates${i}_n${n}_t${t}_m${m}_c${c}${SUF}" >> "${OUT_PATH}outBU_${t}"
	done
done
