 	package edu.gatech.cs6310.projectOne.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StudentDemandFileReader {

	private HashMap<Integer, Set<Integer>> coursesPerStudentHashMap;
	private HashMap<Integer, Set<Integer>> coursesTotalDemandHashMap;
	
	private String csvFileName;
	private int numberOfStudent=0;

	public StudentDemandFileReader(String csvFileName) {
		super();
		this.csvFileName = csvFileName;
		coursesPerStudentHashMap=new HashMap<Integer,Set<Integer>>();
		coursesTotalDemandHashMap=new HashMap<Integer,Set<Integer>>();
	}

	/*
	 * read csv file where each rows has 3 columns returns represeting a line in
	 * the student demand csv file
	 */
	public List<String[]> getStudentDemandRows() {

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		int row = 0;
		List<String[]> studentDemandRows = new ArrayList<String[]>();
		int column = 0;
		try {

			br = new BufferedReader(new FileReader(csvFileName));
			line = br.readLine(); // skip first line
			while ((line = br.readLine()) != null) {

				// read each row

				String[] studentRow = line.split(cvsSplitBy);
				studentDemandRows.add(studentRow);
				addToStudentHashMap(studentRow);
				addToCourseDemandHashMap(studentRow); 

				// System.out.println("Student row: [student id= " +
				// studentRow[0]
				// + " courseid= " + studentRow[1] + " semesterid=
				// "+studentRow[2]);
				// return studentRow;
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	//	System.out.println("Done");
		return studentDemandRows;
	}

	/* 
	 * addToStudentHashMap-- add course to hashMap where key=student number and value=Set{courses for the studen}
	 * input=studentRow = array[student #, course#, semester#)
	 */
	private void addToStudentHashMap(String[] studentRow) {
		// TODO Auto-generated method stub
		
		int studentNumber = Integer.parseInt(studentRow[0]);
		int courseNumber = Integer.parseInt(studentRow[1]);
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
	
	private void addToCourseDemandHashMap(String[] studentRow) {
		// TODO Auto-generated method stub
		
		int studentNumber = Integer.parseInt(studentRow[0]);
		int courseNumber = Integer.parseInt(studentRow[1]);
		Set<Integer> studentSet = coursesTotalDemandHashMap.get(courseNumber);
		if (studentSet == null) {
			studentSet = new HashSet<Integer>();
			studentSet.add(studentNumber);
		} else {
			studentSet.add(studentNumber);
		}

		coursesTotalDemandHashMap.put(courseNumber, studentSet);

	}
	
	public Integer[] getCoursesForStudent(int studentNumber){
		HashSet<Integer> coursesSet = (HashSet<Integer>) coursesPerStudentHashMap.get(studentNumber);
		if (coursesSet==null)
		return null;
		else
			return  coursesSet.toArray(new Integer[coursesSet.size()]);
	}
	
	
	public boolean isCourseDemandByStudent(int studentNumber, int courseNumber){
		Set<Integer> coursesSet = coursesPerStudentHashMap.get(studentNumber);
		if (coursesSet == null) {
		return false;
		}
		else if  (coursesSet.contains(courseNumber)){
			return true;
		}
		return false;
	}
	
	private void addNumOfStudents(){
		numberOfStudent++;
	}
	
	
	
	public int getNumOfStudents(){
		
		return numberOfStudent;
		
	};
	
}
