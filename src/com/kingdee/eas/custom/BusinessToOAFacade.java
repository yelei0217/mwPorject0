package com.kingdee.eas.custom;

import com.kingdee.bos.framework.ejb.EJBRemoteException;
import com.kingdee.bos.util.BOSObjectType;
import java.rmi.RemoteException;
import com.kingdee.bos.framework.AbstractBizCtrl;
import com.kingdee.bos.orm.template.ORMObject;

import com.kingdee.bos.Context;
import com.kingdee.bos.BOSException;
import java.util.List;
import com.kingdee.eas.custom.app.*;
import java.lang.String;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.util.*;

public class BusinessToOAFacade extends AbstractBizCtrl implements IBusinessToOAFacade
{
    public BusinessToOAFacade()
    {
        super();
        registerInterface(IBusinessToOAFacade.class, this);
    }
    public BusinessToOAFacade(Context ctx)
    {
        super(ctx);
        registerInterface(IBusinessToOAFacade.class, this);
    }
    public BOSObjectType getType()
    {
        return new BOSObjectType("2625436C");
    }
    private BusinessToOAFacadeController getController() throws BOSException
    {
        return (BusinessToOAFacadeController)getBizController();
    }
    /**
     *�޸����ϻ�����Ϣ-User defined method
     *@param mId ����ID
     */
    public void updateMaterialInfo(String mId) throws BOSException
    {
        try {
            getController().updateMaterialInfo(getContext(), mId);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *�Զ�ͬ�������嵥��Ϣ-User defined method
     */
    public void syncSupplyInfoAuto() throws BOSException
    {
        try {
            getController().syncSupplyInfoAuto(getContext());
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *���ù�����Ϣ-User defined method
     *@param ids ids
     */
    public void disableSupplyInfo(List ids) throws BOSException
    {
        try {
            getController().disableSupplyInfo(getContext(), ids);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *���ù�����Ϣ-User defined method
     *@param ids ids
     */
    public void ableSupplyInfo(List ids) throws BOSException
    {
        try {
            getController().ableSupplyInfo(getContext(), ids);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
}