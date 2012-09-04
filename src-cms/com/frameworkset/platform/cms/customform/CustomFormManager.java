package com.frameworkset.platform.cms.customform;

import java.util.List;


/**
 * 自定义表单接口
 * @author jxw
 *
 */

public interface CustomFormManager extends java.io.Serializable
{
	/**
	 * 设置文档的各项自定义表单
	 * @param TARGET_ID
	 * @param TARGET_TYPE
	 * @param FORM_TYPE
	 * @param FILE_NAME
	 * @return boolean
	 * @throws CustomFormManagerException
	 */
	public boolean setCustomForm(String targetId,String targetType,String formType,String fileName) throws CustomFormManagerException;
	/**
	 * 设置文档的各项自定义表单
	 * @param <CustomForm>List
	 * @return boolean
	 * @throws CustomFormManagerException
	 */
	public boolean addCustomForms(String id,List customForms) throws CustomFormManagerException;
	/**
	 * update设置文档的各项自定义表单
	 * @param TARGET_ID
	 * @param TARGET_TYPE
	 * @param FORM_TYPE
	 * @param FILE_NAME
	 * @return boolean
	 * @throws CustomFormManagerException
	 */
	public boolean updateCustomFormSet(String targetId,String targetType,String formType,String fileName) throws CustomFormManagerException;
	/**
	 * get设置文档的自定义表单的filename
	 * @param TARGET_ID
	 * @param TARGET_TYPE
	 * @param FORM_TYPE
	 * @param FILE_NAME
	 * @return String
	 * @throws CustomFormManagerException
	 */
	public String getCustomFormFilename(String targetId,String targetType,String formType) throws CustomFormManagerException;
	/**
	 * 新增一项扩展字段前
	 * 判断是否存在字段名字相同的情况
	 * @param String fieldName
	 * @return boolean
	 * @throws CustomFormManagerException
	 */
	public boolean hasSameFieldName(String fieldName) throws CustomFormManagerException;
	/**
	 * 新增一项扩展字段
	 * @param <DocExtField>docExtField
	 * @return boolean
	 * @throws CustomFormManagerException
	 */
	public boolean addDocExtField(DocExtField docExtField) throws CustomFormManagerException;
	/**
	 * 从所有扩展字段中选择一些作为自己的扩展字段，是一种引用关系
	 * @param String[] ids
	 * @param String type
	 * 		1：站点
	 * 		2：频道
	 * @param String id 
	 * 		站点或频道的id
	 * @throws CustomFormManagerException
	 */
	public boolean addDocExtFields(String[] ids,String type,String id) throws CustomFormManagerException;
	/**
	 * 删除扩展字段（在维护界面上删）
	 * @param String[] ids
	 * @return boolean
	 * @throws CustomFormManagerException
	 */
	public boolean delDocExtField(String[] ids) throws CustomFormManagerException;
	/**
	 * 删除具体的站点或频道的扩展字段
	 * @param String[] ids
	 * @param String type
	 * 		1：站点
	 * 		2：频道
	 * @param String id 
	 * 		站点或频道的id
	 * @return boolean
	 * @throws CustomFormManagerException
	 */
	public boolean delDocExtFieldOfSiteOrChl(String[] ids,String type,String id) throws CustomFormManagerException;
	/**
	 * get所有的扩展字段列表
	 * 
	 * @return java.util.List
	 * @throws CustomFormManagerException
	 */
	public List getDocExtFieldList() throws CustomFormManagerException;
	/**
	 * get具体的站点或频道的扩展字段列表
	 * @param String id 
	 * 		站点或频道的id
	 * @param String type
	 * 		1：站点
	 * 		2：频道
	 * @return java.util.List
	 * @throws CustomFormManagerException
	 */
	public List getDocExtFieldOfSiteOrChlList(String id,String type) throws CustomFormManagerException;
	/**
	 * 保存文档的扩展字段的内容
	 * @param String docid 
	 * 		文档的id
	 * @param String[] ids
	 * 		该文档所有的扩展字段
	 * @param String[] values
	 * 		相应的扩展字段内容
	 * @return boolean
	 * @throws CustomFormManagerException
	 */
	public boolean saveDocExtFieldValues(String docid,String[] ids,String[] values, String[] types) throws CustomFormManagerException;
	/**
	 * 根据扩展字段ID取得其内容
	 * @param String id 
	 * 		扩展字段id
	 * @return java.util.List
	 * @throws CustomFormManagerException
	 */
	public DocExtField getExtFieldBy(String id) throws CustomFormManagerException;
	/**
	 * 修改一项扩展字段
	 * @param <DocExtField>docExtField
	 * @return boolean
	 * @throws CustomFormManagerException
	 */
	public boolean updateDocExtField(DocExtField docExtField) throws CustomFormManagerException;

}
