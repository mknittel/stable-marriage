package cslab.ntua.gr.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BinaryAgent
{
    private int n, side, id;
    private int[] interestList;

    // Creates a copy of an existing agent
    public BinaryAgent(BinaryAgent copy)
    {
        this.n = copy.getN();
        this.id = copy.getID();
        this.side = copy.getSide();

        interestList = new int[n];
        int[] old_interestList = copy.getInterests();
        for (int j = 0; j < n; j++) interestList[j] = old_interestList[j];
    }

    // Creates an agent with random preferences (uniform), number selected = threshold * n
    public BinaryAgent(int n, int id, int side, double threshold)
    {
        this.n = n;
        this.id = id;
        this.side = side;

		Random r = new Random();

        interestList = new int[n];
        for (int j = 0; j < n; j++) {
			if (r.nextDouble() < threshold) {
				interestList[j] = 1;
			} else {
				interestList[j] = 0;
			}
		}
    }

    // Creates an agent with preferences read from an input file
    public BinaryAgent(int n, int id, int side, String lineWithPrefs)
    {
        this.n = n;
        this.id = id;
        this.side = side;

        interestList = new int[n];
        String[] tokens = lineWithPrefs.split("\\s+");
		System.out.println(Arrays.toString(tokens));
        for (int j = 0; j < tokens.length; j++) interestList[j] = Integer.parseInt(tokens[j]);
    }

    public boolean checkAgent(int agentNo)
    {
		return this.interestList[agentNo] == 1;
    }


    public int getN(){ return n; }
    public int getID(){ return id; }
    public int getSide(){ return side; }
    public int[] getInterests(){ return interestList; }
}
