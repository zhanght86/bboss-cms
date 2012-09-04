/*/////////////////////////////////////////////////////////////////
????????????????????????????????????????????????????????????????

////////////////////////////////////////////////////////////////*/
function openWin(url,swidth,sheight){
     var w = showModalDialog(url,window,"dialogWidth:"+swidth+"px;dialogHeight:"+sheight+"px;help:no;scroll:auto;status:no");
	 return w;
}

function openWinCommon(url){
     window.open(url,"perspective_content");
}

function isnum()
{
  var k = window.event.keyCode;
  if (k < 48 || k > 57){
     alert("?????????")
     window.event.keyCode = 0 ;}
}

function checkFileName(fileName) {
    if (fileName.indexOf("/") != -1 || fileName.indexOf("\\") != -1 
    || fileName.indexOf(":") != -1 || fileName.indexOf("*") != -1 
    || fileName.indexOf("?") != -1 || fileName.indexOf("\"") != -1 
    || fileName.indexOf("<") != -1 || fileName.indexOf(">") != -1 
    || fileName.indexOf("|") != -1) {
        alert("?????????\\/:*?\"<>|");
        return false;
    }
    return true;
}


function viewDoc(docId,form1,target1){
	form1.action = "./../docManage/previewPubDocHandle.jsp?docId="+docId;
	form1.target = target1;
	form1.submit();
}

function high(ob){
	ob.bgColor="#E9EDF3";
}
function unhigh(ob){
	ob.bgColor="#ffffff";
}	
function checkAll(totalCheck,checkName){	//???????
	var selectAll = document.getElementsByName(totalCheck);
	var o = document.getElementsByName(checkName);
	if(selectAll[0].checked==true){
		for (var i=0; i<o.length; i++){
	      if(!o[i].disabled){
	      		o[i].checked=true;
	      }
		}
	}else{
		  for (var i=0; i<o.length; i++){
	   	  	 o[i].checked=false;
	   	  }
	}
}
//???????
function checkOne(totalCheck,checkName){
	var selectAll = document.getElementsByName(totalCheck);
	var o = document.getElementsByName(checkName);
	var cbs = true;
	for (var i=0;i<o.length;i++){
		if(!o[i].disabled){
			if (o[i].checked==false){
				cbs=false;
			}
		}
	}
	if(cbs){
		selectAll[0].checked=true;
	}else{
		selectAll[0].checked=false;
	}
}		
//???????
function haveSelect(elName){
	var isSelect = false;
	var ch = document.getElementsByName(elName);
	for (var i=0;i<ch.length;i++) {
		if (ch[i].checked){
			isSelect=true;
			break;
		}
	}
	return isSelect;
}	