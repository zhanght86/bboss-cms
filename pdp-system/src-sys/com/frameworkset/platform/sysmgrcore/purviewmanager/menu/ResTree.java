package com.frameworkset.platform.sysmgrcore.purviewmanager.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frameworkset.platform.sysmgrcore.entity.Res;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.ResManager;
import com.frameworkset.platform.sysmgrcore.manager.db.ResManagerImpl;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;
/**
 * 
 * 
 * <p>Title: ResTree.java</p>
 *
 * <p>Description: 
 * 根据资源类型查找出自定义资源种类,自定义资源表td_sm_res
 * </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: 三一集团</p>
 * @Date  Jan 20, 2009 4:20:18 PM
 * @author gao.tang
 * @version 1.0
 */
public class ResTree extends COMTree implements Serializable {

	public boolean hasSon(ITreeNode father) {
		if(father.isRoot()){
			return true;
		}
		return false;
	}

	public boolean setSon(ITreeNode father, int curLevel) {
		String restypeId = request.getParameter("restypeId");
		ResManager resManager = new ResManagerImpl();
		String sql = "select * from td_sm_res where restype_id='" + restypeId + "'";
		List list = new ArrayList();
		try {
			list = resManager.getResList(sql);
			if(list != null && list.size() > 0){
				Iterator iterator = list.iterator();
				while(iterator.hasNext()){
					Res res = (Res) iterator.next();    
					String title = res.getTitle();
					String path = res.getPath();
					addNode(father, title, path, "res", true, curLevel,
                			(String) null, title+":"+path, "",
							(Map)null);
				}
			}
		} catch (ManagerException e) {
			e.printStackTrace();
		}
		return false;
	}

}
