package qin.model.msgContainer;

import java.io.Serializable;

public class HeartBeatContainer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	int userId = -1;
	
	public HeartBeatContainer(HeartBeatContainer hbc){
		userId = hbc.getUserId();
	}
	
	public HeartBeatContainer(int _userId){
		userId = _userId;
	}
	
	public int getUserId(){
		return userId;
	}
	
	public void setUserId(int _userId){
		userId = _userId;
	}
}