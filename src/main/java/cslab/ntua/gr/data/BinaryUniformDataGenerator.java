package cslab.ntua.gr.data;

import java.util.Collections;
import java.util.LinkedList;

public class BinaryUniformDataGenerator extends DataGenerator
{
	protected double threshold;

	public BinaryUniformDataGenerator(int datasetSize)
	{
		super(datasetSize);
		this.threshold = 0.5;
	}
	
	
	public BinaryUniformDataGenerator(int datasetSize, int threshold)
	{
		super(datasetSize);
		this.threshold = (new Double(threshold))/100;
	}
	
	@Override
	protected LinkedList<Integer> line()
	{
		LinkedList<Integer> res = new LinkedList<Integer>();
		for (int i = 0; i < this.datasetSize; i++) {
			if (i >= this.threshold * this.datasetSize) {
				res.add(1);
			} else {
				res.add(0);
			}
		}
		Collections.shuffle(res);
		return res;
	}
	
	public static void main(String[] args) 
	{
		try 
		{
			if (args.length < 1)
			{
				System.err.println("I need size of dataset");
				System.exit(1);
			}
			
			DataGenerator gen = new BinaryUniformDataGenerator(new Integer(args[0]));
			if (args.length > 2) {
				gen = new BinaryUniformDataGenerator(new Integer(args[0]), new Integer(args[1]));
			}
			
			if (args.length > 2) gen.setOutputFile(args[2]);
			else if (args.length > 1) gen.setOutputFile(args[1]);
			gen.create();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}	
	}

}
