/*
 * Created on 2006-3-10
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.platform.config.ConfigManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.util.ListInfo;

/**
 * @author ok
 * 机构管理 机构下的用户排序
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OrgSubUserList extends DataInfoImpl implements Serializable {
      private Logger log = LoggerFactory.getLogger(OrgSubUserList.class);
      
      protected ListInfo getDataList(String sortKey, boolean desc, long offset,
                  int maxPagesize) {
            ListInfo listInfo = new ListInfo();
            String orgId= request.getParameter("orgId");
            /*是否递归*/
    		String recursion = request.getParameter("intervalType");		
    		recursion = recursion==null?"":recursion;
    		if("0".equalsIgnoreCase(recursion)){
    			recursion = "";
    		}
            try{
	            UserManager userManager = SecurityDatabase.getUserManager();
	            //modify by ge.tao
	            //date 2007-8-1
	            String sql = this.sortSQL(orgId,recursion);
	            
	            listInfo = userManager.getUserInfoList(sql,(int)offset,maxPagesize);  
 			
            }catch(Exception e){
                  e.printStackTrace();
            }
 			return listInfo;	
      }
      
      /**
       * 排序规则不同
       * @param orgId
       * @return 
       * OrgSubUserList.java
       * @author: ge.tao
       */
      public String sortSQL(String orgId,String isRecursion){    	  
    	  String sql = "";
    	  /*递归*/
          if(isRecursion.length()>0){
        	  sql = new UserSearchList().getRecOrderUserSql(orgId,"");
        	  log.warn("机构管理 机构下的用户排序 递归---------------------------"+sql);
          }else{
	          sql ="select a.*,b.x,b.y,b.job_starttime,b.job_fettle from td_sm_user a inner join ("+
			    " select user_id,min(job_sn) x,min(same_job_user_sn) y,"+
	            "min(job_starttime) job_starttime,min(job_fettle) job_fettle from td_sm_userjoborg "+
			    " where org_id = '" + orgId +"' "+
			    " group by user_id  "+
			    " ) b on a.user_id=b.user_id ";
	          //sysmanger.xml配置排序方法的参数 同一机构下按照same_job_user_sn字段排序
	          String orderMethod = ConfigManager.getInstance().getConfigValue("cms.userOrder.orderMethod");
	          if(orderMethod != null && orderMethod.trim().length()>0){ 
	              sql += "order by y,b.user_id";
	          }else{
		          sql += "order by x,y";
	          }
	          log.warn("机构管理 机构下的用户排序 不递归---------------------------"+sql);
          }
          
    	  return sql;
      }
      
      

      protected ListInfo getDataList(String arg0, boolean arg1) {
    	  ListInfo listInfo = new ListInfo();
          String orgId= request.getParameter("orgId");
          /*是否递归*/
          String isRecursion = request.getParameter("isRecursion"); 
          isRecursion = isRecursion==null?"":isRecursion;
          String sql = this.sortSQL(orgId,isRecursion);
          try{
	          UserManager userManager = SecurityDatabase.getUserManager();
	          //modify by ge.tao
	          //date 2007-8-1	          
	          List list = userManager.getUserList(sql);
	          listInfo.setDatas(list);			
          }catch(Exception e){
                e.printStackTrace();
          }
			return listInfo;
      }

}
