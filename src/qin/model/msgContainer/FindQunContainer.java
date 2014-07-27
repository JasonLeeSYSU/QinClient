package qin.model.msgContainer;
import java.io.Serializable;

import qin.model.domainClass.Qun;

public class FindQunContainer implements Serializable {
	private static final long serialVersionUID = 1L;

	private Qun qun = null;
	private boolean requestMemberInfo;
	
	public FindQunContainer(){
		qun = null;
		requestMemberInfo = false;
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
	
	public boolean getRequestMemberInfo() {
		return requestMemberInfo;
	}
	
	public void setRequestMemberInfo(boolean _requestMemberInfo) {
		requestMemberInfo = _requestMemberInfo;
	}
}