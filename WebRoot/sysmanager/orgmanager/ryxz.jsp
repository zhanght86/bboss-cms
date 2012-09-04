<%@ page contentType="text/html; charset=UTF-8"%>
<html>
<head>
<title>选择人员</title>
<meta http-equiv="Content-Type" content="text/html;">
<!-- Fireworks MX Dreamweaver MX target.  Created Thu Mar 16 15:25:33 GMT+0800 (?D1ú±ê×?ê±??) 2006-->
<link type="text/css" rel="StyleSheet" href="css/treeview.css">
</head>
<script>
  window.resizeTo(616, 500);
</script>
<%
  String type1=request.getParameter("type1");
  String type2=request.getParameter("type2");

%>
<SCRIPT LANGUAGE="JavaScript">
<!--
var objTree=null
var objTree2=null
function changecolor(e){
	e.style.background="red";
}
function okadd(){
	var names="";
	var ids="";
	for(var i=0;i<document.all("onedet2").options.length;i++){
		names+=document.all("onedet2").options[i].text+",";
		ids+=document.all("onedet2").options[i].value+",";
	}
	if(names.length>1){
		names=names.substring(0,names.length-1);
	}
	opener.<%=type1%>.value=names;
	opener.<%=type2%>.value=ids;
	window.close();

}
function alladd(){
  	opener.<%=type1%>.value="all";
	opener.<%=type2%>.value="all";
	window.close();
}
function initpara(){
   var names=opener.<%=type1%>.value.split(",");
   var ids=opener.<%=type2%>.value.split(",");
   var len=names.length;
   if(names.length>ids.length) len=ids.length;

   for(var i=0;i<=len-1;i++){
	   if(names[i].length!=0){
	   var op=new Option(names[i],ids[i]);
	   document.all("onedet2").add(op);
       }
   }
   
}
function init(){
	

	
	document.all("treeView").style.display="block";
document.all("treeView2").style.display="none";

 objTree = document.all("treeView");
	   objTree.Tree.ImageList[0].src = "public/tableTree/renyuan.gif";

		objTree.Tree.ImageList[1].src = "public/menutree/ftv2folderopen.gif";
		objTree.Tree.ImageList[2].src = "public/menutree/ftv2doc.gif";
		objTree.Tree.Param=new Array();
		
		objTree.Event_OnNodeClick = "OnTreeClick1()";
		
		objTree.LoadStream( "orgstaffdata.jsp" );	
		//objTree.Tree.Extent( objTree.Tree.Node.Key );
		initbutton();
		//objTree.Tree.Node.movenext();
	    
        
}

function query(){
	var yy=document.all("aa").value;
	if(yy==""||yy==null)return false;
	   for (var m=document.all("onedet").options.length-1;m>=0;m--)
         document.all("onedet").options[m]=null
	var no;
	if(document.all("treeView2").style.display=="block"){
	   no=document.all("treeView2");
	}
    else{
		no=document.all("treeView");
	}
	
	
//	alert(no.Tree.DataXml.xml)
    addquery(yy)

}
function  addquery(yy){
	var ol=document.all("onedet").options.length;
	for( var i=curnum-pagesize;i<curnum;i++){
		if(i>allxml.childNodes.length) break;
		var node=allxml.childNodes(i);

             if(node.getElementsByTagName("user_realname")(0).text.indexOf(yy)>=0||node.getElementsByTagName("user_name")(0).text.indexOf(yy)>=0){
			var op=new Option(node.getElementsByTagName("user_realname")(0).text,node.getElementsByTagName("user_id")(0).text);
			document.all("onedet").add(op);
			 }
	
	}
}
function ifquery(){
    if (window.event.keyCode==13) query();

}
function gogroup(){
		
	document.all("treeView2").style.display="block";
	document.all("treeView").style.display="none";
  		objTree2 = document.all("treeView2");
	 
	    objTree2.Tree.ImageList[0].src = "public/tableTree/renyuan.gif";

		objTree2.Tree.ImageList[1].src = "public/menutree/ftv2folderopen.gif";
		
		objTree2.Tree.Param=new Array();
		
		objTree2.Event_OnNodeClick = "OnTreeClick2()";
			

		objTree2.LoadStream( "groupdata.jsp" );	
		objTree2.Tree.Extent( objTree2.Tree.Node.Key );
		objTree2.Tree.Node.movenext();
	
}

