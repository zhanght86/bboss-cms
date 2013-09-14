package com.frameworkset.platform.sanylog.dictionary;

public class Dictionary {
public static String [] titles = {"序号:appId","系统:appName","功能路径:moduleName","操作次数/人数:vcount"};
public static int FunctionListExcelNum = 5;
public static String FunctionList = "com.frameworkset.platform.sanylog.bean.FunctionList";
public static String [] FunctionListExcelKeys = {"0","1","2","3","4"};
public static String [] FunctionListExcelValues = {"appName","functionCode","functionName","estimateUser","estimateOper"};
public static String [] FunctionListExcelInfos = {"应用名称","功能编码","功能名称","预计使用人数","预计使用频次"};
public static String [] SubjectExcelNotNullValues = {"appName","functionCode","functionName","estimateUser","estimateOper"};
public static String [] SubjectExcelNotNullInfos = {"应用名称","功能编码","功能名称","预计使用人数","预计使用频次"};

public static int PageListExcelNum = 3;
public static String PageList = "com.frameworkset.platform.sanylog.bean.PageList";
public static String [] PageListExcelKeys = {"0","1","2"};
public static String [] PageListExcelValues = {"functionName","moduleCode","functionCode"};
public static String [] PageListExcelInfos = {"功能标示","功能路径","功能编码"};
public static String [] PageListSubjectExcelNotNullValues = {"functionName"};
public static String [] PageListSubjectExcelNotNullInfos = {"功能标示"};


}
