
package com.frameworkset.dictionary.tag;

import org.apache.ecs.html.Input;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.dictionary.DataManager;
import com.frameworkset.dictionary.DataManagerFactory;
import com.frameworkset.dictionary.Item;

/**
 * @author biaoping.yin
 * 2004-8-2
 */
public class XMLRadioTag extends XMLBaseTag
{
	protected String vertical;
	/* (non-Javadoc)
	 * @see com.westerasoft.common.tag.BaseTag#generateContent()
	 */
	public String generateContent()
	{

		if(data != null)
		{
			StringBuffer ret = new StringBuffer();

			DataManager  dataManager = DataManagerFactory.getDataManager();
			for(int i = 0; i < data.size(); i ++)
			{
				Input input = new Input();
				Item item = data.getItem(i);
				if(item.getDataValidate() == 0) continue;
				//设置了权限过滤
//				if(this.isCheckPermission() && !super.accesscontroler.checkPermission(ids,this.getOpcode(),"orgTaxcode")){
//			    	continue;
//			    }
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
				input.setType(Input.RADIO)
					 .setName(getName())
					 .setValue(item.getValue())
					 .setChecked(t_value != null && t_value.equals(item.getValue())).setExtend(this.getExtend());
				input.setDisabled(this.isDisabled());
				if(getStyle() != null)
					input.setStyle(getStyle());
				input.setTagText(item.getName());
				ret.append(input.toString());
				if(getVertical().trim().equalsIgnoreCase("true"))
				{
					ret.append("<br>");
				}
			}
			
			return ret.toString();
		}
		else
		{
			return "字典[" + this.type + "]不存在";
		}
	}
	/**
	 * @return
	 */
	public String getVertical()
	{
		return vertical == null? "false":vertical;
	}

	/**
	 * @param string
	 */
	public void setVertical(String string)
	{
		vertical = string;
	}
}
