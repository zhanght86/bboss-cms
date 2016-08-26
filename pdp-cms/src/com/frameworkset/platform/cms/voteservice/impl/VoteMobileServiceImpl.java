package com.frameworkset.platform.cms.voteservice.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.Record;
import com.frameworkset.common.poolman.handle.NullRowHandler;
import com.frameworkset.platform.cms.bean.ExcelBean;
import com.frameworkset.platform.cms.util.CMSUtil;
import com.frameworkset.platform.cms.util.StringUtil;
import com.frameworkset.platform.cms.votemanager.ws.VoteTitle;
import com.frameworkset.platform.cms.voteservice.VoteMobileService;

public class VoteMobileServiceImpl implements VoteMobileService {
	private com.frameworkset.common.poolman.ConfigSQLExecutor executor;

	@Override
	public void saveVoteDetail(String userId, String ip, String titleId,
			String qid, String type, String oid, String content)
			throws Exception {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("ANSER_ID",
				DBUtil.getNextStringPrimaryKey("td_cms_vote_answer"));
		paramMap.put("QID", qid);
		paramMap.put("TYPE", type);
		paramMap.put("ITEM_ID", oid);
		paramMap.put("ANSWER", content);
		paramMap.put("TITLE_ID", titleId);
		paramMap.put("USER_ID", userId);
		paramMap.put("WHO_IP", ip);

		// executor.insert("saveVoteDetail", id,userId, titleId, qid, type, oid,
		// content); gw_tanx 20150204
		executor.insertBean("saveVoteDetail", paramMap);
	}

	@Override
	public List<ExcelBean> queryAnswerContent(String titleId) throws Exception {

		return executor.queryList(ExcelBean.class, "queryAnswerContent",
				titleId);
	}

	@Override
	public List<Map<String, String>> getQuestionTitleById(String titleId)
			throws Exception {

		final List<Map<String, String>> titleList = new ArrayList<Map<String, String>>();

		executor.queryByNullRowHandler(new NullRowHandler() {
			@Override
			public void handleRow(Record origine) throws Exception {
				Map<String, String> tilteMap = new HashMap<String, String>();
				String title = (String) origine.getString("TITLE");
				String id = (String) origine.getString("ID");
				tilteMap.put("TITLE", title);
				tilteMap.put("ID", id);
				titleList.add(tilteMap);
			}

		}, "getQuestionTitleById", titleId);

		return titleList;
	}

	/**
	 * 过滤答案数据
	 * 
	 * @param answerList
	 *            答案数据
	 * @param contentList
	 *            过滤后的答案内容数据 2014年11月26日
	 */
	private void filterExceDate(List<ExcelBean> answerList,
			List<Map<String, String>> contentList) {
		// 临时比较对象-用户id
		String tempUserId = "";
		// 临时比较对象-问题id
		String tempQid = "";
		// 多选题答案
		StringBuffer contents = new StringBuffer();

		Map<String, String> map = null;
		for (int i = 0; i < answerList.size(); i++) {
			ExcelBean bean = answerList.get(i);

			// 初始化对象
			if (StringUtil.isEmpty(tempUserId)) {
				map = new HashMap<String, String>();
				tempUserId = bean.getUserId();
				tempQid = bean.getQuesId();
			}

			if (bean.getUserId().equals(tempUserId)) {

				// 判断是否有多个答案
				if (bean.getQuesId().equals(tempQid)) {
					contents.append(bean.getAnswerContent()).append(",");
				} else {
					// 先保存前面题目的所有答案
					map.put(tempQid,
							contents.toString().substring(0,
									contents.toString().lastIndexOf(",")));// key：题目id，value：题目答案
					// 然后重置判断条件
					tempQid = bean.getQuesId();
					contents.setLength(0);
					contents.append(bean.getAnswerContent()).append(",");

				}
			} else {
				// 保存换人前用户的工号
				map.put("USERID", tempUserId);
				// 保存题目答案
				map.put(tempQid,
						contents.toString().substring(0,
								contents.toString().lastIndexOf(",")));
				// 人员不一致，保存的员工工号和所做的题目
				contentList.add(map);
				// 重置判断条件
				map = new HashMap<String, String>();
				tempUserId = bean.getUserId();
				tempQid = bean.getQuesId();
				contents.setLength(0);
				contents.append(bean.getAnswerContent()).append(",");
			}

			// 最后一条数据
			if (i == answerList.size() - 1) {
				map.put(tempQid,
						contents.toString().substring(0,
								contents.toString().lastIndexOf(",")));
				map.put("USERID", bean.getUserId());
				contentList.add(map);
			}
		}
	}

