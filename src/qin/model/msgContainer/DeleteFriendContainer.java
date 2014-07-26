package qin.model.msgContainer;

import java.io.Serializable;

public class DeleteFriendContainer implements Serializable {
	private static final long serialVersionUID = 1L;

	private int sourceId, friendId;
		
	public DeleteFriendContainer(int _sourceId, int _friendId){
		sourceId = _sourceId;
		friendId = _friendId;
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