function DbSelect(){
  
}
function getxmlHttp(){
        XmlHttp = new ActiveXObject("Microsoft.XMLHTTP")
		return XmlHttp;

}
function GetXmlStr(XmlStr)
	{
		try
        {
            var doc = new ActiveXObject("MSXMl.DOMDocument");
            var RootNode;
            doc.async=false
            doc.loadXML(XmlStr)
            if (doc.readyState==4)
            {
                RootNode=doc.documentElement;
            }
			else
			{
				return null;
			}
            return RootNode;
        }
        catch(err)
        {
            return null;
        }
	}
	var rownum=0;
	var curnum=0;
	var allxml=null;
	var pagesize=100;
function initbutton(){
	var pn=parseInt(rownum/pagesize+1)
	var cn=parseInt(curnum/pagesize+1);
	document.all("pflag").innerHTML=cn+"/"+pn+"页";
	if(rownum<curnum+pagesize){
		document.all("npage").disabled=true;
	}else{
	document.all("npage").disabled=false;
	}
	if(curnum<=0){
		document.all("ppage").disabled=true;
	}else{
	document.all("ppage").disabled=false;
	}
	}
function nexpage(){
	curnum=curnum+pagesize;
	for (var m=document.all("onedet").options.length-1;m>=0;m--){
         document.all("onedet").options[m]=null
		}
		  for(var i=curnum;i<curnum+pagesize;i++){
			  if(i>=rownum){
				  break;
			  }
			   var cno=allxml.childNodes[i];
			   var text=cno.getElementsByTagName("user_realname")(0).text;
			    var id=cno.getElementsByTagName("user_id")(0).text;           
		       var op=new Option(text,id);
			    document.all("onedet").add(op);     
	}
	//curnum=curnum+pagesize;
	initbutton();
}
function previorspage(){
		curnum=curnum-pagesize;  
		if(curnum<0){
			curnum=curnum+pagesize;  
			return null;
		}
    for (var m=document.all("onedet").options.length-1;m>=0;m--){
         document.all("onedet").options[m]=null
		}
		  for(var i=curnum;i<curnum+pagesize;i++){
			  if(i>=rownum){
				
				  break;
			  }
			   var cno=allxml.childNodes[i];
			   var text=cno.getElementsByTagName("user_realname")(0).text;
			    var id=cno.getElementsByTagName("user_id")(0).text;           
		       var op=new Option(text,id);
			    document.all("onedet").add(op);     
	}
	initbutton();

}
function OnTreeClick1(){
	     curnum=0;
	     var cnode=objTree.Tree.getCurrentNode().DataXml;
		 var jobid=cnode.getAttribute("key");
		 var xmlhttp=getxmlHttp();


		 var SearchPageName="getjob.jsp?job="+(jobid.replace("^","#"));
			xmlhttp.open("POST",SearchPageName,false)
			xmlhttp.setRequestHeader("Content-Type","text/xml");
			xmlhttp.send("");
             allxml=GetXmlStr(xmlhttp.responseText);
             rownum=allxml.childNodes.length;
			initbutton();

       for (var m=document.all("onedet").options.length-1;m>=0;m--){
         document.all("onedet").options[m]=null
		}
		  for(var i=curnum;i<curnum+pagesize;i++){
			   var cno=allxml.childNodes[i];
			   var text=cno.getElementsByTagName("user_realname")(0).text;
			    var id=cno.getElementsByTagName("user_id")(0).text;           
		       var op=new Option(text,id);
			    document.all("onedet").add(op);     
		   }
		 

}
function click1(e){
	   var onenode=e;   
       for(var i=0;i<onenode.length;i++){
		if(onenode[i].getAttribute("selectimage")=="0"){
			var op=new Option(onenode[i].getAttribute("text"),onenode[i].getAttribute("id"));
			document.all("onedet").add(op);
		}
		else{
		click1(onenode[i].childNodes)				
		}
	   }
}
function OnTreeClick2(){
	  if(objTree2.Tree.getCurrentNode().DataXml.getAttribute("selectimage")=="0"){
			 var n=document.all("onedet2").options.length-1;
			addone(objTree2.Tree.getCurrentNode().DataXml.getAttribute("text"),objTree2.Tree.getCurrentNode().DataXml.getAttribute("key"),n);
			return;
		}
	   var onenode=objTree2.Tree.getCurrentNode().DataXml.childNodes;
	    if(document.all("incc").checked==true){
		   for (var m=document.all("onedet").options.length-1;m>=0;m--)
            document.all("onedet").options[m]=null
		    click1(onenode)
	        return;
	   }
	    for (var m=document.all("onedet").options.length-1;m>=0;m--)
         document.all("onedet").options[m]=null
       for(var i=0;i<onenode.length;i++){
			if(onenode[i].getAttribute("selectimage")=="0"){
		   var op=new Option(onenode[i].getAttribute("text"),onenode[i].getAttribute("key"));
           document.all("onedet").add(op);
			}
	   }
}
function add(){
	
    var n=document.all("onedet2").options.length-1;
	
  	
  for(var i=0;i<document.all("onedet").options.length;i++){

   var op=document.all("onedet").options[i];
   if(op.selected)
   addone(op.text,op.value,n);
  }
}
function addoj(){
    var n=document.all("onedet2").options.length-1;
	
  	
  for(var i=0;i<document.all("onedet").options.length;i++){

   var op=document.all("onedet").options[i];
   if(op.selected)
   addone(op.text,op.value,n);
  }
}
function addall(){
	var n=document.all("onedet2").options.length-1;
	var p=document.all("onedet").options.length-1;
	if(n*p>=10000){
		if(!confirm("选择了太多的用户会造成系统速度变慢，继续吗？")){
			return;
		}
	}
   if(fm1.rd[1].checked){
     for(var i=0;i<document.all("onedet").options.length;i++){
     var op=document.all("onedet").options[i];
     addone(op.text,op.value,n);

  } 
  return;
   }
	
  for(var i=0;i<document.all("onedet").options.length;i++){
   var op=document.all("onedet").options[i];
   addone(op.text,op.value,n);
  }
}
function addone(name,value,n){

   for(var i=0;i<document.all("onedet2").options.length;i++){
		if(value==document.all("onedet2").options[i].value){
		  return;
		}
	}
   var op=new Option(name,value);
   document.all("onedet2").add(op);
}
function removeall(){
 for (var m=document.all("onedet2").options.length-1;m>=0;m--)
         document.all("onedet2").options[m]=null
}
function remove(){
 for (var m=document.all("onedet2").options.length-1;m>=0;m--)
	      if(document.all("onedet2").options[m].selected)
         document.all("onedet2").options[m]=null

}
function removeoj(){
 for (var m=document.all("onedet2").options.length-1;m>=0;m--)
	      if(document.all("onedet2").options[m].selected)
         document.all("onedet2").options[m]=null

}

