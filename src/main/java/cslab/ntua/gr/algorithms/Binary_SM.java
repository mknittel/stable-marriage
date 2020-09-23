package cslab.ntua.gr.algorithms;

import java.util.Stack;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import cslab.ntua.gr.entities.Agent;
import cslab.ntua.gr.entities.Marriage;
import cslab.ntua.gr.tools.Metrics;

// men == students, women == schools
public class Binary_SM extends Abstract_SM_Algorithm
{
    private int[] kappa;
    private int[][] married;
    private Stack<Integer> singles;
    private int active_proposer;

    public Binary_SM(int n, String menFileName, String womenFileName)
    {
        super(n, menFileName, womenFileName);
    }

    // Constructor for when agents are available
    public Binary_SM(int n, Agent[][] agents)
    {
        super(n, agents);
    }

    public Marriage match()
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
    
    public static void main(String args[]) 
    {
        // Parse the command line
        Options options = new Options();

        Option size = new Option("n", "size", true, "size of instance");
        size.setRequired(true);
        options.addOption(size);

        Option men = new Option("m", "men", true, "men preferences input file");
        men.setRequired(false);
        options.addOption(men);

        Option women = new Option("w", "women", true, "women preferences input file");
        women.setRequired(false);
        options.addOption(women);

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
        String menFile = cmd.getOptionValue("men");
        String womenFile = cmd.getOptionValue("women");
        boolean v;
        if (cmd.hasOption("verify")) v = true;
        else v = false;

        Abstract_SM_Algorithm smp = new Binary_SM(n, menFile, womenFile);
        Marriage matching = smp.match();
        Metrics smpMetrics = new Metrics(smp, matching, getFinalName());
        if (v) smpMetrics.perform_checks();  
        smpMetrics.printPerformance();
    }
}
