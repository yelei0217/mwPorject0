/**
 * output package name
 */
package com.kingdee.eas.custom.client;

import org.apache.log4j.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.border.*;
import javax.swing.BorderFactory;
import javax.swing.event.*;
import javax.swing.KeyStroke;

import com.kingdee.bos.ctrl.swing.*;
import com.kingdee.bos.ctrl.kdf.table.*;
import com.kingdee.bos.ctrl.kdf.data.event.*;
import com.kingdee.bos.dao.*;
import com.kingdee.bos.dao.query.*;
import com.kingdee.bos.metadata.*;
import com.kingdee.bos.metadata.entity.*;
import com.kingdee.bos.ui.face.*;
import com.kingdee.bos.ui.util.ResourceBundleHelper;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.bos.service.ServiceContext;
import com.kingdee.jdbc.rowset.IRowSet;
import com.kingdee.util.enums.EnumUtils;
import com.kingdee.bos.ui.face.UIRuleUtil;
import com.kingdee.bos.ctrl.swing.event.*;
import com.kingdee.bos.ctrl.kdf.table.event.*;
import com.kingdee.bos.ctrl.extendcontrols.*;
import com.kingdee.bos.ctrl.kdf.util.render.*;
import com.kingdee.bos.ui.face.IItemAction;
import com.kingdee.eas.framework.batchHandler.RequestContext;
import com.kingdee.bos.ui.util.IUIActionPostman;
import com.kingdee.bos.appframework.client.servicebinding.ActionProxyFactory;
import com.kingdee.bos.appframework.uistatemanage.ActionStateConst;
import com.kingdee.bos.appframework.validator.ValidateHelper;
import com.kingdee.bos.appframework.uip.UINavigator;


/**
 * output class name
 */
public abstract class AbstractGLRptCompAccountBalanceQueryUI extends com.kingdee.eas.fi.gl.rpt.client.GLRptAccountBalanceQueryUI
{
    private static final Logger logger = CoreUIObject.getLogger(AbstractGLRptCompAccountBalanceQueryUI.class);
    /**
     * output class constructor
     */
    public AbstractGLRptCompAccountBalanceQueryUI() throws Exception
    {
        super();
        jbInit();
        
        initUIP();
    }

    /**
     * output jbInit method
     */
    private void jbInit() throws Exception
    {
        this.resHelper = new ResourceBundleHelper(AbstractGLRptCompAccountBalanceQueryUI.class.getName());
        this.setUITitle(resHelper.getString("this.title"));
        // CustomerQueryPanel		
        this.chkOpIncludeNotPosting.setVisible(false);		
        this.kDLabelContainer2.setVisible(false);		
        this.kDLabelContainer3.setVisible(false);		
        this.chkOpAmountZero.setVisible(false);		
        this.chkOpBalanceZero.setVisible(false);		
        this.chkAmountAndBalZero.setVisible(false);		
        this.chkShowQty.setVisible(false);		
        this.chkNotIncluePLVoucher.setVisible(false);		
        this.tableForm.setVisible(false);		
        this.includeVoucher.setVisible(false);		
        this.exclusionRange.setVisible(false);		
        this.bpCompany.setRequired(true);		
        this.bpAccount.setRequired(true);		
        this.bpAccount.setVisible(false);		
        this.bpPeriod.setRequired(true);		
        this.bpPeriod.setVisible(false);		
        this.chkOpYearAmountZero.setVisible(false);		
        this.chkDisplayAsstDetail.setVisible(false);		
        this.chkYearAmountAndBalZero.setVisible(false);		
        this.chkisDCDispatchAsst.setVisible(false);
		//Register control's property binding
		registerBindings();
		registerUIState();


    }

	public com.kingdee.bos.ctrl.swing.KDToolBar[] getUIMultiToolBar(){
		java.util.List list = new java.util.ArrayList();
		com.kingdee.bos.ctrl.swing.KDToolBar[] bars = super.getUIMultiToolBar();
		if (bars != null) {
			list.addAll(java.util.Arrays.asList(bars));
		}
		return (com.kingdee.bos.ctrl.swing.KDToolBar[])list.toArray(new com.kingdee.bos.ctrl.swing.KDToolBar[list.size()]);
	}




