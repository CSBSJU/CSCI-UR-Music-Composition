package entities;

public class Note {
	
	/*
	 * the time-length of the note
	 */
	private int duration;
	/*
	 * the pitch (sound) of the note
	 */
	private int pitch, noteNumber;
	/*
	 * the conventional name given to the note
	 */
	private String durationName, pitchName;
	/*
	 * boolean signifying if the note is a rest
	 */
	private boolean isRest;

	
	/*
	 * Constructor
	 * @param pitch  the pitch value for the note
	 * @param duration  the length of the note
	 * @param durationName  the conventional name of the note
	 */
	public Note(int pitch, int duration, String durationName) {
		this.setPitch(pitch);
		this.setDuration(duration);
		this.setDurationName(durationName);
	}
	
	
	/*
	 * Constructor
	 * @param duration  the length of the note
	 * @param durationName  the conventional name of the note
	 * @param isRest  tells if the note is a rest
	 */
	public Note(int noteNumber, int duration, String durationName, boolean isRest) {
		this.setNoteNumber(noteNumber);
		this.setDuration(duration);
		this.setDurationName(durationName);
		this.setRest(isRest);
	}
	
	
	/*
	 * Constructor
	 * @param duration  the length of the note
	 * @param durationName  the conventional name of the note
	 * @param isRest  tells if the note is a rest
	 */
	public Note(int noteNumber, int duration, String durationName, int pitch, boolean isRest) {
		this.setNoteNumber(noteNumber);
		this.setDuration(duration);
		this.setDurationName(durationName);
		this.setPitch(pitch);
		this.setRest(isRest);
	}
	
	
	public int getNoteNumber() {
		return noteNumber;
	}


	public void setNoteNumber(int noteNumber) {
		this.noteNumber = noteNumber;
	}


	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getPitch() {
		return pitch;
	}

	public void setPitch(int pitch) {
		this.pitch = pitch;
	}
	
	public String getPitchName() {
		return pitchName;
	}

	public void setPitchName(String pitch) {
		this.pitchName = pitch;
	}

	public String getDurationName() {
		return durationName;
	}

	public void setDurationName(String durationName) {
		this.durationName = durationName;
	}

	public boolean isRest() {
		return isRest;
	}

	public void setRest(boolean isRest) {
		this.isRest = isRest;
	}
	
	public String toString() {
		return  durationName + "\n Pitch: " + pitch + "\n";
	}

}
