package entities;

/*
 * Entity Class supporting Algorithmic Music Composition Software
 * @author Ryan Strelow
 * @version 1.0
 * @since 05/22/18
 */

import java.util.HashMap;
import java.util.ArrayList;

public class Epoch {
	
	/*
	 * the maximum range that pitches can achieve
	 */
	private int range;
	
	/*
	 * the default duration for a whole note;
	 */
	private int defaultDuration;
	
	/*
	 * the name of the era
	 */
	private String era;	
	
	/*
	 * the different durations that a measure can be 
	 */
	private ArrayList<MeasureDurations> durationPatterns;
	
	/*
	 * the different durations that a measure can be 
	 */
	private ArrayList<Double> singleIntervals;
	

	/*
	 * the different intervals that a measure can be
	 */
	private HashMap<Integer, ArrayList<MeasureIntervals>> intervalPatterns;
	
	/*
	 * default constructor
	 */
	public Epoch(ArrayList<MeasureDurations> durationPatterns, HashMap<Integer, ArrayList<MeasureIntervals>> intervalPatterns, int range, int defaultDuration, String era) {
		
		this.setRange(range);
		this.setDefaultDuration(defaultDuration);		
		this.setEra(era);
		this.setDurationPatterns(durationPatterns);
		this.setIntervalPatterns(intervalPatterns);	
	}
	
	/*
	 * constructor if you are using intervals generated one at a time
	 */
	public Epoch(ArrayList<MeasureDurations> durationPatterns, ArrayList<Double> singleIntervals, int range, int defaultDuration, String era) {
		
		this.setRange(range);
		this.setDefaultDuration(defaultDuration);		
		this.setEra(era);
		this.setDurationPatterns(durationPatterns);
		this.setSingleIntervals(singleIntervals);			
	}


	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}


	public String getEra() {
		return era;
	}

	
	public void setEra(String era) {
		this.era = era;
	}

	
	public int getDefaultDuration() {
		return defaultDuration;
	}

	
	public void setDefaultDuration(int defaultDuration) {
		this.defaultDuration = defaultDuration;
	}


	public ArrayList<MeasureDurations> getDurationPatterns() {
		return durationPatterns;
	}

	
	public void setDurationPatterns(ArrayList<MeasureDurations> durationPatterns) {
		this.durationPatterns = durationPatterns;
	}


	public HashMap<Integer, ArrayList<MeasureIntervals>> getIntervalPatterns() {
		return intervalPatterns;
	}


	public void setIntervalPatterns(HashMap<Integer, ArrayList<MeasureIntervals>> intervalPatterns) {
		this.intervalPatterns = intervalPatterns;
	}

	public ArrayList<Double> getSingleIntervals() {
		return singleIntervals;
	}

	public void setSingleIntervals(ArrayList<Double> singleIntervals) {
		this.singleIntervals = singleIntervals;
	}
	
	
	

}
