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


public class DivTerm extends UserDefinedFunction {

    public DivTerm() {
        setName("div-term");
    }

	public String apply(Vector args, Context context) {
		String str=(String)args.get(0);
		   
    	String term1str = (String)args.get(0);
		String term2str = (String)args.get(1);
		String variable="";
		
		int term1;
		if (MathUtilities.isANumber(term1str))
			term1=Integer.parseInt(term1str);
		else{

			String tmp=MathUtilities.getCoefficient(term1str);
			term1=Integer.parseInt(tmp);
			variable=term1str.replace(tmp, "");
		}
		int term2;
		if (MathUtilities.isANumber(term2str))
			term2=Integer.parseInt(term2str);
		else{
			String tmp=MathUtilities.getCoefficient(term2str);
			term2=Integer.parseInt(tmp);
			variable=term2str.replace(tmp, "");
		}
				

		float res=(float) term1/(float) term2;
		
		String returnValue=null;
				
		if(res % 1 != 0) //don't display decimals, display term1/term2 instead
			returnValue= term1str+"/"+term2str+""+variable;
		else if (res==1.0){
			returnValue=variable;
		}
		else {
			returnValue=String.format("%.0f", res)+""+variable;
		}
  	 
		return returnValue;
	}
}
