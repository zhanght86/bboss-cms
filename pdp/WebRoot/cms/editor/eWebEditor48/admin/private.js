/*
*######################################
* eWebEditor v4.00 - Advanced online web based WYSIWYG HTML editor.
* Copyright (c) 2003-2007 eWebSoft.com
*
* For further information go to http://www.ewebsoft.com/
* This copyright notice MUST stay intact for use.
*######################################
*/

function BaseTrim(str){
	  lIdx=0;rIdx=str.length;
	  if (BaseTrim.arguments.length==2)
	    act=BaseTrim.arguments[1].toLowerCase()
	  else
	    act="all"
      for(var i=0;i<str.length;i++){
	  	thelStr=str.substring(lIdx,lIdx+1)
		therStr=str.substring(rIdx,rIdx-1)
        if ((act=="all" || act=="left") && thelStr==" "){
			lIdx++
        }
        if ((act=="all" || act=="right") && therStr==" "){
			rIdx--
        }
      }
	  str=str.slice(lIdx,rIdx)
      return str
}

function BaseAlert(theText,notice){
	alert(notice);
	theText.focus();
	theText.select();
	return false;
}


function HighLightOver(){
	HighLightList("#E0E6F7");
}
function HighLightOut(){
	HighLightList("");
}

function HighLightList(color){
	var el=event.srcElement;
	var b=false;
	var tabElement=null;
	while (!b){
		el=GetParentElement(el, "TR")
		if (el){
			tabElement=GetParentElement(el, "TABLE");
			if (tabElement!=null && tabElement.className.toUpperCase()=="LIST"){
				break;
			}
			el=tabElement;
		}else{
			return;
		}
	}
	
	for (var i=0;i<el.children.length;i++){
		if (el.children[i].tagName=="TD"){
			el.children[i].style.backgroundColor=color;
		}
	}
}

function GetParentElement(obj, tag){
	while(obj!=null && obj.tagName!=tag)
		obj=obj.parentElement;
	return obj;
}

function doCheckWH(flag){
	var oForm = document.myform;
	if (flag==1){
		tdPreview.innerHTML="<span style='font-size:"+oForm.d_syfontsize.value+";font-family:"+oForm.d_syfontname.value+"'>"+oForm.d_sytext.value+"</span>";
		oForm.d_sywztextwidth.value=tdPreview.offsetWidth;
		oForm.d_sywztextheight.value=tdPreview.offsetHeight;
	}else{
		var url=oForm.d_sypicpath.value;
		if (url==""){
			oForm.d_sytpimagewidth.value="0";
			oForm.d_sytpimageheight.value="0";
		}else{
			if ((url.substring(0,1)!=".")&&(url.substring(0,1)!="/")){
				url="../asp/"+url;
			}
			tdPreview.innerHTML="<img border=0 src='"+url+"'>";
			oForm.d_sytpimagewidth.value=tdPreview.offsetWidth;
			oForm.d_sytpimageheight.value=tdPreview.offsetHeight;
		}
	}
}


document.onmouseover=HighLightOver
document.onmouseout=HighLightOut