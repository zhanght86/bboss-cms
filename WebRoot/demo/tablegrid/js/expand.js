var width = screen.width;
var height = screen.height;
var container = document.getElementById("tablediv");
if(width<=1024){container.style.height = "232px";}
if(width>1024&&width<=1440){container.style.height = "342px"; }
if(width>1440){container.style.height = "462px";}



/*展开/收缩*/
function obj(menu)
{
return (navigator.appName == "Microsoft Internet Explorer")?this[menu]:document.getElementById(menu);
}
function togglevisible(treepart)
{
if (this.obj("T"+treepart).style.visibility == "hidden")
{
var change = document.getElementById("change");
change.style.marginLeft='210px';
this.obj("T"+treepart).style.position="";
this.obj("T"+treepart).style.visibility="";
document["I"+treepart].src="images/expand.gif";


}
else
{
var change = document.getElementById("change");
change.style.marginLeft='15px';

this.obj("T"+treepart).style.position="absolute";
this.obj("T"+treepart).style.visibility="hidden";
document["I"+treepart].src="images/shrink.gif";
}
}
/*全选/全不选*/
function checkAll(e, itemName) 
{ 
var aa = document.getElementsByName(itemName); 
for (var i=0; i<aa.length; i++) 
aa[i].checked = e.checked; 
} 
function checkItem(e, allName) 
{ 
var all = document.getElementsByName(allName)[0]; 
if(!e.checked) all.checked = false; 
else 
{ 
var aa = document.getElementsByName(e.name); 
for (var i=0; i<aa.length; i++) 
if(!aa[i].checked) return; 
all.checked = true; 
} 
} 
