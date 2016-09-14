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


public class IsConstant extends UserDefinedFunction {

	
    public IsConstant() {
        setName("is-constant");
    }

	public String apply(Vector args, Context context) {
		   boolean returnValue=false;
	    	String str=(String)args.get(0);
	    	return MathUtilities.containsRegularExpression(str,"^[-]?\\d+\\/\\d+$|^[-]?\\d+$")?"T":null;

	}
}
