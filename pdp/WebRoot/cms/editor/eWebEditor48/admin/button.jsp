<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%request.setCharacterEncoding("UTF-8");%>

<%!
/*
*######################################
* eWebEditor v4.00 - Advanced online web based WYSIWYG HTML editor.
* Copyright (c) 2003-2007 eWebSoft.com
*
* For further information go to http://www.ewebsoft.com/
* This copyright notice MUST stay intact for use.
*######################################
*/


static String[][] aButton = new String[126][9];
%>
<%
//--------------------------------------
// Separate
//--------------------------------------
aButton[1][1] = "TBSep";
aButton[1][2] = "分隔线";
aButton[1][3] = "";
aButton[1][4] = "";
aButton[1][5] = "2";
aButton[1][6] = "";
aButton[1][7] = "TBSep";
aButton[1][8] = "1";

aButton[2][1] = "TBHandle";
aButton[2][2] = "工具栏头";
aButton[2][3] = "";
aButton[2][4] = "";
aButton[2][5] = "2";
aButton[2][6] = "";
aButton[2][7] = "TBHandle";
aButton[2][8] = "1";

aButton[3][1] = "Space";
aButton[3][2] = "空格";
aButton[3][3] = "";
aButton[3][4] = "&nbsp;";
aButton[3][5] = "2";
aButton[3][6] = "";
aButton[3][7] = "TBGen";
aButton[3][8] = "1";

//--------------------------------------
// Edit
//--------------------------------------
aButton[4][1] = "EditMenu";
aButton[4][2] = "编辑菜单";
aButton[4][3] = "editmenu.gif";
aButton[4][4] = "";
aButton[4][5] = "0";
aButton[4][6] = "showToolMenu('edit')";
aButton[4][7] = "Btn";
aButton[4][8] = "1";

aButton[5][1] = "UnDo";
aButton[5][2] = "撤消";
aButton[5][3] = "undo.gif";
aButton[5][4] = "";
aButton[5][5] = "0";
aButton[5][6] = "goHistory(-1)";
aButton[5][7] = "Btn";
aButton[5][8] = "1";

aButton[6][1] = "ReDo";
aButton[6][2] = "恢复";
aButton[6][3] = "redo.gif";
aButton[6][4] = "";
aButton[6][5] = "0";
aButton[6][6] = "goHistory(1)";
aButton[6][7] = "Btn";
aButton[6][8] = "1";

aButton[7][1] = "Cut";
aButton[7][2] = "剪切";
aButton[7][3] = "cut.gif";
aButton[7][4] = "";
aButton[7][5] = "0";
aButton[7][6] = "format('cut')";
aButton[7][7] = "Btn";
aButton[7][8] = "1";

aButton[8][1] = "Copy";
aButton[8][2] = "复制";
aButton[8][3] = "copy.gif";
aButton[8][4] = "";
aButton[8][5] = "0";
aButton[8][6] = "format('copy')";
aButton[8][7] = "Btn";
aButton[8][8] = "1";

aButton[9][1] = "Paste";
aButton[9][2] = "常规粘贴";
aButton[9][3] = "paste.gif";
aButton[9][4] = "";
aButton[9][5] = "0";
aButton[9][6] = "format('paste')";
aButton[9][7] = "Btn";
aButton[9][8] = "1";

aButton[10][1] = "PasteText";
aButton[10][2] = "纯文本粘贴";
aButton[10][3] = "pastetext.gif";
aButton[10][4] = "";
aButton[10][5] = "0";
aButton[10][6] = "PasteText()";
aButton[10][7] = "Btn";
aButton[10][8] = "1";

aButton[11][1] = "PasteWord";
aButton[11][2] = "从Word中粘贴";
aButton[11][3] = "pasteword.gif";
aButton[11][4] = "";
aButton[11][5] = "0";
aButton[11][6] = "PasteWord()";
aButton[11][7] = "Btn";
aButton[11][8] = "1";

aButton[12][1] = "Delete";
aButton[12][2] = "删除";
aButton[12][3] = "delete.gif";
aButton[12][4] = "";
aButton[12][5] = "0";
aButton[12][6] = "format('delete')";
aButton[12][7] = "Btn";
aButton[12][8] = "1";

aButton[13][1] = "RemoveFormat";
aButton[13][2] = "删除文字格式";
aButton[13][3] = "removeformat.gif";
aButton[13][4] = "";
aButton[13][5] = "0";
aButton[13][6] = "format('RemoveFormat')";
aButton[13][7] = "Btn";
aButton[13][8] = "1";

aButton[14][1] = "SelectAll";
aButton[14][2] = "全部选中";
aButton[14][3] = "selectall.gif";
aButton[14][4] = "";
aButton[14][5] = "0";
aButton[14][6] = "format('SelectAll')";
aButton[14][7] = "Btn";
aButton[14][8] = "1";

aButton[15][1] = "UnSelect";
aButton[15][2] = "取消选择";
aButton[15][3] = "unselect.gif";
aButton[15][4] = "";
aButton[15][5] = "0";
aButton[15][6] = "format('Unselect')";
aButton[15][7] = "Btn";
aButton[15][8] = "1";

