package com.frameworkset.platform.dictionary;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.spi.Provider;

import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.common.poolman.sql.ColumnMetaData;
import com.frameworkset.dictionary.Data;
import com.frameworkset.dictionary.Item;
import com.frameworkset.util.ListInfo;

/**
 * 项目：SysMgrCore <br>
 * 描述：字典管理 <br>
 * 日期：Mar 28, 2006 <br>
 * 
 * @author 吴卫雄
 */
public interface DictManager extends Provider {
	
	/**
	 * 字典数据,缺省存放的'数据源'名称
	 */
	public static final String DEFAULT_DATA_DBNAME = "bspf";
	/**
	 * 字典数据,缺省存放的'数据库表'名称
	 */
	public static final String DEFAULT_DATA_TABLENAME = "TD_SM_DICTDATA";
	
	/**
	 * 字典数据,缺省存放'数据名称'的字段
	 */
	public static final String DEFAULT_DATA_NAMEFIELD = "DICTDATA_NAME";
	
	/**
	 * 字典数据,缺省存放的'数据值'的字段
	 */
	public static final String DEFAULT_DATA_VALUEFIELD = "DICTDATA_VALUE";
	
	/**
	 * 字典数据,缺省存放的'排序字段'的字段
	 */
	public static final String DEFAULT_DATA_ORDERFIELD = "DICTDATA_ORDER";
	
	/**
	 * 字典数据,缺省存放的'类型ID'的字段
	 */
	public static final String DEFAULT_DATA_TYPEIDFIELD = "DICTTYPE_ID";
	
	/**
	 * 字典数据,是树形
	 */
	public static final int DICTDATA_IS_TREE = 1;
	
	/**
	 * 字典数据,不是树形
	 */
	public static final int DICTDATA_ISNT_TREE = 0;
	
	/**
	 * 更新字典类型数据表中的 字典类型字段
	 */
	public static final int UPDATE_DICTDATA_TYPEID = 1;
	
	public static final int DELETE_DICTDATA = 1;
	
	/**
	 * 所有字典类型
	 */
	public static final int ALL_DICTTYPE = -1;
	
	/**
	 * 基础字典类型
	 */
	public static final int BASE_DICTTYPE = 0;
	/**
	 * 通用字典类型 业务字典
	 */
	public static final int ALLREAD_BUSINESS_DICTTYPE = 1;
	/**
	 * 授权字典类型 业务字典
	 */
	public static final int PARTREAD_BUSINESS_DICTTYPE = 2;
	
	/**
	 * 所有业务字典
	 */
	public static final int BUSINESS_DICTTYPE = 3;
	
	/**
	 * 不维护数据,只授权的业务字典
	 */
	public static final int BUSINESS_DICTTYPE_POWERONLY = 4;
	
	/**
	 * 不维护数据,只维护常用的业务字典
	 */
	public static final int BUSINESS_DICTTYPE_USUALONLY = 5;
	
	/**
	 * 维护附加字段的字典
	 */
	public static final int ATTACH_DICTTYPE = 6;
	
	/**
	 * 权限字典 包括采集数据的 2 和不采集数据的 4
	 */
	public static final int POWER_DICTTYPE = 7;
	
	
	/**
	 * 字典数据项无效,已经停用了
	 */
	public static final int DICTDATA_INVALIDATE = 0;
	
	/**
	 * 字典数据项有效,还在使用
	 */
	public static final int DICTDATA_VALIDATE = 1;
	
	/**
	 * 该数据库字段可供字典类型选择
	 */
	public static final int COLUMN_AVIALABLE = 0;
	
	/**
	 * 该数据库字段被字典类型的当前属性选中
	 */
	public static final int COLUMN_SELECTED = 1;
	/**
	 * 该数据库字段被字典类型的基础属性选用了
	 */
	public static final int COLUMN_USED = 2;
	
	/**
	 * 该数据库字段被字典类型的高级字段属性选中了
	 */
	public static final int COLUMN_ADVANCE_USED = 4;
	/**
	 * 全部删除失败
	 */
	public static final int ALL_DELETE_FAILD = 0;
	/**
	 * 全部删除成功
	 */
	public static final int ALL_DELETE_SUCCESS = 1;
	/**
	 * 部分删除成功
	 */
	public static final int PART_DELETE_SUCCESS = 2;
	
	/**
	 * 数据库表没被字典类型使用
	 */
	public static final int DICTTYPE_USE_TABLE_FREE = 0;
	
	/**
	 * 数据库表被一个字典类型独占
	 */
	public static final int DICTTYPE_USE_TALBE_SINGLE = 1;
	/**
	 * 字典类型被多个字典类型共用
	 */
	public static final int DICTTYPE_USE_TABLE_SHARE = 2;
	
	/**
	 * 自动生成
	 */
	public static final int KEY_CREATE_TYPE = 1;
	
