package com.frameworkset.platform.ldap;

public class AttrBean implements java.io.Serializable {
  private String attrName;  //属性名
  private String newValue;//属性值
  public AttrBean() {

  }
 public AttrBean(String name,String value) {
	  this.attrName = name;
	  this.newValue = value;
   }
  public String getAttrName() {
    return attrName;
  }
  public void setAttrName(String attrName) {
    this.attrName = attrName;
  }
  public String getNewValue() {
    return newValue;
  }
  public void setNewValue(String newValue) {
    this.newValue = newValue;
  }

}
