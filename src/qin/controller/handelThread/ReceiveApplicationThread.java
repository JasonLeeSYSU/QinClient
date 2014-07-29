package qin.controller.handelThread;

import qin.controller.QinUIController;

public class ReceiveApplicationThread implements Runnable {

	private int sourceID;
	private int qunID;
	private boolean isAddFriend;
	
	public ReceiveApplicationThread(int sourceID) {
		this.sourceID = sourceID;
		this.isAddFriend = true;
	}
	
	public ReceiveApplicationThread(int sourceID, int qunID) {
		this.sourceID = sourceID;
		this.qunID = qunID;
		this.isAddFriend = false;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if(isAddFriend) {
			QinUIController.getInstance().showAddFriendApplication(sourceID);
		} else {
			QinUIController.getInstance().showJoinQunApplication(sourceID, qunID);
		}
	}

}