aButton[16][1] = "FindReplace";
aButton[16][2] = "查找替换";
aButton[16][3] = "findreplace.gif";
aButton[16][4] = "";
aButton[16][5] = "0";
aButton[16][6] = "findReplace()";
aButton[16][7] = "Btn";
aButton[16][8] = "1";

aButton[17][1] = "SpellCheck";
aButton[17][2] = "拼写检查";
aButton[17][3] = "spellcheck.gif";
aButton[17][4] = "";
aButton[17][5] = "0";
aButton[17][6] = "spellCheck()";
aButton[17][7] = "Btn";
aButton[17][8] = "1";

//--------------------------------------
// Font
//--------------------------------------
aButton[18][1] = "FontMenu";
aButton[18][2] = "字体菜单";
aButton[18][3] = "fontmenu.gif";
aButton[18][4] = "";
aButton[18][5] = "0";
aButton[18][6] = "showToolMenu('font')";
aButton[18][7] = "Btn";
aButton[18][8] = "1";

aButton[19][1] = "Bold";
aButton[19][2] = "粗体";
aButton[19][3] = "bold.gif";
aButton[19][4] = "";
aButton[19][5] = "0";
aButton[19][6] = "format('bold')";
aButton[19][7] = "Btn";
aButton[19][8] = "1";

aButton[20][1] = "Italic";
aButton[20][2] = "斜体";
aButton[20][3] = "italic.gif";
aButton[20][4] = "";
aButton[20][5] = "0";
aButton[20][6] = "format('italic')";
aButton[20][7] = "Btn";
aButton[20][8] = "1";

aButton[21][1] = "UnderLine";
aButton[21][2] = "下划线";
aButton[21][3] = "underline.gif";
aButton[21][4] = "";
aButton[21][5] = "0";
aButton[21][6] = "format('underline')";
aButton[21][7] = "Btn";
aButton[21][8] = "1";

aButton[22][1] = "StrikeThrough";
aButton[22][2] = "中划线";
aButton[22][3] = "strikethrough.gif";
aButton[22][4] = "";
aButton[22][5] = "0";
aButton[22][6] = "format('StrikeThrough')";
aButton[22][7] = "Btn";
aButton[22][8] = "1";

aButton[23][1] = "SuperScript";
aButton[23][2] = "上标";
aButton[23][3] = "superscript.gif";
aButton[23][4] = "";
aButton[23][5] = "0";
aButton[23][6] = "format('superscript')";
aButton[23][7] = "Btn";
aButton[23][8] = "1";

aButton[24][1] = "SubScript";
aButton[24][2] = "下标";
aButton[24][3] = "subscript.gif";
aButton[24][4] = "";
aButton[24][5] = "0";
aButton[24][6] = "format('subscript')";
aButton[24][7] = "Btn";
aButton[24][8] = "1";

aButton[25][1] = "UpperCase";
aButton[25][2] = "转大写字母";
aButton[25][3] = "ucase.gif";
aButton[25][4] = "";
aButton[25][5] = "0";
aButton[25][6] = "formatText('uppercase')";
aButton[25][7] = "Btn";
aButton[25][8] = "1";

aButton[26][1] = "LowerCase";
aButton[26][2] = "转小写字母";
aButton[26][3] = "lcase.gif";
aButton[26][4] = "";
aButton[26][5] = "0";
aButton[26][6] = "formatText('lowercase')";
aButton[26][7] = "Btn";
aButton[26][8] = "1";

aButton[27][1] = "ForeColor";
aButton[27][2] = "字体颜色";
aButton[27][3] = "forecolor.gif";
aButton[27][4] = "";
aButton[27][5] = "0";
aButton[27][6] = "showDialog('selcolor.htm?action=forecolor', true)";
aButton[27][7] = "Btn";
aButton[27][8] = "1";

aButton[28][1] = "BackColor";
aButton[28][2] = "字体背景颜色";
aButton[28][3] = "backcolor.gif";
aButton[28][4] = "";
aButton[28][5] = "0";
aButton[28][6] = "showDialog('selcolor.htm?action=backcolor', true)";
aButton[28][7] = "Btn";
aButton[28][8] = "1";

aButton[29][1] = "Big";
aButton[29][2] = "字体增大";
aButton[29][3] = "tobig.gif";
aButton[29][4] = "";
aButton[29][5] = "0";
aButton[29][6] = "insert('big')";
aButton[29][7] = "Btn";
aButton[29][8] = "1";

aButton[30][1] = "Small";
aButton[30][2] = "字体减小";
aButton[30][3] = "tosmall.gif";
aButton[30][4] = "";
aButton[30][5] = "0";
aButton[30][6] = "insert('small')";
aButton[30][7] = "Btn";
aButton[30][8] = "1";

//--------------------------------------
// Paragraph
//--------------------------------------
aButton[31][1] = "ParagraphMenu";
aButton[31][2] = "段落菜单";
aButton[31][3] = "paragraphmenu.gif";
aButton[31][4] = "";
aButton[31][5] = "0";
aButton[31][6] = "showToolMenu('paragraph')";
aButton[31][7] = "Btn";
aButton[31][8] = "1";

