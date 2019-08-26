package GUI;

/*
 * Algorithmic Music Composition Software
 * @author Tom Donald Richmond
 * @author Ryan Strelow
 * @version 3.0
 * @since 02/12/17
 */

/*
 * Java Start up Guide:
 * The project is run from the class MusicCreation.  At this point, this class operates more as a driver than a GUI, 
 * as changing which input files will be read from has to be changed in jsonConfigFile.json.  There are two libraries 
 * that will need to be added to Java’s build path, one for reading the JSON configuration file and the other for YAML files 
 * that contain data for durations and intervals.  By running MusicCreation, a new midi file and a **kern similar file 
 * will be outputted by the paths specified in the constructor.  Which epoch the system will attempt to create can also be 
 * changed in the constructor.  The system randomly generate single measures at a time, but all the measures are added together 
 * to be outputted as a group at the end.  Each measure is composed of Note objects, which each contains information such as its 
 * duration (length) and pitch.  When generating music, values for the durations and pitches are generated separately.
 *   We first generate a measure comprised of just rhythmic values, knowing how many notes and rests there will be along with
 *    the duration for each.  We then randomly generate enough intervals for all the notes in the measure.  Using a similar 
 *    strategy as was originally implemented, we convert the intervals into actual pitches and combine them with the durations 
 *    to output notes with a sound and length.
 */

import java.util.HashMap;
import java.util.ArrayList;

import entities.MeasureDurations;
import entities.MeasureIntervals;
import entities.MusicStruct;
import entities.Epoch;
import entities.Note;

import controllers.InputController;
import controllers.midiOutputController;
import controllers.StatisticsOutputController;
import controllers.StructReader;
import controllers.StructInterpreter;
import controllers.NoteConstructor;

public class MusicCreation {
	 //map that contains all the epochs and their values
    private HashMap<String, Epoch> epochs;
 
    /*
     * The preceding pitch value
     */
    private int prevPitch = 0;
    
    /*
     * The new Pitch represented as a string
     */
    private String newPitchName = "";
    
    /*
     * THe new Pitch represented as an integer (defaults at middle C)
     */
    private int newPitch = 60;
    
    /*
     * String representation of the current era that is being played
     */
    private String era;
    
    /*
     * list of the notes that are algorithmically generated 
     */
    private ArrayList<Note> notes;
    
    /*
     * list of where the measures are in the generated music (for use in statistic files)
     */
    private ArrayList<Integer> measureMarkers;
    
    /*
     * list of the available measures with durations for the currently selected epoch
     */
    private ArrayList<MeasureDurations> measureTypes;
    
    /*
     * list of the probabilities of each interval for the currently selected epoch (unison, step, third, etc.)
     */
    private ArrayList<Double> epochIntervals;
    
 
    
    // variables to ensure the composer runs linearly
    //TODO: EDIT DEFAULT DURATION SO THAT IT WORKS AS INTENDED
    public int myOctave = 5, currentDiff = 0, range = 14, defaultDuration;
    
    private final int DEFAULT_WHOLENOTE_DURATION = 4000;
	

	
	
	public MusicCreation() {

		notes = new ArrayList<Note>();
		measureMarkers = new ArrayList<Integer>();
		
		
		//durationsStruct = new StructReader("kern.json");
		//ArrayList<MusicStruct> durations = durationsStruct.makeStruct(200);
	
	}
	
