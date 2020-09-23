package cslab.ntua.gr.entities;

import java.util.ArrayList;
import java.util.List;

import cslab.ntua.gr.entities.BinaryAgent;

public class School extends BinaryAgent
{
    private int n, side, id;
    private List<Integer> affiliateInterestList; // list of all interested schools for affiliate
    private int[] inverseAffiliateInterests; // binary vector, ith index is acceptability of ith school for affiliate

    // Creates a copy of an existing agent
    public School(School copy)
    {
		super(copy);

        affiliateInterestList = new ArrayList<Integer>();
        List<Integer> old_affiliateInterestList = copy.getAffiliateInterests();
        for (int j = 0; j < n; j++) affiliateInterestList.add(old_affiliateInterestList.get(j));

        inverseAffiliateInterests = new int[n];
        int[] old_inverseAffiliateInterests = copy.getInvAffiliateInterests();
        for (int j = 0; j < n; j++) inverseAffiliateInterests[j] = old_inverseAffiliateInterests[j];
    }

    // Creates an agent with random preferences (uniform), number selected = threshold * n
    public School(int n, int id, int side, double threshold, double affiliateThreshold)
    {
		super(n, id, side, threshold);

		int num_selected = (int) Math.round(affiliateThreshold * n);

        affiliateInterestList = new ArrayList<Integer>();
        for (int j = 0; j < n; j++) affiliateInterestList.add(j);
        java.util.Collections.shuffle(affiliateInterestList);
		affiliateInterestList.subList(0, num_selected);

        inverseAffiliateInterests = new int[n];
        for (int j = 0; j < n; j++) {
			if (affiliateInterestList.contains(j)) {
				inverseAffiliateInterests[j] = 1;
			} else {
				inverseAffiliateInterests[j] = 0;
			}
		}
    }

    // Creates an agent with preferences read from an input file
    public School(int n, int id, int side, String lineWithPrefs, String lineWithAffiliatePrefs)
    {
		super(n, id, side, lineWithPrefs);

        affiliateInterestList = new ArrayList<Integer>();
        String[] tokens = lineWithAffiliatePrefs.split("\\s+");
        for (int j = 0; j < tokens.length; j++) affiliateInterestList.add(Integer.parseInt(tokens[j]));

        inverseAffiliateInterests = new int[n];
        for (int j = 0; j < n; j++) {
			if (affiliateInterestList.contains(j)) {
				inverseAffiliateInterests[j] = 1;
			} else {
				inverseAffiliateInterests[j] = 0;
			}
		}
    }

    public int checkSchool(int schoolNo)
    {
        if (affiliateInterestList.contains(schoolNo)) {
			return 1;
		} else {
			return 0;
		}
    }

	// Is a STRICTLY preferable to b?
    public boolean cmpSchool(int a, int b)
    {
        if (inverseAffiliateInterests[a] < inverseAffiliateInterests[b]) return true;
        else return false;
    }

    public List<Integer> getAffiliateInterests(){ return affiliateInterestList; }
    public int[] getInvAffiliateInterests(){ return inverseAffiliateInterests; }
}