aButton[32][1] = "JustifyLeft";
aButton[32][2] = "左对齐";
aButton[32][3] = "justifyleft.gif";
aButton[32][4] = "";
aButton[32][5] = "0";
aButton[32][6] = "format('justifyleft')";
aButton[32][7] = "Btn";
aButton[32][8] = "1";

aButton[33][1] = "JustifyCenter";
aButton[33][2] = "居中对齐";
aButton[33][3] = "justifycenter.gif";
aButton[33][4] = "";
aButton[33][5] = "0";
aButton[33][6] = "format('justifycenter')";
aButton[33][7] = "Btn";
aButton[33][8] = "1";

aButton[34][1] = "JustifyRight";
aButton[34][2] = "右对齐";
aButton[34][3] = "justifyright.gif";
aButton[34][4] = "";
aButton[34][5] = "0";
aButton[34][6] = "format('justifyright')";
aButton[34][7] = "Btn";
aButton[34][8] = "1";

aButton[35][1] = "JustifyFull";
aButton[35][2] = "两端对齐";
aButton[35][3] = "justifyfull.gif";
aButton[35][4] = "";
aButton[35][5] = "0";
aButton[35][6] = "format('JustifyFull')";
aButton[35][7] = "Btn";
aButton[35][8] = "1";

aButton[36][1] = "OrderedList";
aButton[36][2] = "编号";
aButton[36][3] = "insertorderedlist.gif";
aButton[36][4] = "";
aButton[36][5] = "0";
aButton[36][6] = "format('insertorderedlist')";
aButton[36][7] = "Btn";
aButton[36][8] = "1";

aButton[37][1] = "UnOrderedList";
aButton[37][2] = "项目符号";
aButton[37][3] = "insertunorderedlist.gif";
aButton[37][4] = "";
aButton[37][5] = "0";
aButton[37][6] = "format('insertunorderedlist')";
aButton[37][7] = "Btn";
aButton[37][8] = "1";

aButton[38][1] = "Indent";
aButton[38][2] = "增加缩进量";
aButton[38][3] = "indent.gif";
aButton[38][4] = "";
aButton[38][5] = "0";
aButton[38][6] = "format('indent')";
aButton[38][7] = "Btn";
aButton[38][8] = "1";

aButton[39][1] = "Outdent";
aButton[39][2] = "减少缩进量";
aButton[39][3] = "outdent.gif";
aButton[39][4] = "";
aButton[39][5] = "0";
aButton[39][6] = "format('outdent')";
aButton[39][7] = "Btn";
aButton[39][8] = "1";

aButton[40][1] = "BR";
aButton[40][2] = "插入换行符";
aButton[40][3] = "br.gif";
aButton[40][4] = "";
aButton[40][5] = "0";
aButton[40][6] = "insert('br')";
aButton[40][7] = "Btn";
aButton[40][8] = "1";

aButton[41][1] = "Paragraph";
aButton[41][2] = "插入段落";
aButton[41][3] = "insertparagraph.gif";
aButton[41][4] = "";
aButton[41][5] = "0";
aButton[41][6] = "format('InsertParagraph')";
aButton[41][7] = "Btn";
aButton[41][8] = "1";

aButton[42][1] = "ParagraphAttr";
aButton[42][2] = "段落属性";
aButton[42][3] = "paragraph.gif";
aButton[42][4] = "";
aButton[42][5] = "0";
aButton[42][6] = "paragraphAttr()";
aButton[42][7] = "Btn";
aButton[42][8] = "1";

//--------------------------------------
// Component
//--------------------------------------
aButton[43][1] = "ComponentMenu";
aButton[43][2] = "组件菜单";
aButton[43][3] = "componentmenu.gif";
aButton[43][4] = "";
aButton[43][5] = "0";
aButton[43][6] = "showToolMenu('component')";
aButton[43][7] = "Btn";
aButton[43][8] = "1";

aButton[44][1] = "Image";
aButton[44][2] = "插入或修改图片";
aButton[44][3] = "img.gif";
aButton[44][4] = "";
aButton[44][5] = "0";
aButton[44][6] = "showDialog('img.htm', true)";
aButton[44][7] = "Btn";
aButton[44][8] = "1";

aButton[45][1] = "Flash";
aButton[45][2] = "插入Flash动画";
aButton[45][3] = "flash.gif";
aButton[45][4] = "";
aButton[45][5] = "0";
aButton[45][6] = "showDialog('flash.htm', true)";
aButton[45][7] = "Btn";
aButton[45][8] = "1";

aButton[46][1] = "Media";
aButton[46][2] = "插入自动播放的媒体文件";
aButton[46][3] = "media.gif";
aButton[46][4] = "";
aButton[46][5] = "0";
aButton[46][6] = "showDialog('media.htm', true)";
aButton[46][7] = "Btn";
aButton[46][8] = "1";

aButton[47][1] = "File";
aButton[47][2] = "插入其他文件";
aButton[47][3] = "file.gif";
aButton[47][4] = "";
aButton[47][5] = "0";
aButton[47][6] = "showDialog('file.htm', true)";
aButton[47][7] = "Btn";
aButton[47][8] = "1";

