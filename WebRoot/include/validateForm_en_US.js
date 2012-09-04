/*
 * 对页面表单进行验证的函数
 * 输入：form对象
 * 输出：true：验证通过；false：验证失败
 * 使用方法：
 * 1、在需要调用的页面中包含validateForm.js  如：<SCRIPT language="JavaScript" SRC="../../../js/common/validateForm.js"></SCRIPT>
 * 2、在需要进行校验的输入框中添加 validator="验证类型" cnname="输入框名称" maxlength="长度"
 *    例如：<input name="FAVOURABLE_FEE" type="text" size="16" validator="float10_2Null" cnname="优惠费用" maxlength="13">
 *    目前支持的类型如下：
 *        1.非空的日期型数据
 *        2.可以空的日期型数据
 *        3.非空的数字类型
 *        4.可以为空的数字类型
 *        5.非空的实数型
 *        6.可以为空的实数型
 *        7.非空的最大10位整数，2位小数的实数型
 *        8.可以为空的最大10位整数，2位小数的实数型
 *        9.非空的整型
 *        10.可以为空的整型
 *        11、非空字符串
 *        12、可以为空字符串
 *        13、非空合法的字符串：除去（$*&'=<#>%^@~ ）
 *        14、可以为空的合法字符串：除去（$*&'=<#>%^@~ ）
 *        15、不可以为空的Email
 *        16、可以为空的Email
 * 		  17、非空带时分秒的日期类型
 * 		  18、可空带时分秒的日期类型
 * 		  19、电话号码：没有限制长度，电话号码，数字和-
 * 3、在提交数据的地方添加调用 validateForm(form对象) 如：validateForm(form1)
*/

