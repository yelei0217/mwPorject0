package com.kingdee.eas.custom;

import com.kingdee.bos.framework.ejb.EJBRemoteException;
import com.kingdee.bos.util.BOSObjectType;
import java.rmi.RemoteException;
import com.kingdee.bos.framework.AbstractBizCtrl;
import com.kingdee.bos.orm.template.ORMObject;

import com.kingdee.bos.Context;
import com.kingdee.eas.fi.gl.rpt.GLRptBaseFacade;
import com.kingdee.bos.BOSException;
import com.kingdee.eas.custom.app.*;
import com.kingdee.eas.fi.gl.rpt.IGLRptBaseFacade;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.util.*;

public class GLRptCompAccountBalanceFacade extends GLRptBaseFacade implements IGLRptCompAccountBalanceFacade
{
    public GLRptCompAccountBalanceFacade()
    {
        super();
        registerInterface(IGLRptCompAccountBalanceFacade.class, this);
    }
    public GLRptCompAccountBalanceFacade(Context ctx)
    {
        super(ctx);
        registerInterface(IGLRptCompAccountBalanceFacade.class, this);
    }
    public BOSObjectType getType()
    {
        return new BOSObjectType("DFCB5F04");
    }
    private GLRptCompAccountBalanceFacadeController getController() throws BOSException
    {
        return (GLRptCompAccountBalanceFacadeController)getBizController();
    }
}