	public static final String ISRESCURE_FLAG = "1";

//	public static final String ASSOCIATED_DICTDATASET = "dictdataSet";
//  
	/**
	 * 初始化上下文
	 */
	public void init(HttpServletRequest request,HttpServletResponse response);
	/**
	 * 根据字典类型名称取字典类型
	 * 
	 * @param name
	 *            字典类型的名称，如：“性别”就是一个类型名称
	 * @return 字典类型名称所对应的 Dicttype 对象
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 */
	public Data getDicttypeByName(String name) throws ManagerException;
	
	
//
	/**
	 * 根据字典类型id 获取字典类型对象
	 * 
	 * @param id   字典类型在数据源中的ID值
	 * @return     字典类型ID所对应的 Dicttype 对象
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 */
	public Data getDicttypeById(String id) throws ManagerException;
//
//	/**
//	 * 根据字典数据ID取字典数据对象
//	 * 
//	 * @param name
//	 *            字典数据的名称，如：“性别”是一个字典类型的名称，而“男”或“女”则是该字典类型所对应的字典数据名称
//	 * @return 与字典数据名称对应的 Dictdata 对象
//	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
//	 */
//	public Item getDictdataById(String id) throws ManagerException;
//	
//	/**
//	 * 根据字典数据名称取字典数据对象
//	 * 
//	 * @param name
//	 *            字典数据的名称，如：“性别”是一个字典类型的名称，而“男”或“女”则是该字典类型所对应的字典数据名称
//	 * @return 与字典数据名称对应的 Dictdata 对象
//	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
//	 */
//	public Item getDictdataByName(String name) throws ManagerException;
//
//	/**
//	 * 根据字典数据值取字典字典数据对象
//	 * 
//	 * @param value
//	 *            字典数据的值，如：“男”＝“0”（或其它值），“0”则是字典数据的值。
//	 * @return 与 value 所对应的 Dictdata 对象
//	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
//	 */
//	public Item getDictdataByValue(String value) throws ManagerException;
//
//	/**
//	 * 存储字典类型对象
//	 * 
//	 * @param dicttype
//	 *            需要存储到数据源中的字典类型对象实体，当传入该对象时应尽可能保证该对象的完整性即它的所有属性都有值
//	 * @return 存储成功返回 true 否则返回 false
//	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
//	 */
//	public boolean storeDicttype(Dicttype dicttype) throws ManagerException;
//
//	/**
//	 * 存储字典数据对象 <br>
//	 * 使用方法：<br>
//	 * DictManager dictManager = SecurityDatabase.getDictManager(); Dictdata da =
//	 * new Dictdata(); Dicttype dt = new Dicttype();
//	 * dt.setDicttypeId(actionForm.getDictTypeId());
//	 * da.setDictdataName(actionForm.getDictDataName());
//	 * da.setDictdataValue(actionForm.getDictDataValue());
//	 * da.setDicttypeId(actionForm.getDictTypeId()); da.setDicttype(dt);
//	 * dictManager.storeDictdata(da);
//	 * 
//	 * @param dictdata
//	 *            需要存储到数据源中的字典数据对象，当传入该对象时应尽可能保证该对象的完整性即它的所有属性都有值
//	 * @return 存储成功返回 true 否则返回 false
//	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
//	 */
//	public boolean storeDictdata(Dictdata dictdata) throws ManagerException;
//
//	/**
//	 * 装载 Dicttype 对象实例所关联的其它对象到它的集合属性中，如：装载与当前字典类型有关的 Dictdata 对象到 dictdataSet
//	 * 集合中。
//	 * 
//	 * @param dicttypeId
//	 *            字典类型的ID
//	 * @param associated
//	 *            相关的集合名称，可以直接通过常量 DictManager.ASSOCIATED_XXX
//	 *            来指定，如：DictManager.ASSOCIATED_DICTDATASET
//	 * @return 装入成功后则返回一个包含了其它对象集合（如：dictdataSet）的字典类型对象（Dicttype）
//	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
//	 */
//	public Dicttype loadAssociatedSet(String dicttypeId, String associated)
//			throws ManagerException;
//
	/**
	 * 查看指定字典类型(通过类型ID)是否包含子字典类型
	 * 
	 * @param dicttypeId
	 *            需要检查是否包含子字典类型的字典类型对象，当传入该对象时应尽可能保证该对象的完整性即它的所有属性都有值
	 * @return 如果当前字典类型包含了子字典类型则返回 true 否则返回 false
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 */
	public boolean isContainChildDicttype(String dicttypeId)throws ManagerException;
	
	/**
	 * 查看指定字典类型(通过类型ID)是否包含业务子字典类型
	 * 
	 * @param dicttypeId
	 *            需要检查是否包含子字典类型的字典类型对象，当传入该对象时应尽可能保证该对象的完整性即它的所有属性都有值
	 * @return 如果当前字典类型包含了子字典类型则返回 true 否则返回 false
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 */
	public boolean isContainChildBusinessDicttype(String dicttypeId)throws ManagerException;
	
	/**
	 * 查看指定组合ID(ids 组合ID,字典类型ID:数据项的ID)是否包含字典数据,或是包含子字典数据
	 * @param ids 字典类型ID:数据项的ID:数据项的值 如: 123:45 根节点 123:root:root
	 *            需要检查是否包含子字典类型的字典类型对象，当传入该对象时应尽可能保证该对象的完整性即它的所有属性都有值
	 * @return 如果当前字典类型包含了子字典类型则返回 true 否则返回 false
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 */
	public boolean isContainDictdata(String ids)throws ManagerException;

	/**
	 * 根据字典类型ID,获得指定字典类型的子字典类型列表
	 * 直接子类型
	 * @param dicttypeId
	 *            需要取得子字典类型的字典类型对象，当传入该对象时应尽可能保证该对象的完整性即它的所有属性都有值
	 * @return dicttype 的子字典类型列表 当dicttypeId='0' 获取一级类型
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 */
	public List getChildDicttypeList(String dicttypeId) throws ManagerException;
		
	/**
	 * 根据字典类型ID,获得指定字典类型的"基本"子字典类型列表
	 * 直接子类型
	 * @param dicttypeId
	 *            需要取得子字典类型的字典类型对象，当传入该对象时应尽可能保证该对象的完整性即它的所有属性都有值
	 * @return dicttype 的子字典类型列表 当dicttypeId='0' 获取一级类型
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 */
	public List getBaseChildDicttypeList(String dicttypeId) throws ManagerException;
	
	/**
	 * 根据字典类型ID,获得指定字典类型的"所有人可见"子字典类型列表
	 * 直接子类型
	 * @param dicttypeId
	 *            需要取得子字典类型的字典类型对象，当传入该对象时应尽可能保证该对象的完整性即它的所有属性都有值
	 * @return dicttype 的子字典类型列表 当dicttypeId='0' 获取一级类型
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 */
	public List getAllReadChildDicttypeList(String dicttypeId) throws ManagerException;
	
	/**
	 * 根据字典类型ID,获得指定字典类型的"授权可见"子字典类型列表
	 * 直接子类型
	 * @param dicttypeId
	 *            需要取得子字典类型的字典类型对象，当传入该对象时应尽可能保证该对象的完整性即它的所有属性都有值
	 * @return dicttype 的子字典类型列表 当dicttypeId='0' 获取一级类型
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 */
	public List getPartReadChildDicttypeList(String dicttypeId) throws ManagerException;
	
	/**
	 * 根据字典类型ID,获得指定字典类型的部分(要设置权限的)子字典类型列表
	 * 直接子类型
	 * @param dicttypeId
	 *            需要取得子字典类型的字典类型对象，当传入该对象时应尽可能保证该对象的完整性即它的所有属性都有值
	 * @param typeId 不同的类型,获取不同的字典列表
	 * @return dicttype 的子字典类型列表 当dicttypeId='0' 获取一级类型(要设置权限的)
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 */
	public List getPartChildDicttypeList(String dicttypeId,int typeId) throws ManagerException;
	
	/**
	 * 根据字典类型ID,获得指定字典类型的业务(所有可见,要设置权限的)子字典类型列表
	 * 直接子类型
	 * @param dicttypeId
	 *            需要取得子字典类型的字典类型对象，当传入该对象时应尽可能保证该对象的完整性即它的所有属性都有值
	 * @param typeId 不同的类型,获取不同的字典列表
	 * @return dicttype 的子字典类型列表 当dicttypeId='0' 获取一级类型(要设置权限的)
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 */
	public List getBusinessChildDicttypeList(String dicttypeId) throws ManagerException;
	
