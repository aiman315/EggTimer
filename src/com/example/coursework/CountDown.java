package com.example.coursework;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class CountDown extends Thread implements Runnable, Parcelable
{
	private final String TAG = "CountDown";
	private final int SLEEP_DURATION = 1000; //one second
	
	private int id;
	private String name;
	private long duration;
	private long timeRemaining;

	public boolean isRunning;;

	/**
	 * Default constructor
	 * @param id timer identifier
	 * @param name timer name
	 * @param duration timer duration in seconds
	 */
	public CountDown(int id, String name, long duration)
	{
		this.id = id;
		this.name = name;
		this.duration = duration;
		this.timeRemaining = duration;
		this.isRunning = false;
	}

	/**
	 * Parcelable constructor, to enable recreation of abject when passed
	 * @param in
	 */
	public CountDown(Parcel in) {
		readFromParcel(in);
	}

	/**
	 * Gets the id for timer
	 * @return id timer identifier
	 */
	public int getTimerId() {
		Log.d(TAG, "getTimerId");
		return id;
	}

	/**
	 * Gets the name for timer
	 * @return text name for timer
	 */
	public String getTimerName() {
		Log.d(TAG, "getTimerName");
		return name;
	}

	/**
	 * Gets the initial duration (in seconds) for timer upon creation
	 * @return timer duration in seconds
	 */
	public long getTimerDuration() {
		Log.d(TAG, "getTimerDuration");
		return duration;
	}

	/**
	 * Gets the remaining duration (in seconds) for timer since it was started.
	 * getTimerRemainingTime() == getTimerDuration() if timer has not been started
	 * @return remaining time for timer in seconds
	 */
	public long getTimerRemainingTime() {
		Log.d(TAG, "getTimerRemainingTime");
		return timeRemaining;
	}

	/**
	 * Runs the timer so it starts counting down. A timer cannot start again while it is running
	 * @return true: if timer ran successfully, false: otherwise
	 */
	public boolean startTimer() {
		Log.d(TAG, "startTimer"+"\tDuration\t"+duration+"\ttimeRemaining\t"+timeRemaining);
		isRunning = true;
		try {
			start();
		} catch (Exception e) {
			Log.d(TAG, "problem starting counter");
		}
		return isAlive();
	}

	/**
	 * Pauses the timer if it is running already, if already paused resumes it
	 */
	public void pauseTimer() {
		Log.d(TAG, "pauseTimer");
		isRunning = false;
	}


	/**
	 * Stops the count down for timer, and resets it to initial duration
	 */
	public void stopTimer() {
		Log.d(TAG, "stopTimer");
		timeRemaining = duration;
		isRunning = false;
	}

	@Override
	public void run() {
		Log.d(TAG, "run");
		while (true) {
			while(isRunning) {
				try {
					if (timeRemaining > 0) {
						Thread.sleep(SLEEP_DURATION);
						Log.d(TAG, name+"\t"+"counting down"+"\t"+timeRemaining);
						timeRemaining -= 1;
					} else {
						Thread.sleep(SLEEP_DURATION);
						isRunning = false;
						timeRemaining = duration;
						Log.d(TAG, "counting stopped");
						return;
					}
				} catch(InterruptedException e) {
					return;
				}
			}
			return;
		}
	}

	//------------------ Parcelabel Methods -----------------//

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeLong(duration);
		dest.writeLong(duration);
	}

	public void readFromParcel(Parcel in) {
		this.name = in.readString();
		this.duration = in.readLong();
		this.timeRemaining = in.readLong();
	}

	public static final Parcelable.Creator <CountDown> CREATOR = new Parcelable.Creator <CountDown>() {
		public CountDown createFromParcel(Parcel in) {
			return new CountDown(in); 
		}

		public CountDown[] newArray(int size) {
			return new CountDown[size];
		}
	};
}
