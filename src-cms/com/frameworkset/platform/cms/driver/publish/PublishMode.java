package com.frameworkset.platform.cms.driver.publish;

/**
 * 
 * <p>Title: com.frameworkset.platform.cms.driver.publish.PublishMode.java</p>
 *
 * <p>Description: 定义系统的发布状态:动态和静态发布两种模式</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: iSany</p>
 * @Date 2007-1-10
 * @author biaoping.yin
 * @version 1.0
 */
public class PublishMode implements java.io.Serializable {
	/**
	 * 4种发布模式
	 *  静态未受保护模式
	 *  静态受保护模式
	 *  动态未受保护模式
	 *  动态受保护模式
	 * 
	 * 
	 */
	private String _mode ;
	/**
	 * 确定发布内容是否受页面保护控制        
	 */
	private boolean _protect = false;
	
	public static final PublishMode MODE_STATIC_PROTECTED = new PublishMode("static",true);
	public static final PublishMode MODE_DYNAMIC_PROTECTED = new PublishMode("dynamic",true);
	public static final PublishMode MODE_STATIC_UNPROTECTED = new PublishMode("static",false);
	public static final PublishMode MODE_DYNAMIC_UNPROTECTED = new PublishMode("dynamic",false);
	/**
	 * 不执行发布动作
	 */
	public static final PublishMode MODE_NO_ACTION = new PublishMode("noaction",false);
	
	public static final PublishMode MODE_ONLY_ATTACHMENT = new PublishMode("attachment",false);
	
	
	
	
	public PublishMode(String mode,boolean protect)
	{
		_mode = mode;
		_protect = protect;
	}

	public String getMode() {
		return _mode;
	}

	
	
	public boolean equals(Object other)
	{
		if(other instanceof PublishMode)
		{
			return this._mode.equals(((PublishMode)other).getMode()) 
						&& (this._protect == ((PublishMode)other).isProtected());
		}
		else
			return false;
	}
	
	public int hashCode()
	{
		return this._mode.hashCode() + new Boolean(this._protect).hashCode();
	}

	public boolean isProtected() {
		return _protect;
	}

	public void setProtected(boolean _protect) {
		this._protect = _protect;
	}
	
	

}
