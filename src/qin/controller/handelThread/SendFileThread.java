package qin.controller.handelThread;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import javax.swing.SwingWorker;

import qin.controller.QinUIController;
import qin.controller.handelThread.ReceiveFileThread.ReceiveFileTask;
import qin.model.Command;
import qin.model.QinMessagePacket;
import qin.model.domainClass.User;
import qin.model.msgContainer.SendFileContainer;
import qin.ui.MessageUI;

public class SendFileThread implements Runnable, PropertyChangeListener
{
	private int sourceID;
	private int destinationID;
	
	private String receiverIP;
	private int receiverPort;
	
	private String filePath;
	private long  totalSize;
	private long hadSendSize;
	
	private MessageUI messageUI = null;
	private Socket socket = null;
	
	private SendFileTask sendFileTask = null;
	
	/***
	 * 获取发送文件的大小
	 * 获取接收方的IP、Port
	 * @param sourceID
	 * @param destinationID
	 * @param filePath
	 */
	public SendFileThread(int sourceID, int destinationID, String filePath) {
		
		this.sourceID = sourceID;
		this.destinationID = destinationID;
		this.filePath = filePath;
		
		totalSize = (new File(filePath)).length();
		hadSendSize = 0;
		
		messageUI = QinUIController.getInstance().getPrivateMessageUIByID(destinationID);
		receiverIP = ((User)messageUI.getObject()).getIPAddr();
		receiverPort = ((User)messageUI.getObject()).getPort();
	}
	
	@Override
	public void run() {
		try {
			
			// 连接接收者
			socket = new Socket(receiverIP, receiverPort);
		
			// 改变UI界面
			messageUI.waitForSendFile();
			
			if(isCanSendFile()) {  // 对方同意接受文件
				sendFile(); 
			} else { // 对方拒绝接收文件
				messageUI.giveUpSend();
			}
			
			
		} catch (Exception e) {
			messageUI.sendFileError();
			System.out.println(e.toString());
		} 
	}
	
	
	/***
	 * 等待、接收、判断接收方是否同意接收文件
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private boolean isCanSendFile() throws IOException, ClassNotFoundException {
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		
		// 发送 文件传送请求
		QinMessagePacket packet = new QinMessagePacket(Command.SENDFILE);
		
		packet.setSendFileContainer(getSendFileApplicationSegment());
		out.writeObject(packet);
		out.flush();
		
		// 获取 对方是否同意接收文件
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		packet = (QinMessagePacket) in.readObject();

		if(packet.getCommand().equals(Command.RECEIVEFILE)) {
			return true;
		} else {
			return  false;
		}
	}
	
	/***
	 * 接收方同意接收文件后，正式发送
	 * @throws IOException
	 */
	private void sendFile() throws IOException {
		sendFileTask = new SendFileTask();
		sendFileTask.addPropertyChangeListener(this);
		sendFileTask.execute();
	}
	
	/***
	 * 获取发送文件请求包
	 * @return
	 */
	private SendFileContainer getSendFileApplicationSegment() {
		Path path = FileSystems.getDefault().getPath(filePath); 
		String fileName = path.getFileName()+"";
		
		return new SendFileContainer(sourceID, destinationID, fileName, totalSize);
	}
	
	
	class SendFileTask extends SwingWorker<Void, Void> {
		int progress = 0;
		
		@Override
		protected Void doInBackground() throws Exception {
			// TODO Auto-generated method stub
			
			int len;
			byte[] buff = new byte[128];
	        
			InputStream fileStream  = new FileInputStream(new File(filePath));
			DataOutputStream outPutStream = new DataOutputStream(socket.getOutputStream());
			
	        while ((len = fileStream.read(buff)) != -1) {
	        	outPutStream.write(buff, 0, len);
	        	hadSendSize += len;
	        	
	             progress = (int)(100 * hadSendSize/totalSize);
	             setProgress(Math.min(progress, 100));
	        }
	        
	        messageUI.finishSendFile();;  
	        
	        socket.close();
	        
			return null;
		}
		
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		 if ("progress" == evt.getPropertyName()) {
	           int progress = (Integer) evt.getNewValue();
	            messageUI.getProgressBar().setValue(progress);
	     }
	}
}
