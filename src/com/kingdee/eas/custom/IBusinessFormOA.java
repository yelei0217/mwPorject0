package com.kingdee.eas.custom;

import com.kingdee.bos.BOSException;
//import com.kingdee.bos.metadata.*;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.util.*;
import com.kingdee.bos.Context;

import com.kingdee.bos.Context;
import com.kingdee.bos.BOSException;
import java.lang.String;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.util.*;

public interface IBusinessFormOA extends IBizCtrl
{
    public String PurRequestFormOA(String database) throws BOSException;
    public String ApOtherFormOA(String database) throws BOSException;
    public String PayApplyToOA(String database, String billId) throws BOSException;
    public void syncPayApply(String database) throws BOSException;
    public void mobilePaymentBillBizDate(String ids, String date, String type) throws BOSException;
    public String syncPaymentBillFormOA(String database) throws BOSException;
    public String updateMidPayStatus() throws BOSException;
    public boolean IsExistDownstreamBill(String id) throws BOSException;
    public void PurvspJDFromOA(String database) throws BOSException;
    public void ReceConfirmVSPJD(String database) throws BOSException;
}