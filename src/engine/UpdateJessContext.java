package engine;

import java.util.Vector;

import engine.Sai;
import jess.Context;
import jess.Funcall;
import jess.JessException;
import jess.Userfunction;
import jess.Value;
import jess.ValueVector;

/**
 * Class used to setup a special JESS variable, which is later retrieved from Java.
 * This class must be part of the right hand side of every production rule. 
 *
 * @author Nikolaos Barmpalios {nbarmpalios@gmail.com}
 */
public class UpdateJessContext implements Userfunction {

	
	/** Function name, this is how Jess will refer to this function. */
	private static final String UPDATE_JESS_CONTEXT = "update-jess-context";


	public UpdateJessContext() {
		super();
	}
	
	
	/**
	 * Returns the name of this function as registered with Jess.
	 * @see jess.Userfunction#getName()
	 */
	public String getName() {
		return UPDATE_JESS_CONTEXT;
	}
	
	public Value call(ValueVector vv, Context context) throws JessException {
		Vector args = new Vector();
		
		for (int i = 1; i < vv.size(); i++) 
		    args.add( vv.get(i).stringValue(context) );
		
		boolean result=true;
		
		if (args.size()!=3) 
			result=false;
		else{
			/* args are expected to contain the predicted (by the firing rule) selection,action,input
			 * so we need to update the working memory so Java can get this sai*/
			Sai sai=new Sai((String) args.get(0),(String) args.get(1),(String) args.get(2));
			context.getEngine().getGlobalContext().setVariable("*sSai*",new Value(sai));
			result=true;
		}
		
		
		return result?Funcall.TRUE:Funcall.FALSE;
		
		
	}

}
