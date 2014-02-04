package com.classmateapp.mobile;

import java.util.Map;

public class Course extends Document {
	
	/**
     * Constructor for Course object
     * @param docId Meteor's object ID for this document
     * @param fields field/value map reference
     */
	public Course(String docId, Map<String, Object> fields) {
		super(docId, fields);
	}
	
	/**
	 * Gets title of this Course
	 * @return title of course
	 */
	public String getTitle() {
		return ((String) mFields.get("title"));
	}
	
	/**
	 * Gets userId of this Course
	 * @return userId of course
	 */
	public String getUserId() {
		return ((String) mFields.get("userId"));
	}
	
	/**
	 * Gets username of this Course
	 * @return username of course
	 */
	public String getUsername() {
		return ((String) mFields.get("username"));
	}
	
	/**
	 * Gets submittedDate of this Course
	 * @return submittedDate of course
	 */
	public int getSubmittedDate() {
		return ((Double) mFields.get("submittedDate")).intValue();
	}
	
	/**
	 * Gets tasksCount of this Course
	 * @return tasks Count of course
	 */
	public int getTasksCount() {
		return ((Double) mFields.get("tasksCount")).intValue();
	}	
	
	@Override
	public String toString() {
		return getTitle();
	}
	
}
