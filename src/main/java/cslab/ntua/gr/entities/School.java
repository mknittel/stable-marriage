package cslab.ntua.gr.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cslab.ntua.gr.entities.BinaryAgent;

public class School extends BinaryAgent
{
    private HashMap<Integer, ArrayList<Integer>> affiliateInterestList;
	private ArrayList<Student> affiliates;

    // Creates a copy of an existing agent
    public School(School copy)
    {
		super(copy);

        this.affiliateInterestList = new HashMap<Integer, ArrayList<Integer>>();
        HashMap<Integer, ArrayList<Integer>> old_affiliateInterestList = copy.getAffiliateInterests();
        for (Student affiliate : copy.getAffiliates()) 
		{
			int affID = affiliate.getID();
			ArrayList<Integer> nextInterests = new ArrayList<Integer>();

			for (int pref : old_affiliateInterestList.get(affID)) {
				nextInterests.add(pref);
			}

			this.affiliateInterestList.put(affID, nextInterests);
		}

		affiliates = new ArrayList<Student>();
		for (Student student : copy.getAffiliates()) affiliates.add(student);
    }

    // Creates an agent with preferences read from an input file
    public School(int n, int m, int id, int side, String lineWithPrefs, ArrayList<String> linesWithAffiliatePrefs)
    {
		super(n, m, id, side, lineWithPrefs);

        this.affiliateInterestList = new HashMap<Integer, ArrayList<Integer>>();

		for (int i = 0; i < linesWithAffiliatePrefs.size(); i++) {
			String line = linesWithAffiliatePrefs.get(i);
        	String[] tokens = line.split("\\s+");
			ArrayList<Integer> nextAffiliateInterests = new ArrayList<Integer>();

			for (int j = 0; j < tokens.length; j++) {
				nextAffiliateInterests.add(Integer.parseInt(tokens[j]));
			}

			this.affiliateInterestList.put(id*m + i, nextAffiliateInterests);
		}

		this.affiliates = new ArrayList<Student>();
    }

	public void addAffiliate(Student affiliate)
	{
		if (!this.hasAffiliate(affiliate)) {
			this.affiliates.add(affiliate);
		}
	}

    public boolean checkSchool(int schoolNo, int affiliateNo)
    {	
		return this.affiliateInterestList.get(affiliateNo).get(schoolNo) == 1;
    }

	public boolean hasAffiliate(Student affiliate)
	{
		return this.affiliates.contains(affiliate);
	}

    public HashMap<Integer, ArrayList<Integer>> getAffiliateInterests(){ return affiliateInterestList; }
	public ArrayList<Student> getAffiliates(){ return affiliates; }
}
