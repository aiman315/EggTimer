package com.example.coursework;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class TimerService extends Service {

	private final String TAG = "TimerService";

	private final IBinder binder = new MyBinder();
	private List<CountDown> timers = new ArrayList<CountDown>();
	
	/**
	 * Adds a timer to the list of timers controlled by this service
	 * @param timer
	 */
	public void addTimer(CountDown timer) {
		Log.d(TAG, "addTimer");
		timers.add(timer);
	}

	/**
	 * Gets the remaining time (<strong>in seconds</strong>) for a timer to finish, specified by id <strong>id</strong>
	 * @param id the id of selected timer
	 * @return timer remaining time in seconds
	 */
	public long getRemainingTime(int id) {
		Log.d(TAG, "getRemainingTime");
		int index = findTimer(id);
		return timers.get(index).getTimerRemainingTime();
	}
	
	public List<CountDown> getTimersList() {
		return timers;
	}

	/**
	 * Starts counting down for a selected timer, specified by id <strong>id</strong>
	 * @param id the id of selected timer
	 */
	public void startTimer(int id) {
		Log.d(TAG, "startTimer");
		int index = findTimer(id);
		if(!timers.get(index).startTimer()) {
			Toast.makeText(this, "Timer Already Started", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * Pauses the count down for a selected timer or
	 * resumes counting down from where it is paused last for a selected timer, specified by id <strong>id</strong>
	 * @param id the id of selected timer
	 */
	public void pauseTimer(int id) {
		Log.d(TAG, "pauseTimer");
		int index = findTimer(id);
		timers.get(index).pauseTimer();
	}


	/**
	 * Terminates the count down for a selected timer, specified by id <strong>id</strong>, and resets the timer duration
	 * @param id the id of selected timer
	 */
	public void stopTimer(int id) {
		Log.d(TAG, "stopTimer");
		int index = findTimer(id);
		timers.get(index).stopTimer();
	}

	/**
	 * Searches existing timers list for a timer with the identifier <strong>id</strong>
	 * Invoked by <strong>startTimer(int)</strong>, <strong>stopTimer(int)</strong>, <strong>pauseTimer(int)</strong> and <strong>getRemainingTime(int)</strong> to allow for timer selection
	 * @param id
	 * @return The timers list index for timer with identifier id
	 */
	private int findTimer(int id) {
		Log.d(TAG, "findTimer");
		for (CountDown timer : timers) {
			if (timer.getTimerId() == id) {
				return timers.indexOf(timer);		//index of timer with identifier 'id'
			}
		}
		//TODO: handle : could cause exception
		return -1;									//non-existing timer
	}
	
	
	//--------------- Service Life Cycle Methods --------------------------

		@Override
		public void onCreate() {
			super.onCreate();
			Log.d(TAG, "onCreate");
		}

		@Override
		public IBinder onBind(Intent intent) {
			Log.d(TAG, "onBind");
			return binder;
		}

		@Override
		public int onStartCommand(Intent intent, int flags, int startId) {
			Log.d(TAG, "onStartCommand");
			return Service.START_STICKY;
		}

		@Override
		public void onDestroy() {
			Log.d(TAG, "onDestroy");
			timers = null;
			super.onDestroy();
		}
		
	
	/**
	 * Inner class to allow interface of this service with activities bound to it
	 * @author Aiman
	 *
	 */
	public class MyBinder extends Binder
	{
		void addTimer(CountDown timer) {
			TimerService.this.addTimer(timer);
		}
		
		void startTimer(int id) {
			TimerService.this.startTimer(id);
		}

		void pauseTimer(int id) {
			TimerService.this.pauseTimer(id);
		}

		void stopTimer(int id) {
			TimerService.this.stopTimer(id);
		}

		long getRemainingTime(int id) {
			return TimerService.this.getRemainingTime(id);
		}
		
		List<CountDown> getTimersList() {
			return TimerService.this.getTimersList();
		}

		TimerService getService() {
			return TimerService.this;
		}
	}
}