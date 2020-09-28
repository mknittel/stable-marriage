#/bin/bash

mkdir -p ../../results/outputs/Experiment_Approx

JAR_PATH="../../target/stable-marriage-1.0.jar"
OUT_PATH="../../results/outputs/Experiment_Approx/"
SUF=""
#SUF=".zip"

declare -a ALG_LIST=("PriorityMatch") #("GS_MaleOpt" "GS_FemaleOpt" "iBiLS -c SEq -p 0.125" "HybridMultiSearch -c SEq")

for i in "5" #{1..50}
do
	for impl in "${ALG_LIST[@]}"
	do
		for n in "5" #"4000"
		do
			for t in "50"
			do
				## Uniform
				java -cp ${JAR_PATH} cslab.ntua.gr.algorithms.$impl -n "$n" -s "../../datasets/BinaryUniform/students${i}_n${n}_t${t}${SUF}" -u "../../datasets/BinaryUniform/schools${i}_n${n}_t${t}${SUF}" -a "../../datasets/BinaryUniform/affiliates${i}_n${n}_t${t}${SUF}" # >> "${OUT_PATH}outBU_${n}"
			done
		done
	done
done
