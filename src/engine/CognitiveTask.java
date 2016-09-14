package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import engine.Sai;

/**
 * Class that holds all the necessary information the rule engine needs to perform a cognitive task. 
 * Such information include the working memory configuration and the task interface (i.e. a typical 
 * interface necessary to perform the task). 
 * 
 * @author Nikolaos Barmpalios {nbarmpalios@gmail.com}
 *
 */
public abstract class CognitiveTask {
	
	public static String TASK_COMPLETED_RULE_NAME="Done";

	/*A string representing the problem we are trying to solve*/
	String taskName=null;
	private void setTaskName(String str) { this.taskName=str;}
	protected String getTaskName() { return taskName;}
		

	/*A typical interface that could be used to perform the task (in String format)*/
	TaskInterface theInterface=null;
	public TaskInterface getTaskInterface(){return theInterface;}
	
	/*Force child class to properly set these four files*/
	abstract public String getInitialFactsFile();	
	abstract public String getWMETypesFile();
	abstract public String getProductionRulesFile();
	abstract public String getJessUserDefPackageFile();
	abstract public String getTaskInterfaceLayout();
	abstract public String getFinalResult(Vector<Sai> solution);

	
	/*Domain dependent problem that breaks down the task name and figures out
	 * what WME's should be initially updated.*/
	abstract public HashMap<String, String> computeInitialWMValuesFromTaskName();
	
	/*HashMap <wme,value> to store how the working memory should be initially updated*/
	protected HashMap<String, String> initWMState=new HashMap<String,String>();
	public HashMap<String, String> getInitWMState(){return this.initWMState;}
	
	
	public CognitiveTask(String taskName){
		setTaskName(taskName);
		theInterface=new TaskInterface(this.getTaskInterfaceLayout());
		computeInitialWMValuesFromTaskName();
		
	}
	
	/*Used when we want to solve another problem*/
	public void updateTaskName(String newTaskName){
		setTaskName(newTaskName);
		computeInitialWMValuesFromTaskName();
	}
	
	
	public ArrayList<String> getInterfaceInitialElements(){
		return getTaskInterface().getInitialStateElements();
	}
	
	
	/**
	 * Class to hold the interface used to perform the cognitive task
	 * @author nbarba
	 *
	 */
	protected class TaskInterface{
		
		public static final String TASK_COMPLETED_BUTTON_NAME="doneButton";
		public String TASK_COMPLETED_INTF_NAME="Task is Completed!";

		
		String interfaceLayout;
		void setInterfaceLayout(String str){this.interfaceLayout=str;}
		String getInterfaceLayout(){return interfaceLayout;}
		
		/*All the elements that define the task on the interface.*/
		protected ArrayList<String> initialStateElements=new ArrayList<String>();
		public ArrayList<String> getInitialStateElements(){return this.initialStateElements;}
		
		/*All the elements of the interface*/
		protected ArrayList<String> allElements=new ArrayList<String>();	

		
		public TaskInterface(String layout){
			this.interfaceLayout=layout;
			computeInitialWMInterfaceElements();
		}
		
				
		/**
		 * Method to analyze the interface and estimate the elements that should be updated
		 * for the initial state. 
		 * Updates the {@link initialStateWMEs}.
		 */
		protected void computeInitialWMInterfaceElements(){
			Pattern p = Pattern.compile("([<?$?a-z])\\w+");
			Matcher m = p.matcher(this.getInterfaceLayout());
	    	while (m.find()) {	
	    		allElements.add(m.group().substring(0, m.group().length()));
	    		if (m.group().startsWith("$"))
	    			this.initialStateElements.add(m.group().substring(1, m.group().length()));
	    	}

		}
		
		
		/**
		 * Method that formats the solution on the interface
		 * @param solution
		 * @return
		 */
		protected String displaySolution(SolutionGraph solution){
			String formatedSolution=interfaceLayout;
			formatedSolution=formatedSolution.replace("$", "");
			formatedSolution=formatedSolution.replace("[", "");
			formatedSolution=formatedSolution.replace("]", "");
			formatedSolution=formatedSolution.replace("<", "");
			formatedSolution=formatedSolution.replace(">", "");
			
			/*Display the initial elements (that define the task)*/
			for (Entry<String, String> entry : initWMState.entrySet()) {
				formatedSolution=formatedSolution.replace(entry.getKey(), String.format("%5s", entry.getValue()));
			}
			ArrayList<engine.SolutionGraph.Edge> solutionEdges=solution.getEdges();
			/*Display the solution*/
			for (engine.SolutionGraph.Edge edge:solutionEdges){
				
				if (edge.edgeSai.getSelection().equalsIgnoreCase(CognitiveTask.TASK_COMPLETED_RULE_NAME))
					formatedSolution=formatedSolution.replace(this.TASK_COMPLETED_BUTTON_NAME, String.format("%5s", TASK_COMPLETED_INTF_NAME));					
				else
					formatedSolution=formatedSolution.replace(edge.edgeSai.getSelection(), String.format("%5s", edge.edgeSai.getInput()));
				
			
			}
			
			/*Clear everything else*/
			for (String element: allElements ){		
				formatedSolution=formatedSolution.replace(element.replace("<", ""), String.format("%5s", ""));
			}
			
			
			
			return formatedSolution;		
		}
	}
}
