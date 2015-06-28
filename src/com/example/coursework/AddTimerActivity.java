package com.example.coursework;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddTimerActivity extends Activity {
	private final String TAG = "AddTimerActivity";
	private final int EMPTY_TIMER_NAME = 0;
	private final int SECONDS_MAX = 60;
	private int id = 0;

	/**
	 * Creates a Timer object and return it to MainActivity
	 * @param v
	 */
	public void onClickConfirm(View v)
	{
		Log.d(TAG, "onClickConfirm");

		try {
			//obtain input from EditTexts
			String timerName = ((EditText) findViewById(R.id.timer_name)).getText().toString();
			String timerMinutesStr = ((EditText) findViewById(R.id.timer_duration_minutes)).getText().toString();
			String timerSecondsStr = ((EditText) findViewById(R.id.timer_duration_seconds)).getText().toString();

			//convert input to integers
			int timerMinutes = Integer.parseInt(timerMinutesStr); 
			int timerSeconds = Integer.parseInt(timerSecondsStr);

			//validate input
			if (timerName.length() == EMPTY_TIMER_NAME) {
				Toast.makeText(this, "WARNING: Empty Fields", Toast.LENGTH_SHORT).show();
			} else if (timerSeconds >= SECONDS_MAX) {
				Toast.makeText(this, "WARNING: Invalid Seconds Value", Toast.LENGTH_SHORT).show(); 
			} else if (timerMinutesStr.length() < 2 || timerSecondsStr.length() < 2) {
				Toast.makeText(this, "WARNING: Use format MM:SS", Toast.LENGTH_SHORT).show();
			} else {
				//create CountDown object
				CountDown timer = new CountDown(id, timerName, (timerMinutes*60)+timerSeconds);
				id++;

				//return timer to MainActivity
				Intent result = new Intent();
				result.putExtra("timer", timer);
				setResult(Activity.RESULT_OK, result);
				finish();
			}
		} catch (NumberFormatException e) {
			Toast.makeText(this, "WARNING: Empty Fields", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Cancels the creation of a Timer
	 * @param v
	 */
	public void onClickCancel(View v)
	{
		Log.d(TAG, "onClickCancel");
		setResult(RESULT_CANCELED);
		finish();
	}

	//---------------------------- Activity Life Cycle Methods -----------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_timer);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart");
		Toast.makeText(this, "For Duration, Use format MM:SS", Toast.LENGTH_LONG).show();
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
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}
}
