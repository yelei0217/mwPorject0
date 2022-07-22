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
     *�ɹ����뵥-User defined method
     *@param database ���ݿ�
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
     *��������Ӧ����-User defined method
     *@param database ���ݿ�
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
     *��EAS�������뵥���뵽OA���м��-User defined method
     *@param database ���ݿ�
     *@param billId ����id
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
     *ͬ���������뵥�м��-User defined method
     *@param database ���ݿ�
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
     *�޸ĸ��ҵ������-User defined method
     *@param ids ids
     *@param date ҵ������
     *@param type ��������
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
     *ͬ��OA�ĸ��-User defined method
     *@param database ���ݿ�
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
     *����״̬ͬ��-User defined method
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
     *�����Ƿ�������ε���-User defined method
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
     *�����۲�-User defined method
     *@param database ���ݿ�
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
     *�ջ�ȷ�ϵ�-����-User defined method
     *@param database ���ݿ�
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