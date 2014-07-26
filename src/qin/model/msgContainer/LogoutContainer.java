package qin.model.msgContainer;

import java.io.Serializable;

public class LogoutContainer implements Serializable {
	private static final long serialVersionUID = 1L;

	private int uid;
	
	public LogoutContainer(int id){
		uid = id;
	}
	
	public int getUid(){
		return uid;
	}
	
	public void setUid(int id){
		uid = id;
	}
}