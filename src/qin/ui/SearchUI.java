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
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import qin.model.Resource;
import qin.model.domainClass.Address;
import qin.model.domainClass.Qun;
import qin.model.domainClass.User;
import qin.testcase.StaticTestCase;




public class SearchUI {

		private int SearchUIWidth = Resource.SearchUIWidth;
		private int SearchUIHeight = Resource.SearchUIHeight;
		
		private boolean isUser = true; 
		private User user = null;
		private Qun group = null;
	
		JFrame jFrame = null;
		private  JTextField inputField = null;
		private JButton userButton = null;
		private JButton groupButton = null;
		private JButton findButton = null;
		private JButton addButton = null;
	    private JLabel tipLabel = null;
	    private JLabel noFoundLabel = null;
	    private JPanel showInfoPanel = null;
	    private JLabel nameLabel = null;
	    private JLabel IDLabel = null;
	    		
		public SearchUI() {
			isUser = true;
		}
		
		public void showSearchUI() {
			getJFrame().setVisible(true);
		}
		
		public void hideSearchUI() {
			getJFrame().setVisible(false);
		}
	    
	    /**
	     * 初始化 窗体
	     */
	    private JFrame getJFrame() {
	    	
	        if (jFrame == null) {
	            jFrame = new JFrame("添加好友／群");
	            jFrame.setResizable(false);
	            jFrame.setSize(new Dimension(SearchUIWidth, SearchUIHeight));
	              
	            Toolkit toolkit = jFrame.getToolkit();
	            Dimension screen = toolkit.getScreenSize();
	            int toLeft = screen.height < SearchUIWidth  ? 0 : (screen.height - SearchUIWidth) / 2;
	            int toTop = screen.width < SearchUIHeight  ? 0 : (screen.width - SearchUIHeight) / 2;
	            jFrame.setBounds(toTop, toLeft, SearchUIWidth, SearchUIHeight);

	            jFrame.add(getJPanel());    
	            jFrame.setVisible(false);
	            
	            jFrame.addWindowListener(new java.awt.event.WindowAdapter() {
	                @Override
	                public void windowClosing(java.awt.event.WindowEvent e) {
	                	hideSearchUI();
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
	        jPanel.setEnabled(true);
        	
	        jPanel.add(getUserButton());
	        jPanel.add(getGroupButton());
	        jPanel.add(getNoFoundLabel());
	        
	        jPanel.add(getSearchPanel());
	        jPanel.add(getShowInfoPanel());
	        
	        return jPanel;
	    }
		
	    private JPanel getSearchPanel() {
	    	  JPanel jPanel = new JPanel();
		      jPanel.setLayout(null);
		      jPanel.setEnabled(true);
		      
		      jPanel.setBackground(new Color(217,217,217));
		      jPanel.setBorder(new LineBorder(Color.lightGray, 1, true));
		      jPanel.setBounds(new Rectangle(SearchUIWidth*1/20, SearchUIHeight*1/10, SearchUIWidth*18/20, SearchUIHeight*4/10));
		        
		      jPanel.add(getTipLabel()); 
		      jPanel.add(getInputField());
		      jPanel.add(getFindButton());
		      jPanel.add(getAddButton());
		        
		        
		      return jPanel;
	    }
	    
	    public JTextField getInputField() {
	        if (inputField == null) {
	        	
	        	inputField = new JTextField();
	        	inputField.setBorder(new LineBorder(Color.black, 1, true));
	        	inputField.setBounds(new Rectangle(SearchUIWidth*1/20, SearchUIHeight*1/6, SearchUIWidth*12/15, SearchUIHeight/12));
	        	
	        	inputField.addKeyListener(new KeyAdapter(){
					public void keyTyped(KeyEvent e) {
						int keyChar = e.getKeyChar();				
						if(keyChar < KeyEvent.VK_0 || keyChar > KeyEvent.VK_9 || inputField.getText().length() >= 9) {
							e.consume(); //关键，屏蔽掉非法输入
						}
						
						//if(keyChar == '\b') {
							//System.out.println("退格");
							getFindButton().setVisible(true);
							getAddButton().setVisible(false);
							getShowInfoPanel().setVisible(false);
						//}
					}
				});
	        	
	        	inputField.addMouseListener(new MouseAdapter() {
	        		public void mouseEntered(MouseEvent e) {
	        			if(inputField.getText().length() > 0) {
	        				getFindButton().setEnabled(true);
	        			} else {
	        				getFindButton().setEnabled(false);
	        			}
	        		}
	        	});
	        }
	        
	        return inputField;
	    }
	    
	    private JLabel getTipLabel() {
	    	if(tipLabel == null) {
	    		 tipLabel = new JLabel("通过帐号查找用户: ");
	    		 tipLabel.setBounds(new Rectangle(SearchUIWidth*1/20, SearchUIHeight*1/20, SearchUIWidth/2, SearchUIHeight/10));
	    	}
	    
	    	return tipLabel;
	    }
	    
	    private JButton getUserButton() {
	    	if(userButton == null) {
	    		userButton = new JButton("用户");
	    		userButton.setForeground(Color.BLUE);
	    		userButton.setBounds(new Rectangle(SearchUIWidth*3/9+5, SearchUIHeight*1/20, SearchUIWidth/7+5, SearchUIHeight/10));
	    		
	    		userButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						isUser = true;
						userButton.setForeground(Color.BLUE);
						getGroupButton().setForeground(Color.BLACK);
						getFindButton().setVisible(true);
						
						getAddButton().setText("加为好友");
						getAddButton().setVisible(false);
						getTipLabel().setText("通过账号查找用户");
						
						getShowInfoPanel().setVisible(false);
					}
				});
	    	}
	    	
	    	return userButton;
	    }
	    
	    private JButton getGroupButton() {
	    	if(groupButton == null) {
	    		groupButton = new JButton("群");
	    		groupButton.setBounds(new Rectangle(SearchUIWidth*5/10, SearchUIHeight*1/20, SearchUIWidth/7+5, SearchUIHeight/10));
	    		
	    		groupButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						isUser = false;
						groupButton.setForeground(Color.BLUE);
						getUserButton().setForeground(Color.BLACK);
						getFindButton().setVisible(true);
						
						getAddButton().setText("加入该群");
						getAddButton().setVisible(false);
						getTipLabel().setText("通过账号查找群");
						
						getShowInfoPanel().setVisible(false);
					}
				});
	    	}
	    	
	    	return groupButton;
	    }
	    
	    public JButton getFindButton() {
	    	if(findButton == null) {
	    		findButton = new JButton("下一步");
	    		findButton.setBounds(new Rectangle(SearchUIWidth*1/3, SearchUIHeight*3/10 - 5, SearchUIWidth/5+20, SearchUIHeight*1/10));
	    		
	    		findButton.addMouseListener(new MouseAdapter() {
	        		public void mouseEntered(MouseEvent e) {
	        			if(inputField.getText().length() > 0) {
	        				getFindButton().setEnabled(true);
	        			} else {
	        				getFindButton().setEnabled(false);
	        			}
	        		
	        		}
	        		
	        	});
	        	
	    		findButton.setEnabled(false);
	    	}
	    	
	    	return findButton;
	    }
	    
	    public JButton getAddButton() {
	    	if(addButton == null) {
	    		addButton = new JButton("加为好友");
	    		addButton.setBounds(new Rectangle(SearchUIWidth*1/3, SearchUIHeight*3/10 - 5, SearchUIWidth/5+20, SearchUIHeight*1/10));
	    		
	    		addButton.setVisible(false);
	    	}
	    	
	    	return addButton;
	    }
	    
	    private JPanel getShowInfoPanel() {
	    	if(showInfoPanel == null) {
	    		showInfoPanel = new JPanel();
	    		showInfoPanel.setLayout(null);
	    		showInfoPanel.setEnabled(true);
			      
	    		showInfoPanel.setBackground(new Color(217,217,217));
	    		showInfoPanel.setBorder(new LineBorder(Color.lightGray, 1, true));
	    		showInfoPanel.setBounds(new Rectangle(SearchUIWidth*1/20, SearchUIHeight*5/10 + 20, SearchUIWidth*18/20, SearchUIHeight*3/10));
			        
	    		showInfoPanel.add(getNameLabel() );
	    		showInfoPanel.add(getIDLabel());
	    		
	    		showInfoPanel.setVisible(false);
	    	}
	    	
	    	return showInfoPanel;
	    }
	    
	    private JLabel getImageLabel(String ImagePath) {
	    	
	    	JLabel headImageLabel = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(ImagePath))));
	    	headImageLabel.setBounds(new Rectangle(SearchUIWidth*3/10, SearchUIHeight*1/15, Resource.HeadImaageWidth, Resource.HeadImaageHeight));
	    		
	    	headImageLabel.addMouseListener(new MouseAdapter() {
	    		@Override
	    		public void mouseClicked(MouseEvent e) {
	    			if (e.getClickCount() == 2) {
	    				if(isUser) {
	    					ShowUserInfoUI showUserInfoUI = new ShowUserInfoUI(user);
	    					showUserInfoUI.showShowUserInfoUI();
	    				} else {
	    					ShowQunInfoUI showQunInfoUI = new ShowQunInfoUI(group);
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
	    		nameLabel.setBounds(new Rectangle(SearchUIWidth*1/2-30, SearchUIHeight*1/20, SearchUIWidth/2, SearchUIHeight*1/10));
	    		nameLabel.setText("名称：name???");
	    	}
	    	
	        return nameLabel;
	    }
	    
	    /**
	     *  用户账号
	     */
	    private JLabel getIDLabel() {
	    	if(IDLabel == null) {
	    		IDLabel = new JLabel();
	    		IDLabel.setBounds(new Rectangle(SearchUIWidth*1/2-30, SearchUIHeight*2/20+5, SearchUIWidth/2, SearchUIHeight*1/10));
	    		IDLabel.setText("账号：ID???");
	    	}
	    	
	        return IDLabel;
	    }
	    
	    private JLabel getNoFoundLabel() {
	    	if(noFoundLabel == null) {
	    		noFoundLabel = new JLabel("您查询的用户不存在");
	    		noFoundLabel.setBounds(new Rectangle(SearchUIWidth*1/3+10, SearchUIHeight*5/10, SearchUIWidth/2, SearchUIHeight*1/10));
	    		noFoundLabel.setForeground(Color.RED);
	    		noFoundLabel.setVisible(false);
	    	}
	    	
	        return noFoundLabel;
	    }
	    
	    public void showFindResult(Object obj) {
	    	if(obj == null) {
	    		getNoFoundLabel().setVisible(true);
		    	getShowInfoPanel().setVisible(false);
		    	
		    	getAddButton().setVisible(false);
		    	getFindButton().setVisible(true);
		    	getFindButton().setEnabled(false);
		    	return ;
	    	}
	    	
	    	if(isUser)
	    		user = (User)obj;
	    	else 
	    		group = (Qun)obj;
	    	
	    	String headImagePaht = isUser ? (Resource.OnLineHeadImagePath + user.getHeadImage()) : Resource.QunLogo;
	    	String name  = isUser ? ("昵称: "+user.getNickName()) : ("群名: "+group.getQunName());
	    	String ID = "账号: " + (isUser ? user.getUid() : group.getQunID());
	    	
	    	getNameLabel().setText(name);
	    	getIDLabel().setText(ID);
	    	getShowInfoPanel().add(getImageLabel(headImagePaht));
	    	
	       	getNoFoundLabel().setVisible(false);
	    	getShowInfoPanel().setVisible(true);
	 
	    }
	    
	    public Object getSearchObj() {
	    	if(isUser) {
	    		User u = new User();
	    		u.setUid(new Integer(getInputField().getText()));
	    		return u;
	    	} else {
	    		Qun g = new Qun();
	    		g.setQunID(new Integer(getInputField().getText()));
	    		return g;
	    	}
	    } 
	    
	    public Object getAddObj() {
	    	if(isUser)
	    		return user;
	    	else
	    		return group;
	    }
	    
	    public void showAddMessage() {
	    	if(isUser)
	    		JOptionPane.showMessageDialog(null, "您的请求已经发送过去，正待等待对方回复", "添加好友", JOptionPane.DEFAULT_OPTION);
	    	else 
	    		JOptionPane.showMessageDialog(null, "您的请求已经发送过去，正待等待群主回复", "加入群", JOptionPane.DEFAULT_OPTION);
	    }
	    
	    public void showRepeatAddMessage(int id) {
	    	if(isUser)
	    		JOptionPane.showMessageDialog(null, "您与 " + id + " 已是好友\n不能重复添加", "重复添加", JOptionPane.DEFAULT_OPTION);
	    	else 
	    		JOptionPane.showMessageDialog(null, "您已经加入群 " + id + "\n不能重复添加", "重复添加", JOptionPane.DEFAULT_OPTION);
	    }
}
