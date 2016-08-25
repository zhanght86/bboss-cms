package com.frameworkset.platform.sysmgrcore.entity;

import java.sql.Timestamp;
import java.util.List;

/**
 * <p>类名称：日志</p>
 * <p>类说明：用来记录的日志。</p>
 * @author 李峰高
 * @date Jun 18, 2008 4:33:37 PM
 * @version 1.0
 */
public class Log extends AbstractLog    
{	
    private static final long serialVersionUID = -970408296025479961L;

    //操作类型：无操作
	public static final int NULL_OPER_TYPE = 0 ;
    //操作类型：新增
	public static final int INSERT_OPER_TYPE = 1 ;
	//操作类型：更新
	public static final int UPDATE_OPER_TYPE = 2 ;
	//操作类型：删除
	public static final int DELETE_OPER_TYPE = 3 ;
	//操作类型：其他
	public static final int OTHER_OPER_TYPE = 4 ;
    
	public Log(){
    	super();
    }
    
	/**
	 * 主键构造
	 * @param logId
	 */
    public Log(Integer logId){
        super(logId);
    }
    
    /**
     * 操作类型构造(5类)
     * @param operType
     */
    public Log(int operType){
    	super();
    	this.operType = operType;
    }

    /**
     * 全员构造
     * @param operUser
     * @param operOrg
     * @param logModule
     * @param visitorial
     * @param operTime
     * @param oper
     * @param remark1
     * @param operType
     */
    public Log(Integer logId,String operUser,String operOrg,String logModule, String visitorial,
    		Timestamp operTime,String oper ,String remark1, int operType) {
		super();
		super.setLogId(logId) ;
		super.setOperUser(operUser);
		super.setVisitorial(visitorial);
		super.setOperTime(operTime);
		super.setOper(oper);
		super.setRemark1(remark1);
		
		this.logModule = logModule;
		this.operOrg = operOrg;
		this.operType = operType;
	}	
	
    /**
     * 带用户信息的构造方法
     * @param operUser
     * @param operOrg
     * @param logModule
     * @param visitorial
     * @param operTime
     * @param oper
     * @param remark1
     * @param operType
     */
    public Log(String operUser,String operOrg,String logModule, String visitorial,
    		String oper ,String remark1, int operType) {
		super();		
		super.setOperUser(operUser);
		super.setVisitorial(visitorial);		
		super.setOper(oper);
		super.setRemark1(remark1);
		
		this.logModule = logModule;
		this.operOrg = operOrg;
		this.operType = operType;
	}
    
    /**
     * 不带用户信息的构造方法
     * @param operUser
     * @param operOrg
     * @param logModule
     * @param visitorial
     * @param operTime
     * @param oper
     * @param remark1
     * @param operType
     */
    public Log(String logModule, String oper ,String remark1, int operType) {
		super();				
		super.setOper(oper);
		super.setRemark1(remark1);		
		this.logModule = logModule;		
		this.operType = operType;
	}
    
    
    /* Add customized code below */
    /**
     * 日志模块名称
     */
    private String logModule ;
    
    /**
     * 操作机构（操作人所在机构）
     */
    private String operOrg ;    
    
    /**
     * 操作类型
     */
    private int operType ;   
    
    
	
	/**
	 * 日志明细列表，一条日志可以对应多条明细。
	 */
	public List detailList ;	
	
	
	public String getOperOrg() {
		return operOrg;
	}

	public void setOperOrg(String operOrg) {
		this.operOrg = operOrg;
	}

	public int getOperType() {
		return operType;
	}

	public void setOperType(int operType) {
		this.operType = operType;
	}

	/**
	 * <p>返回日志列表明细</p>
	 * @return List<com.frameworkset.platform.sysmgrcore.entity.LogDetail>
	 */
	public List getDetailList() {
		return detailList;
	}

	public void setDetailList(List detailList) {
		this.detailList = detailList;
	}

	public String getLogModule() {
		return logModule;
	}

	public void setLogModule(String logModule) {
		this.logModule = logModule;
	}
}
