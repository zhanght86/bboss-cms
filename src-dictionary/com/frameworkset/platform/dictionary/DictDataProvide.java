package com.frameworkset.platform.dictionary;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.frameworkset.event.Event;
import org.frameworkset.event.EventType;
import org.frameworkset.event.Listener;
import org.frameworkset.event.NotifiableFactory;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;

import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.dictionary.Data;
import com.frameworkset.dictionary.DataManager;
import com.frameworkset.dictionary.Item;
import com.frameworkset.dictionary.ProfessionDataManagerException;

/**
 * 项目：SysMgrCore <br>
 * 描述：为 sysui 中的数据字典组件提供数据的数据提供实现类 <br>
 * 日期：Apr 20, 2006 <br>
 * 
 * @author 
 * @author chunqiu.zhao 2007.11.2,biaoping.yin 2008.05.22
 */
public class DictDataProvide implements DataManager,Listener {

	private static Logger logger = Logger.getLogger(DictDataProvide.class);
	private Map datas = new ConcurrentHashMap();	
	private Map datasbyid = new ConcurrentHashMap();
	
	/**
	 * Map<List<orgId>>存放机构的列表,这些机构符合编码权限过滤条件
	 */
	private Map taxcodeOrgs = new ConcurrentHashMap();
	
	/**
	 * Map<List<orgId>>存放机构的列表,这些机构符合编码权限过滤条件 不包括自己采集的
	 */
	private Map expSelf_taxcodeOrgs = new ConcurrentHashMap();
	
	
	private static final List NULL_TAXCODEORGS = new ArrayList(0);
	
    public DictDataProvide(){}
    
    /**
     * 获取机构-编码关系对象 包括有权限的和自己采集的
     * 对象保存的名称是 
     * @param userId
     * @param dicttypeId
     * @param dictdataValue
     * @return 
     * DictDataProvide.java
     * @author: ge.tao
     */
    public List getDictDataOpcodeOrgs(String dicttypeId, String dictdataValue,String opcode){
    	String key = dicttypeId+":"+dictdataValue+":"+opcode;
    	List dictDataUserOrgs = (List)this.taxcodeOrgs.get(key);
    	if(dictDataUserOrgs!=null){
    		return dictDataUserOrgs;
    	}
    	try {
			DictManager dictMgr = new DictManagerImpl();
			dictDataUserOrgs = dictMgr.getDictDataOpcodeOrgs(dicttypeId, dictdataValue,opcode);
			if(dictDataUserOrgs != null&& dictDataUserOrgs.size() > 0)
			{				
				taxcodeOrgs.put(key,dictDataUserOrgs);
				
			}	
			else				
			{
				taxcodeOrgs.put(key,NULL_TAXCODEORGS);
			}
			
			
		} catch (Exception e1) {
			logger.error(e1);
			e1.printStackTrace();
		} 
		return dictDataUserOrgs;
    }
    
    /**
     * 获取机构-编码关系对象 包括有权限的 不包括自己采集的
     * 对象保存的名称是 
     * @param userId
     * @param dicttypeId
     * @param dictdataValue
     * @return 
     * DictDataProvide.java
     * @author: ge.tao
     */
    public List getExpSelfDictDataOpcodeOrgs(String dicttypeId, String dictdataValue,String opcode){
    	String key = dicttypeId+":"+dictdataValue+":"+opcode;
    	List dictDataUserOrgs = (List)this.expSelf_taxcodeOrgs.get(key);
    	if(dictDataUserOrgs!=null){
    		return dictDataUserOrgs;
    	}
    	try {
			DictManager dictMgr = new DictManagerImpl();
			dictDataUserOrgs = dictMgr.getExpSelfDictDataOpcodeOrgs(dicttypeId, dictdataValue,opcode);
			if(dictDataUserOrgs != null&& dictDataUserOrgs.size() > 0)
			{				
				expSelf_taxcodeOrgs.put(key,dictDataUserOrgs);
				
			}	
			else				
			{
				expSelf_taxcodeOrgs.put(key,NULL_TAXCODEORGS);
			}
			
			
		} catch (Exception e1) {
			logger.error(e1);
			e1.printStackTrace();
		} 
		return dictDataUserOrgs;
    }
	
