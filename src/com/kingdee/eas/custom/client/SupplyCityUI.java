/**
 * output package name
 */
package com.kingdee.eas.custom.client;

import java.awt.event.*;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.ui.face.CoreUIObject;
import com.kingdee.bos.ctrl.kdf.table.IRow;
import com.kingdee.bos.ctrl.kdf.table.KDTSelectManager;
import com.kingdee.bos.ctrl.kdf.table.KDTable;
import com.kingdee.bos.ctrl.kdf.table.util.KDTableUtil;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.BusinessToOAFacadeFactory;
import com.kingdee.eas.custom.SupplyCityFacadeFactory;
import com.kingdee.eas.fi.gl.GlUtils;
import com.kingdee.eas.fi.gl.client.InitClientHelp;
import com.kingdee.eas.framework.*;
import com.kingdee.eas.framework.report.ICommRptBase;
import com.kingdee.eas.framework.report.client.CommRptBaseConditionUI;
import com.kingdee.eas.framework.report.util.RptParams;
import com.kingdee.jdbc.rowset.IRowSet;

/**
 * output class name
 */
public class SupplyCityUI extends AbstractSupplyCityUI
{
    private static final Logger logger = CoreUIObject.getLogger(SupplyCityUI.class);

    /**
     * output class constructor
     */
    public SupplyCityUI() throws Exception
    {
        super();
        this.setUITitle("物料分配城市报表");
        this.kDTable1.getSelectManager().setSelectMode(KDTSelectManager.MULTIPLE_ROW_SELECT);
    }
    @Override
    public void onLoad() throws Exception {
    	// TODO Auto-generated method stub
    	super.onLoad();
        this.kDTable1.getSelectManager().setSelectMode(KDTSelectManager.MULTIPLE_ROW_SELECT);
        this.btnDisable.setEnabled(true);
        this.btnAble.setEnabled(true);
    }

    /**
     * output storeFields method
     */
    public void storeFields()
    {
        super.storeFields();

    }
    
	@Override
	protected RptParams getParamsForInit() {
 		return params;
	}

	@Override
	protected CommRptBaseConditionUI getQueryDialogUserPanel() throws Exception {
 		return new SupplyCityFilterUI();
	}

	@Override
	protected ICommRptBase getRemoteInstance() throws BOSException {
 		return SupplyCityFacadeFactory.getRemoteInstance();
	}

	@Override
	protected KDTable getTableForPrintSetting() {
 		return this.kDTable1;
	}

