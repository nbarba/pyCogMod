package algebraDomain;

import java.util.HashMap;
import java.util.Vector;

import engine.CognitiveTask;
import engine.Sai;

/**
 * Implementation of a cognitive task for the algebra domain, where the task is to solve 
 * simple one-step/two-step equations. 
 * The following domain dependent (i.e. algebra) elements must be defined: 
 * 1. A string representation of a typical interface that can be used to perform the task (i.e. interfaceLayout).
 * 2. A method to break down the name of the problem into the parts that need to update the working memory for 
 * its initial state. 
 * 
 * @author Nikolaos Barmpalios {nbarmpalios@gmail.com}
 *
 */
public class AlgebraTask extends CognitiveTask{
	
	public AlgebraTask(String taskName) {
		super(taskName);
	}

	String wmeTypesFile="./src/algebraDomain/wmeTypes.clp";
	String initialFactsFile="./src/algebraDomain/init.wme";
	String productionRulesFile="./src/algebraDomain/productionRules.pr";
	String userDefJessPackage="algebraDomain.userDef";
	
	/**
	 * Typical interface used in the algebra domain. Represents a 3x3 matrix (where the problem is solved)
	 * with a button (to notify that the problem is solved). i.e.
	 *     RHS        LHS      TRANSFORMATION
	 *   [      ] = [      ]  [              ]
	 *   [      ] = [      ]  [              ]
	 *   [      ] = [      ]  [              ]
	 *   
	 *          [DONE BUTTON]
	 * Two things must be noted: 
	 * - the '$' symbol signifies that these elements will be used to define the problem on the 
	 * - interface element names enclosed in < > signify a button.        
	 */
	static String interfaceLayout=
			   "[$wmeTable_C1R1] = [$wmeTable_C2R1] | [wmeTable_C3R1]\n"
			 + "[wmeTable_C1R2] = [wmeTable_C2R2] | [wmeTable_C3R2]\n"
			 + "[wmeTable_C1R3] = [wmeTable_C2R3] | [wmeTable_C3R3]\n"
			 + "[<doneButton>]";
	
	
	@Override
	/**
	 * Method that returns a hashmap of the working memory elements that need to be updated to define the problem
	 * E.g. in this example, for an equation 3x+4=5, the hashmap would have the values
	 * key:dorminTable1_C1R1 --> value:3x+4
	 * key:dorminTable1_C2R2 --> value:5
	 */
	public HashMap<String, String> computeInitialWMValuesFromTaskName() {
		String[] problem=getTaskName().split("=");

		if (!problem[0].isEmpty()){
			for (int i=0;i<problem.length;i++)
				initWMState.put(getInterfaceInitialElements().get(i), problem[i]);			
		}	
		return initWMState;
	}

	
	@Override
	public String getInitialFactsFile() {
		// TODO Auto-generated method stub
		return this.initialFactsFile;
	}

	@Override
	public String getWMETypesFile() {
		// TODO Auto-generated method stub
		return this.wmeTypesFile;
	}

	@Override
	public String getProductionRulesFile() {
		// TODO Auto-generated method stub
		return this.productionRulesFile;
	}

	@Override
	public String getJessUserDefPackageFile() {
		// TODO Auto-generated method stub
		return this.userDefJessPackage;
	}

	@Override
	public String getTaskInterfaceLayout() {
		// TODO Auto-generated method stub
		return this.interfaceLayout;
	}


	@Override
	public String getFinalResult(Vector<Sai> solution) {
		int ind=0;
		for (Sai s: solution){
			if (s.getSelection().equalsIgnoreCase(CognitiveTask.TaskInterface.TASK_COMPLETED_BUTTON_NAME))
			break;
			
			ind++;
		}
		

		return ((Sai) solution.get(ind-3)).getInput()+"="+((Sai) solution.get(ind-2)).getInput();
	}



	
}
