package edu.gatech.cs6310.projectOne;

public class ProjectOne {

	public static void main(String[] args) {
		 StudentScheduler scheduler = new StudentScheduler();
		// scheduler.calculateSchedule("C:/Users/BenPoon/Desktop/project1/student_demand_10.csv");
		// scheduler.calculateSchedule("C:/Users/BenPoon/workspace2/ProjectOne/resources/resources/medium/student_demand_600.csv");

		 //System.out.println("file is "+ args[1]);
		 scheduler.calculateSchedule(args[1]);
		 
		 
		
		 System.out.printf("X=%.2f\n",scheduler.getObjectiveValue());
	}

}
