
package edu.gatech.cs6310.projectOne.scheduler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import edu.gatech.cs6310.projectOne.entity.Course;
import edu.gatech.cs6310.projectOne.entity.CourseDependency;
import edu.gatech.cs6310.projectOne.entity.StudentDemand;
import gurobi.GRB;
import gurobi.GRBEnv;
import gurobi.GRBException;
import gurobi.GRBLinExpr;
import gurobi.GRBModel;
import gurobi.GRBVar;

/*
 * author Ben Poon
 * 
 */

public class StudentScheduler implements Scheduler {
	private double objectiveValue;
	private List<StudentDemand> studentDemands;
	private List<Course> courses;
	private List<CourseDependency> courseDependencies;
	private int numOfStudents;
	private Integer[][][] studentDemandMatrix;

	private HashMap<Integer, Set<Integer>> coursesPerStudentHashMap;
	private static final int Max_NUM_COURSES_OFFERED = 18;
	private static final int TOTAL_SEMESTERS = 12;
	private GRBVar[][][] studentCourseSemester;
	private GRBModel model;
	private GRBEnv env;
	private GRBVar x;

	private void setStudentDemands(List<StudentDemand> studentDemands) {
		this.studentDemands = studentDemands;
	}

	private void setCourses(List<Course> courses) {
		this.courses = courses;

	}

	private void setCourseDependencies(List<CourseDependency> courseDependencies) {
		this.courseDependencies = courseDependencies;

	}

	public void calculateSchedule(List<StudentDemand> studentDemand, List<Course> courses,
			List<CourseDependency> courseDependencies) {

		setStudentDemands(studentDemand);
		setCourses(courses);
		setCourseDependencies(courseDependencies);
		coursesPerStudentHashMap = new HashMap<Integer, Set<Integer>>();
		countStudentDemand();

		model = initGRBVariables();
		addMaxCoursePerSemesterContraint();
		addNumStudentsPerCourseXContraint();
		fillAllStudentCoursesConstraint();
		addCourseAvailibiltyContraint();
		AddDependCourseSemester1Contraint();
		AddPrerequisiteConstraint();
		try {
			model.optimize();
			setObjectiveValue(model.get(GRB.DoubleAttr.ObjVal));

		} catch (GRBException e) {
			e.printStackTrace();
		}

	}

