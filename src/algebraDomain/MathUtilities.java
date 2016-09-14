package algebraDomain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Collection of methods that perform basic algebraic operations, using regular expressions. 
 * Note that this is domain dependent code, so code has to be written for each new domain
 * you write a cognitive model for. 
 *
 * @author Nikolaos Barmpalios {nbarmpalios@gmail.com}
 *
 */
public class MathUtilities {

	/**
	 * Method detecting if a string contains an expression.
	 * @param str
	 * @param regex
	 * @return
	 */
	public static boolean containsRegularExpression(String str, String regex){
		boolean returnValue=false;
		
		Pattern p = Pattern.compile(regex);
    	Matcher m = p.matcher(str);
    	if (m.find()){	
    		returnValue=true;
    	}
    	
    	
		
		return returnValue;
	}
	
	/**
	 * Method that extracts the skill from a transformation (e.g. for the transformation "subtract 3"
	 * this method returns "subtract"
	 * @param str
	 * @param skillQuery
	 * @return
	 */
	public static boolean extractSkillFromTransformation(String str,String skillQuery){
		boolean returnValue=false;
		String skill =  str.split(" ")[0];

		if (skill != null && skill.equals(skillQuery)) {
			returnValue = true;
		}

		return returnValue;
	}
	
	/**
	 * Method that detects if an equation has a constant term.
	 * @param str
	 * @return
	 */
	public static boolean hasConstantTerm(String str){
		 boolean returnValue=false;
	    	
	    	Pattern p1 = Pattern.compile("[+-]?(\\d+)?\\d?+[a-z]");
	    	Matcher m1 = p1.matcher(str);
	    	if (m1.find()){	
	    		String variableTerm=m1.group();
				String constantTerm=str.replace(variableTerm, "");
				if (constantTerm.length()!=0) 
					returnValue=true;
				
	    	}
	    	else{
	    		
	    		p1 = Pattern.compile("[+-]?(\\d+)$");
		    	Matcher m2 = p1.matcher(str);
		    	if (m2.find()){	
					returnValue=true;
		    	}
	    	}
	      	
	    	
	       	return returnValue;
		
	}
	
	
	/**
	 * Method that uses regular expressions to detect if a string contains a number
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isANumber(String str){
    	return containsRegularExpression(str,"^[-]?\\d+\\/\\d+$|^[-]?\\d+$");
    	
	}
	
	/**
	 * Method returning the match between a string and a regular expression.
	 * @param str
	 * @param regex
	 * @return
	 */
	public static String getRegularExpressionMatch(String str, String regex){
		String returnValue=null;
		Pattern p = Pattern.compile(regex);
    	Matcher m = p.matcher(str);
    	if (m.find()){	
    		returnValue=m.group();
    	}
		
    	return returnValue;
	}

	/**
	 * Method to extract the variable term of an equation (represented as a string).
	 * @param str
	 * @return
	 */
	public static String getVariableTerm(String str){	
			String returnValue=getRegularExpressionMatch(str,"[+-]?(\\d+)?\\d?+[a-z]");
	    	returnValue=returnValue.replace("+", "");
	    	return returnValue;
	}
	
	
	/**
	 * Method to extract the coefficient of an equation (represented as a string).
	 * @param str
	 * @return
	 */
	public static String getCoefficient(String str){	
		String returnValue=getRegularExpressionMatch(str,"[-]?\\d+[a-z]");
		returnValue = returnValue.substring(0, returnValue.length()-1);
    	return returnValue;
	}
	
	/**
	 * Method that uses regular expressions to extract the constant term of a polynomial 
	 * @param str
	 * @return
	 */
	public static String getConstantTerm(String str){
	   
	    	String constantTerm=null;
	    	Pattern p1 = Pattern.compile("[+-]?(\\d+)?\\d?+[a-z]");
	    	Matcher m1 = p1.matcher(str);
	    	if (m1.find()){	
	    		
	    		String variableTerm=m1.group();
				String tmp=str.replace(variableTerm, "");
				if (tmp.length()!=0) {
					constantTerm=tmp;
				}
	    	}
	    	else{
	    		
	    		p1 = Pattern.compile("[+-]?(\\d+)$");
		    	Matcher m2 = p1.matcher(str);
		    	if (m2.find()){	
					constantTerm=m2.group();
		    	}
	    	}

	    	constantTerm=constantTerm.replace("+", "");
	    	
	    	return constantTerm;
	}
	
	
	
}
