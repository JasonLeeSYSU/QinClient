package qin.model.msgContainer;

import java.io.Serializable;

public class ReceiveJoinQunApplicationContainer implements Serializable {
	private static final long serialVersionUID = 1L;

	private int userId, qunId;
		
	public ReceiveJoinQunApplicationContainer(int _userId, int _qunId){
		userId = _userId;
		qunId = _qunId;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public int getQunId() {
		return qunId;
	}
	
	public void setUserId(int _userId) {
		userId = _userId;
	}
	
	public void setQunId(int _qunId) {
		qunId = _qunId;
	}
}