package com.frameworkset.platform.sysmgrcore.manager.db;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.RollbackException;

import org.apache.log4j.Logger;
import org.frameworkset.event.EventHandle;
import org.frameworkset.persitent.util.SQLUtil;
import org.frameworkset.spi.assemble.Pro;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.orm.adapter.DB;
import com.frameworkset.orm.transaction.TransactionException;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.Log;
import com.frameworkset.platform.sysmgrcore.entity.LogDetail;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;

/**
 * <p>
 * 类名称：日志操作接口
 * </p>
 * <p>
 * 类说明：主要包括日志的增删改功能，在地税大集中系统中要求
 * </p>
 * 
 * @author 李峰高
 * @date Jun 12, 2008 8:30:08 PM
 * @version 1.0
 */
public class LogManagerImpl extends EventHandle implements LogManager {

	private static SQLUtil sqlUtilInsert = SQLUtil
			.getInstance("org/frameworkset/insert.xml");

	/* 序列化id */
	private static final long serialVersionUID = 1L;

	/* 日志主表的序列 */
	// public static final String LOG_SEQ_NAME = "SEQ_LOG" ;
	public static final String LOG_SEQ_NAME = "TD_SM_LOG";
	/* 日志明细表的序列 */
	// public static final String LOG_DETAIL_SEQ_NAME = "SEQ_LOG_DETAIL" ;
	public static final String LOG_DETAIL_SEQ_NAME = "TD_SM_LOGDETAIL";

	/*
	 * private static final String LOG_TABEL_NAME = "TD_SM_LOG"; private static
	 * final String LOG_DETAIL_TABEL_NAME = "TD_SM_LOGDETAIL"; private static
	 * final String LOG_HIS_TABEL_NAME = "TD_SM_LOG_HIS"; private static final
	 * String LOG_HIS_DETAIL_TABEL_NAME = "TD_SM_LOGDETAIL_HIS"; private static
	 * final String LOG_MODULE_TABEL_NAME = "TD_SM_LOGMODULE";
	 */

	/* 保存模块信息的全局变量 */
	static Map map = new EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap();

	private static Logger logger = Logger.getLogger(LogManagerImpl.class);

	/**
	 * 没有被使用的方法
	 */
	public List getLogList() throws ManagerException {// 取所有日志
		List list = new ArrayList();

		// try {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_GET);
		// p.setObject("from Log log order by log.operTime desc");
		//
		// list = (List) cb.execute(p);
		// } catch (ControlException e) {
		// throw new ManagerException(e.getMessage());
		// }

		return list;
	}

	/**
	 * 没有被使用的方法
	 */
	public List getLogList(String hql) throws ManagerException {
		// Parameter p = new Parameter();
		// p.setCommand(Parameter.COMMAND_GET);
		// p.setObject(hql);

		List list = new ArrayList();

		// try {
		// list = (List) cb.execute(p);
		// } catch (ControlException e) {
		// logger.error(e);
		// throw new ManagerException(e.getMessage());
		// }

		return list;
	}

	/**
	 * 根据日志id删除日志
	 */
	public boolean deleteLog(Log log) throws ManagerException {

		if (log != null) {
			return deleteLog(log.getLogId() + "");
		}
		return false;
	}

	/**
	 * <p>
	 * 对日志记录进行批量删除，新老日志表中都删除
	 * </p>
	 * 
	 * @param logId
	 * @return boolean
	 * @throws ManagerException
	 */
	public boolean deleteLog(String[] logId) throws ManagerException {
		boolean r = false;
		// 对参数的合法性进行判断
		if (logId != null && logId.length > 0) {

			String logIds = "-1";
			int i = 0, count = logId.length;
			// 对待删除的ID数组进行IN条件的组合
			for (i = 0; i < count; i++) {
				logIds += "," + logId[i];
			}
			String[] sqls = new String[4];
			sqls[0] = "delete from TD_SM_LOGDETAIL where log_id in (" + logIds
					+ ")";
			sqls[1] = "delete from TD_SM_LOG where log_id in (" + logIds + ")";
			sqls[2] = "delete from TD_SM_LOGDETAIL_HIS where log_id in ("
					+ logIds + ")";
			sqls[3] = "delete from TD_SM_LOG_HIS where log_id in (" + logIds
					+ ")";
			executeBatch(sqls);
			r = true;
		}
		return r;
	}

