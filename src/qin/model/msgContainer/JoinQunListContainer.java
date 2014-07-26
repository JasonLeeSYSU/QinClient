package qin.model.msgContainer;

import java.io.Serializable;
import java.util.ArrayList;

public class JoinQunListContainer implements Serializable {
	private static final long serialVersionUID = 1L;

	private ArrayList<JoinQunContainer> joinList = null;
	
	public JoinQunListContainer(){
		joinList = null;
	}
	
	public JoinQunListContainer(ArrayList<JoinQunContainer> al){
		joinList = al;
	}
	
	public ArrayList<JoinQunContainer> getJoinQunList(){
		return joinList;
	}
	
	public void setJoinQunList(ArrayList<JoinQunContainer> jql){
		joinList = jql;
	}
}
