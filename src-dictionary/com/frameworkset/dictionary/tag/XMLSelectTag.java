
package com.frameworkset.dictionary.tag;


import java.util.List;

import javax.servlet.jsp.JspException;

import org.apache.ecs.html.Option;
import org.apache.ecs.html.Select;
import org.frameworkset.util.I18NUtil;

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
	/**
	 * 默认项的国际化值
	 */
	protected String textValueCode;
	
	/**
	 * 指定默认项的默认的值
	 */
	protected String textNAN = "NaN";
	protected boolean multiple;
	
	/* (non-Javadoc)
	 * @see com.westerasoft.common.tag.BaseTag#generateContent()
	 */
	public String generateContent()
	{
		Select select = new Select();
		select.setName(getName());
		select.setDisabled(this.isDisabled());
		select.setExtend(this.getExtend());
		select.setMultiple(multiple);
		
		if(this.data != null)
		{
			List<String> selected = null;
			if(this.multiple && this.t_value != null)
			{
				selected = selected();
			}
			Option[] options = null;
			int temp = 0;
			if(this.getTextValue() != null)
			{
				options = new Option[data.size() + 1];
				options[0] = new Option().setValue(textNAN);
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
				if(item.getDataValidate() == 0) continue;
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
				Option option = new Option().setValue(item.getValue());
				if(this.multiple)
				{
					if(selected(selected,item.getValue()))
						option.setSelected(true);
				}
				else
				{
					if(this.t_value != null && String.valueOf(t_value).equals(item.getValue()))
					{
						option.setSelected(true);
					}
				}
				option.setTagText(item.getName());
				options[i] = option;
				
				
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
		if(textValue != null)
			return textValue;
		if(this.textValueCode != null)
			return textValue = I18NUtil.getI18nMessage(textValueCode, this.textValue,request);
		else
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
		this.multiple = false;
		textValueCode = null;
		textNAN = "NaN";
		return ret;
	}

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	public String getTextValueCode() {
		return textValueCode;
	}

	public void setTextValueCode(String textValueCode) {
		this.textValueCode = textValueCode;
	}

	public String getTextNAN() {
		return textNAN;
	}

	public void setTextNAN(String textNAN) {
		this.textNAN = textNAN;
	}
}
