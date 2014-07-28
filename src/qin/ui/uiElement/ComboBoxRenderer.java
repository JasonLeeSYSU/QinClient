package qin.ui.uiElement;

import java.awt.Component;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/***
 * 定义一种可以显示图片的下拉菜单布局
 */
@SuppressWarnings({ "serial", "rawtypes" })
public class ComboBoxRenderer extends JLabel implements ListCellRenderer {
        private Font uhOhFont;

        public ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            ImageIcon icon = (ImageIcon) value;
            setIcon(icon);
          
            return this;
        }

        /***
         * Set the font and text when no image was found.
         * @param uhOhText
         * @param normalFont
         */
        
        protected void setUhOhText(String uhOhText, Font normalFont) {
            if (uhOhFont == null) { 
                uhOhFont = normalFont.deriveFont(Font.ITALIC);
            }
            setFont(uhOhFont);
            setText(uhOhText);
        }
    }
