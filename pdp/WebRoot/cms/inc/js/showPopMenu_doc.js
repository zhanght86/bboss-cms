
var bV = parseInt(navigator.appVersion);
var NN4 = document.layers ? true : false;
var IE4 = document.all && bV >= 4 ? true : false;
var Tipmsg = '';
var eventy = 0;
var Tipshow = true;

function goPage(pagenum)
{
	document.MainForm.page.value = pagenum;
	document.MainForm.submit();
	return false;
}
function goGroup(id)
{
	document.MainForm.groupid.value = id;
	document.MainForm.submit();
	return false;
}
function Sort(type)
{
	document.MainForm.sorttype.value = type;
	document.MainForm.submit();
}
function SubmitForm()
{
	document.MainForm.page.value = 1;
	document.MainForm.submit();
}
function showTip(msg)
{
	if (Tipshow)
	{
		Tipmsg = msg;
		eventy = NN4 ? e.y : IE4 ? event.y : 0;
		showTimeOut = setTimeout('delayTip()', 600);
		Tipshow = false;
	}
}
function delayTip()
{
	var obj = 'TipBox';

	if (typeof(showTimeOut) != 'undefined') {clearTimeout(showTimeOut); Tipshow = true;}
	if (typeof(hideTimeOut) != 'undefined') clearTimeout(hideTimeOut);

	if (NN4)
	{
		if (document.layers[obj].visibility != 'visible')
		{
			with (document[obj].document)
			{
				open();
				write('<layer id=TipBox bgColor=#E9E9E9 style="width: 600px; border: 1px solid #8CD4EC" onMouseover="keepTip()" onMousewheel="keepTip()" onMouseout="hideTip()">' + Tipmsg + '</layer>');
				close();
			}
			var objp = document.layers.TipBox;
			objp.moveTo(100, eventy + 6);
		}
		document.layers[obj].visibility = 'visible';
	}
	else if(IE4)
	{
		if (document.all[obj].style.visibility != 'visible')
		{
			document.all[obj].innerHTML = Tipmsg;
			var objp = document.all.TipBox.style;
			var yy = document.body.scrollTop + eventy + 6;
			objp.pixelLeft = 180;
			objp.pixelTop = yy;
		}
		document.all[obj].style.visibility = 'visible';
	}
}
function keepTip()
{
	var obj = 'TipBox';

	if (typeof(showTimeOut) != 'undefined') {clearTimeout(showTimeOut); Tipshow = true;}
	if (typeof(hideTimeOut) != 'undefined') clearTimeout(hideTimeOut);

	if (NN4)
		document.layers[obj].visibility = 'visible';
	else if(IE4)
		document.all[obj].style.visibility = 'visible';
}
function hideTip()
{
	hideTimeOut = setTimeout('delayHide()', 100);
}
function delayHide()
{
	var obj = 'TipBox';

	if (typeof(showTimeOut) != 'undefined') {clearTimeout(showTimeOut); Tipshow = true;}
	if (typeof(hideTimeOut) != 'undefined') clearTimeout(hideTimeOut);

  	if (NN4)
	{
		if (document.layers[obj] != null)
			document.layers[obj].visibility = 'hidden';
	}
	else if(IE4)
		document.all[obj].style.visibility = 'hidden';
}
function showDetail(info_hash)
{
	var url = info_hash == '' ? 'viewgraph.php' : 'detail.php?info_hash=' + info_hash;
	window.open(url, 'detailview', 'scrollbars=yes, width=560, height=300');
}

var showPopStep = 10;
var popOpacity = 80;
var sPop = '';
var tFadeOut = null;

function showPopupText(popText)
{
	if (!IE4) return;

	var o = event.srcElement;
	var MouseX = event.x;
	var MouseY = event.y;

	if (popText != sPop)
	{
		sPop = popText;
		clearTimeout(tFadeOut);

		if (sPop == null || sPop == '')
		{
			document.all.popLayer.innerHTML = '';
			document.all.popLayer.style.filter = "Alpha()";
			document.all.popLayer.filters.Alpha.opacity = 0;	
		}
		else
		{
			document.all.popLayer.innerHTML = sPop;
			document.all.popLayer.style.filter = "Alpha(Opacity=0)";
			var popWidth = document.all.popLayer.clientWidth;
			var popHeight = document.all.popLayer.clientHeight;
			document.all.popLayer.style.left = MouseX + popWidth > document.body.clientWidth - 5 ? document.body.clientWidth - popWidth - 5 : MouseX;
			document.all.popLayer.style.top = MouseY + document.body.scrollTop;
			fadeOut();
		}
	}
}
function fadeOut()
{
	if (popLayer.filters.Alpha.opacity < popOpacity)
	{
		popLayer.filters.Alpha.opacity += showPopStep;
		tFadeOut = setTimeout('fadeOut()', 30);
	}
}


if (IE4)
{
	document.write('<div id=popLayer style="position:absolute; z-index:101" class=cPopText></div>');
	document.write('<div id=TipBox style="width: 120px; position: absolute; visibility: hidden; padding: 0px 0px 0px 0px; background-Color: #E9E9E9; filter: alpha(Opacity=92); border: 1px solid #7277B7; z-index: 99" onMouseover="keepTip()" onMousewheel="keepTip()" onMouseout="hideTip()"></div>');
}
else if (NN4)
{
	document.write('<div id=TipBox style="position: absolute; visibility: hidden; background-Color: #E9E9E9; filter: alpha(Opacity=92); border: 1px solid #7277B7; z-index: 99"></div>');
	setTimeout('window.onResize=setReload', 200);
}
