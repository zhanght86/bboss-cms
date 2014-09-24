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
	$('#feedback_acc').dialog({ id:'iframeId',title:'资产批量确认',min: false,max: false,content: '您已经确认个人资产基本信息。',cover:true });
});
$(function(){	
	$('#msg').dialog({ id:'iframeId',title:'填写注意事项',  
    content: '欢迎使用lhgdialog窗口!', 
    width: 200, 
    height: 100, 
    fixed: true, 
    drag: false, 
    resize: false 
 ,cover:true });
});
$(function(){	
	$('#details').dialog({  id:'iframeId',  max: false,
	width: '700px', height: '400px',
    min: false,
	title:'会议室详情',
    lock: true, 
    content: 'url:dialog/conference_detail.html',
	okVal: '确定',
	parent:this,
    ok: function () { 
        /* 这里要注意多层锁屏一定要加parent参数 */ 
        $.dialog({ icon: 'success.gif',title:'提交成功',content: '添加成功', lock: true,  parent:this,
       ok: true, cancelVal: '关闭', cancel: function(){ 

    } 
    }); 
    },  
	cancelVal: '关闭', 
    cancel: true  });
	});
$(function(){	
	$('#hvisited').dialog({  id:'iframeId',  max: false,
	width: '700px', height: '400px',
    min: false,
	title:'历史访问信息',
    lock: true, 
    content: 'url:dialog/history_visited.html',
	okVal: '确定为来访人员',
	parent:this,
    ok: function () { 
        /* 这里要注意多层锁屏一定要加parent参数 */ 
        $.dialog({ icon: 'success.gif',title:'提交成功',content: '添加成功', lock: true,  parent:this,
       ok: true, cancelVal: '关闭', cancel: function(){ 

    } 
    }); 
    },  
	cancelVal: '关闭', 
    cancel: true  });
	});
$(function(){	
	$('#vcompany').dialog({  id:'iframeId',  max: false,
	width: '800px', height: '650px',
    min: false,
	title:'维护来访单位',
    lock: true, 
    content: 'url:dialog/visited_company.html',
	okVal: '确定',
	parent:this,
    ok: function () { 
        /* 这里要注意多层锁屏一定要加parent参数 */ 
        $.dialog({ icon: 'success.gif',title:'提交成功',content: '提交成功', lock: true,  parent:this,
       ok: true, cancelVal: '关闭', cancel: function(){ 

    } 
    }); 
    },  
	cancelVal: '关闭', 
    cancel: true  });
	});

$(function(){	
	$('#rperson').dialog({  id:'iframeId',  max: false,
	width: '720px', height: '460px',
    min: false,
	title:'新增来访人员',
    lock: true, 
    content: 'url:dialog/add_reception_person.html',
	okVal: '新增',
	parent:this,
    ok: function () { 
        /* 这里要注意多层锁屏一定要加parent参数 */ 
        $.dialog({ icon: 'success.gif',title:'新增成功',content: '提交成功', lock: true,  parent:this,
       ok: true, cancelVal: '关闭', cancel: function(){ 

    } 
    }); 
    },  
	cancelVal: '关闭', 
    cancel: true  });
	});


$(function(){	
	$('#mperson').dialog({  id:'iframeId',  max: false,
	width: '720px', height: '130px',
    min: false,
	title:'请导入人员信息',
    lock: true, 
    content: 'url:dialog/import_person.html',
	okVal: '提交',
	parent:this,
    ok: function () { 
        /* 这里要注意多层锁屏一定要加parent参数 */ 
        $.dialog({ icon: 'success.gif',title:'提交成功',content: '已提交成功。', lock: true,  parent:this,
       ok: true, cancelVal: '关闭', cancel: function(){ 

    } 
    }); 
    },  
	cancelVal: '关闭', 
    cancel: true  });
	});