	@Override
	public void setExcelData(Workbook workbook,
			List<Map<String, String>> titleList, List<ExcelBean> answerList)
			throws Exception {

		Sheet sheet = null;
		sheet = (XSSFSheet) workbook.getSheetAt(0);

		// 标题
		if (null != titleList && titleList.size() > 0) {
			Row titleRow = null;
			titleRow = sheet.createRow(0);
			titleRow.setHeight((short) 450);
			titleRow.createCell(0).setCellValue("工号");

			for (int i = 0; i < titleList.size(); i++) {
				String title = titleList.get(i).get("TITLE") + "";
				titleRow.createCell(i + 1).setCellValue(title);
			}
		}

		// 过滤数据
		List<Map<String, String>> contentList = new ArrayList<Map<String, String>>();

		filterExceDate(answerList, contentList);

		// 内容
		if (null != contentList && contentList.size() > 0) {

			for (int i = 0; i < contentList.size(); i++) {
				Map<String, String> contentMap = contentList.get(i);

				Row contentRow = null;
				contentRow = sheet.createRow(i + 1);
				contentRow.setHeight((short) 450);

				contentRow.createCell(0).setCellValue(
						contentMap.get("USERID") + "");

				for (int j = 0; j < titleList.size(); j++) {
					String quseId = titleList.get(j).get("ID") + "";
					String answer = contentMap.get(quseId) == null ? ""
							: contentMap.get(quseId);

					contentRow.createCell(j + 1).setCellValue(answer);
				}

			}
		}

	}

	@Override
	public void setExcelData(Workbook workbook, List<ExcelBean> list)
			throws Exception {
		try {
			Sheet sheet = null;
			CellStyle cellStyle = null;
			Font font = null;

			sheet = (XSSFSheet) workbook.getSheetAt(0);
			cellStyle = (XSSFCellStyle) workbook.createCellStyle();
			if (null != list && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					ExcelBean bom = list.get(i);
					Row row = null;
					row = sheet.createRow(i + 1);
					row.setHeight((short) 450);
					row.createCell(0).setCellValue(bom.getUserId());
					row.createCell(1).setCellValue(bom.getVoteId());
					row.createCell(2).setCellValue(bom.getVoteName());
					row.createCell(3).setCellValue(bom.getQuesId());
					row.createCell(4).setCellValue(bom.getQuesContent());
					row.createCell(5).setCellValue(bom.getQuesType());
					row.createCell(6).setCellValue(
							getTypeName(bom.getQuesType()));
					row.createCell(7).setCellValue(bom.getOptionId());
					row.createCell(8).setCellValue(bom.getAnswerContent());

				}
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {

		}
	}

	private String getTypeName(String type) {
		if ("0".equals(type)) {
			return "单选并统计投票";
		} else if ("1".equals(type)) {
			return "多选并统计投票";
		} else if ("2".equals(type)) {
			return "自由回答";
		} else if ("3".equals(type)) {
			return "单选";
		} else if ("4".equals(type)) {
			return "多选";
		}
		return "";
	}

	@Override
	public String queryVoteNameByTitleId(String titleId) throws Exception {

		return executor.queryField("queryVoteNameByTitleId", titleId);
	}

	@Override
	public List<VoteTitle> getVoteListByWorkNo(String userWorkNumber,
			long siteID) throws Exception {

		return executor.queryList(VoteTitle.class, "queryVoteList", siteID,
				userWorkNumber);
	}

	@Override
	public String getVoteCount(String userWorkNumber, String siteName)
			throws Exception {

		long siteID = CMSUtil.getSiteCacheManager().getSiteByEname(siteName)
				.getSiteId();

		return executor.queryField("queryVoteCount", siteID, userWorkNumber);
	}
}
