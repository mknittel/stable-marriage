#!/bin/bash
mkdir -p ../datasets/BinaryUniformAff

bash ./clean.sh

JAR_PATH="../target/stable-marriage-1.0.jar"

t=50
n=1000
c=5

for i in {1..50}
do 
	for m in 5 10 20 30
	do
		java -cp ${JAR_PATH} cslab.ntua.gr.data.BinaryUniformDataGenerator -n ${n} -m ${m} -t ${t} -c ${c} -s ../datasets/BinaryUniformAff/students${i}_n${n}_t${t}_m${m}_c${c} -u ../datasets/BinaryUniformAff/schools${i}_n${n}_t${t}_m${m}_c${c} -a ../datasets/BinaryUniformAff/affiliates${i}_n${n}_t${t}_m${m}_c${c}
	done
done

