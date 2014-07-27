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
			if(messageUI == null) {
				System.out.println("找不到 目的主 的 Private MeaageUI");
				return ;
			}
			msg = ((User)(messageUI.getObject())).getNickName() + " ";
			
		} else {
			System.out.println("发送群信息的ID ： " + message.getQunId());
			messageUI = QinUIController.getInstance().getQunMessageUIByID(message.getDestinationId());
			if(messageUI == null) {
				System.out.println("找不到 目的主 的 Quns MeaageUI");
				return ;
			}
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
		msg += "\n\n";
		messageUI.getShowMessageTextArea().setText(messageUI.getShowMessageTextArea().getText() + msg);
		messageUI.getShowMessageTextArea().setCaretPosition(messageUI.getShowMessageTextArea().getText().length());
		messageUI.showMessageUI();
	}
}
