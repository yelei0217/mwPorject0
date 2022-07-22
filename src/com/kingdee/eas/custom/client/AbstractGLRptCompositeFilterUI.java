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
public abstract class AbstractGLRptCompositeFilterUI extends com.kingdee.eas.framework.report.client.CommRptBaseConditionUI
{
    private static final Logger logger = CoreUIObject.getLogger(AbstractGLRptCompositeFilterUI.class);
    protected com.kingdee.bos.ctrl.swing.KDLabelContainer kDLabelContainer1;
    protected com.kingdee.bos.ctrl.swing.KDLabelContainer lblYearBegin;
    protected com.kingdee.bos.ctrl.swing.KDLabelContainer lblPeriodBegin;
    protected com.kingdee.bos.ctrl.swing.KDLabelContainer lblCurrency;
    protected com.kingdee.bos.ctrl.swing.KDCheckBox chkOpIncludeNotPosting;
    protected com.kingdee.bos.ctrl.swing.KDLabel kDLabel1;
    protected com.kingdee.bos.ctrl.swing.KDLabelContainer kDLabelContainer3;
    protected com.kingdee.bos.ctrl.swing.KDCheckBox chkShowLeafAccount;
    protected com.kingdee.bos.ctrl.swing.KDLabelContainer kDLabelContainer4;
    protected com.kingdee.bos.ctrl.swing.KDLabelContainer kDLabelContainer5;
    protected com.kingdee.bos.ctrl.swing.KDLabel kDLabel2;
    protected com.kingdee.bos.ctrl.swing.KDLabelContainer kDLabelContainer6;
    protected com.kingdee.bos.ctrl.swing.KDLabelContainer kDLabelContainer7;
    protected com.kingdee.bos.ctrl.swing.KDLabelContainer lblDisplayMode;
    protected com.kingdee.bos.ctrl.swing.KDLabelContainer lblCompanyLevel;
    protected com.kingdee.bos.ctrl.extendcontrols.KDBizPromptBox bpCompany;
    protected com.kingdee.bos.ctrl.swing.KDSpinner spnPeriodNumberBegin;
    protected com.kingdee.bos.ctrl.swing.KDSpinner spnPeriodYearBegin;
    protected com.kingdee.bos.ctrl.swing.KDComboBox cmbCurrency;
    protected com.kingdee.bos.ctrl.swing.KDSpinner spnAccountLevel;
    protected com.kingdee.bos.ctrl.swing.KDSpinner spnPeriodYearEnd;
    protected com.kingdee.bos.ctrl.swing.KDSpinner spnPeriodNumberEnd;
    protected com.kingdee.bos.ctrl.swing.KDComboBox bpPeriod;
    protected com.kingdee.bos.ctrl.swing.KDComboBox bpAccount;
    protected com.kingdee.bos.ctrl.swing.KDComboBox cbDisplayMode;
    protected com.kingdee.bos.ctrl.swing.KDSpinner spnCompanyLevel;
    /**
     * output class constructor
     */
    public AbstractGLRptCompositeFilterUI() throws Exception
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
        this.resHelper = new ResourceBundleHelper(AbstractGLRptCompositeFilterUI.class.getName());
        this.setUITitle(resHelper.getString("this.title"));
        this.kDLabelContainer1 = new com.kingdee.bos.ctrl.swing.KDLabelContainer();
        this.lblYearBegin = new com.kingdee.bos.ctrl.swing.KDLabelContainer();
        this.lblPeriodBegin = new com.kingdee.bos.ctrl.swing.KDLabelContainer();
        this.lblCurrency = new com.kingdee.bos.ctrl.swing.KDLabelContainer();
        this.chkOpIncludeNotPosting = new com.kingdee.bos.ctrl.swing.KDCheckBox();
        this.kDLabel1 = new com.kingdee.bos.ctrl.swing.KDLabel();
        this.kDLabelContainer3 = new com.kingdee.bos.ctrl.swing.KDLabelContainer();
        this.chkShowLeafAccount = new com.kingdee.bos.ctrl.swing.KDCheckBox();
        this.kDLabelContainer4 = new com.kingdee.bos.ctrl.swing.KDLabelContainer();
        this.kDLabelContainer5 = new com.kingdee.bos.ctrl.swing.KDLabelContainer();
        this.kDLabel2 = new com.kingdee.bos.ctrl.swing.KDLabel();
        this.kDLabelContainer6 = new com.kingdee.bos.ctrl.swing.KDLabelContainer();
        this.kDLabelContainer7 = new com.kingdee.bos.ctrl.swing.KDLabelContainer();
        this.lblDisplayMode = new com.kingdee.bos.ctrl.swing.KDLabelContainer();
        this.lblCompanyLevel = new com.kingdee.bos.ctrl.swing.KDLabelContainer();
        this.bpCompany = new com.kingdee.bos.ctrl.extendcontrols.KDBizPromptBox();
        this.spnPeriodNumberBegin = new com.kingdee.bos.ctrl.swing.KDSpinner();
        this.spnPeriodYearBegin = new com.kingdee.bos.ctrl.swing.KDSpinner();
        this.cmbCurrency = new com.kingdee.bos.ctrl.swing.KDComboBox();
        this.spnAccountLevel = new com.kingdee.bos.ctrl.swing.KDSpinner();
        this.spnPeriodYearEnd = new com.kingdee.bos.ctrl.swing.KDSpinner();
        this.spnPeriodNumberEnd = new com.kingdee.bos.ctrl.swing.KDSpinner();
        this.bpPeriod = new com.kingdee.bos.ctrl.swing.KDComboBox();
        this.bpAccount = new com.kingdee.bos.ctrl.swing.KDComboBox();
        this.cbDisplayMode = new com.kingdee.bos.ctrl.swing.KDComboBox();
        this.spnCompanyLevel = new com.kingdee.bos.ctrl.swing.KDSpinner();
        this.kDLabelContainer1.setName("kDLabelContainer1");
        this.lblYearBegin.setName("lblYearBegin");
        this.lblPeriodBegin.setName("lblPeriodBegin");
        this.lblCurrency.setName("lblCurrency");
        this.chkOpIncludeNotPosting.setName("chkOpIncludeNotPosting");
        this.kDLabel1.setName("kDLabel1");
        this.kDLabelContainer3.setName("kDLabelContainer3");
        this.chkShowLeafAccount.setName("chkShowLeafAccount");
        this.kDLabelContainer4.setName("kDLabelContainer4");
        this.kDLabelContainer5.setName("kDLabelContainer5");
        this.kDLabel2.setName("kDLabel2");
        this.kDLabelContainer6.setName("kDLabelContainer6");
        this.kDLabelContainer7.setName("kDLabelContainer7");
        this.lblDisplayMode.setName("lblDisplayMode");
        this.lblCompanyLevel.setName("lblCompanyLevel");
        this.bpCompany.setName("bpCompany");
        this.spnPeriodNumberBegin.setName("spnPeriodNumberBegin");
        this.spnPeriodYearBegin.setName("spnPeriodYearBegin");
        this.cmbCurrency.setName("cmbCurrency");
        this.spnAccountLevel.setName("spnAccountLevel");
        this.spnPeriodYearEnd.setName("spnPeriodYearEnd");
        this.spnPeriodNumberEnd.setName("spnPeriodNumberEnd");
        this.bpPeriod.setName("bpPeriod");
        this.bpAccount.setName("bpAccount");
        this.cbDisplayMode.setName("cbDisplayMode");
        this.spnCompanyLevel.setName("spnCompanyLevel");
        // CustomerQueryPanel
        // kDLabelContainer1		
        this.kDLabelContainer1.setBoundLabelText(resHelper.getString("kDLabelContainer1.boundLabelText"));		
        this.kDLabelContainer1.setBoundLabelLength(80);		
        this.kDLabelContainer1.setBoundLabelUnderline(true);
        // lblYearBegin		
        this.lblYearBegin.setBoundLabelText(resHelper.getString("lblYearBegin.boundLabelText"));
        // lblPeriodBegin		
        this.lblPeriodBegin.setBoundLabelText(resHelper.getString("lblPeriodBegin.boundLabelText"));		
        this.lblPeriodBegin.setBoundLabelLength(80);		
        this.lblPeriodBegin.setBoundLabelUnderline(true);
        // lblCurrency		
        this.lblCurrency.setBoundLabelText(resHelper.getString("lblCurrency.boundLabelText"));		
        this.lblCurrency.setBoundLabelLength(100);		
        this.lblCurrency.setBoundLabelUnderline(true);
        // chkOpIncludeNotPosting		
        this.chkOpIncludeNotPosting.setText(resHelper.getString("chkOpIncludeNotPosting.text"));
        // kDLabel1		
        this.kDLabel1.setText(resHelper.getString("kDLabel1.text"));
        // kDLabelContainer3		
        this.kDLabelContainer3.setBoundLabelText(resHelper.getString("kDLabelContainer3.boundLabelText"));		
        this.kDLabelContainer3.setBoundLabelLength(80);		
        this.kDLabelContainer3.setBoundLabelUnderline(true);
        // chkShowLeafAccount		
        this.chkShowLeafAccount.setText(resHelper.getString("chkShowLeafAccount.text"));
        // kDLabelContainer4		
        this.kDLabelContainer4.setBoundLabelText(resHelper.getString("kDLabelContainer4.boundLabelText"));		
        this.kDLabelContainer4.setBoundLabelLength(20);		
        this.kDLabelContainer4.setVisible(false);
        // kDLabelContainer5		
        this.kDLabelContainer5.setBoundLabelText(resHelper.getString("kDLabelContainer5.boundLabelText"));		
        this.kDLabelContainer5.setVisible(false);
        // kDLabel2		
        this.kDLabel2.setText(resHelper.getString("kDLabel2.text"));		
        this.kDLabel2.setVisible(false);
        // kDLabelContainer6		
        this.kDLabelContainer6.setBoundLabelText(resHelper.getString("kDLabelContainer6.boundLabelText"));		
        this.kDLabelContainer6.setBoundLabelLength(80);		
        this.kDLabelContainer6.setBoundLabelUnderline(true);
        // kDLabelContainer7		
        this.kDLabelContainer7.setBoundLabelText(resHelper.getString("kDLabelContainer7.boundLabelText"));		
        this.kDLabelContainer7.setBoundLabelLength(80);		
        this.kDLabelContainer7.setBoundLabelUnderline(true);
        // lblDisplayMode		
        this.lblDisplayMode.setBoundLabelText(resHelper.getString("lblDisplayMode.boundLabelText"));		
        this.lblDisplayMode.setVisible(false);
        // lblCompanyLevel		
        this.lblCompanyLevel.setBoundLabelText(resHelper.getString("lblCompanyLevel.boundLabelText"));		
        this.lblCompanyLevel.setVisible(false);
        // bpCompany		
        this.bpCompany.setRequired(true);		
        this.bpCompany.setDisplayFormat("$name$");		
        this.bpCompany.setEditFormat("$number$");		
        this.bpCompany.setCommitFormat("$number$");		
        this.bpCompany.setQueryInfo("com.kingdee.eas.fi.gl.app.CompanyOrgUnitQuery");
        // spnPeriodNumberBegin
        // spnPeriodYearBegin
        // cmbCurrency
        // spnAccountLevel
        // spnPeriodYearEnd		
        this.spnPeriodYearEnd.setVisible(false);
        // spnPeriodNumberEnd		
        this.spnPeriodNumberEnd.setVisible(false);
        // bpPeriod		
        this.bpPeriod.setRequired(true);
        // bpAccount		
        this.bpAccount.setRequired(true);
        // cbDisplayMode		
        this.cbDisplayMode.setVisible(false);
        // spnCompanyLevel		
        this.spnCompanyLevel.setVisible(false);
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
        this.setBounds(new Rectangle(10, 10, 320, 250));
        this.setLayout(null);
        kDLabelContainer1.setBounds(new Rectangle(21, 12, 260, 19));
        this.add(kDLabelContainer1, null);
        lblYearBegin.setBounds(new Rectangle(198, 87, 73, 19));
        this.add(lblYearBegin, null);
        lblPeriodBegin.setBounds(new Rectangle(21, 87, 175, 19));
        this.add(lblPeriodBegin, null);
        lblCurrency.setBounds(new Rectangle(21, 146, 273, 19));
        this.add(lblCurrency, null);
        chkOpIncludeNotPosting.setBounds(new Rectangle(21, 173, 156, 19));
        this.add(chkOpIncludeNotPosting, null);
        kDLabel1.setBounds(new Rectangle(277, 85, 17, 19));
        this.add(kDLabel1, null);
        kDLabelContainer3.setBounds(new Rectangle(21, 120, 150, 19));
        this.add(kDLabelContainer3, null);
        chkShowLeafAccount.setBounds(new Rectangle(175, 120, 120, 19));
        this.add(chkShowLeafAccount, null);
        kDLabelContainer4.setBounds(new Rectangle(294, 85, 90, 19));
        this.add(kDLabelContainer4, null);
        kDLabelContainer5.setBounds(new Rectangle(386, 85, 60, 19));
        this.add(kDLabelContainer5, null);
        kDLabel2.setBounds(new Rectangle(455, 88, 17, 19));
        this.add(kDLabel2, null);
        kDLabelContainer6.setBounds(new Rectangle(21, 61, 260, 19));
        this.add(kDLabelContainer6, null);
        kDLabelContainer7.setBounds(new Rectangle(21, 36, 260, 19));
        this.add(kDLabelContainer7, null);
        lblDisplayMode.setBounds(new Rectangle(286, 12, 116, 19));
        this.add(lblDisplayMode, null);
        lblCompanyLevel.setBounds(new Rectangle(408, 12, 70, 19));
        this.add(lblCompanyLevel, null);
        //kDLabelContainer1
        kDLabelContainer1.setBoundEditor(bpCompany);
        //lblYearBegin
        lblYearBegin.setBoundEditor(spnPeriodNumberBegin);
        //lblPeriodBegin
        lblPeriodBegin.setBoundEditor(spnPeriodYearBegin);
        //lblCurrency
        lblCurrency.setBoundEditor(cmbCurrency);
        //kDLabelContainer3
        kDLabelContainer3.setBoundEditor(spnAccountLevel);
        //kDLabelContainer4
        kDLabelContainer4.setBoundEditor(spnPeriodYearEnd);
        //kDLabelContainer5
        kDLabelContainer5.setBoundEditor(spnPeriodNumberEnd);
        //kDLabelContainer6
        kDLabelContainer6.setBoundEditor(bpPeriod);
        //kDLabelContainer7
        kDLabelContainer7.setBoundEditor(bpAccount);
        //lblDisplayMode
        lblDisplayMode.setBoundEditor(cbDisplayMode);
        //lblCompanyLevel
        lblCompanyLevel.setBoundEditor(spnCompanyLevel);

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
	    return "com.kingdee.eas.custom.app.GLRptCompositeFilterUIHandler";
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
     * output chkOpIncludeNotPosting_actionPerformed method
     */
    protected void chkOpIncludeNotPosting_actionPerformed(java.awt.event.ActionEvent e) throws Exception
    {
    }


    /**
     * output getMetaDataPK method
     */
    public IMetaDataPK getMetaDataPK()
    {
        return new MetaDataPK("com.kingdee.eas.custom.client", "GLRptCompositeFilterUI");
    }




}