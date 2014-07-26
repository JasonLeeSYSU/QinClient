package qin.model.msgContainer;

import java.io.Serializable;

public class JoinQunContainer implements Serializable {
	private static final long serialVersionUID = 1L;

	private int userId, qunId;
	
	public final static int  REJECT = 2;
	public final static int  CHECKED = 0;
	public final static int  PASSED = 1;
	
	private int state;
		
	public JoinQunContainer(int _userId, int _qunId){
		userId = _userId;
		qunId = _qunId;
	}
	
	public JoinQunContainer(int uid, int qid, int st){
		userId = uid;
		qunId = qid;
		state = st;
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
	
	public int getState(){
		return state;
	}
	
	public void setState(int st){
		state = st;
	}
}