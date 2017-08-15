package com.frameworkset.platform.dictionary;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.dictionary.Data;
import com.frameworkset.dictionary.DataManager;
import com.frameworkset.dictionary.DataManagerFactory;
import com.frameworkset.dictionary.Item;
/**
 * 授权字典业务数据字典过滤树
 * <p>Title: AccreditDictDataTree.java</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: bbossgroups</p>
 * @Date 2008-1-15
 * @author gao.tang
 * @version 1.0
 */

public class AccreditDictDataTree extends COMTree implements java.io.Serializable {
	/**
	 * father的ID是: dicttypeId:dictdataValue:dictdataName
	 */
	public boolean hasSon(ITreeNode father) {
		String treeId = father.getId();
		try {
			DataManager dataManager = DataManagerFactory.getDataManager();
			String[] mutiId = treeId.split(":");
			String dicttypeId = "";
			List dicttypeList = null;
			if(mutiId.length==3){
				dicttypeId = mutiId[0];
				//从缓冲中根据字典ID（dicttypeId）取得数据，如果缓冲中存在则返回缓冲里的数据，否则将数据存入缓冲
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

	public boolean setSon(ITreeNode father, int curLevel) {
		String treeId = father.getId();
		try {
			DataManager dataManager = DataManagerFactory.getDataManager();
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
				Iterator iterator = dicttypeList.iterator();
				while (iterator.hasNext()) {					
					Item son = (Item) iterator.next();
					Map map = new HashMap();
					String ids = son.getDataId()+":"+son.getValue()+":"+son.getName().trim();
					map.put("did", ids);
					addNode(father, ids, son.getName().trim(), "dictdata", true, curLevel,
							(String) null, ids, (String)null, map);
					 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
