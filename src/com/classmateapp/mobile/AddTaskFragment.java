package com.classmateapp.mobile;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link AddTaskFragment#newInstance} factory method to create an instance of
 * this fragment.
 * 
 */
public class AddTaskFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	
	private static final String COURSE_ID = "courseid";
	
	//private static SimpleDateFormat sdf_short = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
	private static SimpleDateFormat sdf_long = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.US);
	
	// TODO: Rename and change types of parameters
	private String mCourseId;
	private String mCourseTitle;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment AddTaskFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static AddTaskFragment newInstance(String courseId) {
		AddTaskFragment fragment = new AddTaskFragment();
		Bundle args = new Bundle();
		args.putString(COURSE_ID, courseId);
		fragment.setArguments(args);
		return fragment;
	}

	public AddTaskFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mCourseId = getArguments().getString(COURSE_ID);
			mCourseTitle = MyDDPState.getInstance().getCourse(mCourseId).getTitle();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_add_task, container, false);
		
		TextView titleTextView = (TextView) view.findViewById(R.id.add_task_course_title);
		titleTextView.setText(" " + mCourseTitle + ":");
		
		TextView dateTextView = (TextView) view.findViewById(R.id.add_task_date);
		final Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 7);
		Date date = c.getTime();
		String dateString = sdf_long.format(date);
		dateTextView.setText(dateString);
		
		getActivity().invalidateOptionsMenu();
		
		return view;
	}
	
	public void onSubmitTask() {
		
		EditText titleView = (EditText) getView().findViewById(R.id.add_task_title);
		String title = titleView.getText().toString();
		if (title.equals("")) {
    		CharSequence text = "Please enter a task title.";
    		Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    		return;
    	}
		
		TextView dateTextView = (TextView) getView().findViewById(R.id.add_task_date);
    	String dueDateString = dateTextView.getText().toString();
    	
    	Double dueDate = Double.valueOf((Calendar.getInstance().getTime().getTime()));
    	try {
    		dueDate = Double.valueOf(sdf_long.parse(dueDateString).getTime());
    	} catch (Exception e) {
    		
    	}
    	
    	EditText notesView = (EditText) getView().findViewById(R.id.add_task_notes);
		String notes = notesView.getText().toString();
    	
    	MyDDPState.getInstance().addTask(title, dueDate, notes, mCourseId);
    	
    	((WrapperMyCoursesFragment) getParentFragment()).onTaskSubmitted();
	}

}
