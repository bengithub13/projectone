package edu.gatech.cs6310.projectOne;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import edu.gatech.cs6310.projectOne.FileParser.CourseReader;
import edu.gatech.cs6310.projectOne.FileParser.StudentDemandFileReader;
import edu.gatech.cs6310.projectOne.entity.Course;
import edu.gatech.cs6310.projectOne.entity.CourseDependency;
import edu.gatech.cs6310.projectOne.entity.StudentDemand;
import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;

public class StudentScheduler implements Scheduler {
	private double objectiveValue;
	private StudentDemandFileReader studentDemandFileReader;
	private CourseReader courseReader;
	private List<StudentDemand> studentDemands;
	private List<Course> courses;
	private List<CourseDependency> courseDependencies;
	private int numOfStudents;
	private HashMap<Integer, Set<Integer>> coursesPerStudentHashMap;
    private static final int Max_NUM_COURSES_OFFERED=18;
    private static final int TOTAL_SEMESTERS=12;
    private GRBVar[][][] studentCourseSemester;		               
   
	/*			
    private final Integer[][] coursePrerequisite=new Integer[][]{

    	{4,16},{12,1},{9,13},{3,7}       // {rerequisite, course dependant on rerequsite}
    };
    */
    private void setStudentDemands(List <StudentDemand> studentDemands){
    	this.studentDemands=studentDemands;
    }
  
    private void setCourses(List <Course> courses){
    	this.courses=courses;
   
    }
    

