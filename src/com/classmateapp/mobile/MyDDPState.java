package com.classmateapp.mobile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;

import com.keysolutions.ddpclient.DDPClient.DdpMessageField;
import com.keysolutions.ddpclient.DDPClient.DdpMessageType;
import com.keysolutions.ddpclient.DDPListener;
import com.keysolutions.ddpclient.android.DDPStateSingleton;

public class MyDDPState extends DDPStateSingleton {
	
	/** Contains ALL the classes available, not just the user's courses **/
	private Map<String, Course> mCourses;
	
	/** A list containing the id's of the courses the user is enrolled in **/
	private ArrayList<String> mUserCourses;
	
	/** Contains all the Tasks for the user's courses **/
	private Map<String, Task> mTasks;
	
	private String mUserEmail;
	
	/**
     * Constructor for this singleton (private because it's a singleton)
     * @param context Android application context
     */
	private MyDDPState(Context context) {
		super(context);
		mCourses = new ConcurrentHashMap<String, Course>();
		mUserCourses = new ArrayList<String>();
		mTasks = new ConcurrentHashMap<String, Task>();
	}
	
	/**
     * Used by MyApplication singleton to initialize this singleton
     * @param context Android application context
     */
    public static void initInstance(Context context) {
        // only called by MyApplication
        if (mInstance == null) {
            // Create the instance
            mInstance = new MyDDPState(context);
        }
    }
    
    /**
     * Gets only instance of this singleton
     * @return this singleton object
     */
    public static MyDDPState getInstance() {
        // Return the instance
        return (MyDDPState) mInstance;
    }
    
    /**
     * Gets current collection of Courses
     * @return courses collection
     */
    public Map<String, Course> getCourses() {
        return mCourses;
    }
    
    /**
     * Gets current collection of Tasks
     * @return tasks collection
     */
    public Map<String, Task> getTasks() {
        return mTasks;
    }
    
    /**
     * Gets user's Courses
     * @return a list of the user's enrolled courses
     */
    public ArrayList<String> getUserCourseIds() {
    	return mUserCourses;
    }
    
    public ArrayList<Course> getUserCourses() {
    	ArrayList<Course> myCourses = new ArrayList<Course>();
    	for (String courseId : mUserCourses) {
    		myCourses.add(mCourses.get(courseId));
    	}
    	Collections.sort(myCourses, new Comparator<Course>() {
    		@Override
    		public int compare(Course course1, Course course2) {
    			return course1.getTitle().compareTo(course2.getTitle());
    		}
    	});
    	return myCourses;
    }
    
    /**
     * Gets specified Course object
     * @param courseId Meteor ID of course
     * @return Course object if found
     */
    public Course getCourse(String courseId) {
        if (!mCourses.containsKey(courseId)) {
            return null;
        }
        return mCourses.get(courseId);
    }
    
    /**
     * Gets specified Task object
     * @param taskId Meteor ID of task
     * @return Task object if found
     */
    public Task getTask(String taskId) {
        if (!mTasks.containsKey(taskId)) {
            return null;
        }
        return mTasks.get(taskId);
    }
    
    public String getUserEmail() {
    	return mUserEmail;
    }
    
    public ArrayList<Course> getAlphabeticalCourseList() {
    	ArrayList<Course> myCourses = new ArrayList<Course>();
    	for (String courseId : mCourses.keySet()) {
    		myCourses.add(mCourses.get(courseId));
    	}
    	Collections.sort(myCourses, new Comparator<Course>() {
    		@Override
    		public int compare(Course course1, Course course2) {
    			return course1.getTitle().compareTo(course2.getTitle());
    		}
    	});
    	return myCourses;
    }
    
    public ArrayList<Task> getCourseTasks(String courseId, boolean validTasks) {
    	ArrayList<Task> courseTasks = new ArrayList<Task>();
    	for (String taskId : mTasks.keySet()) {
    		if (mTasks.get(taskId).getCourseId().equals(courseId)
    				&& mTasks.get(taskId).isValid() == validTasks)
    			courseTasks.add(mTasks.get(taskId));
    	}
    	Collections.sort(courseTasks, new Comparator<Task>() {
    		@Override
    		public int compare(Task task1, Task task2) {
    			Long date1 = Long.valueOf(task1.getDueDate());
    			Long date2 = Long.valueOf(task2.getDueDate());
    			return date1.compareTo(date2);
    		}
    	});
    	return courseTasks;
    }
    
    public ArrayList<Task> getDateSortedTasks(boolean validTasks) {
    	ArrayList<Task> sortedTasks = new ArrayList<Task>();
    	for (String taskId : mTasks.keySet()) {
    		if (mTasks.get(taskId).isValid() == validTasks) {
    			sortedTasks.add(mTasks.get(taskId));
    		}
    	}
    	Collections.sort(sortedTasks, new Comparator<Task>() {
    		@Override
    		public int compare(Task task1, Task task2) {
    			Long date1 = Long.valueOf(task1.getDueDate());
    			Long date2 = Long.valueOf(task2.getDueDate());
    			return date1.compareTo(date2);
    		}
    	});
    	return sortedTasks;
    }
    
