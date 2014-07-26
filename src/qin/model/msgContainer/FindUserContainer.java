package qin.model.msgContainer;

import java.io.Serializable;

import qin.model.domainClass.User;

public class FindUserContainer implements Serializable {
	private static final long serialVersionUID = 1L;

	private User user = null;
	
	public FindUserContainer(){
		user = null;
	}
	
	public FindUserContainer(User us){
		user = us;
	}
	
	public User getUser(){
		return user;
	}
	
	public void setUser(User u){
		user = u;
	}
	
	public int getUserId(){
		return user.getUid();
	}
}