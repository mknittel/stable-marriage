#!/bin/bash

mkdir -p ../datasets/Uniform
mkdir -p ../datasets/Gauss
mkdir -p ../datasets/Discrete
mkdir -p ../datasets/BinaryUniform

rm ../datasets/Uniform/*
rm ../datasets/Gauss/*
rm ../datasets/Discrete/*
rm ../datasets/BinaryUniform/*

JAR_PATH="../target/stable-marriage-1.0.jar"

for i in {1..5}
do 
	for t in 20 50 80
	do
		for n in 5 #250 500 1000 2000 4000
		do
			java -cp ${JAR_PATH} cslab.ntua.gr.data.BinaryUniformDataGenerator ${n} ${t} ../datasets/BinaryUniform/students${i}_n${n}_t${t}
			java -cp ${JAR_PATH} cslab.ntua.gr.data.BinaryUniformDataGenerator ${n} ${t} ../datasets/BinaryUniform/schools${i}_n${n}_t${t}
			java -cp ${JAR_PATH} cslab.ntua.gr.data.BinaryUniformDataGenerator ${n} ${t} ../datasets/BinaryUniform/affiliates${i}_n${n}_t${t}
		done
	done
done

echo $i

for i in {1..50}
do 
	for n in 10 #250 500 1000 2000 4000
	do
		java -cp ${JAR_PATH} cslab.ntua.gr.data.UniformDataGenerator ${n} ../datasets/Uniform/men${i}_n${n}
		java -cp ${JAR_PATH} cslab.ntua.gr.data.UniformDataGenerator ${n} ../datasets/Uniform/women${i}_n${n}
	done
done



for i in {1..50}
do 
	for n in 10 #250 500 1000 2000 4000
	do
		java -cp ${JAR_PATH} cslab.ntua.gr.data.DiscreteDataGenerator ${n} 0.4 ../datasets/Discrete/men${i}_n${n}_h40
		java -cp ${JAR_PATH} cslab.ntua.gr.data.DiscreteDataGenerator ${n} 0.4 ../datasets/Discrete/women${i}_n${n}_h40
	done
done

for i in {1..50}
do 
	for n in 10 #250 500 1000 2000 4000
	do
		java -cp ${JAR_PATH} cslab.ntua.gr.data.GaussDataGenerator ${n} 0.4 ../datasets/Gauss/men${i}_n${n}_h40
		java -cp ${JAR_PATH} cslab.ntua.gr.data.GaussDataGenerator ${n} 0.4 ../datasets/Gauss/women${i}_n${n}_h40
	done
done
