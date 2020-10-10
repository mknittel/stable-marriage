package cslab.ntua.gr.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

import gurobi.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import cslab.ntua.gr.entities.Affiliation;
import cslab.ntua.gr.entities.Student;
import cslab.ntua.gr.entities.BipartiteGraph;
import cslab.ntua.gr.entities.Marriage;
import cslab.ntua.gr.entities.School;
import cslab.ntua.gr.tools.Metrics;

public class PriorityMatchLP extends Abstract_BSM_Algorithm
{
    private int[] kappa;
    private int[][] married;
    private Stack<Integer> singles;
    private int active_proposer;
	private boolean debug;

    public PriorityMatchLP(int n, int m, String studentFile, String schoolFile, String affiliateFile, boolean debug)
    {
        super(n, m, studentFile, schoolFile, affiliateFile);
		this.debug = debug;
    }

    // Constructor for when agents are available
    public PriorityMatchLP(int n, int m, Student[] students, School[] schools)
    {
        super(n, m, students, schools);
		this.debug = debug;
    }
	
	public Marriage match()
	{
		long startTime = System.nanoTime();

		ArrayList<Student> students0 = new ArrayList<Student>();
		ArrayList<School> schools0 = new ArrayList<School>();
		
		for (int i = 0; i < this.n*this.m; i++) {
			Student student = new Student(this.students[i]);
			students0.add(student);

			if (i < this.n) {
				School school = new School(this.schools[i]);
				schools0.add(school);
			}
		}

		ArrayList<Student> students1 = this.copyStudents(students0);
		ArrayList<School> schools1 = this.copySchools(schools0);

		ArrayList<ArrayList<Integer>> mu1 = this.getMaximalMatching(1, schools1, students1);

		ArrayList<Student> students2 = this.copyStudents(students1);
		ArrayList<School> schools2 = this.copySchools(schools1);

		this.reduceMatchingCapacity(mu1, students2, schools2);

		ArrayList<Student> students4 = this.copyStudents(students2);
		ArrayList<School> schools4 = this.copySchools(schools2);

		ArrayList<ArrayList<Integer>> mu4 = this.getMaximalMatching(4, schools4, students4);

		this.reduceMatchingCapacity(mu1, students0, schools0);
		this.reduceMatchingCapacity(mu4, students0, schools0);

		ArrayList<ArrayList<Integer>> mu0 = this.getMaximalMatching(0, schools0, students0);

		this.reduceMatchingCapacity(mu4, students2, schools2);

		ArrayList<ArrayList<Integer>> mu2 = this.getMaximalMatching(2, schools2, students2);

		ArrayList<ArrayList<Integer>> finalMatching = new ArrayList<ArrayList<Integer>>();
		finalMatching.addAll(mu0);
		finalMatching.addAll(mu1);
		finalMatching.addAll(mu2);
		finalMatching.addAll(mu4);		

		int[][] matching = new int[finalMatching.size()][2];
		for (int i = 0; i < finalMatching.size(); i++) {
			matching[i][0] = finalMatching.get(i).get(0);
			matching[i][1] = finalMatching.get(i).get(1);
			if (this.debug) System.out.println("Matching " + matching[i][0] + " with " + matching[i][1]);
		}

		long endTime = System.nanoTime();
		long elapsedTime = endTime - startTime;
		time = elapsedTime / 1.0E09;

		return new Marriage(this.n, matching);
	}

	// For the H_1 matching, we only reduce school capacities by the neighborhood sizes in H_0
	public void reduceMatchingCapacity(ArrayList<ArrayList<Integer>> matching, ArrayList<Student> students, ArrayList<School> schools)
	{
		for (ArrayList<Integer> match : matching) {
			int i = match.get(0);
			int j = match.get(1);
			
			School school = schools.get(i);
			Student student = students.get(j);

			school.reduceCapacity();
			student.reduceCapacity();
		}
	}

	public void printMatching(ArrayList<ArrayList<Integer>> matching)
	{
		String matches = "Matches: ";

		for (ArrayList<Integer> match : matching) {
			matches += "(" + match.get(0) + ", " + match.get(1) + "), ";
		}
		
		System.out.println(matches);
	}

