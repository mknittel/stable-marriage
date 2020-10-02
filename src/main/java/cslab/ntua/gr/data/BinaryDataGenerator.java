package cslab.ntua.gr.data;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

public abstract class BinaryDataGenerator 
{

	protected int n;
	protected int m;
	protected PrintStream studentOut = System.out;
	protected PrintStream schoolOut = System.out;
	protected PrintStream affiliateOut = System.out;
	
	
	private List<LinkedList<Integer>> studentBuffer;
	private List<LinkedList<Integer>> schoolBuffer;
	private List<LinkedList<Integer>> affiliateBuffer;
	private int flushThr;
	
	public BinaryDataGenerator(int n, int m)
	{
		this.n = n;
		this.m = m;
		this.studentBuffer = new LinkedList<LinkedList<Integer>>();
		this.schoolBuffer = new LinkedList<LinkedList<Integer>>();
		this.affiliateBuffer = new LinkedList<LinkedList<Integer>>();
		this.flushThr = 100;
	}

	/**
	 * Set the output file that will be created. If no file is specified, the default output will be used
	 * (stdout).
	 * @param fileName
	 */
	public void setOutputFiles(String studentFile, String schoolFile, String affiliateFile)
	{
		try 
		{
			this.studentOut = new PrintStream(studentFile);
			this.schoolOut = new PrintStream(schoolFile);
			this.affiliateOut = new PrintStream(affiliateFile);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	protected abstract LinkedList<Integer> line(String whichFile);
	
	/**
	 * Creates the dataset.
	 */
	public void create(String whichFile)
	{
		PrintStream out = System.out;
		List<LinkedList<Integer>> buffer = new LinkedList<LinkedList<Integer>>();
		int fileLength = this.n;

		if (whichFile == "student") {
			out = studentOut;
			buffer = studentBuffer;
			fileLength = this.n * this.m;
		} else if (whichFile == "school") {
			out = schoolOut;
			buffer = schoolBuffer;
		} else if (whichFile == "affiliate") {
			out = affiliateOut;
			buffer = affiliateBuffer;
			fileLength = this.n * this.m;
		} else {
			System.out.println("Incorrect file name");
			return;
		}

		for (int i = 0; i < fileLength; i++)
		{
			if (i % this.flushThr == 0) 
				this.flushBuffer(buffer, out);
			buffer.add(this.line(whichFile));
		}
		if (!buffer.isEmpty())
			this.flushBuffer(buffer, out);
		out.close();
	}
	
	private void flushBuffer(List<LinkedList<Integer>> buffer, PrintStream out)
	{
		StringBuilder str = new StringBuilder();
		for (List<Integer> l : buffer)
		{
			for (Integer d : l)
				str.append(d.toString() + " ");
			str.append("\n");
		}
		
		buffer.clear();
		out.print(str.toString());
	}
}