		public void calculateSchedule(List<StudentDemand> studentDemand, List<Course> courses, List<CourseDependency> courseDependencies){
        GRBEnv env;
        setStudentDemands(studentDemand);
        setCourses(courses);
        
		try {
			env = new GRBEnv("mip1.log");
			env.set(GRB.IntParam.OutputFlag,0);
			GRBModel model = new GRBModel(env);
		
			
			Set studentSet=new HashSet();
			coursesPerStudentHashMap=new HashMap();
			countStudentDemand();
		
	
			studentCourseSemester=new GRBVar[numOfStudents+1][Max_NUM_COURSES_OFFERED+1][TOTAL_SEMESTERS+1];		 
			
		
			for (int i=1;i<=numOfStudents;i++){
				for (int j=1;j<=Max_NUM_COURSES_OFFERED;j++){  //loop through each course 
					for (int k=1;k<=TOTAL_SEMESTERS;k++){  //loop through each semester
						String varName="studentCourseSemester"+i+j+k;
						
						
						if (isCourseDemandByStudent(i, j)){
									studentCourseSemester[i][j][k]=model.addVar(0.0, 1.0, 0.0, GRB.BINARY,varName);		
						}			
						
						else{
							studentCourseSemester[i][j][k]=model.addVar(0.0, 0.0, 0.0, GRB.BINARY,varName);
							}
					}
				}
				
			}
			
		
			
			
			GRBVar x=model.addVar(0,studentCourseSemester.length, 0.0, GRB.INTEGER,"x");
					
			// Integrate new variables
            model.update();
			
  

            GRBLinExpr expr = new GRBLinExpr();
    //        System.out.print( " Set Objective : minimize " );
            expr.addTerm(1.0,x);
  
          
            
	  model.setObjective(expr, GRB.MINIMIZE);
	
			  

  //***************constraint student cant take more than 2 courses per semester *******************************************************************			  
	
			  for (int i=1;i<=studentCourseSemester.length-1;i++){  //loop through each student  
			 
			  for (int k=1;k<=TOTAL_SEMESTERS;k++){ //loop through each semester 
				  GRBLinExpr constraint = new GRBLinExpr();
	
					for (int j=1;j<=Max_NUM_COURSES_OFFERED;j++){  //loop through each course 
													
						//	if (studentDemandFileReader.isCourseDemandByStudent(i, j)){		
							if (isCourseDemandByStudent(i,j)){
							constraint.addTerm( 1,studentCourseSemester[i][j][k]);
							}
							else{
								constraint.addTerm( 0,studentCourseSemester[i][j][k]);
			
							}
						}
					//add the constraint for every student per semester
					String constraintName="constraint1"+i+k;
					model.addConstr(constraint,GRB.LESS_EQUAL,2.0,constraintName);
				}
				
				}
				
				

 
 //*********************CONSTRAINT each class has to be <=x *************************************************************			  
			
// constraint number of students per course in a semester to less than equal to X 
				  for (int j=1;j<=Max_NUM_COURSES_OFFERED;j++){  //loop through each course
					  
				  for (int k=1;k<=TOTAL_SEMESTERS;k++){ //loop through each semester 
					  GRBLinExpr constraint = new GRBLinExpr();				 			
					  String constraintName="constraint3 "+"course#"+j+"semester#"+k;
					  boolean anyDemandFlag=false;
					  for (int i=1;i<=studentCourseSemester.length-1;i++){  //loop through each student			
						 		
						//	if (studentDemandFileReader.isCourseDemandByStudent(i, j)){			
						  	if (isCourseDemandByStudent(i,j)){
							constraint.addTerm( 1,studentCourseSemester[i][j][k]);
							anyDemandFlag=true;
							}
							else{
								constraint.addTerm( 0,studentCourseSemester[i][j][k]);
			
							}
						}
					 
					  
					  if (anyDemandFlag)
							model.addConstr(constraint,GRB.LESS_EQUAL,x,constraintName);	  
					  
				  }
				
				} 

				  
		  
	

//*********************CONSTRAINT make sure each student takes all their requested courses  *************************************************************			  

		/*		  
	//
	//			  for (int i=1;i<=studentCourseSemester.length-1;i++){  //loop through each student	
				  for (int i=1; i<=studentDemandFileReader.getNumOfStudents();i++){
	 					// add constraint for each course number for student
				  		Integer[] courseList=studentDemandFileReader.getCoursesForStudent(i);
				  		for (int j=0;j<=(courseList.length)-1;j++){
				  			GRBLinExpr constraint = new GRBLinExpr();
				  			 String constraintName="constraint4 "+"student#"+j+"course#"+j;
				  			 int courseNumber = courseList[j];
				  			for (int k=1;k<=TOTAL_SEMESTERS;k++){
				  				constraint.addTerm( 1,studentCourseSemester[i][courseNumber][k]);
				  			}
				  			model.addConstr(constraint,GRB.EQUAL,1,constraintName);
				  		}
				  		
				  	}   
*/

				  for (StudentDemand stdemand: studentDemands){  //loop through each student	
				  
	 					// add constraint for each course number for student
			//	  		Integer[] courseList=studentDemandFileReader.getCoursesForStudent(i);
				//  		for (int j=0;j<=(courseList.length)-1;j++){
				  			GRBLinExpr constraint = new GRBLinExpr();
				  			 String constraintName="constraint4 "+"student#"+stdemand.getStudentId()+"course#"+stdemand.getCourseId();
				  			 
				  			for (int k=1;k<=TOTAL_SEMESTERS;k++){
				  				constraint.addTerm( 1,studentCourseSemester[stdemand.getStudentId()][stdemand.getCourseId()][k]);
				  			}
				  			model.addConstr(constraint,GRB.EQUAL,1,constraintName);
				  		}
				  		
				  	   
 

//*******************************************************************************************
//***** constraint- course availabity certain course only available certain semesters********				  	
//all classes that are not available in a semester should add to 0					 
				  	for (int i=1;i<=studentCourseSemester.length-1;i++){  //loop through each student	
	 					// add constraint for each course number for student
				  		
				  		for (int j=1;j<=Max_NUM_COURSES_OFFERED;j++){
				  			GRBLinExpr constraint = new GRBLinExpr();
				  			 String constraintName="constraint5-  "+"student#"+i+"course#"+j;
				  			for (int k=1;k<=TOTAL_SEMESTERS;k++){
				  				if (!(isCourseAvailableInSemester(k, j)))				  				
				  			constraint.addTerm( 1,studentCourseSemester[i][j][k]);
				  			}
				  			model.addConstr(constraint,GRB.EQUAL,0,constraintName);
				  		}
				  		
				  	}   

	  
        //    model.optimize();
//*******************************************************************************************
//  a student can not take a course before prerequisite  - a course cano not be taken semester 1 if prereq is required
            int courseNum=0;
            String constraintName=null;
            GRBLinExpr constraint = new GRBLinExpr();
          	for (int i=1;i<=studentCourseSemester.length-1;i++){   
		//  	for (int j=0;j<=coursePrerequisite.length-1;j++){  
		 // 		 courseNum=coursePrerequisite[j][1];
	  		for (CourseDependency courseDependency: courseDependencies){
	  			courseNum=courseDependency.getDependentCourseId();
          		constraintName="constraint_prereq1-  "+"student#"+i+"course#"+courseNum;
		  		
		  			for (int k=1;k<=TOTAL_SEMESTERS;k++){
		  							  				
		  			constraint.addTerm( 1,studentCourseSemester[i][courseNum][1]);    //course can't start at semester 1 if dependant on rerequisite
		  			}	  			
		  		}	  		
		  	}  
            
          	model.addConstr(constraint,GRB.EQUAL,0,constraintName);
         
          	
          //*******************************************************************************************
        //  a student can not take a course before prerequisite  from semester 2-max semester
        // a course that depends on a prerequisite Yijk + sum of all prerequistes for Yijk..Yij12<2 .
          
            
          	for (int i=1;i<=studentCourseSemester.length-1;i++){   
		 // 	for (int j=0;j<=coursePrerequisite.length-1;j++){  //prerequisite/course matrix 
		 // 			Integer preReqCourseNum=coursePrerequisite[j][0];
		 // 			courseNum=coursePrerequisite[j][1];
          		for (CourseDependency courseDependency: courseDependencies){
    	  			courseNum=courseDependency.getDependentCourseId();
    	  			Integer preReqCourseNum=courseDependency.getPrerequisiteId();
          		
          		
          		constraintName="constraint_prereq2-  "+"student#"+i+"course#"+courseNum;
	  			 	
		  			for (int k=2;k<=TOTAL_SEMESTERS;k++){
		  					constraint = new GRBLinExpr();
		  			constraint.addTerm( 1,studentCourseSemester[i][courseNum][k]);	 // dependent course for student semester k
		  			
		  				for (int k2=k;k2<=TOTAL_SEMESTERS;k2++){
		  					constraint.addTerm( 1,studentCourseSemester[i][preReqCourseNum][k2]);    //prereq course semester K2....K2max semester
		  				}
		  				model.addConstr(constraint,GRB.LESS_EQUAL,1,constraintName);
		  			}	  			
		  		}	  		
		  	}  
            
          	
         
          	       
          	model.optimize();           
            setObjectiveValue(model.get(GRB.DoubleAttr.ObjVal));
       
			
		
		} catch (GRBException e) {
			e.printStackTrace();
		}

		
	}
	
	
	public void setObjectiveValue(double objectiveValue) {
		this.objectiveValue=objectiveValue;
	}
	
