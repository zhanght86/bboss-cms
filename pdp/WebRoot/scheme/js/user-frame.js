	function download_card() {
	    formobj = document.formDownLoad;
	   	formobj.action = basePath+"/download.action?date="+ new Date().getTime();
        formobj.target = "schemeFrame1";
	    formobj.submit();  
	}
   var hidenTable ;
   var img1 ;
   function initParam(){
       hidenTable = document.getElementById("hidenTable");
       img1 = document.getElementById("img1");
   }
   function hidenTD(){
         initParam() ;
         if(hidenTable!=null){
              if(hidenTable.style.display == ""){
                  img1.src = "/Shopping/backimages/up_arrow.gif" ;
                  hidenTable.style.display = "none" ;                                    
              }else{     
                  img1.src = "/Shopping/backimages/down_arrow.gif"            
                  hidenTable.style.display = "" ;  
              }
          }
   }
   	function showdivImport() 
	{ 
		 /*scheme.style.display="" ;
		 scheme.style.left=window.event.clientX-268 ;
		 scheme.style.top=window.event.clientY+16 ; */
/*			var manageListFrameForm = $("#add_scheme_form");
			manageListFrameForm.attr("target", "_blank");
			manageListFrameForm.attr("action", basePath
					+ "/scheme/upLoad.jsp?cache="
					+ new Date().getTime());
			manageListFrameForm.submit();*/
   		   if (trim($("#tdPwSchemeTemp_schemeId").val())==""){
   			   alert("请输入方案编号后再进行人员的导入！");
   			   return;
   		   }
           JqueryDialog.Open('人员信息导入', basePath+"/scheme/upLoad.jsp?schemeId="+$("#tdPwSchemeTemp_schemeId").val()+"&cache="+ new Date().getTime(), 500, 200);			
	} 
	function hiddiv(blah) 
	{ 
		 blah.style.display="none" ;
	}
	function subMit(){
		var formobj = document.excelForm;
		
		var filedir =document.getElementById("upLoadFile").value;
		if(filedir==""){
			alert("请选择要导入的Excel文件路径！");
			return;
		}
		filedir = filedir.substring(filedir.lastIndexOf("."),filedir.length);
		filedir = filedir.replace(new RegExp(" ","gi"),"");
		if(filedir!=".xls"){
			alert("文件类型只能是Excel，即'.xls'类型！")
			return;
		}
		formobj.action = basePath+"/scheme/scheme_excelImport.action?date="+ new Date().getTime();
        formobj.target = "excelListFrame";
	    formobj.submit();  
	}
	function trim(str) { //删除左右两端的空格
　　return str.replace(/(^\s*)|(\s*$)/g, "");
}
function ltrim(str){ //删除左边的空格
　　return str.replace(/(^\s*)/g,"");
}
function rtrim(str){ //删除右边的空格
　　return str.replace(/(\s*$)/g,"");
}