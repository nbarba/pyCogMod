package algebraDomain.userDef;

import java.util.Vector;

import algebraDomain.MathUtilities;
import engine.UserDefinedFunction;
import jess.Context;
import jess.JessException;
import jess.RU;
import jess.Userfunction;
import jess.Value;
import jess.ValueVector;


public class IsLastConstTermNegative extends UserDefinedFunction {

	
    public IsLastConstTermNegative() {
        setName("is-lastconstterm-negative");
    }

	public String apply(Vector args, Context context) {
		String str=(String)args.get(0);
		boolean regexMatch=MathUtilities.containsRegularExpression(str,"^\\d{0,10}[a-z]\\-\\d{0,10}$") || MathUtilities.containsRegularExpression(str,"^\\-\\d{0,10}\\+\\d{0,10}[a-z]$");

		return regexMatch?"T":null;
	}
}