	/**
	 * <p>
	 * 对最新日志（在线日志）记录进行批量删除
	 * </p>
	 * 
	 * @param logId
	 * @return boolean
	 * @throws ManagerException
	 */
	public boolean deleteLatestLog(String[] logId) throws ManagerException {
		boolean r = false;
		// 对参数的合法性进行判断
		if (logId != null && logId.length > 0) {

			String logIds = "-1";
			int i = 0, count = logId.length;
			// 对待删除的ID数组进行IN条件的组合
			for (i = 0; i < count; i++) {
				logIds += "," + logId[i];
			}
			String[] sqls = new String[2];
			sqls[0] = "delete from TD_SM_LOGDETAIL where log_id in (" + logIds
					+ ")";
			sqls[1] = "delete from TD_SM_LOG where log_id in (" + logIds + ")";
			executeBatch(sqls);
			r = true;
		}
		return r;
	}

	/**
	 * <p>
	 * 对以前日志（历史日志）记录进行批量删除
	 * </p>
	 * 
	 * @param logId
	 * @return boolean
	 * @throws ManagerException
	 */
	public boolean deleteHistoricalLog(String[] logId) throws ManagerException {
		boolean r = false;
		// 对参数的合法性进行判断
		if (logId != null && logId.length > 0) {

			String logIds = "-1";
			int i = 0, count = logId.length;
			// 对待删除的ID数组进行IN条件的组合
			for (i = 0; i < count; i++) {
				logIds += "," + logId[i];
			}

			String[] sqls = new String[2];
			sqls[0] = "delete from TD_SM_LOGDETAIL_HIS where log_id in ("
					+ logIds + ")";
			sqls[1] = "delete from TD_SM_LOG_HIS where log_id in (" + logIds
					+ ")";
			executeBatch(sqls);
			r = true;
		}
		return r;
	}

	/**
	 * <p>
	 * 根据id删除日志，新老日志表中都删除
	 * </p>
	 * 
	 * @param logId
	 * @return boolean
	 * @throws ManagerException
	 */
	public boolean deleteLog(String logId) throws ManagerException {
		boolean r = false;
		if (logId != null) {
			String[] sqls = new String[4];
			sqls[0] = "delete from TD_SM_LOGDETAIL where log_id=" + logId;
			sqls[1] = "delete from TD_SM_LOG where log_id=" + logId;
			sqls[2] = "delete from TD_SM_LOGDETAIL_HIS where log_id=" + logId;
			sqls[3] = "delete from TD_SM_LOG_HIS where log_id=" + logId;
			executeBatch(sqls);
			r = true;
		}
		return r;
	}

	/**
	 * <p>
	 * 批量执行数据库方法
	 * </p>
	 * 
	 * @param sqls
	 * @return boolean
	 * @throws ManagerException
	 */
	public Object[] executeBatch(String[] sqls) throws ManagerException {
		if (sqls != null && sqls.length > 0) {
			DBUtil dbUtil = new DBUtil();
			// dbUtil.setAutoCommit(false);
			try {
				for (int loop = 0; loop < sqls.length; loop++) {
					dbUtil.addBatch(sqls[loop]);
				}
				return dbUtil.executeBatch();
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("executeBatch() -> \n", e);
			} finally {
				dbUtil.resetBatch();
				dbUtil = null;
			}
		}
		return null;
	}

	/**
	 * 删除所有操作日志，包括所有新老日志，且无权限控制
	 * 
	 * @return boolean
	 * @throws ManagerException
	 */
	public boolean deleteAllLog() throws ManagerException {
		String[] sqls = new String[4];
		sqls[0] = "delete from TD_SM_LOGDETAIL ";
		sqls[1] = "delete from TD_SM_LOG ";
		sqls[2] = "delete from TD_SM_LOGDETAIL_HIS ";
		sqls[3] = "delete from TD_SM_LOG_HIS ";
		executeBatch(sqls);
		return true;
	}

