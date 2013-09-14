
package com.frameworkset.dictionary;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;

import com.frameworkset.platform.dictionary.DictManager;
import com.frameworkset.common.poolman.DBUtil;

/**
 * @author biaoping.yin 所有的专业数据以xml文件格式存取 改类封装了井下系统中的使用的固定数据
 * @@org.jboss.cache.aop.AopMarker
 */
public class Data implements Serializable {
	//基础字典
	public static final int DICTTYPE_TYPE_BASE = 0;
	//通用业务字典
	public static final int DICTTYPE_TYPE_COMMON = 1;
	//授权业务字典
	public static final int DICTTYPE_TYPE_AUTH = 2;
	//不维护数据授权业务字典
	public static final int DICTTYPE_TYPE_NODATAAUTH = 4;
	//不维护数据通用业务字典
	public static final int DICTTYPE_TYPE_NODATACOMMON = 5;
	
	private String dataId;

	private String name;

	private List items;

	private List allitems;

	private String parent;

	private String description;

	private Map itemsIdxByName;

	private Map itemsIdxByValue;

	/**
	 * 数据表相关信息 给缺省值
	 */
	private String dataDBName;

	private String dataTableName;

	private String dataNameField;

	private String dataValueField;

	private String dataOrderField;

	private String dataTypeIdField;

	private String dataParentIdFild;

	private int isTree;

	/**
	 * 是否更新数据表中,原来数据的类型ID 0:不更新原来数据 1:更新原来数据
	 */
	private int update_dcitData_typeId;

	/**
	 * 删除字典类型时,是否删除字典类型的 对应的数据 0:不删除 1:删除
	 */
	private int delete_DictData;

	/**
	 * 字典类型种类 0:基础字典 1:通用业务字典 2:授权业务字典 4:不维护数据授权业务字典 5:不维护数据通用业务字典
	 */
	private int dicttype_type;

	/**
	 * 数据项是否有效字段 其中,当改字段的值 1:有效 0:无效
	 */
	private String data_validate_field;

	/**
	 * 数据项的所属机构,或者指定的机构 机构编号 机构名称
	 */
	private String data_create_orgid_field;

	/**
	 * 字典类型指定的数据库表,被字典的使用情况
	 */
	public int dicttypeUseTabelstate;

	/**
	 * 主键生成规则 0:录入 1:自动 tableinfo表插记录
	 */
	private int key_general_type;

	private String nextKeyValue;
	
	
	/**
	 * 字典创建人ID
	 */
	private int user_id;
	
	/**
	 * 数据采集, 名称字段对应名称
	 */
	private String field_name_cn = "名称";
	
	/**
	 * 数据采集, 值字段对应名称
	 */
	private String field_value_cn = "真实值";
	
	/**
	 * 名称生成规则，从字典输入类型中选择，格式为：
	 * inputid:dataformat
     * inputid:dictid:scope
     * inputid:auto:
     * inputid:sequence:seqname
     * inputid:
	 */
	private String name_general_type = "";
	
	/**
	 * 名称生成规则，从字典输入类型中选择，格式为：
	 * inputid:dataformat
     * inputid:dictid:scope
     * inputid:auto:
     * inputid:sequence:seqname
     * inputid:
	 */
	private String key_general_info = "";
	
	/**
	 * 标识值字段的值是否可改变
	 * 0为不可改变
	 * 1为可改变
	 * 默认为0
	 */
	private int enable_value_modify = 0;
	
	/**
	 * 是否需要缓冲数据
      * 0-不需要
      * 1-需要
	 */
	private int needcache = 1;
	
	public String getName_general_type() {
		return name_general_type;
	}

	public void setName_general_type(String name_general_type) {
		this.name_general_type = name_general_type;
	}

	public String getKey_general_info() {
		return key_general_info;
	}

	public void setKey_general_info(String key_general_info) {
		this.key_general_info = key_general_info;
	}

	public int getEnable_value_modify() {
		return enable_value_modify;
	}

