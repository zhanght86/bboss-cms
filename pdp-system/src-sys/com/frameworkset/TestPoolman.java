package com.frameworkset;

import java.io.Serializable;
import java.sql.SQLException;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;

public class TestPoolman implements Serializable
{
	public static void main(String[] args) {
//      Framework frameworkinit = new Framework();
//      frameworkinit.init("templates/module.xml");
//		String path = "d:/tets.x";
//		int idx = path.indexOf('/');
//		System.out.println(path.substring(0,idx));
		
//			Framework.getInstance();
		
		testprekeygenerator();
		
//		for(int i = 0; i < 10; i ++)
//		{
//			new TestDB(i).start();
//			
//		}
//		//new TestDB(0).delete();
//		new TestDB(0).prepareInsert();
//		
  }
	
	static class TestDB extends Thread
	{
		static final int count = 10;
		int j = 0;
		public TestDB(int i)
		{
			this.j = i;
//			System.out.println(j + "% 2=" +(j % 2));
		}
		public void run()
		{
			int i = j % 6;
			switch(i)
			{
			case 0:
				insert();break;
			case 1:
				selectpagining();break;
			case 2:
				select();break;
			case 3:
				update();break;
			case 4:
				delete();break;
			case 5:
				prepareInsert();break;
			}
		}
		
		void insert()
		{
			String sql = "insert into test(name) values('biaoping.yin')";
			for(int i = 0; i < 1000;i ++)
			{
				DBUtil dbUtil = new DBUtil();
				try {
					dbUtil.executeInsert("oadx",sql);
					System.out.println("Thread[" + j + "] " + i + " dbUtil.size:" + dbUtil.size());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//SQLManager.getInstance().destroyPools();
		}
		
		void select()
		{
			String sql = "select * from test";
			for(int i = 0; i < 1000;i ++)
			{
				DBUtil dbUtil = new DBUtil();
				try {
					dbUtil.executeSelect("oadx",sql);
					System.out.println("Thread[" + j + "] " + i + " dbUtil.size:" + dbUtil.size());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		void update()
		{
			String sql = "update test set name=''";
			for(int i = 0; i < 1000;i ++)
			{
				DBUtil dbUtil = new DBUtil();
				try {
					dbUtil.executeUpdate("oadx",sql);
					System.out.println("Thread[" + j + "] " + i + " dbUtil.size:" + dbUtil.size());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		void delete()
		{
			String sql = "delete from test";
			for(int i = 0; i < 1000;i ++)
			{
				DBUtil dbUtil = new DBUtil();
				try {
					dbUtil.executeUpdate("oadx",sql);
					System.out.println("Thread[" + j + "] " + i + " dbUtil.size:" + dbUtil.size());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		  
		void prepareInsert()
		{
			for(int i = 0; i < 100; i ++)
			{
				PreparedDBUtil p = new PreparedDBUtil();
				
				try {
					p.setBatchDBName("oadx");
					for(int j = 0; j < 10; j ++)
					{
						p.addBatch("insert into test(name) values('biaoping.yin')");
					}
					p.executeBatch();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		void selectpagining()
		{
			String sql = "select * from test";
			for(int i = 0; i < 1000;i ++)
			{
				DBUtil dbUtil = new DBUtil();
				try {
					dbUtil.executeSelect("oadx",sql,0,10);
					System.out.println("Thread[" + j + "] " + i + " dbUtil.size:" + dbUtil.size());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 测试带前缀的表主键的生成机制测试方法
	 *
	 */
	public static void testprekeygenerator()
	{
		DBUtil dbutil = new DBUtil();
		try {
			dbutil.executeInsert("insert into testpre(name) values('test')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
