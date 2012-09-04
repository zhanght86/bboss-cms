/**
 * 通用全选函数
 */
function checkAll(formName,elementID,checkBox) {
	var e = document.forms(formName).item(elementID);

	if(e)
	{
		if(e.length)
		{
			for(i = 0; i < e.length; i ++)
				e[i].checked = checkBox.checked;
		}
		else
		{
			e.checked = checkBox.checked;
		}
	}
}

/**
 * 根据单个复选框的状态来设置全选框的选中状态
 */
function checkboxevent(formName, checkbox, checkAllBox)
{
	var element = document.forms(formName).item(checkbox);
	var allCheckBox = document.forms(formName).item(checkAllBox);
	if(element)
	{
		if(element.length)
		{
			for(i = 0; i < element.length; i ++)
			{
				if(!element[i].checked)
				{
					allCheckBox.checked = element[i].checked;
					return;
				}

			}
			allCheckBox.checked = true;
		}
		else
			allCheckBox.checked = element.checked;

	}
}
/**
 * 修改会议通知状态
 */

function changeStatus(formName,meeting_id,modifyType,isStop,isHidden,meetingStatus,queryString)
{
	//修改发布状态
	if(modifyType == "meetingStatus" && confirm("确定要发布该会议通知吗？"))
	{
		document.forms(formName).statusType.value = modifyType;
		document.forms(formName).returnLink.value = queryString;
		document.forms(formName).status.value = 1;
		document.forms(formName).meeting_id.value = meeting_id;
		document.forms(formName).submit();

	}
	//修改隐藏状态
	if(modifyType == "isHidden" && confirm("确定要"+ (isHidden == 0?"显示":"隐藏") +"该会议通知吗？"))
	{

		document.forms(formName).statusType.value = modifyType;
		document.forms(formName).returnLink.value = queryString;
		document.forms(formName).meeting_id.value = meeting_id;
		if(isHidden == 0)
			document.forms(formName).status.value = 1;
		else
			document.forms(formName).status.value = 0;
		document.forms(formName).submit();

	}
	//修改报名截止状态
	if(modifyType == "isStop" && confirm("确定要"+ (isStop == 0?"开通":"截止") +"该会议通知吗？"))
	{
		document.forms(formName).statusType.value = modifyType;
		document.forms(formName).returnLink.value = queryString;
		document.forms(formName).meeting_id.value = meeting_id;
		if(isStop == 0)
			document.forms(formName).status.value = 1;
		else
			document.forms(formName).status.value = 0;
		document.forms(formName).submit();

	}
}

/**
 * 删除记录通用函数
 */
function deleteRecord(formName,deleteID)
{
	var element = document.forms(formName).item(deleteID);
	if(element)
	{
		if(element.length)
		{
			for(i = 0; i < element.length; i ++)
				if(element[i].checked)
				{
					//\r\n如果删除，将删除所有与会议通知相关的信息包括：\r\n会议原文，会议补充通知，会议报名信息！！
					if(confirm("你确定要删除选中的用户吗？"))
						document.forms(formName).submit();
					return;
				}
			alert("请选则要删除的用户");
			return;
		}
		else
		{
			if(!element.checked)
			{
				alert("请选则要删除的用户");
				return;
			}
			else
			{
				//\r\n如果删除，将删除所有与会议通知相关的信息包括：\r\n会议原文，会议补充通知，会议报名信息！！
				if(confirm("你确定要删除选中的用户吗？"))
					document.forms(formName).submit();
				return ;
			}
		}
	}
	else
	{
		alert("没有要删除的用户");
		return;

	}
}


/**
 * 保存补充会议通知
 */
function doSave(formName,contraintField)
{
	var element = document.forms(formName).item(contraintField);
	if(checkNull(element.value))
	{
		alert("补充通知内容不能为空！");
		return;
	}
	document.forms(formName).submit();
}

/**
 * 保存/预览/发布会议通知
 */
