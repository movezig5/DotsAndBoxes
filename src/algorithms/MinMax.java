package algorithms;

import java.util.LinkedList;
import dataStructures.Node;
import enums.NodeType;

public class MinMax {
	public static Node move(Node inNode, int plys)
	{
		Node root = new Node(inNode);
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.add(root);
		Node currNode = queue.pop();
		currNode.getChildren();
		while(currNode.depth <= plys)
		{
			for(Node n : currNode.children)
				queue.add(n);
			currNode = queue.pop();
			currNode.getChildren();
		}
		queue.push(currNode);
		LinkedList<Node> parents = new LinkedList<Node>();
		while(queue.getFirst().depth != 0)
		{
			for(Node n : queue)
			{
				n.eval();
				if(!parents.contains(currNode.parent))
					parents.add(currNode.parent);
			}
			
			for(Node n : parents)
			{
				n.value = n.children.getFirst().value;
				if(n.type == NodeType.MAX)
				{
					for(Node o : n.children)
					{
						if(o.value > n.value)
							n.value = o.value;
					}
				}
				else
				{
					for(Node o : n.children)
					{
						if(o.value < n.value)
							n.value = o.value;
					}
				}
			}
			queue = parents;
			parents = new LinkedList<Node>();
		}
		root = queue.getFirst();
		Node result = root.children.getFirst();
		if(root.type == NodeType.MAX)
		{
			for(Node n : root.children)
			{
				if(n.value > result.value)
					result = n;
			}
		}
		else
		{
			for(Node n : root.children)
			{
				if(n.value < result.value)
					result = n;
			}
		}
		
		return result;
	}
}
