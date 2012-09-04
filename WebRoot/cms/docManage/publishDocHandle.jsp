<%@page import="com.frameworkset.orm.transaction.TransactionManager"%>
<%@ page contentType="text/html; charset=UTF-8" language="java" import="java.util.*"%>
<%@ page import="com.frameworkset.platform.security.AccessControl"%>
<%@ page import="com.frameworkset.platform.cms.documentmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.channelmanager.*"%>
<%@ page import="com.frameworkset.platform.cms.sitemanager.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.publish.impl.*"%>
<%@ page import="com.frameworkset.platform.cms.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.context.impl.*"%>
<%@ page import="com.frameworkset.platform.cms.driver.config.*"%>
<script src="../inc/js/func.js"></script>
<%
	AccessControl accessControl = AccessControl.getInstance();
	accessControl.checkAccess(request,response);
	String userId = accessControl.getUserID();
	
	
	//falg为2表示审核通过后立即发布,文档状态从已审进入发布中；
	//为1表示对个人工作平台的待发文档进行批量发布，这些文档可能来自于不同一个频道，不同一个站点
	//为4表示频道的文档列表的批量发布，这些文档来自于同一个频道，同一个站点
	//为3表示直接发布单个文档
	//为13表示异常情况，用于处理“假”发布中的文档，对发布中文档单个重发
	String flag = request.getParameter("flag");
    //外部注入monitor
    String uuid = request.getParameter("uuid");
    //是否记录日志
    String isRecordValue = request.getParameter("isRecordValue");
    
    //增加是否递归发布和是否清除缓存
    boolean isRecursionPublish = Boolean.parseBoolean(request.getParameter("recursionPublish"));
    boolean isClearCache = Boolean.parseBoolean(request.getParameter("clearCache"));
    
    
    PublishMonitor monitor = (PublishMonitor)session.getAttribute(uuid);
	//文档发布处理
	DocumentManager docManager = new DocumentManagerImpl();
	SiteManager  siteManager = new SiteManagerImpl();
	ChannelManager cm = new ChannelManagerImpl();
	boolean successFlag = false;
	int batchFlag = 0;  //批量标志
	String pageUrl = "";
	try{
	 if(flag.equals("1")||flag.equals("3")||flag.equals("4")||flag.equals("13")){
	 	//初始化发布引擎
	 	WEBPublish publish = new WEBPublish();
	 	
	 	//设置是否递归发布和是否清除缓存
	 	((APPPublish)publish).setEnableRecursive(isRecursionPublish);
	 	((APPPublish)publish).setClearFileCache(isClearCache);
	 	
        //外部注入monitor
        publish.setMonitor(monitor);
        //外部控制是否添加日志
        if("false".equalsIgnoreCase(isRecordValue)){
            monitor.setNotRecordMsg(true);
            //System.out.println("monitor----------------"+monitor.isNotRecordMsg());
        }else{
             monitor.setNotRecordMsg(false);
        }
		publish.init(request,response,pageContext,accessControl);
		PublishCallBack callback = new PublishCallBackImpl();
		publish.setPublishCallBack(callback);
		String publishMessage = "";	
		if(flag.equals("1")){				//批量发布
			batchFlag = 1;
			
			//flag为1传来的参数，一个数组
			String[] docids = request.getParameterValues("ID");
			//将文档id的字符串数组转换为整形数组	
			int[] intDocids = null;
			String[][] strIds = null;
			List siteList = new ArrayList(); //站点列表，以便分类
			if(docids!=null){
				intDocids = new int[docids.length];
				strIds = new String[docids.length][3];
				for(int i=0;i<docids.length;i++){
					String[] id = docids[i].split(":");
					strIds[i][0] = id[2]; //站点id
					strIds[i][1] = id[3]; //频道id
					strIds[i][2] = id[1]; //文档id
					if(!siteList.contains(new Integer(id[2]))){
						siteList.add(new Integer(id[2]));      //所有站点
					}
					intDocids[i] = Integer.parseInt(id[1]);
				}	
			}
			
			Map tranIdMap = docManager.canTransition(intDocids,5);    //待发布文档肯定是能发布的
			//针对不同的站点发布
			for(int i=0;i<siteList.size();i++){
				int curSite = ((Integer)siteList.get(i)).intValue();
				List tempList = new ArrayList();
				for(int j=0;j<docids.length;j++){
					if(Integer.parseInt(strIds[j][0])==curSite)
						tempList.add(strIds[j]);
				}
				String[][] strIdsSite = new String[tempList.size()][3];
				for(int j=0;j<tempList.size();j++)
					strIdsSite[j] = (String[])tempList.get(j);	
					
				String siteName = siteManager.getSiteInfo(curSite+"").getName();
				boolean[] publishWay = siteManager.getSitePublishDestination(curSite+"");
				int[] distributeManners = siteManager.getSiteDistributeManners(curSite+"");
				
				//System.out.println("批量发布1"); 
				publish.publishBatchDocument(strIdsSite,publishWay,distributeManners); 		//批量发布
				//System.out.println("批量发布2");
				//尹标平屏蔽开始
				//List successMessage = callback.getPublishMonitor().getAllSuccessMessages(); //发布成功的信息
				//List failMessage = callback.getPublishMonitor().getAllFailedMessages();//发布失败的信息
				//if(successMessage.size()>0) publishMessage = publishMessage + siteName + "站点的发布成功消息：";
				//for(int j=0;j<successMessage.size();j++){
				//	publishMessage = publishMessage + successMessage.get(j).toString().replace('\\','/');
				//}
				//if(failMessage.size()>0) publishMessage = publishMessage + siteName + "站点的发布失败消息：";
				//for(int j=0;j<failMessage.size();j++){
				//	publishMessage = publishMessage + failMessage.get(j).toString().replace('\\','/');
				//}
								//尹标平屏蔽结束
			}		 
			docManager.recordDocOperate(intDocids,Integer.parseInt(userId),"发布",tranIdMap,"发布");
			//其他信息
			successFlag = callback.getPublishMonitor().isPublishCompleted();
			
			//session.removeAttribute("publishMessage");
			//session.setAttribute("publishMessage",publishMessage);
		}
		else if(flag.equals("3") || flag.equals("13")){			//单个发布
			//flag为3传来的参数
			String docid = request.getParameter("docId");
			int docid_int = Integer.parseInt(docid);
			
			String siteId = null;
			boolean[] publishWay = null;
			int[] distributeManners = null;
			String chnlId = null;
			int docType = -1;
			boolean prepared = false;
			Document doc = null;
			int tranID = -1;
			TransactionManager tm = new TransactionManager(); 
			try
			{
				tm.begin(tm.RW_TRANSACTION);
				doc = docManager.getPartDocInfoById(docid);
				 chnlId = String.valueOf(doc.getChanel_id());
				tranID = docManager.canTransition(docid_int,5);
				docType = doc.getDoctype();////待发布文档肯定是能发布的
				if((tranID >0 ) || (flag.equals("13") && !APPPublish.isRealPublishing(docid))){
					siteId = String.valueOf(docManager.getDocSiteId(docid_int));
					publishWay = siteManager.getSitePublishDestination(siteId);
					distributeManners = siteManager.getSiteDistributeManners(siteId);
					prepared = true;
				}
				tm.commit();
			}
			catch(Exception e)
			{
				
			}
			finally
			{
				tm.release();
			}
			if(prepared)
			{
				publish.publishDocument(siteId,chnlId,docid,publishWay, distributeManners);
				pageUrl = callback.getPageUrl();
				docManager.recordDocOperate(docid_int,Integer.parseInt(userId),"发布",tranID,"发布");
				successFlag  = callback.getPublishMonitor().isAllFailed()==true?false:true;
			}
		}else if(flag.equals("4")){	     //频道文档列表批量发布
			batchFlag = 1;
			
			String[] docids = request.getParameterValues("ID");
			String siteId = request.getParameter("siteId");
			String chnlId = request.getParameter("chnlId");

			int[] intdocids = new int[docids.length];
			for(int i=0;i<docids.length;i++){
				intdocids[i] = Integer.parseInt(docids[i]);
			}
			
			Map tranIdMap = docManager.canTransition(intdocids,5);
			if(tranIdMap.size()>0){
				boolean[] publishWay = siteManager.getSitePublishDestination(siteId);
				int[] distributeManners = siteManager.getSiteDistributeManners(siteId);
				publish.publishBatchDocument(siteId,chnlId,docids,publishWay,distributeManners);
				pageUrl = callback.getPageUrl();
				docManager.recordDocOperate(intdocids,Integer.parseInt(userId),"发布",tranIdMap,"发布");
				
				//List successMessage = callback.getPublishMonitor().getAllSuccessMessages(); //发布成功的信息
				//List failMessage = callback.getPublishMonitor().getAllFailedMessages();//发布失败的信息
				//if(successMessage.size()>0) publishMessage = "发布成功消息：";
				//for(int j=0;j<successMessage.size();j++){
				//	publishMessage = publishMessage + successMessage.get(j).toString().replace('\\','/');
				//}
				//if(failMessage.size()>0) publishMessage = publishMessage + "发布失败消息：";
				//for(int j=0;j<failMessage.size();j++){
				//	publishMessage = publishMessage + failMessage.get(j).toString().replace('\\','/');
				//}
				successFlag = callback.getPublishMonitor().isPublishCompleted();
				//session.removeAttribute("publishMessage");
				//session.setAttribute("publishMessage",publishMessage);
			} 
		}
			
		if(successFlag == true || pageUrl!=null && pageUrl.length()>0){
			//pageUrl = request.getContextPath() + "/" + pageUrl;	
			//flag=4 批量发布 pageUrl返回的首页URL, session保存为""	
			if(flag.equals("4") || flag.equals("1")){
                session.setAttribute("pageUrl"+uuid,"mutipublish");
            }else{
                session.setAttribute("pageUrl"+uuid,pageUrl);
            }
		%>
			<script language = "javascript">
				//parent.document.all.divProcessing.style.display="none";
				var con = true;
				if("<%=(batchFlag==0 && !pageUrl.equals("PublishMode.MODE_ONLY_ATTACHMENT") && !pageUrl.equals("PublishMode.MODE_NO_ACTION"))%>" == "true"){ 
                   if("<%=isRecordValue%>" == "false"){
                       //con = window.confirm("要查看发布文档吗？");
                       //if(con){
                       //     window.open("<%=pageUrl%>");
                       // } 
                       window.parent.alertFun("要查看发布文档吗？","<%=pageUrl%>");     
                        try{                            
                            window.parent.closeSubWindow();                            
                        }catch(err){
                            //alert(err.description)
                        }
                   }else{
                       window.parent.lastRefreshSubPage();
                       //在publish_info.jsp提示
                       //alert("恭喜，发布成功！"); 
                   }                  
				}else if("<%=batchFlag%>" == "1"){ 
                    if("<%=isRecordValue%>" == "false"){
                        //alert("恭喜，发布成功！");                            
                        window.parent.alertFun("恭喜，发布成功！","");
                        try{                            
                            window.parent.closeSubWindow();                            
                        }catch(err){
                            //alert(err.description)
                        }
                   }else{                       
                       window.parent.lastRefreshSubPage();
                       //alert("恭喜，发布成功！"); 
                       //在publish_info.jsp提示
                   }
					//con = window.confirm("发布完成！要查看发布日志吗？");
				}else{
                   if("<%=isRecordValue%>" == "false"){
                        //alert("恭喜，发布成功！");   
                        window.parent.alertFun("恭喜，发布成功！","");                         
                        try{                            
                            window.parent.closeSubWindow();                            
                        }catch(err){
                            //alert(err.description)
                        }
                   }else{                       
                       window.parent.lastRefreshSubPage();
                       //alert("恭喜，发布成功！"); 
                       //在publish_info.jsp提示
                   }   
                }
				var str = parent.document.location.href;
				var end = str.indexOf("?");
				var strArray;
				if(end != -1)
				 	strArray= str.slice(0,end);
				else
					strArray = str;	
                    
                
				//parent.document.location.href = strArray+"?" + parent.document.all.queryString.value;	
				
				
			</script>
		<%
		}
		
		else{
			%>
			<script language = "javascript">
				//parent.document.all.divProcessing.style.display="none";	
				<%if(!flag.equals("13")){%>
					//alert("发布失败，可能原因：1、不能发布；2、当前频道未设置细览模板；3、模板文件不存在！");
					window.parent.alertFun("发布失败，可能原因：1、不能发布；2、当前频道未设置细览模板；3、模板文件不存在！","");
				<%} else{%>
					//alert("对不起，文档正处于发布中，不能重发！");
					window.parent.alertFun("对不起，文档正处于发布中，不能重发！","");
				<%}%>
                               
                try{                            
                    window.parent.closeSubWindow();                            
                }catch(err){
                    //alert(err.description)
                }
			</script>
			<%
			}
	 }
	 else
	 {
	 
	 	//flag为2传来的参数
		String docid = request.getParameter("docid");
		String taskid = request.getParameter("taskid");
		String auditComment = "不同意：" + request.getParameter("auditComment");
		
	 	//审核通过操作
	 	int tranId1 = docManager.canTransition(Integer.parseInt(docid),3);
	 	if(tranId1 >= 0){
	 	  String chnlId = String.valueOf(docManager.getDocChnlId(Integer.parseInt(docid)));
		  String siteId = String.valueOf(docManager.getDocSiteId(Integer.parseInt(docid)));
		  int docType = docManager.getDocType(Integer.parseInt(docid));
		  //先检查是否能进行发布操作（前提是设有细览模板且模板文件存在，且为普通文档或者链接文档）
	 	  if(cm.hasSetDetailTemplate(chnlId)){
	 		docManager.audit(Integer.parseInt(docid),Integer.parseInt(taskid),Integer.parseInt(userId),auditComment,1);
			docManager.recordDocOperate(Integer.parseInt(docid),Integer.parseInt(userId),"审核",tranId1,"审核通过");
	
		 	//立即发布，文档从直接从已审进入已发状态
		 	int tranId2 = docManager.canTransition(Integer.parseInt(docid),5);
		 	if(tranId2 >= 0){	
				
				WEBPublish publish = new WEBPublish();
                //外部注入monitor
                publish.setMonitor(monitor);
                //外部控制是否添加日志
                if("false".equalsIgnoreCase(isRecordValue)){
                    monitor.setNotRecordMsg(true);
                    //System.out.println("monitor 2----------------"+monitor.isNotRecordMsg());
                }else{
                    monitor.setNotRecordMsg(false);
                }
				publish.init(request,response,pageContext,accessControl);
				PublishCallBack callback = new PublishCallBackImpl();
				publish.setPublishCallBack(callback);
				boolean[] publishWay = siteManager.getSitePublishDestination(siteId);
				int[] distributeManners = siteManager.getSiteDistributeManners(siteId);
				publish.publishDocument(siteId,chnlId,docid,publishWay, distributeManners);
				pageUrl = callback.getPageUrl();
				
				docManager.recordDocOperate(Integer.parseInt(docid),Integer.parseInt(userId),"发布",tranId2,"发布");
				if(pageUrl!=null){
					//pageUrl = request.getContextPath() + "/" + pageUrl;
                    session.setAttribute("pageUrl"+uuid,pageUrl);
		 		%>
		 		<script language = "javascript">
		 			//parent.document.all.divProcessing.style.display="none";
		 			if("<%=!pageUrl.equals("PublishMode.MODE_ONLY_ATTACHMENT") && !pageUrl.equals("PublishMode.MODE_NO_ACTION")%>" == "true"){
			            if("<%=isRecordValue%>" == "false"){
                            //con = window.confirm("要查看发布文档吗？");
                            //if(con){
                            //    window.open("<%=pageUrl%>");                                
                            //}
                            window.parent.alertFun("要查看发布文档吗？","<%=pageUrl%>");
                            try{                                
                                window.parent.closeSubWindow();                                
                            }catch(err){
                                //alert(err.description)
                            }
                        }else{ 
                            window.parent.lastRefreshSubPage();
                            //alert("恭喜，发布成功！"); 
                            //在publish_info.jsp提示
                        }
					}else{
                        if("<%=isRecordValue%>" == "false"){
                            //alert("恭喜，发布成功！");       
                            window.parent.alertFun("恭喜，发布成功！","");                     
                            try{                            
                                window.parent.closeSubWindow();                            
                            }catch(err){
                                //alert(err.description)
                            }
                       }else{                       
                           window.parent.lastRefreshSubPage();
                           //alert("恭喜，发布成功！"); 
                       }
                    } 
						//alert("恭喜，发布成功！");
					//window.close();
					var str = window.dialogArguments.location.href;
					var end = str.indexOf("?");
					var strArray;
					if(end != -1)
					 	strArray= str.slice(0,end);
					else
						strArray = str;
                   
					//window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
				</script>
		 		<%
		 		}else{
		 		%>
		 		<script language = "javascript">
					//alert("审核成功，但发布失败，可能当前频道未设置细览模板或者模板文件不存在！！");
					//parent.document.all.divProcessing.style.display="none";
					window.parent.alertFun("审核成功，但发布失败，可能当前频道未设置细览模板或者模板文件不存在！！","");                
                    try{                            
                        window.parent.closeSubWindow();                            
                    }catch(err){
                        //alert(err.description)
                    }
					var str = window.dialogArguments.location.href;
					var end = str.indexOf("?");
					var strArray;
					if(end != -1)
					 	strArray= str.slice(0,end);
					else
						strArray = str;
					window.dialogArguments.location.href = strArray+"?"+window.dialogArguments.document.all.queryString.value;
				</script>
		 		<%
		 		}
		 	  }
		 	}else{
		 		%>
					<script language = "javascript">
						//alert("操作失败，可能当前频道未设置细览模板或者模板文件不存在！！");
						//parent.document.all.divProcessing.style.display="none";
						window.parent.alertFun("操作失败，可能当前频道未设置细览模板或者模板文件不存在！！","");
                        try{                            
                            window.parent.closeSubWindow();                            
                        }catch(err){
                            //alert(err.description)
                        }
					</script>
				<%
		 	}
	 	}
	 	else
	 	{
		%>
			<script language = "javascript">
				//alert("操作失败，请检查所选文档能否进行审核!!");
				window.parent.alertFun("操作失败，请检查所选文档能否进行审核!!","");
                try{                            
                    window.parent.closeSubWindow();                            
                }catch(err){
                    //alert(err.description)
                }
				//parent.document.all.divProcessing.style.display="none";
			</script>
		<%
		}
	  }
	}catch(DocumentManagerException de){
		de.printStackTrace();
		%>
		<script language="javascript">
			//alert("数据库操作失败!!");
			//parent.document.all.divProcessing.style.display="none";
			window.parent.alertFun("数据库操作失败!!","");
			try{                            
                window.parent.closeSubWindow();                            
            }catch(err){
                //alert(err.description)
            }
		</script>
		<%
	}
%>