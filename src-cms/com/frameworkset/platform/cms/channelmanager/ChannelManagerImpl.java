package com.frameworkset.platform.cms.channelmanager;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.frameworkset.event.Event;
import org.frameworkset.event.EventHandle;
import org.frameworkset.event.EventImpl;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.SQLParams.clobfile;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.cms.channelmanager.bean.ChannelCondition;
import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.customform.CustomFormManager;
import com.frameworkset.platform.cms.customform.CustomFormManagerImpl;
import com.frameworkset.platform.cms.documentmanager.Attachment;
import com.frameworkset.platform.cms.documentmanager.Document;
import com.frameworkset.platform.cms.documentmanager.DocumentExtColumnManager;
import com.frameworkset.platform.cms.documentmanager.DocumentManager;
import com.frameworkset.platform.cms.documentmanager.DocumentManagerImpl;
import com.frameworkset.platform.cms.driver.config.DocumentStatus;
import com.frameworkset.platform.cms.driver.context.Context;
import com.frameworkset.platform.cms.driver.context.impl.DefaultContextImpl;
import com.frameworkset.platform.cms.driver.publish.impl.RecursivePublishManagerImpl;
import com.frameworkset.platform.cms.event.CMSEventType;
import com.frameworkset.platform.cms.searchmanager.CMSSearchManager;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteCacheManager;
import com.frameworkset.platform.cms.sitemanager.SiteManager;
import com.frameworkset.platform.cms.sitemanager.SiteManagerException;
import com.frameworkset.platform.cms.sitemanager.SiteManagerImpl;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.util.FileUtil;
import com.frameworkset.platform.cms.util.FtpUpfile;
import com.frameworkset.platform.cms.util.StringOperate;
import com.frameworkset.platform.cms.util.StringUtil;
import com.frameworkset.platform.config.model.Operation;
import com.frameworkset.platform.resource.ResourceManager;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.security.authorization.impl.AccessPermission;
import com.frameworkset.platform.security.event.ACLEventType;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.RoleManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.util.ListInfo;

public class ChannelManagerImpl extends EventHandle implements ChannelManager {

	private ConfigSQLExecutor executor;

	private Logger log = Logger.getLogger(ChannelManagerImpl.class);

