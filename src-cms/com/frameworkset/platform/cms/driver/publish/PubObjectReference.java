package com.frameworkset.platform.cms.driver.publish;

/**
 * 封装发布对象与被该发布对象引用的对象之间的关系
 * <p>Title: PubObjectReference</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-7-15 10:05:27
 * @author biaoping.yin
 * @version 1.0
 */
public class PubObjectReference implements Comparable,java.io.Serializable{
	/**
	 * 如果是站点发布对象就对应于站点的id
	 * 如果是频道则对应于频道的id
	 * 如果是页面，就对应于页面的相对地址
	 * 如果发布对象的类型是文档则对应于文档的id
	 */
	private String pubobject;
	/**
	 * 发布对象对应的站点，对应于站点的英文名称
	 */
	private String pubSite;
	
	/**
	 * 如果引用的对象是文档，则为文档id
	 * 如果引用对象是频道，则为频道id
	 */
	private String referenceObject;
	/**
	 * 参见<com.frameworkset.platform.cms.driver.publish.RecursivePublishManager>中
	 * 发布对象类型常量定义
	 */
	private int pubobjectType;
	
	/**
	 * 参见<com.frameworkset.platform.cms.driver.publish.RecursivePublishManager>中
	 * 引用对象类型常量定义
	 */
	private int refobjectType;
	/**
	 * 发布对象引用元素对应的站点的英文名称
	 */
	private String refSite;
	
	/**
	* 如果要持久化发布对象引用关系，就需要判断所有的属性是否相等
	* 在发布对象时则只需要判断发布对象的相关属性是否相等就可以了
	* false,需要持久化，要比较所有属性（forpublish除外），缺省值为false
	* true，不需要持久化，获取发布对象时，将该属性设置为true，只需要比较发布对象的属性，不需要比较引用对象的属性
	* 
	*/
	private boolean forpublish = false;
	public String getPubobject() {
		return pubobject;
	}
	public void setPubobject(String pubobject) {
		this.pubobject = pubobject;
	}
	public int getPubobjectType() {
		return pubobjectType;
	}
	public void setPubobjectType(int pubobjectType) {
		this.pubobjectType = pubobjectType;
	}
	public String getReferenceObject() {
		return referenceObject;
	}
	public void setReferenceObject(String referenceObject) {
		this.referenceObject = referenceObject;
	}
	
	public int getRefobjectType() {
		return refobjectType;
	}
	public void setRefobjectType(int refobjectType) {
		this.refobjectType = refobjectType;
	}
	
	public String toString()
	{
		StringBuffer sql = new StringBuffer();
		sql.append("REFERENCEOBJECT='").append(getReferenceObject())
		.append("',PUBLISHTYPE=").append(getPubobjectType())
		.append(",PUBLISH_SITE='").append(getPubSite())
		.append("',PUBLISHOBJECT='").append(getPubobject())
		.append("',REFERENCETYPE=").append(getRefobjectType())
		.append("',REFERENCETYPE=").append(getRefSite());
		return sql.toString();
	}
	public String getPubSite() {
		return pubSite;
	}
	public void setPubSite(String pubSite) {
		this.pubSite = pubSite;
	}
	public String getRefSite() {
		return refSite;
	}
	public void setRefSite(String refSite) {
		this.refSite = refSite;
	}
	
	
	/**
	 * 如果要持久化发布对象引用关系，就需要判断所有的属性是否相等
	 * 在发布对象时则只需要判断发布对象的相关属性是否相等就可以了
	 * 通过forpublish属性的值来进行判断
	 * @param other
	 * @return
	 */
	public boolean equal(Object other)
	{
		try
		{
			if(other instanceof PubObjectReference)
			{	
				
				PubObjectReference other_ = (PubObjectReference)other;
				if(!this.forpublish)
				{
					return other_.getPubobject().equals(this.getPubobject()) 
								&& other_.getPubobjectType() == this.getPubobjectType()
								&& other_.getPubSite().equals(this.getPubSite()) 
								&& other_.getReferenceObject().equals(this.getReferenceObject()) 
								&& other_.getRefobjectType() == this.getRefobjectType() 
								&& other_.getRefSite().equals(this.getRefSite()) ;
				}
				else
				{
					return other_.getPubobject().equals(this.getPubobject()) 
					&& other_.getPubobjectType() == this.getPubobjectType()
					&& other_.getPubSite().equals(this.getPubSite()) ;
				}
			}
			else
			{
				return false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public int compareTo(Object o) {
		try
		{
			if(this.equal(o))			
				return 0;
			else
				return 1;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return 1;
		}
	}
	public boolean isForpublish() {
		return forpublish;
	}
	public void setForpublish(boolean forpublish) {
		this.forpublish = forpublish;
	}
	

}
