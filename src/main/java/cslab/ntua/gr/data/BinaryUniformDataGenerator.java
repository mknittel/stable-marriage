package cslab.ntua.gr.data;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Collections;
import java.util.LinkedList;

public class BinaryUniformDataGenerator extends BinaryDataGenerator
{
	protected double threshold;
	protected int capacity;
	
	public BinaryUniformDataGenerator(int n, int m, double threshold, int capacity)
	{
		super(n, m);
		this.threshold = threshold;
		this.capacity = capacity;
	}
	
	@Override
	protected LinkedList<Integer> line(String whichFile)
	{
		LinkedList<Integer> res = new LinkedList<Integer>();
		int lineLength = this.n;

		if (whichFile == "school") {
			lineLength = this.n  * this.m;
		}

		for (int i = 0; i < lineLength; i++) {
			if (i >= this.threshold * lineLength) {
				res.add(1);
			} else {
				res.add(0);
			}
		}
		Collections.shuffle(res);

		if (whichFile == "student") {
			res.add(0, this.capacity);
		} else if (whichFile == "school") {
			res.add(0, this.capacity * this.m);
		}
		return res;
	}
	
	public static void main(String[] args) 
	{
		Options options = new Options();

		Option numSchools = new Option("n", "numSchools", true, "number of schools");
		numSchools.setRequired(true);
		options.addOption(numSchools);

		Option affPerSchool = new Option("m", "affPerSchool", true, "number of affiliations per school");
		affPerSchool.setRequired(true);
		options.addOption(affPerSchool);

		Option studentFile = new Option("s", "studentFile", true, "output file for student data");
		studentFile.setRequired(true);
		options.addOption(studentFile);

		Option schoolFile = new Option("u", "schoolFile", true, "output file for school own preference data");
		schoolFile.setRequired(true);
		options.addOption(schoolFile);

		Option affiliateFile = new Option("a", "affiliateFile", true, "output file for school preferences by affiliate");
		affiliateFile.setRequired(true);
		options.addOption(affiliateFile);

		Option threshold = new Option("t", "threshold", true, "threshold for uniform sampling");
		threshold.setRequired(true);
		options.addOption(threshold);

		Option capacity = new Option("c", "capacity", true, "capacity for students (*m for schools)");
		capacity.setRequired(true);
		options.addOption(capacity);

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
			System.exit(1);
		}

		int n = Integer.parseInt(cmd.getOptionValue("numSchools"));
		int m = Integer.parseInt(cmd.getOptionValue("affPerSchool"));
		double t = Double.parseDouble(cmd.getOptionValue("threshold")) / 100;
		int c = Integer.parseInt(cmd.getOptionValue("capacity"));
		String studentFileName = cmd.getOptionValue("studentFile");
		String schoolFileName = cmd.getOptionValue("schoolFile");
		String affiliateFileName = cmd.getOptionValue("affiliateFile");

		System.out.println("n: " + n + ", m: " + m + ", t: " + t + ", c: " + c);
			

		BinaryDataGenerator gen = new BinaryUniformDataGenerator(n, m, t, c);
		gen.setOutputFiles(studentFileName, schoolFileName, affiliateFileName);
		gen.create("student");
		gen.create("school");
		gen.create("affiliate");
	}
}