aButton[48][1] = "RemoteUpload";
aButton[48][2] = "远程文件自动上传";
aButton[48][3] = "remoteupload.gif";
aButton[48][4] = "";
aButton[48][5] = "0";
aButton[48][6] = "remoteUpload()";
aButton[48][7] = "Btn";
aButton[48][8] = "1";

aButton[49][1] = "LocalUpload";
aButton[49][2] = "本地文件自动上传";
aButton[49][3] = "localupload.gif";
aButton[49][4] = "";
aButton[49][5] = "0";
aButton[49][6] = "showDialog('localupload.htm', true)";
aButton[49][7] = "Btn";
aButton[49][8] = "1";

aButton[50][1] = "Fieldset";
aButton[50][2] = "插入或修改栏目框";
aButton[50][3] = "fieldset.gif";
aButton[50][4] = "";
aButton[50][5] = "0";
aButton[50][6] = "showDialog('fieldset.htm', true)";
aButton[50][7] = "Btn";
aButton[50][8] = "1";

aButton[51][1] = "Iframe";
aButton[51][2] = "插入或修改网页帧";
aButton[51][3] = "iframe.gif";
aButton[51][4] = "";
aButton[51][5] = "0";
aButton[51][6] = "showDialog('iframe.htm', true)";
aButton[51][7] = "Btn";
aButton[51][8] = "1";

aButton[52][1] = "HorizontalRule";
aButton[52][2] = "插入水平尺";
aButton[52][3] = "inserthorizontalrule.gif";
aButton[52][4] = "";
aButton[52][5] = "0";
aButton[52][6] = "format('InsertHorizontalRule')";
aButton[52][7] = "Btn";
aButton[52][8] = "1";

aButton[53][1] = "Marquee";
aButton[53][2] = "插入或修改字幕";
aButton[53][3] = "marquee.gif";
aButton[53][4] = "";
aButton[53][5] = "0";
aButton[53][6] = "showDialog('marquee.htm', true)";
aButton[53][7] = "Btn";
aButton[53][8] = "1";

aButton[54][1] = "CreateLink";
aButton[54][2] = "插入或修改超级链接";
aButton[54][3] = "createlink.gif";
aButton[54][4] = "";
aButton[54][5] = "0";
aButton[54][6] = "createLink()";
aButton[54][7] = "Btn";
aButton[54][8] = "1";

aButton[55][1] = "Unlink";
aButton[55][2] = "取消超级链接或标签";
aButton[55][3] = "unlink.gif";
aButton[55][4] = "";
aButton[55][5] = "0";
aButton[55][6] = "format('UnLink')";
aButton[55][7] = "Btn";
aButton[55][8] = "1";

aButton[56][1] = "Map";
aButton[56][2] = "图形热点链接";
aButton[56][3] = "map.gif";
aButton[56][4] = "";
aButton[56][5] = "0";
aButton[56][6] = "mapEdit()";
aButton[56][7] = "Btn";
aButton[56][8] = "1";

aButton[57][1] = "Anchor";
aButton[57][2] = "书签管理";
aButton[57][3] = "anchor.gif";
aButton[57][4] = "";
aButton[57][5] = "0";
aButton[57][6] = "showDialog('anchor.htm', true);";
aButton[57][7] = "Btn";
aButton[57][8] = "1";

//--------------------------------------
// Gallery
//--------------------------------------
aButton[58][1] = "GalleryMenu";
aButton[58][2] = "文件库菜单";
aButton[58][3] = "gallerymenu.gif";
aButton[58][4] = "";
aButton[58][5] = "0";
aButton[58][6] = "showToolMenu('gallery')";
aButton[58][7] = "Btn";
aButton[58][8] = "1";

aButton[59][1] = "GalleryImage";
aButton[59][2] = "浏览图片库";
aButton[59][3] = "galleryimage.gif";
aButton[59][4] = "";
aButton[59][5] = "0";
aButton[59][6] = "showDialog('browse.htm?type=image', true)";
aButton[59][7] = "Btn";
aButton[59][8] = "1";

aButton[60][1] = "GalleryFlash";
aButton[60][2] = "浏览Flash库";
aButton[60][3] = "galleryflash.gif";
aButton[60][4] = "";
aButton[60][5] = "0";
aButton[60][6] = "showDialog('browse.htm?type=flash', true)";
aButton[60][7] = "Btn";
aButton[60][8] = "1";

aButton[61][1] = "GalleryMedia";
aButton[61][2] = "浏览媒体库";
aButton[61][3] = "galleryimage.gif";
aButton[61][4] = "";
aButton[61][5] = "0";
aButton[61][6] = "showDialog('browse.htm?type=media', true)";
aButton[61][7] = "Btn";
aButton[61][8] = "1";

aButton[62][1] = "GalleryFile";
aButton[62][2] = "浏览文件库";
aButton[62][3] = "galleryfile.gif";
aButton[62][4] = "";
aButton[62][5] = "0";
aButton[62][6] = "showDialog('browse.htm?type=file', true)";
aButton[62][7] = "Btn";
aButton[62][8] = "1";

//--------------------------------------
// Object
//--------------------------------------
aButton[63][1] = "ObjectMenu";
aButton[63][2] = "对象效果菜单";
aButton[63][3] = "objectmenu.gif";
aButton[63][4] = "";
aButton[63][5] = "0";
aButton[63][6] = "showToolMenu('object')";
aButton[63][7] = "Btn";
aButton[63][8] = "1";