	@Override
	protected void query() {
		//查询条件设置到界面
		kDTable1.checkParsed();
		kDTable1.setEditable(false);
		
		//设置光标处于等待状态
		setCursorOfWair();
		 //查询数据,在Facade查询方法里返回值是RptParams，需要把查询集保存到RptParams里，再通过界面取出即可
		 try {
			RptParams rps = ((ICommRptBase)getRemoteInstance()).query(params);
			 if(rps!=null){
				 IRowSet conReport =  ((IRowSet)rps.getObject("rs")) ;
				 if(conReport!=null){
					fillTable(conReport);
					kDTable1.getSelectManager().setSelectMode(KDTSelectManager.MULTIPLE_ROW_SELECT);
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

	private int fillTable(IRowSet rs) throws SQLException {
		kDTable1.removeRows();
	    String amountFormat = GlUtils.getDecimalFormatString();
	    this.kDTable1.getColumn("Price").getStyleAttributes().setNumberFormat(amountFormat);
	    Date date = new Date();
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	 
		while (rs.next()) {
		    IRow row = kDTable1.addRow();
		    
		    if(rs.getObject("SFID") !=null && !"".equals(rs.getObject("SFID").toString())){
				row.getCell("SFID").setValue(rs.getObject("SFID").toString());
			}else{
				row.getCell("SFID").setValue("");
			}
		    
		    if(rs.getObject("OrgNumber") !=null && !"".equals(rs.getObject("ORGNUMBER").toString())){
				row.getCell("OrgNumber").setValue(rs.getObject("ORGNUMBER").toString());
			}else{
				row.getCell("OrgNumber").setValue("");
			}
		    
		    if(rs.getObject("ORGNAME") !=null && !"".equals(rs.getObject("ORGNAME").toString())){
				row.getCell("OrgName").setValue(rs.getObject("ORGNAME").toString());
			}else{
				row.getCell("OrgName").setValue("");
			}
		    
		    
		    if(rs.getObject("SUPPLIERNUMBER") !=null && !"".equals(rs.getObject("SUPPLIERNUMBER").toString())){
				row.getCell("SupplierNumber").setValue(rs.getObject("SUPPLIERNUMBER").toString());
			}else{
				row.getCell("SupplierNumber").setValue("");
			}
		    
		    if(rs.getObject("SUPPLIERNAME") !=null && !"".equals(rs.getObject("SUPPLIERNAME").toString())){
				row.getCell("SupplierName").setValue(rs.getObject("SUPPLIERNAME").toString());
			}else{
				row.getCell("SupplierName").setValue("");
			}
		    
		    
		    if(rs.getObject("UNITNAME") !=null && !"".equals(rs.getObject("UNITNAME").toString())){
				row.getCell("UnitName").setValue(rs.getObject("UNITNAME").toString());
			}else{
				row.getCell("UnitName").setValue("");
			}
		    
		    
		    if(rs.getObject("USEABLE") !=null && !"".equals(rs.getObject("USEABLE").toString())){
				row.getCell("Useable").setValue(rs.getObject("USEABLE").toString());
			}else{
				row.getCell("Useable").setValue("");
			}
		    
		     Object Price = rs.getObject("PRICE");
		      if ((Price != null) && (getFormateAmount(Price, 2).compareTo(InitClientHelp.zero) != 0)) {
		        row.getCell("Price").setValue(getFormateAmount(Price, 2));
		      }
		    
		      
			    if(rs.getObject("MATERIALNUMBER") !=null && !"".equals(rs.getObject("MATERIALNUMBER").toString())){
					row.getCell("MaterialNumber").setValue(rs.getObject("MATERIALNUMBER").toString());
				}else{
					row.getCell("MaterialNumber").setValue("");
				}
			    
			    if(rs.getObject("ENDDATE") !=null && !"".equals(rs.getObject("ENDDATE").toString())){
			    	try {
							Date date2 = format.parse(rs.getObject("ENDDATE").toString());
							if(date.getTime() >= date2.getTime()){ 
								row.getCell("Status").setValue("禁用");
							}else{
								row.getCell("Status").setValue("启用");
							}
					} catch (ParseException e) {
 						e.printStackTrace();
					}
				 
				}else{
					row.getCell("Status").setValue("");
				}
			    
		}
		return kDTable1.getRowCount();
	}
	
	  private BigDecimal getFormateAmount(Object data, int precision)
	  {
	    InitClientHelp.ScaleHelp scaleHelp = new InitClientHelp.ScaleHelp();
	    scaleHelp.setScale(precision);
	    return scaleHelp.getScaleBigDecimal(data);
	  }

	@Override
	public void actoinDisable_actionPerformed(ActionEvent e) throws Exception {
  		List list = new ArrayList();
		int[] selectInt = KDTableUtil.getSelectedRows(kDTable1);
		for(int j = 0 ; j < selectInt.length ; j++){
			this.kDTable1.getRow(selectInt[j]).getCell("SFID");
			if(this.kDTable1.getRow(selectInt[j]).getCell("SFID") !=null && !"".equals(this.kDTable1.getRow(selectInt[j]).getCell("SFID").toString()))
				list.add(this.kDTable1.getRow(selectInt[j]).getCell("SFID").getValue().toString());
		}
		if(list != null && list.size() > 0)
			 BusinessToOAFacadeFactory.getRemoteInstance().disableSupplyInfo(list);
		this.onLoad();
	}
	
	@Override
	public void actionAble_actionPerformed(ActionEvent e) throws Exception {
  		List list = new ArrayList();
		int[] selectInt = KDTableUtil.getSelectedRows(kDTable1);
		for(int j = 0 ; j < selectInt.length ; j++){
			this.kDTable1.getRow(selectInt[j]).getCell("SFID");
			if(this.kDTable1.getRow(selectInt[j]).getCell("SFID") !=null && !"".equals(this.kDTable1.getRow(selectInt[j]).getCell("SFID").toString()))
				list.add(this.kDTable1.getRow(selectInt[j]).getCell("SFID").getValue().toString());
		}
		if(list != null && list.size() > 0)
			 BusinessToOAFacadeFactory.getRemoteInstance().ableSupplyInfo(list);
		this.onLoad();
	}
	  
	  
	  
}