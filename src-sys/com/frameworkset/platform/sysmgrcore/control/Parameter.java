package com.frameworkset.platform.sysmgrcore.control;

import java.io.Serializable;
import java.util.Properties;

/**
 * 项目：SysMgrCore <br>
 * 描述：参数管理器 <br>
 * 版本：1.0 <br>
 * 
 * @author 
 */
public class Parameter implements Serializable {

	/**
	 * 存储命令
	 */
	public static final int COMMAND_STORE = 1;

	/**
	 * 取数据实例命令
	 */
	public static final int COMMAND_GET = 2;

	/**
	 * 删除数据命令
	 */
	public static final int COMMAND_DELETE = 3;

	/**
	 * 需要控制器执行的命令
	 */
	private int command = 0;

	/**
	 * 需要控制器处理的数据实例的对象
	 */
	private Object object = null;

	/**
	 * 扩展对象用于控制功能用多个对象的处理
	 */
	private Properties properties = new Properties();

	/**
	 * 返回需要执行的命令
	 * 
	 * @return 返回 command。
	 */
	public int getCommand() {
		return command;
	}

	/**
	 * 设置需要控制器执行的命令
	 * 
	 * @param command
	 *            需要执行的命令，可以直接通过 Parameter.COMMAND_XXX 来指定
	 */
	public void setCommand(int command) {
		this.command = command;
	}

	/**
	 * 返回 Parameter 中所包含的对象
	 * 
	 * @return 返回 object。
	 */
	public Object getObject() {
		return object;
	}

	/**
	 * 设置 Parameter 中所包含的对象
	 * 
	 * @param object
	 *            要设置的 object。
	 */
	public void setObject(Object object) {
		this.object = object;
	}

	/**
	 * Parameter 中的扩展属性
	 * 
	 * @return 返回 props。
	 */
	public Properties getProperties() {
		return properties;
	}
}