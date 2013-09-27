/*
 * @(#)DocumentController.java
 * 
 * Copyright @ 2001-2012 SANY Group Co.,Ltd.
 * All right reserved.
 * 
 * 这个软件是属于三一集团有限公司机密的和私有信息，不得泄露。
 * 并且只能由三一集团有限公司内部员工在得到许可的情况下才允许使用。
 * This software is the confidential and proprietary information
 * of SANY Group Co, Ltd. You shall not disclose such
 * Confidential Information and shall use it only in accordance
 * with the terms of the license agreement you entered into with
 * SANY Group Co, Ltd.
 */
package com.frameworkset.platform.cms.votemanager;

import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.util.annotations.PagerParam;
import org.frameworkset.util.annotations.ResponseBody;

import com.frameworkset.util.ListInfo;

/**
 * @author xusy3
 * 问卷服务控制器
 */
public class VoteController {

	private VoteManager voteManager;
	

	
	/**
	 * 根据问卷ID号查询问卷
	 *  <js:score value="${memUsed}" partialBlocks="5" fullBlocks="10" showEmptyBlocks="true" showA="false" showB="false">
                    <img src="<c:url value="/css/classic/gifs/rb_{0}.gif"/>" alt="+"
                         title="<spring:message code="probe.jsp.sysinfo.memory.usage.alt"/>"/>
                </js:score>
	 * @param titleId 问卷ID
	 * @param HttpServletRequest request
	 * @return String
	 * @throws Exception Exception
	 */
	public @ResponseBody(datatype="jsonp") Title getSurveyBy(int titleId,String picpath) {
		try {
			voteManager  = new VoteManagerImpl();
			Title title = voteManager.getSurveyBy(titleId);
			for(Question quest:(List<Question>)title.getQuestions()){
				int count=0;
				for(Item item:(List<Item>)quest.getItems()){
					count=count+item.getCount();
				}
				
				double percentCount=0.0;
				
					double totalPercentCount=0.0;
					List<Item>list=(List<Item>)quest.getItems();
					for(int i=0;i<list.size();i++){
						Item item =list.get(i);
						Score score = new Score();
						score.setValue(item.getCount());
						score.setMaxValue(count);
						score.setFullBlocks(10);
						score.setPartialBlocks(5);
						score.setShowA(true);
						score.setShowB(true);
						score.setShowEmptyBlocks(true);
						item.setScore(score.pic(picpath));
						if(count!=0){
							percentCount=new BigDecimal(""+item.getCount()*1.0/count).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
							
							if(i==list.size() -1){ //最后一项修复
								item.setPercentCount( new BigDecimal(""+( 1 -totalPercentCount )).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue() );
							}else{
								item.setPercentCount(percentCount);
							}
							totalPercentCount=totalPercentCount +percentCount;
					}
				}
			}
			return title;
		} catch (Exception e) {
			return null;
		}	
		
	}
	/**
	 * 根据前count问卷列表
	 * 
	 * @param titleId 问卷ID
	 * @param HttpServletRequest request
	 * @return String
	 * @throws Exception Exception
	 */
	public @ResponseBody(datatype="jsonp") List<Title> getTitleNList(String site,String channelName,int count) {
		try {
			voteManager  = new VoteManagerImpl();
			List titleList = voteManager.getCurActiveSurvey( site, count);
			return titleList;
		} catch (Exception e) {
			return null;
		}	
		
	}
	/**
	 * 根据问卷ID号查询问卷
	 * 
	 * @param titleId 问卷ID
	 * @param HttpServletRequest request
	 * @return String
	 * @throws Exception Exception
	 */
	public @ResponseBody(datatype="jsonp") String doVote(String strOptionID,HttpServletRequest request) {
		try {
			String ip=com.frameworkset.util.StringUtil.getClientIP(request);
			voteManager  = new VoteManagerImpl();
			String[] optionIDs = strOptionID.split(";");
			for(int i=0;i<optionIDs.length;i++){
				int qid=voteManager.getQidByItemId(optionIDs[i]);
				int canVote=voteManager.canVote(qid,ip);
				//1:可以投票； 2：不在投票时间段内；3：不在投票IP段内；4：重复投票
				if(canVote==4){
					return "你已经投过票了，谢谢。";
				}
			}
			Enumeration<String> en=request.getParameterNames();
			Map<String,String>textQuestion=new HashMap<String,String>();
			String qn="questionId";
			while(en.hasMoreElements()){
				String param=en.nextElement();
				if(param.indexOf(qn)>=0){
					String tid=param.substring(qn.length());
					textQuestion.put(tid, java.net.URLDecoder.decode(request.getParameter(param),"UTF-8"));
					//System.out.println(tid);
					//System.out.println(request.getParameter(param));
				}
				//System.out.println(param+"="+);
			}
			voteManager.doVote(strOptionID,textQuestion, ip);
			return "success";
		} catch (Exception e) {
			return null;
		}	
		
	}
	
	/**
	 * 根据问题号取得分页答案列表
	 * 
	 * @param questionID 问题ID
	 * @param HttpServletRequest request
	 * @return String
	 * @throws Exception Exception
	 */
	public @ResponseBody(datatype="jsonp") List<Title> getAnswersOfQstionListInfo(int questionID,long offset, int pagesize) {
		try {
			voteManager  = new VoteManagerImpl();
			List answersList = voteManager.getAnswersOfQstionListInfo( questionID, offset,pagesize);
			return answersList;
		} catch (Exception e) {
			return null;
		}	
		
	}
	/**
	 * 展示该自由问题答案(逐步展开)
	 * @param sortKey
	 * @param desc
	 * @param offset
	 * @param pagesize
	 * @param docId
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public String showVoteFreeAnswersList(int pagesize, int questionID,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		voteManager  = new VoteManagerImpl();
		//List answersList  = voteManager.getAnswersOfQstionListInfo(questionID, (int)offset, pagesize);
		Question question=voteManager.getPureQuestionBy(questionID);
		
		//request.setAttribute("answersList", answersList);
		request.setAttribute("questionID", questionID);
		request.setAttribute("question", question);
		request.setAttribute("pagesize",pagesize);
		request.setAttribute("votecount", question.getVotecount());

		return "path:showVoteFreeAnswersList";
	}
}
