package cslab.ntua.gr.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BipartiteGraph
{
	public int n; // Number of vertices in each partition
	public ArrayList<int[]> edges;
	public ArrayList<ArrayList<ArrayList<Integer>>> neighbors;

    public BipartiteGraph(int n) 
    {
        this.n = n;
        this.edges = new ArrayList<int[]>();
		this.neighbors = new ArrayList<ArrayList<ArrayList<Integer>>>();

		for (int i = 0; i < n; i++) {
			this.neighbors.add(new ArrayList<ArrayList<Integer>>());
			this.neighbors.get(i).add(new ArrayList<Integer>());
			this.neighbors.get(i).add(new ArrayList<Integer>());
		}
    }

    // For cloning
    public BipartiteGraph(BipartiteGraph g) 
    {
        this.n = g.n;

		this.edges = new ArrayList<int[]>();
		for (int i = 0; i < g.edges.size(); i++) {
			int[] edge = new int[2];
			edge[0] = g.edges.get(i)[0];
			edge[1] = g.edges.get(i)[1];

			this.edges.add(edge);
		}

		this.neighbors = new ArrayList<ArrayList<ArrayList<Integer>>>();
		
		for (int i = 0; i < n; i++) {
			this.neighbors.add(new ArrayList<ArrayList<Integer>>());

			for (int j = 0; j < 2; j++) {
				ArrayList<Integer> vertNeighbors = new ArrayList<Integer>();
				ArrayList<Integer> old_vertNeighbors = g.neighbors.get(i).get(j);			

				for (int k = 0; k < old_vertNeighbors.size(); k++) {
					vertNeighbors.add(old_vertNeighbors.get(k));
				}

				this.neighbors.get(i).add(vertNeighbors);
			}
		}
    }

	public void addEdge(int a, int b) // Assumed a is side 0 b is side 1
	{
		if (!(this.neighbors.get(a).get(0).contains(b))) {
			this.neighbors.get(a).get(0).add(b);
			this.neighbors.get(b).get(1).add(a);
			
			int[] edge = new int[2];
			edge[0] = a;
			edge[1] = b;
			this.edges.add(edge);
		}
	}

	public void completeGraph()
	{
		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.n; j++) {
				this.addEdge(i, j);
			}
		}
	}

	public void randomizeGraph(double p) 
	{
		Random r = new Random();

		for (int i = 0; i < this.n; i++) {
			for (int j = 0; j < this.n; j++) {
				if (r.nextDouble() < p) {
					this.addEdge(i, j);
				}
			}
		}
	}

	public ArrayList<ArrayList<Integer>> greedyMaximalMatching() {
		BipartiteGraph copy = new BipartiteGraph(this);
		ArrayList<ArrayList<Integer>> matching = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> matched = new ArrayList<Integer>(); // Keep track of matched vertices

		for (int i = 0; i < this.n; i++) {
			if (!(matched.contains(i))) {
				for (Integer neighbor: this.neighbors.get(i).get(0)) {
					int j = neighbor.intValue();

					if (!(matched.contains(j))) {
						ArrayList<Integer> match = new ArrayList<Integer>();
						match.add(i);
						match.add(j);
						
						matching.add(match);
						matched.add(i);
						matched.add(j);
						break;
					}
				}
			}
		}	

		return matching;
	}
}
