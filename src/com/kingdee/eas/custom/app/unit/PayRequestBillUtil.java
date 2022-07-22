/*    */ package com.kingdee.eas.custom.app.unit;
/*    */ 
/*    */ import com.kingdee.bos.BOSException;
/*    */ import com.kingdee.bos.Context;
/*    */ import com.kingdee.eas.custom.util.VerifyUtil;
/*    */ import com.kingdee.eas.util.app.DbUtil;
/*    */ import com.kingdee.jdbc.rowset.IRowSet;
/*    */ import java.sql.SQLException;
/*    */ 
/*    */ 
/*    */ public class PayRequestBillUtil
/*    */ {
/*    */   public static boolean queryPersonStatus(Context ctx, String fid) {
/* 14 */     boolean flag = false;
/* 15 */     if (VerifyUtil.notNull(fid)) {
/*    */       try {
/* 17 */         String sql = "select COUNT(1) C from V_HR_PersonMain_oa where FID ='" + fid + "' and FEmployeeTypeName ='ÆôÓÃ' ";
/* 18 */         IRowSet rs = DbUtil.executeQuery(ctx, sql);
/* 19 */         if (rs.next() && 
/* 20 */           rs.getObject("C") != null && !"".equals(rs.getObject("C").toString()) && 
/* 21 */           Integer.valueOf(rs.getObject("C").toString()).compareTo(Integer.valueOf(1)) >= 0) {
/* 22 */           flag = true;
/*    */         }
/*    */       }
/* 25 */       catch (BOSException e) {
/* 26 */         e.printStackTrace();
/* 27 */       } catch (SQLException e) {
/* 28 */         e.printStackTrace();
/*    */       } 
/*    */     }
/* 31 */     return flag;
/*    */   }
/*    */ }