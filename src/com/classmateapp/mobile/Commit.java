package com.classmateapp.mobile;

import java.util.Map;

public class Commit {
	
	/** All of the fields for this commit **/
	private Map<String, Object> mFields;
	
	/**
     * Constructor for Commit object
     * @param fields field/value map reference
     */
	public Commit(Map<String, Object> fields) {
		this.mFields = fields;
	}
	
	public String getTitle() {
		return ((String) mFields.get("title"));
	}
	
	/**
	 * Gets userId of this commit
	 * @return userId of commit
	 */
	public String getUserId() {
		return ((String) mFields.get("userId"));
	}
	
	/**
	 * Gets username of this commit
	 * @return username of commit
	 */
	public String getUsername() {
		return ((String) mFields.get("username"));
	}
	
	/**
	 * Gets submittedDate of this commit
	 * @return submittedDate of commit
	 */
	public int getSubmittedDate() {
		return ((Double) mFields.get("submittedDate")).intValue();
	}
	
	/**
	 * Gets dueDate of this commit
	 * @return dueDate of commit
	 */
	public long getDueDate() {
		return ((Double) mFields.get("dueDate")).longValue();
	}	
	
	public boolean isValid() {
		return ((Boolean) mFields.get("valid"));
	}
	
	/**
	 * Gets notes of this commit
	 * @return notes of commit
	 */
	public String getNotes() {
		return ((String) mFields.get("notes"));
	}
	
}
