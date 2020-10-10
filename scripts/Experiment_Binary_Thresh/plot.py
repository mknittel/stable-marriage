import matplotlib.pyplot as plt
import numpy as np

PM_times = []
thresh_range = range(10, 100, 10)
thresholds = [(1.0*x)/100 for x in thresh_range]

for i in range(len(thresh_range)):
	filename = "../../results/outputs/Experiment_Binary_Thresh/outBU_" + str(thresh_range[i])
	f = open(filename, "r")

	PM_times += [[]]

	for line in f.readlines():
		words = line.strip().split(" ")
		
		if words[0] == "PriorityMatch:":
			PM_times[i] += [float(words[2])]
		else:
			print("Incorrect key: " + words[0])

PM_avgs = []

for i in range(len(thresh_range)):
	PM_avgs += [sum(PM_times[i]) / len(PM_times[i])]

linewidth = 2
markersize = 3

plt.plot(thresholds, PM_avgs, label='SmartPriorityMatch runtime', marker='o', markerfacecolor='orange', markersize=markersize, color='orange', linewidth=linewidth, linestyle='solid')

plt.gcf().subplots_adjust(bottom=0.15, left=0.15)
plt.rcParams.update({'font.size': 14})
plt.xticks(thresholds, fontsize=14)
plt.yticks(fontsize=14)
plt.xlabel("Interest threshold", fontsize=18)
plt.ylabel("Runtime (sec)", fontsize=18)
plt.savefig('./threshplt.png')
