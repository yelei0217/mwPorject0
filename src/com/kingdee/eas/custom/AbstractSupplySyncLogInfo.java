package com.kingdee.eas.custom;

import java.io.Serializable;
import com.kingdee.bos.dao.AbstractObjectValue;
import java.util.Locale;
import com.kingdee.util.TypeConversionUtils;
import com.kingdee.bos.util.BOSObjectType;


public class AbstractSupplySyncLogInfo extends com.kingdee.eas.framework.DataBaseInfo implements Serializable 
{
    public AbstractSupplySyncLogInfo()
    {
        this("id");
    }
    protected AbstractSupplySyncLogInfo(String pkField)
    {
        super(pkField);
    }
    /**
     * Object:�����۸�ͬ����־'s ��������property 
     */
    public com.kingdee.eas.custom.app.DateBasetype getDateBaseType()
    {
        return com.kingdee.eas.custom.app.DateBasetype.getEnum(getString("dateBaseType"));
    }
    public void setDateBaseType(com.kingdee.eas.custom.app.DateBasetype item)
    {
		if (item != null) {
        setString("dateBaseType", item.getValue());
		}
    }
    /**
     * Object:�����۸�ͬ����־'s ͬ��״̬property 
     */
    public boolean isStatus()
    {
        return getBoolean("status");
    }
    public void setStatus(boolean item)
    {
        setBoolean("status", item);
    }
    /**
     * Object:�����۸�ͬ����־'s �汾property 
     */
    public String getVersion()
    {
        return getString("version");
    }
    public void setVersion(String item)
    {
        setString("version", item);
    }
    /**
     * Object:�����۸�ͬ����־'s ͬ��ʱ��property 
     */
    public java.sql.Time getUpdatetime()
    {
        return getTime("updatetime");
    }
    public void setUpdatetime(java.sql.Time item)
    {
        setTime("updatetime", item);
    }
    /**
     * Object:�����۸�ͬ����־'s ��������property 
     */
    public java.util.Date getUpdateDate()
    {
        return getDate("updateDate");
    }
    public void setUpdateDate(java.util.Date item)
    {
        setDate("updateDate", item);
    }
    /**
     * Object:�����۸�ͬ����־'s ��������property 
     */
    public com.kingdee.eas.custom.app.DateBaseProcessType getProcessType()
    {
        return com.kingdee.eas.custom.app.DateBaseProcessType.getEnum(getString("processType"));
    }
    public void setProcessType(com.kingdee.eas.custom.app.DateBaseProcessType item)
    {
		if (item != null) {
        setString("processType", item.getValue());
		}
    }
    /**
     * Object:�����۸�ͬ����־'s �쳣��Ϣproperty 
     */
    public String getMessage()
    {
        return getString("Message");
    }
    public void setMessage(String item)
    {
        setString("Message", item);
    }
    public BOSObjectType getBOSType()
    {
        return new BOSObjectType("65E31895");
    }
}