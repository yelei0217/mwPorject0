/**
 * output package name
 */
package com.kingdee.eas.custom.client;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;

import org.apache.log4j.Logger;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.metadata.entity.FilterInfo;
import com.kingdee.bos.metadata.entity.FilterItemCollection;
import com.kingdee.bos.metadata.entity.FilterItemInfo;
import com.kingdee.bos.metadata.entity.SelectorItemInfo;
import com.kingdee.bos.metadata.query.util.CompareType;
import com.kingdee.bos.ui.face.CoreUIObject;
import com.kingdee.bos.ctrl.swing.KDComboBox;
import com.kingdee.bos.ctrl.swing.KDPromptSelector;
import com.kingdee.bos.ctrl.swing.event.DataChangeEvent;
import com.kingdee.bos.ctrl.swing.event.DataChangeListener;
import com.kingdee.bos.ctrl.swing.event.PreChangeEvent;
import com.kingdee.bos.ctrl.swing.event.PreChangeListener;
import com.kingdee.bos.dao.AbstractObjectCollection;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.eas.basedata.assistant.CurrencyInfo;
import com.kingdee.eas.basedata.assistant.PeriodTypeCollection;
import com.kingdee.eas.basedata.assistant.PeriodTypeInfo;
import com.kingdee.eas.basedata.master.account.AccountTableCollection;
import com.kingdee.eas.basedata.master.account.AccountTableInfo;
import com.kingdee.eas.basedata.org.CompanyOrgUnitCollection;
import com.kingdee.eas.basedata.org.CompanyOrgUnitFactory;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.basedata.org.client.f7.NewCompanyF7;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.common.client.SysContext;
import com.kingdee.eas.custom.GLRptCompositeFacadeFactory;
import com.kingdee.eas.fi.gl.GLGeneralFacadeFactory;
import com.kingdee.eas.fi.gl.GlUtils;
import com.kingdee.eas.fi.gl.client.CompanyTreePromptBox;
import com.kingdee.eas.fi.gl.rpt.CompanyDisplayModeEnum;
import com.kingdee.eas.fi.gl.rpt.GLRptAccountBalanceCondition;
import com.kingdee.eas.fi.gl.rpt.GLRptQueryInitData;
import com.kingdee.eas.fi.gl.rpt.IGLRptBaseFacade;
import com.kingdee.eas.fi.gl.rpt.client.GLRptBaseUI;
import com.kingdee.eas.framework.*;
import com.kingdee.eas.framework.report.util.PeriodEntity;
import com.kingdee.eas.framework.report.util.RptConditionManager;
import com.kingdee.eas.framework.report.util.RptParams;
import com.kingdee.eas.framework.report.util.SpinnerUtil;
import com.kingdee.eas.util.SysUtil;
import com.kingdee.eas.util.client.MsgBox;

/**
 * output class name
 */
