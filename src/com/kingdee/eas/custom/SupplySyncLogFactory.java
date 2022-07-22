package com.kingdee.eas.custom;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.BOSObjectFactory;
import com.kingdee.bos.util.BOSObjectType;
import com.kingdee.bos.Context;

public class SupplySyncLogFactory
{
    private SupplySyncLogFactory()
    {
    }
    public static com.kingdee.eas.custom.ISupplySyncLog getRemoteInstance() throws BOSException
    {
        return (com.kingdee.eas.custom.ISupplySyncLog)BOSObjectFactory.createRemoteBOSObject(new BOSObjectType("65E31895") ,com.kingdee.eas.custom.ISupplySyncLog.class);
    }
    
    public static com.kingdee.eas.custom.ISupplySyncLog getRemoteInstanceWithObjectContext(Context objectCtx) throws BOSException
    {
        return (com.kingdee.eas.custom.ISupplySyncLog)BOSObjectFactory.createRemoteBOSObjectWithObjectContext(new BOSObjectType("65E31895") ,com.kingdee.eas.custom.ISupplySyncLog.class, objectCtx);
    }
    public static com.kingdee.eas.custom.ISupplySyncLog getLocalInstance(Context ctx) throws BOSException
    {
        return (com.kingdee.eas.custom.ISupplySyncLog)BOSObjectFactory.createBOSObject(ctx, new BOSObjectType("65E31895"));
    }
    public static com.kingdee.eas.custom.ISupplySyncLog getLocalInstance(String sessionID) throws BOSException
    {
        return (com.kingdee.eas.custom.ISupplySyncLog)BOSObjectFactory.createBOSObject(sessionID, new BOSObjectType("65E31895"));
    }
}