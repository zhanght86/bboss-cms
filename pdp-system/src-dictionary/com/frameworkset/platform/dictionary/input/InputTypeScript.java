/**
 * 
 */
package com.frameworkset.platform.dictionary.input;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frameworkset.platform.dictionary.DictAttachField;
import com.frameworkset.platform.dictionary.DictManager;
import com.frameworkset.platform.dictionary.DictManagerImpl;

/**
 * 输入类型的脚本生成 抽象类
 * <p>Title: InputTypeScript.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-12-14 15:28:09
 * @author ge.tao
 * @version 1.0
 */
public interface InputTypeScript extends java.io.Serializable {	
	public static final String SELECTORG_JS_FUNCTION = "";
	
	public static DictManager dictManager = new DictManagerImpl();
//	/**
//	 * 根据页面的字段名称，生成这个字段对应的onclick函数
//	 * 如：onclick='fiedName(this)'
//	 * @param fiedName
//	 * @param inputTypeName 输入类型名称
//	 * @param value 外部传入的值, 缺省值或者update的时候,数据的已有值
//	 * @param maxLength 最大长度
//	 * @return String 
//	 */
//	public String getFunctionScript(String fiedName, String inputTypeName,String value,int maxLength);
//	
//	/**
//	 * 根据页面的字段名称，生成这个字段对应的onclick函数的实现
//	 * 如：
//	 * <script>
//	 * function fiedName(obj){
//	 *    ...
//	 * }
//	 * </script>
//	 * @param extend
//	 * @return
//	 */
//	public String getFunctionContent(String fiedName, String inputTypeName,String value,int maxLength);
	
	/**
	 *  生成编辑附件字段额外的HTML
	 */
	 public String getEditExtendHtmlContent(HttpServletRequest request,HttpServletResponse response,Map keyWords);
	 
	 /**
	 *  生成添加附件字段额外的HTML
	 */
	 public String getNewExtendHtmlContent(HttpServletRequest request,HttpServletResponse response);
}