	/**
	 * add by gao.tang 2007.12.28 删除用户可管理机构的所有操作日志
	 * 
	 * @return
	 * @throws ManagerException
	 */
	public boolean deleteAllLog(String curUserId) throws ManagerException {
		boolean state = false;
		StringBuffer sql = new StringBuffer()
				.append(" delete from (select *")
				.append(
						" from TD_SM_LOG where log_operuser in(select user_name || ':' || user_realname")
				.append(
						" from td_sm_user where user_id in(select user_id from td_sm_userjoborg")
				.append(
						" where org_id in (select distinct org.org_id from td_sm_organization org")
				.append(
						" start with org.org_id in (select o.org_id from td_sm_organization o,")
				.append(
						" td_sm_orgmanager om where o.org_id = om.org_id and om.user_id = '")
				.append(curUserId).append("') ").append(
						" connect by prior org.org_id = org.parent_id)))) lg ");
		try {
			DBUtil dbUtil = new DBUtil();
			dbUtil.executeDelete(sql.toString());
			state = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return state;
	}

	public String transRealName2Counter(String name) {
		String counter = "";
		String sql = "select u.user_name from td_sm_user u where ";
		sql += " u.user_name='" + name + "' or u.user_realname='" + name + "' ";
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				counter = db.getString(0, "user_name");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return counter;

	}

	/**
	 * <p>
	 * 插入日志：有明细则插入明细，没有明细则不插入
	 * </p>
	 * <p>
	 * 可不带入（用户信息，机构信息，ip信息）记录日志，只要求带入业务参数（用户信息由程序自动获取）
	 * </p>
	 * 
	 * @param log
	 * @return long 主键值
	 * @throws ManagerException
	 */
	public String log(final Log log) throws ManagerException {
		// 检测是否合法

		if (log == null || log.getLogModule() == null
				|| log.getLogModule().trim().length() == 0) {
			throw new ManagerException("不是一个合法的日志！");
		}
		String logmodule = log.getLogModule().trim();

		// 检测是否有主键信息
		if (!enabledlog(logmodule)) {
			System.out.println("模块[" + logmodule + "]被配置为不可记录日志！");
			return null;
		}

		// 检测是否包含必要的用户信息
		if (log.getOperUser() == null || log.getOperUser().length() == 0) {
			// 在j2ee环境下取得用户信息
			// 老版系统中没有下面的方法，先注释掉

			AccessControl accessControl = AccessControl.getAccessControl();
			if (accessControl == null
					|| accessControl.getUserID().length() == 0) {
				throw new ManagerException("无法取得合法的用户信息！");
			}
			log.setOperUser(accessControl.getUserAccount());
			log.setOperOrg(accessControl.getChargeOrgId());
			log.setVisitorial(accessControl.getMachinedID());

		}
		// 检测是否包含机构信息，因为机构信息，将用做权限过滤
		else if (log.getOperOrg() == null || log.getOperOrg().length() == 0) {
			// 在j2ee环境下取得用户信息
			// 老版系统中没有下面的方法，先注释掉

			AccessControl accessControl = AccessControl.getAccessControl();
			if (accessControl == null
					|| accessControl.getUserID().length() == 0) {
				throw new ManagerException("无法取得合法的用户信息！");
			}
			log.setOperOrg(accessControl.getChargeOrgId());

		}

		// DBUtil dbUtil = new DBUtil();
		TransactionManager tm = new TransactionManager();
		String logId = "";
		String insertLogSql = "";
		try {
			// dbUtil.addBatch(insertLogSql);
			tm.begin();
			// 直接指定日志id
			logId = getSeqNextvar(LOG_SEQ_NAME);
			log.setLogId(Integer.valueOf(logId));// 不再使用tableinfo表的配置

			insertLogSql = getInsertSQLByLog(log);

			PreparedDBUtil preparedDBUtil = new PreparedDBUtil();
			StringBuffer sql = new StringBuffer();
			
			sql
					.append(
							" INSERT INTO TD_SM_LOG(LOG_ID,LOG_OPERUSER,OP_ORGID,OPER_MODULE,")
					.append(
							" LOG_VISITORIAL,LOG_OPERTIME,LOG_CONTENT,REMARK1,OPER_TYPE)")
					.append(" VALUES(?,?,?,?,?,?,?,?,?)");

			preparedDBUtil.preparedInsert(sql.toString());
			preparedDBUtil.setString(1, logId);
			preparedDBUtil.setString(2, log.getOperUser() == null ? "" : log
					.getOperUser());
			preparedDBUtil.setString(3, log.getOperOrg() == null ? "" : log
					.getOperOrg());
			preparedDBUtil.setString(4, logmodule == null ? "" : logmodule);
			preparedDBUtil.setString(5, log.getVisitorial() == null ? "" : log
					.getVisitorial());
			preparedDBUtil.setTimestamp(6, new Timestamp(new Date().getTime()));
			preparedDBUtil.setString(7, log.getOper() == null ? "" : log
					.getOper());
			preparedDBUtil.setString(8, log.getRemark1() == null ? "" : log
					.getRemark1());
			preparedDBUtil.setInt(9, log.getOperType());
			preparedDBUtil.addPreparedBatch();

			StringBuffer sql_detail = new StringBuffer();
			if (log.getDetailList() != null && log.getDetailList().size() > 0) {
				List detailList = log.getDetailList();
				for (int loop = 0; loop < detailList.size(); loop++) {
					LogDetail detail = (LogDetail) detailList.get(loop);
					detail.setLogID(Long.parseLong(logId));// 此时的明细中日志id应该是没有的
					detail.setDetailID(Long
							.parseLong(getSeqNextvar(LOG_DETAIL_SEQ_NAME)));// 取明细id

					sql_detail
							.append(
									" INSERT INTO TD_SM_LOGDETAIL(DETAIL_ID,OPER_TABLE,LOG_ID,OP_KEY_ID,DETAIL_CONTENT,OP_TYPE) ")
							.append(" VALUES(?,?,?,?,?,?)");

					preparedDBUtil.preparedInsert(sql_detail.toString());
					preparedDBUtil.setLong(1, detail.getDetailID());
					preparedDBUtil.setString(2,
							detail.getOperTable() == null ? "" : detail
									.getOperTable());
					preparedDBUtil.setLong(3, detail.getLogID());
					preparedDBUtil.setString(4,
							detail.getOperKeyID() == null ? "" : detail
									.getOperKeyID());
					preparedDBUtil.setString(5,
							detail.getDetailContent() == null ? "" : detail
									.getDetailContent());
					preparedDBUtil.setInt(6, detail.getOperType());

					preparedDBUtil.addPreparedBatch();
					sql_detail.setLength(0);
				}
			}
			preparedDBUtil.executePreparedBatch();
			tm.commit();
			return logId;
		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			logger.error("LogManagerImpl::log()->\n" + insertLogSql, e);
			throw new ManagerException(e.getMessage());
		} finally {
			// dbUtil.resetBatch();
			insertLogSql = null;
			// dbUtil = null ;
			tm = null;
		}
		// return "";
	}

	/**
	 * <p>
	 * 批量插入日志明细
	 * </p>
	 * 
	 * @param detailList
	 * @return String[]
	 */
	public Object[] logDetails(final List detailList) throws ManagerException {
		DBUtil dbUtil = new DBUtil();
		try {
			for (int loop = 0; loop < detailList.size(); loop++) {
				LogDetail detail = (LogDetail) detailList.get(loop);
				if (detail.getLogID() <= 0) {
					throw new ManagerException("日志明细必须有对应的日志id");
				}
				detail.setDetailID(Long
						.parseLong(getSeqNextvar(LOG_DETAIL_SEQ_NAME)));// 取明细id
				dbUtil.addBatch(getInsertSQLByLogDetail(detail));
			}
			Object[] ids = dbUtil.executeBatch();
			return ids;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ManagerException(e.getMessage());
		} finally {
			dbUtil.resetBatch();
			dbUtil = null;
		}
	}

	/**
	 * <p>
	 * 组装插入日志的sql
	 * </p>
	 * 
	 * @param log
	 * @return String
	 */
	private String getInsertSQLByLog(final Log log) {
		String logmodule = log.getLogModule();
		StringBuffer sql = new StringBuffer();
		sql
				.append(
						" INSERT INTO TD_SM_LOG(LOG_ID,LOG_OPERUSER,OP_ORGID,OPER_MODULE,")
				.append(
						" LOG_VISITORIAL,LOG_OPERTIME,LOG_CONTENT,REMARK1,OPER_TYPE)")
				.append(" VALUES(").append(log.getLogId()).append(" ,'")
				.append(log.getOperUser() == null ? "" : log.getOperUser())
				.append(" ','").append(
						log.getOperOrg() == null ? "" : log.getOperOrg())
				.append(" ','").append(logmodule == null ? "未知模块" : logmodule)
				.append(" ','").append(
						log.getVisitorial() == null ? "" : log.getVisitorial())
				.append(" ',SYSDATE").append(" ,'").append(
						log.getOper() == null ? "" : log.getOper()).append(
						" ','").append(
						log.getRemark1() == null ? "" : log.getRemark1())
				.append(" ',").append(log.getOperType()).append(")");
		return sql.toString();
	}

	/**
	 * <p>
	 * 组装插入日志的sql
	 * </p>
	 * 
	 * @return String
	 */
	private String getInsertSQLByLog(String operUser, String operOrg,
			String logModule, String visitorial, String oper, String remark1,
			int operType) {
		StringBuffer sql = new StringBuffer();
		sql
				.append(
						" INSERT INTO TD_SM_LOG(LOG_OPERUSER,OP_ORGID,OPER_MODULE,")
				.append(
						" LOG_VISITORIAL,LOG_OPERTIME,LOG_CONTENT,REMARK1,OPER_TYPE)")
				.append(" VALUES(").append(" '").append(
						operUser == null ? "" : operUser).append(" ','")
				.append(operOrg == null ? "" : operOrg).append(" ','").append(
						logModule == null ? "" : logModule).append(" ','")
				.append(visitorial == null ? "" : visitorial).append(
						" ',SYSDATE").append(" ,'").append(
						oper == null ? "" : oper).append(" ','").append(
						remark1 == null ? "" : remark1).append(" ',").append(
						operType).append(")");
		return sql.toString();
	}

	/**
	 * <p>
	 * 组装插入日志明细的sql
	 * </p>
	 * 
	 * @param detail
	 * @return String
	 */
	private String getInsertSQLByLogDetail(final LogDetail detail) {
		StringBuffer sql = new StringBuffer();
		sql
				.append(
						" INSERT INTO TD_SM_LOGDETAIL(DETAIL_ID,OPER_TABLE,LOG_ID,OP_KEY_ID,DETAIL_CONTENT,OP_TYPE)")
				.append(" VALUES(").append(detail.getDetailID()).append(" ,'")
				.append(
						detail.getOperTable() == null ? "" : detail
								.getOperTable()).append(" ',").append(
						detail.getLogID()).append(" ,'").append(
						detail.getOperKeyID() == null ? "" : detail
								.getOperKeyID()).append(" ',TO_CLOB('").append(
						detail.getDetailContent() == null ? "" : detail
								.getDetailContent()).append(" '),").append(
						detail.getOperType()).append(")");
		return sql.toString();
	}

	/**
	 * <p>
	 * 组装插入日志明细的sql
	 * </p>
	 * 
	 * @param detail
	 * @return String
	 */
	private String getInsertSQLByLogDetail(long logId, String operTable,
			String operKeyId, String detailContent, int operType) {
		StringBuffer sql = new StringBuffer();
		sql
				.append(
						" INSERT INTO TD_SM_LOGDETAIL(OPER_TABLE,LOG_ID,OP_KEY_ID,DETAIL_CONTENT,OP_TYPE)")
				.append(" VALUES(").append(" '").append(
						operTable == null ? "" : operTable).append(" ',")
				.append(logId).append(" ,'").append(
						operKeyId == null ? "" : operKeyId).append(" ','")
				.append(detailContent == null ? "" : detailContent).append(
						" ',").append(operType).append(")");
		return sql.toString();
	}

	/**
	 * <p>
	 * 根据seq名称取得下一个seq
	 * </p>
	 * 
	 * @param seqName
	 * @return String
	 */

	public String getSeqNextvar(final String seqName) {
		try {
			return DBUtil.getNextStringPrimaryKey(seqName);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		// StringBuffer sql = new StringBuffer(" SELECT ");
		// sql.append(seqName).append(".NEXTVAL").append(" FROM DUAL");
		// DBUtil dbUtil = new DBUtil();
		// try {
		// dbUtil.executeSelect(sql.toString());
		// //假定一定会有值
		// return dbUtil.getString(0, 0);
		// }catch(Exception e){
		// e.printStackTrace();
		// }finally{
		// sql = null ;
		// dbUtil = null ;
		// }
		// return null;
	}

	/**
	 * 记录登陆用户操作信息
	 * 
	 * @param operUser
	 *            操作人
	 * @param operContent
	 *            日志内容
	 * @param operModule
	 *            日志类型
	 * @param operSource
	 *            操作来源（模块，ip）
	 * @param Desc
	 *            日志描述
	 * @return Object 主键值
	 * @throws ManagerException
	 */
	public String log(String operUser, String operContent, String operModle,
			String operSource, String Desc) throws ManagerException {
		// 老版系统中没有下面的方法，先注释掉

		AccessControl accessControl = AccessControl.getAccessControl();
		if (accessControl == null || accessControl.getUserID().length() == 0) {
			throw new ManagerException("无法取得合法的用户信息！");
		}

		return log(operUser, accessControl.getChargeOrgId(), operModle,
				operSource, operContent, Desc, Log.INSERT_OPER_TYPE);
	}

	/**
	 * 记录登陆用户操作信息
	 * 
	 * @param operUser
	 *            操作人
	 * @param operContent
	 *            日志内容
	 * @param operModule
	 *            日志类型
	 * @param operSource
	 *            操作来源（模块，ip）
	 * @throws ManagerException
	 */
	public String log(String operUser, String operContent, String operModle,
			String operSource) throws ManagerException {

		AccessControl accessControl = AccessControl.getAccessControl();
		if (accessControl == null || accessControl.getUserID().length() == 0) {
			throw new ManagerException("无法取得合法的用户信息！");
		}

		return log(operUser, accessControl.getChargeOrgId(), operModle,
				operSource, operContent, "", Log.INSERT_OPER_TYPE);

	}

	/**
	 * 记录用户操作日志，只需传入日志内容与操作模块
	 * 
	 * @param operContent
	 *            日志内容
	 * @param operModule
	 *            日志模块
	 * @throws ManagerException
	 */
	public String log(String operContent, String operModle)
			throws ManagerException {

		AccessControl accessControl = AccessControl.getAccessControl();
		if (accessControl == null || accessControl.getUserID().length() == 0) {
			throw new ManagerException("无法取得合法的用户信息！");
		}

		return log(accessControl.getUserAccount(), accessControl
				.getChargeOrgId(), operModle, accessControl.getMachinedID(),
				operContent, "", Log.INSERT_OPER_TYPE);

	}

	/**
	 * <p>
	 * 记录一条不带明细的日志
	 * </p>
	 * <p>
	 * 可不带入用户信息记录日志，只要求带入业务参数
	 * </p>
	 * 
	 * @param operUser
	 * @param operOrg
	 * @param logModule
	 * @param visitorial
	 * @param oper
	 * @param remark1
	 * @param operType
	 * @return String 主键
	 * @throws ManagerException
	 */
	public String log(String operUser, String operOrg, String logModule,
			String visitorial, String operContent, String remark1, int operType)
			throws ManagerException {
		// 检测是否有主键信息
		if (logModule == null || logModule.trim().equals(""))
			throw new ManagerException("不是一个合法的日志！logModule属性不能为空。");
		logModule = logModule.trim();
		if (!enabledlog(logModule)) {
			System.out.println("模块[" + logModule + "]被配置为不可记录日志！");
			return null;
		}
		PreparedDBUtil preparedDBUtil = new PreparedDBUtil();

		// StringBuffer sql = new StringBuffer();
		// sql.append(" INSERT INTO TD_SM_LOG(LOG_ID,LOG_OPERUSER,OP_ORGID,OPER_MODULE,")
		// .append(" LOG_VISITORIAL,LOG_OPERTIME,LOG_CONTENT,REMARK1,OPER_TYPE)")
		// .append(" VALUES(?,?,?,?,?,SYSDATE,?,?,?)");

		String sql = sqlUtilInsert.getSQL("LogManagerImpl_log");
//		Map<String, String> variablevalues = new HashMap<String, String>();
//        variablevalues.put("date", DBUtil.getDBAdapter().to_date(new Date()));
//        String sql =  sqlUtilInsert.evaluateSQL("inserttdsmlog", sql_, variablevalues);
      

		try {
			String logId = getSeqNextvar(LOG_SEQ_NAME);
			preparedDBUtil.preparedInsert(sql);
			preparedDBUtil.setString(1, logId);
			preparedDBUtil.setString(2, operUser == null ? "" : operUser);
			preparedDBUtil.setString(3, operOrg == null ? "" : operOrg);
			preparedDBUtil.setString(4, logModule == null ? "" : logModule);
			preparedDBUtil.setString(5, visitorial == null ? "" : visitorial);
			preparedDBUtil.setTimestamp(6, new Timestamp(new Date().getTime()));
			preparedDBUtil.setString(7, operContent == null ? "" : operContent);
			preparedDBUtil.setString(8, remark1 == null ? "" : remark1);
			preparedDBUtil.setInt(9, operType);
			preparedDBUtil.executePrepared();
			return logId;
		} catch (Exception e1) {
			e1.printStackTrace();
			logger.error(e1);
			preparedDBUtil.resetPrepare();
		}
		// DBUtil dbUtil = new DBUtil();
		// try{
		// Object obj =
		// dbUtil.executeInsert(getInsertSQLByLog(operUser,operOrg,logModule,
		// visitorial,
		// oper ,remark1, operType));
		// return obj.toString();
		//			
		// }catch(Exception e){
		// e.printStackTrace();
		// logger.error(e);
		// }finally{
		// dbUtil = null ;
		// }
		return null;

	}

	/**
	 * <p>
	 * 记录一条只有一个明细的日志
	 * </p>
	 * <p>
	 * 可不带入用户信息记录日志，只要求带入业务参数
	 * </p>
	 * 
	 * @param logModule
	 * @param oper
	 * @param remark1
	 * @param operType
	 * @param operTable
	 * @param operKeyID
	 * @param detailContent
	 * @param detailOperType
	 * @return String 主键
	 * @throws ManagerException
	 */
	public String log(String logModule, String oper, String remark1,
			int operType, String operTable, String operKeyID,
			String detailContent, int detailOperType) throws ManagerException {
		Log log = new Log(logModule, oper, remark1, operType);
		LogDetail detail = new LogDetail(operTable, operKeyID, detailContent,
				detailOperType);
		List detailList = new ArrayList(1);
		detailList.add(detail);
		log.setDetailList(detailList);
		return log(log);
	}

	/**
	 * <p>
	 * 不带入用户信息记录日志，只要求带入业务参数
	 * </p>
	 * 
	 * @param logModule
	 * @param oper
	 * @param remark1
	 * @param operType
	 * @return String 主键
	 * @throws ManagerException
	 */
	public String log(String logModule, String oper, String remark1,
			int operType) throws ManagerException {

		// 在j2ee环境下取得用户信息

		AccessControl accessControl = AccessControl.getAccessControl();
		if (accessControl == null || accessControl.getUserID().length() == 0) {
			throw new ManagerException("无法取得合法的用户信息！");
		}

		// 新版系统中才有取mac地址的方法 accessControl.getMacAddr() ，所以暂时用 000000000000
		return log(accessControl.getUserAccount(), accessControl
				.getChargeOrgId(), logModule, accessControl.getMachinedID(),
				oper, remark1, Log.INSERT_OPER_TYPE);
	}

	/**
	 * <p>
	 * 保存单个明细
	 * </p>
	 * 
	 * @param detail
	 * @return
	 * @throws ManagerException
	 */
	public String logDetail(final LogDetail detail) throws ManagerException {
		List detailList = new ArrayList(1);
		detailList.add(detail);
		return logDetail(detail.getLogID(), detail.getOperTable(), detail
				.getOperKeyID(), detail.getDetailContent(), detail
				.getOperType());
	}

	/**
	 * <p>
	 * 保存单个明细
	 * </p>
	 * 
	 * @param detail
	 * @return Object 主键
	 * @throws ManagerException
	 */
	public String logDetail(long logID, String operTable, String operKeyID,
			String detailContent, int operType) throws ManagerException {
		// LogDetail detail = new LogDetail(logID, operTable, operKeyID,
		// detailContent, operType);
		DBUtil dbUtil = new DBUtil();
		try {
			return dbUtil.executeInsert(
					getInsertSQLByLogDetail(logID, operTable, operKeyID,
							detailContent, operType)).toString();

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			dbUtil = null;
		}
		return null;
	}

	/**
	 * 判断是否允许记日志
	 * 判断module是否存在于logmodule表中，如果存在继续下一步工作，如果不存在就往logmodule表中插入module记录
	 * 
	 * @param logmodule
	 * @return
	 */
	public boolean enabledlog(String logmodule) {
		if (logmodule == null || logmodule.trim().length() == 0)
			return false;
		String temp = logmodule.trim();
		Integer o_status = (Integer) map.get(temp);
		if (o_status == null) {
			String sql = "select * from td_sm_logmodule where logmodule=?";
			PreparedDBUtil dbUtil = new PreparedDBUtil();
			try {
				dbUtil.preparedSelect(sql);
				dbUtil.setString(1, temp);
				dbUtil.executePrepared();
				if (dbUtil.size() > 0) {
					int status = dbUtil.getInt(0, "status");
					map.put(temp, new Integer(status));
					return status == 0;
					// return true;
				} else {
					String id = dbUtil
							.getNextStringPrimaryKey("td_sm_logmodule");
					sql = "insert into td_sm_logmodule(logmodule,status,id) values('"
							+ temp + "',0,'" + id + "')";
					System.out.println(sql);
					dbUtil.executeInsert(sql);
					map.put(temp, new Integer(0));
					return true;
				}

			} catch (SQLException e) {
				return false;
			}
		} else {
			int status = o_status.intValue();
			return status == 0;
		}
	}

	//
	// /**
	// * 记录系统登录、退出时得日志
	// */
	//
	// public void log(String operUser, String operContent, String operType,
	// String operSource, String Desc) throws ManagerException {
	//			
	// log(operUser,operContent,operType,operSource,operSource,Desc);
	// }

	public boolean updatelog(String status, String logid)
			throws ManagerException {
		boolean b = false;
		DBUtil db = new DBUtil();
		String sql;
		// 0是记录日志
		if (status.equals("0")) {
			sql = "update TD_SM_LOGMODULE set status=1 where id=" + logid + "";
		} else {
			sql = "update TD_SM_LOGMODULE set status=0 where id=" + logid + "";
		}
		try {
			db.executeUpdate(sql);
			sql = "select logmodule from TD_SM_LOGMODULE where id=" + logid
					+ "";
			db.executeSelect(sql);
			if ("0".equals(status)) {
				map.put(db.getString(0, "logmodule"), new Integer(1));
			} else {
				map.put(db.getString(0, "logmodule"), new Integer(0));
			}
			b = true;
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return b;

	}

	public List querylog(Log log) throws ManagerException {
		List list = new java.util.ArrayList();
		// DBUtil db = new DBUtil();
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		if (log.getOper() != null) {

		}

		return list;
	}

	public boolean isNotNull(String str) {
		boolean flag = true;
		if (str == null || str.trim().length() == 0
				|| "null".equalsIgnoreCase(str)) {
			flag = false;
		}
		return flag;
	}

	public boolean backupLog(String day) {
		boolean state = false;

		// DBUtil db = new DBUtil();
		// String sql = "call log_backup("+day+")";
		// try {
		// db.executeUpdate(sql);
		// } catch (SQLException e) {
		// e.printStackTrace();
		// }
		TransactionManager tr = new TransactionManager();
		PreparedDBUtil pe = new PreparedDBUtil();
		PreparedDBUtil pe_ = new PreparedDBUtil();
		try {
			tr.begin();
			int day_ = Integer.parseInt(day);
			DB dbAdaptor = DBUtil.getDBAdapter();

			String log_opertime = dbAdaptor.to_char("log_opertime",
					dbAdaptor.getFORMART_YEAR_MM_DD());
			String start_time = dbAdaptor.to_char(dbAdaptor.to_date(new Date()) + " - ?",
					dbAdaptor.getFORMART_YEAR_MM_DD());
			Map<String, String> variablevalues = new HashMap<String, String>();
			variablevalues.put("log_opertime", log_opertime);
			variablevalues.put("starttime", start_time);
			List<Pro> addList = sqlUtilInsert
					.getListSQLs("logManagerImpl_backupLog_insert");
			int idx = 0;
			for (Pro pro : addList) {
				String sql = pro.toString();
				sql = sqlUtilInsert.evaluateSQL(
						"logManagerImpl_backupLog_insert " + idx, sql,
						variablevalues);
				idx++;
				pe.preparedInsert(sql);
				pe.setInt(1, day_);
				pe.executePrepared();
			}
			idx = 0;
			List<Pro> deleteList = sqlUtilInsert
					.getListSQLs("logManagerImpl_backupLog_delete");
			for (Pro pro : deleteList) {
				String sql_ = pro.toString();
				sql_ = sqlUtilInsert.evaluateSQL(
						"logManagerImpl_backupLog_delete " + idx, sql_,
						variablevalues);
				idx++;
				pe.preparedDelete(sql_);
				pe.setInt(1, day_);
				pe.executePrepared();
			}

			tr.commit();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				tr.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (TransactionException e) {
			try {
				tr.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (Exception e) {
			try {
				tr.rollback();
			} catch (RollbackException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

		return state;
	}

	public static void main(String[] args) {
		LogManagerImpl logImpl = new LogManagerImpl();
		Log log = new Log();
		try {
			log.setLogModule("认证管理");
			log.setOper("登陆系统");
			log.setOperOrg("1");
			log.setOperUser("admin");
			List list = new ArrayList();
			LogDetail ld = null;
			for (int i = 0; i < 10; i++) {
				ld = new LogDetail();
				ld.setOperTable("td_sm_user" + i);
				ld.setOperType(2);
				ld.setDetailContent("jigou" + i);
				list.add(ld);
			}
			log.setDetailList(list);
			logImpl.log(log);
		} catch (ManagerException e) {
			e.printStackTrace();
		}

	}

}
