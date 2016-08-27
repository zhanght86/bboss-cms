<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
function Menu(clss11){
	this.clsName = clss11;
	//system variable
	var menuOpened = false;
	//user variable
	var oWhere = document.body;
	var menu = document.createElement("div");
	oWhere.appendChild(menu);
	menu.id="menu";
	if(this.clsName){
		menu.className=clss11;
	}else{
		menu.className="menu";
	}
	menu.onmouseup = function() {event.cancelBubble=true;}
	
	var sIds = new Array();
	this.setIDS = function(idss){
		sIds = idss;
	}
	var sNames = new Array();
	this.setNames= function(idss){
		sNames = idss;
	}
	this.getNamesStr= function(){
		var sfNamesStr = "";
		for(igns=0;igns<sNames.length;igns++){
			if(igns==0){
				sfNamesStr+=sNames[igns];
			}else{
				sfNamesStr+=","+sNames[igns];
			}
		}
		return sfNamesStr;
	}
	//ֻԐ՚ShowTypeאע²ᶄ`э²Ż␔ʾMenu
	var sShowTypes = new Array();
	this.addShowType=function(showType){
		sShowTypes[sShowTypes.length] = showType;
	}
	this.registerToDoc=function (menu1) {
		document.oncontextmenu = function() {menu1.showMenu("body"); return false;}
		document.onmouseup = function() {menu1.hiddenMenu();}
		document.onselectstart = function() {return false;}
	}
	var items = new Array();
	this.addItem=function (sItem) {
		var itemIndex = items.length;
		sItem.id = "item_id_"+itemIndex;
		sItem.imgId = "img_id_"+itemIndex;
		items[itemIndex]=sItem;
		var item = document.createElement("div");
		item.id = sItem.id ;
		if(sItem.img){item.innerHTML = "<img src="+sItem.img+" id='"+sItem.imgId+"'  align='absmiddle' />"}else{item.innerHTML = "<span></span>"};
		if(sItem.text)item.innerHTML += sItem.text;
		if(sItem.href)item.onmouseup = function() {clickGo(sItem.id,sItem.href,sItem.hrefType)}
		item.onmouseover = function() { mouseoverState(this)}
		item.onmouseout = function() { mouseoutState(this)}
		item.className="item";
		menu.appendChild(item);
/*		if(sItem.img){
			var divImg = document.getElementById(sItem.imgId);
			divImg.onmouseup =function() {return false;}
			divImg.onmousedown =function() {return false;}
			divImg.onclick =function() {return false;}
		}
*/		
	}
	
	function checkShowType(showType){
		for(var ctI=0;ctI<sShowTypes.length;ctI++){
			if(sShowTypes[ctI]==showType){
				return true;
			}
		}
		return false;
	}

	function mouseoverState(obj) {
	//	alert("mouseoverState");
		with(obj.style) {
			border="1px solid #08246B";
			backgroundColor="#9496AD"
		}
	}
	function mouseoutState(obj) {
	//	alert("mouseoutState");
		with(obj.style) {
			border="1px solid inactiveborder";
			backgroundColor="";
		}
	}
	function clickGo(itemID,href,hrefType) {
		//if(srcElem.tagName.toLowerCase()=="img"){
		//	return false;
		//}
		var divItem = document.getElementById(itemID);
		if(!divItem){
			return;
		}
		if(divItem.disabled){
			return;
		}
		sHiddenMenu();
		//alert(idss);
		//alert(href+"('"+this.ids+"')");
		switch(hrefType) {
			case 0:document.location.href=href;break;
			case 1:javascript:eval(href);break;
			//case 2:document.location.href=href+"&ids="+sIds;break;
			case 3:javascript:eval(href+"(sIds,sNames)");break;
		}
	}
	//addLine
	this.addLine =function () {
	
		var itemIndex = items.length;
		var ml = new MenuLine();
		ml.id = "line_id_"+itemIndex;
		items[itemIndex]=ml;
		
		
		var line = document.createElement("div")
		line.id = ml.id;
		line.className = "line";
		menu.appendChild(line);
	}

	this.showMenu =function (showType) {
		if(!checkShowType(showType)){
			return false;
		}
		if(menuOpened) return false;
	//disabled=true
		var haveLine =false;
		//Ɛ¶ϲ˵¥ˇ·򐕊¾,²¢¼Ȃ¼²˵¥ДʾµŌ�¬ ɧ¹�Ϊ0£¬һҹ²»Дʾ
		var menuCount = 0;
		for(iSm = 0;iSm<items.length;iSm++){
			var sItem = items[iSm];
			var divItem = document.getElementById(sItem.id);
			if(!divItem){
				continue;
			}
			if(divItem.className=="line"){
				if(haveLine || menuCount==0 || iSm==(items.length-1)){
					divItem.style.display  = "none";
					continue;
				}
				divItem.style.display  = "";
				haveLine =true;
				continue;
			}
			
			var enabled = true;
			var display = true;
			if(sItem.showType == showType || sItem.showType == ""){
				if(sItem.checkDisFunction){
					display = eval(sItem.checkDisFunction + "('display',sIds,sNames)");
					if(display){
						haveLine=false;
						menuCount++;
						enabled = eval(sItem.checkDisFunction + "('enabled',sIds,sNames)");
					}
				}else{
					haveLine=false;
					menuCount++;
				}
			}else{
				display = false;
				enabled = false;
			}
			var divImg =null;
			if(display){
				divItem.style.display  = "inline";
				if(enabled){
					divItem.disabled = false;
				}else{
					divItem.disabled = true;
				}
			}else{
				divItem.style.display  = "none";
			}
		}
		if(menuCount<1){
			return false;
		}
		menuOpened = true;
		var menu = document.all.menu;
		with(menu.style) {
			visibility="visible";
			if(event.clientX+menu.offsetWidth>document.body.offsetWidth)
				pixelLeft=event.clientX-menu.offsetWidth;
			else
				pixelLeft=event.clientX;
			if(event.clientY+menu.offsetHeight>(document.body.offsetHeight+2))
					if(event.clientY+(menu.offsetHeight/2)>document.body.offsetHeight){
						pixelTop=document.body.offsetHeight-menu.offsetHeight-1;
					}else{
						pixelTop=event.clientY-(menu.offsetHeight/2);
					}
			else
				pixelTop=event.clientY;
		}
		
	}
	this.hiddenMenu =function () {
		sHiddenMenu();
	}
	function sHiddenMenu() {
		if(!menuOpened) return false;
		menuOpened = false;
		var menu = document.all.menu;
		with(menu.style) {
			visibility="hidden";
		}
	}
}

	/*
		hrefType = 0±hrefˇһ¸�э
		hrefType = 1±hrefˇһ¸�ascript
		²»ՙʹԃ hrefType = 2±hrefˇһ¸�ѬѨҪµõ½ѡձµÉD
			՚֢¸�Ӻ􄦻⍭¼Ԓ»¸�s=? µĲϊ�refType = 3±hrefˇһ¸�ascript,ѨҪµõ½ѡձµÉD
			´«ɫµêavascript²»Ŝ´�ºì »ᴫɫһ¸�Ĳϊ�
function MenuItem(img1,text1,href1,hrefType1,showType1,checkDisFunc){
	this.id ="";
	this.imgId ="";
	this.img = img1;
	this.text = text1;
	this.href = href1;
	this.hrefType = hrefType1;
	this.showType = showType1;
	this.checkDisFunction = checkDisFunc;
}
/**
·ָ¶Տ񋈍
*/
function MenuLine(){
	this.id ="";
}

function onMenuMouseOut(){
	srcElem = event.srcElement;
	if (srcElem.tagName == "BODY"){
		sMenu.hiddenMenu();
	}
}
