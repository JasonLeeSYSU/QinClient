package qin.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;

import qin.model.Resource;
import qin.model.domainClass.Qun;
import qin.model.domainClass.User;
import qin.ui.uiElement.FriendTree;

/***
 * Qin Client 主界面
 *
 */
public class MainUI{
	int MainUIWidth = Resource.MainUIWidth;
	int MainUIHeight = Resource.MainUIHeight;
	
	User user = null;
    private JFrame jFrame = null;
    private JPanel jContentPane = null;
    private JPanel FriendListPanel = null;
    private FriendTree friendTree = null;
    private JLabel ImageLabel = null;
    private JLabel NameLabel = null;
    private JLabel IDLabel  = null;
    private JLabel SearchLabel = null;
    private JLabel CreateQunLabel = null;
    private JLabel NotifyLabel = null;
    private JMenuItem sendMessageMenuItem = null;
    private JMenuItem showInfoMenuItem = null;
	
    public MainUI(User user) {
    	this.user = user;
    }
    
    /***
     * 显示 主界面
     */
    public void showMainUI() {
    	getjFrame().setVisible(true);
    }
    
    /**
     * 初始化窗体
     */
    public JFrame getjFrame() {
        if (jFrame == null) {
            jFrame = new JFrame("Qin");
            jFrame.setSize(new Dimension(MainUIWidth, MainUIHeight));
            jFrame.setResizable(false);
            
            jFrame.setContentPane(this.getJContentPane());
                 
            Toolkit toolkit = jFrame.getToolkit();
            Dimension screen = toolkit.getScreenSize();
            int toTop = screen.height < MainUIHeight  ? 0 : (screen.height - MainUIHeight) / 2;
            jFrame.setBounds(screen.width*4/5, toTop, MainUIWidth, MainUIHeight); // 窗体在右侧显示
            
            jFrame.setVisible(false);
        }
        return jFrame;
    }

  
    /**
     * 初始化 jContentPane
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getImageLabel());
            jContentPane.add(getNameLabel());
            jContentPane.add(getIDLabel());
            jContentPane.add(getSearchLabel());
            jContentPane.add(getCreateQunLabel());
            
            jContentPane.add(getFriendListPanel());
        }
        
        return jContentPane;
    }

    private JLabel getImageLabel() {
    	if(ImageLabel == null) {
    		String ImagePath = Resource.OnLineHeadImagePath + user.getHeadImage();
    		
    		ImageLabel = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(ImagePath))));
    		ImageLabel.setBounds(new Rectangle(20, 10, Resource.HeadImaageWidth, Resource.HeadImaageHeight));
    		
    		// 点击用户头像，弹出用户详细信息
    		ImageLabel.addMouseListener(new MouseAdapter() {
	    		@Override
	    		public void mouseClicked(MouseEvent e) {
	    			if (e.getClickCount() == 2) {
	    				ShowUserInfoUI showUserInfoUI = new ShowUserInfoUI(user);
	    				showUserInfoUI.showShowUserInfoUI();
	    			}
	    		}
	    	});
    	}
    	
        return ImageLabel;
    }
    
    /**
     *  用户名称
     */
   private JLabel getNameLabel() {
    	if(NameLabel == null) {
    		NameLabel = new JLabel();
    		NameLabel.setBounds(new Rectangle(20 + Resource.HeadImaageWidth + 10, 10, 200, 20));
    		NameLabel.setForeground(Color.BLACK);
    		NameLabel.setText(user.getNickName());
    	}
        return NameLabel;
    }
    
    /**
     *  用户账号
     */
    private JLabel getIDLabel() {
    	if(IDLabel == null) {
    		IDLabel = new JLabel();
    		IDLabel.setBounds(new Rectangle(20 + Resource.HeadImaageWidth + 10, 30, 200, 20));
    		IDLabel.setForeground(Color.BLACK);
    		IDLabel.setText(user.getUid() + "");
    	}
    	
        return IDLabel;
    }
    
