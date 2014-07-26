package qin.ui;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import qin.model.Resource;
import qin.model.domainClass.Address;
import qin.model.domainClass.User;

public class ShowUserInfoUI {
	
	int width = 250;
	int height = 200;
	
	private User user = null;
	private JFrame jFrame = null;
	private JPanel jContentPane = null;
	private JButton OKButton = null;
	

	
	public ShowUserInfoUI(User user) {
		this.user = user;
		getFrame();
	}
	
    public void showShowUserInfoUI() {
    	getFrame().setVisible(true);
    }
    
	/**
	 * 初始化 jFrame
	 */
	private JFrame getFrame() {
		if (jFrame == null)
		{
			jFrame = new JFrame("用户信息");
			jFrame.setSize(new Dimension(width, height));
			jFrame.setResizable(false);
			
			Toolkit toolkit = jFrame.getToolkit();
			Dimension screen = toolkit.getScreenSize();
			jFrame.setBounds(screen.width/2 - width/2, screen.height/2 - height/2, width, height); 
			
			jFrame.setContentPane(getJContentPane());
			jFrame.setVisible(false);
			
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
	 * 初始化 jContentPane
	 */
	private JPanel getJContentPane()
	{
		if (jContentPane == null)
		{
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.setSize(new Dimension(width, height));
			
			jContentPane.add(getImageLabel());
			jContentPane.add(getNicknameLabel());
			jContentPane.add(getIDLabel());
			
			jContentPane.add(getAgeLabel());
			jContentPane.add(getGenderLabel());
			jContentPane.add(getAddressLabel());
			jContentPane.add(getEmailLabel());

			jContentPane.add(getOKButton());
			
		}
		return jContentPane;
	}


	private JLabel getImageLabel() {
	    String ImagePath = Resource.OnLineHeadImagePath + user.getHeadImage();
	    JLabel ImageLabel = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(ImagePath))));
	    ImageLabel.setBounds(new Rectangle(20, 10, Resource.HeadImaageWidth, Resource.HeadImaageHeight));

	    return ImageLabel;
	}
	
	private JLabel getNicknameLabel() {
		JLabel nicknameLabel = new JLabel("昵称:  " + user.getNickName());
		nicknameLabel.setBounds(new Rectangle(30+Resource.HeadImaageWidth, height*1/25, width*3/5, height/7));
		
		return nicknameLabel;
	}
	
	private JLabel getIDLabel() {
		JLabel IDLabel = new JLabel("帐号:  " + user.getUid());
		IDLabel.setBounds(new Rectangle(30+Resource.HeadImaageWidth, height*1/7, width*3/5, height/7));
		
		return IDLabel;
	}
	

	private JLabel getAgeLabel() {
		String str = "年 龄: ";
		if(user.getAge() != 0)
			str += user.getAge();
		
		JLabel ageLabel = new JLabel(str);
		ageLabel.setBounds(new Rectangle(width/15, height*5/15, width*1/2, height/15));
		
		return ageLabel;
	}

	private JLabel getGenderLabel() {
		JLabel genderLabel = new JLabel("性 别: " + user.getGender());
		genderLabel.setBounds(new Rectangle(width*7/15, height*5/15, width*1/2, height/15));
		
		return genderLabel;
	}

	private JLabel getEmailLabel() {
		String str = "邮 箱: ";
		if(user.getEmail() != null)
			str += user.getEmail();
		
		JLabel emailLabel = new JLabel(str);
		emailLabel.setBounds(new Rectangle(width*1/15, height*7/15, width*9/10, height/15));
		
		return emailLabel;
	}
	
	private JLabel getAddressLabel() {
		String str = "地 址: ";
		
		if(!user.getAddress().getProvince().equalsIgnoreCase("--") && Address.getCitiesByProvinceName(user.getAddress().getProvince()).length == 1) {
			str += user.getAddress().getCity() + "市";
		} else {
			str += user.getAddress().getProvince() + "省 " + user.getAddress().getCity() + "市";
		}
		
		JLabel addressLabel = new JLabel(str);
		addressLabel.setBounds(new Rectangle(width/15, height*9/15, width*4/5, height/15));
		
		return addressLabel;
	}

	
	/**
	 * 初始化 RegisterButton
	 */
	public JButton getOKButton() {
		if (OKButton == null)
		{
			OKButton = new JButton("OK");
			OKButton.setBounds(new Rectangle(width/2-width/8, height*10/14, width/4, height*2/15));
			
			OKButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					getFrame().dispose();
				}
				
			});
		}
		
		return OKButton;
	}
}
