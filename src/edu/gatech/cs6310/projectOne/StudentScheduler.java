package edu.gatech.cs6310.projectOne;
import java.util.List;
import java.util.Vector;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;

public class StudentScheduler implements Scheduler {
	private double objectiveValue;
	private StudentDemandFileReader studentDemandFileReader;
    private static final int Max_NUM_COURSES_OFFERED=18;
    private static final int TOTAL_SEMESTERS=12;
    private GRBVar[][][] studentCourseSemester;		               // what happens to the results
    private List<String[]> studentDemandMatrix;
    private final Integer[][] courseAvailability=new Integer[][]{
    	{},										//place holder to start index off as 1
    	{0,2,3,4,6,8,9,12,13,1,7,11,15,17},		//fall semester 1
    	{0,2,3,4,6,8,9,12,13,5,10,14,16,18},    	//spring
    	{0,2,3,4,6,8,9,12,13},					//summer
    	{0,2,3,4,6,8,9,12,13,1,7,11,15,17},		//fall
    	{0,2,3,4,6,8,9,12,13,5,10,14,16,18},    	//spring
    	{0,2,3,4,6,8,9,12,13},					//summer
    	{0,2,3,4,6,8,9,12,13,1,7,11,15,17},		//fall
    	{0,2,3,4,6,8,9,12,13,5,10,14,16,18},    	//spring
    	{0,2,3,4,6,8,9,12,13},					//summer
    	{0,2,3,4,6,8,9,12,13,1,7,11,15,17},		//fall
    	{0,2,3,4,6,8,9,12,13,5,10,14,16,18},    	//spring
    	{0,2,3,4,6,8,9,12,13},					//summer semester 12
    };					
    private final Integer[][] coursePrerequisite=new Integer[][]{
    	{4,16},{4,16},{12,1},{9,13},{3,7}       // {rerequisite, course dependant on rerequsite}
    };
    
