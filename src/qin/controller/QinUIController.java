package qin.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import qin.controller.handelThread.ReceiveMessageThread;
import qin.model.Command;
import qin.model.QinMessagePacket;
import qin.model.Resource;
import qin.model.domainClass.Address;
import qin.model.domainClass.Message;
import qin.model.domainClass.Qun;
import qin.model.domainClass.User;
import qin.model.msgContainer.AddFriendContainer;
import qin.model.msgContainer.JoinQunContainer;
import qin.testcase.StaticTestCase;
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
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		getLoginUI().showLoginUI();
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
   				
   	 				System.out.println("Register ...................");
   	 				System.out.println("name: " + user.getNickName());
   	 				System.out.println("password: " + user.getPassword());
   	 				System.out.println("email: " + user.getEmail());
   	 				System.out.println("gender: " + user.getGender());
   	 				System.out.println(user.getAddress().getProvince() + " " + user.getAddress().getCity());
   	 				System.out.println("head image: " + user.getHeadImage());
   	 				System.out.println("Register ...................");
   				
   	 				String nickname = user.getNickName();
   	 				String password = user.getPassword();
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
						System.out.println("登录");
						
						int loginID = new Integer(loginUI.getIDField().getText());
						String password = loginUI.getPasswordField().getText();
						
						heartBeatThread = new HeartBeatThread(loginID);
						heartBeatThread.start();
						
						try {
							QinMessagePacket loginResultPacket = BusinessOperationHandel.login(loginID, password, ClientListenerPort);
							
							if(loginResultPacket == null) {
								heartBeatThread.stop();
								loginUI.showNetWorkErrorMessage();
								
							} else if(loginResultPacket.getCommand().equals(Command.LOGINSUCCESS)) {
								
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
								ArrayList<JoinQunContainer> joinQunContainers = loginResultPacket.getJoinQunListContainer().getJoinQunList();
								
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
								
								
								for(int i = 0; i < joinQunContainers.size(); i++) {
									if(joinQunContainers.get(i).getState() == JoinQunContainer.CHECKED) {
										 int sourceID = joinQunContainers.get(i).getUserId();
										 int addedQunID = joinQunContainers.get(i).getQunId();
										 
										 Thread receiveJoinQunApplicationThread = new Thread(new ReceiveApplicationThread(sourceID, addedQunID));
										 receiveJoinQunApplicationThread.start();
									} else {
										 int addedQunID = joinQunContainers.get(i).getQunId();
										 boolean isAdded = joinQunContainers.get(i).getState() == JoinQunContainer.PASSED;
										 
										Thread receiveJoinQunResponseThread = new Thread(new ReceiveApplicationResponseThread(false, addedQunID, isAdded));
										receiveJoinQunResponseThread.start();
									}
								}
								
								for(int i = 0; i < offLineMsg.size(); i++) {
									MessageUI messageUI = null;
									Message message =  offLineMsg.get(i);
									String msg = "";
									
									if(message.isQunMsg()) {
										System.out.println("离线群信息 coming");
										
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
										System.out.println("离线群信息 coming");
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
										System.out.println("找不到 目的主 的 MeaageUI");
									}
								}
								
							} else {
								heartBeatThread.stop();
								loginUI.showErrorMessage(loginResultPacket.getResponseMsg());	
							}
						} catch (ClassNotFoundException | IOException e1) {
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
					System.out.println("注册");
					
					loginUI.hideLoginUI();
					getRegisterUI().showRegisterUI();
				}
    		});
		} 
		
		return loginUI;
	}
	
	private MainUI createMainUI(User _user) {
		if(mainUI == null) {
			this.user = _user;
		
			mainUI = new MainUI(this.user);
		
			mainUI.getjFrame().addWindowListener(new java.awt.event.WindowAdapter() {
				@Override
				public void windowClosing(java.awt.event.WindowEvent e) {
					try {
						System.out.println("退出");
						BusinessOperationHandel.logout(user.getUid());
						//heartBeatThread.stop();
					} catch (ClassNotFoundException | IOException e1) {
						e1.printStackTrace();
					}
					
					System.exit(0);
				}
			});
        
			mainUI.getCreateQunLabel().addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						getCreateQunUI().showCreateQunUI();		
					}
				}
			});
		
			mainUI.getSearchLabel().addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						getSearchUI().showSearchUI();
					}
				}
			});
		
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
						System.out.println("is User" + ((User)obj).getUid());
						try {
							searchUI.getFindButton().setVisible(false);
							searchUI.getAddButton().setVisible(true);
							
							User findUser  = BusinessOperationHandel.findUser(((User)obj).getUid());
							searchUI.showFindResult(findUser);
						} catch (ClassNotFoundException | IOException e1) {
							searchUI.showFindResult(null);
						}
						
					} else {
						System.out.println("is Group" + ((Qun)obj).getQunID());
						try {
							searchUI.getFindButton().setVisible(false);
							searchUI.getAddButton().setVisible(true);
							
							Qun findQun = BusinessOperationHandel.findQun(((Qun)obj).getQunID(), false);
							searchUI.showFindResult(findQun);
						} catch (ClassNotFoundException | IOException e1) {
							searchUI.showFindResult(null);;
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
						if(getFriendInfoByID(((User)obj).getUid()) == null) {
							BusinessOperationHandel.addFriend(user.getUid(), ((User)obj).getUid());
							searchUI.showAddMessage();
						} else {
							searchUI.showRepeatAddMessage(((User)obj).getUid());
						}
							
					} else {
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
		    			// TODO Auto-generated method stub
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
	
	private CreateQunUI getCreateQunUI() {
		
		if(createQunUI == null) {
			createQunUI = new CreateQunUI();
			
			createQunUI.getCreateQunButton().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e){
					Qun createQun = createQunUI.getQunToCreate();
					int qunID = BusinessOperationHandel.createQun(user.getUid(), createQun.getQunName(), createQun.getQunDescription());
					
					if(qunID != Resource.CreateQunFailQunID) {
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
	 * 根据好友ID查找好友
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
	 * 根据群号找群
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
			// TODO Auto-generated catch block
			e.printStackTrace();
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
			
			if(qun == null || sourceUser == null || qun.getQunOwnerID() != user.getUid())
				return ;
				
			ShowApplicationUI showApplicationUI = new ShowApplicationUI(sourceUser, qun.getQunID(), qun.getQunName());
			showApplicationUI.getJFrame();	
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				System.out.println("showAddFriendApplicationResponse addedUser");
				
				if(addedUser != null)
					addFriend(addedUser); 
				
				ShowApplicationResponseUI showApplicationResponseUI = new ShowApplicationResponseUI(addedUser, user.getUid(), isSuccess);
				showApplicationResponseUI.getJFrame();
			} else {
				System.out.println("showAddFriendApplicationResponse addedUser 不存在");
			}
			
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
				System.out.println("showJoinQunApplicationResponse addedQun 不存在");
			}
	
			
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void friendOnline(int ID, String IP, int Port) {
		User friend = getFriendInfoByID(ID);
		System.out.println("用户登录了---" + ID);
		if(friend != null && !friend.isUserOnline()) {
			offlineFriends.remove(friend);
			onlineFriends.add(friend);
			friend.online();
			friend.setIPAddr(IP);
			friend.setPort(Port);
			System.out.println("用户登录了---");
			if(getPrivateMessageUIByID(ID) != null) {
				System.out.println("用户登录了--- 改界面");
				getPrivateMessageUIByID(ID).UserLoginUI();
			}
			
			mainUI.friendOnlining(ID);
		}
	} 
	
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
	
	public void addQun(Qun newQun) {
		if(getQunInfoByID(newQun.getQunID()) == null) {
			quns.add(newQun);
			mainUI.addGroup(newQun);
		} else {
			System.out.println("群已经存在，添加失败");
		}
	}

	public void setClientListenerPort(int port) {
		ClientListenerPort = port;
	}
}
