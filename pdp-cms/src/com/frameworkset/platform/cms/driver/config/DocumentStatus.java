package com.frameworkset.platform.cms.driver.config;

import java.util.ListResourceBundle;


/**
 * 
 * <p>Title: com.frameworkset.platform.cms.driver.config.DocumentStatus.java</p>
 *
 * <p>Description: 定义文档的状态标识:新稿,已审,已发布,已归档,已回收</p>
 *
 * <p>Copyright: Copyright (c) 2007</p>
 *
 * <p>Company: iSany</p>
 * @Date 2007-1-10
 * @author biaoping.yin
 * @version 1.0
 */
public final class DocumentStatus implements java.io.Serializable{
	
	
	
	/**
	 * 新稿状态
	 */
	public final static DocumentStatus NEW = new DocumentStatus("1","新稿");
	
	
	/**
	 * 待审状态
	 */
	public final static DocumentStatus UNAUDIT = new DocumentStatus("2","待审");
	/**
	 * 已审状态
	 */
	public final static DocumentStatus AUDITED = new DocumentStatus("3","已审");
	/**
	 * 返工状态
	 */
	public final static DocumentStatus ROLLBACK = new DocumentStatus("4","返工");
	
	/**
	 * 已发布状态
	 */
	public final static DocumentStatus PUBLISHED = new DocumentStatus("5","已发布");
	
	/**
	 * 已撤发状态
	 */
	public final static DocumentStatus WITHDRAWPUBLISHED = new DocumentStatus("6","已撤发");
	
	/**
	 * 已归档状态
	 */
	public final static DocumentStatus ARCHIVED = new DocumentStatus("7","已归档");
	
	/**
	 * 已回收状态
	 */
	public final static DocumentStatus RECYCLED = new DocumentStatus("8","已回收");
	
	/**
	 * 正在审核状态
	 */
	public final static DocumentStatus AUDITING = new DocumentStatus("9","正在审核");
	
	
	/**
	 * 正在发布状态
	 */
	public final static DocumentStatus PUBLISHING = new DocumentStatus("10","正在发布");
	
	/**
	 * 待发布
	 */
	public final static DocumentStatus PREPUBLISH = new DocumentStatus("11","待发布");

	private String _status;
	private String _desc;
	
	public String getStatus()
	{
		return this._status;
	}
	
	public String getDescription()
	{
		return this._desc;
	}

	public DocumentStatus(String status,String description) {
		if (status == null) {
			
			throw new IllegalArgumentException(
					"DocumentStatus name can not be NULL");
		}
		this._desc = description;
		_status = status.toLowerCase();
	}

	/**
	 * Returns a String representation of this portlet mode. Portlet mode names
	 * are always lower case names.
	 * 
	 * @return String representation of this portlet mode
	 */

	public String toString() {
		return _status;
	}

	/**
	 * Returns the hash code value for this portlet mode. The hash code is
	 * constructed by producing the hash value of the String value of this mode.
	 * 
	 * @return hash code value for this portlet mode
	 */

	public int hashCode() {
		return _status.hashCode();
	}

	/**
	 * Compares the specified object with this portlet mode for equality.
	 * Returns <code>true</code> if the Strings <code>equals</code> method
	 * for the String representing the two portlet modes returns
	 * <code>true</code>.
	 * 
	 * @param the
	 *            portlet mode to compare this portlet mode with
	 * 
	 * @return true, if the specified object is equal with this portlet mode
	 */

	public boolean equals(Object object) {
		if (object instanceof DocumentStatus)
			return _status.equals(((DocumentStatus) object)._status);
		else
			return false;
	}
	
	public static DocumentStatus getDocumentStatus(int status)
	{
		try
		{
			return (DocumentStatus)statuses.getObject(status + "");
		}
		catch(Exception e)
		{
			//e.printStackTrace();
			return DocumentStatus.NEW;
		}
	}
	
	private static ListResourceBundle statuses = new StatusListResourceBundle();
	
	static class StatusListResourceBundle extends ListResourceBundle
	{
		private Object contents[][] = {
				{"1",DocumentStatus.NEW},
				{"2",DocumentStatus.UNAUDIT},
				{"3",DocumentStatus.AUDITED},
				{"4",DocumentStatus.ROLLBACK},
				{"5",DocumentStatus.PUBLISHED},
				{"6",DocumentStatus.WITHDRAWPUBLISHED},
				{"7",DocumentStatus.ARCHIVED},
				{"8",DocumentStatus.RECYCLED},
				{"9",DocumentStatus.AUDITING},
				{"10",DocumentStatus.PUBLISHING},
				{"11",DocumentStatus.PREPUBLISH}
				
		};
		
		protected Object[][] getContents() {
			// TODO Auto-generated method stub
			return contents;
		}
		
	}



}
