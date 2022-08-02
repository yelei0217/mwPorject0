 package com.kingdee.eas.custom.app;
 
 import com.kingdee.bos.BOSException;
 import com.kingdee.bos.Context;
 import com.kingdee.bos.dao.IObjectPK;
 import com.kingdee.bos.dao.IObjectValue;
 import com.kingdee.bos.dao.ormapping.ObjectUuidPK;
 import com.kingdee.bos.metadata.entity.EntityViewInfo;
 import com.kingdee.bos.metadata.entity.FilterInfo;
 import com.kingdee.bos.metadata.entity.FilterItemInfo;
 import com.kingdee.bos.metadata.query.util.CompareType;
 import com.kingdee.bos.util.BOSUuid;
 import com.kingdee.eas.base.permission.UserFactory;
 import com.kingdee.eas.base.permission.UserInfo;
 import com.kingdee.eas.basedata.assistant.CurrencyFactory;
 import com.kingdee.eas.basedata.assistant.CurrencyInfo;
 import com.kingdee.eas.basedata.assistant.MeasureUnitInfo;
 import com.kingdee.eas.basedata.assistant.PaymentTypeFactory;
 import com.kingdee.eas.basedata.assistant.PaymentTypeInfo;
 import com.kingdee.eas.basedata.assistant.SettlementTypeFactory;
 import com.kingdee.eas.basedata.assistant.SettlementTypeInfo;
 import com.kingdee.eas.basedata.master.account.AccountViewFactory;
 import com.kingdee.eas.basedata.master.account.AccountViewInfo;
 import com.kingdee.eas.basedata.master.auxacct.AsstActTypeFactory;
 import com.kingdee.eas.basedata.master.auxacct.AsstActTypeInfo;
 import com.kingdee.eas.basedata.master.cssp.SupplierFactory;
 import com.kingdee.eas.basedata.master.cssp.SupplierInfo;
 import com.kingdee.eas.basedata.master.material.IMaterial;
 import com.kingdee.eas.basedata.master.material.MaterialCollection;
 import com.kingdee.eas.basedata.master.material.MaterialFactory;
 import com.kingdee.eas.basedata.master.material.MaterialInfo;
 import com.kingdee.eas.basedata.org.AdminOrgUnitFactory;
 import com.kingdee.eas.basedata.org.AdminOrgUnitInfo;
 import com.kingdee.eas.basedata.org.CompanyOrgUnitFactory;
 import com.kingdee.eas.basedata.org.CompanyOrgUnitInfo;
 import com.kingdee.eas.basedata.org.CostCenterOrgUnitFactory;
 import com.kingdee.eas.basedata.org.CostCenterOrgUnitInfo;
 import com.kingdee.eas.basedata.org.PurchaseOrgUnitFactory;
 import com.kingdee.eas.basedata.org.PurchaseOrgUnitInfo;
 import com.kingdee.eas.basedata.org.StorageOrgUnitFactory;
 import com.kingdee.eas.basedata.org.StorageOrgUnitInfo;
 import com.kingdee.eas.basedata.person.PersonFactory;
 import com.kingdee.eas.basedata.person.PersonInfo;
 import com.kingdee.eas.basedata.scm.common.BillTypeInfo;
 import com.kingdee.eas.basedata.scm.common.BizTypeFactory;
 import com.kingdee.eas.basedata.scm.common.BizTypeInfo;
 import com.kingdee.eas.basedata.scm.common.RowTypeFactory;
 import com.kingdee.eas.basedata.scm.common.RowTypeInfo;
 import com.kingdee.eas.basedata.scm.sm.pur.DemandTypeInfo;
 import com.kingdee.eas.common.EASBizException;
 import com.kingdee.eas.cp.bc.ExpenseTypeFactory;
 import com.kingdee.eas.cp.bc.ExpenseTypeInfo;
 import com.kingdee.eas.custom.EAISynTemplate;
 import com.kingdee.eas.custom.app.insertbill.VSPJDSupport;
 import com.kingdee.eas.custom.app.unit.AppUnit;
 import com.kingdee.eas.custom.app.unit.PayRequestBillUtil;
 import com.kingdee.eas.custom.app.unit.SupplyInfoLogUnit;
 import com.kingdee.eas.custom.util.DBUtil;
 import com.kingdee.eas.custom.util.VerifyUtil;
 import com.kingdee.eas.fi.ap.IPayRequestBill;
 import com.kingdee.eas.fi.ap.OtherBillFactory;
 import com.kingdee.eas.fi.ap.OtherBillInfo;
 import com.kingdee.eas.fi.ap.OtherBillPlanInfo;
 import com.kingdee.eas.fi.ap.OtherBillType;
 import com.kingdee.eas.fi.ap.OtherBillentryInfo;
 import com.kingdee.eas.fi.ap.PayRequestBillEntryCollection;
 import com.kingdee.eas.fi.ap.PayRequestBillEntryInfo;
 import com.kingdee.eas.fi.ap.PayRequestBillFactory;
 import com.kingdee.eas.fi.ap.PayRequestBillInfo;
 import com.kingdee.eas.fi.ap.VerificateBillTypeEnum;
 import com.kingdee.eas.fi.ar.ArApBillBaseInfo;
 import com.kingdee.eas.fi.ar.BillStatusEnum;
 //import com.kingdee.eas.fi.cas.BillStatusEnum;
 import com.kingdee.eas.fi.cas.CasRecPayBillTypeEnum;
 import com.kingdee.eas.fi.cas.PaymentBillEntryInfo;
 import com.kingdee.eas.fi.cas.PaymentBillFactory;
 import com.kingdee.eas.fi.cas.PaymentBillInfo;
 import com.kingdee.eas.fi.cas.PaymentBillTypeFactory;
 import com.kingdee.eas.fi.cas.PaymentBillTypeInfo;
 import com.kingdee.eas.fi.cas.SourceTypeEnum;
 import com.kingdee.eas.framework.CoreBaseInfo;
 import com.kingdee.eas.framework.CoreBillBaseInfo;
 import com.kingdee.eas.scm.common.PurchaseTypeEnum;
 import com.kingdee.eas.scm.sm.pur.PurRequestEntryInfo;
 import com.kingdee.eas.scm.sm.pur.PurRequestFactory;
 import com.kingdee.eas.scm.sm.pur.PurRequestInfo;
 import com.kingdee.eas.util.app.DbUtil;
 import com.kingdee.jdbc.rowset.IRowSet;
 import java.math.BigDecimal;
 import java.sql.SQLException;
 import java.sql.Timestamp;
 import java.text.DateFormat;
 import java.text.ParseException;
 import java.text.SimpleDateFormat;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.Calendar;
 import java.util.Date;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Locale;
 import java.util.Map;
