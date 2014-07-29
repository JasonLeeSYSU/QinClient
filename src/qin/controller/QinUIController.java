package qin.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import qin.controller.handelThread.BusinessOperationHandel;
import qin.controller.handelThread.HeartBeatThread;
import qin.controller.handelThread.ReceiveApplicationResponseThread;
import qin.controller.handelThread.ReceiveApplicationThread;
import qin.model.Command;
import qin.model.QinMessagePacket;
import qin.model.Resource;
import qin.model.domainClass.Address;
import qin.model.domainClass.Message;
import qin.model.domainClass.Qun;
import qin.model.domainClass.User;
import qin.model.msgContainer.AddFriendContainer;
import qin.model.msgContainer.JoinQunContainer;
import qin.ui.CreateQunUI;
import qin.ui.LoginUI;
import qin.ui.MainUI;
import qin.ui.MessageUI;
import qin.ui.RegisterUI;
import qin.ui.SearchUI;
import qin.ui.ShowApplicationResponseUI;
import qin.ui.ShowApplicationUI;
import qin.ui.ShowQunInfoUI;
import qin.ui.ShowUserInfoUI;

public class QinUIController implements Runnable  {
	private static QinUIController SingleUIController = null;
	private static int ClientListenerPort = -1;
	private static String ServerIP = null;
	
	private User user = null;
	private List<User> onlineFriends = new ArrayList<User>();
	private List<User> offlineFriends = new ArrayList<User>();
	private List<Qun> quns = null;
	
	private LoginUI loginUI= null;
	private RegisterUI registerUI = null;
	private MainUI mainUI = null;
	private SearchUI searchUI = null;
	private CreateQunUI createQunUI = null;
	
	private List<MessageUI> PrivateMessageUIs = new ArrayList<MessageUI>();
	private List<MessageUI> QunMessageUIs = new ArrayList<MessageUI>();
	
	HeartBeatThread heartBeatThread = null;
	
	/***
	 * 把构造函数声明为private
	 * 只允许通过 getInstance() 获取单实例
	 */
	private QinUIController() {
		user = new User();
	}
	
	/***
	 * 单例模式
	 * @return
	 */
	public static QinUIController getInstance() {  
	       if (SingleUIController == null) {  
	    	   SingleUIController = new QinUIController();  
	       }  
	       
	       return SingleUIController;  
	}  
	
	/***
	 * 显示登录页面
	 */
	@Override
	public void run() {
		getLoginUI().showLoginUI();
		
		ServerIP = loadServerIP();
		if(ServerIP == null) {
			setServerIP(false);
		}
		
	}
	
	/***
	 * 创建注册界面
	 * 添加监听事件
	 * @return
	 */
    private RegisterUI getRegisterUI() {
    	if(registerUI == null) {
   	 		registerUI = new RegisterUI();
   	 
   	 		/***
   	 		 * 对"取消"按钮进行监听
   	 		 */
   	 		registerUI.getGiveUpButton().addActionListener(new ActionListener() {
   	 			public void actionPerformed(ActionEvent e) {
   	 				registerUI.hideRegisterUI();
   	 				getLoginUI().showLoginUI();
   	 			}
   	 		});
   	 		
   	 		/***
   	 		 * 对"注册"按钮进行监听
   	 		 */
   	 		registerUI.getRegisterButton().addActionListener(new ActionListener() {
   	 			public void actionPerformed(ActionEvent e) {
   	 				user = registerUI.getRegisterUserInfo();
   				
   	 				// 获取用户注册信息
   	 				String nickname = user.getNickName();
   	 				String password = getMD5(user.getPassword());
   	 				String email = user.getEmail();
   	 				int age = user.getAge();
   	 				String gender = user.getGender();
   	 				Address address = user.getAddress();
   	 				String headImage = user.getHeadImage();
   	 			
   	 				try {
   	 					int Uid = BusinessOperationHandel.register(nickname, password, email, age, gender, address, headImage);
   	 					
   	 					if(Uid != Resource.RegisterFailUserID) {
   	 						registerUI.showMessage(Uid);
   	 						registerUI.hideRegisterUI();
   	 						loginUI.showLoginUI();
   	 						loginUI.getIDField().setText(Uid + "");
   	 					} else {
   	 						registerUI.showErrorMessage();
   	 					}
   	 				
   	 				} catch (Exception ex) {
   	 					registerUI.showErrorMessage();
   	 					ex.printStackTrace();
   	 				} 
   	 			}
   	 		});
    	}
    	
    	return registerUI;
   }
    
