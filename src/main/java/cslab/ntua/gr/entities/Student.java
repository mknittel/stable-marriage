package cslab.ntua.gr.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cslab.ntua.gr.entities.BinaryAgent;

public class Student extends BinaryAgent
{
	private School school;

	public Student() { }

    // Creates a copy of an existing agent
    public Student(Student copy)
    {
		super(copy);
		this.school = copy.school;
    }

    // Creates an agent with preferences read from an input file
    public Student(int n, int m, int id, int side, String lineWithPrefs)
    {
		super(n, m, id, side, lineWithPrefs);
    }

	public void setSchool(School school)
	{
		this.school = school;
	}

	public School getSchool(){ return school; }
}
