package cslab.ntua.gr.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

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

public class PriorityMatch extends Abstract_BSM_Algorithm
{
    private int[] kappa;
    private int[][] married;
    private Stack<Integer> singles;
    private int active_proposer;
	private boolean debug;

    public PriorityMatch(int n, int m, String studentFile, String schoolFile, String affiliateFile, boolean debug)
    {
        super(n, m, studentFile, schoolFile, affiliateFile);
		this.debug = debug;
    }

    // Constructor for when agents are available
    public PriorityMatch(int n, int m, Student[] students, School[] schools)
    {
        super(n, m, students, schools);
		this.debug = debug;
    }
	
	public Marriage match()
	{
		long startTime = System.nanoTime();

		BipartiteGraph g = new BipartiteGraph(this.students, this.schools); // base graph, we remove vertices as they are matched

		ArrayList<ArrayList<Integer>> finalMatching = new ArrayList<ArrayList<Integer>>();

		if (this.debug) System.out.println("Graph 0");
		BipartiteGraph g0 = new BipartiteGraph(g);
		this.constructSubgraph(g0, 0);
		if (this.debug) g0.print();

		HashMap<Integer, ArrayList<ArrayList<Student>>> neighborhoods = new HashMap<Integer, ArrayList<ArrayList<Student>>>();

		for (School school : this.schools) {
			int schoolID = school.getID();
			neighborhoods.put(schoolID, new ArrayList<ArrayList<Student>>());
			neighborhoods.get(schoolID).add(g0.getSchoolNeighborhood(schoolID));
		}

		//ArrayList<ArrayList<Integer>> matching0 = g0.greedyMaximalMatching();
		//this.printMatching(matching0);
		//finalMatching.addAll(matching0);

		//g.removeMatchedVertices(matching0);

		if (this.debug) System.out.println("Graph 1");
		BipartiteGraph g1 = new BipartiteGraph(g);
		this.constructSubgraph(g1, 1);
		g1.addAffiliations(neighborhoods);
		if (this.debug) g1.print();

		ArrayList<ArrayList<Integer>> matching1 = g1.greedyReservedMaximalMatching();
		if (this.debug) this.printMatching(matching1);
		finalMatching.addAll(matching1);

		g.removeMatchedVertices(matching1);

		if (this.debug) System.out.println("Graph 2");
		BipartiteGraph g2 = new BipartiteGraph(g);
		this.constructSubgraph(g2, 2);
		if (this.debug) g2.print();

		for (School school : g2.getSchools()) {
			int schoolID = school.getID();
			neighborhoods.put(schoolID, new ArrayList<ArrayList<Student>>());
			neighborhoods.get(schoolID).add(g2.getSchoolNeighborhood(schoolID));
		}

		//ArrayList<ArrayList<Integer>> matching2 = g2.greedyMaximalMatching();
		//this.printMatching(matching2);
		//finalMatching.addAll(matching2);

		//g.removeMatchedVertices(matching2);

		if (this.debug) System.out.println("Graph 3");
		BipartiteGraph g3 = new BipartiteGraph(g);
		this.constructSubgraph(g3, 3);
		if (this.debug) g3.print();

		for (School school : g3.getSchools()) {
			int schoolID = school.getID();
			neighborhoods.put(schoolID, new ArrayList<ArrayList<Student>>());
			neighborhoods.get(schoolID).add(g3.getSchoolNeighborhood(schoolID));
		}

		//ArrayList<ArrayList<Integer>> matching3 = g3.greedyMaximalMatching();
		//this.printMatching(matching3);
		//finalMatching.addAll(matching3);

		//g.removeMatchedVertices(matching3);

		if (this.debug) System.out.println("Graph 4");
		BipartiteGraph g4 = new BipartiteGraph(g);
		this.constructSubgraph(g4, 4);
		g4.addAffiliations(neighborhoods);
		if (this.debug) g4.print();

		ArrayList<ArrayList<Integer>> matching4 = g4.greedyReservedMaximalMatching();
		if (this.debug) this.printMatching(matching4);
		finalMatching.addAll(matching4);

		g.removeMatchedVertices(matching4);

		if (this.debug) System.out.println("Graph 0 again");
		g0 = new BipartiteGraph(g);
		this.constructSubgraph(g0, 0);
		if (this.debug) g0.print();
		
		ArrayList<ArrayList<Integer>> matching0 = g0.greedyMaximalMatching();
		if (this.debug) this.printMatching(matching0);
		finalMatching.addAll(matching0);

		g.removeMatchedVertices(matching0);

		if (this.debug) System.out.println("Graph 2 again");
		g2 = new BipartiteGraph(g);
		this.constructSubgraph(g2, 2);
		if (this.debug) g2.print();

		ArrayList<ArrayList<Integer>> matching2 = g2.greedyMaximalMatching();
		if (this.debug) this.printMatching(matching2);
		finalMatching.addAll(matching2);

		g.removeMatchedVertices(matching2);

		if (this.debug) System.out.println("Graph 3 again");
		g3 = new BipartiteGraph(g);
		this.constructSubgraph(g3, 3);
		if (this.debug) g3.print();

		ArrayList<ArrayList<Integer>> matching3 = g3.greedyMaximalMatching();
		if (this.debug) this.printMatching(matching3);
		finalMatching.addAll(matching3);

		g.removeMatchedVertices(matching3);

		if (this.debug) System.out.println("Remaining verts:");
		if (this.debug) g.print();

		if (this.debug) System.out.println("Final matching:");
		if (this.debug) this.printMatching(finalMatching);

		for (int i = 0; i < finalMatching.size(); i++) {
			int a = finalMatching.get(i).get(0);
			int b = finalMatching.get(i).get(1);

			finalMatching.get(i).set(0, b);
			finalMatching.get(i).set(1, a);
		}

		int[][] matching = new int[finalMatching.size()][2];
		for (int i = 0; i < finalMatching.size(); i++) {
			matching[i][0] = finalMatching.get(i).get(0);
			matching[i][1] = finalMatching.get(i).get(1);
			if (this.debug) System.out.println("Matching " + matching[i][0] + " with " + matching[i][1]);
		}

		long endTime = System.nanoTime();
		long elapsedTime = endTime - startTime;
		time = elapsedTime / 1.0E09;

		this.computeFairnessMetrics(finalMatching);

		return new Marriage(this.n, matching);
	}

