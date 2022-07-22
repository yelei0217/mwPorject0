package com.kingdee.eas.fi.ap.app;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.bos.dao.IObjectPK;
import com.kingdee.bos.dao.IObjectValue;
import com.kingdee.eas.basedata.framework.app.ParallelSqlExecutor;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.BusinessFormOAFactory;
import com.kingdee.eas.custom.EAISynTemplate;
import com.kingdee.eas.custom.IBusinessFormOA;
import com.kingdee.eas.custom.app.DateBaseProcessType;
import com.kingdee.eas.custom.app.DateBasetype;
import com.kingdee.eas.custom.app.unit.AppUnit;
import com.kingdee.eas.custom.app.unit.PayRequestBillUtil;
import com.kingdee.eas.custom.util.DBUtil;
import com.kingdee.eas.fi.ap.PayRequestBillBizException;
import com.kingdee.eas.fi.ap.PayRequestBillInfo;
import com.kingdee.eas.fi.ar.BillStatusEnum;
import com.kingdee.jdbc.rowset.IRowSet;
import com.kingdee.util.NumericExceptionSubItem;

public class PayRequestBillControllerBeanEx
  extends PayRequestBillControllerBean
{
  private static final long serialVersionUID = 5827061991178115974L;
  
  protected IObjectPK _submit(Context arg0, IObjectValue arg1)
    throws BOSException, EASBizException
  {
    IObjectPK pk = null;
    PayRequestBillInfo info = (PayRequestBillInfo)arg1;
    if (!"ffo858UjRfivG8YQeDT328znrtQ=".equals(info.getCompany().getId().toString()))
    {
      String personid = "";
      if ((info.getString("oacaigoushenqingdandanhao") != null) && (!"".equals(info.getString("oacaigoushenqingdandanhao"))))
      {
        Map map1 = AppUnit.getPurchModelFromOANum(arg0, info.getString("oacaigoushenqingdandanhao"));
        if ((map1.get("person") != null) && (!"".equals(map1.get("person"))))
        {
          String message = "";
          personid = (String)map1.get("person");
          if (PayRequestBillUtil.queryPersonStatus(arg0, personid))
          {
            pk = super._submit(arg0, arg1);
            IBusinessFormOA iof = BusinessFormOAFactory.getLocalInstance(arg0);
            AppUnit.insertLog(arg0, DateBaseProcessType.Update, DateBasetype.PaymentBillToMid, info.getNumber(), info.getString("PaymentBillToMid"), "付款申请单提交方法开始");
            String result = iof.PayApplyToOA("03", pk.toString());
            AppUnit.insertLog(arg0, DateBaseProcessType.Update, DateBasetype.PaymentBillToMid, info.getNumber(), info.getString("PaymentBillToMid"), "付款申请单提交方法结束", result);
          }
          else
          {
            personid = info.getApplyer().getId().toString();
            if (PayRequestBillUtil.queryPersonStatus(arg0, personid))
            {
              pk = super._submit(arg0, arg1);
              IBusinessFormOA iof = BusinessFormOAFactory.getLocalInstance(arg0);
              AppUnit.insertLog(arg0, DateBaseProcessType.Update, DateBasetype.PaymentBillToMid, info.getNumber(), info.getString("PaymentBillToMid"), "付款申请单提交方法开始");
              String result = iof.PayApplyToOA("03", pk.toString());
              AppUnit.insertLog(arg0, DateBaseProcessType.Update, DateBasetype.PaymentBillToMid, info.getNumber(), info.getString("PaymentBillToMid"), "付款申请单提交方法结束", result);
            }
            else
            {
              throw new EASBizException(new NumericExceptionSubItem("人员已离职", "采购申请单【采购人员】与付款申请单【申请人】都为不在职员工，不能提交该单据"));
            }
          }
        }
      }
      else
      {
        personid = info.getApplyer().getId().toString();
        if (PayRequestBillUtil.queryPersonStatus(arg0, personid))
        {
          pk = super._submit(arg0, arg1);
          IBusinessFormOA iof = BusinessFormOAFactory.getLocalInstance(arg0);
          AppUnit.insertLog(arg0, DateBaseProcessType.Update, DateBasetype.PaymentBillToMid, info.getNumber(), info.getString("PaymentBillToMid"), "付款申请单提交方法开始");
          String result = iof.PayApplyToOA("03", pk.toString());
          AppUnit.insertLog(arg0, DateBaseProcessType.Update, DateBasetype.PaymentBillToMid, info.getNumber(), info.getString("PaymentBillToMid"), "付款申请单提交方法结束", result);
        }
        else
        {
          throw new EASBizException(new NumericExceptionSubItem("人员已离职", "付款申请单申请人已离职"));
        }
      }
    }
    else
    {
      pk = super._submit(arg0, arg1);
    }
    return pk;
  }
  
  private void modifyEasSign(Context ctx, String number)
  {
    String sql = "/*dialect*/ update eas_lolkk_fk set eassign = -1,eastime = CONVERT(varchar,GETDATE(),120) where fnumber ='" + number + "'";
    try
    {
      EAISynTemplate.execute(ctx, "03", sql);
    }
    catch (BOSException e)
    {
      e.printStackTrace();
    }
  }
  
  protected void _ignore(Context ctx, IObjectPK pk)
    throws BOSException, EASBizException
  {
    PayRequestBillInfo info = getPayRequestBillInfo(ctx, pk);
    String oasign = flowIsOver(ctx, info.getNumber());
    if (info.getBillStatus() == BillStatusEnum.SUBMITED)
    {
      if ("-1".equals(oasign)) {
        throw new BOSException("未查询到中间表数据。");
      }
      if ("1".equals(oasign)) {
        throw new BOSException("OA已完成审批流程，请联系管理员手动处理。");
      }
      if ("0".equals(oasign))
      {
        modifyEasSign(ctx, info.getNumber());
        super._unfreeze(ctx, pk, info);
        
        updateBillStatus(ctx, pk.toString());
      }
    }
    else if (info.getBillStatus() == BillStatusEnum.AUDITED)
    {
      if (!ExistsMapping(ctx, info.getId().toString()))
      {
        if ("-1".equals(oasign)) {
          throw new BOSException("未查询到中间表数据。");
        }
        if ("1".equals(oasign)) {
          throw new BOSException("OA已完成审批流程，请联系管理员手动处理。");
        }
        if ("0".equals(oasign))
        {
          modifyEasSign(ctx, info.getNumber());
          super.unpassAudit(ctx, pk, info);
          super._unfreeze(ctx, pk, info);
          
          updateBillStatus(ctx, pk.toString());
        }
      }
      else
      {
        throw new PayRequestBillBizException(PayRequestBillBizException.LOCK_NOT_ZERO_CANNOT_IGNORE);
      }
    }
    else
    {
      super._ignore(ctx, pk);
    }
  }
  
  private void updateBillStatus(Context ctx, String fid)
  {
    String sql = " update T_AP_PayRequestBill set FBillStatus = 2 where fid ='" + fid + "' and FBillStatus = 4";
    try
    {
      DBUtil.execute(ctx, sql);
      
      StringBuffer sbr = new StringBuffer("update t_ap_Otherbillentry set FLockUnVerifyAmt = FRecievePayAmount, FLockUnVerifyAmtLocal = FRecievePayAmountLocal  ,FLockVerifyAmt = 0, FLockVerifyAmtLocal= 0 ");
      sbr.append(" where FPARENTID ='").append(fid).append("'");
      DBUtil.execute(ctx, sbr);
    }
    catch (BOSException e)
    {
      e.printStackTrace();
    }
  }
  
  private void writeBackOtherBill(Context ctx, List<String> fids)
  {
    if ((fids != null) && (fids.size() > 0))
    {
      ExecutorService pool = Executors.newFixedThreadPool(6);
      ParallelSqlExecutor pe = new ParallelSqlExecutor(pool);
      for (String fid : fids)
      {
        StringBuffer sbr = new StringBuffer("update t_ap_Otherbillentry set FLockUnVerifyAmt = FRecievePayAmount, FLockUnVerifyAmtLocal = FRecievePayAmountLocal ,FLockVerifyAmt =, FLockVerifyAmtLocal= ");
        sbr.append(" where FPARENTID  ='").append(fid).append("'");
        
        StringBuffer sbr1 = new StringBuffer("update t_ap_Otherbillentry set FLockUnVerifyAmt = FAMOUNT ,FLOCKUNVERIFYAMTLOCAL = FAMOUNT ,FLOCKVERIFYAMT =0,FLOCALWRITTENOFFAMOUNT =0,FLOCKVERIFYQTY =0 ");
        sbr.append(" where fid ='").append(fid).append("'");
        
        pe.getSqlList().add(sbr);
      }
      if (pe.getSqlList().size() > 0) {
        try
        {
          pe.executeUpdate(ctx);
          pool.shutdown();
        }
        catch (EASBizException e)
        {
          e.printStackTrace();
          pool.shutdown();
        }
        catch (BOSException e)
        {
          e.printStackTrace();
          pool.shutdown();
        }
      }
      pool.shutdown();
    }
  }
  
  private String flowIsOver(Context ctx, String number)
  {
    String result = "";
    String sql = "SELECT count(1) C from eas_lolkk_fk where fnumber ='" + number + "'";
    try
    {
      List<Map<String, Object>> list = EAISynTemplate.query(ctx, "03", sql);
      if ((list != null) && (list.size() > 0))
      {
        Map<String, Object> map = (Map)list.get(0);
        if ("0".equals(map.get("C").toString()))
        {
          result = "-1";
        }
        else
        {
          sql = "SELECT OAFINISHSIGN from eas_lolkk_fk where fnumber ='" + number + "'";
          list = EAISynTemplate.query(ctx, "03", sql);
          if ((list != null) && (list.size() > 0))
          {
            map = (Map)list.get(0);
            String oafinishsign = map.get("OAFINISHSIGN").toString();
            if ("0".equals(oafinishsign)) {
              result = "0";
            } else if ("1".equals(oafinishsign)) {
              result = "1";
            } else if ("2".equals(oafinishsign)) {
              result = "0";
            } else {
              result = "-1";
            }
          }
          else
          {
            result = "-1";
          }
        }
      }
    }
    catch (BOSException e)
    {
      e.printStackTrace();
    }
    return result;
  }
  
  protected void _antiAudit(Context arg0, IObjectPK arg1)
    throws BOSException, EASBizException
  {
    super._antiAudit(arg0, arg1);
  }
  
  private static boolean ExistsMapping(Context ctx, String fromid)
  {
    boolean isExists = false;
    
    String sql = "select count(1) as C from t_bot_relation  where FSrcEntityID = 'D001019A' and FDestEntityID = '40284E81' and FSrcObjectID ='" + fromid + "' ";
    try
    {
      IRowSet rs = DBUtil.executeQuery(ctx, sql);
      if ((rs != null) && (rs.size() > 0)) {
        while (rs.next()) {
          if ((rs.getObject("C") != null) && (!"".equals(rs.getObject("C").toString()))) {
            if (Integer.parseInt(rs.getObject("C").toString()) == 0) {
              isExists = false;
            } else {
              isExists = true;
            }
          }
        }
      }
    }
    catch (BOSException e)
    {
      e.printStackTrace();
    }
    catch (SQLException e)
    {
      e.printStackTrace();
    }
    return isExists;
  }
  
  protected void _delete(Context ctx, IObjectPK pk)
    throws BOSException, EASBizException
  {
    PayRequestBillInfo info = getPayRequestBillInfo(ctx, pk);
    if ((info.getBillStatus() == BillStatusEnum.SUBMITED) || (info.getBillStatus() == BillStatusEnum.SAVE)) {
      modifyEasSign(ctx, info.getNumber());
    }
    super._delete(ctx, pk);
  }
}
