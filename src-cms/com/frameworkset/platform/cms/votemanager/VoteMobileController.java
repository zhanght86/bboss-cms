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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.servlet.ModelMap;

import com.frameworkset.platform.cms.bean.ExcelBean;
import com.frameworkset.platform.cms.voteservice.VoteMobileService;

/**
 * @author xusy3
 * 问卷服务控制器
 */
public class VoteMobileController {

	
	private VoteMobileService voteMobileService;
	private Logger log = Logger.getLogger(VoteMobileController.class);
	
	/**
	 * 根据问卷ID号查询问卷
	 *  
	 * @param titleId 问卷ID
	 * @param HttpServletRequest request
	 * @return String
	 * @throws Exception Exception
	 */
	public @ResponseBody(datatype="jsonp") void saveVoteDetail(String titleId,String detail,String userId) {
		
		if(null != detail && !"".equals(detail)){
			String [] beans = detail.split(";");
			for(String bean :beans){
				String [] paras = bean.split(",");
				String qid = paras[0].split(":")[1];
				String type = paras[1].split(":")[1];
				String oid = paras[2].split(":").length>1?paras[2].split(":")[1]:"";
				String content = paras[3].split(":")[1];
				String id = UUID.randomUUID().toString();
				try{
					voteMobileService.saveVoteDetail(userId,id,titleId,qid,type,oid,content);
				}catch(Exception e){
					log.error("工号为："+userId+"，问卷号："+titleId+",题号:"+qid+",选项号:"+oid+",内容："+content+"，保存失败", e);
				}
			}
		}
	}
	/**
	根据问卷编号，导出所有用户保存的答案
	*/
	public void  exportExcel(String titleId,ModelMap model,HttpServletRequest request,HttpServletResponse response) {
		
		InputStream in=null;
		Workbook  workbook=null;
		try{
			List<ExcelBean> list =voteMobileService.queryAnswerContent(titleId);
			in =VoteMobileController.class.getResourceAsStream("MobileVoteAnswerExcelModel.xlsx");
			 workbook = new XSSFWorkbook(in);
			 voteMobileService.setExcelData(workbook, list);
			 String voteName = voteMobileService.queryVoteNameByTitleId(titleId);
			 voteName = null == voteName ||"".equals(voteName)?"调查问卷答案明细":voteName;
			 sendFile( request, response,  voteName+".xlsx",workbook,0);
		}catch(Exception e){
			log.error("用户提交问卷答案导出失败,问卷id="+titleId, e);
			
		}finally{
			 if (in != null) {
	                try {
	                    in.close();
	                }
	                catch (IOException e) {
	                    throw new RuntimeException(e.getMessage(), e);
	                }
	            }
		}
	}
	
	public void newExportExcel(String titleId, ModelMap model,
			HttpServletRequest request, HttpServletResponse response) {

		InputStream in = null;
		Workbook workbook = null;
		try {
			// 获取试卷题目
			List<Map<String, String>> titleList = voteMobileService
					.getQuestionTitleById(titleId);
			// 获取试卷答案
			List<ExcelBean> answerList = voteMobileService
					.queryAnswerContent(titleId);

			in = VoteMobileController.class
					.getResourceAsStream("MobileVoteAnswerExcelModel.xlsx");

			workbook = new XSSFWorkbook(in);

			voteMobileService.setExcelData(workbook, titleList, answerList);

			String voteName = voteMobileService.queryVoteNameByTitleId(titleId);

			voteName = null == voteName || "".equals(voteName) ? "调查问卷答案明细"
					: voteName;
			sendFile(request, response, voteName + ".xlsx", workbook, 0);

		} catch (Exception e) {
			log.error("用户提交问卷答案导出失败,问卷id=" + titleId, e);

		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		}
	}
	
	public static void sendFile(HttpServletRequest request, 
			HttpServletResponse response, String filename,
			Workbook workbook,long fileSize) throws Exception {
        OutputStream out = null;
        try {
        	if(workbook == null)
        		return;
        	out = response.getOutputStream();
            response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode(filename,"UTF-8"));//ISO-8859-1
            response.setHeader("Accept-Ranges", "bytes");
            workbook.write(out);
            out.flush();
        }
        catch(Exception e)
        {
        	e.printStackTrace();
        	throw e;
        }
        finally {
            try
			{
            	if(out != null)
            		out.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
        }
    }
	
}
