package cslab.ntua.gr.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BinaryAgent
{
    private int n, m, side, id, capacity;
    private ArrayList<Integer> interestList;

	public BinaryAgent() { }

    // Creates a copy of an existing agent
    public BinaryAgent(BinaryAgent copy)
    {
        this.n = copy.getN();
        this.id = copy.getID();
        this.side = copy.getSide();
		this.capacity = copy.getCapacity();

        interestList = new ArrayList<Integer>();
        ArrayList<Integer> old_interestList = copy.getInterests();
        for (int interest : old_interestList) interestList.add(interest);
    }

    // Creates an agent with preferences read from an input file
    public BinaryAgent(int n, int m, int id, int side, String lineWithPrefs)
    {
        this.n = n;
		this.m = m;
        this.id = id;
        this.side = side;

        interestList = new ArrayList<Integer>();
        String[] tokens = lineWithPrefs.split("\\s+");
		
		this.capacity = Integer.parseInt(tokens[0]);
        for (int j = 1; j < tokens.length; j++) interestList.add(Integer.parseInt(tokens[j]));
    }
	
	public void setCapacity(int cap)
	{
		this.capacity = cap;
	}

    public boolean checkAgent(int agentNo)
    {
		return this.interestList.get(agentNo) == 1;
    }

	public boolean checkAgent(BinaryAgent agent)
	{
		int agentNo = agent.getID();
		return this.interestList.get(agentNo) == 1;
    }

	public void reduceCapacity() {
		this.capacity--;
	}


    public int getN(){ return n; }
    public int getID(){ return id; }
    public int getSide(){ return side; }
    public int getCapacity(){ return capacity; }
    public ArrayList<Integer> getInterests(){ return interestList; }
}