import org.apache.log4j.Logger;
 
 public class BusinessFormOAControllerBean extends AbstractBusinessFormOAControllerBean {
   private static Logger logger = Logger.getLogger("com.kingdee.eas.custom.app.BusinessFormOAControllerBean");
   
   protected String _ApOtherFormOA(Context ctx, String database) throws BOSException {
     String sql = null;
     updateNoPeople(ctx, database);
     updateNoExpenseType(ctx, database);
     sql = " select bx.id,bx.fnumber,bx.bizDate,bx.isLoan,bx.payType,bx.isrentalfee,bx.company,bx.Dept,bx.supplierid,bx.Yhzh,bx.Khh,bx.applyer,  bx.Applyerbank,bx.Applyerbanknum,bx.Agency,bx.Amount,bx.Jsfs,bx.purchType,bx.purchModel,bx.Paystate,bx.Paystatetime  from eas_lolkk_bx bx  where bx.eassign = 0 and bx.PURCHTYPE != '08' ";
     Calendar cal = Calendar.getInstance();
     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
     List<Map<String, Object>> list = EAISynTemplate.query(ctx, database, sql.toString());
     System.out.println("--------------------------" + list.size());
     for (Map<String, Object> map : list) {
       if (map.get("PURCHTYPE") != null && !map.get("PURCHTYPE").toString().equals("03")) {
         apOtherNotShichang(ctx, database, map);
         continue;
       } 
       if (map.get("PURCHTYPE") != null && map.get("PURCHTYPE").toString().equals("03"))
         apOtherIsShichang(ctx, database, map); 
     } 
     return super._ApOtherFormOA(ctx, database);
   }
   
   private void updateNoExpenseType(Context ctx, String database) {
     String updateSql = "UPDATE  eas_lolkk_bx  set eassign = 2 , EASTIME = TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS'),EASLOG='分录上某个费用类型禁用'  where ID in (select bx.id from eas_lolkk_bx bx left JOIN eas_lolkk_bx_sub sub  on sub.parentid = bx.id  left JOIN EAS_PAYTYPE_OA_MIDTABLE paytype  on paytype.fnumber = sub.paytypecode where bx.eassign = 0 and paytype.fstatus = 2 )";
     System.out.print("--------------" + updateSql);
     try {
       EAISynTemplate.execute(ctx, database, updateSql);
     } catch (BOSException e) {
       e.printStackTrace();
     } 
   }
   
   protected String _PayApplyToOA(Context ctx, String database, String billId) throws BOSException {
     String result = "";
     if (!isExistsByBillId(ctx, database, billId).booleanValue()) {
       try {
         PayRequestBillInfo payRequestBillInfo = 
           PayRequestBillFactory.getLocalInstance(ctx).getPayRequestBillInfo(
             (IObjectPK)new ObjectUuidPK(billId));
         PayRequestBillEntryCollection entryCollection = payRequestBillInfo
           .getEntrys();
         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
         String id = payRequestBillInfo.getId().toString();
         String fnumber = payRequestBillInfo.getNumber();
         String bizDate = sdf.format(payRequestBillInfo.getBizDate());
         String Formtype = "6";
         String Suppliernum = "";
         String Suppliername = "";
         String Supplierbank = "";
         String Supplierbanknum = "";
         String prex = "";
         boolean isPrcosess = false;
         MaterialInfo materialInfo = null;
         if (entryCollection.size() > 0) {
           PayRequestBillEntryInfo entryOne = entryCollection.get(0);
           materialInfo = entryOne.getMaterialNo();
           if (materialInfo != null && materialInfo.getId() != null && !"".equals(materialInfo.getId().toString()))
             isPrcosess = AppUnit.isProcessMaterialByID(ctx, materialInfo.getId().toString()); 
           Suppliernum = entryOne.getAsstActNumber();
           Suppliername = entryOne.getAsstActName();
           String querysql = "select fnumber,fopenbank,fbankaccount from eas_supplier_midtable where fnumber = '" + 
             Suppliernum + "'";
           List<Map<String, Object>> banklist = EAISynTemplate.query(
               ctx, database, querysql);
           if (banklist != null && banklist.size() > 0 && 
             banklist.get(0) != null) {
             Map bankMap = banklist.get(0);
             Supplierbank = (bankMap.get("FOPENBANK") == null) ? "\\" : 
               bankMap.get("FOPENBANK").toString();
             Supplierbanknum = (bankMap.get("FBANKACCOUNT") == null) ? "\\" : 
               bankMap.get("FBANKACCOUNT").toString();
           } 
         } 
         String Usedate = "";
         UserInfo uInfo = UserFactory.getLocalInstance(ctx).getUserInfo(
             (IObjectPK)new ObjectUuidPK(
               payRequestBillInfo.getCreator().getId()));
         CompanyOrgUnitInfo couInfo = 
           CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo(
             (IObjectPK)new ObjectUuidPK(
               payRequestBillInfo.getCompany().getId()));
         String Companynum = couInfo.getId().toString();
         String Company = couInfo.getName();
         String Gzamount = payRequestBillInfo.getRequestAmount()
           .toString();
         String requestAmount = payRequestBillInfo.getAuditAmount().toString();
         String invoiceNumber = (payRequestBillInfo.get("fapiaohao") != null) ? payRequestBillInfo.get("fapiaohao").toString() : "";
         String purpose = (payRequestBillInfo.getDescription() != null) ? payRequestBillInfo.getDescription() : "";
         SettlementTypeInfo stInfo = 
           SettlementTypeFactory.getLocalInstance(ctx)
           .getSettlementTypeInfo(
             (IObjectPK)new ObjectUuidPK(
               "e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
         String Jsfs = stInfo.getNumber();
         String Eassign = "0";
         String Eastime = sdf.format(new Date());
         String oasign = "0";
         String Oatime = sdf.format(new Date());
         String applyer = null;
         String personid = "";
         String requestReason = (payRequestBillInfo.getRequestReason() != null) ? payRequestBillInfo.getRequestReason() : "";
// */         if (payRequestBillInfo.getApplyer() != null && payRequestBillInfo.getApplyer().getId() != null && !"".equals(payRequestBillInfo.getApplyer().getId().toString())) {
// */           personid = payRequestBillInfo.getApplyer().getId().toString();
// */         } else {
// */           personid = "6tB2iCtgRFGVooJDiONYlYDvfe0=";
// */         } 
// */         PersonInfo person = PersonFactory.getLocalInstance(ctx).getPersonInfo((IObjectPK)new ObjectUuidPK(personid));
// */         applyer = person.getNumber();
			PersonInfo person = null; 
			if (payRequestBillInfo.getString("oacaigoushenqingdandanhao") != null && !"".equals(payRequestBillInfo.getString("oacaigoushenqingdandanhao"))) {
			  Map map1 = AppUnit.getPurchModelFromOANum(ctx, payRequestBillInfo.getString("oacaigoushenqingdandanhao"));
			  if (map1.get("person") != null && !"".equals(map1.get("person"))) {
			   personid = (String)map1.get("person");
			    if (!PayRequestBillUtil.queryPersonStatus(ctx, personid))
			     personid = payRequestBillInfo.getApplyer().getId().toString(); 
			 } 
			} else {
			 personid = payRequestBillInfo.getApplyer().getId().toString();
			} 
			person = PersonFactory.getLocalInstance(ctx).getPersonInfo((IObjectPK)new ObjectUuidPK(personid));
			applyer = person.getNumber();
         List<Map<String, String>> entrys = new ArrayList<Map<String, String>>();
         List<Map<String, String>> oanumbers = AppUnit.getSumPayRequestBillEntrys(ctx, id);
         String type = "";
         if (oanumbers != null && oanumbers.size() > 0)
           for (Map<String, String> mp : oanumbers) {
             Map<String, String> map = new HashMap<String, String>();
             map.put("parentID", id);
             map.put("fnumber", ((String)mp.get("oanumber")).toString());
             map.put("Companynum", couInfo.getId().toString());
             map.put("Suppliername", couInfo.getName());
             map.put("requestAmount", ((String)mp.get("amount")).toString());
             type = AppUnit.getPurTypeByOANumber(((String)mp.get("oanumber")).toString(), isPrcosess);
             map.put("formtypezhi", type);
             Formtype = type;
             entrys.add(map);
           }  
         String sql = "insert into eas_lolkk_fk (id,fnumber,bizDate,Formtype,InvoiceNumber,Suppliernum,Suppliername,Supplierbank,Supplierbanknum,Usedate,applyer,Companynum,Company,Gzamount,requestAmount,purpose,Jsfs,requestReason,Eassign,Eastime,oasign,Oatime,oafinishsign,paystate) values('" + 
           id + "','" + fnumber + "','" + bizDate + "','" + Formtype + "','" + invoiceNumber + "','" + 
           Suppliernum + "','" + Suppliername + "','" + Supplierbank + "','" + Supplierbanknum + "','" + 
           Usedate + "','" + applyer + "','" + Companynum + "','" + Company + "','" + 
           Gzamount + "'," + requestAmount + ",'" + purpose + "','" + 
           Jsfs + "','" + requestReason + "'," + Eassign + ",'" + Eastime + "'," + oasign + ",'" + Oatime + "',0,0)";
         String sqlEntry = "";
         for (int i = 0; i < entrys.size(); i++) {
           Map<String, String> map = entrys.get(i);
           sqlEntry = "insert into eas_lolkk_fk_sub(parentID,fnumber,Companynum,Companyname,requestAmount,formtypezhi) values('" + 
             (String)map.get("parentID") + 
             "','" + 
             (String)map.get("fnumber") + 
             "','" + 
             (String)map.get("Companynum") + 
             "','" + 
             (String)map.get("Suppliername") + 
             "'," + 
             (String)map.get("requestAmount") + "," + (String)map.get("formtypezhi") + ")";
           System.out.println("sqlEntry:" + sqlEntry);
           EAISynTemplate.execute(ctx, database, sqlEntry);
           AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
               DateBasetype.PaymentBillToMid, 
               payRequestBillInfo.getNumber(), 
               payRequestBillInfo.getString("PaymentBillToMid"), "单据保存成功");
         } 
         ArrayList<Map<String, String>> attachmentlist = AppUnit.getAttachmentList(ctx, id);
         if (attachmentlist != null && attachmentlist.size() > 0)
           for (Map<String, String> mp : attachmentlist) {
             sqlEntry = "insert into eas_lolkk_fk_file(parentID,filename,filepath) values('" + 
               id + "','" + (String)mp.get("pre") + "','" + 
               (String)mp.get("path") + "')";
             EAISynTemplate.execute(ctx, database, sqlEntry);
           }  
         System.out.println("sql:" + sql);
         EAISynTemplate.execute(ctx, database, sql);
         result = "运行结束，插入成功。";
       } catch (EASBizException e) {
         e.printStackTrace();
         AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
             DateBasetype.PaymentBillToMid, "单据保存失败", 
             e.getMessage());
         String msg = "运行失败，异常是：" + e.toString();
         return msg;
       } 
     } else {
       result = "运行失败，数据已经存在。";
     } 
     return result;
   }
   
   protected Boolean isExistsByBillId(Context ctx, String database, String billID) {
     return Boolean.valueOf(EAISynTemplate.existsoa(ctx, database, "eas_lolkk_fk", billID));
   }
   
   protected String _PurRequestFormOA(Context ctx, String dataBase1) throws BOSException {
     String sql = null;
     sql = "select id,fnumber,bizdate,purchType,purchModel,company,requestAmount,applyer,CGFK_APPLYER,isGift from eas_lolkk_cg where eassign = 0";
     Calendar cal = Calendar.getInstance();
     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
     List<Map<String, Object>> list = EAISynTemplate.query(ctx, dataBase1, sql.toString());
     String demandTypeNo = "010";
     String fid = null;
     int submitInt = 0;
     boolean giftFlag = false;
     String unitID = "";
     String baseUnitID = "";
     BigDecimal qtymultiple = new BigDecimal(1);
     try {
       System.out.println("--------------------------" + list.size());
       for (Map<String, Object> map : list) {
         submitInt = 0;
         Boolean addOrUpdate = Boolean.valueOf(false);
         Boolean flag = Boolean.valueOf(true);
         fid = map.get("ID").toString();
         PurRequestInfo info = null;
         System.out.println("_--------------------------------------" + 
             map.get("FNUMBER") + "====" + map.get("COMPANY") + 
             "-----");
         if (map.get("COMPANY") == null || map.get("COMPANY").equals("")) {
           System.out
             .println("_--------------------------------------" + 
               map.get("FNUMBER"));
           updateFSign(ctx, dataBase1, "eas_lolkk_cg", 2, fid);
           AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
               DateBasetype.OA_PurRequest, info.getNumber(), 
               info.getString("OA_PurRequest"), "单据保存失败，" + 
               info.getNumber() + "的公司编码为空");
           continue;
         } 
         if (map.get("FNUMBER") != null && 
           !map.get("FNUMBER").equals("")) {
           if (PurRequestFactory.getLocalInstance(ctx).exists("where caigoushenqingdandanhao ='" + map.get("FNUMBER") + "'")) {
             addOrUpdate = Boolean.valueOf(true);
             updateFSign(ctx, dataBase1, "eas_lolkk_cg", 1, fid);
             continue;
           } 
           info = new PurRequestInfo();
         } else {
           info = new PurRequestInfo();
         } 
         info.setIsMergeBill(false);
         info.setPurchaseType(PurchaseTypeEnum.PURCHASE);
         BizTypeInfo bizTypeinfo = BizTypeFactory.getLocalInstance(ctx)
           .getBizTypeCollection("where number = '110'").get(0);
         info.setBizType(bizTypeinfo);
         RowTypeInfo rowTypeInfoy = RowTypeFactory.getLocalInstance(ctx)
           .getRowTypeInfo("where number = '010'");
         RowTypeInfo rowTypeInfod = RowTypeFactory.getLocalInstance(ctx)
           .getRowTypeInfo("where number = '210'");
         RowTypeInfo rowTypeInfog = RowTypeFactory.getLocalInstance(ctx)
           .getRowTypeInfo("where number = '200'");
         CurrencyInfo currency = CurrencyFactory.getLocalInstance(ctx)
           .getCurrencyCollection("where number='BB01'").get(0);
         ObjectUuidPK orgPK = new ObjectUuidPK(
             map.get("COMPANY").toString());
         CompanyOrgUnitInfo xmcompany = 
           CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo((IObjectPK)orgPK);
         info.setCompanyOrgUnit(xmcompany);
         System.out.println("------------------所属公司：" + 
             xmcompany.getId() + "----" + xmcompany.getName());
         AdminOrgUnitInfo admin = AdminOrgUnitFactory.getLocalInstance(
             ctx).getAdminOrgUnitInfo((IObjectPK)orgPK);
         info.setAdminOrg(admin);
         StorageOrgUnitInfo storageorginfo = 
           StorageOrgUnitFactory.getLocalInstance(ctx).getStorageOrgUnitInfo((IObjectPK)orgPK);
         PurchaseOrgUnitInfo purchaseorginfo = 
           PurchaseOrgUnitFactory.getLocalInstance(ctx).getPurchaseOrgUnitInfo((IObjectPK)orgPK);
         PersonInfo person = PersonFactory.getLocalInstance(ctx)
           .getPersonCollection(
             "where number='" + 
             map.get("APPLYER").toString() + "'")
           .get(0);
         info.setPerson(person);
         if (map.get("CGFK_APPLYER") != null && 
           !"".equals(map.get("CGFK_APPLYER").toString())) {
           PersonInfo CGFK_APPLYER = PersonFactory.getLocalInstance(
               ctx).getPersonCollection(
               "where number='" + 
               map.get("CGFK_APPLYER").toString() + "'")
             .get(0);
           info.put("caigourenyuan", CGFK_APPLYER);
         } 
         SimpleDateFormat formmat = new SimpleDateFormat(
             "yyyy-MM-dd hh:mm:ss");
         Date bizDate = null;
         String purchModel = "";
         try {
           if (map.get("BIZDATE") != null && 
             !"".equals(map.get("BIZDATE").toString())) {
             bizDate = formmat.parse(map.get("BIZDATE").toString());
           } else {
             bizDate = new Date();
           } 
         } catch (ParseException e) {
           e.printStackTrace();
         } 
         info.setBizDate(bizDate);
         info.put("caigoushenqingdandanhao", map.get("FNUMBER"));
         if (VerifyUtil.notNull(map.get("PURCHMODEL")))
           if ("gaozhi".equals(map.get("PURCHMODEL"))) {
             purchModel = "1";
           } else if ("dizhi".equals(map.get("PURCHMODEL"))) {
             purchModel = "2";
           } else if ("shebei".equals(map.get("PURCHMODEL"))) {
             purchModel = "3";
           } else if ("qixie".equals(map.get("PURCHMODEL"))) {
             purchModel = "4";
           } else if ("xinzeng".equals(map.get("PURCHMODEL"))) {
             purchModel = "5";
           } else if ("hulichuanpin".equals(map.get("PURCHMODEL"))) {
             purchModel = "6";
           } else if ("feiqixie".equals(map.get("PURCHMODEL"))) {
             purchModel = "7";
           }  
         giftFlag = false;
         if (map.get("ISGIFT") != null && !"".equals(map.get("ISGIFT").toString())) {
           info.put("isGift", map.get("ISGIFT"));
           if (("1".equals(purchModel) || "3".equals(purchModel) || "4".equals(purchModel) || "7".equals(purchModel)) && 
             "1".equals(map.get("ISGIFT").toString()))
             giftFlag = true; 
         } else {
           info.put("isGift", "0");
         } 
         if (VerifyUtil.notNull(purchModel))
           info.put("danjuleixzing", purchModel); 
         BillTypeInfo billtype = new BillTypeInfo();
         billtype.setId(
             BOSUuid.read("510b6503-0105-1000-e000-0107c0a812fd463ED552"));
         info.setBillType(billtype);
         if (map.get("PURCHTYPE") != null && 
           map.get("PURCHTYPE").equals("10")) {
           if (flag.booleanValue()) {
             PurRequestInfo infoBig = (PurRequestInfo)info.clone();
             sql = "select parentid,material,materialname,supplier,brand,guige,xh,artNo,price,qty,amount from eas_lolkk_cg_sub where parentid =" + 
               map.get("ID");
             List<Map<String, Object>> list1 = EAISynTemplate.query(
                 ctx, dataBase1, sql);
             BigDecimal totalAmountSmall = new BigDecimal(0);
             BigDecimal totalAmountBig = new BigDecimal(0);
             if (list1 != null && list1.size() > 0) {
               for (Map<String, Object> map1 : list1) {
                 PurRequestEntryInfo entryInfo = new PurRequestEntryInfo();
                 qtymultiple = new BigDecimal(1);
                 try {
                   EntityViewInfo viewInfo = new EntityViewInfo();
                   FilterInfo filter = new FilterInfo();
                   filter.getFilterItems().add(
                       new FilterItemInfo("number", 
                         map1.get("MATERIAL")
                         .toString(), 
                         CompareType.EQUALS));
                   viewInfo.setFilter(filter);
                   IMaterial imaterial = 
                     MaterialFactory.getLocalInstance(ctx);
                   MaterialCollection collection = imaterial
                     .getMaterialCollection(viewInfo);
                   MaterialInfo material = collection.get(0);
                   entryInfo.setMaterialName(
                       material.getName());
                   entryInfo.setNoNumMaterialModel(
                       material.getModel());
                   entryInfo.setMaterial(material);
                   MeasureUnitInfo unitInfo = new MeasureUnitInfo();
                   MeasureUnitInfo baseUnitInfo = new MeasureUnitInfo();
                   unitID = materialUnitId(ctx, map.get("COMPANY").toString(), material.getId().toString());
                   if (unitID != null && !"".equals(unitID)) {
                     unitInfo.setId(BOSUuid.read(unitID));
                     baseUnitInfo = material.getBaseUnit();
                     if (!material.getBaseUnit().getId().toString().equals(unitID))
                       qtymultiple = getmaterialMultiple(ctx, material.getId().toString(), unitID); 
                   } else {
                     unitInfo = material.getBaseUnit();
                     baseUnitInfo = material.getBaseUnit();
                   } 
                   entryInfo.setBaseUnit(baseUnitInfo);
                   entryInfo.setUnit(unitInfo);
                 } catch (Exception e) {
                   updateFSign(ctx, dataBase1, "eas_lolkk_cg", 
                       2, fid);
                   AppUnit.insertLog(ctx, 
                       DateBaseProcessType.AddNew, 
                       DateBasetype.OA_PurRequest, 
                       info.getNumber(), 
                       map.get("FNUMBER").toString(), 
                       "单据保存失败，" + info.getNumber() + 
                       "物料编码不存在");
                 } 
                 BigDecimal amount = new BigDecimal(0.0D);
                 BigDecimal price = new BigDecimal(0.0D);
                 if (map1.get("AMOUNT") != null && 
                   !"".equals(
                     map1.get("AMOUNT").toString().trim()))
                   amount = new BigDecimal(
                       map1.get("AMOUNT").toString().trim()); 
                 if (map1.get("PRICE") != null && 
                   !"".equals(
                     map1.get("PRICE").toString().trim()))
                   price = new BigDecimal(
                       map1.get("PRICE").toString().trim()); 
                 BigDecimal qty = new BigDecimal(
                     map1.get("QTY").toString());
                 if (VerifyUtil.notNull(map1.get("GUIGE")))
                   entryInfo.setNoNumMaterialModel(
                       map1.get("GUIGE").toString()); 
                 if (VerifyUtil.notNull(map1.get("SUPPLIER")))
                   if (SupplierFactory.getLocalInstance(ctx)
                     .exists(
                       "where number='" + 
                       map1
                       .get("SUPPLIER") + 
                       "'")) {
                     SupplierInfo supplierInfo = 
                       SupplierFactory.getLocalInstance(ctx)
                       .getSupplierInfo(
                         " where number='" + 
                         map1
                         .get("SUPPLIER") + 
                         "'");
                     entryInfo.setSupplier(supplierInfo);
                   }  
                 entryInfo.setPerson(person);
                 entryInfo.setPurchasePerson(person);
                 entryInfo.setReceivedOrgUnit(storageorginfo);
                 entryInfo.setAdminOrgUnit(admin);
                 entryInfo.setBizDate(info.getBizDate());
                 entryInfo.setQty(qty);
                 entryInfo.setAssociateQty(qty);
                 entryInfo.setBaseQty(qty.multiply(qtymultiple));
                 entryInfo.setRequestQty(qty);
                 entryInfo.setUnOrderedQty(BigDecimal.ZERO);
                 entryInfo.setUnOrderedBaseQty(BigDecimal.ZERO);
                 entryInfo.setAssistQty(BigDecimal.ZERO);
                 entryInfo
                   .setExchangeRate(new BigDecimal("1.00"));
                 entryInfo.setPrice(price);
                 entryInfo.setTaxPrice(price);
                 entryInfo.setActualPrice(price);
                 entryInfo.setActualTaxPrice(price);
                 entryInfo.setTaxAmount(amount);
                 entryInfo.setCurrency(currency);
                 entryInfo.setStorageOrgUnit(storageorginfo);
                 entryInfo.setPurchaseOrgUnit(purchaseorginfo);
                 entryInfo.setPurchasePerson(person);
                 entryInfo.setLocalAmount(amount);
                 entryInfo.setLocalTaxAmount(amount);
                 entryInfo.setAmount(amount);
                 entryInfo.setParent(info);
                 entryInfo.setLocalTaxAmount(amount);
                 entryInfo.setActualPrice(price);
                 entryInfo.setActualTaxPrice(price);
                 entryInfo.setOrderedQty(qty);
                 entryInfo.setAssOrderBaseQty(qty);
                 String gugexh = "";
                 if (map1.get("GUIGE") != null && 
                   
                   !map1.get("GUIGE").toString().equals("")) {
                   gugexh = map1.get("GUIGE").toString();
                   entryInfo.setNoNumMaterialModel(gugexh);
                 } 
                 entryInfo.put("xinghao", map1.get("XH"));
                 entryInfo.put("pinpai", map1.get("BRAND"));
                 entryInfo.put("huohao", map1.get("ARTNO"));
                 entryInfo.setRequirementDate(bizDate);
                 entryInfo.setProposeDeliveryDate(bizDate);
                 entryInfo.setProposePurchaseDate(bizDate);
                 if (giftFlag) {
                   entryInfo.setRowType(rowTypeInfoy);
                   info.getEntries().add(entryInfo);
                   totalAmountSmall = totalAmountSmall.add(amount);
                   continue;
                 } 
                 if (price.compareTo(new BigDecimal(2000)) == -1) {
                   entryInfo.setRowType(rowTypeInfod);
                   info.getEntries().add(entryInfo);
                   totalAmountSmall = totalAmountSmall.add(amount);
                   continue;
                 } 
                 entryInfo.setRowType(rowTypeInfog);
                 infoBig.getEntries().add(entryInfo);
                 totalAmountBig = totalAmountBig.add(amount);
               } 
             } else {
               System.out
                 .println("entrty is empty _--------------------------------------" + 
                   map.get("FNUMBER"));
               updateFSign(ctx, dataBase1, "eas_lolkk_cg", 2, fid);
               AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
                   DateBasetype.OA_PurRequest, 
                   info.getNumber(), map.get("FNUMBER").toString(), 
                   "单据保存失败，" + info.getNumber() + "的没有分录");
               continue;
             } 
             if (info.getEntries().size() > 0) {
               info.setTotalAmount(totalAmountSmall);
               info.setLocalTotalAmount(totalAmountSmall);
               DemandTypeInfo demandTypeInfo = new DemandTypeInfo();
               if (giftFlag) {
                 demandTypeInfo.setId(BOSUuid.read("d8iX3GB6dt3gUwEAAH8kOKvcMAg="));
               } else {
                 demandTypeInfo.setId(BOSUuid.read("nMyVhAcxRvyXPYq+xI49eqvcMAg="));
               } 
               info.setDemandType(demandTypeInfo);
               info.put("caigoushenqingdanjine", totalAmountSmall);
               PurRequestFactory.getLocalInstance(ctx).save((CoreBaseInfo)info);
             } 
             if (infoBig.getEntries().size() > 0) {
               infoBig.setTotalAmount(totalAmountBig);
               infoBig.setLocalTotalAmount(totalAmountBig);
               DemandTypeInfo type2 = new DemandTypeInfo();
               type2.setId(
                   BOSUuid.read("Sp/A4ZhGTD2izLb/R8/WfavcMAg="));
               infoBig.setDemandType(type2);
               infoBig
                 .put("caigoushenqingdanjine", 
                   totalAmountBig);
               PurRequestFactory.getLocalInstance(ctx).save(
                   (CoreBaseInfo)infoBig);
             } 
             if (info.getEntries().size() > 0) {
               submitInt = 1;
               PurRequestFactory.getLocalInstance(ctx)
                 .submit((CoreBaseInfo)info);
             } 
             if (infoBig.getEntries().size() > 0) {
               submitInt = 2;
               PurRequestFactory.getLocalInstance(ctx).submit(
                   (CoreBaseInfo)infoBig);
             } 
             updateFSign(ctx, dataBase1, "eas_lolkk_cg", 1, 
                 map.get("ID").toString());
             AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
                 DateBasetype.OA_PurRequest, info.getNumber(), 
                 map.get("FNUMBER").toString(), "单据保存成功");
             continue;
           } 
           PurRequestFactory.getLocalInstance(ctx).save((CoreBaseInfo)info);
           submitInt = 1;
           PurRequestFactory.getLocalInstance(ctx).submit((CoreBaseInfo)info);
           updateFSign(ctx, dataBase1, "eas_lolkk_cg", 1, 
               map.get("ID").toString());
           AppUnit.insertLog(ctx, DateBaseProcessType.Update, 
               DateBasetype.OA_PurRequest, info.getNumber(), 
               map.get("FNUMBER").toString(), "单据修改成功");
           continue;
         } 
         BigDecimal totalAmount = new BigDecimal(0);
         if (flag.booleanValue()) {
           sql = "select parentid,material,materialname,supplier,brand,guige,xh,artNo,price,qty,amount from eas_lolkk_cg_sub where parentid =" + 
             map.get("ID");
           List<Map<String, Object>> list1 = EAISynTemplate.query(
               ctx, dataBase1, sql);
           if (list1 != null && list1.size() > 0) {
             for (Map<String, Object> map1 : list1) {
               PurRequestEntryInfo entryInfo = new PurRequestEntryInfo();
               qtymultiple = new BigDecimal(1);
               entryInfo.setRowType(rowTypeInfoy);
               try {
                 EntityViewInfo viewInfo = new EntityViewInfo();
                 FilterInfo filter = new FilterInfo();
                 filter.getFilterItems().add(
                     new FilterItemInfo("number", 
                       map1.get("MATERIAL")
                       .toString(), 
                       CompareType.EQUALS));
                 viewInfo.setFilter(filter);
                 IMaterial imaterial = 
                   MaterialFactory.getLocalInstance(ctx);
                 MaterialCollection collection = imaterial
                   .getMaterialCollection(viewInfo);
                 MaterialInfo material = collection.get(0);
                 entryInfo.setMaterialName(
                     material.getName());
                 entryInfo.setNoNumMaterialModel(
                     material.getModel());
                 entryInfo.setMaterial(material);
                 MeasureUnitInfo unitInfo = new MeasureUnitInfo();
                 MeasureUnitInfo baseUnitInfo = new MeasureUnitInfo();
                 unitID = materialUnitId(ctx, map.get("COMPANY").toString(), material.getId().toString());
                 if (unitID != null && !"".equals(unitID)) {
                   unitInfo.setId(BOSUuid.read(unitID));
                   baseUnitInfo = material.getBaseUnit();
                   if (!material.getBaseUnit().getId().toString().equals(unitID))
                     qtymultiple = getmaterialMultiple(ctx, material.getId().toString(), unitID); 
                 } else {
                   unitInfo = material.getBaseUnit();
                   baseUnitInfo = material.getBaseUnit();
                 } 
                 entryInfo.setBaseUnit(baseUnitInfo);
                 entryInfo.setUnit(unitInfo);
               } catch (Exception e) {
                 updateFSign(ctx, dataBase1, "eas_lolkk_cg", 
                     2, fid);
                 AppUnit.insertLog(ctx, 
                     DateBaseProcessType.AddNew, 
                     DateBasetype.OA_PurRequest, 
                     info.getNumber(), map.get("FNUMBER").toString(), 
                     "单据保存失败，" + info.getNumber() + 
                     "物料编码不存在");
               } 
               BigDecimal amount = new BigDecimal(0.0D);
               BigDecimal price = new BigDecimal(0.0D);
               if (map1.get("AMOUNT") != null && 
                 !"".equals(
                   map1.get("AMOUNT").toString().trim()))
                 amount = new BigDecimal(
                     map1.get("AMOUNT").toString().trim()); 
               if (map1.get("PRICE") != null && 
                 !"".equals(
                   map1.get("PRICE").toString().trim()))
                 price = new BigDecimal(
                     map1.get("PRICE").toString().trim()); 
               BigDecimal qty = new BigDecimal(
                   map1.get("QTY").toString());
               if (VerifyUtil.notNull(map1.get("GUIGE")))
                 entryInfo.setNoNumMaterialModel(
                     map1.get("GUIGE").toString()); 
               if (VerifyUtil.notNull(map1.get("SUPPLIER")))
                 if (SupplierFactory.getLocalInstance(ctx)
                   .exists(
                     "where number='" + 
                     map1
                     .get("SUPPLIER") + 
                     "'")) {
                   SupplierInfo supplierInfo = 
                     SupplierFactory.getLocalInstance(ctx)
                     .getSupplierInfo(
                       " where number='" + 
                       map1
                       .get("SUPPLIER") + 
                       "'");
                   entryInfo.setSupplier(supplierInfo);
                 }  
               entryInfo.setPerson(person);
               entryInfo.setPurchasePerson(person);
               entryInfo.setReceivedOrgUnit(storageorginfo);
               entryInfo.setAdminOrgUnit(admin);
               entryInfo.setBizDate(info.getBizDate());
               entryInfo.setQty(qty);
               entryInfo.setAssociateQty(qty);
               entryInfo.setBaseQty(qty.multiply(qtymultiple));
               entryInfo.setUnOrderedQty(BigDecimal.ZERO);
               entryInfo.setUnOrderedBaseQty(BigDecimal.ZERO);
               entryInfo.setAssistQty(BigDecimal.ZERO);
               entryInfo
                 .setExchangeRate(new BigDecimal("1.00"));
               entryInfo.setPrice(price);
               entryInfo.setTaxPrice(price);
               entryInfo.setActualPrice(price);
               entryInfo.setActualTaxPrice(price);
               entryInfo.setTaxAmount(amount);
               entryInfo.setCurrency(currency);
               entryInfo.setStorageOrgUnit(storageorginfo);
               entryInfo.setPurchaseOrgUnit(purchaseorginfo);
               entryInfo.setPurchasePerson(person);
               entryInfo.setLocalAmount(amount);
               entryInfo.setLocalTaxAmount(amount);
               entryInfo.setAmount(amount);
               entryInfo.setParent(info);
               String gugexh = "";
               if (map1.get("GUIGE") != null && 
                 
                 !map1.get("GUIGE").toString().equals("")) {
                 gugexh = map1.get("GUIGE").toString();
                 entryInfo.setNoNumMaterialModel(gugexh);
               } 
               entryInfo.put("xinghao", map1.get("XH"));
               entryInfo.put("pinpai", map1.get("BRAND"));
               entryInfo.put("huohao", map1.get("ARTNO"));
               entryInfo.setRequestQty(qty);
               entryInfo.setRequirementDate(bizDate);
               entryInfo.setProposeDeliveryDate(bizDate);
               entryInfo.setProposePurchaseDate(bizDate);
               totalAmount = totalAmount.add(amount);
               info.getEntries().add(entryInfo);
             } 
           } else {
             System.out
               .println("entrty is empty _--------------------------------------" + 
                 map.get("FNUMBER"));
             updateFSign(ctx, dataBase1, "eas_lolkk_cg", 2, fid);
             AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
                 DateBasetype.OA_PurRequest, 
                 info.getNumber(), map.get("FNUMBER").toString(), 
                 "单据保存失败，" + info.getNumber() + "的没有分录");
             continue;
           } 
         } 
         info.put("caigoushenqingdanjine", totalAmount);
         info.setTotalAmount(totalAmount);
         info.setLocalTotalAmount(totalAmount);
         DemandTypeInfo type = new DemandTypeInfo();
         type.setId(BOSUuid.read("d8iX3GB6dt3gUwEAAH8kOKvcMAg="));
         info.setDemandType(type);
         PurRequestFactory.getLocalInstance(ctx).save((CoreBaseInfo)info);
         submitInt = 1;
         PurRequestFactory.getLocalInstance(ctx).submit((CoreBaseInfo)info);
         updateFSign(ctx, dataBase1, "eas_lolkk_cg", 1, 
             map.get("ID").toString());
         if (addOrUpdate.booleanValue()) {
           AppUnit.insertLog(ctx, DateBaseProcessType.Update, 
               DateBasetype.OA_PurRequest, info.getNumber(), 
               info.getString("OA_PurRequest"), "单据修改成功");
           continue;
         } 
         AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
             DateBasetype.OA_PurRequest, info.getNumber(), 
             info.getString("OA_PurRequest"), "单据保存成功");
       } 
     } catch (EASBizException e) {
       e.printStackTrace();
       String msg = "";
       if (submitInt == 0) {
         AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
             DateBasetype.OA_PurRequest, String.valueOf(fid) + "单据保存失败", e.getMessage());
         if (fid != null && !fid.equals(""))
           updateFSign(ctx, dataBase1, "eas_lolkk_cg", 2, fid); 
         msg = "运行失败，异常是：" + e.toString();
       } else {
         msg = "单据提交失败: " + e.toString();
         updateFSign(ctx, dataBase1, "eas_lolkk_cg", 1, fid);
       } 
       return msg;
     } 
     return super._PurRequestFormOA(ctx, dataBase1);
   }
   
   public String syncPaymentBillFormOA(Context ctx, String database) throws BOSException {
     String sql = null;
     sql = " select bx.id,bx.fnumber,bx.bizDate,bx.isLoan,bx.payType,bx.isrentalfee,bx.company,bx.Dept,bx.supplierid,bx.Yhzh,bx.Khh,bx.applyer,  bx.Applyerbank,bx.Applyerbanknum,bx.Agency,bx.Amount,bx.Jsfs,bx.purchType,bx.purchModel,bx.Paystate,bx.Paystatetime  from eas_lolkk_bx bx   where bx.PURCHTYPE = '08' and bx.eassign = 0";
     Calendar cal = Calendar.getInstance();
     List<Map<String, Object>> list = EAISynTemplate.query(ctx, database, sql.toString());
     String fid = null;
     try {
       System.out.println("--------------------------" + list.size());
       for (Map<String, Object> map : list) {
         fid = map.get("ID").toString();
         PaymentBillInfo payInfo = new PaymentBillInfo();
         if (map.get("COMPANY") == null || map.get("COMPANY").toString().equals("")) {
           System.out.println("_--------------------------------------" + map.get("FNUMBER"));
           updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
           AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.OA_PaymentBill, payInfo.getNumber(), payInfo.getString("OA_PaymentBill"), "单据保存失败," + payInfo.getNumber() + "的公司编码为空");
           continue;
         } 
         if (map.get("FNUMBER") != null && !map.get("FNUMBER").toString().equals("") && 
           PaymentBillFactory.getLocalInstance(ctx).exists("where caigoushenqingdandanhao ='" + map.get("FNUMBER") + "'")) {
           updateFSign(ctx, database, "eas_lolkk_bx", 1, map.get("ID").toString());
           continue;
         } 
         payInfo.setSourceType(SourceTypeEnum.AP);
         payInfo.setDescription("无");
         payInfo.setIsExchanged(false);
         payInfo.setExchangeRate(new BigDecimal("1.00"));
         payInfo.setLastExhangeRate(new BigDecimal("0.00"));
         payInfo.setIsInitializeBill(false);
         CurrencyInfo currency = CurrencyFactory.getLocalInstance(ctx).getCurrencyCollection("where number='BB01'").get(0);
         payInfo.setCurrency(currency);
         payInfo.setFiVouchered(false);
         payInfo.setIsLanding(false);
         AsstActTypeInfo actType = new AsstActTypeInfo();
         actType.setId(BOSUuid.read("YW3xsAEJEADgAAWgwKgTB0c4VZA="));
         payInfo.setPayeeType(actType);
         payInfo.setIsImport(false);
         payInfo.setIsNeedPay(true);
         payInfo.setIsReverseLockAmount(true);
         payInfo.setPaymentBillType(CasRecPayBillTypeEnum.commonType);
         PaymentBillTypeInfo billType = PaymentBillTypeFactory.getLocalInstance(ctx).getPaymentBillTypeInfo((IObjectPK)new ObjectUuidPK("NLGLdwEREADgAAHjwKgSRj6TKVs="));
         payInfo.setPayBillType(billType);
         PaymentTypeInfo paymentTypeInfo = PaymentTypeFactory.getLocalInstance(ctx).getPaymentTypeInfo((IObjectPK)new ObjectUuidPK("2fa35444-5a23-43fb-99ee-6d4fa5f260da6BCA0AB5"));
         payInfo.setPaymentType(paymentTypeInfo);
         SettlementTypeInfo settlementTypeInfo = SettlementTypeFactory.getLocalInstance(ctx)
           .getSettlementTypeInfo((IObjectPK)new ObjectUuidPK("e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
         payInfo.setSettlementType(settlementTypeInfo);
         payInfo.put("caigoushenqingdandanhao", map.get("FNUMBER").toString());
         ObjectUuidPK orgPK = new ObjectUuidPK(map.get("COMPANY").toString());
         CompanyOrgUnitInfo company = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo((IObjectPK)orgPK);
         payInfo.setCompany(company);
         System.out.println("------------------所属公司：" + company.getId() + "----" + company.getName());
         PersonInfo person = PersonFactory.getLocalInstance(ctx).getPersonCollection("where number='" + map.get("APPLYER").toString() + "'").get(0);
         payInfo.setPayeeID(person.getId().toString());
         payInfo.setPayeeName(person.getName());
         payInfo.setPayeeNumber(person.getNumber());
         payInfo.setPayeeBank(map.get("APPLYERBANK").toString());
         payInfo.setPayeeAccountBank(map.get("APPLYERBANKNUM").toString());
         payInfo.put("kaihuhang", map.get("APPLYERBANK").toString());
         payInfo.put("yinhangzhanghao", map.get("APPLYERBANKNUM").toString());
         payInfo.setBankAcctName(person.getName());
         AdminOrgUnitInfo admin = null;
         if (map.get("DEPT") != null && !"".equals(map.get("DEPT").toString())) {
           admin = AdminOrgUnitFactory.getLocalInstance(ctx).getAdminOrgUnitInfo((IObjectPK)new ObjectUuidPK(map.get("DEPT").toString()));
           payInfo.setAdminOrgUnit(admin);
         } 
         SimpleDateFormat formmat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
         Date bizDate = null;
         try {
           if (map.get("BIZDATE") != null && !"".equals(map.get("BIZDATE").toString())) {
             bizDate = formmat.parse(map.get("BIZDATE").toString());
           } else {
             bizDate = new Date();
           } 
         } catch (ParseException e) {
           e.printStackTrace();
         } 
         payInfo.setBizDate(bizDate);
         payInfo.setBillDate(new Date());
         BigDecimal totalAmount = new BigDecimal(map.get("AMOUNT").toString());
         String entrySql = "select parentID,id,payTypecode,payTypeName,Price,qty,amount,Yjk,Ytbk,remark from eas_lolkk_bx_sub where parentid ='" + map.get("ID").toString() + "' ";
         List<Map<String, Object>> enrtyList = EAISynTemplate.query(ctx, database, entrySql.toString());
         if (enrtyList != null && enrtyList.size() > 0) {
           for (Map<String, Object> entryMap : enrtyList) {
             BigDecimal amount = new BigDecimal(entryMap.get("AMOUNT").toString());
             PaymentBillEntryInfo entryInfo = new PaymentBillEntryInfo();
             if (entryMap.get("PAYTYPECODE") != null && !"".equals(entryMap.get("PAYTYPECODE").toString())) {
               ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx)
                 .getExpenseTypeInfo("where number ='" + entryMap.get("PAYTYPECODE").toString() + "'");
               entryInfo.setExpenseType(typeinfo);
             } else {
               ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx).getExpenseTypeInfo("where number ='CL01'");
               entryInfo.setExpenseType(typeinfo);
             } 
             entryInfo.setCurrency(currency);
             entryInfo.setAmount(amount);
             entryInfo.setAmountVc(BigDecimal.ZERO);
             entryInfo.setLocalAmt(amount);
             entryInfo.setLocalAmtVc(BigDecimal.ZERO);
             entryInfo.setUnVcAmount(amount);
             entryInfo.setUnVcLocAmount(amount);
             entryInfo.setUnVerifyExgRateLoc(BigDecimal.ZERO);
             entryInfo.setRebate(BigDecimal.ZERO);
             entryInfo.setRebateAmtVc(BigDecimal.ZERO);
             entryInfo.setRebateLocAmt(BigDecimal.ZERO);
             entryInfo.setRebateLocAmtVc(BigDecimal.ZERO);
             entryInfo.setActualAmt(amount);
             entryInfo.setActualAmtVc(BigDecimal.ZERO);
             entryInfo.setActualLocAmt(amount);
             entryInfo.setActualLocAmtVc(BigDecimal.ZERO);
             entryInfo.setUnLockAmt(amount);
             entryInfo.setUnLockLocAmt(amount);
             entryInfo.setLockAmt(BigDecimal.ZERO);
             entryInfo.setPayableDate(new Date());
             if (entryMap.get("REMARK") != null)
               entryInfo.setRemark(entryMap.get("REMARK").toString()); 
             payInfo.getEntries().addObject((IObjectValue)entryInfo);
           } 
         } else {
           System.out.println("entrty is empty _---------------------------" + map.get("FNUMBER").toString());
           updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
           AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.OA_PaymentBill, payInfo.getNumber(), payInfo.getString("OA_PaymentBill"), "单据没有分录");
           continue;
         } 
         payInfo.setActPayAmtVc(BigDecimal.ZERO);
         payInfo.setActPayAmt(totalAmount);
         payInfo.setActPayLocAmtVc(BigDecimal.ZERO);
         payInfo.setAmount(totalAmount);
         payInfo.setLocalAmt(totalAmount);
         payInfo.setAccessoryAmt(0);
         payInfo.setBgAmount(BigDecimal.ZERO);
         payInfo.setVerifiedAmt(BigDecimal.ZERO);
         payInfo.setVerifiedAmtLoc(BigDecimal.ZERO);
         payInfo.setUnVerifiedAmt(totalAmount);
         payInfo.setUnVerifiedAmtLoc(totalAmount);
         payInfo.setBgCtrlAmt(totalAmount);
         payInfo.setBillStatus(com.kingdee.eas.fi.cas.BillStatusEnum.SAVE);
         PaymentBillFactory.getLocalInstance(ctx).save((CoreBaseInfo)payInfo);
         updateFSign(ctx, database, "eas_lolkk_bx", 1, map.get("ID").toString());
       } 
     } catch (EASBizException e) {
       e.printStackTrace();
       AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.PaymentBillToMid, String.valueOf(fid) + "单据保存失败", e.getMessage());
       String msg = "运行失败，异常是：" + e.toString();
       System.out.println("--------------------" + msg);
       return msg;
     } 
     return super.syncPaymentBillFormOA(ctx, database);
   }
   
   public void updateFSign(Context ctx, String database, String tableName, int fSign, String fid) throws BOSException {
     String updateSql = "UPDATE " + 
       tableName + 
       " set eassign = " + 
       fSign + 
       " , EASTIME = TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') where ID = '" + 
       fid + "'";
     System.out.print("--------------" + updateSql);
     EAISynTemplate.execute(ctx, database, updateSql);
   }
   
   public void updateFSign(Context ctx, String database, String tableName, int fSign, String fid, String sqlServer) throws BOSException {
     String updateSql = "UPDATE " + tableName + " set eassign = " + fSign + 
       " , EASTIME = CONVERT(varchar,GETDATE(),120) where ID = '" + 
       fid + "'";
     System.out.print("--------------" + updateSql);
     EAISynTemplate.execute(ctx, database, updateSql);
   }
   
   public void syncPayApply(Context ctx, String database) throws BOSException {
     String sql = "select id ,fnumber from eas_lolkk_fk where oafinishsign = 1 and eassign=1 and paystate = 0 and CONVERT(varchar(7), bizdate, 120) >= CONVERT(varchar(7), dateadd(month,-3,getdate()) , 120)  order by bizdate desc";
     List<Map<String, Object>> list = EAISynTemplate.query(ctx, database, sql.toString());
     IPayRequestBill ibiz = PayRequestBillFactory.getLocalInstance(ctx);
     for (Map<String, Object> map : list) {
       if (map.get("ID") != null && !"".equals(map.get("ID").toString())) {
         ObjectUuidPK objectUuidPK = new ObjectUuidPK(map.get("ID").toString());
         try {
           PayRequestBillInfo info = ibiz.getPayRequestBillInfo((IObjectPK)objectUuidPK);
           if (info != null && 
             info.getBillStatus() != BillStatusEnum.AUDITED)
             ibiz.audit((IObjectPK)objectUuidPK); 
         } catch (EASBizException e) {
           e.printStackTrace();
         } 
       } 
     } 
     String sqlNo = "select id ,fnumber from eas_lolkk_fk where oafinishsign = 2 and eassign=1 and paystate = 2 and CONVERT(varchar(7), bizdate, 120) >= CONVERT(varchar(7), dateadd(month,-3,getdate()) , 120)  order by bizdate desc";
     List<Map<String, Object>> listNo = EAISynTemplate.query(ctx, database, sqlNo.toString());
     for (Map<String, Object> map : listNo) {
       if (map.get("ID") != null && !"".equals(map.get("ID").toString())) {
         ObjectUuidPK objectUuidPK = new ObjectUuidPK(map.get("ID").toString());
         try {
           PayRequestBillInfo info = ibiz.getPayRequestBillInfo((IObjectPK)objectUuidPK);
           if (info != null) {
             if (info.getBillStatus() == BillStatusEnum.SUBMITED) {
               ibiz.unfreeze((IObjectPK)objectUuidPK, (CoreBillBaseInfo)info);
               updateBillStatus(ctx, objectUuidPK.toString());
               continue;
             } 
             if (info.getBillStatus() == BillStatusEnum.AUDITED)
               if (!ExistsMapping(ctx, info.getId().toString())) {
                 ibiz.unpassAudit((IObjectPK)objectUuidPK, (CoreBillBaseInfo)info);
                 ibiz.unfreeze((IObjectPK)objectUuidPK, (CoreBillBaseInfo)info);
                 updateBillStatus(ctx, objectUuidPK.toString());
               }  
           } 
         } catch (EASBizException e) {
           e.printStackTrace();
         } 
       } 
     } 
   }
   
   private static boolean ExistsMapping(Context ctx, String fromid) {
     boolean isExists = false;
     String sql = "select count(1) as C from t_bot_relation  where FSrcEntityID = 'D001019A' and FDestEntityID = '40284E81' and FSrcObjectID ='" + fromid + "' ";
     try {
       IRowSet rs = DBUtil.executeQuery(ctx, sql);
       if (rs != null && rs.size() > 0)
         while (rs.next()) {
           if (rs.getObject("C") != null && !"".equals(rs.getObject("C").toString())) {
             if (Integer.parseInt(rs.getObject("C").toString()) == 0) {
               isExists = false;
               continue;
             } 
             isExists = true;
           } 
         }  
     } catch (BOSException e) {
       e.printStackTrace();
     } catch (SQLException e) {
       e.printStackTrace();
     } 
     return isExists;
   }
   
   private void updateBillStatus(Context ctx, String fid) {
     String sql = " update T_AP_PayRequestBill set FBillStatus = 2 where fid ='" + fid + "' and FBillStatus = 4";
     try {
       DBUtil.execute(ctx, sql);
       StringBuffer sbr = new StringBuffer("update t_ap_Otherbillentry set FLockUnVerifyAmt = FRecievePayAmount, FLockUnVerifyAmtLocal = FRecievePayAmountLocal  ,FLockVerifyAmt = 0, FLockVerifyAmtLocal= 0 ");
       sbr.append(" where  FPARENTID ='").append(fid).append("'");
       DBUtil.execute(ctx, sbr);
     } catch (BOSException e) {
       e.printStackTrace();
     } 
   }
   
   public void mobilePaymentBillBizDate(Context ctx, String ids, String date, String type) throws BOSException {
		if(type != null && "cas".equals(type)){
			if (VerifyUtil.notNull(ids)) {
				String sql = "/*dialect*/ update T_CAS_PaymentBill set cfoldbizdate = FBizDate WHERE FID in (" + ids + ") and cfoldbizdate is null";
				DBUtil.execute(ctx, sql);
				
				sql = "/*dialect*/ update T_CAS_PaymentBill set  FBillDate = to_date('"+ date + "','yyyy-mm-dd'),FBizDate = to_date('"+ date + "','yyyy-mm-dd') WHERE FID in (" + ids + ") ";
				DBUtil.execute(ctx, sql);
				
			}
		}else if(type != null && "other".equals(type)){
			if (VerifyUtil.notNull(ids)) {
				String sql = "/*dialect*/ update T_AP_OtherBill set FBillDate = to_date('"+ date + "','yyyy-mm-dd'),FBizDate = to_date('"+ date + "','yyyy-mm-dd') WHERE FID in (" + ids + ")";
				DBUtil.execute(ctx, sql);
			}
		}
   }
   
   private String apOtherNotShichang(Context ctx, String database, Map<String, Object> map) throws BOSException {
     String fid = null;
     try {
       String faccount = "2241.96";
       String actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
       Boolean addOrUpdate = Boolean.valueOf(false);
       fid = map.get("ID").toString();
       OtherBillInfo info = null;
       System.out.println("_--------------------------------------" + map.get("FNUMBER") + "====" + map.get("COMPANY") + "-----" + map.get("SUPPLIER"));
       if (map.get("COMPANY") == null || map.get("COMPANY").toString().equals("")) {
         System.out.println("_--------------------------------------" + map.get("FNUMBER"));
         updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
         AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.OA_OtherBill, info.getNumber(), info.getString("OA_OtherBill"), "单据保存失败," + info.getNumber() + "的公司编码为空");
         return "";
       } 
       if (map.get("FNUMBER") != null && !map.get("FNUMBER").toString().equals("")) {
         if (OtherBillFactory.getLocalInstance(ctx).exists(
             "where caigoushenqingdandanhao ='" + 
             map.get("FNUMBER") + "'")) {
           updateFSign(ctx, database, "eas_lolkk_bx", 1, map.get("ID").toString());
           return "";
         } 
         info = new OtherBillInfo();
       } else {
         info = new OtherBillInfo();
       } 
       info.setIsReversed(false);
       info.setIsReverseBill(false);
       info.setIsTransBill(false);
       info.setIsAllowanceBill(false);
       info.setIsImportBill(false);
       info.setIsExchanged(false);
       info.setIsInitializeBill(false);
       PaymentTypeInfo paymentTypeInfo = 
         PaymentTypeFactory.getLocalInstance(ctx)
         .getPaymentTypeInfo(
           (IObjectPK)new ObjectUuidPK(
             "2fa35444-5a23-43fb-99ee-6d4fa5f260da6BCA0AB5"));
       info.setPaymentType(paymentTypeInfo);
       info.setBillType(OtherBillType.OtherPay);
       CurrencyInfo currency = CurrencyFactory.getLocalInstance(ctx)
         .getCurrencyCollection("where number='BB01'").get(0);
       ObjectUuidPK orgPK = new ObjectUuidPK(
           map.get("COMPANY").toString());
       CompanyOrgUnitInfo xmcompany = 
         CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo((IObjectPK)orgPK);
       info.setCompany(xmcompany);
       System.out.println("------------------所属公司：" + 
           xmcompany.getId() + "----" + xmcompany.getName());
       AdminOrgUnitInfo admin = null;
       if (map.get("DEPT") != null && !"".equals(map.get("DEPT"))) {
         admin = 
           AdminOrgUnitFactory.getLocalInstance(ctx)
           .getAdminOrgUnitInfo(
             (IObjectPK)new ObjectUuidPK(map.get("DEPT").toString()));
         info.setAdminOrgUnit(admin);
         CostCenterOrgUnitInfo CostCenter = 
           CostCenterOrgUnitFactory.getLocalInstance(ctx)
           .getCostCenterOrgUnitInfo(
             (IObjectPK)new ObjectUuidPK(map.get("DEPT").toString()));
         info.setCostCenter(CostCenter);
       } 
       if (map.get("YHZH") != null && !"".equals(map.get("YHZH").toString()))
         info.put("yinhangzhanghao", map.get("YHZH")); 
       if (map.get("APPLYERBANKNUM") != null && !"".equals(map.get("APPLYERBANKNUM").toString()))
         info.setRecAccountBank(map.get("APPLYERBANKNUM").toString()); 
       if (map.get("KHH") != null && !"".equals(map.get("KHH").toString()))
         info.put("kaihuhang", map.get("KHH")); 
       if (map.get("APPLYERBANK") != null && !"".equals(map.get("APPLYERBANK").toString()))
         info.setRecBank(map.get("APPLYERBANK").toString()); 
       PurchaseOrgUnitInfo purchaseorginfo = PurchaseOrgUnitFactory.getLocalInstance(ctx).getPurchaseOrgUnitInfo((IObjectPK)orgPK);
       info.setPurOrg(purchaseorginfo);
       String personId = getPersonIdByNumber(ctx, map.get("APPLYER").toString());
       ObjectUuidPK objectUuidPK1 = new ObjectUuidPK(BOSUuid.read(personId));
       PersonInfo person = PersonFactory.getLocalInstance(ctx).getPersonInfo((IObjectPK)objectUuidPK1);
       info.setPerson(person);
       SettlementTypeInfo settlementTypeInfo = 
         SettlementTypeFactory.getLocalInstance(ctx)
         .getSettlementTypeInfo(
           (IObjectPK)new ObjectUuidPK(
             "e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
       info.setSettleType(settlementTypeInfo);
       SimpleDateFormat formmat = new SimpleDateFormat(
           "yyyy-MM-dd hh:mm:ss");
       Date bizDate = null;
       try {
         if (map.get("BIZDATE") != null && 
           !"".equals(map.get("BIZDATE").toString())) {
           bizDate = formmat.parse(map.get("BIZDATE").toString());
         } else {
           bizDate = new Date();
         } 
       } catch (ParseException e) {
         e.printStackTrace();
       } 
       info.setBizDate(bizDate);
       info.setBillDate(new Date());
       info.setCurrency(currency);
       info.setExchangeRate(new BigDecimal("1.00"));
       OtherBillType otherBillType = null;
       otherBillType = OtherBillType.OtherPay;
       info.setBillType(otherBillType);
       info.put("caigoushenqingdandanhao", map.get("FNUMBER"));
       info.put("OAcaigoushenqingdanjine", map.get("AMOUNT"));
       String jk = null;
       if (map.get("ISLOAN").toString().equals("0")) {
         jk = "否";
       } else if (map.get("ISLOAN").toString().equals("1")) {
         jk = "是";
       } 
       info.put("shifoujiekuan", jk);
       String zlf = null;
       if (map.get("ISRENTALFEE").toString().equals("0")) {
         zlf = "否";
       } else if (map.get("ISRENTALFEE").toString().equals("1")) {
         zlf = "是";
       } 
       info.put("shifouzulinfei", zlf);
       String djlx = null;
       String isAdminDept = "0";
       String[] deptArry = { "企划部", "渠道部", "网电部", "网络部", "新媒体部", "咨询部", "营销中心" };
       if (map.get("PURCHTYPE").toString().equals("01") || map.get("PURCHTYPE").toString().equals("04") || map.get("PURCHTYPE").toString().equals("09")) {
         djlx = "费用报销";
         actTypePk = "YW3xsAEJEADgAAWgwKgTB0c4VZA=";
         faccount = "2241.97";
         if (admin != null && admin.getName() != null)
           if (Arrays.<String>asList(deptArry).contains(admin.getName())) {
             isAdminDept = "2";
           } else {
             isAdminDept = "1";
           }  
       } else if (map.get("PURCHTYPE").toString().equals("02")) {
         djlx = "采购付款";
         actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
         faccount = "2241.96";
       } else if (map.get("PURCHTYPE").toString().equals("03")) {
         djlx = "市场投放";
         actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
         faccount = "2241.96";
       } else if (map.get("PURCHTYPE").toString().equals("04")) {
         djlx = "差旅费报销";
         actTypePk = "YW3xsAEJEADgAAWgwKgTB0c4VZA=";
         faccount = "2241.97";
         if (admin != null && admin.getName() != null)
           if (Arrays.<String>asList(deptArry).contains(admin.getName())) {
             isAdminDept = "2";
           } else {
             isAdminDept = "1";
           }  
       } else if (map.get("PURCHTYPE").toString().equals("05") || map.get("PURCHTYPE").toString().equals("10")) {
         djlx = "对外付款";
         actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
         faccount = "2241.96";
       } else if (map.get("PURCHTYPE").toString().equals("06")) {
         djlx = "合同专用付款";
         actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
         faccount = "2241.96";
       } else if (map.get("PURCHTYPE").toString().equals("07")) {
         djlx = "技加工";
         actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
         faccount = "2241.96";
       } 
       info.put("yingfudanjuleixing", djlx);
       info.put("shifouguanlibumen", isAdminDept);
       info.put("fapiaohao", "OA0000");
       String companytype = SupplyInfoLogUnit.getComapnyTypeByNumber(ctx, xmcompany.getNumber());
       if (companytype != null && !"".equals(companytype))
         info.put("CompanyType", companytype); 
       AccountViewInfo accountInfo = new AccountViewInfo();
       accountInfo = AccountViewFactory.getLocalInstance(ctx).getAccountViewInfo(
           "where number = '" + faccount + "' and companyID ='" + map.get("COMPANY").toString() + "' ");
       AsstActTypeInfo actType = AsstActTypeFactory.getLocalInstance(ctx).getAsstActTypeInfo((IObjectPK)new ObjectUuidPK(actTypePk));
       info.setAsstActType(actType);
       if (map.get("PURCHTYPE").toString().equals("01") || map.get("PURCHTYPE").toString().equals("04") || map.get("PURCHTYPE").toString().equals("09")) {
         info.setAsstActID(person.getId().toString());
         info.setAsstActName(person.getName());
         info.setAsstActNumber(person.getNumber());
       } else {
         try {
           SupplierInfo supplierInfo = 
             SupplierFactory.getLocalInstance(ctx).getSupplierInfo(
               " where number='" + map.get("SUPPLIERID") + "'");
           info.setAsstActID(supplierInfo.getId().toString());
           info.setAsstActName(supplierInfo.getName());
           info.setAsstActNumber(supplierInfo.getNumber());
         } catch (Exception e) {
           updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
           AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
               DateBasetype.OA_OtherBill, info.getNumber(), 
               info.getString("OA_OtherBill"), "没有该供应商编码");
           return "";
         } 
       } 
       VerificateBillTypeEnum billTypeEnum = VerificateBillTypeEnum.OtherPaymentBill;
       info.setSourceBillType(billTypeEnum);
       BizTypeInfo bizTypeInfo = BizTypeFactory.getLocalInstance(ctx)
         .getBizTypeInfo("where number = 110");
       info.setBizType(bizTypeInfo);
       String sql = "select parentID,id,payTypecode,payTypeName,Price,qty,amount,Yjk,Ytbk,remark from eas_lolkk_bx_sub where parentid =" + 
         map.get("ID");
       List<Map<String, Object>> list1 = EAISynTemplate.query(ctx, 
           database, sql);
       BigDecimal totalAmount = new BigDecimal(0);
       BigDecimal totalyjk = new BigDecimal(0);
       BigDecimal totalytbk = new BigDecimal(0);
       if (list1 != null && list1.size() > 0) {
         for (Map<String, Object> map1 : list1) {
           OtherBillentryInfo entryInfo = new OtherBillentryInfo();
           if (map1.get("PAYTYPECODE") != null && !"".equals(map1.get("PAYTYPECODE").toString())) {
             ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx).getExpenseTypeInfo("where number ='" + map1.get("PAYTYPECODE").toString() + "'");
             entryInfo.setExpenseItem(typeinfo);
           } else {
             ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx).getExpenseTypeInfo("where number ='CL01'");
             entryInfo.setExpenseItem(typeinfo);
           } 
           BigDecimal qty = new BigDecimal(map1.get("QTY").toString());
           BigDecimal price = new BigDecimal(map1.get("PRICE").toString());
           BigDecimal amount = new BigDecimal(map1.get("AMOUNT").toString());
           if (price.compareTo(BigDecimal.ZERO) < 0) {
             price = price.negate();
             qty = qty.negate();
           } 
           entryInfo.setPrice(price);
           entryInfo.setTaxPrice(price);
           entryInfo.setActualPrice(price);
           entryInfo.setRealPrice(price);
           entryInfo.setQuantity(qty);
           entryInfo.setBaseQty(BigDecimal.ZERO);
           entryInfo.setDiscountRate(BigDecimal.ZERO);
           entryInfo.setDiscountAmount(BigDecimal.ZERO);
           entryInfo.setDiscountAmountLocal(BigDecimal.ZERO);
           entryInfo.setHisUnVerifyAmount(BigDecimal.ZERO);
           entryInfo.setHisUnVerifyAmountLocal(BigDecimal.ZERO);
           entryInfo.setAssistQty(BigDecimal.ZERO);
           entryInfo.setWittenOffBaseQty(BigDecimal.ZERO);
           entryInfo.setLocalWrittenOffAmount(BigDecimal.ZERO);
           entryInfo.setUnwriteOffBaseQty(BigDecimal.ZERO);
           entryInfo.setVerifyQty(BigDecimal.ZERO);
           entryInfo.setLockVerifyQty(BigDecimal.ZERO);
           entryInfo.setLocalUnwriteOffAmount(amount);
           entryInfo.setAmount(amount);
           entryInfo.setAmountLocal(amount);
           entryInfo.setTaxAmount(BigDecimal.ZERO);
           entryInfo.setTaxAmountLocal(BigDecimal.ZERO);
           entryInfo.setTaxRate(BigDecimal.ZERO);
           entryInfo.setUnVerifyAmount(amount);
           entryInfo.setUnVerifyAmountLocal(amount);
           entryInfo.setLockUnVerifyAmt(amount);
           entryInfo.setLockUnVerifyAmtLocal(amount);
           entryInfo.setApportionAmount(BigDecimal.ZERO);
           entryInfo.setApportionAmtLocal(BigDecimal.ZERO);
           entryInfo.setUnApportionAmount(amount);
           entryInfo.setRecievePayAmount(amount);
           entryInfo.setRecievePayAmountLocal(amount);
           entryInfo.setCompany(map.get("COMPANY").toString());
           entryInfo.setAccount(accountInfo);
           if (map1.get("REMARK") != null)
             entryInfo.setRemark(map1.get("REMARK").toString()); 
           if (map1.get("YJK") != null && !"".equals(map1.get("YJK").toString()))
             totalyjk = totalyjk.add((BigDecimal)map1.get("YJK")); 
           if (map1.get("YTBK") != null && !"".equals(map1.get("YTBK").toString()))
             totalytbk = totalytbk.add((BigDecimal)map1.get("YTBK")); 
           entryInfo.setParent((ArApBillBaseInfo)info);
           entryInfo.setUnwriteOffBaseQty(qty);
           entryInfo.put("pinpai", map.get("BRAND"));
           entryInfo.put("huohao", map.get("ATRNO"));
           totalAmount = totalAmount.add(amount);
           info.getEntries().addObject((IObjectValue)entryInfo);
         } 
       } else {
         System.out.println("entrty is empty _--------------------------------------" + map.get("FNUMBER"));
         updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
         AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
             DateBasetype.OA_OtherBill, info.getNumber(), info.getString("OA_OtherBill"), "单据没有分录");
         return "";
       } 
       info.setAmount(totalAmount);
       info.setTotalTax(BigDecimal.ZERO);
       info.setTotalTaxAmount(totalAmount);
       info.setTotalAmount(totalAmount);
       info.setAmountLocal(totalAmount);
       info.setTotalAmountLocal(totalAmount);
       info.setTotalTaxLocal(BigDecimal.ZERO);
       info.setThisApAmount(totalAmount);
       info.setUnVerifyAmount(totalAmount);
       info.setUnVerifyAmountLocal(totalAmount);
       info.put("yingtuibukuan", totalytbk);
       info.put("yuanjiekuan", totalyjk);
       OtherBillPlanInfo otherBillPlanInfo = new OtherBillPlanInfo();
       otherBillPlanInfo.setLockAmount(totalAmount);
       otherBillPlanInfo.setLockAmountLoc(info.getAmountLocal());
       otherBillPlanInfo.setRecievePayAmount(totalAmount);
       otherBillPlanInfo.setRecievePayAmountLocal(info.getAmountLocal());
       info.getPayPlan().add(otherBillPlanInfo);
       OtherBillFactory.getLocalInstance(ctx).save((CoreBaseInfo)info);
       updateFSign(ctx, database, "eas_lolkk_bx", 1, map.get("ID").toString());
       try {
         System.out.println("------------------info所属公司1111：" + 
             info.getCompany().getId() + "----" + info.getCompany().getName());
         info.setBillStatus(BillStatusEnum.SAVE);
         OtherBillFactory.getLocalInstance(ctx).submit((CoreBaseInfo)info);
         System.out.println("------------------info所属公司2222：" + 
             info.getCompany().getId() + "----" + info.getCompany().getName());
         OtherBillFactory.getLocalInstance(ctx).audit((IObjectPK)new ObjectUuidPK(info.getId().toString()));
         if (addOrUpdate.booleanValue()) {
           AppUnit.insertLog(ctx, DateBaseProcessType.Update, 
               DateBasetype.OA_OtherBill, info.getNumber(), 
               info.getString("OA_OtherBill"), "单据修改成功");
         } else {
           AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
               DateBasetype.OA_OtherBill, info.getNumber(), 
               info.getString("OA_OtherBill"), "单据审核成功");
         } 
       } catch (Exception e2) {
         logger.error(e2.getMessage());
         AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
             DateBasetype.OA_OtherBill, info.getNumber(), 
             info.getString("OA_OtherBill"), "单据保存成功，提交审核失败。");
       } 
     } catch (EASBizException e) {
       e.printStackTrace();
       AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
           DateBasetype.OA_OtherBill, String.valueOf(fid) + "单据保存失败", e.getMessage());
       if (fid != null && !fid.equals(""))
         updateFSign(ctx, database, "eas_lolkk_bx", 2, fid); 
       String msg = "运行失败，异常是：" + e.toString();
       return msg;
     } 
     return null;
   }
   
   private String apOtherIsShichang(Context ctx, String database, Map<String, Object> map) throws BOSException {
     String fid = null;
     try {
       String faccount = "2241.96";
       String actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
       fid = map.get("ID").toString();
       OtherBillInfo info = new OtherBillInfo();
       String sql = "select parentID,fnumber,company,Dept,id,payTypecode,payTypeName,Price,qty,amount,Yjk,Ytbk,remark from eas_lolkk_bx_sub where parentid =" + 
         map.get("ID");
       List<Map<String, Object>> list1 = EAISynTemplate.query(ctx, database, sql);
       if (list1 != null && list1.size() > 0) {
         for (Map<String, Object> map1 : list1) {
           info = new OtherBillInfo();
           BigDecimal totalyjk = new BigDecimal(0);
           BigDecimal totalytbk = new BigDecimal(0);
           Boolean addOrUpdate = Boolean.valueOf(false);
           if (map1.get("COMPANY") == null || map1.get("COMPANY").toString().equals("")) {
             System.out.println("_--------------------------------------" + map.get("FNUMBER"));
             updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
             AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.OA_OtherBill, info.getNumber(), info.getString("OA_OtherBill"), "单据保存失败," + info.getNumber() + "的分录上的公司编码为空");
             return "";
           } 
           if (map1.get("DEPT") == null || map1.get("DEPT").toString().equals("")) {
             System.out.println("_--------------------------------------" + map.get("FNUMBER"));
             updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
             AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.OA_OtherBill, info.getNumber(), info.getString("OA_OtherBill"), "单据保存失败," + info.getNumber() + "的分录上的部门编码为空");
             return "";
           } 
           if (map1.get("FNUMBER") == null || map1.get("FNUMBER").toString().equals("")) {
             System.out.println("_--------------------------------------" + map.get("FNUMBER"));
             updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
             AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.OA_OtherBill, info.getNumber(), info.getString("OA_OtherBill"), "单据保存失败," + info.getNumber() + "的分录上的编码为空");
             return "";
           } 
           AccountViewInfo accountInfo = new AccountViewInfo();
           accountInfo = AccountViewFactory.getLocalInstance(ctx).getAccountViewInfo(
               "where number = '" + faccount + "' and companyID ='" + map1.get("COMPANY").toString() + "' ");
           OtherBillentryInfo entryInfo = new OtherBillentryInfo();
           if (map1.get("PAYTYPECODE") != null && !"".equals(map1.get("PAYTYPECODE").toString())) {
             ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx)
               .getExpenseTypeInfo("where number ='" + map1.get("PAYTYPECODE").toString() + "'");
             entryInfo.setExpenseItem(typeinfo);
           } else {
             ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx).getExpenseTypeInfo("where number ='CL01'");
             entryInfo.setExpenseItem(typeinfo);
           } 
           BigDecimal qty = new BigDecimal(map1.get("QTY").toString());
           BigDecimal price = new BigDecimal(map1.get("PRICE").toString());
           BigDecimal amount = new BigDecimal(map1.get("AMOUNT").toString());
           if (price.compareTo(BigDecimal.ZERO) < 0) {
             price = price.negate();
             qty = qty.negate();
           } 
           entryInfo.setPrice(price);
           entryInfo.setTaxPrice(price);
           entryInfo.setActualPrice(price);
           entryInfo.setRealPrice(price);
           entryInfo.setQuantity(qty);
           entryInfo.setBaseQty(BigDecimal.ZERO);
           entryInfo.setDiscountRate(BigDecimal.ZERO);
           entryInfo.setDiscountAmount(BigDecimal.ZERO);
           entryInfo.setDiscountAmountLocal(BigDecimal.ZERO);
           entryInfo.setHisUnVerifyAmount(BigDecimal.ZERO);
           entryInfo.setHisUnVerifyAmountLocal(BigDecimal.ZERO);
           entryInfo.setAssistQty(BigDecimal.ZERO);
           entryInfo.setWittenOffBaseQty(BigDecimal.ZERO);
           entryInfo.setLocalWrittenOffAmount(BigDecimal.ZERO);
           entryInfo.setUnwriteOffBaseQty(BigDecimal.ZERO);
           entryInfo.setVerifyQty(BigDecimal.ZERO);
           entryInfo.setLockVerifyQty(BigDecimal.ZERO);
           entryInfo.setLocalUnwriteOffAmount(amount);
           entryInfo.setAmount(amount);
           entryInfo.setAmountLocal(amount);
           entryInfo.setTaxAmount(BigDecimal.ZERO);
           entryInfo.setTaxAmountLocal(BigDecimal.ZERO);
           entryInfo.setTaxRate(BigDecimal.ZERO);
           entryInfo.setUnVerifyAmount(amount);
           entryInfo.setUnVerifyAmountLocal(amount);
           entryInfo.setLockUnVerifyAmt(amount);
           entryInfo.setLockUnVerifyAmtLocal(amount);
           entryInfo.setApportionAmount(BigDecimal.ZERO);
           entryInfo.setApportionAmtLocal(BigDecimal.ZERO);
           entryInfo.setUnApportionAmount(amount);
           entryInfo.setRecievePayAmount(amount);
           entryInfo.setRecievePayAmountLocal(amount);
           entryInfo.setCompany(map1.get("COMPANY").toString());
           entryInfo.setAccount(accountInfo);
           if (map1.get("YJK") != null && !"".equals(map1.get("YJK")))
             totalyjk = (BigDecimal)map1.get("YJK"); 
           if (map1.get("YTBK") != null && !"".equals(map1.get("YTBK")))
             totalytbk = (BigDecimal)map1.get("YTBK"); 
           entryInfo.setUnwriteOffBaseQty(qty);
           entryInfo.put("pinpai", map.get("BRAND"));
           entryInfo.put("huohao", map.get("ATRNO"));
           if (map1.get("REMARK") != null)
             entryInfo.setRemark(map1.get("REMARK").toString()); 
           if (map.get("FNUMBER") != null && !map.get("FNUMBER").toString().equals("")) {
             if (OtherBillFactory.getLocalInstance(ctx).exists("where caigoushenqingdandanhao ='" + map.get("FNUMBER") + "_" + map1.get("FNUMBER") + "'"))
               continue; 
             info = new OtherBillInfo();
           } else {
             info = new OtherBillInfo();
           } 
           entryInfo.setParent((ArApBillBaseInfo)info);
           info.getEntries().addObject((IObjectValue)entryInfo);
           info.setIsReversed(false);
           info.setIsReverseBill(false);
           info.setIsTransBill(false);
           info.setIsAllowanceBill(false);
           info.setIsImportBill(false);
           info.setIsExchanged(false);
           info.setIsInitializeBill(false);
           PaymentTypeInfo paymentTypeInfo = PaymentTypeFactory.getLocalInstance(ctx)
             .getPaymentTypeInfo((IObjectPK)new ObjectUuidPK("2fa35444-5a23-43fb-99ee-6d4fa5f260da6BCA0AB5"));
           info.setPaymentType(paymentTypeInfo);
           info.setBillType(OtherBillType.OtherPay);
           ObjectUuidPK orgPK = new ObjectUuidPK(map1.get("COMPANY").toString());
           CompanyOrgUnitInfo company = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo((IObjectPK)orgPK);
           info.setCompany(company);
           System.out.println("------------------所属公司：" + company.getId() + "----" + company.getName());
           AdminOrgUnitInfo admin = null;
           if (map1.get("DEPT") != null && !"".equals(map1.get("DEPT"))) {
             admin = AdminOrgUnitFactory.getLocalInstance(ctx)
               .getAdminOrgUnitInfo((IObjectPK)new ObjectUuidPK(map1.get("DEPT").toString()));
             info.setAdminOrgUnit(admin);
             CostCenterOrgUnitInfo CostCenter = CostCenterOrgUnitFactory.getLocalInstance(ctx)
               .getCostCenterOrgUnitInfo((IObjectPK)new ObjectUuidPK(map1.get("DEPT").toString()));
             info.setCostCenter(CostCenter);
           } 
           if (map.get("YHZH") != null && !"".equals(map.get("YHZH").toString()))
             info.put("yinhangzhanghao", map.get("YHZH")); 
           if (map.get("APPLYERBANKNUM") != null && !"".equals(map.get("APPLYERBANKNUM").toString()))
             info.setRecAccountBank(map.get("APPLYERBANKNUM").toString()); 
           if (map.get("KHH") != null && !"".equals(map.get("KHH").toString()))
             info.put("kaihuhang", map.get("KHH")); 
           if (map.get("APPLYERBANK") != null && !"".equals(map.get("APPLYERBANK").toString()))
             info.setRecBank(map.get("APPLYERBANK").toString()); 
           PurchaseOrgUnitInfo purchaseorginfo = PurchaseOrgUnitFactory.getLocalInstance(ctx).getPurchaseOrgUnitInfo((IObjectPK)orgPK);
           info.setPurOrg(purchaseorginfo);
           String personId = getPersonIdByNumber(ctx, map.get("APPLYER").toString());
           ObjectUuidPK objectUuidPK1 = new ObjectUuidPK(BOSUuid.read(personId));
           PersonInfo person = PersonFactory.getLocalInstance(ctx).getPersonInfo((IObjectPK)objectUuidPK1);
           info.setPerson(person);
           SettlementTypeInfo settlementTypeInfo = SettlementTypeFactory.getLocalInstance(ctx)
             .getSettlementTypeInfo((IObjectPK)new ObjectUuidPK("e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
           info.setSettleType(settlementTypeInfo);
           SimpleDateFormat formmat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
           Date bizDate = null;
           try {
             if (map.get("BIZDATE") != null && !"".equals(map.get("BIZDATE").toString())) {
               bizDate = formmat.parse(map.get("BIZDATE").toString());
             } else {
               bizDate = new Date();
             } 
           } catch (ParseException e) {
             e.printStackTrace();
           } 
           info.setBizDate(bizDate);
           info.setBillDate(new Date());
           CurrencyInfo currency = CurrencyFactory.getLocalInstance(ctx).getCurrencyCollection("where number='BB01'").get(0);
           info.setCurrency(currency);
           info.setExchangeRate(new BigDecimal("1.00"));
           OtherBillType otherBillType = null;
           otherBillType = OtherBillType.OtherPay;
           info.setBillType(otherBillType);
           info.put("caigoushenqingdandanhao", (new StringBuilder()).append(map.get("FNUMBER")).append("_").append(map1.get("FNUMBER")).toString());
           info.put("OAcaigoushenqingdanjine", map1.get("AMOUNT"));
           String companType = SupplyInfoLogUnit.getComapnyTypeByNumber(ctx, company.getNumber());
           if (companType != null && !"".equals(companType))
             info.put("CompanyType", companType); 
           String jk = null;
           if (map.get("ISLOAN").toString().equals("0")) {
             jk = "否";
           } else if (map.get("ISLOAN").toString().equals("1")) {
             jk = "是";
           } 
           info.put("shifoujiekuan", jk);
           String zlf = null;
           if (map.get("ISRENTALFEE").toString().equals("0")) {
             zlf = "否";
           } else if (map.get("ISRENTALFEE").toString().equals("1")) {
             zlf = "是";
           } 
           info.put("shifouzulinfei", zlf);
           String djlx = null;
           String isAdminDept = "0";
           String[] deptArry = { "企划部", "渠道部", "网电部", "网络部", "新媒体部", "咨询部", "营销中心" };
           if (map.get("PURCHTYPE").toString().equals("01")) {
             djlx = "费用报销";
             actTypePk = "YW3xsAEJEADgAAWgwKgTB0c4VZA=";
             faccount = "2241.97";
             if (admin != null && admin.getName() != null)
               if (Arrays.<String>asList(deptArry).contains(admin.getName())) {
                 isAdminDept = "2";
               } else {
                 isAdminDept = "1";
               }  
           } else if (map.get("PURCHTYPE").toString().equals("02")) {
             djlx = "采购付款";
             actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
             faccount = "2241.96";
           } else if (map.get("PURCHTYPE").toString().equals("03")) {
             djlx = "市场投放";
             actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
             faccount = "2241.96";
           } else if (map.get("PURCHTYPE").toString().equals("04")) {
             djlx = "差旅费报销";
             actTypePk = "YW3xsAEJEADgAAWgwKgTB0c4VZA=";
             faccount = "2241.97";
             if (admin != null && admin.getName() != null)
               if (Arrays.<String>asList(deptArry).contains(admin.getName())) {
                 isAdminDept = "2";
               } else {
                 isAdminDept = "1";
               }  
           } else if (map.get("PURCHTYPE").toString().equals("05")) {
             djlx = "对外付款";
             actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
             faccount = "2241.96";
           } else if (map.get("PURCHTYPE").toString().equals("06")) {
             djlx = "合同专用付款";
             actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
             faccount = "2241.96";
           } else if (map.get("PURCHTYPE").toString().equals("07")) {
             djlx = "技加工";
             actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
             faccount = "2241.96";
           } 
           info.put("yingfudanjuleixing", djlx);
           info.put("shifouguanlibumen", isAdminDept);
           info.put("fapiaohao", "OA0000");
           AsstActTypeInfo actType = AsstActTypeFactory.getLocalInstance(ctx).getAsstActTypeInfo((IObjectPK)new ObjectUuidPK(actTypePk));
           info.setAsstActType(actType);
           if (map.get("PURCHTYPE").toString().equals("01") || map.get("PURCHTYPE").toString().equals("04")) {
             info.setAsstActID(person.getId().toString());
             info.setAsstActName(person.getName());
             info.setAsstActNumber(person.getNumber());
           } else {
             try {
               SupplierInfo supplierInfo = SupplierFactory.getLocalInstance(ctx).getSupplierInfo(" where number='" + map.get("SUPPLIERID") + "'");
               info.setAsstActID(supplierInfo.getId().toString());
               info.setAsstActName(supplierInfo.getName());
               info.setAsstActNumber(supplierInfo.getNumber());
             } catch (Exception e) {
               updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
               AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.OA_OtherBill, info.getNumber(), 
                   info.getString("OA_OtherBill"), "分录上的" + map.get("NUMBER") + "没有该供应商编码");
               continue;
             } 
           } 
           VerificateBillTypeEnum billTypeEnum = VerificateBillTypeEnum.OtherPaymentBill;
           info.setSourceBillType(billTypeEnum);
           BizTypeInfo bizTypeInfo = BizTypeFactory.getLocalInstance(ctx).getBizTypeInfo("where number = 110");
           info.setBizType(bizTypeInfo);
           info.setAmount(amount);
           info.setTotalTax(BigDecimal.ZERO);
           info.setTotalTaxAmount(amount);
           info.setTotalAmount(amount);
           info.setAmountLocal(amount);
           info.setTotalAmountLocal(amount);
           info.setTotalTaxLocal(BigDecimal.ZERO);
           info.setThisApAmount(amount);
           info.setUnVerifyAmount(amount);
           info.setUnVerifyAmountLocal(amount);
           info.put("yingtuibukuan", totalytbk);
           info.put("yuanjiekuan", totalyjk);
           OtherBillPlanInfo otherBillPlanInfo = new OtherBillPlanInfo();
           otherBillPlanInfo.setLockAmount(amount);
           otherBillPlanInfo.setLockAmountLoc(info.getAmountLocal());
           otherBillPlanInfo.setRecievePayAmount(amount);
           otherBillPlanInfo.setRecievePayAmountLocal(info.getAmountLocal());
           info.getPayPlan().add(otherBillPlanInfo);
           OtherBillFactory.getLocalInstance(ctx).save((CoreBaseInfo)info);
           try {
             OtherBillFactory.getLocalInstance(ctx).submit((CoreBaseInfo)info);
             OtherBillFactory.getLocalInstance(ctx).audit((IObjectPK)new ObjectUuidPK(info.getId().toString()));
             AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
                 DateBasetype.OA_OtherBill, info.getNumber(), 
                 info.getString("OA_OtherBill"), "采购申请单单号为" + map.get("FNUMBER") + "_" + map1.get("FNUMBER") + "单据审核成功");
           } catch (Exception e2) {
             logger.error(e2.getMessage());
             AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
                 DateBasetype.OA_OtherBill, info.getNumber(), 
                 info.getString("OA_OtherBill"), "采购申请单单号为" + map.get("FNUMBER") + "_" + map1.get("FNUMBER") + "单据保存成功,提交审核失败。");
           } 
         } 
       } else {
         System.out.println("entrty is empty _--------------------------------------" + map.get("FNUMBER"));
         updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
         AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, DateBasetype.OA_OtherBill, 
             map.get("FNUMBER").toString(), info.getString("OA_OtherBill"), "单据没有分录");
         return "";
       } 
       updateFSign(ctx, database, "eas_lolkk_bx", 1, map.get("ID").toString());
     } catch (EASBizException e) {
       e.printStackTrace();
       AppUnit.insertLog(ctx, DateBaseProcessType.AddNew, 
           DateBasetype.OA_OtherBill, String.valueOf(fid) + "单据保存失败", e.getMessage());
       if (fid != null && !fid.equals(""))
         updateFSign(ctx, database, "eas_lolkk_bx", 2, fid); 
       String msg = "运行失败，异常是：" + e.toString();
       return msg;
     } 
     return null;
   }
   
   public void updateNoPeople(Context ctx, String database) throws BOSException {
     String updateSql = "UPDATE  eas_lolkk_bx  set eassign = 2 , EASTIME = TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS'),EASLOG='职员不存在'  where ID in (select bx.id from eas_lolkk_bx bx left JOIN EAS_PERSON_MIDTABLE  person on person.FNUMBER = bx.APPLYER where bx.eassign = 0 and person.FNUMBER is null )";
     System.out.print("--------------" + updateSql);
     EAISynTemplate.execute(ctx, database, updateSql);
     updateSql = "update EAS_LOLKK_bx set EASSIGN = -1, EASTIME = TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS'),EASLOG='PAYTYPECODE为空' where id in (select DISTINCT PARENTID from EAS_LOLKK_BX_SUB where PAYTYPECODE is null ) and EASSIGN = 0";
     System.out.print("--------------" + updateSql);
     EAISynTemplate.execute(ctx, database, updateSql);
     updateSql = "update EAS_LOLKK_bx set EASSIGN = -2, EASTIME = TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS'),EASLOG='PAYTYPECODE不存在' where id in (select DISTINCT PARENTID from EAS_LOLKK_BX_SUB where PAYTYPECODE not in (select FNUMBER from EAS_PAYTYPE_OA_MIDTABLE)) and EASSIGN = 0";
     System.out.print("--------------" + updateSql);
     EAISynTemplate.execute(ctx, database, updateSql);
   }
   
   public String updateMidPayStatus(Context ctx) throws BOSException {
     String noPayBillSql = "select a.id,a.oanumber, a.fbillid ,a.danjuType FROM eas_paymentbillstatus  a where a.status = 0 ";
     IRowSet noPayBill = DbUtil.executeQuery(ctx, noPayBillSql);
     String oanumber = null;
     try {
       while (noPayBill.next()) {
         oanumber = noPayBill.getString("OANUMBER");
         String fbillid = noPayBill.getString("FBILLID");
         String danjuType = noPayBill.getString("DANJUTYPE");
         String id = noPayBill.getString("id");
         String number = oanumber;
         if (VerifyUtil.notNull(oanumber)) {
           if (oanumber != null && !"".equals(oanumber) && !"-1".equals(oanumber)) {
             String sql = null;
             if (danjuType.equals("市场投放")) {
               if (oanumber.indexOf("_") != -1 && (oanumber.split("_")).length > 1) {
                 oanumber = oanumber.split("_")[1];
                 sql = "update eas_lolkk_bx_sub set Paystate = 1,Paystatetime =TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') where fnumber ='" + oanumber + "'";
               } else {
                 sql = "update eas_lolkk_bx set Paystate = 1,Paystatetime =TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') where fnumber ='" + oanumber + "'";
               } 
             } else {
               sql = "update eas_lolkk_bx set Paystate = 1,Paystatetime =TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') where fnumber ='" + oanumber + "'";
             } 
             EAISynTemplate.execute(ctx, "04", sql);
           } 
           if (fbillid != null && !"".equals(fbillid) && !"-1".equals(fbillid)) {
             String sql3 = "update eas_lolkk_fk set Paystate = 1 where id ='" + fbillid + "'";
             EAISynTemplate.execute(ctx, "03", sql3);
           } 
           String updateStatusSql = " update eas_paymentbillstatus set  STATUS =  1  where id=" + id;
           DbUtil.execute(ctx, updateStatusSql);
           AppUnit.insertLog(ctx, DateBaseProcessType.Update, DateBasetype.OA_OtherBill, oanumber, oanumber, "修改付款状态成功");
         } 
       } 
     } catch (BOSException e) {
       AppUnit.insertLog(ctx, DateBaseProcessType.Update, DateBasetype.OA_OtherBill, oanumber, oanumber, "修改付款状态异常");
       e.printStackTrace();
     } catch (SQLException e) {
       e.printStackTrace();
     } 
     return null;
   }
   
   protected boolean _IsExistDownstreamBill(Context ctx, String id) throws BOSException {
     boolean flag = true;
     try {
       flag = AppUnit.isExistDownstreamBill(ctx, id);
     } catch (EASBizException e) {
       e.printStackTrace();
       flag = true;
     } 
     return flag;
   }
   
   protected void _PurvspJDFromOA(Context ctx, String database) throws BOSException {
     VSPJDSupport.savePurRequest(ctx, database);
   }
   
   protected void _ReceConfirmVSPJD(Context ctx, String database) throws BOSException {
     VSPJDSupport.savePurReceivalBill(ctx, database);
   }
   
   private static Timestamp string2Time(String dateString) throws ParseException {
     DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
     dateFormat.setLenient(false);
     Date timeDate = dateFormat.parse(dateString);
     Timestamp dateTime = new Timestamp(timeDate.getTime());
     return dateTime;
   }
   
   private static Date string2Date(String dateString) throws Exception {
     DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
     dateFormat.setLenient(false);
     Date timeDate = dateFormat.parse(dateString);
     Date dateTime = new Date(timeDate.getTime());
     return dateTime;
   }
   
   private static String getPersonIdByNumber(Context ctx, String number) {
     String pid = "";
     if (number != null && !"".equals(number)) {
       String sql = "select FID from t_bd_person where fnumber ='" + number + "'";
       try {
         IRowSet rs = DBUtil.executeQuery(ctx, sql);
         if (rs != null && rs.size() > 0)
           while (rs.next()) {
             if (rs.getObject("FID") != null && !"".equals(rs.getObject("FID").toString()))
               pid = rs.getObject("FID").toString(); 
           }  
       } catch (BOSException e) {
         e.printStackTrace();
       } catch (SQLException e) {
         e.printStackTrace();
       } 
     } 
     return pid;
   }
   
   private static String materialUnitId(Context ctx, String orgId, String materialId) {
     String unitId = "";
     if (orgId != null && !"".equals(orgId) && materialId != null && !"".equals(materialId)) {
       String sql = "select FUnitID from T_BD_MaterialPurchasing where FMaterialID ='" + materialId + "' and FOrgUnit ='" + orgId + "'";
       try {
         IRowSet rs = DBUtil.executeQuery(ctx, sql);
         if (rs != null && rs.size() > 0)
           while (rs.next()) {
             if (rs.getObject("FUnitID") != null && !"".equals(rs.getObject("FUnitID").toString()))
               unitId = rs.getObject("FUnitID").toString(); 
           }  
       } catch (BOSException e) {
         e.printStackTrace();
       } catch (SQLException e) {
         e.printStackTrace();
       } 
     } 
     return unitId;
   }
   
   private static BigDecimal getmaterialMultiple(Context ctx, String materialId, String unitId) {
     BigDecimal multiple = new BigDecimal(1);
     if (materialId != null && !"".equals(materialId) && unitId != null && !"".equals(unitId)) {
       boolean flag = false;
       String sql = "select FBaseUnit from T_BD_Material where FID = '" + materialId + "'";
       try {
         IRowSet rs = DBUtil.executeQuery(ctx, sql);
         if (rs != null && rs.size() > 0)
           while (rs.next()) {
             if (rs.getObject("FBaseUnit") != null && !"".equals(rs.getObject("FBaseUnit").toString()) && 
               !unitId.equals(rs.getObject("FBaseUnit").toString()))
               flag = true; 
           }  
       } catch (BOSException e) {
         e.printStackTrace();
       } catch (SQLException e) {
         e.printStackTrace();
       } 
       if (flag) {
         sql = "select FBaseConvsRate from t_bd_multimeasureunit where fmaterialid ='" + materialId + "'and FMeasureUnitID='" + unitId + "'";
         try {
           IRowSet rs = DBUtil.executeQuery(ctx, sql);
           if (rs != null && rs.size() > 0)
             while (rs.next()) {
               if (rs.getObject("FBaseConvsRate") != null && !"".equals(rs.getObject("FBaseConvsRate").toString()))
                 multiple = new BigDecimal(rs.getObject("FBaseConvsRate").toString()); 
             }  
         } catch (BOSException e) {
           e.printStackTrace();
         } catch (SQLException e) {
           e.printStackTrace();
         } 
       } 
     } 
     return multiple;
   }
 }