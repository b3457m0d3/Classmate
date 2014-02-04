package com.classmateapp.mobile;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Use the
 * {@link WrapperUpcomingTasksFragment#newInstance} factory method to create an
 * instance of this fragment.
 * 
 */
public class WrapperUpcomingTasksFragment extends Fragment {
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
	
	private UpcomingTasksFragment mChildFragment;
	
	/** broadcast receiver */
    private BroadcastReceiver mReceiver;
	
	// TODO: Rename and change types and number of parameters
	public static WrapperUpcomingTasksFragment newInstance() {
		WrapperUpcomingTasksFragment fragment = new WrapperUpcomingTasksFragment();
//		Bundle args = new Bundle();
//		args.putString(ARG_PARAM1, param1);
//		args.putString(ARG_PARAM2, param2);
//		fragment.setArguments(args);
		return fragment;
	}

	public WrapperUpcomingTasksFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
//			mParam1 = getArguments().getString(ARG_PARAM1);
//			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		mChildFragment = UpcomingTasksFragment.newInstance();
		mChildFragment.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> l, View v, int position, long id) {
				String taskId = MyDDPState.getInstance().getDateSortedTasks(true).get(position).getId();
				
				Fragment fragment = TaskDetailFragment.newInstance(taskId);
				getChildFragmentManager()
					.beginTransaction()
					.replace(R.id.upcoming_tasks_container, fragment)
					.addToBackStack(null)
					.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
					.commit();
				getActivity().invalidateOptionsMenu();
			}
		});
		getChildFragmentManager()
			.beginTransaction()
			.add(R.id.upcoming_tasks_container, mChildFragment)
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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_wrapper_upcoming_tasks,
				container, false);
	}
	
	@Override
	public void onResume() {
		super.onResume();

//        // get ready to handle DDP events
//        mReceiver = new DDPBroadcastReceiver(MyDDPState.getInstance(), getActivity()) {
//            @Override
//            protected void onDDPConnect(DDPStateSingleton ddp) {
//                super.onDDPConnect(ddp);
//                // add our subscriptions needed for the activity here
//                ddp.subscribe("courses", new Object[] {});
//    			ddp.subscribe("currentUser", new Object[] {});
//            }
//            @Override
//            protected void onSubscriptionUpdate(String changeType,
//                    String subscriptionName, String docId) {
//                if (subscriptionName.equals("tasks")) {
//                    // show any new parties
//                	showUpcomingTasks();
//                }
//            }
//            @Override
//            protected void onLogin() {
//                // update login/logout action button
//                //getActivity().invalidateOptionsMenu();
//            }
//            @Override
//            protected void onLogout() {
//                // update login/logout action button
//                //getActivity().invalidateOptionsMenu();
//            }
//        };
//        MyDDPState.getInstance().connectIfNeeded();    // start connection process if we're not connected
//        showUpcomingTasks();
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
	
	public void onEditTask(String taskId) {
		EditTaskFragment fragment = EditTaskFragment.newInstance(taskId);
		getChildFragmentManager()
			.beginTransaction()
			.replace(R.id.upcoming_tasks_container, fragment)
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
	
//	public void showUpcomingTasks() {
//		ArrayList<Task> myUpComingTasks = MyDDPState.getInstance().getDateSortedTasks(true);
//		
//		String[] from = new String[] {"title", "dueDate"};
//        int[] to = new int[] { R.id.task_item_title, R.id.task_item_date };
// 
//        // prepare the list of all records
//        ArrayList<Map<String, String>> fillMaps = new ArrayList<Map<String, String>>();
//        
//        for (Task task : myUpComingTasks) {
//        	Map<String, String> map = new ConcurrentHashMap<String, String>();
//        	map.put("title", task.getTitle());
//        	map.put("dueDate", sdf.format(new Date(task.getDueDate())));
//        	fillMaps.add(map);
//        }
//    	
//    	SimpleAdapter adapter = new SimpleAdapter(getActivity(), fillMaps, R.layout.task_template, from, to);
//    	mChildFragment.setListAdapter(adapter);
//	}
	
	public Fragment getCurrentFragment() {
		return getChildFragmentManager().findFragmentById(R.id.upcoming_tasks_container);
	}

}
