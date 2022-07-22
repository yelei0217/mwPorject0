package com.kingdee.eas.scm.cal.client;

import java.awt.event.ActionEvent;

import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.mw.srqr.ISaleIssueSyncFacade;
import com.kingdee.eas.mw.srqr.SaleIssueSyncFacadeFactory;
import com.kingdee.eas.scm.im.inv.client.InvClientUtils;
import com.kingdee.eas.util.SysUtil;
import com.kingdee.eas.util.client.MsgBox;

public class CloseAccountClientUICTEx extends CloseAccountClientUI {

	public CloseAccountClientUICTEx() throws Exception {
		super();
 	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 9223079898401397538L;

	@Override
	public void actionCloseAccount_actionPerformed(ActionEvent e)
			throws Exception {
		 if (this.btnEndAccount.isSelected()) {
			 if (!this.checkData()) {
		            SysUtil.abort();
		        }else{
					  Object[] co = null;
				        if (this.prmtCompanyF7.getValue() instanceof Object[]) {
				            co = (Object[])this.prmtCompanyF7.getValue();
				        }else {
				            co = new Object[] { this.prmtCompanyF7.getValue() };
				        }
				        if (!(co == null || co[0] == null)) {
				        	 ISaleIssueSyncFacade issf = SaleIssueSyncFacadeFactory.getRemoteInstance();
				        	 for(Object obj :co){
				        		 CompanyOrgUnitInfo info = (CompanyOrgUnitInfo) obj ;
				        		 issf.syncSaleIssByCompany(info.getId().toString());
 				        	 }
				        }
		        }
		 }
 		super.actionCloseAccount_actionPerformed(e);
	}
	   private boolean checkData() {
	        Object[] co = null;
	        if (this.prmtCompanyF7.getValue() instanceof Object[]) {
	            co = (Object[])this.prmtCompanyF7.getValue();
	        }
	        else {
	            co = new Object[] { this.prmtCompanyF7.getValue() };
	        }
	        if (co == null || co[0] == null) {
	           // MsgBox.showInfo(InvClientUtils.getResource("CompanyIsNull"));
	            return false;
	        }
	        return true;
	    }
	
}
