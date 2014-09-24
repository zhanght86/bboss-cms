/**选项卡tab***/
function setTab(m,n){
 var lit = document.getElementById("menu"+m).getElementsByTagName("a")
 var lim = document.getElementById("main"+m).getElementsByTagName("ul");
 document.getElementById("main"+m).style.display="block"
 for (i=0;i<lit.length-2;i++){
	   lit[i].className=i==n?"current":"spread";
	   lim[i].style.display=i==n?"block":"none";
	
 }
}

function setTabC(m,n){
 var lit = document.getElementById("menu"+m).getElementsByTagName("a")
 var limparent=document.getElementById("main"+m);
 var lim = document.getElementById("main"+m).getElementsByTagName("ul");
 document.getElementById("main"+m).style.display="block";
 var lima=new Array(); 
 for(j=0;j<lim.length;j++)
 {
	 if(lim[j].parentNode==limparent)
	 {
		var a=lima.push(lim[j]);
	 }  
 }
 for(i=0;i<a;i++){
		 lit[i].className=i==n?"current":"spread";
		 lima[i].style.display=i==n?"block":"none";
   } 
 }







 
 