	/**
	 * 根据字典类型ID,获得所有需要维护 常用编码关系的字典类型 
	 * dicttype_type != 0  
	 * @param dicttypeId dicttype 的子字典类型列表 当dicttypeId='0' 获取一级类型(要设置权限的)
	 * @return
	 * @throws ManagerException 
	 * DictManager.java
	 * @author: ge.tao
	 */
	public List getUsualRelationDicttypeList(String dicttypeId) throws ManagerException;
	
	/**
	 * 根据字典类型ID:数据项的ID:数据项的值, 如123:45 根节点 123:root:root
	 * @param ids (字典类型ID:数据项的ID) 组合ID
	 *            需要取得子字典类型的字典类型对象，当传入该对象时应尽可能保证该对象的完整性即它的所有属性都有值
	 * @return List<com.frameworkset.dictionary.Item>
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 */
	public List getChildDictdataList(String ids) throws ManagerException;
	
	
    
	/**
	 * 根据字典类型ID,获得指定字典类型的子字典类型列表
	 * 递归子类型
	 * @param dicttypeId
	 * @return
	 * @throws ManagerException 
	 * DictManager.java
	 * @author: ge.tao
	 */
	public List getRecursionChildDicttypeList(List dicttypes,String dicttypeId) throws ManagerException;

	/**
	 * 根据字典数据id获得,该字典数据的子的数据  
	 * 不翻页
	 * @param dicttypeid 字典类型ID
	 * @param dictdataid 字典数据ID
	 * @return 由参数 dicttype 所指定的字典类型的字典数据列表
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 */
	public List getChildDictdataListByDataId(String dicttypeid ,String dictdataid) throws ManagerException;
	
	/**
	 * 根据字典数据id获得,该字典数据的子的数据  递归 
	 * 不翻页
	 * @param dicttypeid 字典类型ID
	 * @param dictdataid 字典数据ID
	 * @return 由参数 dicttype 所指定的字典类型的字典数据列表
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 */
	public List getRecursionChildDictdataListByDataId(String dicttypeid ,String dictdataid) throws ManagerException;
	
	/**
	 * 根据字典类型id获得字典的数据  
	 * 不翻页
	 * @param dicttype 需要取得字典数据列表的字典类型对象
	 * @return 由参数 dicttype 所指定的字典类型的字典数据列表
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 */
	public List getChildDictdataListByTypeId(String dicttypeid) throws ManagerException;
	
	/**
	 * 根据字典类型id获得字典的数据 
	 * 不翻页	
	 * @param dicttypeid 需要取得字典数据列表的字典类型对象
	 * @return 由参数 dicttype 所指定的字典类型的字典数据列表
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 */
	public List getDictdataList(String dicttypeid) throws ManagerException;
	
	/**
	 *  根据字典类型id,获得机构有(opcode:read/usual)权限的字典的数据 
	 * @param dicttypeid
	 * @param orgId
	 * @param opcode
	 * @return
	 * @throws ManagerException 
	 * DictManager.java
	 * @author: ge.tao
	 */
	public List getPowerDictdataList(String dicttypeid,String orgId,String opcode) throws ManagerException;
	
	/**
	 * 根据混合id获取该类型字典数据集合
	 * ids 是123:123:aa 字典类型ID:数据项ID:数据项名称 
	 * 不递归 翻页
	 * 判断是否有排序字段,是否有类型ID字段
	 */
	public ListInfo getDictdataList(String ids, int offset, int size) throws ManagerException ;
	
	/**
	 * 普通用户 
	 * 有权限看到的 授权字典数据项 翻页
	 * 
	 * 根据混合id获取该类型字典数据集合
	 * ids 是123:123:aa 字典类型ID:数据项ID:数据项名称 
	 * 不递归 翻页
	 * 判断是否有排序字段,是否有类型ID字段
	 * @param ids
	 * @param showdata 查询条件：显示数据
	 * @param realitydata 查询条件：真实数据
	 * @param occurOrg 查询条件：所属机构
	 * @param isaVailability 查询条件：是否有效
	 * @param offset
	 * @param size
	 * @return
	 * @throws ManagerException
	 */
	public ListInfo getNormalUserDictdataList(String ids,String showdata, String realitydata, 
			String occurOrg, String isaVailability, int offset, int size,String userId) throws ManagerException;
	
	/**
	 * 部门管理员 
	 * 有权限看到 授权字典数据项 翻页
	 * 
	 * 根据混合id获取该类型字典数据集合
	 * ids 是123:123:aa 字典类型ID:数据项ID:数据项名称 
	 * 不递归 翻页
	 * 判断是否有排序字段,是否有类型ID字段
	 * @param ids
	 * @param showdata 查询条件：显示数据
	 * @param realitydata 查询条件：真实数据
	 * @param occurOrg 查询条件：所属机构
	 * @param isaVailability 查询条件：是否有效
	 * @param offset
	 * @param size
	 * @return
	 * @throws ManagerException
	 */
	public ListInfo getOrgManagerDictdataList(String ids,String showdata, String realitydata, 
			String occurOrg, String isaVailability, int offset, int size,String userId) throws ManagerException;
	
	/**
	 *  
	 * 机构权限看到 授权字典数据项 翻页
	 * 
	 * 根据混合id获取该类型字典数据集合
	 * ids 是123:123:aa 字典类型ID:数据项ID:数据项名称 
	 * 不递归 翻页
	 * 判断是否有排序字段,是否有类型ID字段
	 * @param ids
	 * @param showdata 查询条件：显示数据
	 * @param realitydata 查询条件：真实数据
	 * @param occurOrg 查询条件：所属机构
	 * @param isaVailability 查询条件：是否有效
	 * @param offset
	 * @param size
	 * @return
	 * @throws ManagerException
	 */
	public ListInfo getOrgReadDictdataList(String ids,String showdata, String realitydata, 
			String occurOrg, String isaVailability, int offset, int size, String orgId) throws ManagerException;
	
