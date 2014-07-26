package qin.model.msgContainer;

import java.io.Serializable;
import qin.model.domainClass.*;

public class CreateQunContainer implements Serializable {
	private static final long serialVersionUID = 1L;

	private Qun qun;
		
	public CreateQunContainer(Qun _qun){
		qun = _qun;
	}
	
	public Qun getQun() {
		return qun;
	}

	public void setQun(Qun _qun) {
		qun = _qun;
	}
}