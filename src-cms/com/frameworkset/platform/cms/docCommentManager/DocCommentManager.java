package com.frameworkset.platform.cms.docCommentManager;

import java.sql.SQLException;
import java.util.List;

import com.frameworkset.util.ListInfo;

public interface DocCommentManager extends java.io.Serializable {
	/**
	 * 增加一条文档评论
	 * @param docComment
	 * @throws DocCommentManagerException
	 */
	public void addOneComment(DocComment docComment) throws DocCommentManagerException;
	/**
	 * 获取评论总数
	 * @return
	 * @throws DocCommentManagerException
	 */
	public int getCommentCount() throws DocCommentManagerException;
	/**
	 * 获取已发布的评论总数
	 * @return
	 * @throws DocCommentManagerException
	 */
	public int getCommentPublishedCount(int docId) throws DocCommentManagerException;
	/**
	 * 获取指定文档的评论数
	 * @param docId
	 * @return
	 * @throws DocCommentManagerException
	 */
	public int getCommentCount(int docId) throws DocCommentManagerException;
	/**
	 * 获取指定文档的热门评论数
	 * @param docId
	 * @return
	 * @throws DocCommentManagerException
	 */
	public int getHotCommentCount(int docId) throws DocCommentManagerException;
	/**
	 * 获取指定文档的已过滤水贴的评论数
	 * @param docId
	 * @return
	 * @throws DocCommentManagerException
	 */
	public int getFilteredCommentCount(int docId) throws DocCommentManagerException;
	/**
	 * 删除指定评论id对应的评论
	 * @param docId
	 * @throws DocCommentManagerException
	 */
	public void delOneCommentByComId(int docCommentId) throws DocCommentManagerException;
	/**
	 * 批量删除评论id，docCommentIds
	 * @param docCommentIds
	 * @throws DocCommentManagerException
	 */
	public void delCommentsByComId(String[] docCommentIds) throws DocCommentManagerException;
	/**
	 * 删除指定文档的所有评论
	 * @param docId
	 * @throws DocCommentManagerException
	 */
	public void delCommentsByDocId(int docId) throws DocCommentManagerException;
	/**
	 * 删除所有评论
	 * @throws DocCommentManagerException
	 */
	public void delAllComments() throws DocCommentManagerException;
	/**
	 * 获取指定文档id获取文档列表
	 * @param docId
	 * @param offset
	 * @param maxItem
	 * @return
	 * @throws DocCommentManagerException
	 */
	public ListInfo getCommnetList(int docId,int offset,int maxItem) throws SQLException;
	/**
	 * 根据sql查询语句获取文档列表
	 * @param sql
	 * @param offset
	 * @param maxItem
	 * @return
	 * @throws DocCommentManagerException
	 */
	public ListInfo getCommnetList(String sql,int offset,int maxItem) throws DocCommentManagerException;
	/**
	 * 根据指定评论id获取评论
	 * @param docCommentId
	 * @return
	 * @throws DocCommentManagerException
	 */
	public DocComment getCommentByComId(int docCommentId) throws DocCommentManagerException;
	/**
	 * 更新指定评论的状态，初始状态：0－未审；审核通过更新为：1－通过；
	 * @param docCommentId
	 * @throws DocCommentManagerException
	 */
	public void updateCommentStatus(String docCommentId,int newStatus) throws DocCommentManagerException;
	/**
	 * 批量更新
	 * @param docCommentIds
	 * @throws DocCommentManagerException
	 */
	public void updateCommentStatus(String[] docCommentIds,int newStatus) throws DocCommentManagerException;
	
	/**
	 * 获取指定评论的回复情况
	 * @param docCommentId
	 * @return 返回一个整形数组，长度为5，分别为“顶”人数，“不好说”人数，“反对”人数，“不知所云”人数，回复人数
	 * @throws DocCommentManagerException
	 */
	public int[] getResponseInfo(int docCommentId) throws DocCommentManagerException;
	
	/**
	 * 开通open(或者关闭close)评论功能
	 * 如果docorchnl为“doc”则表示开通或关闭文档评论功能
	 * 如果docorchnl为“chnl”则表示开通或关闭频道评论功能
	 * @param docCommentId
	 * @param commentSwitch  值为“open” 或者“close”
	 * @param docorchnl  值为“doc” 或 “chnl”
	 * @throws DocCommentManagerException
	 */
	public void switchDocCommentFunction(String id,String commentSwitch,String docorchnl) throws DocCommentManagerException;
	
	
	/**
	 * 获取频道或者文档是否开通频道的信息
	 * @param id
	 * @param docorchnl  值为“doc” 或 “chnl”
	 * @throws DocCommentManagerException
	 */
	public int getDocCommentSwitch(String id,String docorchnl) throws DocCommentManagerException;
	/**
	 * 增加一条评论举报
	 * @param commentImpeach
	 * @return
	 * @throws DocCommentManagerException
	 */
	public void addDocCommentImpeach(CommentImpeach commentImpeach) throws DocCommentManagerException;
	/**
	 * 获取评论的举报信息列表
	 * @param sql
	 * @param offset
	 * @param maxItem
	 * @return
	 * @throws DocCommentManagerException
	 */
	public ListInfo getCommnetImpeachList(String sql,int offset,int maxItem) throws DocCommentManagerException;
	/**
	 * 删除单个举报信息
	 * @param impeache
	 * @throws DocCommentManagerException
	 */
	public void deleteImpeachInfo(String impeacheId) throws DocCommentManagerException;
	/**
	 * 批量删除举报信息
	 * @param impeaches
	 * @throws DocCommentManagerException
	 */
	public void deleteImpeachInfos(String[] impeacheIds) throws DocCommentManagerException;
	/**
	 * 举报信息邮件回复
	 * @param impeacheId
	 * @throws DocCommentManagerException
	 */
	public void replyImpeach(String impeacheId,String toEmail, String toName, String subject,
			String msg) throws DocCommentManagerException;
	/**
	 * 获取评论字典词汇列表
	 * @param sql
	 * @param offset
	 * @param maxItem
	 * @throws DocCommentManagerException
	 */
	public ListInfo getCommentDictList(String sql,int offset,int maxItem) throws DocCommentManagerException;
	
	/**
	 * 根据频道ID获取频道评论审核的开关信息
	 * @param channelId 频道ID
	 * @return int
	 */
	public Integer getChannelCommentAduitSwitch(int channelId) throws SQLException;
	
	/**
	 * 切换频道的评论审核开关
	 * @param switchFlag 是否开启
	 * @throws SQLException
	 */
	public void switchDocCommentAudit(int channelId, int switchFlag) throws SQLException;
}