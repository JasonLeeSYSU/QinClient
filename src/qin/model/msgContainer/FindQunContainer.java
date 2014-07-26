package qin.model.msgContainer;
import java.io.Serializable;

import qin.model.domainClass.Qun;

public class FindQunContainer implements Serializable {
	private static final long serialVersionUID = 1L;

	private Qun qun = null;
	
	public FindQunContainer(){
		qun = null;
	}
	
	public FindQunContainer(Qun q){
		qun = q;
	}
	
	public Qun getQun(){
		return qun;
	}
	
	public void setQun(Qun q){
		qun = q;
	}
}