    private void setStudentDemandFileReader(StudentDemandFileReader studentDemandFileReader){
    	this.studentDemandFileReader=studentDemandFileReader;
    }
	public void calculateSchedule(String dataFolder){ 


		
		
		
        GRBEnv env;
		try {
			env = new GRBEnv("mip1.log");
			env.set(GRB.IntParam.OutputFlag,0);
			GRBModel model = new GRBModel(env);
			setStudentDemandFileReader(new StudentDemandFileReader(dataFolder));
			studentDemandMatrix=studentDemandFileReader.getStudentDemandRows();
		//Create the variables -- add a variable for each "POSSIBLE" combination of Yijk
		//i want to start the array indexes at 1 to keep sanity.	
		studentCourseSemester=new GRBVar[(studentDemandFileReader.getNumOfStudents())+1][Max_NUM_COURSES_OFFERED+1][TOTAL_SEMESTERS+1];
			
			 
			
			for (int i=1;i<=studentCourseSemester.length-1;i++){  //loop through each row in matrix
				for (int j=1;j<=Max_NUM_COURSES_OFFERED;j++){  //loop through each course 
					for (int k=1;k<=TOTAL_SEMESTERS;k++){  //loop through each semester
						String varName="studentCourseSemester"+i+j+k;
						
						if (studentDemandFileReader.isCourseDemandByStudent(i, j)){
				//		if ((studentDemandFileReader.isCourseDemandByStudent(i, j)) && (isCourseAvailableInSemester(k, j))){
									studentCourseSemester[i][j][k]=model.addVar(0.0, 1.0, 0.0, GRB.BINARY,varName);
						//			System.out.println( "setting GRBVAR- max value = 1 - name= "+varName  );
						}			
						
						else{
							studentCourseSemester[i][j][k]=model.addVar(0.0, 0.0, 0.0, GRB.BINARY,varName);
							//		System.out.println( "setting GRBVAR- max value=0 - name= "+varName  );
							}
					}
				}
				
			}
			
		
			
			
			GRBVar x=model.addVar(0,studentCourseSemester.length, 0.0, GRB.INTEGER,"x");
					
			// Integrate new variables
            model.update();
			
  

            GRBLinExpr expr = new GRBLinExpr();
      //      System.out.print( " Set Objective : minimize " );
            expr.addTerm(1.0,x);
  
          
            
	  model.setObjective(expr, GRB.MINIMIZE);
			 // model.setObjective(expr, GRB.MAXIMIZE);
   // add //constraints
			  
	//    why is this constraint taking so long!
  //***************constraint student cant take more than 2 courses per semester *******************************************************************			  
	//		  System.out.println();
	//		  System.out.print( " Set constraint1 : " );
// each student add constraint per semester- student cant take more then 2 course per semester 	
			//  for (int i=1;i<=studentDemandFileReader.getNumOfStudents();i++){  //loop through each student  
			  for (int i=1;i<=studentCourseSemester.length-1;i++){  //loop through each student  
			 
			  for (int k=1;k<=TOTAL_SEMESTERS;k++){ //loop through each semester 
				  GRBLinExpr constraint = new GRBLinExpr();
	
					for (int j=1;j<=Max_NUM_COURSES_OFFERED;j++){  //loop through each course 
													
							if (studentDemandFileReader.isCourseDemandByStudent(i, j)){			
							constraint.addTerm( 1,studentCourseSemester[i][j][k]);
					//		System.out.print( " - name= "+ "1Y"+i+j+k);
							}
							else{
								constraint.addTerm( 0,studentCourseSemester[i][j][k]);
						//		System.out.print( " - name= "+ "0Y"+i+j+k);
			
							}
						}
					//add the constraint for every student per semester
					String constraintName="constraint1"+i+k;
					model.addConstr(constraint,GRB.LESS_EQUAL,2.0,constraintName);
				}
				
				}
				
				

/*			commnet out- dont think we need this anymore due to constraint to ensure all courses requested by student=1  
 //****************constraint - student cant take course more than once  ******************************************************************			  
			  System.out.println();
			  System.out.print( " Set constraint2 : " );
	// student cant take course more than once in all semesters combined		
			  for (int i=1;i<=studentDemandFileReader.getNumOfStudents();i++){  //loop through each student
				  for (int j=1;j<=Max_NUM_COURSES_OFFERED;j++){  //loop through each course
					  GRBLinExpr constraint = new GRBLinExpr();
				  for (int k=1;k<=TOTAL_SEMESTERS;k++){ //loop through each semester 
					
													
							if (studentDemandFileReader.isCourseDemandByStudent(i, j)){			
							constraint.addTerm( 1,studentCourseSemester[i][j][k]);
							System.out.print( " - name= "+ "1Y"+i+j+k);
							
							}
							else{
								constraint.addTerm( 0,studentCourseSemester[i][j][k]);
								System.out.print( " - name= "+ "0Y"+i+j+k);
			
							}
						}
					//add the constraint for every student per course 
				  String constraintName="constraint2"+i+j;
					model.addConstr(constraint,GRB.LESS_EQUAL,1.0,constraintName);
				 // model.addConstr(constraint,GRB.EQUAL,1.0,constraintName);
				}
				
				}
		  
*/
	  
 //*********************CONSTRAINT each class has to be <=x *************************************************************			  
			
// constraint number of students per course in a semester to less than equal to X 
				  for (int j=1;j<=Max_NUM_COURSES_OFFERED;j++){  //loop through each course
					  
				  for (int k=1;k<=TOTAL_SEMESTERS;k++){ //loop through each semester 
					  GRBLinExpr constraint = new GRBLinExpr();
					 // System.out.println();				
					  String constraintName="constraint3 "+"course#"+j+"semester#"+k;
					//  System.out.println( " Set constraint for course#:"+constraintName);
					  boolean anyDemandFlag=false;
					  for (int i=1;i<=studentCourseSemester.length-1;i++){  //loop through each student			
						 		
							if (studentDemandFileReader.isCourseDemandByStudent(i, j)){			
							constraint.addTerm( 1,studentCourseSemester[i][j][k]);
						//	System.out.print( " student= "+ "1Y"+i+j+k);
							anyDemandFlag=true;
							}
							else{
								constraint.addTerm( 0,studentCourseSemester[i][j][k]);
								//System.out.print( " student= "+ "0Y"+i+j+k);
			
							}
						}
					 
					  
					  if (anyDemandFlag)
							model.addConstr(constraint,GRB.LESS_EQUAL,x,constraintName);
	/*				  
					//add the constraint  per course/semester combination
					  if (anyDemandFlag){
					model.addConstr(constraint,GRB.LESS_EQUAL,x,constraintName);
					 System.out.println( " added constraint name:"+constraintName);
					  }
					  else{
						  System.out.println( " ignored constraint name:"+constraintName);
					  }
		*/
					  
					  
				  }
				
				} 

				  
		  
	

//*********************CONSTRAINT make sure each student takes all their requested courses  *************************************************************			  

	 
				  	for (int i=1;i<=studentCourseSemester.length-1;i++){  //loop through each student	
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

				//  /*fake constraint
	  //GRBLinExpr constraint = new GRBLinExpr();
	  //constraint.addConstant(5.0);		  
	 // model.addConstr(constraint,GRB.LESS_EQUAL,x,"testing");
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
				  	
  
	  
				  	
            model.optimize();
//*******************************************************************************************
//  a student can not take a course before prerequisite  - a course cano not be taken semester 1 if prereq is required
            int courseNum=0;
            String constraintName=null;
            GRBLinExpr constraint = new GRBLinExpr();
          	for (int i=1;i<=studentCourseSemester.length-1;i++){   
		  	for (int j=0;j<=coursePrerequisite.length-1;j++){  
		  		 courseNum=coursePrerequisite[j][1];
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
		  	for (int j=0;j<=coursePrerequisite.length-1;j++){  //prerequisite/course matrix 
		  			Integer preReqCourseNum=coursePrerequisite[j][0];
		  			courseNum=coursePrerequisite[j][1];
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
            
          	
         
          	       
  //**********************************************************************************
           
            // Display our results
          
            //objectiveValue = model.get(GRB.DoubleAttr.ObjVal);            
            setObjectiveValue(model.get(GRB.DoubleAttr.ObjVal));
         //   System.out.printf( "Ojective value = %f\n", objectiveValue );
        /*    
			for (int i=1;i<=studentCourseSemester.length-1;i++){  //loop through each row in matrix
				for (int j=1;j<=Max_NUM_COURSES_OFFERED;j++){  //loop through each course 
					for (int k=1;k<=TOTAL_SEMESTERS;k++){  //loop through each semester
					if (studentCourseSemester[i][j][k].get(GRB.DoubleAttr.X)==1)
				System.out.println( "student"+i+ " is taking course"+j+"in semester "+k);
					}
				}
			}
       */     
			
		
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


	private boolean isCourseAvailableInSemester(int semesterNumber, int courseNumber){
		for (int i=1;i<=courseAvailability[semesterNumber].length-1;i++)
		{
		if(courseAvailability[semesterNumber][i]==courseNumber)
			return true;
		
		}
		
		return false;
	}
	
}