public class GLRptCompositeFilterUI extends AbstractGLRptCompositeFilterUI
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -3023602550275286247L;
	private static final Logger logger = CoreUIObject.getLogger(GLRptCompositeFilterUI.class);
    
	  protected CompanyOrgUnitInfo company = null;
	  protected GLRptQueryInitData queryInitData;
	  protected PeriodEntity pe;
	  protected boolean show = false;
	  protected CompanyOrgUnitInfo[] companys;
	  
	  
    /**
     * output class constructor
     */
    public GLRptCompositeFilterUI() throws Exception
    {
        super();
    }

    /**
     * output storeFields method
     */
    public void storeFields()
    {
        super.storeFields();
    }
    @Override
    public void onLoad() throws Exception {
    	// TODO Auto-generated method stub
        initData();
        this.bpCompany.setEnabledMultiSelection(isMultiSelectCompany());
        super.onLoad();
        initCompanyF7();
        initPeriodComboBox();
        initAccountTableComboBox();
        initCurrencyComboBox();
        companyChanged();
        //accountTableChanged();
        
        if(company.getLevel() == 3){
	        this.bpCompany.setValue(company);
	        Set idSet = new HashSet();
	        idSet.add(company.getId().toString());
	        Map pdRang = GLRptCompositeFacadeFactory.getRemoteInstance().getPeriodRange(idSet,"B+Rlys8oRE6J8BgYd00FIV9piaY=".toString());
	        PeriodEntity pe = new PeriodEntity(pdRang);
	        SpinnerUtil.managePeriodRange(pe, this.spnPeriodYearBegin, this.spnPeriodNumberBegin, this.spnPeriodYearEnd, this.spnPeriodNumberEnd, false, false);    
	        this.spnPeriodYearBegin.setEnabled(true);
	        this.spnPeriodNumberBegin.setEnabled(true);
        }
        
//        this.bpAccount.setSelectedIndex(0);
//        this.cmbCurrency.setSelectedIndex(0);
        
        
    }
    
    protected void initUIP() {
        super.initUIP();
    }
    
 

    /**
     * output chkOpIncludeNotPosting_actionPerformed method
     */
    protected void chkOpIncludeNotPosting_actionPerformed(java.awt.event.ActionEvent e) throws Exception
    {
        super.chkOpIncludeNotPosting_actionPerformed(e);
    }

	@Override
	public RptParams getCustomCondition() {
        RptParams param = new RptParams();
        RptConditionManager rcm = new RptConditionManager();
        CurrencyInfo currency = (CurrencyInfo)this.cmbCurrency.getSelectedItem();
        if (currency != null)
        {
          if (currency.getId() != null)
          {
            rcm.setProperty("currencyID",currency.getId().toString());
            rcm.setProperty("currency",currency);
          }
          else
          {
              rcm.setProperty("currencyID",currency.getNumber());
          }
          rcm.setProperty("currencyName",currency.getName());
        }
        rcm.setProperty("optionOnlyLeaf", this.chkShowLeafAccount.isSelected());
        rcm.setProperty("includeBWAccount", this.chkOpIncludeNotPosting.isSelected());
        rcm.setProperty("companys", getCompanys());
        rcm.setProperty("company", company);
        rcm.setProperty("companyId", company.getId().toString());
        rcm.setProperty("accountLevelStart", ((Integer)this.spnAccountLevel.getValue()).intValue());
        rcm.setProperty("periodYearStart",((Integer)this.spnPeriodYearBegin.getValue()).intValue());
        rcm.setProperty("periodNumberStart",((Integer)this.spnPeriodNumberBegin.getValue()).intValue());
        Map map = rcm.toMap();
        param.putAll(map);
		return param;
 	}

	@Override
	public void onInit(RptParams arg0) throws Exception {
		// TODO Auto-generated method stub
	}

	@Override
	public void setCustomCondition(RptParams arg0) {
		// TODO Auto-generated method stub
	  //  return new GLRptAccountBalanceCondition();
	}
	  
	 public void initData()throws Exception
	 {
	    this.queryInitData = GLRptCompositeFacadeFactory.getRemoteInstance().getQueryInitData(isSupportUnion());
	    this.company =  SysContext.getSysContext().getCurrentFIUnit();
	  //  this.cmbCurrency.setSelectedIndex(3);
	    //this.spnAccountLevel.setValue(1);
	    this.spnAccountLevel.setValue(new Integer(1));
	 }
	  
	  private void initCompanyF7()
	  {
	    this.bpCompany.setEditable(true);
	    EntityViewInfo companyView = new EntityViewInfo();
	    companyView.setSelector(GlUtils.getCompanySic());
	    FilterInfo filter = new FilterInfo();
	    filter.getFilterItems().add(new FilterItemInfo("id", getCompanyIdFilter(), CompareType.INNER));
	    companyView.setFilter(filter);
	    this.bpCompany.setEntityViewInfo(companyView);
	    this.bpCompany.setSelector(getCompanySelector());
	    setCompanyDisplayMode();
	  }
	  
	  protected void initPeriodComboBox()
	    throws Exception
	  {
	    PeriodTypeCollection periodTyes = this.queryInitData.getPeriodTypes();
	    this.bpPeriod.setModel(new DefaultComboBoxModel(toVector(periodTyes)));
	    if (!isMultiSelectCompany()) {
	      this.bpPeriod.setEnabled(false);
	    }
	  }
	  
	  protected void initAccountTableComboBox()
	    throws Exception
	  {
	    AccountTableCollection accountTables = this.queryInitData.getAccountTables();
	    this.bpAccount.setModel(new DefaultComboBoxModel(toVector(accountTables)));
	  }
	  
	  protected void initCurrencyComboBox()
	    throws Exception
	  {
	    Vector currencys = this.queryInitData.getCurrencys();
	    this.cmbCurrency.setModel(new DefaultComboBoxModel(currencys));
	  }
	  
	  
	  protected void companyChanged()
	    throws Exception
	  {
	    boolean isEnable = getCompanys() != null;
	    this.spnPeriodYearBegin.setEnabled(isEnable);
	    this.spnPeriodNumberBegin.setEnabled(isEnable);
	    this.spnPeriodYearEnd.setEnabled(isEnable);
	    this.spnPeriodNumberEnd.setEnabled(isEnable);
	    if (!isUnionQuery())
	    {
	      this.bpPeriod.setSelectedItem(null);
	      setSelectedItem(this.bpAccount, getSameAccountTableInfo());
	      CurrencyInfo currencyInfo = getSameBaseCurrencyInfo();
	      if (currencyInfo == null) {
	        this.cmbCurrency.setSelectedIndex(3);
	      } else {
	        setSelectedItem(this.cmbCurrency, currencyInfo);
	      }
	    }
	    if (isEnable)
	    {
	      setPeriodRange();
	      
	      setCompanyMaxLevel();
	    }
	    if (!isUnionQuery()) {
	      setSelectedItem(this.bpPeriod, getSamePeriodTypeInfo());
	    }
	  }
	  
	  protected void setSelectedItem(KDComboBox comboBox, DataBaseInfo dbInfo)
	  {
	    if (dbInfo == null)
	    {
	      comboBox.setSelectedItem(null);
	      return;
	    }
	    for (int i = 0; i < comboBox.getItemCount(); i++)
	    {
	      Object object = comboBox.getItemAt(i);
	      if ((object instanceof DataBaseInfo))
	      {
	        DataBaseInfo info = (DataBaseInfo)object;
	        if ((info != null) && (dbInfo.getId().equals(info.getId())))
	        {
	          comboBox.setSelectedIndex(i);
	          break;
	        }
	      }
	    }
	  }  
	  private PeriodTypeInfo getSamePeriodTypeInfo()
	  {
	    CompanyOrgUnitInfo[] companys = getCompanys();
	    if (companys == null) {
	      return null;
	    }
	    PeriodTypeInfo periodTypeInfo = companys[0].getAccountPeriodType();
	    if (periodTypeInfo == null) {
	      return null;
	    }
	    for (int i = 1; i < companys.length; i++)
	    {
	      if (companys[i].getAccountPeriodType() == null) {
	        return null;
	      }
	      if (!companys[i].getAccountPeriodType().getId().equals(periodTypeInfo.getId())) {
	        return null;
	      }
	    }
	    return periodTypeInfo;
	  }
	  private void setPeriodRange()
	  {
	    PeriodTypeInfo peridType = (PeriodTypeInfo)this.bpPeriod.getSelectedItem();
	    Set companyIdSet = getCompanyIdSet();
	    boolean isPeriodEnable = (peridType != null) && (!companyIdSet.isEmpty());
	    this.spnPeriodYearBegin.setEnabled(isPeriodEnable);
	    this.spnPeriodYearEnd.setEnabled(isPeriodEnable);
	    this.spnPeriodNumberBegin.setEnabled(isPeriodEnable);
	    this.spnPeriodNumberEnd.setEnabled(isPeriodEnable);
	    if (isPeriodEnable) {
	      try
	      {
	        Map pdRang = GLRptCompositeFacadeFactory.getRemoteInstance().getPeriodRange(companyIdSet, peridType.getId().toString());
	        
	        PeriodEntity pe = new PeriodEntity(pdRang);
	        SpinnerUtil.managePeriodRange(pe, this.spnPeriodYearBegin, this.spnPeriodNumberBegin, this.spnPeriodYearEnd, this.spnPeriodNumberEnd, false, false);
	      }
	      catch (Exception e)
	      {
	        this.spnPeriodYearBegin.setEnabled(false);
	        this.spnPeriodYearEnd.setEnabled(false);
	        this.spnPeriodNumberBegin.setEnabled(false);
	        this.spnPeriodNumberEnd.setEnabled(false);
	        MsgBox.showInfo(this, e.getMessage());
	      }
	    }
	  }
	  
	  
	  private AccountTableInfo getSameAccountTableInfo()
	  {
	    CompanyOrgUnitInfo[] companys = getCompanys();
	    if (companys == null) {
	      return null;
	    }
	    AccountTableInfo accountTable = companys[0].getAccountTable();
	    if (accountTable == null) {
	      return null;
	    }
	    for (int i = 1; i < companys.length; i++)
	    {
	      if (companys[i].getAccountTable() == null) {
	        return null;
	      }
	      if (!companys[i].getAccountTable().getId().equals(accountTable.getId())) {
	        return null;
	      }
	    }
	    return accountTable;
	  }
	  
	  
	  private CurrencyInfo getSameBaseCurrencyInfo()
	  {
	    CompanyOrgUnitInfo[] companys = getCompanys();
	    if (companys == null) {
	      return null;
	    }
	    CurrencyInfo currencyInfo = companys[0].getBaseCurrency();
	    if (currencyInfo == null) {
	      return null;
	    }
	    for (int i = 1; i < companys.length; i++)
	    {
	      if (companys[i].getAccountPeriodType() == null) {
	        return null;
	      }
	      if (!companys[i].getBaseCurrency().getId().equals(currencyInfo.getId())) {
	        return null;
	      }
	    }
	    return currencyInfo;
	  }
	  
	  

	  protected Set getCompanyIdSet()
	  {
	    CompanyOrgUnitInfo[] companys = getCompanys();
	    Set idSet = new HashSet();
	    if (companys == null) {
	      return idSet;
	    }
	    for (int i = 0; i < companys.length; i++) {
	      idSet.add(companys[i].getId().toString());
	    }
	    return idSet;
	  }
	  
	  protected CompanyOrgUnitInfo[] getCompanys()
	  {
	    Object company = this.bpCompany.getValue();
	    if ((company instanceof CompanyOrgUnitInfo)) {
	      return new CompanyOrgUnitInfo[] { (CompanyOrgUnitInfo)company };
	    }
	    if ((this.bpCompany.getValue() instanceof CompanyOrgUnitInfo[])) {
	      return (CompanyOrgUnitInfo[])this.bpCompany.getValue();
	    }
	    if ((this.bpCompany.getValue() instanceof Object[]))
	    {
	      Object[] companyObjs = (Object[])this.bpCompany.getValue();
	      return toObjectArray2CompanyArray(companyObjs);
	    }
	    return null;
	  }
	  
	  private CompanyOrgUnitInfo[] toObjectArray2CompanyArray(Object[] companyObjs)
	  {
	    List companyList = new ArrayList();
	    for (int i = 0; i < companyObjs.length; i++) {
	      if ((companyObjs[i] instanceof CompanyOrgUnitInfo)) {
	        companyList.add(companyObjs[i]);
	      }
	    }
	    if (companyList.isEmpty()) {
	      return null;
	    }
	    CompanyOrgUnitInfo[] companys = new CompanyOrgUnitInfo[companyList.size()];
	    return (CompanyOrgUnitInfo[])companyList.toArray(companys);
	  }
	  
	  protected void setCompanys(CompanyOrgUnitInfo[] companys)
	  {
	    this.bpCompany.setValue(companys);
	  }
	  
	  protected void displayModeChanged()
	  {
	    setCompanyMaxLevel();
	  }
	  
	  private void setCompanyMaxLevel()
	  {
	    boolean isLevel = this.cbDisplayMode.getSelectedItem() == CompanyDisplayModeEnum.level;
	    this.lblCompanyLevel.setVisible(isLevel);
	    if (isLevel)
	    {
	      int maxLevel = 1;
	      CompanyOrgUnitInfo[] companys = getCompanys();
	      if (companys != null)
	      {
	        for (int i = 0; i < companys.length; i++)
	        {
	          int level = companys[i].getLevel();
	          if (maxLevel < level) {
	            maxLevel = level;
	          }
	        }
	        maxLevel++;
	      }
	      SpinnerNumberModel snmCompanyLevel = new SpinnerNumberModel(1, 1, maxLevel, 1);
	      this.spnCompanyLevel.setModel(snmCompanyLevel);
	    }
	  }
	  
	  private CompanyOrgUnitCollection getCompanyCollection(Set companyIdSet)
	  {
	    EntityViewInfo evi = new EntityViewInfo();
	    evi.getSelector().add(new SelectorItemInfo("id"));
	    evi.getSelector().add(new SelectorItemInfo("number"));
	    evi.getSelector().add(new SelectorItemInfo("name"));
	    evi.getSelector().add(new SelectorItemInfo("baseCurrency.id"));
	    FilterInfo f = new FilterInfo();
	    f.getFilterItems().add(new FilterItemInfo("id", companyIdSet, CompareType.INCLUDE));
	    evi.setFilter(f);
	    CompanyOrgUnitCollection comCol = null;
	    try
	    {
	      comCol = CompanyOrgUnitFactory.getRemoteInstance().getCompanyOrgUnitCollection(evi);
	    }
	    catch (BOSException e)
	    {
	      logger.error(e);
	    }
	    return comCol;
	  }
	  
	  public void setCurrCompany(CompanyOrgUnitInfo company)
	  {
	    this.company = company;
	  }
	  
	  public boolean isShow()
	  {
	    return this.show;
	  }
	  
	  
	  private String getCompanyIdFilter()
	  {
	    String userId = SysContext.getSysContext().getCurrentUserInfo().getId().toString();
	    StringBuffer companyIdStr = new StringBuffer();
	    CompanyOrgUnitInfo root = null;
	    boolean isPerm = true;
	    if (isUnionQuery())
	    {
	      root = SysContext.getSysContext().getCurrentFIUnit();
	      isPerm = this.queryInitData.isCompanyByUser();
	    }
	    try
	    {
	      return GLGeneralFacadeFactory.getRemoteInstance().getCompanyIDSQL(isPerm ? getPermissionItem() : null, root, false);
	    }
	    catch (Exception e)
	    {
	      handUIException(e);
	    }
	    return null;
	  }
	  
	  protected boolean canMultiSelectCompany()
	  {
		if(company.getLevel() <3){
			  GLRptBaseUI baseUI = (GLRptBaseUI)getUIContext().get("Owner");
			    String uiParam = (String)baseUI.getUIContext().get("UIClassParam");
			    if ((!GlUtils.isEmpty(uiParam)) && ("FSSC".equalsIgnoreCase(uiParam))) {
			      return true;
			    }
			    return false;
		}else{
			  return false;
		}
	  }
	  
	  private boolean isMultiSelectCompany()
	  {
	    return ((!SysContext.getSysContext().getCurrentFIUnit().isIsBizUnit()) && (isSupportUnion())) || (canMultiSelectCompany());
	  }
	  
	  protected void setCompanyDisplayMode()
	  {
	    if (isUnionQuery())
	    {
	      this.cbDisplayMode.addItem(CompanyDisplayModeEnum.level);
	      if (canDetailDisplay()) {
	        this.cbDisplayMode.addItem(CompanyDisplayModeEnum.details);
	      } else {
	        this.cbDisplayMode.setEnabled(false);
	      }
	    }
	    else
	    {
	      if (canMultiSelectCompany()) {
	        this.lblDisplayMode.setVisible(true);
	      } else {
	        this.lblDisplayMode.setVisible(false);
	      }
	      this.lblDisplayMode.setEnabled(canMergeDisplay());
	      this.cbDisplayMode.addItem(CompanyDisplayModeEnum.enumerate);
	      if (canMergeDisplay()) {
	        this.cbDisplayMode.addItem(CompanyDisplayModeEnum.merger);
	      } else {
	        this.cbDisplayMode.setEnabled(false);
	      }
	    }
	  }
	  
	  protected KDPromptSelector getCompanySelector()
	  {
	    if (isMultiSelectCompany())
	    {
	      CompanyOrgUnitInfo company = SysContext.getSysContext().getCurrentFIUnit();
	      CompanyOrgUnitInfo root = isUnionQuery() ? company : null;
	      return new CompanyTreePromptBox(this, root, this.bpCompany, getPermissionItem(), false);
	    }
	    NewCompanyF7 companySelector = new NewCompanyF7(this);
	    companySelector.setShowAssistantOrg(false);
	    companySelector.setIsCUFilter(false);
	    companySelector.setShowVirtual(false);
	    companySelector.setContainSealUp(false);
	    companySelector.setNeedAddAssistant(true);
	    companySelector.setOuterFilterInfo(getCompanyFilterInfo());
	    companySelector.setContainSealUp(true);
	    return companySelector;
	  }
	  
	  private FilterInfo getCompanyFilterInfo()
	  {
	    FilterInfo filter = new FilterInfo();
	    FilterItemCollection fic = filter.getFilterItems();
	    fic.add(new FilterItemInfo("unit.id", getCompanyIdFilter(), CompareType.INNER));
	    return filter;
	  }
	  
	  protected boolean canMergeDisplay()
	  {
	    return false;
	  }
	  
	  protected boolean canDetailDisplay()
	  {
	    return false;
	  }
	  
	  protected boolean isSupportUnion()
	  {
	    return true;
	  }
	  protected boolean isUnionQuery()
	  {
	    return (!getCurrCompany().isIsBizUnit()) && (isSupportUnion());
	  }
	  
	  protected CompanyOrgUnitInfo getCurrCompany()
	  {
	    return this.company;
	  }
	  
	  private Vector toVector(AbstractObjectCollection coll)
	  {
	    if (coll == null) {
	      return null;
	    }
	    Vector v = new Vector();
	    Iterator it = coll.iterator();
	    while (it.hasNext()) {
	      v.add(it.next());
	    }
	    return v;
	  }
	  
	  protected void initListener()
	  {
	    super.initListener();
	    this.bpCompany.addDataChangeListener(new DataChangeListener()
	    {
	      public void dataChanged(DataChangeEvent event)
	      {
	        try
	        {
	          if (isCompanyChanged(event)) {
	        	    companyChanged();
	          }
	        }
	        catch (Exception e)
	        {
	        	GLRptCompositeFilterUI.this.handUIException(e);
	        }
	      }
	    });
	    this.bpCompany.addPreChangeListener(new PreChangeListener()
	    {
	      public void preChange(PreChangeEvent e)
	      {
	    	 verifySelecteCompany(e);
	      }

	    });
	    this.cbDisplayMode.addItemListener(new ItemListener()
	    {
	      public void itemStateChanged(ItemEvent event)
	      {
	        if (event.getStateChange() == 1) {
	        	GLRptCompositeFilterUI.this.displayModeChanged();
	        }
	      }
	    });
//	    this.bpAccount.addItemListener(new ItemListener()
//	    {
//	      public void itemStateChanged(ItemEvent event)
//	      {
//	        try
//	        {
//	          if (event.getStateChange() == 1) {
//	        	  accountTableChanged();
//	          }
//	        }
//	        catch (Exception e)
//	        {
//	        	GLRptCompositeFilterUI.this.handUIException(e);
//	        }
//	      }
//	    });
//	    this.bpPeriod.addItemListener(new ItemListener()
//	    {
//	      public void itemStateChanged(ItemEvent event)
//	      {
//	        try
//	        {
//	          if (event.getStateChange() == 1) {
//	        	  periodTypeChanged();
//	          }
//	        }
//	        catch (Exception e)
//	        {
//	        	GLRptCompositeFilterUI.this.handUIException(e);
//	        }
//	      }
//	    });
	   
	  }
	  
	  protected boolean isCompanyChanged(DataChangeEvent e)
	  {
	    if (e.getNewValue() == null)
	    {
	      if (e.getOldValue() == null) {
	        return false;
	      }
	      return true;
	    }
	    if (e.getOldValue() == null) {
	      return true;
	    }
	    return !isCompanyEquals(e.getNewValue(), e.getOldValue());
	  }
	  protected boolean isCompanyEquals(Object newObject, Object oldObject)
	  {
	    if (((newObject instanceof Object[])) && ((oldObject instanceof Object[])) && (Arrays.toString((Object[])newObject).equalsIgnoreCase(Arrays.toString((Object[])oldObject)))) {
	      return true;
	    }
	    if (((newObject instanceof CompanyOrgUnitInfo)) && ((oldObject instanceof CompanyOrgUnitInfo))) {
	      return ((CompanyOrgUnitInfo)newObject).getId().equals(((CompanyOrgUnitInfo)oldObject).getId());
	    }
	    return false;
	  }
	  protected void verifySelecteCompany(PreChangeEvent e) {}

	  protected String getViewPermission() {
		  return  "gl_balanceList_view";
	  }

	  protected String getPermissionItem()
	  {
	   return "gl_balanceList_view";
	  }

	@Override
	public boolean verify() {
		CompanyOrgUnitInfo currCompany = SysContext.getSysContext().getCurrentFIUnit();
		if(currCompany.getLevel()==3){
			if(!currCompany.getNumber().contains("@02")){
				   MsgBox.showError("ÇëÇÐ»»µ½¸±ÕË²¾¡£");
		           SysUtil.abort();
			}
		}
		return super.verify();
	}
	  
}