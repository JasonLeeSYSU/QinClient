package qin.model.msgContainer;

import java.io.Serializable;

public class ExitQunContainer implements Serializable {
	private static final long serialVersionUID = 1L;

	private int userId, qunId;
		
	public ExitQunContainer(int _userId, int _qunId){
		userId = _userId;
		qunId = _qunId;
	}
	
	public int getSourceId() {
		return userId;
	}
	
	public int getFriendId() {
		return qunId;
	}
	
	public void setSourceId(int _userId) {
		userId = _userId;
	}
	
	public void setFriendId(int _qunId) {
		qunId = _qunId;
	}
}