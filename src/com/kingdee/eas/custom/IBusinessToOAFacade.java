package com.kingdee.eas.custom;

import com.kingdee.bos.BOSException;
//import com.kingdee.bos.metadata.*;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.util.*;
import com.kingdee.bos.Context;

import com.kingdee.bos.Context;
import com.kingdee.bos.BOSException;
import java.util.List;
import java.lang.String;
import com.kingdee.bos.framework.*;
import com.kingdee.bos.util.*;

public interface IBusinessToOAFacade extends IBizCtrl
{
    public void updateMaterialInfo(String mId) throws BOSException;
    public void syncSupplyInfoAuto() throws BOSException;
    public void disableSupplyInfo(List ids) throws BOSException;
    public void ableSupplyInfo(List ids) throws BOSException;
}