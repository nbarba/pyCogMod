package algebraDomain.userDef;

import java.util.Vector;

import engine.UserDefinedFunction;
import jess.Context;
import jess.Fact;

public class InterfaceDistinctive extends UserDefinedFunction {
	
	public  InterfaceDistinctive(){
		setName("distinctive");
	}

	public String apply(Vector args, Context context) {
		Fact wme1=(Fact)args.get(0);
		Fact wme2=(Fact)args.get(1);
		
		return (wme1.getFactId()!=wme2.getFactId())?"T":null;
	}

}