function doMeetingNoteAction(formName,action)
{
	var sourceNumber = document.forms(formName).item("sourceNumber");
	var meetingType_ID = document.forms(formName).item("meetingType_ID");
	var urgencyLevel = document.forms(formName).item("urgencyLevel");
	var meetingTime = document.forms(formName).item("meetingTime");
	var meetingPlace = document.forms(formName).item("meetingPlace");
	var meetingSubject = document.forms(formName).item("meetingSubject");
	//var meetingContent = document.forms(formName).item("meetingContent");
	var meetingLinkman = document.forms(formName).item("meetingLinkman");
	var meetingLinkPhone = document.forms(formName).item("meetingLinkPhone");
	var linkManFax = document.forms(formName).item("linkManFax");
	document.forms(formName).actiontype.value = action;

	var message = "";
	if(checkNull(sourceNumber.value))
	{
		message += "会议编号不能为空\r\n";
	}
	if(meetingType_ID.value == "-1")
		message += "请选则会议种类\r\n";
	if(urgencyLevel.value == "-1")
		message += "请选则会议缓急程度\r\n";

	if(checkNull(meetingTime.value) || !checkDateFormat(meetingTime.value))
		message += "开会时间不能为空或时间格式不正确，必须为2005-04-05 12：00格式\r\n";
	if(checkNull(meetingPlace.value))
		message += "会议地点不能为空\r\n";
	if(checkNull(meetingSubject.value))
		message += "会议标题题不能为空\r\n";
	//if(checkNull(meetingContent.value))
	//	message += "请编辑会议通知内容\r\n";
	if(checkNull(meetingLinkman.value))
		message += "会议联系人不能为空\r\n";
	if(checkNull(meetingLinkPhone.value))
		message += "会议联系电话不能为空\r\n";
	if(message != "")
	{
		alert("警告：\r\n" + message);
		return;
	}
	if(action == "publish")
	{
	    if(confirm("你确定要发布会议通知吗？"))
    	    document.forms(formName).submit();
    }
    else if(action == "preview")
	{
	    document.forms(formName).action = "meetingnote_preview.jsp"
	    document.forms(formName).target = "_blank";
    	document.forms(formName).submit();
    }
    else
    {
        document.forms(formName).submit();
    }

}

/**
 * 预览会议通知
 */
function preview(formName)
{
}

//检查value是否为空或空串
function checkNull(value)
{
	if(!value || value == "")
		return true;
	return false;
}

function checkDateFormat(value)
{
	return true;
}


function addUndertakerUnit(formName,units)
{
	var hasChoose = false;
	var unitID = document.forms(formName).item(units);
	if(unitID)
	{
		if(unitID.length)
		{
			for(i = 0; i < unitID.length; i ++)
				if(unitID[i].checked)
				{
					hasChoose = true;
					break;
				}
		}
		else
		{
			if(!unitID.checked)
			{
			}
			else
			{
				hasChoose = true;
			}
		}
	}
	//if(!hasChoose)
	//{
	//	alert("请选则参会单位或参会单位类型！");
	//	return;
	//}
	document.forms(formName).submit();
}
function addUnit(formName,units)
{
	var hasChoose = false;

	var unitID = document.forms(formName).item(units);

	if(unitID)
	{
		if(unitID.length)
		{
			for(i = 0; i < unitID.length; i ++)
				if(unitID[i].checked)
				{
					hasChoose = true;
					break;
				}
		}
		else
		{
			if(!unitID.checked)
			{
			}
			else
			{
				hasChoose = true;
			}
		}
	}
	if(!hasChoose)
	{
		alert("请选则参会单位！");
		return;
	}
	document.forms(formName).submit();

}
function addUnitType(formName)
{
	var unitTypeID = document.forms(formName).unitType
	var hasChoose = false;
	if(unitTypeID)
	{
		if(unitTypeID.length)
		{
			for(i = 0; i < unitTypeID.length; i ++)
				if(unitTypeID[i].checked)
				{
					hasChoose = true;
					break;
				}
		}
		else
		{
			if(!unitTypeID.checked)
			{
			}
			else
			{
				hasChoose = true;
			}
		}
	}

	if(!hasChoose)
	{
		alert("请选则参会单位或参会单位类型！");
		return;
	}
	document.forms(formName).submit();
}

