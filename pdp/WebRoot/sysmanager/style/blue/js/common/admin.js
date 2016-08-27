var activeObj = null;
function form_onload() {
	ShowMenu(getPopedom());
}

function moveon(oper){
	 if (activeObj != oper)
	 	oper.className="ButtonHover";
}

function moveover(oper){
	 if (activeObj != oper)
	 	oper.className="Button";
}

function mousedown(oper){
	 if (activeObj != null)
	 	activeObj.className = "Button";
	 oper.className="ButtonActive";
	 activeObj = oper;
}
function ButtonOnClick(oper,moduleName,modulePath){
    if(oper=="QM") {
        if(confirm("您确认退出系统吗？")) {
 	         self.parent.parent.location.replace('selectSystem.jsp');
  	     }
    }
    else if(oper=="QS") {
        if(confirm("您确认退出系统吗？")) {
 	         self.parent.parent.location.replace('index.jsp');
  	     }
    }
    else {
  	     url = "second_index.jsp?moduleID="+oper+"&moduleName="+moduleName+"&modulePath="+modulePath;
   	     self.parent.frames("Main").location.href = url;
 	 }
}



