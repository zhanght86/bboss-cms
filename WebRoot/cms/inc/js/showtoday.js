
var day="";
var month="";
var ampm="";
var ampmhour="";
var myweekday="";
var year="";
mydate=new Date();
myweekday=mydate.getDay();
mymonth=mydate.getMonth()+1;
myday= mydate.getDate();
myyear= mydate.getYear();
year=(myyear > 200) ? myyear : 1900 + myyear;
if(myweekday == 0)
weekday="日";
else if(myweekday == 1)
weekday="一";
else if(myweekday == 2)
weekday="二";
else if(myweekday == 3)
weekday="三";
else if(myweekday == 4)
weekday="四";
else if(myweekday == 5)
weekday="五";
else if(myweekday == 6)
weekday="六";
document.write("<font color=red>欢迎您！</font><font color=black> 今天是:</font> "+year+"年"+mymonth+"月"+myday+"日 周"+weekday);
