package qin.controller.handelThread;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.FileSystems;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.SwingWorker;

import qin.controller.QinUIController;
import qin.model.Command;
import qin.model.QinMessagePacket;
import qin.model.msgContainer.SendFileContainer;
import qin.ui.MessageUI;

public class ReceiveFileThread implements Runnable, PropertyChangeListener {

	private Socket socket;
	private ReceiveFileTask receiveFileTask = null;
	
	private int sourceID;
	MessageUI messageUI = null;
	
	private String fileName;
	private long totalSize;
	private long hadReceiveSize;
	
	private ActionListener agreeActionListener  = null;
	private ActionListener refuseActionListener = null;
	
	/***
	 * 获取 发送文件的名称、大小等消息
	 * @param socket
	 * @param sendFileSegment
	 */
	public ReceiveFileThread(Socket socket, SendFileContainer sendFileSegment) {
		this.socket = socket;
		this.sourceID = sendFileSegment.getSourceID();
		this.fileName = sendFileSegment.getFileName();
		this.totalSize = sendFileSegment.getTotalSize();
		this.hadReceiveSize = 0;
		
		messageUI = QinUIController.getInstance().getPrivateMessageUIByID(sourceID);
	}
	
	@Override
	public void run() {
		waitForReceiverOperate();
	}
	
	/***
	 * 设置事件监听器，处理用户的不同选择
	 */
	private void waitForReceiverOperate() {
		messageUI.waitForReceiveFile(); // 更改UI
	    messageUI.getAgreeReceiveButton().addActionListener(getAgreeActionListener());
		messageUI.getRefuseReceiveButton().addActionListener(getRefuseActionListener());
		messageUI.showMessageUI();
	}
	
	
	/***
	 * 注册监听器
	 * @return
	 */
	private ActionListener getAgreeActionListener() {
		if(agreeActionListener == null) {
			agreeActionListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
				        
					// 获取文件保存路径
					String filePath = "null";
					filePath = messageUI.showSaveFileChooser();
					if(filePath.equals("null"))
						return;
			
					try {
						if(filePath != null) {
							// 获取文件全路径
							String time = (new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss")).format(new Date());	
							fileName =  filePath + FileSystems.getDefault().getSeparator() + time +"_"+fileName;
						
							receiveFile(); // 接收文件
						}
					} catch (IOException ex) {
						try {
							socket.close();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						messageUI.sendFileError(); // 如果接收文件途中出错，把出错消息显示在UI上。
						System.out.println(ex.toString());
					}
					// 取消监听器
					messageUI.getAgreeReceiveButton().removeActionListener(getAgreeActionListener());
					messageUI.getRefuseReceiveButton().removeActionListener(getRefuseActionListener());
				}
			};
		}
		
		return agreeActionListener;
	}
	
	/***
	 * 对 拒绝接收文件 注册监听器
	 * @return
	 */
	private ActionListener getRefuseActionListener() {
		if(refuseActionListener == null) {
			refuseActionListener = new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						refuseReceive();
						socket.close();
						messageUI.refuseReceive();
					} catch (IOException ex) {
						messageUI.sendFileError();
						System.out.println(ex.toString());
					}
					messageUI.getAgreeReceiveButton().removeActionListener(getAgreeActionListener());
					messageUI.getRefuseReceiveButton().removeActionListener(getRefuseActionListener());
				}
			};
		}
		
		return refuseActionListener;
	}
	
	/***
	 * 同意接收文件
	 * 正式接收文件
	 * @throws IOException
	 */
	private void receiveFile() throws IOException {
		receiveFileTask = new ReceiveFileTask();
		receiveFileTask.addPropertyChangeListener(this);
		receiveFileTask.execute();
	}
	
	/***
	 * 拒绝接收文件
	 * @throws IOException
	 */
	private void refuseReceive() throws IOException {
		// 发送 拒绝接送信令
		QinMessagePacket packet = new QinMessagePacket(Command.REFUSETORECEIVEFILE);
		
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		out.writeObject(packet);
		out.flush();
	}
	
	/***
	 * 接收文件任务
	 *
	 */
	class ReceiveFileTask extends SwingWorker<Void, Void> {
		int progress = 0;
		
		@Override
		protected Void doInBackground() throws Exception {
			setProgress(0);
			
			// 发送 同意接收信令
			QinMessagePacket packet = new QinMessagePacket(Command.RECEIVEFILE);
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(packet);
			out.flush();
		
			// 更改UI
			messageUI.ReceivingFile();  
			
			// 接收 文件
			 int len;
	         byte[] bytes = new byte[128];
			 DataInputStream inStream = new DataInputStream(socket.getInputStream());
			 @SuppressWarnings("resource")
			OutputStream outStream = new FileOutputStream(new File(fileName));
			 
			 // 接收字符串
	         while ((len = inStream.read(bytes)) != -1) {
	        	 outStream.write(bytes, 0, len); 
	        	 hadReceiveSize += len;
	             progress = (int)(100 * hadReceiveSize/totalSize);
	             setProgress(Math.min(progress, 100));
	         }
			
	         // 更改UI
	         messageUI.finishReceive(); 
	         socket.close();
	         
			return null;
		}     
	 }


	/***
	 * 设置文件传输进度条
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		 if ("progress" == evt.getPropertyName()) {
	           int progress = (Integer) evt.getNewValue();
	            messageUI.getProgressBar().setValue(progress);
	     } 
	}
}
