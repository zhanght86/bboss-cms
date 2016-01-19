/*
 * Created on 2006-2-22
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.frameworkset.platform.sysmgrcore.manager.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.RollbackException;

import org.apache.log4j.Logger;
import org.frameworkset.event.Event;
import org.frameworkset.event.EventHandle;
import org.frameworkset.event.EventImpl;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.orm.transaction.TransactionException;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.security.event.ACLEventType;

import com.frameworkset.platform.sysmgrcore.entity.Job;
import com.frameworkset.platform.sysmgrcore.entity.Organization;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.platform.sysmgrcore.entity.Userjoborg;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.JobManager;
import com.frameworkset.util.ListInfo;

/**
 * 项目：SysMgrCore <br>
 * 描述：用户管理实现类 <br>
 * 版本：1.0 <br>
 * 
 * @author biaoping.yin,gao.tang
 */
public class JobManagerImpl extends EventHandle implements JobManager {



	private static Logger logger = Logger.getLogger(JobManagerImpl.class
			.getName());

	public boolean deleteJob(Job job) throws ManagerException { // 删除岗位
		boolean r = false;
		DBUtil dbUtil = new DBUtil();
		TransactionManager tm = new TransactionManager();
		if (job != null) {
			try {
				
				tm.begin();
				// 删除当前岗位的所关联的 Userjoborg 对象
				dbUtil.addBatch("delete from TD_SM_USERJOBORG where job_id='" + job.getJobId() + "'");

				// 删除当前岗位的所关联的Orgjob 对象
				dbUtil.addBatch("delete from td_sm_orgjob where job_id='" + job.getJobId() + "'");

				//删除当前岗位的所关联的orgjobrole表外键 gao.tang 2007.10.26
				dbUtil.addBatch("delete from td_sm_orgjobrole where job_id='" + job.getJobId() + "'");
				
				//删除当前岗位的所关联的资源授予记录
				dbUtil.addBatch("delete from TD_SM_roleresop  where restype_id='job' and res_id ='" + job.getJobId() + "'");
				
				// 删除指定的用户实例
				dbUtil.addBatch("delete from td_sm_job where job_id='" + job.getJobId() + "'");
				
				dbUtil.executeBatch();
				tm.commit();
				r = true;
				Event event = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
				super.change(event,true);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				dbUtil.resetBatch();
			}
		}

		return r;

	}
    
