package edu.gatech.cs6310.projectOne.FileParser;

import java.util.ArrayList;
import java.util.List;


import edu.gatech.cs6310.projectOne.entity.CourseDependency;

public class CourseDependenciesFileParser implements FileParser {
	private List<CourseDependency> courseDependencies;

	public List<CourseDependency> getCourseDependencies() {
		return this.courseDependencies;
	}

	public void setCourseDependenciess(List<CourseDependency> courseDependencies) {
		this.courseDependencies = courseDependencies;
	}

	@Override
	// we are hardcoding the data for now. We can read the data and create the
	// same List<Course> with absolute transparency
	public void parseFile() {
		CourseDependency cd = new CourseDependency();
		courseDependencies = new ArrayList<CourseDependency>();
		cd.setPrerequisiteId(4);
		cd.setDependentCourseId(16);
		courseDependencies.add(cd);
		cd = new CourseDependency();
		cd.setPrerequisiteId(12);
		cd.setDependentCourseId(1);
		courseDependencies.add(cd);
		cd = new CourseDependency();
		cd.setPrerequisiteId(9);
		cd.setDependentCourseId(13);
		courseDependencies.add(cd);
		cd = new CourseDependency();
		cd.setPrerequisiteId(3);
		cd.setDependentCourseId(7);
		courseDependencies.add(cd);
	}

}
