package qin.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import qin.model.Resource;
import qin.model.domainClass.Address;
import qin.model.domainClass.User;
import qin.ui.uiElement.ComboBoxRenderer;
import qin.ui.uiElement.ImageCreator;



/***
 * 
 * @author liulingbo
 *
 */
public class RegisterUI implements MouseListener{
	int width = 300;
	int height = 400;
	
	private JFrame jFrame = null;

	private JPanel jContentPane = null;

	// 用户昵称输入框
	private JTextField nicknameTextField = null;
	// 用户密码输入框
	private JPasswordField passwordField = null;
    // 确认密码输入框
	private JPasswordField validationPasswordField = null;
	// 电子邮箱输入框
	private JTextField emailTextField = null;
	// 年龄输入框
	private JTextField ageTextField = null;
	// 用户性别选择
	private JComboBox genderComboBox = null;
	// 用户省份选择
	private JComboBox provinceComboBox = null;
	// 用户城市选择
	private JComboBox cityComboBox = null;
	// 用户头像选择
	private JComboBox headImageComboBox = null;

	
	// 注册按钮
	private JButton registerButton = null;
	// 取消按钮
	private JButton giveUpButton = null;

	private JLabel nicknameTip = null;
	
	private JLabel passwordTip = null;
	
	private JLabel validationPasswordTip = null;
	
	private JLabel emailTip = null;
	
	public RegisterUI() {
		initJFrame();
	}
	
	public void showRegisterUI() {
		jFrame.setVisible(true);
	}
	
	public void hideRegisterUI() {
		jFrame.setVisible(false);
	}
	
