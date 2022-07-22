/**
 * output package name
 */
package com.kingdee.eas.custom.client;

import java.awt.event.*;
import org.apache.log4j.Logger;
import com.kingdee.bos.ui.face.CoreUIObject;
import com.kingdee.bos.ui.util.IUIActionPostman;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.eas.framework.*;
import com.kingdee.eas.framework.batchHandler.RequestContext;

/**
 * output class name
 */
public class SupplySyncLogEditUI extends AbstractSupplySyncLogEditUI
{
    private static final Logger logger = CoreUIObject.getLogger(SupplySyncLogEditUI.class);
    
    /**
     * output class constructor
     */
    public SupplySyncLogEditUI() throws Exception
    {
        super();
    }
    /**
     * output loadFields method
     */
    public void loadFields()
    {
        super.loadFields();
    }

    /**
     * output storeFields method
     */
    public void storeFields()
    {
        super.storeFields();
    }

    /**
     * output actionPageSetup_actionPerformed
     */
    public void actionPageSetup_actionPerformed(ActionEvent e) throws Exception
    {
        super.actionPageSetup_actionPerformed(e);
    }


    /**
     * output actionAbout_actionPerformed
     */
    public void actionAbout_actionPerformed(ActionEvent e) throws Exception
    {
       // super.actionAbout_actionPerformed(e);
        
//        RequestContext request = new RequestContext();
//        request.setClassName("com.kingdee.eas.scm.common.app.SCMBillEditUIHandler");
//        request.put("PAYMENTTYPE_ID", "91f078d7-fb90-4827-83e2-3538237b67a06BCA0AB5");
//        request.setClassName("com.kingdee.eas.scm.im.inv.app.SaleIssueBillEditUIHandler");
//        
//         request.put("SCM_SYSTEM_ENUM", SystemEnum.INVENTORYMANAGEMENT);
//         IUIActionPostman handler = super.prepareInit();
//         handler.setRequestContext(request);
//         
        
             
    }

   

    /**
     * output getBizInterface method
     */
    protected com.kingdee.eas.framework.ICoreBase getBizInterface() throws Exception
    {
        return com.kingdee.eas.custom.SupplySyncLogFactory.getRemoteInstance();
    }

    /**
     * output createNewData method
     */
    protected com.kingdee.bos.dao.IObjectValue createNewData()
    {
        com.kingdee.eas.custom.SupplySyncLogInfo objectValue = new com.kingdee.eas.custom.SupplySyncLogInfo();
        objectValue.setCreator((com.kingdee.eas.base.permission.UserInfo)(com.kingdee.eas.common.client.SysContext.getSysContext().getCurrentUser()));
		
        return objectValue;
    }

}