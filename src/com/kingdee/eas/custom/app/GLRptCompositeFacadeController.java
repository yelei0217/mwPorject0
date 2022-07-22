package com.kingdee.eas.custom.app;

import com.kingdee.bos.BOSException;
//import com.kingdee.bos.metadata.*;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.util.*;
import com.kingdee.bos.Context;

import com.kingdee.bos.Context;
import com.kingdee.eas.fi.gl.rpt.GLRptQueryInitData;
import com.kingdee.bos.BOSException;
import java.util.Map;
import java.util.List;
import java.lang.String;
import java.util.Set;
import com.kingdee.bos.framework.*;
import com.kingdee.eas.framework.report.app.CommRptBaseController;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.bos.util.*;

import java.rmi.RemoteException;
import com.kingdee.bos.framework.ejb.BizController;

public interface GLRptCompositeFacadeController extends CommRptBaseController
{
    public GLRptQueryInitData getQueryInitData(Context ctx, boolean isSupportUnion) throws BOSException, EASBizException, RemoteException;
    public GLRptQueryInitData getQueryInitData(Context ctx, List companyIds, boolean isSupportUnion) throws BOSException, EASBizException, RemoteException;
    public int expendRow(Context ctx, String queryId, int rowIndex) throws BOSException, EASBizException, RemoteException;
    public int expendAllRow(Context ctx, String queryId, int rowIndex) throws BOSException, EASBizException, RemoteException;
    public int shrinkRow(Context ctx, String queryId, int rowIndex) throws BOSException, EASBizException, RemoteException;
    public void relaseDataSource(Context ctx, String queryId) throws BOSException, RemoteException;
    public Map getPeriodRange(Context ctx, Set companyIdSet, String periodTypeId) throws BOSException, EASBizException, RemoteException;
}