package algebraDomain.userDef;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import algebraDomain.MathUtilities;
import engine.UserDefinedFunction;
import jess.Context;
import jess.JessException;
import jess.RU;
import jess.Userfunction;
import jess.Value;
import jess.ValueVector;


public class SubTerm extends UserDefinedFunction {

    public SubTerm() {
        setName("sub-term");
    }

	public String apply(Vector args, Context context) {
		String str=(String)args.get(0);
		   
    	String term1str = (String)args.get(0);
		String term2str = (String)args.get(1);
		String variableTerm="";

		int term1;
		if (MathUtilities.isANumber(term1str))
			term1=Integer.parseInt(term1str);
		else{

			String tmp=MathUtilities.getConstantTerm(term1str);
			term1=Integer.parseInt(tmp);
			variableTerm=MathUtilities.getVariableTerm(term1str);
		}
		
		int term2;
		if (MathUtilities.isANumber(term2str))
			term2=Integer.parseInt(term2str);
		else{
			String tmp=MathUtilities.getConstantTerm(term2str);
			term2=Integer.parseInt(tmp);
			variableTerm=MathUtilities.getVariableTerm(term2str);
		}
		
		float res= term1 - term2;

		String returnValue=null;
				
		if (res==0){
			returnValue=variableTerm;
		}
		else {
			returnValue = variableTerm+""+String.format("%.0f", res);
			
		}
  	 
		return returnValue;
	}
}
