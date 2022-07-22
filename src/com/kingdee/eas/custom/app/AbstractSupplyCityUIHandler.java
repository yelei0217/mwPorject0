/**
 * output package name
 */
package com.kingdee.eas.custom.app;

import com.kingdee.bos.Context;
import com.kingdee.eas.framework.batchHandler.RequestContext;
import com.kingdee.eas.framework.batchHandler.ResponseContext;


/**
 * output class name
 */
public abstract class AbstractSupplyCityUIHandler extends com.kingdee.eas.framework.report.app.CommRptBaseUIHandler

{
	public void handleactoinDisable(RequestContext request,ResponseContext response, Context context) throws Exception {
		_handleactoinDisable(request,response,context);
	}
	protected void _handleactoinDisable(RequestContext request,ResponseContext response, Context context) throws Exception {
	}
	public void handleactionAble(RequestContext request,ResponseContext response, Context context) throws Exception {
		_handleactionAble(request,response,context);
	}
	protected void _handleactionAble(RequestContext request,ResponseContext response, Context context) throws Exception {
	}
}