/**
 * output package name
 */
package com.kingdee.eas.custom.client;

import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.ctrl.common.util.StringUtil;
import com.kingdee.bos.ctrl.kdf.data.impl.BOSQueryDelegate;
import com.kingdee.bos.ctrl.kdf.table.IRow;
import com.kingdee.bos.ctrl.kdf.table.KDTSelectBlock;
import com.kingdee.bos.ctrl.kdf.table.KDTable;
import com.kingdee.bos.ctrl.kdf.table.event.KDTMouseEvent;
import com.kingdee.bos.ctrl.kdf.table.event.KDTMouseListener;
import com.kingdee.bos.ctrl.kdf.table.event.KDTSelectEvent;
import com.kingdee.bos.ctrl.kdf.table.event.KDTSelectListener;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.metadata.entity.FilterInfo;
import com.kingdee.bos.metadata.entity.FilterItemCollection;
import com.kingdee.bos.metadata.entity.FilterItemInfo;
import com.kingdee.bos.metadata.entity.SelectorItemCollection;
import com.kingdee.bos.metadata.entity.SelectorItemInfo;
import com.kingdee.bos.metadata.entity.SorterItemInfo;
import com.kingdee.bos.metadata.query.util.CompareType;
import com.kingdee.bos.ui.face.CoreUIObject;
import com.kingdee.bos.ui.face.IUIWindow;
import com.kingdee.bos.ui.face.UIFactory;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.eas.base.commonquery.client.CommonQueryDialog;
import com.kingdee.eas.basedata.assistant.CurrencyCollection;
import com.kingdee.eas.basedata.assistant.CurrencyFactory;
import com.kingdee.eas.basedata.assistant.CurrencyInfo;
import com.kingdee.eas.basedata.assistant.PeriodInfo;
import com.kingdee.eas.basedata.assistant.SystemStatusCtrolUtils;
import com.kingdee.eas.basedata.master.account.AccountFacadeFactory;
import com.kingdee.eas.basedata.master.account.AccountPLType;
import com.kingdee.eas.basedata.master.account.AccountViewCollection;
import com.kingdee.eas.basedata.master.account.AccountViewFactory;
import com.kingdee.eas.basedata.master.account.AccountViewInfo;
import com.kingdee.eas.basedata.master.account.IAccountView;
import com.kingdee.eas.basedata.master.auxacct.AssistantHGFactory;
import com.kingdee.eas.basedata.master.auxacct.AssistantHGInfo;
import com.kingdee.eas.basedata.org.CompanyOrgUnitCollection;
import com.kingdee.eas.basedata.org.CompanyOrgUnitFactory;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.basedata.org.ICompanyOrgUnit;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.common.client.SysContext;
import com.kingdee.eas.common.client.UIContext;
import com.kingdee.eas.custom.GLRptCompAccountBalanceFacadeFactory;
import com.kingdee.eas.fi.gl.GlUtils;
import com.kingdee.eas.fi.gl.ReportConditionBalanceListAssist;
import com.kingdee.eas.fi.gl.client.ReportBalanceListAssistUI;
import com.kingdee.eas.fi.gl.common.SimpleCompanyUserObject;
import com.kingdee.eas.fi.gl.rpt.CompanyDisplayModeEnum;
import com.kingdee.eas.fi.gl.rpt.GLRptAccountBalanceCondition;
import com.kingdee.eas.fi.gl.rpt.GLRptAccountBalanceFacadeFactory;
import com.kingdee.eas.fi.gl.rpt.GLRptBaseCondition;
import com.kingdee.eas.fi.gl.rpt.GLRptRowSet;
import com.kingdee.eas.fi.gl.rpt.GLRptSubLedgerCondition;
import com.kingdee.eas.fi.gl.rpt.GLRptTreeNode;
import com.kingdee.eas.fi.gl.rpt.IGLRptBaseFacade;
import com.kingdee.eas.fi.gl.rpt.PrintButtonEnum;
import com.kingdee.eas.fi.gl.rpt.client.GLRptAccountBalancePrintConfig;
import com.kingdee.eas.fi.gl.rpt.client.GLRptAccountBalancePrintDelegate;
import com.kingdee.eas.fi.gl.rpt.client.GLRptBasePrintConfig;
import com.kingdee.eas.fi.gl.rpt.client.GLRptBaseQueryUI;
import com.kingdee.eas.fi.gl.rpt.client.GLRptCommonItemInfo;
import com.kingdee.eas.fi.gl.rpt.client.GLRptSubLedgerUI;
import com.kingdee.eas.fi.gl.rpt.client.GLRptUtils;
import com.kingdee.eas.framework.SystemEnum;
import com.kingdee.eas.framework.report.RptException;
import com.kingdee.eas.framework.report.util.KDTableUtil;
import com.kingdee.eas.framework.report.util.PeriodEntity;
import com.kingdee.eas.util.SysUtil;
import com.kingdee.eas.util.client.EASResource;
import com.kingdee.eas.util.client.MsgBox;
import com.kingdee.util.StringUtils;

/**
 * output class name
 */
public class GLRptCompAccountBalanceUI extends AbstractGLRptCompAccountBalanceUI
{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 3231470283549418679L;
	
	private static final Logger logger = CoreUIObject.getLogger(GLRptCompAccountBalanceUI.class);
    private static final String BN_TOTAL = EASResource.getString("com.kingdee.eas.fi.gl.ReportBase", "BNSum");
    private static final String BW_TOTAL = EASResource.getString("com.kingdee.eas.fi.gl.ReportBase", "BWSum");
    private static final String ACCOUNT_TOTAL = EASResource.getString("com.kingdee.eas.fi.gl.GLRes", "total");
    private static final String CURRENCY_TOTAL = EASResource.getString("com.kingdee.eas.fi.gl.GLRes", "subtotal");
    
