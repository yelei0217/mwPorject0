/**
 * output package name
 */
package com.kingdee.eas.custom.client;

import java.awt.Color;
import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.metadata.entity.FilterInfo;
import com.kingdee.bos.metadata.entity.FilterItemInfo;
import com.kingdee.bos.ui.face.CoreUIObject;
import com.kingdee.bos.ui.face.IUIWindow;
import com.kingdee.bos.ui.face.UIFactory;
import com.kingdee.bos.util.BOSUuid;
import com.kingdee.bos.util.EASResource;
import com.kingdee.bos.ctrl.kdf.table.ICell;
import com.kingdee.bos.ctrl.kdf.table.IRow;
import com.kingdee.bos.ctrl.kdf.table.KDTSelectBlock;
import com.kingdee.bos.ctrl.kdf.table.KDTSelectManager;
import com.kingdee.bos.ctrl.kdf.table.KDTable;
import com.kingdee.bos.ctrl.kdf.table.event.KDTActiveCellEvent;
import com.kingdee.bos.ctrl.kdf.table.event.KDTActiveCellListener;
import com.kingdee.bos.ctrl.kdf.table.event.KDTMouseEvent;
import com.kingdee.bos.ctrl.kdf.table.event.KDTMouseListener;
import com.kingdee.bos.ctrl.kdf.table.event.KDTSelectEvent;
import com.kingdee.bos.ctrl.kdf.table.event.KDTSelectListener;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
import com.kingdee.bos.dao.query.ISQLExecutor;
import com.kingdee.bos.dao.query.SQLExecutorFactory;
import com.kingdee.eas.base.log.LogUtil;
import com.kingdee.eas.basedata.assistant.CurrencyFactory;
import com.kingdee.eas.basedata.assistant.CurrencyInfo;
import com.kingdee.eas.basedata.master.account.AccountTableInfo;
import com.kingdee.eas.basedata.master.account.AccountViewCollection;
import com.kingdee.eas.basedata.master.account.AccountViewFactory;
import com.kingdee.eas.basedata.master.account.AccountViewInfo;
import com.kingdee.eas.basedata.org.CompanyOrgUnitCollection;
import com.kingdee.eas.basedata.org.CompanyOrgUnitFactory;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.common.client.SysContext;
import com.kingdee.eas.common.client.UIContext;
import com.kingdee.eas.custom.GLRptCompositeFacadeFactory;
import com.kingdee.eas.fi.gl.GlUtils;
import com.kingdee.eas.fi.gl.client.InitClientHelp;
import com.kingdee.eas.fi.gl.common.GLResUtil;
import com.kingdee.eas.fi.gl.rpt.CompanyDisplayModeEnum;
import com.kingdee.eas.fi.gl.rpt.GLRptAccountBalanceCondition;
import com.kingdee.eas.fi.gl.rpt.GLRptBaseCondition;
import com.kingdee.eas.fi.gl.rpt.GLRptSubLedgerCondition;
import com.kingdee.eas.fi.gl.rpt.client.GLRptSubLedgerUI;
import com.kingdee.eas.fi.gl.rpt.client.GLRptTable;
import com.kingdee.eas.fi.gl.rpt.client.GLRptUtils;
import com.kingdee.eas.framework.*;
import com.kingdee.eas.framework.report.ICommRptBase;
import com.kingdee.eas.framework.report.client.CommRptBaseConditionUI;
import com.kingdee.eas.framework.report.util.KDTableUtil;
import com.kingdee.eas.framework.report.util.RptParams;
import com.kingdee.eas.scm.sd.sale.SaleInvoiceInfo;
import com.kingdee.jdbc.rowset.IRowSet;
import com.kingdee.util.StringUtils;

/**
 * output class name
 */
