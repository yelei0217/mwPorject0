package com.kingdee.eas.custom;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.BOSObjectFactory;
import com.kingdee.bos.util.BOSObjectType;
import com.kingdee.bos.Context;

public class SupplyCityFacadeFactory
{
    private SupplyCityFacadeFactory()
    {
    }
    public static com.kingdee.eas.custom.ISupplyCityFacade getRemoteInstance() throws BOSException
    {
        return (com.kingdee.eas.custom.ISupplyCityFacade)BOSObjectFactory.createRemoteBOSObject(new BOSObjectType("1D991079") ,com.kingdee.eas.custom.ISupplyCityFacade.class);
    }
    
    public static com.kingdee.eas.custom.ISupplyCityFacade getRemoteInstanceWithObjectContext(Context objectCtx) throws BOSException
    {
        return (com.kingdee.eas.custom.ISupplyCityFacade)BOSObjectFactory.createRemoteBOSObjectWithObjectContext(new BOSObjectType("1D991079") ,com.kingdee.eas.custom.ISupplyCityFacade.class, objectCtx);
    }
    public static com.kingdee.eas.custom.ISupplyCityFacade getLocalInstance(Context ctx) throws BOSException
    {
        return (com.kingdee.eas.custom.ISupplyCityFacade)BOSObjectFactory.createBOSObject(ctx, new BOSObjectType("1D991079"));
    }
    public static com.kingdee.eas.custom.ISupplyCityFacade getLocalInstance(String sessionID) throws BOSException
    {
        return (com.kingdee.eas.custom.ISupplyCityFacade)BOSObjectFactory.createBOSObject(sessionID, new BOSObjectType("1D991079"));
    }
}