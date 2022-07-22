package com.kingdee.eas.custom.app;

import java.math.BigDecimal;
import java.sql.SQLException;
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

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
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
import com.kingdee.eas.custom.util.DBUtil;
import com.kingdee.eas.custom.util.VerifyUtil;
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
import com.kingdee.eas.fi.ar.BillStatusEnum;
import com.kingdee.eas.fi.cas.CasRecPayBillTypeEnum;
import com.kingdee.eas.fi.cas.PaymentBillEntryInfo;
import com.kingdee.eas.fi.cas.PaymentBillFactory;
import com.kingdee.eas.fi.cas.PaymentBillInfo;
import com.kingdee.eas.fi.cas.PaymentBillTypeFactory;
import com.kingdee.eas.fi.cas.PaymentBillTypeInfo;
import com.kingdee.eas.fi.cas.RecPayBillTypeEnum;
import com.kingdee.eas.scm.common.PurchaseTypeEnum;
import com.kingdee.eas.scm.sm.pur.PurRequestEntryInfo;
import com.kingdee.eas.scm.sm.pur.PurRequestFactory;
import com.kingdee.eas.scm.sm.pur.PurRequestInfo;
import com.kingdee.eas.util.app.DbUtil;
import com.kingdee.jdbc.rowset.IRowSet;

public class BusinessFormOAControllerBean extends
		AbstractBusinessFormOAControllerBean {
	private static Logger logger = Logger
			.getLogger("com.kingdee.eas.custom.app.BusinessFormOAControllerBean");

	/*
	 * 应付单 (其他应付单) 1
	 * 
	 * @seecom.kingdee.eas.custom.app.AbstractBusinessFormOAControllerBean#
	 * _ApOtherFormOA(com.kingdee.bos.Context, java.lang.String)
	 */
	@Override
	protected String _ApOtherFormOA(Context ctx, String database)
			throws BOSException {
		String sql = null;
		/*
		 * sql =
		 * "select id,fnumber,bizDate,isLoan,payType,isrentalfee,company,Dept,supplierid,Yhzh,Khh,applyer,"
		 * + "Applyerbank,Applyerbanknum,Agency,Amount,Jsfs,purchType,purchModel,Paystate,Paystatetime "
		 * + "from eas_lolkk_bx where eassign = 0 ";
		 */
		
		//将eas_lolkk_bx表中申请人不存在的数据修改为状态2
		updateNoPeople(ctx,database);
		//将eas_lolkk_bxd的分录表中费用类型不存在的数据修改为状态2
		updateNoExpenseType(ctx,database);
				sql = " select bx.id,bx.fnumber,bx.bizDate,bx.isLoan,bx.payType,bx.isrentalfee,bx.company,bx.Dept,bx.supplierid,bx.Yhzh,bx.Khh,bx.applyer, "
				+ " bx.Applyerbank,bx.Applyerbanknum,bx.Agency,bx.Amount,bx.Jsfs,bx.purchType,bx.purchModel,bx.Paystate,bx.Paystatetime "
				+ " from eas_lolkk_bx bx "+ " where bx.eassign = 0 and bx.PURCHTYPE != '08' and ( ispre is null  or ispre = 0 )";

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String, Object>> list = EAISynTemplate.query(ctx, database,sql.toString());
		System.out.println("--------------------------" + list.size());
		for (Map<String, Object> map : list) {
			// 判断单据类型是否为03  进行不同的处理
			if(map.get("PURCHTYPE")!=null && !map.get("PURCHTYPE").toString().equals("03")){
				apOtherNotShichang( ctx,  database, map);
			}else if(map.get("PURCHTYPE")!=null && map.get("PURCHTYPE").toString().equals("03")){
				apOtherIsShichang( ctx,  database, map);
			}

		}
		
		return super._ApOtherFormOA(ctx, database);
	}

	private void updateNoExpenseType(Context ctx, String database) {
		String updateSql = "UPDATE  eas_lolkk_bx  set eassign = 2 , EASTIME = TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS'),EASLOG='分录上某个费用类型禁用' "
			+" where ID in (select bx.id from eas_lolkk_bx bx left JOIN eas_lolkk_bx_sub sub  on sub.parentid = bx.id "+
			" left JOIN EAS_PAYTYPE_OA_MIDTABLE paytype  on paytype.fnumber = sub.paytypecode where bx.eassign = 0 and paytype.fstatus = 2 )";
		System.out.print("--------------" + updateSql);
		try {
			EAISynTemplate.execute(ctx, database, updateSql);
		} catch (BOSException e) {
 			e.printStackTrace();
		}
	}

	/*
	 * 付款申请单
	 */
	@Override
	// 付款申请单单传入中间表bycb
	protected String _PayApplyToOA(Context ctx, String database, String billId)
			throws BOSException {
		// TODO Auto-generated method stub
		// 付款单审核传递数据到oa中间表
		String result = "";
		if (!isExistsByBillId(ctx, database, billId)) {
			try {
				PayRequestBillInfo payRequestBillInfo = PayRequestBillFactory
						.getLocalInstance(ctx).getPayRequestBillInfo(
								new ObjectUuidPK(billId));
				PayRequestBillEntryCollection entryCollection = payRequestBillInfo
						.getEntrys();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String id = payRequestBillInfo.getId().toString();// id

				String fnumber = payRequestBillInfo.getNumber();// 单据编号
				// String oaNumber =
				// payRequestBillInfo.getString("oacaigoushenqingdandanhao"
				// );//oa采购申请单单号
				// String oaMoney =
				// payRequestBillInfo.getString("caigoushenqingdanjine"
				// );//oa采购申请单单号
				String bizDate = sdf.format(payRequestBillInfo.getBizDate());// 业务日期

				String Formtype = "6";
				// String Formtype = pbInfo.getFormtype();//采购类型
				// ?String Suppliernum = pbInfo.getPayeeNumber();//供应商编码
				// ?String Suppliername = pbInfo.getPayeeName();//供应商名称
				// ?String Supplierbank = pbInfo.getPayeeBank();//开户行
				// ?String Supplierbanknum =
				// pbInfo.getPayeeAccountBank();//银行收款账号
				String Suppliernum = "";
				String Suppliername = "";
				String Supplierbank = "";
				String Supplierbanknum = "";
 				String prex ="";
 			 
				MaterialInfo materialInfo = null;
				if (entryCollection.size() > 0) {
					PayRequestBillEntryInfo entryOne = entryCollection.get(0);
					materialInfo = entryOne.getMaterialNo(); 
					if(materialInfo!=null && materialInfo.getId()!=null &&  !"".equals(materialInfo.getId().toString())){
						prex = AppUnit.getMaterialNumberPreByFID(ctx, materialInfo.getId().toString()); 
					}
//					if(entryOne.get("oacaigoushenqingdandanhao")!=null && !"".equals(entryOne.get("oacaigoushenqingdandanhao").toString())){
//							Formtype =AppUnit.getModelFromOANum(ctx,entryOne.get("oacaigoushenqingdandanhao").toString());	
//					}else{
//						//如果物料为技加工的物料 Formtype =6    VMI Formtype = 8
//						if(prex!=null && !"".equals(prex)){
//							if(!"JGF".equals(prex)) Formtype = "8";
//						}else{
//							Formtype = "6";
//						}
//					}
					
					
					Suppliernum = entryOne.getAsstActNumber();// 供应商编码
					Suppliername = entryOne.getAsstActName();// 供应商名称
					String querysql = "select fnumber,fopenbank,fbankaccount from eas_supplier_midtable where fnumber = '"
							+ Suppliernum + "'";
					List<Map<String, Object>> banklist = EAISynTemplate.query(
							ctx, database, querysql);
					if (banklist != null && banklist.size() > 0) {
						if (banklist.get(0) != null) {
							Map bankMap = banklist.get(0);
							Supplierbank = bankMap.get("FOPENBANK") == null ? "\\"
									: bankMap.get("FOPENBANK").toString();
							Supplierbanknum = bankMap.get("FBANKACCOUNT") == null ? "\\"
									: bankMap.get("FBANKACCOUNT").toString();

						}
					} 
				}

				String Usedate = "";
				// String Usedate = pbInfo.getUsedate();//用款日期
				UserInfo uInfo = UserFactory.getLocalInstance(ctx).getUserInfo(
						new ObjectUuidPK(payRequestBillInfo.getCreator()
								.getId()));

				CompanyOrgUnitInfo couInfo = CompanyOrgUnitFactory
						.getLocalInstance(ctx).getCompanyOrgUnitInfo(
								new ObjectUuidPK(payRequestBillInfo
										.getCompany().getId()));
				String Companynum = couInfo.getId().toString();// 财务组织id
				String Company = couInfo.getName();// 财务组织名称
				String Gzamount = payRequestBillInfo.getRequestAmount()
						.toString();// 预算金额合计 申请金额
				String requestAmount = payRequestBillInfo.getAuditAmount().toString();// 应付金额 审批金额
 				String invoiceNumber = payRequestBillInfo.get("fapiaohao") !=null ? payRequestBillInfo.get("fapiaohao").toString():"";
				String purpose = payRequestBillInfo.getDescription() != null ? payRequestBillInfo.getDescription(): "";// 摘要
				// 结算方式，02电汇
				SettlementTypeInfo stInfo = SettlementTypeFactory
						.getLocalInstance(ctx)
						.getSettlementTypeInfo(
								new ObjectUuidPK(
										"e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
				String Jsfs = stInfo.getNumber();
				String Eassign = "0";// 同步标识
				String Eastime = sdf.format(new Date());// 同步时间
				String oasign = "0";// OA记录同步日志的标识
				String Oatime = sdf.format(new Date());// Oa记录时间
				String applyer = null;
				String personid = "";
				String requestReason = payRequestBillInfo.getRequestReason() != null ? payRequestBillInfo.getRequestReason(): "";//请求原因
//				if(payRequestBillInfo.getApplyer()!=null && payRequestBillInfo.getApplyer().getId()!=null && !"".equals(payRequestBillInfo.getApplyer().getId().toString())){
//					personid = payRequestBillInfo.getApplyer().getId().toString();
//				}else{
//					personid="6tB2iCtgRFGVooJDiONYlYDvfe0=";
//				}
//				PersonInfo person = PersonFactory.getLocalInstance(ctx).getPersonInfo(new ObjectUuidPK(personid));
//		        applyer = person.getNumber();// 申请人编码
		        
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
				
//				if (payRequestBillInfo.getString("oacaigoushenqingdandanhao") == null || "".equals(payRequestBillInfo.getString("oacaigoushenqingdandanhao"))) {
//					Formtype = "6";
//					//如果物料为技加工的物料 Formtype =6    VMI Formtype = 8
//					if(prex!=null && !"".equals(prex)){
//						if(!"JGF".equals(prex)) Formtype = "8";
//					}else{
//						Formtype = "6";
//					} 
//					  person = PersonFactory.getLocalInstance(ctx)
//							.getPersonInfo(
//									new ObjectUuidPK(payRequestBillInfo
//											.getApplyer().getId()));
//					applyer = person.getNumber();// 申请人编码
//				} else {
//					Map map1 = AppUnit.getPurchModelFromOANum(ctx,payRequestBillInfo.getString("oacaigoushenqingdandanhao"));
//					String p1 = "";
//					if (map1.get("person") != null && !"".equals(map1.get("person"))) {
//						p1 = (String) map1.get("person");
//					} else {
//						//p1 = "6tB2iCtgRFGVooJDiONYlYDvfe0=";
//						p1 = payRequestBillInfo.getApplyer()==null ?"6tB2iCtgRFGVooJDiONYlYDvfe0=":payRequestBillInfo.getApplyer().getId().toString();
//					}
//					// map1.get("person")==null?"6tB2iCtgRFGVooJDiONYlYDvfe0=":(
//					// String)map1.get("person");
//					Formtype = map1.get("model")==null?"":(String) map1.get("model");
//					person = PersonFactory.getLocalInstance(ctx).getPersonInfo(new ObjectUuidPK(p1));
//					applyer = person.getNumber();// 申请人编码
//				}
				// 分录
				List<Map<String, String>> entrys = new ArrayList<Map<String, String>>();
				List<Map<String, String>> oanumbers = AppUnit.getSumPayRequestBillEntrys(ctx, id);
				String type = "";
				if (oanumbers != null && oanumbers.size() > 0) {
 					for (Map<String, String> mp : oanumbers) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("parentID", id);// 父id
						map.put("fnumber",  mp.get("oanumber").toString());// 采购申请单编号
						map.put("Companynum", couInfo.getId().toString());// eas财务组织id
						map.put("Suppliername", couInfo.getName());// eas财务组织名称
						map.put("requestAmount", mp.get("amount").toString());// 分录金额
						type = AppUnit.getPurTypeByOANumber(mp.get("oanumber").toString(),prex); 
						map.put("formtypezhi",type);// 采购类型
						Formtype = type;
						entrys.add(map);
					}
				}  
				String sql = "insert into eas_lolkk_fk (id,fnumber,bizDate,Formtype,InvoiceNumber,Suppliernum,Suppliername,Supplierbank,Supplierbanknum,Usedate,applyer,Companynum,Company,Gzamount,requestAmount,purpose,Jsfs,requestReason,Eassign,Eastime,oasign,Oatime,oafinishsign,paystate) values("
						+ "'"+ id+ "','"+ fnumber+ "','"+ bizDate + "','"+ Formtype+ "','"+ invoiceNumber+ "','"
						+ Suppliernum+ "','"+ Suppliername+ "','"+ Supplierbank+ "','"+ Supplierbanknum+ "','"
					    + Usedate + "','" + applyer + "','"+ Companynum+ "','"+ Company+ "','"
						+ Gzamount+ "',"+ requestAmount+ ",'"+ purpose+ "','" 
						+ Jsfs+ "','"+requestReason+"',"+ Eassign+",'"+ Eastime+ "',"+ oasign+ ",'" + Oatime + "',0,0)";

				String sqlEntry = "";
				for (int i = 0; i < entrys.size(); i++) {
					Map<String, String> map = entrys.get(i);
					sqlEntry = "insert into eas_lolkk_fk_sub(parentID,fnumber,Companynum,Companyname,requestAmount,formtypezhi) values('"
							+ map.get("parentID")
							+ "','"
							+ map.get("fnumber")
							+ "','"
							+ map.get("Companynum")
							+ "','"
							+ map.get("Suppliername")
							+ "',"
							+ map.get("requestAmount") + ","+ map.get("formtypezhi")+")";
					System.out.println("sqlEntry:" + sqlEntry);
					EAISynTemplate.execute(ctx, database, sqlEntry);
					AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
							DateBasetype.PaymentBillToMid, payRequestBillInfo
									.getNumber(), payRequestBillInfo
									.getString("PaymentBillToMid"), "单据保存成功");// 记录日志
				}

				ArrayList<Map<String,String>> attachmentlist = AppUnit.getAttachmentList(ctx, id);
				if(attachmentlist!=null && attachmentlist.size() > 0){
					for (Map<String, String> mp : attachmentlist) {
						sqlEntry = "insert into eas_lolkk_fk_file(parentID,filename,filepath) values('"
							+ id+ "','"+ mp.get("pre")+ "','"
							+ mp.get("path")+ "')";
						EAISynTemplate.execute(ctx, database, sqlEntry);
					}
				}
				
				
				System.out.println("sql:" + sql);
				EAISynTemplate.execute(ctx, database, sql);

				result = "运行结束，插入成功。";
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
								DateBasetype.PaymentBillToMid, "单据保存失败", e
										.getMessage());// 记录日志
				String msg = "运行失败，异常是：" + e.toString();
				return msg;
			}
		} else {
			result = "运行失败，数据已经存在。";
		}
		return result;
	}

	protected Boolean isExistsByBillId(Context ctx, String database,
			String billID) {
		return EAISynTemplate.existsoa(ctx, database, "eas_lolkk_fk", billID);
	}

	/*
	 * 采购申请单 PurRequestInfo 1
	 */
	@Override
	protected String _PurRequestFormOA(Context ctx, String dataBase1)
			throws BOSException {

		String sql = null;
		//,ISGIFT
		sql = "select id,fnumber,bizdate,purchType,purchModel,company,requestAmount,applyer,CGFK_APPLYER,isGift from eas_lolkk_cg where eassign = 0";
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String, Object>> list = EAISynTemplate.query(ctx, dataBase1,sql.toString());
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
				Boolean addOrUpdate = false;
				Boolean flag = true;
				fid = map.get("ID").toString();
				PurRequestInfo info = null;
				System.out.println("_--------------------------------------"
						+ map.get("FNUMBER") + "====" + map.get("COMPANY")
						+ "-----");
				if (map.get("COMPANY") == null || map.get("COMPANY").equals("")) {
					System.out
							.println("_--------------------------------------"
									+ map.get("FNUMBER"));
					updateFSign(ctx, dataBase1, "eas_lolkk_cg", 2, fid);
					AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
							DateBasetype.OA_PurRequest, info.getNumber(), info
									.getString("OA_PurRequest"), "单据保存失败，"
									+ info.getNumber() + "的公司编码为空");// 记录日志
					continue;
				}
				if (map.get("FNUMBER") != null
						&& !map.get("FNUMBER").equals("")) {// 根据采购单号
					// 查看采购单是否有重复的
					if (PurRequestFactory.getLocalInstance(ctx).exists("where caigoushenqingdandanhao ='"+ map.get("FNUMBER") + "'")) {
						// 如果是存在的 单头 是可以修改的 但是分录是不动的
						// 判断 根据采购单号 查看是否有两种类型
						addOrUpdate = true;
						/*
						 * CoreBaseCollection collection = PurRequestFactory
						 * .getLocalInstance(ctx).getCollection(
						 * "where caigoushenqingdandanhao ='" +
						 * map.get("FNUMBER") + "'"); if (collection.size() > 1)
						 * { // 需要确定 采购申请哪些信息可以修改 } else { //
						 * info=PurRequestFactory.getLocalInstance(ctx). //
						 * getPurRequestInfo //
						 * ("where caigoushenqingdandanhao ='" //
						 * +map.get("FNUMBER")+"'"); info = (PurRequestInfo)
						 * collection.get(0); info.getEntries().clear(); flag =
						 * false; }
						 */
						updateFSign(ctx, dataBase1, "eas_lolkk_cg", 1, fid);
						continue;
					} else {
						info = new PurRequestInfo();
					}
				} else {
					info = new PurRequestInfo();
				}

				// ---------------共有信息
				info.setIsMergeBill(false);
				info.setPurchaseType(PurchaseTypeEnum.PURCHASE);
				// 业务类型
				BizTypeInfo bizTypeinfo = BizTypeFactory.getLocalInstance(ctx)
						.getBizTypeCollection("where number = '110'").get(0);
				info.setBizType(bizTypeinfo);
				
				// 行类型
				/*
				 * RowTypeInfo rowTypeInfoy = new RowTypeInfo();//原料采购
				 * rowTypeInfoy.setId(BOSUuid.read(
				 * "00000000-0000-0000-0000-0000000000017C7DC4A3"));
				 * 
				 * RowTypeInfo rowTypeInfod = new RowTypeInfo();//低值易耗品
				 * rowTypeInfod
				 * .setId(BOSUuid.read("4SooXWYKTKuLrgpPBQzUInx9xKM="));
				 * 
				 * RowTypeInfo rowTypeInfog = new RowTypeInfo();//固定资产
				 * rowTypeInfog
				 * .setId(BOSUuid.read("eLKiKZWaRU2OjdM1BH+1HXx9xKM="));
				 */

				RowTypeInfo rowTypeInfoy = RowTypeFactory.getLocalInstance(ctx)
						.getRowTypeInfo("where number = '" + "010" + "'");

				RowTypeInfo rowTypeInfod = RowTypeFactory.getLocalInstance(ctx)
						.getRowTypeInfo("where number = '" + "210" + "'");

				RowTypeInfo rowTypeInfog = RowTypeFactory.getLocalInstance(ctx)
						.getRowTypeInfo("where number = '" + "200" + "'");

				// 币别
				CurrencyInfo currency = CurrencyFactory.getLocalInstance(ctx)
						.getCurrencyCollection("where number='BB01'").get(0);
				ObjectUuidPK orgPK = new ObjectUuidPK(map.get("COMPANY")
						.toString());
				CompanyOrgUnitInfo xmcompany = CompanyOrgUnitFactory
						.getLocalInstance(ctx).getCompanyOrgUnitInfo(orgPK);
				info.setCompanyOrgUnit(xmcompany);
				System.out.println("------------------所属公司："
						+ xmcompany.getId() + "----" + xmcompany.getName());
				AdminOrgUnitInfo admin = AdminOrgUnitFactory.getLocalInstance(
						ctx).getAdminOrgUnitInfo(orgPK);
				info.setAdminOrg(admin);
				// 库存组织
				StorageOrgUnitInfo storageorginfo = StorageOrgUnitFactory
						.getLocalInstance(ctx).getStorageOrgUnitInfo(orgPK);
				// StorageOrgUnitInfo storageOrgUnitInfo = new
				// StorageOrgUnitInfo();
				// storageOrgUnitInfo.setId(BOSUuid.read(map.get("company").
				// toString()));

				// 采购组织
				PurchaseOrgUnitInfo purchaseorginfo = PurchaseOrgUnitFactory
						.getLocalInstance(ctx).getPurchaseOrgUnitInfo(orgPK);
				// PurchaseOrgUnitInfo purchaseorginfo = new
				// PurchaseOrgUnitInfo();
				//purchaseorginfo.setId(BOSUuid.read(map.get("company").toString
				// ()));

				// 申请人
				PersonInfo person = PersonFactory.getLocalInstance(ctx)
						.getPersonCollection(
								"where number='"
										+ map.get("APPLYER").toString() + "'")
						.get(0);
				info.setPerson(person);

				// oa采购付款单发起人员
				if (map.get("CGFK_APPLYER") != null
						&& !"".equals(map.get("CGFK_APPLYER").toString())) {
					PersonInfo CGFK_APPLYER = PersonFactory.getLocalInstance(
							ctx).getPersonCollection(
							"where number='"
									+ map.get("CGFK_APPLYER").toString() + "'")
							.get(0);
					info.put("caigourenyuan", CGFK_APPLYER);
				}
				//测试
