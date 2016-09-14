package engine;

import java.util.ArrayList;

/**
 * Class to hold the solution (i.e. all the SAI's the cognitive model suggested for 
 * each step. 
 * 
 * @author Nikolaos Barmpalios {nbarmpalios@gmail.com}
 *
 */
public class SolutionGraph {
	public static String STEP_CORRECT="Correct";
	public static String STEP_INCORRECT="Incorrect";
	
	Node rootNode;
	int nodeCounter=0;
	Node currentNode=null;
	
	/**
	 * Constructor 
	 * @param name the name of the starting node 
	 */
	public SolutionGraph(String name){	
		rootNode=new Node(name);
		currentNode=rootNode;
	}
	
	/**
	 * Method that prints the graph.
	 * Internally calls the recursive method {@link printGraphFromNode}.
	 */
	public void printGraph(){
		this.printGraphFromNode(rootNode, "");
	}
	
	/**
	 * Low level method to recursively prints the graph. 
	 * @param root
	 * @param intent
	 */
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
	
	/**
	 * Method that returns all the edges of the graph (i.e. all the solution steps).
	 * Internally calls the recursive method {@link 
	 * @return  an array list of all the edges
	 */
	public ArrayList<Edge> getEdges(){
		ArrayList<Edge> list = new ArrayList<Edge>();
		return getGraphEdges(rootNode,list);
	}
	
	/**
	 * Low level method that recursively retrieves all the edges of the graph. 
	 * @param root
	 * @param list
	 * @return
	 */
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

	/**
	 * Method to add an edge to the graph. Each edge in the graph corresponds to a
	 * solution step, which contains an Sai, the correctness of that Sai, and the name
	 * of the rule that fired and suggested that Sai. 
	 * Note: Correctness has been added to make the edge more complete and account for
	 * cases not supported by the current engine at this time (e.g. you may want to evaluate 
	 * the steps against a human expert OR even another cognitive model, which is usually the
	 * case in tutoring systems). In the current version, correctness is always "Correct".
	 * @param sai: the SAI the rule suggested. 
	 * @param correctness: String indicating if the Sai is correct at this step. In the current version, 
	 *                    it is always set to "Correct".
	 * @param info: the name of the rule that fired and suggested that step
	 */
	public void addEdge(Sai sai,String correctness,String info){
		nodeCounter++;
		Node newDest=new Node("state"+nodeCounter);
		Edge newEdge=new Edge(sai,correctness,newDest,currentNode,info);
		currentNode.connectingEdges.add(newEdge);
		if (correctness.equals(SolutionGraph.STEP_CORRECT))
			currentNode=newDest;
	}
	
	/**
	 * Class representing the edge of the solution graph. 
	 *
	 */
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
			String correcntessMark=correctness.equals(SolutionGraph.STEP_CORRECT)?CHECK_MARK:ERROR_MARK;
			return "< "+info+" : "+edgeSai.getSelection()+","+edgeSai.getInput()+" "+ correcntessMark +" >";
		}
		
	}
	/**
	 * Class representing a node of the solution graph.
	 *
	 */
	private class Node{
		ArrayList<Edge> connectingEdges=new ArrayList<Edge>();
		

		String name;
		public Node(String name){
			this.name=name;
		}
		
	}
}
