package qin.ui.uiElement;

import java.awt.Component;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import qin.model.Resource;
import qin.model.domainClass.Qun;
import qin.model.domainClass.User;




class JTreeCellRenderer extends DefaultTreeCellRenderer {
	public JTreeCellRenderer() {
    	
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        String imagePath = "";
        int ID = 0;
        String name = "";
        
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        Object obj = node.getUserObject();
        if (obj instanceof User) {
        	imagePath = (((User)obj).isUserOnline() ? Resource.OnLineHeadImagePath : Resource.OffLineHeadImagePath) + ((User)obj).getHeadImage();
        	ID = ((User)obj).getUid();
        	name = ((User)obj).getNickName();
        } else {
        	imagePath = Resource.QunLogo;
        	ID = ((Qun)obj).getQunID();
        	name =  ((Qun)obj).getQunName();
        }
        
        if(ID == Resource.NotTreeNodeSign) {
        	setText(name);
        	setIcon(null);
        } else {
        	setText(name + "(" + ID + ")");
        	setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getResource(imagePath))));
        }
  
        return this;
    }

    protected boolean isTutorialBook(Object value) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

        return false;
    }
}

