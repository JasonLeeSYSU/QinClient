package qin.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import qin.controller.QinUIController;
import qin.controller.handelThread.BusinessOperationHandel;
import qin.model.Resource;
import qin.model.domainClass.User;

public class ShowApplicationUI {
	private boolean isAddFriend = true; 
	private User sourceUser = null;
	private int addedUserID;
	private int addedQunID;
	private String addedQunName;
	
	private int Width = Resource.SearchUIWidth;
	private int Height = Resource.SearchUIHeight - 70;
		
	JFrame jFrame = null;
	private JButton agreeButton = null;
	private JButton refuseButton = null;
	private  JTextField inputField = null;
	
    private JPanel showInfoPanel = null;
    private JLabel nameLabel = null;
    private JLabel IDLabel = null;
    
	public ShowApplicationUI(User sourceUser, int addedUserID) {
			this.sourceUser = sourceUser;
			this.addedUserID = addedUserID;
			this.isAddFriend = true;
	}
	
	public ShowApplicationUI(User sourceUser, int addedQunID, String addedQunName) {
		this.sourceUser = sourceUser;
		this.addedQunID = addedQunID;
		this.addedQunName = addedQunName;
		this.isAddFriend = false;
	}
		    
	/**
	* 初始化 窗体
	*/
	public JFrame getJFrame() {  	
		if (jFrame == null) {
		    jFrame = new JFrame(isAddFriend ?  "添加好友请求" : "加入群请求");
		    jFrame.setResizable(false);
		    jFrame.setSize(new Dimension(Width, Height));
		    
		    Toolkit toolkit = jFrame.getToolkit();
		    Dimension screen = toolkit.getScreenSize();
		    int toLeft = screen.height < Width  ? 0 : (screen.height - Width) / 2;
		    int toTop = screen.width < Height  ? 0 : (screen.width - Height) / 2;
		    jFrame.setBounds(toTop, toLeft, Width, Height);

		    jFrame.add(getJPanel());    
		    jFrame.setVisible(true);
		    jFrame.addWindowListener(new java.awt.event.WindowAdapter() {
		                @Override
		                public void windowClosing(java.awt.event.WindowEvent e) {
		                	refuseUser();
		                	jFrame.dispose();
		                }
		    });
		}        
		return jFrame;
	}
		    
	/**
	* 初始化 jPanel
	*/
	private JPanel getJPanel() {
		JPanel jPanel = new JPanel();
		jPanel.setLayout(null);
    
		jPanel.add(getNotifyPanel());
		jPanel.add(getShowInfoPanel());
		        
		return jPanel;
	}
			
	private JPanel getNotifyPanel() {
		JPanel jPanel = new JPanel();
		jPanel.setLayout(null);
		jPanel.setEnabled(true);
			      
		jPanel.setBackground(new Color(217,217,217));
		jPanel.setBorder(new LineBorder(Color.lightGray, 1, true));
		jPanel.setBounds(new Rectangle(Width*1/20, Height*1/12, Width*18/20, Height*9/20));
			        
		jPanel.add(getInputField());
		jPanel.add(getRefuseButton());
		jPanel.add(getAgreeButton());
			              
		return jPanel;
	}
		    
	private JTextField getInputField() {
		if (inputField == null) {
		    String tip = "";
			if(isAddFriend) {
				tip = "用户 " + sourceUser.getNickName() + " 请求成为您的好友";
			} else {
				tip = "用户 " + sourceUser.getNickName() + " 请求加入 <" + addedQunName + ">";
			}
					
		    inputField = new JTextField();
		    inputField.setHorizontalAlignment(JTextField.CENTER);
		    inputField.setText(tip);
		    inputField.setEditable(false);
		    inputField.setFont(new Font("Dialong", Font.BOLD, 13));
	        inputField.setBounds(new Rectangle(Width*1/20, Height*1/15, Width*12/15, Height/7));
	    }
	    return inputField;
	}
		   