    public GLRptCompAccountBalanceUI()
      throws Exception
    {}
    
    protected void initWorkButton()
    {
      super.initWorkButton();
      this.btnSubsidiaryLedger.setIcon(EASResource.getIcon("imgTbtn_listaccount"));
      this.btnAssitAct.setIcon(EASResource.getIcon("imgTbtn_assistantgeneralledger"));
      this.menuItemSubsidiaryLedger.setIcon(EASResource.getIcon("imgTbtn_listaccount"));
      this.menuItemAssistAct.setIcon(EASResource.getIcon("imgTbtn_assistantlistaccount"));
      
      boolean isEnable = getCurrCompany().isIsBizUnit();
      this.actionMenuItemPrintParame.setEnabled(isEnable);
      this.actionMenuItemPrintParame.setVisible(isEnable);
    }
    
    protected void initListener()
    {
      super.initListener();
      getTable().addKDTSelectListener(new KDTSelectListener()
      {
        public void tableSelectChanged(KDTSelectEvent e)
        {
          int rowIndex = e.getSelectBlock().getTop();
          try
          {
            GLRptCompAccountBalanceUI.this.tblMain_SelectChanged(rowIndex);
          }
          catch (Exception ex)
          {
            GLRptCompAccountBalanceUI.this.handleException(ex);
          }
        }
      });
      getTable().addKDTMouseListener(new KDTMouseListener()
      {
        public void tableClicked(KDTMouseEvent e)
        {
          try
          {
            if (e.getClickCount() > 1) {
              GLRptCompAccountBalanceUI.this.doTableDoubleClick(e.getRowIndex());
            }
          }
          catch (Exception ex)
          {
            GLRptCompAccountBalanceUI.this.handUIException(ex);
          }
        }
      });
    }
    
    protected void setQueryDialogSize(CommonQueryDialog dialog)
    {
      dialog.setWidth(500);
      dialog.setHeight(350);
    }
    
    protected GLRptBaseQueryUI getCustQueryPanel()
      throws Exception
    {
      return new GLRptCompAccountBalanceQueryUI();
    }
    @Override
    protected GLRptBaseCondition createQueryCond(Map param)
    {
      try
      {
        return new GLRptAccountBalanceCondition(param);
      }
      catch (CloneNotSupportedException e)
      {
        logger.error("", e);
      }
      return null;
    }
    
    @Override
    protected void doGLRowSet(GLRptRowSet rs)
      throws EASBizException, BOSException
    {
      rs.beforeFirst();
      GLRptAccountBalanceCondition cond = (GLRptAccountBalanceCondition)getQueryCond();
      while (rs.next())
      {
        boolean isBWAccountSum = false;
        GLRptTreeNode node = null;
        if (CompanyDisplayModeEnum.enumerate != cond.getCompanyDisplayMode()) {
          node = (GLRptTreeNode)rs.getObject("companyName");
        }
        if (cond.getIncludeBWAccount())
        {
          Object isBWSum = rs.getObject("FIsAccountBWSum");
          if (isBWSum != null) {
            isBWAccountSum = new BigDecimal(isBWSum.toString()).intValue() == 1;
          }
        }
        if (CompanyDisplayModeEnum.merger == cond.getCompanyDisplayMode())
        {
          SimpleCompanyUserObject obj = (SimpleCompanyUserObject)node.getUserObject();
          if (!node.isLeaf()) {
            obj.setName(EASResource.getString("com.kingdee.eas.fi.gl.FIAutoGenerateResource", "367_RptAsstactGGUI"));
          }
        }
        Object isAccTotal = rs.getObject("FIsAccountTotal");
        boolean isAccountTotal = isAccTotal != null;
        if (isBWAccountSum)
        {
          boolean isBWAccount = false;
          Object isBW = rs.getObject("FBWType");
          if (isBW != null) {
            isBWAccount = new BigDecimal(isBW.toString()).intValue() == 1;
          }
          if (CompanyDisplayModeEnum.enumerate == cond.getCompanyDisplayMode()) {
            rs.updateString("accountName", isBWAccount ? BW_TOTAL : BN_TOTAL);
          } else if (node.getLevel() == 0) {
            rs.updateString("accountName", isBWAccount ? BW_TOTAL : BN_TOTAL);
          }
        }
        else if (isAccountTotal)
        {
          if (CompanyDisplayModeEnum.enumerate == cond.getCompanyDisplayMode()) {
            rs.updateString("accountName", ACCOUNT_TOTAL);
          } else if (node.getLevel() == 0) {
            rs.updateString("accountName", ACCOUNT_TOTAL);
          }
        }
        if (cond.isAllCurrency())
        {
          boolean isCurrencyTotal = rs.getInt("FIsCurrencyTotal") == 1;
          if ((isCurrencyTotal) && ((CompanyDisplayModeEnum.enumerate == cond.getCompanyDisplayMode()) || ((!node.isLeaf()) && (node.getParentNode() == null)))) {
            rs.updateString("currencyName", CURRENCY_TOTAL);
          }
        }
      }
    }
   
