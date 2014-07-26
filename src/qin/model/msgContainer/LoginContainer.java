package qin.model.msgContainer;

import java.io.Serializable;

import qin.model.domainClass.User;

public class LoginContainer implements Serializable{
	private static final long serialVersionUID = 1L;
	private User user;

	/***
	 * 客户端 --> 服务端
	 * 需要提交 账号 和 密码
	 * QinMessagePacket.command = LOGIN
	 * @param id
	 * @param passeord
	 */
	public LoginContainer(int id, String password, String IP, int listenPort) {
		user = new User();
		
		user.setUid(id);
		user.setPassword(password);
		user.setIPAddr(IP);
		user.setPort(listenPort);
	}
	
	/***
	 * 服务端 --> 客户端
	 * 服务端在验证账号、密码正确后，返回登录用户的详细消息
	 * QinMessagePacket.command = LOGINSUCCESS
	 * @param _user
	 */
	public LoginContainer(User _user) {
		user = _user;
	}
	
	/***
	 * 服务端 --> 客户端(登录用户的在线好友)
	 * QinMessagePacket.command = FRIENDLOGIN
	 * @param id
	 */
	public LoginContainer(int id) {
		user = new User();
		user.setUid(id);
	}
	
	/***
	 * （1）服务端：获取登录用户的账号ID
	 * （2）客户端(登录用户的在线好友) : 获取登录好友的账号ID
	 * @return
	 */
	public int getLoginUserID() {
		return user.getUid();
	}
	
	/***
	 * 服务端 获取登录用户的密码password 
	 * @param 
	 */
	public String getLoginUserPassword() {
		return  user.getPassword();
	} 
	
	/***
	 * 客户端 获取其用户消息
	 * @return
	 */
	public User getUser() {
		return user;
	}
}