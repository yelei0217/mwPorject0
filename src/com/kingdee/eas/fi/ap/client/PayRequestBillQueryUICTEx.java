package com.kingdee.eas.fi.ap.client;

import java.util.HashMap;

import com.kingdee.eas.fi.ap.PayReqFilterEnum;

public class PayRequestBillQueryUICTEx  extends PayRequestBillQueryUI{

	public PayRequestBillQueryUICTEx() throws Exception {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2032509478433671481L;

	@Override
	public void onLoad() throws Exception {
 		super.onLoad();
 		//cmbBillState.addItem(PayReqFilterEnum.DELETED);
	}
	
	  public PayRequestBillQueryUICTEx(HashMap uiParam)throws Exception
	  {
		    super.uiParam = uiParam;
	  }
	
}
