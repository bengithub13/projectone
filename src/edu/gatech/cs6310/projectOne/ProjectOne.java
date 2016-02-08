package edu.gatech.cs6310.projectOne;

import edu.gatech.cs6310.projectOne.FileParser.CourseDependenciesFileReader;
import edu.gatech.cs6310.projectOne.FileParser.CourseReader;
import edu.gatech.cs6310.projectOne.FileParser.StudentDemandFileReader;

public class ProjectOne {

	public static void main(String[] args) {

		StudentDemandFileReader studentDemandFileReader = new StudentDemandFileReader(args[1]);
		studentDemandFileReader.parseFile();
		CourseReader courseReader = new CourseReader();
		courseReader.parseFile();
		CourseDependenciesFileReader cDFileReader = new CourseDependenciesFileReader();
		cDFileReader.parseFile();

		StudentScheduler scheduler = new StudentScheduler();
		scheduler.calculateSchedule(studentDemandFileReader.getStudentDemand(), courseReader.getCourses(),
				cDFileReader.getCourseDependencies());

		System.out.printf("X=%.2f\n", scheduler.getObjectiveValue());
	}

}
