package qin.model.msgContainer;

import java.io.Serializable;

import qin.model.domainClass.User;

public class RegisterContainer implements Serializable {
	private static final long serialVersionUID = 1L;

	private User user;
	
	public RegisterContainer(){
		user = null;
	}
	
	public RegisterContainer(User u){
		user = u;
	}
	
	public User getUser(){
		return user;
	}
	
	public void setUser(User u){
		user = u;
	}
}