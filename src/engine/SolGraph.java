package engine;

import java.util.ArrayList;

public class SolGraph {
	public static String STEP_CORRECT="Correct";
	public static String STEP_INCORRECT="Incorrect";
	
	Node rootNode;
	int nodeCounter=0;
	Node currentNode=null;
	
	public SolGraph(String name){	
		rootNode=new Node(name);
		currentNode=rootNode;
	}
	
	
	public void printGraph(){
		this.printGraphFromNode(rootNode, "");
	}
	
	public void printGraphFromNode(Node root,String intent){
		
		if (root==null || root.connectingEdges.size()<1)
			return;
		
		System.out.println(intent+"["+root.name+"]");
		intent+="    ";
		for (int i=0;i<root.connectingEdges.size();i++){
			Edge tmpEdge=root.connectingEdges.get(i);
			System.out.println(intent+"|....."+tmpEdge+".....["+tmpEdge.destinationNode.name+"]");
			printGraphFromNode(tmpEdge.destinationNode,intent);
		}
		
	}
	
	public ArrayList<Edge> getEdges(){
		ArrayList<Edge> list = new ArrayList<Edge>();
		return getGraphEdges(rootNode,list);
	}
	
	public ArrayList<Edge> getGraphEdges(Node root,ArrayList<Edge> list){
		
		if (root==null || root.connectingEdges.size()<1)
			return list;
		
		for (int i=0;i<root.connectingEdges.size();i++){
			Edge tmpEdge=root.connectingEdges.get(i);
			if (tmpEdge.correctness.equals(STEP_CORRECT))
				list.add(tmpEdge);
			getGraphEdges(tmpEdge.destinationNode,list);
		}
		return list;
	}

	public void addEdge(Sai sai,String correctness,String info){
		nodeCounter++;
		Node newDest=new Node("state"+nodeCounter);
		Edge newEdge=new Edge(sai,correctness,newDest,currentNode,info);
		currentNode.connectingEdges.add(newEdge);
		if (correctness.equals(SolGraph.STEP_CORRECT))
			currentNode=newDest;
	}
	
	class Edge{
		Node parentNode;
		Node destinationNode;
		Sai edgeSai;
		String correctness;
		String info;
		
		public Edge(Sai sai,String corr,Node destination,Node parent,String info){
			this.destinationNode=destination;
			this.edgeSai=sai;
			this.correctness=corr;
			this.parentNode=parent;
			this.info=info.replace("MAIN::", "");
		}
		private static final String CHECK_MARK = "\u2713";
		private static final String ERROR_MARK = "\u2717";

		public String toString(){
			String correcntessMark=correctness.equals(SolGraph.STEP_CORRECT)?CHECK_MARK:ERROR_MARK;
			return "<"+info+" : "+edgeSai.getSelection()+","+edgeSai.getInput()+" "+ correcntessMark +" >";
		}
		
	}
	
	private class Node{
		ArrayList<Edge> connectingEdges=new ArrayList<Edge>();
		

		String name;
		public Node(String name){
			this.name=name;
		}
		
	}
}
