
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
    params = (hasParam?"&":"") + offset_key + "=" + offset + "&"+ sort_key + "=" + sortKey  + "&" + desc_key + "=" + desc;
    if(formName)
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
    if(promotion)
    {
        if(!confirm("是否保存已修改的数据？如果想保存则点击确定按钮\r\n否则点击取消,页面数据修改都将丢失."))
		{
            location.replace(forwardurl);
		}
        else
        {

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
