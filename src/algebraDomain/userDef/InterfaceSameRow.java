package algebraDomain.userDef;

import java.util.Vector;

import engine.UserDefinedFunction;
import jess.Context;
import jess.Fact;
import jess.JessException;

public class InterfaceSameRow extends UserDefinedFunction{

	public InterfaceSameRow(){
		setName("same-row");
	}
	
	public String apply(Vector args, Context context) {
		// TODO Auto-generated method stub
		Fact wme1=(Fact)args.get(0);
		Fact wme2=(Fact)args.get(1);
		int wme1Row=-1;
		int wme2Row=-2;
		try {
			wme1Row=wme1.getSlotValue("row-number").intValue(context.getEngine().getGlobalContext());
			wme2Row=wme2.getSlotValue("row-number").intValue(context.getEngine().getGlobalContext());
		} catch (JessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
			
		return (wme1Row==wme2Row)?"T":null;
	}

}
