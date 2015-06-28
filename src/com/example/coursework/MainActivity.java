package com.example.coursework;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	private final String TAG = "MainActivity";
	private static final int ACTIVITY_ADD_TIMER_REQUEST_CODE = 1;

	/**
	 * Creates the intent to make a new CountDown object
	 * and asks for results (confirm | cancel)
	 */
	private void addTimer() {
		Log.d(TAG, "addTimer");
		Intent timerIntent = new Intent(MainActivity.this, AddTimerActivity.class);
		startActivityForResult(timerIntent, ACTIVITY_ADD_TIMER_REQUEST_CODE);
	}

	/**
	 * Depending on intent result (confirm | cancel)
	 * Confirm: The data for Timer is read, and CountDown object is added to the list of available timers
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult");
		if (requestCode == ACTIVITY_ADD_TIMER_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Log.d(TAG, "==== timer confirmed");
				Bundle bundle = data.getExtras();
				CountDown timer = (CountDown) bundle.getParcelable("timer");
				if (timer == null) {
					Log.d(TAG, "==== issue creating timer");
				} else {
					listTimer(timer);
					Log.d(TAG, "==== timer added");
				}
			} else if (resultCode == RESULT_CANCELED) {
				Log.d(TAG, "=== timer canceled");
			} else {
				Log.d(TAG, "==== unrecognized result code");
			}
		} else {
			Log.d(TAG, "==== unrecognized request code");
		}
	}

	/**
	 * Lists the timer to the list of existing timers
	 * @param timer
	 */
	private void listTimer(final CountDown timer) {
		//get timer information
		final String name = timer.getTimerName();
		long duration = timer.getTimerDuration();
		final int minutes = (int) (duration/60);
		final int seconds = (int) (duration%60);
		
		//create views required
		LinearLayout singleTimerLayout = new LinearLayout(this);
		TextView timerName = new TextView(this);
		TextView timerDuration = new TextView(this);
		Button buttonSelect = new Button(this);
		
		//adding timer information to TextViews
		timerName.setText(name);
		StringBuilder timerText = new StringBuilder("");

		//handle minutes
		if(minutes < 10) {
			timerText.append("0");
		}
		timerText.append(minutes+":");

		//handle seconds
		if(seconds < 10) {
			timerText.append("0");
		}

		timerText.append(seconds);
		timerDuration.setText(timerText);
		
		//adding text to button
		buttonSelect.setText("Select");
		
		//set views layout
		LayoutParams defualtLayout = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	
		
		//adding TextViews and button to LinearLayout
		singleTimerLayout.addView(timerName, defualtLayout);
		singleTimerLayout.addView(timerDuration, defualtLayout);
		singleTimerLayout.addView(buttonSelect, defualtLayout);
		
		for (int i = 0; i < singleTimerLayout.getChildCount() ; i++) {
			singleTimerLayout.getChildAt(i).setPadding(5, 0, 5, 0);
		}
		
		//adding LinearLayout to UI
		((ViewGroup)findViewById(R.id.available_timers_layout)).addView(singleTimerLayout);
		
		//adding click listener to select button
		buttonSelect.setOnClickListener(new View.OnClickListener() {      
		    @Override
		    public void onClick(View v) {
		    	Log.d(TAG, "timer "+name+" is clicked");
		        Intent intent = new Intent(MainActivity.this, SelectedTimerActivity.class);
		        intent.putExtra("timer", timer);
		        startActivity(intent);     
		    }
		   });
		
	}
	
	
	
	//--------------------------- Activity Life Cycle Methods -------------------------------
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add_timer:
			addTimer();
			break;
		default:
			Log.d(TAG, "problem in selection");
			break;
		}
		return true;
	}
}

