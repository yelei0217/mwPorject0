package com.kingdee.eas.custom;

import com.kingdee.bos.framework.ejb.EJBRemoteException;
import com.kingdee.bos.util.BOSObjectType;
import java.rmi.RemoteException;
import com.kingdee.bos.framework.AbstractBizCtrl;
import com.kingdee.bos.orm.template.ORMObject;

import com.kingdee.eas.framework.report.CommRptBase;
import com.kingdee.bos.BOSException;
import java.util.Map;
import com.kingdee.eas.framework.report.ICommRptBase;
import java.util.List;
import java.lang.String;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.Context;
import com.kingdee.eas.fi.gl.rpt.GLRptQueryInitData;
import com.kingdee.eas.custom.app.*;
import java.util.Set;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.bos.util.*;

public class GLRptCompositeFacade extends CommRptBase implements IGLRptCompositeFacade
{
    public GLRptCompositeFacade()
    {
        super();
        registerInterface(IGLRptCompositeFacade.class, this);
    }
    public GLRptCompositeFacade(Context ctx)
    {
        super(ctx);
        registerInterface(IGLRptCompositeFacade.class, this);
    }
    public BOSObjectType getType()
    {
        return new BOSObjectType("8A5BA895");
    }
    private GLRptCompositeFacadeController getController() throws BOSException
    {
        return (GLRptCompositeFacadeController)getBizController();
    }
    /**
     *获取查询初始化数据-User defined method
     *@param isSupportUnion isSupportUnion
     *@return
     */
    public GLRptQueryInitData getQueryInitData(boolean isSupportUnion) throws BOSException, EASBizException
    {
        try {
            return getController().getQueryInitData(getContext(), isSupportUnion);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *获取查询初始化数据-User defined method
     *@param companyIds 公司集合
     *@param isSupportUnion isSupportUnion
     *@return
     */
    public GLRptQueryInitData getQueryInitData(List companyIds, boolean isSupportUnion) throws BOSException, EASBizException
    {
        try {
            return getController().getQueryInitData(getContext(), companyIds, isSupportUnion);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *展开直接下级-User defined method
     *@param queryId 查询标识
     *@param rowIndex 行号
     *@return
     */
    public int expendRow(String queryId, int rowIndex) throws BOSException, EASBizException
    {
        try {
            return getController().expendRow(getContext(), queryId, rowIndex);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *展开所有下级-User defined method
     *@param queryId 查询标识
     *@param rowIndex 行号
     *@return
     */
    public int expendAllRow(String queryId, int rowIndex) throws BOSException, EASBizException
    {
        try {
            return getController().expendAllRow(getContext(), queryId, rowIndex);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *收缩行-User defined method
     *@param queryId 查询标识
     *@param rowIndex 行号
     *@return
     */
    public int shrinkRow(String queryId, int rowIndex) throws BOSException, EASBizException
    {
        try {
            return getController().shrinkRow(getContext(), queryId, rowIndex);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *释放泪师表及服务端会话缓存-User defined method
     *@param queryId 查询标识
     */
    public void relaseDataSource(String queryId) throws BOSException
    {
        try {
            getController().relaseDataSource(getContext(), queryId);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
    /**
     *获取期间范围-User defined method
     *@param companyIdSet 公司ID集合
     *@param periodTypeId 期间类型ID
     *@return
     */
    public Map getPeriodRange(Set companyIdSet, String periodTypeId) throws BOSException, EASBizException
    {
        try {
            return getController().getPeriodRange(getContext(), companyIdSet, periodTypeId);
        }
        catch(RemoteException err) {
            throw new EJBRemoteException(err);
        }
    }
}