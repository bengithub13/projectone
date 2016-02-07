package edu.gatech.cs6310.projectOne.entity;

public class CourseDependency {
	private int prerequisiteId;
	private int dependentCourseId;

	public CourseDependency() {
		super();
	}

	public int getPrerequisiteId() {
		return prerequisiteId;
	}

	public void setPrerequisiteId(int prerequisiteId) {
		this.prerequisiteId = prerequisiteId;
	}

	public int getDependentCourseId() {
		return dependentCourseId;
	}

	public void setDependentCourseId(int dependentCourseId) {
		this.dependentCourseId = dependentCourseId;
	}

}
