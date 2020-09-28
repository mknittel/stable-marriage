package cslab.ntua.gr.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cslab.ntua.gr.entities.BinaryAgent;

public class School extends BinaryAgent
{
    private int n, side, id;
    private int[] affiliateInterestList;

    // Creates a copy of an existing agent
    public School(School copy)
    {
		super(copy);

        affiliateInterestList = new int[n];
        int[] old_affiliateInterestList = copy.getAffiliateInterests();
        for (int j = 0; j < n; j++) affiliateInterestList[j] = old_affiliateInterestList[j];
    }

    // Creates an agent with random preferences (uniform), number selected = threshold * n
    public School(int n, int id, int side, double threshold, double affiliateThreshold)
    {
		super(n, id, side, threshold);

		Random r = new Random();

        affiliateInterestList = new int[n];
        for (int j = 0; j < n; j++) {
			if (r.nextDouble() < affiliateThreshold) {
				affiliateInterestList[j] = 1;
			} else {
				affiliateInterestList[j] = 0;
			}
		}
    }

    // Creates an agent with preferences read from an input file
    public School(int n, int id, int side, String lineWithPrefs, String lineWithAffiliatePrefs)
    {
		super(n, id, side, lineWithPrefs);

        affiliateInterestList = new int[n];
        String[] tokens = lineWithAffiliatePrefs.split("\\s+");
        for (int j = 0; j < tokens.length; j++) affiliateInterestList[j] = Integer.parseInt(tokens[j]);
    }

    public boolean checkSchool(int schoolNo)
    {
		return this.affiliateInterestList[schoolNo] == 1;
    }

    public int[] getAffiliateInterests(){ return affiliateInterestList; }
}
