function myOpen(url){
	window.showModalDialog(url,window,"dialogWidth:400px;dialogHeight:300px;center:yes;resizable:no;scroll:no");
}

function onMsEnter(obj){
	//obj.style.backgroundColor = "#C3D2E5";
	obj.style.cursor="hand";
}
function onMsOut(obj){
	//obj.style.backgroundColor = "#EEEEEE";
	obj.style.cursor="default";
}

function onDBClick(obj,name,isFolder){
	if(isFolder == true){
		var u = "";
		if(theURI){
			u = theURI.replace("\\", "/").replace(/^\s+$/img,"");
	
			if(u.charAt(u.length - 1)!="/"){
				u = u+"/";
			}
			if(u=="/"){
				u = "";
			}
		}
		self.location = "file_list.jsp?uri="+u+name;
	}
}

function goBack(){
	//(1)确保uri的格式为:文件夹名1/文件夹名1
	var uri = theURI.replace(/\\/img,"/");
	if(uri.charAt(0)=="/"){
		uri = uri.substr(1);
	}
	if(uri.charAt(uri.length-1)=="/"){
		uri = uri.substring(0,uri.length-1);
	}
	
	
	var uris = uri.split("/");
	uri = "";
	for(var i=0;i<uris.length-1;i++){
		uri += uris[i]+"/";
	}
	if(uri!=""){
		uri = uri.substring(0,uri.length-1);
	}
	self.location = "file_list.jsp?uri="+uri;
}
function publishFile(filename,frameObj,uri,siteId) {
	var u = uri.replace("\\", "/").replace(/\s+/img,"");
	if(u.charAt(u.length - 1)!="/"){
		u = u+"/";
	}
	if(u=="/"){
		u = "";
	}
	hidFrm.location.href = "template_pub_preview.jsp?uri="+(u+filename)+"&siteId="+siteId+"&action=publish";
}
function pubPreviewFile(filename,frameObj,uri,siteId) {
	var u = uri.replace("\\", "/").replace(/\s+/img,"");
	if(u.charAt(u.length - 1)!="/"){
		u = u+"/";
	}
	if(u=="/"){
		u = "";
	}
	hidFrm.location.href = "template_pub_preview.jsp?uri="+(u+filename)+"&siteId="+siteId+"&action=preview";
}