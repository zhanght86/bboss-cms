function addSubSite(siteId,siteName,resName) //添加子站点
{
	var w = openWin("site_add.jsp?siteId="+siteId+"&siteName="+encodeURIComponent(siteName)+"&resName="+encodeURIComponent(resName),750,600);
	if(w!=null)
	{
		window.open("allSiteTree.jsp","perspective_toolbar");
		parent.parent.window.open("../top.jsp","perspective_topbar");
	}
}

function addRootSite() //添加根站点
{
	var w = openWin("site_add.jsp",750,600);
	if(w!=null)
	{
		window.open("allSiteTree.jsp","perspective_toolbar");
		parent.parent.window.open("../top.jsp","perspective_topbar");
	}
}

function setSiteFlow(siteId,siteName) //设置站点流程
{
	var w = openWin("changeWorkflow.jsp?siteId="+siteId+"&siteName="+encodeURIComponent(siteName),480,320);
}

function editSite(siteId) //编辑站点
{
	var w = openWin("site_edit.jsp?siteId="+siteId,750,600);
	if(w=="saveSite" || w=="statusUpdated")
	{
		window.open("allSiteTree.jsp","perspective_toolbar");
		parent.parent.window.open("../top.jsp","perspective_topbar");
	}
}
function deleteSite(siteId,siteName)//删除站点
{
   if(confirm('您确定要删除【' + siteName + '(' + siteId + ')】的站点？\n站点删除后不能恢复!')){
   if(confirm('您确定要删除【' + siteName + '(' + siteId + ')】的站点？\n站点删除后不能恢复!')){
	var w = openWin("site_del.jsp?siteId="+siteId,750,600);
	if(w!=null)
	{
		window.open("allSiteTree.jsp","perspective_toolbar");
		parent.parent.window.open("../top.jsp","perspective_topbar");
	}
	}
	}
}

function addChannel(siteId,siteName,channelId,channelName){
	if(siteId == null){
		alert("siteId为null,无法创建不属于任何站点的频道.");
		return;
	} 
	var url = "../channelManage/channel_add.jsp?siteId="+siteId+"&siteName="+encodeURIComponent(siteName);
	if(channelId!=null){
		url+="&channelId="+channelId+"&channelName="+encodeURIComponent(channelName);
	}
	var w = openWin(url,800,600);
	if(w!=null){
		window.open("navigator_content.jsp","perspective_toolbar");
	}
}

function deleteChannel(siteId,siteName,channelId,treeName){
    if(confirm('您确定要删除【' + treeName + '(' + channelId+')】频道？')){
		var w = openWin("../channelManage/channel_del.jsp?channelId="+channelId,750,600);
		if(w!=null){
			window.open("navigator_content.jsp","perspective_toolbar");
			window.parent.location = window.parent.location;
		}
	}
}


function editChannel(siteId,siteName,channelId,channelName){
    var dateNow	= new Date();
    var intSecond	= dateNow.getSeconds(); 
	if(siteId == null){
		alert("站点不存在,无法编辑不属于任何站点的频道.");
		return;
	}
	var url = "../channelManage/channel_edit.jsp?siteId="+siteId+"&siteName="+encodeURIComponent(siteName)+"&times="+intSecond;
	if(channelId==null){
		alert("频道不存在,无法编辑频道.");	
		return;
	}
	url+="&channelId="+channelId+"&channelName="+encodeURIComponent(channelName);
	var w = openWin(url,800,600);
	if(w!=null){
		window.open("navigator_content.jsp","perspective_toolbar");
	}
	
}


function changeWorkflow(siteId,siteName,channelId,channelName){
	if(!channelId){
		alert("没有提供频道id,无法编辑频道");
		return;
	}
	var url = "../channelManage/changeWorkflow.jsp?channelId="+channelId;
	var w = openWin(url,480,320);
}

function changeIndexPic(siteId,siteName,channelId,channelName){
	if(!channelId){
		alert("没有提供频道id,无法编辑频道");
		return;
	}
	var url = "../channelManage/change_indexPic.jsp?channelId="+channelId;
	var w = openWin(url,480,320);
}

function publishSite(siteId){
	var url = "../siteManage/sitepublish.jsp?siteId="+siteId;
	var w = openWin(url,480,370);
}

function publishChannel(siteId,channelId){
	var url = "../siteManage/sitepublish.jsp?siteId="+siteId+"&channelId="+channelId;
	var w = openWin(url,480,370);
}

function viewSite(siteId){
	var url = "../siteManage/siteview_do.jsp?siteId="+siteId;
	var w = openWin(url,480,320);
}

function viewChannel(siteId,channelId){
	var url = "../siteManage/siteview_do.jsp?siteId="+siteId+"&channelId="+channelId;
	var w = openWin(url,480,320);
}

function publishSiteByAll(siteId){
	alert(siteId);
}

function publishSiteByInc(siteId){
	alert(siteId);
}

function publishChannelByAll(channelId){
	alert(channelId);
}

