package dataStructures;

/* ***********
 * Class: Node
 * ***********
 * Description:
 * 		A class representing a single state of the game.
 * Variables:
 * 		grid:
 * 			A two-dimensional array of integers representing the grid of dots and boxes.
 * 			The first dimension is the rows from top to bottom, and the second dimension is the columns from left to right.
 * 			Every even numbered row contains alternating dots (all 0's), and either lines or spaces, represented by 1's and 0's respectively.
 * 			The odd numbered rows alternate between lines/spaces and numbers from 1 to 5 inclusive that go in the boxes.
 * 		type:
 * 			The player who is making the next move. Used in the min/max algorithm.
 * 		changedRow:
 * 			The row of the move that was made to get to this state.
 * 		changedCol:
 * 			The column of the move that was made to get to this state.
 * 		maxScore:
 * 			The score of the "max" player, i.e. the computer
 * 		minScore:
 * 			The score of the "min" player, i.e. the player
 * 		val:
 * 			For leaf nodes, this represents the result of the evaluation function (maxScore - minScore).
 * 			For other nodes, the value is determined by the min/max algorithm.
 * 		depth:
 * 			The depth of the current node. Used to ensure min/max doesn't exceed the maximum number of plies.
 * 		parent:
 * 			The parent of this node.
 * 		children:
 * 			The children of this node.
 */

import java.util.Random;
import java.util.LinkedList;
import enums.Error;
import enums.Player;

public class Node {
	private int[][] grid;
	private Player type;
	private int changedRow;
	private int changedCol;
	private int maxScore;
	private int minScore;
	public int val;
	public int depth;
	public Node parent;
	public LinkedList<Node> children;
	
	// Default constructor
	// Creates a randomized grid of 4 boxes by 4 boxes (9x9 with the dots and spaces).
	// Player type is set to min (the human player) by default.
	public Node()
	{
		grid = new int[9][9];
		type = Player.MIN;
		changedRow = 0;
		changedCol = 0;
		maxScore = 0;
		minScore = 0;
		val = 0;
		depth = 0;
		parent = null;
		children = new LinkedList<Node>();
		initialize();
	}
	
	// Constructor
	// Creates a randomized grid with the specified dimensions and player type.
	// Dimensions are given as the number of boxes per row/column, not the actual size of the array.
	// For example, passing in 4x4 gives a total array size of 9x9. In other words, the numbers passed in
	// do not include the dots and lines/spaces.
	public Node(int rows, int cols, Player inType)
	{
		int rDim = (rows * 2) + 1;
		int cDim = (cols * 2) + 1;
		grid = new int[rDim][cDim];
		type = inType;
		changedRow = 0;
		changedCol = 0;
		maxScore = 0;
		maxScore = 0;
		val = 0;
		depth = 0;
		parent = null;
		children = new LinkedList<Node>();
		initialize();
	}
	
	// Copy constructor
	// Creates a node, copying the information from another node.
	// Due to the way the constructor is used, the val, depth, parent, and children variables are not copied.
	public Node(Node inNode)
	{
		grid = new int[inNode.grid.length][inNode.grid[0].length];
		for(int i = 0; i < grid.length; i++)
		{
			for(int j = 0; j < grid[i].length; j++)
			{
				grid[i][j] = inNode.grid[i][j];
			}
		}
		type = inNode.type;
		changedRow = inNode.changedRow;
		changedCol = inNode.changedCol;
		maxScore = inNode.maxScore;
		minScore = inNode.minScore;
		val = 0;
		depth = 0;
		parent = null;
		children = new LinkedList<Node>();
	}
	
	// Getters
	// Used to retrieve private variables that are not meant to be modified.
	// Arguments: none
	// Returns: the value of the private variable
	public int getMaxScore() { return maxScore; }
	public int getMinScore() { return minScore; }
	public int getChangedRow() { return changedRow; }
	public int getChangedCol() { return changedCol; }
	public Player getType() { return type; }
	
	// makeMove
	// Makes the specified move by altering the grid, updating the scores accordingly,
	// and switching to the other player.
	// Arguments:
	//		row: the row of the space to by modified
	//		col: the column of the space to by modified
	// Returns:
	//		An error enum indicating the result:
	// 		SUCCESS if successful, or some other error if it's unable to make the move.
	public Error makeMove(int row, int col)
	{
		if(
				row < 0 ||
				row > grid.length ||
				col < 0 ||
				col > grid[0].length
		) {
			return Error.OUT_OF_BOUNDS;
		}
		
		if(!(row % 2 == 0 ^ col % 2 == 0))
			return Error.INVALID_SPACE;
		
		if(grid[row][col] > 0)
			return Error.SPACE_FILLED;
		
		grid[row][col] = 1;
		changedRow = row;
		changedCol = col;
		updateScore(row, col);
		if(type == Player.MAX)
		{
			type = Player.MIN;
		}
		else
		{
			type = Player.MAX;
		}
		return Error.SUCCESS;
	}
	
	// addChild
	// Adds a child node to the linked list "children" representing a move at the specified location.
	// Arguments:
	//		row: The row of the desired move
	//		col: The column of the desired move
	// Returns:
	//		An error enum representing the result of the move.
	//		If the move is unsuccessful, no child node is added.
	public Error addChild(int row, int col)
	{
		Node child = new Node(this);
		child.depth = depth + 1;
		child.parent = this;
		Error result = child.makeMove(row, col);
		if(result != Error.SUCCESS)
			return result;
		children.add(child);
		return result;
	}
	
