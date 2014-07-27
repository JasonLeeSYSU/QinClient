package qin.ui;

import java.awt.Color;
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
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import qin.model.Resource;
import qin.model.domainClass.Qun;

public class ShowQunInfoUI {
	int width = 250;
	int height = 250;
	
	private Qun qun = null;
	private JFrame jFrame = null;
	private JPanel jContentPane = null;
	private JButton OKButton = null;
	

	
	public ShowQunInfoUI(Qun qun) {
		this.qun = qun;
		getFrame();
	}
	
    public void showQunInfoUI() {
    	getFrame().setVisible(true);
    }
    
	/**
	 * 初始化 jFrame
	 */
	private JFrame getFrame() {
		if (jFrame == null)
		{
			jFrame = new JFrame("群信息");
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
			
			jContentPane.add(getQunOwnerLabel());
			jContentPane.add(getQunMemberNumberLabel());
			jContentPane.add(getDescriptionLabel());
			jContentPane.add(getDescriptionTextArea());

			jContentPane.add(getOKButton());
			
		}
		return jContentPane;
	}


	private JLabel getImageLabel() {
	    String ImagePath = Resource.QunLogo;
	    JLabel ImageLabel = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(ImagePath))));
	    ImageLabel.setBounds(new Rectangle(20, 10, Resource.HeadImaageWidth, Resource.HeadImaageHeight));

	    return ImageLabel;
	}
	
	private JLabel getNicknameLabel() {
		JLabel nicknameLabel = new JLabel("群名:  " + qun.getQunName());
		nicknameLabel.setBounds(new Rectangle(30+Resource.HeadImaageWidth, height*1/30, width*3/5, height/8));
		
		return nicknameLabel;
	}
	
	private JLabel getIDLabel() {
		JLabel IDLabel = new JLabel("帐号:  " + qun.getQunID());
		IDLabel.setBounds(new Rectangle(30+Resource.HeadImaageWidth, height*1/10, width*3/5, height/7));
		
		return IDLabel;
	}
	

	private JLabel getQunOwnerLabel() {
		JLabel qunOwnerLabel = new JLabel("群 主: " + qun.getQunOwnerID());
		qunOwnerLabel.setBounds(new Rectangle(width*1/15, height*1/4, width*1/2, height/15));
		
		return qunOwnerLabel;
	}
	
	private JLabel getQunMemberNumberLabel() {
		JLabel qunMemberNumberLabel = new JLabel("群成员: " +  qun.getQunMember().size() + " 人");
		qunMemberNumberLabel.setBounds(new Rectangle(width*7/15, height*1/4, width*1/2, height/15));
		
		return qunMemberNumberLabel;
	}
	
	private JLabel getDescriptionLabel() {

		JLabel descriptionTipLabel = new JLabel("群简介:");
		descriptionTipLabel.setBounds(new Rectangle(width/15, height*5/15, width*4/5, height/15));
		
		return descriptionTipLabel;
	}

	private JTextArea getDescriptionTextArea() {
		JTextArea descriptionArea =  new JTextArea(qun.getQunDescription());
		descriptionArea.setFocusCycleRoot(true);
		descriptionArea.setLineWrap(true);
		descriptionArea.setBorder(new LineBorder(Color.GRAY, 1, false));
		descriptionArea.setEditable(false);
		descriptionArea.setBounds(new Rectangle(width*1/15, height*6/15+3, width*13/15, height*5/15));
		
		return descriptionArea;
	}
	
	/**
	 * 初始化 RegisterButton
	 */
	public JButton getOKButton() {
		if (OKButton == null)
		{
			OKButton = new JButton("OK");
			OKButton.setBounds(new Rectangle(width/2-width/8, height*12/15-8, width/4, height*2/18));
			
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
