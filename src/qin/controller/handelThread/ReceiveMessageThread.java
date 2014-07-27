package qin.controller.handelThread;

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
			if(messageUI == null)
				return ;
			msg = ((User)(messageUI.getObject())).getNickName() + " ";
			
		} else {
			messageUI = QinUIController.getInstance().getQunMessageUIByID(message.getSourceId());
			if(messageUI == null)
				return ;
			List<User> qunUser = ((Qun)(messageUI.getObject())).getQunMember();
			
			String username = "匿名者 ";
			for(int i = 0; i < qunUser.size(); i++) {
				if(qunUser.get(i).getUid() == message.getSourceId()) {
					username = qunUser.get(i).getNickName() + "<" + message.getSourceId() + "> ";
					break;
				}
			}
			msg = username;
		}
		
		msg += message.getDateTime() + "\n" + message.getDetail();
		messageUI.getShowMessageTextArea().setText(messageUI.getShowMessageTextArea().getText() + msg);
		messageUI.showMessageUI();
	}
}
