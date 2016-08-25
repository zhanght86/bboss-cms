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
 * 可见关系维护:
 * 授权业务字典:
 * 对于树形的数据:
 * 并且设置了所属机构字段:
 * 部门管理员登陆, 选择本机构进行机构编码关系维护, 树形节点上只出现他自己采集的编码, 其他编码不允许选择.
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

public class PowreDictdataTree extends COMTree implements java.io.Serializable{
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
			Data dictType = null;
			if(mutiId.length==3){
				dicttypeId = mutiId[0];
				dictType = dataManager.getDataByID(dicttypeId);
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
				//当前用户所属机构
				String orgId = accesscontroler.getChargeOrgId();
				//被选中机构
				String selectOrgId = request.getParameter("orgId")==null?"":request.getParameter("orgId");
				
				DictManager dictManager = new DictManagerImpl();
				//String[] values = dictManager.getOrgDictdataValue(orgId, super.request.getParameter("did"));
				Iterator iterator = dicttypeList.iterator();
				boolean isAdmin = accesscontroler.isAdmin();
				DictDataProvide dictCache = new DictDataProvide();
				while (iterator.hasNext()) {					
					Item son = (Item) iterator.next();
					Map map = new HashMap();
					//当前登陆用户 所在机构 有权限看到的所有编码 包括: 授权给机构的 + 自己采集的
					boolean state = dictManager.orgHasDictdataReadPower(orgId,son.getDataId(),son.getValue());										
					String ids = son.getDataId()+":"+son.getValue()+":"+son.getName().trim();
					
					//禁用的数据项变灰					
					if(Item.DATAVALIDATE_FALSE == son.getDataValidate()){
						map.put("node_checkboxdisabled", new Boolean(true));
					}else{
						map.put("node_checkboxdisabled", new Boolean(false));
					}
					
					map.put("did", ids);
					
					
					if(selectOrgId.equalsIgnoreCase(orgId) //被选中授权的机构==登陆用户机构 
							&& !isAdmin //不是管理员
							&& (dictType!=null) && (dictType.getDicttype_type() == DictManager.PARTREAD_BUSINESS_DICTTYPE) //字典类型是 授权业务字典
							&& (dictType.getData_create_orgid_field() != null) && (dictType.getData_create_orgid_field().trim().length() > 0)//字典类型设置了"所属机构字段"
					){//以上条件成立 说明是:
					  //部门管理员登陆, 选择本机构进行机构编码关系维护, 树形节点上只能选择他自己采集的编码, 其他编码不允许选择.
						//if(state){//有权限看到的
							if(son.getDataOrg().equalsIgnoreCase(orgId)){//自己采集的 能选择
								boolean ischecked = false;
																
								ischecked = dictCache.hasOrgTaxcodeRelationExpSelf(selectOrgId,son.getDataId(),son.getValue(),"read");
							    if(ischecked){
							    	//有权看(自己采集的和关系) 并且 不是自己采集的  那就是关系的 选中
							    	map.put("node_checkboxchecked", new Boolean(true));
							    	addNode(father, ids, son.getName().trim(), "dictdata", true, curLevel,
											(String) null, ids, son.getValue()+":"+son.getName(), map);
							    }else{
							    	addNode(father, ids, son.getName().trim(), "dictdata", true, curLevel,
											(String) null, ids, son.getValue()+":"+son.getName(), map);
							    }
							}else{//不是自己采集的 不能选择
								//add by ge.tao
								//date 2008-01-22
								if(state){// 有权限看到的
									map.put("node_checkboxdisabled", new Boolean(true));
									addNode(father, ids, son.getName().trim(), "dictdata", true, curLevel,
											(String) null, ids, son.getValue()+":"+son.getName(), map);
								}
							}
						//}						
					}else{
						//当前机构有那些 字典 的 可见权限						
						
						if(state || isAdmin){
							boolean ischecked = false;
						    //被选中的机构 那些字典数据被保存过
						    ischecked = dictCache.hasOrgTaxcodeRelationExpSelf(selectOrgId,son.getDataId(),son.getValue(),"read");
						    if(ischecked){
						    	map.put("node_checkboxchecked", new Boolean(true));
						    	addNode(father, ids, son.getName().trim(), "dictdata", true, curLevel,
										(String) null, ids, son.getValue()+":"+son.getName(), map);
						    }else{
						    	addNode(father, ids, son.getName().trim(), "dictdata", true, curLevel,
										(String) null, ids, son.getValue()+":"+son.getName(), map);
						    }
						    
						    
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
