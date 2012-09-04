function trimString(str){
    str = this!=window?this:str;
    return str.replace(/^\s+/g,'').replace(/\s+$/g,'');
}

String.prototype.trim = trimString;

function NumberToMoneyStr(aNumber,aFixLen,aDigital) //return 1000500 to 1,000,500
{
	var lblankStr = "                                               ";
	var lstr = "" + aNumber;
	var lFixLen = aFixLen == null ? 1 : ( aFixLen > 20 ? 20 : aFixLen)
	var ldigital = aDigital == null ? 3 : aDigital	

	var lbase = lstr.length % ldigital;
	var ldstr = (lbase >0) ? lstr.substr( 0, lbase) : "";	
	var lneed = true;
	if(lbase == 0 || (lbase ==1 && ldstr == "-"))
	{
		lneed = false;
	}
	
	for(var i = lbase; i < lstr.length; i +=ldigital)
	{
		
		ldstr += (lneed ? "," : "") + lstr.substr(i,ldigital);
		lneed = true;
	}
	
	if (ldstr.length < lFixLen)
	{
		ldstr = lblankStr.substr(0,lFixLen - ldstr.length) + ldstr;
	}
	return ldstr;	 
}
function MoneyStrToNumber(aStr,aDigital) //1,000,500 to 1000500
{
	if(aStr == null || aStr == "") return 0;
	var lstr = aStr.trim()	
	if(lstr == "" ) return 0;
	lstr = aStr.replace(/,/g,'')
	
	var lint = parseInt(lstr);
	if(isNaN(lint)) lint = -1;
	return lint;
}


function DateToString(aDate) //return aStr like 2001-03-05
{

	if(aDate == null || aDate.constructor != Date) return "";
//	aDate instanceof Date
	try{
		var ys = aDate.getFullYear();
		var ms = aDate.getMonth() + 1;
		var ds = aDate.getDate();
		
		var s = "" + ys + "-";
		
		if(ms<10) s += "0";
		s += ms +"-"
		
		if(ds<10) s +="0";
		s +=ds
		
		return s
	
	}catch(e){
		return "";
	}

}


function StringToDate(aStr) //aStr like 2001-03-05, return a DateObj
{
	var y = aStr.indexOf("-")
	if( y <=0 ) return null;
	var ys = parseInt(aStr.substr(0,y), 10);
	
	var m =  aStr.indexOf("-", y + 1)
	if( m <= y + 1 ) return null;
	var ms = parseInt(aStr.substr(y+1, m - y -1), 10);
	
	var ds = parseInt(aStr.substr(m + 1) , 10);

	if( isNaN(ys) || isNaN(ms) || isNaN(ds) ) 	return null;

	if( ys < 1970 || ys > 9999 ||
	    ms < 1    || ms > 12   ||
	    ds < 1    || ds > 31      ) return null	
	    
	if( ds > 28 ){
		if(ds == 31 )
		{
			if( ms == 2 || ms == 4 || ms ==6 || ms == 9 || ms == 11 ) return null;
		}
		else if( ds == 30 ){
			if( ms == 2 ) return null;
		}else {
			if( ms == 2 ){
				if( ys % 4 == 0 ) {
					if( ys % 100 == 0 && ys % 400 != 0){
						return null;
					}
					//yes...
				}else{
					return null;
				}
				
			}
		}
		
	}
	
	
	return new Date(ys, ms - 1, ds);
	
}
function StrToDateStr(aStr) //return "" or like 2001-03-05
{
	if(aStr == null || aStr == "") return "";
	
	var y = aStr.indexOf("-")
	if( y <=0 ) return "";
	var ys = parseInt(aStr.substr(0,y), 10);
	
	var m =  aStr.indexOf("-", y + 1)
	if( m <= y + 1 ) return "";
	var ms = parseInt(aStr.substr( y + 1, m - y - 1), 10);
	
	var ds = parseInt(aStr.substr(m + 1), 10);

	if( isNaN(ys) || isNaN(ms) || isNaN(ds) ) 	return "";

	if( ys < 1970 || ys > 9999 ||
	    ms < 1    || ms > 12   ||
	    ds < 1    || ds > 31      ) return ""	
	    
	if( ds > 28 ){
		if(ds == 31 )
		{
			if( ms == 2 || ms == 4 || ms ==6 || ms == 9 || ms == 11 ) return "";
		}
		else if( ds == 30 ){
			if( ms == 2 ) return "";
		}else {
			if( ms == 2 ){
				if( ys % 4 == 0 ) {
					if( ys % 100 == 0 && ys % 400 != 0){
						return "";
					}
					//yes...
				}else{
					return "";
				}
				
			}
		}
		
	}
	
	var s = "" + ys + "-";
	if(ms<10) s += "0";
	s +=ms +"-"
	if(ds<10) s +="0";
	s +=ds
	//alert(s);
	return s
	 
}

function StringIsDate(aStr) //is aStr like 2001-03-05, return true of false;
{
	if(aStr == null || aStr == "") return false;
	
	var y = aStr.indexOf("-")
	if( y <=0 ) return false;
	var ys = parseInt(aStr.substr(0,y), 10);
	
	var m =  aStr.indexOf("-", y + 1)
	if( m <= y + 1 ) return false;
	var ms = parseInt(aStr.substr(y+1, m - y - 1), 10);
	
	var ds = parseInt(aStr.substr(m + 1), 10);

	if( isNaN(ys) || isNaN(ms) || isNaN(ds) ) 	return false;

	if( ys < 1970 || ys > 9999 ||
	    ms < 1    || ms > 12   ||
	    ds < 1    || ds > 31   ) 
	    return false	
	    
	if( ds > 28 ){
		if(ds == 31 )
		{
			if( ms == 2 || ms == 4 || ms ==6 || ms == 9 || ms == 11 ) return false;
		}
		else if( ds == 30 ){
			if( ms == 2 ) return false;
		}else {
			if( ms == 2 ){
				if( ys % 4 == 0 ) {
					if( ys % 100 == 0 && ys % 400 != 0){
						return false;
					}
					//yes...
				}else{
					return false;
				}
				
			}
		}
		
	}
	
	return true;
	 
}
function link3(url,w,h)
{
	window.open(url, '', 'toolbar=0, scrollbars=0, resizable=1, width='+w+', height='+h+', top='+(screen.availHeight/2-h/2)+', left='+(screen.availWidth/2-w/2)+' ');
}

function link2(url,w,h)
{
	window.open(url, '', 'toolbar=0, scrollbars=1, resizable=1, width='+w+', height='+h+', top='+(screen.availHeight/2-h/2)+', left='+(screen.availWidth/2-w/2)+' ');
}

function link(url,w,h)
{
	window.open(url, '', 'toolbar=0, scrollbars=0, resizable=0, width='+w+', height='+h+', top='+(screen.availHeight/2-h/2)+', left='+(screen.availWidth/2-w/2)+' ');
}