	/**
	 * 
	 * 获取 字典数据项
	 * 
	 * 根据混合id获取该类型字典数据集合
	 * ids 是123:123:aa 字典类型ID:数据项ID:数据项名称 
	 * 不递归 翻页
	 * 判断是否有排序字段,是否有类型ID字段
	 * @param ids
	 * @param showdata 查询条件：显示数据
	 * @param realitydata 查询条件：真实数据
	 * @param occurOrg 查询条件：所属机构
	 * @param isaVailability 查询条件：是否有效
	 * @param offset
	 * @param size
	 * @return
	 * @throws ManagerException
	 */
	public ListInfo getDictdataList(String ids,String showdata, String realitydata, 
			String occurOrg, String isaVailability, int offset, int size) throws ManagerException;
	
	/**
	 * 增加高级字段条件查询  gao.tang 2008.05.04
	 * 获取 字典数据项
	 * 
	 * 根据混合id获取该类型字典数据集合
	 * ids 是123:123:aa 字典类型ID:数据项ID:数据项名称 
	 * 不递归 翻页
	 * 判断是否有排序字段,是否有类型ID字段
	 * @param ids
	 * @param showdata 查询条件：显示数据
	 * @param realitydata 查询条件：真实数据
	 * @param occurOrg 查询条件：所属机构
	 * @param isaVailability 查询条件：是否有效
	 * @param offset
	 * @param size
	 * @return
	 * @throws ManagerException
	 */
	public ListInfo getDictdataList(String ids,String showdata, String realitydata, 
			String occurOrg, String isaVailability,String attachFieldSql, int offset, int size) throws ManagerException;
//	
//	/**
//	 * 根据字典类型名称获得字典的数据 景峰添加
//	 * 
//	 * @param dicttype 需要取得字典数据列表的字典类型对象
//	 * @return 由参数 dicttype 所指定的字典类型的字典数据列表
//	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
//	 */
//	public List getDictdataListByName(String dicttypename) throws ManagerException;

//	
//	/**
//	 * 根据字典类型名称获得字典的数据 景峰添加
//	 * 
//	 * @param dicttype 需要取得字典数据列表的字典类型对象
//	 * @return 由参数 dicttype 所指定的字典类型的字典数据列表
//	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
//	 */
//	public ListInfo getDictdataListByName(String dicttypename,int offset,int size) throws ManagerException;
//
//	/**
//	 * 返回字典管理的分页配置管理 景峰添加
//	 * 
//	 * @return 字典管理的分页配置管理对象
//	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
//	 */
//	public PageConfig getPageConfig() throws ManagerException;
	
	/**
	 * 添加字典类型
	 * @param 要添加的字典类型类对象
	 * @return 不能为空的字段名称 col1,col2,.... 当为-1时,表示有重复(名字)记录
	 * @throws ManagerException
	 */
	public String addDicttype(Data dicttype)	throws ManagerException;
	
	/**
	 * 更新字典类型
	 * @param 要更新的字典类型类对象
	 * @return 更新成功与否
	 * @throws ManagerException
	 */
	public String updateDicttype(Data dicttype)	throws ManagerException;
	
	/**
	 * 根据字典类型id,
	 * 删除一个字典类型 字典类型的数据
	 * 递归删除字典类型的 子字典类型/子字典类型的数据
	 * 
	 * @param dicttypeId 需要删除的字典类型的ID
	 * @return 删除成功则返回 true 否则返回 false
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 */
	public boolean deletedicttype(String dicttypeId) throws ManagerException;	
	
	/**
	 * 根据字典类型对象,
	 * 删除一个字典类型 判断是否删除 字典类型的数据
	 * 递归删除字典类型的 判断是否删除 子字典类型/子字典类型的数据
	 * 
	 * @param dicttypeId 需要删除的字典类型的ID
	 * @return 删除成功则返回 true 否则返回 false
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 */
	public boolean deletedicttype(Data dicttype) throws ManagerException;
	
	/**
	 * 添加字典数据
	 * @param 要添加的字典数据类对象
	 * @return 0:成功; 1:已经存在; 2:类型不匹配
	 * @throws ManagerException
	 */
	public boolean addDictdata(Item dictdata)	throws ManagerException;
	
	/**
	 * 高级添加字典数据
	 * @param 要添加的字典数据类对象
	 * @param request
	 * @return boolean
	 * @throws ManagerException
	 */
	public boolean addAdvanceDictdata(Item dictdata,HttpServletRequest request)	throws ManagerException;
	
	/**
	 * 字典数据的高级(附加)字段的更新
	 * @param 要添加的字典数据类对象
	 * @param request
	 * @return boolean
	 * @throws ManagerException
	 */
	public boolean updateAdvanceDictdata(Item dictdata,HttpServletRequest request)	throws ManagerException;
	
	/**
	 * 更新字典数据
	 * @param 要更新的字典数据类对象
	 * @return 更新成功与否
	 * @throws ManagerException
	 */
	public boolean updateDictdata(Item dictdata)	throws ManagerException;		
	
	/**
	 * 根据字典类型,删除一个字典类型的具体数据	 
	 * @param dictdataId 需要删除的字典类型的ID
	 * @param dictdataValue 需要删除的字典数据的值
	 * @param dictdataName 需要删除的字典数据的名称 为了从缓存的itemsIdxByName中去掉
	 * @return 删除成功则返回 true 否则返回 false
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 */
	public boolean deletedictdata(String dicttypeId,String dictdataValue,String dictdataName,String primaryCondition) throws ManagerException;
	
	/**
	 * 根据字典类型,删除一个字典类型的具体数据	 
	 * @param dictdataId 需要删除的字典类型的ID
	 * @param String[] dictdataInfos<dictdataValue:dictdataName> 需要删除的字典数据的值
	 * @param dictdataValue 需要删除的字典数据的名称 为了从缓存的itemsIdxByName中去掉
	 * @return 删除成功则返回 true 否则返回 false
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 */
	public boolean deletedictdatas(String dicttypeId,String[] dictdataInfos) throws ManagerException;
	
	/**
	 * 根据字典类型,更新字典数据的顺序
	 * @param dicttypeId
	 * @param docIds
	 * @return boolean
	 * @throws ManagerException 
	 * DictManager.java
	 * @author: ge.tao
	 */
	public boolean updateArrangeDictOrderNo(String dicttypeId,String[] docIds) throws ManagerException;
	
	/**
	 * 根据用户ID,获取用户所在机构的指定税种的编码
	 * @param userId 用户ID
	 * @param dicttypeName 税种类型名称(字典类型名称) 如果dicttypeName=="" 取出所有的税种编码
	 * @return List<com.frameworkset.dictionary.Item>
	 * DataManager.java
	 * @author: ge.tao
	 */
	public List getTaxCodesByUserIdAndTypeName(String userId,String dicttypeName);
	