    /**
     * output initUIContentLayout method
     */
    public void initUIContentLayout()
    {
        this.setBounds(new Rectangle(10, 10, 580, 300));
        this.setLayout(new KDLayout());
        this.putClientProperty("OriginalBounds", new Rectangle(10, 10, 580, 300));
        chkOpIncludeNotPosting.setBounds(new Rectangle(70, 214, 130, 19));
        this.add(chkOpIncludeNotPosting, new KDLayout.Constraints(70, 214, 130, 19, 0));
        lblNumberEnd.setBounds(new Rectangle(396, 35, 17, 19));
        this.add(lblNumberEnd, new KDLayout.Constraints(396, 35, 17, 19, 0));
        lblPeriodBegin.setBounds(new Rectangle(10, 35, 150, 19));
        this.add(lblPeriodBegin, new KDLayout.Constraints(10, 35, 150, 19, 0));
        lblPeriodEnd.setBounds(new Rectangle(242, 36, 90, 19));
        this.add(lblPeriodEnd, new KDLayout.Constraints(242, 36, 90, 19, 0));
        lblCurrency.setBounds(new Rectangle(10, 114, 260, 19));
        this.add(lblCurrency, new KDLayout.Constraints(10, 114, 260, 19, 0));
        lblYearBegin.setBounds(new Rectangle(163, 35, 60, 19));
        this.add(lblYearBegin, new KDLayout.Constraints(163, 35, 60, 19, 0));
        lblYearEnd.setBounds(new Rectangle(334, 35, 60, 19));
        this.add(lblYearEnd, new KDLayout.Constraints(334, 35, 60, 19, 0));
        kDLabelContainer1.setBounds(new Rectangle(10, 10, 260, 19));
        this.add(kDLabelContainer1, new KDLayout.Constraints(10, 10, 260, 19, 0));
        kDLabelContainer2.setBounds(new Rectangle(303, 145, 260, 19));
        this.add(kDLabelContainer2, new KDLayout.Constraints(303, 145, 260, 19, 0));
        kDLabelContainer3.setBounds(new Rectangle(300, 119, 260, 19));
        this.add(kDLabelContainer3, new KDLayout.Constraints(300, 119, 260, 19, 0));
        lblCompanyLevel.setBounds(new Rectangle(401, 10, 70, 19));
        this.add(lblCompanyLevel, new KDLayout.Constraints(401, 10, 70, 19, 0));
        lblNumberBegin.setBounds(new Rectangle(225, 36, 17, 19));
        this.add(lblNumberBegin, new KDLayout.Constraints(225, 36, 17, 19, 0));
        chkOpAmountZero.setBounds(new Rectangle(70, 236, 130, 19));
        this.add(chkOpAmountZero, new KDLayout.Constraints(70, 236, 130, 19, 0));
        chkOpBalanceZero.setBounds(new Rectangle(70, 258, 130, 19));
        this.add(chkOpBalanceZero, new KDLayout.Constraints(70, 258, 130, 19, 0));
        chkAmountAndBalZero.setBounds(new Rectangle(288, 236, 172, 19));
        this.add(chkAmountAndBalZero, new KDLayout.Constraints(288, 236, 172, 19, 0));
        chkShowQty.setBounds(new Rectangle(70, 170, 130, 19));
        this.add(chkShowQty, new KDLayout.Constraints(70, 170, 130, 19, 0));
        chkNotIncluePLVoucher.setBounds(new Rectangle(288, 214, 151, 19));
        this.add(chkNotIncluePLVoucher, new KDLayout.Constraints(288, 214, 151, 19, 0));
        lblDisplayMode.setBounds(new Rectangle(279, 10, 116, 19));
        this.add(lblDisplayMode, new KDLayout.Constraints(279, 10, 116, 19, 0));
        tableForm.setBounds(new Rectangle(10, 170, 58, 19));
        this.add(tableForm, new KDLayout.Constraints(10, 170, 58, 19, 0));
        includeVoucher.setBounds(new Rectangle(10, 214, 62, 19));
        this.add(includeVoucher, new KDLayout.Constraints(10, 214, 62, 19, 0));
        exclusionRange.setBounds(new Rectangle(10, 236, 60, 19));
        this.add(exclusionRange, new KDLayout.Constraints(10, 236, 60, 19, 0));
        lblAccountBegin.setBounds(new Rectangle(10, 62, 214, 19));
        this.add(lblAccountBegin, new KDLayout.Constraints(10, 62, 214, 19, 0));
        lblAccountEnd.setBounds(new Rectangle(229, 62, 166, 19));
        this.add(lblAccountEnd, new KDLayout.Constraints(229, 62, 166, 19, 0));
        lblAccountLevel.setBounds(new Rectangle(10, 89, 150, 19));
        this.add(lblAccountLevel, new KDLayout.Constraints(10, 89, 150, 19, 0));
        chkShowLeafAccount.setBounds(new Rectangle(170, 89, 120, 19));
        this.add(chkShowLeafAccount, new KDLayout.Constraints(170, 89, 120, 19, 0));
        btnAccountDiscrete.setBounds(new Rectangle(401, 62, 22, 19));
        this.add(btnAccountDiscrete, new KDLayout.Constraints(401, 62, 22, 19, 0));
        chkOpYearAmountZero.setBounds(new Rectangle(288, 258, 200, 19));
        this.add(chkOpYearAmountZero, new KDLayout.Constraints(288, 258, 200, 19, 0));
        chkDisplayAsstDetail.setBounds(new Rectangle(288, 170, 131, 19));
        this.add(chkDisplayAsstDetail, new KDLayout.Constraints(288, 170, 131, 19, 0));
        chkIncludeBW.setBounds(new Rectangle(288, 89, 140, 19));
        this.add(chkIncludeBW, new KDLayout.Constraints(288, 89, 140, 19, 0));
        chkYearAmountAndBalZero.setBounds(new Rectangle(70, 280, 200, 19));
        this.add(chkYearAmountAndBalZero, new KDLayout.Constraints(70, 280, 200, 19, 0));
        chkisDCDispatchAsst.setBounds(new Rectangle(70, 192, 200, 19));
        this.add(chkisDCDispatchAsst, new KDLayout.Constraints(70, 192, 200, 19, 0));
        displayLeafCompany.setBounds(new Rectangle(401, 10, 140, 19));
        this.add(displayLeafCompany, new KDLayout.Constraints(401, 10, 140, 19, 0));
        //lblPeriodBegin
        lblPeriodBegin.setBoundEditor(spnPeriodYearBegin);
        //lblPeriodEnd
        lblPeriodEnd.setBoundEditor(spnPeriodYearEnd);
        //lblCurrency
        lblCurrency.setBoundEditor(cmbCurrency);
        //lblYearBegin
        lblYearBegin.setBoundEditor(spnPeriodNumberBegin);
        //lblYearEnd
        lblYearEnd.setBoundEditor(spnPeriodNumberEnd);
        //kDLabelContainer1
        kDLabelContainer1.setBoundEditor(bpCompany);
        //kDLabelContainer2
        kDLabelContainer2.setBoundEditor(bpAccount);
        //kDLabelContainer3
        kDLabelContainer3.setBoundEditor(bpPeriod);
        //lblCompanyLevel
        lblCompanyLevel.setBoundEditor(spnCompanyLevel);
        //lblDisplayMode
        lblDisplayMode.setBoundEditor(cbDisplayMode);
        //lblAccountBegin
        lblAccountBegin.setBoundEditor(prbAccountBegin);
        //lblAccountEnd
        lblAccountEnd.setBoundEditor(prbAccountEnd);
        //lblAccountLevel
        lblAccountLevel.setBoundEditor(spnAccountLevel);

    }


