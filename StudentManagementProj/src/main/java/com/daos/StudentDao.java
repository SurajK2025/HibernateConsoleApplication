package com.daos;

import static com.utils.HibernateUtils.getSession;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.pojos.Course;
import com.pojos.Student;

public class StudentDao {

	public static void addStudent(Long courseId, Student student) {
		Session session = getSession().getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		try {
			Course course = session.get(Course.class, courseId);
			if(course != null) {
				course.addStudent(student);
				session.persist(student);
			}
			tx.commit();
		}
		catch(Exception e) {
			if (tx != null) tx.rollback();
			e.printStackTrace();
		}
	}
	
	public static Student getStudentById(Long id) {
		Student student = null;
		Session session = getSession().getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		try {
			student = session.get(Student.class, id);// Checks if student entity exists in L1 cache, if yes returns the same 
												   // if no fires select query, adds entity in L1 cache and returns it.
			student = session.get(Student.class, id);// No select query fired this time, check console.
			tx.commit();
		}
		catch(Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		}
		return student;// Entity detached
	}
	
	public static List<Student> getAllStudents(){
		Session session = getSession().getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		List<Student> students = new ArrayList<>();
		String jpql = "select s from Student s";
		
		try {
			Query query = session.createQuery(jpql, Student.class);
			students = query.getResultList();
			//students = session.createQuery(jpql, Student.class).getResultList();
			tx.commit();
		}
		catch(Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		}
		
		return students;
	}
	
	public static String deleteStudentById(Long studentId) {
		String msg = "Delete operation failed";
		Session session = getSession().getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		try {
			Student student= session.get(Student.class, studentId);
			if(student != null) {	//valid student : object brought in persistent state.
				session.delete(student);	//marked the entity for removal, but neither removed from DB nor L1 cache
				msg = "Student deleted successfully";
			}
			tx.commit();// Hibernate performs dirty checking, session.flush() --> DML : delete --> session.close()
		}//L1 cache is destroyed , db cn rets to the cn pool	
		catch(Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		}
		
		return msg;
	}
	
	public static String deleteStudentByEmail(String email) {
		String msg = "Delete operation failed";
		
		Session session = getSession().getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		String jpql = "delete Student s where s.email=:email";
		
		try {
			int updateCount = session.createQuery(jpql).setParameter("email", email).executeUpdate();
			tx.commit();
			System.out.println("Update count: "+updateCount);
			msg = "Student deleted successfully.";
		}
		catch(Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		}
		
		return msg;
	}
	
	public static String removeStudentFromCourse(Long studentId, Long courseId) {
		String msg = "Delete operation failed";
		
		Session session = getSession().getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		try {
			Course course = session.get(Course.class, courseId);
			Student student = session.get(Student.class, studentId);
			
			if(course != null & studentId != null) {
				course.removeStudent(student);
				msg = "Student deleted successfully.";
			}
			tx.commit();
		}
		catch(Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		}
		return msg;
	}
}