	private JButton getAgreeButton() {
		if(agreeButton == null) {
		    agreeButton = new JButton("同意");
		    agreeButton.setBounds(new Rectangle(Width*1/4, Height*1/4+5, Width/6, Height*1/8));
		    
		    agreeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					agreeUser();
				}
			});
		}
		return agreeButton;
	}
		    
	private JButton getRefuseButton() {
		if(refuseButton == null) {
		    refuseButton = new JButton("拒绝");
		    refuseButton.setBounds(new Rectangle(Width*1/2-12, Height*1/4+5, Width/6, Height*1/8));
		    
		    agreeButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					refuseUser();
				}
			});
		}
		    	
		return refuseButton;
	}
		    
	private JPanel getShowInfoPanel() {
		if(showInfoPanel == null) {
		   showInfoPanel = new JPanel();
		   showInfoPanel.setLayout(null);
		   showInfoPanel.setEnabled(true);
				      
		   showInfoPanel.setBackground(new Color(217,217,217));
		   showInfoPanel.setBorder(new LineBorder(Color.lightGray, 1, true));
		   showInfoPanel.setBounds(new Rectangle(Width*1/20, Height*5/10, Width*18/20, Height*8/20));
				        
		   showInfoPanel.add(getNameLabel() );
		   showInfoPanel.add(getIDLabel());
		   showInfoPanel.add(getImageLabel());
		   showInfoPanel.setVisible(true);
		}
		    	
		return showInfoPanel;
	}
		    
	private JLabel getImageLabel() {
		 String ImagePath = Resource.OnLineHeadImagePath + sourceUser.getHeadImage();
		 JLabel headImageLabel = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(ImagePath))));
		 headImageLabel.setBounds(new Rectangle(Width*1/4+5, Height*2/15-5, Resource.HeadImaageWidth, Resource.HeadImaageHeight));
		    		
		 headImageLabel.addMouseListener(new MouseAdapter() {
	    		@Override
	    		public void mouseClicked(MouseEvent e) {
	    			if (e.getClickCount() == 2) {
	    				ShowUserInfoUI showUserInfoUI = new ShowUserInfoUI(sourceUser);
	    				showUserInfoUI.showShowUserInfoUI();
	    			}
	    		}
	    	});
		 
		 return headImageLabel;
	}
		    
	/**
	*  用户名称
	*/
	private JLabel getNameLabel() {
		if(nameLabel == null) {
		    nameLabel = new JLabel();
		    nameLabel.setBounds(new Rectangle(Width*2/5, Height*2/20, Width/2, Height*1/10));
		    nameLabel.setText("昵称： " + sourceUser.getNickName());
		 }
		    	
		 return nameLabel;
	}
		    
	/**
	*  用户账号
	*/
	private JLabel getIDLabel() {
		if(IDLabel == null) {
		   IDLabel = new JLabel();
		   IDLabel.setBounds(new Rectangle(Width*2/5, Height*4/20, Width/2, Height*1/10));
		   IDLabel.setText("账号： " + sourceUser.getUid());
		}
		    	
		return IDLabel;
	}

	public void agreeUser() {
		if(isAddFriend) {
			BusinessOperationHandel.respondAddFriendApplication(sourceUser.getUid(), addedUserID, true);
			QinUIController.getInstance().addFriend(sourceUser);
			  	
			JOptionPane.showMessageDialog(null, "您与 用户 " + sourceUser.getNickName() + " 已成为好友\n赶紧开始聊天吧！", "添加好友", JOptionPane.DEFAULT_OPTION); 
		} else {
			BusinessOperationHandel.respondJoinQunApplication(sourceUser.getUid(), addedQunID, true);
		}
		
		getJFrame().dispose();
	}
	
	public void refuseUser() {
		if(isAddFriend) {
			BusinessOperationHandel.respondAddFriendApplication(sourceUser.getUid(), addedUserID, false);
		} else {
			BusinessOperationHandel.respondJoinQunApplication(sourceUser.getUid(), addedQunID, false);
		}
		
		getJFrame().dispose();
	}
}