    @Override
    protected void afterFillTableRow(IRow row, String[] colNames)
    {
      GLRptAccountBalanceCondition cond = (GLRptAccountBalanceCondition)getQueryCond();
      boolean isBWAccountSum = false;
      if (cond.getIncludeBWAccount())
      {
        Object isBWSum = row.getCell("FIsAccountBWSum").getValue();
        if (isBWSum != null) {
          isBWAccountSum = new BigDecimal(row.getCell("FIsAccountBWSum").getValue().toString()).intValue() == 1;
        }
      }
      Object isAccTotal = row.getCell("FIsAccountTotal").getValue();
      boolean isAccountTotal = isAccTotal != null;
      if (isBWAccountSum) {
        row.getStyleAttributes().setBackground(SUB_TOTAL_COLOR);
      } else if (isAccountTotal) {
        row.getStyleAttributes().setBackground(TOTAL_COLOR);
      }
      if (cond.isAllCurrency())
      {
        boolean isCurrencyTotal = new BigDecimal(row.getCell("FIsCurrencyTotal").getValue().toString()).intValue() == 1;
        if (isCurrencyTotal)
        {
          row.getStyleAttributes().setBackground(TOTAL_COLOR);
        }
        else
        {
          String key = null;
          int currencyPre = new BigDecimal(row.getCell("currencyPre").getValue().toString()).intValue();
          KDTable tblMain = getTable();
          for (int i = 0; i < tblMain.getColumnCount(); i++)
          {
            key = tblMain.getColumn(i).getKey();
            if ((key != null) && (key.endsWith("For"))) {
              row.getCell(key).getStyleAttributes().setNumberFormat(GlUtils.getDecimalFormat(currencyPre));
            }
          }
        }
      }
    }
    
