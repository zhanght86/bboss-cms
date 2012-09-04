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

/**
 * @author biaoping.yin
 * 2004-8-5
 */

public class XMLItemnameTag extends XMLBaseTag
{
	String itemValue;
	
	/* (non-Javadoc)
	 * @see com.westerasoft.common.tag.BaseTag#generateContent()
	 */
	public String generateContent()
	{
		if(data != null)
		{
			try
			{
	            if(getItemValue() != null)
	            {
	                return data.getItemName(getItemValue());
	            }
	            else if(t_value != null)
	            {
	                return data.getItemName(t_value);
	            }
			}
			catch (ProfessionDataManagerException e)
			{
				e.printStackTrace();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			
			return defaultName;
		}
		else
		{
			System.out.println("字典[" + this.type + "]不存在");
			return this.defaultName;
		}
	}


	/**
	 * @return
	 */
	public String getItemValue()
	{
		return itemValue;
	}

	/**
	 * @param string
	 */
	public void setItemValue(String string)
	{
		itemValue = string;
	}


	

}
