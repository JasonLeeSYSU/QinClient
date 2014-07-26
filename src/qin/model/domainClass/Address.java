package qin.model.domainClass;

import java.io.Serializable;

public class Address implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String province;
	private String city;
	
	private static String[] allProvince = {"--", "北京", "上海", "天津", "重庆", "河北", "山西", "内蒙古", 
										"黑龙江", "吉林", "辽宁", "陕西", "甘肃", "青海", "山东", "河南", 
										"江苏", "浙江", "安徽", "江西", "福建", "台湾", "湖北", "湖南", 
										"广东", "广西", "海南", "四川", "云南", "贵州","西藏", "新疆", "宁夏", 
										"香港", "澳门"
										};
	
	public Address() {
		province = "--";
		city = "--";
	}
	
	public Address(String _province, String _city) {
		province = _province;
		city = _city;
	}
	
	public String getProvince() {
		return province;
	}
	
	public void setProvince(String _province) {
		province = _province;
	}
	
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String _city) {
		city = _city;
	}

	public String toString() {
		return province+" "+city;
	}
	
	public static String[] getProvinces() {
		
		return allProvince;
	}
	

	public static String[] getCitiesByProvinceName(String provinceName) {
	
		if(provinceName.equals("北京"))
			return new String[] {"北京"};			
		if(provinceName.equals("上海"))
			return new String[] {"上海"};
		if(provinceName.equals("天津")) 
			return new String[] {"天津"};
		if(provinceName.equals("重庆")) 
			return new String[] {"重庆"};
		if(provinceName.equals("香港")) 
			return new String[] {"香港"};
		if(provinceName.equals("澳门")) 
			return new String[] {"澳门"};
		if(provinceName.equals("河北")) 
			return new String[] {"--","石家庄","唐山","秦皇岛","邯郸","邢台","保定","张家口","承德","沧州","廊坊","衡水"};
		if(provinceName.equals("山西")) 
			return new String[] {"--","太原","大同","朔州","阳泉","长治","晋城","忻州","晋中","临汾","运城","吕梁"};
		if(provinceName.equals("陕西")) 
			return new String[] {"--","西安","铜川","宝鸡","咸阳","渭南","延安","汉中","榆林","安康","商洛"};
		if(provinceName.equals("山东")) 
			return new String[] {"--","济南","青岛","淄博","枣庄","东营","烟台","潍坊","济宁","泰安","威海","日照","莱芜","临沂","德州","聊城","滨州","菏泽"};
		if(provinceName.equals("河南")) 
			return new String[] {"--","郑州","开封","洛阳","平顶山","安阳","鹤壁","新乡","焦作","濮阳","许昌","漯河","三门峡","南阳","商丘","信阳","周口","驻马店"};
		if(provinceName.equals("辽宁")) 
			return new String[] {"--","沈阳","大连","鞍山","抚顺","本溪","丹东","锦州","营口","阜新","辽阳","盘锦","铁岭","朝阳","葫芦岛"};
		if(provinceName.equals("吉林")) 
			return new String[] {"--","长春","吉林","四平","辽源","通化","白山","松原","白城"};
		if(provinceName.equals("黑龙江"))
			return new String[] {"--","哈尔滨","齐齐哈尔","鹤岗","双鸭山","鸡西","大庆","伊春","牡丹江","佳木斯","七台河","黑河","绥化"};
		if(provinceName.equals("江苏")) 
			return new String[] {"--","南京","无锡","徐州","常州","苏州","南通","连云港","淮安","盐城","扬州","镇江","泰州","宿迁"};
		if(provinceName.equals("浙江")) 
			return new String[] {"--","杭州","宁波","温州","嘉兴","湖州","绍兴","金华","衢州","舟山","台州","丽水"};
		if(provinceName.equals("安徽")) 
			return new String[] {"--","合肥","芜湖","蚌埠","淮南","马鞍山","淮北","铜陵","安庆","黄山","滁州","阜阳","宿州","巢湖","六安","毫州","池州","宣城"};
		if(provinceName.equals("江西")) 
			return new String[] {"--","南昌","景德镇","萍乡","九江","新余","鹰潭","赣州","吉安","宜春","抚州","上饶"};
		if(provinceName.equals("福建")) 
			return new String[] {"--","福州","厦门","莆田","三明","泉州","漳州","南平","龙岩","宁德"};
		if(provinceName.equals("湖北")) 
			return new String[] {"--","武汉","黄石","十堰","荆州","宜昌","襄樊","鄂州","荆门","孝感","黄冈","咸宁","随州"};
		if(provinceName.equals("湖南")) 
			return new String[] {"--","长沙","株洲","湘潭","衡阳","邵阳","岳阳","常德","张家界","益阳","郴州","永州","怀化","娄底"};
		if(provinceName.equals("四川")) 
			return new String[] {"--","成都","自贡","攀枝花","泸州","德阳","绵阳","广元","遂宁","内江","乐山","南充","眉山","宜宾","广安","达州","雅安","巴中","资阳"};
		if(provinceName.equals("贵州")) 
			return new String[] {"--","贵阳","六盘水","遵义","安顺"};
		if(provinceName.equals("云南")) 
			return new String[] {"--","昆明","曲靖","玉溪","保定","昭通","丽江","普洱","临沧"};	
		if(provinceName.equals("广东")) 
			return new String[] {"--","广州","深圳", "珠海", "汕头", "韶关", "佛山", "江门", "湛江", "茂名","肇庆",
								"惠州","梅州","汕尾","河源","阳江","清远","东莞","中山","潮州","揭阳","云浮" };
		if(provinceName.equals("海南")) 
			return new String[] {"--","海口","三亚"};
		if(provinceName.equals("甘肃")) 
			return new String[] {"--","兰州","金昌","白银","天水","嘉峪关","武威","张掖","平凉","酒泉","庆阳","定西","陇南"};
		if(provinceName.equals("青海")) 
			return new String[] {"--","西宁"};
		if(provinceName.equals("台湾")) 
			return new String[] {"--","台北","高雄","基隆","台中","台南","新竹","嘉义"};
		if(provinceName.equals("内蒙")) 
			return new String[] {"--","呼和浩特","包头","乌海","赤峰","通辽","鄂尔多斯","呼伦贝尔","巴彦淖尔","乌兰察布"};
		if(provinceName.equals("新疆")) 
			return new String[] {"--","乌鲁木齐","克拉玛依"};
		if(provinceName.equals("西藏")) 
			return new String[] {"--","拉萨"};
		if(provinceName.equals("广西")) 
			return new String[] {"--","南宁","柳州","桂林","梧州","北海","防城港","钦州","贵港","玉林","百色","贺州","河池","来宾","崇左"};
		if(provinceName.equals("宁夏")) 
			return new String[] {"--","银川","石嘴山","吴忠","固原","中卫"};
		else
			return new String[] {"--"};
			
	}
}