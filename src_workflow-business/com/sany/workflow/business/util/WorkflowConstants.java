package com.sany.workflow.business.util;

/**
 * 常量类型，用于定义系统中常量
 * 注意：如修改常量，需重新编译全部引用的类
 * @author fudk
 *
 */
public class WorkflowConstants {
	
	//流程TYPE
	public static final String PRO_BUSINESS_TYPE ="businessType";//业务类型
	public static final String PRO_TITLE ="title";//业务类型
	public static final String PRO_CANDIDATE_POSTFIX ="_users";//审批人参数后缀
	public static final String PRO_REALNAME_POSTFIX ="_realname";//审批人真实名称参数后缀
	public static final String PRO_TO_ACTID ="_toActId";//任意跳转名称参数后缀
	public static final String PRO_ACT_CANDIDATE_DEF ="system";//任意跳转名称参数前缀
	public static final String PRO_DEF_START ="usertask1";//流程启动节点key
	public static final String PRO_DEF_SECOND ="usertask2";//流程节点key
	public static final String PRO_DEF_COPY_TO ="抄送";//流程启动节点key
	public static final String PRO_ACTNODE ="actNode";//流程实例节点信息
	public static final String PRO_ISRETURN ="isReturn";//直接返回标志
	public static final String PRO_PAGETYPE ="pagetype";//页面类型（1.0版本使用）
	public static final String PRO_PAGESTATE ="pagestate";//页面类型（2.0版本使用）
	public static final String PRO_ACT_AUTO_POSTFIX  ="_auto";//自动审批类参数后缀
	public static final String PRO_ACT_AUTO_MET  ="_method";//自动审批方法后缀
	public static final String PRO_ACT_AUTO_ARGS  ="_args";//自动审批参数后缀
	public static final String PRO_COPY_TITLE_START  ="请查阅";//
	public static final String PRO_COPY_TITLE_APR  ="请审批";//
	
	//流程页面类型
	public static final Integer PRO_PAGETYPE_ADD= 1;        //流程新增页面
	public static final Integer PRO_PAGETYPE_READD= 2;        //流程新增页面,重新提交
	public static final Integer PRO_PAGETYPE_APPROVE= 3;        //流程审批页面
	public static final Integer PRO_PAGETYPE_UPDATE= 4;        //流程修改页面
	public static final Integer PRO_PAGETYPE_SHOW= 5;        //流程查看页面
	public static final Integer PRO_PAGETYPE_ENDSHOW= 6;        //流程结束后的查看页面
	
	//流程操作描述
	public static final String PRO_OPE_DES_SUBMIT = "提交流程";	
	public static final String PRO_OPE_DES_PASS = "通过流程";	
	public static final String PRO_OPE_DES_REJECT = "驳回流程";	
	public static final String PRO_OPE_DES_CANCLE = "取消流程";	
	public static final String PRO_OPE_DES_DELEGATE = "委派流程";	
	public static final String PRO_OPE_DES_COPY_TO = "抄送查看";	
	public static final String PRO_OPE_DES_RECALL = "撤回流程";	
	
	//新的页面状态类
	/**页面状态   新增初始状态*/
	public static final Integer PRO_PAGESTATE_INIT= 1;        
	/**页面状态   暂存或驳回提交人节点，提交人查看状态*/
	public static final Integer PRO_PAGESTATE_READD= 2;        
	/**页面状态   审批中提交人查看状态*/
	public static final Integer PRO_PAGESTATE_APPLYER= 3;        
	/**页面状态   审批中审批人查看状态*/
	public static final Integer PRO_PAGESTATE_APPROVE= 4; 
	/**页面状态   审批中或暂存中其他人查看状态*/
	public static final Integer PRO_PAGESTATE_SHOW= 5; 
	/**页面状态   流程结束后查看状态*/
	public static final Integer PRO_PAGESTATE_ENDSHOW= 6;     
	/**页面状态    审批中提交人与处理人一致权限累加查看状态*/
	public static final Integer PRO_PAGESTATE_APPLYER_AND_APPROVE= 7;   
	
	/**流程操作类型   流程通过*/
	public static final String PRO_OPE_TYPE_PASS = "pass";	
	/**流程操作类型   流程驳回*/
	public static final String PRO_OPE_TYPE_REJECT = "reject";	
	/**流程操作类型   流程取消、结束*/
	public static final String PRO_OPE_TYPE_TOEND = "toend";	
	/**流程操作类型   流程转办*/
	public static final String PRO_OPE_TYPE_TURNTO = "turnto";	
	/**流程操作类型   流程撤回*/
	public static final String PRO_OPE_TYPE_RECALL = "recall";	
	
	/**流程节点参数说明   单实例 */
	public static final String PRO_ACT_ONE = "0";
	/**流程节点参数说明   多实例串行 */
	public static final String PRO_ACT_SEQ = "10";
	/**流程节点参数说明   多实例并行 */
	public static final String PRO_ACT_MUL = "20";
	/**流程节点参数说明   会审 */
	public static final String PRO_ACT_SIG = "30";
	/**流程节点参数说明   抄送 */
	public static final String PRO_ACT_COP = "40";
	
	/**流程节点参数说明   重复审批人自动跳过 */
	public static final String PRO_ACT_AUTO_PER ="10";
	/**流程节点参数说明   节点自动审批 */
	public static final String PRO_ACT_AUTO ="20";
	/**流程节点参数说明   节点不自动审批 */
	public static final String PRO_ACT_NOT_AUTO ="10";
	/**流程节点参数说明   节点允许撤回 */
	public static final String PRO_ACT_CANRECALL ="10";
	/**流程节点参数说明   节点允许修改后续节点 */
	public static final String PRO_ACT_EDITAFTER ="10";
	/**流程节点参数说明   本节点能否被修改 */
	public static final String PRO_ACT_CANEDIT ="10";
	
	
	/** 显示已阅抄送记录数*/
	public static final Integer SHOW_READEDCOPYTASK_LIMIT= 100;  

}