	public double getObjectiveValue() {
		return this.objectiveValue;
	}

	public Vector<String> getCoursesForStudentSemester(String student, String semester) {
		// TODO: You will need to implement this
		return null;
	}

	public Vector<String> getStudentsForCourseSemester(String course, String semester) {
		// TODO: You will need to implement this
		return null;
	}

	
	private void countStudentDemand(){
		for (StudentDemand studentDemand:studentDemands){
		addToStudentHashMap(studentDemand);
	}}

	private boolean isCourseAvailableInSemester(int semesterNumber, int courseId){
		for (Course course:courses){
			if (courseId==course.getCourseId()){
				if (course.isCourseAvailableInSemester(semesterNumber)){
				return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean isCourseDemandByStudent(int studentNumber, int courseNumber) {
		Set<Integer> coursesSet = coursesPerStudentHashMap.get(studentNumber);
		if (coursesSet == null) {
			return false;
		} else if (coursesSet.contains(courseNumber)) {
			return true;
		}
		return false;
	}

	private void addNumOfStudents() {
		numOfStudents++;
	}
	private void addToStudentHashMap(StudentDemand studentDemand) {
		// TODO Auto-generated method stub

		int studentNumber = studentDemand.getStudentId();
		int courseNumber = studentDemand.getCourseId();
		Set<Integer> coursesSet = coursesPerStudentHashMap.get(studentNumber);
		if (coursesSet == null) {
			addNumOfStudents();
			coursesSet = new HashSet<Integer>();
			coursesSet.add(courseNumber);
		} else {
			coursesSet.add(courseNumber);
		}

		coursesPerStudentHashMap.put(studentNumber, coursesSet);

	}
}
