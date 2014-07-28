package qin.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import qin.model.Resource;

/***
 * 登录界面
 */
public class LoginUI {
	int width = 260;
	int height = 320;
	String logoPath = Resource.LogoPath;
	
    private JFrame jFrame = null;			
    private JPanel jContentPane = null;  
    private JTextField IDField = null;	  	
    private JPasswordField PasswordField = null;  
    private JButton loginButton = null;			
    private JButton regeditButton = null;  		
  
    public LoginUI() {
    	initJFrame();
    }
    
    /***
     * 获取登录窗口 
     * @return
     */
    private JFrame initJFrame() {	
        if (jFrame == null) {
            jFrame = new JFrame();
            jFrame.setTitle("Qin");
            jFrame.setSize(width, height);
            jFrame.setResizable(false);	
            
            Toolkit toolkit = jFrame.getToolkit();     
            Dimension screen = toolkit.getScreenSize();
            jFrame.setBounds(screen.width/2 - width/2, screen.height/2 - height/2, width, height);  //让窗体在屏幕正中央显示
            
            jFrame.setContentPane(getJContentPane());  
            jFrame.setVisible(false);
            
            // 添加窗口时间监听
            jFrame.addWindowListener(new WindowAdapter() {	
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            
            jFrame.setDefaultCloseOperation(1);
            jFrame.getRootPane().setDefaultButton(loginButton);
        }
        return jFrame; 
    }

    /***
     * 获取登录UI画版
     * @return
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getLogo());
            jContentPane.add(getIDTipLable());
            jContentPane.add(getIDField());
            jContentPane.add(getPasswordTipLable());
            jContentPane.add(getPasswordField());
            jContentPane.add(getLoginButton());
            jContentPane.add(getRegeditButton());
        }
        
        return jContentPane;
    }

    /***
     * 显示logo
     * @return
     */
    private JLabel getLogo() {
    	JLabel logo=new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(logoPath))));
    	logo.setBounds(new Rectangle(width/5, height/50, width*2/3,width*2/3));
        return logo;
    }
    
    private JLabel getIDTipLable() {
    	JLabel IDTipLabel = new JLabel("帐号: ");
    	IDTipLabel.setBounds(new Rectangle(width/8, height*15/30, width/4,height/10));
    	return IDTipLabel;
    }
  
    private JLabel getPasswordTipLable() {
    	JLabel PasswordTipLabel = new JLabel("密码: ");
    	PasswordTipLabel.setBounds(new Rectangle(width/8, height*19/30, width/4,height/10));
    	return PasswordTipLabel;
    }
 
    /***
     * 获取帐号输入框
     * @return
     */
    public JTextField getIDField() {
        if (IDField == null) {
        	IDField = new JTextField();
        	IDField.setBorder(new LineBorder(Color.black, 1, true));
        	IDField.setBounds(new Rectangle(width*2/8, height*15/30, width*3/5,height/10));
        	
        	// 设置输入帐号长度
        	IDField.addKeyListener(new KeyAdapter(){
				public void keyTyped(KeyEvent e) {
					int keyChar = e.getKeyChar();				
					if(keyChar < KeyEvent.VK_0 || keyChar > KeyEvent.VK_9 || IDField.getText().length() >= 9) {
						e.consume(); //关键，屏蔽掉非法输入
					}
				}
        	});
        }
        
        return IDField;
    }
   
    public JPasswordField getPasswordField() {
        if (PasswordField == null) {
            PasswordField = new JPasswordField();
            PasswordField.setBorder(new LineBorder(Color.black, 1, true));
            PasswordField.setBounds(new Rectangle(width*2/8, height*19/30, width*3/5,height/10));
            
            PasswordField.addKeyListener(new KeyAdapter(){
    				public void keyTyped(KeyEvent e) {			
    					if(PasswordField.getText().length() >= 9) {
    						e.consume(); //关键，屏蔽掉非法输入
    					}
    				}
            	});
        }
        
        return PasswordField;
    }

    public JButton getLoginButton() {
        if (loginButton == null) {
            loginButton = new JButton("登录");
            loginButton.setFocusPainted(false);
            loginButton.setBounds(new Rectangle(width*2/15, height*23/30, width/3,height*2/18));
            
            loginButton.addMouseListener(new MouseAdapter() {
	    		@Override
	    		public void mouseExited(MouseEvent e) {
	    			loginButton.setEnabled(true);
	    		}
	    		
	    		public void mouseEntered(MouseEvent e) {
	    			// TODO Auto-generated method stub
	    	          if(getIDField().getText().trim().length() > 0 && getPasswordField().getText().length() > 0) {
	    	        	  loginButton.setEnabled(true);
	    	          } else {
	    	        	  loginButton.setEnabled(false);
	    	          }
	    		}
	    	});
            
            
        }
        return loginButton;
    }

    public JButton getRegeditButton() {
        if (regeditButton == null) {
            regeditButton = new JButton("注册");
            regeditButton.setFocusPainted(false);
            regeditButton.setBounds(new Rectangle(width*8/15, height*23/30, width/3,height*2/18));
        }
        
        return regeditButton;
    }

    public void showErrorMessage(String msg) {
    	
    	JOptionPane.showMessageDialog(null, msg, "登录失败", JOptionPane.DEFAULT_OPTION);
    }
    
  public void showNetWorkErrorMessage() {
    	
    	JOptionPane.showMessageDialog(null, "登录失败:\n网络异常， 请稍后再登录。", "网络异常", JOptionPane.DEFAULT_OPTION);
    }
    
    public void showLoginUI() {
    	jFrame.setVisible(true);
    }
    
    public void hideLoginUI() {
    	jFrame.setVisible(false);
    }
}



