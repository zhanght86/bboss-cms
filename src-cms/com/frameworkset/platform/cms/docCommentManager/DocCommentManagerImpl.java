package com.frameworkset.platform.cms.docCommentManager;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.frameworkset.web.servlet.support.WebApplicationContextUtils;

import com.frameworkset.common.poolman.ConfigSQLExecutor;
import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.platform.cms.docCommentManager.docCommentDictManager.DocCommentDict;
import com.frameworkset.platform.cms.mailmanager.EMailImpl;
import com.frameworkset.platform.cms.mailmanager.EMailInterface;
import com.frameworkset.util.ListInfo;

public class DocCommentManagerImpl implements DocCommentManager {

	private ConfigSQLExecutor executor;

	public void updateCommentStatus(String docCommentId, int newStatus)
			throws DocCommentManagerException {
		// TODO Auto-generated method stub
		DBUtil db = new DBUtil();
		String sql = "update td_cms_doc_comment set status = " + newStatus
				+ " where comment_id = " + docCommentId;
		try {
			db.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocCommentManagerException("更新指定评论的状态失败！"
					+ e.getMessage());
		}
	}

	public void addOneComment(DocComment docComment)
			throws DocCommentManagerException {
		// TODO Auto-generated method stub
		PreparedDBUtil preparedDB = new PreparedDBUtil();
		DBUtil dbUitl = new DBUtil();

		String sql = "insert into td_cms_doc_comment(comment_id, doc_id,doc_comment,user_name,comtime,user_ip,src_comment_id,status) values(?, ?,?,?,?,?,?,?)";
		try {

			int id = (Integer) dbUitl.executeSelectForObject(
					executor.getSql("getCommentNextId"), Integer.class);
			preparedDB.preparedInsert(sql);
			preparedDB.setInt(1, id);
			preparedDB.setInt(2, docComment.getDocId());
			preparedDB.setClob(3, docComment.getDocComment(), "doc_comment");
			preparedDB.setString(4, docComment.getUserName());
			preparedDB.setTimestamp(5, new Timestamp(new Date().getTime()));
			preparedDB.setString(6, docComment.getUserIP());
			if (docComment.getSrcCommentId() == 0)
				preparedDB.setNull(7, Types.NUMERIC);
			else
				preparedDB.setInt(7, docComment.getSrcCommentId());
			preparedDB.setInt(8, docComment.getStatus());
			preparedDB.executePrepared();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocCommentManagerException("新评论插入失败！" + e.getMessage());
		} finally {
			preparedDB.resetPrepare();
		}
	}

