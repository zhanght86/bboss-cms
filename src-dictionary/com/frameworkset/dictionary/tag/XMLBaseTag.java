/*
 * Title: The ERP System of kelamayi Downhole Company [PMIP]
 *
 * Copyright: Copyright (c) 2000-2004 westerasoft Co., Ltd All right reserved.
 *
 * Company: westerasoft Co., Ltd
 *
 * All right reserved.
 *
 * Created on 2004-8-2
 *
 * JDK version used		:1.4.1
 *
 * Modification history:
 *
 *
 */
package com.frameworkset.dictionary.tag;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.common.tag.exception.FormulaException;
import com.frameworkset.common.tag.pager.tags.CellTag;
import com.frameworkset.dictionary.Data;
import com.frameworkset.dictionary.DataManagerFactory;
import com.frameworkset.dictionary.ProfessionDataManagerException;




/**
 * @author biaoping.yin
 * 2004-8-2
 */
public class XMLBaseTag extends CellTag implements Serializable
{
    private static Logger log = Logger.getLogger(XMLBaseTag.class);
    
    protected AccessControl accesscontroler = null;
    protected String defaultName = "其它";
    
    
    /**
     * 字典类别
     */
	protected String type;
	
	private boolean disabled = false;
	private String extend = "";
	/**
	 * 字典项名称
	 */
	protected String name;
	protected String style;
	protected Data data;
	  
	 

    protected String t_value ;
    
    /**
     * 做权限过滤,缺省不过滤
     */
    protected boolean checkPermission = false;
    
    /**
     * 操作类型: "可见"和"常用",缺省是"可见"
     */
    protected String opcode = "read";
    
    public void setPageContext(PageContext pageContext)
    {
    	super.setPageContext(pageContext);
    	accesscontroler = AccessControl.getInstance();
    	accesscontroler.checkAccess(request,response,false);
    }



	public int doStartTag()
		throws JspException
	{
        super.init();
        Object defaultvalue  = this.getDefaultValue();
        t_value = defaultvalue == null ?null:defaultvalue.toString();
		try
		{
			//没有权限过滤
			//data = DataManagerFactory.getDataManager().getData(this.getType());
	
			//data里面的数据项没有权限过滤
			data = DataManagerFactory.getDataManager().getData(this.getType());
			
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
//			e.printStackTrace();
			
//			throw new JspException(e.getMessage());
		}
		try
		{
			out.print(generateContent());
		}
		catch (IOException e1)
		{
//			throw new JspException(e1.getMessage());
		}

		return SKIP_BODY;

	}


	protected String getItemValue()
	{
		try
		{
			return DataManagerFactory.getDataManager().getItemValue(this.getType(),getName());
		}
		catch (ProfessionDataManagerException e)
		{
			e.printStackTrace();
		}
		return "";
	}




	/* (non-Javadoc)
	 * @see com.westerasoft.common.tag.BaseTag#generateContent()
	 */
	public String generateContent()
	{
		return "";
	}

	/* (non-Javadoc)
	 * @see com.westerasoft.common.tag.BaseTag#write(java.io.OutputStream)
	 */
	public void write(OutputStream output)
	{
	}

	/**
	 * 获取缺省值
	 * @return
	 */
	public Object getDefaultValue()
	{

        if(t_formula != null)
        {
            try {
                Object temp = t_formula.getValue();
                if(temp != null)
                    defaultValue = temp;
            } catch (FormulaException ex) {
                //ex.printStackTrace();
                log.error(ex);
                //return null;
            }

        }

		if(defaultValue == null && getName() != null)
		{		    
			String temp = (String)request.getAttribute(getName());
			if(temp != null)
				defaultValue = temp;
			else
			    defaultValue = request.getParameter(getName());
		}
        return defaultValue;

	}


	/**
	 * @return
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @return
	 */
	public String getStyle()
	{
		return style;
	}

	/**
	 * @param string
	 */
	public void setDefaultValue(Object string)
	{
		defaultValue = string;
	}



	/**
	 * @param string
	 */
	public void setName(String string)
	{
		name = string;
	}

	/**
	 * @param string
	 */
	public void setStyle(String string)
	{
		style = string;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public boolean isDisabled() {
		return disabled;
	}


	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
	
	public int doEndTag() throws JspException
	{
		int ret = super.doEndTag();
		this.data = null;
		this.defaultValue = "";
		this.t_value = null;
		this.name = null;
		this.style = null;
		this.checkPermission = false;
		defaultName = "其它";
		return ret;
	}


	public String getExtend() {
		return extend;
	}


	public void setExtend(String extend) {
		this.extend = extend;
	}


	public boolean isCheckPermission() {
		return checkPermission;
	}


	public void setCheckPermission(boolean checkPermission) {
		this.checkPermission = checkPermission;
	}



	public String getOpcode() {
		return opcode;
	}



	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}
	/**
	 * @return the defaultName
	 */
	public String getDefaultName() {
		return defaultName;
	}


	/**
	 * @param defaultName the defaultName to set
	 */
	public void setDefaultName(String defaultName) {
		this.defaultName = defaultName;
	}

}
