package com.frameworkset.platform.sysmgrcore.purviewmanager.tag;

import java.io.Serializable;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.manager.JobManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

public class JobSearchList  extends DataInfoImpl implements Serializable{
	 
	    protected ListInfo getDataList(String sortKey, boolean desc, long offset,
	            int maxPagesize) {//查询岗位信息
	        ListInfo listInfo = new ListInfo();
	        
	        String jobId = StringUtil.replaceNull(request.getParameter("jobId"));
	        
	        String jobName = StringUtil.replaceNull(request.getParameter("jobName"));	        
	        String jobDesc = StringUtil.replaceNull(request.getParameter("jobDesc"));
	        String jobNumber = StringUtil.replaceNull(request.getParameter("jobNumber"));
	        String jobRank = StringUtil.replaceNull(request.getParameter("jobRank"));
	        String jobAmount = StringUtil.replaceNull(request.getParameter("jobAmount"));
	        String jobFunction = StringUtil.replaceNull(request.getParameter("jobFunction"));
	        String jobCondition = StringUtil.replaceNull(request.getParameter("jobCondition"));
	        String jobCreatorName = StringUtil.replaceNull(request.getParameter("creatorName"));
	      
	        String flag =request.getParameter("flag");
	        boolean first = true;
	        
	        try {
	            JobManager jobManager = SecurityDatabase.getJobManager();
	            
	            String sql = "";
	            if (jobName.equals("") && jobId.equals("") && jobDesc.equals("")
	            		&& jobNumber.equals("") && jobRank.equals("") && jobAmount.equals("")
	            		&& jobFunction.equals("") && jobCondition.equals("")&& jobCreatorName.equals("")) 
	            {
	            	sql = "select * from td_sm_Job j  order by j.JOB_NUMBER asc"; 
	            } 
	            else if(flag!=null && flag.equals("1"))
	            {
	            	sql = "select * from td_sm_Job j  order by j.JOB_NUMBER asc";
	            }
	            else 
	            {
	                sql = "select * from td_sm_Job j,TD_SM_USER u where u.user_id=j.owner_id ";
	                if (!jobName.equals("")) {
	                    if (first) {
	                       sql += "and j.job_Name like '%" + jobName + "%'";
	                       first = false;
	                    } else {
	                        sql += " and j.job_Name like '%" + jobName + "%'";
	                    }
	                }
	                if (!jobId.equals("")) {
	                    if (first) {
	                       sql += " and j.job_Id like '%" + jobId + "%'";
	                        first = false;
	                    } else {
	                        sql += " and j.job_Id like '%" + jobId + "%'";
	                   }
	                }
	                if (!jobDesc.equals("")) {
	                    if (first) {
	                        sql += "and j.job_Desc like '%" + jobDesc + "%'";
	                       first = false;
	                    } else {
	                        sql += " and j.job_Desc like '%" + jobDesc + "%'";
	                   }
	                }
	                if (!jobNumber.equals("")) {
	                   if (first) {
	                       sql += "and j.job_Number like '%" + jobNumber + "%'";
	                       first = false;
	                   } else {
	                        sql += " and j.job_Number like '%" + jobNumber + "%'";
	                    }
	                }
	                if (!jobRank.equals("")) {
	                   if (first) {
	                        sql += "and j.job_Rank like '%" + jobRank + "%'";
	                       first = false;
	                   } else {
	                        sql += " and j.job_Rank like '%" + jobRank + "%'";
	                    }
	                }
	                if (!jobAmount.equals("")) {
	                    if (first) {
	                       sql += "and j.job_Amount like '%" + jobAmount + "%'";
	                       first = false;
	                  } else {
	                        sql += " and j.job_Amount like '%" + jobAmount + "%'";
	                  }
	                }
	                if (!jobFunction.equals("")) {
	                   if (first) {
	                       sql += "and j.job_Function like '%" + jobFunction + "%'";
	                       first = false;
	                    } else {
	                        sql += " and j.job_Function like '%" + jobFunction + "%'";
	                    }
	                }
//	                if(jobFunction != null && !"".equals(jobFunction))
//	                {
//	                	sql += "and j.job_Function like '%" + jobFunction + "%'";
//	                }
	                if (!jobCondition.equals("")) {
	                    if (first) {
	                        sql += "and j.job_Condition like '%" + jobCondition + "%'";
	                        first = false;
	                    } else {
	                        sql += " and j.job_Condition like '%" + jobCondition + "%'";
	                    }
	                }
	                if (!jobCreatorName.equals("")) {
	                    if (first) {
	                        sql += " and u.user_name like '%" + jobCreatorName + "%'";
	                        first = false;
	                    } else {
	                        sql += " and u.user_name like '%" + jobCreatorName + "%'";
	                    }
	                }
	            }
//	            //System.out.println(sql);
//	            DBUtil db = new DBUtil();
//	            //查处所有角色
//	            db.executeSelect(sql);
	            StringBuffer rightJobs = new StringBuffer();
	            rightJobs.setLength(0);
	            //通过条件过滤， 找出复合条件的角色
//	            for(int i=0;i<db.size();i++){
//	            	String job_id = db.getString(i,"job_Id");
//	            	if(super.accessControl.checkPermission(job_id,
//	            			"jobset",
//							AccessControl.JOB_RESOURCE)){
//	            		if(rightJobs.length()==0){
//	            			rightJobs.append(" job.job_Id = '" ).append(job_id).append("' ");
//	            		}else{
//	            			rightJobs.append(" or job.job_Id = '" ).append(job_id).append("' ");
//	            		}
//	                }
//	            }
	            //构造符合查询条件的 sql语句
	            
	            if(rightJobs.length()>0){
	            	StringBuffer sqlstr = new StringBuffer().append("select * from td_sm_Job job  ");
	            	sqlstr.append("where ").append(rightJobs);
	            	rightJobs.setLength(0);
	            	listInfo = jobManager.getJobList(sqlstr.toString(), offset, maxPagesize);
	            }
	            else
	            {
	            	listInfo = jobManager.getJobList(sql, offset, maxPagesize);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	       
	        return listInfo;
	    }

	    /*
	     * (non-Javadoc)
	     * 
	     * @see com.frameworkset.common.tag.pager.DataInfoImpl#getDataList(java.lang.String,
	     *      boolean)
	     */
	    protected ListInfo getDataList(String arg0, boolean arg1) {
	        // TODO Auto-generated method stub
	        return null;
	    }

}