//				PersonInfo person = new PersonInfo();
//				person.setId(BOSUuid.read("jbYAAACupyyA733t"));
//				info.setPerson(person);
//				info.put("caigourenyuan", person);
				
				/*int isGift = Integer.getInteger(map.get("shifouzengpin").toString());
				info.put("caigourenyuan", isGift);*/
				// 业务日期
				SimpleDateFormat formmat = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				Date bizDate = null;
				String purchModel = "";// oa单据类型
				try {/*
					 * if(map.get("COMPANY")==null ||
					 * "".equals(map.get("COMPANY").toString())){ bizDate =
					 * formmat.parse(map.get("COMPANY").toString()); }
					 */
					if (map.get("BIZDATE") != null
							&& !"".equals(map.get("BIZDATE").toString())) {
						bizDate = formmat.parse(map.get("BIZDATE").toString());
					} else {
						bizDate = new Date();
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				info.setBizDate(bizDate); // 业务日期

				// info.setTotalAmount(new
				// BigDecimal(map.get("REQUESTAMOUNT").toString()));//申请金额 金额合计

				// OA采购申请单单号
				info.put("caigoushenqingdandanhao", map.get("FNUMBER"));
			
				
				// 申请金额
				// info.put("OAcaigoushenqingdanjine",
				// map.get("REQUESTAMOUNT"));

				if (VerifyUtil.notNull(map.get("PURCHMODEL"))) {
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
					} else if("feiqixie".equals(map.get("PURCHMODEL"))){
						purchModel = "7";
					}
				}
				giftFlag = false ;
				//是否赠品
				if(map.get("ISGIFT")!=null && !"".equals(map.get("ISGIFT").toString())){
					info.put("isGift", map.get("ISGIFT"));
					if("1".equals(purchModel)||"3".equals(purchModel)||"4".equals(purchModel)||"7".equals(purchModel)){
						if("1".equals(map.get("ISGIFT").toString()))	
							giftFlag = true ;
					}
				}
				else
					info.put("isGift","0");	
				
				if (VerifyUtil.notNull(purchModel)) {
					// 单据类型
					info.put("danjuleixzing", purchModel);// 暂时
				}

				BillTypeInfo billtype = new BillTypeInfo();
				billtype.setId(BOSUuid
						.read("510b6503-0105-1000-e000-0107c0a812fd463ED552"));
				info.setBillType(billtype);// 单据类型 暂时写死

				/*
				 * if(map.get("APPLYER")!= null &&
				 * PersonFactory.getLocalInstance(ctx).exists(new
				 * ObjectUuidPK(map.get("APPLYER").toString()))){ PersonInfo
				 * personInfo = new PersonInfo();
				 * personInfo.setId(BOSUuid.read(map
				 * .get("APPLYER").toString())); info.setPerson(personInfo); }
				 */

				// ---------------共有信息 ------ end
				if (map.get("PURCHTYPE") != null
						&& map.get("PURCHTYPE").equals("10")) {// 如果是 10 固定资产类型
					// 则进行拆单

					if (flag) {// 判读是否已存在 不存在才进入 插入分录的数据
						PurRequestInfo infoBig = (PurRequestInfo) info.clone();
						sql = "select parentid,material,materialname,supplier,brand,guige,xh,artNo,price,qty,amount from eas_lolkk_cg_sub where parentid ="
								+ map.get("ID");
						// OA_EAS_PurRequestEntry
						List<Map<String, Object>> list1 = EAISynTemplate.query(
								ctx, dataBase1, sql);
						BigDecimal totalAmountSmall = new BigDecimal(0);
						BigDecimal totalAmountBig = new BigDecimal(0);
					
						if (list1 != null && list1.size() > 0) {
							for (Map<String, Object> map1 : list1) {
								PurRequestEntryInfo entryInfo = new PurRequestEntryInfo();
								 qtymultiple = new BigDecimal(1);
								// EntityViewInfo viewInfo = new
								// EntityViewInfo();
								// FilterInfo filter = new FilterInfo();
								// filter.getFilterItems().add(new
								// FilterItemInfo("number",
								// map1.get("MATERIAL").
								// toString(),CompareType.EQUALS));
								// viewInfo.setFilter(filter);
								// IMaterial imaterial =
								// MaterialFactory.getLocalInstance(ctx);
								// MaterialCollection collection =
								// imaterial.getMaterialCollection(viewInfo);
								// MaterialInfo material = collection.get(0); //
								// 物料
								//								

								try {
									EntityViewInfo viewInfo = new EntityViewInfo();
									FilterInfo filter = new FilterInfo();
									filter.getFilterItems().add(
											new FilterItemInfo("number",
													map1.get("MATERIAL")
															.toString(),
													CompareType.EQUALS));
									viewInfo.setFilter(filter);
									IMaterial imaterial = MaterialFactory
											.getLocalInstance(ctx);
									MaterialCollection collection = imaterial
											.getMaterialCollection(viewInfo);
									MaterialInfo material = collection.get(0); // 物料
									entryInfo.setMaterialName(material
											.getName());
									entryInfo.setNoNumMaterialModel(material
											.getModel());
									entryInfo.setMaterial(material);
									MeasureUnitInfo unitInfo = new MeasureUnitInfo();
									MeasureUnitInfo baseUnitInfo = new MeasureUnitInfo();
									unitID = materialUnitId(ctx,map.get("COMPANY").toString(),material.getId().toString());
								 if(unitID !=null && !"".equals(unitID)){
									 unitInfo.setId(BOSUuid.read(unitID));
									 baseUnitInfo = material.getBaseUnit();
									 if(!material.getBaseUnit().getId().toString().equals(unitID)){
										qtymultiple = getmaterialMultiple(ctx,material.getId().toString(),unitID);
									 }
								 }else{
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
											DateBasetype.OA_PurRequest, info
													.getNumber(),
													map.get("FNUMBER").toString(),
											"单据保存失败，" + info.getNumber()
													+ "物料编码不存在");// 记录日志
								}

								BigDecimal amount = new BigDecimal(0.00);
								BigDecimal price = new BigDecimal(0.00);
								if (map1.get("AMOUNT") != null
										&& !"".equals(map1.get("AMOUNT")
												.toString().trim())) {
									amount = new BigDecimal(map1.get("AMOUNT")
											.toString().trim());// 金额
								}
								if (map1.get("PRICE") != null
										&& !"".equals(map1.get("PRICE")
												.toString().trim())) {
									price = new BigDecimal(map1.get("PRICE")
											.toString().trim());// 单价
								}
								BigDecimal qty = new BigDecimal(map1.get("QTY")
										.toString());// 数量

								// 规格
								if (VerifyUtil.notNull(map1.get("GUIGE"))) {
									entryInfo.setNoNumMaterialModel(map1.get(
											"GUIGE").toString());// 规格型号 暂时使用规格
								}

								if (VerifyUtil.notNull(map1.get("SUPPLIER"))) {
									if (SupplierFactory
											.getLocalInstance(ctx)
											.exists(
													"where number='"
															+ map1
																	.get("SUPPLIER")
															+ "'")) {
										SupplierInfo supplierInfo = SupplierFactory
												.getLocalInstance(ctx)
												.getSupplierInfo(
														" where number='"
																+ map1
																		.get("SUPPLIER")
																+ "'");
										entryInfo.setSupplier(supplierInfo);
									}
								}

								entryInfo.setPerson(person);
								entryInfo.setPurchasePerson(person);// 采购员
								entryInfo.setReceivedOrgUnit(storageorginfo);// 收货组织
								// entryInfo.setMergeBillSeq(0);
								entryInfo.setAdminOrgUnit(admin);
								entryInfo.setBizDate(info.getBizDate());
								// entryInfo.setMaterial(material);
								//entryInfo.setBaseUnit(material.getBaseUnit());
								// entryInfo.setUnit(material.getBaseUnit());

								entryInfo.setQty(qty);
								entryInfo.setAssociateQty(qty);
								entryInfo.setBaseQty(qty.multiply(qtymultiple));// 基本数量
								entryInfo.setRequestQty(qty);
								entryInfo.setUnOrderedQty(BigDecimal.ZERO);
								entryInfo.setUnOrderedBaseQty(BigDecimal.ZERO);
								entryInfo.setAssistQty(BigDecimal.ZERO);
								//entryInfo.setMaterialName(material.getName());
								// entryInfo.setNoNumMaterialModel(material.
								// getModel());
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
								// 设置OA传递过来的规格 型号字段进行拼接
								String gugexh = "";
								if (null != map1.get("GUIGE")
										&& !map1.get("GUIGE").toString()
												.equals("")) {
									gugexh = map1.get("GUIGE").toString(); 
									entryInfo.setNoNumMaterialModel(gugexh);
								}  
								entryInfo.put("xinghao", map1.get("XH"));
								entryInfo.put("pinpai", map1.get("BRAND"));
								entryInfo.put("huohao", map1.get("ARTNO"));
								
								entryInfo.setRequirementDate(bizDate);// 需求日期
								entryInfo.setProposeDeliveryDate(bizDate);
								entryInfo.setProposePurchaseDate(bizDate);
								
								if(giftFlag){
									entryInfo.setRowType(rowTypeInfoy);// 行类型
									info.getEntries().add(entryInfo);
									totalAmountSmall = totalAmountSmall.add(amount);
								}else{
									if ((price.compareTo(new BigDecimal(2000))) == -1) { // 小于
										entryInfo.setRowType(rowTypeInfod);// 行类型
										info.getEntries().add(entryInfo);
										totalAmountSmall = totalAmountSmall.add(amount);
									} else {
										entryInfo.setRowType(rowTypeInfog);// 行类型
										infoBig.getEntries().add(entryInfo);
										totalAmountBig = totalAmountBig.add(amount);
									}
								}
								  
							}
						} else {
							System.out
									.println("entrty is empty _--------------------------------------"
											+ map.get("FNUMBER"));
							updateFSign(ctx, dataBase1, "eas_lolkk_cg", 2, fid);
							AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
									DateBasetype.OA_PurRequest, info
											.getNumber(),map.get("FNUMBER").toString(),
									"单据保存失败，" + info.getNumber() + "的没有分录");// 记录日志
							continue;
						}
						// 设置类型
						if (info.getEntries().size() > 0) {
							info.setTotalAmount(totalAmountSmall);
							info.setLocalTotalAmount(totalAmountSmall);
							DemandTypeInfo type = new DemandTypeInfo();
							if(giftFlag){
								type.setId(BOSUuid.read("d8iX3GB6dt3gUwEAAH8kOKvcMAg="));// 原料采购
							}else{
								type.setId(BOSUuid.read("nMyVhAcxRvyXPYq+xI49eqvcMAg="));// 低值易耗品
							}
					
							info.setDemandType(type);
							// OA采购申请单金额
							info.put("caigoushenqingdanjine", totalAmountSmall);
							PurRequestFactory.getLocalInstance(ctx).save(info);// 小的
							// submitInt = 1;
							//PurRequestFactory.getLocalInstance(ctx).submit(info
							// );//
						}
						if (infoBig.getEntries().size() > 0) {
							infoBig.setTotalAmount(totalAmountBig);
							infoBig.setLocalTotalAmount(totalAmountBig);
							DemandTypeInfo type2 = new DemandTypeInfo();
							// type2.setId(BOSUuid.read(
							// "d8XjLJBgKZrgUwEAAH9KPqvcMAg="));//固定资产
							type2.setId(BOSUuid
									.read("Sp/A4ZhGTD2izLb/R8/WfavcMAg="));// 固定资产
							infoBig.setDemandType(type2);
							// OA采购申请单金额
							infoBig
									.put("caigoushenqingdanjine",
											totalAmountBig);
							PurRequestFactory.getLocalInstance(ctx).save(
									infoBig);// 大的
							// submitInt = 2;
							// PurRequestFactory.getLocalInstance(ctx).submit(
							// infoBig);
						}

						if (info.getEntries().size() > 0) {
							submitInt = 1;
							PurRequestFactory.getLocalInstance(ctx)
									.submit(info);//
						}

						if (infoBig.getEntries().size() > 0) {
							submitInt = 2;
							PurRequestFactory.getLocalInstance(ctx).submit(
									infoBig);
						}
						updateFSign(ctx, dataBase1, "eas_lolkk_cg", 1, map.get(
								"ID").toString());
						AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
								DateBasetype.OA_PurRequest, info.getNumber(),
								map.get("FNUMBER").toString(), "单据保存成功");// 记录日志

					} else {
						// 要判断拆单之后 一个采购单单号 可能会有两个单子 所以 要判断
						PurRequestFactory.getLocalInstance(ctx).save(info);
						submitInt = 1;
						PurRequestFactory.getLocalInstance(ctx).submit(info);
						// PurRequestFactory.getLocalInstance(ctx).audit(pk);
						updateFSign(ctx, dataBase1, "eas_lolkk_cg", 1, map.get(
								"ID").toString());
						AppUnit.insertLog(ctx, DateBaseProcessType.Update,
								DateBasetype.OA_PurRequest, info.getNumber(),
								map.get("FNUMBER").toString(), "单据修改成功");// 记录日志

					}

				} else {// 不用
					BigDecimal totalAmount = new BigDecimal(0);
					if (flag) {// 判读是否已存在 不存在才进入 插入分录的数据
						sql = "select parentid,material,materialname,supplier,brand,guige,xh,artNo,price,qty,amount from eas_lolkk_cg_sub where parentid ="
								+ map.get("ID");
						// OA_EAS_PurRequestEntry
						List<Map<String, Object>> list1 = EAISynTemplate.query(
								ctx, dataBase1, sql);

						if (list1 != null && list1.size() > 0) {
							for (Map<String, Object> map1 : list1) {
								PurRequestEntryInfo entryInfo = new PurRequestEntryInfo();
								 qtymultiple = new BigDecimal(1);
								entryInfo.setRowType(rowTypeInfoy);// 行类型
								// EntityViewInfo viewInfo = new
								// EntityViewInfo();
								// FilterInfo filter = new FilterInfo();
								// filter.getFilterItems().add(new
								// FilterItemInfo("number",
								// map1.get("MATERIAL").
								// toString(),CompareType.EQUALS));
								// viewInfo.setFilter(filter);
								// IMaterial imaterial =
								// MaterialFactory.getLocalInstance(ctx);
								// MaterialCollection collection =
								// imaterial.getMaterialCollection(viewInfo);
								// MaterialInfo material = collection.get(0); //
								// 物料

								try {
									EntityViewInfo viewInfo = new EntityViewInfo();
									FilterInfo filter = new FilterInfo();
									filter.getFilterItems().add(
											new FilterItemInfo("number",
													map1.get("MATERIAL")
															.toString(),
													CompareType.EQUALS));
									viewInfo.setFilter(filter);
									IMaterial imaterial = MaterialFactory
											.getLocalInstance(ctx);
									MaterialCollection collection = imaterial
											.getMaterialCollection(viewInfo);
									MaterialInfo material = collection.get(0); // 物料
									entryInfo.setMaterialName(material
											.getName());
									entryInfo.setNoNumMaterialModel(material
											.getModel());
									entryInfo.setMaterial(material);
									MeasureUnitInfo unitInfo = new MeasureUnitInfo();
									MeasureUnitInfo baseUnitInfo = new MeasureUnitInfo();
									unitID = materialUnitId(ctx,map.get("COMPANY").toString(),material.getId().toString());
								 if(unitID !=null && !"".equals(unitID)){
									 unitInfo.setId(BOSUuid.read(unitID));
									 baseUnitInfo = material.getBaseUnit();
									 if(!material.getBaseUnit().getId().toString().equals(unitID)){
										qtymultiple = getmaterialMultiple(ctx,material.getId().toString(),unitID);
									 }
								 }else{
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
											DateBasetype.OA_PurRequest, info
													.getNumber(),map.get("FNUMBER").toString(),
											"单据保存失败，" + info.getNumber()
													+ "物料编码不存在");// 记录日志
								}

								BigDecimal amount = new BigDecimal(0.00);
								BigDecimal price = new BigDecimal(0.00);
								if (map1.get("AMOUNT") != null
										&& !"".equals(map1.get("AMOUNT")
												.toString().trim())) {
									amount = new BigDecimal(map1.get("AMOUNT")
											.toString().trim());// 金额
								}
								if (map1.get("PRICE") != null
										&& !"".equals(map1.get("PRICE")
												.toString().trim())) {
									price = new BigDecimal(map1.get("PRICE")
											.toString().trim());// 单价
								}
								BigDecimal qty = new BigDecimal(map1.get("QTY")
										.toString());// 数量

								// 规格
								if (VerifyUtil.notNull(map1.get("GUIGE"))) {
									entryInfo.setNoNumMaterialModel(map1.get(
											"GUIGE").toString());
								}
								if (VerifyUtil.notNull(map1.get("SUPPLIER"))) {
									if (SupplierFactory
											.getLocalInstance(ctx)
											.exists(
													"where number='"
															+ map1
																	.get("SUPPLIER")
															+ "'")) {
										SupplierInfo supplierInfo = SupplierFactory
												.getLocalInstance(ctx)
												.getSupplierInfo(
														" where number='"
																+ map1
																		.get("SUPPLIER")
																+ "'");
										entryInfo.setSupplier(supplierInfo);
									}
								}
								entryInfo.setPerson(person);
								entryInfo.setPurchasePerson(person);// 采购员
								entryInfo.setReceivedOrgUnit(storageorginfo);// 收货组织
								// entryInfo.setMergeBillSeq(0);

								// entryInfo.setMaterial(material);
								entryInfo.setAdminOrgUnit(admin);
								entryInfo.setBizDate(info.getBizDate());
								//entryInfo.setBaseUnit(material.getBaseUnit());
								// entryInfo.setUnit(material.getBaseUnit());

								entryInfo.setQty(qty);
								entryInfo.setAssociateQty(qty);
								entryInfo.setBaseQty(qty.multiply(qtymultiple));// 基本数量

								entryInfo.setUnOrderedQty(BigDecimal.ZERO);
								entryInfo.setUnOrderedBaseQty(BigDecimal.ZERO);
								entryInfo.setAssistQty(BigDecimal.ZERO);
								//entryInfo.setMaterialName(material.getName());
								// entryInfo.setNoNumMaterialModel(material.
								// getModel());
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
								// 设置OA传递过来的规格 型号字段进行拼接
								String gugexh = "";
								if (null != map1.get("GUIGE")
										&& !map1.get("GUIGE").toString()
												.equals("")) {
									gugexh = map1.get("GUIGE").toString(); 
									entryInfo.setNoNumMaterialModel(gugexh);
								}  
								entryInfo.put("xinghao", map1.get("XH"));
								entryInfo.put("pinpai", map1.get("BRAND"));
								entryInfo.put("huohao", map1.get("ARTNO"));
								entryInfo.setRequestQty(qty);// 申请数量
								entryInfo.setRequirementDate(bizDate);// 需求日期
								entryInfo.setProposeDeliveryDate(bizDate);
								entryInfo.setProposePurchaseDate(bizDate);

								// entryInfo.setString(key, val)
								totalAmount = totalAmount.add(amount);
								info.getEntries().add(entryInfo);
							}
						} else {
							System.out
									.println("entrty is empty _--------------------------------------"
											+ map.get("FNUMBER"));
							updateFSign(ctx, dataBase1, "eas_lolkk_cg", 2, fid);
							AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
									DateBasetype.OA_PurRequest, info
											.getNumber(), map.get("FNUMBER").toString(),
									"单据保存失败，" + info.getNumber() + "的没有分录");// 记录日志
							continue;
						}
					}
					info.put("caigoushenqingdanjine", totalAmount);
					info.setTotalAmount(totalAmount);
					info.setLocalTotalAmount(totalAmount);
					DemandTypeInfo type = new DemandTypeInfo();
					//type.setId(BOSUuid.read("d8XjLJBfKZrgUwEAAH9KPqvcMAg="));/
					// / 原料采购
					type.setId(BOSUuid.read("d8iX3GB6dt3gUwEAAH8kOKvcMAg="));// 原料采购
					info.setDemandType(type);

					PurRequestFactory.getLocalInstance(ctx).save(info);
					submitInt = 1;
					PurRequestFactory.getLocalInstance(ctx).submit(info);//
					updateFSign(ctx, dataBase1, "eas_lolkk_cg", 1, map
							.get("ID").toString());

					if (addOrUpdate) {
						AppUnit.insertLog(ctx, DateBaseProcessType.Update,
								DateBasetype.OA_PurRequest, info.getNumber(),
								info.getString("OA_PurRequest"), "单据修改成功");// 记录日志
					} else {
						AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
								DateBasetype.OA_PurRequest, info.getNumber(),
								info.getString("OA_PurRequest"), "单据保存成功");// 记录日志
					}
				}

				// AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
				// DateBasetype.HIS_PurchIn,
				// info.getNumber(),info.getString("HisReqID"),"单据保存成功");//记录日志
			}

		} catch (EASBizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String msg = "";
			if (submitInt == 0) {
				AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
						DateBasetype.OA_PurRequest, fid+"单据保存失败", e.getMessage());// 记录日志
				if (fid != null && !fid.equals("")) {
					updateFSign(ctx, dataBase1, "eas_lolkk_cg", 2, fid);
				}
				msg = "运行失败，异常是：" + e.toString();
			} else {// 保存成功，提交失败
				msg = "单据提交失败: " + e.toString();
				updateFSign(ctx, dataBase1, "eas_lolkk_cg", 1, fid);
			}
			return msg;
			// AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
			// DateBasetype.HIS_PurchIn,"","","单据保存失败");//记录日志
		}
		return super._PurRequestFormOA(ctx, dataBase1);
	}
  
	/**
	 * 备用金付款单同步到eas
	 */

	@Override
	public String syncPaymentBillFormOA(Context ctx, String database)throws BOSException {
		
		//将eas_lolkk_bx表中申请人不存在的数据修改为状态2
	//	updateNoPeople(ctx,database);
		
		String sql = null;
		sql = " select bx.id,bx.fnumber,bx.bizDate,bx.isLoan,bx.payType,bx.isrentalfee,bx.company,bx.Dept,bx.supplierid,bx.Yhzh,bx.Khh,bx.applyer, "+
				" bx.Applyerbank,bx.Applyerbanknum,bx.Agency,bx.Amount,bx.Jsfs,bx.purchType,bx.purchModel,bx.Paystate,bx.Paystatetime,bx.ispre  "+
				" from eas_lolkk_bx bx  "+" where (bx.PURCHTYPE = '08' or bx.ispre = 1) and bx.eassign = 0";
		Calendar cal = Calendar.getInstance();
		List<Map<String, Object>> list = EAISynTemplate.query(ctx, database,sql.toString());
		String fid = null;
		try {
			System.out.println("--------------------------" + list.size());
			for (Map<String, Object> map : list) {
				fid = map.get("ID").toString();
				PaymentBillInfo payInfo = new PaymentBillInfo();
				if (map.get("COMPANY") == null || map.get("COMPANY").toString().equals("")) {
					System.out.println("_--------------------------------------"+ map.get("FNUMBER"));
					updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
					AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,DateBasetype.OA_PaymentBill, payInfo.getNumber(), payInfo.getString("OA_PaymentBill"), "单据保存失败,"+ payInfo.getNumber() + "的公司编码为空");
					continue;
				}
				if (map.get("FNUMBER") != null && !map.get("FNUMBER").toString().equals("")) {//查看编码是否存在，如果存在这修改中间表的状态，然后执行下一个
					if (PaymentBillFactory.getLocalInstance(ctx).exists("where caigoushenqingdandanhao ='"+ map.get("FNUMBER") + "'")) {
						updateFSign(ctx, database, "eas_lolkk_bx", 1, map.get("ID").toString());
						continue;
					}
				}
				
				payInfo.setSourceType(com.kingdee.eas.fi.cas.SourceTypeEnum.AP);//来源系统
				payInfo.setDescription("无");//摘要
				payInfo.setIsExchanged(false); //是否已经调汇 
				payInfo.setExchangeRate(new BigDecimal("1.00"));// 汇率 
				payInfo.setLastExhangeRate(new BigDecimal("0.00"));//最后调汇汇率
				payInfo.setIsInitializeBill(false); //是否初始化单据
				CurrencyInfo currency = CurrencyFactory.getLocalInstance(ctx).getCurrencyCollection("where number='BB01'").get(0);
				payInfo.setCurrency(currency);// 币别
				payInfo.setFiVouchered(false);//是否已生成凭证
				payInfo.setIsLanding(false); //强制落地
				//？？  单据状态  集中结算状态
				//payInfo.setPayType(0); 
				
				AsstActTypeInfo actType = new AsstActTypeInfo(); 
				actType.setId(BOSUuid.read("YW3xsAEJEADgAAWgwKgTB0c4VZA="));//--职员的
				payInfo.setPayeeType(actType);//往来户类型
				payInfo.setIsImport(false); //是否导入
				payInfo.setIsNeedPay(true);//是否需要支付
				payInfo.setIsReverseLockAmount(true);//是否反写锁定金额
				payInfo.setPaymentBillType(CasRecPayBillTypeEnum.commonType);//付款单类型
				PaymentBillTypeInfo  billType = PaymentBillTypeFactory.getLocalInstance(ctx).getPaymentBillTypeInfo(new ObjectUuidPK("NLGLdwEREADgAAHjwKgSRj6TKVs="));
				payInfo.setPayBillType(billType);//付款类型
				PaymentTypeInfo paymentTypeInfo = PaymentTypeFactory.getLocalInstance(ctx).getPaymentTypeInfo(new ObjectUuidPK("2fa35444-5a23-43fb-99ee-6d4fa5f260da6BCA0AB5"));
				payInfo.setPaymentType(paymentTypeInfo);//付款方式  
				
				// 结算方式 默认:电汇  02
				SettlementTypeInfo settlementTypeInfo = SettlementTypeFactory.getLocalInstance(ctx)
						.getSettlementTypeInfo(new ObjectUuidPK("e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
				payInfo.setSettlementType(settlementTypeInfo);
				
				// OA采购申请单单号
				payInfo.put("caigoushenqingdandanhao", map.get("FNUMBER").toString());

				//公司
				ObjectUuidPK orgPK = new ObjectUuidPK(map.get("COMPANY").toString());
				CompanyOrgUnitInfo company = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo(orgPK);
				payInfo.setCompany(company);
				System.out.println("------------------所属公司："+ company.getId() + "----" + company.getName());

				
				PersonInfo person = PersonFactory.getLocalInstance(ctx).getPersonCollection("where number='"+ map.get("APPLYER").toString() + "'").get(0);
				//payInfo.setPerson(person);//人员
				
				//PersonInfo person = PersonFactory.getLocalInstance(ctx).getPersonCollection("where number='"+ map.get("APPLYER").toString() + "'").get(0);
				payInfo.setPayeeID(person.getId().toString());//开户行id
				payInfo.setPayeeName(person.getName());//开户行名字
				payInfo.setPayeeNumber(person.getNumber());//开行编码
				payInfo.setPayeeBank(map.get("APPLYERBANK").toString());//收款银行
				payInfo.setPayeeAccountBank(map.get("APPLYERBANKNUM").toString());//收款账号
				/*payInfo.setPayeeBank(map.get("KHH").toString());//收款银行
				payInfo.setPayeeAccountBank(map.get("YHZH").toString());//收款账号*/				
				payInfo.setBankAcctName(person.getName());//收款人实名
				
				AdminOrgUnitInfo admin = null;
				if(map.get("DEPT")!=null && !"".equals(map.get("DEPT").toString())){
					admin = AdminOrgUnitFactory.getLocalInstance(ctx).getAdminOrgUnitInfo(new ObjectUuidPK(map.get("DEPT").toString()));
					payInfo.setAdminOrgUnit(admin); //部门
					/*CostCenterOrgUnitInfo CostCenter = CostCenterOrgUnitFactory.getLocalInstance(ctx).getCostCenterOrgUnitInfo(new ObjectUuidPK(map.get("DEPT").toString()));
					payInfo.setCostCenter(CostCenter);//成本中心	*/					
					//SupplierInfo supplier = SupplierFactory.getLocalInstance(ctx).getSupplierInfo(new ObjectUuidPK(map.get("DEPT").toString()));
					
					//payInfo.setBankNumber(item)
					
				}
				 
				// 业务日期
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
				
				payInfo.setBizDate(bizDate); // 业务日期
				payInfo.setBillDate(new Date());// 订单日期
				

				//自定义字段-------------------
				/*String jk = null;
				if (map.get("ISLOAN").toString().equals("0")) {
					jk = "否";
				} else if (map.get("ISLOAN").toString().equals("1")) {
					jk = "是";
				}
				// 是否借款
				payInfo.put("shifoujiekuan", jk);

				String zlf = null;
				if (map.get("ISRENTALFEE").toString().equals("0")) {
					zlf = "否";
				} else if (map.get("ISRENTALFEE").toString().equals("1")) {
					zlf = "是";
				}
				// 是否租赁费
				payInfo.put("shifouzulinfei", zlf);*/

				// 银行卡号
				payInfo.put("yinhangzhanghao", map.get("YHZH"));
				// 开户行
				payInfo.put("kaihuhang", map.get("KHH"));

				//自定义字段-------------------end

				BigDecimal totalAmount = new BigDecimal(map.get("AMOUNT").toString()) ; 
				String entrySql = "select parentID,id,payTypecode,payTypeName,Price,qty,amount,Yjk,Ytbk,remark from eas_lolkk_bx_sub where parentid ='"+ map.get("ID").toString()+"' ";
				List<Map<String, Object>> enrtyList = EAISynTemplate.query(ctx, database,entrySql.toString());
				if(enrtyList != null && enrtyList.size()>0){
					for(Map<String, Object> entryMap : enrtyList){
						BigDecimal amount = new BigDecimal(entryMap.get("AMOUNT").toString());
						PaymentBillEntryInfo entryInfo = new PaymentBillEntryInfo();
							// 费用项目  费用类型
						if (entryMap.get("PAYTYPECODE") != null && !"".equals(entryMap.get("PAYTYPECODE").toString())) {
								ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx)
									.getExpenseTypeInfo("where number ='"+ entryMap.get("PAYTYPECODE").toString() + "'");
								entryInfo.setExpenseType(typeinfo);
						}else {
								ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx).getExpenseTypeInfo("where number ='CL01'");
								entryInfo.setExpenseType(typeinfo);
						}

						entryInfo.setCurrency(currency);// 币别
						entryInfo.setAmount(amount);//应付金额  
						entryInfo.setAmountVc(BigDecimal.ZERO);//应收（付）金额累计核销 
						entryInfo.setLocalAmt(amount);//应收（付）本位币金额
						entryInfo.setLocalAmtVc(BigDecimal.ZERO);//应收（付）本位币累计核销
						entryInfo.setUnVcAmount(amount); //应收（付）未核销金额
						entryInfo.setUnVcLocAmount(amount); //应收（付）未核销本位币金额
						entryInfo.setUnVerifyExgRateLoc(BigDecimal.ZERO); //未结算调汇本位币金额
						entryInfo.setRebate(BigDecimal.ZERO); //现金折扣
						entryInfo.setRebateAmtVc(BigDecimal.ZERO); //折扣金额累计核销
						entryInfo.setRebateLocAmt(BigDecimal.ZERO); //折扣本位币金额
						entryInfo.setRebateLocAmtVc(BigDecimal.ZERO); //折扣本位币金额累计核销
						entryInfo.setActualAmt(amount);//实收（付）金额
						entryInfo.setActualAmtVc(BigDecimal.ZERO);//实收（付）金额累计核销
						entryInfo.setActualLocAmt(amount);//实收（付）本位币金额
						entryInfo.setActualLocAmtVc(BigDecimal.ZERO);//实收（付）本位币金额累计核销
						entryInfo.setUnLockAmt(amount);//未锁定金额
						entryInfo.setUnLockLocAmt(amount);//未锁定本位币金额
						entryInfo.setLockAmt(BigDecimal.ZERO); //已锁定金额
						
						entryInfo.setPayableDate(new Date());//应付日期
						
						if(entryMap.get("REMARK")!=null){
							entryInfo.setRemark(entryMap.get("REMARK").toString());
						}
						
						//totalAmount = totalAmount.add(amount);
						payInfo.getEntries().addObject(entryInfo);//添加分录 
			
					}

				}else{
					System.out.println("entrty is empty _---------------------------"+ map.get("FNUMBER").toString());
					updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
					AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,DateBasetype.OA_PaymentBill, payInfo.getNumber(), payInfo.getString("OA_PaymentBill"), "单据没有分录");// 记录日志
					continue;
				}
				//payInfo.setActualPayAmount(totalAmount);//实付金额合计
				payInfo.setActPayAmtVc(BigDecimal.ZERO);//实付金额累计核销
				payInfo.setActPayAmt(totalAmount);//实付本位币金额合计
				payInfo.setActPayLocAmtVc(BigDecimal.ZERO);//实付本位币金额累计核销
				payInfo.setAmount(totalAmount);//应收金额
				payInfo.setLocalAmt(totalAmount);//应收（付）本位币金额
				payInfo.setAccessoryAmt(0); //附件数
				payInfo.setBgAmount(BigDecimal.ZERO);//预算核准金额
				payInfo.setVerifiedAmt(BigDecimal.ZERO);//已计算金额合计
				payInfo.setVerifiedAmtLoc(BigDecimal.ZERO);//已计算金额本位币合计
				payInfo.setUnVerifiedAmt(totalAmount);//未结算金额合计
				payInfo.setUnVerifiedAmtLoc(totalAmount);//未结算金额本位币合计
				payInfo.setBgCtrlAmt(totalAmount);//预算控制金额
				
				payInfo.setBillStatus(com.kingdee.eas.fi.cas.BillStatusEnum.SAVE);
				
				//payInfo.setAuditDate(new Date(System.currentTimeMillis()));//审核日期

				/*IPaymentBill iPayBill = PaymentBillFactory.getRemoteInstance();//获取实体 
				IObjectPK pk = iPayBill.addnew(payInfo);*/

				PaymentBillFactory.getLocalInstance(ctx).save(payInfo);
				updateFSign(ctx, database, "eas_lolkk_bx", 1, map.get("ID").toString());

				/*try {
					PaymentBillFactory.getLocalInstance(ctx).submit(payInfo);

					PaymentBillFactory.getLocalInstance(ctx).audit(payInfo, "");
					
				} catch (Exception e11) {
					System.out.println("保存成功，提交或者审核失败");
				}*/
			}

		} catch (EASBizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,DateBasetype.PaymentBillToMid, fid+"单据保存失败", e.getMessage());// 记录日志
			String msg = "运行失败，异常是：" + e.toString();
			System.out.println("--------------------"+msg);
			return msg;
		
		}
		return super.syncPaymentBillFormOA(ctx, database);
	}

	/**
	 * 修改同步的状态
	 * 
	 * @param ctx
	 * @param database
	 * @param tableName
	 * @param fSign
	 * @param fid
	 * @throws BOSException
	 */
	public void updateFSign(Context ctx, String database, String tableName,
			int fSign, String fid) throws BOSException {
		String updateSql = "UPDATE "
				+ tableName
				+ " set eassign = "
				+ fSign
				+ " , EASTIME = TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') where ID = '"
				+ fid + "'";
		System.out.print("--------------" + updateSql);
		EAISynTemplate.execute(ctx, database, updateSql);
	}

	public void updateFSign(Context ctx, String database, String tableName,
			int fSign, String fid, String sqlServer) throws BOSException {
		String updateSql = "UPDATE " + tableName + " set eassign = " + fSign
				+ " , EASTIME = CONVERT(varchar,GETDATE(),120) where ID = '"
				+ fid + "'";
		System.out.print("--------------" + updateSql);
		EAISynTemplate.execute(ctx, database, updateSql);
	}

	@Override
	public void syncPayApply(Context ctx, String database) throws BOSException {
		String sql = "select id ,fnumber from eas_lolkk_fk where oafinishsign = 1 and eassign=1 and paystate = 0 and CONVERT(varchar(7), bizdate, 120) >= CONVERT(varchar(7), dateadd(month,-3,getdate()) , 120)  order by bizdate desc";
		List<Map<String, Object>> list = EAISynTemplate.query(ctx, database,
				sql.toString());
		for (Map<String, Object> map : list) {
			String id = (String) map.get("ID");
			PayRequestBillInfo payRequestBillInfo;
			try {
				payRequestBillInfo = PayRequestBillFactory
						.getLocalInstance(ctx).getPayRequestBillInfo(
								new ObjectUuidPK(id));
				if (payRequestBillInfo != null) {
					if (payRequestBillInfo.getBillStatus() != BillStatusEnum.AUDITED) {
						PayRequestBillFactory.getLocalInstance(ctx).audit(
								new ObjectUuidPK(id));
					}
				}
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		String sqlNo = "select id ,fnumber from eas_lolkk_fk where oafinishsign = 2 and eassign=1 and paystate = 2 and CONVERT(varchar(7), bizdate, 120) >= CONVERT(varchar(7), dateadd(month,-3,getdate()) , 120)  order by bizdate desc";
		List<Map<String, Object>> listNo = EAISynTemplate.query(ctx, database,
				sqlNo.toString());
		for (Map<String, Object> map : listNo) {
			String id = (String) map.get("ID");
			PayRequestBillInfo payRequestBillInfo;
			try {
				payRequestBillInfo = PayRequestBillFactory
						.getLocalInstance(ctx).getPayRequestBillInfo(
								new ObjectUuidPK(id));
				if (payRequestBillInfo != null) {
					if (payRequestBillInfo.getBillStatus() != BillStatusEnum.DELETED
							|| payRequestBillInfo.getBillStatus() != BillStatusEnum.AUDITED) {
						payRequestBillInfo
								.setBillStatus(BillStatusEnum.DELETED);
						PayRequestBillFactory.getLocalInstance(ctx).update(
								new ObjectUuidPK(id), payRequestBillInfo);
					}
				}
			} catch (EASBizException e) {
 				e.printStackTrace();
			} 
		}
	}
 
	@Override
	public void mobilePaymentBillBizDate(Context ctx, String ids, String date,
			String type) throws BOSException { 
		if(type != null && "cas".equals(type)){
			if (VerifyUtil.notNull(ids)) {
				String sql = "/*dialect*/ update T_CAS_PaymentBill set  cfoldbizdate = FBizDate WHERE FID in (" + ids + ") and cfoldbizdate is null";
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

	/**
	 * 其他应付单新增(除去市场投放类型)
	 * @param ctx
	 * @param database
	 * @param map
	 * @return
	 * @throws BOSException
	 */
	private String apOtherNotShichang(Context ctx, String database,Map<String, Object> map)throws BOSException{
		String fid = null;
		try {
			String faccount = "2241.96";
			String actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
			Boolean addOrUpdate = false;
			fid = map.get("ID").toString();
			OtherBillInfo info = null;
			System.out.println("_--------------------------------------"+ map.get("FNUMBER") + "====" + map.get("COMPANY")+ "-----" + map.get("SUPPLIER"));
			if (map.get("COMPANY") == null || map.get("COMPANY").toString().equals("")) {
				System.out.println("_--------------------------------------"+ map.get("FNUMBER"));
				updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
				AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,DateBasetype.OA_OtherBill, info.getNumber(), info.getString("OA_OtherBill"), "单据保存失败,"+ info.getNumber() + "的公司编码为空");// 记录日志
				return "";
			}
			if (map.get("FNUMBER") != null && !map.get("FNUMBER").toString().equals("")) {
				// 考虑caigoushenqingdandanhao 字段是否是唯一的
				if (OtherBillFactory.getLocalInstance(ctx).exists(
						"where caigoushenqingdandanhao ='"
								+ map.get("FNUMBER") + "'")) { 
					updateFSign(ctx, database, "eas_lolkk_bx", 1, map.get("ID").toString());
					return "";
				} else {
					info = new OtherBillInfo();
				}
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
			PaymentTypeInfo paymentTypeInfo = PaymentTypeFactory
					.getLocalInstance(ctx)
					.getPaymentTypeInfo(
							new ObjectUuidPK(
									"2fa35444-5a23-43fb-99ee-6d4fa5f260da6BCA0AB5"));
			info.setPaymentType(paymentTypeInfo); 
			info.setBillType(OtherBillType.OtherPay);
			// 行类型
			// RowTypeInfo rowTypeInfo =
			// RowTypeFactory.getLocalInstance(ctx)
			// .getRowTypeInfo("where number = '"+demandTypeNo+"'");
			// 币别
			CurrencyInfo currency = CurrencyFactory.getLocalInstance(ctx)
					.getCurrencyCollection("where number='BB01'").get(0);

			ObjectUuidPK orgPK = new ObjectUuidPK(map.get("COMPANY")
					.toString());
			CompanyOrgUnitInfo xmcompany = CompanyOrgUnitFactory
					.getLocalInstance(ctx).getCompanyOrgUnitInfo(orgPK);
			info.setCompany(xmcompany);
			System.out.println("------------------所属公司："
					+ xmcompany.getId() + "----" + xmcompany.getName());
 
			
			AdminOrgUnitInfo admin = null;
			if (map.get("DEPT") != null && !"".equals(map.get("DEPT"))) {
				admin = AdminOrgUnitFactory
						.getLocalInstance(ctx)
						.getAdminOrgUnitInfo(
								new ObjectUuidPK(map.get("DEPT").toString()));
				info.setAdminOrgUnit(admin);
				CostCenterOrgUnitInfo CostCenter = CostCenterOrgUnitFactory
						.getLocalInstance(ctx)
						.getCostCenterOrgUnitInfo(
								new ObjectUuidPK(map.get("DEPT").toString()));
				info.setCostCenter(CostCenter);
			}  
			// 银行卡号
			if(map.get("YHZH")!=null && !"".equals(map.get("YHZH").toString())){
				info.put("yinhangzhanghao", map.get("YHZH"));
			}
			if(map.get("APPLYERBANKNUM")!=null && !"".equals(map.get("APPLYERBANKNUM").toString())){
				info.setRecAccountBank(map.get("APPLYERBANKNUM").toString());
			}
			// 开户行
			if(map.get("KHH")!=null && !"".equals(map.get("KHH").toString())){
				info.put("kaihuhang", map.get("KHH"));
			}
			if(map.get("APPLYERBANK")!=null && !"".equals(map.get("APPLYERBANK").toString())){
				info.setRecBank(map.get("APPLYERBANK").toString());
			}
			
			// 库存组织
			// StorageOrgUnitInfo storageorginfo
			// =StorageOrgUnitFactory.getLocalInstance
			// (ctx).getStorageOrgUnitInfo(orgPK);
			// 采购组织
			PurchaseOrgUnitInfo purchaseorginfo = PurchaseOrgUnitFactory.getLocalInstance(ctx).getPurchaseOrgUnitInfo(orgPK);
			info.setPurOrg(purchaseorginfo); 
			
			String personId = getPersonIdByNumber(ctx,map.get("APPLYER").toString());
		    IObjectPK personpk = new ObjectUuidPK(BOSUuid.read(personId));
			PersonInfo person = PersonFactory.getLocalInstance(ctx).getPersonInfo(personpk);
			info.setPerson(person); 
			
			
			// 结算方式 默认:电汇
			SettlementTypeInfo settlementTypeInfo = SettlementTypeFactory
					.getLocalInstance(ctx)
					.getSettlementTypeInfo(
							new ObjectUuidPK(
									"e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
			info.setSettleType(settlementTypeInfo);  
			// 业务日期
			SimpleDateFormat formmat = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			Date bizDate = null;
			try { 
				if (map.get("BIZDATE") != null
						&& !"".equals(map.get("BIZDATE").toString())) {
					bizDate = formmat.parse(map.get("BIZDATE").toString());
				} else {
					bizDate = new Date();
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			info.setBizDate(bizDate); // 业务日期
			info.setBillDate(new Date());// 订单日期
			info.setCurrency(currency);// 币别

			// info.setNumber(map.get("FNUMBER").toString()); 
			// 汇率
			info.setExchangeRate(new BigDecimal("1.00")); 
			OtherBillType otherBillType = null;
			// 其他应付单=201,采购发票=202,采购费用发票=203,应付借贷项调整单=204
			/*
			 * if(map.get("PURCHTYPE").toString().equals("201")){
			 * otherBillType = OtherBillType.OtherPay; }else
			 * if(map.get("PURCHTYPE").toString().equals("202")){
			 * otherBillType = OtherBillType.InvoiceBill; }else
			 * if(map.get("PURCHTYPE").toString().equals("203")){
			 * otherBillType = OtherBillType.ExpenseInvoice; }else
			 * if(map.get("PURCHTYPE").toString().equals("204")){
			 * otherBillType = OtherBillType.DebitAdjust; }
			 */
			otherBillType = OtherBillType.OtherPay;// 先默认 等来了 确认使用什么单据类型
			info.setBillType(otherBillType);

			// BillStatusEnum billStatusEnum = null;
			// info.setBill(billStatusEnum);

			// OA采购申请单单号
			info.put("caigoushenqingdandanhao", map.get("FNUMBER")); 
			// 申请金额
			info.put("OAcaigoushenqingdanjine", map.get("AMOUNT")); 
			String jk = null;
			if (map.get("ISLOAN").toString().equals("0")) {
				jk = "否";
			} else if (map.get("ISLOAN").toString().equals("1")) {
				jk = "是";
			}
			// 是否借款
			info.put("shifoujiekuan", jk);

			String zlf = null;
			if (map.get("ISRENTALFEE").toString().equals("0")) {
				zlf = "否";
			} else if (map.get("ISRENTALFEE").toString().equals("1")) {
				zlf = "是";
			}
			// 是否租赁费
			info.put("shifouzulinfei", zlf);

			String djlx = null;
			// shifouguanlibumen
			String isAdminDept = "0";
			String[] deptArry = { "企划部", "渠道部", "网电部", "网络部", "新媒体部","咨询部", "营销中心" };
			if (map.get("PURCHTYPE").toString().equals("01")||map.get("PURCHTYPE").toString().equals("04") ||map.get("PURCHTYPE").toString().equals("09")) {
				djlx = "费用报销";
				actTypePk = "YW3xsAEJEADgAAWgwKgTB0c4VZA=";
				faccount = "2241.97";
				if (admin != null && admin.getName() != null) {
					if (Arrays.asList(deptArry).contains(admin.getName())) {
						isAdminDept = "2";
					} else {
						isAdminDept = "1";
					}
				}

			} else if (map.get("PURCHTYPE").toString().equals("02")) {
				djlx = "采购付款"; // 采购付款（行政、工服）
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
				if (admin != null && admin.getName() != null) {
					if (Arrays.asList(deptArry).contains(admin.getName())) {
						isAdminDept = "2";
					} else {
						isAdminDept = "1";
					}
				}
			} else if (map.get("PURCHTYPE").toString().equals("05")||map.get("PURCHTYPE").toString().equals("10")) {
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

			// 单据类型
			info.put("yingfudanjuleixing", djlx);
			info.put("shifouguanlibumen", isAdminDept);
			info.put("fapiaohao", "OA0000"); // OA0000
			
			String companytype = AppUnit.getComapnyTypeByNumber(ctx,xmcompany.getNumber());
			if(companytype!=null && !"".equals(companytype))
			info.put("CompanyType",companytype);
			
			// 会计科目
			AccountViewInfo accountInfo = new AccountViewInfo(); 
			accountInfo = AccountViewFactory.getLocalInstance(ctx).getAccountViewInfo(
							"where number = '" + faccount+ "' and companyID ='"+ map.get("COMPANY").toString() + "' ");

			// 往来户类型 YW3xsAEJEADgAAVEwKgTB0c4VZA=
			AsstActTypeInfo actType = AsstActTypeFactory.getLocalInstance(ctx).getAsstActTypeInfo(new ObjectUuidPK(actTypePk));
			info.setAsstActType(actType);
			// AsstActTypeInfo actType = new AsstActTypeInfo(); //往来户类型
			// actType.setId(BOSUuid.read("YW3xsAEJEADgAAVEwKgTB0c4VZA="));
			// info.setAsstActType(actType);

			if (map.get("PURCHTYPE").toString().equals("01")|| map.get("PURCHTYPE").toString().equals("04")||map.get("PURCHTYPE").toString().equals("09")) {
				info.setAsstActID(person.getId().toString());
				info.setAsstActName(person.getName());
				info.setAsstActNumber(person.getNumber());
			} else {
				try {
					SupplierInfo supplierInfo = SupplierFactory
							.getLocalInstance(ctx).getSupplierInfo(
									" where number='"+ map.get("SUPPLIERID") + "'");
					info.setAsstActID(supplierInfo.getId().toString());
					info.setAsstActName(supplierInfo.getName());
					info.setAsstActNumber(supplierInfo.getNumber());
				} catch (Exception e) {
					updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
					AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
							DateBasetype.OA_OtherBill, info.getNumber(),
							info.getString("OA_OtherBill"), "没有该供应商编码");// 记录日志
					return "";
				} 
			}

			VerificateBillTypeEnum billTypeEnum = VerificateBillTypeEnum.OtherPaymentBill;
			info.setSourceBillType(billTypeEnum);
			// 业务类型
			BizTypeInfo bizTypeInfo = BizTypeFactory.getLocalInstance(ctx)
					.getBizTypeInfo("where number = 110");
			info.setBizType(bizTypeInfo);

			// ----------------------
			/*
			 * FSourceBillType 来源单据类型 FBizTypeID 业务类型 T_SCM_BizType
			 * FTotalAmount 金额合计
			 */
			// ----------------------
			String sql = "select parentID,id,payTypecode,payTypeName,Price,qty,amount,Yjk,Ytbk,remark from eas_lolkk_bx_sub where parentid ="
					+ map.get("ID");
			List<Map<String, Object>> list1 = EAISynTemplate.query(ctx,
					database, sql);
			BigDecimal totalAmount = new BigDecimal(0);
			BigDecimal totalyjk = new BigDecimal(0);
			BigDecimal totalytbk = new BigDecimal(0);
			if (list1 != null && list1.size() > 0) {
				for (Map<String, Object> map1 : list1) {
					OtherBillentryInfo entryInfo = new OtherBillentryInfo();
					// 费用项目
					if (map1.get("PAYTYPECODE") != null && !"".equals(map1.get("PAYTYPECODE").toString())) {
						ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx).getExpenseTypeInfo("where number ='"+ map1.get("PAYTYPECODE").toString() + "'");
						entryInfo.setExpenseItem(typeinfo);
					} else {
						ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx).getExpenseTypeInfo("where number ='CL01'");
						entryInfo.setExpenseItem(typeinfo);
					}

					BigDecimal qty = new BigDecimal(map1.get("QTY").toString());
					BigDecimal price = new BigDecimal(map1.get("PRICE").toString());
					BigDecimal amount = new BigDecimal(map1.get("AMOUNT").toString());
					// if price < 0
					if (price.compareTo(BigDecimal.ZERO) < 0) {
						price = price.negate();
						qty = qty.negate();
					} 
					entryInfo.setPrice(price);// 单价
					entryInfo.setTaxPrice(price);// 含税单价
					entryInfo.setActualPrice(price);// 实际含税单价
					entryInfo.setRealPrice(price);// 实际单价
					entryInfo.setQuantity(qty); // 数量
					entryInfo.setBaseQty(BigDecimal.ZERO); // 基本计量单位数量
					entryInfo.setDiscountRate(BigDecimal.ZERO); // 单位折扣
					entryInfo.setDiscountAmount(BigDecimal.ZERO); // 折扣额
					entryInfo.setDiscountAmountLocal(BigDecimal.ZERO); // 折扣额本位币
					entryInfo.setHisUnVerifyAmount(BigDecimal.ZERO); // 历史未销金额
					entryInfo.setHisUnVerifyAmountLocal(BigDecimal.ZERO); // 历史未销金额本位币
					entryInfo.setAssistQty(BigDecimal.ZERO); // 辅助数量
					entryInfo.setWittenOffBaseQty(BigDecimal.ZERO); // 已核销本基本数量
					entryInfo.setLocalWrittenOffAmount(BigDecimal.ZERO); // 未核销本位币金额
					entryInfo.setUnwriteOffBaseQty(BigDecimal.ZERO); // 未核销基本数量
					entryInfo.setVerifyQty(BigDecimal.ZERO);
					entryInfo.setLockVerifyQty(BigDecimal.ZERO);
					entryInfo.setLocalUnwriteOffAmount(amount); // 未核销本位币金额
					entryInfo.setAmount(amount);// 应付金额
					entryInfo.setAmountLocal(amount); // 应付金额本位币
					entryInfo.setTaxAmount(BigDecimal.ZERO);
					entryInfo.setTaxAmountLocal(BigDecimal.ZERO);
					entryInfo.setTaxRate(BigDecimal.ZERO);
					entryInfo.setUnVerifyAmount(amount);// 未结算金额
					entryInfo.setUnVerifyAmountLocal(amount);// 未结算金额本位币
					entryInfo.setLockUnVerifyAmt(amount);// 未锁定金额
					entryInfo.setLockUnVerifyAmtLocal(amount);// 未锁定金额本位币
					// ApportionAmtLocal
					entryInfo.setApportionAmount(BigDecimal.ZERO);
					entryInfo.setApportionAmtLocal(BigDecimal.ZERO);
					entryInfo.setUnApportionAmount(amount);
					entryInfo.setRecievePayAmount(amount);
					entryInfo.setRecievePayAmountLocal(amount);
					entryInfo.setCompany(map.get("COMPANY").toString());
					entryInfo.setAccount(accountInfo);
					
					if(map1.get("REMARK")!=null){
						entryInfo.setRemark(map1.get("REMARK").toString());
					}			
					if (map1.get("YJK") != null && !"".equals(map1.get("YJK").toString()))
						totalyjk = totalyjk.add((BigDecimal) map1.get("YJK"));
					if (map1.get("YTBK") != null && !"".equals(map1.get("YTBK").toString()))
						totalytbk = totalytbk.add((BigDecimal) map1.get("YTBK")); 
					entryInfo.setParent(info); 
					entryInfo.setUnwriteOffBaseQty(qty);
					entryInfo.put("pinpai", map.get("BRAND")); // 找不到 没有传递值
					entryInfo.put("huohao", map.get("ATRNO")); // 找不到 没有传递值 
					totalAmount = totalAmount.add(amount);
					info.getEntries().addObject(entryInfo);
				} 
			} else {
				System.out.println("entrty is empty _--------------------------------------"+ map.get("FNUMBER"));
				updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
				AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
						DateBasetype.OA_OtherBill, info.getNumber(), info.getString("OA_OtherBill"), "单据没有分录");// 记录日志
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
			OtherBillFactory.getLocalInstance(ctx).save(info);
			updateFSign(ctx, database, "eas_lolkk_bx", 1, map.get("ID").toString()); 
			try {
				System.out.println("------------------info所属公司1111："
						+ info.getCompany().getId() + "----" + info.getCompany().getName());
				info.setBillStatus(BillStatusEnum.SAVE);
				OtherBillFactory.getLocalInstance(ctx).submit(info);
				System.out.println("------------------info所属公司2222："
						+ info.getCompany().getId() + "----" + info.getCompany().getName());

				OtherBillFactory.getLocalInstance(ctx).audit(new ObjectUuidPK(info.getId().toString()));
				
				if (addOrUpdate) {// 修改
					AppUnit.insertLog(ctx, DateBaseProcessType.Update,
							DateBasetype.OA_OtherBill, info.getNumber(), info
									.getString("OA_OtherBill"), "单据修改成功");// 记录日志
				} else {
					AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
							DateBasetype.OA_OtherBill, info.getNumber(), info
									.getString("OA_OtherBill"), "单据审核成功");// 记录日志
				}
			} catch (Exception e2) {
				//System.out.println("保存成功，提交或者审核失败,"+e2.getMessage());
				logger.error(e2.getMessage());
				AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
						DateBasetype.OA_OtherBill, info.getNumber(), info
								.getString("OA_OtherBill"), "单据保存成功，提交审核失败。");// 记录日志
			} 
			
			
			
		} catch (EASBizException e) {
			e.printStackTrace();
			AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
					DateBasetype.OA_OtherBill, fid+"单据保存失败", e.getMessage());// 记录日志
			if (fid != null && !fid.equals("")) {
				updateFSign(ctx, database, "eas_lolkk_bx", 2, fid);
			}
			String msg = "运行失败，异常是：" + e.toString();
			return msg; 
		} 
		return null;
	}
	
	/**
	 * 其他应付单新增(市场投放类型)，根据分录插入
	 * @param ctx
	 * @param database
	 * @param map
	 * @return
	 * @throws BOSException
	 */
	private String apOtherIsShichang(Context ctx, String database,Map<String, Object> map)throws BOSException{
		String fid = null;
		try {
			String faccount = "2241.96";
			String actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
			fid = map.get("ID").toString();
			OtherBillInfo info = new OtherBillInfo();
			
			//----------------先是分录
			String sql = "select parentID,fnumber,company,Dept,id,payTypecode,payTypeName,Price,qty,amount,Yjk,Ytbk,remark from eas_lolkk_bx_sub where parentid ="
				+ map.get("ID");
			List<Map<String, Object>> list1 = EAISynTemplate.query(ctx,database, sql);
			
			if (list1 != null && list1.size() > 0) {
				for (Map<String, Object> map1 : list1) {
					info = new OtherBillInfo();
					BigDecimal totalyjk = new BigDecimal(0);
					BigDecimal totalytbk = new BigDecimal(0);
					Boolean addOrUpdate = false;
					if (map1.get("COMPANY") == null || map1.get("COMPANY").toString().equals("")) {
						System.out.println("_--------------------------------------"+ map.get("FNUMBER"));
						updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
						AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,DateBasetype.OA_OtherBill, info.getNumber(), info.getString("OA_OtherBill"), "单据保存失败,"+ info.getNumber() + "的分录上的公司编码为空");// 记录日志
						return "";
					}
					if (map1.get("DEPT") == null || map1.get("DEPT").toString().equals("")) {
						System.out.println("_--------------------------------------"+ map.get("FNUMBER"));
						updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
						AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,DateBasetype.OA_OtherBill, info.getNumber(), info.getString("OA_OtherBill"), "单据保存失败,"+ info.getNumber() + "的分录上的部门编码为空");// 记录日志
						return "";
					}
					if (map1.get("FNUMBER") == null || map1.get("FNUMBER").toString().equals("")) {
						System.out.println("_--------------------------------------"+ map.get("FNUMBER"));
						updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
						AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,DateBasetype.OA_OtherBill, info.getNumber(), info.getString("OA_OtherBill"), "单据保存失败,"+ info.getNumber() + "的分录上的编码为空");// 记录日志
						return "";
					}
					// 会计科目
					AccountViewInfo accountInfo = new AccountViewInfo();
					accountInfo = AccountViewFactory.getLocalInstance(ctx).getAccountViewInfo(
									"where number = '" + faccount+ "' and companyID ='"+ map1.get("COMPANY").toString() + "' ");
					
					OtherBillentryInfo entryInfo = new OtherBillentryInfo();
					// 费用项目
					if (map1.get("PAYTYPECODE") != null && !"".equals(map1.get("PAYTYPECODE").toString())) {
						ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx)
							.getExpenseTypeInfo("where number ='"+ map1.get("PAYTYPECODE").toString() + "'");
						entryInfo.setExpenseItem(typeinfo);
					} else {
						ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx).getExpenseTypeInfo("where number ='CL01'");
						entryInfo.setExpenseItem(typeinfo);
					}
	
					BigDecimal qty = new BigDecimal(map1.get("QTY").toString());
					BigDecimal price = new BigDecimal(map1.get("PRICE").toString());
					BigDecimal amount = new BigDecimal(map1.get("AMOUNT").toString());
					// if price < 0
					if (price.compareTo(BigDecimal.ZERO) < 0) {
						price = price.negate();
						qty = qty.negate();
					}
	
					entryInfo.setPrice(price);// 单价
					entryInfo.setTaxPrice(price);// 含税单价
					entryInfo.setActualPrice(price);// 实际含税单价
					entryInfo.setRealPrice(price);// 实际单价
					entryInfo.setQuantity(qty); // 数量
					entryInfo.setBaseQty(BigDecimal.ZERO); // 基本计量单位数量
					entryInfo.setDiscountRate(BigDecimal.ZERO); // 单位折扣
					entryInfo.setDiscountAmount(BigDecimal.ZERO); // 折扣额
					entryInfo.setDiscountAmountLocal(BigDecimal.ZERO); // 折扣额本位币
					entryInfo.setHisUnVerifyAmount(BigDecimal.ZERO); // 历史未销金额
					entryInfo.setHisUnVerifyAmountLocal(BigDecimal.ZERO); // 历史未销金额本位币
					entryInfo.setAssistQty(BigDecimal.ZERO); // 辅助数量
					entryInfo.setWittenOffBaseQty(BigDecimal.ZERO); // 已核销本基本数量
					entryInfo.setLocalWrittenOffAmount(BigDecimal.ZERO); // 未核销本位币金额
					entryInfo.setUnwriteOffBaseQty(BigDecimal.ZERO); // 未核销基本数量
					entryInfo.setVerifyQty(BigDecimal.ZERO);
					entryInfo.setLockVerifyQty(BigDecimal.ZERO);
					entryInfo.setLocalUnwriteOffAmount(amount); // 未核销本位币金额
					entryInfo.setAmount(amount);// 应付金额
					entryInfo.setAmountLocal(amount); // 应付金额本位币
					entryInfo.setTaxAmount(BigDecimal.ZERO);
					entryInfo.setTaxAmountLocal(BigDecimal.ZERO);
					entryInfo.setTaxRate(BigDecimal.ZERO);
					entryInfo.setUnVerifyAmount(amount);// 未结算金额
					entryInfo.setUnVerifyAmountLocal(amount);// 未结算金额本位币
					entryInfo.setLockUnVerifyAmt(amount);// 未锁定金额
					entryInfo.setLockUnVerifyAmtLocal(amount);// 未锁定金额本位币
					// ApportionAmtLocal
					entryInfo.setApportionAmount(BigDecimal.ZERO);
					entryInfo.setApportionAmtLocal(BigDecimal.ZERO);
					entryInfo.setUnApportionAmount(amount);
					entryInfo.setRecievePayAmount(amount);
					entryInfo.setRecievePayAmountLocal(amount);
					entryInfo.setCompany(map1.get("COMPANY").toString());
					entryInfo.setAccount(accountInfo);
					if (map1.get("YJK") != null && !"".equals(map1.get("YJK")))
						totalyjk =  ((BigDecimal) map1 .get("YJK"));
					if (map1.get("YTBK") != null && !"".equals(map1.get("YTBK")))
						totalytbk = ((BigDecimal) map1 .get("YTBK"));
					/*
					 * OtherBillInfo otherBillInfo = new OtherBillInfo();
					 * otherBillInfo
					 * .setId(BOSUuid.read(map.get("ID").toString()));
					 */
					
	
					entryInfo.setUnwriteOffBaseQty(qty);
					entryInfo.put("pinpai", map.get("BRAND")); // 找不到 没有传递值
					entryInfo.put("huohao", map.get("ATRNO")); // 找不到 没有传递值
	
					if(map1.get("REMARK")!=null){
						entryInfo.setRemark(map1.get("REMARK").toString());
					}				 
					if (map.get("FNUMBER") != null && !map.get("FNUMBER").toString().equals("")) {
						// 考虑caigoushenqingdandanhao 字段是否是唯一的
						if (OtherBillFactory.getLocalInstance(ctx).exists("where caigoushenqingdandanhao ='"+ map.get("FNUMBER")+"_"+map1.get("FNUMBER")+ "'")) {
							//updateFSign(ctx, database, "eas_lolkk_bx", 1, map.get("ID").toString());
							continue;
						} else {
							info = new OtherBillInfo();
						}
					} else {
						info = new OtherBillInfo();
					}
					entryInfo.setParent(info);
					info.getEntries().addObject(entryInfo);
					
					info.setIsReversed(false);
					info.setIsReverseBill(false);
					info.setIsTransBill(false);
					info.setIsAllowanceBill(false);
					info.setIsImportBill(false);
					info.setIsExchanged(false);
					info.setIsInitializeBill(false);

					PaymentTypeInfo paymentTypeInfo = PaymentTypeFactory.getLocalInstance(ctx)
							.getPaymentTypeInfo(new ObjectUuidPK("2fa35444-5a23-43fb-99ee-6d4fa5f260da6BCA0AB5"));
					info.setPaymentType(paymentTypeInfo);
					 
					info.setBillType(OtherBillType.OtherPay);

					ObjectUuidPK orgPK = new ObjectUuidPK(map1.get("COMPANY").toString());
					CompanyOrgUnitInfo company = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo(orgPK);
					info.setCompany(company);
					System.out.println("------------------所属公司："+ company.getId() + "----" + company.getName());
 
					
					AdminOrgUnitInfo admin = null;
					if (map1.get("DEPT") != null && !"".equals(map1.get("DEPT"))) {
						admin = AdminOrgUnitFactory.getLocalInstance(ctx)
								.getAdminOrgUnitInfo(new ObjectUuidPK(map1.get("DEPT").toString()));
						info.setAdminOrgUnit(admin);
						CostCenterOrgUnitInfo CostCenter = CostCenterOrgUnitFactory.getLocalInstance(ctx)
								.getCostCenterOrgUnitInfo(new ObjectUuidPK(map1.get("DEPT").toString()));
						info.setCostCenter(CostCenter);
					}

					
					// 银行卡号
					if(map.get("YHZH")!=null && !"".equals(map.get("YHZH").toString())){
						info.put("yinhangzhanghao", map.get("YHZH"));
					}
					if(map.get("APPLYERBANKNUM")!=null && !"".equals(map.get("APPLYERBANKNUM").toString())){
						info.setRecAccountBank(map.get("APPLYERBANKNUM").toString());
					}
					// 开户行
					if(map.get("KHH")!=null && !"".equals(map.get("KHH").toString())){
						info.put("kaihuhang", map.get("KHH"));
					}
					if(map.get("APPLYERBANK")!=null && !"".equals(map.get("APPLYERBANK").toString())){
						info.setRecBank(map.get("APPLYERBANK").toString());
					}

					// 采购组织
					PurchaseOrgUnitInfo purchaseorginfo = PurchaseOrgUnitFactory.getLocalInstance(ctx).getPurchaseOrgUnitInfo(orgPK);
					info.setPurOrg(purchaseorginfo);
//					
//					PersonInfo person = PersonFactory.getLocalInstance(ctx)
//							.getPersonCollection( "where number='"+ map.get("APPLYER").toString() + "'").get(0);
//					info.setPerson(person);
					
					String personId = getPersonIdByNumber(ctx,map.get("APPLYER").toString());
				    IObjectPK personpk = new ObjectUuidPK(BOSUuid.read(personId));
					PersonInfo person = PersonFactory.getLocalInstance(ctx).getPersonInfo(personpk);
					info.setPerson(person); 

					// 结算方式 默认:电汇
					SettlementTypeInfo settlementTypeInfo = SettlementTypeFactory.getLocalInstance(ctx)
							.getSettlementTypeInfo(new ObjectUuidPK("e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
					info.setSettleType(settlementTypeInfo);
					 

					// 业务日期
					SimpleDateFormat formmat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					Date bizDate = null;
					try {
						if (map.get("BIZDATE") != null  && !"".equals(map.get("BIZDATE").toString())) {
							bizDate = formmat.parse(map.get("BIZDATE").toString());
						} else {
							bizDate = new Date();
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}

					info.setBizDate(bizDate); // 业务日期
					info.setBillDate(new Date());// 订单日期
					// 币别
					CurrencyInfo currency = CurrencyFactory.getLocalInstance(ctx).getCurrencyCollection("where number='BB01'").get(0);
					info.setCurrency(currency);// 币别

					// 汇率
					info.setExchangeRate(new BigDecimal("1.00"));

					OtherBillType otherBillType = null;
					// 其他应付单=201,采购发票=202,采购费用发票=203,应付借贷项调整单=204
					 
					otherBillType = OtherBillType.OtherPay;// 先默认 等来了 确认使用什么单据类型
					info.setBillType(otherBillType);


					// OA采购申请单单号
					info.put("caigoushenqingdandanhao", map.get("FNUMBER")+"_"+map1.get("FNUMBER"));

					// 申请金额
					info.put("OAcaigoushenqingdanjine", map1.get("AMOUNT"));

					String companType = AppUnit.getComapnyTypeByNumber(ctx,company.getNumber());
					if(companType!=null && !"".equals(companType))
					info.put("CompanyType",companType); 
					
					
					String jk = null;
					if (map.get("ISLOAN").toString().equals("0")) {
						jk = "否";
					} else if (map.get("ISLOAN").toString().equals("1")) {
						jk = "是";
					}
					// 是否借款
					info.put("shifoujiekuan", jk);

					String zlf = null;
					if (map.get("ISRENTALFEE").toString().equals("0")) {
						zlf = "否";
					} else if (map.get("ISRENTALFEE").toString().equals("1")) {
						zlf = "是";
					}
					// 是否租赁费
					info.put("shifouzulinfei", zlf);

					String djlx = null;
					String isAdminDept = "0";
					String[] deptArry = { "企划部", "渠道部", "网电部", "网络部", "新媒体部","咨询部", "营销中心" };
					if (map.get("PURCHTYPE").toString().equals("01")) {
						djlx = "费用报销";
						actTypePk = "YW3xsAEJEADgAAWgwKgTB0c4VZA=";
						faccount = "2241.97";
						if (admin != null && admin.getName() != null) {
							if (Arrays.asList(deptArry).contains(admin.getName())) {
								isAdminDept = "2";
							} else {
								isAdminDept = "1";
							}
						}

					} else if (map.get("PURCHTYPE").toString().equals("02")) {
						djlx = "采购付款"; // 采购付款（行政、工服）
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
						if (admin != null && admin.getName() != null) {
							if (Arrays.asList(deptArry).contains(admin.getName())) {
								isAdminDept = "2";
							} else {
								isAdminDept = "1";
							}
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

					// 单据类型
					info.put("yingfudanjuleixing", djlx);
					info.put("shifouguanlibumen", isAdminDept);
					info.put("fapiaohao", "OA0000"); // OA0000

					  
					// 往来户类型 YW3xsAEJEADgAAVEwKgTB0c4VZA=
					AsstActTypeInfo actType = AsstActTypeFactory.getLocalInstance(ctx).getAsstActTypeInfo(new ObjectUuidPK(actTypePk));
					info.setAsstActType(actType);
					 

					if (map.get("PURCHTYPE").toString().equals("01") || map.get("PURCHTYPE").toString().equals("04")) {
						info.setAsstActID(person.getId().toString());
						info.setAsstActName(person.getName());
						info.setAsstActNumber(person.getNumber());
					} else {
						try {
							SupplierInfo supplierInfo = SupplierFactory.getLocalInstance(ctx).getSupplierInfo( " where number='" + map.get("SUPPLIERID") + "'");
							info.setAsstActID(supplierInfo.getId().toString());
							info.setAsstActName(supplierInfo.getName());
							info.setAsstActNumber(supplierInfo.getNumber());
						} catch (Exception e) {
							updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
							AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,DateBasetype.OA_OtherBill, info.getNumber(),
									info.getString("OA_OtherBill"), "分录上的"+map.get("NUMBER")+"没有该供应商编码");// 记录日志
							continue;
						}
					}

					VerificateBillTypeEnum billTypeEnum = VerificateBillTypeEnum.OtherPaymentBill;
					info.setSourceBillType(billTypeEnum);
					// 业务类型
					BizTypeInfo bizTypeInfo = BizTypeFactory.getLocalInstance(ctx) .getBizTypeInfo("where number = 110");
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

					OtherBillFactory.getLocalInstance(ctx).save(info);
					
					try {
						OtherBillFactory.getLocalInstance(ctx).submit(info);

						OtherBillFactory.getLocalInstance(ctx).audit(new ObjectUuidPK(info.getId().toString()));
						AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
								DateBasetype.OA_OtherBill, info.getNumber(), 
								info.getString("OA_OtherBill"), "采购申请单单号为"+map.get("FNUMBER")+"_"+map1.get("FNUMBER")+"单据审核成功");// 记录日志
					} catch (Exception e2) {
						//System.out.println("保存成功，提交或者审核失败，"+e2.getMessage());
						logger.error(e2.getMessage());
						AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
								DateBasetype.OA_OtherBill, info.getNumber(), 
								info.getString("OA_OtherBill"), "采购申请单单号为"+map.get("FNUMBER")+"_"+map1.get("FNUMBER")+"单据保存成功,提交审核失败。");// 记录日志
					}
					/*
					if (addOrUpdate) {// 修改
						AppUnit.insertLog(ctx, DateBaseProcessType.Update,
								DateBasetype.OA_OtherBill, info.getNumber(), 
								info.getString("OA_OtherBill"), "单据修改成功");// 记录日志
					} else {
						AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
								DateBasetype.OA_OtherBill, info.getNumber(), 
								info.getString("OA_OtherBill"), "单据保存成功");// 记录日志
					}*/
				}
				
			} else {
				System.out.println("entrty is empty _--------------------------------------"+ map.get("FNUMBER"));
				updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
				AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,DateBasetype.OA_OtherBill, 
						map.get("FNUMBER").toString(), info.getString("OA_OtherBill"), "单据没有分录");// 记录日志
				return "";
			}
			//--------------------分录结束
			//分录全部遍历完毕  修改表头状态
			updateFSign(ctx, database, "eas_lolkk_bx", 1, map.get("ID").toString());
			
		} catch (EASBizException e) {
			e.printStackTrace();
			AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
					DateBasetype.OA_OtherBill, fid+"单据保存失败", e.getMessage());// 记录日志
			if (fid != null && !fid.equals("")) {
				updateFSign(ctx, database, "eas_lolkk_bx", 2, fid);
			}
			String msg = "运行失败，异常是：" + e.toString();
			return msg;
			
		}
		
		return null;
	}

	
	
	/**
	 * 将eas_lolkk_bx表中申请人不存在的数据修改为状态2
	 * @param ctx
	 * @param database
	 * @throws BOSException
	 */
	public void updateNoPeople(Context ctx, String database) throws BOSException {
		String updateSql = "UPDATE  eas_lolkk_bx  set eassign = 2 , EASTIME = TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS'),EASLOG='职员不存在' "
				+" where ID in (select bx.id from eas_lolkk_bx bx left JOIN EAS_PERSON_MIDTABLE  person on person.FNUMBER = bx.APPLYER where bx.eassign = 0 and person.FNUMBER is null )";
		System.out.print("--------------" + updateSql);
		EAISynTemplate.execute(ctx, database, updateSql);
		
		updateSql = "update EAS_LOLKK_bx set EASSIGN = -1, EASTIME = TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS'),EASLOG='PAYTYPECODE为空' where id in (select DISTINCT PARENTID from EAS_LOLKK_BX_SUB where PAYTYPECODE is null ) and EASSIGN = 0";
		System.out.print("--------------" + updateSql);
		EAISynTemplate.execute(ctx, database, updateSql);
		
		updateSql = "update EAS_LOLKK_bx set EASSIGN = -2, EASTIME = TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS'),EASLOG='PAYTYPECODE不存在' where id in (select DISTINCT PARENTID from EAS_LOLKK_BX_SUB where PAYTYPECODE not in (select FNUMBER from EAS_PAYTYPE_OA_MIDTABLE)) and EASSIGN = 0";
		System.out.print("--------------" + updateSql);
		EAISynTemplate.execute(ctx, database, updateSql);
		
	}

	@Override
	public String updateMidPayStatus(Context ctx) throws BOSException{
		// TODO Auto-generated method stub
		String noPayBillSql = "select a.id,a.oanumber, a.fbillid ,a.danjuType FROM eas_paymentbillstatus  a where a.status = 0 "; 
		IRowSet noPayBill = DbUtil.executeQuery(ctx, noPayBillSql);
		String  oanumber = null;
		try {
			while (noPayBill.next()) {
				oanumber = noPayBill.getString("OANUMBER");
				String fbillid = noPayBill.getString("FBILLID");
				String danjuType =noPayBill.getString("DANJUTYPE"); 
				String id = noPayBill.getString("id");
				String number = oanumber;
				if(VerifyUtil.notNull(oanumber)){
					if(oanumber!=null &&!"".equals(oanumber) &&!"-1".equals(oanumber) ){
						String sql = null;
						if( danjuType.equals("市场投放") ){
							if(oanumber.indexOf("_")!= -1 && oanumber.split("_").length >1){
								// 修改分录上的付款状态
								oanumber = oanumber.split("_")[1];
								sql="update eas_lolkk_bx_sub set Paystate = 1,Paystatetime =TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') where fnumber ='"+oanumber+"'";
							}else{
								sql="update eas_lolkk_bx set Paystate = 1,Paystatetime =TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') where fnumber ='"+oanumber+"'"; 
							}
						}else{
							sql="update eas_lolkk_bx set Paystate = 1,Paystatetime =TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') where fnumber ='"+oanumber+"'"; 
							
						}
						EAISynTemplate.execute(ctx, "04",sql );
					}
					
					if(fbillid!=null && !"".equals(fbillid)&& !"-1".equals(fbillid)){
						String sql3="update eas_lolkk_fk set Paystate = 1 where id ='"+fbillid+"'"; 
						
						EAISynTemplate.execute(ctx, "03",sql3 ); 
					}
					
					//String updateStatusSql = " update eas_paymentbillstatus set  STATUS =  1  where oanumber='"+number+"'";
					String updateStatusSql = " update eas_paymentbillstatus set  STATUS =  1  where id="+id;
					DbUtil.execute(ctx, updateStatusSql);
					
					AppUnit.insertLog(ctx, DateBaseProcessType.Update,DateBasetype.OA_OtherBill,oanumber,oanumber,"修改付款状态成功");
				}
				
			}
			
		} catch (BOSException e) {
			AppUnit.insertLog(ctx, DateBaseProcessType.Update,DateBasetype.OA_OtherBill,oanumber,oanumber,"修改付款状态异常");
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected boolean _IsExistDownstreamBill(Context ctx, String id)
			throws BOSException {
		boolean flag = true ;
 		try {
 			flag = AppUnit.isExistDownstreamBill(ctx, id);
		} catch (EASBizException e) {
 			e.printStackTrace();
 			flag = true ;
		}
		return flag;
	}
	/**
	 * 京东惠采 -- 采购申请单
	 */
	@Override
	protected void _PurvspJDFromOA(Context ctx, String database)
			throws BOSException {
		VSPJDSupport.savePurRequest(ctx, database);
	}

	/*
	 *  京东惠采 -- 采购收货单
	 * @see 
	 */
	@Override
	protected void _ReceConfirmVSPJD(Context ctx, String database)
			throws BOSException {
		VSPJDSupport.savePurReceivalBill(ctx, database);
	}  
	private static java.sql.Timestamp string2Time(String dateString) 
	  throws java.text.ParseException { 
	   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);//设定格式 
	   dateFormat.setLenient(false); 
	  java.util.Date timeDate = dateFormat.parse(dateString);//util类型 
	  java.sql.Timestamp dateTime = new java.sql.Timestamp(timeDate.getTime());//Timestamp类型,timeDate.getTime()返回一个long型 
	  return dateTime; 
	} 
	
	/** 
	  *method 将字符串类型的日期转换为一个Date（java.sql.Date） 
	   dateString 需要转换为Date的字符串 
	   dataTime Date 
	  */ 
	private static java.sql.Date string2Date(String dateString) 
	  throws java.lang.Exception { 
	  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH); 
	  dateFormat.setLenient(false); 
	  java.util.Date timeDate = dateFormat.parse(dateString);//util类型 
	  java.sql.Date dateTime = new java.sql.Date(timeDate.getTime());//sql类型 
	  return dateTime; 
	} 
	
    private static String getPersonIdByNumber(Context ctx,String number){
    	String pid = "";
    	if(number!=null && !"".equals(number)){
    		String sql ="select FID from t_bd_person where fnumber ='"+number+"'";
    	 	try {
    			IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
    			 if(rs!=null && rs.size() > 0){ 
    				 while(rs.next()){
    					if( rs.getObject("FID")!=null && !"".equals(rs.getObject("FID").toString())){
    						pid = rs.getObject("FID").toString() ;
    					}
    			     }
    			 } 
    		} catch (BOSException e) {
     			e.printStackTrace();
    		} catch (SQLException e) {
     			e.printStackTrace();
    		} 
    	} 
    	return pid;
    }
    
    private static String materialUnitId(Context ctx,String orgId,String materialId){
    	String unitId = "";
    	if(orgId != null && !"".equals(orgId) && materialId != null && !"".equals(materialId)){
    		String sql ="select FUnitID from T_BD_MaterialPurchasing where FMaterialID ='"+materialId+"' and FOrgUnit ='"+orgId+"'";
    	 	try {
    			IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
    			 if(rs!=null && rs.size() > 0){ 
    				 while(rs.next()){
    					if( rs.getObject("FUnitID")!=null && !"".equals(rs.getObject("FUnitID").toString())){
    						unitId = rs.getObject("FUnitID").toString() ;
    					}
    			     }
    			 } 
    		} catch (BOSException e) {
     			e.printStackTrace();
    		} catch (SQLException e) {
     			e.printStackTrace();
    		} 
    	}
    	return unitId ;
    }
    
    private static BigDecimal getmaterialMultiple(Context ctx,String materialId,String unitId){
    	BigDecimal multiple = new BigDecimal(1);
    	if(materialId != null && !"".equals(materialId) && unitId != null && !"".equals(unitId)){
    		// 获取基本计量单位
    		boolean flag = false ;
    		String sql="select FBaseUnit from T_BD_Material where FID = '"+materialId+"'";
    	 	try {
    			IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
    			 if(rs!=null && rs.size() > 0){ 
    				 while(rs.next()){
    					if( rs.getObject("FBaseUnit") != null && !"".equals(rs.getObject("FBaseUnit").toString())){
    						 if(!unitId.equals(rs.getObject("FBaseUnit").toString())) flag = true ;
    					}
    			     }
    			 } 
    		} catch (BOSException e) {
     			e.printStackTrace();
    		} catch (SQLException e) {
     			e.printStackTrace();
    		}
    		
    		if(flag){
    			sql = "select FBaseConvsRate from t_bd_multimeasureunit where fmaterialid ='"+materialId+"'and FMeasureUnitID='"+unitId+"'";
        	 	try {
        			IRowSet  rs = com.kingdee.eas.custom.util.DBUtil.executeQuery(ctx,sql);
        			 if(rs!=null && rs.size() > 0){ 
        				 while(rs.next()){
        					if( rs.getObject("FBaseConvsRate")!=null && !"".equals(rs.getObject("FBaseConvsRate").toString())){
        						multiple = new BigDecimal(rs.getObject("FBaseConvsRate").toString());
        					}
        			     }
        			 } 
        		} catch (BOSException e) {
         			e.printStackTrace();
        		} catch (SQLException e) {
         			e.printStackTrace();
        		} 
    		}
    	
    	}
    	return multiple ;
    }
    
}
