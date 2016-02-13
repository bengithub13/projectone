package edu.gatech.cs6310.projectOne.entity;

public class StudentDemand {
private int studentId;
private int courseId;
private int semesterId;
public int getStudentId() {
	return studentId;
}
public void setStudentId(int studentId) {
	this.studentId = studentId;
}
public int getCourseId() {
	return courseId;
}
public void setCourseId(int courseId) {
	this.courseId = courseId;
}
public int getSemesterId() {
	return semesterId;
}
public void setSemesterId(int semesterId) {
	this.semesterId = semesterId;
}
public StudentDemand() {
	super();
	// TODO Auto-generated constructor stub
}



}
