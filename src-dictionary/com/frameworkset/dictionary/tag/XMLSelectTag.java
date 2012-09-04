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


import javax.servlet.jsp.JspException;

import org.apache.ecs.html.Option;
import org.apache.ecs.html.Select;

import com.frameworkset.dictionary.DataManager;
import com.frameworkset.dictionary.DataManagerFactory;
import com.frameworkset.dictionary.Item;

/**
 * @author biaoping.yin
 * 2004-8-2
 */
public class XMLSelectTag extends XMLBaseTag
{
	protected String textValue;
	/* (non-Javadoc)
	 * @see com.westerasoft.common.tag.BaseTag#generateContent()
	 */
	public String generateContent()
	{
		Select select = new Select();
		select.setName(getName());
		select.setDisabled(this.isDisabled());
		select.setExtend(this.getExtend());
		if(this.data != null)
		{
			Option[] options = null;
			int temp = 0;
			if(this.getTextValue() != null)
			{
				options = new Option[data.size() + 1];
				options[0] = new Option().setValue("NaN");
				options[0].setTagText(getTextValue());
				temp ++;
			}
			else
			{
				options = new Option[data.size()];
			}

			DataManager  dataManager = DataManagerFactory.getDataManager();
			for(int i = temp ; i < data.size() + temp; i ++)
			{				
				Item item = data.getItem(i - temp);
				//设置了权限过滤
//				if(this.isCheckPermission() && !super.accesscontroler.checkPermission(ids,this.getOpcode(),"orgTaxcode")){
				if(this.isCheckPermission()){
//					登陆用户的机构
					String orgId = super.accesscontroler.getChargeOrgId();
					if("read".equalsIgnoreCase(getOpcode())){//可见
						if(!dataManager.hasOrgTaxcodeRelation(orgId,item.getDataId(),item.getValue(),getOpcode()) 
								&& data.getDicttype_type()==2){//没有机构编码关系,并且字典类型是授权可见的							
							continue;
					    }
					}else if("usual".equalsIgnoreCase(getOpcode())){//常用
						if(!dataManager.hasOrgTaxcodeRelation(orgId,item.getDataId(),item.getValue(),getOpcode()) 
								&& (data.getDicttype_type()==1 || data.getDicttype_type()==2)){//没有机构编码关系,并且字典类型是业务字典							
							continue;
					    }
					}
				}
				options[i] = new Option().setValue(item.getValue())
									.setSelected(t_value != null && t_value.equals(item.getValue()));
				options[i].setTagText(item.getName());
				
			}
			
			select.addElement(options);
			if(getStyle() != null)
				select.setStyle(getStyle());
			//buffer.append(data.getName());
			return select.toString();
		}
		else
		{
			Option[] options = new Option[1];
			options[0] = new Option().setValue("NaN");
			options[0].setTagText("字典[" + this.type + "]不存在");
			select.addElement(options);
			return select.toString();
		}
	}

	/**
	 * @return
	 */
	public String getTextValue()
	{
		return textValue;
	}

	/**
	 * @param string
	 */
	public void setTextValue(String string)
	{
		textValue = string;
	}
	
	public int doEndTag() throws JspException
	{
		int ret = super.doEndTag();
		this.textValue = null;
		return ret;
	}

}
