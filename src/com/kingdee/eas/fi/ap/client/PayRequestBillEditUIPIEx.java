package com.kingdee.eas.fi.ap.client;

import java.awt.event.ActionEvent;

import com.kingdee.eas.custom.BusinessFormOAFactory;
import com.kingdee.eas.custom.IBusinessFormOA;
import com.kingdee.eas.fi.ar.BillStatusEnum;

public class PayRequestBillEditUIPIEx extends PayRequestBillEditUI {

	public PayRequestBillEditUIPIEx() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 6781534697020796793L;

	@Override
	public void actionEdit_actionPerformed(ActionEvent e) throws Exception {
		//if(this.editData.getBillStatus() != BillStatusEnum.SUBMITED)
 		super.actionEdit_actionPerformed(e);
	}

	@Override
	public void actionRemove_actionPerformed(ActionEvent e) throws Exception {
	//	if(this.editData.getBillStatus() != BillStatusEnum.SUBMITED)
 		super.actionRemove_actionPerformed(e);
	}
	
}
