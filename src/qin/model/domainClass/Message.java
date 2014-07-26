package qin.model.domainClass;

import java.io.Serializable;

public class Message implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int qunId;
	private int sourceId;
	private int destinationId;
	private String detail;
	private boolean isQunMsg;
	private String dateTime;
	
	public Message(){
		qunId = 0;
		sourceId = 0;
		destinationId = 0;
		detail = null;
		isQunMsg = false;
		dateTime = null;
	}
	
	public Message(Message msg){
		qunId = msg.qunId;
		sourceId = msg.getSourceId();
		destinationId = msg.getDestinationId();
		detail = msg.getDetail();
		isQunMsg = msg.isQunMsg();
		dateTime = msg.getDateTime();
	}
	
	public Message(int s, int d, String _message, String _time) {
		sourceId = s;
		destinationId = d;
		detail = _message;
		dateTime = _time;
	}
	
	public int getQunId(){
		return qunId;
	}
	
	public void setQunId(int q){
		qunId = q;
	}
	
	public int getSourceId(){
		return sourceId;
	}
	
	public void setSourceId(int sid){
		sourceId = sid;
	}
	
	public int getDestinationId(){
		return destinationId;
	}
	
	public void setDestinationId(int did){
		destinationId = did;
	}
	
	public String getDetail(){
		return detail;
	}
	
	public void setDetail(String msg){
		detail = msg;
	}
	
	public String getDateTime(){
		return dateTime;
	}
	
	public void setDateTime(String dtime){
		dateTime = dtime;
	}
	
	public boolean isQunMsg(){
		return isQunMsg;
	}
	
	public void setIsQunMsg(boolean isqm){
		isQunMsg = isqm;
	}

}