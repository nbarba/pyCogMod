# LightweightRuleEngine

A cognitive model testing API based on the [JESS rule engine](http://www.jessrules.com). It provides a simple and easy to use way to test a cognitive model on a given task, without having to deal with JESS  

# Contents 


# Requirements 

This code is written in Java and requires the JESS rule engine. Due to licensing, source code or the JESS library cannot be included in this repo. However, if you do not have JESS, you can still download it from [here](http://www.jessrules.com/jess/download.shtml).

Note: Once downloaded, the code must be packed in a jar file, using the command
```
jar cvf jess.jar /path/to/jess/source
```

Once downloaded, you should edit the build.xml, and set the path to jess.jar in line 9, i.e.
```
<property name="jessJarPath" value="/Users/nbarba/CMU/Tomodachi8-1-7-macfriendly/lib/jess.jar"></property>
```

# Usage 
