package com.kingdee.eas.custom;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.BOSObjectFactory;
import com.kingdee.bos.util.BOSObjectType;
import com.kingdee.bos.Context;

public class GLRptCompAccountBalanceFacadeFactory
{
    private GLRptCompAccountBalanceFacadeFactory()
    {
    }
    public static com.kingdee.eas.custom.IGLRptCompAccountBalanceFacade getRemoteInstance() throws BOSException
    {
        return (com.kingdee.eas.custom.IGLRptCompAccountBalanceFacade)BOSObjectFactory.createRemoteBOSObject(new BOSObjectType("DFCB5F04") ,com.kingdee.eas.custom.IGLRptCompAccountBalanceFacade.class);
    }
    
    public static com.kingdee.eas.custom.IGLRptCompAccountBalanceFacade getRemoteInstanceWithObjectContext(Context objectCtx) throws BOSException
    {
        return (com.kingdee.eas.custom.IGLRptCompAccountBalanceFacade)BOSObjectFactory.createRemoteBOSObjectWithObjectContext(new BOSObjectType("DFCB5F04") ,com.kingdee.eas.custom.IGLRptCompAccountBalanceFacade.class, objectCtx);
    }
    public static com.kingdee.eas.custom.IGLRptCompAccountBalanceFacade getLocalInstance(Context ctx) throws BOSException
    {
        return (com.kingdee.eas.custom.IGLRptCompAccountBalanceFacade)BOSObjectFactory.createBOSObject(ctx, new BOSObjectType("DFCB5F04"));
    }
    public static com.kingdee.eas.custom.IGLRptCompAccountBalanceFacade getLocalInstance(String sessionID) throws BOSException
    {
        return (com.kingdee.eas.custom.IGLRptCompAccountBalanceFacade)BOSObjectFactory.createBOSObject(sessionID, new BOSObjectType("DFCB5F04"));
    }
}