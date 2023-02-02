package com.daos;

import static com.utils.HibernateUtils.getSession;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import com.pojos.Course;

public class CourseDao {

	public static void addCourse(Course course) {
		Session session = getSession().openSession();
		//Session session = getSession().getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			Long id = (Long) session.save(course);
			tx.commit();
			System.out.println("Course added with id: "+id);
		}
		catch(Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		}
		//Needed only if using openSession() API
		finally {
			if (session != null) session.close();
		}
	}

	public static Course getCourseById(Long id) {
		Course course = null;
		Session session = getSession().getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			course = session.get(Course.class, id);// Checks if course entity exists in L1 cache, if yes returns the same
												   // if no fires select query, adds entity in L1 cache and returns it.
			course = session.get(Course.class, id);// No select query fired this time, check console.
			tx.commit();
		}
		catch(Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		}
		return course;// Entity detached
	}

	public static List<Course> getAllCourses(){
		Session session = getSession().getCurrentSession();
		Transaction tx = session.beginTransaction();

		List<Course> courses = new ArrayList<>();
		String jpql = "select c from Course c";

		try {
			Query query = session.createQuery(jpql, Course.class);
			courses = query.getResultList();
			//courses = session.createQuery(jpql, Course.class).getResultList();
			tx.commit();
		}
		catch(Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		}

		return courses;
	}

	public static Course getCourseByTitle(String title) {
		Session session = getSession().getCurrentSession();
		Transaction tx = session.beginTransaction();

		Course course = null;
		String jpql = "select c from Course c where c.title=:title";

		try {
			course = session.createQuery(jpql, Course.class).setParameter("title", title).getSingleResult();
			tx.commit();
		}
		catch(Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		}

		return course;
	}

	public static String updateCourseFeesByTitle(String title, Double newFees) {
		String msg = "Update failed.";

		Session session = getSession().getCurrentSession();
		Transaction tx = session.beginTransaction();

		Course course = null;
		String jpql = "select c from Course c where c.title=:title";

		try {
			course = session.createQuery(jpql, Course.class).setParameter("title", title).getSingleResult();
			course.setFees(newFees);//Changing the state of a Persistent entity. Hibernate performs DirtyChecking.
			tx.commit();
			msg = "Course Fees Updated Successfully";
		}
		catch(Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		}
		course.setFees(2*newFees);//Trying to modify the state of a Detached entity, Hibernate cannot perform DirtyChecking.
		return msg;
	}//course object marked for garbage collection.

	public static String updateCourseFeesByStartDate(LocalDate startdate, Double newFees) {
		String msg = "Update failed.";

		Session session = getSession().getCurrentSession();
		Transaction tx = session.beginTransaction();

		String jpql = "update Course c set c.fees=:newFees where c.startdate<:startdate";

		try {
			int updateCount = session.createQuery(jpql).setParameter("newFees", newFees).setParameter("startdate", startdate).executeUpdate();
			tx.commit();
			System.out.println("Update count: "+updateCount);
			msg = "Course Fees Updated Successfully";
		}
		catch(Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		}
		return msg;
	}

	public static String deleteCourseById(Long courseId) {
		String msg = "Delete operation failed";
		Session session = getSession().getCurrentSession();
		Transaction tx = session.beginTransaction();

		try {
			Course course= session.get(Course.class, courseId);
			if(course != null) {	//valid course : object brought in persistent state.
				session.delete(course);	//marked the entity for removal, but neither removed from DB nor L1 cache
				msg = "Course deleted successfully";
			}
			tx.commit();// Hibernate performs dirty checking, session.flush() --> DML : delete --> session.close()
		}//L1 cache is destroyed , db cn rets to the cn pool
		catch(Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		}

		return msg;
	}

	public static String deleteCourseByTitle(String title) {
		String msg = "Delete operation failed";
		Session session = getSession().getCurrentSession();
		Transaction tx = session.beginTransaction();

		String jpql = "select c from Course c where c.title=:title";

		try {
			Course course = session.createQuery(jpql, Course.class).setParameter("title", title).getSingleResult();
			session.delete(course);
			tx.commit();
			msg = "Course deleted successfully.";
		}
		catch(Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		}

		return msg;
	}

//	FK constraint violation
//	public static String deleteCourseByTitle(String title) {
//		String msg = "Delete operation failed";
//		Session session = getSession().getCurrentSession();
//		Transaction tx = session.beginTransaction();
//
//		String jpql = "delete Course c where c.title=:title";
//
//		try {
//			int updateCount = session.createQuery(jpql).setParameter("title", title).executeUpdate();
//			tx.commit();
//			System.out.println("Update count: "+updateCount);
//			msg = "Course deleted successfully.";
//		}
//		catch(Exception e) {
//			if (tx != null) tx.rollback();
//			throw e;
//		}
//
//		return msg;
//	}

	public static Course getCourseAndStudentsDetails(String title) {
		Course course = null;
		Session session = getSession().getCurrentSession();
		Transaction tx = session.beginTransaction();

		//String jpql = "select c from Course c where c.title=:title";//LazyInitException
		String jpql = "select c from Course c left outer join fetch c.students where c.title=:title";

		try {
			course = session.createQuery(jpql, Course.class).setParameter("title", title).getSingleResult();
			//course.getStudents().size();
			tx.commit();
		}
		catch(Exception e) {
			if (tx != null) tx.rollback();
			throw e;
		}

		return course;
	}
}
