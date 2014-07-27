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

import qin.controller.QinController;
import qin.controller.QinUIController;
import qin.controller.handelThread.BusinessOperationHandel;
import qin.model.Resource;
import qin.model.domainClass.Qun;
import qin.model.domainClass.User;
import qin.testcase.StaticTestCase;

public class ShowApplicationResponseUI {

	private User addedUser = null;
	private Qun addedQun = null;
	private int sourceID;
	private boolean isSuccess;
	private boolean isAddFriend;
	
	private int Width = Resource.SearchUIWidth;
	private int Height = Resource.SearchUIHeight - 70;
		
	JFrame jFrame = null;
	private JButton okButton = null;
	private JButton againButton = null;
	private  JTextField inputField = null;
	
    private JPanel showInfoPanel = null;
    private JLabel nameLabel = null;
    private JLabel IDLabel = null;
    
	public ShowApplicationResponseUI(User addedUser, int sourceID, boolean isSuccess) {
		this.addedUser = addedUser;
		this.sourceID = sourceID;
		this.isSuccess = isSuccess;
		this.isAddFriend = true;
		
	} 
	
	public ShowApplicationResponseUI(Qun addedQun, int sourceID,  boolean isSuccess) {
		this.addedQun = addedQun;
		this.sourceID = sourceID;
		this.isSuccess = isSuccess;
		this.isAddFriend = false;
	}
		    
	/**
	* 初始化 窗体
	*/
	public JFrame getJFrame() {  	
		if (jFrame == null) {
		    jFrame = new JFrame(isAddFriend ?  "添加好友请求结果" : "加入群请求结果");
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
		
		if(isSuccess)
			jPanel.add(getOKButton());
		else
			jPanel.add(getAgainButton());
			              
		return jPanel;
	}
		    
	private JTextField getInputField() {
		if (inputField == null) {
		    String tip = "";
			if(isAddFriend) {
				if(isSuccess) {
					tip = "用户 " + addedUser.getNickName() + " 已成为您的好友,赶紧开始聊天吧！";
				} else {
					tip = "抱歉,用户 " + addedUser.getNickName() + " 拒绝成为您的好友！";
				}
			} else {
				if(isSuccess) {
					tip = "您已加入群 " + addedQun.getQunName() + ",赶紧开始聊天吧！";
				} else {
					tip = "抱歉,您被拒绝加入群 " + addedQun.getQunName();
				}
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
		   
	private JButton getOKButton() {
		if(okButton == null) {
		    okButton = new JButton("OK");
		    okButton.setBounds(new Rectangle(Width*2/5, Height*1/4+5, Width/6, Height*1/8));
		    
		    okButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					jFrame.dispose();
				}
			});
		}
		return okButton;
	}
		    
	private JButton getAgainButton() {
		if(againButton == null) {
			againButton = new JButton("重发");
			againButton.setBounds(new Rectangle(Width*2/5, Height*1/4+5, Width/6, Height*1/8));
		    
			againButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if(isAddFriend)
						BusinessOperationHandel.addFriend(sourceID, addedUser.getUid());
					else
						BusinessOperationHandel.joinQun(sourceID, addedQun.getQunID());
					
					JOptionPane.showMessageDialog(null, "您的请求已重新发送\n请耐心等待", "重发", JOptionPane.DEFAULT_OPTION); 
					jFrame.dispose();
				}
			});
		}
		    	
		return againButton;
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
		 String ImagePath = isAddFriend ? Resource.OnLineHeadImagePath + addedUser.getHeadImage() : Resource.QunLogo;
		 JLabel headImageLabel = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(ImagePath))));
		 headImageLabel.setBounds(new Rectangle(Width*1/4+5, Height*2/15-5, Resource.HeadImaageWidth, Resource.HeadImaageHeight));
		    		
		 headImageLabel.addMouseListener(new MouseAdapter() {
	    		@Override
	    		public void mouseClicked(MouseEvent e) {
	    			if (e.getClickCount() == 2) {
	    				if(isAddFriend) {
	    					ShowUserInfoUI showUserInfoUI = new ShowUserInfoUI(addedUser);
	    					showUserInfoUI.showShowUserInfoUI();
	    				} else {
	    					ShowQunInfoUI showQunInfoUI = new ShowQunInfoUI(addedQun);
	    					showQunInfoUI.showQunInfoUI();
	    				}
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
		    if(isAddFriend)
		    	nameLabel.setText("昵称： " +  addedUser.getNickName());
		    else
		    	nameLabel.setText("群名：" + addedQun.getQunName());
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
		   if(isAddFriend)
			    IDLabel.setText("账号： " +  addedUser.getUid());
		    else
		    	IDLabel.setText("账号：" + addedQun.getQunID());
		}
		    	
		return IDLabel;
	}
}
