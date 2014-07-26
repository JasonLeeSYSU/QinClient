package qin.model.msgContainer;

import java.io.Serializable;

import qin.model.domainClass.User;

public class ModifyUserInfoContainer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private User user;
	
	public ModifyUserInfoContainer(User _user) {
		user = _user;
	}

	public ModifyUserInfoContainer(ModifyUserInfoContainer muic) {
		user = muic.user;
	}
	
	public void setUser(User _user) {
		user = _user;
	}
	
	public User getUser() {
		return user;
	}
}