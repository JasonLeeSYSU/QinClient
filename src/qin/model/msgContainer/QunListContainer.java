package qin.model.msgContainer;

import java.io.Serializable;
import java.util.ArrayList;

import qin.model.domainClass.Qun;

public class QunListContainer implements Serializable {
	private static final long serialVersionUID = 1L;

	private ArrayList<Qun> qunList = null;
	
	public QunListContainer(){
		qunList = new ArrayList<Qun>();
	}
	
	public QunListContainer(ArrayList<Qun> qlist){
		qunList = qlist;
	}
	
	public ArrayList<Qun> getQunList(){
		return qunList;
	}
	
	public void setQunList(ArrayList<Qun> qlist){
		qunList = qlist;
	}
}
