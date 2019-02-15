package algorithms;

import java.util.LinkedList;
import dataStructures.Node;
import enums.Player;

public class MinMax {
	public static int[] makeMove(Node inNode, int plys)
	{
		int[] coords = {0, 0};
		Node root = new Node(inNode);
		LinkedList<Node> queue = new LinkedList<Node>();
		LinkedList<Node> children = new LinkedList<Node>();
		Node currNode;
		queue.push(root);
		while(!queue.isEmpty())
		{
			currNode = queue.pop();
			if(currNode.depth < plys)
			{
				currNode.addChildren();
				for(Node n : currNode.children)
					queue.add(n);
			}
			else
			{
				children.add(currNode);
			}
		}
		for(Node n : children)
		{
			n.eval();
		}
		
		LinkedList<Node> parents = new LinkedList<Node>();
		
		if(children.isEmpty())
			children.push(root);
		
		while(children.getFirst() != root)
		{
			for(Node n : children)
			{
				if(!parents.contains(n.parent))
					parents.add(n.parent);
				
				if(n.getType() == Player.MIN && n.val > n.parent.val)
					n.parent.val = n.val;
				if(n.getType() == Player.MAX && n.val < n.parent.val)
					n.parent.val = n.val;
				
				children.clear();
				children.addAll(parents);
				parents.clear();
			}
		}
		
		if(!root.children.isEmpty())
		{
			coords[0] = root.children.getFirst().getChangedRow();
			coords[1] = root.children.getFirst().getChangedCol();
		}
		
		for(Node n : root.children)
		{
			if(n.val > root.val)
			{
				root.val = n.val;
				coords[0] = n.getChangedRow();
				coords[1] = n.getChangedCol();
			}
		}
		
		return coords;
	}
}