//-->
</SCRIPT>

<body bgcolor="#ffffff" topmargin=0 leftmargin="0" onload="init()">
<xml id="xmldata">
</xml>
<table border="0" cellpadding="0" cellspacing="0" width="615" height="100" topmargin=0 leftmargin="0">


  <tr>
   <td><img name="ryxz1_r1_c1" src="imge/ryxz1_r1_c1.jpg" width="21" height="24" border="0" alt=""></td>
   <td><img name="ryxz1_r1_c2" src="imge/ryxz1_r1_c2.jpg" width="2" height="24" border="0" alt=""></td>
   <td rowspan="2" colspan="17"><img name="ryxz1_r1_c3" src="imge/ryxz1_r1_c3.jpg" width="573" height="70" border="0" alt=""></td>
   <td><img name="ryxz1_r1_c20" src="imge/ryxz1_r1_c20.jpg" width="19" height="24" border="0" alt=""></td>
   <td><img src="imge/spacer.gif" width="1" height="24" border="0" alt=""></td>
  </tr>
  <tr>
   <td rowspan="10" colspan="2"><img name="ryxz1_r2_c1" src="imge/ryxz1_r2_c1.jpg" width="23" height="426" border="0" alt=""></td>
   <td rowspan="10"><img name="ryxz1_r2_c20" src="imge/ryxz1_r2_c20.jpg" width="19" height="426" border="0" alt=""></td>
   <td><img src="imge/spacer.gif" width="1" height="46" border="0" alt=""></td>
  </tr>
  <tr>
   <td><img name="ryxz1_r3_c3" src="imge/ryxz1_r3_c3.jpg" width="15" height="18" border="0" alt=""></td>
   <td rowspan="8" colspan="2"><img name="ryxz1_r3_c4" src="imge/ryxz1_r3_c4.jpg" width="170" height="362" border="0" alt=""></td>
   <td><img name="ryxz1_r3_c6" src="imge/ryxz1_r3_c6.jpg" width="5" height="18" border="0" alt=""></td>
   <td><img name="ryxz1_r3_c7" src="imge/ryxz1_r3_c7.jpg" width="12" height="18" border="0" alt=""></td>
   <td rowspan="11"><img name="ryxz1_r3_c8" src="imge/ryxz1_r3_c8.jpg" width="14" height="407" border="0" alt=""></td>
   <td rowspan="3"><img name="ryxz1_r3_c9" src="imge/ryxz1_r3_c9.jpg" width="14" height="31" border="0" alt=""></td>
   <td rowspan="3"><img name="ryxz1_r3_c10" src="imge/ryxz1_r3_c10.jpg" width="5" height="31" border="0" alt=""></td>
   <td rowspan="6" colspan="3"><img name="ryxz1_r3_c11" src="imge/ryxz1_r3_c11.jpg" width="123" height="342" border="0" alt=""></td>
   <td rowspan="3"><img name="ryxz1_r3_c14" src="imge/ryxz1_r3_c14.jpg" width="19" height="31" border="0" alt=""></td>
   <td rowspan="8" colspan="5"><img name="ryxz1_r3_c15" src="imge/ryxz1_r3_c15.jpg" width="196" height="362" border="0" alt=""></td>
   <td><img src="imge/spacer.gif" width="1" height="18" border="0" alt=""></td>
  </tr>
  <tr>
   <td><img name="ryxz1_r4_c3" src="imge/ryxz1_r4_c3.jpg" width="15" height="4" border="0" alt=""></td>
   <td rowspan="6" colspan="2"><img name="ryxz1_r4_c6" src="imge/ryxz1_r4_c6.jpg" width="17" height="339" border="0" alt=""></td>
   <td><img src="imge/spacer.gif" width="1" height="4" border="0" alt=""></td>
  </tr>
  <tr>
   <td rowspan="6"><img name="ryxz1_r5_c3" src="imge/ryxz1_r5_c3.jpg" width="15" height="340" border="0" alt=""></td>
   <td><img src="imge/spacer.gif" width="1" height="9" border="0" alt=""></td>
  </tr>
  <tr>
   <td rowspan="2" colspan="2"><img name="ryxz1_r6_c9" src="imge/ryxz1_r6_c9.jpg" width="19" height="305" border="0" alt=""></td>
   <td><img name="ryxz1_r6_c14" src="imge/ryxz1_r6_c14.jpg" width="19" height="1" border="0" alt=""></td>
   <td><img src="imge/spacer.gif" width="1" height="1" border="0" alt=""></td>
  </tr>
  <tr>
   <td rowspan="2"><img name="ryxz1_r7_c14" src="imge/ryxz1_r7_c14.jpg" width="19" height="310" border="0" alt=""></td>
   <td><img src="imge/spacer.gif" width="1" height="304" border="0" alt=""></td>
  </tr>
  <tr>
   <td colspan="2"><img name="ryxz1_r8_c9" src="imge/ryxz1_r8_c9.jpg" width="19" height="6" border="0" alt=""></td>
   <td><img src="imge/spacer.gif" width="1" height="6" border="0" alt=""></td>
  </tr>
  <tr>
   <td colspan="2"><img name="ryxz1_r9_c9" src="imge/ryxz1_r9_c9.jpg" width="19" height="15" border="0" alt=""></td>
   <td rowspan="2" colspan="2"><img name="ryxz1_r9_c11" src="imge/ryxz1_r9_c11.jpg" width="117" height="20" border="0" alt=""></td>
   <td><img name="ryxz1_r9_c13" src="imge/ryxz1_r9_c13.jpg" width="6" height="15" border="0" alt=""></td>
   <td><img name="ryxz1_r9_c14" src="imge/ryxz1_r9_c14.jpg" width="19" height="15" border="0" alt=""></td>
   <td><img src="imge/spacer.gif" width="1" height="15" border="0" alt=""></td>
  </tr>
  <tr>
   <td rowspan="4"><img name="ryxz1_r10_c6" src="imge/ryxz1_r10_c6.jpg" width="5" height="50" border="0" alt=""></td>
   <td><img name="ryxz1_r10_c7" src="imge/ryxz1_r10_c7.jpg" width="12" height="5" border="0" alt=""></td>
   <td rowspan="4" colspan="2"><img name="ryxz1_r10_c9" src="imge/ryxz1_r10_c9.jpg" width="19" height="50" border="0" alt=""></td>
   <td rowspan="4" colspan="2"><img name="ryxz1_r10_c13" src="imge/ryxz1_r10_c13.jpg" width="25" height="50" border="0" alt=""></td>
   <td><img src="imge/spacer.gif" width="1" height="5" border="0" alt=""></td>
  </tr>
  <tr>
   <td><img name="ryxz1_r11_c3" src="imge/ryxz1_r11_c3.jpg" width="15" height="18" border="0" alt=""></td>
   <td><img name="ryxz1_r11_c4" src="imge/ryxz1_r11_c4.jpg" width="5" height="18" border="0" alt=""></td>
   <td rowspan="3"><img name="ryxz1_r11_c5" src="imge/ryxz1_r11_c5.jpg" width="165" height="45" border="0" alt=""></td>
   <td><img name="ryxz1_r11_c7" src="imge/ryxz1_r11_c7.jpg" width="12" height="18" border="0" alt=""></td>
   <td rowspan="3"><img name="ryxz1_r11_c11" src="imge/ryxz1_r11_c11.jpg" width="75" height="45" border="0" alt=""></td>
   <td rowspan="2"><img name="ryxz1_r11_c12" src="imge/ryxz1_r11_c12.jpg" width="42" height="23" border="0" alt=""></td>
   <td rowspan="2"><img name="ryxz1_r11_c15" src="imge/ryxz1_r11_c15.jpg" width="57" height="23" border="0" alt=""></td>
   <td rowspan="3"><img name="ryxz1_r11_c16" src="imge/ryxz1_r11_c16.jpg" width="22" height="45" border="0" alt=""></td>
   <td rowspan="2"><img name="ryxz1_r11_c17" src="imge/ryxz1_r11_c17.jpg" width="44" height="23" border="0" alt=""></td>
   <td colspan="2"><img name="ryxz1_r11_c18" src="imge/ryxz1_r11_c18.jpg" width="73" height="18" border="0" alt=""></td>
   <td><img src="imge/spacer.gif" width="1" height="18" border="0" alt=""></td>
  </tr>
  <tr>
   <td rowspan="2" colspan="2"><img name="ryxz1_r12_c1" src="imge/ryxz1_r12_c1.jpg" width="23" height="27" border="0" alt=""></td>
   <td rowspan="2" colspan="2"><img name="ryxz1_r12_c3" src="imge/ryxz1_r12_c3.jpg" width="20" height="27" border="0" alt=""></td>
   <td rowspan="2"><img name="ryxz1_r12_c7" src="imge/ryxz1_r12_c7.jpg" width="12" height="27" border="0" alt=""></td>
   <td rowspan="2"><img name="ryxz1_r12_c18" src="imge/ryxz1_r12_c18.jpg" width="68" height="27" border="0" alt=""></td>
   <td rowspan="2"><img name="ryxz1_r12_c19" src="imge/ryxz1_r12_c19.jpg" width="5" height="27" border="0" alt=""></td>
   <td rowspan="2"><img name="ryxz1_r12_c20" src="imge/ryxz1_r12_c20.jpg" width="19" height="27" border="0" alt=""></td>
   <td><img src="imge/spacer.gif" width="1" height="5" border="0" alt=""></td>
  </tr>
  <tr>
   <td><img name="ryxz1_r13_c12" src="imge/ryxz1_r13_c12.jpg" width="42" height="22" border="0" alt=""></td>
   <td><img name="ryxz1_r13_c15" src="imge/ryxz1_r13_c15.jpg" width="57" height="22" border="0" alt=""></td>
   <td><img name="ryxz1_r13_c17" src="imge/ryxz1_r13_c17.jpg" width="44" height="22" border="0" alt=""></td>
   <td><img src="imge/spacer.gif" width="1" height="22" border="0" alt=""></td>
  </tr>
