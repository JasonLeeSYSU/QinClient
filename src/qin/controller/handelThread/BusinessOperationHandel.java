package qin.controller.handelThread;

import java.io.*;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import qin.controller.handelThread.basicOperation.MessagePacketSender;
import qin.model.*;
import qin.model.domainClass.*;
import qin.model.msgContainer.*;
import qin.testcase.StaticTestCase;

public class BusinessOperationHandel {
	
	/***
	 * return the userId if register successfully
	 * return -1(Resource.LoginFailUserID) if register unsuccessfully.
	 * @param nickname
	 * @param password
	 * @param email
	 * @param age
	 * @param gender
	 * @param address
	 * @param headImage
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static int register(String nickname, String password, String email, int age, String gender, Address address, String headImage) throws ClassNotFoundException, IOException {
		int userId = Resource.RegisterFailUserID;
		try {			
			QinMessagePacket messagePacket = new QinMessagePacket(Command.REGISTER);
			
			User user = new User();
			user.setNickName(nickname);
			user.setPassword(password);
			user.setEmail(email);
			user.setAge(age);
			user.setGender(gender);
			user.setAddress(address);
			user.setHeadImage(headImage);
			
			RegisterContainer registerContainer = new RegisterContainer(user);
			
			messagePacket.setRegisterContainer(registerContainer);
			QinMessagePacket registerResultPacket = MessagePacketSender.sendPacket(messagePacket);
			
			if(registerResultPacket.getCommand().equals(Command.REGISTERSUCCESS)) {
				userId = (Integer.valueOf(registerResultPacket.getResponseMsg())).intValue();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userId;
	}

	/***
	 * return TRUE if login successfully
	 * return FALSE if login unsuccessfully.
	 * @param userID
	 * @param password
	 * @param listenPort
	 * @param offLineMsg
	 * @param friends
	 * @param quns
	 * @param addFriendContainers
	 * @param joinQunContainers
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static QinMessagePacket login(int userID, String password, int listenPort) 
			throws ClassNotFoundException, IOException {
		
		QinMessagePacket loginResultPacket = null;
		try {			
			QinMessagePacket messagePacket = new QinMessagePacket(Command.LOGIN);
			User user = new User();
			user.setUid(userID);
			user.setPassword(password);
			user.setPort(listenPort);
			LoginContainer loginContainer = new LoginContainer(user);
			
			messagePacket.setLoginContainer(loginContainer);
			
			System.out.println("等待login结果包！");
			loginResultPacket = MessagePacketSender.sendPacket(messagePacket);
			System.out.println("返回login结果包！");
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return loginResultPacket;
	}
	
	
	/***
	 * logout
	 * @param userID
	 * @param password
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static void logout(int userID) throws ClassNotFoundException, IOException {
		try {			
			QinMessagePacket messagePacket = new QinMessagePacket(Command.LOGOUT);
			
			LogoutContainer logoutContainer = new LogoutContainer(userID);
			
			messagePacket.setLogoutContainer(logoutContainer);
			
			Socket socket = new Socket(Resource.ServerIP, Resource.ServerPort);
				
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(messagePacket);
			out.flush();
			
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/***
	 * 
	 * @param sourceId
	 * @param destinationId
	 * @param detail
	 * @param isQunMsg
	 */
	public static void sendMessage(int sourceId, int destinationId, String detail, boolean isQunMsg) {
		QinMessagePacket messagePacket = new QinMessagePacket(null);
		if (isQunMsg) {
			messagePacket.setCommand(Command.SENDQUNMSG);
		} else {
			messagePacket.setCommand(Command.SENDPRIVATEMSG);
		}
		
		Message message = new Message();
		message.setSourceId(sourceId);
		message.setDestinationId(destinationId);
		message.setDetail(detail);
		message.setIsQunMsg(isQunMsg);
		
		MessageContainer messageContainer = new MessageContainer(message);
		
		messagePacket.setMessageContainer(messageContainer);

		try {
			Socket socket = new Socket(Resource.ServerIP, Resource.ServerPort);
				
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(messagePacket);
			out.flush();
			
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/***
	 * find user
	 * return the User if find a user successfully
	 * return the null pointer otherwise. 
	 * @param userID
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static User findUser(int userID) throws ClassNotFoundException, IOException {
		User userResult = null;
		try {			
			QinMessagePacket messagePacket = new QinMessagePacket(Command.FINDUSER);
			
			User user = new User();
			user.setUid(userID);
			FindUserContainer findUserContainer = new FindUserContainer(user);
			
			messagePacket.setFindUserContainer(findUserContainer);
			QinMessagePacket findUserResultPacket = MessagePacketSender.sendPacket(messagePacket);
			
			if( findUserResultPacket.getCommand().equals(Command.GAINUSERINFO)) {
				userResult = findUserResultPacket.getFindUserContainer().getUser();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return userResult;
	}
	
	public static void addFriend(int sourceId, int friendId) {
		try {			
			QinMessagePacket messagePacket = new QinMessagePacket(Command.ADDFRIEND);
			AddFriendContainer addFriendContainer = new AddFriendContainer(sourceId, friendId);
			messagePacket.setAddFriendContainer(addFriendContainer);

			Socket socket = new Socket(Resource.ServerIP, Resource.ServerPort);
				
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(messagePacket);
			out.flush();
			
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void deleteFriend(int sourceId, int friendId) {
		try {			
			QinMessagePacket messagePacket = new QinMessagePacket(Command.DELETEFRIEND);
			DeleteFriendContainer deleteFriendContainer = new DeleteFriendContainer(sourceId, friendId);
			messagePacket.setDeleteFriendContainer(deleteFriendContainer);

			Socket socket = new Socket(Resource.ServerIP, Resource.ServerPort);
				
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(messagePacket);
			out.flush();
			
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/***
	 * find qun
	 * return the qun if find a qun successfully
	 * return the null pointer otherwise.
	 * @param qunID
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	public static Qun findQun(int qunID) throws ClassNotFoundException, IOException {
		Qun qunResult = null;
		try {			
			QinMessagePacket messagePacket = new QinMessagePacket(Command.FINDQUN);
			
			Qun qun = new Qun();
			qun.setQunID(qunID);
			FindQunContainer findQunContainer = new FindQunContainer(qun);
			
			messagePacket.setFindQunContainer(findQunContainer);
			QinMessagePacket findQunResultPacket = MessagePacketSender.sendPacket(messagePacket);
			
			if( findQunResultPacket.getCommand().equals(Command.GAINQUNINFO)) {
				qunResult = findQunResultPacket.getFindQunContainer().getQun();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qunResult;
	}
	
	// 
	/***
	 * create qun
	 * return the Qun ID if create a qun successfully
	 * return -1(Resource.CreateQunFailQunID) otherwise.
	 * @param qunOwnerId
	 * @param qunName
	 * @param qunDescription
	 * @return
	 */
	public static int createQun(int qunOwnerId, String qunName, String qunDescription) {
		int qunId = Resource.CreateQunFailQunID;
		try {			
			QinMessagePacket messagePacket = new QinMessagePacket(Command.CREATEQUN);
			
			Qun qun = new Qun();
			qun.setQunOwnerID(qunOwnerId);
			qun.setQunName(qunName);
			qun.setQunDescription(qunDescription);
			
			CreateQunContainer createQunContainer = new CreateQunContainer(qun);
			
			messagePacket.setCreateQunContainer(createQunContainer);
			QinMessagePacket createQunResultPacket = MessagePacketSender.sendPacket(messagePacket);
			
			System.out.println(createQunResultPacket.getCommand());
			
			if(createQunResultPacket.getCommand().equals(Command.CREATEQUNSUCCESS)) {
				qunId = (Integer.valueOf(createQunResultPacket.getResponseMsg())).intValue();
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return qunId;
	}
	
	public static void joinQun(int userId, int qunId) {
		try {			
			QinMessagePacket messagePacket = new QinMessagePacket(Command.JOININQUN);
			JoinQunContainer joinQunContainer = new JoinQunContainer(userId, qunId);
			messagePacket.setJoinQunContainer(joinQunContainer);

			Socket socket = new Socket(Resource.ServerIP, Resource.ServerPort);
				
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(messagePacket);
			out.flush();
			
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void exitQun(int userId, int qunId) {
		try {			
			QinMessagePacket messagePacket = new QinMessagePacket(Command.EXITQUN);
			ExitQunContainer exitQunContainer = new ExitQunContainer(userId, qunId);
			messagePacket.setExitQunContainer(exitQunContainer);

			Socket socket = new Socket(Resource.ServerIP, Resource.ServerPort);
				
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(messagePacket);
			out.flush();
			
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void modifyUserInfo(User user) {
		try {			
			QinMessagePacket messagePacket = new QinMessagePacket(Command.MODIFYUSERINFO);
			ModifyUserInfoContainer modifyUserInfoContainer = new ModifyUserInfoContainer(user);
			messagePacket.setModifyUserInfoContainer(modifyUserInfoContainer);

			Socket socket = new Socket(Resource.ServerIP, Resource.ServerPort);
				
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(messagePacket);
			out.flush();
			
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void modifyQunInfo(Qun qun) {
		try {			
			QinMessagePacket messagePacket = new QinMessagePacket(Command.MODIFYQUNINFO);
			ModifyQunInfoContainer modifyQunInfoContainer = new ModifyQunInfoContainer(qun);
			messagePacket.setModifyQunInfoContainer(modifyQunInfoContainer);

			Socket socket = new Socket(Resource.ServerIP, Resource.ServerPort);
				
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(messagePacket);
			out.flush();
			
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// the first parameter is the id of the user who want to add a friend
	// the second parameter is the id of the user who is requested to be added
	// the third parameter is a boolean value:
//	         the true value represents 'agree' and the false value represents 'disagree' 
	public static void respondAddFriendApplication(int sourceId, int userId, boolean ifAgree) {
		try {
			QinMessagePacket messagePacket = new QinMessagePacket(Command.RESPONDADDFRIENDAPPLICATION);
			AddFriendContainer afc = new AddFriendContainer(sourceId, userId);
			if (ifAgree) {
				afc.setState(AddFriendContainer.PASSED);
			} else {
				afc.setState(AddFriendContainer.REJECT);
			}
			messagePacket.setAddFriendContainer(afc);

			Socket socket = new Socket(Resource.ServerIP, Resource.ServerPort);
				
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(messagePacket);
			out.flush();
			
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// the first parameter is the id of the user who want to join a QUN
	// the second parameter is the id of the QUN
	// the third parameter is a boolean value:
//	         the true value represents 'agree' and the false value represents 'disagree' 
	public static void respondJoinQunApplication(int sourceId, int qunId, boolean ifAgree) {
		try {
			QinMessagePacket messagePacket = new QinMessagePacket(Command.RESPONDJOINQUNAPPLICATION);
			JoinQunContainer jqc = new JoinQunContainer(sourceId, qunId);
			if (ifAgree) {
				jqc.setState(JoinQunContainer.PASSED);
			} else {
				jqc.setState(JoinQunContainer.REJECT);
			}
			messagePacket.setJoinQunContainer(jqc);

			Socket socket = new Socket(Resource.ServerIP, Resource.ServerPort);
				
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(messagePacket);
			out.flush();
			
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
