package qin.testcase;

import java.util.ArrayList;
import java.util.List;

import qin.model.Resource;
import qin.model.domainClass.Qun;
import qin.model.domainClass.User;



public class StaticTestCase {

	public static User getTestingUser() {
		
		User user = new User();
		user.setUid(11331210);
		user.setNickName("liu lingbo");
		user.setPassword("nopassword");
		user.setEmail("1239682118@qq.com");
		user.setAge(22);
		user.setGender("男");
		user.setHeadImage("1.png");
		user.setIPAddr("127.0.0.1");
		user.setPort(7777);
		
		return user;
	}
	
	public static List<User> getOnlineFriends() {
		List<User>  users = new ArrayList<User>();

		for(int i = 1; i < 7; i++) {
			User u = getTestingUser();
			u.setUid(i*100000);
			u.setHeadImage((i%6==0 ? 6 : i%6) + ".png");
			u.online();
			users.add(u);
		}

		return users;
	}

	public static List<User> getOfflineFriends() {
		List<User>  users = new ArrayList<User>();

		for(int i = 1; i < 20; i++) {
			User u = getTestingUser();
			u.setUid(i);
			u.setHeadImage((i%6==0 ? 6 : i%6) + ".png");
			u.offline();
			users.add(u);
		}

		return users;
	}
	
	public static Qun getGroup() {
		Qun group = new Qun();
		
		group.setQunID(12345678);
		group.setQunName("Class one");
		
		group.setQunOwnerID(11331210);
		group.setQunMember(getOnlineFriends());
		group.setQunDescription("这是中山大学软件学院11级1班群。请所有加入群的同学把昵称给成：学号＋ 姓名");
		
		return group;
	}
	
	
	public static List<Qun> getGroupList() {
		List<Qun> groupList = new ArrayList<Qun>();
		
		for(int i = 1; i < 6; i++) {
			Qun g = getGroup();
			
			g.setQunID(i * 1000);
			
			groupList.add(g);
		}
		
		return groupList;
	}
}
