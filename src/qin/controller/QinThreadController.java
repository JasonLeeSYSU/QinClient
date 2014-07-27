package qin.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import qin.controller.handelThread.ModifyFriendStatusThread;
import qin.controller.handelThread.ReceiveApplicationResponseThread;
import qin.controller.handelThread.ReceiveApplicationThread;
import qin.controller.handelThread.ReceiveFileThread;
import qin.controller.handelThread.ReceiveMessageThread;
import qin.model.Command;
import qin.model.QinMessagePacket;
import qin.model.msgContainer.AddFriendContainer;
import qin.model.msgContainer.JoinQunContainer;

public class QinThreadController implements Runnable {
	private ServerSocket clientListenerSocket;
	
	public QinThreadController() 
	{
		try {
			clientListenerSocket = new ServerSocket(0);
			QinUIController.getInstance().setClientListenerPort(clientListenerSocket.getLocalPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	@Override
	public void run() {
		System.out.println("客户端正在监听端口");
		
		while(true)
		{
			try
			{
				Socket socket = clientListenerSocket.accept();
				
				ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
				QinMessagePacket packet = (QinMessagePacket) in.readObject();
				
				System.out.println("Order come: " + packet.getCommand());
				
				if(packet.getCommand().equals(Command.RECEIVEPRIVATEMSG)) {
					 // 接收到好友信息
					Thread receivePrivateMessageThread = new Thread(new ReceiveMessageThread(packet.getMessageContainer().getMessage(), true));
					receivePrivateMessageThread.start();
					
				} else if(packet.getCommand().equals(Command.RECEIVEQUNMSG)) {
					// 接收到群信息
					Thread receivePrivateMessageThread = new Thread(new ReceiveMessageThread(packet.getMessageContainer().getMessage(), false));
					receivePrivateMessageThread.start();
					
				} else if(packet.getCommand().equals(Command.FRIENDLOGIN)) {
					// 接收到好友上线通知
					int onlineFriendID = packet.getOnlineInfoContainer().getUid();
					String IPAddr = packet.getOnlineInfoContainer().getIPAddr();
					int Port = packet.getOnlineInfoContainer().getPort();
					
					Thread friendOnlineThread = new Thread(new ModifyFriendStatusThread(onlineFriendID, IPAddr, Port));
					friendOnlineThread.start();
					
				} else if(packet.getCommand().equals(Command.FRIENDLOGOUT)) {
					 // 接收到好友下线通知
					int offlineFriendID = packet.getOnlineInfoContainer().getUid();
					
					Thread friendOfflineThread = new Thread(new ModifyFriendStatusThread(offlineFriendID));
					friendOfflineThread.start();
													
				} else if(packet.getCommand().equals(Command.RECEIVEADDFRIENDAPPLICATION)) {
					// 接收到被添加好友的请求
					 int sourceID = packet.getAddFriendContainer().getSourceId();
					 
					 Thread receiveAddFriendApplicationThread = new Thread(new ReceiveApplicationThread(sourceID));
					 receiveAddFriendApplicationThread.start();
					 
				} else if(packet.getCommand().equals(Command.GAINADDFRIENDRESPOND)) {
					 // 接收到添加好友结果
					 int addedUserID = packet.getAddFriendContainer().getFriendId();
					 boolean isAdded;
					 if (packet.getAddFriendContainer().getState() == AddFriendContainer.PASSED) {
						 isAdded = true;
					 } else {
						 isAdded = false;
					 }
					 
					 Thread receiveAddFriendResponseThread = new Thread(new ReceiveApplicationResponseThread(true,addedUserID, isAdded));
					 receiveAddFriendResponseThread.start();
					
				} else if(packet.getCommand().equals(Command.RECEIVEJOINQUNAPPLICATION)) {
					 // 接收到加入群请求
					 int sourceID = packet.getJoinQunContainer().getUserId();
					 int addedQunID = packet.getJoinQunContainer().getQunId();
					 
					 Thread receiveJoinQunApplicationThread = new Thread(new ReceiveApplicationThread(sourceID, addedQunID));
					 receiveJoinQunApplicationThread.start();
					 
				} else if(packet.getCommand().equals(Command.GAINQUNRESPOND)) {
					// 接收到加入群结果
					 int addedQunID = packet.getJoinQunContainer().getQunId();
					 boolean isAdded;
					 if (packet.getJoinQunContainer().getState() == JoinQunContainer.PASSED) {
						 isAdded = true;
					 } else {
						 isAdded = false;
					 }
					 
					Thread receiveJoinQunResponseThread = new Thread(new ReceiveApplicationResponseThread(false, addedQunID, isAdded));
					receiveJoinQunResponseThread.start();
					
				} else if(packet.getCommand().equals(Command.SENDFILE)) { 
					// 收到文件接收请求
					Thread receiveFileThread = new Thread(new ReceiveFileThread(socket, packet.getSendFileSegment()));
					receiveFileThread.start();
					
				} 
				
			} catch(Exception e) {
				System.out.println(e.toString());
			}
		}
	}
}

