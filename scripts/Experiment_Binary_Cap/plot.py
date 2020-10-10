import matplotlib.pyplot as plt
import numpy as np

PM_times = []
cap_range = [10, 100, 250, 500]

for i in range(len(cap_range)):
	filename = "../../results/outputs/Experiment_Binary_Cap/outBU_" + str(cap_range[i])
	f = open(filename, "r")

	PM_times += [[]]

	for line in f.readlines():
		words = line.strip().split(" ")
		
		if words[0] == "PriorityMatch:":
			PM_times[i] += [float(words[2])]
		else:
			print("Incorrect key: " + words[0])

PM_avgs = []

for i in range(len(cap_range)):
	PM_avgs += [sum(PM_times[i]) / len(PM_times[i])]

linewidth = 2
markersize = 3

plt.plot(cap_range, PM_avgs, label='SmartPriorityMatch runtime', marker='o', markerfacecolor='orange', markersize=markersize, color='orange', linewidth=linewidth, linestyle='solid')

plt.gcf().subplots_adjust(bottom=0.15, left=0.15)
plt.rcParams.update({'font.size': 14})
plt.xticks(cap_range, fontsize=14)
plt.yticks(fontsize=14)
plt.xlabel("Applicant capacity", fontsize=18)
plt.ylabel("Runtime (sec)", fontsize=18)
plt.savefig('/mnt/c/Users/marin/Downloads/capplt.png')