	public ArrayList<ArrayList<Integer>> getMaximalMatching(int graphNum, ArrayList<School> schools, ArrayList<Student> students)
	{
		ArrayList<Affiliation> affiliations = new ArrayList<Affiliation>();

		ArrayList<ArrayList<ArrayList<Integer>>> neighborhoods = this.buildNeighborhoods(graphNum, students, schools, affiliations);
		ArrayList<ArrayList<Integer>> studentNeighbors = neighborhoods.get(0);
		ArrayList<ArrayList<Integer>> schoolNeighbors = neighborhoods.get(1);

		try
		{
			GRBEnv env = new GRBEnv(true);
			env.set("logFile", "test.log");
			env.start();

			GRBModel model = new GRBModel(env);
			GRBVar[][] matches = new GRBVar[this.n][this.n*this.m];
			GRBVar zero = model.addVar(0.0, 0.0, 0.0, GRB.BINARY, "zero");

			for (int i = 0; i < this.n; i++) {
				for (int j = 0; j < this.n*this.m; j++) {
					GRBVar match = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, this.matchName(i,j));
					matches[i][j] = match;
				}
			}
			
			for (int i = 0; i < this.n; i++) {
				int iCap = schools.get(i).getCapacity();

				for (int j = 0; j < this.n * this.m; j++) {
					int jCap = students.get(j).getCapacity();

					// Maximality constraints
					GRBLinExpr expr = new GRBLinExpr();
					expr.addTerm(0.0, zero);

					for (int istar : studentNeighbors.get(j)) {
						expr.addTerm(iCap, matches[istar][j]);
					}

					for (int jstar : schoolNeighbors.get(i)) {
						expr.addTerm(jCap, matches[i][jstar]);
					}

					model.addConstr(expr, GRB.GREATER_EQUAL, iCap*jCap, this.maximalityConstraintName(i, j));
				}

				// Capacity constraints for employers
				GRBLinExpr expr = new GRBLinExpr();
				expr.addTerm(0.0, zero);

				for (int jstar : schoolNeighbors.get(i)) {
					expr.addTerm(1.0, matches[i][jstar]);
				}

				model.addConstr(expr, GRB.LESS_EQUAL, iCap, this.neighborhoodName(i, "school"));
			}

			for (int j = 0; j < this.n*this.m; j++) {
				int jCap = students.get(j).getCapacity();

				//Capacity constraints for students
				GRBLinExpr expr = new GRBLinExpr();
				expr.addTerm(0.0, zero);

				for (int istar : studentNeighbors.get(j)) {
					expr.addTerm(1.0, matches[istar][j]);
				}

				model.addConstr(expr, GRB.LESS_EQUAL, jCap, this.neighborhoodName(j, "student"));
			}

			GRBVar[] apps = new GRBVar[this.n*this.m];

			// Handling reservations (otherwise the model is complete)
			if (graphNum == 1 || graphNum == 4) {
				for (int j = 0; j < this.n*this.m; j++) {
					apps[j] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, this.appName(j));
				}
				
				for (int j = 0; j < this.n*this.m; j++) {
					Student student = this.students[j];
					int jCap = student.getCapacity();
					
					GRBLinExpr expr = new GRBLinExpr();
					expr.addTerm(jCap, apps[j]);
					
					for (int istar : studentNeighbors.get(j)) {
						expr.addTerm(1.0, matches[istar][j]);
					}
					
					model.addConstr(expr, GRB.GREATER_EQUAL, jCap, this.reserveName(j));
				}

				GRBLinExpr optExpr = new GRBLinExpr();

				for (Affiliation aff : affiliations) {
					int reservation = aff.getReservation();
					ArrayList<Student> affiliates = aff.getStudents();

					GRBLinExpr expr = new GRBLinExpr();
					expr.addTerm(0.0, zero);
					
					for (Student affiliate : affiliates) {
						int studentID = affiliate.getID();
						expr.addTerm(1.0, apps[studentID]);
					}

					model.addConstr(expr, GRB.GREATER_EQUAL, reservation, this.affiliationName(aff.getSchool().getID()));

					optExpr.addTerm(1.0, apps[aff.getSchool().getID()]);
				}


				model.setObjective(optExpr, GRB.MINIMIZE);
			}

			model.optimize();

			ArrayList<ArrayList<Integer>> finalMatchingList = new ArrayList<ArrayList<Integer>>();
			for (int i = 0; i < this.n; i++) {
				for (int j = 0; j < this.n*this.m; j++) {
					if (matches[i][j].get(GRB.DoubleAttr.X) > 0.0) {
						ArrayList<Integer> match = new ArrayList<Integer>();
						match.add(i);
						match.add(j);
						finalMatchingList.add(match); 
					}
				}
			}

