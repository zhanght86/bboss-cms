//Source file: D:\\workspace\\cms\\src\\com\\frameworkset\\platform\\cms\\sitemanager\\SiteManagerImpl.java
package com.frameworkset.platform.cms.documentmanager;

import java.io.File;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.RollbackException;

import org.apache.log4j.Logger;
import org.htmlparser.util.ParserException;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.SQLExecutor;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.common.poolman.handle.RowHandler;
import com.frameworkset.common.poolman.sql.ColumnMetaData;
import com.frameworkset.common.poolman.sql.TableMetaData;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.cms.channelmanager.Channel;
import com.frameworkset.platform.cms.channelmanager.ChannelManager;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerException;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
import com.frameworkset.platform.cms.container.Template;
import com.frameworkset.platform.cms.docCommentManager.DocCommentManager;
import com.frameworkset.platform.cms.docCommentManager.DocCommentManagerImpl;
import com.frameworkset.platform.cms.documentmanager.bean.ArrangeDoc;
import com.frameworkset.platform.cms.documentmanager.bean.CitedDocSrcChannel;
import com.frameworkset.platform.cms.documentmanager.bean.CitedDocument;
import com.frameworkset.platform.cms.documentmanager.bean.DocAggregation;
import com.frameworkset.platform.cms.documentmanager.bean.DocExtValue;
import com.frameworkset.platform.cms.documentmanager.bean.DocHistoryOperate;
import com.frameworkset.platform.cms.documentmanager.bean.DocLevel;
import com.frameworkset.platform.cms.documentmanager.bean.DocRelated;
import com.frameworkset.platform.cms.documentmanager.bean.DocTemplate;
import com.frameworkset.platform.cms.documentmanager.bean.DocumentCondition;
import com.frameworkset.platform.cms.documentmanager.bean.Extvaluescope;
import com.frameworkset.platform.cms.documentmanager.bean.NewsCondition;
import com.frameworkset.platform.cms.documentmanager.bean.TaskDocument;
import com.frameworkset.platform.cms.driver.config.DocumentStatus;
import com.frameworkset.platform.cms.driver.context.ContentContext;
import com.frameworkset.platform.cms.driver.htmlconverter.CMSTemplateLinkTable;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor;
import com.frameworkset.platform.cms.driver.htmlconverter.CmsLinkProcessor.CMSLink;
import com.frameworkset.platform.cms.driver.i18n.CmsEncoder;
import com.frameworkset.platform.cms.driver.publish.impl.APPPublish;
import com.frameworkset.platform.cms.searchmanager.CMSSearchManager;
import com.frameworkset.platform.cms.sitemanager.Site;
import com.frameworkset.platform.cms.sitemanager.SiteManager;
import com.frameworkset.platform.cms.sitemanager.SiteManagerException;
import com.frameworkset.platform.cms.sitemanager.SiteManagerImpl;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.util.FileUtil;
import com.frameworkset.platform.cms.util.FtpUpfile;
import com.frameworkset.platform.security.AccessControl;
import com.frameworkset.platform.sysmgrcore.entity.User;
import com.frameworkset.util.ListInfo;
import com.frameworkset.util.StringUtil;

/**
 * 文档管理
 * 
 * @author huaihai.ou,xinwang.jiao,huiqiong.zeng,biaoping.yin,ge.tao,da.wei
 *         日期:Dec 19, 2006 版本:1.0 版权所有:三一重工
 */
public class DocumentManagerImpl implements DocumentManager {

	private ConfigSQLExecutor executor;

	private DBUtil dbUitl = new DBUtil();

	protected static Logger log = Logger.getLogger(DocumentManagerImpl.class);

	public DocumentManagerImpl() {

	}

	public int creatorDoc(Document doc) throws DocumentManagerException {
		int b = 0;
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		StringBuffer sql = new StringBuffer();
		PreparedDBUtil db = new PreparedDBUtil();
		// System.out.println(doc.getTitle());

		try {

			long documentId = db.getNextPrimaryKey("TD_CMS_DOCUMENT");

			ChannelManagerImpl chnl = new ChannelManagerImpl();
			sql.append("insert into TD_CMS_DOCUMENT(")
					.append("TITLE,SUBTITLE,AUTHOR,CONTENT,CHANNEL_ID,STATUS,KEYWORDS,DOCABSTRACT,")
					.append("DOCTYPE,TITLECOLOR,CREATETIME,CREATEUSER,DOCSOURCE_ID,DETAILTEMPLATE_ID,")
					.append("LINKTARGET,FLOW_ID,DOCWTIME,doc_level,PARENT_DETAIL_TPL,PIC_PATH,mediapath,")
					.append("publishfilename,secondtitle,isnew,newpic_path,ordertime,seq,DOCUMENT_ID,doc_class"
							+ extManager.appendExtSQLField(doc.getExtColumn(), "td_cms_document", true) + ") ")
					.append("values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?"
							+ extManager.appendExt(doc.getExtColumn(), "td_cms_document") + ")");

			db.preparedInsert(sql.toString());

			db.setString(1, doc.getTitle());
			db.setString(2, doc.getSubtitle());
			db.setString(3, doc.getAuthor());
			db.setClob(4, doc.getContent(), "content");
			db.setInt(5, doc.getChanel_id());
			db.setInt(6, 1);
			db.setString(7, doc.getKeywords());
			db.setString(8, doc.getDocabstract() == null ? "无" : doc.getDocabstract());
			db.setInt(9, doc.getDoctype());
			// db.setTimestamp(10,new Timestamp(new Date().getTime()));
			db.setString(10, doc.getTitlecolor());
			db.setTimestamp(11, new Timestamp(new Date().getTime()));
			db.setInt(12, doc.getCreateUser());
			db.setInt(13, doc.getDocsource_id());
			db.setInt(14, doc.getDetailtemplate_id());
			db.setString(15, doc.getLinktarget());
			db.setInt(16, chnl.getChannelInfo(Integer.toString(doc.getChanel_id())).getWorkflow());
			db.setTimestamp(17, new Timestamp(doc.getDocwtime().getTime()));
			db.setInt(18, doc.getDoc_level());
			db.setString(19, doc.getParentDetailTpl());
			db.setString(20, doc.getPicPath());
			db.setString(21, doc.getMediapath());
			db.setString(22, doc.getPublishfilename());
			db.setString(23, doc.getSecondtitle());
			db.setInt(24, doc.getIsNew());
			db.setString(25, doc.getNewPicPath());
			db.setTimestamp(26, new Timestamp(doc.getOrdertime().getTime()));
			db.setInt(27, doc.getSeq());
			db.setLong(28, documentId);
			db.setString(29, doc.getDoc_class());

			extManager.appendExtPreparedValue(db, doc.getExtColumn(), "td_cms_document", 30);

			db.executePrepared();

			b = (int) documentId;

		} catch (Exception e1) {
			e1.printStackTrace();
			throw new DocumentManagerException(e1.getMessage());
		} finally {
			db.resetPrepare();
		}

		return b;
	}

	/**
	 * 重写createDoc
	 * 
	 * @param <Document>
	 *            doc
	 * @param List
	 *            <Attachment> attr
	 * @param List
	 *            <DocAggregation> docAggr
	 * @param List
	 *            <DocRelated> docRel
	 */
	public boolean creatorDoc(Document doc, List attr, List docAggr, List docRel) {
		boolean flag = false;
		try {
			int docId = creatorDoc(doc);
			if (docId > 0) {
				if (attr != null && attr.size() > 0) {
					for (int i = 0; i < attr.size(); i++) {
						Attachment attachment = (Attachment) attr.get(i);
						attachment.setDocumentId(docId);
						createAttachment(attachment);
					}
				}
				if (docAggr != null && docAggr.size() > 0) {
					for (int i = 0; i < docAggr.size(); i++) {
						DocAggregation docAggrengation = (DocAggregation) docAggr.get(i);
						docAggrengation.setAggrdocid(docId);
						addAggrDoc(docAggrengation);
					}
				}
				if (docRel != null && docRel.size() > 0) {
					for (int i = 0; i < docRel.size(); i++) {
						DocRelated docRelated = (DocRelated) docRel.get(i);
						docRelated.setDocId(docId);
						creatorDocRelated(docRelated);
					}
				}
			}
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public boolean updateDoc(Document doc) throws DocumentManagerException {
		boolean b = false;
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		PreparedDBUtil db = new PreparedDBUtil();
		StringBuffer sql = new StringBuffer();
		sql.append("update TD_CMS_DOCUMENT set TITLE=?,SUBTITLE=?,")
				.append("AUTHOR=?,CONTENT=?,KEYWORDS=?,")
				// dockind
				.append("DOCABSTRACT=?,")
				.append("TITLECOLOR=?,")
				.append("DOCSOURCE_ID=?,")
				.append("DETAILTEMPLATE_ID=?,")
				.append("LINKTARGET=?,Doc_level=?,PARENT_DETAIL_TPL=?,PIC_PATH=?,mediapath=?,publishfilename=?,")
				.append("docwtime=" + DBUtil.getDBDate(doc.getDocwtime()))
				.append(",secondtitle=?,isnew=?,newpic_path=?,ordertime=?,seq=?,doc_class=?"
						+ extManager.appendExtUpdateSQLField(doc.getExtColumn(), "td_cms_document")
						+ " where document_id=?");
		try {
			db.preparedUpdate(sql.toString());

			db.setString(1, doc.getTitle());
			db.setString(2, doc.getSubtitle());
			db.setString(3, doc.getAuthor());
			db.setClob(4, doc.getContent(), "content");
			db.setString(5, doc.getKeywords());
			db.setString(6, doc.getDocabstract() == null ? "无" : doc.getDocabstract());
			db.setString(7, doc.getTitlecolor());
			db.setInt(8, doc.getDocsource_id());
			db.setInt(9, doc.getDetailtemplate_id());
			db.setString(10, doc.getLinktarget());
			db.setInt(11, doc.getDoc_level());
			db.setString(12, doc.getParentDetailTpl());
			db.setString(13, doc.getPicPath());
			db.setString(14, doc.getMediapath());
			db.setString(15, doc.getPublishfilename());
			db.setString(16, doc.getSecondtitle());
			db.setInt(17, doc.getIsNew());
			db.setString(18, doc.getNewPicPath());
			db.setTimestamp(19, new Timestamp(doc.getOrdertime().getTime()));
			db.setInt(20, doc.getSeq());
			db.setString(21, doc.getDoc_class());
			int index = 0;
			try {
				index = extManager.appendExtPreparedValue(db, doc.getExtColumn(), "td_cms_document", 22);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println(index);
			db.setPrimaryKey(index, doc.getDocument_id());
			db.executePrepared();

			b = true;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		} finally {
			db.resetPrepare();
		}
		return b;
	}

	/**
	 * 
	 */
	public void deleteDoc(int docid) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		String sql = "delete from td_cms_document where document_id=" + docid;
		try {
			// 删除任务
			clearTask(docid);
			// 删除文档的历史操作记录
			clearHisOpeRecord(docid);
			// 删除所有该文档的引用关系
			clearCiteRef(docid);
			// 删除文档的相关评论
			DocCommentManager dcm = new DocCommentManagerImpl();
			dcm.delCommentsByDocId(docid);

			String sql0 = "delete from td_cms_chnl_ref_doc where doc_id = " + docid;
			String sql1 = "delete from td_cms_doc_aggregation a where a.aggr_doc_id =" + docid + " or a.id_by_aggr ="
					+ docid;
			String sql2 = "delete from td_cms_doc_arrange b where b.document_id =" + docid;
			String sql3 = "delete from td_cms_doc_attach c where c.document_id =" + docid;
			String sql4 = "delete from td_cms_doc_related d where d.doc_id =" + docid + " or d.related_doc_id ="
					+ docid;
			String sql5 = "delete from td_cms_doc_ver e where e.document_id =" + docid;
			String sql6 = "delete from td_cms_doc_ver_attach f where f.document_id =" + docid;
			String sql7 = "delete from td_cms_doc_dist_manner g where g.document_id =" + docid;
			String sql8 = "delete from tl_cms_doc_oper_log h where h.doc_id =" + docid;
			String sql9 = "delete from td_cms_doc_task i where i.document_id =" + docid;
			String sql10 = "delete from td_cms_extfieldvalue k where k.document_id = " + docid;
			String sql11 = "delete from td_cms_doc_publishing l where l.document_id = " + docid;

			db.addBatch(sql0);
			db.addBatch(sql1);
			db.addBatch(sql2);
			db.addBatch(sql3);
			db.addBatch(sql4);
			db.addBatch(sql5);
			db.addBatch(sql6);
			db.addBatch(sql7);
			db.addBatch(sql8);
			db.addBatch(sql9);
			db.addBatch(sql10);
			db.addBatch(sql11);
			db.addBatch(sql);
			db.executeBatch();// 批处理操作数据库
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
	}

	/**
	 * 
	 */
	public void deleteDoc(int[] docids) throws DocumentManagerException {
		try {
			for (int i = 0; i < docids.length; i++)
				deleteDoc(docids[i]);
		} catch (DocumentManagerException e) {
			throw e;
		}
	}

	/**
	 * 删除文档附件，包括发布和预览的相关文件（同时删除采集目录下的附件）
	 * 
	 * @param request
	 * @param docid
	 * @param siteid
	 * @throws DocumentManagerException
	 */
	public void deleteDocAttFiles(HttpServletRequest request, int docid, String siteid) throws DocumentManagerException {
		try {
			Document document = getPartDocInfoById(docid + "");
			// 只有普通文档才要删除物理文件
			if (document.getDoctype() == Document.DOCUMENT_NORMAL) {
				int channelId = document.getChanel_id();

				String docName = "";
				/* 频道发布的相对路径 */
				String rentpath = "";
				/* 站点指定的发布到本地的路径 绝对路径 */
				String localpath = "";

				Site site = CMSUtil.getSiteCacheManager().getSite(siteid);

				Channel channel = CMSUtil.getChannelCacheManager(siteid).getChannel(channelId + "");

				/* 发布后的文件名 */
				docName = CMSUtil.getContentFileName(siteid, document);

				/* 发布的相对路径 */
				rentpath = channel.getChannelPath();

				/* 删除文档附件 */
				/* 频道发布的相对路径 */
				String sitePublishedDir = CMSUtil.getSitePubDestinction(siteid);
				// channelPublishedDir =
				// channelPublishedDir.replaceAll("\\\\","/");
				// channelPublishedDir = channelPublishedDir.toLowerCase();
				// channelPublishedDir =
				// channelPublishedDir.replaceAll("sitepublish/site"+siteid,"");
				// channelPublishedDir = CMSUtil.getSitePubDestinction(siteid) +
				// "/" + channelPublishedDir;
				List list = getAllPublishedAttachmentOfDocument(request, String.valueOf(docid), siteid);

				/* (1)删除发布到本地的文件 */
				boolean[] local2remote = SiteManagerImpl.getSitePublishDestination(site.getPublishDestination());

				if (local2remote[0]) {
					localpath = CMSUtil.getSitePubDestinction(siteid);
					if (localpath != null && localpath.length() > 0) {
						if (!CMSUtil.isDynamic(siteid, document)) {
							String localFilePath = CMSUtil.getPath(localpath + "/" + rentpath, docName);
							File localFile = new File(localFilePath);
							if (localFile.exists()) {
								localFile.delete();
							}
						}
						File file = null;
						for (int i = 0; i < list.size(); i++) {
							file = new File(sitePublishedDir + "/" + list.get(i));
							// System.out.println(file.getAbsolutePath());
							if (file.exists()) {
								file.delete();
							}
						}
					}
				}

				/* (2)删除发布到ftp服务器的文件 */
				if (local2remote[1]) {
					String remoteFilePath = CMSUtil.getPath(rentpath, docName);
					if (!isNull(site.getFtpIp()) && !isNull(site.getFtpUser()) && !isNull(site.getFtpPassword())
							&& !isNull(site.getFtpFolder())) {
						FtpUpfile ftpUpfile = new FtpUpfile(site.getFtpIp(), site.getFtpUser(), site.getFtpPassword());
						ftpUpfile.login();
						ftpUpfile.deleteRemoteFile(site.getFtpFolder() + "/" + remoteFilePath);

						for (int i = 0; i < list.size(); i++) {
							String filename = CMSUtil.getPath(sitePublishedDir, (String) list.get(i));
							ftpUpfile.deleteRemoteFile(filename);
						}
						ftpUpfile.logout();
					}
				}

				/* (3)删除文档附件 */
				String sitepath = CMSUtil.getAppRootPath() + "/cms/siteResource/" + site.getSiteDir() + "/_webprj/";
				File attfile = null;
				for (int i = 0; i < list.size(); i++) {
					attfile = new File(sitepath + list.get(i));
					// System.out.println(attfile.getAbsolutePath());
					if (attfile.exists()) {
						attfile.delete();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
	}

	/**
	 * 删除已发文档的附件，包括发布和预览的相关文件（不删除采集目录下的附件），用于文档的撤发
	 * 
	 * @param request
	 * @param docid
	 * @param siteid
	 * @throws DocumentManagerException
	 */
	public void deletePubDocAttFiles(HttpServletRequest request, int docid, String siteid)
			throws DocumentManagerException {
		try {
			Document document = getPartDocInfoById(docid + "");
			// 只有普通文档才要删除物理文件
			if (document.getDoctype() == Document.DOCUMENT_NORMAL) {
				int channelId = document.getChanel_id();

				String docName = "";
				/* 频道发布的相对路径 */
				String rentpath = "";
				/* 站点指定的发布到本地的路径 绝对路径 */
				String localpath = "";

				Site site = CMSUtil.getSiteCacheManager().getSite(siteid);

				Channel channel = CMSUtil.getChannelCacheManager(siteid).getChannel(channelId + "");

				/* 发布后的文件名 */
				docName = CMSUtil.getContentFileName(siteid, document);

				/* 发布的相对路径 */
				rentpath = channel.getChannelPath();

				/* 删除文档附件 */
				/* 频道发布的相对路径 */
				String sitePublishedDir = CMSUtil.getSitePubDestinction(siteid);
				// channelPublishedDir =
				// channelPublishedDir.replaceAll("\\\\","/");
				// channelPublishedDir = channelPublishedDir.toLowerCase();
				// channelPublishedDir =
				// channelPublishedDir.replaceAll("sitepublish/site"+siteid,"");
				// channelPublishedDir = CMSUtil.getSitePubDestinction(siteid) +
				// "/" + channelPublishedDir;
				List list = getAllPublishedAttachmentOfDocument(request, String.valueOf(docid), siteid);

				/* (1)删除发布到本地的文件 */
				localpath = CMSUtil.getSitePubDestinction(siteid);
				if (localpath != null && localpath.length() > 0) {
					if (!CMSUtil.isDynamic(siteid, document)) {
						String localFilePath = CMSUtil.getPath(localpath + "/" + rentpath, docName);
						File localFile = new File(localFilePath);
						if (localFile.exists()) {
							localFile.delete();
						}
					}

					File file = null;
					for (int i = 0; i < list.size(); i++) {
						file = new File(sitePublishedDir + "/" + list.get(i));
						// System.out.println(file.getAbsolutePath());
						if (file.exists()) {
							file.delete();
						}
					}
				}
				/* (2)删除发布到ftp服务器的文件 */
				String remoteFilePath = CMSUtil.getPath(rentpath, docName);
				if (!isNull(site.getFtpIp()) && !isNull(site.getFtpUser()) && !isNull(site.getFtpPassword())) {
					FtpUpfile ftpUpfile = new FtpUpfile(site.getFtpIp(), site.getFtpUser(), site.getFtpPassword());
					ftpUpfile.deleteRemoteFile(remoteFilePath);

					for (int i = 0; i < list.size(); i++) {
						String filename = CMSUtil.getPath(sitePublishedDir, (String) list.get(i));
						ftpUpfile.deleteRemoteFile(filename);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
	}

	/**
	 * 删除文档,不仅删除数据库的相关记录，还删除物理文件，包括发布和预览的相关文件,还从索引库中把索引记录删除
	 * 
	 * @param docid
	 * @return
	 * @throws DocumentManagerException
	 *             add by xinwang.jiao 2007.07.05
	 */
	public void deleteDoc(HttpServletRequest request, int docid, String siteid) throws DocumentManagerException {
		try {

			/* 删除索引记录 */
			// da.wei
			if (CMSUtil.enableIndex()) {
				try {
					CMSSearchManager sm = new CMSSearchManager();
					if (CMSUtil.enableIndexType(CMSUtil.INDEX_ENGINE_TYPE_LUCENE)) {
						sm.deleteDocumetFromIndex(request, docid + "", siteid);
					}
					if (CMSUtil.enableIndexType(CMSUtil.INDEX_ENGINE_TYPE_HAILIANG)) {
						CMSUtil.deleteIndexForHL(docid + "", siteid);
					}
				} catch (Exception e) {
				}
			}

			/* 删除物理文件 */
			deleteDocAttFiles(request, docid, siteid);
			/* 删除数据库记录 */
			deleteDoc(docid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
	}

	public static boolean isNull(String str) {
		boolean flag = false;
		if (str == null || str.trim().length() <= 0) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 批量删除文档,不仅删除数据库的相关记录，还删除物理文件，包括发布和预览的相关文件
	 * 
	 * @param docid
	 * @throws DocumentManagerException
	 *             add by xinwang.jiao 2007.07.05
	 */
	public void deleteDoc(HttpServletRequest request, int[] docids, String siteid) throws DocumentManagerException {

		try {
			for (int i = 0; i < docids.length; i++)
				deleteDoc(request, docids[i], siteid);

		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
	}

	/**
	 * 筛选文档，排除那些被频道引用为首页文档的文档 这些文档不能进行删除，回收，归档操作。
	 * 
	 * @param docids
	 * @return Map key "errormsg" 存放 error.toString()); key "docids" 存放 docids;
	 * @throws DocumentManagerException
	 */
	public Map eliminateUnOpDoc(int[] docids) throws DocumentManagerException {
		Map map = new HashMap();
		try {
			StringBuffer error = new StringBuffer();// 前台页面提示信息

			StringBuffer tempids = new StringBuffer();

			int[] newdocids = null;
			List list = new ArrayList();

			for (int i = 0; i < docids.length; i++) {
				if (i != docids.length - 1)
					tempids.append(docids[i] + ",");
				else
					tempids.append(docids[i]);

				list.add(docids[i] + "");
			}

			DBUtil db = new DBUtil();

			StringBuffer sql = new StringBuffer();

			sql.append("select b.document_id,b.subtitle,a.display_name from td_cms_channel a ,td_cms_document b ")
					.append("where a.pageflag = 2 and a.indexpagepath = b.document_id ")
					.append("and b.document_id in (").append(tempids.toString()).append(")");

			db.executeSelect(sql.toString());
			if (db.size() > 0) {
				// error.append("以下文档不能删除：\\n");
				newdocids = new int[docids.length - db.size()];
				for (int i = 0; i < db.size(); i++) {
					list.remove(db.getInt(i,"document_id") + "");
					error.append("文档[" + db.getString(i, "subtitle") + "]被频道").append(
							"[" + db.getString(i, "display_name") + "]的首页引用\\n");
				}
				for (int i = 0; i < list.size(); i++) {
					newdocids[i] = Integer.parseInt((String) list.get(i));
				}
			} else {
				newdocids = docids;
			}
			map.put("errormsg", error.toString());
			map.put("docids", newdocids);
			return map;
		} catch (Exception e) {
			map.put("errormsg", "");
			map.put("docids", docids);
			return map;
		}
	}

	public List getDocList() throws DocumentManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getDocList(int channelid) throws DocumentManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	// 根据sql语句获取文档的ListInfo列表
	public ListInfo getDocList(String sql, int offset, int maxItem) throws DocumentManagerException {
		DBUtil dbUtil = new DBUtil();
		// DBUtil db = new DBUtil();
		try {
			dbUtil.executeSelect(sql, offset, maxItem);
			ListInfo listInfo = new ListInfo();
			List list = new ArrayList();
			Document document;

			for (int i = 0; i < dbUtil.size(); i++) {
				document = new Document();
				document.setDocument_id(dbUtil.getInt(i, "document_id"));
				document.setTitle(dbUtil.getString(i, "title"));
				document.setSubtitle(dbUtil.getString(i, "SUBTITLE"));
				document.setDocabstract(dbUtil.getString(i, "docabstract"));
				document.setStatus(dbUtil.getInt(i, "status"));
				// 是否置顶
				if (dbUtil.getInt(i, "order_no") != -1) {
					document.setArrangeDoc(true);
					// 是否过期
					SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					try {
						if (format.parse(dbUtil.getString(i, "end_time")).after(new Date())) {
							document.setArrangeOverTime(false);
						} else {
							document.setArrangeOverTime(true);
						}
					} catch (ParseException e) {
						document.setArrangeOverTime(false);
						e.printStackTrace();
					}
				} else {
					document.setArrangeDoc(false);
					document.setArrangeOverTime(true);
				}
				// 置顶是否过期
				// db.executeSelect("select name from TB_CMS_DOC_STATUS where
				// id="+ dbUtil.getInt(i,"status") +"");
				// if(db.size()>0){
				document.setStatusname(dbUtil.getString(i, "statusname"));
				// }else{
				// document.setStatusname("状态不明");
				// }
				document.setAuthor(dbUtil.getString(i, "AUTHOR"));
				document.setUser_id(dbUtil.getInt(i, "createuser"));

				document.setDocwtime(dbUtil.getDate(i, "docwtime"));
				document.setDoctype(dbUtil.getInt(i, "DOCTYPE"));
				// String str="select USER_REALNAME from td_sm_user where
				// user_id="+ dbUtil.getInt(i,"createuser") +"";
				// db.executeSelect(str);
				// if(db.size()>0){

				document.setUsername(dbUtil.getString(i, "username"));
				// }
				// str = "select name from tb_cms_flow where id =" +
				// dbUtil.getInt(i,"flow_id");
				// db.executeSelect(str);
				// if(db.size()>0)
				try {
					document.setFlowName(dbUtil.getString(i, "flowname"));
				} catch (Exception e) {

				}
				// else document.setFlowName("无流程");

				document.setChanel_id(dbUtil.getInt(i, "channel_id"));
				// str = "select name from td_cms_channel where channel_id =" +
				// dbUtil.getInt(i,"channel_id");
				// db.executeSelect(str);
				// if(db.size()>0)
				document.setChannelName(dbUtil.getString(i, "channelName"));

				document.setCount(dbUtil.getInt(i, "count"));

				document.setDoc_class(dbUtil.getString(i, "doc_class"));

				list.add(document);
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
			return listInfo;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
	}

	// 获取待审文档列表
	public ListInfo getAuditDocList(String sql, int offset, int maxItem) throws DocumentManagerException {
		ListInfo listInfo = new ListInfo();
		ArrayList list = new ArrayList();
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql, offset, maxItem);
			for (int i = 0; i < db.size(); i++) {
				TaskDocument doc = new TaskDocument();
				doc.setTaskid(db.getInt(i, "task_id"));
				doc.setDocid(db.getInt(i, "document_id"));
				doc.setDocName(db.getString(i, "subtitle"));

				doc.setSubmitTime(db.getDate(i, "submit_time"));
				doc.setSubmitUserName(db.getString(i, "user_name"));
				doc.setDocChannelName(db.getString(i, "channel_name"));
				doc.setDocChannelid(db.getInt(i, "channel_id"));
				doc.setDocSiteName(db.getString(i, "docSiteName"));
				doc.setDocSiteid(db.getInt(i, "site_id"));
				doc.setDocTpye(db.getInt(i, "doctype"));
				list.add(doc);
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(db.getTotalSize());
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			throw new DocumentManagerException("获取审核文档列表时数据库操作失败：" + sqle.getMessage());
		}
		return listInfo;
	}

	// 获取待审文档列表
	public ListInfo getNewDocList(String sql, int offset, int maxItem) throws DocumentManagerException {
		ListInfo listInfo = new ListInfo();
		ArrayList list = new ArrayList();
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql, offset, maxItem);
			for (int i = 0; i < db.size(); i++) {
				// a.document_id,
				// a.title,a.subtitle,a.doctype,a.flow_id,a.createtime,
				// c.channel_id, c.NAME channel_name, d.site_id, d.NAME
				// docsitename, b.user_name
				Document doc = new Document();
				doc.setDocument_id(db.getInt(i, "document_id"));
				doc.setTitle(db.getString(i, "title"));
				doc.setSubtitle(db.getString(i, "subtitle"));
				doc.setDoctype(db.getInt(i, "doctype"));
				doc.setFlowId(db.getInt(i, "flow_id"));
				doc.setChanel_id(db.getInt(i, "channel_id"));
				doc.setChannelName(db.getString(i, "channel_name"));
				doc.setSiteid(db.getInt(i, "site_id"));
				doc.setSiteName(db.getString(i, "docsitename"));
				doc.setCreateTime(db.getDate(i, "createtime"));
				doc.setUsername(db.getString(i, "user_name"));
				list.add(doc);
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(db.getTotalSize());
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			throw new DocumentManagerException("获取新文档列表时数据库操作失败：" + sqle.getMessage());
		}
		return listInfo;
	}

	// 获取返工文档列表
	public ListInfo getReboundtDocList(String sql, int offset, int maxItem) throws DocumentManagerException {
		ListInfo listInfo = new ListInfo();
		ArrayList list = new ArrayList();
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql, offset, maxItem);
			for (int i = 0; i < db.size(); i++) {
				TaskDocument doc = new TaskDocument();
				doc.setTaskid(db.getInt(i, "task_id"));
				doc.setDocid(db.getInt(i, "document_id"));
				doc.setDocName(db.getString(i, "subtitle"));

				doc.setSubmitTime(db.getDate(i, "submit_time"));
				doc.setSubmitUserName(db.getString(i, "user_name"));
				doc.setDocChannelName(db.getString(i, "channel_name"));
				doc.setDocChannelid(db.getInt(i, "channel_id"));
				doc.setDocTpye(db.getInt(i, "doctype"));
				doc.setDocSiteid(db.getInt(i, "site_id"));
				doc.setDocSiteName(db.getString(i, "docSiteName"));
				list.add(doc);
				;
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(db.getTotalSize());
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			throw new DocumentManagerException("获取返工文档列表时数据库操作失败：" + sqle.getMessage());
		}
		return listInfo;
	}

	// 回去发布文档列表
	public ListInfo getPubishDocList(String sql, int offset, int maxItem) throws DocumentManagerException {
		DBUtil dbUtil = new DBUtil();
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql, offset, maxItem);
			ListInfo listInfo = new ListInfo();
			List list = new ArrayList();

			for (int i = 0; i < db.size(); i++) {
				TaskDocument doc = new TaskDocument();
				doc.setTaskid(db.getInt(i, "task_id"));
				doc.setDocid(db.getInt(i, "document_id"));
				doc.setDocName(db.getString(i, "subtitle"));
				doc.setSubmitTime(db.getDate(i, "submit_time"));
				doc.setSubmitUserName(db.getString(i, "user_name"));
				doc.setDocChannelName(db.getString(i, "channel_name"));
				doc.setDocChannelid(db.getInt(i, "channel_id"));
				doc.setDocSiteName(db.getString(i, "docSiteName"));
				doc.setDocTpye(db.getInt(i, "doctype"));
				doc.setDocSiteid(db.getInt(i, "site_id"));
				String str = "select name from tb_cms_flow where id =" + db.getInt(i, "flow_id");
				dbUtil.executeSelect(str);
				if (dbUtil.size() > 0)
					doc.setDocFlowName(dbUtil.getString(0, "name"));
				else
					doc.setDocFlowName("无流程");
				list.add(doc);
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(db.getTotalSize());
			return listInfo;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
	}

	public ListInfo getPubishingDocList(String sql, int offset, int maxItem) throws DocumentManagerException {
		ListInfo listInfo = new ListInfo();
		ArrayList list = new ArrayList();
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql, offset, maxItem);
			for (int i = 0; i < db.size(); i++) {
				Document doc = new Document();
				doc.setDocument_id(db.getInt(i, "document_id"));
				doc.setSubtitle(db.getString(i, "subtitle"));
				doc.setChanel_id(db.getInt(i, "channel_id"));
				doc.setChannelName(db.getString(i, "channel_name"));
				doc.setSiteid(db.getInt(i, "site_id"));
				doc.setSiteName(db.getString(i, "docsitename"));
				doc.setPublishTime(db.getDate(i, "pub_start_time"));
				doc.setUsername(db.getString(i, "publisher"));
				doc.setDocOldStatus(db.getString(i, "docoldstatusname"));
				list.add(doc);
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(db.getTotalSize());
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			throw new DocumentManagerException("获取发布中文档列表时数据库操作失败：" + sqle.getMessage());
		}
		return listInfo;
	}

	// 获取引用文档列表
	public ListInfo getCitedDocList(String sql, int offset, int maxItem) throws DocumentManagerException {
		ListInfo listInfo = new ListInfo();
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		ArrayList list = new ArrayList();
		try {
			db.executeSelect(sql, offset, maxItem);
			for (int i = 0; i < db.size(); i++) {
				CitedDocument citedDoc = new CitedDocument();
				int curChannelId = db.getInt(i, "chnl_id");
				int userId = db.getInt(i, "op_user_id");
				int citeType = db.getInt(i, "citetype");
				int docId = db.getInt(i, "doc_id"); // 此docId可能为文档id，也可能为频道id，由citeType决定
				int srcSiteId = db.getInt(i, "site_id");

				citedDoc.setDocid(docId);
				citedDoc.setCiteTime(db.getDate(i, "op_time"));
				citedDoc.setCiteType(citeType);
				citedDoc.setSrcSiteId(srcSiteId);

				String sqltemp = "select name from td_cms_site where site_id = " + srcSiteId;
				db1.executeSelect(sqltemp);
				if (db1.size() > 0)
					citedDoc.setSrcSiteName(db1.getString(0, "name"));

				if (citeType == 0) { // 引用文档
					String sql1 = "select a.subtitle as docName,b.display_name as srcChannelName,"
							+ "c.display_name as curChannelName,d.user_name as citeUserName,e.name as statusName "
							+ "from td_cms_document a,td_cms_channel b,td_cms_channel c,"
							+ "td_sm_user d,tb_cms_doc_status e " + "where a.isdeleted=0 and a.document_id = " + docId
							+ " and a.channel_id = b.channel_id and b.status=0 " + "and c.channel_id = " + curChannelId
							+ " and d.user_id = " + userId + " and e.id = a.status";
					db1.executeSelect(sql1);
					if (db1.size() > 0) {
						citedDoc.setDocName(db1.getString(0, "docName"));
						citedDoc.setSrcChannelName(db1.getString(0, "srcChannelName"));
						citedDoc.setCiteUserName(db1.getString(0, "citeUserName"));
						citedDoc.setCurChannelName(db1.getString(0, "curChannelName"));
						citedDoc.setStatusName(db1.getString(0, "statusName"));
						list.add(citedDoc);
					}
				} else { // 引用频道
					String sql1 = "select a.display_name as srcChannelName,b.user_name as citeUserName,"
							+ "c.display_name as curChannelName "
							+ "from td_cms_channel a,td_sm_user b,td_cms_channel c " + "where a.channel_id = " + docId
							+ " and a.status =0 and b.user_id = " + userId + " and c.channel_id = " + curChannelId;
					db1.executeSelect(sql1);
					if (db1.size() > 0) {
						citedDoc.setDocName(db1.getString(0, "srcChannelName"));
						citedDoc.setSrcChannelName(db1.getString(0, "srcChannelName"));
						citedDoc.setCiteUserName(db1.getString(0, "citeUserName"));
						citedDoc.setCurChannelName(db1.getString(0, "curChannelName"));
						citedDoc.setStatusName("");
						list.add(citedDoc);
					}
				}

			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(db.getTotalSize());
		} catch (Exception sqle) {
			sqle.printStackTrace();
			throw new DocumentManagerException("获取引用文档列表时数据库操作失败：" + sqle.getMessage());
		}
		return listInfo;
	}

	public int getDocCitedType(String citedDocId) throws DocumentManagerException {
		String sql = "select citetype from td_cms_chnl_ref_doc where doc_id =" + citedDocId;
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			if (db.size() > 0)
				return db.getInt(0, "citetype");
		} catch (Exception sqle) {
			sqle.printStackTrace();
			throw new DocumentManagerException("获取引用文档类型时数据库操作失败：" + sqle.getMessage());
		}
		return -1;
	}

	public ListInfo getPigeonholeDocList(String sql, int offset, int maxItem) throws DocumentManagerException {
		ListInfo listInfo = new ListInfo();
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		try {
			db.executeSelect(sql, offset, maxItem);
			ArrayList list = new ArrayList();
			for (int i = 0; i < db.size(); i++) {
				Document doc = new Document();
				doc.setDocument_id(db.getInt(i, "document_id"));
				doc.setSubtitle(db.getString(i, "subtitle"));
				doc.setChannelName(db.getString(i, "channelName"));
				doc.setChanel_id(db.getInt(i, "channel_id"));
				doc.setSiteName(db.getString(i, "siteName"));
				doc.setSiteid(db.getInt(i, "site_id"));
				doc.setStatusname(db.getString(i, "statusname"));
				doc.setDoctype(db.getInt(i, "doctype"));
				doc.setUsername(db.getString(i, "createuserName"));
				doc.setFlowName(db.getString(i, "flowName"));
				doc.setFlowId(db.getInt(i, "flow_id"));
				String str = "select USER_REALNAME from td_sm_user where user_id=" + db.getInt(i, "pigeonholePerfomer")
						+ "";
				db1.executeSelect(str);
				if (db1.size() > 0) {
					doc.setPigeonholePerfomer(db1.getString(0, "USER_REALNAME"));
				}
				str = "select USER_REALNAME from td_sm_user where user_id=" + db.getInt(i, "recycleman") + "";
				db1.executeSelect(str);
				if (db1.size() > 0) {
					doc.setRecyclePerfomer(db1.getString(0, "USER_REALNAME"));
				}
				doc.setCreateTime(db.getDate(i, "createtime"));
				// 设置归档时间
				doc.setPigeonholeTime(db.getDate(i, "pigeonholeTime"));
				// 设置回收时间
				doc.setRecycleTime(db.getDate(i, "recycletime"));
				doc.setPublishTime(db.getDate(i, "publishtime"));
				list.add(doc);
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(db.getTotalSize());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException("获取已归档的文档列表时数据库操作失败：" + e.getMessage());
		}
		return listInfo;
	}

	// 根据sql语句获取文档历史操作的ListInfo列表
	public ListInfo getDocHistoryOperList(String sql, int offset, int maxItem) throws DocumentManagerException {
		ListInfo listInfo = new ListInfo();
		DBUtil dbUtil = new DBUtil();
		DBUtil db = new DBUtil();
		try {
			dbUtil.executeSelect(sql, offset, maxItem);
			List list = new ArrayList();
			DocHistoryOperate hisOper;
			for (int i = 0; i < dbUtil.size(); i++) {
				hisOper = new DocHistoryOperate();
				int userid = dbUtil.getInt(i, "user_id");
				hisOper.setUserid(userid);
				db.executeSelect("select USER_REALNAME from td_sm_user where user_id = " + userid);
				if (db.size() > 0) {
					hisOper.setUserName(db.getString(0, "USER_REALNAME"));
				}
				int docid = dbUtil.getInt(i, "doc_id");
				hisOper.setDocid(docid);
				db.executeSelect("select subtitle from td_cms_document where document_id = " + docid);
				if (db.size() > 0) {
					hisOper.setDocSubtitle(db.getString(0, "subtitle"));
				}
				int operid = dbUtil.getInt(i, "doc_oper_id");
				hisOper.setOperid(operid);
				db.executeSelect("select name from tb_cms_doc_oper where id = " + operid);
				if (db.size() > 0) {
					hisOper.setOperateName(db.getString(0, "name"));
				}
				hisOper.setHisOperid(dbUtil.getInt(i, "id"));
				hisOper.setHisOperContent(dbUtil.getString(i, "oper_content"));
				hisOper.setDocStatusTranid(dbUtil.getInt(i, "doc_trans_id"));
				java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String riqi = formatter.format(dbUtil.getDate(i, "oper_time"));
				hisOper.setHisOperTime(riqi);
				list.add(hisOper);
			}
			listInfo.setDatas(list);
			listInfo.setTotalSize(dbUtil.getTotalSize());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return listInfo;
	}

	public Document getDocWithNoContent(String docid) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		Document document = null;
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		StringBuffer sql = new StringBuffer()
				.append("select doc.DOCUMENT_ID, doc.TITLE, doc.SUBTITLE, ")
				.append("doc.AUTHOR,  doc.CHANNEL_ID,")
				.append("doc.STATUS, doc.KEYWORDS, doc.DOCABSTRACT,")
				.append("doc.DOCTYPE, doc.DOCWTIME, doc.TITLECOLOR,")
				.append("doc.CREATETIME, doc.CREATEUSER, doc.DOCSOURCE_ID,")
				.append("doc.DETAILTEMPLATE_ID, doc.LINKTARGET, doc.FLOW_ID,")
				.append("doc.DOC_LEVEL, doc.DOC_KIND, doc.PARENT_DETAIL_TPL,")
				.append("doc.ISDELETED, doc.AUDITTIME, doc.PUBLISHTIME, ")
				.append("doc.GUIDANGTIME, doc.RECYCLETIME, doc.GUIDANGMAN, ")
				.append("doc.RECYCLEMAN, doc.VERSION, doc.PIC_PATH, ")
				.append("doc.MEDIAPATH, doc.PUBLISHFILENAME, doc.COMMENT_TEMPLATE_ID, ")
				.append("doc.COMMENTSWITCH, doc.SECONDTITLE, doc.ISNEW,")
				.append("doc.NEWPIC_PATH, doc.ORDERTIME, doc.FILECONTENT,")
				.append("doc.SEQ, doc.EXT_CLASS, doc.EXT_ORG, ")
				.append(" doc.EXT_WH, doc.EXT_INDEX, doc.EXT_DJH, ")
				.append("COUNT, DOC_CLASS, AUDITFLAG,ds.SRCNAME as SRCNAME, tmpl.NAME as NAME from TD_CMS_DOCUMENT doc ")
				.append("inner join TD_CMS_DOCSOURCE ds on ds.DOCSOURCE_ID=doc.DOCSOURCE_ID ")
				.append("left join TD_CMS_TEMPLATE tmpl on tmpl.TEMPLATE_ID=doc.DETAILTEMPLATE_ID ")
				.append("where doc.document_id=").append(docid);
		// log.warn(sql.toString());
		try {
			db.executeSelect(sql.toString());

			if (db.size() > 0) {
				document = new Document();
				document.setDocument_id(db.getInt(0, "document_id"));
				document.setTitle(db.getString(0, "title"));
				document.setSubtitle(db.getString(0, "SUBTITLE"));
				document.setStatus(db.getInt(0, "status"));
				document.setDocflag(db.getInt(0, "isdeleted"));
				document.setAuthor(db.getString(0, "AUTHOR"));
				document.setCreateTime(db.getDate(0, "CREATETIME"));
				document.setDoctype(db.getInt(0, "DOCTYPE"));

				document.setChanel_id(db.getInt(0, "channel_id"));
				document.setLinktarget(db.getString(0, "LINKTARGET"));
				// 出于链接文档的链接地址保存的是在content字段的考虑
				if (document.getDoctype() == 1)
					document.setLinkfile(document.getContent());
				document.setDocsource_id(db.getInt(0, "DOCSOURCE_ID"));

				document.setDocwtime(db.getDate(0, "DOCWTIME"));

				document.setCreateUser(db.getInt(0, "CREATEUSER"));
				document.setDetailtemplate_id(db.getInt(0, "DETAILTEMPLATE_ID"));
				document.setDocabstract(db.getString(0, "DOCABSTRACT"));

				document.setTitlecolor(db.getString(0, "TITLECOLOR"));
				document.setKeywords(db.getString(0, "KEYWORDS"));
				document.setDoc_level(db.getInt(0, "doc_level"));
				document.setParentDetailTpl(db.getString(0, "PARENT_DETAIL_TPL"));
				document.setPicPath(db.getString(0, "PIC_PATH"));
				document.setMediapath(db.getString(0, "mediapath"));
				document.setPublishfilename(db.getString(0, "publishfilename"));
				document.setSecondtitle(db.getString(0, "secondtitle"));
				document.setIsNew(db.getInt(0, "isnew"));
				document.setNewPicPath(db.getString(0, "newpic_path"));
				document.setOrdertime(db.getDate(0, "ordertime"));
				document.setSeq(db.getInt(0, "seq"));
				document.setCommentswitch(db.getInt(0, "commentswitch"));
				document.setDoc_class(db.getString(0, "doc_class"));
				/* 装载扩展字段数据 */
				document.setExtColumn(extManager.getExtColumnInfo(0,db));

				// String str ="select SRCNAME from TD_CMS_DOCSOURCE where
				// DOCSOURCE_ID ="+ db.getInt(0,"DOCSOURCE_ID") +"";
				// db1.executeSelect(str);
				// if(db1.size()>0)
				// {
				// document.setDocsource_name(db1.getString(0,"SRCNAME"));
				// }
				document.setDocsource_name(db.getString(0, "SRCNAME"));

				// String str1 ="select NAME from TD_CMS_TEMPLATE where
				// TEMPLATE_ID="+ db.getInt(0,"DETAILTEMPLATE_ID") +"";
				// db1.executeSelect(str1);
				// if(db1.size()>0)
				// {
				// document.setDetailtemplate_name(db1.getString(0,"NAME"));
				// }
				document.setDetailtemplate_name(db.getString(0, "NAME"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}

		return document;
	}
	public Document getDoc(String docid) throws DocumentManagerException 
	{
		return getDoc(docid,false);
	}
	/**
	 * modify by ge.tao 2007-09-17 合并SQL
	 */
	public Document getDoc(String docid,boolean loaddocextfield) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		Document document = null;
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		StringBuffer sql = new StringBuffer()
				.append("select doc.*,ds.SRCNAME as SRCNAME, tmpl.NAME as NAME from TD_CMS_DOCUMENT doc ")
				.append("inner join TD_CMS_DOCSOURCE ds on ds.DOCSOURCE_ID=doc.DOCSOURCE_ID ")
				.append("left join TD_CMS_TEMPLATE tmpl on tmpl.TEMPLATE_ID=doc.DETAILTEMPLATE_ID ")
				.append("where doc.document_id=").append(docid);
		// log.warn(sql.toString());
		try {
			db.executeSelect(sql.toString());

			if (db.size() > 0) {
				document = new Document();
				document.setDocument_id(db.getInt(0, "document_id"));
				document.setTitle(db.getString(0, "title"));
				document.setSubtitle(db.getString(0, "SUBTITLE"));
				document.setStatus(db.getInt(0, "status"));
				document.setDocflag(db.getInt(0, "isdeleted"));
				document.setAuthor(db.getString(0, "AUTHOR"));
				document.setCreateTime(db.getDate(0, "CREATETIME"));
				document.setDoctype(db.getInt(0, "DOCTYPE"));
				document.setContent(db.getString(0, "CONTENT"));
				document.setChanel_id(db.getInt(0, "channel_id"));
				document.setLinktarget(db.getString(0, "LINKTARGET"));
				// 出于链接文档的链接地址保存的是在content字段的考虑
				if (document.getDoctype() == 1)
					document.setLinkfile(document.getContent());
				document.setDocsource_id(db.getInt(0, "DOCSOURCE_ID"));

				document.setDocwtime(db.getDate(0, "DOCWTIME"));

				document.setCreateUser(db.getInt(0, "CREATEUSER"));
				document.setDetailtemplate_id(db.getInt(0, "DETAILTEMPLATE_ID"));
				document.setDocabstract(db.getString(0, "DOCABSTRACT"));

				document.setTitlecolor(db.getString(0, "TITLECOLOR"));
				document.setKeywords(db.getString(0, "KEYWORDS"));
				document.setDoc_level(db.getInt(0, "doc_level"));
				document.setParentDetailTpl(db.getString(0, "PARENT_DETAIL_TPL"));
				document.setPicPath(db.getString(0, "PIC_PATH"));
				document.setMediapath(db.getString(0, "mediapath"));
				document.setPublishfilename(db.getString(0, "publishfilename"));
				document.setSecondtitle(db.getString(0, "secondtitle"));
				document.setIsNew(db.getInt(0, "isnew"));
				document.setNewPicPath(db.getString(0, "newpic_path"));
				document.setOrdertime(db.getDate(0, "ordertime"));
				document.setSeq(db.getInt(0, "seq"));
				document.setCommentswitch(db.getInt(0, "commentswitch"));
				document.setDoc_class(db.getString(0, "doc_class"));
				/* 装载系统扩展字段数据 */
				document.setExtColumn(extManager.getExtColumnInfo(0,db));
				if(loaddocextfield)
					document.setDocExtField(getDocExtFieldMapBean(docid));

				// String str ="select SRCNAME from TD_CMS_DOCSOURCE where
				// DOCSOURCE_ID ="+ db.getInt(0,"DOCSOURCE_ID") +"";
				// db1.executeSelect(str);
				// if(db1.size()>0)
				// {
				// document.setDocsource_name(db1.getString(0,"SRCNAME"));
				// }
				document.setDocsource_name(db.getString(0, "SRCNAME"));

				// String str1 ="select NAME from TD_CMS_TEMPLATE where
				// TEMPLATE_ID="+ db.getInt(0,"DETAILTEMPLATE_ID") +"";
				// db1.executeSelect(str1);
				// if(db1.size()>0)
				// {
				// document.setDetailtemplate_name(db1.getString(0,"NAME"));
				// }
				document.setDetailtemplate_name(db.getString(0, "NAME"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}

		return document;

	}

	/**
	 * 通过文档ID获取文档的部分信息 不要clob字段 content
	 * 
	 * @param docid
	 *            如:123
	 * @return Document
	 * @throws DocumentManagerException
	 *             DocumentManagerImpl.java
	 * @author: 陶格
	 */
	public Document getPartDocInfoById(String docid) throws DocumentManagerException {
		PreparedDBUtil db = new PreparedDBUtil();
		Document document = null;
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		StringBuffer sql = new StringBuffer();
		sql.append("select document_id,title,SUBTITLE,t.status as status,AUTHOR, indexpagepath,")
				.append("t.CREATETIME, DOCTYPE, case when DOCTYPE=1 ")
				.append("then t.content else null end linkfile,t.channel_id,LINKTARGET,t.DOCSOURCE_ID, ")
				.append("DOCWTIME,t.CREATEUSER,t.DETAILTEMPLATE_ID,DOCABSTRACT,TITLECOLOR,")
				.append("KEYWORDS,doc_level,PARENT_DETAIL_TPL,PIC_PATH,mediapath,publishfilename,secondtitle, ")
				.append("isnew,newpic_path,ordertime,")
				.append("ds.SRCNAME as SRCNAME, tmpl.NAME as NAME,t_channel.site_id as site_id,t_channel.display_name as channelname ")
				.append("from TD_CMS_DOCUMENT t inner join TD_CMS_DOCSOURCE ds on ds.DOCSOURCE_ID=t.DOCSOURCE_ID ")
				.append("left join TD_CMS_TEMPLATE tmpl on tmpl.TEMPLATE_ID=t.DETAILTEMPLATE_ID ")
				.append("left join td_cms_channel t_channel on t.channel_id = t_channel.channel_id ")
				.append(" where document_id =?");
//				.append(docid);

		// log.warn(sql.toString());
		try {
			db.preparedSelect(sql.toString());
			db.setInt(1,Integer.parseInt(docid));
			db.executePrepared();
			if (db.size() > 0) {
				document = new Document();
				document.setDocument_id(db.getInt(0, "document_id"));
				document.setTitle(db.getString(0, "title"));
				document.setSubtitle(db.getString(0, "SUBTITLE"));
				document.setStatus(db.getInt(0, "status"));
				document.setAuthor(db.getString(0, "AUTHOR"));
				document.setCreateTime(db.getDate(0, "CREATETIME"));
				document.setDoctype(db.getInt(0, "DOCTYPE"));
				document.setChanel_id(db.getInt(0, "channel_id"));
				document.setLinktarget(db.getString(0, "LINKTARGET"));
				// 出于链接文档的链接地址保存的是在content字段的考虑
				// if(document.getDoctype() == 1)
				document.setLinkfile(db.getString(0, "linkfile"));
				document.setDocsource_id(db.getInt(0, "DOCSOURCE_ID"));

				document.setDocwtime(db.getDate(0, "DOCWTIME"));

				document.setCreateUser(db.getInt(0, "CREATEUSER"));
				document.setDetailtemplate_id(db.getInt(0, "DETAILTEMPLATE_ID"));
				document.setDocabstract(db.getString(0, "DOCABSTRACT"));

				document.setTitlecolor(db.getString(0, "TITLECOLOR"));
				document.setKeywords(db.getString(0, "KEYWORDS"));
				document.setDoc_level(db.getInt(0, "doc_level"));
				document.setParentDetailTpl(db.getString(0, "PARENT_DETAIL_TPL"));
				document.setPicPath(db.getString(0, "PIC_PATH"));
				document.setMediapath(db.getString(0, "mediapath"));
				document.setPublishfilename(db.getString(0, "publishfilename"));
				document.setSecondtitle(db.getString(0, "secondtitle"));
				document.setIsNew(db.getInt(0, "isnew"));
				document.setNewPicPath(db.getString(0, "newpic_path"));
				document.setOrdertime(db.getDate(0, "ordertime"));
				document.setSiteid(db.getInt(0, "site_id"));
				document.setChannelName(db.getString(0, "channelname"));

				/* 装载扩展字段数据 */
				document.setExtColumn(extManager.getExtColumnInfo(0,db));

				document.setDocsource_name(db.getString(0, "SRCNAME"));

				document.setDetailtemplate_name(db.getString(0, "NAME"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}

		return document;

	}

	/**
	 * 通过被引用文档ID 获取引用该文档的文档列表 文档信息不要clob字段 content list.add(Dcoument)
	 * 
	 * @param docid
	 *            123;22;333;
	 * @return
	 * @throws DocumentManagerException
	 *             DocumentManager.java
	 * @author: ge.tao
	 */
	public List getDocInfoByIds(String docids) throws DocumentManagerException {
		List list = new ArrayList();
		DBUtil db = new DBUtil();
		Document document = null;
		if (docids.endsWith(";"))
			docids = docids.substring(0, docids.length() - 1);
		docids = docids.replace(';', ',');
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		StringBuffer sql = new StringBuffer();
		sql.append("select document_id,title,SUBTITLE,t.status as status ,AUTHOR, indexpagepath,")
				.append("CREATETIME, DOCTYPE, case when DOCTYPE=1 ")
				.append("then t.content else null end linkfile,channel_id,LINKTARGET,t.DOCSOURCE_ID, ")
				.append("DOCWTIME,CREATEUSER,t.DETAILTEMPLATE_ID,DOCABSTRACT,TITLECOLOR,")
				.append("KEYWORDS,doc_level,PARENT_DETAIL_TPL,PIC_PATH,mediapath,publishfilename,secondtitle, ")
				.append("isnew,newpic_path,ordertime,")
				.append("ds.SRCNAME as SRCNAME, tmpl.NAME as NAME,t_channel.site_id as site_id ")
				.append("from TD_CMS_DOCUMENT t inner join TD_CMS_DOCSOURCE ds on ds.DOCSOURCE_ID=t.DOCSOURCE_ID ")
				.append("left join TD_CMS_TEMPLATE tmpl on tmpl.TEMPLATE_ID=t.DETAILTEMPLATE_ID ")
				.append("left join td_cms_channel t_channel on t.channel_id = t_channel.channel_id ")
				.append(" where document_id in (").append(docids).append(") ");
		log.warn(sql.toString());
		try {
			db.executeSelect(sql.toString());
			for (int i = 0; i < db.size(); i++) {
				document = new Document();
				document.setDocument_id(db.getInt(i, "document_id"));
				document.setTitle(db.getString(i, "title"));
				document.setSubtitle(db.getString(i, "SUBTITLE"));
				document.setStatus(db.getInt(i, "status"));
				document.setAuthor(db.getString(i, "AUTHOR"));
				document.setCreateTime(db.getDate(i, "CREATETIME"));
				document.setDoctype(db.getInt(i, "DOCTYPE"));
				document.setChanel_id(db.getInt(i, "channel_id"));
				document.setLinktarget(db.getString(i, "LINKTARGET"));
				// 出于链接文档的链接地址保存的是在content字段的考虑
				// if(document.getDoctype() == 1)
				document.setLinkfile(db.getString(i, "linkfile"));
				document.setDocsource_id(db.getInt(i, "DOCSOURCE_ID"));

				document.setDocwtime(db.getDate(i, "DOCWTIME"));

				document.setCreateUser(db.getInt(i, "CREATEUSER"));
				document.setDetailtemplate_id(db.getInt(i, "DETAILTEMPLATE_ID"));
				document.setDocabstract(db.getString(i, "DOCABSTRACT"));

				document.setTitlecolor(db.getString(i, "TITLECOLOR"));
				document.setKeywords(db.getString(i, "KEYWORDS"));
				document.setDoc_level(db.getInt(i, "doc_level"));
				document.setParentDetailTpl(db.getString(i, "PARENT_DETAIL_TPL"));
				document.setPicPath(db.getString(i, "PIC_PATH"));
				document.setMediapath(db.getString(i, "mediapath"));
				document.setPublishfilename(db.getString(i, "publishfilename"));
				document.setSecondtitle(db.getString(i, "secondtitle"));
				document.setOrdertime(db.getDate(i, "ordertime"));
				document.setSiteid(db.getInt(i, "site_id"));
				/* 装载扩展字段数据 */
				document.setExtColumn(extManager.getExtColumnInfo(i,db));
				document.setDocsource_name(db.getString(i, "SRCNAME"));
				document.setDetailtemplate_name(db.getString(i, "NAME"));
				list.add(document);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// 回收单个文档
	public void garbageDoc(int docid, int userid) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		int status = this.getDocStatus(docid);
		// 将回收标志位置为1
		String sql = "update td_cms_document set isdeleted=1,recycletime =" + DBUtil.getDBAdapter().to_date(new Date())
				+ ",recycleman=" + userid + " where document_id=" + docid;
		try {
			db.executeUpdate(sql);
			// 将文档当前的任务置为无效
			sql = "update td_cms_doc_task_detail set valid=0 where task_id in"
					+ "(select a.task_id from td_cms_doc_task a where a.pre_status ="
					+ status
					+ " and a.document_id="
					+ docid
					+ " and not exists "
					+ "(select * from td_cms_doc_task_detail b where b.task_id = a.task_id and b.complete_time is not null))";
			db.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException("回收时出现异常：" + e.getMessage());
		}
	}

	// 批量回收文档
	public void garbageDoc(int[] docids, int userid) throws DocumentManagerException {
		try {
			for (int i = 0; i < docids.length; i++) {
				garbageDoc(docids[i], userid);
			}
		} catch (DocumentManagerException de) {
			de.printStackTrace();
			throw de;
		}
	}

	/**
	 * 恢复已回收的文档
	 */
	public void recoverDoc(int docid) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		int status = this.getDocStatus(docid);
		String sql = "update td_cms_document set isdeleted=0 where document_id=" + docid;
		try {
			db.executeUpdate(sql);
			// 恢复任务，即将文档当前的任务置为有效
			sql = "update td_cms_doc_task_detail set valid=1 where task_id in"
					+ "(select a.task_id from td_cms_doc_task a where a.pre_status ="
					+ status
					+ " and a.document_id="
					+ docid
					+ " and not exists "
					+ "(select * from td_cms_doc_task_detail b where b.task_id = a.task_id and b.complete_time is not null))";
			db.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException("垃圾文档恢复时出现异常" + e.getMessage());
		}
	}

	// 批量恢复文档
	public void recoverDoc(int[] docids) throws DocumentManagerException {
		try {
			for (int i = 0; i < docids.length; i++) {
				recoverDoc(docids[i]);
			}
		} catch (DocumentManagerException de) {
			de.printStackTrace();
			throw de;
		}
	}

	public List getDocSourceList() throws DocumentManagerException {
		List docSourcelist = new ArrayList();
		DBUtil conn = new DBUtil();
		String sql = "";
		try {
			sql = "select * from TD_CMS_DOCSOURCE order by crtime desc,docsource_id";
			// System.out.println(sql);
			conn.executeSelect(sql);
			if (conn.size() > 0) {
				for (int i = 0; i < conn.size(); i++) {
					DocumentSource ds = new DocumentSource();
					ds.setDocsource_id(conn.getInt(i, "docsource_id"));
					ds.setSrcname(conn.getString(i, "srcname"));
					docSourcelist.add(ds);
				}
			}

		} catch (Exception e) {
			System.out.print("取文档来源信息出错!" + e);
			throw new DocumentManagerException(e.getMessage());
		}
		return docSourcelist;

	}

	/**
	 * 
	 */
	public List getDistributeList(String channelid) throws DocumentManagerException {
		List distributeList = new ArrayList();
		DBUtil dbUtil = new DBUtil();
		String sql = "";
		try {
			sql = "select user_id,user_realname from TD_sm_user where user_id "
					+ " in(select distinct createuser from td_cms_document where channel_id =" + channelid + ") ";
			// System.out.println(sql);
			dbUtil.executeSelect(sql);
			if (dbUtil.size() > 0) {
				for (int i = 0; i < dbUtil.size(); i++) {
					DistributeUser du = new DistributeUser();
					du.setUser_id(dbUtil.getInt(i, "user_id"));
					du.setUser_name(dbUtil.getString(i, "user_realname"));
					distributeList.add(du);
				}
			}

		} catch (Exception e) {
			System.out.print("取发稿人来源信息出错!" + e);
			throw new DocumentManagerException(e.getMessage());
		}
		return distributeList;
	}

	/**
	 * 取站点下所有采集过文档的人员列表
	 */
	public List getDistributeListOfSite(String siteid) throws DocumentManagerException {
		List distributeList = new ArrayList();
		DBUtil dbUtil = new DBUtil();
		String sql = "";
		try {
			sql = "select user_id,user_realname from TD_sm_user where user_id "
					+ " in(select distinct a.createuser from td_cms_document a ,td_cms_channel b,td_cms_site c "
					+ "where a.channel_id = b.channel_id and c.site_id = b.site_id and b.site_id =" + siteid + ") ";
			// System.out.println(sql);
			dbUtil.executeSelect(sql);
			if (dbUtil.size() > 0) {
				for (int i = 0; i < dbUtil.size(); i++) {
					DistributeUser du = new DistributeUser();
					du.setUser_id(dbUtil.getInt(i, "user_id"));
					du.setUser_name(dbUtil.getString(i, "user_realname"));
					distributeList.add(du);
				}
			}

		} catch (Exception e) {
			System.out.print("取发稿人来源信息出错!" + e);
			throw new DocumentManagerException(e.getMessage());
		}
		return distributeList;
	}

	// 根据频道id获取这个频道所有引用文档的引用人
	public List getCiteUserList(String channelid) throws DocumentManagerException {
		List distributeList = new ArrayList();
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		String sql = "";
		try {
			sql = "select user_id,user_realname from TD_sm_user where user_id "
					+ " in(select op_user_id from td_cms_chnl_ref_doc where chnl_id =?) ";
			dbUtil.preparedSelect(sql);
			dbUtil.setInt(1, Integer.parseInt(channelid));
			dbUtil.executePrepared();
			if (dbUtil.size() > 0) {
				for (int i = 0; i < dbUtil.size(); i++) {
					DistributeUser du = new DistributeUser();
					du.setUser_id(dbUtil.getInt(i, "user_id"));
					du.setUser_name(dbUtil.getString(i, "user_realname"));
					distributeList.add(du);
				}
			}

		} catch (Exception e) {
			throw new DocumentManagerException("获取引用人信息时出错：" + e.getMessage());
		}
		return distributeList;
	}

	public List getDocVerUserList(String docid) throws DocumentManagerException {
		List distributeList = new ArrayList();
		DBUtil dbUtil = new DBUtil();
		String sql = "";
		try {
			sql = "select user_id,user_realname from TD_sm_user where user_id "
					+ " in(select op_user from td_cms_doc_ver where document_id =" + docid + ") ";
			dbUtil.executeSelect(sql);
			if (dbUtil.size() > 0) {
				for (int i = 0; i < dbUtil.size(); i++) {
					DistributeUser du = new DistributeUser();
					du.setUser_id(dbUtil.getInt(i, "user_id"));
					du.setUser_name(dbUtil.getString(i, "user_realname"));
					distributeList.add(du);
				}
			}

		} catch (Exception e) {
			throw new DocumentManagerException("获取文档版本保存人信息时出错：" + e.getMessage());
		}
		return distributeList;
	}

	// 根据频道id获取这个频道所有引用文档的所在原频道列表
	public List getCiteSrcChnlList(String channelid) throws DocumentManagerException {
		List distributeList = new ArrayList();
		PreparedDBUtil dbUtil = new PreparedDBUtil();
		String sql = "";
		try {
			sql = "select distinct b.channel_id as channelid,b.name as channelName from "
					+ "td_cms_document a,td_cms_channel b,td_cms_chnl_ref_doc c " + "where c.chnl_id = ?"
					+ " and " + "c.doc_id = a.document_id and a.channel_id = b.channel_id";

			dbUtil.preparedSelect(sql);
			dbUtil.setInt(1, Integer.parseInt(channelid));
			dbUtil.executePrepared();
			if (dbUtil.size() > 0) {
				for (int i = 0; i < dbUtil.size(); i++) {
					CitedDocSrcChannel du = new CitedDocSrcChannel();
					du.setChannelid(dbUtil.getInt(i, "channelid"));
					du.setChannelName(dbUtil.getString(i, "channelName"));
					distributeList.add(du);
				}
			}

		} catch (Exception e) {
			throw new DocumentManagerException("获取引用人信息时出错：" + e.getMessage());
		}
		return distributeList;
	}

	public List getStatusList() throws DocumentManagerException {
		List statusList = new ArrayList();
		DBUtil dbUtil = new DBUtil();
		String sql = "";
		try {
			sql = "select * from TB_CMS_DOC_STATUS where id not in(8,20,100)";
			// System.out.println(sql);
			dbUtil.executeSelect(sql);
			if (dbUtil.size() > 0) {
				for (int i = 0; i < dbUtil.size(); i++) {
					DocStatus ds = new DocStatus();
					ds.setStatus_id(dbUtil.getInt(i, "id"));
					ds.setStatus_name(dbUtil.getString(i, "name"));
					statusList.add(ds);
				}
			}

		} catch (Exception e) {
			System.out.print("取文档状态信息出错!" + e);
			throw new DocumentManagerException(e.getMessage());
		}
		return statusList;
	}

	public List getStatusList(int flowId) throws DocumentManagerException {
		List statusList = new ArrayList();
		DBUtil dbUtil = new DBUtil();
		String sql = "";
		try {
			sql = "select distinct a.id as id,a.name as name "
					+ "from tb_cms_doc_status a,tb_cms_flow b,tb_cms_flow_doc_trans c,tb_cms_doc_status_trans d "
					+ "where a.id not in(8,20,100,10,12) and b.id=" + flowId
					+ " and b.id=c.flow_id and c.transision_id = d.id and d.src_status = a.id ";
			dbUtil.executeSelect(sql);
			if (dbUtil.size() > 0) {
				for (int i = 0; i < dbUtil.size(); i++) {
					DocStatus ds = new DocStatus();
					ds.setStatus_id(dbUtil.getInt(i, "id"));
					ds.setStatus_name(dbUtil.getString(i, "name"));
					statusList.add(ds);
				}
			}

		} catch (Exception e) {
			System.out.print("取文档状态信息出错!" + e);
			throw new DocumentManagerException(e.getMessage());
		}
		return statusList;
	}

	public int getDocStatus(int docId) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		try {
			String sql = "select status from td_cms_document " + "where document_id = " + docId;
			db.executeSelect(sql);
			if (db.size() > 0)
				return (db.getInt(0, "status"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return -1;
	}

	public int getDocType(int docId) throws DocumentManagerException {
		int type = 0;
		DBUtil db = new DBUtil();
		try {
			String sql = "select doctype from td_cms_document " + "where document_id = " + docId;
			db.executeSelect(sql);
			if (db.size() > 0)
				type = db.getInt(0, "doctype");
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return type;
	}

	/**
	 * 获取文档所在频道的id
	 */
	public int getDocChnlId(int docId) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		try {
			String sql = "select channel_id from td_cms_document " + "where document_id = " + docId;
			db.executeSelect(sql);
			if (db.size() > 0)
				return (db.getInt(0, "channel_id"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return -1;
	}

	/**
	 * 获取文档所在站点的id
	 */
	public int getDocSiteId(int docId) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		try {
			String sql = "select b.site_id as site_id from td_cms_document a, td_cms_channel b "
					+ "where a.channel_id = b.channel_id and a.document_id = " + docId;
			db.executeSelect(sql);
			if (db.size() > 0)
				return (db.getInt(0, "site_id"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return -1;
	}

	/**
	 * 判断当前用户对指定文档是否有指定任务
	 * 
	 * @param userId
	 * @param docId
	 * @param task
	 *            1：审核任务 2：返工任务 3：发布任务
	 * @return 返回任务id，没有则返回0
	 */
	public int hasTask(int userId, int docId, int task) {
		DBUtil db = new DBUtil();
		int taskId = 0;
		String pre_status = "";
		switch (task) {
		case 1:
			pre_status = DocumentStatus.UNAUDIT.getStatus();
			break;
		case 2:
			pre_status = DocumentStatus.ROLLBACK.getStatus();
			break;
		case 3:
			pre_status = DocumentStatus.PREPUBLISH.getStatus();
			break;
		}
		String sql = "select a.task_id as task_id from td_cms_doc_task a,td_cms_doc_task_detail c where a.pre_status ="
				+ pre_status
				+ " and a.document_id="
				+ docId
				+ " and c.task_id = a.task_id and c.performer="
				+ userId
				+ " and not exists "
				+ "(select * from td_cms_doc_task_detail b where b.task_id = a.task_id and b.complete_time is not null)";
		try {
			db.executeSelect(sql);
			if (db.size() > 0)
				taskId = db.getInt(0, "task_id");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return taskId;
	}

	public int[] hasTask(int userId, int docIds[], int task) {
		int[] tasks = new int[docIds.length];
		for (int i = 0; i < docIds.length; i++)
			tasks[i] = this.hasTask(userId, docIds[i], task);
		return tasks;
	}

	/*
	 * {发布预览，送审，撤消送审，审核，提交发布，发布，归档，版本管理，设置置顶}的子集 任务文档的操作不仅要有操作权限，允许状态迁移，还必须有任务 发布预览没有权限控制，也不关是否有模板，只要是普通文档则显示
	 */
	public Map getDocOpers(int docId, AccessControl accessControl) throws DocumentManagerException {
		String chnlId = getDocChnlId(docId) + "";
		String siteId = getDocSiteId(docId) + "";

		Map docOpers = new HashMap();
		if (getDocType(docId) == 0 || getDocType(docId) == 1)
			docOpers.put("pubPrevie", "发布预览");
		if ((accessControl.checkPermission(chnlId, AccessControl.DELIVER_PERMISSION, AccessControl.CHANNELDOC_RESOURCE) || accessControl
				.checkPermission(siteId, AccessControl.DELIVER_PERMISSION, AccessControl.SITEDOC_RESOURCE))
				&& this.canTransition(docId, Integer.parseInt(DocumentStatus.UNAUDIT.getStatus())) >= 0
				&& (this.getDocStatus(docId) == Integer.parseInt(DocumentStatus.NEW.getStatus()) || this.hasTask(
						Integer.parseInt(accessControl.getUserID()), docId, 2) != 0))
			docOpers.put("deliver", "送审");

		if ((accessControl.checkPermission(chnlId, AccessControl.WITHDRAW_DELIVER_PERMISSION,
				AccessControl.CHANNELDOC_RESOURCE) || accessControl.checkPermission(siteId,
				AccessControl.WITHDRAW_DELIVER_PERMISSION, AccessControl.SITEDOC_RESOURCE))
				&& this.canTransition(docId, Integer.parseInt(DocumentStatus.NEW.getStatus())) >= 0)
			docOpers.put("withdrawdeliver", "撤消送审");

		if ((accessControl.checkPermission(chnlId, AccessControl.AUDIT_PERMISSION, AccessControl.CHANNELDOC_RESOURCE) || accessControl
				.checkPermission(siteId, AccessControl.AUDIT_PERMISSION, AccessControl.SITEDOC_RESOURCE))
				&& this.canTransition(docId, Integer.parseInt(DocumentStatus.AUDITED.getStatus())) >= 0
				&& hasTask(Integer.parseInt(accessControl.getUserID()), docId, 1) != 0)
			docOpers.put("audit", "审核");

		if ((accessControl.checkPermission(chnlId, AccessControl.SUBPUBLISH_PERMISSION,
				AccessControl.CHANNELDOC_RESOURCE) || accessControl.checkPermission(siteId,
				AccessControl.SUBPUBLISH_PERMISSION, AccessControl.SITEDOC_RESOURCE))
				&& this.canTransition(docId, Integer.parseInt(DocumentStatus.PREPUBLISH.getStatus())) >= 0)
			docOpers.put("subpublish", "提交发布");

		if ((accessControl.checkPermission(chnlId, AccessControl.DOCPUBLISH_PERMISSION,
				AccessControl.CHANNELDOC_RESOURCE) || accessControl.checkPermission(siteId,
				AccessControl.DOCPUBLISH_PERMISSION, AccessControl.SITEDOC_RESOURCE))
				&& this.canTransition(docId, Integer.parseInt(DocumentStatus.PUBLISHED.getStatus())) >= 0)
			// &&
			// hasTask(Integer.parseInt(accessControl.getUserID()),docId,3)!=0)
			// //新稿可以直接发布，没有发布任务
			docOpers.put("publish", "发布");

		if ((accessControl.checkPermission(chnlId, AccessControl.WITHDRAWPUBLISH_MANAGER,
				AccessControl.CHANNELDOC_RESOURCE) || accessControl.checkPermission(siteId,
				AccessControl.WITHDRAWPUBLISH_MANAGER, AccessControl.SITEDOC_RESOURCE))
				&& this.canTransition(docId, 6) >= 0)
			docOpers.put("withdrawPublish", "撤销发布");

		if ((accessControl.checkPermission(chnlId, AccessControl.ARCHIVE_PERMISSION, AccessControl.CHANNELDOC_RESOURCE) || accessControl
				.checkPermission(siteId, AccessControl.ARCHIVE_PERMISSION, AccessControl.SITEDOC_RESOURCE))
				&& this.canTransition(docId, Integer.parseInt(DocumentStatus.ARCHIVED.getStatus())) >= 0)
			docOpers.put("archive", "归档");

		if (accessControl.checkPermission(chnlId, AccessControl.MANAGE_DOCVER_PERMISSION,
				AccessControl.CHANNELDOC_RESOURCE)
				|| accessControl.checkPermission(siteId, AccessControl.MANAGE_DOCVER_PERMISSION,
						AccessControl.SITEDOC_RESOURCE))
			docOpers.put("manageDocVer", "版本管理");

		if (accessControl
				.checkPermission(chnlId, AccessControl.UPARRANGE_PERMISSION, AccessControl.CHANNELDOC_RESOURCE)
				|| accessControl.checkPermission(siteId, AccessControl.UPARRANGE_PERMISSION,
						AccessControl.SITEDOC_RESOURCE))
			docOpers.put("addArrangeDoc", "设置置顶");

		if (accessControl.checkPermission(chnlId, AccessControl.MANAGE_DOCCOMMENT_PERMISSION,
				AccessControl.CHANNELDOC_RESOURCE)
				|| accessControl.checkPermission(siteId, AccessControl.MANAGE_DOCCOMMENT_PERMISSION,
						AccessControl.SITEDOC_RESOURCE))
			docOpers.put("manageDocCommnet", "评论管理");
		return docOpers;
	}

	/**
	 * 获取多页文档列表
	 * 
	 * @param document_id
	 * @return
	 */
	public List getPagingDocs(int document_id) throws DocumentManagerException {
		List list = new ArrayList();
		return list;
	}

	/**
	 * 获取聚合文档的文档列表
	 * 
	 * @param document_id
	 * @return
	 */
	public List getCompositeDocs(int document_id) throws DocumentManagerException {
		List list = new ArrayList();
		return list;
	}

	public Document getDoc(ContentContext context) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		Document document = null;
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		String sql = "select * from TD_CMS_DOCUMENT where document_id=" + context.getContentid() + "";
		try {
			db.executeSelect(context.getDBName(), sql);

			if (db.size() > 0) {
				document = new Document();
				document.setDocument_id(db.getInt(0, "document_id"));
				document.setTitle(db.getString(0, "title"));
				document.setSubtitle(db.getString(0, "SUBTITLE"));
				document.setStatus(db.getInt(0, "status"));
				document.setAuthor(db.getString(0, "AUTHOR"));
				document.setCreateTime(db.getDate(0, "CREATETIME"));
				document.setDoctype(db.getInt(0, "DOCTYPE"));
				document.setContent(db.getString(0, "CONTENT"));
				document.setChanel_id(db.getInt(0, "channel_id"));
				document.setLinktarget(db.getString(0, "LINKTARGET"));
				// 出于链接文档的链接地址保存的是在content字段的考虑
				if (document.getDoctype() == 1)
					document.setLinkfile(document.getContent());
				document.setDocsource_id(db.getInt(0, "DOCSOURCE_ID"));

				document.setDocwtime(db.getDate(0, "DOCWTIME"));

				document.setCreateUser(db.getInt(0, "CREATEUSER"));
				document.setDetailtemplate_id(db.getInt(0, "DETAILTEMPLATE_ID"));
				document.setDocabstract(db.getString(0, "DOCABSTRACT"));

				document.setTitlecolor(db.getString(0, "TITLECOLOR"));
				document.setKeywords(db.getString(0, "KEYWORDS"));
				document.setDoc_level(db.getInt(0, "doc_level"));
				document.setParentDetailTpl(db.getString(0, "PARENT_DETAIL_TPL"));
				document.setPicPath(db.getString(0, "PIC_PATH"));
				document.setMediapath(db.getString(0, "mediapath"));
				document.setPublishfilename(db.getString(0, "publishfilename"));
				document.setSecondtitle(db.getString(0, "secondtitle"));
				document.setIsNew(db.getInt(0, "isnew"));
				document.setNewPicPath(db.getString(0, "newpic_path"));
				document.setOrdertime(db.getDate(0, "ordertime"));
				document.setDoc_class(db.getString(0, "doc_class"));
				/* 装载扩展字段数据 */
				document.setExtColumn(extManager.getExtColumnInfo(0,db));
				String str = "select SRCNAME from TD_CMS_DOCSOURCE where DOCSOURCE_ID =" + db.getInt(0, "DOCSOURCE_ID")
						+ "";

				db1.executeSelect(context.getDBName(), str);
				if (db1.size() > 0) {
					document.setDocsource_name(db1.getString(0, "SRCNAME"));
				}
				String str1 = "select NAME from TD_CMS_TEMPLATE where TEMPLATE_ID=" + db.getInt(0, "DETAILTEMPLATE_ID")
						+ "";
				db1.executeSelect(context.getDBName(), str1);
				if (db1.size() > 0) {
					document.setDetailtemplate_name(db1.getString(0, "NAME"));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}

		return document;
	}

	public List getCompositeDocs(ContentContext contentcontext) throws DocumentManagerException {
		// TODO Auto-generated method stub
		return null;
	}

	public List getPagingDocs(ContentContext contentcontext) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 判断指定文档能否进行向指定状态迁移的操作，针对单个文档判断
	 * 能迁移则返回迁移id，不能则返回－1。若返回为0则表示该文档不按流程操作，即可以任意操作
	 */
	public int canTransition(int docid, int destStatus) throws DocumentManagerException {
		int b = -1;
		DBUtil db = new DBUtil();
		String sql = "select a.id as tranId from tb_cms_doc_status_trans a, "
				+ "td_cms_document b, tb_cms_flow_doc_trans c " + "where a.src_status= b.status and a.dest_status= "
				+ destStatus + " and a.id = c.transision_id and c.flow_id = b.flow_id " + "and b.document_id = "
				+ docid;
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				b = db.getInt(0, "tranId");
				return b;
			}
			sql = "select document_id from td_cms_document where flow_id is null and document_id = " + docid;
			db.executeSelect(sql);
			if (db.size() > 0)
				b = 0;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException("判断能否迁移时出现异常" + e.getMessage());
		}
		return b;
	}

	/**
	 * 判断一组文档能否同时进行状态迁移，只要有一个不能迁移，则返回false， 要么全部进行状态迁移操作，要么一个也不进行
	 * 返回一个文档迁移的映射，若能迁移则map的size应该为数组的长度，否则map的size为0
	 */
	public Map canTransition(int[] docids, int destStatus) throws DocumentManagerException {
		Map tranIdMap = new HashMap();
		if (docids != null) {
			for (int i = 0; i < docids.length; i++) {
				int canTran = this.canTransition(docids[i], destStatus);
				if (canTran == -1) {
					tranIdMap.clear();
					break;
				} else
					tranIdMap.put(new Integer(docids[i]), new Integer(canTran));
			}
		}
		return tranIdMap;
	}

	public int canTransition(int channelId, int srcStatus, int destStatus) throws DocumentManagerException {
		int b = -1;
		DBUtil db = new DBUtil();
		String sql = "select a.id as tranId from tb_cms_doc_status_trans a, "
				+ "td_cms_channel b, tb_cms_flow_doc_trans c " + "where a.src_status= " + srcStatus
				+ " and a.dest_status= " + destStatus
				+ " and a.id = c.transision_id and c.flow_id = b.workflow and b.channel_id=" + channelId;
		try {
			db.executeSelect(sql);
			if (db.size() > 0)
				b = db.getInt(0, "tranId");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException("判断能否迁移时出现异常" + e.getMessage());
		}
		return b;
	}

	/*
	 * 判断能不能在指定频道的引用指定文档, 返回为不能引用的文档名称（供提示用户用），若长度为0则可以执行引用操作
	 */
	public String canCite(int docid, int channelid, int citeType) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		String sql;
		String canntCiteDocName = "";
		try {
			if (citeType == 0) {
				sql = "select a.doc_id as docid,b.subtitle as docName from td_cms_chnl_ref_doc a,td_cms_document b where a.doc_id = b.document_id and a.chnl_id = "
						+ channelid + " and a.doc_id = " + docid;
				db.executeSelect(sql);
				if (db.size() > 0) {
					canntCiteDocName = db.getString(0, "docName");
				}
			} else {
				sql = "select a.doc_id as docid,b.display_name as chnlName from td_cms_chnl_ref_doc a,td_cms_channel b where a.doc_id = b.channel_id and a.chnl_id = "
						+ channelid + " and a.doc_id = " + docid;
				db.executeSelect(sql);
				if (db.size() > 0) {
					canntCiteDocName = db.getString(0, "chnlName");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException("数据库失败:" + e.getMessage());
		}
		return canntCiteDocName;
	}

	/**
	 * 判断能不能在指定频道批量的引用指定文档， 返回为不能引用的文档名称串，若长度为0则可以该组文档都能执行引用操作
	 */
	public String canCite(int[] docids, int channelid, int citeType) throws DocumentManagerException {
		String canntCiteDocNames = "";
		try {
			for (int i = 0; i < docids.length; i++) {
				String canntCiteDocName = canCite(docids[i], channelid, citeType);
				if (canntCiteDocName.length() != 0)
					canntCiteDocNames = canntCiteDocNames + canntCiteDocName + ";";
			}
		} catch (DocumentManagerException de) {
			de.printStackTrace();
			throw de;
		}
		return canntCiteDocNames;
	}

	/**
	 * 当前用户向指定审核人呈送文档,对于新稿，前驱任务为null,但参数需传0过来
	 */
	public void deliverDoc(int docid, int[] auditorids, int userid, int preTaskid) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		String sqlUpdate;
		String sqlInsert;
		try {
			long docTaskId = db.getNextPrimaryKey("td_cms_doc_task");

			// 更新文档状态为待审
			sqlUpdate = "update td_cms_document set status = 2 where document_id=" + docid;
			db.executeUpdate(sqlUpdate);
			// 插入待审任务
			if (preTaskid != 0)
				sqlInsert = "insert into td_cms_doc_task(task_id,document_id,submit_id,submit_time,pre_status,pre_task_id) "
						+ "values("
						+ docTaskId
						+ ","
						+ docid
						+ ","
						+ userid
						+ ","
						+ DBUtil.getDBAdapter().to_date(new Date()) + ",2," + preTaskid + ")";
			else
				sqlInsert = "insert into td_cms_doc_task(task_id,document_id,submit_id,submit_time,pre_status) "
						+ "values(" + docTaskId + "," + docid + "," + userid + ","
						+ DBUtil.getDBAdapter().to_date(new Date()) + ",2)";

			db.executeInsert(sqlInsert);

			for (int j = 0; j < auditorids.length; j++) {
				sqlInsert = "insert into td_cms_doc_task_detail(task_id,performer)" + "values(" + docTaskId + ","
						+ auditorids[j] + ")";
				db.executeInsert(sqlInsert);
			}
			// 如果preTaskid不为0,则表示呈送返工文档.返工文档呈送完后,必须更新任务表
			if (preTaskid != 0) {
				sqlUpdate = "update td_cms_doc_task_detail set complete_time ="
						+ DBUtil.getDBAdapter().to_date(new Date()) + ",after_status = 2 where valid = 1 "
						+ "and task_id =" + preTaskid + " and performer =" + userid;
				db.executeUpdate(sqlUpdate);
				// 将非当前用户的待审任务置为无效
				sqlUpdate = "update td_cms_doc_task_detail set valid = 0 where valid = 1" + " and task_id ="
						+ preTaskid + " and performer !=" + userid;
				db.executeUpdate(sqlUpdate);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException("文档呈送时数据库更新失败:" + e.getMessage());
		}
	}

	/**
	 * 当前用户向指定审核人批量呈送文档，前驱任务为统一的一个任务
	 */
	public void deliverDoc(int[] docids, int[] auditorids, int userid, int preTaskid) throws DocumentManagerException {
		try {
			for (int i = 0; i < docids.length; i++) {
				deliverDoc(docids[i], auditorids, userid, preTaskid);
			}
		} catch (DocumentManagerException de) {
			de.printStackTrace();
			throw de;
		}
	}

	/**
	 * 当前用户向指定审核人批量呈送文档
	 */
	public void deliverDoc(int[] docids, int[] auditorids, int userid, int[] preTaskid) throws DocumentManagerException {
		try {
			for (int i = 0; i < docids.length; i++) {
				deliverDoc(docids[i], auditorids, userid, preTaskid[i]);
			}
		} catch (DocumentManagerException de) {
			de.printStackTrace();
			throw de;
		}
	}

	// 撤销呈送
	public void withdrawDeliver(int docid) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		String sql;
		// 首先判断能否进行迁移，能呈送则执行更新
		try {
			// 更新文档状态
			sql = "update td_cms_document set status = 1 where document_id=" + docid;
			db.executeUpdate(sql);
			// 删除审核任务
			sql = "select task_id from td_cms_doc_task where pre_status = 2 and pre_task_id "
					+ "is null and document_id = " + docid;
			db.executeSelect(sql);
			if (db.size() > 0) {
				int taskid = db.getInt(0, "task_id");
				sql = "delete from td_cms_doc_task_detail " + "where task_id = " + taskid;
				db1.executeDelete(sql);
				sql = "delete from td_cms_doc_task where task_id = " + taskid;
				db1.executeDelete(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException("撤销呈送时数据库更新失败:" + e.getMessage());
		}
	}

	// 批量撤销呈送
	public void withdrawDeliver(int[] docids) throws DocumentManagerException {
		try {
			for (int i = 0; i < docids.length; i++) {
				withdrawDeliver(docids[i]);
			}
		} catch (DocumentManagerException de) {
			de.printStackTrace();
			throw de;
		}
	}

	/**
	 * 撤销发布
	 * 
	 * @param docid
	 * @throws DocumentManagerException
	 */
	public void withdrawPublish(int docid) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		String sql = "update td_cms_document set status = 6 where document_id = " + docid;
		try {
			db.executeUpdate(sql);
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			throw new DocumentManagerException("撤销已发文档时数据库操作失败：" + sqle.getMessage());
		}

	}

	/**
	 * 批量撤销发布
	 * 
	 * @param docids
	 * @throws DocumentManagerException
	 */
	public void withdrawPublish(int[] docids) throws DocumentManagerException {
		try {
			for (int i = 0; i < docids.length; i++) {
				this.withdrawPublish(docids[i]);
			}
		} catch (DocumentManagerException de) {
			throw de;
		}
	}

	/**
	 * 将处于待发布状态的文档退回到返工状态， 删除当前任务 同时为提交发布的人增加一条返工任务
	 * 
	 * @param docid
	 * @throws DocumentManagerException
	 */
	public void toPubWithdraw(int docid, int preTaskId, int submitterId, int[] executers)
			throws DocumentManagerException {
		try {
			completeOneTask(preTaskId, 4, submitterId, "此文档是从待发状态返工的文档！");
			updateDocumentStatus(docid, 4);
			addOneTask(docid, preTaskId, 4, submitterId, executers);
		} catch (DocumentManagerException de) {
			de.printStackTrace();
			throw de;
		}
	}

	/**
	 * 提交发布
	 */
	public void subPublishDoc(int docid, int[] publisherids, int userid, int preTaskid) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		String sql;
		try {
			// 更新文档状态为待发布
			sql = "update td_cms_document set status = 11 where document_id=" + docid;
			db.executeUpdate(sql);
			// preTaskid不为0表示已审文档的提交发布，为0则表示新稿等的提交发布
			addOneTask(docid, preTaskid, 11, userid, publisherids);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException("提交发布时出现异常" + e.getMessage());
		}
	}

	/**
	 * 批量提交发布
	 */
	public void subPublishDoc(int[] docids, int[] publisherids, int userid, int preTaskid)
			throws DocumentManagerException {
		try {
			for (int i = 0; i < docids.length; i++) {
				subPublishDoc(docids[i], publisherids, userid, preTaskid);
			}
		} catch (DocumentManagerException de) {
			de.printStackTrace();
			throw de;
		}
	}

	// 文档归档
	public void pigeonholeDoc(int docid, int userId) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		String sqlUpdate = "update td_cms_document set status = 7, guidangtime = "
				+ DBUtil.getDBAdapter().to_date(new Date()) + ",guidangman=" + userId + " where document_id=" + docid;
		try {
			db.executeUpdate(sqlUpdate);
			if (ArrangeDocExist(String.valueOf(docid))) {
				String sqlDel = "delete Td_Cms_Doc_Arrange where document_id=" + docid + "";
				db.executeDelete(sqlDel);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException("归档时出现异常" + e.getMessage());

		}
	}

	// 批量文档归档
	public void pigeonholeDoc(int[] docids, int userId) throws DocumentManagerException {
		try {
			for (int i = 0; i < docids.length; i++) {
				this.pigeonholeDoc(docids[i], userId);
			}
		} catch (DocumentManagerException e) {
			throw e;
		}
	}

	public boolean isPigeonholeManager(int userId) {
		boolean b = false;
		DBUtil db = new DBUtil();
		String sql = "select count(*) from td_sm_userrole where role_id = 11 and user_id = " + userId;
		try {
			db.executeSelect(sql);
			if (db.size() > 0)
				b = db.getInt(0, "count(*)") > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return b;
	}

	/**
	 * 将文档操作记录到数据库中
	 */
	public void recordDocOperate(int docid, int userid, String operType, int tranId, String operContent)
			throws DocumentManagerException {
		DBUtil db = new DBUtil();
		String sqlSelOperid = "select id from tb_cms_doc_oper where name = '" + operType + "'";
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			db.executeSelect(sqlSelOperid);

			long docOperLogId = db.getNextPrimaryKey("tl_cms_doc_oper_log");

			if (db.size() > 0) {
				int operId = db.getInt(0, "id");
				String sqlInsert = "insert into tl_cms_doc_oper_log(ID,user_id,doc_id,doc_oper_id,doc_trans_id,oper_time,oper_content) "
						+ "values("
						+ docOperLogId
						+ ","
						+ userid
						+ ","
						+ docid
						+ ","
						+ operId
						+ ","
						+ tranId
						+ ","
						+ DBUtil.getDBAdapter().to_date(new Date()) + ",'" + operContent + "')";
				// 考虑其他操作没有对应的tranId（如，删除操作）,回收文档恢复
				if (tranId == -1 || tranId == 0) {
					sqlInsert = "insert into tl_cms_doc_oper_log(ID,user_id,doc_id,doc_oper_id,doc_trans_id,oper_time,oper_content) "
							+ "values("
							+ docOperLogId
							+ ","
							+ userid
							+ ","
							+ docid
							+ ","
							+ operId
							+ ","
							+ null
							+ ","
							+ DBUtil.getDBAdapter().to_date(new Date()) + ",'" + operContent + "')";
				}

				db.executeInsert(sqlInsert);
				
			}
			tm.commit();
		} catch (Exception sqle) {
			
			throw new DocumentManagerException("插入历史记录时失败:" + sqle.getMessage());
		} finally {
			tm.release();
		}
	}

	/**
	 * 批量的将文档操作记录到数据库中
	 */
	public void recordDocOperate(int[] docids, int userid, String operType, Map tranIdMap, String operContent)
			throws DocumentManagerException {
		DBUtil db = new DBUtil();
		String sqlSelOperid = "select id from tb_cms_doc_oper where name = '" + operType + "'";
		TransactionManager tm = new TransactionManager();
		try {
			tm.begin();
			db.executeSelect(sqlSelOperid);
			if (db.size() > 0) {
				int operId = db.getInt(0, "id");
				for (int i = 0; i < docids.length; i++) {
					Object temp = tranIdMap.get(new Integer(docids[i]));
					int tranId = -1;
					if (temp != null)
						tranId = ((Integer) temp).intValue();
					String sqlInsert = "";

					long operLogId = db.getNextPrimaryKey("tl_cms_doc_oper_log");

					if (tranId > 0)
						sqlInsert = "insert into tl_cms_doc_oper_log(id,user_id,doc_id,doc_oper_id,doc_trans_id,oper_time,oper_content) "
								+ "values("
								+ operLogId
								+ ","
								+ userid
								+ ","
								+ docids[i]
								+ ","
								+ operId
								+ ","
								+ tranId
								+ "," + DBUtil.getDBAdapter().to_date(new Date()) + ",'" + operContent + "')";
					else
						sqlInsert = "insert into tl_cms_doc_oper_log(id,user_id,doc_id,doc_oper_id,doc_trans_id,oper_time,oper_content) "
								+ "values("
								+ operLogId
								+ ","
								+ userid
								+ ","
								+ docids[i]
								+ ","
								+ operId
								+ ","
								+ null
								+ "," + DBUtil.getDBAdapter().to_date(new Date()) + ",'" + operContent + "')";
					db.executeInsert(sqlInsert);
				}
			}
			tm.commit();
		} catch (Exception sqle) {
			sqle.printStackTrace();
			throw new DocumentManagerException("插入历史记录时失败:" + sqle.getMessage());
		} finally {
			tm.release();
		}
	}

	/**
	 * 移动指定文档到指定频道，单个文档移动
	 */
	public void moveDoc(HttpServletRequest request, int docid, int channelid) throws DocumentManagerException {
		DBUtil db = new DBUtil();

		String sql;
		try {
			// 文档删除前的原频道id以及删除前的附件地址
			Document document = this.getPartDocInfoById(docid + "");
			int srcChnlId = document.getChanel_id();
			List allAttach = this.getAllAttachmentOfDocument(request, docid);

			ChannelManager cm = new ChannelManagerImpl();

			Channel channel = cm.getChannelInfo(channelid + "");
			Channel srcchannel = cm.getChannelInfo(srcChnlId + "");

			long desSiteId = channel.getSiteId();
			long srcSiteId = srcchannel.getSiteId();

			// 1.更新文档表,并删除相关信息
			sql = "update td_cms_document set status = 1,channel_id = " + channelid + ", flow_id = "
					+ channel.getWorkflow() + ", parent_detail_tpl = 1, detailtemplate_id = "
					+ channel.getDetailTemplateId() + " where document_id = " + docid;
			System.out.println("sql" + sql);
			db.executeUpdate(sql);
			// 删除该文档的所有历史操作记录信息
			sql = "delete from tl_cms_doc_oper_log where doc_id = " + docid;
			db.executeDelete(sql);
			// 删除该任务在待审任务表中的所有任务
			this.clearTask(docid);

			// 2.复制文档内容中包含的附件文件到目标频道对应站点的目录下去

			// if(srcSiteId != desSiteId){
			SiteManager siteManager = new SiteManagerImpl();
			String desSitedir = siteManager.getSiteInfo(desSiteId + "").getSiteDir();
			String srcCSitedir = siteManager.getSiteInfo(srcSiteId + "").getSiteDir();
			String desRelativePath = channel.getChannelPath();
			String srcRelativePath = srcchannel.getChannelPath();
			String desUrl = CMSUtil.getAppRootPath() + "\\cms\\siteResource\\" + desSitedir + "\\_webprj\\"
					+ desRelativePath + "\\content_files\\";
			;
			String srcUrl = CMSUtil.getAppRootPath() + "\\cms\\siteResource\\" + srcCSitedir + "\\_webprj\\"
					+ srcRelativePath + "\\content_files\\";
			String srcFile;
			if (srcSiteId != desSiteId)// 如果是跨站点，则要把相应的主题图片和多媒体文件copy过去
			{
				String desrootpath = CMSUtil.getAppRootPath() + "\\cms\\siteResource\\" + desSitedir + "\\_template\\";
				String srcrootpath = CMSUtil.getAppRootPath() + "\\cms\\siteResource\\" + srcCSitedir + "\\_template\\";
				if (document.getPicPath() != null && !"".equals(document.getPicPath())) {
					try {
						FileUtil.fileCopy(srcrootpath + document.getPicPath().replace('/', '\\'), desrootpath
								+ document.getPicPath().replace('/', '\\'));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (document.getMediapath() != null && !"".equals(document.getMediapath())) {
					try {
						FileUtil.fileCopy(srcrootpath + document.getMediapath().replace('/', '\\'), desrootpath
								+ document.getMediapath().replace('/', '\\'));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			for (int i = 0; i < allAttach.size(); i++) {
				srcFile = (String) allAttach.get(i);
				String subdir;
				boolean flag = true;
				while (flag) {
					subdir = srcFile.substring(srcUrl.length(), srcFile.length());
					srcFile = srcFile.replace('/', '\\');
					if (subdir.indexOf("\\") == -1) { // 附件在content_files/目录下
						FileUtil.copy(srcFile, desUrl);
						flag = false;
					} else { // 附件在content_files/目录的子目录下
						srcUrl = srcUrl + subdir.substring(0, subdir.indexOf("/") + 1);
						desUrl = desUrl + subdir.substring(0, subdir.indexOf("/") + 1);
					}
				}
			}
			// }
		} catch (Exception sqle) {
			sqle.printStackTrace();
			throw new DocumentManagerException("移动文档时数据库错误" + sqle.getMessage());
		}

	}

	// 移动指定文档组到指定频道，批量文档移动
	public void moveDoc(HttpServletRequest request, int[] docids, int channelid) throws DocumentManagerException {
		try {
			for (int i = 0; i < docids.length; i++) {
				moveDoc(request, docids[i], channelid);
			}
		} catch (DocumentManagerException de) {
			de.printStackTrace();
			throw de;
		}
	}

	/**
	 * 复制制定文档到指定的频道,复制后变为新文档
	 */
	public void copyDoc(HttpServletRequest request, int docid, int channelid) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		String sql;
		String sql1;
		try {
			ChannelManager cm = new ChannelManagerImpl();

			Document document = this.getPartDocInfoById(docid + "");
			int srcChnlId = document.getChanel_id();
			Channel channel = cm.getChannelInfo(channelid + "");
			Channel srcchannel = cm.getChannelInfo(srcChnlId + "");

			long desSiteId = channel.getSiteId();
			long srcSiteId = srcchannel.getSiteId();

			// 修改主键生成方式
			// sql = "select max(document_id) as maxid from td_cms_document";
			// db.executeSelect(sql);
			int newid;
			// if (db.size() > 0) {
			// 1.首先复制文档到目标频道
			// newid = db.getInt(0, "maxid") + 1;

			newid = (int) db.getNextPrimaryKey("td_cms_document");

			sql = "insert into td_cms_document(document_id,title,"
					+ "subtitle,author,content,channel_id,status,keywords,"
					+ "docabstract,doctype,docwtime,titlecolor,createtime,"
					+ "createuser,docsource_id,detailtemplate_id,linktarget,"
					+ "flow_id,doc_level,doc_kind,parent_detail_tpl,"
					+ "isdeleted,version,publishfilename,pic_path,mediapath,secondtitle,isnew,newpic_path,ordertime,seq,ext_wh,ext_index,ext_org,ext_class,count) "
					+ "(select "
					+ newid
					+ ",title,subtitle,author,content,"
					+ channelid
					+ ",1,"
					+ "keywords,docabstract,doctype,docwtime,"
					+ "titlecolor,createtime,createuser,docsource_id,"
					+ channel.getDetailTemplateId()
					+ ",linktarget,flow_id,doc_level,"
					+ "doc_kind,1,0,1,publishfilename,pic_path,mediapath,secondtitle,isnew,newpic_path,ordertime,seq,ext_wh,ext_index,ext_org,ext_class,count "
					+ "from td_cms_document " + "where document_id=" + docid + ")";
			db.executeInsert(sql);
			// 2.复制文档的相关附件关系
			// 若为聚合文档，则必须复制聚合的子文档
			sql = "select * from td_cms_doc_aggregation where aggr_doc_id = " + docid;
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					int seq = i + 1;
					sql1 = "insert into td_cms_doc_aggregation(aggr_doc_id,seq,id_by_aggr,title,type) " + "(select "
							+ newid + ",seq,id_by_aggr,title,type " + "from td_cms_doc_aggregation where aggr_doc_id="
							+ docid + " and seq=" + seq + ")";
					db1.executeInsert(sql1);
				}
			}
			// 若有附件，则必须复制附件
			sql = "select * from td_cms_doc_attach where document_id = " + docid;
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					int tempID = db.getInt(i, "id");
					// sql1 = "select max(id) as maxid from td_cms_doc_attach";
					// db1.executeSelect(sql1);
					// int tempNewId = db1.getInt(0, "maxid") + 1;

					int tempNewId = (int) db1.getNextPrimaryKey("td_cms_doc_attach");

					sql1 = "insert into td_cms_doc_attach(id,document_id,url,type,description,original_filename,valid) "
							+ "(select "
							+ tempNewId
							+ ","
							+ newid
							+ ",url,type,description,original_filename,valid "
							+ "from td_cms_doc_attach where id=" + tempID + ")";
					db1.executeInsert(sql1);
				}
			}
			// 若设置了文档分发方式，则复制文档分发方式
			sql = "select * from td_cms_doc_dist_manner where document_id = " + docid;
			db.executeSelect(sql);
			if (db.size() > 0) {
				sql1 = "inset into td_cms_doc_dist_manner(document_id,dist_manner_id) " + "(select " + newid
						+ ",dist_manner_id " + "from td_cms_doc_dist_manner where document_id = " + docid + " )";
				db1.executeInsert(sql1);
			}
			// 若存在相关文档，则复制相相关文档关系
			sql = "select * from td_cms_doc_related where doc_id = " + docid;
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					int tempRelatedDocId = db.getInt(i, "related_doc_id");
					sql1 = "insert into td_cms_doc_related(doc_id,related_doc_id,op_user_id,op_time) " + "(select "
							+ newid + ",related_doc_id,op_user_id,op_time " + "from td_cms_doc_related where doc_id="
							+ docid + " and related_doc_id=" + tempRelatedDocId + ")";
					db1.executeInsert(sql1);
				}
			}
			// 若存在矿展字段，则复制扩展字段
			sql = "select * from td_cms_extfieldvalue where document_id = " + docid;
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					int tempFieldId = db.getInt(i, "field_id");
					sql1 = "insert into td_cms_extfieldvalue(field_id,document_id,fieldvalue,numbervalue,clobvalue,datevalue) "
							+ "(select field_id,"
							+ newid
							+ ",fieldvalue,numbervalue,clobvalue,datevalue "
							+ "from td_cms_extfieldvalue where document_id="
							+ docid
							+ " and field_id="
							+ tempFieldId
							+ ")";
					db1.executeInsert(sql1);
				}
			}
			// 3.复制文档内容中包含的附件文件到目标频道对应站点的目录下去

			if (srcChnlId != channelid) {
				SiteManager siteManager = new SiteManagerImpl();
				List allAttach = this.getAllAttachmentOfDocument(request, docid);
				String desSitedir = siteManager.getSiteInfo(desSiteId + "").getSiteDir();
				String srcCSitedir = siteManager.getSiteInfo(srcSiteId + "").getSiteDir();
				String desRelativePath = channel.getChannelPath();
				String srcRelativePath = srcchannel.getChannelPath();
				String desUrl = CMSUtil.getAppRootPath() + "\\cms\\siteResource\\" + desSitedir + "\\_webprj\\"
						+ desRelativePath + "\\content_files\\";
				;
				String srcUrl = CMSUtil.getAppRootPath() + "\\cms\\siteResource\\" + srcCSitedir + "\\_webprj\\"
						+ srcRelativePath + "\\content_files\\";
				;
				String srcFile;
				for (int i = 0; i < allAttach.size(); i++) {
					srcFile = (String) allAttach.get(i);
					srcFile = srcFile.replace('/', '\\');
					this.copyFile(srcUrl, desUrl, srcFile);
				}

				// 4.复制文档视频文件和图片
				if (srcSiteId != desSiteId)// 如果是跨站点，则要把相应的主题图片和多媒体文件copy过去
				{
					String desrootpath = CMSUtil.getAppRootPath() + "\\cms\\siteResource\\" + desSitedir
							+ "\\_template\\";
					String srcrootpath = CMSUtil.getAppRootPath() + "\\cms\\siteResource\\" + srcCSitedir
							+ "\\_template\\";
					if (document.getPicPath() != null && !"".equals(document.getPicPath())) {
						try {
							FileUtil.fileCopy(srcrootpath + document.getPicPath().replace('/', '\\'), desrootpath
									+ document.getPicPath().replace('/', '\\'));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (document.getMediapath() != null && !"".equals(document.getMediapath())) {
						try {
							FileUtil.fileCopy(srcrootpath + document.getMediapath().replace('/', '\\'), desrootpath
									+ document.getMediapath().replace('/', '\\'));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				// sql = "select * from td_cms_document where document_id =
				// " + docid;
				// db.executeSelect(sql);
				// if(db.size()>0){
				// String mediaPath = db.getString(0,"mediapath");
				// String picPath = db.getString(0,"pic_path");
				// if(mediaPath != null && mediaPath.length()>0){ //复制文档视频文件
				// desUrl=CMSUtil.getAppRootPath() + "\\cms\\siteResource\\"
				// + desSitedir + "\\_template\\";
				// srcUrl=CMSUtil.getAppRootPath() + "\\cms\\siteResource\\"
				// + srcCSitedir + "\\_template\\";
				// if(mediaPath.startsWith("\\"))
				// mediaPath = mediaPath.substring(1,mediaPath.length());
				// if(mediaPath.endsWith("\\"))
				// mediaPath = mediaPath.substring(0,mediaPath.length()-1);
				// srcFile = srcUrl + mediaPath;
				// srcFile = srcFile.replace('/','\\');
				// //FileUtil.copy(srcFile,desUrl);
				// this.copyFile(srcUrl,desUrl,srcFile);
				// }
				// if(picPath != null && picPath.length()>0){ //复制文档图片
				// desUrl=CMSUtil.getAppRootPath() + "\\cms\\siteResource\\"
				// + desSitedir + "\\_template\\";
				// srcUrl=CMSUtil.getAppRootPath() + "\\cms\\siteResource\\"
				// + srcCSitedir + "\\_template\\";
				// if(picPath.startsWith("\\"))
				// picPath = picPath.substring(1,picPath.length());
				// if(picPath.endsWith("\\"))
				// picPath = picPath.substring(0,picPath.length()-1);
				// srcFile = srcUrl + picPath;
				// srcFile = srcFile.replace('/','\\');
				// //FileUtil.copy(srcFile,desUrl);
				// this.copyFile(srcUrl,desUrl,srcFile);
				// }
				// }
			}
			// }
		} catch (Exception se) {
			se.printStackTrace();
			throw new DocumentManagerException("复制文档时数据库操作失败" + se.getMessage());
		}
	}

	/**
	 * 复制制定文档到指定的频道, 复制后变为新文档, 并且 文档流程更新为 频道的流程 可指定文档状态 status add by ge.tao
	 * 2007-09-18
	 */
	public int copyDoc(HttpServletRequest request, int docid, int channelid, int status)
			throws DocumentManagerException {
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		String sql = "";
		String sql1 = "";
		int newid = 0;
		try {
			ChannelManager cm = new ChannelManagerImpl();

			Document document = this.getPartDocInfoById(docid + "");
			int srcChnlId = document.getChanel_id();
			Channel channel = cm.getChannelInfo(channelid + "");
			int newFlowId = channel.getWorkflow();
			Channel srcchannel = cm.getChannelInfo(srcChnlId + "");

			long desSiteId = channel.getSiteId();
			long srcSiteId = srcchannel.getSiteId();

			// 修改主键生成的方式
			// sql = "select max(document_id) as maxid from td_cms_document";
			// db.executeSelect(sql);
			// if (db.size() > 0) {
			// 1.首先复制文档到目标频道
			// newid = db.getInt(0, "maxid") + 1;
			newid = (int) db.getNextPrimaryKey("td_cms_document");

			sql = "insert into td_cms_document(document_id,title,"
					+ "subtitle,author,content,channel_id,status,keywords,"
					+ "docabstract,doctype,docwtime,titlecolor,createtime,"
					+ "createuser,docsource_id,detailtemplate_id,linktarget,"
					+ "flow_id,doc_level,doc_kind,parent_detail_tpl,"
					+ "isdeleted,version,publishfilename,pic_path,mediapath,secondtitle,isnew,newpic_path,ordertime,seq,ext_wh,ext_index,ext_org,ext_class,count) "
					+ "(select "
					+ newid
					+ ",title,subtitle,author,content,"
					+ channelid
					+ ","
					+ status
					+ ","
					+ "keywords,docabstract,doctype,docwtime,"
					+ "titlecolor,createtime,createuser,docsource_id,"
					+ channel.getDetailTemplateId()
					+ ",linktarget,"
					+ newFlowId
					+ ",doc_level,"
					+ "doc_kind,1,0,1,publishfilename,pic_path,mediapath,secondtitle,isnew,newpic_path,ordertime,seq,ext_wh,ext_index,ext_org,ext_class,count "
					+ "from td_cms_document " + "where document_id=" + docid + ")";
			db.executeInsert(sql);
			// 2.复制文档的相关附件关系
			// 若为聚合文档，则必须复制聚合的子文档
			sql = "select * from td_cms_doc_aggregation where aggr_doc_id = " + docid;
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					int seq = i + 1;
					sql1 = "insert into td_cms_doc_aggregation(aggr_doc_id,seq,id_by_aggr,title,type) " + "(select "
							+ newid + ",seq,id_by_aggr,title,type " + "from td_cms_doc_aggregation where aggr_doc_id="
							+ docid + " and seq=" + seq + ")";
					db1.executeInsert(sql1);
				}
			}
			// 若有附件，则必须复制附件
			sql = "select * from td_cms_doc_attach where document_id = " + docid;
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					int tempID = db.getInt(i, "id");
					// sql1 = "select max(id) as maxid from td_cms_doc_attach";
					// db1.executeSelect(sql1);
					// int tempNewId = db1.getInt(0, "maxid") + 1;
					int tempNewId = (int) db1.getNextPrimaryKey("td_cms_doc_attach");

					sql1 = "insert into td_cms_doc_attach(id,document_id,url,type,description,original_filename,valid) "
							+ "(select "
							+ tempNewId
							+ ","
							+ newid
							+ ",url,type,description,original_filename,valid "
							+ "from td_cms_doc_attach where id=" + tempID + ")";
					db1.executeInsert(sql1);
				}
			}
			// 若设置了文档分发方式，则复制文档分发方式
			sql = "select * from td_cms_doc_dist_manner where document_id = " + docid;
			db.executeSelect(sql);
			if (db.size() > 0) {
				sql1 = "inset into td_cms_doc_dist_manner(document_id,dist_manner_id) " + "(select " + newid
						+ ",dist_manner_id " + "from td_cms_doc_dist_manner where document_id = " + docid + " )";
				db1.executeInsert(sql1);
			}
			// 若存在相关文档，则复制相相关文档关系
			sql = "select * from td_cms_doc_related where doc_id = " + docid;
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					int tempRelatedDocId = db.getInt(i, "related_doc_id");
					sql1 = "insert into td_cms_doc_related(doc_id,related_doc_id,op_user_id,op_time) " + "(select "
							+ newid + ",related_doc_id,op_user_id,op_time " + "from td_cms_doc_related where doc_id="
							+ docid + " and related_doc_id=" + tempRelatedDocId + ")";
					db1.executeInsert(sql1);
				}
			}
			// 若存在矿展字段，则复制扩展字段
			sql = "select * from td_cms_extfieldvalue where document_id = " + docid;
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					int tempFieldId = db.getInt(i, "field_id");
					sql1 = "insert into td_cms_extfieldvalue(field_id,document_id,fieldvalue,numbervalue,clobvalue,datevalue) "
							+ "(select field_id,"
							+ newid
							+ ",fieldvalue,numbervalue,clobvalue,datevalue "
							+ "from td_cms_extfieldvalue where document_id="
							+ docid
							+ " and field_id="
							+ tempFieldId
							+ ")";
					db1.executeInsert(sql1);
				}
			}
			// 3.复制文档内容中包含的附件文件到目标频道对应站点的目录下去

			if (srcChnlId != channelid) {
				SiteManager siteManager = new SiteManagerImpl();
				List allAttach = this.getAllAttachmentOfDocument(request, docid);
				String desSitedir = siteManager.getSiteInfo(desSiteId + "").getSiteDir();
				String srcCSitedir = siteManager.getSiteInfo(srcSiteId + "").getSiteDir();
				String desRelativePath = channel.getChannelPath();
				String srcRelativePath = srcchannel.getChannelPath();
				String desUrl = CMSUtil.getAppRootPath() + "\\cms\\siteResource\\" + desSitedir + "\\_webprj\\"
						+ desRelativePath + "\\content_files\\";
				;
				String srcUrl = CMSUtil.getAppRootPath() + "\\cms\\siteResource\\" + srcCSitedir + "\\_webprj\\"
						+ srcRelativePath + "\\content_files\\";
				;
				String srcFile;
				for (int i = 0; i < allAttach.size(); i++) {
					srcFile = (String) allAttach.get(i);
					srcFile = srcFile.replace('/', '\\');
					this.copyFile(srcUrl, desUrl, srcFile);
				}

				// 4.复制文档视频文件和图片
				if (srcSiteId != desSiteId)// 如果是跨站点，则要把相应的主题图片和多媒体文件copy过去
				{
					String desrootpath = CMSUtil.getAppRootPath() + "\\cms\\siteResource\\" + desSitedir
							+ "\\_template\\";
					String srcrootpath = CMSUtil.getAppRootPath() + "\\cms\\siteResource\\" + srcCSitedir
							+ "\\_template\\";
					if (document.getPicPath() != null && !"".equals(document.getPicPath())) {
						try {
							FileUtil.fileCopy(srcrootpath + document.getPicPath().replace('/', '\\'), desrootpath
									+ document.getPicPath().replace('/', '\\'));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (document.getMediapath() != null && !"".equals(document.getMediapath())) {
						try {
							FileUtil.fileCopy(srcrootpath + document.getMediapath().replace('/', '\\'), desrootpath
									+ document.getMediapath().replace('/', '\\'));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				// sql = "select * from td_cms_document where document_id =
				// " + docid;
				// db.executeSelect(sql);
				// if(db.size()>0){
				// String mediaPath = db.getString(0,"mediapath");
				// String picPath = db.getString(0,"pic_path");
				// if(mediaPath != null && mediaPath.length()>0){ //复制文档视频文件
				// desUrl=CMSUtil.getAppRootPath() + "\\cms\\siteResource\\"
				// + desSitedir + "\\_template\\";
				// srcUrl=CMSUtil.getAppRootPath() + "\\cms\\siteResource\\"
				// + srcCSitedir + "\\_template\\";
				// if(mediaPath.startsWith("\\"))
				// mediaPath = mediaPath.substring(1,mediaPath.length());
				// if(mediaPath.endsWith("\\"))
				// mediaPath = mediaPath.substring(0,mediaPath.length()-1);
				// srcFile = srcUrl + mediaPath;
				// srcFile = srcFile.replace('/','\\');
				// //FileUtil.copy(srcFile,desUrl);
				// this.copyFile(srcUrl,desUrl,srcFile);
				// }
				// if(picPath != null && picPath.length()>0){ //复制文档图片
				// desUrl=CMSUtil.getAppRootPath() + "\\cms\\siteResource\\"
				// + desSitedir + "\\_template\\";
				// srcUrl=CMSUtil.getAppRootPath() + "\\cms\\siteResource\\"
				// + srcCSitedir + "\\_template\\";
				// if(picPath.startsWith("\\"))
				// picPath = picPath.substring(1,picPath.length());
				// if(picPath.endsWith("\\"))
				// picPath = picPath.substring(0,picPath.length()-1);
				// srcFile = srcUrl + picPath;
				// srcFile = srcFile.replace('/','\\');
				// //FileUtil.copy(srcFile,desUrl);
				// this.copyFile(srcUrl,desUrl,srcFile);
				// }
				// }
			}
			// }
		} catch (Exception se) {
			se.printStackTrace();
			throw new DocumentManagerException("复制文档时数据库操作失败" + se.getMessage());
		}
		return newid;
	}

	/**
	 * 复制制定文档到指定的频道, 复制后变为新文档, 并且 文档流程更新为 频道的流程 可指定文档状态 status add by ge.tao
	 * 2007-09-18
	 */
	public int copyDoc(HttpServletRequest request, int docid, int channelid, int siteid, int status)
			throws DocumentManagerException {
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		String sql = "";
		String sql1 = "";
		int newid = 0;
		try {
			ChannelManager cm = new ChannelManagerImpl();

			Document document = this.getPartDocInfoById(docid + "");
			int srcChnlId = document.getChanel_id();
			Channel channel = cm.getChannelInfo(channelid + "");
			int newFlowId = channel.getWorkflow();
			Channel srcchannel = cm.getChannelInfo(srcChnlId + "");

			long desSiteId = channel.getSiteId();
			long srcSiteId = srcchannel.getSiteId();

			// sql = "select max(document_id) as maxid from td_cms_document";
			// db.executeSelect(sql);
			// if (db.size() > 0) {
			// 1.首先复制文档到目标频道
			// newid = db.getInt(0, "maxid") + 1;

			newid = (int) db.getNextPrimaryKey("td_cms_document");

			sql = "insert into td_cms_document(document_id,title,"
					+ "subtitle,author,content,channel_id,status,keywords,"
					+ "docabstract,doctype,docwtime,titlecolor,createtime,"
					+ "createuser,docsource_id,detailtemplate_id,linktarget,"
					+ "flow_id,doc_level,doc_kind,parent_detail_tpl,"
					+ "isdeleted,version,publishfilename,pic_path,mediapath,secondtitle,isnew,newpic_path,ordertime,seq,ext_wh,ext_index,ext_org,ext_class,count) "
					+ "(select "
					+ newid
					+ ",title,subtitle,author,content,"
					+ channelid
					+ ","
					+ status
					+ ","
					+ "keywords,docabstract,doctype,docwtime,"
					+ "titlecolor,createtime,createuser,docsource_id,"
					+ channel.getDetailTemplateId()
					+ ",linktarget,"
					+ newFlowId
					+ ",doc_level,"
					+ "doc_kind,1,0,1,publishfilename,pic_path,mediapath,secondtitle,isnew,newpic_path,ordertime,seq,ext_wh,ext_index,ext_org,ext_class,count "
					+ "from td_cms_document " + "where document_id=" + docid + ")";
			db.executeInsert(sql);
			// 2.复制文档的相关附件关系
			// 若为聚合文档，则必须复制聚合的子文档
			sql = "select * from td_cms_doc_aggregation where aggr_doc_id = " + docid;
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					int seq = i + 1;
					sql1 = "insert into td_cms_doc_aggregation(aggr_doc_id,seq,id_by_aggr,title,type) " + "(select "
							+ newid + ",seq,id_by_aggr,title,type " + "from td_cms_doc_aggregation where aggr_doc_id="
							+ docid + " and seq=" + seq + ")";
					db1.executeInsert(sql1);
				}
			}
			// 若有附件，则必须复制附件
			sql = "select * from td_cms_doc_attach where document_id = " + docid;
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					int tempID = db.getInt(i, "id");
					// sql1 = "select max(id) as maxid from td_cms_doc_attach";
					// db1.executeSelect(sql1);
					// int tempNewId = db1.getInt(0, "maxid") + 1;
					int tempNewId = (int) db1.getNextPrimaryKey("td_cms_doc_attach");

					sql1 = "insert into td_cms_doc_attach(id,document_id,url,type,description,original_filename,valid) "
							+ "(select "
							+ tempNewId
							+ ","
							+ newid
							+ ",url,type,description,original_filename,valid "
							+ "from td_cms_doc_attach where id=" + tempID + ")";
					db1.executeInsert(sql1);
				}
			}
			// 若设置了文档分发方式，则复制文档分发方式
			sql = "select * from td_cms_doc_dist_manner where document_id = " + docid;
			db.executeSelect(sql);
			if (db.size() > 0) {
				sql1 = "inset into td_cms_doc_dist_manner(document_id,dist_manner_id) " + "(select " + newid
						+ ",dist_manner_id " + "from td_cms_doc_dist_manner where document_id = " + docid + " )";
				db1.executeInsert(sql1);
			}
			// 若存在相关文档，则复制相相关文档关系
			sql = "select * from td_cms_doc_related where doc_id = " + docid;
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					int tempRelatedDocId = db.getInt(i, "related_doc_id");
					sql1 = "insert into td_cms_doc_related(doc_id,related_doc_id,op_user_id,op_time) " + "(select "
							+ newid + ",related_doc_id,op_user_id,op_time " + "from td_cms_doc_related where doc_id="
							+ docid + " and related_doc_id=" + tempRelatedDocId + ")";
					db1.executeInsert(sql1);
				}
			}
			// 若存在矿展字段，则复制扩展字段
			sql = "select * from td_cms_extfieldvalue where document_id = " + docid;
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					int tempFieldId = db.getInt(i, "field_id");
					sql1 = "insert into td_cms_extfieldvalue(field_id,document_id,fieldvalue,numbervalue,clobvalue,datevalue) "
							+ "(select field_id,"
							+ newid
							+ ",fieldvalue,numbervalue,clobvalue,datevalue "
							+ "from td_cms_extfieldvalue where document_id="
							+ docid
							+ " and field_id="
							+ tempFieldId
							+ ")";
					db1.executeInsert(sql1);
				}
			}
			// 3.复制文档内容中包含的附件文件到目标频道对应站点的目录下去

			if (srcChnlId != channelid) {
				SiteManager siteManager = new SiteManagerImpl();
				List allAttach = this.getAllAttachmentOfDocument(request, docid);
				String desSitedir = siteManager.getSiteInfo(desSiteId + "").getSiteDir();
				String srcCSitedir = siteManager.getSiteInfo(srcSiteId + "").getSiteDir();
				String desRelativePath = channel.getChannelPath();
				String srcRelativePath = srcchannel.getChannelPath();
				String desUrl = CMSUtil.getAppRootPath() + "\\cms\\siteResource\\" + desSitedir + "\\_webprj\\"
						+ desRelativePath + "\\content_files\\";
				;
				String srcUrl = CMSUtil.getAppRootPath() + "\\cms\\siteResource\\" + srcCSitedir + "\\_webprj\\"
						+ srcRelativePath + "\\content_files\\";
				;
				String srcFile;
				for (int i = 0; i < allAttach.size(); i++) {
					srcFile = (String) allAttach.get(i);
					srcFile = srcFile.replace('/', '\\');
					this.copyFile(srcUrl, desUrl, srcFile);
				}

				// 4.复制文档视频文件和图片
				if (srcSiteId != desSiteId)// 如果是跨站点，则要把相应的主题图片和多媒体文件copy过去
				{
					String desrootpath = CMSUtil.getAppRootPath() + "\\cms\\siteResource\\" + desSitedir
							+ "\\_template\\";
					String srcrootpath = CMSUtil.getAppRootPath() + "\\cms\\siteResource\\" + srcCSitedir
							+ "\\_template\\";
					if (document.getPicPath() != null && !"".equals(document.getPicPath())) {
						try {
							FileUtil.fileCopy(srcrootpath + document.getPicPath().replace('/', '\\'), desrootpath
									+ document.getPicPath().replace('/', '\\'));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (document.getMediapath() != null && !"".equals(document.getMediapath())) {
						try {
							FileUtil.fileCopy(srcrootpath + document.getMediapath().replace('/', '\\'), desrootpath
									+ document.getMediapath().replace('/', '\\'));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				// sql = "select * from td_cms_document where document_id =
				// " + docid;
				// db.executeSelect(sql);
				// if(db.size()>0){
				// String mediaPath = db.getString(0,"mediapath");
				// String picPath = db.getString(0,"pic_path");
				// if(mediaPath != null && mediaPath.length()>0){ //复制文档视频文件
				// desUrl=CMSUtil.getAppRootPath() + "\\cms\\siteResource\\"
				// + desSitedir + "\\_template\\";
				// srcUrl=CMSUtil.getAppRootPath() + "\\cms\\siteResource\\"
				// + srcCSitedir + "\\_template\\";
				// if(mediaPath.startsWith("\\"))
				// mediaPath = mediaPath.substring(1,mediaPath.length());
				// if(mediaPath.endsWith("\\"))
				// mediaPath = mediaPath.substring(0,mediaPath.length()-1);
				// srcFile = srcUrl + mediaPath;
				// srcFile = srcFile.replace('/','\\');
				// //FileUtil.copy(srcFile,desUrl);
				// this.copyFile(srcUrl,desUrl,srcFile);
				// }
				// if(picPath != null && picPath.length()>0){ //复制文档图片
				// desUrl=CMSUtil.getAppRootPath() + "\\cms\\siteResource\\"
				// + desSitedir + "\\_template\\";
				// srcUrl=CMSUtil.getAppRootPath() + "\\cms\\siteResource\\"
				// + srcCSitedir + "\\_template\\";
				// if(picPath.startsWith("\\"))
				// picPath = picPath.substring(1,picPath.length());
				// if(picPath.endsWith("\\"))
				// picPath = picPath.substring(0,picPath.length()-1);
				// srcFile = srcUrl + picPath;
				// srcFile = srcFile.replace('/','\\');
				// //FileUtil.copy(srcFile,desUrl);
				// this.copyFile(srcUrl,desUrl,srcFile);
				// }
				// }
			}
			// }
		} catch (Exception se) {
			se.printStackTrace();
			throw new DocumentManagerException("复制文档时数据库操作失败" + se.getMessage());
		}
		return newid;
	}

	private void copyFile(String srcUrl, String desUrl, String srcFile) throws Exception {
		String subdir;
		boolean flag = true;
		while (flag) {
			subdir = srcFile.substring(srcUrl.length(), srcFile.length());
			if (subdir.indexOf("\\") == -1) { // 附件在content_files/目录下
				FileUtil.copy(srcFile, desUrl);
				flag = false;
			} else { // 附件在content_files/目录的子目录下
				srcUrl = srcUrl + subdir.substring(0, subdir.indexOf("\\") + 1);
				desUrl = desUrl + subdir.substring(0, subdir.indexOf("\\") + 1);
			}
		}
	}

	// 批量复制文档组到指定文档
	public void copyDoc(HttpServletRequest request, int[] docids, int channelid) throws DocumentManagerException {
		try {
			for (int i = 0; i < docids.length; i++) {
				copyDoc(request, docids[i], channelid);
			}
		} catch (DocumentManagerException de) {
			de.printStackTrace();
			throw de;
		}
	}

	/**
	 * 批量复制文档组到多个频道下 可指定文档状态status add by ge.tao 2007-09-17 String[] channelid
	 * {200:1,201:2,202,3....} 返回 新增文档的ID 的数组
	 */
	public int[] copyDoc(HttpServletRequest request, int[] docids, String[] channelid, int status)
			throws DocumentManagerException {
		int[] newDocIds = new int[docids.length];
		try {
			for (int i = 0; i < channelid.length; i++) {
				String[] mixids = channelid[i].split(":");
				for (int j = 0; j < docids.length; j++) {
					// 指定状态
					newDocIds[j] = copyDoc(request, docids[j], Integer.parseInt(mixids[1]), status);
				}
			}
		} catch (DocumentManagerException de) {
			de.printStackTrace();
			throw de;
		}
		return newDocIds;
	}

	/**
	 * 批量复制文档组到多个频道下 可指定文档状态status
	 * 
	 * @param status
	 *            取值范围为 DocumentStatus.NEW DocumentStatus.AUDITING
	 *            DocumentStatus.PREPUBLISH DocumentStatus.PUBLISHED
	 * @param userId
	 *            操作人userid add by ge.tao 2007-09-17 String[][]
	 *            {siteid,channelid,docid} {200:1,201:2,202,3....} 返回 新增文档的ID
	 *            的数组
	 */
	public int[][] copyDocs(HttpServletRequest request, int[] docids, int[] channelids, int[] siteids, int status,
			int userId) throws DocumentManagerException {
		ChannelManager cmi = new ChannelManagerImpl();
		int[][] newDocIds = new int[docids.length * channelids.length][];
		String _status = status + "";
		int k = 0;
		try {
			for (int i = 0; i < channelids.length; i++) {
				for (int j = 0; j < docids.length; j++) {
					// 指定状态
					try {
						int newdocid = copyDoc(request, docids[j], channelids[i], siteids[i], status);// 新文档ID
						newDocIds[k] = new int[] { siteids[i], channelids[i], newdocid };
						if (_status.equals(DocumentStatus.NEW.getStatus())) {
							// 无任何操作
						} else if (_status.equals(DocumentStatus.UNAUDIT.getStatus())) {
							// 分派审核任务
							List auditer = cmi.getAuditorList(channelids[i] + ""); // 频道下具有审核权限的用户列表
							int[] auditerid = new int[auditer.size()];
							for (int u = 0; u < auditer.size(); u++) {
								User user = (User) auditer.get(u);
								auditerid[u] = user.getUserId().intValue();
							}
							// 做呈送处理
							deliverDoc(newdocid, auditerid, userId, 0); // 新稿,最后一个参数为0
						} else if (_status.equals(DocumentStatus.PREPUBLISH.getStatus())) {
							// 分派发布任务
							List publisher = cmi.getPublisherList(channelids[i] + "");// 频道下具有发布权限的用户列表
							int[] publisherid = new int[publisher.size()];
							for (int u = 0; u < publisher.size(); u++) {
								User user = (User) publisher.get(u);
								publisherid[u] = user.getUserId().intValue();
							}
							// 做提交发布处理
							subPublishDoc(newdocid, publisherid, userId, 0);// 新稿,最后一个参数为0
						} else if (_status.equals(DocumentStatus.PUBLISHED.getStatus())) {
							// 返回新文档ID数组
						}
						k++;
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		} catch (Exception de) {
			de.printStackTrace();
		}
		return newDocIds;
	}

	// 在指定频道引用指定文档
	public void citeDoc(int docid, int channelid, int userid, int type, int siteid) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		String sql;
		try {
			sql = "insert into td_cms_chnl_ref_doc(chnl_id,doc_id,op_user_id,op_time,citetype,site_id) values("
					+ channelid + "," + docid + "," + userid + "," + DBUtil.getDBAdapter().to_date(new Date()) + ","
					+ type + "," + siteid + ")";
			db.executeInsert(sql);
		} catch (SQLException se) {
			se.printStackTrace();
			throw new DocumentManagerException("引用文档时数据库操作失败" + se.getMessage());
		}
	}

	// 在指定频道批量的引用指定文档
	public void citeDoc(int[] docids, int channelid, int userid, int type, int siteid) throws DocumentManagerException {
		try {
			for (int i = 0; i < docids.length; i++) {
				citeDoc(docids[i], channelid, userid, type, siteid);
			}
		} catch (DocumentManagerException de) {
			de.printStackTrace();
			throw de;
		}
	}

	// 撤销指定频道对指定文档的引用
	public void withdrawCite(int docid, int channelid) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		String sql;
		try {
			sql = "delete from td_cms_chnl_ref_doc where chnl_id = " + channelid + " and doc_id = " + docid;
			db.executeDelete(sql);
		} catch (SQLException se) {
			se.printStackTrace();
			throw new DocumentManagerException("引用文档时数据库操作失败" + se.getMessage());
		}
	}

	// 批量撤销指定频道对指定文档的引用
	public void withdrawCite(int[] docids, int channelid) throws DocumentManagerException {
		try {
			for (int i = 0; i < docids.length; i++) {
				withdrawCite(docids[i], channelid);
			}
		} catch (DocumentManagerException de) {
			de.printStackTrace();
			throw de;
		}
	}

	// 指定用户审核指定的文档,用于标志审核通过还是不通过，1表通过，0表不通过
	public void audit(int docid, int taskid, int auditorid, String comment, int flag) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		String sql;
		try {
			// 更新文档状态为已审
			if (flag == 1) {
				sql = "update td_cms_document set status = 3, audittime = " + DBUtil.getDBAdapter().to_date(new Date())
						+ " where document_id =" + docid;
				db.executeUpdate(sql);
				// 将当前用户的待审任务更新，填写任务完成时间和审核意见
				sql = "update td_cms_doc_task_detail set complete_time =" + DBUtil.getDBAdapter().to_date(new Date())
						+ ",after_status = 3,opinion='" + comment + "' where valid = 1 " + "and task_id =" + taskid
						+ " and performer =" + auditorid;
				db.executeUpdate(sql);
				// 将非当前用户的待审任务置为无效
				sql = "update td_cms_doc_task_detail set valid = 0 where valid = 1" + " and task_id =" + taskid
						+ " and performer !=" + auditorid;
				db.executeUpdate(sql);
			} else if (flag == 0) {
				sql = "update td_cms_document set status = 4, audittime = " + DBUtil.getDBAdapter().to_date(new Date())
						+ " where document_id =" + docid;
				db.executeUpdate(sql);
				// 将当前用户的待审任务更新，填写任务完成时间和审核意见
				sql = "update td_cms_doc_task_detail set complete_time =" + DBUtil.getDBAdapter().to_date(new Date())
						+ ",after_status = 4,opinion='" + comment + "' where valid = 1 " + "and task_id =" + taskid
						+ " and performer =" + auditorid;
				db.executeUpdate(sql);
				// 将非当前用户的待审任务置为无效
				sql = "update td_cms_doc_task_detail set valid = 0 where valid = 1" + " and task_id =" + taskid
						+ " and performer !=" + auditorid;
				db.executeUpdate(sql);
				// 如果不同意，则增加一个返工的任务

				int temptid = (int) db.getNextPrimaryKey("td_cms_doc_task");

				sql = "insert into td_cms_doc_task(task_id,pre_task_id,document_id,pre_status,submit_id,submit_time) values("
						+ temptid
						+ ","
						+ taskid
						+ ","
						+ docid
						+ ",4,"
						+ auditorid
						+ ","
						+ DBUtil.getDBAdapter().to_date(new Date()) + ")";
				db.executeInsert(sql);
				// int temptid = ((Long) o).intValue(); // 得到新增加的任务的id
				// 得到原来的待审任务的提交人（也即呈送人），返工任务是原传送人的任务
				sql = "select submit_id from td_cms_doc_task where task_id = " + taskid;
				db1.executeSelect(sql);
				int tempOid;
				if (db1.size() > 0) {
					tempOid = db1.getInt(0, "submit_id");
					// 将返工的任务作为原呈送人的任务插入任务明细表中
					sql = "insert into td_cms_doc_task_detail(task_id,performer,valid) " + "values(" + temptid + ","
							+ tempOid + ",1)";
					db.executeInsert(sql);
				}
			}
		} catch (Exception se) {
			se.printStackTrace();
			throw new DocumentManagerException("文档审核通过时数据库操作失败" + se.getMessage());
		}
	}

	// 指定用户审核批量文档,用于标志审核通过还是不通过，1表通过，0表不通过
	public void audit(int[] docids, int[] taskids, int auditorid, String comment, int flag)
			throws DocumentManagerException {
		try {
			for (int i = 0; i < taskids.length; i++) {
				audit(docids[i], taskids[i], auditorid, comment, flag);
			}
		} catch (DocumentManagerException de) {
			de.printStackTrace();
			throw de;
		}
	}

	// 获取制定的返工任务的前驱任务(待审任务,待发任务的)的执行者
	public int getPreSubmitterId(int taskid) throws DocumentManagerException {
		int performer = -1;
		DBUtil db = new DBUtil();
		// 已经经过其他步骤处理的任务
		// 有前驱任务
		String sql = "select a.performer as preAuditorid from td_cms_doc_task_detail a,"
				+ "td_cms_doc_task b, td_cms_doc_task c " + "where a.valid =1 and a.complete_time is not null and "
				+ "a.task_id = b.task_id and b.task_id = c.pre_task_id and " + "c.task_id = " + taskid;
		// 另外一种情况:
		// 文档待发布, 没有发布时间, 返工, 这时, 也没有前驱任务
		String publishing_return = "select a.performer as preAuditorid from td_cms_doc_task_detail a,"
				+ "td_cms_doc_task b, td_cms_doc_task c " + "where a.valid =1  and " + "a.task_id = b.task_id and "
				+ "c.task_id = " + taskid;
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				performer = db.getInt(0, "preAuditorid");
			} else {
				// 另外一种情况:
				db.executeSelect(publishing_return);
				if (db.size() > 0) {
					performer = db.getInt(0, "preAuditorid");
				}
			}
		} catch (Exception se) {
			se.printStackTrace();
			throw new DocumentManagerException("获取返工任务的前驱待审任务的审核人时数据库操作失败" + se.getMessage());
		}
		return performer;
	}

	// 获取当前返工任务的审核意见
	public TaskDocument getTaskInfo(int taskid) throws DocumentManagerException {
		TaskDocument task = new TaskDocument();
		DBUtil db = new DBUtil();
		String sql = "select a.task_id as task_id, a.submit_id as submit_id, "
				+ "a.submit_time as submit_time, b.opinion as opinion,c.user_name as user_name "
				+ "from td_cms_doc_task a,td_cms_doc_task_detail b,td_sm_user c "
				+ "where b.valid=1 and b.complete_time is not null and " + "b.task_id = a.pre_task_id and a.task_id ="
				+ taskid + " and c.user_id = a.submit_id";
		try {

			db.executeSelect(sql);
			if (db.size() > 0) {
				task.setTaskid(db.getInt(0, "task_id"));
				task.setSubmitUserid(db.getInt(0, "submit_id"));
				task.setSubmitUserName(db.getString(0, "user_name"));
				task.setTaskOpinion(db.getString(0, "opinion"));
				task.setSubmitTime(db.getDate(0, "submit_time"));
			}
		} catch (Exception se) {
			se.printStackTrace();
			throw new DocumentManagerException("获取任务的相关信息时数据库操作失败" + se.getMessage());
		}
		return task;
	}

	public void completeTask(ContentContext context, DocumentStatus srcStatus, DocumentStatus desStatus, int performer,
			String opinion) throws DocumentManagerException {
		TransactionManager tm = new TransactionManager();

		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		String docid = context.getContentid();
		String sStatus = srcStatus.getStatus();
		if (opinion == null)
			opinion = "";
		String sql = "select a.task_id as task_id from td_cms_doc_task a, td_cms_doc_task_detail b "
				+ "where a.document_id = " + docid + " and a.pre_status = " + sStatus
				+ " and b.task_id = a.task_id and b.valid = 1 and complete_time is null for update nowait";
		try {
			tm.begin();
			int taskid;
			db.executeSelect(context.getDBName(), sql);
			if (db.size() > 0) {
				taskid = db.getInt(0, "task_id");
				// 将当前执行人performer的待审任务更新，填写任务完成时间和审核意见等
				sql = "update td_cms_doc_task_detail set complete_time ="
						+ DBUtil.getDBDate(new Date(), context.getDBName()) + ",after_status = "
						+ desStatus.getStatus() + ",opinion='" + opinion + "' where valid = 1 " + "and task_id ="
						+ taskid + " and performer =" + performer;
				db1.executeUpdate(context.getDBName(), sql);
				// 将非当前执行人performer的待审任务置为无效
				sql = "update td_cms_doc_task_detail set valid = 0 where valid = 1" + " and task_id =" + taskid
						+ " and performer !=" + performer;
				db1.executeUpdate(context.getDBName(), sql);
			}
			tm.commit();
		} catch (Exception se) {
			try {
				tm.rollback();
			} catch (RollbackException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			se.printStackTrace();
			throw new DocumentManagerException("完成任务更新失败" + se.getMessage());
		}
	}

	public void completeOneTask(int taskId, int desStatus, int performer, String opinion)
			throws DocumentManagerException {
		DBUtil db1 = new DBUtil();
		if (opinion == null)
			opinion = "";
		try {
			String sql = "";
			// 将当前执行人performer的待审任务更新，填写任务完成时间和审核意见等
			sql = "update td_cms_doc_task_detail set complete_time =" + DBUtil.getDBAdapter().to_date(new Date())
					+ ",after_status = " + desStatus + ",opinion='" + opinion + "' where valid = 1 " + "and task_id ="
					+ taskId + " and performer =" + performer;
			db1.executeUpdate(sql);
			// 将非当前执行人performer的待审任务置为无效
			sql = "update td_cms_doc_task_detail set valid = 0 where valid = 1" + " and task_id =" + taskId
					+ " and performer !=" + performer;
			db1.executeUpdate(sql);
		} catch (Exception se) {
			se.printStackTrace();
			throw new DocumentManagerException("完成任务更新失败" + se.getMessage());
		}
	}

	public int addOneTask(int docid, int preTaskId, int preStatus, int submitId, int[] executerIds)
			throws DocumentManagerException {
		DBUtil db = new DBUtil();
		String sql = "";
		long taskId = -1;
		try {
			taskId = db.getNextPrimaryKey("td_cms_doc_task");

			if (preTaskId != 0)
				sql = "insert into td_cms_doc_task(TASK_ID,pre_task_id,document_id,pre_status,submit_id,submit_time) "
						+ "values(" + taskId + "," + preTaskId + "," + docid + "," + preStatus + "," + submitId + ","
						+ DBUtil.getDBAdapter().to_date(new Date()) + ")";
			else
				sql = "insert into td_cms_doc_task(TASK_ID,document_id,pre_status,submit_id,submit_time) " + "values("
						+ taskId + "," + docid + "," + preStatus + "," + submitId + ","
						+ DBUtil.getDBAdapter().to_date(new Date()) + ")";

			db.executeInsert(sql); // 往任务表中增加一条任务

			// 在任务明细表中为每一个执行人添加一条任务
			for (int i = 0; i < executerIds.length; i++) {
				sql = "insert into td_cms_doc_task_detail(task_id,performer)" + "values(" + taskId + ","
						+ executerIds[i] + ")";
				db.executeInsert(sql);
			}
		} catch (Exception se) {
			se.printStackTrace();
			throw new DocumentManagerException("新增任务失败" + se.getMessage());
		}
		return (int) taskId;
	}

	public void clearTask(ContentContext context) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		String docid = context.getContentid();
		String sql = "";
		try {
			// 删除任务明细表中的数据
			sql = "delete from td_cms_doc_task_detail where task_id in" + "(select task_id from td_cms_doc_task "
					+ "where document_id = " + docid + ")";
			db.executeDelete(sql);
			// 删除任务表中的数据
			sql = "delete from td_cms_doc_task where document_id = " + docid;
			db.executeDelete(sql);
		} catch (Exception se) {
			se.printStackTrace();
			throw new DocumentManagerException("任务删除失败" + se.getMessage());
		}
	}

	// 删除所有该文档的相关任务
	public void clearTask(int docid) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		String sql = "";
		try {
			// 删除任务明细表中的数据
			sql = "delete from td_cms_doc_task_detail where task_id in" + "(select task_id from td_cms_doc_task "
					+ "where document_id = " + docid + ")";
			db.executeDelete(sql);
			// 删除任务表中的数据
			sql = "delete from td_cms_doc_task where document_id = " + docid;
			db.executeDelete(sql);
		} catch (Exception se) {
			se.printStackTrace();
			throw new DocumentManagerException("任务删除失败" + se.getMessage());
		}
	}

	// 删除指定任务
	public void clearOneTask(int taskId) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		String sql = "";
		try {
			// 删除任务明细表中的数据
			sql = "delete from td_cms_doc_task_detail where task_id=" + taskId;
			db.executeDelete(sql);
			// 删除任务表中的数据
			sql = "delete from td_cms_doc_task where task_id = " + taskId;
			db.executeDelete(sql);
		} catch (Exception se) {
			se.printStackTrace();
			throw new DocumentManagerException("任务删除失败" + se.getMessage());
		}
	}

	// 删除所有该文档的引用关系
	public void clearCiteRef(int docid) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		String sql = "";
		try {
			sql = "delete from td_cms_chnl_ref_doc where doc_id = " + docid;
			db.executeDelete(sql);
		} catch (Exception se) {
			se.printStackTrace();
			throw new DocumentManagerException("引用关系删除失败" + se.getMessage());
		}
	}

	// 从数据库中删除指定文档的历史操作记录
	public void clearHisOpeRecord(int docid) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		String sql = "";
		try {
			sql = "delete from tl_cms_doc_oper_log where doc_id = " + docid;
			db.executeDelete(sql);
		} catch (Exception se) {
			se.printStackTrace();
			throw new DocumentManagerException("任务删除失败" + se.getMessage());
		}
	}

	public static String reNull(String s) {
		return s == null ? "" : s;
	}

	/**
	 * get文档级别
	 * 
	 */
	public List getDocLevelList() throws DocumentManagerException {
		List list = new ArrayList();
		DBUtil conn = new DBUtil();
		String sql = "select * from tb_cms_doc_level t ";
		try {
			conn.executeSelect(sql);
			if (conn.size() > 0) {
				for (int i = 0; i < conn.size(); i++) {
					DocLevel doclevel = new DocLevel();
					doclevel.setId(conn.getInt(i, "id"));
					doclevel.setName(conn.getString(i, "name"));
					doclevel.setLevel(conn.getInt(i, "level_value"));
					list.add(doclevel);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return list;
	}

	/**
	 * 新增文档的相关文档 param DocRelated d
	 * 
	 * @return boolean
	 */
	public boolean creatorDocRelated(DocRelated d) throws DocumentManagerException {
		boolean flag = false;
		DBUtil db = new DBUtil();
		String sql = "insert into TD_CMS_DOC_RELATED(DOC_ID,RELATED_DOC_ID,OP_USER_ID,OP_TIME) " + "values("
				+ d.getDocId() + "," + d.getRelatedDocId() + "," + d.getOpUserId() + ",sysdate)";
		try {
			// System.out.println(sql);
			db.executeInsert(sql);
			flag = true;
		} catch (Exception e) {
			System.out.println("error:新增文档的相关文档时出错！");
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return flag;
	}

	/**
	 * 新增文档的相关文档 param DocRelated d param int[] related_doc_ids
	 * 
	 * @return boolean
	 */
	public boolean creatorDocRelated(DocRelated d, int[] relatedDocIds) throws DocumentManagerException {
		boolean flag = false;
		try {
			for (int i = 0; i < relatedDocIds.length; i++) {
				d.setRelatedDocId(relatedDocIds[i]);
				creatorDocRelated(d);
			}
			flag = true;
		} catch (Exception e) {
			System.out.println("error:批量新增文档的相关文档时出错！");
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return flag;
	}

	/**
	 * 删除文档的相关文档 param docid
	 * 
	 * @return boolean
	 */
	public boolean deleteDocRelated(int docid) throws DocumentManagerException {
		boolean flag = false;
		DBUtil db = new DBUtil();
		String sql = "delete from TD_CMS_DOC_RELATED where DOC_ID = " + docid;
		try {
			db.executeDelete(sql);
			flag = true;
		} catch (Exception e) {
			System.out.println("error:删除文档的相关文档时出错！");
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return flag;
	}

	/**
	 * get文档的相关文档的信息列表 param docid
	 * 
	 * @return List
	 */
	public List getDocRelatedList(int docid) throws DocumentManagerException {
		List list = new ArrayList();

		DBUtil db = new DBUtil();
		String sql = "select a.doc_id as docid,a.related_doc_id as related_doc_id,ISDELETED,"
				+ "a.op_user_id as op_user_id,b.subtitle as name, c.display_name "
				+ "from TD_CMS_DOC_RELATED a inner join td_cms_document b on a.related_doc_id=b.document_id "
				+ " inner join td_cms_channel c on c.channel_id = b.channel_id " + " where a.doc_id=" + docid
				+ " order by b.DOCWTIME desc ";
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					DocRelated docrelated = new DocRelated();
					docrelated.setDocId(db.getInt(i, "docid"));
					docrelated.setRelatedDocId(db.getInt(i, "related_doc_id"));
					docrelated.setRelatedDocName(db.getString(i, "name"));
					docrelated.setOpUserId(db.getInt(i, "op_user_id"));
					docrelated.setChlDisplayName(db.getString(i, "display_name"));
					// 保存是否回收的字段信息
					// ge.tao
					// 2007-09-15
					if (db.getInt(i, "ISDELETED") == 0) {
						docrelated.setDeleted(false);
					} else {
						docrelated.setDeleted(true);
					}
					list.add(docrelated);
				}
			}
		} catch (Exception e) {
			System.out.println("error:取文档的相关文档的信息列表时出错！");
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return list;
	}

	/**
	 * get文档的相关文档的信息列表 param docid
	 * 
	 * @return String[]
	 */
	public String[] getDocRelatedString(int docid) throws DocumentManagerException {
		List list = getDocRelatedList(docid);
		String[] strings = new String[list.size()];
		try {
			for (int i = 0; i < list.size(); i++) {
				DocRelated docrelated = new DocRelated();
				docrelated = (DocRelated) list.get(i);
				strings[i] = docrelated.getRelatedDocId() + "№" + docrelated.getRelatedDocName() + "№"
						+ docrelated.getChlDisplayName();
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return strings;
	}

	/**
	 * 获取文档的内容附件
	 * 
	 * @return List<Attachment>
	 * @throws DocumentManagerException
	 */
	public List getAttachmentsOfDocument(int docid) throws DocumentManagerException {
		List list = new ArrayList();
		DBUtil db = new DBUtil();
		String sql = "select * from TD_CMS_DOC_ATTACH t where t.valid=1 and t.document_id=" + docid;
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					Attachment att = new Attachment();
					att.setId(db.getInt(i, "id"));
					att.setDocumentId(db.getInt(i, "document_id"));
					att.setUrl(db.getString(i, "url"));
					att.setType(db.getInt(i, "type"));
					att.setDescription(db.getString(i, "description"));
					att.setOriginalFilename(db.getString(i, "original_filename"));
					att.setValid(db.getString(i, "valid"));

					list.add(att);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return list;
	}

	/**
	 * 获取文档的相关图片
	 * @return List<Attachment>
	 * @throws DocumentManagerException
	 */
	public List<Attachment> getPicturesOfDocument(final Document doc) throws DocumentManagerException {
		String sql = "select * from TD_CMS_DOC_ATTACH t where t.type=3 and t.valid=1 and t.document_id=?";
		List<Attachment> datas = null;
		try {
			datas = SQLExecutor.queryListByRowHandler(new RowHandler<Attachment>() {

				@Override
				public void handleRow(Attachment att, Record record) throws Exception {

					att.setId(record.getInt("id"));
					att.setDocumentId(record.getInt("document_id"));
					att.setUrl(record.getString("url"));
					att.setType(record.getInt("type"));
					att.setDescription(record.getString("description"));
					att.setOriginalFilename(record.getString("original_filename"));
					att.setValid(record.getString("valid"));
					att.setDocument(doc);
				}
			}, Attachment.class, sql, doc.getDocument_id());
			return datas;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<Attachment>();
	}

	public ListInfo getPicturesOfDocument(final Document doc, long offSet, int pageItemsize)
			throws DocumentManagerException {
		String sql = "select * from TD_CMS_DOC_ATTACH t where t.type=3 and t.valid=1 and t.document_id=?";
		ListInfo datas = null;
		try {
			datas = SQLExecutor.queryListInfoByRowHandler(new RowHandler<Attachment>() {

				@Override
				public void handleRow(Attachment att, Record record) throws Exception {

					att.setId(record.getInt("id"));
					att.setDocumentId(record.getInt("document_id"));
					att.setUrl(record.getString("url"));
					att.setType(record.getInt("type"));
					att.setDescription(record.getString("description"));
					att.setOriginalFilename(record.getString("original_filename"));
					att.setValid(record.getString("valid"));
					att.setDocument(doc);
				}
			}, Attachment.class, sql, offSet, pageItemsize, doc.getDocument_id());
			return datas;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取文档的相关图片
	 * 
	 * @return List<Attachment>
	 * @throws DocumentManagerException
	 */
	public List<Attachment> getPicturesOfDocument(int docid) throws DocumentManagerException {
		// List<Attachment> list = new ArrayList<Attachment>();
		// DBUtil db = new DBUtil();
		// String sql = "select * from TD_CMS_DOC_ATTACH t where t.type=3 and t.valid=1 and t.document_id=" + docid;
		// try {
		// db.executeSelect(sql);
		// if (db.size() > 0) {
		// for (int i = 0; i < db.size(); i++) {
		// Attachment att = new Attachment();
		// att.setId(db.getInt(i, "id"));
		// att.setDocumentId(db.getInt(i, "document_id"));
		// att.setUrl(db.getString(i, "url"));
		// att.setType(db.getInt(i, "type"));
		// att.setDescription(db.getString(i, "description"));
		// att.setOriginalFilename(db.getString(i, "original_filename"));
		// att.setValid(db.getString(i, "valid"));
		//
		// list.add(att);
		// }
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// throw new DocumentManagerException(e.getMessage());
		// }
		// return list;
		String sql = "select * from TD_CMS_DOC_ATTACH t where t.type=3 and t.valid=1 and t.document_id=?";
		List<Attachment> datas = null;
		try {
			datas = SQLExecutor.queryListByRowHandler(new RowHandler<Attachment>() {

				@Override
				public void handleRow(Attachment att, Record record) throws Exception {

					att.setId(record.getInt("id"));
					att.setDocumentId(record.getInt("document_id"));
					att.setUrl(record.getString("url"));
					att.setType(record.getInt("type"));
					att.setDescription(record.getString("description"));
					att.setOriginalFilename(record.getString("original_filename"));
					att.setValid(record.getString("valid"));

				}
			}, Attachment.class, sql, docid);
			return datas;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ArrayList<Attachment>();
	}

	public ListInfo getPicturesOfDocument(int docid, long offSet, int pageItemsize) throws DocumentManagerException {
		String sql = "select * from TD_CMS_DOC_ATTACH t where t.type=3 and t.valid=1 and t.document_id=?";
		ListInfo datas = null;
		try {
			datas = SQLExecutor.queryListInfoByRowHandler(new RowHandler<Attachment>() {

				@Override
				public void handleRow(Attachment att, Record record) throws Exception {

					att.setId(record.getInt("id"));
					att.setDocumentId(record.getInt("document_id"));
					att.setUrl(record.getString("url"));
					att.setType(record.getInt("type"));
					att.setDescription(record.getString("description"));
					att.setOriginalFilename(record.getString("original_filename"));
					att.setValid(record.getString("valid"));

				}
			}, Attachment.class, sql, offSet, pageItemsize, docid);
			return datas;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取文档的相关附件
	 * 
	 * @return List<Attachment>
	 * @throws DocumentManagerException
	 */
	public List getRelationAttachmentsOfDocument(int docid) throws DocumentManagerException {
		List list = new ArrayList();
		DBUtil db = new DBUtil();
		String sql = "select * from TD_CMS_DOC_ATTACH t where t.type=2 and t.valid=1 and t.document_id=" + docid;
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					Attachment att = new Attachment();
					att.setId(db.getInt(i, "id"));
					att.setDocumentId(db.getInt(i, "document_id"));
					att.setUrl(db.getString(i, "url"));
					att.setType(db.getInt(i, "type"));
					att.setDescription(db.getString(i, "description"));
					att.setOriginalFilename(db.getString(i, "original_filename"));
					att.setValid(db.getString(i, "valid"));

					list.add(att);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return list;
	}

	/**
	 * 获取文档的所有的相关东西，包括内容附件，相关图片，相关附件等 param docid(文档id) param type(附件类型)
	 * 
	 * @return List<Attachment>
	 * @throws DocumentManagerException
	 */
	public List getAllRelationOfDocument(int docid, int type) throws DocumentManagerException {
		List list = new ArrayList();
		DBUtil db = new DBUtil();
		String sql = "select * from TD_CMS_DOC_ATTACH t where t.valid=1 and t.type=" + type + " and  t.document_id="
				+ docid;
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					Attachment att = new Attachment();
					att.setId(db.getInt(i, "id"));
					att.setDocumentId(db.getInt(i, "document_id"));
					att.setUrl(db.getString(i, "url"));
					att.setType(db.getInt(i, "type"));
					att.setDescription(db.getString(i, "description"));
					att.setOriginalFilename(db.getString(i, "original_filename"));
					att.setValid(db.getString(i, "valid"));

					list.add(att);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return list;
	}

	/**
	 * 获取文档的所有的相关东西，包括内容附件，相关图片，相关附件等 返回所有附件的绝对路径 param HttpServletRequest
	 * request（从页面传进来） param docid(文档id) param channelId
	 * 
	 * @return List<String>
	 * @throws DocumentManagerException
	 */
	public List getAllAttachmentOfDocument(HttpServletRequest request, int docid) throws DocumentManagerException {
		List list = new ArrayList();
		ChannelManager cm = new ChannelManagerImpl();
		SiteManager siteManager = new SiteManagerImpl();
		Document document = this.getDoc(docid + "");

		String channelId = document.getChanel_id() + "";
		Channel channel = null;
		// 第一步，分析文档内容，提取路径信息
		String relativePath = "";
		try {
			channel = cm.getChannelInfo(channelId);
			relativePath = channel.getChannelPath();
		} catch (ChannelManagerException e1) {
			e1.printStackTrace();
		}
		// String topicpic = document.getPicPath();
		// if(topicpic != null || topicpic.length() > 0)
		// list.add(CMSUtil.getAppRootPath() + "/cms/siteResource/" + sitedir +
		// "/_webprj/" + topicpic);
		String sitedir = "";
		try {
			sitedir = siteManager.getSiteInfo(channel.getSiteId() + "").getSiteDir();
		} catch (SiteManagerException e1) {
			e1.printStackTrace();
		}
		CmsLinkProcessor processor = new CmsLinkProcessor(request, relativePath, sitedir);

		processor.setHandletype(CmsLinkProcessor.PROCESS_BACKUPCONTENT);
		try {
			processor.process(document.getContent(), CmsEncoder.ENCODING_UTF_8);
			CMSTemplateLinkTable linktable = processor.getOrigineTemplateLinkTable();
			Iterator it = linktable.iterator();
			while (it.hasNext()) {
				CMSLink link = (CMSLink) it.next();
				String attachmentPath = link.getHref();// attachmentPath=="zjcz/content_files/2007052304240100.jpg"
				attachmentPath = CMSUtil.getAppRootPath() + "/cms/siteResource/" + sitedir + "/_webprj/"
						+ attachmentPath;
				list.add(attachmentPath);
			}
			// CmsLinkTable staticlinktable =
			// processor.getOrigineStaticPageLinkTable();
			// CmsLinkTable dynamiclinktable =
			// processor.getOrigineDynamicPageLinkTable();

		} catch (ParserException e) {
			e.printStackTrace();
		}
		// 第二布，取相关图片，相关附件
		DBUtil db = new DBUtil();
		String sql = "select * from TD_CMS_DOC_ATTACH t where t.document_id=" + docid;
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					String url = db.getString(i, "url");
					url = CMSUtil.getAppRootPath() + "/cms/siteResource/" + sitedir + "/_webprj/" + relativePath
							+ "/content_files/" + url;
					list.add(url);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return list;
	}

	/**
	 * 获取文档发布后的所有附件的 不是绝对路径 是 相对路径+文件名
	 * 
	 * @param request
	 * @param docId
	 * @return DocumentManagerImpl.java
	 * @author: 陶格
	 */
	public List getAllPublishedAttachmentOfDocument(HttpServletRequest request, String docid, String siteid)
			throws DocumentManagerException {
		Document document = this.getDoc(docid);
		return getAllPublishedAttachmentOfDocument(request, document, siteid);
	}

	public String getContent(Document doc) {
		try {
			return SQLExecutor.queryObject(String.class, "select content from td_cms_document where document_id=?",
					doc.getDocument_id());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public String getContent(int doc) {
		try {
			return SQLExecutor
					.queryObject(String.class, "select content from td_cms_document where document_id=?", doc);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取文档发布后的所有附件的 不是绝对路径 是 相对路径+文件名
	 * 
	 * @param request
	 * @param docId
	 * @return DocumentManagerImpl.java
	 * @author: 陶格
	 */
	public List getAllPublishedAttachmentOfDocument(HttpServletRequest request, Document document, String siteid)
			throws DocumentManagerException {
		final List attachements = new ArrayList();
		// 第二步，取相关图片，相关附件
				
				String sql = "select * from TD_CMS_DOC_ATTACH t where t.document_id=?";
				try {
					String channelId = String.valueOf(document.getChanel_id());
					
					
						final String relativePath = CMSUtil.getChannelCacheManager(siteid).getChannel(channelId).getChannelPath();
							
						SQLExecutor.queryByNullRowHandler(new NullRowHandler()
						{

							@Override
							public void handleRow(Record origine)
									throws Exception {
								String url = origine.getString( "url");
								// url = CMSUtil.getAppRootPath() + "/cms/siteResource/" +
								// sitedir + "/_webprj/" + relativePath + "/content_files/"
								// + url;
								url = CMSUtil.getPath(CMSUtil.getPath(relativePath, "content_files"), url);
								attachements.add(url);
								
							}
							
						},sql,document.getDocument_id());
							
					
					
				} catch (Exception e) {
					
					throw new DocumentManagerException(e.getMessage());
				}
				
		return getAllPublishedAttachmentOfDocument( request,  document,  siteid, attachements);
//		
		
	}
	
	/**
	 * 获取文档发布后的所有附件的 不是绝对路径 是 相对路径+文件名
	 * 
	 * @param request
	 * @param docId
	 * @return DocumentManagerImpl.java
	 * @author: 陶格
	 */
	public List getAllPublishedAttachmentOfDocument(HttpServletRequest request, Document document, String siteid,List attachements)
			throws DocumentManagerException {
	

		String channelId = String.valueOf(document.getChanel_id());
		// String siteid = String.valueOf(document.getSiteid());
		// 频道发布路径
		// String channelPublishedDir =
		// CMSUtil.getChannelPubDestinction(siteid,channelId);

		// 第一步，分析文档内容，提取路径信息
		String relativePath = "";
		try {
			relativePath = CMSUtil.getChannelCacheManager(siteid).getChannel(channelId).getChannelPath();
		} catch (ChannelManagerException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sitedir = "";
		try {
			sitedir = CMSUtil.getSiteCacheManager().getSite(siteid).getSiteDir();
		} catch (ChannelManagerException e1) {
			e1.printStackTrace();
		} catch (SiteManagerException e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		CmsLinkProcessor processor = new CmsLinkProcessor(request, relativePath, sitedir);
		processor.setHandletype(CmsLinkProcessor.PROCESS_BACKUPCONTENT);
		CMSTemplateLinkTable linktable = null;
		try {
			if (document.getContent() == null) {
				document.setContent(this.getContent(document));
			}
			processor.process(document.getContent(), CmsEncoder.ENCODING_UTF_8);
			linktable = processor.getOrigineTemplateLinkTable();

			

		} catch (ParserException e) {
			e.printStackTrace();
		}
		
		return getAllPublishedAttachmentOfDocument(request, document, siteid,attachements,linktable );
	
	}
	
	public List getAllPublishedAttachmentOfDocument(HttpServletRequest request, Document document, String siteid,
			List attachements,CMSTemplateLinkTable origineTemplateLinkTable )
			throws DocumentManagerException {
		List list = new ArrayList();
		
//
//		String channelId = String.valueOf(document.getChanel_id());
//		// String siteid = String.valueOf(document.getSiteid());
//		// 频道发布路径
//		// String channelPublishedDir =
//		// CMSUtil.getChannelPubDestinction(siteid,channelId);
//
//		// 第一步，分析文档内容，提取路径信息
//		String relativePath = "";
//		try {
//			relativePath = CMSUtil.getChannelCacheManager(siteid).getChannel(channelId).getChannelPath();
//		} catch (ChannelManagerException e1) {
//			e1.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		String sitedir = "";
//		try {
//			sitedir = CMSUtil.getSiteCacheManager().getSite(siteid).getSiteDir();
//		} catch (ChannelManagerException e1) {
//			e1.printStackTrace();
//		} catch (SiteManagerException e1) {
//			e1.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		CmsLinkProcessor processor = new CmsLinkProcessor(request, relativePath, sitedir);
//		processor.setHandletype(CmsLinkProcessor.PROCESS_BACKUPCONTENT);
//		try {
//			if(document.getContent() == null)
//			{
//				document.setContent(this.getContent(document));
//			}
//			processor.process(document.getContent(), CmsEncoder.ENCODING_UTF_8);
//			CMSTemplateLinkTable linktable = processor.getOrigineTemplateLinkTable();
//			Iterator it = linktable.iterator();
//			while (it.hasNext()) {
//				CMSLink link = (CMSLink) it.next();
//				String attachmentPath = link.getRelativeFilePath();// attachmentPath=="zjcz/content_files/2007052304240100.jpg"
//				// attachmentPath = CMSUtil.getAppRootPath() +
//				// "/cms/siteResource/" + sitedir + "/_webprj/" +
//				// attachmentPath;
//				// attachmentPath = channelPublishedDir + attachmentPath;
//				list.add(attachmentPath);
//			}
//
//		} catch (ParserException e) {
//			e.printStackTrace();
//		}
		if(origineTemplateLinkTable != null &&origineTemplateLinkTable.size() > 0)
		{
			Iterator it = origineTemplateLinkTable.iterator();
			while (it.hasNext()) {
				CMSLink link = (CMSLink) it.next();
				String attachmentPath = link.getRelativeFilePath();// attachmentPath=="zjcz/content_files/2007052304240100.jpg"
				// attachmentPath = CMSUtil.getAppRootPath() +
				// "/cms/siteResource/" + sitedir + "/_webprj/" +
				// attachmentPath;
				// attachmentPath = channelPublishedDir + attachmentPath;
				list.add(attachmentPath);
			}
		}
		if(attachements != null && attachements.size() > 0)
			list.addAll(attachements);

		return list;
	}

	/**
	 * 获取文档的所有的相关东西，包括内容附件，相关图片，相关附件等 param docid(文档id) param type(附件类型)
	 * 
	 * @return String[]<Attachment>
	 * @throws DocumentManagerException
	 */
	public String[] getAllRelationOfDocument2String(int docid, int type) throws DocumentManagerException {
		String[] s;
		List list = getAllRelationOfDocument(docid, type);
		s = new String[list.size()];
		try {
			for (int i = 0; i < list.size(); i++) {
				Attachment attachment = new Attachment();
				attachment = (Attachment) list.get(i);
				s[i] = attachment.getId() + "№" + attachment.getDocumentId() + "№" + attachment.getUrl() + "№"
						+ attachment.getType() + "№" + attachment.getDescription() + "№"
						+ attachment.getOriginalFilename() + "№";
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return s;
	}

	/**
	 * 新增文档附件 param <Attachment>
	 * 
	 * @return boolean
	 * @throws DocumentManagerException
	 */
	public boolean createAttachment(Attachment attachment) throws DocumentManagerException {
		boolean flag = false;
		boolean isIdentical = false;// 是否雷同
		DBUtil dbUtil = new DBUtil();
		String flagsql = "select 1 from TD_CMS_DOC_ATTACH " + "where document_id = " + attachment.getDocumentId()
				+ " and url = '" + attachment.getUrl() + "'";
		try {
			dbUtil.executeSelect(flagsql);
			if (dbUtil.size() > 0)
				isIdentical = true;
			if (!isIdentical) {
				PreparedDBUtil db = new PreparedDBUtil();
				String sql = "insert into TD_CMS_DOC_ATTACH(DOCUMENT_ID,URL,TYPE,DESCRIPTION,ORIGINAL_FILENAME,ID) values(?,?,?,?,?,?)";
				try {
					long attachId = db.getNextPrimaryKey("TD_CMS_DOC_ATTACH");

					db.preparedInsert(sql);

					db.setInt(1, attachment.getDocumentId());
					db.setString(2, attachment.getUrl());
					db.setInt(3, attachment.getType());
					db.setString(4, attachment.getDescription());
					db.setString(5, attachment.getOriginalFilename());
					db.setLong(6, attachId);

					db.executePrepared();
					flag = true;
				} catch (Exception e) {
					e.printStackTrace();
					throw new DocumentManagerException(e.getMessage());
				} finally {
					db.resetPrepare();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return flag;
	}

	/**
	 * 更新文档附件 param <Attachment>
	 * 
	 * @return boolean
	 * @throws DocumentManagerException
	 */
	public boolean UpdateAttachment(Attachment attachment) throws DocumentManagerException {
		boolean flag = false;
		PreparedDBUtil db = new PreparedDBUtil();
		String sql = "update TD_CMS_DOC_ATTACH set description=? where id=?";
		try {
			db.preparedUpdate(sql);

			db.setString(1, attachment.getDescription());
			db.setPrimaryKey(2, attachment.getId());

			db.executePrepared();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		} finally {
			db.resetPrepare();
		}
		return flag;
	}

	/**
	 * 更新文档附件的状态（字段：valid）
	 * 
	 * @param int
	 *            docId(文档id)
	 * @param String
	 *            sqlstr(sql条件，所有有效的attr的url拼成的string)
	 * @return boolean
	 * @throws DocumentManagerException
	 */
	public boolean UpdateAttachmentState(int docId, int type, String sqlstr) throws DocumentManagerException {
		boolean flag = false;
		DBUtil db = new DBUtil();
		String sql = "update td_cms_doc_attach t set t.valid='0' " + "where t.document_id = " + docId
				+ " and t.type = " + type + " and t.url not in(" + sqlstr + ")";
		try {
			db.executeUpdate(sql);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return flag;
	}

	/**
	 * 删除文档附件 param 文档id param type
	 * 
	 * @return boolean
	 * @throws DocumentManagerException
	 */
	public boolean deleteAttachment(int docid, int type) throws DocumentManagerException {
		boolean flag = false;
		DBUtil db = new DBUtil();
		String sql = "delete from TD_CMS_DOC_ATTACH t where t.document_id=" + docid + " and t.type=" + type;
		try {
			db.executeDelete(sql);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return flag;
	}

	/**
	 * 更新文档的状态
	 * 
	 * @param published
	 */
	public void updateDocumentStatus(ContentContext context, DocumentStatus newStatus) throws DocumentManagerException {
		DBUtil dbUtil = new DBUtil();
		String docid = context.getContentid();
		String status = newStatus.getStatus();
		String sql = "update td_cms_document set status =" + status + " where document_id =" + docid;
		try {
			dbUtil.executeUpdate(context.getDBName(), sql);
			if (status.equals("5"))
				this.completeTask(context, DocumentStatus.PREPUBLISH, newStatus,
						Integer.parseInt((context.getPublisher())[0]), "发布");
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
	}

	public void updateDocumentStatus(int docid, int newStatus) throws DocumentManagerException {
		DBUtil dbUtil = new DBUtil();
		String sql = "update td_cms_document set status =" + newStatus + " where document_id =" + docid;
		try {
			dbUtil.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
	}

	/**
	 * 更新文档发布时间,发布的时候调用
	 * 
	 * @param docid
	 * @throws DocumentManagerException
	 */
	public void updateDocPublishTime(ContentContext context) throws DocumentManagerException {
		DBUtil dbUtil = new DBUtil();
		String sqlUpdate = "update td_cms_document set publishtime = "
				+ DBUtil.getDBDate(context.getPublishTime(), context.getDBName()) + " where document_id="
				+ context.getContentid();
		try {
			dbUtil.executeUpdate(context.getDBName(), sqlUpdate);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
	}

	/**
	 * 更新文档发布时间
	 * 
	 * @param docid
	 * @throws DocumentManagerException
	 */
	public void updateDocPublishTime(int docid) throws DocumentManagerException {
		DBUtil dbUtil = new DBUtil();
		String sqlUpdate = "update td_cms_document set publishtime = " + DBUtil.getDBAdapter().to_date(new Date())
				+ " where document_id=" + docid;
		try {
			dbUtil.executeUpdate(sqlUpdate);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
	}

	/**
	 * 返回文档发布方式的列表 return int[]
	 */
	public int[] getDocDistributeManners(int docId) throws DocumentManagerException {
		try {
			int[] docDistributeManners = new int[1];
			docDistributeManners[0] = 0;
			return docDistributeManners;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
	}

	/**
	 * get具体某一文档的最高版本号 return int
	 */
	public int getMaxDocVersion(int docId) throws DocumentManagerException {
		int mVersion = 0;
		String sql = "select nvl(max(version),0) from TD_CMS_DOC_VER t where document_id = " + docId;
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				mVersion = db.getInt(0, "version");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return mVersion;
	}

	/**
	 * get具体某一文档的所有版本 return List<Document>
	 * only(version,subtitle,op_user,op_time)
	 */
	public List getAllDocVersion(int docId) throws DocumentManagerException {
		List list = new ArrayList();
		String sql = "select version,subtitle,op_user,op_time from TD_CMS_DOC_VER t where document_id = " + docId;
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					Document doc = new Document();

					doc.setVersion(db.getInt(i, "version"));
					doc.setSubtitle(db.getString(i, "subtitle"));
					doc.setOpUser(db.getInt(i, "op_user"));
					doc.setOpTime(db.getDate(i, "op_time"));

					list.add(doc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return list;
	}

	/**
	 * get文档指定版本的文档的详细信息
	 * 
	 * @param docId
	 * @param version
	 * @return
	 * @throws DocumentManagerException
	 */
	public Document getDocVerInfo(String docId, String version) throws DocumentManagerException {
		String sql = "select * from TD_CMS_DOC_VER t where t.document_id = " + docId + "and t.version = " + version;
		DBUtil db = new DBUtil();
		Document document = new Document();
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				document.setDocument_id(db.getInt(0, "document_id"));
				document.setVersion(db.getInt(0, "version"));
				document.setTitle(db.getString(0, "title"));
				document.setSubtitle(db.getString(0, "SUBTITLE"));
				document.setOpTime(db.getDate(0, "op_time"));
				document.setVerLable(db.getString(0, "label"));
				document.setVerDiscription(db.getString(0, "versiondescription"));

				/*
				 * document.setDocument_id (db.getInt(0,"document_id")); document.setTitle (db.getString(0,"title"));
				 * document.setSubtitle (db.getString(0,"SUBTITLE")); document.setStatus (db.getInt(0,"status"));
				 * document.setDocflag (db.getInt(0,"isdeleted")); document.setAuthor (db.getString(0,"AUTHOR"));
				 * document.setCreateTime (db.getDate(0,"CREATETIME")); document.setDoctype (db.getInt(0,"DOCTYPE"));
				 * document.setContent (db.getString(0,"CONTENT")); document.setChanel_id (db.getInt(0,"channel_id"));
				 * document.setLinktarget (db.getString(0,"LINKTARGET")); //出于链接文档的链接地址保存的是在content字段的考虑
				 * if(document.getDoctype() == 1) document.setLinkfile(document.getContent()); document.setDocsource_id
				 * (db.getInt(0,"DOCSOURCE_ID"));
				 * 
				 * document.setDocwtime (db.getDate(0,"DOCWTIME"));
				 * 
				 * document.setCreateUser (db.getInt(0,"CREATEUSER"));
				 * document.setDetailtemplate_id(db.getInt(0,"DETAILTEMPLATE_ID")); document.setDocabstract
				 * (db.getString(0,"DOCABSTRACT"));
				 * 
				 * document.setTitlecolor (db.getString(0,"TITLECOLOR")); document.setKeywords
				 * (db.getString(0,"KEYWORDS")); document.setDoc_level (db.getInt(0,"doc_level"));
				 * document.setParentDetailTpl(db.getString(0,"PARENT_DETAIL_TPL")); document.setPicPath
				 * (db.getString(0,"PIC_PATH")); document.setMediapath (db.getString(0,"mediapath"));
				 * document.setPublishfilename(db.getString(0,"publishfilename")); document.setSecondtitle
				 * (db.getString(0,"secondtitle"));
				 * 
				 * 装载扩展字段数据 document.setExtColumn(extManager.getExtColumnInfo(db)); String str ="select SRCNAME from
				 * TD_CMS_DOCSOURCE where DOCSOURCE_ID ="+ db.getInt(0,"DOCSOURCE_ID") +"";
				 * 
				 * db1.executeSelect(str); if(db1.size()>0) { document.setDocsource_name(db1.getString(0,"SRCNAME")); }
				 * String str1 ="select NAME from TD_CMS_TEMPLATE where
				 * TEMPLATE_ID="+ db.getInt(0,"DETAILTEMPLATE_ID") +""; db1.executeSelect(str1); if(db1.size()>0) {
				 * document.setDetailtemplate_name(db1.getString(0,"NAME")); }
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return document;
	}

	/**
	 * 新增版本 return int
	 */
	public void addDocVersion(int docId, int userId, String verLable, String verComment)
			throws DocumentManagerException {
		String sql = "insert into TD_CMS_DOC_VER select " + docId + ","
				+ "(select nvl(max(version),0)+1 from TD_CMS_DOC_VER where document_id = " + docId + "),"
				+ "TITLE,SUBTITLE,AUTHOR ,CONTENT ,CHANNEL_ID ,KEYWORDS ,DOCABSTRACT,DOCTYPE ,DOCWTIME ,"
				+ "TITLECOLOR  ,DOCSOURCE_ID  ,DETAILTEMPLATE_ID ,LINKTARGET  ,FLOW_ID  ,DOC_LEVEL ,DOC_KIND,"
				+ "PARENT_DETAIL_TPL," + userId + ",sysdate,'" + StringUtil.replaceAll(verLable, "'", "''") + "','"
				+ StringUtil.replaceAll(verComment, "'", "''")
				+ "',PIC_PATH,MEDIAPATH,PUBLISHFILENAME,secondtitle,isnew,newpic_path "
				+ "from td_cms_document where document_id = " + docId;
		DBUtil db = new DBUtil();
		try {
			db.executeInsert(sql);
			// 文挡当前版本号+1
			sql = "update td_cms_document t set t.version = "
					+ "nvl((select version from td_cms_document where document_id = " + docId + "),0)+1 "
					+ "where document_id = " + docId;
			db.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
	}

	/**
	 * 批量新增版本 return int
	 */
	public void addDocVersions(int[] docIds, int userId, String verLable, String verComment)
			throws DocumentManagerException {
		try {
			for (int i = 0; i < docIds.length; i++) {
				addDocVersion(docIds[i], userId, verLable, verComment);
			}
		} catch (DocumentManagerException de) {
			throw de;
		}
	}

	/**
	 * 删除版本 return int
	 */
	public int delDocVersion(int docId, int docVersion) throws DocumentManagerException {
		int i = 0;
		DBUtil db = new DBUtil();
		String sql = "delete from TD_CMS_DOC_VER t where t.document_id=" + docId + " and t.version=" + docVersion;
		try {
			db.executeDelete(sql);
			i = 1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return i;
	}

	/**
	 * 恢复到某一版本状态 return int
	 */
	public int resumeDocVersion(int docId, int docVersion, int userId) throws DocumentManagerException {
		int i = 0;
		String sql = "insert into TD_CMS_DOC_VER select " + docId + ","
				+ "(select nvl(max(version),0)+1 from TD_CMS_DOC_VER where document_id = " + docId + "),"
				+ "TITLE,SUBTITLE,AUTHOR ,CONTENT ,CHANNEL_ID ,KEYWORDS ,DOCABSTRACT,DOCTYPE ,DOCWTIME ,"
				+ "TITLECOLOR  ,DOCSOURCE_ID  ,DETAILTEMPLATE_ID ,LINKTARGET  ,FLOW_ID  ,DOC_LEVEL ,DOC_KIND,"
				+ "PARENT_DETAIL_TPL," + userId
				+ ",sysdate,'','',PIC_PATH,MEDIAPATH,PUBLISHFILENAME,secondtitle,isnew,newpic_path "
				+ "from td_cms_document where document_id = " + docId;

		DBUtil db = new DBUtil();
		try {
			// 先保存当前版本
			db.executeInsert(sql);
			// 再更新
			sql = "update td_cms_document set(TITLE,SUBTITLE,AUTHOR ,CONTENT ,CHANNEL_ID ,KEYWORDS,"
					+ "DOCABSTRACT,DOCTYPE ,DOCWTIME ,TITLECOLOR  ,DOCSOURCE_ID  ,DETAILTEMPLATE_ID ,LINKTARGET,"
					+ "FLOW_ID  ,DOC_LEVEL ,DOC_KIND,PARENT_DETAIL_TPL,PIC_PATH,MEDIAPATH,PUBLISHFILENAME,secondtitle,isnew,newpic_path) = "
					+ "(select TITLE,SUBTITLE,AUTHOR ,CONTENT ,CHANNEL_ID ,KEYWORDS ,DOCABSTRACT,DOCTYPE ,"
					+ "DOCWTIME ,TITLECOLOR  ,DOCSOURCE_ID  ,DETAILTEMPLATE_ID ,LINKTARGET  ,FLOW_ID  ,"
					+ "DOC_LEVEL ,DOC_KIND  ,PARENT_DETAIL_TPL,PIC_PATH,MEDIAPATH,PUBLISHFILENAME,secondtitle,isnew,newpic_path from td_cms_doc_ver "
					+ "where document_id = " + docId + " and version = " + docVersion + ")" + "where document_id = "
					+ docId;
			db.executeUpdate(sql);
			// 文挡当前版本号+1
			sql = "update td_cms_document t set t.version = "
					+ "nvl((select version from td_cms_document where document_id = " + docId + "),0)+1 "
					+ "where document_id = " + docId;
			db.executeUpdate(sql);
			i = 1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}

		return i;
	}

	/**
	 * 获取某个文档的相关文档集合 return List<Document> param docId(文档id)
	 */
	public List getRelatedDocsOfDoc(String docId) throws DocumentManagerException {
		List list = new ArrayList();
		DBUtil db = new DBUtil();
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		String sql = "select b.DOCUMENT_ID, b.TITLE, b.SUBTITLE, b.AUTHOR, b.CHANNEL_ID,"
				+ " b.STATUS, b.KEYWORDS, b.DOCABSTRACT, b.DOCTYPE, b.DOCWTIME, b.TITLECOLOR,"
				+ " b.CREATETIME, b.CREATEUSER, b.DOCSOURCE_ID, b.DETAILTEMPLATE_ID, b.LINKTARGET,"
				+ " b.FLOW_ID, b.DOC_LEVEL, b.DOC_KIND,  b.PARENT_DETAIL_TPL,b.PUBLISHFILENAME,b.secondtitle "
				+ "from TD_CMS_CHNL_REF_DOC a  inner join "
				+ "td_cms_document b on a.doc_id = b.document_id where chnl_id = " + docId;
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
					doc.setPublishfilename(db.getString(i, "PUBLISHFILENAME"));
					doc.setSecondtitle(db.getString(i, "secondtitle"));
					/* 装载扩展字段数据 */
					doc.setExtColumn(extManager.getExtColumnInfo(i,db));

					list.add(doc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return list;
	}

	/**
	 * new 聚合文档 param <DocAggregation> aggrDoc
	 */
	public boolean addAggrDoc(DocAggregation aggrDoc) throws DocumentManagerException {
		boolean flag = false;
		DBUtil db = new DBUtil();
		String sql = "insert into TD_CMS_DOC_AGGREGATION(AGGR_DOC_ID,SEQ,ID_BY_AGGR,TITLE,TYPE) " + "values("
				+ aggrDoc.getAggrdocid() + "," + aggrDoc.getSeq() + "," + aggrDoc.getIdbyaggr() + ",'"
				+ StringUtil.replaceAll(aggrDoc.getTitle(), "'", "''") + "'," + aggrDoc.getType() + ")";
		try {
			db.executeInsert(sql);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return flag;
	}

	/**
	 * del 聚合文档 param int docId
	 */
	public boolean delAggrDoc(int docId) throws DocumentManagerException {
		boolean flag = false;
		DBUtil db = new DBUtil();
		String sql = "delete from TD_CMS_DOC_AGGREGATION where AGGR_DOC_ID = " + docId;
		try {
			db.executeDelete(sql);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return flag;
	}

	/**
	 * get 聚合文档List param int docId return List<DocAggregation>
	 */
	/*
	 * public List getAggrDocList(int docId) throws DocumentManagerException { List list = new ArrayList(); String sql =
	 * "select a.*,b.subtitle from TD_CMS_DOC_AGGREGATION a inner join td_cms_document b" + " on a.id_by_aggr =
	 * b.document_id where a.AGGR_DOC_ID = " + docId + " order by a.seq"; DBUtil db = new DBUtil(); try {
	 * db.executeSelect(sql); if(db.size()>0) { for(int i=0;i<db.size();i++) { AggrDoc aggrDoc = new AggrDoc();
	 * 
	 * aggrDoc.setAggrDocID(docId); aggrDoc.setSeq(db.getInt(i,"SEQ")); aggrDoc.setIdByAggr(db.getInt(i,"ID_BY_AGGR"));
	 * aggrDoc.setTitle(db.getString(i,"TITLE")); aggrDoc.setType(db.getInt(i,"TYPE"));
	 * aggrDoc.setDocsubtitle(db.getString(i,"subtitle"));
	 * 
	 * list.add(aggrDoc); } } }catch(Exception e) { e.printStackTrace(); throw new
	 * DocumentManagerException(e.getMessage()); } return list; }
	 */
	/**
	 * get 聚合文档List2Str param int docId return String[]<DocAggregation>
	 */
	public String[] getAggrDocList2Str(String docId) throws DocumentManagerException {
		String[] s;
		List list = getDocAggregationInfo(docId);
		s = new String[list.size()];
		for (int i = 0; i < list.size(); i++) {
			DocAggregation ad = new DocAggregation();
			ad = (DocAggregation) list.get(i);
			s[i] = ad.getIdbyaggr() + "№" + ad.getDocsubtitle() + "№" + ad.getTitle() + "№" + ad.getChlName();
		}
		return s;
	}

	/**
	 * 根据文档ID获取文档聚合信息 param String docId return List<DocAggregation>
	 */
	public List getDocAggregationInfo(String docId) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		List list = new ArrayList();
		String sql = "select a.*,b.subtitle,b.channel_id,d.display_name from TD_CMS_DOC_AGGREGATION a inner join td_cms_document b"
				+ " on a.id_by_aggr = b.document_id "
				+ " inner join td_cms_channel d on d.channel_id = b.channel_id "
				+ " where b.isdeleted = '0' and a.AGGR_DOC_ID = " + docId + " order by a.seq";
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					DocAggregation dagg = new DocAggregation();

					dagg.setTitle(db.getString(i, "title"));
					dagg.setType(db.getString(i, "type"));
					dagg.setIdbyaggr(db.getInt(i, "id_by_aggr"));
					dagg.setDocsubtitle(db.getString(i, "subtitle"));
					dagg.setChlId(String.valueOf(db.getInt(i, "channel_id")));
					dagg.setChlName(db.getString(i, "display_name"));

					list.add(dagg);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return list;
	}

	/**
	 * 根据文档ID获取文档聚合中所有已经发布的被聚合的文档 那些没发布的被聚合文档不取进来 param String docId return List<DocAggregation>
	 */
	public List getPubAggrDocList(String docId) throws DocumentManagerException {
		PreparedDBUtil db = new PreparedDBUtil();
		List list = new ArrayList();
		StringBuffer sql = new StringBuffer();
//		sql.append("select a.*,b.subtitle,b.channel_id,b.isnew,b.newpic_path,b.titlecolor ")
//				.append("from TD_CMS_DOC_AGGREGATION a inner join td_cms_document b")
//				.append(" on a.id_by_aggr = b.document_id ").append(" where b.STATUS in ( ")
//				.append(DocumentStatus.PUBLISHED.getStatus()).append(",").append(DocumentStatus.PUBLISHING.getStatus())
//				.append(") and b.isdeleted = '0' and a.AGGR_DOC_ID = ").append(docId).append(" order by a.seq");
		sql.append("select a.*,b.subtitle,b.channel_id,b.isnew,b.newpic_path,b.titlecolor ")
		.append("from TD_CMS_DOC_AGGREGATION a inner join td_cms_document b")
		.append(" on a.id_by_aggr = b.document_id ").append(" where b.STATUS in ( ?,?) and b.isdeleted = '0' and a.AGGR_DOC_ID = ? order by a.seq");
		try {
			db.preparedSelect(sql.toString());
			db.setString(1, DocumentStatus.PUBLISHED.getStatus());
			db.setString(2, DocumentStatus.PUBLISHING.getStatus());
			db.setString(3, docId);
			db.executePrepared();
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					DocAggregation dagg = new DocAggregation();

					dagg.setTitle(db.getString(i, "title"));
					dagg.setType(db.getString(i, "type"));
					dagg.setIdbyaggr(db.getInt(i, "id_by_aggr"));
					dagg.setDocsubtitle(db.getString(i, "subtitle"));
					dagg.setChlId(String.valueOf(db.getInt(i, "channel_id")));
					dagg.setIsNew(db.getInt(i, "isnew"));
					dagg.setNewPicPath(db.getString(i, "newpic_path"));
					dagg.setTitlecolor(db.getString(i, "titlecolor"));

					list.add(dagg);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return list;
	}

	/*
	 * public static void main(String[] args) throws DocumentManagerException { DocumentManagerImpl impl = new
	 * DocumentManagerImpl(); Attachment a = new Attachment(); a.setDocumentId(3018); a.setDescription("aaa");
	 * a.setUrl("aaa.mp3"); a.setType(1); a.setOriginalFilename("文件是mp3"); //a.setId(1); impl.createAttachment(a); }
	 */

	public Template getDetailTemplate(String docId) throws DocumentManagerException {
		DBUtil conn = new DBUtil();
		String sql = "";
		try {

			sql = "select b.*,c.user_name from td_cms_document a inner join td_cms_template b on a.DETAILTEMPLATE_ID = b.template_id "
					+ "inner join td_sm_user c on b.createuser = c.user_id and "
					+ "parent_detail_tpl=0 and a.document_id='" + docId + "'";
			conn.executeSelect(sql);
			if (conn.size() > 0) {
				Template templateobj = new Template();
				templateobj.setName(conn.getString(0, "NAME")); // 模板名称
				templateobj.setDescription(conn.getString(0, "description")); // 模板描述
				/* 判断text CLOB字段是否为空 */
				DBUtil db = new DBUtil();
				String sqlstr = "select b.* from td_cms_document a inner join td_cms_template b on a.DETAILTEMPLATE_ID = b.template_id "
						+ " and a.document_id='" + docId + "' and dbms_lob.getlength(b.text)=0";
				db.executeSelect(sqlstr);
				if (db.size() > 0) {/* CLOB为空 */
					templateobj.setText("");
				} else {
					templateobj.setText(conn.getString(0, "text"));
				}
				/* end */
				templateobj.setType(conn.getInt(0, "TYPE"));
				templateobj.setCreateTime(conn.getDate(0, "CREATETIME"));
				templateobj.setCreateUserName(conn.getString(0, "user_name"));
				templateobj.setHeader(conn.getString(0, "HEADER"));
				templateobj.setTemplateId(conn.getInt(0, "TEMPLATE_ID"));

				templateobj.setTemplateFileName(conn.getString(0, "TEMPLATEFILENAME"));

				templateobj.setTemplatePath(conn.getString(0, "TEMPLATEPATH"));
				templateobj.setPersistType(conn.getInt(0, "PERSISTTYPE"));
				return templateobj;
			} else {
				return null;// new Template();
			}
		} catch (Exception e) {
			throw new DocumentManagerException("文档取详细模板出错." + e.getMessage());
		}

	}

	/**
	 * 置顶文档已经存在
	 * 
	 * @param docId
	 * @return boolean
	 * @throws DocumentManagerException
	 */
	public boolean ArrangeDocExist(String docId) throws DocumentManagerException {
		boolean flag = false;
		String sql = "select 1 from TD_CMS_DOC_ARRANGE where document_id = " + docId;
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			if (db.size() > 0)
				flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return flag;
	}

	/**
	 * 新增置顶文档
	 * 
	 * @param <ArrangeDoc>arrangeDoc
	 * @return boolean
	 * @throws DocumentManagerException
	 */
	public boolean addArrangeDoc(ArrangeDoc arrangeDoc) throws DocumentManagerException {
		boolean flag = false;
		String sql = "insert into TD_CMS_DOC_ARRANGE values(" + arrangeDoc.getDocumentId() + ",'"
				+ arrangeDoc.getStartTime() + "','" + arrangeDoc.getEndTime()
				+ "',(select nvl(max(order_no),-1)+1 from TD_CMS_DOC_ARRANGE where document_id "
				+ "in(select b.document_id from td_cms_document b where b.channel_id = "
				+ "(select c.channel_id from td_cms_document c where c.document_id = " + arrangeDoc.getDocumentId()
				+ ")))," + arrangeDoc.getOpUser() + ", " + DBUtil.getDBAdapter().to_date(new Date()) + ")";
		// System.out.print(sql);
		DBUtil db = new DBUtil();
		try {
			db.executeInsert(sql);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return flag;
	}

	/**
	 * get置顶文档信息
	 * 
	 * @param String
	 *            docid
	 * @return <ArrangeDoc>arrangeDoc
	 * @throws DocumentManagerException
	 */
	public ArrangeDoc getArrangeDoc(String docid) throws DocumentManagerException {
		ArrangeDoc arrangeDoc = new ArrangeDoc();
		String sql = "select * from TD_CMS_DOC_ARRANGE where document_id = " + docid;
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				arrangeDoc.setDocumentId(db.getInt(0, "document_id"));
				// arrangeDoc.setDoctitle("");
				arrangeDoc.setStartTime(db.getString(0, "start_time"));
				arrangeDoc.setEndTime(db.getString(0, "end_time"));
				arrangeDoc.setOpUser(db.getInt(0, "op_user"));
				// arrangeDoc.set
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return arrangeDoc;
	}

	/**
	 * 删除置顶文档
	 * 
	 * @param docId
	 * @return boolean
	 * @throws DocumentManagerException
	 */
	public boolean delArrangeDoc(String[] docId) throws DocumentManagerException {
		boolean flag = false;

		DBUtil db = new DBUtil();
		try {
			for (int i = 0; i < docId.length; i++) {
				String sql = "delete from TD_CMS_DOC_ARRANGE where document_id = " + docId[i];

				// db.executeDelete(sql);
				db.addBatch(sql);
				flag = true;

			}
			if (flag)
				db.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return flag;
	}

	/**
	 * 更新置顶文档信息
	 * 
	 * @param <ArrangeDoc>arrangeDoc
	 * @return boolean
	 * @throws DocumentManagerException
	 */
	public boolean updateArrangeDoc(ArrangeDoc arrangeDoc) throws DocumentManagerException {
		boolean flag = false;
		String sql = "update TD_CMS_DOC_ARRANGE " + "set start_time = '" + arrangeDoc.getStartTime() + "',end_time = '"
				+ arrangeDoc.getEndTime() + "'" + " where document_id = " + arrangeDoc.getDocumentId();
		DBUtil db = new DBUtil();
		try {
			db.executeUpdate(sql);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return flag;
	}

	/**
	 * 设置更新置顶文档的权重
	 * 
	 * @param docId
	 * @return boolean
	 * @throws DocumentManagerException
	 */
	public boolean updateArrangeDocOrderNo(String[] docIds) throws DocumentManagerException {
		boolean flag = false;

		DBUtil db = new DBUtil();
		try {
			int no = docIds.length;
			String sql = null;

			for (int i = 0; i < docIds.length; i++) {
				sql = "update TD_CMS_DOC_ARRANGE " + "set order_no = '" + (--no) + "'" + " where document_id = "
						+ docIds[i];
				db.addBatch(sql);
				// db.executeUpdate(sql);
			}
			if (docIds.length > 0)
				db.executeBatch();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return flag;
	}

	/**
	 * get文档的所有扩展字段信息的Map
	 * 
	 * @param String
	 *            docid
	 * @return Map Map对应的key：扩展字段的name Map对应的value：文档对应该扩展字段的内容
	 * @throws DocumentManagerException
	 */
	public Map getDocExtFieldMap(String docid) throws DocumentManagerException {
		Map map = new HashMap();
		DBUtil db = new DBUtil();
		DBUtil db2 = new DBUtil();
		String sql = "select a.fieldname as key,b.fieldvalue as value "
				+ "from td_cms_extfield a inner join td_cms_extfieldvalue b "
				+ "on a.field_id = b.field_id where b.document_id = " + docid + " and a.fieldtype in ('1','4','5','6')";
		try {
			// varchar,file,select,radiobox类型
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					map.put(db.getString(i, "key"), db.getString(i, "value"));
				}
			}
			// number类型
			sql = "select a.fieldname as key,b.numbervalue as value "
					+ " from td_cms_extfield a inner join td_cms_extfieldvalue b "
					+ " on a.field_id = b.field_id where b.document_id = " + docid + " and a.fieldtype=0";

			db.executeSelect(sql);

			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					map.put(db.getString(i, "key"), db.getInt(i, "value") + "");
				}
			}
			// clob类型
			sql = "select a.fieldname as key,b.clobvalue as value "
					+ " from td_cms_extfield a inner join td_cms_extfieldvalue b "
					+ " on a.field_id = b.field_id where b.document_id = " + docid + " and a.fieldtype=3";
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					map.put(db.getString(i, "key"), db.getString(i, "value"));
				}
			}
			// date类型
			sql = "select a.fieldname as key,b.datevalue as value "
					+ " from td_cms_extfield a inner join td_cms_extfieldvalue b "
					+ " on a.field_id = b.field_id where b.document_id = " + docid + " and a.fieldtype=2";
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					map.put(db.getString(i, "key"), db.getDate(i, "value"));
				}
			}
			// checkbox类型
			sql = "select distinct a.field_id as id " + " from td_cms_extfield a inner join td_cms_extfieldvalue b "
					+ " on a.field_id = b.field_id where b.document_id = " + docid + " and a.fieldtype=7";
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					int tempid = db.getInt(i, "id");
					sql = "select a.fieldname as key,c.value as value "
							+ " from td_cms_extfield a inner join td_cms_extfieldvalue b "
							+ " on a.field_id = b.field_id ,td_cms_extvaluescope c "
							+ " where b.fieldvalue = c.id and b.document_id = " + docid
							+ " and a.fieldtype=7 and a.field_id = " + tempid + " order by c.id";
					db2.executeSelect(sql);
					String key = db2.getString(i, "key");
					String value = "";
					if (db2.size() > 0) {
						for (int j = 0; j < db2.size(); j++) {
							value += db2.getString(j, "value") + "&nbsp;";
						}
						map.put(key, value);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return map;
	}
	/**
	 * get文档的所有扩展字段信息的Map
	 * 
	 * @param String
	 *            docid
	 * @return Map Map对应的key：扩展字段的name Map对应的value：文档对应该扩展字段的内容
	 * @throws DocumentManagerException
	 */
	public Map<String,DocExtValue> getDocExtFieldMapBean(String docid) throws DocumentManagerException {
		Map<String,DocExtValue> map = new HashMap<String,DocExtValue>();
		PreparedDBUtil db = new PreparedDBUtil();
		PreparedDBUtil db2 = new PreparedDBUtil();
		String sql = "select a.fieldname as key,a.fieldtype as fieldtype,a.FIELDLABEL as fieldlabel,b.fieldvalue as value "
				+ "from td_cms_extfield a inner join td_cms_extfieldvalue b "
				+ "on a.field_id = b.field_id where b.document_id = ? and a.fieldtype in ('1','4','5','6')";
		try {
			// varchar,file,select,radiobox类型
			int id = Integer.parseInt(docid);
			db.preparedSelect(sql);
			db.setInt(1, id);
			db.executePrepared();
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					DocExtValue docvalue = new DocExtValue();
					docvalue.setField(db.getString(i, "key"));
					docvalue.setStringvalue(db.getString(i, "value"));
					docvalue.setFieldtype(db.getString(i, "fieldtype"));
					docvalue.setLabel(db.getString(i, "fieldlabel"));
					map.put(docvalue.getField(), docvalue);
				}
			}
			// number类型
			sql = "select a.fieldname as key,a.fieldtype as fieldtype,a.FIELDLABEL as fieldlabel,b.numbervalue as value "
					+ " from td_cms_extfield a inner join td_cms_extfieldvalue b "
					+ " on a.field_id = b.field_id where b.document_id = ? and a.fieldtype=0";
			db = new PreparedDBUtil();
			db.preparedSelect(sql);
			db.setInt(1, id);
			db.executePrepared();

			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					DocExtValue docvalue = new DocExtValue();
					docvalue.setField(db.getString(i, "key"));
					docvalue.setIntvalue(db.getInt(i, "value"));
					docvalue.setFieldtype(db.getString(i, "fieldtype"));
					docvalue.setLabel(db.getString(i, "fieldlabel"));
					map.put(docvalue.getField(), docvalue);
					
				}
			}
			// clob类型
			sql = "select a.fieldname as key,a.fieldtype as fieldtype,a.FIELDLABEL as fieldlabel,b.clobvalue as value "
					+ " from td_cms_extfield a inner join td_cms_extfieldvalue b "
					+ " on a.field_id = b.field_id where b.document_id = ? and a.fieldtype=3";
			db = new PreparedDBUtil();
			db.preparedSelect(sql);
			db.setInt(1, id);
			db.executePrepared();
		
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					DocExtValue docvalue = new DocExtValue();
					docvalue.setField(db.getString(i, "key"));
					docvalue.setStringvalue(db.getString(i, "value"));
					docvalue.setFieldtype(db.getString(i, "fieldtype"));
					docvalue.setLabel(db.getString(i, "fieldlabel"));
					map.put(docvalue.getField(), docvalue);
					
				}
			}
			// date类型
			sql = "select a.fieldname as key,a.fieldtype as fieldtype,a.FIELDLABEL as fieldlabel,b.datevalue as value "
					+ " from td_cms_extfield a inner join td_cms_extfieldvalue b "
					+ " on a.field_id = b.field_id where b.document_id = ? and a.fieldtype=2";
			db = new PreparedDBUtil();
			db.preparedSelect(sql);
			db.setInt(1, id);
			db.executePrepared();
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					DocExtValue docvalue = new DocExtValue();
					docvalue.setField(db.getString(i, "key"));
					docvalue.setDatevalue(db.getDate(i, "value"));
					docvalue.setFieldtype(db.getString(i, "fieldtype"));
					docvalue.setLabel(db.getString(i, "fieldlabel"));
					map.put(docvalue.getField(), docvalue);
				}
			}
			// checkbox类型
			sql = "select distinct a.fieldname as key,a.field_id as id,a.FIELDLABEL as fieldlabel,a.fieldtype as fieldtype from td_cms_extfield a inner join td_cms_extfieldvalue b "
					+ " on a.field_id = b.field_id where b.document_id = ? and a.fieldtype=7";
			db = new PreparedDBUtil();
			db.preparedSelect(sql);
			db.setInt(1, id);
			db.executePrepared();
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					int tempid = db.getInt(i, "id");
					sql = "select a.fieldname as key,c.value as value "
							+ " from td_cms_extfield a inner join td_cms_extfieldvalue b "
							+ " on a.field_id = b.field_id ,td_cms_extvaluescope c "
							+ " where b.fieldvalue = c.id and b.document_id = ? and a.fieldtype=7 and a.field_id = ? order by c.id";
					db2.preparedSelect(sql);
					db2.setInt(1, id);
					db2.setInt(2, tempid);
					db2.executePrepared();
					String key = db.getString(i, "key");
					String fieldtype = db.getString(i, "fieldtype");
					String label  = db.getString(i, "fieldlabel"); 
					
					String value = "";
					if (db2.size() > 0) {
						for (int j = 0; j < db2.size(); j++) {
							value += db2.getString(j, "value") + "&nbsp;";
						}
						DocExtValue docvalue = new DocExtValue();
						docvalue.setField(key);
						docvalue.setStringvalue(value);
						docvalue.setFieldtype(fieldtype);
						docvalue.setLabel(label);
						map.put(docvalue.getField(), docvalue);
						
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return map;
	}

	/*
	 * public static void main(String[] args) { DocumentManagerImpl dm = new DocumentManagerImpl(); try { Map map =
	 * dm.getDocExtFieldMap("33467");//("33478"); System.out.println(map.get("date"));//("j")); } catch
	 * (DocumentManagerException e) { e.printStackTrace(); } }
	 */
	/**
	 * 
	 */
	public List getTopDocOfChnl(String chnlID, int top) throws DocumentManagerException {
		List list = new ArrayList();
		DBUtil db = new DBUtil();
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		String sql = "select b.publishtime,b.DOCUMENT_ID, b.TITLE, b.SUBTITLE, b.AUTHOR, b.CHANNEL_ID, b.STATUS, b.KEYWORDS, b.DOCABSTRACT, b.DOCTYPE, b.DOCWTIME, b.TITLECOLOR, b.CREATETIME, b.CREATEUSER, b.DOCSOURCE_ID, b.DETAILTEMPLATE_ID, b.LINKTARGET, b.FLOW_ID, b.DOC_LEVEL, b.DOC_KIND, b.PARENT_DETAIL_TPL,b.PUBLISHFILENAME,b.secondtitle from td_cms_document b where channel_id = "
				+ chnlID + " and ROWNUM<=" + String.valueOf(top) + " order by docwtime desc";
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
					doc.setPublishTime(db.getDate(i, "PUBLISHTIME"));
					doc.setPublishfilename(db.getString(i, "PUBLISHFILENAME"));
					doc.setSecondtitle(db.getString(i, "secondtitle"));
					doc.setExtColumn(extManager.getExtColumnInfo(i,db));

					list.add(doc);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return list;
	}

	public List getFieldValueList(String channelId, String fieldId, String type) throws DocumentManagerException {
		List list = new ArrayList();
		DBUtil conn = new DBUtil();
		DBUtil db = new DBUtil();
		String str = "select INPUTTYPE from TD_CMS_EXTFIELD where field_id =" + fieldId;

		String sql = "select b.value,b.id,a.field_id,a.FIELDTYPE from TD_CMS_EXTFIELD a,TD_CMS_EXTVALUESCOPE b"
				+ " where a.field_id = b.field_id and a.FIELDTYPE=" + type + " and a.field_id in( "
				+ " select field_id from td_cms_channelfield where channel_id =" + channelId + ") "
				+ " and a.field_id =" + fieldId;

		String sql1 = "select b.minvalue,b.maxvalue from TD_CMS_EXTFIELD a,TD_CMS_EXTVALUESCOPE b  "
				+ " where a.field_id = b.field_id and  a.field_id =" + fieldId;

		try {
			conn.executeSelect(str);
			if (conn.size() > 0) {
				String inputtype = conn.getInt(0, "INPUTTYPE") + "";
				if (inputtype.equals("1")) {
					db.executeSelect(sql);
					if (db.size() > 0) {

						for (int i = 0; i < db.size(); i++) {
							Extvaluescope evs = new Extvaluescope();
							evs.setId(db.getInt(i, "id") + "");
							evs.setValue(db.getString(i, "value"));
							list.add(evs);
						}
					}
				} else {
					db.executeSelect(sql1);
					if (db.size() > 0) {
						int min = db.getInt(0, "minvalue");
						int max = db.getInt(0, "maxvalue");
						for (int i = min; i <= max; i++) {
							Extvaluescope evs = new Extvaluescope();
							evs.setId(i + "");
							evs.setValue(i + "");
							list.add(evs);
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return list;
	}

	public void saveExtFieldMap(String docid, String fieldid, String clobvalue, String checked, String flag)
			throws DocumentManagerException {

		DBUtil db = new DBUtil();

		String select = "select count(*) from TD_CMS_EXTFIELDVALUE where " + " FIELD_ID=" + fieldid
				+ " and DOCUMENT_ID=" + docid + " and FIELDVALUE='" + clobvalue + "'";

		String sql = "insert into TD_CMS_EXTFIELDVALUE(FIELD_ID,DOCUMENT_ID,FIELDVALUE)" + " values(" + fieldid + ","
				+ docid + ",'" + clobvalue + "')";

		String delete = "delete from TD_CMS_EXTFIELDVALUE where " + " FIELD_ID=" + fieldid + " and DOCUMENT_ID="
				+ docid + "";

		String delete2 = "delete from TD_CMS_EXTFIELDVALUE where " + " FIELD_ID=" + fieldid + " and DOCUMENT_ID="
				+ docid + " and FIELDVALUE='" + clobvalue + "'";

		// 单选按钮的扩展字段
		if (flag.equals("radiobox")) {
			try {

				db.executeDelete(delete);
				db.executeInsert(sql);

			} catch (SQLException e) {
				e.printStackTrace();
				throw new DocumentManagerException(e.getMessage());
			}
		}
		// 复选框的扩展字段
		else {
			try {

				if (checked.equals("1")) {
					db.executeSelect(select);
					if (db.getInt(0, 0) == 0) {
						// System.out.println(sql);
						db.executeInsert(sql);
					}
				} else {
					// System.out.println(delete);
					db.executeDelete(delete2);
				}

			} catch (SQLException e) {
				e.printStackTrace();
				throw new DocumentManagerException(e.getMessage());
			}
		}
	}

	/**
	 * get文档的相关文档的信息列表 param docid
	 * 
	 * @return List
	 */
	public List getRelatedDocList(String docid, int count) throws DocumentManagerException {
		List list = new ArrayList();
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		DBUtil db = new DBUtil();
		Document document = null;
		String sql = "select t.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, "
				+ "DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, DOCSOURCE_ID, DETAILTEMPLATE_ID, "
				+ "LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,STATUS,"
				+ "case when DOCTYPE=1 "
				+ "then t.content else null end linkfile,pic_path,mediapath,publishfilename,"
				+ "commentswitch,secondtitle,ordertime from td_cms_document t "
				+ "where document_id in (select related_doc_id from TD_CMS_DOC_RELATED where doc_id="
				+ docid
				+ ") "
				+ (count > 0 ? "and rownum<=" + String.valueOf(count) : "")
				+ " and STATUS in ( "
				+ DocumentStatus.PUBLISHED.getStatus()
				+ ","
				+ DocumentStatus.PUBLISHING.getStatus()
				+ ")  and ISDELETED=0 order by DOCWTIME desc";
		// System.out.println(sql);
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					document = new Document();
					// 处理引用频道
					// add by ge.tao
					// 2007-09-14
					if (db.getInt(i, "DOCTYPE") == Document.DOCUMENT_CHANNEL) {
						String channelid = String.valueOf(db.getLong(i, "document_id"));
						Channel refChannel = new ChannelManagerImpl().getChannelInfo(channelid);
						// document.setRefChannel(refChannel);
						// document.setDoctype(Document.DOCUMENT_CHANNEL);
						list.add(refChannel);
					} else {
						document.setDocument_id(db.getInt(i, "document_id"));
						document.setTitle(db.getString(i, "title"));
						document.setSubtitle(db.getString(i, "SUBTITLE"));
						document.setStatus(db.getInt(i, "status"));
						document.setAuthor(db.getString(i, "AUTHOR"));
						document.setCreateTime(db.getDate(i, "CREATETIME"));
						document.setDoctype(db.getInt(i, "DOCTYPE"));
						// document.setContent (db.getString(i,"CONTENT"));
						document.setChanel_id(db.getInt(i, "channel_id"));
						document.setLinktarget(db.getString(i, "LINKTARGET"));
						// 出于链接文档的链接地址保存的是在content字段的考虑
						// if(document.getDoctype() == 1)
						document.setLinkfile(db.getString(i, "linkfile"));
						document.setDocsource_id(db.getInt(i, "DOCSOURCE_ID"));

						document.setDocwtime(db.getDate(i, "DOCWTIME"));

						document.setCreateUser(db.getInt(i, "CREATEUSER"));
						document.setDetailtemplate_id(db.getInt(i, "DETAILTEMPLATE_ID"));
						document.setDocabstract(db.getString(i, "DOCABSTRACT"));

						document.setTitlecolor(db.getString(i, "TITLECOLOR"));
						document.setKeywords(db.getString(i, "KEYWORDS"));
						document.setDoc_level(db.getInt(i, "doc_level"));
						document.setParentDetailTpl(db.getString(i, "PARENT_DETAIL_TPL"));
						document.setPicPath(db.getString(i, "PIC_PATH"));
						document.setMediapath(db.getString(i, "mediapath"));
						document.setPublishfilename(db.getString(i, "publishfilename"));
						document.setSecondtitle(db.getString(i, "secondtitle"));
						document.setOrdertime(db.getDate(i, "ordertime"));
						/* 装载扩展字段数据 */
						Map docExtField = getDocExtFieldMapBean(document.getDocument_id() + "");
						document.setDocExtField(docExtField);
						/* 装载系统扩展字段数据 */
						document.setExtColumn(extManager.getExtColumnInfo(i,db));
						list.add(document);
					}

				}
			}
		} catch (Exception e) {
			System.out.println("error:取文档的相关文档的信息列表时出错！");
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return list;
	}

	/**
	 * get文档的相关文档的信息列表 param docid
	 * 
	 * @return List
	 */
	public ListInfo getRelatedDocList(String docid, long offset, int pageitems) throws DocumentManagerException {
		List list = new ArrayList();
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		DBUtil db = new DBUtil();
		Document document = null;
		String sql = "select t.DOCUMENT_ID as document_id, TITLE, SUBTITLE, AUTHOR, CHANNEL_ID, KEYWORDS, DOCABSTRACT, DOCTYPE, "
				+ "DOCWTIME, TITLECOLOR, CREATETIME, CREATEUSER, DOCSOURCE_ID, DETAILTEMPLATE_ID, "
				+ "LINKTARGET, FLOW_ID, DOC_LEVEL, DOC_KIND, PARENT_DETAIL_TPL,publishtime,STATUS,"
				+ "case when DOCTYPE=1 "
				+ "then t.content else null end linkfile,pic_path,mediapath,publishfilename,"
				+ "commentswitch,secondtitle,ordertime from td_cms_document t where document_id in (select related_doc_id from TD_CMS_DOC_RELATED where doc_id="
				+ docid
				+ ") "
				+ " and STATUS in ( "
				+ DocumentStatus.PUBLISHED.getStatus()
				+ ","
				+ DocumentStatus.PUBLISHING.getStatus() + ") " + "and ISDELETED=0 order by DOCWTIME desc";
		// System.out.println(sql);
		try {
			db.executeSelect(sql, offset, pageitems);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					document = new Document();
					// 处理引用频道
					// add by ge.tao
					// 2007-09-14
					if (db.getInt(i, "DOCTYPE") == Document.DOCUMENT_CHANNEL) {
						String channelid = String.valueOf(db.getLong(i, "document_id"));
						Channel refChannel = new ChannelManagerImpl().getChannelInfo(channelid);
						// document.setRefChannel(refChannel);
						// document.setDoctype(Document.DOCUMENT_CHANNEL);
						list.add(refChannel);
					} else {
						document.setDocument_id(db.getInt(i, "document_id"));
						document.setTitle(db.getString(i, "title"));
						document.setSubtitle(db.getString(i, "SUBTITLE"));
						document.setStatus(db.getInt(i, "status"));
						document.setAuthor(db.getString(i, "AUTHOR"));
						document.setCreateTime(db.getDate(i, "CREATETIME"));
						document.setDoctype(db.getInt(i, "DOCTYPE"));
						// document.setContent (db.getString(i,"CONTENT"));
						document.setChanel_id(db.getInt(i, "channel_id"));
						document.setLinktarget(db.getString(i, "LINKTARGET"));
						// 出于链接文档的链接地址保存的是在content字段的考虑
						// if(document.getDoctype() == 1)
						document.setLinkfile(db.getString(i, "linkfile"));
						document.setDocsource_id(db.getInt(i, "DOCSOURCE_ID"));

						document.setDocwtime(db.getDate(i, "DOCWTIME"));

						document.setCreateUser(db.getInt(i, "CREATEUSER"));
						document.setDetailtemplate_id(db.getInt(i, "DETAILTEMPLATE_ID"));
						document.setDocabstract(db.getString(i, "DOCABSTRACT"));

						document.setTitlecolor(db.getString(i, "TITLECOLOR"));
						document.setKeywords(db.getString(i, "KEYWORDS"));
						document.setDoc_level(db.getInt(i, "doc_level"));
						document.setParentDetailTpl(db.getString(i, "PARENT_DETAIL_TPL"));
						document.setPicPath(db.getString(i, "PIC_PATH"));
						document.setMediapath(db.getString(i, "mediapath"));
						document.setPublishfilename(db.getString(i, "publishfilename"));
						document.setSecondtitle(db.getString(i, "secondtitle"));
						document.setOrdertime(db.getDate(i, "ordertime"));
						/* 装载扩展字段数据 */
						Map docExtField = getDocExtFieldMapBean(document.getDocument_id() + "");
						document.setDocExtField(docExtField);
						/* 装载系统扩展字段数据 */
						document.setExtColumn(extManager.getExtColumnInfo(i,db));
						list.add(document);
					}

				}
			}
		} catch (Exception e) {
			System.out.println("error:取文档的相关文档的信息列表时出错！");
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		ListInfo listinfo = new ListInfo();
		listinfo.setDatas(list);
		listinfo.setTotalSize(db.getTotalSize());
		return listinfo;
	}

	/**
	 * 频道下 直接 子频道和文档的SQL语句 公共部分
	 * 
	 * @author: 陶格
	 */
	private String directSpecialDocSQL = "t.channel_id," + "d.document_id  "
			+ "from td_cms_channel t,td_cms_document d " + "where t.channel_id=d.channel_id ";

	/**
	 * 频道下 文档 引用文档 引用频道 混合列表 的SQL语句 String
	 * 
	 * @param channel
	 *            display_name
	 * @return String DocumentManagerImpl.java
	 * @author: 陶格
	 */
	public String createarrangeSQL(String channel) {
		// DBUtil db = new DBUtil();
		StringBuffer partSQL = new StringBuffer()
				.append(" (select channel_id from td_cms_channel where display_name = '").append(channel).append("') ");
		StringBuffer arrangeSpecialDocSQL = new StringBuffer()
				.append("select * from ( ")
				.append("select p.* from( ")
				.append("select t.DOCUMENT_ID as document_id,  DOCTYPE, ")
				// add
				.append("title,SUBTITLE,status,AUTHOR, ")
				.append("CREATETIME,  case when DOCTYPE=1 then to_char(t.content) else null end linkfile,")
				.append("channel_id,LINKTARGET,DOCSOURCE_ID, ")
				.append("DOCWTIME,CREATEUSER,DETAILTEMPLATE_ID,DOCABSTRACT,TITLECOLOR,")
				.append("KEYWORDS,doc_level,PARENT_DETAIL_TPL,PIC_PATH,mediapath,publishfilename,secondtitle, ")
				// --end add
				.append("nvl(a.order_no,-1) as order_no,1 as ordersq ,publishtime ")
				.append("from td_cms_document t ")
				.append("left outer join ( ")
				.append("select * from td_cms_doc_arrange a ")
				.append("where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')< ")
				.append(DBUtil.getDBAdapter().to_date(new Date()) + " ")
				.append("and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')> ")
				.append(DBUtil.getDBAdapter().to_date(new Date()) + " ")
				.append(") a on t.document_id = a.document_id ")
				.append("where STATUS in ( " + DocumentStatus.PUBLISHED.getStatus() + ","
						+ DocumentStatus.PUBLISHING.getStatus() + ") and ISDELETED = 0 ")
				.append("and t.document_id not in( ")
				.append("select c.id_by_aggr from td_cms_doc_aggregation c ")
				.append("inner join td_cms_document z1 on c.id_by_aggr = z1.document_id ")
				.append("inner join td_cms_channel z2 on z1.channel_id = z2.channel_id ")
				.append("where z1.channel_id = ")
				.append(partSQL.toString())
				.append(") and CHANNEL_ID = ")
				.append(partSQL.toString())
				.append("union all ")
				.append("select c.DOCUMENT_ID as document_id,  DOCTYPE, ")
				// add
				.append("title,SUBTITLE,status,AUTHOR, ")
				.append("CREATETIME,  case when DOCTYPE=1 then to_char(c.content) else null end linkfile,")
				.append("channel_id,LINKTARGET,DOCSOURCE_ID, ")
				.append("DOCWTIME,CREATEUSER,DETAILTEMPLATE_ID,DOCABSTRACT,TITLECOLOR,")
				.append("KEYWORDS,doc_level,PARENT_DETAIL_TPL,PIC_PATH,mediapath,publishfilename,secondtitle, ")
				// --end add
				.append("nvl(e.order_no,-1) as order_no,2 as ordersq ,publishtime ")
				.append("from td_cms_document c, td_cms_chnl_ref_doc d ")
				.append("left outer join ( ")
				.append("select * from td_cms_doc_arrange a ")
				.append("where to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')< ")
				.append(DBUtil.getDBAdapter().to_date(new Date()) + " ")
				.append("and to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')> ")
				.append(DBUtil.getDBAdapter().to_date(new Date()) + " ")
				.append(") e on d.doc_id = e.document_id ")
				.append("where c.document_id=d.doc_id and STATUS in ( " + DocumentStatus.PUBLISHED.getStatus() + ","
						+ DocumentStatus.PUBLISHING.getStatus() + ") and ISDELETED = 0 ")
				.append("and d.doc_id not in( ")
				.append("select c.id_by_aggr from td_cms_doc_aggregation c ")
				.append("inner join td_cms_document z1 on c.id_by_aggr = z1.document_id ")
				.append("inner join td_cms_channel z2 on z1.channel_id = z2.channel_id ")
				.append("where z1.channel_id = ")
				.append(partSQL.toString())
				.append(") and d.chnl_id = ")
				.append(partSQL.toString())
				.append("order by order_no desc,docwtime desc ")
				.append(") p union ( ")
				.append("select doc.doc_id as document_id,4 as DOCTYPE, ")
				// add
				.append("'' as title,'' as SUBTITLE,1 as status,'' as AUTHOR, ")
				.append("sysdate as CREATETIME,   ")
				.append("''  as  linkfile,1 as channel_id,'' as LINKTARGET,1 as DOCSOURCE_ID, ")
				.append("channel.createtime as DOCWTIME,1 as CREATEUSER,1 as DETAILTEMPLATE_ID,'' as DOCABSTRACT,'' as TITLECOLOR,")
				.append("'' as KEYWORDS,1 as doc_level,'1' as PARENT_DETAIL_TPL,'' as PIC_PATH,'' as mediapath,'' as publishfilename,'' as secondtitle ,")
				.append(" -1 as order_no, -1 as ordersq, sysdate as publishtime ")
				// --end add
				.append("from td_cms_chnl_ref_doc doc , ")
				.append("td_cms_channel channel where doc.citetype=1  ")
				.append("and doc.doc_id = channel.channel_id and doc.CHNL_ID= ( ")
				.append(partSQL)
				.append(") union ")
				.append("select pd.channel_id as document_id, 4 as doctype,")
				// add
				.append("'' as title,'' as SUBTITLE,1 as status,'' as AUTHOR, ")
				.append("sysdate as CREATETIME,   ")
				.append("''  as  linkfile,1 as channel_id,'' as LINKTARGET,1 as DOCSOURCE_ID, ")
				.append("pd.createtime as DOCWTIME,1 as CREATEUSER,1 as DETAILTEMPLATE_ID,'' as DOCABSTRACT,'' as TITLECOLOR,")
				.append("'' as KEYWORDS,1 as doc_level,'1' as PARENT_DETAIL_TPL,'' as PIC_PATH,'' as mediapath,'' as publishfilename,'' as secondtitle ,")
				.append(" -1 as order_no, -1 as ordersq, sysdate as publishtime ")
				// --end add
				.append(" from td_cms_channel pd where pd.parent_id= ").append(partSQL.toString()).append(") ) abc  ");
		// System.out.println("==========================="+arrangeSpecialDocSQL.toString());
		// order by abc.publishtime desc,order_no desc
		return arrangeSpecialDocSQL.toString();
	}

	/**
	 * 纯SQL 频道下 文档 引用文档 引用频道 混合列表 的SQL语句 考虑 (1)文档置顶排序 (2)聚合文档 (3)引用文档 引用频道
	 * (4)最近发布
	 * 
	 * @author: 陶格 select * from ( select p.* from( select t.DOCUMENT_ID as
	 *          document_id, DOCTYPE, nvl(a.order_no,-1) as order_no,1 as
	 *          ordersq ,publishtime from td_cms_document t left outer join (
	 *          select * from td_cms_doc_arrange a where
	 *          to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<sysdate and
	 *          to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>sysdate ) a on
	 *          t.document_id = a.document_id where STATUS in ( " +
	 *          DocumentStatus.PUBLISHED.getStatus() + "," +
	 *          DocumentStatus.PUBLISHING.getStatus() + ") and ISDELETED = 0 and
	 *          t.document_id not in( select c.id_by_aggr from
	 *          td_cms_doc_aggregation c inner join td_cms_document z1 on
	 *          c.id_by_aggr = z1.document_id inner join td_cms_channel z2 on
	 *          z1.channel_id = z2.channel_id where z1.channel_id = 541 ) and
	 *          CHANNEL_ID = 541 --联合 union all select c.DOCUMENT_ID as
	 *          document_id, DOCTYPE, nvl(e.order_no,-1) as order_no,2 as
	 *          ordersq ,publishtime from td_cms_document c, td_cms_chnl_ref_doc
	 *          d left outer join ( select * from td_cms_doc_arrange a where
	 *          to_date(a.start_time,'yyyy-mm-dd hh24:mi:ss')<sysdate and
	 *          to_date(a.end_time,'yyyy-mm-dd hh24:mi:ss')>sysdate ) e on
	 *          d.doc_id = e.document_id where c.document_id=d.doc_id and STATUS
	 *          in ( " + DocumentStatus.PUBLISHED.getStatus() + "," +
	 *          DocumentStatus.PUBLISHING.getStatus() + ") and ISDELETED = 0 and
	 *          d.doc_id not in( select c.id_by_aggr from td_cms_doc_aggregation
	 *          c inner join td_cms_document z1 on c.id_by_aggr = z1.document_id
	 *          inner join td_cms_channel z2 on z1.channel_id = z2.channel_id
	 *          where z1.channel_id = 541 ) and d.chnl_id = 541 order by
	 *          ordersq,order_no desc,publishtime desc ) p union ( select
	 *          doc.doc_id,4 as doctype,-1 as order_no,-1 as ordersq
	 *          ,channel.createtime as publishtime from td_cms_chnl_ref_doc doc ,
	 *          td_cms_channel channel where doc.doc_id = channel.channel_id and
	 *          doc.citetype=1 union select pd.channel_id as document_id, 4 as
	 *          doctype, -1 as order_no, -1 as ordersq , pd.createtime as
	 *          publishtime from td_cms_channel pd where pd.parent_id= 541 ) )
	 *          abc order by abc.publishtime desc,order_no desc
	 */

	/**
	 * 少用 获取频道下 直接 子频道和文档的混合列表 不翻页!!!
	 * 
	 * @param channel
	 * @param count
	 * @return List
	 * @throws DocumentManagerException
	 *             DocumentManagerImpl.java
	 * @author: 陶格
	 */
	public List getDirectSpecialDocList(String siteid, String channel, int count) throws DocumentManagerException {
		List list = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from (")
				.append("select 0 as type,d.docwtime as ordertime, d.DOCTYPE,d.publishtime,d.docwtime,")
				// add
				.append("d.title,d.SUBTITLE,d.status,d.AUTHOR,")
				.append("d.CREATETIME,  case when DOCTYPE=1 ")
				.append("then to_char(d.content) else null end linkfile,d.LINKTARGET,d.DOCSOURCE_ID, ")
				.append("d.CREATEUSER,d.DETAILTEMPLATE_ID,d.DOCABSTRACT,d.TITLECOLOR,")
				.append("d.KEYWORDS,d.doc_level,d.PARENT_DETAIL_TPL,d.PIC_PATH,d.mediapath,d.publishfilename,d.secondtitle, ")
				// --end add
				.append("t.channel_id," + "d.document_id  " + "from td_cms_channel t,td_cms_document d "
						+ "where t.channel_id=d.channel_id ")
				.append("and (d.status=5 or d.status=10 ) and d.ISDELETED=0 ")
				.append("and t.display_name='")
				.append(channel)
				.append("' and t.site_id=")
				.append(siteid)
				.append(" union ")
				.append("(select 1 as type,t.createtime as ordertime, 4 as DOCTYPE, sysdate as publishtime,sysdate as docwtime,")
				// add
				.append("'' as title,'' as SUBTITLE,1 as status,'' as AUTHOR, ")
				.append("sysdate as CREATETIME,   ")
				.append("''  as  linkfile,'' as LINKTARGET,1 as DOCSOURCE_ID, ")
				.append("1 as CREATEUSER,1 as DETAILTEMPLATE_ID,'' as DOCABSTRACT,'' as TITLECOLOR,")
				.append("'' as KEYWORDS,1 as doc_level,'1' as PARENT_DETAIL_TPL,'' as PIC_PATH,'' as mediapath,'' as publishfilename,'' as secondtitle ,")
				// --end add
				.append("t.channel_id," + "t.channel_id as document_id  " + "from td_cms_channel t "
						+ "where t.parent_id in (").append("select t.channel_id from td_cms_channel t ")
				.append("where t.display_name='").append(channel).append("' and t.site_id=").append(siteid)
				.append(") ) )abc ").append(count > 0 ? "where rownum<=" + String.valueOf(count) : " ")
				.append(" order by abc.ordertime desc");
		log.warn(sql.toString());
		list = (List) this.setSpecialDocData(sql.toString(), false, 0, 0);
		return list;
	}

	/**
	 * 少用 获取频道下 直接 子频道和文档的混合列表 翻页!!!
	 * 
	 * @param channel
	 * @param offset
	 * @param pageitems
	 * @return ListInfo
	 * @throws DocumentManagerException
	 *             DocumentManagerImpl.java
	 * @author: 陶格
	 */
	public ListInfo getDirectSpecialDocList(String siteid, String channel, long offset, int pageitems)
			throws DocumentManagerException {
		ListInfo listinfo = new ListInfo();

		StringBuffer sql = new StringBuffer();
		sql.append("select * from (")
				.append("select 0 as type,d.docwtime as ordertime, d.DOCTYPE,d.publishtime,d.docwtime,")
				// add
				.append("d.title,d.SUBTITLE,d.status,d.AUTHOR,")
				.append("d.CREATETIME,  case when DOCTYPE=1 ")
				.append("then to_char(d.content) else null end linkfile,d.LINKTARGET,d.DOCSOURCE_ID, ")
				.append("d.CREATEUSER,d.DETAILTEMPLATE_ID,d.DOCABSTRACT,d.TITLECOLOR,")
				.append("d.KEYWORDS,d.doc_level,d.PARENT_DETAIL_TPL,d.PIC_PATH,d.mediapath,d.publishfilename,d.secondtitle, ")
				// --end add
				.append("t.channel_id," + "d.document_id  " + "from td_cms_channel t,td_cms_document d "
						+ "where t.channel_id=d.channel_id ")
				.append("and (d.status=5 or d.status=10 )  and d.ISDELETED=0 ")
				.append("and t.display_name='")
				.append(channel)
				.append("' and t.site_id=")
				.append(siteid)
				.append(" union ")
				.append("(select 1 as type,t.createtime as ordertime, 4 as DOCTYPE, sysdate as publishtime,sysdate as docwtime,")
				// add
				.append("'' as title,'' as SUBTITLE,1 as status,'' as AUTHOR, ")
				.append("sysdate as CREATETIME,   ")
				.append("''  as  linkfile,'' as LINKTARGET,1 as DOCSOURCE_ID, ")
				.append("1 as CREATEUSER,1 as DETAILTEMPLATE_ID,'' as DOCABSTRACT,'' as TITLECOLOR,")
				.append("'' as KEYWORDS,1 as doc_level,'1' as PARENT_DETAIL_TPL,'' as PIC_PATH,'' as mediapath,'' as publishfilename,'' as secondtitle ,")
				// --end add
				.append("t.channel_id," + "t.channel_id as document_id  " + "from td_cms_channel t "
						+ "where t.parent_id in (").append("select t.channel_id from td_cms_channel t ")
				.append("where t.display_name='").append(channel).append("' and t.site_id=").append(siteid)
				.append(") ) )abc ").append("order by abc.ordertime desc");
		log.warn(sql.toString());
		listinfo = (ListInfo) this.setSpecialDocData(sql.toString(), true, offset, pageitems);
		return listinfo;
	}

	/**
	 * 获取频道下 文档 引用文档 引用频道 混合列表 不翻页!!!
	 * 
	 * @param channel
	 * @param count
	 * @return List
	 * @throws DocumentManagerException
	 *             DocumentManagerImpl.java
	 * @author: 陶格
	 */
	public List getArrangeSpecialDocList(String channel, int count) throws DocumentManagerException {
		List list = new ArrayList();
		StringBuffer sql = new StringBuffer();
		sql.append(this.createarrangeSQL(channel)).append(count > 0 ? " where rownum<=" + String.valueOf(count) : "  ")
				.append(" order by abc.docwtime desc,order_no desc ");
		list = (List) this.setSpecialDocData(sql.toString(), false, 0, 0);
		return list;
	}

	/**
	 * 获取频道下 文档 引用文档 引用频道 混合列表 翻页!!!
	 * 
	 * @param channel
	 * @param offset
	 * @param pageitems
	 * @return ListInfo
	 * @throws DocumentManagerException
	 *             DocumentManagerImpl.java
	 * @author: 陶格
	 */
	public ListInfo getArrangeSpecialDocList(String channel, long offset, int pageitems)
			throws DocumentManagerException {
		ListInfo listinfo = new ListInfo();
		StringBuffer sql = new StringBuffer().append(this.createarrangeSQL(channel)).append(
				"order by abc.docwtime desc,order_no desc ");
		listinfo = (ListInfo) this.setSpecialDocData(sql.toString(), true, offset, pageitems);
		return listinfo;
	}

	/**
	 * 加载 频道下 直接/引用 子频道和文档的混合列表 数据 type: 0 文档; type: 1 频道;
	 * 
	 * @param sqlstr
	 * @param isTurnPage
	 * @param offset
	 * @param pageitems
	 * @return Object DocumentManagerImpl.java
	 * @author: 陶格
	 */
	public Object setSpecialDocData(String sqlstr, boolean isTurnPage, long offset, int pageitems) {
		List list = new ArrayList();
		DBUtil db = new DBUtil();
		Document document = null;
		Channel refChannel = null;
		ListInfo listinfo = new ListInfo();
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		try {
			if (isTurnPage) {
				db.executeSelect(sqlstr, offset, pageitems);
			} else {
				db.executeSelect(sqlstr);
			}
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					document = new Document();
					/* 引用的频道信息 */
					if (db.getInt(i, "DOCTYPE") == Document.DOCUMENT_CHANNEL) {
						String channelid = String.valueOf(db.getLong(i, "document_id"));
						refChannel = new ChannelManagerImpl().getChannelInfo(channelid);
						// document.setRefChannel(refChannel);
						// document.setDoctype(Document.DOCUMENT_CHANNEL);
						list.add(refChannel);
					} else {/* 文档信息 */
						String docid = String.valueOf(db.getInt(i, "document_id"));

						document.setDocument_id(db.getInt(i, "document_id"));
						document.setTitle(db.getString(i, "title"));
						document.setSubtitle(db.getString(i, "SUBTITLE"));
						document.setStatus(db.getInt(i, "status"));
						document.setAuthor(db.getString(i, "AUTHOR"));
						document.setCreateTime(db.getDate(i, "CREATETIME"));
						document.setDoctype(db.getInt(i, "DOCTYPE"));
						document.setChanel_id(db.getInt(i, "channel_id"));
						document.setLinktarget(db.getString(i, "LINKTARGET"));
						// 出于链接文档的链接地址保存的是在content字段的考虑
						// if(document.getDoctype() == 1)
						document.setLinkfile(db.getString(i, "linkfile"));
						document.setDocsource_id(db.getInt(i, "DOCSOURCE_ID"));

						document.setDocwtime(db.getDate(i, "DOCWTIME"));

						document.setCreateUser(db.getInt(i, "CREATEUSER"));
						document.setDetailtemplate_id(db.getInt(i, "DETAILTEMPLATE_ID"));
						document.setDocabstract(db.getString(i, "DOCABSTRACT"));

						document.setTitlecolor(db.getString(i, "TITLECOLOR"));
						document.setKeywords(db.getString(i, "KEYWORDS"));
						document.setDoc_level(db.getInt(i, "doc_level"));
						document.setParentDetailTpl(db.getString(i, "PARENT_DETAIL_TPL"));
						document.setPicPath(db.getString(i, "PIC_PATH"));
						document.setMediapath(db.getString(i, "mediapath"));
						document.setPublishfilename(db.getString(i, "publishfilename"));
						document.setSecondtitle(db.getString(i, "secondtitle"));

						/* 装载扩展字段数据 */
						Map docExtField = getDocExtFieldMapBean(docid);
						document.setDocExtField(docExtField);
						/* 装载系统扩展字段数据 */
						document.setExtColumn(extManager.getExtColumnInfo(i,db));
						list.add(document);
					}

				}
			}
		} catch (Exception e) {
			System.out.println("error:频道下的子频道和文档的混合列表时出错！");
			e.printStackTrace();
		}
		if (isTurnPage) {
			listinfo.setDatas(list);
			listinfo.setTotalSize(db.getTotalSize());
			return listinfo;
		} else {
			return list;
		}
	}

	public String getPublishfilenameById(String id) {
		String filename = "";
		DBUtil db = new DBUtil();
		StringBuffer sql = new StringBuffer();
		sql.append("select publishfilename from td_cms_document where document_id=").append(id);
		try {
			db.executeSelect(sql.toString());
			if (db.size() > 0) {
				filename = db.getString(0, "publishfilename");
			}
		} catch (Exception e) {

		}
		return filename;
	}

	public void storeOldStatus(ContentContext context, DocumentStatus oldStatus) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		String docid = context.getContentid();
		String status = oldStatus.getStatus();
		String publisherStr = context.getPublisher()[1];
		String publisher = publisherStr.substring(publisherStr.indexOf("[") + 1, publisherStr.indexOf("]"));
		try {
			String sql = "select * from td_cms_doc_publishing where document_id = " + docid;
			db.executeSelect(sql);
			if (db.size() == 0) {
				String sql1 = "insert into td_cms_doc_publishing(document_id,old_status,publisher,pub_start_time) values("
						+ docid
						+ ","
						+ status
						+ ",'"
						+ publisher
						+ "',"
						+ DBUtil.getDBAdapter().to_date(new Date())
						+ ")";
				db.executeInsert(sql1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException("保存文档发布之前的状态时出错！" + e.getMessage());
		}
	}

	public int getOldStatus(String docId) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		try {
			String sql = "select old_status from td_cms_doc_publishing  where document_id = " + docId;
			db.executeSelect(sql);
			if (db.size() > 0) {
				return db.getInt(0, "old_status");
			} else {
				this.clearTask(Integer.parseInt(docId)); // 删除所有的任务是不合理的，暂时这样。
				return 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException("保存文档发布之前的状态时出错！" + e.getMessage());
		}
	}

	public void deleteOldStatus(String docId) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		try {
			String sql = "delete from td_cms_doc_publishing where document_id = " + docId;
			db.executeDelete(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException("保存文档发布之前的状态时出错！" + e.getMessage());
		}
	}

	public void deleteOldStatus(ContentContext context, String docId) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		try {
			String sql = "delete from td_cms_doc_publishing where document_id = " + docId;
			db.executeDelete(context.getDBName(), sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException("保存文档发布之前的状态时出错！" + e.getMessage());
		}
	}

	/**
	 * 获得指定表名的所有列信息
	 * 
	 * @param tableName
	 * @return
	 */
	public List getTableColumnsInfo(String tableName) {
		List list = new ArrayList();
		tableName = tableName.toLowerCase();
		StringBuffer sql = new StringBuffer();
		// select
		// a.table_name,a.column_name,a.data_type,a.data_length,b.comments from
		// USER_TAB_COLUMNS a, USER_COL_COMMENTS b where
		// a.TABLE_NAME=b.table_name and
		// a.COLUMN_NAME=b.column_name and lower(a.TABLE_NAME)='test'
		sql.append("select a.table_name,a.column_name,a.data_type,a.data_length,b.comments  from ");
		sql.append("USER_TAB_COLUMNS a, USER_COL_COMMENTS b where  a.TABLE_NAME=b.table_name and ");
		sql.append("a.COLUMN_NAME=b.column_name and lower(a.TABLE_NAME)='").append(tableName).append("'");
		String sqlstr = sql.toString();
		TableMetaData table = DBUtil.getTableMetaData(tableName);
		Set columns = table.getColumns();
		Iterator iter = columns.iterator();
		while (iter.hasNext()) {
			ColumnMetaData col = (ColumnMetaData) iter.next();
			String column_name = col.getColumnName();
			String data_type = col.getTypeName();
			String data_length = String.valueOf(col.getColunmSize());
			String comments = col.getRemarks();
			String[] ob = new String[] { column_name, data_type, data_length, comments };
			list.add(ob);
		}
		return list;
	}

	/**
	 * 不翻页 --ge.tao 获取文档的相关附件 kaihu 070601
	 * 
	 * @return List<Attachment>
	 * @throws DocumentManagerException
	 */
	public List getRelationAttachmentsOfDocument(int docid, int count) throws DocumentManagerException {
		List list = new ArrayList();
		DBUtil db = new DBUtil();
		String sql = "select * from TD_CMS_DOC_ATTACH t where " + (count > 0 ? " t.rownum>" + count + " and " : "")
				+ " t.type=2 and t.valid=1 and t.document_id=" + docid;
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					Attachment att = new Attachment();
					att.setId(db.getInt(i, "id"));
					att.setDocumentId(db.getInt(i, "document_id"));
					att.setUrl(db.getString(i, "url"));
					att.setType(db.getInt(i, "type"));
					att.setDescription(db.getString(i, "description"));
					att.setOriginalFilename(db.getString(i, "original_filename"));
					att.setValid(db.getString(i, "valid"));

					list.add(att);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return list;
	}

	/**
	 * 翻页 --ge.tao 获取文档的相关附件 kaihu 070601
	 * 
	 * @return List<Attachment>
	 * @throws DocumentManagerException
	 */
	public ListInfo getRelationAttachmentsOfDocument(int docid, long offset, int pageitems)
			throws DocumentManagerException {
		ListInfo listinfo = new ListInfo();
		List list = new ArrayList();
		DBUtil db = new DBUtil();
		String sql = "select * from TD_CMS_DOC_ATTACH t where  t.type=2 and t.valid=1 and t.document_id=" + docid;
		try {
			db.executeSelect(sql, offset, pageitems);
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					Attachment att = new Attachment();
					att.setId(db.getInt(i, "id"));
					att.setDocumentId(db.getInt(i, "document_id"));
					att.setUrl(db.getString(i, "url"));
					att.setType(db.getInt(i, "type"));
					att.setDescription(db.getString(i, "description"));
					att.setOriginalFilename(db.getString(i, "original_filename"));
					att.setValid(db.getString(i, "valid"));
					list.add(att);
				}
			}
			listinfo.setDatas(list);
			listinfo.setTotalSize(db.getTotalSize());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return listinfo;
	}

	/**
	 * 翻页 --ge.tao
	 * 获取文档的相关附件 kaihu 070601
	 * @return List<Attachment>
	 * @throws DocumentManagerException
	 */
	public ListInfo getRelationAttachmentsOfDocument(final Document doc, long offset, int pageitems)
			throws DocumentManagerException {

		String sql = "select * from TD_CMS_DOC_ATTACH t where  t.type=2 and t.valid=1 and t.document_id=?";

		try {
			return SQLExecutor.queryListInfoByRowHandler(new RowHandler<Attachment>() {

				@Override
				public void handleRow(Attachment att, Record db) throws Exception {

					att.setId(db.getInt("id"));
					att.setDocumentId(db.getInt("document_id"));
					att.setUrl(db.getString("url"));
					att.setType(db.getInt("type"));
					att.setDescription(db.getString("description"));
					att.setOriginalFilename(db.getString("original_filename"));
					att.setValid(db.getString("valid"));
					att.setDocument(doc);
				}
			}, Attachment.class, sql, offset, pageitems, doc.getDocument_id());

		} catch (Exception e) {

			throw new DocumentManagerException(e.getMessage());
		}

	}

	/**
	 * 不翻页 --ge.tao
	 * 获取文档的相关附件 kaihu 070601
	 * @return List<Attachment>
	 * @throws DocumentManagerException
	 */
	public List getRelationAttachmentsOfDocument(final Document doc, int count) throws DocumentManagerException {
		if (count > 0) {
			String sql = "select * from TD_CMS_DOC_ATTACH t where  t.rownum>?"
					+ " and t.type=2 and t.valid=1 and t.document_id=?";
			try {
				return SQLExecutor.queryListByRowHandler(new RowHandler<Attachment>() {

					@Override
					public void handleRow(Attachment att, Record db) throws Exception {

						att.setId(db.getInt("id"));
						att.setDocumentId(db.getInt("document_id"));
						att.setUrl(db.getString("url"));
						att.setType(db.getInt("type"));
						att.setDescription(db.getString("description"));
						att.setOriginalFilename(db.getString("original_filename"));
						att.setValid(db.getString("valid"));
						att.setDocument(doc);
					}
				}, Attachment.class, sql, count, doc.getDocument_id());

			} catch (Exception e) {

				throw new DocumentManagerException(e.getMessage());
			}
		} else {
			String sql = "select * from TD_CMS_DOC_ATTACH t where " + " t.type=2 and t.valid=1 and t.document_id=?";
			try {
				return SQLExecutor.queryListByRowHandler(new RowHandler<Attachment>() {

					@Override
					public void handleRow(Attachment att, Record db) throws Exception {

						att.setId(db.getInt("id"));
						att.setDocumentId(db.getInt("document_id"));
						att.setUrl(db.getString("url"));
						att.setType(db.getInt("type"));
						att.setDescription(db.getString("description"));
						att.setOriginalFilename(db.getString("original_filename"));
						att.setValid(db.getString("valid"));
						att.setDocument(doc);
					}
				}, Attachment.class, sql, doc.getDocument_id());

			} catch (Exception e) {

				throw new DocumentManagerException(e.getMessage());
			}
		}

	}

	/**
	 * 
	 * @param channel_id
	 * @param docPublishName
	 * @return boolean DocumentManagerImpl.java 陶格
	 */
	public boolean isExistDocumentInChannel(long channel_id, String docPublishName) {
		StringBuffer sqlstr = new StringBuffer();
		boolean flag = false;
		sqlstr.append("select * from td_cms_document t where t.channel_id=").append(channel_id)
				.append(" and t.publishfilename='").append(docPublishName).append("' and t.status=5 ");
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sqlstr.toString());
			if (db.size() > 0) {
				flag = true;
			}
		} catch (Exception e) {
		}
		return flag;
	}

	public void addDocTPL(DocTemplate docTPL) throws DocumentManagerException {
		PreparedDBUtil preparedDB = new PreparedDBUtil();
		String sql = "insert into td_cms_doc_template(tplname,tplcode,channel_id,creator,description,creattime,id) values(?,?,?,?,?,?,?)";
		try {

			long docTempId = preparedDB.getNextPrimaryKey("td_cms_doc_template");

			preparedDB.preparedInsert(sql);
			preparedDB.setString(1, docTPL.getTplName());
			preparedDB.setClob(2, docTPL.getTplCode(), "tplcode");
			preparedDB.setLong(3, docTPL.getChnlId());
			preparedDB.setInt(4, docTPL.getCreateUser());
			preparedDB.setString(5, docTPL.getDescription());
			preparedDB.setTimestamp(6, new Timestamp(new Date().getTime()));
			preparedDB.setLong(7, docTempId);
			preparedDB.executePrepared();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException("文档模板插入失败！" + e.getMessage());
		} finally {
			preparedDB.resetPrepare();
		}
	}

	public void updateDocTPL(DocTemplate docTPL) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		// PreparedDBUtil preparedDB = new PreparedDBUtil();
		String sql = "update td_cms_doc_template set tplname='" + docTPL.getTplName() + "'," + "tplcode='"
				+ docTPL.getTplCode() + "',channel_id=" + docTPL.getChnlId() + "," + "description='"
				+ docTPL.getDescription() + "' where id = " + docTPL.getDocTplId();
		try {
			// preparedDB.preparedUpdate(sql);
			// preparedDB.setString(1, docTPL.getTplName());
			// preparedDB.setClob(2, docTPL.getTplCode(), "tplcode");
			// preparedDB.setLong(3,docTPL.getChnlId());
			// preparedDB.setString(4,docTPL.getDescription());
			// preparedDB.executePrepared();
			db.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException("文档采集模板更新失败！" + e.getMessage());
		}
	}

	public void deleteDocTPL(String docTPLId) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		String sql = "delete from td_cms_doc_template where id = " + docTPLId;
		try {
			db.executeDelete(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException("文档模板删除失败！" + e.getMessage());
		}

	}

	public List getAllDocTPLList(String chnlId) throws DocumentManagerException {
		ArrayList list = new ArrayList();
		PreparedDBUtil db = new PreparedDBUtil();
		// String sql = "select * from td_cms_doc_template where channel_id = "
		// + chnlId;
		
		try {
			String sql = "";
			if ("".equals(chnlId)) {
				sql = "select * from td_cms_doc_template";
				db.preparedSelect(sql);
			} else {
				sql = "select * from td_cms_doc_template where channel_id = ?" ;
				db.preparedSelect(sql);
				db.setInt(1, Integer.parseInt(chnlId));
			}
			db.executePrepared();
			if (db.size() > 0) {
				for (int i = 0; i < db.size(); i++) {
					DocTemplate docTPL = new DocTemplate();
					docTPL.setDocTplId(db.getInt(i, "id"));
					docTPL.setChnlId(db.getLong(i, "channel_id"));
					docTPL.setCreateUser(db.getInt(i, "creator"));
					docTPL.setTplName(db.getString(i, "tplname"));
					docTPL.setTplCode(db.getString(i, "tplcode"));
					docTPL.setDescription(db.getString(i, "description"));
					docTPL.setCreateTime(db.getDate(i, "creattime"));
					list.add(docTPL);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException("获取指定频道下所有文档模板列表失败！" + e.getMessage());
		}
		return list;
	}

	public List getDocTPLList(String chnlId, String userId) throws DocumentManagerException {
		ArrayList list = new ArrayList();
		DBUtil db = new DBUtil();
		String sql = "select * from td_cms_doc_template where channel_id = " + chnlId + " and creator = " + userId;
		try {
			db.executeSelect(sql);
			for (int i = 0; i < db.size(); i++) {
				DocTemplate docTPL = new DocTemplate();
				docTPL.setDocTplId(db.getInt(i, "id"));
				docTPL.setChnlId(db.getLong(i, "channel_id"));
				docTPL.setCreateUser(db.getInt(i, "creator"));
				docTPL.setTplName(db.getString(i, "tplname"));
				docTPL.setTplCode(db.getString(i, "tplcode"));
				docTPL.setDescription(db.getString(i, "description"));
				docTPL.setCreateTime(db.getDate(i, "creattime"));
				list.add(docTPL);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException("获取指定频道下所有文档模板列表失败！" + e.getMessage());
		}
		return list;
	}

	public ListInfo getDocTPLList(String sql, long offset, int pageitems) throws DocumentManagerException {
		ArrayList list = new ArrayList();
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		ListInfo listinfo = new ListInfo();
		String sql1 = "";
		try {
			db.executeSelect(sql, (int) offset, pageitems);
			for (int i = 0; i < db.size(); i++) {
				DocTemplate docTPL = new DocTemplate();
				docTPL.setDocTplId(db.getInt(i, "id"));
				long chnlId = db.getLong(i, "channel_id");
				docTPL.setChnlId(chnlId);

				sql1 = "select display_name from td_cms_channel where channel_id = " + chnlId;
				db1.executeSelect(sql1);
				if (db1.size() > 0)
					docTPL.setChnlName(db1.getString(0, "display_name"));

				int createUser = db.getInt(i, "creator");
				docTPL.setCreateUser(createUser);
				sql1 = "select user_name from td_sm_user where user_id = " + createUser;
				db1.executeSelect(sql1);
				if (db1.size() > 0)
					docTPL.setCreateUserName(db1.getString(0, "user_name"));

				docTPL.setTplName(db.getString(i, "tplname"));
				docTPL.setTplCode(db.getString(i, "tplcode"));
				docTPL.setDescription(db.getString(i, "description"));
				docTPL.setCreateTime(db.getDate(i, "creattime"));
				list.add(docTPL);
			}
			listinfo.setDatas(list);
			listinfo.setTotalSize(db.getTotalSize());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException("获取指定频道下所有文档模板列表失败！" + e.getMessage());
		}
		return listinfo;
	}

	public void deleteDocTPLs(String[] docTPLIds) throws DocumentManagerException {
		try {
			for (int i = 0; i < docTPLIds.length; i++)
				this.deleteDocTPL(docTPLIds[i]);
		} catch (DocumentManagerException de) {
			throw de;
		}
	}

	public DocTemplate getDocTPL(String docTPLId) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		String sql = "select * from td_cms_doc_template where id = " + docTPLId;
		DocTemplate docTpl = new DocTemplate();
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				docTpl.setDocTplId(db.getInt(0, "id"));
				long chnlId = db.getLong(0, "channel_id");
				docTpl.setChnlId(chnlId);
				docTpl.setCreateUser(db.getInt(0, "creator"));
				docTpl.setTplName(db.getString(0, "tplname"));
				docTpl.setTplCode(db.getString(0, "tplcode"));
				docTpl.setDescription(db.getString(0, "description"));
				docTpl.setCreateTime(db.getDate(0, "creattime"));

				String sql1 = "select display_name from td_cms_channel where channel_id = " + chnlId;
				db1.executeSelect(sql1);
				if (db1.size() > 0)
					docTpl.setChnlName(db1.getString(0, "display_name"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException("获取指定频道下所有文档模板列表失败！" + e.getMessage());
		}
		return docTpl;
	}

	public ListInfo getChnlListOfDocCited(String docId, int offset, int maxitem) throws DocumentManagerException {
		ListInfo chnlList = new ListInfo();
		List list = new ArrayList();
		DBUtil db = new DBUtil();

		String sql = "select a.op_time as citeTime,a.op_user_id as op_user_id,"
				+ "b.display_name as display_name,b.channel_id as channel_id,"
				+ "c.name as siteName,d.user_name as user_name  "
				+ "from td_cms_chnl_ref_doc a,td_cms_channel b,td_cms_site c,td_sm_user d "
				+ "where a.chnl_id = b.channel_id and b.site_id = c.site_id and d.user_id=a.op_user_id and a.doc_id = "
				+ docId;
		try {
			db.executeSelect(sql, offset, maxitem);
			for (int i = 0; i < db.size(); i++) {
				CitedDocument citeDoc = new CitedDocument();
				citeDoc.setCiteTime(db.getDate(i, "citeTime"));
				citeDoc.setCiteUserid(db.getInt(i, "op_user_id"));
				citeDoc.setCurChannelName(db.getString(i, "display_name"));
				citeDoc.setCurChannelid(db.getInt(i, "channel_id"));
				citeDoc.setCurSiteName(db.getString(i, "siteName"));
				citeDoc.setCiteUserName(db.getString(i, "user_name"));
				list.add(citeDoc);
			}
			chnlList.setDatas(list);
			chnlList.setTotalSize(db.getTotalSize());
			return chnlList;
		} catch (Exception e) {
			System.out.print("查看文档引用情况时出错!");
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
	}

	public int recoverPublishingDoc(String docId) throws DocumentManagerException {
		try {
			if (!APPPublish.isRealPublishing(docId)) {
				int newStatus = this.getOldStatus(docId);
				this.updateDocumentStatus(Integer.parseInt(docId.trim()), newStatus);
				this.deleteOldStatus(docId.trim());
				return 1;
			} else {
				return 2;
			}
		} catch (DocumentManagerException dm) {
			throw dm;
		}
	}

	/**
	 * 恢复发布中的文档, 返回true：恢复操作成功；false：恢复失败，数据库操作失败；
	 * 
	 * @param docId
	 * @throws DocumentManagerException
	 *             add by xinwang.jiao 2007.8.21
	 */
	public boolean recoverAllPublishingDoc() throws DocumentManagerException {
		boolean flag = false;
		DBUtil db = new DBUtil();
		StringBuffer sb = new StringBuffer();
		String sql = sb.append("update td_cms_document a set( a.status) = ")
				.append("(select old_status from td_cms_doc_publishing b where a.document_id = b.document_id) ")
				.append("where exists(select 1 from td_cms_doc_publishing c where c.document_id = a.document_id)")
				.toString();
		try {
			db.executeUpdate(sql);
			sql = "delete from td_cms_doc_publishing ";
			db.executeDelete(sql);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentManagerException(e.getMessage());
		}
		return flag;
	}

	public void recoverPublishingDocs(String docIds[]) throws DocumentManagerException {
		try {
			for (int i = 0; i < docIds.length; i++)
				this.recoverPublishingDoc(docIds[i]);
		} catch (DocumentManagerException dm) {
			throw dm;
		}

	}

	/**
	 * 获取当前用户的任务数, flag:0-审核任务数；1-新稿文档数;2-返工文档数;3-待发布任务数;
	 */
	public int getTaskCount(String curUserid, int flag) throws DocumentManagerException {
		if (curUserid == null || curUserid.equals("") || curUserid.equals("null"))
			return 0;
		int count = 0;
		DBUtil db = new DBUtil();
		String sql = "";
		switch (flag) {
		case 0:
			sql = "select count(*) "
					+ "from td_cms_doc_task a, td_cms_doc_task_detail b, "
					+ "td_cms_document c,td_cms_channel e,td_cms_site f  "
					+ "where a.pre_status=2 and c.isdeleted!= 1 and c.status=2 and a.task_id=b.task_id and c.document_id=a.document_id "
					+ "and e.channel_id=c.channel_id  and f.site_id=e.site_id and (f.status=0 or f.status=1) "
					+ "and b.performer = " + curUserid + " and b.valid = 1 and complete_time is null ";
			break;
		case 1:
			sql = "select count(*) " + "from td_cms_document a,td_cms_channel c,td_cms_site d "
					+ "where  a.isdeleted != 1 and a.status = 1 and a.createuser = " + curUserid
					+ " and a.channel_id = c.channel_id and c.site_id = d.site_id and (d.status = 0 or  d.status = 1) ";
			break;
		case 2:
			sql = "select count(*) "
					+ "from td_cms_doc_task a, td_cms_doc_task_detail b, "
					+ "td_cms_document c,td_cms_channel e,td_cms_site f "
					+ "where c.isdeleted!=1 and a.pre_status=4 and c.status=4 and a.task_id=b.task_id and c.document_id=a.document_id "
					+ "and e.channel_id=c.channel_id and f.site_id=e.site_id and (f.status=0 or f.status=1) "
					+ "and b.performer = " + curUserid + " and b.valid = 1 and complete_time is null ";
			break;
		case 3:
			sql = "select count(*) "
					+ "from td_cms_doc_task a, td_cms_doc_task_detail b, "
					+ "td_cms_document c ,td_cms_channel e,td_cms_site f "
					+ "where c.isdeleted!=1 and a.pre_status=11 and c.status=11 and a.task_id=b.task_id and c.document_id=a.document_id "
					+ "and e.channel_id=c.channel_id and f.site_id=e.site_id and (f.status=0 or f.status=1) "
					+ "and b.performer = " + curUserid + " and b.valid = 1 and complete_time is null ";
			break;
		}
		try {
			db.executeSelect(sql);
			if (db.size() > 0)
				count = db.getInt(0, "count(*)");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException("获取当前用户的待发文档数失败。详细信息为：" + e.getMessage());
		}
		return count;
	}

	/**
	 * 更新频道下继承原来频道模板的文档模板 DocumentManager.java
	 * 
	 * @author: ge.tao
	 */
	public void updatePreviousDocInChannel(String channelId, String tmpeleteId) throws DocumentManagerException {
		DBUtil db = new DBUtil();
		StringBuffer sqlstr = new StringBuffer();
		sqlstr.append(" update TD_CMS_DOCUMENT set DETAILTEMPLATE_ID=").append(tmpeleteId).append(" where CHANNEL_ID=")
				.append(channelId).append(" and PARENT_DETAIL_TPL='1' ");
		try {
			db.executeUpdate(sqlstr.toString());
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException("获取当前用户的待发文档数失败。详细信息为：" + e.getMessage());
		}
	}

	/**
	 * 频道下采集的文档 DocumentManager.java
	 * 
	 * @author: ge.tao
	 */
	public boolean channelHasDoc(String channelId) throws DocumentManagerException {
		boolean flag = false;
		DBUtil db = new DBUtil();
		StringBuffer sqlstr = new StringBuffer();
		sqlstr.append(" select count(*) from TD_CMS_DOCUMENT ").append(" where CHANNEL_ID=").append(channelId)
				.append(" and (DOCTYPE=0 or DOCTYPE=3 ) ");
		try {
			db.executeSelect(sqlstr.toString());
			if (db.size() > 0) {
				if (db.getInt(0, 0) > 0) {
					flag = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocumentManagerException("获取当前用户的待发文档数失败。详细信息为：" + e.getMessage());
		}
		return flag;
	}

	/**
	 * 根据文档关键字获取文档所属的站点中所有已发文档中标题, 摘要, 关键字(内容 现在不要)包含本关键字的文档列表 翻页
	 * 
	 * @param docid
	 *            文档ID int
	 * @param offset
	 * @param pageitems
	 * @return
	 * @throws DocumentManagerException
	 *             DocumentManager.java
	 * @author: ge.tao
	 */
	public ListInfo getKeyWordRelationOfDocument(String keywords_, int docid, String siteid, long offset, int pageitems)
			throws DocumentManagerException {
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		ListInfo listinfo = new ListInfo();
		Document doc = this.getPartDocInfoById(docid + "");
		List list = new ArrayList();
		if (keywords_ == null) {
			keywords_ = doc.getKeywords();
		}
		if (keywords_ != null && keywords_.trim().length() > 0) {
			DBUtil db = new DBUtil();
			StringBuffer sql = new StringBuffer();

			String[] keywords = null;
			sql.append("select * from (")
					.append("select document_id,title,SUBTITLE,t.status as status,AUTHOR,isdeleted,  ")
					.append("CREATETIME, DOCTYPE, case when DOCTYPE=1 ")
					.append("then t.content else null end linkfile,channel_id,LINKTARGET,t.DOCSOURCE_ID,t.publishtime, ")
					.append("DOCWTIME,CREATEUSER,t.DETAILTEMPLATE_ID,DOCABSTRACT,TITLECOLOR,")
					.append("KEYWORDS,doc_level,PARENT_DETAIL_TPL,PIC_PATH,mediapath,publishfilename,secondtitle, ")
					.append("isnew,newpic_path,ordertime,")
					.append("ds.SRCNAME as SRCNAME, tmpl.NAME as NAME, ch.SITE_ID as SITE_ID ")
					.append("from TD_CMS_DOCUMENT t inner join TD_CMS_DOCSOURCE ds on ds.DOCSOURCE_ID=t.DOCSOURCE_ID ")
					.append("left join TD_CMS_TEMPLATE tmpl on tmpl.TEMPLATE_ID=t.DETAILTEMPLATE_ID left join TD_CMS_CHANNEL ch ")
					.append("on ch.CHANNEL_ID=t.CHANNEL_ID )").append(" un where un.document_id != ").append(docid)
					.append(" and un.status in (").append(DocumentStatus.PUBLISHED.getStatus()).append(",")
					.append(DocumentStatus.PUBLISHING.getStatus()).append(")  and un.isdeleted =0 and un.SITE_ID=")
					.append(siteid).append(" ");

			keywords = keywords_.split(",");
			String subsql = "";
			for (int i = 0; i < keywords.length; i++) {
				if (keywords[i].trim().length() == 0)
					continue;
				// 2007-11-28
				// 北湖需求, 只匹配文档标题
				if ("".equalsIgnoreCase(subsql.trim())) {
					subsql += " un.title like '%" + keywords[i].trim() + "%' ";
					// " or un.DOCABSTRACT like '%" +
					// keywords[i].trim() + "%' or un.KEYWORDS like '%" +
					// keywords[i].trim() + "%'" ;
				} else {
					subsql += " or un.title like '%" + keywords[i].trim() + "%' ";
					// " or un.DOCABSTRACT like '%" +
					// keywords[i].trim() + "%' or un.KEYWORDS like '%" +
					// keywords[i].trim() + "%'" ;
				}
			}
			if (subsql.trim().length() > 0) {
				subsql = " and (" + subsql + " ) ";
			}
			sql.append(subsql);
			// 按发布时间排序
			sql.append(" order by un.ordertime desc,un.DOCWTIME desc,un.publishtime desc ");
			log.warn(sql.toString());
			try {
				// 整个站点下的跟该文档关键字相关联的所有文档 翻页
				if (pageitems <= 0) {
					db.executeSelect(sql.toString());
				} else {
					db.executeSelect(sql.toString(), offset, pageitems);
				}

				ListInfo newestlistinfo = null;
				if (db.size() == 0) {// 整个站点下 没有 跟该文档关键字相关联的所有文档,
					// 获取该文档所在频道的最新发布的文档, 不包括自己 翻页
					newestlistinfo = CMSUtil
							.getCMSDriverConfiguration()
							.getCMSService()
							.getChannelManager()
							.getLatestPubAndPubingDocListByFilter(doc.getSiteid() + "", doc.getChannelName(),
									(int) offset, pageitems, new String[] { docid + "" });

					return newestlistinfo;
				} else {
					for (int i = 0; i < db.size(); i++) {
						Document document = new Document();
						document.setDocument_id(db.getInt(i, "document_id"));
						document.setTitle(db.getString(i, "title"));
						document.setSubtitle(db.getString(i, "SUBTITLE"));
						document.setStatus(db.getInt(i, "status"));
						document.setDocflag(db.getInt(i, "isdeleted"));
						document.setAuthor(db.getString(i, "AUTHOR"));
						document.setCreateTime(db.getDate(i, "CREATETIME"));
						document.setDoctype(db.getInt(i, "DOCTYPE"));
						// document.setContent (db.getString(i,"CONTENT"));
						document.setChanel_id(db.getInt(i, "channel_id"));
						document.setLinktarget(db.getString(i, "LINKTARGET"));
						// 出于链接文档的链接地址保存的是在content字段的考虑
						if (document.getDoctype() == 1)
							document.setLinkfile(document.getContent());
						document.setDocsource_id(db.getInt(i, "DOCSOURCE_ID"));

						document.setDocwtime(db.getDate(i, "DOCWTIME"));

						document.setCreateUser(db.getInt(i, "CREATEUSER"));
						document.setDetailtemplate_id(db.getInt(i, "DETAILTEMPLATE_ID"));
						document.setDocabstract(db.getString(i, "DOCABSTRACT"));

						document.setTitlecolor(db.getString(i, "TITLECOLOR"));
						document.setKeywords(db.getString(i, "KEYWORDS"));
						document.setDoc_level(db.getInt(i, "doc_level"));
						document.setParentDetailTpl(db.getString(i, "PARENT_DETAIL_TPL"));
						document.setPicPath(db.getString(i, "PIC_PATH"));
						document.setMediapath(db.getString(i, "mediapath"));
						document.setPublishfilename(db.getString(i, "publishfilename"));
						document.setSecondtitle(db.getString(i, "secondtitle"));
						document.setIsNew(db.getInt(i, "isnew"));
						document.setNewPicPath(db.getString(i, "newpic_path"));
						document.setOrdertime(db.getDate(i, "ordertime"));
						/* 装载扩展字段数据 */
						document.setExtColumn(extManager.getExtColumnInfo(i,db));
						document.setDocsource_name(db.getString(i, "SRCNAME"));
						document.setDetailtemplate_name(db.getString(i, "NAME"));
						list.add(document);
						// 记录递归发布关系

					}
				}
				listinfo.setDatas(list);
				listinfo.setTotalSize(db.getTotalSize());
			} catch (Exception e) {
				e.printStackTrace();
				throw new DocumentManagerException(e.getMessage());
			}
		}
		return listinfo;

	}

	/**
	 * 获取关键字与本文档相匹配的已发布文档
	 */
	public ListInfo getKeyWord2RelationOfDocument(String title, int docid, String channel, String siteid, long offset,
			int pageitems) throws DocumentManagerException {
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		ListInfo listinfo = new ListInfo();

		Document doc = this.getPartDocInfoById(docid + "");
		List list = new ArrayList();
		if (title == null) {
			title = doc.getTitle();
		}
		if (title != null && title.trim().length() > 0) {
			DBUtil db = new DBUtil();
			StringBuffer sql = new StringBuffer();

			sql.append("select * from (")
					.append("select document_id,title,SUBTITLE,t.status as status,AUTHOR,isdeleted,  ")
					.append("CREATETIME, DOCTYPE, case when DOCTYPE=1 ")
					.append("then t.content else null end linkfile,channel_id,LINKTARGET,t.DOCSOURCE_ID,t.publishtime, ")
					.append("DOCWTIME,CREATEUSER,t.DETAILTEMPLATE_ID,DOCABSTRACT,TITLECOLOR,")
					.append("KEYWORDS,doc_level,PARENT_DETAIL_TPL,PIC_PATH,mediapath,publishfilename,secondtitle, ")
					.append("isnew,newpic_path,ordertime,")
					.append("ds.SRCNAME as SRCNAME, tmpl.NAME as NAME, ch.SITE_ID as SITE_ID ")
					.append("from TD_CMS_DOCUMENT t inner join TD_CMS_DOCSOURCE ds on ds.DOCSOURCE_ID=t.DOCSOURCE_ID ")
					.append("left join TD_CMS_TEMPLATE tmpl on tmpl.TEMPLATE_ID=t.DETAILTEMPLATE_ID left join TD_CMS_CHANNEL ch ")
					.append("on ch.CHANNEL_ID=t.CHANNEL_ID order by ordertime desc )")
					.append(" un where un.document_id != ").append(docid).append(" and un.status in (")
					.append(DocumentStatus.PUBLISHED.getStatus()).append(",")
					.append(DocumentStatus.PUBLISHING.getStatus()).append(")  and un.isdeleted =0 and un.SITE_ID=")
					.append(siteid).append(" ");

			String subsql = "";
			subsql += " un.author like '%" + title.trim()
					+ "%' and channel_id=(select channel_id from TD_CMS_CHANNEL where display_name='" + channel + "')";
			if (subsql.trim().length() > 0) {
				subsql = " and (" + subsql + " ) ";
			}
			sql.append(subsql);
			// 按发布时间排序
			// sql.append(" order by un.ordertime desc");
			log.warn(sql.toString());
			System.out.println(sql.toString());
			try {
				// 整个站点下的跟该文档关键字相关联的所有文档 翻页
				db.executeSelect(sql.toString(), offset, pageitems);
				List newestlist = new ArrayList();
				ListInfo newestlistinfo = null;
				if (db.size() == 0) {// 整个站点下 没有 跟该文档关键字相关联的所有文档,
					// 获取该文档所在频道的最新发布的文档, 不包括自己 翻页
					// newestlistinfo = CMSUtil.getCMSDriverConfiguration()
					// .getCMSService().getChannelManager()
					// .getLatestPubAndPubingDocList(doc.getSiteid() + "",
					// doc.getChannelName(), (int) offset,
					// pageitems);
					// // 去掉最新的 就是当前发布的文档
					// if (newestlistinfo != null) {
					// newestlist = newestlistinfo.getDatas();
					// if (newestlist != null && newestlist.size() > 0)
					// newestlist.remove(0);
					// newestlistinfo.setDatas(newestlist);
					// }
					// return newestlistinfo;
				} else {
					for (int i = 0; i < db.size(); i++) {
						Document document = new Document();
						document.setDocument_id(db.getInt(i, "document_id"));
						document.setTitle(db.getString(i, "title"));
						document.setSubtitle(db.getString(i, "SUBTITLE"));
						document.setStatus(db.getInt(i, "status"));
						document.setDocflag(db.getInt(i, "isdeleted"));
						document.setAuthor(db.getString(i, "AUTHOR"));
						document.setCreateTime(db.getDate(i, "CREATETIME"));
						document.setDoctype(db.getInt(i, "DOCTYPE"));
						// document.setContent (db.getString(i,"CONTENT"));
						document.setChanel_id(db.getInt(i, "channel_id"));
						document.setLinktarget(db.getString(i, "LINKTARGET"));
						// 出于链接文档的链接地址保存的是在content字段的考虑
						if (document.getDoctype() == 1)
							document.setLinkfile(document.getContent());
						document.setDocsource_id(db.getInt(i, "DOCSOURCE_ID"));

						document.setDocwtime(db.getDate(i, "DOCWTIME"));

						document.setCreateUser(db.getInt(i, "CREATEUSER"));
						document.setDetailtemplate_id(db.getInt(i, "DETAILTEMPLATE_ID"));
						document.setDocabstract(db.getString(i, "DOCABSTRACT"));

						document.setTitlecolor(db.getString(i, "TITLECOLOR"));
						document.setKeywords(db.getString(i, "KEYWORDS"));
						document.setDoc_level(db.getInt(i, "doc_level"));
						document.setParentDetailTpl(db.getString(i, "PARENT_DETAIL_TPL"));
						document.setPicPath(db.getString(i, "PIC_PATH"));
						document.setMediapath(db.getString(i, "mediapath"));
						document.setPublishfilename(db.getString(i, "publishfilename"));
						document.setSecondtitle(db.getString(i, "secondtitle"));
						document.setIsNew(db.getInt(i, "isnew"));
						document.setNewPicPath(db.getString(i, "newpic_path"));
						document.setOrdertime(db.getDate(i, "ordertime"));
						/* 装载扩展字段数据 */
						document.setExtColumn(extManager.getExtColumnInfo(i,db));
						document.setDocsource_name(db.getString(i, "SRCNAME"));
						document.setDetailtemplate_name(db.getString(i, "NAME"));
						list.add(document);
						// 记录递归发布关系

					}
				}
				listinfo.setDatas(list);
				listinfo.setTotalSize(db.getTotalSize());
			} catch (Exception e) {
				e.printStackTrace();
				throw new DocumentManagerException(e.getMessage());
			}
		}
		return listinfo;

	}

	/**
	 * 获取关键字与本文档相匹配的已发布文档
	 */
	public ListInfo getKeyWord3RelationOfDocument(String title, int docid, String channel, String siteid, long offset,
			int pageitems) throws DocumentManagerException {
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		ListInfo listinfo = new ListInfo();

		Document doc = this.getPartDocInfoById(docid + "");
		List list = new ArrayList();
		if (title == null) {
			title = doc.getTitle();
		}
		if (title != null && title.trim().length() > 0) {
			DBUtil db = new DBUtil();
			StringBuffer sql = new StringBuffer();

			sql.append("select * from (")
					.append("select document_id,title,SUBTITLE,t.status as status,AUTHOR,isdeleted,  ")
					.append("CREATETIME, DOCTYPE, case when DOCTYPE=1 ")
					.append("then t.content else null end linkfile,channel_id,LINKTARGET,t.DOCSOURCE_ID,t.publishtime, ")
					.append("DOCWTIME,CREATEUSER,t.DETAILTEMPLATE_ID,DOCABSTRACT,TITLECOLOR,")
					.append("KEYWORDS,doc_level,PARENT_DETAIL_TPL,PIC_PATH,mediapath,publishfilename,secondtitle, ")
					.append("isnew,newpic_path,ordertime,")
					.append("ds.SRCNAME as SRCNAME, tmpl.NAME as NAME, ch.SITE_ID as SITE_ID ")
					.append("from TD_CMS_DOCUMENT t inner join TD_CMS_DOCSOURCE ds on ds.DOCSOURCE_ID=t.DOCSOURCE_ID ")
					.append("left join TD_CMS_TEMPLATE tmpl on tmpl.TEMPLATE_ID=t.DETAILTEMPLATE_ID left join TD_CMS_CHANNEL ch ")
					.append("on ch.CHANNEL_ID=t.CHANNEL_ID order by ordertime desc )")
					.append(" un where un.document_id != ").append(docid).append(" and un.status in (")
					.append(DocumentStatus.PUBLISHED.getStatus()).append(",")
					.append(DocumentStatus.PUBLISHING.getStatus()).append(")  and un.isdeleted =0 and un.SITE_ID=")
					.append(siteid).append(" ");

			String subsql = "";
			subsql += " un.keywords like '%" + title.trim()
					+ "%' and channel_id=(select channel_id from TD_CMS_CHANNEL where display_name='" + channel + "')";
			if (subsql.trim().length() > 0) {
				subsql = " and (" + subsql + " ) ";
			}
			sql.append(subsql);
			// 按发布时间排序
			// sql.append(" order by un.ordertime desc");
			log.warn(sql.toString());
			System.out.println(sql.toString());
			try {
				// 整个站点下的跟该文档关键字相关联的所有文档 翻页
				db.executeSelect(sql.toString(), offset, pageitems);
				List newestlist = new ArrayList();
				ListInfo newestlistinfo = null;
				if (db.size() == 0) {// 整个站点下 没有 跟该文档关键字相关联的所有文档,
					// 获取该文档所在频道的最新发布的文档, 不包括自己 翻页
					// newestlistinfo = CMSUtil.getCMSDriverConfiguration()
					// .getCMSService().getChannelManager()
					// .getLatestPubAndPubingDocList(doc.getSiteid() + "",
					// doc.getChannelName(), (int) offset,
					// pageitems);
					// // 去掉最新的 就是当前发布的文档
					// if (newestlistinfo != null) {
					// newestlist = newestlistinfo.getDatas();
					// if (newestlist != null && newestlist.size() > 0)
					// newestlist.remove(0);
					// newestlistinfo.setDatas(newestlist);
					// }
					// return newestlistinfo;
				} else {
					for (int i = 0; i < db.size(); i++) {
						Document document = new Document();
						document.setDocument_id(db.getInt(i, "document_id"));
						document.setTitle(db.getString(i, "title"));
						document.setSubtitle(db.getString(i, "SUBTITLE"));
						document.setStatus(db.getInt(i, "status"));
						document.setDocflag(db.getInt(i, "isdeleted"));
						document.setAuthor(db.getString(i, "AUTHOR"));
						document.setCreateTime(db.getDate(i, "CREATETIME"));
						document.setDoctype(db.getInt(i, "DOCTYPE"));
						// document.setContent (db.getString(i,"CONTENT"));
						document.setChanel_id(db.getInt(i, "channel_id"));
						document.setLinktarget(db.getString(i, "LINKTARGET"));
						// 出于链接文档的链接地址保存的是在content字段的考虑
						if (document.getDoctype() == 1)
							document.setLinkfile(document.getContent());
						document.setDocsource_id(db.getInt(i, "DOCSOURCE_ID"));

						document.setDocwtime(db.getDate(i, "DOCWTIME"));

						document.setCreateUser(db.getInt(i, "CREATEUSER"));
						document.setDetailtemplate_id(db.getInt(i, "DETAILTEMPLATE_ID"));
						document.setDocabstract(db.getString(i, "DOCABSTRACT"));

						document.setTitlecolor(db.getString(i, "TITLECOLOR"));
						document.setKeywords(db.getString(i, "KEYWORDS"));
						document.setDoc_level(db.getInt(i, "doc_level"));
						document.setParentDetailTpl(db.getString(i, "PARENT_DETAIL_TPL"));
						document.setPicPath(db.getString(i, "PIC_PATH"));
						document.setMediapath(db.getString(i, "mediapath"));
						document.setPublishfilename(db.getString(i, "publishfilename"));
						document.setSecondtitle(db.getString(i, "secondtitle"));
						document.setIsNew(db.getInt(i, "isnew"));
						document.setNewPicPath(db.getString(i, "newpic_path"));
						document.setOrdertime(db.getDate(i, "ordertime"));
						/* 装载扩展字段数据 */
						document.setExtColumn(extManager.getExtColumnInfo(i,db));
						document.setDocsource_name(db.getString(i, "SRCNAME"));
						document.setDetailtemplate_name(db.getString(i, "NAME"));
						list.add(document);
						// 记录递归发布关系

					}
				}
				listinfo.setDatas(list);
				listinfo.setTotalSize(db.getTotalSize());
			} catch (Exception e) {
				e.printStackTrace();
				throw new DocumentManagerException(e.getMessage());
			}
		}
		return listinfo;

	}

	/**
	 * 根据文档关键字获取文档所属的站点中所有已发文档中标题, 摘要, 关键字(内容 现在不要)包含本关键字的文档列表 不翻页
	 * 
	 * @param docid
	 *            文档ID int
	 * @param count
	 * @return
	 * @throws DocumentManagerException
	 *             DocumentManager.java
	 * @author: ge.tao
	 */
	public List getKeyWordRelationOfDocument(String keywords_, int docid, String siteid, int count)
			throws DocumentManagerException {
		if (count <= 0)
			count = 5;
		return getKeyWordRelationOfDocument(keywords_, docid, siteid, 0, count).getDatas();
		// DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		// Document doc = this.getPartDocInfoById(docid + "");
		// List list = new ArrayList();
		// if(keywords_ == null){
		// keywords_ = doc.getKeywords();
		// }
		// if(keywords_ != null && keywords_.trim().length()>0){
		// DBUtil db = new DBUtil();
		// StringBuffer sql = new StringBuffer();
		//
		// String[] keywords = null;
		// sql.append("select * from (")
		// .append("select document_id,title,SUBTITLE,t.status as status,AUTHOR,
		// isdeleted, ")
		// .append("CREATETIME, DOCTYPE, case when DOCTYPE=1 ")
		// .append( "then t.content else null end
		// linkfile,channel_id,LINKTARGET,t.DOCSOURCE_ID,t.publishtime, ")
		// .append("DOCWTIME,CREATEUSER,t.DETAILTEMPLATE_ID,DOCABSTRACT,TITLECOLOR,")
		// .append("KEYWORDS,doc_level,PARENT_DETAIL_TPL,PIC_PATH,mediapath,publishfilename,secondtitle,
		// ")
		// .append("isnew,newpic_path,ordertime,")
		// .append("ds.SRCNAME as SRCNAME, tmpl.NAME as NAME, ch.SITE_ID as
		// SITE_ID ")
		// .append("from TD_CMS_DOCUMENT t inner join TD_CMS_DOCSOURCE ds on
		// ds.DOCSOURCE_ID=t.DOCSOURCE_ID ")
		// .append("left join TD_CMS_TEMPLATE tmpl on
		// tmpl.TEMPLATE_ID=t.DETAILTEMPLATE_ID left join TD_CMS_CHANNEL ch ")
		// .append("on ch.CHANNEL_ID=t.CHANNEL_ID )")
		// .append(" un where un.document_id != ").append(docid).append(" and
		// un.status in (")
		// .append(DocumentStatus.PUBLISHED.getStatus())
		// .append(",")
		// .append(DocumentStatus.PUBLISHING.getStatus())
		// .append(") and un.isdeleted =0 and
		// un.SITE_ID=").append(siteid).append(" ");
		// keywords = keywords_.split(",");
		// String subsql = "";
		// for(int i=0;i<keywords.length;i++){
		// if(keywords[i].trim().length()==0) continue;
		// if("".equalsIgnoreCase(subsql.trim())) {
		// subsql += " un.title like '%" + keywords[i].trim() + "%' or
		// un.DOCABSTRACT like '%"
		// + keywords[i].trim() + "%' or un.KEYWORDS like '%" +
		// keywords[i].trim() + "%'" ;
		// }else{
		// subsql += " or un.title like '%" + keywords[i].trim() + "%' or
		// un.DOCABSTRACT like '%"
		// + keywords[i].trim() + "%' or un.KEYWORDS like '%" +
		// keywords[i].trim() + "%'" ;
		// }
		// }
		// if(subsql.trim().length()>0){
		// subsql = " and (" + subsql + " ) ";
		// }
		// sql.append(subsql);
		// sql.append(count>0?" and rownum<="+count:" ");
		// //按发布时间排序
		// sql.append(" order by un.ordertime,un.publishtime desc");
		// //System.out.println("-----------------------------------------------------------------"+sql.toString());
		// log.warn(sql.toString());
		// try{
		// //整个站点下的跟该文档关键字相关联的所有文档 不翻页
		// db.executeSelect(sql.toString());
		// List newestlist = new ArrayList();
		// if(db.size()==0){
		// newestlist = CMSUtil.getCMSDriverConfiguration()
		// .getCMSService()
		// .getChannelManager()
		// .getLatestPubAndPubingDocList(doc.getSiteid()+"" ,
		// doc.getChannelName(),count);
		// //去掉最新的 就是当前发布的文档
		// if(newestlist != null && newestlist.size()>0)
		// newestlist.remove(0);
		// return newestlist;
		// }else{//整个站点下 没有 跟该文档关键字相关联的所有文档, 获取该文档所在频道的最新发布的文档, 不包括自己 不翻页
		// for(int i=0;i<db.size();i++){
		// Document document = new Document();
		// document.setDocument_id (db.getInt(i,"document_id"));
		// document.setTitle (db.getString(i,"title"));
		// document.setSubtitle (db.getString(i,"SUBTITLE"));
		// document.setStatus (db.getInt(i,"status"));
		// document.setDocflag (db.getInt(i,"isdeleted"));
		// document.setAuthor (db.getString(i,"AUTHOR"));
		// document.setCreateTime (db.getDate(i,"CREATETIME"));
		// document.setDoctype (db.getInt(i,"DOCTYPE"));
		// //document.setContent (db.getString(i,"CONTENT"));
		// document.setChanel_id (db.getInt(i,"channel_id"));
		// document.setLinktarget (db.getString(i,"LINKTARGET"));
		// //出于链接文档的链接地址保存的是在content字段的考虑
		// if(document.getDoctype() == 1)
		// document.setLinkfile(document.getContent());
		// document.setDocsource_id (db.getInt(i,"DOCSOURCE_ID"));
		//
		// document.setDocwtime (db.getDate(i,"DOCWTIME"));
		//
		// document.setCreateUser (db.getInt(i,"CREATEUSER"));
		// document.setDetailtemplate_id(db.getInt(i,"DETAILTEMPLATE_ID"));
		// document.setDocabstract (db.getString(i,"DOCABSTRACT"));
		//
		// document.setTitlecolor (db.getString(i,"TITLECOLOR"));
		// document.setKeywords (db.getString(i,"KEYWORDS"));
		// document.setDoc_level (db.getInt(i,"doc_level"));
		// document.setParentDetailTpl(db.getString(i,"PARENT_DETAIL_TPL"));
		// document.setPicPath (db.getString(i,"PIC_PATH"));
		// document.setMediapath (db.getString(i,"mediapath"));
		// document.setPublishfilename(db.getString(i,"publishfilename"));
		// document.setSecondtitle (db.getString(i,"secondtitle"));
		// document.setIsNew (db.getInt(i,"isnew"));
		// document.setNewPicPath (db.getString(i,"newpic_path"));
		// document.setOrdertime (db.getDate(i,"ordertime"));
		//
		// /*装载扩展字段数据*/
		// document.setExtColumn(extManager.getExtColumnInfo(db));
		// document.setDocsource_name(db.getString(i,"SRCNAME"));
		// document.setDetailtemplate_name(db.getString(i,"NAME"));
		// list.add(document);
		// //记录递归发布关系
		//
		// }
		// }
		// }catch(Exception e)
		// {
		// e.printStackTrace();
		// throw new DocumentManagerException(e.getMessage());
		// }
		// }
		// return list;

	}

	/**
	 * 获取关键字与本文档相匹配的已发布文档
	 */
	public List getKeyWord2RelationOfDocument(String title, int docid, String channel, String siteid, int count)
			throws DocumentManagerException {
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		Document doc = this.getPartDocInfoById(docid + "");
		List list = new ArrayList();
		if (title == null) {
			title = doc.getTitle();
		}
		if (title != null && title.trim().length() > 0) {
			DBUtil db = new DBUtil();
			StringBuffer sql = new StringBuffer();
			sql.append("select * from (")
					.append("select document_id,title,SUBTITLE,t.status as status,AUTHOR, isdeleted, ")
					.append("CREATETIME, DOCTYPE, case when DOCTYPE=1 ")
					.append("then t.content else null end linkfile,channel_id,LINKTARGET,t.DOCSOURCE_ID,t.publishtime, ")
					.append("DOCWTIME,CREATEUSER,t.DETAILTEMPLATE_ID,DOCABSTRACT,TITLECOLOR,")
					.append("KEYWORDS,doc_level,PARENT_DETAIL_TPL,PIC_PATH,mediapath,publishfilename,secondtitle, ")
					.append("isnew,newpic_path,ordertime,")
					.append("ds.SRCNAME as SRCNAME, tmpl.NAME as NAME, ch.SITE_ID as SITE_ID ")
					.append("from TD_CMS_DOCUMENT t inner join TD_CMS_DOCSOURCE ds on ds.DOCSOURCE_ID=t.DOCSOURCE_ID ")
					.append("left join TD_CMS_TEMPLATE tmpl on tmpl.TEMPLATE_ID=t.DETAILTEMPLATE_ID left join TD_CMS_CHANNEL ch ")
					.append("on ch.CHANNEL_ID=t.CHANNEL_ID order by ordertime desc )")
					.append(" un where un.document_id != ").append(docid).append(" and un.status in (")
					.append(DocumentStatus.PUBLISHED.getStatus()).append(",")
					.append(DocumentStatus.PUBLISHING.getStatus()).append(") and un.isdeleted =0 and un.SITE_ID=")
					.append(siteid).append(" ");
			String subsql = "";
			subsql += " un.AUTHOR like '%" + title.trim()
					+ "%' and channel_id=(select channel_id from TD_CMS_CHANNEL where display_name='" + channel + "')";
			if (subsql.trim().length() > 0) {
				subsql = " and (" + subsql + " ) ";
			}
			sql.append(subsql);
			sql.append(count > 0 ? " and rownum<=" + count : " ");
			// 按发布时间排序
			// sql.append(" order by un.ordertime desc");
			System.out.println("-----------------------------------------------------------------" + sql.toString());
			log.warn(sql.toString());
			try {
				// 整个站点下的跟该文档关键字相关联的所有文档 不翻页
				db.executeSelect(sql.toString());
				List newestlist = new ArrayList();
				if (db.size() == 0) {
					// newestlist = CMSUtil.getCMSDriverConfiguration()
					// .getCMSService().getChannelManager()
					// .getLatestPubAndPubingDocList(doc.getSiteid() + "",
					// doc.getChannelName(), count);
					// // 去掉最新的 就是当前发布的文档
					// if (newestlist != null && newestlist.size() > 0)
					// newestlist.remove(0);
					// return newestlist;
				} else {// 整个站点下 没有 跟该文档关键字相关联的所有文档, 获取该文档所在频道的最新发布的文档, 不包括自己
					// 不翻页
					for (int i = 0; i < db.size(); i++) {
						Document document = new Document();
						document.setDocument_id(db.getInt(i, "document_id"));
						document.setTitle(db.getString(i, "title"));
						document.setSubtitle(db.getString(i, "SUBTITLE"));
						document.setStatus(db.getInt(i, "status"));
						document.setDocflag(db.getInt(i, "isdeleted"));
						document.setAuthor(db.getString(i, "AUTHOR"));
						document.setCreateTime(db.getDate(i, "CREATETIME"));
						document.setDoctype(db.getInt(i, "DOCTYPE"));
						// document.setContent (db.getString(i,"CONTENT"));
						document.setChanel_id(db.getInt(i, "channel_id"));
						document.setLinktarget(db.getString(i, "LINKTARGET"));
						// 出于链接文档的链接地址保存的是在content字段的考虑
						if (document.getDoctype() == 1)
							document.setLinkfile(document.getContent());
						document.setDocsource_id(db.getInt(i, "DOCSOURCE_ID"));

						document.setDocwtime(db.getDate(i, "DOCWTIME"));

						document.setCreateUser(db.getInt(i, "CREATEUSER"));
						document.setDetailtemplate_id(db.getInt(i, "DETAILTEMPLATE_ID"));
						document.setDocabstract(db.getString(i, "DOCABSTRACT"));

						document.setTitlecolor(db.getString(i, "TITLECOLOR"));
						document.setKeywords(db.getString(i, "KEYWORDS"));
						document.setDoc_level(db.getInt(i, "doc_level"));
						document.setParentDetailTpl(db.getString(i, "PARENT_DETAIL_TPL"));
						document.setPicPath(db.getString(i, "PIC_PATH"));
						document.setMediapath(db.getString(i, "mediapath"));
						document.setPublishfilename(db.getString(i, "publishfilename"));
						document.setSecondtitle(db.getString(i, "secondtitle"));
						document.setIsNew(db.getInt(i, "isnew"));
						document.setNewPicPath(db.getString(i, "newpic_path"));
						document.setOrdertime(db.getDate(i, "ordertime"));

						/* 装载扩展字段数据 */
						document.setExtColumn(extManager.getExtColumnInfo(i,db));
						document.setDocsource_name(db.getString(i, "SRCNAME"));
						document.setDetailtemplate_name(db.getString(i, "NAME"));
						list.add(document);
						// 记录递归发布关系

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new DocumentManagerException(e.getMessage());
			}
		}
		return list;

	}

	/**
	 * 获取关键字与本文档相匹配的已发布文档
	 */
	public List getKeyWord3RelationOfDocument(String title, int docid, String channel, String siteid, int count)
			throws DocumentManagerException {
		DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		Document doc = this.getPartDocInfoById(docid + "");
		List list = new ArrayList();
		if (title == null) {
			title = doc.getTitle();
		}
		if (title != null && title.trim().length() > 0) {
			DBUtil db = new DBUtil();
			StringBuffer sql = new StringBuffer();
			sql.append("select * from (")
					.append("select document_id,title,SUBTITLE,t.status as status,AUTHOR, isdeleted, ")
					.append("CREATETIME, DOCTYPE, case when DOCTYPE=1 ")
					.append("then t.content else null end linkfile,channel_id,LINKTARGET,t.DOCSOURCE_ID,t.publishtime, ")
					.append("DOCWTIME,CREATEUSER,t.DETAILTEMPLATE_ID,DOCABSTRACT,TITLECOLOR,")
					.append("KEYWORDS,doc_level,PARENT_DETAIL_TPL,PIC_PATH,mediapath,publishfilename,secondtitle, ")
					.append("isnew,newpic_path,ordertime,")
					.append("ds.SRCNAME as SRCNAME, tmpl.NAME as NAME, ch.SITE_ID as SITE_ID ")
					.append("from TD_CMS_DOCUMENT t inner join TD_CMS_DOCSOURCE ds on ds.DOCSOURCE_ID=t.DOCSOURCE_ID ")
					.append("left join TD_CMS_TEMPLATE tmpl on tmpl.TEMPLATE_ID=t.DETAILTEMPLATE_ID left join TD_CMS_CHANNEL ch ")
					.append("on ch.CHANNEL_ID=t.CHANNEL_ID order by ordertime desc )")
					.append(" un where un.document_id != ").append(docid).append(" and un.status in (")
					.append(DocumentStatus.PUBLISHED.getStatus()).append(",")
					.append(DocumentStatus.PUBLISHING.getStatus()).append(") and un.isdeleted =0 and un.SITE_ID=")
					.append(siteid).append(" ");
			String subsql = "";
			subsql += " un.keywords like '%" + title.trim()
					+ "%' and channel_id=(select channel_id from TD_CMS_CHANNEL where display_name='" + channel + "')";
			if (subsql.trim().length() > 0) {
				subsql = " and (" + subsql + " ) ";
			}
			sql.append(subsql);
			sql.append(count > 0 ? " and rownum<=" + count : " ");
			// 按发布时间排序
			// sql.append(" order by un.ordertime desc");
			System.out.println("-----------------------------------------------------------------" + sql.toString());
			log.warn(sql.toString());
			try {
				// 整个站点下的跟该文档关键字相关联的所有文档 不翻页
				db.executeSelect(sql.toString());
				List newestlist = new ArrayList();
				if (db.size() == 0) {
					// newestlist = CMSUtil.getCMSDriverConfiguration()
					// .getCMSService().getChannelManager()
					// .getLatestPubAndPubingDocList(doc.getSiteid() + "",
					// doc.getChannelName(), count);
					// // 去掉最新的 就是当前发布的文档
					// if (newestlist != null && newestlist.size() > 0)
					// newestlist.remove(0);
					// return newestlist;
				} else {// 整个站点下 没有 跟该文档关键字相关联的所有文档, 获取该文档所在频道的最新发布的文档, 不包括自己
					// 不翻页
					for (int i = 0; i < db.size(); i++) {
						Document document = new Document();
						document.setDocument_id(db.getInt(i, "document_id"));
						document.setTitle(db.getString(i, "title"));
						document.setSubtitle(db.getString(i, "SUBTITLE"));
						document.setStatus(db.getInt(i, "status"));
						document.setDocflag(db.getInt(i, "isdeleted"));
						document.setAuthor(db.getString(i, "AUTHOR"));
						document.setCreateTime(db.getDate(i, "CREATETIME"));
						document.setDoctype(db.getInt(i, "DOCTYPE"));
						// document.setContent (db.getString(i,"CONTENT"));
						document.setChanel_id(db.getInt(i, "channel_id"));
						document.setLinktarget(db.getString(i, "LINKTARGET"));
						// 出于链接文档的链接地址保存的是在content字段的考虑
						if (document.getDoctype() == 1)
							document.setLinkfile(document.getContent());
						document.setDocsource_id(db.getInt(i, "DOCSOURCE_ID"));

						document.setDocwtime(db.getDate(i, "DOCWTIME"));

						document.setCreateUser(db.getInt(i, "CREATEUSER"));
						document.setDetailtemplate_id(db.getInt(i, "DETAILTEMPLATE_ID"));
						document.setDocabstract(db.getString(i, "DOCABSTRACT"));

						document.setTitlecolor(db.getString(i, "TITLECOLOR"));
						document.setKeywords(db.getString(i, "KEYWORDS"));
						document.setDoc_level(db.getInt(i, "doc_level"));
						document.setParentDetailTpl(db.getString(i, "PARENT_DETAIL_TPL"));
						document.setPicPath(db.getString(i, "PIC_PATH"));
						document.setMediapath(db.getString(i, "mediapath"));
						document.setPublishfilename(db.getString(i, "publishfilename"));
						document.setSecondtitle(db.getString(i, "secondtitle"));
						document.setIsNew(db.getInt(i, "isnew"));
						document.setNewPicPath(db.getString(i, "newpic_path"));
						document.setOrdertime(db.getDate(i, "ordertime"));

						/* 装载扩展字段数据 */
						document.setExtColumn(extManager.getExtColumnInfo(i,db));
						document.setDocsource_name(db.getString(i, "SRCNAME"));
						document.setDetailtemplate_name(db.getString(i, "NAME"));
						list.add(document);
						// 记录递归发布关系

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new DocumentManagerException(e.getMessage());
			}
		}
		return list;

	}

	/**
	 * 更新文档排序时间
	 * 
	 * @param docId
	 * @param ordertime
	 *            DocumentManagerImpl.java
	 * @author: ge.tao
	 */
	public void updateDocOrdertime(String docId, java.util.Date ordertime) {
		if (docId == null || ordertime == null)
			return;
		PreparedDBUtil db = new PreparedDBUtil();
		StringBuffer sql = new StringBuffer();
		sql.append("update TD_CMS_DOCUMENT set ").append("ordertime=? where ").append("document_id=? ");
		try {

			db.preparedUpdate(sql.toString());
			db.setTimestamp(1, new Timestamp(ordertime.getTime()));
			db.setPrimaryKey(2, docId);
			db.executePrepared();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.resetPrepare();
		}
	}

	public void addCount(String docId) {
		String sql_select = "select count from td_cms_document where document_id=" + docId;
		System.out.println(sql_select);
		String sql_update = "update td_cms_document set count=count+1 where document_id=" + docId;
		PreparedDBUtil db_select = new PreparedDBUtil();
		try {
			db_select.preparedSelect(sql_select);
			db_select.executePrepared();
			if (db_select.size() > 0) {
				if (db_select.getInt(0, 0) < 1) {
					sql_update = "update td_cms_document set count=1 where document_id=" + docId;
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		PreparedDBUtil db_update = new PreparedDBUtil();
		try {
			db_update.preparedUpdate(sql_update);
			db_update.executePrepared();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean updateDocs(HashMap map, String docIdStr) {
		boolean ret = false;
		int size = map.size();
		int count = 0;
		StringBuffer sql = new StringBuffer();
		sql.append("update td_cms_document set ");
		if (map.containsKey("titlecolor")) {
			sql.append("titlecolor=?");
			count++;
		}
		if (map.containsKey("docsource_id")) {
			if (count != 0)
				sql.append(",docsource_id=?");
			else
				sql.append("docsource_id=?");
			count++;
		}
		if (map.containsKey("docwtime")) {
			if (count != 0)
				sql.append(",docwtime=to_date(?,'yyyy-mm-dd hh24:mi:ss')");
			else
				sql.append("docwtime=to_date(?,'yyyy-mm-dd hh24:mi:ss')");
			count++;
		}
		if (map.containsKey("ordertime")) {
			if (count != 0)
				sql.append(",ordertime=to_date(?,'yyyy-mm-dd hh24:mi:ss')");
			else
				sql.append("ordertime=to_date(?,'yyyy-mm-dd hh24:mi:ss')");
			count++;
		}
		if (map.containsKey("author")) {
			if (count != 0)
				sql.append(",author=?");
			else
				sql.append("author=?");
			count++;
		}
		if (map.containsKey("seq")) {
			if (count != 0)
				sql.append(",seq=?");
			else
				sql.append("seq=?");
			count++;
		}
		if (map.containsKey("isnew")) {
			if (count != 0)
				sql.append(",isnew=?");
			else
				sql.append("isnew=?");
			count++;
		}
		if (map.containsKey("newpicpath")) {
			if (count != 0)
				sql.append(",NEWPIC_PATH=?");
			else
				sql.append("NEWPIC_PATH=?");
			count++;
		}
		if (map.containsKey("keywords")) {
			if (count != 0)
				sql.append(",keywords=?");
			else
				sql.append("keywords=?");
			count++;
		}

		sql.append(" where DOCUMENT_ID in(" + docIdStr.replace(';', ',').substring(0, docIdStr.length() - 1) + ")");
		// String sql = "update td_cms_document set
		// TITLECOLOR='"+doc.getTitlecolor()+"',DOCSOURCE_ID="+doc.getDocsource_id()+"
		// where DOCUMENT_ID
		// in("+docIdStr.replace(';',',').substring(0,docIdStr.length()-1)+")";
		System.out.println(sql);
		PreparedDBUtil db = new PreparedDBUtil();
		try {
			db.preparedUpdate(sql.toString());
			int flag = 1;
			if (map.containsKey("titlecolor")) {
				db.setString(flag, map.get("titlecolor").toString());
				flag++;

			}
			if (map.containsKey("docsource_id")) {
				db.setInt(flag, Integer.parseInt(map.get("docsource_id").toString()));
				flag++;
			}
			if (map.containsKey("docwtime")) {
				db.setString(flag, map.get("docwtime").toString());
				flag++;
			}
			if (map.containsKey("ordertime")) {
				db.setString(flag, map.get("ordertime").toString());
				flag++;
			}
			if (map.containsKey("author")) {
				db.setString(flag, map.get("author").toString());
				flag++;
			}
			if (map.containsKey("seq")) {
				db.setInt(flag, Integer.parseInt(map.get("seq").toString()));
				flag++;
			}
			if (map.containsKey("isnew")) {
				db.setInt(flag, Integer.parseInt(map.get("isnew").toString()));
				flag++;
			}
			if (map.containsKey("newpicpath")) {
				db.setString(flag, map.get("newpicpath").toString());
				flag++;
			}
			if (map.containsKey("keywords")) {
				db.setString(flag, map.get("keywords").toString());
				flag++;
			}
			// extManager.appendExtPreparedValue(db,doc.getExtColumn(),"td_cms_document",0);
			db.executePrepared();
			ret = true;
			updateStatus(docIdStr);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public void updateStatus(String docIdStr) {
		String sql = "update td_cms_document set status=11 where document_id in ("
				+ docIdStr.replace(';', ',').substring(0, docIdStr.length() - 1) + ") and status<>1";
		DBUtil db = new DBUtil();
		try {
			db.executeUpdate(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public int getDocCommentByDocId(int docId, Integer status) {
		int ret = 0;
		String sql = "select count(COMMENT_ID) from TD_CMS_DOC_COMMENT where doc_id=" + docId;
		if (status != null) {
			sql += " and STATUS=" + status;
		}
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			ret = db.getInt(0, 0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	public String ext_orgByDocId(String docId) {
		String ret = null;
		String sql = "select ext_org from td_cms_document where DOCUMENT_ID=" + docId;
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				ret = db.getString(0, 0);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ret = ret == null ? "" : ret;

		return ret;
	}

	public static void main(String[] args) {
		String docid = "35014";
		Document doc1 = null;
		Document doc2 = null;
		try {
			doc1 = new DocumentManagerImpl().getPartDocInfoById(docid);
			doc2 = new DocumentManagerImpl().getDoc(docid);
		} catch (DocumentManagerException e) {
			e.printStackTrace();
		}
		log.warn("part info of doc:" + doc1.getChannelName());
		log.warn("all info of doc:" + doc2.getChannelName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.frameworkset.platform.cms.documentmanager.DocumentManager#queryNewsByPublishTimeAndKeywords(java.util.Date,
	 * java.util.Date, java.lang.String, long, int)
	 */
	@Override
	public ListInfo queryDocumentsByPublishTimeAndKeywords(DocumentCondition condition, long offset, int pagesize)
			throws SQLException {
		if (!StringUtil.isEmpty(condition.getKeywords())) {
			condition.setKeywords("%" + condition.getKeywords() + "%");
		}
		final DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		final List list = new ArrayList();
		ListInfo listInfo = executor.queryListInfoBeanByNullRowHandler(new NullRowHandler(){

			@Override
			public void handleRow(Record db) throws Exception {
				Document doc = new Document();
				if (db.getInt("DOCTYPE") == Document.DOCUMENT_CHANNEL) {
					String channelid = String.valueOf(db.getLong("DOCUMENT_ID"));
					Channel refChannel = new ChannelManagerImpl().getChannelInfo(channelid);
					list.add(refChannel);
					// doc.setRefChannel(refChannel);
					// doc.setDoctype(Document.DOCUMENT_CHANNEL);
				} else {
					doc.setDocument_id(db.getInt("DOCUMENT_ID"));
					doc.setTitle(db.getString("TITLE"));
					doc.setSubtitle(db.getString("SUBTITLE"));
					doc.setAuthor(db.getString("AUTHOR"));
					doc.setChanel_id(db.getInt("CHANNEL_ID"));
					doc.setKeywords(db.getString("KEYWORDS"));
					doc.setDocabstract(db.getString("DOCABSTRACT"));
					doc.setDoctype(db.getInt("DOCTYPE"));
					doc.setDocwtime(db.getDate("DOCWTIME"));
					doc.setTitlecolor(db.getString("TITLECOLOR"));
					doc.setCreateTime(db.getDate("CREATETIME"));
					doc.setCreateUser(db.getInt("CREATEUSER"));
					doc.setDocsource_id(db.getInt("DOCSOURCE_ID"));
					doc.setDetailtemplate_id(db.getInt("DETAILTEMPLATE_ID"));
					doc.setLinktarget(db.getString("LINKTARGET"));
					doc.setLinkfile(db.getString("linkfile"));
					doc.setFlowId(db.getInt("FLOW_ID"));
					doc.setDoc_level(db.getInt("DOC_LEVEL"));
					doc.setDoc_kind(db.getInt("DOC_KIND"));
					doc.setParentDetailTpl(db.getString("PARENT_DETAIL_TPL"));
					doc.setPublishTime(db.getDate("publishtime"));
					doc.setPicPath(db.getString("pic_path"));

					doc.setMediapath(db.getString("mediapath"));
					doc.setPublishfilename(db.getString("publishfilename"));
					// doc.setc(db.getString(i,"commentswitch"));
					doc.setSecondtitle(db.getString("secondtitle"));
					doc.setIsNew(db.getInt("isnew"));
					doc.setNewPicPath(db.getString("newpic_path"));

					doc.setSiteid(db.getInt("site_id"));
					// new
					doc.setExt_class(db.getString("ext_class"));
					doc.setExt_djh(db.getString("ext_djh"));
					doc.setExt_index(db.getString("ext_index"));
					doc.setExt_org(db.getString("ext_org"));
					doc.setExt_wh(db.getString("ext_wh"));
					// new

					// String str = "select SRCNAME from TD_CMS_DOCSOURCE where DOCSOURCE_ID ="
					// + db.getInt("DOCSOURCE_ID") + "";
					//
					// db1.executeSelect(str);
					// if (db1.size() > 0) {
					doc.setDocsource_name(db.getString("source_name"));
					// }
					int isref = db.getInt("ordersq");
					doc.setRef(isref == 1 ? false : true);// 判断是否是引用的文档：true为是，flase为不是
					/* 装载扩展字段数据 */
//					Map docExtField = (new DocumentManagerImpl()).getDocExtFieldMap(doc.getDocument_id() + "");
//					doc.setDocExtField(docExtField);
					/* 装载系统扩展字段数据 */
//					doc.setExtColumn(extManager.getExtColumnInfo(db));
					doc.setOrdertime(db.getDate( "ordertime"));
					
					list.add(doc);
				}
				
			}
			
		}, "queryNewsByPublishTimeAndKeywords", offset, pagesize,
				condition);
		listInfo.setDatas(list);
		return listInfo;
	}
	
	/**
	 * 获取文档的点级数
	 */
	public void putDocCount(List<Integer> countids ,final Map<Integer,Object> idx) throws DocumentManagerException
	{

		if(countids== null ||  countids.size()==0)return ;
		final Map<Integer,String> datas=new HashMap<Integer,String>();
		Map<String,List> params = new HashMap<String,List>();
		params.put("countids", countids);
		try{
			executor.queryBeanByNullRowHandler(new NullRowHandler() {

				@Override
				public void handleRow(Record origine) throws Exception {
					Object obj = idx.get(origine.getInt("docid"));
					long num = origine.getLong("cnt");
					if(obj instanceof DocAggregation){
						DocAggregation docAggregation =(DocAggregation)obj;
						docAggregation.setCount(num);
					}else if(obj instanceof Document){
						Document doc=(Document)obj;
						doc.setCount(num);
					}

				}
			}, "queryDocCountsByIds",params);
		}catch(Exception e){
			
		}
			
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.frameworkset.platform.cms.documentmanager.DocumentManager#queryNewsByPublishTimeAndKeywords(java.util.Date,
	 * java.util.Date, java.lang.String, long, int)
	 */
	@Override
	public List<Document> queryDocumentsByPublishTimeAndKeywords(NewsCondition condition)
			throws SQLException {
		if (!StringUtil.isEmpty(condition.getKeywords())) {
			condition.setKeywords("%" + condition.getKeywords() + "%");
		}
		
		final DocumentExtColumnManager extManager = new DocumentExtColumnManager();
		final List<Document> list = new ArrayList<Document>();
		executor.queryBeanByNullRowHandler(new NullRowHandler(){

			@Override
			public void handleRow(Record db) throws Exception {
				Document doc = new Document();
//				if (db.getInt("DOCTYPE") == Document.DOCUMENT_CHANNEL) {
//					String channelid = String.valueOf(db.getLong("DOCUMENT_ID"));
//					Channel refChannel = new ChannelManagerImpl().getChannelInfo(channelid);
//					list.add(refChannel);
//					// doc.setRefChannel(refChannel);
//					// doc.setDoctype(Document.DOCUMENT_CHANNEL);
//				}
//				else 
				{
					doc.setDocument_id(db.getInt("DOCUMENT_ID"));
					doc.setTitle(db.getString("TITLE"));
					doc.setSubtitle(db.getString("SUBTITLE"));
					doc.setAuthor(db.getString("AUTHOR"));
					doc.setChanel_id(db.getInt("CHANNEL_ID"));
					doc.setKeywords(db.getString("KEYWORDS"));
					doc.setDocabstract(db.getString("DOCABSTRACT"));
					doc.setDoctype(db.getInt("DOCTYPE"));
					doc.setDocwtime(db.getDate("DOCWTIME"));
					doc.setTitlecolor(db.getString("TITLECOLOR"));
					doc.setCreateTime(db.getDate("CREATETIME"));
					doc.setCreateUser(db.getInt("CREATEUSER"));
					doc.setDocsource_id(db.getInt("DOCSOURCE_ID"));
					doc.setDetailtemplate_id(db.getInt("DETAILTEMPLATE_ID"));
					doc.setLinktarget(db.getString("LINKTARGET"));
					doc.setLinkfile(db.getString("linkfile"));
					doc.setFlowId(db.getInt("FLOW_ID"));
					doc.setDoc_level(db.getInt("DOC_LEVEL"));
					doc.setDoc_kind(db.getInt("DOC_KIND"));
					doc.setParentDetailTpl(db.getString("PARENT_DETAIL_TPL"));
					doc.setPublishTime(db.getDate("publishtime"));
					doc.setPicPath(db.getString("pic_path"));

					doc.setMediapath(db.getString("mediapath"));
					doc.setPublishfilename(db.getString("publishfilename"));
					// doc.setc(db.getString(i,"commentswitch"));
					doc.setSecondtitle(db.getString("secondtitle"));
					doc.setIsNew(db.getInt("isnew"));
					doc.setNewPicPath(db.getString("newpic_path"));

					doc.setSiteid(db.getInt("site_id"));
					// new
					doc.setExt_class(db.getString("ext_class"));
					doc.setExt_djh(db.getString("ext_djh"));
					doc.setExt_index(db.getString("ext_index"));
					doc.setExt_org(db.getString("ext_org"));
					doc.setExt_wh(db.getString("ext_wh"));
					// new

					// String str = "select SRCNAME from TD_CMS_DOCSOURCE where DOCSOURCE_ID ="
					// + db.getInt("DOCSOURCE_ID") + "";
					//
					// db1.executeSelect(str);
					// if (db1.size() > 0) {
					doc.setDocsource_name(db.getString("source_name"));
					// }
					int isref = db.getInt("ordersq");
					doc.setRef(isref == 1 ? false : true);// 判断是否是引用的文档：true为是，flase为不是
					/* 装载扩展字段数据 */
//					Map docExtField = (new DocumentManagerImpl()).getDocExtFieldMap(doc.getDocument_id() + "");
//					doc.setDocExtField(docExtField);
					/* 装载系统扩展字段数据 */
//					doc.setExtColumn(extManager.getExtColumnInfo(db));
					doc.setOrdertime(db.getDate( "ordertime"));
					
					list.add(doc);
				}
				
			}
			
		}, "queryNewsByPublishTimeAndKeywordsWithMore",
				condition);
		
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.frameworkset.platform.cms.documentmanager.DocumentManager#queryVideosByOrderType(com.frameworkset.platform
	 * .cms.documentmanager.bean.DocumentCondition, long, int)
	 */
	@Override
	public ListInfo queryVideosByOrderType(DocumentCondition condition, long offset, int pagesize) throws SQLException {
		return executor.queryListInfoBean(Document.class, "queryVideosByOrderType", offset, pagesize, condition);
	}
}
