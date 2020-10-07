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

import cslab.ntua.gr.entities.Student;
import cslab.ntua.gr.entities.BipartiteGraph;
import cslab.ntua.gr.entities.Marriage;
import cslab.ntua.gr.entities.School;
import cslab.ntua.gr.tools.Metrics;

public class ILP extends Abstract_BSM_Algorithm
{
    private int[] kappa;
    private int[][] married;
    private Stack<Integer> singles;
    private int active_proposer;

    public ILP(int n, int m, String studentFile, String schoolFile, String affiliateFile)
    {
        super(n, m, studentFile, schoolFile, affiliateFile);
    }

    // Constructor for when agents are available
    public ILP(int n, int m, Student[] students, School[] schools)
    {
        super(n, m, students, schools);
    }
	
	public Marriage match()
	{
		long initStartTime = System.nanoTime();
		long startTime = System.nanoTime();

		try
		{
			GRBEnv env = new GRBEnv(true);
			env.set("logFile", "test.log");
			env.start();

			GRBModel model = new GRBModel(env);
			GRBVar one = model.addVar(1.0, 1.0, 0.0, GRB.BINARY, "one");
			GRBVar zero = model.addVar(0.0, 0.0, 0.0, GRB.BINARY, "zero");

			GRBVar[][] matches = new GRBVar[this.n][this.n * this.m];

			// 1 variable for each potential match
			for (int i = 0; i < this.n; i++) {
				for (int j = 0; j < this.n * this.m; j++) {
					GRBVar match = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, this.matchName(i, j));
					matches[i][j] = match;
				}
			}

			// Capacity constraints
			for (int i = 0; i < this.n; i++) {
				School school = this.schools[i];
				GRBLinExpr expr = new GRBLinExpr();

				for (int j = 0; j < this.n * this.m; j++) {
					expr.addTerm(1.0, matches[i][j]);
				}

				model.addConstr(expr, GRB.LESS_EQUAL, school.getCapacity(), this.capacityName(i, "school"));
			}

			for (int j = 0; j < this.n * this.m; j++) {
				Student student = this.students[j];
				GRBLinExpr expr = new GRBLinExpr();

				for (int i = 0; i < this.n; i++) {
					expr.addTerm(1.0, matches[i][j]);
				}

				model.addConstr(expr, GRB.LESS_EQUAL, student.getCapacity(), this.capacityName(j, "student"));
			}


			// Blocking tuple constraints
			for (int i = 0; i < this.n; i++) {
				for (int j = 0; j < this.n * this.m; j++) {
					// Blocking pairs: a, e. In this case, they are undermatched
					// and simply match together
					School school = this.schools[i];
					Student student = this.students[j];
					
					int iCap = school.getCapacity();
					int jCap = student.getCapacity();

					boolean iPos1 = school.checkAgent(j);
					boolean iPos2 = school.hasAffiliate(student) && school.checkSchool(i, j);
					boolean jPos1 = student.checkAgent(i);

					boolean[] iPoss = new boolean[]{iPos1, iPos2};
					boolean[] jPoss = new boolean[]{jPos1};
					boolean[] empty = new boolean[]{};

					if (strictlyPrefers(iPoss, 2, empty, 0) && strictlyPrefers(jPoss, 1, empty, 0)) {
						GRBLinExpr expr = new GRBLinExpr();
						expr.addTerm(iCap + jCap, matches[i][j]);

						for (int jstar = 0; jstar < this.n * this.m; jstar++) {
							expr.addTerm(1.0, matches[i][jstar]);
						}

						for (int istar = 0; istar < this.n; istar++) {
							expr.addTerm(1.0, matches[istar][j]);
						}

						model.addConstr(expr, GRB.GREATER_EQUAL, iCap + jCap, this.blockName(new int[]{i, j}, 2));
					}

					for (int i2 = 0; i2 < this.n; i2++) {
						// Blocking tuples: a, e, e'. In this case, e is undermatched
						// and a is swapping its match with e' for e. e' does not get to 
						// match with anything else.
						if (i == i2) {
							continue;
						}

						boolean iNeg1 = school.hasAffiliate(student) && school.checkSchool(i2, j);
						boolean jNeg1 = student.checkAgent(i2);

						boolean[] iNegs = new boolean[]{iNeg1};
						boolean[] jNegs = new boolean[]{jNeg1};

					if (strictlyPrefers(iPoss, 2, iNegs, 1) && strictlyPrefers(jPoss, 1, iNegs, 1)) {
							GRBLinExpr expr = new GRBLinExpr();
							expr.addTerm(iCap, matches[i][j]);
							expr.addTerm(iCap, one);
							expr.addTerm(-iCap, matches[i2][j]);

							for (int jstar = 0; jstar < this.n * this.m; jstar++) {
								expr.addTerm(1.0, matches[i][jstar]);
							}

							model.addConstr(expr, GRB.GREATER_EQUAL, iCap, this.blockName(new int[]{i, i2, j, -1}, 4));
						}
					}

					for (int j2 = 0; j2 < this.n * this.m; j2++) {
						// Blocking tuples: a, a', e. In this case, a is undermatched
						// and e is swapping its match with a' for a. a' does not get to 
						// match with anything else.
						if (j == j2) {
							continue;
						}

						Student student2 = this.students[j2];

						boolean iNeg1 = school.checkAgent(j2);
						boolean iNeg2 = school.hasAffiliate(student2) && school.checkSchool(i, j2);

						boolean[] iNegs = new boolean[]{iNeg1, iNeg2};

						if (strictlyPrefers(iPoss, 2, iNegs, 2) && strictlyPrefers(jPoss, 1, empty, 0)) {
							GRBLinExpr expr = new GRBLinExpr();
							expr.addTerm(jCap, matches[i][j]);
							expr.addTerm(jCap, one);
							expr.addTerm(-jCap, matches[i][j2]);

							for (int istar = 0; istar < this.n; istar++) {
								expr.addTerm(1.0, matches[istar][j]);
							}

							model.addConstr(expr, GRB.GREATER_EQUAL, jCap, this.blockName(new int[]{i, -1, j, j2}, 4));
						}

						for (int i2 = 0; i2 < this.n; i2++) {
							// Blocking tuples: a, a', e, e'. In this case, a and e
							// both swap their matches. Then a' and e' do NOT match
							// (them matching is a later case)

							if (i == i2) {
								continue;
							}

							School school2 = this.schools[i2];
							
							boolean iNeg3 = school.hasAffiliate(student) && school.checkSchool(i2, j);
							boolean jNeg1 = student.checkAgent(i2);

							iNegs = new boolean[]{iNeg1, iNeg2, iNeg3};
							boolean[] jNegs = new boolean[]{jNeg1};
							
							if (strictlyPrefers(iPoss, 2, iNegs, 3) && strictlyPrefers(jPoss, 1, jNegs, 1)) {
								GRBLinExpr expr = new GRBLinExpr();
								expr.addTerm(1.0, matches[i][j]);
								expr.addTerm(1.0, one);
								expr.addTerm(-1.0, matches[i][j2]);
								expr.addTerm(1.0, one);
								expr.addTerm(-1.0, matches[i2][j]);

								model.addConstr(expr, GRB.GREATER_EQUAL, 1.0, this.blockName(new int[]{i, i2, j, j2}, 4));
							}

							for (int i3 = 0; i3 < this.n; i3++) {
								if (i == i3) {
									continue;
								}

								School school3 = this.schools[i3];
								int i3Cap = school3.getCapacity();

								boolean iPos3 = school.hasAffiliate(student2) && school.checkSchool(i3, j2);
								boolean j2Pos1 = student2.checkAgent(i3);
								boolean i3Pos1 = school3.checkAgent(j2);
								boolean i3Pos2 = school3.hasAffiliate(student2) && school3.checkSchool(i3, j2);

								iPoss = new boolean[]{iPos1, iPos2, iPos3};
								boolean[] j2Poss = new boolean[]{j2Pos1};
								boolean[] i3Poss = new boolean[]{i3Pos1, i3Pos2};

								if (strictlyPrefers(iPoss, 3, iNegs, 3) && strictlyPrefers(jPoss, 1, jNegs, 1)
									&& strictlyPrefers(j2Poss, 1, empty, 0) && strictlyPrefers(i3Poss, 2, empty, 0)) {
									GRBLinExpr expr = new GRBLinExpr();
									expr.addTerm(i3Cap, matches[i][j]);
									expr.addTerm(i3Cap, one);
									expr.addTerm(-i3Cap, matches[i][j2]);
									expr.addTerm(i3Cap, one);
									expr.addTerm(-i3Cap, matches[i2][j]);
									expr.addTerm(i3Cap, matches[i3][j2]);

									for (int jstar = 0; jstar < this.n * this.m; jstar++) {
										expr.addTerm(1.0, matches[i3][jstar]);
									}

									model.addConstr(expr, GRB.GREATER_EQUAL, i3Cap, this.blockName(new int[]{i, i2, i3, j, j2, -1}, 6));
								}
							}


							for (int j3 = 0; j3 < this.n * this.m; j3++) {
								if (j == j3) {
									continue;
								}

								Student student3 = this.students[j3];
								int j3Cap = student3.getCapacity();

								boolean iPos3 = school.hasAffiliate(student3) && school.checkSchool(i2, j3);
								boolean i2Pos1 = school2.checkAgent(j3);
								boolean i2Pos2 = school2.hasAffiliate(student3) && school2.checkSchool(i2, j3);
								boolean j3Pos1 = student3.checkAgent(i2);

								iPoss = new boolean[]{iPos1, iPos2, iPos3};
								boolean[] i2Poss = new boolean[]{i2Pos1, i2Pos2};
								boolean[] j3Poss = new boolean[]{j3Pos1};

								if (strictlyPrefers(iPoss, 3, iNegs, 3) && strictlyPrefers(jPoss, 1, jNegs, 1)
									&& strictlyPrefers(i2Poss, 2, empty, 0) && strictlyPrefers(j3Poss, 1, empty, 0)) {
									GRBLinExpr expr = new GRBLinExpr();
									expr.addTerm(j3Cap, matches[i][j]);
									expr.addTerm(j3Cap, one);
									expr.addTerm(-j3Cap, matches[i][j2]);
									expr.addTerm(j3Cap, one);
									expr.addTerm(-j3Cap, matches[i2][j]);
									expr.addTerm(j3Cap, matches[i2][j3]);

									for (int istar = 0; istar < this.n; istar++) {
										expr.addTerm(1.0, matches[istar][j3]);
									}

									model.addConstr(expr, GRB.GREATER_EQUAL, j3Cap, this.blockName(new int[]{i, i2, -1, j, j2, j3}, 6));
								}

								for (int i3 = 0; i3 < this.n; i3++) {
									if (i == i3) {
										continue;
									}

									School school3 = this.schools[i3];
									int i3Cap = school3.getCapacity();

									
									boolean iPos4 = school.hasAffiliate(student2) && school.checkSchool(i3, j2);
									boolean j2Pos1 = student2.checkAgent(i3);
									boolean i3Pos1 = school3.checkAgent(j2);
									boolean i3Pos2 = school3.hasAffiliate(student2) && school3.checkSchool(i3, j2);

									iPoss = new boolean[]{iPos1, iPos2, iPos3, iPos4};
									boolean[] j2Poss = new boolean[]{j2Pos1};
									boolean[] i3Poss = new boolean[]{i3Pos1, i3Pos2};

									if (strictlyPrefers(iPoss, 4, iNegs, 3) && strictlyPrefers(jPoss, 1, jNegs, 1)
										&& strictlyPrefers(i2Poss, 2, empty, 0) && strictlyPrefers(j2Poss, 1, empty, 0)
										&& strictlyPrefers(i3Poss, 2, empty, 0) && strictlyPrefers(j3Poss, 1, empty, 0)) {
										GRBLinExpr expr = new GRBLinExpr();
										expr.addTerm(i3Cap+j3Cap, matches[i][j]);
										expr.addTerm(i3Cap+j3Cap, one);
										expr.addTerm(-i3Cap-j3Cap, matches[i][j2]);
										expr.addTerm(i3Cap+j3Cap, one);
										expr.addTerm(-i3Cap-j3Cap, matches[i2][j]);
										expr.addTerm(i3Cap+j3Cap, matches[i3][j2]);
										expr.addTerm(i3Cap+j3Cap, matches[i2][j3]);

										for (int istar = 0; istar < this.n; istar++) {
											expr.addTerm(1.0, matches[istar][j3]);
										}

										for (int jstar = 0; jstar < this.n * this.m; jstar++) {
											expr.addTerm(1.0, matches[i3][jstar]);
										}

										model.addConstr(expr, GRB.GREATER_EQUAL, i3Cap+j3Cap, this.blockName(new int[]{i, i2, i3, j, j2, j3}, 6));
									}
								}
							}
						}
					}
				}
			}
			
			// RUNNING MODEL
			startTime = System.nanoTime();
		} catch (GRBException e)
		{
			System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
		}
		
