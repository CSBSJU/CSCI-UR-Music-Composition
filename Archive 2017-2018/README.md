# CSCI-UR-Music-Composition
All things pertaining to the All-College Thesis work of Tom Richmond with Dr. Imad Rahal

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
 * that will need to be added to Javaâ€™s build path, one for reading the JSON configuration file and the other for YAML files 
 * that contain data for durations and intervals.  By running MusicCreation, a new midi file and a **kern similar file 
 * will be outputted by the paths specified in the constructor.  Which epoch the system will attempt to create can also be 
 * changed in the constructor.  The system will generate 10 measures by default, which can also be adjusted in the constructor for MusicCreation. 
 Changing the selected time feel for each Epoch has to be adjusted in the Json file located at "bin/textFiles/jsonConfigFile.json."  The system randomly generate single measures at a time, but all the measures are added together 
 * to be outputted as a group at the end.  Each measure is composed of Note objects, which each contains information such as its 
 * duration (length) and pitch.  When generating music, values for the durations and pitches are generated separately.
 *   We first generate a measure comprised of just rhythmic values, knowing how many notes and rests there will be along with
 *    the duration for each.  We then randomly generate enough intervals for all the notes in the measure.  Using a similar 
 *    strategy as was originally implemented, we convert the intervals into actual pitches and combine them with the durations 
 *    to output notes with a sound and length.
 */
