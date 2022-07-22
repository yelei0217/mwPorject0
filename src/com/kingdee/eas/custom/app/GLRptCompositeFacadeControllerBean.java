package com.kingdee.eas.custom.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.metadata.entity.SelectorItemCollection;
import com.kingdee.bos.metadata.entity.SelectorItemInfo;
import com.kingdee.eas.base.param.ParamControlFactory;
import com.kingdee.eas.basedata.assistant.CurrencyCollection;
import com.kingdee.eas.basedata.assistant.CurrencyFactory;
import com.kingdee.eas.basedata.assistant.CurrencyInfo;
import com.kingdee.eas.basedata.assistant.PeriodTypeCollection;
import com.kingdee.eas.basedata.assistant.PeriodTypeFactory;
import com.kingdee.eas.basedata.master.account.AccountTableCollection;
import com.kingdee.eas.basedata.master.account.AccountTableFactory;
import com.kingdee.eas.basedata.master.account.AccountTools;
import com.kingdee.eas.basedata.org.CompanyOrgUnitFactory;
import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.fi.gl.GLBalanceUtils;
import com.kingdee.eas.fi.gl.GlUtils;
import com.kingdee.eas.fi.gl.ReportBizException;
import com.kingdee.eas.fi.gl.app.InitHelp;
import com.kingdee.eas.fi.gl.common.GLResUtil;
import com.kingdee.eas.fi.gl.rpt.GLRptQueryInitData;
import com.kingdee.eas.framework.report.util.RptParams;
import com.kingdee.eas.util.app.ContextUtil;
import com.kingdee.eas.util.app.DbUtil;
import com.kingdee.jdbc.rowset.IRowSet;

