package cslab.ntua.gr.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BipartiteGraph
{
	protected ArrayList<BinaryAgent> students;
	protected ArrayList<School> schools;
	protected HashMap<Integer, BinaryAgent> studentIDs;
	protected HashMap<Integer, School> schoolIDs;
	protected HashMap<Integer, ArrayList<School>> studentNeighbors;
	protected HashMap<Integer, ArrayList<BinaryAgent>> schoolNeighbors;	

	// Constructs with n on each side
    public BipartiteGraph(BinaryAgent[] students, School[] schools) 
    {
		this.students = new ArrayList<BinaryAgent>(Arrays.asList(students));
		this.schools = new ArrayList<School>(Arrays.asList(schools));

		this.studentIDs = new HashMap<Integer, BinaryAgent>();
		this.schoolIDs = new HashMap<Integer, School>();

		this.studentNeighbors = new HashMap<Integer, ArrayList<School>>();
		this.schoolNeighbors = new HashMap<Integer, ArrayList<BinaryAgent>>();

		for (BinaryAgent student : students) {
			this.studentIDs.put(student.getID(), student);
			this.studentNeighbors.put(student.getID(), new ArrayList<School>());
		}

		for (School school: schools) { 
			this.schoolIDs.put(school.getID(), school);
			this.schoolNeighbors.put(school.getID(), new ArrayList<BinaryAgent>());
		}
    }

    // For cloning
    public BipartiteGraph(BipartiteGraph g) 
    {
		this.students = new ArrayList<BinaryAgent>();
		this.schools = new ArrayList<School>();

		this.studentIDs = new HashMap<Integer, BinaryAgent>();
		this.schoolIDs = new HashMap<Integer, School>();

		this.studentNeighbors = new HashMap<Integer, ArrayList<School>>();
		this.schoolNeighbors = new HashMap<Integer, ArrayList<BinaryAgent>>();

		for (BinaryAgent student : g.students) {
			this.students.add(student);
			this.studentIDs.put(student.getID(), student);
			this.studentNeighbors.put(student.getID(), new ArrayList<School>());

			for (School school : g.studentNeighbors.get(student.getID())) {
				this.studentNeighbors.get(student.getID()).add(school);
			}
		}
		for (School school: g.schools) {
			this.schools.add(school);
			this.schoolIDs.put(school.getID(), school);
			this.schoolNeighbors.put(school.getID(), new ArrayList<BinaryAgent>());

			for (BinaryAgent student : g.schoolNeighbors.get(school.getID())) {
				this.schoolNeighbors.get(school.getID()).add(student);
			}
		}
    }

	// Intended behavior: if one of the agents is not in the graph, the edge is not created
	public void addEdge(BinaryAgent student, School school)
	{
		int a = student.getID();
		int b = school.getID();

		if (!(this.studentNeighbors.get(a).contains(school))) {
			this.studentNeighbors.get(a).add(school);
			this.schoolNeighbors.get(b).add(student);
		}
	}

	// Intended behavior: if one of the agents is not in the graph, the edge is not created
	public void addEdge(int a, int b)
	{
		BinaryAgent student = this.studentIDs.get(a);
		School school = this.schoolIDs.get(b);

		if (this.students.contains(student)
			&& this.schools.contains(school)
			&& !(this.studentNeighbors.get(a).contains(school))) {
			this.studentNeighbors.get(a).add(school);
			this.schoolNeighbors.get(b).add(student);
		}
	}

	public void completeGraph()
	{
		for (BinaryAgent student : this.students) {
			for (School school : this.schools) {
				this.addEdge(student, school);
			}
		}
	}

	public ArrayList<ArrayList<Integer>> greedyMaximalMatching() {
		ArrayList<ArrayList<Integer>> matching = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> matchedStudents = new ArrayList<Integer>();
		ArrayList<Integer> matchedSchools = new ArrayList<Integer>();

		for (BinaryAgent student : this.students) {
			int studentID = student.getID();

			if (!(matchedStudents.contains(studentID))) {
				for (School school: this.studentNeighbors.get(studentID)) {
					int schoolID = school.getID();

					if (!(matchedSchools.contains(schoolID))) {
						ArrayList<Integer> match = new ArrayList<Integer>();
						match.add(studentID);
						match.add(schoolID);
						
						matching.add(match);
						matchedStudents.add(studentID);
						matchedSchools.add(schoolID);
						break;
					}
				}
			}
		}	

		return matching;
	}

	public void removeMatch(int a, int b) {
		BinaryAgent student = studentIDs.get(a);
		School school = schoolIDs.get(b);

		//System.out.println("before");
		//this.print();
		this.students.remove(student);
		this.schools.remove(school);
		//System.out.println("after");
		//this.print();

		this.studentIDs.remove(a);
		this.schoolIDs.remove(b);

		this.studentNeighbors.remove(a);
		this.schoolNeighbors.remove(b);

		for (BinaryAgent student2 : this.students) {
			int a2 = student2.getID();

			if (this.studentNeighbors.get(a2).contains(school)) {
				this.studentNeighbors.get(a2).remove(school);
				//System.out.println("removing from student neighbors");
			}
		}

		for (School school2 : this.schools) {
			int b2 = school2.getID();
			//System.out.println("DEBUGGING: b2=" + b2 + " and our school is " + b);
			//this.print();

			if (this.schoolNeighbors.get(b2).contains(student)) {
				this.schoolNeighbors.get(b2).remove(student);
			}
		}
	}

	public void removeMatchedVertices(ArrayList<ArrayList<Integer>> matching) {
		for (ArrayList<Integer> match : matching) {
			int a = match.get(0);
			int b = match.get(1);

			this.removeMatch(a, b);
		}
	}

	public void print() {
		String binaryAgents = "Students: ";
		String schools = "Schools: ";
		String edges = "Edges: ";

		for (BinaryAgent student : this.students) {
			int studentID = student.getID();
			binaryAgents += studentID + ", ";

			for (School school : this.studentNeighbors.get(studentID)) {
				int schoolID = school.getID();
				edges += "(" + studentID + ", " + schoolID + "), ";
			}
		}

		for (School school : this.schools) {
			schools += school.getID() + ", ";
		}

		System.out.println(binaryAgents);
		System.out.println(schools);
		System.out.println(edges);
	}
}