aButton[64][1] = "BgColor";
aButton[64][2] = "对象背景颜色";
aButton[64][3] = "bgcolor.gif";
aButton[64][4] = "";
aButton[64][5] = "0";
aButton[64][6] = "showDialog('selcolor.htm?action=bgcolor', true)";
aButton[64][7] = "Btn";
aButton[64][8] = "1";

aButton[65][1] = "BackImage";
aButton[65][2] = "背景图片";
aButton[65][3] = "bgpic.gif";
aButton[65][4] = "";
aButton[65][5] = "0";
aButton[65][6] = "showDialog('backimage.htm', true)";
aButton[65][7] = "Btn";
aButton[65][8] = "1";

aButton[66][1] = "absolutePosition";
aButton[66][2] = "绝对或相对位置";
aButton[66][3] = "abspos.gif";
aButton[66][4] = "";
aButton[66][5] = "0";
aButton[66][6] = "absolutePosition()";
aButton[66][7] = "Btn";
aButton[66][8] = "1";

aButton[67][1] = "zIndexBackward";
aButton[67][2] = "下移一层";
aButton[67][3] = "backward.gif";
aButton[67][4] = "";
aButton[67][5] = "0";
aButton[67][6] = "zIndex('backward')";
aButton[67][7] = "Btn";
aButton[67][8] = "1";

aButton[68][1] = "zIndexForward";
aButton[68][2] = "上移一层";
aButton[68][3] = "forward.gif";
aButton[68][4] = "";
aButton[68][5] = "0";
aButton[68][6] = "zIndex('forward')";
aButton[68][7] = "Btn";
aButton[68][8] = "1";

aButton[69][1] = "ShowBorders";
aButton[69][2] = "显示或隐藏指导方针";
aButton[69][3] = "showborders.gif";
aButton[69][4] = "";
aButton[69][5] = "0";
aButton[69][6] = "showBorders()";
aButton[69][7] = "Btn";
aButton[69][8] = "1";

aButton[70][1] = "Quote";
aButton[70][2] = "引用样式";
aButton[70][3] = "quote.gif";
aButton[70][4] = "";
aButton[70][5] = "0";
aButton[70][6] = "insert('quote')";
aButton[70][7] = "Btn";
aButton[70][8] = "1";

aButton[71][1] = "Code";
aButton[71][2] = "代码样式";
aButton[71][3] = "code.gif";
aButton[71][4] = "";
aButton[71][5] = "0";
aButton[71][6] = "insert('code')";
aButton[71][7] = "Btn";
aButton[71][8] = "1";

//--------------------------------------
// Tool
//--------------------------------------
aButton[72][1] = "ToolMenu";
aButton[72][2] = "工具菜单";
aButton[72][3] = "toolmenu.gif";
aButton[72][4] = "";
aButton[72][5] = "0";
aButton[72][6] = "showToolMenu('tool')";
aButton[72][7] = "Btn";
aButton[72][8] = "1";

aButton[73][1] = "Symbol";
aButton[73][2] = "插入特殊字符";
aButton[73][3] = "symbol.gif";
aButton[73][4] = "";
aButton[73][5] = "0";
aButton[73][6] = "showDialog('symbol.htm', true)";
aButton[73][7] = "Btn";
aButton[73][8] = "1";

aButton[74][1] = "PrintBreak";
aButton[74][2] = "插入打印分页符";
aButton[74][3] = "printbreak.gif";
aButton[74][4] = "";
aButton[74][5] = "0";
aButton[74][6] = "insert('printbreak')";
aButton[74][7] = "Btn";
aButton[74][8] = "1";

aButton[75][1] = "Excel";
aButton[75][2] = "插入Excel表格";
aButton[75][3] = "excel.gif";
aButton[75][4] = "";
aButton[75][5] = "0";
aButton[75][6] = "showDialog('owcexcel.htm', true)";
aButton[75][7] = "Btn";
aButton[75][8] = "1";

aButton[76][1] = "Emot";
aButton[76][2] = "插入表情图标";
aButton[76][3] = "emot.gif";
aButton[76][4] = "";
aButton[76][5] = "0";
aButton[76][6] = "showDialog('emot.htm', true)";
aButton[76][7] = "Btn";
aButton[76][8] = "1";

aButton[77][1] = "EQ";
aButton[77][2] = "插入或修改公式";
aButton[77][3] = "eq.gif";
aButton[77][4] = "";
aButton[77][5] = "0";
aButton[77][6] = "showDialog('eq.htm', true)";
aButton[77][7] = "Btn";
aButton[77][8] = "1";

aButton[78][1] = "Art";
aButton[78][2] = "插入或修改艺术字";
aButton[78][3] = "art.gif";
aButton[78][4] = "";
aButton[78][5] = "0";
aButton[78][6] = "showDialog('art.htm', true)";
aButton[78][7] = "Btn";
aButton[78][8] = "1";

aButton[79][1] = "NowDate";
aButton[79][2] = "插入当前日期";
aButton[79][3] = "date.gif";
aButton[79][4] = "";
aButton[79][5] = "0";
aButton[79][6] = "insert('nowdate')";
aButton[79][7] = "Btn";
aButton[79][8] = "1";

