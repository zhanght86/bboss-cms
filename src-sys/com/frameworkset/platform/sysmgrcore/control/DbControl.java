package com.frameworkset.platform.sysmgrcore.control;

import java.util.List;

//import net.sf.hibernate.HibernateException;
//import net.sf.hibernate.Query;
//import net.sf.hibernate.Session;
//import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;

import com.frameworkset.platform.sysmgrcore.exception.ControlException;
import com.frameworkset.platform.sysmgrcore.unit.HibernateSessionFactory;

/**
 * 项目：SysMgrCore <br>
 * 描述：数据库控制器 <br>
 * 版本：1.0 <br>
 * 
 * @author 吴卫雄
 */
class DbControl extends DataControl {

	private static Logger logger = Logger.getLogger(DbControl.class.getName());

	private PageConfig pageConfig = null;

	public DbControl() {
		pageConfig = new PageConfig();
	}

	public Object execute(Parameter parameter) throws ControlException {
//		Transaction trans = null;

//		try {
////			Session session = HibernateSessionFactory.currentSession();
//			trans = session.beginTransaction();
//
//			// ********************************************************
//			// 新增或更新数据实例
//
//			if (parameter.getCommand() == Parameter.COMMAND_STORE) {
//				if (parameter.getObject() == null)
//					throw new ControlException(
//							"parameter 中所包含的 Object 不能为 null.");
//
//				session.saveOrUpdate(parameter.getObject());
//				// session.flush();
//
//				if (isAutoCommit()) {
//					if (trans != null)
//						trans.commit();
//				}
//
//				return new Boolean(true);
//			}
//
//			// ********************************************************
//			// 取指定的数据实例
//
//			if (parameter.getCommand() == Parameter.COMMAND_GET) {
//				if (parameter.getObject() == null)
//					throw new ControlException("缺少做为取值依据的参数");
//
//				if (pageConfig.getStartIndex() == -1)
//					// 根据HSQL语句来取数据实例
//					return session.find(parameter.getObject().toString());
//				else {
//					Query q = session.createQuery("select count(*) "
//							+ parameter.getObject().toString());
//
//					List list = q.list();
//					if (list.size() == 1 && list.get(0) instanceof Integer)
//						pageConfig.setTotalSize(((Integer) q.list().get(0))
//								.intValue());
//					else
//						pageConfig.setTotalSize(list.size());
//
//					q = session.createQuery(parameter.getObject().toString());
//					q.setFirstResult(pageConfig.getStartIndex());
//					q.setMaxResults(pageConfig.getPageSize());
//
//					// 将起始值重设为-1以保证下一次可以取完整列表，除非是客户程序明确指出
//					// 取的是分页后的数据
//					pageConfig.setStartIndex(-1);
//					return q.list();
//				}
//			}
//
//			// ********************************************************
//			// 删除指定的数据实例
//
//			if (parameter.getCommand() == Parameter.COMMAND_DELETE) {
//				if (parameter.getObject() == null)
//					throw new ControlException("parameter 中的 object 不能为 null. ");
//
//				if (parameter.getObject() instanceof String) {
//					// 根据HSQL条件删除机构实体
//					session.delete(parameter.getObject().toString());
//					// session.flush();
//				} else {
//					// 删除指定的实体
//					session.delete(parameter.getObject());
//					// session.flush();
//				}
//
//				if (isAutoCommit()) {
//					if (trans != null)
//						trans.commit();
//				}
//
//				return new Boolean(true);
//			}
//
//			return null;
//		} catch (HibernateException e) {
//			e.printStackTrace();
//			logger.error(e);
//
//			if (isAutoCommit()) {
//				if (trans != null) {
//					try {
//						trans.rollback();
//					} catch (HibernateException e1) {
//						logger.error(e);
//					}
//				}
//			}
//
//			throw new ControlException(e.getMessage());
//		} finally {
//			if (isAutoCommit())
//				exit();
//		}
		return null;
	}

	public void exit() throws ControlException {
//		HibernateSessionFactory.closeSession();
//		setAutoCommit(true);
	}

	public void commit(boolean isExit) throws ControlException {
//		try {
//			HibernateSessionFactory.currentSession().beginTransaction()
//					.commit();
//		} catch (HibernateException e) {
//			logger.error(e);
//			throw new ControlException(e.getMessage());
//		} finally {
//			if (isExit)
//				exit();
//		}
	}

	public void rollback(boolean isExit) throws ControlException {
//		try {
//			HibernateSessionFactory.currentSession().beginTransaction()
//					.rollback();
//		} catch (HibernateException e) {
//			logger.error(e);
//			throw new ControlException(e.getMessage());
//		} finally {
//			if (isExit)
//				exit();
//		}
	}

	public PageConfig getPageConfig() throws ControlException {
		return pageConfig;
	}
}