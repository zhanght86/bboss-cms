package com.frameworkset.platform.dictionary;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.dictionary.Data;
import com.frameworkset.dictionary.DataManager;
import com.frameworkset.dictionary.DataManagerFactory;
import com.frameworkset.dictionary.Item;
/**
 * 数据字典的一个数据类型,包含的数据项
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

public class DictDataTree extends COMTree implements java.io.Serializable{
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
			if(mutiId.length==3){
				dicttypeId = mutiId[0];
				Data dictType = dataManager.getDataByID(dicttypeId);
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
		try {
			DataManager dataManager = DataManagerFactory.getDataManager();
//			DictManager dictManager = new DictManagerImpl();
//			List dicttypeList = dictManager.getChildDictdataList(treeId);
			List dicttypeList = null;
			String[] mutiId = treeId.split(":");
			String dicttypeId = "";
			if(mutiId.length==3){
				dicttypeId = mutiId[0];
				Data dictType = dataManager.getDataByID(dicttypeId);
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
				AccessControl accesscontroler = AccessControl.getInstance();
				accesscontroler.checkAccess(request, response);
				//当前机构
				String orgId = accesscontroler.getChargeOrgId();
				//被选中机构
				String selectOrgId = request.getParameter("orgId");
				
				DictManager dictManager = new DictManagerImpl();
				//String[] values = dictManager.getOrgDictdataValue(orgId, super.request.getParameter("did"));
				Iterator iterator = dicttypeList.iterator();
				boolean isAdmin = accesscontroler.isAdmin();
				while (iterator.hasNext()) {					
					Item son = (Item) iterator.next();
					Map map = new HashMap();
					//当前机构有那些 字典 的 可见权限
					boolean state = dictManager.orgHasDictdataReadPower(orgId,son.getDataId(),son.getValue());					
					String ids = son.getDataId()+":"+son.getValue()+":"+son.getName().trim();
					
					map.put("did", ids);//;
					
					if(state || isAdmin){
						boolean ischecked = false;
					    //被选中的机构 那些字典数据被保存过
					    ischecked = dictManager.orgHasDictdataReadPower(selectOrgId,son.getDataId(),son.getValue());
					    if(ischecked){
					    	addNode(father, ids, son.getName().trim(), "dictdata", true, curLevel,
									(String) null, ids, son.getValue()+":"+son.getName()+"' checked='true", map);
					    }else{
					    	addNode(father, ids, son.getName().trim(), "dictdata", true, curLevel,
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
