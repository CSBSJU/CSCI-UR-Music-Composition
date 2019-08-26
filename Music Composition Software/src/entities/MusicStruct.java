package entities;

import java.util.ArrayList;
import java.util.Collections;

/**
 * MusicStruct is an entity class that stores a
 * signature for a MusicStruct and a list of 
 * adjacent MusicStrucs
 * 
 * @author Andrew Joseph Heroux
 * @version 1.2
 * @since June 11, 2019
 */
public class MusicStruct {
	
	//Instance variables
	String singature;
	String startOrEnd;
	ArrayList<String> signatureSequence;
	ArrayList<AdjacentMusicStruct> adjasentStructs = new ArrayList<AdjacentMusicStruct>();
	
	/**
	 * Public constructor assigns the instance variable signature
	 * @param signature
	 */
	public MusicStruct(String signature, ArrayList<String> signatureSequence, String startOrEnd) {
		this.singature = signature;
		this.startOrEnd = startOrEnd;
		this.signatureSequence = signatureSequence;
	}
	
	/**
	 * Adds a new AdjacentStruct to this MusicStruct
	 * @param adjacentStruct
	 * @param frequency
	 */
	public void addAdjacentStruct(String adjacentStruct, int frequency) {
		this.adjasentStructs.add(new AdjacentMusicStruct(adjacentStruct, frequency));
	}
	
	
	/**
	 * Sorts the list of adjacent structs by their
	 * their frequencies
	 */
	public void sortByFrequency() {
		Collections.sort(this.adjasentStructs);
	}
	
	public void assignProbabilities() {
		int frequencyCount = 0;
		int totalFrequencyCount = 0;
		
		for (int i = 0; i < this.adjasentStructs.size(); i++) {
			totalFrequencyCount = totalFrequencyCount + this.adjasentStructs.get(i).getFrequency();
		}
		
		for (int i = 0; i < this.adjasentStructs.size(); i++) {
			AdjacentMusicStruct adjacentMusicStruct = this.adjasentStructs.get(i);
			double probability = (double) (adjacentMusicStruct.getFrequency() + frequencyCount) / (double) totalFrequencyCount;
			frequencyCount = frequencyCount + adjacentMusicStruct.getFrequency();
			adjacentMusicStruct.setProbability(probability);
		}
	}
	
	//Getters for signature, signatureSequence, startOrEnd, and adjacentStructs
	public ArrayList<String> getSignatureSequence() {
		return this.signatureSequence;
	}
	public String getSignature() {
		return this.singature;
	}
	public ArrayList<AdjacentMusicStruct> getAdjacentStructs(){
		return this.adjasentStructs;
	}
	public String getStartOrEnd() {
		return this.startOrEnd;
	}
}
