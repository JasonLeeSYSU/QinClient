package qin.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import qin.controller.handelThread.BusinessOperationHandel;
import qin.controller.handelThread.SendFileThread;
import qin.model.Resource;
import qin.model.domainClass.Qun;
import qin.model.domainClass.User;

public class MessageUI {
	private int MessageUIWidth = Resource.MessageUIWidth;
	private int MessageUIHeight = Resource.MessageUIHeight;
	
	private boolean isUserMessage = true; 
	private int sourceID;
	private User user = null;
	private Qun group = null;
	
	private JFrame jFrame = null;
	private JTextArea inputTextArea = null;
	private JTextArea showMessageTextArea = null;
	private JLabel ImageLabel = null;
	private JButton sendButton = null;
	private JButton giveUpButton = null;
	private JLabel SendFileLabel = null;
	private JButton agreeReceiveButton = null;
	private JButton refuseReceiveButton = null;
	private JProgressBar progressBar = null;
	
	/***
	 * 创建信息界面
	 * @param obj
	 * @param sourceID
	 */
	public MessageUI(Object obj, int sourceID) {
		this.sourceID = sourceID;
		if(obj instanceof User) {
			isUserMessage = true;
			user = (User)obj;
		} else {
			isUserMessage = false;
			group = (Qun)obj;
		}
	}
	
	/***
	 * 显示消息界面
	 */
	public void showMessageUI() {
		getjFrame().setVisible(true);
	}
	
	/***
	 * 隐藏信息界面
	 */
	public void hideMessageUI() {
		getjFrame().setVisible(false);
	}
    
	/***
	 * 返回接收方的对象
	 * 可以返回User 或者 Qun
	 * @return
	 */
	public Object getObject() {
		if(isUserMessage)
			return user;
	else 
			return group;
	}
	
	/***
	 * 返回接收方ID
	 * 可以是群ID或者是用户ID
	 * @return
	 */
	public int getObjectID() {
		if(isUserMessage)
			return user.getUid();
		else 
			return group.getQunID();
	}
	