    @Override
    protected GLRptBaseCondition buildLinkedQuery(String uiParams)
      throws EASBizException, BOSException
    {
      String[] params = uiParams.split(";");
      HashMap paramMap = new HashMap();
      for (int i = 0; i < params.length; i++)
      {
        String[] param = params[i].split("=", 2);
        if (((param[0] != null) && (param[0].trim().length() > 0)) || ((param[1] != null) && (param[1].trim().length() > 0))) {
          paramMap.put(param[0].trim().toUpperCase(), param[1].trim());
        }
      }
      GLRptAccountBalanceCondition condition = new GLRptAccountBalanceCondition();
      Object beginYearObj = paramMap.get("BEGIN_PERIOD_YEAR");
      Object beginMonthObj = paramMap.get("BEGIN_PERIOD_NUMBER");
      Object endYearObj = paramMap.get("END_PERIOD_YEAR");
      Object endMonthObj = paramMap.get("END_PERIOD_NUMBER");
      int startPeriodYear = -1;
      int startPeriodNumber = -1;
      int endPeriodYear = -1;
      int endPeriodNumber = -1;
      if ((beginYearObj == null) || (beginMonthObj == null) || (endYearObj == null) || (endMonthObj == null))
      {
        PeriodInfo currPeriod = SystemStatusCtrolUtils.getCurrentPeriod(null, SystemEnum.GENERALLEDGER, getCurrCompany());
        startPeriodYear = endPeriodYear = currPeriod.getPeriodYear();
        startPeriodNumber = endPeriodNumber = currPeriod.getPeriodNumber();
      }
      else
      {
        startPeriodYear = Integer.parseInt(beginYearObj.toString());
        startPeriodNumber = Integer.parseInt(beginMonthObj.toString());
        endPeriodYear = Integer.parseInt(endYearObj.toString());
        endPeriodNumber = Integer.parseInt(endMonthObj.toString());
      }
      String startAccountNumber = (String)paramMap.get("BEGIN_ACCOUNT_NUMBER");
      String endAccountNumber = (String)paramMap.get("END_ACCOUNT_NUMBER");
      
      boolean isIncludeNotPosted = false;
      Object isIncludeNotPostedObj = paramMap.get("IS_INCLUDE_UNPOST_VOUCHER");
      if (isIncludeNotPostedObj != null) {
        isIncludeNotPosted = Boolean.valueOf((String)isIncludeNotPostedObj).booleanValue();
      }
      boolean isShowAsstDetail = false;
      Object isShowAsstActObj = paramMap.get("IS_SHOW_ASSTACT");
      if (isShowAsstActObj != null) {
        isShowAsstDetail = Boolean.valueOf((String)isShowAsstActObj).booleanValue();
      }
      boolean isShowQty = false;
      Object isShowQtyObj = paramMap.get("IS_SHOW_QTY");
      if (isShowQtyObj != null) {
        isShowQty = Boolean.valueOf((String)isShowQtyObj).booleanValue();
      }
      boolean isAmountZero = false;
      Object isAmountZeroObj = paramMap.get("IS_SHOW_ZERO_AMOUNT");
      if (isAmountZeroObj != null) {
        isAmountZero = Boolean.valueOf((String)isAmountZeroObj).booleanValue();
      }
      int accountLevel = 1;
      if (startAccountNumber != null) {
        accountLevel = startAccountNumber.split("\\.").length;
      }
      if (endAccountNumber != null)
      {
        int level = endAccountNumber.split("\\.").length;
        if (level > accountLevel) {
          accountLevel = level;
        }
      }
      String currencyId = null;
      String currencyNumber = (String)paramMap.get("CURRENCY_NUMBER");
      String currencyName = (String)paramMap.get("CURRENCY_NAME");
      EntityViewInfo evi = new EntityViewInfo();
      evi.getSelector().add(new SelectorItemInfo("id"));
      evi.getSelector().add(new SelectorItemInfo("number"));
      evi.getSelector().add(new SelectorItemInfo("name"));
      if (currencyNumber != null)
      {
        FilterInfo filter = new FilterInfo();
        filter.getFilterItems().add(new FilterItemInfo("number", currencyNumber));
        evi.setFilter(filter);
        CurrencyCollection currColl = CurrencyFactory.getRemoteInstance().getCurrencyCollection(evi);
        if (currColl.size() > 0)
        {
          currencyId = currColl.get(0).getId().toString();
          currencyName = currColl.get(0).getName();
        }
      }
      else if (currencyName != null)
      {
        FilterInfo filter = new FilterInfo();
        filter.getFilterItems().add(new FilterItemInfo("name", currencyName));
        evi.setFilter(filter);
        CurrencyCollection currColl = CurrencyFactory.getRemoteInstance().getCurrencyCollection(evi);
        if (currColl.size() > 0)
        {
          currencyId = currColl.get(0).getId().toString();
          currencyNumber = currColl.get(0).getNumber();
        }
      }
      if (currencyId == null)
      {
        currencyId = getCurrCompany().getBaseCurrency().getId().toString();
        currencyNumber = getCurrCompany().getBaseCurrency().getNumber();
        currencyName = getCurrCompany().getBaseCurrency().getName();
        if (currencyName == null) {
          currencyName = CurrencyFactory.getRemoteInstance().getCurrencyInfo(new ObjectUuidPK(currencyId)).getName();
        }
      }
      if (!getCurrCompany().isIsBizUnit())
      {
        String companys = (String)paramMap.get("COMPANYS");
        if (companys != null)
        {
          evi = new EntityViewInfo();
          evi.getSelector().add(new SelectorItemInfo("id"));
          evi.getSelector().add(new SelectorItemInfo("number"));
          evi.getSelector().add(new SelectorItemInfo("name"));
          evi.getSelector().add(new SelectorItemInfo("longNumber"));
          evi.getSelector().add(new SelectorItemInfo("isBizUnit"));
          evi.getSelector().add(new SelectorItemInfo("level"));
          evi.getSorter().add(new SorterItemInfo("longNumber"));
          FilterInfo filter = new FilterInfo();
          HashSet companyNumberSet = new HashSet();
          String[] companyNumbers = companys.split("\\|");
          for (int i = 0; i < companyNumbers.length; i++) {
            if ((companyNumbers[i] != null) && (companyNumbers[i].trim().length() > 0)) {
              companyNumberSet.add(companyNumbers[i]);
            }
          }
          if (companyNumberSet.size() == 0)
          {
            MsgBox.showError(this, EASResource.getString("com.kingdee.eas.fi.gl.GLAutoGenerateResource", "97_ReportBalanceListUI"));
            SysUtil.abort();
          }
          filter.getFilterItems().add(new FilterItemInfo("number", companyNumberSet, CompareType.INCLUDE));
          evi.setFilter(filter);
          CompanyOrgUnitCollection orgColl = CompanyOrgUnitFactory.getRemoteInstance().getCompanyOrgUnitCollection(evi);
          StringBuffer companyIds = new StringBuffer();
          for (int i = 0; i < orgColl.size(); i++)
          {
            if (i > 0) {
              companyIds.append(",");
            }
            companyIds.append("'");
            companyIds.append(orgColl.get(i).getId().toString());
            companyIds.append("'");
          }
          getCurrCompany().setDescription(companyIds.toString());
        }
      }
      condition.setPeriodYearStart(startPeriodYear);
      condition.setPeriodNumberStart(startPeriodNumber);
      condition.setPeriodYearEnd(endPeriodYear);
      condition.setPeriodNumberEnd(endPeriodNumber);
      
      condition.setAccountCodeStart(startAccountNumber);
      condition.setAccountCodeEnd(endAccountNumber);
      condition.setAccountLevelStart(1);
      condition.setAccountLevelEnd(accountLevel);
      condition.setCurrencyID(currencyId);
      condition.setCurrencyName(currencyName);
      
      condition.setDisplayAsstDetail(isShowAsstDetail);
      
      condition.setOptionPosting(isIncludeNotPosted);
      
      condition.setShowQty(isShowQty);
      
      condition.setOptionAmountZero(isAmountZero);
      if (getCurrCompany().isIsBizUnit())
      {
        PeriodEntity pe = PeriodEntity.requestPeriodEntity(getCurrCompany());
        condition.getExpandInfo().put("periodEntity", pe);
        condition.getExpandInfo().put("isAfterPeriodIncluded", Boolean.valueOf((endPeriodYear > pe.getCurrentYear()) || ((endPeriodYear == pe.getCurrentYear()) && (endPeriodNumber > pe.getCurrentPeriod()))));
      }
      else
      {
        PeriodEntity pe = PeriodEntity.requestPeriodEntity(getCurrCompany());
        condition.getExpandInfo().put("periodEntity", pe);
      }
      condition.getExpandInfo().put("company", getCurrCompany());
      return condition;
    }
    
    protected void tblMain_SelectChanged(int rowIndex)
      throws EASBizException, BOSException
    {
      setActionStatus(rowIndex);
    }
    
