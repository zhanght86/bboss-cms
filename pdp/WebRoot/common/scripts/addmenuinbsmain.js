/**
 * 从外呼添加TAB页(不是从菜单进去的)
 * @param frameobj 添加TAB的目标页面对像(在这里是指centerContent.html)
 * @param objStr 添加的对像对应的字符串,如: "<span class='func' id='' title='' dataType='iframe' dataLink='url' iconImg='images/msn.gif'>menuName</span>";
 *   id 与数据库的菜单ID保持一致,dataLink为数据库中URL一致,iconImg与数据库中PICTURE_URL一致

 * @param parentId 添加的TAB页对应的父菜单ID
 * @return
 */
function addTabeItemByStr(frameobj,objStr,parentId){
	var obj = $(objStr);
		frameobj.$('table').first().show("fast");
		var tabHash = frameobj.tabHash;									
		if (typeof tabHash == 'undefined' || tabHash.length<=0){
			//删除首页
			frameobj.$("#indexdiv").remove();
			//创建TAB DIV
			frameobj.jericho.buildTabpanel(frameobj.maxTabs);
		}
		obj.attr('id',obj.attr('id')+"Node");
	frameobj.addTableItemByStr(objStr);
}
/**
 * 从外呼添加TAB页(不是从菜单进去的)
 * @param frameobj 添加TAB的目标页面对像(在这里是指centerContent.html)
 * @param obj 添加的对像,如: $("<span class='func' id='' title='' dataType='iframe' dataLink='url' iconImg='images/msn.gif'>menuName</span>");
 *   id 与数据库的菜单ID保持一致,dataLink为数据库中URL一致,iconImg与数据库中PICTURE_URL一致

 * @param parentId 添加的TAB页对应的父菜单ID
 * @return
 */
function addTabeItem(frameobj,obj,parentId){
//	var jsonObj = $("<span class='func' id='' title='' dataType='iframe' dataLink='url' iconImg='images/msn.gif'>menuName</span>");
	//alert(top.frames["mainFrame"].id);
		frameobj.$('table').first().show("fast");
		var tabHash = frameobj.tabHash;									
		if (typeof tabHash == 'undefined' || tabHash.length<=0){
			//删除首页
			frameobj.$("#indexdiv").remove();
			//创建TAB DIV
			frameobj.jericho.buildTabpanel(frameobj.maxTabs);
		}
		obj.attr('id',obj.attr('id')+"Node");
	frameobj.addTabeItem(obj);
}

/**
 * 打开公告便笺TAB
 * @param frameobj 添加TAB的目标页面对像(在这里是指centerContent.html)
 * @return
 */
function addNoticeItem(frameobj){
	var obj = $("<span class='func' id='RES-41-111' title='公告便笺' dataType='iframe' dataLink='notes/notesMain.jsp' iconImg='images/msn.gif'>公告便笺</span>");
	addTabeItem(frameobj,obj,'RES-4-111');
}

/**
 * 判断TAB页是否已经打开
 * @param frameobj 添加TAB的目标页面对像(在这里是指centerContent.html)
 * @param tabId 与数据库的菜单ID保持一致

 * @return
 */
function isExistsTab(frameobj,tabId){
	return frameobj.isExistsTab(tabId);
}

/**
 * 关闭TAB页
 * @param frameobj  添加TAB的目标页面对像(在这里是指centerContent.html)
 * @param tabId 与数据库的菜单ID保持一致
 * @return
 */
function colseTabe(frameobj,tabId) {
	if (isExistsTab(frameobj,tabId))
		frameobj.colseTabe(tabId);
}

/**
 * 添加TAB的目标页面对像(在这里是指centerContent.html)
 * @param frameobj 添加TAB的目标页面对像(在这里是指centerContent.html)
 * @param obj 添加的对像,如: $("<span class='func' id='' title='' dataType='iframe' dataLink='url' iconImg='images/msn.gif'>menuName</span>");
 *  id 与数据库的菜单ID保持一致,dataLink为数据库中URL一致,iconImg与数据库中PICTURE_URL一致
 * @param parentId 添加的TAB页对应的父菜单ID
 * @return
 */
function addTabAfterColse(frameobj,obj,parentId){
	if (frameobj.channel=="BS"||frameobj.channel=="bs"){
		var divwindow = top.frames["mainFrame"];
		obj.attr('id',obj.attr('id')+"Node");	
		divwindow.parent.topFrame.$("#mainMenu").find('li[id='+parentId+']').click();
	} 
	frameobj.addAfterClose(obj);
}
