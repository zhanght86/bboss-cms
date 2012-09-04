package com.frameworkset.platform.sysmgrcore.control;

import java.io.Serializable;

import com.frameworkset.platform.sysmgrcore.exception.ControlException;

/**
 * 项目：SysMgrCore <br>
 * 描述：控制器基础类 <br>
 * 版本：1.0 <br>
 * 
 * @author 吴卫雄
 */
public abstract class DataControl implements Serializable{

	/**
	 * 数据库控制器实例
	 */
	public static final int CONTROL_INSTANCE_DB = 1;

	/**
	 * LDAP控制器实例
	 */
	public static final int CONTROL_INSTANCE_LDAP = 2;

	/**
	 * 是否自动提交
	 */
	private boolean isAutoCommit = true;

	/**
	 * 根据参数返回控制器实例类型
	 * 
	 * @param controlInstance
	 *            控制器实例类型，可以直接通过 DataControl.CONTROL_INSTANCE_XXX 来指定，如：<br>
	 *            DataControl.CONTROL_INSTANCE_DB
	 * @return DataControl 对象
	 */
	public static DataControl getInstance(int controlInstance) {

		DataControl dataControl = null;

		if (controlInstance == CONTROL_INSTANCE_DB)
			dataControl = new DbControl();

		if (controlInstance == CONTROL_INSTANCE_LDAP)
			dataControl = new LdapControl();

		return dataControl;
	}

	/**
	 * 执行由子类实例的控制功能
	 * 
	 * @param object
	 *            由调用程序传入参数或者命令，具体规则由子类来定义
	 * @return 执行相关操作后的结果
	 */
	public abstract Object execute(Parameter parameter) throws ControlException;

	/**
	 * 由子类实现的停止控制器执行方法，在方法中可以提供释放相关资源的操作
	 */
	public abstract void exit() throws ControlException;

	/**
	 * 提交事务
	 * 
	 * @param isExit
	 *            提交后是否退出数据控制
	 */
	public abstract void commit(boolean isExit) throws ControlException;

	/**
	 * 回滚事务
	 * 
	 * @param isExit
	 *            回滚后是否退出数据控制
	 */
	public abstract void rollback(boolean isExit) throws ControlException;

	/**
	 * 返回当前数据控制器的页面配置管理
	 * 
	 * @return 分页配置管理对象
	 */
	public abstract PageConfig getPageConfig() throws ControlException;

	/**
	 * 是否完成相关操作后自动进行事务提交
	 * 
	 * @return 完成操作后是否自动提交
	 */
	public boolean isAutoCommit() {
		return isAutoCommit;
	}

	/**
	 * 设置是事进行自动事务提交
	 * 
	 * @param 完成操作后是否自动提交
	 */
	public void setAutoCommit(boolean isAutoCommit) {
		this.isAutoCommit = isAutoCommit;
	}

}