function validateForm(theForm)
{   
		//对页面表单进行验证的函数

		//定义正则表达式
		var PatternsDict=new Object();
		//1.非空的日期型数据,不带十分秒
		PatternsDict.date = /^[1-2]\d{3}-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[0-1])$/;
		
		
		//2.可以空的日期型数据
		PatternsDict.dateNull = /(^[1-2]\d{3}-(0?[1-9]|1[0-2]|3[0-1])-(0?[1-9]|[12][0-9]|3[0-1])$)|(^[1-2]\d{3}-([1-9]|1[0-2]|3[0-1])-([1-9]|[12][0-9]|3[0-1])$)|(^$)/;
		//3.非空的数字类型
		PatternsDict.num= /^\d+$/;
		//4.可以为空的数字类型
		PatternsDict.numNull = /^\d*$/;
        PatternsDict.charNull=/^([a-z]|[A-Z])*$/;
		//5.非空的实数型
		PatternsDict.float= /^(0|\-?[1-9]+[0-9]*|\-?[1-9]+[0-9]*\.\d+|\-?[0-9]*\.\d+)$/;
		//6.可以为空的实数型
		PatternsDict.floatNull = /(^(0|\-?[1-9]+[0-9]*|\-?[1-9]+[0-9]*\.\d+|\-?[0-9]*\.\d+)$)|(^$)/;
		//7.非空的最大10位整数，2位小数的实数型
		PatternsDict.float10_2= /^(0|\-?[1-9]{1}\d{0,9}|\-?[1-9]{1}\d{0,9}\.\d{1,2}|\-?\d{0,9}\.\d{1,2})$/;
		//8.可以为空的最大10位整数，2位小数的实数型
		PatternsDict.float10_2Null = /(^(0|\-?[1-9]{1}\d{0,9}|\-?[1-9]{1}\d{0,9}\.\d{1,2}|\-?\d{0,9}\.\d{1,2})$)|(^$)/;
		//9.非空的整型
		PatternsDict.int= /^(0|\-?[1-9]{1}\d*)$/;
		//10.可以为空的整型
		PatternsDict.intNull = /(^(0|\-?[1-9]{1}\d*)$)|(^$)/;
		//11、非空字符串
		PatternsDict.string = /^[\S\s]+$/;
		//12、可以为空字符串
		PatternsDict.stringNull = /^[\S\s]*$/;
		//13、非空合法的字符串：除去（$*&'=<#>%^@~ ）
		PatternsDict.stringLegal = /^[^\$\*&'=<#>%\^@~\s]+$/;
		//14、可以为空的合法字符串：除去（$*&'=<#>%^@~ ）
		PatternsDict.stringLegalNull = /(^[^\$\*&'=<#>%\^@~\s]+$)|(^$)/;
		//15、不可以为空的Email
		PatternsDict.email = /^[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+@[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+(\.[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+)+$/;
		//16、可以为空的Email
		PatternsDict.emailNull = /(^[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+@[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+(\.[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+)+$)|(^$)/;
			
		//17、非空带时分秒的日期类型
		PatternsDict.datetime = /^[1-2]\d{3}-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[0-1]) (0?[0-9]|1[0-9]|2[0-3]|):(0?[0-9]|[1-5][0-9]):(0?[0-9]|[1-5][0-9])$/;
				
		//18、可空带时分秒的日期类型
		PatternsDict.datetimeNull = /(^[1-2]\d{3}-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[0-1]) (0?[0-9]|1[0-9]|2[0-3]|):(0?[0-9]|[1-5][0-9]):(0?[0-9]|[1-5][0-9])$)|(^$)/;
		
		//19、电话号码，数字和-		
		PatternsDict.phone = /^([0-9]|[\-])*$/;
		
		//ip地址
		PatternsDict.ipaddr = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/; 
		
　　　　　　　　var elArr = theForm.elements; //elArr数组获得全部表单元素
　　　　　　　　var val;
　　　　　　　　var vname = "";
　　　　　　　　for(var i = 0; i < elArr.length; i++)
			 {
	　　　　　　　　if(elArr[i])
	　　　　　　　　{　　　　　　　　　　　　　　　//循环表单中每一个元素
	　　　　　　　　　　var v = elArr[i].validator; //获取validator属性值
	　　　　　　　　　　var cnname=elArr[i].cnname; //获取cnname属性值
			    	var maxlength=elArr[i].maxLength; //获得长度
	　　　　　　　　　　if(!v) continue; //如没有validator属性值，则跳出本次循环
	　　　　　　　　　　if(!cnname) continue;
	　　　　　　　　　　var thePat = PatternsDict[v]; //选择对应的正则表达式
	　　　　　　　　　　var gotIt = thePat.exec(elArr[i].value); //运行对比
	　　　　　　　　　　if(!gotIt) //如果符合相应的正则表达式（为true），则转为false，否则执行该if块。
	　　　　　　　　　　{
	　　　　　　　　　　　　　　if (v=="date") vname="Date(For Example: 2005-01-02)";
	　　　　　　　　　　　　　　if (v=="dateNull") vname="Date(For Example: 2005-01-02), Can Be Null";
								if (v=="charNull") vname="Only Letter, Can Not Be Null";
	　　　　　　　　　　　　　　if (v=="num") vname="Only Number, Can Not Be Null";
							    if (v=="numNull") vname="Number";
	　　　　　　　　　　　　　　if (v=="float") vname="Only Float, Can Not Be Null";
								if (v=="floatNull") vname="Float";
	　　　　　　　　　　　　　　if (v=="float10_2") vname="Only Float, Can Not Be Null, MaxLength Is Ten And MinLength Is Two";
								if (v=="float10_2Null") vname="Only Float, MaxLength Is Ten And MinLength Is Two";
	　　　　　　　　　　　　　　if (v=="int") vname="Only Integer, Can Not Be Null";
								if (v=="intNull") vname="Integer";
	　　　　　　　　　　　　　　if (v=="email") vname="Email Can Not Be Null";
						if (v=="emailNull") vname="Email";
						if (v=="string") vname="Can Not Be Null";
						 if (v=="stringLegal") vname="Only Character, Can Not Be Null, Except Like: 『$*&'=<#>%^@~ 』";
						 if (v=="stringLegalNull") vname="Only Character,Except Like: 『$*&'=<#>%^@~ 』";
						 if(v == "datetime") vname="Date(For Example: 2005-01-02)";
						 if(v == "datetimeNull") vname="Date(For Example: 2005-01-02), Can Be Null";
						 if(v == "phone") vname="Phone Number";
						 if(v == "ipaddr") vname = "IP Address(For Example: 92.168.1.1)";
	　　　　　　　　　　}
			    	else 
			    	{
			    	    if(maxlength){
				    	    //这里主要针对textarea类型没有maxlength属性而设置的
				    	    if (elArr[i].value.length>maxlength) {
				    	    	vname="The Length Less Then【 "+maxlength+" 】Character";
				    	    }
			    	    }
			    	}
				}
			
			    if (vname != "")
			    {
			    	    //有错误，阻止提交
			    	try
				    {
	　　　　　　　　　　　　　　	elArr[i].focus(); //此元素获得焦点
				    	if (elArr[i].type=="text" ) elArr[i].select();
				    } 
				    catch(e) {
				    }
				    
　　　　　　　　　　　　　　$.dialog.alert("Please Input According To Rule Wtich “" + cnname + "”！The Data Should Be：" + vname );
　　　　　　　　　　　　　　return false; //阻止提交
			    }
　　　　		}
　　　　　　　return true; 
}

//===============================================================================================

function validateInput(theInput)
　　　　　　{   //对页面表单进行验证的函数
	
		//定义正则表达式
		var PatternsDict=new Object();
		//1.非空的日期型数据
		PatternsDict.date = /^[1-2]\d{3}-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[0-1])$/;
		//2.可以空的日期型数据
		PatternsDict.dateNull = /(^[1-2]\d{3}-(0?[1-9]|1[0-2]|3[0-1])-(0?[1-9]|[12][0-9]|3[0-1])$)|(^[1-2]\d{3}-([1-9]|1[0-2]|3[0-1])-([1-9]|[12][0-9]|3[0-1])$)|(^$)/;
		//3.非空的数字类型
		PatternsDict.num= /^\d+$/;
		//4.可以为空的数字类型
		PatternsDict.numNull = /^\d*$/;
		//5.非空的实数型
		PatternsDict.float= /^(0|\-?[1-9]+[0-9]*|\-?[1-9]+[0-9]*\.\d+|\-?[0-9]*\.\d+)$/;
		//6.可以为空的实数型
		PatternsDict.floatNull = /(^(0|\-?[1-9]+[0-9]*|\-?[1-9]+[0-9]*\.\d+|\-?[0-9]*\.\d+)$)|(^$)/;
		//7.非空的最大10位整数，2位小数的实数型
		PatternsDict.float10_2= /^(0|\-?[1-9]{1}\d{0,9}|\-?[1-9]{1}\d{0,9}\.\d{1,2}|\-?\d{0,9}\.\d{1,2})$/;
		//8.可以为空的最大10位整数，2位小数的实数型
		PatternsDict.float10_2Null = /(^(0|\-?[1-9]{1}\d{0,9}|\-?[1-9]{1}\d{0,9}\.\d{1,2}|\-?\d{0,9}\.\d{1,2})$)|(^$)/;
		//9.非空的整型
		PatternsDict.int= /^(0|\-?[1-9]{1}\d*)$/;
		//10.可以为空的整型
		PatternsDict.intNull = /(^(0|\-?[1-9]{1}\d*)$)|(^$)/;
		//11、非空字符串
		PatternsDict.string = /^[\S\s]+$/;
		//12、可以为空字符串
		PatternsDict.stringNull = /^[\S\s]*$/;
		//13、非空合法的字符串：除去（$*&'=<#>%^@~ ）
		PatternsDict.stringLegal = /^[^\$\*&'=<#>%\^@~\s]+$/;
		//14、可以为空的合法字符串：除去（$*&'=<#>%^@~ ）
		PatternsDict.stringLegalNull = /(^[^\$\*&'=<#>%\^@~\s]+$)|(^$)/;
		//15、不可以为空的Email
		PatternsDict.email = /^[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+@[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+(\.[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+)+$/;
		//16、可以为空的Email
		PatternsDict.emailNull = /(^[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+@[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+(\.[\-!#\$%&'\*\+\\\.\/0-9=\?A-Z\^_`a-z{|}~]+)+$)|(^$)/;
		
		//17、非空带时分秒的日期类型
		PatternsDict.datetime = /^[1-2]\d{3}-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[0-1]) (0?[0-9]|1[0-9]|2[0-3]|):(0?[0-9]|[1-5][0-9]):(0?[0-9]|[1-5][0-9])$/;
		
		//18、可空带时分秒的日期类型
		PatternsDict.datetimeNull = /(^[1-2]\d{3}-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[0-1]) (0?[0-9]|1[0-9]|2[0-3]|):(0?[0-9]|[1-5][0-9]):(0?[0-9]|[1-5][0-9])$)|(^$)/;
		
		//19、电话号码，数字和-		
		PatternsDict.phone = /^([0-9]|[\-])+$/;
		
		//ip地址
		PatternsDict.ipaddr = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/; 
		
　　　　　　　　var elArr = theInput; //elArr数组获得输入框元素
　　　　　　　　var val;
　　　　　　　　var vname;

		if (elArr!=null)
　　　　　　　　{　　　　　　　　　　　　　　　//循环表单中每一个元素
　　　　　　　　　　var v = elArr.validator; //获取validator属性值
　　　　　　　　　　var cnname=elArr.cnname; //获取cnname属性值
　　　　　　　　　　if(!v) return true; //如没有validator属性值，则跳出本次循环
　　　　　　　　　　if(!cnname) return true;
　　　　　　　　　　var thePat = PatternsDict[v]; //选择对应的正则表达式
　　　　　　　　　　var gotIt = thePat.exec(elArr.value); //运行对比
			//alert(elArr.value);
　　　　　　　　　　if(!gotIt) //如果符合相应的正则表达式（为true），则转为false，否则执行该if块。
　　　　　　　　　　{
　　　　　　　　　　　　　　if (v=="date") vname="Date(For Example: 2005-01-02)";
　　　　　　　　　　　　　　if (v=="dateNull") vname="Date(For Example: 2005-01-02), Can Be Null";
　　　　　　　　　　　　　　if (v=="num") vname="Only Number, Can Not Be Null";
			    if (v=="numNull") vname="Number";
　　　　　　　　　　　　　　if (v=="float") vname="Only Float, Can Not Be Null";
			    if (v=="floatNull") vname="Float";
　　　　　　　　　　　　　　if (v=="float10_2") vname="Only Float, Can Not Be Null, MaxLength Is Ten And MinLength Is Two";
			    if (v=="float10_2Null") vname="Only Float, MaxLength Is Ten And MinLength Is Two";
　　　　　　　　　　　　　　if (v=="int") vname="Only Integer, Can Not Be Null";
			    if (v=="intNull") vname="Integer";
　　　　　　　　　　　　　　if (v=="email") vname="Email Can Not Be Null";
			    if (v=="emailNull") vname="Eamil";
			    if (v=="string") vname="Can Not Be Null";
			    if (v=="stringLegal") vname="Only Character, Can Not Be Null, Except Like: 『$*&'=<#>%^@~ 』";
			    if (v=="stringLegalNull") vname="Only Character,Except Like: 『$*&'=<#>%^@~ 』";
			     if(v == "datetime") vname="Date(For Example: 2005-01-02)";
						 if(v == "datetimeNull") vname="Date(For Example: 2005-01-02), Can Be Null";
　　　　　　　　　　　　　　elArr.focus(); //此元素获得焦点
			    if (elArr.type=="text" ) elArr.select();
　　　　　　　　　　　　　　$.dialog.alert("Please Input According To Rule Wtich “" + cnname + "”！The Data Should Be：" + vname );
　　　　　　　　　　　　　　return false; //阻止提交
　　　　　　　　　　}
				if(v == "phone") vname="Phone Number";
				if(v == "ipaddr") vname="IP Address(For Example: 92.168.1.1)";
　　　　　　　　}
　　　　　　　　return true; 
}