package com.classmateapp.mobile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPStateSingleton;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class TasksOfCourseFragment extends ListFragment {

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String COURSE_ID = "courseid";
	
	private OnItemClickListener mClickListener;
	
	//private static SimpleDateFormat sdf_short = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
	private static SimpleDateFormat sdf_long = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.US);

	// TODO: Rename and change types of parameters
	private String mCourseId;
	
	/** broadcast receiver */
    private BroadcastReceiver mReceiver;

//	private static MainActivity.MyCoursesPageFragmentListener mListener;

	// TODO: Rename and change types of parameters
	public static TasksOfCourseFragment newInstance(String courseId) {
		TasksOfCourseFragment fragment = new TasksOfCourseFragment();		
		Bundle args = new Bundle();
		args.putString(COURSE_ID, courseId);
		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public TasksOfCourseFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments() != null) {
			mCourseId = getArguments().getString(COURSE_ID);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		
        // get ready to handle DDP events
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
                if (subscriptionName.equals("tasks")) {
                    // show any new parties
                    showTasksOfCourse();
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
        MyDDPState.getInstance().connectIfNeeded();    // start connection process if we're not connected
        showTasksOfCourse();
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

	private void showTasksOfCourse() {
    	
    	ArrayList<Task> tasksOfCourse = MyDDPState.getInstance().getCourseTasks(mCourseId, true);
    	
    	String[] from = new String[] {"title", "dueDate"};
        int[] to = new int[] { R.id.task_item_title, R.id.task_item_date };
 
        // prepare the list of all records
        ArrayList<Map<String, String>> fillMaps = new ArrayList<Map<String, String>>();
        
        for (Task task : tasksOfCourse) {
        	Map<String, String> map = new ConcurrentHashMap<String, String>();
        	map.put("title", task.getTitle());
        	map.put("dueDate", sdf_long.format(new Date(task.getDueDate())));
        	fillMaps.add(map);
        }
    	
    	SimpleAdapter adapter = new SimpleAdapter(getActivity(), fillMaps, R.layout.task_template, from, to);
    	setListAdapter(adapter);
    }
	
//	@Override
//	public void onAttach(Activity activity) {
//		super.onAttach(activity);
//		try {
//			mDummyListener = (OnFragmentInteractionListener) activity;
//		} catch (ClassCastException e) {
//			throw new ClassCastException(activity.toString()
//					+ " must implement OnFragmentInteractionListener");
//		}
//	}

//	@Override
//	public void onDetach() {
//		super.onDetach();
//		mDummyListener = null;
//	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tasks_of_course, container, false);
		
		String courseTitle = MyDDPState.getInstance().getCourse(mCourseId).getTitle();
		TextView header = (TextView) view.findViewById(R.id.tasks_of_course_header);
		header.setText("Tasks for " + courseTitle);
		
		getActivity().invalidateOptionsMenu();
		
		return view;
	}
	
	public void setOnItemClickListener(OnItemClickListener listener) {
		mClickListener = listener;
    }
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(mClickListener != null) {
        	mClickListener.onItemClick(l, v, position, id);
        }
    }

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(String id);
	}
	
	public String getCourseId() {
		return mCourseId;
	}

}
