package cslab.ntua.gr.entities;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;

public class Subgraph
{
	public int n;
	public ArrayList<int[]> edges;
	public Dictionary<Integer, ArrayList<Integer>> neighbors;

    public Subgraph(int n) 
    {
        this.n = n;
        this.edges = new ArrayList<int[]>();
		this.neighbors = new Hashtable<Integer, ArrayList<Integer>>();
    }

    // For cloning
    public Subgraph(Subgraph g) 
    {
        this.n = g.n;

		this.edges = new ArrayList<int[]>();
		for (int i = 0; i < g.edges.size(); i++) {
			int[] edge = new int[2];
			edge[0] = g.edges.get(i)[0];
			edge[1] = g.edges.get(i)[1];

			this.edges.add(edge);
		}

		this.neighbors = new Hashtable<Integer, ArrayList<Integer>>();
		Enumeration<Integer> vertexEnum = g.neighbors.keys();
		
		while (vertexEnum.hasMoreElements()) {
			int vertex = vertexEnum.nextElement();
			ArrayList<Integer> vertNeighbors = new ArrayList<Integer>();
			ArrayList<Integer> old_vertNeighbors = g.neighbors.get(vertex);			

			for (int i = 0; i < old_vertNeighbors.size(); i++) {
				vertNeighbors.add(old_vertNeighbors.get(i));
			}

			this.neighbors.put(vertex, vertNeighbors);
		}
    }
}
