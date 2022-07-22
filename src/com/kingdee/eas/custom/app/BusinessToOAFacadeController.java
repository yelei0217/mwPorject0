package com.kingdee.eas.custom.app;

import com.kingdee.bos.BOSException;
//import com.kingdee.bos.metadata.*;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.util.*;
import com.kingdee.bos.Context;

import com.kingdee.bos.Context;
import com.kingdee.bos.BOSException;
import java.util.List;
import java.lang.String;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.util.*;

import java.rmi.RemoteException;
import com.kingdee.bos.framework.ejb.BizController;

public interface BusinessToOAFacadeController extends BizController
{
    public void updateMaterialInfo(Context ctx, String mId) throws BOSException, RemoteException;
    public void syncSupplyInfoAuto(Context ctx) throws BOSException, RemoteException;
    public void disableSupplyInfo(Context ctx, List ids) throws BOSException, RemoteException;
    public void ableSupplyInfo(Context ctx, List ids) throws BOSException, RemoteException;
}