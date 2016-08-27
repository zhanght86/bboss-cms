function StickPad(){
	var ctRg = document.body.createControlRange();
	
	this.addItem=function(tb){
		ctRg.addElement(tb);
	}
	this.copy=function (){
		ctRg.select();
		ctRg.execCommand("Copy");
		document.selection.empty();
		return true;
	}
	this.clearAll=function(){
		while(ctRg.length>0){
			ctRg.remove(ctRg.length-1);
		}
	}
}