package main;

import java.util.Scanner;
import algorithms.MinMax;
import dataStructures.Node;
import enums.NodeType;

public class DotsAndBoxes {
	public static void main(String[] args)
	{
		Scanner s = new Scanner(System.in);
		String input;
		String[] splitInput;
		int inRow;
		int inCol;
		Node node1 = new Node();
		Node node2 = new Node(node1);
		node1.type = NodeType.MIN;
		
		while(node2 != null) {
			node1.printNode();
			System.out.print("Your move: ");
			input = s.nextLine();
			splitInput = input.split(",");
			inCol = Integer.parseInt(splitInput[0]);
			inRow = Integer.parseInt(splitInput[1]);
			node1.makeMove(inRow, inCol, node2);
			node2.printNode();
			node1 = node2;
			node2 = MinMax.move(node1, 2);
			if(node2 != null)
				node2.printNode();
		}
		s.close();
	}
}
