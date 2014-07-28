package qin.controller;

public class QinController {
	
	public static void main(String[] args) {
		
		// 开启UI线程
		Thread  qinUIControllerThread = new Thread(QinUIController.getInstance());
		qinUIControllerThread.start();
		
		// 开启监听线程
		Thread qinThreadControllerThread = new Thread(new QinThreadController());
		qinThreadControllerThread.start();
	}
}
