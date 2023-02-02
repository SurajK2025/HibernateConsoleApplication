package com.pojos;

import javax.persistence.Entity;

@Entity
public class Address extends BaseEntity{
	private String city;
	private String state;
	private double zipcode;
}