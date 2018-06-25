package entities;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Queue;
import java.util.HashMap;

public class MeasureDurations {
	
	/*
	 * the number of notes for this measure
	 */
	private int length;	
	
	/*
	 * the current position in the measure
	 */
	private int currentPosition;
	
	/*
	 * the notes making of the measure
	 */
	private ArrayList<Note> notes;
	
	/*
	 * the probability of this measure occurring 
	 */
	private double probability;
	
	/*
	 * default length for a whole note
	 */
	private final int DEFAULT_WHOLENOTE_DURATION = 4000;
	
	/*
	 * Constructor for a MeasureDurations
	 */
	public MeasureDurations(String type, double probability) {
		this.setProbability(probability);
		notes =  new ArrayList<Note>();
		currentPosition = 0;
		
		int nextPosition = type.indexOf('-');
		if (nextPosition == -1) {
			convert(type);
		}
		else {
			while (nextPosition != -1) {
				convert(type.substring(0, nextPosition));

				type = type.substring(++nextPosition);			
				nextPosition = type.indexOf('-');	
			}
			convert(type);
		}
	}
	
	
	/*
	 * helper method to convert the YAML input into usable data
	 * @param String token  the value to convert
	 */
	private void convert(String token) {
		
		String convertedToken = "";
		int duration = DEFAULT_WHOLENOTE_DURATION;
		double multiplier = 1.0;
		boolean isRest = false;
		
		//determines if the note is a rest
		if (token.endsWith("r")) {
			isRest = true;
			token = token.substring(0, token.length()-1);
		}
		
		//determines if the noted is dotted
		if (token.endsWith(".")) 
		{
			multiplier = 1.5;
			token = token.substring(0, token.length()-1);	
			convertedToken = "Dotted - ";
		}
		
		int num = Integer.parseInt(token);
		multiplier /= num;
		
		if (num == 16) 
			convertedToken += "Sixteenth";
		else if (num == 8) 
			convertedToken += "Eighth";

		else if (num == 4) 
			convertedToken += "Quarter";
		else if (num == 2)			
			convertedToken += "Half";
		else if (num == 1) 
			convertedToken += "Whole";
		else 
			convertedToken += "1/" + num;
			
		
		//calculate if it is a rest
		if(isRest) {
			convertedToken += " Rest";
		}
		else {
			convertedToken += " Note";
			length++;
		}
		
		duration *= multiplier;
		
		Note nextNote = new Note(duration, convertedToken, isRest);
		notes.add(nextNote);
	}
	
	
	/*
	 * checks to see if there is another duration for this measure
	 * @return boolean  
	 */
	public boolean hasNextDuration() {
		return (currentPosition < notes.size());	
	}
	
	
	/*
	 * resets the position in the measure to zero for future measure generation
	 *  
	 */
	public void resetDurations() {
		this.currentPosition = 0;
	}
	
	/*
	 * gets the next duration in the measure 
	 * @return boolean  
	 */
	public Note nextDuration() {		
		return notes.get(currentPosition++);
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
	

}
