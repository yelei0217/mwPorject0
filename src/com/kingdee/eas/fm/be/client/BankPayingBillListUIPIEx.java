package com.kingdee.eas.fm.be.client;

import com.kingdee.eas.custom.util.UtilClass;
import com.kingdee.eas.util.SysUtil;
import com.kingdee.eas.util.client.MsgBox;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BankPayingBillListUIPIEx extends BankPayingBillListUI {
  public BankPayingBillListUIPIEx() throws Exception {
		super();
	}

private static final long serialVersionUID = -4339331345616692689L;
  
  public void actionSubmit_actionPerformed(ActionEvent e) throws Exception {
    checkSelected();
    Date nowData = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    String currentDate = formatter.format(nowData);
    boolean flag = true;
    List<String> idList = getSelectedIdValues();
    for (int j = 0; j < idList.size(); j++) {
      String billBizDate = getBizDateByBankPayingID(idList.get(j).toString());
      if (!"".equals(billBizDate) && !billBizDate.equals(currentDate)) {
        flag = false;
        break;
      } 
    } 
    if (flag) {
      super.actionSubmit_actionPerformed(e);
    } else {
      MsgBox.showInfo("银行付款单提交失败，付款单业务日期和当前日期不一致。");
      SysUtil.abort();
    } 
  }
  
  private String getBizDateByBankPayingID(String fid) {
    String bizDate = "";
    if (fid != null && !"".equals(fid))
      bizDate = UtilClass.executeQueryString("select  to_char(FBIZDATE,'yyyy-MM-dd') as FBIZDATE from T_CAS_PaymentBill where fid in (select FSOURCEBILLID from T_BE_BankPayingBill where fid ='" + fid + "')"); 
    return bizDate;
  }
}
