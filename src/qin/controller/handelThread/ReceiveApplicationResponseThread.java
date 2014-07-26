package qin.controller.handelThread;

import qin.controller.QinUIController;

public class ReceiveApplicationResponseThread implements Runnable {

	private int sourceID;
	private int qunID;
	private boolean isSuccess;
	private boolean isAddFriend;
	
	public ReceiveApplicationResponseThread(boolean isAddFriend, int ID, boolean isSuccess) {
		this.isAddFriend = true;
		this.isSuccess = isSuccess;
		
		if(this.isAddFriend)
			this.sourceID = ID;
		else
			this.sourceID = ID;
	}
	

	@Override
	public void run() {
		if(isAddFriend) {
			QinUIController.getInstance().showAddFriendApplicationResponse(sourceID, isSuccess);
		} else {
			QinUIController.getInstance().showJoinQunApplicationResponse(qunID, isSuccess);
		}
	}
}