	/**
	 * 根据机构ID,获取机构的指定税种的编码
	 * @param orgId 机构ID
	 * @param dicttypeName 税种类型名称(字典类型名称) 如果dicttypeName=="" 取出所有的税种编码
	 * @return List<com.frameworkset.dictionary.Item>
	 * DataManager.java
	 * @author: ge.tao
	 */
	public List getTaxCodesByOrgIdAndTypeName(String orgId,String dicttypeName);
	
	/**
	 * 根据字典类型ID, 判读该字典的数据项是否树形
	 * @param dicttypeId
	 * @return boolean true:是树形;false:不是树形
	 * DictManager.java
	 * @author: ge.tao
	 */
	public boolean isTreeDictdata(String dicttypeId);
	
	/**
	 * 获取字典类型的附加(高级)字段列表 翻页
	 * @param dicttypeId
	 * @param offset
	 * @param maxPagesize
	 * @return ListInfo<List<>>
	 * DictManager.java
	 * @author: ge.tao
	 */
	public ListInfo getDictdataAttachFieldList(String dicttypeId,int offset,long maxPagesize);
	
	/**
	 * 获取字典类型的附加(高级)字段列表 不翻页
	 * 当count=-1时,取出全部的记录
	 * @param dicttypeId
	 * @param count
	 * @return ListInfo<List<>>
	 * DictManager.java
	 * @author: ge.tao
	 */
	public List getDictdataAttachFieldList(String dicttypeId,int count);
	
	/**
	 * 获取字典类型的所有附加(高级)字段列表,生成更新页面的时候
	 * 把区对应的值设置到input的字段中去.
	 * @param dicttypeId
	 * @return ListInfo<List<>>
	 * DictManager.java
	 * @author: ge.tao
	 */
	public List getAllDictdataAttachFieldList(String dicttypeId,String nameKey,String valueKey);
	
	/**
	 * 保存字典数据项的附加(高级)字段
	 * @param attNames 附加字段名称
	 * @param attLabels 附加字段说明
	 * @param attTypes 附加字段类型
	 * @param dicttypeId 数据项对应的字典类型ID
	 * DictManager.java
	 * @author: ge.tao
	 */
	public void storeDictdataAttachField(String[] attNames,String[] attLabels, String[] attTypes,String dicttypeId);
	
	/**
	 * 保存机构与编码之间的关系 先删除,后保存
	 * @param orgId  机构ID
	 * @param dicttypeId  字典类型ID  
	 * @param dictdataValue  字典数据Value数组
	 * DictManager.java
	 * @author: ge.tao
	 */
	public void storeOrgTaxcode(String orgId,String dicttypeId,String[] dictdataValue);
	
	/**
	 * 保存机构与编码之间的关系 传入选中和没被选中的 ID;
	 * @param orgId  机构ID
	 * @param dicttypeId  字典类型ID  
	 * @param dictdataValue  字典数据Value数组
	 * DictManager.java
	 * @author: ge.tao
	 */
	public void storeOrgTaxcode(String orgId,String dicttypeId,String[] selected_dictdataValue,
			String[] unselected_dictdataValue) throws ManagerException;
	
	/**
	 * 根据机构ID和字典类型ID,获取该机构在该字典类型下绑定的字典数据列表	 
	 * @param orgId 机构ID
	 * @param dicttypeId 字典类型ID
	 * @return List<String dictdataValue> 
	 * DictManager.java
	 * @author: ge.tao
	 */
	public List getDictdatasByOrgIdAndTypeId(String orgId,String dicttypeId);
	
	
    
	/**
	 * 根据数据库字段的类型,获取当前输入字段的校验类型
	 * @param obj
	 * @return 校验类型在"../user/validateForm.js中定义 有:
	 * DictManager.java
	 * @author: ge.tao
	 */
	public String getValidatorTypeByColumnMetaData(ColumnMetaData obj);
	
	/**
	 * 根据数据库字段的类型,获取当前输入字段的校验类型
	 * @param obj
	 * @param nullable 是否为空
	 * @return 校验类型在"../user/validateForm.js中定义 
	 * DictManager.java
	 * @author: ge.tao
	 */
	public String getValidatorTypeByColumnMetaData(ColumnMetaData obj,String nullable);
	
	/**
	 * 指定的字典类型是否已有字典数据
	 * true:有;false:没有
	 * @param dicttypeId
	 * @return 
	 * DictManager.java
	 * @author: ge.tao
	 */
	public boolean dictTypeHasDatas(String dicttypeId);
	
	/**
	 * 保存机构与编码之间的关系 先删除,后保存
	 * @param orgId  机构ID
	 * @param dicttypeId  字典类型ID  
	 * @param dictdataValue  字典数据Value数组
	 * DictManager.java
	 * @author: ge.tao
	 */
	public void storeUsualOrgTaxcode(String orgId, String dicttypeId, String[] dictdataValues);
	
	/**
	 * 保存机构与编码之间的关系 传入选中的和没被选中的ID
	 * @param orgId  机构ID
	 * @param dicttypeId  字典类型ID  
	 * @param dictdataValue  字典数据Value数组
	 * DictManager.java
	 * @author: ge.tao
	 */
	public void storeUsualOrgTaxcode(String orgId, String dicttypeId, String[] selected_dictdataValues,
			String[] unselected_dictdataValues) throws ManagerException;
	
	/**
	 * @author gao.tang 2007.12.03
	 * @param orgId
	 * @param resId
	 * @return
	 */
	public boolean getOrgisReadRes(String orgId, String resId, String value);
	
	/**
	 * usual
	 * @author gao.tang 2007.12.03
	 * @param orgId
	 * @param dicttypeId
	 * @return
	 */
	public List getDictdatasByOrgIdAndTypeId2(String orgId,String dicttypeId) ;
	
	/**
	 * read列表
	 * @author gao.tang 2007.12.03
	 * @param orgId
	 * @param dicttypeId
	 * @return
	 */
	public List getReadDictdatasByOrgIdAndTypeId(String orgId,String dicttypeId) ;
	
	/**
	 * 根据字典类型ID,数据项ID和操作码,获取符合条件的机构列表 包括本机构 创建数据项的机构.
	 * @param dicttypeId
	 * @param dictdataValue
	 * @param opcode
	 * @return List<organizationId>
	 * DictManager.java
	 * @author: ge.tao
	 */
	public List getDictDataOpcodeOrgs(String dicttypeId,String dictdataValue,String opcode);
	
