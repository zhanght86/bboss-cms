package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Log;
import com.frameworkset.platform.sysmgrcore.manager.db.OrgAdministratorImpl;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;


public class LogSearchList  extends DataInfoImpl implements Serializable{
	 
	 /**
	 * 
	 */
	private static final long serialVersionUID = 4944931965615278488L;
	
	private Logger log = Logger.getLogger(LogSearchList.class);
	    /*
	     * (non-Javadoc)
	     * 
	     * @see com.frameworkset.common.tag.pager.DataInfoImpl#getDataList(java.lang.String,
	     *      boolean)
	     */
	    protected ListInfo getDataList(String arg0, boolean arg1) {
	        return null;
	    }
	    
	    /**
	     * 
	     */
	    protected ListInfo getDataList(String sortKey, boolean desc, long offset,
	            int maxPagesize) {//查询日志信息
	        ListInfo listInfo = new ListInfo();
	        
	        //当前用户ID
	        String curUserId = super.accessControl.getUserID();
	        //判断当前用户是否是部门管理
	        boolean isOrgmanager = new OrgAdministratorImpl().isOrgManager(curUserId);
	        //当前用户登陆名
	        String curUserName = super.accessControl.getUserAccount();
	        //当前用户真实名称
	        String curRealuserName = super.accessControl.getUserName();
	        
	        //操作人帐号
	        String operUser= request.getParameter("operUser");
	        //日志内容
	        String oper= request.getParameter("oper");
	        //
	        String type= request.getParameter("type");
	        //日志模块
	        String logModuel= request.getParameter("logModuel");	
	        //操作起止时间
	        String startDate = request.getParameter("startDate");
	        String endDate = request.getParameter("endDate");
	        //日志所属机构查询
	        String opOrgid = request.getParameter("opOrgid");
	        // 1 为递归查询      0 不递归查询
	        String isRecursion = request.getParameter("isRecursion");	//是否递归查询日志所属机构下的子机构
	        //日志来源
	        String logVisitorial = request.getParameter("logVisitorial");
	        //是否历史日志查询  "n"：不是历史查询   "y"：是历史查询
	        String isHis = request.getParameter("isHis");
	        String table = "TD_SM_LOG";
	        if("y".equals(isHis)){//如果是历史日志查询 则查询历史记录表
	        	table = "TD_SM_LOG_HIS";
	        }
	        
	        try {
//	            if ((operUser==null && oper==null && type==null && startDate==null && endDate==null &&logModuel==null)
//	            		||("".equals(operUser) && "".equals(oper) && "".equals(type) && "".equals(startDate) && "".equals(endDate) && "".equals(logModuel))) {
	            	StringBuffer sql = new StringBuffer()
            		.append("select log.log_operUser ,org.org_name org_name, LOG.op_orgid op_orgid,log.OPER_TYPE ,log.OPER_MODULE,")
//	            		.append("select log.log_operUser ,NVL(org.org_name,'未知') org_name, NVL(LOG.op_orgid,'未知') op_orgid,log.OPER_TYPE ,log.OPER_MODULE,")
	            		.append("log.LOG_CONTENT ,log.log_visitorial ,log.log_operTime,log.log_id  ");
	            	//是系统管理员就全部查出来
	            	if(super.accessControl.isAdmin()){
	            		if(opOrgid != null && !opOrgid.equals("")){//如果查询条件中有机构条件则不查未知机构的日志记录
	            			sql.append("from ").append(table).append(" log, td_sm_organization org where 1 = 1 and trim(log.OP_ORGID)=org.org_id ");
	            		}else{
	            			sql.append("from ").append(table).append(" log left join td_sm_organization org on trim(log.OP_ORGID)=org.org_id where 1 = 1  ");
	            		}
	            	}else if(isOrgmanager){
	            		String concat_ = DBUtil.getDBAdapter().concat(" org_tree_level","'|%' ");
	            		StringBuffer all_orgs = new StringBuffer()//可管理机构集合
		    				.append("select org_id,org_name from td_sm_organization where org_id in (")
		    				.append("select distinct org.org_id from td_sm_organization org where org.org_tree_level like ")
		    				.append("(select ")
		    				.append(concat_)
		    				.append(" from TD_SM_ORGANIZATION o,td_sm_orgmanager om ")
		    				.append(" where o.org_id = om.org_id and om.user_id = '")
		    				.append(curUserId).append(" ')")
		    				.append(" or org.org_id in ( select e.org_id from TD_SM_ORGANIZATION e,td_sm_orgmanager org where e.org_id = org.org_id and org.user_id = '")
		    				.append(curUserId).append(" ')")
		    				.append(")");
//		    				.append("select distinct org.org_id from td_sm_organization org start with org.org_id in(")
//		    				.append("select o.org_id from td_sm_organization o, td_sm_orgmanager om where ")
//		    				.append("o.org_id = om.org_id and om.user_id='").append(curUserId).append("') ")
//		    				.append(" connect by prior org.org_id = org.parent_id) ");
//	            		if(opOrgid != null && !opOrgid.equals("")){//如果查询条件中有机构条件则不查未知机构的日志记录
	            			sql.append("from ").append(table).append(" log ,(").append(all_orgs).append(") org where 1 = 1  ")
	            				.append(" and org.org_id=TRIM(log.OP_ORGID) ");
//	            		}else{
//		            		sql.append("from ").append(table).append(" log left join (").append(all_orgs).append(") org on ")
//		            			.append(" org.org_id=TRIM(log.OP_ORGID) ");
//	            		}
//	            		sql.append("  from (select log_operUser, OPER_TYPE, LOG_CONTENT, log_visitorial, log_operTime, log_id ")
//	            			.append(" from TD_SM_LOG log where log.log_operuser in(select user_name || ':' || user_realname ")
//	            			.append(" from td_sm_user where user_id in(select user_id from td_sm_userjoborg ")
//	            			.append(" where org_id in (select distinct org.org_id from td_sm_organization org ")
//	            			.append(" start with org.org_id in (select o.org_id from td_sm_organization o, ")
//	            			.append(" td_sm_orgmanager om where o.org_id = om.org_id and om.user_id = '")
//	            			.append(curUserId).append("') ")
//	            			.append("connect by prior org.org_id = org.parent_id)))) log where 1=1 ");
	            	}else{
	            		sql.append(" from ").append(table).append(" log,td_sm_organization org where (trim(log.log_operuser) = '")
	            			.append(curUserName).append("' or trim(log.log_operuser) = '")
	            			.append(curUserName+":"+curRealuserName).append("') and  ")
	            			.append(" trim(log.OP_ORGID)=org.org_id ");
	            	}
	            		
//	            	sql.append("order by log.log_operTime desc");	                
//	                listInfo = this.getQueryResult(sql.toString(),offset,maxPagesize);
//	            } 
//	            else
//	            {
//	            	StringBuffer sql = new StringBuffer()
//	            	.append("select log.log_operUser,org.org_name ,log.OP_ORGID,log.OPER_TYPE ,log.OPER_MODULE,")
//            			.append("log.LOG_CONTENT ,log.log_visitorial ,log.log_operTime,log.log_id  ");
//	            	//是系统管理员就全部查出来
//	            	if(super.accessControl.isAdmin()){
//	            		sql.append("from TD_SM_LOG log left join td_sm_organization org on log.OP_ORGID=org.org_id  where 1=1 ");
//	            	}else{
//	            		sql.append("from (select log_operUser, OPER_TYPE, LOG_CONTENT, log_visitorial, log_operTime, log_id ")
//	            			.append(" from TD_SM_LOG where log_operuser in(select user_name || ':' || user_realname ")
//	            			.append("from td_sm_user where user_id in(select user_id from td_sm_userjoborg ")
//	            			.append("where org_id in (select distinct org.org_id from td_sm_organization org ")
//	            			.append("start with org.org_id in (select o.org_id from td_sm_organization o, ")
//	            			.append("td_sm_orgmanager om where o.org_id = om.org_id and om.user_id = '")
//	            			.append(curUserId).append("') ")
//	            			.append("connect by prior org.org_id = org.parent_id)))) log where 1=1 ");
//	            	}
	            	
	                if (operUser!=null && !"".equals(operUser)) {
	                	sql.append(" and log.log_operUser like '%").append(operUser).append("%' ");
	                }
	                if (logModuel!=null && !"".equals(logModuel)) {
	                	sql.append(" and log.OPER_MODULE like '%").append(logModuel).append("%' ");
	                }
	                if (oper!=null && !"".equals(oper)) {
	                	sql.append(" and log.LOG_CONTENT like '%").append(oper).append("%' ");
	                }
	                if (type!=null && !type.equals("")) {
	                	sql.append(" and log.OPER_TYPE like '%").append(type).append("%' ");
	                }
	                
	                if (startDate!=null && !startDate.equals("")) {
	                	String dateString = DBUtil.getDBDate(startDate+" 00:00:00");
	                	sql.append(" and log.log_operTime >=").append(dateString);
	                }
	                if (endDate!=null && !endDate.equals("")) {
	                	String dateString = DBUtil.getDBDate(endDate+" 23:59:59");
	                	sql.append(" and log.log_operTime <=").append(dateString);
	                }
	                if(opOrgid != null && !opOrgid.equals("")){
	                	if(isRecursion.equals("0")){
	                		sql.append(" and trim(log.OP_ORGID) = '").append(opOrgid).append("' ");
	                	}else if(isRecursion.equals("1")){
	                		String concat_ = DBUtil.getDBAdapter().concat(" org_tree_level","'|%' ");
	                		sql.append(" and trim(log.OP_ORGID) in (select t.org_id from TD_SM_ORGANIZATION t where t.org_tree_level like ")
                			.append("  (select ")
                			.append(concat_)
                			.append(" from TD_SM_ORGANIZATION c where c.org_id = '").append(opOrgid).append("' )")
                			.append(" or t.org_id ='").append(opOrgid).append("'")
                			.append(")");
                			
//	                		sql.append(" and trim(log.OP_ORGID) in (select org_id from td_sm_organization start ")
//	                			.append(" with org_id='").append(opOrgid).append("' connect by prior org_id=parent_id ")
//	                			.append(") ");
	                	}
	                }
	                if(logVisitorial != null && !logVisitorial.equals("")){
	                	sql.append(" and log.LOG_VISITORIAL like '%").append(logVisitorial).append("%' ");
	                }
//	                System.out.println(sql.toString());
	                listInfo = this.getQueryResult(sql.append(" order by log.log_operTime desc").toString(),offset,maxPagesize);
	                
//	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	       
	        	 return listInfo;
	       
	       
	    }

	    /**
	     * 
	     * @param sql
	     * @return 
	     * LogSearchList.java
	     * @author: ge.tao
	     */
	    protected ListInfo getQueryResult(String sql,long offset,int maxItem){
//	    	System.out.println("sql = " + sql);
	    	List datas = new ArrayList();
	    	ListInfo listInfo = new ListInfo();
	    	DBUtil db = new DBUtil();	    	
	    	try{	 
	    		db.executeSelect(sql,offset,maxItem);
                   		
	    		Log log = null;
	    		for(int i=0; i<db.size(); i++){
	    			log = new Log();
	    			
	    			/*
	    			 * log_operUser字段在td_sm_log表中值组成 --> 账号 : 真实名
	    			 * 实际数据库中的值有以下三种:(1) : (2) 账号 (3) 账号 :　真实名
	    			 * 为了显示统一，在web页面暂时取账号或真实名之一
	    			 */
	    			String operUser = db.getString(i,"log_operUser".toUpperCase());
	    			
	    			String []operNameOrRealName = operUser.split(":");
	    			
	    			if(operNameOrRealName != null)
	    			{
	    				if(operNameOrRealName.length == 0)
	    				{
	    					log.setOperUser("");
	    				}
	    				else if(operNameOrRealName.length == 1)
	    				{
	    					log.setOperUser(operNameOrRealName[0]);
	    				}
	    				else if(operNameOrRealName.length == 2)
	    				{
	    					if(operNameOrRealName[0].equals(""))
	    					{
	    						log.setOperUser(operNameOrRealName[1]);
	    					}
	    					else
	    					{
	    						log.setOperUser(operNameOrRealName[0]);
	    					}
	    				}
	    			}
	    			else
	    			{
	    				log.setOperUser("");
	    			}
	    			
	    			//log.setOperUser(db.getString(i,"log_operUser".toUpperCase()));
	    			log.setLogId(Integer.valueOf(db.getString(i, "LOG_ID")));	    			
	    			log.setOper(db.getString(i,"LOG_CONTENT"));
	    			log.setVisitorial(db.getString(i,"LOG_VISITORIAL"));
	    			log.setOperTime(db.getTimestamp(i,"LOG_OPERTIME"));
	    			log.setOperOrg(db.getString(i,"ORG_NAME"));
	    			log.setLogModule(db.getString(i,"OPER_MODULE"));	    			
	    			log.setOperType(db.getInt(i,"OPER_TYPE"));
	    			//log.setOperOrg(db.getString(i, "OP_ORGID"));
	    			
	    			datas.add(log);
				}
	    		listInfo.setDatas(datas);
	    		listInfo.setTotalSize(db.getTotalSize());
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    	return listInfo;
	    }
}