    /**
     * 初始化窗体
     */
    private JFrame getjFrame() {
        if (jFrame == null) {
            jFrame = new JFrame("Qin聊天");
            jFrame.setResizable(false);
            jFrame.setSize(new Dimension(MessageUIWidth, MessageUIHeight));
           
            Toolkit toolkit = jFrame.getToolkit();
            Dimension screen = toolkit.getScreenSize();
            int toLeft = screen.height < MessageUIHeight  ? 0 : (screen.height -MessageUIHeight) / 2;
            int toTop = screen.width < MessageUIWidth  ? 0 : (screen.width - MessageUIWidth) / 2;
            jFrame.setBounds(toTop, toLeft, MessageUIWidth, MessageUIHeight);
            
            jFrame.add(getJPanel());
            jFrame.setVisible(false);
            
            // 点击关闭聊天窗口，实际是隐藏窗口
            jFrame.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                	jFrame.setVisible(false);
                }
            });
        }
        
        return jFrame;
    }
    
  
    /**
     * 初始化 jPanel
     */
    public JPanel getJPanel() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(null);
        jPanel.setEnabled(true);
        
        String ImagePath = isUserMessage ? ((user.isUserOnline() ? Resource.OnLineHeadImagePath : Resource.OffLineHeadImagePath) + user.getHeadImage()) : Resource.QunLogo;
        jPanel.add(setAndGetHeadImageLabel(ImagePath ));
      
        jPanel.add(getNameLabel());
        jPanel.add(getIDLabel());
        jPanel.add(getAgreeReceiveButton());
        jPanel.add(getRefuseReceiveButton());
        jPanel.add(getProgressBar());
        
        if(isUserMessage)
        	jPanel.add(getSendFileLabel());
        
       jPanel.add(getShowMessageTextAreaScrollPane());
       jPanel.add(getInputTextAreaScrollPane());
       jPanel.add(getSendButton());
       jPanel.add(getGiveUpButton());
       
        return jPanel;
    }
	
    /***
     * 设置聊天界面的头像
     * 用户头像或者群头像
     * @param ImagePath
     * @return
     */
    private JLabel setAndGetHeadImageLabel(String ImagePath) {
    	if(ImageLabel == null) {
    		ImageLabel = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(ImagePath))));
    		ImageLabel.setBounds(new Rectangle(20, 10, Resource.HeadImaageWidth, Resource.HeadImaageHeight));
    	
    		// 点击头像，弹出用户或者群的详细信息
    		ImageLabel.addMouseListener(new MouseAdapter() {
    			@Override
    			public void mouseClicked(MouseEvent e) {
    				if (e.getClickCount() == 2) {
    					if(isUserMessage) {	
    						ShowUserInfoUI showUserInfoUI = new ShowUserInfoUI(user);
    						showUserInfoUI.showShowUserInfoUI();
    					} else {
    						ShowQunInfoUI showQunInfoUI = new ShowQunInfoUI(group);
    						showQunInfoUI.showQunInfoUI();
    					}
    				}
    			}
    		});
    	} else {
    		ImageLabel.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(ImagePath))));
    	}
    	
    	
    	
    	return ImageLabel;
    }
    
    /***
     * 接收方的名称
     * @return
     */
    private JLabel getNameLabel() {
        String Name = isUserMessage ? user.getNickName() : group.getQunName();
        
       	JLabel NameLabel = new JLabel();
    	NameLabel.setBounds(new Rectangle(20+Resource.HeadImaageWidth+10, 10, 200, 20));
    	NameLabel.setForeground(Color.BLACK);
    	NameLabel.setText(Name);
      	
      	return NameLabel;
    }
    
    /***
     * 接收方的ID
     * @return
     */
    private JLabel getIDLabel() {
        String ID = isUserMessage ? user.getUid() + "" : group.getQunID() + "";

       	JLabel IDLabel = new JLabel();
    	IDLabel.setBounds(new Rectangle(20+Resource.HeadImaageWidth+10, 30, 200, 20));
    	IDLabel.setForeground(Color.BLACK);
    	IDLabel.setText(ID);
      	
      	return IDLabel;
    }
    
    /***
     * 发送文件图标
     * 只有在在线好友之间才能发送文件
     * @return
     */
    public JLabel getSendFileLabel() {
    	if(SendFileLabel == null) {
    		String SendFileImagePath = Resource.SendFilePicture;
    		SendFileLabel = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(SendFileImagePath))));
    		SendFileLabel.setBounds(new Rectangle(Resource.MessageUIWidth - 2*Resource.HeadImaageWidth, 10, Resource.HeadImaageWidth, Resource.HeadImaageHeight));
    		SendFileLabel.setVisible(user.isUserOnline());
    		
    		SendFileLabel.addMouseListener(new MouseAdapter() {
    			@Override
    			public void mouseClicked(MouseEvent e) {
    				if (e.getClickCount() == 2) {
    					String fileName = showSendFileChooser();
    					
    					if(fileName != null) { // 已经选择文件
    						File file = new File(fileName);
    						if(file.isFile()) {
    							Thread sendFileThread = new Thread(new SendFileThread(sourceID, getObjectID(), fileName));
    							sendFileThread.start();
    						} else {
    							sendNoSupportFile();
    						}
    					}
    				}
    			}
    		});
    	
    	}
    	
    	return SendFileLabel;
    }
    
    /***
     * 文件接收方的“接收”按钮
     * @return
     */
	public JButton getAgreeReceiveButton() {
		if(agreeReceiveButton == null) {
			agreeReceiveButton = new JButton("接收");
			agreeReceiveButton.setBounds(new Rectangle(Resource.HeadImaageWidth*13/2+10, 10, Resource.HeadImaageWidth+20, Resource.HeadImaageHeight-5));
			agreeReceiveButton.setVisible(false);
		}
		
		return agreeReceiveButton;
	}
	
	 /***
	  * 文件接收方的“拒绝”按钮
	  * @return
	  */
	public JButton getRefuseReceiveButton() {
		if(refuseReceiveButton == null) {
			refuseReceiveButton = new JButton("拒绝");
			refuseReceiveButton.setBounds(new Rectangle(Resource.HeadImaageWidth*15/2+30, 10, Resource.HeadImaageWidth+20, Resource.HeadImaageHeight-5));
			refuseReceiveButton.setVisible(false);
		}
		return refuseReceiveButton;
	}
	
	/***
	 * 文件传输进度条
	 * @return
	 */
    public JProgressBar getProgressBar() {
    	if(progressBar == null) {
    		progressBar = new JProgressBar(0, 100);
    		progressBar.setValue(0);
    		progressBar.setStringPainted(true);
    		progressBar.setBounds(new Rectangle(4*Resource.HeadImaageWidth, 10, Resource.HeadImaageWidth*5/2, Resource.HeadImaageHeight));
    		progressBar.setVisible(false);
    	}
    	
    	return progressBar;
    }
    
    /***
     * 聊天信息展示框的滑动条
     * @return
     */
    private JScrollPane getShowMessageTextAreaScrollPane() {
        JScrollPane  jScrollPane = new JScrollPane();
        jScrollPane.setBounds(0, Resource.MessageUIHeight*1/8 , Resource.MessageUIWidth-7, Resource.MessageUIHeight*8/15 + 12);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.getViewport().add(getShowMessageTextArea());
        
        return jScrollPane;
    }
    
    /***
     * 聊天信息展示框
     * @return
     */
    public JTextArea getShowMessageTextArea() {
        if (showMessageTextArea == null) {
        	showMessageTextArea = new JTextArea();
        	showMessageTextArea.setEditable(false);
        	showMessageTextArea.setFocusCycleRoot(true);
        	showMessageTextArea.setLineWrap(true); 			
        }
        
        return showMessageTextArea;
    }
    
    /***
     * 聊天信息输入框的滑动条
     * @return
     */
    private JScrollPane getInputTextAreaScrollPane() {
        JScrollPane  jScrollPane = new JScrollPane();
        jScrollPane.setBounds(25, Resource.MessageUIHeight*7/10, Resource.MessageUIWidth-50, Resource.MessageUIHeight/6);
        jScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.getViewport().add(getInputTextArea());
        
        return jScrollPane;
    }
    
    /***
     * 聊天信息输入框
     * @return
     */
    public JTextArea getInputTextArea() {
        if (inputTextArea == null) {
        	inputTextArea = new JTextArea();
        	inputTextArea.setFocusCycleRoot(true);
            inputTextArea.setLineWrap(true);
            
            inputTextArea.addKeyListener(new KeyAdapter(){
         		public void keyTyped(KeyEvent e) {
         			int keyChar = e.getKeyChar();
         			
         			// 按 Enter 发送消息
         			if(keyChar == '\n') {
         				String msg =  inputTextArea.getText();
     					msg = msg.substring(0, msg.length()-1);
     					
         				if(inputTextArea.getText().trim().length() > 0 && !inputTextArea.getText().trim().equals("\n")) {
         					
         					inputTextArea.setText("");
         					String time = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date());
         					showMessageTextArea.setText(showMessageTextArea.getText() + "我  " + time  + "\n" + msg + "\n\n");

         					int destinationId = getObjectID();
         					boolean isQunMsg = !isUserMessage;
         					BusinessOperationHandel.sendMessage(sourceID, destinationId, msg, isQunMsg);
         					
         				} else {
         					inputTextArea.setText("");	
         				}
         			}
         				
         			// 限制输入最大字数
         			if(inputTextArea.getText().length() > Resource.MessageMaxCharacter) {
         				e.consume(); 
         			}
         		}
         	});
        }
        
        return inputTextArea;
    }
    
    /***
     * 为“发送”按钮添加监听事件
     * @return
     */
    public JButton getSendButton() {
    	if(sendButton == null) {
    		sendButton = new JButton("发送");
    		sendButton.setBounds(new Rectangle(MessageUIWidth*5/15, MessageUIHeight*13/15+3, 60, 33));
    		
    		// 聊天信息非空才能发送
    		sendButton.addMouseListener(new MouseAdapter() {
    			public void mouseEntered(MouseEvent e) {
    				if(getInputTextArea().getText().trim().length() == 0)
    					sendButton.setEnabled(false);
    			}
       			public void mouseExited(MouseEvent e) {
    					sendButton.setEnabled(true);
    			}	
    		});
    		
    		// 按“发送”按钮发送聊天信息
    		sendButton.addActionListener(new ActionListener() {
    			@Override
    			public void actionPerformed(ActionEvent e) {
    					String msg = getInputTextArea().getText();
    					
    					if(msg.trim().length() > 0) {
    						String time = (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(new Date());
    						getShowMessageTextArea().setText(getShowMessageTextArea().getText() + "我  " + time  + "\n" + msg + "\n\n");
    						getInputTextArea().setText("");	
    					
         					int destinationId = getObjectID();
         					String detail = msg;
         					boolean isQunMsg = !isUserMessage;
         					BusinessOperationHandel.sendMessage(sourceID, destinationId, detail, isQunMsg);
    					}
    			}
    		});
    	}
    	return sendButton;
    }
    
    /***
     * 清空输入框中的内容
     * @return
     */
    private JButton getGiveUpButton() {
    	if(giveUpButton == null) {
    		giveUpButton = new JButton("清空");
    		giveUpButton.setBounds(new Rectangle(MessageUIWidth*8/15, MessageUIHeight*13/15+3, 60, 33));
    		
    		giveUpButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					getInputTextArea().setText("");
				}
    			
    		});
    	}
    	return giveUpButton;
    }
    
    /***
     * 选择要发送的文件
     * @return
     */
    public String showSendFileChooser() {
		JFileChooser Dialog = new JFileChooser();
		Dialog.setDialogTitle("请选择文件");
		
		int sign = Dialog.showOpenDialog(null);
		if(JFileChooser.APPROVE_OPTION == sign){
			return Dialog.getSelectedFile() + "";
			
		} else {
			return null;
		}
    }
    
    /***
     * 选择接收文件的存放路径
     * @return
     */
    public String showSaveFileChooser() {
    	String folder = null;
    	do {
    		JFileChooser saveFileChooser = new JFileChooser("选择存放的文件夹");
    		saveFileChooser.setFileSelectionMode(JFileChooser.SAVE_DIALOG | JFileChooser.DIRECTORIES_ONLY); 
    		saveFileChooser.showDialog(null,null);  
    		
    		if(saveFileChooser.getSelectedFile() == null || saveFileChooser.getSelectedFile().isDirectory()) {
    			folder = saveFileChooser.getSelectedFile() + "";
    			
    			break;
    		}  else {
    			JOptionPane.showMessageDialog(null, saveFileChooser.getSelectedFile() + " 不是一个文件夹", "文件夹路径错误", JOptionPane.DEFAULT_OPTION);
    		}
    	} while(true);
    	
    	return folder; 
    }
    
    /***
     * 更改UI
     * 发送方等待对方接收文件
     */
    public void waitForSendFile() {
    	getSendFileLabel().setVisible(false);
    	getProgressBar().setValue(0);
    	getProgressBar().setVisible(true);
    	
    	String msg = "\t发送文件:正在等待对方接收文件\n\n";
    	getShowMessageTextArea().setText(getShowMessageTextArea().getText() + msg);
    }
    
    /***
     * 更改UI
     * 发送方完成文件发送
     */
    public void finishSendFile() {
    	getSendFileLabel().setVisible(true);
    	getProgressBar().setVisible(false);
    	
       	String msg = "\t发送文件:已经成功发送文件\n\n";
    	getShowMessageTextArea().setText(getShowMessageTextArea().getText() + msg);
    }
    
    /***
     * 更改UI
     * 发送方被拒绝发送文件
     */
    public void giveUpSend() {
       	getSendFileLabel().setVisible(true);
    	getProgressBar().setVisible(false);
    	
    	String msg = "\t发送文件:对方拒绝接收文件\n\n";
    	getShowMessageTextArea().setText(getShowMessageTextArea().getText() + msg);
    }
    
    /***
     * 更改UI
     * 文件传输发生异常
     */
    public void sendFileError() {
       	getSendFileLabel().setVisible(true);
    	getProgressBar().setVisible(false);
    	
    	String msg = "\t文件传输: 网络异常，请稍后再试\n\n";
    	getShowMessageTextArea().setText(getShowMessageTextArea().getText() + msg);
    }
    
    /***
     * 更改UI
     * 接收方等待接收
     */
    public void waitForReceiveFile() {
    	getSendFileLabel().setVisible(false);
    	
    	getAgreeReceiveButton().setVisible(true);
    	getRefuseReceiveButton().setVisible(true);
    	getAgreeReceiveButton().setEnabled(true);
    	getRefuseReceiveButton().setEnabled(true);
    	getProgressBar().setValue(0);
    	getProgressBar().setVisible(true);
    	
    	String msg = "\t接收文件: 好友 "+  user.getNickName() + " 给您发送文件 \n\n";
    	getShowMessageTextArea().setText(getShowMessageTextArea().getText() + msg);
    }
    
    /*** 
     * 更改UI
     * 接收方正在接收文件
     */
    public void ReceivingFile() {
    	getAgreeReceiveButton().setVisible(true);
    	getRefuseReceiveButton().setVisible(true);
    	getAgreeReceiveButton().setEnabled(false);
    	getRefuseReceiveButton().setEnabled(false);
    	
     	String msg = "\t接收文件: 正在接收文件 \n\n";
    	getShowMessageTextArea().setText(getShowMessageTextArea().getText() + msg);
    }
    
    /*** 
     * 更改UI
     * 接收方接收文件完毕
     */
    public void finishReceive() {
    	getSendFileLabel().setVisible(true);
    	
    	getAgreeReceiveButton().setVisible(false);
    	getRefuseReceiveButton().setVisible(false);
    	getProgressBar().setVisible(false);
    	
    	String msg = "\t接收文件:  已接收完文件 \n\n";
    	getShowMessageTextArea().setText(getShowMessageTextArea().getText() + msg);
    }
    
    /*** 
     * 更改UI
     * 接收方拒绝接收文件
     */
    public void refuseReceive() {
    	getSendFileLabel().setVisible(true);
    	
    	getAgreeReceiveButton().setVisible(false);
    	getRefuseReceiveButton().setVisible(false);
    	getProgressBar().setVisible(false);
    	
    	String msg = "\t接收文件:  你已拒绝接收对方文件 \n\n";
    	getShowMessageTextArea().setText(getShowMessageTextArea().getText() + msg);
    }
    
    /***
     * 当前对话用户下线
     * 隐藏发送文件图标和文件传输进度条
     */
    public void UserLogoutUI() {
    	if(isUserMessage) {
    		getSendFileLabel().setVisible(false);
    		getProgressBar().setVisible(false);
    		String ImagePath = Resource.OffLineHeadImagePath + user.getHeadImage();
    		setAndGetHeadImageLabel(ImagePath);
    	}
    }
    
    /***
     * 当前对话用户上线
     * 可以传输文件
     * 头像变亮
     */
    public void UserLoginUI() {
    	if(isUserMessage) {
    		
    		getSendFileLabel().setVisible(true);
    		String ImagePath = Resource.OnLineHeadImagePath + user.getHeadImage();
    		setAndGetHeadImageLabel(ImagePath);
    	}	
    }
    
    /***
     * 提示传输了不支持的文件
     */
    public void sendNoSupportFile() {
    	JOptionPane.showMessageDialog(null, "暂时不支持您要传输的文件类型，请选择其他文件", "文件传输失败", JOptionPane.DEFAULT_OPTION);
    }
}
