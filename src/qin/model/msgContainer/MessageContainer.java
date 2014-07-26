package qin.model.msgContainer;

import java.io.Serializable;

import qin.model.domainClass.Message;

public class MessageContainer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Message msg = null;
	
	public MessageContainer(MessageContainer msgc){
		msg = msgc.getMessage();
	}
	
	public MessageContainer(Message m){
		msg = m;
	}
	
	public Message getMessage(){
		return msg;
	}
	
	public void setMessage(Message _msg){
		msg = _msg;
	}
}