</table>
<!--查询框-->
<div style="position:absolute;left:401px;top:28px;width:130px">
<input type="text"  name="aa"  value=""  id="aa"  style="width:130px;height:19px;border:none" onkeydown=ifquery()>
</div>
<form name="fm1" id="fm1">
<!--查询按钮-->
<div style="position:absolute;left:545px;top:28px;width:50px;height:19px;cursor:hand" onclick="query()">
&nbsp;&nbsp;&nbsp;
</div>
<!--确定按钮-->
<div style="position:absolute;left:338px;top:437px;width:50px;height:19px;cursor:hand" onclick="okadd()">
&nbsp;&nbsp;&nbsp;&nbsp;
</div>
<!--所有按钮-->
<div style="position:absolute;left:400px;top:437px;width:90px;height:19px;cursor:hand" onclick="alladd()">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
</div>
<!--取消按钮-->
<div style="position:absolute;left:480px;top:437px;width:60px;height:19px;cursor:hand" onclick="alladd()">
&nbsp;&nbsp;&nbsp;&nbsp;
</div>
<!--含子节点-->
<div style="position:absolute;left:200px;top:31px;height:19px;font-size:14px">
<input type="hidden"  name="incc" id="incc" style="border:none;border-color:none">
</div>
<!--下一页-->
<div style="position:absolute;left:342px;top:47px;height:19px;font-size:12px">
<input type="button" id="npage" style="background-image: url(imge/ryxz1_r11_c15_1.jpg);border: 1px none;line-height: 17px;" name="npage" value="下一页" onclick="nexpage()" />
</div>
<!--页码-->
<div id="pflag" style="position:absolute;left:298px;top:52px;height:19px;font-size:12px;color:red">
</div>
<!-- 上一页-->
<div style="position:absolute;left:240px;top:47px;height:19px;font-size:14px">
<input type="button" id="ppage" style="background-image: url(imge/ryxz1_r11_c15_1.jpg);border: 1px none;line-height: 17px;" name="ppage" value="上一页" onclick="previorspage()" />
</div>
<!--个人组-->
<div style="position:absolute;left:189px;top:32px;width:10px;height:19px;">

