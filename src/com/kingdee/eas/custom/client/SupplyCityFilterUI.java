/**
 * output package name
 */
package com.kingdee.eas.custom.client;

import java.util.Map;

import org.apache.log4j.Logger;
import com.kingdee.bos.ui.face.CoreUIObject;
import com.kingdee.eas.basedata.master.cssp.SupplierInfo;
import com.kingdee.eas.basedata.master.material.MaterialInfo;
import com.kingdee.eas.framework.report.util.RptConditionManager;
import com.kingdee.eas.framework.report.util.RptParams;
import com.kingdee.eas.scm.common.filter.CompositeFilterElement;
import com.kingdee.eas.scm.common.filter.RangeFilterElement;
import com.kingdee.eas.scm.sm.pur.util.PurUtil;
import com.kingdee.eas.util.SysUtil;
import com.kingdee.eas.util.client.MsgBox;

/**
 * output class name
 */
public class SupplyCityFilterUI extends AbstractSupplyCityFilterUI
{
    private static final Logger logger = CoreUIObject.getLogger(SupplyCityFilterUI.class);
    
    /**
     * output class constructor
     */
    public SupplyCityFilterUI() throws Exception
    {
        super();
        
    	CompositeFilterElement elements = new CompositeFilterElement(0);
	//	SingleFilterElement mainOrgElement = new SingleFilterElement("purchaseOrg.number", this.prmtPurchaseOrgFrom);
	//	elements.add(mainOrgElement);
//		getFilterManager().setMainOrgElement(mainOrgElement, "purchaseOrg.id");
		elements.add(new RangeFilterElement("materialItem.number",
				this.prmtMaterialNumFrom, this.prmtMaterialNumTo));
		elements.add(new RangeFilterElement("supplier.number",
				this.prmtSupplierNumFrom, this.prmtSupplierNumTo));

	//	getFilterManager().setElement(elements);

//		setBizMaterialF7(this.prmtMaterialNumFrom, false, null);
//		setBizMaterialF7(this.prmtMaterialNumTo, false, null);
//		setBizSupplierF7(this.prmtSupplierNumFrom, null);
//		setBizSupplierF7(this.prmtSupplierNumTo, null);

//		setBizOrgF7ByType(this.prmtPurchaseOrgFrom, OrgType.Purchase,getMainBizOrgType(), true);
//		this.prmtPurchaseOrgFrom.setEnabledMultiSelection(false);
		
//		setNeedMainOrgF7s(new KDBizPromptBox[] { this.prmtMaterialNumFrom,
//				this.prmtMaterialNumTo, this.prmtSupplierNumFrom,
//				this.prmtSupplierNumTo });
	 

		this.prmtSupplierNumFrom.setEditable(true);
		this.prmtSupplierNumTo.setEditable(true);
		this.prmtSupplierNumFrom.setCommitFormat("$number$");
		this.prmtSupplierNumTo.setCommitFormat("$number$");
		this.prmtMaterialNumFrom.setCommitFormat("$number$");
		this.prmtMaterialNumTo.setCommitFormat("$number$");

		PurUtil.setF7RangeChange(this.prmtSupplierNumFrom,
				this.prmtSupplierNumTo);
		PurUtil.setF7RangeChange(this.prmtMaterialNumFrom,
				this.prmtMaterialNumTo);
    }
    
 

    @Override
    public void onLoad() throws Exception {
    	// TODO Auto-generated method stub
    	super.onLoad();
    	
    	
    	
    }
    /**
     * output storeFields method
     */
    public void storeFields()
    {
        super.storeFields();
    }

	@Override
	public RptParams getCustomCondition() {
        RptParams param = new RptParams();
        // 放置本界面的参数
        RptConditionManager rcm = new RptConditionManager();
        if(null != this.prmtMaterialNumFrom.getValue() ){
        	MaterialInfo materialfrom = (MaterialInfo)prmtMaterialNumFrom.getValue();
        	rcm.setProperty("materialfrom",materialfrom);
        }else{
        	rcm.setProperty("materialfrom", null);
        }
        
        if(null != this.prmtMaterialNumTo.getValue() ){
      	  MaterialInfo materialto = (MaterialInfo)prmtMaterialNumTo.getValue();
      	  rcm.setProperty("materialto",materialto);
	      }else{
	      	rcm.setProperty("materialto", null);
	      }
       
        if(null != this.prmtSupplierNumFrom.getValue() ){
        	SupplierInfo supplierfrom = (SupplierInfo)prmtSupplierNumFrom.getValue();
      	  rcm.setProperty("supplierfrom",supplierfrom);
	      }else{
	      	rcm.setProperty("supplierfrom", null);
	      }
      
        if(null != this.prmtSupplierNumTo.getValue() ){
        	SupplierInfo supplierto = (SupplierInfo)prmtSupplierNumTo.getValue();
    	  rcm.setProperty("supplierto",supplierto);
	      }else{
	      	rcm.setProperty("supplierto", null);
	      }
      
        
        Map map = rcm.toMap();
        param.putAll(map);
        return param;
	}

	@Override
	public void onInit(RptParams param) throws Exception {
		
	}

	@Override
	public void setCustomCondition(RptParams param) {
		if(null != param.getObject("materialfrom")){
			this.prmtMaterialNumFrom.setValue(param.getObject("materialfrom"));
		}
		if(null != param.getObject("materialto")){
			this.prmtMaterialNumTo.setValue(param.getObject("materialto"));
		}
	}

	@Override
	public boolean verify() {
		if(null ==  this.prmtMaterialNumFrom.getValue()){
		     MsgBox.showError("请选择物料.");
             SysUtil.abort();
		}
 		return super.verify();
	}

	
}