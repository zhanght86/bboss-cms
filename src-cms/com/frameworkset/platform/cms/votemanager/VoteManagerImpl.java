package com.frameworkset.platform.cms.votemanager;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.transaction.RollbackException;

import org.frameworkset.spi.SPIException;

import com.frameworkset.common.poolman.DBUtil;
import com.frameworkset.common.poolman.PreparedDBUtil;
import com.frameworkset.orm.transaction.TransactionManager;
import com.frameworkset.platform.cms.channelmanager.ChannelManager;
import com.frameworkset.platform.cms.channelmanager.ChannelManagerImpl;
import com.frameworkset.platform.sysmgrcore.exception.ManagerException;
import com.frameworkset.platform.sysmgrcore.manager.LogManager;
import com.frameworkset.platform.sysmgrcore.manager.SecurityDatabase;
import com.frameworkset.platform.util.CommonUtil;

public class VoteManagerImpl implements VoteManager {

	/**
	 * 取得问卷
	 * 
	 * @param titleID
	 * @return
	 */
	public Title getSurveyBy(int titleID) throws VoteManagerException {
		try {
			DBUtil dbUtil = new DBUtil();
			
			String sql = "select a.*,d.name as chname,d.CHANNEL_ID,e.disposedep,f.user_name  from td_cms_vote_title a,TD_CMS_CHANNEL_VOTE c,TD_CMS_CHANNEL d," +
					"td_comm_email_disposedep e,td_sm_user f where e.id=a.depart_id and  a.id=c.VOTE_TITLE_ID(+) and c.CHANNEL_ID=d.CHANNEL_ID(+) and a.founder_id=f.user_id(+) and a.ID=" + titleID;
			dbUtil.executeSelect(sql);

			Title item = new Title();

			if (dbUtil.size() > 0) {
				item.setId(dbUtil.getInt(0, "id"));
				item.setContent(dbUtil.getString(0, "content"));
				item.setFounderID(dbUtil.getInt(0, "founder_id"));
				item.setTimeGap(dbUtil.getInt(0, "time_Gap"));
				item.setName(dbUtil.getString(0, "name"));
				item.setIpRepeat(dbUtil.getInt(0, "ip_Repeat"));
				item.setSiteid(dbUtil.getInt(0, "siteid"));
				item.setPicpath(dbUtil.getString(0,"picpath"));
				item.setDepart_id(dbUtil.getString(0,"depart_id"));
				item.setChannelID(String.valueOf(dbUtil.getInt(0, "CHANNEL_ID")));
				item.setChannelName(String.valueOf(dbUtil.getString(0, "chname")));
				item.setIsTop(dbUtil.getInt(0, "istop"));
				item.setFoundDate(dbUtil.getDate(0, "ctime").toString());
				item.setIpCtrls(getIPctrlOfServey(dbUtil.getInt(0, "id")));
				item.setTimeCtrls(getTimectrlOfServey(dbUtil.getInt(0, "id")));
				item.setQuestions(getQstionsOfServey(dbUtil.getInt(0, "id")));
				item.setIslook(dbUtil.getInt(0,"islook"));
				item.setDepart_name(dbUtil.getString(0,"disposedep"));
				item.setFoundername(dbUtil.getString(0,"user_name"));
				if(item.getTimeCtrls()!=null&& item.getTimeCtrls().size()>0){
					try{
					TimeCtrl timeCtrl=(TimeCtrl)item.getTimeCtrls().get(0);
					SimpleDateFormat dateformt = new SimpleDateFormat("yyyy-MM-dd");
			    	Date expriedtime_ = dateformt.parse(timeCtrl.getTimeEnd());
			    	String endStr=CommonUtil.getDaysBetween(new Date(),expriedtime_)+"天"
			    	  + ((expriedtime_.getTime() -new Date().getTime())/1000/60/60%24)+"小时"
			    	  + ((expriedtime_.getTime() -new Date().getTime())/1000/60%60)+"分";
					item.setEndTime(endStr);
					}catch(Exception ex){
						
					}
				}
				
			}

			return item;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}
	
	/**
	 * 取得问题
	 * 
	 * @param titleID
	 * @return
	 */
	public Question getQuestionBy(int qID) throws VoteManagerException {
		try {
			DBUtil dbUtil = new DBUtil();
			
			String sql = "select * from td_cms_vote_questions where  ID=" + qID;
			dbUtil.executeSelect(sql);

			Question item = null;
			if (dbUtil.size() > 0) {
				item = new Question();
				item.setId(dbUtil.getInt(0, "id"));
				item.setStyle(dbUtil.getInt(0, "style"));
				item.setTitle(dbUtil.getString(0, "title"));
				item.setAnswers(getAnswersOfQstion(qID));
				item.setActive(dbUtil.getInt(0, "active"));
				item.setItems(getItemsOfQstion(qID));
				item.setVotecount(dbUtil.getInt(0, "votecount"));
				item.setIsTop(dbUtil.getInt(0, "istop"));
			}

			return item;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}
	
	/**
	 * 取得问题
	 * 
	 * @param titleID
	 * @return
	 */
	public Question getPureQuestionBy(int qID) throws VoteManagerException {
		try {
			DBUtil dbUtil = new DBUtil();
			
			String sql = "select * from td_cms_vote_questions where  ID=" + qID;
			dbUtil.executeSelect(sql);

			Question item = null;
			if (dbUtil.size() > 0) {
				item = new Question();
				item.setId(dbUtil.getInt(0, "id"));
				item.setStyle(dbUtil.getInt(0, "style"));
				item.setTitle(dbUtil.getString(0, "title"));
				//item.setAnswers(getAnswersOfQstion(qID));
				item.setActive(dbUtil.getInt(0, "active"));
				item.setItems(getItemsOfQstion(qID));
				item.setVotecount(dbUtil.getInt(0, "votecount"));
				item.setIsTop(dbUtil.getInt(0, "istop"));
			}

			return item;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}

	/**
	 * 取得问题选项
	 * 
	 * @param titleID
	 * @return
	 */
	public List getItemsOfQstion(int questionID) throws VoteManagerException {
		try {
			List res = new ArrayList();
			DBUtil dbUtil = new DBUtil();
			String sql = "select * from td_cms_vote_items where  qID="
					+ String.valueOf(questionID)+" order by id";
			dbUtil.executeSelect(sql);

			Item item = null;
			for (int i = 0; dbUtil.size() > 0 && i < dbUtil.size(); i++) {
				item = new Item();
				item.setId(dbUtil.getInt(i, "id"));
				item.setOptions(dbUtil.getString(i, "options"));
				item.setQid(dbUtil.getInt(i, "qid"));
				item.setCount(dbUtil.getInt(i, "count"));

				res.add(item);
			}
			
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}

	/**
	 * 取得问卷问题
	 * 
	 * @param titleID
	 * @return
	 */
	public List getQstionsOfServey(int titleID) throws VoteManagerException {
		try {
			List res = new ArrayList();

			DBUtil dbUtil = new DBUtil();
			String sql = "select * from TD_CMS_VOTE_QUESTIONS a,TD_CMS_VOTE_TQ b where  a.id=b.QUESIONT_ID and b.title_id="
					+ String.valueOf(titleID)+" order by a.id";
			dbUtil.executeSelect(sql);

			Question item = null;
			for (int i = 0; dbUtil.size() > 0 && i < dbUtil.size(); i++) {
				item = new Question();
				item.setId(dbUtil.getInt(i, "QUESIONT_ID"));
				item.setActive(dbUtil.getInt(i, "ACTIVE"));
				item.setAnswers(getAnswersOfQstion(dbUtil.getInt(i, "QUESIONT_ID")));
				item.setItems(getItemsOfQstion(dbUtil.getInt(i, "QUESIONT_ID")));
				item.setStyle(dbUtil.getInt(i, "STYLE"));
				item.setTitle(dbUtil.getString(i, "TITLE"));
				item.setTitleID(dbUtil.getInt(i, "TITLE_ID"));
				item.setVotecount(dbUtil.getInt(i, "VOTECOUNT"));
				item.setIsTop(dbUtil.getInt(i, "istop"));

				res.add(item);
			}
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}
	
	/**
	 * 取得问卷问题
	 * 
	 * @param titleID
	 * @return
	 */
	public List getActiveQstionsOfServey(int titleID) throws VoteManagerException {
		try {
			List res = new ArrayList();

			DBUtil dbUtil = new DBUtil();
			String sql = "select * from TD_CMS_VOTE_QUESTIONS a,TD_CMS_VOTE_TQ b where a.active=1 and a.id=b.QUESIONT_ID and b.title_id="
					+ String.valueOf(titleID);
			dbUtil.executeSelect(sql);

			Question item = null;
			for (int i = 0; dbUtil.size() > 0 && i < dbUtil.size(); i++) {
				item = new Question();
				item.setId(dbUtil.getInt(i, "QUESIONT_ID"));
				item.setActive(dbUtil.getInt(i, "ACTIVE"));
				item.setAnswers(getAnswersOfQstion(dbUtil.getInt(i, "QUESIONT_ID")));
				item.setItems(getItemsOfQstion(dbUtil.getInt(i, "QUESIONT_ID")));
				item.setStyle(dbUtil.getInt(i, "STYLE"));
				item.setTitle(dbUtil.getString(i, "TITLE"));
				item.setTitleID(dbUtil.getInt(i, "TITLE_ID"));
				item.setVotecount(dbUtil.getInt(i, "VOTECOUNT"));
				item.setIsTop(dbUtil.getInt(i, "istop"));

				res.add(item);
			}
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}

	/**
	 * 取得问题答案
	 * 
	 * @param questionID
	 * @return
	 */
	public List getAnswersOfQstion(int questionID) throws VoteManagerException {
		try {
			List res = new ArrayList();

			DBUtil dbUtil = new DBUtil();
			String sql = "select * from TD_CMS_VOTE_ANSWER  where  QID="
					+ String.valueOf(questionID);
			dbUtil.executeSelect(sql);

			Answer item ;
			for (int i = 0; dbUtil.size() > 0 && i < dbUtil.size(); i++) {
				item = new Answer();
				item.setAnswer(dbUtil.getString(i, "ANSWER"));
				item.setAnswerID(dbUtil.getInt(i, "ANSER_ID"));
				item.setIsBigTitle(dbUtil.getInt(i, "ISBIGTITLE"));
				item.setItemId(dbUtil.getInt(i, "ITEM_ID"));
				item.setQid(dbUtil.getInt(i, "QID"));
				item.setType(dbUtil.getInt(i, "TYPE"));
				item.setWhen(dbUtil.getString(i, "WHEN"));
				item.setWhoIp(dbUtil.getString(i, "WHO_IP"));
				item.setState(dbUtil.getInt(i,"state"));

				res.add(item);
			}
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}
	/**
	 * 分页查找记录
	 * @param
	 * @return
	 */
	public List getAnswersOfQstionListInfo(int questionID,long offset, int pagesize ) throws VoteManagerException{
		List res = new ArrayList();
		try{
			DBUtil dbUtil = new DBUtil();
			StringBuffer sql=new StringBuffer("select * from TD_CMS_VOTE_ANSWER  where  QID=").append(questionID).append(" order by anser_id desc");
			dbUtil.executeSelect(sql.toString(), offset, pagesize);

			Answer item ;
			for (int i = 0; dbUtil.size() > 0 && i < dbUtil.size(); i++) {
				item = new Answer();
				item.setAnswer(dbUtil.getString(i, "ANSWER"));
				item.setAnswerID(dbUtil.getInt(i, "ANSER_ID"));
				item.setIsBigTitle(dbUtil.getInt(i, "ISBIGTITLE"));
				item.setItemId(dbUtil.getInt(i, "ITEM_ID"));
				item.setQid(dbUtil.getInt(i, "QID"));
				item.setType(dbUtil.getInt(i, "TYPE"));
				item.setWhen(dbUtil.getString(i, "WHEN"));
				item.setWhoIp(dbUtil.getString(i, "WHO_IP"));
				item.setState(dbUtil.getInt(i,"state"));
				res.add(item);
			}
			
		}catch(SQLException e){
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());
		}
		return res;
	}
	/**
	 * 取得问卷的IP限制段
	 * 
	 * @param titleID
	 * @return
	 */
	public List getIPctrlOfServey(int titleID) throws VoteManagerException {
		try {
			List res = new ArrayList();

			DBUtil dbUtil = new DBUtil();
			String sql = "select * from td_cms_vote_ipctrl  where  TITLE_ID="
					+ String.valueOf(titleID);
			dbUtil.executeSelect(sql);

			IpCtrl item = null;
			for (int i = 0; dbUtil.size() > 0 && i < dbUtil.size(); i++) {
				item = new IpCtrl();
				item.setId(dbUtil.getInt(i, "id"));
				item.setIpEnd(dbUtil.getString(i, "IP_END"));
				item.setIpStart(dbUtil.getString(i, "IP_START"));
				item.setTitleId(dbUtil.getInt(i, "TITLE_ID"));

				res.add(item);
			}
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}

	/**
	 * 取得问卷的时间限制段
	 * 
	 * @param titleID
	 * @return
	 */
	public List getTimectrlOfServey(int titleID) throws VoteManagerException {
		try {
			List res = new ArrayList();
			SimpleDateFormat sdf = new SimpleDateFormat();
			sdf.applyPattern("yyyy-MM-dd");
			 
			DBUtil dbUtil = new DBUtil();
			String sql = "select * from TD_CMS_VOTE_TIMECTRL  where  TITLE_ID="
					+ String.valueOf(titleID);
			dbUtil.executeSelect(sql);

			TimeCtrl item = null;
			for (int i = 0; dbUtil.size() > 0 && i < dbUtil.size(); i++) {
				item = new TimeCtrl();
				item.setId(dbUtil.getInt(i, "id"));
				item.setTimeStart(sdf.format(dbUtil.getDate(i, "TIME_START")));
				item.setTimeEnd(sdf.format(dbUtil.getDate(i, "TIME_END")));
				item.setTitleId(dbUtil.getInt(i, "TITLE_ID"));

				res.add(item);
			}
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}

	/**
	 * 增加问卷
	 * 
	 * @param titleID
	 * @return
	 * @throws VoteManagerException
	 * @throws Exception
	 */
	public int insertSurvey(Title title,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException {
		try {
			PreparedDBUtil conn = new PreparedDBUtil();
			String sqlInsert = "insert into td_cms_vote_title(content,name,siteid,ip_repeat,time_gap,founder_id,picpath,depart_id,ctime,id)" +
					" values(?,?,?,?,?,?,?,?,sysdate,?)";
			
			long id = conn.getNextPrimaryKey("td_cms_vote_title") ;
			
			conn.preparedInsert(sqlInsert);
			conn.setClob(1,title.getContent());
			conn.setString(2,title.getName());
			conn.setInt(3,title.getSiteid());
			conn.setInt(4,title.getIpRepeat());
			conn.setInt(5,title.getTimeGap());
			conn.setInt(6,title.getFounderID());
			conn.setString(7,title.getPicpath());
			conn.setString(8,title.getDepart_id());
			conn.setLong(9,id);
			conn.executePrepared() ;
			
			int titleID = (int) id ;
			
//			String titleID = conn.executeInsert(sqlInsert).toString();
			DBUtil db = new DBUtil();
			if (title.getChannelID()!=null&&!"".equals(title.getChannelID())){
				sqlInsert = "insert into TD_CMS_CHANNEL_VOTE(CHANNEL_ID,VOTE_TITLE_ID)values("+title.getChannelID()+","+titleID+")";
				db.executeInsert(sqlInsert);
			}
			
			List qids = new ArrayList();
			
			List list = title.getQuestions();
			for (int i = 0; i < list.size(); i++) {
				long questionId = db.getNextPrimaryKey("TD_CMS_VOTE_QUESTIONS") ;
				
				Question question = (Question) list.get(i);
				
				sqlInsert = "insert into TD_CMS_VOTE_QUESTIONS(TITLE,STYLE,ID)values('"
						+ question.getTitle()
						+ "',"
						+ question.getStyle()
						+ ","
						+ questionId +") ";
				db.addBatch(sqlInsert);
				
				qids.add(questionId+"");
			}
			
			if (list.size()>0)
				db.executeBatch();

			list = title.getQuestions();
			for (int i = 0; i < list.size(); i++) {
				Question question = (Question) list.get(i);
				sqlInsert = "insert into TD_CMS_VOTE_TQ(TITLE_ID,QUESIONT_ID)values("
						+ titleID + "," + qids.get(i).toString() + ") ";
				db.addBatch(sqlInsert);
			}
			if (list.size()>0)
				db.executeBatch();

			list = title.getIpCtrls();
			for (int i = 0; i < list.size(); i++) {
				IpCtrl ipctrl = (IpCtrl) list.get(i);
				
				long ipctrlID = db.getNextPrimaryKey("TD_CMS_VOTE_IPCTRL") ;
				
				sqlInsert = "insert into TD_CMS_VOTE_IPCTRL(TITLE_ID,IP_START,IP_end,ID)values("
						+ titleID
						+ ",'"
						+ ipctrl.getIpStart()
						+ "','"
						+ ipctrl.getIpEnd() + "',"+ ipctrlID +") ";
				db.addBatch(sqlInsert);
			}
			if (list.size()>0)
				db.executeBatch();

			list = title.getTimeCtrls();
			for (int i = 0; i < list.size(); i++) {
				
				long timeCtrlID = db.getNextPrimaryKey("TD_CMS_VOTE_TIMECTRL") ;
				
				TimeCtrl timectrl = (TimeCtrl) list.get(i);
				sqlInsert = "insert into TD_CMS_VOTE_TIMECTRL(TITLE_ID,TIME_START,TIME_END,ID)values("
						+ titleID
						+ ","
						+ "to_date('"+timectrl.getTimeStart()+"','YYYY-MM-DD')"
						+ ","
						+ "to_date('"+timectrl.getTimeEnd()+"','YYYY-MM-DD'),"
						+ timeCtrlID +")" ;
				db.addBatch(sqlInsert);
			}
			if (list.size()>0)
				db.executeBatch();

			list = title.getQuestions();
			for (int i = 0; i < list.size(); i++) {
				Question question = (Question) list.get(i);
				List itemlist = question.getItems();
				for (int j = 0; itemlist!=null&&j < itemlist.size(); j++) {
					Item item = (Item) itemlist.get(j);
					
					long itemsID = db.getNextPrimaryKey("TD_CMS_VOTE_ITEMS") ;
					
					sqlInsert = "insert into TD_CMS_VOTE_ITEMS(QID,OPTIONS,ID)values("
							+ qids.get(i).toString()
							+ ",'"
							+ item.getOptions()
							+ "',"
							+ itemsID +") ";
					db.addBatch(sqlInsert);
				}
				if (itemlist!=null && itemlist.size()>0)
					db.executeBatch();
			}

            //---------------START--网上调查新增操作日志
			String operContent="";       
		    String openModle="网上调查";
		    String description="";
		    LogManager logManager = SecurityDatabase.getLogManager(); 		
			operContent="新增调查信息: "+"调查信息主题为 "+ title.getName();
			logManager.log(account+":"+userName,operContent,openModle,ipAddress,description);  
		    //---------------END
			return 1;
            
		} catch (Exception e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}

	}

	/**
	 * 修改问卷
	 * 
	 * @param titleID
	 * @return
	 */
	public int modifySurvey(Title title,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException {
		try {
            PreparedDBUtil conn = new PreparedDBUtil();
			String sqlDelete = "";
			String sqlInsert = "";
			int titleID = title.getId();
//			String sqlUpdate = "update  td_cms_vote_title set content=?"
//				    + ",name='" + title.getName()
//					+ "',siteid='" + title.getSiteid() + "',ip_repeat='"
//					+ title.getIpRepeat() + "',time_gap='" + title.getTimeGap()+ "'"
//					+" where id=" + title.getId();
//			conn.preparedUpdate(sqlUpdate);
//			conn.setClob(1,title.getContent(),"content");
//			conn.executePrepared();
			
			String sql = "";
			sql = "update td_cms_vote_title set name='"+title.getName()+"',siteid="+title.getSiteid()+","
			      +"ip_repeat="+title.getIpRepeat()+",time_gap="+title.getTimeGap()+",picpath='"+title.getPicpath()+"',"
			      +"content=?,depart_id=?,ctime=to_date('"+title.getFoundDate()+"','yyyy-mm-dd hh24:mi:ss') where id =?";
			conn.preparedUpdate(sql);
			conn.setClob(1,title.getContent(),"content");
			conn.setString(2,title.getDepart_id());
			conn.setPrimaryKey(3,title.getId(),"id");
			conn.executePrepared();
			
			DBUtil db = new DBUtil();
			sqlDelete = "delete from TD_CMS_CHANNEL_VOTE where VOTE_TITLE_ID=" + title.getId();
			db.executeDelete(sqlDelete);
			if (title.getChannelID()!=null&&!"".equals(title.getChannelID())){
				sqlInsert = "insert into TD_CMS_CHANNEL_VOTE(CHANNEL_ID,VOTE_TITLE_ID)values("+title.getChannelID()+","+title.getId()+")";
				db.executeInsert(sqlInsert);
			}
			//判断题目是否被修改
			String sqlSelect = "select QUESIONT_ID from TD_CMS_VOTE_TQ where TITLE_ID="	+ title.getId();
			db.executeSelect(sqlSelect);
			String deleteQIDs = "-1";
			List list = title.getQuestions();
			for(int i=0;i<db.size();i++){
				//int o= 0;
				//for (int j=0;j<list.size();j++){
				//	Question q = (Question)list.get(j);
				//	if(!isQuestionChanged(db.getInt(i,"QUESIONT_ID"),q)){
				//		o = 1;
				//		break;
				//	}
				//}
				//if (o==0){
				deleteQIDs += ","+String.valueOf(db.getInt(i,"QUESIONT_ID"));
				//}
			}
//			for(int i=0;i<list.size();i++){
//				for (int j=0;j<db.size();j++){
//					Question q = (Question)list.get(i);
//					if(!isQuestionChanged(db.getInt(j,"QUESIONT_ID"),q)){
//						list.remove(i);
//						i--;
//						break;
//					}
//				}
//			}
			sqlDelete = "delete from td_cms_vote_answer where QID in ("+deleteQIDs+")";
			db.executeDelete(sqlDelete);
			sqlDelete = "delete from TD_CMS_VOTE_ITEMS where QID in ("+deleteQIDs+")";
			db.executeDelete(sqlDelete);
			sqlDelete = "delete from TD_CMS_VOTE_TQ where QUESIONT_ID in ("+deleteQIDs+")";
			db.executeDelete(sqlDelete);
			sqlDelete = "delete from TD_CMS_VOTE_QUESTIONS where id in ("+deleteQIDs+")";
			db.executeDelete(sqlDelete);
//			String sqlUpdate = "";
//			for (int i = 0; i < list.size(); i++) {
//				Question question = (Question) list.get(i);
//				String deleteQIDs_array[] = deleteQIDs.split(",");
//				for(int k=0;k<deleteQIDs_array.length;k++)
//				{
//					if(deleteQIDs_array[k].equals(Integer.toString(question.getId()))&&!deleteQIDs_array[k].equals("-1"))
//					{
//						sqlUpdate = "update TD_CMS_VOTE_QUESTIONS set title='"+question.getTitle()+"',style="+question.getStyle()+",votecount="+question.getVotecount()
//							+ " where id="+question.getId();
//						db.addBatch(sqlUpdate);
//					}
//				}
//			}
//			db.executeBatch();
			/*String sql_update = "";
			for (int i = 0; i < list.size(); i++) {
				Question question = (Question) list.get(i);
				String deleteQIDs_array[] = deleteQIDs.split(",");
				for(int k=0;k<deleteQIDs_array.length;k++)
				{
					if(deleteQIDs_array[k].equals(Integer.toString(question.getId())))
					{
						List itemlist = question.getItems();
						for (int j = 0; itemlist != null && j < itemlist.size(); j++) {
							Item item = (Item) itemlist.get(j);
							if(item.getId()>=0)
							{
								sql_update = "update TD_CMS_VOTE_ITEMS set qid="+question.getId()+",options='"+item.getOptions()+"' where id="+item.getId();
								db.addBatch(sql_update);
							}
						}
						if (itemlist != null && itemlist.size() > 0)
							db.executeBatch();
					}
				}
			}*/
			
			List qids = new ArrayList();
			
			for (int i = 0; i < list.size(); i++) {
				Question question = (Question) list.get(i);
				
				long questionId = db.getNextPrimaryKey("TD_CMS_VOTE_QUESTIONS") ;
				
				sqlInsert = "insert into TD_CMS_VOTE_QUESTIONS(TITLE,STYLE,VOTECOUNT,ID) values('"
					+ question.getTitle()
					+ "',"
					+ question.getStyle()
					+ ","
					+ question.getVotecount() 
					+ "," 
					+ questionId +") ";
				
				db.addBatch(sqlInsert);
				
				qids.add(questionId+"") ;
			}
			
			if (list.size() > 0)
				db.executeBatch();
				
			for (int i = 0; i < list.size(); i++) {
				Question question = (Question) list.get(i);
				List itemlist = question.getItems();
				for (int j = 0; itemlist != null && j < itemlist.size(); j++) {
					Item item = (Item) itemlist.get(j);
					
					long itemID = db.getNextPrimaryKey("TD_CMS_VOTE_ITEMS") ;
					
					sqlInsert = "insert into TD_CMS_VOTE_ITEMS(QID,OPTIONS,count,ID) values("
							+ qids.get(i).toString()
							+ ",'"
							+ item.getOptions()
							+ "',"+item.getCount()+","
							+ itemID +") ";
					db.addBatch(sqlInsert);
				}
				if (itemlist != null && itemlist.size() > 0)
					db.executeBatch();
			}

			for (int i = 0; i < list.size(); i++) {
				Question question = (Question) list.get(i);
				sqlInsert = "insert into TD_CMS_VOTE_TQ(TITLE_ID,QUESIONT_ID)values("
						+ titleID + "," + qids.get(i).toString() + ") ";
				db.addBatch(sqlInsert);
			}
			if (list.size()>0)
				db.executeBatch();

			sqlDelete = "delete from TD_CMS_VOTE_IPCTRL where TITLE_ID="
					+ title.getId();
			db.executeDelete(sqlDelete);

			list = title.getIpCtrls();
			for (int i = 0; i < list.size(); i++) {
				IpCtrl ipctrl = (IpCtrl) list.get(i);
				
				long ipCtrlID = db.getNextPrimaryKey("TD_CMS_VOTE_IPCTRL") ;
				
				sqlInsert = "insert into TD_CMS_VOTE_IPCTRL(TITLE_ID,IP_START,IP_end,ID) values("
						+ titleID
						+ ",'"
						+ ipctrl.getIpStart()
						+ "','"
						+ ipctrl.getIpEnd() + "',"+ ipCtrlID +") ";
				db.addBatch(sqlInsert);
			}
			if (list.size()>0)
				db.executeBatch();

			sqlDelete = "delete from TD_CMS_VOTE_TIMECTRL where TITLE_ID="
					+ title.getId();
			db.executeDelete(sqlDelete);

			list = title.getTimeCtrls();
			for (int i = 0; i < list.size(); i++) {
				TimeCtrl timectrl = (TimeCtrl) list.get(i);
				
				long timeCtrlID = db.getNextPrimaryKey("TD_CMS_VOTE_TIMECTRL") ;
				
				sqlInsert = "insert into TD_CMS_VOTE_TIMECTRL(TITLE_ID,TIME_START,TIME_END,ID) values("
						+ titleID
						+ ","
						+"to_date('"+timectrl.getTimeStart()+"','YYYY-MM-DD')"
						+ ","
						+ "to_date('"+timectrl.getTimeEnd()+"','YYYY-MM-DD')" 
						+ ","
						+ timeCtrlID +") ";
				db.addBatch(sqlInsert);
			}
			if (list.size()>0)
				db.executeBatch();

            //---------------START--网上调查修改操作日志
			String operContent="";       
		    String openModle="网上调查";
		    String description="";
		    LogManager logManager = SecurityDatabase.getLogManager(); 		
			operContent="修改调查信息："+"调查信息主题为 "+ title.getName();
			logManager.log(account+":"+userName,operContent,openModle,ipAddress,description);  
		    //---------------END
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}

	/**
	 * 删除问卷
	 * 
	 * @param titleID
	 * @return
	 */
	public int deleteSurveyBy(String titleIDs,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException {
		DBUtil db = new DBUtil();
//		---------------START--网上调查删除操作日志
		String operContent="";        
	    String openModle="网上调查";
	    String description="";
	    LogManager logManager = SecurityDatabase.getLogManager(); 		
		operContent="删除调查信息: "+"信息主题为 "+ getTitlesById(titleIDs.substring(titleIDs.indexOf(',')+1));
	    //---------------END
		try {
			String sql = "select QUESIONT_ID from TD_CMS_VOTE_TQ where TITLE_ID in ("+titleIDs+")";
			db.executeSelect(sql);
			String qids = "-1";
			for (int i=0;i<db.size();i++)
				qids +=","+ String.valueOf(db.getInt(i,"QUESIONT_ID"));
			
			sql = "delete from td_cms_vote_tq where TITLE_ID in ("+titleIDs+")";
			db.executeDelete(sql);
			sql = "delete from td_cms_vote_answer where qID in ("+qids+")";
			db.executeDelete(sql);
			sql = "delete from TD_CMS_VOTE_ITEMS where QID in ("+qids+")";
			db.executeDelete(sql);
			
			String sqlInsert = "delete from td_cms_vote_questions where ID in("+qids+")" ;
			db.executeDelete(sqlInsert);

			sqlInsert = "delete from TD_CMS_VOTE_IPCTRL where TITLE_ID in("+titleIDs+")" ;
			db.executeDelete(sqlInsert);
			
			sqlInsert = "delete from TD_CMS_VOTE_TIMECTRL where TITLE_ID in("+titleIDs+")" ;
			db.executeDelete(sqlInsert);

			sql = "delete from TD_CMS_CHANNEL_VOTE where VOTE_TITLE_ID in ("+titleIDs+")";
			db.executeDelete(sql);
			
			sqlInsert = "delete from td_cms_vote_title where ID in("+titleIDs+")" ;
			db.executeDelete(sqlInsert);
            //---------------START--网上调查删除操作日志
			logManager.log(account+":"+userName,operContent,openModle,ipAddress,description);  
		    //---------------END
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}

	}

//	public static void main(String[] args) throws ParseException {
//
//		List qst = new ArrayList();
//
//		Title title = new Title();
//
//		title.setContent("测试内容");
//		title.setName("测试大标题");
//		title.setSiteid(1);
//		title.setIpRepeat(1);
//		title.setTimeGap(1);
//		title.setFounderID(1);
//
//		for (int i = 0; i < 2; i++) {
//			
//			Question question = new Question();
//			question.setActive(1);
//			question.setStyle(0);
//			question.setTitle("测试小标题" + i);
//
//			
//			List ite = new ArrayList();
//			for (int j = 0; j < 3; j++) {
//				Item item = new Item();
//				item.setOptions("测试选项" + j);
//				ite.add(item);
//			}
//
//			
//			question.setItems(ite);
//			qst.add(question);
//		}
//		title.setQuestions(qst);
//
//		List ipt = new ArrayList();
//
//		for (int j = 0; j < 2; j++) {
//			IpCtrl ipctrl = new IpCtrl();
//			ipctrl.setIpEnd("127.0.0." + j);
//			ipctrl.setIpStart("127.0.1." + j);
//			ipt.add(ipctrl);
//		}
//		title.setIpCtrls(ipt);
//
//		List tim = new ArrayList();
//
//		for (int j = 0; j < 2; j++) {
//			TimeCtrl timeCtrls = new TimeCtrl();
//			//SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
//			//timeCtrls.setTimeStart(sm.parse("2005-07-01"));
//			//timeCtrls.setTimeEnd(sm.parse("2005-07-02"));
//			timeCtrls.setTimeStart("2005-07-01");
//			timeCtrls.setTimeEnd("2005-07-02");
//			tim.add(timeCtrls);
//		}
//		title.setTimeCtrls(tim);
//
//		
//		VoteManagerImpl vote = new VoteManagerImpl();
//		try {
//			vote.insertSurvey(title);
//		} catch (VoteManagerException e) {
//			
//			e.printStackTrace();
//		}
//
//	}
	
	/**
	 * 投票
	 * 
	 * @param titleID
	 * @return
	 */
	public int doVote(int optionID) throws VoteManagerException {
		try {
			DBUtil dbUtil = new DBUtil();
			
			String sql = "update td_cms_vote_items set count=count+1 where  ID=" + optionID;
			dbUtil.executeUpdate(sql);
			
			sql = "select qid from td_cms_vote_items where ID=" + optionID;
			dbUtil.executeSelect(sql);

			sql = "update td_cms_vote_questions set votecount=votecount+1 where  ID in (select qid from td_cms_vote_items where ID=" + optionID+")" + dbUtil.getInt(0,"id");
			dbUtil.executeUpdate(sql);

			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}
	
	/**
	 * 投票
	 * 
	 * @param titleID
	 * @return
	 */
	public int doVote(String strOptionID,String ip) throws VoteManagerException {
		try {
			String[] optionIDs = strOptionID.split(";");
			DBUtil dbUtil = new DBUtil();
			
			String ids = "";
			for (int i=0;i<optionIDs.length;i++){
				ids += " or ID=" + optionIDs[i];
			}
			String sql = "update td_cms_vote_items set count=count+1 where  1=2"+ids;
			dbUtil.executeUpdate(sql);
			
			sql = "update td_cms_vote_questions set votecount=votecount+1 where  ID in (select qid from td_cms_vote_items where 1=2"+ids+")";
			dbUtil.executeUpdate(sql);
			
			sql = "select QID,0,'"+ip+"',sysdate,ID from TD_CMS_VOTE_ITEMS where  1=2"+ids;
			dbUtil.executeSelect(sql);
			for (int i=0;i<dbUtil.size();i++){
				DBUtil db = new DBUtil();
				long id = DBUtil.getNextPrimaryKey("TD_CMS_VOTE_ANSWER");
				sql = "insert into TD_CMS_VOTE_ANSWER(ANSER_ID,QID,TYPE,WHO_IP,WHEN,ITEM_ID)values" +
						"("+id+","+dbUtil.getInt(i,"qid")+",0,'"+ip+"',sysdate,"+dbUtil.getInt(i,"id")+")";
				db.executeInsert(sql);
			}
			

			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}
	/**
	 * 投票
	 * 
	 * @param strOptionId 单选，多选答案,question文本答案<问题编号，问题答案>,IP地址
	 * @return
	 */
	public int doVote(String strOptionID,Map<String,String>questionAnswer,String ip) throws VoteManagerException{
		TransactionManager tm = new TransactionManager();
		try {
		
			String[] optionIDs = strOptionID.split(";");
			DBUtil dbUtil = new DBUtil();
			
			String ids = "";
			for (int i=0;i<optionIDs.length;i++){
				ids += " or ID=" + optionIDs[i];
			}
			
			tm.begin();
			String sql = "update td_cms_vote_items set count=count+1 where  1=2"+ids;
			dbUtil.executeUpdate(sql);
			
			sql = "update td_cms_vote_questions set votecount=votecount+1 where  ID in (select qid from td_cms_vote_items where 1=2"+ids+")";
			dbUtil.executeUpdate(sql);
			
			sql = "select QID,0,'"+ip+"',sysdate,ID from TD_CMS_VOTE_ITEMS where  1=2"+ids;
			dbUtil.executeSelect(sql);
			for (int i=0;i<dbUtil.size();i++){
				DBUtil db = new DBUtil();
				long id = DBUtil.getNextPrimaryKey("TD_CMS_VOTE_ANSWER");
				sql = "insert into TD_CMS_VOTE_ANSWER(ANSER_ID,QID,TYPE,WHO_IP,WHEN,ITEM_ID)values" +
						"("+id+","+dbUtil.getInt(i,"qid")+",0,'"+ip+"',sysdate,"+dbUtil.getInt(i,"id")+")";
				db.executeInsert(sql);
			}
			Iterator<Map.Entry<String, String>> it=questionAnswer.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<String, String> map=it.next();
				doAnswer(Integer.parseInt(map.getKey()),map.getValue(),ip);
			}
			tm.commit();
			return 1;
		} catch (Exception e) {
			try {
				tm.rollback();
			} catch (RollbackException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}
	
	/**
	 * 清空投票结果
	 * 
	 * @param titleID
	 * @return
	 */
	public int clearVote(String titleIds,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException
	{
		int ret = 0;
		DBUtil db = new DBUtil();
		try {
			String questionSql = "update td_cms_vote_questions set votecount=0 where id in" +
					"(select quesiont_id from td_cms_vote_tq where title_id in("+titleIds+"))";
			db.executeUpdate(questionSql);
			String itemSql = "update td_cms_vote_items set count=0 where qid in" +
					"(select quesiont_id from td_cms_vote_tq where title_id in("+titleIds+"))";
			db.executeUpdate(itemSql);
			String answerSql = "delete from td_cms_vote_answer where qid in" +
					"(select quesiont_id from td_cms_vote_tq where title_id in("+titleIds+"))";
			db.executeDelete(answerSql);
            //---------------START--网上调查清零操作日志
			String operContent="";        
		    String openModle="网上调查";
		    String description="";
		    LogManager logManager = SecurityDatabase.getLogManager(); 		
			operContent="投票清零: "+"信息主题为 "+ getTitlesById(titleIds.substring(titleIds.indexOf(',')+1));
			logManager.log(account+":"+userName,operContent,openModle,ipAddress,description);  
		    //---------------END
			ret = 1;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());
		}
		return ret;
	}
	/**
	 * 投票
	 * 
	 * @param titleID
	 * @return
	 */
	public int doAnswer(int qID,String answer,String ip) throws VoteManagerException {
		try {
			DBUtil dbUtil = new DBUtil();
			
			String sql = "update td_cms_vote_questions set votecount=votecount+1 where  ID="+qID;
			dbUtil.executeUpdate(sql);
			int answerid;
			PreparedDBUtil conn = new PreparedDBUtil();
			answerid=Integer.parseInt(conn.getNextStringPrimaryKey("td_cms_vote_answer"));
			StringBuffer sqlBuffer = new StringBuffer();
			sqlBuffer.append("insert into td_cms_vote_answer(ANSWER,QID,TYPE,WHO_IP,WHEN,ANSER_ID)values");
			sqlBuffer.append("(?,?,1,?,sysdate,?)");
			//sql = "insert into td_cms_vote_answer(ANSWER,QID,TYPE,WHO_IP,WHEN)values" +
			//		"('"+answer+"',"+qID+",1,'"+ip+"',sysdate)";
			conn.preparedInsert(sqlBuffer.toString());
			conn.setClob(1,answer,"answer");
			conn.setInt(2,qID);
			conn.setString(3,ip);
			conn.setInt(4,answerid);
			conn.executePrepared();
			//dbUtil.executeInsert(sql);
 
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}
	
	/**
	 * 该IP是否对这个题目投过票
	 * 
	 * @param titleID
	 * @return
	 */
	public int hasVoted(int qID,String ip) throws VoteManagerException {
		try {
			DBUtil dbUtil = new DBUtil();
			
			String sql = "select * from TD_CMS_VOTE_ANSWER where QID="+qID+" and WHO_IP='"+ip+"'";
			dbUtil.executeSelect(sql);
			if (dbUtil.size()>0)
				return 1;

			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}
	
	/**
	 * 
	 * @param qID
	 * @param ip
	 * @return 1:可以投票； 2：不在投票时间段内；3：不在投票IP段内；4：重复投票
	 * @throws VoteManagerException
	 */
	public int canVote(int qID,String ip) throws VoteManagerException{
		try {
			DBUtil dbUtil = new DBUtil();
			
			//time constrain
			String sql = "select *  from td_cms_vote_timectrl t where title_id in (select title_id from td_cms_vote_tq where QUESIONT_ID ="+qID+")";
			dbUtil.executeSelect(sql);
			if (dbUtil.size()>0){
				sql = "select *  from td_cms_vote_timectrl t " +
				" where time_start<=sysdate and time_end>sysdate-1 " +
					" and title_id in (select title_id from td_cms_vote_tq where QUESIONT_ID ="+qID+")";
				dbUtil.executeSelect(sql);
				if (!(dbUtil.size()>0))
					return 2;
			}
			
			//ip constrain
			sql = "select *  from TD_CMS_VOTE_IPCTRL t where title_id in (select title_id from td_cms_vote_tq where QUESIONT_ID ="+qID+")";
			dbUtil.executeSelect(sql);
			if (dbUtil.size()>0){
				sql = "select *  from td_cms_vote_ipctrl t " +
				" where replace('"+ip+"','.','') >= replace(IP_START,'.','')  and replace('"+ip+"','.','')<= replace(IP_END,'.','')  " +
					" and title_id in (select title_id from td_cms_vote_tq where QUESIONT_ID ="+qID+")";
				dbUtil.executeSelect(sql);
				if (!(dbUtil.size()>0))
					return 3;
			}
			
			//ip repeat constrain
			sql = "select *  from TD_CMS_VOTE_TITLE t where IP_REPEAT=0 and id in (select title_id from td_cms_vote_tq where QUESIONT_ID ="+qID+")";
			dbUtil.executeSelect(sql);
			if (dbUtil.size()>0){
				int timeGap = dbUtil.getInt(0,"TIME_GAP");
				if (hasVoted(qID,ip)==1){
					if (timeGap<0)
						return 4;
					
					sql = "select max(WHEN) as WHEN ,sysdate as now from TD_CMS_VOTE_ANSWER where WHO_IP='"+ip+"' and QID="+qID;
					dbUtil.executeSelect(sql);
					Date when = dbUtil.getDate(0,"WHEN");
					Date now = dbUtil.getDate(0,"now");
					if ((now.getTime()-when.getTime())/(1000*60*60)<timeGap)
						return 4;
				}
			}
			
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}
	
	/**
	 * 是否对问题进行了修改
	 * 
	 * @param titleID
	 * @return
	 */
	public boolean isQuestionChanged(int qID,Question q) throws VoteManagerException {
		try {
			DBUtil dbUtil = new DBUtil();
			
			String sql = "select * from TD_CMS_VOTE_QUESTIONS where ID="+qID;
			dbUtil.executeSelect(sql);
			if (dbUtil.size()>0){
				if (!q.getTitle().equals(dbUtil.getString(0,"TITLE")))
					return true;
				if (q.getStyle()!=dbUtil.getInt(0,"style"))
					return true;
				
				sql = "select * from TD_CMS_VOTE_ITEMS where QID="+qID;
				dbUtil.executeSelect(sql);
				List items = q.getItems();
				if (items==null&&dbUtil.size()==0)
					return false;
				if (items.size()!=dbUtil.size())
					return true;
				
				for(int i=0;i<dbUtil.size();i++){
					int t=0;
					for(int j=0;j<items.size();j++){
						Item oneItem = (Item)items.get(j);
						if (dbUtil.getString(i,"OPTIONS").equals(oneItem.getOptions())){
							t = 1;
							break;
						}
					}
					if (t==0)
						return true;
				}
			}

			return false;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}
	
	/**
	 * 删除问题
	 * 
	 * @param titleID
	 * @return
	 */
	public int delQuestions(String qids) throws VoteManagerException {
		try {
			DBUtil dbUtil = new DBUtil();
			
			String sql = "delete from td_cms_vote_tq where QUESIONT_ID in ("+qids+")";
			dbUtil.executeDelete(sql);
			sql = "delete from td_cms_vote_answer where qID in ("+qids+")";
			dbUtil.executeDelete(sql);
			sql = "delete from TD_CMS_VOTE_ITEMS where QID in ("+qids+")";
			dbUtil.executeDelete(sql);
			sql = "delete from td_cms_vote_questions where ID in ("+qids+")";
			dbUtil.executeDelete(sql);

			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}
	/**
	 * 删除自由问答回答记录
	 * 
	 * @param answerIDs
	 * @return
	 */
	public int delAnswers(String answerIDs,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException {
		try {
//			---------------START--网上调查删除自由问答回答记录操作日志
			String operContent="";       
		    String openModle="网上调查";
		    String description="";
		    LogManager logManager = SecurityDatabase.getLogManager(); 		
			operContent="删除自由问答回答记录: "+"回答记录内容为 "+ getAnwersById(answerIDs);
		    //---------------END
			DBUtil dbUtil = new DBUtil();
			
			String sql = "delete from td_cms_vote_answer where ANSER_ID in ("+answerIDs+")";
			dbUtil.executeDelete(sql);
            //---------------START--网上调查删除自由问答回答记录操作日志
			logManager.log(account+":"+userName,operContent,openModle,ipAddress,description);  
		    //---------------END
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}
	
	
	/**
	 * 激活问题
	 * 
	 * @param titleID
	 * @return
	 */
	public int activateQuestions(String qids,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException {
		try {
			DBUtil dbUtil = new DBUtil();
			
			String sql = "update td_cms_vote_questions set active=1 where ID in ("+qids+")";
			dbUtil.executeUpdate(sql);
            //---------------START--网上调查审核问题操作日志
			String operContent="";       
		    String openModle="网上调查";
		    String description="";
		    LogManager logManager = SecurityDatabase.getLogManager(); 		
			operContent="审核问题信息: "+"问题名称为 "+ getQuestionsById(qids.substring(qids.indexOf(",")+1));
			logManager.log(account+":"+userName,operContent,openModle,ipAddress,description);  
		    //---------------END
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}
	/**
	 * 反激活问题
	 * 
	 * @param titleID
	 * @return
	 */
	public int unactivateQuestions(String qids,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException {
		try {
			DBUtil dbUtil = new DBUtil();
			
			String sql = "update td_cms_vote_questions set active=0 where ID in ("+qids+")";
			dbUtil.executeUpdate(sql);
            //---------------START--网上调查取消审核问题操作日志
			String operContent="";       
		    String openModle="网上调查";
		    String description="";
		    LogManager logManager = SecurityDatabase.getLogManager(); 		
			operContent="取消审核问题信息: "+"问题名称为 "+ getQuestionsById(qids.substring(qids.indexOf(",")+1));
			logManager.log(account+":"+userName,operContent,openModle,ipAddress,description);  
		    //---------------END
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}
	
	/**
	 * 根据问题ID取得问卷ID
	 * 
	 * @param titleID
	 * @return
	 */
	public int getTitleIDBy(String qid) throws VoteManagerException {
		try {
			DBUtil dbUtil = new DBUtil();
			
			String sql = "select title_id from td_cms_vote_tq where QUESIONT_ID="+qid;
			dbUtil.executeSelect(sql);
			if (dbUtil.size()>0)
				return dbUtil.getInt(0,"title_id");

			return -1;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}
	
	/**
	 * 取得站点活动问题
	 * 
	 * @param titleID
	 * @return
	 */
	public List getActiveQuestions(long siteID) throws VoteManagerException {
		try {
			List res = new ArrayList();

			DBUtil dbUtil = new DBUtil();
			String sql = "select * from TD_CMS_VOTE_QUESTIONS a,TD_CMS_VOTE_TQ b, TD_CMS_VOTE_TITLE c " +
					" where c.siteid="+siteID+" and a.id=b.QUESIONT_ID and b.title_id=c.id and a.active=1";
			dbUtil.executeSelect(sql);

			Question item = null;
			for (int i = 0; dbUtil.size() > 0 && i < dbUtil.size(); i++) {
				item = new Question();
				item.setId(dbUtil.getInt(i, "QUESIONT_ID"));
				item.setActive(dbUtil.getInt(i, "ACTIVE"));
				item.setAnswers(getAnswersOfQstion(dbUtil.getInt(i, "QUESIONT_ID")));
				item.setItems(getItemsOfQstion(dbUtil.getInt(i, "QUESIONT_ID")));
				item.setStyle(dbUtil.getInt(i, "STYLE"));
				item.setTitle(dbUtil.getString(i, "TITLE"));
				item.setTitleID(dbUtil.getInt(i, "TITLE_ID"));
				item.setVotecount(dbUtil.getInt(i, "VOTECOUNT"));

				res.add(item);
			}
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}
	
	/**
	 * 取得问卷下的所有问题编号
	 * 
	 * @param titleID
	 * @return id1,id2,id3
	 */
	public String getQuestionIDsBy(String titleid) throws VoteManagerException {
		try {
			String res = "";

			DBUtil dbUtil = new DBUtil();
			String sql = "select QUESIONT_ID from TD_CMS_VOTE_TQ  where title_ID="+titleid;
			dbUtil.executeSelect(sql);

			for (int i = 0; dbUtil.size() > 0 && i < dbUtil.size(); i++) {
				res += dbUtil.getInt(i, "QUESIONT_ID");
				if (i!=dbUtil.size()-1)
					res += ",";
			}
			
			return res;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}
	
	/**
	 * 取得某站点某频道下的所有活动问卷
	 * @param siteid
	 * @param displayName
	 * @param count
	 * @return
	 * @throws VoteManagerException
	 */
	public List getActiveSurvey(String siteid,String displayName,int count)
	 throws VoteManagerException {
		DBUtil db = new DBUtil();
		List res = new ArrayList();
		ChannelManager channel = new ChannelManagerImpl();
		//equest.getInputStream();
		try {
			String channelId = String.valueOf(channel.getChannelInfoByDisplayName(siteid,displayName).getChannelId());
			String sql = "select a.id,a.islook,a.name,a.siteid,a.ip_repeat,a.active,a.picpath,a.istop,a.content,a.ctime," +
					"b.user_name,d.name as chname,d.CHANNEL_ID from td_cms_vote_title a,td_sm_user b,TD_CMS_CHANNEL_VOTE c,TD_CMS_CHANNEL d " +
					" where c.CHANNEL_ID="+channelId+" and a.founder_id=b.user_id " + (count>0?"and rownum<="+count:"")+
					" and a.id=c.VOTE_TITLE_ID(+) and c.CHANNEL_ID=d.CHANNEL_ID(+) and a.SITEID="+siteid+" order by ctime desc";
			
			db.executeSelect(sql );	
			
			for (int i = 0; i < db.size(); i++) {

				Title  title = new Title();
				title.setId(db.getInt(i,"id"));
				title.setName(db.getString(i,"name"));
				title.setSiteid(db.getInt(i,"siteid"));
				title.setIpRepeat(db.getInt(i,"ip_repeat"));
				title.setActive(db.getInt(i,"active"));
				title.setPicpath(db.getString(i,"picpath"));
				title.setIsTop(db.getInt(i,"istop"));
				title.setContent(db.getString(i,"content"));
				title.setChannelID(String.valueOf(db.getInt(i,"CHANNEL_ID")));
				title.setChannelName(String.valueOf(db.getString(i,"chname")));
				title.setFoundDate(db.getDate(i,"ctime").toString());
				title.setFoundername(db.getString(i,"user_name"));
			    title.setIslook(db.getInt(i,"islook"));
				res.add(title);
			}

			return res;
		} catch (Exception e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());
		}
	}
	/**
	 * 取得某站点当前时间活动问卷
	 * @param siteid
	 * @param displayName
	 * @param count
	 * @return
	 * @throws VoteManagerException
	 */
	public List getCurActiveSurvey(String siteid,int count)
	 throws VoteManagerException {
		DBUtil db = new DBUtil();
		List res = new ArrayList();
		ChannelManager channel = new ChannelManagerImpl();
		//equest.getInputStream();
		try {
			
			String sql = "SELECT   a.ID, a.islook, a.NAME, a.siteid, a.ip_repeat, a.active, a.picpath, a.istop, a.content, a.ctime, b.user_name,c.time_end+1" +
					" from td_cms_vote_title a,td_sm_user b, td_cms_vote_timectrl c " +
					" where  a.founder_id=b.user_id " + (count>0?"and rownum<="+count:"")+
					" and  a.id=c.title_id and sysdate between time_start and time_end+1   and a.SITEID="+siteid+" order by ctime desc";
			
			db.executeSelect(sql );	
			
			for (int i = 0; i < db.size(); i++) {

				Title  title = new Title();
				title.setId(db.getInt(i,"id"));
				title.setName(db.getString(i,"name"));
				title.setSiteid(db.getInt(i,"siteid"));
				title.setIpRepeat(db.getInt(i,"ip_repeat"));
				title.setActive(db.getInt(i,"active"));
				title.setPicpath(db.getString(i,"picpath"));
				title.setIsTop(db.getInt(i,"istop"));
				title.setContent(db.getString(i,"content"));
				title.setChannelID(String.valueOf(db.getInt(i,"CHANNEL_ID")));
				title.setChannelName(String.valueOf(db.getString(i,"chname")));
				title.setFoundDate(db.getDate(i,"ctime").toString());
				title.setFoundername(db.getString(i,"user_name"));
			    title.setIslook(db.getInt(i,"islook"));
				res.add(title);
			}

			return res;
		} catch (Exception e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());
		}
	}
	
	
	/**
	 * 取得某站点某频道下的所有活动问卷
	 * 
	 * @param siteid
	 * @param displayName
	 * @param count
	 * @return
	 * @throws VoteManagerException
	 */
	public int setSurveyTop(String titleid,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException {
		String sqlSelect = "select a.active,to_char(b.time_end,'yyyy-mm-dd') end_time from td_cms_vote_title a,td_cms_vote_timectrl b " +
		"where a.id=b.title_id and active=1 and time_end>=sysdate-1 and a.id="+titleid;
//		String sqlSelect ="select * from td_cms_vote_timectrl where title_id="+titleid;
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sqlSelect);
			if(db.size()<=0)
			{
				return 2;
			}
			String sql = "update TD_CMS_VOTE_TITLE set istop=1 where id="+titleid;
			db.executeUpdate(sql);
			sql = "update TD_CMS_VOTE_TITLE set istop=0 where id<>"+titleid+" and siteid in (select siteid from TD_CMS_VOTE_TITLE where id="+titleid+")";
			db.executeUpdate(sql);
            //---------------START--网上调查置顶操作日志
			String operContent="";        
		    String openModle="网上调查";
		    String description="";
		    LogManager logManager = SecurityDatabase.getLogManager(); 		
			operContent="置顶调查信息: "+"信息主题为 "+ getTitlesById(titleid);
			logManager.log(account+":"+userName,operContent,openModle,ipAddress,description);  
		    //---------------END
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());
		}	
	}
	public int cancelSurveyTop(String titleid,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException
	{
		DBUtil db = new DBUtil();
		try {
			String sqlSelect = "select istop from td_cms_vote_title where istop=0 and id="+titleid;
			db.executeSelect(sqlSelect);
			if(db.size()>0)
			{
				return 2;
			}
			String sql = "update TD_CMS_VOTE_TITLE set istop=0 where id="+titleid;
			db.executeUpdate(sql);
            //---------------START--网上调查取消置顶操作日志
			String operContent="";        
		    String openModle="网上调查";
		    String description="";
		    LogManager logManager = SecurityDatabase.getLogManager(); 		
			operContent="取消置顶调查信息: "+"信息主题为 "+ getTitlesById(titleid);
			logManager.log(account+":"+userName,operContent,openModle,ipAddress,description);  
		    //---------------END
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());
		}
	}
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
			throws VoteManagerException {
		DBUtil db = new DBUtil();
		try {
			String sql = "update TD_CMS_VOTE_questions set istop=1 where id="+qid;
			db.executeUpdate(sql);
			sql = "update TD_CMS_VOTE_questions " +
					" set istop=0 where id in (" +
					"select a.id from TD_CMS_VOTE_questions a,td_cms_vote_tq b,td_cms_vote_title c " +
					" where a.id=b.quesiont_id and c.id=b.title_id and a.id<>"+qid+" and c.siteid in (" +
							"select g.siteid from TD_CMS_VOTE_questions e,td_cms_vote_tq f,td_cms_vote_title g " +
							" where e.id=f.quesiont_id and g.id=f.title_id and e.id="+qid+"))";
			db.executeUpdate(sql);
			
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());
		}
	}
	
	/**
	 * 取得置顶问卷ID
	 * 
	 * @param siteid
	 * @param displayName
	 * @param count
	 * @return
	 * @throws VoteManagerException
	 */
	public int getTopSurveyID(int siteID) throws VoteManagerException {
		DBUtil db = new DBUtil();
		try {
			String sql = "select id from td_cms_vote_title where istop=1 and siteid="+siteID;
			db.executeSelect(sql);

			if (db.size() > 0)
				return db.getInt(0, "id");

			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());
		}
	}
	

	/**
	 * 取得置顶问题ID
	 * 
	 * @param siteid
	 * @param displayName
	 * @param count
	 * @return
	 * @throws VoteManagerException
	 */
	public int getTopQuestionID(int siteID) throws VoteManagerException {
		DBUtil db = new DBUtil();
		try {
			String sql = "select a.id from TD_CMS_VOTE_questions a,td_cms_vote_tq b,td_cms_vote_title c  where a.istop=1 and a.id=b.quesiont_id and c.id=b.title_id and c.siteid="+siteID;
			db.executeSelect(sql);

			if (db.size() > 0)
				return db.getInt(0, "id");

			return -1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());
		}
	}

	/**
	 * 取得频道下的所有活动问题
	 * 
	 * @param siteid
	 * @param displayName
	 * @param count
	 * @return
	 * @throws VoteManagerException
	 */
	public List getActiveQstOf(String channelID,int count) throws VoteManagerException {
		try {
			DBUtil dbUtil = new DBUtil();
			
			String sql="select a.* from td_cms_vote_questions a,td_cms_vote_tq b,td_cms_vote_title c,td_cms_channel_vote d " +
					" where a.active=1 and a.id=b.quesiont_id and c.id=b.title_id and c.id=d.vote_title_id " +
					" and d.channel_id="+channelID+(count>0?" and rownum<"+count:"")+" order by c.ctime desc";
			//String sql="select * from td_cms_vote_questions  order by id";

			dbUtil.executeSelect(sql);
			List list = new ArrayList();
			Question question;
			for (int i = 0; i < dbUtil.size(); i++) {
				question = new Question();
				question.setId(dbUtil.getInt(i,"id"));
				question.setActive(dbUtil.getInt(i,"active"));
				question.setStyle(dbUtil.getInt(i,"style"));
				question.setItems(getItemsOfQstion(dbUtil.getInt(i,"id")));
				question.setTitle(dbUtil.getString(i,"title"));
				question.setVotecount(dbUtil.getInt(i,"votecount"));
				question.setSurveyName(dbUtil.getString(i,"title"));
				question.setIsTop(dbUtil.getInt(i,"istop"));
				list.add(question);
			}
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 取得频道下的最新活动问卷id
	 * 
	 * @param siteid
	 * @param displayName
	 * @param count
	 * @return
	 * @throws VoteManagerException
	 */
	public int getLatestActiveSurveyIDInChnl(String channelID) throws VoteManagerException {
		try {
			DBUtil dbUtil = new DBUtil();
			
			String sql="select id from td_cms_vote_title c,td_cms_channel_vote d where c.id=d.vote_title_id and d.channel_id="+channelID+" and rownum=1 order by ctime desc";
			//String sql="select * from td_cms_vote_questions  order by id";

			dbUtil.executeSelect(sql);

			if (dbUtil.size()>0) {
				return dbUtil.getInt(0,"id");
			}
			
			return -1;
		}catch(Exception e){
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * 激活问题
	 * 
	 * @param titleID
	 * @return
	 */
	public int activateSurveys(String sids,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException {
		try {
			DBUtil dbUtil = new DBUtil();
			String selectSql = "select active from td_cms_vote_title where active=1 and ID in("+sids+")";
			dbUtil.executeSelect(selectSql);
			if(dbUtil.size()>0)
			{
				return 2;
			}
			String sql = "update td_cms_vote_title set active=1 where ID in ("+sids+")";
			dbUtil.executeUpdate(sql);
            //---------------START--网上调查审核操作日志
			String operContent="";        
		    String openModle="网上调查";
		    String description="";
		    LogManager logManager = SecurityDatabase.getLogManager(); 		
			operContent="审核调查信息: "+"信息主题为 "+ getTitlesById(sids.substring(sids.indexOf(',')+1));
			logManager.log(account+":"+userName,operContent,openModle,ipAddress,description);  
		    //---------------END
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}
	/**
	 * 反激活问题
	 * 
	 * @param titleID
	 * @return
	 */
	public int unactivateSurveys(String sids,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException {
		try {
			DBUtil dbUtil = new DBUtil();
			String selectSql = "select active from td_cms_vote_title where active=0 and ID in("+sids+")";
			dbUtil.executeSelect(selectSql);
			if(dbUtil.size()>0)
			{
				return 2;
			}
			String sql = "update td_cms_vote_title set active=0 where ID in ("+sids+")";
			dbUtil.executeUpdate(sql);
            //---------------START--网上调查取消审核操作日志
			String operContent="";        
		    String openModle="网上调查";
		    String description="";
		    LogManager logManager = SecurityDatabase.getLogManager(); 		
			operContent="取消审核调查信息: "+"信息主题为 "+ getTitlesById(sids.substring(sids.indexOf(',')+1));
			logManager.log(account+":"+userName,operContent,openModle,ipAddress,description);  
		    //---------------END
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}

	public int passAnswers(String answerIDs,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException {
		try {
			DBUtil dbUtil = new DBUtil();
			
			String sql = "update td_cms_vote_answer set state=1 where anser_id in("+answerIDs+")";
			dbUtil.executeUpdate(sql);
            //---------------START--网上调查审核自由问答回答记录操作日志
			String operContent="";       
		    String openModle="网上调查";
		    String description="";
		    LogManager logManager = SecurityDatabase.getLogManager(); 		
			operContent="审核自由问答回答记录: "+"回答记录内容为 "+ getAnwersById(answerIDs);
			logManager.log(account+":"+userName,operContent,openModle,ipAddress,description);  
		    //---------------END
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());

		}
	}
	public int getQidByItemId(String itemId) throws VoteManagerException
	{
		int ret = 0;
		String sql = "select qid from TD_CMS_VOTE_ITEMS where id="+itemId;
		DBUtil db =new DBUtil();
		try {
			db.executeSelect(sql);
			if(db.size()>0)
			{
				ret = db.getInt(0,"qid");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());
		}
		return ret;
	}
	public int cancelSurveysLook(String sids,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException
	{
		DBUtil db = new DBUtil();
		String sql = "update td_cms_vote_title set islook=1 where id in("+sids+")";
		String selectSql = "select islook from td_cms_vote_title where islook=1 and ID in("+sids+")";
		try
		{
			db.executeSelect(selectSql);
			if(db.size()>0)
			{
				return 2;
			}
			db.executeUpdate(sql);
//			---------------START--网上调查取消查看操作日志
			String operContent="";        
		    String openModle="网上调查";
		    String description="";
		    LogManager logManager = SecurityDatabase.getLogManager(); 		
			operContent="取消查看: "+"信息主题为 "+ getTitlesById(sids.substring(sids.indexOf(',')+1));
			logManager.log(account+":"+userName,operContent,openModle,ipAddress,description);  
		    //---------------END
			return 1;
		}catch(SQLException e)
		{
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());
		}
	}
	public int setSurveysLook(String sids,String account,String userName,String ipAddress) throws SPIException, ManagerException, VoteManagerException
	{
		DBUtil db = new DBUtil();
		String sql = "update td_cms_vote_title set islook=0 where id in("+sids+")";
		String selectSql = "select islook from td_cms_vote_title where islook=0 and ID in("+sids+")";
		try
		{
			db.executeSelect(selectSql);
			if(db.size()>0)
			{
				return 2;
			}
			db.executeUpdate(sql);
            //			---------------START--网上调查恢复查看操作日志
			String operContent="";        
		    String openModle="网上调查";
		    String description="";
		    LogManager logManager = SecurityDatabase.getLogManager(); 		
			operContent="恢复查看: "+"信息主题为 "+ getTitlesById(sids.substring(sids.indexOf(',')+1));
			logManager.log(account+":"+userName,operContent,openModle,ipAddress,description);  
		    //---------------END
			return 1;
		}catch(SQLException e)
		{
			e.printStackTrace();
			throw new VoteManagerException(e.getMessage());
		}
	}
	public Answer getAnswerByAnswerId(String answerId)throws VoteManagerException
	{
		Answer answer = new Answer();
		String sql = "select a.answer answer,b.title title from td_cms_vote_answer a,td_cms_vote_questions b " +
				"where a.qid=b.id and a.anser_id="+answerId;
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			if(db.size()>0)
			{
				answer.setAnswer(db.getString(0,"answer"));
				answer.setQtitle(db.getString(0,"title"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return answer;
	}
	public String getTitlesById(String id)
 	{
 		String ret = "";
 		 String sql = "select name from td_cms_vote_title  where id in ("+id+")";
 		 DBUtil db = new DBUtil();
 		 try {
			db.executeSelect(sql);
			if(db.size()>0)
			{
				for(int i=0;i<db.size();i++)
				{
					if(ret.equals(""))
					{
						ret = db.getString(0,"name");
					}
					else
					{
						ret += " || " + db.getString(0,"name");
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		return ret;
 	}
	public String getAnwersById(String id)
 	{
 		String ret = "";
 		 String sql = "select answer from td_cms_vote_answer  where ANSER_ID in ("+id+")";
 		 System.out.println(sql);
 		 DBUtil db = new DBUtil();
 		 try {
			db.executeSelect(sql);
			if(db.size()>0)
			{
				for(int i=0;i<db.size();i++)
				{
					if(ret.equals(""))
					{
						ret = db.getString(0,"answer");
					}
					else
					{
						ret += " || " + db.getString(0,"answer");
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		return ret;
 	}
	public String getQuestionsById(String id)
 	{
 		String ret = "";
 		 String sql = "select TITLE from td_cms_vote_questions  where id in ("+id+")";
 		 DBUtil db = new DBUtil();
 		 try {
			db.executeSelect(sql);
			if(db.size()>0)
			{
				for(int i=0;i<db.size();i++)
				{
					if(ret.equals(""))
					{
						ret = db.getString(0,"TITLE");
					}
					else
					{
						ret += " || " + db.getString(0,"TITLE");
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		return ret;
 	}
	public String getVoteIdByDepartId(String depart_id)
	{
		String ret = "0";
		String sql = "select id from TD_CMS_VOTE_TITLE a,td_cms_channel_vote b,td_cms_channel c where a.id=b.vote_title_id and b.channel_id=c.channel_id and c.name='网上测评' and DEPART_ID='"+depart_id+"' ";
		System.out.println(sql);
		DBUtil db = new DBUtil();
		try {
			db.executeSelect(sql);
			if(db.size()>0)
			{
				ret = db.getInt(0,"id")+"";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

}
