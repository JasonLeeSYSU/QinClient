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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import qin.model.Resource;
import qin.model.domainClass.Qun;



public class CreateQunUI {

	int width = 250;
	int height = 250;
	
	private Qun Qun = null;
	private JFrame jFrame = null;
	private JPanel jContentPane = null;
	private JTextField nameField = null;
	private JTextArea descriptionArea = null;
	private JButton createQunButton = null;
	
	public CreateQunUI() {
		getFrame();
	}
	
    public void showCreateQunUI() {
    	getFrame().setVisible(true);
    }
    
    public void hideCreateQunUI() {
    	getFrame().setVisible(false);
    }
    
	private JFrame getFrame() {
		if (jFrame == null) {
			jFrame = new JFrame("创建群");
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
                	getNameField().setText("");
                	getDescriptionTextArea().setText("");
                	hideCreateQunUI();
                }
            });
		}
		
		return jFrame;
	}
	
	
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.setSize(new Dimension(width, height));
			
			jContentPane.add(getImageLabel());
			jContentPane.add(getNameLabel());
			jContentPane.add(getNameField());
			jContentPane.add(getDescriptionLabel());
			jContentPane.add(getDescriptionTextArea());
			jContentPane.add(getCreateQunButton());
		}
		
		return jContentPane;
	}


	private JLabel getImageLabel() {
	    String ImagePath = Resource.QunLogo;
	    JLabel ImageLabel = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(ImagePath))));
	    ImageLabel.setBounds(new Rectangle(20, 10, Resource.HeadImaageWidth, Resource.HeadImaageHeight));

	    return ImageLabel;
	}
	
	private JLabel getNameLabel() {
		JLabel nicknameLabel = new JLabel("群名:  ");
		nicknameLabel.setBounds(new Rectangle(30+Resource.HeadImaageWidth, 10, width*3/5, height/12));
		
		return nicknameLabel;
	}
	
	private JTextField getNameField() {
	    if (nameField == null) {
	        nameField = new JTextField();
	        nameField.setBorder(new LineBorder(Color.GRAY, 1, false));
	        nameField.setBounds(new Rectangle(width*1/4+10, height*1/9+3, width*3/5,height/11));
	        	
	        nameField.addKeyListener(new KeyAdapter(){
				public void keyTyped(KeyEvent e) {			
					if(nameField.getText().length() >= 10) {
						e.consume(); // 屏蔽过长输入
					}
				}
			});
	    }
	       
	    return nameField;
	}
	   
	private JLabel getDescriptionLabel() {
		JLabel descriptionTipLabel = new JLabel("群简介:");
		descriptionTipLabel.setBounds(new Rectangle(width/15, height*1/4, width*4/5, height/15));
		
		return descriptionTipLabel;
	}

	private JTextArea getDescriptionTextArea() {
		
		if(descriptionArea == null) {
			descriptionArea =  new JTextArea();
			descriptionArea.setFocusCycleRoot(true);
			descriptionArea.setLineWrap(true);
			descriptionArea.setBorder(new LineBorder(Color.GRAY, 1, false));
			descriptionArea.setBounds(new Rectangle(width*1/15, height*3/10+5, width*13/15, height*6/15));
			
			descriptionArea.addKeyListener(new KeyAdapter(){
				public void keyTyped(KeyEvent e) {			
					if(descriptionArea.getText().length() >= 130) {
						e.consume(); // 屏蔽过长输入
					}
				}
			});
		}
		
		return descriptionArea;
	}
	

	public JButton getCreateQunButton() {
		if (createQunButton == null) {
			createQunButton = new JButton("创建");
			createQunButton.setBounds(new Rectangle(width/2-width/8, height*12/15-8, width/4, height*2/18));
			
			createQunButton.addMouseListener(new MouseAdapter() {
        		public void mouseEntered(MouseEvent e) {
        			if(getNameField().getText().length() > 0) {
        				createQunButton.setEnabled(true);
        			} else {
        				createQunButton.setEnabled(false);
        			}
        		}
        	});
		}
		
		return createQunButton;
	}
	
	public Qun getQunToCreate() {
		Qun qun = new Qun();
		qun.setQunName(getNameField().getText());
		qun.setQunDescription(getDescriptionTextArea().getText());
		
		return qun;
	}
	
	public void showCreateQunID(String name, int ID) {
	    	JOptionPane.showMessageDialog(null, name + " 已经成功创建\n" + "群号为 " + ID, "创建成功", JOptionPane.DEFAULT_OPTION);
	    	getNameField().setText("");
	    	getDescriptionTextArea().setText("");
	}
	
	public void showErrorMessage() {
		JOptionPane.showMessageDialog(null, "群创建失败\n请稍后再试\n", "创建失败", JOptionPane.ERROR_MESSAGE);
	}
}
