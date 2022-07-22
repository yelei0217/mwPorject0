package com.kingdee.eas.custom.app;

import org.apache.log4j.Logger;
import javax.ejb.*;
import java.rmi.RemoteException;
import com.kingdee.bos.*;
import com.kingdee.bos.util.BOSObjectType;
import com.kingdee.bos.metadata.IMetaDataPK;
import com.kingdee.bos.metadata.rule.RuleExecutor;
import com.kingdee.bos.metadata.MetaDataPK;
//import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.framework.ejb.AbstractEntityControllerBean;
import com.kingdee.bos.framework.ejb.AbstractBizControllerBean;
//import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.bos.dao.IObjectCollection;
import com.kingdee.bos.service.ServiceContext;
import com.kingdee.bos.service.IServiceContext;

import com.kingdee.eas.basedata.master.cssp.SupplierInfo;
import com.kingdee.eas.basedata.master.material.MaterialInfo;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.framework.report.app.CommRptBaseControllerBean;
import com.kingdee.eas.framework.report.util.RptParams;
import com.kingdee.eas.util.app.DbUtil;
import com.kingdee.jdbc.rowset.IRowSet;
import com.kingdee.util.NumericExceptionSubItem;

public class SupplyCityFacadeControllerBean extends AbstractSupplyCityFacadeControllerBean
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3997897585898971877L;
	private static Logger logger = Logger.getLogger("com.kingdee.eas.custom.app.SupplyCityFacadeControllerBean");
    
	@Override
	public RptParams query(Context ctx, RptParams params) throws BOSException,EASBizException {
		  if( params != null){
			  if(params.getObject("materialfrom") != null){
				  MaterialInfo materialfrom = (MaterialInfo)params.getObject("materialfrom");
				  String mfn = materialfrom.getNumber();
				  String mtn = materialfrom.getNumber(); 
				 if(params.getObject("materialto")!=null){
					 MaterialInfo materialto = (MaterialInfo)params.getObject("materialto");
					 mtn = materialto.getNumber();
				 }
				  String sfn = "";
				  String stn = "";
				  if(params.getObject("supplierfrom")!=null){
					SupplierInfo supplierInfo = (SupplierInfo) params.getObject("supplierfrom");
					sfn =supplierInfo.getNumber();
				  }
		 
				  if(params.getObject("supplierto")!=null){
						SupplierInfo supplierInfo = (SupplierInfo) params.getObject("supplierto");
						stn =supplierInfo.getNumber();
				  }
				  StringBuffer sql = new StringBuffer();	
				  sql.append("/*dialect*/select distinct city.fnumber OrgNumber,city.fname_l2 OrgName,s.fnumber SupplierNumber,s.fname_l2 SupplierName,").append("\r\n");
				  sql.append("unit.fname_l2 UnitName,decode(sy.FPrice,999999,0,sy.FPrice) Price, decode(sy.FIsUseable,0,'保存',1,'核准','保存') Useable,m.FNumber MaterialNumber,").append("\r\n");
				  sql.append("sy.FID SFID,to_char(sy.FUNEFFECTUALDATE ,'yyyy-mm-dd hh24:mi:ss') as EndDate from t_sm_supplyinfo sy ").append("\r\n");
 				  sql.append("inner join eas_city_company cc on cc.comid = sy.fpurchaseorgid").append("\r\n");
				  sql.append("inner join T_ORG_Purchase org on  org.fid=cc.comid").append("\r\n");
				  sql.append("inner join T_ORG_Purchase city on city.fid = cc.cityid").append("\r\n");
				  sql.append("inner join T_BD_Supplier s on sy.fsupplierid = s.fid").append("\r\n");
				  sql.append("inner join t_BD_Material m on sy.FMaterialItemID = m.fid").append("\r\n");
				  sql.append("inner join T_BD_MeasureUnit unit on sy.FPurMeasureUnitID = unit.fid").append("\r\n");
				  sql.append("where m.fnumber >= '").append(mfn).append("' and  m.fnumber <= '").append(mtn).append("'");
				  
				  if(!"".equals(sfn) &&!"".equals(stn) ){
					  sql.append(" and s.fnumber >= '").append(sfn).append("' and  s.fnumber <= '").append(stn).append("'");
				  }
				  
				  IRowSet rs = DbUtil.executeQuery(ctx, sql.toString());
				  if(rs != null &&  rs.size() > 0)
					params.setObject("rs", rs); 
				  
			  }else{
				   throw new EASBizException(new NumericExceptionSubItem("0003","请先设置过滤条件.."));
			  }
		  }else{
			    throw new EASBizException(new NumericExceptionSubItem("0003","请先设置过滤条件.."));
		  }
		return params;
	}
}