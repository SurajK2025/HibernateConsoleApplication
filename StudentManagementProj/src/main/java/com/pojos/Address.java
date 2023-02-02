package com.pojos;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="addresses")
public class Address extends BaseEntity{
	private String city;
	private String state;
	
	@Column(unique = true)
	private double zipcode;
	
	@OneToOne
	@JoinColumn(name = "s_id")
	private Student student;
	
	public void setStudent(Student student) {
		this.student = student;
	}

	public Address(String city, String state, double zipcode) {
		super();
		this.city = city;
		this.state = state;
		this.zipcode = zipcode;
	}

	@Override
	public String toString() {
		return "Address [city=" + city + ", state=" + state + ", zipcode=" + zipcode + "]";
	}
}