	public void setEnable_value_modify(int enable_value_modify) {
		this.enable_value_modify = enable_value_modify;
	}

	public int getNeedcache() {
		return needcache;
	}
	
	public boolean isCachable()
	{
		return this.needcache == 1;
	}
	
	public boolean isEnableValueModify(){
		return this.enable_value_modify == 1; 
	}

	public void setNeedcache(int needcache) {
		this.needcache = needcache;
	}

	public String getField_name_cn() {
		return field_name_cn;
	}

	public void setField_name_cn(String field_name_cn) {
		this.field_name_cn = field_name_cn;
	}

	public String getField_value_cn() {
		return field_value_cn;
	}

	public void setField_value_cn(String field_value_cn) {
		this.field_value_cn = field_value_cn;
	}

	public String getNextKeyValue() {
		return nextKeyValue;
	}

	public void setNextKeyValue(String nextKeyValue) {
		this.nextKeyValue = nextKeyValue;
	}

	public int getKey_general_type() {
		return key_general_type;
	}

	public void setKey_general_type(int key_general_type) {
		this.key_general_type = key_general_type;
	}

	public int getUpdate_dcitData_typeId() {
		return update_dcitData_typeId;
	}

	public void setUpdate_dcitData_typeId(int update_dcitData_typeId) {
		this.update_dcitData_typeId = update_dcitData_typeId;
	}

	public String getDataParentIdFild() {
		return dataParentIdFild;
	}

	public void setDataParentIdFild(String dataParentIdFild) {
		this.dataParentIdFild = dataParentIdFild;
	}

	public int getIsTree() {
		return isTree;
	}

	public void setIsTree(int isTree) {
		this.isTree = isTree;
	}

	public void init() {
		this.dataDBName = DictManager.DEFAULT_DATA_DBNAME;
		this.dataTableName = DictManager.DEFAULT_DATA_TABLENAME;
		this.dataNameField = DictManager.DEFAULT_DATA_NAMEFIELD;
		this.dataValueField = DictManager.DEFAULT_DATA_VALUEFIELD;
		this.dataOrderField = DictManager.DEFAULT_DATA_ORDERFIELD;
		this.dataTypeIdField = DictManager.DEFAULT_DATA_TYPEIDFIELD;
	}

	public Data() {
		init();
	}

	/**
	 * @return Returns the id.
	 */
	public String getDataId() {
		return dataId;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setDataId(String id) {
		this.dataId = id;
	}

	/**
	 * @return Returns the items.
	 */
	public List getItems() {
		return items;
	}

	/**
	 * @param items
	 *            The items to set.
	 */
	public void setItems(List items) {
		this.items = items;
		if (itemsIdxByName == null) {
			itemsIdxByName = new ConcurrentHashMap();
		}
		if (itemsIdxByValue == null) {
			itemsIdxByValue = new ConcurrentHashMap();
		}
		for (int i = 0; items != null && i < items.size(); i++) {
			Item it = (Item) items.get(i);
			itemsIdxByName.put(it.getName(), it);
			itemsIdxByValue.put(it.getValue(), it);
		}
	}

	public void setIndexItem(Item item) {

		if (itemsIdxByName == null) {
			itemsIdxByName = new ConcurrentHashMap();
		}
		if (itemsIdxByValue == null) {
			itemsIdxByValue = new ConcurrentHashMap();
		}
		itemsIdxByName.put(item.getName(), item);
		itemsIdxByValue.put(item.getValue(), item);

	}

	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append("Data:[name=" + name + "][id=" + dataId + "]\r\n");
		for (int i = 0; items != null && i < items.size(); i++) {
			ret.append(items.get(i).toString());
		}
		return ret.toString();
	}

	public Item getItem(int idx) {
		return (Item) items.get(idx);
	}

