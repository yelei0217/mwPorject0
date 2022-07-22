/*      */ package com.kingdee.eas.custom.app;

/*      */ import com.alibaba.fastjson.JSON;
/*      */ import com.kingdee.bos.BOSException;
/*      */ import com.kingdee.bos.Context;
/*      */ import com.kingdee.bos.dao.IObjectPK;
/*      */ import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
/*      */ import com.kingdee.bos.metadata.entity.SelectorItemCollection;
/*      */ import com.kingdee.bos.metadata.entity.SelectorItemInfo;
/*      */ import com.kingdee.bos.util.BOSUuid;
/*      */ import com.kingdee.eas.base.permission.Locale;
/*      */ import com.kingdee.eas.base.permission.LoginAuthorWayEnum;
/*      */ import com.kingdee.eas.base.permission.SecurityInfo;
/*      */ import com.kingdee.eas.base.permission.UserFactory;
/*      */ import com.kingdee.eas.base.permission.UserInfo;
/*      */ import com.kingdee.eas.base.permission.UserType;
/*      */ import com.kingdee.eas.basedata.assistant.CurrencyFactory;
/*      */ import com.kingdee.eas.basedata.assistant.CurrencyInfo;
/*      */ import com.kingdee.eas.basedata.master.cssp.CSSPGroupFactory;
/*      */ import com.kingdee.eas.basedata.master.cssp.CSSPGroupInfo;
/*      */ import com.kingdee.eas.basedata.master.cssp.CSSPGroupStandardFactory;
/*      */ import com.kingdee.eas.basedata.master.cssp.CSSPGroupStandardInfo;
/*      */ import com.kingdee.eas.basedata.master.cssp.CustomerCompanyBankFactory;
/*      */ import com.kingdee.eas.basedata.master.cssp.CustomerCompanyBankInfo;
/*      */ import com.kingdee.eas.basedata.master.cssp.CustomerCompanyInfoFactory;
/*      */ import com.kingdee.eas.basedata.master.cssp.CustomerCompanyInfoInfo;
/*      */ import com.kingdee.eas.basedata.master.cssp.CustomerFactory;
/*      */ import com.kingdee.eas.basedata.master.cssp.CustomerGroupDetailInfo;
/*      */ import com.kingdee.eas.basedata.master.cssp.CustomerInfo;
/*      */ import com.kingdee.eas.basedata.master.cssp.CustomerSaleInfoFactory;
/*      */ import com.kingdee.eas.basedata.master.cssp.CustomerSaleInfoInfo;
/*      */ import com.kingdee.eas.basedata.master.cssp.SupplierCompanyInfoInfo;
/*      */ import com.kingdee.eas.basedata.master.cssp.SupplierFactory;
/*      */ import com.kingdee.eas.basedata.master.cssp.SupplierGroupDetailInfo;
/*      */ import com.kingdee.eas.basedata.master.cssp.SupplierInfo;
/*      */ import com.kingdee.eas.basedata.master.cssp.UsedStatusEnum;
/*      */ import com.kingdee.eas.basedata.org.AdminOrgUnitCollection;
/*      */ import com.kingdee.eas.basedata.org.AdminOrgUnitFactory;
/*      */ import com.kingdee.eas.basedata.org.AdminOrgUnitInfo;
/*      */ import com.kingdee.eas.basedata.org.BizParentAndDelegateInfo;
/*      */ import com.kingdee.eas.basedata.org.CUBDControlCollection;
/*      */ import com.kingdee.eas.basedata.org.CompanyOrgUnitFactory;
/*      */ import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
/*      */ import com.kingdee.eas.basedata.org.CostCenterOrgUnitFactory;
/*      */ import com.kingdee.eas.basedata.org.CostCenterOrgUnitInfo;
/*      */ import com.kingdee.eas.basedata.org.CtrlUnitInfo;
/*      */ import com.kingdee.eas.basedata.org.FullOrgUnitFactory;
/*      */ import com.kingdee.eas.basedata.org.FullOrgUnitInfo;
/*      */ import com.kingdee.eas.basedata.org.OUPartAdminInfo;
/*      */ import com.kingdee.eas.basedata.org.OUPartCostCenterInfo;
/*      */ import com.kingdee.eas.basedata.org.OrgType;
/*      */ import com.kingdee.eas.basedata.org.OrgUnitInfo;
/*      */ import com.kingdee.eas.basedata.org.OrgUnitLayerTypeFactory;
/*      */ import com.kingdee.eas.basedata.org.OrgUnitLayerTypeInfo;
/*      */ import com.kingdee.eas.basedata.org.OrgUnitRelationInfo;
/*      */ import com.kingdee.eas.basedata.org.SaleOrgUnitFactory;
/*      */ import com.kingdee.eas.basedata.org.SaleOrgUnitInfo;
/*      */ import com.kingdee.eas.basedata.org.helper.OrgTypeHelper;
/*      */ import com.kingdee.eas.basedata.org.service.CUBDControlUtils;
/*      */ import com.kingdee.eas.basedata.person.Genders;
/*      */ import com.kingdee.eas.basedata.person.PersonFactory;
/*      */ import com.kingdee.eas.basedata.person.PersonInfo;
/*      */ import com.kingdee.eas.basedata.person.PersonStates;
/*      */ import com.kingdee.eas.common.EASBizException;
/*      */ import com.kingdee.eas.common.client.SysContext;
/*      */ import com.kingdee.eas.custom.BusMasterDataClassFactory;
/*      */ import com.kingdee.eas.custom.BusMasterDataClassInfo;
/*      */ import com.kingdee.eas.custom.DateBaseLogFactory;
/*      */ import com.kingdee.eas.custom.DateBaseLogInfo;
/*      */ import com.kingdee.eas.custom.EAISynTemplate;
/*      */ import com.kingdee.eas.custom.app.unit.AppUnit;
/*      */ import com.kingdee.eas.custom.app.unit.SupplyInfoChange;
/*      */ import com.kingdee.eas.custom.util.CoreUtil;
/*      */ import com.kingdee.eas.custom.util.VerifyUtil;
/*      */ import com.kingdee.eas.framework.CoreBaseInfo;
/*      */ import com.kingdee.eas.framework.DeletedStatusEnum;
/*      */ import com.kingdee.eas.framework.EffectedStatusEnum;
/*      */ import com.kingdee.eas.hr.base.EmployeeTypeInfo;
/*      */ import com.kingdee.eas.hr.emp.PersonPositionFactory;
/*      */ import com.kingdee.eas.hr.emp.PersonPositionInfo;
/*      */ import com.kingdee.eas.util.app.ContextUtil;
/*      */ import com.kingdee.eas.util.app.DbUtil;
/*      */ import com.kingdee.jdbc.rowset.IRowSet;
/*      */ import com.kingdee.jdbc.rowset.IRowSetMetaData;
/*      */ import java.math.BigDecimal;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.rmi.RemoteException;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Time;
/*      */ import java.sql.Timestamp;
/*      */ import java.text.ParseException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import javax.xml.rpc.Service;
/*      */ import localhost.services.HrmService.HrmServiceHttpBindingStub;
/*      */ import localhost.services.HrmService.HrmServiceLocator;
/*      */ import org.apache.axis.AxisFault;
/*      */ import org.apache.commons.httpclient.Cookie;
/*      */ import org.apache.commons.httpclient.HttpClient;
/*      */ import org.apache.commons.httpclient.HttpMethod;
/*      */ import org.apache.commons.httpclient.NameValuePair;
/*      */ import org.apache.commons.httpclient.methods.PostMethod;
/*      */ import org.apache.commons.httpclient.methods.RequestEntity;
/*      */ import org.apache.commons.httpclient.methods.StringRequestEntity;
/*      */ import org.apache.log4j.Logger;
import weaver.hrm.webservice.UserBean;

/*      */ public class DateBaseInterfaceFacadeControllerBean
/*      */   extends AbstractDateBaseInterfaceFacadeControllerBean
/*      */ {
/*  135 */   private static Logger logger = Logger.getLogger("com.kingdee.eas.custom.app.DateBaseInterfaceFacadeControllerBean");
/*      */  
/*      */   public void getSupplierFormOA(Context ctx, String database) throws BOSException {
/*  143 */     Context oldCTX = CoreUtil.copyContext(ctx);
/*  144 */     Context tempCTX = new Context();
/*      */     try {
/*  146 */       tempCTX = CoreUtil.context(oldCTX, "user");
/*  147 */     } catch (EASBizException e1) {
/*  148 */       e1.printStackTrace();
/*      */     } 
/*      */ 
/*      */     
/*  152 */     List<Map<String, String>> quanJT = new ArrayList<Map<String, String>>();
/*      */     
/*  154 */     List<Map<String, String>> feiJT = new ArrayList<Map<String, String>>();
/*      */     
/*  156 */     String sql = null;
/*  157 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*  158 */     sql = "/*dialect*/select FID,FNUMBER,FNAME,FStatus,isnull(FOpenBank,'') as FOpenBank,isnull(FBankAccount,'') as FBankAccount,FCLASSNAME,FISGROUP,FORGNUMBER from EAS_Supplier_MIDTABLE  where  FSIGN =0 and FORGNUMBER is not null ";
/*  159 */     Calendar cal = Calendar.getInstance();
/*  160 */     String version = String.valueOf(cal.getTimeInMillis());
/*  161 */     List<Map<String, Object>> list = EAISynTemplate.query(ctx, database, sql.toString());
/*  162 */     System.out.println("--------------------------" + list.size());
/*  163 */     for (Map<String, Object> map : list) {
/*      */       
/*  165 */       if (map.get("FCLASSNAME").toString().equals("G5")) {
/*  166 */         insertToCustomer(ctx, database, map.get("FNUMBER").toString());
/*      */       }
/*      */       
/*  169 */       DateBaseLogInfo loginfo = getlogInfo(map, DateBasetype.Supplier);
/*  170 */       SupplierInfo info = null;
/*      */       
/*      */       try {
/*  173 */         boolean isAdd = false;
/*  174 */         if (SupplierFactory.getLocalInstance(ctx).exists(
/*  175 */             "where number='" + map.get("FNUMBER") + "'")) {
/*  176 */           info = 
/*  177 */             SupplierFactory.getLocalInstance(ctx)
/*  178 */             .getSupplierInfo(
/*  179 */               "where number='" + map.get("FNUMBER") + "'");
/*  180 */           if (map.get("FSTATUS").toString().equals("1")) {
/*  181 */             loginfo.setProcessType(DateBaseProcessType.DisAble);
/*  182 */             if (info.getUsedStatus().getValue() == 1) {
/*  183 */               SupplierFactory.getLocalInstance(ctx).freezed(
/*  184 */                   (IObjectPK)new ObjectUuidPK(info.getId()), true);
/*      */             }
/*  186 */             loginfo.setStatus(true);
/*  187 */             DateBaseLogFactory.getLocalInstance(ctx).save((CoreBaseInfo)loginfo);
/*      */             
/*  189 */             updateFSign(ctx, database, "EAS_Supplier_MIDTABLE", 1, 
/*  190 */                 map.get("FID").toString());
/*      */             continue;
/*      */           } 
/*  193 */           if (info.getUsedStatus() == UsedStatusEnum.APPROVED) {
/*  194 */             String name = map.get("FNAME").toString();
/*  195 */             DbUtil.execute(ctx, 
/*  196 */                 "update T_BD_Supplier set FUsedStatus = 0 where  fnumber ='" + 
/*  197 */                 info.getNumber() + "' ");
/*      */             
/*  199 */             loginfo.setProcessType(DateBaseProcessType.Update);
/*  200 */             info.setName(name);
/*  201 */             SupplierFactory.getLocalInstance(ctx).update(
/*  202 */                 (IObjectPK)new ObjectUuidPK(info.getId()), (CoreBaseInfo)info);
/*      */             
/*  204 */             DbUtil.execute(ctx, 
/*  205 */                 "update T_BD_Supplier set FUsedStatus = 1 where  fnumber ='" + 
/*  206 */                 info.getNumber() + "' ");
/*  207 */             loginfo.setStatus(true);
/*  208 */             DateBaseLogFactory.getLocalInstance(ctx).save(
/*  209 */                 (CoreBaseInfo)loginfo);
/*      */             
/*  211 */             updateFSign(ctx, database, "EAS_Supplier_MIDTABLE", 
/*  212 */                 1, map.get("FID").toString()); continue;
/*      */           } 
/*  214 */           if (info.getUsedStatus() == UsedStatusEnum.UNAPPROVE) {
/*  215 */             String name = map.get("FNAME").toString();
/*  216 */             loginfo.setProcessType(DateBaseProcessType.Update);
/*  217 */             info.setName(name);
/*  218 */             SupplierFactory.getLocalInstance(ctx).update(
/*  219 */                 (IObjectPK)new ObjectUuidPK(info.getId()), (CoreBaseInfo)info);
/*  220 */             loginfo.setStatus(true);
/*  221 */             DateBaseLogFactory.getLocalInstance(ctx).save(
/*  222 */                 (CoreBaseInfo)loginfo);
/*      */             
/*  224 */             updateFSign(ctx, database, "EAS_Supplier_MIDTABLE", 
/*  225 */                 1, map.get("FID").toString());
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } else {
/*  230 */           loginfo.setProcessType(DateBaseProcessType.AddNew);
/*  231 */           isAdd = true;
/*  232 */           info = new SupplierInfo();
/*      */         } 
/*  234 */         info.setNumber(map.get("FNUMBER").toString());
/*  235 */         info.setName(map.get("FNAME").toString());
/*      */         
/*  237 */         UsedStatusEnum item = null;
/*  238 */         if (map.get("FSTATUS").toString().equals("1")) {
/*  239 */           item = UsedStatusEnum.FREEZED;
/*      */         } else {
/*  241 */           item = UsedStatusEnum.APPROVED;
/*      */         } 
/*  243 */         info.setUsedStatus(item);
/*  244 */         SupplierCompanyInfoInfo company = new SupplierCompanyInfoInfo();
/*  245 */         CompanyOrgUnitInfo companyInfo = new CompanyOrgUnitInfo();
/*  246 */         if (isAdd) {
/*  247 */           SupplierGroupDetailInfo detailinfo = new SupplierGroupDetailInfo();
/*  248 */           CSSPGroupStandardInfo csinfo = 
/*  249 */             CSSPGroupStandardFactory.getLocalInstance(ctx).getCSSPGroupStandardInfo(
/*  250 */               "where number='supplierGroupStandard'");
/*  251 */           detailinfo.setSupplierGroupStandard(csinfo);
/*  252 */           CSSPGroupInfo cssinfo = CSSPGroupFactory.getLocalInstance(
/*  253 */               ctx).getCSSPGroupInfo(
/*  254 */               "where number ='" + 
/*  255 */               map.get("FCLASSNAME").toString() + 
/*  256 */               "' and groupStandard.id='" + 
/*  257 */               csinfo.getId().toString() + "'");
/*  258 */           detailinfo.setSupplierGroup(cssinfo);
/*  259 */           info.getSupplierGroupDetails().add(detailinfo);
/*  260 */           companyInfo = CompanyOrgUnitFactory.getLocalInstance(ctx)
/*  261 */             .getCompanyOrgUnitInfo(
/*  262 */               "where id='" + 
/*  263 */               map.get("FORGNUMBER").toString() + 
/*  264 */               "'");
/*  265 */           company.setCompanyOrgUnit(companyInfo);
/*  266 */           company.setIsFreezePayment(false);
/*  267 */           CurrencyInfo currency = CurrencyFactory.getLocalInstance(
/*  268 */               ctx).getCurrencyCollection("where number='BB01'")
/*  269 */             .get(0);
/*  270 */           company.setSettlementCurrency(currency);
/*      */           
/*  272 */           info.setIsInternalCompany(false);
/*  273 */           info.setInternalCompany(companyInfo);
/*  274 */           info.setBrowseGroup(cssinfo);
/*  275 */           System.out
/*  276 */             .println("_______________________________________________________锟斤拷应锟斤拷锟斤拷锟斤拷" + 
/*  277 */               info.getNumber());
/*  278 */           CtrlUnitInfo ctrlorg = new CtrlUnitInfo();
/*  279 */           info.put("isImport", Boolean.valueOf(true));
/*  280 */           String cuid = "";
/*  281 */           if (map.get("FISGROUP").toString().equals("1")) {
/*  282 */             cuid = "00000000-0000-0000-0000-000000000000CCE7AED4";
/*      */           } else {
/*  284 */             cuid = companyInfo.getCU().getId().toString();
/*      */           } 
/*  286 */           ctrlorg.setId(BOSUuid.read(cuid));
/*      */           
/*  288 */           tempCTX.put(OrgType.Company, companyInfo);
/*  289 */           tempCTX.put("CompanyInfo", companyInfo);
/*  290 */           tempCTX.put("CurCompanyId", map.get("FORGNUMBER").toString());
/*  291 */           tempCTX.put(OrgType.ControlUnit, ctrlorg);
/*      */           
/*  293 */           info.setCU(ctrlorg);
/*  294 */           info.setEffectedStatus(EffectedStatusEnum.EFFECTED);
/*      */         } 
/*  296 */         if (map.get("FISGROUP").toString().equals("1")) {
/*  297 */           SupplierFactory.getLocalInstance(ctx).save((CoreBaseInfo)info);
/*      */         } else {
/*  299 */           SupplierFactory.getLocalInstance(tempCTX).save((CoreBaseInfo)info);
/*      */         } 
/*      */         
/*  302 */         if (isAdd) {
/*  303 */           if (map.get("FISGROUP").toString().equals("1")) {
/*  304 */             Map<String, String> jt = new HashMap<String, String>();
/*  305 */             jt.put("sid", info.getId().toString());
/*  306 */             jt.put("cid", 
/*  307 */                 "00000000-0000-0000-0000-000000000000CCE7AED4");
/*  308 */             jt.put("bank", map.get("FOPENBANK").toString());
/*  309 */             jt.put("bankAccount", map.get("FBANKACCOUNT").toString());
/*  310 */             quanJT.add(jt);
/*  311 */           } else if (map.get("FISGROUP").toString().equals("0")) {
/*  312 */             Map<String, String> jt = new HashMap<String, String>();
/*  313 */             jt.put("sid", info.getId().toString());
/*  314 */             jt.put("cid", companyInfo.getCU().getId().toString());
/*  315 */             jt.put("bank", map.get("FOPENBANK").toString());
/*  316 */             jt.put("bankAccount", map.get("FBANKACCOUNT").toString());
/*  317 */             feiJT.add(jt);
/*      */           } 
/*      */         }   
/*  378 */         updateFSign(ctx, database, "EAS_Supplier_MIDTABLE", 1, 
/*  379 */             map.get("FID").toString());
/*      */         
/*  381 */         loginfo.setStatus(true);
/*  382 */         DateBaseLogFactory.getLocalInstance(ctx).save((CoreBaseInfo)loginfo);
/*  383 */       } catch (EASBizException e) {
/*      */ 
/*      */         
/*  386 */         updateFSign(ctx, database, "EAS_Supplier_MIDTABLE", 2, 
/*  387 */             map.get("FID").toString());
/*  388 */         e.printStackTrace();
/*      */       } 
/*      */     } 
/*  391 */     AppUnit.AssginSupplier(ctx, quanJT, feiJT);
/*      */   } 
/*      */   public void insertToCustomer(Context ctx, String database, String fnumber) throws BOSException {
/*  404 */     String sql = null;
/*  405 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*  406 */     sql = "select FID,FNUMBER,FNAME,FStatus,FOpenBank ,FBankAccount,FISGROUP,FORGNUMBER,FCLASSNAME  from EAS_Supplier_MIDTABLE where fnumber = '" + 
/*  407 */       fnumber + "' and  FSIGN =0";
/*  408 */     List<Map<String, Object>> list = EAISynTemplate.query(ctx, database, 
/*  409 */         sql.toString());
/*      */     
/*  411 */     doCustomer(ctx, database, list);
/*      */   }   
/*      */   public void getCustomerFormOA(Context ctx, String database) throws BOSException {
/*  420 */     String sql = null;
/*  421 */     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
/*  422 */     sql = "select FID,FNUMBER,FNAME,FStatus,FOpenBank ,FBankAccount,FISGROUP,FORGNUMBER,FCLASSNAME  from EAS_Supplier_MIDTABLE where FClassName = '01' and  FSIGN =0";
/*  423 */     List<Map<String, Object>> list = EAISynTemplate.query(ctx, database, 
/*  424 */         sql.toString());
/*  425 */     doCustomer(ctx, database, list);
/*  426 */     System.out.println("--------------------------" + list.size());
/*      */   }
/*      */   public void doCustomer(Context ctx, String database, List<Map<String, Object>> list) throws BOSException {
/*  440 */     for (Map<String, Object> map : list) {
/*      */       
/*  442 */       DateBaseLogInfo loginfo = getlogInfo(map, DateBasetype.Customer);
/*  443 */       CustomerInfo info = null;
/*      */       try {
/*  445 */         boolean isAdd = false;
/*  446 */         if (CustomerFactory.getLocalInstance(ctx).exists(
/*  447 */             "where number='" + map.get("FNUMBER") + "'")) {
/*  448 */           info = 
/*  449 */             CustomerFactory.getLocalInstance(ctx)
/*  450 */             .getCustomerInfo(
/*  451 */               "where number='" + map.get("FNUMBER") + "'");
/*  452 */           if (map.get("FSTATUS").toString().equals("1")) {
/*  453 */             loginfo.setProcessType(DateBaseProcessType.DisAble);
/*  454 */             if (info.getUsedStatus().getValue() == 1) {
/*  455 */               CustomerFactory.getLocalInstance(ctx).freezed(
/*  456 */                   (IObjectPK)new ObjectUuidPK(info.getId()), true);
/*      */             }
/*  458 */             loginfo.setStatus(true);
/*  459 */             DateBaseLogFactory.getLocalInstance(ctx).save((CoreBaseInfo)loginfo);
/*      */             
/*  461 */             updateFSign(ctx, database, "EAS_Supplier_MIDTABLE", 1, 
/*  462 */                 map.get("FID").toString());
/*      */             continue;
/*      */           } 
/*  465 */           if (info.getUsedStatus() == UsedStatusEnum.APPROVED) {
/*  466 */             loginfo.setProcessType(DateBaseProcessType.Update);
/*  467 */             DbUtil.execute(ctx, 
/*  468 */                 "update t_bd_customer set FUsedStatus = 0 where  fnumber ='" + 
/*  469 */                 info.getNumber() + "' ");
/*      */             
/*  471 */             info.setName(map.get("FNAME").toString());
/*  472 */             CustomerFactory.getLocalInstance(ctx).update(
/*  473 */                 (IObjectPK)new ObjectUuidPK(info.getId()), (CoreBaseInfo)info);
/*      */             
/*  475 */             DbUtil.execute(ctx, 
/*  476 */                 "update t_bd_customer set FUsedStatus = 1 where  fnumber ='" + 
/*  477 */                 info.getNumber() + "' ");
/*      */             
/*  479 */             loginfo.setStatus(true);
/*  480 */             DateBaseLogFactory.getLocalInstance(ctx).save(
/*  481 */                 (CoreBaseInfo)loginfo);
/*      */             
/*  483 */             updateFSign(ctx, database, "EAS_Supplier_MIDTABLE", 
/*  484 */                 1, map.get("FID").toString()); continue;
/*      */           } 
/*  486 */           if (info.getUsedStatus() == UsedStatusEnum.UNAPPROVE) {
/*      */             
/*  488 */             info.setName(map.get("FNAME").toString());
/*  489 */             CustomerFactory.getLocalInstance(ctx).update(
/*  490 */                 (IObjectPK)new ObjectUuidPK(info.getId()), (CoreBaseInfo)info);
/*      */             
/*  492 */             loginfo.setStatus(true);
/*  493 */             DateBaseLogFactory.getLocalInstance(ctx).save(
/*  494 */                 (CoreBaseInfo)loginfo);
/*      */             
/*  496 */             updateFSign(ctx, database, "EAS_Supplier_MIDTABLE", 
/*  497 */                 1, map.get("FID").toString());
/*      */ 
/*      */             
/*      */             continue;
/*      */           } 
/*      */         } else {
/*  503 */           isAdd = true;
/*  504 */           info = new CustomerInfo();
/*  505 */           loginfo.setProcessType(DateBaseProcessType.AddNew);
/*      */         } 
/*  507 */         info.setNumber(map.get("FNUMBER").toString());
/*  508 */         info.setName(map.get("FNAME").toString());
/*      */         
/*  510 */         UsedStatusEnum item = null;
/*  511 */         if (map.get("FSTATUS").toString().equals("1")) {
/*  512 */           item = UsedStatusEnum.FREEZED;
/*      */         } else {
/*  514 */           item = UsedStatusEnum.APPROVED;
/*      */         } 
/*  516 */         info.setUsedStatus(item);
/*      */         
/*  518 */         System.out.println("--------------------客户名称"+ 
/*  519 */             map.get("FNUMBER") + "------------------" + 
/*  520 */             map.get("FNAME"));
/*  521 */         CustomerCompanyInfoInfo company = new CustomerCompanyInfoInfo();
/*  522 */         if (isAdd) {
/*  523 */           CustomerGroupDetailInfo detailinfo = new CustomerGroupDetailInfo();
/*  524 */           CSSPGroupStandardInfo csinfo = 
/*  525 */             CSSPGroupStandardFactory.getLocalInstance(ctx).getCSSPGroupStandardInfo(
/*  526 */               "where number='customerGroupStandard'");
/*  527 */           detailinfo.setCustomerGroupStandard(csinfo);
/*  528 */           CSSPGroupInfo cssinfo = CSSPGroupFactory.getLocalInstance(
/*  529 */               ctx).getCSSPGroupInfo(
/*  530 */               "where number='K2' and groupStandard.id='" + 
/*  531 */               csinfo.getId().toString() + "'");
/*  532 */           detailinfo.setCustomerGroup(cssinfo);
/*  533 */           detailinfo.setCustomer(info);
/*  534 */           info.getCustomerGroupDetails().add(detailinfo);
/*      */ 
/*      */ 
/*      */           
/*  538 */           CompanyOrgUnitInfo companyInfo = 
/*  539 */             CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo(
/*  540 */               "where id='" + 
/*  541 */               map.get("FORGNUMBER").toString() + 
/*  542 */               "'");
/*  543 */           company.setCompanyOrgUnit(companyInfo);
/*  544 */           company.setIsFreezeIssueInvoice(false);
/*  545 */           CurrencyInfo currency = CurrencyFactory.getLocalInstance(
/*  546 */               ctx).getCurrencyCollection("where number='BB01'")
/*  547 */             .get(0);
/*  548 */           company.setSettlementCurrency(currency);
/*  549 */           info.setIsInternalCompany(false);
/*  550 */           info.setBrowseGroup(cssinfo);
/*      */           
/*  552 */           CtrlUnitInfo ctrlorg = null;
/*  553 */           if (map.get("FISGROUP").toString().equals("1")) {
/*  554 */             ctrlorg = new CtrlUnitInfo();
/*  555 */             ctrlorg
/*  556 */               .setId(
/*  557 */                 BOSUuid.read("00000000-0000-0000-0000-000000000000CCE7AED4"));
/*  558 */             info.setAdminCU(ctrlorg);
/*  559 */           } else if (map.get("FISGROUP").toString().equals("0")) {
/*  560 */             info.setAdminCU(companyInfo.getCU());
/*      */           } 
/*      */         } 
/*      */         
/*  564 */         CustomerFactory.getLocalInstance(ctx).save((CoreBaseInfo)info);
/*      */ 
/*      */         
/*  567 */         updateFSign(ctx, database, "EAS_Supplier_MIDTABLE", 1, 
/*  568 */             map.get("FID").toString());
/*      */         
/*  570 */         if (isAdd) {
/*  571 */           System.out.println("--------------------客户销售组织" + 
/*  572 */               company.getCompanyOrgUnit().getName());
/*  573 */           CustomerSaleInfoInfo customerSaleInfoInfo = new CustomerSaleInfoInfo();
/*  574 */           SaleOrgUnitInfo saleOrgUnitInfo = 
/*  575 */             SaleOrgUnitFactory.getLocalInstance(ctx).getSaleOrgUnitInfo(
/*  576 */               (IObjectPK)new ObjectUuidPK(
/*  577 */                 map.get("FORGNUMBER").toString()));
/*  578 */           customerSaleInfoInfo.setSaleOrgUnit(saleOrgUnitInfo);
/*  579 */           customerSaleInfoInfo.setBillingOrgUnit(info);
/*  580 */           customerSaleInfoInfo.setDeliverOrgUnit(info);
/*  581 */           customerSaleInfoInfo.setSettlementOrgUnit(info);
/*  582 */           customerSaleInfoInfo.setName(map.get("FNAME").toString());
/*  583 */           customerSaleInfoInfo.setCustomer(info);
/*  584 */           CustomerSaleInfoFactory.getLocalInstance(ctx).save(
/*  585 */               (CoreBaseInfo)customerSaleInfoInfo);
/*      */           
/*  587 */           System.out.println("--------------------客户财务组织" + 
/*  588 */               company.getCompanyOrgUnit().getName());
/*  589 */           company.setCustomer(info);
/*  590 */           CustomerCompanyInfoFactory.getLocalInstance(ctx).save(
/*  591 */               (CoreBaseInfo)company);
/*      */           
/*  593 */           System.out
/*  594 */             .println("--------------------客户银行--------------------------------------");
/*  595 */           CustomerCompanyBankInfo bank = new CustomerCompanyBankInfo();
/*  596 */           bank.setBank(map.get("FOPENBANK").toString());
/*  597 */           bank.setBankAccount(map.get("FBANKACCOUNT").toString());
/*  598 */           bank.setCustomerCompanyInfo(company);
/*  599 */           CustomerCompanyBankFactory.getLocalInstance(ctx).save((CoreBaseInfo)bank);
/*      */         }          
/*  606 */         loginfo.setStatus(true);
/*  607 */         DateBaseLogFactory.getLocalInstance(ctx).save((CoreBaseInfo)loginfo);
/*  608 */         System.out
/*  609 */           .println("--------------------------------------执行完成");
/*  610 */       } catch (EASBizException e) {
/*      */ 
/*      */         
/*  613 */         updateFSign(ctx, database, "EAS_Supplier_MIDTABLE", 2, 
/*  614 */             map.get("FID").toString());
/*  615 */         e.printStackTrace();
/*      */       } 
/*      */     } 
/*      */   }   
/*      */   public void updateFSign(Context ctx, String database, String tableName, int fSign, String fid) throws BOSException {
/*  633 */     String updateSql = "UPDATE " + tableName + " set fSign = " + fSign + 
/*  634 */       " where FID = '" + fid + "'";
/*  635 */     System.out.print("--------------" + updateSql);
/*  636 */     EAISynTemplate.execute(ctx, database, updateSql);
/*      */   }
/*      */ 
/*      */   
/*      */   public DateBaseLogInfo getlogInfo(Map<String, Object> map) throws BOSException {
/*  641 */     Calendar cal = Calendar.getInstance();
/*  642 */     DateBaseLogInfo loginfo = new DateBaseLogInfo();
/*  643 */     cal.setTime(new Date());
/*  644 */     loginfo.setNumber(String.valueOf(cal.getTimeInMillis()) + "." + map.get("FNUMBER"));
/*  645 */     loginfo.setName(map.get("FNAME").toString());
/*  646 */     loginfo.setSimpleName(map.get("FNUMBER").toString());
/*  647 */     loginfo.setDateBaseType(DateBasetype.Supplier);
/*  648 */     String version = String.valueOf(cal.getTimeInMillis());
/*  649 */     loginfo.setVersion(version);
/*  650 */     loginfo.setUpdateDate(new Date());
/*  651 */     SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/*  652 */     String updatetime = sdf1.format(new Date()).substring(11);
/*  653 */     loginfo.setUpdatetime(Time.valueOf(updatetime));
/*  654 */     return loginfo;
/*      */   }
/*      */ 
/*      */   
/*      */   public DateBaseLogInfo getlogInfo(Map<String, Object> map, DateBasetype dateBasetype) throws BOSException {
/*  659 */     Calendar cal = Calendar.getInstance();
/*  660 */     DateBaseLogInfo loginfo = new DateBaseLogInfo();
/*  661 */     cal.setTime(new Date());
/*  662 */     loginfo.setNumber(String.valueOf(cal.getTimeInMillis()) + "." + map.get("FNUMBER"));
/*  663 */     loginfo.setName(map.get("FNAME").toString());
/*  664 */     loginfo.setSimpleName(map.get("FNUMBER").toString());
/*  665 */     loginfo.setDateBaseType(dateBasetype);
/*  666 */     String version = String.valueOf(cal.getTimeInMillis());
/*  667 */     loginfo.setVersion(version);
/*  668 */     loginfo.setUpdateDate(new Date());
/*  669 */     SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/*  670 */     String updatetime = sdf1.format(new Date()).substring(11);
/*  671 */     loginfo.setUpdatetime(Time.valueOf(updatetime));
/*  672 */     return loginfo;
/*      */   } 
/*      */   public void getPersonFormOA(Context ctx, String database) throws BOSException {
/*  683 */     HrmServiceLocator locator1 = new HrmServiceLocator();
/*      */     try {
/*  685 */       URL url1 = new URL("http://101.200.170.62:9081/services/HrmService");
/*  686 */       HrmServiceHttpBindingStub stub1 = new HrmServiceHttpBindingStub(
/*  687 */           url1, (Service)locator1);
/*  688 */       UserBean[] userBeans = stub1.getHrmUserInfo(
/*  689 */           "123.57.253.212", "", "", "", "", "");
/*  690 */       if (userBeans != null && userBeans.length > 0) {
/*  691 */         Calendar cal = Calendar.getInstance();
/*  692 */         String version = String.valueOf(cal.getTimeInMillis());
/*      */         
/*  694 */         SimpleDateFormat sdf = new SimpleDateFormat(
/*  695 */             "yyyy-MM-dd HH:mm:ss");
/*      */ 
/*      */         
/*  698 */         SecurityInfo securityInfo = new SecurityInfo();
/*  699 */         securityInfo.setId(
/*  700 */             BOSUuid.read("00000000-0000-0000-0000-0000000000013EE2C673"));
/*      */ 
/*      */         
/*  703 */         FullOrgUnitInfo fullorgunitinfo = new FullOrgUnitInfo();
/*  704 */         fullorgunitinfo.setId(BOSUuid.read("xxxx")); byte b; int i;
/*      */         UserBean[] arrayOfUserBean;
/*  706 */         for (i = (arrayOfUserBean = userBeans).length, b = 0; b < i; ) { UserBean u = arrayOfUserBean[b];
/*  707 */           DateBaseLogInfo loginfo = new DateBaseLogInfo();
/*  708 */           cal.setTime(new Date());
/*  709 */           loginfo.setNumber(String.valueOf(String.valueOf(cal.getTimeInMillis())) + 
/*  710 */               u.getWorkcode());
/*  711 */           loginfo.setSimpleName(u.getWorkcode());
/*  712 */           loginfo.setName(u.getLastname());
/*  713 */           loginfo.setVersion(version);
/*  714 */           loginfo.setUpdateDate(new Date());
/*  715 */           String updatetime = sdf.format(new Date()).substring(11);
/*  716 */           loginfo.setUpdatetime(Time.valueOf(updatetime));
/*  717 */           loginfo.setDateBaseType(DateBasetype.Person);
/*      */ 
/*      */           
/*  720 */           PersonInfo person = null;
/*  721 */           UserInfo user = null;
/*  722 */           if (!PersonFactory.getLocalInstance(ctx).exists(
/*  723 */               "where number='" + u.getWorkcode() + "'")) {
/*  724 */             loginfo.setProcessType(DateBaseProcessType.AddNew);
/*  725 */             person = new PersonInfo();
/*  726 */             person.setName(u.getLastname());
/*  727 */             person.setNumber(u.getWorkcode());
/*  728 */             if (u.getBirthday() != null && 
/*  729 */               !"".equals(u.getBirthday())) {
/*  730 */               person.setBirthday(sdf.parse(u.getBirthday()));
/*      */             }
/*  732 */             if (u.getSex() != null && "男".equals(u.getSex())) {
/*  733 */               person.setGender(Genders.Male);
/*      */             } else {
/*  735 */               person.setGender(Genders.Female);
/*      */             }              
/*  742 */             person.setDeletedStatus(DeletedStatusEnum.NORMAL);
/*  743 */             if (u.getMobile() != null && !u.getMobile().equals("")) {
/*  744 */               person.setCell(u.getMobile());
/*      */             }
/*  746 */             person.setEFFDT(new Date());
/*  747 */             EmployeeTypeInfo typeInfo = new EmployeeTypeInfo();
/*  748 */             typeInfo
/*  749 */               .setId(
/*  750 */                 BOSUuid.read("00000000-0000-0000-0000-000000000009A29E85B3"));
/*  751 */             person.setEmployeeType(typeInfo);
/*  752 */             person.setState(PersonStates.Enable);
/*  753 */             person.put("NOPOSITION_PERSON_SAVE", Boolean.TRUE);
/*  754 */             person.put("GKADMIN", 
/*  755 */                 SysContext.getSysContext().getCurrentAdminUnit());
/*  756 */             PersonFactory.getLocalInstance(ctx).save((CoreBaseInfo)person);
/*  757 */             PersonPositionInfo pp = new PersonPositionInfo();
/*  758 */             pp.setPerson(person);
/*  759 */             AdminOrgUnitInfo admin = 
/*  760 */               AdminOrgUnitFactory.getLocalInstance(ctx)
/*  761 */               .getAdminOrgUnitInfo(
/*  762 */                 (IObjectPK)new ObjectUuidPK(person.getCU().getId()));
/*  763 */             pp.setPersonDep(admin);
/*  764 */             PersonPositionFactory.getLocalInstance(ctx).save((CoreBaseInfo)pp);
/*      */ 
/*      */             
/*  767 */             user = new UserInfo();
/*  768 */             user.setNumber(u.getWorkcode());
/*  769 */             user.setName(u.getLastname());
/*  770 */             user.setType(UserType.PERSON);
/*  771 */             user.setIsDelete(false);
/*  772 */             user.setIsLocked(false);
/*  773 */             user.setEffectiveDate(Timestamp.valueOf(updatetime));
/*  774 */             user.setInvalidationDate(
/*  775 */                 Timestamp.valueOf("2199-12-31 00:00:00"));
/*  776 */             user.setPWEffectiveDate(Timestamp.valueOf(updatetime));
/*  777 */             user.setPerson(person);
/*  778 */             user.setSecurity(securityInfo);
/*  779 */             user.setCell(u.getMobile());
/*  780 */             user.setLoginAuthorWay(LoginAuthorWayEnum.NORMAL);
/*  781 */             user.setCU(
/*  782 */                 SysContext.getSysContext().getCurrentCtrlUnit());
/*  783 */             user.setDefOrgUnit(fullorgunitinfo);
/*  784 */             user.setDefLocale(Locale.L2);
/*  785 */             loginfo.setStatus(true);
/*  786 */             DateBaseLogFactory.getLocalInstance(ctx).save((CoreBaseInfo)loginfo);
/*      */           } else {
/*  788 */             loginfo.setProcessType(DateBaseProcessType.Update);
/*  789 */             person = PersonFactory.getLocalInstance(ctx)
/*  790 */               .getPersonCollection(
/*  791 */                 "where number='" + u.getWorkcode() + 
/*  792 */                 "'").get(0);
/*  793 */             if ("5".equals(u.getStatus())) {
/*  794 */               person.setDeletedStatus(DeletedStatusEnum.DELETED);
/*  795 */               PersonFactory.getLocalInstance(ctx).cancel(
/*  796 */                   (IObjectPK)new ObjectUuidPK(person.getId()), (CoreBaseInfo)person);
/*  797 */               user = UserFactory.getLocalInstance(ctx)
/*  798 */                 .getUserInfoByNumber(u.getWorkcode());
/*  799 */               user.setIsDelete(true);
/*  800 */               user.setIsLocked(true);
/*  801 */               UserFactory.getLocalInstance(ctx).cancel(
/*  802 */                   (IObjectPK)new ObjectUuidPK(user.getId()), (CoreBaseInfo)user);
/*      */             } 
/*  804 */             loginfo.setStatus(true);
/*  805 */             DateBaseLogFactory.getLocalInstance(ctx).save((CoreBaseInfo)loginfo);
/*      */           }  b++; }
/*      */       
/*      */       } 
/*  809 */     } catch (AxisFault e1) {
/*      */       
/*  811 */       e1.printStackTrace();
/*  812 */     } catch (MalformedURLException e) {
/*      */       
/*  814 */       e.printStackTrace();
/*  815 */     } catch (RemoteException e) {
/*      */       
/*  817 */       e.printStackTrace();
/*  818 */     } catch (EASBizException e) {
/*      */       
/*  820 */       e.printStackTrace();
/*  821 */     } catch (ParseException e) {
/*      */       
/*  823 */       e.printStackTrace();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void getHROrgFormOA(Context ctx, String database) throws BOSException {
/*  832 */     String sql = null;
/*  833 */     sql = "select FNUMBER,FNAME,FPARENTNUM,canceled,FDEPATEMENTLEVEL from T_OA_EAS_DEPARTMENT";
/*  834 */     List<Map<String, Object>> list = EAISynTemplate.query(ctx, database, 
/*  835 */         sql.toString());
/*  836 */     Calendar cal = Calendar.getInstance();
/*  837 */     String version = String.valueOf(cal.getTimeInMillis());
/*  838 */     for (Map<String, Object> map : list) {
/*      */       try {
/*  840 */         AdminOrgUnitCollection adcoll = 
/*  841 */           AdminOrgUnitFactory.getLocalInstance(ctx).getAdminOrgUnitCollection(
/*  842 */             "where number='" + map.get("FNUMBER") + "'");
/*  843 */         DateBaseLogInfo loginfo = new DateBaseLogInfo();
/*  844 */         cal.setTime(new Date());
/*  845 */         loginfo.setNumber(String.valueOf(String.valueOf(cal.getTimeInMillis())) + "." + 
/*  846 */             map.get("FNUMBER").toString());
/*  847 */         loginfo.setSimpleName(map.get("FNUMBER").toString());
/*  848 */         loginfo.setName(map.get("FNAME").toString());
/*  849 */         loginfo.setVersion(version);
/*  850 */         loginfo.setDateBaseType(DateBasetype.orgUnit);
/*  851 */         loginfo.setUpdateDate(new Date());
/*  852 */         SimpleDateFormat sdf = new SimpleDateFormat(
/*  853 */             "yyyy-MM-dd HH:mm:ss");
/*  854 */         String updatetime = sdf.format(new Date()).substring(11);
/*  855 */         loginfo.setUpdatetime(Time.valueOf(updatetime));
/*  856 */         SelectorItemCollection sic = new SelectorItemCollection();
/*  857 */         sic.add(new SelectorItemInfo("*"));
/*  858 */         sic.add(new SelectorItemInfo("cu.*"));
/*  859 */         FullOrgUnitInfo fullOrg = null;
/*  860 */         boolean isAdd = false;
/*  861 */         if (adcoll.size() > 0) {
/*      */           
/*  863 */           fullOrg = FullOrgUnitFactory.getLocalInstance(ctx)
/*  864 */             .getFullOrgUnitInfo(
/*  865 */               (IObjectPK)new ObjectUuidPK(adcoll.get(0).getId()));
/*  866 */           String cancel = "1";
/*  867 */           if (map.get("CANCELED") != null && 
/*  868 */             !map.get("CANCELED").toString().equals("")) {
/*  869 */             cancel = map.get("CANCELED").toString();
/*      */           } else {
/*  871 */             cancel = "0";
/*      */           } 
/*  873 */           System.out.println("----------------------组织状态" + cancel);
/*  874 */           if (cancel.equals("1")) {
/*  875 */             loginfo.setProcessType(DateBaseProcessType.SealUp);
/*  876 */             fullOrg.setIsOUSealUp(true);
/*  877 */           } else if (cancel.equals("0")) {
/*  878 */             if (fullOrg.isIsOUSealUp()) {
/*  879 */               fullOrg.setIsOUSealUp(false);
/*  880 */               loginfo
/*  881 */                 .setProcessType(DateBaseProcessType.UnSealUp);
/*      */             } else {
/*  883 */               loginfo.setProcessType(DateBaseProcessType.Update);
/*      */             } 
/*      */           } 
/*  886 */           AdminOrgUnitCollection adscoll = 
/*  887 */             AdminOrgUnitFactory.getLocalInstance(ctx).getAdminOrgUnitCollection(
/*  888 */               "where number='" + map.get("FPARENTNUM") + 
/*  889 */               "'");
/*  890 */           if (!fullOrg.getName().equals(map.get("FNAME").toString())) {
/*  891 */             fullOrg.setName(map.get("FNAME").toString());
/*      */           }
/*      */           
/*  894 */           if (adscoll.size() > 0) {
/*  895 */             FullOrgUnitInfo superadmin = new FullOrgUnitInfo();
/*  896 */             superadmin.setId(adscoll.get(0).getId());
/*  897 */             if (!fullOrg.getParent().getId().toString().equals(
/*  898 */                 superadmin.getId().toString())) {
/*  899 */               System.out.println("-----------------旧" + 
/*  900 */                   fullOrg.getId() + "----------新" + 
/*  901 */                   superadmin.getId());
/*  902 */               fullOrg.setParent(superadmin);
/*      */             } 
/*      */           } else {
/*  905 */             loginfo.setStatus(false);
/*  906 */             loginfo.setDescription("上级组织不存在");
/*      */           } 
/*  908 */           fullOrg.put("isCus", new Boolean(true));
/*  909 */           FullOrgUnitFactory.getLocalInstance(ctx).update(
/*  910 */               (IObjectPK)new ObjectUuidPK(fullOrg.getId()), (CoreBaseInfo)fullOrg);
/*  911 */           loginfo.setStatus(true);
/*      */         } else {
/*      */           
/*  914 */           isAdd = true;
/*  915 */           loginfo.setProcessType(DateBaseProcessType.AddNew);
/*  916 */           fullOrg = new FullOrgUnitInfo();
/*      */           
/*  918 */           fullOrg.setNumber(map.get("FNUMBER").toString());
/*  919 */           fullOrg.setName(map.get("FNAME").toString());
/*  920 */           AdminOrgUnitCollection adscoll = 
/*  921 */             AdminOrgUnitFactory.getLocalInstance(ctx).getAdminOrgUnitCollection(
/*  922 */               "where number='" + map.get("FPARENTNUM") + 
/*  923 */               "'");
/*  924 */           System.out
/*  925 */             .println("----------------------------org----add-------1");
/*  926 */           if (adscoll.size() > 0) {
/*  927 */             FullOrgUnitInfo superadmin = new FullOrgUnitInfo();
/*  928 */             superadmin.setId(adscoll.get(0).getId());
/*  929 */             fullOrg.setParent(superadmin);
/*  930 */             CUBDControlCollection cuctrol = 
/*  931 */               CUBDControlUtils.loadDefaultCUBDControl(ctx);
/*  932 */             fullOrg.put("CUBDControl", cuctrol);
/*  933 */             fullOrg.setIsCU(false);
/*  934 */             fullOrg.put("isCus", new Boolean(true));
/*  935 */             IObjectPK pk = FullOrgUnitFactory.getLocalInstance(ctx)
/*  936 */               .submit((CoreBaseInfo)fullOrg);
/*  937 */             fullOrg = FullOrgUnitFactory.getLocalInstance(ctx)
/*  938 */               .getFullOrgUnitInfo(pk);
/*      */           } else {
/*  940 */             loginfo.setStatus(false);
/*  941 */             loginfo.setDescription("上级组织不存在");
/*      */           } 
/*      */         } 
/*      */         
/*  945 */         OrgTypeHelper orgTypeHelper = new OrgTypeHelper();
/*  946 */         fullOrg.setIsAdminOrgUnit(true);
/*  947 */         OUPartAdminInfo admin = fullOrg.getPartAdmin();
/*  948 */         if (admin == null) {
/*  949 */           admin = new OUPartAdminInfo();
/*      */         }
/*  951 */         System.out.println("-----------------------------------新增" + 
/*  952 */             fullOrg.getNumber());
/*  953 */         if (map.get("FDEPATEMENTLEVEL") != null && 
/*  954 */           !map.get("FDEPATEMENTLEVEL").toString().equals("")) {
/*  955 */           System.out.println(("---------------------" + 
/*  956 */               map.get("FDEPATEMENTLEVEL") != null && 
/*  957 */               !map.get("FDEPATEMENTLEVEL").toString().equals(
/*  958 */                 "")));
/*  959 */           OrgUnitLayerTypeInfo ltinfo = 
/*  960 */             OrgUnitLayerTypeFactory.getLocalInstance(ctx)
/*  961 */             .getOrgUnitLayerTypeInfo(
/*  962 */               "where name='" + 
/*  963 */               map.get("FDEPATEMENTLEVEL") + "'");
/*  964 */           admin.setUnitLayerType(ltinfo);
/*  965 */           admin.setEFFDT(new Date());
/*  966 */           admin.setIsIndependence(true);
/*  967 */           orgTypeHelper.addOrgType(OrgType.Admin);
/*  968 */           fullOrg.setPartAdmin(admin);
/*  969 */           BizParentAndDelegateInfo PDValueInfo = new BizParentAndDelegateInfo();
/*  970 */           AdminOrgUnitInfo adminOrgUnitInfo = 
/*  971 */             AdminOrgUnitFactory.getLocalInstance(ctx).getAdminOrgUnitInfo(
/*  972 */               (IObjectPK)new ObjectUuidPK(
/*  973 */                 fullOrg.getParent().getId().toString()), sic);
/*  974 */           PDValueInfo.setAdminParent((OrgUnitInfo)adminOrgUnitInfo);
/*  975 */           fullOrg.put("PDValueInfo", PDValueInfo);
/*  976 */           if (!isAdd)
/*      */           {
/*  978 */             if (fullOrg.isIsCostOrgUnit()) {
/*  979 */               OUPartCostCenterInfo costInfo = fullOrg
/*  980 */                 .getPartCostCenter();
/*  981 */               CostCenterOrgUnitInfo cspInfo = null;
/*  982 */               cspInfo = 
/*  983 */                 CostCenterOrgUnitFactory.getLocalInstance(ctx)
/*  984 */                 .getCostCenterOrgUnitInfo(
/*  985 */                   (IObjectPK)new ObjectUuidPK(
/*  986 */                     fullOrg.getParent().getId()
/*  987 */                     .toString()), sic);
/*  988 */               PDValueInfo.setCostParent((OrgUnitInfo)cspInfo);
/*  989 */               orgTypeHelper.addOrgType(OrgType.CostCenter);
/*  990 */               fullOrg.setPartCostCenter(costInfo);
/*      */             } 
/*      */           }
/*  993 */           fullOrg
/*  994 */             .setOrgTypeStr(orgTypeHelper
/*  995 */               .createOrgTypeStrBySet());
/*  996 */           FullOrgUnitFactory.getLocalInstance(ctx).submit((CoreBaseInfo)fullOrg);
/*      */           
/*  998 */           PDValueInfo = new BizParentAndDelegateInfo();
/*  999 */           OrgUnitRelationInfo orgUnitInfo = new OrgUnitRelationInfo();
/* 1000 */           orgUnitInfo.setFromUnit(fullOrg);
/* 1001 */           orgUnitInfo.put("fromType", OrgType.Admin);
/* 1002 */           orgUnitInfo.put("toType", OrgType.Company);
/* 1003 */           PDValueInfo.getDelegateCollection().add(orgUnitInfo);
/* 1004 */           if (fullOrg.isIsCostOrgUnit()) {
/* 1005 */             orgUnitInfo = new OrgUnitRelationInfo();
/* 1006 */             orgUnitInfo.setFromUnit(fullOrg);
/* 1007 */             orgUnitInfo.setToUnit(fullOrg);
/* 1008 */             orgUnitInfo.put("fromType", OrgType.Admin);
/* 1009 */             orgUnitInfo.put("toType", OrgType.CostCenter);
/* 1010 */             PDValueInfo.getDelegateCollection().add(orgUnitInfo);
/*      */           } 
/* 1012 */           System.out.println("--------------------------------------" + 
/* 1013 */               PDValueInfo.getDelegateCollection().size());
/* 1014 */           fullOrg.put("PDValueInfo", PDValueInfo);
/* 1015 */           FullOrgUnitFactory.getLocalInstance(ctx).submit((CoreBaseInfo)fullOrg);
/* 1016 */           loginfo.setStatus(true);
/*      */         } else {
/* 1018 */           loginfo.setStatus(false);
/* 1019 */           loginfo.setDescription("组织层次类型不能为空");
/*      */         } 
/* 1021 */         DateBaseLogFactory.getLocalInstance(ctx).save((CoreBaseInfo)loginfo);
/* 1022 */       } catch (EASBizException e) {
/*      */         
/* 1024 */         e.printStackTrace();
/*      */       } 
/*      */     } 
/* 1027 */     super.getHROrgFormOA(ctx, database);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void getMainClassFormHis(Context ctx, String dataBase) throws BOSException {
/* 1036 */     String sql = null;
/* 1037 */     sql = "select FNUMBER,FNAME,FUPDATETYPE from eas_mainclass_midtable where FSIGN = 0\t";
/* 1038 */     List<Map<String, Object>> list = EAISynTemplate.query(ctx, dataBase, 
/* 1039 */         sql.toString());
/* 1040 */     System.out.println("--------------------------" + list.size());
/* 1041 */     String name = "";
/* 1042 */     String number = "";
/* 1043 */     DateBaseProcessType processType = null;
/* 1044 */     for (Map<String, Object> map : list) {
/* 1045 */       name = map.get("FNAME").toString();
/* 1046 */       number = map.get("FNUMBER").toString();
/* 1047 */       BusMasterDataClassInfo info = null;
/*      */       try {
/* 1049 */         if (!BusMasterDataClassFactory.getLocalInstance(ctx).exists("where number='" + number + "'")) {           
/* 1057 */           info = new BusMasterDataClassInfo();
/* 1058 */           processType = DateBaseProcessType.AddNew;
/* 1059 */           info.setNumber(number);
/* 1060 */           info.setName(name);
/* 1061 */           System.out.println("--------------------业务主数据分类" + number + "------------------" + name);
/* 1062 */           info.setCU(ContextUtil.getCurrentCtrlUnit(ctx));
/* 1063 */           info.setCreator(ContextUtil.getCurrentUserInfo(ctx));
/* 1064 */           info.setCreateTime(new Timestamp((new Date()).getTime()));
/* 1065 */           BusMasterDataClassFactory.getLocalInstance(ctx).save((CoreBaseInfo)info);
/*      */         } 
/* 1067 */         sql = "update eas_mainclass_midtable set FSIGN =1 , fupdatetime = SYSDATE where fid ='" + map.get("id") + "'";
/* 1068 */         EAISynTemplate.execute(ctx, dataBase, sql.toString());
/* 1069 */         insertLog(ctx, processType, DateBasetype.MainClass, name, number, "业务主数据分类");
/* 1070 */         System.out.println("--------------------------------------执行完成");
/* 1071 */       } catch (EASBizException e) {
/* 1072 */         e.printStackTrace();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   public void getEasPayTypeToMid(Context ctx, String dataBase, String fid) throws BOSException {
/* 1083 */     StringBuffer sql = new StringBuffer();
/* 1084 */     sql
/* 1085 */       .append(
/* 1086 */         "select a.FID as fid,a.FNUMBER as fnumber,a.FNAME_L2 as Fname,")
/* 1087 */       .append(
/* 1088 */         "a.CFCTRLORGUNITID as Forgtid,c.FNUMBER as ForgNumber,c.FNAME_L2 as ForgName,")
/* 1089 */       .append(
/* 1090 */         "b.FLEVEL as FLevel,b.FID as FParentID,a.fcreatorid as Fcreator,")
/* 1091 */       .append(
/* 1092 */         "to_char(a.FCreateTime,'yyyy-mm-dd hh24:mi:ss') as FCreateTime,to_char(a.FLastUpdateTime,'yyyy-mm-dd hh24:mi:ss') as FUpdateTime")
/* 1093 */       .append(
/* 1094 */         " from ct_cus_easPaytype a inner join t_cus_easPaytypeTree b on a.FTREEID=b.FID")
/* 1095 */       .append(
/* 1096 */         " LEFT OUTER JOIN T_ORG_CtrlUnit c on a.CFCTRLORGUNITID=c.FID");
/* 1097 */     if (fid != null && !"".equals(fid)) {
/* 1098 */       sql.append(" where a.fid='");
/* 1099 */       sql.append(fid);
/* 1100 */       sql.append("'");
/*      */     } 
/* 1102 */     IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
/* 1103 */     insertMidTable(ctx, DateBaseProcessType.AddNew, DateBasetype.PayType, 
/* 1104 */         dataBase, "EAS_PAYTYPE_MIDTABLE", rows);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void getEasPayTypeTreeToMid(Context ctx, String dataBase, String fid) throws BOSException {
/* 1110 */     StringBuffer sql = new StringBuffer();
/* 1111 */     sql
/* 1112 */       .append(
/* 1113 */         "select a.FID as fid,a.FNUMBER as fnumber,a.FNAME_L2 as Fname,")
/* 1114 */       .append(
/* 1115 */         "a.FControlunitid as Forgtid,b.FNUMBER as ForgNumber,b.FNAME_L2 as ForgName,")
/* 1116 */       .append(
/* 1117 */         " a.FLEVEL as FLevel,a.FParentID as FParentID,a.fcreatorid as Fcreator, ")
/* 1118 */       .append(
/* 1119 */         "to_char(a.FCreateTime,'yyyy-mm-dd hh24:mi:ss') as FCreateTime,to_char(a.FLastUpdateTime,'yyyy-mm-dd hh24:mi:ss') as FUpdateTime")
/* 1120 */       .append(" from  t_cus_easPaytypeTree a ")
/* 1121 */       .append(
/* 1122 */         " LEFT OUTER JOIN T_ORG_CtrlUnit b on a.FControlunitid=b.FID");
/* 1123 */     if (fid != null && !"".equals(fid)) {
/* 1124 */       sql.append(" where a.fid='");
/* 1125 */       sql.append(fid);
/* 1126 */       sql.append("'");
/*      */     } 
/* 1128 */     IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
/* 1129 */     insertMidTable(ctx, DateBaseProcessType.AddNew, 
/* 1130 */         DateBasetype.PayTypeTree, dataBase, "EAS_PAYTYPETREE_MIDTABLE", 
/* 1131 */         rows);
/*      */   }  
/*      */   public void getFreeItemlToMid(Context ctx, String dataBase, String fid) throws BOSException {
/* 1140 */     StringBuffer sql = new StringBuffer();
/* 1141 */     sql
/* 1142 */       .append(
/* 1143 */         " select FID as fid,FNUMBER as fnumber,FNAME_L2 as Fname,fcreatorid as Fcreator,")
/* 1144 */       .append(
/* 1145 */         "to_char(FCreateTime,'yyyy-mm-dd hh24:mi:ss') as FCreateTime,")
/* 1146 */       .append(
/* 1147 */         "to_char(FLastUpdateTime,'yyyy-mm-dd hh24:mi:ss') as FUpdateTime from ct_cus_freeItem ");
/* 1148 */     if (fid != null && !"".equals(fid)) {
/* 1149 */       sql.append(" where fid='");
/* 1150 */       sql.append(fid);
/* 1151 */       sql.append("'");
/*      */     } 
/* 1153 */     IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
/* 1154 */     insertMidTable(ctx, DateBaseProcessType.AddNew, DateBasetype.FreeItem, 
/* 1155 */         dataBase, "EAS_FreeItem_MIDTABLE", rows);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void getMaterielToMid(Context ctx, String dataBase, String fid) throws BOSException {
/* 1164 */     StringBuffer sql = new StringBuffer();
/* 1165 */     sql.append("  SELECT a.FID as fid,a.FNUMBER as fnumber,a.FNAME_L2 as Fname,c.fnumber as fclassNumber, c.fname_l2 as FTypeName,")
/* 1166 */       .append(" a.FAdminCUID as Forgtid,a.FModel as Fmodel,a.cfxinghao as Fxinghao,b.FNUMBER as ForgNumber,b.FNAME_l2 as ForgName,a.FStatus,")
/* 1167 */       .append(" (case a.FStatus when 2 then 2 else 0 end )as FUpdateType,a.fcreatorid as Fcreator, ")
/* 1168 */       .append(" to_char(a.FCreateTime,'yyyy-mm-dd hh24:mi:ss') as FCreateTime,to_char(a.FLastUpdateTime,'yyyy-mm-dd hh24:mi:ss') as FUpdateTime, ")
/* 1169 */       .append(" 0 as FSIGN,0 as FMAILSIGN FROM T_BD_Material  a ")
/* 1170 */       .append(" INNER JOIN T_ORG_CtrlUnit  b on a.FADMINCUID  =b.FID ")
/* 1171 */       .append(" inner join T_BD_MaterialGroup c on a.fmaterialgroupid = c.fid")
/* 1172 */       .append(" inner join t_Bd_Materialgroupstandard d on c.fgroupstandard = d.fid")
/* 1173 */       .append(" where d.fnumber ='BaseGroupStandard' and c.fnumber not like 'W3%'");
/*      */     
/* 1175 */     if (fid != null && !"".equals(fid)) {
/* 1176 */       sql.append("  a.fid='");
/* 1177 */       sql.append(fid);
/* 1178 */       sql.append("'");
/*      */     } 
/* 1180 */     IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
/* 1181 */     MaterielCom(ctx, dataBase, rows, "LOLKK_ITEMS");
/*      */     
/* 1183 */     getMaterielLIKEToMid(ctx, dataBase, fid);
/*      */   }  
/*      */   public void getMaterielLIKEToMid(Context ctx, String dataBase, String fid) throws BOSException {
/* 1196 */     StringBuffer sql = new StringBuffer();
/* 1197 */     sql.append("  SELECT a.FID as fid,a.FNUMBER as fnumber,a.FNAME_L2 as Fname,c.fnumber as fclassNumber, c.fname_l2 as FTypeName,")
/* 1198 */       .append(" a.FAdminCUID as Forgtid,a.FModel as Fmodel,a.cfxinghao as Fxinghao,b.FNUMBER as ForgNumber,b.FNAME_l2 as ForgName,a.FStatus,")
/* 1199 */       .append(" (case a.FStatus when 2 then 2 else 0 end )as FUpdateType,a.fcreatorid as Fcreator, ")
/* 1200 */       .append(" to_char(a.FCreateTime,'yyyy-mm-dd hh24:mi:ss') as FCreateTime,to_char(a.FLastUpdateTime,'yyyy-mm-dd hh24:mi:ss') as FUpdateTime, ")
/* 1201 */       .append(" 0 as FSIGN FROM T_BD_Material  a ")
/* 1202 */       .append(" INNER JOIN T_ORG_CtrlUnit  b on a.FADMINCUID  =b.FID ")
/* 1203 */       .append(" inner join T_BD_MaterialGroup c on a.fmaterialgroupid = c.fid")
/* 1204 */       .append(" inner join t_Bd_Materialgroupstandard d on c.fgroupstandard = d.fid")
/* 1205 */       .append(" where d.fnumber ='BaseGroupStandard' and c.fnumber  like 'W3%'");
/*      */     
/* 1207 */     if (fid != null && !"".equals(fid)) {
/* 1208 */       sql.append("  a.fid='");
/* 1209 */       sql.append(fid);
/* 1210 */       sql.append("'");
/*      */     } 
/* 1212 */     IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
/*      */     
/* 1214 */     MaterielCom(ctx, dataBase, rows, "eas_materiel_midtable");
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void MaterielCom(Context ctx, String dataBase, IRowSet rows, String table) throws BOSException {
/* 1220 */     String tableName = table;
/* 1221 */     String queryStr = "select FID,FNUMBER,fname,FStatus,FMODEL,FXINGHAO,Fupdatetype from  " + 
/* 1222 */       tableName + " ";
/* 1223 */     List<Map<String, Object>> rets = EAISynTemplate.query(ctx, dataBase, 
/* 1224 */         queryStr);
/* 1225 */     StringBuffer allsql = new StringBuffer("");
/* 1226 */     Map<String, Map<String, Object>> mapAll = new HashMap<String, Map<String, Object>>();
/* 1227 */     for (int i = 0; i < rets.size(); i++) {
/* 1228 */       mapAll.put((String)((Map)rets.get(i)).get("FID"), rets.get(i));
/*      */     }
/* 1230 */     List<String> sqls = new ArrayList<String>();
/*      */     try {
/* 1232 */       while (rows.next()) {
/* 1233 */         String fidTwo = rows.getString("fid");
/* 1234 */         String name = rows.getString("fname");
/* 1235 */         String number = rows.getString("fnumber");
/* 1236 */         String fStatus = rows.getString("FStatus");
/*      */         
/* 1238 */         String fmodel = rows.getString("fmodel");
/* 1239 */         String fxinghao = rows.getString("fxinghao");
/*      */         
/* 1241 */         Map<String, Object> mp = mapAll.get(fidTwo);
/* 1242 */         if (mp != null && mp.size() > 0) {
/* 1243 */           String modelMid = mp.get("FMODEL").toString();
/* 1244 */           String xinghao = mp.get("FXINGHAO").toString();
/* 1245 */           if (modelMid == null) {
/* 1246 */             modelMid = "";
/*      */           }
/* 1248 */           if (xinghao == null) {
/* 1249 */             xinghao = "";
/*      */           }
/* 1251 */           String upStr = "更新的字段为：";
/* 1252 */           boolean upFlag = false;
/* 1253 */           boolean flag = false;
/*      */           
/* 1255 */           if (VerifyUtil.notNull(mp.get("FNAME")) && !mp.get("FNAME").equals(name)) {
/* 1256 */             flag = true;
/*      */           }
/* 1258 */           if (VerifyUtil.notNull(mp.get("FNUMBER")) && !mp.get("FNUMBER").equals(number)) {
/* 1259 */             flag = true;
/*      */           }
/* 1261 */           if (flag) {
/* 1262 */             String sqlUp = updateMidTable(ctx, DateBaseProcessType.Update, DateBasetype.Material, dataBase, number, name, fidTwo, tableName);
/* 1263 */             sqls.add(sqlUp);
/* 1264 */             upFlag = true;
/* 1265 */             upStr = String.valueOf(upStr) + "名称；";
/*      */           } 
/*      */ 
/*      */           
/* 1269 */           if ((VerifyUtil.notNull(fmodel) && !modelMid.equals(fmodel)) || (
/* 1270 */             VerifyUtil.notNull(fxinghao) && !xinghao.equals(fxinghao))) {
/* 1271 */             String sqlUp = updateMidModelAndXinghao(ctx, DateBaseProcessType.Update, DateBasetype.Material, dataBase, number, fmodel, fxinghao, fidTwo, tableName);
/*      */             
/* 1273 */             upFlag = true;
/* 1274 */             upStr = String.valueOf(upStr) + "规格；型号；";
/*      */           } 
/*      */           
/* 1277 */           if (VerifyUtil.notNull(mp.get("FSTATUS")) && 
/* 1278 */             !mp.get("FSTATUS").toString().equals(fStatus)) {
/* 1279 */             String updalSql = "";
/* 1280 */             if (fStatus.equals("0")) {
/* 1281 */               updalSql = updateMidStatus(ctx, 
/* 1282 */                   DateBaseProcessType.SealUp, 
/* 1283 */                   DateBasetype.Material, dataBase, number, 
/* 1284 */                   name, fidTwo, tableName);
/*      */             }
/* 1286 */             if (fStatus.equals("1")) {
/* 1287 */               updalSql = updateMidStatus(ctx, 
/* 1288 */                   DateBaseProcessType.ENABLE, 
/* 1289 */                   DateBasetype.Material, dataBase, number, 
/* 1290 */                   name, fidTwo, tableName);
/*      */             }
/* 1292 */             if (fStatus.equals("2")) {
/* 1293 */               updalSql = updateMidStatus(ctx, 
/* 1294 */                   DateBaseProcessType.DisAble, 
/* 1295 */                   DateBasetype.Material, dataBase, number, 
/* 1296 */                   name, fidTwo, tableName);
/*      */             }
/* 1298 */             sqls.add(updalSql);
/* 1299 */             upFlag = true;
/* 1300 */             upStr = String.valueOf(upStr) + "状态；";
/*      */           } 
/* 1302 */           if (upFlag) {
/* 1303 */             insertLog(ctx, DateBaseProcessType.Update, DateBasetype.Material, name, number, upStr);
/*      */           }
/*      */           continue;
/*      */         } 
/* 1307 */         String newSql = insertOneMidTable(ctx, DateBaseProcessType.AddNew, DateBasetype.Material, 
/* 1308 */             dataBase, tableName, rows);
/* 1309 */         if (newSql == null || newSql.equals("")) {
/* 1310 */           System.out.print("---------------错误i----------------"); continue;
/*      */         } 
/* 1312 */         allsql.append(newSql).append(";");
/* 1313 */         sqls.add(newSql);
/*      */       } 
/*      */ 
/*      */       
/* 1317 */       if (sqls.size() > 0) {
/* 1318 */         doInsertSqls(ctx, dataBase, sqls);
/*      */       }
/* 1320 */     } catch (Exception e) {
/* 1321 */       e.printStackTrace();
/*      */     } 
/*      */   }
/*      */   
/*      */   private void doInsertSqls(Context ctx, String dataBase, List<String> sqls) {
/*      */     try {
/* 1327 */       int size = sqls.size();
/* 1328 */       int qian = (int)Math.ceil((size / 10000));
/* 1329 */       if (size % 10000 > 0) {
/* 1330 */         qian++;
/*      */       }
/* 1332 */       for (int i = 0; i < qian; i++) {
/* 1333 */         List<String> sumSqls = new ArrayList<String>();
/*      */         
/* 1335 */         if (size < (i + 1) * 10000) {
/* 1336 */           sumSqls = sqls.subList(i * 10000, size);
/*      */         } else {
/* 1338 */           sumSqls = sqls.subList(i * 10000, (i + 1) * 10000);
/*      */         } 
/* 1340 */         EAISynTemplate.executeBatch(ctx, dataBase, sumSqls);
/*      */       }
/*      */     
/* 1343 */     } catch (BOSException e) {
/*      */       
/* 1345 */       e.printStackTrace();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private String updateMidModelAndXinghao(Context ctx, DateBaseProcessType processType, DateBasetype baseType, String dataBase, String number, String fmodel, String fxinghao, String id, String tableName) throws BOSException {
/* 1352 */     if (VerifyUtil.notNull(dataBase) && VerifyUtil.notNull(number) && 
/* 1353 */       VerifyUtil.notNull(id)) {
/* 1354 */       StringBuffer updateSql = new StringBuffer("UPDATE " + tableName);
/* 1355 */       if (fmodel == null) {
/* 1356 */         fmodel = "";
/*      */       }
/*      */       
/* 1359 */       if (fxinghao == null) {
/* 1360 */         fxinghao = "";
/*      */       }
/*      */       
/* 1363 */       if (fmodel.contains("'")) {
/* 1364 */         fmodel = fmodel.replace("'", "''");
/*      */       }
/* 1366 */       if (fxinghao.contains("'")) {
/* 1367 */         fxinghao = fxinghao.replace("'", "''");
/*      */       }
/* 1369 */       if (baseType == DateBasetype.Material) {
/* 1370 */         updateSql.append("  set FMODEL ='");
/* 1371 */         updateSql.append(fmodel);
/* 1372 */         updateSql.append("',FXINGHAO ='");
/* 1373 */         updateSql.append(fxinghao);
/* 1374 */         updateSql.append("',fSign =0");
/* 1375 */         updateSql.append(",FupdateType=1");
/*      */         
/* 1377 */         updateSql.append(",FupdateTime =  SYSDATE ");
/*      */       } 
/* 1379 */       updateSql.append(" where fid='").append(id).append("'");
/* 1380 */       EAISynTemplate.execute(ctx, dataBase, updateSql.toString());
/*      */     } 
/*      */ 
/*      */     
/* 1384 */     return "";
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void getOrgToMid(Context ctx, String dataBase, String fid) throws BOSException {
/* 1393 */     StringBuffer sql = new StringBuffer();
/*      */ 
/*      */ 
/*      */     
/* 1397 */     sql
/* 1398 */       .append(
/* 1399 */         "select a.FID ,a.FParentID as FParentID,a.FNumber,a.FName_l2 as FName,a.FLongNumber, b.FOrgCode as Fdm,b.FLayerTypeID,")
/* 1400 */       .append(
/* 1401 */         " a.FIsCompanyOrgUnit,a.FIsAdminOrgUnit,a.FIsCostOrgUnit,a.FIsStart,a.FLevel,a.FIsLeaf,a.FIsOUSealUp,")
/* 1402 */       .append(
/* 1403 */         "c.FIsBizUnit as FObject,(CASE a.FLevel WHEN 3 THEN 1 WHEN 4 THEN 2 ELSE 0 END) as Fcp,")
/* 1404 */       .append(
/* 1405 */         "to_char(a.FCreateTime,'yyyy-mm-dd hh24:mi:ss') as FCreateTime from T_ORG_BaseUnit a")
/* 1406 */       .append(" Left Join T_ORG_Admin b on a.FID = b.FID").append(
/* 1407 */         " Left Join T_ORG_Company c on a.FID = c.FID");
/*      */     
/* 1409 */     if (fid != null && !"".equals(fid)) {
/* 1410 */       sql.append(" where a.fid='");
/* 1411 */       sql.append(fid);
/* 1412 */       sql.append("'");
/*      */     } 
/* 1414 */     IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
/* 1415 */     insertMidTable(ctx, DateBaseProcessType.AddNew, DateBasetype.orgUnit, 
/* 1416 */         dataBase, "EAS_ORG_MIDTABLE", rows);
/*      */   }  
/*      */   private String insertOneMidTable(Context ctx, DateBaseProcessType processType, DateBasetype baseType, String dataBase, String tableName, IRowSet rows) {
/* 1432 */     String insertSql = "";
/*      */     try {
/* 1434 */       IRowSetMetaData rowSetMataData = rows.getRowSetMetaData();
/* 1435 */       int columnsSize = rowSetMataData.getColumnCount();
/* 1436 */       String insertSqlStart = "INSERT INTO " + tableName + "(";
/* 1437 */       String insertSqlValues = "";
/* 1438 */       String name = rows.getString("fname");
/* 1439 */       String number = rows.getString("fnumber");
/* 1440 */       for (int i = 1; i <= columnsSize; i++) {
/* 1441 */         String columnName = rowSetMataData.getColumnName(i);
/* 1442 */         String value = rows.getString(columnName);
/* 1443 */         if (value == null) {
/* 1444 */           value = "";
/*      */         }
/* 1446 */         if (columnName.equals("FNAME") && value.contains("'")) {
/* 1447 */           value = value.replace("'", "''");
/*      */         }
/* 1449 */         if (columnName.equals("FMODEL") && value.contains("'")) {
/* 1450 */           value = value.replace("'", "''");
/*      */         }
/* 1452 */         if (columnName.equals("FXINGHAO") && value.contains("'")) {
/* 1453 */           value = value.replace("'", "''");
/*      */         }
/* 1455 */         if (i == columnsSize) {
/* 1456 */           insertSqlStart = String.valueOf(insertSqlStart) + columnName + " ";
/* 1457 */           if (columnName.toLowerCase().endsWith("time") || columnName.toLowerCase().endsWith("date")) {
/*      */             
/* 1459 */             if (value != null && !value.equals("")) {
/* 1460 */               insertSqlValues = String.valueOf(insertSqlValues) + "to_date('" + value + 
/* 1461 */                 "','yyyy-mm-dd hh24:mi:ss')" + " ";
/*      */             } else {
/* 1463 */               insertSqlValues = String.valueOf(insertSqlValues) + " null ";
/*      */             }
/*      */           
/* 1466 */           } else if (value == null) {
/* 1467 */             insertSqlValues = String.valueOf(insertSqlValues) + "'0' ";
/*      */           } else {
/* 1469 */             insertSqlValues = String.valueOf(insertSqlValues) + "'" + value + "' ";
/*      */           } 
/*      */         } else {
/*      */           
/* 1473 */           insertSqlStart = String.valueOf(insertSqlStart) + columnName + ",";
/* 1474 */           if (columnName.toLowerCase().endsWith("time") || columnName.toLowerCase().endsWith("date")) {
/*      */             
/* 1476 */             if (value != null && !value.equals("")) {
/* 1477 */               insertSqlValues = String.valueOf(insertSqlValues) + "to_date('" + value + 
/* 1478 */                 "','yyyy-mm-dd hh24:mi:ss')" + ",";
/*      */             } else {
/* 1480 */               insertSqlValues = String.valueOf(insertSqlValues) + " null ,";
/*      */             }
/*      */           
/*      */           }
/* 1484 */           else if (value == null) {
/* 1485 */             insertSqlValues = String.valueOf(insertSqlValues) + "'0',";
/*      */           } else {
/* 1487 */             insertSqlValues = String.valueOf(insertSqlValues) + "'" + value + "',";
/*      */           } 
/*      */         } 
/*      */       }   
/* 1495 */       if (baseType.getValue().equals(DateBasetype.Person.getValue())) {
/* 1496 */         insertSqlStart = String.valueOf(insertSqlStart) + " ,FUPDATETYPE ) VALUES(";
/* 1497 */         insertSqlValues = String.valueOf(insertSqlValues) + " ,0 )";
/*      */       } else {
/* 1499 */         insertSqlStart = String.valueOf(insertSqlStart) + " ) VALUES(";
/* 1500 */         insertSqlValues = String.valueOf(insertSqlValues) + " )";
/*      */       } 
/* 1502 */       insertSql = String.valueOf(insertSqlStart) + insertSqlValues;
/* 1503 */       System.out.print("**************sql=" + insertSql);
/*      */       
/* 1505 */       insertLog(ctx, processType, baseType, name, number, "新增");
/* 1506 */       return insertSql;
/*      */     }
/* 1508 */     catch (Exception e) {
/* 1509 */       e.printStackTrace();
/*      */       
/* 1511 */       return insertSql;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void SyncOrg(Context ctx, String dataBase) throws BOSException {
/* 1519 */     StringBuffer sql = new StringBuffer();
/* 1520 */     List<String> sqls = new ArrayList<String>();
/* 1521 */     List<String> updatesqls = new ArrayList<String>();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1526 */     sql.append("/*dialect*/ select a.FID ,b.FParentID as FParentID,a.FNumber,a.FName_l2 as FName,a.FLongNumber, nvl(b.FOrgCode,' ') as Fdm,b.FLayerTypeID,")
/* 1527 */       .append("a.FIsCompanyOrgUnit as FIsCompanyOrgUnit ,a.FIsAdminOrgUnit,a.FIsCostOrgUnit,b.FIsStart,a.FLevel,a.FIsLeaf,b.FIsSealUp  as FIsOUSealUp ,")
/*      */       
/* 1529 */       .append("nvl(c.FIsBizUnit,0) as FObject,nvl(d.fsortcode,0)as Fcp, (CASE WHEN a.FIsOUSealUp = 1 THEN 2  WHEN a.FIsStart=1  THEN 2   ELSE 0 END ) as FupdateType, ")
/* 1530 */       .append("to_char(nvl(a.FCreateTime,sysdate),'yyyy-mm-dd hh24:mi:ss') as FCreateTime  , 0 as fSign_0  , 0 as fSign_1  from T_ORG_BaseUnit a")
/* 1531 */       .append(" Left Join T_ORG_Admin b on a.FID = b.FID")
/* 1532 */       .append(" left join T_Org_LayerType d on d.fid = b.FLayerTypeID")
/* 1533 */       .append(" Left Join T_ORG_Company c on a.FID = c.FID where a.FLevel>0 ");
/*      */ 
/*      */     
/* 1536 */     IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
/*      */     try {
/* 1538 */       String tableName = "EAS_ORG_MIDTABLE";
/* 1539 */       String queryStr = "select FID,FPARENTID,FNUMBER,fname,FIsOUSealUp,FIsCompanyOrgUnit,FObject,FLEVEL,FCP from  " + 
/* 1540 */         tableName + " ";
/* 1541 */       List<Map<String, Object>> rets = EAISynTemplate.query(ctx, 
/* 1542 */           dataBase, queryStr);
/*      */       
/* 1544 */       Map<String, Map<String, Object>> mapAll = new HashMap<String, Map<String, Object>>();
/* 1545 */       for (int i = 0; i < rets.size(); i++) {
/* 1546 */         mapAll.put((String)((Map)rets.get(i)).get("FID"), rets.get(i));
/*      */       }
/*      */       
/* 1549 */       while (rows.next()) {
/* 1550 */         String fid = rows.getString("fid");
/* 1551 */         String name = rows.getString("fname");
/* 1552 */         String number = rows.getString("fnumber");
/* 1553 */         String fIsCompanyOrgUnit = rows.getString("FIsCompanyOrgUnit");
/* 1554 */         int fObject = rows.getInt("FObject");
/* 1555 */         String fIsOUSealUp = rows.getString("FIsOUSealUp");
/* 1556 */         String flevel = rows.getString("FLEVEL");
/* 1557 */         String fparentID = rows.getString("FPARENTID");
/* 1558 */         String fcp = rows.getString("Fcp");
/*      */         
/* 1560 */         Map<String, Object> mp = mapAll.get(fid);
/* 1561 */         if (mp != null && mp.size() > 0) {
/*      */           
/* 1563 */           String upStr = "修改的字段为：";
/* 1564 */           String updateSql = " update EAS_ORG_MIDTABLE set ";
/* 1565 */           boolean flag = false;
/*      */           
/* 1567 */           if (VerifyUtil.notNull(mp.get("FNAME")) && 
/* 1568 */             !mp.get("FNAME").equals(name) && name != null) {
/* 1569 */             updateSql = updateMidTable(DateBasetype.orgUnit, "FNAME", name, updateSql);
/* 1570 */             upStr = String.valueOf(upStr) + "名称；";
/* 1571 */             flag = true;
/*      */           } 
/*      */           
/* 1574 */           if (VerifyUtil.notNull(mp.get("FLEVEL")) && !mp.get("FLEVEL").toString().equals(flevel) && flevel != null) {
/* 1575 */             updateSql = updateMidTable(DateBasetype.orgUnit, "FLEVEL", flevel, updateSql);
/* 1576 */             flag = true;
/* 1577 */             upStr = String.valueOf(upStr) + "等级；";
/*      */           } 
/*      */           
/* 1580 */           if (VerifyUtil.notNull(mp.get("FPARENTID")) && !mp.get("FPARENTID").toString().equals(fparentID) && 
/* 1581 */             !mp.get("FNUMBER").toString().equals("M") && fparentID != null) {
/* 1582 */             updateSql = updateMidTable(DateBasetype.orgUnit, "FPARENTID", fparentID, updateSql);
/* 1583 */             flag = true;
/* 1584 */             upStr = String.valueOf(upStr) + "上级；";
/*      */           } 
/*      */           
/* 1587 */           if (VerifyUtil.notNull(mp.get("FCP")) && !mp.get("FCP").toString().equals(fcp) && fcp != null) {
/* 1588 */             updateSql = updateMidTable(DateBasetype.orgUnit, "FCP", fcp, updateSql);
/* 1589 */             flag = true;
/* 1590 */             upStr = String.valueOf(upStr) + "组织层级；";
/*      */           } 
/*      */           
/* 1593 */           if (VerifyUtil.notNull(mp.get("FISCOMPANYORGUNIT")) && 
/* 1594 */             !mp.get("FISCOMPANYORGUNIT").toString().equals(fIsCompanyOrgUnit) && fIsCompanyOrgUnit != null) {
/* 1595 */             updateSql = updateMidTable(DateBasetype.orgUnit, "FISCOMPANYORGUNIT", fIsCompanyOrgUnit, updateSql);
/* 1596 */             flag = true;
/* 1597 */             upStr = String.valueOf(upStr) + "是否为财务公司；";
/*      */           } 
/*      */           
/* 1600 */           if (VerifyUtil.notNull(mp.get("FOBJECT")) && 
/* 1601 */             !mp.get("FOBJECT").toString().equals((new StringBuilder(String.valueOf(fObject))).toString())) {
/* 1602 */             updateSql = updateMidTable(DateBasetype.orgUnit, "FOBJECT", Integer.valueOf(fObject), updateSql);
/* 1603 */             flag = true;
/* 1604 */             upStr = String.valueOf(upStr) + "是否为财务实体；";
/*      */           } 
/* 1606 */           if (VerifyUtil.notNull(mp.get("FISOUSEALUP")) && !mp.get("FISOUSEALUP").toString().equals(fIsOUSealUp) && fIsOUSealUp != null) {
/*      */             
/* 1608 */             if (fIsOUSealUp.equals("0")) {
/* 1609 */               updateSql = updateMidTable(DateBasetype.orgUnit, "FISOUSEALUP", Integer.valueOf(1), updateSql);
/* 1610 */               flag = true;
/* 1611 */               upStr = String.valueOf(upStr) + "状态；";
/*      */             } 
/* 1613 */             if (fIsOUSealUp.equals("1")) {
/* 1614 */               updateSql = updateMidTable(DateBasetype.orgUnit, "FISOUSEALUP", Integer.valueOf(2), updateSql);
/* 1615 */               flag = true;
/* 1616 */               upStr = String.valueOf(upStr) + "状态；";
/*      */             } 
/*      */           } 
/*      */           
/* 1620 */           if (flag) {
/* 1621 */             updateSql = String.valueOf(updateSql) + "   fSign_0 = 0 , fSign_1 = 0 where fid='" + fid + "' ";
/* 1622 */             insertLog(ctx, DateBaseProcessType.Update, DateBasetype.orgUnit, name, number, upStr);
/* 1623 */             updatesqls.add(updateSql);
/*      */           } 
/*      */           continue;
/*      */         } 
/* 1627 */         String sqlStr = insertOneMidTable(ctx, DateBaseProcessType.AddNew, DateBasetype.orgUnit, dataBase, tableName, rows);
/* 1628 */         sqls.add(sqlStr);
/*      */       }
/*      */     
/*      */     }
/* 1632 */     catch (Exception e) {
/* 1633 */       e.printStackTrace();
/*      */     } 
/* 1635 */     if (sqls.size() > 0) {
/* 1636 */       EAISynTemplate.executeBatch(ctx, dataBase, sqls);
/*      */     }
/* 1638 */     if (updatesqls.size() > 0) {
/* 1639 */       EAISynTemplate.executeBatch(ctx, dataBase, updatesqls);
/*      */     }
/*      */   } 
/*      */   private void updateFLEVEL(Context ctx, DateBaseProcessType update, DateBasetype orgUnit, String dataBase, String flevel, String fid, String name, String number) throws BOSException {
/* 1659 */     if (VerifyUtil.notNull(dataBase) && VerifyUtil.notNull(flevel) && 
/* 1660 */       VerifyUtil.notNull(fid)) {
/* 1661 */       StringBuffer updateSql = new StringBuffer(
/* 1662 */           "UPDATE  EAS_ORG_MIDTABLE ");
/* 1663 */       updateSql
/* 1664 */         .append("  set  FLEVEL = '" + 
/* 1665 */           flevel + 
/* 1666 */           "' ,FUPDATETYPE = 1  , fSign_0 = 0 , fSign_1 = 0 where fid='" + 
/* 1667 */           fid + "'");
/* 1668 */       EAISynTemplate.execute(ctx, dataBase, updateSql.toString());
/*      */     } 
/*      */   }   
/*      */   private void updateFCP(Context ctx, DateBaseProcessType update, DateBasetype orgUnit, String dataBase, String fcp, String fid, String name, String number) throws BOSException {
/* 1677 */     if (VerifyUtil.notNull(dataBase) && VerifyUtil.notNull(fcp) && 
/* 1678 */       VerifyUtil.notNull(fid)) {
/* 1679 */       StringBuffer updateSql = new StringBuffer(
/* 1680 */           "UPDATE  EAS_ORG_MIDTABLE ");
/* 1681 */       updateSql
/* 1682 */         .append("  set  FCP = '" + 
/* 1683 */           fcp + 
/* 1684 */           "' ,FUPDATETYPE = 1  , fSign_0 = 0 , fSign_1 = 0 where fid='" + 
/* 1685 */           fid + "'");
/* 1686 */       EAISynTemplate.execute(ctx, dataBase, updateSql.toString());
/*      */     } 
/*      */   }   
/*      */   private void updateParent(Context ctx, DateBaseProcessType update, DateBasetype orgUnit, String dataBase, String fparentID, String fid, String name, String number) throws BOSException {
/* 1706 */     if (VerifyUtil.notNull(dataBase) && VerifyUtil.notNull(fparentID) && 
/* 1707 */       VerifyUtil.notNull(fid)) {
/* 1708 */       StringBuffer updateSql = new StringBuffer(
/* 1709 */           "UPDATE  EAS_ORG_MIDTABLE ");
/* 1710 */       updateSql
/* 1711 */         .append("  set  FParentID = '" + 
/* 1712 */           fparentID + 
/* 1713 */           "' ,FUPDATETYPE = 1  , fSign_0 = 0 , fSign_1 = 0 where fid='" + 
/* 1714 */           fid + "'");
/* 1715 */       EAISynTemplate.execute(ctx, dataBase, updateSql.toString());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String updateSupplyInfoStatus(Context ctx, DateBaseProcessType processType, DateBasetype baseType, String dataBase, String materialNumber, String cityid, String tableName) throws BOSException {
/* 1722 */     if (VerifyUtil.notNull(dataBase) && VerifyUtil.notNull(materialNumber) && VerifyUtil.notNull(cityid)) {
/* 1723 */       StringBuffer updateSql = new StringBuffer("UPDATE  " + tableName);
/*      */       
/* 1725 */       if (baseType == DateBasetype.SupplyinfoToMid && 
/* 1726 */         processType == DateBaseProcessType.DisAble) {
/* 1727 */         updateSql.append("  set  FSTATUS = 0,  fSign = 0 where FMATERIALNUMBER='" + 
/* 1728 */             materialNumber + "' and CSFID = '" + cityid + "'");
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1733 */       EAISynTemplate.execute(ctx, dataBase, updateSql.toString());
/*      */     } 
/*      */     
/* 1736 */     return "";
/*      */   } 
/*      */   public void SyncSupplyinfoToMid(Context ctx, String dataBase) throws BOSException {
/* 1753 */     SupplyInfoChange syc = new SupplyInfoChange();
/*      */   
/* 1770 */     String sqlCity = " select CITYID, COMID FROM EAS_CITY_COMPANY ";
/*      */     
/* 1772 */     IRowSet rowsCityID = DbUtil.executeQuery(ctx, sqlCity.toString());
/*      */     try {
/* 1774 */       while (rowsCityID.next()) {
/* 1775 */         StringBuffer sql = new StringBuffer();
/* 1776 */         List<String> sqls = new ArrayList<String>();
/* 1777 */         List<String> updateSqls = new ArrayList<String>();
/* 1778 */         String cityID = rowsCityID.getString("CITYID").toString();
/* 1779 */         String comid = rowsCityID.getString("COMID").toString();
/* 1780 */         String companyId = "";
/* 1781 */         String companyId2 = "";
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1786 */         if (comid != null && !comid.equals("")) {
/* 1787 */           companyId = " and chase.fid = '" + comid + "' ";
/* 1788 */           companyId2 = " and purchase.fid = '" + comid + "' ";
/*      */         }          
/* 1814 */         sql.append("/*dialect*/ select  distinct supplyinfo.fid  SFID,(material.fnumber||'_'|| purchase2.FID || '_'||supplier.FNUMBER) as fid , purchase2.fid as csfid, ")
/* 1815 */           .append(" purchase2.FNUMBER as ForgNumber ,purchase2.Fname_l2 as ForgName ,supplier.fname_l2 as FSupplierName,supplier.FNUMBER as supplierid ,supplier.fid as supplierkey , 0 as supplierzhukey ,material.fname_l2 as FMaterialName,")
/* 1816 */           .append(" material.fnumber as FMaterialNumber,materialgroup.fnumber as fclassNumber, materialgroup.fname_l2 as FTypeName,material.FMODEL as format,material.CFXINGHAO as Model_number,material.CFHUOHAO  as Item_number,material.CFPINPAI as brand,")
/* 1817 */           .append(" (CASE  WHEN supplyinfo.FUNEFFECTUALDATE > sysdate THEN 1   ELSE 0 END)  as FStatus ,measure.fname_l2  as unit,currency.fname_l2 as FCurrency,decode(supplyinfo.FPrice,999999,0,supplyinfo.FPrice) as price,to_char(supplyinfo.FEFFECTUALDATE,'yyyy-mm-dd hh24:mi:ss') as FStateDate,")
/* 1818 */           .append(" to_char(supplyinfo.FUNEFFECTUALDATE ,'yyyy-mm-dd hh24:mi:ss') as FEndDate ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign  ")
/* 1819 */           .append(" from ( select  fid,FUNEFFECTUALDATE,FPRICE,FEFFECTUALDATE,FSUPPLIERID,FMATERIALITEMID,FCURRENCYID ,fisuseable,FPURCHASEORGID,FPurMeasureUnitID from  T_sm_supplyinfo where FPURCHASEORGID = '" + comid + "'  and   fisuseable=1 )  supplyinfo   ")
/* 1820 */           .append(" inner join T_org_purchase purchase  on   purchase.fid ='" + comid + "' and purchase.FID = supplyinfo.FPURCHASEORGID")
/* 1821 */           .append(" inner join T_org_purchase purchase2 on purchase2.fid = '" + cityID + "' and purchase2.fid= purchase.FParentID and  purchase2.fiscu = 1 and purchase2.fisbizunit = 0  and purchase2.flevel = 2  ")
/* 1822 */           .append(" inner join T_BD_supplier  supplier  on supplier.fid = supplyinfo.FSUPPLIERID  ")
/* 1823 */           .append(" inner join t_bd_material  material  on material.fid = supplyinfo.FMATERIALITEMID ")
/* 1824 */           .append(" inner join t_bd_measureunit measure on measure.fid  = supplyinfo.FPurMeasureUnitID ")
/* 1825 */           .append(" inner join t_bd_currency  currency  on currency.fid = supplyinfo.FCURRENCYID ")
/* 1826 */           .append(" inner join T_BD_MaterialGroup materialgroup on material.fmaterialgroupid = materialgroup.fid")
/* 1827 */           .append(" right join ( select (mate.fnumber||'_'|| chase2.FID ||'_'|| supp.fnumber||'_'||max(info.FEFFECTUALDATE ) ) as midid  ")
/* 1828 */           .append("  from ( select  FUNEFFECTUALDATE,FPRICE,FEFFECTUALDATE,FSUPPLIERID,FMATERIALITEMID,FCURRENCYID ,fisuseable,FPURCHASEORGID,FPurMeasureUnitID from  T_sm_supplyinfo where FPURCHASEORGID ='" + comid + "'  and fisuseable=1  and  FUNEFFECTUALDATE > sysdate )  info ")
/* 1829 */           .append(" inner join T_org_purchase chase on chase.fid ='" + comid + "' and chase.FID =  info.FPURCHASEORGID ")
/* 1830 */           .append(" inner join T_org_purchase chase2 on  chase2.fid = '" + cityID + "'  and chase2.fid = chase.FParentID and chase2.fiscu = 1 and chase2.fisbizunit = 0  and chase2.flevel = 2  ")
/* 1831 */           .append(" inner join T_BD_supplier supp on  supp.fid = info.FSUPPLIERID  ")
/* 1832 */           .append(" inner join t_bd_material mate on mate.fid = info.FMATERIALITEMID ")
/* 1833 */           .append(" inner join T_BD_MaterialGroup mgroup on  mate.fmaterialgroupid = mgroup.fid and  mgroup.fnumber != 'W8'  and mate.fmaterialgroupid = mgroup.fid ")
/* 1834 */           .append(" group by  mate.fnumber,chase2.fid,supp.fnumber  )  a   ")
/* 1835 */           .append("    on  a.midid =  (material.fnumber||'_'|| purchase2.FID || '_'||supplier.FNUMBER || '_'|| supplyinfo.FEFFECTUALDATE  )   ");
/*      */ 
/*      */         
/* 1838 */         IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
/*      */         try {
/* 1840 */           String tableName = "EAS_ORG_SupplyinfoMid";
/* 1841 */           String queryStr = " select FID,CSFID,FORGNUMBER,FSupplierName,supplierid,FMaterialName,FMaterialNumber,format,Model_number,Item_number, brand,FStatus,price,SFID,UNIT,  to_char(FStateDate,'yyyy-mm-dd hh24:mi:ss') as FStateDate ,to_char(FEndDate ,'yyyy-mm-dd hh24:mi:ss') as FEndDate from  " + 
/* 1842 */             tableName + " where CSFID='" + cityID + "' and supplierzhukey = 0 ";
/*      */ 
/*      */ 
/*      */           
/* 1846 */           List<Map<String, Object>> rets = EAISynTemplate.query(ctx, dataBase, queryStr);
/*      */           
/* 1848 */           Map<String, Map<String, Object>> mapAll = new HashMap<String, Map<String, Object>>();
/* 1849 */           Map<String, Map<String, Object>> newMapAll = new HashMap<String, Map<String, Object>>();
/* 1850 */           for (int i = 0; i < rets.size(); i++) {
/* 1851 */             mapAll.put((String)((Map)rets.get(i)).get("FID"), rets.get(i));
/* 1852 */             newMapAll.put((String)((Map)rets.get(i)).get("FID"), rets.get(i));
/* 1853 */             SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
/* 1854 */             Date nowDate = new Date();
/* 1855 */             String lastStr = ((Map)rets.get(i)).get("FENDDATE").toString();
/* 1856 */             String midstatus = ((Map)rets.get(i)).get("FSTATUS").toString();
/* 1857 */             Date lastDate = df.parse(lastStr);
/* 1858 */             if (lastDate.getTime() < nowDate.getTime() && midstatus.equals("1")) {
/* 1859 */               String updateSal = SupplyInfoChange.updateSuInMidStatus(ctx, DateBaseProcessType.DisAble, DateBasetype.SupplyinfoToMid, dataBase, ((Map)rets.get(i)).get("FMATERIALNUMBER").toString(), ((Map)rets.get(i)).get("CSFID").toString(), ((Map)rets.get(i)).get("FID").toString(), tableName);
/* 1860 */               updateSqls.add(updateSal);
/*      */             } 
/*      */           } 
/*      */ 
/*      */           
/* 1865 */           if (updateSqls != null && updateSqls.size() > 0) {
/* 1866 */             doInsertSqls(ctx, dataBase, updateSqls);
/* 1867 */             updateSqls.clear();
/*      */           } 
/*      */           
/* 1870 */           while (rows.next()) {
/* 1871 */             String fid = rows.getString("fid");
/* 1872 */             String materialNumber = rows.getString("FMaterialNumber");
/* 1873 */             String cityid = rows.getString("CSFID");
/* 1874 */             String supplierNumber = rows.getString("supplierid");
/* 1875 */             BigDecimal price = new BigDecimal(rows.getString("price"));
/* 1876 */             String fStatus = rows.getString("FStatus");
/* 1877 */             String materialName = rows.getString("FMaterialName");
/* 1878 */             String model = rows.getString("format");
/* 1879 */             String xinghao = rows.getString("Model_number");
/* 1880 */             String huohao = rows.getString("Item_number");
/* 1881 */             String pinpai = rows.getString("brand");
/*      */             
/* 1883 */             String supplyinfoid = rows.getString("SFID");
/* 1884 */             String unit = rows.getString("unit");
/* 1885 */             String statetimeStr = rows.getString("FStateDate");
/* 1886 */             String endtimeStr = rows.getString("FEndDate");
/*      */             
/* 1888 */             Map<String, Object> mp = mapAll.get(fid);
/* 1889 */             if (mp != null && mp.size() > 0) {
/* 1890 */               String upStr = "修改的字段为：";
/* 1891 */               boolean upFlag = false;
/* 1892 */               boolean flag = false;
/* 1893 */               String thisupdatesql = "update " + tableName + " set ";
/*      */               
/* 1895 */               if (!VerifyUtil.isNull(mp.get("PRICE")) && price.compareTo(new BigDecimal(mp.get("PRICE").toString())) != 0) {
/*      */ 
/*      */ 
/*      */ 
/*      */                 
/* 1900 */                 String updateSal = SupplyInfoChange.updateSuInMid(ctx, price.toString(), "", "", 0);
/* 1901 */                 thisupdatesql = String.valueOf(thisupdatesql) + updateSal;
/* 1902 */                 upFlag = true;
/* 1903 */                 upStr = String.valueOf(upStr) + "价格；";
/*      */               } 
/*      */               
/* 1906 */               if (mp.get("SFID") == null || !mp.get("SFID").equals(supplyinfoid)) {              
/* 1911 */                 String updateSal = SupplyInfoChange.updateSuInMid(ctx, supplyinfoid.toString(), "", "", 1);
/* 1912 */                 thisupdatesql = String.valueOf(thisupdatesql) + updateSal;
/* 1913 */                 upFlag = true;
/* 1914 */                 upStr = String.valueOf(upStr) + "供货价格id；";
/*      */               } 
/*      */ 
/*      */               
/* 1918 */               if (mp.get("UNIT") == null || !mp.get("UNIT").equals(unit)) {
/* 1919 */                 String updateSal = SupplyInfoChange.updateSuInMid(ctx, unit, "", "", 5);
/* 1920 */                 thisupdatesql = String.valueOf(thisupdatesql) + updateSal;
/* 1921 */                 upFlag = true;
/* 1922 */                 upStr = String.valueOf(upStr) + "计量单位；";
/*      */               } 
/*      */               
/* 1925 */               String midstatus = mp.get("FSTATUS").toString();
/* 1926 */               String lastDmp = mp.get("FENDDATE").toString();
/* 1927 */               String stateDep = mp.get("FSTATEDATE").toString();
/*      */               
/* 1929 */               SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
/* 1930 */               Date easEndDate = df.parse(endtimeStr);
/* 1931 */               Date easStateDate = df.parse(statetimeStr);
/* 1932 */               Date lastDate = df.parse(lastDmp);
/* 1933 */               Date stateDate = df.parse(stateDep);
/*      */               
/* 1935 */               if (!VerifyUtil.isNull(mp.get("FENDDATE"))) {
/*      */ 
/*      */                 
/* 1938 */                 if (easStateDate.getTime() != stateDate.getTime() || easEndDate.getTime() != lastDate.getTime()) {
/* 1939 */                   stateDate = easStateDate;
/* 1940 */                   lastDate = easEndDate;
/*      */ 
/*      */                   
/* 1943 */                   upFlag = true;
/* 1944 */                   String updateSal = SupplyInfoChange.updateSuInMid(ctx, rows.getString("FStatus"), statetimeStr, endtimeStr, 2);
/* 1945 */                   thisupdatesql = String.valueOf(thisupdatesql) + updateSal;
/* 1946 */                   upStr = String.valueOf(upStr) + "生效时间；失效时间；";
/*      */                 } 
/*      */                 
/* 1949 */                 Date nowDate = new Date();
/* 1950 */                 if (lastDate.getTime() < nowDate.getTime()) {
/* 1951 */                   if (mp.get("FSTATUS") != null && midstatus.equals("1"))
/*      */                   {
/*      */                     
/* 1954 */                     String updateSal = SupplyInfoChange.updateSuInMid(ctx, "", "", "", 4);
/* 1955 */                     thisupdatesql = String.valueOf(thisupdatesql) + updateSal;
/*      */                     
/* 1957 */                     upFlag = true;
/* 1958 */                     upStr = String.valueOf(upStr) + "状态为禁用；";
/*      */                   }
/*      */                 
/* 1961 */                 } else if (mp.get("FSTATUS") != null && midstatus.equals("0") && rows.getString("FStatus").equals("1")) {
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/* 1966 */                   String updateSal = SupplyInfoChange.updateSuInMid(ctx, "", "", "", 3);
/* 1967 */                   thisupdatesql = String.valueOf(thisupdatesql) + updateSal;
/* 1968 */                   upFlag = true;
/* 1969 */                   upStr = String.valueOf(upStr) + "状态为启用；";
/* 1970 */                 } else if (mp.get("FSTATUS") != null && midstatus.equals("1") && rows.getString("FStatus").equals("0")) {
/*      */ 
/*      */ 
/*      */ 
/*      */                   
/* 1975 */                   String updateSal = SupplyInfoChange.updateSuInMid(ctx, "", "", "", 4);
/* 1976 */                   thisupdatesql = String.valueOf(thisupdatesql) + updateSal;
/* 1977 */                   upFlag = true;
/* 1978 */                   upStr = String.valueOf(upStr) + "状态为禁用；";
/*      */                 } 
/*      */               } 
/*      */               
/* 1982 */               if (upFlag)
/*      */               {
/*      */                 
/* 1985 */                 thisupdatesql = String.valueOf(thisupdatesql) + " fSign = 0 ,FUPDATETIME =  TO_DATE(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')  where   fid ='" + fid + "'";
/*      */                 
/* 1987 */                 updateSqls.add(thisupdatesql);
/* 1988 */                 insertLog(ctx, DateBaseProcessType.Update, DateBasetype.SupplyinfoToMid, "", fid, upStr);
/*      */ 
/*      */ 
/*      */ 
/*      */               
/*      */               }
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             }
/*      */             else {
/*      */ 
/*      */ 
/*      */ 
/*      */               
/* 2004 */               String sqlStr = insertSupplyinfoOneMidTable(ctx, DateBaseProcessType.AddNew, 
/* 2005 */                   DateBasetype.SupplyinfoToMid, dataBase, "EAS_ORG_SupplyinfoMid", rows);
/*      */               
/* 2007 */               sqls.add(sqlStr);
/*      */             } 
/*      */ 
/*      */             
/* 2011 */             newMapAll.remove(fid);
/*      */           } 
/*      */           
/* 2014 */           if (updateSqls != null && updateSqls.size() > 0) {
/* 2015 */             doInsertSqls(ctx, dataBase, updateSqls);
/* 2016 */             updateSqls.clear();
/*      */           } 
/*      */ 
/*      */           
/* 2020 */           for (Map.Entry<String, Map<String, Object>> entry : newMapAll.entrySet()) {
/* 2021 */             System.out.println("Key = " + (String)entry.getKey() + ", Value = " + entry.getValue());
/* 2022 */             Map<String, Object> value = entry.getValue();
/*      */             
/* 2024 */             SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
/* 2025 */             Date nowDate = new Date();
/* 2026 */             String lastStr = value.get("FENDDATE").toString();
/* 2027 */             String midstatus = value.get("FSTATUS").toString();
/* 2028 */             Date lastDate = df.parse(lastStr);
/* 2029 */             if (lastDate.getTime() > nowDate.getTime() && midstatus.equals("1")) {
/* 2030 */               String updateSql = SupplyInfoChange.updateSuInMidStatus(ctx, DateBaseProcessType.DisAble, DateBasetype.SupplyinfoToMid, dataBase, value.get("FMATERIALNUMBER").toString(), value.get("CSFID").toString(), value.get("FID").toString(), tableName);
/* 2031 */               updateSqls.add(updateSql);
/*      */             } 
/*      */           } 
/*      */           
/* 2035 */           if (updateSqls != null && updateSqls.size() > 0) {
/* 2036 */             doInsertSqls(ctx, dataBase, updateSqls);
/* 2037 */             updateSqls.clear();
/*      */           }
/*      */         
/* 2040 */         } catch (Exception e) {
/* 2041 */           e.printStackTrace();
/*      */         } 
/*      */         
/* 2044 */         if (sqls.size() > 0) {
/* 2045 */           doInsertSqls(ctx, dataBase, sqls);
/* 2046 */           insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.SupplyinfoToMid, "", "", "新增已有供应商的数据" + sqls.size() + "条");
/*      */         
/*      */         }
/*      */       
/*      */       }
/*      */     
/*      */     }
/* 2053 */     catch (SQLException e1) {
/*      */       
/* 2055 */       e1.printStackTrace();
/*      */     } 
/*      */   }
/*      */
/*      */   
/*      */   private String insertSupplyinfoOneMidTable(Context ctx, DateBaseProcessType processType, DateBasetype baseType, String dataBase, String tableName, IRowSet rows) {
/* 2164 */     String insertSql = "";
/*      */     try {
/* 2166 */       IRowSetMetaData rowSetMataData = rows.getRowSetMetaData();
/* 2167 */       int columnsSize = rowSetMataData.getColumnCount();
/* 2168 */       String insertSqlStart = "INSERT INTO " + tableName + "(";
/* 2169 */       String insertSqlValues = "";
/* 2170 */       for (int i = 1; i <= columnsSize; i++) {
/* 2171 */         String columnName = rowSetMataData.getColumnName(i);
/* 2172 */         String value = rows.getString(columnName);
/* 2173 */         if (value == null) {
/* 2174 */           value = "";
/*      */         }
/* 2176 */         if (columnName.equals("FMATERIALNAME") && value.contains("'")) {
/* 2177 */           value = value.replace("'", "''");
/*      */         }
/* 2179 */         if (columnName.equals("FORMAT") && value.contains("'")) {
/* 2180 */           value = value.replace("'", "''");
/*      */         }
/* 2182 */         if (columnName.equals("MODEL_NUMBER") && value.contains("'")) {
/* 2183 */           value = value.replace("'", "''");
/*      */         }
/* 2185 */         if (columnName.equals("BRAND") && value.contains("'")) {
/* 2186 */           value = value.replace("'", "''");
/*      */         }
/* 2188 */         if (columnName.equals("ITEM_NUMBER") && value.contains("'")) {
/* 2189 */           value = value.replace("'", "''");
/*      */         }
/* 2191 */         if (i == columnsSize) {
/* 2192 */           insertSqlStart = String.valueOf(insertSqlStart) + columnName + " ";
/* 2193 */           if (columnName.toLowerCase().endsWith("time") || columnName.toLowerCase().endsWith("date")) {
/*      */             
/* 2195 */             if (value != null && !value.equals("")) {
/* 2196 */               insertSqlValues = String.valueOf(insertSqlValues) + "to_date('" + value + 
/* 2197 */                 "','yyyy-mm-dd hh24:mi:ss')" + " ";
/*      */             } else {
/* 2199 */               insertSqlValues = String.valueOf(insertSqlValues) + " null ";
/*      */             }
/*      */           
/* 2202 */           } else if (value == null) {
/* 2203 */             insertSqlValues = String.valueOf(insertSqlValues) + "'0' ";
/*      */           } else {
/* 2205 */             insertSqlValues = String.valueOf(insertSqlValues) + "'" + value + "' ";
/*      */           } 
/*      */         } else {
/*      */           
/* 2209 */           insertSqlStart = String.valueOf(insertSqlStart) + columnName + ",";
/* 2210 */           if (columnName.toLowerCase().endsWith("time") || columnName.toLowerCase().endsWith("date")) {
/*      */             
/* 2212 */             if (value != null && !value.equals("")) {
/* 2213 */               insertSqlValues = String.valueOf(insertSqlValues) + "to_date('" + value + 
/* 2214 */                 "','yyyy-mm-dd hh24:mi:ss')" + ",";
/*      */             } else {
/* 2216 */               insertSqlValues = String.valueOf(insertSqlValues) + " null ";
/*      */             }
/*      */           
/* 2219 */           } else if (value == null) {
/* 2220 */             insertSqlValues = String.valueOf(insertSqlValues) + "'0',";
/*      */           } else {
/* 2222 */             insertSqlValues = String.valueOf(insertSqlValues) + "'" + value + "',";
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */ 
/*      */       
/* 2229 */       insertSqlStart = String.valueOf(insertSqlStart) + " ) VALUES(";
/* 2230 */       insertSqlValues = String.valueOf(insertSqlValues) + " )";
/* 2231 */       insertSql = String.valueOf(insertSqlStart) + insertSqlValues;
/* 2232 */       System.out.print("**************sql=" + insertSql);
/*      */ 
/*      */       
/* 2235 */       return insertSql;
/*      */     }
/* 2237 */     catch (Exception e) {
/* 2238 */       e.printStackTrace();
/*      */       
/* 2240 */       return insertSql;
/*      */     } 
/*      */   }
/*      */   
/*      */   public String updatePurchase(Context ctx, DateBaseProcessType processType, DateBasetype baseType, String dataBase, String materialNumber, String cityID, String supplierNumber, String id) throws BOSException {
/* 2245 */     StringBuffer sql = new StringBuffer();
/* 2246 */     HashMap<String, String> comMap = getComMap(ctx, dataBase, cityID);
/* 2247 */     String companyId = "";
/* 2248 */     String companyId2 = "";
/* 2249 */     if (comMap.get(cityID) != null && !((String)comMap.get(cityID)).toString().equals("")) {
/* 2250 */       companyId = " and chase.fid = '" + ((String)comMap.get(cityID)).toString() + "' ";
/* 2251 */       companyId2 = " and purchase.fid = '" + ((String)comMap.get(cityID)).toString() + "' ";
/*      */     } 
/* 2253 */     sql.append(" select  distinct (material.fnumber||'_'|| purchase2.FID || '_'||supplier.FNUMBER) as fid , purchase2.fid as csfid, ")
/* 2254 */       .append(" purchase2.FNUMBER as  ForgNumber ,purchase2.Fname_l2 as  FORGNAME  ,supplier.fname_l2 as FSupplierName,supplier.FNUMBER as supplierid ,supplier.fid as supplierkey ,0 as supplierzhukey , material.fname_l2 as FMaterialName,")
/* 2255 */       .append(" material.fnumber as FMaterialNumber,materialgroup.fnumber as fclassNumber, materialgroup.fname_l2 as FTypeName,material.FMODEL as format,material.CFXINGHAO as Model_number,material.CFHUOHAO  as Item_number,material.CFPINPAI as brand,")
/* 2256 */       .append(" supplyinfo.fisuseable  as FStatus,measure.fname_l2  as unit,currency.fname_l2 as FCurrency,decode(supplyinfo.FPrice,999999,0,supplyinfo.FPrice) as price,to_char(supplyinfo.FEFFECTUALDATE,'yyyy-mm-dd hh24:mi:ss') as FStateDate,")
/* 2257 */       .append(" to_char(supplyinfo.FUNEFFECTUALDATE ,'yyyy-mm-dd hh24:mi:ss') as FEndDate ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign  from T_sm_supplyinfo supplyinfo ")
/* 2258 */       .append(" inner join T_org_purchase purchase on purchase.FID =  supplyinfo.FPURCHASEORGID ")
/* 2259 */       .append(" inner join  T_org_purchase  purchase2 on purchase2.fid = purchase.FParentID")
/* 2260 */       .append(" inner join T_BD_supplier supplier on  supplier.fid = supplyinfo.FSUPPLIERID  ")
/* 2261 */       .append(" inner join t_bd_material material on material.fid = supplyinfo.FMATERIALITEMID ")
/* 2262 */       .append(" inner join t_bd_measureunit measure on measure.fid = supplyinfo.FPurMeasureUnitID ")
/* 2263 */       .append(" inner join t_bd_currency currency on currency.fid = supplyinfo.FCURRENCYID ")
/* 2264 */       .append(" inner join T_BD_MaterialGroup materialgroup on material.fmaterialgroupid = materialgroup.fid")
/* 2265 */       .append(" right join ( select (mate.fnumber||'_'|| chase2.FID ||'_'|| supp.fnumber) as  midid , max(info.FEFFECTUALDATE ) as begintime, max(info.FUNEFFECTUALDATE) as endtime  cfrom T_sm_supplyinfo info ")
/* 2266 */       .append(" inner join T_org_purchase chase  on chase.FID =  info.FPURCHASEORGID ")
/* 2267 */       .append(" inner join T_org_purchase chase2 on chase2.fid = chase.FParentID ")
/* 2268 */       .append(" inner join T_BD_supplier supp on  supp.fid = info.FSUPPLIERID  ")
/* 2269 */       .append(" inner join t_bd_material mate on mate.fid = info.FMATERIALITEMID ")
/* 2270 */       .append(" inner join T_BD_MaterialGroup mgroup on mate.fmaterialgroupid = mgroup.fid ")
/* 2271 */       .append(" where mgroup.fnumber !='W8' and chase2.fiscu = 1 and chase2.fisbizunit = 0  and chase2.flevel = 2 ")
/* 2272 */       .append(" and mate.fnumber='").append(materialNumber).append("' and chase2.fid ='").append(cityID).append("' and supp.FNUMBER='").append(supplierNumber)
/* 2273 */       .append("'  " + companyId + " and info.FUNEFFECTUALDATE > sysdate  group by  mate.fnumber,chase2.FID,supp.fnumber ) a on supplyinfo.FUNEFFECTUALDATE = a.endtime  and a.midid = (material.fnumber||'_'|| purchase2.FID || '_'||supplier.FNUMBER ) ")
/* 2274 */       .append("  where supplyinfo.fisuseable=1 and  purchase2.fiscu = 1 and purchase2.fisbizunit = 0  and purchase2.flevel = 2")
/* 2275 */       .append(" and material.fnumber='").append(materialNumber)
/* 2276 */       .append("' " + companyId2 + " and purchase2.fid ='").append(cityID)
/* 2277 */       .append("' and supplier.FNUMBER='").append(supplierNumber).append("' ");
/*      */     
/* 2279 */     IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
/*      */     
/* 2281 */     String sqlStr = insertSupplyinfoOneMidTable(ctx, DateBaseProcessType.AddNew, 
/* 2282 */         DateBasetype.SupplyinfoToMid, dataBase, "EAS_ORG_SupplyinfoMid", rows);
/*      */     
/* 2284 */     return sqlStr;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void updateMaterialOfSupplyinfoMid(Context ctx, DateBaseProcessType processType, DateBasetype baseType, String dataBase, String materialNumber, String materialName, String model, String xinghao, String huohao, String pinpai) throws BOSException {
/* 2290 */     if (VerifyUtil.notNull(dataBase) && VerifyUtil.notNull(materialNumber)) {
/* 2291 */       StringBuffer updateSql = new StringBuffer(" /*dialect*/ UPDATE ");
/* 2292 */       if (baseType == DateBasetype.SupplyinfoToMid) {
/* 2293 */         if (materialName != null && materialName.contains("'")) {
/* 2294 */           materialName = materialName.replace("'", "''");
/*      */         }
/* 2296 */         if (model != null && model.contains("'")) {
/* 2297 */           model = model.replace("'", "''");
/*      */         }
/* 2299 */         if (xinghao != null && xinghao.contains("'")) {
/* 2300 */           xinghao = xinghao.replace("'", "''");
/*      */         }
/* 2302 */         if (huohao != null && huohao.contains("'")) {
/* 2303 */           huohao = huohao.replace("'", "''");
/*      */         }
/* 2305 */         if (pinpai != null && pinpai.contains("'")) {
/* 2306 */           pinpai = pinpai.replace("'", "''");
/*      */         }
/* 2308 */         updateSql.append(" EAS_ORG_SupplyinfoMid set format = '");
/* 2309 */         updateSql.append(model);
/* 2310 */         updateSql.append("' ,Model_number ='");
/* 2311 */         updateSql.append(xinghao);
/* 2312 */         updateSql.append("' ,Item_number ='");
/* 2313 */         updateSql.append(huohao);
/* 2314 */         updateSql.append("' ,brand ='");
/* 2315 */         updateSql.append(pinpai);
/* 2316 */         updateSql.append("' ,fSign =0 , FMaterialName='");
/* 2317 */         updateSql.append(String.valueOf(materialName) + "' ");
/*      */       } 
/*      */       
/* 2320 */       updateSql.append(" where FMaterialNumber='").append(materialNumber).append("'");
/* 2321 */       System.out.println("修改名称规格型号品牌货号SQL：" + updateSql);
/* 2322 */       EAISynTemplate.execute(ctx, dataBase, updateSql.toString());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private String updateMidTable(DateBasetype baseType, String leiType, Object obj, String updateSql) {
/* 2337 */     if (baseType == DateBasetype.position) {
/* 2338 */       if (leiType.equals("FNAME")) {
/* 2339 */         updateSql = String.valueOf(updateSql) + " FName = '" + obj.toString() + "', ";
/* 2340 */       } else if (leiType.equals("FUPDATETYPE")) {
/* 2341 */         updateSql = String.valueOf(updateSql) + "FUPDATETYPE = " + obj.toString() + " , ";
/*      */       }
/*      */     
/* 2344 */     } else if (baseType == DateBasetype.Person) {
/* 2345 */       if (leiType.equals("FNAME")) {
/* 2346 */         updateSql = String.valueOf(updateSql) + " FName = '" + obj.toString() + "', ";
/* 2347 */       } else if (leiType.equals("FNUMBER")) {
/* 2348 */         updateSql = String.valueOf(updateSql) + " FNUMBER = '" + obj.toString() + "' ,FEMPNUMBER = '" + obj.toString() + "', ";
/* 2349 */       } else if (leiType.equals("FORGTID")) {
/* 2350 */         String[] arr = obj.toString().split(";");
/* 2351 */         updateSql = String.valueOf(updateSql) + " FORGTID = '" + arr[0].toString() + "' ,FORGNUMBER = '" + arr[1].toString() + "', FORGNAME = '" + arr[2].toString() + "' , ";
/* 2352 */       } else if (leiType.equals("FDEPTID")) {
/* 2353 */         updateSql = String.valueOf(updateSql) + " FDEPTID = '" + obj.toString() + "' , ";
/* 2354 */       } else if (leiType.equals("BANKNUM")) {
/* 2355 */         updateSql = String.valueOf(updateSql) + " BANKNUM = '" + obj.toString() + "' , ";
/* 2356 */       } else if (leiType.equals("BANK")) {
/* 2357 */         updateSql = String.valueOf(updateSql) + " BANK = '" + obj.toString() + "' , ";
/* 2358 */       } else if (leiType.equals("FPOSITIONNUMBER")) {
/* 2359 */         String[] arr = obj.toString().split(";");
/* 2360 */         updateSql = String.valueOf(updateSql) + " FPositionNumber = '" + arr[0].toString() + "' , FPositionName= '" + arr[1].toString() + "',FPositionID='" + arr[2].toString() + "' , ";
/*      */       }
/* 2362 */       else if (leiType.equals("POSTCODE")) {
/* 2363 */         updateSql = String.valueOf(updateSql) + " POSTCODE = '" + obj.toString() + "' , ";
/* 2364 */       } else if (leiType.equals("FENTERDATE")) {
/* 2365 */         updateSql = String.valueOf(updateSql) + " FENTERDATE = '" + obj.toString() + "' , ";
/* 2366 */       } else if (leiType.equals("FPLANFORMALDATE")) {
/* 2367 */         updateSql = String.valueOf(updateSql) + " FPlanFormalDate = '" + obj.toString() + "' , ";
/* 2368 */       } else if (leiType.equals("FJOBLEVELNUMBER")) {
/* 2369 */         String[] arr = obj.toString().split(";");
/* 2370 */         updateSql = String.valueOf(updateSql) + " FJobLevelNumber = '" + arr[0].toString() + "' , FJobLevelName = '" + arr[1].toString() + "' ,FJobLevelID='" + arr[2].toString() + "' , ";
/*      */       }
/* 2372 */       else if (leiType.equals("FJOBCATEGORYNUMBER")) {
/* 2373 */         String[] arr = obj.toString().split(";");
/* 2374 */         updateSql = String.valueOf(updateSql) + " FJobCategoryNumber = '" + arr[0].toString() + "' , FJobCategoryName = '" + arr[1].toString() + "' ,FJobCategoryID='" + arr[2].toString() + "' , ";
/*      */       }
/* 2376 */       else if (leiType.equals("FUPDATETYPE")) {
/* 2377 */         if (obj.toString().equals("1")) {
/* 2378 */           updateSql = String.valueOf(updateSql) + " FUPDATETYPE = " + obj.toString() + " , FEMPLOYEETYPENAME = '锟斤拷锟斤拷' , FEMPLOYEETYPENUMBER = 0, ";
/* 2379 */         } else if (obj.toString().equals("2")) {
/* 2380 */           updateSql = String.valueOf(updateSql) + " FUPDATETYPE = " + obj.toString() + " , FEMPLOYEETYPENAME = '锟斤拷锟斤拷' , FEMPLOYEETYPENUMBER = 1, ";
/*      */         }
/*      */       
/*      */       } 
/* 2384 */     } else if (baseType == DateBasetype.Supplier2HIS) {
/* 2385 */       if (leiType.equals("FNAME")) {
/* 2386 */         updateSql = String.valueOf(updateSql) + " FName = '" + obj.toString() + "', ";
/* 2387 */       } else if (leiType.equals("FUPDATETYPE")) {
/* 2388 */         updateSql = String.valueOf(updateSql) + "FUPDATETYPE = " + obj.toString() + " , ";
/*      */       }
/*      */     
/* 2391 */     } else if (baseType == DateBasetype.PayType) {
/* 2392 */       if (leiType.equals("FNAME")) {
/* 2393 */         updateSql = String.valueOf(updateSql) + " FName = '" + obj.toString() + "', ";
/* 2394 */       } else if (leiType.equals("FUPDATETYPE")) {
/* 2395 */         if (obj.toString().equals("1")) {
/* 2396 */           updateSql = String.valueOf(updateSql) + " FISSTART = 0 , FUPDATETYPE = 1 , ";
/* 2397 */         } else if (obj.toString().equals("2")) {
/* 2398 */           updateSql = String.valueOf(updateSql) + " FISSTART = 1,  FUPDATETYPE = 2 , ";
/*      */         }
/*      */       
/*      */       }
/*      */     
/* 2403 */     } else if (baseType == DateBasetype.FreeItem) {
/* 2404 */       if (leiType.equals("FNAME")) {
/* 2405 */         updateSql = String.valueOf(updateSql) + " FName = '" + obj.toString() + "', ";
/* 2406 */       } else if (leiType.equals("FUPDATETYPE")) {
/* 2407 */         updateSql = String.valueOf(updateSql) + "  FUPDATETYPE = " + obj.toString() + " , ";
/*      */       }
/*      */     
/*      */     }
/* 2411 */     else if (baseType == DateBasetype.ExpenseTypeToMid) {
/* 2412 */       if (leiType.equals("FNAME")) {
/* 2413 */         updateSql = String.valueOf(updateSql) + " FName = '" + obj.toString() + "', ";
/* 2414 */       } else if (leiType.equals("FUPDATETYPE")) {
/* 2415 */         if (obj.toString().equals("1")) {
/* 2416 */           updateSql = String.valueOf(updateSql) + " FSTATUS = 1,  FUPDATETYPE = 1 , ";
/* 2417 */         } else if (obj.toString().equals("2")) {
/* 2418 */           updateSql = String.valueOf(updateSql) + " FSTATUS = 0,  FUPDATETYPE = 2 , ";
/*      */         }
/*      */       
/*      */       }
/*      */     
/* 2423 */     } else if (baseType == DateBasetype.PayTypeTree) {
/* 2424 */       if (leiType.equals("FNAME")) {
/* 2425 */         updateSql = String.valueOf(updateSql) + " FName = '" + obj.toString() + "', ";
/* 2426 */       } else if (leiType.equals("FUPDATETYPE")) {
/* 2427 */         if (obj.toString().equals("1")) {
/* 2428 */           updateSql = String.valueOf(updateSql) + " FSTATUS = 1,  FUPDATETYPE = 1 , ";
/* 2429 */         } else if (obj.toString().equals("2")) {
/* 2430 */           updateSql = String.valueOf(updateSql) + " FSTATUS = 0,  FUPDATETYPE = 2 , ";
/*      */         }
/*      */       
/*      */       }
/*      */     
/* 2435 */     } else if (baseType == DateBasetype.orgUnit) {
/* 2436 */       if (leiType.equals("FNAME")) {
/* 2437 */         updateSql = String.valueOf(updateSql) + " FName = '" + obj.toString() + "', ";
/* 2438 */       } else if (leiType.equals("FLEVEL")) {
/* 2439 */         updateSql = String.valueOf(updateSql) + " FLEVEL = '" + obj.toString() + "', ";
/* 2440 */       } else if (leiType.equals("FPARENTID")) {
/* 2441 */         updateSql = String.valueOf(updateSql) + " FParentID = '" + obj.toString() + "', ";
/* 2442 */       } else if (leiType.equals("FCP")) {
/* 2443 */         updateSql = String.valueOf(updateSql) + " FCP =  '" + obj.toString() + "', ";
/* 2444 */       } else if (leiType.equals("FISCOMPANYORGUNIT")) {
/* 2445 */         updateSql = String.valueOf(updateSql) + " FISCOMPANYORGUNIT  = '" + obj.toString() + "', ";
/* 2446 */       } else if (leiType.equals("FOBJECT")) {
/* 2447 */         updateSql = String.valueOf(updateSql) + " FOBJECT  = '" + obj.toString() + "', ";
/* 2448 */       } else if (leiType.equals("FISOUSEALUP")) {
/* 2449 */         if (obj.toString().equals("1")) {
/* 2450 */           updateSql = String.valueOf(updateSql) + "   FISOUSEALUP = 0, FUPDATETYPE = 1 , ";
/* 2451 */         } else if (obj.toString().equals("2")) {
/* 2452 */           updateSql = String.valueOf(updateSql) + "   FISOUSEALUP = 1, FUPDATETYPE = 2 , ";
/*      */         } 
/*      */       } 
/*      */     } 
/* 2460 */     return updateSql;
/*      */   }

/**
 * 职位
 */
public void SyncPosition(Context ctx, String dataBase) throws BOSException {
	StringBuffer sql = new StringBuffer();
	List<String> sqls = new ArrayList<String>();
	List<String> updatesqls = new ArrayList<String>();
	sql
			.append("select FID as fid,FNAME_L2 as Fname,FNumber,")
			.append("to_char(FCreateTime,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , (case FDeletedStatus when 2 then 2 else 0 end)as FupdateType , 0 as fSign_0  , 0 as fSign_1  from T_ORG_Position");
	IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
	try {
		String tableName = "EAS_POST_MIDTABLE";
		String queryStr = "select FID,FNUMBER,fname, fupdateType from  "
				+ tableName + " ";
		List<Map<String, Object>> rets = EAISynTemplate.query(ctx,
				dataBase, queryStr);

		Map<String, Map<String, Object>> mapAll = new HashMap<String, Map<String, Object>>();
		for (int i = 0; i < rets.size(); i++) {
			mapAll.put((String) rets.get(i).get("FID"), rets.get(i));
		}

		while (rows.next()) {
			String fid = rows.getString("fid");
			String name = rows.getString("fname");
			String number = rows.getString("fnumber");
			String fupdateType = rows.getString("FupdateType");

			Map<String, Object> mp = mapAll.get(fid);
			if (mp != null && mp.size() > 0) {
				boolean flag = false;
				String upStr= "修改的字段为：";
				String updateSql = " update EAS_POST_MIDTABLE set ";
				
				if (VerifyUtil.notNull(mp.get("FNAME"))
						&& !mp.get("FNAME").equals(name)) {
					updateSql = updateMidTable(DateBasetype.position,"FNAME",(Object)name, updateSql);
					upStr += "名称；";
					flag = true;
				}
				
				if (VerifyUtil.notNull(mp.get("FUPDATETYPE")) && !mp.get("FUPDATETYPE").toString().equals(
								fupdateType)) {
					if (fupdateType.equals("0")) {// 生效
						updateSql = updateMidTable(DateBasetype.position,"FUPDATETYPE",(Object)1, updateSql);
					}
					if (fupdateType.equals("2")) {// 失效
						updateSql = updateMidTable(DateBasetype.position,"FUPDATETYPE",(Object)2, updateSql);
					}
					upStr += "状态；";
					flag = true;
				}
				
				if(flag){
					updateSql += " fSign_0 = 0 , fSign_1 = 0   where fid='"+fid+"' ";
					insertLog(ctx, DateBaseProcessType.Update,DateBasetype.position, name, number,upStr);
					updatesqls.add(updateSql);
				}
			} else {
				// 新增
				String sqlStr = insertOneMidTable(ctx,
						DateBaseProcessType.AddNew, DateBasetype.position,
						dataBase, tableName, rows);
				sqls.add(sqlStr);
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}
	if (sqls.size() > 0) {
		EAISynTemplate.executeBatch(ctx, dataBase, sqls);
	}
	if (updatesqls.size() > 0) {
		doInsertSqls(ctx, dataBase, updatesqls);
	}
}
/*      */ 
/*      */   
/*      */   public void SyncPerson(Context ctx, String dataBase) throws BOSException {
/* 2547 */     AppUnit.updateNotExistsPerson(ctx, dataBase);
/*      */     
/* 2549 */     StringBuffer sql = new StringBuffer();
/* 2550 */     List<String> sqls = new ArrayList<String>();
/* 2551 */     List<String> updatesqls = new ArrayList<String>();
/*      */     
/* 2553 */     Map<Object, Object> sendMp = new HashMap<Object, Object>();
/* 2554 */     List<Map> ls = new ArrayList<Map>();
/*      */     
/* 2580 */     IRowSet rows = DbUtil.executeQuery(ctx, "select * from V_HR_PersonMain_oa");
/*      */     try {
/* 2582 */       String tableName = "EAS_Person_MIDTABLE";
/* 2583 */       String queryStr = "select FID,FNUMBER,fname,FEmployeeTypeNumber,Forgtid,ForgNumber,ForgName,FPOSITIONNUMBER,Fdeptid,FEmployeeTypeName,nvl(Bank,'') Bank ,nvl(BankNum,'') BankNum,FEnterDate,FPlanFormalDate,FJoblevelNumber,FJobCategoryNumber,POSTCODE from " + 
/* 2584 */         tableName + " ";
/* 2585 */       List<Map<String, Object>> rets = EAISynTemplate.query(ctx, dataBase, queryStr);
/* 2586 */       List<Map<String, String>> updatePerson = new ArrayList<Map<String, String>>();
/*      */       
/* 2588 */       Map<String, Map<String, Object>> mapAll = new HashMap<String, Map<String, Object>>();
/* 2589 */       for (int i = 0; i < rets.size(); i++) {
/* 2590 */         mapAll.put((String)((Map)rets.get(i)).get("FID"), rets.get(i));
/*      */       }
/*      */       
/* 2593 */       while (rows.next()) {
/* 2594 */         String fid = rows.getString("fid");
/* 2595 */         String name = rows.getString("fname");
/* 2596 */         String number = rows.getString("fnumber");
/* 2597 */         String fEmployeeTypeNumber = rows.getString("FEmployeeTypeNumber");
/*      */         
/* 2599 */         String forgtid = rows.getString("FORGTID");
/* 2600 */         String forgNumber = rows.getString("FORGNUMBER");
/* 2601 */         String forgName = rows.getString("FORGNAME");
/* 2602 */         String fdeptid = rows.getString("FDEPTID");
/* 2603 */         String bank = rows.getString("BANK");
/* 2604 */         String bankNum = rows.getString("BankNum");
/*      */         
/* 2606 */         String positionNumber = rows.getString("FPOSITIONNUMBER");
/* 2607 */         String positionid = rows.getString("FPOSITIONID");
/* 2608 */         String positionName = rows.getString("FPOSITIONNAME");
/*      */         
/* 2610 */         String joblevelNumber = rows.getString("FJOBLEVELNUMBER");
/* 2611 */         String joblevelid = rows.getString("FJOBLEVELID");
/* 2612 */         String joblevelName = rows.getString("FJOBLEVELNAME");
/* 2613 */         String enterdate = rows.getString("FENTERDATE");
/* 2614 */         String planFormalDate = rows.getString("FPLANFORMALDATE");
/*      */         
/* 2616 */         String jobCategoryID = rows.getString("FJOBCATEGORYID");
/* 2617 */         String jobCategoryName = rows.getString("FJOBCATEGORYNAME");
/* 2618 */         String jobCategoryNumber = rows.getString("FJOBCATEGORYNUMBER");
/*      */         
/* 2620 */         String postcode = rows.getString("POSTCODE");
/*      */         
/* 2622 */         Map<String, Object> mp = mapAll.get(fid);
/* 2623 */         if (mp != null && mp.size() > 0) {
/* 2624 */           boolean flag = false;
/* 2625 */           String upStr = "修改的字段为：";
/* 2626 */           String updateSql = " update EAS_Person_MIDTABLE set ";
/*      */           
/* 2628 */           if (VerifyUtil.notNull(mp.get("FNUMBER")) && 
/* 2629 */             !mp.get("FNUMBER").equals(number)) {
/* 2630 */             updateSql = updateMidTable(DateBasetype.Person, "FNUMBER", number, updateSql);
/* 2631 */             upStr = String.valueOf(upStr) + "编码；；";
/* 2632 */             flag = true;
/*      */             
/* 2634 */             Map<String, String> personmp = new HashMap<String, String>();
/* 2635 */             personmp.put("fid", fid);
/* 2636 */             personmp.put("oldNo", mp.get("FNUMBER").toString());
/* 2637 */             personmp.put("newNo", number);
/* 2638 */             ls.add(personmp);
/*      */           } 
/*      */ 
/* 2649 */           if (VerifyUtil.notNull(mp.get("FNAME")) && 
/* 2650 */             !mp.get("FNAME").equals(name)) {
/* 2651 */             updateSql = updateMidTable(DateBasetype.Person, "FNAME", name, updateSql);
/* 2652 */             upStr = String.valueOf(upStr) + "名称；";
/* 2653 */             flag = true;
/*      */           } 
/*      */           
/* 2656 */           if (VerifyUtil.notNull(mp.get("FORGTID")) && 
/* 2657 */             !mp.get("FORGTID").toString().equals(forgtid) && forgtid != null) {
/* 2658 */             updateSql = updateMidTable(DateBasetype.Person, "FORGTID", forgtid + ";" + forgNumber + ";" + forgName, updateSql);
/* 2659 */             flag = true;
/* 2660 */             upStr = String.valueOf(upStr) + "组织；";
/*      */           } 
/*      */ 
/*      */ 
/*      */           
/* 2665 */           if (VerifyUtil.notNull(mp.get("FDEPTID")) && 
/* 2666 */             !mp.get("FDEPTID").toString().equals(fdeptid) && fdeptid != null) {
/* 2667 */             updateSql = updateMidTable(DateBasetype.Person, "FDEPTID", fdeptid, updateSql);
/* 2668 */             flag = true;
/* 2669 */             upStr = String.valueOf(upStr) + "部门；";
/*      */           } 
/*      */           
/* 2672 */           if (VerifyUtil.notNull(mp.get("FPOSITIONNUMBER")) && 
/* 2673 */             !mp.get("FPOSITIONNUMBER").toString().equals(positionNumber) && positionNumber != null) {
/* 2674 */             updateSql = updateMidTable(DateBasetype.Person, "FPOSITIONNUMBER", positionNumber + ";" + positionName + ";" + positionid, updateSql);
/* 2675 */             flag = true;
/* 2676 */             upStr = String.valueOf(upStr) + "职位；";
/*      */           } 
/*      */           
/* 2679 */           if (VerifyUtil.notNull(mp.get("FENTERDATE")) && 
/* 2680 */             !mp.get("FENTERDATE").toString().equals(enterdate) && enterdate != null) {
/* 2681 */             updateSql = updateMidTable(DateBasetype.Person, "FENTERDATE", enterdate, updateSql);
/* 2682 */             flag = true;
/* 2683 */             upStr = String.valueOf(upStr) + "入职日期；";
/*      */           } 
/*      */           
/* 2686 */           if (VerifyUtil.notNull(mp.get("FPLANFORMALDATE")) && 
/* 2687 */             !mp.get("FPLANFORMALDATE").toString().equals(planFormalDate) && planFormalDate != null) {
/* 2688 */             updateSql = updateMidTable(DateBasetype.Person, "FPLANFORMALDATE", planFormalDate, updateSql);
/* 2689 */             flag = true;
/* 2690 */             upStr = String.valueOf(upStr) + "计划入职日期；";
/*      */           } 
/*      */           
/* 2693 */           if (VerifyUtil.notNull(mp.get("FJOBLEVELNUMBER")) && 
/* 2694 */             !mp.get("FJOBLEVELNUMBER").toString().equals(joblevelNumber) && joblevelNumber != null) {
/* 2695 */             updateSql = updateMidTable(DateBasetype.Person, "FJOBLEVELNUMBER", joblevelNumber + ";" + joblevelName + ";" + joblevelid, updateSql);
/* 2696 */             flag = true;
/* 2697 */             upStr = String.valueOf(upStr) + "职层；";
/*      */           } 
/*      */           
/* 2700 */           if (VerifyUtil.notNull(mp.get("FJOBCATEGORYNUMBER")) && 
/* 2701 */             !mp.get("FJOBCATEGORYNUMBER").toString().equals(jobCategoryNumber) && jobCategoryNumber != null) {
/* 2702 */             updateSql = updateMidTable(DateBasetype.Person, "FJOBCATEGORYNUMBER", jobCategoryNumber + ";" + jobCategoryName + ";" + jobCategoryID, updateSql);
/* 2703 */             flag = true;
/* 2704 */             upStr = String.valueOf(upStr) + "职位类；";
/*      */           } 
/*      */  
/* 2716 */           if (mp.get("BANKNUM") != null && VerifyUtil.notNull(bankNum) && 
/* 2717 */             !mp.get("BANKNUM").toString().trim().equals(bankNum.trim())) {
/* 2718 */             updateSql = updateMidTable(DateBasetype.Person, "BANKNUM", bankNum, updateSql);
/* 2719 */             flag = true;
/* 2720 */             upStr = String.valueOf(upStr) + "银行账号；";
/*      */           } 
/*      */ 
/*      */           
/* 2724 */           if (VerifyUtil.notNull(mp.get("BANK")) && 
/* 2725 */             !mp.get("BANK").toString().trim().equals(bank.trim()) && bank != null) {
/* 2726 */             updateSql = updateMidTable(DateBasetype.Person, "BANK", bank, updateSql);
/* 2727 */             flag = true;
/* 2728 */             upStr = String.valueOf(upStr) + "银行；";
/*      */           } 
/*      */           
/* 2731 */           if (VerifyUtil.notNull(mp.get("POSTCODE")) && 
/* 2732 */             !mp.get("POSTCODE").toString().equals(postcode) && postcode != null) {
/* 2733 */             updateSql = updateMidTable(DateBasetype.Person, "POSTCODE", postcode, updateSql);
/* 2734 */             flag = true;
/* 2735 */             upStr = String.valueOf(upStr) + "职级；";
/*      */           } 
/*      */           
/* 2738 */           if (VerifyUtil.notNull(mp.get("FEMPLOYEETYPENUMBER")) && 
/*      */             
/* 2740 */             !mp.get("FEMPLOYEETYPENUMBER").toString().equals(fEmployeeTypeNumber)) {
/* 2741 */             if (fEmployeeTypeNumber.equals("0")) {
/* 2742 */               updateSql = updateMidTable(DateBasetype.Person, "FUPDATETYPE", Integer.valueOf(1), updateSql);
/*      */             }
/* 2744 */             if (fEmployeeTypeNumber.equals("1")) {
/* 2745 */               updateSql = updateMidTable(DateBasetype.Person, "FUPDATETYPE", Integer.valueOf(2), updateSql);
/*      */             }
/* 2747 */             flag = true;
/* 2748 */             upStr = String.valueOf(upStr) + "状态；";
/*      */           } 
/* 2750 */           if (flag) {
/* 2751 */             updateSql = String.valueOf(updateSql) + " fSign_0 = 0 , fSign_1 = 0   where fid='" + fid + "' ";
/* 2752 */             insertLog(ctx, DateBaseProcessType.Update, DateBasetype.Person, name, number, upStr);
/* 2753 */             updatesqls.add(updateSql);
/*      */           } 
/*      */           
/*      */           continue;
/*      */         } 
/* 2758 */         String sqlStr = insertOneMidTable(ctx, 
/* 2759 */             DateBaseProcessType.AddNew, DateBasetype.Person, 
/* 2760 */             dataBase, tableName, rows);
/* 2761 */         sqls.add(sqlStr);
/*      */       }
/*      */     
/*      */     }
/* 2765 */     catch (Exception e) {
/* 2766 */       e.printStackTrace();
/*      */     } 
/*      */     
/* 2769 */     if (sqls.size() > 0) {
/* 2770 */       EAISynTemplate.executeBatch(ctx, dataBase, sqls);
/*      */     }
/* 2772 */     if (updatesqls.size() > 0) {
/* 2773 */       doInsertSqls(ctx, dataBase, updatesqls);
/*      */     }
/*      */     
/* 2776 */     if (ls.size() > 0) {
/* 2777 */       SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
/* 2778 */       Date date = new Date();
/* 2779 */       sendMp.put("count", Integer.valueOf(ls.size()));
/* 2780 */       sendMp.put("time", df.format(date));
/* 2781 */       sendMp.put("data", ls);
/* 2782 */       requestOAInterface(sendMp);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private static void requestOAInterface(Map mp) {
/* 2788 */     String loginUrl = "http://oa.meiweigroup.com:8001/seeyon/main.do?method=login";
/*      */     
/* 2790 */     String dataUrl = "http://oa.meiweigroup.com:8001/seeyon/lolkk/thirdUrlController.do?method=updateUserCode";
/* 2791 */     HttpClient httpClient = new HttpClient();
/* 2793 */     PostMethod postMethod = new PostMethod(loginUrl);
/* 2795 */     NameValuePair[] data = { new NameValuePair("login_username", "bd3"), new NameValuePair("login_password", "meiwei2020") };
/* 2808 */     postMethod.setRequestBody(data);
/*      */     try {
/* 2811 */       httpClient.getParams().setCookiePolicy("compatibility");
/* 2812 */       httpClient.executeMethod((HttpMethod)postMethod);
/*      */       
/* 2814 */       Cookie[] cookies = httpClient.getState().getCookies();
/* 2815 */       StringBuffer tmpcookies = new StringBuffer(); byte b; int i; Cookie[] arrayOfCookie1;
/* 2816 */       for (i = (arrayOfCookie1 = cookies).length, b = 0; b < i; ) { Cookie c = arrayOfCookie1[b];
/* 2817 */         tmpcookies.append(String.valueOf(c.toString()) + ";"); b++; }
/*      */       
/* 2819 */       String body = JSON.toJSONString(mp);
/* 2820 */       StringRequestEntity stringRequestEntity = new StringRequestEntity(body, "application/json", "UTF-8");
/* 2821 */       PostMethod postMethod1 = new PostMethod(dataUrl);
/*      */       
/* 2823 */       postMethod1.setRequestHeader("cookie", tmpcookies.toString());
/*      */       
/* 2825 */       postMethod1.setRequestEntity((RequestEntity)stringRequestEntity);
/* 2826 */       postMethod1.setRequestHeader("Content-Type", "application/json");
/* 2827 */       httpClient.executeMethod((HttpMethod)postMethod1);
/* 2828 */       String text = postMethod1.getResponseBodyAsString();
/* 2831 */       System.out.println(text);
/* 2832 */     } catch (Exception e) {
/* 2833 */       e.printStackTrace();
/*      */     } 
/*      */   }  
/*      */   public void SyncSupplier(Context ctx, String dataBase) throws BOSException {
/* 2842 */     StringBuffer sql = new StringBuffer();
/* 2843 */     List<String> sqls = new ArrayList<String>();
/* 2844 */     List<String> updatesqls = new ArrayList<String>();
/* 2845 */     sql
/* 2846 */       .append(
/* 2847 */         "/*dialect*/ select a.FID as FID, c.fname_l2 as FCLASSNAME,a.FNumber as FNUMBER,a.FName_L2 as FNAME,'' as  FOPENBANK,'' as FBANKACCOUNT,")
/* 2848 */       .append(
/* 2849 */         "a.FCreatorID as FCREATOR,(CASE WHEN b.FNumber='M' THEN 1 ELSE 0 END) as FISGROUP,")
/* 2850 */       .append(
/* 2851 */         "b.FNumber as FORGNUMBER,b.FName_L2 as FORGNAME,(case a.FUsedStatus when 1  then 0 when 0 then 0  else 2 end  ) as FupdateType,b.FID as FORGTID,0 as FSIGN,''as FSYNLOG,0 as FMAILSIGN,")
/* 2852 */       .append(
/* 2853 */         "to_char(a.FCreateTime,'yyyy-mm-dd hh24:mi:ss') as FCreateTime,to_char(a.FLastUpdateTime,'yyyy-mm-dd hh24:mi:ss') as FUpdateTime ,")
/* 2854 */       .append(
/* 2855 */         "(case a.FUsedStatus when 3  then 1 else 0 end  ) as FStatus  from T_BD_Supplier a  INNER JOIN T_ORG_CtrlUnit  b on a.FADMINCUID=b.FID")
/* 2856 */       .append(
/* 2857 */         " inner join T_BD_CSSPGroup c on a.FBrowseGroupID = c.fid  where c.fname_l2 ='加工厂'");
/* 2858 */     IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
/*      */     try {
/* 2860 */       String tableName = "EAS_Supplier_MIDTABLE";
/* 2861 */       String queryStr = "select FID,FNUMBER,fname ,FupdateType from  EAS_Supplier_MIDTABLE ";
/* 2862 */       List<Map<String, Object>> rets = EAISynTemplate.query(ctx, 
/* 2863 */           dataBase, queryStr);
/*      */       
/* 2865 */       Map<String, Map<String, Object>> mapAll = new HashMap<String, Map<String, Object>>();
/* 2866 */       for (int i = 0; i < rets.size(); i++) {
/* 2867 */         mapAll.put((String)((Map)rets.get(i)).get("FID"), rets.get(i));
/*      */       }
/* 2869 */       while (rows.next()) {
/* 2870 */         String fid = rows.getString("fid");
/* 2871 */         String name = rows.getString("fname");
/* 2872 */         String number = rows.getString("fnumber");
/* 2873 */         String status = rows.getString("FupdateType");
/*      */         
/* 2875 */         Map<String, Object> mp = mapAll.get(fid);
/* 2876 */         if (mp != null && mp.size() > 0) {
/* 2877 */           String upStr = "修改的字段为：";
/* 2878 */           boolean flag = false;
/* 2879 */           String updateSql = " update EAS_Supplier_MIDTABLE set ";
/* 2880 */           if (VerifyUtil.notNull(mp.get("FNAME")) && 
/* 2881 */             !mp.get("FNAME").equals(name)) {
/* 2882 */             updateSql = updateMidTable(DateBasetype.Supplier2HIS, "FNAME", name, updateSql);
/* 2883 */             upStr = String.valueOf(upStr) + "名称；";
/* 2884 */             flag = true;
/*      */           } 
/* 2886 */           if (VerifyUtil.notNull(mp.get("FUPDATETYPE")) && 
/* 2887 */             !mp.get("FUPDATETYPE").toString().equals(status)) {
/* 2888 */             if (status.equals("0") || status.equals("1")) {
/* 2889 */               updateSql = updateMidTable(DateBasetype.Supplier2HIS, "FUPDATETYPE", Integer.valueOf(1), updateSql);
/*      */             }
/* 2891 */             if (status.equals("2")) {
/* 2892 */               updateSql = updateMidTable(DateBasetype.Supplier2HIS, "FUPDATETYPE", Integer.valueOf(2), updateSql);
/*      */             }
/* 2894 */             upStr = String.valueOf(upStr) + "状态；";
/* 2895 */             flag = true;
/*      */           } 
/*      */           
/* 2898 */           if (flag) {
/* 2899 */             updateSql = String.valueOf(updateSql) + " FSIGN = 0,FupdateTime = SYSDATE   where fid='" + fid + "' ";
/* 2900 */             insertLog(ctx, DateBaseProcessType.Update, DateBasetype.Supplier2HIS, name, number, upStr);
/* 2901 */             updatesqls.add(updateSql);
/*      */           } 
/*      */           continue;
/*      */         } 
/* 2905 */         String sqlStr = insertOneMidTable(ctx, 
/* 2906 */             DateBaseProcessType.AddNew, 
/* 2907 */             DateBasetype.Supplier2HIS, dataBase, 
/* 2908 */             "EAS_Supplier_MIDTABLE", rows);
/* 2909 */         sqls.add(sqlStr);
/*      */       }
/*      */     
/* 2912 */     } catch (Exception e) {
/* 2913 */       e.printStackTrace();
/*      */     } 
/* 2915 */     if (sqls.size() > 0) {
/* 2916 */       EAISynTemplate.executeBatch(ctx, dataBase, sqls);
/*      */     }
/* 2918 */     if (updatesqls.size() > 0) {
/* 2919 */       doInsertSqls(ctx, dataBase, updatesqls);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void SyncEasPayType(Context ctx, String dataBase) throws BOSException {
/* 2930 */     StringBuffer sql = new StringBuffer();
/* 2931 */     List<String> sqls = new ArrayList<String>();
/* 2932 */     List<String> updatesqls = new ArrayList<String>();
/* 2933 */     sql
/* 2934 */       .append("/*dialect*/ select a.FID as fid,a.FNUMBER as fnumber,a.FNAME_L2 as Fname,")
/* 2935 */       .append("a.CFCTRLORGUNITID as Forgtid,c.FNUMBER as ForgNumber,c.FNAME_L2 as ForgName,")
/* 2936 */       .append("b.FLEVEL as FLevel,b.FID as FParentID,a.fcreatorid as Fcreator,CFDATASTATE as FIsStart, (case CFDATASTATE when 1  then 2  else 0 end) as FupdateType,")
/* 2937 */       .append("nvl(cus.fnumber,'') FCusNumber , nvl(cus.Fname_L2,'') as FCusName,")
/* 2938 */       .append("to_char(a.FCreateTime,'yyyy-mm-dd hh24:mi:ss') as FCreateTime,to_char(a.FLastUpdateTime,'yyyy-mm-dd hh24:mi:ss') as FUpdateTime")
/* 2939 */       .append(" from ct_cus_easPaytype a inner join t_cus_easPaytypeTree b on a.FTREEID=b.FID")
/* 2940 */       .append(" LEFT OUTER JOIN t_bd_customer cus on cus.Fid = a.cfcustomerid")
/* 2941 */       .append(" LEFT OUTER JOIN T_ORG_CtrlUnit c on a.CFCTRLORGUNITID=c.FID")
/* 2942 */       .append(" order by a.FNUMBER desc");
/* 2943 */     IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
/*      */     try {
/* 2945 */       String tableName = "EAS_PAYTYPE_MIDTABLE";
/* 2946 */       String queryStr = "select FID,FNUMBER,fname,FIsStart from  " + 
/* 2947 */         tableName + " ";
/* 2948 */       List<Map<String, Object>> rets = EAISynTemplate.query(ctx, 
/* 2949 */           dataBase, queryStr);
/*      */       
/* 2951 */       Map<String, Map<String, Object>> mapAll = new HashMap<String, Map<String, Object>>();
/* 2952 */       for (int i = 0; i < rets.size(); i++) {
/* 2953 */         mapAll.put((String)((Map)rets.get(i)).get("FID"), rets.get(i));
/*      */       }
/* 2955 */       while (rows.next()) {
/* 2956 */         String fid = rows.getString("fid");
/* 2957 */         String name = rows.getString("fname");
/* 2958 */         String number = rows.getString("fnumber");
/* 2959 */         String fIsStart = rows.getString("FIsStart");
/*      */         
/* 2961 */         Map<String, Object> mp = mapAll.get(fid);
/* 2962 */         if (mp != null && mp.size() > 0) {
/* 2963 */           String upStr = "修改的字段为：";
/* 2964 */           boolean flag = false;
/* 2965 */           String updateSql = " update EAS_PAYTYPE_MIDTABLE set ";
/*      */           
/* 2967 */           if (VerifyUtil.notNull(mp.get("FNAME")) && 
/* 2968 */             !mp.get("FNAME").equals(name)) {
/* 2969 */             updateSql = updateMidTable(DateBasetype.PayType, "FNAME", name, updateSql);
/* 2970 */             upStr = String.valueOf(upStr) + "名称；";
/* 2971 */             flag = true;
/*      */           } 
/*      */ 
/*      */           
/* 2975 */           if (VerifyUtil.notNull(mp.get("FISSTART")) && !mp.get("FISSTART").toString().equals(fIsStart)) {
/* 2976 */             if (fIsStart.equals("0")) {
/* 2977 */               updateSql = updateMidTable(DateBasetype.PayType, "FUPDATETYPE", Integer.valueOf(1), updateSql);
/*      */             }
/* 2979 */             if (fIsStart.equals("1")) {
/* 2980 */               updateSql = updateMidTable(DateBasetype.PayType, "FUPDATETYPE", Integer.valueOf(2), updateSql);
/*      */             }
/* 2982 */             flag = true;
/* 2983 */             upStr = String.valueOf(upStr) + "状态";
/*      */           } 
/* 2985 */           if (flag) {
/* 2986 */             updateSql = String.valueOf(updateSql) + " fSign = 0   where fid='" + fid + "' ";
/* 2987 */             insertLog(ctx, DateBaseProcessType.Update, DateBasetype.PayType, name, number, upStr);
/* 2988 */             updatesqls.add(updateSql);
/*      */           } 
/*      */           continue;
/*      */         } 
/* 2992 */         String sqlStr = insertOneMidTable(ctx, 
/* 2993 */             DateBaseProcessType.AddNew, DateBasetype.PayType, 
/* 2994 */             dataBase, tableName, rows);
/* 2995 */         sqls.add(sqlStr);
/*      */       }
/*      */     
/* 2998 */     } catch (Exception e) {
/* 2999 */       e.printStackTrace();
/*      */     } 
/* 3001 */     if (sqls.size() > 0) {
/* 3002 */       EAISynTemplate.executeBatch(ctx, dataBase, sqls);
/*      */     }
/* 3004 */     if (updatesqls.size() > 0) {
/* 3005 */       doInsertSqls(ctx, dataBase, updatesqls);
/*      */     }
/* 3007 */     SyncEasPayTypeTree(ctx, "04");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void SyncFreeItem(Context ctx, String dataBase) throws BOSException {
/* 3015 */     StringBuffer sql = new StringBuffer();
/* 3016 */     List<String> sqls = new ArrayList<String>();
/* 3017 */     List<String> updatesqls = new ArrayList<String>();
/* 3018 */     sql.append(" /*dialect*/select FID as fid,FNUMBER as fnumber,FNAME_L2 as Fname,fcreatorid as Fcreator,")
/* 3019 */       .append("to_char(FCreateTime,'yyyy-mm-dd hh24:mi:ss') as FCreateTime,(case CFDATASTATE when 1  then 2 else 0 end  ) as FupdateType ,")
/* 3020 */       .append("to_char(FLastUpdateTime,'yyyy-mm-dd hh24:mi:ss') as FUpdateTime from ct_cus_freeItem ");
/* 3021 */     IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
/*      */     try {
/* 3023 */       String tableName = "EAS_FreeItem_MIDTABLE";
/* 3024 */       String queryStr = "select FID,FNUMBER,fname,FupdateType from  " + 
/* 3025 */         tableName + " ";
/* 3026 */       List<Map<String, Object>> rets = EAISynTemplate.query(ctx, 
/* 3027 */           dataBase, queryStr);
/*      */       
/* 3029 */       Map<String, Map<String, Object>> mapAll = new HashMap<String, Map<String, Object>>();
/* 3030 */       for (int i = 0; i < rets.size(); i++) {
/* 3031 */         mapAll.put((String)((Map)rets.get(i)).get("FID"), rets.get(i));
/*      */       }
/*      */       
/* 3034 */       while (rows.next()) {
/* 3035 */         String fid = rows.getString("fid");
/* 3036 */         String name = rows.getString("fname");
/* 3037 */         String number = rows.getString("fnumber");
/* 3038 */         String status = rows.getString("FupdateType");
/*      */         
/* 3040 */         Map<String, Object> mp = mapAll.get(fid);
/* 3041 */         if (mp != null && mp.size() > 0) {
/* 3042 */           String upStr = "修改的字段为:";
/* 3043 */           String updateSql = " update EAS_PAYTYPE_MIDTABLE set ";
/* 3044 */           boolean flag = false;
/*      */           
/* 3046 */           if (VerifyUtil.notNull(mp.get("FNAME")) && !mp.get("FNAME").equals(name)) {
/* 3047 */             updateSql = updateMidTable(DateBasetype.FreeItem, "FNAME", name, updateSql);
/* 3048 */             upStr = String.valueOf(upStr) + "名称；";
/* 3049 */             flag = true;
/*      */           } 
/* 3051 */           if (VerifyUtil.notNull(mp.get("FUPDATETYPE")) && !mp.get("FUPDATETYPE").toString().equals(status)) {
/* 3052 */             if (status.equals("0")) {
/* 3053 */               updateSql = updateMidTable(DateBasetype.FreeItem, "FUPDATETYPE", Integer.valueOf(0), updateSql);
/*      */             }
/* 3055 */             if (status.equals("2")) {
/* 3056 */               updateSql = updateMidTable(DateBasetype.FreeItem, "FUPDATETYPE", Integer.valueOf(2), updateSql);
/*      */             }
/* 3058 */             flag = true;
/* 3059 */             upStr = String.valueOf(upStr) + "状态；";
/*      */           } 
/* 3061 */           if (flag) {
/* 3062 */             updateSql = String.valueOf(updateSql) + " fSign = 0   where fid='" + fid + "' ";
/* 3063 */             insertLog(ctx, DateBaseProcessType.Update, DateBasetype.FreeItem, name, number, upStr);
/* 3064 */             updatesqls.add(updateSql);
/*      */           } 
/*      */ 
/*      */           
/*      */           continue;
/*      */         } 
/*      */         
/* 3071 */         String sqlStr = insertOneMidTable(ctx, 
/* 3072 */             DateBaseProcessType.AddNew, DateBasetype.FreeItem, 
/* 3073 */             dataBase, tableName, rows);
/* 3074 */         sqls.add(sqlStr);
/*      */       }
/*      */     
/* 3077 */     } catch (Exception e) {
/* 3078 */       e.printStackTrace();
/*      */     } 
/* 3080 */     if (sqls.size() > 0) {
/* 3081 */       EAISynTemplate.executeBatch(ctx, dataBase, sqls);
/*      */     }
/* 3083 */     if (updatesqls.size() > 0) {
/* 3084 */       doInsertSqls(ctx, dataBase, updatesqls);
/*      */     }
/*      */   } 
/*      */   protected void _SynExpenseType(Context ctx, String dataBase) throws BOSException {
/* 3094 */     StringBuffer sql = new StringBuffer();
/* 3095 */     List<String> sqls = new ArrayList<String>();
/* 3096 */     List<String> updatesqls = new ArrayList<String>();
/* 3097 */     sql
/* 3098 */       .append(
/* 3099 */         "/*dialect*/ select a.fid as FID , a.fname_l2 AS FNAME, a.fnumber AS FNUMBER , b.fnumber AS FCLASSNUMBER , b.fname_l2  AS FCLASSNAME, c.fid AS FORGTID ,")
/* 3100 */       .append(
/* 3101 */         "c.fnumber AS FORGNUMBER,  c.fname_l2 AS FORGNAME  ,a.FCREATORID as FCREATOR ,to_char(a.FCREATETIME ,'yyyy-mm-dd hh24:mi:ss') as FCREATETIME,0 as FUPDATETYPE, a.FIsStart AS FSTATUS ,0 AS FSIGN , 0 as FMAILSIGN from T_BC_ExpenseType a  ")
/* 3102 */       .append(
/* 3103 */         "INNER JOIN   T_BC_OperationType b  ON  a.FOperationTypeID = b.fid  inner join  T_ORG_Company c on a.FCompanyID = c.fid  order by a.fnumber");
/*      */     
/* 3105 */     IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
/*      */     try {
/* 3107 */       String tableName = "EAS_PAYTYPE_OA_MIDTABLE";
/* 3108 */       String queryStr = "select FID,FNUMBER,fname,FSTATUS from  " + 
/* 3109 */         tableName + " ";
/* 3110 */       List<Map<String, Object>> rets = EAISynTemplate.query(ctx, 
/* 3111 */           dataBase, queryStr);
/*      */       
/* 3113 */       Map<String, Map<String, Object>> mapAll = new HashMap<String, Map<String, Object>>();
/* 3114 */       for (int i = 0; i < rets.size(); i++) {
/* 3115 */         mapAll.put((String)((Map)rets.get(i)).get("FID"), rets.get(i));
/*      */       }
/*      */       
/* 3118 */       while (rows.next()) {
/* 3119 */         String fid = rows.getString("fid");
/* 3120 */         String name = rows.getString("fname");
/* 3121 */         String number = rows.getString("fnumber");
/* 3122 */         String fStart = rows.getString("FSTATUS");
/*      */         
/* 3124 */         Map<String, Object> mp = mapAll.get(fid);
/* 3125 */         if (mp != null && mp.size() > 0) {
/* 3126 */           String upStr = "修改的字段为:";
/* 3127 */           String updateSql = " update EAS_PAYTYPE_OA_MIDTABLE set ";
/* 3128 */           boolean flag = false;
/*      */           
/* 3130 */           if (VerifyUtil.notNull(mp.get("FNAME")) && 
/* 3131 */             !mp.get("FNAME").equals(name)) {
/* 3132 */             updateSql = updateMidTable(DateBasetype.ExpenseTypeToMid, "FNAME", name, updateSql);
/* 3133 */             upStr = String.valueOf(upStr) + "名称；";
/* 3134 */             flag = true;
/*      */           } 
/*      */ 
/*      */           
/* 3138 */           if (VerifyUtil.notNull(mp.get("FSTATUS")) && 
/* 3139 */             !mp.get("FSTATUS").toString().equals(fStart)) {
/* 3140 */             if (fStart.equals("1")) {
/* 3141 */               updateSql = updateMidTable(DateBasetype.ExpenseTypeToMid, "FUPDATETYPE", Integer.valueOf(1), updateSql);
/*      */             }
/* 3143 */             if (fStart.equals("0")) {
/* 3144 */               updateSql = updateMidTable(DateBasetype.ExpenseTypeToMid, "FUPDATETYPE", Integer.valueOf(2), updateSql);
/*      */             }
/* 3146 */             flag = true;
/* 3147 */             upStr = String.valueOf(upStr) + "状态；";
/*      */           } 
/* 3149 */           if (flag) {
/* 3150 */             updateSql = String.valueOf(updateSql) + " fSign = 0   where fid='" + fid + "' ";
/* 3151 */             insertLog(ctx, DateBaseProcessType.Update, DateBasetype.ExpenseTypeToMid, name, number, upStr);
/* 3152 */             updatesqls.add(updateSql);
/*      */           } 
/*      */           
/*      */           continue;
/*      */         } 
/* 3157 */         String sqlStr = insertOneMidTable(ctx, 
/* 3158 */             DateBaseProcessType.AddNew, 
/* 3159 */             DateBasetype.ExpenseTypeToMid, dataBase, 
/* 3160 */             "EAS_PAYTYPE_OA_MIDTABLE", rows);
/* 3161 */         sqls.add(sqlStr);
/*      */       }
/*      */     
/*      */     }
/* 3165 */     catch (SQLException e) {
/*      */       
/* 3167 */       AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
/* 3168 */           DateBasetype.ExpenseTypeToMid, "单据保存失败", e.getMessage());
/* 3169 */       e.printStackTrace();
/*      */     } 
/* 3171 */     if (sqls.size() > 0) {
/* 3172 */       EAISynTemplate.executeBatch(ctx, dataBase, sqls);
/*      */     }
/* 3174 */     if (updatesqls.size() > 0) {
/* 3175 */       doInsertSqls(ctx, dataBase, updatesqls);
/*      */     }
/*      */   }   
/*      */   public void SyncEasPayTypeTree(Context ctx, String dataBase) throws BOSException {
/* 3185 */     StringBuffer sql = new StringBuffer();
/* 3186 */     List<String> sqls = new ArrayList<String>();
/* 3187 */     List<String> updatesqls = new ArrayList<String>();
/* 3188 */     sql.append(
/* 3189 */         "/*dialect*/ select a.FID as fid,a.FNUMBER as fnumber,a.FNAME_L2 as Fname,")
/* 3190 */       .append(
/* 3191 */         "a.FControlunitid as Forgtid,b.FNUMBER as ForgNumber,b.FNAME_L2 as ForgName,")
/* 3192 */       .append(
/* 3193 */         " a.FLEVEL as FLevel,a.FParentID as FParentID,a.fcreatorid as Fcreator, 0 as FupdateType,")
/* 3194 */       .append(
/* 3195 */         "to_char(a.FCreateTime,'yyyy-mm-dd hh24:mi:ss') as FCreateTime,to_char(a.FLastUpdateTime,'yyyy-mm-dd hh24:mi:ss') as FUpdateTime")
/* 3196 */       .append(" from  t_cus_easPaytypeTree a ")
/* 3197 */       .append(
/* 3198 */         " LEFT OUTER JOIN T_ORG_CtrlUnit b on a.FControlunitid=b.FID");
/* 3199 */     IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
/*      */     
/*      */     try {
/* 3202 */       String tableName = "EAS_PAYTYPETREE_MIDTABLE";
/* 3203 */       String queryStr = "select FID,FNUMBER,fname,FupdateType from  " + 
/* 3204 */         tableName + " ";
/* 3205 */       List<Map<String, Object>> rets = EAISynTemplate.query(ctx, 
/* 3206 */           dataBase, queryStr);
/*      */       
/* 3208 */       Map<String, Map<String, Object>> mapAll = new HashMap<String, Map<String, Object>>();
/* 3209 */       for (int i = 0; i < rets.size(); i++) {
/* 3210 */         mapAll.put((String)((Map)rets.get(i)).get("FID"), rets.get(i));
/*      */       }
/* 3212 */       while (rows.next()) {
/* 3213 */         String fid = rows.getString("fid");
/* 3214 */         String name = rows.getString("fname");
/* 3215 */         String number = rows.getString("fnumber");
/*      */         
/* 3217 */         Map<String, Object> mp = mapAll.get(fid);
/* 3218 */         if (mp != null && mp.size() > 0) {
/* 3219 */           String upStr = "修改的字段为:";
/* 3220 */           String updateSql = " update EAS_PAYTYPETREE_MIDTABLE set ";
/* 3221 */           boolean flag = false;
/*      */           
/* 3223 */           if (VerifyUtil.notNull(mp.get("FNAME")) && 
/* 3224 */             !mp.get("FNAME").equals(name)) {
/* 3225 */             updateSql = updateMidTable(DateBasetype.PayTypeTree, "FNAME", name, updateSql);
/* 3226 */             upStr = String.valueOf(upStr) + "名称：";
/* 3227 */             flag = true;
/*      */           } 
/*      */           
/* 3230 */           if (flag) {
/* 3231 */             updateSql = String.valueOf(updateSql) + "  fSign =0 ,FupdateType=1 where fid='" + fid + "' ";
/* 3232 */             insertLog(ctx, DateBaseProcessType.Update, DateBasetype.PayTypeTree, name, number, upStr);
/* 3233 */             updatesqls.add(updateSql);
/*      */           } 
/*      */           
/*      */           continue;
/*      */         } 
/* 3238 */         String sqlStr = insertOneMidTable(ctx, 
/* 3239 */             DateBaseProcessType.AddNew, 
/* 3240 */             DateBasetype.PayTypeTree, dataBase, tableName, rows);
/* 3241 */         sqls.add(sqlStr);
/*      */       }
/*      */     
/* 3244 */     } catch (Exception e) {
/* 3245 */       e.printStackTrace();
/*      */     } 
/*      */     
/* 3248 */     if (sqls.size() > 0) {
/* 3249 */       EAISynTemplate.executeBatch(ctx, dataBase, sqls);
/*      */     }
/* 3251 */     if (updatesqls.size() > 0) {
/* 3252 */       doInsertSqls(ctx, dataBase, updatesqls);
/*      */     }
/*      */   }   
/*      */   public void getPersonToMid(Context ctx, String dataBase, String fid) throws BOSException {
/* 3263 */     StringBuffer sql = new StringBuffer();
/* 3264 */     sql
/* 3265 */       .append(
/* 3266 */         "select f.fid,f.fnumber,f.fname_l2 as fname,g.fpositionid,h.fnumber as FPositionNumber,h.fname_l2 as FPositionName,")
/* 3267 */       .append(
/* 3268 */         "porg.FID as Forgtid,porg.fnumber as ForgNumber,porg.fname_l2 as ForgName,org.FID as Fdeptid,f.FNumber as FEMPNUMBER,")
/* 3269 */       .append(
/* 3270 */         "h.FNUMBER as Postcode,f.FEMail,'' as Bank,'' as Banknum,m.fnumber as FEmployeeTypeNumber,m.fname_l2 as FEmployeeTypeName,")
/* 3271 */       .append(
/* 3272 */         "to_char(f.FCreateTime,'yyyy-mm-dd hh24:mi:ss') as FCreateTime from T_BD_Person f")
/* 3273 */       .append(
/* 3274 */         " left join T_ORG_PositionMember g on f.fid=g.fpersonid")
/* 3275 */       .append(" left join T_ORG_Position h on g.fpositionid = h.fid")
/* 3276 */       .append(
/* 3277 */         " left join T_HR_BDEmployeeType m on f.FEmployeeTypeID = m.fid")
/* 3278 */       .append(
/* 3279 */         " inner join t_org_admin org on h.FAdminOrgUnitID=org.FID")
/* 3280 */       .append(
/* 3281 */         " inner join t_org_admin porg on porg.FID= org.FParentID where g.FISPRIMARY =1");
/* 3282 */     if (fid != null && !"".equals(fid)) {
/* 3283 */       sql.append(" where f.fid='");
/* 3284 */       sql.append(fid);
/* 3285 */       sql.append("'");
/*      */     } 
/* 3287 */     IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
/* 3288 */     insertMidTable(ctx, DateBaseProcessType.AddNew, DateBasetype.Person, 
/* 3289 */         dataBase, "EAS_Person_MIDTABLE", rows);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void getPositionToMid(Context ctx, String dataBase, String fid) throws BOSException {
/* 3298 */     StringBuffer sql = new StringBuffer();
/*      */     
/* 3300 */     sql
/* 3301 */       .append("select FID as fid,FNAME_L2 as Fname,FNumber,")
/* 3302 */       .append(
/* 3303 */         "to_char(FCreateTime,'yyyy-mm-dd hh24:mi:ss') as FCreateTime from T_ORG_Position");
/* 3304 */     if (fid != null && !"".equals(fid)) {
/* 3305 */       sql.append(" where fid='");
/* 3306 */       sql.append(fid);
/* 3307 */       sql.append("'");
/*      */     } 
/* 3309 */     IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
/* 3310 */     insertMidTable(ctx, DateBaseProcessType.AddNew, DateBasetype.position, 
/* 3311 */         dataBase, "EAS_POST_MIDTABLE", rows);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void getSupplierToHis(Context ctx, String dataBase, String fid) throws BOSException {
/* 3320 */     StringBuffer sql = new StringBuffer();
/* 3321 */     sql
/* 3322 */       .append(
/* 3323 */         "select a.FID as FID, '采购供应商' as FCLASSNAME,a.FNumber as FNUMBER,a.FName_L2 as FNAME,'工商银行' as  FOPENBANK,'611120120120' as FBANKACCOUNT,")
/* 3324 */       .append(
/* 3325 */         "a.FCreatorID as FCREATOR,(CASE WHEN b.FNumber='M' THEN 1 ELSE 0 END) as FISGROUP,")
/* 3326 */       .append(
/* 3327 */         "b.FNumber as FORGNUMBER,b.FName_L2 as FORGNAME,a.FUsedStatus as FSTATUS,b.FID as FORGTID,0 as FSIGN,''as FSYNLOG,0 as FMAILSIGN,")
/* 3328 */       .append(
/* 3329 */         "to_char(a.FCreateTime,'yyyy-mm-dd hh24:mi:ss') as FCreateTime,to_char(a.FLastUpdateTime,'yyyy-mm-dd hh24:mi:ss') as FUpdateTime")
/* 3330 */       .append(" from T_BD_Supplier as a")
/* 3331 */       .append(" INNER JOIN T_ORG_CtrlUnit as b on a.FADMINCUID=b.FID");
/* 3332 */     if (fid != null && !"".equals(fid)) {
/* 3333 */       sql.append(" where a.fid='");
/* 3334 */       sql.append(fid);
/* 3335 */       sql.append("'");
/*      */     } 
/* 3337 */     IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
/*      */     
/* 3339 */     insertMidTable(ctx, DateBaseProcessType.AddNew, 
/* 3340 */         DateBasetype.Supplier2HIS, dataBase, "EAS_Supplier_MIDTABLE", 
/* 3341 */         rows);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void insertMidTable(Context ctx, DateBaseProcessType processType, DateBasetype baseType, String dataBase, String tableName, IRowSet rows) {
/*      */     try {
/* 3348 */       List<String> sqls = new ArrayList<String>();
/* 3349 */       while (rows.next()) {
/* 3350 */         IRowSetMetaData rowSetMataData = rows.getRowSetMetaData();
/* 3351 */         int columnsSize = rowSetMataData.getColumnCount();
/* 3352 */         String insertSqlStart = "INSERT INTO " + tableName + "(";
/* 3353 */         String insertSqlValues = "";
/* 3354 */         String insertSql = "";
/* 3355 */         String name = rows.getString("fname");
/* 3356 */         String number = rows.getString("fnumber");
/* 3357 */         for (int i = 1; i <= columnsSize; i++) {
/* 3358 */           String columnName = rowSetMataData.getColumnName(i);
/* 3359 */           String value = rows.getString(columnName);
/* 3360 */           insertSqlStart = String.valueOf(insertSqlStart) + columnName + ",";
/* 3361 */           if (columnName.toLowerCase().endsWith("time") || columnName.toLowerCase().endsWith("date")) {
/*      */             
/* 3363 */             insertSqlValues = String.valueOf(insertSqlValues) + "to_date('" + value + 
/* 3364 */               "','yyyy-mm-dd hh24:mi:ss')" + ",";
/*      */           }
/* 3366 */           else if (value == null) {
/* 3367 */             insertSqlValues = String.valueOf(insertSqlValues) + "'0',";
/*      */           } else {
/* 3369 */             insertSqlValues = String.valueOf(insertSqlValues) + "'" + value + "',";
/*      */           } 
/*      */         } 
/*      */         
/* 3373 */         insertSqlStart = String.valueOf(insertSqlStart) + "FupdateType) VALUES(";
/* 3374 */         insertSqlValues = String.valueOf(insertSqlValues) + "0)";
/* 3375 */         insertSql = String.valueOf(insertSqlStart) + insertSqlValues;
/* 3376 */         System.out.print("**************sql=" + insertSql);
/* 3377 */         sqls.add(insertSql);
/*      */         
/* 3379 */         insertLog(ctx, processType, baseType, name, number, "");
/*      */       } 
/* 3381 */       EAISynTemplate.executeBatch(ctx, dataBase, sqls);
/* 3382 */     } catch (Exception e) {
/* 3383 */       e.printStackTrace();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void insertLog(Context ctx, DateBaseProcessType processType, DateBasetype baseType, String name, String number, String miaoshu) {
/*      */     try {
/* 3390 */       DateBaseLogInfo loginfo = new DateBaseLogInfo();
/* 3391 */       Calendar cal = Calendar.getInstance();
/* 3392 */       cal.setTime(new Date());
/* 3393 */       String version = String.valueOf(cal.getTimeInMillis());
/* 3394 */       loginfo.setProcessType(processType);
/* 3395 */       loginfo.setNumber(String.valueOf(cal.getTimeInMillis()) + "." + number);
/*      */       
/* 3397 */       loginfo.setSimpleName(number);
/* 3398 */       loginfo.setDateBaseType(baseType);
/* 3399 */       loginfo.setVersion(version);
/* 3400 */       loginfo.setUpdateDate(new Date());
/* 3401 */       loginfo.setStatus(true);
/* 3402 */       loginfo.setDescription(miaoshu);
/* 3403 */       DateBaseLogFactory.getLocalInstance(ctx).save((CoreBaseInfo)loginfo);
/* 3404 */     } catch (EASBizException e) {
/*      */       
/* 3406 */       e.printStackTrace();
/* 3407 */     } catch (BOSException e) {
/*      */       
/* 3409 */       e.printStackTrace();
/*      */     } 
/*      */   }  
/*      */   public void updateMidTable(Context ctx, DateBaseProcessType processType, DateBasetype baseType, String dataBase, String number, String name, String id) throws BOSException {
/* 3420 */     if (VerifyUtil.notNull(dataBase) && VerifyUtil.notNull(number) && 
/* 3421 */       VerifyUtil.notNull(name) && VerifyUtil.notNull(id)) {
/* 3422 */       StringBuffer updateSql = new StringBuffer("UPDATE ");
/* 3423 */       if (baseType == DateBasetype.FreeItem) {
/* 3424 */         updateSql.append(" EAS_FreeItem_MIDTABLE set FName ='");
/* 3425 */         updateSql.append(name);
/* 3426 */         updateSql.append("',fSign=0");
/* 3427 */         updateSql.append(",FupdateType=1");
/*      */ 
/*      */         
/* 3434 */         updateSql.append(",FupdateTime = SYSDATE");
/* 3435 */       } else if (baseType == DateBasetype.PayType) {
/* 3436 */         updateSql.append(" EAS_PAYTYPE_MIDTABLE set FName ='");
/* 3437 */         updateSql.append(name);
/* 3438 */         updateSql.append("',fSign=0");
/* 3439 */         updateSql.append(",FupdateType=1");
/*      */          
/* 3448 */         updateSql.append(",FupdateTime = SYSDATE");
/*      */       }
/* 3450 */       else if (baseType == DateBasetype.position) {
/* 3451 */         updateSql.append(" EAS_POST_MIDTABLE set FName ='");
/* 3452 */         updateSql.append(name);
/* 3453 */         updateSql.append("', fSign_0 = 0 , fSign_1 = 0");
/* 3454 */         updateSql.append(",FupdateType=1");       
/*      */       }
/* 3464 */       else if (baseType == DateBasetype.Person) {
/* 3465 */         updateSql.append(" EAS_Person_MIDTABLE set FName ='");
/* 3466 */         updateSql.append(name);
/* 3467 */         updateSql.append("'");
/*      */         
/* 3469 */         updateSql.append(",FupdateType=1, fSign_0 = 0 , fSign_1 = 0");     
/*      */       }
/* 3479 */       else if (baseType == DateBasetype.Supplier2HIS) {
/* 3480 */         updateSql.append(" EAS_Supplier_MIDTABLE set FName ='");
/* 3481 */         updateSql.append(name);
/* 3482 */         updateSql.append("',fSign=0");
/* 3483 */         updateSql.append(",FupdateType=1");         
/* 3492 */         updateSql.append(",FupdateTime = SYSDATE");
/*      */       }
/* 3494 */       else if (baseType == DateBasetype.orgUnit) {
/* 3495 */         updateSql.append(" EAS_ORG_MIDTABLE set FName ='");
/* 3496 */         updateSql.append(name);
/* 3497 */         updateSql.append("', fSign_0 = 0 , fSign_1 = 0");
/* 3498 */         updateSql.append(",FupdateType=1");       
/*      */       }
/* 3508 */       else if (baseType == DateBasetype.PayTypeTree) {
/* 3509 */         updateSql.append(" EAS_PAYTYPETREE_MIDTABLE set FName ='");
/* 3510 */         updateSql.append(name);
/* 3511 */         updateSql.append("',fSign =0");
/* 3512 */         updateSql.append(",FupdateType=1");      
/*      */       }
/* 3522 */       else if (baseType == DateBasetype.Material) {
/* 3523 */         updateSql.append(" LOLKK_ITEMS set FName ='");
/* 3524 */         updateSql.append(name);
/* 3525 */         updateSql.append("',fSign =0");
/* 3526 */         updateSql.append(",FupdateType=1");     
/*      */       }
/* 3536 */       else if (baseType == DateBasetype.ExpenseTypeToMid) {
/* 3537 */         updateSql.append(" EAS_PAYTYPE_OA_MIDTABLE set FName ='");
/* 3538 */         updateSql.append(name);
/* 3539 */         updateSql.append("',fSign =0");
/* 3540 */         updateSql.append(",FupdateType=1");      
/*      */       }
/* 3550 */       else if (baseType == DateBasetype.SupplyinfoToMid) {
/* 3551 */         BigDecimal price = new BigDecimal(name);
/* 3552 */         updateSql.append(" EAS_ORG_SupplyinfoMid set PRICE = ");
/* 3553 */         updateSql.append(name);
/* 3554 */         updateSql.append(" ,fSign =0");
/* 3555 */         updateSql.append(",FupdateTime = SYSDATE");
/*      */       } 
/* 3557 */       updateSql.append(" where fid='").append(id).append("'");
/* 3558 */       EAISynTemplate.execute(ctx, dataBase, updateSql.toString());
/*      */     } 
/*      */   }  
/*      */   private String updateMidTable(Context ctx, DateBaseProcessType processType, DateBasetype baseType, String dataBase, String number, String name, String id, String tableName) throws BOSException {
/* 3580 */     if (VerifyUtil.notNull(dataBase) && VerifyUtil.notNull(number) && 
/* 3581 */       VerifyUtil.notNull(name) && VerifyUtil.notNull(id)) {
/* 3582 */       StringBuffer updateSql = new StringBuffer("UPDATE " + tableName);
/* 3583 */       if (name.contains("'")) {
/* 3584 */         name = name.replace("'", "''");
/*      */       }
/*      */       
/* 3587 */       if (baseType == DateBasetype.Material) {
/* 3588 */         updateSql.append("  set FName ='");
/* 3589 */         updateSql.append(name);
/* 3590 */         updateSql.append("',fSign =0");
/* 3591 */         updateSql.append(",FupdateType=1");
/*      */       }        
/* 3602 */       updateSql.append(" where fid='").append(id).append("'");
/*      */ 
/*      */       
/* 3605 */       return updateSql.toString();
/*      */     } 
/* 3607 */     return "";
/*      */   }
/*      */ 
/*      */   
/*      */   private HashMap<String, String> getComMap(Context ctx, String dataBase, String cityid) throws BOSException {
/* 3612 */     HashMap<String, String> comMap = new HashMap<String, String>();
/*      */ 
/*      */     
/* 3615 */     String queryStr = " select CITYID,COMID  FROM EAS_CITY_COMPANY ";
/* 3616 */     if (cityid != null && !cityid.equals("")) {
/* 3617 */       queryStr = String.valueOf(queryStr) + " where cityid = '" + cityid + "'";
/*      */     }
/* 3619 */     IRowSet rowsCityID = DbUtil.executeQuery(ctx, queryStr);
/*      */     try {
/* 3621 */       while (rowsCityID.next()) {
/* 3622 */         String cityID = rowsCityID.getString("CITYID").toString();
/* 3623 */         String comid = rowsCityID.getString("COMID").toString();
/* 3624 */         comMap.put(cityID, comid);
/*      */       } 
/* 3626 */     } catch (SQLException e) {
/*      */       
/* 3628 */       e.printStackTrace();
/*      */     } 
/* 3630 */     return comMap;
/*      */   }
/*      */   
/*      */   public String updateMidStatus(Context ctx, DateBaseProcessType processType, DateBasetype baseType, String dataBase, String number, String name, String id, String tableName) throws BOSException {
/* 3641 */     if (VerifyUtil.notNull(dataBase) && VerifyUtil.notNull(number) && VerifyUtil.notNull(name) && VerifyUtil.notNull(id)) {
/* 3642 */       StringBuffer updateSql = new StringBuffer("UPDATE  " + tableName);
/*      */       
/* 3644 */       if (baseType == DateBasetype.FreeItem) {
/* 3645 */         if (processType == DateBaseProcessType.ENABLE) {
/* 3646 */           updateSql
/* 3647 */             .append("  set  FUPDATETYPE = 1  , fSign = 0 where fid='" + 
/* 3648 */               id + "'");
/* 3649 */         } else if (processType == DateBaseProcessType.DisAble) {
/* 3650 */           updateSql
/* 3651 */             .append("  set  FUPDATETYPE = 2  , fSign = 0 where fid='" + 
/* 3652 */               id + "'");
/*      */         }
/*      */       
/* 3655 */       } else if (baseType == DateBasetype.PayType) {
/* 3656 */         if (processType == DateBaseProcessType.ENABLE) {
/* 3657 */           updateSql
/* 3658 */             .append("  set FISSTART = 0 , FUPDATETYPE = 1  , fSign = 0 where fid='" + 
/* 3659 */               id + "'");
/* 3660 */         } else if (processType == DateBaseProcessType.DisAble) {
/* 3661 */           updateSql
/* 3662 */             .append("  set  FISSTART = 1,  FUPDATETYPE = 2  , fSign = 0 where fid='" + 
/* 3663 */               id + "'");
/*      */         } 
/* 3665 */       } else if (baseType == DateBasetype.position) {
/* 3666 */         if (processType == DateBaseProcessType.ENABLE) {
/* 3667 */           updateSql
/* 3668 */             .append("  set  FUPDATETYPE = 1 , fSign_0 = 0 , fSign_1 = 0 where fid='" + 
/* 3669 */               id + "'");
/* 3670 */         } else if (processType == DateBaseProcessType.DisAble) {
/* 3671 */           updateSql
/* 3672 */             .append("  set  FUPDATETYPE = 2  , fSign_0 = 0 , fSign_1 = 0 where fid='" + 
/* 3673 */               id + "'");
/*      */         }
/*      */       
/* 3676 */       } else if (baseType == DateBasetype.Person) {
/* 3677 */         if (processType == DateBaseProcessType.ENABLE) {
/* 3678 */           updateSql
/* 3679 */             .append("  set FEMPLOYEETYPENAME = '启用' , FEMPLOYEETYPENUMBER = 0, FUPDATETYPE = 1  , fSign_0 = 0 , fSign_1 = 0 where fid='" + 
/* 3680 */               id + "'");
/* 3681 */         } else if (processType == DateBaseProcessType.DisAble) {
/* 3682 */           updateSql
/* 3683 */             .append("  set FEMPLOYEETYPENAME = '禁用' , FEMPLOYEETYPENUMBER = 0, FUPDATETYPE = 2  , fSign_0 = 0 , fSign_1 = 0 where fid='" + 
/* 3684 */               id + "'");
/*      */         }
/*      */       
/* 3687 */       } else if (baseType == DateBasetype.Supplier2HIS) {
/* 3688 */         if (processType == DateBaseProcessType.ENABLE) {
/* 3689 */           updateSql
/* 3690 */             .append("  set   FUPDATETYPE = 1  , FSIGN = 0 where fid='" + 
/* 3691 */               id + "'");
/* 3692 */         } else if (processType == DateBaseProcessType.DisAble) {
/* 3693 */           updateSql
/* 3694 */             .append("  set FUPDATETYPE = 2  , FSIGN = 0 where fid='" + 
/* 3695 */               id + "'");
/*      */         } 
/* 3697 */       } else if (baseType == DateBasetype.orgUnit) {
/* 3698 */         if (processType == DateBaseProcessType.ENABLE) {
/* 3699 */           updateSql
/* 3700 */             .append("  set FISOUSEALUP = 0, FUPDATETYPE = 1  , fSign_0 = 0 , fSign_1 = 0 where fid='" + 
/* 3701 */               id + "'");
/* 3702 */         } else if (processType == DateBaseProcessType.DisAble) {
/* 3703 */           updateSql
/* 3704 */             .append("  set FISOUSEALUP = 1, FUPDATETYPE = 2  , fSign_0 = 0 , fSign_1 = 0 where fid='" + 
/* 3705 */               id + "'");
/*      */         }
/*      */       
/* 3708 */       } else if (baseType == DateBasetype.PayTypeTree) {
/* 3709 */         if (processType == DateBaseProcessType.ENABLE) {
/* 3710 */           updateSql
/* 3711 */             .append("  set  FUPDATETYPE = 1  , fSign = 0 where fid='" + 
/* 3712 */               id + "'");
/* 3713 */         } else if (processType == DateBaseProcessType.DisAble) {
/* 3714 */           updateSql
/* 3715 */             .append("  set    FUPDATETYPE = 2  , fSign = 0 where fid='" + 
/* 3716 */               id + "'");
/*      */         } 
/*      */       } else {
/* 3719 */         if (baseType == DateBasetype.Material) {
/* 3720 */           if (processType == DateBaseProcessType.ENABLE) {
/* 3721 */             updateSql
/* 3722 */               .append("  set  FSTATUS = 1,  FUPDATETYPE = 1  , fSign = 0 where fid='" + 
/* 3723 */                 id + "'");
/* 3724 */           } else if (processType == DateBaseProcessType.DisAble) {
/* 3725 */             updateSql
/* 3726 */               .append("  set  FSTATUS = 2,  FUPDATETYPE = 2  , fSign = 0 where fid='" + 
/* 3727 */                 id + "'");
/* 3728 */           } else if (processType == DateBaseProcessType.SealUp) {
/* 3729 */             updateSql
/* 3730 */               .append("  set  FSTATUS = 0,  FUPDATETYPE = 1  , fSign = 0 where fid='" + 
/* 3731 */                 id + "'");
/*      */           } 
/*      */           
/* 3734 */           return updateSql.toString();
/* 3735 */         }  if (baseType == DateBasetype.ExpenseTypeToMid) {
/* 3736 */           if (processType == DateBaseProcessType.ENABLE) {
/* 3737 */             updateSql
/* 3738 */               .append("  set  FSTATUS = 1,  FUPDATETYPE = 1  , fSign = 0 where fid='" + 
/* 3739 */                 id + "'");
/* 3740 */           } else if (processType == DateBaseProcessType.DisAble) {
/* 3741 */             updateSql
/* 3742 */               .append("  set  FSTATUS = 0,  FUPDATETYPE = 2  , fSign = 0 where fid='" + 
/* 3743 */                 id + "'");
/*      */           }
/*      */         
/* 3746 */         } else if (baseType == DateBasetype.SupplyinfoToMid) {
/* 3747 */           if (processType == DateBaseProcessType.ENABLE) {
/*      */             
/* 3749 */             String overdueStatus = " /*dialect*/ update eas_org_supplyinfomid set  Fstatus = 0  , fsign=0, fupdatetime = sysdate  where fid='" + number + "_" + name + "' ";
/* 3750 */             EAISynTemplate.execute(ctx, dataBase, overdueStatus);
/*      */             
/* 3752 */             updateSql.append("  set  FSTATUS = 1,  fSign = 0 ,FUPDATETIME =  TO_DATE(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') where fid='" + 
/* 3753 */                 id + "'");
/* 3754 */           } else if (processType == DateBaseProcessType.DisAble) {
/*      */             
/* 3756 */             String selectSuppSql = " /*dialect*/ select fid from eas_org_supplyinfomid  where fid != '" + id + "' and  supplierzhukey = 0  and    csfid = '" + name + "' and FMaterialNumber ='" + number + "' and  FSTATUS = 1 and fenddate is not null  and  fenddate > sysdate";
/* 3757 */             List<Map<String, Object>> rets = EAISynTemplate.query(ctx, dataBase, selectSuppSql);
/* 3758 */             if (rets.size() > 0) {
/* 3759 */               String overdueStatus = " /*dialect*/ update eas_org_supplyinfomid set  Fstatus = 0  , fsign=0, fupdatetime = sysdate  where fid='" + number + "_" + name + "' ";
/* 3760 */               EAISynTemplate.execute(ctx, dataBase, overdueStatus);
/*      */             } else {
/* 3762 */               String overdueStatus = " /*dialect*/ update eas_org_supplyinfomid set  Fstatus = 1  , fsign=0, fupdatetime = sysdate  where fid='" + number + "_" + name + "' ";
/* 3763 */               EAISynTemplate.execute(ctx, dataBase, overdueStatus);
/*      */             } 
/*      */             
/* 3766 */             updateSql.append("  set  FSTATUS = 0,  fSign = 0 ,FUPDATETIME =  TO_DATE(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')  where fid='" + 
/* 3767 */                 id + "'");
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/* 3772 */       EAISynTemplate.execute(ctx, dataBase, updateSql.toString());
/*      */     } 
/*      */     
/* 3775 */     return "";
/*      */   }   
/*      */   private void updateFOBJECT(Context ctx, DateBaseProcessType processType, DateBasetype baseType, String dataBase, String tableName, String number, String name, String object, String fid, int i) throws BOSException {
/* 3798 */     StringBuffer updateSql = new StringBuffer("UPDATE  " + tableName);
/* 3799 */     if (i == 0) {
/* 3800 */       updateSql
/* 3801 */         .append("  set FISCOMPANYORGUNIT = '" + 
/* 3802 */           object + 
/* 3803 */           "', FUPDATETYPE = 1  , fSign_0 = 0 , fSign_1 = 0 where fid='" + 
/* 3804 */           fid + "'");
/*      */     }
/* 3806 */     else if (i == 1) {
/* 3807 */       updateSql
/* 3808 */         .append("  set FOBJECT = '" + 
/* 3809 */           object + 
/* 3810 */           "', FUPDATETYPE = 1  , fSign_0 = 0 , fSign_1 = 0 where fid='" + 
/* 3811 */           fid + "'");
/*      */     } 
/* 3813 */     EAISynTemplate.execute(ctx, dataBase, updateSql.toString());
/*      */   }   
/*      */   private void updatePerson(Context ctx, DateBaseProcessType processType, DateBasetype baseType, String dataBase, String tableName, String number, String name, String object, String fid, int i) throws BOSException {
/* 3837 */     StringBuffer updateSql = new StringBuffer("UPDATE  " + tableName);
/* 3838 */     if (i == 0) {
/* 3839 */       updateSql
/* 3840 */         .append("  set FORGTID = '" + 
/* 3841 */           object + 
/* 3842 */           "', FUPDATETYPE = 1  , fSign_0 = 0 , fSign_1 = 0 where fid='" + 
/* 3843 */           fid + "'");
/*      */     }
/* 3845 */     else if (i == 1) {
/* 3846 */       updateSql
/* 3847 */         .append("  set FDEPTID = '" + 
/* 3848 */           object + 
/* 3849 */           "', FUPDATETYPE = 1  , fSign_0 = 0 , fSign_1 = 0 where fid='" + 
/* 3850 */           fid + "'");
/* 3851 */     } else if (i == 2) {
/* 3852 */       updateSql
/* 3853 */         .append("  set BANKNUM = '" + 
/* 3854 */           object + 
/* 3855 */           "', FUPDATETYPE = 1  , fSign_0 = 0 , fSign_1 = 0 where fid='" + 
/* 3856 */           fid + "'");
/* 3857 */     } else if (i == 3) {
/* 3858 */       updateSql
/* 3859 */         .append("  set BANK = '" + 
/* 3860 */           object + 
/* 3861 */           "', FUPDATETYPE = 1  , fSign_0 = 0 , fSign_1 = 0 where fid='" + 
/* 3862 */           fid + "'");
/* 3863 */     } else if (i == 4) {
/* 3864 */       updateSql
/* 3865 */         .append("  set FPositionNumber = '" + 
/* 3866 */           number + "',FPositionName = '" + name + "' , FPositionID='" + object + "' , FUPDATETYPE = 1  , fSign_0 = 0 , fSign_1 = 0 where fid='" + 
/* 3867 */           fid + "'");
/* 3868 */     } else if (i == 5) {
/* 3869 */       updateSql
/* 3870 */         .append("  set FEnterDate = '" + 
/* 3871 */           object + 
/* 3872 */           "', FUPDATETYPE = 1  , fSign_0 = 0 , fSign_1 = 0 where fid='" + 
/* 3873 */           fid + "'");
/* 3874 */     } else if (i == 7) {
/* 3875 */       updateSql
/* 3876 */         .append("  set FPlanFormalDate = '" + 
/* 3877 */           object + 
/* 3878 */           "', FUPDATETYPE = 1  , fSign_0 = 0 , fSign_1 = 0 where fid='" + 
/* 3879 */           fid + "'");
/* 3880 */     } else if (i == 6) {
/* 3881 */       updateSql
/* 3882 */         .append("  set FJobLevelNumber = '" + 
/* 3883 */           number + "',FJobLevelName = '" + name + "' , FJobLevelID='" + object + "' , FUPDATETYPE = 1  , fSign_0 = 0 , fSign_1 = 0 where fid='" + 
/* 3884 */           fid + "'");
/* 3885 */     } else if (i == 8) {
/* 3886 */       updateSql
/* 3887 */         .append("  set FJobCategoryNumber = '" + 
/* 3888 */           number + "',FJobCategoryName = '" + name + "' , FJobCategoryID='" + object + "' , FUPDATETYPE = 1  , fSign_0 = 0 , fSign_1 = 0 where fid='" + 
/* 3889 */           fid + "'");
/*      */     } 
/* 3891 */     EAISynTemplate.execute(ctx, dataBase, updateSql.toString());
/*      */   }   
/*      */   private void updatePerson(Context ctx, DateBaseProcessType processType, DateBasetype baseType, String dataBase, String tableName, String number, String name, String object, String fid, int i, String orgNumber, String orgName) throws BOSException {
/* 3900 */     StringBuffer updateSql = new StringBuffer("UPDATE  " + tableName);
/* 3901 */     if (i == 0) {
/* 3902 */       updateSql
/* 3903 */         .append("  set FORGTID = '" + object + 
/* 3904 */           "',FORGNUMBER = '" + orgNumber + "', FORGNAME = '" + orgName + "' , FUPDATETYPE = 1  , fSign_0 = 0 , fSign_1 = 0 where fid='" + 
/* 3905 */           fid + "'");
/*      */     }
/*      */     
/* 3908 */     EAISynTemplate.execute(ctx, dataBase, updateSql.toString());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void SyncRuZhi(Context ctx, String dataBase) throws BOSException {
/* 3914 */     List<String> sqls = new ArrayList<String>();
/*      */     
/* 3916 */     for (int i = 1; i < 4; i++) {
/* 3917 */       StringBuffer sql = new StringBuffer();
/* 3918 */       sql = getSqlRuORLi(sql, "入职", i);
/* 3919 */       IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
/*      */       try {
/* 3921 */         while (rows.next()) {
/* 3922 */           String sqlStr = insertOneHRMidTable(ctx, DateBaseProcessType.AddNew, DateBasetype.HR_RUZHI, dataBase, "EAS_HR_RUZHI", rows);
/* 3923 */           sqls.add(sqlStr);
/*      */         } 
/* 3925 */       } catch (SQLException e) {
/* 3926 */         e.printStackTrace();
/*      */       } 
/*      */     } 
/*      */     
/* 3930 */     if (sqls.size() > 0) {
/* 3931 */       EAISynTemplate.executeBatch(ctx, dataBase, sqls);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void SyncLiZhi(Context ctx, String dataBase) throws BOSException {
/* 3937 */     List<String> sqls = new ArrayList<String>();
/*      */     
/* 3939 */     for (int i = 1; i < 4; i++) {
/* 3940 */       StringBuffer sql = new StringBuffer();
/* 3941 */       sql = getSqlRuORLi(sql, "离职", i);
/* 3942 */       IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
/*      */       try {
/* 3944 */         while (rows.next()) {
/* 3945 */           String sqlStr = insertOneHRMidTable(ctx, DateBaseProcessType.AddNew, DateBasetype.HR_LIZHI, dataBase, "EAS_HR_LIZHI", rows);
/* 3946 */           sqls.add(sqlStr);
/*      */         } 
/* 3948 */       } catch (SQLException e) {
/*      */         
/* 3950 */         e.printStackTrace();
/*      */       } 
/*      */     } 
/*      */     
/* 3954 */     if (sqls.size() > 0) {
/* 3955 */       EAISynTemplate.executeBatch(ctx, dataBase, sqls);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void SyncYiDong(Context ctx, String dataBase) throws BOSException {
/* 3962 */     List<String> sqls = new ArrayList<String>();
/*      */     
/* 3964 */     for (int i = 0; i < 5; i++) {
/* 3965 */       for (int j = 1; j < 4; j++) {
/* 3966 */         StringBuffer sql = new StringBuffer();
/* 3967 */         sql = getSqlYingDong(sql, i, j);
/* 3968 */         System.out.print(sql);
/* 3969 */         IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
/*      */         try {
/* 3971 */           while (rows.next()) {
/* 3972 */             String sqlStr = insertOneHRMidTable(ctx, DateBaseProcessType.AddNew, DateBasetype.HR_YIDONG, dataBase, "EAS_HR_YIDONG", rows);
/* 3973 */             sqls.add(sqlStr);
/*      */           } 
/* 3975 */           if (i == 1 && j == 3) {
/* 3976 */             StringBuffer sql1 = new StringBuffer();
/* 3977 */             sql1 = getSqlCity();
/* 3978 */             IRowSet rows1 = DbUtil.executeQuery(ctx, sql1.toString());
/* 3979 */             while (rows1.next()) {
/* 3980 */               String sqlStr = insertOneHRMidTable(ctx, DateBaseProcessType.AddNew, DateBasetype.HR_YIDONG, dataBase, "EAS_HR_YIDONG", rows);
/* 3981 */               sqls.add(sqlStr);
/*      */             } 
/*      */           } 
/* 3984 */         } catch (SQLException e) {
/*      */           
/* 3986 */           e.printStackTrace();
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 3992 */     if (sqls.size() > 0) {
/* 3993 */       EAISynTemplate.executeBatch(ctx, dataBase, sqls);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private StringBuffer getSqlRuORLi(StringBuffer sql, String type, int count) throws BOSException {
/* 4001 */     if ("入职".equals(type)) {
/* 4002 */       if (count == 1) {
/* 4003 */         sql.append("/*dialect*/ SELECT distinct admin5.fnumber as groupNumber ,admin4.fnumber as daquNumber ,admin3.fnumber as cityNumber,admin2.fnumber as companyNumber,admin1.fnumber as deptNumber ,")
/* 4004 */           .append(" person.FNumber as personnumber,person.fname_l2 as fname ,position.Fnumber as positionNumber , position.fname_l2 as positionName ,  nvl( bdpost.fnumber  ,' ') as TechnicalPost,   ")
/* 4005 */           .append(" positionName.fnumber as zhiwunumber ,nvl( jobGrade.fnumber,' ') as jobGradeNumber  , ' ' as fentanTypeNumber  , ' ' as guanbaoTypeNumber  , ' ' as empTypeNumber , to_char( empOrgRelation.FEFFDT ,'yyyy-mm-dd hh24:mi:ss') as empDate,  ")
/* 4006 */           .append(" nvl(  to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss') ,nvl(to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss'),' ') ) as empzhuanzhengDate  , to_char(empOrgRelation.Fleffdt,'yyyy-mm-dd hh24:mi:ss') as empleftdate  ,nvl( employeeModle.fnumber,' ') as empModelNumber  ,(case employeeContract.FNewState when 0 then  '01'  when 1 then  '01'  when 2 then  '02' else ' ' end ) as hetongtype ,   ")
/* 4007 */           .append(" nvl( to_char(employeeContract.FEffectDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  EffectDate , nvl( to_char(employeeContract.FEndDate,'yyyy-mm-dd hh24:mi:ss'),' ') as EndDate , nvl(person.FIDCardNO,' ') as  IDCardNO,nvl(  person.FHjAddress,' ') as HjAddress ,   ")
/* 4008 */           .append("  (case person.FGender when 1 then  'F'  when 2 then  'M'  ELSE  ' '  end )  as Gender , nvl(  to_char( person.FBirthday,'yyyy-mm-dd hh24:mi:ss'),' ') as Birthday ,nvl( hrWed.FNAME_l2,' ') as wefName ,nvl( hrFolk.FNAME_l2,' ') as folkName ,nvl(hrPolitical.fname_l2,' ') as PoliticalName ,  ")
/* 4009 */           .append(" nvl(personContactMethod.FADDRESS,' ')  as faddress , nvl( personContactMethod.FEmail,' ') as Email , nvl( personContactMethod.FMobile ,' ') as Mobile , nvl( personContactMethod.FLinkName ,' ') as LinkName ,  nvl( personContactMethod.FLinkTelNum ,' ') as LinkTelNum ,")
/* 4010 */           .append(" nvl( hrDiploma.fname_l2 ,' ') as DiplomaName , nvl(personDegree.FGraduateSchool,' ') as  GraduateSchool ,  nvl(personDegree.FSpecialty,' ') as  Specialty ,  nvl(to_char(personDegree.FGraduateDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  GraduateDate ,  ")
/* 4011 */           .append(" nvl(person.FIdNum,' ') as idNum ,  nvl(person.FRTX,' ')  as RTX , nvl( employeeContract.FCONTRACTNO ,' ') as CONTRACTNO ,  nvl( to_char(personCertifyCompetency.FObtainDate,'yyyy-mm-dd hh24:mi:ss'),'') as ObtainDate , nvl(to_char(personWorkExp.FBeginDate ,'yyyy-mm-dd hh24:mi:ss'),'') as firstWorkTime,  ")
/* 4012 */           .append(" ' ' as insuredCityNumber , regpermresidence.fname_l2 as  hujiName , ' ' as  gongjijinNum , '' as Remarks ")
/* 4013 */           .append(" ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign ")
/* 4014 */           .append(" FROM   T_BD_Person  person    ")
/* 4015 */           .append(" left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid ")
/* 4016 */           .append(" LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid ")
/* 4017 */           .append(" left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID  ")
/* 4018 */           .append(" left join  T_ORG_Admin  admin1  on empOrgRelation.fadminorgid  = admin1.fid ")
/* 4019 */           .append(" left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid ")
/* 4020 */           .append(" left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid ")
/* 4021 */           .append(" left join  T_ORG_Admin  admin4  on admin3.FPARENTID = admin4.fid  ")
/* 4022 */           .append(" left join  T_ORG_Admin  admin5  on admin4.FPARENTID = admin5.fid   ")
/* 4023 */           .append(" left join  T_ORG_Position  position   on  empOrgRelation.FPositionID = position.fid  ")
/* 4024 */           .append(" left join  T_ORG_Job  positionName   on  position.FJobID =positionName.fid  ")
/* 4025 */           .append(" left join  T_HR_JobGrade  jobGrade   on  empOrgRelation.FJobGradeID =jobGrade.fid ")
/* 4026 */           .append(" left join  T_HR_EmpLaborRelation  empLaborRelation   on  empLaborRelation.fpersonid =person.fid  ")
/* 4027 */           .append(" left join  T_HR_EmpPostExperienceHis  empPostExperienceHis   on  empPostExperienceHis.fpersonid =person.fid ")
/* 4028 */           .append(" left join  T_BD_EmployeeModle  employeeModle   on  empLaborRelation.FEmployeeModleID =employeeModle.fid ")
/* 4029 */           .append(" left join  T_HR_EmployeeContract employeeContract   on  employeeContract.FEmployeeID =person.fid ")
/* 4030 */           .append(" left join  T_BD_HRWed hrWed   on  hrWed.fid =person.FWedID ")
/* 4031 */           .append(" left join  T_BD_HRFolk hrFolk   on  hrFolk.fid =person.FFolkID ")
/* 4032 */           .append(" left join  T_BD_HRPolitical  hrPolitical   on  hrPolitical.fid =person.FPoliticalFaceID ")
/* 4033 */           .append(" left join  T_HR_PersonContactMethod  personContactMethod   on  person.fid =personContactMethod.fpersonid ")
/* 4034 */           .append(" left join  T_HR_PersonDegree personDegree   on  person.fid = personDegree.fpersonid  ")
/* 4035 */           .append(" left join  T_BD_HRDiploma  hrDiploma   on  hrDiploma.fid = personDegree.FDIPLOMA  ")
/* 4036 */           .append(" left join  T_HR_PersonCertifyCompetency  personCertifyCompetency   on  person.fid = personCertifyCompetency.FPERSONID   ")
/* 4037 */           .append(" left join  T_HR_PersonWorkExp  personWorkExp   on  person.fid = personWorkExp.FPERSONID   ")
/* 4038 */           .append(" left join  T_BD_Regpermresidence  regpermresidence   on  regpermresidence.fid = person.FRegresidenceID  ")
/* 4039 */           .append(" left join  T_HR_PersonTechPost  psot    on  psot.fpersonid =person.fid  and  FISHIGHTECHNICAL = 1 ")
/* 4040 */           .append(" left join T_HR_BDTechnicalPost  bdpost  on  psot.FTECHNICALPOSTID  =bdpost.fid  ")
/* 4041 */           .append(" where   1=1  ")
/*      */ 
/*      */           
/* 4044 */           .append(" and  ( affairActionReason.fnumber = '0101'  )    and  ( personDegree.fishighest = 1 or personDegree.fishighest  is null  )  ")
/* 4045 */           .append(" and  ( employeeContract.FNewState !=3 or  employeeContract.FNewState is null ) and  admin1.FLEVEL  = 5  order by person.fnumber    ");
/*      */       
/*      */       }
/* 4048 */       else if (count == 2) {
/* 4049 */         sql.append(" /*dialect*/ SELECT distinct   admin4.fnumber as groupNumber ,admin3.fnumber as daquNumber , ' ' as cityNumber,admin2.fnumber as companyNumber,admin1.fnumber as deptNumber ,")
/* 4050 */           .append(" person.FNumber as personnumber,person.fname_l2 as fname ,position.Fnumber as positionNumber , position.fname_l2 as positionName ,  nvl( bdpost.fnumber  ,' ') as TechnicalPost,   ")
/* 4051 */           .append(" positionName.fnumber as zhiwunumber ,nvl( jobGrade.fnumber,' ') as jobGradeNumber  , ' ' as fentanTypeNumber  , ' ' as guanbaoTypeNumber  , ' ' as empTypeNumber , to_char( empOrgRelation.FEFFDT ,'yyyy-mm-dd hh24:mi:ss') as empDate,  ")
/* 4052 */           .append(" nvl(  to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss') ,nvl(to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss'),' ') ) as empzhuanzhengDate  , to_char(empOrgRelation.Fleffdt,'yyyy-mm-dd hh24:mi:ss') as empleftdate  ,nvl( employeeModle.fnumber,' ') as empModelNumber  ,(case employeeContract.FNewState when 0 then  '01'  when 1 then  '01'  when 2 then  '02' else ' ' end ) as hetongtype ,   ")
/* 4053 */           .append(" nvl( to_char(employeeContract.FEffectDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  EffectDate , nvl( to_char(employeeContract.FEndDate,'yyyy-mm-dd hh24:mi:ss'),' ') as EndDate , nvl(person.FIDCardNO,' ') as  IDCardNO,nvl(  person.FHjAddress,' ') as HjAddress ,   ")
/* 4054 */           .append("  (case person.FGender when 1 then  'F'  when 2 then  'M'  ELSE  ' '  end )  as Gender , nvl(  to_char( person.FBirthday,'yyyy-mm-dd hh24:mi:ss'),' ') as Birthday ,nvl( hrWed.FNAME_l2,' ') as wefName ,nvl( hrFolk.FNAME_l2,' ') as folkName ,nvl(hrPolitical.fname_l2,' ') as PoliticalName ,  ")
/* 4055 */           .append(" nvl(personContactMethod.FADDRESS,' ')  as faddress , nvl( personContactMethod.FEmail,' ') as Email , nvl( personContactMethod.FMobile ,' ') as Mobile , nvl( personContactMethod.FLinkName ,' ') as LinkName ,  nvl( personContactMethod.FLinkTelNum ,' ') as LinkTelNum , ")
/* 4056 */           .append(" nvl( hrDiploma.fname_l2 ,' ') as DiplomaName , nvl(personDegree.FGraduateSchool,' ') as  GraduateSchool ,  nvl(personDegree.FSpecialty,' ') as  Specialty ,  nvl(to_char(personDegree.FGraduateDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  GraduateDate ,  ")
/* 4057 */           .append(" nvl(person.FIdNum,' ') as idNum ,  nvl(person.FRTX,' ')  as RTX , nvl( employeeContract.FCONTRACTNO ,' ') as CONTRACTNO , nvl( to_char(personCertifyCompetency.FObtainDate,'yyyy-mm-dd hh24:mi:ss'),' ') as ObtainDate , nvl(to_char(personWorkExp.FBeginDate ,'yyyy-mm-dd hh24:mi:ss'),' ') as firstWorkTime,  ")
/* 4058 */           .append(" ' ' as insuredCityNumber , regpermresidence.fname_l2 as  hujiName , ' ' as  gongjijinNum , '' as Remarks ")
/* 4059 */           .append(" ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign ")
/* 4060 */           .append(" FROM   T_BD_Person  person    ")
/* 4061 */           .append(" left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid ")
/* 4062 */           .append(" LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid ")
/* 4063 */           .append(" left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID  ")
/* 4064 */           .append(" left join  T_ORG_Admin  admin1  on empOrgRelation.fadminorgid  = admin1.fid ")
/* 4065 */           .append(" left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid ")
/* 4066 */           .append(" left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid ")
/* 4067 */           .append(" left join  T_ORG_Admin  admin4  on admin3.FPARENTID = admin4.fid  ")
/* 4068 */           .append(" left join  T_ORG_Position  position   on  empOrgRelation.FPositionID = position.fid  ")
/* 4069 */           .append(" left join  T_ORG_Job  positionName   on  position.FJobID =positionName.fid  ")
/* 4070 */           .append(" left join  T_HR_JobGrade  jobGrade   on  empOrgRelation.FJobGradeID =jobGrade.fid ")
/* 4071 */           .append(" left join  T_HR_EmpLaborRelation  empLaborRelation   on  empLaborRelation.fpersonid =person.fid  ")
/* 4072 */           .append(" left join  T_HR_EmpPostExperienceHis  empPostExperienceHis   on  empPostExperienceHis.fpersonid =person.fid ")
/* 4073 */           .append(" left join  T_BD_EmployeeModle  employeeModle   on  empLaborRelation.FEmployeeModleID =employeeModle.fid ")
/* 4074 */           .append(" left join  T_HR_EmployeeContract employeeContract   on  employeeContract.FEmployeeID =person.fid ")
/* 4075 */           .append(" left join  T_BD_HRWed hrWed   on  hrWed.fid =person.FWedID ")
/* 4076 */           .append(" left join  T_BD_HRFolk hrFolk   on  hrFolk.fid =person.FFolkID ")
/* 4077 */           .append(" left join  T_BD_HRPolitical  hrPolitical   on  hrPolitical.fid =person.FPoliticalFaceID ")
/* 4078 */           .append(" left join  T_HR_PersonContactMethod  personContactMethod   on  person.fid =personContactMethod.fpersonid ")
/* 4079 */           .append(" left join  T_HR_PersonDegree personDegree   on  person.fid = personDegree.fpersonid  ")
/* 4080 */           .append(" left join  T_BD_HRDiploma  hrDiploma   on  hrDiploma.fid = personDegree.FDIPLOMA  ")
/* 4081 */           .append(" left join  T_HR_PersonCertifyCompetency  personCertifyCompetency   on  person.fid = personCertifyCompetency.FPERSONID   ")
/* 4082 */           .append(" left join  T_HR_PersonWorkExp  personWorkExp   on  person.fid = personWorkExp.FPERSONID   ")
/* 4083 */           .append(" left join  T_BD_Regpermresidence  regpermresidence   on  regpermresidence.fid = person.FRegresidenceID  ")
/* 4084 */           .append(" left join  T_HR_PersonTechPost  psot    on  psot.fpersonid =person.fid  and  FISHIGHTECHNICAL = 1 ")
/* 4085 */           .append(" left join T_HR_BDTechnicalPost  bdpost  on  psot.FTECHNICALPOSTID  =bdpost.fid  ")
/* 4086 */           .append(" where   1=1  ")
/*      */ 
/*      */           
/* 4089 */           .append(" and  ( affairActionReason.fnumber = '0101'  )    and  ( personDegree.fishighest = 1 or personDegree.fishighest  is null  )  ")
/* 4090 */           .append(" and  ( employeeContract.FNewState !=3 or  employeeContract.FNewState is null ) and   admin1.FLEVEL  = 4  and  admin3.fnumber = 'glzb'  order by person.fnumber     ");
/*      */       }
/* 4092 */       else if (count == 3) {
/* 4093 */         sql.append(" /*dialect*/ SELECT distinct admin4.fnumber as groupNumber ,admin3.fnumber as daquNumber , admin2.fnumber  as cityNumber,admin1.fnumber as companyNumber, ' ' as deptNumber ,")
/* 4094 */           .append(" person.FNumber as personnumber,person.fname_l2 as fname ,position.Fnumber as positionNumber , position.fname_l2 as positionName ,  nvl( bdpost.fnumber  ,' ') as TechnicalPost,   ")
/* 4095 */           .append(" positionName.fnumber as zhiwunumber ,nvl( jobGrade.fnumber,' ') as jobGradeNumber  , ' ' as fentanTypeNumber  , ' ' as guanbaoTypeNumber  , ' ' as empTypeNumber , to_char( empOrgRelation.FEFFDT ,'yyyy-mm-dd hh24:mi:ss') as empDate,  ")
/* 4096 */           .append(" nvl(  to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss') ,nvl(to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss'),' ') ) as empzhuanzhengDate  , to_char(empOrgRelation.Fleffdt,'yyyy-mm-dd hh24:mi:ss') as empleftdate  ,nvl( employeeModle.fnumber,' ') as empModelNumber  ,(case employeeContract.FNewState when 0 then  '01'  when 1 then  '01'  when 2 then  '02' else ' ' end ) as hetongtype ,   ")
/* 4097 */           .append(" nvl( to_char(employeeContract.FEffectDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  EffectDate , nvl( to_char(employeeContract.FEndDate,'yyyy-mm-dd hh24:mi:ss'),' ') as EndDate , nvl(person.FIDCardNO,' ') as  IDCardNO,nvl(  person.FHjAddress,' ') as HjAddress ,   ")
/* 4098 */           .append("  (case person.FGender when 1 then  'F'  when 2 then  'M'  ELSE  ' '  end )  as Gender , nvl(  to_char( person.FBirthday,'yyyy-mm-dd hh24:mi:ss'),' ') as Birthday ,nvl( hrWed.FNAME_l2,' ') as wefName ,nvl( hrFolk.FNAME_l2,' ') as folkName ,nvl(hrPolitical.fname_l2,' ') as PoliticalName ,  ")
/* 4099 */           .append(" nvl(personContactMethod.FADDRESS,' ')  as faddress , nvl( personContactMethod.FEmail,' ') as Email , nvl( personContactMethod.FMobile ,' ') as Mobile , nvl( personContactMethod.FLinkName ,' ') as LinkName ,   nvl( personContactMethod.FLinkTelNum ,' ') as LinkTelNum , ")
/* 4100 */           .append(" nvl( hrDiploma.fname_l2 ,' ') as DiplomaName , nvl(personDegree.FGraduateSchool,' ') as  GraduateSchool ,  nvl(personDegree.FSpecialty,' ') as  Specialty ,  nvl(to_char(personDegree.FGraduateDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  GraduateDate ,  ")
/* 4101 */           .append(" nvl(person.FIdNum,' ') as idNum ,  nvl(person.FRTX,' ')  as RTX , nvl( employeeContract.FCONTRACTNO ,' ') as CONTRACTNO , nvl( to_char(personCertifyCompetency.FObtainDate,'yyyy-mm-dd hh24:mi:ss'),' ') as ObtainDate , nvl(to_char(personWorkExp.FBeginDate ,'yyyy-mm-dd hh24:mi:ss'),' ') as firstWorkTime,  ")
/* 4102 */           .append(" ' ' as insuredCityNumber , regpermresidence.fname_l2 as  hujiName , ' ' as  gongjijinNum , '' as Remarks ")
/* 4103 */           .append(" ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign ")
/* 4104 */           .append(" FROM   T_BD_Person  person    ")
/* 4105 */           .append(" left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid ")
/* 4106 */           .append(" LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid ")
/* 4107 */           .append(" left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID  ")
/* 4108 */           .append(" left join  T_ORG_Admin  admin1  on empOrgRelation.fadminorgid  = admin1.fid ")
/* 4109 */           .append(" left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid ")
/* 4110 */           .append(" left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid ")
/* 4111 */           .append(" left join  T_ORG_Admin  admin4  on admin3.FPARENTID = admin4.fid  ")
/* 4112 */           .append(" left join  T_ORG_Position  position   on  empOrgRelation.FPositionID = position.fid  ")
/* 4113 */           .append(" left join  T_ORG_Job  positionName   on  position.FJobID =positionName.fid  ")
/* 4114 */           .append(" left join  T_HR_JobGrade  jobGrade   on  empOrgRelation.FJobGradeID =jobGrade.fid ")
/* 4115 */           .append(" left join  T_HR_EmpLaborRelation  empLaborRelation   on  empLaborRelation.fpersonid =person.fid  ")
/* 4116 */           .append(" left join  T_HR_EmpPostExperienceHis  empPostExperienceHis   on  empPostExperienceHis.fpersonid =person.fid ")
/* 4117 */           .append(" left join  T_BD_EmployeeModle  employeeModle   on  empLaborRelation.FEmployeeModleID =employeeModle.fid ")
/* 4118 */           .append(" left join  T_HR_EmployeeContract employeeContract   on  employeeContract.FEmployeeID =person.fid ")
/* 4119 */           .append(" left join  T_BD_HRWed hrWed   on  hrWed.fid =person.FWedID ")
/* 4120 */           .append(" left join  T_BD_HRFolk hrFolk   on  hrFolk.fid =person.FFolkID ")
/* 4121 */           .append(" left join  T_BD_HRPolitical  hrPolitical   on  hrPolitical.fid =person.FPoliticalFaceID ")
/* 4122 */           .append(" left join  T_HR_PersonContactMethod  personContactMethod   on  person.fid =personContactMethod.fpersonid ")
/* 4123 */           .append(" left join  T_HR_PersonDegree personDegree   on  person.fid = personDegree.fpersonid  ")
/* 4124 */           .append(" left join  T_BD_HRDiploma  hrDiploma   on  hrDiploma.fid = personDegree.FDIPLOMA  ")
/* 4125 */           .append(" left join  T_HR_PersonCertifyCompetency  personCertifyCompetency   on  person.fid = personCertifyCompetency.FPERSONID   ")
/* 4126 */           .append(" left join  T_HR_PersonWorkExp  personWorkExp   on  person.fid = personWorkExp.FPERSONID   ")
/* 4127 */           .append(" left join  T_BD_Regpermresidence  regpermresidence   on  regpermresidence.fid = person.FRegresidenceID  ")
/* 4128 */           .append(" left join  T_HR_PersonTechPost  psot    on  psot.fpersonid =person.fid  and  FISHIGHTECHNICAL = 1 ")
/* 4129 */           .append(" left join T_HR_BDTechnicalPost  bdpost  on  psot.FTECHNICALPOSTID  =bdpost.fid  ")
/* 4130 */           .append(" where   1=1  ")
/*      */ 
/*      */           
/* 4133 */           .append(" and  ( affairActionReason.fnumber = '0101'  )    and  ( personDegree.fishighest = 1 or personDegree.fishighest  is null  )  ")
/* 4134 */           .append(" and  ( employeeContract.FNewState !=3 or  employeeContract.FNewState is null ) and   admin1.FLEVEL  = 4  and  admin3.fnumber != 'glzb'  order by person.fnumber    ");
/*      */       }
/*      */     
/* 4137 */     } else if ("离职".equals(type)) {
/* 4138 */       if (count == 1) {
/* 4139 */         sql.append("/*dialect*/ SELECT distinct admin5.fnumber as groupNumber ,admin4.fnumber as daquNumber ,admin3.fnumber as cityNumber,admin2.fnumber as companyNumber,admin1.fnumber as deptNumber ,")
/* 4140 */           .append(" person.FNumber as personnumber,person.fname_l2 as fname ,position.Fnumber as positionNumber , position.fname_l2 as positionName ,  nvl( bdpost.fnumber  ,' ') as TechnicalPost,   ")
/* 4141 */           .append(" positionName.fnumber as zhiwunumber ,nvl( jobGrade.fnumber,' ') as jobGradeNumber  , ' ' as fentanTypeNumber  , ' ' as guanbaoTypeNumber  , ' ' as empTypeNumber , to_char( empOrgRelation.FEFFDT ,'yyyy-mm-dd hh24:mi:ss') as empDate,  ")
/* 4142 */           .append(" nvl(  to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss') ,nvl(to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss'),' ') ) as empzhuanzhengDate  , to_char(empOrgRelation.Fleffdt,'yyyy-mm-dd hh24:mi:ss') as empleftdate  ,nvl( employeeModle.fnumber,' ') as empModelNumber  ,(case employeeContract.FNewState when 0 then  '01'  when 1 then  '01'  when 2 then  '02' else ' ' end ) as hetongtype ,   ")
/* 4143 */           .append(" nvl( to_char(employeeContract.FEffectDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  EffectDate , nvl( to_char(employeeContract.FEndDate,'yyyy-mm-dd hh24:mi:ss'),' ') as EndDate , nvl(person.FIDCardNO,' ') as  IDCardNO,nvl(  person.FHjAddress,' ') as HjAddress ,   ")
/* 4144 */           .append("  (case person.FGender when 1 then  'F'  when 2 then  'M'  ELSE  ' '  end )  as Gender , nvl(  to_char( person.FBirthday,'yyyy-mm-dd hh24:mi:ss'),' ') as Birthday ,nvl( hrWed.FNAME_l2,' ') as wefName ,nvl( hrFolk.FNAME_l2,' ') as folkName ,nvl(hrPolitical.fname_l2,' ') as PoliticalName ,  ")
/* 4145 */           .append(" nvl(personContactMethod.FADDRESS,' ')  as faddress , nvl( personContactMethod.FEmail,' ') as Email , nvl( personContactMethod.FMobile ,' ') as Mobile , nvl( personContactMethod.FLinkName ,' ') as LinkName ,  nvl( personContactMethod.FLinkTelNum ,' ') as LinkTelNum ,  ")
/* 4146 */           .append(" nvl( hrDiploma.fname_l2 ,' ') as DiplomaName , nvl(personDegree.FGraduateSchool,' ') as  GraduateSchool ,  nvl(personDegree.FSpecialty,' ') as  Specialty ,  nvl(to_char(personDegree.FGraduateDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  GraduateDate ,  ")
/* 4147 */           .append(" nvl(person.FIdNum,' ') as idNum ,  nvl(person.FRTX,' ')  as RTX , nvl( employeeContract.FCONTRACTNO ,' ') as CONTRACTNO , nvl( to_char(personCertifyCompetency.FObtainDate,'yyyy-mm-dd hh24:mi:ss'),' ') as ObtainDate , nvl(to_char(personWorkExp.FBeginDate ,'yyyy-mm-dd hh24:mi:ss'),' ') as firstWorkTime,  ")
/* 4148 */           .append("  ' ' as insuredCityNumber , nvl(regpermresidence.fname_l2,' ') as  hujiName , ' ' as  gongjijinNum ,nvl( entry.FDescription,' ') as Remarks  ")
/* 4149 */           .append(" ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign ")
/* 4150 */           .append(" FROM  T_HR_ResignBizBill bill  left join  T_HR_ResignBizBillEntry  entry  on  entry.FBILLID = bill.fid ")
/* 4151 */           .append(" left join   T_BD_Person  person    on  entry.FPersonID = person.fid   ")
/* 4152 */           .append(" left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid   and  empOrgRelation.FFlowOutAffairID = entry.FID ")
/* 4153 */           .append(" LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid ")
/* 4154 */           .append(" left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID  ")
/* 4155 */           .append(" left join  T_ORG_Admin  admin1  on empOrgRelation.fadminorgid  = admin1.fid ")
/* 4156 */           .append(" left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid ")
/* 4157 */           .append(" left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid ")
/* 4158 */           .append(" left join  T_ORG_Admin  admin4  on admin3.FPARENTID = admin4.fid  ")
/* 4159 */           .append(" left join  T_ORG_Admin  admin5  on admin4.FPARENTID = admin5.fid   ")
/* 4160 */           .append(" left join  T_ORG_Position  position   on  empOrgRelation.FPositionID = position.fid  ")
/* 4161 */           .append(" left join  T_ORG_Job  positionName   on  position.FJobID =positionName.fid  ")
/* 4162 */           .append(" left join  T_HR_JobGrade  jobGrade   on  empOrgRelation.FJobGradeID =jobGrade.fid ")
/* 4163 */           .append(" left join  T_HR_EmpLaborRelation  empLaborRelation   on  empLaborRelation.fpersonid =person.fid  ")
/* 4164 */           .append(" left join  T_HR_EmpPostExperienceHis  empPostExperienceHis   on  empPostExperienceHis.fpersonid =person.fid ")
/* 4165 */           .append(" left join  T_BD_EmployeeModle  employeeModle   on  empLaborRelation.FEmployeeModleID =employeeModle.fid ")
/* 4166 */           .append(" left join  T_HR_EmployeeContract employeeContract   on  employeeContract.FEmployeeID =person.fid ")
/* 4167 */           .append(" left join  T_BD_HRWed hrWed   on  hrWed.fid =person.FWedID ")
/* 4168 */           .append(" left join  T_BD_HRFolk hrFolk   on  hrFolk.fid =person.FFolkID ")
/* 4169 */           .append(" left join  T_BD_HRPolitical  hrPolitical   on  hrPolitical.fid =person.FPoliticalFaceID ")
/* 4170 */           .append(" left join  T_HR_PersonContactMethod  personContactMethod   on  person.fid =personContactMethod.fpersonid ")
/* 4171 */           .append(" left join  T_HR_PersonDegree personDegree   on  person.fid = personDegree.fpersonid  ")
/* 4172 */           .append(" left join  T_BD_HRDiploma  hrDiploma   on  hrDiploma.fid = personDegree.FDIPLOMA  ")
/* 4173 */           .append(" left join  T_HR_PersonCertifyCompetency  personCertifyCompetency   on  person.fid = personCertifyCompetency.FPERSONID   ")
/* 4174 */           .append(" left join  T_HR_PersonWorkExp  personWorkExp   on  person.fid = personWorkExp.FPERSONID   ")
/* 4175 */           .append(" left join  T_BD_Regpermresidence  regpermresidence   on  regpermresidence.fid = person.FRegresidenceID  ")
/* 4176 */           .append(" left join  T_HR_PersonTechPost  psot    on  psot.fpersonid =person.fid  and  FISHIGHTECHNICAL = 1 ")
/* 4177 */           .append(" left join T_HR_BDTechnicalPost  bdpost  on  psot.FTECHNICALPOSTID  =bdpost.fid  ")
/* 4178 */           .append(" where bill.fbillstate = 3   and  ( employeeContract.FNewState !=3 or  employeeContract.FNewState is null )  and  admin1.FLEVEL  = 5   ")
/*      */ 
/*      */           
/* 4181 */           .append("  order by person.fnumber ");
/*      */       }
/* 4183 */       else if (count == 2) {
/* 4184 */         sql.append("/*dialect*/ SELECT distinct   admin4.fnumber as groupNumber ,admin3.fnumber as daquNumber , ' ' as cityNumber,admin2.fnumber as companyNumber,admin1.fnumber as deptNumber ,")
/* 4185 */           .append(" person.FNumber as personnumber,person.fname_l2 as fname ,position.Fnumber as positionNumber , position.fname_l2 as positionName ,  nvl( bdpost.fnumber  ,' ') as TechnicalPost,   ")
/* 4186 */           .append(" positionName.fnumber as zhiwunumber ,nvl( jobGrade.fnumber,' ') as jobGradeNumber  , ' ' as fentanTypeNumber  , ' ' as guanbaoTypeNumber  , ' ' as empTypeNumber , to_char( empOrgRelation.FEFFDT ,'yyyy-mm-dd hh24:mi:ss') as empDate,  ")
/* 4187 */           .append(" nvl(  to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss') ,nvl(to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss'),' ') ) as empzhuanzhengDate  , to_char(empOrgRelation.Fleffdt,'yyyy-mm-dd hh24:mi:ss') as empleftdate  ,nvl( employeeModle.fnumber,' ') as empModelNumber  ,(case employeeContract.FNewState when 0 then  '01'  when 1 then  '01'  when 2 then  '02' else ' ' end ) as hetongtype ,   ")
/* 4188 */           .append(" nvl( to_char(employeeContract.FEffectDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  EffectDate , nvl( to_char(employeeContract.FEndDate,'yyyy-mm-dd hh24:mi:ss'),' ') as EndDate , nvl(person.FIDCardNO,' ') as  IDCardNO,nvl(  person.FHjAddress,' ') as HjAddress ,   ")
/* 4189 */           .append("  (case person.FGender when 1 then  'F'  when 2 then  'M'  ELSE  ' '  end )  as Gender , nvl(  to_char( person.FBirthday,'yyyy-mm-dd hh24:mi:ss'),' ') as Birthday ,nvl( hrWed.FNAME_l2,' ') as wefName ,nvl( hrFolk.FNAME_l2,' ') as folkName ,nvl(hrPolitical.fname_l2,' ') as PoliticalName ,  ")
/* 4190 */           .append(" nvl(personContactMethod.FADDRESS,' ')  as faddress , nvl( personContactMethod.FEmail,' ') as Email , nvl( personContactMethod.FMobile ,' ') as Mobile , nvl( personContactMethod.FLinkName ,' ') as LinkName , nvl( personContactMethod.FLinkTelNum ,' ') as LinkTelNum ,   ")
/* 4191 */           .append(" nvl( hrDiploma.fname_l2 ,' ') as DiplomaName , nvl(personDegree.FGraduateSchool,' ') as  GraduateSchool ,  nvl(personDegree.FSpecialty,' ') as  Specialty ,  nvl(to_char(personDegree.FGraduateDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  GraduateDate ,  ")
/* 4192 */           .append(" nvl(person.FIdNum,' ') as idNum ,  nvl(person.FRTX,' ')  as RTX , nvl( employeeContract.FCONTRACTNO ,' ') as CONTRACTNO , nvl( to_char(personCertifyCompetency.FObtainDate,'yyyy-mm-dd hh24:mi:ss'),' ') as ObtainDate , nvl(to_char(personWorkExp.FBeginDate ,'yyyy-mm-dd hh24:mi:ss'),' ') as firstWorkTime,  ")
/* 4193 */           .append("  ' ' as insuredCityNumber , nvl(regpermresidence.fname_l2,' ') as  hujiName , ' ' as  gongjijinNum ,nvl( entry.FDescription,' ') as Remarks  ")
/* 4194 */           .append(" ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign ")
/* 4195 */           .append(" FROM T_HR_ResignBizBill bill  left join  T_HR_ResignBizBillEntry  entry  on  entry.FBILLID = bill.fid ")
/* 4196 */           .append(" left join   T_BD_Person  person    on  entry.FPersonID = person.fid   ")
/* 4197 */           .append(" left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid   and  empOrgRelation.FFlowOutAffairID = entry.FID ")
/* 4198 */           .append(" LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid ")
/* 4199 */           .append(" left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID  ")
/* 4200 */           .append(" left join  T_ORG_Admin  admin1  on empOrgRelation.fadminorgid  = admin1.fid ")
/* 4201 */           .append(" left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid ")
/* 4202 */           .append(" left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid ")
/* 4203 */           .append(" left join  T_ORG_Admin  admin4  on admin3.FPARENTID = admin4.fid  ")
/* 4204 */           .append(" left join  T_ORG_Position  position   on  empOrgRelation.FPositionID = position.fid  ")
/* 4205 */           .append(" left join  T_ORG_Job  positionName   on  position.FJobID =positionName.fid  ")
/* 4206 */           .append(" left join  T_HR_JobGrade  jobGrade   on  empOrgRelation.FJobGradeID =jobGrade.fid ")
/* 4207 */           .append(" left join  T_HR_EmpLaborRelation  empLaborRelation   on  empLaborRelation.fpersonid =person.fid  ")
/* 4208 */           .append(" left join  T_HR_EmpPostExperienceHis  empPostExperienceHis   on  empPostExperienceHis.fpersonid =person.fid ")
/* 4209 */           .append(" left join  T_BD_EmployeeModle  employeeModle   on  empLaborRelation.FEmployeeModleID =employeeModle.fid ")
/* 4210 */           .append(" left join  T_HR_EmployeeContract employeeContract   on  employeeContract.FEmployeeID =person.fid ")
/* 4211 */           .append(" left join  T_BD_HRWed hrWed   on  hrWed.fid =person.FWedID ")
/* 4212 */           .append(" left join  T_BD_HRFolk hrFolk   on  hrFolk.fid =person.FFolkID ")
/* 4213 */           .append(" left join  T_BD_HRPolitical  hrPolitical   on  hrPolitical.fid =person.FPoliticalFaceID ")
/* 4214 */           .append(" left join  T_HR_PersonContactMethod  personContactMethod   on  person.fid =personContactMethod.fpersonid ")
/* 4215 */           .append(" left join  T_HR_PersonDegree personDegree   on  person.fid = personDegree.fpersonid  ")
/* 4216 */           .append(" left join  T_BD_HRDiploma  hrDiploma   on  hrDiploma.fid = personDegree.FDIPLOMA  ")
/* 4217 */           .append(" left join  T_HR_PersonCertifyCompetency  personCertifyCompetency   on  person.fid = personCertifyCompetency.FPERSONID   ")
/* 4218 */           .append(" left join  T_HR_PersonWorkExp  personWorkExp   on  person.fid = personWorkExp.FPERSONID   ")
/* 4219 */           .append(" left join  T_BD_Regpermresidence  regpermresidence   on  regpermresidence.fid = person.FRegresidenceID  ")
/* 4220 */           .append(" left join  T_HR_PersonTechPost  psot    on  psot.fpersonid =person.fid  and  FISHIGHTECHNICAL = 1 ")
/* 4221 */           .append(" left join T_HR_BDTechnicalPost  bdpost  on  psot.FTECHNICALPOSTID  =bdpost.fid  ")
/* 4222 */           .append(" where bill.fbillstate = 3    and  ( employeeContract.FNewState !=3 or  employeeContract.FNewState is null )  and  admin1.FLEVEL  = 4  and  admin3.fnumber = 'glzb'   ")
/*      */ 
/*      */           
/* 4225 */           .append("  order by person.fnumber ");
/*      */       }
/* 4227 */       else if (count == 3) {
/* 4228 */         sql.append("/*dialect*/ SELECT distinct admin4.fnumber as groupNumber ,admin3.fnumber as daquNumber , admin2.fnumber  as cityNumber,admin1.fnumber as companyNumber, ' '  as deptNumber ,")
/* 4229 */           .append(" person.FNumber as personnumber,person.fname_l2 as fname ,position.Fnumber as positionNumber , position.fname_l2 as positionName ,  nvl( bdpost.fnumber  ,' ') as TechnicalPost,   ")
/* 4230 */           .append(" positionName.fnumber as zhiwunumber ,nvl( jobGrade.fnumber,' ') as jobGradeNumber  , ' ' as fentanTypeNumber  , ' ' as guanbaoTypeNumber  , ' ' as empTypeNumber , to_char( empOrgRelation.FEFFDT ,'yyyy-mm-dd hh24:mi:ss') as empDate,  ")
/* 4231 */           .append(" nvl(  to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss') ,nvl(to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss'),' ') ) as empzhuanzhengDate  ,  to_char(empOrgRelation.Fleffdt,'yyyy-mm-dd hh24:mi:ss') as empleftdate  ,nvl( employeeModle.fnumber,' ') as empModelNumber  ,(case employeeContract.FNewState when 0 then  '01'  when 1 then  '01'  when 2 then  '02' else ' ' end ) as hetongtype ,   ")
/* 4232 */           .append(" nvl( to_char(employeeContract.FEffectDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  EffectDate , nvl( to_char(employeeContract.FEndDate,'yyyy-mm-dd hh24:mi:ss'),' ') as EndDate , nvl(person.FIDCardNO,' ') as  IDCardNO,nvl(  person.FHjAddress,' ') as HjAddress ,   ")
/* 4233 */           .append("  (case person.FGender when 1 then  'F'  when 2 then  'M'  ELSE  ' '  end )  as Gender , nvl(  to_char( person.FBirthday,'yyyy-mm-dd hh24:mi:ss'),' ') as Birthday ,nvl( hrWed.FNAME_l2,' ') as wefName ,nvl( hrFolk.FNAME_l2,' ') as folkName ,nvl(hrPolitical.fname_l2,' ') as PoliticalName ,  ")
/* 4234 */           .append(" nvl(personContactMethod.FADDRESS,' ')  as faddress , nvl( personContactMethod.FEmail,' ') as Email , nvl( personContactMethod.FMobile ,' ') as Mobile , nvl( personContactMethod.FLinkName ,' ') as LinkName ,  nvl( personContactMethod.FLinkTelNum ,' ') as LinkTelNum ,  ")
/* 4235 */           .append(" nvl( hrDiploma.fname_l2 ,' ') as DiplomaName , nvl(personDegree.FGraduateSchool,' ') as  GraduateSchool ,  nvl(personDegree.FSpecialty,' ') as  Specialty ,  nvl(to_char(personDegree.FGraduateDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  GraduateDate ,  ")
/* 4236 */           .append(" nvl(person.FIdNum,' ') as idNum ,  nvl(person.FRTX,' ')  as RTX , nvl( employeeContract.FCONTRACTNO ,' ') as CONTRACTNO , nvl( to_char(personCertifyCompetency.FObtainDate,'yyyy-mm-dd hh24:mi:ss'),' ') as ObtainDate , nvl(to_char(personWorkExp.FBeginDate ,'yyyy-mm-dd hh24:mi:ss'),' ') as firstWorkTime,  ")
/* 4237 */           .append("  ' ' as insuredCityNumber , nvl(regpermresidence.fname_l2,' ') as  hujiName , ' ' as  gongjijinNum ,nvl( entry.FDescription,' ') as Remarks  ")
/* 4238 */           .append(" ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign ")
/* 4239 */           .append(" FROM  T_HR_ResignBizBill bill  left join  T_HR_ResignBizBillEntry  entry  on  entry.FBILLID = bill.fid ")
/* 4240 */           .append(" left join   T_BD_Person  person    on  entry.FPersonID = person.fid   ")
/* 4241 */           .append(" left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid   and  empOrgRelation.FFlowOutAffairID = entry.FID ")
/* 4242 */           .append(" LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid ")
/* 4243 */           .append(" left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID  ")
/* 4244 */           .append(" left join  T_ORG_Admin  admin1  on empOrgRelation.fadminorgid  = admin1.fid ")
/* 4245 */           .append(" left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid ")
/* 4246 */           .append(" left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid ")
/* 4247 */           .append(" left join  T_ORG_Admin  admin4  on admin3.FPARENTID = admin4.fid  ")
/* 4248 */           .append(" left join  T_ORG_Position  position   on  empOrgRelation.FPositionID = position.fid  ")
/* 4249 */           .append(" left join  T_ORG_Job  positionName   on  position.FJobID =positionName.fid  ")
/* 4250 */           .append(" left join  T_HR_JobGrade  jobGrade   on  empOrgRelation.FJobGradeID =jobGrade.fid ")
/* 4251 */           .append(" left join  T_HR_EmpLaborRelation  empLaborRelation   on  empLaborRelation.fpersonid =person.fid  ")
/* 4252 */           .append(" left join  T_HR_EmpPostExperienceHis  empPostExperienceHis   on  empPostExperienceHis.fpersonid =person.fid ")
/* 4253 */           .append(" left join  T_BD_EmployeeModle  employeeModle   on  empLaborRelation.FEmployeeModleID =employeeModle.fid ")
/* 4254 */           .append(" left join  T_HR_EmployeeContract employeeContract   on  employeeContract.FEmployeeID =person.fid ")
/* 4255 */           .append(" left join  T_BD_HRWed hrWed   on  hrWed.fid =person.FWedID ")
/* 4256 */           .append(" left join  T_BD_HRFolk hrFolk   on  hrFolk.fid =person.FFolkID ")
/* 4257 */           .append(" left join  T_BD_HRPolitical  hrPolitical   on  hrPolitical.fid =person.FPoliticalFaceID ")
/* 4258 */           .append(" left join  T_HR_PersonContactMethod  personContactMethod   on  person.fid =personContactMethod.fpersonid ")
/* 4259 */           .append(" left join  T_HR_PersonDegree personDegree   on  person.fid = personDegree.fpersonid  ")
/* 4260 */           .append(" left join  T_BD_HRDiploma  hrDiploma   on  hrDiploma.fid = personDegree.FDIPLOMA  ")
/* 4261 */           .append(" left join  T_HR_PersonCertifyCompetency  personCertifyCompetency   on  person.fid = personCertifyCompetency.FPERSONID   ")
/* 4262 */           .append(" left join  T_HR_PersonWorkExp  personWorkExp   on  person.fid = personWorkExp.FPERSONID   ")
/* 4263 */           .append(" left join  T_BD_Regpermresidence  regpermresidence   on  regpermresidence.fid = person.FRegresidenceID  ")
/* 4264 */           .append(" left join  T_HR_PersonTechPost  psot    on  psot.fpersonid =person.fid  and  FISHIGHTECHNICAL = 1 ")
/* 4265 */           .append(" left join T_HR_BDTechnicalPost  bdpost  on  psot.FTECHNICALPOSTID  =bdpost.fid  ")
/* 4266 */           .append(" where bill.fbillstate = 3  and  ( employeeContract.FNewState !=3 or  employeeContract.FNewState is null )  and  admin1.FLEVEL  = 4  and  admin3.fnumber != 'glzb'  ")
/*      */ 
/*      */           
/* 4269 */           .append(" and    EmpOrgRelation.FActionID  in ('DawAAAApViXmaL7Z','4zbGCL3aRkmpyN/K20hGEeZovtk=','DawAAAAqVJfmaL7Z','P/OSDCPfQXqU0TB/LQoA6uZovtk=') order by person.fnumber ");
/*      */       } 
/*      */     }   
/* 4274 */     return sql;
/*      */   }
/*      */   private StringBuffer getSqlYingDong(StringBuffer sql, int yidongType, int sqlType) throws BOSException {
/* 4282 */     if (yidongType == 0) {
/* 4283 */       if (sqlType == 1) {
/* 4284 */         sql.append("/*dialect*/ SELECT  distinct to_char(entry.FBizDate,'yyyy-mm-dd hh24:mi:ss') as changesDate , admin5.fnumber as groupNumber ,admin4.fnumber as daquNumber ,admin3.fnumber as cityNumber,admin2.fnumber as companyNumber,admin1.fnumber as deptNumber ,")
/* 4285 */           .append(" person.FNumber as personnumber,person.fname_l2 as fname ,position.Fnumber as positionNumber , position.fname_l2 as positionName ,  nvl( bdpost.fnumber  ,' ') as TechnicalPost,   ")
/* 4286 */           .append(" positionName.fnumber as zhiwunumber ,nvl( jobGrade.fnumber,' ') as jobGradeNumber  , ' ' as fentanTypeNumber  , ' ' as guanbaoTypeNumber  , ' ' as empTypeNumber , to_char( empOrgRelation.FEFFDT ,'yyyy-mm-dd hh24:mi:ss') as empDate,  ")
/* 4287 */           .append(" nvl(  to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss') ,nvl(to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss'),' ') ) as empzhuanzhengDate  ,  to_char(empOrgRelation.Fleffdt,'yyyy-mm-dd hh24:mi:ss') as empleftdate  ,nvl( employeeModle.fnumber,' ') as empModelNumber  ,(case employeeContract.FNewState when 0 then  '01'  when 1 then  '01'  when 2 then  '02' else ' ' end ) as hetongtype ,   ")
/* 4288 */           .append(" nvl( to_char(employeeContract.FEffectDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  EffectDate , nvl( to_char(employeeContract.FEndDate,'yyyy-mm-dd hh24:mi:ss'),' ') as EndDate , nvl(person.FIDCardNO,' ') as  IDCardNO,nvl(  person.FHjAddress,' ') as HjAddress ,   ")
/* 4289 */           .append("  (case person.FGender when 1 then  'F'  when 2 then  'M'  ELSE  ' '  end )  as Gender , nvl(  to_char( person.FBirthday,'yyyy-mm-dd hh24:mi:ss'),' ') as Birthday ,nvl( hrWed.FNAME_l2,' ') as wefName ,nvl( hrFolk.FNAME_l2,' ') as folkName ,nvl(hrPolitical.fname_l2,' ') as PoliticalName ,  ")
/* 4290 */           .append(" nvl(personContactMethod.FADDRESS,' ')  as faddress , nvl( personContactMethod.FEmail,' ') as Email , nvl( personContactMethod.FMobile ,' ') as Mobile , nvl( personContactMethod.FLinkName ,' ') as LinkName ,  nvl( personContactMethod.FLinkTelNum ,' ') as LinkTelNum ,  ")
/* 4291 */           .append(" nvl( hrDiploma.fname_l2 ,' ') as DiplomaName , nvl(personDegree.FGraduateSchool,' ') as  GraduateSchool ,  nvl(personDegree.FSpecialty,' ') as  Specialty ,  nvl(to_char(personDegree.FGraduateDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  GraduateDate ,  ")
/* 4292 */           .append(" nvl(person.FIdNum,' ') as idNum ,  nvl(person.FRTX,' ')  as RTX , nvl( employeeContract.FCONTRACTNO ,' ') as CONTRACTNO , nvl( to_char(personCertifyCompetency.FObtainDate,'yyyy-mm-dd hh24:mi:ss'),' ') as ObtainDate , nvl(to_char(personWorkExp.FBeginDate ,'yyyy-mm-dd hh24:mi:ss'),' ') as firstWorkTime,  ")
/* 4293 */           .append("  ' ' as insuredCityNumber , nvl(regpermresidence.fname_l2,' ') as  hujiName , ' ' as  gongjijinNum ,nvl( entry.FDescription,' ') as Remarks  ")
/* 4294 */           .append(" ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign ")
/* 4295 */           .append(" FROM  t_hr_RiseBizBill  bill ")
/* 4296 */           .append(" left join  T_HR_RiseBizBillentry   entry  on entry.FBILLID = bill.fid")
/* 4297 */           .append(" left join   T_BD_Person  person    on entry.FPersonID = person.fid")
/* 4298 */           .append(" left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid  and empOrgRelation.FEFFDT = entry.FBizDate")
/* 4299 */           .append(" LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid")
/* 4300 */           .append(" left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID ")
/* 4301 */           .append(" left join  T_ORG_Admin  admin1  on entry.FAdminOrgID  = admin1.fid")
/* 4302 */           .append(" left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid")
/* 4303 */           .append(" left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid")
/* 4304 */           .append(" left join  T_ORG_Admin  admin4  on admin3.FPARENTID = admin4.fid")
/* 4305 */           .append(" left join  T_ORG_Admin  admin5  on admin4.FPARENTID = admin5.fid  ")
/* 4306 */           .append(" left join  T_ORG_Position  position   on  entry.FPositionID = position.fid")
/* 4307 */           .append(" left join  T_ORG_Job  positionName   on  position.FJobID =positionName.fid")
/* 4308 */           .append(" left join  T_HR_JobGrade  jobGrade   on  empOrgRelation.FJobGradeID =jobGrade.fid")
/* 4309 */           .append(" left join  T_HR_EmpLaborRelation  empLaborRelation   on  empLaborRelation.fpersonid =person.fid")
/* 4310 */           .append(" left join  T_HR_EmpPostExperienceHis  empPostExperienceHis   on  empPostExperienceHis.fpersonid =person.fid")
/* 4311 */           .append(" left join  T_BD_EmployeeModle  employeeModle   on  empLaborRelation.FEmployeeModleID =employeeModle.fid")
/* 4312 */           .append(" left join  T_HR_EmployeeContract employeeContract   on  employeeContract.FEmployeeID =person.fid  ")
/* 4313 */           .append(" left join  T_BD_HRWed hrWed   on  hrWed.fid =person.FWedID")
/* 4314 */           .append(" left join  T_BD_HRFolk hrFolk   on  hrFolk.fid =person.FFolkID")
/* 4315 */           .append(" left join  T_BD_HRPolitical  hrPolitical   on  hrPolitical.fid =person.FPoliticalFaceID")
/* 4316 */           .append(" left join  T_HR_PersonContactMethod  personContactMethod   on  person.fid =personContactMethod.fpersonid")
/* 4317 */           .append(" left join  T_HR_PersonDegree personDegree   on  person.fid = personDegree.fpersonid")
/* 4318 */           .append(" left join  T_BD_HRDiploma  hrDiploma   on  hrDiploma.fid = personDegree.FDIPLOMA")
/* 4319 */           .append(" left join  T_HR_PersonCertifyCompetency  personCertifyCompetency   on  person.fid = personCertifyCompetency.FPERSONID ")
/* 4320 */           .append(" left join  T_HR_PersonWorkExp  personWorkExp   on  person.fid = personWorkExp.FPERSONID ")
/* 4321 */           .append(" left join  T_BD_Regpermresidence  regpermresidence   on  regpermresidence.fid = person.FRegresidenceID")
/* 4322 */           .append(" left join  T_HR_PersonTechPost  psot    on  psot.fpersonid =person.fid  and  FISHIGHTECHNICAL = 1 ")
/* 4323 */           .append(" left join T_HR_BDTechnicalPost  bdpost  on  psot.FTECHNICALPOSTID  =bdpost.fid  ")
/* 4324 */           .append(" where bill.fbillstate = 3   and  bill.fapplierid is not null     ")
/*      */ 
/*      */           
/* 4327 */           .append(" and  admin1.FLEVEL  = 5 order by  person.fnumber   ");
/*      */       }
/* 4329 */       else if (sqlType == 2) {
/* 4330 */         sql.append(" /*dialect*/ SELECT distinct to_char(entry.FBizDate,'yyyy-mm-dd hh24:mi:ss') as changesDate ,  admin4.fnumber as groupNumber ,admin3.fnumber as daquNumber , ' ' as cityNumber,admin2.fnumber as companyNumber,admin1.fnumber as deptNumber ,")
/* 4331 */           .append(" person.FNumber as personnumber,person.fname_l2 as fname ,position.Fnumber as positionNumber , position.fname_l2 as positionName ,  nvl( bdpost.fnumber  ,' ') as TechnicalPost,   ")
/* 4332 */           .append(" positionName.fnumber as zhiwunumber ,nvl( jobGrade.fnumber,' ') as jobGradeNumber  , ' ' as fentanTypeNumber  , ' ' as guanbaoTypeNumber  , ' ' as empTypeNumber , to_char( empOrgRelation.FEFFDT ,'yyyy-mm-dd hh24:mi:ss') as empDate,  ")
/* 4333 */           .append(" nvl(  to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss') ,nvl(to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss'),' ') ) as empzhuanzhengDate  , to_char(empOrgRelation.Fleffdt,'yyyy-mm-dd hh24:mi:ss') as empleftdate  ,nvl( employeeModle.fnumber,' ') as empModelNumber  ,(case employeeContract.FNewState when 0 then  '01'  when 1 then  '01'  when 2 then  '02' else ' ' end ) as hetongtype ,   ")
/* 4334 */           .append(" nvl( to_char(employeeContract.FEffectDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  EffectDate , nvl( to_char(employeeContract.FEndDate,'yyyy-mm-dd hh24:mi:ss'),' ') as EndDate , nvl(person.FIDCardNO,' ') as  IDCardNO,nvl(  person.FHjAddress,' ') as HjAddress ,   ")
/* 4335 */           .append("  (case person.FGender when 1 then  'F'  when 2 then  'M'  ELSE  ' '  end )  as Gender , nvl(  to_char( person.FBirthday,'yyyy-mm-dd hh24:mi:ss'),' ') as Birthday ,nvl( hrWed.FNAME_l2,' ') as wefName ,nvl( hrFolk.FNAME_l2,' ') as folkName ,nvl(hrPolitical.fname_l2,' ') as PoliticalName ,  ")
/* 4336 */           .append(" nvl(personContactMethod.FADDRESS,' ')  as faddress , nvl( personContactMethod.FEmail,' ') as Email , nvl( personContactMethod.FMobile ,' ') as Mobile , nvl( personContactMethod.FLinkName ,' ') as LinkName ,  nvl( personContactMethod.FLinkTelNum ,' ') as LinkTelNum ,  ")
/* 4337 */           .append(" nvl( hrDiploma.fname_l2 ,' ') as DiplomaName , nvl(personDegree.FGraduateSchool,' ') as  GraduateSchool ,  nvl(personDegree.FSpecialty,' ') as  Specialty ,  nvl(to_char(personDegree.FGraduateDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  GraduateDate ,  ")
/* 4338 */           .append(" nvl(person.FIdNum,' ') as idNum ,  nvl(person.FRTX,' ')  as RTX , nvl( employeeContract.FCONTRACTNO ,' ') as CONTRACTNO , nvl( to_char(personCertifyCompetency.FObtainDate,'yyyy-mm-dd hh24:mi:ss'),' ') as ObtainDate , nvl(to_char(personWorkExp.FBeginDate ,'yyyy-mm-dd hh24:mi:ss'),' ') as firstWorkTime,  ")
/* 4339 */           .append("  ' ' as insuredCityNumber , nvl(regpermresidence.fname_l2,' ') as  hujiName , ' ' as  gongjijinNum ,nvl( entry.FDescription,' ') as Remarks  ")
/* 4340 */           .append(" ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign ")
/* 4341 */           .append(" FROM  t_hr_RiseBizBill  bill ")
/* 4342 */           .append(" left join  T_HR_RiseBizBillentry   entry  on entry.FBILLID = bill.fid")
/* 4343 */           .append("  left join   T_BD_Person  person    on entry.FPersonID = person.fid")
/* 4344 */           .append(" left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid  and empOrgRelation.FEFFDT = entry.FBizDate")
/* 4345 */           .append(" LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid")
/* 4346 */           .append(" left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID ")
/* 4347 */           .append(" left join  T_ORG_Admin  admin1  on entry.FAdminOrgID  = admin1.fid")
/* 4348 */           .append(" left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid")
/* 4349 */           .append(" left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid")
/* 4350 */           .append("  left join  T_ORG_Admin  admin4  on admin3.FPARENTID = admin4.fid")
/* 4351 */           .append(" left join  T_ORG_Position  position   on  entry.FPositionID = position.fid")
/* 4352 */           .append(" left join  T_ORG_Job  positionName   on  position.FJobID =positionName.fid")
/* 4353 */           .append(" left join  T_HR_JobGrade  jobGrade   on  empOrgRelation.FJobGradeID =jobGrade.fid")
/* 4354 */           .append(" left join  T_HR_EmpLaborRelation  empLaborRelation   on  empLaborRelation.fpersonid =person.fid")
/* 4355 */           .append(" left join  T_HR_EmpPostExperienceHis  empPostExperienceHis   on  empPostExperienceHis.fpersonid =person.fid")
/* 4356 */           .append("  left join  T_BD_EmployeeModle  employeeModle   on  empLaborRelation.FEmployeeModleID =employeeModle.fid")
/* 4357 */           .append(" left join  T_HR_EmployeeContract employeeContract   on  employeeContract.FEmployeeID =person.fid  ")
/* 4358 */           .append(" left join  T_BD_HRWed hrWed   on  hrWed.fid =person.FWedID")
/* 4359 */           .append(" left join  T_BD_HRFolk hrFolk   on  hrFolk.fid =person.FFolkID")
/* 4360 */           .append(" left join  T_BD_HRPolitical  hrPolitical   on  hrPolitical.fid =person.FPoliticalFaceID")
/* 4361 */           .append(" left join  T_HR_PersonContactMethod  personContactMethod   on  person.fid =personContactMethod.fpersonid")
/* 4362 */           .append(" left join  T_HR_PersonDegree personDegree   on  person.fid = personDegree.fpersonid")
/* 4363 */           .append(" left join  T_BD_HRDiploma  hrDiploma   on  hrDiploma.fid = personDegree.FDIPLOMA")
/* 4364 */           .append(" left join  T_HR_PersonCertifyCompetency  personCertifyCompetency   on  person.fid = personCertifyCompetency.FPERSONID ")
/* 4365 */           .append(" left join  T_HR_PersonWorkExp  personWorkExp   on  person.fid = personWorkExp.FPERSONID ")
/* 4366 */           .append(" left join  T_BD_Regpermresidence  regpermresidence   on  regpermresidence.fid = person.FRegresidenceID")
/* 4367 */           .append(" left join  T_HR_PersonTechPost  psot    on  psot.fpersonid =person.fid  and  FISHIGHTECHNICAL = 1 ")
/* 4368 */           .append(" left join T_HR_BDTechnicalPost  bdpost  on  psot.FTECHNICALPOSTID  =bdpost.fid  ")
/* 4369 */           .append(" where bill.fbillstate = 3   and  bill.fapplierid is not null   ")
/*      */ 
/*      */           
/* 4372 */           .append(" and admin1.FLEVEL  = 4  and  admin3.fnumber = 'glzb'  order by person.fnumber    ");
/*      */       }
/* 4374 */       else if (sqlType == 3) {
/*      */         
/* 4376 */         sql.append(" /*dialect*/ SELECT distinct to_char(entry.FBizDate,'yyyy-mm-dd hh24:mi:ss') as changesDate , admin4.fnumber as groupNumber ,admin3.fnumber as daquNumber , admin2.fnumber  as cityNumber,admin1.fnumber as companyNumber, ' ' as deptNumber ,")
/* 4377 */           .append(" person.FNumber as personnumber,person.fname_l2 as fname ,position.Fnumber as positionNumber , position.fname_l2 as positionName ,  nvl( bdpost.fnumber  ,' ') as TechnicalPost,   ")
/* 4378 */           .append(" positionName.fnumber as zhiwunumber ,nvl( jobGrade.fnumber,' ') as jobGradeNumber  , ' ' as fentanTypeNumber  , ' ' as guanbaoTypeNumber  , ' ' as empTypeNumber , to_char( empOrgRelation.FEFFDT ,'yyyy-mm-dd hh24:mi:ss') as empDate,  ")
/* 4379 */           .append(" nvl(  to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss') ,nvl(to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss'),' ') ) as empzhuanzhengDate  , to_char(empOrgRelation.Fleffdt,'yyyy-mm-dd hh24:mi:ss') as empleftdate  ,nvl( employeeModle.fnumber,' ') as empModelNumber  ,(case employeeContract.FNewState when 0 then  '01'  when 1 then  '01'  when 2 then  '02' else ' ' end ) as hetongtype ,   ")
/* 4380 */           .append(" nvl( to_char(employeeContract.FEffectDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  EffectDate , nvl( to_char(employeeContract.FEndDate,'yyyy-mm-dd hh24:mi:ss'),' ') as EndDate , nvl(person.FIDCardNO,' ') as  IDCardNO,nvl(  person.FHjAddress,' ') as HjAddress ,   ")
/* 4381 */           .append("  (case person.FGender when 1 then  'F'  when 2 then  'M'  ELSE  ' '  end )  as Gender , nvl(  to_char( person.FBirthday,'yyyy-mm-dd hh24:mi:ss'),' ') as Birthday ,nvl( hrWed.FNAME_l2,' ') as wefName ,nvl( hrFolk.FNAME_l2,' ') as folkName ,nvl(hrPolitical.fname_l2,' ') as PoliticalName ,  ")
/* 4382 */           .append(" nvl(personContactMethod.FADDRESS,' ')  as faddress , nvl( personContactMethod.FEmail,' ') as Email , nvl( personContactMethod.FMobile ,' ') as Mobile , nvl( personContactMethod.FLinkName ,' ') as LinkName ,  nvl( personContactMethod.FLinkTelNum ,' ') as LinkTelNum , ")
/* 4383 */           .append(" nvl( hrDiploma.fname_l2 ,' ') as DiplomaName , nvl(personDegree.FGraduateSchool,' ') as  GraduateSchool ,  nvl(personDegree.FSpecialty,' ') as  Specialty ,  nvl(to_char(personDegree.FGraduateDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  GraduateDate ,  ")
/* 4384 */           .append(" nvl(person.FIdNum,' ') as idNum ,  nvl(person.FRTX,' ')  as RTX , nvl( employeeContract.FCONTRACTNO ,' ') as CONTRACTNO , nvl( to_char(personCertifyCompetency.FObtainDate,'yyyy-mm-dd hh24:mi:ss'),' ') as ObtainDate , nvl(to_char(personWorkExp.FBeginDate ,'yyyy-mm-dd hh24:mi:ss'),' ') as firstWorkTime,  ")
/* 4385 */           .append("  ' ' as insuredCityNumber , nvl(regpermresidence.fname_l2,' ') as  hujiName , ' ' as  gongjijinNum ,nvl( entry.FDescription,' ') as Remarks  ")
/* 4386 */           .append(" ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign ")
/* 4387 */           .append(" FROM  t_hr_RiseBizBill  bill ")
/* 4388 */           .append(" left join  T_HR_RiseBizBillentry   entry  on entry.FBILLID = bill.fid")
/* 4389 */           .append("  left join   T_BD_Person  person    on entry.FPersonID = person.fid")
/* 4390 */           .append(" left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid  and empOrgRelation.FEFFDT = entry.FBizDate")
/* 4391 */           .append(" LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid")
/* 4392 */           .append(" left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID ")
/* 4393 */           .append(" left join  T_ORG_Admin  admin1  on entry.FAdminOrgID  = admin1.fid")
/* 4394 */           .append(" left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid")
/* 4395 */           .append(" left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid")
/* 4396 */           .append("  left join  T_ORG_Admin  admin4  on admin3.FPARENTID = admin4.fid")
/* 4397 */           .append(" left join  T_ORG_Position  position   on  entry.FPositionID = position.fid")
/* 4398 */           .append(" left join  T_ORG_Job  positionName   on  position.FJobID =positionName.fid")
/* 4399 */           .append(" left join  T_HR_JobGrade  jobGrade   on  empOrgRelation.FJobGradeID =jobGrade.fid")
/* 4400 */           .append(" left join  T_HR_EmpLaborRelation  empLaborRelation   on  empLaborRelation.fpersonid =person.fid")
/* 4401 */           .append(" left join  T_HR_EmpPostExperienceHis  empPostExperienceHis   on  empPostExperienceHis.fpersonid =person.fid")
/* 4402 */           .append("  left join  T_BD_EmployeeModle  employeeModle   on  empLaborRelation.FEmployeeModleID =employeeModle.fid")
/* 4403 */           .append(" left join  T_HR_EmployeeContract employeeContract   on  employeeContract.FEmployeeID =person.fid  ")
/* 4404 */           .append(" left join  T_BD_HRWed hrWed   on  hrWed.fid =person.FWedID")
/* 4405 */           .append(" left join  T_BD_HRFolk hrFolk   on  hrFolk.fid =person.FFolkID")
/* 4406 */           .append(" left join  T_BD_HRPolitical  hrPolitical   on  hrPolitical.fid =person.FPoliticalFaceID")
/* 4407 */           .append(" left join  T_HR_PersonContactMethod  personContactMethod   on  person.fid =personContactMethod.fpersonid")
/* 4408 */           .append(" left join  T_HR_PersonDegree personDegree   on  person.fid = personDegree.fpersonid")
/* 4409 */           .append(" left join  T_BD_HRDiploma  hrDiploma   on  hrDiploma.fid = personDegree.FDIPLOMA")
/* 4410 */           .append(" left join  T_HR_PersonCertifyCompetency  personCertifyCompetency   on  person.fid = personCertifyCompetency.FPERSONID ")
/* 4411 */           .append(" left join  T_HR_PersonWorkExp  personWorkExp   on  person.fid = personWorkExp.FPERSONID ")
/* 4412 */           .append(" left join  T_BD_Regpermresidence  regpermresidence   on  regpermresidence.fid = person.FRegresidenceID")
/* 4413 */           .append(" left join  T_HR_PersonTechPost  psot    on  psot.fpersonid =person.fid  and  FISHIGHTECHNICAL = 1 ")
/* 4414 */           .append(" left join T_HR_BDTechnicalPost  bdpost  on  psot.FTECHNICALPOSTID  =bdpost.fid  ")
/* 4415 */           .append(" where bill.fbillstate = 3   and  bill.fapplierid is not null    ")
/*      */ 
/*      */           
/* 4418 */           .append(" and admin1.FLEVEL  = 4  and  admin3.fnumber != 'glzb'  order by person.fnumber    ");
/*      */       }
/*      */     
/* 4421 */     } else if (yidongType == 1) {
/* 4422 */       if (sqlType == 1) {
/* 4423 */         sql.append(" /*dialect*/ SELECT   distinct  to_char(entry.FBizDate,'yyyy-mm-dd hh24:mi:ss') as changesDate , admin5.fnumber as groupNumber ,admin4.fnumber as daquNumber ,admin3.fnumber as cityNumber,admin2.fnumber as companyNumber,admin1.fnumber as deptNumber , ")
/* 4424 */           .append(" person.FNumber as personnumber,person.fname_l2 as fname ,position.Fnumber as positionNumber , position.fname_l2 as positionName ,  nvl( bdpost.fnumber  ,' ') as TechnicalPost,   ")
/* 4425 */           .append(" positionName.fnumber as zhiwunumber ,nvl( jobGrade.fnumber,' ') as jobGradeNumber  , ' ' as fentanTypeNumber  , ' ' as guanbaoTypeNumber  , ' ' as empTypeNumber , to_char( empOrgRelation.FEFFDT ,'yyyy-mm-dd hh24:mi:ss') as empDate,  ")
/* 4426 */           .append(" nvl(  to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss') ,nvl(to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss'),' ') ) as empzhuanzhengDate  , to_char(empOrgRelation.Fleffdt,'yyyy-mm-dd hh24:mi:ss') as empleftdate  ,nvl( employeeModle.fnumber,' ') as empModelNumber  ,(case employeeContract.FNewState when 0 then  '01'  when 1 then  '01'  when 2 then  '02' else ' ' end ) as hetongtype ,   ")
/* 4427 */           .append(" nvl( to_char(employeeContract.FEffectDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  EffectDate , nvl( to_char(employeeContract.FEndDate,'yyyy-mm-dd hh24:mi:ss'),' ') as EndDate , nvl(person.FIDCardNO,' ') as  IDCardNO,nvl(  person.FHjAddress,' ') as HjAddress ,   ")
/* 4428 */           .append("  (case person.FGender when 1 then  'F'  when 2 then  'M'  ELSE  ' '  end )  as Gender , nvl(  to_char( person.FBirthday,'yyyy-mm-dd hh24:mi:ss'),' ') as Birthday ,nvl( hrWed.FNAME_l2,' ') as wefName ,nvl( hrFolk.FNAME_l2,' ') as folkName ,nvl(hrPolitical.fname_l2,' ') as PoliticalName ,  ")
/* 4429 */           .append(" nvl(personContactMethod.FADDRESS,' ')  as faddress , nvl( personContactMethod.FEmail,' ') as Email , nvl( personContactMethod.FMobile ,' ') as Mobile , nvl( personContactMethod.FLinkName ,' ') as LinkName , nvl( personContactMethod.FLinkTelNum ,' ') as LinkTelNum ,  ")
/* 4430 */           .append(" nvl( hrDiploma.fname_l2 ,' ') as DiplomaName , nvl(personDegree.FGraduateSchool,' ') as  GraduateSchool ,  nvl(personDegree.FSpecialty,' ') as  Specialty ,  nvl(to_char(personDegree.FGraduateDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  GraduateDate ,  ")
/* 4431 */           .append(" nvl(person.FIdNum,' ') as idNum ,  nvl(person.FRTX,' ')  as RTX , nvl( employeeContract.FCONTRACTNO ,' ') as CONTRACTNO , nvl( to_char(personCertifyCompetency.FObtainDate,'yyyy-mm-dd hh24:mi:ss'),' ') as ObtainDate , nvl(to_char(personWorkExp.FBeginDate ,'yyyy-mm-dd hh24:mi:ss'),' ') as firstWorkTime,  ")
/* 4432 */           .append("  ' ' as insuredCityNumber , nvl(regpermresidence.fname_l2,' ') as  hujiName , ' ' as  gongjijinNum ,nvl( entry.FDescription,' ') as Remarks  ")
/* 4433 */           .append(" ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign ")
/* 4434 */           .append(" FROM  T_HR_PluralityAddBizBill  bill   ")
/* 4435 */           .append(" left join  T_HR_PluralityAddBizBillEntry   entry  on entry.FBILLID = bill.fid ")
/* 4436 */           .append(" left join   T_BD_Person  person    on entry.FPersonID = person.fid ")
/* 4437 */           .append(" left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid  and entry.FBizDate = empOrgRelation.FEFFDT ")
/* 4438 */           .append(" LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid ")
/* 4439 */           .append(" left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID  ")
/* 4440 */           .append(" left join  T_ORG_Admin  admin1  on entry.FAdminOrgID  = admin1.fid ")
/* 4441 */           .append(" left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid ")
/* 4442 */           .append(" left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid ")
/* 4443 */           .append(" left join  T_ORG_Admin  admin4  on admin3.FPARENTID = admin4.fid ")
/* 4444 */           .append(" left join  T_ORG_Admin  admin5  on admin4.FPARENTID = admin5.fid   ")
/* 4445 */           .append(" left join  T_ORG_Position  position   on  entry.FPositionID = position.fid ")
/* 4446 */           .append(" left join  T_ORG_Job  positionName   on  position.FJobID =positionName.fid ")
/* 4447 */           .append(" left join  T_HR_JobGrade  jobGrade   on  empOrgRelation.FJobGradeID =jobGrade.fid ")
/* 4448 */           .append(" left join  T_HR_EmpLaborRelation  empLaborRelation   on  empLaborRelation.fpersonid =person.fid ")
/* 4449 */           .append(" left join  T_HR_EmpPostExperienceHis  empPostExperienceHis   on  empPostExperienceHis.fpersonid =person.fid ")
/* 4450 */           .append(" left join  T_BD_EmployeeModle  employeeModle   on  empLaborRelation.FEmployeeModleID =employeeModle.fid ")
/* 4451 */           .append(" left join  T_HR_EmployeeContract employeeContract   on  employeeContract.FEmployeeID =person.fid   ")
/* 4452 */           .append(" left join  T_BD_HRWed hrWed   on  hrWed.fid =person.FWedID ")
/* 4453 */           .append(" left join  T_BD_HRFolk hrFolk   on  hrFolk.fid =person.FFolkID ")
/* 4454 */           .append(" left join  T_BD_HRPolitical  hrPolitical   on  hrPolitical.fid =person.FPoliticalFaceID ")
/* 4455 */           .append(" left join  T_HR_PersonContactMethod  personContactMethod   on  person.fid =personContactMethod.fpersonid ")
/* 4456 */           .append(" left join  T_HR_PersonDegree personDegree   on  person.fid = personDegree.fpersonid ")
/* 4457 */           .append(" left join  T_BD_HRDiploma  hrDiploma   on  hrDiploma.fid = personDegree.FDIPLOMA ")
/* 4458 */           .append(" left join  T_HR_PersonCertifyCompetency  personCertifyCompetency   on  person.fid = personCertifyCompetency.FPERSONID  ")
/* 4459 */           .append(" left join  T_HR_PersonWorkExp  personWorkExp   on  person.fid = personWorkExp.FPERSONID  ")
/* 4460 */           .append(" left join  T_BD_Regpermresidence  regpermresidence   on  regpermresidence.fid = person.FRegresidenceID ")
/* 4461 */           .append(" left join  T_HR_PersonTechPost  psot    on  psot.fpersonid =person.fid  and  FISHIGHTECHNICAL = 1 ")
/* 4462 */           .append(" left join T_HR_BDTechnicalPost  bdpost  on  psot.FTECHNICALPOSTID  =bdpost.fid  ")
/* 4463 */           .append("  where  bill.fbillstate = 3      and  EmpOrgRelation.FActionID ='DawAAAApaaPmaL7Z'    and admin1.FLEVEL =5 ")
/*      */ 
/*      */           
/* 4466 */           .append("  order by  person.fnumber    ");
/*      */       }
/* 4468 */       else if (sqlType == 2) {
/* 4469 */         sql.append(" /*dialect*/ SELECT distinct  to_char(entry.FBizDate,'yyyy-mm-dd hh24:mi:ss') as changesDate , admin4.fnumber as groupNumber ,admin3.fnumber as daquNumber , ' ' as cityNumber,admin2.fnumber as companyNumber,admin1.fnumber as deptNumber , ")
/* 4470 */           .append(" person.FNumber as personnumber,person.fname_l2 as fname ,position.Fnumber as positionNumber , position.fname_l2 as positionName ,  nvl( bdpost.fnumber  ,' ') as TechnicalPost,   ")
/* 4471 */           .append(" positionName.fnumber as zhiwunumber ,nvl( jobGrade.fnumber,' ') as jobGradeNumber  , ' ' as fentanTypeNumber  , ' ' as guanbaoTypeNumber  , ' ' as empTypeNumber , to_char( empOrgRelation.FEFFDT ,'yyyy-mm-dd hh24:mi:ss') as empDate,  ")
/* 4472 */           .append(" nvl(  to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss') ,nvl(to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss'),' ') ) as empzhuanzhengDate  ,  to_char(empOrgRelation.Fleffdt,'yyyy-mm-dd hh24:mi:ss') as empleftdate  ,nvl( employeeModle.fnumber,' ') as empModelNumber  ,(case employeeContract.FNewState when 0 then  '01'  when 1 then  '01'  when 2 then  '02' else ' ' end ) as hetongtype ,   ")
/* 4473 */           .append(" nvl( to_char(employeeContract.FEffectDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  EffectDate , nvl( to_char(employeeContract.FEndDate,'yyyy-mm-dd hh24:mi:ss'),' ') as EndDate , nvl(person.FIDCardNO,' ') as  IDCardNO,nvl(  person.FHjAddress,' ') as HjAddress ,   ")
/* 4474 */           .append("  (case person.FGender when 1 then  'F'  when 2 then  'M'  ELSE  ' '  end )  as Gender , nvl(  to_char( person.FBirthday,'yyyy-mm-dd hh24:mi:ss'),' ') as Birthday ,nvl( hrWed.FNAME_l2,' ') as wefName ,nvl( hrFolk.FNAME_l2,' ') as folkName ,nvl(hrPolitical.fname_l2,' ') as PoliticalName ,  ")
/* 4475 */           .append(" nvl(personContactMethod.FADDRESS,' ')  as faddress , nvl( personContactMethod.FEmail,' ') as Email , nvl( personContactMethod.FMobile ,' ') as Mobile , nvl( personContactMethod.FLinkName ,' ') as LinkName ,  nvl( personContactMethod.FLinkTelNum ,' ') as LinkTelNum ,  ")
/* 4476 */           .append(" nvl( hrDiploma.fname_l2 ,' ') as DiplomaName , nvl(personDegree.FGraduateSchool,' ') as  GraduateSchool ,  nvl(personDegree.FSpecialty,' ') as  Specialty ,  nvl(to_char(personDegree.FGraduateDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  GraduateDate ,  ")
/* 4477 */           .append(" nvl(person.FIdNum,' ') as idNum ,  nvl(person.FRTX,' ')  as RTX , nvl( employeeContract.FCONTRACTNO ,' ') as CONTRACTNO , nvl( to_char(personCertifyCompetency.FObtainDate,'yyyy-mm-dd hh24:mi:ss'),' ') as ObtainDate , nvl(to_char(personWorkExp.FBeginDate ,'yyyy-mm-dd hh24:mi:ss'),' ') as firstWorkTime,  ")
/* 4478 */           .append("  ' ' as insuredCityNumber , nvl(regpermresidence.fname_l2,' ') as  hujiName , ' ' as  gongjijinNum ,nvl( entry.FDescription,' ') as Remarks  ")
/* 4479 */           .append(" ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign ")
/* 4480 */           .append(" FROM  T_HR_PluralityAddBizBill  bill   ")
/* 4481 */           .append(" left join  T_HR_PluralityAddBizBillEntry   entry  on entry.FBILLID = bill.fid ")
/* 4482 */           .append(" left join   T_BD_Person  person    on entry.FPersonID = person.fid ")
/* 4483 */           .append(" left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid  and entry.FBizDate = empOrgRelation.FEFFDT ")
/* 4484 */           .append(" LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid ")
/* 4485 */           .append(" left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID  ")
/* 4486 */           .append(" left join  T_ORG_Admin  admin1  on entry.FAdminOrgID  = admin1.fid ")
/* 4487 */           .append(" left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid ")
/* 4488 */           .append(" left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid ")
/* 4489 */           .append(" left join  T_ORG_Admin  admin4  on admin3.FPARENTID = admin4.fid ")
/* 4490 */           .append(" left join  T_ORG_Position  position   on  entry.FPositionID = position.fid ")
/* 4491 */           .append(" left join  T_ORG_Job  positionName   on  position.FJobID =positionName.fid ")
/* 4492 */           .append(" left join  T_HR_JobGrade  jobGrade   on  empOrgRelation.FJobGradeID =jobGrade.fid ")
/* 4493 */           .append(" left join  T_HR_EmpLaborRelation  empLaborRelation   on  empLaborRelation.fpersonid =person.fid ")
/* 4494 */           .append(" left join  T_HR_EmpPostExperienceHis  empPostExperienceHis   on  empPostExperienceHis.fpersonid =person.fid ")
/* 4495 */           .append(" left join  T_BD_EmployeeModle  employeeModle   on  empLaborRelation.FEmployeeModleID =employeeModle.fid ")
/* 4496 */           .append(" left join  T_HR_EmployeeContract employeeContract   on  employeeContract.FEmployeeID =person.fid   ")
/* 4497 */           .append(" left join  T_BD_HRWed hrWed   on  hrWed.fid =person.FWedID ")
/* 4498 */           .append(" left join  T_BD_HRFolk hrFolk   on  hrFolk.fid =person.FFolkID ")
/* 4499 */           .append(" left join  T_BD_HRPolitical  hrPolitical   on  hrPolitical.fid =person.FPoliticalFaceID ")
/* 4500 */           .append(" left join  T_HR_PersonContactMethod  personContactMethod   on  person.fid =personContactMethod.fpersonid ")
/* 4501 */           .append(" left join  T_HR_PersonDegree personDegree   on  person.fid = personDegree.fpersonid ")
/* 4502 */           .append(" left join  T_BD_HRDiploma  hrDiploma   on  hrDiploma.fid = personDegree.FDIPLOMA ")
/* 4503 */           .append(" left join  T_HR_PersonCertifyCompetency  personCertifyCompetency   on  person.fid = personCertifyCompetency.FPERSONID  ")
/* 4504 */           .append(" left join  T_HR_PersonWorkExp  personWorkExp   on  person.fid = personWorkExp.FPERSONID  ")
/* 4505 */           .append(" left join  T_BD_Regpermresidence  regpermresidence   on  regpermresidence.fid = person.FRegresidenceID ")
/* 4506 */           .append(" left join  T_HR_PersonTechPost  psot    on  psot.fpersonid =person.fid  and  FISHIGHTECHNICAL = 1 ")
/* 4507 */           .append(" left join T_HR_BDTechnicalPost  bdpost  on  psot.FTECHNICALPOSTID  =bdpost.fid  ")
/* 4508 */           .append("  where  bill.fbillstate = 3      and  EmpOrgRelation.FActionID ='DawAAAApaaPmaL7Z'    and admin1.FLEVEL =4  and   admin3.fnumber = 'glzb'   ")         
/* 4511 */           .append("  order by  person.fnumber    ");
/*      */       
/*      */       }
/* 4514 */       else if (sqlType == 3) {
/* 4515 */         sql.append(" /*dialect*/ SELECT distinct to_char(entry.FBizDate,'yyyy-mm-dd hh24:mi:ss') as changesDate ,admin4.fnumber as groupNumber ,admin3.fnumber as daquNumber , admin2.fnumber  as cityNumber,admin1.fnumber as companyNumber, ' ' as deptNumber , ")
/* 4516 */           .append(" person.FNumber as personnumber,person.fname_l2 as fname ,position.Fnumber as positionNumber , position.fname_l2 as positionName ,  nvl( bdpost.fnumber  ,' ') as TechnicalPost,   ")
/* 4517 */           .append(" positionName.fnumber as zhiwunumber ,nvl( jobGrade.fnumber,' ') as jobGradeNumber  , ' ' as fentanTypeNumber  , ' ' as guanbaoTypeNumber  , ' ' as empTypeNumber , to_char( empOrgRelation.FEFFDT ,'yyyy-mm-dd hh24:mi:ss') as empDate,  ")
/* 4518 */           .append(" nvl(  to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss') ,nvl(to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss'),' ') ) as empzhuanzhengDate  , to_char(empOrgRelation.Fleffdt,'yyyy-mm-dd hh24:mi:ss') as empleftdate  ,nvl( employeeModle.fnumber,' ') as empModelNumber  ,(case employeeContract.FNewState when 0 then  '01'  when 1 then  '01'  when 2 then  '02' else ' ' end ) as hetongtype ,   ")
/* 4519 */           .append(" nvl( to_char(employeeContract.FEffectDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  EffectDate , nvl( to_char(employeeContract.FEndDate,'yyyy-mm-dd hh24:mi:ss'),' ') as EndDate , nvl(person.FIDCardNO,' ') as  IDCardNO,nvl(  person.FHjAddress,' ') as HjAddress ,   ")
/* 4520 */           .append("  (case person.FGender when 1 then  'F'  when 2 then  'M'  ELSE  ' '  end )  as Gender , nvl(  to_char( person.FBirthday,'yyyy-mm-dd hh24:mi:ss'),' ') as Birthday ,nvl( hrWed.FNAME_l2,' ') as wefName ,nvl( hrFolk.FNAME_l2,' ') as folkName ,nvl(hrPolitical.fname_l2,' ') as PoliticalName ,  ")
/* 4521 */           .append(" nvl(personContactMethod.FADDRESS,' ')  as faddress , nvl( personContactMethod.FEmail,' ') as Email , nvl( personContactMethod.FMobile ,' ') as Mobile , nvl( personContactMethod.FLinkName ,' ') as LinkName , nvl( personContactMethod.FLinkTelNum ,' ') as LinkTelNum ,  ")
/* 4522 */           .append(" nvl( hrDiploma.fname_l2 ,' ') as DiplomaName , nvl(personDegree.FGraduateSchool,' ') as  GraduateSchool ,  nvl(personDegree.FSpecialty,' ') as  Specialty ,  nvl(to_char(personDegree.FGraduateDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  GraduateDate ,  ")
/* 4523 */           .append(" nvl(person.FIdNum,' ') as idNum ,  nvl(person.FRTX,' ')  as RTX , nvl( employeeContract.FCONTRACTNO ,' ') as CONTRACTNO , nvl( to_char(personCertifyCompetency.FObtainDate,'yyyy-mm-dd hh24:mi:ss'),' ') as ObtainDate , nvl(to_char(personWorkExp.FBeginDate ,'yyyy-mm-dd hh24:mi:ss'),' ') as firstWorkTime,  ")
/* 4524 */           .append("  ' ' as insuredCityNumber , nvl(regpermresidence.fname_l2,' ') as  hujiName , ' ' as  gongjijinNum ,nvl( entry.FDescription,' ') as Remarks  ")
/* 4525 */           .append(" ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign ")
/* 4526 */           .append(" FROM  T_HR_PluralityAddBizBill  bill   ")
/* 4527 */           .append(" left join  T_HR_PluralityAddBizBillEntry   entry  on entry.FBILLID = bill.fid ")
/* 4528 */           .append(" left join   T_BD_Person  person    on entry.FPersonID = person.fid ")
/* 4529 */           .append(" left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid  and entry.FBizDate = empOrgRelation.FEFFDT ")
/* 4530 */           .append(" LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid ")
/* 4531 */           .append(" left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID  ")
/* 4532 */           .append(" left join  T_ORG_Admin  admin1  on entry.FAdminOrgID  = admin1.fid ")
/* 4533 */           .append(" left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid ")
/* 4534 */           .append(" left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid ")
/* 4535 */           .append(" left join  T_ORG_Admin  admin4  on admin3.FPARENTID = admin4.fid ")
/* 4536 */           .append(" left join  T_ORG_Position  position   on  entry.FPositionID = position.fid ")
/* 4537 */           .append(" left join  T_ORG_Job  positionName   on  position.FJobID =positionName.fid ")
/* 4538 */           .append(" left join  T_HR_JobGrade  jobGrade   on  empOrgRelation.FJobGradeID =jobGrade.fid ")
/* 4539 */           .append(" left join  T_HR_EmpLaborRelation  empLaborRelation   on  empLaborRelation.fpersonid =person.fid ")
/* 4540 */           .append(" left join  T_HR_EmpPostExperienceHis  empPostExperienceHis   on  empPostExperienceHis.fpersonid =person.fid ")
/* 4541 */           .append(" left join  T_BD_EmployeeModle  employeeModle   on  empLaborRelation.FEmployeeModleID =employeeModle.fid ")
/* 4542 */           .append(" left join  T_HR_EmployeeContract employeeContract   on  employeeContract.FEmployeeID =person.fid   ")
/* 4543 */           .append(" left join  T_BD_HRWed hrWed   on  hrWed.fid =person.FWedID ")
/* 4544 */           .append(" left join  T_BD_HRFolk hrFolk   on  hrFolk.fid =person.FFolkID ")
/* 4545 */           .append(" left join  T_BD_HRPolitical  hrPolitical   on  hrPolitical.fid =person.FPoliticalFaceID ")
/* 4546 */           .append(" left join  T_HR_PersonContactMethod  personContactMethod   on  person.fid =personContactMethod.fpersonid ")
/* 4547 */           .append(" left join  T_HR_PersonDegree personDegree   on  person.fid = personDegree.fpersonid ")
/* 4548 */           .append(" left join  T_BD_HRDiploma  hrDiploma   on  hrDiploma.fid = personDegree.FDIPLOMA ")
/* 4549 */           .append(" left join  T_HR_PersonCertifyCompetency  personCertifyCompetency   on  person.fid = personCertifyCompetency.FPERSONID  ")
/* 4550 */           .append(" left join  T_HR_PersonWorkExp  personWorkExp   on  person.fid = personWorkExp.FPERSONID  ")
/* 4551 */           .append(" left join  T_BD_Regpermresidence  regpermresidence   on  regpermresidence.fid = person.FRegresidenceID ")
/* 4552 */           .append(" left join  T_HR_PersonTechPost  psot    on  psot.fpersonid =person.fid  and  FISHIGHTECHNICAL = 1 ")
/* 4553 */           .append(" left join T_HR_BDTechnicalPost  bdpost  on  psot.FTECHNICALPOSTID  =bdpost.fid  ")
/* 4554 */           .append("  where  bill.fbillstate = 3      and  EmpOrgRelation.FActionID ='DawAAAApaaPmaL7Z'    and admin1.FLEVEL =4  and   admin3.fnumber  != 'glzb'     ")
/*      */ 
/*      */           
/* 4557 */           .append("  order by  person.fnumber    ");
/*      */       } 
/* 4559 */     } else if (yidongType == 2) {
/* 4560 */       if (sqlType == 1) {
/* 4561 */         sql.append(" /*dialect*/ SELECT   distinct to_char(entry.FBizDate,'yyyy-mm-dd hh24:mi:ss') as changesDate , admin5.fnumber as groupNumber ,admin4.fnumber as daquNumber ,admin3.fnumber as cityNumber,admin2.fnumber as companyNumber,admin1.fnumber as deptNumber , ")
/* 4562 */           .append(" person.FNumber as personnumber,person.fname_l2 as fname ,position.Fnumber as positionNumber , position.fname_l2 as positionName ,  nvl( bdpost.fnumber  ,' ') as TechnicalPost,   ")
/* 4563 */           .append(" positionName.fnumber as zhiwunumber ,nvl( jobGrade.fnumber,' ') as jobGradeNumber  , ' ' as fentanTypeNumber  , ' ' as guanbaoTypeNumber  , ' ' as empTypeNumber , to_char( empOrgRelation.FEFFDT ,'yyyy-mm-dd hh24:mi:ss') as empDate,  ")
/* 4564 */           .append(" nvl(  to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss') ,nvl(to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss'),' ') ) as empzhuanzhengDate  , to_char(empOrgRelation.Fleffdt,'yyyy-mm-dd hh24:mi:ss') as empleftdate  ,nvl( employeeModle.fnumber,' ') as empModelNumber  ,(case employeeContract.FNewState when 0 then  '01'  when 1 then  '01'  when 2 then  '02' else ' ' end ) as hetongtype ,   ")
/* 4565 */           .append(" nvl( to_char(employeeContract.FEffectDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  EffectDate , nvl( to_char(employeeContract.FEndDate,'yyyy-mm-dd hh24:mi:ss'),' ') as EndDate , nvl(person.FIDCardNO,' ') as  IDCardNO,nvl(  person.FHjAddress,' ') as HjAddress ,   ")
/* 4566 */           .append("  (case person.FGender when 1 then  'F'  when 2 then  'M'  ELSE  ' '  end )  as Gender , nvl(  to_char( person.FBirthday,'yyyy-mm-dd hh24:mi:ss'),' ') as Birthday ,nvl( hrWed.FNAME_l2,' ') as wefName ,nvl( hrFolk.FNAME_l2,' ') as folkName ,nvl(hrPolitical.fname_l2,' ') as PoliticalName ,  ")
/* 4567 */           .append(" nvl(personContactMethod.FADDRESS,' ')  as faddress , nvl( personContactMethod.FEmail,' ') as Email , nvl( personContactMethod.FMobile ,' ') as Mobile , nvl( personContactMethod.FLinkName ,' ') as LinkName ,  nvl( personContactMethod.FLinkTelNum ,' ') as LinkTelNum , ")
/* 4568 */           .append(" nvl( hrDiploma.fname_l2 ,' ') as DiplomaName , nvl(personDegree.FGraduateSchool,' ') as  GraduateSchool ,  nvl(personDegree.FSpecialty,' ') as  Specialty ,  nvl(to_char(personDegree.FGraduateDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  GraduateDate ,  ")
/* 4569 */           .append(" nvl(person.FIdNum,' ') as idNum ,  nvl(person.FRTX,' ')  as RTX , nvl( employeeContract.FCONTRACTNO ,' ') as CONTRACTNO , nvl( to_char(personCertifyCompetency.FObtainDate,'yyyy-mm-dd hh24:mi:ss'),' ') as ObtainDate , nvl(to_char(personWorkExp.FBeginDate ,'yyyy-mm-dd hh24:mi:ss'),' ') as firstWorkTime,  ")
/* 4570 */           .append("  ' ' as insuredCityNumber , nvl(regpermresidence.fname_l2,' ') as  hujiName , ' ' as  gongjijinNum ,nvl( entry.FDescription,' ') as Remarks  ")
/* 4571 */           .append(" ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign ")
/* 4572 */           .append(" FROM  T_HR_EmpHireBizBill  bill   ")
/* 4573 */           .append(" left join  T_HR_EmpHireBizBillEntry   entry  on entry.FBILLID = bill.fid ")
/* 4574 */           .append(" left join   T_BD_Person  person    on entry.FPersonID = person.fid ")
/* 4575 */           .append(" left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid   and  empOrgRelation.FFlowInAffairID = entry.fid ")
/* 4576 */           .append(" LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid ")
/* 4577 */           .append(" left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID  ")
/* 4578 */           .append(" left join  T_ORG_Admin  admin1  on entry.FAdminOrgID  = admin1.fid ")
/* 4579 */           .append(" left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid ")
/* 4580 */           .append(" left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid ")
/* 4581 */           .append(" left join  T_ORG_Admin  admin4  on admin3.FPARENTID = admin4.fid ")
/* 4582 */           .append(" left join  T_ORG_Admin  admin5  on admin4.FPARENTID = admin5.fid   ")
/* 4583 */           .append(" left join  T_ORG_Position  position   on  entry.FPositionID = position.fid ")
/* 4584 */           .append(" left join  T_ORG_Job  positionName   on  position.FJobID =positionName.fid ")
/* 4585 */           .append(" left join  T_HR_JobGrade  jobGrade   on  empOrgRelation.FJobGradeID =jobGrade.fid ")
/* 4586 */           .append(" left join  T_HR_EmpLaborRelation  empLaborRelation   on  empLaborRelation.fpersonid =person.fid ")
/* 4587 */           .append(" left join  T_HR_EmpPostExperienceHis  empPostExperienceHis   on  empPostExperienceHis.fpersonid =person.fid ")
/* 4588 */           .append(" left join  T_BD_EmployeeModle  employeeModle   on  empLaborRelation.FEmployeeModleID =employeeModle.fid ")
/* 4589 */           .append(" left join  T_HR_EmployeeContract employeeContract   on  employeeContract.FEmployeeID =person.fid   ")
/* 4590 */           .append(" left join  T_BD_HRWed hrWed   on  hrWed.fid =person.FWedID ")
/* 4591 */           .append(" left join  T_BD_HRFolk hrFolk   on  hrFolk.fid =person.FFolkID ")
/* 4592 */           .append(" left join  T_BD_HRPolitical  hrPolitical   on  hrPolitical.fid =person.FPoliticalFaceID ")
/* 4593 */           .append(" left join  T_HR_PersonContactMethod  personContactMethod   on  person.fid =personContactMethod.fpersonid ")
/* 4594 */           .append(" left join  T_HR_PersonDegree personDegree   on  person.fid = personDegree.fpersonid  and  personDegree.FIsHighestDegree  = 1 ")
/* 4595 */           .append(" left join  T_BD_HRDiploma  hrDiploma   on  hrDiploma.fid = personDegree.FDIPLOMA ")
/* 4596 */           .append(" left join  T_HR_PersonCertifyCompetency  personCertifyCompetency   on  person.fid = personCertifyCompetency.FPERSONID  ")
/* 4597 */           .append(" left join  T_HR_PersonWorkExp  personWorkExp   on  person.fid = personWorkExp.FPERSONID  ")
/* 4598 */           .append(" left join  T_BD_Regpermresidence  regpermresidence   on  regpermresidence.fid = person.FRegresidenceID ")
/* 4599 */           .append(" left join  T_HR_PersonTechPost  psot    on  psot.fpersonid =person.fid  and  FISHIGHTECHNICAL = 1 ")
/* 4600 */           .append(" left join T_HR_BDTechnicalPost  bdpost  on  psot.FTECHNICALPOSTID  =bdpost.fid  ")
/* 4601 */           .append("  where bill.fbillstate = 3   ")
/*      */ 
/*      */           
/* 4604 */           .append(" and admin1.FLEVEL = 5   order by  person.fnumber    ");
/*      */       }
/* 4606 */       else if (sqlType == 2) {
/* 4607 */         sql.append(" /*dialect*/ SELECT distinct  to_char(entry.FBizDate,'yyyy-mm-dd hh24:mi:ss') as changesDate , admin4.fnumber as groupNumber ,admin3.fnumber as daquNumber , ' ' as cityNumber,admin2.fnumber as companyNumber,admin1.fnumber as deptNumber , ")
/* 4608 */           .append(" person.FNumber as personnumber,person.fname_l2 as fname ,position.Fnumber as positionNumber , position.fname_l2 as positionName ,  nvl( bdpost.fnumber  ,' ') as TechnicalPost,   ")
/* 4609 */           .append(" positionName.fnumber as zhiwunumber ,nvl( jobGrade.fnumber,' ') as jobGradeNumber  , ' ' as fentanTypeNumber  , ' ' as guanbaoTypeNumber  , ' ' as empTypeNumber , to_char( empOrgRelation.FEFFDT ,'yyyy-mm-dd hh24:mi:ss') as empDate,  ")
/* 4610 */           .append(" nvl(  to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss') ,nvl(to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss'),' ') ) as empzhuanzhengDate  , to_char(empOrgRelation.Fleffdt,'yyyy-mm-dd hh24:mi:ss') as empleftdate  ,nvl( employeeModle.fnumber,' ') as empModelNumber  ,(case employeeContract.FNewState when 0 then  '01'  when 1 then  '01'  when 2 then  '02' else ' ' end ) as hetongtype ,   ")
/* 4611 */           .append(" nvl( to_char(employeeContract.FEffectDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  EffectDate , nvl( to_char(employeeContract.FEndDate,'yyyy-mm-dd hh24:mi:ss'),' ') as EndDate , nvl(person.FIDCardNO,' ') as  IDCardNO,nvl(  person.FHjAddress,' ') as HjAddress ,   ")
/* 4612 */           .append("  (case person.FGender when 1 then  'F'  when 2 then  'M'  ELSE  ' '  end )  as Gender , nvl(  to_char( person.FBirthday,'yyyy-mm-dd hh24:mi:ss'),' ') as Birthday ,nvl( hrWed.FNAME_l2,' ') as wefName ,nvl( hrFolk.FNAME_l2,' ') as folkName ,nvl(hrPolitical.fname_l2,' ') as PoliticalName ,  ")
/* 4613 */           .append(" nvl(personContactMethod.FADDRESS,' ')  as faddress , nvl( personContactMethod.FEmail,' ') as Email , nvl( personContactMethod.FMobile ,' ') as Mobile , nvl( personContactMethod.FLinkName ,' ') as LinkName ,  nvl( personContactMethod.FLinkTelNum ,' ') as LinkTelNum , ")
/* 4614 */           .append(" nvl( hrDiploma.fname_l2 ,' ') as DiplomaName , nvl(personDegree.FGraduateSchool,' ') as  GraduateSchool ,  nvl(personDegree.FSpecialty,' ') as  Specialty ,  nvl(to_char(personDegree.FGraduateDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  GraduateDate ,  ")
/* 4615 */           .append(" nvl(person.FIdNum,' ') as idNum ,  nvl(person.FRTX,' ')  as RTX , nvl( employeeContract.FCONTRACTNO ,' ') as CONTRACTNO , nvl( to_char(personCertifyCompetency.FObtainDate,'yyyy-mm-dd hh24:mi:ss'),' ') as ObtainDate , nvl(to_char(personWorkExp.FBeginDate ,'yyyy-mm-dd hh24:mi:ss'),' ') as firstWorkTime,  ")
/* 4616 */           .append("  ' ' as insuredCityNumber , nvl(regpermresidence.fname_l2,' ') as  hujiName , ' ' as  gongjijinNum ,nvl( entry.FDescription,' ') as Remarks  ")
/* 4617 */           .append(" ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign ")
/* 4618 */           .append(" FROM  T_HR_EmpHireBizBill  bill   ")
/* 4619 */           .append(" left join  T_HR_EmpHireBizBillEntry   entry  on entry.FBILLID = bill.fid ")
/* 4620 */           .append(" left join   T_BD_Person  person    on entry.FPersonID = person.fid ")
/* 4621 */           .append(" left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid   and  empOrgRelation.FFlowInAffairID = entry.fid ")
/* 4622 */           .append(" LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid ")
/* 4623 */           .append(" left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID  ")
/* 4624 */           .append(" left join  T_ORG_Admin  admin1  on entry.FAdminOrgID  = admin1.fid ")
/* 4625 */           .append(" left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid ")
/* 4626 */           .append(" left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid ")
/* 4627 */           .append(" left join  T_ORG_Admin  admin4  on admin3.FPARENTID = admin4.fid ")
/* 4628 */           .append(" left join  T_ORG_Position  position   on  entry.FPositionID = position.fid ")
/* 4629 */           .append(" left join  T_ORG_Job  positionName   on  position.FJobID =positionName.fid ")
/* 4630 */           .append(" left join  T_HR_JobGrade  jobGrade   on  empOrgRelation.FJobGradeID =jobGrade.fid ")
/* 4631 */           .append(" left join  T_HR_EmpLaborRelation  empLaborRelation   on  empLaborRelation.fpersonid =person.fid ")
/* 4632 */           .append(" left join  T_HR_EmpPostExperienceHis  empPostExperienceHis   on  empPostExperienceHis.fpersonid =person.fid ")
/* 4633 */           .append(" left join  T_BD_EmployeeModle  employeeModle   on  empLaborRelation.FEmployeeModleID =employeeModle.fid ")
/* 4634 */           .append(" left join  T_HR_EmployeeContract employeeContract   on  employeeContract.FEmployeeID =person.fid   ")
/* 4635 */           .append(" left join  T_BD_HRWed hrWed   on  hrWed.fid =person.FWedID ")
/* 4636 */           .append(" left join  T_BD_HRFolk hrFolk   on  hrFolk.fid =person.FFolkID ")
/* 4637 */           .append(" left join  T_BD_HRPolitical  hrPolitical   on  hrPolitical.fid =person.FPoliticalFaceID ")
/* 4638 */           .append(" left join  T_HR_PersonContactMethod  personContactMethod   on  person.fid =personContactMethod.fpersonid ")
/* 4639 */           .append(" left join  T_HR_PersonDegree personDegree   on  person.fid = personDegree.fpersonid  and  personDegree.FIsHighestDegree  = 1 ")
/* 4640 */           .append(" left join  T_BD_HRDiploma  hrDiploma   on  hrDiploma.fid = personDegree.FDIPLOMA ")
/* 4641 */           .append(" left join  T_HR_PersonCertifyCompetency  personCertifyCompetency   on  person.fid = personCertifyCompetency.FPERSONID  ")
/* 4642 */           .append(" left join  T_HR_PersonWorkExp  personWorkExp   on  person.fid = personWorkExp.FPERSONID  ")
/* 4643 */           .append(" left join  T_BD_Regpermresidence  regpermresidence   on  regpermresidence.fid = person.FRegresidenceID ")
/* 4644 */           .append(" left join  T_HR_PersonTechPost  psot    on  psot.fpersonid =person.fid  and  FISHIGHTECHNICAL = 1 ")
/* 4645 */           .append(" left join T_HR_BDTechnicalPost  bdpost  on  psot.FTECHNICALPOSTID  =bdpost.fid  ")
/* 4646 */           .append("  where bill.fbillstate = 3    and admin1.FLEVEL = 4  and admin3.FNUMBER  = 'glzb' ")
/*      */ 
/*      */           
/* 4649 */           .append("  order by  person.fnumber   ");
/*      */       }
/* 4651 */       else if (sqlType == 3) {
/* 4652 */         sql.append(" /*dialect*/ SELECT distinct to_char(entry.FBizDate,'yyyy-mm-dd hh24:mi:ss') as changesDate ,admin4.fnumber as groupNumber ,admin3.fnumber as daquNumber , admin2.fnumber  as cityNumber,admin1.fnumber as companyNumber, ' ' as deptNumber , ")
/* 4653 */           .append(" person.FNumber as personnumber,person.fname_l2 as fname ,position.Fnumber as positionNumber , position.fname_l2 as positionName ,  nvl( bdpost.fnumber  ,' ') as TechnicalPost,   ")
/* 4654 */           .append(" positionName.fnumber as zhiwunumber ,nvl( jobGrade.fnumber,' ') as jobGradeNumber  , ' ' as fentanTypeNumber  , ' ' as guanbaoTypeNumber  , ' ' as empTypeNumber , to_char( empOrgRelation.FEFFDT ,'yyyy-mm-dd hh24:mi:ss') as empDate,  ")
/* 4655 */           .append(" nvl(  to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss') ,nvl(to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss'),' ') ) as empzhuanzhengDate  ,  to_char(empOrgRelation.Fleffdt,'yyyy-mm-dd hh24:mi:ss') as empleftdate  ,nvl( employeeModle.fnumber,' ') as empModelNumber  ,(case employeeContract.FNewState when 0 then  '01'  when 1 then  '01'  when 2 then  '02' else ' ' end ) as hetongtype ,   ")
/* 4656 */           .append(" nvl( to_char(employeeContract.FEffectDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  EffectDate , nvl( to_char(employeeContract.FEndDate,'yyyy-mm-dd hh24:mi:ss'),' ') as EndDate , nvl(person.FIDCardNO,' ') as  IDCardNO,nvl(  person.FHjAddress,' ') as HjAddress ,   ")
/* 4657 */           .append("  (case person.FGender when 1 then  'F'  when 2 then  'M'  ELSE  ' '  end )  as Gender , nvl(  to_char( person.FBirthday,'yyyy-mm-dd hh24:mi:ss'),' ') as Birthday ,nvl( hrWed.FNAME_l2,' ') as wefName ,nvl( hrFolk.FNAME_l2,' ') as folkName ,nvl(hrPolitical.fname_l2,' ') as PoliticalName ,  ")
/* 4658 */           .append(" nvl(personContactMethod.FADDRESS,' ')  as faddress , nvl( personContactMethod.FEmail,' ') as Email , nvl( personContactMethod.FMobile ,' ') as Mobile , nvl( personContactMethod.FLinkName ,' ') as LinkName ,   nvl( personContactMethod.FLinkTelNum ,' ') as LinkTelNum ,  ")
/* 4659 */           .append(" nvl( hrDiploma.fname_l2 ,' ') as DiplomaName , nvl(personDegree.FGraduateSchool,' ') as  GraduateSchool ,  nvl(personDegree.FSpecialty,' ') as  Specialty ,  nvl(to_char(personDegree.FGraduateDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  GraduateDate ,  ")
/* 4660 */           .append(" nvl(person.FIdNum,' ') as idNum ,  nvl(person.FRTX,' ')  as RTX , nvl( employeeContract.FCONTRACTNO ,' ') as CONTRACTNO , nvl( to_char(personCertifyCompetency.FObtainDate,'yyyy-mm-dd hh24:mi:ss'),' ') as ObtainDate , nvl(to_char(personWorkExp.FBeginDate ,'yyyy-mm-dd hh24:mi:ss'),' ') as firstWorkTime,  ")
/* 4661 */           .append("  ' ' as insuredCityNumber , nvl(regpermresidence.fname_l2,' ') as  hujiName , ' ' as  gongjijinNum ,nvl( entry.FDescription,' ') as Remarks  ")
/* 4662 */           .append(" ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign ")
/* 4663 */           .append(" FROM  T_HR_EmpHireBizBill  bill   ")
/* 4664 */           .append(" left join  T_HR_EmpHireBizBillEntry   entry  on entry.FBILLID = bill.fid ")
/* 4665 */           .append(" left join   T_BD_Person  person    on entry.FPersonID = person.fid ")
/* 4666 */           .append(" left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid  and  empOrgRelation.FFlowInAffairID = entry.fid ")
/* 4667 */           .append(" LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid ")
/* 4668 */           .append(" left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID  ")
/* 4669 */           .append(" left join  T_ORG_Admin  admin1  on entry.FAdminOrgID  = admin1.fid ")
/* 4670 */           .append(" left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid ")
/* 4671 */           .append(" left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid ")
/* 4672 */           .append(" left join  T_ORG_Admin  admin4  on admin3.FPARENTID = admin4.fid ")
/* 4673 */           .append(" left join  T_ORG_Position  position   on  entry.FPositionID = position.fid ")
/* 4674 */           .append(" left join  T_ORG_Job  positionName   on  position.FJobID =positionName.fid ")
/* 4675 */           .append(" left join  T_HR_JobGrade  jobGrade   on  empOrgRelation.FJobGradeID =jobGrade.fid ")
/* 4676 */           .append(" left join  T_HR_EmpLaborRelation  empLaborRelation   on  empLaborRelation.fpersonid =person.fid ")
/* 4677 */           .append(" left join  T_HR_EmpPostExperienceHis  empPostExperienceHis   on  empPostExperienceHis.fpersonid =person.fid ")
/* 4678 */           .append(" left join  T_BD_EmployeeModle  employeeModle   on  empLaborRelation.FEmployeeModleID =employeeModle.fid ")
/* 4679 */           .append(" left join  T_HR_EmployeeContract employeeContract   on  employeeContract.FEmployeeID =person.fid   ")
/* 4680 */           .append(" left join  T_BD_HRWed hrWed   on  hrWed.fid =person.FWedID ")
/* 4681 */           .append(" left join  T_BD_HRFolk hrFolk   on  hrFolk.fid =person.FFolkID ")
/* 4682 */           .append(" left join  T_BD_HRPolitical  hrPolitical   on  hrPolitical.fid =person.FPoliticalFaceID ")
/* 4683 */           .append(" left join  T_HR_PersonContactMethod  personContactMethod   on  person.fid =personContactMethod.fpersonid ")
/* 4684 */           .append(" left join  T_HR_PersonDegree personDegree   on  person.fid = personDegree.fpersonid and  personDegree.FIsHighestDegree  = 1 ")
/* 4685 */           .append(" left join  T_BD_HRDiploma  hrDiploma   on  hrDiploma.fid = personDegree.FDIPLOMA ")
/* 4686 */           .append(" left join  T_HR_PersonCertifyCompetency  personCertifyCompetency   on  person.fid = personCertifyCompetency.FPERSONID  ")
/* 4687 */           .append(" left join  T_HR_PersonWorkExp  personWorkExp   on  person.fid = personWorkExp.FPERSONID  ")
/* 4688 */           .append(" left join  T_BD_Regpermresidence  regpermresidence   on  regpermresidence.fid = person.FRegresidenceID ")
/* 4689 */           .append(" left join  T_HR_PersonTechPost  psot    on  psot.fpersonid =person.fid  and  FISHIGHTECHNICAL = 1 ")
/* 4690 */           .append(" left join T_HR_BDTechnicalPost  bdpost  on  psot.FTECHNICALPOSTID  =bdpost.fid  ")
/* 4691 */           .append("  where bill.fbillstate = 3  and admin1.FLEVEL = 4  and admin3.FNUMBER  != 'glzb'    ")
/*      */ 
/*      */           
/* 4694 */           .append("  order by  person.fnumber    ");
/*      */       }
/*      */     
/* 4697 */     } else if (yidongType == 3) {
/* 4698 */       if (sqlType == 1) {
/* 4699 */         sql.append(" /*dialect*/ SELECT   distinct to_char(entry.FBizDate,'yyyy-mm-dd hh24:mi:ss') as changesDate ,admin5.fnumber as groupNumber ,admin4.fnumber as daquNumber ,admin3.fnumber as cityNumber,admin2.fnumber as companyNumber,admin1.fnumber as deptNumber , ")
/* 4700 */           .append(" person.FNumber as personnumber,person.fname_l2 as fname ,position.Fnumber as positionNumber , position.fname_l2 as positionName ,  nvl( bdpost.fnumber  ,' ') as TechnicalPost,   ")
/* 4701 */           .append(" positionName.fnumber as zhiwunumber ,nvl( jobGrade.fnumber,' ') as jobGradeNumber  , ' ' as fentanTypeNumber  , ' ' as guanbaoTypeNumber  , ' ' as empTypeNumber , to_char( empOrgRelation.FEFFDT ,'yyyy-mm-dd hh24:mi:ss') as empDate,  ")
/* 4702 */           .append(" nvl(  to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss') ,nvl(to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss'),' ') ) as empzhuanzhengDate  ,  to_char(empOrgRelation.Fleffdt,'yyyy-mm-dd hh24:mi:ss') as empleftdate  ,nvl( employeeModle.fnumber,' ') as empModelNumber  ,(case employeeContract.FNewState when 0 then  '01'  when 1 then  '01'  when 2 then  '02' else ' ' end ) as hetongtype ,   ")
/* 4703 */           .append(" nvl( to_char(employeeContract.FEffectDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  EffectDate , nvl( to_char(employeeContract.FEndDate,'yyyy-mm-dd hh24:mi:ss'),' ') as EndDate , nvl(person.FIDCardNO,' ') as  IDCardNO,nvl(  person.FHjAddress,' ') as HjAddress ,   ")
/* 4704 */           .append("  (case person.FGender when 1 then  'F'  when 2 then  'M'  ELSE  ' '  end )  as Gender , nvl(  to_char( person.FBirthday,'yyyy-mm-dd hh24:mi:ss'),' ') as Birthday ,nvl( hrWed.FNAME_l2,' ') as wefName ,nvl( hrFolk.FNAME_l2,' ') as folkName ,nvl(hrPolitical.fname_l2,' ') as PoliticalName ,  ")
/* 4705 */           .append(" nvl(personContactMethod.FADDRESS,' ')  as faddress , nvl( personContactMethod.FEmail,' ') as Email , nvl( personContactMethod.FMobile ,' ') as Mobile , nvl( personContactMethod.FLinkName ,' ') as LinkName ,  nvl( personContactMethod.FLinkTelNum ,' ') as LinkTelNum ,  ")
/* 4706 */           .append(" nvl( hrDiploma.fname_l2 ,' ') as DiplomaName , nvl(personDegree.FGraduateSchool,' ') as  GraduateSchool ,  nvl(personDegree.FSpecialty,' ') as  Specialty ,  nvl(to_char(personDegree.FGraduateDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  GraduateDate ,  ")
/* 4707 */           .append(" nvl(person.FIdNum,' ') as idNum ,  nvl(person.FRTX,' ')  as RTX , nvl( employeeContract.FCONTRACTNO ,' ') as CONTRACTNO , nvl( to_char(personCertifyCompetency.FObtainDate,'yyyy-mm-dd hh24:mi:ss'),' ') as ObtainDate , nvl(to_char(personWorkExp.FBeginDate ,'yyyy-mm-dd hh24:mi:ss'),' ') as firstWorkTime,  ")
/* 4708 */           .append("  ' ' as insuredCityNumber , nvl(regpermresidence.fname_l2,' ') as  hujiName , ' ' as  gongjijinNum ,nvl( entry.FDescription,' ') as Remarks  ")
/* 4709 */           .append(" ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign ")
/* 4710 */           .append(" FROM  T_HR_FluctuationBizBill  bill ")
/* 4711 */           .append(" left join  T_HR_FluctuationBizBillEntry   entry  on entry.FBILLID = bill.fid  ")
/* 4712 */           .append(" left join   T_BD_Person  person    on entry.FPersonID = person.fid ")
/* 4713 */           .append(" left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid  and entry.FBizDate = empOrgRelation.FEFFDT  and entry.FAdminOrgID = empOrgRelation.FADMINORGID ")
/* 4714 */           .append(" LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid ")
/* 4715 */           .append(" left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID  ")
/* 4716 */           .append(" left join  T_ORG_Admin  admin1  on entry.FAdminOrgID  = admin1.fid ")
/* 4717 */           .append(" left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid ")
/* 4718 */           .append(" left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid ")
/* 4719 */           .append(" left join  T_ORG_Admin  admin4  on admin3.FPARENTID = admin4.fid ")
/* 4720 */           .append(" left join  T_ORG_Admin  admin5  on admin4.FPARENTID = admin5.fid   ")
/* 4721 */           .append(" left join  T_ORG_Position  position   on  entry.FPositionID = position.fid ")
/* 4722 */           .append(" left join  T_ORG_Job  positionName   on  position.FJobID =positionName.fid ")
/* 4723 */           .append(" left join  T_HR_JobGrade  jobGrade   on  empOrgRelation.FJobGradeID =jobGrade.fid ")
/* 4724 */           .append(" left join  T_HR_EmpLaborRelation  empLaborRelation   on  empLaborRelation.fpersonid =person.fid ")
/* 4725 */           .append(" left join  T_HR_EmpPostExperienceHis  empPostExperienceHis   on  empPostExperienceHis.fpersonid =person.fid ")
/* 4726 */           .append(" left join  T_BD_EmployeeModle  employeeModle   on  empLaborRelation.FEmployeeModleID =employeeModle.fid ")
/* 4727 */           .append(" left join  T_HR_EmployeeContract employeeContract   on  employeeContract.FEmployeeID =person.fid   ")
/* 4728 */           .append(" left join  T_BD_HRWed hrWed   on  hrWed.fid =person.FWedID ")
/* 4729 */           .append(" left join  T_BD_HRFolk hrFolk   on  hrFolk.fid =person.FFolkID ")
/* 4730 */           .append(" left join  T_BD_HRPolitical  hrPolitical   on  hrPolitical.fid =person.FPoliticalFaceID ")
/* 4731 */           .append(" left join  T_HR_PersonContactMethod  personContactMethod   on  person.fid =personContactMethod.fpersonid ")
/* 4732 */           .append(" left join  T_HR_PersonDegree personDegree   on  person.fid = personDegree.fpersonid ")
/* 4733 */           .append(" left join  T_BD_HRDiploma  hrDiploma   on  hrDiploma.fid = personDegree.FDIPLOMA ")
/* 4734 */           .append(" left join  T_HR_PersonCertifyCompetency  personCertifyCompetency   on  person.fid = personCertifyCompetency.FPERSONID  ")
/* 4735 */           .append(" left join  T_HR_PersonWorkExp  personWorkExp   on  person.fid = personWorkExp.FPERSONID  ")
/* 4736 */           .append(" left join  T_BD_Regpermresidence  regpermresidence   on  regpermresidence.fid = person.FRegresidenceID ")
/* 4737 */           .append(" left join  T_HR_PersonTechPost  psot    on  psot.fpersonid =person.fid  and  FISHIGHTECHNICAL = 1 ")
/* 4738 */           .append(" left join T_HR_BDTechnicalPost  bdpost  on  psot.FTECHNICALPOSTID  =bdpost.fid  ")
/* 4739 */           .append("  where bill.fbillstate = 3     and   empOrgRelation.FActionID = 'DawAAAApVj/maL7Z' ")
/*      */ 
/*      */           
/* 4742 */           .append(" and admin1.FLEVEL = 5   order by  person.fnumber  ");
/*      */       }
/* 4744 */       else if (sqlType == 2) {
/* 4745 */         sql.append(" /*dialect*/ SELECT distinct  to_char(entry.FBizDate,'yyyy-mm-dd hh24:mi:ss') as changesDate , admin4.fnumber as groupNumber ,admin3.fnumber as daquNumber , ' ' as cityNumber,admin2.fnumber as companyNumber,admin1.fnumber as deptNumber , ")
/* 4746 */           .append(" person.FNumber as personnumber,person.fname_l2 as fname ,position.Fnumber as positionNumber , position.fname_l2 as positionName ,  nvl( bdpost.fnumber  ,' ') as TechnicalPost,   ")
/* 4747 */           .append(" positionName.fnumber as zhiwunumber ,nvl( jobGrade.fnumber,' ') as jobGradeNumber  , ' ' as fentanTypeNumber  , ' ' as guanbaoTypeNumber  , ' ' as empTypeNumber , to_char( empOrgRelation.FEFFDT ,'yyyy-mm-dd hh24:mi:ss') as empDate,  ")
/* 4748 */           .append(" nvl(  to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss') ,nvl(to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss'),' ') ) as empzhuanzhengDate  , to_char(empOrgRelation.Fleffdt,'yyyy-mm-dd hh24:mi:ss') as empleftdate  ,nvl( employeeModle.fnumber,' ') as empModelNumber  ,(case employeeContract.FNewState when 0 then  '01'  when 1 then  '01'  when 2 then  '02' else ' ' end ) as hetongtype ,   ")
/* 4749 */           .append(" nvl( to_char(employeeContract.FEffectDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  EffectDate , nvl( to_char(employeeContract.FEndDate,'yyyy-mm-dd hh24:mi:ss'),' ') as EndDate , nvl(person.FIDCardNO,' ') as  IDCardNO,nvl(  person.FHjAddress,' ') as HjAddress ,   ")
/* 4750 */           .append("  (case person.FGender when 1 then  'F'  when 2 then  'M'  ELSE  ' '  end )  as Gender , nvl(  to_char( person.FBirthday,'yyyy-mm-dd hh24:mi:ss'),' ') as Birthday ,nvl( hrWed.FNAME_l2,' ') as wefName ,nvl( hrFolk.FNAME_l2,' ') as folkName ,nvl(hrPolitical.fname_l2,' ') as PoliticalName ,  ")
/* 4751 */           .append(" nvl(personContactMethod.FADDRESS,' ')  as faddress , nvl( personContactMethod.FEmail,' ') as Email , nvl( personContactMethod.FMobile ,' ') as Mobile , nvl( personContactMethod.FLinkName ,' ') as LinkName ,  nvl( personContactMethod.FLinkTelNum ,' ') as LinkTelNum , ")
/* 4752 */           .append(" nvl( hrDiploma.fname_l2 ,' ') as DiplomaName , nvl(personDegree.FGraduateSchool,' ') as  GraduateSchool ,  nvl(personDegree.FSpecialty,' ') as  Specialty ,  nvl(to_char(personDegree.FGraduateDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  GraduateDate ,  ")
/* 4753 */           .append(" nvl(person.FIdNum,' ') as idNum ,  nvl(person.FRTX,' ')  as RTX , nvl( employeeContract.FCONTRACTNO ,' ') as CONTRACTNO , nvl( to_char(personCertifyCompetency.FObtainDate,'yyyy-mm-dd hh24:mi:ss'),' ') as ObtainDate , nvl(to_char(personWorkExp.FBeginDate ,'yyyy-mm-dd hh24:mi:ss'),' ') as firstWorkTime,  ")
/* 4754 */           .append("  ' ' as insuredCityNumber , nvl(regpermresidence.fname_l2,' ') as  hujiName , ' ' as  gongjijinNum ,nvl( entry.FDescription,' ') as Remarks  ")
/* 4755 */           .append(" ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign ")
/* 4756 */           .append(" FROM  T_HR_FluctuationBizBill  bill ")
/* 4757 */           .append(" left join  T_HR_FluctuationBizBillEntry   entry  on entry.FBILLID = bill.fid  ")
/* 4758 */           .append(" left join   T_BD_Person  person    on entry.FPersonID = person.fid ")
/* 4759 */           .append(" left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid  and entry.FBizDate = empOrgRelation.FEFFDT  and entry.FAdminOrgID = empOrgRelation.FADMINORGID ")
/* 4760 */           .append(" LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid ")
/* 4761 */           .append(" left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID  ")
/* 4762 */           .append(" left join  T_ORG_Admin  admin1  on entry.FAdminOrgID  = admin1.fid ")
/* 4763 */           .append(" left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid ")
/* 4764 */           .append(" left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid ")
/* 4765 */           .append(" left join  T_ORG_Admin  admin4  on admin3.FPARENTID = admin4.fid ")
/* 4766 */           .append(" left join  T_ORG_Position  position   on  entry.FPositionID = position.fid ")
/* 4767 */           .append(" left join  T_ORG_Job  positionName   on  position.FJobID =positionName.fid ")
/* 4768 */           .append(" left join  T_HR_JobGrade  jobGrade   on  empOrgRelation.FJobGradeID =jobGrade.fid ")
/* 4769 */           .append(" left join  T_HR_EmpLaborRelation  empLaborRelation   on  empLaborRelation.fpersonid =person.fid ")
/* 4770 */           .append(" left join  T_HR_EmpPostExperienceHis  empPostExperienceHis   on  empPostExperienceHis.fpersonid =person.fid ")
/* 4771 */           .append(" left join  T_BD_EmployeeModle  employeeModle   on  empLaborRelation.FEmployeeModleID =employeeModle.fid ")
/* 4772 */           .append(" left join  T_HR_EmployeeContract employeeContract   on  employeeContract.FEmployeeID =person.fid   ")
/* 4773 */           .append(" left join  T_BD_HRWed hrWed   on  hrWed.fid =person.FWedID ")
/* 4774 */           .append(" left join  T_BD_HRFolk hrFolk   on  hrFolk.fid =person.FFolkID ")
/* 4775 */           .append(" left join  T_BD_HRPolitical  hrPolitical   on  hrPolitical.fid =person.FPoliticalFaceID ")
/* 4776 */           .append(" left join  T_HR_PersonContactMethod  personContactMethod   on  person.fid =personContactMethod.fpersonid ")
/* 4777 */           .append(" left join  T_HR_PersonDegree personDegree   on  person.fid = personDegree.fpersonid ")
/* 4778 */           .append(" left join  T_BD_HRDiploma  hrDiploma   on  hrDiploma.fid = personDegree.FDIPLOMA ")
/* 4779 */           .append(" left join  T_HR_PersonCertifyCompetency  personCertifyCompetency   on  person.fid = personCertifyCompetency.FPERSONID  ")
/* 4780 */           .append(" left join  T_HR_PersonWorkExp  personWorkExp   on  person.fid = personWorkExp.FPERSONID  ")
/* 4781 */           .append(" left join  T_BD_Regpermresidence  regpermresidence   on  regpermresidence.fid = person.FRegresidenceID ")
/* 4782 */           .append(" left join  T_HR_PersonTechPost  psot    on  psot.fpersonid =person.fid  and  FISHIGHTECHNICAL = 1 ")
/* 4783 */           .append(" left join T_HR_BDTechnicalPost  bdpost  on  psot.FTECHNICALPOSTID  =bdpost.fid  ")
/* 4784 */           .append("  where bill.fbillstate = 3     and   empOrgRelation.FActionID = 'DawAAAApVj/maL7Z'  and admin1.FLEVEL = 4  and admin3.FNUMBER  = 'glzb'  ")
/*      */ 
/*      */           
/* 4787 */           .append("  order by  person.fnumber  ");
/*      */       }
/* 4789 */       else if (sqlType == 3) {
/* 4790 */         sql.append(" /*dialect*/ SELECT distinct to_char(entry.FBizDate,'yyyy-mm-dd hh24:mi:ss') as changesDate ,admin4.fnumber as groupNumber ,admin3.fnumber as daquNumber , admin2.fnumber  as cityNumber,admin1.fnumber as companyNumber, ' ' as deptNumber , ")
/* 4791 */           .append(" person.FNumber as personnumber,person.fname_l2 as fname ,position.Fnumber as positionNumber , position.fname_l2 as positionName ,  nvl( bdpost.fnumber  ,' ') as TechnicalPost,   ")
/* 4792 */           .append(" positionName.fnumber as zhiwunumber ,nvl( jobGrade.fnumber,' ') as jobGradeNumber  , ' ' as fentanTypeNumber  , ' ' as guanbaoTypeNumber  , ' ' as empTypeNumber , to_char( empOrgRelation.FEFFDT ,'yyyy-mm-dd hh24:mi:ss') as empDate,  ")
/* 4793 */           .append(" nvl(  to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss') ,nvl(to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss'),' ') ) as empzhuanzhengDate  ,  to_char(empOrgRelation.Fleffdt,'yyyy-mm-dd hh24:mi:ss') as empleftdate  ,nvl( employeeModle.fnumber,' ') as empModelNumber  ,(case employeeContract.FNewState when 0 then  '01'  when 1 then  '01'  when 2 then  '02' else ' ' end ) as hetongtype ,   ")
/* 4794 */           .append(" nvl( to_char(employeeContract.FEffectDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  EffectDate , nvl( to_char(employeeContract.FEndDate,'yyyy-mm-dd hh24:mi:ss'),' ') as EndDate , nvl(person.FIDCardNO,' ') as  IDCardNO,nvl(  person.FHjAddress,' ') as HjAddress ,   ")
/* 4795 */           .append("  (case person.FGender when 1 then  'F'  when 2 then  'M'  ELSE  ' '  end )  as Gender , nvl(  to_char( person.FBirthday,'yyyy-mm-dd hh24:mi:ss'),' ') as Birthday ,nvl( hrWed.FNAME_l2,' ') as wefName ,nvl( hrFolk.FNAME_l2,' ') as folkName ,nvl(hrPolitical.fname_l2,' ') as PoliticalName ,  ")
/* 4796 */           .append(" nvl(personContactMethod.FADDRESS,' ')  as faddress , nvl( personContactMethod.FEmail,' ') as Email , nvl( personContactMethod.FMobile ,' ') as Mobile , nvl( personContactMethod.FLinkName ,' ') as LinkName ,  nvl( personContactMethod.FLinkTelNum ,' ') as LinkTelNum ,  ")
/* 4797 */           .append(" nvl( hrDiploma.fname_l2 ,' ') as DiplomaName , nvl(personDegree.FGraduateSchool,' ') as  GraduateSchool ,  nvl(personDegree.FSpecialty,' ') as  Specialty ,  nvl(to_char(personDegree.FGraduateDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  GraduateDate ,  ")
/* 4798 */           .append(" nvl(person.FIdNum,' ') as idNum ,  nvl(person.FRTX,' ')  as RTX , nvl( employeeContract.FCONTRACTNO ,' ') as CONTRACTNO , nvl( to_char(personCertifyCompetency.FObtainDate,'yyyy-mm-dd hh24:mi:ss'),' ') as ObtainDate , nvl(to_char(personWorkExp.FBeginDate ,'yyyy-mm-dd hh24:mi:ss'),' ') as firstWorkTime,  ")
/* 4799 */           .append("  ' ' as insuredCityNumber , nvl(regpermresidence.fname_l2,' ') as  hujiName , ' ' as  gongjijinNum ,nvl( entry.FDescription,' ') as Remarks  ")
/* 4800 */           .append(" ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign ")
/* 4801 */           .append(" FROM  T_HR_FluctuationBizBill  bill ")
/* 4802 */           .append(" left join  T_HR_FluctuationBizBillEntry   entry  on entry.FBILLID = bill.fid  ")
/* 4803 */           .append(" left join   T_BD_Person  person    on entry.FPersonID = person.fid ")
/* 4804 */           .append(" left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid  and entry.FBizDate = empOrgRelation.FEFFDT  and entry.FAdminOrgID = empOrgRelation.FADMINORGID ")
/* 4805 */           .append(" LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid ")
/* 4806 */           .append(" left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID  ")
/* 4807 */           .append(" left join  T_ORG_Admin  admin1  on entry.FAdminOrgID  = admin1.fid ")
/* 4808 */           .append(" left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid ")
/* 4809 */           .append(" left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid ")
/* 4810 */           .append(" left join  T_ORG_Admin  admin4  on admin3.FPARENTID = admin4.fid ")
/* 4811 */           .append(" left join  T_ORG_Position  position   on  entry.FPositionID = position.fid ")
/* 4812 */           .append(" left join  T_ORG_Job  positionName   on  position.FJobID =positionName.fid ")
/* 4813 */           .append(" left join  T_HR_JobGrade  jobGrade   on  empOrgRelation.FJobGradeID =jobGrade.fid ")
/* 4814 */           .append(" left join  T_HR_EmpLaborRelation  empLaborRelation   on  empLaborRelation.fpersonid =person.fid ")
/* 4815 */           .append(" left join  T_HR_EmpPostExperienceHis  empPostExperienceHis   on  empPostExperienceHis.fpersonid =person.fid ")
/* 4816 */           .append(" left join  T_BD_EmployeeModle  employeeModle   on  empLaborRelation.FEmployeeModleID =employeeModle.fid ")
/* 4817 */           .append(" left join  T_HR_EmployeeContract employeeContract   on  employeeContract.FEmployeeID =person.fid   ")
/* 4818 */           .append(" left join  T_BD_HRWed hrWed   on  hrWed.fid =person.FWedID ")
/* 4819 */           .append(" left join  T_BD_HRFolk hrFolk   on  hrFolk.fid =person.FFolkID ")
/* 4820 */           .append(" left join  T_BD_HRPolitical  hrPolitical   on  hrPolitical.fid =person.FPoliticalFaceID ")
/* 4821 */           .append(" left join  T_HR_PersonContactMethod  personContactMethod   on  person.fid =personContactMethod.fpersonid ")
/* 4822 */           .append(" left join  T_HR_PersonDegree personDegree   on  person.fid = personDegree.fpersonid ")
/* 4823 */           .append(" left join  T_BD_HRDiploma  hrDiploma   on  hrDiploma.fid = personDegree.FDIPLOMA ")
/* 4824 */           .append(" left join  T_HR_PersonCertifyCompetency  personCertifyCompetency   on  person.fid = personCertifyCompetency.FPERSONID  ")
/* 4825 */           .append(" left join  T_HR_PersonWorkExp  personWorkExp   on  person.fid = personWorkExp.FPERSONID  ")
/* 4826 */           .append(" left join  T_BD_Regpermresidence  regpermresidence   on  regpermresidence.fid = person.FRegresidenceID ")
/* 4827 */           .append(" left join  T_HR_PersonTechPost  psot    on  psot.fpersonid =person.fid  and  FISHIGHTECHNICAL = 1 ")
/* 4828 */           .append(" left join T_HR_BDTechnicalPost  bdpost  on  psot.FTECHNICALPOSTID  =bdpost.fid  ")
/* 4829 */           .append("  where bill.fbillstate = 3     and   empOrgRelation.FActionID = 'DawAAAApVj/maL7Z'   and admin1.FLEVEL = 4  and admin3.FNUMBER ! = 'glzb'  ")
/*      */ 
/*      */           
/* 4832 */           .append("  order by  person.fnumber  ");
/*      */       }
/*      */     
/* 4835 */     } else if (yidongType == 4) {
/* 4836 */       if (sqlType == 1) {
/* 4837 */         sql.append(" /*dialect*/ SELECT  distinct to_char(entry.FBizDate,'yyyy-mm-dd hh24:mi:ss') as changesDate , admin5.fnumber as groupNumber ,admin4.fnumber as daquNumber ,admin3.fnumber as cityNumber,admin2.fnumber as companyNumber, admin1.fnumber as deptNumber , ")
/* 4838 */           .append(" person.FNumber as personnumber,person.fname_l2 as fname ,position.Fnumber as positionNumber , position.fname_l2 as positionName ,  nvl( bdpost.fnumber  ,' ') as TechnicalPost,   ")
/* 4839 */           .append(" positionName.fnumber as zhiwunumber ,nvl( jobGrade.fnumber,' ') as jobGradeNumber  , ' ' as fentanTypeNumber  , ' ' as guanbaoTypeNumber  , ' ' as empTypeNumber , to_char( empOrgRelation.FEFFDT ,'yyyy-mm-dd hh24:mi:ss') as empDate,  ")
/* 4840 */           .append(" nvl(  to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss') ,nvl(to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss'),' ') ) as empzhuanzhengDate  , to_char(empOrgRelation.Fleffdt,'yyyy-mm-dd hh24:mi:ss') as empleftdate  ,nvl( employeeModle.fnumber,' ') as empModelNumber  ,(case employeeContract.FNewState when 0 then  '01'  when 1 then  '01'  when 2 then  '02' else ' ' end ) as hetongtype ,   ")
/* 4841 */           .append(" nvl( to_char(employeeContract.FEffectDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  EffectDate , nvl( to_char(employeeContract.FEndDate,'yyyy-mm-dd hh24:mi:ss'),' ') as EndDate , nvl(person.FIDCardNO,' ') as  IDCardNO,nvl(  person.FHjAddress,' ') as HjAddress ,   ")
/* 4842 */           .append("  (case person.FGender when 1 then  'F'  when 2 then  'M'  ELSE  ' '  end )  as Gender , nvl(  to_char( person.FBirthday,'yyyy-mm-dd hh24:mi:ss'),' ') as Birthday ,nvl( hrWed.FNAME_l2,' ') as wefName ,nvl( hrFolk.FNAME_l2,' ') as folkName ,nvl(hrPolitical.fname_l2,' ') as PoliticalName ,  ")
/* 4843 */           .append(" nvl(personContactMethod.FADDRESS,' ')  as faddress , nvl( personContactMethod.FEmail,' ') as Email , nvl( personContactMethod.FMobile ,' ') as Mobile , nvl( personContactMethod.FLinkName ,' ') as LinkName ,  nvl( personContactMethod.FLinkTelNum ,' ') as LinkTelNum ,  ")
/* 4844 */           .append(" nvl( hrDiploma.fname_l2 ,' ') as DiplomaName , nvl(personDegree.FGraduateSchool,' ') as  GraduateSchool ,  nvl(personDegree.FSpecialty,' ') as  Specialty ,  nvl(to_char(personDegree.FGraduateDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  GraduateDate ,  ")
/* 4845 */           .append(" nvl(person.FIdNum,' ') as idNum ,  nvl(person.FRTX,' ')  as RTX , nvl( employeeContract.FCONTRACTNO ,' ') as CONTRACTNO , nvl( to_char(personCertifyCompetency.FObtainDate,'yyyy-mm-dd hh24:mi:ss'),' ') as ObtainDate , nvl(to_char(personWorkExp.FBeginDate ,'yyyy-mm-dd hh24:mi:ss'),' ') as firstWorkTime,  ")
/* 4846 */           .append("  ' ' as insuredCityNumber , nvl(regpermresidence.fname_l2,' ') as  hujiName , ' ' as  gongjijinNum ,nvl( entry.FDescription,' ') as Remarks  ")
/* 4847 */           .append(" ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign ")
/* 4848 */           .append(" FROM  T_HR_FlucOutBizBill  bill ")
/* 4849 */           .append(" left join  T_HR_FlucOutBizBillEntry   entry  on entry.FBILLID = bill.fid  ")
/* 4850 */           .append(" left join   T_BD_Person  person    on entry.FPersonID = person.fid ")
/* 4851 */           .append(" left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid     ")
/* 4852 */           .append(" LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid ")
/* 4853 */           .append(" left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID  ")
/* 4854 */           .append(" left join  T_ORG_Admin  admin1  on empOrgRelation.FAdminOrgID  = admin1.fid ")
/* 4855 */           .append(" left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid ")
/* 4856 */           .append(" left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid ")
/* 4857 */           .append(" left join  T_ORG_Admin  admin4  on admin3.FPARENTID = admin4.fid ")
/* 4858 */           .append(" left join  T_ORG_Admin  admin5  on admin4.FPARENTID = admin5.fid   ")
/* 4859 */           .append(" left join  T_ORG_Position  position   on  entry.FPositionID = position.fid ")
/* 4860 */           .append(" left join  T_ORG_Job  positionName   on  position.FJobID =positionName.fid ")
/* 4861 */           .append(" left join  T_HR_JobGrade  jobGrade   on  empOrgRelation.FJobGradeID =jobGrade.fid ")
/* 4862 */           .append(" left join  T_HR_EmpLaborRelation  empLaborRelation   on  empLaborRelation.fpersonid =person.fid ")
/* 4863 */           .append(" left join  T_HR_EmpPostExperienceHis  empPostExperienceHis   on  empPostExperienceHis.fpersonid =person.fid ")
/* 4864 */           .append(" left join  T_BD_EmployeeModle  employeeModle   on  empLaborRelation.FEmployeeModleID =employeeModle.fid ")
/* 4865 */           .append(" left join  T_HR_EmployeeContract employeeContract   on  employeeContract.FEmployeeID =person.fid   ")
/* 4866 */           .append(" left join  T_BD_HRWed hrWed   on  hrWed.fid =person.FWedID ")
/* 4867 */           .append(" left join  T_BD_HRFolk hrFolk   on  hrFolk.fid =person.FFolkID ")
/* 4868 */           .append(" left join  T_BD_HRPolitical  hrPolitical   on  hrPolitical.fid =person.FPoliticalFaceID ")
/* 4869 */           .append(" left join  T_HR_PersonContactMethod  personContactMethod   on  person.fid =personContactMethod.fpersonid ")
/* 4870 */           .append(" left join  T_HR_PersonDegree personDegree   on  person.fid = personDegree.fpersonid ")
/* 4871 */           .append(" left join  T_BD_HRDiploma  hrDiploma   on  hrDiploma.fid = personDegree.FDIPLOMA ")
/* 4872 */           .append(" left join  T_HR_PersonCertifyCompetency  personCertifyCompetency   on  person.fid = personCertifyCompetency.FPERSONID  ")
/* 4873 */           .append(" left join  T_HR_PersonWorkExp  personWorkExp   on  person.fid = personWorkExp.FPERSONID  ")
/* 4874 */           .append(" left join  T_BD_Regpermresidence  regpermresidence   on  regpermresidence.fid = person.FRegresidenceID ")
/* 4875 */           .append(" left join  T_HR_PersonTechPost  psot    on  psot.fpersonid =person.fid  and  FISHIGHTECHNICAL = 1 ")
/* 4876 */           .append(" left join T_HR_BDTechnicalPost  bdpost  on  psot.FTECHNICALPOSTID  =bdpost.fid  ")
/* 4877 */           .append("  where bill.fbillstate = 3  and  EmpOrgRelation.fassigntype = 1 and empOrgRelation.Fleffdt > sysdate     and  EmpOrgRelation.FIsLatestInAday = 1     and admin1.FLEVEL =5 ")
/*      */ 
/*      */           
/* 4880 */           .append("  order by  person.fnumber   ");
/*      */       }
/* 4882 */       else if (sqlType == 2) {
/* 4883 */         sql.append(" /*dialect*/ SELECT distinct  to_char(entry.FBizDate,'yyyy-mm-dd hh24:mi:ss') as changesDate , admin4.fnumber as groupNumber ,admin3.fnumber as daquNumber , ' ' as cityNumber,admin2.fnumber as companyNumber,admin1.fnumber as deptNumber , ")
/* 4884 */           .append(" person.FNumber as personnumber,person.fname_l2 as fname ,position.Fnumber as positionNumber , position.fname_l2 as positionName ,  nvl( bdpost.fnumber  ,' ') as TechnicalPost,   ")
/* 4885 */           .append(" positionName.fnumber as zhiwunumber ,nvl( jobGrade.fnumber,' ') as jobGradeNumber  , ' ' as fentanTypeNumber  , ' ' as guanbaoTypeNumber  , ' ' as empTypeNumber , to_char( empOrgRelation.FEFFDT ,'yyyy-mm-dd hh24:mi:ss') as empDate,  ")
/* 4886 */           .append(" nvl(  to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss') ,nvl(to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss'),' ') ) as empzhuanzhengDate  , to_char(empOrgRelation.Fleffdt,'yyyy-mm-dd hh24:mi:ss') as empleftdate  ,nvl( employeeModle.fnumber,' ') as empModelNumber  ,(case employeeContract.FNewState when 0 then  '01'  when 1 then  '01'  when 2 then  '02' else ' ' end ) as hetongtype ,   ")
/* 4887 */           .append(" nvl( to_char(employeeContract.FEffectDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  EffectDate , nvl( to_char(employeeContract.FEndDate,'yyyy-mm-dd hh24:mi:ss'),' ') as EndDate , nvl(person.FIDCardNO,' ') as  IDCardNO,nvl(  person.FHjAddress,' ') as HjAddress ,   ")
/* 4888 */           .append("  (case person.FGender when 1 then  'F'  when 2 then  'M'  ELSE  ' '  end )  as Gender , nvl(  to_char( person.FBirthday,'yyyy-mm-dd hh24:mi:ss'),' ') as Birthday ,nvl( hrWed.FNAME_l2,' ') as wefName ,nvl( hrFolk.FNAME_l2,' ') as folkName ,nvl(hrPolitical.fname_l2,' ') as PoliticalName ,  ")
/* 4889 */           .append(" nvl(personContactMethod.FADDRESS,' ')  as faddress , nvl( personContactMethod.FEmail,' ') as Email , nvl( personContactMethod.FMobile ,' ') as Mobile , nvl( personContactMethod.FLinkName ,' ') as LinkName ,  nvl( personContactMethod.FLinkTelNum ,' ') as LinkTelNum ,  ")
/* 4890 */           .append(" nvl( hrDiploma.fname_l2 ,' ') as DiplomaName , nvl(personDegree.FGraduateSchool,' ') as  GraduateSchool ,  nvl(personDegree.FSpecialty,' ') as  Specialty ,  nvl(to_char(personDegree.FGraduateDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  GraduateDate ,  ")
/* 4891 */           .append(" nvl(person.FIdNum,' ') as idNum ,  nvl(person.FRTX,' ')  as RTX , nvl( employeeContract.FCONTRACTNO ,' ') as CONTRACTNO , nvl( to_char(personCertifyCompetency.FObtainDate,'yyyy-mm-dd hh24:mi:ss'),' ') as ObtainDate , nvl(to_char(personWorkExp.FBeginDate ,'yyyy-mm-dd hh24:mi:ss'),' ') as firstWorkTime,  ")
/* 4892 */           .append("  ' ' as insuredCityNumber , nvl(regpermresidence.fname_l2,' ') as  hujiName , ' ' as  gongjijinNum ,nvl( entry.FDescription,' ') as Remarks  ")
/* 4893 */           .append(" ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign ")
/* 4894 */           .append(" FROM  T_HR_FlucOutBizBill  bill ")
/* 4895 */           .append(" left join  T_HR_FlucOutBizBillEntry   entry  on entry.FBILLID = bill.fid  ")
/* 4896 */           .append(" left join   T_BD_Person  person    on entry.FPersonID = person.fid ")
/* 4897 */           .append(" left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid     ")
/* 4898 */           .append(" LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid ")
/* 4899 */           .append(" left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID  ")
/* 4900 */           .append(" left join  T_ORG_Admin  admin1  on empOrgRelation.FAdminOrgID  = admin1.fid ")
/* 4901 */           .append(" left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid ")
/* 4902 */           .append(" left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid ")
/* 4903 */           .append(" left join  T_ORG_Admin  admin4  on admin3.FPARENTID = admin4.fid ")
/* 4904 */           .append(" left join  T_ORG_Position  position   on  entry.FPositionID = position.fid ")
/* 4905 */           .append(" left join  T_ORG_Job  positionName   on  position.FJobID =positionName.fid ")
/* 4906 */           .append(" left join  T_HR_JobGrade  jobGrade   on  empOrgRelation.FJobGradeID =jobGrade.fid ")
/* 4907 */           .append(" left join  T_HR_EmpLaborRelation  empLaborRelation   on  empLaborRelation.fpersonid =person.fid ")
/* 4908 */           .append(" left join  T_HR_EmpPostExperienceHis  empPostExperienceHis   on  empPostExperienceHis.fpersonid =person.fid ")
/* 4909 */           .append(" left join  T_BD_EmployeeModle  employeeModle   on  empLaborRelation.FEmployeeModleID =employeeModle.fid ")
/* 4910 */           .append(" left join  T_HR_EmployeeContract employeeContract   on  employeeContract.FEmployeeID =person.fid   ")
/* 4911 */           .append(" left join  T_BD_HRWed hrWed   on  hrWed.fid =person.FWedID ")
/* 4912 */           .append(" left join  T_BD_HRFolk hrFolk   on  hrFolk.fid =person.FFolkID ")
/* 4913 */           .append(" left join  T_BD_HRPolitical  hrPolitical   on  hrPolitical.fid =person.FPoliticalFaceID ")
/* 4914 */           .append(" left join  T_HR_PersonContactMethod  personContactMethod   on  person.fid =personContactMethod.fpersonid ")
/* 4915 */           .append(" left join  T_HR_PersonDegree personDegree   on  person.fid = personDegree.fpersonid ")
/* 4916 */           .append(" left join  T_BD_HRDiploma  hrDiploma   on  hrDiploma.fid = personDegree.FDIPLOMA ")
/* 4917 */           .append(" left join  T_HR_PersonCertifyCompetency  personCertifyCompetency   on  person.fid = personCertifyCompetency.FPERSONID  ")
/* 4918 */           .append(" left join  T_HR_PersonWorkExp  personWorkExp   on  person.fid = personWorkExp.FPERSONID  ")
/* 4919 */           .append(" left join  T_BD_Regpermresidence  regpermresidence   on  regpermresidence.fid = person.FRegresidenceID ")
/* 4920 */           .append(" left join  T_HR_PersonTechPost  psot    on  psot.fpersonid =person.fid  and  FISHIGHTECHNICAL = 1 ")
/* 4921 */           .append(" left join T_HR_BDTechnicalPost  bdpost  on  psot.FTECHNICALPOSTID  =bdpost.fid  ")
/* 4922 */           .append("  where bill.fbillstate = 3 and  EmpOrgRelation.fassigntype = 1 and empOrgRelation.Fleffdt > sysdate     and  EmpOrgRelation.FIsLatestInAday = 1    and admin1.FLEVEL = 4  and admin3.FNUMBER = 'glzb'   ")
/*      */ 
/*      */           
/* 4925 */           .append("  order by  person.fnumber   ");
/* 4926 */       } else if (sqlType == 3) {
/* 4927 */         sql.append(" /*dialect*/ SELECT distinct to_char(entry.FBizDate,'yyyy-mm-dd hh24:mi:ss') as changesDate ,admin4.fnumber as groupNumber ,admin3.fnumber as daquNumber , admin2.fnumber  as cityNumber,admin1.fnumber as companyNumber, ' ' as deptNumber , ")
/* 4928 */           .append(" person.FNumber as personnumber,person.fname_l2 as fname ,position.Fnumber as positionNumber , position.fname_l2 as positionName ,  nvl( bdpost.fnumber  ,' ') as TechnicalPost,   ")
/* 4929 */           .append(" positionName.fnumber as zhiwunumber ,nvl( jobGrade.fnumber,' ') as jobGradeNumber  , ' ' as fentanTypeNumber  , ' ' as guanbaoTypeNumber  , ' ' as empTypeNumber , to_char( empOrgRelation.FEFFDT ,'yyyy-mm-dd hh24:mi:ss') as empDate,  ")
/* 4930 */           .append(" nvl(  to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss') ,nvl(to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss'),' ') ) as empzhuanzhengDate  ,  to_char(empOrgRelation.Fleffdt,'yyyy-mm-dd hh24:mi:ss') as empleftdate  ,nvl( employeeModle.fnumber,' ') as empModelNumber  ,(case employeeContract.FNewState when 0 then  '01'  when 1 then  '01'  when 2 then  '02' else ' ' end ) as hetongtype ,   ")
/* 4931 */           .append(" nvl( to_char(employeeContract.FEffectDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  EffectDate , nvl( to_char(employeeContract.FEndDate,'yyyy-mm-dd hh24:mi:ss'),' ') as EndDate , nvl(person.FIDCardNO,' ') as  IDCardNO,nvl(  person.FHjAddress,' ') as HjAddress ,   ")
/* 4932 */           .append("  (case person.FGender when 1 then  'F'  when 2 then  'M'  ELSE  ' '  end )  as Gender , nvl(  to_char( person.FBirthday,'yyyy-mm-dd hh24:mi:ss'),' ') as Birthday ,nvl( hrWed.FNAME_l2,' ') as wefName ,nvl( hrFolk.FNAME_l2,' ') as folkName ,nvl(hrPolitical.fname_l2,' ') as PoliticalName ,  ")
/* 4933 */           .append(" nvl(personContactMethod.FADDRESS,' ')  as faddress , nvl( personContactMethod.FEmail,' ') as Email , nvl( personContactMethod.FMobile ,' ') as Mobile , nvl( personContactMethod.FLinkName ,' ') as LinkName , nvl( personContactMethod.FLinkTelNum ,' ') as LinkTelNum ,   ")
/* 4934 */           .append(" nvl( hrDiploma.fname_l2 ,' ') as DiplomaName , nvl(personDegree.FGraduateSchool,' ') as  GraduateSchool ,  nvl(personDegree.FSpecialty,' ') as  Specialty ,  nvl(to_char(personDegree.FGraduateDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  GraduateDate ,  ")
/* 4935 */           .append(" nvl(person.FIdNum,' ') as idNum ,  nvl(person.FRTX,' ')  as RTX , nvl( employeeContract.FCONTRACTNO ,' ') as CONTRACTNO , nvl( to_char(personCertifyCompetency.FObtainDate,'yyyy-mm-dd hh24:mi:ss'),' ') as ObtainDate , nvl(to_char(personWorkExp.FBeginDate ,'yyyy-mm-dd hh24:mi:ss'),' ') as firstWorkTime,  ")
/* 4936 */           .append("  ' ' as insuredCityNumber , nvl(regpermresidence.fname_l2,' ') as  hujiName , ' ' as  gongjijinNum ,nvl( entry.FDescription,' ') as Remarks  ")
/* 4937 */           .append(" ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign ")
/* 4938 */           .append(" FROM  T_HR_FlucOutBizBill  bill ")
/* 4939 */           .append(" left join  T_HR_FlucOutBizBillEntry   entry  on entry.FBILLID = bill.fid  ")
/* 4940 */           .append(" left join   T_BD_Person  person    on entry.FPersonID = person.fid ")
/* 4941 */           .append(" left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid   ")
/* 4942 */           .append(" LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid ")
/* 4943 */           .append(" left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID  ")
/* 4944 */           .append(" left join  T_ORG_Admin  admin1  on empOrgRelation.FAdminOrgID  = admin1.fid ")
/* 4945 */           .append(" left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid ")
/* 4946 */           .append(" left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid ")
/* 4947 */           .append(" left join  T_ORG_Admin  admin4  on admin3.FPARENTID = admin4.fid ")
/* 4948 */           .append(" left join  T_ORG_Admin  admin5  on admin4.FPARENTID = admin5.fid   ")
/* 4949 */           .append(" left join  T_ORG_Position  position   on  entry.FPositionID = position.fid ")
/* 4950 */           .append(" left join  T_ORG_Job  positionName   on  position.FJobID =positionName.fid ")
/* 4951 */           .append(" left join  T_HR_JobGrade  jobGrade   on  empOrgRelation.FJobGradeID =jobGrade.fid ")
/* 4952 */           .append(" left join  T_HR_EmpLaborRelation  empLaborRelation   on  empLaborRelation.fpersonid =person.fid ")
/* 4953 */           .append(" left join  T_HR_EmpPostExperienceHis  empPostExperienceHis   on  empPostExperienceHis.fpersonid =person.fid ")
/* 4954 */           .append(" left join  T_BD_EmployeeModle  employeeModle   on  empLaborRelation.FEmployeeModleID =employeeModle.fid ")
/* 4955 */           .append(" left join  T_HR_EmployeeContract employeeContract   on  employeeContract.FEmployeeID =person.fid   ")
/* 4956 */           .append(" left join  T_BD_HRWed hrWed   on  hrWed.fid =person.FWedID ")
/* 4957 */           .append(" left join  T_BD_HRFolk hrFolk   on  hrFolk.fid =person.FFolkID ")
/* 4958 */           .append(" left join  T_BD_HRPolitical  hrPolitical   on  hrPolitical.fid =person.FPoliticalFaceID ")
/* 4959 */           .append(" left join  T_HR_PersonContactMethod  personContactMethod   on  person.fid =personContactMethod.fpersonid ")
/* 4960 */           .append(" left join  T_HR_PersonDegree personDegree   on  person.fid = personDegree.fpersonid ")
/* 4961 */           .append(" left join  T_BD_HRDiploma  hrDiploma   on  hrDiploma.fid = personDegree.FDIPLOMA ")
/* 4962 */           .append(" left join  T_HR_PersonCertifyCompetency  personCertifyCompetency   on  person.fid = personCertifyCompetency.FPERSONID  ")
/* 4963 */           .append(" left join  T_HR_PersonWorkExp  personWorkExp   on  person.fid = personWorkExp.FPERSONID  ")
/* 4964 */           .append(" left join  T_BD_Regpermresidence  regpermresidence   on  regpermresidence.fid = person.FRegresidenceID ")
/* 4965 */           .append(" left join  T_HR_PersonTechPost  psot    on  psot.fpersonid =person.fid  and  FISHIGHTECHNICAL = 1 ")
/* 4966 */           .append(" left join T_HR_BDTechnicalPost  bdpost  on  psot.FTECHNICALPOSTID  =bdpost.fid  ")
/* 4967 */           .append("  where bill.fbillstate = 3  and  EmpOrgRelation.fassigntype = 1 and empOrgRelation.Fleffdt > sysdate     and  EmpOrgRelation.FIsLatestInAday = 1   and admin1.FLEVEL = 4  and admin3.FNUMBER  != 'glzb'   ")
/*      */ 
/*      */           
/* 4970 */           .append("  order by  person.fnumber   ");
/*      */       } 
/*      */     } 
/*      */ 
/*      */     
/* 4975 */     return sql;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private StringBuffer getSqlCity() throws BOSException {
/* 4981 */     StringBuffer sql = new StringBuffer();
/* 4982 */     sql.append(" /*dialect*/ SELECT   distinct to_char(entry.FBizDate,'yyyy-mm-dd hh24:mi:ss') as changesDate , admin3.fnumber as groupNumber ,admin2.fnumber as daquNumber ,admin1.fnumber as cityNumber, ' ' as companyNumber, ' ' as  deptNumber ,  ")
/* 4983 */       .append(" person.FNumber as personnumber,person.fname_l2 as fname ,position.Fnumber as positionNumber , position.fname_l2 as positionName ,  nvl( bdpost.fnumber  ,' ') as TechnicalPost,   ")
/* 4984 */       .append(" positionName.fnumber as zhiwunumber ,nvl( jobGrade.fnumber,' ') as jobGradeNumber  , ' ' as fentanTypeNumber  , ' ' as guanbaoTypeNumber  , ' ' as empTypeNumber , to_char( empOrgRelation.FEFFDT ,'yyyy-mm-dd hh24:mi:ss') as empDate,  ")
/* 4985 */       .append(" nvl(  to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss') ,nvl(to_char( empLaborRelation.FPLANFORMALDATE,'yyyy-mm-dd hh24:mi:ss'),' ') ) as empzhuanzhengDate  , to_char(empOrgRelation.Fleffdt,'yyyy-mm-dd hh24:mi:ss') as empleftdate  ,nvl( employeeModle.fnumber,' ') as empModelNumber  ,(case employeeContract.FNewState when 0 then  '01'  when 1 then  '01'  when 2 then  '02' else ' ' end ) as hetongtype ,   ")
/* 4986 */       .append(" nvl( to_char(employeeContract.FEffectDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  EffectDate , nvl( to_char(employeeContract.FEndDate,'yyyy-mm-dd hh24:mi:ss'),' ') as EndDate , nvl(person.FIDCardNO,' ') as  IDCardNO,nvl(  person.FHjAddress,' ') as HjAddress ,   ")
/* 4987 */       .append("  (case person.FGender when 1 then  'F'  when 2 then  'M'  ELSE  ' '  end )  as Gender , nvl(  to_char( person.FBirthday,'yyyy-mm-dd hh24:mi:ss'),' ') as Birthday ,nvl( hrWed.FNAME_l2,' ') as wefName ,nvl( hrFolk.FNAME_l2,' ') as folkName ,nvl(hrPolitical.fname_l2,' ') as PoliticalName ,  ")
/* 4988 */       .append(" nvl(personContactMethod.FADDRESS,' ')  as faddress , nvl( personContactMethod.FEmail,' ') as Email , nvl( personContactMethod.FMobile ,' ') as Mobile , nvl( personContactMethod.FLinkName ,' ') as LinkName ,  nvl( personContactMethod.FLinkTelNum ,' ') as LinkTelNum ,  ")
/* 4989 */       .append(" nvl( hrDiploma.fname_l2 ,' ') as DiplomaName , nvl(personDegree.FGraduateSchool,' ') as  GraduateSchool ,  nvl(personDegree.FSpecialty,' ') as  Specialty ,  nvl(to_char(personDegree.FGraduateDate,'yyyy-mm-dd hh24:mi:ss'),' ') as  GraduateDate ,  ")
/* 4990 */       .append(" nvl(person.FIdNum,' ') as idNum ,  nvl(person.FRTX,' ')  as RTX , nvl( employeeContract.FCONTRACTNO ,' ') as CONTRACTNO , nvl( to_char(personCertifyCompetency.FObtainDate,'yyyy-mm-dd hh24:mi:ss'),' ') as ObtainDate , nvl(to_char(personWorkExp.FBeginDate ,'yyyy-mm-dd hh24:mi:ss'),' ') as firstWorkTime,  ")
/* 4991 */       .append("  ' ' as insuredCityNumber , nvl(regpermresidence.fname_l2,' ') as  hujiName , ' ' as  gongjijinNum ,nvl( entry.FDescription,' ') as Remarks  ")
/* 4992 */       .append("  ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign  ")
/* 4993 */       .append("  FROM  T_HR_PluralityAddBizBill  bill    ")
/* 4994 */       .append("  left join  T_HR_PluralityAddBizBillEntry   entry  on entry.FBILLID = bill.fid  ")
/* 4995 */       .append("  left join   T_BD_Person  person    on entry.FPersonID = person.fid  ")
/* 4996 */       .append("  left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid  and entry.FBizDate = empOrgRelation.FEFFDT  ")
/* 4997 */       .append("  LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid  ")
/* 4998 */       .append("  left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID   ")
/* 4999 */       .append("  left join  T_ORG_Admin  admin1  on entry.FAdminOrgID  = admin1.fid  ")
/* 5000 */       .append("  left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid  ")
/* 5001 */       .append("  left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid   join   T_BD_Person  person    on entry.FPersonID = person.fid  ")
/* 5002 */       .append("  left join  T_HR_EmpOrgRelation empOrgRelation  on  empOrgRelation.fpersonid  =  person.fid  and entry.FBizDate = empOrgRelation.FEFFDT  ")
/* 5003 */       .append("  LEFT JOIN T_HR_BDEmployeeType  employeeType on person.FEmployeeTypeID = employeeType.fid  ")
/* 5004 */       .append("  left join T_HR_AffairActionReason  affairActionReason  ON  empOrgRelation.FActionTypeID =affairActionReason.FID   ")
/* 5005 */       .append("  left join  T_ORG_Admin  admin1  on entry.FAdminOrgID  = admin1.fid  ")
/* 5006 */       .append("  left join  T_ORG_Admin  admin2  on admin1.FPARENTID = admin2.fid  ")
/* 5007 */       .append("  left join  T_ORG_Admin  admin3  on admin2.FPARENTID = admin3.fid   ")
/* 5008 */       .append("  left join  T_ORG_Position  position   on  entry.FPositionID = position.fid  ")
/* 5009 */       .append("  left join  T_ORG_Job  positionName   on  position.FJobID =positionName.fid  ")
/* 5010 */       .append("  left join  T_HR_JobGrade  jobGrade   on  empOrgRelation.FJobGradeID =jobGrade.fid  ")
/* 5011 */       .append("  left join  T_HR_EmpLaborRelation  empLaborRelation   on  empLaborRelation.fpersonid =person.fid  ")
/* 5012 */       .append("  left join  T_HR_EmpPostExperienceHis  empPostExperienceHis   on  empPostExperienceHis.fpersonid =person.fid  ")
/* 5013 */       .append("  left join  T_BD_EmployeeModle  employeeModle   on  empLaborRelation.FEmployeeModleID =employeeModle.fid  ")
/* 5014 */       .append("  left join  T_HR_EmployeeContract employeeContract   on  employeeContract.FEmployeeID =person.fid    ")
/* 5015 */       .append("  left join  T_BD_HRWed hrWed   on  hrWed.fid =person.FWedID  ")
/* 5016 */       .append("  left join  T_BD_HRFolk hrFolk   on  hrFolk.fid =person.FFolkID  ")
/* 5017 */       .append("  left join  T_BD_HRPolitical  hrPolitical   on  hrPolitical.fid =person.FPoliticalFaceID  ")
/* 5018 */       .append("  left join  T_HR_PersonContactMethod  personContactMethod   on  person.fid =personContactMethod.fpersonid  ")
/* 5019 */       .append("  left join  T_HR_PersonDegree personDegree   on  person.fid = personDegree.fpersonid  ")
/* 5020 */       .append("  left join  T_BD_HRDiploma  hrDiploma   on  hrDiploma.fid = personDegree.FDIPLOMA  ")
/* 5021 */       .append("  left join  T_HR_PersonCertifyCompetency  personCertifyCompetency   on  person.fid = personCertifyCompetency.FPERSONID   ")
/* 5022 */       .append("  left join  T_HR_PersonWorkExp  personWorkExp   on  person.fid = personWorkExp.FPERSONID   ")
/* 5023 */       .append("  left join  T_BD_Regpermresidence  regpermresidence   on  regpermresidence.fid = person.FRegresidenceID  ")
/* 5024 */       .append(" left join  T_HR_PersonTechPost  psot    on  psot.fpersonid =person.fid  and  FISHIGHTECHNICAL = 1 ")
/* 5025 */       .append(" left join T_HR_BDTechnicalPost  bdpost  on  psot.FTECHNICALPOSTID  =bdpost.fid  ")
/* 5026 */       .append("   where  bill.fbillstate = 3      and  EmpOrgRelation.FActionID ='DawAAAApaaPmaL7Z'    and admin1.FLEVEL =3 ")
/*      */       
/* 5028 */       .append("   order by  person.fnumber   ");
/*      */     
/* 5030 */     return sql;
/*      */   }
/*      */   
/*      */   private String insertOneHRMidTable(Context ctx, DateBaseProcessType processType, DateBasetype baseType, String dataBase, String tableName, IRowSet rows) {
/* 5039 */     String insertSql = "";
/*      */     try {
/* 5041 */       IRowSetMetaData rowSetMataData = rows.getRowSetMetaData();
/* 5042 */       int columnsSize = rowSetMataData.getColumnCount();
/* 5043 */       String insertSqlStart = "INSERT INTO " + tableName + "(";
/* 5044 */       String insertSqlValues = "";
/* 5045 */       String name = rows.getString("fname");
/* 5046 */       String number = rows.getString("personnumber");
/* 5047 */       for (int i = 1; i <= columnsSize; i++) {
/* 5048 */         String columnName = rowSetMataData.getColumnName(i);
/* 5049 */         String value = rows.getString(columnName);
/* 5050 */         if (value == null) {
/* 5051 */           value = "";
/*      */         }
/* 5053 */         if ((columnName.toLowerCase().endsWith("time") || columnName.toLowerCase().endsWith("date")) && (value.equals("") || value.equals(" "))) {
/* 5054 */           value = null;
/*      */         }
/* 5056 */         if (columnName.equals("FNAME") && value.contains("'")) {
/* 5057 */           value = value.replace("'", "''");
/*      */         }
/* 5059 */         if (columnName.equals("FMODEL") && value.contains("'")) {
/* 5060 */           value = value.replace("'", "''");
/*      */         }
/* 5062 */         if (columnName.equals("FXINGHAO") && value.contains("'")) {
/* 5063 */           value = value.replace("'", "''");
/*      */         }
/* 5065 */         if (i == columnsSize) {
/* 5066 */           insertSqlStart = String.valueOf(insertSqlStart) + columnName + " ";
/*      */           
/* 5068 */           if (value == null) {
/* 5069 */             insertSqlValues = String.valueOf(insertSqlValues) + " null ";
/*      */           } else {
/* 5071 */             insertSqlValues = String.valueOf(insertSqlValues) + "'" + value + "' ";
/*      */           } 
/*      */         } else {
/* 5074 */           insertSqlStart = String.valueOf(insertSqlStart) + columnName + ",";
/*      */           
/* 5076 */           if (value == null) {
/* 5077 */             insertSqlValues = String.valueOf(insertSqlValues) + " null, ";
/*      */           } else {
/* 5079 */             insertSqlValues = String.valueOf(insertSqlValues) + "'" + value + "' , ";
/*      */           } 
/*      */         } 
/*      */       } 
/* 5083 */       insertSqlStart = String.valueOf(insertSqlStart) + " ) VALUES(";
/* 5084 */       insertSqlValues = String.valueOf(insertSqlValues) + " )";
/* 5085 */       insertSql = String.valueOf(insertSqlStart) + insertSqlValues;
/* 5086 */       System.out.print("**************sql=" + insertSql);
/*      */       
/* 5088 */       insertLog(ctx, processType, baseType, name, number, "新增");
/* 5089 */       return insertSql;
/*      */     }
/* 5091 */     catch (Exception e) {
/* 5092 */       e.printStackTrace();
/*      */       
/* 5094 */       return insertSql;
/*      */     } 
/*      */   }
/*      */ }