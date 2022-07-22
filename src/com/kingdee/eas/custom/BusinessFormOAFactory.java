package com.kingdee.eas.custom;

import com.kingdee.bos.BOSException;
import com.kingdee.bos.BOSObjectFactory;
import com.kingdee.bos.util.BOSObjectType;
import com.kingdee.bos.Context;

public class BusinessFormOAFactory
{
    private BusinessFormOAFactory()
    {
    }
    public static com.kingdee.eas.custom.IBusinessFormOA getRemoteInstance() throws BOSException
    {
        return (com.kingdee.eas.custom.IBusinessFormOA)BOSObjectFactory.createRemoteBOSObject(new BOSObjectType("2A0C8EFB") ,com.kingdee.eas.custom.IBusinessFormOA.class);
    }
    
    public static com.kingdee.eas.custom.IBusinessFormOA getRemoteInstanceWithObjectContext(Context objectCtx) throws BOSException
    {
        return (com.kingdee.eas.custom.IBusinessFormOA)BOSObjectFactory.createRemoteBOSObjectWithObjectContext(new BOSObjectType("2A0C8EFB") ,com.kingdee.eas.custom.IBusinessFormOA.class, objectCtx);
    }
    public static com.kingdee.eas.custom.IBusinessFormOA getLocalInstance(Context ctx) throws BOSException
    {
        return (com.kingdee.eas.custom.IBusinessFormOA)BOSObjectFactory.createBOSObject(ctx, new BOSObjectType("2A0C8EFB"));
    }
    public static com.kingdee.eas.custom.IBusinessFormOA getLocalInstance(String sessionID) throws BOSException
    {
        return (com.kingdee.eas.custom.IBusinessFormOA)BOSObjectFactory.createBOSObject(sessionID, new BOSObjectType("2A0C8EFB"));
    }
}