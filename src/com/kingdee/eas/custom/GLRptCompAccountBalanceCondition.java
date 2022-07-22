package com.kingdee.eas.custom;

import java.util.Map;
import com.kingdee.eas.fi.gl.rpt.GLRptBaseCondition;

public class GLRptCompAccountBalanceCondition extends GLRptBaseCondition {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3799127466888144927L;
    private boolean displayAsstDetail;
    private boolean optionYearAmountZero;
    private boolean optionYearAmountAndBalZero;
    private boolean optionShowLeafAccount;
    private boolean includeBWAccount;
    private boolean optionDCDispatchAsst;
    private boolean isDisplayLeafCompany;
    
	public boolean isDisplayAsstDetail() {
		return displayAsstDetail;
	}
	public void setDisplayAsstDetail(boolean displayAsstDetail) {
		this.displayAsstDetail = displayAsstDetail;
	}
	public boolean isOptionYearAmountZero() {
		return optionYearAmountZero;
	}
	public void setOptionYearAmountZero(boolean optionYearAmountZero) {
		this.optionYearAmountZero = optionYearAmountZero;
	}
	public boolean isOptionYearAmountAndBalZero() {
		return optionYearAmountAndBalZero;
	}
	public void setOptionYearAmountAndBalZero(boolean optionYearAmountAndBalZero) {
		this.optionYearAmountAndBalZero = optionYearAmountAndBalZero;
	}
	public boolean isOptionShowLeafAccount() {
		return optionShowLeafAccount;
	}
	public void setOptionShowLeafAccount(boolean optionShowLeafAccount) {
		this.optionShowLeafAccount = optionShowLeafAccount;
	}
	public boolean isIncludeBWAccount() {
		return includeBWAccount;
	}
	public void setIncludeBWAccount(boolean includeBWAccount) {
		this.includeBWAccount = includeBWAccount;
	}
	public boolean isOptionDCDispatchAsst() {
		return optionDCDispatchAsst;
	}
	public void setOptionDCDispatchAsst(boolean optionDCDispatchAsst) {
		this.optionDCDispatchAsst = optionDCDispatchAsst;
	}
	public boolean isDisplayLeafCompany() {
		return isDisplayLeafCompany;
	}
	public void setDisplayLeafCompany(boolean isDisplayLeafCompany) {
		this.isDisplayLeafCompany = isDisplayLeafCompany;
	}
    
    
    public GLRptCompAccountBalanceCondition(){
        this.displayAsstDetail = false;
        this.optionYearAmountZero = false;
        this.optionYearAmountAndBalZero = false;
        this.optionShowLeafAccount = false;
        this.includeBWAccount = false;
        this.optionDCDispatchAsst = false;
        this.isDisplayLeafCompany = false;
    }
    
    public GLRptCompAccountBalanceCondition(GLRptBaseCondition base){
    	super(base);
        this.displayAsstDetail = false;
        this.optionYearAmountZero = false;
        this.optionYearAmountAndBalZero = false;
        this.optionShowLeafAccount = false;
        this.includeBWAccount = false;
        this.optionDCDispatchAsst = false;
        this.isDisplayLeafCompany = false;
    }
    public GLRptCompAccountBalanceCondition(Map map) throws CloneNotSupportedException {
        super(map);
        this.displayAsstDetail = false;
        this.optionYearAmountZero = false;
        this.optionYearAmountAndBalZero = false;
        this.optionShowLeafAccount = false;
        this.includeBWAccount = false;
        this.optionDCDispatchAsst = false;
        this.isDisplayLeafCompany = false;
        this.setDisplayAsstDetail(Boolean.parseBoolean(map.get("chkDisplayAsstDetail").toString()));
        this.setOptionYearAmountZero(Boolean.parseBoolean(map.get("OptionYearAmountZero").toString()));
        this.setOptionYearAmountAndBalZero(Boolean.parseBoolean(map.get("optionYearAmountAndBalZero").toString()));
        this.setOptionDCDispatchAsst(Boolean.parseBoolean(map.get("optionDCDispatchAsst").toString()));
        this.setShowLeafAccount(Boolean.parseBoolean(map.get("optionShowLeafAccount").toString()));
        this.setTreeModelOfCompany(Boolean.parseBoolean(map.get("treeModelOfCompany").toString()));
        this.setIncludeBWAccount(Boolean.parseBoolean(map.get("IncludeBWAccount").toString()));
        this.setDisplayLeafCompany(Boolean.parseBoolean(map.get("isDisplayLeafCompany").toString()));
    }
    @Override
    public Map toMap() {
        final Map map = super.toMap();
        map.put("chkDisplayAsstDetail", this.isDisplayAsstDetail());
        map.put("OptionYearAmountZero", this.isOptionYearAmountZero());
        map.put("optionYearAmountAndBalZero", this.isOptionYearAmountAndBalZero());
        map.put("optionShowLeafAccount", this.isShowLeafAccount());
        map.put("IncludeBWAccount", this.isIncludeBWAccount());
        map.put("optionDCDispatchAsst", this.isOptionDCDispatchAsst());
        map.put("isDisplayLeafCompany", this.isDisplayLeafCompany());
        return map;
    }
    
    
    @Override
    public boolean isShowLeafAccount() {
        return this.optionShowLeafAccount;
    }
    
    @Override
    public void setShowLeafAccount(final boolean showLeafAccount) {
        this.optionShowLeafAccount = showLeafAccount;
    }
    
    
}
