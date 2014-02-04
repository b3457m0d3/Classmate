package com.classmateapp.mobile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPStateSingleton;

import android.app.Activity;
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

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class UpcomingTasksFragment extends ListFragment {

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//	private static final String ARG_PARAM1 = "param1";
//	private static final String ARG_PARAM2 = "param2";
	
	/** broadcast receiver */
    private BroadcastReceiver mReceiver;
	
	private OnItemClickListener mClickListener;
	
	//private static SimpleDateFormat sdf_short = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
	private static SimpleDateFormat sdf_long = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.US);

	// TODO: Rename and change types of parameters
//	private String mParam1;
//	private String mParam2;

	@SuppressWarnings("unused")
	private UpcomingTasksFragmentListener mActivityListener;

	// TODO: Rename and change types of parameters
	public static UpcomingTasksFragment newInstance(/*String param1, String param2*/) {
		UpcomingTasksFragment fragment = new UpcomingTasksFragment();
//		Bundle args = new Bundle();
//		args.putString(ARG_PARAM1, param1);
//		args.putString(ARG_PARAM2, param2);
//		fragment.setArguments(args);
		return fragment;
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public UpcomingTasksFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		if (getArguments() != null) {
//			mParam1 = getArguments().getString(ARG_PARAM1);
//			mParam2 = getArguments().getString(ARG_PARAM2);
//		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getActivity().invalidateOptionsMenu();
		return inflater.inflate(R.layout.fragment_upcoming_tasks, container, false);
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
                	showUpcomingTasks();
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
        showUpcomingTasks();
	}
	
	public void showUpcomingTasks() {
		ArrayList<Task> myUpComingTasks = MyDDPState.getInstance().getDateSortedTasks(true);
		
		String[] from = new String[] {"title", "dueDate"};
        int[] to = new int[] { R.id.task_item_title, R.id.task_item_date };
 
        // prepare the list of all records
        ArrayList<Map<String, String>> fillMaps = new ArrayList<Map<String, String>>();
        
        for (Task task : myUpComingTasks) {
        	Map<String, String> map = new ConcurrentHashMap<String, String>();
        	map.put("title", task.getTitle());
        	map.put("dueDate", sdf_long.format(new Date(task.getDueDate())));
        	fillMaps.add(map);
        }
    	
    	SimpleAdapter adapter = new SimpleAdapter(getActivity(), fillMaps, R.layout.task_template, from, to);
    	setListAdapter(adapter);
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
	
	public void setOnItemClickListener(OnItemClickListener listener) {
		mClickListener = listener;
    }
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(mClickListener != null) {
        	mClickListener.onItemClick(l, v, position, id);
        }
    }
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mActivityListener = (UpcomingTasksFragmentListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mActivityListener = null;
		mClickListener = null;
	}

//	@Override
//	public void onListItemClick(ListView l, View v, int position, long id) {
//		super.onListItemClick(l, v, position, id);
//
//		if (null != mListener) {
//			// Notify the active callbacks interface (the activity, if the
//			// fragment is attached to one) that an item has been selected.
//			
//			String courseId = MyDDPState.getInstance().getUserCourses().get(position).getId();
//			mListener
//					.onUpcomingTasksFragmentInteraction(courseId);
//		}
//	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface UpcomingTasksFragmentListener {
		// TODO: Update argument type and name
		public void onUpcomingTasksFragmentInteraction(String courseId);
	}

}