	public boolean channelIsExist(String siteId, String parentChannelId, String channelName)
			throws ChannelManagerException {

		if (channelName == null || channelName.trim().length() == 0) {
			throw new ChannelManagerException("没有提供频道名字,无法判断频道是否存在.");
		}

		try {
			DBUtil db = new DBUtil();
			String sql = "select count(*) total from TD_CMS_CHANNEL " + "where NAME = '" + channelName + "'";
			if (siteId != null && siteId.trim().length() != 0) {
				sql += " and SITE_ID = '" + siteId + "'";
			}
			if (parentChannelId != null && parentChannelId.trim().length() != 0) {
				sql += " and PARENT_ID = '" + parentChannelId + "'";
			}

			db.executeSelect(sql);
			if (db.size() > 0) {
				if (db.getInt(0, "total") > 0) {
					return true;
				}
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("发生sql异常,无法判断频道是否存在.异常信息为:" + e.getMessage());
		}
	}

	public void logCreateChannel(Channel channel, HttpServletRequest request, HttpServletResponse response)
			throws ChannelManagerException {
		try {
			createChannel(channel);
			AccessControl control = AccessControl.getInstance();
			control.checkAccess(request, response);
			String operContent = "";
			String operSource = com.frameworkset.util.StringUtil.getClientIP(request);
			String openModle = "频道管理";
			// String userName = control.getUserName();
			String description = "";
			LogManager logManager = SecurityDatabase.getLogManager();
			operContent = "创建频道:" + channel.getName();
			// System.out.println(operContent);
			description = "";
			logManager.log(control.getUserAccount(), operContent, openModle, operSource, description);
		} catch (Exception e) {
			throw new ChannelManagerException(e.getMessage());
		}
	}

	public void createChannel(Channel channel) throws ChannelManagerException {
		if (channel == null) {
			throw new ChannelManagerException("channel对象为null,无法根据它来创建频道.");
		}
		long siteId = channel.getSiteId();
		if (siteId <= 0) {
			throw new ChannelManagerException("无法创建不属于任何站点的频道.");
		}
		if (hasSameDisplayName(String.valueOf(channel.getSiteId()), channel.getDisplayName())) {
			// throw new ChannelManagerException("频道显示名称已存在，请重新选择.");
			throw new ChannelManagerException("频道已存在，请重新选择.");// 为了频道显示名称能去其它路径名称，将原来的displayName和channelName进行对调,所以更改了异常信息
		}

		long parentChannelId = channel.getParentChannelId();

		if (channelIsExist("" + siteId, (parentChannelId == 0 ? null : "" + parentChannelId), channel.getName())) {
			throw new ChannelManagerException("要新建的频道是当前频道的直接子频道,无法创建同样的频道");
		}
		String sitepath = null;
		try {
			sitepath = (new SiteManagerImpl()).getSiteAbsolutePath("" + siteId);
		} catch (SiteManagerException e1) {
			e1.printStackTrace();
			throw new ChannelManagerException("获取站点的路径发生错误.错误描述为:" + e1.getMessage());
		}
		if (sitepath == null || sitepath.trim().length() == 0) {
			throw new ChannelManagerException("不知道站点的路径,无法创建频道的文件夹.");
		}
		File currSiteDirectory = new File(sitepath);
		if (!currSiteDirectory.exists()) {
			boolean mk = currSiteDirectory.mkdirs();
			if (!mk) {
				throw new ChannelManagerException("站点所属目录不存在,也无法创建,导致无法创建频道的文件夹.");
			}
		}

		File channelRootDirectory = new File(currSiteDirectory.getAbsolutePath(), "_webprj");
		if (!channelRootDirectory.exists()) {
			boolean mkdirs = channelRootDirectory.mkdirs();
			if (!mkdirs) {
				throw new ChannelManagerException("找不到当前站点的存放频道的路径,同时,也没有办法创建它.");
			}
		}

		String channelPath = "";
		if (parentChannelId > 0) {
			Channel parentChannel = getChannelInfo("" + parentChannelId);
			if (parentChannel == null) {
				throw new ChannelManagerException("该新建的频道有父频道,但是找不到父频道的信息.");
			}
			String parentPath = parentChannel.getChannelPath();
			if (parentPath == null || parentPath.trim().length() == 0) {
				throw new ChannelManagerException("新建的频道的父频道没有指定存放路径.");
			}
			channelPath += parentPath;
			if (!parentPath.endsWith("/")) {
				channelPath += "/";
			}
		}

		String tempPath = channel.getChannelPath();
		if (tempPath == null || tempPath.trim().length() == 0) {
			throw new ChannelManagerException("新建频道不能不指定存放路径.");
		}
		channelPath += tempPath;
		channel.setChannelPath(channelPath);

		File currChannelDirectory = new File(channelRootDirectory.getAbsolutePath(), channelPath);
		if (!currChannelDirectory.exists()) {
			boolean mkdirs = currChannelDirectory.mkdirs();
			if (!mkdirs) {
				throw new ChannelManagerException("新建频道的目录不成功.");
			}
		} else {
			throw new ChannelManagerException("频道的目录已经存在,新建频道的目录不成功.");
		}

		// File theFiles = new File(currChannelDirectory.getAbsolutePath(),
		// "files");
		// boolean mkdir = theFiles.mkdir();
		// if (!mkdir) {
		// throw new ChannelManagerException("新建频道下的files目录不成功.");
		// }
		StringBuffer sqlBuffer = new StringBuffer();
		sqlBuffer.append("insert into td_cms_channel(");
		sqlBuffer.append("NAME, DISPLAY_NAME, PARENT_ID,");
		sqlBuffer.append("CHNL_PATH, CREATEUSER, CREATETIME, ORDER_NO,");
		sqlBuffer.append("SITE_ID, STATUS, OUTLINE_TPL_ID, DETAIL_TPL_ID,");
		sqlBuffer.append("CHNL_OUTLINE_DYNAMIC, DOC_DYNAMIC,");
		sqlBuffer
				.append("CHNL_OUTLINE_PROTECT, DOC_PROTECT,WORKFLOW,PARENT_WORKFLOW,pub_file_name,"
						+ "ISNAVIGATOR,NAVIGATORLEVEL,MOUSEINIMAGE,MOUSEOUTIMAGE,MOUSECLICKIMAGE,MOUSEUPIMAGE,"
						+ "outlinePicture,pageflag,indexpagepath,commentswitch,comment_template_id,commentpagepath,specialflag,channel_id,channel_desc,openTarget ");
		sqlBuffer.append(")values(");
		sqlBuffer.append("?,?,?,");
		sqlBuffer.append("?,?,?,?,");
		sqlBuffer.append("?,?,?,?,");
		sqlBuffer.append("?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

		PreparedDBUtil conn = new PreparedDBUtil();
		try {

			String channelId = conn.getNextStringPrimaryKey("td_cms_channel");

			conn.preparedInsert(sqlBuffer.toString());
			String channelName = channel.getName();
			if (channelName == null || channelName.trim().length() == 0) {
				throw new ChannelManagerException("频道名称不能为空.");
			}

			conn.setString(1, channelName);
			conn.setString(2, channel.getDisplayName());
			if (parentChannelId <= 0) {
				conn.setNull(3, java.sql.Types.INTEGER);
			} else {
				conn.setLong(3, parentChannelId);
			}

			conn.setString(4, channelPath);
			long createUser = channel.getCreateUser();
			if (createUser <= 0) {
				throw new ChannelManagerException("频道的创建者不能为空.");
			}
			conn.setLong(5, createUser);
			conn.setTimestamp(6, new Timestamp(new java.util.Date().getTime()));
			conn.setLong(7, channel.getOrderNo());

			if (siteId <= 0) {
				throw new ChannelManagerException("频道所属的站点不能为空.");
			}
			conn.setLong(8, siteId);
			conn.setInt(9, channel.getStaus());

			long outerTplt = channel.getOutlineTemplateId();
			if (outerTplt <= 0) {
				conn.setNull(10, java.sql.Types.INTEGER);
			} else {
				conn.setLong(10, outerTplt);
			}
			long detailTplt = channel.getDetailTemplateId();
			if (detailTplt <= 0) {
				conn.setNull(11, java.sql.Types.INTEGER);
			} else {
				conn.setLong(11, detailTplt);
			}

			conn.setInt(12, channel.getOutlineIsDynamic());
			conn.setInt(13, channel.getDocIsDynamic());
			conn.setInt(14, channel.getOutlineIsProtect());
			conn.setInt(15, channel.getDocIsProtect());

			int workflowFromParent = channel.workflowIsFromParent();
			if (workflowFromParent == 1) {
				conn.setInt(16, 0);
				conn.setInt(17, 1);
			} else {
				int workflow = channel.getWorkflow();
				if (workflow <= 0) {
					conn.setInt(16, 0);
				} else {
					conn.setInt(16, workflow);
				}
				conn.setInt(17, 0);
			}
			String pubFileName = "default.htm";
			// if(channel.getPubFileName()!=null &&
			// channel.getPubFileNameSuffix()!=null){
			// pubFileName =
			// channel.getPubFileName()+"."+channel.getPubFileNameSuffix();
			// }

			if (channel.getPubFileName() != null) {
				pubFileName = channel.getPubFileName();
			}
			conn.setString(18, pubFileName);
			conn.setInt(19, channel.isNavigator() ? 1 : 0);
			conn.setInt(20, channel.getNavigatorLevel());
			conn.setString(21, channel.getMouseInImage());
			conn.setString(22, channel.getMouseOutImage());
			conn.setString(23, channel.getMouseClickImage());
			conn.setString(24, channel.getMouseUpImage());
			conn.setString(25, channel.getOutlinepicture());
			conn.setInt(26, channel.getPageflag());
			conn.setString(27, channel.getIndexpagepath());
			conn.setInt(28, 0); // 默认开通评论功能
			long commentTemplateId = channel.getCommentTemplateId();
			if (commentTemplateId <= 0) {
				conn.setNull(29, java.sql.Types.INTEGER);
			} else {
				conn.setLong(29, commentTemplateId);
			}
			conn.setString(30, channel.getCommentPagePath());
			conn.setInt(31,channel.getSpecialflag());
			
			conn.setString(32,channelId) ;
			conn.setString(33, channel.getChannel_desc());
			conn.setString(34, channel.getOpenTarget());
			conn.executePrepared();

			channel.setChannelId(Integer.parseInt(channelId));// 由于后面要用到该频道对象

			Event event = new EventImpl(channel, CMSEventType.EVENT_CHANNEL_ADD);

			super.change(event, true);
			// 增加自定义表单
			try {
				CustomFormManager cm = new CustomFormManagerImpl();
				cm.addCustomForms(channelId, channel.getCustomforms());
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 新增频道的时候默认将当前用户控制频道权限加上
			String userId = String.valueOf(channel.getCreateUser());
			if (!userId.equals("1")) {
				ResourceManager resManager = new ResourceManager();
				List list = resManager.getOperations("channel");
				List listdoc = resManager.getOperations("channeldoc");
				Operation op = new Operation();
				String[] opids1 = new String[list.size()];
				String[] opids2 = new String[listdoc.size()];
				RoleManager roleManager;
				for (int i = 0; i < list.size(); i++) {
					op = (Operation) list.get(i);
					opids1[i] = op.getId();
				}
				try {
					roleManager = SecurityDatabase.getRoleManager();
					roleManager.storeRoleresop(opids1, channelId, userId, "channel", channelName, "user");
				} catch (Exception e) {

					e.printStackTrace();
				}
				for (int k = 0; k < listdoc.size(); k++) {
					op = (Operation) listdoc.get(k);
					opids2[k] = op.getId();
				}
				try {
					roleManager = SecurityDatabase.getRoleManager();
					roleManager.storeRoleresop(opids2, channelId, userId, "channeldoc", channelName, "user");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			// 新增频道的时候默认将当前频道权限赋给站点管理员角色
			DBUtil db1 = new DBUtil();
			DBUtil db2 = new DBUtil();
			String selectname = "select name from td_cms_site where site_id =" + siteId;

			db1.executeSelect(selectname);
			if (db1.size() > 0) {
				String rolename = db1.getString(0, "name") + "站点管理员";
				String selectrole = "select role_id from td_sm_role where role_name='" + rolename + "'";

				db2.executeSelect(selectrole);

				if (db2.size() > 0) {
					String roleId = db2.getString(0, "role_id");

					ResourceManager resManager = new ResourceManager();
					List list = resManager.getOperations("channel");
					List listdoc = resManager.getOperations("channeldoc");
					Operation op = new Operation();
					String[] opids1 = new String[list.size()];
					String[] opids2 = new String[listdoc.size()];
					RoleManager roleManager;
					for (int i = 0; i < list.size(); i++) {
						op = (Operation) list.get(i);
						opids1[i] = op.getId();
					}
					try {
						roleManager = SecurityDatabase.getRoleManager();
						roleManager.storeRoleresop(opids1, channelId, roleId, "channel", channelName, "role");
					} catch (Exception e) {
						e.printStackTrace();
					}
					for (int k = 0; k < listdoc.size(); k++) {
						op = (Operation) listdoc.get(k);
						opids2[k] = op.getId();
					}
					try {
						roleManager = SecurityDatabase.getRoleManager();
						roleManager.storeRoleresop(opids2, channelId, roleId, "channeldoc", channelName, "role");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("SQL异常,无法创建频道.异常信息为:" + e.getMessage());
		} finally {
			conn.resetPrepare();
		}

	}

	/**
	 * 改变站点的工作流程
	 */
	public boolean changeWorkflow(String channalId, int newFlowId, int workflowIsFromParent)
			throws ChannelManagerException {
		if (channalId == null || channalId.trim().length() == 0) {
			throw new ChannelManagerException("没有提供频道id,不知道要更新哪个频道");
		}

		PreparedDBUtil conn = null;
		try {
			conn = new PreparedDBUtil();
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("update TD_CMS_CHANNEL set WORKFLOW = ?,PARENT_WORKFLOW = ? ");
			sqlBuffer.append("where CHANNEL_ID = ?");

			conn.preparedUpdate(sqlBuffer.toString());
			if (workflowIsFromParent == 1 || newFlowId <= 0) {
				conn.setInt(1, 0);
				conn.setInt(2, 1);
			} else {
				conn.setInt(1, newFlowId);
				conn.setInt(2, 0);
			}
			conn.setString(3, channalId);
			conn.executePrepared();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("编辑频道流程报错！" + e.getMessage());
		} finally {
			conn.resetPrepare();
		}
	}

	/**
	 * 改变频道的概览图片
	 * @param channalId
	 * @param outlinePicture
	 * @return
	 * @throws ChannelManagerException
	 */
	public boolean changeIndexPic(String channalId, String outlinePicture) throws ChannelManagerException {
		if (channalId == null || channalId.trim().length() == 0) {
			throw new ChannelManagerException("没有提供频道id,不知道要更新哪个频道");
		}
		PreparedDBUtil conn = null;
		try {
			conn = new PreparedDBUtil();
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("update TD_CMS_CHANNEL set OUTLINEPICTURE = ? ");
			sqlBuffer.append("where CHANNEL_ID = ?");

			conn.preparedUpdate(sqlBuffer.toString());

			conn.setString(1, outlinePicture);
			conn.setString(2, channalId);

			conn.executePrepared();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("编辑频道概览图片报错！" + e.getMessage());
		} finally {
			conn.resetPrepare();
		}
	}

	public boolean logDeleteChannel(String channelId, HttpServletRequest request, HttpServletResponse response)
			throws ChannelManagerException {
		boolean flag = false;
		Channel channel = getChannelInfo(channelId);
		String siteid = "";
		try {
			AccessControl control = AccessControl.getInstance();
			control.checkAccess(request, response);
			String operContent = "";
			String operSource = com.frameworkset.util.StringUtil.getClientIP(request);
			String openModle = "频道管理";
			// String userName = control.getUserName();
			String description = "";
			LogManager logManager = SecurityDatabase.getLogManager();
			operContent = "删除频道，" + channel.getDisplayName() + "【ID：" + channelId + "】";
			System.out.println(operContent);
			description = "";
			logManager.log(control.getUserAccount(), operContent, openModle, operSource, description);

			/* ********************* 删除文件 start ************************ */

			siteid = channel.getSiteId() + "";
			SiteManager sm = new SiteManagerImpl();
			Site site = CMSUtil.getSiteCacheManager().getSite(siteid);

			/* 频道发布路径 */
			String chlPubPath = CMSUtil.getChannelPubDestinction(siteid, channelId);

			channel = CMSUtil.getChannelCacheManager(siteid).getChannel(channelId);

			boolean[] local2remote = SiteManagerImpl.getSitePublishDestination(site.getPublishDestination());

			// 第一步 ，删除发布到本地的频道文件
			if (local2remote[0]) {
				FileUtil.deleteFile(chlPubPath);// 删除发布的频道内容
			}

			if (local2remote[1]) {
				// 第二步 ，删除发布到ftp的频道文件
				if (site != null && !isNull(site.getFtpIp()) && !isNull(site.getFtpUser())
						&& !isNull(site.getFtpPassword()) && !isNull(site.getFtpFolder())) {
					FtpUpfile ftpUpfile = new FtpUpfile(site.getFtpIp(), site.getFtpUser(), site.getFtpPassword());
					ftpUpfile.login();
					// String relatefilepath = CMSUtil.getPath(CMSUtil.getPath("",
					// site.getSiteDir()), channel.getChannelPath());
					// 频道在ftp相对目录下

					String relatefilepath = "";
					if ("/".equals(site.getFtpFolder())) {
						relatefilepath = "/" + channel.getChannelPath();
					} else {
						relatefilepath = site.getFtpFolder() + "/" + channel.getChannelPath();
					}

					ftpUpfile.deleteAll(relatefilepath);
					ftpUpfile.logout();
				}
			}

			// 第三步 ，删除文档采集时的频道文件
			String chlLocPath = CMSUtil.getPath(CMSUtil.getPath(sm.getSiteAbsolutePath(siteid), "_webprj"),
					channel.getChannelPath());
			FileUtil.deleteFile(chlLocPath);

			/* ****************** 删除文件 end ****************************** */
		} catch (Exception e) {
			throw new ChannelManagerException(e.getMessage());
		}

		flag = deleteChannel(request, channelId, siteid);

		Event event = new EventImpl(channel, CMSEventType.EVENT_CHANNEL_DELETE);
		super.change(event, true);

		return flag;
	}

	public static boolean isNull(String str) {
		boolean flag = false;
		if (str == null || str.trim().length() <= 0) {
			flag = true;
		}
		return flag;
	}

	public boolean deleteChannel(HttpServletRequest request, String channelId, String siteId)
			throws ChannelManagerException {
		boolean flag = false;
		try {
			Channel channel = getChannelInfo(channelId);
			// Context context = new DefaultContextImpl(channel.getSiteId() + "");
			Context context = new DefaultContextImpl(siteId);
			DBUtil db = new DBUtil();
			List sonChls = getDirectSubChannels(channelId);

			if (sonChls == null || sonChls.size() <= 0) {
				// boolean innerflag = false;
				int i = -1;
				int[] docids = null;

				CMSSearchManager sm = new CMSSearchManager();
				try {

					// 删除索引记录,目前只关联了整站索引
					if (CMSUtil.enableIndex()) {
						// sm.deleteChnlIndexs(channelId);
						String selectDocid = "select t.document_id from td_cms_document t" + " where t.channel_id = "
								+ channelId + " and STATUS=5";
						db.executeSelect(selectDocid);
						docids = new int[db.size()];
						for (i = 0; i < db.size(); i++) {
							docids[i] = db.getInt(i, "document_id");
							if (CMSUtil.enableIndexType(CMSUtil.INDEX_ENGINE_TYPE_LUCENE)) {
								sm.deleteDocumetFromIndex(request, docids[i] + "", siteId); // 删除整站索引中的索引记录
							}
							if (CMSUtil.enableIndexType(CMSUtil.INDEX_ENGINE_TYPE_HAILIANG)) {
								CMSUtil.deleteIndexForHL(docids[i] + "", siteId);
							}
						}
					} else {
					}

					// innerflag = true;
				} catch (Exception e) {

				}

				// bug : 删除频道时,只删除了状态是 5(已发)文档
				// 删除所有文档开始 2009-1-16
				if (!flag) {
					// if(i == -1)
					// {
					String selectDocid = "select t.document_id from td_cms_document t where t.channel_id = "
							+ channelId;
					db.executeSelect(selectDocid);
					docids = new int[db.size()];
					i = 0;
					// }

					for (; i < db.size(); i++) {
						docids[i] = db.getInt(i, "document_id");

					}
				}
				DocumentManager dm = new DocumentManagerImpl();
				dm.deleteDoc(docids);
				// 删除该频道下所有文档相关的记录end

				// 删除具体的频道
				String sql = "delete from td_cms_channel t where t.channel_id = " + channelId;
				String sql1 = "delete from td_sm_roleresop where restype_id='channel' and res_id ='" + channelId + "'";
				String sql2 = "delete from td_cms_channelfield t where t.channel_id = " + channelId;
				String sql3 = "delete from td_cms_chnl_ref_doc t where t.chnl_id = " + channelId;
				String sql4 = "delete from x_td_cms_publishschedular t where t.channel_id = " + channelId;
				String sql5 = "delete from td_cms_chnl_ref_doc t where t.citetype = 1 and t.doc_id = " + channelId;
				String sql6 = "delete from td_cms_channel_vote t where t.channel_id = " + channelId;
				String sql7 = "delete from td_cms_doc_template t where t.channel_id = " + channelId;
				// 删除递归发布关系表中关于该频道的记录
				try {
					RecursivePublishManagerImpl recursivePublishManagerImpl = new RecursivePublishManagerImpl();
					recursivePublishManagerImpl.deleteRefObjectsOfPubobject(context, channelId,
							RecursivePublishManagerImpl.PUBOBJECTTYPE_CHANNEL);
					recursivePublishManagerImpl.removeAllPubObjectOfRefelement(context, channelId,
							RecursivePublishManagerImpl.REFOBJECTTYPE_CHANNEL_ANSESTOR + "");
				} catch (Exception e) {

				}

				db.addBatch(sql2);
				db.addBatch(sql3);
				db.addBatch(sql4);
				db.addBatch(sql1);
				db.addBatch(sql5);
				db.addBatch(sql6);
				db.addBatch(sql7);
				db.addBatch(sql);
				db.executeBatch();// 批处理操作数据库
			} else {
				for (int a = 0; a < sonChls.size(); a++) {
					Channel sonChl = (Channel) sonChls.get(a);
					deleteChannel(request, sonChl.getChannelId() + "", siteId);
					deleteChannel(request, channel.getChannelId() + "", siteId);
				}
			}

			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());

		}

		return flag;
	}

	public boolean deleteChannel(String channelId) throws ChannelManagerException {
		boolean flag = false;
		try {
			Channel channel = getChannelInfo(channelId);
			if (channel == null)
				return flag;
			Context context = new DefaultContextImpl(channel.getSiteId() + "");
			DBUtil db = new DBUtil();
			List sonChls = getDirectSubChannels(channelId);

			if (sonChls == null || sonChls.size() <= 0) {

				// //删除频道对应的索引，应该是只删除整站索引中当前频道下的文档记录
				// if(CMSUtil.enableIndex()){
				// CMSSearchManager sm = new CMSSearchManager();
				// try{
				// sm.deleteChnlIndexs(channelId);
				// }catch(Exception e){}
				// }

				String selectDocid = "select t.document_id from td_cms_document t where t.channel_id = " + channelId;
				db.executeSelect(selectDocid);
				int[] docids = new int[db.size()];
				// for (int i = 0; i < db.size(); i++) {
				// docids[i] = db.getInt(i, "document_id");
				// sm.deleteDocumetFromIndex(null, docids[i] + "", channel.getSiteId() + ""); // 删除整站索引中的索引记录
				// }
				DocumentManager dm = new DocumentManagerImpl();
				dm.deleteDoc(docids);
				// 删除该频道下所有文档相关的记录end

				// 删除具体的频道
				String sql = "delete from td_cms_channel t where t.channel_id = " + channelId;
				String sql1 = "delete from td_sm_roleresop where restype_id='channel' and res_id ='" + channelId + "'";
				String sql2 = "delete from td_cms_channelfield t where t.channel_id = " + channelId;
				String sql3 = "delete from td_cms_chnl_ref_doc t where t.chnl_id = " + channelId;
				String sql4 = "delete from x_td_cms_publishschedular t where t.channel_id = " + channelId;
				String sql5 = "delete from td_cms_chnl_ref_doc t where t.citetype = 1 and t.doc_id = " + channelId;
				String sql6 = "delete from td_cms_channel_vote t where t.channel_id = " + channelId;
				String sql7 = "delete from td_cms_doc_template t where t.channel_id = " + channelId;
				// 删除递归发布关系表中关于该频道的记录
				try {
					RecursivePublishManagerImpl recursivePublishManagerImpl = new RecursivePublishManagerImpl();
					recursivePublishManagerImpl.deleteRefObjectsOfPubobject(context, channelId,
							RecursivePublishManagerImpl.PUBOBJECTTYPE_CHANNEL);
					recursivePublishManagerImpl.removeAllPubObjectOfRefelement(context, channelId,
							RecursivePublishManagerImpl.REFOBJECTTYPE_CHANNEL_ANSESTOR + "");
				} catch (Exception e) {

				}

				/*
				 * db.addBatch(sql2); db.addBatch(sql3); db.addBatch(sql4); db.addBatch(sql); db.addBatch(sql1);
				 * db.addBatch(sql5);
				 */
				db.addBatch(sql2);
				db.addBatch(sql3);
				db.addBatch(sql4);
				db.addBatch(sql1);
				db.addBatch(sql5);
				db.addBatch(sql6);
				db.addBatch(sql7);
				db.addBatch(sql);
				db.executeBatch();// 批处理操作数据库
			} else {
				// for (int a = 0; a < sonChls.size(); a++) {
				Channel sonChl = (Channel) sonChls.get(0);
				deleteChannel(sonChl.getChannelId() + "");
				deleteChannel(channel.getChannelId() + "");
				// }
			}

			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());

		}

		return flag;
	}

	/**
	 * 获取频道下所有子频道
	 */
	public List getAllSubChannels(String channelId) throws ChannelManagerException {
		if (channelId == null || channelId.trim().length() == 0) {
			throw new ChannelManagerException("没有提供频道id,无法返回子频道列表.");
		}
		try {
			DBUtil db = new DBUtil();
			String sql = "select * from (select CHANNEL_ID, NAME, DISPLAY_NAME, "
					+ "PARENT_ID, CHNL_PATH, CREATEUSER, CREATETIME, " + "ORDER_NO, SITE_ID, STATUS, OUTLINE_TPL_ID, "
					+ "DETAIL_TPL_ID,  CHANNEL_FLOW_ID(channel_id) WORKFLOW, "
					+ "CHNL_OUTLINE_DYNAMIC, DOC_DYNAMIC, CHNL_OUTLINE_PROTECT, "
					+ "DOC_PROTECT, PARENT_WORKFLOW,ISNAVIGATOR,NAVIGATORLEVEL,MOUSEINIMAGE, MOUSEOUTIMAGE, "
					+ "MOUSECLICKIMAGE, MOUSEUPIMAGE, OUTLINEPICTURE, PAGEFLAG, INDEXPAGEPATH, COMMENTSWITCH, "
					+ " COMMENT_TEMPLATE_ID, COMMENTPAGEPATH, openTarget  from td_cms_channel " + "start with channel_id='"
					+ channelId + "' and status='0' " + "connect by prior channel_id = parent_id and status='0' "
					+ "order by order_no,channel_id) b where b.channel_id<>'" + channelId + "'";
			db.executeSelect(sql);
			List channels = new ArrayList();
			for (int i = 0; i < db.size(); i++) {
				Channel channel = new Channel();
				channel.setChannelId(db.getLong(i, "CHANNEL_ID"));
				channel.setName(db.getString(i, "NAME"));
				channel.setDisplayName(db.getString(i, "DISPLAY_NAME"));
				channel.setParentChannelId(db.getInt(i, "PARENT_ID"));
				channel.setChannelPath(db.getString(i, "CHNL_PATH"));
				channel.setCreateUser(db.getLong(i, "CREATEUSER"));
				channel.setCreateTime(db.getDate(i, "CREATETIME"));
				channel.setOrderNo(db.getInt(i, "ORDER_NO"));
				channel.setSiteId(db.getLong(i, "SITE_ID"));
				channel.setStaus(db.getInt(i, "STATUS"));
				channel.setOutlineTemplateId(db.getInt(i, "OUTLINE_TPL_ID"));
				channel.setDetailTemplateId(db.getInt(i, "DETAIL_TPL_ID"));
				channel.setWorkflow(db.getInt(i, "WORKFLOW"));
				channel.setOutlineIsDynamic(db.getInt(i, "CHNL_OUTLINE_DYNAMIC"));
				channel.setDocIsDynamic(db.getInt(i, "DOC_DYNAMIC"));
				channel.setOutlineIsProtect(db.getInt(i, "CHNL_OUTLINE_PROTECT"));
				channel.setDocIsProtect(db.getInt(i, "DOC_PROTECT"));
				channel.setWorkflowIsFromParent(db.getInt(i, "PARENT_WORKFLOW"));
				channel.setNavigator(0 != db.getInt(i, "ISNAVIGATOR"));
				channel.setNavigatorLevel(db.getInt(i, "NAVIGATORLEVEL"));
				channel.setMouseInImage(db.getString(i, "MOUSEINIMAGE"));
				channel.setMouseOutImage(db.getString(i, "MOUSEOUTIMAGE"));
				channel.setMouseClickImage(db.getString(i, "MOUSECLICKIMAGE"));
				channel.setMouseUpImage(db.getString(i, "MOUSEUPIMAGE"));
				channel.setOutlinepicture(db.getString(i, "OUTLINEPICTURE"));
				channel.setPageflag(db.getInt(i, "PAGEFLAG"));
				channel.setIndexpagepath(db.getString(i, "INDEXPAGEPATH"));
				channel.setCommentTemplateId(db.getInt(i, "COMMENT_TEMPLATE_ID"));
				channel.setCommentPagePath(db.getString(i, "COMMENTPAGEPATH"));
				channel.setOpenTarget(db.getString(i, "openTarget"));
				channels.add(channel);
			}
			return channels;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("发生sql异常,无法返回频道信息.异常信息为:" + e.getMessage());
		}
	}

	/**
	 * 根据频道id查找频道信息,如果没有找到,返回null
	 */
	public Channel getChannelInfo(String channelId) throws ChannelManagerException {
		if (channelId == null || channelId.trim().length() == 0) {
			throw new ChannelManagerException("没有提供频道id,无法返回频道信息.");
		}
		try {
			PreparedDBUtil db = new PreparedDBUtil();
			String sql = "select CHANNEL_ID,NAME,DISPLAY_NAME,PARENT_ID,"
					+ "CHNL_PATH,CREATEUSER,CREATETIME,ORDER_NO,SITE_ID,"
					+ "STATUS,OUTLINE_TPL_ID,DETAIL_TPL_ID, CHANNEL_FLOW_ID(channel_id) WORKFLOW,"
					+ "CHNL_OUTLINE_DYNAMIC,DOC_DYNAMIC,CHNL_OUTLINE_PROTECT,"
					+ "DOC_PROTECT,PARENT_WORKFLOW,pub_file_name,ISNAVIGATOR,NAVIGATORLEVEL,MOUSEINIMAGE, "
					+ "MOUSEOUTIMAGE, MOUSECLICKIMAGE, MOUSEUPIMAGE,OUTLINEPICTURE,"
					+ "PAGEFLAG,INDEXPAGEPATH,comment_template_id,commentpagepath ,specialflag,channel_desc,openTarget "
					+ "from TD_CMS_CHANNEL " + "where status=0 and CHANNEL_ID = ?";
			db.preparedSelect(sql);
			db.setInt(1, Integer.parseInt(channelId));
			db.executePrepared();
			if (db.size() > 0) {
				Channel channel = new Channel();
				channel.setChannelId(db.getLong(0, "CHANNEL_ID"));
				channel.setName(db.getString(0, "NAME"));
				channel.setDisplayName(db.getString(0, "DISPLAY_NAME"));
				channel.setParentChannelId(db.getInt(0, "PARENT_ID"));
				channel.setChannelPath(db.getString(0, "CHNL_PATH"));
				channel.setCreateUser(db.getLong(0, "CREATEUSER"));
				channel.setCreateTime(db.getDate(0, "CREATETIME"));
				channel.setOrderNo(db.getInt(0, "ORDER_NO"));
				channel.setSiteId(db.getLong(0, "SITE_ID"));
				channel.setStaus(db.getInt(0, "STATUS"));
				channel.setOutlineTemplateId(db.getInt(0, "OUTLINE_TPL_ID"));
				channel.setDetailTemplateId(db.getInt(0, "DETAIL_TPL_ID"));
				channel.setWorkflow(db.getInt(0, "WORKFLOW"));
				channel.setOutlineIsDynamic(db.getInt(0, "CHNL_OUTLINE_DYNAMIC"));
				channel.setDocIsDynamic(db.getInt(0, "DOC_DYNAMIC"));
				channel.setOutlineIsProtect(db.getInt(0, "CHNL_OUTLINE_PROTECT"));
				channel.setDocIsProtect(db.getInt(0, "DOC_PROTECT"));
				channel.setWorkflowIsFromParent(db.getInt(0, "PARENT_WORKFLOW"));
				String[] pubFileNames = StringOperate.getFileNameAndExtName(db.getString(0, "pub_file_name"));
				channel.setPubFileName(db.getString(0, "pub_file_name"));
				channel.setPubFileNameSuffix(pubFileNames[1]);
				channel.setNavigator(0 != db.getInt(0, "ISNAVIGATOR"));
				channel.setNavigatorLevel(db.getInt(0, "NAVIGATORLEVEL"));
				channel.setMouseInImage(db.getString(0, "MOUSEINIMAGE"));
				channel.setMouseOutImage(db.getString(0, "MOUSEOUTIMAGE"));
				channel.setMouseClickImage(db.getString(0, "MOUSECLICKIMAGE"));
				channel.setMouseUpImage(db.getString(0, "MOUSEUPIMAGE"));
				channel.setOutlinepicture(db.getString(0, "outlinepicture"));
				channel.setPageflag(db.getInt(0, "PAGEFLAG"));
				channel.setIndexpagepath(db.getString(0, "INDEXPAGEPATH"));
				channel.setCommentTemplateId(db.getInt(0, "comment_template_id"));
				channel.setCommentPagePath(db.getString(0, "commentpagepath"));
				channel.setSpecialflag(db.getInt(0, "specialflag"));
				channel.setChannel_desc(db.getString(0,"channel_desc"));	
				channel.setOpenTarget(db.getString(0, "openTarget"));
				return channel;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("发生sql异常,无法返回频道信息.异常信息为:" + e.getMessage());
		}
	}

	public List getDirectSubChannels(String channelId) throws ChannelManagerException {
		if (channelId == null || channelId.trim().length() == 0) {
			throw new ChannelManagerException("没有提供频道id,无法返回子频道列表.");
		}
		try {
			DBUtil db = new DBUtil();
			String sql = "select CHANNEL_ID, NAME,DISPLAY_NAME, PUB_FILE_NAME, "
					+ "PARENT_ID,CHNL_PATH,CREATEUSER,CREATETIME, " + "ORDER_NO,SITE_ID,STATUS,OUTLINE_TPL_ID, "
					+ "DETAIL_TPL_ID, CHANNEL_FLOW_ID(channel_id) WORKFLOW," + "CHNL_OUTLINE_DYNAMIC,DOC_DYNAMIC, "
					+ "CHNL_OUTLINE_PROTECT, DOC_PROTECT,"
					+ "PARENT_WORKFLOW,ISNAVIGATOR,NAVIGATORLEVEL,MOUSEINIMAGE, MOUSEOUTIMAGE,"
					+ " MOUSECLICKIMAGE, MOUSEUPIMAGE, OUTLINEPICTURE, PAGEFLAG, INDEXPAGEPATH, COMMENTSWITCH, "
					+ " COMMENT_TEMPLATE_ID, COMMENTPAGEPATH ,channel_desc ,openTarget "
					+ " from TD_CMS_CHANNEL where PARENT_ID!=0 and PARENT_ID is not null and status=0 and PARENT_ID="
					+ channelId + " order by order_no,channel_id";
			// System.out.println("sql=" + sql);
			db.executeSelect(sql);
			List channels = new ArrayList();
			for (int i = 0; i < db.size(); i++) {
				Channel channel = new Channel();
				channel.setChannelId(db.getLong(i, "CHANNEL_ID"));
				channel.setName(db.getString(i, "NAME"));
				channel.setDisplayName(db.getString(i, "DISPLAY_NAME"));
				channel.setParentChannelId(db.getInt(i, "PARENT_ID"));
				channel.setChannelPath(db.getString(i, "CHNL_PATH"));
				channel.setCreateUser(db.getLong(i, "CREATEUSER"));
				channel.setCreateTime(db.getDate(i, "CREATETIME"));
				channel.setOrderNo(db.getInt(i, "ORDER_NO"));
				channel.setSiteId(db.getLong(i, "SITE_ID"));
				channel.setStaus(db.getInt(i, "STATUS"));
				channel.setOutlineTemplateId(db.getInt(i, "OUTLINE_TPL_ID"));
				channel.setDetailTemplateId(db.getInt(i, "DETAIL_TPL_ID"));
				channel.setWorkflow(db.getInt(i, "WORKFLOW"));
				channel.setOutlineIsDynamic(db.getInt(i, "CHNL_OUTLINE_DYNAMIC"));
				channel.setDocIsDynamic(db.getInt(i, "DOC_DYNAMIC"));
				channel.setOutlineIsProtect(db.getInt(i, "CHNL_OUTLINE_PROTECT"));
				channel.setDocIsProtect(db.getInt(i, "DOC_PROTECT"));
				channel.setWorkflowIsFromParent(db.getInt(i, "PARENT_WORKFLOW"));
				channel.setNavigator(0 != db.getInt(i, "ISNAVIGATOR"));
				channel.setNavigatorLevel(db.getInt(i, "NAVIGATORLEVEL"));
				channel.setMouseInImage(db.getString(i, "MOUSEINIMAGE"));
				channel.setMouseOutImage(db.getString(i, "MOUSEOUTIMAGE"));
				channel.setMouseClickImage(db.getString(i, "MOUSECLICKIMAGE"));
				channel.setMouseUpImage(db.getString(i, "MOUSEUPIMAGE"));
				String[] pubFileNames = StringOperate.getFileNameAndExtName(db.getString(i, "PUB_FILE_NAME"));
				channel.setPubFileName(db.getString(0, "pub_file_name"));
				channel.setPubFileNameSuffix(pubFileNames[1]);
				channel.setOutlinepicture(db.getString(i, "OUTLINEPICTURE"));
				channel.setPageflag(db.getInt(i, "PAGEFLAG"));
				channel.setIndexpagepath(db.getString(i, "INDEXPAGEPATH"));
				channel.setCommentTemplateId(db.getInt(i, "COMMENT_TEMPLATE_ID"));
				channel.setCommentPagePath(db.getString(i, "COMMENTPAGEPATH"));
				channel.setChannel_desc(db.getString(i,"channel_desc"));	
				channel.setOpenTarget(db.getString(i, "openTarget"));
				channels.add(channel);
			}
			return channels;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("发生sql异常,无法返回频道信息.异常信息为:" + e.getMessage());
		}
	}

	/**
	 * 获取频道的父频道信息,如果没有父频道,将返回null
	 */
	public Channel getParentChannelInfo(String channelId) throws ChannelManagerException {
		if (channelId == null || channelId.trim().length() == 0) {
			throw new ChannelManagerException("没有提供频道id,无法返回父频道信息.");
		}
		try {
			DBUtil db = new DBUtil();
			String sql = "select a.CHANNEL_ID, a.NAME, a.DISPLAY_NAME, "
					+ "a.PARENT_ID, a.CHNL_PATH, a.CREATEUSER, a.CREATETIME, "
					+ "a.ORDER_NO, a.SITE_ID, a.STATUS, a.OUTLINE_TPL_ID, "
					+ "a.DETAIL_TPL_ID, CHANNEL_FLOW_ID(a.channel_id) WORKFLOW,"
					+ "a.CHNL_OUTLINE_DYNAMIC, a.DOC_DYNAMIC, "
					+ "a.CHNL_OUTLINE_PROTECT, a.DOC_PROTECT,"
					+ "a.PARENT_WORKFLOW,A.ISNAVIGATOR, A.NAVIGATORLEVEL,A.MOUSEINIMAGE, A.MOUSEOUTIMAGE, "
					+ "A.MOUSECLICKIMAGE,A.MOUSEUPIMAGE,a.OUTLINEPICTURE, a.PAGEFLAG, a.INDEXPAGEPATH, "
					+ "a.COMMENTSWITCH,a.COMMENT_TEMPLATE_ID,a.COMMENTPAGEPATH,a.channel_desc from TD_CMS_CHANNEL a inner join TD_CMS_CHANNEL b "
					+ "on a.CHANNEL_ID = b.PARENT_ID " + "where b.channel_id ='" + channelId + "'";
			db.executeSelect(sql);
			if (db.size() > 0) {
				Channel channel = new Channel();
				channel.setChannelId(db.getLong(0, "CHANNEL_ID"));
				channel.setName(db.getString(0, "NAME"));
				channel.setDisplayName(db.getString(0, "DISPLAY_NAME"));
				channel.setParentChannelId(db.getInt(0, "PARENT_ID"));
				channel.setChannelPath(db.getString(0, "CHNL_PATH"));
				channel.setCreateUser(db.getLong(0, "CREATEUSER"));
				channel.setCreateTime(db.getDate(0, "CREATETIME"));
				channel.setOrderNo(db.getInt(0, "ORDER_NO"));
				channel.setSiteId(db.getLong(0, "SITE_ID"));
				channel.setStaus(db.getInt(0, "STATUS"));
				channel.setOutlineTemplateId(db.getInt(0, "OUTLINE_TPL_ID"));
				channel.setDetailTemplateId(db.getInt(0, "DETAIL_TPL_ID"));
				channel.setWorkflow(db.getInt(0, "WORKFLOW"));
				channel.setOutlineIsDynamic(db.getInt(0, "CHNL_OUTLINE_DYNAMIC"));
				channel.setDocIsDynamic(db.getInt(0, "DOC_DYNAMIC"));
				channel.setOutlineIsProtect(db.getInt(0, "CHNL_OUTLINE_PROTECT"));
				channel.setDocIsProtect(db.getInt(0, "DOC_PROTECT"));
				channel.setWorkflowIsFromParent(db.getInt(0, "PARENT_WORKFLOW"));
				channel.setNavigator(0 != db.getInt(0, "ISNAVIGATOR"));
				channel.setNavigatorLevel(db.getInt(0, "NAVIGATORLEVEL"));
				channel.setMouseInImage(db.getString(0, "MOUSEINIMAGE"));
				channel.setMouseOutImage(db.getString(0, "MOUSEOUTIMAGE"));
				channel.setMouseClickImage(db.getString(0, "MOUSECLICKIMAGE"));
				channel.setMouseUpImage(db.getString(0, "MOUSEUPIMAGE"));
				channel.setOutlinepicture(db.getString(0, "OUTLINEPICTURE"));
				channel.setPageflag(db.getInt(0, "PAGEFLAG"));
				channel.setIndexpagepath(db.getString(0, "INDEXPAGEPATH"));
				channel.setCommentTemplateId(db.getInt(0, "COMMENT_TEMPLATE_ID"));
				channel.setCommentPagePath(db.getString(0, "COMMENTPAGEPATH"));
				channel.setChannel_desc(db.getString(0,"channel_desc"));		
				return channel;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("发生sql异常,无法返回父频道信息.异常信息为:" + e.getMessage());
		}
	}

	/**
	 * 通过频道显示名称获取频道的父频道信息,如果没有父频道,将返回null
	 */
	public Channel getParentChannelInfoByDisplayName(String siteid, String channelDisplayName)
			throws ChannelManagerException {
		if (channelDisplayName == null || channelDisplayName.trim().length() == 0) {
			throw new ChannelManagerException("没有提供频道显示名称,无法返回父频道信息.");
		}
		try {
			DBUtil db = new DBUtil();
			String sql = "select a.CHANNEL_ID, a.NAME, a.DISPLAY_NAME, "
					+ "a.PARENT_ID, a.CHNL_PATH, a.CREATEUSER, a.CREATETIME, "
					+ "a.ORDER_NO, a.SITE_ID, a.STATUS, a.OUTLINE_TPL_ID, "
					+ "a.DETAIL_TPL_ID, CHANNEL_FLOW_ID(a.channel_id) WORKFLOW,"
					+ "a.CHNL_OUTLINE_DYNAMIC, a.DOC_DYNAMIC, " + "a.CHNL_OUTLINE_PROTECT, a.DOC_PROTECT,"
					+ "a.PARENT_WORKFLOW,A.ISNAVIGATOR, A.NAVIGATORLEVEL,A.MOUSEINIMAGE, A.MOUSEOUTIMAGE,"
					+ " A.MOUSECLICKIMAGE,A.MOUSEUPIMAGE,a.OUTLINEPICTURE, a.PAGEFLAG, a.INDEXPAGEPATH,"
					+ " a.COMMENTSWITCH,a.COMMENT_TEMPLATE_ID,a.COMMENTPAGEPATH ,a.channel_desc"
					+ " from TD_CMS_CHANNEL a inner join TD_CMS_CHANNEL b " + "on a.CHANNEL_ID = b.PARENT_ID "
					+ "where b.DISPLAY_NAME ='" + channelDisplayName + "' and b.site_id = " + siteid;
			db.executeSelect(sql);
			if (db.size() > 0) {
				Channel channel = new Channel();
				channel.setChannelId(db.getLong(0, "CHANNEL_ID"));
				channel.setName(db.getString(0, "NAME"));
				channel.setDisplayName(db.getString(0, "DISPLAY_NAME"));
				channel.setParentChannelId(db.getInt(0, "PARENT_ID"));
				channel.setChannelPath(db.getString(0, "CHNL_PATH"));
				channel.setCreateUser(db.getLong(0, "CREATEUSER"));
				channel.setCreateTime(db.getDate(0, "CREATETIME"));
				channel.setOrderNo(db.getInt(0, "ORDER_NO"));
				channel.setSiteId(db.getLong(0, "SITE_ID"));
				channel.setStaus(db.getInt(0, "STATUS"));
				channel.setOutlineTemplateId(db.getInt(0, "OUTLINE_TPL_ID"));
				channel.setDetailTemplateId(db.getInt(0, "DETAIL_TPL_ID"));
				channel.setWorkflow(db.getInt(0, "WORKFLOW"));
				channel.setOutlineIsDynamic(db.getInt(0, "CHNL_OUTLINE_DYNAMIC"));
				channel.setDocIsDynamic(db.getInt(0, "DOC_DYNAMIC"));
				channel.setOutlineIsProtect(db.getInt(0, "CHNL_OUTLINE_PROTECT"));
				channel.setDocIsProtect(db.getInt(0, "DOC_PROTECT"));
				channel.setWorkflowIsFromParent(db.getInt(0, "PARENT_WORKFLOW"));
				channel.setNavigator(0 != db.getInt(0, "ISNAVIGATOR"));
				channel.setNavigatorLevel(db.getInt(0, "NAVIGATORLEVEL"));
				channel.setMouseInImage(db.getString(0, "MOUSEINIMAGE"));
				channel.setMouseOutImage(db.getString(0, "MOUSEOUTIMAGE"));
				channel.setMouseClickImage(db.getString(0, "MOUSECLICKIMAGE"));
				channel.setMouseUpImage(db.getString(0, "MOUSEUPIMAGE"));

				channel.setOutlinepicture(db.getString(0, "OUTLINEPICTURE"));
				channel.setPageflag(db.getInt(0, "PAGEFLAG"));
				channel.setIndexpagepath(db.getString(0, "INDEXPAGEPATH"));
				channel.setCommentTemplateId(db.getInt(0, "COMMENT_TEMPLATE_ID"));
				channel.setCommentPagePath(db.getString(0, "COMMENTPAGEPATH"));
				channel.setChannel_desc(db.getString(0,"channel_desc"));	
				return channel;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("发生sql异常,无法返回父频道信息.异常信息为:" + e.getMessage());
		}
	}

	public List getSupportedChannelPublishStatus(String channelId) throws ChannelManagerException {
		// TODO 优先级:低
		throw new ChannelManagerException("getSupportedChannelPublishStatus()方法还没有实现.");
	}

	/**
	 * 判断某个频道下是否有子频道。 parent_id为0或者为null的频道永远不会计算在内,
	 * parent_id为0或为null的频道,会被认为是站点的一级频道
	 */
	public boolean hasSubChannel(String channelId) throws ChannelManagerException {
		if (channelId == null || channelId.trim().length() == 0) {
			throw new ChannelManagerException("没有提供频道id,无法判断频道下是否有子频道.");
		}
		try {
			DBUtil db = new DBUtil();
			String sql = "select count(*) total from TD_CMS_CHANNEL "
					+ "where status=0 and parent_id is not null and parent_id!=0 and parent_id='" + channelId + "'";
			db.executeSelect(sql);
			if (db.size() > 0) {
				if (db.getInt(0, "total") > 0) {
					return true;
				}
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("发生sql异常,无法判断频道下是否有子频道.异常信息为:" + e.getMessage());
		}
	}

	public boolean startChannel(String channelId) throws ChannelManagerException {
		// TODO 优先级：低
		throw new ChannelManagerException("startChannel()方法还没有实现.");
	}

	/**
	 * 判断站点是否是活动的，即站点的status是否为0（正常）
	 * 
	 * @author xinwang.jiao
	 * @return boolean
	 * @throws ChannelManagerException
	 */
	public boolean isActive(String channelId) throws ChannelManagerException {
		boolean flag = false;
		int status = ((Channel) getChannelInfo(channelId)).getStaus();
		if (status == 0)
			flag = true;
		return flag;
	}

	private boolean canUpdate(Channel channel) throws ChannelManagerException {
		long channelId = channel.getChannelId();
		String name = channel.getName();
		String displayName = channel.getDisplayName();
		long siteId = channel.getSiteId();
		long prtChannelId = channel.getParentChannelId();
		StringBuffer sql = new StringBuffer("select count(*) as total from td_cms_channel ");
		sql.append(" where channel_id != '" + channelId + "' ");
		sql.append(" and SITE_ID = '" + siteId + "' ");
		if (prtChannelId > 0) {
			sql.append(" and PARENT_ID = '" + prtChannelId + "'");
		} else {
			sql.append(" and(PARENT_ID is null or PARENT_ID = 0)");
		}
		sql.append(" and(1!=1 ");
		if (name != null && name.trim().length() != 0) {
			sql.append(" or NAME = '" + name + "' ");
		}
		if (displayName != null && displayName.trim().length() != 0) {
			sql.append(" or DISPLAY_NAME = '" + displayName + "' ");
		}
		sql.append(")");
		System.out.println("sql=" + sql.toString());
		try {
			DBUtil db = new DBUtil();
			db.executeSelect(sql.toString());
			if (db.size() > 0) {
				if (db.getInt(0, "total") > 0) {
					return false;
				}
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("发生sql异常,无法判断频道是否可以更新.异常信息为:" + e.getMessage());
		}
	}

	public boolean logUpdateChannel(Channel channel, HttpServletRequest request, HttpServletResponse response)
			throws ChannelManagerException {
		boolean returnValue = false;
		try {
			returnValue = updateChannel(channel);
			if (returnValue) {
				AccessControl control = AccessControl.getAccessControl();
				
				String operContent = "";
				String operSource = com.frameworkset.util.StringUtil.getClientIP(request);
				String openModle = "频道管理";
				// String userName = control.getUserName();
				String description = "";
				LogManager logManager = SecurityDatabase.getLogManager();
				operContent = "更新频道:" + channel.getName();
				description = "";
				logManager.log(control.getUserAccount(), operContent, openModle, operSource, description);
			}
			return returnValue;
		} catch (Exception e) {
			throw new ChannelManagerException(e.getMessage());
		}
	}

	public boolean updateChannel(Channel channel) throws ChannelManagerException {
		if (channel == null) {
			throw new ChannelManagerException("channel对象为空,无法更新频道");
		}
		if (!canUpdate(channel)) {
			throw new ChannelManagerException("可能重名等原因,频道不可更新");
		}
		if (hasSameDisplayNameForUpdate(String.valueOf(channel.getSiteId()), channel.getDisplayName(),
				channel.getChannelId())) {
			// throw new ChannelManagerException("频道显示名称已存在，请重新选择.");
			throw new ChannelManagerException("频道已存在，请重新选择.");// 为了频道显示名称能去其它路径名称，将原来的displayName和channelName进行对调,所以更改了异常信息
		}

		PreparedDBUtil conn = null;
		try {
			conn = new PreparedDBUtil();
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("update TD_CMS_CHANNEL set ");

			sqlBuffer.append("NAME=?,");
			sqlBuffer.append("DISPLAY_NAME=?,");
			// change-start
			// 由于更新概缆模板和细缆模板单独拿出来作，所以这里不更新。
			// sqlBuffer.append("OUTLINE_TPL_ID=?,");
			// sqlBuffer.append("DETAIL_TPL_ID=?,");
			// chang-end
			sqlBuffer.append("CHNL_OUTLINE_DYNAMIC=?,");
			sqlBuffer.append("DOC_DYNAMIC=?,");
			sqlBuffer.append("CHNL_OUTLINE_PROTECT=?,");
			sqlBuffer.append("DOC_PROTECT=?,");
			sqlBuffer.append("ORDER_NO = ? ,");
			sqlBuffer.append("pub_file_name = ?,");
			sqlBuffer.append("ISNAVIGATOR = ? ,");
			sqlBuffer.append("NAVIGATORLEVEL = ?, ");
			sqlBuffer.append("MOUSEINIMAGE = ? ,");
			sqlBuffer.append("MOUSEOUTIMAGE = ?, ");
			sqlBuffer.append("MOUSECLICKIMAGE = ?, ");
			sqlBuffer.append("MOUSEUPIMAGE = ?,");
			sqlBuffer.append("outlinepicture = ?, ");
			sqlBuffer.append("WORKFLOW = ?, ");
			sqlBuffer.append("PARENT_WORKFLOW = ?, ");
			sqlBuffer.append("pageflag = ?, ");
			sqlBuffer.append("indexpagepath = ?, ");
			sqlBuffer.append("outline_tpl_id = ?, ");
			sqlBuffer.append("detail_tpl_id = ?, ");
			sqlBuffer.append("comment_template_id = ?, ");
			sqlBuffer.append("commentpagepath = ?, ");
			sqlBuffer.append("specialflag = ? ,");
			sqlBuffer.append("channel_desc = ?, ");
			sqlBuffer.append("openTarget = ? ");
			sqlBuffer.append("where CHANNEL_ID = ?");

			conn.preparedUpdate(sqlBuffer.toString());

			String name = channel.getName();
			if (name == null || name.trim().length() == 0) {
				throw new ChannelManagerException("频道名字为空,不允许插入数据库.");
			}

			conn.setString(1, name);

			conn.setString(2, channel.getDisplayName());
			/*
			 * long outerTplt = channel.getOutlineTemplateId(); if (outerTplt <= 0) { conn.setNull(3,
			 * java.sql.Types.INTEGER); } else { conn.setLong(3, outerTplt); } long detailTplt =
			 * channel.getDetailTemplateId(); if (detailTplt <= 0) { conn.setNull(4, java.sql.Types.INTEGER); } else {
			 * conn.setLong(4, detailTplt); }
			 */

			conn.setInt(3, channel.getOutlineIsDynamic());

			conn.setInt(4, channel.getDocIsDynamic());

			conn.setInt(5, channel.getOutlineIsProtect());

			conn.setInt(6, channel.getDocIsProtect());

			conn.setInt(7, channel.getOrderNo());

			long channelId = channel.getChannelId();
			if (channelId <= 0) {
				throw new ChannelManagerException("频道id为空,无法更新频道");
			}
			String pubFileName = "default.htm";
			// if(channel.getPubFileName()!= null &&
			// channel.getPubFileNameSuffix()!=null)
			// pubFileName =
			// channel.getPubFileName()+"."+channel.getPubFileNameSuffix();
			if (channel.getPubFileName() != null)
				pubFileName = channel.getPubFileName();
			conn.setString(8, pubFileName);
			conn.setInt(9, channel.isNavigator() ? 1 : 0);
			conn.setInt(10, channel.getNavigatorLevel());
			conn.setString(11, channel.getMouseInImage());
			conn.setString(12, channel.getMouseOutImage());
			conn.setString(13, channel.getMouseClickImage());
			conn.setString(14, channel.getMouseUpImage());
			conn.setString(15, channel.getOutlinepicture());
			conn.setInt(16, channel.getWorkflow());
			conn.setInt(17, channel.workflowIsFromParent());
			conn.setInt(18, channel.getPageflag());
			conn.setString(19, channel.getIndexpagepath());
			if (channel.getOutlineTemplateId() == 0)
				conn.setNull(20, Types.NUMERIC);
			else
				conn.setInt(20, channel.getOutlineTemplateId());
			if (channel.getDetailTemplateId() == 0)
				conn.setNull(21, Types.NUMERIC);
			else
				conn.setInt(21, channel.getDetailTemplateId());
			if (channel.getCommentTemplateId() == 0)
				conn.setNull(22, Types.NUMERIC);
			else
				conn.setInt(22, channel.getCommentTemplateId());
			conn.setString(23, channel.getCommentPagePath());
			conn.setInt(24, channel.getSpecialflag());
			conn.setString(25, channel.getChannel_desc());
			conn.setString(26,channel.getOpenTarget());
			conn.setLong(27, channelId);
			conn.executePrepared();
			Event event = new EventImpl(channel, CMSEventType.EVENT_CHANNEL_UPDATE);
			super.change(event, true);

			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("频道编辑保存报错！" + e.getMessage());
		} finally {
			conn.resetPrepare();
		}
	}

	public boolean updateChannelOutputTemplateId(int channelId, int ouputTemplateId) throws ChannelManagerException {

		PreparedDBUtil conn = null;
		try {
			conn = new PreparedDBUtil();
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("update TD_CMS_CHANNEL set ");
			sqlBuffer.append("OUTLINE_TPL_ID=? ");
			sqlBuffer.append("where CHANNEL_ID = ?");
			conn.preparedUpdate(sqlBuffer.toString());
			if (ouputTemplateId <= 0) {
				conn.setNull(1, java.sql.Types.INTEGER);
			} else {
				conn.setLong(1, ouputTemplateId);
			}
			conn.setLong(2, channelId);
			conn.executePrepared();
			Event event = new EventImpl("", ACLEventType.RESOURCE_INFO_CHANGE);
			super.change(event, true);
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("频道编辑保存报错！" + e.getMessage());
		} finally {
			conn.resetPrepare();
		}
	}

	public boolean updateChannelDetailTemplateId(int channelId, int detailTemplateId) throws ChannelManagerException {
		PreparedDBUtil conn = null;
		try {
			conn = new PreparedDBUtil();
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("update TD_CMS_CHANNEL set ");
			sqlBuffer.append("DETAIL_TPL_ID=? ");
			sqlBuffer.append("where CHANNEL_ID = ?");
			conn.preparedUpdate(sqlBuffer.toString());
			if (detailTemplateId <= 0) {
				conn.setNull(1, java.sql.Types.INTEGER);
			} else {
				conn.setLong(1, detailTemplateId);
			}
			conn.setLong(2, channelId);
			conn.executePrepared();
			Event event = new EventImpl("", CMSEventType.EVENT_CHANNEL_UPDATE);
			super.change(event, true);
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("频道编辑保存报错！" + e.getMessage());
		} finally {
			conn.resetPrepare();
		}
	}

	/**
	 * 获取频道审核人列表
	 */
	public List getAuditorList(String channelId) throws ChannelManagerException {
		AccessPermission[] ap = new AccessPermission[2];
		ap[0] = new AccessPermission(channelId, AccessControl.AUDIT_PERMISSION, AccessControl.CHANNELDOC_RESOURCE);
		ap[1] = new AccessPermission(this.getChannelInfo(channelId).getSiteId() + "", AccessControl.AUDIT_PERMISSION,
				AccessControl.SITEDOC_RESOURCE);
		List users = null;
		try {
			users = SecurityDatabase.getRoleManager().getAllUserTypeRoleHasPermission(ap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}

	/**
	 * 获取审核角色列表
	 */
	public List getAuditRoleList(String channelId) throws ChannelManagerException {
		AccessPermission[] ap = new AccessPermission[2];
		ap[0] = new AccessPermission(channelId, AccessControl.AUDIT_PERMISSION, AccessControl.CHANNELDOC_RESOURCE);
		ap[1] = new AccessPermission(this.getChannelInfo(channelId).getSiteId() + "", AccessControl.AUDIT_PERMISSION,
				AccessControl.SITEDOC_RESOURCE);
		List roles = null;
		try {
			roles = SecurityDatabase.getRoleManager().getAllRoleTypeRoleHasPermission(ap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return roles;
	}

	/**
	 * 获取审核机构列表
	 */
	public List getAuditOrgList(String channelId) throws ChannelManagerException {
		AccessPermission[] ap = new AccessPermission[2];
		ap[0] = new AccessPermission(channelId, AccessControl.AUDIT_PERMISSION, AccessControl.CHANNELDOC_RESOURCE);
		ap[1] = new AccessPermission(this.getChannelInfo(channelId).getSiteId() + "", AccessControl.AUDIT_PERMISSION,
				AccessControl.SITEDOC_RESOURCE);
		List orgs = null;
		try {
			orgs = SecurityDatabase.getRoleManager().getAllOrganizationTypeRoleHasPermission(ap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orgs;
	}

	/**
	 * 获取具有文档发布权限的发布人列表
	 */
	public List getPublisherList(String channelId) throws ChannelManagerException {
		AccessPermission[] ap = new AccessPermission[2];
		ap[0] = new AccessPermission(channelId, AccessControl.DOCPUBLISH_PERMISSION, AccessControl.CHANNELDOC_RESOURCE);
		ap[1] = new AccessPermission(this.getChannelInfo(channelId).getSiteId() + "",
				AccessControl.DOCPUBLISH_PERMISSION, AccessControl.SITEDOC_RESOURCE);
		List users = null;
		try {
			users = SecurityDatabase.getRoleManager().getAllUserTypeRoleHasPermission(ap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return users;
	}

	/**
	 * 获取具有文档发布权限的机构列表
	 */
	public List getPublishOrgList(String channelId) throws ChannelManagerException {
		AccessPermission[] ap = new AccessPermission[2];
		ap[0] = new AccessPermission(channelId, AccessControl.DOCPUBLISH_PERMISSION, AccessControl.CHANNELDOC_RESOURCE);
		ap[1] = new AccessPermission(this.getChannelInfo(channelId).getSiteId() + "",
				AccessControl.DOCPUBLISH_PERMISSION, AccessControl.SITEDOC_RESOURCE);
		List roles = null;
		try {
			roles = SecurityDatabase.getRoleManager().getAllOrganizationTypeRoleHasPermission(ap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return roles;
	}

	/**
	 * 获取具有文档发布权限的角色列表
	 */
	public List getPublishRoleList(String channelId) throws ChannelManagerException {
		AccessPermission[] ap = new AccessPermission[2];
		ap[0] = new AccessPermission(channelId, AccessControl.DOCPUBLISH_PERMISSION, AccessControl.CHANNELDOC_RESOURCE);
		ap[1] = new AccessPermission(this.getChannelInfo(channelId).getSiteId() + "",
				AccessControl.DOCPUBLISH_PERMISSION, AccessControl.SITEDOC_RESOURCE);
		List orgs = null;
		try {
			orgs = SecurityDatabase.getRoleManager().getAllRoleTypeRoleHasPermission(ap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orgs;
	}

	public int[] getChnlDistributeManners(String channelId) throws ChannelManagerException {
		int[] a = { 0 };
		return a;
	}

	public List getFlowInfo(String channelId) throws ChannelManagerException {
		if (channelId == null || channelId.trim().length() == 0) {
			throw new ChannelManagerException("没有频道id,无法频道的流程信息");
		}
		try {
			DBUtil db = new DBUtil();
			String sql = "select ID,NAME from table(f_channel_flow('" + channelId + "'))";
			db.executeSelect(sql);
			if (db.size() > 0) {
				List flowInfo = new ArrayList(2);
				flowInfo.add(db.getString(0, "ID"));
				flowInfo.add(db.getString(0, "NAME"));
				return flowInfo;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("发生sql异常,无法返回流程信息.异常信息为:" + e.getMessage());
		}
	}

	public List getAllUNPublishDocumentOfChannel(String channelID) {
		// TODO 还没有实现
		return null;
	}

	private Template getTemplate(String channelId, char type) throws ChannelManagerException {
		if (channelId == null || channelId.trim().length() == 0) {
			throw new ChannelManagerException("没有提供模板id无法获取它的模板信息");
		}
		String sql = "";
		if (type == '1') {
			sql = "select a.* from td_cms_template a inner join td_cms_channel b "
					+ "on a.TEMPLATE_ID = b.DETAIL_TPL_ID " + "where CHANNEL_ID = ?";
		} else if (type == '2') {
			sql = "select a.* from td_cms_template a inner join td_cms_channel b "
					+ "on a.TEMPLATE_ID = b.OUTLINE_TPL_ID " + "where CHANNEL_ID = ?";

		} else {
			throw new ChannelManagerException("不知道要取概览模板还是细览模板");
		}
		try {
			PreparedDBUtil db = new PreparedDBUtil();
			db.preparedSelect(sql);
			db.setInt(1, Integer.parseInt(channelId));
			db.executePrepared();
			if (db.size() > 0) {
				Template t = new Template();
				t.setTemplateId(db.getInt(0, "TEMPLATE_ID"));
				t.setName(db.getString(0, "NAME"));
				t.setDescription(db.getString(0, "DESCRIPTION"));
				t.setHeader(db.getString(0, "HEADER"));
				try {
					t.setText(db.getString(0, "TEXT"));
				} catch (Exception e) {

				}
				t.setType(db.getInt(0, "TYPE"));
				t.setCreateUserId(db.getInt(0, "CREATEUSER"));
				t.setCreateTime(db.getDate(0, "CREATETIME"));
				t.setIncreasePublishFlag(db.getInt(0, "INC_PUB_FLAG"));
				t.setTemplateFileName(db.getString(0, "TEMPLATEFILENAME"));

				t.setTemplatePath(db.getString(0, "TEMPLATEPATH"));
				t.setPersistType(db.getInt(0, "PERSISTTYPE"));
				return t;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("获取模板信息时发生异常.异常信息为:" + e.getMessage());
		}
	}

	public Template getDetailTemplateOfChannel(String channelId) throws ChannelManagerException {
		return this.getTemplate(channelId, '1');
	}

	public Template getOutlineTemplateOfChannel(String channelId) throws ChannelManagerException {
		return this.getTemplate(channelId, '2');
	}

	/**
	 * 获取频道下待发布文档和已经发布过的方法
	 */
	public List getAllCanPubDocsOfChnl(String channelId) throws ChannelManagerException {
		return getSpecialDoc(channelId, '1');
	}

	/**
	 * 获取频道下所有待发布的文档
	 */
	public List getIncCanPubDocsOfChnl(String channelId) throws ChannelManagerException {
		return getSpecialDoc(channelId, '2');
	}

	/**
	 * 判断文档是否可以发布
	 * 
	 * @param docid
	 * @param increament
	 * @return
	 */
	public boolean canPublishDocument(String docid, boolean increament) {
		String sql = "";
		if (!increament) {

			sql = "select 1 " + "from td_cms_document a " + "where a.document_id = " + docid + " and (a.STATUS = 5 "
					+ " or 5 in (select dest_status from v_doc_flow_trans c "
					+ "where c.flow_id=a.flow_id and c.SRC_STATUS=a.status)) ";
		} else {
			sql = "select 1 " + "from td_cms_document a " + "where a.document_id = " + docid
					+ " and 5 in (select dest_status from v_doc_flow_trans c "
					+ "where c.flow_id=a.flow_id and c.SRC_STATUS=a.status)) ";
		}

		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			return db.size() > 0;
		} catch (SQLException e) {

			e.printStackTrace();
			return false;
		}

	}

	/**
	 * 将频道下所有未发布能发布文档的状态修改为已发布
	 * 
	 * @param channel
	 *            频道显示名称
	 */
	public void publishUnpublishDocs(String channel) throws ChannelManagerException {
		if (channel == null || channel.trim().length() == 0) {
			throw new ChannelManagerException("频道id为null 无法获取频道下面的文档");
		}
		StringBuffer sql = new StringBuffer("update td_cms_document a set a.status=5 where 5 in ");
		sql.append("(select dest_status from v_doc_flow_trans c ")
				.append("where c.flow_id=a.flow_id and c.SRC_STATUS=a.status and c.SRC_STATUS!=")
				.append(DocumentStatus.PUBLISHED.getStatus()).append(" and c.SRC_STATUS!=")
				.append(DocumentStatus.ARCHIVED.getStatus()).append(" and c.src_status != ")
				.append(DocumentStatus.WITHDRAWPUBLISHED.getStatus()).append(" and c.SRC_STATUS!=")
				.append(DocumentStatus.RECYCLED.getStatus()).append(" and c.SRC_STATUS!=")
				.append(DocumentStatus.PUBLISHING.getStatus()).append(") ").append("and a.channel_id in ")
				.append("(select channel_id from td_cms_channel b where b.display_name='").append(channel).append("')");
		try {
			DBUtil db = new DBUtil();
			db.executeUpdate(sql.toString());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("获取更新频道下文档状态为已发布时发生异常.异常信息为:" + e.getMessage());
		}
	}

	public void publishUnpublishDocs(int channelId) throws ChannelManagerException {
		if (channelId == 0) {
			throw new ChannelManagerException("频道id为0 无法获取频道下面的文档");
		}
		StringBuffer sql = new StringBuffer("update td_cms_document a set a.status=5 where 5 in ")
				.append("(select dest_status from v_doc_flow_trans c ")
				.append("where c.flow_id=a.flow_id and c.SRC_STATUS=a.status and c.SRC_STATUS!=")
				.append(DocumentStatus.PUBLISHED.getStatus()).append(" and c.SRC_STATUS!=")
				.append(DocumentStatus.ARCHIVED.getStatus()).append(" and c.src_status != ")
				.append(DocumentStatus.WITHDRAWPUBLISHED.getStatus()).append(" and c.SRC_STATUS!=")
				.append(DocumentStatus.RECYCLED.getStatus()).append(" and c.SRC_STATUS!=")
				.append(DocumentStatus.PUBLISHING.getStatus()).append(") ").append("and a.channel_id=")
				.append(channelId);
		try {
			DBUtil db = new DBUtil();
			db.executeUpdate(sql.toString());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("获取更新频道下文档状态为已发布时发生异常.异常信息为:" + e.getMessage());
		}
	}

	private List getSpecialDoc(String channelId, char type) throws ChannelManagerException {
		if (channelId == null || channelId.trim().length() == 0) {
			throw new ChannelManagerException("频道id为null 无法获取频道下面的文档");
		}
		StringBuffer sql = new StringBuffer();
		if (type == '1') {// 获取频道下待发布文档和已经发布过的方法

			sql.append("select a.document_id,a.title,a.channel_id,a.status,a.flow_id ")
					.append("from td_cms_document a ")
					.append("where a.channel_id = ")
					.append(channelId)
					.append(" and (a.STATUS in ( " + DocumentStatus.PUBLISHED.getStatus() + ","
							+ DocumentStatus.PUBLISHING.getStatus() + ") ")
					.append(" or EXISTS (select dest_status from v_doc_flow_trans c ")
					.append("where c.flow_id=a.flow_id and c.SRC_STATUS=a.status and a.status!=")
					.append(DocumentStatus.PUBLISHED.getStatus()).append(" and a.status!=")
					.append(DocumentStatus.ARCHIVED.getStatus()).append(" and a.status != ")
					.append(DocumentStatus.WITHDRAWPUBLISHED.getStatus()).append(" and a.status!=")

					.append(DocumentStatus.RECYCLED.getStatus()).append(" and a.status!=")
					.append(DocumentStatus.PUBLISHING.getStatus())

					.append(" and c.dest_status = ").append(DocumentStatus.PUBLISHED.getStatus()).append(")) ");
		} else if (type == '2') {// 获取频道下所有待发布的文档
			sql.append("select a.document_id,a.title,a.channel_id,a.status,a.flow_id ")
					.append("from td_cms_document a ").append("where a.channel_id =").append(channelId)
					.append(" and EXISTS  (select dest_status from v_doc_flow_trans c ")
					.append("where c.flow_id=a.flow_id and c.SRC_STATUS=a.status and a.status!=")
					.append(DocumentStatus.PUBLISHED.getStatus()).append(" and a.status!=")
					.append(DocumentStatus.ARCHIVED.getStatus()).append(" and a.status != ")
					.append(DocumentStatus.WITHDRAWPUBLISHED.getStatus()).append(" and a.status!=")
					.append(DocumentStatus.RECYCLED.getStatus()).append(" and a.status!=")
					.append(DocumentStatus.PUBLISHING.getStatus()).append(" and c.dest_status = ")
					.append(DocumentStatus.PUBLISHED.getStatus()).append(")");
		} else {
			throw new ChannelManagerException("不知道是要取所有可发布频道还是取增量发布频道");
		}
		try {
			DBUtil db = new DBUtil();
			db.executeSelect(sql.toString());
			List docs = new ArrayList();
			for (int i = 0; i < db.size(); i++) {
				Document doc = new Document();
				doc.setDocument_id(db.getInt(i, "DOCUMENT_ID"));
				doc.setTitle(db.getString(i, "TITLE"));
				doc.setChanel_id(db.getInt(i, "CHANNEL_ID"));
				doc.setStatus(db.getInt(i, "STATUS"));
				doc.setFlowId(db.getInt(i, "FLOW_ID"));
				docs.add(doc);
			}
			return docs;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("获取文档列表时发生异常.异常信息为:" + e.getMessage());
		}
	}

	/**
	 * 获取某个频道的引用文档集合 return List<Document> param chnId(频道id)
	 */
	public List getRefDocsOfChnl(String chnlId) throws ChannelManagerException {
		List list = new ArrayList();
		DBUtil db = new DBUtil();
		String sql = "select b.DOCUMENT_ID, b.TITLE, b.SUBTITLE, b.AUTHOR, b.CHANNEL_ID,"
				+ " b.STATUS, b.KEYWORDS, b.DOCABSTRACT, b.DOCTYPE, b.DOCWTIME, b.TITLECOLOR,"
				+ " b.CREATETIME, b.CREATEUSER, b.DOCSOURCE_ID, b.DETAILTEMPLATE_ID, b.LINKTARGET,"
				+ " b.FLOW_ID, b.DOC_LEVEL, b.DOC_KIND, b.AGGREGATION, b.PARENT_DETAIL_TPL "
				+ " from TD_CMS_CHNL_REF_DOC a  inner join "
				+ "td_cms_document b on a.doc_id = b.document_id where chnl_id = " + chnlId
				+ " order by b.DOCWTIME desc";
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					Document doc = new Document();

					doc.setDocument_id(db.getInt(i, "DOCUMENT_ID"));
					doc.setTitle(db.getString(i, "title"));
					doc.setSubtitle(db.getString(i, "subtitle"));
					doc.setAuthor(db.getString(i, "author"));
					doc.setChanel_id(db.getInt(i, "CHANNEL_ID"));
					doc.setStatus(db.getInt(i, "STATUS"));
					doc.setKeywords(db.getString(i, "KEYWORDS"));
					doc.setDocabstract(db.getString(i, "DOCABSTRACT"));
					doc.setDoctype(db.getInt(i, "DOCTYPE"));
					doc.setDocwtime(db.getDate(i, "DOCWTIME"));
					doc.setTitlecolor(db.getString(i, "TITLECOLOR"));
					doc.setCreateTime(db.getDate(i, "CREATETIME"));
					doc.setCreateUser(db.getInt(i, "CREATEUSER"));
					doc.setDoc_kind(db.getInt(i, "DOC_KIND"));
					doc.setAggregation(db.getInt(i, "AGGREGATION"));
					// doc.setp

					list.add(doc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}
		return list;
	}

	/**
	 * 根据用户id取有审核权限频道列表
	 */
	public List getChnlList(String userId) throws ChannelManagerException {
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		List list = new ArrayList();

		String str = "select count(*) from td_sm_userrole where user_id =" + userId + " and role_id='1'";

		String sql = " select distinct res_id from td_sm_roleresop where types='user' "
				+ " and  restype_id ='channel' and role_id =" + userId + " and res_id in "
				+ " (select channel_id from td_cms_channel) " + " union "
				+ " select res_id from td_sm_roleresop where types='role' and  "
				+ " restype_id ='channel' and role_id in " + " (select role_id from td_sm_userrole where user_id ="
				+ userId + ") and res_id  in " + " (select channel_id from td_cms_channel)";

		// String sql1 = "select channel_id from td_cms_channel";
		try {
			db.executeSelect(str);

			if (db.getInt(0, 0) > 0) {
				db1.executeSelect("select distinct channel_id,name from td_cms_channel");
				for (int i = 0; i < db1.size(); i++) {
					Channel channel = new Channel();
					String id = String.valueOf(db1.getInt(i, "channel_id")).toString();
					channel.setChannelId(Long.parseLong(id));
					channel.setName(db1.getString(i, "name"));
					list.add(channel);
				}
			} else {
				db1.executeSelect(sql);
				for (int i = 0; i < db1.size(); i++) {
					Channel channel = new Channel();
					channel.setChannelId(db1.getLong(i, "res_id"));
					list.add(channel);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}
		return list;

	}

	public List getAllChnlList() throws ChannelManagerException {
		DBUtil db = new DBUtil();
		List list = new ArrayList();
		String sql = "select distinct channel_id,name,display_name from td_cms_channel";
		try {
			db.executeSelect(sql);
			for (int i = 0; i < db.size(); i++) {
				Channel channel = new Channel();
				String id = String.valueOf(db.getInt(i, "channel_id")).toString();
				channel.setChannelId(Long.parseLong(id));
				channel.setName(db.getString(i, "name"));
				channel.setDisplayName(db.getString(i, "display_name"));
				list.add(channel);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}
		return list;
	}

	public String getChannelAbsolutePath(String channelId) throws ChannelManagerException {
		if (channelId == null || channelId.trim().length() == 0) {
			return null;
		}
		Channel channel = this.getChannelInfo(channelId);
		if (channel == null) {
			return null;
		}
		long siteid = channel.getSiteId();
		// 因为频道不能不属于任何站点
		if (siteid == 0) {
			return null;
		}
		String channelpath = channel.getChannelPath();
		if (channelpath == null || channelpath.trim().length() == 0) {
			return null;
		}
		SiteManager sm = new SiteManagerImpl();
		String sitepath = null;
		try {
			sitepath = sm.getSiteAbsolutePath("" + siteid);
		} catch (SiteManagerException e) {
			e.printStackTrace();
			throw new ChannelManagerException("获取站点路径时发生异常。异常信息为：" + e.getMessage());
		}
		if (sitepath == null || sitepath.trim().length() == 0) {
			return null;
		}
		File f = new File(sitepath, "/_webprj");
		f = new File(f.getAbsolutePath(), channelpath);
		return f.getAbsolutePath();
	}

	/**
	 * 频道是否设置了细览模板
	 * 
	 * @param channalId
	 * @return
	 * @throws ChannelManagerException
	 */
	public boolean hasSetDetailTemplate(String channalId) throws ChannelManagerException {
		String sql = "select a.site_id as site_id, b.templatefilename as templatefilename,b.templatepath as templatepath "
				+ "from td_cms_channel a "
				+ "inner join td_cms_template b "
				+ "on a.DETAIL_TPL_ID = b.template_id and b.type = '2' "
				+ "where to_char(a.channel_id)='"
				+ channalId
				+ "'";
		DBUtil db = new DBUtil();
		SiteManager sm = new SiteManagerImpl();
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				String templatefilename = db.getString(0, "templatefilename");
				String templatepath = db.getString(0, "templatepath");
				int site_id = db.getInt(0, "site_id");
				String path = sm.getSiteAbsolutePath(site_id + "");

				path = path + "\\_template\\" + templatepath + "\\" + templatefilename;

				path = path.replaceAll("\\\\", "/");

				return new File(path).exists();

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}
		return false;
	}

	/**
	 * 频道是否设置了概览模板
	 * 
	 * @param channalId
	 * @return
	 * @throws ChannelManagerException
	 */
	public boolean hasSetOutlineTemplate(String channalId) throws ChannelManagerException {
		String sql = "select 1 from td_cms_channel a " + "inner join td_cms_template b "
				+ "on a.OUTLINE_TPL_ID = b.template_id and b.type = '1' " + "where to_char(a.channel_id)='" + channalId
				+ "'";
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}
		return false;
	}

	/**
	 * 取得指定站点下所有被表示为必须导航的频道
	 * 
	 * @param siteId
	 * @param parent_channel
	 * @return
	 * @throws Exception
	 */
	public List getNavigatChannel(String siteId, String channelName) throws Exception {
		List list = new ArrayList();
		// StringBuffer sqlstr = new StringBuffer();
		/*
		 * CHANNEL_ID,NAME,DISPLAY_NAME,PARENT_ID,"+ "CHNL_PATH,CREATEUSER,CREATETIME,ORDER_NO,SITE_ID,"+
		 * "STATUS,OUTLINE_TPL_ID,DETAIL_TPL_ID, CHANNEL_FLOW_ID(channel_id) WORKFLOW," +
		 * "CHNL_OUTLINE_DYNAMIC,DOC_DYNAMIC,CHNL_OUTLINE_PROTECT,"+
		 * "DOC_PROTECT,PARENT_WORKFLOW,pub_file_name,ISNAVIGATOR,NAVIGATORLEVEL,MOUSEINIMAGE, " + "MOUSEOUTIMAGE,
		 * MOUSECLICKIMAGE, MOUSEUPIMAGE,OUTLINEPICTURE,PAGEFLAG,INDEXPAGEPATH
		 */
		/* 需要得到 频道id 频道名称 频道链接地址 */
		ChannelCacheManager cm = (ChannelCacheManager) SiteCacheManager.getInstance().getChannelCacheManager(siteId);
		if (channelName.length() <= 0) {
			list = cm.getSubNavigatorChlsByDisplayName("0");
			/*
			 * sqlstr.append("select CHANNEL_ID,DISPLAY_NAME,CHNL_PATH,MOUSEINIMAGE, "); sqlstr.append(
			 * "MOUSEOUTIMAGE,MOUSECLICKIMAGE,MOUSEUPIMAGE,PUB_FILE_NAME,navigatorlevel,name,PAGEFLAG,INDEXPAGEPATH
			 * "); sqlstr.append(" from TD_CMS_CHANNEL where site_id ="+ siteId);
			 * sqlstr.append(" and navigatorlevel=0 "); sqlstr.append("and ISNAVIGATOR=1 order by order_no");
			 */
		} else {
			list = cm.getSubNavigatorChlsByDisplayName(channelName);
			/*
			 * Channel chl = this.getChannelInfoByDisplayName(siteId,channelName); long channelId = 0; if(chl!=null)
			 * channelId = chl.getChannelId(); sqlstr.append("select * from ("); sqlstr.append("select
			 * CHANNEL_ID,DISPLAY_NAME,CHNL_PATH,MOUSEINIMAGE, ");
			 * sqlstr.append("MOUSEOUTIMAGE,MOUSECLICKIMAGE,MOUSEUPIMAGE,PUB_FILE_NAME,navigatorlevel,name
			 * "); sqlstr.append(" ,level as a,PAGEFLAG,INDEXPAGEPATH from TD_CMS_CHANNEL start with channel_id=");
			 * sqlstr.append(channelId); sqlstr.append(" connect by prior channel_id=parent_id "); sqlstr.append(" and
			 * level=2 and ISNAVIGATOR=1 and site_id="+siteId+" order by order_no)abc where abc.a>1 ");
			 */

		}
		// System.out.println(sqlstr.toString());
		/*
		 * String sql = sqlstr.toString(); DBUtil db = new DBUtil(); try { db.executeSelect(sql); for(int
		 * i=0;i<db.size();i++){ Channel channel = new Channel(); channel.setChannelId(db.getLong(i,0));
		 * channel.setDisplayName(db.getString(i,1)); channel.setChannelPath(db.getString(i,2));
		 * channel.setMouseInImage(db.getString(i,3)); channel.setMouseOutImage(db.getString(i,4));
		 * channel.setMouseClickImage(db.getString(i,5)); channel.setMouseUpImage(db.getString(i,6));
		 * channel.setPubFileName(db.getString(i,"PUB_FILE_NAME"));
		 * channel.setNavigatorLevel(db.getInt(i,"navigatorlevel"));
		 * 
		 * channel.setPageflag(db.getInt(i,"PAGEFLAG")); channel.setIndexpagepath(db.getString(i,"INDEXPAGEPATH"));
		 * channel.setName(db.getString(i,"name")); list.add(channel); } } catch (Exception e) { e.printStackTrace();
		 * throw new ChannelManagerException(e.getMessage()); }
		 */
		return list;
	}

	/**
	 * 判断是否有子频道
	 */
	public boolean hasChildChannel(Channel channel) throws Exception {
		boolean flag = false;
		long channel_id = channel.getChannelId();
		String sqlstr = "select 1 from td_cms_channel t where t.parent_id=" + channel_id;
		DBUtil db = new DBUtil();
		db.executeSelect(sqlstr);
		if (db.size() > 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 判断是否有子频道
	 */
	public boolean hasNavChildChannel(Channel channel) throws Exception {
		boolean flag = false;
		long channel_id = channel.getChannelId();
		String sqlstr = "select 1 from td_cms_channel t where t.parent_id=" + channel_id + " and t.isnavigator=1";
		DBUtil db = new DBUtil();
		db.executeSelect(sqlstr);
		if (db.size() > 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 取得子频道 递归在调用的函数中去做
	 */
	public List getChildChannel(Channel channel, int levelDegree) throws Exception {
		List list = new ArrayList();
		/* 第一次已经打印处理 */
		long channel_id = channel.getChannelId();
		StringBuffer sqlstr = new StringBuffer();
		sqlstr.append("select CHANNEL_ID,DISPLAY_NAME,CHNL_PATH,MOUSEINIMAGE, ");
		sqlstr.append("MOUSEOUTIMAGE,MOUSECLICKIMAGE,MOUSEUPIMAGE,ISNAVIGATOR,navigatorlevel,pub_file_name,PAGEFLAG,INDEXPAGEPATH ");
		sqlstr.append(" from td_cms_channel where parent_id=");
		sqlstr.append(channel_id);
		sqlstr.append(" and isnavigator=1");
		DBUtil db = new DBUtil();
		// String sql = sqlstr.toString();
		db.executeSelect(sqlstr.toString());
		for (int i = 0; i < db.size(); i++) {
			if (db.getInt(i, "navigatorlevel") <= levelDegree) {
				Channel subchannel = new Channel();
				subchannel.setChannelId(db.getLong(i, 0));
				subchannel.setDisplayName(db.getString(i, 1));
				subchannel.setChannelPath(db.getString(i, 2));
				subchannel.setMouseInImage(db.getString(i, 3));
				subchannel.setMouseOutImage(db.getString(i, 4));
				subchannel.setMouseClickImage(db.getString(i, 5));
				subchannel.setMouseUpImage(db.getString(i, 6));
				subchannel.setNavigator(true);
				String[] pubFileNames = StringOperate.getFileNameAndExtName(db.getString(i, "pub_file_name"));
				subchannel.setPubFileName(db.getString(i, "pub_file_name"));
				subchannel.setPubFileNameSuffix(pubFileNames[1]);
				channel.setPageflag(db.getInt(i, "PAGEFLAG"));
				channel.setIndexpagepath(db.getString(i, "INDEXPAGEPATH"));
				list.add(subchannel);
			}
		}
		return list;
	}

	/**
	 * get all sub channels of next level --------edit by kai.hu
	 * 
	 * @param channelId
	 * @return
	 * @throws ChannelManagerException
	 */
	public List getDirectSubNaviChannels(String channelId) throws ChannelManagerException {
		if (channelId == null || channelId.trim().length() == 0) {
			throw new ChannelManagerException("没有提供频道id,无法返回子频道列表.");
		}
		try {
			DBUtil db = new DBUtil();
			String sql = "select CHANNEL_ID, NAME,DISPLAY_NAME, "
					+ "PARENT_ID,CHNL_PATH,CREATEUSER,CREATETIME, "
					+ "ORDER_NO,SITE_ID,STATUS,OUTLINE_TPL_ID, "
					+ "DETAIL_TPL_ID, CHANNEL_FLOW_ID(channel_id) WORKFLOW,"
					+ "CHNL_OUTLINE_DYNAMIC,DOC_DYNAMIC, "
					+ "CHNL_OUTLINE_PROTECT, DOC_PROTECT,"
					+ "PARENT_WORKFLOW,ISNAVIGATOR,NAVIGATORLEVEL,MOUSEINIMAGE, MOUSEOUTIMAGE, MOUSECLICKIMAGE,"
					+ " MOUSEUPIMAGE,OUTLINEPICTURE, PAGEFLAG, INDEXPAGEPATH, COMMENTSWITCH,  COMMENT_TEMPLATE_ID,"
					+ " COMMENTPAGEPATH,channel_desc from TD_CMS_CHANNEL where PARENT_ID!=0 and PARENT_ID is not null and status=0 and PARENT_ID="
					+ channelId + " and ISNAVIGATOR=1 order by order_no,channel_id";
			// System.out.println("sql=" + sql);
			db.executeSelect(sql);
			List channels = new ArrayList();
			for (int i = 0; i < db.size(); i++) {
				Channel channel = new Channel();
				channel.setChannelId(db.getLong(i, "CHANNEL_ID"));
				channel.setName(db.getString(i, "NAME"));
				channel.setDisplayName(db.getString(i, "DISPLAY_NAME"));
				channel.setParentChannelId(db.getInt(i, "PARENT_ID"));
				channel.setChannelPath(db.getString(i, "CHNL_PATH"));
				channel.setCreateUser(db.getLong(i, "CREATEUSER"));
				channel.setCreateTime(db.getDate(i, "CREATETIME"));
				channel.setOrderNo(db.getInt(i, "ORDER_NO"));
				channel.setSiteId(db.getLong(i, "SITE_ID"));
				channel.setStaus(db.getInt(i, "STATUS"));
				channel.setOutlineTemplateId(db.getInt(i, "OUTLINE_TPL_ID"));
				channel.setDetailTemplateId(db.getInt(i, "DETAIL_TPL_ID"));
				channel.setWorkflow(db.getInt(i, "WORKFLOW"));
				channel.setOutlineIsDynamic(db.getInt(i, "CHNL_OUTLINE_DYNAMIC"));
				channel.setDocIsDynamic(db.getInt(i, "DOC_DYNAMIC"));
				channel.setOutlineIsProtect(db.getInt(i, "CHNL_OUTLINE_PROTECT"));
				channel.setDocIsProtect(db.getInt(i, "DOC_PROTECT"));
				channel.setWorkflowIsFromParent(db.getInt(i, "PARENT_WORKFLOW"));
				channel.setNavigator(0 != db.getInt(i, "ISNAVIGATOR"));
				channel.setNavigatorLevel(db.getInt(i, "NAVIGATORLEVEL"));
				channel.setMouseInImage(db.getString(i, "MOUSEINIMAGE"));
				channel.setMouseOutImage(db.getString(i, "MOUSEOUTIMAGE"));
				channel.setMouseClickImage(db.getString(i, "MOUSECLICKIMAGE"));
				channel.setMouseUpImage(db.getString(i, "MOUSEUPIMAGE"));
				channel.setOutlinepicture(db.getString(i, "OUTLINEPICTURE"));
				channel.setPageflag(db.getInt(i, "PAGEFLAG"));
				channel.setIndexpagepath(db.getString(i, "INDEXPAGEPATH"));
				channel.setCommentTemplateId(db.getInt(i, "COMMENT_TEMPLATE_ID"));
				channel.setCommentPagePath(db.getString(i, "COMMENTPAGEPATH"));
				channel.setChannel_desc(db.getString(i,"channel_desc"));	
				channels.add(channel);
			}
			return channels;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("发生sql异常,无法返回频道信息.异常信息为:" + e.getMessage());
		}
	}

	/**
	 * 取得该频道下所有有权限的用户id
	 * 
	 * @author xinwang.jiao
	 * @param channelId
	 * @return String[] String[0] 为userids,String[1] 为usernames，String[2]
	 *         为“user”
	 * @throws ChannelManagerException
	 */
	public String[] getUsersOfChl(String channelId) throws ChannelManagerException {
		String[] str = new String[3];
		String userIds = "";
		String userNames = "";
		String userOrRoleOrOrg = "";
		DBUtil db = new DBUtil();
		String sql = "select distinct a.role_id,b.user_realname from td_sm_roleresop a "
				+ "inner join td_sm_user b on a.role_id = b.user_id "
				+ "where a.types='user' and (a.restype_id = 'channel' or a.restype_id = 'channeldoc') and a.res_id = '"
				+ channelId + "'";
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					userIds += db.getString(i, "role_id") + ",";
					userNames += db.getString(i, "user_realname") + ",";
					userOrRoleOrOrg += "user" + ",";
				}
			}
			str[0] = userIds;
			str[1] = userNames;
			str[2] = userOrRoleOrOrg;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}
		return str;
	}

	/**
	 * 取得该站点下所有有权限的角色id
	 * 
	 * @author xinwang.jiao
	 * @param channelId
	 * @return String[] String[0] 为角色id,String[1] 为角色名称，String[2] 为“role”
	 * @throws SiteManagerException
	 */
	public String[] getRolesOfChl(String channelId) throws ChannelManagerException {
		String[] str = new String[3];
		String userIds = "";
		String userNames = "";
		String userOrRoleOrOrg = "";
		DBUtil db = new DBUtil();
		String sql = "select distinct a.role_id,b.role_name from td_sm_roleresop a "
				+ "inner join td_sm_role b on a.role_id = b.role_id "
				+ "where (a.restype_id = 'channel' or a.restype_id = 'channeldoc') "
				+ " and a.types='role' and a.res_id = '" + channelId + "'";
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					userIds += db.getString(i, "role_id") + ",";
					userNames += db.getString(i, "role_name") + ",";
					userOrRoleOrOrg += "role" + ",";
				}
			}
			str[0] = userIds;
			str[1] = userNames;
			str[2] = userOrRoleOrOrg;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}
		return str;
	}

	public String[] getOrgsOfChl(String channelId) throws ChannelManagerException {
		String[] str = new String[3];
		String orgIds = "";
		String orgNames = "";
		String userOrRoleOrOrg = "";
		DBUtil db = new DBUtil();
		String sql = "select distinct a.role_id,b.org_name from td_sm_roleresop a "
				+ "inner join td_sm_organization b on a.role_id = b.org_id "
				+ "where (a.restype_id = 'channel' or a.restype_id = 'channeldoc') "
				+ " and a.types='organization' and a.res_id = '" + channelId + "'";
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					orgIds += db.getString(i, "role_id") + ",";
					orgNames += db.getString(i, "org_name") + ",";
					userOrRoleOrOrg += "organization" + ",";
				}
			}
			str[0] = orgIds;
			str[1] = orgNames;
			str[2] = userOrRoleOrOrg;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}
		return str;
	}

	/**
	 * Update时判断是否已存在该频道显示名称（DisplayName） 频道显示名称唯一性判断
	 * 
	 * @author xinwang.jiao
	 * @param String
	 *            DisplayName
	 * @param long
	 *            chlId
	 * @return boolean (存在返回true，不存在返回false)
	 * @throws ChannelManagerException
	 */
	public boolean hasSameDisplayNameForUpdate(String siteid, String DisplayName, long chlId)
			throws ChannelManagerException {
		boolean flag = false;
		try {
			String sql = "select t.display_name from td_cms_channel t " + "where t.display_name = '" + DisplayName
					+ "' and t.channel_id != " + chlId + " and t.site_id = " + siteid;
			DBUtil db = new DBUtil();

			db.executeSelect(sql);

			if (db.size() > 0)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}
		return flag;
	}

	/**
	 * 判断是否已存在该频道显示名称（DisplayName） 频道显示名称唯一性判断
	 * 
	 * @author xinwang.jiao
	 * @param String
	 *            DisplayName
	 * @return boolean (存在返回true，不存在返回false)
	 * @throws ChannelManagerException
	 */
	public boolean hasSameDisplayName(String siteid, String DisplayName) throws ChannelManagerException {
		boolean flag = false;
		try {
			String sql = "select t.display_name from td_cms_channel t " + "where t.display_name = '" + DisplayName
					+ "' and t.site_id = " + siteid;
			DBUtil db = new DBUtil();

			db.executeSelect(sql);

			if (db.size() > 0)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}
		return flag;
	}

	/**
	 * 根据频道显示名称获取该频道的信息
	 * 
	 * @author xinwang.jiao
	 * @param String
	 *            DisplayName
	 * @return <Channel>
	 * @throws ChannelManagerException
	 */
	public Channel getChannelInfoByDisplayName(String siteid, String DisplayName) throws ChannelManagerException {
		if (DisplayName == null || DisplayName.trim().length() == 0) {
			throw new ChannelManagerException("没有提供频道id,无法返回频道信息.");
		}
		try {
			DBUtil db = new DBUtil();
			String sql = "select CHANNEL_ID,NAME,DISPLAY_NAME,PARENT_ID,"
					+ "CHNL_PATH,CREATEUSER,CREATETIME,ORDER_NO,SITE_ID,"
					+ "STATUS,OUTLINE_TPL_ID,DETAIL_TPL_ID, CHANNEL_FLOW_ID(channel_id) WORKFLOW,"
					+ "CHNL_OUTLINE_DYNAMIC,DOC_DYNAMIC,CHNL_OUTLINE_PROTECT,"
					+ "DOC_PROTECT,PARENT_WORKFLOW,pub_file_name,ISNAVIGATOR,NAVIGATORLEVEL,MOUSEINIMAGE, "
					+ "MOUSEOUTIMAGE, MOUSECLICKIMAGE, MOUSEUPIMAGE,OUTLINEPICTURE, PAGEFLAG, INDEXPAGEPATH, COMMENTSWITCH,  COMMENT_TEMPLATE_ID, COMMENTPAGEPATH,channel_desc "
					+ "from TD_CMS_CHANNEL " + "where status=0 and display_name = '" + DisplayName + "' and site_id = "
					+ siteid;
			log.warn(sql);
			db.executeSelect(sql);
			if (db.size() > 0) {
				Channel channel = new Channel();
				channel.setChannelId(db.getLong(0, "CHANNEL_ID"));
				channel.setName(db.getString(0, "NAME"));
				channel.setDisplayName(db.getString(0, "DISPLAY_NAME"));
				channel.setParentChannelId(db.getInt(0, "PARENT_ID"));
				channel.setChannelPath(db.getString(0, "CHNL_PATH"));
				channel.setCreateUser(db.getLong(0, "CREATEUSER"));
				channel.setCreateTime(db.getDate(0, "CREATETIME"));
				channel.setOrderNo(db.getInt(0, "ORDER_NO"));
				channel.setSiteId(db.getLong(0, "SITE_ID"));
				channel.setStaus(db.getInt(0, "STATUS"));
				channel.setOutlineTemplateId(db.getInt(0, "OUTLINE_TPL_ID"));
				channel.setDetailTemplateId(db.getInt(0, "DETAIL_TPL_ID"));
				channel.setWorkflow(db.getInt(0, "WORKFLOW"));
				channel.setOutlineIsDynamic(db.getInt(0, "CHNL_OUTLINE_DYNAMIC"));
				channel.setDocIsDynamic(db.getInt(0, "DOC_DYNAMIC"));
				channel.setOutlineIsProtect(db.getInt(0, "CHNL_OUTLINE_PROTECT"));
				channel.setDocIsProtect(db.getInt(0, "DOC_PROTECT"));
				channel.setWorkflowIsFromParent(db.getInt(0, "PARENT_WORKFLOW"));
				String[] pubFileNames = StringOperate.getFileNameAndExtName(db.getString(0, "pub_file_name"));
				channel.setPubFileName(db.getString(0, "pub_file_name"));
				channel.setPubFileNameSuffix(pubFileNames[1]);
				channel.setNavigator(0 != db.getInt(0, "ISNAVIGATOR"));
				channel.setNavigatorLevel(db.getInt(0, "NAVIGATORLEVEL"));
				channel.setMouseInImage(db.getString(0, "MOUSEINIMAGE"));
				channel.setMouseOutImage(db.getString(0, "MOUSEOUTIMAGE"));
				channel.setMouseClickImage(db.getString(0, "MOUSECLICKIMAGE"));
				channel.setMouseUpImage(db.getString(0, "MOUSEUPIMAGE"));
				channel.setOutlinepicture(db.getString(0, "OUTLINEPICTURE"));
				channel.setPageflag(db.getInt(0, "PAGEFLAG"));
				channel.setIndexpagepath(db.getString(0, "INDEXPAGEPATH"));
				channel.setCommentTemplateId(db.getInt(0, "COMMENT_TEMPLATE_ID"));
				channel.setCommentPagePath(db.getString(0, "COMMENTPAGEPATH"));
				channel.setChannel_desc(db.getString(0,"channel_desc"));	
				return channel;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("发生sql异常,无法返回频道信息.异常信息为:" + e.getMessage());
		}
	}

	/**
	 * 按 文档发布时间 排序 不翻页!!! 通过频道显示名称获取频道最近发布的相应数目的文档列表 (对于那些包含于聚合文档的文档不在其列)
	 * 最近发布的相应数目的文档列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * 
	 * @author xinwang.jiao
	 * @param String
	 *            DisplayName
	 * @param int
	 *            count（指定返回的文档数目）
	 * @return ListInfo<Document>
	 * @throws ChannelManagerException
	 */
	public List getLatestPubDocList(String siteid, String DisplayName, int count) throws ChannelManagerException {
		List list = new ArrayList();
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		StringBuffer sql = new StringBuffer();
		try {
			// sql = "select t.channel_id from td_cms_channel t " +
			// " where t.display_name = '" + DisplayName + "' and t.site_id = "
			// + siteid;
			// db.executeSelect(sql);
			// int chlId;
			ChannelCacheManager cm = (ChannelCacheManager) SiteCacheManager.getInstance()
					.getChannelCacheManager(siteid);
			long chlId = ((Channel) cm.getChannelByDisplayName(DisplayName)).getChannelId();
			// if(db.size() > 0)
			// {
			// chlId = db.getInt(0,"channel_id");
			sql.append(
					"select p.* from(select t.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, ")
					.append("DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, DOCSOURCE_ID, DETAILTEMPLATE_ID, ")
					.append("LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,")
					.append("case when DOCTYPE=1 ")
					.append("then t.content else null end linkfile,pic_path,mediapath,publishfilename,commentswitch,secondtitle,")
					.append("isnew,newpic_path,")
					.append("nvl(a.order_no,-1) as order_no,1 as ordersq ")
					.append(" from td_cms_document t ")
					.append(" left outer join (select * from td_cms_doc_arrange a ")
					.append(" where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<= ")
					.append(DBUtil.getDBAdapter().to_date(new Date()))
					.append(" and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>=")
					.append(DBUtil.getDBAdapter().to_date(new Date()))
					.append(") a ")
					.append(" on t.document_id = a.document_id ")
					.append(" where STATUS = ")
					.append(DocumentStatus.PUBLISHED.getStatus())
					.append(" and ISDELETED = 0 and t.document_id not ")
					.append(" in(select c.id_by_aggr from td_cms_doc_aggregation c ")
					.append(" inner join td_cms_document z1 on c.id_by_aggr = z1.document_id ")
					.append(" inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id = ")
					.append(chlId)
					.append(") ")
					.append(" and CHANNEL_ID =")
					.append(chlId)
					.append(" union all ")
					// 联合查询
					.append(" select c.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, ")
					.append("DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, DOCSOURCE_ID, DETAILTEMPLATE_ID, ")
					.append("LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,")
					.append("case when DOCTYPE=1 ")
					.append("then content else null end linkfile,pic_path,mediapath,publishfilename,commentswitch,secondtitle,")
					.append("isnew,newpic_path,").append("nvl(e.order_no,-1) as order_no,2 as ordersq ")
					.append(" from td_cms_document c, td_cms_chnl_ref_doc d ")
					.append(" left outer join (select * from td_cms_doc_arrange a ")
					.append(" where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<=")
					.append(DBUtil.getDBAdapter().to_date(new Date()))
					.append(" and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>=")
					.append(DBUtil.getDBAdapter().to_date(new Date())).append(") e ")
					.append(" on d.doc_id = e.document_id ").append(" where c.document_id=d.doc_id and STATUS = ")
					.append(DocumentStatus.PUBLISHED.getStatus()).append(" and ISDELETED = 0 and d.doc_id not ")
					.append(" in(select c.id_by_aggr from td_cms_doc_aggregation c ")
					.append(" inner join td_cms_document z1 on c.id_by_aggr = z1.document_id ")
					.append(" inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id = ")
					.append(chlId).append(") ").append(" and d.chnl_id =").append(chlId)
					.append(" order by order_no desc,publishtime desc) p ")
					.append(((count < 1) ? "" : "where rownum <=" + count));// 如果count为-1默认查询所有的记录
			// System.out.println(sql);
			db.executeSelect(sql.toString());
			Document doc;

			for (int i = 0; i < db.size(); i++) {
				doc = new Document();
				// 处理引用频道
				// add by ge.tao
				// 2007-09-14
				if (db.getInt(i, "DOCTYPE") == Document.DOCUMENT_CHANNEL) {
					String channelid = String.valueOf(db.getLong(i, "document_id"));
					Channel refChannel = new ChannelManagerImpl().getChannelInfo(channelid);
					// doc.setRefChannel(refChannel);
					// doc.setDoctype(Document.DOCUMENT_CHANNEL);
					list.add(refChannel);
				} else {
					doc.setDocument_id(db.getInt(i, "DOCUMENT_ID"));
					doc.setTitle(db.getString(i, "TITLE"));
					doc.setSubtitle(db.getString(i, "SUBTITLE"));
					doc.setAuthor(db.getString(i, "AUTHOR"));
					doc.setChanel_id(db.getInt(i, "CHANNEL_ID"));
					doc.setKeywords(db.getString(i, "KEYWORDS"));
					doc.setDocabstract(db.getString(i, "DOCABSTRACT"));
					doc.setDoctype(db.getInt(i, "DOCTYPE"));
					doc.setDocwtime(db.getDate(i, "DOCWTIME"));
					doc.setTitlecolor(db.getString(i, "TITLECOLOR"));
					doc.setCreateTime(db.getDate(i, "CREATETIME"));
					doc.setCreateUser(db.getInt(i, "CREATEUSER"));
					doc.setDocsource_id(db.getInt(i, "DOCSOURCE_ID"));
					doc.setDetailtemplate_id(db.getInt(i, "DETAILTEMPLATE_ID"));
					doc.setLinktarget(db.getString(i, "LINKTARGET"));
					doc.setLinkfile(db.getString(i, "linkfile"));
					doc.setFlowId(db.getInt(i, "FLOW_ID"));
					doc.setDoc_level(db.getInt(i, "DOC_LEVEL"));
					doc.setDoc_kind(db.getInt(i, "DOC_KIND"));
					doc.setParentDetailTpl(db.getString(i, "PARENT_DETAIL_TPL"));
					doc.setPublishTime(db.getDate(i, "publishtime"));
					doc.setPicPath(db.getString(i, "pic_path"));

					doc.setMediapath(db.getString(i, "mediapath"));
					doc.setPublishfilename(db.getString(i, "publishfilename"));
					// doc.setc(db.getString(i,"commentswitch"));
					doc.setSecondtitle(db.getString(i, "secondtitle"));
					doc.setIsNew(db.getInt(i, "isnew"));
					doc.setNewPicPath(db.getString(i, "newpic_path"));

					String str = "select SRCNAME from TD_CMS_DOCSOURCE where DOCSOURCE_ID ="
							+ db.getInt(i, "DOCSOURCE_ID") + "";

					db1.executeSelect(str);
					if (db1.size() > 0) {
						doc.setDocsource_name(db1.getString(0, "SRCNAME"));
					}
					int isref = db.getInt(i, "ordersq");
					doc.setRef(isref == 1 ? false : true);// 判断是否是引用的文档：true为是，flase为不是
					/* 装载扩展字段数据 */
					Map docExtField = (new DocumentManagerImpl()).getDocExtFieldMap(doc.getDocument_id() + "");
					doc.setDocExtField(docExtField);
					/* 装载系统扩展字段数据 */
					doc.setExtColumn(extManager.getExtColumnInfo(i,db));
					list.add(doc);
				}

			}
			// }

		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}

		return list;
	}

	/**
	 * 按 文档发布时间 排序 不翻页!!! 通过频道显示名称获取频道最近发布的相应数目的文档列表 包括那些文档状态为“正在发布中”的文档
	 * (对于那些包含于聚合文档的文档不在其列) 最近发布的相应数目的文档列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * 
	 * @author xinwang.jiao
	 * @param String
	 *            DisplayName
	 * @param int
	 *            count（指定返回的文档数目）
	 * @return List<Document>
	 * @throws ChannelManagerException
	 *             add by xinwang.jiao 2007.6.11
	 * 
	 */
	public List getLatestPubAndPubingDocList(String siteid, String DisplayName, int count)
			throws ChannelManagerException {
		return this.getLatestPubAndPubingDocList(siteid, DisplayName, 0, count).getDatas();
		// List list = new ArrayList();
		// DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		// DBUtil db = new DBUtil();
		// DBUtil db1 = new DBUtil();
		// StringBuffer sql = new StringBuffer();
		// try {
		// // sql = "select t.channel_id from td_cms_channel t " +
		// // " where t.display_name = '" + DisplayName + "' and t.site_id = "
		// // + siteid;
		// // db.executeSelect(sql);
		// // int chlId;
		// ChannelCacheManager cm = (ChannelCacheManager) SiteCacheManager
		// .getInstance().getChannelCacheManager(siteid);
		// long chlId = ((Channel) cm.getChannelByDisplayName(DisplayName))
		// .getChannelId();
		// // if(db.size() > 0)
		// // {
		// // chlId = db.getInt(0,"channel_id");
		// sql.append("select p.* from(select t.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, ")
		// .append("DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, DOCSOURCE_ID, DETAILTEMPLATE_ID, ")
		// .append("LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,")
		// .append("case when DOCTYPE=1 ")
		// .append("then t.content else null end linkfile,pic_path,mediapath,publishfilename,commentswitch,secondtitle,")
		// .append("isnew,newpic_path,")
		// .append("nvl(a.order_no,-1) as order_no,1 as ordersq,ordertime ")
		// .append(" from td_cms_document t ")
		// .append(" left outer join (select * from td_cms_doc_arrange a ")
		// .append(" where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<= ")
		// .append(DBUtil.getDBAdapter().sysdate())
		// .append(" and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>=")
		// .append(DBUtil.getDBAdapter().sysdate())
		// .append(") a ")
		// .append(" on t.document_id = a.document_id ")
		// .append(" where (STATUS = ")
		// .append(DocumentStatus.PUBLISHED.getStatus())
		// .append(" or STATUS = ")
		// .append(DocumentStatus.PUBLISHING.getStatus())
		// .append(") ")
		// .append(" and ISDELETED = 0 and t.document_id not ")
		// .append(" in(select c.id_by_aggr from td_cms_doc_aggregation c ")
		// .append(" inner join td_cms_document z1 on c.id_by_aggr = z1.document_id ")
		// .append(" inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id = ")
		// .append(chlId)
		// .append(") ")
		// .append(" and CHANNEL_ID =")
		// .append(chlId)
		// .append(" union all ")// 联合查询
		// .append(" select c.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, ")
		// .append("DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, DOCSOURCE_ID, DETAILTEMPLATE_ID, ")
		// .append("LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,")
		// .append("case when DOCTYPE=1 ")
		// .append("then content else null end linkfile,pic_path,mediapath,publishfilename,commentswitch,secondtitle,")
		// .append("isnew,newpic_path,")
		// .append("nvl(e.order_no,-1) as order_no,2 as ordersq ,ordertime")
		// .append(" from td_cms_document c, td_cms_chnl_ref_doc d ")
		// .append(" left outer join (select * from td_cms_doc_arrange a ")
		// .append(" where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<=")
		// .append(DBUtil.getDBAdapter().sysdate())
		// .append(" and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>=")
		// .append(DBUtil.getDBAdapter().sysdate())
		// .append(") e ")
		// .append(" on d.doc_id = e.document_id ")
		// .append(" where c.document_id=d.doc_id and ")
		// .append(" (STATUS = ")
		// .append(DocumentStatus.PUBLISHED.getStatus())
		// .append(" or STATUS = ")
		// .append(DocumentStatus.PUBLISHING.getStatus())
		// .append(") ")
		// .append(" and ISDELETED = 0 and d.doc_id not ")
		// .append(" in(select c.id_by_aggr from td_cms_doc_aggregation c ")
		// .append(" inner join td_cms_document z1 on c.id_by_aggr = z1.document_id ")
		// .append(" inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id = ")
		// .append(chlId)
		// .append(") ")
		// .append(" and d.chnl_id =")
		// .append(chlId)
		// .append(" order by order_no desc,publishtime desc) p ")
		// .append(((count < 1) ? "" : "where rownum <=" + count));// 如果count为-1默认查询所有的记录
		// //System.out.println(sql.toString());
		// db.executeSelect(sql.toString());
		// Document doc;
		//
		// for (int i = 0; i < db.size(); i++) {
		// doc = new Document();
		// //处理引用频道
		// //add by ge.tao
		// //2007-09-14
		// if(db.getInt(i,"DOCTYPE")==Document.DOCUMENT_CHANNEL){
		// String channelid = String.valueOf(db.getLong(i, "DOCUMENT_ID"));
		// Channel refChannel = new ChannelManagerImpl().getChannelInfo(channelid);
		// // doc.setRefChannel(refChannel);
		// // doc.setDoctype(Document.DOCUMENT_CHANNEL);
		// list.add(refChannel);
		// }else{
		// doc.setDocument_id(db.getInt(i, "DOCUMENT_ID"));
		// doc.setTitle(db.getString(i, "TITLE"));
		// doc.setSubtitle(db.getString(i, "SUBTITLE"));
		// doc.setAuthor(db.getString(i, "AUTHOR"));
		// doc.setChanel_id(db.getInt(i, "CHANNEL_ID"));
		// doc.setKeywords(db.getString(i, "KEYWORDS"));
		// doc.setDocabstract(db.getString(i, "DOCABSTRACT"));
		// doc.setDoctype(db.getInt(i, "DOCTYPE"));
		// doc.setDocwtime(db.getDate(i, "DOCWTIME"));
		// doc.setTitlecolor(db.getString(i, "TITLECOLOR"));
		// doc.setCreateTime(db.getDate(i, "CREATETIME"));
		// doc.setCreateUser(db.getInt(i, "CREATEUSER"));
		// doc.setDocsource_id(db.getInt(i, "DOCSOURCE_ID"));
		// doc.setDetailtemplate_id(db.getInt(i, "DETAILTEMPLATE_ID"));
		// doc.setLinktarget(db.getString(i, "LINKTARGET"));
		// doc.setLinkfile(db.getString(i, "linkfile"));
		// doc.setFlowId(db.getInt(i, "FLOW_ID"));
		// doc.setDoc_level(db.getInt(i, "DOC_LEVEL"));
		// doc.setDoc_kind(db.getInt(i, "DOC_KIND"));
		// doc.setParentDetailTpl(db.getString(i, "PARENT_DETAIL_TPL"));
		// doc.setPublishTime(db.getDate(i, "publishtime"));
		// doc.setPicPath(db.getString(i, "pic_path"));
		//
		// doc.setMediapath(db.getString(i, "mediapath"));
		// doc.setPublishfilename(db.getString(i, "publishfilename"));
		// // doc.setc(db.getString(i,"commentswitch"));
		// doc.setSecondtitle(db.getString(i, "secondtitle"));
		// doc.setIsNew(db.getInt(i, "isnew"));
		// doc.setNewPicPath(db.getString(i, "newpic_path"));
		//
		// String str = "select SRCNAME from TD_CMS_DOCSOURCE where DOCSOURCE_ID ="
		// + db.getInt(i, "DOCSOURCE_ID") + "";
		//
		// db1.executeSelect(str);
		// if (db1.size() > 0) {
		// doc.setDocsource_name(db1.getString(0, "SRCNAME"));
		// }
		// int isref = db.getInt(i, "ordersq");
		// doc.setRef(isref == 1 ? false : true);// 判断是否是引用的文档：true为是，flase为不是
		// /* 装载扩展字段数据 */
		// Map docExtField = (new DocumentManagerImpl())
		// .getDocExtFieldMap(doc.getDocument_id() + "");
		// doc.setDocExtField(docExtField);
		// /* 装载系统扩展字段数据 */
		// doc.setExtColumn(extManager.getExtColumnInfo(db));
		// list.add(doc);
		// }
		//
		// }
		// // }
		//
		// } catch (Exception e) {
		// e.printStackTrace();
		// throw new ChannelManagerException(e.getMessage());
		// }

		// return list;
	}
	public List getLatestPubDocListOrderByDocwtime(String siteid,String DisplayName,int count,String docType,boolean loaddocrelatepic) throws ChannelManagerException
	{
		return getLatestPubDocListOrderByDocwtime(siteid,DisplayName,count,docType, loaddocrelatepic, false);
	}
	public List getLatestPubDocListOrderByDocwtime(String siteid,String DisplayName,int count,Map params) throws ChannelManagerException
	{
		boolean loaddocrelatepic = this.getBooleanParam(params,"loadrelatepic",false);
		boolean loadcontent = this.getBooleanParam(params,"loadcontent",false);
		String docType = params != null?(String)params.get("doctype"):null;
		return getLatestPubDocListOrderByDocwtime(siteid,DisplayName,count,docType, loaddocrelatepic, loadcontent);
	}
	public List getLatestPubDocListOrderByDocwtime(String siteid,String DisplayName,int count,String docType,boolean loaddocrelatepic,boolean loadcontent) throws ChannelManagerException
	
	{
		List list = new ArrayList();
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		StringBuffer sql = new StringBuffer();
		TransactionManager tm = new TransactionManager(); 
		try {
			// sql = "select t.channel_id from td_cms_channel t " +
			// " where t.display_name = '" + DisplayName + "' and t.site_id = "
			// + siteid;
			// db.executeSelect(sql);
			// int chlId;
			ChannelCacheManager cm = (ChannelCacheManager) SiteCacheManager.getInstance()
					.getChannelCacheManager(siteid);
			Channel channel = ((Channel) cm.getChannelByDisplayName(DisplayName));
			if (channel == null) {
				System.out
						.println("站点[siteid=" + siteid + "]中显示名称为[DisplayName=" + DisplayName + "]的频道不存在，获取概览列表数据失败。");
				return list;
			}
			long chlId = ((Channel) cm.getChannelByDisplayName(DisplayName)).getChannelId();
			// if(db.size() > 0)
			// {
			// chlId = db.getInt(0,"channel_id");
			sql.append("select p.* from(")
					// 获取频道本身文档开始，置顶的文档排在最前面，其他的按编稿时间排序
					.append("select t.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, ")
					.append("DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, t.DOCSOURCE_ID,nvl(ds.srcname,'未知') as source_name, DETAILTEMPLATE_ID, ")
					.append("LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,")
					.append("case when DOCTYPE=1 ")
					.append("then t.content else null end linkfile,")
					.append(loadcontent?"case when DOCTYPE<>1 then t.content else null end content,":"null content,")					
					.append("pic_path,mediapath,publishfilename,commentswitch,secondtitle,")
					.append("isnew,newpic_path,")
					.append("nvl(a.order_no,-1) as order_no,1 as ordersq,ordertime,seq,-1 site_id,ext_wh,ext_class,ext_index,ext_org,ext_djh ")
					.append(" from td_cms_document t ")
					.append(" left outer join (select * from td_cms_doc_arrange a ")
					.append(" where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<=")
					.append(DBUtil.getDBAdapter().to_date(new Date()))
					.append(" and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>=")
					.append(DBUtil.getDBAdapter().to_date(new Date()))
					.append(" ) a ")
					// 获取频道本身文档结束
					.append(" on t.document_id = a.document_id ")
					.append(" inner join TD_CMS_DOCSOURCE ds on t.DOCSOURCE_ID = ds.DOCSOURCE_ID")
					.append(" where STATUS in ( ").append(DocumentStatus.PUBLISHED.getStatus()).append(",")
					.append(DocumentStatus.PUBLISHING.getStatus()).append(") and ISDELETED = 0 and t.document_id not ")
					.append(" in(select c.id_by_aggr from td_cms_doc_aggregation c ")
					.append(" inner join td_cms_document z1 on c.id_by_aggr = z1.document_id ")
					.append(" inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id = ")
					.append(chlId).append(") ").append(" and CHANNEL_ID =").append(chlId);
			if (!StringUtil.isEmpty(docType)) {
//				sql.append(" and t.doc_kind = ").append(docType);// 关联文档类型
				sql.append(" and t.doc_class = '").append(docType).append("'");// 关联文档类型
			}
			sql.append(" union all ")
					// 联合查询
					.append(" select c.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, ")
					.append("DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, c.DOCSOURCE_ID,nvl(ds.srcname,'未知') as source_name, DETAILTEMPLATE_ID, ")
					.append("LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,")
					.append("case when DOCTYPE=1 ")
					.append("then content else null end linkfile,")
					.append(loadcontent?"case when DOCTYPE<>1 then c.content else null end content,":"null content,")					
					.append("pic_path,mediapath,publishfilename,commentswitch,secondtitle,")
					.append("isnew,newpic_path,")
					.append("nvl(e.order_no,-1) as order_no,2 as ordersq,ordertime,seq,d.site_id,ext_wh,ext_class,ext_index,ext_org,ext_djh ")
					.append(" from td_cms_document c,TD_CMS_DOCSOURCE ds , td_cms_chnl_ref_doc d ")
					.append(" left outer join (select * from td_cms_doc_arrange a ")
					.append(" where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<=")
					.append(DBUtil.getDBAdapter().to_date(new Date()))
					.append(" and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>=")
					.append(DBUtil.getDBAdapter().to_date(new Date()))
					.append(") e ")
					.append(" on d.doc_id = e.document_id ")

					.append(" where c.DOCSOURCE_ID = ds.DOCSOURCE_ID and d.citetype = 0 and ")
					// 查询引用文档类型为文档的引用文档，没有对引用频道的情况的处理
					.append(" c.document_id=d.doc_id and STATUS in ( ").append(DocumentStatus.PUBLISHED.getStatus())
					.append(",").append(DocumentStatus.PUBLISHING.getStatus())
					.append(") and ISDELETED = 0 and d.doc_id not ")
					.append(" in(select c.id_by_aggr from td_cms_doc_aggregation c ")
					.append(" inner join td_cms_document z1 on c.id_by_aggr = z1.document_id ")
					.append(" inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id = ")
					.append(chlId).append(") ").append(" and d.chnl_id =").append(chlId);
			if (!StringUtil.isEmpty(docType)) {
				sql.append(" and c.doc_class = '").append(docType).append("'");// 关联文档类型
			}
			sql.append(
					" union all   select t.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, DOCWTIME,")
					.append("TITLECOLOR, CREATETIME, CREATEUSER, t.DOCSOURCE_ID,nvl(ds.srcname,'未知') as source_name, DETAILTEMPLATE_ID, LINKTARGET,")
					.append("FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,case when DOCTYPE=1 then t.content else null end linkfile,")
					.append(loadcontent?"case when DOCTYPE<>1 then t.content else null end content,":"null content,")					
					.append("pic_path,")
					.append("mediapath,publishfilename,commentswitch,secondtitle,isnew,newpic_path,nvl(a.order_no,-1) as order_no,1 as ordersq,ordertime,seq,-1 site_id,ext_wh,ext_class,ext_index,ext_org,ext_djh ")
					.append("from td_cms_document t  left outer join (select * from td_cms_doc_arrange a  ")
					.append("where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<=sysdate ")
					.append("and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>=sysdate) a  ")
					.append("on t.document_id = a.document_id inner join TD_CMS_DOCSOURCE ds on t.DOCSOURCE_ID = ds.DOCSOURCE_ID where (STATUS = 5 or STATUS = 10)")
					.append("  and ISDELETED = 0 and t.document_id not  in(select c.id_by_aggr from td_cms_doc_aggregation c  inner join td_cms_document z1 on ")
					.append("  c.id_by_aggr = z1.document_id  inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id  in(select doc_id from td_cms_chnl_ref_doc where chnl_id="
							+ chlId + "))  and ")
					.append("  CHANNEL_ID in (select doc_id from td_cms_chnl_ref_doc where chnl_id=" + chlId + ") ");
			if (!StringUtil.isEmpty(docType)) {
//				sql.append(" and t.doc_kind = ").append(docType);// 关联文档类型
				sql.append(" and t.doc_class = '").append(docType).append("'");// 关联文档类型
			}
			sql
			// + " order by ordersq,order_no desc,docwtime desc,publishtime desc,DOCUMENT_ID desc) p "
			.append(" order by ordersq,order_no desc,seq,ordertime desc,docwtime desc,publishtime desc,DOCUMENT_ID desc) p ")
					.append(((count < 1) ? "" : "where rownum <=" + count));// 如果count为-1默认查询所有的记录
			// System.out.println(sql);
			tm.begin(tm.RW_TRANSACTION);
			db.executeSelect(sql.toString());
			Document doc;

			for (int i = 0; i < db.size(); i++) {
				doc = new Document();
				// 处理引用频道
				// add by ge.tao
				// 2007-09-14
				if (db.getInt(i, "DOCTYPE") == Document.DOCUMENT_CHANNEL) {
					String channelid = String.valueOf(db.getLong(i, "DOCUMENT_ID"));
					Channel refChannel = new ChannelManagerImpl().getChannelInfo(channelid);
					list.add(refChannel);
					// doc.setRefChannel(refChannel);
					// doc.setDoctype(Document.DOCUMENT_CHANNEL);
				} else {
					doc.setDocument_id(db.getInt(i, "DOCUMENT_ID"));
					doc.setTitle(db.getString(i, "TITLE"));
					doc.setSubtitle(db.getString(i, "SUBTITLE"));
					doc.setAuthor(db.getString(i, "AUTHOR"));
					doc.setChanel_id(db.getInt(i, "CHANNEL_ID"));
					doc.setKeywords(db.getString(i, "KEYWORDS"));
					doc.setDocabstract(db.getString(i, "DOCABSTRACT"));
					doc.setDoctype(db.getInt(i, "DOCTYPE"));
					doc.setDocwtime(db.getDate(i, "DOCWTIME"));
					doc.setTitlecolor(db.getString(i, "TITLECOLOR"));
					doc.setCreateTime(db.getDate(i, "CREATETIME"));
					doc.setCreateUser(db.getInt(i, "CREATEUSER"));
					doc.setDocsource_id(db.getInt(i, "DOCSOURCE_ID"));
					doc.setDetailtemplate_id(db.getInt(i, "DETAILTEMPLATE_ID"));
					doc.setLinktarget(db.getString(i, "LINKTARGET"));
					doc.setLinkfile(db.getString(i, "linkfile"));
					doc.setContent(db.getString(i,"content"));
					doc.setFlowId(db.getInt(i, "FLOW_ID"));
					doc.setDoc_level(db.getInt(i, "DOC_LEVEL"));
					doc.setDoc_kind(db.getInt(i, "DOC_KIND"));
					doc.setParentDetailTpl(db.getString(i, "PARENT_DETAIL_TPL"));
					doc.setPublishTime(db.getDate(i, "publishtime"));
					doc.setPicPath(db.getString(i, "pic_path"));

					doc.setMediapath(db.getString(i, "mediapath"));
					doc.setPublishfilename(db.getString(i, "publishfilename"));
					// doc.setc(db.getString(i,"commentswitch"));
					doc.setSecondtitle(db.getString(i, "secondtitle"));
					doc.setIsNew(db.getInt(i, "isnew"));
					doc.setNewPicPath(db.getString(i, "newpic_path"));

					doc.setSiteid(db.getInt(i, "site_id"));
					// new
					doc.setExt_class(db.getString(i, "ext_class"));
					doc.setExt_djh(db.getString(i, "ext_djh"));
					doc.setExt_index(db.getString(i, "ext_index"));
					doc.setExt_org(db.getString(i, "ext_org"));
					doc.setExt_wh(db.getString(i, "ext_wh"));
					// new

					// String str = "select SRCNAME from TD_CMS_DOCSOURCE where DOCSOURCE_ID ="
					// + db.getInt(i, "DOCSOURCE_ID") + "";
					//
					// db1.executeSelect(str);
					// if (db1.size() > 0) {
					doc.setDocsource_name(db.getString(i, "source_name"));
					// }
					int isref = db.getInt(i, "ordersq");
					doc.setRef(isref == 1 ? false : true);// 判断是否是引用的文档：true为是，flase为不是
					/* 装载扩展字段数据 */
					Map docExtField = (new DocumentManagerImpl()).getDocExtFieldMap(doc.getDocument_id() + "");
					doc.setDocExtField(docExtField);
					/* 装载系统扩展字段数据 */
					doc.setExtColumn(extManager.getExtColumnInfo(i,db));
					doc.setOrdertime(db.getDate(i, "ordertime"));
					if(loaddocrelatepic)
					{
						List<Attachment> pics = CMSUtil.getCMSDriverConfiguration().getCMSService().getDocumentManager().getPicturesOfDocument(doc.getDocument_id());
						
							
						doc.setRelatePics(pics);
						
					}
					list.add(doc);
				}

			}
			tm.commit();

		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}
		finally
		{
			tm.release();
		}
		

		return list;
	}
	/**
	 * 按 创建时间,编稿时间 排序 不翻页!!! 通过频道显示名称获取频道最近发布的相应数目的文档列表 (对于那些包含于聚合文档的文档不在其列)
	 * 最近发布的相应数目的文档列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * 
	 * @param siteid
	 * @param DisplayName
	 * @param count
	 * @return List
	 * @throws ChannelManagerException
	 *             ChannelManagerImpl.java
	 * @author: 陶格
	 */
	public List getLatestPubDocListOrderByDocwtime(String siteid, String DisplayName, int count, String docType)
			throws ChannelManagerException {
		return getLatestPubDocListOrderByDocwtime(siteid,DisplayName,count,docType,false);
	}

	/**
	 * 获得频道下所有发布的带主题图片的文档
	 * 
	 * @param siteid
	 * @param DisplayName
	 * @param count
	 * @return
	 * @throws ChannelManagerException
	 */
	public ListInfo getPubDocListOfChannel(String siteid, String DisplayName, long offset, int maxpagesize)
			throws ChannelManagerException {
		List list = new ArrayList();
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		String sql = "";
		ListInfo listinfo = new ListInfo();
		try {
			sql = "select t.channel_id from td_cms_channel t " + " where t.display_name = '" + DisplayName
					+ "' and t.site_id = " + siteid;
			db.executeSelect(sql);
			int chlId;
			if (db.size() > 0) {
				chlId = db.getInt(0, "channel_id");
				sql = "select p.* from(select t.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, "
						+ "DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, DOCSOURCE_ID, DETAILTEMPLATE_ID, "
						+ "LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,"
						+ "case when DOCTYPE=1 then t.content else null end linkfile,pic_path ,1 as ordersq,seq,ordertime "
						+ "from td_cms_document t where t.pic_path is not null and  t.STATUS in ( "
						+ DocumentStatus.PUBLISHED.getStatus()
						+ ","
						+ DocumentStatus.PUBLISHING.getStatus()
						+ ") and t.ISDELETED = 0 "
						+ "and t.channel_id="
						+ chlId
						+ " union all "// 联合查询
						+ " select c.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, "
						+ "DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, c.DOCSOURCE_ID, DETAILTEMPLATE_ID, "
						+ "LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,"
						+ "case when DOCTYPE=1 "
						+ "then content else null end linkfile,pic_path,"
						+ "2 as ordersq,seq,ordertime "
						+ " from td_cms_document c,TD_CMS_DOCSOURCE ds , td_cms_chnl_ref_doc d "
						+ " left outer join (select * from td_cms_doc_arrange a "
						+ " where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<="
						+ DBUtil.getDBAdapter().to_date(new Date())
						+ " and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>="
						+ DBUtil.getDBAdapter().to_date(new Date())
						+ ") e "
						+ " on d.doc_id = e.document_id "

						+ " where c.pic_path is not null and c.DOCSOURCE_ID = ds.DOCSOURCE_ID and d.citetype = 0 and " // 查询引用文档类型为文档的引用文档，没有对引用频道的情况的处理
						+ " c.document_id=d.doc_id and STATUS in ( "
						+ DocumentStatus.PUBLISHED.getStatus()
						+ ","
						+ DocumentStatus.PUBLISHING.getStatus()
						+ ") and ISDELETED = 0 and d.doc_id not "
						+ " in(select c.id_by_aggr from td_cms_doc_aggregation c "
						+ " inner join td_cms_document z1 on c.id_by_aggr = z1.document_id "
						+ " inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id = "
						+ chlId
						+ ") "
						+ " and d.chnl_id ="
						+ chlId
						+ " union all   select t.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, DOCWTIME,"
						+ "TITLECOLOR, CREATETIME, CREATEUSER, t.DOCSOURCE_ID, DETAILTEMPLATE_ID, LINKTARGET,"
						+ "FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,case when DOCTYPE=1 then t.content else null end linkfile,pic_path,"
						+ "1 as ordersq,seq,ordertime "
						+ "from td_cms_document t  left outer join (select * from td_cms_doc_arrange a  "
						+ "where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<=TO_DATE('24-07-2008 08:29:34', 'DD-MM-YYYY HH24:MI:SS') "
						+ "and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>=TO_DATE('24-07-2008 08:29:34', 'DD-MM-YYYY HH24:MI:SS')) a  "
						+ "on t.document_id = a.document_id inner join TD_CMS_DOCSOURCE ds on t.DOCSOURCE_ID = ds.DOCSOURCE_ID where (STATUS = 5 or STATUS = 10)"
						+ " and t.pic_path is not null and ISDELETED = 0 and t.document_id not  in(select c.id_by_aggr from td_cms_doc_aggregation c  inner join td_cms_document z1 on "
						+ "  c.id_by_aggr = z1.document_id  inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id  in(select doc_id from td_cms_chnl_ref_doc where chnl_id="
						+ chlId
						+ "))  and "
						+ "  CHANNEL_ID in (select doc_id from td_cms_chnl_ref_doc where chnl_id="
						+ chlId + ") " + " order by seq,ordertime desc,docwtime desc) p ";// 如果count为-1默认查询所有的记录
				// System.out.println(sql);
				db.executeSelect(sql, offset, maxpagesize);
				Document doc;
				for (int i = 0; i < db.size(); i++) {
					doc = new Document();
					doc.setDocument_id(db.getInt(i, "DOCUMENT_ID"));
					doc.setTitle(db.getString(i, "TITLE"));
					doc.setSubtitle(db.getString(i, "SUBTITLE"));
					doc.setAuthor(db.getString(i, "AUTHOR"));
					doc.setChanel_id(db.getInt(i, "CHANNEL_ID"));
					doc.setKeywords(db.getString(i, "KEYWORDS"));
					doc.setDocabstract(db.getString(i, "DOCABSTRACT"));
					doc.setDoctype(db.getInt(i, "DOCTYPE"));
					doc.setDocwtime(db.getDate(i, "DOCWTIME"));
					doc.setOrdertime(db.getDate(i, "ordertime"));
					doc.setTitlecolor(db.getString(i, "TITLECOLOR"));
					doc.setCreateTime(db.getDate(i, "CREATETIME"));
					doc.setCreateUser(db.getInt(i, "CREATEUSER"));
					doc.setDocsource_id(db.getInt(i, "DOCSOURCE_ID"));
					doc.setDetailtemplate_id(db.getInt(i, "DETAILTEMPLATE_ID"));
					doc.setLinktarget(db.getString(i, "LINKTARGET"));
					doc.setLinkfile(db.getString(i, "linkfile"));
					doc.setFlowId(db.getInt(i, "FLOW_ID"));
					doc.setDoc_level(db.getInt(i, "DOC_LEVEL"));
					doc.setDoc_kind(db.getInt(i, "DOC_KIND"));
					doc.setParentDetailTpl(db.getString(i, "PARENT_DETAIL_TPL"));
					doc.setPublishTime(db.getDate(i, "publishtime"));
					doc.setPicPath(db.getString(i, "pic_path"));
					/* 装载扩展字段数据 */
					Map docExtField = (new DocumentManagerImpl()).getDocExtFieldMap(doc.getDocument_id() + "");
					doc.setDocExtField(docExtField);

					String str = "select SRCNAME from TD_CMS_DOCSOURCE where DOCSOURCE_ID ="
							+ db.getInt(i, "DOCSOURCE_ID") + "";

					db1.executeSelect(str);
					if (db1.size() > 0) {
						doc.setDocsource_name(db1.getString(0, "SRCNAME"));
					}

					list.add(doc);
				}
				listinfo.setDatas(list);
				listinfo.setTotalSize(db.getTotalSize());

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}
		return listinfo;
	}

	/**
	 * 获得频道下所有发布的带主题图片的文档
	 * 
	 * @param siteid
	 * @param DisplayName
	 * @param count
	 * @return
	 * @throws ChannelManagerException
	 */
	public List getPubDocListOfChannel(String siteid, String DisplayName, int count) throws ChannelManagerException {
		List list = new ArrayList();
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		String sql = "";
		try {
			sql = "select t.channel_id from td_cms_channel t " + " where t.display_name = '" + DisplayName
					+ "' and t.site_id = " + siteid;
			db.executeSelect(sql);
			int chlId;
			if (db.size() > 0) {
				chlId = db.getInt(0, "channel_id");
				sql = "select p.* from(select t.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, "
						+ "DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, DOCSOURCE_ID, DETAILTEMPLATE_ID, "
						+ "LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,"
						+ "case when DOCTYPE=1 then t.content else null end linkfile,pic_path ,1 as ordersq,seq,ordertime "
						+ "from td_cms_document t where t.pic_path is not null and t.STATUS in ( "
						+ DocumentStatus.PUBLISHED.getStatus()
						+ ","
						+ DocumentStatus.PUBLISHING.getStatus()
						+ ") and t.ISDELETED = 0 "
						+ "and t.channel_id="
						+ chlId
						+ " union all "// 联合查询
						+ " select c.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, "
						+ "DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, c.DOCSOURCE_ID, DETAILTEMPLATE_ID, "
						+ "LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,"
						+ "case when DOCTYPE=1 "
						+ "then content else null end linkfile,pic_path,"
						+ "2 as ordersq,seq,ordertime "
						+ " from td_cms_document c,TD_CMS_DOCSOURCE ds , td_cms_chnl_ref_doc d "
						+ " left outer join (select * from td_cms_doc_arrange a "
						+ " where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<="
						+ DBUtil.getDBAdapter().to_date(new Date())
						+ " and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>="
						+ DBUtil.getDBAdapter().to_date(new Date())
						+ ") e "
						+ " on d.doc_id = e.document_id "

						+ " where c.pic_path is not null and c.DOCSOURCE_ID = ds.DOCSOURCE_ID and d.citetype = 0 and " // 查询引用文档类型为文档的引用文档，没有对引用频道的情况的处理
						+ " c.document_id=d.doc_id and STATUS in ( "
						+ DocumentStatus.PUBLISHED.getStatus()
						+ ","
						+ DocumentStatus.PUBLISHING.getStatus()
						+ ") and ISDELETED = 0 and d.doc_id not "
						+ " in(select c.id_by_aggr from td_cms_doc_aggregation c "
						+ " inner join td_cms_document z1 on c.id_by_aggr = z1.document_id "
						+ " inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id = "
						+ chlId
						+ ") "
						+ " and d.chnl_id ="
						+ chlId
						+ " union all   select t.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, DOCWTIME,"
						+ "TITLECOLOR, CREATETIME, CREATEUSER, t.DOCSOURCE_ID, DETAILTEMPLATE_ID, LINKTARGET,"
						+ "FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,case when DOCTYPE=1 then t.content else null end linkfile,pic_path,"
						+ "1 as ordersq,seq,ordertime "
						+ "from td_cms_document t  left outer join (select * from td_cms_doc_arrange a  "
						+ "where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<=TO_DATE('24-07-2008 08:29:34', 'DD-MM-YYYY HH24:MI:SS') "
						+ "and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>=TO_DATE('24-07-2008 08:29:34', 'DD-MM-YYYY HH24:MI:SS')) a  "
						+ "on t.document_id = a.document_id inner join TD_CMS_DOCSOURCE ds on t.DOCSOURCE_ID = ds.DOCSOURCE_ID where (STATUS = 5 or STATUS = 10)"
						+ " and t.pic_path is not null and ISDELETED = 0 and t.document_id not  in(select c.id_by_aggr from td_cms_doc_aggregation c  inner join td_cms_document z1 on "
						+ "  c.id_by_aggr = z1.document_id  inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id  in(select doc_id from td_cms_chnl_ref_doc where chnl_id="
						+ chlId
						+ "))  and "
						+ "  CHANNEL_ID in (select doc_id from td_cms_chnl_ref_doc where chnl_id="
						+ chlId
						+ ") "
						+ " order by seq,ordertime desc,docwtime desc) p "
						+ ((count < 1) ? "" : "where rownum <=" + count);// 如果count为-1默认查询所有的记录
				// System.out.println(sql);
				db.executeSelect(sql);
				Document doc;
				for (int i = 0; i < db.size(); i++) {
					doc = new Document();
					doc.setDocument_id(db.getInt(i, "DOCUMENT_ID"));
					doc.setTitle(db.getString(i, "TITLE"));
					doc.setSubtitle(db.getString(i, "SUBTITLE"));
					doc.setAuthor(db.getString(i, "AUTHOR"));
					doc.setChanel_id(db.getInt(i, "CHANNEL_ID"));
					doc.setKeywords(db.getString(i, "KEYWORDS"));
					doc.setDocabstract(db.getString(i, "DOCABSTRACT"));
					doc.setDoctype(db.getInt(i, "DOCTYPE"));
					doc.setDocwtime(db.getDate(i, "DOCWTIME"));
					doc.setOrdertime(db.getDate(i, "ordertime"));
					doc.setTitlecolor(db.getString(i, "TITLECOLOR"));
					doc.setCreateTime(db.getDate(i, "CREATETIME"));
					doc.setCreateUser(db.getInt(i, "CREATEUSER"));
					doc.setDocsource_id(db.getInt(i, "DOCSOURCE_ID"));
					doc.setDetailtemplate_id(db.getInt(i, "DETAILTEMPLATE_ID"));
					doc.setLinktarget(db.getString(i, "LINKTARGET"));
					doc.setLinkfile(db.getString(i, "linkfile"));
					doc.setFlowId(db.getInt(i, "FLOW_ID"));
					doc.setDoc_level(db.getInt(i, "DOC_LEVEL"));
					doc.setDoc_kind(db.getInt(i, "DOC_KIND"));
					doc.setParentDetailTpl(db.getString(i, "PARENT_DETAIL_TPL"));
					doc.setPublishTime(db.getDate(i, "publishtime"));
					doc.setPicPath(db.getString(i, "pic_path"));
					/* 装载扩展字段数据 */
					Map docExtField = (new DocumentManagerImpl()).getDocExtFieldMap(doc.getDocument_id() + "");
					doc.setDocExtField(docExtField);

					String str = "select SRCNAME from TD_CMS_DOCSOURCE where DOCSOURCE_ID ="
							+ db.getInt(i, "DOCSOURCE_ID") + "";

					db1.executeSelect(str);
					if (db1.size() > 0) {
						doc.setDocsource_name(db1.getString(0, "SRCNAME"));
					}

					list.add(doc);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}
		return list;
	}

	/**
	 * 按 文档发布时间 排序 翻页!!! 通过频道显示名称获取频道最近发布的相应数目的分页文档列表 (对于那些包含于聚合文档的文档不在其列)
	 * 最近发布的相应数目的文档列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * 
	 * @author xinwang.jiao
	 * @param String
	 *            DisplayName
	 * @return List<Document>
	 * @throws ChannelManagerException
	 */
	public ListInfo getLatestPubDocList(String siteid, String DisplayName, int offset, int maxItem)
			throws ChannelManagerException {
		ListInfo list = new ListInfo();
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		StringBuffer sql = new StringBuffer();
		try {
			// sql = "select t.channel_id from td_cms_channel t " +
			// " where t.display_name = '" + DisplayName + "' and t.site_id = "
			// + siteid;
			// db.executeSelect(sql);
			// int chlId;
			ChannelCacheManager cm = (ChannelCacheManager) SiteCacheManager.getInstance()
					.getChannelCacheManager(siteid);
			long chlId = ((Channel) cm.getChannelByDisplayName(DisplayName)).getChannelId();
			// if(db.size() > 0)
			// {
			// chlId = db.getInt(0,"channel_id");
			sql.append(
					"select p.* from(select t.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, ")
					.append("DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, DOCSOURCE_ID, DETAILTEMPLATE_ID, ")
					.append("LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,")
					.append("case when DOCTYPE=1 ")
					.append("then t.content else null end linkfile,pic_path,mediapath,publishfilename,commentswitch,secondtitle,")
					.append("isnew,newpic_path,")
					.append("nvl(a.order_no,-1) as order_no,1 as ordersq ")
					.append(" from td_cms_document t ")
					.append(" left outer join (select * from td_cms_doc_arrange a ")
					.append(" where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<=")
					.append(DBUtil.getDBAdapter().to_date(new Date()))
					.append(" and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>=")
					.append(DBUtil.getDBAdapter().to_date(new Date()))
					.append(") a ")
					.append(" on t.document_id = a.document_id ")
					.append(" where STATUS = ")
					.append(DocumentStatus.PUBLISHED.getStatus())
					.append(" and ISDELETED = 0 and t.document_id not ")
					.append(" in(select c.id_by_aggr from td_cms_doc_aggregation c ")
					.append(" inner join td_cms_document z1 on c.id_by_aggr = z1.document_id ")
					.append(" inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id = ")
					.append(chlId)
					.append(") ")
					.append(" and CHANNEL_ID =")
					.append(chlId)
					.append(" union all ")
					// 联合查询
					.append(" select c.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, ")
					.append("DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, DOCSOURCE_ID, DETAILTEMPLATE_ID, ")
					.append("LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,")
					.append("case when DOCTYPE=1 ")
					.append("then content else null end linkfile,pic_path,mediapath,publishfilename,commentswitch,secondtitle,")
					.append("isnew,newpic_path,").append("nvl(e.order_no,-1) as order_no,2 as ordersq ")
					.append(" from td_cms_document c, td_cms_chnl_ref_doc d ")
					.append(" left outer join (select * from td_cms_doc_arrange a ")
					.append(" where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<=")
					.append(DBUtil.getDBAdapter().to_date(new Date()))
					.append(" and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>=")
					.append(DBUtil.getDBAdapter().to_date(new Date())).append(") e ")
					.append(" on d.doc_id = e.document_id ").append(" where c.document_id=d.doc_id and STATUS = ")
					.append(DocumentStatus.PUBLISHED.getStatus()).append(" and ISDELETED = 0 and d.doc_id not ")
					.append(" in(select c.id_by_aggr from td_cms_doc_aggregation c ")
					.append(" inner join td_cms_document z1 on c.id_by_aggr = z1.document_id ")
					.append(" inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id = ")
					.append(chlId).append(") ").append(" and d.chnl_id =").append(chlId)
					.append(" order by order_no desc,publishtime desc) p ");// 如果count为-1默认查询所有的记录
			// System.out.println(sql);
			db.executeSelect(sql.toString(), offset, maxItem);
			Document doc;
			List datas = new ArrayList();

			for (int i = 0; i < db.size(); i++) {
				doc = new Document();
				// 处理引用频道
				// add by ge.tao
				// 2007-09-14
				if (db.getInt(i, "DOCTYPE") == Document.DOCUMENT_CHANNEL) {
					String channelid = String.valueOf(db.getLong(i, "document_id"));
					Channel refChannel = new ChannelManagerImpl().getChannelInfo(channelid);
					// doc.setRefChannel(refChannel);
					// doc.setDoctype(Document.DOCUMENT_CHANNEL);
					datas.add(refChannel);
				} else {
					doc.setDocument_id(db.getInt(i, "DOCUMENT_ID"));
					doc.setTitle(db.getString(i, "TITLE"));
					doc.setSubtitle(db.getString(i, "SUBTITLE"));
					doc.setAuthor(db.getString(i, "AUTHOR"));
					doc.setChanel_id(db.getInt(i, "CHANNEL_ID"));
					doc.setKeywords(db.getString(i, "KEYWORDS"));
					doc.setDocabstract(db.getString(i, "DOCABSTRACT"));
					doc.setDoctype(db.getInt(i, "DOCTYPE"));
					doc.setDocwtime(db.getDate(i, "DOCWTIME"));
					doc.setTitlecolor(db.getString(i, "TITLECOLOR"));
					doc.setCreateTime(db.getDate(i, "CREATETIME"));
					doc.setCreateUser(db.getInt(i, "CREATEUSER"));
					doc.setDocsource_id(db.getInt(i, "DOCSOURCE_ID"));
					doc.setDetailtemplate_id(db.getInt(i, "DETAILTEMPLATE_ID"));
					doc.setLinktarget(db.getString(i, "LINKTARGET"));
					doc.setLinkfile(db.getString(i, "linkfile"));
					doc.setFlowId(db.getInt(i, "FLOW_ID"));
					doc.setDoc_level(db.getInt(i, "DOC_LEVEL"));
					doc.setDoc_kind(db.getInt(i, "DOC_KIND"));
					doc.setParentDetailTpl(db.getString(i, "PARENT_DETAIL_TPL"));
					doc.setPublishTime(db.getDate(i, "publishtime"));
					doc.setPicPath(db.getString(i, "pic_path"));

					doc.setMediapath(db.getString(i, "mediapath"));
					doc.setPublishfilename(db.getString(i, "publishfilename"));
					// doc.setc(db.getString(i,"commentswitch"));
					doc.setSecondtitle(db.getString(i, "secondtitle"));
					doc.setIsNew(db.getInt(i, "isnew"));
					doc.setNewPicPath(db.getString(i, "newpic_path"));

					String str = "select SRCNAME from TD_CMS_DOCSOURCE where DOCSOURCE_ID ="
							+ db.getInt(i, "DOCSOURCE_ID") + "";

					db1.executeSelect(str);
					if (db1.size() > 0) {
						doc.setDocsource_name(db1.getString(0, "SRCNAME"));
					}
					int isref = db.getInt(i, "ordersq");
					doc.setRef(isref == 1 ? false : true);// 判断是否是引用的文档：true为是，flase为不是
					/* 装载扩展字段数据 */
					Map docExtField = (new DocumentManagerImpl()).getDocExtFieldMap(doc.getDocument_id() + "");
					doc.setDocExtField(docExtField);
					/* 装载系统扩展字段数据 */
					doc.setExtColumn(extManager.getExtColumnInfo(i,db));
					datas.add(doc);
				}

			}
			list.setDatas(datas);
			list.setTotalSize(db.getTotalSize());
			// }
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}

		return list;
	}

	/**
	 * 按 文档发布时间, 翻页!!! 通过频道显示名称获取频道最近发布的相应数目的分页文档列表 包括那些文档状态为“正在发布中”的文档
	 * (对于那些包含于聚合文档的文档不在其列) 最近发布的相应数目的文档列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * 
	 * @author xinwang.jiao
	 * @param String
	 *            DisplayName
	 * @return List<Document>
	 * @throws ChannelManagerException
	 *             add by xinwang.jiao 2007.6.11
	 */
	public ListInfo getLatestPubAndPubingDocList(String siteid, String DisplayName, int offset, int maxItem)
			throws ChannelManagerException {
		ListInfo list = new ListInfo();
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		StringBuffer sql = new StringBuffer();
		try {
			// sql = "select t.channel_id from td_cms_channel t " +
			// " where t.display_name = '" + DisplayName + "' and t.site_id = "
			// + siteid;
			// db.executeSelect(sql);
			// int chlId;
			ChannelCacheManager cm = (ChannelCacheManager) SiteCacheManager.getInstance()
					.getChannelCacheManager(siteid);
			long chlId = ((Channel) cm.getChannelByDisplayName(DisplayName)).getChannelId();
			// if(db.size() > 0)
			// {
			// chlId = db.getInt(0,"channel_id");
			sql.append(
					"select p.* from(select t.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, ")
					.append("DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, DOCSOURCE_ID, DETAILTEMPLATE_ID, ")
					.append("LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,")
					.append("case when DOCTYPE=1 ")
					.append("then t.content else null end linkfile,pic_path,mediapath,publishfilename,commentswitch,secondtitle,")
					.append("isnew,newpic_path,")
					.append("nvl(a.order_no,-1) as order_no,1 as ordersq ")
					.append(" from td_cms_document t ")
					.append(" left outer join (select * from td_cms_doc_arrange a ")
					.append(" where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<=")
					.append(DBUtil.getDBAdapter().to_date(new Date()))
					.append(" and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>=")
					.append(DBUtil.getDBAdapter().to_date(new Date()))
					.append(") a ")
					.append(" on t.document_id = a.document_id ")
					.append(" where (STATUS = ")
					.append(DocumentStatus.PUBLISHED.getStatus())
					.append(" or STATUS = ")
					.append(DocumentStatus.PUBLISHING.getStatus())
					.append(") ")
					.append(" and ISDELETED = 0 and t.document_id not ")
					.append(" in(select c.id_by_aggr from td_cms_doc_aggregation c ")
					.append(" inner join td_cms_document z1 on c.id_by_aggr = z1.document_id ")
					.append(" inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id = ")
					.append(chlId)
					.append(") ")
					.append(" and CHANNEL_ID =")
					.append(chlId)
					.append(" union all ")
					// 联合查询
					.append(" select c.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, ")
					.append("DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, DOCSOURCE_ID, DETAILTEMPLATE_ID, ")
					.append("LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,")
					.append("case when DOCTYPE=1 ")
					.append("then content else null end linkfile,pic_path,mediapath,publishfilename,commentswitch,secondtitle,")
					.append("isnew,newpic_path,").append("nvl(e.order_no,-1) as order_no,2 as ordersq ")
					.append(" from td_cms_document c, td_cms_chnl_ref_doc d ")
					.append(" left outer join (select * from td_cms_doc_arrange a ")
					.append(" where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<=")
					.append(DBUtil.getDBAdapter().to_date(new Date()))
					.append(" and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>=")
					.append(DBUtil.getDBAdapter().to_date(new Date())).append(") e ")
					.append(" on d.doc_id = e.document_id ").append(" where c.document_id=d.doc_id and ")
					.append(" (STATUS = ").append(DocumentStatus.PUBLISHED.getStatus()).append(" or STATUS = ")
					.append(DocumentStatus.PUBLISHING.getStatus()).append(") ")
					.append(" and ISDELETED = 0 and d.doc_id not ")
					.append(" in(select c.id_by_aggr from td_cms_doc_aggregation c ")
					.append(" inner join td_cms_document z1 on c.id_by_aggr = z1.document_id ")
					.append(" inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id = ")
					.append(chlId).append(") ").append(" and d.chnl_id =").append(chlId)
					.append(" order by order_no desc,publishtime desc) p ");// 如果count为-1默认查询所有的记录
			// System.out.println(sql);
			if (maxItem <= 0) {
				db.executeSelect(sql.toString());
			} else {
				db.executeSelect(sql.toString(), offset, maxItem);
			}
			Document doc;
			List datas = new ArrayList();

			for (int i = 0; i < db.size(); i++) {
				doc = new Document();
				// 处理引用频道
				// add by ge.tao
				// 2007-09-14
				if (db.getInt(i, "DOCTYPE") == Document.DOCUMENT_CHANNEL) {
					String channelid = String.valueOf(db.getLong(i, "DOCUMENT_ID"));
					Channel refChannel = new ChannelManagerImpl().getChannelInfo(channelid);
					// doc.setRefChannel(refChannel);
					// doc.setDoctype(Document.DOCUMENT_CHANNEL);
					datas.add(refChannel);
				} else {
					doc.setDocument_id(db.getInt(i, "DOCUMENT_ID"));
					doc.setTitle(db.getString(i, "TITLE"));
					doc.setSubtitle(db.getString(i, "SUBTITLE"));
					doc.setAuthor(db.getString(i, "AUTHOR"));
					doc.setChanel_id(db.getInt(i, "CHANNEL_ID"));
					doc.setKeywords(db.getString(i, "KEYWORDS"));
					doc.setDocabstract(db.getString(i, "DOCABSTRACT"));
					doc.setDoctype(db.getInt(i, "DOCTYPE"));
					doc.setDocwtime(db.getDate(i, "DOCWTIME"));
					doc.setTitlecolor(db.getString(i, "TITLECOLOR"));
					doc.setCreateTime(db.getDate(i, "CREATETIME"));
					doc.setCreateUser(db.getInt(i, "CREATEUSER"));
					doc.setDocsource_id(db.getInt(i, "DOCSOURCE_ID"));
					doc.setDetailtemplate_id(db.getInt(i, "DETAILTEMPLATE_ID"));
					doc.setLinktarget(db.getString(i, "LINKTARGET"));
					doc.setLinkfile(db.getString(i, "linkfile"));
					doc.setFlowId(db.getInt(i, "FLOW_ID"));
					doc.setDoc_level(db.getInt(i, "DOC_LEVEL"));
					doc.setDoc_kind(db.getInt(i, "DOC_KIND"));
					doc.setParentDetailTpl(db.getString(i, "PARENT_DETAIL_TPL"));
					doc.setPublishTime(db.getDate(i, "publishtime"));
					doc.setPicPath(db.getString(i, "pic_path"));

					doc.setMediapath(db.getString(i, "mediapath"));
					doc.setPublishfilename(db.getString(i, "publishfilename"));
					// doc.setc(db.getString(i,"commentswitch"));
					doc.setSecondtitle(db.getString(i, "secondtitle"));
					doc.setIsNew(db.getInt(i, "isnew"));
					doc.setNewPicPath(db.getString(i, "newpic_path"));

					String str = "select SRCNAME from TD_CMS_DOCSOURCE where DOCSOURCE_ID ="
							+ db.getInt(i, "DOCSOURCE_ID") + "";

					db1.executeSelect(str);
					if (db1.size() > 0) {
						doc.setDocsource_name(db1.getString(0, "SRCNAME"));
					}
					int isref = db.getInt(i, "ordersq");
					doc.setRef(isref == 1 ? false : true);// 判断是否是引用的文档：true为是，flase为不是
					/* 装载扩展字段数据 */
					Map docExtField = (new DocumentManagerImpl()).getDocExtFieldMap(doc.getDocument_id() + "");
					doc.setDocExtField(docExtField);
					/* 装载系统扩展字段数据 */
					doc.setExtColumn(extManager.getExtColumnInfo(i,db));
					datas.add(doc);
				}

			}
			list.setDatas(datas);
			list.setTotalSize(db.getTotalSize());
			// }
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}

		return list;
	}

	/**
	 * 按 创建时间,编稿时间 排序 不翻页!!! 通过频道显示名称获取频道最近发布的相应数目的文档附件列表 (对于那些包含于聚合文档的文档不在其列)
	 * 最近发布的相应数目的文档列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * 
	 * @param siteid
	 * @param DisplayName
	 * @param count
	 * @return List
	 * @throws ChannelManagerException
	 *             ChannelManagerImpl.java
	 * @author: biaoping.yin
	 */
	public List getLatestPubDocAttachListOrderByDocwtime(String siteid, String DisplayName, int count,String docType)
			throws ChannelManagerException {
		return this.getLatestPubDocAttachListOrderByDocwtime(siteid, DisplayName, 0, count, docType).getDatas();
	}

	/**
	 * 按 文档创建时间,发布时间,id 排序 翻页!!! 通过频道显示名称获取频道最近发布的相应数目的分页文档附件列表
	 * (对于那些包含于聚合文档的文档不在其列) 最近发布的相应数目的文档附件列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * 
	 * @param siteid
	 * @param DisplayName
	 * @param offset
	 * @param maxItem
	 * @return ListInfo
	 * @throws ChannelManagerException
	 *             ChannelManagerImpl.java
	 * @author: biaoping.yin added on 2007.11.18
	 */
	public ListInfo getLatestPubDocAttachListOrderByDocwtime(String siteid, String DisplayName, int offset, int maxItem,String docType)
			throws ChannelManagerException {
		ListInfo list = new ListInfo();
		// DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		DBUtil db = new DBUtil();
		// DBUtil db1 = new DBUtil();
		StringBuffer sql = new StringBuffer();
		try {
			// sql = "select t.channel_id from td_cms_channel t " +
			// " where t.display_name = '" + DisplayName + "' and t.site_id = "
			// + siteid;
			// db.executeSelect(sql);
			// int chlId;
			ChannelCacheManager cm = (ChannelCacheManager) SiteCacheManager.getInstance()
					.getChannelCacheManager(siteid);
			long chlId = ((Channel) cm.getChannelByDisplayName(DisplayName)).getChannelId();
			// if(db.size() > 0)
			// {
			// chlId = db.getInt(0,"channel_id");
			sql.append(
					"select p.* from ( select xx.*,bbb.id attach_id,bbb.url,bbb.type,bbb.description,bbb.original_filename from (select t.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, ")
					.append("DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, t.DOCSOURCE_ID,nvl(ds.srcname,'未知') as source_name, DETAILTEMPLATE_ID, ")
					.append("LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,")
					.append("case when DOCTYPE=1 ")
					.append("then t.content else null end linkfile,pic_path,mediapath,publishfilename,commentswitch,secondtitle,")
					.append("isnew,newpic_path,")
					.append("nvl(a.order_no,-1) as order_no,1 as ordersq,ordertime,seq ")
					.append(" from td_cms_document t ")
					.append(" left outer join (select * from td_cms_doc_arrange a ")
					.append(" where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<=")
					.append(DBUtil.getDBAdapter().to_date(new Date()))
					.append(" and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>=")
					.append(DBUtil.getDBAdapter().to_date(new Date()))
					.append(") a ")
					.append(" on t.document_id = a.document_id ")
					.append("inner join TD_CMS_DOCSOURCE ds on t.DOCSOURCE_ID = ds.DOCSOURCE_ID")
					.append(" where (STATUS = ")
					.append(DocumentStatus.PUBLISHED.getStatus())
					.append(" or STATUS = ")
					.append(DocumentStatus.PUBLISHING.getStatus())
					.append(") ")
					.append(" and ISDELETED = 0 and t.document_id not ")
					.append(" in(select c.id_by_aggr from td_cms_doc_aggregation c ")
					.append(" inner join td_cms_document z1 on c.id_by_aggr = z1.document_id ")
					.append(" inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id = ")
					.append(chlId)
					.append(") ")
					.append(" and CHANNEL_ID =")
					.append(chlId);
					if (!StringUtil.isEmpty(docType)) {
		//				sql.append(" and t.doc_kind = ").append(docType);// 关联文档类型
						sql.append(" and t.doc_class = '").append(docType).append("'");// 关联文档类型
					}
					// .append(" union all ")// 联合查询
					// .append(" select c.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, ")
					// .append("DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, c.DOCSOURCE_ID,nvl(ds.srcname,'未知') as source_name, DETAILTEMPLATE_ID, ")
					// .append("LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,")
					// .append("case when DOCTYPE=1 ")
					// .append("then content else null end linkfile,pic_path,mediapath,publishfilename,commentswitch,secondtitle,")
					// .append("isnew,newpic_path,")
					// .append("nvl(e.order_no,-1) as order_no,2 as ordersq,ordertime ")
					// .append(" from td_cms_document c, TD_CMS_DOCSOURCE ds, td_cms_chnl_ref_doc d ")
					// .append(" left outer join (select * from td_cms_doc_arrange a ")
					// .append(" where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<=")
					// .append(DBUtil.getDBAdapter().sysdate())
					// .append(" and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>=")
					// .append(DBUtil.getDBAdapter().sysdate())
					// .append(") e ")
					// .append(" on d.doc_id = e.document_id ")
					//
					// .append(" where  c.DOCSOURCE_ID = ds.DOCSOURCE_ID and d.citetype = 0 and ")
					// //查询引用文档类型为文档的引用文档，没有对引用频道的情况的处理
					// .append(" c.document_id=d.doc_id and (STATUS = ")
					// .append(DocumentStatus.PUBLISHED.getStatus())
					// .append(" or STATUS = ")
					// .append(DocumentStatus.PUBLISHING.getStatus())
					// .append(") ")
					// .append(" and ISDELETED = 0 and d.doc_id not ")
					// .append(" in(select c.id_by_aggr from td_cms_doc_aggregation c ")
					// .append(" inner join td_cms_document z1 on c.id_by_aggr = z1.document_id ")
					// .append(" inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id = ")
					// .append(chlId)
					// .append(") ")
					// .append(" and d.chnl_id =")
					// .append(chlId)
					sql.append(") xx ,TD_CMS_DOC_ATTACH bbb where xx.document_id = bbb.document_id ")
					.append(" order by xx.order_no desc,xx.seq,xx.ordertime desc,xx.docwtime desc,xx.publishtime desc,xx.DOCUMENT_ID desc) p ");// 如果count为-1默认查询所有的记录
			System.out.println(sql);
			if (maxItem < 0) {
				db.executeSelect(sql.toString());
			}

			else {
				db.executeSelect(sql.toString(), offset, maxItem);
			}
			Document doc;
			List datas = new ArrayList();

			for (int i = 0; i < db.size(); i++) {
				doc = new Document();
				// 处理引用频道
				// add by ge.tao
				// 2007-09-14
				if (db.getInt(i, "DOCTYPE") == Document.DOCUMENT_CHANNEL) {
					String channelid = String.valueOf(db.getLong(i, "DOCUMENT_ID"));
					Channel refChannel = new ChannelManagerImpl().getChannelInfo(channelid);
					// doc.setRefChannel(refChannel);
					// doc.setDoctype(Document.DOCUMENT_CHANNEL);
					datas.add(refChannel);
				} else {
					doc.setDocument_id(db.getInt(i, "DOCUMENT_ID"));
					doc.setTitle(db.getString(i, "TITLE"));
					doc.setSubtitle(db.getString(i, "SUBTITLE"));
					doc.setAuthor(db.getString(i, "AUTHOR"));
					doc.setChanel_id(db.getInt(i, "CHANNEL_ID"));
					doc.setKeywords(db.getString(i, "KEYWORDS"));
					doc.setDocabstract(db.getString(i, "DOCABSTRACT"));
					doc.setDoctype(db.getInt(i, "DOCTYPE"));
					doc.setDocwtime(db.getDate(i, "DOCWTIME"));
					doc.setTitlecolor(db.getString(i, "TITLECOLOR"));
					doc.setCreateTime(db.getDate(i, "CREATETIME"));
					doc.setCreateUser(db.getInt(i, "CREATEUSER"));
					doc.setDocsource_id(db.getInt(i, "DOCSOURCE_ID"));
					doc.setDetailtemplate_id(db.getInt(i, "DETAILTEMPLATE_ID"));
					doc.setLinktarget(db.getString(i, "LINKTARGET"));
					doc.setLinkfile(db.getString(i, "linkfile"));
					doc.setFlowId(db.getInt(i, "FLOW_ID"));
					doc.setDoc_level(db.getInt(i, "DOC_LEVEL"));
					doc.setDoc_kind(db.getInt(i, "DOC_KIND"));
					doc.setParentDetailTpl(db.getString(i, "PARENT_DETAIL_TPL"));
					doc.setPublishTime(db.getDate(i, "publishtime"));
					doc.setPicPath(db.getString(i, "pic_path"));

					doc.setMediapath(db.getString(i, "mediapath"));
					doc.setPublishfilename(db.getString(i, "publishfilename"));
					// doc.setc(db.getString(i,"commentswitch"));
					doc.setSecondtitle(db.getString(i, "secondtitle"));
					doc.setIsNew(db.getInt(i, "isnew"));
					doc.setNewPicPath(db.getString(i, "newpic_path"));

					// String str = "select SRCNAME from TD_CMS_DOCSOURCE where DOCSOURCE_ID ="
					// + db.getInt(i, "DOCSOURCE_ID") + "";
					//
					// db1.executeSelect(str);
					// if (db1.size() > 0) {
					doc.setDocsource_name(db.getString(i, "source_name"));
					// }
					int isref = db.getInt(i, "ordersq");
					doc.setRef(isref == 1 ? false : true);// 判断是否是引用的文档：true为是，flase为不是
					/* 装载扩展字段数据 */
					// Map docExtField = (new DocumentManagerImpl())
					// .getDocExtFieldMap(doc.getDocument_id() + "");
					// doc.setDocExtField(docExtField);
					/* 装载系统扩展字段数据 */
					// doc.setExtColumn(extManager.getExtColumnInfo(db));

					Attachment attach = new Attachment();
					attach.setId(db.getInt(i, "attach_id"));
					attach.setDocumentId(db.getInt(i, "document_id"));
					attach.setUrl(db.getString(i, "url"));
					attach.setDescription(db.getString(i, "description"));
					attach.setOriginalFilename(db.getString(i, "ORIGINAL_FILENAME"));

					attach.setId(db.getInt(i, "attach_id"));
					attach.setDocument(doc);
					datas.add(attach);
				}

			}
			list.setDatas(datas);
			list.setTotalSize(db.getTotalSize());
			// }
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}

		return list;
	}
	public ListInfo getLatestPubDocListOrderByDocwtime(String siteid,String DisplayName,int offset,int maxItem,String doctype) throws ChannelManagerException
	{
		return getLatestPubDocListOrderByDocwtime(siteid, DisplayName, offset, maxItem,
				doctype,false);
	}
	public ListInfo getLatestPubDocListOrderByDocwtime(String siteid, String DisplayName, int offset, int maxItem,
			String docType,boolean loaddocrelatepic) throws ChannelManagerException
	{
		return getLatestPubDocListOrderByDocwtime( siteid,  DisplayName,  offset,  maxItem,
				 docType, loaddocrelatepic,false);
	}
	public boolean getBooleanParam(Map params,String key,boolean defaultValue)
	{
		if(params != null)
		{
			Boolean value = (Boolean)params.get(key);
			if(value != null)
				return value.booleanValue();
			return defaultValue;
		}
		return defaultValue;
	}
	public ListInfo getLatestPubDocListOrderByDocwtime(String siteid,String DisplayName,int offset,int maxItem,String doctype,Map params) throws ChannelManagerException
	{
		boolean loaddocrelatepic = this.getBooleanParam(params,"loadrelatepic",false);
		boolean loadcontent = this.getBooleanParam(params,"loadcontent",false);
		return getLatestPubDocListOrderByDocwtime( siteid,  DisplayName,  offset,  maxItem,
				doctype, loaddocrelatepic,loadcontent);
	}
	/**
	 * 按 文档创建时间,发布时间,id 排序 翻页!!! 通过频道显示名称获取频道最近发布的相应数目的分页文档列表
	 * (对于那些包含于聚合文档的文档不在其列) 最近发布的相应数目的文档列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * 
	 * @param siteid
	 * @param DisplayName
	 * @param offset
	 * @param maxItem
	 * @return ListInfo
	 * @throws ChannelManagerException
	 *             ChannelManagerImpl.java
	 * @author: 陶格
	 */
	public ListInfo getLatestPubDocListOrderByDocwtime(String siteid, String DisplayName, int offset, int maxItem,
			String docType,boolean loaddocrelatepic,boolean loadcontent) throws ChannelManagerException {
		ListInfo list = new ListInfo();
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		StringBuffer sql = new StringBuffer();
		TransactionManager tm = new TransactionManager(); 
		try {
			
			// sql = "select t.channel_id from td_cms_channel t " +
			// " where t.display_name = '" + DisplayName + "' and t.site_id = "
			// + siteid;
			// db.executeSelect(sql);
			// int chlId;
			ChannelCacheManager cm = (ChannelCacheManager) SiteCacheManager.getInstance()
					.getChannelCacheManager(siteid);
			long chlId = ((Channel) cm.getChannelByDisplayName(DisplayName)).getChannelId();
			// if(db.size() > 0)
			// {
			// chlId = db.getInt(0,"channel_id");
			sql.append(
					"select p.* from (select t.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, ")
					.append("DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, t.DOCSOURCE_ID,nvl(ds.srcname,'未知') as source_name, DETAILTEMPLATE_ID, ")
					.append("LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,")
					.append("case when DOCTYPE=1 ")
					.append("then t.content else null end linkfile,")
					.append(loadcontent?"case when DOCTYPE<>1 then t.content else null end content,":"null content,")					
					.append("pic_path,mediapath,publishfilename,commentswitch,secondtitle,")
					.append("isnew,newpic_path,")
					.append("nvl(a.order_no,-1) as order_no,1 as ordersq,ordertime,seq,-1 site_id,ext_wh,ext_class,ext_index,ext_org,ext_djh ")
					.append(" from td_cms_document t ").append(" left outer join (select * from td_cms_doc_arrange a ")
					.append(" where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<=")
					.append(DBUtil.getDBAdapter().to_date(new Date()))
					.append(" and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>=")
					.append(DBUtil.getDBAdapter().to_date(new Date())).append(") a ")
					.append(" on t.document_id = a.document_id ")
					.append("inner join TD_CMS_DOCSOURCE ds on t.DOCSOURCE_ID = ds.DOCSOURCE_ID")
					.append(" where (STATUS = ").append(DocumentStatus.PUBLISHED.getStatus()).append(" or STATUS = ")
					.append(DocumentStatus.PUBLISHING.getStatus()).append(") ")
					.append(" and ISDELETED = 0 and t.document_id not ")
					.append(" in(select c.id_by_aggr from td_cms_doc_aggregation c ")
					.append(" inner join td_cms_document z1 on c.id_by_aggr = z1.document_id ")
					.append(" inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id = ")
					.append(chlId).append(") ").append(" and CHANNEL_ID =").append(chlId);
			if (!StringUtil.isEmpty(docType)) {
//				sql.append(" and t.doc_kind = ").append(docType);// 关联文档类型
				sql.append(" and t.doc_class = '").append(docType).append("'");// 关联文档类型
			}
			sql.append(" union all ")
					// 联合查询
					.append(" select c.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, ")
					.append("DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, c.DOCSOURCE_ID,nvl(ds.srcname,'未知') as source_name, DETAILTEMPLATE_ID, ")
					.append("LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,")
					.append("case when DOCTYPE=1 ")
					.append("then content else null end linkfile,")
					.append(loadcontent?"case when DOCTYPE<>1 then c.content else null end content,":"null content,")					
					.append("pic_path,mediapath,publishfilename,commentswitch,secondtitle,")
					.append("isnew,newpic_path,")
					.append("nvl(e.order_no,-1) as order_no,2 as ordersq,ordertime,seq,d.site_id,ext_wh,ext_class,ext_index,ext_org,ext_djh ")
					.append(" from td_cms_document c, TD_CMS_DOCSOURCE ds, td_cms_chnl_ref_doc d ")
					.append(" left outer join (select * from td_cms_doc_arrange a ")
					.append(" where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<=")
					.append(DBUtil.getDBAdapter().to_date(new Date()))
					.append(" and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>=")
					.append(DBUtil.getDBAdapter().to_date(new Date()))
					.append(") e ")
					.append(" on d.doc_id = e.document_id ")

					.append(" where  c.DOCSOURCE_ID = ds.DOCSOURCE_ID and d.citetype = 0 and ")
					// 查询引用文档类型为文档的引用文档，没有对引用频道的情况的处理
					.append(" c.document_id=d.doc_id and (STATUS = ").append(DocumentStatus.PUBLISHED.getStatus())
					.append(" or STATUS = ").append(DocumentStatus.PUBLISHING.getStatus()).append(") ")
					.append(" and ISDELETED = 0 and d.doc_id not ")
					.append(" in(select c.id_by_aggr from td_cms_doc_aggregation c ")
					.append(" inner join td_cms_document z1 on c.id_by_aggr = z1.document_id ")
					.append(" inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id = ")
					.append(chlId).append(") ").append(" and d.chnl_id =").append(chlId);
			if (!StringUtil.isEmpty(docType)) {
				sql.append(" and c.doc_kind = ").append(docType);// 关联文档类型
			}
			sql.append(
					" union all   select t.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, DOCWTIME,")
					.append("TITLECOLOR, CREATETIME, CREATEUSER, t.DOCSOURCE_ID,nvl(ds.srcname,'未知') as source_name, DETAILTEMPLATE_ID, LINKTARGET,")
					.append("FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,case when DOCTYPE=1 then t.content else null end linkfile,")
					.append(loadcontent?"case when DOCTYPE<>1 then t.content else null end content,":"null content,")					
					.append("pic_path,")
					.append("mediapath,publishfilename,commentswitch,secondtitle,isnew,newpic_path,nvl(a.order_no,-1) as order_no,1 as ordersq,ordertime,seq,-1 site_id,ext_wh,ext_class,ext_index,ext_org,ext_djh ")
					.append("from td_cms_document t  left outer join (select * from td_cms_doc_arrange a  ")
					.append("where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<=TO_DATE('24-07-2008 08:29:34', 'DD-MM-YYYY HH24:MI:SS') ")
					.append("and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>=TO_DATE('24-07-2008 08:29:34', 'DD-MM-YYYY HH24:MI:SS')) a  ")
					.append("on t.document_id = a.document_id inner join TD_CMS_DOCSOURCE ds on t.DOCSOURCE_ID = ds.DOCSOURCE_ID where (STATUS = 5 or STATUS = 10)")
					.append("  and ISDELETED = 0 and t.document_id not  in(select c.id_by_aggr from td_cms_doc_aggregation c  inner join td_cms_document z1 on ")
					.append("  c.id_by_aggr = z1.document_id  inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id  in(select doc_id from td_cms_chnl_ref_doc where chnl_id="
							+ chlId + "))  and ")
					.append("  CHANNEL_ID in (select doc_id from td_cms_chnl_ref_doc where chnl_id=" + chlId + ") ");
			if (!StringUtil.isEmpty(docType)) {
//				sql.append(" and t.doc_kind = ").append(docType);// 关联文档类型
				sql.append(" and t.doc_class = '").append(docType).append("'");// 关联文档类型
			}
			/*sql.append(" order by ordersq,order_no desc,seq,ordertime desc,docwtime desc,publishtime desc,DOCUMENT_ID desc) p ");// 如果count为-1默认查询所有的记录*/
			sql.append(" ) p ORDER BY p.ordersq, p.order_no DESC, p.seq,  p.ordertime DESC,  p.docwtime DESC,  p.publishtime DESC,  p.DOCUMENT_ID DESC");
//			System.out.println("=======================" + sql);
			tm.begin();
			db.executeSelect(sql.toString(), offset, maxItem);
			Document doc;
			List datas = new ArrayList();

			for (int i = 0; i < db.size(); i++) {
				doc = new Document();
				// 处理引用频道
				// add by ge.tao
				// 2007-09-14
				if (db.getInt(i, "DOCTYPE") == Document.DOCUMENT_CHANNEL) {
					String channelid = String.valueOf(db.getLong(i, "DOCUMENT_ID"));
					Channel refChannel = new ChannelManagerImpl().getChannelInfo(channelid);
					// doc.setRefChannel(refChannel);
					// doc.setDoctype(Document.DOCUMENT_CHANNEL);
					datas.add(refChannel);
				} else {
					doc.setDocument_id(db.getInt(i, "DOCUMENT_ID"));
					doc.setTitle(db.getString(i, "TITLE"));
					doc.setSubtitle(db.getString(i, "SUBTITLE"));
					doc.setAuthor(db.getString(i, "AUTHOR"));
					doc.setChanel_id(db.getInt(i, "CHANNEL_ID"));
					doc.setKeywords(db.getString(i, "KEYWORDS"));
					doc.setDocabstract(db.getString(i, "DOCABSTRACT"));
					doc.setDoctype(db.getInt(i, "DOCTYPE"));
					doc.setDocwtime(db.getDate(i, "DOCWTIME"));
					doc.setTitlecolor(db.getString(i, "TITLECOLOR"));
					doc.setCreateTime(db.getDate(i, "CREATETIME"));
					doc.setCreateUser(db.getInt(i, "CREATEUSER"));
					doc.setDocsource_id(db.getInt(i, "DOCSOURCE_ID"));
					doc.setDetailtemplate_id(db.getInt(i, "DETAILTEMPLATE_ID"));
					doc.setLinktarget(db.getString(i, "LINKTARGET"));
					doc.setLinkfile(db.getString(i, "linkfile"));
					doc.setContent(db.getString(i,"content"));
					doc.setFlowId(db.getInt(i, "FLOW_ID"));
					doc.setDoc_level(db.getInt(i, "DOC_LEVEL"));
					doc.setDoc_kind(db.getInt(i, "DOC_KIND"));
					doc.setParentDetailTpl(db.getString(i, "PARENT_DETAIL_TPL"));
					doc.setPublishTime(db.getDate(i, "publishtime"));
					doc.setPicPath(db.getString(i, "pic_path"));

					doc.setMediapath(db.getString(i, "mediapath"));
					doc.setPublishfilename(db.getString(i, "publishfilename"));
					// doc.setc(db.getString(i,"commentswitch"));
					doc.setSecondtitle(db.getString(i, "secondtitle"));
					doc.setIsNew(db.getInt(i, "isnew"));
					doc.setNewPicPath(db.getString(i, "newpic_path"));
					doc.setDocsource_name(db.getString(i, "source_name"));
					doc.setExt_class(db.getString(i, "ext_class"));
					doc.setExt_djh(db.getString(i, "ext_djh"));
					doc.setExt_index(db.getString(i, "ext_index"));
					doc.setExt_org(db.getString(i, "ext_org"));
					doc.setExt_wh(db.getString(i, "ext_wh"));
					// String str = "select SRCNAME from TD_CMS_DOCSOURCE where DOCSOURCE_ID ="
					// + db.getInt(i, "DOCSOURCE_ID") + "";
					//
					// db1.executeSelect(str);
					// if (db1.size() > 0) {
					// doc.setDocsource_name(db1.getString(0, "SRCNAME"));
					// }
					int isref = db.getInt(i, "ordersq");
					doc.setRef(isref == 1 ? false : true);// 判断是否是引用的文档：true为是，flase为不是
					/* 装载扩展字段数据 */
					Map docExtField = (new DocumentManagerImpl()).getDocExtFieldMap(doc.getDocument_id() + "");
					doc.setDocExtField(docExtField);
					/* 装载系统扩展字段数据 */
					doc.setExtColumn(extManager.getExtColumnInfo(i,db));
					doc.setOrdertime(db.getDate(i, "ordertime"));
					
					datas.add(doc);
					if(loaddocrelatepic)
					{
						List pics = CMSUtil.getCMSDriverConfiguration().getCMSService().getDocumentManager().getPicturesOfDocument(doc.getDocument_id());
						doc.setRelatePics(pics);
						
					}
				}

			}
			tm.commit();
			list.setDatas(datas);
			list.setTotalSize(db.getLongTotalSize());
			// }
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}
		finally
		{
			tm.release();
		}

		return list;
	}

	private String build(String[] ids) {
		StringBuffer ret = new StringBuffer();
		for (int i = 0; i < ids.length; i++) {
			if (i == 0) {
				ret.append(ids[i]);
			} else {
				ret.append(",").append(ids[i]);
			}

		}
		return ret.toString();
	}

	/**
	 * 按 文档发布时间 排序 翻页!!!
	 * 通过频道显示名称获取频道最近发布的相应数目的分页文档列表
	 * 包括那些文档状态为“正在发布中”的文档
	 * (对于那些包含于聚合文档的文档不在其列)
	 * 最近发布的相应数目的文档列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * 排除filters数组中的文档
	 * @author xinwang.jiao
	 * @param String DisplayName
	 * @return List<Document>
	 * @throws ChannelManagerException
	 * add by xinwang.jiao 2007.6.11
	 */
	public ListInfo getLatestPubAndPubingDocListByFilter(String siteid, String DisplayName, int offset, int maxItem,
			String[] filterIDs) throws ChannelManagerException {
		ListInfo list = new ListInfo();
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		DBUtil db = new DBUtil();

		StringBuffer sql = new StringBuffer();
		try {
			// sql = "select t.channel_id from td_cms_channel t " +
			// " where t.display_name = '" + DisplayName + "' and t.site_id = "
			// + siteid;
			// db.executeSelect(sql);
			// int chlId;
			ChannelCacheManager cm = (ChannelCacheManager) SiteCacheManager.getInstance()
					.getChannelCacheManager(siteid);
			long chlId = ((Channel) cm.getChannelByDisplayName(DisplayName)).getChannelId();
			// if(db.size() > 0)

			String ids = this.build(filterIDs);
			// {
			// chlId = db.getInt(0,"channel_id");
			sql.append(
					"select p.* from(select t.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, ")
					.append("DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, t.DOCSOURCE_ID,nvl(ds.srcname,'未知') as source_name, DETAILTEMPLATE_ID, ")
					.append("LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,")
					.append("case when DOCTYPE=1 ")
					.append("then t.content else null end linkfile,pic_path,mediapath,publishfilename,commentswitch,secondtitle,")
					.append("isnew,newpic_path,").append("nvl(a.order_no,-1) as order_no,1 as ordersq,ordertime,seq ")
					.append(" from td_cms_document t ").append(" left outer join (select * from td_cms_doc_arrange a ")
					.append(" where ");

			sql.append(" to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<=")
					.append(DBUtil.getDBAdapter().to_date(new Date()))
					.append(" and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>=")
					.append(DBUtil.getDBAdapter().to_date(new Date())).append(") a ")
					.append(" on t.document_id = a.document_id ")
					.append("inner join TD_CMS_DOCSOURCE ds on t.DOCSOURCE_ID = ds.DOCSOURCE_ID")
					.append(" where (STATUS = ").append(DocumentStatus.PUBLISHED.getStatus()).append(" or STATUS = ")
					.append(DocumentStatus.PUBLISHING.getStatus()).append(") ")
					.append(" and ISDELETED = 0 and t.document_id not ")
					.append(" in(select c.id_by_aggr from td_cms_doc_aggregation c ")
					.append(" inner join td_cms_document z1 on c.id_by_aggr = z1.document_id ")
					.append(" inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id = ")
					.append(chlId).append(") ");
			if (!ids.equals(""))
				sql.append(" and t.document_id not in (").append(ids).append(")");
			sql.append(" and CHANNEL_ID =")
					.append(chlId)
					.append(" union all ")
					// 联合查询
					.append(" select c.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, ")
					.append("DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, c.DOCSOURCE_ID,nvl(ds.srcname,'未知') as source_name, DETAILTEMPLATE_ID, ")
					.append("LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,")
					.append("case when DOCTYPE=1 ")
					.append("then content else null end linkfile,pic_path,mediapath,publishfilename,commentswitch,secondtitle,")
					.append("isnew,newpic_path,")
					.append("nvl(e.order_no,-1) as order_no,2 as ordersq,ordertime,seq ")
					.append(" from td_cms_document c, TD_CMS_DOCSOURCE ds, td_cms_chnl_ref_doc d ")
					.append(" left outer join (select * from td_cms_doc_arrange a ")
					.append(" where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<=")
					.append(DBUtil.getDBAdapter().to_date(new Date()))
					.append(" and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>=")
					.append(DBUtil.getDBAdapter().to_date(new Date()))
					.append(") e ")
					.append(" on d.doc_id = e.document_id ")

					.append(" where  c.DOCSOURCE_ID = ds.DOCSOURCE_ID and d.citetype = 0 and ")
					// 查询引用文档类型为文档的引用文档，没有对引用频道的情况的处理
					.append(" c.document_id=d.doc_id and (STATUS = ")
					.append(DocumentStatus.PUBLISHED.getStatus())
					.append(" or STATUS = ")
					.append(DocumentStatus.PUBLISHING.getStatus())
					.append(") ")
					.append(" and ISDELETED = 0 and d.doc_id not ")
					.append(" in(select c.id_by_aggr from td_cms_doc_aggregation c ")
					.append(" inner join td_cms_document z1 on c.id_by_aggr = z1.document_id ")
					.append(" inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id = ")
					.append(chlId)
					.append(") ")
					.append(" and d.chnl_id =")
					.append(chlId)
					.append(" order by order_no desc,seq,ordertime desc,docwtime desc,publishtime desc,DOCUMENT_ID desc) p ");// 如果count为-1默认查询所有的记录
			// System.out.println(sql);

			if (maxItem <= 0) {
				db.executeSelect(sql.toString());
			} else {
				db.executeSelect(sql.toString(), offset, maxItem);
			}
			Document doc;
			List datas = new ArrayList();

			for (int i = 0; i < db.size(); i++) {
				doc = new Document();
				// 处理引用频道
				// add by ge.tao
				// 2007-09-14
				if (db.getInt(i, "DOCTYPE") == Document.DOCUMENT_CHANNEL) {
					String channelid = String.valueOf(db.getLong(i, "DOCUMENT_ID"));
					Channel refChannel = new ChannelManagerImpl().getChannelInfo(channelid);
					// doc.setRefChannel(refChannel);
					// doc.setDoctype(Document.DOCUMENT_CHANNEL);
					datas.add(refChannel);
				} else {
					doc.setDocument_id(db.getInt(i, "DOCUMENT_ID"));
					doc.setTitle(db.getString(i, "TITLE"));
					doc.setSubtitle(db.getString(i, "SUBTITLE"));
					doc.setAuthor(db.getString(i, "AUTHOR"));
					doc.setChanel_id(db.getInt(i, "CHANNEL_ID"));
					doc.setKeywords(db.getString(i, "KEYWORDS"));
					doc.setDocabstract(db.getString(i, "DOCABSTRACT"));
					doc.setDoctype(db.getInt(i, "DOCTYPE"));
					doc.setDocwtime(db.getDate(i, "DOCWTIME"));
					doc.setTitlecolor(db.getString(i, "TITLECOLOR"));
					doc.setCreateTime(db.getDate(i, "CREATETIME"));
					doc.setCreateUser(db.getInt(i, "CREATEUSER"));
					doc.setDocsource_id(db.getInt(i, "DOCSOURCE_ID"));
					doc.setDetailtemplate_id(db.getInt(i, "DETAILTEMPLATE_ID"));
					doc.setLinktarget(db.getString(i, "LINKTARGET"));
					doc.setLinkfile(db.getString(i, "linkfile"));
					doc.setFlowId(db.getInt(i, "FLOW_ID"));
					doc.setDoc_level(db.getInt(i, "DOC_LEVEL"));
					doc.setDoc_kind(db.getInt(i, "DOC_KIND"));
					doc.setParentDetailTpl(db.getString(i, "PARENT_DETAIL_TPL"));
					doc.setPublishTime(db.getDate(i, "publishtime"));
					doc.setPicPath(db.getString(i, "pic_path"));

					doc.setMediapath(db.getString(i, "mediapath"));
					doc.setPublishfilename(db.getString(i, "publishfilename"));
					// doc.setc(db.getString(i,"commentswitch"));
					doc.setSecondtitle(db.getString(i, "secondtitle"));
					doc.setIsNew(db.getInt(i, "isnew"));
					doc.setNewPicPath(db.getString(i, "newpic_path"));
					doc.setDocsource_name(db.getString(i, "source_name"));
					// String str = "select SRCNAME from TD_CMS_DOCSOURCE where DOCSOURCE_ID ="
					// + db.getInt(i, "DOCSOURCE_ID") + "";
					//
					// db1.executeSelect(str);
					// if (db1.size() > 0) {
					// doc.setDocsource_name(db1.getString(0, "SRCNAME"));
					// }
					int isref = db.getInt(i, "ordersq");
					doc.setRef(isref == 1 ? false : true);// 判断是否是引用的文档：true为是，flase为不是
					/* 装载扩展字段数据 */
					Map docExtField = (new DocumentManagerImpl()).getDocExtFieldMap(doc.getDocument_id() + "");
					doc.setDocExtField(docExtField);
					/* 装载系统扩展字段数据 */
					doc.setExtColumn(extManager.getExtColumnInfo(i,db));
					datas.add(doc);
				}

			}
			list.setDatas(datas);
			list.setTotalSize(db.getTotalSize());
			// }
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}

		return list;
	}

	/**
	 * 按 文档发布时间 排序 列表!!!
	 * 通过频道显示名称获取频道最近发布的相应数目的分页文档列表
	 * 包括那些文档状态为“正在发布中”的文档
	 * (对于那些包含于聚合文档的文档不在其列)
	 * 最近发布的相应数目的文档列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * 排除filters数组中的文档
	 * @author xinwang.jiao
	 * @param String DisplayName
	 * @return List<Document>
	 * @throws ChannelManagerException
	 * add by xinwang.jiao 2007.6.11
	 */
	public List getLatestPubAndPubingDocListByFilter(String siteid, String DisplayName, int count, String[] filterIDs)
			throws ChannelManagerException {
		return getLatestPubAndPubingDocListByFilter(siteid, DisplayName, 0, count, filterIDs).getDatas();
	}

	/**
	 * 不翻页 子频道
	 * 根据频道显示名称取直接子频道，该频道必须有首页
	 */
	public List getDirectSubChannels(String siteid, String displayName, int count) throws ChannelManagerException {
		String channelId = "";
		String subsql = "";
		if (displayName != null && displayName.trim().length() > 0 && !"root".equalsIgnoreCase(displayName)) {
			channelId = String.valueOf(getChannelInfoByDisplayName(siteid, displayName).getChannelId());
			if (channelId == null || channelId.trim().length() == 0) {
				throw new ChannelManagerException("没有提供频道id,无法返回子频道列表.");
			} else {
				subsql = " and PARENT_ID=" + channelId;
			}
		} else {
			subsql = " and (PARENT_ID='' or PARENT_ID is null ) ";
		}
		try {
			DBUtil db = new DBUtil();
			StringBuffer sql = new StringBuffer();
			
			sql.append((count <= 0 ? "" : (" select * from ("))).append("select CHANNEL_ID, NAME,DISPLAY_NAME, PUB_FILE_NAME, ")
					.append("PARENT_ID,CHNL_PATH,CREATEUSER,CREATETIME, ")
					.append("ORDER_NO,SITE_ID,STATUS,OUTLINE_TPL_ID, ")
					.append("DETAIL_TPL_ID, ")
					.append( "CHNL_OUTLINE_DYNAMIC,DOC_DYNAMIC, ")
					.append("CHNL_OUTLINE_PROTECT, DOC_PROTECT,")
					.append("PARENT_WORKFLOW,ISNAVIGATOR,NAVIGATORLEVEL,MOUSEINIMAGE, MOUSEOUTIMAGE, MOUSECLICKIMAGE, MOUSEUPIMAGE , OUTLINEPICTURE, PAGEFLAG, INDEXPAGEPATH, COMMENTSWITCH,  COMMENT_TEMPLATE_ID, COMMENTPAGEPATH ")
					.append( "from TD_CMS_CHANNEL where  site_id= ")
					.append(siteid)
					.append(" and  status=0 ")
					.append(subsql)
					.append(" and (1=1 )")
					.append( " and ((PAGEFLAG=0 or PAGEFLAG is null )and OUTLINE_TPL_ID is not null or (PAGEFLAG=1 or PAGEFLAG=2) and INDEXPAGEPATH is not null)")
					.append(" order by order_no desc,createtime desc ").append((count <= 0 ? "" : (" ) where rownum <=" + String.valueOf(count))));
			// System.out.println("sql=" + sql);
			db.executeSelect(sql.toString());
			List channels = new ArrayList();
			for (int i = 0; i < db.size(); i++) {
				Channel channel = new Channel();
				channel.setChannelId(db.getLong(i, "CHANNEL_ID"));
				channel.setName(db.getString(i, "NAME"));
				channel.setDisplayName(db.getString(i, "DISPLAY_NAME"));
				channel.setParentChannelId(db.getInt(i, "PARENT_ID"));
				channel.setChannelPath(db.getString(i, "CHNL_PATH"));
				channel.setCreateUser(db.getLong(i, "CREATEUSER"));
				channel.setCreateTime(db.getDate(i, "CREATETIME"));
				channel.setOrderNo(db.getInt(i, "ORDER_NO"));
				channel.setSiteId(db.getLong(i, "SITE_ID"));
				channel.setStaus(db.getInt(i, "STATUS"));
				channel.setOutlineTemplateId(db.getInt(i, "OUTLINE_TPL_ID"));
				channel.setDetailTemplateId(db.getInt(i, "DETAIL_TPL_ID"));
				// channel.setWorkflow(db.getInt(i, "WORKFLOW"));
				channel.setOutlineIsDynamic(db.getInt(i, "CHNL_OUTLINE_DYNAMIC"));
				channel.setDocIsDynamic(db.getInt(i, "DOC_DYNAMIC"));
				channel.setOutlineIsProtect(db.getInt(i, "CHNL_OUTLINE_PROTECT"));
				channel.setDocIsProtect(db.getInt(i, "DOC_PROTECT"));
				channel.setWorkflowIsFromParent(db.getInt(i, "PARENT_WORKFLOW"));
				channel.setNavigator(0 != db.getInt(i, "ISNAVIGATOR"));
				channel.setNavigatorLevel(db.getInt(i, "NAVIGATORLEVEL"));
				channel.setMouseInImage(db.getString(i, "MOUSEINIMAGE"));
				channel.setMouseOutImage(db.getString(i, "MOUSEOUTIMAGE"));
				channel.setMouseClickImage(db.getString(i, "MOUSECLICKIMAGE"));
				channel.setMouseUpImage(db.getString(i, "MOUSEUPIMAGE"));
				String[] pubFileNames = StringOperate.getFileNameAndExtName(db.getString(i, "PUB_FILE_NAME"));
				channel.setPubFileName(db.getString(0, "pub_file_name"));
				channel.setPubFileNameSuffix(pubFileNames[1]);
				channel.setOutlinepicture(db.getString(i, "OUTLINEPICTURE"));
				channel.setPageflag(db.getInt(i, "PAGEFLAG"));
				channel.setIndexpagepath(db.getString(i, "INDEXPAGEPATH"));
				channel.setCommentTemplateId(db.getInt(i, "COMMENT_TEMPLATE_ID"));
				channel.setCommentPagePath(db.getString(i, "COMMENTPAGEPATH"));
				channels.add(channel);
			}
			return channels;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("发生sql异常,无法返回频道信息.异常信息为:" + e.getMessage());
		}
	}

	/**
	 * 
	 * 根据频道显示名称取直接子频道，该频道必须有首页 翻页!!!
	 * 
	 */
	public ListInfo getDirectSubChannels(String siteid, String displayName, int offset, int maxpageitems)
			throws ChannelManagerException {
		String channelId = "";
		String subsql = "";
		if (displayName != null && displayName.trim().length() > 0 && !"root".equalsIgnoreCase(displayName)) {
			channelId = String.valueOf(getChannelInfoByDisplayName(siteid, displayName).getChannelId());
			if (channelId == null || channelId.trim().length() == 0) {
				throw new ChannelManagerException("没有提供频道id,无法返回子频道列表.");
			} else {
				subsql = " and PARENT_ID=" + channelId;
			}
		} else {
			subsql = " and (PARENT_ID='' or PARENT_ID is null ) ";
		}
		try {
			DBUtil db = new DBUtil();
			String sql = "select CHANNEL_ID, NAME,DISPLAY_NAME, PUB_FILE_NAME, "
					+ "PARENT_ID,CHNL_PATH,CREATEUSER,CREATETIME, "
					+ "ORDER_NO,SITE_ID,STATUS,OUTLINE_TPL_ID, "
					+ "DETAIL_TPL_ID, "
					// + "DETAIL_TPL_ID, CHANNEL_FLOW_ID(channel_id) WORKFLOW,"
					+ "CHNL_OUTLINE_DYNAMIC,DOC_DYNAMIC, "
					+ "CHNL_OUTLINE_PROTECT, DOC_PROTECT,"
					+ "PARENT_WORKFLOW,ISNAVIGATOR,NAVIGATORLEVEL,MOUSEINIMAGE, MOUSEOUTIMAGE, MOUSECLICKIMAGE, MOUSEUPIMAGE , OUTLINEPICTURE, PAGEFLAG, INDEXPAGEPATH, COMMENTSWITCH,  COMMENT_TEMPLATE_ID, COMMENTPAGEPATH,channel_desc "
					+ "from TD_CMS_CHANNEL where  site_id= "
					+ siteid
					+ " and  status=0 "
					+ subsql
					+ " and ((PAGEFLAG=0 or PAGEFLAG is null )and OUTLINE_TPL_ID is not null or (PAGEFLAG=1 or PAGEFLAG=2) and INDEXPAGEPATH is not null)"
					+ " order by order_no,channel_id ";
			// System.out.println("sql=" + sql);
			db.executeSelect(sql, offset, maxpageitems);
			List channels = new ArrayList();
			for (int i = 0; i < db.size(); i++) {
				Channel channel = new Channel();
				channel.setChannelId(db.getLong(i, "CHANNEL_ID"));
				channel.setName(db.getString(i, "NAME"));
				channel.setDisplayName(db.getString(i, "DISPLAY_NAME"));
				channel.setParentChannelId(db.getInt(i, "PARENT_ID"));
				channel.setChannelPath(db.getString(i, "CHNL_PATH"));
				channel.setCreateUser(db.getLong(i, "CREATEUSER"));
				channel.setCreateTime(db.getDate(i, "CREATETIME"));
				channel.setOrderNo(db.getInt(i, "ORDER_NO"));
				channel.setSiteId(db.getLong(i, "SITE_ID"));
				channel.setStaus(db.getInt(i, "STATUS"));
				channel.setOutlineTemplateId(db.getInt(i, "OUTLINE_TPL_ID"));
				channel.setDetailTemplateId(db.getInt(i, "DETAIL_TPL_ID"));
				// channel.setWorkflow(db.getInt(i, "WORKFLOW"));
				channel.setOutlineIsDynamic(db.getInt(i, "CHNL_OUTLINE_DYNAMIC"));
				channel.setDocIsDynamic(db.getInt(i, "DOC_DYNAMIC"));
				channel.setOutlineIsProtect(db.getInt(i, "CHNL_OUTLINE_PROTECT"));
				channel.setDocIsProtect(db.getInt(i, "DOC_PROTECT"));
				channel.setWorkflowIsFromParent(db.getInt(i, "PARENT_WORKFLOW"));
				channel.setNavigator(0 != db.getInt(i, "ISNAVIGATOR"));
				channel.setNavigatorLevel(db.getInt(i, "NAVIGATORLEVEL"));
				channel.setMouseInImage(db.getString(i, "MOUSEINIMAGE"));
				channel.setMouseOutImage(db.getString(i, "MOUSEOUTIMAGE"));
				channel.setMouseClickImage(db.getString(i, "MOUSECLICKIMAGE"));
				channel.setMouseUpImage(db.getString(i, "MOUSEUPIMAGE"));
				String[] pubFileNames = StringOperate.getFileNameAndExtName(db.getString(i, "PUB_FILE_NAME"));
				channel.setPubFileName(db.getString(0, "pub_file_name"));
				channel.setPubFileNameSuffix(pubFileNames[1]);
				channel.setOutlinepicture(db.getString(i, "OUTLINEPICTURE"));
				channel.setPageflag(db.getInt(i, "PAGEFLAG"));
				channel.setIndexpagepath(db.getString(i, "INDEXPAGEPATH"));
				channel.setCommentTemplateId(db.getInt(i, "COMMENT_TEMPLATE_ID"));
				channel.setCommentPagePath(db.getString(i, "COMMENTPAGEPATH"));
				channel.setChannel_desc(db.getString(i,"channel_desc"));
				channels.add(channel);
			}
			ListInfo listinfo = new ListInfo();
			listinfo.setDatas(channels);
			listinfo.setTotalSize(db.getTotalSize());
			return listinfo;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("发生sql异常,无法返回频道信息.异常信息为:" + e.getMessage());
		}
	}

	/**
	 * 翻页 子频道
	 * 根据频道显示名称取直接子频道，该频道必须有首页 kai.hu 070530
	 */
	public ListInfo getDirectSubChannels(String siteid, String displayName, long offset, int pageitems)
			throws ChannelManagerException {
		String channelId = "";
		String subsql = "";
		if (displayName != null && displayName.trim().length() > 0 && !"root".equalsIgnoreCase(displayName)) {
			channelId = String.valueOf(getChannelInfoByDisplayName(siteid, displayName).getChannelId());
			if (channelId == null || channelId.trim().length() == 0) {
				throw new ChannelManagerException("没有提供频道id,无法返回子频道列表.");
			} else {
				subsql = " and PARENT_ID=" + channelId;
			}
		} else {
			subsql = " and (PARENT_ID='' or PARENT_ID is null ) ";
		}
		try {
			DBUtil db = new DBUtil();
			String sql = "select CHANNEL_ID, NAME,DISPLAY_NAME, PUB_FILE_NAME, "
					+ "PARENT_ID,CHNL_PATH,CREATEUSER,CREATETIME, "
					+ "ORDER_NO,SITE_ID,STATUS,OUTLINE_TPL_ID, "
					+ "DETAIL_TPL_ID, "
					// + "DETAIL_TPL_ID, CHANNEL_FLOW_ID(channel_id) WORKFLOW,"
					+ "CHNL_OUTLINE_DYNAMIC,DOC_DYNAMIC, "
					+ "CHNL_OUTLINE_PROTECT, DOC_PROTECT,"
					+ "PARENT_WORKFLOW,ISNAVIGATOR,NAVIGATORLEVEL,MOUSEINIMAGE, MOUSEOUTIMAGE, MOUSECLICKIMAGE, MOUSEUPIMAGE , OUTLINEPICTURE, PAGEFLAG, INDEXPAGEPATH, COMMENTSWITCH, "
					+ " COMMENT_TEMPLATE_ID, COMMENTPAGEPATH ,channel_desc"
					+ "from TD_CMS_CHANNEL where  site_id= "
					+ siteid
					+ " and  status=0 "
					+ subsql
					+ " and ((PAGEFLAG=0 or PAGEFLAG is null )and OUTLINE_TPL_ID is not null or (PAGEFLAG=1 or PAGEFLAG=2) and INDEXPAGEPATH is not null)"
					+ " order by order_no,channel_id desc";
			// System.out.println("sql=" + sql);
			db.executeSelect(sql, offset, pageitems);
			List channels = new ArrayList();
			for (int i = 0; i < db.size(); i++) {
				Channel channel = new Channel();
				channel.setChannelId(db.getLong(i, "CHANNEL_ID"));
				channel.setName(db.getString(i, "NAME"));
				channel.setDisplayName(db.getString(i, "DISPLAY_NAME"));
				channel.setParentChannelId(db.getInt(i, "PARENT_ID"));
				channel.setChannelPath(db.getString(i, "CHNL_PATH"));
				channel.setCreateUser(db.getLong(i, "CREATEUSER"));
				channel.setCreateTime(db.getDate(i, "CREATETIME"));
				channel.setOrderNo(db.getInt(i, "ORDER_NO"));
				channel.setSiteId(db.getLong(i, "SITE_ID"));
				channel.setStaus(db.getInt(i, "STATUS"));
				channel.setOutlineTemplateId(db.getInt(i, "OUTLINE_TPL_ID"));
				channel.setDetailTemplateId(db.getInt(i, "DETAIL_TPL_ID"));
				// channel.setWorkflow(db.getInt(i, "WORKFLOW"));
				channel.setOutlineIsDynamic(db.getInt(i, "CHNL_OUTLINE_DYNAMIC"));
				channel.setDocIsDynamic(db.getInt(i, "DOC_DYNAMIC"));
				channel.setOutlineIsProtect(db.getInt(i, "CHNL_OUTLINE_PROTECT"));
				channel.setDocIsProtect(db.getInt(i, "DOC_PROTECT"));
				channel.setWorkflowIsFromParent(db.getInt(i, "PARENT_WORKFLOW"));
				channel.setNavigator(0 != db.getInt(i, "ISNAVIGATOR"));
				channel.setNavigatorLevel(db.getInt(i, "NAVIGATORLEVEL"));
				channel.setMouseInImage(db.getString(i, "MOUSEINIMAGE"));
				channel.setMouseOutImage(db.getString(i, "MOUSEOUTIMAGE"));
				channel.setMouseClickImage(db.getString(i, "MOUSECLICKIMAGE"));
				channel.setMouseUpImage(db.getString(i, "MOUSEUPIMAGE"));
				String[] pubFileNames = StringOperate.getFileNameAndExtName(db.getString(i, "PUB_FILE_NAME"));
				channel.setPubFileName(db.getString(0, "pub_file_name"));
				channel.setPubFileNameSuffix(pubFileNames[1]);
				channel.setOutlinepicture(db.getString(i, "OUTLINEPICTURE"));
				channel.setPageflag(db.getInt(i, "PAGEFLAG"));
				channel.setIndexpagepath(db.getString(i, "INDEXPAGEPATH"));
				channel.setCommentTemplateId(db.getInt(i, "COMMENT_TEMPLATE_ID"));
				channel.setCommentPagePath(db.getString(i, "COMMENTPAGEPATH"));
				channel.setChannel_desc(db.getString(i,"channel_desc"));
				channels.add(channel);
			}
			ListInfo listinfo = new ListInfo();
			listinfo.setDatas(channels);
			listinfo.setTotalSize(db.getTotalSize());
			return listinfo;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("发生sql异常,无法返回频道信息.异常信息为:" + e.getMessage());
		}
	}

	/**
	 * 不翻页 导航子频道
	 * 根据频道显示名称取直接导航子频道，该频道必须有首页
	 * 在概览标签里面使用,当概览标签的channel属性: 
	 * channel:指定为特定的频道时, 获取该频道下的 导航子频道
	 * channel:指定为"root"时, 获取站点下的一级导航频道
	 */
	public List getDirectSubNaviChannels(String siteid, String displayName, int count) throws ChannelManagerException {
		String channelId = "";
		String subsql = "";
		if (displayName != null && displayName.trim().length() > 0 && !"root".equalsIgnoreCase(displayName)) {
			channelId = String.valueOf(getChannelInfoByDisplayName(siteid, displayName).getChannelId());
			if (channelId == null || channelId.trim().length() == 0) {
				throw new ChannelManagerException("没有提供频道id,无法返回子频道列表.");
			} else {
				subsql = " and PARENT_ID=" + channelId;
			}
		} else {
			subsql = " and (PARENT_ID='' or PARENT_ID is null ) ";
		}

		try {
			DBUtil db = new DBUtil();
			String sql = "select * from(select CHANNEL_ID, NAME,DISPLAY_NAME, PUB_FILE_NAME, "
					+ "PARENT_ID,CHNL_PATH,CREATEUSER,CREATETIME, "
					+ "ORDER_NO,SITE_ID,STATUS,OUTLINE_TPL_ID, "
					+ "DETAIL_TPL_ID, "
					// + "DETAIL_TPL_ID, CHANNEL_FLOW_ID(channel_id) WORKFLOW,"
					+ "CHNL_OUTLINE_DYNAMIC,DOC_DYNAMIC, "
					+ "CHNL_OUTLINE_PROTECT, DOC_PROTECT,"
					+ "PARENT_WORKFLOW,ISNAVIGATOR,NAVIGATORLEVEL,MOUSEINIMAGE, MOUSEOUTIMAGE, MOUSECLICKIMAGE, MOUSEUPIMAGE , OUTLINEPICTURE, PAGEFLAG, INDEXPAGEPATH, COMMENTSWITCH, "
					+ " COMMENT_TEMPLATE_ID, COMMENTPAGEPATH ,channel_desc"
					+ " from TD_CMS_CHANNEL where site_id= "
					+ siteid
					+ " and  status=0 "
					+ subsql
					+ " and ISNAVIGATOR=1 "
 			        + " order by order_no,channel_id desc)  "
					+ ((count < 1) ? "" : "where rownum <=" + count);// 如果count为-1默认查询所有的记录

			System.out.println("sql=" + sql);
			db.executeSelect(sql);

			List channels = new ArrayList();
			for (int i = 0; i < db.size(); i++) {
				Channel channel = new Channel();
				channel.setChannelId(db.getLong(i, "CHANNEL_ID"));
				channel.setName(db.getString(i, "NAME"));
				channel.setDisplayName(db.getString(i, "DISPLAY_NAME"));
				channel.setParentChannelId(db.getInt(i, "PARENT_ID"));
				channel.setChannelPath(db.getString(i, "CHNL_PATH"));
				channel.setCreateUser(db.getLong(i, "CREATEUSER"));
				channel.setCreateTime(db.getDate(i, "CREATETIME"));
				channel.setOrderNo(db.getInt(i, "ORDER_NO"));
				channel.setSiteId(db.getLong(i, "SITE_ID"));
				channel.setStaus(db.getInt(i, "STATUS"));
				channel.setOutlineTemplateId(db.getInt(i, "OUTLINE_TPL_ID"));
				channel.setDetailTemplateId(db.getInt(i, "DETAIL_TPL_ID"));
				// channel.setWorkflow(db.getInt(i, "WORKFLOW"));
				channel.setOutlineIsDynamic(db.getInt(i, "CHNL_OUTLINE_DYNAMIC"));
				channel.setDocIsDynamic(db.getInt(i, "DOC_DYNAMIC"));
				channel.setOutlineIsProtect(db.getInt(i, "CHNL_OUTLINE_PROTECT"));
				channel.setDocIsProtect(db.getInt(i, "DOC_PROTECT"));
				channel.setWorkflowIsFromParent(db.getInt(i, "PARENT_WORKFLOW"));
				channel.setNavigator(0 != db.getInt(i, "ISNAVIGATOR"));
				channel.setNavigatorLevel(db.getInt(i, "NAVIGATORLEVEL"));
				channel.setMouseInImage(db.getString(i, "MOUSEINIMAGE"));
				channel.setMouseOutImage(db.getString(i, "MOUSEOUTIMAGE"));
				channel.setMouseClickImage(db.getString(i, "MOUSECLICKIMAGE"));
				channel.setMouseUpImage(db.getString(i, "MOUSEUPIMAGE"));
				String[] pubFileNames = StringOperate.getFileNameAndExtName(db.getString(i, "PUB_FILE_NAME"));
				channel.setPubFileName(db.getString(0, "pub_file_name"));
				channel.setPubFileNameSuffix(pubFileNames[1]);
				channel.setOutlinepicture(db.getString(i, "OUTLINEPICTURE"));
				channel.setPageflag(db.getInt(i, "PAGEFLAG"));
				channel.setIndexpagepath(db.getString(i, "INDEXPAGEPATH"));
				channel.setCommentTemplateId(db.getInt(i, "COMMENT_TEMPLATE_ID"));
				channel.setCommentPagePath(db.getString(i, "COMMENTPAGEPATH"));
				channel.setChannel_desc(db.getString(i,"channel_desc"));
				channels.add(channel);
			}
			return channels;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("发生sql异常,无法返回频道信息.异常信息为:" + e.getMessage());
		}
	}

	public List getDirectNaviChannels(String siteid, String displayName, int count, String level, String levelDegree)
			throws ChannelManagerException {
		String channelId = "";
		String subsql = "";

		// 设置默认值
		int ilevel = 1;
		int ilevelDegree = 2;

		try {
			ilevel = Integer.parseInt(level);
		} catch (NumberFormatException e) {
		}

		try {
			ilevelDegree = Integer.parseInt(levelDegree);
		} catch (NumberFormatException e) {
		}

		if (displayName != null && displayName.trim().length() > 0 && !"root".equalsIgnoreCase(displayName)) {
			channelId = String.valueOf(getChannelInfoByDisplayName(siteid, displayName).getChannelId());
			if (channelId == null || channelId.trim().length() == 0) {
				throw new ChannelManagerException("没有提供频道id,无法返回子频道列表.");
			} else {
				// 如果ilevel大于1时，显示ilevelDegree层的ilevel导航层级别
				// 否则显示ilevelDegree层的0导航层级别
				if (ilevel > 1) {

					subsql = " site_id = " + siteid + " and navigatorlevel = '" + ilevel + "' and level="
							+ ilevelDegree + " start with channel_id='" + channelId
							+ "' connect by prior channel_id = parent_id and level <=" + ilevelDegree;
				} else {
					subsql = " site_id = " + siteid + " and navigatorlevel = '0'  and level= " + ilevelDegree
							+ " start with channel_id='" + channelId
							+ "' connect by prior channel_id = parent_id and level <=" + ilevelDegree;
					// subsql = " site_id= "+ siteid +" and PARENT_ID="+ channelId;
				}

			}
		} else {
			subsql = " site_id = " + siteid + " and (PARENT_ID='' or PARENT_ID is null ) ";
		}

		try {
			DBUtil db = new DBUtil();
			String sql = "select CHANNEL_ID, NAME,DISPLAY_NAME, PUB_FILE_NAME, "
					+ "PARENT_ID,CHNL_PATH,CREATEUSER,CREATETIME, "
					+ "ORDER_NO,SITE_ID,STATUS,OUTLINE_TPL_ID, "
					+ "DETAIL_TPL_ID, "
					// + "DETAIL_TPL_ID, CHANNEL_FLOW_ID(channel_id) WORKFLOW,"
					+ "CHNL_OUTLINE_DYNAMIC,DOC_DYNAMIC, "
					+ "CHNL_OUTLINE_PROTECT, DOC_PROTECT,"
					+ "PARENT_WORKFLOW,ISNAVIGATOR,NAVIGATORLEVEL,MOUSEINIMAGE, MOUSEOUTIMAGE, MOUSECLICKIMAGE, MOUSEUPIMAGE , OUTLINEPICTURE, PAGEFLAG, INDEXPAGEPATH, COMMENTSWITCH, "
					+ " COMMENT_TEMPLATE_ID, COMMENTPAGEPATH,channel_desc "
					+ " from TD_CMS_CHANNEL where status=0 and"
					+ subsql
					+ " and ISNAVIGATOR=1 "
			        + ((count < 1) ? "" : "and rownum <=" + count)// 如果count为-1默认查询所有的记录
 			        + " order by order_no,channel_id ";
			
			System.out.println("sql=" + sql);
			db.executeSelect(sql);

			List channels = new ArrayList();
			for (int i = 0; i < db.size(); i++) {
				Channel channel = new Channel();
				channel.setChannelId(db.getLong(i, "CHANNEL_ID"));
				channel.setName(db.getString(i, "NAME"));
				channel.setDisplayName(db.getString(i, "DISPLAY_NAME"));
				channel.setParentChannelId(db.getInt(i, "PARENT_ID"));
				channel.setChannelPath(db.getString(i, "CHNL_PATH"));
				channel.setCreateUser(db.getLong(i, "CREATEUSER"));
				channel.setCreateTime(db.getDate(i, "CREATETIME"));
				channel.setOrderNo(db.getInt(i, "ORDER_NO"));
				channel.setSiteId(db.getLong(i, "SITE_ID"));
				channel.setStaus(db.getInt(i, "STATUS"));
				channel.setOutlineTemplateId(db.getInt(i, "OUTLINE_TPL_ID"));
				channel.setDetailTemplateId(db.getInt(i, "DETAIL_TPL_ID"));
				// channel.setWorkflow(db.getInt(i, "WORKFLOW"));
				channel.setOutlineIsDynamic(db.getInt(i, "CHNL_OUTLINE_DYNAMIC"));
				channel.setDocIsDynamic(db.getInt(i, "DOC_DYNAMIC"));
				channel.setOutlineIsProtect(db.getInt(i, "CHNL_OUTLINE_PROTECT"));
				channel.setDocIsProtect(db.getInt(i, "DOC_PROTECT"));
				channel.setWorkflowIsFromParent(db.getInt(i, "PARENT_WORKFLOW"));
				channel.setNavigator(0 != db.getInt(i, "ISNAVIGATOR"));
				channel.setNavigatorLevel(db.getInt(i, "NAVIGATORLEVEL"));
				channel.setMouseInImage(db.getString(i, "MOUSEINIMAGE"));
				channel.setMouseOutImage(db.getString(i, "MOUSEOUTIMAGE"));
				channel.setMouseClickImage(db.getString(i, "MOUSECLICKIMAGE"));
				channel.setMouseUpImage(db.getString(i, "MOUSEUPIMAGE"));
				String[] pubFileNames = StringOperate.getFileNameAndExtName(db.getString(i, "PUB_FILE_NAME"));
				channel.setPubFileName(db.getString(0, "pub_file_name"));
				channel.setPubFileNameSuffix(pubFileNames[1]);
				channel.setOutlinepicture(db.getString(i, "OUTLINEPICTURE"));
				channel.setPageflag(db.getInt(i, "PAGEFLAG"));
				channel.setIndexpagepath(db.getString(i, "INDEXPAGEPATH"));
				channel.setCommentTemplateId(db.getInt(i, "COMMENT_TEMPLATE_ID"));
				channel.setCommentPagePath(db.getString(i, "COMMENTPAGEPATH"));
				channel.setChannel_desc(db.getString(i,"channel_desc"));
				channels.add(channel);
			}
			return channels;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("发生sql异常,无法返回频道信息.异常信息为:" + e.getMessage());
		}
	}

	/**
	 * 翻页 导航子频道
	 * 根据频道显示名称取直接导航子频道，该频道必须有首页
	 * 在概览标签里面使用,当概览标签的channel属性: 
	 * channel:指定为特定的频道时, 获取该频道下的 导航子频道
	 * channel:指定为"root"时, 获取站点下的一级导航频道 
	 */
	public ListInfo getDirectSubNaviChannels(String siteid, String displayName, long offset, int pageitems)
			throws ChannelManagerException {
		String channelId = "";
		String subsql = "";
		if (displayName != null && displayName.trim().length() > 0 && !"root".equalsIgnoreCase(displayName)) {
			channelId = String.valueOf(getChannelInfoByDisplayName(siteid, displayName).getChannelId());
			if (channelId == null || channelId.trim().length() == 0) {
				throw new ChannelManagerException("没有提供频道id,无法返回子频道列表.");
			} else {
				subsql = " and PARENT_ID=" + channelId;
			}
		} else {
			subsql = " and (PARENT_ID='' or PARENT_ID is null ) ";
		}

		try {
			DBUtil db = new DBUtil();
			String sql = "select CHANNEL_ID, NAME,DISPLAY_NAME, PUB_FILE_NAME, "
					+ "PARENT_ID,CHNL_PATH,CREATEUSER,CREATETIME, "
					+ "ORDER_NO,SITE_ID,STATUS,OUTLINE_TPL_ID, "
					+ "DETAIL_TPL_ID, "
					// + "DETAIL_TPL_ID, CHANNEL_FLOW_ID(channel_id) WORKFLOW,"
					+ "CHNL_OUTLINE_DYNAMIC,DOC_DYNAMIC, "
					+ "CHNL_OUTLINE_PROTECT, DOC_PROTECT,"
					+ "PARENT_WORKFLOW,ISNAVIGATOR,NAVIGATORLEVEL,MOUSEINIMAGE, MOUSEOUTIMAGE, MOUSECLICKIMAGE, MOUSEUPIMAGE , OUTLINEPICTURE, PAGEFLAG, INDEXPAGEPATH, COMMENTSWITCH, "
					+ " COMMENT_TEMPLATE_ID, COMMENTPAGEPATH,channel_desc "
					+ " from TD_CMS_CHANNEL where  site_id= "
					+ siteid
					+ " and  status=0 "
					+ subsql
					+ " and ISNAVIGATOR=1 order by order_no,channel_id desc";
			// System.out.println("sql=" + sql);
			db.executeSelect(sql, offset, pageitems);
			List channels = new ArrayList();
			for (int i = 0; i < db.size(); i++) {
				Channel channel = new Channel();
				channel.setChannelId(db.getLong(i, "CHANNEL_ID"));
				channel.setName(db.getString(i, "NAME"));
				channel.setDisplayName(db.getString(i, "DISPLAY_NAME"));
				channel.setParentChannelId(db.getInt(i, "PARENT_ID"));
				channel.setChannelPath(db.getString(i, "CHNL_PATH"));
				channel.setCreateUser(db.getLong(i, "CREATEUSER"));
				channel.setCreateTime(db.getDate(i, "CREATETIME"));
				channel.setOrderNo(db.getInt(i, "ORDER_NO"));
				channel.setSiteId(db.getLong(i, "SITE_ID"));
				channel.setStaus(db.getInt(i, "STATUS"));
				channel.setOutlineTemplateId(db.getInt(i, "OUTLINE_TPL_ID"));
				channel.setDetailTemplateId(db.getInt(i, "DETAIL_TPL_ID"));
				// channel.setWorkflow(db.getInt(i, "WORKFLOW"));
				channel.setOutlineIsDynamic(db.getInt(i, "CHNL_OUTLINE_DYNAMIC"));
				channel.setDocIsDynamic(db.getInt(i, "DOC_DYNAMIC"));
				channel.setOutlineIsProtect(db.getInt(i, "CHNL_OUTLINE_PROTECT"));
				channel.setDocIsProtect(db.getInt(i, "DOC_PROTECT"));
				channel.setWorkflowIsFromParent(db.getInt(i, "PARENT_WORKFLOW"));
				channel.setNavigator(0 != db.getInt(i, "ISNAVIGATOR"));
				channel.setNavigatorLevel(db.getInt(i, "NAVIGATORLEVEL"));
				channel.setMouseInImage(db.getString(i, "MOUSEINIMAGE"));
				channel.setMouseOutImage(db.getString(i, "MOUSEOUTIMAGE"));
				channel.setMouseClickImage(db.getString(i, "MOUSECLICKIMAGE"));
				channel.setMouseUpImage(db.getString(i, "MOUSEUPIMAGE"));
				String[] pubFileNames = StringOperate.getFileNameAndExtName(db.getString(i, "PUB_FILE_NAME"));
				channel.setPubFileName(db.getString(0, "pub_file_name"));
				channel.setPubFileNameSuffix(pubFileNames[1]);
				channel.setOutlinepicture(db.getString(i, "OUTLINEPICTURE"));
				channel.setPageflag(db.getInt(i, "PAGEFLAG"));
				channel.setIndexpagepath(db.getString(i, "INDEXPAGEPATH"));
				channel.setCommentTemplateId(db.getInt(i, "COMMENT_TEMPLATE_ID"));
				channel.setCommentPagePath(db.getString(i, "COMMENTPAGEPATH"));
				channel.setChannel_desc(db.getString(i,"channel_desc"));
				channels.add(channel);
			}
			ListInfo listinfo = new ListInfo();
			listinfo.setDatas(channels);
			listinfo.setTotalSize(db.getTotalSize());
			return listinfo;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new ChannelManagerException("发生sql异常,无法返回频道信息.异常信息为:" + e.getMessage());
		}
	}

	/**
	 * 根据文档id获取文档所属的频道
	 * 
	 * @param docid
	 * @return
	 */
	public Channel getChannelOfDocument(String docid) {
		if (docid == null || docid.trim().length() == 0) {
			return null;
		}
		try {
			DBUtil db = new DBUtil();
			String sql = "select z1.CHANNEL_ID,z1.NAME,z1.DISPLAY_NAME,z1.PARENT_ID,"
					+ "z1.CHNL_PATH,z1.CREATEUSER,z1.CREATETIME,z1.ORDER_NO,z1.SITE_ID,"
					+ "z1.STATUS,z1.OUTLINE_TPL_ID,z1.DETAIL_TPL_ID, CHANNEL_FLOW_ID(z1.channel_id) WORKFLOW,"
					+ "z1.CHNL_OUTLINE_DYNAMIC,z1.DOC_DYNAMIC,z1.CHNL_OUTLINE_PROTECT,"
					+ "z1.DOC_PROTECT,z1.PARENT_WORKFLOW,z1.pub_file_name,z1.ISNAVIGATOR,z1.NAVIGATORLEVEL,z1.MOUSEINIMAGE, "
					+ "z1.MOUSEOUTIMAGE, z1.MOUSECLICKIMAGE, z1.MOUSEUPIMAGE,z1.OUTLINEPICTURE,z1.PAGEFLAG,z1.INDEXPAGEPATH, "
					+ "z1.commentpagepath,z1.channel_desc "
					+ "from TD_CMS_CHANNEL z1 inner join td_cms_document z2 on z1.channel_id = z2.channel_id where z2.document_id = "
					+ docid + " and z1.status=0";
			db.executeSelect(sql);
			if (db.size() > 0) {
				Channel channel = new Channel();
				channel.setChannelId(db.getLong(0, "CHANNEL_ID"));
				channel.setName(db.getString(0, "NAME"));
				channel.setDisplayName(db.getString(0, "DISPLAY_NAME"));
				channel.setParentChannelId(db.getInt(0, "PARENT_ID"));
				channel.setChannelPath(db.getString(0, "CHNL_PATH"));
				channel.setCreateUser(db.getLong(0, "CREATEUSER"));
				channel.setCreateTime(db.getDate(0, "CREATETIME"));
				channel.setOrderNo(db.getInt(0, "ORDER_NO"));
				channel.setSiteId(db.getLong(0, "SITE_ID"));
				channel.setStaus(db.getInt(0, "STATUS"));
				channel.setOutlineTemplateId(db.getInt(0, "OUTLINE_TPL_ID"));
				channel.setDetailTemplateId(db.getInt(0, "DETAIL_TPL_ID"));
				channel.setWorkflow(db.getInt(0, "WORKFLOW"));
				channel.setOutlineIsDynamic(db.getInt(0, "CHNL_OUTLINE_DYNAMIC"));
				channel.setDocIsDynamic(db.getInt(0, "DOC_DYNAMIC"));
				channel.setOutlineIsProtect(db.getInt(0, "CHNL_OUTLINE_PROTECT"));
				channel.setDocIsProtect(db.getInt(0, "DOC_PROTECT"));
				channel.setWorkflowIsFromParent(db.getInt(0, "PARENT_WORKFLOW"));
				String[] pubFileNames = StringOperate.getFileNameAndExtName(db.getString(0, "pub_file_name"));
				channel.setPubFileName(db.getString(0, "pub_file_name"));
				channel.setPubFileNameSuffix(pubFileNames[1]);
				channel.setNavigator(0 != db.getInt(0, "ISNAVIGATOR"));
				channel.setNavigatorLevel(db.getInt(0, "NAVIGATORLEVEL"));
				channel.setMouseInImage(db.getString(0, "MOUSEINIMAGE"));
				channel.setMouseOutImage(db.getString(0, "MOUSEOUTIMAGE"));
				channel.setMouseClickImage(db.getString(0, "MOUSECLICKIMAGE"));
				channel.setMouseUpImage(db.getString(0, "MOUSEUPIMAGE"));
				channel.setOutlinepicture(db.getString(0, "outlinepicture"));
				channel.setPageflag(db.getInt(0, "PAGEFLAG"));
				channel.setIndexpagepath(db.getString(0, "INDEXPAGEPATH"));
				channel.setCommentPagePath(db.getString(0, "commentpagepath"));
				channel.setChannel_desc(db.getString(0,"channel_desc"));
				return channel;
			}
			return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
			// throw new ChannelManagerException("发生sql异常,无法返回频道信息.异常信息为:"
			// + e.getMessage());
		}
	}

	/**
	 * 通过频道显示名称获取子频道中最近发布的相应数目的分页文档列表 (对于那些包含于聚合文档的文档不在其列)
	 * 最近发布的相应数目的文档列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * 
	 * @author xinwang.jiao
	 * @param String
	 *            DisplayName
	 * @return List<Document>
	 * @throws ChannelManagerException
	 */
	public List getLatestDocListOfSubChnls(String siteid, String fatherDisplayName, int count, String doctype)
			throws ChannelManagerException {
		List list = new ArrayList();
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		DBUtil db = new DBUtil();
		String sqlstr = "";
		StringBuffer sql = new StringBuffer();
		try {
			sqlstr = "select t.channel_id from td_cms_channel t " + " where t.display_name = '" + fatherDisplayName
					+ "' and t.site_id = " + siteid;
			db.executeSelect(sqlstr);
			int chlId;
			if (db.size() > 0) {
				chlId = db.getInt(0, "channel_id");
				// add 2007-09-15: case when e.order_no is null then -1 else 1 end
				sql.append("select p.* from(select case when a.order_no is null then -1 else 1 end  as ordernum,")
						.append("t.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR,CHANNEL_ID reference_channel_id, CHANNEL_ID, KEYWORDS,")
						.append(" DOCABSTRACT, DOCTYPE, ")
						.append("DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, DOCSOURCE_ID, DETAILTEMPLATE_ID, ")
						.append("LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,")
						.append("case when DOCTYPE=1 ")
						.append("then t.content else null end linkfile,pic_path,mediapath,")
						.append("publishfilename,commentswitch,secondtitle,").append("isnew,newpic_path,")
						.append("a.order_no,1 as ordersq,t.seq,t.ordertime ").append(" from td_cms_document t ")
						.append(" left outer join (select * from td_cms_doc_arrange a ")
						.append(" where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')< ")
						.append(DBUtil.getDBAdapter().to_date(new Date()))
						.append(" and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')> ")
						.append(DBUtil.getDBAdapter().to_date(new Date()))
						.append(" ) a on t.document_id = a.document_id ").append(" where STATUS in ( ")
						.append(DocumentStatus.PUBLISHED.getStatus()).append(",")
						.append(DocumentStatus.PUBLISHING.getStatus())
						.append(") and ISDELETED = 0 and t.document_id not ")
						.append(" in(select c.id_by_aggr from td_cms_doc_aggregation c ")
						.append(" inner join td_cms_document z1 on c.id_by_aggr = z1.document_id ")
						.append(" inner join td_cms_channel z2 on z1.channel_id = z2.channel_id ")
						.append("where z1.channel_id = ").append(chlId).append(") ")
						.append(" and CHANNEL_ID in (select CHANNEL_ID from TD_CMS_CHANNEL ")
						.append("where PARENT_ID!=0 and PARENT_ID is not null and status=0 and PARENT_ID=")
						.append(chlId).append(")");
				if (!StringUtil.isEmpty(doctype)) {
//					sql.append(" and t.doc_kind = ").append(doctype);// 关联文档类型
					sql.append(" and t.doc_class = '").append(doctype).append("'");// 关联文档类型
				}
				sql.append(" union all ")
						// 联合查询 add 2007-09-15: case when e.order_no is null then -1 else 1 end
						.append(" select case when e.order_no is null then -1 else 1 end as ordernum,")
						.append("c.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR,d.chnl_id reference_channel_id, CHANNEL_ID, ")
						.append("KEYWORDS, DOCABSTRACT, DOCTYPE, ")
						.append("DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, DOCSOURCE_ID, DETAILTEMPLATE_ID, ")
						.append("LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,")
						.append("case when DOCTYPE=1 ")
						.append("then content else null end linkfile,pic_path,mediapath,")
						.append("publishfilename,commentswitch,secondtitle,").append("isnew,newpic_path,")
						.append("e.order_no,2 as ordersq,c.seq,c.ordertime ")
						.append(" from td_cms_document c, td_cms_chnl_ref_doc d ")
						.append(" left outer join (select * from td_cms_doc_arrange a ")
						.append(" where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')< ")
						.append(DBUtil.getDBAdapter().to_date(new Date()))
						.append(" and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')> ")
						.append(DBUtil.getDBAdapter().to_date(new Date())).append(" ) e on d.doc_id = e.document_id ")
						.append(" where c.document_id=d.doc_id and STATUS in ( ")
						.append(DocumentStatus.PUBLISHED.getStatus()).append(",")
						.append(DocumentStatus.PUBLISHING.getStatus()).append(") and ISDELETED = 0 and d.doc_id not ")
						.append(" in(select c.id_by_aggr from td_cms_doc_aggregation c ")
						.append(" inner join td_cms_document z1 on c.id_by_aggr = z1.document_id ")
						.append(" inner join td_cms_channel z2 on z1.channel_id = z2.channel_id")
						.append(" where z1.channel_id = ").append(chlId).append(") ")
						.append(" and d.chnl_id in (select CHANNEL_ID from TD_CMS_CHANNEL")
						.append(" where PARENT_ID!=0 and PARENT_ID is not null and status=0 and PARENT_ID=")
						.append(chlId).append(")");
				if (!StringUtil.isEmpty(doctype)) {
					sql.append(" and c.doc_kind = ").append(doctype);// 关联文档类型
				}
				sql

				.append(" order by ordernum desc,seq,ordertime desc,DOCWTIME desc) p ").append(
						((count < 1) ? "" : "where rownum <=" + count));// 如果count为-1默认查询所有的记录
				System.out.println("---------ssss---" + sql);
				db.executeSelect(sql.toString());
				Document doc;
				for (int i = 0; i < db.size(); i++) {
					doc = new Document();
					// 处理引用频道
					// add by ge.tao
					// 2007-09-14
					if (db.getInt(i, "DOCTYPE") == Document.DOCUMENT_CHANNEL) {
						String channelid = String.valueOf(db.getLong(i, "DOCUMENT_ID"));
						Channel refChannel = new ChannelManagerImpl().getChannelInfo(channelid);
						// doc.setRefChannel(refChannel);
						// doc.setDoctype(Document.DOCUMENT_CHANNEL);
						list.add(refChannel);
					} else {
						doc.setDocument_id(db.getInt(i, "DOCUMENT_ID"));
						doc.setTitle(db.getString(i, "TITLE"));
						doc.setSubtitle(db.getString(i, "SUBTITLE"));
						doc.setAuthor(db.getString(i, "AUTHOR"));
						doc.setChanel_id(db.getInt(i, "CHANNEL_ID"));
						doc.setReference_channel_id(db.getInt(i, "reference_channel_id"));
						doc.setKeywords(db.getString(i, "KEYWORDS"));
						doc.setDocabstract(db.getString(i, "DOCABSTRACT"));
						doc.setDoctype(db.getInt(i, "DOCTYPE"));
						doc.setDocwtime(db.getDate(i, "DOCWTIME"));
						doc.setTitlecolor(db.getString(i, "TITLECOLOR"));
						doc.setCreateTime(db.getDate(i, "CREATETIME"));
						doc.setCreateUser(db.getInt(i, "CREATEUSER"));
						doc.setDocsource_id(db.getInt(i, "DOCSOURCE_ID"));
						doc.setDetailtemplate_id(db.getInt(i, "DETAILTEMPLATE_ID"));
						doc.setLinktarget(db.getString(i, "LINKTARGET"));
						doc.setLinkfile(db.getString(i, "linkfile"));
						doc.setFlowId(db.getInt(i, "FLOW_ID"));
						doc.setDoc_level(db.getInt(i, "DOC_LEVEL"));
						doc.setDoc_kind(db.getInt(i, "DOC_KIND"));
						doc.setParentDetailTpl(db.getString(i, "PARENT_DETAIL_TPL"));
						doc.setPublishTime(db.getDate(i, "publishtime"));
						doc.setPicPath(db.getString(i, "pic_path"));
						doc.setMediapath(db.getString(i, "mediapath"));
						doc.setPublishfilename(db.getString(i, "publishfilename"));
						// doc.setc(db.getString(i,"commentswitch"));
						doc.setSecondtitle(db.getString(i, "secondtitle"));
						doc.setIsNew(db.getInt(i, "isnew"));
						doc.setNewPicPath(db.getString(i, "newpic_path"));

						int isref = db.getInt(i, "ordersq");
						doc.setRef(isref == 1 ? false : true);// 判断是否是引用的文档：true为是，flase为不是
						/* 装载扩展字段数据 */

						doc.setOrdertime(db.getDate(i, "ordertime"));
						doc.setExtColumn(extManager.getExtColumnInfo(i,db));
						list.add(doc);
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}

		return list;
	}

	/**
	 * 翻页!!! 通过频道显示名称获取子频道中最近发布的相应数目的分页文档列表 (对于那些包含于聚合文档的文档不在其列)
	 * 最近发布的相应数目的文档列表规则：首先考虑是否是置顶，其次考虑发布时间
	 * 
	 * @param siteid
	 * @param fatherDisplayName
	 * @param offset
	 * @param maxpageitems
	 * @return
	 * @throws ChannelManagerException
	 *             List ChannelManagerImpl.java 陶格
	 */
	public ListInfo getLatestDocListOfSubChnls(String siteid, String fatherDisplayName, long offset, int maxpageitems,
			String docType) throws ChannelManagerException {
		List list = new ArrayList();
		ListInfo listinfo = new ListInfo();
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		DBUtil db = new DBUtil();
		String sqlstr = "";
		StringBuffer sql = new StringBuffer();
		try {
			sqlstr = "select t.channel_id from td_cms_channel t " + " where t.display_name = '" + fatherDisplayName
					+ "' and t.site_id = " + siteid;
			db.executeSelect(sqlstr);
			int chlId;
			if (db.size() > 0) {
				chlId = db.getInt(0, "channel_id");
				// add 2007-09-15: case when e.order_no is null then -1 else 1 end
				sql.append("select p.* from(select case when a.order_no is null then -1 else 1 end as ordernum, ")
						.append("t.DOCUMENT_ID as document_id, TITLE, SUBTITLE, ")
						.append("AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, ")
						.append("DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, DOCSOURCE_ID, DETAILTEMPLATE_ID, ")
						.append("LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,")
						.append("case when DOCTYPE=1 ")
						.append("then t.content else null end linkfile,pic_path,mediapath,")
						.append("publishfilename,commentswitch,secondtitle,")
						.append("isnew,newpic_path,")
						.append("a.order_no,1 as ordersq ")
						.append(" from td_cms_document t ")
						.append(" left outer join (select * from td_cms_doc_arrange a ")
						.append(" where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')< ")
						.append(DBUtil.getDBAdapter().to_date(new Date()))
						.append(" and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')> ")
						.append(DBUtil.getDBAdapter().to_date(new Date()))
						.append(" ) a on t.document_id = a.document_id ")
						.append(" where STATUS in ( ")
						.append(DocumentStatus.PUBLISHED.getStatus())
						.append(",")
						.append(DocumentStatus.PUBLISHING.getStatus())
						.append(") and ISDELETED = 0 and t.document_id not ")
						.append(" in(select c.id_by_aggr from td_cms_doc_aggregation c ")
						.append(" inner join td_cms_document z1 on c.id_by_aggr = z1.document_id ")
						.append(" inner join td_cms_channel z2 on z1.channel_id = z2.channel_id where z1.channel_id = ")
						.append(chlId).append(") ").append(" and CHANNEL_ID in (select CHANNEL_ID from TD_CMS_CHANNEL")
						.append(" where PARENT_ID!=0 and PARENT_ID is not null and status=0 and PARENT_ID=")
						.append(chlId).append(")");
				if (!StringUtil.isEmpty(docType)) {
//					sql.append(" and t.doc_kind = ").append(docType);// 关联文档类型
					sql.append(" and t.doc_class = '").append(docType).append("'");// 关联文档类型
				}
				sql.append(" union all ")
						// 联合查询 + 引用文档DOCTYPE=1 add 2007-09-15: case when e.order_no is null then -1 else 1 end
						.append(" select case when e.order_no is null then -1 else 1 end as ordernum, ")
						.append("c.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, ")
						.append("KEYWORDS, DOCABSTRACT, DOCTYPE, ")
						.append("DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, DOCSOURCE_ID, DETAILTEMPLATE_ID, ")
						.append("LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,")
						.append("case when DOCTYPE=1 ")
						.append("then content else null end linkfile,pic_path,mediapath,publishfilename,commentswitch,secondtitle,")
						.append("isnew,newpic_path,").append("e.order_no,2 as ordersq ")
						.append(" from td_cms_document c, td_cms_chnl_ref_doc d ")
						.append(" left outer join (select * from td_cms_doc_arrange a ")
						.append(" where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')< ")
						.append(DBUtil.getDBAdapter().to_date(new Date()))
						.append(" and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')> ")
						.append(DBUtil.getDBAdapter().to_date(new Date())).append(" ) e on d.doc_id = e.document_id ")
						.append(" where c.document_id=d.doc_id and STATUS in ( ")
						.append(DocumentStatus.PUBLISHED.getStatus()).append(",")
						.append(DocumentStatus.PUBLISHING.getStatus()).append(") and ISDELETED = 0 and d.doc_id not ")
						.append(" in(select c.id_by_aggr from td_cms_doc_aggregation c ")
						.append(" inner join td_cms_document z1 on c.id_by_aggr = z1.document_id ")
						.append(" inner join td_cms_channel z2 on z1.channel_id = z2.channel_id ")
						.append("where z1.channel_id = ").append(chlId).append(") ")
						.append(" and d.chnl_id in (select CHANNEL_ID from TD_CMS_CHANNEL ")
						.append("where PARENT_ID!=0 and PARENT_ID is not null and status=0 and PARENT_ID=")
						.append(chlId);
				if (!StringUtil.isEmpty(docType)) {
					sql.append(" and c.doc_kind = ").append(docType);// 关联文档类型
				}
				sql.append(")").append(" order by ordernum desc,DOCWTIME desc) p ");
				// ((count < 1)?"":"where rownum <=" +
				// count);//如果count为-1默认查询所有的记录
				// System.out.println(sql);
				db.executeSelect(sql.toString(), offset, maxpageitems);
				Document doc;
				for (int i = 0; i < db.size(); i++) {
					doc = new Document();
					// 处理引用频道
					// add by ge.tao
					// 2007-09-14
					if (db.getInt(i, "DOCTYPE") == Document.DOCUMENT_CHANNEL) {
						String channelid = String.valueOf(db.getLong(i, "DOCUMENT_ID"));
						Channel refChannel = new ChannelManagerImpl().getChannelInfo(channelid);
						// doc.setRefChannel(refChannel);
						// doc.setDoctype(Document.DOCUMENT_CHANNEL);
						list.add(refChannel);
					} else {
						doc.setDocument_id(db.getInt(i, "DOCUMENT_ID"));
						doc.setTitle(db.getString(i, "TITLE"));
						doc.setSubtitle(db.getString(i, "SUBTITLE"));
						doc.setAuthor(db.getString(i, "AUTHOR"));
						doc.setChanel_id(db.getInt(i, "CHANNEL_ID"));
						doc.setKeywords(db.getString(i, "KEYWORDS"));
						doc.setDocabstract(db.getString(i, "DOCABSTRACT"));
						doc.setDoctype(db.getInt(i, "DOCTYPE"));
						doc.setDocwtime(db.getDate(i, "DOCWTIME"));
						doc.setTitlecolor(db.getString(i, "TITLECOLOR"));
						doc.setCreateTime(db.getDate(i, "CREATETIME"));
						doc.setCreateUser(db.getInt(i, "CREATEUSER"));
						doc.setDocsource_id(db.getInt(i, "DOCSOURCE_ID"));
						doc.setDetailtemplate_id(db.getInt(i, "DETAILTEMPLATE_ID"));
						doc.setLinktarget(db.getString(i, "LINKTARGET"));
						doc.setLinkfile(db.getString(i, "linkfile"));
						doc.setFlowId(db.getInt(i, "FLOW_ID"));
						doc.setDoc_level(db.getInt(i, "DOC_LEVEL"));
						doc.setDoc_kind(db.getInt(i, "DOC_KIND"));
						doc.setParentDetailTpl(db.getString(i, "PARENT_DETAIL_TPL"));
						doc.setPublishTime(db.getDate(i, "publishtime"));
						doc.setPicPath(db.getString(i, "pic_path"));

						doc.setMediapath(db.getString(i, "mediapath"));
						doc.setPublishfilename(db.getString(i, "publishfilename"));
						// doc.setc(db.getString(i,"commentswitch"));
						doc.setSecondtitle(db.getString(i, "secondtitle"));
						doc.setIsNew(db.getInt(i, "isnew"));
						doc.setNewPicPath(db.getString(i, "newpic_path"));

						int isref = db.getInt(i, "ordersq");
						doc.setRef(isref == 1 ? false : true);// 判断是否是引用的文档：true为是，flase为不是
						/* 装载扩展字段数据 */
						doc.setOrdertime(db.getDate(i, "ordertime"));
						doc.setExtColumn(extManager.getExtColumnInfo(i,db));
						list.add(doc);
					}

				}
				listinfo.setDatas(list);
				listinfo.setTotalSize(db.getTotalSize());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}
		return listinfo;
	}

	public boolean doesHaveHomePageOfChnl(Channel channel) throws ChannelManagerException {
		if (channel == null)
			return false;

		try {
			DBUtil db = new DBUtil();
			String sql = "select * from TD_CMS_CHANNEL where channel_id="
					+ String.valueOf(channel.getChannelId())
					+ " and (PAGEFLAG=0 and OUTLINE_TPL_ID is not null or (PAGEFLAG=1 or PAGEFLAG=2) and INDEXPAGEPATH is not null)";
			// System.out.println("sql=" + sql);
			db.executeSelect(sql);

			if (db.size() > 0)
				return true;

			return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}
	}

	/**
	 * 移动频道
	 * 
	 * @param String
	 *            fromId（源频道）
	 * @param String
	 *            toId（目的频道）toId 可以为null
	 * @return boolean
	 * @throws ChannelManagerException
	 */
	public boolean moveChannel(String fromId, String toId) throws ChannelManagerException {
		/** 从根目录下移动到根目录下，不移动 */
		if (getChannelInfo(fromId).getParentChannelId() == 0 && toId == null)
			return false;
		/** 非根目录且父目录相同，不移动 */
		if (toId != null && getChannelInfo(fromId).getParentChannelId() == getChannelInfo(toId).getChannelId())
			return false;
		boolean flag = false;

		// 数据库更新
		DBUtil db = new DBUtil();
		String sql = "update td_cms_channel a set a.parent_id = " + toId + " where a.channel_id = " + fromId;
		try {
			db.executeUpdate(sql);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}
		try {
			Event event = new EventImpl(new SiteManagerImpl().getSiteInfo(getChannelInfo(fromId).getSiteId() + ""),
					CMSEventType.EVENT_CHANNEL_MOVE);
			super.change(event, true);
		} catch (SiteManagerException e) {
			e.printStackTrace();
		} catch (ChannelManagerException e) {
			e.printStackTrace();
		}

		return flag;
	}

	/**
	 * 通过频道显示名称,获取兄弟频道列表 翻页
	 * 
	 * @param siteid
	 * @param displayName
	 * @param offset
	 * @param pageitems
	 * @return
	 * @throws ChannelManagerException
	 *             ChannelManager.java
	 * @author: 陶格
	 */
	public ListInfo getBrotherChannels(String siteid, String displayName, long offset, int pageitems)
			throws ChannelManagerException {
		displayName = getParentDisplayNameChannel(siteid, displayName);
		return this.getDirectSubChannels(siteid, displayName, offset, pageitems);
	}

	/**
	 * 通过频道显示名称,获取兄弟频道列表 不翻页
	 * 
	 * @param siteid
	 * @param displayName
	 * @param count
	 * @return
	 * @throws ChannelManagerException
	 *             ChannelManager.java
	 * @author: 陶格
	 */
	public List getBrotherChannels(String siteid, String displayName, int count) throws ChannelManagerException {
		displayName = getParentDisplayNameChannel(siteid, displayName);
		return getDirectSubChannels(siteid, displayName, count);
	}

	/**
	 * 通过频道显示名称,获取父频道的兄弟频道列表 翻页 即 获得 父父频道的所有子频道
	 * 
	 * @param siteid
	 * @param displayName
	 * @param offset
	 * @param pageitems
	 * @return ListInfo
	 * @throws ChannelManagerException
	 *             ChannelManager.java
	 * @author: 陶格
	 */
	public ListInfo getParentBrotherChannels(String siteid, String displayName, long offset, int pageitems)
			throws ChannelManagerException {
		Channel channel = this.getChannelInfoByDisplayName(siteid, displayName);
		Channel parentParentchannel = this.getParentChannel(channel, 2);
		return getDirectSubChannels(siteid, parentParentchannel.getDisplayName(), offset, pageitems);
	}

	/**
	 * 通过频道显示名称,获取父频道的兄弟频道列表 不翻页 即 获得 父父频道的所有子频道
	 * 
	 * @param siteid
	 * @param displayName
	 * @param count
	 * @return List
	 * @throws ChannelManagerException
	 *             ChannelManager.java
	 * @author: 陶格
	 */
	public List getParentBrotherChannels(String siteid, String displayName, int count) throws ChannelManagerException {

		Channel channel = this.getChannelInfoByDisplayName(siteid, displayName);
		Channel parentParentchannel = this.getParentChannel(channel, 2);
		return getDirectSubChannels(siteid, parentParentchannel.getDisplayName(), count);
	}

	/**
	 * 根据站点ID和显示名称 获得 父频道显示名称
	 * 
	 * @param siteid
	 * @param displayName
	 * @return ChannelManagerImpl.java
	 * @author: 陶格
	 */
	public String getParentDisplayNameChannel(String siteid, String displayName) {
		StringBuffer sqlstr = new StringBuffer()
				.append("select c.display_name from td_cms_channel c where c.channel_id = (")
				.append("select c.parent_id from td_cms_channel c where c.site_id=").append(siteid)
				.append(" and c.display_name='").append(displayName).append("')");
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sqlstr.toString());
			if (db.size() > 0) {
				return db.getString(0, "display_name");
			} else {
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 通过channel获取父频道 可以指定父频道的层级
	 * 
	 * @param channel
	 * @param level
	 * @return Channel
	 * @author: 陶格
	 */
	public Channel getParentChannel(Channel channel, int level) {
		if (level <= 0)
			level = 1;
		if (channel == null)
			return null;
		Channel parent = null;
		int maxLevel = this.getParentMaxLevel(String.valueOf(channel.getChannelId()));
		level = level + 1;
		level = level > maxLevel ? maxLevel : level;
		StringBuffer sqlstr = new StringBuffer().append("select * from (")
				.append("select level as ceng,t.channel_id from td_cms_channel t ").append("start with t.channel_id= ")
				.append(channel.getChannelId()).append(" connect by prior t.parent_id=  t.channel_id ")
				.append(")abc where abc.ceng=").append(level);
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sqlstr.toString());
			if (db.size() > 0) {
				ChannelManagerImpl impl = new ChannelManagerImpl();
				int parent_id = 0;
				parent_id = db.getInt(0, "channel_id");
				if (parent_id != 0) {
					try {
						parent = impl.getChannelInfo(String.valueOf(parent_id));
					} catch (ChannelManagerException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parent;
	}

	/**
	 * 判断最多有几层父频道
	 * 
	 * @param channel_id
	 * @return ChannelManagerImpl.java
	 * @author: 陶格
	 */
	public int getParentMaxLevel(String channel_id) {
		int level = 1;
		StringBuffer sqlstr = new StringBuffer().append("select max(level) as ceng from td_cms_channel t ")
				.append("start with t.channel_id= ").append(channel_id)
				.append(" connect by prior t.parent_id=  t.channel_id ");
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sqlstr.toString());
			if (db.size() > 0) {
				level = db.getInt(0, "ceng");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return level;
	}

	/**
	 * 获取频道id为channelId的频道中所有与频道设定的细览模板一致的已发文档列表 add by xinwang.jiao 2007.06.25
	 * 以便
	 * @param String
	 *            channelId
	 * @return List<Document>
	 * @throws ChannelManagerException
	 */
	public List getPubDocWithSameTplOfChannel(String channelId, String excludedocids[]) throws ChannelManagerException {
		List list = new ArrayList();
		String temp = "";
		for (int i = 0; excludedocids != null && i < excludedocids.length; i++) {
			if (i == 0) {
				temp = excludedocids[i];

			} else
				temp = "," + excludedocids[i];
		}
		StringBuffer sb = new StringBuffer();

		sb.append("select document_id, TITLE, SUBTITLE, AUTHOR, a.CHANNEL_ID as CHANNEL_ID, b.display_name as channelname, ");
		sb.append("KEYWORDS, DOCABSTRACT, DOCTYPE, DOCWTIME, TITLECOLOR, a.CREATETIME, a.CREATEUSER, DOCSOURCE_ID, ");
		sb.append("DETAILTEMPLATE_ID, LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime, ");
		sb.append("case when DOCTYPE=1 then a.content else null end linkfile, pic_path ");
		sb.append("from td_cms_document a inner join td_cms_channel b on a.channel_id = b.channel_id ");
		sb.append("where a.channel_id = '" + channelId + "' and a.status = '" + DocumentStatus.PUBLISHED.getStatus()
				+ "' ");
		sb.append("and (a.detailtemplate_id = b.detail_tpl_id or a.parent_detail_tpl = 1) ");
		if (temp.length() > 0) {
			sb.append(" and a.document_id not in (").append(temp).append(")");
		}

		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		try { // System.out.println(sb.toString());
			db.executeSelect(sb.toString());

			Document doc;

			for (int i = 0; i < db.size(); i++) {
				doc = new Document();
				// 处理引用频道
				// add by ge.tao
				// 2007-09-14
				if (db.getInt(i, "DOCTYPE") == Document.DOCUMENT_CHANNEL) {
					String channelid = String.valueOf(db.getLong(i, "document_id"));
					Channel refChannel = new ChannelManagerImpl().getChannelInfo(channelid);
					list.add(refChannel);
					// doc.setRefChannel(refChannel);
					// doc.setDoctype(Document.DOCUMENT_CHANNEL);
				} else {
					doc.setDocument_id(db.getInt(i, "DOCUMENT_ID"));
					doc.setTitle(db.getString(i, "TITLE"));
					doc.setSubtitle(db.getString(i, "SUBTITLE"));
					doc.setAuthor(db.getString(i, "AUTHOR"));
					doc.setChanel_id(db.getInt(i, "CHANNEL_ID"));
					doc.setChannelName(db.getString(i, "channelname"));
					doc.setKeywords(db.getString(i, "KEYWORDS"));
					doc.setDocabstract(db.getString(i, "DOCABSTRACT"));
					doc.setDoctype(db.getInt(i, "DOCTYPE"));
					doc.setDocwtime(db.getDate(i, "DOCWTIME"));
					doc.setTitlecolor(db.getString(i, "TITLECOLOR"));
					doc.setCreateTime(db.getDate(i, "CREATETIME"));
					doc.setCreateUser(db.getInt(i, "CREATEUSER"));
					doc.setDocsource_id(db.getInt(i, "DOCSOURCE_ID"));
					doc.setDetailtemplate_id(db.getInt(i, "DETAILTEMPLATE_ID"));
					doc.setLinktarget(db.getString(i, "LINKTARGET"));
					doc.setLinkfile(db.getString(i, "linkfile"));
					doc.setFlowId(db.getInt(i, "FLOW_ID"));
					doc.setDoc_level(db.getInt(i, "DOC_LEVEL"));
					doc.setDoc_kind(db.getInt(i, "DOC_KIND"));
					doc.setParentDetailTpl(db.getString(i, "PARENT_DETAIL_TPL"));
					doc.setPublishTime(db.getDate(i, "publishtime"));
					doc.setPicPath(db.getString(i, "pic_path"));

					String str = "select SRCNAME from TD_CMS_DOCSOURCE where DOCSOURCE_ID ="
							+ db.getInt(i, "DOCSOURCE_ID") + "";

					db1.executeSelect(str);
					if (db1.size() > 0) {
						doc.setDocsource_name(db1.getString(0, "SRCNAME"));
					}

					/* 装载扩展字段数据 */
					Map docExtField = (new DocumentManagerImpl()).getDocExtFieldMap(doc.getDocument_id() + "");
					doc.setDocExtField(docExtField);
					/* 装载系统扩展字段数据 */
					DocumentExtColumnManager extManager = new DocumentExtColumnManager();
					doc.setExtColumn(extManager.getExtColumnInfo(i,db));
					list.add(doc);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ChannelManagerException(e.getMessage());
		}
		return list;
	}

	/**
	 * 根据频道id获取频道所属的站点id
	 * @param channelid
	 * @return
	 * added by biaoping.yin on 2007.9.20
	 */
	public String getSiteIDOfchannel(String channelid) throws ChannelManagerException {
		String sql = "select site_id from td_cms_channel where channel_id=" + channelid;
		DBUtil dbUtil = new DBUtil();
		try {
			dbUtil.executeSelect(sql);
			if (dbUtil.size() > 0) {
				return dbUtil.getInt(0, 0) + "";
			} else {
				throw new ChannelManagerException("获取频道[" + channelid + "]的站点id失败：该频道对应的站点id不存在。");
			}
		} catch (SQLException e) {
			throw new ChannelManagerException("获取频道[" + channelid + "]的站点id失败：该频道对应的站点id不存在。", e);
		}

	}

	/**
	 * 判断频道是否存在
	 */
	public boolean channelIsExist(String channelID) {
		DBUtil dbutil = new DBUtil();
		try {

			dbutil.executeSelect("select channel_id from td_cms_channel where channel_id=" + channelID);
			return dbutil.size() > 0;

		} catch (Exception e) {
			return false;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.channelmanager.ChannelManager#queryAllSubChannels(long)
	 */
	@Override
	public ListInfo querySubChannels(int offset, int pagesize, ChannelCondition condition) throws SQLException {
		if (!StringUtil.isEmpty(condition.getKeywords())) {
			condition.setKeywords("%" + condition.getKeywords() + "%");
		}
		
		return executor.queryListInfoBean(Channel.class, "querySubChannels", offset, pagesize, condition);
	}

}