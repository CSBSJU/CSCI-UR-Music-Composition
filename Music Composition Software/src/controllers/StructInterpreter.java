package controllers;


import java.util.ArrayList;


/**
 * StructInterpreter is a class used to parse
 * measures into a list of durations
 * @author Andrew Joseph Heroux
 *
 */
public class StructInterpreter {
	
	
	/**
	 * interpretMeasure parses measures
	 * into individual durations
	 * 
	 * @param A list of measures 
	 * @return A list of durations
	 */
	public ArrayList<String> interpretMeasure(ArrayList<String> unparsedMeasures) {
		
		ArrayList<String> parsedMeasures = new ArrayList<String>();
		
		for (int i = 0; i < unparsedMeasures.size(); i++) {
			//there is extra code here to parse chords into the first duration of the chord
			String[] singleMeasures = unparsedMeasures.get(i).split("-");
			for (int j = 0; j < singleMeasures.length; j++) {
				String[] measureToAdd = singleMeasures[j].split(":");
				parsedMeasures.add(measureToAdd[0]);
			}
		}
		return parsedMeasures;
	}
	
	public int countRests(ArrayList<String> parsedMeasures) {
		int count = 0;
		for (String s : parsedMeasures) {
			if (s.contains("r"))
				count++;
		}
		return count;
	}
}

