package entities;


/**
 * AdjacentMusicStruct is an entity class that stores
 * two instance variables
 * 
 * @author Andrew Joseph Heroux
 * @version 1.0
 * @since June 11, 2019
 */
public class AdjacentMusicStruct implements Comparable<AdjacentMusicStruct> {
	
	//Instance variables
	String signature;
	int frequency;
	double probability;
	
	/**
	 * Public constructor assigns the instance variables signature and frequency
	 * @param signature
	 * @param frequency
	 */
	public AdjacentMusicStruct(String signature, int frequency) {
		this.signature= signature;
		this.frequency = frequency;
	}
	
	//Getters for signature and frequency
	public String geSignature() {
		return this.signature;
	}
	public int getFrequency() {
		return this.frequency;
	}
	public double getProbability() {
		return this.probability;
	}
	
	//Setter for probability
	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	//implementing compareTo method to compare probabilities 
	public int compareTo(AdjacentMusicStruct adjacentStruct) {
		return this.frequency - adjacentStruct.getFrequency();
	}
	//override toString method
	public String toString() {
		return "Signature: " + this.signature + " Frequency: " + this.frequency + " Probability " + this.probability;
	}
}
