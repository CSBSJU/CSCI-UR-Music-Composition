package controllers;

import java.io.FileNotFoundException;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.yaml.snakeyaml.Yaml;

import entities.Epoch;
import entities.MeasureDurations;

/*
 * class to read input from JSON and YAML files and add it to a map of Epoch Objects
 */

public class InputController {	

	 /*
     * method to initially set up the epochs with their values by reading JSON and YAML Files
     * @return  a map containing all the possible epochs
     */
    public static HashMap<String, Epoch> readInput()  {
	    
    	HashMap<String, Epoch> epochs = new HashMap<String, Epoch>();
    	
    	JSONParser jsonParser = new JSONParser();
    	 try (FileReader reader = new FileReader("bin/textFiles/jsonConfigFile.json"))
         {
             //Read JSON file
             Object obj = jsonParser.parse(reader); 
             JSONArray employeeList = (JSONArray) obj;
             System.out.println(employeeList);

             //Iterate over employee array
             for (int i = 0; i < employeeList.size(); i ++) {
            	 JSONObject emp = (JSONObject)employeeList.get(i);
            	 Epoch newEpoch = parseEpochObject(emp);
            	 epochs.put(newEpoch.getEra(), newEpoch);
             }

         } catch (FileNotFoundException e) {
             e.printStackTrace();
         } catch (IOException e) {
             e.printStackTrace();
         } catch (ParseException e) {
             e.printStackTrace();
         }
    	 return epochs;
     }
    
    /*
     * Helper method to create epoch objects
     * @param epochObject  the JSON object that will be used to create an epoch
     * @return  the epoch 
     */
     private static Epoch parseEpochObject(JSONObject epochObject)
     {
    	 ArrayList<MeasureDurations> measureTypes = new ArrayList<MeasureDurations>();
    	 ArrayList<Double> epochIntervals = new ArrayList<Double>();
    	 //HashMap<Integer, ArrayList<MeasureIntervals>> measureSizes = new HashMap<Integer, ArrayList<MeasureIntervals>>();
	    	
         JSONObject epoch = (JSONObject) epochObject.get("epoch");
         
         //Get the era for this epoch
         String era = (String) epoch.get("era");   
         System.out.println(era);
          
         //Get the path for the measure intervals
         String intervalLocation = (String) epoch.get("measureIntervals"); 
         //measureSizes = readIntervalsYAML(intervalLocation);	
         epochIntervals = readEpochIntervals(intervalLocation);
         System.out.println(intervalLocation);
          
         //Get the path for the measure durations
         String measureLocation = (String) epoch.get("measureDurations");  
         measureTypes = readDurationsYAML(measureLocation);	
         System.out.println(measureLocation);
     
         //Get the range that intervals follow
         int range = ((Long)epoch.get("range")).intValue();
         System.out.println(range);
         
         //Get the default length for a whole note
         int defaultDuration = ((Long)epoch.get("defaultDuration")).intValue();
         System.out.println(defaultDuration);
         
         //Epoch newEpoch = new Epoch(measureTypes, measureSizes, range, defaultDuration, era);
         Epoch newEpoch = new Epoch(measureTypes, epochIntervals, range, defaultDuration, era);
         return newEpoch;	
     }
      
   
    /*
     * Helper method to initially set up the epochs with their values by reading YAML Files
     * @param fileLocation  a string of where the yaml file is located
     * @return  the measure durations associated with the epoch
     */
    private static ArrayList<MeasureDurations> readDurationsYAML(String fileLocation) {
    	ArrayList<MeasureDurations> measures = new ArrayList<MeasureDurations>();
    	Yaml yaml = new Yaml();
		try (InputStream in = InputController.class.getResourceAsStream(fileLocation)) {
			Map<String, Object> durationProbabilities = yaml.load(in);
			System.out.println(durationProbabilities);
			Set<String> durationNames = durationProbabilities.keySet();
			Double maxOccurrence = 0.0;
			for (Object durationType: durationNames) {
				String temp = durationType.toString();
				if (durationType instanceof Double)
					temp = temp.substring(0, temp.length()-1);
				String percentages = durationProbabilities.get(durationType).toString();
				int split = percentages.indexOf(";");
				Double filePercentage = Double.parseDouble(percentages.substring(split+1));
				if (filePercentage > maxOccurrence) 
					maxOccurrence = filePercentage;									
			}
			for (Object durationType: durationNames) {
				String temp = durationType.toString();
				if (durationType instanceof Double)
					temp = temp.substring(0, temp.length()-1);
				String percentages = durationProbabilities.get(durationType).toString();
				int split = percentages.indexOf(";");
				String totalPercentage = percentages.substring(0, split-1);
				Double filePercentage = Double.parseDouble(percentages.substring(split+1));
				measures.add(new MeasureDurations(temp, Double.parseDouble(totalPercentage)/maxOccurrence*filePercentage));								
			}
			in.close();									
		}
		catch(IOException ioe) {
			System.out.println("Sorry!");
		}			
		return measures;
    }
        	    	    
    
    /*
     * Helper method to initially set up the epochs with their values by reading YAML Files
     * @param fileLocation  a string of where the yaml file is located
     * @return  the measure intervals associated with the epoch
     */
//    private static HashMap<Integer, ArrayList<MeasureIntervals>> readIntervalsYAML(String fileLocation) {
//    	HashMap<Integer, ArrayList<MeasureIntervals>> measureSizes = new HashMap<Integer, ArrayList<MeasureIntervals>>();
//    	Yaml yaml = new Yaml();
//		try (InputStream in = InputController.class.getResourceAsStream(fileLocation)) {
//			Map<String, Object> intervalProbabilities = yaml.load(in);
//			System.out.println(intervalProbabilities);
//			Set<String> intervalNames = intervalProbabilities.keySet();
//			for (Object intervalType: intervalNames) {
//				String temp = intervalType.toString();				
//				MeasureIntervals tempInterval = new MeasureIntervals(temp, (Double)intervalProbabilities.get(intervalType));
//				int intervalSize = tempInterval.getSize();
//				ArrayList<MeasureIntervals> currentAvailableIntervals = measureSizes.get(intervalSize);
//				if (currentAvailableIntervals == null) 
//					currentAvailableIntervals = new ArrayList<MeasureIntervals>();
//				currentAvailableIntervals.add(tempInterval);								
//				measureSizes.put(intervalSize, currentAvailableIntervals);									
//			}													
//		}
//		catch(IOException ioe) {
//			System.out.println("Sorry!");
//		}
//		return measureSizes;
//    }
       
    
    private static ArrayList<Double> readEpochIntervals(String fileLocation) {
    	ArrayList<Double> probabilities = new ArrayList<Double>();
    	Yaml yaml = new Yaml();
		try (InputStream in = InputController.class.getResourceAsStream(fileLocation)) {
			Map<Integer, Double> intervalProbabilities = yaml.load(in);
			//System.out.println(intervalProbabilities);
			Set<Integer> intervalNames = intervalProbabilities.keySet();
			for (int intervalType: intervalNames) {
				probabilities.add(intervalType-1, intervalProbabilities.get(intervalType));								
			}													
		}
		catch(IOException ioe) {
			System.out.println("Sorry!");
		}
		return probabilities;
    }
	
}