	/**
	 * 删除岗位 的机构岗位关系..错误的删除。。参见机构管理类 
	 * com.frameworkset.platform.sysmgrcore.manager.db.OrgManagerImpl().deleteOrgjob(String orgid, String[] jobids)
	 */
	public boolean deleteOrgjob(Job job) throws ManagerException {
		boolean r = false;

		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_DELETE);
//
//			// 删除所关联的机构岗位
//			p.setObject("from Orgjob oj where oj.id.jobId = '" + job.getJobId()
//					+ "'");
//			cb.execute(p);
			/**
			 * modify by ge.tao
			 * date 2007-10-25
			 */
			String jobid = job.getJobId();
            String sql = "delete from TD_SM_ORGJOB where JOB_ID='"+jobid+"' ";
            DBUtil db = new DBUtil();
            db.executeDelete(sql);
			r = true;
			Event event = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
			super.change(event,true);
		} catch (Exception e) {
			logger.error(e);
			throw new ManagerException(e.getMessage());
		}

		return r;
	}

	/**
	 * 去掉hibernate后的方法：
	 */
	public boolean deleteJob(String jobId) throws ManagerException { // 根据岗位Id删除岗位
		boolean r = false;

//		try {
//			cb.setAutoCommit(false);
//			// 删除当前岗位的所关联的 Userjoborg 对象
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_DELETE);
//			p.setObject("from Userjoborg ujo where ujo.id.jobId = '" + jobId
//					+ "'");
//			cb.execute(p);
//
//			// 删除当前岗位的所关联的Orgjob 对象
//			p.setObject("from Orgjob oj where oj.id.jobId = '" + jobId + "'");
//			cb.execute(p);
//
//			// 删除指定的岗位实例
//			p.setObject("from Job j where j.jobId = '" + jobId + "'");
//			cb.execute(p);
//			cb.commit(true);
//			r = true;
//
//		} catch (ControlException e) {
//			try {
//				cb.rollback(true);
//			} catch (ControlException e1) {
//				logger.error(e1);
//			}
//			throw new ManagerException(e.getMessage());
//		}
		TransactionManager tm = new TransactionManager();
		// 删除当前岗位的所关联的 Userjoborg 对象
		String sql_Userjoborg = "delete from td_sm_userjoborg where job_id='"+jobId+"'";
		// 删除当前岗位的所关联的Orgjob 对象
		String sql_Orgjob = "delete from td_sm_orgjob where job_id='"+jobId+"'";
		// 删除指定的岗位实例
		String sql = "delete from td_sm_job where job_id='"+jobId+"'";
		DBUtil db = new DBUtil();
		try {
			tm.begin();
			db.addBatch(sql_Userjoborg);
			db.addBatch(sql_Orgjob);
			db.addBatch(sql);
			db.executeBatch();
			tm.commit();
			r = true;
		} catch (TransactionException e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (SQLException e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		return r;
	}

	/**
	 * 没有被使用的方法
	 * @deprecated 不推荐使用该方法，方法实现已被注释
	 */
	public boolean deleteUserjoborg(Userjoborg userjoborg)
			throws ManagerException { // 删除岗位与用户岗位机构的关系
		boolean r = false;

//		if (userjoborg != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_DELETE);
//				p.setObject(userjoborg);
//				if (cb.execute(p) != null)
//					r = true;
//			} catch (ControlException e) {
//				logger.error(e);
//			}
//		}

		return r;
	}

	/**
	 * 去掉hibernate后的方法
	 */
	public Job getJob(String propName, String value) throws ManagerException { // 根据名称取岗位

		Job job = null;
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p.setObject("from Job job where job." + propName + "='" + value
//					+ "'");
//			List list = (List) cb.execute(p);
//
//			// wwx: 增加 !list.isEmpty()
//			if (list != null && !list.isEmpty())
//				job = (Job) list.get(0);
//		} catch (ControlException e) {
//			throw new ManagerException(e.getMessage());
//		}
		String sql = "select * from td_sm_job where " + propName + "='" + value + "'";
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			job = getJob(db);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return job;
	}
	
	/**
	 * 根据DBUtil执行的结果得到Job  -- 注意执行的sql列不能是count(*)
	 * @param db
	 * @return
	 */
	public Job getJob(DBUtil db){
		if(db.size() > 0){
			Job job = new Job();
			try {
				job.setJobId(db.getString(0, "job_id"));
			} catch (SQLException e) {
			
			}
			try{
				job.setJobName(db.getString(0, "job_name"));
			} catch (SQLException e) {
			
			}
			try{
				job.setJobAmount(db.getString(0, "JOB_AMOUNT"));
			} catch (SQLException e) {
			
			}
			try{
				job.setJobCondition(db.getString(0, "JOB_CONDITION"));
			} catch (SQLException e) {
			}
			try{
				job.setJobDesc(db.getString(0, "JOB_DESC"));
			} catch (SQLException e) {
			
			}
			try{
				job.setJobFunction(db.getString(0, "JOB_FUNCTION"));
			} catch (SQLException e) {
			}
			try{
				job.setJobNumber(db.getString(0, "JOB_NUMBER"));
			} catch (SQLException e) {
			
			}
			try{
				job.setJobRank(db.getString(0, "JOB_RANK"));
			} catch (SQLException e) {
			
			}
			try{
				job.setOwner_id(db.getInt(0, "OWNER_ID"));
			} catch (SQLException e) {
			
			}
			
			return job;
		}
		return null;
	}
	
	/**
	 * 根据DBUtil执行的结果得到Jobs  -- 注意执行的sql列不能是count(*)
	 * @param db
	 * @return
	 */
	public Job getJob(Job job,Record db){
		
		
		try {
			job.setJobId(db.getString("JOB_ID"));
		} catch (SQLException e) {
		
		}
		try{
			job.setJobName(db.getString("JOB_NAME"));
		} catch (SQLException e) {
		
		}
		try{
			job.setJobAmount(db.getString("JOB_AMOUNT"));
		} catch (SQLException e) {
		
		}
		try{
			job.setJobCondition(db.getString("JOB_CONDITION"));
		} catch (SQLException e) {
		}
		try{
			job.setJobDesc(db.getString( "JOB_DESC"));
		} catch (SQLException e) {
		
		}
		try{
			job.setJobFunction(db.getString( "JOB_FUNCTION"));
		} catch (SQLException e) {
		}
		try{
			job.setJobNumber(db.getString( "JOB_NUMBER"));
		} catch (SQLException e) {
		
		}
		try{
			job.setJobRank(db.getString( "JOB_RANK"));
		} catch (SQLException e) {
		
		}
		try{
			job.setOwner_id(db.getInt( "OWNER_ID"));
		} catch (SQLException e) {
		
		}
			
		
		return job;
	}
	
	
	/**
	 * 根据DBUtil执行的结果得到Jobs  -- 注意执行的sql列不能是count(*)
	 * @param db
	 * @return
	 */
	public List getJobList(DBUtil db){
		if(db.size() > 0){
			List list = new ArrayList();
			Job job = null;
			for(int i = 0; i < db.size(); i++){
				job = new Job();
				try {
					job.setJobId(db.getString(i, "JOB_ID").trim());
				} catch (SQLException e) {
				
				}
				try{
					job.setJobName(db.getString(i, "JOB_NAME").trim());
				} catch (SQLException e) {
				
				}
				try{
					job.setJobAmount(db.getString(i, "JOB_AMOUNT").trim());
				} catch (SQLException e) {
				
				}
				try{
					job.setJobCondition(db.getString(i, "JOB_CONDITION").trim());
				} catch (SQLException e) {
				}
				try{
					job.setJobDesc(db.getString(i, "JOB_DESC").trim());
				} catch (SQLException e) {
				
				}
				try{
					job.setJobFunction(db.getString(i, "JOB_FUNCTION").trim());
				} catch (SQLException e) {
				}
				try{
					job.setJobNumber(db.getString(i, "JOB_NUMBER").trim());
				} catch (SQLException e) {
				
				}
				try{
					job.setJobRank(db.getString(i, "JOB_RANK").trim());
				} catch (SQLException e) {
				
				}
				try{
					job.setOwner_id(db.getInt(i, "OWNER_ID"));
				} catch (SQLException e) {
				
				}
				list.add(job);
			}
			
			return list;
		}
		return null;
	}

	/**
	 * 没有被使用的方法
	 * @deprecated 不推荐使用该方法，方法实现已被注释
	 */
	public List getJobList(String propName, String value, boolean isLike)
			throws ManagerException {

//		List list = null;
//
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			if (!isLike)
//				p.setObject("from Job j where j." + propName + " = '" + value
//						+ "'");
//			else
//				p.setObject("from Job j where j." + propName + " like '"
//						+ value + "'");
//
//			list = (List) cb.execute(p);
//		} catch (ControlException e) {
//			throw new ManagerException(e.getMessage());
//		}
//
//		return list;
		return null;
	}

	public List getJobList() throws ManagerException {
		List list = new ArrayList();
		try {
			String sql = "Select * from td_sm_job t where t.job_id<>'1' order by t.job_name";
			DBUtil dBUtil = new DBUtil();
			dBUtil.executeSelect(sql);
			list = getJobList(dBUtil);
			
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return list;
	}
	public ListInfo selectjoblist(String jobname,String jobnumber,long offset,int maxsize) throws ManagerException {
		ListInfo datas = null;
		try {
//			String sql = "Select * from td_sm_job t where t.job_id<>'1' where JOB_NAME = ? and jobnumber = ? order by t.job_name";
			
			ConfigSQLExecutor executor = new ConfigSQLExecutor("com/frameworkset/platform/sysmgrcore/manager/db/job.xml");
			Map conditions = new HashMap();
			conditions.put("jobname", jobname);
			conditions.put("jobnumber", jobnumber);
			datas = executor.queryListInfoBeanByRowHandler(new RowHandler<Job>(){

				@Override
				public void handleRow(Job job, Record record)
						throws Exception {
					
					try {
						job.setJobId(record.getString("JOB_ID").trim());
					} catch (Exception e) {
					
					}
					try{
						job.setJobName(record.getString("JOB_NAME").trim());
					} catch (Exception e) {
					
					}
					try{
						job.setJobAmount(record.getString("JOB_AMOUNT").trim());
					} catch (Exception e) {
					
					}
					try{
						job.setJobCondition(record.getString("JOB_CONDITION").trim());
					} catch (Exception e) {
					}
					try{
						job.setJobDesc(record.getString("JOB_DESC").trim());
					} catch (Exception e) {
					
					}
					try{
						job.setJobFunction(record.getString("JOB_FUNCTION").trim());
					} catch (Exception e) {
					}
					try{
						job.setJobNumber(record.getString("JOB_NUMBER").trim());
					} catch (Exception e) {
					
					}
					try{
						job.setJobRank(record.getString("JOB_RANK").trim());
					} catch (Exception e) {
					
					}
					try{
						job.setOwner_id(record.getInt("OWNER_ID"));
					} catch (Exception e) {
					
					}
				}
				
			},Job.class, "selectjoblist", offset, maxsize, conditions);
			
			
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return datas;
	}

	public List getJobList(String hql) throws ManagerException {
		List list = new ArrayList();
		try {
			DBUtil dBUtil = new DBUtil();
			dBUtil.executeSelect(hql);
			list = getJobList(dBUtil);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return list;
	}
	
	public ListInfo getJobList(String hql,long offset,int maxsize) throws ManagerException {
		
	
		try {
			
			return SQLExecutor.queryListInfoByRowHandler(new RowHandler<Job>() {

				
				public void handleRow(Job job,Record origine) throws Exception {
					 getJob(job,origine);
					
				}
			}, Job.class,hql, offset, maxsize);
		
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	
	}

	public List getJobList(Organization org) throws ManagerException { // 根据机构取岗位
		List list = new ArrayList();
		if(org != null)
		{
			try {
				String sql = "select j.* from td_sm_job j, td_sm_orgjob oj " +
						"where j.job_id<>'1' and " +
						"j.job_id = oj.job_id " +
						"and oj.org_id = '" + org.getOrgId() + "' " +
						"order by oj.job_sn,j.job_name asc ";
				DBUtil dBUtil = new DBUtil();
				dBUtil.executeSelect(sql);
				list = getJobList(dBUtil);
			} catch (Exception e) {
				logger.error(e);
				e.printStackTrace();
			}
		}		
		return list;
	}

	/**
	 * 去掉hibernate后的方法
	 */
	public List getJobList(User user) throws ManagerException { // 根据用户取岗位
		List list = null;

//		if (user != null) {
//			try {
//				Parameter p = new Parameter();
//				p.setCommand(Parameter.COMMAND_GET);
//				p
//						.setObject("from Job job where job.jobId in ("
//								+ "select ujo.id.jobId from Userjoborg ujo where ujo.id.userId = '"
//								+ user.getUserId()
//								+ "')order by job.jobNumber asc");
//				list = (List) cb.execute(p);
//			} catch (ControlException e) {
//				logger.error(e);
//			}
//		}
		if(user != null){
			String sql = "select * from td_sm_job where job_id in(select job_id from td_sm_userjoborg "
				+ "where user_id='"+user.getUserId()+"') order by JOB_NUMBER asc";
			DBUtil db = new DBUtil();
			try {
				db.executeSelect(sql);
				list = getJobList(db);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;

	}

	/**
	 * 没有被使用的方法
	 * @deprecated 不推荐使用该方法，方法实现已被注释
	 */
	public List getJobList(Organization org, int type) throws ManagerException {// (根据机构取岗位列表，王卓添加)
		List list = new ArrayList();
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			String strSelect = "";
//			if (type == TYPE_ORGJOB) {
//				strSelect = "from Job job,Orgjob orgjob where job.jobId = orgjob.id.jobId and orgjob.id.orgId = '"
//						+ org.getOrgId() + "' order by orgjob.jobSn asc";
//			} else if (type == TYPE_USERJOBORG) {
//				strSelect = "from Job job,Userjoborg ujo where job.jobId = ujo.id.jobId and ujo.id.orgId = '"
//						+ org.getOrgId() + "' order by ujo.jobSn asc";
//			}
//			p.setObject(strSelect);
//			List jobList = (List) cb.execute(p);
//			list = new ArrayList();
//
//			for (int i = 0; i < jobList.size(); i++) {
//				Object objs[] = (Object[]) jobList.get(i);
//				if (!list.contains(objs[0]))
//					list.add(objs[0]);
//			}
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}

		return list;
	}

	/**
	 * 去掉hibernate后的方法
	 */
	public List getJobList(Organization org, User user) throws ManagerException {
		List list = null;
		if (org == null || user == null)
			return list;
//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			String strSelect = "";
//
//			//隐藏在职显示 job.jobId<>'1'
//			strSelect = "from Job job,Userjoborg ujo where job.jobId<>'1' and job.jobId = ujo.id.jobId and ujo.id.orgId = '"
//					+ org.getOrgId()
//					+ "' and ujo.id.userId = '"
//					+ user.getUserId() + "' order by ujo.jobSn asc";
//
//			p.setObject(strSelect);
//			List jobList = (List) cb.execute(p);
//			list = new ArrayList();
//
//			for (int i = 0; i < jobList.size(); i++) {
//				Object objs[] = (Object[]) jobList.get(i);
//				if (!list.contains(objs[0]))
//					list.add(objs[0]);
//			}
//		} catch (ControlException e) {
//			logger.error(e);
//			throw new ManagerException(e.getMessage());
//		}
		String sql = "select j.* from td_sm_job j,td_sm_userjoborg o where j.job_id<>'1' and "
			+ "j.job_id=o.job_id and o.org_id='" + org.getOrgId()+"' and o.user_id='" + user.getUserId()
			+ "' order by o.JOB_SN asc";
		//System.out.println("sql = " + sql);
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			list = this.getJobList(db);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	/**
	 * 去掉hibernate后的方法
	 */
	public boolean isJobExist(String jobName) throws ManagerException { // 根据岗位名判断岗位是否存在
		boolean r = false;

//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p.setObject("from Job job where job.jobName='" + jobName + "'");
//			List list = (List) cb.execute(p);
//
//			if (list != null && list.size() > 0)
//				r = true;
//		} catch (ControlException e) {
//			logger.error(e);
//		}
		String sql = "select count(1) from td_sm_job where job_name='" + jobName + "'";
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			if(db.getInt(0, 0) > 0){
				r = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return r;
	}

	/**
	 * 去掉hibernate后的方法
	 */
	public boolean isJobNumber(String jobNumber) throws ManagerException { // 根据岗位名判断岗位编号是否存在
		boolean r = false;

//		try {
//			Parameter p = new Parameter();
//			p.setCommand(Parameter.COMMAND_GET);
//			p.setObject("from Job job where job.jobNumber='" + jobNumber + "'");
//			List list = (List) cb.execute(p);
//
//			if (list != null && list.size() > 0)
//				r = true;
//		} catch (ControlException e) {
//			logger.error(e);
//		}
		String sql = "select count(1) from td_sm_job where JOB_NUMBER='"+jobNumber+"'";
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			if(db.getInt(0, 0) > 0){
				r = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return r;
	}

	public boolean isJobExistNumber(String jobId, String jobNumber)
			throws ManagerException {
		boolean r = false;
		DBUtil dbUtil = new DBUtil();

		String sql = "select JOB_NUMBER from TD_SM_JOB where JOB_ID <>'"
				+ jobId + "'";
		try {
			dbUtil.executeSelect(sql);
			for (int i = 0; i < dbUtil.size(); i++) {
				// System.out.println(jobNumber);
				if (dbUtil.getString(i, "JOB_NUMBER").equals(jobNumber))
					r = true;
				if (jobNumber.equals(""))
					r = false;

			}
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return r;
	}

	/**
	 * @deprecated 不推荐使用该方法，方法实现已被注释
	 */
	public void refresh(Job job) throws ManagerException { // 刷新

	}

	/**
	 * 去掉hibernate后的方法:  此方法只支持岗位更新
	 */
	public boolean storeJob(Job job) throws ManagerException { // 存储岗位
		/*
		 * Parameter p = new Parameter(); p.setCommand(Parameter.COMMAND_STORE);
		 * p.setObject(job);
		 * 
		 * Boolean r = new Boolean(false); try { r = (Boolean) cb.execute(p); }
		 * catch (ControlException e) { e.printStackTrace(); } return
		 * r.booleanValue();
		 */
		boolean r = false;

		// 保存
//		Parameter p = new Parameter();
//		p.setCommand(Parameter.COMMAND_STORE);
//		p.setObject(job);
//
//		try {
//			if (cb.execute(p) != null) {
//				r = true;
//			}
//		} catch (ControlException e) {
//			e.printStackTrace();
//			throw new ManagerException(e.getMessage());
//		}
		StringBuffer sql = new StringBuffer()
			.append("update td_sm_job set JOB_NAME='").append(job.getJobName()==null?"":job.getJobName().trim()).append("',")
			.append("JOB_DESC='").append(job.getJobDesc()==null?"":job.getJobDesc().trim()).append("',")
			.append("JOB_FUNCTION='").append(job.getJobFunction()==null?"":job.getJobFunction().trim()).append("',")
			.append("JOB_AMOUNT='").append(job.getJobAmount()==null?"":job.getJobAmount().trim()).append("',")
			.append("JOB_NUMBER='").append(job.getJobNumber()==null?"":job.getJobNumber().trim()).append("',")
			.append("JOB_CONDITION='").append(job.getJobCondition()==null?"":job.getJobCondition().trim()).append("',")
			.append("JOB_RANK='").append(job.getJobRank()==null?"":job.getJobRank().trim()).append("' where ")
			.append("JOB_ID='").append(job.getJobId()).append("'");
		DBUtil db = new DBUtil();
		try {
			db.executeUpdate(sql.toString());
			r = true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return r;
	}

	/**
	 * 没有被使用的方法
	 * @deprecated 不推荐使用该方法，方法实现已被注释
	 */
	public Job loadAssociatedSet(String jobId, String associated)
			throws ManagerException { // 装载指定岗位对象的关联对象
//		Job jobRel = null;
//
////		try {
////
////			Parameter par = new Parameter();
////			par.setCommand(Parameter.COMMAND_GET);
////			par.setObject("from Job job left join fetch job." + associated
////					+ " where job.jobId = '" + jobId + "'");
////
////			List list = (List) cb.execute(par);
////			if (list != null && !list.isEmpty()) {
////				jobRel = (Job) list.get(0);
////			}
////		} catch (ControlException e) {
////			logger.error(e);
////		}
//
//		return jobRel;
		return null;
	}



	public boolean isContainJob(Organization org) throws ManagerException {
		boolean r = false;
		DBUtil db = new DBUtil();
//		String sql = "select count(*) from td_sm_orgjob where org_id='"
//				+ org.getOrgId() + "'";
//		不出现在职
		String sql = "select count(*) from td_sm_orgjob where org_id='"
			+ org.getOrgId() + "' and job_id<>'1'";
		try {
			db.executeSelect(sql);
			if (db.getInt(0, 0) > 0) {
				r = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return r;
	}

	public boolean deleteJobroleByJobId(String jobId, String orgId)
			throws ManagerException {
		boolean r = false;
		DBUtil db = new DBUtil();

		if (jobId != null && !jobId.equals("")) {
			String sql = "delete td_sm_orgjobrole jr where jr.job_id='" + jobId
					+ "' and jr.org_id = '" + orgId + "'";
			try {
				db.executeDelete(sql);
				r = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Event event = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
		super.change(event,true);
		return r;
	}
	
	

	public boolean addJobroleMap(String jobId, String orgId, String roleids[])
			throws ManagerException {
		boolean state = false;

		PreparedDBUtil db = new PreparedDBUtil();
		if (jobId != null && roleids != null && roleids.length > 0
				&& !jobId.equals("")) {
			StringBuffer sql_b = new StringBuffer();
			sql_b.append("insert all when totalsize <= 0 then into td_sm_orgjobrole(job_id, org_id, role_id) values ('")
				.append(jobId).append("','").append(orgId).append("','");
			int length = sql_b.length();
			try {
				if(roleids.length > 0){
					for (int i = 0; i < roleids.length; i++) {
						sql_b.append(roleids[i]).append("') select count(1) as totalsize from td_sm_orgjobrole ")
							.append("where job_id='").append(jobId).append("' and org_id='").append(orgId).append("' ")
							.append(" and role_id='").append(roleids[i]).append("'");
						db.addBatch(sql_b.toString());
						sql_b.setLength(length);
					}
					db.executeBatch();
					state = true;
					Event event = new EventImpl("", ACLEventType.USER_ROLE_INFO_CHANGE);
					super.change(event,true);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.resetBatch();
			}
		}
		return state;
	}

	public ListInfo getJobOrgList(String jobId, String orgnumber, String orgName,
			long offset, int maxPagesize) throws ManagerException {
		DBUtil dbUtil = new DBUtil();
		List list = new ArrayList();
		ListInfo listInfo = new ListInfo();

		StringBuffer hsql = new StringBuffer(
				"select a.org_id,a.org_name,a.org_sn,a.orgdesc,a.orgnumber,a.remark5 "
						+ "from td_sm_organization a, td_sm_orgjob b "
						+ "where a.org_id = b.org_id " + "and b.job_id = '"
						+ jobId + "'");

		if (orgnumber != null && !orgnumber.equals("")) {
			hsql.append(" and a.orgnumber like '%" + orgnumber + "%'");
		}
		if (orgName != null && !orgName.equals("")) {
			hsql.append(" and a.org_name like '%" + orgName + "%'");
		}

		hsql.append(" order by a.org_sn asc");
		try {
			dbUtil.executeSelect(hsql.toString(), (int) offset, maxPagesize);
			for (int i = 0; i < dbUtil.size(); i++) {
				Organization organization = new Organization();
				organization.setOrgId(dbUtil.getString(i, "org_id"));
				organization.setOrgName(dbUtil.getString(i, "org_name"));
				organization.setOrgnumber(dbUtil.getString(i, "orgnumber"));
				organization.setOrgdesc(dbUtil.getString(i, "orgdesc"));
				organization.setOrgSn(dbUtil.getString(i, "org_sn"));
				organization.setRemark5(dbUtil.getString(i, "remark5"));
				list.add(organization);
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listInfo;
	}
	
	public ListInfo getJobOrgShowList(String jobId, String orgnumber, String orgShowName, long offset, int maxPagesize) throws ManagerException {
		
		DBUtil dbUtil = new DBUtil();
		List list = new ArrayList();
		ListInfo listInfo = new ListInfo();

		StringBuffer hsql = new StringBuffer(
				"select a.org_id,a.org_name,a.org_sn,a.orgdesc,a.orgnumber,a.remark5 "
						+ "from td_sm_organization a, td_sm_orgjob b "
						+ "where a.org_id = b.org_id " + "and b.job_id = '"
						+ jobId + "'");

		if (orgnumber != null && !orgnumber.equals("")) {
			hsql.append(" and a.orgnumber like '%" + orgnumber + "%'");
		}
		if (orgShowName != null && !orgShowName.equals("")) {
			hsql.append(" and a.remark5 like '%" + orgShowName + "%'");
		}

		hsql.append(" order by a.org_sn asc");
		try {
			dbUtil.executeSelect(hsql.toString(), (int) offset, maxPagesize);
			for (int i = 0; i < dbUtil.size(); i++) {
				Organization organization = new Organization();
				organization.setOrgId(dbUtil.getString(i, "org_id"));
				organization.setOrgName(dbUtil.getString(i, "org_name"));
				organization.setOrgnumber(dbUtil.getString(i, "orgnumber"));
				organization.setOrgdesc(dbUtil.getString(i, "orgdesc"));
				organization.setOrgSn(dbUtil.getString(i, "org_sn"));
				organization.setRemark5(dbUtil.getString(i, "remark5"));
				list.add(organization);
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listInfo;
	}
	
//	private Job dbutilToJob(DBUtil dBUtil) throws SQLException
//	{
//		if(dBUtil.size()>0){
//			Job job = new Job();					
//			job.setJobId(dBUtil.getString(0, "JOB_ID"));
//			job.setJobName(dBUtil.getString(0, "JOB_NAME"));
//			job.setJobDesc(dBUtil.getString(0, "JOB_DESC"));
//			job.setJobFunction(dBUtil.getString(0, "JOB_FUNCTION"));
//			job.setJobAmount(dBUtil.getString(0, "JOB_AMOUNT"));
//			job.setJobNumber(dBUtil.getString(0, "JOB_NUMBER"));
//			job.setJobCondition(dBUtil.getString(0, "JOB_CONDITION"));
//			job.setJobRank(dBUtil.getString(0, "JOB_RANK"));
//			job.setOwner_id(dBUtil.getInt(0,"OWNER_ID"));
//			return job;
//		}
//		return null;
//	}
	
//	private List dbutilToJobList(DBUtil dBUtil) throws SQLException
//	{
//		List list = new ArrayList();
//		for(int i = 0; i < dBUtil.size(); i ++)
//		{
//			Job job = new Job();
//			job.setJobId(dBUtil.getString(i, "JOB_ID"));
//			job.setJobName(dBUtil.getString(i, "JOB_NAME"));
//			job.setJobDesc(dBUtil.getString(i, "JOB_DESC"));
//			job.setJobFunction(dBUtil.getString(i, "JOB_FUNCTION"));
//			job.setJobAmount(dBUtil.getString(i, "JOB_AMOUNT"));
//			job.setJobNumber(dBUtil.getString(i, "JOB_NUMBER"));
//			job.setJobCondition(dBUtil.getString(i, "JOB_CONDITION"));
//			job.setJobRank(dBUtil.getString(i, "JOB_RANK"));
//			job.setOwner_id(dBUtil.getInt(i, "OWNER_ID"));
//			list.add(job);
//		}
//		return list;
//	}

	public Job getJobById(String jobId) throws ManagerException 
	{
		try
		{
			String sql = "Select * from td_sm_job t where t.job_id='" + jobId + "'";
			DBUtil dBUtil = new DBUtil();
			dBUtil.executeSelect(sql);
			Job job = new Job();
			job = getJob(dBUtil);
			return job;
		}
		catch(Exception e)
		{
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}

	public Job getJobByName(String jobName) throws ManagerException {
		try
		{
			String sql = "Select * from td_sm_job t where t.JOB_NAME='" + jobName + "'";
			DBUtil dBUtil = new DBUtil();
			dBUtil.executeSelect(sql);
			Job job = new Job();
			job = getJob(dBUtil);
			return job;
		}
		catch(Exception e)
		{
			logger.error(e);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 根据用户ID与岗位ID得到用户所创建的岗位，用户对自己所创建的岗位有所有的权限
	 * @param userId
	 * @return
	 * @throws ManagerException
	 */
	public Job getByCreatorJobId(String userId, String jobId) throws ManagerException {
		DBUtil db = new DBUtil();
		String sql = "select * from td_sm_job where owner_id='"+userId+"' and job_id='"+jobId+"'";
		try {
			db.executeSelect(sql);
			Job job = new Job();
			job = getJob(db);
			return job;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	/**
	 * 根据用户ID与岗位ID得到用户所创建的岗位，用户对自己所创建的岗位有所有的权限
	 * @param userId
	 * @return
	 * @throws ManagerException
	 */
	public static boolean isJobCreatorByUserId(String userId, String jobId) throws ManagerException {
		DBUtil db = new DBUtil();
		String sql = "select count(1) from td_sm_job where owner_id='"+userId+"' and job_id='"+jobId+"'";
		try {
			db.executeSelect(sql);
			if(db.size() > 0){
				return db.getInt(0,0) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 添加岗位
	 * @param job
	 * @return
	 * @throws ManagerException
	 * @author gao.tang 
	 */
	public boolean saveJob(Job job) throws ManagerException {
		PreparedDBUtil preparedDBUtil = new PreparedDBUtil();
		StringBuffer sql = new StringBuffer()
		.append("insert into TD_SM_JOB(job_id,JOB_NAME,JOB_DESC,JOB_FUNCTION,JOB_AMOUNT,JOB_NUMBER,")
		.append("JOB_CONDITION,JOB_RANK,OWNER_ID) values(?,?,?,?,?,?,?,?,?)");
		
		
//		.append(job.getJobName()).append("','").append(job.getJobDesc()==null?"":job.getJobDesc()).append("','")
//		.append(job.getJobFunction()==null?"":job.getJobFunction()).append("','")
//		.append(job.getJobAmount()==null?"":job.getJobAmount()).append("','")
//		.append(job.getJobNumber()==null?"":job.getJobNumber()).append("','")
//		.append(job.getJobCondition()==null?"":job.getJobCondition()).append("','")
//		.append(job.getJobRank()==null?"":job.getJobRank()).append("',")
//		.append(job.getOwner_id()).append(")");
		try {
			preparedDBUtil.preparedInsert(sql.toString());
			String jobId = preparedDBUtil.getNextStringPrimaryKey("TD_SM_JOB");
			preparedDBUtil.setString(1, jobId);
			preparedDBUtil.setString(2, job.getJobName());
			preparedDBUtil.setString(3, job.getJobDesc());
			preparedDBUtil.setString(4, job.getJobFunction());
			preparedDBUtil.setString(5, job.getJobAmount());
			preparedDBUtil.setString(6, job.getJobNumber());
			preparedDBUtil.setString(7, job.getJobCondition());
			preparedDBUtil.setString(8, job.getJobRank());
			preparedDBUtil.setInt(9, job.getOwner_id());
			preparedDBUtil.executePrepared();
			return true;
		} catch (SQLException e1) {
			e1.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 修改岗位信息
	 * @param job
	 * @return
	 * @throws ManagerException
	 * @author gao.tang 
	 */
	public boolean updateJob(Job job) throws ManagerException {
		StringBuffer sql = new StringBuffer()
			.append("update td_sm_job set JOB_NAME='").append(job.getJobName()).append("',JOB_DESC='")
			.append(job.getJobDesc()).append("',JOB_FUNCTION='").append(job.getJobFunction())
			.append("',JOB_AMOUNT='").append(job.getJobAmount()).append("',JOB_NUMBER='")
			.append(job.getJobNumber()).append("',JOB_CONDITION='").append(job.getJobCondition())
			.append("',JOB_RANK='").append(job.getJobRank()).append("' where JOB_ID='")
			.append(job.getJobId()).append("'");
		DBUtil db = new DBUtil();
		try {
			db.executeUpdate(sql.toString());
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static void main(String[] args){
		try {
			JobManagerImpl.isJobCreatorByUserId("1", "27");
		} catch (ManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}