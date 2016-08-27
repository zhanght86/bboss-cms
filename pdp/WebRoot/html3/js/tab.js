function tabAction(parNode,parCon){
	var tabs = document.getElementById(parNode).getElementsByTagName("span");
	var tabLength = tabs.length;
	var tabCon = document.getElementById(parCon).getElementsByTagName("ul");
	var tabConLength = tabCon.length;
	//初始化tab和tab content
	function initTab(){
		for(var i=0;i<tabLength;i++){
			tabs[i].className = "u_tab";
		}
	}
	function initTabCon(){
		for(var i=0;i<tabConLength;i++){
			tabCon[i].style.display = "none";
		}
	}
	for(var i=0;i<tabLength;i++){
		tabs[i].theI = i;
		tabs[i].onclick = function(){
			var theI = this.theI;
			//被点击的tab显示为选中状态，同时显示相应的content
			initTab();
			initTabCon();
			this.className = "u_tab_hover";
			tabCon[theI].style.display = "block";
			//判断被点击tab的前一个tab,在非IE中可能为文本节点
			if(this.previousSibling){
				var preObj = this.previousSibling;
				if(this.previousSibling.nodeType==1){
					preObj.className = "u_tab_no";
				}
				else if(this.previousSibling.nodeType==3 && this.previousSibling.previousSibling){
					preObj = this.previousSibling.previousSibling;
					preObj.className = "u_tab_no";
				}
			}
			//去掉锚点的虚线框
			this.childNodes[0].blur();
		}
	}
}