	public String getItemName(String value)
			throws ProfessionDataManagerException {
		if (items == null){
//			throw new ProfessionDataManagerException("id为'" + dataId
//					+ "'的专业数据不能为空");
//			System.out.println("id为'" + dataId + "'的专业数据不能为空");
			return "字典[id:" + dataId + " name:" + name + "]的数据项为空";
		}

		Item item = (Item) itemsIdxByValue.get(value);
		if (item != null)
			return item.getName();
		// for(int i = 0; i < items.size(); i ++)
		// {
		// Item item = (Item)items.get(i);
		// if(item.getValue().equals(value))
		// return item.getName();
		// }
//		throw new ProfessionDataManagerException("id为'" + dataId
//				+ "'专业数据中不存在值为'" + value + "'的数据项");
		return "字典[id:" + dataId + " name:" + name + "]的数据项["+value+"]不存在";
	}
	
	
	public String getItemNameByValue(String value)
			throws ProfessionDataManagerException {
		if (items == null){
//			throw new ProfessionDataManagerException("id为'" + dataId
//					+ "'的专业数据不能为空");
//			System.out.println("id为'" + dataId + "'的专业数据不能为空");
			return null;
		}

		Item item = (Item) itemsIdxByValue.get(value);
		if (item != null)
			return item.getName();
		// for(int i = 0; i < items.size(); i ++)
		// {
		// Item item = (Item)items.get(i);
		// if(item.getValue().equals(value))
		// return item.getName();
		// }
//		throw new ProfessionDataManagerException("id为'" + dataId
//				+ "'专业数据中不存在值为'" + value + "'的数据项");
		return null;
	}
	
	public String getItemName(String value,String defaultName)
			throws ProfessionDataManagerException {
		if (items == null){
			if(defaultName == null)
			{
//			throw new ProfessionDataManagerException("id为'" + dataId
//					+ "'的专业数据不能为空");
//			System.out.println("id为'" + dataId + "'的专业数据不能为空");
				return "字典[id:" + dataId + " name:" + name + "]的数据项为空";
			}
			else
			{
				return defaultName;
			}
		}

		Item item = (Item) itemsIdxByValue.get(value);
		if (item != null)
			return item.getName();
		// for(int i = 0; i < items.size(); i ++)
		// {
		// Item item = (Item)items.get(i);
		// if(item.getValue().equals(value))
		// return item.getName();
		// }
//		throw new ProfessionDataManagerException("id为'" + dataId
//				+ "'专业数据中不存在值为'" + value + "'的数据项");
		if(defaultName == null)
		{
			return "字典[id:" + dataId + " name:" + name + "]的数据项["+value+"]不存在";
		}
		else
		{
			return defaultName;
		}
	}
	
	public String getItemNameByValue(String value,String defaultName)
			throws ProfessionDataManagerException {
		if (items == null){
			if(defaultName == null)
			{
//			throw new ProfessionDataManagerException("id为'" + dataId
//					+ "'的专业数据不能为空");
//			System.out.println("id为'" + dataId + "'的专业数据不能为空");
				return null;
			}
			else
			{
				return defaultName;
			}
		}

		Item item = (Item) itemsIdxByValue.get(value);
		if (item != null)
			return item.getName();
		// for(int i = 0; i < items.size(); i ++)
		// {
		// Item item = (Item)items.get(i);
		// if(item.getValue().equals(value))
		// return item.getName();
		// }
//		throw new ProfessionDataManagerException("id为'" + dataId
//				+ "'专业数据中不存在值为'" + value + "'的数据项");
		if(defaultName == null)
		{
			return null;
		}
		else
		{
			return defaultName;
		}
	}

	public String getItemValue(String name)
			throws ProfessionDataManagerException {
		if (items == null){
//			throw new ProfessionDataManagerException("id为'" + dataId
//					+ "'的专业数据不能为空");
			return "字典[id:" + dataId + " name:" + name + "]的数据项为空";
		}
		Item item = (Item) itemsIdxByName.get(name);

		if (item != null)
			return item.getValue();

		// for(int i = 0; i < items.size(); i ++)
		// {
		// Item item = (Item)items.get(i);
		// if(item.getName().equals(name))
		// return item.getValue();
		// }
//		throw new ProfessionDataManagerException("id为'" + dataId
//				+ "'的专业数据中不存在名称为" + name + "数据项");
		return "字典[id:" + dataId + " name:" + name + "]不存在名称为["+name+"]的数据项";
	}
	