	/**
	 * 根据字典类型ID,数据项ID和操作码,获取符合条件的机构列表, 
	 * 不包括本机构 创建数据项的机构.
	 * @param dicttypeId
	 * @param dictdataValue
	 * @param opcode
	 * @return 
	 * DictManager.java
	 * @author: ge.tao
	 */
	public List getExpSelfDictDataOpcodeOrgs( String dicttypeId, String dictdataValue,String opcode);
	
	/**
	 * 得到字典类型种类
	 * @param dicttypeId
	 * @return
	 */
	public int getDicttype_type(String dicttypeId);
	
	/**
	 * 根据机构ID和字典dicttypeid的到td_sm_taxcode_organization中的data_value值
	 * @param orgId
	 * @param dicttypeid
	 * @return
	 */
	public String[] getOrgDictdataValue(String orgId, String dicttypeid);
	
	/**
	 * 新增
	 * 新增,要过滤基础字段, 因为基础字段不能作为附加字段来配置.
	 * @param dictatt
	 * @return 
	 * DictManager.java
	 * @author: ge.tao
	 */
	public boolean storeDictdataAttachField(DictAttachField dictatt);
	
	/**
	 * 修改/修改字典附加字段
	 * 有数据, 对应的数据库字段不允许被修改
	 * @param dictatt
	 * @return 
	 * DictManager.java
	 * @author: ge.tao
	 */
	public boolean updateDictdataAttachField(DictAttachField dictatt);
	
		
	/**
	 * 删除字典附加字段
	 * 有数据不允许被删除
	 * @param String dicttypeId 
	 * @param String[] tableColumns
	 * @return  int
	 * DictManager.java
	 * @author: ge.tao
	 */
	public int deleteDictdataAttachField(String dicttypeId, String[] tableColumns);
	
	/**
	 * 当前数据库字段的使用情况
	 * 0:可用
	 * 1:已经被选中
	 * 2:已经被使用
	 * @param dicttype
	 * @param columnName
	 * @param selectValue
	 * @return 
	 * DictManager.java
	 * @author: ge.tao
	 */
	public int columnUseStatue(Data dicttype,String columnName, String selectValue);
	
	/**
	 * 根据类型ID和数据库表字段,获取字典附加字段对象信息
	 * @param dicttypeId
	 * @param tableColumn
	 * @return 
	 * DictManager.java
	 * @author: ge.tao
	 */
	public DictAttachField getDictAttachFieldInfo(String dicttypeId,String tableColumn);
	
	/**
	 * 更新字典排序
	 * @param dicttypeId
	 * @param docid
	 * @return
	 * @throws ManagerException
	 */
	public boolean updateDictArr(String dicttypeId, String[] docid) throws ManagerException ;
		
	/**
	 * 根据数据源名称和数据库表名称,判断数据库表被字典类型的使用情况
	 * 0:数据库表没有被任何字典类型使用
	 * 1:数据库表被字典类型独占(字典数据没有指定类型ID)
	 * 2:数据库表被多个字典类型公用(字典数据指定了类型ID)
	 * @param dbName
	 * @param tableName
	 * @return 
	 * DictManager.java
	 * @author: ge.tao
	 */
	public int getDictTypeUseTableStates(String dbName,String tableName);
	
	/**
	 * 更新树形字典列表排序
	 * @param dicttypeId
	 * @param dictdataValue
	 * @param docid
	 * @return
	 * @throws ManagerException
	 */
	public boolean updateDictArrTree(String dicttypeId, String dictdataValue, String[] docid) throws ManagerException ;

	/**
	 * 树形的 启用，停用字典项 
	 * @param dicttypeId
	 * @param dictdataValue
	 * @param docid
	 * @param flag
	 * @param isRescure 是否递归启用/停用 子数据项
	 * @return
	 * @throws ManagerException
	 */
	public boolean changeState(String dicttypeId, String dictdataValue, String[] docid, String flag, String isRescure) throws ManagerException ;
	
	/**
	 * 根据数据源和数据库表名,获取数据库表不能为空的字段列表
	 * @param dbName
	 * @param tableName
	 * @return List<ColumnMetaData>
	 * DictManager.java
	 * @author: ge.tao
	 */
	public List getUnableNullColumns(String dbName,String tableName);
	
	/**
	 * 根据数据源和数据库表名,获取数据库表不能为空的字段列的串 col1,col2...
	 * @param dicttype
	 * @return col1,col2...
	 * DictManager.java
	 * @author: ge.tao
	 */
	public String getUnableNullColumnNames(Data dicttype);
	
	/**
	 * 得到高级字段配置对应的数据库字段
	 * @param dicttypeId
	 * @return
	 */
	public String getColumnName(String dicttypeId);
	
	/**
	 * 列表启用，停用字典项
	 * @param dicttypeId
	 * @param docid
	 * @param flag
	 * @return
	 * @throws ManagerException
	 */
	public boolean changeState(String dicttypeId, String[] docid, String flag) throws ManagerException ;
	
	/**
	 * 得附字段值
	 * @param dicttypeId
	 * @param name
	 * @param value
	 * @param attachField
	 * @return
	 */
	public String getAttachValue(String dicttypeId, String name, String value, String attachField)  throws ManagerException;
	
	/**
	 * 得附字段值
	 * @param dicttypeId
	 * @param name
	 * @param value
	 * @param attachField
	 * @param primarykeys
	 * @return
	 * @throws ManagerException 
	 */
	public String getAttachValue(String dicttypeId, String name, String value, String attachField,Map primarykeys) throws ManagerException;
	
	

	/**
	 * 根据字典类型,删除一个字典类型的具体数据
	 * 包含递归删除
	 * 删除编码机构关系	 
	 * @param dictdataId 需要删除的字典类型的ID
	 * @param dictdataValue 需要删除的字典数据的值
	 * @param dictdataValue 需要删除的字典数据的名称 为了从缓存的itemsIdxByName中去掉
	 * @return 删除成功则返回 true 否则返回 false
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 * add by ge.tao
	 * 2007-11-12
	 */
	public boolean deletedictdataByName(String dicttypeId,String dictdataName,String primaryCondition) throws ManagerException ;

	
	/**
	 * 根据字典类型,删除一个字典类型的具体数据
	 * 包含递归删除
	 * 删除编码机构关系	 
	 * @param dictdataId 需要删除的字典类型的ID
	 * @param dictdataValue 需要删除的字典数据的值
	 * @param dictdataValue 需要删除的字典数据的名称 为了从缓存的itemsIdxByName中去掉
	 * @return 删除成功则返回 true 否则返回 false
	 * @throws ManagerException 如果在执行该方法的过程中遇到问题将抛出 ManagerException 异常
	 * add by ge.tao
	 * 2007-11-12
	 */
	public boolean deletedictdataByValue(String dicttypeId,String dictdataValue,String primaryCondition) throws ManagerException ;
	
	
	/**
	 * 修改树形数据项
	 * @param dictdata 得到新的数据项
	 * @param request 
	 * @param keyName 修改条件数据库保存列的"名称字段"值
	 * @param keyValue 修改条件数据库保存列的"值字段"值
	 * @param parentId 父节点id,id不为空时用keyName与keyValue来做修改条件，否则加上parentId作为条件
	 * @return boolean 修改成功状态
	 * @throws ManagerException
	 */
	public boolean updateAdvanceDictdata(Item dictdata, HttpServletRequest request, 
			String keyName, String keyValue, String parentId)  throws ManagerException ;
	
