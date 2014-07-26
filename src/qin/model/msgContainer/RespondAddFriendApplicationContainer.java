package qin.model.msgContainer;

import java.io.Serializable;

public class RespondAddFriendApplicationContainer implements Serializable {
	private static final long serialVersionUID = 1L;

	private int sourceId, friendId;
	private boolean ifAgree;
		
	public RespondAddFriendApplicationContainer(int _sourceId, int _friendId, boolean _ifAgree){
		sourceId = _sourceId;
		friendId = _friendId;
		ifAgree = _ifAgree;
	}
	
	public int getSourceId() {
		return sourceId;
	}
	
	public int getFriendId() {
		return friendId;
	}
	
	public boolean getIfAgree() {
		return ifAgree;
	}
	
	public void setSourceId(int _sourceId) {
		sourceId = _sourceId;
	}
	
	public void setFriendId(int _friendId) {
		friendId = _friendId;
	}
	
	public void setIfAgree(boolean _ifAgree) {
		ifAgree = _ifAgree;
	}
}