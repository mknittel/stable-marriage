package cslab.ntua.gr.entities;

import java.util.ArrayList;
import java.util.List;

public class Affiliation
{
    protected int id;
	protected int reservation;
	protected int numMatched;
	protected School school;
	protected ArrayList<Student> students;

	public Affiliation(int id, School school, ArrayList<Student> students) {
		this.id = id;
		this.reservation = 1;
		this.numMatched = 0;
		this.school = school;

		this.students = new ArrayList<Student>();
		
		for (Student student: students) {
			this.students.add(student);
		}
	}

	public Affiliation(int id, int reservation, School school, ArrayList<Student> students) {
		this.id = id;
		this.reservation = reservation;
		this.numMatched = 0;
		this.school = school;

		this.students = new ArrayList<Student>();
		
		for (Student student: students) {
			this.students.add(student);
		}
	}

	// Assuming we are refreshing the numMatched
	public Affiliation(Affiliation a) {
		this.id = a.getID();
		this.reservation = a.getReservation();
		this.numMatched = 0;
		this.school = a.getSchool();

		this.students = new ArrayList<Student>();
		
		for (Student student: a.getStudents()) {
			this.students.add(student);
		}
	}

	public void addMatch() {
		numMatched++;
	}

	public boolean isAtCapacity() {
		return (this.students.size() - this.numMatched) <= this.reservation;
	}

	public ArrayList<Student> getStudents() {
		return this.students;
	}

	public int getID() {
		return this.id;
	}

	public int getReservation() {
		return this.reservation;
	}

	public int getNumMatched() {
		return this.numMatched;
	}

	public School getSchool() {
		return this.school;
	}
}
