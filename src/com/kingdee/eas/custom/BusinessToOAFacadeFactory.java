package com.kingdee.eas.custom;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.BOSObjectFactory;
import com.kingdee.bos.util.BOSObjectType;
import com.kingdee.bos.Context;

public class BusinessToOAFacadeFactory
{
    private BusinessToOAFacadeFactory()
    {
    }
    public static com.kingdee.eas.custom.IBusinessToOAFacade getRemoteInstance() throws BOSException
    {
        return (com.kingdee.eas.custom.IBusinessToOAFacade)BOSObjectFactory.createRemoteBOSObject(new BOSObjectType("2625436C") ,com.kingdee.eas.custom.IBusinessToOAFacade.class);
    }
    
    public static com.kingdee.eas.custom.IBusinessToOAFacade getRemoteInstanceWithObjectContext(Context objectCtx) throws BOSException
    {
        return (com.kingdee.eas.custom.IBusinessToOAFacade)BOSObjectFactory.createRemoteBOSObjectWithObjectContext(new BOSObjectType("2625436C") ,com.kingdee.eas.custom.IBusinessToOAFacade.class, objectCtx);
    }
    public static com.kingdee.eas.custom.IBusinessToOAFacade getLocalInstance(Context ctx) throws BOSException
    {
        return (com.kingdee.eas.custom.IBusinessToOAFacade)BOSObjectFactory.createBOSObject(ctx, new BOSObjectType("2625436C"));
    }
    public static com.kingdee.eas.custom.IBusinessToOAFacade getLocalInstance(String sessionID) throws BOSException
    {
        return (com.kingdee.eas.custom.IBusinessToOAFacade)BOSObjectFactory.createBOSObject(sessionID, new BOSObjectType("2625436C"));
    }
}