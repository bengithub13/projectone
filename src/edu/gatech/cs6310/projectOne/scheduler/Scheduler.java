package edu.gatech.cs6310.projectOne.scheduler;
import java.util.List;
import java.util.Vector;

import edu.gatech.cs6310.projectOne.entity.Course;
import edu.gatech.cs6310.projectOne.entity.CourseDependency;
import edu.gatech.cs6310.projectOne.entity.StudentDemand;

public interface Scheduler {
	public void calculateSchedule(List<StudentDemand> studentDemand, List<Course> courses, List <CourseDependency> courseDependencies);	
	public double getObjectiveValue();
	
}
