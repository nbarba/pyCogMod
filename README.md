# LightweightRuleEngine

A cognitive model testing API based on the [JESS rule engine](http://www.jessrules.com). It provides a simple and easy to use way to test a cognitive model on a given task, provided that all the necessary JESS components are already there (i.e. working memory templates & configuration, the cognitive model itself). As an example, a simple cognitive model for solving simple algebra equations is provided, together with all the necessary domain functions and the JESS related structures.

**Note**: Cognitive modeling is a complicated task, and generating a cognitive model requires a sequence of complicated steps. However, thanks to the amazing work by Carnegie Mellon's [CTAT team](http://ctat.pact.cs.cmu.edu/), there are tools that can help you easily create and debug cognitive models. To learn more about JESS and cognitive models, consider the following links:

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

The project comes with a cognitive model for solving algebra equations, so testing the project is pretty straightforward: compile and run! The main class is ['/src/engine/RuleEngine.java'], so 
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

The code is organized in two packages: 
* *engine package :* contains the generic Java code for the cognitive model engine.
* *algebraDomain package :* contains the domain dependent Java code, regarding a cognitive model for the algebra domain.
  * *algebraDomain.userDef package:* contains the java functions that are used by the rules of the cognitive model (e.g. divide two numbers, does an equation have a coefficient etc).
  * *wmeTypes.clp:* the working memory templates corresponding to the interface elements we will be using 
  * *init.wme:* the configuration of the working memory elements (e.g. structure, initial values etc)
  * *productionRules.pr:* the actual cognitive model, a list of production rules describing how to solve equations

### Using the API in your own domain

To create the 


## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details





