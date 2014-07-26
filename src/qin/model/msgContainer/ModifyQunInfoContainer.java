package qin.model.msgContainer;

import java.io.Serializable;

import qin.model.domainClass.Qun;

public class ModifyQunInfoContainer implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Qun qun;
	
	public ModifyQunInfoContainer(Qun _qun) {
		qun = _qun;
	}

	public ModifyQunInfoContainer(ModifyQunInfoContainer muic) {
		qun = muic.qun;
	}
	
	public void setQun(Qun _qun) {
		qun = _qun;
	}
	
	public Qun getQun() {
		return qun;
	}
}