	/**
	 * 机构是否有对字典数据项的使用权限
	 * @param orgId
	 * @param dicttypeId
	 * @param dictdataName
	 * @param dictdataValue
	 * @return 
	 * DictManager.java
	 * @author: ge.tao
	 */
	public boolean orgHasDictdataReadPower(String orgId,String dicttypeId,String dictdataValue);
	
	/**
	 * 机构是否有对字典数据项的常用维护权限
	 * @param orgId
	 * @param dicttypeId
	 * @param dictdataName
	 * @param dictdataValue
	 * @return 
	 * DictManager.java
	 * @author: ge.tao
	 */
	public boolean orgHasDictdataUsualPower(String orgId,String dicttypeId,String dictdataValue);
	
//	/**
//	 * 机构编码关系的通存
//	 * @param orgId
//	 * @param dicttypeId
//	 * @param opcode 
//	 * DictManager.java
//	 * @author: ge.tao
//	 */
//	public void storeAllOrgTaxCode(String orgId,String dicttypeId,String opcode);
//	
//	/**
//	 * 机构编码关系通删
//	 * @param orgId
//	 * @param dicttypeId
//	 * @param opcode 
//	 * DictManager.java
//	 * @author: ge.tao
//	 */
//	public void deleteAllOrgTaxCode(String orgId,String dicttypeId,String opcode);
	
	/**
	 *  查看指定字典类型(通过类型ID)是否包含 维护附件字段的(0 1 2) 子字典类型
	 *  @param dicttypeId
	 *  @return List<>
	 */
	public boolean isContainAttachDictType(String dicttypeId);
	
	/**
	 * 根据字典类型ID,获得指定字典类型的业务(要维护附件字段的 0 1 2)子字典类型列表
	 * @param dicttypeId
	 * @return List<>
	 * DictManager.java
	 * @author: ge.tao
	 */
	public List getAttachDictTypeList(String dicttypeId);
	
	/**
	 * 获取字典数据的属性值
	 * @param dicttypeId
	 * @param dictdataValue
	 * @param dictdataName
	 * @param property
	 * @return 
	 * DictManager.java
	 * @author: ge.tao
	 */
	public String getDictDataPropertyValue(String dicttypeId, String dictdataValue, String dictdataName, String property);
	
	/* (non-Javadoc)
	 * public String getDictDataPropertyValue(String dicttypeId, String dictdataValue, String dictdataName, String property)
	 * 重载上面方法，当name与value为空时以上方法存在问题
	 * @see com.frameworkset.platform.dictionary.DictManager#getDictDataPropertyValue(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * 获取字典数据的属性值
	 */
	public String getDictDataPropertyValue(String dicttypeId, String dictdataValue, String dictdataName, String property,String primaryCondition);
	
	/**
	 * 验证字典数据的属性的唯一性
	 * @param dicttypeId
	 * @param property
	 * @param value
	 * @return 
	 * DictManager.java
	 * @author: ge.tao
	 */
	public String checkDictDataPropertyValueUnique(String dicttypeId,  String property, String value);
	
	/**
	 * 机构授权编码维护列表。分为已设置项列表与未设置项列表 gao.tang 2008.1.4
	 * @param id 字典ID
 	 * @param orgId 根据机构判断已设置项与未设置项
	 * @param showdata 数据项名称
	 * @param identifier 已设置项与未设置项标识：值为sealed时标识为未设置项，值为selected时标识为已设置项
	 * @param offset
	 * @param size
	 * @return
	 * @throws ManagerException
	 */
	public ListInfo getDictdataList(String id, String orgId, String showdata,String identifier, int offset, int size) throws ManagerException ;
	
	/**
	 * 部门管理员登陆能看见的机构授权编码维护列表。分为已设置项列表与未设置项列表 gao.tang 2008.1.4
	 * @param id 字典ID
 	 * @param orgId 根据机构判断已设置项与未设置项
	 * @param showdata 数据项名称
	 * @param identifier 已设置项与未设置项标识：值为sealed时标识为未设置项，值为selected时标识为已设置项
	 * @param offset
	 * @param size
	 * @param userId 用户ID，只有部门管理员才能维护字典数据
	 * @return
	 * @throws ManagerException
	 */
	public ListInfo getUserDictdataList(String id, String orgId, String showdata,String identifier, int offset, int size, String userId) throws ManagerException ;
	
	/**
	 * 列表未设置项选中保存操作 gao.tang 2008.1.5
	 * @param orgId 授予机构ID
	 * @param dicttypeId 字典类型ID
	 * @param dictdataValues 字典数据项的名称与值
	 */
	public void storeReadOrgTaxcode(String orgId, String dicttypeId, String[] dictdataValues );
	
	/**
	 * 得到授权编码的全部的未设置项和全部的已设置项
	 * @param id	字典ID
	 * @param orgId	机构ID
	 * @param identifier	已设置项与未设置项标识：值为sealed时标识为未设置项，值为selected时标识为已设置项	
	 * @param isAdmin	用户是否拥有超级管理员角色 
	 * @param userId	isAdmin为false，userId不为空
	 * @return
	 * @throws ManagerException 
	 */
	public String[] getDictdataValues(String id, String orgId, String identifier, boolean isAdmin, String userId) throws ManagerException;
	
	/**
	 * 列表已设置项选中删除操作 gao.tang 2008.1.5
	 * @param orgId 删除机构ID
	 * @param dicttypeId 字典类型ID
	 * @param dictdataValues 字典数据项的名称与值
	 */
	public void deleteReadOrgTaxcode(String orgId, String dicttypeId, String[] dictdataValues );
	
