package cslab.ntua.gr.entities;

import java.util.ArrayList;
import java.util.List;

public class BinaryAgent
{
    private int n, side, id;
    private List<Integer> interestList; // list of all interested agents
    private int[] inverseInterests; // binary vector, ith index is acceptability of ith agent

    // Creates a copy of an existing agent
    public BinaryAgent(BinaryAgent copy)
    {
        this.n = copy.getN();
        this.id = copy.getID();
        this.side = copy.getSide();

        interestList = new ArrayList<Integer>();
        List<Integer> old_interestList = copy.getInterests();
        for (int j = 0; j < n; j++) interestList.add(old_interestList.get(j));

        inverseInterests = new int[n];
        int[] old_inverseInterests = copy.getInvInterests();
        for (int j = 0; j < n; j++) inverseInterests[j] = old_inverseInterests[j];
    }

    // Creates an agent with random preferences (uniform), number selected = threshold * n
    public BinaryAgent(int n, int id, int side, double threshold)
    {
        this.n = n;
        this.id = id;
        this.side = side;

		int num_selected = (int) Math.round(threshold * n);

        interestList = new ArrayList<Integer>();
        for (int j = 0; j < n; j++) interestList.add(j);
        java.util.Collections.shuffle(interestList);
		interestList.subList(0, num_selected);
		

        inverseInterests = new int[n];
        for (int j = 0; j < n; j++) {
			if (interestList.contains(j)) {
				inverseInterests[j] = 1;
			} else {
				inverseInterests[j] = 0;
			}
		}
    }

    // Creates an agent with preferences read from an input file
    public BinaryAgent(int n, int id, int side, String lineWithPrefs)
    {
        this.n = n;
        this.id = id;
        this.side = side;

        interestList = new ArrayList<Integer>();
        String[] tokens = lineWithPrefs.split("\\s+");
        for (int j = 0; j < tokens.length; j++) interestList.add(Integer.parseInt(tokens[j]));

		inverseInterests = new int[n];
		for (int j = 0; j < n; j++) {
			if (interestList.contains(j)) {
				inverseInterests[j] = 1;
			} else {
				inverseInterests[j] = 0;
			}
		}
    }

    public int checkAgent(int agentNo)
    {
        if (interestList.contains(agentNo)) {
			return 1;
		} else {
			return 0;
		}
    }

	// Is a STRICTLY preferable to b?
    public boolean cmp(int a, int b)
    {
        if (inverseInterests[a] < inverseInterests[b]) return true;
        else return false;
    }

    /*public boolean prefers_first(Integer a, int b)
    {
        if (a == null) return false;
        if (b == Integer.MAX_VALUE) return true;

        if (inversePrefs[Integer.valueOf(a)] < inversePrefs[b]) return true;
        else return false;        
    }*/

    public int getN(){ return n; }
    public int getID(){ return id; }
    public int getSide(){ return side; }
    public List<Integer> getInterests(){ return interestList; }
    public int[] getInvInterests(){ return inverseInterests; }
}
