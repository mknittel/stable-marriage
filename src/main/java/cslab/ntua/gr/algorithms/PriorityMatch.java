package cslab.ntua.gr.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import cslab.ntua.gr.entities.BinaryAgent;
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

    public PriorityMatch(int n, String studentFile, String schoolFile, String affiliateFile)
    {
        super(n, studentFile, schoolFile, affiliateFile);
    }

    // Constructor for when agents are available
    public PriorityMatch(int n, BinaryAgent[] students, School[] schools)
    {
        super(n, students, schools);
    }
	
	public Marriage match()
	{
		BipartiteGraph g = new BipartiteGraph(this.students, this.schools); // base graph, we remove vertices as they are matched

		ArrayList<ArrayList<Integer>> finalMatching = new ArrayList<ArrayList<Integer>>();

		System.out.println("Graph 0");
		BipartiteGraph g0 = new BipartiteGraph(g);
		this.constructSubgraph(g0, 0);
		g0.print();

		ArrayList<ArrayList<Integer>> matching0 = g0.greedyMaximalMatching();
		this.printMatching(matching0);
		finalMatching.addAll(matching0);

		g.removeMatchedVertices(matching0);

		System.out.println("Graph 1");
		BipartiteGraph g1 = new BipartiteGraph(g);
		this.constructSubgraph(g1, 1);
		g1.print();

		ArrayList<ArrayList<Integer>> matching1 = g1.greedyMaximalMatching();
		this.printMatching(matching1);
		finalMatching.addAll(matching1);

		g.removeMatchedVertices(matching1);

		System.out.println("Graph 2");
		BipartiteGraph g2 = new BipartiteGraph(g);
		this.constructSubgraph(g2, 2);
		g2.print();

		ArrayList<ArrayList<Integer>> matching2 = g2.greedyMaximalMatching();
		this.printMatching(matching2);
		finalMatching.addAll(matching2);

		g.removeMatchedVertices(matching2);

		System.out.println("Graph 3");
		BipartiteGraph g3 = new BipartiteGraph(g);
		this.constructSubgraph(g3, 3);
		g3.print();

		ArrayList<ArrayList<Integer>> matching3 = g3.greedyMaximalMatching();
		this.printMatching(matching3);
		finalMatching.addAll(matching3);

		g.removeMatchedVertices(matching3);

		System.out.println("Graph 4");
		BipartiteGraph g4 = new BipartiteGraph(g);
		this.constructSubgraph(g4, 4);
		g4.print();

		ArrayList<ArrayList<Integer>> matching4 = g4.greedyMaximalMatching();
		this.printMatching(matching4);
		finalMatching.addAll(matching4);

		g.removeMatchedVertices(matching4);

		System.out.println("Remaining verts:");
		g.print();

		System.out.println("Final matching:");
		this.printMatching(finalMatching);

		int[][] matching = new int[finalMatching.size()][2];
		for (int i = 0; i < finalMatching.size(); i++) {
			matching[i][0] = finalMatching.get(i).get(0);
			matching[i][1] = finalMatching.get(i).get(1);
			System.out.println("Matching " + matching[i][0] + " with " + matching[i][1]);
		}

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
		for (int i = 0; i < this.n; i++) {
			//System.out.println(Arrays.toString(this.students[i].getInterests()));
			//System.out.println(Arrays.toString(this.schools[i].getInterests()));
			//System.out.println(Arrays.toString(this.schools[i].getAffiliateInterests()));

			for (int j = 0; j < this.n; j++) {
				switch (graphNum) {
					case 0 :
						if (i == j
							&& this.students[i].checkAgent(j)
					 		&& this.schools[j].checkAgent(i)
				   	 		&& this.schools[j].checkSchool(j)) {
							g.addEdge(i, j);
							//System.out.println("(" + i + "," + j + ")" + " - " + 0);
						}
						break;
					case 1 :
						if (i != j
							&& this.students[i].checkAgent(j)
							&& this.schools[j].checkAgent(i)) {
							g.addEdge(i, j);
							//System.out.println("(" + i + "," + j + ")" + " - " + 1);
						}
						break;
					case 2 :
						if (i == j
							&& this.students[i].checkAgent(j)
							&& this.schools[j].checkAgent(i)) {
							g.addEdge(i, j);
							//System.out.println("(" + i + "," + j + ")" + " - " + 2);
						}
						break;
					case 3 :
						if (i == j
							&& this.students[i].checkAgent(j)
							&& this.schools[j].checkSchool(j)) {
							g.addEdge(i, j);
							//System.out.println("(" + i + "," + j + ")" + " - " + 3);
						}
						break;
					case 4 :
						if (i != j
							&& this.schools[i].checkSchool(j)) {
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



    /*public Marriage match()
    {
        long startTime = System.nanoTime();

        // Initialize
        kappa = new int[n];
        married = new int[2][n];  
        for (int i = 0; i < n; i++)
        {
            married[0][i] = Integer.MAX_VALUE;
            married[1][i] = Integer.MAX_VALUE;
        } 
        singles = new Stack<Integer>();
        for (int i = 0; i < n; i++) singles.push(i); 

        active_proposer = singles.pop();
        // Propose
        while (true)
        {
            propose(active_proposer);
            if (active_proposer == -1)
            {
                if (singles.isEmpty()) break;
                else active_proposer = singles.pop();
            }
        }

        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        time = elapsedTime / 1.0E09;

        Marriage result = new Marriage(n, married);
        return result;
    }

    // Returns true if a proposal was issued, false otherwise
    private void propose(int proposer)
    {
        int proposeToIndex = kappa[proposer];
        if (married[1][proposer] == Integer.MAX_VALUE)
        {
            // Wants to propose
            int acceptor = agents[1][proposer].getAgentAt(proposeToIndex);
            if (evaluate(acceptor, proposer)) married[1][proposer] = proposeToIndex;
            else kappa[proposer]++;
        }
    }

    // Returns true if acceptor agrees to marry proposer
    private boolean evaluate(int acceptor, int proposer)
    {
        int proposerRank = agents[0][acceptor].getRankOf(proposer);
        int marriedToIndex = married[0][acceptor];
        if (marriedToIndex > proposerRank)
        {
            // Break up with old
            if (marriedToIndex != Integer.MAX_VALUE)
            {
                int old = agents[0][acceptor].getAgentAt(marriedToIndex);
                married[1][old] = Integer.MAX_VALUE;                
                active_proposer = old;            
            }
            else
            {
                active_proposer = -1;
            }             
            //Engage with new
            married[0][acceptor] = proposerRank;            
            return true;
        }
        else return false;
    }

    private static String getFinalName()
    {
        String className = getName();
        return className.substring(className.lastIndexOf('.') + 1);
    }  
	*/  
    
    public static void main(String args[]) 
    {
        // Parse the command line
        Options options = new Options();

        Option size = new Option("n", "size", true, "size of instance");
        size.setRequired(true);
        options.addOption(size);

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
        String studentFile = cmd.getOptionValue("students");
        String schoolFile = cmd.getOptionValue("schools");
		String affiliateFile = cmd.getOptionValue("affiliates");
        boolean v;
        if (cmd.hasOption("verify")) v = true;
        else v = false;

        Abstract_BSM_Algorithm smp = new PriorityMatch(n, studentFile, schoolFile, affiliateFile);
        Marriage matching = smp.match();

		//Agent[][] agents = new Agent[1][n];
		//for (int i = 0; i < n; i++) {
		//	agents[0][i] = new Agent(n, i, 

		//System.out.println(matching.marriageToStr());
        //Metrics smpMetrics = new Metrics(smp, matching, getFinalName());
        //if (v) smpMetrics.perform_checks();  
        //smpMetrics.printPerformance();
    }
}
