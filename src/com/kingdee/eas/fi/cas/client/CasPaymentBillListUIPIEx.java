package com.kingdee.eas.fi.cas.client;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.ctrl.extendcontrols.KDBizPromptBox;
import com.kingdee.bos.ctrl.kdf.table.ICell;
import com.kingdee.bos.ctrl.kdf.table.IRow;
import com.kingdee.bos.ctrl.kdf.table.KDTSelectBlock;
import com.kingdee.bos.ctrl.kdf.table.KDTSelectManager;
import com.kingdee.bos.ctrl.kdf.table.KDTable;
import com.kingdee.bos.ctrl.swing.KDDatePicker;
import com.kingdee.bos.ctrl.swing.KDLabelContainer;
import com.kingdee.bos.ctrl.swing.KDToolBar;
import com.kingdee.bos.ctrl.swing.KDWorkButton;
import com.kingdee.bos.dao.ormapping.ObjectStringPK;
import com.kingdee.bos.dao.query.IQueryExecutor;
import com.kingdee.bos.dao.query.ISQLExecutor;
import com.kingdee.bos.dao.query.SQLExecutorFactory;
import com.kingdee.bos.metadata.IMetaDataPK;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.metadata.entity.FilterInfo;
import com.kingdee.bos.metadata.entity.FilterItemCollection;
import com.kingdee.bos.metadata.entity.FilterItemInfo;
import com.kingdee.bos.metadata.query.util.CompareType;
import com.kingdee.bos.ui.face.IUIFactory;
import com.kingdee.bos.ui.face.IUIWindow;
import com.kingdee.bos.ui.face.UIException;
import com.kingdee.bos.ui.face.UIFactory;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.eas.base.permission.UserInfo;
import com.kingdee.eas.basedata.org.CompanyOrgUnitFactory;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.basedata.org.ICompanyOrgUnit;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.common.client.OprtState;
import com.kingdee.eas.common.client.SysContext;
import com.kingdee.eas.common.client.UIContext;
import com.kingdee.eas.custom.BusinessFormOAFactory;
import com.kingdee.eas.custom.IBusinessFormOA;
import com.kingdee.eas.custom.mvp.approvalfiltering.CasPaymentFilterUIPIEx;
import com.kingdee.eas.custom.mvp.emergency.EmergencyrReportCollection;
import com.kingdee.eas.custom.mvp.emergency.EmergencyrReportFactory;
import com.kingdee.eas.custom.mvp.emergency.IEmergencyrReport;
import com.kingdee.eas.custom.mvp.examination.IRegionalApproval;
import com.kingdee.eas.custom.mvp.examination.RegionalApprovalCollection;
import com.kingdee.eas.custom.mvp.examination.RegionalApprovalFactory;
import com.kingdee.eas.custom.mvp.examination.RegionalApprovalInfo;
import com.kingdee.eas.custom.mvp.recordstatus.IRecordStatusTable;
import com.kingdee.eas.custom.mvp.recordstatus.RecordStatusTableFactory;
import com.kingdee.eas.custom.mvp.recordstatus.RecordStatusTableInfo;
import com.kingdee.eas.custom.util.UtilClass;
import com.kingdee.eas.fi.cas.BillStatusEnum;
import com.kingdee.eas.fi.cas.IPaymentBill;
import com.kingdee.eas.fi.cas.PaymentBillFactory;
import com.kingdee.eas.fi.cas.PaymentBillInfo;
import com.kingdee.eas.fm.common.client.FMFilterBaseUI;
import com.kingdee.eas.framework.client.FindDialog;
import com.kingdee.eas.framework.client.context.IMultiOrgSupport;
import com.kingdee.eas.util.client.EASResource;
import com.kingdee.eas.util.client.MsgBox;
import com.kingdee.eas.utilItem.LjzConfigUtil;
import com.kingdee.jdbc.rowset.IRowSet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CasPaymentBillListUIPIEx
  extends CasPaymentBillListUI
{
  private static final long serialVersionUID = -6187259467824645525L;
  private CasPaymentFilterUIPIEx filterUI;
  private String[] ids;
  boolean check;
  
  public CasPaymentBillListUIPIEx()
    throws Exception
  {
    KDWorkButton btnModifyDate = new KDWorkButton();
    btnModifyDate.setText("修改日期");
    btnModifyDate.setIcon(EASResource.getIcon("imgTbtn_time"));
    this.toolBar.add(btnModifyDate);
    btnModifyDate.setVisible(true);
    btnModifyDate.setEnabled(true);
    btnModifyDate.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        CasPaymentBillListUIPIEx.this.ShowWind();
      }
    });
    KDWorkButton btnEndDate = new KDWorkButton();
    btnEndDate.setText("修改预计结束日期");
    btnEndDate.setIcon(EASResource.getIcon("imgTbtn_time"));
    this.toolBar.add(btnEndDate);
    btnEndDate.setVisible(true);
    btnEndDate.setEnabled(true);
    btnEndDate.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        CasPaymentBillListUIPIEx.this.ShowWindEndDate();
      }
    });
  }
  
  public void onLoad()
    throws Exception
  {
    super.onLoad();
    
    KDWorkButton btnBack = new KDWorkButton();
    
    btnBack.setText("打回功能");
    this.toolBar.add(btnBack);
    btnBack.setVisible(true);
    btnBack.setEnabled(true);
    
    btnBack.addActionListener(new ActionListener()
    {
      public void actionPerformed(ActionEvent e)
      {
        try
        {
          CasPaymentBillListUIPIEx.this.actionBack_actionPerformed(e);
        }
        catch (Exception e1)
        {
          e1.printStackTrace();
        }
      }
    });
    UserInfo currentUserInfo = SysContext.getSysContext().getCurrentUserInfo();
    
    EmergencyrReportCollection emergencyrReportCollection = EmergencyrReportFactory.getRemoteInstance().getEmergencyrReportCollection(
      "where user='" + currentUserInfo.getId().toString() + "'");
    if (emergencyrReportCollection.size() != 0)
    {
      KDWorkButton btnEmergency = new KDWorkButton();
      
      btnEmergency.setText("紧急上报");
      this.toolBar.add(btnEmergency);
      btnEmergency.setVisible(true);
      btnEmergency.setEnabled(true);
      
      btnEmergency.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          try
          {
            CasPaymentBillListUIPIEx.this.actionEmergency_actionPerformed(e);
          }
          catch (Exception e1)
          {
            e1.printStackTrace();
          }
        }
      });
    }
  }
  
  public void actionAntiAudit_actionPerformed(ActionEvent e)
    throws Exception
  {
    checkSelected();
    
    List IDList = getSelectedIdValues();
    
    int showConfirm2 = MsgBox.showConfirm2(this, "是否反审批 " + IDList.size() + "条付款单");
    if (showConfirm2 == 0)
    {
      String baseUid = LjzConfigUtil.getValue("treeID", "jbYAAAL/4iNPKCf9");
      String sql = "select FLONGNUMBER,FUNITID from  T_ORG_Structure where ftreeid ='" + baseUid + "' and FUNITID = '" + 
        SysContext.getSysContext().getCurrentFIUnit().getId() + "'";
      IRowSet executeSQL = SQLExecutorFactory.getRemoteInstance(sql).executeSQL();
      if (executeSQL.next()) {
        for (int i = 0; i < IDList.size(); i++)
        {
          PaymentBillInfo paymentBillInfo = PaymentBillFactory.getRemoteInstance().getPaymentBillInfo(new ObjectStringPK(IDList.get(i).toString()));
          
          Object payStatus = paymentBillInfo.get("payStatus");
          if ("0".equals(payStatus))
          {
            MsgBox.showInfo("该单据不符合反审批条件 编码为: " + paymentBillInfo.getNumber());
            return;
          }
        }
      }
    }
    super.actionAntiAudit_actionPerformed(e);
  }
  
  public void actionCommitToBE_actionPerformed(ActionEvent e)
    throws Exception
  {
    checkSelected();
    
    List IDList = getSelectedIdValues();
    
    int showConfirm2 = MsgBox.showConfirm2(this, "是否提交银行付款单  " + IDList.size() + "条付款单");
    if (showConfirm2 == 0)
    {
      String baseUid = LjzConfigUtil.getValue("treeID", "jbYAAAL/4iNPKCf9");
      String sql = "select FLONGNUMBER,FUNITID from  T_ORG_Structure where ftreeid ='" + baseUid + "' and FUNITID = '" + 
        SysContext.getSysContext().getCurrentFIUnit().getId() + "'";
      IRowSet executeSQL = SQLExecutorFactory.getRemoteInstance(sql).executeSQL();
      if (executeSQL.next()) {
        for (int i = 0; i < IDList.size(); i++)
        {
          PaymentBillInfo paymentBillInfo = PaymentBillFactory.getRemoteInstance().getPaymentBillInfo(new ObjectStringPK(IDList.get(i).toString()));
          
          Object payStatus = paymentBillInfo.get("payStatus");
          if (!"0".equals(payStatus))
          {
            MsgBox.showInfo("该单据不符合提交银行付款单条件 编码为: " + paymentBillInfo.getNumber());
            return;
          }
        }
      }
    }
    Date nowData = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String currentDate = formatter.format(nowData);
    
    int errorCount = 0;
    StringBuffer msg = new StringBuffer();
    List<String> errorIds = new ArrayList();
    List<String> errorNos = new ArrayList();
    boolean dateFlag = true;
    IPaymentBill ibiz = PaymentBillFactory.getRemoteInstance();
    for (int j = 0; j < IDList.size(); j++)
    {
      PaymentBillInfo paymentBillInfo = ibiz.getPaymentBillInfo(new ObjectStringPK(IDList.get(j).toString()));
      if (!paymentBillInfo.getBizDate().toString().equals(currentDate))
      {
        errorCount++;
        errorIds.add(IDList.get(j).toString());
        errorNos.add(paymentBillInfo.getNumber());
      }
    }
    if (errorCount > 0)
    {
      StringBuffer sbr = new StringBuffer();
      String ids = "";
      IBusinessFormOA ibfOA = BusinessFormOAFactory.getRemoteInstance();
      Iterator it = errorIds.iterator();
      String fid = "";
      while (it.hasNext())
      {
        fid = (String)it.next();
        if (!ibfOA.IsExistDownstreamBill(fid)) {
          sbr.append("'").append(fid).append("'").append(",");
        }
      }
      if (sbr.length() > 0)
      {
        ids = sbr.substring(0, sbr.length() - 1);
        ibfOA.mobilePaymentBillBizDate(ids, currentDate, "cas");
      }
      ids = "";
      errorCount = 0;
      dateFlag = true;
    }
    if (dateFlag) {
      super.actionCommitToBE_actionPerformed(e);
    }
  }
  
  private String getBizDateByFID(String fid)
  {
    String bizDate = "";
    if ((fid != null) && (!"".equals(fid))) {
      bizDate = UtilClass.executeQueryString("select FBIZDATE from T_CAS_PaymentBill where fid ='" + fid + "'");
    }
    return bizDate;
  }
  
  private void actionEmergency_actionPerformed(ActionEvent e)
    throws Exception
  {
    checkSelected();
    
    List IDList = getSelectedIdValues();
    
    int showConfirm2 = MsgBox.showConfirm2(this, "是否紧急上报 " + IDList.size() + "条付款单");
    if (showConfirm2 == 0)
    {
      int count = 0;
      StringBuffer msg = new StringBuffer();
      for (int i = 0; i < IDList.size(); i++)
      {
        Object id = IDList.get(i);
        
        PaymentBillInfo paymentBillInfo = PaymentBillFactory.getRemoteInstance().getPaymentBillInfo(new ObjectStringPK(id.toString()));
        BillStatusEnum billStatus = paymentBillInfo.getBillStatus();
        if (billStatus.equals(BillStatusEnum.PAYED))
        {
          msg.append("单据编号: " + paymentBillInfo.getNumber() + " 付款单已付款.").append("\n");
        }
        else if (paymentBillInfo.getExpectPayEndDate() != null)
        {
          paymentBillInfo.setExpectPayEndDate(new Date());
          paymentBillInfo.put("payStatus", "0");
          
          PaymentBillFactory.getRemoteInstance().update(new ObjectStringPK(id.toString()), paymentBillInfo);
          RecordStatusTableInfo recordStatusTableInfo = new RecordStatusTableInfo();
          recordStatusTableInfo.setPayNumber(paymentBillInfo.getNumber());
          UserInfo currentUserInfo = SysContext.getSysContext().getCurrentUserInfo();
          recordStatusTableInfo.setOperator(currentUserInfo);
          SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          String format = df.format(new Date());
          Date parse = df.parse(format);
          recordStatusTableInfo.setOperationDate(parse);
          recordStatusTableInfo.setOperationState("已上报");
          RecordStatusTableFactory.getRemoteInstance().addnew(recordStatusTableInfo);
          
          count++;
        }
        else
        {
          msg.append("单据编号: " + paymentBillInfo.getNumber() + " 预计付款结束日期为空,不能上报!").append("\n");
        }
      }
      if ((msg == null) || ("".equals(msg.toString()))) {
        MsgBox.showInfo("成功 " + count + "条");
      } else {
        MsgBox.showDetailAndOK(this, "成功 " + count + "条", msg.toString(), 0);
      }
    }
    refresh(e);
  }
  
  public void actionBack_actionPerformed(ActionEvent e)
    throws Exception
  {
    checkSelected();
    List selectId = getSelectedIdValues();
    
    int showConfirm2 = MsgBox.showConfirm2(this, "是否打回 " + selectId.size() + "条付款单");
    if (showConfirm2 == 0)
    {
      int count = 0;
      StringBuffer msg = new StringBuffer();
      for (int i = 0; i < selectId.size(); i++)
      {
        Object id = selectId.get(i);
        
        PaymentBillInfo paymentBillInfo = PaymentBillFactory.getRemoteInstance().getPaymentBillInfo(new ObjectStringPK(id.toString()));
        BillStatusEnum billStatus = paymentBillInfo.getBillStatus();
        Object payStatus = paymentBillInfo.get("payStatus");
        if ((payStatus == null) || ("".equals(payStatus)))
        {
          msg.append("单据编号: " + paymentBillInfo.getNumber() + " 付款单未上报.").append("\n");
        }
        else if (billStatus.equals(BillStatusEnum.PAYED))
        {
          msg.append("单据编号: " + paymentBillInfo.getNumber() + " 付款单已付款.").append("\n");
        }
        else if ("0".equals(payStatus.toString()))
        {
          Date expectPayEndDate = paymentBillInfo.getExpectPayEndDate();
          paymentBillInfo.put("payStatus", "1");
          paymentBillInfo.setExpectPayEndDate(getNextWeek(expectPayEndDate));
          
          PaymentBillFactory.getRemoteInstance().update(new ObjectStringPK(id.toString()), paymentBillInfo);
          RecordStatusTableInfo recordStatusTableInfo = new RecordStatusTableInfo();
          recordStatusTableInfo.setPayNumber(paymentBillInfo.getNumber());
          UserInfo currentUserInfo = SysContext.getSysContext().getCurrentUserInfo();
          recordStatusTableInfo.setOperator(currentUserInfo);
          SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          String format = df.format(new Date());
          Date parse = df.parse(format);
          recordStatusTableInfo.setOperationDate(parse);
          recordStatusTableInfo.setOperationState("已退回");
          RecordStatusTableFactory.getRemoteInstance().addnew(recordStatusTableInfo);
          count++;
        }
        else
        {
          msg.append("单据编号: " + paymentBillInfo.getNumber() + " 付款单未上报.").append("\n");
        }
      }
      if ((msg == null) || ("".equals(msg.toString()))) {
        MsgBox.showInfo("成功 " + count + "条");
      } else {
        MsgBox.showDetailAndOK(this, "成功 " + count + "条", msg.toString(), 0);
      }
    }
    refresh(e);
  }
  
  private static Date getNextWeek(Date dateTime)
  {
    Calendar cal1 = Calendar.getInstance();
    Calendar cal2 = Calendar.getInstance();
    
    cal1.setTime(dateTime);
    
    cal2.setTime(dateTime);
    
    int dayWeek = cal1.get(7);
    if (dayWeek == 1)
    {
      cal1.add(5, 1);
      cal2.add(5, 7);
    }
    else
    {
      cal1.add(5, 1 - dayWeek + 8);
      cal2.add(5, 1 - dayWeek + 14);
    }
    Calendar cStart = Calendar.getInstance();
    cStart.setTime(cal1.getTime());
    
    Date date = null;
    while (cal2.getTime().after(cStart.getTime())) {
      try
      {
        cStart.add(5, 1);
        String dayForWeek = dayForWeek(cStart.getTime());
        if (dayForWeek != null) {
          date = cStart.getTime();
        }
      }
      catch (Throwable e)
      {
        e.printStackTrace();
      }
    }
    return date;
  }
  
  public static String dayForWeek(Date pTime)
    throws Throwable
  {
    Calendar cal = Calendar.getInstance();
    
    String[] weekDays = { "7", "1", "2", "3", "4", "5", "6" };
    try
    {
      cal.setTime(pTime);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    int w = cal.get(7) - 1;
    if (w == 4) {
      return weekDays[w];
    }
    return null;
  }
  
  public String getComFilter()
  {
    UserInfo currentUserInfo = SysContext.getSysContext().getCurrentUserInfo();
    try
    {
      RegionalApprovalCollection regionalApprovalCollection = RegionalApprovalFactory.getRemoteInstance().getRegionalApprovalCollection(
        "where user='" + currentUserInfo.getId().toString() + "' and status = '1'");
      String id = "";
      if (regionalApprovalCollection.size() != 0) {
        for (int i = 0; i < regionalApprovalCollection.size(); i++)
        {
          RegionalApprovalInfo regionalApprovalInfo = regionalApprovalCollection.get(i);
          CompanyOrgUnitInfo companyOrgUnitInfo = CompanyOrgUnitFactory.getRemoteInstance().getCompanyOrgUnitInfo(
            new ObjectStringPK(regionalApprovalInfo.getCompany().getId().toString()));
          if (i == 0) {
            id = companyOrgUnitInfo.getId().toString();
          } else {
            id = id + "," + companyOrgUnitInfo.getId().toString();
          }
        }
      }
      return id;
    }
    catch (BOSException e)
    {
      e.printStackTrace();
    }
    catch (EASBizException e)
    {
      e.printStackTrace();
    }
    return null;
  }
  
  protected FindDialog getFindDialog()
    throws Exception
  {
    return super.getFindDialog();
  }
  
  protected IQueryExecutor getQueryExecutor(IMetaDataPK queryPK, EntityViewInfo viewInfo)
  {
    this.ids = ((String[])getUIContext().get("IDS"));
    if ((this.ids == null) || (this.ids.length == 0))
    {
      FilterInfo filter = viewInfo.getFilter();
      FilterItemCollection filterItems = filter.getFilterItems();
      String comFilter = "";
      
      comFilter = getComFilter();
      if ((comFilter != null) && (!"".equals(comFilter)))
      {
        if (this.check) {
          return super.getQueryExecutor(queryPK, viewInfo);
        }
        for (int i = 0; i < filterItems.size(); i++)
        {
          FilterItemInfo filterItemInfo = filterItems.get(i);
          String compareValue = filterItemInfo.getPropertyName().toString();
          if ("company.id".equals(compareValue))
          {
            filterItems.removeObject(filterItemInfo);
            filterItems.add(new FilterItemInfo("company.id", comFilter, CompareType.INCLUDE));
          }
        }
      }
      return super.getQueryExecutor(queryPK, viewInfo);
    }
    viewInfo = new EntityViewInfo();
    FilterInfo filter = new FilterInfo();
    String payIDs = "";
    for (int i = 0; i < this.ids.length; i++) {
      if (("".equals(payIDs)) || (payIDs == null)) {
        payIDs = this.ids[i];
      } else {
        payIDs = payIDs + "," + this.ids[i];
      }
    }
    filter.getFilterItems().add(new FilterItemInfo("id", payIDs, CompareType.INCLUDE));
    viewInfo.setFilter(filter);
    return super.getQueryExecutor(queryPK, viewInfo);
  }
  
  public void actionQuery_actionPerformed(ActionEvent e)
    throws Exception
  {
    super.actionQuery_actionPerformed(e);
    refreshList();
  }
  
  public FMFilterBaseUI getFilterUI()
    throws Exception
  {
    String comFilter = null;
    this.ids = ((String[])getUIContext().get("IDS"));
    String comFilter2 = getComFilter();
    if ((comFilter2 != null) && (!"".equals(comFilter2)))
    {
      if (this.filterUI == null)
      {
        this.filterUI = new CasPaymentFilterUIPIEx(this.paramValue);
        this.filterUI.kDLabelContainer3.setVisible(false);
        this.filterUI.prmtCompany.setVisible(false);
        if ((this.ids == null) || (this.ids.length == 0))
        {
          comFilter = getComFilter();
        }
        else
        {
          Date startDate = (Date)getUIContext().get("startDate");
          Date endDate = (Date)getUIContext().get("endDate");
          this.filterUI.pkDateFrom.setValue(startDate);
          this.filterUI.pkDateFrom.setEnabled(false);
          this.filterUI.pkDateTo.setValue(endDate);
          this.filterUI.pkDateTo.setEnabled(false);
        }
        if ("".equals(comFilter))
        {
          MsgBox.showInfo("区域审批表没有配置该用户或用户未启用无法查看,请在区域审批表中配置用户。");
          abort();
        }
      }
      else
      {
        this.check = this.filterUI.getChecaBox();
      }
      return this.filterUI;
    }
    return super.getFilterUI();
  }
  
  protected IMultiOrgSupport getMainCustomerQueryPanel()
  {
    return super.getMainCustomerQueryPanel();
  }
  
  private void ShowWind()
  {
    UIContext context = new UIContext(this);
    
    Set list = getSelectIds();
    if ((list != null) && (list.size() > 0))
    {
      context.put("selectIds", list);
      context.put("oper", "cas");
      try
      {
        String path = "com.kingdee.eas.custom.client.TimeCheckNoNullEditUI";
        IUIWindow window = UIFactory.createUIFactory("com.kingdee.eas.base.uiframe.client.UIModelDialogFactory").create(path, context, null, OprtState.VIEW);
        window.show();
        refreshList();
      }
      catch (UIException e)
      {
        e.printStackTrace();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      MsgBox.showInfo("请选择有效的数据");
    }
  }
  
  private void ShowWindEndDate()
  {
    UIContext context = new UIContext(this);
    
    Set list = getSelectIds();
    if ((list != null) && (list.size() > 0))
    {
      context.put("selectIds", list);
      context.put("oper", "cas");
      try
      {
        String path = "com.kingdee.eas.custom.mvp.alterdate.client.AlterEndDateUI";
        IUIWindow window = UIFactory.createUIFactory("com.kingdee.eas.base.uiframe.client.UIModelDialogFactory").create(path, context, null, OprtState.VIEW);
        window.show();
        refreshList();
      }
      catch (UIException e)
      {
        e.printStackTrace();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    else
    {
      MsgBox.showInfo("请选择有效的数据");
    }
  }
  
  private Set getSelectIds()
  {
    Object strObj = null;
    Object strObj1 = null;
    KDTSelectBlock block = null;
    Set souIDs = new LinkedHashSet();
    int size = this.tblMain.getSelectManager().size();
    for (int i = 0; i < size; i++)
    {
      block = this.tblMain.getSelectManager().get(i);
      for (int j = block.getTop(); j <= block.getBottom(); j++)
      {
        ICell cellstr = this.tblMain.getRow(j).getCell("billStatus");
        strObj = cellstr.getValue();
        
        ICell cellstr1 = this.tblMain.getRow(j).getCell("id");
        strObj1 = cellstr1.getValue();
        souIDs.add(strObj1);
      }
    }
    return souIDs;
  }
}
