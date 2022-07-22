package com.kingdee.eas.custom.app.dao;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.metadata.entity.FilterInfo;
import com.kingdee.bos.metadata.entity.FilterItemInfo;
import com.kingdee.bos.metadata.entity.SelectorItemInfo;
import com.kingdee.bos.metadata.entity.SorterItemInfo;
import com.kingdee.bos.metadata.query.util.CompareType;
import com.kingdee.eas.basedata.assistant.CurrencyInfo;
import com.kingdee.eas.basedata.assistant.PeriodInfo;
import com.kingdee.eas.basedata.assistant.SystemStatusCtrolUtils;
import com.kingdee.eas.basedata.master.account.AccountViewCollection;
import com.kingdee.eas.basedata.master.account.AccountViewFactory;
import com.kingdee.eas.basedata.master.account.AccountViewInfo;
import com.kingdee.eas.basedata.master.auxacct.AssistantHGInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.fi.gl.GlUtils;
import com.kingdee.eas.fi.gl.app.GLRptHelper;
import com.kingdee.eas.fi.gl.app.GLTempTableUtil;
import com.kingdee.eas.fi.gl.common.SQLUtil;
import com.kingdee.eas.fi.gl.rpt.CompanyDisplayModeEnum;
import com.kingdee.eas.fi.gl.rpt.GLRptAccountBalanceCondition;
import com.kingdee.eas.fi.gl.rpt.GLRptRowSet;
import com.kingdee.eas.fi.gl.rpt.GLRptTableHeadInfo;
import com.kingdee.eas.fi.gl.rpt.GLRptTreeNode;
import com.kingdee.eas.fi.gl.rpt.IGLRptRowData;
import com.kingdee.eas.fi.gl.rpt.app.DefaultGLRptIDCreator;
import com.kingdee.eas.fi.gl.rpt.app.GLRptAccountBalanceIDCreator;
import com.kingdee.eas.fi.gl.rpt.app.IGLRptIDCreator;
import com.kingdee.eas.fi.gl.rpt.app.dao.AbstractGLRptDao;
import com.kingdee.eas.fi.gl.rpt.app.dao.GLRptAccountBalPageQuery;
import com.kingdee.eas.fi.gl.rpt.app.dao.GLRptAccountBalQueryHelper;
import com.kingdee.eas.fi.gl.rpt.app.dao.GLRptAccountFilter;
import com.kingdee.eas.fi.gl.rpt.app.dao.GLRptDaoUtil;
import com.kingdee.eas.fi.gl.rpt.app.dao.GLRptField;
import com.kingdee.eas.fi.gl.rpt.app.dao.GLRptPageQuery;
import com.kingdee.eas.fi.gl.rpt.app.dao.GLRptSelector;
import com.kingdee.eas.framework.SystemEnum;
import com.kingdee.eas.util.app.DbUtil;
import com.kingdee.jdbc.rowset.IRowSet;
import com.kingdee.util.StringUtils;
public class GLRptCompositeDao extends AbstractGLRptDao {
	  
	 private static Logger logger = Logger.getLogger("com.kingdee.eas.custom.app.GLRptCompAccountBalanceDao");
	 protected GLRptAccountBalanceCondition cond;
	  private Set accountIdSet4Sum;
	  private String accountIdStr4Sum;
	  private String accountTempTable4Sum;
	  private int assistCount = -1;
	  private String sysStatusTmpTable;
	  String plVoucherTempTable;
	  private Map<String, Integer> actTypeMap;
	  private boolean isYearQuery = false;
	  
	  public GLRptCompositeDao(Context ctx)
	    throws EASBizException, BOSException
	  {
	    super(ctx);
	    if (this.actTypeMap == null) {
	      this.actTypeMap = GLRptHelper.initAcctTypeMap(ctx);
	    }
	  }
	  
	  protected void init(EntityViewInfo ev)
	    throws EASBizException, BOSException
	  {
	    super.init(ev);
	    this.cond = ((GLRptAccountBalanceCondition)getQueryCondition());
	    if (this.cond.getDisplayAsstDetail()) {}
	  }
	  
	  public void buildData()
	    throws BOSException, EASBizException
	  {
	    insertData();
	  }
	  
