package com.kingdee.eas.custom;

import com.kingdee.bos.dao.AbstractObjectCollection;
import com.kingdee.bos.dao.IObjectPK;

public class SupplySyncLogCollection extends AbstractObjectCollection 
{
    public SupplySyncLogCollection()
    {
        super(SupplySyncLogInfo.class);
    }
    public boolean add(SupplySyncLogInfo item)
    {
        return addObject(item);
    }
    public boolean addCollection(SupplySyncLogCollection item)
    {
        return addObjectCollection(item);
    }
    public boolean remove(SupplySyncLogInfo item)
    {
        return removeObject(item);
    }
    public SupplySyncLogInfo get(int index)
    {
        return(SupplySyncLogInfo)getObject(index);
    }
    public SupplySyncLogInfo get(Object key)
    {
        return(SupplySyncLogInfo)getObject(key);
    }
    public void set(int index, SupplySyncLogInfo item)
    {
        setObject(index, item);
    }
    public boolean contains(SupplySyncLogInfo item)
    {
        return containsObject(item);
    }
    public boolean contains(Object key)
    {
        return containsKey(key);
    }
    public int indexOf(SupplySyncLogInfo item)
    {
        return super.indexOf(item);
    }
}