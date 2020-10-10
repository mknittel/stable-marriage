#!/bin/bash

mkdir -p ../datasets/BinaryUniformCap

bash ./clean.sh

JAR_PATH="../target/stable-marriage-1.0.jar"

t=50
m=5
n=1000

for i in {1..50}
do 
	for c in 10 100 250 500
	do
		java -cp ${JAR_PATH} cslab.ntua.gr.data.BinaryUniformDataGenerator -n ${n} -m ${m} -t ${t} -c ${c} -s ../datasets/BinaryUniformCap/students${i}_n${n}_t${t}_m${m}_c${c} -u ../datasets/BinaryUniformCap/schools${i}_n${n}_t${t}_m${m}_c${c} -a ../datasets/BinaryUniformCap/affiliates${i}_n${n}_t${t}_m${m}_c${c}
	done
done

