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


public class HasCoefficient extends UserDefinedFunction {

	
    public HasCoefficient() {
        setName("has-coefficient");
    }

	public String apply(Vector args, Context context) {
		String str=(String)args.get(0);
    	return MathUtilities.containsRegularExpression(str,"-?\\d{1,10}[a-z]$")?"T":null;
	}
}
