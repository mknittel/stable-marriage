package cslab.ntua.gr.entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BipartiteGraph
{
	protected ArrayList<Student> students;
	protected ArrayList<School> schools;

	protected HashMap<Integer, Student> studentIDs;
	protected HashMap<Integer, School> schoolIDs;

	protected HashMap<Integer, ArrayList<School>> studentNeighbors;
	protected HashMap<Integer, ArrayList<Student>> schoolNeighbors;	

	protected ArrayList<Affiliation> affiliations;
	protected HashMap<Integer, ArrayList<Affiliation>> schoolToAffiliations;
	protected HashMap<Integer, ArrayList<Affiliation>> studentToAffiliations;

	// Constructs with n on each side
    public BipartiteGraph(Student[] students, School[] schools) 
    {
		this.students = new ArrayList<Student>(Arrays.asList(students));
		this.schools = new ArrayList<School>(Arrays.asList(schools));

		this.studentIDs = new HashMap<Integer, Student>();
		this.schoolIDs = new HashMap<Integer, School>();

		this.studentNeighbors = new HashMap<Integer, ArrayList<School>>();
		this.schoolNeighbors = new HashMap<Integer, ArrayList<Student>>();

		for (Student student : students) {
			this.studentIDs.put(student.getID(), student);
			this.studentNeighbors.put(student.getID(), new ArrayList<School>());
		}

		for (School school: schools) { 
			this.schoolIDs.put(school.getID(), school);
			this.schoolNeighbors.put(school.getID(), new ArrayList<Student>());
		}

		this.affiliations = new ArrayList<Affiliation>();
		this.schoolToAffiliations = new HashMap<Integer, ArrayList<Affiliation>>();
		this.studentToAffiliations = new HashMap<Integer, ArrayList<Affiliation>>();

		for (Student student : this.students) {
			this.studentToAffiliations.put(student.getID(), new ArrayList<Affiliation>());
		}

		for (School school : this.schools) {
			this.schoolToAffiliations.put(school.getID(), new ArrayList<Affiliation>());
		}
    }

    // For cloning
    public BipartiteGraph(BipartiteGraph g) 
    {
		this.students = new ArrayList<Student>();
		this.schools = new ArrayList<School>();

		this.studentIDs = new HashMap<Integer, Student>();
		this.schoolIDs = new HashMap<Integer, School>();

		this.studentNeighbors = new HashMap<Integer, ArrayList<School>>();
		this.schoolNeighbors = new HashMap<Integer, ArrayList<Student>>();

		for (Student student : g.students) {
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
			this.schoolNeighbors.put(school.getID(), new ArrayList<Student>());

			for (Student student : g.schoolNeighbors.get(school.getID())) {
				this.schoolNeighbors.get(school.getID()).add(student);
			}
		}

		this.affiliations = new ArrayList<Affiliation>();
		this.studentToAffiliations = new HashMap<Integer, ArrayList<Affiliation>>();
		this.schoolToAffiliations = new HashMap<Integer, ArrayList<Affiliation>>();

		for (Student student : this.students) {
			this.studentToAffiliations.put(student.getID(), new ArrayList<Affiliation>());
		}

		for (School school : this.schools) {
			this.schoolToAffiliations.put(school.getID(), new ArrayList<Affiliation>());
		}

		this.addAffiliations(g.affiliations);
    }

    // For cloning with affiliations
    public BipartiteGraph(BipartiteGraph g, HashMap<Integer, ArrayList<ArrayList<Student>>> affiliations) 
    {
		this.students = new ArrayList<Student>();
		this.schools = new ArrayList<School>();

		this.studentIDs = new HashMap<Integer, Student>();
		this.schoolIDs = new HashMap<Integer, School>();

		this.studentNeighbors = new HashMap<Integer, ArrayList<School>>();
		this.schoolNeighbors = new HashMap<Integer, ArrayList<Student>>();

		for (Student student : g.students) {
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
			this.schoolNeighbors.put(school.getID(), new ArrayList<Student>());

			for (Student student : g.schoolNeighbors.get(school.getID())) {
				this.schoolNeighbors.get(school.getID()).add(student);
			}
		}

		this.affiliations = new ArrayList<Affiliation>();
		this.studentToAffiliations = new HashMap<Integer, ArrayList<Affiliation>>();
		this.schoolToAffiliations = new HashMap<Integer, ArrayList<Affiliation>>();

		for (Student student : this.students) {
			this.studentToAffiliations.put(student.getID(), new ArrayList<Affiliation>());
		}

		for (School school : this.schools) {
			this.schoolToAffiliations.put(school.getID(), new ArrayList<Affiliation>());
		}

		this.addAffiliations(affiliations);
    }

	public ArrayList<School> getSchools()
	{
		return this.schools;
	}
	
	public ArrayList<Student> getStudents()
	{
		return this.students;
	}

	// Intended behavior: if one of the agents is not in the graph, the edge is not created
	public void addEdge(Student student, School school)
	{
		int a = student.getID();
		int b = school.getID();

		if (!(this.studentNeighbors.get(a).contains(school))) {
			this.studentNeighbors.get(a).add(school);
			this.schoolNeighbors.get(b).add(student);
		}
	}

	public void addAffiliations(ArrayList<Affiliation> affiliations) {
		for (Affiliation aff : affiliations) {
			this.affiliations.add(new Affiliation(aff));
			this.schoolToAffiliations.get(aff.school.getID()).add(aff);

			for (Student student : aff.getStudents()) {
				this.studentToAffiliations.get(student.getID()).add(aff);
			}
		}
	}

	public void addAffiliations(HashMap<Integer, ArrayList<ArrayList<Student>>> affiliations) {
		int id = 0;

		for (School school : this.schools) {
			int schoolID = school.getID();
			
			for (ArrayList<Student> affiliates : affiliations.get(schoolID)) {
				int reservation = Math.min(school.getCapacity(), affiliates.size());

				Affiliation aff = new Affiliation(id, reservation, school, affiliates);
				id += 1;

				this.affiliations.add(aff);
				this.schoolToAffiliations.get(aff.school.getID()).add(aff);

				for (Student student : aff.getStudents()) {
					this.studentToAffiliations.get(student.getID()).add(aff);
				}
			}
		}
	}

	// Intended behavior: if one of the agents is not in the graph, the edge is not created
	public void addEdge(int a, int b)
	{
		Student student = this.studentIDs.get(a);
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
		for (Student student : this.students) {
			for (School school : this.schools) {
				this.addEdge(student, school);
			}
		}
	}

	public ArrayList<Student> getSchoolNeighborhood(int schoolID)
	{
		ArrayList<Student> neighbors = new ArrayList<Student>();

		for (Student neighbor : this.schoolNeighbors.get(schoolID)) {
			neighbors.add(neighbor);
		}

		return neighbors;
	}

	public int getSchoolNeighborhoodSize(int schoolID)
	{
		return schoolNeighbors.get(schoolID).size();
	}

	public ArrayList<ArrayList<Integer>> greedyMaximalMatching() {
		ArrayList<ArrayList<Integer>> matching = new ArrayList<ArrayList<Integer>>();
		HashMap<Integer, Integer> studentMatchSize = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> schoolMatchSize = new HashMap<Integer, Integer>();

		for (Student student: this.students) {
			int studentID = student.getID();
			studentMatchSize.put(studentID, 0);
		}

		for (School school : this.schools) {
			int schoolID = school.getID();
			schoolMatchSize.put(schoolID, 0);

			for (Student student: this.schoolNeighbors.get(schoolID)) {
				// Stop when school is entirely matched
				if (schoolMatchSize.get(schoolID) >= school.getCapacity()) {
					break;
				}

				int studentID = student.getID();

				if (studentMatchSize.get(studentID) < student.getCapacity()) {
					ArrayList<Integer> match = new ArrayList<Integer>();
					match.add(studentID);
					match.add(schoolID);
					
					matching.add(match);
					studentMatchSize.put(studentID, studentMatchSize.get(studentID) + 1);
					schoolMatchSize.put(schoolID, schoolMatchSize.get(schoolID) + 1);
				}
			}
		}	

		return matching;
	}

	public ArrayList<ArrayList<Integer>> greedyReservedMaximalMatching() {
		ArrayList<ArrayList<Integer>> matching = new ArrayList<ArrayList<Integer>>();
		HashMap<Integer, Integer> studentMatchSize = new HashMap<Integer, Integer>();
		HashMap<Integer, Integer> schoolMatchSize = new HashMap<Integer, Integer>();

		for (Student student: this.students) {
			int studentID = student.getID();
			studentMatchSize.put(studentID, 0);
		}

		for (School school : this.schools) {
			int schoolID = school.getID();
			schoolMatchSize.put(schoolID, 0);
			int numReserved = 0;

			for (Affiliation aff : this.schoolToAffiliations.get(schoolID)) {
				numReserved += aff.getReservation();
			}

			for (Student student: this.schoolNeighbors.get(schoolID)) {
				// Stop when school is entirely matched
				if (schoolMatchSize.get(schoolID) >= school.getCapacity() - numReserved) {
					break;
				}

				int studentID = student.getID();
				boolean isReserved = false;

				for (Affiliation aff : this.studentToAffiliations.get(studentID)) {
					if (aff.isAtCapacity()) {
						isReserved = true;
						break;
					}
				}

				if (((!isReserved) && studentMatchSize.get(studentID) < student.getCapacity())
					|| studentMatchSize.get(studentID) < student.getCapacity() - 1) {
					ArrayList<Integer> match = new ArrayList<Integer>();
					match.add(studentID);
					match.add(schoolID);
					
					matching.add(match);
					studentMatchSize.put(studentID, studentMatchSize.get(studentID) + 1);
					schoolMatchSize.put(schoolID, schoolMatchSize.get(schoolID) + 1);

					if (studentMatchSize.get(studentID) == student.getCapacity()) {
						for (Affiliation aff : this.studentToAffiliations.get(studentID)) {
							aff.addMatch();
						}
					}
				}
			}
		}	

		return matching;
	}

	public void removeSchool(int v) {
		School school = schoolIDs.get(v);
		this.schools.remove(school);
		this.schoolIDs.remove(v);
		this.schoolNeighbors.remove(v);

		for (Student student2 : this.students) {
			int a2 = student2.getID();

			if (this.studentNeighbors.get(a2).contains(school)) {
				this.studentNeighbors.get(a2).remove(school);
				//System.out.println("removing from student neighbors");
			}
		}
	}

	public void removeStudent(int v) {
		Student  student = studentIDs.get(v);
		this.students.remove(student);
		this.studentIDs.remove(v);
		this.studentNeighbors.remove(v);

		for (School school : this.schools) {
			int b = school.getID();

			if (this.schoolNeighbors.get(b).contains(student)) {
				this.schoolNeighbors.get(b).remove(student);
			}
		}
	}

	public void remove0CapacityVertices() {
		for (Student student : this.students) {
			if (student.getCapacity() == 0) this.removeStudent(student.getID());
		}

		for (School school : this.schools) {
			if (school.getCapacity() == 0) this.removeSchool(school.getID());
		}
	}


	public void removeMatch(int a, int b) {
		Student student = studentIDs.get(a);
		School school = schoolIDs.get(b);
		System.out.println("reducing: " + a + " and " + b);

		student.reduceCapacity();
		school.reduceCapacity();

		if (student.getCapacity() == 0) {
			this.removeStudent(a);
		}

		if (school.getCapacity() == 0) {
			this.removeSchool(b);
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
		String students = "Students: ";
		String schools = "Schools: ";
		String edges = "Edges: ";

		for (Student student : this.students) {
			if (student.getCapacity() == 0) {
				continue;
			}

			int studentID = student.getID();
			students += studentID + ", ";

			for (School school : this.studentNeighbors.get(studentID)) {
				int schoolID = school.getID();
				edges += "(" + studentID + ", " + schoolID + "), ";
			}
		}

		for (School school : this.schools) {
			if (school.getCapacity() == 0) {
				continue;
			}

			schools += school.getID() + ", ";
		}

		System.out.println(students);
		System.out.println(schools);
		System.out.println(edges);
	}
}
