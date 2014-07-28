package qin.ui.uiElement;

import java.util.Enumeration;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import qin.model.Resource;
import qin.model.domainClass.Qun;
import qin.model.domainClass.User;

public class FriendTree {
	
	private User rootNode = null;
	private User friendNode = null;
	private User onlineNode = null;
	private User offlineNode = null;
    private Qun groupNode = null;
   
	private DefaultMutableTreeNode root = null;
	private DefaultMutableTreeNode friendSubTree = null;
	private DefaultMutableTreeNode onlineSubTree = null;
	private DefaultMutableTreeNode offlineSubTree = null;
	private DefaultMutableTreeNode groupSubTree = null;

	private JTree tree = null;
	private DefaultTreeModel treeMode;
	
	
    public FriendTree() {
    	initFriendTree();
    }
    
    /**
     * 初始化好友树
     * 未加任何好友的情况
     */
    private void initFriendTree()
    {
    	rootNode = new User();
    	rootNode.setUid(Resource.NotTreeNodeSign);
    	rootNode.setNickName("好友&&群");
    	
    	friendNode = new User();
    	friendNode.setUid(Resource.NotTreeNodeSign);
    	friendNode.setNickName("我的好友");
    	
    	onlineNode = new User();
    	onlineNode.setUid(Resource.NotTreeNodeSign);
    	onlineNode.setNickName("在线好友");
    	
    	offlineNode = new User();
    	offlineNode.setUid(Resource.NotTreeNodeSign);
    	offlineNode.setNickName("下线好友");
    	
    	groupNode = new Qun();
    	groupNode.setQunID(Resource.NotTreeNodeSign);
    	groupNode.setQunName("我的群");
    	
    	root = new DefaultMutableTreeNode(rootNode);
    	friendSubTree = new DefaultMutableTreeNode(friendNode);
    	onlineSubTree = new DefaultMutableTreeNode(onlineNode);
    	offlineSubTree = new DefaultMutableTreeNode(offlineNode);
    	groupSubTree = new DefaultMutableTreeNode(groupNode);

    	tree = new JTree(root);
        treeMode = (DefaultTreeModel) tree.getModel();
        treeMode.insertNodeInto(friendSubTree, root, 0);
        treeMode.insertNodeInto(groupSubTree, root, 1);
        treeMode.insertNodeInto(onlineSubTree, friendSubTree, 0);
        treeMode.insertNodeInto(offlineSubTree, friendSubTree, 1);
        
        tree.setCellRenderer(new JTreeCellRenderer());
        tree.setRowHeight(Resource.HeadImaageHeight + 10);
   
        tree.scrollPathToVisible(new TreePath(offlineSubTree.getPath()));
    }

    public JTree getJTree() {
    	return tree;
    } 
    

    
    /***
     * 好友上线
     * 
     * 先在 下线用户列表 中删除好友
     * 再在 上线用户列表 中添加好友
     * @param ID
     */
    public void onlining(int ID) {
        User user = deleteNodeFromOfflineTree(ID);
        
        if(user != null)
        	addNodeToOnlineTree(user);
    }
    
    
    /***
     * 好友下线
     * 
     * 先在 上线用户列表 中删除好友
     * 再在 下线用户列表 中添加好友
     * @param ID
     */
    public void offlining(int ID) {
        User user = deleteNodeFromOnlineTree(ID);
       
        if(user != null)
        	addNodeToOfflineTree(user);
    }


   /***
   * 设置 上线用户列表 
   * @param users
   */
   public void setOnlineFriend(List<User> users) {
	   for(int i = 0; i < users.size(); i++) {
		   User user = users.get(i);
		   addNodeToOnlineTree(user);
	   }
   }
   
   
  /***
   * 设置 下线用户列表 
   * @param users
   */
   public void setOfflineFriend(List<User> users) {
	   for(int i = 0; i < users.size(); i++) {
		   User user = users.get(i);
		   addNodeToOfflineTree(user);
	   }
   }

   /***
   * 设置 上线用户列表 
   * @param user
   */
   public void setOnlineFriend(User user) {
		   addNodeToOnlineTree(user);
   }
   
   
  /***
   * 设置 下线用户列表 
   * @param user
   */
   public void setOfflineFriend(User user) {
		   addNodeToOfflineTree(user);
   }
   
   
   /***
   * 向“在线好友"添加子节点
   * @param id
   */
    private void addNodeToOnlineTree(User user) {
    	user.online();
    	
        treeMode = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(user);
        treeMode.insertNodeInto(newNode, onlineSubTree, onlineSubTree.getChildCount());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); 
        tree.scrollPathToVisible(new TreePath(newNode.getPath()));
    }

    /***
     * “在线好友"删除子节点
     * @param id
     */
    private User deleteNodeFromOnlineTree(int ID)
    {
    	User user = null;
    	
        DefaultMutableTreeNode n = new DefaultMutableTreeNode();
        if (onlineSubTree.getChildCount() > 0) {
            for (Enumeration e = onlineSubTree.children(); e.hasMoreElements();) {
                n = (DefaultMutableTreeNode) e.nextElement();
                User tempUser = (User) n.getUserObject();
                
                if (tempUser.getUid() == ID) {
                	user = tempUser;
                	
                    onlineSubTree.remove(n);
                    treeMode.reload();
                    tree.scrollPathToVisible(new TreePath(n.getPath()));
                }
            }
        }
        
        return user;
    }
    
    /***
     * 向“下线好友"添加子节点
     * @param id
     */
    private void addNodeToOfflineTree(User user) {
    	user.offline();
        treeMode = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(user); 
        treeMode.insertNodeInto(newNode, offlineSubTree, offlineSubTree.getChildCount());
        tree.scrollPathToVisible(new TreePath(newNode.getPath()));
    }

    
    /***
     * "下线好友" 中删除子节点
     * @param user
     * @param id
     */
    private User deleteNodeFromOfflineTree(int ID)// 删除节点
    {
    	User user = null;
    	
        DefaultMutableTreeNode n = new DefaultMutableTreeNode();
        if (offlineSubTree.getChildCount() > 0) {
            for (Enumeration e = offlineSubTree.children(); e.hasMoreElements(); ) {
                n = (DefaultMutableTreeNode) e.nextElement();
                User tempUser = (User) n.getUserObject();
                if (tempUser.getUid() == ID) {
                	user = tempUser;
                	
                    offlineSubTree.remove(n);
                    treeMode.reload();
                    tree.scrollPathToVisible(new TreePath(n.getPath()));
                }
            }
        }
        
        return user;
    }
    
    /***
     * 添加群列表
     * @param groupList
     */
    public void addNodeGroupTree(List<Qun> groupList) {
    	for(int i = 0; i < groupList.size(); i++) {
    		addNodeGroupTree(groupList.get(i));
    	}
    }
    
    /***
     * 向“我的群"添加子节点
     * @param group
     */
      public void addNodeGroupTree(Qun group) {
          treeMode = (DefaultTreeModel) tree.getModel();
          DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(group);
          treeMode.insertNodeInto(newNode, groupSubTree, groupSubTree.getChildCount());
          tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION); 
          tree.scrollPathToVisible(new TreePath(newNode.getPath()));
      }
}

