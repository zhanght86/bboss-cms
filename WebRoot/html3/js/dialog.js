(function() {
	var _skin, _lhgcore;
	var _search = window.location.search;
	if (_search) {
		_skin = _search.split('demoSkin=')[1];
	};
	document.write('<script src="lhgdialog.min.js?skin=' + (_skin || 'default') +'"></script>');
	window._isDemoSkin = !!_skin;
})();
$(function(){	
	$('#view_sincerity').dialog({  id:'iframeId',  max: false,
	width: '900px', height: '600px',
    min: false,
	title:'查看诚信档案',
    lock: true, 
    content: 'url:view_sincerity.html'
 });
	});