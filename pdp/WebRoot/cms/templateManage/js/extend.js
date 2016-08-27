/**********************************************
*
*Author:GCQ
*2007.01.06
***********************************************/

var tplNavFrame = null; //Navigator框架
var tplTagFrame = null; //模板标签面板框架

function clickEditorBody()
{
	tplNavFrame = parent.parent.template_navigator;

	if(!tplNavFrame){
		alert("错误，不能取到Navigator框架的引用!");
		return;
	}
	
	tplTagFrame = tplNavFrame.frames[1]; //模板标签框架
	if(!tplTagFrame){
		alert("错误，不能取到模板标签面板框架的引用!");
		return;
	}

	var sHTML = '';
	
	var e = eWebEditor.event;
	
	if ( tplTagFrame.curr_row && (tplNavFrame.oldClickedItem == 0 )) //不是选中了"取消选择"而且打开了模板标签面板
	{
		var currentTag = tplTagFrame.tplTagArray[tplTagFrame.curr_row - 1]; //当前选中的标签
		var tagHTML = currentTag.getTagHTML();
		
		alert(tagHTML);

		if(e.srcElement.tagName == 'TD'){ //点击了TD元素
			sHTML = e.srcElement.innerHTML;
			e.srcElement.innerHTML = sHTML + tagHTML;
		}else{ //点击了其他地方
			sHTML = eWebEditor.document.body.innerHTML;
			eWebEditor.document.body.innerHTML = sHTML + tagHTML;
		}
		
		tplTagFrame.changeCurrentRow(0); //选中"取消选择"
	}
	
	return false;
}

//设置标签的属性
function setTplTagProperty(node){
	//alert(node.outerHTML);
	currentNode = node;
	var typeId = node.cms_attr_name_tagTypeId;
	//alert("类型id:"+typeId);
	if(!typeId){
		alert("当前节点的typeId属性被删掉,无法确定它有那些属性.");
		return;
	}
	var reg = eval("/<cms:"+node.tagName+"([^>]*)>/igm");
	node.outerHTML.match(reg);
	//alert("所有属性:"+RegExp.$1);
	var styleAttrs = new Array();	//样式属性对
	var attrs = new Array();		//普通属性对

	var allAttrs = RegExp.$1;
	reg = eval('/\s*cms_attr_name_style="([^"]*)"/igm');
	allAttrs.match(reg);
	if(allAttrs!=RegExp.$1){
		styleAttrs = RegExp.$1.split(/\s*;\s*/igm);
	}
	attrs = allAttrs.replace(reg," ").split(/\s+/igm);

	var attrStr = "";
	for(var i=0;i<attrs.length;i++){
		var attr = attrs[i].split("=");	
		attrStr +=(";;;"+attr[0]+":::"+attr[1].substring(1,attr[1].length-1));
	}
	var styleAttrStr = "";
	for(var i=0;i<styleAttrs.length;i++){
		var attr = styleAttrs[i].split(/\s*:\s*/igm);	
		styleAttrStr +=(";;;"+attr[0]+":::"+attr[1]);		
	}
	if(styleAttrStr!=""){
		styleAttrStr = styleAttrStr.substr(3);
	}
	var tagsFrm = parent.template_navigator.frames[1];

	var url = "tagProperty.jsp?tagId="+typeId+"&attrs="+attrStr.substr(3)+"&styleAttrs="+styleAttrStr;
	//alert(url);
	var swidth = 500;
	var sheight = 500;

	var w = parent.showModalDialog(url,self,
			"dialogWidth:"+swidth+"px;dialogHeight:"+sheight+"px;help:no;scroll:auto;status:no");
}

//编辑属性时的当前标签
var currentNode = null;
function setTagAttributes(attrs){
	//alert("返回的attrs:"+attrs);

	var reg = eval("/<cms:"+currentNode.tagName+"([^>]*)>/igm");
	currentNode.outerHTML.match(reg);
	var nodebegin1 = "<cms:"+currentNode.tagName+RegExp.$1;
	var nodebegin2 = "<cms:"+currentNode.tagName+attrs;
	currentNode.outerHTML = currentNode.outerHTML.replace(nodebegin1,nodebegin2);

}