    /**
     * Lets us lightly wrapper default implementation's objects
     * instead of using our own DB if we had to override 
     * the add/remove/updateDoc methods
     */
    @Override
    public void broadcastSubscriptionChanged(String collectionName,
            String changetype, String docId) {  
    	
    	if (collectionName.equals("users")) {
    		if (changetype.equals(DdpMessageType.ADDED)) {
    			@SuppressWarnings("unchecked")
    			ArrayList<Map<String, Object>> emailsMapList = (ArrayList<Map<String, Object>>) 
    					getCollection(collectionName).get(docId).get("emails");
    			mUserEmail = (String) emailsMapList.get(0).get("address");
    		}
    		if (changetype.equals(DdpMessageType.CHANGED)) {    
    			@SuppressWarnings("unchecked")
    			ArrayList<String> userCoursesMap = (ArrayList<String>) getCollection(collectionName).get(docId).get("courses");
    			mUserCourses = (ArrayList<String>) userCoursesMap;
    			for (String courseId : mUserCourses) {
    				super.subscribe("tasks", new Object[] {courseId});
    				System.out.println("Subscribed to tasks of course " + courseId);
    			}
    		}
    	}
    	
    	if (collectionName.equals("tasks")) {
    		if (changetype.equals(DdpMessageType.ADDED)) {
                mTasks.put(docId, new Task(docId, (Map<String, Object>) getCollection(collectionName).get(docId)));   
                System.out.println("Added task " + docId + " isValid: " + mTasks.get(docId).isValid());
            } else if (changetype.equals(DdpMessageType.REMOVED)) {
                mTasks.remove(docId);
                System.out.println("Removed task " + docId);
            } else if (changetype.equals(DdpMessageType.CHANGED)) {
            	mTasks.put(docId, new Task(docId, (Map<String, Object>) getCollection(collectionName).get(docId)));
            	System.out.println("Changed task " + docId);
            	// TO DO: in here, need to do real time updating???
            }
    	}
    	
        if (collectionName.equals("courses")) {
            if (changetype.equals(DdpMessageType.ADDED)) {
                mCourses.put(docId, new Course(docId, (Map<String, Object>) getCollection(collectionName).get(docId)));       
                System.out.println("Added course " + docId);
            } else if (changetype.equals(DdpMessageType.REMOVED)) {
                mCourses.remove(docId);
                System.out.println("Removed course " + docId);
            } else if (changetype.equals(DdpMessageType.CHANGED)) {
            	mCourses.put(docId, new Course(docId, (Map<String, Object>) getCollection(collectionName).get(docId)));
            	System.out.println("Changed course " + docId);
            	// TO DO: in here, need to do real time updating???
            }
        }
        // do the broadcast after we've taken care of our tasks wrapper
    	super.broadcastSubscriptionChanged(collectionName, changetype, docId);
    }
    
    /*** Meteor methods on the server go here ***/
    public void addCourse(String courseTitle) {
    	
    	Map<String, Object> courseParams = new ConcurrentHashMap<String, Object>();
    	courseParams.put("title", courseTitle);
    	
    	Object[] methodArgs = new Object[1];
    	methodArgs[0] = courseParams;
    	
    	mDDP.call("addCourse", methodArgs, new DDPListener() {
    		@Override
    		@SuppressWarnings("unchecked")
    		public void onResult(Map<String, Object> jsonFields) {
    			if (jsonFields.containsKey("error")) {
                    Map<String, Object> error = (Map<String, Object>) jsonFields
                            .get(DdpMessageField.ERROR);
                    broadcastDDPError((String) error.get("message"));
                }
    		}
    	});    	
    }
    
    public void removeCourse(String courseId) {
    	
    	Object[] methodArgs = new Object[1];
    	methodArgs[0] = courseId;
    	
    	mDDP.call("removeCourse", methodArgs, new DDPListener() {
    		@Override
    		@SuppressWarnings("unchecked")
    		public void onResult(Map<String, Object> jsonFields) {
    			if (jsonFields.containsKey("error")) {
                    Map<String, Object> error = (Map<String, Object>) jsonFields
                            .get(DdpMessageField.ERROR);
                    broadcastDDPError((String) error.get("message"));
                }
    		}
    	}); 
	}
    
    public void addTask(String title, Double dueDate, String notes, String courseId) {
    	Map<String, Object> taskParams = new ConcurrentHashMap<String, Object>();
    	taskParams.put("title", title);
    	taskParams.put("dueDate", dueDate);
    	taskParams.put("notes", notes);
    	taskParams.put("courseId", courseId);
    	
    	Object[] methodArgs = new Object[1];
    	methodArgs[0] = taskParams;
    	
    	mDDP.call("addTask", methodArgs, new DDPListener() {
    		@Override
    		@SuppressWarnings("unchecked")
    		public void onResult(Map<String, Object> jsonFields) {
    			if (jsonFields.containsKey("error")) {
                    Map<String, Object> error = (Map<String, Object>) jsonFields
                            .get(DdpMessageField.ERROR);
                    broadcastDDPError((String) error.get("message"));
                }
    		}
    	});
    }
    
    public void updateTask(String taskId, String title, Double dueDate, String notes) {
    	Map<String, Object> taskParams = new ConcurrentHashMap<String, Object>();
    	taskParams.put("_id", taskId);
    	taskParams.put("title", title);
    	taskParams.put("dueDate", dueDate);
    	taskParams.put("notes", notes);
    	
    	Object[] methodArgs = new Object[1];
    	methodArgs[0] = taskParams;
    	
    	mDDP.call("updateTask", methodArgs, new DDPListener() {
    		@Override
    		@SuppressWarnings("unchecked")
    		public void onResult(Map<String, Object> jsonFields) {
    			if (jsonFields.containsKey("error")) {
                    Map<String, Object> error = (Map<String, Object>) jsonFields
                            .get(DdpMessageField.ERROR);
                    broadcastDDPError((String) error.get("message"));
                }
    		}
    	});
    }
    
}