public class GLRptCompositeFacadeControllerBean extends AbstractGLRptCompositeFacadeControllerBean
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -8760798377889160025L;
	private static Logger logger =
        Logger.getLogger("com.kingdee.eas.custom.app.GLRptCompositeFacadeControllerBean");
 
	@Override
	public int expendAllRow(Context ctx, String queryId, int rowIndex)
			throws BOSException, EASBizException {
 		return super.expendAllRow(ctx, queryId, rowIndex);
	}
	@Override
	public int expendRow(Context ctx, String queryId, int rowIndex)
			throws BOSException, EASBizException {
 		return super.expendRow(ctx, queryId, rowIndex);
	}
 
	public GLRptQueryInitData getQueryInitData(Context ctx,boolean isSupportUnion) throws BOSException, EASBizException 
	{
	   GLRptQueryInitData initData = new GLRptQueryInitData();
	   Map params = getGlobalParams(ctx);
	   setGlobalParamsValue(ctx, initData);
	   Object useRptCurrency = params.get("G001");
	   boolean isUseRptCurrency = useRptCurrency != null && Boolean.valueOf(useRptCurrency.toString()).booleanValue();
	   initData.setCurrencys(getCurrencys(ctx, isUseRptCurrency));
	   initData.setAccountTables(getAccountTableCollection(ctx, null, isSupportUnion));
	   initData.setPeriodTypes(getPeriodTypeCollection(ctx));
	   return initData;
	}
	
    private Map getGlobalParams(Context ctx)
    throws EASBizException, BOSException
	{
	   HashMap items = new HashMap();
	   items.put("G001", null);
	   items.put("G010", null);
	   items.put("GL_053", null);
	   items.put("GL_008", ContextUtil.getCurrentFIUnit(ctx).getId().toString());
	   return ParamControlFactory.getLocalInstance(ctx).getParamHashMap(items);
	}
	    
    private void setGlobalParamsValue(Context ctx, GLRptQueryInitData initData)
    throws EASBizException, BOSException
	{
	        Map params = getGlobalParams(ctx);
	        if(params != null)
	    {
	            Object qtyAsst = params.get("G010");
	            if(qtyAsst != null)
	                initData.setAsstQty(Boolean.valueOf(qtyAsst.toString()).booleanValue());
	           Object companyByUser = params.get("GL_053");
	            if(companyByUser != null)
	               initData.setCompanyByUser(Boolean.valueOf(companyByUser.toString()).booleanValue());
	    }
	}
    private Vector getCurrencys(Context ctx, boolean isUseReportCurrency)
    throws BOSException, EASBizException
	{
	        CurrencyCollection currencyCollection = getAllCurrencyCollection(ctx);
	        CurrencyInfo generalLocalCurrency = null;
	        CurrencyInfo generalRptCurrency = null;
	        Vector currencys = new Vector();
	        for(int i = 0; i < currencyCollection.size(); i++)
		    {
		            CurrencyInfo currency = currencyCollection.get(i);
		            if(currency != null && !currency.getId().toString().trim().equals(CurrencyInfo.GENERAL_LOCAL_CURRENCY_ID.toString()) && !currency.getId().toString().trim().equals(CurrencyInfo.GENERAL_REPORT_CURRENCY_ID.toString()))
		                currencys.add(currencyCollection.get(i));
		            if(currency != null && currency.getId().toString().trim().equals(CurrencyInfo.GENERAL_LOCAL_CURRENCY_ID.toString()))
		                generalLocalCurrency = currency;
		            if(currency != null && currency.getId().toString().trim().equals(CurrencyInfo.GENERAL_REPORT_CURRENCY_ID.toString()))
		               generalRptCurrency = currency;
		    }
		
	       if(!isForeignCurrency(ctx))
		    {
		       if(generalLocalCurrency != null && generalLocalCurrency.getName() != null && !generalLocalCurrency.getName().trim().equals(""))
		            currencys.add(generalLocalCurrency);
		      if(isUseReportCurrency && generalRptCurrency != null && generalRptCurrency.getName() != null && !generalRptCurrency.getName().trim().equals(""))
		           currencys.add(generalRptCurrency);
		    }
	       currencys.add(GLResUtil.getRes("all_currency", ctx.getLocale()));
	       return currencys;
	} 
	    
    
    private CurrencyCollection getAllCurrencyCollection(Context ctx)
    throws BOSException
	{
	       EntityViewInfo view = new EntityViewInfo();
	       SelectorItemCollection sic = view.getSelector();
	       sic.add(new SelectorItemInfo("id"));
	       sic.add(new SelectorItemInfo("number"));
	       sic.add(new SelectorItemInfo("name"));
	       sic.add(new SelectorItemInfo("precision"));
	       CurrencyCollection currencyCollection = CurrencyFactory.getLocalInstance(ctx).getCurrencyCollection(view);
	       return currencyCollection;
	}
    private boolean isForeignCurrency(Context ctx)throws EASBizException, BOSException
	{
	      HashMap items = new HashMap();
	      items.put("GL_008", ContextUtil.getCurrentFIUnit(ctx).getId().toString());
	      Map param = ParamControlFactory.getLocalInstance(ctx).getParamHashMap(items);
	      return Boolean.valueOf(param.get("GL_008").toString()).booleanValue();
	}
    
    
    
    private AccountTableCollection getAccountTableCollection(Context ctx, CompanyOrgUnitInfo company, boolean isSupportUnion)
    throws ReportBizException, BOSException
	{
	        if(company == null)
	            company = ContextUtil.getCurrentFIUnit(ctx);
	        if(!company.isIsBizUnit() && isSupportUnion)
	    {
	           return AccountTools.getAllAccountTableByCompany(ctx, company);
	    } 
	        else{
	            EntityViewInfo view = new EntityViewInfo();
	            SelectorItemCollection sic = view.getSelector();
	            sic.add(new SelectorItemInfo("id"));
	            sic.add(new SelectorItemInfo("number"));
	            sic.add(new SelectorItemInfo("name"));
	            return AccountTableFactory.getLocalInstance(ctx).getAccountTableCollection(view);
	    }
	}

	private PeriodTypeCollection getPeriodTypeCollection(Context ctx)throws ReportBizException, BOSException
	{
	       EntityViewInfo view = new EntityViewInfo();
	        SelectorItemCollection sic = view.getSelector();
	        sic.add(new SelectorItemInfo("id"));
	        sic.add(new SelectorItemInfo("number"));
	        sic.add(new SelectorItemInfo("name"));
	        return PeriodTypeFactory.getLocalInstance(ctx).getPeriodTypeCollection(view);
	}


	@Override
	public GLRptQueryInitData getQueryInitData(Context ctx, List companyIds,
			boolean isSupportUnion) throws BOSException {
 		try {
			return super.getQueryInitData(ctx, companyIds, isSupportUnion);
		} catch (EASBizException e) {
 			e.printStackTrace();
		}
		return null;
	}
	@Override
	public void relaseDataSource(Context ctx, String queryId)
			throws BOSException {
 		super.relaseDataSource(ctx, queryId);
	}
	@Override
	public int shrinkRow(Context ctx, String queryId, int rowIndex)
			throws BOSException, EASBizException {
 		return super.shrinkRow(ctx, queryId, rowIndex);
	}
	@Override
	public Map getPeriodRange(Context ctx, Set companyIdSet, String periodTypeId)
			throws BOSException, EASBizException {
		 return GlUtils.getPeriodRange(ctx, companyIdSet, periodTypeId);
	}
	
	private boolean isCompanyIsCity(Context ctx,String name){
		boolean flag = false ;
		  try {
			IRowSet rs = DbUtil.executeQuery(ctx, " select count(1) C from t_org_company where fparentid ='00000000-0000-0000-0000-000000000000CCE7AED4' and fisleaf = 0 and fname_l2= '"+name+"' and faccounttableid is not null");
			if(rs!=null && rs.size() > 0){
				if(rs.next()){
					if(rs.getObject("C") != null && !"".equals(rs.getObject("C").toString())){
						if("1".equals(rs.getObject("C").toString())) flag = true;
					}
				}
			}
		  } catch (BOSException e) {
 			e.printStackTrace();
		} catch (java.sql.SQLException e) {
 			e.printStackTrace();
		}

		return flag;
	}
	
	private List<CompanyOrgUnitInfo> getCompanysFilter(Context ctx,CompanyOrgUnitInfo[] companys) throws EASBizException, BOSException{
		List<CompanyOrgUnitInfo> list = new ArrayList<CompanyOrgUnitInfo>();
		if(companys !=null && companys.length >0){
			String comnumber = "";
			 for(CompanyOrgUnitInfo com :companys){
				 comnumber = com.getNumber();
				 if(!com.getNumber().contains("@02")){
					 comnumber = com.getNumber()+"@02";
				 }
 				 if(CompanyOrgUnitFactory.getLocalInstance(ctx).exists("where number = '"+comnumber+"'"))
 					list.add(com);
			 }
		} 
		return list;
	}
	@Override
	public RptParams query(Context ctx, RptParams params) throws BOSException,EASBizException {
		
		    boolean isOnlyLeaf = params.getBoolean("optionOnlyLeaf"); 
		    boolean isIncludePost =  params.getBoolean("includeBWAccount");
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
		    CompanyOrgUnitInfo company = (CompanyOrgUnitInfo) params.getObject("company") ;
		    
		    CompanyOrgUnitInfo curCompany = null ; 
		    if(params.getObject("companys") != null){
		    	List<CompanyOrgUnitInfo> companys =getCompanysFilter(ctx,(CompanyOrgUnitInfo[]) params.getObject("companys"));
		    	if(companys != null && companys.size() > 0){ 
		    		StringBuffer sbr = new StringBuffer("  SELECT cnumber,cname,dnumber,dname,faccountnumber,faccountname,");
 		    	//	sbr.append("  d.fnumber dnumber,d.fname_l2 dname,a.faccountnumber,a.faccountname,");
		    		//sbr.append(" a.fMEndBalance,a.FAdjustDebit,a.FAdjustCredit,a.fSEndBalance,a.FMDebit,a.FMCredit ,v.fisleaf,v.flevel FROM ("); 
		    		
		    		sbr.append("FYearBeginDebit,FYearBeginCredit,FPeriodBeginDebit,FPeriodBeginCredit,FDebit,FCredit,FYearDebit,FYearCredit,");
		    		sbr.append("FSubDebit,FSubCredit,FSubYearDebit,FSubYearCredit,FSubPeriodEndDebit,FSubPeriodEndCredit,FLevel,FIsLeaf FROM (");
				  String comnumber ="";
		    		List<String> tbs = new ArrayList<String>();
			    	for(int j =0 ; j < companys.size() ;j++){
			    		CompanyOrgUnitInfo com = companys.get(j);
					    comnumber = com.getNumber();
					    if(!com.getNumber().contains("@02")){
							 comnumber = com.getNumber()+"@02";
						 }else{ 
							 com = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo("where number = '"+comnumber.substring(0, comnumber.length()-3)+"'");
						 }
					    String masterCompanyId = com.getId().toString();

				    	curCompany = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo("where number = '"+comnumber+"'");
					    String curCompanyId = curCompany.getId().toString();
//						String tempTable = InitHelp.createTempTable(ctx, getTempTableStructure(), "BOOKSBALANCECMOPARE3141");
//						tbs.add(tempTable);
						
//					    sbr.append(getQuerySqlByCompanyId(ctx, isOnlyLeaf, isIncludePost,level, periodYear, periodNumber, masterCompanyId,curCompanyId,tempTable));
					
					    String tempTable1 = InitHelp.createTempTable(ctx, getTempTableStructure1(), "BOOKSBALANCECMOPARE3141");
						tbs.add(tempTable1); 
					//	
						sbr.append(getQuerySqlByCompanyId1(ctx, isOnlyLeaf, isIncludePost,level, periodYear, periodNumber, masterCompanyId,curCompanyId,tempTable1));
						
					  //   
						if(j < companys.size() -1)
						sbr.append(" UNION ALL  ");
			    	} 
			    	sbr.append(" ) as a ");
//		    		sbr.append(" ) a inner join t_BD_AccountView v on v.fid = a.faccountnumber and v.fcompanyid   \r\n inner join t_org_company c on c.fid = v.fcompanyid");
//		    		sbr.append(" inner join t_org_company d on d.fid = c.fparentid   \r\n"); 
		    		String tempTable = InitHelp.createTempTable(ctx, getResultTempTableStructure1(), "BOOKSBALANCECMOPARE3141");
		    		
		    		StringBuffer sql = new StringBuffer();	
		    		sql.append(" INSERT INTO ");
		   		    sql.append(tempTable);
		   		    sql.append(sbr.toString());
		   		  
		   		    DbUtil.execute(ctx, sql.toString());
		   		    
		   		    sql = new StringBuffer("/*dialect*/select * from (");
		   		    if(company.getLevel() ==1 ){
			   			 sql.append(" select '0' as pcon, 'M' con , '美维集团' cna,t1.faccountnumber,t1.faccountname,").append("\r\n");
				   		 
			   			 //sql.append(" sum(t1.fMEndBalance) fMEndBalance ,sum(t1.FAdjustDebit) FAdjustDebit,sum(t1.FAdjustCredit) FAdjustCredit,sum(t1.fSEndBalance) fSEndBalance,").append("\r\n");
				   		// sql.append(" sum(t1.FMDebit) FMDebit ,sum(t1.FMCredit) FMCredit,t1.flevel,t1.fisleaf,1 lv,1 ishow from ").append(tempTable).append(" t1 ").append("\r\n");
			 
						 sql.append(" sum(t1.FYearBeginDebit) as FYearBeginDebit,sum(t1.FYearBeginCredit) as FYearBeginCredit,sum(t1.FPeriodBeginDebit) as FPeriodBeginDebit,\r\n");
						 sql.append(" sum(t1.FPeriodBeginCredit) as FPeriodBeginCredit ,sum(t1.FDebit) as FDebit,sum(t1.FCredit) as FCredit,sum(t1.FYearDebit) as FYearDebit,sum(t1.FYearCredit) as FYearCredit,sum(t1.FSubDebit) as FSubDebit,sum(t1.FSubCredit) as FSubCredit,sum(t1.FSubYearDebit) as FSubYearDebit,\r\n");
						 sql.append(" sum(t1.FSubYearCredit) as FSubYearCredit,sum(t1.FSubPeriodEndDebit) as FSubPeriodEndDebit,sum(t1.FSubPeriodEndCredit) as FSubPeriodEndCredit,t1.FLevel,t1.FIsLeaf,1 lv,1 ishow from ").append(tempTable).append(" t1 ").append("\r\n");
 				   		 sql.append(" group by  t1.faccountnumber,t1.faccountname,t1.fisleaf,t1.flevel");
				   		 sql.append(" UNION ALL ");
				   		 
				   		sql.append(" select 'M' as pcon, t1.dnumber con,t1.dname as cna ,t1.faccountnumber,t1.faccountname,").append("\r\n");
				   		
		   		    	//sql.append(" sum(t1.fMEndBalance) fMEndBalance ,sum(t1.FAdjustDebit) FAdjustDebit,sum(t1.FAdjustCredit) FAdjustCredit,sum(t1.fSEndBalance) fSEndBalance,").append("\r\n");
		   		    	//sql.append(" sum(t1.FMDebit) FMDebit ,sum(t1.FMCredit) FMCredit,t1.flevel,t1.fisleaf,2 lv,1 ishow from ").append(tempTable).append(" t1 ").append("\r\n");
		   		    	
				   	 sql.append(" sum(t1.FYearBeginDebit) as FYearBeginDebit,sum(t1.FYearBeginCredit) as FYearBeginCredit,sum(t1.FPeriodBeginDebit) as FPeriodBeginDebit,\r\n");
					 sql.append(" sum(t1.FPeriodBeginCredit) as FPeriodBeginCredit ,sum(t1.FDebit) as FDebit,sum(t1.FCredit) as FCredit,sum(t1.FYearDebit) as FYearDebit,sum(t1.FYearCredit) as FYearCredit,sum(t1.FSubDebit) as FSubDebit,sum(t1.FSubCredit) as FSubCredit,sum(t1.FSubYearDebit) as FSubYearDebit,\r\n");
					 sql.append(" sum(t1.FSubYearCredit) as FSubYearCredit,sum(t1.FSubPeriodEndDebit) as FSubPeriodEndDebit,sum(t1.FSubPeriodEndCredit) as FSubPeriodEndCredit,t1.FLevel,t1.FIsLeaf,2 lv,1 ishow from ").append(tempTable).append(" t1 ").append("\r\n");

		   		     sql.append(" group by t1.dnumber,t1.dname,t1.faccountnumber,t1.faccountname,t1.fisleaf,t1.flevel").append("\r\n");
		   		     sql.append(" UNION ALL ");
		   		    	
		   		    }else if(company.getLevel() ==2 ){
		   		    	sql.append(" select'M' as pcon,t1.dnumber con,t1.dname as cna ,t1.faccountnumber,t1.faccountname,").append("\r\n");
		   		    	//sql.append(" sum(t1.fMEndBalance) fMEndBalance ,sum(t1.FAdjustDebit) FAdjustDebit,sum(t1.FAdjustCredit) FAdjustCredit,sum(t1.fSEndBalance) fSEndBalance,").append("\r\n");
		   		    	//sql.append(" sum(t1.FMDebit) FMDebit ,sum(t1.FMCredit) FMCredit,t1.flevel,t1.fisleaf,2 lv,1 ishow from ").append(tempTable).append(" t1 ").append("\r\n");
		   		    	
			   			 sql.append(" sum(t1.FYearBeginDebit) as FYearBeginDebit,sum(t1.FYearBeginCredit) as FYearBeginCredit,sum(t1.FPeriodBeginDebit) as FPeriodBeginDebit,\r\n");
						 sql.append(" sum(t1.FPeriodBeginCredit) as FPeriodBeginCredit ,sum(t1.FDebit) as FDebit,sum(t1.FCredit) as FCredit,sum(t1.FYearDebit) as FYearDebit,sum(t1.FYearCredit) as FYearCredit,sum(t1.FSubDebit) as FSubDebit,sum(t1.FSubCredit) as FSubCredit,sum(t1.FSubYearDebit) as FSubYearDebit, \r\n");
						 sql.append(" sum(t1.FSubYearCredit) as FSubYearCredit,sum(t1.FSubPeriodEndDebit) as FSubPeriodEndDebit,sum(t1.FSubPeriodEndCredit) as FSubPeriodEndCredit,t1.FLevel,t1.FIsLeaf,2 lv,1 ishow from ").append(tempTable).append(" t1 ").append("\r\n");

		   		    	sql.append(" group by t1.dnumber,t1.dname,t1.faccountnumber,t1.faccountname,t1.fisleaf,t1.flevel").append("\r\n");
		   		    	sql.append(" UNION ALL ");
		   		    }
		   		    	sql.append(" select dnumber as pcon,cnumber as con ,cname as cna ,t1.faccountnumber,t1.faccountname,").append("\r\n");
		   		    	//sql.append(" t1.FYearBeginDebit,t1.FAdjustDebit,t1.FAdjustCredit,t1.fSEndBalance,t1.FMDebit,t1.FMCredit,t1.flevel,t1.fisleaf,3 lv,1 ishow").append("\r\n");		   		    	
		   			  sql.append(" t1.FYearBeginDebit,t1.FYearBeginCredit,t1.FPeriodBeginDebit,t1.FPeriodBeginCredit,t1.FDebit,t1.FCredit,t1.FYearDebit,t1.FYearCredit,\r\n");
		   			  sql.append(" t1.FSubDebit,t1.FSubCredit,t1.FSubYearDebit,t1.FSubYearCredit,t1.FSubPeriodEndDebit,t1.FSubPeriodEndCredit,t1.FLevel,t1.FIsLeaf,3 lv,1 ishow \r\n");
		   			 // sql.append(" sum(FSubYearCredit),sum(FSubPeriodEndDebit),sum(FSubPeriodEndCredit),FLevel,FIsLeaf from \r\n");
		   			  
		   			 // t1.FYearBeginDebit,t1.FYearBeginCredit,t1.FPeriodBeginDebit,t1.FPeriodBeginCredit,t1.FDebit,t1.FCredit,t1.FYearDebit,t1.FYearCredit,
		   			 // t1.FSubDebit,t1.FSubCredit,t1.FSubYearDebit,t1.FSubYearCredit,t1.FSubPeriodEndDebit,t1.FSubPeriodEndCredit,t1.FLevel,t1.FIsLeaf,3 lv,1 ishow
		   			  
		   		    	sql.append(" from ").append(tempTable).append(" t1 ").append("\r\n");
		   		    	sql.append("  ) t order by faccountnumber,con  ");
		   		    IRowSet rs = DbUtil.executeQuery(ctx, sql.toString());
				    if(rs != null &&  rs.size() > 0)
						 params.setObject("rs", rs); 
				    
				    InitHelp.dropTempTable(ctx, tempTable);
				    if(tbs!=null && tbs.size() > 0){
				    	for(String ta :tbs){
						    InitHelp.dropTempTable(ctx, ta);
				    	}
				    }
		    	}
		    }
			  
 		return params;
	}
	
	private String getQuerySqlByCompanyId1(Context ctx, boolean isOnlyLeaf,
			boolean isIncludePost, int level, int periodYear, int periodNumber,
			String masterCompanyId, String curCompanyId,String tempTable) throws BOSException {
		String currencyId = "11111111-1111-1111-1111-111111111111DEB58FDC";
		int baltype = isIncludePost ? 1 : 5;
		boolean isForCurrency = false;
		StringBuffer sql = null;
		  Object[] param = null;
		  sql = new StringBuffer();
		  sql.append("    INSERT INTO ");
		  sql.append(tempTable);
		  sql.append(" (FTypeID,FAccountNumber,FAccountName,FYearBeginDebit,FYearBeginCredit,FPeriodBeginDebit,FPeriodBeginCredit,FDebit,FCredit,  \r\n");
		  sql.append(" FYearDebit,FYearCredit,FSubDebit,FSubCredit,FSubYearDebit,FSubYearCredit,FSubPeriodEndDebit,FSubPeriodEndCredit,FLevel,FIsLeaf) \r\n");
		  
		  sql.append("(select 1 ftypeid,av.fnumber FAccountNumber, av.fname_l2 FAccountName,");
		  sql.append("sum(CASE WHEN TB.FEndBalanceLocal+TB.FYearCreditLocal-TB.FYearDebitLocal> 0 THEN (TB.FEndBalanceLocal+TB.FYearCreditLocal-TB.FYearDebitLocal) ELSE 0.0 END) FYearBeginDebit");
		  sql.append(",sum(CASE WHEN TB.FEndBalanceLocal+TB.FYearCreditLocal-TB.FYearDebitLocal< 0 THEN -1 *(TB.FEndBalanceLocal+TB.FYearCreditLocal-TB.FYearDebitLocal) ELSE 0.0 END) FYearBeginCredit");
		  sql.append(",sum(CASE WHEN TB.FBeginBalanceLocal > 0 THEN (TB.FBeginBalanceLocal) ELSE 0.0 END) FPeriodBeginDebit");
		  sql.append(",sum(CASE WHEN TB.FBeginBalanceLocal < 0 THEN -1 *(TB.FBeginBalanceLocal) ELSE 0.0 END) FPeriodBeginCredit");
		 // sql.append(",sum(CASE WHEN TB.FEndBalanceLocal > 0 THEN (TB.FEndBalanceLocal) ELSE 0.0 END) FPeriodEndDebit");
		//  sql.append(",sum(CASE WHEN TB.FEndBalanceLocal < 0 THEN -1 *(TB.FEndBalanceLocal) ELSE 0.0 END) FPeriodEndCredit");
		  sql.append(",sum(TB.FDebitLocal) FDebit,sum(TB.FCreditLocal) FCredit,sum(TB.FYearDebitLocal) FYearDebit,sum(TB.FYearCreditLocal) FYearCredit");
		  sql.append(",0.0 as FSubDebit,0.0 as FSubCredit,0.0 as FSubYearDebit,0.0 as FSubYearCredit,0.0 as FSubPeriodEndDebit,0.0 as FSubPeriodEndCredit");
		  sql.append(",av.FLevel,av.FIsLeaf");
		  sql.append(" from " + GLBalanceUtils.getAccountBalanceTable(baltype, currencyId) + " TB \r\n");
		  sql.append(" inner join  t_bd_accountview av on av.fid=TB.FAccountID   \r\n");
		  sql.append(" where TB.forgunitid=? \r\n");
		  if (isForCurrency) {
			    sql.append(" and TB.fcurrencyid=? \r\n");
		   }
			  sql.append(" and TB.fperiod=?   \r\n");
			if (isOnlyLeaf) {
				    sql.append(" and av.fisleaf=1 ");
			 } else {
				    sql.append(" and av.flevel <=? ");
			 }
			  
		  sql.append("   group by av.fname_l2,av.fnumber,av.FLevel,av.FIsLeaf  )  \r\n");
		  if (isOnlyLeaf)
		  {
		    if (isForCurrency) {
		      param = new Object[] { masterCompanyId, currencyId, Integer.valueOf(periodYear * 100 + periodNumber) };
		    } else {
		      param = new Object[] { masterCompanyId, Integer.valueOf(periodYear * 100 + periodNumber) };
		    }
		  }
		  else if (isForCurrency) {
		    param = new Object[] { masterCompanyId, currencyId, Integer.valueOf(periodYear * 100 + periodNumber), new Integer(level) };
		  } else {
		    param = new Object[] { masterCompanyId, Integer.valueOf(periodYear * 100 + periodNumber), new Integer(level) };
		  }
		  DbUtil.execute(ctx, sql.toString(), param);
		  
		  sql = new StringBuffer();
		  sql.append("    INSERT INTO ");
		  sql.append(tempTable);
		  sql.append(" (FTypeID,FAccountNumber,FAccountName,FYearBeginDebit,FYearBeginCredit,FPeriodBeginDebit,FPeriodBeginCredit,FDebit,FCredit,  \r\n");
		  sql.append(" FYearDebit,FYearCredit,FSubDebit,FSubCredit,FSubYearDebit,FSubYearCredit,FSubPeriodEndDebit,FSubPeriodEndCredit,FLevel,FIsLeaf) \r\n");
		  
		  sql.append("(select 2 ftypeid,av.fnumber FAccountNumber, av.fname_l2 FAccountName,");
		  sql.append("0.0 as FYearBeginDebit,0.0 as FYearBeginCredit,0.0 as FPeriodBeginDebit,0.0 as FPeriodBeginCredit");
		  sql.append(", 0.0 as FDebit,0.0 as FCredit,0.0 as FYearDebit,0.0 as FYearCredit"); 
		  sql.append(", 0.0 as FSubDebit,0.0 as FSubCredit,0.0 as FSubYearDebit,0.0 as FSubYearCredit"); 
		  sql.append(", sum(CASE WHEN TB.FEndBalanceLocal > 0 THEN (TB.FEndBalanceLocal) ELSE 0.0 END) FSubPeriodEndDebit");
		  sql.append(", sum(CASE WHEN TB.FEndBalanceLocal < 0 THEN -1 *(TB.FEndBalanceLocal) ELSE 0.0 END) FSubPeriodEndCredit"); 
		  sql.append(", av.FLevel,av.FIsLeaf");
		  sql.append(" from " + GLBalanceUtils.getAccountBalanceTable(baltype, currencyId) + " TB \r\n");
		  sql.append(" inner join  t_bd_accountview av on av.fid=TB.FAccountID   \r\n");
		  sql.append(" where TB.forgunitid=? \r\n");
		  if (isForCurrency) {
			    sql.append(" and TB.fcurrencyid=? \r\n");
		   }
			  sql.append(" and TB.fperiod=?   \r\n");
			if (isOnlyLeaf) {
				    sql.append(" and av.fisleaf=1 ");
			 } else {
				    sql.append(" and av.flevel <=? ");
			 }
		  sql.append(" group by av.fname_l2,av.fnumber,av.FLevel,av.FIsLeaf ) \r\n");
		  if (isOnlyLeaf)
		  {
		    if (isForCurrency) {
		      param = new Object[] { curCompanyId, currencyId, Integer.valueOf(periodYear * 100 + periodNumber) };
		    } else {
		      param = new Object[] { curCompanyId, Integer.valueOf(periodYear * 100 + periodNumber) };
		    }
		  }
		  else if (isForCurrency) {
		    param = new Object[] { curCompanyId, currencyId, Integer.valueOf(periodYear * 100 + periodNumber), new Integer(level) };
		  } else {
		    param = new Object[] { curCompanyId, Integer.valueOf(periodYear * 100 + periodNumber), new Integer(level) };
		  }
		   
		 DbUtil.execute(ctx, sql.toString(), param);
		  
//		  sql = new StringBuffer();
//		  sql.append("    INSERT INTO ");
//		  sql.append(tempTable);
//		  sql.append(" (FTypeID,FAccountNumber,FAccountName,FYearBeginDebit,FYearBeginCredit,FPeriodBeginDebit,FPeriodBeginCredit,FDebit,FCredit,  \r\n");
//		  sql.append(" FYearDebit,FYearCredit,FSubDebit,FSubCredit,FSubYearDebit,FSubYearCredit,FSubPeriodEndDebit,FSubPeriodEndCredit,FLevel,FIsLeaf) \r\n"); 
//		  sql.append(" (SELECT 3 ftypeid,acct1.fnumber FAccountNumber, acct1.fname_l2 FAccountName,");
//		  sql.append("0.0 as FYearBeginDebit,0.0 as FYearBeginCredit,0.0 as FPeriodBeginDebit,0.0 as FPeriodBeginCredit");
//		  sql.append(",0.0 as FDebit,0.0 as FCredit,0.0 as FYearDebit,0.0 as FYearCredit"); 
//		  sql.append(",sum(case entry.FEntryDC when 1 then entry.FLocalAmount else 0 end) as FSubDebit,sum(case entry.FEntryDC when 0 then entry.FLocalAmount else 0 end) as FSubCredit"); 
//		  sql.append(",0.0 as FSubYearDebit,0.0 as FSubYearCredit,0.0 as FSubPeriodEndDebit,0.0 as FSubPeriodEndCredit"); 
//		  sql.append(",acct1.FLevel,acct1.FIsLeaf"); 
//		  sql.append(" from T_GL_Voucher vch    \r\n");
//		  sql.append("   inner join T_GL_VoucherEntry entry on entry.fbillid=vch.fid \r\n");
//		  sql.append("   inner join t_bd_period pi on pi.fid=vch.fperiodid    \r\n");
//		  sql.append("   inner join t_bd_accountview acct1 on acct1.fid=entry.faccountid    \r\n");
//		  //sql.append("   inner join t_BD_AccountView pacct  on pacct.Fcompanyid = acct1.Fcompanyid  and pacct.faccounttableid = acct1.faccounttableid and  (charindex(pacct.FLongNumber || '!', acct1.FLongNumber) = 1 or pacct.fid = acct1.fid)        \r\n");
//		//  sql.append("  WHERE vch.fcompanyid=? and pi.fperiodyear =? and pi.fperiodnumber=? and (vch.fabstract !='结转损益' and vch.FDescription != '从主业务账簿引入' or vch.FDescription is null) ");
// 		  sql.append("  WHERE vch.fcompanyid=? and pi.fperiodyear =? and pi.fperiodnumber=? and vch.FDescription is null ");
//		  if (isOnlyLeaf)
//		  {
//		    sql.append(" and pacct.fisleaf=1 ");
//		    sql.append(" and acct1.fisleaf=1 ");
//		  }
//		  else
//		  {
//		    sql.append(" and (acct1.flevel <=? or acct1.fisleaf=1)");
//		  }
//		  if (isIncludePost) {
//		    sql.append("       and vch.FBizStatus>0 and vch.FBizStatus !=2    \r\n");
//		  } else {
//		    sql.append("       and vch.FBizStatus=5     \r\n");
//		  }
//		
//		  if (isOnlyLeaf)
//		  {
//		  //  sql.append(")  \r\n");
//		    param = new Object[] { curCompanyId, new Integer(periodYear), new Integer(periodNumber) };
//		  }
//		  else
//		  {
//		    sql.append("and acct1.flevel <=? \r\n");
//		    param = new Object[] { curCompanyId, new Integer(periodYear), new Integer(periodNumber), new Integer(level), new Integer(level) };
//		  }
//		  sql.append(" GROUP BY  acct1.fnumber,acct1.fname_l2,acct1.FLevel,acct1.FIsLeaf ) ");
//		  DbUtil.execute(ctx, sql.toString(), param);
		   
		  
		  sql = new StringBuffer();
		  sql.append("    INSERT INTO ");
		  sql.append(tempTable);
		  sql.append(" (FTypeID,FAccountNumber,FAccountName,FYearBeginDebit,FYearBeginCredit,FPeriodBeginDebit,FPeriodBeginCredit,FDebit,FCredit,  \r\n");
		  sql.append(" FYearDebit,FYearCredit,FSubDebit,FSubCredit,FSubYearDebit,FSubYearCredit,FSubPeriodEndDebit,FSubPeriodEndCredit,FLevel,FIsLeaf) \r\n");
		  sql.append(" select * from (SELECT 3 ftypeid,pacct.fnumber FAccountNumber, pacct.fname_l2 FAccountName,");
		  sql.append("0.0 as FYearBeginDebit,0.0 as FYearBeginCredit,0.0 as FPeriodBeginDebit,0.0 as FPeriodBeginCredit");
		  sql.append(",0.0 as FDebit,0.0 as FCredit,0.0 as FYearDebit,0.0 as FYearCredit"); 
		  sql.append(",sum(case entry.FEntryDC when 1 then entry.FLocalAmount else 0 end) as FSubDebit,sum(case entry.FEntryDC when 0 then entry.FLocalAmount else 0 end) as FSubCredit"); 
		  sql.append(",0.0 as FSubYearDebit,0.0 as FSubYearCredit  \r\n");
 		  sql.append(",0.0 as FSubPeriodEndDebit,0.0 as FSubPeriodEndCredit,pacct.FLevel,pacct.FIsLeaf"); 
		  sql.append(" from T_GL_Voucher vch        \r\n");
		  sql.append("   inner join T_GL_VoucherEntry entry on entry.fbillid=vch.fid \r\n");
		  sql.append("   inner join t_bd_period pi on pi.fid=vch.fperiodid    \r\n");
		  sql.append("   inner join t_bd_accountview acct1 on acct1.fid=entry.faccountid    \r\n"); 
		  sql.append("   inner join t_BD_AccountView pacct  on pacct.Fcompanyid = acct1.Fcompanyid    and pacct.faccounttableid = acct1.faccounttableid and  (charindex(pacct.FLongNumber || '!', acct1.FLongNumber) = 1 or pacct.fid = acct1.fid)        \r\n");
		  sql.append("  WHERE vch.fcompanyid=? and vch.FSourceType not in (1,5) and pi.fperiodyear =? and pi.fperiodnumber=? ");
		  if (isOnlyLeaf)
		  {
		    sql.append(" and pacct.fisleaf=1 ");
		    sql.append(" and acct1.fisleaf=1 ");
		  }
		  else
		  {
		    sql.append(" and (acct1.flevel <=? or acct1.fisleaf=1)");
		  }
		  if (isIncludePost) {
		    sql.append("       and vch.FBizStatus>0 and vch.FBizStatus !=2    \r\n");
		  } else {
		    sql.append("       and vch.FBizStatus=5     \r\n");
		  }
		   sql.append("   GROUP BY  pacct.fnumber,pacct.fname_l2 ,pacct.FLevel,pacct.FIsLeaf )");
		//  sql.append("   GROUP BY  pacct.fnumber,pacct.fname_l2 ,pacct.FLevel,pacct.FIsLeaf  ) as temp inner join t_BD_AccountView av on temp.FAccountNumber = av.fnumber ");
		  if (isOnlyLeaf)
		  {
		 //   sql.append(")  \r\n");
		    param = new Object[] { curCompanyId, new Integer(periodYear), new Integer(periodNumber) };
		  }
		  else
		  {
		    sql.append(" as temp  where temp.flevel <= ?  \r\n");
		    param = new Object[] { curCompanyId, new Integer(periodYear), new Integer(periodNumber), new Integer(level), new Integer(level) };
		  }
		  DbUtil.execute(ctx, sql.toString(), param);
		  
		  
		  
		  
		  sql = new StringBuffer();
		  sql.append("    INSERT INTO ");
		  sql.append(tempTable);
		  sql.append(" (FTypeID,FAccountNumber,FAccountName,FYearBeginDebit,FYearBeginCredit,FPeriodBeginDebit,FPeriodBeginCredit,FDebit,FCredit,  \r\n");
		  sql.append(" FYearDebit,FYearCredit,FSubDebit,FSubCredit,FSubYearDebit,FSubYearCredit,FSubPeriodEndDebit,FSubPeriodEndCredit,FLevel,FIsLeaf) \r\n");
		  sql.append(" select * from (SELECT 4 ftypeid,pacct.fnumber FAccountNumber, pacct.fname_l2 FAccountName,");
		  sql.append("0.0 as FYearBeginDebit,0.0 as FYearBeginCredit,0.0 as FPeriodBeginDebit,0.0 as FPeriodBeginCredit");
		  sql.append(",0.0 as FDebit,0.0 as FCredit,0.0 as FYearDebit,0.0 as FYearCredit,0.0 as FSubDebit,0.0 as FSubCredit"); 
		  sql.append(",  sum(case entry.FEntryDC when 1 then entry.FLocalAmount else 0 end) FSubYearDebit  \r\n");
		  sql.append(",  sum(case entry.FEntryDC when 0 then entry.FLocalAmount else 0 end) FSubYearCredit  \r\n");
		  sql.append(",0.0 as FSubPeriodEndDebit,0.0 as FSubPeriodEndCredit,pacct.FLevel,pacct.FIsLeaf"); 
		  sql.append(" from T_GL_Voucher vch        \r\n");
		  sql.append("   inner join T_GL_VoucherEntry entry on entry.fbillid=vch.fid \r\n");
		  sql.append("   inner join t_bd_period pi on pi.fid=vch.fperiodid    \r\n");
		  sql.append("   inner join t_bd_accountview acct1 on acct1.fid=entry.faccountid    \r\n"); 
		  sql.append("   inner join t_BD_AccountView pacct  on pacct.Fcompanyid = acct1.Fcompanyid    and pacct.faccounttableid = acct1.faccounttableid and  (charindex(pacct.FLongNumber || '!', acct1.FLongNumber) = 1 or pacct.fid = acct1.fid)        \r\n");
		  sql.append("  WHERE vch.fcompanyid=? and vch.FSourceType not in (1,5) and pi.fperiodyear =? and pi.fperiodnumber<=? ");
		  if (isOnlyLeaf)
		  {
		    sql.append(" and pacct.fisleaf=1 ");
		    sql.append(" and acct1.fisleaf=1 ");
		  }
		  else
		  {
		    sql.append(" and (acct1.flevel <=? or acct1.fisleaf=1)");
		  }
		  if (isIncludePost) {
		    sql.append("       and vch.FBizStatus>0 and vch.FBizStatus !=2    \r\n");
		  } else {
		    sql.append("       and vch.FBizStatus=5     \r\n");
		  }
		   sql.append("   GROUP BY   pacct.fnumber,pacct.fname_l2 ,pacct.FLevel,pacct.FIsLeaf  )");
		//  sql.append("   GROUP BY  pacct.fnumber,pacct.fname_l2 ,pacct.FLevel,pacct.FIsLeaf  ) as temp inner join t_BD_AccountView av on temp.FAccountNumber = av.fnumber ");
		  if (isOnlyLeaf)
		  {
		 //   sql.append(")  \r\n");
		    param = new Object[] { curCompanyId, new Integer(periodYear), new Integer(periodNumber) };
		  }
		  else
		  {
			  sql.append(" as temp  where temp.flevel <= ?  \r\n");
			  param = new Object[] { curCompanyId, new Integer(periodYear), new Integer(periodNumber), new Integer(level), new Integer(level) };
		  }
		  DbUtil.execute(ctx, sql.toString(), param);
	
		  
		  
		  
//		  sql = new StringBuffer();
//		  sql.append("    INSERT INTO ");
//		  sql.append(tempTable);
//		  sql.append(" (FTypeID,FAccountNumber,FAccountName,FYearBeginDebit,FYearBeginCredit,FPeriodBeginDebit,FPeriodBeginCredit,FDebit,FCredit,  \r\n");
//		  sql.append(" FYearDebit,FYearCredit,FSubDebit,FSubCredit,FSubYearDebit,FSubYearCredit,FSubPeriodEndDebit,FSubPeriodEndCredit,FLevel,FIsLeaf) \r\n"); 
//		  sql.append(" (SELECT 4 ftypeid,acct1.fnumber FAccountNumber, acct1.fname_l2 FAccountName,");
//		  sql.append("0.0 as FYearBeginDebit,0.0 as FYearBeginCredit,0.0 as FPeriodBeginDebit,0.0 as FPeriodBeginCredit");
//		  sql.append(",0.0 as FDebit,0.0 as FCredit,0.0 as FYearDebit,0.0 as FYearCredit,0.0 FSubDebit,0.0 FSubCredit"); 
//  		  sql.append(",  sum(case entry.FEntryDC when 1 then entry.FLocalAmount else 0 end) FSubYearDebit  \r\n");
//		  sql.append(",  sum(case entry.FEntryDC when 0 then entry.FLocalAmount else 0 end) FSubYearCredit  \r\n");
//		  sql.append(",0.0 as FSubPeriodEndDebit,0.0 as FSubPeriodEndCredit,acct1.FLevel,acct1.FIsLeaf"); 
//		  sql.append(" from T_GL_Voucher vch    \r\n");
//		  sql.append("   inner join T_GL_VoucherEntry entry on entry.fbillid=vch.fid \r\n");
//		  sql.append("   inner join t_bd_period pi on pi.fid=vch.fperiodid    \r\n");
//		  sql.append("   inner join t_bd_accountview acct1 on acct1.fid=entry.faccountid    \r\n");
//		  //sql.append("   inner join t_BD_AccountView pacct  on pacct.Fcompanyid = acct1.Fcompanyid  and pacct.faccounttableid = acct1.faccounttableid and  (charindex(pacct.FLongNumber || '!', acct1.FLongNumber) = 1 or pacct.fid = acct1.fid)        \r\n");
//		  sql.append("  WHERE vch.fcompanyid=? and pi.fperiodyear =? and pi.fperiodnumber=? and vch.FDescription is null ");
//		  //sql.append("  WHERE vch.fcompanyid=? and pi.fperiodyear =? and pi.fperiodnumber<=? "); 
//		  if (isOnlyLeaf)
//		  {
//		    sql.append(" and pacct.fisleaf=1 ");
//		    sql.append(" and acct1.fisleaf=1 ");
//		  }
//		  else
//		  {
//		    sql.append(" and (acct1.flevel <=? or acct1.fisleaf=1)");
//		  }
//		  if (isIncludePost) {
//		    sql.append("       and vch.FBizStatus>0 and vch.FBizStatus !=2    \r\n");
//		  } else {
//		    sql.append("       and vch.FBizStatus=5     \r\n");
//		  }
//		
//		  if (isOnlyLeaf)
//		  {
//		  //  sql.append(")  \r\n");
//		    param = new Object[] { curCompanyId, new Integer(periodYear), new Integer(periodNumber) };
//		  }
//		  else
//		  {
//		    sql.append("and acct1.flevel <=? \r\n");
//		    param = new Object[] { curCompanyId, new Integer(periodYear), new Integer(periodNumber), new Integer(level), new Integer(level) };
//		  }
//		  sql.append(" GROUP BY  acct1.fnumber,acct1.fname_l2,acct1.FLevel,acct1.FIsLeaf ) ");
//		  DbUtil.execute(ctx, sql.toString(), param);
		  
		  
		  
		  
		  sql = new StringBuffer();
		  sql.append("    INSERT INTO ");
		  sql.append(tempTable);
		  sql.append(" (FTypeID,FAccountNumber,FAccountName,FYearBeginDebit,FYearBeginCredit,FPeriodBeginDebit,FPeriodBeginCredit,FDebit,FCredit,  \r\n");
		  sql.append(" FYearDebit,FYearCredit,FSubDebit,FSubCredit,FSubYearDebit,FSubYearCredit,FSubPeriodEndDebit,FSubPeriodEndCredit,FLevel,FIsLeaf) \r\n");
		  sql.append(" (SELECT 5 ftypeid, FAccountNumber,FAccountName,sum(FYearBeginDebit),sum(FYearBeginCredit),sum(FPeriodBeginDebit),\r\n");
		  sql.append(" sum(FPeriodBeginCredit),sum(FDebit),sum(FCredit),sum(FYearDebit),sum(FYearCredit),sum(FSubDebit),sum(FSubCredit),sum(FSubYearDebit),\r\n");
		  sql.append(" sum(FSubYearCredit),sum(FSubPeriodEndDebit),sum(FSubPeriodEndCredit),FLevel,FIsLeaf from \r\n");
		  sql.append(tempTable);
		  sql.append("  group by FAccountNumber,FAccountName,FLevel,FIsLeaf ) \r\n");
		  sql.append(" \r\n");
		  DbUtil.execute(ctx, sql.toString());
		  
		  sql = new StringBuffer();
		  sql.append(" DELETE from  ").append(tempTable).append(" where FTypeID < 5");
		  DbUtil.execute(ctx, sql.toString());
		  
		  sql = new StringBuffer();
		  sql.append(" select FTypeID, \r\n");  
		  sql.append("(select p.fnumber from t_org_company p inner join t_org_company c on p.fid = c.fparentid where c.fid = '").append(masterCompanyId).append("' ) dnumber,");
		  sql.append("(select p.fname_l2 from t_org_company p inner join t_org_company c on p.fid = c.fparentid where  c.fid = '").append(masterCompanyId).append("' ) dname,");
		  sql.append("(select fnumber from t_org_company where fid = '").append(masterCompanyId).append("' ) cnumber,");
		  sql.append("(select fname_l2 from t_org_company where fid = '").append(masterCompanyId).append("' ) cname,"); 
		  sql.append("FAccountNumber,FAccountName,FYearBeginDebit,FYearBeginCredit,FPeriodBeginDebit,FPeriodBeginCredit,FDebit,FCredit,");
		  sql.append(" FYearDebit,FYearCredit,FSubDebit,FSubCredit,FSubYearDebit,FSubYearCredit,FSubPeriodEndDebit,FSubPeriodEndCredit,FLevel,FIsLeaf \r\n");
		  sql.append(" from ").append(tempTable);
		//  param = new Object[] { masterCompanyId, masterCompanyId, masterCompanyId, masterCompanyId };
		  
		  
		  
		  return sql.toString();
	}
	private String getQuerySqlByCompanyId(Context ctx, boolean isOnlyLeaf,
			boolean isIncludePost, int level, int periodYear, int periodNumber,
			String masterCompanyId, String curCompanyId,String tempTable) throws BOSException {
		String currencyId = "11111111-1111-1111-1111-111111111111DEB58FDC";
		int baltype = isIncludePost ? 1 : 5;
		boolean isForCurrency = false;
		StringBuffer sql = null;
		  Object[] param = null;
		  sql = new StringBuffer();
		  sql.append("    INSERT INTO ");
		  sql.append(tempTable);
		  sql.append("  (ftypeid,faccountid,faccountnumber,faccountname,fsubacctid,fsubacctnumber,fsubacctname,fmendbalance,FMDebit,FMCredit)   \r\n");
		  sql.append("  (SELECT 1 ftypeid,acct1.fid, acct1.fnumber as faccountnumber ,acct1.fname_l2 as faccountname ,");
		  sql.append(" acct1.fid as fsubacctid,acct1.fnumber as fsubacctnumber,acct1.fname_l2 as fsubacctname ,");
		  if (currencyId.equals(CurrencyInfo.GENERAL_LOCAL_CURRENCY_ID.toString())) {
		    sql.append("  bal1.FEndBalanceLocal as fmendbalance  ");
		    sql.append(",bal1.FDebitLocal as FMDebit,bal1.FCreditLocal as FMCredit");
		  } else if (currencyId.equals(CurrencyInfo.GENERAL_REPORT_CURRENCY_ID.toString())) {
		    sql.append("  bal1.FEndBalanceRpt as fmendbalance  ");
		    sql.append(",bal1.FDebitRpt as FMDebit,bal1.FCreditRpt as FMCredit");
		  }
		 
		  sql.append("  from " + GLBalanceUtils.getAccountBalanceTable(baltype, currencyId) + " bal1   \r\n");
		  sql.append(" inner join  t_bd_accountview acct1 on acct1.fid=bal1.FAccountID   \r\n");
		  sql.append(" where bal1.forgunitid=? \r\n");
		  if (isForCurrency) {
		    sql.append(" and bal1.fcurrencyid=? \r\n");
		  }
		  sql.append(" and bal1.fperiod=?   \r\n");
		  if (isOnlyLeaf) {
		    sql.append(" and acct1.fisleaf=1 ");
		  } else {
		    sql.append(" and acct1.flevel <=? ");
		  }
//			  if (currencyId.equals(CurrencyInfo.GENERAL_LOCAL_CURRENCY_ID.toString())) {
//			    sql.append("  and bal1.FEndBalanceLocal <> 0");
//			  } else if (currencyId.equals(CurrencyInfo.GENERAL_REPORT_CURRENCY_ID.toString())) {
//			    sql.append("  and bal1.FEndBalanceRpt <> 0  ");
//			  }
		  sql.append("   )     \r\n");
		  if (isOnlyLeaf)
		  {
		    if (isForCurrency) {
		      param = new Object[] { masterCompanyId, currencyId, Integer.valueOf(periodYear * 100 + periodNumber) };
		    } else {
		      param = new Object[] { masterCompanyId, Integer.valueOf(periodYear * 100 + periodNumber) };
		    }
		  }
		  else if (isForCurrency) {
		    param = new Object[] { masterCompanyId, currencyId, Integer.valueOf(periodYear * 100 + periodNumber), new Integer(level) };
		  } else {
		    param = new Object[] { masterCompanyId, Integer.valueOf(periodYear * 100 + periodNumber), new Integer(level) };
		  }
		  DbUtil.execute(ctx, sql.toString(), param);
		  
		  sql = new StringBuffer();
		  sql.append("    INSERT INTO ");
		  sql.append(tempTable);
		  sql.append("  (ftypeid,faccountid,faccountnumber,faccountname,fsubacctid,fsubacctnumber,fsubacctname,fsendbalance,FMDebit,FMCredit)   \r\n");
		  sql.append("(SELECT 2 ftypeid,acct1.fid,acct1.fnumber,acct1.fname_l2,");
		  sql.append(" acct1.fid as fsubacctid, acct1.fnumber as fsubacctnumber,acct1.fname_l2 as fsubacctname, ");
		  if (currencyId.equals(CurrencyInfo.GENERAL_LOCAL_CURRENCY_ID.toString())) {
		    sql.append("  bal1.FEndBalanceLocal as fsendbalance  ");
		  } else if (currencyId.equals(CurrencyInfo.GENERAL_REPORT_CURRENCY_ID.toString())) {
		    sql.append("  bal1.FEndBalanceRpt as fsendbalance  ");
		  }
		  sql.append(",0 as FMDebit,0 as FMCredit");
		  sql.append(" from " + GLBalanceUtils.getAccountBalanceTable(baltype, currencyId) + " bal1   \r\n");
		  sql.append(" inner join  t_bd_accountview acct1 on acct1.fid=bal1.FAccountID   \r\n");
		  sql.append(" where bal1.forgunitid=? \r\n");
		  if (isForCurrency) {
		    sql.append(" and bal1.fcurrencyid=? \r\n");
		  }
		 
		  sql.append(" and bal1.fperiod=?   \r\n");
		  if (isOnlyLeaf) {
		    sql.append(" and acct1.fisleaf=1 ");
		  } else {
		    sql.append(" and acct1.flevel <=? ");
		  }
//			  if (currencyId.equals(CurrencyInfo.GENERAL_LOCAL_CURRENCY_ID.toString())) {
//			    sql.append("  and bal1.FEndBalanceLocal <>0");
//			  } else if (currencyId.equals(CurrencyInfo.GENERAL_REPORT_CURRENCY_ID.toString())) {
//			    sql.append("  and bal1.FEndBalanceRpt <>0  ");
//			  }
		  sql.append(" )  \r\n");
		  if (isOnlyLeaf)
		  {
		    if (isForCurrency) {
		      param = new Object[] { curCompanyId, currencyId, Integer.valueOf(periodYear * 100 + periodNumber) };
		    } else {
		      param = new Object[] { curCompanyId, Integer.valueOf(periodYear * 100 + periodNumber) };
		    }
		  }
		  else if (isForCurrency) {
		    param = new Object[] { curCompanyId, currencyId, Integer.valueOf(periodYear * 100 + periodNumber), new Integer(level) };
		  } else {
		    param = new Object[] { curCompanyId, Integer.valueOf(periodYear * 100 + periodNumber), new Integer(level) };
		  }
		  DbUtil.execute(ctx, sql.toString(), param);
		  
		  sql = new StringBuffer();
		  sql.append("    INSERT INTO ");
		  sql.append(tempTable);
		  sql.append("  (ftypeid,faccountid,faccountnumber,faccountname,fsubacctid,fsubacctnumber,fsubacctname,FAdjustDebit,FAdjustcredit,FMDebit,FMCredit)   \r\n");
		  sql.append("  (select temp.* from (SELECT 3 ftypeid,pacct.fid ,pacct.fnumber ,pacct.fname_l2,");
		  sql.append("  pacct.fid as fsubacctid, pacct.fnumber as fsubacctnumber,pacct.fname_l2 as fsubacctname,");
		  if (currencyId.equals(CurrencyInfo.GENERAL_LOCAL_CURRENCY_ID.toString()))
		  {
		    sql.append("  sum(case entry.FEntryDC when 1 then entry.FLocalAmount else 0 end) fAdjustDebitBalance , \r\n");
		    sql.append("  sum(case entry.FEntryDC when 0 then entry.FLocalAmount else 0 end) fAdjustCreditBalance  \r\n");
		  }
		  else if (currencyId.equals(CurrencyInfo.GENERAL_REPORT_CURRENCY_ID.toString()))
		  {
		    sql.append("  sum(case entry.FEntryDC when 1 then entry.FReportingAmount else 0 end) fAdjustDebitBalance,  \r\n");
		    sql.append("  sum(case entry.FEntryDC when 0 then entry.FReportingAmount else 0 end) fAdjustCreditBalance  \r\n");
		  }
		  sql.append(",0 as FMDebit,0 as FMCredit");
		  sql.append(" from T_GL_Voucher vch        \r\n");
		  sql.append("   inner join T_GL_VoucherEntry entry on entry.fbillid=vch.fid \r\n");
		  sql.append("   inner join t_bd_period pi on pi.fid=vch.fperiodid    \r\n");
		  sql.append("   inner join t_bd_accountview acct1 on acct1.fid=entry.faccountid    \r\n");
		  
		  sql.append("   inner join t_BD_AccountView pacct  on pacct.Fcompanyid = acct1.Fcompanyid    and pacct.faccounttableid = acct1.faccounttableid and  (charindex(pacct.FLongNumber || '!', acct1.FLongNumber) = 1 or pacct.fid = acct1.fid)        \r\n");
		  sql.append("  WHERE vch.FIsAccountCopy=0 and vch.fcompanyid=? and pi.fperiodyear =? and pi.fperiodnumber=?  ");
		  if (isOnlyLeaf)
		  {
		    sql.append(" and pacct.fisleaf=1 ");
		    sql.append(" and acct1.fisleaf=1 ");
		  }
		  else
		  {
		    sql.append(" and (acct1.flevel <=? or acct1.fisleaf=1)");
		  }
		  if (isIncludePost) {
		    sql.append("       and vch.FBizStatus>0 and vch.FBizStatus !=2    \r\n");
		  } else {
		    sql.append("       and vch.FBizStatus=5     \r\n");
		  }
		  sql.append("   GROUP BY pacct.fid,pacct.fnumber,pacct.fname_l2 ) as temp inner join t_BD_AccountView av on temp.fid = av.fid ");
		  if (isOnlyLeaf)
		  {
		    sql.append(")  \r\n");
		    param = new Object[] { curCompanyId, new Integer(periodYear), new Integer(periodNumber) };
		  }
		  else
		  {
		    sql.append("and av.flevel <=?)  \r\n");
		    param = new Object[] { curCompanyId, new Integer(periodYear), new Integer(periodNumber), new Integer(level), new Integer(level) };
		  }
		  DbUtil.execute(ctx, sql.toString(), param);
		  
		  sql = new StringBuffer();
		  sql.append(" UPDATE ").append(tempTable).append("  SET  FAdjustDebit=").append(" (SELECT sum(isnull(b.FAdjustDebit,0)) FROM ").append(tempTable).append(" b WHERE b.faccountnumber=").append(tempTable).append(".faccountnumber and b.ftypeid=3 ),  \r\n").append(" FAdjustCredit=").append(" (SELECT sum(isnull(b.FAdjustCredit,0)) FROM ").append(tempTable).append(" b WHERE b.faccountnumber=").append(tempTable).append(".faccountnumber and b.ftypeid=3 ),  \r\n").append(" fsendbalance=").append(" (SELECT sum(isnull(c.fsendbalance,0)) FROM ").append(tempTable).append(" c WHERE c.faccountnumber=").append(tempTable).append(".faccountnumber and c.ftypeid=2 )  \r\n").append(" WHERE ftypeid=1   \r\n");
		  DbUtil.execute(ctx, sql.toString());
		  
		  sql = new StringBuffer();
		  sql.append(" UPDATE ").append(tempTable).append("  SET FAdjustDebit=    \r\n").append(" (SELECT b.FAdjustDebit FROM ").append(tempTable).append(" b WHERE b.faccountnumber=").append(tempTable).append(".faccountnumber and b.ftypeid=3 ),  \r\n").append(" FAdjustCredit=     \r\n").append(" (SELECT b.FAdjustCredit FROM ").append(tempTable).append(" b WHERE b.faccountnumber=").append(tempTable).append(".faccountnumber and b.ftypeid=3 )  \r\n").append(" WHERE ftypeid=2 and ").append(tempTable).append(".faccountnumber not in (select faccountnumber from  ").append(tempTable).append("  where ftypeid=1 )");
		  DbUtil.execute(ctx, sql.toString());
		  
		  
		  sql = new StringBuffer();
		  sql.append(" UPDATE ").append(tempTable).append("  SET  FMDebit=").append(" (SELECT sum(isnull(b.FMDebit,0)) FROM ").append(tempTable).append(" b WHERE b.faccountnumber=").append(tempTable).append(".faccountnumber and b.ftypeid=1 ),  \r\n").append(" FMCredit=").append(" (SELECT sum(isnull(b.FMCredit,0)) FROM ").append(tempTable).append(" b WHERE b.faccountnumber=").append(tempTable).append(".faccountnumber and b.ftypeid=1 ) WHERE ftypeid=2   \r\n");
		  DbUtil.execute(ctx, sql.toString());
		  
		  
		  sql = new StringBuffer();
		  sql.append(" UPDATE ").append(tempTable).append("  SET  FMDebit=").append(" (SELECT sum(isnull(b.FMDebit,0)) FROM ").append(tempTable).append(" b WHERE b.faccountnumber=").append(tempTable).append(".faccountnumber and b.ftypeid=1 ),  \r\n").append(" FMCredit=").append(" (SELECT sum(isnull(b.FMCredit,0)) FROM ").append(tempTable).append(" b WHERE b.faccountnumber=").append(tempTable).append(".faccountnumber and b.ftypeid=1 ) WHERE ftypeid=3   \r\n");
		  DbUtil.execute(ctx, sql.toString());
		  
		  
		  sql = new StringBuffer();
		  sql.append(" update  ").append(tempTable).append(" set FSubAcctNumber = FAccountNumber, FSubAcctName=FAccountName, \r\n ").append(" FAdjustDebit=0,fSEndBalance=0 where FAccountNumber is not null and FSubAcctNumber is null ");
		  DbUtil.execute(ctx, sql.toString());
		
		  sql = new StringBuffer();
		  //sql.append("SELECT * FROM ").append(" ( SELECT * FROM ").append(tempTable).append(" WHERE ftypeid=1 UNION ALL     \r\n").append(" SELECT * FROM ").append(tempTable).append(" WHERE ftypeid=2 AND faccountnumber NOT IN (SELECT faccountnumber FROM ").append(tempTable).append(" WHERE ftypeid=1) UNION ALL     \r\n").append(" SELECT * FROM ").append(tempTable).append(" WHERE ftypeid=3 AND faccountnumber NOT IN (SELECT faccountnumber FROM ").append(tempTable).append(" WHERE ftypeid=1)      \r\n").append(" AND faccountnumber NOT IN (SELECT faccountnumber FROM ").append(tempTable).append(" WHERE ftypeid=2)      \r\n").append("  ) a ORDER BY a.FAccountNumber");
		  
		  sql.append(" SELECT * FROM ").append(tempTable).append(" WHERE ftypeid=1 UNION ALL     \r\n").append(" SELECT * FROM ").append(tempTable).append(" WHERE ftypeid=2 AND faccountnumber NOT IN (SELECT faccountnumber FROM ").append(tempTable).append(" WHERE ftypeid=1) UNION ALL     \r\n").append(" SELECT * FROM ").append(tempTable).append(" WHERE ftypeid=3 AND faccountnumber NOT IN (SELECT faccountnumber FROM ").append(tempTable).append(" WHERE ftypeid=1)      \r\n").append(" AND faccountnumber NOT IN (SELECT faccountnumber FROM ").append(tempTable).append(" WHERE ftypeid=2) ");
		  
		return sql.toString();
	}
 
	
	  private String getTempTableStructure()
	  {
	    StringBuffer sb = new StringBuffer();
	    sb.append(" (").append("FTypeID Int,").append("FAccountid VARCHAR(44),").append("FAccountNumber VARCHAR(80),").
	    append("FAccountName VARCHAR(256),").append("FSubAcctid VARCHAR(44),").append("FSubAcctNumber VARCHAR(80),").
	    append("FSubAcctName VARCHAR(256),").append("fMEndBalance DECIMAL(28,10),").append("FAdjustDebit DECIMAL(28,10),").
	    append("FAdjustCredit DECIMAL(28,10),").append("fSEndBalance DECIMAL(28,10),").
	    append("FMDebit DECIMAL(28,10),").append("FMCredit DECIMAL(28,10),").
	    append("FLevel Int,").append("FIsLeaf Int").append(")");
	    return sb.toString();
	  }
	
	  
	  private String getTempTableStructure1()
	  {
	    StringBuffer sb = new StringBuffer();
	    sb.append(" (").append("FTypeID Int,").append("FAccountNumber VARCHAR(80),").append("FAccountName VARCHAR(256),").
	    append("FYearBeginDebit DECIMAL(28,10),").append("FYearBeginCredit DECIMAL(28,10),").
	    append("FPeriodBeginDebit DECIMAL(28,10),").append("FPeriodBeginCredit DECIMAL(28,10),").
 	    append("FDebit DECIMAL(28,10),").append("FCredit DECIMAL(28,10),"). 
	    append("FYearDebit DECIMAL(28,10),").append("FYearCredit DECIMAL(28,10),").  
	    append("FSubDebit DECIMAL(28,10),").append("FSubCredit DECIMAL(28,10),"). 
	    append("FSubYearDebit DECIMAL(28,10),").append("FSubYearCredit DECIMAL(28,10),").  
	    append("FSubPeriodEndDebit DECIMAL(28,10),").append("FSubPeriodEndCredit DECIMAL(28,10),"). 
	    append("FLevel Int,").append("FIsLeaf Int").append(")");
	    return sb.toString();
	  }
	
	  private String getResultTempTableStructure1()
	  {
	    StringBuffer sb = new StringBuffer();
	    sb.append(" (").append("cnumber VARCHAR(80),").append("cname VARCHAR(255),").
	    append("dnumber VARCHAR(80),").append("dname VARCHAR(255),").
	    append("faccountnumber VARCHAR(80),").append("faccountname VARCHAR(256),").  
	    append("FYearBeginDebit DECIMAL(28,10),").append("FYearBeginCredit DECIMAL(28,10),").
	    append("FPeriodBeginDebit DECIMAL(28,10),").append("FPeriodBeginCredit DECIMAL(28,10),").
 	    append("FDebit DECIMAL(28,10),").append("FCredit DECIMAL(28,10),"). 
	    append("FYearDebit DECIMAL(28,10),").append("FYearCredit DECIMAL(28,10),").  
	    append("FSubDebit DECIMAL(28,10),").append("FSubCredit DECIMAL(28,10),"). 
	    append("FSubYearDebit DECIMAL(28,10),").append("FSubYearCredit DECIMAL(28,10),").  
	    append("FSubPeriodEndDebit DECIMAL(28,10),").append("FSubPeriodEndCredit DECIMAL(28,10),"). 
	    append("FLevel Int,").append("FIsLeaf Int").append(")");
	    return sb.toString();
	  }
	  
	  private String getResultTempTableStructure()
	  {
	    StringBuffer sb = new StringBuffer();
	    sb.append(" (").append("cnumber VARCHAR(80),").append("cname VARCHAR(255),").
	    append("dnumber VARCHAR(80),").append("dname VARCHAR(255),").
	    append("faccountnumber VARCHAR(80),").append("faccountname VARCHAR(256),"). 
	    append("fMEndBalance DECIMAL(28,10),").append("FAdjustDebit DECIMAL(28,10),").
	    append("FAdjustCredit DECIMAL(28,10),").append("fSEndBalance DECIMAL(28,10),").
	    append("FMDebit DECIMAL(28,10),").append("FMCredit DECIMAL(28,10),").
	    append("FLevel Int,").append("FIsLeaf Int").append(")");
	    return sb.toString();
	  }
}