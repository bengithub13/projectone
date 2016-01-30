import java.util.List;
import java.util.Vector;

import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;

public class Project1Scheduler implements Scheduler {
	private StudentDemandFileReader studentDemandFileReader;
    private static final int Max_NUM_COURSES_OFFERED=18;
    private static final int TOTAL_SEMESTERS=12;
	private static final int MAX_CLASS_SIZE = 2;	// Note: Play with this and see 
    private GRBVar[][][] yijk;		               // what happens to the results
    private List<String[]> studentDemandMatrix;
    
    
    private void setStudentDemandFileReader(StudentDemandFileReader studentDemandFileReader){
    	this.studentDemandFileReader=studentDemandFileReader;
    }
	public void calculateSchedule(String dataFolder){ 

		// TODO Read the test data from the provided folder

		// The following code is an example of how to use the Gurobi solver.
		// Replace the variables, objective, and constraints with those
		// needed to calculate the schedule for the provided data.
		
		// This example has three students and two classes.  Each class is
		// limited to two students. The objective is to maximize the total 
		// number of student-classes taken. It do not deal with semesters
		
		
		
        GRBEnv env;
		try {
			env = new GRBEnv("mip1.log");
			GRBModel model = new GRBModel(env);
			setStudentDemandFileReader(new StudentDemandFileReader(dataFolder));
			studentDemandMatrix=studentDemandFileReader.getStudentDemandRows();
		//Create the variables -- add a variable for each "POSSIBLE" combination of Yijk
		//i want to start the array indexes at 1 to keep sanity.	
			yijk=new GRBVar[(studentDemandMatrix.size())+1][Max_NUM_COURSES_OFFERED+1][TOTAL_SEMESTERS+1];
			
			for (int i=1;i<=yijk.length-1;i++){  //loop through each student
				for (int j=1;j<=Max_NUM_COURSES_OFFERED;j++){  //loop through each course 
					for (int k=1;k<=TOTAL_SEMESTERS;k++){  //loop through each course 
						String varName="Y"+i+j+k;
						yijk[i][j][k]=model.addVar(0.0, 1.0, 0.0, GRB.BINARY,varName);
					    System.out.println( "setting GRBVAR - name= "+varName );
					}
				}
			}
			
			// Create the variables
			GRBVar gvarJoeCS6300 = model.addVar(0, 1, 0.0, GRB.BINARY, "Joe_CS6300");
			GRBVar gvarJoeCS6310 = model.addVar(0, 1, 0.0, GRB.BINARY, "Joe_CS6310");
			GRBVar gvarJaneCS6300 = model.addVar(0, 1, 0.0, GRB.BINARY, "Jane_CS6300");
			GRBVar gvarJaneCS6310 = model.addVar(0, 1, 0.0, GRB.BINARY, "Jane_CS6310");
			GRBVar gvarMaryCS6300 = model.addVar(0, 1, 0.0, GRB.BINARY, "Mary_CS6300");
			GRBVar gvarMaryCS6310 = model.addVar(0, 1, 0.0, GRB.BINARY, "Mary_CS6310");
			
			 
			
			
			
			
			// Integrate new variables
            model.update();
			
  
            // Set the objective as the sum of all student-courses-semisters
 /*
            GRBLinExpr expr = new GRBLinExpr();
            expr.addTerm( 1, gvarJoeCS6300 );
            expr.addTerm( 1, gvarJoeCS6310 );
            expr.addTerm( 1, gvarJaneCS6300 );
            expr.addTerm( 1, gvarJaneCS6310 );
            expr.addTerm( 1, gvarMaryCS6300 );
            expr.addTerm( 1, gvarMaryCS6310 );
            
            model.setObjective(expr, GRB.MAXIMIZE);
*/
            GRBLinExpr expr = new GRBLinExpr();
			for (int i=1;i<=yijk.length-1;i++){  //loop through each student
				for (int j=1;j<=Max_NUM_COURSES_OFFERED;j++){  //loop through each course 
					for (int k=1;k<=TOTAL_SEMESTERS;k++){  //loop through each course 
						String varName="Y"+i+j+k;
//	i student wants to take course set to 1 else set to 0 since we do not want to consider it .
						
						
						
						expr.addTerm( 1, gvarJoeCS6300 );
						
						
						yijk[i][j][k]=model.addVar(0.0, 1.0, 0.0, GRB.BINARY,varName);
					    System.out.println( "setting GRBVAR - name= "+varName );
					}
				}
			}
            
            
            
            
            
            
			// Add Constraints for each class so that the sum of students taking
            // the course is less than or equal to MAX_CLASS_SIZE
            expr = new GRBLinExpr();
            expr.addTerm( 1, gvarJoeCS6300 );
            expr.addTerm( 1, gvarJaneCS6300 );
            expr.addTerm( 1, gvarMaryCS6300 );
            model.addConstr(expr, GRB.LESS_EQUAL, MAX_CLASS_SIZE, "CS6300" );

            expr = new GRBLinExpr();
            expr.addTerm( 1, gvarJoeCS6310 );
            expr.addTerm( 1, gvarJaneCS6310 );
            expr.addTerm( 1, gvarMaryCS6310 );
            model.addConstr(expr, GRB.LESS_EQUAL, MAX_CLASS_SIZE, "CS6310" );

            // Optimize the model
            model.optimize();

            // Display our results
            double objectiveValue = model.get(GRB.DoubleAttr.ObjVal);            
            System.out.printf( "Ojective value = %f\n", objectiveValue );
            
            if( gvarJoeCS6300.get(GRB.DoubleAttr.X) == 1 )
                System.out.printf( "Joe is taking CS6300\n" );            	
            if( gvarJoeCS6310.get(GRB.DoubleAttr.X) == 1 )
                System.out.printf( "Joe is taking CS6310\n" );            	
            if( gvarJaneCS6300.get(GRB.DoubleAttr.X) == 1 )
                System.out.printf( "Jane is taking CS6300\n" );            	
            if( gvarJaneCS6310.get(GRB.DoubleAttr.X) == 1 )
                System.out.printf( "Jane is taking CS6310\n" );            	
            if( gvarMaryCS6300.get(GRB.DoubleAttr.X) == 1 )
                System.out.printf( "Mary is taking CS6300\n" );            	
            if( gvarMaryCS6310.get(GRB.DoubleAttr.X) == 1 )
                System.out.printf( "Mary is taking CS6310\n" );            	
                        
            
		} catch (GRBException e) {
			e.printStackTrace();
		}

		
	}
	
	//private boolean isCourseForStudent(int studentNumber, Course Number)

	public double getObjectiveValue() {
		// TODO: You will need to implement this
		return 0;
	}

	public Vector<String> getCoursesForStudentSemester(String student, String semester) {
		// TODO: You will need to implement this
		return null;
	}

	public Vector<String> getStudentsForCourseSemester(String course, String semester) {
		// TODO: You will need to implement this
		return null;
	}


	
	
}
