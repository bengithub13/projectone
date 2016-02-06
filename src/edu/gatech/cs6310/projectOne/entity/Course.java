package edu.gatech.cs6310.projectOne.entity;

public class Course {
	private int courseId;
	private int courseName;
	private int courseNumber;
	private int fallTerm;
	private int springTerm;
	private int summerTerm;
	private String availabiliy;

	public Course() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getCourseName() {
		return courseName;
	}

	public void setCourseName(int courseName) {
		this.courseName = courseName;
	}

	public int getCourseNumber() {
		return courseNumber;
	}

	public void setCourseNumber(int courseNumber) {
		this.courseNumber = courseNumber;
	}

	public int getFallTerm() {
		return fallTerm;
	}

	public void setFallTerm(int fallTerm) {
		this.fallTerm = fallTerm;
	}

	public int getSpringTerm() {
		return springTerm;
	}

	public void setSpringTerm(int springTerm) {
		this.springTerm = springTerm;
	}

	public int getSummerTerm() {
		return summerTerm;
	}

	public void setSummerTerm(int summerTerm) {
		this.summerTerm = summerTerm;
	}

	public String getAvailabiliy() {
		return availabiliy;
	}

	public void setAvailabiliy(String availabiliy) {
		this.availabiliy = availabiliy;
	}

	public boolean isCourseAvailableInSemester(int semesterNumber) {
		boolean isAvailable;
		switch (semesterNumber) {
		case 1:						//fall semester
			if (this.fallTerm==1)
			isAvailable=true;
			else isAvailable=false;
			break;
		case 2:						//spring semester
			if (this.springTerm==1)
				isAvailable=true;
				else isAvailable=false;
				break;
		case 3:						//summer semester
			if (this.summerTerm==1)
				isAvailable=true;
				else isAvailable=false;
				break;
		case 4:
			if (this.fallTerm==1)
				isAvailable=true;
				else isAvailable=false;
				break;
		case 5:
			if (this.springTerm==1)
				isAvailable=true;
				else isAvailable=false;
				break;
		case 6:
			if (this.summerTerm==1)
				isAvailable=true;
				else isAvailable=false;
				break;
		case 7:
			if (this.fallTerm==1)
				isAvailable=true;
				else isAvailable=false;
				break;
		case 8:
			if (this.springTerm==1)
				isAvailable=true;
				else isAvailable=false;
				break;
		case 9:
			if (this.summerTerm==1)
				isAvailable=true;
				else isAvailable=false;
				break;
		case 10:
			if (this.fallTerm==1)
				isAvailable=true;
				else isAvailable=false;
				break;
		case 11:
			if (this.springTerm==1)
				isAvailable=true;
				else isAvailable=false;
				break;
		case 12:
			if (this.summerTerm==1)
				isAvailable=true;
				else isAvailable=false;
				break;
	
		default:
			isAvailable=false;
			break;

		}

		return isAvailable;

	}

}
