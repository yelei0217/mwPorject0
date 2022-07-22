package com.kingdee.eas.fi.ap.client;

import java.awt.event.ActionEvent;
import java.util.Iterator;
import java.util.Set;

import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.metadata.entity.FilterInfo;
import com.kingdee.bos.metadata.entity.FilterItemInfo;
import com.kingdee.bos.metadata.query.util.CompareType;
import com.kingdee.eas.fi.ap.IPayRequestBill;
import com.kingdee.eas.fi.ap.PayRequestBillCollection;
import com.kingdee.eas.fi.ap.PayRequestBillFactory;
import com.kingdee.eas.fi.ap.PayRequestBillInfo;
import com.kingdee.eas.fi.ar.BillStatusEnum;
import com.kingdee.eas.util.SysUtil;
import com.kingdee.eas.util.client.MsgBox;

public class PayRequestBillListUIPIEx extends PayRequestBillListUI {

	public PayRequestBillListUIPIEx() throws Exception {
		super();
	//	this.btnCancel.setVisible(true);
	//	this.btnCancel.setText("关闭");
	}

	
	@Override
	public void actionCancel_actionPerformed(ActionEvent e) throws Exception {
 		super.actionCancel_actionPerformed(e);
	}


	/**
	 * 
	 */
	private static final long serialVersionUID = -4598400163467210852L;

	
	@Override
	public void actionQuery_actionPerformed(ActionEvent e) throws Exception {
 		super.actionQuery_actionPerformed(e);
	}

	@Override
	protected PayRequestBillQueryUI getUserPanel() throws Exception {
 		//return super.getUserPanel();
	       if(userPanel == null)
		          userPanel = new PayRequestBillQueryUICTEx(getUIParam());
		    return userPanel;
	}
	
	@Override
	public void actionEdit_actionPerformed(ActionEvent e) throws Exception {
//		Set ids = getSelectedIdSet();
//		boolean flag = false ;
//		IPayRequestBill ipbf = PayRequestBillFactory.getRemoteInstance();  
//		 FilterInfo filter = new FilterInfo();
//		 filter.getFilterItems().add(new FilterItemInfo("ID", ids, CompareType.INCLUDE));
// 		 EntityViewInfo view = new EntityViewInfo();
// 		 view.setFilter(filter);
// 		PayRequestBillCollection coll = ipbf.getPayRequestBillCollection(view);
//		if(coll != null && coll.size() >0){
//			Iterator it = coll.iterator();
// 			while (it.hasNext()) {
// 				PayRequestBillInfo info = (PayRequestBillInfo) it.next();
// 				if (BillStatusEnum.SUBMITED == info.getBillStatus()) {
// 					flag = true ;
// 					break;
//				}
//			} 
//		} 
//		if(flag){
//			MsgBox.showError("提交状体不允许删除");
//		}else{
//	 		super.actionEdit_actionPerformed(e); 
//		}
		super.actionEdit_actionPerformed(e); 
	}



	@Override
	public void actionRemove_actionPerformed(ActionEvent e) throws Exception {
//		Set ids = getSelectedIdSet();
//		boolean flag = false ;
//		IPayRequestBill ipbf = PayRequestBillFactory.getRemoteInstance();  
//		 FilterInfo filter = new FilterInfo();
//		 filter.getFilterItems().add(new FilterItemInfo("ID", ids, CompareType.INCLUDE));
// 		 EntityViewInfo view = new EntityViewInfo();
// 		 view.setFilter(filter);
// 		PayRequestBillCollection coll = ipbf.getPayRequestBillCollection(view);
//		if(coll != null && coll.size() >0){
//			Iterator it = coll.iterator();
// 			while (it.hasNext()) {
// 				PayRequestBillInfo info = (PayRequestBillInfo) it.next();
// 				if (BillStatusEnum.SUBMITED == info.getBillStatus()) {
// 					flag = true ;
// 					break;
//				}
//			} 
//		} 
//  
//		if(flag){
//			MsgBox.showError("提交状体不允许删除");
//			SysUtil.abort();
//		}else{
//	 		super.actionRemove_actionPerformed(e); 
//		}
//		
		super.actionRemove_actionPerformed(e); 
	}
	
}
