package qin.model.msgContainer;

import java.io.Serializable;

public class AddFriendContainer implements Serializable {
	private static final long serialVersionUID = 1L;

	private int sourceId, friendId;
	
	public final static int  REJECT = 2;
	public final static int  CHECKED = 0;
	public final static int  PASSED = 1;
	
	private int state;
		
	public AddFriendContainer(int _sourceId, int _friendId){
		sourceId = _sourceId;
		friendId = _friendId;
		state = 0;
	}
	
	public AddFriendContainer(int s, int f, int st){
		sourceId = s;
		friendId = f;
		state = st;
	}
	
	public int getState(){
		return state;
	}
	
	public void setState(int st){
		state = st;
	}
	
	public int getSourceId() {
		return sourceId;
	}
	
	public int getFriendId() {
		return friendId;
	}
	
	public void setSourceId(int _sourceId) {
		sourceId = _sourceId;
	}
	
	public void setFriendId(int _friendId) {
		friendId = _friendId;
	}
}
