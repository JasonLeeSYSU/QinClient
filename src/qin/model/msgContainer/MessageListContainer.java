package qin.model.msgContainer;

import java.io.Serializable;
import java.util.ArrayList;

import qin.model.domainClass.Message;

public class MessageListContainer implements Serializable {
	private static final long serialVersionUID = 1L;

	ArrayList<Message> msgList = null;
	
	public MessageListContainer(MessageListContainer msglc){
		msgList = msglc.getMessageList();
	}
	
	public MessageListContainer(ArrayList<Message> mList){
		msgList = mList;
	}

	public ArrayList<Message> getMessageList(){
		return msgList;
	}
	
	public void setMessageList(ArrayList<Message> mList){
		msgList = mList;
	}
}