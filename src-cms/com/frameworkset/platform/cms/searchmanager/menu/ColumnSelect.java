package com.frameworkset.platform.cms.searchmanager.menu;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteCacheManager;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.sql.ColumnMetaData;
import com.frameworkset.common.poolman.sql.TableMetaData;

/**
 * 
 * 
 * <p>
 * Title: MutisiteSelect
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * 
 * <p>
 * Company: iSany
 * </p>
 * 
 * @Date sep 21, 2007
 * @author da.wei
 * @version 1.0
 */
public class ColumnSelect extends COMTree implements java.io.Serializable {

	public boolean hasSon(ITreeNode father) {
		List columnList = null;
		if (father.isRoot()) {
			try {
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			//只有一层
		}
		return false;
	}

	public boolean setSon(ITreeNode father, int curLevel) {
		String db_name = (String)session.getAttribute("db_name");
		String table_name = (String)session.getAttribute("table_name");
		//List columnList = null;
		if (father.isRoot()) {
			try {
				DBUtil db = new DBUtil();
				TableMetaData tableMetaData = db.getTableMetaData(db_name, table_name);
				Set columnSet = tableMetaData.getColumns();
				Iterator columnIter = columnSet.iterator();
				while (columnIter.hasNext()){
					ColumnMetaData columnMetaData = (ColumnMetaData)columnIter.next();
					String column = columnMetaData.getColumnName().toLowerCase();
					String type = columnMetaData.getTypeName();
					String remarks = columnMetaData.getRemarks();
					if(column==null || column.equals(null))
						column = "取列名称失败";
					if(type==null || type.equals(null))
						type = "取列类型失败";
					if(remarks==null || remarks.equals(null))
						remarks = "没有注释";
					String columnValues = column + "(" + type + ")_" + remarks;
					Map map = new HashMap();
					map.put("columnValues", columnValues);
					ITreeNode tt = addNode(father, columnValues, columnValues, "style", false, 
							curLevel, (String) null, columnValues, columnValues, map);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			//只有一层
		}
		return true;
	}

}
