package com.frameworkset.platform.cms.votemanager;

import java.util.ArrayList;
import java.util.List;
 
public class VoteMngrProtocol {
	
	public static List getQuestionFromString(String qstionStr){
		List res = new ArrayList();
		
		int s = qstionStr.indexOf("<");
		int e = qstionStr.indexOf(">");
		while(s>-1){
			String q = qstionStr.substring(s+1,e);
			String[] strArray = q.split("'"); 
			qstionStr = qstionStr.substring(e+1);
			Question qObj = new Question();
			qObj.setTitle(strArray[0]);
			
			if(strArray[1].equals("")||(strArray[1]==null))
			{
				strArray[1] = "-1";
			}
			qObj.setId(Integer.parseInt(strArray[1]));
			qObj.setVotecount(Integer.parseInt(strArray[1]));
			if(strArray[2].equals("")||(strArray[2]==null))
			{
				strArray[2] = "0";
			}
			qObj.setVotecount(Integer.parseInt(strArray[2]));
			qObj.setStyle(Integer.parseInt(strArray[3]));
			if (strArray.length==7)
				qObj.setItems(getItemsFromString(strArray[4],strArray[5],strArray[6]));
			
			res.add(qObj);
			
			s = qstionStr.indexOf("<");
			e = qstionStr.indexOf(">");
		}
		
		return res;
	}
	
	public static  List getItemsFromString(String itemStr,String itemIdStr,String countStr){
		List res = new ArrayList();
		String[] strArray = itemStr.split(";");
		String[] strArray1 = countStr.split(";");
		String[] strArray2 = itemIdStr.split(";");
		for (int i=0;i<strArray.length;i++){
			Item item =  new Item();
			item.setOptions(strArray[i]);
			if(strArray1[i].equals("")||(strArray1[i]==null))
			{
				strArray1[i] = "0";
			}
			item.setCount(Integer.parseInt(strArray1[i]));
			if(strArray2[i].equals("")||(strArray2[i]==null))
			{
				strArray2[i] = "-1";
			}
			item.setId(Integer.parseInt(strArray2[i]));
			res.add(item);
		}
		return res;
	}
	
	public static  List getIpCtrlFromString(String ipCtrlStartStr,String ipCtrlEndStr){
		List res = new ArrayList();
		if ("".equals(ipCtrlStartStr))
			return res;
		
		String[] strArrayStart = ipCtrlStartStr.split(";");
		String[] strArrayEnd = ipCtrlEndStr.split(";");
		for (int i=0;i<strArrayStart.length;i++){
			IpCtrl ipCtrl =  new IpCtrl();
			ipCtrl.setIpStart(strArrayStart[i]);
			ipCtrl.setIpEnd(strArrayEnd[i]);
			res.add(ipCtrl);
		}
		return res;
	}
	
	public  static List getTimeCtrlFromString(String timeCtrlStartStr,String timeCtrlEndStr){
		List res = new ArrayList();
		if ("".equals(timeCtrlStartStr))
			return res;
		
		String[] strArrayStart = timeCtrlStartStr.split(";");
		String[] strArrayEnd = timeCtrlEndStr.split(";");
		for (int i=0;i<strArrayStart.length;i++){
			TimeCtrl timeCtrl =  new TimeCtrl();
			timeCtrl.setTimeStart(strArrayStart[i]);
			timeCtrl.setTimeEnd(strArrayEnd[i]);
			res.add(timeCtrl);
		}
		return res;
	}

}
/*List questions = oneTitle.getQuestions();
				Question  qstion = (Question)questions.get(0);
				%>document.all("qstionTbl1Qstion").value = "<%=qstion.getTitle()%>";
				checkRadiobox("qstionTbl1Style","<%=qstion.getStyle()%>");<%
				if (qstion.getStyle()!=2){
					List items = qstion.getItems();
					Item item = (Item)items.get(0);
					%>addOption("qstionTbl1");
					document.all("qstionTbl1Option").value = "<%=item.getOptions()%>";<%
					for (int j=1;j<items.size();j++){
						item = (Item)items.get(j);
						%>addOption("qstionTbl1");
						document.all("qstionTbl1Option")[<%=j%>].value = "<%=item.getOptions()%>";<%
					}
				}
				for (int i=1;i<questions.size();i++){
					qstion = (Question)questions.get(i);
					%>addQuestion();
					var tblNm = "qstionTbl"+tableIndex;
					document.all(tblNm+"AddBtn").disabled = true;
					document.all(tblNm+"DelBtn").disabled = true;
					document.all(tblNm+"Qstion").value = "<%=qstion.getTitle()%>";
					checkRadiobox(tblNm+"Style","<%=qstion.getStyle()%>");<%
					if (qstion.getStyle()!=2){
						List items = qstion.getItems();
						Item item = (Item)items.get(0);
						%>addOption(tblNm);
						document.all(tblNm+"Option").value = "<%=item.getOptions()%>";<%
						for (int j=1;j<items.size();j++){
							item = (Item)items.get(j);
							%>addOption(tblNm);
							document.all(tblNm+"Option")[<%=j%>].value = "<%=item.getOptions()%>";<%
						}
					}
				}*/