	public String getItemValueByName(String name)
			throws ProfessionDataManagerException {
		if (items == null){
//			throw new ProfessionDataManagerException("id为'" + dataId
//					+ "'的专业数据不能为空");
			return null;
		}
		Item item = (Item) itemsIdxByName.get(name);

		if (item != null)
			return item.getValue();

		// for(int i = 0; i < items.size(); i ++)
		// {
		// Item item = (Item)items.get(i);
		// if(item.getName().equals(name))
		// return item.getValue();
		// }
//		throw new ProfessionDataManagerException("id为'" + dataId
//				+ "'的专业数据中不存在名称为" + name + "数据项");
		return null;
	}
	
	public String getItemValue(String name,String defaultValue)
			throws ProfessionDataManagerException {
		if (items == null){
//			throw new ProfessionDataManagerException("id为'" + dataId
//					+ "'的专业数据不能为空");
			if(defaultValue == null)
			{
				return "字典[id:" + dataId + " name:" + name + "]的数据项为空";
			}
			else
			{
				return defaultValue;
			}
		}
		Item item = (Item) itemsIdxByName.get(name);

		if (item != null)
			return item.getValue();

		// for(int i = 0; i < items.size(); i ++)
		// {
		// Item item = (Item)items.get(i);
		// if(item.getName().equals(name))
		// return item.getValue();
		// }
//		throw new ProfessionDataManagerException("id为'" + dataId
//				+ "'的专业数据中不存在名称为" + name + "数据项");
		if(defaultValue == null)
		{
			return "字典[id:" + dataId + " name:" + name + "]不存在名称为["+name+"]的数据项";
		}
		else
			return defaultValue;
	}
	
	public String getItemValueByName(String name,String defaultValue)
			throws ProfessionDataManagerException {
		if (items == null){
//			throw new ProfessionDataManagerException("id为'" + dataId
//					+ "'的专业数据不能为空");
			if(defaultValue == null)
			{
				return null;
			}
			else
			{
				return defaultValue;
			}
		}
		Item item = (Item) itemsIdxByName.get(name);

		if (item != null)
			return item.getValue();

		// for(int i = 0; i < items.size(); i ++)
		// {
		// Item item = (Item)items.get(i);
		// if(item.getName().equals(name))
		// return item.getValue();
		// }
//		throw new ProfessionDataManagerException("id为'" + dataId
//				+ "'的专业数据中不存在名称为" + name + "数据项");
		if(defaultValue == null)
		{
			return null;
		}
		else
			return defaultValue;
	}

	public Item getItemByValue(String value)
			throws ProfessionDataManagerException {
		if (items == null){
			throw new ProfessionDataManagerException("字典[id:" + dataId + " name:" + name + "]的专业数据为空");
//			System.out.println("字典[id:" + dataId + " name:" + name + "]的专业数据为空");
		}

		Item item = (Item) itemsIdxByValue.get(value);
		if (item != null)
			return item;
		
		throw new ProfessionDataManagerException("字典[id:" + dataId + " name:" + name + "]的数据项["+value+"]不存在");
	}

	public Item getItemByName(String name)
			throws ProfessionDataManagerException {
		if (items == null){
			throw new ProfessionDataManagerException("字典[id:" + dataId + " name:" + name + "]的专业数据为空");
		}
		Item item = (Item) itemsIdxByName.get(name);

		if (item != null)
			return item;

	
		throw new ProfessionDataManagerException("字典[id:" + dataId + " name:" + name + "]不存在名称为["+name+"]的数据项");
	}