	public void makeMusicFromJson(String fileName, int numMeasures) {
		
		StructReader structReader = new StructReader("m" + fileName + ".json");
		StructInterpreter interpreter = new StructInterpreter();
		
		//TODO: remember the location of measure dividers (either keep track or count after)
		ArrayList<String> durations = interpreter.interpretMeasure(structReader.structToString(structReader.makeStruct(numMeasures)));
				
		structReader = new StructReader("i" + fileName + ".json");
		ArrayList<String> intervals = structReader.structToString(structReader.makeStruct(durations.size()-interpreter.countRests(durations)));
		
		NoteConstructor constructor = new NoteConstructor();
		notes = constructor.constructNotes(durations, intervals);
			
		midiOutputController.outputMidi(notes, "/home/rstrelow001/MusicComposition/CSCI-UR-Music-Composition/Audio Files/6-20-19/" + fileName + ".mid");	
			
	}
	
	

	

	
	

	
	/*
	 * method to generate a new measure of music and add the notes to a list.
	 * The method selects a measure of durations from the available pool and then selects
	 * the number of intervals needed based on the number of durations in the measure (excluding rests). 
	 */
//	 private void generateMeasure() {
//	    	
//	    	MeasureDurations newDurations = measureDurationsGenerator();
//	    	MeasureIntervals newIntervals;
//	    	newIntervals = intervalGenerator(newDurations.getSize());
//	    	while(newDurations.hasNextDuration()) {
//	    		Note newNote = newDurations.nextDuration();
//	    		System.out.println("Duration: " + newNote.getDurationName());
//	    	    System.out.println("Time Value: " + newNote.getDuration());
//	    		if (!newNote.isRest()) {
//	    			int newInterval = newIntervals.nextInterval();
//	    			System.out.println("Interval: " + newInterval);
//	    			prevPitch = playNextNote(newInterval, prevPitch, newNote.getDuration());
//	    			//newPitch and newPitchName are updated in playNextNote()
//	    			newNote.setPitch(newPitch);
//	    			newNote.setPitchName(newPitchName);	    			
//	    		}
//	    		System.out.println();
//	    		notes.add(newNote);
//	    	}
//	    	//remembers where the next measure is
//	    	measureMarkers.add(notes.size()-1);
//	    	System.out.println("-------------END OF MEASURE--------------\n");
//	    	newDurations.resetDurations();
//	    	newIntervals.resetIntervals();
//	    }
//
//	 
//	 /* Method to re-adjust the probability values when a new epoch is selected
//     * @param String representing epoch
//     */
//    public void changeEpoch(String epoch) {
//    	Epoch newEpoch = epochs.get(epoch); 
//    	range = newEpoch.getRange();
//    	defaultDuration = newEpoch.getDefaultDuration();
//    	era = newEpoch.getEra();      	
//    	measureTypes = newEpoch.getDurationPatterns();
//    	epochIntervals = newEpoch.getSingleIntervals();
//    }
//    
//      
//    /*
//     * Method designed to generate a new measure of durations
//     * @returns a randomly generated measure of intervals
//     * */	
//	private MeasureDurations measureDurationsGenerator(){		
//		MeasureDurations tempMeasure = measureTypes.get(0);
//		/* Resets the valFound var to false for next note generation */
//		boolean valFound = false;
//		
//		while (!valFound) {
//			int i = 0;
//			double running = 0.0;
//			double value = Math.random();
//
//			while (!valFound && i < measureTypes.size()) {
//				tempMeasure = measureTypes.get(i);
//				double measureProbability = tempMeasure.getProbability();
//				if (value <= measureProbability + running) 
//					valFound = true;
//
//				running += measureProbability;
//				i++;
//			}
//		}
//		return tempMeasure;
//	}
//	
//	
//	private MeasureIntervals intervalGenerator(int size) {
//		MeasureIntervals newMeasure = new MeasureIntervals();
//		
//		//adds enough intervals to match the number of durations in the measure
//		for (int i = 0; i < size; i++) {
//			boolean valFound = false;
//
//			while (!valFound ) {
//				double running = 0.0;
//				double value = Math.random();
//
//				int j = 0;
//				while (!valFound && j < epochIntervals.size()) {
//					double probability = epochIntervals.get(j);
//					if (value <= probability + running) {
//						valFound = true;
//						newMeasure.addInterval(j);						
//					}
//
//					running += probability;
//					j++;
//				}
//			}
//		}
//		return newMeasure;		
//	}
//	 
//	 
//	 /*
//	     * Method designed to generate a new musical note value based on given previous note value
//	     * @param int prevVal
//	     * @returns int newVal
//	     * */	
//		private int playNextNote(int newInterval, int prevVal, int duration){
//			if (prevVal == 0){				
//				toNote(1);
//				return 1;
//			}
//			/* Sets ascLim and descLim to half of the average range of the 
//			 * given epoch. DescLim gets the ceiling arbitrarily*/
//			int ascLim = range/2;
//			int descLim= (range/2) + (range%2);
//
//			int newVal;
//			int direction = (int)(Math.random()*2);
//
//			/* determines before each note whether it was generated to be ascending
//			 * or descending. This process is regulated with ascLim and descLim */
//			boolean ascending = false;
//			if(direction == 1)
//				ascending = true;
//
//			if (ascending && currentDiff + newInterval >= ascLim) {
//				System.out.println("Switched, too high");
//				ascending = false;
//			}
//			if (!ascending && -1*(currentDiff - newInterval) >= descLim) {
//				System.out.println("Switched, too low");
//				ascending = true;
//			}
//			System.out.println("Ascending = "+ascending);
//						
//			if(ascending){
//				currentDiff += newInterval;
//				System.out.println("Current Difference = " + currentDiff);
//				newVal = prevVal;
//				for (int i = 0; i < newInterval; i++){
//					if (newVal == 5 || newVal == 12)
//						newVal += 1;
//					else
//						newVal += 2;
//					if (newVal > 12) {
//						myOctave++;
//						newVal -= 12;
//					}
//				}
//			}
//			else{
//				currentDiff -= newInterval;
//				System.out.println("Current Difference = " + currentDiff);
//				newVal = prevVal;
//				for (int i = 0; i < newInterval; i++){
//					if (newVal == 6 || newVal == 13 || newVal == 1)
//						newVal -= 1;
//					else
//						newVal -= 2;
//					if (newVal < 1) {
//						newVal += 12;
//						myOctave--;
//					}
//				}
//			}
//			//change newVal to its actual midi pitch
//			newPitch = toNote(newVal);
//
//			//return newVal, which is a relative pitch, so that the next generated interval can be applied to it
//			return newVal;
//		}
//
//		
//		/*
//		 * Method that takes pitch value representation from binary as integer, prints corresponding
//		 * value and updates the String representation of the pitch
//		 * @param int val - Value of pitch (1-13) generated by the rule system
//		 * @returns the absolute pitch as an int
//		 * */
//		private int toNote(int val) {
//			int noteVal;
//			int C = myOctave * 12;
//
//			if(val == 1 || val == 13){
//				noteVal = C+0;
//				System.out.println("New Pitch = C");
//				newPitchName = "C";
//			}
//			else if(val == 2){
//				noteVal = C+1;
//				System.out.println("New Pitch = C#/Db");
//				newPitchName = "C#";
//			}
//			else if(val == 3){
//				noteVal = C+2;
//				System.out.println("New Pitch = D");
//				newPitchName = "D";
//			}
//			else if(val == 4){
//				noteVal = C+3;
//				System.out.println("New Pitch = D#/Eb");
//				newPitchName = "Eb";
//			}
//			else if(val == 5){
//				noteVal = C+4;
//				System.out.println("New Pitch = E");
//				newPitchName = "E";
//			}
//			else if(val == 6){
//				noteVal = C+5;
//				System.out.println("New Pitch = F");
//				newPitchName = "F";
//			}
//			else if(val == 7){
//				noteVal = C+6;
//				System.out.println("New Pitch = F#/Gb");
//				newPitchName = "F#";
//			}
//			else if(val == 8){
//				noteVal = C+7;
//				System.out.println("New Pitch = G");
//				newPitchName = "G";
//			}
//			else if(val == 9){
//				noteVal = C+8;
//				System.out.println("New Pitch = G#/Ab");
//				newPitchName = "Ab";
//			}
//			else if(val == 10){
//				noteVal = C+9;
//				System.out.println("New Pitch = A");
//				newPitchName = "A";
//			}
//			else if(val == 11){
//				noteVal = C+10;
//				System.out.println("New Pitch = A#/Bb");
//				newPitchName = "Bb";
//			}
//			else if(val == 12){
//				noteVal = C+11;
//				System.out.println("New Pitch = B");
//				newPitchName = "B";
//			}
//			else {
//				return 0;
//			}			
//			System.out.println("New Midi Value = " + noteVal);
//			return noteVal;
//		}
		

}
