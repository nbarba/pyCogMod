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


public class HasConstTerm extends UserDefinedFunction {

    public HasConstTerm() {
        setName("has-const-term");
    }

    public String apply(Vector args, Context context) {
    	String str=(String)args.get(0);
    	return MathUtilities.hasConstantTerm(str)?"T":null;
    	 	
    }

}
