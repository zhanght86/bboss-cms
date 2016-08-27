function trim(val) {
	var str = val+"";
	if (str.length == 0) return str;
	var re = /^\s*/;
	str = str.replace(re,'');
	re = /\s*$/;
	return str.replace(re,'');
}

function checknumber(data) {
	var tmp ;
	if (data == "") return true;
	var re = /^[\-\+]?([0-9]\d*|0|[1-9]\d{0,2}(,\d{3})*)(\.\d+)?$/;   // By Dennis set 04 OK!
	if (re.test(data)) {
		return true;
	}
	return false;
}

function checknumber_null(data) {
	if (trim(data)=="") {
   	  return false;
	}
	return true;
}

function checkint_null(data) {
	if (trim(data)=="") {
 	  return false;
	}
	return true;
}

function checkstring_null(data) {
	if (trim(data)=="") {
	  return false;
	}
	return true;
}
function checkselece_null(data) {
	if(trim(data)=="") {
	   return false;
	}
	return true;
}

function checkint(data) {
	if (data == "") return true;
	var re = /^[\-\+]?([1-9]\d*|0|[1-9]\d{0,2}(,\d{3})*)$/;
	if (re.test(data))
		return true;
	return false;
}

function checkstring(str,maxlen,lbl) {
	if (str.length > maxlen) {
			alert(lbl+":"+ERR_STRING_LONG+maxlen);
		return false;
	}
	return true;
}

function checkday(day,month,year) {
    err = false;
    if (!checkint(year) || (year < 1900)) {
        alert("year");
        return false;
    }
    if (!checkint(month) || (month < 1) || (month > 12)) {
        return false;
    }
    if (!checkint(day) || (day < 1) || (day > 31)) {
        return false;
    }

	switch (parseInt(month)) {
		case 2:
			high =28;
			if ((year % 4 == 0) && (year % 100 != 0))
				{high =29;}
			else if (year % 400 == 0) {high=29;}
			break;
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			high =31;
			break;
		default:
			high =30;
	}
	if ((day < 1) || (day > high)) {
		errorDay(lbl);
		return false;
	}
	return true;
}

function checkemail(umail,lbl) {
	umail=trim(umail);
	if (umail.length == 0) return true;
	var re=/^[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+@[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+(\.[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+)+$/;
	if (re.test(umail))
		return true;
	return false;
}

function checktime(ctime,lbl) {
	if (ctime.length == 0) return true;

	var re=/^(([0-9]|[01][0-9]|2[0-3])(:([0-9]|[0-5][0-9])){0,2}|(0?[0-9]|1[0-1])(:([0-9]|[0-5][0-9])){0,2}\s?[aApP][mM])?$/;
	return re.test(ctime);
}

function checkdate2(bdate) {
	if (bdate.length == 0) return true;
	var re = /^(0?[1-9]|11|12|10)\/([1-2]?[0-9]|0[1-9]|30|31)\/([1-2]\d{3})$/;
	return re.test(bdate);
}

function checkdate(data) {
  var str = data;
  var year,month,day;
  if (str.indexOf("-")>-1)
    year = str.substr(0,str.indexOf("-"));
  else
	return false;
  str = str.substr(str.indexOf("-")+1);
  if (str.indexOf("-")>-1)
    month = str.substr(0,str.indexOf("-"));
  else
	return false;
  str = str.substr(str.indexOf("-")+1);
  day = str.substr(0);

  if (checkday(day,month,year))
	return true;
  else
	return false;
}

function isLegal(Obj) {//判断是否含有非法字符
    var strRef = "$*&'=<#>%^@~ ";
    var flag="legal"
    for (i=0;i<Obj.length;i++) {
        var tempChar= Obj.substring(i,i+1);
        if (strRef.indexOf(tempChar,0)!=-1) {
            flag="illegal"
            break;
        }
        else {
            flag="legal"
        }
    }
    if(flag=="illegal") {
        return false;
    }
    else {
       return true;
    }
}

/*字符串分割成数组*/
function dataDivision(str,delim) {
   var arrayLength = getSize(str,delim);
   var myArray = new Array(arrayLength);
   if(str.indexOf(delim)>-1) {
       for(var i=0;i<myArray.length;i++) {
           myArray[i] = str.substring(0,str.indexOf(delim));
           str = str.substring(str.indexOf(delim) + 1);
       }
   }
   else {
       myArray[0] = str;
   }
   return myArray;
}
/*获得字符串分割的总数*/
function getSize(str,delim) {
    var count = 0;
    while(str.indexOf(delim)>-1) {
        str = str.substring(str.indexOf(delim) + 1);
        count++;
    }
    if(str!="" && count==0) count = 1;
    return count;
}
/*将多个元素的内容组合成一个字符串*/
function compages(controlName) {
    var tempStr = "";
    var elements = document.all[controlName];
    if(elements.length>0) {
        for(var i=0;i<elements.length;i++) {
            tempStr = tempStr + elements[i].value + ":";
        }
    }
    else {
        tempStr = elements.value;
    }
    return tempStr;
}