$(function(){	
	$('#car').dialog({  id:'iframeId',  max: false,
	width: '700px', height: '265px',
    min: false,
	title:'新增车辆',
    lock: true, 
    content: 'url:dialog/car.html',
	okVal: '新增',
	parent:this,
    ok: function () { 
        /* 这里要注意多层锁屏一定要加parent参数 */ 
        $.dialog({ icon: 'success.gif',title:'新增成功',content: '新增成功', lock: true,  parent:this,
       ok: true, cancelVal: '关闭', cancel: function(){ 

    } 
    }); 
    },  
	cancelVal: '关闭', 
    cancel: true  });
	});
$(function(){	
	$('#leader').dialog({  id:'iframeId',  max: false,
	width: '700px', height: '220px',
    min: false,
	title:'新增预约领导',
    lock: true, 
    content: 'url:dialog/leader.html',
	okVal: '新增',
	parent:this,
    ok: function () { 
        /* 这里要注意多层锁屏一定要加parent参数 */ 
        $.dialog({ icon: 'success.gif',title:'新增成功',content: '新增成功', lock: true,  parent:this,
       ok: true, cancelVal: '关闭', cancel: function(){ 

    } 
    }); 
    },  
	cancelVal: '关闭', 
    cancel: true  });
	});
$(function(){	
	$('#expert').dialog({  id:'iframeId',  max: false,
	width: '700px', height: '220px',
    min: false,
	title:'新增预约技术专家',
    lock: true, 
    content: 'url:dialog/expert.html',
	okVal: '新增',
	parent:this,
    ok: function () { 
        /* 这里要注意多层锁屏一定要加parent参数 */ 
        $.dialog({ icon: 'success.gif',title:'新增成功',content: '新增成功', lock: true,  parent:this,
       ok: true, cancelVal: '关闭', cancel: function(){ 

    } 
    }); 
    },  
	cancelVal: '关闭', 
    cancel: true  });
	});
$(function(){	
	$('#souvenir').dialog({  id:'iframeId',  max: false,
	width: '700px', height: '220px',
    min: false,
	title:'新增纪念品',
    lock: true, 
    content: 'url:dialog/souvenir.html',
	okVal: '新增',
	parent:this,
    ok: function () { 
        /* 这里要注意多层锁屏一定要加parent参数 */ 
        $.dialog({ icon: 'success.gif',title:'新增成功',content: '新增成功', lock: true,  parent:this,
       ok: true, cancelVal: '关闭', cancel: function(){ 

    } 
    }); 
    },  
	cancelVal: '关闭', 
    cancel: true  });
	});
$(function(){	
	$('#camera').dialog({  id:'iframeId',  max: false,
	width: '700px', height: '220px',
    min: false,
	title:'新增拍照/摄像',
    lock: true, 
    content: 'url:dialog/camera.html',
	okVal: '新增',
	parent:this,
    ok: function () { 
        /* 这里要注意多层锁屏一定要加parent参数 */ 
        $.dialog({ icon: 'success.gif',title:'新增成功',content: '新增成功', lock: true,  parent:this,
       ok: true, cancelVal: '关闭', cancel: function(){ 

    } 
    }); 
    },  
	cancelVal: '关闭', 
    cancel: true  });
	});
$(function(){	
	$('#reporter').dialog({  id:'iframeId',  max: false,
	width: '700px', height: '220px',
    min: false,
	title:'新增记者',
    lock: true, 
    content: 'url:dialog/reporter.html',
	okVal: '新增',
	parent:this,
    ok: function () { 
        /* 这里要注意多层锁屏一定要加parent参数 */ 
        $.dialog({ icon: 'success.gif',title:'新增成功',content: '新增成功', lock: true,  parent:this,
       ok: true, cancelVal: '关闭', cancel: function(){ 

    } 
    }); 
    },  
	cancelVal: '关闭', 
    cancel: true  });
	});
$(function(){	
	$('#add_cost').dialog({  id:'iframeId',  max: false,
	width: '600px', height: '135px',
    min: false,
	title:'费用划分',
    lock: true, 
    content: 'url:dialog/add_cost.html',
	okVal: '提交',
	parent:this,
    ok: function () { 
        /* 这里要注意多层锁屏一定要加parent参数 */ 
        $.dialog({ icon: 'success.gif',title:'提交成功',content: '处理成功', lock: true,  parent:this,
       ok: true, cancelVal: '关闭', cancel: function(){ 

    } 
    }); 
    },  
	cancelVal: '关闭', 
    cancel: true  });
	});
