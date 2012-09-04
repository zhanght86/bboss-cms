
var  commitevent ;

 
/**
*url:页面地址
*pages:当前页面总数
*maxPageItem:每页显示的最大记录数
*hasParam:判断当前页地址是否带参数  
*/
function goTo(url,
              pages,
              maxPageItem,
              hasParam, 
              sortKey,desc,id,
              formName,
              params,
              promotion)
{
	
	var regx = /\d+/g;
	var gotopage = document.all.item("gotopage");
	
    var goPage = "NaN";
    if(gotopage.length)
    {
	    for(var i = 0; i < gotopage.length; i ++ )
	    {
	    	//var temp = parseInt(gotopage[i].value);
	    	//if(!isNaN(temp))
	    	var temp = gotopage[i].value;
	    	if(regx.test(temp))
	        	goPage = temp;

	    }
	}
	else
	{
		//var temp = parseInt(gotopage.value);
		var temp = gotopage.value;
    	//if(!isNaN(temp))
	    	if(regx.test(temp))
	    	{
	        	goPage = temp;
		}

	}

    if(isNaN(goPage) || goPage.indexOf(".") != -1)
    {
		alert("请输入跳转页或者跳转页必须为整数");
		return;
    }
    //如果跳转页比总页数大时，将跳转页指定为pages
    
    if(pages < goPage)
    {
    	goPage = pages;
    }
    
    var offset = (goPage - 1) * maxPageItem;
    var offset_key = "";
    var desc_key = "";
    var sort_key = "";
    if(id)
    {
        offset_key = id + ".offset";
        desc_key = id + ".desc"
        sort_key = id + ".sortKey";
    }
    else
    {
        offset_key = "pager.offset";
        desc_key = "desc";
        sort_key = "sortKey";
    }
    var forwardurl = url + (hasParam?"&":"?") + offset_key + "=" + offset;
    
    if(sortKey)
        forwardurl = forwardurl + "&" + sort_key + "=" + sortKey  + "&" + desc_key + "=" + desc;
    //params = (hasParam?"&":"") + offset_key + "=" + offset + "&"+ sort_key + "=" + sortKey  + "&" + desc_key + "=" + desc;
    params += (hasParam?"&":"") + offset_key + "=" + offset + "&"+ sort_key + "=" + sortKey  + "&" + desc_key + "=" + desc;
    
	var flag = false;
    if(commitevent){
        flag = eval(commitevent);
    }
	
    if(formName && flag)
        pageSubmit(formName,params,forwardurl,promotion,id);
    else
	{
		
		location.replace(forwardurl);
	}

}

 /**
 * 实现分页标签中翻页按钮提交表单功能
 * formName:表单名称
 * params:页面参数
 * forwardurl:跳转页面
 * promotion:是否提示信息
 */
function pageSubmit(formName,params,forwardurl,promotion,id)
{
	
    //如果需要提示是否保存页面数据的修改，则弹出提示窗口，
    var paramsName = "PAGE_QUERY_STRING";
    if(id)
        paramsName = id + ".PAGE_QUERY_STRING";
	var flag = false;
    if(commitevent){
        flag = eval(commitevent);
    }
    if(promotion && flag)
    {
    	
        if(!confirm("是否保存当前页面修改的数据？\r\n是，点击'确定'；否，点击'取消'."))
		{		
			
            location.replace(forwardurl);            
            //document.all.item(paramsName).value = params;
			//if(document.all.sendOrgId){
			//    document.all.sendOrgId.value = "";
			//}
            //document.forms(formName).submit();
		}
        else
        {   
			if(commitevent){
				try
				{
					eval(commitevent);

				}
				catch(e)
				{
					alert("Error Command:\n\""+commitevent+"\".\n"+e.description);
				}
				
			}
            document.all.item(paramsName).value = params;
            document.forms(formName).submit();
        }
    }
    else
    {		
        document.all.item(paramsName).value = params;
        document.forms(formName).submit();
    }
}
function keydowngo(goid)
{
	if(window.event.keyCode == 13)
	{
		document.getElementById(goid).onclick();
	}
	
	return false;
}