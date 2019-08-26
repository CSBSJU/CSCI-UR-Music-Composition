package controllers;

import entities.Note;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import java.util.ArrayList;

public class StatisticsOutputController {



	public static void outputStatistics(ArrayList<Note> notes, ArrayList<Integer> measureMarkers, String fileName) {
		final int DEFAULT_DURATION = 4000;
		int measureCounter = 0;

		try {
			PrintWriter writer = new PrintWriter(fileName);

			for (int i = 0; i < notes.size(); i++) {
				Note nextNote = notes.get(i);				
				writer.print(nextNote.getNoteNumber());
				
				if (DEFAULT_DURATION/nextNote.getNoteNumber()*1.5 == nextNote.getDuration())
					writer.print(".");
				else if  (DEFAULT_DURATION/nextNote.getNoteNumber()*1.75 == nextNote.getDuration())
					writer.print("..");
				
				if (nextNote.isRest())
					writer.println("r");
				else
					writer.println(nextNote.getPitchName());
				
				int nextMarker = measureMarkers.get(measureCounter);
				if (nextMarker == i && measureCounter < measureMarkers.size()-1) {
					writer.println("=" + (++measureCounter+1));
				}
			}
			writer.close();

		}catch (FileNotFoundException fnfe) {}
	}

}
