package com.daos;

import static com.utils.HibernateUtils.getSession;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.pojos.Address;
import com.pojos.Student;

public class AddressDao {
	public static void addAddress(Long studentId, Address address) {
		Session session = getSession().getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			Student student = session.get(Student.class, studentId);
			if(student != null) {
				student.addAddress(address);
				session.persist(address);
			}
			tx.commit();
		}
		catch(Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		}
	}
}
