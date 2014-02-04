package com.classmateapp.mobile;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.classmateapp.mobile.MyCoursesFragment.MyCoursesFragmentListener;
import com.classmateapp.mobile.TasksOfCourseFragment.OnFragmentInteractionListener;
import com.classmateapp.mobile.UpcomingTasksFragment.UpcomingTasksFragmentListener;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener, MyCoursesFragmentListener, UpcomingTasksFragmentListener,
		OnFragmentInteractionListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	//private static SimpleDateFormat sdf_short = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
	private static SimpleDateFormat sdf_long = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.US);
	
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	int mCurrentPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		Fragment fragment = (Fragment) getFragmentManager().findFragmentByTag("android:switcher:"+R.id.pager+":"+mViewPager.getCurrentItem());
		if (fragment instanceof WrapperMyCoursesFragment) { 
			WrapperMyCoursesFragment wmmcFragment = (WrapperMyCoursesFragment) fragment;
			
			if (wmmcFragment.getCurrentFragment() instanceof MyCoursesFragment) {
				System.out.println("invalidated: MyCourses!");
				getMenuInflater().inflate(R.menu.my_courses, menu);
				getActionBar().setDisplayHomeAsUpEnabled(false);
			} 
			else if (wmmcFragment.getCurrentFragment() instanceof TasksOfCourseFragment) {
				System.out.println("invalidated: TasksOfCourse!");
				getMenuInflater().inflate(R.menu.tasks_of_course, menu);
				getActionBar().setDisplayHomeAsUpEnabled(true);
			} 
			else if (wmmcFragment.getCurrentFragment() instanceof AddTaskFragment) {
				System.out.println("invalidated: AddTask!");
				getMenuInflater().inflate(R.menu.add_task, menu);
				getActionBar().setDisplayHomeAsUpEnabled(true);
			}
			else if (wmmcFragment.getCurrentFragment() instanceof TaskDetailFragment) {
				System.out.println("invalidated: TaskDetail!");
				getMenuInflater().inflate(R.menu.task_detail_upcoming, menu);
				getActionBar().setDisplayHomeAsUpEnabled(true);
			}
			else if (wmmcFragment.getCurrentFragment() instanceof EditTaskFragment) {
				System.out.println("invalidated: EditTask!");
				getMenuInflater().inflate(R.menu.edit_task, menu);
				getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		else if (fragment instanceof WrapperUpcomingTasksFragment) {
			WrapperUpcomingTasksFragment wutFragment = (WrapperUpcomingTasksFragment) fragment;
			
			if (wutFragment.getCurrentFragment() instanceof UpcomingTasksFragment) {
				System.out.println("invalidated: UpcomingTasks!");
				getActionBar().setDisplayHomeAsUpEnabled(false);
			} 
			else if (wutFragment.getCurrentFragment() instanceof TaskDetailFragment) {
				System.out.println("invalidated: TaskDetail!");
				getMenuInflater().inflate(R.menu.task_detail_upcoming, menu);
				getActionBar().setDisplayHomeAsUpEnabled(true);
			}
			else if (wutFragment.getCurrentFragment() instanceof EditTaskFragment) {
				System.out.println("invalidated: EditTask!");
				getMenuInflater().inflate(R.menu.edit_task, menu);
				getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
		else if (fragment instanceof MyAccountFragment) {
			getMenuInflater().inflate(R.menu.my_account, menu);
		}
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        	case android.R.id.home:
        		onBackPressed();
        		return true;
            case R.id.action_add_course:
            	WrapperMyCoursesFragment fragmentCourse = (WrapperMyCoursesFragment) getFragmentManager().findFragmentByTag("android:switcher:"+R.id.pager+":"+mViewPager.getCurrentItem());
            	fragmentCourse.onAddNewCourse();
            	invalidateOptionsMenu();
            	return true;
            case R.id.action_add_task:
            	WrapperMyCoursesFragment fragmentTasks = (WrapperMyCoursesFragment) getFragmentManager().findFragmentByTag("android:switcher:"+R.id.pager+":"+mViewPager.getCurrentItem());
            	String courseId = ((TasksOfCourseFragment) fragmentTasks.getCurrentFragment()).getCourseId();
            	fragmentTasks.onAddNewTask(courseId);
            	invalidateOptionsMenu();
            	return true;
            case R.id.action_submit_task:
            	WrapperMyCoursesFragment fragmentAddTask = (WrapperMyCoursesFragment) getFragmentManager().findFragmentByTag("android:switcher:"+R.id.pager+":"+mViewPager.getCurrentItem());
            	fragmentAddTask.onSubmitTask();
            	invalidateOptionsMenu();
            	return true;
            case R.id.action_edit_task_upcoming:
            	if (mViewPager.getCurrentItem() == 1) {	
            		WrapperUpcomingTasksFragment fragmentEditTaskUpcoming = (WrapperUpcomingTasksFragment) getFragmentManager().findFragmentByTag("android:switcher:"+R.id.pager+":"+mViewPager.getCurrentItem());
            		String taskId = ((TaskDetailFragment) fragmentEditTaskUpcoming.getCurrentFragment()).getTaskId();
            		fragmentEditTaskUpcoming.onEditTask(taskId);
            	} else if (mViewPager.getCurrentItem() == 0) {
            		WrapperMyCoursesFragment fragmentEditTaskMyCourses = (WrapperMyCoursesFragment) getFragmentManager().findFragmentByTag("android:switcher:"+R.id.pager+":"+mViewPager.getCurrentItem());
            		String taskId = ((TaskDetailFragment) fragmentEditTaskMyCourses.getCurrentFragment()).getTaskId();
            		fragmentEditTaskMyCourses.onEditTask(taskId);
            	}
            	invalidateOptionsMenu();
            	return true;
            case R.id.action_edit_task_submit:
            	if (mViewPager.getCurrentItem() == 1) {	
            		WrapperUpcomingTasksFragment fragmentEditTaskSubmit = (WrapperUpcomingTasksFragment) getFragmentManager().findFragmentByTag("android:switcher:"+R.id.pager+":"+mViewPager.getCurrentItem());
            		fragmentEditTaskSubmit.onSubmitEditedTask();
            	} else if (mViewPager.getCurrentItem() == 0) {
            		WrapperMyCoursesFragment fragmentEditTaskSubmit2 = (WrapperMyCoursesFragment) getFragmentManager().findFragmentByTag("android:switcher:"+R.id.pager+":"+mViewPager.getCurrentItem());
            		fragmentEditTaskSubmit2.onSubmitEditedTask();
            	}
            	invalidateOptionsMenu();
            	return true;
        	default:
        		return super.onOptionsItemSelected(item);
        }
	}
	
	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
		mCurrentPosition = tab.getPosition();
		invalidateOptionsMenu();
	}

	@Override
	public void onBackPressed() {
		Fragment fragment = (Fragment) getFragmentManager().findFragmentByTag("android:switcher:"+R.id.pager+":"+mViewPager.getCurrentItem());
		if (fragment instanceof WrapperMyCoursesFragment) {
			WrapperMyCoursesFragment wrapperMyCoursesFragment = (WrapperMyCoursesFragment) fragment;
			wrapperMyCoursesFragment.onBackPressed();
		}
		if (fragment instanceof WrapperUpcomingTasksFragment) {
			WrapperUpcomingTasksFragment wutFragment = (WrapperUpcomingTasksFragment) fragment;
			wutFragment.onBackPressed();
		}
		invalidateOptionsMenu();
	}
	
	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		if (mCurrentPosition == tab.getPosition()) {
			Fragment fragment = (Fragment) getFragmentManager().findFragmentByTag("android:switcher:"+R.id.pager+":"+mViewPager.getCurrentItem());
			if (fragment instanceof WrapperMyCoursesFragment) {
				WrapperMyCoursesFragment wrapperMyCoursesFragment = (WrapperMyCoursesFragment) fragment;
				wrapperMyCoursesFragment.onTabReselected();
			}	
			if (fragment instanceof WrapperUpcomingTasksFragment) {
				WrapperUpcomingTasksFragment wrapperUpcomingTasksFragment = (WrapperUpcomingTasksFragment) fragment;
				wrapperUpcomingTasksFragment.onTabReselected();
			}
		}
		invalidateOptionsMenu();
	}
	
	public void onMyCoursesFragmentInteraction(String courseId) {
		
	}
	
	public void onUpcomingTasksFragmentInteraction(String courseId) {

	}
	
	public void onFragmentInteraction(String id) {
		
	}

	public void showDatePickerDialog(View view) {
    	// add another constructor that sets the date and then calls super()
		TextView dateView = (TextView) findViewById(R.id.add_task_date);
		Date dueDate = new Date();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 7);
		try {
    		dueDate = sdf_long.parse(dateView.getText().toString());
    		c.setTime(dueDate);
    	} catch (Exception e) {
    		
    	}
		
        DialogFragment newFragment = DatePickerFragment.newInstance(c);
        newFragment.show(getFragmentManager(), "datePicker");
    }
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			
			if (position == 0) {
				return WrapperMyCoursesFragment.newInstance();
			}
			else if (position == 1) {
				return WrapperUpcomingTasksFragment.newInstance();
			}
			else {
				return MyAccountFragment.newInstance();
			}
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section_my_courses).toUpperCase(l);
			case 1:
				return getString(R.string.title_section_upcoming_tasks).toUpperCase(l);
			case 2:
				return getString(R.string.title_section_my_account).toUpperCase(l);
			}
			return null;
		}
		
	}

}