	public void delAllComments() throws DocCommentManagerException {
		// TODO Auto-generated method stub
		DBUtil db = new DBUtil();
		String sql = "delete from td_cms_doc_comment";
		try {
			db.executeDelete(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocCommentManagerException("清除评论表失败！" + e.getMessage());
		}
	}

	public void delCommentsByDocId(int docId) throws DocCommentManagerException {
		// TODO Auto-generated method stub
		DBUtil db = new DBUtil();
		String sql = "delete from td_cms_doc_comment where doc_id = " + docId;
		String sql1 = "delete from td_cms_doccom_impeachinfo b "
				+ "where b.comment_id in "
				+ "(select a.comment_id from td_cms_doc_comment a "
				+ "where a.doc_id = " + docId + ")";
		try {
			db.executeDelete(sql1);
			db.executeDelete(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocCommentManagerException("删除指定文档的评论失败！"
					+ e.getMessage());
		}
	}

	public void delOneCommentByComId(int docCommentId)
			throws DocCommentManagerException {
		// TODO Auto-generated method stub
		DBUtil db = new DBUtil();
		String sql = "delete from td_cms_doc_comment where comment_id = "
				+ docCommentId;
		try {
			db.executeDelete(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocCommentManagerException("删除指定评论失败！" + e.getMessage());
		}
	}

	public DocComment getCommentByComId(int docCommentId)
			throws DocCommentManagerException {
		// TODO Auto-generated method stub
		DBUtil db = new DBUtil();
		String sql = "select * from td_cms_doc_comment where comment_id = "
				+ docCommentId;
		try {
			db.executeSelect(sql);
			if (db.size() > 0) {
				DocComment docComment = new DocComment();
				docComment.setCommentId(db.getInt(0, "comment_id"));
				docComment.setDocId(db.getInt(0, "doc_id"));
				docComment.setDocComment(db.getString(0, "doc_comment"));
				docComment.setUserName(db.getString(0, "user_name"));
				docComment.setUserIP(db.getString(0, "user_ip"));
				docComment.setSubTime(db.getDate(0, "comtime"));
				docComment.setSrcCommentId(db.getInt(0, "src_comment_id"));
				docComment.setStatus(db.getInt(0, "status"));
				return docComment;
			} else
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocCommentManagerException("获取指定评论!" + e.getMessage());
		}
	}

	public int getCommentCount() throws DocCommentManagerException {
		// TODO Auto-generated method stub
		DBUtil db = new DBUtil();
		String sql = "select comment_id from td_cms_doc_comment";
		try {
			db.executeSelect(sql);
			return db.size();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocCommentManagerException("统计评论总数失败!" + e.getMessage());
		}
	}

	public int getCommentCount(int docId) throws DocCommentManagerException {
		DBUtil db = new DBUtil();
		String sql = "select comment_id from td_cms_doc_comment where doc_id = "
				+ docId;
		try {
			db.executeSelect(sql);
			return db.size();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocCommentManagerException("统计指定文档的评论总数失败！"
					+ e.getMessage());
		}
	}

	public int getCommentPublishedCount(int docId)
			throws DocCommentManagerException {
		DBUtil db = new DBUtil();
		String sql = "select comment_id from td_cms_doc_comment where status = 1 and doc_id = "
				+ docId;
		try {
			db.executeSelect(sql);
			return db.size();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocCommentManagerException("统计指定文档的评论总数失败！"
					+ e.getMessage());
		}
	}

	public ListInfo getCommnetList(int docId, int offset, int maxItem)
			throws SQLException {
		// TODO Auto-generated method stub
		// throw new DocCommentManagerException("未实现！");

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("docId", docId);

		return executor.queryListInfoBean(DocComment.class, "getCommnetList",
				offset, maxItem, paramMap);
	}

	public ListInfo getCommnetList(String sql, int offset, int maxItem)
			throws DocCommentManagerException {
		// TODO Auto-generated method stub
		DBUtil db = new DBUtil();
		DBUtil dbUtil = new DBUtil();
		try {
			db.executeSelect(sql, offset, maxItem);
			List tempList = new ArrayList();
			ListInfo listInfo = new ListInfo();
			for (int i = 0; i < db.size(); i++) {
				DocComment docComment = new DocComment();
				int comment_id = db.getInt(i, "comment_id");
				docComment.setCommentId(comment_id);
				docComment.setDocId(db.getInt(i, "doc_id"));
				String doc_comment = db.getString(i, "doc_comment");
				docComment.setDocComment(doc_comment);
				docComment.setUserName(db.getString(i, "user_name"));
				docComment.setSubTime(db.getDate(i, "comtime"));
				docComment.setUserIP(db.getString(i, "user_ip"));
				docComment.setSrcCommentId(db.getInt(i, "src_comment_id"));
				docComment.setDocTitle(db.getString(i, "docTitle"));
				docComment.setStatus(db.getInt(i, "status"));
				String sql1 = "select word from td_cms_doccomment_dict";
				dbUtil.executeSelect(sql1);
				if (dbUtil.size() > 0) {
					for (int j = 0; j < dbUtil.size(); j++) {
						if (doc_comment.indexOf(dbUtil.getString(j, "word")) >= 0) { // 判断是否报警
							docComment.setAlarm(1); // 设置报警
						}
					}
				}
				sql1 = "select id from td_cms_doccom_impeachinfo where comment_id ="
						+ comment_id;
				dbUtil.executeSelect(sql1);
				if (dbUtil.size() > 0) { // 判断是否有举报信息
					docComment.setImpeachFlag(1); // 设置为有举报信息
				}
				tempList.add(docComment);
			}
			listInfo.setDatas(tempList);
			listInfo.setTotalSize(db.getTotalSize());
			return listInfo;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocCommentManagerException("获取评论列表失败！" + e.getMessage());
		}
	}

	public void delCommentsByComId(String[] docCommentIds)
			throws DocCommentManagerException {
		// TODO Auto-generated method stub
		try {
			for (int i = 0; i < docCommentIds.length; i++) {
				this.delOneCommentByComId(Integer.parseInt(docCommentIds[i]));
			}
		} catch (DocCommentManagerException de) {
			de.printStackTrace();
			throw de;
		}
	}

	public void updateCommentStatus(String[] docCommentIds, int newStatus)
			throws DocCommentManagerException {
		// TODO Auto-generated method stub
		try {
			for (int i = 0; i < docCommentIds.length; i++) {
				this.updateCommentStatus(docCommentIds[i], newStatus);
			}
		} catch (DocCommentManagerException de) {
			de.printStackTrace();
			throw de;
		}
	}

	public int[] getResponseInfo(int docCommentId)
			throws DocCommentManagerException {
		int[] temp = new int[5];
		DBUtil db = new DBUtil();
		try {
			String sql = "select * from td_cms_doc_comment where src_comment_id = "
					+ docCommentId + " and doc_comment like '顶'";
			db.executeSelect(sql);
			temp[0] = db.size(); // "顶"人数
			sql = "select * from td_cms_doc_comment where src_comment_id = "
					+ docCommentId + " and doc_comment like '不好说'";
			db.executeSelect(sql);
			temp[1] = db.size(); // "不好说"人数
			sql = "select * from td_cms_doc_comment where src_comment_id = "
					+ docCommentId + " and doc_comment like '反对'";
			db.executeSelect(sql);
			temp[2] = db.size(); // "反对"人数
			sql = "select * from td_cms_doc_comment where src_comment_id = "
					+ docCommentId + " and doc_comment like '不知所云'";
			db.executeSelect(sql);
			temp[3] = db.size(); // "不知所云"人数
			sql = "select * from td_cms_doc_comment where src_comment_id = "
					+ docCommentId
					+ " and doc_comment not like '顶' and doc_comment not like '不好说' and doc_comment not like '反对' and doc_comment not like '不知所云'";
			db.executeSelect(sql);
			temp[4] = db.size(); // "回复"人数
			return temp;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocCommentManagerException("获取回复情况失败！" + e.getMessage());
		}
	}

	public int getFilteredCommentCount(int docId)
			throws DocCommentManagerException {
		DBUtil db = new DBUtil();
		String sql = "select comment_id from td_cms_doc_comment a "
				+ "where doc_id = "
				+ docId
				+ " and a.comment_id not in (select d.comment_id from td_cms_doc_comment d where d.doc_comment like '顶' or d.doc_comment like '不好说' or d.doc_comment like'不知所云' or d.doc_comment like '反对')";
		try {
			db.executeSelect(sql);
			return db.size();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocCommentManagerException("统计指定文档的评论总数失败！"
					+ e.getMessage());
		}
	}

	public int getHotCommentCount(int docId) throws DocCommentManagerException {
		DBUtil db = new DBUtil();
		String sql = "select comment_id from td_cms_doc_comment a "
				+ "where doc_id = "
				+ docId
				+ " and (select count(c.comment_id) from td_cms_doc_comment c where a.comment_id = c.src_comment_id) >= 2"
				+ " and a.comment_id not in (select d.comment_id from td_cms_doc_comment d where d.doc_comment like '顶' or d.doc_comment like '不好说' or d.doc_comment like'不知所云' or d.doc_comment like '反对')";
		try {
			db.executeSelect(sql);
			return db.size();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocCommentManagerException("统计指定文档的评论总数失败！"
					+ e.getMessage());
		}
	}

	public void switchDocCommentFunction(String id, String commentSwitch,
			String docorchnl) throws DocCommentManagerException {
		DBUtil db = new DBUtil();
		String sql = "";
		String sql1 = "";
		if (docorchnl.equals("doc")) {
			if (commentSwitch.equals("open")) { // 开通文档评论功能
				sql = "update td_cms_document set commentswitch = 0 where document_id = "
						+ id;
			} else if (commentSwitch.equals("close")) { // 关闭文档评论功能
				sql = "update td_cms_document set commentswitch = 1 where document_id = "
						+ id;
			}
		} else if (docorchnl.equals("chnl")) {
			if (commentSwitch.equals("open")) { // 关闭频道评论功能
				// sql =
				// "update td_cms_channel set commentswitch = 0 where channel_id in "
				// +
				// "(select channel_id from td_cms_channel connect by parent_id = prior channel_id start with channel_id='"
				// + id + "')";
				// sql1 =
				// "update td_cms_document set commentswitch = 0 where document_id in "
				// +
				// "(select document_id from td_cms_document where channel_id in"
				// +
				// "(select channel_id from td_cms_channel connect by parent_id = prior channel_id start with channel_id='"
				// + id + "'))";
				sql = "update td_cms_channel set commentswitch = 0 where channel_id = '"
						+ id + "'";
				sql1 = "update td_cms_document set commentswitch = 0 where document_id in "
						+ "(select document_id from td_cms_document where channel_id = '"
						+ id + "')";

				// System.out.print(sql1);
			} else if (commentSwitch.equals("close")) { // 关闭频道评论功能
			// sql =
			// "update td_cms_channel set commentswitch = 1 where channel_id in "
			// +
			// "(select channel_id from td_cms_channel connect by parent_id = prior channel_id start with channel_id='"
			// + id + "')";
			// sql1 =
			// "update td_cms_document set commentswitch = 1 where document_id in "
			// + "(select document_id from td_cms_document where channel_id in "
			// +
			// "(select channel_id from td_cms_channel connect by parent_id = prior channel_id start with channel_id='"
			// + id + "'))";

				sql = "update td_cms_channel set commentswitch = 1 where channel_id = '"
						+ id + "'";
				sql1 = "update td_cms_document set commentswitch = 1 where document_id in "
						+ "(select document_id from td_cms_document where channel_id = '"
						+ id + "')";

				// System.out.print(sql1);
			}
			try {
				db.addBatch(sql);
			} catch (SQLException e) {

				e.printStackTrace();
			}
			try {
				db.addBatch(sql1);
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
		try {
			if (sql.length() > 0) {
				db.executeUpdate(sql);
			}
			if (sql1.length() > 0) {
				db.executeUpdate(sql1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocCommentManagerException("开通（关闭）评论功能时，数据库操作失败！"
					+ e.getMessage());
		}
	}

	public int getDocCommentSwitch(String id, String docorchnl)
			throws DocCommentManagerException {
		DBUtil db = new DBUtil();
		String sql = "";
		int commentSwitch = 0;
		if (docorchnl.equals("doc")) {
			sql = "select commentswitch from td_cms_document where document_id = "
					+ id;
		} else if (docorchnl.equals("chnl")) {
			sql = "select commentswitch from td_cms_channel where channel_id = "
					+ id;
		}
		try {
			db.executeSelect(sql);
			if (db.size() > 0)
				commentSwitch = db.getInt(0, "commentswitch");
			return commentSwitch;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocCommentManagerException("获取评论功能信息时，数据库操作失败！"
					+ e.getMessage());
		}
	}

	public void addDocCommentImpeach(CommentImpeach commentImpeach)
			throws DocCommentManagerException {
		PreparedDBUtil preparedDB = new PreparedDBUtil();
		String sql = "insert into td_cms_doccom_impeachinfo(comment_id,impeacher,email,reason,descriptioninfo,comtime) values(?,?,?,?,?,?)";
		try {
			preparedDB.preparedInsert(sql);

			preparedDB.setInt(1, commentImpeach.getCommentId());
			preparedDB.setString(2, commentImpeach.getImpeacher());
			preparedDB.setString(3, commentImpeach.getEmail());
			preparedDB.setInt(4, commentImpeach.getReason());
			preparedDB.setString(5, commentImpeach.getDescription());
			preparedDB.setTimestamp(6, new Timestamp(new Date().getTime()));
			preparedDB.executePrepared();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocCommentManagerException("举报信息插入数据库失败！"
					+ e.getMessage());
		} finally {
			preparedDB.resetPrepare();
		}
	}

	public ListInfo getCommnetImpeachList(String sql, int offset, int maxItem)
			throws DocCommentManagerException {
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql, offset, maxItem);
			List tempList = new ArrayList();
			ListInfo listInfo = new ListInfo();
			for (int i = 0; i < db.size(); i++) {
				CommentImpeach commentImpeach = new CommentImpeach();
				commentImpeach.setId(db.getInt(i, "id"));
				commentImpeach.setCommentId(db.getInt(i, "comment_id"));
				commentImpeach.setImpeacher(db.getString(i, "impeacher"));
				commentImpeach.setEmail(db.getString(i, "email"));
				commentImpeach.setReason(db.getInt(i, "reason"));
				commentImpeach.setDescription(db
						.getString(i, "descriptioninfo"));
				commentImpeach.setComtime(db.getDate(i, "comtime"));
				commentImpeach.setRepled(db.getInt(i, "replyed"));

				tempList.add(commentImpeach);
			}
			listInfo.setDatas(tempList);
			listInfo.setTotalSize(db.getTotalSize());
			return listInfo;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocCommentManagerException("获取评论列表失败！" + e.getMessage());
		}
	}

	public void deleteImpeachInfo(String impeacheId)
			throws DocCommentManagerException {
		DBUtil db = new DBUtil();
		String sql = "delete from td_cms_doccom_impeachinfo where id="
				+ impeacheId;
		try {
			db.executeDelete(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocCommentManagerException("删除举报信息失败！" + e.getMessage());
		}
	}

	public void deleteImpeachInfos(String[] impeacheIds)
			throws DocCommentManagerException {
		try {
			for (int i = 0; i < impeacheIds.length; i++)
				deleteImpeachInfo(impeacheIds[i]);
		} catch (DocCommentManagerException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public void replyImpeach(String impeacheId, String toEmail, String toName,
			String subject, String msg) throws DocCommentManagerException {
		try {
			EMailInterface eMail = new EMailImpl();
			eMail.sendSimpleEmail(toEmail, toName, subject, msg);
			// 更新为已回复
			DBUtil db = new DBUtil();
			String sql = "update td_cms_doccom_impeachinfo set replyed=1 where id="
					+ impeacheId;
			db.executeUpdate(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ListInfo getCommentDictList(String sql, int offset, int maxItem)
			throws DocCommentManagerException {
		DBUtil db = new DBUtil();
		DBUtil db1 = new DBUtil();
		try {
			db.executeSelect(sql, offset, maxItem);
			List tempList = new ArrayList();
			ListInfo listInfo = new ListInfo();
			for (int i = 0; i < db.size(); i++) {
				DocCommentDict docCommentDict = new DocCommentDict();
				long siteid = db.getLong(i, "siteid");
				docCommentDict.setSiteId(siteid);
				docCommentDict.setWordId(db.getInt(i, "id"));
				docCommentDict.setWord(db.getString(i, "word"));
				docCommentDict.setDescription(db.getString(i, "description"));
				if (siteid != 0) {
					String sql1 = "select name from td_cms_site where site_id = "
							+ siteid;
					db1.executeSelect(sql1);
					if (db1.size() > 0)
						docCommentDict.setSiteName(db1.getString(0, "name"));
					else
						docCommentDict.setSiteName("不详(可能站点已经删除)");
				} else {
					docCommentDict.setSiteName("各站点通用");
				}
				tempList.add(docCommentDict);
			}
			listInfo.setDatas(tempList);
			listInfo.setTotalSize(db.getTotalSize());
			return listInfo;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DocCommentManagerException("获取字典词汇失败！" + e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.docCommentManager.DocCommentManager#
	 * getChannelCommentAduitSwitch(int)
	 */
	@Override
	public Integer getChannelCommentAduitSwitch(int channelId)
			throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("channelId", channelId);

		return executor.queryObjectBean(Integer.class,
				"getChannelCommentAduitSwitch", paramMap);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.frameworkset.platform.cms.docCommentManager.DocCommentManager#
	 * switchDocCommentAudit(int, int)
	 */
	@Override
	public void switchDocCommentAudit(int channelId, int switchFlag)
			throws SQLException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("channelId", channelId);
		paramMap.put("switchFlag", switchFlag);

		executor.updateBean("switchDocCommentAudit", paramMap);
	}
}