public class GLRptCompositeUI extends AbstractGLRptCompositeUI
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4060489987469645553L;
	private static final Logger logger = CoreUIObject.getLogger(GLRptCompositeUI.class);
    
    /**
     * output class constructor
     */
    public GLRptCompositeUI() throws Exception
    {
        super();
    }

   
    
    @Override
	protected void initUIP() {
 		super.initUIP();
	}

    @Override
    public void onLoad() throws Exception {
     	super.onLoad();
     	  if (this.tHelper != null) {
              this.tHelper.setCanMoveColumn(true); 
          }
     	  
  	    CompanyOrgUnitInfo company = (CompanyOrgUnitInfo) params.getObject("company");
	    int companyLevel = company.getLevel();
	    if(companyLevel == 3){
	    	this.btnShowAll.setVisible(false);
	    	this.btnShowSelf.setVisible(false);
	    }else{
	    	this.btnShowAll.setVisible(true);
	    	this.btnShowSelf.setVisible(true);
	    }
    }

    @Override
    public void beforePrint() {
    	//super.beforePrint();
    	 this.logUIOperation("LOG_SALEGROSSPROFITS");
    }
    
    private void logUIOperation(final String key) {
        String operName = EASResource.getString("com.kingdee.eas.scm.sd.sale.report.SDReportResource", key);
        IObjectPK pk = LogUtil.beginLog(null, operName, new SaleInvoiceInfo().getBOSType(), (IObjectPK)null, operName);
        LogUtil.afterLog(null, pk);
    }
    
    protected String getUIFullName() {
        return "com.kingdee.eas.custom.client.GLRptCompositeUI";
    }
    
    
	protected void initListener()
    {
      super.initListener();
      
      tblMain.addKDTActiveCellListener(new KDTActiveCellListener(){
		@Override
		public void activeCellChanged(KDTActiveCellEvent e) {
 			if(e.getColumnIndex()==10||e.getColumnIndex()==11){
 				 doTableDoubleClick(e.getRowIndex(),0);
 			}else if(e.getColumnIndex()==14||e.getColumnIndex()==15){
 				 doTableDoubleClick(e.getRowIndex(),1);
 			}  
		}
      }
      
      );
      tblMain.addKDTSelectListener(new KDTSelectListener()
      {
        public void tableSelectChanged(KDTSelectEvent e)
        {
          int rowIndex = e.getSelectBlock().getTop();
          try
          {
            tblMain_SelectChanged(rowIndex);
          }
          catch (Exception ex)
          {
            handleException(ex);
          }
        }
      });
//      tblMain.addKDTMouseListener(new KDTMouseListener()
//      {
//        public void tableClicked(KDTMouseEvent e)
//        {
//          try
//          {
//            if (e.getClickCount() > 1) {
//             doTableDoubleClick(e.getRowIndex());
//            }
//          }
//          catch (Exception ex)
//          {
//            handUIException(ex);
//          }
//        }
//      });
    }
    
    @Override
	protected void btnShowAll_actionPerformed(ActionEvent e) throws Exception {
    	 CompanyOrgUnitInfo company = (CompanyOrgUnitInfo) params.getObject("company");
   	    int companyLevel = company.getLevel();
   	    int c = tblMain.getRowCount();
   	    if(companyLevel == 1){
   	 	 for(int j = 0 ; j < c ;j++){
	   		 IRow rw = tblMain.getRow(j);
	   		 if(rw != null &&
					 ("2".equals(rw.getCell("lv").getValue().toString())||"3".equals(rw.getCell("lv").getValue().toString()))){
						 rw.getStyleAttributes().setHided(false);
						 rw.getCell("ishow").setValue("1");
			 }else if(rw != null &&"1".equals(rw.getCell("lv").getValue().toString())){
				 rw.getCell("ishow").setValue("0");
			 }
	   	 }
   	    }else if(companyLevel == 2){
   		 for(int j = 0 ; j < c ;j++){
   			 IRow rw = tblMain.getRow(j);
   			 if(rw != null &&"3".equals(rw.getCell("lv").getValue().toString())){ 
   				rw.getStyleAttributes().setHided(false);
   			    rw.getCell("ishow").setValue("1");
   			 }else if(rw != null &&"2".equals(rw.getCell("lv").getValue().toString())){
				 rw.getCell("ishow").setValue("0");
			 }
		    }
   	    }
	}

	@Override
	protected void btnShowSelf_actionPerformed(ActionEvent e) throws Exception {
		   CompanyOrgUnitInfo company = (CompanyOrgUnitInfo) params.getObject("company");
		  int companyLevel = company.getLevel();
	   	    int c = tblMain.getRowCount();
	   	    if(companyLevel == 1){
	   	 	 for(int j = 0 ; j < c ;j++){
		   		 IRow rw = tblMain.getRow(j);
		   		 if(rw != null &&
						 ("2".equals(rw.getCell("lv").getValue().toString())||"3".equals(rw.getCell("lv").getValue().toString()))){
							 rw.getStyleAttributes().setHided(true);
							 rw.getCell("ishow").setValue("0");
				 }else if(rw != null &&"1".equals(rw.getCell("lv").getValue().toString())){
					 rw.getCell("ishow").setValue("1");
				 }
		   	 }
	   	    }else if(companyLevel == 2){
	   		 for(int j = 0 ; j < c ;j++){
	   			 IRow rw = tblMain.getRow(j);
	   			 if(rw != null &&"3".equals(rw.getCell("lv").getValue().toString())){ 
	   				rw.getStyleAttributes().setHided(true);
	   			    rw.getCell("ishow").setValue("0");
	   			 }else if(rw != null &&"2".equals(rw.getCell("lv").getValue().toString())){
					 rw.getCell("ishow").setValue("1");
				 }
			    }
	   	    }
	}

	protected void tblMain_SelectChanged(int rowIndex)throws EASBizException, BOSException
	 {
       	IRow row = tblMain.getRow(rowIndex);
	    CompanyOrgUnitInfo company = (CompanyOrgUnitInfo) params.getObject("company");
	    int companyLevel = company.getLevel();
	    if(companyLevel == 1){
	    	if(row !=null && "1".equals(row.getCell("lv").getValue().toString())){
	    		String accountnumber = row.getCell("AccountNumber").getValue().toString();
	    		 int c = tblMain.getRowCount();
	    		 String ishowStr = row.getCell("ishow").getValue().toString();
	    		 if("1".equals(ishowStr)){
	    			 for(int j = 0 ; j < c ;j++){
	        			 IRow rw = tblMain.getRow(j);
	        			 if(rw != null && accountnumber.equals(rw.getCell("AccountNumber").getValue().toString())&&
	        					 ("2".equals(rw.getCell("lv").getValue().toString())||"3".equals(rw.getCell("lv").getValue().toString()))){
	        						 rw.getStyleAttributes().setHided(false);
	        						 rw.getCell("ishow").setValue("1");
	        			 }
	        		 }
	    			 row.getCell("ishow").setValue("0");
	    		 }else{
	    			 for(int j = 0 ; j < c ;j++){
	        			 IRow rw = tblMain.getRow(j);
	        			 if(rw != null && accountnumber.equals(rw.getCell("AccountNumber").getValue().toString())&&
	        					 ("2".equals(rw.getCell("lv").getValue().toString())||"3".equals(rw.getCell("lv").getValue().toString()))){
	        						 rw.getStyleAttributes().setHided(true);
	        						 rw.getCell("ishow").setValue("1");
	        			 }
	        		 }
	    			 row.getCell("ishow").setValue("1");
	    		 }
	    	}else if(row !=null && "2".equals(row.getCell("lv").getValue().toString())){
	    		String accountnumber = row.getCell("AccountNumber").getValue().toString();
	    		String ishowStr = row.getCell("ishow").getValue().toString();
	    		String code =  row.getCell("CompanyNumber").getValue().toString();
		   		 int c = tblMain.getRowCount();
		   		  if("1".equals(ishowStr)){
		   			 for(int j = 0 ; j < c ;j++){
			   			 IRow rw = tblMain.getRow(j);
			   			 if(rw != null && accountnumber.equals(rw.getCell("AccountNumber").getValue().toString())
			   					 &&"3".equals(rw.getCell("lv").getValue().toString())&&
			   					code.equals(rw.getCell("pcon").getValue().toString())){ 
			   				rw.getStyleAttributes().setHided(true);
			   			    rw.getCell("ishow").setValue("0");
			   			 }
		   		    }
			   			row.getCell("ishow").setValue("0"); 
		   		  }else{
		   			 for(int j = 0 ; j < c ;j++){
			   			 IRow rw = tblMain.getRow(j);
			   			 if(rw != null && accountnumber.equals(rw.getCell("AccountNumber").getValue().toString())
			   					 &&"3".equals(rw.getCell("lv").getValue().toString())&&
				   					code.equals(rw.getCell("pcon").getValue().toString())){
			   			    rw.getStyleAttributes().setHided(false);
			   		   	    rw.getCell("ishow").setValue("1");
			   			 }
		   		    }
			   			row.getCell("ishow").setValue("1");
		   		  }
		   	
	    	}
	    }else if(companyLevel == 2){
	    	 if(row !=null && "2".equals(row.getCell("lv").getValue().toString())){
		    		String accountnumber = row.getCell("AccountNumber").getValue().toString();
		    		String ishowStr = row.getCell("ishow").getValue().toString();
		    		String code =  row.getCell("CompanyNumber").getValue().toString();
			   		 int c = tblMain.getRowCount();
			   		  if("1".equals(ishowStr)){
			   			 for(int j = 0 ; j < c ;j++){
				   			 IRow rw = tblMain.getRow(j);
				   			 if(rw != null && accountnumber.equals(rw.getCell("AccountNumber").getValue().toString())
				   					 &&"3".equals(rw.getCell("lv").getValue().toString())&&
				   					code.equals(rw.getCell("pcon").getValue().toString())){ 
				   				rw.getStyleAttributes().setHided(false);
				   			    rw.getCell("ishow").setValue("1");
				   			 }
			   		    }
				   			row.getCell("ishow").setValue("0"); 
			   		  }else{
			   			 for(int j = 0 ; j < c ;j++){
				   			 IRow rw = tblMain.getRow(j);
				   			 if(rw != null && accountnumber.equals(rw.getCell("AccountNumber").getValue().toString())
				   					 &&"3".equals(rw.getCell("lv").getValue().toString())&&
					   					code.equals(rw.getCell("pcon").getValue().toString())){
				   			    rw.getStyleAttributes().setHided(true);
				   		   	    rw.getCell("ishow").setValue("0");
				   			 }
			   		    }
				   			row.getCell("ishow").setValue("1");
			   		  }
			   	
		    	}
	    }

	 }
    
    /**
     * output storeFields method
     */
    public void storeFields()
    {
        super.storeFields();
    }
    
    protected void doTableDoubleClick(int rowIndex,int type)
    {
        try {
        	IRow row = tblMain.getRow(rowIndex);
        	if(row !=null && "3".equals(row.getCell("lv").getValue().toString())){
	            setCursorOfWair();
				findSubsidiaryLedger(type);
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
   
    private void findSubsidiaryLedger(int type)
    throws Exception
	  {
	    KDTSelectBlock sb = this.tblMain.getSelectManager().get();
	    if ((sb == null)) {
	      return;
	    }
	    GLRptSubLedgerCondition cond = new GLRptSubLedgerCondition(getCustomCondition(type));
	    cond.setCompanyDisplayMode(CompanyDisplayModeEnum.enumerate);
	    joinQury(cond, GLRptSubLedgerUI.class.getName());
	  }
    
 
    

    protected void joinQury(GLRptSubLedgerCondition cond, String uiClassName)
    throws Exception
  {
    try
    {
      setCursorOfWair();
      if (cond.getCompany() == null) {
          if (cond.getCompanyId() != null) {
            cond.switchCompany(GlUtils.getCompanyById(cond.getCompanyId()), true);
          } else {
            cond.switchCompany(getCurrCompany(), true);
          }
        }
      
      String strCurrencyid = "11111111-1111-1111-1111-111111111111DEB58FDC";
      cond.setCurrencyID(strCurrencyid); 
      CurrencyInfo currency = CurrencyFactory.getRemoteInstance().getCurrencyInfo(new ObjectUuidPK(strCurrencyid));
      cond.setCurrencyName(currency.getName());
      
      cond.setShowDisplayAsstDetail(false);
      cond.setAsstActList(new ArrayList());
      
      cond.setNotIncluePLVoucher(true);
      
      EntityViewInfo view = new EntityViewInfo();
      FilterInfo filter = new FilterInfo();
      filter.getFilterItems().add(new FilterItemInfo("SourceType",0)); //源单ID
      
      //filter.getFilterItems().add(new FilterItemInfo("voucher.Description",null)); //源单ID

      view.setFilter(filter);
      cond.setSelfCond(view);
       
      setLinkedAccount(cond);
    
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
    
    protected void setLinkedAccount(GLRptSubLedgerCondition cond)
      throws Exception
    {
      if (tblMain.getSelectManager().get() != null)
      {
        int i = KDTableUtil.getSelectedTop(tblMain);
        if (i != -1)
        {
          String accountnumber = (String)tblMain.getCell(i, "AccountNumber").getValue();
          String companyNumber = (String)tblMain.getCell(i, "CompanyNumber").getValue();

	          if(!companyNumber.endsWith("@02")){
	        	  companyNumber = companyNumber+"@02";
	          }
	        CompanyOrgUnitCollection coll = CompanyOrgUnitFactory.getRemoteInstance().getCompanyOrgUnitCollection("where number ='"+companyNumber+"'");
	        CompanyOrgUnitInfo companys[] = new CompanyOrgUnitInfo[1];
	        companys[0] = coll.get(0);
	        cond.setCompanyId(companys[0].getId().toString());
	        cond.setCompany(companys[0]);
	        cond.setSelectedCompanys(companys);
	        cond.setCompanys(companys);
        
          if (StringUtils.isEmpty(accountnumber)) {
            return;
          }
          EntityViewInfo view = new EntityViewInfo();
          FilterInfo filter = new FilterInfo();
          filter.getFilterItems().add(new FilterItemInfo("companyID.number",companyNumber)); //源单ID
          filter.getFilterItems().add(new FilterItemInfo("number", accountnumber)); //目标单据类型
          view.setFilter(filter);
          AccountViewCollection accounts =  AccountViewFactory.getRemoteInstance().getAccountViewCollection(view);
          AccountViewInfo account = accounts.get(0);
          if (cond.getAccountIdSet() != null)
          {
            Set accountIdSet = new HashSet();
            accountIdSet.add(account.getId().toString());
            cond.setAccountIdSet(accountIdSet);
          }
          if (!getCurrCompany().isIsBizUnit()) {
            cond.setAccountIdSet(null);
          }
          cond.setAccountCodeStart(account.getNumber());
          cond.setAccountCodeEnd(account.getNumber());
          cond.setNotIncluePLVoucher(true);
          boolean isOnlyLeaf = params.getBoolean("optionOnlyLeaf"); 
	  //  boolean isIncludePost =  params.getBoolean("includeBWAccount");
		    if (isOnlyLeaf)
	        {
	          cond.setAccountLevelStart(account.getLevel());
	          cond.setAccountLevelEnd(account.getLevel());
	        }
		    
        }
      }
    }
    
    protected CompanyOrgUnitInfo getCurrCompany()
    {
        CompanyOrgUnitInfo currCompany = null;
        GLRptBaseCondition cond = (GLRptBaseCondition)getUIContext().get("com.kingdee.eas.framework.report.client.CommRptBaseUI#UICONTEXT_KEY_JOINQUERY_PARAMS");
       if(cond != null && cond.getCompany() != null)
           currCompany = cond.getCompany();
        else
        if(getUIContext().get("CurrCompany") == null)
            currCompany = SysContext.getSysContext().getCurrentFIUnit();
        else
            currCompany = (CompanyOrgUnitInfo)getUIContext().get("CurrCompany");
        return currCompany;
    }
    
    protected void setJoinCompany(GLRptBaseCondition cond)
    throws EASBizException, BOSException
	{
	   CompanyOrgUnitInfo company = null;
	   if(CompanyDisplayModeEnum.enumerate == cond.getCompanyDisplayMode())
	    {
	         if(cond.getCompany() != null)
	           company =cond.getCompany();
	    } else
	    {
	        company = getSelectCompanyNotEnumerate();
	    }
	    if(company != null)
	      cond.switchCompany(company, true);
	}
    
    
    protected CompanyOrgUnitInfo getSelectCompanyNotEnumerate()
    throws EASBizException, BOSException
	{
	       if(CompanyDisplayModeEnum.enumerate == getCustomCondition(0).getCompanyDisplayMode())
	            return null;
	       CompanyOrgUnitInfo company = null;
	       int rowIndex = tblMain.getSelectManager().getActiveRowIndex();
	        if(rowIndex >= 0)
	    {
	            ICell cellOrgUnit = tblMain.getRow(rowIndex).getCell("FOrgUnitID");
	            if(cellOrgUnit != null)
	        {
	                Object obj = cellOrgUnit.getValue();
	               if(obj != null && (obj instanceof String))
	                    company = GlUtils.getCompanyById(obj.toString());
	        }
	    }
	        return company;
	}
    
    public GLRptBaseCondition getCustomCondition(int type)
    {
		    int level = 1;
		    if(params.getObject("accountLevelStart") !=null && !"".equals(params.getObject("accountLevelStart").toString())){
		    	level = Integer.parseInt(params.getObject("accountLevelStart").toString());
		    }
		    int periodYear = 2020 ;
		    if(params.getObject("periodYearStart") !=null && !"".equals(params.getObject("periodYearStart").toString())){
		    	periodYear = Integer.parseInt(params.getObject("periodYearStart").toString());
		    }
		    int periodNumber = 5 ;
		    if(params.getObject("periodNumberStart") !=null && !"".equals(params.getObject("periodNumberStart").toString())){
		    	periodNumber = Integer.parseInt(params.getObject("periodNumberStart").toString());
 		    }
       GLRptBaseCondition customCondition = new GLRptBaseCondition();
   
//      customCondition.setCompanys((CompanyOrgUnitInfo[]) params.getObject("companys"));
//      customCondition.setCompanyId(company.getId().toString());
//      customCondition.setCompany(company);
//      customCondition.setSelectedCompanys((CompanyOrgUnitInfo[]) params.getObject("companys"));
      
      
      customCondition.setCompanyDisplayMode(CompanyDisplayModeEnum.enumerate);
      customCondition.setExpandCompanyLevel(level);
    //  cond.setCompanyDisplayMode(CompanyDisplayModeEnum.enumerate);
      customCondition.setAccountTableId("T91pJxKKSk+72DxiIK07Jhdpjm0=");//T_BD_AccountTable
      customCondition.setPeriodTypeId("B+Rlys8oRE6J8BgYd00FIV9piaY=");
      
       customCondition.setPeriodYearStart(periodYear);
       customCondition.setPeriodYearEnd(periodYear);
        customCondition.setPeriodNumberEnd(periodNumber);
       
       if(type==0){
    	    customCondition.setPeriodNumberStart(periodNumber); 
       }else if(type==1){
    	   customCondition.setPeriodNumberStart(1); 
       }
       
       
       customCondition.setAccountLevelStart(level);
       customCondition.setAccountLevelEnd(level);
      
       String currencyId = "11111111-1111-1111-1111-111111111111DEB58FDC";
       customCondition.setCurrencyID(currencyId);
       return customCondition;
    }
    
    
    protected UIContext getLinkedContext(GLRptBaseCondition cond)
    {
      UIContext uiContext = new UIContext(this);
      uiContext.put("JoinQuery", Boolean.TRUE);
      uiContext.put("UIClassParam", cond.toMap().toString());
      uiContext.put("com.kingdee.eas.framework.report.client.CommRptBaseUI#UICONTEXT_KEY_JOINQUERY_PARAMS", cond);
      return uiContext;
    }
  
	@Override
	protected RptParams getParamsForInit() {
 		return params;
	}
 

	@Override
	protected ICommRptBase getRemoteInstance() throws BOSException {
 		return GLRptCompositeFacadeFactory.getRemoteInstance();
	}

	@Override
	protected KDTable getTableForPrintSetting() {
 		return this.tblMain;
	}

	@Override
	protected void query() {
		tblMain.checkParsed();
		tblMain.setEditable(false);
		//设置光标处于等待状态
		setCursorOfWair();
		 //查询数据,在Facade查询方法里返回值是RptParams，需要把查询集保存到RptParams里，再通过界面取出即可
		 try {
			RptParams rps = ((ICommRptBase)getRemoteInstance()).query(params);
			 if(rps!=null){
				 IRowSet conReport =  ((IRowSet)rps.getObject("rs")) ;
				 if(conReport!=null){
					fillTable(conReport);
					tblMain.getSelectManager().setSelectMode(KDTSelectManager.ROW_SELECT);
				 }
			 }
		} catch (EASBizException e) {
 			e.printStackTrace();
		} catch (BOSException e) {
 			e.printStackTrace();
		} catch (SQLException e) {
 			e.printStackTrace();
		}

	}

	@Override
	protected CommRptBaseConditionUI getQueryDialogUserPanel() throws Exception {
 		return new GLRptCompositeFilterUI();
	}

	private Map<String,String> getAccountInfo(){
		Map<String,String> acs = new HashMap<String,String>();
		// 缺少上下文Context，无法在前端直接使用com.kingdee.eas.util.app.DbUtil
		String sql = "select FNUMBER,FDC from T_BD_AccountView where fcompanyid = '00000000-0000-0000-0000-000000000000CCE7AED4' and FAccountTableID ='T91pJxKKSk+72DxiIK07Jhdpjm0='";
		ISQLExecutor executor = SQLExecutorFactory.getRemoteInstance(sql);
 		try {
			IRowSet rs = executor.executeSQL();
			//获取结果集
			while(rs.next()){ 
				acs.put(rs.getString("FNUMBER"), rs.getString("FDC"));
			}
		} catch (BOSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
 			e.printStackTrace();
		} 
		return acs ;
	}
	
	
	private int fillTable(IRowSet rs) throws SQLException {
		tblMain.removeRows();
	    String amountFormat = GlUtils.getDecimalFormatString();
	    this.tblMain.getColumn("YearBeginDebit").getStyleAttributes().setNumberFormat(amountFormat);
	    this.tblMain.getColumn("YearBeginCredit").getStyleAttributes().setNumberFormat(amountFormat);
	    this.tblMain.getColumn("PeriodBeginDebit").getStyleAttributes().setNumberFormat(amountFormat);
	    this.tblMain.getColumn("PeriodBeginCredit").getStyleAttributes().setNumberFormat(amountFormat);
	    this.tblMain.getColumn("Debit").getStyleAttributes().setNumberFormat(amountFormat);
	    this.tblMain.getColumn("Credit").getStyleAttributes().setNumberFormat(amountFormat);
	    this.tblMain.getColumn("YearDebit").getStyleAttributes().setNumberFormat(amountFormat);
	    this.tblMain.getColumn("YearCredit").getStyleAttributes().setNumberFormat(amountFormat);
	    this.tblMain.getColumn("SubDebit").getStyleAttributes().setNumberFormat(amountFormat);
	    this.tblMain.getColumn("SubCredit").getStyleAttributes().setNumberFormat(amountFormat);
	    this.tblMain.getColumn("SubYearDebit").getStyleAttributes().setNumberFormat(amountFormat);
	    this.tblMain.getColumn("SubYearCredit").getStyleAttributes().setNumberFormat(amountFormat);
	    this.tblMain.getColumn("SubPeriodEndDebit").getStyleAttributes().setNumberFormat(amountFormat);
	    this.tblMain.getColumn("SubPeriodEndCredit").getStyleAttributes().setNumberFormat(amountFormat);
	    this.tblMain.getColumn("Company").getStyleAttributes().setWeight(250);
	    
	    Map<String,String> acs = getAccountInfo();

	    int currencyPrecision = 2;
	    setNumberFormat(currencyPrecision);
	    
//	    BigDecimal amtT1 = new BigDecimal(0.00);
//	    BigDecimal amtT2 = new BigDecimal(0.00);
//	    BigDecimal amtT3 = new BigDecimal(0.00);
//	    BigDecimal amtT4 = new BigDecimal(0.00);
//	    BigDecimal amtT5 = new BigDecimal(0.00);
//	    BigDecimal amtT6 = new BigDecimal(0.00);
//	    BigDecimal amtT7 = new BigDecimal(0.00);
//	    BigDecimal amtT8 = new BigDecimal(0.00);

	    CompanyOrgUnitInfo company = (CompanyOrgUnitInfo) params.getObject("company");
	    int companyLevel = company.getLevel();
		while (rs.next()) {
//		    BigDecimal amt1 = new BigDecimal(0.00);
//		    BigDecimal amt2 = new BigDecimal(0.00);
//		    BigDecimal amt3 = new BigDecimal(0.00);
//		    BigDecimal amt4 = new BigDecimal(0.00);
//		    BigDecimal amt5 = new BigDecimal(0.00);
//		    BigDecimal amt6 = new BigDecimal(0.00);
//		    BigDecimal amt7 = new BigDecimal(0.00);
//		    BigDecimal amt8 = new BigDecimal(0.00);
		    
			IRow row = tblMain.addRow();
			if(rs.getObject("FLEVEL") !=null && !"".equals(rs.getObject("FLEVEL").toString())){
				row.getCell("fisleaf").setValue(rs.getObject("FLEVEL").toString());
			}else{
				row.getCell("fisleaf").setValue(1);
			}
 			if(rs.getObject("FISLEAF") !=null && !"".equals(rs.getObject("FISLEAF").toString())){
				row.getCell("flevel").setValue(rs.getObject("FISLEAF").toString());
			}else{
				row.getCell("flevel").setValue(1);
			}
			
//			if(rs.getObject("ISHOW") !=null && !"".equals(rs.getObject("ISHOW").toString())){
//				row.getCell("ishow").setValue(rs.getObject("ISHOW").toString());
//			}else{
//				row.getCell("ishow").setValue(1);
//			}
			
			row.getCell("CompanyNumber").setValue(rs.getString("CON"));

			row.getCell("pcon").setValue(rs.getString("PCON"));
			String accountnumber = rs.getString("FACCOUNTNUMBER");
			int accountType = 1; 
			if(accountnumber.startsWith("6")){
				 accountType = 2;
				 if(accountnumber.startsWith("6001")||accountnumber.startsWith("6111")||accountnumber.startsWith("6117")||accountnumber.startsWith("6301")){
					 accountType = 3;
				 }
			}
			
			row.getCell("AccountNumber").setValue(accountnumber);
			row.getCell("AccountName").setValue(rs.getString("FACCOUNTNAME"));
			int lv = 1;
			if(rs.getObject("LV") !=null && !"".equals(rs.getObject("LV").toString())){
				 lv = Integer.parseInt(rs.getObject("LV").toString());
		      	row.getCell("lv").setValue(rs.getObject("LV").toString());
				if(companyLevel == 1){
					if(lv == 1){
						row.getStyleAttributes().setBackground(new Color(255,250,250));
						row.getStyleAttributes().setHided(false);
						row.getCell("Company").setValue(rs.getString("CNA"));
						row.getCell("ishow").setValue(1);
					} else if(lv ==2){
						row.getCell("ishow").setValue(0);
						row.getStyleAttributes().setHided(true);
						row.getCell("Company").setValue("  ----  "+rs.getString("CNA"));
						row.getStyleAttributes().setBackground(new Color(255,255,255));
					}else{
						row.getStyleAttributes().setHided(true);
						row.getStyleAttributes().setBackground(new Color(245,245,245));
						row.getCell("Company").setValue("  ---- ----  "+rs.getString("CNA"));
						row.getCell("ishow").setValue(0);
					}
				}else if(companyLevel == 2){
					 if(lv ==2){
							row.getCell("ishow").setValue(1);
							row.getStyleAttributes().setHided(false);
							row.getCell("Company").setValue("  ----  "+rs.getString("CNA"));
							row.getStyleAttributes().setBackground(new Color(255,255,255));
						}else{
							row.getCell("ishow").setValue(0);
							row.getStyleAttributes().setHided(true);
							row.getStyleAttributes().setBackground(new Color(245,245,245));
							row.getCell("Company").setValue("  ---- ----  "+rs.getString("CNA"));
						}
				}else if(companyLevel == 3){
					 if(lv == 3){
							row.getCell("ishow").setValue(1);
							row.getStyleAttributes().setHided(false);
							row.getStyleAttributes().setBackground(new Color(245,245,245));
							row.getCell("Company").setValue("  ---- ----  "+rs.getString("CNA"));
						}
				}
			}		
		      Object mYearBeginDebit = rs.getObject("FYearBeginDebit");
		      if ((mYearBeginDebit != null) && (getFormateAmount(mYearBeginDebit, currencyPrecision).compareTo(InitClientHelp.zero) != 0)) {
 		        row.getCell("YearBeginDebit").setValue(getFormateAmount(mYearBeginDebit, currencyPrecision));
		      }
		      
		      Object mYearBeginCredit = rs.getObject("FYearBeginCredit");
		      if ((mYearBeginCredit != null) && (getFormateAmount(mYearBeginCredit, currencyPrecision).compareTo(InitClientHelp.zero) != 0)) {
 		        row.getCell("YearBeginCredit").setValue(getFormateAmount(mYearBeginCredit, currencyPrecision));
		      }
		      
		      if(acs != null && acs.get(accountnumber) !=null && !"".equals( acs.get(accountnumber) )){
		    	  BigDecimal bigybd = getFormateAmount(mYearBeginDebit, currencyPrecision);
	    		  BigDecimal bigybc = getFormateAmount(mYearBeginCredit, currencyPrecision);
		    	  if("1".equals( acs.get(accountnumber))){
		    		  row.getCell("YearBeginDebit").setValue(bigybd.subtract(bigybc));
		 		      row.getCell("YearBeginCredit").setValue("");
		    	  }else if("-1".equals( acs.get(accountnumber))){
		    		  row.getCell("YearBeginDebit").setValue("");
		 		      row.getCell("YearBeginCredit").setValue(bigybc.subtract(bigybd));
		    	  }
		      } 
		      
		      Object mPeriodBeginDebit = rs.getObject("FPeriodBeginDebit");
		      if ((mPeriodBeginDebit != null) && (getFormateAmount(mPeriodBeginDebit, currencyPrecision).compareTo(InitClientHelp.zero) != 0)) {
 		        row.getCell("PeriodBeginDebit").setValue(getFormateAmount(mPeriodBeginDebit, currencyPrecision));
		      }
		      
		      Object mPeriodBeginCredit = rs.getObject("FPeriodBeginCredit");
		      if ((mPeriodBeginCredit != null) && (getFormateAmount(mPeriodBeginCredit, currencyPrecision).compareTo(InitClientHelp.zero) != 0)) {
 		        row.getCell("PeriodBeginCredit").setValue(getFormateAmount(mPeriodBeginCredit, currencyPrecision));
		      }
		      
		      Object mDebit = rs.getObject("FDebit");
		      if ((mDebit != null) && (getFormateAmount(mDebit, currencyPrecision).compareTo(InitClientHelp.zero) != 0)) {
 		        row.getCell("Debit").setValue(getFormateAmount(mDebit, currencyPrecision));
		      }
		      
		      Object mCredit = rs.getObject("FCredit");
		      if ((mCredit != null) && (getFormateAmount(mCredit, currencyPrecision).compareTo(InitClientHelp.zero) != 0)) {
 		        row.getCell("Credit").setValue(getFormateAmount(mCredit, currencyPrecision));
		      }
		      
		      
		      Object mYearDebit = rs.getObject("FYearDebit");
		      if ((mYearDebit != null) && (getFormateAmount(mYearDebit, currencyPrecision).compareTo(InitClientHelp.zero) != 0)) {
 		        row.getCell("YearDebit").setValue(getFormateAmount(mYearDebit, currencyPrecision));
		      }
		      
		      Object mYearCredit = rs.getObject("FYearCredit");
		      if ((mYearCredit != null) && (getFormateAmount(mYearCredit, currencyPrecision).compareTo(InitClientHelp.zero) != 0)) {
 		        row.getCell("YearCredit").setValue(getFormateAmount(mYearCredit, currencyPrecision));
		      }
		      
		      Object mSubDebit = rs.getObject("FSubDebit"); 
		      if( accountType == 3)
		    	  mSubDebit = rs.getObject("FSubCredit"); 
		      if ((mSubDebit != null) && (getFormateAmount(mSubDebit, currencyPrecision).compareTo(InitClientHelp.zero) != 0)) {
 		        row.getCell("SubDebit").setValue(getFormateAmount(mSubDebit, currencyPrecision));
		      }
		      
		      Object mSubCredit = rs.getObject("FSubCredit");  
		      if( accountType == 2)
		    	  mSubCredit = rs.getObject("FSubDebit"); 
		      if ((mSubCredit != null) && (getFormateAmount(mSubCredit, currencyPrecision).compareTo(InitClientHelp.zero) != 0)) {
 		        row.getCell("SubCredit").setValue(getFormateAmount(mSubCredit, currencyPrecision));
		      }
		      
		      Object mSubYearDebit = rs.getObject("FSubYearDebit");
		      if( accountType == 3)
		    	  mSubYearDebit = rs.getObject("FSubYearCredit");
		      if ((mSubYearDebit != null) && (getFormateAmount(mSubYearDebit, currencyPrecision).compareTo(InitClientHelp.zero) != 0)) {
 		        row.getCell("SubYearDebit").setValue(getFormateAmount(mSubYearDebit, currencyPrecision));
		      }
		      
		      Object mSubYearCredit = rs.getObject("FSubYearCredit");
		      if(accountType == 2)
		    	  mSubYearCredit = rs.getObject("FSubYearDebit"); 
		      if ((mSubYearCredit != null) && (getFormateAmount(mSubYearCredit, currencyPrecision).compareTo(InitClientHelp.zero) != 0)) {
 		        row.getCell("SubYearCredit").setValue(getFormateAmount(mSubYearCredit, currencyPrecision));
		      }
		      
		      Object mSubPeriodEndDebit = rs.getObject("FSubPeriodEndDebit");
		      if( accountType == 3)
		    	  mSubPeriodEndDebit = rs.getObject("FSubPeriodEndCredit");
		      if ((mSubPeriodEndDebit != null) && (getFormateAmount(mSubPeriodEndDebit, currencyPrecision).compareTo(InitClientHelp.zero) != 0)) {
 		        row.getCell("SubPeriodEndDebit").setValue(getFormateAmount(mSubPeriodEndDebit, currencyPrecision));
		      }
		      
		       Object mSubPeriodEndCredit = rs.getObject("FSubPeriodEndCredit");
 		      if(accountType == 2)
		    	  mSubPeriodEndCredit = rs.getObject("FSubPeriodEndDebit");
		      if ((mSubPeriodEndCredit != null) && (getFormateAmount(mSubPeriodEndCredit, currencyPrecision).compareTo(InitClientHelp.zero) != 0)) {
 		        row.getCell("SubPeriodEndCredit").setValue(getFormateAmount(mSubPeriodEndCredit, currencyPrecision));
		      }
			
		}
		// 添加合计行，以下代码可选
		// 有金额字段需要合计值时，才需要该部分代码
//		IRow footRow = tblMain.addRow();
//		footRow.getCell("Company").setValue("合计");
// 		footRow.getCell("MasterDebit").setValue(amtT1);
//		footRow.getCell("MasterCredit").setValue(amtT2);
//		footRow.getCell("AdjustDebit").setValue(amtT3);
//		footRow.getCell("AdjustCredit").setValue(amtT4);
//		footRow.getCell("MDebit").setValue(amtT5);
//		footRow.getCell("MCredit").setValue(amtT6);
//		footRow.getCell("SubDebit").setValue(amtT7);
//		footRow.getCell("SubCredit").setValue(amtT8);
//		footRow.getCell("lv").setValue(4);
//		footRow.getCell("ishow").setValue(1);
//		footRow.getCell("AccountNumber").setValue("-----");
//		footRow.getStyleAttributes().setBackground(new Color(220,220,220));
//		footRow.getStyleAttributes().setBold(true);
		return tblMain.getRowCount();
	}
	 private void setNumberFormat(int currencyPrecision)
	  {
	    this.tblMain.getColumn("YearBeginDebit").getStyleAttributes().setNumberFormat(KDTableUtil.getNumberFormat(currencyPrecision, true));
	    this.tblMain.getColumn("YearBeginCredit").getStyleAttributes().setNumberFormat(KDTableUtil.getNumberFormat(currencyPrecision, true));
	    this.tblMain.getColumn("PeriodBeginDebit").getStyleAttributes().setNumberFormat(KDTableUtil.getNumberFormat(currencyPrecision, true));
	    this.tblMain.getColumn("PeriodBeginCredit").getStyleAttributes().setNumberFormat(KDTableUtil.getNumberFormat(currencyPrecision, true));
	    this.tblMain.getColumn("Debit").getStyleAttributes().setNumberFormat(KDTableUtil.getNumberFormat(currencyPrecision, true));
	    this.tblMain.getColumn("Credit").getStyleAttributes().setNumberFormat(KDTableUtil.getNumberFormat(currencyPrecision, true));
	    this.tblMain.getColumn("YearDebit").getStyleAttributes().setNumberFormat(KDTableUtil.getNumberFormat(currencyPrecision, true));
	    this.tblMain.getColumn("YearCredit").getStyleAttributes().setNumberFormat(KDTableUtil.getNumberFormat(currencyPrecision, true));
	    
	    this.tblMain.getColumn("SubDebit").getStyleAttributes().setNumberFormat(KDTableUtil.getNumberFormat(currencyPrecision, true));
	    this.tblMain.getColumn("SubCredit").getStyleAttributes().setNumberFormat(KDTableUtil.getNumberFormat(currencyPrecision, true));
	    
	    this.tblMain.getColumn("SubYearDebit").getStyleAttributes().setNumberFormat(KDTableUtil.getNumberFormat(currencyPrecision, true));
	    this.tblMain.getColumn("SubYearCredit").getStyleAttributes().setNumberFormat(KDTableUtil.getNumberFormat(currencyPrecision, true));
	    this.tblMain.getColumn("SubPeriodEndDebit").getStyleAttributes().setNumberFormat(KDTableUtil.getNumberFormat(currencyPrecision, true));
	    this.tblMain.getColumn("SubPeriodEndCredit").getStyleAttributes().setNumberFormat(KDTableUtil.getNumberFormat(currencyPrecision, true));
	    
	    
	  }
	  
	  private BigDecimal getFormateAmount(Object data, int precision)
	  {
	    InitClientHelp.ScaleHelp scaleHelp = new InitClientHelp.ScaleHelp();
	    scaleHelp.setScale(precision);
	    return scaleHelp.getScaleBigDecimal(data);
	  }
	  
	  protected String getViewPermission() {
		  return  "gl_balanceList_view";
	  }

	  protected String getPermissionItem()
	  {
	   return "gl_balanceList_view";
	  }
 
}