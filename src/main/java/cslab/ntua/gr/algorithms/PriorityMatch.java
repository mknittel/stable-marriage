package cslab.ntua.gr.algorithms;

import java.util.Stack;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import cslab.ntua.gr.entities.BinaryAgent;
import cslab.ntua.gr.entities.School;
import cslab.ntua.gr.entities.Marriage;
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
		// placeholder
		int[][] placehold = new int[1][1];
		return new Marriage(1, placehold);
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
        //Marriage matching = smp.match();
        //Metrics smpMetrics = new Metrics(smp, matching, getFinalName());
        //if (v) smpMetrics.perform_checks();  
        //smpMetrics.printPerformance();
    }
}
