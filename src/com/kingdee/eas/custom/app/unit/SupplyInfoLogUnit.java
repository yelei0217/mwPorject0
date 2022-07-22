package com.kingdee.eas.custom.app.unit;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import com.alibaba.fastjson.JSON;
import com.kingdee.bos.BOSException;
import com.kingdee.bos.Context;
import com.kingdee.eas.common.EASBizException;
import com.kingdee.eas.custom.SupplySyncLogFactory;
import com.kingdee.eas.custom.SupplySyncLogInfo;
import com.kingdee.eas.custom.app.DateBaseProcessType;
import com.kingdee.eas.custom.app.DateBasetype;

public class SupplyInfoLogUnit {

	public static void insertLog(Context ctx, DateBaseProcessType processType,DateBasetype baseType,String name,String number){
		try {
			SupplySyncLogInfo loginfo=new SupplySyncLogInfo();
			Calendar cal=Calendar.getInstance();
			cal.setTime(new Date());
			String version=String.valueOf(cal.getTimeInMillis());
			loginfo.setProcessType(processType);
			loginfo.setNumber(cal.getTimeInMillis()+"."+number);
			loginfo.setName(name);
			loginfo.setSimpleName(number);
			loginfo.setDateBaseType(baseType);
			loginfo.setVersion(version);
			loginfo.setUpdateDate(new Date());
			loginfo.setStatus(true);
			SupplySyncLogFactory.getLocalInstance(ctx).save(loginfo);
		} catch (EASBizException e) {
 			e.printStackTrace();
		} catch (BOSException e) {
 			e.printStackTrace();
		}
	} 
	
	public static void insertLog(Context ctx, DateBaseProcessType processType,DateBasetype baseType,String name,String number,String description){
		try {
			SupplySyncLogInfo loginfo=new SupplySyncLogInfo();
			Calendar cal=Calendar.getInstance();
			cal.setTime(new Date());
			String version=String.valueOf(cal.getTimeInMillis());
			loginfo.setProcessType(processType);
			loginfo.setNumber(cal.getTimeInMillis()+"."+number);
			loginfo.setName(name);
			loginfo.setSimpleName(number);
			loginfo.setDateBaseType(baseType);
			loginfo.setVersion(version);
			loginfo.setUpdateDate(new Date());
			loginfo.setStatus(true);
			loginfo.setDescription(description);
			SupplySyncLogFactory.getLocalInstance(ctx).save(loginfo);
		} catch (EASBizException e) {
 			e.printStackTrace();
		} catch (BOSException e) {
 			e.printStackTrace();
		}
	} 
	
	public static void insertLog(Context ctx, DateBaseProcessType processType,DateBasetype baseType,String name,String number,String description,String message){
		try {
			SupplySyncLogInfo loginfo=new SupplySyncLogInfo();
			Calendar cal=Calendar.getInstance();
			cal.setTime(new Date());
			String version=String.valueOf(cal.getTimeInMillis());
			loginfo.setProcessType(processType);
			loginfo.setNumber(cal.getTimeInMillis()+"."+number);
			loginfo.setName(name);
			loginfo.setSimpleName(number);
			loginfo.setDateBaseType(baseType);
			loginfo.setVersion(version);
			loginfo.setUpdateDate(new Date());
			loginfo.setStatus(true);
			loginfo.setDescription(description);
			loginfo.setMessage(message);
			SupplySyncLogFactory.getLocalInstance(ctx).save(loginfo);
		} catch (EASBizException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BOSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	} 
	
	public static void requestOAInterface(Map mp) {
        // ��½ Url  
        String loginUrl = "http://oa.meiweigroup.com:8001/seeyon/main.do?method=login";  
        // ���½����ʵ� Url  
        String dataUrl = "http://oa.meiweigroup.com:8001/seeyon/lolkk/thirdUrlController.do?method=syncSupplierInfo";  
        HttpClient httpClient = new HttpClient();  
        // ģ���½����ʵ�ʷ�������Ҫ��ѡ�� Post �� Get ����ʽ  
        PostMethod postMethod = new PostMethod(loginUrl);  
        // ���õ�½ʱҪ�����Ϣ���û���������  
        NameValuePair[] data = { new NameValuePair("login_username", "bd3"), new NameValuePair("login_password", "meiwei2020") };  
         
//        
//        // ��½ Url  
//        String loginUrl = "http://43.254.45.43:8080/seeyon/main.do?method=login";  
//        // ���½����ʵ� Url  
//        String dataUrl = "http://43.254.45.43:8080/seeyon/lolkk/thirdUrlController.do?method=syncSupplierInfo";  
//        HttpClient httpClient = new HttpClient();  
//        // ģ���½����ʵ�ʷ�������Ҫ��ѡ�� Post �� Get ����ʽ  
//        PostMethod postMethod = new PostMethod(loginUrl);  
//        // ���õ�½ʱҪ�����Ϣ���û���������  
//        NameValuePair[] data = { new NameValuePair("login_username", "dengchangchi"), new NameValuePair("login_password", "111111") };  
        
        postMethod.setRequestBody(data);  
        try {  
            // ���� HttpClient ���� Cookie,���������һ���Ĳ���  
            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);  
            httpClient.executeMethod(postMethod);  
            // ��õ�½��� Cookie  
            Cookie[] cookies = httpClient.getState().getCookies();  
            StringBuffer tmpcookies = new StringBuffer();  
            for (Cookie c : cookies) {  
                tmpcookies.append(c.toString() + ";"); 
            }
            String body = JSON.toJSONString(mp);
            RequestEntity bbb = new StringRequestEntity (body ,"application/json" ,"UTF-8");
            PostMethod postMethod1 = new PostMethod(dataUrl);  
            // ÿ�η�������Ȩ����ַʱ�����ǰ��� cookie ��Ϊͨ��֤  
            postMethod1.setRequestHeader("cookie", tmpcookies.toString());   
            // ��ӡ���������ݣ�����һ���Ƿ�ɹ� 
            postMethod1.setRequestEntity(bbb);
            postMethod1.setRequestHeader("Content-Type","application/json");
            httpClient.executeMethod(postMethod1);  
            String text = postMethod1.getResponseBodyAsString();  
             
            
            System.out.println(text);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	}
	
}
