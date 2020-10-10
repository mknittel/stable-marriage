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

public class BinarySatSolver extends Abstract_BSM_Algorithm
{
    private int[] kappa;
    private int[][] married;
    private Stack<Integer> singles;
    private int active_proposer;

    public BinarySatSolver(int n, int m, String studentFile, String schoolFile, String affiliateFile)
    {
        super(n, m, studentFile, schoolFile, affiliateFile);
    }

    // Constructor for when agents are available
    public BinarySatSolver(int n, int m, Student[] students, School[] schools)
    {
        super(n, m, students, schools);
    }
	
	public Marriage match()
	{
		return new Marriage(this.n, new int[n][2]);
	}

	public void printMatching(ArrayList<ArrayList<Integer>> matching)
	{
		String matches = "Matches: ";

		for (ArrayList<Integer> match : matching) {
			matches += "(" + match.get(0) + ", " + match.get(1) + "), ";
		}
		
		System.out.println(matches);
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

        Abstract_BSM_Algorithm smp = new BinarySatSolver(n, m, studentFile, schoolFile, affiliateFile);
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
