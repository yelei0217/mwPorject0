package com.kingdee.eas.custom;

import java.io.Serializable;

public class SupplySyncLogInfo extends AbstractSupplySyncLogInfo implements Serializable 
{
    public SupplySyncLogInfo()
    {
        super();
    }
    protected SupplySyncLogInfo(String pkField)
    {
        super(pkField);
    }
}