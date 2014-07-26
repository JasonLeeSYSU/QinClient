package qin.model.msgContainer;

import java.io.Serializable;

public class RespondJoinQunApplicationContainer implements Serializable {
	private static final long serialVersionUID = 1L;

	private int userId, qunId;
	private boolean ifAgree;
		
	public RespondJoinQunApplicationContainer(int _userId, int _qunId, boolean _ifAgree){
		userId = _userId;
		qunId = _qunId;
		ifAgree = _ifAgree;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public int getQunId() {
		return qunId;
	}
	
	public boolean getIfAgree() {
		return ifAgree;
	}
	
	public void setUserId(int _userId) {
		userId = _userId;
	}
	
	public void setQunId(int _qunId) {
		qunId = _qunId;
	}
	
	public void setIfAgree(boolean _ifAgree) {
		ifAgree = _ifAgree;
	}
}