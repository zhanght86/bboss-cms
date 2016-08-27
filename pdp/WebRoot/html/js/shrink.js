// JavaScript Document
function obj(menu)
{
return (navigator.appName == "Microsoft Internet Explorer")?this[menu]:document.getElementById(menu);
}
function togglevisible(treepart)
{
if (this.obj("T"+treepart).style.display == "none")
{
this.obj("T"+treepart).style.position="";
this.obj("T"+treepart).style.display="block";
document["I"+treepart].src="images/shrink.gif";
}
else
{
this.obj("T"+treepart).style.position="absolute";
this.obj("T"+treepart).style.display="none";
document["I"+treepart].src="images/expand.gif";
}
}
