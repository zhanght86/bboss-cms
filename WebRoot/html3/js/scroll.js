// JavaScript Document
try{ 
	var isStoped = false; 
	var oScroll = document.getElementById("scrollWrap"); 
	with(oScroll){ 
		noWrap = true; 
	} 

	oScroll.onmouseover = new Function('isStoped = true'); 
	oScroll.onmouseout = new Function('isStoped = false'); 

	var preTop = 0; 
	var curTop = 0; 
	var stopTime = 0; 
	var oScrollMsg = document.getElementById("scrollMsg");

	oScroll.appendChild(oScrollMsg.cloneNode(true)); 
	init_srolltext();
	
}catch(e){} 

function init_srolltext(){ 
	oScroll.scrollTop = 0; 
	setInterval('scrollUp()', 23); 
}

function scrollUp(){ 
	if(isStoped)
	return; 
	curTop += 1; 
	if(curTop == 27){ 
		stopTime += 1; 
		curTop -= 1; 
		if(stopTime == 180){ 
			curTop = 0; 
			stopTime = 0; 
		} 
	}else{ 
		preTop = oScroll.scrollTop; 
		oScroll.scrollTop += 1; 
		if(preTop == oScroll.scrollTop){ 
			oScroll.scrollTop = 0; 
			oScroll.scrollTop += 1; 
		} 
	} 
}