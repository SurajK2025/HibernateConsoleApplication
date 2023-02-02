package com.pojos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "courses")
public class Course extends BaseEntity {
	@Column(length = 20, nullable = false, unique = true)
	private String title;

	@Column(name = "start_date")
	private LocalDate startdate;

	@Column(name = "end_date")
	private LocalDate enddate;

	private double fees;

	private int capacity;

	@OneToMany(mappedBy = "course", cascade = CascadeType.ALL,
			fetch=FetchType.LAZY, orphanRemoval = true)
	private List<Student> students = new ArrayList<>();

	public Course() {}

	//Students list is skipped in the parameters of constructor, as they will be added later (A course will not have any students at the time of publishing.).
	public Course(String title, LocalDate startdate, LocalDate enddate, double fees, int capacity) {
		super();
		this.title = title;
		this.startdate = startdate;
		this.enddate = enddate;
		this.fees = fees;
		this.capacity = capacity;
	}

	public void addStudent(Student student) {
		students.add(student);
		student.setCourse(this);
	}

	public void removeStudent(Student student) {
		students.remove(student);
		student.setCourse(null);
	}

	public void setFees(double fees) {
		this.fees = fees;
	}

	public List<Student> getStudents() {
		return students;
	}

	//
	@Override
	public String toString() {
		return "Course [id="+ super.getId() +" title=" + title + ", startdate=" + startdate + ", enddate=" + enddate + ", fees=" + fees
				+ ", capacity=" + capacity + "]";
	}
}