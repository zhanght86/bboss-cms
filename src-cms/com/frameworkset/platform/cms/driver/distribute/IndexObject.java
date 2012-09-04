package com.frameworkset.platform.cms.driver.distribute;

import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.driver.htmlconverter.CMSTemplateLinkTable;

/**
 * 索引对象，发布过程中需要建立索引的对象包括文档细览页面(type=0)、频道首页(type=1)、站点首页(type=2)
 * <p>Title: IndexObject</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-9-24 9:56:43
 * @author biaoping.yin
 * @version 1.0
 */
public class IndexObject implements Comparable,java.io.Serializable
{
	public static final int DOCUMENT = 0;
	public static final int CHANNEL = 1;
	public static final int SITE = 2;
	public static final int PAGER = 3;
	
	/**
	 * 发布过程中需要建立索引的对象包括文档细览页面(type=0)、频道首页(type=1)、站点首页(type=2)、普通页面 (type=3)
	 */
	private int type;
	
	/**
	 * type=0 Document
	 * type=1 Channel
	 * type=2 Site
	 */
	private Object indexObject;
	
	/**
	 * 标识索引对象
	 */
	private String uid;
	private Document document;
	/**
	 * 
	 * @param type 索引对象类型，
	 * @param indexObject 索引对象
	 */
	public IndexObject(String uid,int type,Object indexObject,Document document,CMSTemplateLinkTable contentOrigineTemplateLinkTable)
	{
		this.type = type ;
		this.indexObject = indexObject;
		this.uid = uid;
		this.document = document;
		this.contentOrigineTemplateLinkTable = contentOrigineTemplateLinkTable;
	}
	
	
	/**
	 * 
	 * @param type 索引对象类型，
	 * @param indexObject 索引对象
	 */
	public IndexObject(String uid,int type,Object indexObject)
	{
		this.type = type ;
		this.indexObject = indexObject;
		this.uid = uid;
		
	}
	

	
	public Object getIndexObject() {
		return indexObject;
	}
	
	public int getType() {
		return type;
	}
	
	public String getUid()
	{
		return this.uid;
	}

	/**
	 * 避免在Set容器中存入重复的IndexObject,需要调用本方法判断
	 * IndexObject之间是否是一样的，依据就是uid和type相等就认为是一致的
	 */
	public int compareTo(Object o) {		
		if(o instanceof IndexObject)
		{
			IndexObject other = (IndexObject)o;
			if(other.getUid().equals(this.getUid()) 
					&& other.getType() == other.getType())
			{
				return 0;
			}
			else
			{
				return -1;
			}
		}
		else
		{
			return -1;
		}
	}


	public Document getDocument() {
		return document;
	}
	protected CMSTemplateLinkTable contentOrigineTemplateLinkTable;

	
	public CMSTemplateLinkTable getContentOrigineTemplateLinkTable() {
		// TODO Auto-generated method stub
		return contentOrigineTemplateLinkTable;
	}


}
