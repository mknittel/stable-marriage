import matplotlib.pyplot as plt
import matplotlib
import numpy as np

ILP_times = []
ILP_times_with_init = []
PM_times = []

for i in range(4):
	filename = "../../results/outputs/Experiment_Binary/outBU_" + str((i+1)*5)
	f = open(filename, "r")

	ILP_times += [[]]
	ILP_times_with_init += [[]]
	PM_times += [[]]

	for line in f.readlines():
		words = line.strip().split(" ")
		
		if words[0] == "ElapsedTime":
			ILP_times_with_init[i] += [float(words[3])]
		elif words[0] == "ElapsedTime:":
			ILP_times[i] += [float(words[1])]
		elif words[0] == "PriorityMatch:":
			PM_times[i] += [float(words[2])]
		elif words[0] == "ILP:":
			continue
		else:
			print("Incorrect key: " + words[0])

ILP_avgs = []
ILP_avgs_init = []
PM_avgs = []

for i in range(4):
	ILP_avgs += [sum(ILP_times[i]) / len(ILP_times[i])]
	ILP_avgs_init += [sum(ILP_times_with_init[i]) / len(ILP_times_with_init[i])]
	PM_avgs += [sum(PM_times[i]) / len(PM_times[i])]

x_values = [5,10,15,20]

linewidth = 2
markersize = 3

plt.plot(x_values, ILP_avgs, label='ILP runtime', marker='o', markerfacecolor='blue', markersize=markersize, color='blue', linewidth=linewidth, linestyle='dashed')
plt.plot(x_values, ILP_avgs_init, label='ILP runtime with initialization', marker='o', markerfacecolor='green', markersize=markersize, color='green', linewidth=linewidth, linestyle='dotted')
plt.plot(x_values, PM_avgs, label='SmartPriorityMatch runtime', marker='o', markerfacecolor='orange', markersize=markersize, color='orange', linewidth=linewidth, linestyle='solid')

plt.gcf().subplots_adjust(bottom=0.15, left=0.15)
plt.rcParams.update({'font.size': 14})
plt.xticks(x_values, fontsize=14)
plt.yticks(fontsize=14)
plt.yscale("log")
plt.xlabel("Number of employers", fontsize=18)
plt.ylabel("Runtime (sec)", fontsize=18)
plt.legend()
plt.savefig('/mnt/c/Users/marin/Downloads/pm_vs_ilp.png')
