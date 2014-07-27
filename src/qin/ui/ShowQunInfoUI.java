package qin.ui;

import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import qin.model.Resource;
import qin.model.domainClass.Qun;

public class ShowQunInfoUI {
	int width = 250;
	int height = 250;
	
	private Qun qun = null;
	private JFrame jFrame = null;
	private JPanel jContentPane = null;
	private JButton OKButton = null;
	
	JButton qunMemberNumberButton = null;
	JButton descriptionTipButton = null;
	private JScrollPane tableScrollPane = null;
	private JTable table;
	
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
			jContentPane.add(getQunMemberNumberButton());
			jContentPane.add(getDescriptionButton());
			
			jContentPane.add(getTableScrollPane());
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
	
	private JButton getQunMemberNumberButton() {
		if(qunMemberNumberButton == null) {
			qunMemberNumberButton = new JButton("群成员");
			qunMemberNumberButton.setBounds(new Rectangle(width*7/15, height*1/3, width*1/3, height*1/9));
		
			qunMemberNumberButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					getDescriptionTextArea().setVisible(false);
					getTableScrollPane().setVisible(true);
					qunMemberNumberButton.setEnabled(false);
					descriptionTipButton.setEnabled(true);
				}
			});
		}
		
		return qunMemberNumberButton;
	}
	
	private JButton getDescriptionButton() {
		if(descriptionTipButton == null) { 
			descriptionTipButton = new JButton("群简介");
			descriptionTipButton.setBounds(new Rectangle(width/15, height*5/15, width*1/3, height*1/9));
			
			descriptionTipButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					getDescriptionTextArea().setVisible(true);
					getTableScrollPane().setVisible(false);
					
					descriptionTipButton.setEnabled(false);
					qunMemberNumberButton.setEnabled(true);
				}
			});
		}
		
		return descriptionTipButton;
	}

	private JTextArea getDescriptionTextArea() {
		JTextArea descriptionArea =  new JTextArea(qun.getQunDescription());
		descriptionArea.setFocusCycleRoot(true);
		descriptionArea.setLineWrap(true);
		descriptionArea.setBorder(new LineBorder(Color.GRAY, 1, false));
		descriptionArea.setEditable(false);
		descriptionArea.setBounds(new Rectangle(width*1/15, height*6/15+15, width*13/15, height*5/15));
		
		return descriptionArea;
	}
	
	private JScrollPane getTableScrollPane() {
		if(tableScrollPane == null) {
			tableScrollPane = new JScrollPane(getTable());
			tableScrollPane.setVisible(false);
			tableScrollPane.setBorder(new LineBorder(Color.GRAY, 1, false));
			tableScrollPane.setBounds(new Rectangle(width*1/15, height*6/15+15, width*13/15, height*5/15));
		} 
		
		return tableScrollPane;
	}
	
	private JTable getTable() {
		if(table == null) {
			 String[] tableName = { "帐号", "昵称"};
			 String[][] tableContent = new String[qun.getQunMember().size()][2];
			 for(int i = 0; i < qun.getQunMember().size(); i++) {
				 tableContent[i][0] = qun.getQunMember().get(i).getUid() + "";
				 tableContent[i][1] = qun.getQunMember().get(i).getNickName(); 
			 }
			 
			 DefaultTableModel model = new DefaultTableModel();
			 model.setDataVector(tableContent, tableName);
			  
			 table = new JTable(model){
				  private static final long serialVersionUID = 7563012525369954552L;
				  public boolean isCellEditable(int row, int column) { //表格不允许被编辑
				  	return false;
				  }
			  };
			  
			  table.setRowHeight(20);
			  table.getTableHeader().setReorderingAllowed(false);
			  table.getTableHeader().setBackground(new Color(252, 168, 251));
			  table.setGridColor(new Color(166, 228, 143));
			  table.setPreferredScrollableViewportSize(new Dimension(550, 150));
			  
			  table.addMouseListener(new MouseAdapter() {
				  @Override
				  public void mouseClicked(MouseEvent e) {
		    			if (e.getClickCount() == 2) {
		    				 int r  = table.rowAtPoint(e.getPoint());
							 if (r >= 0 && r < table.getRowCount() && r < qun.getQunMember().size()) {
								  ShowUserInfoUI showUserInfoUI = new ShowUserInfoUI(qun.getQunMember().get(r));
								  showUserInfoUI.showShowUserInfoUI();
							  }
		    			}
		    		}
			  });
		} 
		return table;
	}
	
	/**
	 * 初始化 RegisterButton
	 */
	public JButton getOKButton() {
		if (OKButton == null)
		{
			OKButton = new JButton("OK");
			OKButton.setBounds(new Rectangle(width/2-width/8, height*12/15+3, width/4, height*2/18));
			
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