aButton[80][1] = "NowTime";
aButton[80][2] = "插入当前时间";
aButton[80][3] = "time.gif";
aButton[80][4] = "";
aButton[80][5] = "0";
aButton[80][6] = "insert('nowtime')";
aButton[80][7] = "Btn";
aButton[80][8] = "1";

aButton[81][1] = "ImportWord";
aButton[81][2] = "导入Word文档";
aButton[81][3] = "importword.gif";
aButton[81][4] = "";
aButton[81][5] = "0";
aButton[81][6] = "showDialog('importword.htm', true)";
aButton[81][7] = "Btn";
aButton[81][8] = "1";

aButton[82][1] = "ImportExcel";
aButton[82][2] = "导入Excel电子表格";
aButton[82][3] = "importexcel.gif";
aButton[82][4] = "";
aButton[82][5] = "0";
aButton[82][6] = "showDialog('importexcel.htm', true)";
aButton[82][7] = "Btn";
aButton[82][8] = "1";

//--------------------------------------
// Form
//--------------------------------------
aButton[83][1] = "FormMenu";
aButton[83][2] = "表单菜单";
aButton[83][3] = "formmenu.gif";
aButton[83][4] = "";
aButton[83][5] = "0";
aButton[83][6] = "showToolMenu('form')";
aButton[83][7] = "Btn";
aButton[83][8] = "1";

aButton[84][1] = "FormText";
aButton[84][2] = "插入输入框";
aButton[84][3] = "formtext.gif";
aButton[84][4] = "";
aButton[84][5] = "0";
aButton[84][6] = "format('InsertInputText')";
aButton[84][7] = "Btn";
aButton[84][8] = "1";

aButton[85][1] = "FormTextArea";
aButton[85][2] = "插入文字输入区";
aButton[85][3] = "formtextarea.gif";
aButton[85][4] = "";
aButton[85][5] = "0";
aButton[85][6] = "format('InsertTextArea')";
aButton[85][7] = "Btn";
aButton[85][8] = "1";

aButton[86][1] = "FormRadio";
aButton[86][2] = "插入单选钮";
aButton[86][3] = "formradio.gif";
aButton[86][4] = "";
aButton[86][5] = "0";
aButton[86][6] = "format('InsertInputRadio')";
aButton[86][7] = "Btn";
aButton[86][8] = "1";

aButton[87][1] = "FormCheckbox";
aButton[87][2] = "插入复选框";
aButton[87][3] = "formcheckbox.gif";
aButton[87][4] = "";
aButton[87][5] = "0";
aButton[87][6] = "format('InsertInputCheckbox')";
aButton[87][7] = "Btn";
aButton[87][8] = "1";

aButton[88][1] = "FormDropdown";
aButton[88][2] = "插入下拉框";
aButton[88][3] = "formdropdown.gif";
aButton[88][4] = "";
aButton[88][5] = "0";
aButton[88][6] = "format('InsertSelectDropdown')";
aButton[88][7] = "Btn";
aButton[88][8] = "1";

aButton[89][1] = "FormButton";
aButton[89][2] = "插入按钮";
aButton[89][3] = "formbutton.gif";
aButton[89][4] = "";
aButton[89][5] = "0";
aButton[89][6] = "format('InsertButton')";
aButton[89][7] = "Btn";
aButton[89][8] = "1";

//--------------------------------------
// Table
//--------------------------------------
aButton[90][1] = "TableMenu";
aButton[90][2] = "表格菜单";
aButton[90][3] = "tablemenu.gif";
aButton[90][4] = "";
aButton[90][5] = "0";
aButton[90][6] = "showToolMenu('table')";
aButton[90][7] = "Btn";
aButton[90][8] = "1";

aButton[91][1] = "TableInsert";
aButton[91][2] = "插入表格";
aButton[91][3] = "tableinsert.gif";
aButton[91][4] = "";
aButton[91][5] = "0";
aButton[91][6] = "TableInsert()";
aButton[91][7] = "Btn";
aButton[91][8] = "1";

aButton[92][1] = "TableProp";
aButton[92][2] = "修改表格属性";
aButton[92][3] = "tableprop.gif";
aButton[92][4] = "";
aButton[92][5] = "0";
aButton[92][6] = "TableProp()";
aButton[92][7] = "Btn";
aButton[92][8] = "1";

aButton[93][1] = "TableCellProp";
aButton[93][2] = "单元格属性";
aButton[93][3] = "tablecellprop.gif";
aButton[93][4] = "";
aButton[93][5] = "0";
aButton[93][6] = "TableCellProp()";
aButton[93][7] = "Btn";
aButton[93][8] = "1";

aButton[94][1] = "TableCellSplit";
aButton[94][2] = "拆分单元格";
aButton[94][3] = "tablecellsplit.gif";
aButton[94][4] = "";
aButton[94][5] = "0";
aButton[94][6] = "TableCellSplit()";
aButton[94][7] = "Btn";
aButton[94][8] = "1";

