package com.frameworkset.platform.cms.votemanager;



import java.util.List;
import java.util.Map;

import org.frameworkset.spi.SPIException;

import com.frameworkset.platform.sysmgrcore.exception.ManagerException;




public interface VoteManager {

	/**
	 * 取得问卷
	 * @param titleID
	 * @return
	 */
	public Title getSurveyBy(int titleID) throws VoteManagerException;
	/*
	 * 取得某站点当前时间活动问卷
	 */
	public List getCurActiveSurvey(String siteid,int count) throws VoteManagerException ;
	
	/**
	 * 取得问题选项
	 * @param titleID
	 * @return
	 */
	public List getItemsOfQstion(int questionID) throws VoteManagerException;
	
	/**
	 * 取得问卷问题
	 * @param titleID
	 * @return
	 */
	public List getQstionsOfServey(int titleID) throws VoteManagerException;
	


	/**
	 * 取得问题答案
	 * @param titleID
	 * @return
	 */
	public List getAnswersOfQstion(int questionID) throws VoteManagerException;
	
	/**
	 * 取得问卷的IP限制段
	 * @param titleID
	 * @return
	 */
	public List getIPctrlOfServey(int titleID) throws VoteManagerException;
	
	/**
	 * 取得问卷的时间限制段
	 * @param titleID
	 * @return
	 */
	public List getTimectrlOfServey(int titleID) throws VoteManagerException;
	
