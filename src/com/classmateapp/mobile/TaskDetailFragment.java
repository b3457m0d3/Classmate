package com.classmateapp.mobile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPStateSingleton;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link TaskDetailFragment#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */

public class TaskDetailFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String TASK_ID = "taskid";
	
	//private static SimpleDateFormat sdf_short = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
	private static SimpleDateFormat sdf_long = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.US);

	/** broadcast receiver */
    private BroadcastReceiver mReceiver;
	
	// TODO: Rename and change types of parameters
	private String mTaskId;
	private Task mTask;
	private Course mCourse;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment TaskDetailFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static TaskDetailFragment newInstance(String taskId) {
		TaskDetailFragment fragment = new TaskDetailFragment();
		Bundle args = new Bundle();
		args.putString(TASK_ID, taskId);
		fragment.setArguments(args);
		return fragment;
	}

	public TaskDetailFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mTaskId = getArguments().getString(TASK_ID);
			mTask = MyDDPState.getInstance().getTask(mTaskId);
			mCourse = MyDDPState.getInstance().getCourse(mTask.getCourseId());
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater
				.inflate(R.layout.fragment_task_detail, container, false);
		
		TextView courseTitle = (TextView) view.findViewById(R.id.task_detail_course);
		courseTitle.setText(mCourse.getTitle());
		
		TextView taskTitle = (TextView) view.findViewById(R.id.task_detail_title);
		taskTitle.setText(mTask.getTitle());
		
		TextView dueDate = (TextView) view.findViewById(R.id.task_detail_duedate);
		dueDate.setText("Due on: " + sdf_long.format(new Date(mTask.getDueDate())));
		
		TextView notes = (TextView) view.findViewById(R.id.task_detail_notes);
		notes.setText(mTask.getNotes());
		
		getActivity().invalidateOptionsMenu();
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		mReceiver = new DDPBroadcastReceiver(MyDDPState.getInstance(), getActivity()) {
            @Override
            protected void onDDPConnect(DDPStateSingleton ddp) {
                super.onDDPConnect(ddp);
                // add our subscriptions needed for the activity here
                ddp.subscribe("courses", new Object[] {});
    			ddp.subscribe("currentUser", new Object[] {});
            }
            @Override
            protected void onSubscriptionUpdate(String changeType,
                    String subscriptionName, String docId) {
                if (mTaskId.equals(docId)) {
                	
                	View view = getView();
                	mTask = MyDDPState.getInstance().getTask(mTaskId);
                	mCourse = MyDDPState.getInstance().getCourse(mTask.getCourseId());

                	TextView courseTitle = (TextView) view.findViewById(R.id.task_detail_course);
                	courseTitle.setText(mCourse.getTitle());

                	TextView taskTitle = (TextView) view.findViewById(R.id.task_detail_title);
                	taskTitle.setText(mTask.getTitle());

                	TextView dueDate = (TextView) view.findViewById(R.id.task_detail_duedate);
                	dueDate.setText("Due on: " + sdf_long.format(new Date(mTask.getDueDate())));

                	TextView notes = (TextView) view.findViewById(R.id.task_detail_notes);
                	notes.setText(mTask.getNotes());
                	
                }
            }
            @Override
            protected void onLogin() {
                // update login/logout action button
                //getActivity().invalidateOptionsMenu();
            }
            @Override
            protected void onLogout() {
                // update login/logout action button
                //getActivity().invalidateOptionsMenu();
            }
        };	
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (mReceiver != null) {
            // unhook the receiver
            LocalBroadcastManager.getInstance(getActivity())
                    .unregisterReceiver(mReceiver);
            mReceiver = null;
        }
	}
	
	public String getTaskId() {
		return mTaskId;
	}

}


