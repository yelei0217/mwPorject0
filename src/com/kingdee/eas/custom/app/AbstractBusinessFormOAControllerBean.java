package com.kingdee.eas.custom.app;

import javax.ejb.*;
import java.rmi.RemoteException;
import com.kingdee.bos.*;
import com.kingdee.bos.util.BOSObjectType;
import com.kingdee.bos.metadata.IMetaDataPK;
import com.kingdee.bos.metadata.rule.RuleExecutor;
import com.kingdee.bos.metadata.MetaDataPK;
//import com.kingdee.bos.metadata.entity.EntityViewInfo;
import com.kingdee.bos.framework.ejb.AbstractEntityControllerBean;
import com.kingdee.bos.framework.ejb.AbstractBizControllerBean;
//import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.bos.dao.IObjectCollection;
import com.kingdee.bos.service.ServiceContext;
import com.kingdee.bos.service.IServiceContext;
import com.kingdee.eas.framework.Result;
import com.kingdee.eas.framework.LineResult;
import com.kingdee.eas.framework.exception.EASMultiException;
import com.kingdee.bos.dao.ormapping.ObjectUuidPK;

import java.lang.String;



public abstract class AbstractBusinessFormOAControllerBean extends AbstractBizControllerBean implements BusinessFormOAController
{
    protected AbstractBusinessFormOAControllerBean()
    {
    }

    protected BOSObjectType getBOSType()
    {
        return new BOSObjectType("2A0C8EFB");
    }

    public String PurRequestFormOA(Context ctx, String database) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("afb505f8-be24-4558-aae9-04ef0cfa9b9e"), new Object[]{ctx, database});
            invokeServiceBefore(svcCtx);
            if(!svcCtx.invokeBreak()) {
            String retValue = (String)_PurRequestFormOA(ctx, database);
            svcCtx.setMethodReturnValue(retValue);
            }
            invokeServiceAfter(svcCtx);
            return (String)svcCtx.getMethodReturnValue();
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected String _PurRequestFormOA(Context ctx, String database) throws BOSException
    {    	
        return null;
    }

    public String ApOtherFormOA(Context ctx, String database) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("b267cb18-bd38-4b38-912f-71c8b91fa19a"), new Object[]{ctx, database});
            invokeServiceBefore(svcCtx);
            if(!svcCtx.invokeBreak()) {
            String retValue = (String)_ApOtherFormOA(ctx, database);
            svcCtx.setMethodReturnValue(retValue);
            }
            invokeServiceAfter(svcCtx);
            return (String)svcCtx.getMethodReturnValue();
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected String _ApOtherFormOA(Context ctx, String database) throws BOSException
    {    	
        return null;
    }

    public String PayApplyToOA(Context ctx, String database, String billId) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("118b5b23-bfc3-476f-be7f-75ecb38409ce"), new Object[]{ctx, database, billId});
            invokeServiceBefore(svcCtx);
            if(!svcCtx.invokeBreak()) {
            String retValue = (String)_PayApplyToOA(ctx, database, billId);
            svcCtx.setMethodReturnValue(retValue);
            }
            invokeServiceAfter(svcCtx);
            return (String)svcCtx.getMethodReturnValue();
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected String _PayApplyToOA(Context ctx, String database, String billId) throws BOSException
    {    	
        return null;
    }

    public void syncPayApply(Context ctx, String database) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("de5b87d5-eb41-4368-a0f8-1e435468b92c"), new Object[]{ctx, database});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _syncPayApply(ctx, database);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _syncPayApply(Context ctx, String database) throws BOSException
    {    	
        return;
    }

    public void mobilePaymentBillBizDate(Context ctx, String ids, String date, String type) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("d6015bcf-cac9-46a6-9eb2-368064ad0766"), new Object[]{ctx, ids, date, type});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _mobilePaymentBillBizDate(ctx, ids, date, type);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _mobilePaymentBillBizDate(Context ctx, String ids, String date, String type) throws BOSException
    {    	
        return;
    }

    public String syncPaymentBillFormOA(Context ctx, String database) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("efd54385-205b-4645-bd70-fe3c7d3eedb6"), new Object[]{ctx, database});
            invokeServiceBefore(svcCtx);
            if(!svcCtx.invokeBreak()) {
            String retValue = (String)_syncPaymentBillFormOA(ctx, database);
            svcCtx.setMethodReturnValue(retValue);
            }
            invokeServiceAfter(svcCtx);
            return (String)svcCtx.getMethodReturnValue();
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected String _syncPaymentBillFormOA(Context ctx, String database) throws BOSException
    {    	
        return null;
    }

    public String updateMidPayStatus(Context ctx) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("13074d96-4ee2-466e-9967-1940fc8bbb97"), new Object[]{ctx});
            invokeServiceBefore(svcCtx);
            if(!svcCtx.invokeBreak()) {
            String retValue = (String)_updateMidPayStatus(ctx);
            svcCtx.setMethodReturnValue(retValue);
            }
            invokeServiceAfter(svcCtx);
            return (String)svcCtx.getMethodReturnValue();
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected String _updateMidPayStatus(Context ctx) throws BOSException
    {    	
        return null;
    }

    public boolean IsExistDownstreamBill(Context ctx, String id) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("a1aa97ae-689d-40b0-acbb-7221bdb61c2c"), new Object[]{ctx, id});
            invokeServiceBefore(svcCtx);
            if(!svcCtx.invokeBreak()) {
            boolean retValue = (boolean)_IsExistDownstreamBill(ctx, id);
            svcCtx.setMethodReturnValue(new Boolean(retValue));
            }
            invokeServiceAfter(svcCtx);
            return ((Boolean)svcCtx.getMethodReturnValue()).booleanValue();
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected boolean _IsExistDownstreamBill(Context ctx, String id) throws BOSException
    {    	
        return false;
    }

    public void PurvspJDFromOA(Context ctx, String database) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("925e4f6d-33b0-4273-91bf-517275678c23"), new Object[]{ctx, database});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _PurvspJDFromOA(ctx, database);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _PurvspJDFromOA(Context ctx, String database) throws BOSException
    {    	
        return;
    }

    public void ReceConfirmVSPJD(Context ctx, String database) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("cb8dccf7-7c06-458e-9e95-e99f1a1cef90"), new Object[]{ctx, database});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _ReceConfirmVSPJD(ctx, database);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _ReceConfirmVSPJD(Context ctx, String database) throws BOSException
    {    	
        return;
    }

}