aButton[95][1] = "TableRowProp";
aButton[95][2] = "表格行属性";
aButton[95][3] = "tablerowprop.gif";
aButton[95][4] = "";
aButton[95][5] = "0";
aButton[95][6] = "TableRowProp()";
aButton[95][7] = "Btn";
aButton[95][8] = "1";

aButton[96][1] = "TableRowInsertAbove";
aButton[96][2] = "插入行（在上方）";
aButton[96][3] = "tablerowinsertabove.gif";
aButton[96][4] = "";
aButton[96][5] = "0";
aButton[96][6] = "TableRowInsertAbove()";
aButton[96][7] = "Btn";
aButton[96][8] = "1";

aButton[97][1] = "TableRowInsertBelow";
aButton[97][2] = "插入行（在下方）";
aButton[97][3] = "tablerowinsertbelow.gif";
aButton[97][4] = "";
aButton[97][5] = "0";
aButton[97][6] = "TableRowInsertBelow()";
aButton[97][7] = "Btn";
aButton[97][8] = "1";

aButton[98][1] = "TableRowMerge";
aButton[98][2] = "合并行（向下方）";
aButton[98][3] = "tablerowmerge.gif";
aButton[98][4] = "";
aButton[98][5] = "0";
aButton[98][6] = "TableRowMerge()";
aButton[98][7] = "Btn";
aButton[98][8] = "1";

aButton[99][1] = "TableRowSplit";
aButton[99][2] = "拆分行";
aButton[99][3] = "tablerowsplit.gif";
aButton[99][4] = "";
aButton[99][5] = "0";
aButton[99][6] = "TableRowSplit(2)";
aButton[99][7] = "Btn";
aButton[99][8] = "1";

aButton[100][1] = "TableRowDelete";
aButton[100][2] = "删除行";
aButton[100][3] = "tablerowdelete.gif";
aButton[100][4] = "";
aButton[100][5] = "0";
aButton[100][6] = "TableRowDelete()";
aButton[100][7] = "Btn";
aButton[100][8] = "1";

aButton[101][1] = "TableColInsertLeft";
aButton[101][2] = "插入列（在左侧）";
aButton[101][3] = "tablecolinsertleft.gif";
aButton[101][4] = "";
aButton[101][5] = "0";
aButton[101][6] = "TableColInsertLeft()";
aButton[101][7] = "Btn";
aButton[101][8] = "1";

aButton[102][1] = "TableColInsertRight";
aButton[102][2] = "插入列（在右侧）";
aButton[102][3] = "tablecolinsertright.gif";
aButton[102][4] = "";
aButton[102][5] = "0";
aButton[102][6] = "TableColInsertRight()";
aButton[102][7] = "Btn";
aButton[102][8] = "1";

aButton[103][1] = "TableColMerge";
aButton[103][2] = "合并列（向右侧）";
aButton[103][3] = "tablecolmerge.gif";
aButton[103][4] = "";
aButton[103][5] = "0";
aButton[103][6] = "TableColMerge()";
aButton[103][7] = "Btn";
aButton[103][8] = "1";

aButton[104][1] = "TableColSplit";
aButton[104][2] = "拆分列";
aButton[104][3] = "tablecolsplit.gif";
aButton[104][4] = "";
aButton[104][5] = "0";
aButton[104][6] = "TableColSplit(2)";
aButton[104][7] = "Btn";
aButton[104][8] = "1";

aButton[105][1] = "TableColDelete";
aButton[105][2] = "删除列";
aButton[105][3] = "tablecoldelete.gif";
aButton[105][4] = "";
aButton[105][5] = "0";
aButton[105][6] = "TableColDelete()";
aButton[105][7] = "Btn";
aButton[105][8] = "1";

//--------------------------------------
// File
//--------------------------------------
aButton[106][1] = "FileMenu";
aButton[106][2] = "文件视图菜单";
aButton[106][3] = "filemenu.gif";
aButton[106][4] = "";
aButton[106][5] = "0";
aButton[106][6] = "showToolMenu('file')";
aButton[106][7] = "Btn";
aButton[106][8] = "1";

aButton[107][1] = "Refresh";
aButton[107][2] = "新建";
aButton[107][3] = "refresh.gif";
aButton[107][4] = "";
aButton[107][5] = "0";
aButton[107][6] = "format('refresh')";
aButton[107][7] = "Btn";
aButton[107][8] = "1";

aButton[108][1] = "ModeCode";
aButton[108][2] = "代码状态";
aButton[108][3] = "modecodebtn.gif";
aButton[108][4] = "";
aButton[108][5] = "0";
aButton[108][6] = "setMode('CODE')";
aButton[108][7] = "Btn";
aButton[108][8] = "1";

aButton[109][1] = "ModeEdit";
aButton[109][2] = "编辑状态";
aButton[109][3] = "modeeditbtn.gif";
aButton[109][4] = "";
aButton[109][5] = "0";
aButton[109][6] = "setMode('EDIT')";
aButton[109][7] = "Btn";
aButton[109][8] = "1";

aButton[110][1] = "ModeText";
aButton[110][2] = "文本状态";
aButton[110][3] = "modetextbtn.gif";
aButton[110][4] = "";
aButton[110][5] = "0";
aButton[110][6] = "setMode('TEXT')";
aButton[110][7] = "Btn";
aButton[110][8] = "1";

