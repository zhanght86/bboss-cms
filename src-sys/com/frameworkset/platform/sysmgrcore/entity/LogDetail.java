package com.frameworkset.platform.sysmgrcore.entity;

import java.io.Serializable;

/**
 * <p>类名称：日志明细</p>
 * <p>类说明：将日志明确到表以及表中的字段改变内容</p>
 * @author 李峰高
 * @date Jun 13, 2008 2:34:09 PM
 * @version 1.0
 */
public class LogDetail implements Serializable{

	private static final long serialVersionUID = 5026032549166178450L;
	
	//操作类型：无操作
	public static final int NULL_OPER_TYPE = 0 ;
    //操作类型：插入
	public static final int INSERT_OPER_TYPE = 1 ;
	//操作类型：更新
	public static final int UPDATE_OPER_TYPE = 2 ;
	//操作类型：删除
	public static final int DELETE_OPER_TYPE = 3 ;
	//操作类型：其他
	public static final int OTHER_OPER_TYPE = 4 ;
	
	
	/**
	 * 日志明细id
	 */
	private long detailID ;
	
	/**
	 * 日志id
	 */
	private long logID ; 
	
	/**
	 * 日志操作的表名
	 */
	private String operTable ;
	
	/**
	 * 操作表记录id值
	 */
	private String operKeyID ;
	
	/**
	 * 操作记录内容
	 */
	private String detailContent ;
	
	/**
	 * 操作类型
	 */
	private int operType ;

	
	
	/************方法*************/
	
	/**
	 * <p>所有域的构造方法</p>
	 */
	public LogDetail(long detailID, long logID, String operTable,
			String operKeyID, String detailContent, int operType) {
		super();
		this.detailID = detailID;
		this.logID = logID;
		this.operTable = operTable;
		this.operKeyID = operKeyID;
		this.detailContent = detailContent;
		this.operType = operType;
	}
	
	/**
	 * <p>不含主键的构造方法</p>
	 */
	public LogDetail(long logID, String operTable,String operKeyID, String detailContent, int operType) {
		super();		
		this.logID = logID;
		this.operTable = operTable;
		this.operKeyID = operKeyID;
		this.detailContent = detailContent;
		this.operType = operType;
	}
	
	/**
	 * <p>不含明细id和日志id的构造方法</p>
	 */
	public LogDetail(String operTable,String operKeyID, String detailContent, int operType) {
		super();		
		this.operTable = operTable;
		this.operKeyID = operKeyID;
		this.detailContent = detailContent;
		this.operType = operType;
	}
	
	
	/**
	 * <p>明细id以及日志id的构造方法</p>
	 * @param detailID
	 * @param logID
	 */
	public LogDetail(long detailID, long logID) {
		super();
		this.detailID = detailID;
		this.logID = logID;
	}

	/**
	 * <p>主键：明细id构造方法</p>
	 * @param detailID
	 */
	public LogDetail(long detailID) {
		super();
		this.detailID = detailID;
	}
	
	/**
	 * <p>空构造方法</p>
	 */
	public LogDetail() {
		super();		
	}

	public long getDetailID() {
		return detailID;
	}

	public void setDetailID(long detailID) {
		this.detailID = detailID;
	}

	public long getLogID() {
		return logID;
	}

	public void setLogID(long logID) {
		this.logID = logID;
	}

	public String getOperTable() {
		return operTable;
	}

	public void setOperTable(String operTable) {
		this.operTable = operTable;
	}

	public String getOperKeyID() {
		return operKeyID;
	}

	public void setOperKeyID(String operKeyID) {
		this.operKeyID = operKeyID;
	}

	public String getDetailContent() {
		return detailContent;
	}

	public void setDetailContent(String detailContent) {
		this.detailContent = detailContent;
	}

	public int getOperType() {
		return operType;
	}

	public void setOperType(int operType) {
		this.operType = operType;
	} 
	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (detailID ^ (detailID >>> 32));
		result = prime * result + (int) (logID ^ (logID >>> 32));
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final LogDetail other = (LogDetail) obj;
		if (detailID != other.detailID)
			return false;
		if (logID != other.logID)
			return false;
		return true;
	} 
	
	
}