    /**
     * output initUIMenuBarLayout method
     */
    public void initUIMenuBarLayout()
    {

    }

    /**
     * output initUIToolBarLayout method
     */
    public void initUIToolBarLayout()
    {


    }

	//Regiester control's property binding.
	private void registerBindings(){		
	}
	//Regiester UI State
	private void registerUIState(){		
	}
	public String getUIHandlerClassName() {
	    return "com.kingdee.eas.custom.app.GLRptCompAccountBalanceQueryUIHandler";
	}
	public IUIActionPostman prepareInit() {
		IUIActionPostman clientHanlder = super.prepareInit();
		if (clientHanlder != null) {
			RequestContext request = new RequestContext();
    		request.setClassName(getUIHandlerClassName());
			clientHanlder.setRequestContext(request);
		}
		return clientHanlder;
    }
	
	public boolean isPrepareInit() {
    	return false;
    }
    protected void initUIP() {
        super.initUIP();
    }



	
	

    /**
     * output setDataObject method
     */
    public void setDataObject(IObjectValue dataObject)
    {
        IObjectValue ov = dataObject;        	    	
        super.setDataObject(ov);
    }

    /**
     * output loadFields method
     */
    public void loadFields()
    {
        dataBinder.loadFields();
    }
    /**
     * output storeFields method
     */
    public void storeFields()
    {
		dataBinder.storeFields();
    }

	/**
	 * ????????§µ??
	 */
	protected void registerValidator() {
    	getValidateHelper().setCustomValidator( getValidator() );		
	}



    /**
     * output setOprtState method
     */
    public void setOprtState(String oprtType)
    {
        super.setOprtState(oprtType);
    }


    /**
     * output getMetaDataPK method
     */
    public IMetaDataPK getMetaDataPK()
    {
        return new MetaDataPK("com.kingdee.eas.custom.client", "GLRptCompAccountBalanceQueryUI");
    }




}