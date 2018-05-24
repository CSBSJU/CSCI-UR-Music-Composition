package controllers;

/*
 * Algorithmic Music Composition Software
 * @author Tom Donald Richmond
 * @author Ryan Strelow
 * @version 2.0
 * @since 02/12/17
 */

import java.util.HashMap;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import entities.Epoch;

public class MusicCompositionController {

	// variable to store the current Epoch
	private Epoch epoch;	
    // variables to ensure the composer runs linearly
    public int myOctave = 5, currentDiff = 0, range, start;
    // variable to store the probability of each interval
    double uni, step, third, fourth, fifth, sixth, seventh, octave;
    // boolean to see if an epoch has been selected
    boolean selected = false;
	// variables to track total number of interval occurrences
	int t;
    // variables to track the occurrences of each interval for testing
    int[] totals = new int[8];
    // variable to hold string value representing era
    String era;
    //map that contains all the epochs and their values
    private HashMap<String, Epoch> epochs;
	
	public MusicCompositionController(HashMap<String, Epoch> epochs) {
		this.epochs = epochs;
	}
	
	
    /*
     * Method to re-adjust the probability values when new epoch is selected
     * @param String representing epoch
     */
    public void changeEpoch(String epoch) {
    	Epoch newEpoch = epochs.get(epoch);
    	playNote(newEpoch.getStart());
    	uni = newEpoch.getUni();
    	step = newEpoch.getStep();
    	third = newEpoch.getThird();
    	fourth = newEpoch.getFourth();
    	fifth = newEpoch.getFifth();
    	sixth = newEpoch.getSixth();
    	seventh = newEpoch.getSeventh();
    	octave = newEpoch.getOctave();
    	range = newEpoch.getRange();
    	era = newEpoch.getEra();   	
    }
	
    
    /*
     * Method designed to generate a new musical note value based on given previous note value
     * @param int prevVal
     * @returns int newVal
     * */	
	public int ruleGenerator(int prevVal){
		if (prevVal == 0){
			return 1;
		}

		/* Sets ascLim and descLim to half of the average range of the 
		 * given epoch. DescLim gets the ceiling arbitrarily*/
		int ascLim = range/2;
		int descLim= (range/2) + (range%2);

		double running = 0.0;
		double value = Math.random();
		System.out.println(value);

		int newVal;
		int diff = 0;
		int direction = (int)(Math.random()*2);

		/* determines before each note whether it was generated to be ascending
		 * or descending. This process is regulated with ascLim and descLim */
		boolean ascending = false;
		if(direction == 1)
			ascending = true;

		/* Resets the valFound var to false for next note generation */
		boolean valFound = false;

		/* checks which range the generated number falls in and produces a
		 * note based on this value. Once note is found, valFound is set to
		 * true, and no other if statements are reached. It will access each
		 * if statement until the correct is found, increasing running total
		 * as it goes. */
		if (value <= uni){
			totals[0]+=1;
			t+=1;
			diff = 0;
			valFound = true;
			System.out.println("Unison");
		}
		running += uni;
		if ((value <= step + running) && valFound == false){
			totals[1]+=1;
			t+=1;
			diff =  1;
			valFound = true;
			System.out.println("Step");
		}
		running += step;
		if (value <= third + running && valFound == false){
			totals[2]+=1;
			t+=1;
			diff =  2;
			valFound = true;
			System.out.println("Third");
		}
		running += third;
		if (value <= fourth + running && valFound == false){
			totals[3]+=1;
			t+=1;
			diff =  3;
			valFound = true;
			System.out.println("Forth");
		}
		running += fourth;
		if (value <= fifth + running && valFound == false){
			totals[4]+=1;
			t+=1;
			diff =  4;
			valFound = true;
			System.out.println("Fifth");
		}
		running += fifth;
		if (value <= sixth + running && valFound == false){
			totals[5]+=1;
			t+=1;
			diff =  5;
			valFound = true;
			System.out.println("Sixth");
		}
		running += sixth;
		if (value <= seventh + running && valFound == false){
			totals[6]+=1;
			t+=1;
			diff =  6;
			valFound = true;
			System.out.println("Seventh");
		}
		running += seventh;
		if (value <= octave + running && valFound == false){
			totals[7]+=1;
			t+=1;
			diff =  7;
			valFound = true;
			System.out.println("Octave");
		}

		//System.out.println((currentDiff+diff) +": total diff");
		if (ascending && currentDiff + diff >= ascLim) {
			System.out.println("Switched, too high");
			ascending = false;
		}
		if (!ascending && -1*(currentDiff - diff) >= descLim) {
			System.out.println("Switched, too low");
			ascending = true;
		}
		System.out.println("Ascending = "+ascending);
		if(ascending){
			currentDiff += diff;
			System.out.println(currentDiff);
			newVal = prevVal;
			for (int i = 0; i < diff; i++){
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
			currentDiff -= diff;
			System.out.println(currentDiff);
			newVal = prevVal;
			for (int i = 0; i < diff; i++){
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
		System.out.println(newVal + " " + ascending);
		int noteVal = toNote(newVal, ascending);

		//System.out.println(prevVal);
		//newVal = 1+((int)(Math.random()*12));
		return noteVal;
	}

	
	/*
	 * Method designed to generate a new musical note value based on given previous note value
	 * @param int prevVal
	 * @returns int newVal
	 * */
	public void ruleGeneratorAnalysis(){

		double running = 0.0;
		double value = Math.random();

		/* Resets the valFound var to false for next note generation */
		boolean valFound = false;

		/* checks which range the generated number falls in and produces a
		 * note based on this value. Once note is found, valFound is set to
		 * true, and no other if statements are reached. It will access each
		 * if statement until the correct is found, increasing running total
		 * as it goes. */
		if (value <= uni){
			totals[0]+=1;
			t+=1;
			valFound = true;
		}
		running += uni;
		if ((value <= step + running) && valFound == false){
			totals[1]+=1;
			t+=1;
			valFound = true;
		}
		running += step;
		if (value <= third + running && valFound == false){
			totals[2]+=1;
			t+=1;
			valFound = true;
		}
		running += third;
		if (value <= fourth + running && valFound == false){
			totals[3]+=1;
			t+=1;
			valFound = true;
		}
		running += fourth;
		if (value <= fifth + running && valFound == false){
			totals[4]+=1;
			t+=1;
			valFound = true;
		}
		running += fifth;
		if (value <= sixth + running && valFound == false){
			totals[5]+=1;
			t+=1;
			valFound = true;
		}
		running += sixth;
		if (value <= seventh + running && valFound == false){
			totals[6]+=1;
			t+=1;
			valFound = true;
		}
		running += seventh;
		if (value <= octave + running && valFound == false){
			totals[7]+=1;
			t+=1;
			valFound = true;
		}

		/* When the composer has generated 100 notes, 
		 * it automatically calculates the results and prints 
		 * for analysis process */
		if(t==100) {
			System.out.println(kernResults());
			//JOptionPane.showMessageDialog(null,kernResults());
			clearStats();
		}
	}

	
	/*
	 * Method that takes note value representation from binary as integer, prints corresponding
	 * value and plays note using MIDI output
	 * @param int val - Value of note (1-13) generated by the rule system
	 * @returns String letter value equivelant to corresponding int value
	 * */
	public int toNote(int val, Boolean asc){
		int noteVal;
		int C = myOctave * 12;

		if(val == 1 || val == 13){
			noteVal = C+0;
			System.out.println("C");
		}
		else if(val == 2){
			noteVal = C+1;
			System.out.println("C#/D-");
		}
		else if(val == 3){
			noteVal = C+2;
			System.out.println("D");
		}
		else if(val == 4){
			noteVal = C+3;
			System.out.println("D#/E-");
		}
		else if(val == 5){
			noteVal = C+4;
			System.out.println("E");
		}
		else if(val == 6){
			noteVal = C+5;
			System.out.println("F");
		}
		else if(val == 7){
			noteVal = C+6;
			System.out.println("F#/G-");
		}
		else if(val == 8){
			noteVal = C+7;
			System.out.println("G");
		}
		else if(val == 9){
			noteVal = C+8;
			System.out.println("G#/A-");
		}
		else if(val == 10){
			noteVal = C+9;
			System.out.println("A");
		}
		else if(val == 11){
			noteVal = C+10;
			System.out.println("A#/B-");
		}
		else if(val == 12){
			noteVal = C+11;
			System.out.println("B");
		}
		else {
			return 0;
		}
		//System.out.println(noteVal);
		playNote(noteVal);
		return val;
	}

	
	/*
	 * Method to play note value using MIDI synthesizer based upon input note
	 * @param int representing the MIDI value of desired note.
	 */
	public void playNote(int i) { 
	    try{
	    	/* Create a new Synthesizer and open it. 
	    	 */
		    	Synthesizer midiSynth = MidiSystem.getSynthesizer(); 
		    	midiSynth.open();
		    	
		    	//get and load default instrument and channel lists
		    	Instrument[] instr = midiSynth.getDefaultSoundbank().getInstruments();
		    	MidiChannel[] mChannels = midiSynth.getChannels();
		      
		    	midiSynth.loadInstrument(instr[0]);//load an instrument
		    	mChannels[0].noteOff(i);//turn off the previous note
		    	mChannels[0].noteOn(i, 120);//On channel 0, play note number i with velocity 120
		    	try {
		    		//Following line controls duration of notes played. 1000 used for samples of 30 seconds. 750 used for samples of 15 seconds
		    		Thread.sleep(750); // wait time in milliseconds to control duration
		    	}
		    	catch( InterruptedException e ) {}
	    } 
	    catch (MidiUnavailableException e) {}
	}


	/*
	 * method that returns string that prints composition statistics for visual analysis
	 * @returns String statistics
	 */
	public String printResults() {
		
		return "Total length of composition: "+t+"\n"
				+"\tStatistics:\n"
				+"\nUnison:\t "+((double)totals[0]/t)
				+"\nStep:\t "+((double)totals[1]/t)
				+"\nThird:\t "+((double)totals[2]/t)
				+"\nForth:\t "+((double)totals[3]/t)
				+"\nFifth:\t "+((double)totals[4]/t)
				+"\nSixth:\t "+((double)totals[5]/t)
				+"\nSeventh:\t "+((double)totals[6]/t)
				+"\nOctave:\t "+((double)totals[7]/t);
	}
	
	
	/*
	 * method that returns string that prints composition statistics for analysis
	 * @returns String statistics
	 */
	public String kernResults() {
		//variable to store percentage of most common interval
		int max = 0;
		
		// computes the most common interval
		for(int i = 0; i<8;i++) {
			if(totals[i] > max){
				max = totals[i];
			}
		}
		
		//returns expected String output based on totals array and above computation
		return ""+((double)totals[0]/t)
				+","+((double)totals[1]/t)
				+","+((double)totals[2]/t)
				+","+((double)totals[3]/t)
				+","+((double)totals[4]/t)
				+","+((double)totals[5]/t)
				+","+((double)totals[6]/t)
				+","+((double)totals[7]/t)
				+","+((double)max/t)
				+","+era;
	}

	
	/*
	 * Method to clear the statistics after terminations for next composition
	 */
	public void clearStats() {
		//loops through all saved data and resets to 0 for future processing
		for (int i = 0; i < 8; i++) {
			totals[i] = 0;
		}
		t = 0;
	}

}

