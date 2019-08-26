package GUI;


import java.util.ArrayList;

import controllers.*;
import entities.*;

import java.util.Scanner;

public class TestDriver {
	
	public TestDriver() {
		this.testMusicCreation();

	}
	
	
	public void testMusicCreation() {
		
		Scanner in = new Scanner(System.in);
		
		MusicCreator mc = new MusicCreator();
		
//		System.out.println("Enter Era");
//		String era = in.nextLine();
		
		System.out.println("Number of Measures: ");
		int measures = in.nextInt();
		in.close();
		
		mc.makeMusicFromJson("folk-germany_order3",  measures);
	}
	
	public static void main(String args[]) {
		new TestDriver();
	}
	
	
}
