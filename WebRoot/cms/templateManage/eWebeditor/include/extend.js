/**********************************************
*
*Author:GCQ
*2007.01.06
***********************************************/

var tplNavFrame = null;
var tplTagFrame = null;

function clickEWebEditorBody()
{
	tplNavFrame = parent.parent.template_navigator;
	tplTagFrame = tplNavFrame.frames[1]; //模板标签框架
	
	var sBody = '';
	
	var e = eWebEditor.event;
	
	if ( tplTagFrame.curr_row && (tplNavFrame.oldClickedItem == 0 ))
	{
		var currentTag = tplTagFrame.tplTagArray[tplTagFrame.curr_row - 1]; //当前选中的标签
		var tagHTML = currentTag.getTagHTML('<BR>');

		if(e.srcElement.tagName == 'TD'){
			sBody = e.srcElement.innerHTML;
			e.srcElement.innerHTML = sBody + tagHTML;
		}else{
			sBody = eWebEditor.document.body.innerHTML;
			eWebEditor.document.body.innerHTML = sBody + tagHTML;
		}
		
		tplTagFrame.changeCurrentRow(0);
	}
	
	//alert(e.srcElement.innerHTML);

	/*var xx = tplTagFrame.tplTagInstanceArray;
	for(var i=0;i<xx.length;i++)
	{
		var yy = xx[i];
		alert("instanceId:"+yy.instanceId);
		alert("tagId:"+yy.tagId);
	}*/
	
	return false;
}

function setTplTagProperty(tagId)
{
	alert(tagId);
}


function convertHtmlToText(text)
{
	var workText = text;
	workText = workText.replace(xmlHead,'');

	var pos = 0;
	var pos2 = -1;
	var pos3 = -1;
	var startIndex = 0;

	//<cms:cell>元素嵌套的情形暂未实现
	while( pos != -1 )
	{
		pos = workText.indexOf('<cms:cell',startIndex);
		if( pos != -1 ){
			pos2 = workText.indexOf('>',pos+9);
			pos3 = workText.indexOf('</cms:cell>',pos+9);
			workText = workText.replace(workText.substring(pos2+1,pos3),'');
			startIndex = pos2+11;
		}
	}

	pos = 0;
	pos2 = -1;
	pos3 = -1;
	startIndex = 0;
	while( pos != -1 )
	{
		pos = workText.indexOf('<cms:list',startIndex);
		if( pos != -1 ){
			pos2 = workText.indexOf('>',pos+9);
			pos3 = workText.indexOf('</cms:list>',pos+9);
			
			var tmp = getCellStr(workText.substring(pos2+1,pos3));
			workText = workText.replace(workText.substring(pos2+1,pos3),tmp);
			startIndex = pos2+11;
		}
	}
	return workText;
}


function getCellStr(text)
{
	var workText = text;
	
	var ret = '';

	var pos = 0;
	var pos2 = -1;
	var startIndex = 0;

	//<cms:cell>元素嵌套的情形暂未实现
	while( pos != -1 )
	{
		pos = workText.indexOf('<cms:cell',startIndex);
		if( pos != -1 ){
			pos2 = workText.indexOf('>',pos+9);
			ret += workText.substring(pos,pos2+1)+'</cms:cell>';
			startIndex = pos2+11;
		}
	}
	return ret;
}

function convertTextToHtml(text)
{


}