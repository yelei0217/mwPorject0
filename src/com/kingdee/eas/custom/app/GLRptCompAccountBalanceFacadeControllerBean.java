package com.kingdee.eas.custom.app;

import org.apache.log4j.Logger;
import javax.ejb.*;
import java.rmi.RemoteException;
import com.kingdee.bos.*;
import com.kingdee.bos.util.BOSObjectType;
import com.kingdee.bos.metadata.IMetaDataPK;
import com.kingdee.bos.metadata.entity.EntityViewInfo;
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

import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.app.dao.GLRptCompAccountBalanceDao;
import com.kingdee.eas.fi.gl.ReportConditionBase;
import com.kingdee.eas.fi.gl.rpt.GLRptQueryResult;
import com.kingdee.eas.fi.gl.rpt.IGLRptDao;
import com.kingdee.eas.fi.gl.rpt.app.GLRptBaseFacadeControllerBean;
import com.kingdee.eas.fi.gl.rpt.app.dao.GLRptAccountBalanceDao;

public class GLRptCompAccountBalanceFacadeControllerBean extends AbstractGLRptCompAccountBalanceFacadeControllerBean
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7569843165560017268L;
	private static Logger logger = Logger.getLogger("com.kingdee.eas.custom.app.GLRptCompAccountBalanceFacadeControllerBean");

	@Override
	protected IGLRptDao getGLRptDao(Context ctx, ReportConditionBase condition)throws BOSException, EASBizException {
		String msg = "&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&getGLRptDao&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&";
		System.out.println(msg);
		logger.info(msg);
 		return new GLRptCompAccountBalanceDao(ctx);
	}

	@Override
	protected GLRptQueryResult _query(Context ctx, EntityViewInfo ev)
			throws BOSException, EASBizException {
 		return super._query(ctx, ev);
	}


	 
}