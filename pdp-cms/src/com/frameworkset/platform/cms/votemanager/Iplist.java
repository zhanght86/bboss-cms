package com.frameworkset.platform.cms.votemanager;

import java.util.ArrayList;
import java.util.List;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class Iplist extends  DataInfoImpl {

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		ListInfo listInfo = new ListInfo();
		DBUtil db = new DBUtil();
		List res = new ArrayList();
		String id = request.getParameter("qid");
		//System.out.println("========="+id);
	   int idd = Integer.parseInt(id);

		try {
			
			String sql = "select a.* ,b.title from td_cms_vote_answer a,td_cms_vote_questions b  where a.qid="+idd+"and a.qid=b.id";
			
			db.executeSelect(sql,(int)offset,maxPagesize);	
			for (int i = 0; i < db.size(); i++) {

				Answer answer = new Answer();

				answer.setAnswer(db.getString(i, "answer"));
				//answer.setAnswerID(db.getInt(i, "answer_id"));
				//answer.setItemId(db.getInt(i,"item_id"));
				answer.setQid(db.getInt(i, "qid"));
				answer.setType(db.getInt(i,"type"));
				answer.setWhen((db.getDate(i,"when")).toString());
				answer.setWhoIp(db.getString(i,"who_ip"));
				answer.setQtitle(db.getString(i,"title"));
				res.add(answer);
				
				

			}

			
			listInfo.setDatas(res);
			listInfo.setTotalSize(db.getTotalSize());
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		
		return listInfo;
	}
	
	protected ListInfo getDataList(String arg0, boolean arg1) {
		return null;
	}


}
