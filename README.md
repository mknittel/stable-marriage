Details
-------
Implementation of algorithms that solve the stable marriage problem and the binary affiliate matching problem. 
(https://en.wikipedia.org/wiki/Stable_marriage_problem)

Code has been adapted from the ntzia stable-marriage repo (https://github.com/ntzia/stable-marriage), used in "Equitable Stable Matchings in Quadratic Time" (https://papers.nips.cc/paper/8337-equitable-stable-matchings-in-quadratic-time).

Binary affiliate matching problem algorithms:
* ILP: a standard integer linear program to solve BAM
* PriorityMatch: an implementation of our proposed PriorityMatch algorithm (specifically, the SmartPriorityMatch implementation)

Stable marriage algorithms implemented from the literature:
* Gale-Shapley  
[https://www.researchgate.net/publication/228108175_College_Admissions_and_Stability_of_Marriage]
	* GS_MaleOpt produces the optimal solution for men
	* GS_FemaleOpt produces the optimal solution for women
* MinRegret   
[https://dl.acm.org/citation.cfm?id=23802]
* MinEgalitarian  
[https://dl.acm.org/citation.cfm?id=28871]
* Approx  
[https://dl.acm.org/citation.cfm?id=1868239]
* DACC (2 variants, controlled with -s)  
[https://dl.acm.org/citation.cfm?id=2940727]
	* DACC_R (-s R) chooses the sequence of proposals randomly
	* DACC_D (-s D) chooses the sequence of proposals based on which side is "losing" (as PowerBalance does)
* ESMA  
[https://dl.acm.org/citation.cfm?id=2921985&preflayout=flat]
* Lotto  
[https://dl.acm.org/citation.cfm?id=593304]
* ROM  
[https://link.springer.com/article/10.1007/BF01211824]
* EROM  
[https://link.springer.com/article/10.1007/s11238-005-6846-0]
* Swing  
[https://dl.acm.org/citation.cfm?id=2485203]
* SML2  
[https://www.researchgate.net/publication/286062161_Local_Search_Approaches_in_Stable_Matching_Problems]
* BiLS  
[https://www.researchgate.net/publication/312256504_A_Bidirectional_Local_Search_for_the_Stable_Marriage_Problem]
* Better/Best Response Dynamics  
[https://dl.acm.org/citation.cfm?id=1386831]

Stable Marriage algorithms from previous work:
* PDB, EDS, LDS: proposal-based approaches that terminate by monotonically increasing content couples
* PowerBalance: proposal-based algorithm that tries to keep a good balance between both sides and then terminates by a compromising procedure
* Hybrid, HybridMultiSearch: combinations of PowerBalance with the local search method

* iBiLS (refined version with rotations) [https://www.researchgate.net/publication/312256504_A_Bidirectional_Local_Search_for_the_Stable_Marriage_Problem]

Usage
-----
Clone from github

Install your gurobi.jar file with maven in the repo directory:
```
mvn install:install-file -Dfile=path/to/gurobi/jar/from/repository/gurobi.jar -DgroupId=gurobi -DartifactId=gurobi-jar -Dversion=7.5.1 -Dpackaging=jar
```
Run:
```
mvn package
```
To reproduce the small-scale PriorityMatch vs ILP experiments:
```
cd scripts/
bash create_binary_data.sh 
cd Experiment_Binary/
bash run.sh
python3 plot.py

```
To reproduce the large-scale PriorityMatch experiments (varying n):
```
cd scripts/
bash create_binary_n_data.sh
cd Experiment_Binary_n/
bash run.sh
python3 plot.py
```

To reproduce the quota-varying PriorityMatch experiments:
```
cd scripts/
bash create_binary_cap_data.sh
cd Experiment_Binary_Cap/
bash run.sh
python3 plot.py
```

To reproduce the affiliates-per-employer-varying PriorityMatch experiments:
```
cd scripts/
bash create_binary_aff_data.sh
cd Experiment_Binary_Aff/
bash run.sh
python3 plot.py
```

To reproduce the threshold-varying PriorityMatch experiments:
```
cd scripts/
bash create_binary_thresh_data.sh
cd Experiment_Binary_Thresh/
bash run.sh
python3 plot.py
```

Note: creating data cleans out all old data from all previous experiments. However, experiment results are kept until that specific experiment is run again.

Dependencies
-----
You need maven and gurobi to build the project:
* Maven: sudo apt-get install maven
* Gurobi: https://www.gurobi.com/ (either use a free trial or get an academic license)

For the plotting scripts you need:

* pip install numpy
* pip install pandas
* pip install seaborn
* sudo apt-get install python-tk
* sudo apt-get install texlive-full

Tested on Ubuntu 18.04.
