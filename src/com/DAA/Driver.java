/*
 * Purpose: Design and Analysis of Algorithms Assignment 2
 * Status: Complete and thoroughly tested
 * Last update: 01/20/15
 * Submitted:  03/06/15
 * Comment: 
 * @author: Albert Rynkiewicz
 * @version: 2015.03.25
 */

package com.DAA;

import java.io.*;

public class Driver {

	// Set up instance variables
	static BufferedReader reader = new BufferedReader(new InputStreamReader(
			System.in));

	static int numPeople;
	static int board[][];
	static int currentCombo[];
	static int jobsChosen = 0;
	static int bestCombo[];
	static boolean done = false;
	static int cost = 0;

	public static void main(String[] args) {

		initialize(args);
		printBoard();

		currentCombo = new int[numPeople];

		// The brains here
		while (!done) {

			if (getNextJob()) {

				// if there is a job assigned for each person, check if the
				// total productivity is greater than the current max
				if (jobsChosen == numPeople) {
					compareCombos();
				}
			} else {
				backUpLevel();
			}
		}
		int total = 0;
		System.out.println("The best job assignment is: ");
		for (int i = 0; i < numPeople; i++) {
			total += board[i][bestCombo[i]];
			System.out.println("Person " + i + " assigned job " + bestCombo[i]);
		}
		System.out.println("Number of Job Assignments Explored: " + cost);
		System.out.println("Best Job Assignment cost: " + total);

	}

	/*
	 * This method simply compares the current productivity combo to the
	 * previous best
	 */
	public static void compareCombos() {
		int currentTotal = 0;
		int pastTotal = 0;

		if (bestCombo == null) {
			bestCombo = new int[numPeople];
			for (int i = 0; i < numPeople; i++) {
				bestCombo[i] = currentCombo[i];
			}
		}
		cost++;
		for (int i = 0; i < numPeople; i++) {
			currentTotal += board[i][currentCombo[i]];
			pastTotal += board[i][bestCombo[i]];
		}

		if (currentTotal >= pastTotal) {
			for (int i = 0; i < numPeople; i++) {
				bestCombo[i] = currentCombo[i];
			}
		}

	}

	/*
	 * This method assigns the next person to the next job
	 */
	public static boolean getNextJob() {

		// If jobsChosen == numPeople, that means we have to back up
		if (jobsChosen == numPeople) {
			return false;
		}

		// if we the last chosen job is at the end of the row, we have to back
		// up
		if (jobsChosen != 0) {
			if (currentCombo[jobsChosen - 1] == numPeople) {
				return false;
			}
		}
		// We are good to get the next Job, try each job left to right until we
		// find a valid one
		for (int i = 0; i < numPeople; i++) {
			if (isValid(i)) {
				currentCombo[jobsChosen] = i;
				jobsChosen++;
				return true;
			}
		}

		return false;
	}

	/*
	 * This method checks all the previous values in currentCombo to see if the
	 * next one will be valid or not
	 */
	public static boolean isValid(int pos) {

		// Start at index jobsChosen-1 and go down to 0
		for (int i = jobsChosen - 1; i >= 0; i--) {
			if (pos == currentCombo[i]) {
				return false;
			}
		}
		// Test passed, return true,position is not interfering with others
		return true;
	}

	/*
	 * This method backtracks one level on the board
	 */
	public static void backUpLevel() {

		// if we are trying to backup when we are on the first level at the last
		// position,
		// then we are done
		if (jobsChosen == 1 && currentCombo[0] == numPeople) {
			done = true;
			return;
		}

		if (jobsChosen > 1) {
			currentCombo[jobsChosen - 1] = 0;// reset last row
			jobsChosen--;

			do {
				currentCombo[jobsChosen - 1]++;
			} while (!checkRows() && currentCombo[jobsChosen - 1] < numPeople);
			
		} else {
			currentCombo[0]++;// increment top row
			jobsChosen--;
		}

	}

	/*
	 * 
	 * 
	 * @returns true if previous values do not interfere with new incremented
	 * row
	 * 
	 * @returns false if there was a conflict
	 */
	public static boolean checkRows() {

		for (int i = 0; i < jobsChosen - 1; i++) {
			if (currentCombo[i] == currentCombo[jobsChosen - 1]) {
				return false;
			}
		}
		return true;
	}

	/*
	 * This method sets up and initialized the job board
	 */

	public static void initialize(String[] args) {

		System.out.print("Please enter the number of People (n): ");
		System.out.println(args[0]);
		numPeople = Integer.parseInt(args[0]);
		board = new int[numPeople][numPeople];

		int i = 0;
		int j = 0;

		// Convert args to ints
		int size = args.length;
		int[] argsNums = new int[size - 1];
		// start at 1 because first int specifies size
		for (i = 1; i < size; i++) {
			argsNums[i - 1] = Integer.parseInt(args[i]);
			// System.out.println(argsNums[i-1]);
		}

		// Row Counter
		for (i = 0; i < numPeople; i++) {

			// Column Counter
			for (j = 0; j < numPeople; j++) {
				board[i][j] = argsNums[numPeople * i + j];
			}

		}

	}

	/*
	 * This metho prints the people/job board cleanly
	 */
	public static void printBoard() {
		int i = 0;
		int j = 0;

		// Row Counter
		for (i = 0; i < numPeople; i++) {

			// Column Counter
			for (j = 0; j < numPeople; j++) {
				System.out.print(board[i][j] + "\t");
			}
			System.out.println("");

		}

	}

}
