package com.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {
	//Static SessionFactory reference along with static initializer block ensures singleton pattern implementation.
	public static SessionFactory factory;
	
	static {
		//factory = new Configuration().configure().buildSessionFactory();
		Configuration cfg = new Configuration();
		cfg.configure();	// By default looks at src/main/resources for 'hibernate.cfg.xml'.
							// If placed at some other location (with different name), provide relative path as a parameter to configure function.
		factory = cfg.buildSessionFactory();
		System.out.println("Session factory configured.");
	}
	
	public static SessionFactory getSession() {
		return factory;
	}
}