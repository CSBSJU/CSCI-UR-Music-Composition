package controllers;

import java.util.ArrayList;
import java.util.List;

import entities.MusicStruct;
import entities.AdjacentMusicStruct;

/**
 * StructReader is a controller class that reads
 * parsed data from the StructParser class and
 * puts it into a song format
 * 
 * @author Andrew Joseph Heroux
 * @version 1.0
 * @since June 12, 2019
 */

public class StructReader {
	
	//instance variable
	ArrayList<MusicStruct> musicStructs;
	
	/**
	 * Public constructor gets parsed MusicStrucs from
	 * StructParser and loads the parsed Structs
	 * into an ArrayList
	 */
	public StructReader(String file) {
		StructParser parsedStruct = new StructParser(file);
		this.musicStructs = parsedStruct.getMusicStructs();
	}
	
	//Method used for testing
	public void printStruct() {
		System.out.println("Print Structs");
		for (int i = 0; i < this.musicStructs.size(); i++) {
			MusicStruct thisStruct = this.musicStructs.get(i);
			ArrayList<AdjacentMusicStruct> thisAdjecentStruct = thisStruct.getAdjacentStructs();
			
			for (int j = 0; j < thisAdjecentStruct.size(); j++) {
				System.out.println("|        " + thisAdjecentStruct.get(j).toString());
			}
			System.out.println("|\n|\n|\n|\n|");
		}
	}
	
	//Method used for testing
	public void printSong(ArrayList<MusicStruct> song) {
		System.out.println("Print Song");
		for (int i = 0; i < song.size(); i++) {
			System.out.println((i + 1) + "  -  " + song.get(i).getSignature() + "  -  Start or End: " + song.get(i).getStartOrEnd());
			for (int j = 0; j < song.get(i).getAdjacentStructs().size(); j++) {
				System.out.print("|        " + (j + 1) + "  -  ");
				System.out.println(song.get(i).getAdjacentStructs().get(j));
			}
		}
		System.out.println("End of Song");
	}
	
	/**
	 * makeStruct constructs the song with 
	 * a specified number of MusicStructs
	 * 
	 * @param numStructs
	 * @return
	 */
	public ArrayList<MusicStruct> makeStruct(int numStructs) {
		
		MusicStruct currentStruct;
		ArrayList<MusicStruct> song = new ArrayList<MusicStruct>();
		
		int firstStruct = (int) (Math.random() * this.musicStructs.size());
		
		currentStruct = this.musicStructs.get(firstStruct);
		song.add(currentStruct);
		
		for (int i = 0; i < numStructs; i++) {
			currentStruct = findNextStruct(currentStruct, song);
			song.add(currentStruct);
		}
		
		return song;
	}
	
	/**
	 * findNextStruct will return a MusicStruct based on 
	 * adjacency frequency of of a specified MusicStruct
	 * to others. The selected MusicStruct is random,
	 * but also weighs more frequently adjacent MusicStructs
	 * higher
	 * 
	 * @param currentStruct
	 * @return
	 */
	public MusicStruct findNextStruct(MusicStruct currentStruct, ArrayList<MusicStruct> song) {
		
		String frontOfSong = "";
		int lookupSize = 0;
		
		if (song.size() < currentStruct.getSignatureSequence().size()) {
			lookupSize = song.size();
			frontOfSong = song.get(0).getSignature();
			for (int i = 1; i < song.size(); i++) {
				frontOfSong = frontOfSong + "&&" + song.get(i).getSignature();
			}
		}
		else {
			lookupSize = currentStruct.getSignatureSequence().size();
			frontOfSong = song.get((song.size() - currentStruct.getSignatureSequence().size())).getSignature();
			for (int i = 1; i < currentStruct.getSignatureSequence().size(); i++) {
				frontOfSong = frontOfSong + "&&" + song.get((song.size() - currentStruct.getSignatureSequence().size()) + i).getSignature();
			}
		}
		
		for (int i = 0; i < this.musicStructs.size(); i++) {
			ArrayList<String> signatureSequenceList = this.musicStructs.get(i).getSignatureSequence();
			
			//for testing
			if (signatureSequenceList.size() - lookupSize == -1) {
				System.out.println(this.musicStructs.get(i).getSignatureSequence());
				System.err.println("BROKEN!");
			}
			
			String signatureSequence = signatureSequenceList.get(signatureSequenceList.size() - lookupSize);
			for (int j = 1; j < lookupSize; j++) {
				signatureSequence = signatureSequence + "&&" +signatureSequenceList.get((signatureSequenceList.size() - lookupSize) + j);
			}
			
			if (frontOfSong.equals(signatureSequence)) {
				
				double rndNum = Math.random();
				
				ArrayList<AdjacentMusicStruct> adjacentStructs = this.musicStructs.get(i).getAdjacentStructs();
				
				if (rndNum < adjacentStructs.get(0).getProbability()) {
					return this.getStructFromSignature(adjacentStructs.get(0).geSignature());
				}
				for (int k = 0; k < adjacentStructs.size() - 1; k++) {
					if (adjacentStructs.get(k).getProbability() < rndNum && rndNum < adjacentStructs.get(k + 1).getProbability()) {
						return this.getStructFromSignature(adjacentStructs.get(k).geSignature());
					}
				}	
			}	
		}
		return null;
	}
	
	/**
	 * getStructFromSignature is and auxiliary
	 * method used by findNextStruct to return
	 * a MusicStruct object given a specified 
	 * MusicStruct signature
	 * 
	 * @param signature
	 * @return
	 */
	public MusicStruct getStructFromSignature(String signature) {
		
		for (int i = 0; i < this.musicStructs.size(); i++) {
			if (this.musicStructs.get(i).getSignature().contentEquals(signature)) {
				return this.musicStructs.get(i);
			}
		}
		return null;
	}
	
	
	/**
	 * structToString takes a list of MusicStructs
	 * and puts the signatures of the MusicStructs
	 * into a list of Strings
	 * 
	 * @param List of MusicStructs
	 * @return List of Strings
	 */
	public ArrayList<String> structToString(ArrayList<MusicStruct> structs){
		
		ArrayList<String> strings = new ArrayList<String>();
		
		for (int i = 0; i < structs.size(); i++) {
			strings.add(structs.get(i).getSignature());
		}
		
		return strings;
	}
	
	
	//getter for musicStructs
	public ArrayList<MusicStruct> getMusicStructs(){
		return this.musicStructs;
	}
	
	

}
