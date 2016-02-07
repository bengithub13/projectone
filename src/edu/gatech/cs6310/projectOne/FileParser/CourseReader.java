package edu.gatech.cs6310.projectOne.FileParser;
import java.util.ArrayList;
import java.util.List;

import edu.gatech.cs6310.projectOne.entity.Course;

public class CourseReader implements FileParser{
	private List<Course> courses;
	
	
	public CourseReader() {
		super();
	}

	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	@Override
	public void parseFile() {
	// we are hardcoding the data for now.  We can read the data and create the same List<Course> with absolute transparency
		courses=new ArrayList<Course>();
		Course course=new Course();
		course.setCourseId(1);
		course.setFallTerm(1);
		course.setSpringTerm(0);
		course.setSummerTerm(0);
		courses.add(course);
		
		course=new Course();
		course.setCourseId(2);
		course.setFallTerm(1);
		course.setSpringTerm(1);
		course.setSummerTerm(1);
		courses.add(course);
		
		course=new Course();
		course.setCourseId(3);
		course.setFallTerm(1);
		course.setSpringTerm(1);
		course.setSummerTerm(1);
		courses.add(course);
				
		course=new Course();
		course.setCourseId(4);
		course.setFallTerm(1);
		course.setSpringTerm(1);
		course.setSummerTerm(1);
		courses.add(course);
		
		course=new Course();
		course.setCourseId(5);
		course.setFallTerm(0);
		course.setSpringTerm(1);
		course.setSummerTerm(0);
		courses.add(course);
				
		course=new Course();
		course.setCourseId(6);
		course.setFallTerm(1);
		course.setSpringTerm(1);
		course.setSummerTerm(1);
		courses.add(course);
				
		course=new Course();
		course.setCourseId(7);
		course.setFallTerm(1);
		course.setSpringTerm(0);
		course.setSummerTerm(0);
		courses.add(course);
		
		course=new Course();
		course.setCourseId(8);
		course.setFallTerm(1);
		course.setSpringTerm(1);
		course.setSummerTerm(1);
		courses.add(course);
				
		course=new Course();
		course.setCourseId(9);
		course.setFallTerm(1);
		course.setSpringTerm(1);
		course.setSummerTerm(1);
		courses.add(course);
				
		course=new Course();
		course.setCourseId(10);
		course.setFallTerm(0);
		course.setSpringTerm(1);
		course.setSummerTerm(0);
		courses.add(course);
				
		course=new Course();
		course.setCourseId(11);
		course.setFallTerm(1);
		course.setSpringTerm(0);
		course.setSummerTerm(0);
		courses.add(course);
				
		course=new Course();
		course.setCourseId(12);
		course.setFallTerm(1);
		course.setSpringTerm(1);
		course.setSummerTerm(1);
		courses.add(course);
				
		course=new Course();
		course.setCourseId(13);
		course.setFallTerm(1);
		course.setSpringTerm(1);
		course.setSummerTerm(1);
		courses.add(course);
				
		course=new Course();
		course.setCourseId(14);
		course.setFallTerm(0);
		course.setSpringTerm(1);
		course.setSummerTerm(0);
		courses.add(course);
				
		course=new Course();
		course.setCourseId(15);
		course.setFallTerm(1);
		course.setSpringTerm(0);
		course.setSummerTerm(0);
		courses.add(course);
				
		course=new Course();
		course.setCourseId(16);
		course.setFallTerm(0);
		course.setSpringTerm(1);
		course.setSummerTerm(0);
		courses.add(course);
				
		course=new Course();
		course.setCourseId(17);
		course.setFallTerm(1);
		course.setSpringTerm(0);
		course.setSummerTerm(0);
		courses.add(course);
		
		course=new Course();
		course.setCourseId(18);
		course.setFallTerm(0);
		course.setSpringTerm(1);
		course.setSummerTerm(0);
		courses.add(course);
				
	
	}
	

}