	public void printMatching(ArrayList<ArrayList<Integer>> matching)
	{
		String matches = "Matches: ";

		for (ArrayList<Integer> match : matching) {
			matches += "(" + match.get(0) + ", " + match.get(1) + "), ";
		}
		
		System.out.println(matches);
	}


	public void constructSubgraph(BipartiteGraph g, int graphNum)
	{
		for (int i = 0; i < this.n * this.m; i++) {
			//System.out.println(Arrays.toString(this.students[i].getInterests()));
			//System.out.println(Arrays.toString(this.schools[i].getInterests()));
			//System.out.println(Arrays.toString(this.schools[i].getAffiliateInterests()));

			for (int j = 0; j < this.n; j++) {
				int k = this.students[i].getSchool().getID();

				switch (graphNum) {
					case 0 :
						if (this.schools[j].hasAffiliate(students[i])
							&& this.students[i].checkAgent(j)
					 		&& this.schools[j].checkAgent(i)
				   	 		&& this.schools[j].checkSchool(j, i)) {
							g.addEdge(i, j);
							//System.out.println("(" + i + "," + j + ")" + " - " + 0);
						}
						break;
					case 1 :
						if ((!this.schools[j].hasAffiliate(students[i]))
							&& this.students[i].checkAgent(j)
							&& this.schools[j].checkAgent(i)) {
							g.addEdge(i, j);
							//System.out.println("(" + i + "," + j + ")" + " - " + 1);
						}
						break;
					case 2 :
						if (this.schools[j].hasAffiliate(students[i])
							&& this.students[i].checkAgent(j)
							&& this.schools[j].checkAgent(i)
							&& !this.schools[j].checkSchool(j, i)) {
							g.addEdge(i, j);
							//System.out.println("(" + i + "," + j + ")" + " - " + 2);
						}
						break;
					case 3 :
						if (this.schools[j].hasAffiliate(students[i])
							&& this.students[i].checkAgent(j)
							&& (!this.schools[j].checkAgent(i))
							&& this.schools[j].checkSchool(j, i)) {
							g.addEdge(i, j);
							//System.out.println("(" + i + "," + j + ")" + " - " + 3);
						}
						break;
					case 4 :
						if ((!this.schools[j].hasAffiliate(students[i]))
							&& this.schools[k].checkSchool(j, i)
							&& ((!this.students[i].checkAgent(j))
								|| !this.schools[j].checkAgent(i))) {
							g.addEdge(i, j);
							//System.out.println("(" + i + "," + j + ")" + " - " + 4);
						}
						break;
					default :
						break;
				}
			}
		}
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

        Abstract_BSM_Algorithm smp = new PriorityMatch(n, m, studentFile, schoolFile, affiliateFile, debug);
        Marriage matching = smp.match();

        Metrics smpMetrics = new Metrics(smp, matching, getFinalName());
        //if (v) smpMetrics.perform_checks();  
        smpMetrics.printBinaryPerformance();
    }
}
