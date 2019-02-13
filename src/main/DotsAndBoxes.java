package main;

import dataStructures.Node;

public class DotsAndBoxes {
	public static void main(String[] args)
	{
		Node node1 = new Node();
		Node node2 = new Node();
		node1.printNode();
		System.out.print("\n");
		node1.makeMove(0, 1, 1, node2);
		node2.printNode();
		System.out.print("\n");
		node2.makeMove(1, 0, 2, node1);
		node1.printNode();
		System.out.print("\n");
		node1.makeMove(1, 2, 1, node2);
		node2.printNode();
		System.out.print("\n");
		node2.makeMove(2, 1, 2, node1);
		node1.printNode();
	}
}
