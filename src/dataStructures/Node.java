package dataStructures;

import java.util.Random;
import java.util.LinkedList;
import enums.Error;

public class Node {
	private int[][] data;
	private int scoreOne;
	private int scoreTwo;
	public Node parent;
	public LinkedList<Node> children;
	
	public Node()
	{
		data = new int[9][9];
		scoreOne = 0;
		scoreTwo = 0;
		parent = null;
		children = new LinkedList<Node>();
		initialize();
	}
	
	public Node(int dim)
	{
		int xy = (dim * 2) + 1;
		data = new int[xy][xy];
		scoreOne = 0;
		scoreTwo = 0;
		parent = null;
		children = new LinkedList<Node>();
		initialize();
	}
	
	public Node(int rows, int cols)
	{
		data = new int[(rows * 2) + 1][(cols * 2) + 1];
		scoreOne = 0;
		scoreTwo = 0;
		parent = null;
		children = new LinkedList<Node>();
		initialize();
	}
	
	public Node(Node inNode)
	{
		data = new int[inNode.data.length][inNode.data[0].length];
		for(int i = 0; i < data.length; i++)
		{
			for(int j = 0; j < data[0].length; j++)
			{
				data[i][j] = inNode.data[i][j];
			}
		}
		scoreOne = inNode.scoreOne;
		scoreTwo = inNode.scoreTwo;
		parent = null;
		children = new LinkedList<Node>();
	}
	
	public Error copyTo(Node out)
	{
		if(out == null)
			return Error.NULL_NODE;
		out.data = new int[data.length][data[0].length];
		for(int i = 0; i < data.length; i++)
		{
			for(int j = 0; j < data[0].length; j++)
			{
				out.data[i][j] = data[i][j];
			}
		}
		out.scoreOne = scoreOne;
		out.scoreTwo = scoreTwo;
		out.parent = null;
		out.children = new LinkedList<Node>();
		return Error.SUCCESS;
	}
	
	public Error makeMove(int row, int col, int player, Node outNode)
	{
		if(row > data.length || col > data[0].length)
			return Error.OUT_OF_BOUNDS;
		
		if(row % 2 == 0 || col % 2 == 0)
		{
			if(data[row][col] < 1)
			{
				copyTo(outNode);
				outNode.data[row][col] = 1;
				outNode.updateScore(row, col, player);
				return Error.SUCCESS;
			}
			else
			{
				return Error.SPACE_FILLED;
			}
		}
		else
		{
			return Error.INVALID_SPACE;
		}
	}
	
	public int eval()
	{
		return scoreOne - scoreTwo;
	}
	
	public void initialize()
	{
		Random rand = new Random(System.currentTimeMillis());
		for(int row = 0; row < data.length; row ++)
		{
			if(row % 2 == 0)
			{
				for(int col = 0; col < data[row].length; col++)
				{
					data[row][col] = 0;
				}
			}
			else
			{
				for(int col = 0; col < data[row].length; col++)
				{
					if(col % 2 == 0)
					{
						data[row][col] = 0;
					}
					else
					{
						data[row][col] = rand.nextInt(5) + 1;
					}
				}
			}
		}
	}
	
	private void updateScore(int row, int col, int player)
	{
		int score = 0;
		if(row % 2 != 0 || col % 2 != 0)
		{
			if(row % 2 == 0)
			{
				int up = row - 1;
				int down = row + 1;
				if(up > -1 && isSurrounded(up, col))
					score += data[up][col];
				if(down < data.length && isSurrounded(down, col))
					score += data[down][col];
			}
			else
			{
				int left = col - 1;
				int right = col + 1;
				if(left > -1 && isSurrounded(row, left))
					score += data[row][left];
				if(right < data[row].length && isSurrounded(row, right))
					score += data[row][right];
			}
		}
		if(player == 1)
			scoreOne += score;
		if(player == 2)
			scoreTwo += score;
	}
	
	private boolean isSurrounded(int row, int col)
	{
		if(row % 2 == 1 && col % 2 == 1)
		{
			if(
					data[row - 1][col] > 0 &&
					data[row + 1][col] > 0 &&
					data[row][col - 1] > 0 &&
					data[row][col + 1] > 0
			)
				return true;
			else
				return false;
		}
		return false;
	}
	
	public void printNode()
	{
		int rowNum = 0;
		int colNum = 0;
		System.out.print(" ");
		for(int i = 0; i < data[0].length; i++)
		{
			System.out.print(colNum++ + " ");
		}
		System.out.print("\n");
		for(int row = 0; row < data.length; row++)
		{
			System.out.print(String.format("%d", rowNum++));
			if(row % 2 == 0)
			{
				for(int col = 0; col < data[row].length; col++)
				{
					if(col % 2 == 0)
					{
						System.out.print("•");
					}
					else
					{
						if(data[row][col] > 0)
							System.out.print("───");
						else
							System.out.print("   ");
					}
				}
				System.out.print("\n");
			}
			else
			{
				for(int col = 0; col < data[row].length; col++)
				{
					if(col % 2 == 0)
					{
						if(data[row][col] > 0)
							System.out.print("│");
						else
							System.out.print(" ");
					}
					else
					{
						System.out.print(" " + data[row][col] + " ");
					}
				}
				System.out.print("\n");
			}
		}
		System.out.println("Player 1 Score: " + scoreOne);
		System.out.println("Player 2 Score: " + scoreTwo);
	}
}
