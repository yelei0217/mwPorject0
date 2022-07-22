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
public abstract class AbstractSupplyCityFilterUI extends com.kingdee.eas.framework.report.client.CommRptBaseConditionUI
{
    private static final Logger logger = CoreUIObject.getLogger(AbstractSupplyCityFilterUI.class);
    protected com.kingdee.bos.ctrl.swing.KDLabelContainer kDLabelContainer1;
    protected com.kingdee.bos.ctrl.swing.KDLabelContainer kDLabelContainer2;
    protected com.kingdee.bos.ctrl.swing.KDLabelContainer kDLabelContainer3;
    protected com.kingdee.bos.ctrl.swing.KDLabelContainer kDLabelContainer4;
    protected com.kingdee.bos.ctrl.swing.KDLabelContainer kDLabelContainer6;
    protected com.kingdee.bos.ctrl.swing.KDLabelContainer kDLabelContainer8;
    protected com.kingdee.bos.ctrl.extendcontrols.KDBizPromptBox prmtMaterialNumFrom;
    protected com.kingdee.bos.ctrl.extendcontrols.KDBizPromptBox prmtMaterialNumTo;
    protected com.kingdee.bos.ctrl.extendcontrols.KDBizPromptBox prmtSupplierNumFrom;
    protected com.kingdee.bos.ctrl.extendcontrols.KDBizPromptBox prmtSupplierNumTo;
    protected com.kingdee.bos.ctrl.extendcontrols.KDBizPromptBox kDBizPromptBox2;
    protected com.kingdee.bos.ctrl.extendcontrols.KDBizPromptBox kDBizPromptBox4;
    /**
     * output class constructor
     */
    public AbstractSupplyCityFilterUI() throws Exception
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
        this.resHelper = new ResourceBundleHelper(AbstractSupplyCityFilterUI.class.getName());
        this.setUITitle(resHelper.getString("this.title"));
        this.kDLabelContainer1 = new com.kingdee.bos.ctrl.swing.KDLabelContainer();
        this.kDLabelContainer2 = new com.kingdee.bos.ctrl.swing.KDLabelContainer();
        this.kDLabelContainer3 = new com.kingdee.bos.ctrl.swing.KDLabelContainer();
        this.kDLabelContainer4 = new com.kingdee.bos.ctrl.swing.KDLabelContainer();
        this.kDLabelContainer6 = new com.kingdee.bos.ctrl.swing.KDLabelContainer();
        this.kDLabelContainer8 = new com.kingdee.bos.ctrl.swing.KDLabelContainer();
        this.prmtMaterialNumFrom = new com.kingdee.bos.ctrl.extendcontrols.KDBizPromptBox();
        this.prmtMaterialNumTo = new com.kingdee.bos.ctrl.extendcontrols.KDBizPromptBox();
        this.prmtSupplierNumFrom = new com.kingdee.bos.ctrl.extendcontrols.KDBizPromptBox();
        this.prmtSupplierNumTo = new com.kingdee.bos.ctrl.extendcontrols.KDBizPromptBox();
        this.kDBizPromptBox2 = new com.kingdee.bos.ctrl.extendcontrols.KDBizPromptBox();
        this.kDBizPromptBox4 = new com.kingdee.bos.ctrl.extendcontrols.KDBizPromptBox();
        this.kDLabelContainer1.setName("kDLabelContainer1");
        this.kDLabelContainer2.setName("kDLabelContainer2");
        this.kDLabelContainer3.setName("kDLabelContainer3");
        this.kDLabelContainer4.setName("kDLabelContainer4");
        this.kDLabelContainer6.setName("kDLabelContainer6");
        this.kDLabelContainer8.setName("kDLabelContainer8");
        this.prmtMaterialNumFrom.setName("prmtMaterialNumFrom");
        this.prmtMaterialNumTo.setName("prmtMaterialNumTo");
        this.prmtSupplierNumFrom.setName("prmtSupplierNumFrom");
        this.prmtSupplierNumTo.setName("prmtSupplierNumTo");
        this.kDBizPromptBox2.setName("kDBizPromptBox2");
        this.kDBizPromptBox4.setName("kDBizPromptBox4");
        // CustomerQueryPanel
        // kDLabelContainer1		
        this.kDLabelContainer1.setBoundLabelText(resHelper.getString("kDLabelContainer1.boundLabelText"));		
        this.kDLabelContainer1.setBoundLabelLength(80);		
        this.kDLabelContainer1.setBoundLabelUnderline(true);
        // kDLabelContainer2		
        this.kDLabelContainer2.setBoundLabelLength(80);		
        this.kDLabelContainer2.setBoundLabelUnderline(true);		
        this.kDLabelContainer2.setBoundLabelText(resHelper.getString("kDLabelContainer2.boundLabelText"));
        // kDLabelContainer3		
        this.kDLabelContainer3.setBoundLabelText(resHelper.getString("kDLabelContainer3.boundLabelText"));		
        this.kDLabelContainer3.setBoundLabelLength(80);		
        this.kDLabelContainer3.setBoundLabelUnderline(true);
        // kDLabelContainer4		
        this.kDLabelContainer4.setBoundLabelLength(80);		
        this.kDLabelContainer4.setBoundLabelUnderline(true);		
        this.kDLabelContainer4.setBoundLabelText(resHelper.getString("kDLabelContainer4.boundLabelText"));
        // kDLabelContainer6		
        this.kDLabelContainer6.setBoundLabelText(resHelper.getString("kDLabelContainer6.boundLabelText"));		
        this.kDLabelContainer6.setBoundLabelLength(58);		
        this.kDLabelContainer6.setToolTipText(resHelper.getString("kDLabelContainer6.toolTipText"));		
        this.kDLabelContainer6.setBoundLabelUnderline(true);
        // kDLabelContainer8		
        this.kDLabelContainer8.setBoundLabelText(resHelper.getString("kDLabelContainer8.boundLabelText"));		
        this.kDLabelContainer8.setBoundLabelLength(58);		
        this.kDLabelContainer8.setBoundLabelUnderline(true);		
        this.kDLabelContainer8.setToolTipText(resHelper.getString("kDLabelContainer8.toolTipText"));
        // prmtMaterialNumFrom		
        this.prmtMaterialNumFrom.setRequired(true);		
        this.prmtMaterialNumFrom.setDisplayFormat("$name$");		
        this.prmtMaterialNumFrom.setEditFormat("$number$");		
        this.prmtMaterialNumFrom.setCommitFormat("$number$");		
        this.prmtMaterialNumFrom.setQueryInfo("com.kingdee.eas.basedata.master.material.app.F7MaterialQuery");
        this.prmtMaterialNumFrom.addDataChangeListener(new com.kingdee.bos.ctrl.swing.event.DataChangeListener() {
            public void dataChanged(com.kingdee.bos.ctrl.swing.event.DataChangeEvent e) {
                try {
                    prmtMaterialNumFrom_dataChanged(e);
                } catch (Exception exc) {
                    handUIException(exc);
                } finally {
                }
            }
        });
        // prmtMaterialNumTo		
        this.prmtMaterialNumTo.setRequired(true);		
        this.prmtMaterialNumTo.setDisplayFormat("$name$");		
        this.prmtMaterialNumTo.setEditFormat("$number$");		
        this.prmtMaterialNumTo.setCommitFormat("$number$");		
        this.prmtMaterialNumTo.setQueryInfo("com.kingdee.eas.basedata.master.material.app.F7MaterialQuery");
        // prmtSupplierNumFrom		
        this.prmtSupplierNumFrom.setRequired(true);		
        this.prmtSupplierNumFrom.setDisplayFormat("$name$");		
        this.prmtSupplierNumFrom.setEditFormat("$number$");		
        this.prmtSupplierNumFrom.setCommitFormat("$number$");		
        this.prmtSupplierNumFrom.setQueryInfo("com.kingdee.eas.basedata.master.cssp.app.F7SupplierQuery");
        this.prmtSupplierNumFrom.addDataChangeListener(new com.kingdee.bos.ctrl.swing.event.DataChangeListener() {
            public void dataChanged(com.kingdee.bos.ctrl.swing.event.DataChangeEvent e) {
                try {
                    prmtSupplierNumFrom_dataChanged(e);
                } catch (Exception exc) {
                    handUIException(exc);
                } finally {
                }
            }
        });
        // prmtSupplierNumTo		
        this.prmtSupplierNumTo.setRequired(true);		
        this.prmtSupplierNumTo.setDisplayFormat("$name$");		
        this.prmtSupplierNumTo.setEditFormat("$number$");		
        this.prmtSupplierNumTo.setCommitFormat("$number$");		
        this.prmtSupplierNumTo.setQueryInfo("com.kingdee.eas.basedata.master.cssp.app.F7SupplierQuery");
        // kDBizPromptBox2		
        this.kDBizPromptBox2.setDisplayFormat("$number$");		
        this.kDBizPromptBox2.setEditFormat("$number$");		
        this.kDBizPromptBox2.setQueryInfo("com.kingdee.eas.basedata.master.cssp.app.F7SupplierQuery");		
        this.kDBizPromptBox2.setEditable(true);
        // kDBizPromptBox4		
        this.kDBizPromptBox4.setQueryInfo("com.kingdee.eas.basedata.master.material.app.F7MaterialQuery");		
        this.kDBizPromptBox4.setDisplayFormat("$number$");		
        this.kDBizPromptBox4.setEditFormat("$number$");		
        this.kDBizPromptBox4.setEditable(true);
        this.kDBizPromptBox4.addDataChangeListener(new com.kingdee.bos.ctrl.swing.event.DataChangeListener() {
            public void dataChanged(com.kingdee.bos.ctrl.swing.event.DataChangeEvent e) {
                try {
                    prmtMaterialNumTo_dataChanged(e);
                } catch (Exception exc) {
                    handUIException(exc);
                } finally {
                }
            }
        });
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
        this.setBounds(new Rectangle(10, 10, 500, 120));
        this.setLayout(null);
        kDLabelContainer1.setBounds(new Rectangle(38, 24, 207, 19));
        this.add(kDLabelContainer1, null);
        kDLabelContainer2.setBounds(new Rectangle(267, 24, 207, 19));
        this.add(kDLabelContainer2, null);
        kDLabelContainer3.setBounds(new Rectangle(38, 56, 207, 19));
        this.add(kDLabelContainer3, null);
        kDLabelContainer4.setBounds(new Rectangle(265, 56, 207, 19));
        this.add(kDLabelContainer4, null);
        kDLabelContainer6.setBounds(new Rectangle(672, 86, 208, 19));
        this.add(kDLabelContainer6, null);
        kDLabelContainer8.setBounds(new Rectangle(672, 114, 208, 19));
        this.add(kDLabelContainer8, null);
        //kDLabelContainer1
        kDLabelContainer1.setBoundEditor(prmtMaterialNumFrom);
        //kDLabelContainer2
        kDLabelContainer2.setBoundEditor(prmtMaterialNumTo);
        //kDLabelContainer3
        kDLabelContainer3.setBoundEditor(prmtSupplierNumFrom);
        //kDLabelContainer4
        kDLabelContainer4.setBoundEditor(prmtSupplierNumTo);
        //kDLabelContainer6
        kDLabelContainer6.setBoundEditor(kDBizPromptBox2);
        //kDLabelContainer8
        kDLabelContainer8.setBoundEditor(kDBizPromptBox4);

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
	    return "com.kingdee.eas.custom.app.SupplyCityFilterUIHandler";
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
     * output prmtMaterialNumFrom_dataChanged method
     */
    protected void prmtMaterialNumFrom_dataChanged(com.kingdee.bos.ctrl.swing.event.DataChangeEvent e) throws Exception
    {
    }

    /**
     * output prmtSupplierNumFrom_dataChanged method
     */
    protected void prmtSupplierNumFrom_dataChanged(com.kingdee.bos.ctrl.swing.event.DataChangeEvent e) throws Exception
    {
    }

    /**
     * output prmtMaterialNumTo_dataChanged method
     */
    protected void prmtMaterialNumTo_dataChanged(com.kingdee.bos.ctrl.swing.event.DataChangeEvent e) throws Exception
    {
    }


    /**
     * output getMetaDataPK method
     */
    public IMetaDataPK getMetaDataPK()
    {
        return new MetaDataPK("com.kingdee.eas.custom.client", "SupplyCityFilterUI");
    }




}