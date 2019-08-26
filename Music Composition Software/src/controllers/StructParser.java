package controllers;


import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import entities.MusicStruct;

/**
 * StructParser reads a JSON file that is structured
 * as a signature(String), adjacentStruct(A list of Strings)
 * and adjacentStructFrequencies(A list of Integers) and prepares it
 * to be read by CSCI-UR-MUSIC-COMPOSITION.
 * 
 * @author Andrew Joseph Heroux
 * @version 1.3
 * @since June 11, 2019
 */
public class StructParser {
	
	//instance variable
	ArrayList<MusicStruct> musicStructs = new ArrayList<MusicStruct>();
	
	
	/**
	 * Public constructor takes in a file name of an appropriately
	 * structured JSON file for parsing into a MusicStruct object.
	 * 
	 * @param fileName
	 * @throws IOException 
	 */
	
	public StructParser(String fileName) {
		this.loadData(fileName);
	}
	
	
	
	/**
	 * Loads data from a specified file into the instance variable
	 * musicStructs.
	 * 
	 * @param fileName
	 */
	@SuppressWarnings("unchecked")
	public void loadData(String fileName) {
		
		JSONParser jsonParser = new JSONParser();
		
		try {
			@SuppressWarnings("unchecked")
			Map<String, Map<String, Object>> parsedArray  =  (Map<String, Map<String, Object>>) jsonParser.parse(new FileReader("jsonFiles/" + fileName));
			
			Iterator<String> keyIterator = parsedArray.keySet().iterator();
			

			while (keyIterator.hasNext()) {
				String unparsedSignature = keyIterator.next();
				ArrayList<String> signatureSequence = new ArrayList<String>(Arrays.asList(unparsedSignature.split("&&")));
				String signature = signatureSequence.get(0);
				String startEnd = (String) parsedArray.get(unparsedSignature).get("startEnd");
				
				Map<String, Object> adjacentValues = parsedArray.get(unparsedSignature);
				
				@SuppressWarnings("unchecked")
				Map<String, Long> mapOfAdjacentSigs = (Map<String, Long>) adjacentValues.get("adjacentSignaturs");
				
				Iterator<String> adjacentKeys = mapOfAdjacentSigs.keySet().iterator();
				
				System.out.println("Token: " + unparsedSignature);
				System.out.println("StartEnd: " + startEnd);
				
				MusicStruct newMusicStruct = new MusicStruct(signature, signatureSequence, startEnd);
				
				System.out.println(signature);
				
				while (adjacentKeys.hasNext()) {
					String adjacentStruct = adjacentKeys.next();
					mapOfAdjacentSigs = (Map<String, Long>) adjacentValues.get("adjacentSignaturs");
					int frequency = ((Long) mapOfAdjacentSigs.get(adjacentStruct)).intValue();
					
					newMusicStruct.addAdjacentStruct(adjacentStruct, frequency);
					
				}
				newMusicStruct.sortByFrequency();
				newMusicStruct.assignProbabilities();
				this.musicStructs.add(newMusicStruct);
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	/**
	 * Getter for the ArrayList musicStrucs
	 * @return musicStructs
	 */
	public ArrayList<MusicStruct> getMusicStructs(){
		return this.musicStructs;
	}
}
