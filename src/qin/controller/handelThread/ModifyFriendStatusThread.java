package qin.controller.handelThread;

import qin.controller.QinUIController;
import qin.model.domainClass.User;
import qin.ui.MainUI;

public class ModifyFriendStatusThread implements Runnable {
	private int id;
	private boolean isOnline;
	private String IPAdrr;
	private int Port ;
	
	public ModifyFriendStatusThread(int id) {
		this.id = id;
		this.isOnline = false;
	}
	
	public ModifyFriendStatusThread(int id, String IPAdrr, int Port) {
		this.id = id;
		this.isOnline = false;
		this.IPAdrr = IPAdrr;
		this.Port = Port;
	}

	@Override
	public void run() {
		if(isOnline) {
			QinUIController.getInstance().friendOnline(id, IPAdrr, Port);
		} else {
			QinUIController.getInstance().friendOffline(id);;
		}
	}
}
