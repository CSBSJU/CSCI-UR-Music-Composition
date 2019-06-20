package controllers;

import java.util.ArrayList;


import entities.Note;

public class NoteConstructor {

	private static final int range = 26;
	private int myOctave = 5, currentDiff = 0, startingPitch = 1;
	
	
	public ArrayList<Note> constructNotes(ArrayList<String> songDurations, ArrayList<String> songIntervals) {
		
		
		
		return new ArrayList<Note>();
	}
	
	
	
	public  ArrayList<Integer> convertIntervalsToPitches(ArrayList<String> songIntervals) {
		
		ArrayList<Integer> pitches = new ArrayList<Integer>();
		
		int prevVal = startingPitch;
		
		pitches.add(toMidiPitch(prevVal));
		
		for (String currentInterval : songIntervals) {
			int newInterval = Integer.parseInt(currentInterval);
			
			/* Sets ascLim and descLim to half of the average range of the 
			 * given epoch. DescLim gets the ceiling arbitrarily*/
			int ascLim = range/2;
			int descLim= (range/2) + (range%2);

			int newVal;
			int direction = (int)(Math.random()*2);

			/* determines before each note whether it was generated to be ascending
			 * or descending. This process is regulated with ascLim and descLim */
			boolean ascending = false;
			if(direction == 1)
				ascending = true;

			if (ascending && currentDiff + newInterval >= ascLim) {
				//System.out.println("Switched, too high");
				ascending = false;
			}
			if (!ascending && -1*(currentDiff - newInterval) >= descLim) {
				//System.out.println("Switched, too low");
				ascending = true;
			}
			//System.out.println("Ascending = "+ascending);
						
			if(ascending){
				currentDiff += newInterval;
				//System.out.println("Current Difference = " + currentDiff);
				newVal = prevVal;
				for (int i = 0; i < newInterval; i++){
					if (newVal == 5 || newVal == 12)
						newVal += 1;
					else
						newVal += 2;
					if (newVal > 12) {
						myOctave++;
						newVal -= 12;
					}
				}
			}
			else{
				currentDiff -= newInterval;
				//System.out.println("Current Difference = " + currentDiff);
				newVal = prevVal;
				for (int i = 0; i < newInterval; i++){
					if (newVal == 6 || newVal == 13 || newVal == 1)
						newVal -= 1;
					else
						newVal -= 2;
					if (newVal < 1) {
						newVal += 12;
						myOctave--;
					}
				}
			}
			//change newVal to its actual midi pitch	
			prevVal = newVal;
			pitches.add(toMidiPitch(newVal));
		}	
		return pitches;
	}
	

	private int toMidiPitch(int val) {
		int noteVal;
		int C = myOctave * 12;

		if(val == 1 || val == 13){
			noteVal = C+0;
			//System.out.println("New Pitch = C");
			//newPitchName = "C";
		}
		else if(val == 2){
			noteVal = C+1;
			//System.out.println("New Pitch = C#/Db");
			//newPitchName = "C#";
		}
		else if(val == 3){
			noteVal = C+2;
			//System.out.println("New Pitch = D");
			//newPitchName = "D";
		}
		else if(val == 4){
			noteVal = C+3;
			//System.out.println("New Pitch = D#/Eb");
			//newPitchName = "Eb";
		}
		else if(val == 5){
			noteVal = C+4;
			//System.out.println("New Pitch = E");
			//newPitchName = "E";
		}
		else if(val == 6){
			noteVal = C+5;
			//System.out.println("New Pitch = F");
			//newPitchName = "F";
		}
		else if(val == 7){
			noteVal = C+6;
			//System.out.println("New Pitch = F#/Gb");
			//newPitchName = "F#";
		}
		else if(val == 8){
			noteVal = C+7;
			//System.out.println("New Pitch = G");
			//newPitchName = "G";
		}
		else if(val == 9){
			noteVal = C+8;
			//System.out.println("New Pitch = G#/Ab");
			//newPitchName = "Ab";
		}
		else if(val == 10){
			noteVal = C+9;
			//System.out.println("New Pitch = A");
			//newPitchName = "A";
		}
		else if(val == 11){
			noteVal = C+10;
			//System.out.println("New Pitch = A#/Bb");
			//newPitchName = "Bb";
		}
		else if(val == 12){
			noteVal = C+11;
			//System.out.println("New Pitch = B");
			//newPitchName = "B";
		}
		else {
			return 0;
		}			
		//System.out.println("New Midi Value = " + noteVal);
		return noteVal;
	}
	
	/*
	 * Converts a midi Pitch to its relative Pitch Name (kern format)
	 */
	private String toPitchName(int val) {
		return "";
	}
	

	

}
