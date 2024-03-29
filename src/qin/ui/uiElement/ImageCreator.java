package qin.ui.uiElement;
import javax.swing.ImageIcon;

/***
 * 图片生成
 */
public class ImageCreator {
	
	/***
	 * Load the images and create an array of indexes.
	 * @param path
	 * @param files
	 * @return
	 */
    public static ImageIcon[] createImageIcons(String path, String[] files) {
    	ImageIcon[]  images = new ImageIcon[files.length];
        for (int i = 0; i < files.length; i++) {
            images[i] = createImageIcon(path + files[i]);
            if (images[i] != null) {
                images[i].setDescription(files[i]);
            }
        }

        return images;
    }

    /***
     *  Returns an ImageIcon, or null if the path was invalid
     * @param path
     * @return
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ImageCreator.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
                return null;
        }
    }
}

