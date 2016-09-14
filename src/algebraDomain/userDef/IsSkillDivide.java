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


public class IsSkillDivide extends UserDefinedFunction {

    public IsSkillDivide() {
        setName("is-skill-divide");
    }

   
	public String apply(Vector args, Context context) {
        String skillOperand = (String)args.get(0);
        return MathUtilities.extractSkillFromTransformation(skillOperand,"divide")?"T":null;
	}
}
