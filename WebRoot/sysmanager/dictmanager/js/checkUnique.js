var sendHttpRequestPath = "checkUnique.jsp";
var http_request;
var fieldObj ;
var fieldObj_name ;
function send_request(obj,dicttypeId) {
    if(obj.value.replace(/(^\s*) |(\s*$)/g, "").length==0){
        return ;
    }
    fieldObj = obj
    http_request = false;
    var url = sendHttpRequestPath + "?dicttypeId="+dicttypeId+"&fieldName="+obj.name+"&fieldValue="+obj.value;
    if(window.XMLHttpRequest) {
        http_request = new XMLHttpRequest();
        if (http_request.overrideMimeType) {
            http_request.overrideMimeType('text/xml');
        }
    }
    else if (window.ActiveXObject) {
        try {
            http_request = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e) {
            try {
                http_request = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e) {}
        }
    }
    if (!http_request) {
        window.alert("不能创建XMLHttpRequest对象实例.");
        return false;
    }
    http_request.onreadystatechange = processRequest;
    http_request.open("POST", url, true);
    http_request.send(null);
}
//修改字典数据时，调用此函数..
function send_request_update(obj,dicttypeId,val) {
    if(obj.value.replace(/(^\s*) |(\s*$)/g, "").length==0){
        return ;
    }
    if(obj.value==val){
    	return;
    }
    fieldObj = obj
    http_request = false;
    var url = sendHttpRequestPath + "?dicttypeId="+dicttypeId+"&fieldName="+obj.name+"&fieldValue="+obj.value;
    if(window.XMLHttpRequest) {
        http_request = new XMLHttpRequest();
        if (http_request.overrideMimeType) {
            http_request.overrideMimeType('text/xml');
        }
    }
    else if (window.ActiveXObject) {
        try {
            http_request = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e) {
            try {
                http_request = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e) {}
        }
    }
    if (!http_request) {
        window.alert("不能创建XMLHttpRequest对象实例.");
        return false;
    }
    http_request.onreadystatechange = processRequest;
    http_request.open("POST", url, true);
    http_request.send(null);
}


//选择人员和选择机构时调用这个函数--obj隐藏的对象
function send_request_name(obj,obj_name,dicttypeId) {
    if(obj.value.replace(/(^\s*) |(\s*$)/g, "").length==0){
        return ;
    }
    fieldObj_name = obj;
    fieldObj = obj_name;
    http_request = false;
    var url = sendHttpRequestPath + "?dicttypeId="+dicttypeId+"&fieldName="+fieldObj_name.name+"&fieldValue="+fieldObj_name.value;
    if(window.XMLHttpRequest) {
        http_request = new XMLHttpRequest();
        if (http_request.overrideMimeType) {
            http_request.overrideMimeType('text/xml');
        }
    }
    else if (window.ActiveXObject) {
        try {
            http_request = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e) {
            try {
                http_request = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e) {}
        }
    }
    if (!http_request) {
        window.alert("不能创建XMLHttpRequest对象实例.");
        return false;
    }
    http_request.onreadystatechange = processRequest;
    http_request.open("POST", url, true);
    http_request.send(null);
}

//选择字典调用该函数   isNull是否为空，isUnique是否重复---选择字典不能是唯一
function send_requestSelect(obj,isNull,isUnique,dicttypeId) {
    if(obj.value.replace(/(^\s*) |(\s*$)/g, "").length==0){
        return ;
    }
    fieldObj_name = obj;
    http_request = false;
    var url = sendHttpRequestPath + "?dicttypeId="+dicttypeId+"&fieldName="+obj.name+"&fieldValue="+obj.value;
    if(window.XMLHttpRequest) {
        http_request = new XMLHttpRequest();
        if (http_request.overrideMimeType) {
            http_request.overrideMimeType('text/xml');
        }
    }
    else if (window.ActiveXObject) {
        try {
            http_request = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e) {
            try {
                http_request = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e) {}
        }
    }
    if (!http_request) {
        window.alert("不能创建XMLHttpRequest对象实例.");
        return false;
    }
    http_request.onreadystatechange = processRequest;
    http_request.open("POST", url, true);
    http_request.send(null);
}

function processRequest() {
    if (http_request.readyState == 4) {
        if (http_request.status == 200) {
           var restr = http_request.responseText.replace(/(^\s*) |(\s*$)/g, ""); 
           if(restr.length>0){
               alert("值重复!请从新输入");
               fieldObj.value = "";
               fieldObj.focus();
               fieldObj = null;
               if(fieldObj_name){
               	fieldObj_name.value = "";
               	fieldObj_name = null;
               }
               return ;
           }
            
        } else { 
            alert("您所请求的页面有异常。");
        }
       fieldObj = null;
       fieldObj_name = null;
    }
    
}

function selectOrg(obj){
	var oldValue = '';
	var org_id = obj.org_id;   
	
	var valueWin = window.showModalDialog('orgSelectTree.jsp?orgNames='+obj.value,window,'dialogWidth:'+(400)+'px;dialogHeight:'+(600)+'px;help:no;scroll:auto;status:no');
	if(valueWin!=null){
		var restr = valueWin; 
		obj.value = valueWin.split('\^')[0]; 
		document.all.item(org_id).value=valueWin.split('\^')[1];
	}
}

function selectUser(obj){
	var oldValue = '';
	var org_id = obj.org_id;   
	
	var valueWin = window.showModalDialog('selectexecutor.jsp?userNames='+obj.value,window,'dialogWidth:'+(850)+'px;dialogHeight:'+(600)+'px;help:no;scroll:auto;status:no');
	if(valueWin!=null){
		var restr = valueWin; 
		obj.value = valueWin.split('\^')[0]; 
		document.all.item(org_id).value=valueWin.split('\^')[1];
	}
}