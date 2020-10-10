#!/bin/bash

mkdir -p ../datasets/BinaryUniformn

bash ./clean.sh

JAR_PATH="../target/stable-marriage-1.0.jar"

t=50
m=5
c=5

for i in {1..50}
do 
	for n in 10 250 500 1000 2000 4000
	do
		java -cp ${JAR_PATH} cslab.ntua.gr.data.BinaryUniformDataGenerator -n ${n} -m ${m} -t ${t} -c ${c} -s ../datasets/BinaryUniformn/students${i}_n${n}_t${t}_m${m}_c${c} -u ../datasets/BinaryUniformn/schools${i}_n${n}_t${t}_m${m}_c${c} -a ../datasets/BinaryUniformn/affiliates${i}_n${n}_t${t}_m${m}_c${c}
	done
done