    /**
     * 根据字典类型名称,获取字典类型对象 
     */
	public Data getData(String dictionaryName) throws ProfessionDataManagerException {
		Data data = (Data)this.datas.get(dictionaryName);
		if(data != null)
		{
			return data;
		}
		try {
			DictManagerImpl dictMgr = new DictManagerImpl();
			data = dictMgr.getDicttypeByName(dictionaryName);
			if(data != null)
			{   //字典类型的顶级数据项对象(Item)
				 
				List items = dictMgr.getDictdataList(data.getDataId());
				
//				if(data.isTree())
//				{
//					
//					List newItems = new ArrayList();
//					setCacheValue( data, items,newItems);
//					data.setItems(items);
//					data.setAllitems(newItems);
//				
//				}
//				else
//				{
//					data.setItems(items);
//					data.setAllitems(items);
//				}
//				
//				datas.put(data.getName(),data);
//				datasbyid.put(data.getDataId(),data);
				loadData(data, items);
			}
			return data;
		} catch (ManagerException e1) {
			logger.error(e1);
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return null;
	}
	
	public Data refreshDataByID(String dictionaryID)
	{
		
		
		try {
			removeDictFromCacheByID(dictionaryID);			
			return getDataByID( dictionaryID);
		} catch (Exception e) {
			
			logger.error("",e);
		}
		return null;
	}
	private static Object lock = new Object();
	/**
	 * 根据字典类型ID,获取字典类型对象 
	 */
	public Data getDataByID(String dictionaryID) throws ProfessionDataManagerException {
		DictManagerImpl dictMgr = new DictManagerImpl();
		
		try {
			Data data = (Data)this.datasbyid.get(dictionaryID);
			if(data != null)
			{
				return data;
			}
			synchronized(lock)
			{
				data = (Data)this.datasbyid.get(dictionaryID);
				if(data != null)
				{
					return data;
				}
				data = dictMgr.getDicttypeById(dictionaryID);
				if(data != null)
				{   //字典类型的顶级数据项对象(Item)
					 
					List items = dictMgr.getDictdataList(data.getDataId());
					
					loadData(data, items);
				}
			}
			return data;
		} catch (ManagerException e1) {
			logger.error(e1);
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return null;
	}

	/**
	 * @param data
	 * @param items
	 */
	private void loadData(Data data, List items) {
		if(data.isTree())
		{
			
			List newItems = new ArrayList();
			setCacheValue( data, items,newItems);
			data.setItems(items);
			data.setAllitems(newItems);
			datas.put(data.getName(),data);
			datasbyid.put(data.getDataId(),data);
		}
		else
		{
			data.setItems(items);
			data.setAllitems(items);
			if(data.isCachable())
			{
				datas.put(data.getName(),data);
				datasbyid.put(data.getDataId(),data);
			}
		}
	}
	
	public boolean removeDictFromCacheByID(String dictionaryID) throws ProfessionDataManagerException {
		boolean state = false;
		try {
			
			if(datasbyid != null && datas != null){
				
				Data data = (Data)this.datasbyid.get(dictionaryID);
				if(data != null && data.isCachable())
				{
					
					datasbyid.remove(data.getDataId());
				
				
				
					datas.remove(data.getName());
					
					
					state = true;
				}
			}
		}  catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
		return state;
	}
	
	/**
	 * 递归设置数据到缓冲
	 * @param dictMgr
	 * @param data
	 * @param items
	 * @param newItems 
	 * DictDataProvide.java
	 * @author: ge.tao
	 */
    private void setCacheValue(Data data,List items,List newItems){
    	try{
    		DictManagerImpl dictMgr = new DictManagerImpl();
    		
	    	for(int i=0;i<items.size();i++){
	    		
				Item item = (Item)items.get(i);
			
				List subItems = dictMgr.getChildDictdataListByDataId(item.getDataId(),item.getItemId());
				if(subItems !=null && subItems.size()>0){	
					//System.out.println("-----------递归---------------");
					item.setSubItems(subItems);
					data.setIndexItem(item);
					newItems.add(item);
					//放开 就加载全部数据
					setCacheValue( data, subItems,newItems);					
				}else{
					//System.out.println("-----------不递归---------------");
					item.setSubItems(new ArrayList());
					data.setIndexItem(item);
					newItems.add(item);
				}					
			}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	//return newItems;
    	
    	
    }
	
	/**
	 * 
	 * @param ids 根据字典类型ID:数据项的ID:数据项名称, 如123:45:aa 根节点 123:root:root
	 * @return List<com.frameworkset.dictionary.Item>
	 * @throws ManagerException 
	 * DictDataProvide.java
	 * @author: ge.tao
	 */
	public List getChildDictdataList(String ids) throws ManagerException {
		return null;
	}
	
	/**
	 * 根据字典类型名称,获取字典的数据项列表
	 */
	public List getDataItems(String dictionaryName) throws ProfessionDataManagerException {		
		return getData(dictionaryName).getItems();
	}

    /**
     * 根据字典类型ID和数据项名称,获取数据项的真实值
     */
	public String getItemValue(String dictionaryName, String itemName) throws ProfessionDataManagerException {
		// TODO Auto-generated method stub
		return getData(dictionaryName).getItemValue(itemName);
	}
	
    /**
     * 根据字典类型ID和数据项真实值,获取数据项的名称
     */
	public String getItemName(String dictionaryName, String value) throws ProfessionDataManagerException {
		// TODO Auto-generated method stub
		return getData(dictionaryName).getItemName(value);
	}

	public void init() throws ProfessionDataManagerException {
		List eventType = new ArrayList();
    	eventType.add(DictionaryChangeEvent.DICTIONARY_ADD);
    	eventType.add(DictionaryChangeEvent.DICTIONARY_DATA_ADD);
    	eventType.add(DictionaryChangeEvent.DICTIONARY_DATA_DELETE);
    	eventType.add(DictionaryChangeEvent.DICTIONARY_INFO_UPDATE);
    	
    	eventType.add(DictionaryChangeEvent.DICTIONARY_DATA_ORDERCHANGE);
    	eventType.add(DictionaryChangeEvent.DICTIONARY_DELETE);
    	eventType.add(DictionaryChangeEvent.DICTIONARY_TREE_DATA_ORDERCHANGE);
    	eventType.add(DictionaryChangeEvent.DICTIONARY_DATA_INFO_UPDATE);
    	eventType.add(DictionaryChangeEvent.DICTIONARY_DATA_VALIDATE_UPDATE);
    	eventType.add(DictionaryChangeEvent.DICTIONARY_USERORG_CHANGE_READ);
		eventType.add( DictionaryChangeEvent.DICTIONARY_USERORG_CHANGE_USUAL);
		NotifiableFactory.getNotifiable().addListener(this,eventType);
	}

	public void reinit() {		
		reset();
	}

    private void reset() {
        datas.clear();   
        this.datasbyid.clear();
//        description = null;
//        version = null;
//        datas = null;
    }
    
   

	public  void handle(Event e) {
		Object source = e.getSource();
		EventType type = e.getType();
		Object obj = datasbyid;
		if(source instanceof Item){
			Item dictdata = (Item)source;
			obj = datasbyid.get(dictdata.getDataId());
		}else if(source instanceof Data){
			Data dicttype = (Data)source;
			obj = datasbyid.get(dicttype.getDataId());
		}
		if(obj == null){
			return;
		}
		//datasbyid
		synchronized(obj)
		{
			
			//处理事件
			if(type .equals( DictionaryChangeEvent.DICTIONARY_ADD)){//新增字典类型
	//			if(source instanceof Data){
	//				Data dicttype = (Data)source;
	//				try {
	//					this.getData(dicttype.getName());
	//				} catch (ProfessionDataManagerException e1) {
	//					// TODO Auto-generated catch block
	//					e1.printStackTrace();
	//				}
	//			}
			}else if(type .equals( DictionaryChangeEvent.DICTIONARY_DATA_ADD)){//新增字典数据
				if(source instanceof Item){				
					Item dictdata = (Item)source;				
					try {
						Data dicttype = this.getDataByID(dictdata.getDataId());
						
						if(dicttype==null) 
							return;
						if(!dicttype.isCachable())
							return;
						//放置到item列表中
						if(dictdata.getParentId()==null || dictdata.getParentId().trim().length()==0 
								|| "null".equalsIgnoreCase(dictdata.getParentId())){
							//树形结构,但是 是顶级数据项的
							//平铺结构的,直接放到type的item列表中
							dicttype.addItem(dictdata);	
							
						}else{//树形机构,不是顶级数据项,放到子item的item列表中
							//======.equals(=============================================================================
							Map itemsMap = dicttype.getItemsIdxByValue();
							if(itemsMap!=null){
								//找到父item 
								//为什么找不到根据Value索引的item对象呢??? 重新实例化了ItemsIdxByValueMap
								Item parentItem = (Item)itemsMap.get(String.valueOf(dictdata.getParentId()));
								//添加到数据项下 作为数据项的子数据项列表
								if(parentItem!=null){
									parentItem.addSubItem(dictdata);
								}
							}
						}
						//放到dicttype的索引
						dicttype.setIndexItem(dictdata);
						
					} catch (ProfessionDataManagerException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}			
			}else if(type .equals( DictionaryChangeEvent.DICTIONARY_DATA_DELETE)){//删除字典数据
				if(source instanceof Item){
					Item dictdata = (Item)source;	
					try {
						Data dicttype = this.getDataByID(dictdata.getDataId());
						if(dicttype==null) 
							return;
						if(!dicttype.isCachable())
							return;
						if(dicttype.getIsTree()==DictManager.DICTDATA_IS_TREE){//树形的						
							Map itemsMap = dicttype.getItemsIdxByValue();	
							//缓存现有item
							Item old_item = (Item)itemsMap.get(String.valueOf(dictdata.getValue()));
							String parentId = old_item.getParentId();
							//缓存的父item
							Item parentItem = (Item)itemsMap.get(String.valueOf(parentId));	
							List subItems = new ArrayList();
							if(parentItem == null){//字典类型的一级数据项.
								subItems = dicttype.getItems();							
							}else{//子数据项
								subItems = parentItem.getSubItems();	
							}
							for(int i=0;i<subItems.size();i++){
								Item subItem = (Item)subItems.get(i);
								if(subItem.getValue().equalsIgnoreCase(dictdata.getValue())){
									subItems.remove(i);
									break;
								}
							}
							
							if(parentItem == null){
								dicttype.setItems(subItems);
								dicttype.deleteItem(dictdata);//biaoping.yin 添加于2008.05.22
							}else{
								//重新放subItem
								parentItem.setSubItems(subItems);
								//重新把parentItem放到dicttype
								//dicttype.deleteItem(parentItem);
								dicttype.setIndexItem(parentItem);
								dicttype.deleteItem(dictdata);
							}
						
						}else{//非树形的	
							dicttype.deleteItem(dictdata);
						}
						//重新放dicttype
						this.datasbyid.put(dicttype.getDataId(),dicttype);
					} catch (ProfessionDataManagerException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}else if(type .equals( DictionaryChangeEvent.DICTIONARY_DELETE)){//删除字典类型
				if(source instanceof Data){
					Data dicttype = (Data)source;
					if(!dicttype.isCachable())
						return;
					try {
						this.datas.remove(dicttype.getName());
						this.datasbyid.remove(dicttype.getDataId());
					} catch (Exception e1) {
						e1.printStackTrace();
					}				
					
				}else if(source instanceof Data[]){
					Data[] dicttypes = (Data[])source;
					if(dicttypes != null){
						for(int i = 0; i < dicttypes.length; i++){
							Data dicttype = dicttypes[i];
							if(!dicttype.isCachable())
								return;
							try {
								this.datas.remove(dicttype.getName());
								this.datasbyid.remove(dicttype.getDataId());
							} catch (Exception e1) {
								e1.printStackTrace();
							}	
						}
					}
				}
			}
			else if(type .equals( DictionaryChangeEvent.DICTIONARY_INFO_UPDATE)){//删除字典类型
				if(source instanceof Data){
					Data dicttype = (Data)source;
					if(!dicttype.isCachable())
						return;
					try {
						this.datas.remove(dicttype.getName());
						this.datasbyid.remove(dicttype.getDataId());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}				
					
				}
			}
			
			else if(type .equals( DictionaryChangeEvent.DICTIONARY_DATA_ORDERCHANGE)){//字典数据排序 OK
				if(source instanceof Data){
					Data dicttype = (Data)source;
					if(!(new DictManagerImpl()).isCachable(dicttype.getDataId()))
						return;
					Data old_dicttype = null;
					try {
						old_dicttype = this.getDataByID(dicttype.getDataId());
						List new_items = dicttype.getItems();
						for(int i = 0; new_items != null && i < new_items.size(); i ++)
						{
							Item newitem = (Item)new_items.get(i);
							Item old = old_dicttype.getItemByValue(newitem.getValue());
							copy(old,newitem);
						}
	//					更新items 更新顺序
						old_dicttype.setItems(new_items);
						//this.datasbyid.put(old_dicttype.getDataId(),old_dicttype);
						//先删除原来的items
	
					} catch (ProfessionDataManagerException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					
				}
			}else if(type .equals( DictionaryChangeEvent.DICTIONARY_TREE_DATA_ORDERCHANGE)){//树形 字典数据排序 OK
				if(source instanceof Item){//非一级数据项
					Item dictdata = (Item)source;
					Item old_dictdate = null;
					Data old_dicttype = null;					
					try {
						old_dicttype = this.getDataByID(dictdata.getDataId());
						old_dictdate = (Item)old_dicttype.getItemByValue(dictdata.getValue());
						List putItems = new ArrayList();
						List newItems = dictdata.getSubItems();
						for(int i=0 ; i< newItems.size(); i++){
							Item subItem = (Item)newItems.get(i);
							Item oldSubItem = old_dicttype.getItemByValue(subItem.getValue());
							putItems.add(oldSubItem);						
						}
						old_dictdate.setSubItems(putItems);
					} catch (ProfessionDataManagerException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}else if(source instanceof Data){//一级数据项
					Data dicttype = (Data)source;
					Data old_dicttype = null;
					try {
						old_dicttype = this.getDataByID(dicttype.getDataId());
						
						List putItems = new ArrayList();
						List newItems = dicttype.getItems();
						for(int i=0 ; i< newItems.size(); i++){
							Item subItem = (Item)newItems.get(i);
							Item oldSubItem = old_dicttype.getItemByValue(subItem.getValue());
							putItems.add(oldSubItem);						
						}
						old_dicttype.setItems(putItems);
						
					} catch (ProfessionDataManagerException e1) {
						e1.printStackTrace();
					}
				}
			}else if(type .equals( DictionaryChangeEvent.DICTIONARY_DATA_INFO_UPDATE)){//数据项的修改 事件
				if(source instanceof Item){//非一级数据项
					Item dictdata = (Item)source;
					if(!(new DictManagerImpl()).isCachable(dictdata.getDataId()))
						return;
					Item old_dictdate = null;
					Data old_dicttype = null;
					try {
						//树形字典数据存在parentId变化
						old_dicttype = (Data)this.datasbyid.get(dictdata.getDataId());
						if(old_dicttype == null)
						{
							return;
						}
						old_dicttype = this.getDataByID(dictdata.getDataId());
						String parentId = dictdata.getParentId();
						String oldParentId = dictdata.getOldParentId();
						//如果更新的是树形数据且设置了oldParentId并且oldParentId不为null，oldParentId与parentId的值不相等
						if(old_dicttype.getIsTree() == DictManagerImpl.DICTDATA_IS_TREE 
								&& oldParentId != null && !parentId.equals(oldParentId)){
							//老的缓存数据
							old_dictdate = (Item)old_dicttype.getItemByValue(dictdata.getValue());
							Map itemsMap = old_dicttype.getItemsIdxByValue();	
							
							//缓存老的父ID
							Item oldparentItem = (Item)itemsMap.get(String.valueOf(oldParentId));	
							List oldsubItems = new ArrayList();
							if(oldparentItem == null){//字典类型的一级数据项.
								oldsubItems = old_dicttype.getItems();							
							}else{//子数据项
								oldsubItems = oldparentItem.getSubItems();	
							}
							oldsubItems.remove(old_dictdate);
							//删除old_dictdate以及old_dictdate子数据的授权数据TD_SM_TAXCODE_ORGANIZATION
							if(old_dicttype.getDicttype_type() == Data.DICTTYPE_TYPE_AUTH 
									|| old_dicttype.getDicttype_type() == Data.DICTTYPE_TYPE_NODATAAUTH
									|| old_dicttype.getDicttype_type() == Data.DICTTYPE_TYPE_COMMON
									|| old_dicttype.getDicttype_type() == Data.DICTTYPE_TYPE_NODATACOMMON){
								String dbName = old_dicttype.getDataDBName();
								String tableName = old_dicttype.getDataTableName();
								String nameField = old_dicttype.getDataNameField();
								String valueField = old_dicttype.getDataValueField();
								String parentIdField = old_dicttype.getDataParentIdFild();
								String dictTypeId = old_dicttype.getDataId();
								StringBuffer del_sql = new StringBuffer()
									.append("delete TD_SM_TAXCODE_ORGANIZATION where dicttype_id = '").append(dictTypeId)
									.append("' and (data_value,data_name) in(select ").append(valueField)
									.append(",").append(nameField).append(" from ").append(tableName)
									.append(" start with ").append(valueField).append(" = '").append(old_dictdate.getValue())
									.append("' connect by prior ").append(valueField).append(" = ").append(parentIdField).append(")");
								DBUtil db = new DBUtil();
								try {
//									System.out.println(del_sql.toString());
									db.executeDelete(dbName,del_sql.toString());
								} catch (SQLException e1) {
									e1.printStackTrace();
								}
								
							}
							
							
							//缓存新的父ID
							Item parentItem = (Item)itemsMap.get(String.valueOf(parentId));	
							List subItems = new ArrayList();
							if(parentItem == null){//字典类型的一级数据项.
								subItems = old_dicttype.getItems();							
							}else{//子数据项
								subItems = parentItem.getSubItems();	
							}
							old_dictdate.setName(dictdata.getName());
							old_dictdate.setDataOrg(dictdata.getDataOrg());
							old_dictdate.setDataValidate(dictdata.getDataValidate());
							old_dictdate.setParentId(dictdata.getParentId());
							subItems.add(old_dictdate);
							
						}else{
//							try {
//								old_dictdate = (Item)old_dicttype.getItemByName(dictdata.getName());
////								old_dictdate = (Item)old_dicttype.getItemByValue(dictdata.getValue());
//							} catch (Exception e1) {
//								try {
//									old_dictdate = (Item)old_dicttype.getItemByValue(dictdata.getValue());
//								} catch (Exception e2) {
//									
//								}
//							}
//							if(old_dictdate != null)
//							{
//								//修改了名称
//								old_dictdate.setName(dictdata.getName());
//								old_dictdate.setValue(dictdata.getValue());
//								old_dictdate.setDataOrg(dictdata.getDataOrg());
//								old_dictdate.setDataValidate(dictdata.getDataValidate());
//							}
							this.refreshDataByID(old_dicttype.getDataId());
							
						}
						//....
						
					} catch (ProfessionDataManagerException e1) {
						e1.printStackTrace();
					}
				}
			}else if(type .equals( DictionaryChangeEvent.DICTIONARY_DATA_VALIDATE_UPDATE)){//数据项启用停用状态的修改 
				if(source instanceof Item){
					//dictdata_构造的信息不全的Item, 它的subItem里面是一些Item 
					//这些Item包括 dicttypeId和dictdataId和dataValue
					Item dictdata_ = (Item)source;
					try {
					for(int j=0;j<dictdata_.getSubItems().size();j++){
						Item dictdata = (Item)dictdata_.getSubItems().get(j);
					
						Item old_dictdate = null;
						Data old_dicttype = null;
						
						old_dicttype = this.getDataByID(dictdata.getDataId());
						old_dictdate = (Item)old_dicttype.getItemByValue(dictdata.getValue());					
						old_dictdate.setDataValidate(dictdata_.getDataValidate());
						if(dictdata_.isFlag()){//是否递归							
							try {								
								List subItems = old_dictdate.getSubItems();
								updateValidate(subItems,dictdata_.getDataValidate());
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}
						//....
						
					} catch (ProfessionDataManagerException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		}
		synchronized(taxcodeOrgs){
			if(type .equals( DictionaryChangeEvent.DICTIONARY_USERORG_CHANGE_READ)
					|| type .equals( DictionaryChangeEvent.DICTIONARY_USERORG_CHANGE_USUAL)
					|| type .equals( DictionaryChangeEvent.DICTIONARY_DATA_DELETE)
					|| type .equals( DictionaryChangeEvent.DICTIONARY_DELETE)
					|| type .equals( DictionaryChangeEvent.DICTIONARY_INFO_UPDATE)
					|| type .equals( DictionaryChangeEvent.DICTIONARY_DATA_INFO_UPDATE)){//机构-编码-可见变更 ID dicttypeId:dictdataValue
				
					taxcodeOrgs.clear();
					expSelf_taxcodeOrgs.clear();
			
			}
		}
		
	}
	
	/**
	 * 递归设置状态
	 * @param items
	 * @param flag 
	 * DictDataProvide.java
	 * @author: ge.tao
	 */
	private void updateValidate(List items, int flag){
		if(items == null)
			return;
		for(int i=0;i<items.size();i++){
		    Item subItem = (Item)items.get(i);
		    if(subItem.getSubItems()!=null && subItem.getSubItems().size()>0){
		    	subItem.setDataValidate(flag);
		    	updateValidate(subItem.getSubItems(), flag);
		    }else{
		    	subItem.setDataValidate(flag);
		    }
		}
		
	}
	
	private void copy(Item old,Item newitem)
	{
		newitem.setAttachFields(old.getAttachFields());
		newitem.setSubItems(old.getSubItems());
//		newitem.set
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.dictionary.DataManager#getTaxCodesByUserId(java.lang.String)
	 * @retrun List<com.frameworkset.dictionary.Item>
	 * 根据用户ID,获取用户所在机构的指定税种的编码,如果dicttypeName=="" 取出所有的税种编码
	 */
	public List getTaxCodesByUserId(String userId,String dicttypeName) {
		// TODO Auto-generated method stub
		try {
			DictManager dictManager = new DictManagerImpl();
		    return dictManager.getTaxCodesByUserIdAndTypeName(userId,dicttypeName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.frameworkset.dictionary.DataManager#getTaxCodesByOrgId(java.lang.String)
	 * @retrun List<com.frameworkset.dictionary.Item>
	 * 根据机构ID,获取机构的指定税种的编码,如果dicttypeName=="" 取出所有的税种编码 
	 */
	public List getTaxCodesByOrgId(String orgId,String dicttypeName) {
		// TODO Auto-generated method stub
		try {
			DictManager dictManager = new DictManagerImpl();
			return dictManager.getTaxCodesByOrgIdAndTypeName(orgId,dicttypeName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	

	/* (non-Javadoc)
	 * @see com.frameworkset.dictionary.DataManager#hasOrgTaxcodeRelation(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * 判断机构是否设置了 此类型ID,数据项值,操作编码的权限
	 */
	public boolean hasOrgTaxcodeRelation(String orgid, String dictypeid, String dictdatavalue, String opcode) {
		List orgs = this.getDictDataOpcodeOrgs(dictypeid,dictdatavalue,opcode);
		if(orgs == null) return false;
		for(int i=0;i<orgs.size();i++){
			String orgId = (String)orgs.get(i);
			if(orgId.equalsIgnoreCase(orgid)){
				return true;
			}
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.frameworkset.dictionary.DataManager#hasOrgTaxcodeRelation(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 * 判断机构是否设置了 此类型ID,数据项值,操作编码的权限
	 */
	public boolean hasOrgTaxcodeRelationExpSelf(String orgid, String dictypeid, String dictdatavalue, String opcode) {
		List orgs = this.getExpSelfDictDataOpcodeOrgs(dictypeid,dictdatavalue,opcode);
		if(orgs == null) return false;
		for(int i=0;i<orgs.size();i++){
			String orgId = (String)orgs.get(i);
			if(orgId.equalsIgnoreCase(orgid)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void destory() {
		 datas.clear();   
	     this.datasbyid.clear();
	     this.expSelf_taxcodeOrgs.clear();
	     this.taxcodeOrgs.clear();
	     
	     datas = null;   
	     this.datasbyid = null;
	     this.expSelf_taxcodeOrgs = null;
	     this.taxcodeOrgs = null;
	}

	



}
