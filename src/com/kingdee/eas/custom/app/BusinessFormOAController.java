package com.kingdee.eas.custom.app;

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

import java.rmi.RemoteException;
import com.kingdee.bos.framework.ejb.BizController;

public interface BusinessFormOAController extends BizController
{
    public String PurRequestFormOA(Context ctx, String database) throws BOSException, RemoteException;
    public String ApOtherFormOA(Context ctx, String database) throws BOSException, RemoteException;
    public String PayApplyToOA(Context ctx, String database, String billId) throws BOSException, RemoteException;
    public void syncPayApply(Context ctx, String database) throws BOSException, RemoteException;
    public void mobilePaymentBillBizDate(Context ctx, String ids, String date, String type) throws BOSException, RemoteException;
    public String syncPaymentBillFormOA(Context ctx, String database) throws BOSException, RemoteException;
    public String updateMidPayStatus(Context ctx) throws BOSException, RemoteException;
    public boolean IsExistDownstreamBill(Context ctx, String id) throws BOSException, RemoteException;
    public void PurvspJDFromOA(Context ctx, String database) throws BOSException, RemoteException;
    public void ReceConfirmVSPJD(Context ctx, String database) throws BOSException, RemoteException;
}