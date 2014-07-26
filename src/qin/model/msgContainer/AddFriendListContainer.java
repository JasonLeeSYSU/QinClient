package qin.model.msgContainer;

import java.io.Serializable;
import java.util.ArrayList;


public class AddFriendListContainer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private ArrayList<AddFriendContainer> friendList;
	
	public AddFriendListContainer(){
		friendList = null;
	}
	
	public AddFriendListContainer(ArrayList<AddFriendContainer> list){
		friendList = list;
	}
	
	public ArrayList<AddFriendContainer> getAddFriendList(){
		return friendList;
	}
	
	public void setAddFriendList(ArrayList<AddFriendContainer> list){
		friendList = list;
	}
}