		ArrayList<ArrayList<Integer>> finalMatching = new ArrayList<ArrayList<Integer>>();
		int[][] matching = new int[finalMatching.size()][2];
		for (int i = 0; i < finalMatching.size(); i++) {
			matching[i][0] = finalMatching.get(i).get(0);
			matching[i][1] = finalMatching.get(i).get(1);
			//System.out.println("Matching " + matching[i][0] + " with " + matching[i][1]);
		}

		long endTime = System.nanoTime();
		long elapsedTime = endTime - startTime;
		long elapsedTimeWithInit = endTime - initStartTime;
		time = elapsedTime / 1.0E09;

		System.out.println("ElapsedTime: " + (elapsedTime / 1.0E09) + " seconds");
		System.out.println("ElapsedTime with init: " + (elapsedTimeWithInit / 1.0E09) + " seconds");

		return new Marriage(this.n, matching);
	}

	public boolean strictlyPrefers(boolean[] positives, int nPos, boolean[] negatives, int nNeg)
	{
		int sum = 0;

		for (int i = 0; i < nPos; i++) {
			int pos = positives[i] ? 1 : 0;
			sum += pos;
		}

		for (int i = 0; i < nNeg; i++) {
			int neg = negatives[i] ? 1 : 0;
			sum -= neg;
		}

		return sum > 0;
	}

	public String matchName(int i, int j)
	{
		return i + "," + j;
	}

	public String capacityName(int i, String type)
	{
		return type + "_cap_" + i;
	}

	public String blockName(int[] tup, int len)
	{
		String name = "tuple:";

		for (int i = 0; i < len; i++) {
			name += tup[i] + ",";
		}

		if (len % 2 == 1) {
			name += "e,";
		}
		
		return name;
	}

	public void printMatching(ArrayList<ArrayList<Integer>> matching)
	{
		String matches = "Matches: ";

		for (ArrayList<Integer> match : matching) {
			matches += "(" + match.get(0) + ", " + match.get(1) + "), ";
		}
		
		System.out.println(matches);
	}


    private static String getFinalName()
    {
        String className = getName();
        return className.substring(className.lastIndexOf('.') + 1);
    }  

	public static void testGurobi()
	{
		try
		{
			GRBEnv env = new GRBEnv(true);
			env.set("logFile", "test.log");
			env.start();

			// Create empty model
			GRBModel model = new GRBModel(env);
			
			// Create variables
			GRBVar x = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "x");
			GRBVar y = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "y");
			GRBVar z = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "z");

			// Set objective: maximize x
		} catch (GRBException e)
		{
			System.out.println("Error code: " + e.getErrorCode() + ". " + e.getMessage());
		}
		
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

		//testGurobi();
		
        Abstract_BSM_Algorithm smp = new ILP(n, m, studentFile, schoolFile, affiliateFile);
        Marriage matching = smp.match();

        Metrics smpMetrics = new Metrics(smp, matching, getFinalName());
        //if (v) smpMetrics.perform_checks();  
        smpMetrics.printTime();
    }
}
