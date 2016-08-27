function reinitIframe() {
	var iframe = document.getElementById("userListFrame");
	try {
		var bHeight = iframe.contentWindow.document.body.scrollHeight;
		var dHeight = iframe.contentWindow.document.documentElement.scrollHeight;
		var height = Math.max(bHeight, dHeight);
		iframe.height = height;
	} catch (ex) {
	}
}
//window.setInterval("reinitIframe()", 200);
function update_scheme() {
	var id = getCheckBoxValue("queryList", "onecheck");
	if (id == "") {
		alert("请选择要修改的记录！");
		return;
	}
	id = id.substr(0, id.length - 1);
	var temp = id.split(",");
	if (temp.length > 1) {
		alert("每次最多只能修改一条记录，请移除相关记录后再进行修改！");
		return;
	} else {
		JqueryDialog.Open('编辑方案信息', basePath+'/scheme_getSchemeById.action?tdPwSchemeTemp.sequenceId='+temp, 1000, 298);
	}
}
function del_scheme() {
	var id = getCheckBoxValue("queryList", "onecheck");
	if (id == "") {
		alert("请选择要删除的记录！");
		return;
	} else {
		if (confirm("确定要删除选择的记录信息？")) {
			id = id.substr(0, id.length - 1);
			var manageListFrameForm = $("#manageListFrameForm");
			manageListFrameForm.attr("target", "queryList");
			manageListFrameForm.attr("action", basePath
					+ "/scheme_deleteScheme.action?tdPwSchemeTemp.sequenceId=" + id + "&cache="
					+ new Date().getTime());
			manageListFrameForm.submit();
		}
	}
}

function trim(str) { //删除左右两端的空格
　　return str.replace(/(^\s*)|(\s*$)/g, "");
}
function ltrim(str){ //删除左边的空格
　　return str.replace(/(^\s*)/g,"");
}
function rtrim(str){ //删除右边的空格
　　return str.replace(/(\s*$)/g,"");
}
