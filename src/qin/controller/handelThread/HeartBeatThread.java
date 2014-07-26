package qin.controller.handelThread;

import java.util.Timer;

import qin.model.Resource;

public class HeartBeatThread extends Thread {
	private int userId;
	

	public HeartBeatThread(int _userId) {
		userId = _userId;
	}
	
	public void run() {
		HeartBeatTimerTask heartBeatTimerTask = new HeartBeatTimerTask(userId);
		Timer timer = new Timer();
		timer.schedule(heartBeatTimerTask, 0, Resource.DelayTime);
	}
}
