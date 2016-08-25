package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;

import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.sysmgrcore.manager.UserManager;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class UseraddressList extends DataInfoImpl implements Serializable{
	 protected ListInfo getDataList(String sortKey, boolean desc, long offset,
             int maxPagesize) {
	  
	  		String userName = request.getParameter("userName");
			String userMOBILETEL = request.getParameter("userMOBILETEL");
		
			
//			System.out.println("recursion............"+recursion);
//			System.out.println("userName............"+userName);
//			System.out.println("userRealname............"+userRealname);

       ListInfo listInfo = new ListInfo();
  
     
       try{
    	   
       UserManager userManager = SecurityDatabase.getUserManager();
       
       if(userName==null && userMOBILETEL==null){
    	   String str="select * from td_sm_user order by user_name";  
      
       		listInfo = userManager.getUserList(str,(int)offset,maxPagesize);
       		//return listInfo;
       		
       }
       else {
     	  String sql ="select * from td_sm_user where "+
     		 
 				 "user_name like '%" + userName + "%' and USER_MOBILETEL1 like '%" + userMOBILETEL + "%'";
     	  		 listInfo = userManager.getUserList(sql,(int)offset,maxPagesize);  
        }
       }catch(Exception e){
             e.printStackTrace();
       }
		return listInfo;	
 }	
      
      protected ListInfo getDataList(String arg0, boolean arg1) {
            // TODO Auto-generated method stub
            return null;
      }
}
