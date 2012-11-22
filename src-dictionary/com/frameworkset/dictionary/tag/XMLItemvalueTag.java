
package com.frameworkset.dictionary.tag;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

import com.frameworkset.dictionary.ProfessionDataManagerException;
import com.frameworkset.util.StringUtil;

import org.apache.log4j.Logger;

/**
 * @author biaoping.yin
 * 2004-8-5
 */
public class XMLItemvalueTag extends XMLBaseTag
{
    private static Logger log = Logger.getLogger(XMLItemvalueTag.class);
	String itemName;
	/* (non-Javadoc)
	 * @see com.westerasoft.common.tag.BaseTag#generateContent()
	 */

	protected String getItemValues(Object t_value) throws ArrayIndexOutOfBoundsException, IllegalArgumentException, ProfessionDataManagerException
	{
		StringBuffer buffer = new StringBuffer();
		if(t_value.getClass().isArray())
		{
			int size = Array.getLength(t_value);
			for(int i = 0; i < size; i ++)
			{
				if(i == 0)
					buffer.append(data.getItemValue(String.valueOf(Array.get(t_value, i))));
				else
					buffer.append(",").append(data.getItemValue(String.valueOf(Array.get(t_value, i))));
			}
		}
		else if(t_value instanceof Collection)
		{
			Collection c_values = (Collection)t_value;
		
			Iterator it = c_values.iterator();
			int i = 0;
			while(it.hasNext())
			{
				if(i == 0)
					buffer.append(data.getItemValue(String.valueOf(it.next())));
				else
					buffer.append(",").append(data.getItemValue(String.valueOf(it.next())));
//				t_values.add(String.valueOf(it.next()));
				i ++;
			}
		}
		else if(t_value instanceof Iterator)
		{
			Iterator it = (Iterator)t_value;
			int i = 0;
			while(it.hasNext())
			{
				if(i == 0)
					buffer.append(data.getItemValue(String.valueOf(it.next())));
				else
					buffer.append(",").append(data.getItemValue(String.valueOf(it.next())));
				i ++;
//				t_values.add(String.valueOf(it.next()));
			}
		}
		else if(t_value instanceof String)
		{
			String defaultValues = (String)t_value;
			 if(defaultValues == null|| defaultValues.equals("") )
			        return null;
		    else
		    {
		    	String[] vs = StringUtil.split(defaultValues,"#$");
		    	int i = 0;
		        for(String v:vs)
	        	{
		        	if(i == 0)
						buffer.append(data.getItemValue(v));
					else
						buffer.append(",").append(data.getItemValue(v));
					i ++;
	        	}
		        
		    }
		}
		else
		{
			return data.getItemValue((String.valueOf(t_value)));
		}
		return buffer.toString();
	}

	public String generateContent()
	{
		if(data != null)
		{
			try
			{
	            if(getItemName() != null)
				    return getItemValues(this.getItemName());
	            else if(t_value != null)
	            {
//	                return data.getItemValue(t_value);
	            	return getItemValues(t_value);
	            }
	
			}
			catch (ProfessionDataManagerException e)
			{
				e.printStackTrace();
			}
			return "";
		}
		else
		{
			return "字典[" + this.type + "]不存在";
		}
	}
	/**
	 * @return
	 */
	public String getItemName()
	{
		return itemName;
	}

	/**
	 * @param string
	 */
	public void setItemName(String string)
	{
		itemName = string;
	}

}
