package dataStructures;

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
	
	public int getMaxScore() { return maxScore; }
	public int getMinScore() { return minScore; }
	public int getChangedRow() { return changedRow; }
	public int getChangedCol() { return changedCol; }
	public Player getType() { return type; }
	
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
			type = Player.MIN;
		else
			type = Player.MAX;
		return Error.SUCCESS;
	}
	
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
	
	public void addChildren()
	{
		for(int row = 0; row < grid.length; row++)
		{
			if(row % 2 == 0)
			{
				for(int col = 0; col < grid[row].length; col++)
				{
					if(col % 2 != 0 && grid[row][col] < 1)
					{
						addChild(row, col);
					}
				}
			}
			else
			{
				for(int col = 0; col < grid[row].length; col++)
				{
					if(col % 2 == 0 && grid[row][col] < 1)
					{
						addChild(row, col);
					}
				}
			}
		}
	}
	
	public void eval()
	{
		val = maxScore - minScore;
	}
	
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
						System.out.print("•");
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
}
