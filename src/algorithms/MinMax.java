package algorithms;

import dataStructures.Node;
import enums.Player;

/* *************
 * Class: MinMax
 * *************
 * Description:
 * 		A class that executes the min/max (or minimax) algorithm with alpha-beta pruning.
 * 		Contains only static methods.
 * Variables: none
 */
public class MinMax {
	// makeMove
	// A method that makes the initial call to the min/max algorithm.
	// Uses the return value of that algorithm to select the best move
	// from among its children nodes.
	// Arguments:
	//		node:		The node from which to make the move
	//		numPlys:	The number of plys (or depth) to look forward by
	// Returns:
	//		An array of two integers representing the row and column of the computer's next move.
	//		The first element represents the row and the second represents the column.
	public static int[] makeMove(Node node, int numPlys)
	{
		int coords[] = {0, 0};
		Node root = new Node(node);
		int best = minMax(root, numPlys, Integer.MIN_VALUE, Integer.MAX_VALUE);
		for(Node n : root.children)
		{
			if(n.val == best)
			{
				coords[0] = n.getChangedRow();
				coords[1] = n.getChangedCol();
				return coords;
			}
		}
		return coords;
	}
	
	// minMax
	// A recursive method that executes the min/max algorithm with alpha-beta pruning.
	// While it was possible to do this non-recursively without alpha-beta pruning,
	// it is too complicated when alpha-beta pruning is used.
	// Arguments:
	//		root:	The root node to construct the search tree from
	//		plys:	The number of plys to use (i.e. the number of moves to look ahead by)
	//		alpha:	The "alpha" value, or highest value found so far
	//		beta:	The "beta" value, or lowest value found so far
	// Returns:
	//		The "val" variable of the node representing the best move from the current node. 
	private static int minMax(Node root, int plys, int alpha, int beta)
	{
		root.addChildren();
		if(root.depth >= plys || root.children.isEmpty())
		{
			root.eval();
			return root.val;
		}
		if(root.getType() == Player.MAX)
		{
			int bestVal = Integer.MIN_VALUE;
			int value;
			for(Node n : root.children)
			{
				value = minMax(n, plys - 1, alpha, beta);
				if(value > bestVal)
					bestVal = value;
				if(bestVal > alpha)
					alpha = bestVal;
				if(beta <= alpha)
					break;
			}
			root.val = bestVal;
			return bestVal;
		}
		else
		{
			int bestVal = Integer.MAX_VALUE;
			int value;
			for(Node n : root.children)
			{
				value = minMax(n, plys - 1, alpha, beta);
				if(value < bestVal)
					bestVal = value;
				if(bestVal < beta)
					beta = bestVal;
				if(beta <= alpha)
					break;
			}
			root.val = bestVal;
			return bestVal;
		}
	}
}
