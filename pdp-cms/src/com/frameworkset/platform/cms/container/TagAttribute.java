package com.frameworkset.platform.cms.container;


/**
 * 标签的属性
 */
public class TagAttribute implements java.io.Serializable
{
    
    /**
     * 属性名称
     */
    private String name;
    
    /**
     * 属性值
     */
    private String defaultValue;
    
    private String label;
    
    private String tagId;
    
    private String required;
    
    
    /**
     * @since 2006.12
     */
    public TagAttribute() 
    {
     
    }
    
    /**
     * Access method for the name property.
     * 
     * @return   the current value of the name property
     */
    public String getName() 
    {
        return name;
    }
    
    /**
     * Sets the value of the name property.
     * 
     * @param aName the new value of the name property
     */
    public void setName(String aName) 
    {
        name = aName;
    }

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
	}
    
    public boolean isRequired()
    {
    	return "0".equals(this.required)?false:true;
    }
}
