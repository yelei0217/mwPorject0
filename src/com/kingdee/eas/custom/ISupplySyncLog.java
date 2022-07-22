package com.kingdee.eas.custom;

import com.kingdee.bos.BOSException;
//import com.kingdee.bos.metadata.*;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.util.*;
import com.kingdee.bos.Context;

import com.kingdee.bos.Context;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
import java.lang.String;
import com.kingdee.eas.framework.CoreBaseInfo;
import com.kingdee.eas.framework.CoreBaseCollection;
import com.kingdee.bos.framework.*;
import com.kingdee.eas.framework.IDataBase;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.bos.metadata.entity.SelectorItemCollection;
import com.kingdee.bos.util.*;

public interface ISupplySyncLog extends IDataBase
{
    public SupplySyncLogInfo getSupplySyncLogInfo(IObjectPK pk) throws BOSException, EASBizException;
    public SupplySyncLogInfo getSupplySyncLogInfo(IObjectPK pk, SelectorItemCollection selector) throws BOSException, EASBizException;
    public SupplySyncLogInfo getSupplySyncLogInfo(String oql) throws BOSException, EASBizException;
    public SupplySyncLogCollection getSupplySyncLogCollection() throws BOSException;
    public SupplySyncLogCollection getSupplySyncLogCollection(EntityViewInfo view) throws BOSException;
    public SupplySyncLogCollection getSupplySyncLogCollection(String oql) throws BOSException;
}