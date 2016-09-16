# CogModAPI

CogModAPI is a cognitive modeling testing API based on the [JESS rule engine](http://www.jessrules.com). It provides a simple and easy to use way to test a production rule cognitive model on a given task, provided that all the necessary JESS components are already there (i.e. working memory templates & configuration, the cognitive model itself). As an example, a simple cognitive model for solving simple algebra equations (i.e. the task is to solve an algebraic equation using only subtract and divide), together with all the necessary domain dependent functions and the JESS related structures.

**Note**: Cognitive modeling is a complicated task, and generating a cognitive model requires a series steps, including: 
  * writting code to implement domain dependent functions (e.g. for algebra this may be divide two numbers, figure out if an equation has a coefficient etc),
  * writing the cognitive model, which is a set of production rules that uses the domain depedent functions and describes  how to perform a task (e.g. what to do when an equation is given)
  * preparing whatever is necessary to setup the working memory of the rule engine that will "run" the cognitive model (e.g. templates, initalization etc).

However, thanks to the amazing work by Carnegie Mellon's [CTAT team](http://ctat.pact.cs.cmu.edu/), there are tools that can help you easily create and debug cognitive models (i.e. tha last two bullets). To learn more about JESS and cognitive models, consider the following links:
 * [Jess documentation](http://www.jessrules.com/jess/docs/71/) (you will need this to write the production rules for the cognitive model)
 * [CTAT documentation](http://ctat.pact.cs.cmu.edu/docs/ctat_2_6/) (you will need this to create the necessary Jess templates and working memory configuration)


## Requirements 

This code is written in Java and requires the JESS rule engine. Due to licensing, source code or the JESS library cannot be included in this repo. However, if you do not have JESS, you can still download it from [here](http://www.jessrules.com/jess/download.shtml).

Note: Once downloaded, the code must be packed in a jar file, using the command
```
$ jar cvf jess.jar /path/to/jess/source
```

Once downloaded, edit the build.xml, and set the path to jess.jar in line 9, i.e.
```
<property name="jessJarPath" value="/Users/nbarba/CMU/Tomodachi8-1-7-macfriendly/lib/jess.jar"></property>
```

Thats it, after that the project should be compiling and running without glitches! 

## Usage

### Basics

The project comes with a cognitive model for solving algebra equations, so testing the project is pretty straightforward: all you have to do is compile and run! The main class is ['/src/engine/RuleEngine.java'], so 
```
$ ant   # this will compile the code and fill out the /bin folder with the class files.
$ java -cp ./bin/:/path/to/jess.jar engine/RuleEngine 2x+8=12  #alternatively, type ant -Dequation=2x+8=12 run
```
where [2x+8=12] is the equation you want to solve. You should see something like this in the output: 
```
-----Cognitive Model Testing------
Solving equation 2x+8=12...

Solution on Interface:
 2x+8 =    12 | subtract 8
   2x =     4 | divide 2
    x =     2 |      
Task is Completed!

Solution Graph:
[2x+8=12]
    |.....< subtract : wmeTable_C3R1,subtract 8 ✓ >.....[state1]
    [state1]
        |.....< subtract-typein : wmeTable_C1R2,2x ✓ >.....[state2]
        [state2]
            |.....< subtract-typein : wmeTable_C2R2,4 ✓ >.....[state3]
            [state3]
                |.....< divide : wmeTable_C3R2,divide 2 ✓ >.....[state4]
                [state4]
                    |.....< divide-typein : wmeTable_C1R3,x ✓ >.....[state5]
                    [state5]
                        |.....< divide-typein : wmeTable_C2R3,2 ✓ >.....[state6]
                        [state6]
                            |.....< done : done,-1 ✓ >.....[state7]
```

### Code structure

The code is organized in two packages (the key classes are covered here): 
* *engine package :* contains the generic code for the cognitive model engine:
  * *RuleEngine.java:* a wrapper for the JESS rule engine.
  * *CognitiveTask.java:* an abstract class holding all the info to perfrom a cognitive task, e.g. the cognitive model, the interface (in a String representation).
  * *SolutionGraph.java:* a class to hold the solution (i.e. how the cognitive model performed on the given task).
  * several utility classes to make our life easier when dealing with Jess).
* *algebraDomain package :* contains the domain dependent code for a cognitive model that solves algebraic equations:
  * AlgebraTask: implementation of a CognitiveTask specific for the algebra domain
  * MathUtilities.java: a collection of low level domain dependent methods.
  * *algebraDomain.userDef package:* high level domain depedent classes used by the cognitive model.
  * *wmeTypes.clp:* the working memory templates corresponding to the interface elements we will be using 
  * *init.wme:* the configuration of the working memory elements (e.g. structure, initial values etc)
  * *productionRules.pr:* the actual cognitive model, a list of production rules describing how to solve equations

### Using the API

To begin, you must have the cognitive model. Rule based cognitive models consist of a production rule file (i.e. productionRules.pr), and the working memory configuration (i.e. wmeTypes.clp and init.wme). These latter files define the necessary structures for the domain under study, and can be generated either by hand (if you are experienced), or automatically using [CTAT] (http://ctat.pact.cs.cmu.edu/docs/ctat_2_6/) (shoot me an email at nbarmpalios@gmail.com if you have trouble doing so). Withing the current project, you will find working memory files representing a 3x3 matrix structure, which should be sufficient for several tasks involving 9 textboxes.

Once you have the production rules and working memory files, you need to instantiate a ['CognitiveTask'] for your domain.
```java
public class MyTask extends CognitiveTask{
	
	public MyTask(String taskName) {
		super(taskName);
	}

 @Override
 public HashMap<String, String> computeInitialWMValuesFromTaskName(){
  /* Enter here code that breaks down the task name and returns a hash containing [what workign memory element should be updated]->[updating value]. 
  E.g. for algebra and task name 3x+4=5, we would return [wmeTable_C1R1]->3x+4, [wmeTable_C1R1]->5 */   
 }
 
 @Override
 public String getInitialFactsFile() {
  /*return the init.wme filename
  e.g. return "/somepath/init.wme";
  */
 }

@Override
 public String getWMETypesFile() {
  /*return the wmeTypes.clp filename
  e.g. return "/somepath/wmeTypes.clp";
  */
 }
 
 @Override
 public String getProductionRulesFile() {
  /*return the production rules filename
  e.g. return "/somepath/productionRules.pr";
  */
 }
 
 @Override
 public String getJessUserDefPackageFile() {
  /*return the name of the package containing the user defined functions (that are used in the production rules).
  e.g. return "algebraDomain.userDef";
  */
 }
 
 @Override
 public String getTaskInterfaceLayout() {
		/*Return a String representation of the interface, show we display the in a meaningfull way. Note that $ behind the name of the workign memroy element means this element is expected to be part of the initial state (that defines the problem).
		e.g. return "[$wmeTable_C1R1] = [$wmeTable_C2R1] | [wmeTable_C3R1]\n"
			         + "[wmeTable_C1R2] = [wmeTable_C2R2] | [wmeTable_C3R2]\n"
			         + "[wmeTable_C1R3] = [wmeTable_C2R3] | [wmeTable_C3R3]\n"
			               + "[<doneButton>]";
		*/
	}
 
```
Once this class is implemented, you are done! To test the cognitive model, all you have to do is create an instance of your task, an pass it through an instance of your rule engine, e.g. for the Algebra domain:  

```java

AlgebraTask equationProblem=new AlgebraTask("6=4x+8");
RuleEngine algebraSolver=new RuleEngine(equationProblem);
SolutionGraph solution=algebraSolver.runEngine();

```

The solution graph now contains the solution. To display the solution in the interface, you may use:
```java
System.out.println(equationProblem.getTaskInterface().displaySolution(solution));
```

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details