aButton[111][1] = "ModeView";
aButton[111][2] = "预览状态";
aButton[111][3] = "modeviewbtn.gif";
aButton[111][4] = "";
aButton[111][5] = "0";
aButton[111][6] = "setMode('VIEW')";
aButton[111][7] = "Btn";
aButton[111][8] = "1";

aButton[112][1] = "SizePlus";
aButton[112][2] = "增高编辑区";
aButton[112][3] = "sizeplus.gif";
aButton[112][4] = "";
aButton[112][5] = "0";
aButton[112][6] = "sizeChange(300)";
aButton[112][7] = "Btn";
aButton[112][8] = "1";

aButton[113][1] = "SizeMinus";
aButton[113][2] = "减小编辑区";
aButton[113][3] = "sizeminus.gif";
aButton[113][4] = "";
aButton[113][5] = "0";
aButton[113][6] = "sizeChange(-300)";
aButton[113][7] = "Btn";
aButton[113][8] = "1";

aButton[114][1] = "Print";
aButton[114][2] = "打印";
aButton[114][3] = "print.gif";
aButton[114][4] = "";
aButton[114][5] = "0";
aButton[114][6] = "format('Print')";
aButton[114][7] = "Btn";
aButton[114][8] = "1";

aButton[115][1] = "ZoomMenu";
aButton[115][2] = "缩放菜单";
aButton[115][3] = "zoommenu.gif";
aButton[115][4] = "";
aButton[115][5] = "0";
aButton[115][6] = "showToolMenu('zoom')";
aButton[115][7] = "Btn";
aButton[115][8] = "1";

aButton[116][1] = "Maximize";
aButton[116][2] = "全屏编辑";
aButton[116][3] = "maximize.gif";
aButton[116][4] = "";
aButton[116][5] = "0";
aButton[116][6] = "Maximize()";
aButton[116][7] = "Btn";
aButton[116][8] = "1";

aButton[117][1] = "Minimize";
aButton[117][2] = "全屏返回";
aButton[117][3] = "minimize.gif";
aButton[117][4] = "";
aButton[117][5] = "0";
aButton[117][6] = "parent.Minimize()";
aButton[117][7] = "Btn";
aButton[117][8] = "0";

aButton[118][1] = "Save";
aButton[118][2] = "保存并返回";
aButton[118][3] = "save.gif";
aButton[118][4] = "";
aButton[118][5] = "0";
aButton[118][6] = "parent.doSave()";
aButton[118][7] = "Btn";
aButton[118][8] = "1";

//--------------------------------------
// Help
//--------------------------------------
aButton[119][1] = "Help";
aButton[119][2] = "查看使用帮助";
aButton[119][3] = "help.gif";
aButton[119][4] = "";
aButton[119][5] = "0";
aButton[119][6] = "showDialog('help.htm')";
aButton[119][7] = "Btn";
aButton[119][8] = "0";

aButton[120][1] = "About";
aButton[120][2] = "关于 - eWebEditor";
aButton[120][3] = "about.gif";
aButton[120][4] = "";
aButton[120][5] = "0";
aButton[120][6] = "showDialog('about.htm')";
aButton[120][7] = "Btn";
aButton[120][8] = "1";

aButton[121][1] = "Site";
aButton[121][2] = "eWebEditor站点";
aButton[121][3] = "site.gif";
aButton[121][4] = "";
aButton[121][5] = "0";
aButton[121][6] = "window.open('http://www.ewebeditor.net')";
aButton[121][7] = "Btn";
aButton[121][8] = "1";

//--------------------------------------
// Combo
//--------------------------------------
aButton[122][1] = "FontName";
aButton[122][2] = "字体";
aButton[122][3] = "";
aButton[122][4] = "";
aButton[122][5] = "1";
aButton[122][6] = "formatFont('face',this[this.selectedIndex].value);this.selectedIndex=0";
aButton[122][7] = "TBGen";
aButton[122][8] = "1";

aButton[123][1] = "FontSize";
aButton[123][2] = "字体大小";
aButton[123][3] = "";
aButton[123][4] = "";
aButton[123][5] = "1";
aButton[123][6] = "formatFont('size',this[this.selectedIndex].value);this.selectedIndex=0";
aButton[123][7] = "TBGen";
aButton[123][8] = "1";

aButton[124][1] = "FormatBlock";
aButton[124][2] = "段落样式";
aButton[124][3] = "";
aButton[124][4] = "";
aButton[124][5] = "1";
aButton[124][6] = "format('FormatBlock',this[this.selectedIndex].value);this.selectedIndex=0";
aButton[124][7] = "TBGen";
aButton[124][8] = "1";

aButton[125][1] = "ZoomSelect";
aButton[125][2] = "缩放下拉框";
aButton[125][3] = "";
aButton[125][4] = "<option value='10'>10%</option><option value='25'>25%</option><option value='50'>50%</option><option value='75'>75%</option><option value='100' selected>100%</option><option value='150'>150%</option><option value='200'>200%</option><option value='500'>500%</option>";
aButton[125][5] = "1";
aButton[125][6] = "doZoom(this[this.selectedIndex].value)";
aButton[125][7] = "TBGen";
aButton[125][8] = "1";

%>