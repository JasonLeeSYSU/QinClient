package qin.controller.handelThread;

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
			msg = ((Qun)(messageUI.getObject())).getQunName() + " ";
		}
		
		msg += message.getDateTime() + "\n" + message.getDetail();
		messageUI.getInputTextArea().setText(messageUI.getInputTextArea().getText() + msg);
	}
}
