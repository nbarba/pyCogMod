package engine;

import java.util.Vector;

import jess.Context;
import jess.JessException;
import jess.RU;
import jess.Userfunction;
import jess.Value;
import jess.ValueVector;


/**
 * Wrapper for the JESS Userfunction. All the domain dependent function our production rules
 * our going to use must extend this class. This way, the domain dependent functions only have to 
 * implement one method, called apply.
 * 
 * @author Nikolaos Barmpalios {nbarmpalios@gmail.com}
 *
 */
public abstract class UserDefinedFunction implements Userfunction {
	private String name;
	public void setName(String name){this.name=name;}
	
	public abstract String apply(Vector args, Context context);    	 	
    	
	@Override
	public Value call(ValueVector arg0, Context cntx) throws JessException {
		Vector argv = new Vector();
		
		for (int i = 1; i < arg0.size(); i++) {
			/*check if arguments contain facts or simple values*/
			if (arg0.get(i).javaObjectValue(cntx).toString().contains("MAIN::")){
				argv.add(arg0.get(i).factValue(cntx));
			}
			else 
				argv.add( arg0.get(i).stringValue(cntx) );				
		}
		
		String returnJessValue = null;
	    returnJessValue = apply( argv, cntx );
	    return (returnJessValue==null) ? new Value("FALSE",RU.SYMBOL) : new Value(returnJessValue,RU.STRING);		    
	}

	
	public String getName() {
		return this.name;
	}

}
