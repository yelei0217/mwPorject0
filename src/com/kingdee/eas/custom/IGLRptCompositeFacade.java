package com.kingdee.eas.custom;

import com.kingdee.bos.BOSException;
//import com.kingdee.bos.metadata.*;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.util.*;
import com.kingdee.bos.Context;

import com.kingdee.bos.Context;
import com.kingdee.eas.fi.gl.rpt.GLRptQueryInitData;
import com.kingdee.bos.BOSException;
import java.util.Map;
import com.kingdee.eas.framework.report.ICommRptBase;
import java.util.List;
import java.lang.String;
import java.util.Set;
import com.kingdee.bos.framework.*;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.bos.util.*;

public interface IGLRptCompositeFacade extends ICommRptBase
{
    public GLRptQueryInitData getQueryInitData(boolean isSupportUnion) throws BOSException, EASBizException;
    public GLRptQueryInitData getQueryInitData(List companyIds, boolean isSupportUnion) throws BOSException, EASBizException;
    public int expendRow(String queryId, int rowIndex) throws BOSException, EASBizException;
    public int expendAllRow(String queryId, int rowIndex) throws BOSException, EASBizException;
    public int shrinkRow(String queryId, int rowIndex) throws BOSException, EASBizException;
    public void relaseDataSource(String queryId) throws BOSException;
    public Map getPeriodRange(Set companyIdSet, String periodTypeId) throws BOSException, EASBizException;
}