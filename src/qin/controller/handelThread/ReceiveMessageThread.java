package qin.controller.handelThread;

import java.io.IOException;
import java.util.List;

import qin.controller.QinUIController;
import qin.model.domainClass.Message;
import qin.model.domainClass.Qun;
import qin.model.domainClass.User;
import qin.ui.MessageUI;

public class ReceiveMessageThread implements Runnable{
	private Message message = null;
	private boolean isUserMessage ;
	
	public ReceiveMessageThread(Message message, boolean isUserMessage) {
		this.message = message;
		this.isUserMessage = isUserMessage;
	}
	
	@Override
	public void run() {
		MessageUI messageUI = null;
		String msg = "";
		
		if(isUserMessage) {
			messageUI = QinUIController.getInstance().getPrivateMessageUIByID(message.getSourceId());
			if(messageUI == null) {
				return ;
			}
			msg = ((User)(messageUI.getObject())).getNickName() + " ";
			
		} else {
			messageUI = QinUIController.getInstance().getQunMessageUIByID(message.getDestinationId());
			if(messageUI == null) {
				return ;
			}
			
			List<User> qunUser = ((Qun)(messageUI.getObject())).getQunMember();
			
			// 在本地的群成员中查找用户的昵称
			String username = "";
			for(int i = 0; i < qunUser.size(); i++) {
				if(qunUser.get(i).getUid() == message.getSourceId()) {
					username = qunUser.get(i).getNickName() + "<" + message.getSourceId() + "> ";
					break;
				}
			}
			
			if(username.equals("")) { // 本地群成员中找不到用户的
				try {
					Qun tempQun = BusinessOperationHandel.findQun(message.getDestinationId(), true);
					
					for(int i = 0; i < tempQun.getQunMember().size(); i++) {
						if(tempQun.getQunMember().get(i).getUid() == message.getSourceId()) {
							username = tempQun.getQunMember().get(i).getNickName() + "<" + message.getSourceId() + "> ";
							qunUser.add(tempQun.getQunMember().get(i));
							break;
						}
					}
					
					if(username.equals("")) 
						username = "匿名者<" + message.getSourceId() + ">";
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
				
			}
			
			msg = username;
		}
		
		msg += message.getDateTime() + "\n" + message.getDetail();
		msg += "\n\n";
		messageUI.getShowMessageTextArea().setText(messageUI.getShowMessageTextArea().getText() + msg);
		messageUI.getShowMessageTextArea().setCaretPosition(messageUI.getShowMessageTextArea().getText().length());
		messageUI.showMessageUI();
	}
}
