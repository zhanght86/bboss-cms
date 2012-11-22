
package com.frameworkset.dictionary;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author biaoping.yin
 * 改类用来封装专业数据中具体的数据项
 * @@org.jboss.cache.aop.AopMarker
 */
public class Item implements Serializable{
	public static final int DATAVALIDATE_TRUE = 1;
	public static final int DATAVALIDATE_FALSE = 0;
	private String itemId;
	private String dataId;
    private String name;
    private String value;
    private int order;
    protected List subItems;
    /**
     * 父数据项索引
     */
    private String parentId;
    /**
     * List<AttachField>
     */
    private List attachFields;
    /**
     * 数据项所属的机构
     */
    private String dataOrg;
    /**
     * 数据项是否有效
     * 0 无效
     * 1 有效
     */
    private int dataValidate = 1;
    
    private boolean flag ;
    
    private String orgId;
    
    //
    private String oldParentId;
    
    /**
	 * 存放字典主键值
	 * Map<primarykeyName,primarykeyValue>
	 */
	private Map primarykeys ;
	
	//存放主键条件串  例如："  and a.org_id='179'"；不包含"值字段"与"名称字段"的主键条件串
	private String primaryCondition = "";

    public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	/**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }
    /**
     * @param value The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }

    public boolean equals(Object obj)
    {
        if(obj == null)
            return false;
        try
        {
            Item tep = (Item)obj;
            return tep.name.equals(this.name)
            			&& tep.value.equals(this.value);
        }
        catch(Exception e)
        {
            return false;
        }

     }

    public String toString()
    {
        return new StringBuffer().append("item:[name="+name+"][value="+value+"]\r\n").toString();
    }

	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	public String getDataId() {
		return dataId;
	}
	public void setDataId(String dataId) {
		this.dataId = dataId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public List getAttachFields() {
		
		return attachFields;
	}
	public void setAttachFields(List attachFields) {
		this.attachFields = attachFields;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	

	public List getSubItems() {
		return subItems;
	}
	public void setSubItems(List subItems) {
		this.subItems = subItems;
	}
	
	public void addSubItem(Item item){
		if(this.subItems==null) 
			subItems = new ArrayList();
		this.subItems.add(item);
	}
	public String getDataOrg() {
		return dataOrg;
	}
	public void setDataOrg(String dataOrg) {
		this.dataOrg = dataOrg;
	}
	public int getDataValidate() {
		return dataValidate;
	}
	public void setDataValidate(int dataValidate) {
		this.dataValidate = dataValidate;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public Map getPrimarykeys() {
		return primarykeys;
	}
	public void setPrimarykeys(Map primarykeys) {
		this.primarykeys = primarykeys;
	}
	public String getPrimaryCondition() {
		return primaryCondition;
	}
	public void setPrimaryCondition(String primaryCondition) {
		this.primaryCondition = primaryCondition;
	}
	public String getOldParentId() {
		return oldParentId;
	}
	public void setOldParentId(String oldParentId) {
		this.oldParentId = oldParentId;
	}
}
