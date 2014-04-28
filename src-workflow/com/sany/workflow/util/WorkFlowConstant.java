package com.sany.workflow.util;

public class WorkFlowConstant {

    /**
     * 待办配置类型:通用
     */
    public static final String BUSINESS_TYPE_COMMON = "0";

    /**
     * 待办配置类型:组织机构
     */
    public static final String BUSINESS_TYPE_ORG = "1";

    /**
     * 待办配置类型:业务类型
     */
    public static final String BUSINESS_TYPE_BUSINESSTYPE = "2";
    
    
    
    public static final int PARAM_READONLY = 1;
    
    public static final int PARAM_EDIT = 0;
    
    /**
     * 应用配置类型:第三方应用
     */
    private static final String app_third_mode_type = "第三方应用";
    
    public static String getApp_third_mode_type(){
    	
    	return app_third_mode_type;
    }
    
    /**
     * 应用配置类型:单点登陆的应用类型  独立库应用
     */
    private static final String app_sso_mode_type = "独立库应用";
    
    public static String getApp_sso_mode_type(){
    	
    	return app_sso_mode_type;
    }
    
    private static final String tocken_service_url = "http://localhost:8080/SanyPDP/hessian?service=tokenService";
    
    public static String getTocken_service_url(){
    	
    	return tocken_service_url;
    }
}
