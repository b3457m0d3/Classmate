package com.classmateapp.mobile;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListFragment;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPStateSingleton;

/**
 * A fragment representing a list of Items.
 * <p />
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class AddCourseFragment extends ListFragment implements
				SearchView.OnQueryTextListener {

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//	private static final String ARG_PARAM1 = "param1";
//	private static final String ARG_PARAM2 = "param2";
	
	/** broadcast receiver */
    private BroadcastReceiver mReceiver;
	
	private OnItemClickListener mClickListener;
	
	private ListView mListView;

	// TODO: Rename and change types of parameters
//	private String mParam1;
//	private String mParam2;

//	private static MainActivity.MyCoursesPageFragmentListener mListener;
	@SuppressWarnings("unused")
	private AddCourseFragmentListener mActivityListener;

	// TODO: Rename and change types of parameters
	public static AddCourseFragment newInstance() {
		AddCourseFragment fragment = new AddCourseFragment();
//		mListener = listener;
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
	public AddCourseFragment() {
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
		View view =inflater.inflate(R.layout.fragment_add_course, container, false);
		
		mListView = (ListView) view.findViewById(android.R.id.list);
		mListView.setTextFilterEnabled(true);
		
		SearchView searchView = (SearchView) view.findViewById(R.id.search_view);
		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(this);
		searchView.setSubmitButtonEnabled(true);
		searchView.setQueryHint("Search courses");
		searchView.requestFocus();
		
		getActivity().invalidateOptionsMenu();
		
		return view;
	}
	
	public boolean onQueryTextChange(String newText) {
		
		if (TextUtils.isEmpty(newText)) {
			mListView.clearTextFilter();
		}
		else {
			mListView.setFilterText(newText.toString());
		}
		return true;
	}
	
	public boolean onQueryTextSubmit(String query) {
		// hide the keyboard
		InputMethodManager inputMethodManager = (InputMethodManager)  getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
		MyDDPState.getInstance().addCourse(query);
		((WrapperMyCoursesFragment) getParentFragment()).onCourseAdded();
		return true;
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
                //ddp.subscribe("courses", new Object[] {});
    			//ddp.subscribe("currentUser", new Object[] {});
            }
            @Override
            protected void onSubscriptionUpdate(String changeType,
                    String subscriptionName, String docId) {
                if (subscriptionName.equals("courses")) {
                    // show any new parties
                	ArrayList<Course> myCourses = MyDDPState.getInstance().getAlphabeticalCourseList();
                	setListAdapter(new ArrayAdapter<Course>(getActivity(),
            				android.R.layout.simple_list_item_1, android.R.id.text1,
            				myCourses));
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
        
        ArrayList<Course> myCourses = MyDDPState.getInstance().getAlphabeticalCourseList();
        
    	setListAdapter(new ArrayAdapter<Course>(getActivity(),
				android.R.layout.simple_list_item_1, android.R.id.text1,
				myCourses));
    	
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
//		try {
//			mActivityListener = (AddCourseFragmentListener) activity;
//		} catch (ClassCastException e) {
//			throw new ClassCastException(activity.toString()
//					+ " must implement OnFragmentInteractionListener");
//		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mActivityListener = null;
		mClickListener = null;
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
	public interface AddCourseFragmentListener {
		// TODO: Update argument type and name
		public void onMyCoursesFragmentInteraction(String courseId);
	}

}
