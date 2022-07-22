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
	 * Ӧ���� (����Ӧ����) 1
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
		
		//��eas_lolkk_bx���������˲����ڵ������޸�Ϊ״̬2
		updateNoPeople(ctx,database);
		//��eas_lolkk_bxd�ķ�¼���з������Ͳ����ڵ������޸�Ϊ״̬2
		updateNoExpenseType(ctx,database);
				sql = " select bx.id,bx.fnumber,bx.bizDate,bx.isLoan,bx.payType,bx.isrentalfee,bx.company,bx.Dept,bx.supplierid,bx.Yhzh,bx.Khh,bx.applyer, "
				+ " bx.Applyerbank,bx.Applyerbanknum,bx.Agency,bx.Amount,bx.Jsfs,bx.purchType,bx.purchModel,bx.Paystate,bx.Paystatetime "
				+ " from eas_lolkk_bx bx "+ " where bx.eassign = 0 and bx.PURCHTYPE != '08' and ( ispre is null  or ispre = 0 )";

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<Map<String, Object>> list = EAISynTemplate.query(ctx, database,sql.toString());
		System.out.println("--------------------------" + list.size());
		for (Map<String, Object> map : list) {
			// �жϵ��������Ƿ�Ϊ03  ���в�ͬ�Ĵ���
			if(map.get("PURCHTYPE")!=null && !map.get("PURCHTYPE").toString().equals("03")){
				apOtherNotShichang( ctx,  database, map);
			}else if(map.get("PURCHTYPE")!=null && map.get("PURCHTYPE").toString().equals("03")){
				apOtherIsShichang( ctx,  database, map);
			}

		}
		
		return super._ApOtherFormOA(ctx, database);
	}

	private void updateNoExpenseType(Context ctx, String database) {
		String updateSql = "UPDATE  eas_lolkk_bx  set eassign = 2 , EASTIME = TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS'),EASLOG='��¼��ĳ���������ͽ���' "
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
	 * �������뵥
	 */
	@Override
	// �������뵥�������м��bycb
	protected String _PayApplyToOA(Context ctx, String database, String billId)
			throws BOSException {
		// TODO Auto-generated method stub
		// �����˴������ݵ�oa�м��
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

				String fnumber = payRequestBillInfo.getNumber();// ���ݱ��
				// String oaNumber =
				// payRequestBillInfo.getString("oacaigoushenqingdandanhao"
				// );//oa�ɹ����뵥����
				// String oaMoney =
				// payRequestBillInfo.getString("caigoushenqingdanjine"
				// );//oa�ɹ����뵥����
				String bizDate = sdf.format(payRequestBillInfo.getBizDate());// ҵ������

				String Formtype = "6";
				// String Formtype = pbInfo.getFormtype();//�ɹ�����
				// ?String Suppliernum = pbInfo.getPayeeNumber();//��Ӧ�̱���
				// ?String Suppliername = pbInfo.getPayeeName();//��Ӧ������
				// ?String Supplierbank = pbInfo.getPayeeBank();//������
				// ?String Supplierbanknum =
				// pbInfo.getPayeeAccountBank();//�����տ��˺�
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
//						//�������Ϊ���ӹ������� Formtype =6    VMI Formtype = 8
//						if(prex!=null && !"".equals(prex)){
//							if(!"JGF".equals(prex)) Formtype = "8";
//						}else{
//							Formtype = "6";
//						}
//					}
					
					
					Suppliernum = entryOne.getAsstActNumber();// ��Ӧ�̱���
					Suppliername = entryOne.getAsstActName();// ��Ӧ������
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
				// String Usedate = pbInfo.getUsedate();//�ÿ�����
				UserInfo uInfo = UserFactory.getLocalInstance(ctx).getUserInfo(
						new ObjectUuidPK(payRequestBillInfo.getCreator()
								.getId()));

				CompanyOrgUnitInfo couInfo = CompanyOrgUnitFactory
						.getLocalInstance(ctx).getCompanyOrgUnitInfo(
								new ObjectUuidPK(payRequestBillInfo
										.getCompany().getId()));
				String Companynum = couInfo.getId().toString();// ������֯id
				String Company = couInfo.getName();// ������֯����
				String Gzamount = payRequestBillInfo.getRequestAmount()
						.toString();// Ԥ����ϼ� ������
				String requestAmount = payRequestBillInfo.getAuditAmount().toString();// Ӧ����� �������
 				String invoiceNumber = payRequestBillInfo.get("fapiaohao") !=null ? payRequestBillInfo.get("fapiaohao").toString():"";
				String purpose = payRequestBillInfo.getDescription() != null ? payRequestBillInfo.getDescription(): "";// ժҪ
				// ���㷽ʽ��02���
				SettlementTypeInfo stInfo = SettlementTypeFactory
						.getLocalInstance(ctx)
						.getSettlementTypeInfo(
								new ObjectUuidPK(
										"e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
				String Jsfs = stInfo.getNumber();
				String Eassign = "0";// ͬ����ʶ
				String Eastime = sdf.format(new Date());// ͬ��ʱ��
				String oasign = "0";// OA��¼ͬ����־�ı�ʶ
				String Oatime = sdf.format(new Date());// Oa��¼ʱ��
				String applyer = null;
				String personid = "";
				String requestReason = payRequestBillInfo.getRequestReason() != null ? payRequestBillInfo.getRequestReason(): "";//����ԭ��
//				if(payRequestBillInfo.getApplyer()!=null && payRequestBillInfo.getApplyer().getId()!=null && !"".equals(payRequestBillInfo.getApplyer().getId().toString())){
//					personid = payRequestBillInfo.getApplyer().getId().toString();
//				}else{
//					personid="6tB2iCtgRFGVooJDiONYlYDvfe0=";
//				}
//				PersonInfo person = PersonFactory.getLocalInstance(ctx).getPersonInfo(new ObjectUuidPK(personid));
//		        applyer = person.getNumber();// �����˱���
		        
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
//					//�������Ϊ���ӹ������� Formtype =6    VMI Formtype = 8
//					if(prex!=null && !"".equals(prex)){
//						if(!"JGF".equals(prex)) Formtype = "8";
//					}else{
//						Formtype = "6";
//					} 
//					  person = PersonFactory.getLocalInstance(ctx)
//							.getPersonInfo(
//									new ObjectUuidPK(payRequestBillInfo
//											.getApplyer().getId()));
//					applyer = person.getNumber();// �����˱���
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
//					applyer = person.getNumber();// �����˱���
//				}
				// ��¼
				List<Map<String, String>> entrys = new ArrayList<Map<String, String>>();
				List<Map<String, String>> oanumbers = AppUnit.getSumPayRequestBillEntrys(ctx, id);
				String type = "";
				if (oanumbers != null && oanumbers.size() > 0) {
 					for (Map<String, String> mp : oanumbers) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("parentID", id);// ��id
						map.put("fnumber",  mp.get("oanumber").toString());// �ɹ����뵥���
						map.put("Companynum", couInfo.getId().toString());// eas������֯id
						map.put("Suppliername", couInfo.getName());// eas������֯����
						map.put("requestAmount", mp.get("amount").toString());// ��¼���
						type = AppUnit.getPurTypeByOANumber(mp.get("oanumber").toString(),prex); 
						map.put("formtypezhi",type);// �ɹ�����
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
									.getString("PaymentBillToMid"), "���ݱ���ɹ�");// ��¼��־
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

				result = "���н���������ɹ���";
			} catch (EASBizException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
								DateBasetype.PaymentBillToMid, "���ݱ���ʧ��", e
										.getMessage());// ��¼��־
				String msg = "����ʧ�ܣ��쳣�ǣ�" + e.toString();
				return msg;
			}
		} else {
			result = "����ʧ�ܣ������Ѿ����ڡ�";
		}
		return result;
	}

	protected Boolean isExistsByBillId(Context ctx, String database,
			String billID) {
		return EAISynTemplate.existsoa(ctx, database, "eas_lolkk_fk", billID);
	}

	/*
	 * �ɹ����뵥 PurRequestInfo 1
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
									.getString("OA_PurRequest"), "���ݱ���ʧ�ܣ�"
									+ info.getNumber() + "�Ĺ�˾����Ϊ��");// ��¼��־
					continue;
				}
				if (map.get("FNUMBER") != null
						&& !map.get("FNUMBER").equals("")) {// ���ݲɹ�����
					// �鿴�ɹ����Ƿ����ظ���
					if (PurRequestFactory.getLocalInstance(ctx).exists("where caigoushenqingdandanhao ='"+ map.get("FNUMBER") + "'")) {
						// ����Ǵ��ڵ� ��ͷ �ǿ����޸ĵ� ���Ƿ�¼�ǲ�����
						// �ж� ���ݲɹ����� �鿴�Ƿ�����������
						addOrUpdate = true;
						/*
						 * CoreBaseCollection collection = PurRequestFactory
						 * .getLocalInstance(ctx).getCollection(
						 * "where caigoushenqingdandanhao ='" +
						 * map.get("FNUMBER") + "'"); if (collection.size() > 1)
						 * { // ��Ҫȷ�� �ɹ�������Щ��Ϣ�����޸� } else { //
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

				// ---------------������Ϣ
				info.setIsMergeBill(false);
				info.setPurchaseType(PurchaseTypeEnum.PURCHASE);
				// ҵ������
				BizTypeInfo bizTypeinfo = BizTypeFactory.getLocalInstance(ctx)
						.getBizTypeCollection("where number = '110'").get(0);
				info.setBizType(bizTypeinfo);
				
				// ������
				/*
				 * RowTypeInfo rowTypeInfoy = new RowTypeInfo();//ԭ�ϲɹ�
				 * rowTypeInfoy.setId(BOSUuid.read(
				 * "00000000-0000-0000-0000-0000000000017C7DC4A3"));
				 * 
				 * RowTypeInfo rowTypeInfod = new RowTypeInfo();//��ֵ�׺�Ʒ
				 * rowTypeInfod
				 * .setId(BOSUuid.read("4SooXWYKTKuLrgpPBQzUInx9xKM="));
				 * 
				 * RowTypeInfo rowTypeInfog = new RowTypeInfo();//�̶��ʲ�
				 * rowTypeInfog
				 * .setId(BOSUuid.read("eLKiKZWaRU2OjdM1BH+1HXx9xKM="));
				 */

				RowTypeInfo rowTypeInfoy = RowTypeFactory.getLocalInstance(ctx)
						.getRowTypeInfo("where number = '" + "010" + "'");

				RowTypeInfo rowTypeInfod = RowTypeFactory.getLocalInstance(ctx)
						.getRowTypeInfo("where number = '" + "210" + "'");

				RowTypeInfo rowTypeInfog = RowTypeFactory.getLocalInstance(ctx)
						.getRowTypeInfo("where number = '" + "200" + "'");

				// �ұ�
				CurrencyInfo currency = CurrencyFactory.getLocalInstance(ctx)
						.getCurrencyCollection("where number='BB01'").get(0);
				ObjectUuidPK orgPK = new ObjectUuidPK(map.get("COMPANY")
						.toString());
				CompanyOrgUnitInfo xmcompany = CompanyOrgUnitFactory
						.getLocalInstance(ctx).getCompanyOrgUnitInfo(orgPK);
				info.setCompanyOrgUnit(xmcompany);
				System.out.println("------------------������˾��"
						+ xmcompany.getId() + "----" + xmcompany.getName());
				AdminOrgUnitInfo admin = AdminOrgUnitFactory.getLocalInstance(
						ctx).getAdminOrgUnitInfo(orgPK);
				info.setAdminOrg(admin);
				// �����֯
				StorageOrgUnitInfo storageorginfo = StorageOrgUnitFactory
						.getLocalInstance(ctx).getStorageOrgUnitInfo(orgPK);
				// StorageOrgUnitInfo storageOrgUnitInfo = new
				// StorageOrgUnitInfo();
				// storageOrgUnitInfo.setId(BOSUuid.read(map.get("company").
				// toString()));

				// �ɹ���֯
				PurchaseOrgUnitInfo purchaseorginfo = PurchaseOrgUnitFactory
						.getLocalInstance(ctx).getPurchaseOrgUnitInfo(orgPK);
				// PurchaseOrgUnitInfo purchaseorginfo = new
				// PurchaseOrgUnitInfo();
				//purchaseorginfo.setId(BOSUuid.read(map.get("company").toString
				// ()));

				// ������
				PersonInfo person = PersonFactory.getLocalInstance(ctx)
						.getPersonCollection(
								"where number='"
										+ map.get("APPLYER").toString() + "'")
						.get(0);
				info.setPerson(person);

				// oa�ɹ����������Ա
				if (map.get("CGFK_APPLYER") != null
						&& !"".equals(map.get("CGFK_APPLYER").toString())) {
					PersonInfo CGFK_APPLYER = PersonFactory.getLocalInstance(
							ctx).getPersonCollection(
							"where number='"
									+ map.get("CGFK_APPLYER").toString() + "'")
							.get(0);
					info.put("caigourenyuan", CGFK_APPLYER);
				}
				//����
