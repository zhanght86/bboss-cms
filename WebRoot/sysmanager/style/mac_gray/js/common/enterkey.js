function enterkey(){
	//实现用回车键移动焦点的函数
	//alert("www");
	try
	{
		var el_keydown=window.event.srcElement;
		var len;
		//仅仅拦截回车动作
		if (event.keyCode!=13) return;
		//如果焦点在按钮上，直接产生点击动作
		if (el_keydown.type=="button" ) return;
		len=document.forms(0).length;
		for (i=0;i<len;i++)
			{
			if (document.forms(0).elements(i)==el_keydown)
				{
				//跳过hidden、disabled类型的输入框
				do
				{
					i += 1;
				}
				//while (document.forms(0).elements(i).disabled||document.forms(0).elements(i).type=='hidden'&&i<len);
				while ((document.forms(0).elements(i).disabled||document.forms(0).elements(i).type=='hidden'||document.forms(0).elements(i).readOnly)&&i<len);
				document.forms(0).elements(i).focus();
				document.forms(0).elements(i).select();
				event.returnvalue=false; //抛弃多余的回车，否则进入textarea、button会直接送出回车！
				return;
				}
			}
	}
	catch(e)
	{return;}
}