    /***
     * 创建登录界面
     * 添加登录、注册监听事件
     * @return
     */
	private LoginUI getLoginUI() {
		if(loginUI == null) {
			loginUI = new LoginUI();
			
			/***
			 * 对"登录"按钮进行监听
			 */
			loginUI.getLoginButton().addActionListener(new ActionListener() {
				@SuppressWarnings("deprecation")
				@Override
				public void actionPerformed(ActionEvent e) {
						// 获取登录ID和密码
						int loginID = new Integer(loginUI.getIDField().getText());
						String password = getMD5(loginUI.getPasswordField().getText());
						
						try {
							QinMessagePacket loginResultPacket = BusinessOperationHandel.login(loginID, password, ClientListenerPort);
							
							if(loginResultPacket == null) { 
								// 网络异常
								loginUI.showNetWorkErrorMessage(); 
								
							} else if(loginResultPacket.getCommand().equals(Command.LOGINSUCCESS)) { 
								// 启动心跳进程
								heartBeatThread = new HeartBeatThread(loginID);
								heartBeatThread.start();
								
								// 成功登录
								user = loginResultPacket.getLoginContainer().getUser();
								quns = loginResultPacket.getQunListContainer().getQunList();
								ArrayList<User> myFriends = loginResultPacket.getUserListContainer().getUserList();
								
								for(int i = 0; i < myFriends.size(); i++) {
									if(myFriends.get(i).isUserOnline())
										onlineFriends.add(myFriends.get(i));
									else
										offlineFriends.add(myFriends.get(i));
								}
							
								createMainUI(user);
								mainUI.setOnlineFriend(onlineFriends);
								mainUI.setOfflineFriend(offlineFriends);
								mainUI.addGroup(quns);
							
								loginUI.hideLoginUI();
								mainUI.showMainUI();
								
								ArrayList<Message> offLineMsg = loginResultPacket.getMessageListContainer().getMessageList();
								ArrayList<AddFriendContainer> addFriendContainers = loginResultPacket.getAddFriendListContainer().getAddFriendList();
								ArrayList<JoinQunContainer> joinQunListContainers = loginResultPacket.getJoinQunListContainer().getJoinQunList();
								
								// 处理添加好友的请求、结果
								for(int i = 0; i < addFriendContainers.size(); i++) {
									if(addFriendContainers.get(i).getState() == AddFriendContainer.CHECKED) {
										 int sourceID = addFriendContainers.get(i).getSourceId();
										 Thread receiveAddFriendApplicationThread = new Thread(new ReceiveApplicationThread(sourceID));
										 receiveAddFriendApplicationThread.start();
									} else {
										 int addedUserID = addFriendContainers.get(i).getFriendId();
										 boolean isAdded = addFriendContainers.get(i).getState() == AddFriendContainer.PASSED;
										 
										 Thread receiveAddFriendResponseThread = new Thread(new ReceiveApplicationResponseThread(true,addedUserID, isAdded));
										 receiveAddFriendResponseThread.start();
									}
								}
								
								// 处理加入群的请求、结果
								for(int i = 0; i < joinQunListContainers.size(); i++) {
									if(joinQunListContainers.get(i).getState() == JoinQunContainer.CHECKED) {
										 int sourceID = joinQunListContainers.get(i).getUserId();
										 int addedQunID = joinQunListContainers.get(i).getQunId();
										
										 Thread receiveJoinQunApplicationThread = new Thread(new ReceiveApplicationThread(sourceID, addedQunID));
										 receiveJoinQunApplicationThread.start();
									} else {
										 int addedQunID = joinQunListContainers.get(i).getQunId();
										 boolean isAdded = joinQunListContainers.get(i).getState() == JoinQunContainer.PASSED;
										 
										Thread receiveJoinQunResponseThread = new Thread(new ReceiveApplicationResponseThread(false, addedQunID, isAdded));
										receiveJoinQunResponseThread.start();
									}
								}
								
								// 显示离线信息
								for(int i = 0; i < offLineMsg.size(); i++) {
									MessageUI messageUI = null;
									Message message =  offLineMsg.get(i);
									String msg = "";
									
									if(message.isQunMsg()) {	
										messageUI = getQunMessageUIByID(message.getDestinationId());
										if(messageUI != null) {
											List<User> qunUser = ((Qun)(messageUI.getObject())).getQunMember();
											String username = "匿名者 ";
											for(int j = 0; j < qunUser.size(); j++) {
												if(qunUser.get(j).getUid() == message.getSourceId()) {
													username = qunUser.get(j).getNickName() + "<" + message.getSourceId() + "> ";
													break;
												}
											}
											msg = username;
										} 
									} else {	
										messageUI = getPrivateMessageUIByID(message.getSourceId());
										if(messageUI != null) {
											msg = ((User)(messageUI.getObject())).getNickName() + " ";
										} 
									}
									
									if(messageUI != null) {
										msg += message.getDateTime() + "\n" + message.getDetail();
										msg += "\n\n";
										messageUI.getShowMessageTextArea().setText(messageUI.getShowMessageTextArea().getText() + msg);
										messageUI.getShowMessageTextArea().setCaretPosition(messageUI.getShowMessageTextArea().getText().length());
										
										messageUI.showMessageUI();
									} else {
										//System.out.println("找不到 目的主 的 MeaageUI");
									}
								}
								
							} else {
								loginUI.showErrorMessage(loginResultPacket.getResponseMsg());	
							}
						} catch (ClassNotFoundException | IOException e1) {
							if(heartBeatThread != null)
								heartBeatThread.stop();
							loginUI.showNetWorkErrorMessage();
						}
					}
    		});
			
			/***
			 * 对"注册"按钮进行监听
			 */
			loginUI.getRegeditButton().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					loginUI.hideLoginUI();
					getRegisterUI().showRegisterUI();
				}
    		});
			
			loginUI.getSettingLogo().addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						setServerIP(true);		
					}
				}
			});
		} 
		
		return loginUI;
	}
	
	private MainUI createMainUI(User _user) {
		if(mainUI == null) {
			this.user = _user;
			mainUI = new MainUI(this.user);
		
			// 关闭主界面时，退出客户端
			mainUI.getjFrame().addWindowListener(new java.awt.event.WindowAdapter() {
				@SuppressWarnings("deprecation")
				@Override
				public void windowClosing(java.awt.event.WindowEvent e) {
					try {
						BusinessOperationHandel.logout(user.getUid());
						heartBeatThread.stop();
					} catch (ClassNotFoundException | IOException e1) {
						//e1.printStackTrace();
					}
					
					System.exit(0);
				}
			});
        
			// 双击创建群Lable，弹出创建群的UI
			mainUI.getCreateQunLabel().addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						getCreateQunUI().showCreateQunUI();		
					}
				}
			});
		
			// 双击搜索Lable，弹出搜索UI
			mainUI.getSearchLabel().addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						getSearchUI().showSearchUI();
					}
				}
			});
		
			// 在好友列表和群列表上双击时，弹出聊天UI
			mainUI.getFriendsTree().addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) { 
					if (e.getClickCount() == 2) {
						
						JTree tree = (JTree) e.getSource();
						int rowLocation = tree.getRowForLocation(e.getX(), e.getY());
						TreePath treepath = tree.getPathForRow(rowLocation);
						if(treepath == null)
							return;
                    
						DefaultMutableTreeNode treenode = (DefaultMutableTreeNode) treepath.getLastPathComponent();
						Object obj =  treenode.getUserObject();
                   
						if(obj instanceof User && ((User)obj).getUid() != Resource.NotTreeNodeSign) {
							MessageUI messageUI = getPrivateMessageUIByID(((User)obj).getUid());
							if(messageUI != null) {
								messageUI.showMessageUI();
							}
							
						} else if(obj instanceof Qun && ((Qun)obj).getQunID() != Resource.NotTreeNodeSign) {
							MessageUI messageUI = getQunMessageUIByID(((Qun)obj).getQunID());
							if(messageUI != null) {
								messageUI.showMessageUI();
							}	
						}
					}
				}
			});
		
			// 在好友列表和群列表上右击选择“聊天”时，弹出聊天UI
			mainUI.getSendMessageMenuItem().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					TreePath treePath = mainUI.getFriendsTree().getSelectionPath();
					if(treePath != null) {
						DefaultMutableTreeNode selectNode=(DefaultMutableTreeNode)treePath.getLastPathComponent();
						Object obj = selectNode.getUserObject();
					
						if(obj instanceof User && ((User)obj).getUid() != Resource.NotTreeNodeSign) {
							MessageUI messageUI = getPrivateMessageUIByID(((User)obj).getUid());
							if(messageUI != null) {
								messageUI.showMessageUI();
							}
			        	
						} else if(obj instanceof Qun && ((Qun)obj).getQunID() != Resource.NotTreeNodeSign) {
							MessageUI messageUI = getQunMessageUIByID(((Qun)obj).getQunID());
							if(messageUI != null) {
								messageUI.showMessageUI();
							}
						}
					}
				}
			});
		
			// 在好友列表和群列表上右击选择“查看资料”时，弹出用户或者群的详细信息
			mainUI.getShowInfoMenuItem().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					TreePath treePath = mainUI.getFriendsTree().getSelectionPath();
					if(treePath != null) {
						DefaultMutableTreeNode selectNode=(DefaultMutableTreeNode)treePath.getLastPathComponent();
						Object obj = selectNode.getUserObject();
					
						if(obj instanceof User && ((User)obj).getUid() != Resource.NotTreeNodeSign) {
							ShowUserInfoUI showUserInfoUI = new ShowUserInfoUI(((User)obj));
							showUserInfoUI.showShowUserInfoUI();
						
						} else if(obj instanceof Qun && ((Qun)obj).getQunID() != Resource.NotTreeNodeSign) {
							ShowQunInfoUI showQunInfoUI = new ShowQunInfoUI((Qun)obj);
							showQunInfoUI.showQunInfoUI();
						}
					}
				}
			});
		} 
		
		return mainUI;
	}
	
	/***
	 * 创建查询界面
	 * 添加监听事件
	 * @return
	 */
	private SearchUI getSearchUI() {
		if(searchUI == null) {
			searchUI = new SearchUI();
			
			/***
			 * 对"查询"按钮添加监听事件
			 */
			searchUI.getFindButton().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					Object obj = searchUI.getSearchObj();
					
					if(obj instanceof User) { 
						// 查询用户
						try {
							searchUI.getFindButton().setVisible(false);
							searchUI.getAddButton().setVisible(true);
							
							User findUser  = BusinessOperationHandel.findUser(((User)obj).getUid());
							searchUI.showFindResult(findUser); // 如果成功找到一个用户，则显示其信息
						} catch (ClassNotFoundException | IOException e1) {
							searchUI.showFindResult(null);
						}
					} else {
						try {
							// 查询群
							searchUI.getFindButton().setVisible(false);
							searchUI.getAddButton().setVisible(true);
							
							Qun findQun = BusinessOperationHandel.findQun(((Qun)obj).getQunID(), false);
							searchUI.showFindResult(findQun);
						} catch (ClassNotFoundException | IOException e1) {
							searchUI.showFindResult(null);
						}	
					} 
				}
			});
			
			/***
			 * 对"添加"按钮添加监听事件
			 */
			searchUI.getAddButton().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					Object obj = searchUI.getAddObj();
					
					if(obj instanceof User) {
						// 申请添加好友
						if(getFriendInfoByID(((User)obj).getUid()) == null) {
							BusinessOperationHandel.addFriend(user.getUid(), ((User)obj).getUid());
							searchUI.showAddMessage();
						} else {
							searchUI.showRepeatAddMessage(((User)obj).getUid());
						}
							
					} else {
						// 申请加入群
						if(getQunInfoByID(((Qun)obj).getQunID()) == null) {
							BusinessOperationHandel.joinQun(user.getUid(), ((Qun)obj).getQunID());
							searchUI.showAddMessage();
						} else {
							searchUI.showRepeatAddMessage(((Qun)obj).getQunID());
						}
					}	
				}
			});
			
			
			searchUI.getAddButton().addMouseListener(new MouseAdapter() {
		    		@Override
		    		public void mouseExited(MouseEvent e) {
		    			searchUI.getAddButton().setEnabled(true);
		    		}
		    		
		    		public void mouseEntered(MouseEvent e) {
		    			Object obj = searchUI.getAddObj();
						
						if(obj instanceof User && ((User)obj).getUid() == user.getUid()) {
							 searchUI.getAddButton().setEnabled(false);
						} else {
		    	        	  searchUI.getAddButton().setEnabled(true);
		    	          }
		    		}
		    	});
		}
		
		return searchUI;
	}

	/***
	 * 为创建群UI添加监听事件
	 * @return
	 */
	private CreateQunUI getCreateQunUI() {
		
		if(createQunUI == null) {
			createQunUI = new CreateQunUI();
			
			createQunUI.getCreateQunButton().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					Qun createQun = createQunUI.getQunToCreate();
					int qunID = BusinessOperationHandel.createQun(user.getUid(), createQun.getQunName(), createQun.getQunDescription());
					
					if(qunID != Resource.CreateQunFailQunID) {
						// 成功创建群
						createQun.setQunOwnerID(user.getUid());
						createQun.setQunID(qunID);
						List<User> qunUser = new ArrayList<User>();
						qunUser.add(user);
						createQun.setQunMember(qunUser);
						
						addQun(createQun);
						createQunUI.showCreateQunID(createQun.getQunName(), qunID);
					} else {
						createQunUI.showErrorMessage();
					}
				}
			});
		}
		
		return createQunUI;
	}
		
	/***
	 * 根据好友ID在本地查找自己的好友
	 * @param FriendID
	 * @return
	 */
	private User getFriendInfoByID(int FriendID) {
		for(int i = 0; i < onlineFriends.size(); i++) {
			if(onlineFriends.get(i).getUid() == FriendID)
				return onlineFriends.get(i);
		}
		
		for(int i = 0; i < offlineFriends.size(); i++) {
			if(offlineFriends.get(i).getUid() == FriendID)
				return offlineFriends.get(i);
		}
		
		return null;
	}
	
	/***
	 * 根据群号在本地找自己加入的群
	 * @param GroupID
	 * @return
	 */
	private Qun getQunInfoByID(int GroupID) {
		for(int i = 0; i < quns.size(); i++) {
			if(quns.get(i).getQunID() == GroupID)
				return quns.get(i);
		}
		
		return null;
	}
	
	/***
	 * 根据 用户消息 返回对应的聊天界面
	 * @param user
	 * @return
	 */
	public MessageUI getPrivateMessageUIByID(int UserID) {
		for(int i = 0; i < PrivateMessageUIs.size(); i++) {
			if(PrivateMessageUIs.get(i).getObjectID() == UserID) {
				return PrivateMessageUIs.get(i);
			}
		}
		
		// 如果在PrivateMessageUIs找不到对应的UI，则新创建一个
		for(int i = 0; i < onlineFriends.size(); i++) {
			if(onlineFriends.get(i).getUid() == UserID) {
				MessageUI messageUI = new MessageUI(onlineFriends.get(i), user.getUid());
				PrivateMessageUIs.add(messageUI);
				return messageUI;
			}
		}
		
		for(int i = 0; i < offlineFriends.size(); i++) {
			if(offlineFriends.get(i).getUid() == UserID) {
				MessageUI messageUI = new MessageUI(offlineFriends.get(i),user.getUid());
				PrivateMessageUIs.add(messageUI);
				return messageUI;
			}
		}
		
		return null;
	}
	
	/***
	 * 根据 群信息 返回对应的群聊界面
	 * @param group
	 * @return
	 */
	public MessageUI getQunMessageUIByID(int GroupID) {
		for(int i = 0; i < QunMessageUIs.size(); i++) {
			if(QunMessageUIs.get(i).getObjectID() == GroupID) {
				return QunMessageUIs.get(i);
			}
		}
		
		// 如果在QunMessageUIs找不到对应的UI，则新创建一个
		for(int i = 0; i < quns.size(); i++) {
			if(quns.get(i).getQunID() == GroupID) {
				MessageUI messageUI = new MessageUI(quns.get(i), user.getUid());
				QunMessageUIs.add(messageUI);
				return messageUI;
			}
		}
		
		return null;
	}
	
	/***
	 * 有请求者请求 “添加好友” 
	 * @param sourceID
	 */
	public void showAddFriendApplication(int sourceID) {
		try {
			User sourceUser = BusinessOperationHandel.findUser(sourceID);
			if(sourceUser != null) {
				ShowApplicationUI showApplicationUI = new ShowApplicationUI(sourceUser, user.getUid());
				showApplicationUI.getJFrame();
			}
		} catch (ClassNotFoundException | IOException e) {
			//e.printStackTrace();
		}
	}
	
	/***
	 * 有请求者请求 “加入群”
	 * @param sourceID
	 * @param qunID
	 */
	public void showJoinQunApplication(int sourceID, int qunID) {
		try {
			User sourceUser = BusinessOperationHandel.findUser(sourceID);
			Qun qun = getQunInfoByID(qunID);
			
			if(qun == null || sourceUser == null || qun.getQunOwnerID() != user.getUid()) {
				return ;
			}
			
			ShowApplicationUI showApplicationUI = new ShowApplicationUI(sourceUser, qun.getQunID(), qun.getQunName());
			showApplicationUI.getJFrame();	
		} catch (ClassNotFoundException | IOException e) {
			//e.printStackTrace();
		}
	}
	
	/***
	 * 查看“添加好友”的结果 
	 * @param sourceID
	 */
	public void showAddFriendApplicationResponse(int addedId, boolean isSuccess) {
		try {
			User addedUser = BusinessOperationHandel.findUser(addedId);
			
			if(addedUser != null) {		
				if(isSuccess)
					addFriend(addedUser); 
				
				ShowApplicationResponseUI showApplicationResponseUI = new ShowApplicationResponseUI(addedUser, user.getUid(), isSuccess);
				showApplicationResponseUI.getJFrame();
			} else {
				//System.out.println("showAddFriendApplicationResponse addedUser 不存在");
			}
		} catch (ClassNotFoundException | IOException e) {
			//e.printStackTrace();
		}
	}
	
	/***
	 * 查看“加入群”请求结果
	 * @param sourceID
	 * @param qunID
	 */
	public void showJoinQunApplicationResponse(int addedQunID, boolean isSuccess) {
		try {
			Qun addedQun = null;
				
			if(isSuccess)		
				addedQun = BusinessOperationHandel.findQun(addedQunID, true);
			else 
				addedQun = BusinessOperationHandel.findQun(addedQunID, false);
			
			if(addedQun != null) {
				if(isSuccess)
					addQun(addedQun);
				
				ShowApplicationResponseUI showApplicationResponseUI = new ShowApplicationResponseUI(addedQun, user.getUid(), isSuccess);
				showApplicationResponseUI.getJFrame();
			} else {
				//System.out.println("showJoinQunApplicationResponse addedQun 不存在");
			}
		} catch (ClassNotFoundException | IOException e) {
			//e.printStackTrace();
		}
	}
	
	/***
	 * 好友登录
	 * 设置好友的IP地址和端口，用于P2P文件传输
	 * @param ID
	 * @param IP
	 * @param Port
	 */
	public void friendOnline(int ID, String IP, int Port) {
		User friend = getFriendInfoByID(ID);
		if(friend != null && !friend.isUserOnline()) {
			offlineFriends.remove(friend);
			onlineFriends.add(friend);
			friend.online();
			friend.setIPAddr(IP);
			friend.setPort(Port);
			if(getPrivateMessageUIByID(ID) != null) {
				getPrivateMessageUIByID(ID).UserLoginUI();
			}
			
			mainUI.friendOnlining(ID);
		}
	} 

	/***
	 * 好友下线
	 * @param ID
	 */
	public void friendOffline(int ID) {
		User friend = getFriendInfoByID(ID);
		if(friend != null && friend.isUserOnline()) {
			onlineFriends.remove(friend);
			offlineFriends.add(friend);
			friend.offline();
			if(getPrivateMessageUIByID(ID) != null)
				getPrivateMessageUIByID(ID).UserLogoutUI();
			
			mainUI.friendOfflining(ID);
		}
	}
	
	/***
	 * 添加新的好友
	 * @param newUser
	 */
	public void addFriend(User newUser) {
		if(getFriendInfoByID(newUser.getUid()) == null) {
			if(newUser.isUserOnline()) {
				onlineFriends.add(newUser);
				mainUI.setOnlineFriend(newUser);
			} else {
				offlineFriends.add(newUser);
				mainUI.setOfflineFriend(newUser);
			}
		}
	}
	
	/***
	 * 添加新群
	 * @param newQun
	 */
	public void addQun(Qun newQun) {
		if(getQunInfoByID(newQun.getQunID()) == null) {
			quns.add(newQun);
			mainUI.addGroup(newQun);
		} else {
			//System.out.println("群已经存在，添加失败");
		}
	}

	/***
	 * 同意一个用户加入自己的群后，在本地数据中加入被添加用户的信息
	 * @param qunID
	 * @param addedUser
	 */
	public void addUserIntoMyQun(int qunID, User addedUser) {
		Qun qun = getQunInfoByID(qunID);
		
		if(qun != null && qun.getQunOwnerID() == user.getUid()) {
			for(int i = 0; i < qun.getQunMember().size(); i++) {
				if(qun.getQunMember().get(i).getUid() == addedUser.getUid())
					return ;
			}
	
			qun.getQunMember().add(addedUser);
		}
	}
	
	/***
	 * 从文件中读出服务器IP地址
	 * @return
	 */
	private String loadServerIP() {
		String ip = null;
		File ipFile = new File(".IP.txt");
		
		 if(ipFile.isFile() && ipFile.exists()){ //判断文件是否存在
			 try {
				@SuppressWarnings("resource")
				BufferedReader reader = new BufferedReader(new FileReader(ipFile));
				ip = reader.readLine();
				reader.close();
			} catch (IOException e) {
				
			}
		 }
		 
		System.out.println("服务器IP地址：" + ip);
		return ip;
	}
	
	/***
	 * 把服务器IP地址保存在本地文件中
	 * @param ip
	 */
	private void saveServerIP(String ip) {
	   
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(".IP.txt", false));
			writer.write(ip);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			
		}

	}
	
	/***
	 * 
	 * @param isCanCancel
	 */
	private void setServerIP(boolean isCanCancel) {
		
		do {
			ServerIP = getLoginUI().setServerIP();
			
			if(ServerIP == null && isCanCancel)
				break;
			else if(ServerIP != null){
				saveServerIP(ServerIP);
				break;
			}
		} while(true);
		
		
	}
	
	public String getServerIP() {
		return ServerIP;
	}
	
	public void setClientListenerPort(int port) {
		ClientListenerPort = port;
	}
	
	/***
	 * MD5 用于加密密码
	 * @param password
	 * @return
	 */
	public String getMD5(String password) {
		  String s = null;
		  char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',  'e', 'f'};
		  
		  try {
			  java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			  md.update(password.getBytes());
		    
			  byte tmp[] = md.digest();                                                  
			  char str[] = new char[16 * 2]; 
		    
			  int k = 0;                              
			  for(int i = 0; i < 16; i++) {       
				  byte byte0 = tmp[i];             
				  str[k++] = hexDigits[byte0 >>> 4 & 0xf];  
				  str[k++] = hexDigits[byte0 & 0xf];            
			  }
			  s = new String(str);                              
		   	}  catch( Exception e ) {
			 //  e.printStackTrace();
		   }
		   return s;
	}
}
