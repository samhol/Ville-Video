package sph;

/**
 * Implements a simple time handling class.
 * 
 * @author  <a href="mailto:sami.holck@gmail.com">Sami Holck</a>
 * @version 1.0
 * @since   12.09.2013
 * 
 */
public class Time {
	/**
	 * the hours part of the time.
	 */
	private int h;
	/**
	 * the minutes part of the time.
	 */
	private int m;
	/**
	 * the seconds part of the time.
	 */
	private int s;
	private double parts;
	private double time;

	/**
	 * 
	 */
	public Time() {
		this.setTime(0);
	}

	/**
	 * 
	 * @.pre  time >= 0
	 * @param time the time in seconds.
	 */
	public Time(double time) {
		this.setTime(time);
	}

	/**
	 * 
	 * 
	 * @.pre  hours >= 0 &  minutes >= 0 & seconds >= 0
	 * @param hours the hours part of the time.
	 * @param minutes the minutes part of the time.
	 * @param seconds the seconds part of the time.
	 */
	public Time(int hours, int minutes, int seconds) {
		this.h = hours;
		this.m = minutes;
		this.s = seconds;
		this.calculateTime();
	}

	protected void calculateTime() {
		this.time = this.h * 3600 + this.m * 60 + this.s + this.parts;
	}

	/**
	 * Returns an integer describing the hours.
	 * @.post  RESULT >= 0
	 * @return hours as an integer.
	 */
	public int getHours() {
		return this.h;
	}

	/**
	 * Sets the hours to the time.
	 * 
	 * @.pre  hours >= 0
	 * @param hours the hours of the time
	 */
	public void setHours(int hours) {
		this.h = hours;
		this.calculateTime();
	}

	/**
	 * Returns an integer describing the minutes.
	 * 
	 * @.post  RESULT >= 0
	 * @return minutes as an integer.
	 */
	public int getMinutes() {
		return this.m;
	}

	/**
	 * Sets the minutes to the time.
	 * 
	 * @.pre  minutes >= 0
	 * @param minutes the minutes of the time
	 */
	public void setMinutes(int minutes) {
		this.m = minutes;
		this.calculateTime();
	}

	/**
	 * Returns an integer describing the seconds.
	 * 
	 * @.post  RESULT >= 0
	 * @return seconds as an integer.
	 */
	public int getSeconds() {
		return this.s;
	}

	/**
	 * Sets the seconds to the time.
	 * 
	 * @.pre  seconds >= 0
	 * @param seconds the seconds of the time
	 */
	public void setSeconds(int seconds) {
		this.s = seconds;
		this.calculateTime();
	}

	/**
	 * Returns an double describing the time in seconds.
	 * 
	 * @.post  RESULT >= 0
	 * @return the time in seconds.
	 */
	public double getTime() {
		return this.time;
	}

	/**
	 * Sets the time in seconds.
	 * 
	 * @.pre  time >= 0
	 * @param time the time seconds.
	 */
	public void setTime(double time) {
		this.time = time;
		this.h = (int) time / 3600;
		this.parts = time % 1;
		int remainder = (int) time % 3600;
		this.m = remainder / 60;
		this.s = remainder % 60;
	}

	/**
	 * Returns a string describing the hours.
	 * 
	 * @return hours as a string.
	 */
	public String hoursAsString() {
		return (this.h < 10 ? 0 + Integer.toString(this.h) : Integer.toString(this.h));
	}

	/**
	 * Returns a string describing the minutes.
	 * 
	 * @return minutes as a string.
	 */
	public String minutesAsString() {
		return (this.m < 10 ? 0 + Integer.toString(this.m) : Integer.toString(this.m));
	}

	/**
	 * Returns a string describing the seconds.
	 * 
	 * @return seconds as a string.
	 */
	public String secondsAsString() {
		return (this.s < 10 ? 0 + Integer.toString(this.s) : Integer.toString(this.s));
	}

	/**
	 * Returns a string describing this time in hh:mm:ss format.
	 * 
	 * @return this time in hh:mm:ss format
	 */
	@Override
	public String toString() {
		String min = (this.m < 10 ? 0 + Integer.toString(this.m) : Integer.toString(this.m));
		String sec = (this.s < 10 ? 0 + Integer.toString(this.s) : Integer.toString(this.s));
		return this.h + ":" + min + ":" + sec;
	}

	/**
	 * Converts the time in seconds to a time string in hh:mm:ss format.
	 * 
	 * @.pre   seconds >= 0
	 * @param  seconds the time in seconds to convert
	 * @return corresponding time in hh:mm:ss format
	 */
	public static String convertToTimeString(double seconds) {
		Time t = new Time(seconds);
		return t.toString();
	}

	/**
	 * Converts the time in seconds to a time string in hh:mm:ss format.
	 * 
	 * @.pre   hours >= 0 &  minutes >= 0 & seconds >= 0
	 * @param  hours the hours part of the time.
	 * @param  minutes the minutes part of the time.
	 * @param  seconds the seconds part of the time.
	 * @return corresponding time in hh:mm:ss format
	 */
	public static String convertToTimeString(int hours, int minutes, int seconds) {
		Time t = new Time(hours, minutes, seconds);
		return t.toString();
	}
}