function getTemplateText(){
	if(sCurrMode == "CODE"){
		return eWebEditor.document.body.innerText;
	}else{
		return converToCode();
	}

}



function convertHtmlToText(text){
	return text;
}

function convertTextToHtml(text){
	return text;
}



function converToCode(){
	var	res = "";
	var t = eWebEditor.document.body.innerHTML;
	var tempContainer = document.createElement("div");
	tempContainer.style.display="none";
	tempContainer.innerHTML = t;
	
	document.appendChild(tempContainer);
	var dall = tempContainer.all;
	for(var i=0;i<dall.length;i++){
		if(dall(i).myattr && dall(i).myattr=="cms"){
			dall(i).outerHTML = dall(i).innerHTML;
			i=0;
		} 
		if(dall(i).myattr && dall(i).myattr=="cmslabel"){
			var nodesHTML = "";
			
			for(var j=0;j<dall(i).children.length;j++){
				//alert(dall(i).children.length);
				nodesHTML += dall(i).children(j).outerHTML;
				//alert(nodesHTML);
				dall(i).removeChild(dall(i).children(j));
				j=0;
			}
			dall(i).parentNode.cms_attr_name_desc = dall(i).innerText;
			dall(i).parentNode.innerHTML = dall(i).parentNode.innerHTML+nodesHTML;
			dall(i).outerHTML = "";
			i=0;
		}
	}
	var t = tempContainer.innerHTML.replace(/<\?xml:namespace\s*prefix\s*=\s*cms\s*\/>/gim,'');
	tempContainer.outerHTML = ""; 
	t = t.replace(/cms_attr_name_/gim,'');
	return t; 
}

//改变属性的名字
function changeAttrName(text){
	var mc = text.match(/<cms:[^>]*>/igm);
	for(var i=0;mc!=null && i<mc.length;i++){
		var t = mc[i].replace(/(\s+)([^'"=]*={1}["']{1})/igm,"$1cms_attr_name_$2");
		//alert(t);
		text = text.replace(mc[i],t);
	}
	return text;
}

//从代码模式转换为编辑模式
function converToEdit(text){
	//把代码模式里的代码,存储在一个临时的div中,方便用DOM来操作
	var temp = eWebEditor.document.createElement("div");
	temp.innerHTML = changeAttrName(text);
	eWebEditor.document.body.appendChild(temp);
	
	//是否返回空白
	for(var i=0;i<temp.all.length;i++){
		//alert("节点本身:"+temp.all(i).outerHTML+"\n\n\n节点的最后一个子节点:"+(temp.all(i).lastChild?temp.all(i).lastChild.outerHTML:""));
		
		//如果1、元素不上<cms:开头的
		//同时2、没有最后一个子元素(肯定没有处理过,处理)
		//2.1如果有最后一个子元素,但类型是Text node(肯定没有处理过,处理)
		//2.2如果类型是不是Text node(肯定是element node),如果它的innerText不是当前节点的中文名字的话(没有处理过,处理)
		//如果是当前中文名字,表明已经处理,跳过.
		
		if(temp.all(i).outerHTML.indexOf(xmlHead)!=-1 
			&& temp.all(i).cms_attr_name_desc
			&&(temp.all(i).lastChild==null 
				||temp.all(i).lastChild.nodeType == 3 
				||temp.all(i).lastChild.innerText!=temp.all(i).cms_attr_name_desc
				||temp.all(i).lastChild.myattr ==null
				||temp.all(i).lastChild.myattr !="cmslabel"
			)
		){
			var div = eWebEditor.document.createElement("<div class=\"cmstag\" myattr=\"cms\" ondblclick='parent.setTplTagProperty(this.children(0))' >");
			eWebEditor.document.body.appendChild(div);
			var labelDiv = eWebEditor.document.createElement("<div myattr=\"cmslabel\">");
			labelDiv.innerText = temp.all(i).cms_attr_name_desc
			temp.all(i).appendChild(labelDiv);
			temp.all(i).applyElement(div);
			i=0;
		}
	}
	//alert("总共循环了:"+j);
	//alert(temp.outerHTML);
	return  temp.innerHTML;
}


function onLabelChange(node){
	alert(this.outerHTML);
}