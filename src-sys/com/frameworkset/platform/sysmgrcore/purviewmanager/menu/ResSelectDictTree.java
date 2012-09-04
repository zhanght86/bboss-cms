package com.frameworkset.platform.sysmgrcore.purviewmanager.menu;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.dictionary.DictManager;
import com.frameworkset.platform.dictionary.DictManagerImpl;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
import com.frameworkset.dictionary.Data;

public class ResSelectDictTree extends COMTree implements Serializable  {

	public boolean hasSon(ITreeNode father) {
		String treeID = father.getId();		
		try {
			DictManager dictManager = new DictManagerImpl();
			
			return dictManager.isContainChildDicttype(treeID);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setSon(ITreeNode father, int curLevel) {
		String treeID = father.getId();
		try {
			DictManager dictManager = new DictManagerImpl();
			List dicttypeList = dictManager.getPartChildDicttypeList(treeID, DictManager.PART_DELETE_SUCCESS);			
			if (dicttypeList != null) {
				Iterator iterator = dicttypeList.iterator();
				while (iterator.hasNext()) {
					Data son = (Data) iterator.next();
					String dataId = son.getDataId();
					String description = son.getDescription().trim();
					if(super.accessControl.checkPermission(son.getDataId().toString(), AccessControl.WRITE_PERMISSION, AccessControl.DICT_RESOURCE)
							|| super.accessControl.checkPermission(son.getDataId().toString(), AccessControl.READ_PERMISSION, AccessControl.DICT_RESOURCE))
					{
						addNode(father, dataId, description, "", true, curLevel,
								(String) null, dataId+":"+description, (String) null, (Map)null);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
