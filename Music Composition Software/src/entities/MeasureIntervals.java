package entities;

import java.util.ArrayList;

public class MeasureIntervals {
	
	/*
	 * the number of intervals for this measure
	 */
	private int length;	
	
	/*
	 * the current position in the measure
	 */
	private int currentPosition;
	
	/*
	 * the intervals that make up this measure
	 */
	private ArrayList<Integer> intervals;
	
	/*
	 * the probability of this measure occurring
	 */
	private double probability;
		
	/*
	 * default constructor
	 */
	public MeasureIntervals(String type, double probability) {
		this.setProbability(probability);
		intervals =  new ArrayList<Integer>();
		currentPosition = 0;
		
		int nextPosition = type.indexOf('-');
		if (nextPosition == -1) {
			intervals.add(Integer.parseInt(type));
		}
		else {
			while (nextPosition != -1) {
				intervals.add(Integer.parseInt(type.substring(0, nextPosition)));		
				type = type.substring(++nextPosition);			
				nextPosition = type.indexOf('-');	
			}
			intervals.add(Integer.parseInt(type));
		}
		this.length = intervals.size();
	}
	
	/*
	 * constructor if you want to add the intervals by hand
	 */
	public MeasureIntervals() {
		intervals = new ArrayList<Integer>();
		currentPosition = 0;
	}
	
	/*
	 * checks to see if there are intervals left in the measure
	 * @return  true if there is a next, false otherwise
	 */
	public boolean hasNextInterval() {
		return (currentPosition < intervals.size());	
	}
	
	/*
	 * resets the position in the measure to zero for future iterations over the measure
	 */
	public void resetIntervals() {
		this.currentPosition = 0;
	}
	
	
	public int nextInterval() {		
		return intervals.get(currentPosition++);
	}


	public double getProbability() {
		return probability;
	}


	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	
	public int getSize() {
		return length;
	}


	public void setSize(int length) {
		this.length = length;
	}
	
	
	public void addInterval(int interval) {
		this.intervals.add(interval);
	}
	
	
	
	

}
