#/bin/bash

mkdir -p ../../results/outputs/Experiment_Binary

rm ../../results/outputs/Experiment_Binary/*

GUROBI_PATHH="~/opt/gurobi903/linux64/lib/gurobi.jar"
JAR_PATH="../../target/stable-marriage-1.0.jar"
OUT_PATH="../../results/outputs/Experiment_Binary/"
SUF=""

declare -a ALG_LIST=("ILP" "PriorityMatch") 

t=50
m=2
c=3

for i in {1..50}
do
	for impl in "${ALG_LIST[@]}"
	do
		for n in 5 10 15 20
		do
			java -cp ${JAR_PATH}:${GUROBI_PATHH} cslab.ntua.gr.algorithms.$impl -n "$n" -m "$m" -s "../../datasets/BinaryUniform/students${i}_n${n}_t${t}_m${m}_c${c}${SUF}" -u "../../datasets/BinaryUniform/schools${i}_n${n}_t${t}_m${m}_c${c}${SUF}" -a "../../datasets/BinaryUniform/affiliates${i}_n${n}_t${t}_m${m}_c${c}${SUF}" >> "${OUT_PATH}outBU_${n}"
		done
	done
done