$(function(){	
	$('#distribution').dialog({  id:'iframeId',  max: false,
	width: '700px', height: '265px',
    min: false,
	title:'分配责任人',
    lock: true, 
    content: 'url:dialog/distribution.html',
	okVal: '提交',
	parent:this,
    ok: function () { 
        /* 这里要注意多层锁屏一定要加parent参数 */ 
        $.dialog({ icon: 'success.gif',title:'提交成功',content: '提交成功', lock: true,  parent:this,
       ok: true, cancelVal: '关闭', cancel: function(){ 

    } 
    }); 
    },  
	cancelVal: '关闭', 
    cancel: true  });
	});
$(function(){	
	$('#add_company').dialog({  id:'iframeId',  max: false,
	width: '720px', height: '380px',
    min: false,
	title:'新增来访单位',
    lock: true, 
    content: 'url:dialog/add_company.html',
	okVal: '确定',
	parent:this,
    ok: function () { 
        /* 这里要注意多层锁屏一定要加parent参数 */ 
        $.dialog({ icon: 'success.gif',title:'提交成功',content: '提交成功', lock: true,  parent:this,
       ok: true, cancelVal: '关闭', cancel: function(){ 

    } 
    }); 
    },  
	cancelVal: '关闭', 
    cancel: true  });
	});
$(function(){	
	$('#menuChoose').dialog({  id:'iframeId',  max: false,
	width: '800px', height: '560px',
    min: false,
	title:'点菜',
    lock: true, 
    content: 'url:dialog/add_catering_menu.html',
	//okVal: '',
	//parent:this,
    /*ok: function () { 
        /* 这里要注意多层锁屏一定要加parent参数 */ 
     /*  $.dialog({ icon: 'success.gif',title:'提交成功',content: '提交成功', lock: true,  parent:this,
       ok: true, cancelVal: '关闭', cancel: function(){ 

    } 
    }); 
    }, */ 
	//cancelVal: '关闭', 
    //cancel: true  
	});
	});
$(function(){	
	$('#materingMenu').dialog({  id:'iframeId',  max: false,
	width: '1000px', height: '600px',
    min: false,
	title:'我要点菜',
    lock: true, 
    content: 'url:dialog/catering_menu.html',
	okVal: '确定',
	//parent:this,
    ok: function () { 
        /* 这里要注意多层锁屏一定要加parent参数 */ 
        $.dialog({ icon: 'success.gif',title:'提交成功',content: '提交成功', lock: true,  parent:this,
       ok: true, cancelVal: '关闭', cancel: function(){ 

    } 
    }); 
    },  
	cancelVal: '关闭', 
    cancel: true  });
	});
$(function(){	
	$('#materingPlans').dialog({  id:'iframeId',  max: false,
	width: '1000px', height: '600px',
    min: false,
	title:'我要套餐',
    lock: true, 
    content: 'url:dialog/catering_plans.html',
	okVal: '确定',
	//parent:this,
    ok: function () { 
        /* 这里要注意多层锁屏一定要加parent参数 */ 
        $.dialog({ icon: 'success.gif',title:'提交成功',content: '提交成功', lock: true,  parent:this,
       ok: true, cancelVal: '关闭', cancel: function(){ 

    } 
    }); 
    },  
	cancelVal: '关闭', 
    cancel: true  });
	});
$(function(){	
	$('#sousuo').dialog({  id:'iframeId',  max: false,
	width: '450px', height: '150px',
    min: false,
	title:'搜索订单',
    lock: true, 
    content: 'url:dialog/search_meal.html',
	okVal: '确定',
	});
	});
	
$(function(){	
	$('#jiesuan').dialog({  id:'iframeId',  max: false,
	width: '680px', height: '360px',
    min: false,
	title:'结算',
    lock: true, 
    content: 'url:dialog/jiesuan.html',
	okVal: '确定',
	});
	});


