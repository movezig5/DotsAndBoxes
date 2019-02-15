package main;

import java.util.Random;
import java.util.Scanner;
import algorithms.MinMax;
import dataStructures.Node;
import enums.Error;
import enums.Player;

public class DotsAndBoxes {
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
		Node state;
		int[] move = new int[2];
		//String input = "";
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
		
		state = new Node(height, width, starting);
		
		System.out.println("Please enter the coordinates of your moves in column, row order."
				+ "\nCoordinates should be separated by a comma.\n");
		
		state.printNode();
		
		if(starting == Player.MAX) {
			System.out.print("Computer's move: ");
			move = MinMax.makeMove(state, numPlys);
			System.out.println(move[1] + "," + move[0]);
			state.makeMove(move[0], move[1]);
			state.printNode();
		}
		
		while(!state.isOver())
		{
			do {
				System.out.print("Player's move: ");
				moveStr = s.nextLine().split(",");
				for(int i = 0; i < 2; i++)
					move[i] = Integer.parseInt(moveStr[i].trim());
				result = state.makeMove(move[1], move[0]);
				if(result == Error.INVALID_SPACE)
					System.out.println("\nYou cannot draw a line there.");
				if(result == Error.OUT_OF_BOUNDS)
					System.out.println("\nThat coordinate is outside the boundaries of the grid.");
				if(result == Error.SPACE_FILLED)
					System.out.println("\nThat space already has a line drawn in it.");
			} while (result != Error.SUCCESS);
			System.out.print("\n");
			state.printNode();
			if(!state.isOver())
			{
				System.out.print("Computer's move: ");
				move = MinMax.makeMove(state, numPlys);
				System.out.println(move[1] + "," + move[0]);
				state.makeMove(move[0], move[1]);
				state.printNode();
			}
		}
		
		System.out.println("-------------\nFinal results\n-------------");
		System.out.println("Player score: " + state.getMinScore());
		System.out.println("Computer score: " + state.getMaxScore());
		if(state.getMaxScore() > state.getMinScore())
			System.out.println("Winner: Computer");
		else if(state.getMinScore() > state.getMaxScore())
			System.out.println("Winner: Player");
		else
			System.out.println("The game is a draw.");
		
		System.out.println("\nThank you for playing. Goodbye!");
		
		s.close();
	}
}
