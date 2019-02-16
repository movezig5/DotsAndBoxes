package main;

import java.util.Random;
import java.util.Scanner;
import algorithms.MinMax;
import dataStructures.Node;
import enums.Error;
import enums.Player;

/* *******************
 * Class: DotsAndBoxes
 * *******************
 * Description:
 * 		The starting class of the program. Contains the main method.
 * Variables: none
 */
public class DotsAndBoxes {
	// main
	// The main method of the program. Collects user input and executes the game loop,
	// alternately getting the player's next move and determining the computer's next move.
	// The player can specify the dimensions of the grid, the number of plys used in the
	// min/max algorithm, and who goes first (player, computer, or choose randomly).
	// At the end of the game, the program will display the final score and announce the winner.
	// The program exits after a single game. The user must run the program again to play again.
	// Arguments:
	//		args: Arguments to the program. Not used.
	// Returns: none
	public static void main(String[] args)
	{
		Scanner s = new Scanner(System.in);
		System.out.println("Welcome to dots and boxes.");
		System.out.println("Made by Daniel Ziegler for CSC 480");
		int width = 0;
		int height = 0;
		int numPlys = 0;
		int playerSelection = 0;
		Player starting;
		Node root;
		int[] move = new int[2];
		String[] moveStr;
		Error result;
		while(width == 0)
		{
			System.out.print("Please enter the desired width of the grid: ");
			try {
				width = Integer.parseInt(s.nextLine());
			} catch(NumberFormatException e)
			{
				System.out.print("\nThat is not a valid width. Please try again.");
			}
		}
		while(height == 0)
		{
			System.out.print("\nPlease enter the desired height of the grid: ");
			try {
				height = Integer.parseInt(s.nextLine());
			} catch(NumberFormatException e)
			{
				System.out.print("\nThat is not a valid height. Please try again.");
			}
		}
		while(numPlys == 0)
		{
			System.out.println("\nPlease enter the desired difficulty (number of plys). It must be a number greater than zero.");
			System.out.println("Higher difficulties will make better decisions, but will take longer to act.");
			System.out.print("Difficulty selection: ");
			try {
				numPlys = Integer.parseInt(s.nextLine());
			} catch(NumberFormatException e)
			{
				System.out.print("\nThat is not a valid difficulty. Please try again.");
			}
		}
		while(playerSelection == 0)
		{
			System.out.println("\nFinally, decide who you want to go first:");
			System.out.println("1. Player\n2. Computer\n3. Random");
			System.out.print("Selection: ");
			try {
				playerSelection = Integer.parseInt(s.nextLine());
			} catch(NumberFormatException e)
			{
				System.out.print("\nThat is not a valid choice. Please try again.");
			}
			if(playerSelection < 1 || playerSelection > 3)
			{
				playerSelection = 0;
				System.out.print("\nThat is not a valid selection. Please try again.");
			}
		}
		System.out.print("\n");
		
		if(playerSelection == 1)
		{
			starting = Player.MIN;
		}
		else if(playerSelection == 2)
		{
			starting = Player.MAX;
		}
		else
		{
			Random rand = new Random(System.currentTimeMillis());
			boolean coinToss = rand.nextBoolean();
			if(coinToss)
				starting = Player.MIN;
			else
				starting = Player.MAX;
		}
		
		root = new Node(height, width, starting);
		
		System.out.println("Please enter the coordinates of your moves in column, row order."
				+ "\nCoordinates should be separated by a comma.\n");
		
		root.printNode();
		
		if(starting == Player.MAX) {
			System.out.print("Computer's move: ");
			move = MinMax.makeMove(root, numPlys);
			System.out.println(move[1] + "," + move[0]);
			root.makeMove(move[0], move[1]);
			root.printNode();
		}
		
		while(!root.isOver())
		{
			do {
				System.out.print("Player's move: ");
				moveStr = s.nextLine().split(",");
				result = Error.SUCCESS;
				if(moveStr.length < 2)
				{
					System.out.println("That move is invalid. Please enter the desired move in <row>,<column> format.");
					result = Error.INVALID_NUMBER;
				} else
				{
					for(int i = 0; i < 2; i++)
					{
						try {
							move[i] = Integer.parseInt(moveStr[i]);
						} catch (NumberFormatException e)
						{
							System.out.println("That move is invalid. Please enter the desired move in <row>,<column> format.");
							result = Error.INVALID_NUMBER;
						}
					}
				}
				if(result != Error.INVALID_NUMBER)
					result = root.makeMove(move[1], move[0]);
				if(result == Error.INVALID_SPACE)
					System.out.println("You cannot draw a line there.");
				if(result == Error.OUT_OF_BOUNDS)
					System.out.println("That coordinate is outside the boundaries of the grid.");
				if(result == Error.SPACE_FILLED)
					System.out.println("That space already has a line drawn in it.");
			} while (result != Error.SUCCESS);
			System.out.print("\n");
			root.printNode();
			if(!root.isOver())
			{
				System.out.print("Computer's move: ");
				move = MinMax.makeMove(root, numPlys);
				System.out.println(move[1] + "," + move[0]);
				root.makeMove(move[0], move[1]);
				root.printNode();
			}
		}
		
		System.out.println("-------------\nFinal results\n-------------");
		System.out.println("Player score: " + root.getMinScore());
		System.out.println("Computer score: " + root.getMaxScore());
		if(root.getMaxScore() > root.getMinScore())
			System.out.println("Winner: Computer");
		else if(root.getMinScore() > root.getMaxScore())
			System.out.println("Winner: Player");
		else
			System.out.println("The game is a draw.");
		
		System.out.println("\nThank you for playing. Goodbye!");
		
		s.close();
	}
}