    @Override
    protected void setActionStatus(int rowIndex)
      throws EASBizException, BOSException
    {
      this.actionAssistAccount.setEnabled(false);
      this.actionSubsidiaryLedger.setEnabled(false);
      GLRptAccountBalanceCondition condition = (GLRptAccountBalanceCondition)getQueryCond();
      if (condition != null)
      {
        IRow row = getTable().getRow(rowIndex);
        if (row != null)
        {
          String strAccountId = (String)row.getCell("FAccountID").getValue();
          if ((strAccountId == null) || (strAccountId.trim().length() == 0)) {
            return;
          }
          SelectorItemCollection accountSel = new SelectorItemCollection();
          accountSel.add(new SelectorItemInfo("id"));
          accountSel.add(new SelectorItemInfo("isLeaf"));
          accountSel.add(new SelectorItemInfo("CAA.id"));
          AccountViewInfo av = AccountViewFactory.getRemoteInstance().getAccountViewInfo(new ObjectUuidPK(strAccountId), accountSel);
          if (CompanyDisplayModeEnum.enumerate == condition.getCompanyDisplayMode())
          {
            if (av != null)
            {
              if (av.getId() != null) {
                this.actionSubsidiaryLedger.setEnabled(true);
              }
              if ((av.isIsLeaf()) && (canJoinQueryForPeriodLimit()))
              {
                int startPeriod = condition.getPeriodYearStart() * 100 + condition.getPeriodNumberStart();
                int endPeriod = condition.getPeriodYearEnd() * 100 + condition.getPeriodNumberEnd();
                Set caas = AccountFacadeFactory.getRemoteInstance().getCAAIdsByPeriodRange(av.getId().toString(), startPeriod, endPeriod);
                if (caas.size() > 0) {
                  this.actionAssistAccount.setEnabled(true);
                }
              }
            }
          }
          else
          {
            GLRptTreeNode treeNode = (GLRptTreeNode)getTable().getCell(rowIndex, "companyName").getValue();
            SimpleCompanyUserObject company = null;
            if (treeNode != null) {
              company = (SimpleCompanyUserObject)treeNode.getUserObject();
            }
            if (company != null)
            {
              SelectorItemCollection companySel = new SelectorItemCollection();
              companySel.add(new SelectorItemInfo("id"));
              companySel.add(new SelectorItemInfo("isBizUnit"));
              CompanyOrgUnitInfo companyInfo = CompanyOrgUnitFactory.getRemoteInstance().getCompanyOrgUnitInfo(new ObjectUuidPK(company.getCompanyId()), companySel);
              if (companyInfo.isIsBizUnit())
              {
                Map ctx = new HashMap();
                ctx.put("companyId", company.getCompanyId());
                ctx.put("accountTableId", condition.getAccountTableId());
                ctx.put("accountPeriodTypeId", condition.getPeriodTypeId());
                fetchCompany(ctx);
                if (av != null)
                {
                  this.actionSubsidiaryLedger.setEnabled(true);
                  if ((av.isIsLeaf()) && (canJoinQueryForPeriodLimit()))
                  {
                    int startPeriod = condition.getPeriodYearStart() * 100 + condition.getPeriodNumberStart();
                    int endPeriod = condition.getPeriodYearEnd() * 100 + condition.getPeriodNumberEnd();
                    Set caas = AccountFacadeFactory.getRemoteInstance().getCAAIdsByPeriodRange(av.getId().toString(), startPeriod, endPeriod);
                    if (caas.size() > 0) {
                      this.actionAssistAccount.setEnabled(true);
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    
    @Override
    protected GLRptBaseCondition getSysDefaultQueryCond()
      throws EASBizException, BOSException
    {
      if (!getCurrCompany().isIsBizUnit()) {
        return null;
      }
      GLRptBaseCondition cond = createQueryCond(new HashMap());
      CompanyOrgUnitInfo company = CompanyOrgUnitFactory.getRemoteInstance().getCompanyOrgUnitInfo(new ObjectUuidPK(getCurrCompany().getId()), GlUtils.getCompanySic());
      cond.setCurrencyID(company.getBaseCurrency().getId().toString());
      cond.setPeriodTypeId(company.getAccountPeriodType().getId().toString());
      cond.switchCompany(company);
      cond.setCompanyId(company.getId().toString());
      cond.setCurrencyName(company.getBaseCurrency().getName());
      PeriodInfo currPeriod = SystemStatusCtrolUtils.getCurrentPeriod(null, SystemEnum.GENERALLEDGER, company);
      if (currPeriod == null) {
        throw new RptException(RptException.PERIOD_ERROR);
      }
      cond.setPeriodYearStart(currPeriod.getPeriodYear());
      cond.setPeriodNumberStart(currPeriod.getPeriodNumber());
      cond.setPeriodYearEnd(currPeriod.getPeriodYear());
      cond.setPeriodNumberEnd(currPeriod.getPeriodNumber());
      String accountTableId = GlUtils.getAccountTableInfo(company, Integer.valueOf(currPeriod.getPeriodYear()), Integer.valueOf(currPeriod.getPeriodNumber())).getId().toString();
      cond.setAccountTableId(accountTableId);
      cond.setAccountLevelEnd(1);
      return cond;
    }
    
    private boolean canJoinQueryForPeriodLimit()
    {
    	GLRptAccountBalanceCondition condition = (GLRptAccountBalanceCondition)getQueryCond();
      PeriodEntity pe = (PeriodEntity)condition.getExpandInfo().get("periodEntity");
      if (pe == null) {
        return true;
      }
      return condition.getPeriodYearEnd() * 100 + condition.getPeriodNumberEnd() <= pe.getCurrentYear() * 100 + pe.getCurrentPeriod();
    }
    
    protected void doTableDoubleClick(int rowIndex)
    {
      if (this.actionSubsidiaryLedger.isEnabled()) {
        try
        {
          setCursorOfWair();
          findSubsidiaryLedger();
        }
        catch (Exception ex)
        {
          handUIException(ex);
        }
      } else {}
    }
    
    public void actionSubsidiaryLedger_actionPerformed(ActionEvent e)
      throws Exception
    {
      checkSelected();
      findSubsidiaryLedger();
    }
    
   
    public void actionAssistAccount_actionPerformed(ActionEvent e)
      throws Exception
    {
      checkSelected();
      findAssistAccountBalance();
    }
    
    public void checkSelected()
    {
      if ((getTable().getRowCount() == 0) || (getTable().getSelectManager().size() == 0))
      {
        MsgBox.showWarning(this, EASResource.getString("com.kingdee.eas.framework.FrameWorkResource.Msg_MustSelected"));
        SysUtil.abort();
      }
    }
    
    private void findSubsidiaryLedger()
      throws Exception
    {
      KDTSelectBlock sb = getTable().getSelectManager().get();
      if ((sb == null) || (getQueryCond() == null)) {
        return;
      }
      GLRptSubLedgerCondition cond = new GLRptSubLedgerCondition(getQueryCond());
      cond.setCompanyDisplayMode(CompanyDisplayModeEnum.enumerate);
      joinQury(cond, GLRptSubLedgerUI.class.getName());
    }
    
    private void findAssistAccountBalance()
      throws Exception
    {
      KDTSelectBlock sb = getTable().getSelectManager().get();
      if ((sb != null) && (getQueryCond() != null))
      {
        int top = sb.getTop();
        IRow row = getTable().getRow(top);
        String strAccountId = (String)row.getCell("FAccountID").getValue();
        AccountViewInfo acc = null;
        if ((strAccountId != null) && (strAccountId.trim().length() > 0)) {
          acc = AccountViewFactory.getRemoteInstance().getAccountViewInfo(new ObjectUuidPK(strAccountId));
        }
        String strAssisthgId = null;
        if (row.getCell("FAssistGrpId") != null) {
          strAssisthgId = (String)row.getCell("FAssistGrpId").getValue();
        }
        GLRptAccountBalanceCondition cond = (GLRptAccountBalanceCondition)getQueryCond();
        CurrencyInfo currency = null;
        if (cond.isAllCurrency())
        {
          String cyId = (String)row.getCell("FCurrencyID").getValue();
          if ((cyId != null) && (cyId.trim().length() > 0)) {
            currency = CurrencyFactory.getRemoteInstance().getCurrencyInfo(new ObjectUuidPK(cyId));
          }
        }
        if ((cond.isNotIncluePLVoucher()) && 
          ((acc == null) || ((acc != null) && (acc.getPLType() != AccountPLType.NONE))) && 
          (!MsgBox.isOk(MsgBox.showConfirm2(this, EASResource.getString("com.kingdee.eas.fi.gl.GLAutoGenerateResource", "98_ReportBalanceListUI"))))) {
          SysUtil.abort();
        }
        EntityViewInfo evi = new EntityViewInfo();
        ReportConditionBalanceListAssist assistBalanceCondition = new ReportConditionBalanceListAssist(cond);
        assistBalanceCondition.setOptionAmountZero(cond.isOptionAmountZero());
        assistBalanceCondition.setOptionShowQty(cond.isShowQty());
        if (currency != null)
        {
          assistBalanceCondition.setCurrencyID(currency.getId().toString());
          assistBalanceCondition.setCurrencyName(currency.getName());
        }
        evi.put("GLFixCondition", assistBalanceCondition);
        if ((strAssisthgId != null) && (strAssisthgId.trim().length() > 0))
        {
          evi.put("asstHGId", strAssisthgId);
          assistBalanceCondition.setAssistItemId(strAssisthgId);
          
          SelectorItemCollection selector = new SelectorItemCollection();
          selector.add("id");
          selector.add("asstAccount.id");
          AssistantHGInfo hg = AssistantHGFactory.getRemoteInstance().getAssistantHGInfo(new ObjectUuidPK(strAssisthgId), selector);
          if (hg.getAsstAccount() != null) {
            assistBalanceCondition.setAsstAccountId(hg.getAsstAccount().getId().toString());
          }
        }
        CompanyOrgUnitInfo linkCompany;
        if (CompanyDisplayModeEnum.enumerate == cond.getCompanyDisplayMode())
        {
          assistBalanceCondition.setAccountId(strAccountId);
          linkCompany = cond.getCompany();
        }
        else
        {
          String companyId = getSelectCompanyId();
          assistBalanceCondition.setCompanyId(companyId);
          
          Map ctx = new HashMap();
          ctx.put("companyId", companyId); 
          ctx.put("accountTableId", cond.getAccountTableId());
          ctx.put("accountPeriodTypeId", cond.getPeriodTypeId());
          ctx.put("asstHGId", strAssisthgId);
          fetchCompany(ctx); 
          linkCompany = (CompanyOrgUnitInfo)ctx.get("company"); 
          AccountViewInfo linkAV = findAccountViewInfoByNumber(acc.getNumber(), companyId, cond.getAccountTableId());
          if (linkAV != null) {
            assistBalanceCondition.setAccountId(linkAV.getId().toString());
          }
        }
        ReportBalanceListAssistUI.queryByCondition(evi, this, linkCompany);
      }
    }
    
    private AccountViewInfo findAccountViewInfoByNumber(String strNumber, String companyId, String accountTableId)
      throws BOSException
    {
      if (StringUtil.isEmptyString(strNumber)) {
        return null;
      }
      EntityViewInfo view = new EntityViewInfo();
      SelectorItemCollection sic = view.getSelector();
      sic.add(new SelectorItemInfo("id"));
      sic.add(new SelectorItemInfo("number"));
      sic.add(new SelectorItemInfo("isLeaf"));
      sic.add(new SelectorItemInfo("isQty"));
      sic.add(new SelectorItemInfo("CAA"));
      FilterInfo filter = new FilterInfo();
      view.setFilter(filter);
      FilterItemCollection fic = filter.getFilterItems();
      fic.add(new FilterItemInfo("number", strNumber.trim()));
      fic.add(new FilterItemInfo("companyID.id", companyId));
      fic.add(new FilterItemInfo("accountTableID.id", accountTableId));
      filter.setMaskString("#0 and #1 and #2 ");
      AccountViewInfo accountViewInfo = null;
      IAccountView accountView = AccountViewFactory.getRemoteInstance();
      AccountViewCollection accountViewCollection = accountView.getAccountViewCollection(view);
      if ((accountViewCollection != null) && (accountViewCollection.size() != 0)) {
        accountViewInfo = accountViewCollection.get(0);
      }
      return accountViewInfo;
    }
    
    private void fetchCompany(Map uiContext)
      throws BOSException, EASBizException
    {
      String companyId = (String)uiContext.get("companyId");
      CompanyOrgUnitInfo company = (CompanyOrgUnitInfo)uiContext.get("company");
      BOSUuid preAccountTableId = null;
      if ((company != null) && (company.getAccountTable() != null)) {
        preAccountTableId = company.getAccountTable().getId();
      }
      if (companyId == null) {
        if (company == null) {
          companyId = SysContext.getSysContext().getCurrentFIUnit().getId().toString();
        } else {
          companyId = company.getId().toString();
        }
      }
      company = getCompanyById(companyId);
      if (preAccountTableId != null) {
        company.getAccountTable().setId(preAccountTableId);
      }
      String accountTableId = (String)uiContext.get("accountTableId");
      String accountPeriodTypeId = (String)uiContext.get("accountPeriodTypeId");
      if ((accountTableId != null) && (company.getAccountTable() != null)) {
        company.getAccountTable().setId(BOSUuid.read(accountTableId));
      }
      if (accountPeriodTypeId != null) {
        company.getAccountPeriodType().setId(BOSUuid.read(accountPeriodTypeId));
      }
      uiContext.put("companyId", companyId);
      uiContext.put("company", company);
    }
    
    private CompanyOrgUnitInfo getCompanyById(String companyId)
      throws BOSException, EASBizException
    {
      ICompanyOrgUnit companyOrgUnit = CompanyOrgUnitFactory.getRemoteInstance();
      IObjectPK pk = new ObjectUuidPK(companyId);
      EntityViewInfo view = new EntityViewInfo();
      SelectorItemCollection sic = view.getSelector();
      sic.addObjectCollection(GlUtils.getCompanySic());
      return companyOrgUnit.getCompanyOrgUnitInfo(pk, sic);
    }
    
    protected String getPrintConfigUIName()
    {
      return "com.kingdee.eas.fi.gl.rpt.client.GLRptAccountBalancePrintConfigUI";
    }
    
    public Object getTablePreferenceSchemaKey()
    {
      String userId = SysContext.getSysContext().getCurrentUserInfo().getId().toString();
      String companyId = SysContext.getSysContext().getCurrentFIUnit().getId().toString();
      GLRptAccountBalanceCondition cond = (GLRptAccountBalanceCondition)getQueryCond();
      String currencyId = cond.getCurrencyID(); 
      String showQty = String.valueOf(cond.isShowQty());
      String showAsstact = String.valueOf(cond.getDisplayAsstDetail());
      return userId + "||" + companyId + "||" + currencyId + "||" + showQty + "||" + showAsstact;
    }
    
    protected BOSQueryDelegate getPrintQueryDelegate(GLRptRowSet rs, GLRptBaseCondition cond, GLRptBasePrintConfig config)
      throws EASBizException, BOSException
    {
      return new GLRptAccountBalancePrintDelegate(rs, getPrintVar(cond), config);
    }
    
    protected Map getPrintVar(GLRptBaseCondition cond)
    {
      Map printParams = new HashMap();
      printParams.put("mapAccountLevel", Integer.toString(cond.getAccountLevelEnd()));
      printParams.put("mapCompany", cond.getCompany());
      printParams.put("currencyId", cond.getCurrencyID());
      printParams.put("mapCurrency", cond.getCurrencyName());
      printParams.put("mapPeriod", this.lblPeriod.getText());
      printParams.put("showQty", Boolean.valueOf(cond.isShowQty()));
      return printParams;
    }
    
    protected GLRptBasePrintConfig getPrintConfig()
    {
      return new GLRptAccountBalancePrintConfig(this);
    }
    
    protected String getQueryName()
    {
      return "com.kingdee.eas.fi.gl.app.VoucherReportQuery";
    }
    
    protected IGLRptBaseFacade getIGLRptFacade()
      throws BOSException
    {
      //return GLRptAccountBalanceFacadeFactory.getRemoteInstance();
      return GLRptCompAccountBalanceFacadeFactory.getRemoteInstance();
    }
    
    protected List getCommonPrintButtons()
    {
      List list = new ArrayList();
      list.add(new GLRptCommonItemInfo(PrintButtonEnum.CompanyArea, new String[] { EASResource.getString("com.kingdee.eas.fi.gl.ReportPrintTitle", "SelectedCompany"), EASResource.getString("com.kingdee.eas.fi.gl.ReportPrintTitle", "AllCompany") }));
      return list;
    }
    
    protected void joinQury(GLRptSubLedgerCondition cond, String uiClassName)
      throws Exception
    {
      if (getDataComponent().isEmpty()) {
        return;
      }
      try
      {
        setCursorOfWair();
        setLinkedCompany(cond);
        setLinkedAccount(cond);
        setLinkedCurrency(cond);
        setLinkedAsstactList(cond);
        //cond.setAccountTableId("jbYAAACapzAXaY5t");
        String companyId = "jbYAAACeIRDM567U";
//        cond.setCompanyId(companyId);
        CompanyOrgUnitInfo companyInfo = CompanyOrgUnitFactory.getRemoteInstance().getCompanyOrgUnitInfo( new ObjectUuidPK(companyId));
//        CompanyOrgUnitInfo companyInfo =  new CompanyOrgUnitInfo();
//        companyInfo.setId(BOSUuid.read(companyId));
//        companyInfo.setNumber("MS3101WLMZ001@02");
//        companyInfo.setName("上海美维口腔门诊部有限公司-副账簿");
        
        CompanyOrgUnitCollection coll = new CompanyOrgUnitCollection();
        coll.add(companyInfo);
        cond.setCompany(companyInfo);
        
        CompanyOrgUnitInfo companys[] = new CompanyOrgUnitInfo[1];
        companys[0] = companyInfo;
        
        cond.setSelectedCompanys(companys);
        cond.setCompanys(companys);

        IUIWindow ui = UIFactory.createUIFactory("com.kingdee.eas.base.uiframe.client.UINewTabFactory").create(uiClassName, getLinkedContext(cond), null);
        ui.show();
      }
      catch (Exception ex)
      {
        handUIException(ex);
      }
      finally
      {
        setCursorOfDefault();
      }
    }
    
    protected UIContext getLinkedContext(GLRptBaseCondition cond)
    {
      UIContext uiContext = new UIContext(this);
      uiContext.put("JoinQuery", Boolean.TRUE);
      uiContext.put("UIClassParam", cond.toMap().toString());
      uiContext.put("com.kingdee.eas.framework.report.client.CommRptBaseUI#UICONTEXT_KEY_JOINQUERY_PARAMS", cond);
      return uiContext;
    }
    
    private void setLinkedCompany(GLRptSubLedgerCondition cond)
      throws EASBizException, BOSException
    {
      if (cond.getCompany() == null) {
        if (cond.getCompanyId() != null) {
          cond.switchCompany(GlUtils.getCompanyById(cond.getCompanyId()), true);
        } else {
          cond.switchCompany(getCurrCompany(), true);
        }
      }
      setJoinCompany(cond);
    }
    
    protected void setLinkedAccount(GLRptSubLedgerCondition cond)
      throws Exception
    {
      if (getTable().getSelectManager().get() != null)
      {
        int i = KDTableUtil.getSelectedTop(getTable());
        if (i != -1)
        {
          IRow row = getTable().getRow(i);
          String accountId = (String)getTable().getCell(i, "FAccountID").getValue();
          if (StringUtils.isEmpty(accountId)) {
            return;
          }
          AccountViewInfo account = AccountViewFactory.getRemoteInstance().getAccountViewInfo(new ObjectUuidPK(accountId));
          if (cond.getAccountIdSet() != null)
          {
            Set accountIdSet = new HashSet();
            accountIdSet.add(accountId);
            cond.setAccountIdSet(accountIdSet);
          }
          if (!getCurrCompany().isIsBizUnit()) {
            cond.setAccountIdSet(null);
          }
          cond.setAccountCodeStart(account.getNumber());
          cond.setAccountCodeEnd(account.getNumber());
          if (cond.isShowLeafAccount())
          {
            cond.setAccountLevelStart(account.getLevel());
            cond.setAccountLevelEnd(account.getLevel());
          }
        }
      }
    }
    
    protected void setLinkedCurrency(GLRptSubLedgerCondition cond)
      throws Exception
    {
      if ((cond.isAllCurrency()) && (getTable().getSelectManager().get() != null))
      {
        int i = KDTableUtil.getSelectedTop(getTable());
        if (i != -1)
        {
          IRow row = getTable().getRow(i); 
          String strCurrencyid = (String)row.getCell("FCurrencyID").getValue();
          if (StringUtils.isEmpty(strCurrencyid)) {
            return;
          }
          cond.setCurrencyID(strCurrencyid); 
          CurrencyInfo currency = CurrencyFactory.getRemoteInstance().getCurrencyInfo(new ObjectUuidPK(strCurrencyid));
          cond.setCurrencyName(currency.getName());
        }
      }
    }
    
    protected void setLinkedAsstactList(GLRptSubLedgerCondition cond)
      throws CloneNotSupportedException, BOSException, EASBizException
    {
    	GLRptAccountBalanceCondition oldCond = (GLRptAccountBalanceCondition)getQueryCond();
      if (!oldCond.getDisplayAsstDetail())
      {
        cond.setShowDisplayAsstDetail(false);
        cond.setAsstActList(new ArrayList());
        return;
      }
      List asstActList = GLRptUtils.getasstActList(getTableForPrint(), "FAssistGrpId");
      if (asstActList == null)
      {
        cond.setShowDisplayAsstDetail(false);
        cond.setAsstActList(new ArrayList());
        return;
      }
      cond.setAsstActList(asstActList);
      cond.setShowDisplayAsstDetail(true);
      cond.setSumByAsstact(true);
    }
    
    public void onLoad()
      throws Exception
    {
      super.onLoad();
      this.actionAssistAccount.setEnabled(false);
      this.actionSubsidiaryLedger.setEnabled(false);
    }
    
    protected String getPermissionItem()
    {
      return "gl_balanceList_view";
    }

}