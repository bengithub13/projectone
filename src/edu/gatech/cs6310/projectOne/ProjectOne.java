package edu.gatech.cs6310.projectOne;

public class ProjectOne {

	public static void main(String[] args) {
		 StudentScheduler scheduler = new StudentScheduler();
		 scheduler.calculateSchedule("C:/Users/BenPoon/Desktop/project1/student_demand_600.csv");
		 
		 
		 System.out.printf("X= %f\n",scheduler.getObjectiveValue());
		 
		//StudentDemand studentDemand=new StudentDemand();
	   // studentDemand.SetStudentDemandRow();
	}

}
