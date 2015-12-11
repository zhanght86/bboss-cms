package com.frameworkset.platform.cms.votemanager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.frameworkset.util.DataFormatUtil;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.tag.pager.DataInfoImpl;
import com.frameworkset.util.ListInfo;

public class Answerlist extends DataInfoImpl {

	protected ListInfo getDataList(String sortKey, boolean desc, long offset,
			int maxPagesize) {
		ListInfo listInfo = new ListInfo();
		DBUtil db = new DBUtil();
		List res = new ArrayList();
		
		String id = request.getParameter("id");
		//System.out.println("========="+id);

		int titleid = Integer.parseInt(id);
		SimpleDateFormat bartDateFormat = DataFormatUtil.getSimpleDateFormat(request,"yyyy-MM-dd");
		try {
			
			String sql="select a.*,b.title from td_cms_vote_answer a,td_cms_vote_questions b,td_cms_vote_tq c where a.qid=b.id and b.id=c.quesiont_id and a.type=1 and c.title_id="+titleid +"  order by a.when";
			db.executeSelect(sql,(int)offset,maxPagesize);

			for (int i = 0; i < db.size(); i++) {
				Answer answer = new Answer();
				answer.setQtitle(db.getString(i,"title"));
				answer.setAnswer(db.getString(i,"answer"));
				answer.setType(db.getInt(i,"type"));
				answer.setWhen(bartDateFormat.format(db.getDate(i,"when")));
				answer.setWhoIp(db.getString(i,"who_ip"));
				answer.setAnswerID(db.getInt(i,"anser_id"));
				answer.setState(db.getInt(i,"state"));
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
