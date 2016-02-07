package edu.gatech.cs6310.projectOne;
import java.util.List;
import java.util.Vector;

import edu.gatech.cs6310.projectOne.entity.Course;
import edu.gatech.cs6310.projectOne.entity.CourseDependency;
import edu.gatech.cs6310.projectOne.entity.StudentDemand;

public interface Scheduler {

	public void calculateSchedule(List<StudentDemand> studentDemand, List<Course> courses, List <CourseDependency> courseDependencies);	
	public double getObjectiveValue();
	public Vector<String> getCoursesForStudentSemester( String student, String semester );
	public Vector<String> getStudentsForCourseSemester( String course, String semester );
}
