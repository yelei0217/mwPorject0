package com.kingdee.eas.custom.app.unit;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.DateBaseLogFactory;
import com.kingdee.eas.custom.DateBaseLogInfo;
import com.kingdee.eas.custom.EAISynTemplate;
import com.kingdee.eas.custom.app.DateBaseProcessType;
import com.kingdee.eas.custom.app.DateBasetype;
import com.kingdee.eas.custom.util.VerifyUtil;
import com.kingdee.eas.util.app.DbUtil;
import com.kingdee.jdbc.rowset.IRowSet;
import com.kingdee.jdbc.rowset.IRowSetMetaData;

public class SupplyInfoChange {
	
	public static void SyncSupplyinfoToMidByIds(Context ctx , String dataBase, List<String> listIDs){
		int size = listIDs.size();
		StringBuffer ids = new StringBuffer("");
		for(int i = 1 ; i <= size ; i++){
			
			String id = listIDs.get(i-1);
			ids.append("'"+id+"',");
			if(i == size  || ( i%900 == 0)){//���һ��
				ids = new StringBuffer(ids.substring(0, ids.length()-1));
				SyncSupplyinfoToMidBy( ctx ,  dataBase, ids.toString());
				ids = new StringBuffer("");
			}  
		}
	} 
	public static  void SyncSupplyinfoToMidBy(Context ctx , String dataBase, String ids){
		StringBuffer sql = new StringBuffer();
		List<String> sqls = new ArrayList<String>();
		List<String> updateSqls = new ArrayList<String>(); 
		
		//��������-����-��Ӧ�̽��з��� ��ѯ��ʧЧ������������-����-��Ӧ�̵�Ʒ������� 
 
		sql.append("/*dialect*/ select  distinct supplyinfo.fid  SFID,(material.fnumber||'_'|| purchase2.FID || '_'||supplier.FNUMBER) as fid , purchase2.fid as csfid, ")
		.append(" purchase2.FNUMBER as ForgNumber ,purchase2.Fname_l2 as ForgName ,supplier.fname_l2 as FSupplierName,supplier.FNUMBER as supplierid ,supplier.fid as supplierkey , 0 as supplierzhukey ,material.fname_l2 as FMaterialName,")
		.append(" material.fnumber as FMaterialNumber,materialgroup.fnumber as fclassNumber, materialgroup.fname_l2 as FTypeName,material.FMODEL as format,material.CFXINGHAO as Model_number,material.CFHUOHAO  as Item_number,material.CFPINPAI as brand,")
		.append(" (CASE  WHEN supplyinfo.FUNEFFECTUALDATE > sysdate THEN 1   ELSE 0 END)  as FStatus ,measure.fname_l2  as unit,currency.fname_l2 as FCurrency,decode(supplyinfo.FPrice,999999,0,supplyinfo.FPrice) price,to_char(supplyinfo.FEFFECTUALDATE,'yyyy-mm-dd hh24:mi:ss') as FStateDate,")
		.append(" to_char(supplyinfo.FUNEFFECTUALDATE ,'yyyy-mm-dd hh24:mi:ss') as FEndDate ,0 as fSign, to_char(sysdate ,'yyyy-mm-dd hh24:mi:ss') as FCreateTime , 0 as FmailSign  ")
		.append(" from ( select  fid,FUNEFFECTUALDATE,FPRICE,FEFFECTUALDATE,FSUPPLIERID,FMATERIALITEMID,FCURRENCYID ,fisuseable,FPURCHASEORGID,FPurMeasureUnitID from  T_sm_supplyinfo where  fid in ("+ids+")  )  supplyinfo   ")
		.append(" inner join T_org_purchase purchase  on  purchase.FID = supplyinfo.FPURCHASEORGID")
		.append(" inner join T_org_purchase purchase2 on  purchase2.fid= purchase.FParentID and  purchase2.fiscu = 1 and purchase2.fisbizunit = 0  and purchase2.flevel = 2  ")
		.append(" inner join T_BD_supplier  supplier  on supplier.fid = supplyinfo.FSUPPLIERID  ")
		.append(" inner join t_bd_material  material  on material.fid = supplyinfo.FMATERIALITEMID ")
		.append(" inner join t_bd_measureunit measure on measure.fid  = supplyinfo.FPurMeasureUnitID ")
		.append(" inner join t_bd_currency  currency  on currency.fid = supplyinfo.FCURRENCYID ")
		.append(" inner join T_BD_MaterialGroup materialgroup on material.fmaterialgroupid = materialgroup.fid ");
		
		//GQC����
		//sql.append(" and material.fnumber in ('101683','101684','101685','101686','101687','101688','101689')");
		
		try {
			IRowSet rows = DbUtil.executeQuery(ctx, sql.toString());
			
			String tableName = "EAS_ORG_SupplyinfoMid";
			String queryStr = " select FID,CSFID,FORGNUMBER,FSupplierName,supplierid,FMaterialName,FMaterialNumber,format,Model_number,Item_number, brand,FStatus,price,SFID,UNIT, "+
					" to_char(FStateDate,'yyyy-mm-dd hh24:mi:ss') as FStateDate ,to_char(FEndDate ,'yyyy-mm-dd hh24:mi:ss') as FEndDate from  "+ tableName + " where SFID in ("+ids+")   ";
			//GQC����
		//	queryStr = queryStr+" and FMaterialNumber in ('101683','101684','101685','101686','101687','101688','101689')";
			
			List<Map<String, Object>> rets = EAISynTemplate.query(ctx,dataBase, queryStr);

			Map<String, Map<String, Object>> mapAll = new HashMap<String, Map<String, Object>>();
			Map<String, Map<String, Object>> newMapAll = new HashMap<String, Map<String, Object>>();
			for (int i = 0; i < rets.size(); i++) {
				mapAll.put((String) rets.get(i).get("FID"), rets.get(i));
				newMapAll.put((String) rets.get(i).get("FID"), rets.get(i));
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date nowDate = new Date();
				String lastStr = rets.get(i).get("FENDDATE").toString();
				String midstatus = rets.get(i).get("FSTATUS").toString();
				Date lastDate = df.parse(lastStr);//mid ��ʧЧʱ��
				if(lastDate.getTime() < nowDate.getTime() && midstatus.equals("1")){
					String updateSal = updateSuInMidStatus(ctx, DateBaseProcessType.DisAble,DateBasetype.SupplyinfoToMid, dataBase,rets.get(i).get("FMATERIALNUMBER").toString(),rets.get(i).get("CSFID").toString(),rets.get(i).get("FID").toString(), tableName);
					updateSqls.add(updateSal);
				}
				
			}
			//����ִ���޸�״̬����
			if(updateSqls!= null && updateSqls.size()>0){
				doInsertSqls(ctx, dataBase, updateSqls);
				updateSqls.clear();
			}
			
			while (rows.next()) { 
				String fid = rows.getString("fid");
				String materialNumber = rows.getString("FMaterialNumber");
				String cityid = rows.getString("CSFID");
				String supplierNumber = rows.getString("supplierid");
				BigDecimal 	price = new BigDecimal(rows.getString("price"));
				String fStatus = rows.getString("FStatus");
				String materialName = rows.getString("FMaterialName");
				String model   = rows.getString("format");
				String xinghao = rows.getString("Model_number");
				String huohao  = rows.getString("Item_number");
				String pinpai  = rows.getString("brand");

				String supplyinfoid  = rows.getString("SFID");
				
				String unit = rows.getString("unit");
					
				String statetimeStr = rows.getString("FStateDate");
				String endtimeStr   = rows.getString("FEndDate");
				
				Map<String,Object> mp = mapAll.get(fid);  //�м�������
				if (mp != null && mp.size() > 0) { 
					String upStr= "�޸ĵ��ֶ�Ϊ��";
					boolean upFlag = false;
					boolean flag = false;
					String  thisupdatesql  = "update "+tableName +" set ";
					// �޸��˼۸�  
					if ( !VerifyUtil.isNull((BigDecimal)mp.get("PRICE")) &&  price.compareTo(new BigDecimal(mp.get("PRICE").toString()))!= 0 ){
						//String updateSal = updateSuInMidPrice(ctx,DateBaseProcessType.Update,DateBasetype.SupplyinfoToMid, dataBase, materialNumber, price.toString(),fid ,0);
						//updateSqls.add(updateSal);
						String updateSal = updateSuInMid(ctx, price.toString(),"","" ,0);
						thisupdatesql = thisupdatesql+ updateSal;
						upFlag = true;
						upStr += "�۸�";
					} 
					
					if (  mp.get("SFID") == null  || !mp.get("SFID").equals(supplyinfoid) ) {
						//String updateSal = updateSuInMidPrice(ctx,DateBaseProcessType.Update,DateBasetype.SupplyinfoToMid, dataBase, materialNumber, supplyinfoid,fid ,1);
						//updateSqls.add(updateSal);
						String updateSal = updateSuInMid(ctx, supplyinfoid.toString(),"","" ,1);
						thisupdatesql = thisupdatesql+ updateSal;
						upFlag = true;
						upStr += "�����۸�id��";
					} 
					
					if ( mp.get("UNIT") == null  || !mp.get("UNIT").equals(unit) ) {
						String updateSal = updateSuInMid(ctx, unit,"","" ,5);
						thisupdatesql = thisupdatesql+ updateSal;
						upFlag = true;
						upStr += "������λ��";
					}
					
					String midstatus = mp.get("FSTATUS").toString();
					String lastDmp = mp.get("FENDDATE").toString();
					String stateDep = mp.get("FSTATEDATE").toString();
					
					SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					Date easEndDate = df.parse(endtimeStr); // ��ȡeas��ʧЧʱ��
					Date easStateDate = df.parse(statetimeStr); // ��ȡeas��ʧЧʱ��
					Date lastDate = df.parse(lastDmp);//mid ��ʧЧʱ��
					Date stateDate = df.parse(stateDep);//mid ����Чʱ��
					
					if (!VerifyUtil.isNull(mp.get("FENDDATE")) ) {
						//midͬ��eas��ʱ��  ��״̬
						if( (easStateDate.getTime() != stateDate.getTime()) || (easEndDate.getTime() != lastDate.getTime())){//ʧЧʱ�䲻һ��   midͬ��eas��ʱ�� 
						
							stateDate = easStateDate;
							lastDate = easEndDate;
							//String updateSal = updateSupplyInfoDate(ctx, DateBaseProcessType.ENABLE,DateBasetype.SupplyinfoToMid, dataBase, statetimeStr,endtimeStr, fid,rows.getString("FStatus"), tableName);
							//updateSqls.add(updateSal);
							upFlag = true;
							String updateSal = updateSuInMid(ctx, rows.getString("FStatus"),statetimeStr,endtimeStr ,2);
							thisupdatesql = thisupdatesql+ updateSal;
							upStr += "��Чʱ�䣻ʧЧʱ�䣻";
						}
						
						Date nowDate = new Date();
						if(lastDate.getTime() < nowDate.getTime()){// eas��mid��ʱ��һ��        mid��ʧЧʱ��С�ڵ�ǰʱ�� 
							if(mp.get("FSTATUS")!= null && midstatus.equals("1")){
							
								//String updatesql = updateSuInMidStatus(ctx, DateBaseProcessType.DisAble,DateBasetype.SupplyinfoToMid,dataBase, materialNumber,cityid, fid, tableName);
								//updateSqls.add(updatesql); 
								String updateSal = updateSuInMid(ctx,"","","" ,4);
								thisupdatesql = thisupdatesql+ updateSal;

								upFlag = true;
								upStr += "״̬Ϊ���ã�";
							}
						}else {//mid��ʧЧʱ����� ��ǰʱ�� 
							if(mp.get("FSTATUS")!= null && midstatus.equals("0") && rows.getString("FStatus").equals("1")){
							
								//����м���ǽ���״̬��eas������״̬
								//String updatesql = updateSuInMidStatus(ctx, DateBaseProcessType.ENABLE,DateBasetype.SupplyinfoToMid, dataBase, materialNumber,cityid, fid, tableName);
								//updateSqls.add(updatesql);
								
								String updateSal = updateSuInMid(ctx,"","","" ,3);
								thisupdatesql = thisupdatesql+ updateSal;
								upFlag = true;
								upStr += "״̬Ϊ���ã�";	
							}else if(mp.get("FSTATUS")!= null && midstatus.equals("1") && rows.getString("FStatus").equals("0")){
								//����м��������״̬��eas�ǽ���״̬
								//String updatesql = updateSuInMidStatus(ctx, DateBaseProcessType.DisAble,DateBasetype.SupplyinfoToMid, dataBase, materialNumber,cityid, fid, tableName);
								//updateSqls.add(updatesql);
								
								String updateSal = updateSuInMid(ctx,"","","" ,4);
								thisupdatesql = thisupdatesql+ updateSal;
								upFlag = true;
								upStr += "״̬Ϊ���ã�";	
							}
						}
					}
					if(upFlag){
						
						//thisupdatesql = thisupdatesql.substring(0, thisupdatesql.length()-1);
						thisupdatesql = thisupdatesql +  " fSign = 0 ,FUPDATETIME =  TO_DATE(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')  where   fid ='"+fid+"'";
						
						updateSqls.add(thisupdatesql); 
						insertLog(ctx, DateBaseProcessType.Update,DateBasetype.SupplyinfoToMid, "", fid,upStr);
					}
				} else {
					 
					//�鿴�Ƿ�����Ѿ���ʼ�����һ���ʹ��״̬�µ�����
//					String selectSuppSql = " /*dialect*/ select fid from eas_org_supplyinfomid  where fid != '"+fid+"' and  supplierzhukey = 0  and    csfid = '"+cityid+"' and FMaterialNumber ='"+materialNumber+"' and  FSTATUS = 1 and fenddate is not null  and  fenddate > sysdate";
//					List<Map<String, Object>> retsSup = EAISynTemplate.query(ctx,dataBase, selectSuppSql);
//					if(retsSup.size() == 0 ){//û�� 
//						if(fStatus.equals("1")){//��������Ϊ����
//							//��ʼ�������޸�Ϊ����
//							String overdueStatus = " /*dialect*/ update eas_org_supplyinfomid set  Fstatus = 0  , fsign=0, fupdatetime = sysdate  where fid='"+materialNumber+"_"+cityid+"' ";
//							EAISynTemplate.execute(ctx, dataBase, overdueStatus);
//						}
//					}
					//Ȼ�����һ���µ����ϳ��� ��Ӧ����Ϣ
					//String updateSql = updatePurchase(ctx, DateBaseProcessType.Update,DateBasetype.SupplyinfoToMid, dataBase, materialNumber,cityid,supplierNumber,fid);
					String sqlStr = insertSupplyinfoOneMidTable(ctx,DateBaseProcessType.AddNew, 
							DateBasetype.SupplyinfoToMid,dataBase, "EAS_ORG_SupplyinfoMid", rows);
					//String updateSql = updatePurchase(ctx, DateBaseProcessType.Update,DateBasetype.SupplyinfoToMid, dataBase,rows.getArray(i));
					sqls.add(sqlStr);
					//insertLog(ctx, DateBaseProcessType.Update,DateBasetype.SupplyinfoToMid, "", fid,"�޸ģ�ID���۸񣻹�Ӧ�̣��ұ����ڣ�");
					
				}
				newMapAll.remove(fid);
			}
			
			if(updateSqls!= null && updateSqls.size()>0){
				doInsertSqls(ctx, dataBase, updateSqls);
				updateSqls.clear();
			}
			
			//����û��ʧЧ���� �����Ѿ�������׼������  ����ͬ�� 
			for (Entry<String, Map<String, Object>> entry : newMapAll.entrySet()) { 
				System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()); 
				Map<String, Object> value = entry.getValue();
				
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date nowDate = new Date();
				String lastStr = value.get("FENDDATE").toString();
				String midstatus = value.get("FSTATUS").toString();
				Date lastDate = df.parse(lastStr);//mid ��ʧЧʱ��
				if(lastDate.getTime() > nowDate.getTime() && midstatus.equals("1")){
					String updateSql = updateSuInMidStatus(ctx, DateBaseProcessType.DisAble,DateBasetype.SupplyinfoToMid, dataBase,value.get("FMATERIALNUMBER").toString(),value.get("CSFID").toString(),value.get("FID").toString(), tableName);
					updateSqls.add(updateSql);
				}
				  
			}
			if(updateSqls!= null && updateSqls.size()>0){
				doInsertSqls(ctx, dataBase, updateSqls);
				updateSqls.clear();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (sqls.size() > 0) {
			doInsertSqls(ctx, dataBase, sqls);
			insertLog(ctx, DateBaseProcessType.AddNew,DateBasetype.SupplyinfoToMid, "", "","�������й�Ӧ�̵�����:"+sqls.size()+"��");
			//EAISynTemplate.executeBatch(ctx, dataBase, sqls);
		}
	}
	
	public static String updateSuInMidStatus(Context ctx, DateBaseProcessType processType,
			DateBasetype baseType, String dataBase, String number, String name,  
			String id, String tableName) throws BOSException {
		if (VerifyUtil.notNull(dataBase) && VerifyUtil.notNull(number)&& VerifyUtil.notNull(name) && VerifyUtil.notNull(id)) {
			StringBuffer updateSql = new StringBuffer("UPDATE  " + tableName);

			if (baseType == DateBasetype.SupplyinfoToMid) {// �۸�ά��
				if (processType == DateBaseProcessType.ENABLE) {// ����  FSTATEDATE = to_timestamp('"+number+"','yyyy-mm-dd hh24:mi:ss') , FENDDATE = to_timestamp('"+name+"','yyyy-mm-dd hh24:mi:ss'),
					//����ʼ�����ݽ���
					//String overdueStatus = " /*dialect*/ update eas_org_supplyinfomid set  Fstatus = 0  , fsign=0, fupdatetime = sysdate  where fid='"+number+"_"+name+"' ";
					//EAISynTemplate.execute(ctx, dataBase, overdueStatus);
					
					updateSql.append("  set  FSTATUS = 1,  fSign = 0 ,FUPDATETIME =  TO_DATE(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss') where fid='"+ id + "'");
				} else if (processType == DateBaseProcessType.DisAble) {// ����
					//�ж���û���������ɹ�Ӧ������  
					//String selectSuppSql = " /*dialect*/ select fid from eas_org_supplyinfomid  where fid != '"+id+"' and  supplierzhukey = 0  and    csfid = '"+name+"' and FMaterialNumber ='"+number+"' and  FSTATUS = 1 and fenddate is not null  and  fenddate > sysdate";
					//List<Map<String, Object>> rets = EAISynTemplate.query(ctx,dataBase, selectSuppSql);
					//if(rets.size() > 0 ){//�� ��ʼ�����ݽ���
						//String overdueStatus = " /*dialect*/ update eas_org_supplyinfomid set  Fstatus = 0  , fsign=0, fupdatetime = sysdate  where fid='"+number+"_"+name+"' ";
						//EAISynTemplate.execute(ctx, dataBase, overdueStatus);
					//}else {//û��  ��ʼ����������  Ҳ������
						//--20201210 String overdueStatus = " /*dialect*/ update eas_org_supplyinfomid set  Fstatus = 1  , fsign=0, fupdatetime = sysdate  where fid='"+number+"_"+name+"' ";
						//--20201210 EAISynTemplate.execute(ctx, dataBase, overdueStatus);
					//}
					
					updateSql.append("  set  FSTATUS = 0,  fSign = 0 ,FUPDATETIME =  TO_DATE(to_char(sysdate,'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')  where fid='"
									+ id + "'");
				}
				
				
			}

			//EAISynTemplate.execute(ctx, dataBase, updateSql.toString());
			//insertLog(ctx, processType, baseType, name, number);
			return updateSql.toString();
		}
		return "";
	}
	private static void doInsertSqls(Context ctx, String dataBase,List<String> sqls){
		try {
			int size = sqls.size();
			int qian = (int)Math.ceil(size/10000);
			if(size%10000 >0){
				qian ++;
			}
			for(int i = 0 ; i < qian ; i++){
				List<String> sumSqls =  new ArrayList<String>();
			
				if(size < (i+1)*10000 ){
					sumSqls = sqls.subList(i*10000, size);
				}else{
					sumSqls = sqls.subList(i*10000, (i+1)*10000);
				}
				EAISynTemplate.executeBatch(ctx, dataBase, sumSqls);
			
			}
		} catch (BOSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	
	public static String updateSuInMid(Context ctx,  String value,String value2,String value3, int type ) throws BOSException {
		StringBuffer updateSql = new StringBuffer(" " );
		if(type == 0){ 
			updateSql.append("  PRICE = "+value+","); 
		}else if(type == 1){
			updateSql.append("  SFID = '"+value+"',");
		}else if(type == 2){
			updateSql.append("  FSTATEDATE = to_timestamp('"+value2+"','yyyy-mm-dd hh24:mi:ss') , "+
					" FENDDATE = to_timestamp('"+value3+"','yyyy-mm-dd hh24:mi:ss') ,");
		}else if (type == 3) {//ENABLE
			updateSql.append("   FSTATUS = 1 ,");
		} else if (type == 4) {//DisAble ���� 
			updateSql.append("   FSTATUS = 0 ,");
		} else if(type == 5){
			updateSql.append("  UNIT = '"+value+"',");
		}
		
		return updateSql.toString();
	}
	
	/**
	 * һ�δ���һ������,����һ�����
	 * 
	 * @param ctx
	 * @param processType
	 * @param baseType
	 * @param dataBase
	 * @param tableName
	 * @param rows
	 */
	private static String insertSupplyinfoOneMidTable(Context ctx,
			DateBaseProcessType processType, DateBasetype baseType,
			String dataBase, String tableName, IRowSet rows) {
		String insertSql = "";
		try {
			IRowSetMetaData rowSetMataData = rows.getRowSetMetaData();
			int columnsSize = rowSetMataData.getColumnCount();
			String insertSqlStart = "INSERT INTO " + tableName + "(";
			String insertSqlValues = "";
			for (int i = 1; i <= columnsSize; i++) {
				String columnName = rowSetMataData.getColumnName(i);
				String value = rows.getString(columnName);
				if(value == null){
					value = "";
				}
				if (columnName.equals("FMATERIALNAME") && value.contains("'")) {
					value = value.replace("'", "''");
				}
				if (columnName.equals("FORMAT") && value.contains("'")) {
					value = value.replace("'", "''");
				}
				if (columnName.equals("MODEL_NUMBER") && value.contains("'")) {
					value = value.replace("'", "''");
				}
				if (columnName.equals("BRAND") && value.contains("'")) {
					value = value.replace("'", "''");
				}
				if (columnName.equals("ITEM_NUMBER") && value.contains("'")) {
					value = value.replace("'", "''");
				}
				if (i == columnsSize) {
					insertSqlStart += columnName + " ";
					if (columnName.toLowerCase().endsWith("time") || columnName.toLowerCase().endsWith("date")) {
						// insertSqlValues += value+"','";
						if(value != null && !value.equals("")){
							insertSqlValues += "to_date('" + value
							+ "','yyyy-mm-dd hh24:mi:ss')" + " ";
						}else{
							insertSqlValues += " null ";
						}					
					} else {
						if (null == value) {
							insertSqlValues += "'0' ";
						} else {
							insertSqlValues += "'" + value + "' ";
						}
					}
				} else {
					insertSqlStart += columnName + ",";
					if (columnName.toLowerCase().endsWith("time") || columnName.toLowerCase().endsWith("date")) {
						// insertSqlValues += value+"','";
						if(value != null && !value.equals("")){
							insertSqlValues += "to_date('" + value
							+ "','yyyy-mm-dd hh24:mi:ss')" + ",";
						}else{
							insertSqlValues += " null ";
						}
					} else {
						if (null == value) {
							insertSqlValues += "'0',";
						} else {
							insertSqlValues += "'" + value + "',";
						}
					}
				}
			}
			// insertSqlStart +="FupdateType) VALUES(";
			// insertSqlValues +="0)";
			insertSqlStart += " ) VALUES(";
			insertSqlValues += " )";
			insertSql = insertSqlStart + insertSqlValues;
			System.out.print("**************sql=" + insertSql);
			// EAISynTemplate.execute(ctx, dataBase, insertSql);
			//insertLog(ctx, processType, baseType, name, number,"����");
			return insertSql;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return insertSql;
	}
	
	private static void insertLog(Context ctx, DateBaseProcessType processType,
			DateBasetype baseType, String name, String number,String miaoshu) {
		try {
			DateBaseLogInfo loginfo = new DateBaseLogInfo();
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			String version = String.valueOf(cal.getTimeInMillis());
			loginfo.setProcessType(processType);
			loginfo.setNumber(cal.getTimeInMillis() + "." + number);
			// loginfo.setName(name);
			loginfo.setSimpleName(number);
			loginfo.setDateBaseType(baseType);
			loginfo.setVersion(version);
			loginfo.setUpdateDate(new Date());
			loginfo.setStatus(true);
			loginfo.setDescription(miaoshu);
			DateBaseLogFactory.getLocalInstance(ctx).save(loginfo);
		} catch (EASBizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BOSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