	/**
	 * 得到常用编码的全部的未设置项和全部的已设置项
	 * @param dicttypeId
	 * @param showdata
	 * @param orgId
	 * @param identifier 已设置项与未设置项标识：值为sealed时标识为未设置项，值为selected时标识为已设置项	
	 * @param offset
	 * @param size
	 * @return
	 * @throws ManagerException
	 */
	public ListInfo getUsualDictdataList(String dicttypeId, String showdata, 
			String orgId, String identifier, int offset, int size) throws ManagerException;
	
	/**
	 * 常用编码列表未设置项选中保存操作 gao.tang 2008.1.5
	 * @param orgId 授予机构ID
	 * @param dicttypeId 字典类型ID
	 * @param dictdataValues 字典数据项的名称与值
	 */
	public void store_UsualOrgTaxcode(String orgId, String dicttypeId, String[] dictdataValues );
	
	/**
	 * 得到常用编码的全部的未设置项和全部的已设置项
	 * @param dicttypeId
	 * @param showdata
	 * @param orgId
	 * @param identifier 已设置项与未设置项标识：值为sealed时标识为未设置项，值为selected时标识为已设置项	
	 * @param offset
	 * @param size
	 * @return
	 * @throws ManagerException
	 */
	public String[] getUsualDictdata(String dicttypeId, 
			String orgId, String identifier) throws ManagerException;
	
	/**
	 * 常用编码列表已设置项选中删除操作 gao.tang 2008.1.5
	 * @param orgId 删除机构ID
	 * @param dicttypeId 字典类型ID
	 * @param dictdataValues 字典数据项的名称与值
	 */
	public void delete_UsualOrgTaxcode(String orgId, String dicttypeId, String[] dictdataValues );
	
	/**
	 * 在常用编码中过滤未设置的read数据项
	 * @param dicttypeId
	 * @param orgId
	 * @param showdata
	 * @param identifier
	 * @param offset
	 * @param size
	 * @return
	 * @throws ManagerException
	 */
	public ListInfo getReadDictdataList(String dicttypeId, String showdata, String orgId,  
			String identifier, int offset, int size) throws ManagerException;
	
	/**
	 * 常用编码中得到已授权read项的未设置usual项
	 * @param dicttypeId
	 * @param orgId
	 * @return
	 * @throws ManagerException
	 */
	public String[] getUsuslSealedReadDictdataValus(String dicttypeId, String orgId) throws ManagerException;
	
	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#columnUseStatue(com.frameworkset.dictionary.Data, java.lang.String, java.lang.String)
	 * 当前数据库字段的被字典类型的高级字段使用情况
	 */
	public int advanceColumnUseStatue(Data dicttype, String columnName, String selectedValue);
	
	/**
	 * 根据字典表名称得到所有配置了该表的字典类型名称
	 * @param dictdataTablename
	 * @return
	 */
	public String getDicttypeNames(String dictdataTablename);
	
	/**
	 * 字典类型的数据采集, 是否要分级授权.
	 * 两个必要条件:
	 *           (1)必须是授权字典
	 *           (2)字典必须配置了"所属机构"属性
	 * @param dicttype
	 * @return 
	 * DictManager.java
	 * @author: ge.tao
	 */
	public boolean isDicttypeGatherMustFilter(Data dicttype);
	
	/**
	 * 判断数据库表是否被其他字典类型映射, 如果映射了, 那么其他字典指定的值(主键)字段 是那个字段
	 * @param dbName
	 * @param tableName
	 * @return 
	 * DictManager.java
	 * @author: ge.tao
	 */
	public String getOtherDicttypePKColumnByTable(String dbName, String tableName);
	
	/**
	 * 在授权业务字典中，选择用户所属机构时显示用户所属机构的已设置项与未设置项的数据项
	 * @param CurOrg 当前机构ID
	 * @param dicttypeId 字典类型ID
	 * @param identifier 已设置项与未设置项标识：值为sealed时标识为未设置项，值为selected时标识为已设置项  
	 * @param showDate 数据项查询数据
	 * @param offset 
	 * @param size
	 * @return ListInfo
	 * @author: gao.tang 2008.1.15
	 */
	public ListInfo getCurOrgGatherDictDataList(String CurOrg, String dicttypeId, 
			String identifier, String showDate, int offset, int size) throws ManagerException;
	
	/**
	 * 机构授权编码，当字典设置了机构字段-保存数据项向上保存不超过本级机构
	 * @param curOrg 用户所在机构
	 * @param orgId 授权给当前所选机构
	 * @param dicttypeId
	 * @param dictdataValues
	 * @author gao.tang 2008.1.16
	 */
	public void storeReadOrgTaxcode(String curOrg, String orgId, String dicttypeId, String[] dictdataValues );
	
	/**
	 * 机构授权编码-选择用户所属机构时得到用户所属机构的所有数据项的已设置项与未设置项 gao.tang 2008.1.16
	 * @param CurOrg
	 * @param dicttypeId
	 * @param identifier
	 * @return
	 * @throws ManagerException
	 */
	public String[] getCurOrgSelfGatherDictDataValues(String CurOrg, String dicttypeId, 
			String identifier) throws ManagerException;
	
	/**
	 * 超级管理员登陆选择了设置字典机构授权编码，当字典设置了机构字段-保存数据项向上保存不超过数据项所属机构
	 * @param orgId 授权给当前所选机构
	 * @param dicttypeId
	 * @param dictdataValues （value:name:orgnumber org_name）
	 * @author gao.tang 2008.1.16
	 */
	public void storeReadOrgTaxcodeorAdmin(String orgId, String dicttypeId, String[] dictdataValues);
	
	/**
	 * 高级字段排序
	 * @param dicttypeId 字典ID
	 * @param docid 复选框值","隔开
	 * @return
	 */
	public boolean storeAdvanceFieldArr(String dicttypeId, String docid);
	
	/**
	 * 判断字典类型是否缓冲数据
	 * @param dataId
	 * @return
	 * peng.yang 2008.04.08
	 */
	public boolean isCachable(String dataId);
	
	/* (non-Javadoc)
	 * @see com.frameworkset.platform.dictionary.DictManager#getAllDictdataAttachFieldList(java.lang.String)
	 * 获取字典类型的所有附加(高级)字段列表,生成更新页面的时候
	 * 把区对应的值设置到input的字段中去.
	 * 根据字典的名称和值字段.update数据
	 * @param filterMaps 主键信息，不包括字典定义中的主键信息
	 */
	public List getAllDictdataAttachFieldList(String dicttypeId,String nameKey,String valueKey,String primaryCondition); 
	
	/**
	 * 得到主键条件信息 
	 * @param primarykeys
	 * @return " and a.columnName='column' and a.coulumnName='column....'"
	 */
	public String getPrimarykeysCondition(Map primarykeys);
}
