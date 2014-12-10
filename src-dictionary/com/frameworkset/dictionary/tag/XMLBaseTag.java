
package com.frameworkset.dictionary.tag;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.frameworkset.common.tag.exception.FormulaException;
import com.frameworkset.common.tag.pager.tags.CellTag;
import com.frameworkset.dictionary.Data;
import com.frameworkset.dictionary.DataManagerFactory;
import com.frameworkset.dictionary.ProfessionDataManagerException;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.util.StringUtil;




/**
 * @author biaoping.yin
 * 2004-8-2
 */
public class XMLBaseTag extends CellTag 
{
    private static Logger log = Logger.getLogger(XMLBaseTag.class);
    
    protected AccessControl accesscontroler = null;
    private boolean defaultCell;
    protected String defaultName = "";
    protected String splittoken = "#$";
    
    
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
	  
	 

    protected Object t_value ;
    
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
    	accesscontroler = AccessControl.getAccessControl();
    }
    
    /**
	 * 判断特定的选项是否是选中的
	 * @param value
	 * @return
	 */
	protected List<String> selected()
	{
		List<String> t_values = null;
		if(t_value != null)
		{			
			t_values = new ArrayList<String>();
			if(t_value.getClass().isArray())
			{
				int size = Array.getLength(t_value);
				for(int i = 0; i < size; i ++)
				{
					t_values.add(String.valueOf(Array.get(t_value, i)));
				}
			}
			else if(t_value instanceof Collection)
			{
				Collection c_values = (Collection)t_value;
			
				Iterator it = c_values.iterator();
				while(it.hasNext())
				{
					t_values.add(String.valueOf(it.next()));
				}
			}
			else if(t_value instanceof Iterator)
			{
				Iterator it = (Iterator)t_value;
				while(it.hasNext())
				{
					t_values.add(String.valueOf(it.next()));
				}
			}
			else if(t_value instanceof String)
			{
				t_values = this.parserDefaultValues((String)t_value);
			}
			else
			{
				t_values.add(String.valueOf(t_value));
			}
		}
		return t_values;
	}
	protected boolean selected(List<String> selected,String value)
	{
		if(selected == null || selected.size() == 0)
			return false;
		for(String select:selected)
		{
			if(select.equals(value))
				return true;
		}
		return false;
	}
	protected List<String> parserDefaultValues(String defaultValues)
	{
		List<String> ret = new ArrayList<String>();
	    if(defaultValues == null|| defaultValues.equals("") )
	        return ret;
	    else
	    {
	    	String[] vs = StringUtil.split(defaultValues,splittoken);
	        for(String v:vs)
        	{
	        	ret.add(v);
        	}
	        return ret;
	    }
	}
	public String getSplittoken() {
		return splittoken;
	}

	public void setSplittoken(String splittoken) {
		this.splittoken = splittoken;
	}

	
	public int doStartTag()
		throws JspException
	{
        super.init();
        Object defaultvalue  = this._getDefaultValue();
        t_value = defaultvalue == null ?null:defaultvalue;
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
	public Object _getDefaultValue()
	{

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
        else
        {
        	Object v = _getObjectValue(defaultCell);
        	if(v != null)
        		defaultValue = v;
        		
        }
        
//        if(this.actual !=null)
//        {
//        	defaultValue = actual;
//        }

//		if(defaultValue == null)
//		{
//			defaultValue = getObjectValue();
//		}
		if(defaultValue == null && getName() != null)
		{		    
			Object temp = request.getAttribute(getName());
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
		this.defaultValue = null;
		this.t_value = null;
		this.name = null;
		this.style = null;
		this.checkPermission = false;
		defaultName = "";
		splittoken = "#$";
		defaultCell = false;
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

	public boolean isDefaultCell() {
		return defaultCell;
	}

	public void setDefaultCell(boolean defaultCell) {
		this.defaultCell = defaultCell;
	}

}
