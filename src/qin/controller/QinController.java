package qin.controller;

import qin.controller.handelThread.HeartBeatThread;

public class QinController {
	
	
	public void runClent() {
		QinUIController.getInstance().RunLoginUI();
	}
	
	public static void main(String[] args) {
		
		Thread qinThreadController = new Thread(new QinThreadController());
		qinThreadController.start();
		
		QinController qinController = new QinController();
		qinController.runClent();
	}
}
