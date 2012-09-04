var divMainLoader;
var divSourceclick ;//需要单击打开TAB的DIV
var divTarget;//加载TAB的DIV
var jericho = {
	showLoader : function() {
		$(divMainLoader).css('display', 'block');
	},
	removeLoader : function() {
		$(divMainLoader).css('display', 'none');
	},
	buildTree : function() {
		$('.functree li').each( function() {
			if ($(this).is(':has(ul)'))
				$(this).addClass('collapse');
			if ($(this).next().is('li') || $(this).next().is('ul'))
				$(this).css( {
					borderLeft :'dashed 1px #dedede'
				});
		})
		$('li.collapse>span').click(
				function() {
					$(this).next('ul').slideToggle(
							'fast',
							function() {
								if ($(this).parent().hasClass('collapse'))
									$(this).parent().removeClass('collapse')
											.addClass('expand');
								else
									$(this).parent().removeClass('expand')
											.addClass('collapse');
							})
				})

		$('span.func').css( {
			'cursor' :'pointer'
		}).hover( function() {
			$(this).css( {
				'color' :'#3de',
				'text-decoration' :'underline'
			});
		}, function() {
			$(this).css( {
				'color' :'#000',
				'text-decoration' :'none'
			});
		}).click( function() {
			addTabeItem($(this));
			/*$.fn.jerichoTab.addTab( {
				tabFirer :$(this),
				title :$(this).text(),
				closeable :true,
				iconImg :$(this).attr('iconImg'),
				data : {
					dataType :$(this).attr('dataType'),
					dataLink :$(this).attr('dataLink')
				}
			}).showLoader().loadData();*/
		});
	},
	buildTabpanel : function(maxtabs) {
		$.fn.initJerichoTab( {
			renderTo :divTarget,
			uniqueId :'myJerichoTab',
			maxtabs : maxtabs,
			contentCss : {
				'height' :$(divTarget).height() -23
			},
			tabs : [ {
				title :'工作台',
				closeable :false,
				iconImg :'',
				data : {
					dataType :'iframe',
					dataLink :'jf.jsp'
				},
				onLoadCompleted : function(h) {
					$('<b style="color:red" />').html(
							'The JerichoTab processed in ' + (new Date()
									.getTime() - d1) + ' milliseconds!')
							.appendTo(h);
				}
			} ],
			activeTabIndex :1,			
			loadOnce :true
		});
		
	}
}
/**
 *  从菜单进去的添加TAB
 * @param str 添加对像对应的字符串
 * @return
 */
function addTableItemByStr(str){
	var obj = $(str);
	obj.attr('id',obj.attr('id')+"Node");
	addTabeItem(obj);
}
/**
 * 从菜单进去的添加TAB
 * @param obj
 * @return
 */
function addTabeItem(obj){
	var tabsNum = getTabsNum();
	var tabId = obj.attr('id');
	var menuId = tabId.substring(0,tabId.indexOf('Node', 0));
	//如果页面已到最大数且新开的页面不存在,先关闭最后一个
	if(maxTabs==undefined||maxTabs==0||maxTabs==''){
		maxTabs=$.fn.jerichoTab.maxtabs;
	}
	if(maxTabs == tabsNum && !isExistsTab (menuId)){
		//$.messager.defaults={ok:"确定",cancel:"取消"};
		alert("打开页面数已超过最大限制,请先关闭部分页面后再操作！");
		return false;
	};
	try{
		$.fn.jerichoTab.addTab( {
			tabFirer :obj,
			id : obj.attr('id'),
			title :obj.text(),
			closeable :true,
			iconImg :obj.attr('iconImg'),
			data : {
				dataType :obj.attr('dataType'),
				dataLink :obj.attr('dataLink')
			}
		//}).showLoader().loadData();
		}).loadData();
	}catch(e){
		
		return ;
	}
}
/**
 * 关闭TAB页

 * @param tabId Tab页的ID,与数据库中的MENU_ID保存一致

 * @return
 */
function colseTabe(tabId){
	if(typeof($('#jerichotab_'+tabId+'Node'))!= 'undefined' 
    	&& typeof($('#jerichotab_'+tabId+'Node').attr("id"))!='null'){
		$('#jerichotab_'+tabId+'Node').dblclick();
	}
}  

/**
 * 判断TAB页是否已经打开
 * @param tabId 与数据库中的MENU_ID保存一致
 * @return
 */
function isExistsTab(tabId){
	if (typeof $('#jerichotab_' + tabId+'Node') != 'undefined' && $('#jerichotab_' + tabId+'Node').length > 0) {    	
        return true;  
    }
	return false;
}

/**
 * 获取当前TAB个数(包括初始化的那个)
 * @return
 */
function getTabsNum(){
	tabsNum = $('li[id^="jerichotab_"][id$="Node"]').length+1;
	return tabsNum;
}

/**
 * 重新打开TAB(先关闭再打开)
 * @param obj obj的ID与数据库保存一致,不加"Node"
 * @return
 */
function addAfterClose(obj){	
	var tabId = obj.attr('id');
	if (isExistsTab(tabId)){
		$('#jerichotab_'+tabId+'Node').dblclick();
		setTimeout(add,1000);
	} else {
		add();
	}
	function add(){
		obj.attr('id',tabId+"Node");
		try{
			$.fn.jerichoTab.addTab( {
				tabFirer :obj,
				id : obj.attr('id'),
				title :obj.text(),
				closeable :true,
				iconImg :obj.attr('iconImg'),
				data : {
					dataType :obj.attr('dataType'),
					dataLink :obj.attr('dataLink')
				}
			//}).showLoader().loadData();
			}).loadData();
		}catch(e){		
			return ;
		}
	}
	
}