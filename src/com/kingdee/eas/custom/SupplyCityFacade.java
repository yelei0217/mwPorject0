package com.kingdee.eas.custom;

import com.kingdee.bos.framework.ejb.EJBRemoteException;
import com.kingdee.bos.util.BOSObjectType;
import java.rmi.RemoteException;
import com.kingdee.bos.framework.AbstractBizCtrl;
import com.kingdee.bos.orm.template.ORMObject;

import com.kingdee.eas.framework.report.CommRptBase;
import com.kingdee.bos.Context;
import com.kingdee.bos.BOSException;
import com.kingdee.eas.framework.report.ICommRptBase;
import com.kingdee.eas.custom.app.*;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.util.*;

public class SupplyCityFacade extends CommRptBase implements ISupplyCityFacade
{
    public SupplyCityFacade()
    {
        super();
        registerInterface(ISupplyCityFacade.class, this);
    }
    public SupplyCityFacade(Context ctx)
    {
        super(ctx);
        registerInterface(ISupplyCityFacade.class, this);
    }
    public BOSObjectType getType()
    {
        return new BOSObjectType("1D991079");
    }
    private SupplyCityFacadeController getController() throws BOSException
    {
        return (SupplyCityFacadeController)getBizController();
    }
}