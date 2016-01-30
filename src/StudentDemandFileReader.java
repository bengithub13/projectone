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
	private String csvFileName;

	public StudentDemandFileReader(String csvFileName) {
		super();
		this.csvFileName = csvFileName;
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
			//	addToStudentHashMap(studentRow);

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

		System.out.println("Done");
		return studentDemandRows;
	}

	/* 
	 * addToStudentHashMap-- add course to hashMap where key=student number and value=Set{courses for the studen}
	 * input=studentRow = array[student #, course#, semester#)
	 */
	private void addToStudentHashMap(String[] studentRow) {
		// TODO Auto-generated method stub
		Set<Integer> coursesSet = coursesPerStudentHashMap.get(studentRow[0]);
		int studentNumber = Integer.parseInt(studentRow[0]);
		int courseNumber = Integer.parseInt(studentRow[1]);
		if (coursesSet == null) {
			coursesSet = new HashSet<Integer>();
		} else {
			coursesSet.add(studentNumber);
		}

		coursesPerStudentHashMap.put(studentNumber, coursesSet);

	}
}
