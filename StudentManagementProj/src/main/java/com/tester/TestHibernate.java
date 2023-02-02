package com.tester;

import static com.utils.HibernateUtils.getSession;

import java.time.LocalDate;
import java.util.Scanner;

import org.hibernate.SessionFactory;

import com.daos.CourseDao;
import com.daos.StudentDao;
import com.pojos.Course;
import com.pojos.Student;

public class TestHibernate {

	public static void main(String[] args) {

		int choice;
		try(SessionFactory sf = getSession(); Scanner sc = new Scanner(System.in)){
			do {
				System.out.println("1.Insert Course \n2.Insert Student "
						+ "\n3.Get List of all courses \n4.Get course by Id "
						+ "\n5.Get List of all students \n6.Get student by Id "
						+ "\n7.Get all students of a particular course \n8.Get course by title "
						+ "\n9.Update course fees by course title "
						+ "\n10.Update course fees by startdate "
						+ "\n11.Drop a student by Id \n12.Drop a student by email"
						+ "\n13.Unpublish a course by Id "
						+ "\n14.Unpublish a course by Title "
						+ "\n15.Remove student from course"
						+ "\n16.Display course details with students enrolled \n99.Close");
				switch(choice = sc.nextInt()) {

				case 1:
					System.out.println("Course title, startdate(yr-m-d), enddate(yr-m-d), fees and capacity");
					CourseDao.addCourse(new Course(sc.next(), LocalDate.parse(sc.next()), LocalDate.parse(sc.next()), sc.nextDouble(), sc.nextInt()));
					break;

				case 2:
					System.out.println("CourseId, name and email");
					StudentDao.addStudent(sc.nextLong(), new Student(sc.next(), sc.next()));
					break;

				case 3:
					System.out.println("List of all courses: ");
					for(Course c : CourseDao.getAllCourses()) {
						System.out.println(c);
					}
					break;

				case 4:
					System.out.println("Enter course id: ");
					System.out.println(CourseDao.getCourseById(sc.nextLong()));
					break;

				case 5:
					System.out.println("List of all students: ");
					for(Student c : StudentDao.getAllStudents()) {
						System.out.println(c);
					}
					break;

				case 6:
					System.out.println("Enter student id: ");
					System.out.println(StudentDao.getStudentById(sc.nextLong()));
					break;

				case 7:
					break;

				case 8:
					System.out.println("Enter course title: ");
					System.out.println(CourseDao.getCourseByTitle(sc.next()));
					break;

				case 9:
					System.out.println("Enter course title and updated fees: ");
					System.out.println(CourseDao.updateCourseFeesByTitle(sc.next(), sc.nextDouble()));
					break;

				case 10:
					System.out.println("Enter startdate(y-m-d) and new fees: ");
					System.out.println(CourseDao.updateCourseFeesByStartDate(LocalDate.parse(sc.next()), sc.nextDouble()));
					break;

				case 11:
					System.out.println("Enter student id: ");
					System.out.println(StudentDao.deleteStudentById(sc.nextLong()));
					break;

				case 12:
					System.out.println("Enter students email: ");
					System.out.println(StudentDao.deleteStudentByEmail(sc.next()));
					break;

				case 13:
					System.out.println("Enter courseId");
					System.out.println(CourseDao.deleteCourseById(sc.nextLong()));
					break;

				case 14:
					System.out.println("Enter course title");
					System.out.println(CourseDao.deleteCourseByTitle(sc.next()));
					break;

				case 15:
					System.out.println("Enter studentId and courseId: ");
					System.out.println(StudentDao.removeStudentFromCourse(sc.nextLong(), sc.nextLong()));
					break;

				case 16:
					System.out.println("Enter course name: ");
					Course course = CourseDao.getCourseAndStudentsDetails(sc.next());
					System.out.println(course);
					course.getStudents().forEach(System.out::println);
					break;
				}
			}while(choice != 99);
		}//At this point sf.close() is called and DB connection pool is cleaned up.
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}