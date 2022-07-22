package com.kingdee.eas.custom;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.BOSObjectFactory;
import com.kingdee.bos.util.BOSObjectType;
import com.kingdee.bos.Context;

public class GLRptCompositeFacadeFactory
{
    private GLRptCompositeFacadeFactory()
    {
    }
    public static com.kingdee.eas.custom.IGLRptCompositeFacade getRemoteInstance() throws BOSException
    {
        return (com.kingdee.eas.custom.IGLRptCompositeFacade)BOSObjectFactory.createRemoteBOSObject(new BOSObjectType("8A5BA895") ,com.kingdee.eas.custom.IGLRptCompositeFacade.class);
    }
    
    public static com.kingdee.eas.custom.IGLRptCompositeFacade getRemoteInstanceWithObjectContext(Context objectCtx) throws BOSException
    {
        return (com.kingdee.eas.custom.IGLRptCompositeFacade)BOSObjectFactory.createRemoteBOSObjectWithObjectContext(new BOSObjectType("8A5BA895") ,com.kingdee.eas.custom.IGLRptCompositeFacade.class, objectCtx);
    }
    public static com.kingdee.eas.custom.IGLRptCompositeFacade getLocalInstance(Context ctx) throws BOSException
    {
        return (com.kingdee.eas.custom.IGLRptCompositeFacade)BOSObjectFactory.createBOSObject(ctx, new BOSObjectType("8A5BA895"));
    }
    public static com.kingdee.eas.custom.IGLRptCompositeFacade getLocalInstance(String sessionID) throws BOSException
    {
        return (com.kingdee.eas.custom.IGLRptCompositeFacade)BOSObjectFactory.createBOSObject(sessionID, new BOSObjectType("8A5BA895"));
    }
}