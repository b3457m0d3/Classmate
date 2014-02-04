package com.classmateapp.mobile;

import java.util.ArrayList;
import java.util.Map;

public class Task extends Document {
	
	private ArrayList<Commit> mCommits;
	
	/**
     * Constructor for Task object
     * @param docId Meteor's object ID for this document
     * @param fields field/value map reference
     */
	public Task(String docId, Map<String, Object> fields) {
		super(docId, fields);
		
		mCommits = new ArrayList<Commit>();
		
		@SuppressWarnings("unchecked")
		ArrayList<Object> commits = (ArrayList<Object>) mFields.get("commits");
		for (Object commit : commits) {
			@SuppressWarnings("unchecked")
			Map<String, Object> commitFields = (Map<String, Object>) commit;
			mCommits.add(new Commit(commitFields));
		}
	}
	
	public String getTitle() {
		return mCommits.get(mCommits.size()-1).getTitle();
	}
	
	public long getDueDate() {
		return mCommits.get(mCommits.size()-1).getDueDate();
	}
	
	public String getNotes() {
		return mCommits.get(mCommits.size()-1).getNotes();
	}
	
	public Commit getCommit(int i) {
		return mCommits.get(i);
	}
	
	public int getNumCommits() {
		return mCommits.size();
	}
	
	/**
	 * Gets userId of this Task
	 * @return userId of task
	 */
	public String getTaskUserId() {
		return ((String) mFields.get("userId"));
	}
	
	/**
	 * Gets courseId of this Task
	 * @return courseId of task
	 */
	public String getCourseId() {
		String courseId =((String) mFields.get("courseId")); 
		return courseId;
	}
	
	public boolean isValid() {
		return mCommits.get(mCommits.size()-1).isValid();
	}
	
	public String toString() {
		return getTitle();
	}
	
}
