package qin.model.domainClass;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int uid = 0;
	private String nickName = null;
	private String password = null;
	private String email = "";
	private int age = 0;
	private String gender = "";
	private Address address = new Address();
	private String headImage = "1.png";
	private String IPAddr = null;
	private int port = 0;
	private boolean isOnline = false;
	
	public User() {
		uid = 0;
		nickName = "";
	}
	
	/***
	 * 
	 * @param _ID
	 * @param _nickname
	 * @param _password
	 */
	public User(int _ID, String _nickname, String _password) {
		
		uid = _ID;
		nickName = _nickname;
		password = _password;
	}
	
	public void setUid(int _ID) {
		uid = _ID;
	}
	
	public void  setNickName(String _nickname) {
		nickName = _nickname;
	}
	
	public void  setPassword(String _password) {
		password = _password;
	}
	
	public void  setEmail(String _email) {
		email = _email;
	}
	
	public void setAge(int _age) {
		age = _age;
	}
	
	public void  setGender(String _gender) {
		gender = _gender;
	}

	public void  setAddress(Address _address) {
		address = _address;
	}
	
	public void  setHeadImage(String _headImage) {
		headImage =  _headImage;
	}
	
	public void  setIPAddr(String _IP) {
		IPAddr = _IP;
	}
	
	public void setPort(int _port) {
		port = _port;
	}
	
	public int getUid() {
		return uid;
	}
	
	public String getNickName() {
		return nickName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public String getEmail() {
		return email;
	}
	
	public int getAge() {
		return age;
	}
	
	public String getGender() {
		return gender;
	}

	public Address getAddress() {
		return address;
	}
	
	public String getHeadImage() {
		return headImage;
	}
	
	public String getIPAddr() {
		return IPAddr;
	}
	
	public int getPort() {
		return port;
	}

	public boolean isUserOnline() {
		return isOnline;
	}
	
	public void online() {
		isOnline = true;
	}
	
	public void offline() {
		isOnline = false;
	}

}