function closeChooseUnitWindow()
{
	//alert(window.parent.opener.document.all.item("unit").value = );
	window.parent.close();
}


function deleteUnitType(formName,deleteID)
{
	var element = document.forms(formName).item(deleteID);
	if(element)
	{
		if(element.length)
		{
			for(i = 0; i < element.length; i ++)
				if(element[i].checked)
				{
					if(confirm("你确定要删除参加会议单位类型吗？"))
						document.forms(formName).submit();
					return;
				}
			alert("请选择要删除参加会议单位类型");
			return;
		}
		else
		{
			if(!element.checked)
			{
				alert("请选择要删除参加会议单位类型");
				return;
			}
			else
			{
				if(confirm("你确定要删除参加会议单位类型吗？"))
					document.forms(formName).submit();
				return ;
			}
		}
	}
	else
	{
		alert("没有要删除的参加会议单位类型");
		return;

	}
}

function deleteUnit(formName,OrgUnitID)
{
	var element = document.forms(formName).item(OrgUnitID);
	if(element)
	{
		if(element.length)
		{
			for(i = 0; i < element.length; i ++)
				if(element[i].checked)
				{
					if(confirm("你确定要删除参加会议单位吗？"))
						document.forms(formName).submit();
					return;
				}
			alert("请选择要删除参加会议单位");
			return;
		}
		else
		{
			if(!element.checked)
			{
				alert("请选择要删除参加会议单位");
				return;
			}
			else
			{
				if(confirm("你确定要删除参加会议单位吗？"))
					document.forms(formName).submit();
				return ;
			}
		}
	}
	else
	{
		alert("没有要删除的参加会议单位");
		return;

	}
}

function deleteOriginText(formName,id)
{
	if(confirm("确定要删除会议原文吗？"))
	{
		document.forms(formName).originTextID.value = id;
		//document.forms(formName).action.value="deleteOriginText";
		document.forms(formName).submit();
		//location.replace(formName);
	}
}

function upload(id,url)
{
	if(id != "-1")
	{
		if(confirm("确定要替换原来的文档吗？"))
		{
			window.open(url);
		}
	}
	else
		window.open(url);
}

/**
 * 选择模板时，切换会议通知内容区的内容
 */
function changeTemplate(formName,element)
{

	var template = element.options[element.selectedIndex].value;

	if(template == "-1")
		return;
	if(!confirm("你确定要使用模板吗？\r\n如果你采用模板，先前的信息将丢失！"))
	    return;

	var templateInfo = template.split(":") ;
	window.opener.document.forms(formName).meetingTemplate_ID.value= templateInfo[0];
	var meetingContent = window.opener.document.forms(formName).meetingContent.value;

	var idx = templateInfo[1].indexOf(".");
	var fileName = templateInfo[1].substring(0,idx);
	var url = window.opener.document.forms(formName).meetingContenthref.value;

	var q = url.indexOf("?");
	if(q != -1)
	    url += "&sTemplate=/upload/template/" + fileName;
	else
	    url += "?sTemplate=/upload/template/" + fileName;

    url += "&sOpenModelFileName=" + meetingContent + "&meetingTemplate_ID=" + templateInfo[0];
	//document.fmeetingContent.location.replace(url);
	location.replace(url);
}
function addContent(formName)
{
   	var url = document.forms(formName).meetingContenthref_true.value;
   	window.open(url);
}
/**
 * 编辑会议通知页面返回函数
 */
function returnButton(formName)
{
	document.forms(formName).submit();
}