			return finalMatchingList;

		} catch (GRBException e)
		{
			System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
		}

		return new ArrayList<ArrayList<Integer>>();
	}

	public ArrayList<ArrayList<ArrayList<Integer>>> buildNeighborhoods(int graphNum, ArrayList<Student> students, ArrayList<School> schools, ArrayList<Affiliation> affiliations)
	{
		ArrayList<ArrayList<Integer>> studentNeighbors = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Integer>> schoolNeighbors = new ArrayList<ArrayList<Integer>>();


		for (int i = 0; i < this.n*this.m; i++) {
			studentNeighbors.add(new ArrayList<Integer>());

			if (i < this.n) {
				schoolNeighbors.add(new ArrayList<Integer>());
				
				if (affiliations.size() == 0) {
					affiliations.add(new Affiliation(i, schools.get(i), new ArrayList<Student>()));
				}
			}
		}

		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.n*this.m; j++) {
				Student student = students.get(j);
				School school = schools.get(i);
				School school2 = student.getSchool();

				boolean inH0 = school.hasAffiliate(student)
								&& student.checkAgent(school)
								&& school.checkAgent(student)
								&& school.checkSchool(school2, student);
				boolean inH1 = (!school.hasAffiliate(student))
								&& student.checkAgent(school)
								&& school.checkAgent(student);
				boolean inH2 = school.hasAffiliate(student)
								&& student.checkAgent(school)
								&& (school.checkAgent(student)
								^ school.checkSchool(school2, student));
				boolean inH4 = (!school.hasAffiliate(student))
								&& school2.checkSchool(school, student)
								&& ((!student.checkAgent(school))
									|| !school.checkAgent(student));

				switch (graphNum) {
					case 0 :
						if (inH0) {
							schoolNeighbors.get(i).add(j);		
							studentNeighbors.get(j).add(i);
						}
						break;
					case 1 :
						if (inH1) {
							schoolNeighbors.get(i).add(j);		
							studentNeighbors.get(j).add(i);
						} else if (inH0) {
							affiliations.get(i).addAffiliate(student);
							school.reduceCapacity();
						}
						break;
					case 2 :
						if (inH2) {
							schoolNeighbors.get(i).add(j);		
							studentNeighbors.get(j).add(i);
						}
						break;
					//case 3 :
					//	if (this.schools[j].hasAffiliate(students[i])
					//		&& this.students[i].checkAgent(j)
					//		&& (!this.schools[j].checkAgent(i))
					//		&& this.schools[j].checkSchool(j, i)) {
					//		g.addEdge(i, j);
					//		//System.out.println("(" + i + "," + j + ")" + " - " + 3);
					//	}
					//	break;
					case 4 :
						if (inH4) {
							schoolNeighbors.get(i).add(j);		
							studentNeighbors.get(j).add(i);
						} else if (inH2) {
							affiliations.get(i).addAffiliate(student);
							school.reduceCapacity();
						}
						break;
					default :
						break;
				}
			}
		}

		ArrayList<ArrayList<ArrayList<Integer>>> neighborhoods = new ArrayList<ArrayList<ArrayList<Integer>>>();
		neighborhoods.add(studentNeighbors);
		neighborhoods.add(schoolNeighbors);
	
		return neighborhoods;
	}

	public ArrayList<Student> copyStudents(ArrayList<Student> oldStudents)
	{
		ArrayList<Student> students = new ArrayList<Student>();

		for (Student student : oldStudents) {
			students.add(new Student(student));
		}

		return students;
	}	

	public ArrayList<School> copySchools(ArrayList<School> oldSchools)
	{
		ArrayList<School> schools = new ArrayList<School>();

		for (School school : oldSchools) {
			schools.add(new School(school));
		}

		return schools;
	}	

    public String matchName(int i, int j)
    {
        return i + "," + j;
    }

	public String neighborhoodName(int i, String agentType)
	{
		return agentType + i;
	}

	public String maximalityConstraintName(int i, int j)
	{
		return "constraint:" + i + "," + j;
	}

	public String appName(int j)
	{
		return "" + j;
	}

	public String reserveName(int j)
	{
		return "reserve:" + j;
	}

	public String affiliationName(int i)
	{
		return "affiliation:" + i;
	}


    private static String getFinalName()
    {
        String className = getName();
        return className.substring(className.lastIndexOf('.') + 1);
    }  
    
    public static void main(String args[]) 
    {
        // Parse the command line
        Options options = new Options();

        Option size = new Option("n", "size", true, "size of instance");
        size.setRequired(true);
        options.addOption(size);

        Option affiliatesPerSchool = new Option("m", "affiliatesPerSchool", true, "the number of affiliates for each school");
        affiliatesPerSchool.setRequired(true);
        options.addOption(affiliatesPerSchool);

        Option students = new Option("s", "students", true, "student preferences input file");
        students.setRequired(false);
        options.addOption(students);

        Option schools = new Option("u", "schools", true, "school preferences input file");
        schools.setRequired(false);
        options.addOption(schools);

        Option affiliates = new Option("a", "affiliates", true, "affiliate preferences input file");
        affiliates.setRequired(false);
        options.addOption(affiliates);

        Option verify = new Option("v", "verify", false, "verify result");
        verify.setRequired(false);
        options.addOption(verify);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try
        {
            cmd = parser.parse(options, args);
        } 
        catch (ParseException e) 
        {
            System.err.println(e.getMessage());
            formatter.printHelp(getName(), options);
            System.exit(1);
        }

        int n = Integer.parseInt(cmd.getOptionValue("size"));
		int m = Integer.parseInt(cmd.getOptionValue("affiliatesPerSchool"));
        String studentFile = cmd.getOptionValue("students");
        String schoolFile = cmd.getOptionValue("schools");
		String affiliateFile = cmd.getOptionValue("affiliates");
        boolean v;
        if (cmd.hasOption("verify")) v = true;
        else v = false;

		boolean debug = false;

        Abstract_BSM_Algorithm smp = new PriorityMatchLP(n, m, studentFile, schoolFile, affiliateFile, debug);
        Marriage matching = smp.match();

        Metrics smpMetrics = new Metrics(smp, matching, getFinalName());
        //if (v) smpMetrics.perform_checks();  
        smpMetrics.printTime();
    }
}
