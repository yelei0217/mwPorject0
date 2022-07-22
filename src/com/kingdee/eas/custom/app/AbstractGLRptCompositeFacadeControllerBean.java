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

import com.kingdee.eas.fi.gl.rpt.GLRptQueryInitData;
import com.kingdee.eas.framework.report.app.CommRptBaseControllerBean;
import java.util.Map;
import java.util.List;
import java.lang.String;
import java.util.Set;
import com.kingdee.eas.common.EASBizException;



public abstract class AbstractGLRptCompositeFacadeControllerBean extends CommRptBaseControllerBean implements GLRptCompositeFacadeController
{
    protected AbstractGLRptCompositeFacadeControllerBean()
    {
    }

    protected BOSObjectType getBOSType()
    {
        return new BOSObjectType("8A5BA895");
    }

    public GLRptQueryInitData getQueryInitData(Context ctx, boolean isSupportUnion) throws BOSException, EASBizException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("1786b07d-1842-4ee9-97a3-8725af135fbd"), new Object[]{ctx, new Boolean(isSupportUnion)});
            invokeServiceBefore(svcCtx);
            if(!svcCtx.invokeBreak()) {
            GLRptQueryInitData retValue = (GLRptQueryInitData)_getQueryInitData(ctx, isSupportUnion);
            svcCtx.setMethodReturnValue(retValue);
            }
            invokeServiceAfter(svcCtx);
            return (GLRptQueryInitData)svcCtx.getMethodReturnValue();
        } catch (BOSException ex) {
            throw ex;
        } catch (EASBizException ex0) {
            throw ex0;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected GLRptQueryInitData _getQueryInitData(Context ctx, boolean isSupportUnion) throws BOSException, EASBizException
    {    	
        return null;
    }

    public GLRptQueryInitData getQueryInitData(Context ctx, List companyIds, boolean isSupportUnion) throws BOSException, EASBizException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("19fccc66-ed9b-4e78-a8bb-f2cdbb40adbe"), new Object[]{ctx, companyIds, new Boolean(isSupportUnion)});
            invokeServiceBefore(svcCtx);
            if(!svcCtx.invokeBreak()) {
            GLRptQueryInitData retValue = (GLRptQueryInitData)_getQueryInitData(ctx, companyIds, isSupportUnion);
            svcCtx.setMethodReturnValue(retValue);
            }
            invokeServiceAfter(svcCtx);
            return (GLRptQueryInitData)svcCtx.getMethodReturnValue();
        } catch (BOSException ex) {
            throw ex;
        } catch (EASBizException ex0) {
            throw ex0;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected GLRptQueryInitData _getQueryInitData(Context ctx, List companyIds, boolean isSupportUnion) throws BOSException, EASBizException
    {    	
        return null;
    }

    public int expendRow(Context ctx, String queryId, int rowIndex) throws BOSException, EASBizException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("e1b9f723-0435-426b-8531-15e079d35998"), new Object[]{ctx, queryId, new Integer(rowIndex)});
            invokeServiceBefore(svcCtx);
            if(!svcCtx.invokeBreak()) {
            int retValue = (int)_expendRow(ctx, queryId, rowIndex);
            svcCtx.setMethodReturnValue(new Integer(retValue));
            }
            invokeServiceAfter(svcCtx);
            return ((Integer)svcCtx.getMethodReturnValue()).intValue();
        } catch (BOSException ex) {
            throw ex;
        } catch (EASBizException ex0) {
            throw ex0;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected int _expendRow(Context ctx, String queryId, int rowIndex) throws BOSException, EASBizException
    {    	
        return 0;
    }

    public int expendAllRow(Context ctx, String queryId, int rowIndex) throws BOSException, EASBizException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("6eac9aba-d0ae-48ee-8bc7-82bb109767bf"), new Object[]{ctx, queryId, new Integer(rowIndex)});
            invokeServiceBefore(svcCtx);
            if(!svcCtx.invokeBreak()) {
            int retValue = (int)_expendAllRow(ctx, queryId, rowIndex);
            svcCtx.setMethodReturnValue(new Integer(retValue));
            }
            invokeServiceAfter(svcCtx);
            return ((Integer)svcCtx.getMethodReturnValue()).intValue();
        } catch (BOSException ex) {
            throw ex;
        } catch (EASBizException ex0) {
            throw ex0;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected int _expendAllRow(Context ctx, String queryId, int rowIndex) throws BOSException, EASBizException
    {    	
        return 0;
    }

    public int shrinkRow(Context ctx, String queryId, int rowIndex) throws BOSException, EASBizException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("1673cb3c-08af-4fd0-92c6-a735bc424a96"), new Object[]{ctx, queryId, new Integer(rowIndex)});
            invokeServiceBefore(svcCtx);
            if(!svcCtx.invokeBreak()) {
            int retValue = (int)_shrinkRow(ctx, queryId, rowIndex);
            svcCtx.setMethodReturnValue(new Integer(retValue));
            }
            invokeServiceAfter(svcCtx);
            return ((Integer)svcCtx.getMethodReturnValue()).intValue();
        } catch (BOSException ex) {
            throw ex;
        } catch (EASBizException ex0) {
            throw ex0;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected int _shrinkRow(Context ctx, String queryId, int rowIndex) throws BOSException, EASBizException
    {    	
        return 0;
    }

    public void relaseDataSource(Context ctx, String queryId) throws BOSException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("805c12c7-cf5a-4adb-8512-300bea76ebd2"), new Object[]{ctx, queryId});
            invokeServiceBefore(svcCtx);
              if(!svcCtx.invokeBreak()) {
            _relaseDataSource(ctx, queryId);
            }
            invokeServiceAfter(svcCtx);
        } catch (BOSException ex) {
            throw ex;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected void _relaseDataSource(Context ctx, String queryId) throws BOSException
    {    	
        return;
    }

    public Map getPeriodRange(Context ctx, Set companyIdSet, String periodTypeId) throws BOSException, EASBizException
    {
        try {
            ServiceContext svcCtx = createServiceContext(new MetaDataPK("0aede798-fe04-443c-94c6-ec442f57630c"), new Object[]{ctx, companyIdSet, periodTypeId});
            invokeServiceBefore(svcCtx);
            if(!svcCtx.invokeBreak()) {
            Map retValue = (Map)_getPeriodRange(ctx, companyIdSet, periodTypeId);
            svcCtx.setMethodReturnValue(retValue);
            }
            invokeServiceAfter(svcCtx);
            return (Map)svcCtx.getMethodReturnValue();
        } catch (BOSException ex) {
            throw ex;
        } catch (EASBizException ex0) {
            throw ex0;
        } finally {
            super.cleanUpServiceState();
        }
    }
    protected Map _getPeriodRange(Context ctx, Set companyIdSet, String periodTypeId) throws BOSException, EASBizException
    {    	
        return null;
    }

}