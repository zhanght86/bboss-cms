package com.frameworkset.platform.cms.voteservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import com.frameworkset.platform.cms.bean.ExcelBean;

public interface VoteMobileService {
	public void saveVoteDetail(String userId,String id,String titleId,String qid,String type,String oid,String content)throws Exception;
	
	public List<ExcelBean> queryAnswerContent(String titleId)throws Exception;
	
	public void  setExcelData(Workbook workbook, List<ExcelBean> list)throws Exception;
	
	public String queryVoteNameByTitleId(String titleId)throws Exception;
	
	/**
	 * 根据试卷id获取试卷题目
	 * 
	 * @param titleId
	 * @return
	 * @throws Exception
	 *             2014年11月26日
	 */
	public List<Map<String, String>> getQuestionTitleById(String titleId)
			throws Exception;

	/**
	 * 导入Excel
	 * 
	 * @param workbook
	 * @param titleList
	 *            题目列表
	 * @param answerList
	 *            答案列表
	 * @throws Exception
	 *             2014年11月26日
	 */
	public void setExcelData(Workbook workbook,
			List<Map<String, String>> titleList, List<ExcelBean> answerList)
			throws Exception;

}
