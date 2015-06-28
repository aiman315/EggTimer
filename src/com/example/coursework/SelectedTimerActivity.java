package com.example.coursework;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class SelectedTimerActivity extends Activity {

	private final String TAG = "SelectedTimer";
	private final int SLEEP_DURATION = 1000; //one second
	private TextView timerDuration;
	private CountDown timer;

	// timer GUI attributes
	private String timerName;
	private long duration; // in seconds
	private boolean isUpdating;

	//notification initializations
	private final String NOTIFICATION_TIMER_MESSAGE = "Timer Is Up!";
	private int notificationId;
	private NotificationManager myNotifyMgr;

	//Binding to TimerService attributes
	private TimerService.MyBinder timerService = null;
	private ServiceConnection serviceConnection = new ServiceConnection()
	{
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Log.d(TAG, "onServiceConnected");
			timerService = (TimerService.MyBinder) service;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			Log.d(TAG, "onServiceDisconnected");
			timerService = null;
		}
	};


	/**
	 * Handles click on Start button, to start the timer
	 * @param v The button clicked on
	 */
	public void startTimer(View v) {
		Log.d(TAG, "startTimer");
		timerService.addTimer(timer);
		timerService.startTimer(timer.getTimerId());

		//can only start if not already working
		if (!isUpdating) {
			isUpdating = true;
			//updates timer on screen
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (isUpdating) {
						if (!timerDuration.getText().toString().equals("00:00")) {
							try {
								Thread.sleep(SLEEP_DURATION);
								update();
							} catch (InterruptedException e) {
								return;
							}
						} else {
							//notification upon end of timer
							displayNotification();
							isUpdating = false;
							return;
						}
					}
				}

				/**
				 * Updates the timer textView to display the remaining time
				 * Invoked by thread in startTimer(View)
				 */
				private void update() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							updateTimer(timer.getTimerRemainingTime());
						}
					});
				}

				/**
				 * Sets up and Pops up a notification with message <strong>NOTIFICATION_TIMER_MESSAGE</strong> for timer
				 * Invoked by thread in startTimer(View) to indicate end of timer
				 */
				@SuppressWarnings("deprecation")
				private void displayNotification() {
					notificationId = timer.getTimerId();
					Notification myNotification = new Notification(
							R.drawable.ic_launcher, timerName, System
									.currentTimeMillis());
					myNotification.setLatestEventInfo(getApplicationContext(),
							timerName, timerName+" "+NOTIFICATION_TIMER_MESSAGE, null);
					myNotification.defaults |= Notification.DEFAULT_ALL;
					myNotifyMgr.notify(notificationId, myNotification);
				}
			}).start();
		}
	}


	/**
	 * Handles click on Pause button, to pause the timer
	 * @param v The button clicked on
	 */
	public void pauseTimer(View v) {
		Log.d(TAG, "pauseTimer");
		isUpdating = false;
		timerService.pauseTimer(timer.getTimerId());
	}


	/**
	 * Handles click on Stop button, to stop the timer
	 * @param v The button clicked on
	 */
	public void stopTimer(View v) {
		Log.d(TAG, "stopTimer");
		isUpdating = false;
		timerService.stopTimer(timer.getTimerId());
		updateTimer(duration);				// resets the timer value
	}

	/**
	 * Updates the TextView responsible for displaying the count down
	 * Invoked by:
	 * <li> 
	 * 		<strong>onCreate(Bundle)</strong> : to display selected timer duration
	 * 		<strong>onStart(View)</strong> : to update GUI every second
	 * 		<strong>stopTimer(View)</strong> : to reset selected timer duration value after it has been started
	 * </li>
	 * @param duration Timer duration in seconds
	 */
	private void updateTimer(long duration) {
		Log.d(TAG, "updateTimer");

		int minutes = (int) (duration / 60);
		int seconds = (int) (duration % 60);

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
	}

	//------------------------ Activity Life Cycle Methods ------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");

		setContentView(R.layout.activity_selected_timer);
		timerDuration = (TextView)findViewById(R.id.time_remaining);

		//get clicked timer information from previous activity
		Bundle bundle = getIntent().getExtras();
		timer = (CountDown) bundle.getParcelable("timer");
		timerName = timer.getTimerName();
		duration = timer.getTimerRemainingTime();

		//GUI setup
		setTitle(timerName);
		updateTimer(duration);

		//notification setup
		myNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		//Binding to TimerService setup
		this.bindService(new Intent(this, TimerService.class), serviceConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.selected_timer, menu);
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
		unbindService(serviceConnection);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.edit_timer:
			Log.d(TAG, "edit timer");
			break;
		case R.id.delete_timer:
			Log.d(TAG, "delete timer");
			break;
		default:
			Log.d(TAG, "problem in selection");
			break;
		}
		return true;
	}

}
