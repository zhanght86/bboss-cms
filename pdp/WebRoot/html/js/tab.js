function setTab(m,n){
 var lit = document.getElementById("menu"+m).getElementsByTagName("a");
 var lim = document.getElementById("main"+m).getElementsByTagName("ul");
 for (i=0;i<lit.length;i++){
 lit[i].className=i==n?"current":"";
 lim[i].style.display=i==n?"block":"none";
 }
}