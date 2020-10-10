#!/bin/bash
mkdir -p ../datasets/BinaryUniform

bash ./clean.sh

JAR_PATH="../target/stable-marriage-1.0.jar"

t=50
m=2
c=3

for i in {1..50}
do 
	for n in 5 10 15 20
	do
		java -cp ${JAR_PATH} cslab.ntua.gr.data.BinaryUniformDataGenerator -n ${n} -m ${m} -t ${t} -c ${c} -s ../datasets/BinaryUniform/students${i}_n${n}_t${t}_m${m}_c${c} -u ../datasets/BinaryUniform/schools${i}_n${n}_t${t}_m${m}_c${c} -a ../datasets/BinaryUniform/affiliates${i}_n${n}_t${t}_m${m}_c${c}
	done
done

