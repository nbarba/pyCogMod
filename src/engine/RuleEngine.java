package engine;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileManager.Location;

import algebraDomain.AlgebraTask;

import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import jess.*;
import engine.Sai;

/**
 * Class that implements a JESS-based lightweight rule engine. Basically its a JESS wrapper for 
 * researchers to easily evaluate cognitive models without having to dive into JESS. 
 *  
 * @author Nikolaos Barmpalios {nbarmpalios@gmail.com}
 * 
 */
public class RuleEngine {
		
		Rete reteEngine=null;
		void setReteEngine(Rete rete){this.reteEngine=rete;}
		Rete getReteEngine(){return reteEngine;}
			
		String productionRulesFile=null;
		void setProductionRuleFilename(String filename){this.productionRulesFile=filename;}
		String getProductionRulesFilename(){return this.productionRulesFile;}
			
		String userDefPackage=null;
		void setUserDefPackage(String packageName){userDefPackage=packageName;}
		String getUserDefPackage(){return this.userDefPackage;}
		
		SolutionGraph solutionGraph;
		String tempTaskName;
		
	    /**
	     * Constructor 
	     * @param task
	     * @throws JessException
	     * @throws IOException
	     * @throws ClassNotFoundException
	     * @throws InstantiationException
	     * @throws IllegalAccessException
	     */
		public RuleEngine(CognitiveTask task) throws JessException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{		
			setProductionRuleFilename(task.getProductionRulesFile());
			setUserDefPackage(task.getJessUserDefPackageFile());
			reteEngine=new Rete();
			initReteEngine(productionRulesFile,task.getWMETypesFile(),task.getInitialFactsFile(),getUserDefPackage());
			initWorkingMemory(task.getInitWMState());	
			tempTaskName=task.getTaskName();

		}
				
			
		/**
		 * Method to prepares rule engine for a new task
		 * @param task
		 * @throws JessException
		 * @throws IOException 
		 * @throws IllegalAccessException 
		 * @throws InstantiationException 
		 * @throws ClassNotFoundException 
		 */
		public void resetRuleEngine(CognitiveTask task) throws JessException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
			this.reteEngine.reset();
			initReteEngine(this.getProductionRulesFilename(),task.getWMETypesFile(),task.getInitialFactsFile(),this.getUserDefPackage());
			initWorkingMemory(task.getInitWMState());
			tempTaskName=task.getTaskName();
		}
		
				
		/**
		 * Method to initialize the rete engine (i.e. define the types, initial the working memory, load the 
		 * production rules and the user defined functions) 
		 * @param productionRulesFile
		 * @param wmeTypesFile
		 * @param initialFactsFile
		 * @throws JessException
		 * @throws IOException 
		 * @throws IllegalAccessException 
		 * @throws InstantiationException 
		 * @throws ClassNotFoundException 
		 */
		private void initReteEngine(String productionRulesFile,String wmeTypesFile,String initialFactsFile,String userDefPackage) throws JessException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{

			reteEngine.batch(wmeTypesFile);
			reteEngine.batch(initialFactsFile);	
			reteEngine.batch(productionRulesFile);
			
			loadUserFunctions(userDefPackage);
		}
					
		/**
		 * Method that loads user defined jess functions, used in the LHS and RHS of the production rules.
		 * @throws IOException 
		 * @throws ClassNotFoundException 
		 * @throws IllegalAccessException 
		 * @throws InstantiationException 
		 */
		private void loadUserFunctions(String packageName) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException{
			
			reteEngine.addUserfunction( new UpdateJessContext());
			if (this.getUserDefPackage().equals("")) return;
			
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

			StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
						
			Location location = StandardLocation.CLASS_PATH;
			Set<JavaFileObject.Kind> kinds = new HashSet<JavaFileObject.Kind>();
			kinds.add(JavaFileObject.Kind.CLASS);
			boolean recurse = false;
			
			Iterable<JavaFileObject> list = fileManager.list(location, packageName,kinds, recurse);
			for (JavaFileObject javaFileObject : list) {
				
				String className=javaFileObject.getName();
				className=className.substring(className.lastIndexOf("/")+1, className.length()-6); //6 to remove the ".class" part

				Class userClass = Class.forName(packageName+"."+className);
				reteEngine.addUserfunction((Userfunction) userClass.newInstance());
				
			}
			
		}
		
		/**
		 * Method invoked to update the working memory while solving a problem
		 * @param initwm
		 * @throws JessException
		 */
		public void initWorkingMemory(HashMap<String,String> initwm) throws JessException{
							
			for (Entry<String, String> entry : initwm.entrySet()) {
				String key = entry.getKey();
			    String value = entry.getValue();
				this.updateWorkingMemory(key, value);				
			}
		}
		
		
		/***
		 * Method that invokes the rete to solve the problem
		 * @return
		 * @throws JessExceptionY
		 * @throws IOException 
		 */
		public SolutionGraph runEngine() throws JessException, IOException{			
			String tmpSelection="";		
			solutionGraph=new SolutionGraph(this.tempTaskName);
	        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

			 /*loop until problem solved or no rule fires (i.e. engine does not know how to proceed) */
			 while (!tmpSelection.equalsIgnoreCase(CognitiveTask.TASK_COMPLETED_RULE_NAME)){
				String firingRule=getAgendaTopActivation();
								
				/*fire the first rule in the agenda*/
				int numRules=reteEngine.run(1);				
				if (numRules==0) break; //if no rule fires, break
				
				/*get the output of the rule (i.e. the selection, action, input) */
				Value sai_value=reteEngine.eval("?*sSai*");
				Sai step_sai=(Sai) sai_value.javaObjectValue(reteEngine.getGlobalContext());

				tmpSelection=step_sai.getSelection(); 

				solutionGraph.addEdge(step_sai, SolutionGraph.STEP_CORRECT,firingRule);

			}		    
			return solutionGraph;
		}
		

		/**
		 * Method to update the working memory of the reteEngine.
		 * @param element
		 * @param newValue
		 * @return
		 * @throws JessException
		 */
		public boolean updateWorkingMemory(String element, String newValue) throws JessException{
		
			Fact fact = getFact(element);
			if (fact==null) return false;
			reteEngine.modify(fact, "value", new Value(newValue,RU.STRING));

			return true;	 
		}

		/**
		 * Method to get a specific fact specified by the element
		 * @param element
		 * @return
		 * @throws JessException
		 */
		public Fact getFact(String factName) throws JessException{
			Fact fact = null;
			Iterator it= reteEngine.listFacts();	
			/*Loop through all facts, locate the one with factName */
			while(it.hasNext()){
				fact = (Fact) it.next();
				if (fact.getDeftemplate().getSlotIndex("name") != -1) {			
					Value val = fact.getSlotValue("name");						
					if (val.stringValue(null).trim().equalsIgnoreCase(factName)) { 
						break;
					}
				}
				fact = null;	 
			}
			return fact;
		}
		
		
		/**
		 * Method that returns the rule which is about to fire (i.e. the first rule in the agenda)
		 * @return
		 */
		public String getAgendaTopActivation(){

			String returnValue="";
			Iterator it = reteEngine.listActivations();
			Activation topActivation;
			if (it.hasNext()){
				topActivation= (Activation) it.next();
				
				returnValue= topActivation.getRule().getName();
			}
			return returnValue;

		}
		
		/**
		 * Method that returns a list of all the facts (usefull for debugging puprposes)
		 * @return
		 */
		public ArrayList getReteFacts() {
			ArrayList facts = new ArrayList();
			for(Iterator it = this.reteEngine.listFacts(); it.hasNext();) {
				facts.add(it.next());
			}
			return facts;
		}
		

		
		public static void main(String[] args) throws Exception {

						
			/*Create a new algebra task */
			AlgebraTask equationProblem=new AlgebraTask("-3x+2=14");
			/*Create an instance of the rule engine*/
			RuleEngine algebraSolver=new RuleEngine(equationProblem);
			/*Solve...*/
			System.out.println("Solving problem "+equationProblem.getTaskName()+"...");
			SolutionGraph solution=algebraSolver.runEngine();
			System.out.println("Solution on Interface:");
			System.out.println(equationProblem.getTaskInterface().displaySolution(solution));
			System.out.println("\nSolution Graph:");
			solution.printGraph();
			
			/*Refresh the task for a new problem*/
			equationProblem.updateTaskName("x+3=5");
			algebraSolver.resetRuleEngine(equationProblem);
			solution=algebraSolver.runEngine();
			System.out.println("Solution on Interface:");
			System.out.println(equationProblem.getTaskInterface().displaySolution(solution));
			System.out.println("\nSolution Graph:");
			solution.printGraph();	
			
					 	
		}
		
		
		
}
