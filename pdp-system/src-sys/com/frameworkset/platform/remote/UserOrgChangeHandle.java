package com.frameworkset.platform.remote;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.transaction.RollbackException;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.orm.transaction.TransactionException;
import com.frameworkset.orm.transaction.TransactionManager;

public class UserOrgChangeHandle {
	static final UserOrgChangeHandle userOrgChangeHandle ;
	static {
		userOrgChangeHandle = new UserOrgChangeHandle();
		userOrgChangeHandle.init();
	}
	private UserOrgChangeHandle()
	{
		
	}
	private void init()
	{
		RefreshThread refreshThread = new RefreshThread();
		refreshThread.start();
	}
	public static UserOrgChangeHandle getInstance()
	{
		return userOrgChangeHandle ;
	}
	public List requests = new ArrayList();
	
	public void addRequests(String request,String requestServer)
	{
		try
		{
			synchronized(requests)
			{
				requests.add(request);
				requests.notifyAll();
			}
			System.out.println("requestServer:" + requestServer);
			System.out.println("request event:" + request);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void refreshReadorgname() throws Exception {
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			DBUtil del_db = new DBUtil();
			StringBuffer del_sql = new StringBuffer()
					.append("DELETE FROM V_TB_RES_ORG_USER_WRITE oo WHERE (oo.user_id , oo.org_id) ")
					.append("IN (SELECT user_id ,org_id  FROM V_TB_RES_ORG_USER_WRITE ")
					.append("MINUS  ").append("SELECT user_id,org_id FROM V_TB_RES_ORG_USER)");
			Hashtable[] o = (Hashtable[])del_db.executeDelete(del_sql.toString());
			if(o != null && o.length > 0)
			{
				
				System.out.println("Delete records:" + o[0].get("Rows Affected"));
			}

			DBUtil add_db = new DBUtil();
			StringBuffer add_sql = new StringBuffer().append(
					"insert into V_TB_RES_ORG_USER_WRITE select a.user_id,a.org_id from (")
					.append("(SELECT distinct user_id,org_id  FROM V_TB_RES_ORG_USER ")
					.append("minus ").append(
							"SELECT user_id,org_id FROM V_TB_RES_ORG_USER_WRITE)) a");
			Object o1 = add_db.executeInsert(add_sql.toString());
			System.out.println("Insert records:" + o1 + " rows");
			tm.commit();
		} catch (TransactionException e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			throw e;
		} catch (RollbackException e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			throw e;
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			throw e;
		}

	}

	class RefreshThread extends Thread {
		
		public void run() {
			
			while (true) {
				boolean state = false;
				synchronized (requests) {
					if (requests.size() <= 0) {
						try {
							requests.wait();
							if(requests.size() > 0){
								System.out.println("requests.size() <= 0:" + requests.size());
								requests.clear();
								state = true;
							}
							
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						System.out.println("requests.size() > 0:" + requests.size());
						requests.clear();
						state = true;
						
					}
				
					
				}
				if(state){
					try {
						System.out.println("Refresh table [V_TB_RES_ORG_USER_WRITE] begin.");
						long start = System.currentTimeMillis();
						refreshReadorgname();
						state = false;
						long end = System.currentTimeMillis();
						System.out
								.println("Refresh table [V_TB_RES_ORG_USER_WRITE] end, Spend times:"
										+ (end - start) / 1000 + " seconds.");
						sleep(5000);
					} catch (Exception e1) {
						e1.printStackTrace();
	
					}		
				}
			}
		}
	}
}