	private GRBModel initGRBVariables() {

		try {
			env = new GRBEnv("mip1.log");
			env.set(GRB.IntParam.OutputFlag, 0);
			model = new GRBModel(env);
			studentCourseSemester = new GRBVar[numOfStudents + 1][Max_NUM_COURSES_OFFERED + 1][TOTAL_SEMESTERS + 1];

			// studentCourseSemester = new GRBVar[studentDemands.size() +
			// 1][Max_NUM_COURSES_OFFERED + 1][TOTAL_SEMESTERS + 1];

			for (int i = 1; i <= numOfStudents; i++) {
				// for (int i = 1; i <= studentDemands.size(); i++) {

				for (int j = 1; j <= Max_NUM_COURSES_OFFERED; j++) {
					for (int k = 1; k <= TOTAL_SEMESTERS; k++) {
						String varName = "studentCourseSemester" + i + j + k;
						if (isCourseDemandByStudent(i, j)) {
							studentCourseSemester[i][j][k] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, varName);
						}

						else {
							studentCourseSemester[i][j][k] = model.addVar(0.0, 0.0, 0.0, GRB.BINARY, varName);
						}
					}
				}

			}

			x = model.addVar(0, studentCourseSemester.length, 0.0, GRB.INTEGER, "x");

			model.update();

			GRBLinExpr expr = new GRBLinExpr();
			expr.addTerm(1.0, x);

			model.setObjective(expr, GRB.MINIMIZE);

		} catch (GRBException e) {
			e.printStackTrace();
		}
		return model;

	}

	private void setObjectiveValue(double objectiveValue) {
		this.objectiveValue = objectiveValue;
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

	private void countStudentDemand() {
		for (StudentDemand studentDemand : studentDemands) {
			addToStudentHashMap(studentDemand);
		}
	}

	private boolean isCourseAvailableInSemester(int semesterNumber, int courseId) {
		for (Course course : courses) {
			if (courseId == course.getCourseId()) {
				if (course.isCourseAvailableInSemester(semesterNumber)) {
					return true;
				}
			}
		}

		return false;
	}

	private void addMaxCoursePerSemesterContraint() {
		try {
			for (int i = 1; i <= studentCourseSemester.length - 1; i++) {

				for (int k = 1; k <= TOTAL_SEMESTERS; k++) { // loop through
																// each semester
					GRBLinExpr constraint = new GRBLinExpr();

					for (int j = 1; j <= Max_NUM_COURSES_OFFERED; j++) { // loop
																			// through
																			// each
																			// course
						if (isCourseDemandByStudent(i, j)) {
							constraint.addTerm(1, studentCourseSemester[i][j][k]);
						} else {
							constraint.addTerm(0, studentCourseSemester[i][j][k]);
						}
					}
					String constraintName = "constraint1" + i + k;
					model.addConstr(constraint, GRB.LESS_EQUAL, 2.0, constraintName);
				}

			}
		} catch (GRBException e) {
			e.printStackTrace();
		}

	}

	private void addNumStudentsPerCourseXContraint() {
		try {
			for (int j = 1; j <= Max_NUM_COURSES_OFFERED; j++) {

				for (int k = 1; k <= TOTAL_SEMESTERS; k++) {
					GRBLinExpr constraint = new GRBLinExpr();
					String constraintName = "constraint3 " + "course#" + j + "semester#" + k;
					boolean anyDemandFlag = false;
					for (int i = 1; i <= studentCourseSemester.length - 1; i++) { //
						if (isCourseDemandByStudent(i, j)) {
							constraint.addTerm(1, studentCourseSemester[i][j][k]);
							anyDemandFlag = true;
						} else {
							constraint.addTerm(0, studentCourseSemester[i][j][k]);

						}
					}
					if (anyDemandFlag)
						model.addConstr(constraint, GRB.LESS_EQUAL, x, constraintName);
				}
			}
		} catch (GRBException e) {
			e.printStackTrace();
		}
	}

	/*********************************************************************************************************
	 * comment out - should iterate through courseHashmap instead since it might
	 * contain prereqs added private void fillAllStudentCoursesConstraint() {
	 * try { for (StudentDemand stdemand : studentDemands) {
	 * 
	 * GRBLinExpr constraint = new GRBLinExpr(); String constraintName =
	 * "constraint4 " + "student#" + stdemand.getStudentId() + "course#" +
	 * stdemand.getCourseId();
	 * 
	 * for (int k = 1; k <= TOTAL_SEMESTERS; k++) { constraint.addTerm(1,
	 * studentCourseSemester[stdemand.getStudentId()][stdemand.getCourseId()][k]
	 * ); } model.addConstr(constraint, GRB.EQUAL, 1, constraintName); } } catch
	 * (GRBException e) { e.printStackTrace(); } }
	 ************************************************************************************************************
	 */
	private void fillAllStudentCoursesConstraint() {
		try {
			for (int i = 1; i <= numOfStudents; i++) {
				Set<Integer> coursesSet = coursesPerStudentHashMap.get(i);
				Iterator<Integer> cIterator = coursesSet.iterator();

				while (cIterator.hasNext()) {
					int courseNum = cIterator.next();
					GRBLinExpr constraint = new GRBLinExpr();
					String constraintName = "constraint4 " + "student#" + i + "course#" + courseNum;

					for (int k = 1; k <= TOTAL_SEMESTERS; k++) {
						constraint.addTerm(1, studentCourseSemester[i][courseNum][k]);
					}
					model.addConstr(constraint, GRB.EQUAL, 1, constraintName);
				}

			}

		} catch (GRBException e) {
			e.printStackTrace();
		}

	}

	private void addCourseAvailibiltyContraint() {
		try {
			for (int i = 1; i <= studentCourseSemester.length - 1; i++) {
				for (int j = 1; j <= Max_NUM_COURSES_OFFERED; j++) {
					GRBLinExpr constraint = new GRBLinExpr();
					String constraintName = "constraint5-  " + "student#" + i + "course#" + j;
					for (int k = 1; k <= TOTAL_SEMESTERS; k++) {
						if (!(isCourseAvailableInSemester(k, j)))
							constraint.addTerm(1, studentCourseSemester[i][j][k]);
					}
					model.addConstr(constraint, GRB.EQUAL, 0, constraintName);
				}
			}
		} catch (GRBException e) {
			e.printStackTrace();
		}
	}

	/*
	 * dependent course cant be taken in 1st semester since it requires
	 * prerequisite
	 */
	private void AddDependCourseSemester1Contraint() {
		int courseNum = 0;
		String constraintName = null;
		GRBLinExpr constraint = new GRBLinExpr();
		try {
			for (int i = 1; i <= studentCourseSemester.length - 1; i++) {
				for (CourseDependency courseDependency : courseDependencies) {
					courseNum = courseDependency.getDependentCourseId();
					constraintName = "constraint_prereq1-  " + "student#" + i + "course#" + courseNum;
					/*
					 * for (int k = 1; k <= TOTAL_SEMESTERS; k++) {
					 * 
					 * constraint.addTerm(1,
					 * studentCourseSemester[i][courseNum][1]); }
					 */
					constraint.addTerm(1, studentCourseSemester[i][courseNum][1]);

				}
			}

			model.addConstr(constraint, GRB.EQUAL, 0, constraintName);
		} catch (GRBException e) {
			e.printStackTrace();
		}
	}

	/*
	 * AddPrerequisite constraint. For any dependent course " ex : course 3
	 * dependant course in semester 2 + course 6 prereq course in semester
	 * 3,4,5,6,7,8,9,10,11,12 can not be >1. course 3 dependant course in
	 * semester 3 + course 6 prereq course in semester 4,5,6,7,8,9,10,11,12 can
	 * not be >1.
	 * 
	 */
	private void AddPrerequisiteConstraint() {
		GRBLinExpr constraint = null;
		try {
			for (int i = 1; i <= studentCourseSemester.length - 1; i++) { // for
																			// each
																			// student
				for (CourseDependency courseDependency : courseDependencies) { // for
																				// each
																				// prereq/course
																				// combos
					int courseNum = courseDependency.getDependentCourseId();
					Integer preReqCourseNum = courseDependency.getPrerequisiteId();

					String constraintName = "constraint_prereq2-  " + "student#" + i + "course#" + courseNum;

					for (int k = 2; k <= TOTAL_SEMESTERS; k++) {
						constraint = new GRBLinExpr();
						constraint.addTerm(1, studentCourseSemester[i][courseNum][k]); // dependent
																						// course
																						// for
																						// student
																						// for
																						// semester
																						// k

						for (int k2 = k; k2 <= TOTAL_SEMESTERS; k2++) {
							constraint.addTerm(1, studentCourseSemester[i][preReqCourseNum][k2]); // prereq
																									// course
																									// for
																									// semestet
																									// sum
																									// of
																									// all
																									// k2

						}
						model.addConstr(constraint, GRB.LESS_EQUAL, 1, constraintName);
					}
				}
			}
		} catch (GRBException e) {
			e.printStackTrace();
		}
	}

	private boolean isCourseDemandByStudent(int studentNumber, int courseNumber) {

		Set<Integer> coursesSet = coursesPerStudentHashMap.get(studentNumber);
		if (coursesSet == null) {
			return false;
		} else if (coursesSet.contains(courseNumber)) {
			// testing if prereq requested with dependent course
		//	checkPrereqRequested(studentNumber, courseNumber);
			//

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
		// add prereq?
		addPreRequisite(studentNumber, courseNumber);
		
	}

	public void printSchedule() {
		try {
			for (int i = 1; i <= studentCourseSemester.length - 1; i++) { // loop
																			// through
																			// each
																			// row
																			// in
																			// matrix
				for (int j = 1; j <= Max_NUM_COURSES_OFFERED; j++) { // loop
																		// through
																		// each
																		// course
					for (int k = 1; k <= TOTAL_SEMESTERS; k++) { // loop through
																	// each
																	// semester
						if (studentCourseSemester[i][j][k].get(GRB.DoubleAttr.X) == 1)
							System.out.println("student" + i + " is taking course" + j + "in semester " + k);
					}
				}
			}
		}

		catch (GRBException e) {
			e.printStackTrace();
		}
	}

	// add prereq for student if they request dep course without requesting
	// prereq
	private void addPreRequisite(int studentNumber, int courseNumber) {
		for (CourseDependency courseDependency : courseDependencies) {
			int depCourseNum = courseDependency.getDependentCourseId();
			int preCourseNum = courseDependency.getPrerequisiteId();
			if (courseNumber == depCourseNum) { // is prereq in the set of
												// students request?
				if (!(coursesPerStudentHashMap.get(studentNumber).contains(preCourseNum))) {
					// System.out.println("student id=
					// "+studentNumber+"dependent course "+courseNumber +"does
					// not have prereq in set prereq= "+preCourseNum +"adding
					// it");
					Set<Integer> coursesSet = coursesPerStudentHashMap.get(studentNumber);
					coursesSet.add(preCourseNum);
					coursesPerStudentHashMap.put(studentNumber, coursesSet);
				}

			}
		}

	}


	private void checkPrereqRequested(int studentNumber, int courseNumber) {
		for (CourseDependency courseDependency : courseDependencies) {
			int depCourseNum = courseDependency.getDependentCourseId();
			int preCourseNum = courseDependency.getPrerequisiteId();
			if (courseNumber == depCourseNum) { // is prereq in the set of
												// students request?
				if (coursesPerStudentHashMap.get(studentNumber).contains(preCourseNum)) {
					// System.out.println("student id= "+studentNumber+"
					// dependent course "+courseNumber +"has the prereq in set
					// prereq= "+preCourseNum);
				} else {
					System.out.println("student id= " + studentNumber + "dependent course " + courseNumber
							+ "does not have prereq in set prereq= " + preCourseNum);

				}

			}
		}

	}

}