	/**
	 * 初始化jFrame
	 */
	private JFrame initJFrame()
	{
		if (jFrame == null)
		{
			jFrame = new JFrame();
			jFrame.setTitle("用户注册");
			jFrame.setSize(new Dimension(width, height));
			
			Toolkit toolkit = jFrame.getToolkit();
			Dimension screen = toolkit.getScreenSize();
			jFrame.setBounds(screen.width/2 - width/2, screen.height/2 - height/2, width, height); 
			
			jFrame.setContentPane(getJContentPane());
			jFrame.setResizable(false);
			jFrame.setVisible(false);
			
			
			// 添加窗口时间监听
			jFrame.setDefaultCloseOperation(1);
                        jFrame.addWindowListener(new WindowAdapter() {
                        @Override
						public void windowClosing(WindowEvent e)
						{
							System.exit(0);
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
			
			jContentPane.add(getNicknameLable());
			jContentPane.add(getNicknameTextField());
			jContentPane.add(getNicknameTip());
			
			jContentPane.add(getPasswordLabel());
			jContentPane.add(getPasswordField());
			jContentPane.add(getPasswordTip());
			
			jContentPane.add(getValidationPasswordLabel());
			jContentPane.add(getValidationPasswordField());
			jContentPane.add(getValidationPasswordTip());
			
			jContentPane.add(getEmialLabel());
			jContentPane.add(getEmailTextField());
			jContentPane.add(getEmailTip());
			
			jContentPane.add(getGenderLabel());
			jContentPane.add(getGenderComboBox());
			
			jContentPane.add(getAgeLabel());
			jContentPane.add(getAgeTextField());
			
			jContentPane.add(getAddressLabel());
		
			jContentPane.add(getCityComboBox());
			jContentPane.add(getCityLabel());
			jContentPane.add(getProvinceComboBox());
			jContentPane.add(getProvinceLabel());
			
			jContentPane.add(getHeadImageLabel());
			jContentPane.add(getheadImageComboBox());
			
			
			jContentPane.add(getGiveUpButton());
			jContentPane.add(getRegisterButton());
			
		}
		return jContentPane;
	}

	
	private JLabel getNicknameLable() {
		JLabel nicknameLabel = new JLabel();
		nicknameLabel.setBounds(new Rectangle(width/15, height/15, width/5, height/15));
		nicknameLabel.setText("用户昵称:");
		
		return nicknameLabel;
	}
	
	/**
	 * 初始化 NicknameTextField
	 */
	private JTextField getNicknameTextField()
	{
		if (nicknameTextField == null)
		{
			nicknameTextField = new JTextField();
			nicknameTextField.setBorder(new LineBorder(Color.black, 1, true));
			nicknameTextField.setBounds(new Rectangle(width*3/10, height/15, width/2,  height/15));
			
			nicknameTextField.addMouseListener(this);
			// 限制只能输入10个字母
			nicknameTextField.addKeyListener(new KeyAdapter(){
				public void keyTyped(KeyEvent e) {				
					if(nicknameTextField.getText().length() >= 10) {	
						e.consume(); 
					}
				}
			});
		}
		
		return nicknameTextField;
	}
	
	private JLabel getNicknameTip() {

		if(nicknameTip == null) {
			nicknameTip = new JLabel();
			nicknameTip.setBounds(new Rectangle(width*8/10, height*1/15, width/5, height/15));
			nicknameTip.setText("需非空");
			nicknameTip.setFont(new Font("Dialog", 0, 10));;
			nicknameTip.setForeground(Color.RED);;
			nicknameTip.setVisible(true);
		}
		
		return nicknameTip; 
	} 
	
	private JLabel getPasswordLabel() {
		JLabel passwordLabel0 = new JLabel();
		passwordLabel0.setBounds(new Rectangle(width/15, height*2/15, width/5, height/15));
		passwordLabel0.setText("用户密码:");
		
		return passwordLabel0;
	}
	
	/**
	 * 初始化jPasswordField
	 */
	private JPasswordField getPasswordField() {
		if (passwordField == null)
		{
			passwordField = new JPasswordField();
			passwordField.setBorder(new LineBorder(Color.black, 1, true));
			passwordField.setBounds(new Rectangle(width*3/10, height*2/15, width/2,  height/15));
			
			passwordField.addMouseListener(this);
			// 限制只能输入10个字母
			passwordField.addKeyListener(new KeyAdapter(){
				public void keyTyped(KeyEvent e) {				
					if(passwordField.getText().length() >= 10) {	
							e.consume(); 
					}
				}
			});
		}
		
		return passwordField;
	}
	
	private JLabel getPasswordTip() {
		if(passwordTip == null) {
			passwordTip = new JLabel();
			passwordTip.setBounds(new Rectangle(width*8/10, height*2/15, width/5, height/15));
		    passwordTip.setText("需非空");
			passwordTip.setFont(new Font("Dialog", 0, 10));;
			passwordTip.setForeground(Color.RED);;
		    passwordTip.setVisible(true);
		}
		
		return passwordTip;
	} 

	private JLabel getValidationPasswordLabel() {
		JLabel validationPasswordLabel = new JLabel();
		validationPasswordLabel.setBounds(new Rectangle(width/15, height*3/15, width/5, height/15));
		validationPasswordLabel.setText("确认密码:");
		
		return validationPasswordLabel;
	}
	
	/**
	 * 初始化jPasswordField
	 */
	private JPasswordField getValidationPasswordField()
	{
		if (validationPasswordField == null)
		{
			validationPasswordField = new JPasswordField();
			validationPasswordField.setBorder(new LineBorder(Color.black, 1, true));
			validationPasswordField.setBounds(new Rectangle(width*3/10, height*3/15, width/2,  height/15));
			
			validationPasswordField.addMouseListener(this);			// 限制只能输入10个字母
			validationPasswordField.addKeyListener(new KeyAdapter(){
				public void keyTyped(KeyEvent e) {				
					if(validationPasswordField.getText().length() >= 10) {	
							e.consume(); 
					}
				}
			});
			
		}
		
		return validationPasswordField;
	}
	
	private JLabel getValidationPasswordTip() {
		if(validationPasswordTip == null) {
			validationPasswordTip = new JLabel();
			validationPasswordTip.setBounds(new Rectangle(width*8/10, height*3/15, width/5, height/15));
			validationPasswordTip.setText("需非空");
			validationPasswordTip.setFont(new Font("Dialog", 0, 10));;
			validationPasswordTip.setForeground(Color.RED);;
			validationPasswordTip.setVisible(true);
		}
		
		return validationPasswordTip; 
	} 

	private JLabel getEmialLabel() {
		JLabel emailLabel = new JLabel();
		emailLabel.setBounds(new Rectangle(width/15, height*4/15, width/5, height/15));
		emailLabel.setText("电子邮箱:");
		
		return emailLabel;
	}
	
	/**
	 * 初始化 emailTextField
	 */
	private JTextField getEmailTextField() {
		if(emailTextField == null)
		{
			emailTextField = new JTextField();
			emailTextField.setBorder(new LineBorder(Color.black, 1, true));
			emailTextField.setBounds(new Rectangle(width*3/10, height*4/15, width/2,  height/15));
			emailTextField.addMouseListener(this);
			emailTextField.addKeyListener(new KeyAdapter(){
				public void keyTyped(KeyEvent e) {
					isEmailCorrect();
				}
			});
		}
		
		return emailTextField;
	}
	
	private JLabel getEmailTip() {
		if(emailTip == null) {
			emailTip = new JLabel();
			emailTip.setBounds(new Rectangle(width*8/10, height*4/15, width/5, height/15));
			emailTip.setText("邮箱格式错误");
			emailTip.setFont(new Font("Dialog", 0, 10));;
			emailTip.setForeground(Color.RED);;
			emailTip.setVisible(false);
		}
			
		return emailTip; 
	} 
	
	private JLabel getAgeLabel() {
		JLabel ageLabel = new JLabel();
		ageLabel.setBounds(new Rectangle(width/15, height*5/15, width/5, height/15));
		ageLabel.setText("年      龄:");
		
		return ageLabel;
	}
	
	/**
	 * 初始化 ageTextField
	 */
	private JTextField getAgeTextField() {
		if(ageTextField == null)
		{
			ageTextField = new JTextField();
			ageTextField.setBorder(new LineBorder(Color.black, 1, true));
			ageTextField.setBounds(new Rectangle(width*3/10, height*5/15, width/5, height/15));
			
			// 限制只能输入2个数字
			ageTextField.addKeyListener(new KeyAdapter(){
				public void keyTyped(KeyEvent e) {
					int keyChar = e.getKeyChar();				
					if(keyChar < KeyEvent.VK_0 || keyChar > KeyEvent.VK_9 || ageTextField.getText().length() >= 2 
					   || (ageTextField.getText().length() == 1 && ageTextField.getText().equals("0"))) {
						
						e.consume(); //关键，屏蔽掉非法输入
					}else{
						//System.out.println(ageTextField.getText());
					}
				}
			});
			
		
			
		}
		
		return ageTextField;
	}
	
	private JLabel getGenderLabel() {
		JLabel genderLabel = new JLabel();
		genderLabel.setBounds(new Rectangle(width/15, height*6/15, width/5, height/15));
		genderLabel.setText("性      别:");
	
		return genderLabel;
	}

	/**
	 * 初始化 GenderComboBox
	 */
	private JComboBox getGenderComboBox()
	{
		if (genderComboBox == null)
		{
			String[] sex = { "男", "女", "保密"};
			genderComboBox = new JComboBox(sex);
			genderComboBox.setSelectedIndex(0);
			genderComboBox.setBounds(new Rectangle(width*3/10, height*6/15, width/3, height/15));
		}
		return genderComboBox;
	}
	
	
	private JLabel getAddressLabel() {
		JLabel addressLabel = new JLabel();
		addressLabel.setBounds(new Rectangle(width/15, height*7/15, width/5, height/15));
		addressLabel.setText("地      址:");
	
		return addressLabel;
	}

	private JLabel getProvinceLabel() {
		JLabel provinceLabel = new JLabel();
		provinceLabel.setBounds(new Rectangle(width*2/3, height*7/15, width/5, height/15));
		provinceLabel.setText("省");
	
		return provinceLabel;
	}
	
	/**
	 * 初始化 ProvinceComboBox
	 */
	private JComboBox getProvinceComboBox()
	{
		if (provinceComboBox == null)
		{
			String[] allProvince = Address.getProvinces();
			
			provinceComboBox = new JComboBox(allProvince);
			provinceComboBox.setSelectedIndex(0);
			provinceComboBox.setBounds(new Rectangle(width*3/10, height*7/15, width/3, height/15));
			provinceComboBox.setMaximumRowCount(allProvince.length);
			
			provinceComboBox.addActionListener(new ActionListener() {
				/***
				 *  通过 选择省份，动态改变 城市列表
				 */
				@Override
				public void actionPerformed(ActionEvent e) {
			
					String selectedProvince=((JComboBox)e.getSource()).getSelectedItem().toString();
					
					JComboBox callee = getCityComboBox();
					callee.removeAllItems();
					
					String[] allCities = Address.getCitiesByProvinceName(selectedProvince);
					for(int i = 0; i < allCities.length; i++) {
						callee.addItem(allCities[i]);
					}
					callee.setMaximumRowCount(allCities.length);
				}
			}
			);
		}
		
		return provinceComboBox;
	}
	
	private JLabel getCityLabel() {
		JLabel cityLabel = new JLabel();
		cityLabel.setBounds(new Rectangle(width*2/3, height*8/15, width/5, height/15));
		cityLabel.setText("市");
	
		return cityLabel;
	}
	
	
	/**
	 * 初始化 CityComboBox
	 */
	private JComboBox getCityComboBox()
	{
		if (cityComboBox == null)
		{
			String[] cities = {"--"};
			cityComboBox = new JComboBox(cities);
			cityComboBox.setSelectedIndex(0);
			cityComboBox.setBounds(new Rectangle(width*3/10, height*8/15, width/3, height/15));
		}

		return cityComboBox;
	}
	
	private JLabel getHeadImageLabel() {
		JLabel getHeadImageLabel = new JLabel();
		getHeadImageLabel.setBounds(new Rectangle(width/15, height*9/15, width/5, height/15));
		getHeadImageLabel.setText("头     像:");
	
		return getHeadImageLabel;
	}
	
	/**
	 * 初始化 CityComboBox
	 */
	private JComboBox getheadImageComboBox()
    {
        if (headImageComboBox == null)
        {
            int imageWidth = Resource.HeadImaageWidth + 20;
            int imageHeight = Resource.HeadImaageHeight + 20;
            
            String[] files = Resource.HeadImageFiles;
            String Path = Resource.OnLineHeadImagePath;
            ImageIcon[] images = ImageCreator.createImageIcons(Path, files);
            
            ComboBoxRenderer renderer= new ComboBoxRenderer();
            renderer.setPreferredSize(new Dimension(imageWidth, imageHeight));
            
            headImageComboBox = new JComboBox(images);
            headImageComboBox.setRenderer(renderer);
            headImageComboBox.setSelectedIndex(0);
            headImageComboBox.setMaximumRowCount(images.length);
            headImageComboBox.setBounds(new Rectangle(width*3/10, height*9/15,  imageWidth + 40 , imageHeight));
        }
        
        return headImageComboBox;
    }


	/**
	 * 初始化 RegisterButton
	 */
	public JButton getRegisterButton()
	{
		if (registerButton == null)
		{
			registerButton = new JButton();
			registerButton.setBounds(new Rectangle(width*2/15, height*11/14, width/3, height*1/12));
			registerButton.setText("注册");
			
			registerButton.setEnabled(false);
			registerButton.addMouseListener(this);
		}
		
		return registerButton;
	}

	/**
	 * 初始化 giveUpButton
	 */
	public JButton getGiveUpButton()
	{
		if (giveUpButton == null)
		{
			giveUpButton = new JButton();
			giveUpButton.setBounds(new Rectangle(width*8/15, height*11/14, width/3, height*1/12));
			giveUpButton.setText("取消");
		}
		return giveUpButton;
	}
	


	@Override
	public void mouseClicked(MouseEvent e) {
		if(isMessageTrue())
			getRegisterButton().setEnabled(true);
		else 
			getRegisterButton().setEnabled(false);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		if(isMessageTrue())
			getRegisterButton().setEnabled(true);
		else 
			getRegisterButton().setEnabled(false);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		if(isMessageTrue())
			getRegisterButton().setEnabled(true);
		else 
			getRegisterButton().setEnabled(false);
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		if(isMessageTrue())
			getRegisterButton().setEnabled(true);
		else 
			getRegisterButton().setEnabled(false);
	}
	
	private boolean isMessageTrue() {
		if( isNicknameCorrect() && isPasswordCorrent() && isValidationPasswordCorrect() && isEmailCorrect()) {	
			return true;
		}
		return false;
	}
	
	private boolean isNicknameCorrect() {
		if(nicknameTextField.getText().trim().equals("")) {
			nicknameTip.setVisible(true);
			getRegisterButton().setEnabled(false);
			return false;
		} else {
			nicknameTip.setVisible(false);
			return true;
		}
	}
	
	private boolean isPasswordCorrent() {
		if(passwordField.getText().trim().equals("")) {
			passwordTip.setVisible(true);
			getRegisterButton().setEnabled(false);
			return false;
		} else {
			passwordTip.setVisible(false);
			return true;
		}
	}
	
	private boolean isValidationPasswordCorrect() {
		if(validationPasswordField.getText().trim().equals("")) {
			validationPasswordTip.setText("需非空");
			validationPasswordTip.setVisible(true);
			getRegisterButton().setEnabled(false);
			return false;
		} else if(!validationPasswordField.getText().trim().equals(passwordField.getText().trim())) {
			validationPasswordTip.setText("密码不匹配");
			validationPasswordTip.setVisible(true);
			getRegisterButton().setEnabled(false);
			return false;
		} else {
			validationPasswordTip.setVisible(false);
			return true;
		}
	}
	
	private boolean isEmailCorrect() {
		if(!emailTextField.getText().trim().equals("") && !isMatchEmail(emailTextField.getText().trim())) {
			emailTip.setVisible(true);
			getRegisterButton().setEnabled(false);
			return false;
		} else {
			emailTip.setVisible(false);
			return true;
		}
	}
	
	
	boolean isMatchEmail(String email) {
	    String check = "[0-9a-zA-Z]+([_.]*[0-9a-zA-Z]+)*@[0-9a-zA-Z]+.[0-9a-zA-Z]+(.*[0-9a-zA-Z]+)*";
	    Pattern p = Pattern.compile(check);
	    Matcher m = p.matcher(email);
	    return m.matches();
	}
	
	public User getRegisterUserInfo() {
		User user = new User();
		
		user.setNickName(nicknameTextField.getText());
		user.setPassword(passwordField.getText());
		user.setEmail(emailTextField.getText());
		
		
		if(!ageTextField.getText().equals(""))
			user.setAge(new Integer(ageTextField.getText()));
		else 
			user.setAge(new Integer(0));
		
		user.setGender((String) genderComboBox.getSelectedItem());
		
        Address address = new Address((String) provinceComboBox.getSelectedItem(), (String) cityComboBox.getSelectedItem());
        user.setAddress(address);
		
        user.setHeadImage((headImageComboBox.getSelectedIndex()+1) + ".png");
        
		return user;
	}
	
	public void showMessage(int uid) {
		JOptionPane.showMessageDialog(null, "注册成功\n您的Qin账号为 " + uid + "\n", "注册成功", JOptionPane.DEFAULT_OPTION);
	}
	
	public void showErrorMessage() {
		JOptionPane.showMessageDialog(null, "注册失败\n请稍后再试\n", "注册失败", JOptionPane.ERROR_MESSAGE);
	}
	
}
