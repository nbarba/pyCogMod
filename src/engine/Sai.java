package engine;

/**
 * Class to hold the Selection, Action, Input, (i.e. SAI) that a rule suggests when that
 * rule fires. Please note:
 * Selection indicates which interface element/working memory element should be updated
 * Action   indicates how the interface element should be updated. (if its a table  it can be "Update Table", 
 *          if it is a dropdown element it can be "UpdateDropDown". 
 * Input    indicates what value should that interface element / working memory element should take.
 * 
 * @author Nikolaos Barmpalios {nbarmpalios@gmail.com}
 *
 */
public class Sai {

	String selection="";
	void setSelection(String str){this.selection=str;}
	public String getSelection(){return this.selection;}
	
	String action="";
	void setAction(String str){this.action=str;}
	public String getAction(){return this.action;}

	String input="";
	void setInput(String str){this.input=str;}
	public String getInput(){return this.input;}

	public Sai(String selection, String action, String input){
		setSelection(selection);
		setAction(action);
		setInput(input);
	}
	public String toString(){
		return "["+selection+","+action+","+input+"]";
	}
}
