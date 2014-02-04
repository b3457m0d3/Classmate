package com.classmateapp.mobile;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link WrapperMyCoursesFragment#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class WrapperMyCoursesFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//	private static final String ARG_PARAM1 = "param1";
//	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
//	private String mParam1;
//	private String mParam2;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment WrapperMyCoursesFragment.
	 */
	
	private MyCoursesFragment mMyCoursesFragment;
	
	/** broadcast receiver */
	@SuppressWarnings("unused")
    private BroadcastReceiver mReceiver;
	
	// TODO: Rename and change types and number of parameters
	public static WrapperMyCoursesFragment newInstance() {
		WrapperMyCoursesFragment fragment = new WrapperMyCoursesFragment();
//		Bundle args = new Bundle();
//		args.putString(ARG_PARAM1, param1);
//		args.putString(ARG_PARAM2, param2);
//		fragment.setArguments(args);
		return fragment;
	}

	public WrapperMyCoursesFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
//			mParam1 = getArguments().getString(ARG_PARAM1);
//			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		mMyCoursesFragment = MyCoursesFragment.newInstance();
		mMyCoursesFragment.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> l, View v, int position, long id) {
				final String courseId = MyDDPState.getInstance().getUserCourses().get(position).getId();
				
				TasksOfCourseFragment fragment = TasksOfCourseFragment.newInstance(courseId);
				fragment.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> l, View v, int position, long id) {
						String taskId = MyDDPState.getInstance().getCourseTasks(courseId, true).get(position).getId();
						Fragment fragment = TaskDetailFragment.newInstance(taskId);
						getChildFragmentManager()
							.beginTransaction()
							.replace(R.id.container, fragment)
							.addToBackStack(null)
							.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
							.commit();
						getActivity().invalidateOptionsMenu();
					}
				});
				getChildFragmentManager()
					.beginTransaction()
					.replace(R.id.container, fragment)
					.addToBackStack(null)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
					.commit();
				getActivity().invalidateOptionsMenu();
			}
		});
		getChildFragmentManager()
			.beginTransaction()
			.add(R.id.container, mMyCoursesFragment)
			.commit();
		getActivity().invalidateOptionsMenu();
	}

	public void onBackPressed() {
		if (getView() != null) {
			if (!getChildFragmentManager().popBackStackImmediate()) {
				getActivity().finish();
			}
		}
	}
	
	public void onTabReselected() {
		if (getView() != null) {
			getChildFragmentManager().popBackStackImmediate();
		}
	}
	
	public void onCourseAdded() {
		if (getView() != null) {
			getChildFragmentManager().popBackStackImmediate();
		}
		getActivity().invalidateOptionsMenu();
	}
	
	public void onTaskSubmitted() {
		if (getView() != null) {
			getChildFragmentManager().popBackStackImmediate();
		}
		getActivity().invalidateOptionsMenu();
	}
	
	public void onAddNewCourse() {
		AddCourseFragment fragment = AddCourseFragment.newInstance();
		fragment.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> l, View v, int position, long id){
				InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
		        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
				MyDDPState.getInstance().addCourse(((Course)l.getItemAtPosition(position)).getTitle());
				onCourseAdded();
			}
		});
		getChildFragmentManager()
			.beginTransaction()
			.replace(R.id.container, fragment)
			.addToBackStack(null)
			.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
			.commit();
		getActivity().invalidateOptionsMenu();
	}
	
	public void onAddNewTask(String courseId) {
		AddTaskFragment fragment = AddTaskFragment.newInstance(courseId);
		getChildFragmentManager()
			.beginTransaction()
			.replace(R.id.container, fragment)
			.addToBackStack(null)
			.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
			.commit();
		getActivity().invalidateOptionsMenu();
	}
	
	public void onSubmitTask() {
		((AddTaskFragment) getCurrentFragment()).onSubmitTask();
	}
	
	public void onEditTask(String taskId) {
		EditTaskFragment fragment = EditTaskFragment.newInstance(taskId);
		getChildFragmentManager()
			.beginTransaction()
			.replace(R.id.container, fragment)
			.addToBackStack(null)
			.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
			.commit();
		getActivity().invalidateOptionsMenu();
	}
	
	public void onTaskEdited() {
		if (getView() != null) {
			getChildFragmentManager().popBackStackImmediate();
		}
		getActivity().invalidateOptionsMenu();
	}
	
	public void onSubmitEditedTask() {
		((EditTaskFragment) getCurrentFragment()).onSubmitTask();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_wrapper_my_courses,
				container, false);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		// get ready to handle DDP events
	}
	
	public Fragment getCurrentFragment() {
		return getChildFragmentManager().findFragmentById(R.id.container);
	}

}