	// addChildren
	// Adds child nodes to the children list for all possible moves.
	// Arguments: none
	// Returns: none
	public void addChildren()
	{
		for(int row = 0; row < grid.length; row++)
		{
			if(row % 2 == 0)
			{
				for(int col = 1; col < grid[row].length; col += 2)
				{
					if(grid[row][col] < 1)
						addChild(row, col);
				}
			}
			else
			{
				for(int col = 0; col < grid[row].length; col += 2)
				{
					if(grid[row][col] < 1)
					{
						addChild(row, col);
					}
				}
			}
		}
	}
	
	// eval
	// Sets the val variable equal to maxScore - minScore.
	// Only called on leaf nodes.
	// Arguments: none
	// Returns: none
	public void eval()
	{
		val = maxScore - minScore;
	}
	
	// updateScore
	// Updates the score of the current player by checking the surrounding spaces for complete boxes.
	// Checks boxes above and below horizontal lines, or left and right of vertical lines.
	// Arguments:
	//		row: the row of the last move made
	//		col: the column of the last move made
	// Returns: none
	private void updateScore(int row, int col)
	{
		// Check above and below
		if(row % 2 == 0)
		{
			int up = row - 1;
			int down = row + 1;
			if(isSurrounded(up, col))
			{
				if(type == Player.MAX)
					maxScore += grid[up][col];
				else
					minScore += grid[up][col];
			}
			if(isSurrounded(down, col))
			{
				if(type == Player.MAX)
					maxScore += grid[down][col];
				else
					minScore += grid[down][col];
			}
		}
		// Check left and right
		else
		{
			int left = col - 1;
			int right = col + 1;
			if(isSurrounded(row, left))
			{
				if(type == Player.MAX)
					maxScore += grid[row][left];
				else
					minScore += grid[row][left];
			}
			if(isSurrounded(row, right))
			{
				if(type == Player.MAX)
					maxScore += grid[row][right];
				else
					minScore += grid[row][right];
			}
		}
	}
	
	// isSurrounded
	// Checks if there are lines on all four sides of a given space.
	// Arguments:
	//		row: the row of the space to check
	//		col: the column of the space to check
	// Returns: true if the space is surrounded, or false if it is not surrounded
	private boolean isSurrounded(int row, int col)
	{
		if(
				row > 0 &&
				row < grid.length &&
				col > 0 &&
				col < grid[row].length
		) {
			return grid[row - 1][col] > 0 &&
					grid[row + 1][col] > 0 &&
					grid[row][col - 1] > 0 &&
					grid[row][col + 1] > 0;
		}
		
		return false;
	}
	
	// initialize
	// Initializes every "box" space on the grid to a random number from 1 - 5.
	// Uses the system time to seed the RNG.
	// Arguments: none
	// Returns: none
	private void initialize()
	{
		Random rand = new Random(System.currentTimeMillis());
		for(int row = 0; row < grid.length; row++)
		{
			if(row % 2 == 0)
			{
				for(int col = 0; col < grid[row].length; col++)
					grid[row][col] = 0;
			}
			else
			{
				for(int col = 0; col < grid[row].length; col++)
				{
					if(col % 2 == 0)
						grid[row][col] = 0;
					else
						grid[row][col] = rand.nextInt(5) + 1;
				}
			}
		}
	}
	
	// isOver
	// Checks if any more lines can be drawn
	// Arguments: none
	// Returns: true if it is not possible to draw any more lines, false otherwise
	public boolean isOver()
	{
		for(int row = 0; row < grid.length; row++)
		{
			if(row % 2 == 0)
			{
				for(int col = 1; col < grid[row].length; col += 2)
				{
					if(grid[row][col] == 0)
						return false;
				}
			}
			else
			{
				for(int col = 0; col < grid[row].length; col += 2)
					if(grid[row][col] == 0)
						return false;
			}
		}
		return true;
	}
	
	// printNode
	// Prints the grid and current score
	// Also prints the numbers of the rows and columns,
	// so the player can determine where to make a move.
	// Arguments: none
	// Returns: none
	public void printNode()
	{
		System.out.print(" ");
		for(int col = 0; col < grid[0].length; col++)
		{
			System.out.print(" " + col);
		}
		System.out.print("\n");
		for(int row = 0; row < grid.length; row++)
		{
			System.out.print(row + " ");
			if(row % 2 == 0)
			{
				for(int col = 0; col < grid[row].length; col++)
				{
					if(col % 2 == 0)
						System.out.print("*");
					else if(grid[row][col] > 0)
						System.out.print("───");
					else
						System.out.print("   ");
				}
			}
			else
			{
				for(int col = 0; col < grid[row].length; col++)
				{
					if(col % 2 != 0)
						System.out.print(" " + grid[row][col] + " ");
					else if(grid[row][col] > 0)
						System.out.print("│");
					else
						System.out.print(" ");
				}
			}
			System.out.print("\n");
		}
		System.out.println("-----\nScore\n-----");
		System.out.println("Player: " + minScore);
		System.out.println("Computer: " + maxScore + "\n");
	}
	
	public int gridVal(int row, int col)
	{
		return grid[row][col];
	}
}
