package qin.model.msgContainer;

import java.io.Serializable;
import java.util.ArrayList;

import qin.model.domainClass.User;

public class UserListContainer implements Serializable {
	private static final long serialVersionUID = 1L;

	private ArrayList<User> userList;
	
	public UserListContainer(){
		userList = null;
	}
	
	public UserListContainer(ArrayList<User> list){
		userList = list;
	}
	
	public ArrayList<User> getUserList(){
		return userList;
	}
	
	public void setUserList(ArrayList<User> list){
		userList = list;
	}
}
