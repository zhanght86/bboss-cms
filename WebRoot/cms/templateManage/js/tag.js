/**********************************************
*
*Author:GCQ
*2007.01.06
***********************************************/

//当前高亮显示的行，同时也是tplTagArray数组的下标指针
var curr_row = -1; 


//模板标签数组，系统提供的标签种类
var tplTagArray = new Array();


function getTagById(tagId){
	for(var i=0;i<tplTagArray.length;i++){
		if( tagId == tplTagArray[i].id){
			return tplTagArray[i];
		}
	}
	return -1;
}

//模板标签类
function TemplateTag(id,name,type,marker){
	this.id = id;
	this.name = name;
	this.type = type;
	this.startMarker = marker;
	this.endMarker = '</' + this.startMarker.substring(1);

	this.innerTagArray = new Array();


	this.getTagHTML = function(){
		 //原子标签
		if (this.type == '0101'){		
			return '<div class="cmstag" ondblclick="parent.setTplTagProperty(this.children(0))" '
				+' myattr="cms">'
				+ this.startMarker.substring(0,this.startMarker.length-1)
				+' cms_attr_name_desc="'+this.name+'" cms_attr_name_tagTypeId="'+this.id+'">'
				+ '<div myattr="cmslabel">'+this.name+'</div>'
				+ this.endMarker
				+'</div>';

		}else if(this.type == '03'){ //组合标签
			var innerTagCnt = this.innerTagArray.length; //子标签的个数

			var startTagHTML = '<div class="cmstag" '
				+'ondblclick="parent.setTplTagProperty(this.children(0))" '
				+' myattr="cms">'
				+this.startMarker.substring(0,this.startMarker.length-1)
				+' cms_attr_name_desc="'+this.name+'" cms_attr_name_tagTypeId="'+this.id+'">' ;
			
			var oneRow = '<div myattr="cmslabel">'+ this.name + '</div>';
			
			var secondRow = '<div myattr="cms">';

			for(var i=0;i<innerTagCnt;i++){
				secondRow += this.innerTagArray[i].getTagHTML();
			}
			
			secondRow += '</div>';

			var endTagHTML = this.endMarker + '</div>';

			return startTagHTML + oneRow + secondRow + endTagHTML;
			
		}
		
		return "Unknown tag type";
	}
}

function addInnerTag (id,subTplTagId){
	var tag = getTagById(id);
	if(tag==-1){
		alert("没有找到标签,无法为该标签添加子标签.");
		return;
	}
	var subTag = getTagById(subTplTagId);
	if(subTag==-1){
		alert("没有找到子标签,无法为该标签添加子标签.");
		return;	
	}
	tag.innerTagArray[tag.innerTagArray.length]= subTag;
}

function addTplTag(id,name,type,marker){
	tplTagArray[tplTagArray.length] = new TemplateTag(id,name,type,marker);
	return tplTagArray[ tplTagArray.length - 1];
}

function changeCurrentRow(newRow){
	if (curr_row == newRow)
	{
		return;
	}

	if(curr_row != -1){
		eval('document.all.tr'+curr_row+'.style.backgroundColor=""');
	}
			
	curr_row = newRow;

	eval('document.all.tr'+newRow+'.style.backgroundColor="#B9CEF0"');
}

function showTagName(name,row){
	document.write('<tr id="tr'+row+'" onclick="changeCurrentRow('+row+')">');
	document.write('<td><img src="../images/point_tag.gif" width="21" height="15"></td>');
	document.write('<td class="labelTD">'+name+'</td>');
	document.write('</tr>');
}