	/**
	 * 增加问卷
	 * @param titleID
	 * @return
	 */
	public int insertSurvey(Title title,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException;

	/**
	 * 修改问卷
	 * @param titleID
	 * @return
	 */
	public int modifySurvey(Title title,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException;

	/**
	 * 删除问卷
	 * @param titleID
	 * @return
	 */
	public int deleteSurveyBy(String titleIDs,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException;
	/**
	 * 取得问题
	 * 
	 * @param titleID
	 * @return
	 */
	public Question getQuestionBy(int qID) throws VoteManagerException ;
	
	/*
	 * 取得纯问题，不包括答案
	 */
	public Question getPureQuestionBy(int qID) throws VoteManagerException ;
	/**
	 * 投票
	 * 
	 * @param titleID
	 * @return
	 */
	public int doVote(String strOptionID,String ip) throws VoteManagerException;
	
	/**
	 * 投票
	 * 
	 * @param titleID
	 * @return
	 */
	public int doVote(String strOptionID,String ip,String user_id,String titleId) throws VoteManagerException;
	
	/**
	 * 投票
	 * 
	 * @param strOptionId 单选，多选答案,question文本答案<问题编号，问题答案>,IP地址
	 * @return
	 */
	public int doVote(String strOptionID,Map<String,String>questionAnswer,String ip) throws VoteManagerException;
	
	/**
	 * 投票
	 * 
	 * @param strOptionId 单选，多选答案,question文本答案<问题编号，问题答案>,IP地址
	 * @return
	 */
	public int doVote(String strOptionID,Map<String,String>questionAnswer,String ip,String user_id,String titleId) throws VoteManagerException;
	
	
	/**
	 * 投票
	 * 
	 * @param titleID
	 * @return
	 */
	public int doVote(int optionID) throws VoteManagerException;
	/**
	 * 清空投票结果
	 * 
	 * @param titleID
	 * @return
	 */
	public int clearVote(String titleIds,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException;
	/**
	 * 投票
	 * 
	 * @param titleID
	 * @return
	 */
	public int doAnswer(int qID,String answer,String ip) throws VoteManagerException;
	
	/**
	 * 投票
	 * 
	 * @param titleID
	 * @return
	 */
	public int doAnswer(int qID,String answer,String ip,String user_id,String titleId) throws VoteManagerException;
	
	/**
	 * 
	 * @param qID
	 * @param ip
	 * @return 1:可以投票； 2：不在投票时间段内；3：不在投票IP段内；4：重复投票
	 * @throws VoteManagerException
	 */
	public int canVote(int qID,String ip) throws VoteManagerException;
	
	/**
	 * 该IP是否对这个题目投过票
	 * 
	 * @param titleID
	 * @return
	 */
	public int hasVoted(int qID,String ip) throws VoteManagerException;
	
	/**
	 * 删除问题
	 * 
	 * @param titleID
	 * @return
	 */
	public int delQuestions(String qids) throws VoteManagerException;
	
	/**
	 * 删除自由问答回答记录
	 * 
	 * @param answerIDs
	 * @return
	 */
	public int delAnswers(String answerIDs,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException;
	/**
	 * 审核自由问答回答记录
	 * @param answerIDs
	 * @return
	 * @throws VoteManagerException
	 */
	public int passAnswers(String answerIDs,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException;
	/**
	 * 激活问题
	 * 
	 * @param titleID
	 * @return
	 */
	public int activateQuestions(String qids,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException;
	
	/**
	 * 反激活问题
	 * 
	 * @param titleID
	 * @return
	 */
	public int unactivateQuestions(String qids,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException;
	
	/**
	 * 根据问题ID取得问卷ID
	 * 
	 * @param titleID
	 * @return
	 */
	public int getTitleIDBy(String qid) throws VoteManagerException ;
	
	/**
	 * 取得站点活动问题
	 * 
	 * @param titleID
	 * @return
	 */
	public List getActiveQuestions(long siteID) throws VoteManagerException;
	
	/**
	 * 取得问卷下的所有问题编号
	 * 
	 * @param titleID
	 * @return id1,id2,id3
	 */
	public String getQuestionIDsBy(String titleid) throws VoteManagerException;
	
	/**
	 * 取得问卷问题
	 * 
	 * @param titleID
	 * @return
	 */
	public List getActiveQstionsOfServey(int titleID) throws VoteManagerException;
	
	/**
	 * 取得某站点某频道下的所有活动问卷
	 * @param siteid
	 * @param displayName
	 * @param count
	 * @return
	 * @throws VoteManagerException
	 */
	public List getActiveSurvey(String siteid,String displayName,int count)
	 throws VoteManagerException ;
	
	/**
	 * 取得某站点某频道下的所有活动问卷
	 * 
	 * @param siteid
	 * @param displayName
	 * @param count
	 * @return
	 * @throws VoteManagerException
	 */
	public int setSurveyTop(String titleid,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException;
	/**
	 * 取消置顶
	 * @param titleid
	 * @return
	 * @throws VoteManagerException
	 */
	public int cancelSurveyTop(String titleid,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException;
	/**
	 * 取得某站点某频道下的所有活动问卷
	 * 
	 * @param siteid
	 * @param displayName
	 * @param count
	 * @return
	 * @throws VoteManagerException
	 */
	public int setQuestionTop(String qid)
			throws VoteManagerException;
	
	/**
	 * 取得置顶问卷ID
	 * 
	 * @param siteid
	 * @param displayName
	 * @param count
	 * @return
	 * @throws VoteManagerException
	 */
	public int getTopSurveyID(int siteID) throws VoteManagerException ;
	
	/**
	 * 取得置顶问题ID
	 * 
	 * @param siteid
	 * @param displayName
	 * @param count
	 * @return
	 * @throws VoteManagerException
	 */
	public int getTopQuestionID(int siteID) throws VoteManagerException;
	
	/**
	 * 取得频道下的所有活动问题
	 * 
	 * @param siteid
	 * @param displayName
	 * @param count
	 * @return
	 * @throws VoteManagerException
	 */
	public List getActiveQstOf(String channelID,int count) throws VoteManagerException ;
	
	/**
	 * 取得频道下的最新活动问卷id
	 * 
	 * @param siteid
	 * @param displayName
	 * @param count
	 * @return
	 * @throws VoteManagerException
	 */
	public int getLatestActiveSurveyIDInChnl(String channelID) throws VoteManagerException;
	
	/**
	 * 激活问题
	 * 
	 * @param titleID
	 * @return
	 */
	public int activateSurveys(String sids,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException;
	
	/**
	 * 反激活问题
	 * 
	 * @param titleID
	 * @return
	 */
	public int unactivateSurveys(String sids,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException;
	/**
	 * 根据问题选项ID得到问题ID
	 */
	public int getQidByItemId(String itemId) throws VoteManagerException;
	/**
	 * 取消查看
	 */
	public int cancelSurveysLook(String sids,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException;
	/**
	 * 恢复查看
	 */
	public int setSurveysLook(String sids,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException;
	/**
	 * 获取回答内容
	 * @param aswerId
	 * @return
	 */
	public Answer getAnswerByAnswerId(String answerId)throws VoteManagerException;
	/**
	 * 通过部门id获取调查id
	 */
	public String getVoteIdByDepartId(String depart_id);

	/**
	 * 分页查找答案记录
	 * @param
	 * @return
	 */
	public List getAnswersOfQstionListInfo(int questionID,long offset, int pagesize ) throws VoteManagerException;

}
	