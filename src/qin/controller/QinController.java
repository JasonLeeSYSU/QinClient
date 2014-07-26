package qin.controller;


public class QinController {
	
	
	public void runClent() {
		//.RunLoginUI();
	}
	
	public static void main(String[] args) {
		
		Thread qinThreadControllerThread = new Thread(new QinThreadController());
		qinThreadControllerThread.start();
		
		Thread  qinUIControllerThread = new Thread(QinUIController.getInstance());
		qinUIControllerThread.start();
	}
}
