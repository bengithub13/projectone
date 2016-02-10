package edu.gatech.cs6310.projectOne;

import edu.gatech.cs6310.projectOne.FileParser.CourseDependenciesFileReader;
import edu.gatech.cs6310.projectOne.FileParser.CourseFileReader;
import edu.gatech.cs6310.projectOne.FileParser.StudentDemandFileReader;

public class ProjectOne {

	public static void main(String[] args) {

		StudentDemandFileReader studentDemandFileReader = new StudentDemandFileReader(args[1]);
		studentDemandFileReader.parseFile();
		CourseFileReader courseFileReader = new CourseFileReader();
		courseFileReader.parseFile();
		CourseDependenciesFileReader cDFileReader = new CourseDependenciesFileReader();
		cDFileReader.parseFile();

		StudentScheduler scheduler = new StudentScheduler();
		scheduler.calculateSchedule(studentDemandFileReader.getStudentDemands(), courseFileReader.getCourses(),
				cDFileReader.getCourseDependencies());

		System.out.printf("X=%.2f\n", scheduler.getObjectiveValue());
		
	//	scheduler.printSchedule();
	}

}
