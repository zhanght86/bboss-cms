package com.frameworkset.platform.sysmgrcore.web.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import com.frameworkset.common.tag.exception.FormulaException;
import com.frameworkset.common.tag.pager.tags.CellTag;
import com.frameworkset.platform.sysmgrcore.manager.db.UserCacheManager;
import common.Logger;

public class UserTag extends CellTag {
	private String user;
	private boolean isAccount = true;
	private String attribute = "userName";
	private Object defaultValue;
	private static final Logger log = Logger.getLogger(UserTag.class);
	public int doStartTag()
			throws JspException
	{
        super.init();
        return SKIP_BODY;
	}
	@Override
	public void doFinally() {
		
		super.doFinally();
		user = null;
		isAccount = true;
		attribute = "userName";
		defaultValue = null;
	}
	@Override
	public int doEndTag() throws JspException {
		Object value = null;
		if(user != null && !user.equals(""))
		{
			if(isAccount)
				value = UserCacheManager.getInstance().getUserAttribute(user,attribute);
			else 
			{
				value = UserCacheManager.getInstance().getUserAttributeByID(user,attribute);
			}
		}
		else
		{
			Object user_ = this._getDefaultValue();
			if(user_ != null)
			{
				
				if(isAccount)
					value = UserCacheManager.getInstance().getUserAttribute(String.valueOf(user_),attribute);
				else 
				{
					value = UserCacheManager.getInstance().getUserAttributeByID(String.valueOf(user_),attribute);
				}
			}
		}
		try {
			if(value != null )
				out.print(String.valueOf(value));
			else if(defaultValue != null)
				out.print(defaultValue);
		} catch (IOException e) {
			throw new JspException(e);
		}
		return super.doEndTag();
	}
	
	/**
	 * 获取缺省值
	 * @return
	 */
	public Object _getDefaultValue()
	{
		Object defaultValue = null;
        if(t_formula != null)
        {
            try {
                Object temp = t_formula.getValue();
                if(temp != null)
                    defaultValue = temp;
            } catch (FormulaException ex) {
                //ex.printStackTrace();
                log.info(ex.getMessage());
                //return null;
            }

        }
        else if(this.actual !=null)
        {
        	defaultValue = actual;
        }

		if(defaultValue == null)
		{
			defaultValue = getObjectValue();
		}
		
        return defaultValue;

	}
	
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public Object getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(Object defaultValue) {
		this.defaultValue = defaultValue;
	}
	public boolean isAccount() {
		return isAccount;
	}
	public void setAccount(boolean isAccount) {
		this.isAccount = isAccount;
	}
	
	
}