    public JLabel getSearchLabel() {
    	if(SearchLabel == null) {
    		String ImagePath = Resource.SearchPicture;
    		
    		SearchLabel = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(ImagePath))));
    		SearchLabel.setBounds(new Rectangle(MainUIWidth*3/10, MainUIHeight*9/10-10, Resource.HeadImaageWidth, Resource.HeadImaageHeight));
    	}
    	
        return SearchLabel;
    }
    
    public JLabel getCreateQunLabel() {
    	if(CreateQunLabel == null) {
    		String ImagePath = Resource.CreateQunPicture;
    		CreateQunLabel = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(ImagePath))));
    		CreateQunLabel.setBounds(new Rectangle(MainUIWidth*6/10, MainUIHeight*9/10-10, Resource.HeadImaageWidth, Resource.HeadImaageHeight));
    	}
    	
        return CreateQunLabel;
    }
    
    public JLabel getNotifyLabel() {
    	if(NotifyLabel == null) {
    		String ImagePath = Resource.NotifyPicture;
    		NotifyLabel = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(ImagePath))));
    		NotifyLabel.setBounds(new Rectangle(MainUIWidth*6/10, MainUIHeight*9/10-10, Resource.HeadImaageWidth, Resource.HeadImaageHeight));
    	}
    	
        return NotifyLabel;
    }
    
    /***
     * 添加好友列表
     * @param tree
     */
      private FriendTree getFriendTree() {
          if(friendTree == null) {
        	  friendTree = new FriendTree();
        	  friendTree.getJTree().setComponentPopupMenu(createPopMenu());
          }
          return friendTree;
      }

      /**
       * 初始化好友列表FriendListPanel
       */
      private JPanel getFriendListPanel() {
          if (FriendListPanel == null) {
          	FriendListPanel = new JPanel();
          	FriendListPanel.setLayout(new GridLayout(1, 1, 0, 0));
            FriendListPanel.setBounds(new Rectangle(1, MainUIHeight/10, MainUIWidth-2, MainUIHeight*3/4+10));
            FriendListPanel.setBackground(SystemColor.inactiveCaptionText);
            
            JScrollPane jScrollPane = new JScrollPane();
            jScrollPane.getViewport().add(getFriendTree().getJTree());
            FriendListPanel.add(jScrollPane);
            
            FriendListPanel.repaint(); 
          }
          
          return FriendListPanel;
    }
      
    public JTree getFriendsTree() {
    	return getFriendTree().getJTree();
    } 
    
    /***
     * 创建弹出菜单对象
     * @return
     */
    private JPopupMenu createPopMenu() {
		JPopupMenu popMenu=new JPopupMenu();
		sendMessageMenuItem  = new JMenuItem("聊天");
		showInfoMenuItem  = new JMenuItem("查看资料");
		popMenu.add(sendMessageMenuItem);
		popMenu.add(showInfoMenuItem);
		
		return popMenu;
    }
    
    public JMenuItem getSendMessageMenuItem() {
    	return sendMessageMenuItem;
    }
    
    public JMenuItem getShowInfoMenuItem() {
    	return showInfoMenuItem;
    }
    
    /***
     * 添加在线好友
     * @param onlineFriends
     */
    public void setOnlineFriend(List<User> onlineFriends) {
    	getFriendTree().setOnlineFriend(onlineFriends);
    }
    
    /***
     * 添加下限好友
     * @param offlineFrineds
     */
    public void setOfflineFriend(List<User> offlineFrineds) {
    	getFriendTree().setOfflineFriend(offlineFrineds);
    }
    
    /***
     * 添加在线好友
     * @param onlineFriend
     */
    public void setOnlineFriend(User onlineFriend) {
    	getFriendTree().setOnlineFriend(onlineFriend);
    }
    
    /***
     * 添加下限好友
     * @param offlineFrined
     */
    public void setOfflineFriend(User offlineFrined) {
    	getFriendTree().setOfflineFriend(offlineFrined);
    }
    
    
    /***
     * 好友上线
     * @param OnliningUserID
     */
    public void friendOnlining(int OnliningUserID) {
    	getFriendTree().onlining(OnliningUserID);;
    }
    
    /***
     * 好友下线
     * @param user
     */
    public void friendOfflining(int OffliningUserID) {
    	getFriendTree().offlining(OffliningUserID);
    }
    
    /***
     * 添加群
     * @param group
     */
    public void addGroup(Qun group) {
    	getFriendTree().addNodeGroupTree(group);
    }
    
    /***
     * 添加群
     * @param groupList
     */
    public void addGroup(List<Qun> groupList) {
    	getFriendTree().addNodeGroupTree(groupList);
    }
}

