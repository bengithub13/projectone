package edu.gatech.cs6310.projectOne;

import edu.gatech.cs6310.projectOne.FileParser.CourseDependenciesFileParser;
import edu.gatech.cs6310.projectOne.FileParser.CourseFileParser;
import edu.gatech.cs6310.projectOne.FileParser.StudentDemandFileParser;
import edu.gatech.cs6310.projectOne.scheduler.StudentScheduler;

public class ProjectOne {

	public static void main(String[] args) {

		StudentDemandFileParser studentDemandFileParser = new StudentDemandFileParser(args[1]);
		studentDemandFileParser.parseFile();
		CourseFileParser courseFileParser = new CourseFileParser();
		courseFileParser.parseFile();
		CourseDependenciesFileParser cDFileReader = new CourseDependenciesFileParser();
		cDFileReader.parseFile();

		StudentScheduler scheduler = new StudentScheduler();
		scheduler.calculateSchedule(studentDemandFileParser.getStudentDemands(), courseFileParser.getCourses(),
				cDFileReader.getCourseDependencies());

		System.out.printf("X=%.2f\n", scheduler.getObjectiveValue());
		
		//scheduler.printSchedule();
	}

}