	  protected void insertData()
	    throws BOSException, EASBizException
	  {
	    if (this.cond.isNotIncluePLVoucher()) {
	      createPLVoucherTempTable();
	    }
	    if ((this.cond.getPeriodYearStart() == this.cond.getPeriodYearEnd()) && (this.cond.getPeriodNumberStart() == 1))
	    {
	      this.cond.setPeriodNumberStart(this.cond.getPeriodNumberEnd());
	      this.isYearQuery = true;
	    }
	    if (isCreateDataTempTable()) {
	      this.sysStatusTmpTable = GLRptDaoUtil.createSysStatusTmpTable(getContex(), this.cond);
	    }
	    Set<String> moreBeginPeriodCompanyIds = GLRptDaoUtil.getMoreBeginPeriodCompanyIds(getContex(), this.cond.getCompanyIdsStr(), this.cond.getPeriodYearStart(), this.cond.getPeriodNumberStart());
	    if (!moreBeginPeriodCompanyIds.isEmpty()) {
	      insertAccountBalance(GLRptDaoUtil.getIdFilter(getContex(), moreBeginPeriodCompanyIds), this.cond.getPeriodYearStart(), this.cond.getPeriodNumberStart());
	    }
	    Map<Integer, Set<String>> lessBeginPeriodCompanyIds = GLRptDaoUtil.getLessBeginPeriodCompanyIds(getContex(), this.cond.getCompanyIdsStr(), this.cond.getPeriodYearStart(), this.cond.getPeriodNumberStart());
	    Iterator<Integer> it = lessBeginPeriodCompanyIds.keySet().iterator();
	    while (it.hasNext())
	    {
	      int period = ((Integer)it.next()).intValue();
	      Set<String> comapnyIds = (Set)lessBeginPeriodCompanyIds.get(Integer.valueOf(period));
	      insertAccountBalance(GLRptDaoUtil.getIdFilter(getContex(), comapnyIds), period / 100, period % 100);
	    }
	    if (this.cond.getIncludeBWAccount()) {
	      insertAccountSum(true);
	    }
	    insertAccountSum(false);
	    if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	      insertCompanySum();
	    }
	    if (isAllCurrency()) {
	      insertCurrencySum();
	    }
	    if (this.cond.getDisplayAsstDetail())
	    {
	      if (!moreBeginPeriodCompanyIds.isEmpty()) {
	        insertAssistBalance(moreBeginPeriodCompanyIds, this.cond.getPeriodYearStart(), this.cond.getPeriodNumberStart());
	      }
	      it = lessBeginPeriodCompanyIds.keySet().iterator();
	      while (it.hasNext())
	      {
	        int period = ((Integer)it.next()).intValue();
	        Set<String> comapnyIds = (Set)lessBeginPeriodCompanyIds.get(Integer.valueOf(period));
	        insertAssistBalance(comapnyIds, period / 100, period % 100);
	      }
	      this.assistCount = getAssistColumnCount();
	    }
	  }
	  
	  protected int getAssistColumnCount()
	    throws BOSException, EASBizException
	  {
	    List sp = new ArrayList();
	    StringBuffer sql = new StringBuffer();
	    sql.append(" select max(asst.fcount) fcount");
	    sql.append(" from t_bd_accountview av");
	    sql.append(" inner join t_bd_asstaccount asst on asst.fid = av.fcaa");
	    sql.append(" where av.fisleaf = 1");
	    sql.append(" and av.faccounttableid = ?");
	    sp.add(this.cond.getAccountTableId());
	    if (CompanyDisplayModeEnum.enumerate == this.cond.getCompanyDisplayMode())
	    {
	      sql.append(" and av.fcompanyid = ? ");
	      sp.add(getCompany().getId().toString());
	    }
	    else
	    {
	      sql.append(" and av.fcompanyid in ").append(this.cond.getCompanyIdsStr());
	    }
	    if (!this.cond.getIncludeBWAccount()) {
	      sql.append(" and av.FBW = 0 ");
	    }
	    if (!this.cond.isShowLeafAccount())
	    {
	      sql.append(" and av.flevel <= ? ");
	      sp.add(Integer.valueOf(this.cond.getAccountLevelEnd()));
	    }
	    String accountFilter = new GLRptAccountFilter(getContex(), this.cond, "av", "av.fid", "av.fcompanyid", false, true, this.cond.getCompanyIdsStr()).getSQL();
	    if (accountFilter != null) {
	      sql.append(" and ").append(accountFilter);
	    }
	    sql.append(" and ").append("av.fid in ( select faccountid from ").append(getDataSourceTableName()).append(" )");
	    
	    IRowSet result = DbUtil.executeQuery(getContex(), sql.toString(), sp.toArray());
	    int assCount = 0;
	    try
	    {
	      if (result.next()) {
	        assCount = result.getInt("fcount");
	      }
	    }
	    catch (SQLException e)
	    {
	      throw new BOSException("SQL ERROR!", e);
	    }
	    return assCount;
	  }
	  
	  protected void toGLRptRowData(IRowSet rs, GLRptRowSet rowData)
	    throws SQLException, BOSException, EASBizException
	  {
	    super.toGLRptRowData(rs, rowData);
	    if (this.cond.getDisplayAsstDetail()) {
	      setAsstName(rs, rowData);
	    }
	    setBalance(rs, rowData);
	    if (isAccounTotal(rs)) {
	      setAcountSumAmount(getAccountSumAmount(rs), rowData);
	    } else {
	      setYearBegin(rs, rowData);
	    }
	  }
	  
	  protected void doGLRptRowData(GLRptRowSet rowSet)
	    throws EASBizException, BOSException
	  {
	    super.doGLRptRowData(rowSet);
	    GLRptTreeNode node = null;
	    if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode())
	    {
	      node = (GLRptTreeNode)rowSet.getObject("companyName");
	      if ((this.cond.getDisplayAsstDetail()) && (rowSet.getInt("FIsAssist") == 1)) {
	        node.setShow(false);
	      }
	    }
	    if ((CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) && (node.getParentNode() != null)) {
	      rowSet.updateObject(GLRptField.currencyName.getName(), null);
	    }
	    if (((this.cond.getDisplayAsstDetail()) && (rowSet.getInt("FIsAssist") == 1)) || ((CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) && (node.getParentNode() != null)))
	    {
	      rowSet.updateObject(GLRptField.accountName.getName(), null);
	      rowSet.updateObject(GLRptField.accountNumber.getName(), null);
	    }
	  }
	  
	  private void setAsstName(IRowSet rs, IGLRptRowData rowData)
	    throws SQLException, EASBizException, BOSException
	  {
	    String asstNameL = rs.getString(GLRptField.HGName.getName());
	    String asstNameD = rs.getString("FdisplaynameGroup");
	    String asstNameN = rs.getString("FNumberGroup");
	    AssistantHGInfo hg = null;
	    boolean onlyOne = true;
	    if ((asstNameL != null) && (asstNameL.trim().length() > 0))
	    {
	      asstNameL = asstNameL.substring(0, asstNameL.length() - 1);
	      asstNameD = asstNameD.substring(0, asstNameD.length() - 1);
	      asstNameN = asstNameN.substring(0, asstNameN.length() - 1);
	      String[] strAsstNameL = asstNameL.split(";");
	      String[] strAsstNameD = asstNameD.split(";");
	      String[] strAsstNameN = asstNameN.split(";");
	      String number = null;
	      int acctTypeFlag = 0;
	      for (int i = 0; i < strAsstNameL.length; i++)
	      {
	        if (i >= this.assistCount) {
	          break;
	        }
	        if (strAsstNameL[i] != null)
	        {
	          number = strAsstNameN[i].substring(0, strAsstNameN[i].indexOf("_!"));
	          if (this.actTypeMap.get(number) != null) {
	            acctTypeFlag = ((Integer)this.actTypeMap.get(number)).intValue();
	          }
	          String realName = GLRptHelper.getAssDisplayName(this.cond.getRptParams().isAssitemShowNumber(), this.cond.getRptParams().isShowLongNameForGeneral(), this.cond.getRptParams().isShowLongNumberForGeneral(), acctTypeFlag, strAsstNameL[i], strAsstNameD[i], strAsstNameN[i]);
	          if ((realName.equals("Error")) && (onlyOne))
	          {
	            onlyOne = false;
	            String hgid = rs.getString("FAssistGrpId");
	            String[] result = GLRptHelper.rePairHg(getContex(), hgid);
	            strAsstNameL = result[0].split(";");
	            strAsstNameD = result[1].split(";");
	            strAsstNameN = result[2].split(";");
	            realName = GLRptHelper.getAssDisplayName(this.cond.getRptParams().isAssitemShowNumber(), this.cond.getRptParams().isShowLongNameForGeneral(), this.cond.getRptParams().isShowLongNumberForGeneral(), acctTypeFlag, strAsstNameL[i], strAsstNameD[i], strAsstNameN[i]);
	          }
	          rowData.updateString("FAsstAccountName" + i, realName);
	        }
	      }
	    }
	  }
	  
	  private void setBalance(IRowSet rs, IGLRptRowData rowData)
	    throws SQLException, EASBizException, BOSException
	  {
	    BigDecimal endQty = null;
	    BigDecimal beginQty = null;
	    if (isShowQty())
	    {
	      endQty = rs.getBigDecimal("FEndQty");
	      if (endQty == null) {
	        endQty = GlUtils.zero;
	      }
	      if (this.isYearQuery)
	      {
	        BigDecimal yearDebitQty = rs.getBigDecimal("FYearDebitQty");
	        BigDecimal yearCreditQty = rs.getBigDecimal("FYearCreditQty");
	        if (yearDebitQty == null) {
	          yearDebitQty = GlUtils.zero;
	        }
	        if (yearCreditQty == null) {
	          yearCreditQty = GlUtils.zero;
	        }
	        rowData.updateBigDecimal("FDebitQty", yearDebitQty);
	        rowData.updateBigDecimal("FCreditQty", yearCreditQty);
	        beginQty = endQty.add(yearCreditQty).subtract(yearDebitQty);
	      }
	      else
	      {
	        beginQty = rs.getBigDecimal("FBeginQty");
	      }
	      if (beginQty == null) {
	        beginQty = GlUtils.zero;
	      }
	    }
	    BigDecimal endBalFor = null;
	    BigDecimal beginBalFor = null;
	    if (isShowFor())
	    {
	      endBalFor = rs.getBigDecimal("FEndBalanceFor");
	      if (endBalFor == null) {
	        endBalFor = GlUtils.zero;
	      }
	      if (this.isYearQuery)
	      {
	        BigDecimal yearDebitBalFor = rs.getBigDecimal("FYearDebitFor");
	        BigDecimal yearCreditBalFor = rs.getBigDecimal("FYearCreditFor");
	        if (yearDebitBalFor == null) {
	          yearDebitBalFor = GlUtils.zero;
	        }
	        if (yearCreditBalFor == null) {
	          yearCreditBalFor = GlUtils.zero;
	        }
	        rowData.updateBigDecimal("FDebitFor", yearDebitBalFor);
	        rowData.updateBigDecimal("FCreditFor", yearCreditBalFor);
	        beginBalFor = endBalFor.add(yearCreditBalFor).subtract(yearDebitBalFor);
	      }
	      else
	      {
	        beginBalFor = rs.getBigDecimal("FBeginBalanceFor");
	      }
	      if (beginBalFor == null) {
	        beginBalFor = GlUtils.zero;
	      }
	    }
	    BigDecimal endBalLocal = null;
	    BigDecimal beginBalLocal = null;
	    if (isShowLocal())
	    {
	      endBalLocal = rs.getBigDecimal("FEndBalanceLocal");
	      if (endBalLocal == null) {
	        endBalLocal = GlUtils.zero;
	      }
	      if (this.isYearQuery)
	      {
	        BigDecimal yearDebitBalLocal = rs.getBigDecimal("FYearDebitLocal");
	        BigDecimal yearCreditBalLocal = rs.getBigDecimal("FYearCreditLocal");
	        if (yearDebitBalLocal == null) {
	          yearDebitBalLocal = GlUtils.zero;
	        }
	        if (yearCreditBalLocal == null) {
	          yearCreditBalLocal = GlUtils.zero;
	        }
	        rowData.updateBigDecimal("FDebitLocal", yearDebitBalLocal);
	        rowData.updateBigDecimal("FCreditLocal", yearCreditBalLocal);
	        beginBalLocal = endBalLocal.add(yearCreditBalLocal).subtract(yearDebitBalLocal);
	      }
	      else
	      {
	        beginBalLocal = rs.getBigDecimal("FBeginBalanceLocal");
	      }
	      if (beginBalLocal == null) {
	        beginBalLocal = GlUtils.zero;
	      }
	    }
	    BigDecimal endBalRpt = null;
	    BigDecimal beginBalRpt = null;
	    if (isShowRpt())
	    {
	      endBalRpt = rs.getBigDecimal("FEndBalanceRpt");
	      if (endBalRpt == null) {
	        endBalRpt = GlUtils.zero;
	      }
	      if (this.isYearQuery)
	      {
	        BigDecimal yearDebitBalRpt = rs.getBigDecimal("FYearDebitRpt");
	        BigDecimal yearCreditBalRpt = rs.getBigDecimal("FYearCreditRpt");
	        if (yearDebitBalRpt == null) {
	          yearDebitBalRpt = GlUtils.zero;
	        }
	        if (yearCreditBalRpt == null) {
	          yearCreditBalRpt = GlUtils.zero;
	        }
	        rowData.updateBigDecimal("FDebitRpt", yearDebitBalRpt);
	        rowData.updateBigDecimal("FCreditRpt", yearCreditBalRpt);
	        beginBalRpt = endBalRpt.add(yearCreditBalRpt).subtract(yearDebitBalRpt);
	      }
	      else
	      {
	        beginBalRpt = rs.getBigDecimal("FBeginBalanceRpt");
	      }
	      if (beginBalRpt == null) {
	        beginBalRpt = GlUtils.zero;
	      }
	    }
	    boolean isEndDebit = isDebit(rs, endBalFor, endBalLocal, endBalRpt);
	    boolean isBeginDebit = isDebit(rs, beginBalFor, beginBalLocal, beginBalRpt);
	    if (isShowQty())
	    {
	      if (isEndDebit) {
	        rowData.updateBigDecimal("FEndDebitQty", endQty);
	      } else {
	        rowData.updateBigDecimal("FEndCreditQty", endQty.negate());
	      }
	      if (isBeginDebit) {
	        rowData.updateBigDecimal("FBeginDebitQty", beginQty);
	      } else {
	        rowData.updateBigDecimal("FBeginCreditQty", beginQty.negate());
	      }
	    }
	    if (isShowFor())
	    {
	      if (endBalFor.doubleValue() != 0.0D) {
	        if (isEndDebit) {
	          rowData.updateBigDecimal("FEndDebitBalanceFor", endBalFor);
	        } else {
	          rowData.updateBigDecimal("FEndCreditBalanceFor", endBalFor.negate());
	        }
	      }
	      if (beginBalFor.doubleValue() != 0.0D) {
	        if (isBeginDebit) {
	          rowData.updateBigDecimal("FBeginDebitBalanceFor", beginBalFor);
	        } else {
	          rowData.updateBigDecimal("FBeginCreditBalanceFor", beginBalFor.negate());
	        }
	      }
	    }
	    if (isShowLocal())
	    {
	      if (endBalLocal.doubleValue() != 0.0D) {
	        if (isEndDebit) {
	          rowData.updateBigDecimal("FEndDebitBalanceLocal", endBalLocal);
	        } else {
	          rowData.updateBigDecimal("FEndCreditBalanceLocal", endBalLocal.negate());
	        }
	      }
	      if (beginBalLocal.doubleValue() != 0.0D) {
	        if (isBeginDebit) {
	          rowData.updateBigDecimal("FBeginDebitBalanceLocal", beginBalLocal);
	        } else {
	          rowData.updateBigDecimal("FBeginCreditBalanceLocal", beginBalLocal.negate());
	        }
	      }
	    }
	    if (isShowRpt())
	    {
	      if (endBalRpt.doubleValue() != 0.0D) {
	        if (isEndDebit) {
	          rowData.updateBigDecimal("FEndDebitBalanceRpt", endBalRpt);
	        } else {
	          rowData.updateBigDecimal("FEndCreditBalanceRpt", endBalRpt.negate());
	        }
	      }
	      if (beginBalRpt.doubleValue() != 0.0D) {
	        if (isBeginDebit) {
	          rowData.updateBigDecimal("FBeginDebitBalanceRpt", beginBalRpt);
	        } else {
	          rowData.updateBigDecimal("FBeginCreditBalanceRpt", beginBalRpt.negate());
	        }
	      }
	    }
	  }
	  
	  private boolean isDebit(IRowSet rs, BigDecimal balanceFor, BigDecimal balanceLocal, BigDecimal balanceRpt)
	    throws SQLException, EASBizException, BOSException
	  {
	    Object dc = rs.getObject(GLRptField.accountDC.getName());
	    boolean isSameDC = (dc != null) && (this.cond.getRptParams().isBalanceDirSamAsAccount());
	    boolean isDebit = true;
	    if (isSameDC)
	    {
	      isDebit = new BigDecimal(dc.toString()).intValue() == 1;
	    }
	    else
	    {
	      BigDecimal amount = GlUtils.zero;
	      if (isShowFor()) {
	        amount = balanceFor;
	      }
	      if (((amount == null) || (amount.doubleValue() == 0.0D)) && (isShowLocal())) {
	        amount = balanceLocal;
	      }
	      if (((amount == null) || (amount.doubleValue() == 0.0D)) && (isShowRpt())) {
	        amount = balanceRpt;
	      }
	      isDebit = (amount == null) || (amount.doubleValue() >= 0.0D);
	    }
	    return isDebit;
	  }
	  
	  private void appendAmountSumSelect(StringBuffer sql, String currencyId, String companyId, boolean isBalanceDirSamAsAccount)
	    throws EASBizException, BOSException
	  {
	    if (isShowQty()) {
	      if (isNeedQty(companyId)) {
	        appendYearBeginDebitSum(sql, "Qty", "Qty", isBalanceDirSamAsAccount);
	      } else {
	        sql.append(",0.0 FYearBeginDebitQty ");
	      }
	    }
	    if (isShowFor()) {
	      if ((!isAllCurrency()) || (currencyId != null)) {
	        appendYearBeginDebitSum(sql, "For", "BalanceFor", isBalanceDirSamAsAccount);
	      } else {
	        sql.append(", 0.0  FYearBeginDebitBalanceFor ");
	      }
	    }
	    if (isShowLocal()) {
	      appendYearBeginDebitSum(sql, "Local", "BalanceLocal", isBalanceDirSamAsAccount);
	    }
	    if (isShowRpt()) {
	      appendYearBeginDebitSum(sql, "Rpt", "BalanceRpt", isBalanceDirSamAsAccount);
	    }
	    if (isShowQty()) {
	      if (isNeedQty(companyId)) {
	        appendYearBeginCreditSum(sql, "Qty", "Qty", isBalanceDirSamAsAccount);
	      } else {
	        sql.append(", 0.0 FYearBeginCreditQty ");
	      }
	    }
	    if (isShowFor()) {
	      if ((!isAllCurrency()) || (currencyId != null)) {
	        appendYearBeginCreditSum(sql, "For", "BalanceFor", isBalanceDirSamAsAccount);
	      } else {
	        sql.append(", 0.0  FYearBeginCreditBalanceFor ");
	      }
	    }
	    if (isShowLocal()) {
	      appendYearBeginCreditSum(sql, "Local", "BalanceLocal", isBalanceDirSamAsAccount);
	    }
	    if (isShowRpt()) {
	      appendYearBeginCreditSum(sql, "Rpt", "BalanceRpt", isBalanceDirSamAsAccount);
	    }
	    if (isShowQty()) {
	      if (isNeedQty(companyId)) {
	        appendPeriodBeginDebitSum(sql, "Qty", "Qty", isBalanceDirSamAsAccount);
	      } else {
	        sql.append(", 0.0 FBeginDebitQty ");
	      }
	    }
	    if (isShowFor()) {
	      if ((!isAllCurrency()) || (currencyId != null)) {
	        appendPeriodBeginDebitSum(sql, "For", "BalanceFor", isBalanceDirSamAsAccount);
	      } else {
	        sql.append(", 0.0  FBeginDebitBalanceFor ");
	      }
	    }
	    if (isShowLocal()) {
	      appendPeriodBeginDebitSum(sql, "Local", "BalanceLocal", isBalanceDirSamAsAccount);
	    }
	    if (isShowRpt()) {
	      appendPeriodBeginDebitSum(sql, "Rpt", "BalanceRpt", isBalanceDirSamAsAccount);
	    }
	    if (isShowQty()) {
	      if (isNeedQty(companyId)) {
	        appendPeriodBeginCreditSum(sql, "Qty", "Qty", isBalanceDirSamAsAccount);
	      } else {
	        sql.append(", 0.0 FBeginCreditQty ");
	      }
	    }
	    if (isShowFor()) {
	      if ((!isAllCurrency()) || (currencyId != null)) {
	        appendPeriodBeginCreditSum(sql, "For", "BalanceFor", isBalanceDirSamAsAccount);
	      } else {
	        sql.append(", 0.0  FBeginCreditBalanceFor ");
	      }
	    }
	    if (isShowLocal()) {
	      appendPeriodBeginCreditSum(sql, "Local", "BalanceLocal", isBalanceDirSamAsAccount);
	    }
	    if (isShowRpt()) {
	      appendPeriodBeginCreditSum(sql, "Rpt", "BalanceRpt", isBalanceDirSamAsAccount);
	    }
	    if (isShowQty()) {
	      if (isNeedQty(companyId)) {
	        sql.append(",sum(case when av.FIsQty = 1 then TB.").append(this.isYearQuery ? "FYearDebit" : "FDebit").append("Qty*1.0000/ISNULL(MU.FCoefficient,1) else 0.00 end) FDebitQty");
	      } else {
	        sql.append(", 0.0 FDebitQty ");
	      }
	    }
	    if (isShowFor()) {
	      if ((!isAllCurrency()) || (currencyId != null)) {
	        sql.append(",sum(TB.").append(this.isYearQuery ? "FYearDebit" : "FDebit").append("For) FDebitFor");
	      } else {
	        sql.append(", 0.0  FDebitFor ");
	      }
	    }
	    if (isShowLocal()) {
	      sql.append(",sum(TB.").append(this.isYearQuery ? "FYearDebit" : "FDebit").append("Local) FDebitLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(",sum(TB.").append(this.isYearQuery ? "FYearDebit" : "FDebit").append("Rpt) FDebitRpt");
	    }
	    if (isShowQty()) {
	      if (isNeedQty(companyId)) {
	        sql.append(",sum(case when av.FIsQty = 1 then TB.").append(this.isYearQuery ? "FYearCredit" : "FCredit").append("Qty*1.0000/ISNULL(MU.FCoefficient,1) else 0.00 end) FCreditQty");
	      } else {
	        sql.append(", 0.0 FCreditQty ");
	      }
	    }
	    if (isShowFor()) {
	      if ((!isAllCurrency()) || (currencyId != null)) {
	        sql.append(",sum(TB.").append(this.isYearQuery ? "FYearCredit" : "FCredit").append("For) FCreditFor");
	      } else {
	        sql.append(", 0.0  FCreditFor ");
	      }
	    }
	    if (isShowLocal()) {
	      sql.append(",sum(TB.").append(this.isYearQuery ? "FYearCredit" : "FCredit").append("Local) FCreditLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(",sum(TB.").append(this.isYearQuery ? "FYearCredit" : "FCredit").append("Rpt) FCreditRpt");
	    }
	    if (isShowQty()) {
	      if (isNeedQty(companyId)) {
	        sql.append(",sum(case when av.FIsQty = 1 then TB.FYearDebitQty*1.0000/ISNULL(MU.FCoefficient,1) else 0.00 end) FYearDebitQty");
	      } else {
	        sql.append(", 0.0 FYearDebitQty ");
	      }
	    }
	    if (isShowFor()) {
	      if ((!isAllCurrency()) || (currencyId != null)) {
	        sql.append(",sum(TB.FYearDebitFor) FYearDebitFor");
	      } else {
	        sql.append(", 0.0  FYearDebitFor ");
	      }
	    }
	    if (isShowLocal()) {
	      sql.append(",sum(TB.FYearDebitLocal) FYearDebitLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(",sum(TB.FYearDebitRpt) FYearDebitRpt");
	    }
	    if (isShowQty()) {
	      if (isNeedQty(companyId)) {
	        sql.append(",sum(case when av.FIsQty = 1 then TB.FYearCreditQty*1.0000/ISNULL(MU.FCoefficient,1) else 0.00 end) FYearCreditQty");
	      } else {
	        sql.append(", 0.0 FYearCreditQty ");
	      }
	    }
	    if (isShowFor()) {
	      if ((!isAllCurrency()) || (currencyId != null)) {
	        sql.append(",sum(TB.FYearCreditFor) FYearCreditFor");
	      } else {
	        sql.append(", 0.0  FYearCreditFor ");
	      }
	    }
	    if (isShowLocal()) {
	      sql.append(",sum(TB.FYearCreditLocal) FYearCreditLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(",sum(TB.FYearCreditRpt) FYearCreditRpt");
	    }
	    if (isShowQty()) {
	      if (isNeedQty(companyId)) {
	        appendPeriodEndDebitSum(sql, "Qty", "Qty", isBalanceDirSamAsAccount);
	      } else {
	        sql.append(", 0.0 FEndDebitQty ");
	      }
	    }
	    if (isShowFor()) {
	      if ((!isAllCurrency()) || (currencyId != null)) {
	        appendPeriodEndDebitSum(sql, "For", "BalanceFor", isBalanceDirSamAsAccount);
	      } else {
	        sql.append(", 0.0 FEndDebitBalanceFor ");
	      }
	    }
	    if (isShowLocal()) {
	      appendPeriodEndDebitSum(sql, "Local", "BalanceLocal", isBalanceDirSamAsAccount);
	    }
	    if (isShowRpt()) {
	      appendPeriodEndDebitSum(sql, "Rpt", "BalanceRpt", isBalanceDirSamAsAccount);
	    }
	    if (isShowQty()) {
	      if (isNeedQty(companyId)) {
	        appendPeriodEndCreditSum(sql, "Qty", "Qty", isBalanceDirSamAsAccount);
	      } else {
	        sql.append(", 0.0 FEndCreditQty ");
	      }
	    }
	    if (isShowFor()) {
	      if ((!isAllCurrency()) || (currencyId != null)) {
	        appendPeriodEndCreditSum(sql, "For", "BalanceFor", isBalanceDirSamAsAccount);
	      } else {
	        sql.append(", 0.0 FEndCreditBalanceFor ");
	      }
	    }
	    if (isShowLocal()) {
	      appendPeriodEndCreditSum(sql, "Local", "BalanceLocal", isBalanceDirSamAsAccount);
	    }
	    if (isShowRpt()) {
	      appendPeriodEndCreditSum(sql, "Rpt", "BalanceRpt", isBalanceDirSamAsAccount);
	    }
	  }
	  
	  private void appendYearBeginDebitSum(StringBuffer sql, String type, String suff, boolean isBalanceDirSamAsAccount)
	    throws EASBizException, BOSException
	  {
	    sql.append(", sum(CASE WHEN ");
	    if (type.equals("Qty")) {
	      sql.append("av.FIsQty = 1 and ");
	    }
	    if (isBalanceDirSamAsAccount) {
	      sql.append(" av.fdc = ").append(1);
	    } else {
	      sql.append(" TB.FEnd" + suff + "+TB.FYearCredit" + type + "-TB.FYearDebit" + type + " > 0 ");
	    }
	    sql.append(" THEN (TB.FEnd" + suff + "+TB.FYearCredit" + type + "-TB.FYearDebit" + type).append(")");
	    if (type.equals("Qty")) {
	      sql.append("*1.0000/ISNULL(MU.FCoefficient,1)");
	    }
	    sql.append(" ELSE 0.0 END) FYearBeginDebit" + suff + " ");
	  }
	  
	  private void appendYearBeginCreditSum(StringBuffer sql, String type, String suff, boolean isBalanceDirSamAsAccount)
	    throws EASBizException, BOSException
	  {
	    sql.append(", sum(CASE WHEN ");
	    if (type.equals("Qty")) {
	      sql.append("av.FIsQty = 1 and ");
	    }
	    if (isBalanceDirSamAsAccount) {
	      sql.append(" av.fdc = ").append(-1);
	    } else {
	      sql.append(" TB.FEnd" + suff + "+TB.FYearCredit" + type + "-TB.FYearDebit" + type + " < 0 ");
	    }
	    sql.append(" THEN -1 * (TB.FEnd" + suff + "+TB.FYearCredit" + type + "-TB.FYearDebit" + type + ") ");
	    if (type.equals("Qty")) {
	      sql.append("*1.0000/ISNULL(MU.FCoefficient,1)");
	    }
	    sql.append(" ELSE 0.0 END) FYearBeginCredit" + suff + " ");
	  }
	  
	  private void appendPeriodBeginDebitSum(StringBuffer sql, String type, String suff, boolean isBalanceDirSamAsAccount)
	    throws EASBizException, BOSException
	  {
	    String begin = " TB.FBegin" + suff;
	    sql.append(" ,sum(CASE WHEN ");
	    if (type.equals("Qty")) {
	      sql.append("av.FIsQty = 1 and ");
	    }
	    if (isBalanceDirSamAsAccount) {
	      sql.append(" av.fdc = ").append(1);
	    } else {
	      sql.append(begin + " > 0 ");
	    }
	    sql.append(" THEN (").append(begin).append(")");
	    if (type.equals("Qty")) {
	      sql.append("*1.0000/ISNULL(MU.FCoefficient,1)");
	    }
	    sql.append(" ELSE 0.0 END) FBeginDebit" + suff);
	  }
	  
	  private void appendPeriodBeginCreditSum(StringBuffer sql, String type, String suff, boolean isBalanceDirSamAsAccount)
	    throws EASBizException, BOSException
	  {
	    String begin = " TB.FBegin" + suff;
	    sql.append(", sum(CASE WHEN ");
	    if (type.equals("Qty")) {
	      sql.append("av.FIsQty = 1 and ");
	    }
	    if (isBalanceDirSamAsAccount) {
	      sql.append(" av.fdc = ").append(-1);
	    } else {
	      sql.append(begin + " < 0 ");
	    }
	    sql.append(" THEN -1 * (").append(begin).append(")");
	    if (type.equals("Qty")) {
	      sql.append("*1.0000/ISNULL(MU.FCoefficient,1)");
	    }
	    sql.append(" ELSE 0.0 END) FBeginCredit" + suff);
	  }
	  
	  private void appendPeriodEndDebitSum(StringBuffer sql, String type, String suff, boolean isBalanceDirSamAsAccount)
	    throws EASBizException, BOSException
	  {
	    sql.append(", sum(CASE WHEN ");
	    if (type.equals("Qty")) {
	      sql.append("av.FIsQty = 1 and ");
	    }
	    if (isBalanceDirSamAsAccount) {
	      sql.append(" av.fdc = ").append(1);
	    } else {
	      sql.append(" TB.FEnd" + suff + ">0");
	    }
	    sql.append(" THEN TB.FEnd" + suff);
	    if (type.equals("Qty")) {
	      sql.append("*1.0000/ISNULL(MU.FCoefficient,1)");
	    }
	    sql.append(" ELSE 0.0 END) FEndDebit" + suff + " ");
	  }
	  
	  private void appendPeriodEndCreditSum(StringBuffer sql, String type, String suff, boolean isBalanceDirSamAsAccount)
	    throws EASBizException, BOSException
	  {
	    sql.append(", sum(CASE WHEN ");
	    if (type.equals("Qty")) {
	      sql.append("av.FIsQty = 1 and ");
	    }
	    if (isBalanceDirSamAsAccount) {
	      sql.append(" av.fdc = ").append(-1);
	    } else {
	      sql.append(" TB.FEnd" + suff + "<0");
	    }
	    sql.append(" THEN -1*TB.FEnd" + suff);
	    if (type.equals("Qty")) {
	      sql.append("*1.0000/ISNULL(MU.FCoefficient,1)");
	    }
	    sql.append(" ELSE 0.0 END) FEndCredit" + suff + " ");
	  }
	  
	  private IRowSet getAccountSumAmount(IRowSet rs)
	    throws EASBizException, BOSException, SQLException
	  {
	    String companyId = null;
	    if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	      companyId = rs.getString("FOrgUnitID");
	    } else {
	      companyId = getCompany().getId().toString();
	    }
	    String currencyId = null;
	    if (isAllCurrency())
	    {
	      currencyId = rs.getString("FCurrencyID");
	      if ((currencyId != null) && (currencyId.trim().length() == 0)) {
	        currencyId = null;
	      }
	    }
	    if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode())
	    {
	      if ((this.cond.getDisplayAsstDetail()) && (this.cond.getOptionDCDispatchAsst()))
	      {
	        StringBuffer sql = new StringBuffer();
	        sql.append(" select 1 xxx");
	        appendAmountSumSelect(sql, currencyId, companyId, this.cond.isBalanceDirSamAsAccount());
	        sql.append(" FROM ").append(getDataSourceTableName()).append(" TB \r\n");
	        sql.append(" INNER JOIN T_BD_AccountView av on TB.FAccountID = av.FID \r\n");
	        if (isShowQty()) {
	          sql.append(" LEFT OUTER JOIN T_BD_MEASUREUNIT MU on MU.FID = av.FMeasureUnitID  \r\n");
	        }
	        if ((this.cond.getDisplayAsstDetail()) && (this.cond.getOptionDCDispatchAsst()) && ((CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) || (CompanyDisplayModeEnum.enumerate == this.cond.getCompanyDisplayMode())))
	        {
	          sql.append(" WHERE ((av.FCAA IS NULL AND FIsAssist = 0 and (av.fisleaf = 1 or av.flevel = ").append(new Integer(this.cond.getAccountLevelEnd())).append("))");
	          
	          sql.append(" OR (av.FCAA IS not NULL AND FIsAssist = 0 and av.FLevel = ").append(new Integer(this.cond.getAccountLevelEnd())).append(" and av.FIsLeaf = 0) ");
	          
	          sql.append(" OR (av.FCAA IS not NULL AND FIsAssist = 1 )) \r\n");
	        }
	        else
	        {
	          if (this.cond.isShowLeafAccount()) {
	            sql.append(" WHERE ").append(" av.FIsLeaf = 1 ");
	          } else if (this.cond.getAccountLevelEnd() == 1) {
	            sql.append(" WHERE ").append(" av.flevel = 1 ");
	          } else {
	            sql.append(" WHERE av.fid in (").append(getAccountIdFilter4Sum(companyId)).append(")");
	          }
	          if (this.cond.getDisplayAsstDetail()) {
	            sql.append(" and tb.FIsAssist = 0 ");
	          }
	        }
	        if (isAllCurrency()) {
	          sql.append(" and TB.FCurrencyID is not null and TB.FCurrencyID <> ' ' \r\n");
	        }
	        if (companyId.equals("00000000-0000-0000-0000-000000000000CCE7AED4")) {
	          sql.append(" and tb.FOrgUnitID <> '").append("00000000-0000-0000-0000-000000000000CCE7AED4").append("'");
	        } else {
	          sql.append(" and tb.FOrgUnitID = '").append(companyId).append("'");
	        }
	        if (isAllCurrency()) {
	          if ((currencyId != null) && (currencyId.trim().length() > 0)) {
	            sql.append(" and TB.FCurrencyID = '").append(currencyId).append("'");
	          } else {
	            sql.append(" and TB.FCurrencyID != ' '");
	          }
	        }
	        if (this.cond.getIncludeBWAccount())
	        {
	          int bwType = rs.getInt("FBWType");
	          if ((bwType == 0) || (bwType == 1)) {
	            sql.append(" and TB.FBWType = ").append(bwType);
	          }
	        }
	        return executeQuery(sql.toString());
	      }
	      return getAccountAmountForMerger(rs, currencyId, companyId);
	    }
	    StringBuffer sql = new StringBuffer();
	    sql.append(" select 1 xxx");
	    appendAmountSumSelect(sql, currencyId, companyId, this.cond.isBalanceDirSamAsAccount());
	    int bwType = -1;
	    if (this.cond.getIncludeBWAccount()) {
	      bwType = rs.getInt("FBWType");
	    }
	    if (isCreateDataTempTable())
	    {
	      sql.append(" FROM ").append(getDataSourceTableName()).append(" TB ");
	      sql.append(" INNER JOIN T_BD_AccountView av on TB.FAccountID = av.FID ");
	      if (isShowQty()) {
	        sql.append(" LEFT OUTER JOIN T_BD_MEASUREUNIT MU on MU.FID = av.FMeasureUnitID  ");
	      }
	      if ((this.cond.getDisplayAsstDetail()) && (this.cond.getOptionDCDispatchAsst()) && ((CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) || (CompanyDisplayModeEnum.enumerate == this.cond.getCompanyDisplayMode())))
	      {
	        sql.append(" WHERE ((av.FCAA is null AND FIsAssist = 0 and (av.fisleaf = 1 or av.flevel = ").append(new Integer(this.cond.getAccountLevelEnd())).append("))");
	        
	        sql.append(" OR (av.FCAA is not null AND FIsAssist = 0 and av.FLevel = ").append(new Integer(this.cond.getAccountLevelEnd())).append(" and av.FIsLeaf = 0) ");
	        
	        sql.append(" OR (av.FCAA is not null AND FIsAssist = 1 )) ");
	      }
	      else
	      {
	        if (this.cond.isShowLeafAccount()) {
	          sql.append(" WHERE ").append(" av.FIsLeaf = 1 ");
	        } else if (this.cond.getAccountLevelEnd() == 1) {
	          sql.append(" WHERE ").append(" av.flevel = 1 ");
	        } else {
	          sql.append(" WHERE av.fid in (").append(getAccountIdFilter4Sum(companyId)).append(")");
	        }
	        if (this.cond.getDisplayAsstDetail()) {
	          sql.append(" and tb.FIsAssist = 0 ");
	        }
	      }
	      if (isAllCurrency()) {
	        sql.append(" and TB.FCurrencyID is not null and TB.FCurrencyID <> ' ' ");
	      }
	      if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode())
	      {
	        sql.append(" and TB.FOrgUnitID = '").append(companyId).append("'");
	        if (CompanyDisplayModeEnum.merger != this.cond.getCompanyDisplayMode()) {
	          if ((this.cond.getDisplayAsstDetail()) && (this.cond.getOptionDCDispatchAsst()) && ((CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) || (CompanyDisplayModeEnum.enumerate == this.cond.getCompanyDisplayMode()))) {
	            sql.append(" and av.fcompanyid = tb.forgunitid ");
	          }
	        }
	      }
	      if (isAllCurrency()) {
	        if ((currencyId != null) && (currencyId.trim().length() > 0)) {
	          sql.append(" and TB.FCurrencyID = '").append(currencyId).append("'");
	        } else {
	          sql.append(" and TB.FCurrencyID != ' '");
	        }
	      }
	      if ((bwType == 0) || (bwType == 1)) {
	        sql.append(" and TB.FBWType = ").append(bwType);
	      }
	    }
	    else
	    {
	      sql.append(" from (").append(getBalanceSumSQL(false, bwType));
	      if ((this.cond.getDisplayAsstDetail()) && (this.cond.getOptionDCDispatchAsst())) {
	        sql.append(" union all ").append(getBalanceSumSQL(true, bwType));
	      }
	      sql.append(") tb ");
	      sql.append(" left outer join T_BD_AccountView av on tb.FAccountID = av.FID ");
	      if (isShowQty()) {
	        sql.append(" left outer join T_BD_MeasureUnit mu on mu.FID = av.FMeasureUnitID  ");
	      }
	    }
	    return executeQuery(sql.toString());
	  }
	  
	  private String getBalanceSumSQL(boolean isAsstBal, int bwType)
	    throws EASBizException, BOSException
	  {
	    StringBuffer sql = new StringBuffer();
	    sql.append("select t.faccountid");
	    if (isAllCurrency()) {
	      sql.append(", t.fcurrencyid");
	    }
	    GLRptAccountBalQueryHelper helper = getQueryHelper();
	    sql.append(helper.getAmountSelectSQL());
	    sql.append(helper.getFromSQL(isAsstBal, getDataSourceTableName()));
	    sql.append(" left outer join t_bd_accountview av on av.fid = t.faccountid ");
	    sql.append(helper.getWhereSQL());
	    if ((bwType == 0) || (bwType == 1)) {
	      sql.append(" and t.FBWType = ").append(bwType);
	    }
	    if (this.cond.isShowLeafAccount()) {
	      sql.append(" and ").append(" av.FIsLeaf = 1 ");
	    } else if (this.cond.getAccountLevelEnd() == 1) {
	      sql.append(" and ").append(" av.flevel = 1 ");
	    } else {
	      sql.append(" and av.fid in (").append(getAccountIdFilter4Sum(null)).append(")");
	    }
	    if ((!isAsstBal) && (this.cond.getDisplayAsstDetail()) && (this.cond.getOptionDCDispatchAsst()) && ((CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) || (CompanyDisplayModeEnum.enumerate == this.cond.getCompanyDisplayMode())))
	    {
	      sql.append(" and ((av.FCAA is null AND t.FIsAssist = 0 and (av.fisleaf = 1 or av.flevel = ").append(this.cond.getAccountLevelEnd()).append("))");
	      
	      sql.append(" or (av.FCAA is not null AND t.FIsAssist = 0 and av.FLevel = ").append(this.cond.getAccountLevelEnd()).append(" and av.FIsLeaf = 0)) ");
	    }
	    sql.append(" group by t.faccountid");
	    if (isAllCurrency()) {
	      sql.append(", t.fcurrencyid");
	    }
	    return sql.toString();
	  }
	  
	  private void setAcountSumAmount(IRowSet rs, IGLRptRowData rowData)
	    throws BOSException, SQLException, EASBizException
	  {
	    if (!rs.next()) {
	      return;
	    }
	    if (isShowQty())
	    {
	      rowData.updateBigDecimal("FYearBeginDebitQty", rs.getBigDecimal("FYearBeginDebitQty"));
	      rowData.updateBigDecimal("FYearBeginCreditQty", rs.getBigDecimal("FYearBeginCreditQty"));
	      rowData.updateBigDecimal("FBeginDebitQty", rs.getBigDecimal("FBeginDebitQty"));
	      rowData.updateBigDecimal("FBeginCreditQty", rs.getBigDecimal("FBeginCreditQty"));
	      rowData.updateBigDecimal("FDebitQty", rs.getBigDecimal("FDebitQty"));
	      rowData.updateBigDecimal("FCreditQty", rs.getBigDecimal("FCreditQty"));
	      rowData.updateBigDecimal("FYearDebitQty", rs.getBigDecimal("FYearDebitQty"));
	      rowData.updateBigDecimal("FYearCreditQty", rs.getBigDecimal("FYearCreditQty"));
	      rowData.updateBigDecimal("FEndDebitQty", rs.getBigDecimal("FEndDebitQty"));
	      rowData.updateBigDecimal("FEndCreditQty", rs.getBigDecimal("FEndCreditQty"));
	    }
	    if (isShowFor())
	    {
	      rowData.updateBigDecimal("FYearBeginDebitBalanceFor", rs.getBigDecimal("FYearBeginDebitBalanceFor"));
	      rowData.updateBigDecimal("FYearBeginCreditBalanceFor", rs.getBigDecimal("FYearBeginCreditBalanceFor"));
	      rowData.updateBigDecimal("FBeginDebitBalanceFor", rs.getBigDecimal("FBeginDebitBalanceFor"));
	      rowData.updateBigDecimal("FBeginCreditBalanceFor", rs.getBigDecimal("FBeginCreditBalanceFor"));
	      rowData.updateBigDecimal("FDebitFor", rs.getBigDecimal("FDebitFor"));
	      rowData.updateBigDecimal("FCreditFor", rs.getBigDecimal("FCreditFor"));
	      rowData.updateBigDecimal("FYearDebitFor", rs.getBigDecimal("FYearDebitFor"));
	      rowData.updateBigDecimal("FYearCreditFor", rs.getBigDecimal("FYearCreditFor"));
	      rowData.updateBigDecimal("FEndDebitBalanceFor", rs.getBigDecimal("FEndDebitBalanceFor"));
	      rowData.updateBigDecimal("FEndCreditBalanceFor", rs.getBigDecimal("FEndCreditBalanceFor"));
	    }
	    if (isShowLocal())
	    {
	      rowData.updateBigDecimal("FYearBeginDebitBalanceLocal", rs.getBigDecimal("FYearBeginDebitBalanceLocal"));
	      rowData.updateBigDecimal("FYearBeginCreditBalanceLocal", rs.getBigDecimal("FYearBeginCreditBalanceLocal"));
	      rowData.updateBigDecimal("FBeginDebitBalanceLocal", rs.getBigDecimal("FBeginDebitBalanceLocal"));
	      rowData.updateBigDecimal("FBeginCreditBalanceLocal", rs.getBigDecimal("FBeginCreditBalanceLocal"));
	      rowData.updateBigDecimal("FDebitLocal", rs.getBigDecimal("FDebitLocal"));
	      rowData.updateBigDecimal("FCreditLocal", rs.getBigDecimal("FCreditLocal"));
	      rowData.updateBigDecimal("FYearDebitLocal", rs.getBigDecimal("FYearDebitLocal"));
	      rowData.updateBigDecimal("FYearCreditLocal", rs.getBigDecimal("FYearCreditLocal"));
	      rowData.updateBigDecimal("FEndDebitBalanceLocal", rs.getBigDecimal("FEndDebitBalanceLocal"));
	      rowData.updateBigDecimal("FEndCreditBalanceLocal", rs.getBigDecimal("FEndCreditBalanceLocal"));
	    }
	    if (isShowRpt())
	    {
	      rowData.updateBigDecimal("FYearBeginDebitBalanceRpt", rs.getBigDecimal("FYearBeginDebitBalanceRpt"));
	      rowData.updateBigDecimal("FYearBeginCreditBalanceRpt", rs.getBigDecimal("FYearBeginCreditBalanceRpt"));
	      rowData.updateBigDecimal("FBeginDebitBalanceRpt", rs.getBigDecimal("FBeginDebitBalanceRpt"));
	      rowData.updateBigDecimal("FBeginCreditBalanceRpt", rs.getBigDecimal("FBeginCreditBalanceRpt"));
	      rowData.updateBigDecimal("FDebitRpt", rs.getBigDecimal("FDebitRpt"));
	      rowData.updateBigDecimal("FCreditRpt", rs.getBigDecimal("FCreditRpt"));
	      rowData.updateBigDecimal("FYearDebitRpt", rs.getBigDecimal("FYearDebitRpt"));
	      rowData.updateBigDecimal("FYearCreditRpt", rs.getBigDecimal("FYearCreditRpt"));
	      rowData.updateBigDecimal("FEndDebitBalanceRpt", rs.getBigDecimal("FEndDebitBalanceRpt"));
	      rowData.updateBigDecimal("FEndCreditBalanceRpt", rs.getBigDecimal("FEndCreditBalanceRpt"));
	    }
	  }
	  
	  private boolean isAccounTotal(IRowSet rs)
	    throws SQLException
	  {
	    if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) {
	      return (rs.getString("accountNumber") == null) || (rs.getString("accountNumber").trim().length() == 0);
	    }
	    return (rs.getString("FAccountID") == null) || (rs.getString("FAccountID").trim().length() == 0);
	  }
	  
	  protected void setYearBegin(IRowSet rs, IGLRptRowData rowData)
	    throws SQLException, BOSException, EASBizException, NumberFormatException
	  {
	    if (isShowQty()) {
	      setYearBegin(rs, rowData, "Qty", "Qty");
	    }
	    if (isShowFor()) {
	      setYearBegin(rs, rowData, "For", "BalanceFor");
	    }
	    if (isShowLocal()) {
	      setYearBegin(rs, rowData, "Local", "BalanceLocal");
	    }
	    if (isShowRpt()) {
	      setYearBegin(rs, rowData, "Rpt", "BalanceRpt");
	    }
	  }
	  
	  private void setYearBegin(IRowSet rs, IGLRptRowData rowData, String suffix, String balSuffix)
	    throws SQLException, EASBizException, BOSException
	  {
	    BigDecimal end = rs.getBigDecimal("FEnd" + balSuffix);
	    if (end == null) {
	      end = GlUtils.zero;
	    }
	    BigDecimal yearDebit = rs.getBigDecimal("FYearDebit" + suffix);
	    if (yearDebit == null) {
	      yearDebit = GlUtils.zero;
	    }
	    BigDecimal yearCredit = rs.getBigDecimal("FYearCredit" + suffix);
	    if (yearCredit == null) {
	      yearCredit = GlUtils.zero;
	    }
	    BigDecimal amount = end.subtract(yearDebit).add(yearCredit);
	    Object dc = rs.getObject(GLRptField.accountDC.getName());
	    if ((dc != null) && (this.cond.getRptParams().isBalanceDirSamAsAccount()))
	    {
	      if (new BigDecimal(dc.toString()).intValue() == 1) {
	        rowData.updateBigDecimal("FYearBeginDebit" + balSuffix, amount);
	      } else {
	        rowData.updateBigDecimal("FYearBeginCredit" + balSuffix, amount.negate());
	      }
	    }
	    else if (amount.doubleValue() < 0.0D) {
	      rowData.updateBigDecimal("FYearBeginCredit" + balSuffix, amount.negate());
	    } else {
	      rowData.updateBigDecimal("FYearBeginDebit" + balSuffix, amount);
	    }
	  }
	  
	  private void insertAccountBalance(String companyIds, int startYear, int startMonth)
	    throws EASBizException, BOSException
	  {
	    boolean showExist = (this.cond.isOptionAmountZero()) || (this.cond.getOptionYearAmountZero()) || (this.cond.isOptionAmountAndBalZero()) || (this.cond.getOptionYearAmountAndBalZero());
	    StringBuffer sqlAccount = new StringBuffer();
	    StringBuffer select = new StringBuffer();
	    select.append(" select AV.FID FAccountId");
	    if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode())
	    {
	      select.append(", AV.FNumber accountNumber");
	      select.append(", AV.").append(getAccountName()).append(" accountName");
	    }
	    select.append(", 0 FIsAccountTotal ");
	    if (this.cond.getIncludeBWAccount()) {
	      select.append(" ,AV.fbw FBWType, 0 FIsAccountBWSum ");
	    }
	    if (isAllCurrency()) {
	      select.append(" ,isnull(TB.FCurrencyID,'").append(getCompany().getBaseCurrency().getId().toString()).append("') FCurrencyID, 0 FIsCurrencyTotal ");
	    }
	    if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	      select.append(",AV.FCompanyid forgunitid ");
	    }
	    if (this.cond.getDisplayAsstDetail()) {
	      select.append(",0 FIsAssist, ' ' FAssistGrpID ");
	    }
	    if (isShowQty())
	    {
	      select.append(", MU.FID FMeasureUnitID");
	      select.append(", MU.fname_").append(getContex().getLocale()).append(" measureUnit");
	    }
	    boolean isUnionQuery = (CompanyDisplayModeEnum.level == this.cond.getCompanyDisplayMode()) || (CompanyDisplayModeEnum.details == this.cond.getCompanyDisplayMode());
	    String avAlias = isUnionQuery ? "TA" : "AV";
	    if (isCreateDataTempTable()) {
	      addInsertSqlForCurrency(avAlias, select);
	    }
	    sqlAccount.append(select.toString());
	    if ((CompanyDisplayModeEnum.level == this.cond.getCompanyDisplayMode()) || (CompanyDisplayModeEnum.details == this.cond.getCompanyDisplayMode()))
	    {
	      sqlAccount.append(" from t_bd_accountview AV ");
	      sqlAccount.append("inner join t_bd_accountview TA on TA.fnumber = AV.fnumber and TA.faccounttableid = AV.faccounttableid and TA.fcompanyid = '").append(this.cond.getCompany().getId()).append("' ");
	    }
	    else
	    {
	      sqlAccount.append(" from t_bd_accountview AV ");
	    }
	    sqlAccount.append(" LEFT OUTER JOIN T_BD_MEASUREUNIT MU on MU.FID = ").append(avAlias).append(".FMeasureUnitID ");
	    if (showExist) {
	      sqlAccount.append(" inner join \t");
	    } else {
	      sqlAccount.append(" left outer join ");
	    }
	    sqlAccount.append("( ");
	    
	    sqlAccount.append(getAccountBalanceSQL(companyIds, startYear, startMonth));
	    sqlAccount.append(") TB on AV.FID = TB.accountId ");
	    
	    sqlAccount.append(" WHERE AV.FAccountTableID = '").append(this.cond.getAccountTableId()).append("' ");
	    if (this.cond.isShowLeafAccount()) {
	      sqlAccount.append(" and ").append(avAlias).append(".fisleaf = 1 ");
	    } else {
	      sqlAccount.append(" and AV.flevel <= ").append(this.cond.getAccountLevelEnd());
	    }
	    sqlAccount.append(" AND AV.FCompanyId in ").append(companyIds);
	    if (!this.cond.getIncludeBWAccount()) {
	      sqlAccount.append(" AND ").append(avAlias).append(".FBW = 0 ");
	    }
	    String accountFilter = new GLRptAccountFilter(getContex(), this.cond, "AV", "AV.FID", "AV.fcompanyid", false, true, this.cond.getCompanyIdsStr()).getSQL();
	    if (accountFilter != null) {
	      sqlAccount.append(" AND ").append(accountFilter);
	    }
	    String sql = sqlAccount.toString();
	    if (!this.cond.isNotIncluePLVoucher())
	    {
	      String where = buildAmountFilterSql("TP");
	      if ((where != null) && (where.length() > 0)) {
	        sql = "select TP.* from (" + sql + ") TP " + where;
	      }
	    }
	    else
	    {
	      sql = getNotIncludePLVoucherSQL(sql, this.plVoucherTempTable);
	    }
	    insertData(sql);
	  }
	  
	  private void insertAssistBalance(Set<String> companyIds, int startYear, int startMonth)
	    throws EASBizException, BOSException
	  {
	    StringBuffer sqlAssist = new StringBuffer();
	    sqlAssist.append(" select TB.FAccountID");
	    if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) {
	      sqlAssist.append(", AV.Fnumber accountNumber, AV.").append(getAccountName()).append(" accountName");
	    }
	    sqlAssist.append(", 0 FIsAccountTotal ");
	    if (this.cond.getIncludeBWAccount()) {
	      sqlAssist.append(", AV.fbw FBWType, 0 FIsAccountBWSum ");
	    }
	    if (isAllCurrency()) {
	      sqlAssist.append(", TB.FCurrencyID, 0 FIsCurrencyTotal ");
	    }
	    if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	      sqlAssist.append(", TB.FOrgUnitId ");
	    }
	    sqlAssist.append(", 1 FIsAssist, TB.FAssistGrpID FAssistGrpID");
	    if (isShowQty())
	    {
	      sqlAssist.append(", MU.FID FMeasureUnitID");
	      sqlAssist.append(", MU.fname_").append(getContex().getLocale()).append(" measureUnit");
	    }
	    boolean isUnionQuery = (CompanyDisplayModeEnum.level == this.cond.getCompanyDisplayMode()) || (CompanyDisplayModeEnum.details == this.cond.getCompanyDisplayMode());
	    String avAlias = isUnionQuery ? "TA" : "AV";
	    if (isCreateDataTempTable()) {
	      addInsertSqlForCurrency(avAlias, sqlAssist);
	    }
	    sqlAssist.append(" FROM (").append(getAssistBalanceSQL(GLRptDaoUtil.getIdFilter(getContex(), companyIds), startYear, startMonth)).append(") TB ");
	    if ((isShowQty()) || (this.cond.getIncludeBWAccount()) || (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()))
	    {
	      sqlAssist.append(" LEFT OUTER JOIN T_BD_AccountView AV ON AV.FID = TB.FAccountID ");
	      if ((isUnionQuery) && (isShowQty())) {
	        sqlAssist.append(" LEFT OUTER JOIN T_BD_AccountView TA ON TA.FNumber = AV.FNumber AND TA.FAccountTableID = AV.FAccountTableID AND TA.FCompanyID = '").append(this.cond.getCompany().getId()).append("' ");
	      }
	    }
	    if (isShowQty()) {
	      sqlAssist.append(" LEFT OUTER JOIN T_BD_MEASUREUNIT MU on MU.FID = ").append(avAlias).append(".FMeasureUnitID ");
	    }
	    if (!this.cond.isNotIncluePLVoucher()) {
	      sqlAssist.append(buildAmountFilterSql("TB"));
	    }
	    String sql = sqlAssist.toString();
	    if (this.cond.isNotIncluePLVoucher()) {
	      sql = getNotIncludePLVoucherSQL(sql, this.plVoucherTempTable);
	    }
	    insertData(sql);
	  }
	  
	  private String buildAmountFilterSql(String t)
	    throws EASBizException, BOSException
	  {
	    String debit = this.isYearQuery ? "FYearDebit" : "FDebit";
	    String credit = this.isYearQuery ? "FYearCredit" : "FCredit";
	    StringBuffer where = new StringBuffer();
	    if (this.cond.isOptionAmountZero())
	    {
	      StringBuffer str = new StringBuffer();
	      if (isShowFor()) {
	        str.append(" ((").append(t).append(".").append(debit).append("For <> 0) OR (").append(t).append(".").append(credit).append("For <>0 ))");
	      }
	      if (isShowLocal())
	      {
	        if (str.length() > 0) {
	          str.append(" or ");
	        }
	        str.append(" ((").append(t).append(".").append(debit).append("Local <> 0) OR (").append(t).append(".").append(credit).append("Local <>0 ))");
	      }
	      if (isShowRpt())
	      {
	        if (str.length() > 0) {
	          str.append(" or ");
	        }
	        str.append(" ((").append(t).append(".").append(debit).append("Rpt <> 0) OR (").append(t).append(".").append(credit).append("Rpt <>0 ))");
	      }
	      where.append(" where (").append(str).append(" ) ");
	    }
	    if (this.cond.isOptionBalanceZero())
	    {
	      StringBuffer str = new StringBuffer();
	      if (isShowFor()) {
	        str.append(" ((").append(t).append(".FEndBalanceFor <> 0))");
	      }
	      if (isShowLocal())
	      {
	        if (str.length() > 0) {
	          str.append(" or ");
	        }
	        str.append(" ((").append(t).append(".FEndBalanceLocal <> 0))");
	      }
	      if (isShowRpt())
	      {
	        if (str.length() > 0) {
	          str.append(" or ");
	        }
	        str.append(" ((").append(t).append(".FEndBalanceRpt <> 0))");
	      }
	      where.append(where.length() == 0 ? " where " : " and ").append("(").append(str).append(") ");
	    }
	    if (this.cond.getOptionYearAmountZero())
	    {
	      StringBuffer str = new StringBuffer();
	      if (isShowFor()) {
	        str.append(" ((").append(t).append(".FYearDebitFor <> 0) OR (").append(t).append(".FYearCreditFor <>0 ))");
	      }
	      if (isShowLocal())
	      {
	        if (str.length() > 0) {
	          str.append(" or ");
	        }
	        str.append(" ((").append(t).append(".FYearDebitLocal <> 0) OR (").append(t).append(".FYearCreditLocal <>0 ))");
	      }
	      if (isShowRpt())
	      {
	        if (str.length() > 0) {
	          str.append(" or ");
	        }
	        str.append(" ((").append(t).append(".FYearDebitRpt <> 0) OR (").append(t).append(".FYearCreditRpt <>0 ))");
	      }
	      where.append(where.length() == 0 ? " where " : " and ").append("(").append(str).append(") ");
	    }
	    if (this.cond.isOptionAmountAndBalZero())
	    {
	      StringBuffer str = new StringBuffer();
	      if (isShowFor()) {
	        str.append(" ((").append(t).append(".").append(debit).append("For <> 0) OR (").append(t).append(".").append(credit).append("For <>0 ) OR (").append(t).append(".FEndBalanceFor <> 0))");
	      }
	      if (isShowLocal())
	      {
	        if (str.length() > 0) {
	          str.append(" or ");
	        }
	        str.append(" ((").append(t).append(".").append(debit).append("Local <> 0) OR (").append(t).append(".").append(credit).append("Local <>0 ) OR (").append(t).append(".FEndBalanceLocal <> 0))");
	      }
	      if (isShowRpt())
	      {
	        if (str.length() > 0) {
	          str.append(" or ");
	        }
	        str.append(" ((").append(t).append(".").append(debit).append("Rpt <> 0) OR (").append(t).append(".").append(credit).append("Rpt <>0 ) OR (").append(t).append(".FEndBalanceRpt <> 0))");
	      }
	      where.append(where.length() == 0 ? " where " : " and ").append("(").append(str).append(") ");
	    }
	    if (this.cond.getOptionYearAmountAndBalZero())
	    {
	      StringBuffer str = new StringBuffer();
	      if (isShowFor()) {
	        str.append(" ((").append(t).append(".FYearDebitFor <> 0) OR (").append(t).append(".FYearCreditFor <>0 ) OR (").append(t).append(".FEndBalanceFor <> 0))");
	      }
	      if (isShowLocal())
	      {
	        if (str.length() > 0) {
	          str.append(" or ");
	        }
	        str.append(" ((").append(t).append(".FYearDebitLocal <> 0) OR (").append(t).append(".FYearCreditLocal <>0 ) OR (").append(t).append(".FEndBalanceLocal <> 0))");
	      }
	      if (isShowRpt())
	      {
	        if (str.length() > 0) {
	          str.append(" or ");
	        }
	        str.append(" ((").append(t).append(".FYearDebitRpt <> 0) OR (").append(t).append(".FYearCreditRpt <>0 ) OR (").append(t).append(".FEndBalanceRpt <> 0))");
	      }
	      where.append(where.length() == 0 ? " where " : " and ").append("(").append(str).append(") ");
	    }
	    return where.toString();
	  }
	  
	  private String getAssistBalanceSQL(String companyIds, int startYear, int startMonth)
	    throws EASBizException, BOSException
	  {
	    StringBuffer sql = new StringBuffer();
	    sql.append("select b.faccountid");
	    sql.append(", b.fassistgrpid ");
	    if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	      sql.append(", b.forgunitid ");
	    }
	    if (isAllCurrency()) {
	      sql.append(", b.fcurrencyid ");
	    }
	    if (isCreateDataTempTable())
	    {
	      GLRptAccountBalQueryHelper helper = getQueryHelper();
	      sql.append(helper.getAmountSelectSQL());
	      sql.append(" from ").append(getAssistBalanceTable()).append(" b ");
	      sql.append(" inner join ").append(this.sysStatusTmpTable).append(" ssc on ssc.fcompanyid = b.forgunitid ");
	    }
	    else
	    {
	      sql.append(" from ").append(getAssistBalanceTable()).append(" b ");
	    }
	    sql.append(" where b.forgunitid in ").append(companyIds);
	    sql.append(" and ").append(getPeriodFilter(startYear, startMonth));
	    if (SQLUtil.isQueryByCurrency(this.cond)) {
	      sql.append(" and b.fcurrencyid = '").append(this.cond.getCurrencyID()).append("'");
	    }
	    sql.append(" and exists (select 1 from ").append(getDataSourceTableName()).append(" t where t.faccountid = b.faccountid ");
	    if (isAllCurrency()) {
	      sql.append(" and b.fcurrencyid = t.fcurrencyid ");
	    }
	    sql.append(") ");
	    if ((isCreateDataTempTable()) || (this.cond.getPeriodYearStart() != this.cond.getPeriodYearEnd()) || (this.cond.getPeriodNumberStart() != this.cond.getPeriodNumberEnd()))
	    {
	      sql.append(" group by b.faccountid ");
	      if (isAllCurrency()) {
	        sql.append(", b.fcurrencyid ");
	      }
	      if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	        sql.append(", b.forgUnitid ");
	      }
	      sql.append(", b.fassistgrpid ");
	    }
	    return sql.toString();
	  }
	  
	  private String getAccountBalanceSQL(String companyIds, int startYear, int startMonth)
	    throws EASBizException, BOSException
	  {
	    StringBuffer sql = new StringBuffer();
	    sql.append(" select");
	    if ((StringUtils.isEmpty(this.cond.getAccountCodeStart())) && (StringUtils.isEmpty(this.cond.getAccountCodeEnd()))) {
	      sql.append("/*+ INDEX(b ").append(getAccountBalanceIndexName()).append(")*/");
	    }
	    sql.append(" av.fid accountId ");
	    if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	      sql.append(", b.forgunitid ");
	    }
	    if (isAllCurrency()) {
	      sql.append(", b.fcurrencyid ");
	    }
	    if (isCreateDataTempTable())
	    {
	      GLRptAccountBalQueryHelper helper = getQueryHelper();
	      sql.append(helper.getAmountSelectSQL());
	    }
	    sql.append(" from ").append(getAccountBalanceTable()).append(" b ");
	    sql.append(" inner join t_bd_accountview av on av.fid = b.faccountid ");
	    if (isCreateDataTempTable()) {
	      sql.append(" inner join ").append(this.sysStatusTmpTable).append(" ssc on ssc.fcompanyid = b.forgunitid ");
	    }
	    sql.append(" where b.forgunitid in ").append(companyIds);
	    sql.append(" and ").append(getPeriodFilter(startYear, startMonth));
	    if (SQLUtil.isQueryByCurrency(this.cond)) {
	      sql.append(" and b.fcurrencyid = '").append(this.cond.getCurrencyID()).append("'");
	    }
	    sql.append(" and av.faccounttableid = '").append(this.cond.getAccountTableId()).append("' ");
	    sql.append(" and av.fcompanyid in ").append(companyIds);
	    GLRptAccountFilter accountFilter = new GLRptAccountFilter(getContex(), this.cond, "av", "av.fid", "b.forgunitid", false, true, companyIds);
	    accountFilter.setPermFilter(false);
	    if (accountFilter.getSQL() != null) {
	      sql.append(" and ").append(accountFilter.getSQL());
	    }
	    if (this.cond.isShowLeafAccount())
	    {
	      if ((CompanyDisplayModeEnum.enumerate == this.cond.getCompanyDisplayMode()) || (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode())) {
	        sql.append(" and av.fisleaf = 1 ");
	      }
	    }
	    else {
	      sql.append(" and av.flevel <= ").append(this.cond.getAccountLevelEnd());
	    }
	    sql.append(" group by av.fid ");
	    if (isAllCurrency()) {
	      sql.append(" ,b.fcurrencyid ");
	    }
	    if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	      sql.append(" ,b.forgUnitid ");
	    }
	    return sql.toString();
	  }
	  
	  private String getAccountBalanceIndexName()
	  {
	    StringBuffer indexName = new StringBuffer();
	    indexName.append("IX_GL_AC_");
	    indexName.append(this.cond.getOptionPosting() ? "1" : "5");
	    if ("11111111-1111-1111-1111-111111111111DEB58FDC".equals(this.cond.getCurrencyID())) {
	      indexName.append("L");
	    } else if ("22222222-2222-2222-2222-222222222222DEB58FDC".equals(this.cond.getCurrencyID())) {
	      indexName.append("R");
	    } else {
	      indexName.append("F");
	    }
	    indexName.append("_AC1");
	    return indexName.toString();
	  }
	  
	  protected String getPeriodFilter(int startYear, int startMonth)
	    throws EASBizException, BOSException
	  {
	    StringBuffer sql = new StringBuffer();
	    sql.append(" b.fperiod in ");
	    sql.append(SQLUtil.buildPeriodNumbersInSql(startYear, startMonth, this.cond.getPeriodYearEnd(), this.cond.getPeriodNumberEnd()));
	    return sql.toString();
	  }
	  
	  private void addInsertSqlForCurrency(String alias, StringBuffer sql)
	    throws EASBizException, BOSException
	  {
	    if (isShowQty()) {
	      sql.append(", TB.FBeginQty");
	    }
	    if (isShowFor()) {
	      sql.append(", TB.FBeginBalanceFor");
	    }
	    if (isShowLocal()) {
	      sql.append(", TB.FBeginBalanceLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(", TB.FBeginBalanceRpt");
	    }
	    if (isShowQty()) {
	      sql.append(",isnull(TB.FDebitQty,0) FDebitQty ");
	    }
	    if (isShowFor()) {
	      sql.append(",isnull(TB.FDebitFor,0) FDebitFor ");
	    }
	    if (isShowLocal()) {
	      sql.append(",isnull(TB.FDebitLocal,0) FDebitLocal ");
	    }
	    if (isShowRpt()) {
	      sql.append(",isnull(TB.FDebitRpt,0) FDebitRpt ");
	    }
	    if (isShowQty()) {
	      sql.append(",isnull(TB.FCreditQty,0) FCreditQty ");
	    }
	    if (isShowFor()) {
	      sql.append(",isnull(TB.FCreditFor,0) FCreditFor ");
	    }
	    if (isShowLocal()) {
	      sql.append(",isnull(TB.FCreditLocal,0) FCreditLocal ");
	    }
	    if (isShowRpt()) {
	      sql.append(",isnull(TB.FCreditRpt,0) FCreditRpt ");
	    }
	    if (isShowQty()) {
	      sql.append(",isnull(TB.FYearDebitQty,0) FYearDebitQty ");
	    }
	    if (isShowFor()) {
	      sql.append(",isnull(TB.FYearDebitFor,0) FYearDebitFor ");
	    }
	    if (isShowLocal()) {
	      sql.append(",isnull(TB.FYearDebitLocal,0) FYearDebitLocal ");
	    }
	    if (isShowRpt()) {
	      sql.append(",isnull(TB.FYearDebitRpt,0) FYearDebitRpt ");
	    }
	    if (isShowQty()) {
	      sql.append(",isnull(TB.FYearCreditQty,0) FYearCreditQty ");
	    }
	    if (isShowFor()) {
	      sql.append(",isnull(TB.FYearCreditFor,0) FYearCreditFor ");
	    }
	    if (isShowLocal()) {
	      sql.append(",isnull(TB.FYearCreditLocal,0) FYearCreditLocal ");
	    }
	    if (isShowRpt()) {
	      sql.append(",isnull(TB.FYearCreditRpt,0) FYearCreditRpt ");
	    }
	    if (isShowQty()) {
	      sql.append(", TB.FEndQty");
	    }
	    if (isShowFor()) {
	      sql.append(", TB.FEndBalanceFor");
	    }
	    if (isShowLocal()) {
	      sql.append(", TB.FEndBalanceLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(", TB.FEndBalanceRpt");
	    }
	  }
	  
	  protected void insertAccountSum(boolean isBW)
	    throws BOSException, EASBizException
	  {
	    StringBuffer sql = new StringBuffer();
	    sql.append("SELECT distinct ' ' FAccountID");
	    if (this.cond.getCompanyDisplayMode() == CompanyDisplayModeEnum.merger) {
	      sql.append(", to_char(null) accountNumber");
	    }
	    sql.append(", 1 FIsAccountTotal ");
	    if (this.cond.getIncludeBWAccount()) {
	      if (isBW)
	      {
	        sql.append(" ,TP.FBWType ");
	        sql.append(" ,1 FIsAccountBWSum ");
	      }
	      else
	      {
	        sql.append(" ,2 FBWType ");
	        sql.append(" ,2 FIsAccountBWSum ");
	      }
	    }
	    if (isAllCurrency()) {
	      sql.append(",TP.FCurrencyID, 0 FIsCurrencyTotal ");
	    }
	    if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	      sql.append(",TP.FOrgUnitID ");
	    }
	    if (this.cond.getDisplayAsstDetail()) {
	      sql.append(",0 FIsAssist, ' ' FAssistGrpID ");
	    }
	    sql.append(" FROM ").append(getDataSourceTableName()).append(" TP ");
	    if ((CompanyDisplayModeEnum.level == this.cond.getCompanyDisplayMode()) || (CompanyDisplayModeEnum.details == this.cond.getCompanyDisplayMode())) {
	      sql.append(" where TP.FOrgUnitID is not null");
	    }
	    List params = new ArrayList();
	    List fields = new ArrayList();
	    fields.add("FAccountID");
	    if (this.cond.getCompanyDisplayMode() == CompanyDisplayModeEnum.merger) {
	      fields.add("accountNumber");
	    }
	    fields.add("FIsAccountTotal");
	    if (this.cond.getIncludeBWAccount())
	    {
	      fields.add("FBWType");
	      fields.add("FIsAccountBWSum");
	    }
	    if (isAllCurrency())
	    {
	      fields.add("FCurrencyID");
	      fields.add("FIsCurrencyTotal");
	    }
	    if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	      fields.add("FOrgUnitID");
	    }
	    if (this.cond.getDisplayAsstDetail())
	    {
	      fields.add("FIsAssist");
	      fields.add("FAssistGrpID");
	    }
	    String[] fs = new String[fields.size()];
	    insertData((String[])fields.toArray(fs), sql.toString(), params);
	  }
	  
	  protected void createPLVoucherTempTable()
	    throws BOSException, EASBizException
	  {
	    StringBuffer struct = new StringBuffer();
	    if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	      struct.append("companyid varchar(44),");
	    }
	    struct.append("accountid varchar(44)");
	    if (isAllCurrency()) {
	      struct.append(",currencyid varchar(44)");
	    }
	    if (this.cond.getDisplayAsstDetail()) {
	      struct.append(",hgid varchar(44)");
	    }
	    if (isShowFor())
	    {
	      struct.append(",debitfor NUMERIC(28,10)");
	      struct.append(",creditfor NUMERIC(28,10)");
	      struct.append(",yeardebitfor NUMERIC(28,10)");
	      struct.append(",yearcreditfor NUMERIC(28,10)");
	    }
	    if (isShowLocal())
	    {
	      struct.append(",debitlocal NUMERIC(28,10)");
	      struct.append(",creditlocal NUMERIC(28,10)");
	      struct.append(",yeardebitlocal NUMERIC(28,10)");
	      struct.append(",yearcreditlocal NUMERIC(28,10)");
	    }
	    if (isShowRpt())
	    {
	      struct.append(",debitrpt NUMERIC(28,10)");
	      struct.append(",creditrpt NUMERIC(28,10)");
	      struct.append(",yeardebitrpt NUMERIC(28,10)");
	      struct.append(",yearcreditrpt NUMERIC(28,10)");
	    }
	    if (isShowQty())
	    {
	      struct.append(",debitqty NUMERIC(28,10)");
	      struct.append(",creditqty NUMERIC(28,10)");
	      struct.append(",yeardebitqty NUMERIC(28,10)");
	      struct.append(",yearcreditqty NUMERIC(28,10)");
	    }
	    StringBuffer indexStr = new StringBuffer();
	    if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	      indexStr.append(" companyid,");
	    }
	    indexStr.append("accountid");
	    if (isAllCurrency()) {
	      indexStr.append(",currencyid");
	    }
	    if (this.cond.getDisplayAsstDetail()) {
	      indexStr.append(",hgid ");
	    }
	    this.plVoucherTempTable = GLTempTableUtil.createDBSysTempTable(getContex(), struct.toString(), indexStr.toString());
	    
	    StringBuffer sql = new StringBuffer("select ");
	    if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	      sql.append("v.fcompanyid companyid,");
	    }
	    sql.append("ve.faccountid accountid");
	    if (isAllCurrency()) {
	      sql.append(",ve.fcurrencyid currencyid");
	    }
	    if (this.cond.getDisplayAsstDetail()) {
	      sql.append(",' ' hgid ");
	    }
	    if (isShowFor())
	    {
	      sql.append(" ,sum(case ve.FEntryDC when ").append(1).append(" then ve.FOriginalAmount else 0 end)").append(" debitfor ");
	      sql.append(" ,sum(case ve.FEntryDC when ").append(0).append(" then ve.FOriginalAmount else 0 end)").append(" creditfor ");
	      sql.append(" ,sum(case when ve.FEntryDC =").append(1).append(" and pd.fperiodyear = ").append(this.cond.getPeriodYearEnd()).append(" then ve.FOriginalAmount else 0 end) yeardebitfor ");
	      sql.append(" ,sum(case when ve.FEntryDC =").append(0).append(" and pd.fperiodyear = ").append(this.cond.getPeriodYearEnd()).append(" then ve.FOriginalAmount else 0 end) yearcreditfor ");
	    }
	    if (isShowLocal())
	    {
	      sql.append(" ,sum(case ve.FEntryDC when ").append(1).append(" then ve.FLocalAmount else 0 end) debitlocal ");
	      sql.append(" ,sum(case ve.FEntryDC when ").append(0).append(" then ve.FLocalAmount else 0 end) creditlocal ");
	      sql.append(" ,sum(case when ve.FEntryDC =").append(1).append(" and pd.fperiodyear = ").append(this.cond.getPeriodYearEnd()).append(" then ve.FLocalAmount else 0 end) yeardebitlocal ");
	      sql.append(" ,sum(case when ve.FEntryDC =").append(0).append(" and pd.fperiodyear = ").append(this.cond.getPeriodYearEnd()).append(" then ve.FLocalAmount else 0 end) yearcreditlocal ");
	    }
	    if (isShowRpt())
	    {
	      sql.append(" ,sum(case ve.FEntryDC when ").append(1).append(" then ve.FReportingAmount else 0 end) debitrpt ");
	      sql.append(" ,sum(case ve.FEntryDC when ").append(0).append(" then ve.FReportingAmount else 0 end) creditrpt ");
	      sql.append(" ,sum(case when ve.FEntryDC =").append(1).append(" and pd.fperiodyear = ").append(this.cond.getPeriodYearEnd()).append(" then ve.FReportingAmount else 0 end) yeardebitrpt ");
	      sql.append(" ,sum(case when ve.FEntryDC =").append(0).append(" and pd.fperiodyear = ").append(this.cond.getPeriodYearEnd()).append(" then ve.FReportingAmount else 0 end) yearcreditrpt ");
	    }
	    if (isShowQty())
	    {
	      sql.append(" ,sum(case ve.FEntryDC when ").append(1).append(" then ve.FStandardQuantity else 0 end) debitqty ");
	      sql.append(" ,sum(case ve.FEntryDC when ").append(0).append(" then ve.FStandardQuantity else 0 end) creditqty ");
	      sql.append(" ,sum(case when ve.FEntryDC =").append(1).append(" and pd.fperiodyear = ").append(this.cond.getPeriodYearEnd()).append(" then ve.FStandardQuantity else 0 end) yeardebitqty ");
	      sql.append(" ,sum(case when ve.FEntryDC =").append(0).append(" and pd.fperiodyear = ").append(this.cond.getPeriodYearEnd()).append(" then ve.FStandardQuantity else 0 end) yearcreditqty ");
	    }
	    sql.append(" from t_gl_voucher v ");
	    sql.append(" inner join t_gl_voucherentry ve on ve.fbillid = v.fid ");
	    sql.append(" inner join t_bd_accountview av on av.fid = ve.faccountid and av.fcompanyid = v.fcompanyid ");
	    List sp = new ArrayList();
	    sql.append(" inner join t_bd_period pd on pd.fid = v.fperiodid ");
	    sql.append(" where v.fbizstatus ").append(this.cond.getOptionPosting() ? " in (1,3,5)" : "=5");
	    sql.append(" and fsourcetype = ").append(1);
	    if (CompanyDisplayModeEnum.enumerate == this.cond.getCompanyDisplayMode())
	    {
	      sql.append(" and v.fcompanyid = ?");
	      sp.add(getCompany().getId().toString());
	    }
	    else
	    {
	      sql.append(" and v.fcompanyid in ").append(this.cond.getCompanyIdsStr());
	    }
	    sql.append(" and av.faccounttableid = ? ");
	    sp.add(this.cond.getAccountTableId());
	    sql.append(" and av.fcompanyid in ").append(this.cond.getCompanyIdsStr());
	    GLRptAccountFilter accountFilter = new GLRptAccountFilter(getContex(), this.cond, "av", "av.fid", "v.fcompanyid", false, true, this.cond.getCompanyIdsStr());
	    accountFilter.setPermFilter(false);
	    String accountFilterSQL = accountFilter.getSQL();
	    if (accountFilterSQL != null) {
	      sql.append(" and ").append(accountFilterSQL);
	    }
	    sql.append(" and pd.fnumber >= ?").append(" and pd.fnumber <=?");
	    sp.add(Integer.valueOf(this.cond.getPeriodYearStart() * 100 + this.cond.getPeriodNumberStart()));
	    sp.add(Integer.valueOf(this.cond.getPeriodYearEnd() * 100 + this.cond.getPeriodNumberEnd()));
	    if ((!CurrencyInfo.GENERAL_LOCAL_CURRENCY_ID.toString().equals(this.cond.getCurrencyID())) && (!CurrencyInfo.GENERAL_REPORT_CURRENCY_ID.toString().equals(this.cond.getCurrencyID())) && (!isAllCurrency()))
	    {
	      sql.append(" and ve.fcurrencyid = ?");
	      sp.add(this.cond.getCurrencyID());
	    }
	    sql.append(" group by ");
	    if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	      sql.append("v.fcompanyid,");
	    }
	    sql.append("ve.faccountid");
	    if (isAllCurrency()) {
	      sql.append(",ve.fcurrencyid");
	    }
	    if (this.cond.getDisplayAsstDetail())
	    {
	      sql.append(" union all ");
	      sql.append(" select ");
	      if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	        sql.append("v.fcompanyid companyid,");
	      }
	      sql.append("ve.faccountid accountid");
	      if (isAllCurrency()) {
	        sql.append(",ve.fcurrencyid currencyid");
	      }
	      sql.append(",r.fassgrpid hgid");
	      if (isShowFor())
	      {
	        sql.append(" ,sum(case ve.FEntryDC when ").append(1).append(" then r.FOriginalAmount else 0 end)").append(" debitfor ");
	        sql.append(" ,sum(case ve.FEntryDC when ").append(0).append(" then r.FOriginalAmount else 0 end)").append(" creditfor ");
	        sql.append(" ,sum(case when ve.FEntryDC =").append(1).append(" and pd.fperiodyear = ").append(this.cond.getPeriodYearEnd()).append(" then r.FOriginalAmount else 0 end) yeardebitfor ");
	        sql.append(" ,sum(case when ve.FEntryDC =").append(0).append(" and pd.fperiodyear = ").append(this.cond.getPeriodYearEnd()).append(" then r.FOriginalAmount else 0 end) yearcreditfor ");
	      }
	      if (isShowLocal())
	      {
	        sql.append(" ,sum(case ve.FEntryDC when ").append(1).append(" then r.FLocalAmount else 0 end) debitlocal ");
	        sql.append(" ,sum(case ve.FEntryDC when ").append(0).append(" then r.FLocalAmount else 0 end) creditlocal ");
	        sql.append(" ,sum(case when ve.FEntryDC =").append(1).append(" and pd.fperiodyear = ").append(this.cond.getPeriodYearEnd()).append(" then r.FLocalAmount else 0 end) yeardebitlocal ");
	        sql.append(" ,sum(case when ve.FEntryDC =").append(0).append(" and pd.fperiodyear = ").append(this.cond.getPeriodYearEnd()).append(" then r.FLocalAmount else 0 end) yearcreditlocal ");
	      }
	      if (isShowRpt())
	      {
	        sql.append(" ,sum(case ve.FEntryDC when ").append(1).append(" then r.FReportingAmount else 0 end) debitrpt ");
	        sql.append(" ,sum(case ve.FEntryDC when ").append(0).append(" then r.FReportingAmount else 0 end) creditrpt ");
	        sql.append(" ,sum(case when ve.FEntryDC =").append(1).append(" and pd.fperiodyear = ").append(this.cond.getPeriodYearEnd()).append(" then r.FReportingAmount else 0 end) yeardebitrpt ");
	        sql.append(" ,sum(case when ve.FEntryDC =").append(0).append(" and pd.fperiodyear = ").append(this.cond.getPeriodYearEnd()).append(" then r.FReportingAmount else 0 end) yearcreditrpt ");
	      }
	      if (isShowQty())
	      {
	        sql.append(" ,sum(case ve.FEntryDC when ").append(1).append(" then r.FStandardQuantity else 0 end) debitqty ");
	        sql.append(" ,sum(case ve.FEntryDC when ").append(0).append(" then r.FStandardQuantity else 0 end) creditqty ");
	        sql.append(" ,sum(case when ve.FEntryDC =").append(1).append(" and pd.fperiodyear = ").append(this.cond.getPeriodYearEnd()).append(" then r.FStandardQuantity else 0 end) yeardebitqty ");
	        sql.append(" ,sum(case when ve.FEntryDC =").append(0).append(" and pd.fperiodyear = ").append(this.cond.getPeriodYearEnd()).append(" then r.FStandardQuantity else 0 end) yearcreditqty ");
	      }
	      sql.append(" from t_gl_voucher v ");
	      sql.append(" inner join t_gl_voucherentry ve on ve.fbillid = v.fid ");
	      sql.append(" inner join t_gl_voucherassistrecord r on r.fbillid = v.fid and r.fentryid = ve.fid ");
	      sql.append(" inner join t_bd_accountview av on av.fid = ve.faccountid and av.fcompanyid = v.fcompanyid ");
	      sql.append(" inner join t_bd_period pd on pd.fid = v.fperiodid ");
	      sql.append(" where v.fbizstatus ").append(this.cond.getOptionPosting() ? " in (1,3,5)" : "=5");
	      sql.append(" and fsourcetype = ").append(1);
	      if (CompanyDisplayModeEnum.enumerate == this.cond.getCompanyDisplayMode())
	      {
	        sql.append(" and v.fcompanyid = ?");
	        sp.add(getCompany().getId().toString());
	      }
	      else
	      {
	        sql.append(" and v.fcompanyid in ").append(this.cond.getCompanyIdsStr());
	      }
	      sql.append(" and av.faccounttableid = ? ");
	      sp.add(this.cond.getAccountTableId());
	      sql.append(" and av.fcompanyid in ").append(this.cond.getCompanyIdsStr());
	      if (accountFilterSQL != null) {
	        sql.append(" and ").append(accountFilterSQL);
	      }
	      sql.append(" and pd.ftypeid = ?");
	      sp.add(this.cond.getPeriodTypeId());
	      sql.append(" and pd.fnumber >= ?").append(" and pd.fnumber <=?");
	      sp.add(Integer.valueOf(this.cond.getPeriodYearStart() * 100 + this.cond.getPeriodNumberStart()));
	      sp.add(Integer.valueOf(this.cond.getPeriodYearEnd() * 100 + this.cond.getPeriodNumberEnd()));
	      if ((!CurrencyInfo.GENERAL_LOCAL_CURRENCY_ID.toString().equals(this.cond.getCurrencyID())) && (!CurrencyInfo.GENERAL_REPORT_CURRENCY_ID.toString().equals(this.cond.getCurrencyID())) && (!isAllCurrency()))
	      {
	        sql.append(" and ve.fcurrencyid = ?");
	        sp.add(this.cond.getCurrencyID());
	      }
	      sql.append(" group by ");
	      if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	        sql.append("v.fcompanyid,");
	      }
	      sql.append("ve.faccountid,");
	      if (isAllCurrency()) {
	        sql.append("ve.fcurrencyid,");
	      }
	      sql.append("r.fassgrpid");
	    }
	    try
	    {
	      DbUtil.execute(getContex(), "insert into " + this.plVoucherTempTable + " " + sql.toString(), sp.toArray());
	      if ((!this.cond.isShowLeafAccount()) || (CompanyDisplayModeEnum.level == this.cond.getCompanyDisplayMode()) || (CompanyDisplayModeEnum.details == this.cond.getCompanyDisplayMode())) {
	        insertPLAccountSum(this.plVoucherTempTable);
	      }
	    }
	    catch (Exception e)
	    {
	      logger.error("Execute select into failed.(" + sql + ")", e);
	      throw new BOSException("Error sql: " + sql, e);
	    }
	  }
	  
	  private void insertPLAccountSum(String plTempTable)
	    throws BOSException, EASBizException, SQLException
	  {
	    int maxLevel = getMaxPLAccountLevel(plTempTable);
	    while (maxLevel > 1)
	    {
	      StringBuffer accountSum = new StringBuffer(" select ");
	      if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	        accountSum.append("t.companyid companyid,");
	      }
	      accountSum.append("av.fid accountid");
	      if (isAllCurrency()) {
	        accountSum.append(",t.currencyid currencyid ");
	      }
	      if (this.cond.getDisplayAsstDetail()) {
	        accountSum.append(",' ' hgid ");
	      }
	      if (isShowFor())
	      {
	        accountSum.append(" ,sum(debitfor) debitfor ");
	        accountSum.append(" ,sum(creditfor) creditfor ");
	        accountSum.append(" ,sum(yeardebitfor) yeardebitfor ");
	        accountSum.append(" ,sum(yearcreditfor) yearcreditfor ");
	      }
	      if (isShowLocal())
	      {
	        accountSum.append(" ,sum(debitlocal) debitlocal ");
	        accountSum.append(" ,sum(creditlocal) creditlocal ");
	        accountSum.append(" ,sum(yeardebitlocal) yeardebitlocal ");
	        accountSum.append(" ,sum(yearcreditlocal) yearcreditlocal ");
	      }
	      if (isShowRpt())
	      {
	        accountSum.append(" ,sum(debitrpt) debitrpt ");
	        accountSum.append(" ,sum(creditrpt) creditrpt ");
	        accountSum.append(" ,sum(yeardebitrpt) yeardebitrpt ");
	        accountSum.append(" ,sum(yearcreditrpt) yearcreditrpt ");
	      }
	      if (isShowQty())
	      {
	        accountSum.append(" ,sum(debitqty) debitqty ");
	        accountSum.append(" ,sum(creditqty) creditqty ");
	        accountSum.append(" ,sum(yeardebitqty) yeardebitqty ");
	        accountSum.append(" ,sum(yearcreditqty) yearcreditqty ");
	      }
	      accountSum.append(" from ").append(plTempTable).append(" t ");
	      accountSum.append(" inner join t_bd_accountview lav on lav.fid = t.accountid ");
	      accountSum.append(" inner join t_bd_accountview av on av.fid = lav.fparentid ");
	      
	      accountSum.append(" where lav.flevel = ? ");
	      if (this.cond.getDisplayAsstDetail()) {
	        accountSum.append(" and t.hgid = ' ' ");
	      }
	      accountSum.append(" group by ");
	      if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	        accountSum.append("t.companyid,");
	      }
	      accountSum.append("av.fid");
	      if (isAllCurrency()) {
	        accountSum.append(",t.currencyid ");
	      }
	      List params = new ArrayList();
	      params.add(new Integer(maxLevel));
	      execute("insert into " + plTempTable + accountSum.toString(), params.toArray());
	      maxLevel--;
	    }
	  }
	  
	  private int getMaxPLAccountLevel(String plBalanceTable)
	    throws BOSException, SQLException, EASBizException
	  {
	    int maxLevel = 0;
	    StringBuffer sql = new StringBuffer();
	    sql.append(" select max(av.flevel) maxLevel from ").append(plBalanceTable).append(" temp ");
	    sql.append(" inner join t_bd_accountview av on av.fid = temp.accountid ");
	    if (CompanyDisplayModeEnum.level == this.cond.getCompanyDisplayMode()) {
	      sql.append(" and av.fcompanyid = temp.companyid ");
	    }
	    IRowSet rs = executeQuery(sql.toString());
	    while (rs.next()) {
	      maxLevel = rs.getInt("maxLevel");
	    }
	    return maxLevel;
	  }
	  
	  protected String getNotIncludePLVoucherSQL(String sqlToBeExcute, String plVoucherTempTable)
	    throws BOSException, EASBizException
	  {
	    StringBuffer tempSql = new StringBuffer();
	    
	    String where = buildAmountFilterSql(" tpnl ");
	    if (where.length() > 0) {
	      tempSql.append(" select tpnl.* from (");
	    }
	    tempSql.append(" select t1.FAccountID");
	    if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) {
	      tempSql.append(",t1.accountNumber, t1.accountName");
	    }
	    tempSql.append(", t1.FIsAccountTotal ");
	    if (this.cond.getIncludeBWAccount())
	    {
	      tempSql.append(" ,t1.FBWType ");
	      
	      tempSql.append(" ,t1.FIsAccountBWSum ");
	    }
	    if (isAllCurrency())
	    {
	      tempSql.append(",t1.FCurrencyID ");
	      tempSql.append(",t1.FIsCurrencyTotal ");
	    }
	    if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	      tempSql.append(",t1.FOrgUnitID ");
	    }
	    if (this.cond.getDisplayAsstDetail())
	    {
	      tempSql.append(",t1.FIsAssist ");
	      tempSql.append(",t1.FAssistGrpID ");
	    }
	    if (isShowQty())
	    {
	      tempSql.append(", t1.FMeasureUnitID  \r\n");
	      tempSql.append(", t1.measureUnit  \r\n");
	      tempSql.append(", t1.FBeginQty  ");
	    }
	    if (isShowFor()) {
	      tempSql.append(", t1.FBeginBalanceFor\t");
	    }
	    if (isShowLocal()) {
	      tempSql.append(", t1.FBeginBalanceLocal\t");
	    }
	    if (isShowRpt()) {
	      tempSql.append(", t1.FBeginBalanceRpt\t");
	    }
	    if (isShowQty()) {
	      tempSql.append(", t1.FDebitQty - isnull(t2.DebitQty,0) FDebitQty ");
	    }
	    if (isShowFor()) {
	      tempSql.append(", t1.FDebitFor - isnull(t2.DebitFor,0) FDebitFor");
	    }
	    if (isShowLocal()) {
	      tempSql.append(", t1.FDebitLocal - isnull(t2.DebitLocal,0) FDebitLocal");
	    }
	    if (isShowRpt()) {
	      tempSql.append(", t1.FDebitRpt - isnull(t2.DebitRpt,0) FDebitRpt");
	    }
	    if (isShowQty()) {
	      tempSql.append(", t1.FCreditQty - isnull(t2.CreditQty,0) FCreditQty ");
	    }
	    if (isShowFor()) {
	      tempSql.append(", t1.FCreditFor - isnull(t2.CreditFor,0) FCreditFor ");
	    }
	    if (isShowLocal()) {
	      tempSql.append(", t1.FCreditLocal - isnull(t2.CreditLocal,0) FCreditLocal ");
	    }
	    if (isShowRpt()) {
	      tempSql.append(", t1.FCreditRpt - isnull(t2.CreditRpt,0) FCreditRpt ");
	    }
	    if (isShowQty()) {
	      tempSql.append(", t1.FYearDebitQty - isnull(t2.YearDebitQty,0) FYearDebitQty ");
	    }
	    if (isShowFor()) {
	      tempSql.append(", t1.FYearDebitFor - isnull(t2.YearDebitFor,0) FYearDebitFor");
	    }
	    if (isShowLocal()) {
	      tempSql.append(", t1.FYearDebitLocal - isnull(t2.YearDebitLocal,0) FYearDebitLocal");
	    }
	    if (isShowRpt()) {
	      tempSql.append(", t1.FYearDebitRpt - isnull(t2.YearDebitRpt,0) FYearDebitRpt");
	    }
	    if (isShowQty()) {
	      tempSql.append(", t1.FYearCreditQty - isnull(t2.YearCreditQty,0) FYearCreditQty ");
	    }
	    if (isShowFor()) {
	      tempSql.append(", t1.FYearCreditFor - isnull(t2.YearCreditFor,0) FYearCreditFor\t");
	    }
	    if (isShowLocal()) {
	      tempSql.append(", t1.FYearCreditLocal - isnull(t2.YearCreditLocal,0) FYearCreditLocal ");
	    }
	    if (isShowRpt()) {
	      tempSql.append(", t1.FYearCreditRpt - isnull(t2.YearCreditRpt,0) FYearCreditRpt ");
	    }
	    if (isShowQty()) {
	      tempSql.append(", t1.FEndQty + isnull(t2.CreditQty,0) - isnull(t2.DebitQty, 0) FEndQty ");
	    }
	    if (isShowFor()) {
	      tempSql.append(", t1.FEndBalanceFor + isnull(t2.CreditFor,0) - isnull(t2.DebitFor,0) FEndBalanceFor ");
	    }
	    if (isShowLocal()) {
	      tempSql.append(", t1.FEndBalanceLocal + isnull(t2.CreditLocal,0) - isnull(t2.DebitLocal,0) FEndBalanceLocal ");
	    }
	    if (isShowRpt()) {
	      tempSql.append(", t1.FEndBalanceRpt + isnull(t2.CreditRpt,0) - isnull(t2.DebitRpt,0) FEndBalanceRpt ");
	    }
	    tempSql.append(" from (").append(sqlToBeExcute).append(") t1 ");
	    tempSql.append(" left outer join ").append(plVoucherTempTable).append(" t2 on t1.FAccountID = t2.accountID ");
	    if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	      tempSql.append(" and t1.forgunitid = t2.companyid");
	    }
	    if (this.cond.getDisplayAsstDetail()) {
	      tempSql.append(" and t1.FAssistGrpID = t2.hgid ");
	    }
	    if (isAllCurrency()) {
	      tempSql.append(" and t1.fcurrencyid = t2.currencyid ");
	    }
	    if (where.length() > 0)
	    {
	      tempSql.append(") tpnl ");
	      tempSql.append(where);
	    }
	    return tempSql.toString();
	  }
	  
	  protected void insertCurrencySum()
	    throws EASBizException, BOSException
	  {
	    StringBuffer sql = new StringBuffer();
	    sql.append("SELECT TP.FAccountID");
	    if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) {
	      sql.append(", TP.accountNumber, TP.accountName");
	    }
	    sql.append(", TP.FIsAccountTotal ");
	    if (this.cond.getIncludeBWAccount())
	    {
	      sql.append(" ,TP.FBWType ");
	      sql.append(" ,TP.FIsAccountBWSum ");
	    }
	    sql.append(",' ' FCurrencyID, 1 FIsCurrencyTotal ");
	    if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	      sql.append(",TP.FOrgUnitID ");
	    }
	    if (isShowQty())
	    {
	      sql.append(", '' FMeasureUnitID ");
	      sql.append(", '' measureUnit ");
	    }
	    if (this.cond.getDisplayAsstDetail()) {
	      sql.append(",0 FIsAssist,' ' FAssistGrpID ");
	    }
	    if (isShowQty()) {
	      sql.append(",SUM(FBeginQty) FBeginQty");
	    }
	    if (isShowFor()) {
	      sql.append(",0 FBeginBalanceFor");
	    }
	    if (isShowLocal()) {
	      sql.append(",SUM(ISNULL(FBeginBalanceLocal,0)) FBeginBalanceLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(",SUM(ISNULL(FBeginBalanceRpt,0)) FBeginBalanceRpt");
	    }
	    if (isShowQty()) {
	      sql.append(",SUM(FDebitQty) FDebitQty");
	    }
	    if (isShowFor()) {
	      sql.append(",0 FDebitFor");
	    }
	    if (isShowLocal()) {
	      sql.append(",SUM(ISNULL(FDebitLocal,0)) FDebitLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(",SUM(ISNULL(FDebitRpt,0)) FDebitRPT");
	    }
	    if (isShowQty()) {
	      sql.append(",SUM(FCreditQty) FCreditQty");
	    }
	    if (isShowFor()) {
	      sql.append(",0 FCreditFor");
	    }
	    if (isShowLocal()) {
	      sql.append(",SUM(ISNULL(FCreditLocal,0)) FCreditLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(",SUM(ISNULL(FCreditRpt,0)) FCreditRPT");
	    }
	    if (isShowQty()) {
	      sql.append(",SUM(FYearDebitQty) FYearDebitQty");
	    }
	    if (isShowFor()) {
	      sql.append(",0 FYearDebitFor");
	    }
	    if (isShowLocal()) {
	      sql.append(",SUM(ISNULL(FYearDebitLocal,0)) FYearDebitLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(",SUM(ISNULL(FYearDebitRpt,0)) FYearDebitRPT");
	    }
	    if (isShowQty()) {
	      sql.append(",SUM(FYearCreditQty) FYearCreditQty");
	    }
	    if (isShowFor()) {
	      sql.append(",0 FYearCreditFor");
	    }
	    if (isShowLocal()) {
	      sql.append(",SUM(ISNULL(FYearCreditLocal,0)) FYearCreditLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(",SUM(ISNULL(FYearCreditRpt,0)) FYearCreditRpt ");
	    }
	    if (isShowQty()) {
	      sql.append(",SUM(FEndQty) FEndQty");
	    }
	    if (isShowFor()) {
	      sql.append(",0 FEndBalanceFor");
	    }
	    if (isShowLocal()) {
	      sql.append(",SUM(ISNULL(FEndBalanceLocal,0)) FEndBalanceLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(",SUM(ISNULL(FEndBalanceRpt,0)) FEndBalanceRpt");
	    }
	    sql.append(" FROM ").append(getDataSourceTableName()).append(" TP ");
	    if (isAllCurrency()) {
	      sql.append(" where TP.FCurrencyID is not null ");
	    }
	    sql.append(" group by TP.FIsAccountTotal, TP.FAccountID ");
	    if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) {
	      sql.append(", TP.accountNumber, TP.accountName");
	    }
	    if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	      sql.append(",TP.FOrgUnitID ");
	    }
	    if (this.cond.getIncludeBWAccount()) {
	      sql.append(",TP.FBWType,TP.FIsAccountBWSum ");
	    }
	    insertData(sql.toString());
	  }
	  
	  protected void insertCompanySum()
	    throws EASBizException, BOSException
	  {
	    StringBuffer sql = new StringBuffer();
	    sql.append(" SELECT ");
	    if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) {
	      sql.append(" '' FAccountID, TP.accountNumber, TP.accountName");
	    } else {
	      sql.append(" PAV.FID FAccountID");
	    }
	    sql.append(", TP.FIsAccountTotal ");
	    if (this.cond.getIncludeBWAccount())
	    {
	      sql.append(" ,TP.FBWType ");
	      sql.append(" ,TP.FIsAccountBWSum ");
	    }
	    if (isAllCurrency()) {
	      sql.append(",TP.FCurrencyID, TP.FIsCurrencyTotal ");
	    }
	    if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) {
	      sql.append(",'").append("00000000-0000-0000-0000-000000000000CCE7AED4").append("'");
	    } else if (CompanyDisplayModeEnum.details == this.cond.getCompanyDisplayMode()) {
	      sql.append(", '").append(this.cond.getCompany().getId().toString()).append("'");
	    } else if (CompanyDisplayModeEnum.level == this.cond.getCompanyDisplayMode()) {
	      sql.append(",cp.FParentID ");
	    }
	    sql.append(" FOrgUnitID ");
	    if (this.cond.getDisplayAsstDetail()) {
	      sql.append(",0 FIsAssist,' ' FAssistGrpID ");
	    }
	    if (isShowQty())
	    {
	      sql.append(", '' FMeasureUnitID ");
	      sql.append(", '' measureUnit ");
	    }
	    if (isShowQty()) {
	      if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) {
	        sql.append(",0 FBeginQty");
	      } else {
	        sql.append(",sum(FBeginQty) FBeginQty");
	      }
	    }
	    if (isShowFor()) {
	      sql.append(",SUM(FBeginBalanceFor) FBeginBalanceFor");
	    }
	    if (isShowLocal()) {
	      sql.append(",SUM(FBeginBalanceLocal) FBeginBalanceLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(",SUM(FBeginBalanceRpt) FBeginBalanceRpt");
	    }
	    if (isShowQty()) {
	      if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) {
	        sql.append(",0 FDebitQty");
	      } else {
	        sql.append(",sum(FDebitQty) FDebitQty");
	      }
	    }
	    if (isShowFor()) {
	      sql.append(",SUM(FDebitFor) FDebitFor");
	    }
	    if (isShowLocal()) {
	      sql.append(",SUM(FDebitLocal) FDebitLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(",SUM(FDebitRpt) FDebitRpt");
	    }
	    if (isShowQty()) {
	      if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) {
	        sql.append(",0 FCreditQty");
	      } else {
	        sql.append(",sum(FCreditQty) FCreditQty");
	      }
	    }
	    if (isShowFor()) {
	      sql.append(",SUM(FCreditFor) FCreditFor");
	    }
	    if (isShowLocal()) {
	      sql.append(",SUM(FCreditLocal) FCreditLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(",SUM(FCreditRpt) FCreditRpt");
	    }
	    if (isShowQty()) {
	      if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) {
	        sql.append(",0 FYearDebitQty");
	      } else {
	        sql.append(",sum(FYearDebitQty) FYearDebitQty");
	      }
	    }
	    if (isShowFor()) {
	      sql.append(",SUM(FYearDebitFor) FYearDebitFor");
	    }
	    if (isShowLocal()) {
	      sql.append(",SUM(FYearDebitLocal) FYearDebitLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(",SUM(FYearDebitRpt) FYearDebitRpt");
	    }
	    if (isShowQty()) {
	      if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) {
	        sql.append(",0 FYearCreditQty");
	      } else {
	        sql.append(",sum(FYearCreditQty) FYearCreditQty");
	      }
	    }
	    if (isShowFor()) {
	      sql.append(",SUM(FYearCreditFor) FYearCreditFor");
	    }
	    if (isShowLocal()) {
	      sql.append(",SUM(FYearCreditLocal) FYearCreditLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(",SUM(FYearCreditRpt) FYearCreditRpt");
	    }
	    if (isShowQty()) {
	      if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) {
	        sql.append(",0 FEndQty");
	      } else {
	        sql.append(",sum(FEndQty) FEndQty");
	      }
	    }
	    if (isShowFor()) {
	      sql.append(",SUM(FEndBalanceFor) FEndBalanceFor");
	    }
	    if (isShowLocal()) {
	      sql.append(",SUM(FEndBalanceLocal) FEndBalanceLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(",SUM(FEndBalanceRpt) FEndBalanceRpt");
	    }
	    sql.append(" FROM ").append(getDataSourceTableName()).append(" TP ");
	    if (CompanyDisplayModeEnum.level == this.cond.getCompanyDisplayMode()) {
	      sql.append(" inner join t_org_company cp on cp.fid = tp.forgunitid ");
	    }
	    if (CompanyDisplayModeEnum.merger != this.cond.getCompanyDisplayMode())
	    {
	      sql.append(" left outer join t_bd_accountview AV on AV.fid = TP.FAccountID ");
	      sql.append(" left outer join t_bd_accountview PAV on AV.FNumber = PAV.FNumber and AV.FAccountTableID = PAV.FAccountTableID ");
	      if (CompanyDisplayModeEnum.level == this.cond.getCompanyDisplayMode()) {
	        sql.append(" and PAV.FCompanyID = cp.FParentID ");
	      } else if (CompanyDisplayModeEnum.details == this.cond.getCompanyDisplayMode()) {
	        sql.append(" and PAV.FCompanyID = '").append(this.cond.getCompany().getId().toString()).append("'");
	      }
	    }
	    if (CompanyDisplayModeEnum.level == this.cond.getCompanyDisplayMode()) {
	      sql.append(" where cp.flevel = ? ");
	    } else if (CompanyDisplayModeEnum.details == this.cond.getCompanyDisplayMode()) {
	      sql.append(" where TP.Forgunitid is not null ");
	    }
	    sql.append(" group by ");
	    if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) {
	      sql.append("TP.accountNumber, TP.accountName");
	    } else {
	      sql.append("PAV.FID ");
	    }
	    sql.append(", TP.FIsAccountTotal ");
	    if (isAllCurrency()) {
	      sql.append(",TP.FCurrencyID, TP.FIsCurrencyTotal ");
	    }
	    if (CompanyDisplayModeEnum.level == this.cond.getCompanyDisplayMode()) {
	      sql.append(",cp.FParentID ");
	    }
	    if (this.cond.getIncludeBWAccount()) {
	      sql.append(",TP.FBWType,TP.FIsAccountBWSum ");
	    }
	    if (CompanyDisplayModeEnum.level == this.cond.getCompanyDisplayMode())
	    {
	      int maxLevel = getMaxCompanyLevel();
	      for (int i = maxLevel; i > getCompany().getLevel(); i--)
	      {
	        List sp = new ArrayList();
	        sp.add(new Integer(i));
	        insertData(sql.toString(), sp);
	      }
	    }
	    else if ((CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) || (CompanyDisplayModeEnum.details == this.cond.getCompanyDisplayMode()))
	    {
	      insertData(sql.toString());
	    }
	  }
	  
	  protected boolean isCreateDataTempTable()
	    throws EASBizException, BOSException
	  {
	    return (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) || (this.cond.isAllCurrency()) || (this.cond.isNotIncluePLVoucher()) || (this.cond.getIncludeBWAccount()) || (this.cond.getPeriodYearStart() * 100 + this.cond.getPeriodNumberStart() > this.cond.getCurrPeriod().getNumber()) || (this.cond.isAfterCurrPeriod()) || (this.cond.isOptionAmountZero()) || (this.cond.isOptionBalanceZero()) || (this.cond.getOptionYearAmountZero()) || (this.cond.getOptionYearAmountAndBalZero()) || (this.cond.isOptionAmountAndBalZero()) || (this.cond.getOptionDCDispatchAsst());
	  }
	  
	  protected GLRptPageQuery getGLRptDataQuery()
	    throws EASBizException, BOSException
	  {
	    return new GLRptAccountBalPageQuery(getContex(), this.cond, isCreateDataTempTable(), getQueryHelper());
	  }
	  
	  
	  public GLRptTableHeadInfo initTableHeadInfo()
	    throws BOSException, EASBizException
	  {
	    GLRptTableHeadInfo headInfo = new GLRptTableHeadInfo();
	    addTableHeadColumn(headInfo, new GLRptField("KSQL_SEQ", "INT"));
	    if (!isCreateSeqTable()) {
	      addTableHeadColumn(headInfo, new GLRptField("SQL_BATCH", "INT"));
	    }
	    GLRptSelector selector = getGLRptSelector();
	    if (getGLRptSelector() == null) {
	      return headInfo;
	    }
	    Iterator it = selector.iterator();
	    List creditList = new ArrayList();
	    String lastPrefix = null;
	    while (it.hasNext())
	    {
	      GLRptField field = (GLRptField)it.next();
	      if ((!creditList.isEmpty()) && (!field.getName().startsWith(lastPrefix)))
	      {
	        for (int i = 0; i < creditList.size(); i++) {
	          addTableHeadCreditColumn(headInfo, (GLRptField)creditList.get(i));
	        }
	        creditList.clear();
	      }
	      if (field.isYearBegin())
	      {
	        creditList.add(field);
	        lastPrefix = "FYearBegin";
	      }
	      else if (field.isBegin())
	      {
	        creditList.add(field);
	        lastPrefix = "FBegin";
	      }
	      else if (field.isEnd())
	      {
	        creditList.add(field);
	        lastPrefix = "FEnd";
	      }
	      else
	      {
	        lastPrefix = null;
	      }
	      addTableHeadColumn(headInfo, field);
	    }
	    if (!creditList.isEmpty())
	    {
	      for (int i = 0; i < creditList.size(); i++) {
	        addTableHeadCreditColumn(headInfo, (GLRptField)creditList.get(i));
	      }
	      creditList.clear();
	    }
	    headInfo.setHeadTitle(getHeadTitles());
	    return headInfo;
	  }
	  
	  protected String[][] getHeadTitles()
	    throws EASBizException, BOSException
	  {
	    String[][] titles = (String[][])null;
	    if ((!this.cond.isShowQty()) && (isSingleAmountFiled()))
	    {
	      titles = new String[2][];
	      titles[0] = getTitleRow1();
	      titles[1] = getTitleRow2();
	    }
	    else
	    {
	      titles = new String[3][];
	      titles[0] = getTitleRow1();
	      titles[1] = getTitleRow2();
	      titles[2] = getTitleRow3();
	    }
	    return titles;
	  }
	  
	  protected void addTableHeadCreditColumn(GLRptTableHeadInfo headInfo, GLRptField field)
	    throws EASBizException, BOSException
	  {
	    String prefix = null;
	    if (field.isYearBegin()) {
	      prefix = "FYearBegin";
	    } else if (field.isBegin()) {
	      prefix = "FBegin";
	    } else if (field.isEnd()) {
	      prefix = "FEnd";
	    }
	    GLRptTableHeadInfo.GLRptTableColumnInfo colInfo = headInfo.addColumnInfo();
	    colInfo.setNumeral(true);
	    colInfo.setColName(prefix + "Credit" + field.getBalSuffix());
	    colInfo.setWidth(field.getLength());
	    if (field.isQty())
	    {
	      colInfo.setScale(this.cond.getQtyScale());
	    }
	    else
	    {
	      CurrencyInfo currency = getQueryCurrencyInfo();
	      if ((currency != null) && (field.getName().endsWith("For"))) {
	        colInfo.setScale(currency.getPrecision());
	      } else if (field.getName().endsWith("Local")) {
	        colInfo.setScale(getCompany().getBaseCurrency().getPrecision());
	      } else if ((getCompany().getReportCurrency() != null) && (field.getName().endsWith("Rpt"))) {
	        colInfo.setScale(getCompany().getReportCurrency().getPrecision());
	      }
	    }
	    if ((colInfo != null) && (getFreezeField() == field)) {
	      colInfo.setFreeze(true);
	    }
	    if ((colInfo != null) && (getFreezeField() == field)) {
	      colInfo.setFreeze(true);
	    }
	  }
	  
	  protected void addTableHeadColumn(GLRptTableHeadInfo headInfo, GLRptField field)
	    throws EASBizException, BOSException
	  {
	    if ((field.isYearBegin()) || (field.isBegin()) || (field.isEnd()))
	    {
	      String prefix = null;
	      if (field.isYearBegin()) {
	        prefix = "FYearBegin";
	      } else if (field.isBegin()) {
	        prefix = "FBegin";
	      } else if (field.isEnd()) {
	        prefix = "FEnd";
	      }
	      GLRptTableHeadInfo.GLRptTableColumnInfo colInfo = headInfo.addColumnInfo();
	      colInfo.setNumeral(true);
	      colInfo.setColName(prefix + "Debit" + field.getBalSuffix());
	      colInfo.setWidth(field.getLength());
	      if (field.isQty())
	      {
	        colInfo.setScale(this.cond.getQtyScale());
	      }
	      else
	      {
	        CurrencyInfo currency = getQueryCurrencyInfo();
	        if ((currency != null) && (field.getName().endsWith("For"))) {
	          colInfo.setScale(currency.getPrecision());
	        } else if (field.getName().endsWith("Local")) {
	          colInfo.setScale(getCompany().getBaseCurrency().getPrecision());
	        } else if ((getCompany().getReportCurrency() != null) && (field.getName().endsWith("Rpt"))) {
	          colInfo.setScale(getCompany().getReportCurrency().getPrecision());
	        }
	      }
	      if ((colInfo != null) && (getFreezeField() == field)) {
	        colInfo.setFreeze(true);
	      }
	    }
	    else if (GLRptField.HGName == field)
	    {
	      for (int i = 0; i < this.assistCount; i++)
	      {
	        GLRptTableHeadInfo.GLRptTableColumnInfo colInfo = headInfo.addColumnInfo();
	        colInfo.setColName("FAsstAccountName" + i);
	        colInfo.setWidth(120);
	        if ((colInfo != null) && (getFreezeField() == field)) {
	          colInfo.setFreeze(true);
	        }
	      }
	    }
	    else if (GLRptField.accountDC == field)
	    {
	      GLRptTableHeadInfo.GLRptTableColumnInfo colInfo = headInfo.addColumnInfo();
	      colInfo.setColName(field.getName());
	      colInfo.setWidth(-1);
	    }
	    else
	    {
	      super.addTableHeadColumn(headInfo, field);
	    }
	  }
	  
	  protected void setRow1Title(List colTitles, GLRptField field)
	  {
	    if ((field.isYearBegin()) || (field.isBegin()) || (field.isEnd()))
	    {
	      colTitles.add(field.getTitle());
	      colTitles.add(field.getTitle());
	    }
	    else if ((field.isDebit()) || (field.isCredit()))
	    {
	      colTitles.add("period_happened");
	    }
	    else if ((field.isYearDebit()) || (field.isYearCredit()))
	    {
	      colTitles.add("sum_year");
	    }
	    else if (GLRptField.HGName == field)
	    {
	      for (int i = 0; i < this.assistCount; i++) {
	        colTitles.add(field.getTitle());
	      }
	    }
	    else
	    {
	      colTitles.add(field.getTitle());
	    }
	  }
	  
	  protected String[] getTitleRow2()
	    throws EASBizException, BOSException
	  {
	    List colTitles = new ArrayList();
	    colTitles.add("KSQL_SEQ");
	    if (!isCreateSeqTable()) {
	      colTitles.add("SQL_BATCH");
	    }
	    Iterator it = getGLRptSelector().iterator();
	    List creditList = new ArrayList();
	    String lastPrefix = null;
	    while (it.hasNext())
	    {
	      GLRptField field = (GLRptField)it.next();
	      if (field.getName() != null)
	      {
	        if ((!creditList.isEmpty()) && (!field.getName().startsWith(lastPrefix)))
	        {
	          for (int i = 0; i < creditList.size(); i++) {
	            colTitles.add("dir_credit");
	          }
	          creditList.clear();
	        }
	        if (field.isYearBegin())
	        {
	          creditList.add(field);
	          lastPrefix = "FYearBegin";
	        }
	        else if (field.isBegin())
	        {
	          creditList.add(field);
	          lastPrefix = "FBegin";
	        }
	        else if (field.isEnd())
	        {
	          creditList.add(field);
	          lastPrefix = "FEnd";
	        }
	        else
	        {
	          lastPrefix = null;
	        }
	        setRow2Title(colTitles, field);
	      }
	    }
	    if (!creditList.isEmpty())
	    {
	      for (int i = 0; i < creditList.size(); i++) {
	        colTitles.add("dir_credit");
	      }
	      creditList.clear();
	    }
	    String[] titles = new String[colTitles.size()];
	    colTitles.toArray(titles);
	    return titles;
	  }
	  
	  protected void setRow2Title(List colTitles, GLRptField field)
	    throws EASBizException, BOSException
	  {
	    if ((field.isYearBegin()) || (field.isBegin()) || (field.isEnd())) {
	      colTitles.add("dir_debit");
	    } else if (GLRptField.HGName == field) {
	      for (int i = 0; i < this.assistCount; i++) {
	        colTitles.add(field.getTitle());
	      }
	    } else if (field.isYearDebit()) {
	      colTitles.add("dir_debit");
	    } else if (field.isYearCredit()) {
	      colTitles.add("dir_credit");
	    } else {
	      colTitles.add(field.getTitle());
	    }
	  }
	  
	  protected String[] getTitleRow3()
	    throws EASBizException, BOSException
	  {
	    List colTitles = new ArrayList();
	    colTitles.add("KSQL_SEQ");
	    if (!isCreateSeqTable()) {
	      colTitles.add("SQL_BATCH");
	    }
	    Iterator it = getGLRptSelector().iterator();
	    List creditList = new ArrayList();
	    String lastPrefix = null;
	    while (it.hasNext())
	    {
	      GLRptField field = (GLRptField)it.next();
	      if (field.getName() != null)
	      {
	        if ((!creditList.isEmpty()) && (!field.getName().startsWith(lastPrefix)))
	        {
	          for (int i = 0; i < creditList.size(); i++) {
	            setRow3Title(colTitles, (GLRptField)creditList.get(i));
	          }
	          creditList.clear();
	        }
	        if (field.isYearBegin())
	        {
	          creditList.add(field);
	          lastPrefix = "FYearBegin";
	        }
	        else if (field.isBegin())
	        {
	          creditList.add(field);
	          lastPrefix = "FBegin";
	        }
	        else if (field.isEnd())
	        {
	          creditList.add(field);
	          lastPrefix = "FEnd";
	        }
	        else
	        {
	          lastPrefix = null;
	        }
	        setRow3Title(colTitles, field);
	      }
	    }
	    if (!creditList.isEmpty())
	    {
	      for (int i = 0; i < creditList.size(); i++) {
	        setRow3Title(colTitles, (GLRptField)creditList.get(i));
	      }
	      creditList.clear();
	    }
	    String[] titles = new String[colTitles.size()];
	    colTitles.toArray(titles);
	    return titles;
	  }
	  
	  protected void setRow3Title(List colTitles, GLRptField field)
	    throws EASBizException, BOSException
	  {
	    if (field.getType() == "NUMERIC(29,10)")
	    {
	      if (field.isQty()) {
	        colTitles.add("asst_quantity");
	      } else if (isSingleAmountFiled()) {
	        colTitles.add("money_amount");
	      } else if (field.getName().endsWith("For")) {
	        colTitles.add("currency_for");
	      } else if (field.getName().endsWith("Local")) {
	        colTitles.add("currency_local");
	      } else if (field.getName().endsWith("Rpt")) {
	        colTitles.add("currency_rpt");
	      }
	    }
	    else if (GLRptField.HGName == field) {
	      for (int i = 0; i < this.assistCount; i++) {
	        colTitles.add(field.getTitle());
	      }
	    } else {
	      colTitles.add(field.getTitle());
	    }
	  }
	  
	  protected GLRptSelector initSelector()
	    throws EASBizException, BOSException
	  {
	    GLRptSelector selector = new GLRptSelector();
	    selector.add(GLRptField.accountID);
	    if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode())
	    {
	      selector.add(GLRptField.accountNumberMerger);
	      selector.add(this.cond.getRptParams().isAccountShowLongName() ? GLRptField.accountDisplayNameMerger : GLRptField.accountNameMerger);
	    }
	    else
	    {
	      selector.add(GLRptField.accountNumber);
	      selector.add(this.cond.getRptParams().isAccountShowLongName() ? GLRptField.accountDisplayName : GLRptField.accountName);
	    }
	    selector.add(GLRptField.accountDC);
	    selector.add(new GLRptField("FIsAccountTotal", "t.FIsAccountTotal", "FIsAccountTotal", "INT"));
	    if (this.cond.getIncludeBWAccount())
	    {
	      selector.add(new GLRptField("FBWType", "t.FBWType", "FBWType", "INT"));
	      
	      selector.add(new GLRptField("FIsAccountBWSum", "t.FIsAccountBWSum", "FIsAccountBWSum", "INT"));
	    }
	    if (isAllCurrency())
	    {
	      selector.add(GLRptField.currencyID);
	      selector.add(GLRptField.currencyName);
	      selector.add(GLRptField.currencyPre);
	      
	      selector.add(new GLRptField("FIsCurrencyTotal", "t.FIsCurrencyTotal", "FIsCurrencyTotal", "INT"));
	    }
	    if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode())
	    {
	      selector.add(GLRptField.orgUnitID);
	      selector.add(GLRptField.orgUnitName);
	    }
	    if (this.cond.getDisplayAsstDetail())
	    {
	      selector.add(new GLRptField("FIsAssist", "t.FIsAssist", "FIsAssist", "INT"));
	      selector.add(GLRptField.HG);
	      selector.add(GLRptField.HGName);
	    }
	    if (isShowQty())
	    {
	      selector.add(GLRptField.measureUnitID);
	      selector.add(GLRptField.accountMeasureUnitName);
	      selector.add(GLRptField.yearBeginQty);
	    }
	    if (isShowFor()) {
	      selector.add(GLRptField.yearBeginBalanceFor);
	    }
	    if (isShowLocal()) {
	      selector.add(GLRptField.yearBeginBalanceLocal);
	    }
	    if (isShowRpt()) {
	      selector.add(GLRptField.yearBeginBalanceRpt);
	    }
	    if (isShowQty()) {
	      selector.add(GLRptField.beginQty);
	    }
	    if (isShowFor()) {
	      selector.add(GLRptField.beginBalanceFor);
	    }
	    if (isShowLocal()) {
	      selector.add(GLRptField.beginBalanceLocal);
	    }
	    if (isShowRpt()) {
	      selector.add(GLRptField.beginBalanceRpt);
	    }
	    if (isShowQty()) {
	      selector.add(GLRptField.debitQty);
	    }
	    if (isShowFor()) {
	      selector.add(GLRptField.debitFor);
	    }
	    if (isShowLocal()) {
	      selector.add(GLRptField.debitLocal);
	    }
	    if (isShowRpt()) {
	      selector.add(GLRptField.debitRpt);
	    }
	    if (isShowQty()) {
	      selector.add(GLRptField.creditQty);
	    }
	    if (isShowFor()) {
	      selector.add(GLRptField.creditFor);
	    }
	    if (isShowLocal()) {
	      selector.add(GLRptField.creditLocal);
	    }
	    if (isShowRpt()) {
	      selector.add(GLRptField.creditRpt);
	    }
	    if (isShowQty()) {
	      selector.add(GLRptField.yearDebitQty);
	    }
	    if (isShowFor()) {
	      selector.add(GLRptField.yearDebitFor);
	    }
	    if (isShowLocal()) {
	      selector.add(GLRptField.yearDebitLocal);
	    }
	    if (isShowRpt()) {
	      selector.add(GLRptField.yearDebitRpt);
	    }
	    if (isShowQty()) {
	      selector.add(GLRptField.yearCreditQty);
	    }
	    if (isShowFor()) {
	      selector.add(GLRptField.yearCreditFor);
	    }
	    if (isShowLocal()) {
	      selector.add(GLRptField.yearCreditLocal);
	    }
	    if (isShowRpt()) {
	      selector.add(GLRptField.yearCreditRpt);
	    }
	    if (isShowQty()) {
	      selector.add(GLRptField.endQty);
	    }
	    if (isShowFor()) {
	      selector.add(GLRptField.endBalanceFor);
	    }
	    if (isShowLocal()) {
	      selector.add(GLRptField.endBalanceLocal);
	    }
	    if (isShowRpt()) {
	      selector.add(GLRptField.endBalanceRpt);
	    }
	    return selector;
	  }
	  
	  protected GLRptField getFreezeField()
	  {
	    if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	      return GLRptField.orgUnitName;
	    }
	    if (isAllCurrency()) {
	      return GLRptField.currencyName;
	    }
	    return GLRptField.accountName;
	  }
	  
	  public GLRptField getTreeColmun()
	  {
	    if (CompanyDisplayModeEnum.enumerate != this.cond.getCompanyDisplayMode()) {
	      return GLRptField.orgUnitName;
	    }
	    return null;
	  }
	  
	  public IGLRptIDCreator getGLRptIDCreator(String mainTableAlias, GLRptField field)
	  {
	    if ((CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) || (CompanyDisplayModeEnum.details == this.cond.getCompanyDisplayMode())) {
	      return new GLRptAccountBalanceIDCreator(mainTableAlias, this.cond);
	    }
	    return new DefaultGLRptIDCreator(mainTableAlias, this.cond);
	  }
	  
	  protected int getExpandLevel()
	  {
	    if ((CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) || (CompanyDisplayModeEnum.details == this.cond.getCompanyDisplayMode()))
	    {
	      if (this.cond.isDisplayLeafCompany()) {
	        return 1;
	      }
	      return 0;
	    }
	    return super.getExpandLevel();
	  }
	  
	  private String getAccountName()
	    throws EASBizException, BOSException
	  {
	    if (this.cond.getRptParams().isAccountShowLongName()) {
	      return "FdisplayName_" + getContex().getLocale();
	    }
	    return "FName_" + getContex().getLocale();
	  }
	  
	  private boolean isNeedQty(String companyId)
	  {
	    return CompanyDisplayModeEnum.merger != this.cond.getCompanyDisplayMode();
	  }
	  
	  private AccountViewCollection getAccountInfoByTempTable(String companyIds)
	    throws BOSException, EASBizException
	  {
	    EntityViewInfo view = new EntityViewInfo();
	    view.getSelector().add(new SelectorItemInfo("id"));
	    view.getSelector().add(new SelectorItemInfo("longNumber"));
	    FilterInfo filter = new FilterInfo();
	    if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) {
	      filter.getFilterItems().add(new FilterItemInfo("companyID.id", this.cond.getCompanyIds(), CompareType.INCLUDE));
	    } else if (companyIds != null) {
	      filter.getFilterItems().add(new FilterItemInfo("companyID.id", companyIds));
	    } else {
	      filter.getFilterItems().add(new FilterItemInfo("companyID.id", getCompany().getId().toString()));
	    }
	    filter.getFilterItems().add(new FilterItemInfo("accountTableID.id", this.cond.getAccountTableId()));
	    filter.getFilterItems().add(new FilterItemInfo("id", "select faccountid from " + getDataSourceTableName(), CompareType.INNER));
	    view.setFilter(filter);
	    
	    view.getSorter().add(new SorterItemInfo("companyID.id"));
	    
	    view.getSorter().add(new SorterItemInfo("longNumber"));
	    return AccountViewFactory.getLocalInstance(getContex()).getAccountViewCollection(view);
	  }
	  
	  private Set getAccountIdSet4Sum(String companyIds)
	    throws EASBizException, BOSException
	  {
	    AccountViewCollection accounts = getAccountInfoByTempTable(companyIds);
	    if ((accounts == null) || (accounts.size() == 0)) {
	      return null;
	    }
	    Iterator it = accounts.iterator();
	    
	    List paccounts = new ArrayList();
	    
	    this.accountIdSet4Sum = new HashSet();
	    while (it.hasNext())
	    {
	      AccountViewInfo account = (AccountViewInfo)it.next();
	      if (!paccounts.isEmpty())
	      {
	        Iterator pit = paccounts.iterator();
	        AccountViewInfo ac = (AccountViewInfo)pit.next();
	        if (!account.getCompanyID().getId().equals(ac.getCompanyID().getId()))
	        {
	          paccounts.add(0, account);
	          this.accountIdSet4Sum.add(account.getId().toString());
	        }
	        else if (account.getLongNumber().contains("!"))
	        {
	          if (!account.getLongNumber().startsWith(ac.getLongNumber() + "!"))
	          {
	            paccounts.add(0, account);
	            this.accountIdSet4Sum.add(account.getId().toString());
	          }
	        }
	        else
	        {
	          paccounts.add(0, account);
	          this.accountIdSet4Sum.add(account.getId().toString());
	        }
	      }
	      else
	      {
	        paccounts.add(account);
	        this.accountIdSet4Sum.add(account.getId().toString());
	      }
	    }
	    return this.accountIdSet4Sum;
	  }
	  
	  private String getAccountIdsStr4Sum(String companyIds)
	    throws BOSException, EASBizException
	  {
	    if (getAccountIdSet4Sum(companyIds).size() > 100)
	    {
	      this.accountTempTable4Sum = GLRptDaoUtil.createIdTempTable(getContex(), "accountId", getAccountIdSet4Sum(companyIds));
	      return "select accountId from " + this.accountTempTable4Sum;
	    }
	    StringBuffer accountIds = new StringBuffer();
	    Set accountIdSet = getAccountIdSet4Sum(companyIds);
	    if ((accountIdSet == null) || (accountIdSet.isEmpty())) {
	      return "''";
	    }
	    Iterator it = accountIdSet.iterator();
	    accountIds.append("'").append(it.next()).append("'");
	    while (it.hasNext()) {
	      accountIds.append(",'").append(it.next()).append("'");
	    }
	    return accountIds.toString();
	  }
	  
	  private String getAccountIdFilter4Sum(String companyIds)
	    throws EASBizException, BOSException
	  {
	    this.accountIdStr4Sum = getAccountIdsStr4Sum(companyIds);
	    
	    return this.accountIdStr4Sum;
	  }
	  
	  private IRowSet getAccountAmountForMerger(IRowSet rs, String currencyId, String companyId)
	    throws EASBizException, BOSException, SQLException
	  {
	    StringBuffer sql = new StringBuffer();
	    sql.append(" select 1 xxx");
	    
	    appendAmountTotalSumSelect(sql, currencyId, companyId);
	    sql.append(" FROM (").append(getDataSourceTableNameForMeger(rs, currencyId, companyId)).append(") TB ");
	    return executeQuery(sql.toString());
	  }
	  
	  private String getDataSourceTableNameForMeger(IRowSet rs, String currencyId, String companyId)
	    throws EASBizException, BOSException, SQLException
	  {
	    StringBuffer sql = new StringBuffer();
	    sql.append("select tg.accountnumber,tg.accountname ");
	    if (isShowQty()) {
	      if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) {
	        sql.append(",0 FBeginQty");
	      } else {
	        sql.append(",SUM(FBeginQty) FBeginQty");
	      }
	    }
	    if (isShowFor()) {
	      sql.append(",SUM(FBeginBalanceFor) FBeginBalanceFor");
	    }
	    if (isShowLocal()) {
	      sql.append(",SUM(FBeginBalanceLocal) FBeginBalanceLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(",SUM(FBeginBalanceRpt) FBeginBalanceRpt");
	    }
	    if (isShowQty()) {
	      if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) {
	        sql.append(",0 FDebitQty");
	      } else {
	        sql.append(",sum(FDebitQty) FDebitQty");
	      }
	    }
	    if (isShowFor()) {
	      sql.append(",SUM(FDebitFor) FDebitFor");
	    }
	    if (isShowLocal()) {
	      sql.append(",SUM(FDebitLocal) FDebitLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(",SUM(FDebitRpt) FDebitRpt");
	    }
	    if (isShowQty()) {
	      if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) {
	        sql.append(",0 FCreditQty");
	      } else {
	        sql.append(",sum(FCreditQty) FCreditQty");
	      }
	    }
	    if (isShowFor()) {
	      sql.append(",SUM(FCreditFor) FCreditFor");
	    }
	    if (isShowLocal()) {
	      sql.append(",SUM(FCreditLocal) FCreditLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(",SUM(FCreditRpt) FCreditRpt");
	    }
	    if (isShowQty()) {
	      if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) {
	        sql.append(",0 FYearDebitQty");
	      } else {
	        sql.append(",SUM(FYearDebitQty) FYearDebitQty");
	      }
	    }
	    if (isShowFor()) {
	      sql.append(",SUM(FYearDebitFor) FYearDebitFor");
	    }
	    if (isShowLocal()) {
	      sql.append(",SUM(FYearDebitLocal) FYearDebitLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(",SUM(FYearDebitRpt) FYearDebitRpt");
	    }
	    if (isShowQty()) {
	      if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) {
	        sql.append(",0 FYearCreditQty");
	      } else {
	        sql.append(",SUM(FYearCreditQty) FYearCreditQty");
	      }
	    }
	    if (isShowFor()) {
	      sql.append(",SUM(FYearCreditFor) FYearCreditFor");
	    }
	    if (isShowLocal()) {
	      sql.append(",SUM(FYearCreditLocal) FYearCreditLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(",SUM(FYearCreditRpt) FYearCreditRpt");
	    }
	    if (isShowQty()) {
	      if (CompanyDisplayModeEnum.merger == this.cond.getCompanyDisplayMode()) {
	        sql.append(",0 FEndQty");
	      } else {
	        sql.append(",sum(FEndQty) FEndQty");
	      }
	    }
	    if (isShowFor()) {
	      sql.append(",SUM(FEndBalanceFor) FEndBalanceFor");
	    }
	    if (isShowLocal()) {
	      sql.append(",SUM(FEndBalanceLocal) FEndBalanceLocal");
	    }
	    if (isShowRpt()) {
	      sql.append(",SUM(FEndBalanceRpt) FEndBalanceRpt");
	    }
	    sql.append(" from ").append(getDataSourceTableName()).append(" tg ");
	    sql.append(" INNER JOIN T_BD_AccountView av on tg.FAccountID = av.FID ");
	    if (isShowQty()) {
	      sql.append(" LEFT OUTER JOIN T_BD_MEASUREUNIT MU on MU.FID = av.FMeasureUnitID  ");
	    }
	    if ((this.cond.getDisplayAsstDetail()) && (this.cond.getOptionDCDispatchAsst()))
	    {
	      sql.append(" WHERE ((av.FCAA IS NULL AND FIsAssist = 0 and (av.fisleaf = 1 or av.flevel = ").append(new Integer(this.cond.getAccountLevelEnd())).append("))");
	      
	      sql.append(" OR (av.FCAA IS not NULL AND FIsAssist = 0 and av.FLevel = ").append(new Integer(this.cond.getAccountLevelEnd())).append(" and av.FIsLeaf = 0) ");
	      
	      sql.append(" OR (av.FCAA IS not NULL AND FIsAssist = 1 )) ");
	    }
	    else
	    {
	      if (this.cond.isShowLeafAccount()) {
	        sql.append(" WHERE ").append(" av.FIsLeaf = 1 ");
	      } else if (this.cond.getAccountLevelEnd() == 1) {
	        sql.append(" WHERE ").append(" av.flevel = 1 ");
	      } else {
	        sql.append(" WHERE av.fid in (").append(getAccountIdFilter4Sum(null)).append(")");
	      }
	      if (this.cond.getDisplayAsstDetail()) {
	        sql.append(" and tg.FIsAssist = 0 ");
	      }
	    }
	    if (isAllCurrency()) {
	      sql.append(" and tg.FCurrencyID is not null and tg.FCurrencyID <> ' ' ");
	    }
	    if (companyId.equals("00000000-0000-0000-0000-000000000000CCE7AED4")) {
	      sql.append(" and tg.FOrgUnitID <> '").append("00000000-0000-0000-0000-000000000000CCE7AED4").append("'");
	    } else {
	      sql.append(" and tg.FOrgUnitID = '").append(companyId).append("'");
	    }
	    if (isAllCurrency()) {
	      if ((currencyId != null) && (currencyId.trim().length() > 0)) {
	        sql.append(" and tg.FCurrencyID = '").append(currencyId).append("'");
	      } else {
	        sql.append(" and tg.FCurrencyID != ' '");
	      }
	    }
	    if (this.cond.getIncludeBWAccount())
	    {
	      int bwType = rs.getInt("FBWType");
	      if ((bwType == 0) || (bwType == 1)) {
	        sql.append(" and tg.FBWType = ").append(bwType);
	      }
	    }
	    sql.append(" group by tg.accountnumber,tg.accountname ");
	    return sql.toString();
	  }
	  
	  protected GLRptAccountBalQueryHelper getQueryHelper()
	  {
	    return new GLRptAccountBalQueryHelper(this.cond);
	  }
	  
	  protected PeriodInfo getCurrPeriod()
	    throws EASBizException, BOSException
	  {
	    return this.cond.getCurrPeriod();
	  }
	  
	  protected PeriodInfo getStartPeriod()
	    throws EASBizException, BOSException
	  {
	    return SystemStatusCtrolUtils.getStartPeriod(getContex(), SystemEnum.GENERALLEDGER, this.cond.getCompany());
	  }
	  
	  private void appendAmountTotalSumSelect(StringBuffer sql, String currencyId, String companyId)
	    throws EASBizException, BOSException
	  {
	    if (isShowQty()) {
	      if (isNeedQty(companyId)) {
	        appendYearBeginDebitTotal(sql, "Qty", "Qty");
	      } else {
	        sql.append(",0.0 FYearBeginDebitQty \r\n");
	      }
	    }
	    if (isShowFor()) {
	      if ((!isAllCurrency()) || (currencyId != null)) {
	        appendYearBeginDebitTotal(sql, "For", "BalanceFor");
	      } else {
	        sql.append(", 0.0  FYearBeginDebitBalanceFor     \r\n");
	      }
	    }
	    if (isShowLocal()) {
	      appendYearBeginDebitTotal(sql, "Local", "BalanceLocal");
	    }
	    if (isShowRpt()) {
	      appendYearBeginDebitTotal(sql, "Rpt", "BalanceRpt");
	    }
	    if (isShowQty()) {
	      if (isNeedQty(companyId)) {
	        appendYearBeginCreditTotal(sql, "Qty", "Qty");
	      } else {
	        sql.append(", 0.0 FYearBeginCreditQty \r\n");
	      }
	    }
	    if (isShowFor()) {
	      if ((!isAllCurrency()) || (currencyId != null)) {
	        appendYearBeginCreditTotal(sql, "For", "BalanceFor");
	      } else {
	        sql.append(", 0.0  FYearBeginCreditBalanceFor     \r\n");
	      }
	    }
	    if (isShowLocal()) {
	      appendYearBeginCreditTotal(sql, "Local", "BalanceLocal");
	    }
	    if (isShowRpt()) {
	      appendYearBeginCreditTotal(sql, "Rpt", "BalanceRpt");
	    }
	    if (isShowQty()) {
	      if (isNeedQty(companyId)) {
	        appendPeriodBeginDebitTotal(sql, "Qty", "Qty");
	      } else {
	        sql.append(", 0.0 FBeginDebitQty \r\n");
	      }
	    }
	    if (isShowFor()) {
	      if ((!isAllCurrency()) || (currencyId != null)) {
	        appendPeriodBeginDebitTotal(sql, "For", "BalanceFor");
	      } else {
	        sql.append(", 0.0  FBeginDebitBalanceFor     \r\n");
	      }
	    }
	    if (isShowLocal()) {
	      appendPeriodBeginDebitTotal(sql, "Local", "BalanceLocal");
	    }
	    if (isShowRpt()) {
	      appendPeriodBeginDebitTotal(sql, "Rpt", "BalanceRpt");
	    }
	    if (isShowQty()) {
	      if (isNeedQty(companyId)) {
	        appendPeriodBeginCreditTotal(sql, "Qty", "Qty");
	      } else {
	        sql.append(", 0.0 FBeginCreditQty \r\n");
	      }
	    }
	    if (isShowFor()) {
	      if ((!isAllCurrency()) || (currencyId != null)) {
	        appendPeriodBeginCreditTotal(sql, "For", "BalanceFor");
	      } else {
	        sql.append(", 0.0  FBeginCreditBalanceFor     \r\n");
	      }
	    }
	    if (isShowLocal()) {
	      appendPeriodBeginCreditTotal(sql, "Local", "BalanceLocal");
	    }
	    if (isShowRpt()) {
	      appendPeriodBeginCreditTotal(sql, "Rpt", "BalanceRpt");
	    }
	    if (isShowQty()) {
	      if (isNeedQty(companyId)) {
	        sql.append(",sum(TB.FDebitQty) FDebitQty       \r\n");
	      } else {
	        sql.append(", 0.0 FDebitQty \r\n");
	      }
	    }
	    if (isShowFor()) {
	      if ((!isAllCurrency()) || (currencyId != null)) {
	        sql.append(",sum(TB.FDebitFor) FDebitFor       \r\n");
	      } else {
	        sql.append(", 0.0  FDebitFor     \r\n");
	      }
	    }
	    if (isShowLocal()) {
	      sql.append(",sum(TB.FDebitLocal) FDebitLocal   \r\n");
	    }
	    if (isShowRpt()) {
	      sql.append(",sum(TB.FDebitRpt) FDebitRpt       \r\n");
	    }
	    if (isShowQty()) {
	      if (isNeedQty(companyId)) {
	        sql.append(",sum(TB.FCreditQty) FCreditQty\t   \r\n");
	      } else {
	        sql.append(", 0.0 FCreditQty \r\n");
	      }
	    }
	    if (isShowFor()) {
	      if ((!isAllCurrency()) || (currencyId != null)) {
	        sql.append(",sum(TB.FCreditFor) FCreditFor     \r\n");
	      } else {
	        sql.append(", 0.0  FCreditFor     \r\n");
	      }
	    }
	    if (isShowLocal()) {
	      sql.append(",sum(TB.FCreditLocal) FCreditLocal \r\n");
	    }
	    if (isShowRpt()) {
	      sql.append(",sum(TB.FCreditRpt) FCreditRpt     \r\n");
	    }
	    if (isShowQty()) {
	      if (isNeedQty(companyId)) {
	        sql.append(",sum(TB.FYearDebitQty) FYearDebitQty       \r\n");
	      } else {
	        sql.append(", 0.0 FYearDebitQty \r\n");
	      }
	    }
	    if (isShowFor()) {
	      if ((!isAllCurrency()) || (currencyId != null)) {
	        sql.append(",sum(TB.FYearDebitFor) FYearDebitFor       \r\n");
	      } else {
	        sql.append(", 0.0  FYearDebitFor     \r\n");
	      }
	    }
	    if (isShowLocal()) {
	      sql.append(",sum(TB.FYearDebitLocal) FYearDebitLocal   \r\n");
	    }
	    if (isShowRpt()) {
	      sql.append(",sum(TB.FYearDebitRpt) FYearDebitRpt       \r\n");
	    }
	    if (isShowQty()) {
	      if (isNeedQty(companyId)) {
	        sql.append(",sum(TB.FYearCreditQty) FYearCreditQty\t   \r\n");
	      } else {
	        sql.append(", 0.0 FYearCreditQty \r\n");
	      }
	    }
	    if (isShowFor()) {
	      if ((!isAllCurrency()) || (currencyId != null)) {
	        sql.append(",sum(TB.FYearCreditFor) FYearCreditFor     \r\n");
	      } else {
	        sql.append(", 0.0  FYearCreditFor     \r\n");
	      }
	    }
	    if (isShowLocal()) {
	      sql.append(",sum(TB.FYearCreditLocal) FYearCreditLocal \r\n");
	    }
	    if (isShowRpt()) {
	      sql.append(",sum(TB.FYearCreditRpt) FYearCreditRpt     \r\n");
	    }
	    if (isShowQty()) {
	      if (isNeedQty(companyId)) {
	        appendPeriodEndDebitTotal(sql, "Qty", "Qty");
	      } else {
	        sql.append(", 0.0 FEndDebitQty \r\n");
	      }
	    }
	    if (isShowFor()) {
	      if ((!isAllCurrency()) || (currencyId != null)) {
	        appendPeriodEndDebitTotal(sql, "For", "BalanceFor");
	      } else {
	        sql.append(", 0.0 FEndDebitBalanceFor ");
	      }
	    }
	    if (isShowLocal()) {
	      appendPeriodEndDebitTotal(sql, "Local", "BalanceLocal");
	    }
	    if (isShowRpt()) {
	      appendPeriodEndDebitTotal(sql, "Rpt", "BalanceRpt");
	    }
	    if (isShowQty()) {
	      if (isNeedQty(companyId)) {
	        appendPeriodEndCreditTotal(sql, "Qty", "Qty");
	      } else {
	        sql.append(", 0.0 FEndCreditQty \r\n");
	      }
	    }
	    if (isShowFor()) {
	      if ((!isAllCurrency()) || (currencyId != null)) {
	        appendPeriodEndCreditTotal(sql, "For", "BalanceFor");
	      } else {
	        sql.append(", 0.0 FEndCreditBalanceFor ");
	      }
	    }
	    if (isShowLocal()) {
	      appendPeriodEndCreditTotal(sql, "Local", "BalanceLocal");
	    }
	    if (isShowRpt()) {
	      appendPeriodEndCreditTotal(sql, "Rpt", "BalanceRpt");
	    }
	  }
	  
	  private void appendYearBeginDebitTotal(StringBuffer sql, String type, String suff)
	    throws EASBizException, BOSException
	  {
	    sql.append(", sum(CASE WHEN ");
	    sql.append(" TB.FEnd" + suff + "+TB.FYearCredit" + type + "-TB.FYearDebit" + type + " > 0 ");
	    sql.append(" THEN (TB.FEnd" + suff + "+TB.FYearCredit" + type + "-TB.FYearDebit" + type).append(")");
	    sql.append(" ELSE 0.0 END) FYearBeginDebit" + suff + "     \r\n");
	  }
	  
	  private void appendYearBeginCreditTotal(StringBuffer sql, String type, String suff)
	    throws EASBizException, BOSException
	  {
	    sql.append(", sum(CASE WHEN ");
	    sql.append(" TB.FEnd" + suff + "+TB.FYearCredit" + type + "-TB.FYearDebit" + type + " < 0 ");
	    sql.append(" THEN -1 * (TB.FEnd" + suff + "+TB.FYearCredit" + type + "-TB.FYearDebit" + type + ") ");
	    sql.append(" ELSE 0.0 END) FYearBeginCredit" + suff + "      \r\n");
	  }
	  
	  private void appendPeriodBeginDebitTotal(StringBuffer sql, String type, String suff)
	    throws EASBizException, BOSException
	  {
	    sql.append(" ,sum(CASE WHEN ");
	    sql.append(" TB.FBegin" + suff + " > 0 ");
	    sql.append(" THEN TB.FBegin" + suff);
	    sql.append(" ELSE 0.0 END) FBeginDebit" + suff + "     \r\n");
	  }
	  
	  private void appendPeriodBeginCreditTotal(StringBuffer sql, String type, String suff)
	    throws EASBizException, BOSException
	  {
	    sql.append(", sum(CASE WHEN ");
	    sql.append(" TB.FBegin" + suff + " < 0 ");
	    sql.append(" THEN -1 * TB.FBegin" + suff);
	    sql.append(" ELSE 0.0 END) FBeginCredit" + suff + "\t\r\n");
	  }
	  
	  private void appendPeriodEndDebitTotal(StringBuffer sql, String type, String suff)
	    throws EASBizException, BOSException
	  {
	    sql.append(", sum(CASE WHEN ");
	    sql.append(" TB.FEnd" + suff + ">0");
	    sql.append(" THEN TB.FEnd" + suff);
	    sql.append(" ELSE 0.0 END) FEndDebit" + suff + "    \r\n");
	  }
	  
	  private void appendPeriodEndCreditTotal(StringBuffer sql, String type, String suff)
	    throws EASBizException, BOSException
	  {
	    sql.append(", sum(CASE WHEN ");
	    sql.append(" TB.FEnd" + suff + "<0");
	    sql.append(" THEN -1*TB.FEnd" + suff);
	    sql.append(" ELSE 0.0 END) FEndCredit" + suff + "  \r\n");
	  }
	 
}
