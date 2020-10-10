#!/bin/bash
mkdir -p ../datasets/BinaryUniformThresh

bash ./clean.sh

JAR_PATH="../target/stable-marriage-1.0.jar"

n=1000
m=5
c=5

for i in {1..50}
do 
	for t in 10 20 30 40 50 60 70 80 90
	do
		java -cp ${JAR_PATH} cslab.ntua.gr.data.BinaryUniformDataGenerator -n ${n} -m ${m} -t ${t} -c ${c} -s ../datasets/BinaryUniformThresh/students${i}_n${n}_t${t}_m${m}_c${c} -u ../datasets/BinaryUniformThresh/schools${i}_n${n}_t${t}_m${m}_c${c} -a ../datasets/BinaryUniformThresh/affiliates${i}_n${n}_t${t}_m${m}_c${c}
	done
done

