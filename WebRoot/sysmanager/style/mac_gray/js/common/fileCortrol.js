/**
 * 以模式窗口的方式打开word文档操作功能
 * url word文档操作页面的相对路径，例如：“../../word/wordWindow.jsp”
 * labelName word文档中定义的标签名称，以“：”分割
 * labelValue 用来替换word文档中定义的标签名称的值，以“：”分割；要和labelName参数个数保持一致
 * openFile 打开的word文档的文件名，不包括扩展名
 * saveFile 保存的word文档的文件名，不包括扩展名
 * templateFile 打开的word模版的文件名，不包括扩展名
 * fileType 文件类型，即文件扩展名
 * fileStatus 以何种形式打开文件，0－模板管理； 1－正常方式打开文档； 2－只读方式打开文档；如果是已“2”不显示【保存】按钮
 * closeFlag 是否显示【关闭】按钮，0－不显示；1－显示
 * printFlag 是否显示【打印】按钮，0－不显示；1－显示
 */
function openWord(url,labelName,labelValue,openFile,saveFile,templateFile,fileType,fileStatus,closeFlag,printFlag) {
    var obj = new Object();
    obj.myStr = new Array(9);
    obj.myStr[0] = labelName;
    obj.myStr[1] = labelValue;
    obj.myStr[2] = openFile;
    obj.myStr[3] = saveFile;
    obj.myStr[4] = templateFile;
    obj.myStr[5] = fileType;
    obj.myStr[6] = fileStatus;
    obj.myStr[7] = closeFlag
    obj.myStr[8] = printFlag;

    var adjustWidthValue = 17;
    var adjustHeightValue = 22;
    var widthValue = 1200;
    var heightValue = 740;
    var topValue = 1;
    var leftValue = 1;
    var strDlgFeather='dialogTop:'+topValue+';dialogLeft:'+leftValue+'px;dialogWidth:'+widthValue+'px;dialogHeight:'+heightValue+'px;center:yes;status:no;menu:no;help:0;history:noresizable:no;';
    var obj = window.showModalDialog(url,obj,strDlgFeather);
}

/**
 * 以模式窗口的方式打开word文档操作功能
 * url word文档操作页面的相对路径，例如：“../../word/wordWindow.jsp”
 * labelName word文档中定义的标签名称，以“：”分割
 * labelValue 用来替换word文档中定义的标签名称的值，以“：”分割；要和labelName参数个数保持一致
 * openFile 打开的word文档的文件名，不包括扩展名
 * saveFile 保存的word文档的文件名，不包括扩展名
 * templateFile 打开的word模版的文件名，不包括扩展名
 * fileType 文件类型，即文件扩展名
 * fileStatus 以何种形式打开文件，0－模板管理； 1－正常方式打开文档； 2－只读方式打开文档；如果是已“2”不显示【保存】按钮
 * closeFlag 是否显示【关闭】按钮，0－不显示；1－显示
 * printFlag 是否显示【打印】按钮，0－不显示；1－显示
 * imageLabelName 图片标签名
 * imageLabelValue 图片标签值
 * localFile 打开的本地文件名
 */
function openWord(url,labelName,labelValue,openFile,saveFile,templateFile,fileType,fileStatus,closeFlag,printFlag,imageLabelName,imageLabelValue,localFile) {
    var obj = new Object();
    obj.myStr = new Array(12);
    obj.myStr[0] = labelName;
    obj.myStr[1] = labelValue;
    obj.myStr[2] = openFile;
    obj.myStr[3] = saveFile;
    obj.myStr[4] = templateFile;
    obj.myStr[5] = fileType;
    obj.myStr[6] = fileStatus;
    obj.myStr[7] = closeFlag
    obj.myStr[8] = printFlag;
    obj.myStr[9] = imageLabelName;
    obj.myStr[10] = imageLabelValue;
	obj.myStr[11] = localFile;

    var adjustWidthValue = 17;
    var adjustHeightValue = 22;
    var widthValue = 1200;
    var heightValue = 740;
    var topValue = 1;
    var leftValue = 1;
    var strDlgFeather='dialogTop:'+topValue+';dialogLeft:'+leftValue+'px;dialogWidth:'+widthValue+'px;dialogHeight:'+heightValue+'px;center:yes;status:no;menu:no;help:0;history:noresizable:no;';
    var obj = window.showModalDialog(url,obj,strDlgFeather);
}