	public int size() {
		return items == null ? 0 : items.size();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getDataNameField() {
		return dataNameField;
	}

	public void setDataNameField(String dataNameField) {
		this.dataNameField = dataNameField;
	}

	public String getDataOrderField() {
		return dataOrderField;
	}

	public void setDataOrderField(String dataOrderField) {
		this.dataOrderField = dataOrderField;
	}

	public String getDataTableName() {
		return dataTableName;
	}

	public void setDataTableName(String dataTableName) {
		this.dataTableName = dataTableName;
	}

	public String getDataValueField() {
		return dataValueField;
	}

	public void setDataValueField(String dataValueField) {
		this.dataValueField = dataValueField;
	}

	public String getDataDBName() {
		return dataDBName;
	}

	public void setDataDBName(String dataDBName) {
		this.dataDBName = dataDBName;
	}

	public String getDataTypeIdField() {
		return dataTypeIdField;
	}

	public void setDataTypeIdField(String dataTypeIdField) {
		this.dataTypeIdField = dataTypeIdField;
	}

	/**
	 * 动态添加字典项
	 * 
	 * @param dictdata
	 */
	public void addItem(Item dictdata) {
		if (this.itemsIdxByValue.containsKey(dictdata.getValue())
				&& this.itemsIdxByName.containsKey(dictdata.getName()))
			return;
		this.items.add(dictdata);
		this.itemsIdxByName.put(dictdata.getName(), dictdata);
		this.itemsIdxByValue.put(dictdata.getValue(), dictdata);
	}

	/**
	 * 动态添加字典项
	 * 
	 * @param dictdata
	 */
	public void deleteItem(Item dictdata) {
		this.items.remove(dictdata);
		this.allitems.remove(dictdata);
		if (this.itemsIdxByValue != null
				&& this.itemsIdxByValue.containsKey(dictdata.getValue())) {
			this.itemsIdxByValue.remove(dictdata.getValue());
		}
		if (this.itemsIdxByValue != null
				&& this.itemsIdxByName.containsKey(dictdata.getName())) {
			this.itemsIdxByName.remove(dictdata.getName());
		}

	}

	public int getDelete_DictData() {
		return delete_DictData;
	}

	public void setDelete_DictData(int delete_DictData) {
		this.delete_DictData = delete_DictData;
	}

	public int getDicttype_type() {
		return dicttype_type;
	}

	public void setDicttype_type(int dicttype_type) {
		this.dicttype_type = dicttype_type;
	}

	public Map getItemsIdxByName() {
		return itemsIdxByName;
	}

	public void setItemsIdxByName(Map itemsIdxByName) {
		this.itemsIdxByName = itemsIdxByName;
	}

	public Map getItemsIdxByValue() {
		return itemsIdxByValue;
	}

	public void setItemsIdxByValue(Map itemsIdxByValue) {
		this.itemsIdxByValue = itemsIdxByValue;
	}

	public String getData_create_orgid_field() {
		return data_create_orgid_field;
	}

	public void setData_create_orgid_field(String data_create_orgid_field) {
		this.data_create_orgid_field = data_create_orgid_field;
	}

	public String getData_validate_field() {
		return data_validate_field;
	}

	public void setData_validate_field(String data_validate_field) {
		this.data_validate_field = data_validate_field;
	}

	public int getDicttypeUseTabelstate() {
		return dicttypeUseTabelstate;
	}

	public void setDicttypeUseTabelstate(int dicttypeUseTabelstate) {
		this.dicttypeUseTabelstate = dicttypeUseTabelstate;
	}

	public boolean isTree() {

		return this.isTree == DictManager.DICTDATA_IS_TREE;
	}

	public List getAllitems() {
		return allitems;
	}

	public void setAllitems(List allitems) {
		this.allitems = allitems;
	}

	/**
	 * 获取主键
	 * 
	 * @return Data.java
	 * @author: ge.tao
	 */
	public String getNextKeyValueStr() {
		String keyValue = "";
		if (this.getKey_general_type() == DictManager.KEY_CREATE_TYPE) {// 自动生成主键的值
			try {
				keyValue = DBUtil.getNextStringPrimaryKey(this.getDataDBName(),
						this.getDataTableName());
				return String.valueOf(keyValue);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return keyValue;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	

}
