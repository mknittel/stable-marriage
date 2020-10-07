#/bin/bash

mkdir -p ../../results/outputs/Experiment_Approx

GUROBI_PATHH="~/opt/gurobi903/linux64/lib/gurobi.jar"
JAR_PATH="../../target/stable-marriage-1.0.jar"
OUT_PATH="../../results/outputs/Experiment_Approx/"
SUF=""
#SUF=".zip"

declare -a ALG_LIST=("PriorityMatch") 
#declare -a ALG_LIST=("ILP") 
#declare -a ALG_LIST=("PriorityMatch" "ILP") 

for i in "1" #{1..50}
do
	for impl in "${ALG_LIST[@]}"
	do
		for n in 10 250 500 1000 2000 4000
		do
			for t in 20 50 80
			do
				for m in 5 #{1...5}
				do
					for c in 5 #{1...10}
					do
						## Uniform
						java -cp ${JAR_PATH}:${GUROBI_PATHH} cslab.ntua.gr.algorithms.$impl -n "$n" -m "$m" -s "../../datasets/BinaryUniform/students${i}_n${n}_t${t}_m${m}_c${c}${SUF}" -u "../../datasets/BinaryUniform/schools${i}_n${n}_t${t}_m${m}_c${c}${SUF}" -a "../../datasets/BinaryUniform/affiliates${i}_n${n}_t${t}_m${m}_c${c}${SUF}" # >> "${OUT_PATH}outBU_${n}"
					done
				done
			done
		done
	done
done
