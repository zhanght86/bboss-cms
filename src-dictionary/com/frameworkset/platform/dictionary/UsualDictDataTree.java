package com.frameworkset.platform.dictionary;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.dictionary.Data;
import com.frameworkset.dictionary.DataManager;
import com.frameworkset.dictionary.DataManagerFactory;
import com.frameworkset.dictionary.Item;
/**
 * 数据字典的一个数据类型,包含的数据项 常用编码关系维护使用
 * <p>Title: DictDataTree.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date 2007-11-16 10:41:59
 * @author ge.tao
 * @version 1.0
 */

public class UsualDictDataTree extends COMTree implements java.io.Serializable{

	Data dictType = null;
	public void setPageContext(PageContext pageContext)
	{
		super.setPageContext(pageContext);
		
		dictType = null; 
		
		
	}
	/**
	 * father的ID是: dicttypeId:dictdataValue:dictdataName
	 */
	/**
	 * father的ID是: dicttypeId:dictdataValue:dictdataName
	 */
	public boolean hasSon(ITreeNode father) {
		
		
		String treeId = father.getId();
		try {	
//			DictManager dictManager = new DictManagerImpl();
//			return dictManager.isContainDictdata(treeId);
			DataManager dataManager = DataManagerFactory.getDataManager();
			String[] mutiId = treeId.split(":");
			String dicttypeId = "";
			List dicttypeList = null;
			String orgId = super.request.getParameter("orgId");
			if(orgId != null && !"".equals(orgId)){
				if(mutiId.length==3){
					dicttypeId = mutiId[0];
					if(dictType == null)
					{
						dictType = dataManager.getDataByID(dicttypeId);
					}
					if(mutiId[1].equalsIgnoreCase(mutiId[2]) && "root".equalsIgnoreCase(mutiId[1])){//顶级数据项
						dicttypeList = dictType.getItems();
					}else{//子数据项
						String dataValue = mutiId[1];//数据项名称
						//根据数据项名称索引数据项对象
						Map map = dictType.getItemsIdxByValue();
						if(map != null){
							Item item = (Item)map.get(String.valueOf(dataValue));
							//获取数据项的子数据项列表
							if(item != null){
								dicttypeList = item.getSubItems();	
							}
						}
					}
				}
				if (dicttypeList != null && dicttypeList.size()>0) {//有数据项或者子数据项
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return false;
	}
    
	/**
	 * 保证节点的ID是: dicttypeId:dictdataValue:dictdataName
	 */
	public boolean setSon(ITreeNode father, int curLevel) {
		String treeId = father.getId();
		AccessControl accesscontroler = AccessControl.getInstance();
		accesscontroler.checkAccess(request, response);
		//被选中机构
		String orgId = super.request.getParameter("orgId");
		try {
			DataManager dataManager = DataManagerFactory.getDataManager();
//			DictManager dictManager = new DictManagerImpl();
//			List dicttypeList = dictManager.getChildDictdataList(treeId);
			List dicttypeList = null;
			String[] mutiId = treeId.split(":");
			String dicttypeId = "";
			if(mutiId.length==3){
				dicttypeId = mutiId[0];
				if(dictType == null)
				{
					dictType = dataManager.getDataByID(dicttypeId);
				}
				if(mutiId[1].equalsIgnoreCase(mutiId[2]) && "root".equalsIgnoreCase(mutiId[1])){//顶级数据项
					dicttypeList = dictType.getItems();
				}else{//子数据项
					String dataValue = mutiId[1];//数据项名称
					//根据数据项名称索引数据项对象
					Item item = (Item)dictType.getItemsIdxByValue().get(dataValue);
					//获取数据项的子数据项列表
					dicttypeList = item.getSubItems();					
				}
			}
			if (dicttypeList != null) {				
				
				DictManager dictManager = new DictManagerImpl();
				Iterator iterator = dicttypeList.iterator();
				
				while (iterator.hasNext()) {					
					Item son = (Item) iterator.next();
					Map map = new HashMap();					
					String ids = son.getDataId()+":"+son.getValue()+":"+son.getName().trim();
					map.put("did", ids);
					
					//禁用的数据项变灰		
					
					if(Item.DATAVALIDATE_FALSE == son.getDataValidate()){
						map.put("node_checkboxdisabled", new Boolean(true));
					}else{
						map.put("node_checkboxdisabled", new Boolean(false));
					}
					
					//第二个参数,设置father id
					//倒数第二个参数是raido的值,设置为:字典值ID:字典名称
					//被选中机构有 字典 的可见权限 先用可见过滤
					//自己采集的字典数据 都会保存到 机构编码关系表中 所以 只要判断机构编码关系 就可以了
					boolean state = false;
					DictDataProvide dictCache = new DictDataProvide();		
					
					state = dictCache.hasOrgTaxcodeRelationExpSelf(orgId,son.getDataId(),son.getValue(),"read");
									
					int dicttype_type = dictType.getDicttype_type();
					if(orgId != null && orgId.trim().length()>0){//对于选了机构
						if(state && (dicttype_type == DictManager.PARTREAD_BUSINESS_DICTTYPE || 
								dicttype_type == DictManager.BUSINESS_DICTTYPE_POWERONLY) ){//针对权限字典,不维护数据的权限字典 
						    //被选中机构原来选中了那些 字典数据
							boolean ischecked =  dictManager.orgHasDictdataUsualPower(orgId,son.getDataId(),son.getValue());
						    if(ischecked){
						    	addNode(father, ids, son.getName(), "dictdata", true, curLevel,
										(String) null, ids, son.getValue()+":"+son.getName()+"' checked='true", map);
						    }else{
						    	addNode(father, ids, son.getName(), "dictdata", true, curLevel,
										(String) null, ids, son.getValue()+":"+son.getName(), map);
						    }					    
						}else if((dicttype_type == DictManager.ALLREAD_BUSINESS_DICTTYPE || 
								dicttype_type == DictManager.BUSINESS_DICTTYPE_USUALONLY) ){//对于通用字典
							boolean ischecked =  dictManager.orgHasDictdataUsualPower(orgId,son.getDataId(),son.getValue());
						    if(ischecked){
						    	addNode(father, ids, son.getName(), "dictdata", true, curLevel,
										(String) null, ids, son.getValue()+":"+son.getName()+"' checked='true", map);
						    }else{
						    	addNode(father, ids, son.getName(), "dictdata", true, curLevel,
										(String) null, ids, son.getValue()+":"+son.getName(), map);
						    }
						}
					}else{//没选机构
						if(super.accessControl.isAdmin()){
							addNode(father, ids, son.getName(), "dictdata", true, curLevel,
								(String) null, ids, son.getValue()+":"+son.getName(), map);
						}
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