</div>
<!--公共组-->
<div style="position:absolute;left:110px;top:32px;height:19px;font-size:14px">
<input type="radio" id="rd" name="rd" onclick="gogroup()" value="0"> 按组
</div>
<!--部门-->
<div style="position:absolute;left:31px;top:32px;height:19px;font-size:14px">
<input type="radio" id="rd" name="rd" value="1" checked onclick="init()"> 按部门
</div>
<!--预选-->
<div style="position:absolute;left:243px;overflow:true;top:96px;width:150px;height:320px;font-size:12px">

<select multiple name="onedet" id="onedet" style="scrollbar-base-color:white;scrollbar-face-color:#99CCFF;scrollbar-arrow-color: #4D6997;scrollbar-track-color:white;scrollbar-darkshadow-color:white;scrollbar-highlight-color:white;width:152;height:338" onDBLclick="addoj()">
</select>
</div>
<!--jue选-->
<div style="position:absolute;left:455px;overflow:true;top:96px;width:10px;height:320px;font-size:12px">
<select name="onedet2" id="onedet2" multiple onDBLclick="removeoj()"  style="border-width:0;border-style:none;width:135;height:338">
</select>
</div>

<!--反选择按钮-->
<div style="position:absolute;left:400px;top:165px;width:60px;height:19px;cursor:hand" onclick="remove()">
&nbsp;&nbsp;&nbsp;&nbsp;
</div>
<!--全反选择按钮-->
<div style="position:absolute;left:400px;top:205px;width:60px;height:19px;cursor:hand" onclick="removeall()">
&nbsp;&nbsp;&nbsp;&nbsp;
</div>
<!--全选择按钮-->
<div style="position:absolute;left:400px;top:245px;width:60px;height:19px;cursor:hand" onclick="addall()">
&nbsp;&nbsp;&nbsp;&nbsp;
</div>
<!--选择按钮-->
<div style="position:absolute;left:400px;top:285px;width:60px;height:19px;cursor:hand" onclick="add()">
&nbsp;&nbsp;&nbsp;&nbsp;
</div>
	<div class="extreeView" id="treeView" style="scroll:no;position:absolute;left:40px;overflow:false;top:90px;width:182px;height:355px;font-size:12px">	
	</div>
		<div class="extreeView" id="treeView2" style="scroll:no;position:absolute;left:40px;overflow:false;top:90px;width:182px;height:355px;font-size:12px">	
	</div>
	<!--提示-->
<div   style="position:absolute;left:0px;overflow:true;top:10px;width:200px;height:12px;font-size:12px;display:block">
<center>
<font color="red" id="tishi"></font>
<center>
</div>
	</form>
<SCRIPT LANGUAGE="JavaScript">
<!--
	initpara();
//-->
</SCRIPT>
<%@ include file="../sysMsg.jsp"%>
</body>

</html>


