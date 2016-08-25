package com.frameworkset.platform.dictionary.input;

import java.util.List;

import org.frameworkset.spi.Provider;

import com.frameworkset.platform.sysmgrcore.exception.ManagerException;

/**
 * 项目：InputTypeManager <br>
 * 描述：输入类型管理接口 <br>
 * 版本：1.0 <br>
 * 
 * @author zjx
 */
public interface InputTypeManager extends Provider {
	
	/**
	 * 删除多个输入类型,当选中的输入类型全部被删除时, 删除方法返回 0
	 */
	public static final int ALL_DELETE = 0;
	
	/**
	 * 删除多个输入类型,当选中的输入类型部分被删除时, 删除方法返回 1
	 * 部分没有删除 是因为被字典的附件字段引用
	 */
	public static final int PART_DELETE = 1;
	
	/**
	 * 删除多个输入类型,当选中的输入类型全部没有被删除时, 删除方法返回 2
	 * 部分没有删除 是因为被字典的附件字段引用
	 */
	public static final int NO_DELETE = 2;
	
	/**
	 * 六种基本输入类型,不允许修改和删除
	 * 文本类型  选择字典  选择时间  主键生成  选择机构  选择人员 当前用户 当前机构 当前时间
	 */
	public static final String BASE_INPUTTYPE_TEXT = "文本类型";
	
	public static final String BASE_INPUTTYPE_DICT = "选择字典";
	
	public static final String BASE_INPUTTYPE_DATE = "选择时间";
	
	public static final String BASE_INPUTTYPE_PK = "主键生成";
	
	public static final String BASE_INPUTTYPE_ORG = "选择机构";
	
	public static final String BASE_INPUTTYPE_USER = "选择人员";
	
	public static final String BASE_INPUTTYPE_CURRENT_USER = "当前用户";
	
	public static final String BASE_INPUTTYPE_CURRENT_ORG = "当前机构";
	
	public static final String BASE_INPUTTYPE_CURRENT_DATE = "当前时间";
		
	/**
	 * 新增输入类型信息
	 * @param inputType 输入类型实体对象
	 */
	public boolean insertInputType(InputType inputType) throws ManagerException;
	/**
	 * 修改输入类型信息
	 * @param inputType 输入类型实体对象
	 */
	public boolean updateInputType(InputType inputType) throws ManagerException;
	/**
	 * 删除输入类型信息
	 * @param inputType 输入类型实体对象
	 * @return int 0:全部删除成功; 1:部分删除成功; 2:全部不能被删除
	 */
	public int deleteInputType(String[] inputTypeIds) throws ManagerException;
	/**
	 * 获取所有输入类型列表
	 * @return list<InputType>
	 */
	public List getInputTypeList() throws ManagerException;
	
	/**
	 * 根据字典类型ID,获取与之关联的输入类型列表
	 * @return list<InputType>
	 */
	public List getInputTypeList(String dicttypeId) throws ManagerException;
	
	/**
	 * 数据库字段columnName是否已经被类型dicttypeId做为附加字段使用了
	 * @param dicttypeId
	 * @param columnName
	 * @return 
	 * InputTypeManager.java
	 * @author: ge.tao
	 */
	public boolean isColumnUsedByDictType(String dicttypeId,String columnName);
	
	/**
	 * 根据输入类型ID,获取输入类型对象信息
	 * @param id
	 * @return 
	 * InputTypeManager.java
	 * @author: ge.tao
	 */
	public InputType getInputTypeInfoById(String id);
}
