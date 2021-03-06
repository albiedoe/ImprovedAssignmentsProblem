/*
 * Purpose: Design and Analysis of Algorithms Assignment 2
 * Status: Complete and thoroughly tested
 * Last update: 03/25/15
 * Submitted:  03/30/15
 * Comment: Same as Assignment 2. Only Difference is need to check at each assignment
 * if we can prune. We prune by taking best possible combo of next rows and adding them to the total
 * to see if it can be higher than the previous max.
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
	static int bestTotal = 0;
	static int bestPossibleCombination[];
	static boolean done = false;
	static int cost = 0;
	static int partial = 0; 

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
				else if(canPrune()){//Compare partial solution with optimal future picks with currentBest
					partial++;
					skipCurrentJob();
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
		System.out.println("Number of Partial Job Assignments Explored: " + partial);
		int sum = partial+cost;
		System.out.println("Number of Total Job Assignments Explored: " + sum);		
		System.out.println("Best Job Assignment cost: " + total);

	}

	/*
	 * This method skips the currently last assigned job because it cannot lead 
	 * to a feasible solution.
	 */
	public static void skipCurrentJob() {
		do {
			currentCombo[jobsChosen - 1]++;
		} while (!checkRows() && currentCombo[jobsChosen - 1] < numPeople);
		
	}
	
	
	
	/*
	 * This method compares the currentCombination of assignments with the best
	 * possible future picks to see if it can be greater than the current best Total.
	 * If true, then partial gets incremented and we back up because it can't lead 
	 * to a feasible solution
	 * 
	 * @return boolean stating whether we can prune this branch or not
	 */
	public static boolean canPrune() {
		boolean result = false;
		int projectedTotal = 0;
		
		int i = 0;
		while(i<jobsChosen){
			projectedTotal += board[i][currentCombo[i]];
			i++;
		}
		while(i<numPeople){
			projectedTotal += bestPossibleCombination[i];
			i++;
		}
		if(projectedTotal<bestTotal){//Cant be greater than current Best Total
			result = true;
		}
		return result;
	}
	
	/*
	 * This method simply compares the current productivity combo to the
	 * previous best
	 */
	public static void compareCombos() {
		int currentTotal = 0;
		int pastTotal = 0;

		cost++;
		for (int i = 0; i < numPeople; i++) {
			currentTotal += board[i][currentCombo[i]];
			pastTotal += board[i][bestCombo[i]];
		}
		
		if (currentTotal >= pastTotal) {
			bestTotal = 0;
			for (int i = 0; i < numPeople; i++) {
				bestCombo[i] = currentCombo[i];
				bestTotal += board[i][currentCombo[i]];
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
		bestCombo = new int[numPeople];
		bestPossibleCombination = new int[numPeople];
		
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
				int num = argsNums[numPeople * i + j];
				//Initializes board
				board[i][j] = num;
				//initializes bestPossibleCombination
				if(bestPossibleCombination[i]<num){
					bestPossibleCombination[i]=num;
				}
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
