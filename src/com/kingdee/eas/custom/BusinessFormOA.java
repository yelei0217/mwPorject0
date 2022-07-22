package com.kingdee.eas.custom;

import com.kingdee.bos.framework.ejb.EJBRemoteException;
import com.kingdee.bos.util.BOSObjectType;
import java.rmi.RemoteException;
import com.kingdee.bos.framework.AbstractBizCtrl;
import com.kingdee.bos.orm.template.ORMObject;

import com.kingdee.bos.Context;
import com.kingdee.bos.BOSException;
import com.kingdee.eas.custom.app.*;
import java.lang.String;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.util.*;

public class BusinessFormOA extends AbstractBizCtrl implements IBusinessFormOA
{
    public BusinessFormOA()
    {
        super();
        registerInterface(IBusinessFormOA.class, this);
    }
    public BusinessFormOA(Context ctx)
    {
        super(ctx);
        registerInterface(IBusinessFormOA.class, this);
    }
    public BOSObjectType getType()
    {
        return new BOSObjectType("2A0C8EFB");
    }
    private BusinessFormOAController getController() throws BOSException
    {
        return (BusinessFormOAController)getBizController();
    }
    /**
     *采购申请单-User defined method
     *@param database 数据库
     *@return
     */
    public String PurRequestFormOA(String database) throws BOSException
    {
        try {
            return getController().PurRequestFormOA(getContext(), database);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *（其他）应付单-User defined method
     *@param database 数据库
     *@return
     */
    public String ApOtherFormOA(String database) throws BOSException
    {
        try {
            return getController().ApOtherFormOA(getContext(), database);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *将EAS付款申请单传入到OA的中间表-User defined method
     *@param database 数据库
     *@param billId 单据id
     *@return
     */
    public String PayApplyToOA(String database, String billId) throws BOSException
    {
        try {
            return getController().PayApplyToOA(getContext(), database, billId);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *同步付款申请单中间表-User defined method
     *@param database 数据库
     */
    public void syncPayApply(String database) throws BOSException
    {
        try {
            getController().syncPayApply(getContext(), database);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *修改付款单业务日期-User defined method
     *@param ids ids
     *@param date 业务日期
     *@param type 单据类型
     */
    public void mobilePaymentBillBizDate(String ids, String date, String type) throws BOSException
    {
        try {
            getController().mobilePaymentBillBizDate(getContext(), ids, date, type);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *同步OA的付款单-User defined method
     *@param database 数据库
     *@return
     */
    public String syncPaymentBillFormOA(String database) throws BOSException
    {
        try {
            return getController().syncPaymentBillFormOA(getContext(), database);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *付款状态同步-User defined method
     *@return
     */
    public String updateMidPayStatus() throws BOSException
    {
        try {
            return getController().updateMidPayStatus(getContext());
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *单据是否存在下游单据-User defined method
     *@param id id
     *@return
     */
    public boolean IsExistDownstreamBill(String id) throws BOSException
    {
        try {
            return getController().IsExistDownstreamBill(getContext(), id);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *京东慧采-User defined method
     *@param database 数据库
     */
    public void PurvspJDFromOA(String database) throws BOSException
    {
        try {
            getController().PurvspJDFromOA(getContext(), database);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *收货确认单-京东-User defined method
     *@param database 数据库
     */
    public void ReceConfirmVSPJD(String database) throws BOSException
    {
        try {
            getController().ReceConfirmVSPJD(getContext(), database);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
}