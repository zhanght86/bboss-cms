/*
 * Title: The ERP System of kelamayi Downhole Company [PMIP]
 *
 * Copyright: Copyright (c) 2000-2004 westerasoft Co., Ltd All right reserved.
 *
 * Company: westerasoft Co., Ltd
 *
 * All right reserved.
 *
 * Created on 2004-8-5
 *
 * JDK version used		:1.4.1
 *
 * Modification history:
 *
 *
 */
package com.frameworkset.dictionary.tag;

import com.frameworkset.dictionary.ProfessionDataManagerException;
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


	public String generateContent()
	{
		if(data != null)
		{
			try
			{
	            if(getItemName() != null)
				    return data.getItemValue(this.getItemName());
	            else if(t_value != null)
	            {
	                return data.getItemValue(t_value);
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
