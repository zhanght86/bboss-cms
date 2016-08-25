package com.frameworkset.platform.security;

import java.util.ArrayList;
import java.util.List;

import org.frameworkset.event.Event;
import org.frameworkset.event.Listener;
import org.frameworkset.event.NotifiableFactory;

import com.frameworkset.platform.config.BaseSystemInit;
import com.frameworkset.platform.config.DestroyException;
import com.frameworkset.platform.config.InitException;
import com.frameworkset.platform.remote.Utils;
import com.frameworkset.platform.security.event.ACLEventType;

public class MaterailViewRefresh extends BaseSystemInit implements  Listener{
	
	
	
	public void handle(Event e) {
		
		try {
			
			Utils.refreshReadorgname(e.getType().toString());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		
		//DBUtil dbUtil = new DBUtil();
		//try {
			System.out.println("Refresh table [V_TB_RES_ORG_USER_WRITE] begin.");
			long start = System.currentTimeMillis();
			//dbUtil.executeUpdate("call dbms_mview.refresh('V_TB_RES_ORG_USER_WRITE','COMPLETE')");
			long end = System.currentTimeMillis();
			System.out.println("Refresh table [V_TB_RES_ORG_USER_WRITE] end, Spending times:"+(end-start)/1000+" secondes.");
		//} catch (SQLException e1) {
		//	e1.printStackTrace();
		//}
	}

	public void init() throws InitException {
		
		try
		{
				List eventType = new ArrayList();
		        
		    	eventType.add(ACLEventType.USER_ROLE_INFO_CHANGE);
		    	eventType.add(ACLEventType.USER_INFO_CHANGE);
		    	eventType.add(ACLEventType.USER_INFO_ADD);
		    	eventType.add(ACLEventType.USER_INFO_DELETE);
		    	eventType.add(ACLEventType.GROUP_ROLE_INFO_CHANGE);
		    	eventType.add(ACLEventType.GROUP_INFO_CHANGE);
		    	eventType.add(ACLEventType.ROLE_INFO_CHANGE);
		    	
		    	eventType.add(ACLEventType.USER_GROUP_INFO_CHANGE);
		    	eventType.add(ACLEventType.ORGUNIT_INFO_CHANGE);
		    	eventType.add(ACLEventType.ORGUNIT_INFO_ADD);
		    	eventType.add(ACLEventType.ORGUNIT_INFO_DELETE);
		    	eventType.add(ACLEventType.ORGUNIT_ROLE_CHANGE);      
				
				eventType.add(ACLEventType.RESOURCE_ROLE_INFO_CHANGE);
		    	eventType.add(ACLEventType.RESOURCE_INFO_CHANGE);
		    	eventType.add(ACLEventType.PERMISSION_CHANGE);		    
		    	
		    	NotifiableFactory.getNotifiable()
		                .addListener(this,eventType,false);
		        
		       // refreshReadorgname();
		        //System.out.println("register refurbish table  【V_TB_RES_ORG_USER_WRITE】 monitor complete！");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}

	public void destroy() throws DestroyException {
		
	}


}
