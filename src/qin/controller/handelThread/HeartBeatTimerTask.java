package qin.controller.handelThread;

import java.io.IOException;
import java.util.TimerTask;

import qin.controller.handelThread.basicOperation.MessagePacketSender;
import qin.model.Command;
import qin.model.QinMessagePacket;
import qin.model.msgContainer.HeartBeatContainer;

public class HeartBeatTimerTask extends TimerTask {
	private int userId;
	
	public HeartBeatTimerTask(int _userId) {
		userId = _userId;
	}

	@Override
	public void run() {
		try {
			QinMessagePacket messagePacket = new QinMessagePacket(Command.HEARTBEAT);
			HeartBeatContainer heartBeatContainer = new HeartBeatContainer(userId);
			messagePacket.setHeartBeatContainer(heartBeatContainer);
			MessagePacketSender.sendPacket(messagePacket);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
