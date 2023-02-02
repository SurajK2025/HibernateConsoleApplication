package com.pojos;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "students")
public class Student extends BaseEntity {

	private String name;

	@Column(unique = true)
	private String email;

	@ManyToOne
	@JoinColumn(name = "c_Id")
	private Course course;

	@OneToOne(mappedBy = "student", cascade = CascadeType.ALL, 
			fetch = FetchType.LAZY, orphanRemoval = true)
	private Address address;
	
	public void addAddress(Address address) {
		this.address = address;
		address.setStudent(this);
	}
	
	public Student() {}

	public Student(String name, String email) {
		super();
		this.name = name;
		this.email = email;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	@Override
	public String toString() {
		return "Student [id="+ super.getId() +" name=" + name + ", email=" + email + "]";
	}
}