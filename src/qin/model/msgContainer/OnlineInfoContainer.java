package qin.model.msgContainer;

import java.io.Serializable;

public class OnlineInfoContainer implements Serializable {
	private static final long serialVersionUID = 1L;

	private int uid;
	private String IPAddr;
	private int port;
	private boolean isOnline;
	
	public OnlineInfoContainer(){
		uid = 0;
		IPAddr = null;
		port = 0;
		isOnline = false;
	}
	
	public OnlineInfoContainer(int id, String ip, int _port, boolean online){
		uid = id;
		IPAddr = ip;
		port = _port;
		isOnline = online;
	}
	
	public int getUid(){
		return uid;
	}
	
	public void setUid(int id){
		uid = id;
	}
	
	public String getIPAddr(){
		return IPAddr;
	}
	
	public void setIPAddr(String ip){
		IPAddr = ip;
	}
	
	public int getPort(){
		return port;
	}
	
	public void setPort(int p){
		port = p;
	}
	
	public boolean isOnline(){
		return isOnline;
	}
	
	public void setIsOnline(boolean online){
		isOnline = online;
	}
}