//				PersonInfo person = new PersonInfo();
//				person.setId(BOSUuid.read("jbYAAACupyyA733t"));
//				info.setPerson(person);
//				info.put("caigourenyuan", person);
				
				/*int isGift = Integer.getInteger(map.get("shifouzengpin").toString());
				info.put("caigourenyuan", isGift);*/
				// ҵ������
				SimpleDateFormat formmat = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				Date bizDate = null;
				String purchModel = "";// oa��������
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
				info.setBizDate(bizDate); // ҵ������

				// info.setTotalAmount(new
				// BigDecimal(map.get("REQUESTAMOUNT").toString()));//������ ���ϼ�

				// OA�ɹ����뵥����
				info.put("caigoushenqingdandanhao", map.get("FNUMBER"));
			
				
				// ������
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
				//�Ƿ���Ʒ
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
					// ��������
					info.put("danjuleixzing", purchModel);// ��ʱ
				}

				BillTypeInfo billtype = new BillTypeInfo();
				billtype.setId(BOSUuid
						.read("510b6503-0105-1000-e000-0107c0a812fd463ED552"));
				info.setBillType(billtype);// �������� ��ʱд��

				/*
				 * if(map.get("APPLYER")!= null &&
				 * PersonFactory.getLocalInstance(ctx).exists(new
				 * ObjectUuidPK(map.get("APPLYER").toString()))){ PersonInfo
				 * personInfo = new PersonInfo();
				 * personInfo.setId(BOSUuid.read(map
				 * .get("APPLYER").toString())); info.setPerson(personInfo); }
				 */

				// ---------------������Ϣ ------ end
				if (map.get("PURCHTYPE") != null
						&& map.get("PURCHTYPE").equals("10")) {// ����� 10 �̶��ʲ�����
					// ����в�

					if (flag) {// �ж��Ƿ��Ѵ��� �����ڲŽ��� �����¼������
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
								// ����
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
									MaterialInfo material = collection.get(0); // ����
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
											"���ݱ���ʧ�ܣ�" + info.getNumber()
													+ "���ϱ��벻����");// ��¼��־
								}

								BigDecimal amount = new BigDecimal(0.00);
								BigDecimal price = new BigDecimal(0.00);
								if (map1.get("AMOUNT") != null
										&& !"".equals(map1.get("AMOUNT")
												.toString().trim())) {
									amount = new BigDecimal(map1.get("AMOUNT")
											.toString().trim());// ���
								}
								if (map1.get("PRICE") != null
										&& !"".equals(map1.get("PRICE")
												.toString().trim())) {
									price = new BigDecimal(map1.get("PRICE")
											.toString().trim());// ����
								}
								BigDecimal qty = new BigDecimal(map1.get("QTY")
										.toString());// ����

								// ���
								if (VerifyUtil.notNull(map1.get("GUIGE"))) {
									entryInfo.setNoNumMaterialModel(map1.get(
											"GUIGE").toString());// ����ͺ� ��ʱʹ�ù��
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
								entryInfo.setPurchasePerson(person);// �ɹ�Ա
								entryInfo.setReceivedOrgUnit(storageorginfo);// �ջ���֯
								// entryInfo.setMergeBillSeq(0);
								entryInfo.setAdminOrgUnit(admin);
								entryInfo.setBizDate(info.getBizDate());
								// entryInfo.setMaterial(material);
								//entryInfo.setBaseUnit(material.getBaseUnit());
								// entryInfo.setUnit(material.getBaseUnit());

								entryInfo.setQty(qty);
								entryInfo.setAssociateQty(qty);
								entryInfo.setBaseQty(qty.multiply(qtymultiple));// ��������
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
								// ����OA���ݹ����Ĺ�� �ͺ��ֶν���ƴ��
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
								
								entryInfo.setRequirementDate(bizDate);// ��������
								entryInfo.setProposeDeliveryDate(bizDate);
								entryInfo.setProposePurchaseDate(bizDate);
								
								if(giftFlag){
									entryInfo.setRowType(rowTypeInfoy);// ������
									info.getEntries().add(entryInfo);
									totalAmountSmall = totalAmountSmall.add(amount);
								}else{
									if ((price.compareTo(new BigDecimal(2000))) == -1) { // С��
										entryInfo.setRowType(rowTypeInfod);// ������
										info.getEntries().add(entryInfo);
										totalAmountSmall = totalAmountSmall.add(amount);
									} else {
										entryInfo.setRowType(rowTypeInfog);// ������
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
									"���ݱ���ʧ�ܣ�" + info.getNumber() + "��û�з�¼");// ��¼��־
							continue;
						}
						// ��������
						if (info.getEntries().size() > 0) {
							info.setTotalAmount(totalAmountSmall);
							info.setLocalTotalAmount(totalAmountSmall);
							DemandTypeInfo type = new DemandTypeInfo();
							if(giftFlag){
								type.setId(BOSUuid.read("d8iX3GB6dt3gUwEAAH8kOKvcMAg="));// ԭ�ϲɹ�
							}else{
								type.setId(BOSUuid.read("nMyVhAcxRvyXPYq+xI49eqvcMAg="));// ��ֵ�׺�Ʒ
							}
					
							info.setDemandType(type);
							// OA�ɹ����뵥���
							info.put("caigoushenqingdanjine", totalAmountSmall);
							PurRequestFactory.getLocalInstance(ctx).save(info);// С��
							// submitInt = 1;
							//PurRequestFactory.getLocalInstance(ctx).submit(info
							// );//
						}
						if (infoBig.getEntries().size() > 0) {
							infoBig.setTotalAmount(totalAmountBig);
							infoBig.setLocalTotalAmount(totalAmountBig);
							DemandTypeInfo type2 = new DemandTypeInfo();
							// type2.setId(BOSUuid.read(
							// "d8XjLJBgKZrgUwEAAH9KPqvcMAg="));//�̶��ʲ�
							type2.setId(BOSUuid
									.read("Sp/A4ZhGTD2izLb/R8/WfavcMAg="));// �̶��ʲ�
							infoBig.setDemandType(type2);
							// OA�ɹ����뵥���
							infoBig
									.put("caigoushenqingdanjine",
											totalAmountBig);
							PurRequestFactory.getLocalInstance(ctx).save(
									infoBig);// ���
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
								map.get("FNUMBER").toString(), "���ݱ���ɹ�");// ��¼��־

					} else {
						// Ҫ�жϲ�֮�� һ���ɹ������� ���ܻ����������� ���� Ҫ�ж�
						PurRequestFactory.getLocalInstance(ctx).save(info);
						submitInt = 1;
						PurRequestFactory.getLocalInstance(ctx).submit(info);
						// PurRequestFactory.getLocalInstance(ctx).audit(pk);
						updateFSign(ctx, dataBase1, "eas_lolkk_cg", 1, map.get(
								"ID").toString());
						AppUnit.insertLog(ctx, DateBaseProcessType.Update,
								DateBasetype.OA_PurRequest, info.getNumber(),
								map.get("FNUMBER").toString(), "�����޸ĳɹ�");// ��¼��־

					}

				} else {// ����
					BigDecimal totalAmount = new BigDecimal(0);
					if (flag) {// �ж��Ƿ��Ѵ��� �����ڲŽ��� �����¼������
						sql = "select parentid,material,materialname,supplier,brand,guige,xh,artNo,price,qty,amount from eas_lolkk_cg_sub where parentid ="
								+ map.get("ID");
						// OA_EAS_PurRequestEntry
						List<Map<String, Object>> list1 = EAISynTemplate.query(
								ctx, dataBase1, sql);

						if (list1 != null && list1.size() > 0) {
							for (Map<String, Object> map1 : list1) {
								PurRequestEntryInfo entryInfo = new PurRequestEntryInfo();
								 qtymultiple = new BigDecimal(1);
								entryInfo.setRowType(rowTypeInfoy);// ������
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
								// ����

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
									MaterialInfo material = collection.get(0); // ����
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
											"���ݱ���ʧ�ܣ�" + info.getNumber()
													+ "���ϱ��벻����");// ��¼��־
								}

								BigDecimal amount = new BigDecimal(0.00);
								BigDecimal price = new BigDecimal(0.00);
								if (map1.get("AMOUNT") != null
										&& !"".equals(map1.get("AMOUNT")
												.toString().trim())) {
									amount = new BigDecimal(map1.get("AMOUNT")
											.toString().trim());// ���
								}
								if (map1.get("PRICE") != null
										&& !"".equals(map1.get("PRICE")
												.toString().trim())) {
									price = new BigDecimal(map1.get("PRICE")
											.toString().trim());// ����
								}
								BigDecimal qty = new BigDecimal(map1.get("QTY")
										.toString());// ����

								// ���
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
								entryInfo.setPurchasePerson(person);// �ɹ�Ա
								entryInfo.setReceivedOrgUnit(storageorginfo);// �ջ���֯
								// entryInfo.setMergeBillSeq(0);

								// entryInfo.setMaterial(material);
								entryInfo.setAdminOrgUnit(admin);
								entryInfo.setBizDate(info.getBizDate());
								//entryInfo.setBaseUnit(material.getBaseUnit());
								// entryInfo.setUnit(material.getBaseUnit());

								entryInfo.setQty(qty);
								entryInfo.setAssociateQty(qty);
								entryInfo.setBaseQty(qty.multiply(qtymultiple));// ��������

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
								// ����OA���ݹ����Ĺ�� �ͺ��ֶν���ƴ��
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
								entryInfo.setRequestQty(qty);// ��������
								entryInfo.setRequirementDate(bizDate);// ��������
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
									"���ݱ���ʧ�ܣ�" + info.getNumber() + "��û�з�¼");// ��¼��־
							continue;
						}
					}
					info.put("caigoushenqingdanjine", totalAmount);
					info.setTotalAmount(totalAmount);
					info.setLocalTotalAmount(totalAmount);
					DemandTypeInfo type = new DemandTypeInfo();
					//type.setId(BOSUuid.read("d8XjLJBfKZrgUwEAAH9KPqvcMAg="));/
					// / ԭ�ϲɹ�
					type.setId(BOSUuid.read("d8iX3GB6dt3gUwEAAH8kOKvcMAg="));// ԭ�ϲɹ�
					info.setDemandType(type);

					PurRequestFactory.getLocalInstance(ctx).save(info);
					submitInt = 1;
					PurRequestFactory.getLocalInstance(ctx).submit(info);//
					updateFSign(ctx, dataBase1, "eas_lolkk_cg", 1, map
							.get("ID").toString());

					if (addOrUpdate) {
						AppUnit.insertLog(ctx, DateBaseProcessType.Update,
								DateBasetype.OA_PurRequest, info.getNumber(),
								info.getString("OA_PurRequest"), "�����޸ĳɹ�");// ��¼��־
					} else {
						AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
								DateBasetype.OA_PurRequest, info.getNumber(),
								info.getString("OA_PurRequest"), "���ݱ���ɹ�");// ��¼��־
					}
				}

				// AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
				// DateBasetype.HIS_PurchIn,
				// info.getNumber(),info.getString("HisReqID"),"���ݱ���ɹ�");//��¼��־
			}

		} catch (EASBizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String msg = "";
			if (submitInt == 0) {
				AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
						DateBasetype.OA_PurRequest, fid+"���ݱ���ʧ��", e.getMessage());// ��¼��־
				if (fid != null && !fid.equals("")) {
					updateFSign(ctx, dataBase1, "eas_lolkk_cg", 2, fid);
				}
				msg = "����ʧ�ܣ��쳣�ǣ�" + e.toString();
			} else {// ����ɹ����ύʧ��
				msg = "�����ύʧ��: " + e.toString();
				updateFSign(ctx, dataBase1, "eas_lolkk_cg", 1, fid);
			}
			return msg;
			// AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
			// DateBasetype.HIS_PurchIn,"","","���ݱ���ʧ��");//��¼��־
		}
		return super._PurRequestFormOA(ctx, dataBase1);
	}
  
	/**
	 * ���ý𸶿ͬ����eas
	 */

	@Override
	public String syncPaymentBillFormOA(Context ctx, String database)throws BOSException {
		
		//��eas_lolkk_bx���������˲����ڵ������޸�Ϊ״̬2
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
					AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,DateBasetype.OA_PaymentBill, payInfo.getNumber(), payInfo.getString("OA_PaymentBill"), "���ݱ���ʧ��,"+ payInfo.getNumber() + "�Ĺ�˾����Ϊ��");
					continue;
				}
				if (map.get("FNUMBER") != null && !map.get("FNUMBER").toString().equals("")) {//�鿴�����Ƿ���ڣ�����������޸��м���״̬��Ȼ��ִ����һ��
					if (PaymentBillFactory.getLocalInstance(ctx).exists("where caigoushenqingdandanhao ='"+ map.get("FNUMBER") + "'")) {
						updateFSign(ctx, database, "eas_lolkk_bx", 1, map.get("ID").toString());
						continue;
					}
				}
				
				payInfo.setSourceType(com.kingdee.eas.fi.cas.SourceTypeEnum.AP);//��Դϵͳ
				payInfo.setDescription("��");//ժҪ
				payInfo.setIsExchanged(false); //�Ƿ��Ѿ����� 
				payInfo.setExchangeRate(new BigDecimal("1.00"));// ���� 
				payInfo.setLastExhangeRate(new BigDecimal("0.00"));//���������
				payInfo.setIsInitializeBill(false); //�Ƿ��ʼ������
				CurrencyInfo currency = CurrencyFactory.getLocalInstance(ctx).getCurrencyCollection("where number='BB01'").get(0);
				payInfo.setCurrency(currency);// �ұ�
				payInfo.setFiVouchered(false);//�Ƿ�������ƾ֤
				payInfo.setIsLanding(false); //ǿ�����
				//����  ����״̬  ���н���״̬
				//payInfo.setPayType(0); 
				
				AsstActTypeInfo actType = new AsstActTypeInfo(); 
				actType.setId(BOSUuid.read("YW3xsAEJEADgAAWgwKgTB0c4VZA="));//--ְԱ��
				payInfo.setPayeeType(actType);//����������
				payInfo.setIsImport(false); //�Ƿ���
				payInfo.setIsNeedPay(true);//�Ƿ���Ҫ֧��
				payInfo.setIsReverseLockAmount(true);//�Ƿ�д�������
				payInfo.setPaymentBillType(CasRecPayBillTypeEnum.commonType);//�������
				PaymentBillTypeInfo  billType = PaymentBillTypeFactory.getLocalInstance(ctx).getPaymentBillTypeInfo(new ObjectUuidPK("NLGLdwEREADgAAHjwKgSRj6TKVs="));
				payInfo.setPayBillType(billType);//��������
				PaymentTypeInfo paymentTypeInfo = PaymentTypeFactory.getLocalInstance(ctx).getPaymentTypeInfo(new ObjectUuidPK("2fa35444-5a23-43fb-99ee-6d4fa5f260da6BCA0AB5"));
				payInfo.setPaymentType(paymentTypeInfo);//���ʽ  
				
				// ���㷽ʽ Ĭ��:���  02
				SettlementTypeInfo settlementTypeInfo = SettlementTypeFactory.getLocalInstance(ctx)
						.getSettlementTypeInfo(new ObjectUuidPK("e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
				payInfo.setSettlementType(settlementTypeInfo);
				
				// OA�ɹ����뵥����
				payInfo.put("caigoushenqingdandanhao", map.get("FNUMBER").toString());

				//��˾
				ObjectUuidPK orgPK = new ObjectUuidPK(map.get("COMPANY").toString());
				CompanyOrgUnitInfo company = CompanyOrgUnitFactory.getLocalInstance(ctx).getCompanyOrgUnitInfo(orgPK);
				payInfo.setCompany(company);
				System.out.println("------------------������˾��"+ company.getId() + "----" + company.getName());

				
				PersonInfo person = PersonFactory.getLocalInstance(ctx).getPersonCollection("where number='"+ map.get("APPLYER").toString() + "'").get(0);
				//payInfo.setPerson(person);//��Ա
				
				//PersonInfo person = PersonFactory.getLocalInstance(ctx).getPersonCollection("where number='"+ map.get("APPLYER").toString() + "'").get(0);
				payInfo.setPayeeID(person.getId().toString());//������id
				payInfo.setPayeeName(person.getName());//����������
				payInfo.setPayeeNumber(person.getNumber());//���б���
				payInfo.setPayeeBank(map.get("APPLYERBANK").toString());//�տ�����
				payInfo.setPayeeAccountBank(map.get("APPLYERBANKNUM").toString());//�տ��˺�
				/*payInfo.setPayeeBank(map.get("KHH").toString());//�տ�����
				payInfo.setPayeeAccountBank(map.get("YHZH").toString());//�տ��˺�*/				
				payInfo.setBankAcctName(person.getName());//�տ���ʵ��
				
				AdminOrgUnitInfo admin = null;
				if(map.get("DEPT")!=null && !"".equals(map.get("DEPT").toString())){
					admin = AdminOrgUnitFactory.getLocalInstance(ctx).getAdminOrgUnitInfo(new ObjectUuidPK(map.get("DEPT").toString()));
					payInfo.setAdminOrgUnit(admin); //����
					/*CostCenterOrgUnitInfo CostCenter = CostCenterOrgUnitFactory.getLocalInstance(ctx).getCostCenterOrgUnitInfo(new ObjectUuidPK(map.get("DEPT").toString()));
					payInfo.setCostCenter(CostCenter);//�ɱ�����	*/					
					//SupplierInfo supplier = SupplierFactory.getLocalInstance(ctx).getSupplierInfo(new ObjectUuidPK(map.get("DEPT").toString()));
					
					//payInfo.setBankNumber(item)
					
				}
				 
				// ҵ������
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
				
				payInfo.setBizDate(bizDate); // ҵ������
				payInfo.setBillDate(new Date());// ��������
				

				//�Զ����ֶ�-------------------
				/*String jk = null;
				if (map.get("ISLOAN").toString().equals("0")) {
					jk = "��";
				} else if (map.get("ISLOAN").toString().equals("1")) {
					jk = "��";
				}
				// �Ƿ���
				payInfo.put("shifoujiekuan", jk);

				String zlf = null;
				if (map.get("ISRENTALFEE").toString().equals("0")) {
					zlf = "��";
				} else if (map.get("ISRENTALFEE").toString().equals("1")) {
					zlf = "��";
				}
				// �Ƿ����޷�
				payInfo.put("shifouzulinfei", zlf);*/

				// ���п���
				payInfo.put("yinhangzhanghao", map.get("YHZH"));
				// ������
				payInfo.put("kaihuhang", map.get("KHH"));

				//�Զ����ֶ�-------------------end

				BigDecimal totalAmount = new BigDecimal(map.get("AMOUNT").toString()) ; 
				String entrySql = "select parentID,id,payTypecode,payTypeName,Price,qty,amount,Yjk,Ytbk,remark from eas_lolkk_bx_sub where parentid ='"+ map.get("ID").toString()+"' ";
				List<Map<String, Object>> enrtyList = EAISynTemplate.query(ctx, database,entrySql.toString());
				if(enrtyList != null && enrtyList.size()>0){
					for(Map<String, Object> entryMap : enrtyList){
						BigDecimal amount = new BigDecimal(entryMap.get("AMOUNT").toString());
						PaymentBillEntryInfo entryInfo = new PaymentBillEntryInfo();
							// ������Ŀ  ��������
						if (entryMap.get("PAYTYPECODE") != null && !"".equals(entryMap.get("PAYTYPECODE").toString())) {
								ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx)
									.getExpenseTypeInfo("where number ='"+ entryMap.get("PAYTYPECODE").toString() + "'");
								entryInfo.setExpenseType(typeinfo);
						}else {
								ExpenseTypeInfo typeinfo = ExpenseTypeFactory.getLocalInstance(ctx).getExpenseTypeInfo("where number ='CL01'");
								entryInfo.setExpenseType(typeinfo);
						}

						entryInfo.setCurrency(currency);// �ұ�
						entryInfo.setAmount(amount);//Ӧ�����  
						entryInfo.setAmountVc(BigDecimal.ZERO);//Ӧ�գ���������ۼƺ��� 
						entryInfo.setLocalAmt(amount);//Ӧ�գ�������λ�ҽ��
						entryInfo.setLocalAmtVc(BigDecimal.ZERO);//Ӧ�գ�������λ���ۼƺ���
						entryInfo.setUnVcAmount(amount); //Ӧ�գ�����δ�������
						entryInfo.setUnVcLocAmount(amount); //Ӧ�գ�����δ������λ�ҽ��
						entryInfo.setUnVerifyExgRateLoc(BigDecimal.ZERO); //δ������㱾λ�ҽ��
						entryInfo.setRebate(BigDecimal.ZERO); //�ֽ��ۿ�
						entryInfo.setRebateAmtVc(BigDecimal.ZERO); //�ۿ۽���ۼƺ���
						entryInfo.setRebateLocAmt(BigDecimal.ZERO); //�ۿ۱�λ�ҽ��
						entryInfo.setRebateLocAmtVc(BigDecimal.ZERO); //�ۿ۱�λ�ҽ���ۼƺ���
						entryInfo.setActualAmt(amount);//ʵ�գ��������
						entryInfo.setActualAmtVc(BigDecimal.ZERO);//ʵ�գ���������ۼƺ���
						entryInfo.setActualLocAmt(amount);//ʵ�գ�������λ�ҽ��
						entryInfo.setActualLocAmtVc(BigDecimal.ZERO);//ʵ�գ�������λ�ҽ���ۼƺ���
						entryInfo.setUnLockAmt(amount);//δ�������
						entryInfo.setUnLockLocAmt(amount);//δ������λ�ҽ��
						entryInfo.setLockAmt(BigDecimal.ZERO); //���������
						
						entryInfo.setPayableDate(new Date());//Ӧ������
						
						if(entryMap.get("REMARK")!=null){
							entryInfo.setRemark(entryMap.get("REMARK").toString());
						}
						
						//totalAmount = totalAmount.add(amount);
						payInfo.getEntries().addObject(entryInfo);//��ӷ�¼ 
			
					}

				}else{
					System.out.println("entrty is empty _---------------------------"+ map.get("FNUMBER").toString());
					updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
					AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,DateBasetype.OA_PaymentBill, payInfo.getNumber(), payInfo.getString("OA_PaymentBill"), "����û�з�¼");// ��¼��־
					continue;
				}
				//payInfo.setActualPayAmount(totalAmount);//ʵ�����ϼ�
				payInfo.setActPayAmtVc(BigDecimal.ZERO);//ʵ������ۼƺ���
				payInfo.setActPayAmt(totalAmount);//ʵ����λ�ҽ��ϼ�
				payInfo.setActPayLocAmtVc(BigDecimal.ZERO);//ʵ����λ�ҽ���ۼƺ���
				payInfo.setAmount(totalAmount);//Ӧ�ս��
				payInfo.setLocalAmt(totalAmount);//Ӧ�գ�������λ�ҽ��
				payInfo.setAccessoryAmt(0); //������
				payInfo.setBgAmount(BigDecimal.ZERO);//Ԥ���׼���
				payInfo.setVerifiedAmt(BigDecimal.ZERO);//�Ѽ�����ϼ�
				payInfo.setVerifiedAmtLoc(BigDecimal.ZERO);//�Ѽ����λ�Һϼ�
				payInfo.setUnVerifiedAmt(totalAmount);//δ������ϼ�
				payInfo.setUnVerifiedAmtLoc(totalAmount);//δ�����λ�Һϼ�
				payInfo.setBgCtrlAmt(totalAmount);//Ԥ����ƽ��
				
				payInfo.setBillStatus(com.kingdee.eas.fi.cas.BillStatusEnum.SAVE);
				
				//payInfo.setAuditDate(new Date(System.currentTimeMillis()));//�������

				/*IPaymentBill iPayBill = PaymentBillFactory.getRemoteInstance();//��ȡʵ�� 
				IObjectPK pk = iPayBill.addnew(payInfo);*/

				PaymentBillFactory.getLocalInstance(ctx).save(payInfo);
				updateFSign(ctx, database, "eas_lolkk_bx", 1, map.get("ID").toString());

				/*try {
					PaymentBillFactory.getLocalInstance(ctx).submit(payInfo);

					PaymentBillFactory.getLocalInstance(ctx).audit(payInfo, "");
					
				} catch (Exception e11) {
					System.out.println("����ɹ����ύ�������ʧ��");
				}*/
			}

		} catch (EASBizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,DateBasetype.PaymentBillToMid, fid+"���ݱ���ʧ��", e.getMessage());// ��¼��־
			String msg = "����ʧ�ܣ��쳣�ǣ�" + e.toString();
			System.out.println("--------------------"+msg);
			return msg;
		
		}
		return super.syncPaymentBillFormOA(ctx, database);
	}

	/**
	 * �޸�ͬ����״̬
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
	 * ����Ӧ��������(��ȥ�г�Ͷ������)
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
				AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,DateBasetype.OA_OtherBill, info.getNumber(), info.getString("OA_OtherBill"), "���ݱ���ʧ��,"+ info.getNumber() + "�Ĺ�˾����Ϊ��");// ��¼��־
				return "";
			}
			if (map.get("FNUMBER") != null && !map.get("FNUMBER").toString().equals("")) {
				// ����caigoushenqingdandanhao �ֶ��Ƿ���Ψһ��
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
			// ������
			// RowTypeInfo rowTypeInfo =
			// RowTypeFactory.getLocalInstance(ctx)
			// .getRowTypeInfo("where number = '"+demandTypeNo+"'");
			// �ұ�
			CurrencyInfo currency = CurrencyFactory.getLocalInstance(ctx)
					.getCurrencyCollection("where number='BB01'").get(0);

			ObjectUuidPK orgPK = new ObjectUuidPK(map.get("COMPANY")
					.toString());
			CompanyOrgUnitInfo xmcompany = CompanyOrgUnitFactory
					.getLocalInstance(ctx).getCompanyOrgUnitInfo(orgPK);
			info.setCompany(xmcompany);
			System.out.println("------------------������˾��"
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
			// ���п���
			if(map.get("YHZH")!=null && !"".equals(map.get("YHZH").toString())){
				info.put("yinhangzhanghao", map.get("YHZH"));
			}
			if(map.get("APPLYERBANKNUM")!=null && !"".equals(map.get("APPLYERBANKNUM").toString())){
				info.setRecAccountBank(map.get("APPLYERBANKNUM").toString());
			}
			// ������
			if(map.get("KHH")!=null && !"".equals(map.get("KHH").toString())){
				info.put("kaihuhang", map.get("KHH"));
			}
			if(map.get("APPLYERBANK")!=null && !"".equals(map.get("APPLYERBANK").toString())){
				info.setRecBank(map.get("APPLYERBANK").toString());
			}
			
			// �����֯
			// StorageOrgUnitInfo storageorginfo
			// =StorageOrgUnitFactory.getLocalInstance
			// (ctx).getStorageOrgUnitInfo(orgPK);
			// �ɹ���֯
			PurchaseOrgUnitInfo purchaseorginfo = PurchaseOrgUnitFactory.getLocalInstance(ctx).getPurchaseOrgUnitInfo(orgPK);
			info.setPurOrg(purchaseorginfo); 
			
			String personId = getPersonIdByNumber(ctx,map.get("APPLYER").toString());
		    IObjectPK personpk = new ObjectUuidPK(BOSUuid.read(personId));
			PersonInfo person = PersonFactory.getLocalInstance(ctx).getPersonInfo(personpk);
			info.setPerson(person); 
			
			
			// ���㷽ʽ Ĭ��:���
			SettlementTypeInfo settlementTypeInfo = SettlementTypeFactory
					.getLocalInstance(ctx)
					.getSettlementTypeInfo(
							new ObjectUuidPK(
									"e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
			info.setSettleType(settlementTypeInfo);  
			// ҵ������
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
			info.setBizDate(bizDate); // ҵ������
			info.setBillDate(new Date());// ��������
			info.setCurrency(currency);// �ұ�

			// info.setNumber(map.get("FNUMBER").toString()); 
			// ����
			info.setExchangeRate(new BigDecimal("1.00")); 
			OtherBillType otherBillType = null;
			// ����Ӧ����=201,�ɹ���Ʊ=202,�ɹ����÷�Ʊ=203,Ӧ������������=204
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
			otherBillType = OtherBillType.OtherPay;// ��Ĭ�� ������ ȷ��ʹ��ʲô��������
			info.setBillType(otherBillType);

			// BillStatusEnum billStatusEnum = null;
			// info.setBill(billStatusEnum);

			// OA�ɹ����뵥����
			info.put("caigoushenqingdandanhao", map.get("FNUMBER")); 
			// ������
			info.put("OAcaigoushenqingdanjine", map.get("AMOUNT")); 
			String jk = null;
			if (map.get("ISLOAN").toString().equals("0")) {
				jk = "��";
			} else if (map.get("ISLOAN").toString().equals("1")) {
				jk = "��";
			}
			// �Ƿ���
			info.put("shifoujiekuan", jk);

			String zlf = null;
			if (map.get("ISRENTALFEE").toString().equals("0")) {
				zlf = "��";
			} else if (map.get("ISRENTALFEE").toString().equals("1")) {
				zlf = "��";
			}
			// �Ƿ����޷�
			info.put("shifouzulinfei", zlf);

			String djlx = null;
			// shifouguanlibumen
			String isAdminDept = "0";
			String[] deptArry = { "�󻮲�", "������", "���粿", "���粿", "��ý�岿","��ѯ��", "Ӫ������" };
			if (map.get("PURCHTYPE").toString().equals("01")||map.get("PURCHTYPE").toString().equals("04") ||map.get("PURCHTYPE").toString().equals("09")) {
				djlx = "���ñ���";
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
				djlx = "�ɹ�����"; // �ɹ����������������
				actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
				faccount = "2241.96";
			} else if (map.get("PURCHTYPE").toString().equals("03")) {
				djlx = "�г�Ͷ��";
				actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
				faccount = "2241.96";
			} else if (map.get("PURCHTYPE").toString().equals("04")) {
				djlx = "���÷ѱ���";
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
				djlx = "���⸶��";
				actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
				faccount = "2241.96";
			} else if (map.get("PURCHTYPE").toString().equals("06")) {
				djlx = "��ͬר�ø���";
				actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
				faccount = "2241.96";
			} else if (map.get("PURCHTYPE").toString().equals("07")) {
				djlx = "���ӹ�";
				actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
				faccount = "2241.96";
			}

			// ��������
			info.put("yingfudanjuleixing", djlx);
			info.put("shifouguanlibumen", isAdminDept);
			info.put("fapiaohao", "OA0000"); // OA0000
			
			String companytype = AppUnit.getComapnyTypeByNumber(ctx,xmcompany.getNumber());
			if(companytype!=null && !"".equals(companytype))
			info.put("CompanyType",companytype);
			
			// ��ƿ�Ŀ
			AccountViewInfo accountInfo = new AccountViewInfo(); 
			accountInfo = AccountViewFactory.getLocalInstance(ctx).getAccountViewInfo(
							"where number = '" + faccount+ "' and companyID ='"+ map.get("COMPANY").toString() + "' ");

			// ���������� YW3xsAEJEADgAAVEwKgTB0c4VZA=
			AsstActTypeInfo actType = AsstActTypeFactory.getLocalInstance(ctx).getAsstActTypeInfo(new ObjectUuidPK(actTypePk));
			info.setAsstActType(actType);
			// AsstActTypeInfo actType = new AsstActTypeInfo(); //����������
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
							info.getString("OA_OtherBill"), "û�иù�Ӧ�̱���");// ��¼��־
					return "";
				} 
			}

			VerificateBillTypeEnum billTypeEnum = VerificateBillTypeEnum.OtherPaymentBill;
			info.setSourceBillType(billTypeEnum);
			// ҵ������
			BizTypeInfo bizTypeInfo = BizTypeFactory.getLocalInstance(ctx)
					.getBizTypeInfo("where number = 110");
			info.setBizType(bizTypeInfo);

			// ----------------------
			/*
			 * FSourceBillType ��Դ�������� FBizTypeID ҵ������ T_SCM_BizType
			 * FTotalAmount ���ϼ�
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
					// ������Ŀ
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
					entryInfo.setPrice(price);// ����
					entryInfo.setTaxPrice(price);// ��˰����
					entryInfo.setActualPrice(price);// ʵ�ʺ�˰����
					entryInfo.setRealPrice(price);// ʵ�ʵ���
					entryInfo.setQuantity(qty); // ����
					entryInfo.setBaseQty(BigDecimal.ZERO); // ����������λ����
					entryInfo.setDiscountRate(BigDecimal.ZERO); // ��λ�ۿ�
					entryInfo.setDiscountAmount(BigDecimal.ZERO); // �ۿ۶�
					entryInfo.setDiscountAmountLocal(BigDecimal.ZERO); // �ۿ۶λ��
					entryInfo.setHisUnVerifyAmount(BigDecimal.ZERO); // ��ʷδ�����
					entryInfo.setHisUnVerifyAmountLocal(BigDecimal.ZERO); // ��ʷδ����λ��
					entryInfo.setAssistQty(BigDecimal.ZERO); // ��������
					entryInfo.setWittenOffBaseQty(BigDecimal.ZERO); // �Ѻ�������������
					entryInfo.setLocalWrittenOffAmount(BigDecimal.ZERO); // δ������λ�ҽ��
					entryInfo.setUnwriteOffBaseQty(BigDecimal.ZERO); // δ������������
					entryInfo.setVerifyQty(BigDecimal.ZERO);
					entryInfo.setLockVerifyQty(BigDecimal.ZERO);
					entryInfo.setLocalUnwriteOffAmount(amount); // δ������λ�ҽ��
					entryInfo.setAmount(amount);// Ӧ�����
					entryInfo.setAmountLocal(amount); // Ӧ����λ��
					entryInfo.setTaxAmount(BigDecimal.ZERO);
					entryInfo.setTaxAmountLocal(BigDecimal.ZERO);
					entryInfo.setTaxRate(BigDecimal.ZERO);
					entryInfo.setUnVerifyAmount(amount);// δ������
					entryInfo.setUnVerifyAmountLocal(amount);// δ�����λ��
					entryInfo.setLockUnVerifyAmt(amount);// δ�������
					entryInfo.setLockUnVerifyAmtLocal(amount);// δ������λ��
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
					entryInfo.put("pinpai", map.get("BRAND")); // �Ҳ��� û�д���ֵ
					entryInfo.put("huohao", map.get("ATRNO")); // �Ҳ��� û�д���ֵ 
					totalAmount = totalAmount.add(amount);
					info.getEntries().addObject(entryInfo);
				} 
			} else {
				System.out.println("entrty is empty _--------------------------------------"+ map.get("FNUMBER"));
				updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
				AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
						DateBasetype.OA_OtherBill, info.getNumber(), info.getString("OA_OtherBill"), "����û�з�¼");// ��¼��־
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
				System.out.println("------------------info������˾1111��"
						+ info.getCompany().getId() + "----" + info.getCompany().getName());
				info.setBillStatus(BillStatusEnum.SAVE);
				OtherBillFactory.getLocalInstance(ctx).submit(info);
				System.out.println("------------------info������˾2222��"
						+ info.getCompany().getId() + "----" + info.getCompany().getName());

				OtherBillFactory.getLocalInstance(ctx).audit(new ObjectUuidPK(info.getId().toString()));
				
				if (addOrUpdate) {// �޸�
					AppUnit.insertLog(ctx, DateBaseProcessType.Update,
							DateBasetype.OA_OtherBill, info.getNumber(), info
									.getString("OA_OtherBill"), "�����޸ĳɹ�");// ��¼��־
				} else {
					AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
							DateBasetype.OA_OtherBill, info.getNumber(), info
									.getString("OA_OtherBill"), "������˳ɹ�");// ��¼��־
				}
			} catch (Exception e2) {
				//System.out.println("����ɹ����ύ�������ʧ��,"+e2.getMessage());
				logger.error(e2.getMessage());
				AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
						DateBasetype.OA_OtherBill, info.getNumber(), info
								.getString("OA_OtherBill"), "���ݱ���ɹ����ύ���ʧ�ܡ�");// ��¼��־
			} 
			
			
			
		} catch (EASBizException e) {
			e.printStackTrace();
			AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
					DateBasetype.OA_OtherBill, fid+"���ݱ���ʧ��", e.getMessage());// ��¼��־
			if (fid != null && !fid.equals("")) {
				updateFSign(ctx, database, "eas_lolkk_bx", 2, fid);
			}
			String msg = "����ʧ�ܣ��쳣�ǣ�" + e.toString();
			return msg; 
		} 
		return null;
	}
	
	/**
	 * ����Ӧ��������(�г�Ͷ������)�����ݷ�¼����
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
			
			//----------------���Ƿ�¼
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
						AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,DateBasetype.OA_OtherBill, info.getNumber(), info.getString("OA_OtherBill"), "���ݱ���ʧ��,"+ info.getNumber() + "�ķ�¼�ϵĹ�˾����Ϊ��");// ��¼��־
						return "";
					}
					if (map1.get("DEPT") == null || map1.get("DEPT").toString().equals("")) {
						System.out.println("_--------------------------------------"+ map.get("FNUMBER"));
						updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
						AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,DateBasetype.OA_OtherBill, info.getNumber(), info.getString("OA_OtherBill"), "���ݱ���ʧ��,"+ info.getNumber() + "�ķ�¼�ϵĲ��ű���Ϊ��");// ��¼��־
						return "";
					}
					if (map1.get("FNUMBER") == null || map1.get("FNUMBER").toString().equals("")) {
						System.out.println("_--------------------------------------"+ map.get("FNUMBER"));
						updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
						AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,DateBasetype.OA_OtherBill, info.getNumber(), info.getString("OA_OtherBill"), "���ݱ���ʧ��,"+ info.getNumber() + "�ķ�¼�ϵı���Ϊ��");// ��¼��־
						return "";
					}
					// ��ƿ�Ŀ
					AccountViewInfo accountInfo = new AccountViewInfo();
					accountInfo = AccountViewFactory.getLocalInstance(ctx).getAccountViewInfo(
									"where number = '" + faccount+ "' and companyID ='"+ map1.get("COMPANY").toString() + "' ");
					
					OtherBillentryInfo entryInfo = new OtherBillentryInfo();
					// ������Ŀ
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
	
					entryInfo.setPrice(price);// ����
					entryInfo.setTaxPrice(price);// ��˰����
					entryInfo.setActualPrice(price);// ʵ�ʺ�˰����
					entryInfo.setRealPrice(price);// ʵ�ʵ���
					entryInfo.setQuantity(qty); // ����
					entryInfo.setBaseQty(BigDecimal.ZERO); // ����������λ����
					entryInfo.setDiscountRate(BigDecimal.ZERO); // ��λ�ۿ�
					entryInfo.setDiscountAmount(BigDecimal.ZERO); // �ۿ۶�
					entryInfo.setDiscountAmountLocal(BigDecimal.ZERO); // �ۿ۶λ��
					entryInfo.setHisUnVerifyAmount(BigDecimal.ZERO); // ��ʷδ�����
					entryInfo.setHisUnVerifyAmountLocal(BigDecimal.ZERO); // ��ʷδ����λ��
					entryInfo.setAssistQty(BigDecimal.ZERO); // ��������
					entryInfo.setWittenOffBaseQty(BigDecimal.ZERO); // �Ѻ�������������
					entryInfo.setLocalWrittenOffAmount(BigDecimal.ZERO); // δ������λ�ҽ��
					entryInfo.setUnwriteOffBaseQty(BigDecimal.ZERO); // δ������������
					entryInfo.setVerifyQty(BigDecimal.ZERO);
					entryInfo.setLockVerifyQty(BigDecimal.ZERO);
					entryInfo.setLocalUnwriteOffAmount(amount); // δ������λ�ҽ��
					entryInfo.setAmount(amount);// Ӧ�����
					entryInfo.setAmountLocal(amount); // Ӧ����λ��
					entryInfo.setTaxAmount(BigDecimal.ZERO);
					entryInfo.setTaxAmountLocal(BigDecimal.ZERO);
					entryInfo.setTaxRate(BigDecimal.ZERO);
					entryInfo.setUnVerifyAmount(amount);// δ������
					entryInfo.setUnVerifyAmountLocal(amount);// δ�����λ��
					entryInfo.setLockUnVerifyAmt(amount);// δ�������
					entryInfo.setLockUnVerifyAmtLocal(amount);// δ������λ��
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
					entryInfo.put("pinpai", map.get("BRAND")); // �Ҳ��� û�д���ֵ
					entryInfo.put("huohao", map.get("ATRNO")); // �Ҳ��� û�д���ֵ
	
					if(map1.get("REMARK")!=null){
						entryInfo.setRemark(map1.get("REMARK").toString());
					}				 
					if (map.get("FNUMBER") != null && !map.get("FNUMBER").toString().equals("")) {
						// ����caigoushenqingdandanhao �ֶ��Ƿ���Ψһ��
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
					System.out.println("------------------������˾��"+ company.getId() + "----" + company.getName());
 
					
					AdminOrgUnitInfo admin = null;
					if (map1.get("DEPT") != null && !"".equals(map1.get("DEPT"))) {
						admin = AdminOrgUnitFactory.getLocalInstance(ctx)
								.getAdminOrgUnitInfo(new ObjectUuidPK(map1.get("DEPT").toString()));
						info.setAdminOrgUnit(admin);
						CostCenterOrgUnitInfo CostCenter = CostCenterOrgUnitFactory.getLocalInstance(ctx)
								.getCostCenterOrgUnitInfo(new ObjectUuidPK(map1.get("DEPT").toString()));
						info.setCostCenter(CostCenter);
					}

					
					// ���п���
					if(map.get("YHZH")!=null && !"".equals(map.get("YHZH").toString())){
						info.put("yinhangzhanghao", map.get("YHZH"));
					}
					if(map.get("APPLYERBANKNUM")!=null && !"".equals(map.get("APPLYERBANKNUM").toString())){
						info.setRecAccountBank(map.get("APPLYERBANKNUM").toString());
					}
					// ������
					if(map.get("KHH")!=null && !"".equals(map.get("KHH").toString())){
						info.put("kaihuhang", map.get("KHH"));
					}
					if(map.get("APPLYERBANK")!=null && !"".equals(map.get("APPLYERBANK").toString())){
						info.setRecBank(map.get("APPLYERBANK").toString());
					}

					// �ɹ���֯
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

					// ���㷽ʽ Ĭ��:���
					SettlementTypeInfo settlementTypeInfo = SettlementTypeFactory.getLocalInstance(ctx)
							.getSettlementTypeInfo(new ObjectUuidPK("e09a62cd-00fd-1000-e000-0b33c0a8100dE96B2B8E"));
					info.setSettleType(settlementTypeInfo);
					 

					// ҵ������
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

					info.setBizDate(bizDate); // ҵ������
					info.setBillDate(new Date());// ��������
					// �ұ�
					CurrencyInfo currency = CurrencyFactory.getLocalInstance(ctx).getCurrencyCollection("where number='BB01'").get(0);
					info.setCurrency(currency);// �ұ�

					// ����
					info.setExchangeRate(new BigDecimal("1.00"));

					OtherBillType otherBillType = null;
					// ����Ӧ����=201,�ɹ���Ʊ=202,�ɹ����÷�Ʊ=203,Ӧ������������=204
					 
					otherBillType = OtherBillType.OtherPay;// ��Ĭ�� ������ ȷ��ʹ��ʲô��������
					info.setBillType(otherBillType);


					// OA�ɹ����뵥����
					info.put("caigoushenqingdandanhao", map.get("FNUMBER")+"_"+map1.get("FNUMBER"));

					// ������
					info.put("OAcaigoushenqingdanjine", map1.get("AMOUNT"));

					String companType = AppUnit.getComapnyTypeByNumber(ctx,company.getNumber());
					if(companType!=null && !"".equals(companType))
					info.put("CompanyType",companType); 
					
					
					String jk = null;
					if (map.get("ISLOAN").toString().equals("0")) {
						jk = "��";
					} else if (map.get("ISLOAN").toString().equals("1")) {
						jk = "��";
					}
					// �Ƿ���
					info.put("shifoujiekuan", jk);

					String zlf = null;
					if (map.get("ISRENTALFEE").toString().equals("0")) {
						zlf = "��";
					} else if (map.get("ISRENTALFEE").toString().equals("1")) {
						zlf = "��";
					}
					// �Ƿ����޷�
					info.put("shifouzulinfei", zlf);

					String djlx = null;
					String isAdminDept = "0";
					String[] deptArry = { "�󻮲�", "������", "���粿", "���粿", "��ý�岿","��ѯ��", "Ӫ������" };
					if (map.get("PURCHTYPE").toString().equals("01")) {
						djlx = "���ñ���";
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
						djlx = "�ɹ�����"; // �ɹ����������������
						actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
						faccount = "2241.96";
					} else if (map.get("PURCHTYPE").toString().equals("03")) {
						djlx = "�г�Ͷ��";
						actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
						faccount = "2241.96";
					} else if (map.get("PURCHTYPE").toString().equals("04")) {
						djlx = "���÷ѱ���";
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
						djlx = "���⸶��";
						actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
						faccount = "2241.96";
					} else if (map.get("PURCHTYPE").toString().equals("06")) {
						djlx = "��ͬר�ø���";
						actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
						faccount = "2241.96";
					} else if (map.get("PURCHTYPE").toString().equals("07")) {
						djlx = "���ӹ�";
						actTypePk = "YW3xsAEJEADgAAVEwKgTB0c4VZA=";
						faccount = "2241.96";
					}

					// ��������
					info.put("yingfudanjuleixing", djlx);
					info.put("shifouguanlibumen", isAdminDept);
					info.put("fapiaohao", "OA0000"); // OA0000

					  
					// ���������� YW3xsAEJEADgAAVEwKgTB0c4VZA=
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
									info.getString("OA_OtherBill"), "��¼�ϵ�"+map.get("NUMBER")+"û�иù�Ӧ�̱���");// ��¼��־
							continue;
						}
					}

					VerificateBillTypeEnum billTypeEnum = VerificateBillTypeEnum.OtherPaymentBill;
					info.setSourceBillType(billTypeEnum);
					// ҵ������
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
								info.getString("OA_OtherBill"), "�ɹ����뵥����Ϊ"+map.get("FNUMBER")+"_"+map1.get("FNUMBER")+"������˳ɹ�");// ��¼��־
					} catch (Exception e2) {
						//System.out.println("����ɹ����ύ�������ʧ�ܣ�"+e2.getMessage());
						logger.error(e2.getMessage());
						AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
								DateBasetype.OA_OtherBill, info.getNumber(), 
								info.getString("OA_OtherBill"), "�ɹ����뵥����Ϊ"+map.get("FNUMBER")+"_"+map1.get("FNUMBER")+"���ݱ���ɹ�,�ύ���ʧ�ܡ�");// ��¼��־
					}
					/*
					if (addOrUpdate) {// �޸�
						AppUnit.insertLog(ctx, DateBaseProcessType.Update,
								DateBasetype.OA_OtherBill, info.getNumber(), 
								info.getString("OA_OtherBill"), "�����޸ĳɹ�");// ��¼��־
					} else {
						AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
								DateBasetype.OA_OtherBill, info.getNumber(), 
								info.getString("OA_OtherBill"), "���ݱ���ɹ�");// ��¼��־
					}*/
				}
				
			} else {
				System.out.println("entrty is empty _--------------------------------------"+ map.get("FNUMBER"));
				updateFSign(ctx, database, "eas_lolkk_bx", 2, map.get("ID").toString());
				AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,DateBasetype.OA_OtherBill, 
						map.get("FNUMBER").toString(), info.getString("OA_OtherBill"), "����û�з�¼");// ��¼��־
				return "";
			}
			//--------------------��¼����
			//��¼ȫ���������  �޸ı�ͷ״̬
			updateFSign(ctx, database, "eas_lolkk_bx", 1, map.get("ID").toString());
			
		} catch (EASBizException e) {
			e.printStackTrace();
			AppUnit.insertLog(ctx, DateBaseProcessType.AddNew,
					DateBasetype.OA_OtherBill, fid+"���ݱ���ʧ��", e.getMessage());// ��¼��־
			if (fid != null && !fid.equals("")) {
				updateFSign(ctx, database, "eas_lolkk_bx", 2, fid);
			}
			String msg = "����ʧ�ܣ��쳣�ǣ�" + e.toString();
			return msg;
			
		}
		
		return null;
	}

	
	
	/**
	 * ��eas_lolkk_bx���������˲����ڵ������޸�Ϊ״̬2
	 * @param ctx
	 * @param database
	 * @throws BOSException
	 */
	public void updateNoPeople(Context ctx, String database) throws BOSException {
		String updateSql = "UPDATE  eas_lolkk_bx  set eassign = 2 , EASTIME = TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS'),EASLOG='ְԱ������' "
				+" where ID in (select bx.id from eas_lolkk_bx bx left JOIN EAS_PERSON_MIDTABLE  person on person.FNUMBER = bx.APPLYER where bx.eassign = 0 and person.FNUMBER is null )";
		System.out.print("--------------" + updateSql);
		EAISynTemplate.execute(ctx, database, updateSql);
		
		updateSql = "update EAS_LOLKK_bx set EASSIGN = -1, EASTIME = TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS'),EASLOG='PAYTYPECODEΪ��' where id in (select DISTINCT PARENTID from EAS_LOLKK_BX_SUB where PAYTYPECODE is null ) and EASSIGN = 0";
		System.out.print("--------------" + updateSql);
		EAISynTemplate.execute(ctx, database, updateSql);
		
		updateSql = "update EAS_LOLKK_bx set EASSIGN = -2, EASTIME = TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS'),EASLOG='PAYTYPECODE������' where id in (select DISTINCT PARENTID from EAS_LOLKK_BX_SUB where PAYTYPECODE not in (select FNUMBER from EAS_PAYTYPE_OA_MIDTABLE)) and EASSIGN = 0";
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
						if( danjuType.equals("�г�Ͷ��") ){
							if(oanumber.indexOf("_")!= -1 && oanumber.split("_").length >1){
								// �޸ķ�¼�ϵĸ���״̬
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
					
					AppUnit.insertLog(ctx, DateBaseProcessType.Update,DateBasetype.OA_OtherBill,oanumber,oanumber,"�޸ĸ���״̬�ɹ�");
				}
				
			}
			
		} catch (BOSException e) {
			AppUnit.insertLog(ctx, DateBaseProcessType.Update,DateBasetype.OA_OtherBill,oanumber,oanumber,"�޸ĸ���״̬�쳣");
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
	 * �����ݲ� -- �ɹ����뵥
	 */
	@Override
	protected void _PurvspJDFromOA(Context ctx, String database)
			throws BOSException {
		VSPJDSupport.savePurRequest(ctx, database);
	}

	/*
	 *  �����ݲ� -- �ɹ��ջ���
	 * @see 
	 */
	@Override
	protected void _ReceConfirmVSPJD(Context ctx, String database)
			throws BOSException {
		VSPJDSupport.savePurReceivalBill(ctx, database);
	}  
	private static java.sql.Timestamp string2Time(String dateString) 
	  throws java.text.ParseException { 
	   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);//�趨��ʽ 
	   dateFormat.setLenient(false); 
	  java.util.Date timeDate = dateFormat.parse(dateString);//util���� 
	  java.sql.Timestamp dateTime = new java.sql.Timestamp(timeDate.getTime());//Timestamp����,timeDate.getTime()����һ��long�� 
	  return dateTime; 
	} 
	
	/** 
	  *method ���ַ������͵�����ת��Ϊһ��Date��java.sql.Date�� 
	   dateString ��Ҫת��ΪDate���ַ��� 
	   dataTime Date 
	  */ 
	private static java.sql.Date string2Date(String dateString) 
	  throws java.lang.Exception { 
	  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH); 
	  dateFormat.setLenient(false); 
	  java.util.Date timeDate = dateFormat.parse(dateString);//util���� 
	  java.sql.Date dateTime = new java.sql.Date(timeDate.getTime());//sql���� 
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
    		// ��ȡ����������λ
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
