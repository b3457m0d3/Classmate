package com.classmateapp.mobile;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

public class DatePickerFragment extends DialogFragment 
							implements DatePickerDialog.OnDateSetListener {

	private static final String DATE = "date";
	
	//private static SimpleDateFormat sdf_short = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
	private static SimpleDateFormat sdf_long = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.US);
	
	private Calendar defaultDay;
	
	public DatePickerFragment() {
		
	}
	
	public static DatePickerFragment newInstance(Calendar c) {
		DatePickerFragment fragment = new DatePickerFragment();
		Bundle args = new Bundle();
		args.putLong(DATE, c.getTimeInMillis());
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		if (getArguments() != null) {
			defaultDay = Calendar.getInstance();
			defaultDay.setTimeInMillis(getArguments().getLong(DATE));
		}
		int year = defaultDay.get(Calendar.YEAR);
		int month = defaultDay.get(Calendar.MONTH);
		int day = defaultDay.get(Calendar.DAY_OF_MONTH);
		
		System.out.println("At end of onCreateDialog");
		
		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		// Do something with the date chosen by the user
		TextView dateTextView = (TextView) getActivity().findViewById(R.id.add_task_date);
		
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		Date date = cal.getTime();
		
		String dateString = sdf_long.format(date);
		
		dateTextView.setText(dateString);
	}

}