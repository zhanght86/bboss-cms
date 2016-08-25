// Decompiled by DJ v2.9.9.60 Copyright 2000 Atanas Neshkov  Date: 2006-6-4 10:49:11
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3)
// Source File Name:   ResourceTypeTree.java

package com.frameworkset.platform.menu;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import com.frameworkset.platform.config.ResourceInfoQueue;
import com.frameworkset.platform.config.model.ResourceInfo;
import com.frameworkset.platform.resource.ResourceManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.tree.COMTree;
import com.frameworkset.common.tag.tree.itf.ITreeNode;

public class ResourceTypeTree extends COMTree implements Serializable{

	public ResourceTypeTree() {
	}

	public void setPageContext(PageContext context) {
		super.setPageContext(context);

		if (resourceManager == null)
			resourceManager = new ResourceManager();
	}

	public boolean hasSon(ITreeNode father) {
		String path = father.getPath();
		String id = father.getId();

		boolean hasson = false;
		// 根节点判断
		if (father.isRoot() && id.equals("0"))
			return resourceManager.getResources().getResourceQueue().size() > 0;
		// 判断是否是资源
		if (father.getType().equals("resourceType")
				|| father.getType().equals("resourceType_auto")) {
			hasson = resourceManager.getResourceInfoByType(id)
					.getSubResources() != null
					&& resourceManager.getResourceInfoByType(id)
							.getSubResources().size() > 0;
			// 判断是否有子资源类型
			if (!hasson) { // 如果没有资源类型判断资源的数据结构 如果是列表资源直接返回false
				if (resourceManager.getResourceInfoByType(id).getStruction()
						.equals("list"))
					return false;
				else { // 如果是tree结构的资源判断是否包含资源
					String sql = "select count(*) from td_sm_res where restype_id='"
							+ id + "' and parent_id='-1'";
					DBUtil dbUtil = new DBUtil();
					try {
						dbUtil.executeSelect(sql);
						if (dbUtil.getInt(0, 0) > 0)
							return true;
					} catch (SQLException ex) {
						return false;
					}

				}
			}
		} else if (father.getType().equals("application_resource")) // 如果是资源判断资源是否包含子资源
		{
			String sql = "select count(*) from td_sm_res where parent_id='"
					+ id + "'";
			DBUtil dbUtil = new DBUtil();
			try {
				dbUtil.executeSelect(sql);
				if (dbUtil.getInt(0, 0) > 0)
					return true;
			} catch (SQLException ex) {
				return false;
			}
		}
		return false;
	}

	public boolean setSon(ITreeNode father, int curLevel) {

		String treeid = "";
		String treeName = "";
		String resType = "resourceType";
		boolean showHref = true;
		String memo = null;
		String radioValue = null;
		String checkboxValue = null;
		String path = "";

		String id = father.getId();
		if (id.equals("0")) {
			id = "orgunit";
		}
		if (father.isRoot() || father.getType().equals("resourceType")
				|| father.getType().equals("resourceType_auto")) {
			ResourceInfoQueue resQueue = null;
			ResourceInfo resourceInfo = resourceManager
					.getResourceInfoByType(id);
			if (father.isRoot())
				resQueue = resourceManager.getResourceInfoQueue();
			else
				resQueue = resourceManager.getResourceInfoByType(id)
						.getSubResources();
			for (int i = 0; resQueue != null && i < resQueue.size(); i++) {

				Map params = new HashMap();
				ResourceInfo res = resQueue.getResourceInfo(i);

				if (super.accessControl.checkPermission(res.getId(),
						AccessControl.READ_PERMISSION, "rescustom")) {
					if (res.isUsed()) {
						treeid = res.getId();

						// 不显示“用户自定义资源授权”、“菜单资源”、“tab资源”、“频道资源”和“资源管理”
						if (!(res.getId().equals("rescustom"))
								&& !(res.getId().equals("column"))
								&& !(res.getId().equals("restab"))
								&& !(res.getId().equals("channel"))
								&& !(res.getId().equals("resmanager"))) {
							treeName = res.getName();
							params.put("restypeName", treeName);
							params.put("restypeId", treeid);
							params.put("auto", (new StringBuffer(String
									.valueOf(res.isAuto()))).toString());

							
							if (res.isAuto())
								addNode(father, treeid, treeName, resType
										+ "_auto", showHref, curLevel, memo,
										radioValue, checkboxValue, params);
							else
								addNode(father, treeid, treeName, resType,
										showHref, curLevel, memo, radioValue,
										checkboxValue, params);
						}
					}
				}
			}
			/**
			 * 如果是树型的数据源则设置树型数据资源
			 */
			if (resourceInfo != null
					&& resourceInfo.getStruction().equals("tree")) {
				DBUtil dbUtil = new DBUtil();
				try {
					dbUtil
							.executeSelect("select * from td_sm_res where restype_id='"
									+ id + "' and parent_id='-1'");
					resType = "application_resource";

					for (int i = 0; i < dbUtil.size(); i++) {
						Map params = new HashMap();
						// 输出资源名称
						params.put("restypeName", father.getName());
						// 输出资源类型id
						params.put("restypeId", father.getId());
						treeid = dbUtil.getString(i, "res_id");
						// 输出资源id
						params.put("parent_resId", treeid);
						treeName = dbUtil.getString(i, "title");
						// 输出资源名称
						params.put("parent_resName", treeName);
						params.put("nodeLink",
								"/sysmanager/resmanager/resource_toolbar.jsp");

						addNode(father, treeid, " " + treeName, resType,
								showHref, curLevel, memo, radioValue,
								checkboxValue, params);

					}
				} catch (SQLException ex) {
				}

			}

		} else {
			DBUtil dbUtil = new DBUtil();
			try {
				dbUtil
						.executeSelect("select * from td_sm_res where parent_id='"
								+ id + "'");
				resType = "application_resource";

				for (int i = 0; i < dbUtil.size(); i++) {
					Map params = new HashMap();
					// 输出资源类型名称
					ResourceInfo resourceInfo = resourceManager
							.getResourceInfoByType(dbUtil.getString(i,
									"restype_id"));
					params.put("restypeName", resourceInfo.getName());
					params.put("restypeId", resourceInfo.getId());
					treeid = dbUtil.getString(i, "res_id");
					// 输出资源id
					params.put("parent_resId", treeid);
					// 输出资源名称，权限检测的资源标识
					treeName = dbUtil.getString(i, "title");
					params.put("parent_resName", treeName);
					params.put("nodeLink",
							"/sysmanager/resmanager/resource_toolbar.jsp");
					addNode(father, treeid, " " + treeName, resType, showHref,
							curLevel, memo, radioValue, checkboxValue, params);

				}
			} catch (SQLException ex) {
			}

		}
		return true;
	}

	private static final Logger log;

	private ResourceManager resourceManager;

	static {
		log = Logger.getLogger(com.frameworkset.platform.menu.ResourceTypeTree.class);
	}
}