function publishChannelByInc(channelId){
	alert(channelId);
}
function copyDoc(siteId,channelId){
	if(siteId == null){
		alert("siteId为null");
		return;
	}
	var url = "../channelManage/channel_doc_xz.jsp?siteid="+siteId+"&flag=2&channelid="+channelId;
	var w = openWin(url,750,550);
}
function moveDoc(siteId,channelId){
	if(siteId == null){
		alert("siteId为null");
		return;
	}
	var url = "../channelManage/channel_doc_xz.jsp?siteid="+siteId+"&flag=4&channelid="+channelId;
	var w = openWin(url,750,550);
}
function arrangeDocM(siteId,channelId){
	if(channelId == null){
		alert("channelId为null");
		return;
	}
	
	var url = "../docManage/docArrange_manage.jsp?siteid="+siteId+"&channelid="+channelId;
	var w = openWin(url,800,700);
}

function citeDocManager(siteId,siteName,channelId,channelName){
	if(channelId == null){
		alert("channelId??null");
		return;
	}
	
	var url = "../docManage/cited_doc_manager.jsp?siteid="+siteId+"&channelid="+channelId+"&channelname="+encodeURIComponent(channelName);
	var w = openWin(url,800,700);
}

//扩展字段
function extfieldmanage(channelId)
{
	//alert(parent.frames[0].name);
	//alert(parent.frames[1].name);
	//alert(parent.frames[2].name);
	//parent.perspective_content.document.location.href = "../docManage/doc_extfieldofsiteorchl_list.jsp?type=2&id=" + channelId;
	var url = "../docManage/doc_extfieldofsiteorchl_list.jsp?type=2&id=" + channelId;
	parent.window.open(url,"base_properties_content");
}
//设置站点首页模板
function setSiteIndexTemplate(siteId)
{		
	var w = showModalDialog("../siteManage/site_set_index_templete.jsp?siteId="+siteId+"&type=0&isSite=is","setChannelOutlineTemplateId","dialogWidth:400px;dialogHeight:250px;help:no;scroll:no;status:no");
	try{
	if(w.text && w.val){
		document.getElementById("divSiteIndexTemplate").innerText = w.text;
		document.all.siteForm.templateId.value = w.val;
	}
	}catch(e){}
	
}
//设置频道模板
function editChannelTemp(siteId,siteName,channelId,channelName)
{
	var url = "../channelManage/channel_templeteSet_jsp.jsp?siteId="+siteId+"&siteName="+encodeURIComponent(siteName)+"&channelId="+channelId+"&channelName="+encodeURIComponent(channelName);
	var w = showModalDialog(url,"setSiteIndexTemplate","dialogWidth:400px;dialogHeight:300px;help:no;scroll:no;status:no");
	try{
	if(w.text && w.val){
		document.getElementById("divSiteIndexTemplate").innerText = w.text;
		document.all.siteForm.templateId.value = w.val;
	}
	}catch(e){}
}
//站点权限管理
function siteSysManager(siteId,siteName)
{
	var url = "../siteManage/res_manager.jsp?resId2="+siteId+"&resTypeId2=site&resTypeName="+encodeURIComponent(siteName)+"&title="+encodeURIComponent(siteName);
	parent.window.open(url,"base_properties_content");
}
//站点权限管理(用户授权)
function siteSysManaUsers(siteId,siteName)
{
	var url = "../siteManage/res_manager_users.jsp?resTypeId=site&channelId="+siteId+"&channelName="+encodeURIComponent(siteName);
	parent.window.open(url,"base_properties_content");
}
//频道权限管理
function channelSysManager(channelId,channelName)
{
	var url = "../siteManage/res_manager_chl.jsp?resId2="+channelId+"&resTypeId2=channel&resTypeName="+encodeURIComponent(channelName)+"&title="+encodeURIComponent(channelName);
	parent.window.open(url,"base_properties_content");
}
//频道权限管理(用户授权)
function channelSysManaUsers(channelId,channelName)
{
	var url = "../siteManage/res_manager_users.jsp?resTypeId=channel&channelId="+channelId+"&channelName="+encodeURIComponent(channelName);
	parent.window.open(url,"base_properties_content");
}

function switchComments(channelId)
{
	openWin("../docManage/docCommentSwitch.jsp?channelId="+channelId,260,152);
}

//频道评论审核开关
function switchCommentAudit(channelId)
{
	openWin("../docManage/docCommentAuditSwitch.jsp?channelId="+channelId,260,152);
}

//移动频道
function moveChl(siteId)
{
	openWin("../channelManage/moveChl_frame.jsp?siteid="+siteId,600,480);
}
//站点浏览
function siteUrlView(siteename){
   window.open("../siteManage/site_urlview.jsp?siteename=" + encodeURIComponent(siteename));
}
//频道浏览
function channelUrlView(siteid,channelid){
   window.open("../channelManage/channel_urlview.jsp?siteid=" + siteid + "&channelid=" + channelid);
}