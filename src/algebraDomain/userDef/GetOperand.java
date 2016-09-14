package algebraDomain.userDef;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import engine.UserDefinedFunction;
import jess.Context;
import jess.JessException;
import jess.RU;
import jess.Userfunction;
import jess.Value;
import jess.ValueVector;


public class GetOperand extends UserDefinedFunction {

    public GetOperand() {
        setName("get-operand");
    }

	public String apply(Vector args, Context context) {
	    boolean returnValue=false;
    	String str=(String)args.get(0);
   
    	String operand=null;
    	
    	int indx = str.indexOf(' ');
    	if ( indx > 0 ) {
    	    operand = str.substring( indx +1 );
    	}
      	
    	return operand;
	}
}
