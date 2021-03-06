$(function(){
	myLib.progressBar();
});
$.include(['css/desktop.css', 'css/jquery-ui-1.8.21.custom.css', 'css/smartMenu.css' , 'js/jquery-ui-1.8.21.custom.js', 'js/jquery.winResize.js', 'js/jquery-smartMenu-min.js', 'js/desktop.js']);
$(window).load(function(){
		   myLib.stopProgress();
		   var lrBarIconData={
			   'app0':{
					   'title':'jsfoot网页特效',
					   'url':'http://www.jsfoot.com/',
					   'winWidth':1100,
					   'winHeight':650
					   },
				'app1':{
					   'title':'jQuery特效',
					   'url':'http://www.jsfoot.com/jquery/',
					   'winWidth':1100,
					   'winHeight':650
					   },
				'app2':{
					   'title':'javascript特效',
					   'url':'http://www.jsfoot.com/js/',
					   'winWidth':1100,
					   'winHeight':650
					   },
				'app3':{
					   'title':'资源分享',
					   'url':'ziyuan.html',
					   'winWidth':1100,
					   'winHeight':650
					   }
					   };
		  var deskIconData={
			        'kuwoMusic':{
					   'title':'酷我音乐盒',
					   'url':'http://mbox.kuwo.cn/',
					   'winWidth':950,
					   'winHeight':500
						},
					'hudong':{
					   'title':'互动百科',
					   'url':'http://lab.hudong.com/webqq/index.html',
					   'winWidth':950,
					   'winHeight':500
						},
					'dubianFim':{
					   'title':'豆瓣FIM',
					   'url':'http://douban.fm/partner/qq_plus',
					   'winWidth':550,
					   'winHeight':480
						},
					'Pixlr':{
					   'title':'Pixlr',
					   'url':'http://pixlr.com/editor/?loc=zh-cn',
					   'winWidth':942,
					   'winHeight':547
						},
					'qidian':{
					   'title':'起点中文',
					   'url':'http://webqq.qidian.com',
					   'winWidth':942,
					   'winHeight':547
						},
					'leshiwang':{
					   'title':'乐视网',
					   'url':'http://www.letv.com/cooperation/qq.html',
					   'winWidth':842,
					   'winHeight':547
						},
					'qianqianMusic':{
					   'title':'千千音乐',
					   'url':'http://www.qianqian.com/paihang.html',
					   'winWidth':930,
					   'winHeight':500
						},
					'zfMeishi':{
					   'title':'主妇美食',
					   'url':'http://www.zhms.cn/',
					   'winWidth':930,
					   'winHeight':500
						},
					'mglvyou':{
						'title':'芒果旅游',
					   'url':'http://www.mangocity.com/webqq/bookFlight.html',
					   'winWidth':930,
					   'winHeight':500
						},	
					'taobao':{
						'title':'淘宝网',
					   'url':'http://marketing.taobao.com/home/webqq/index.htm',
					   'winWidth':930,
					   'winHeight':500
						},	
					'qingshu':{
						'title':'情书',
					   'url':'http://www.qingshu8.net/',
					   'winWidth':930,
					   'winHeight':500
						},
					'fenghuang':{
						'title':'凤凰网',
					   'url':'http://www.ifeng.com/',
					   'winWidth':930,
					   'winHeight':500
						},	
					'zhongguancun':{
						'title':'中关村在线',
					   'url':'http://www.zol.com.cn/',
					   'winWidth':930,
					   'winHeight':500
						},
					'win35':{
						'title':'搜狐汽车',
					   'url':'http://auto.sohu.com/',
					   'winWidth':930,
					   'winHeight':500
						},	
					'win36':{
						'title':'布丁电影票',
					   'url':'http://piao.buding.cn/',
					   'winWidth':900,
					   'winHeight':500
						},	
					'win37':{
						'title':'中国数学资源网',
					   'url':'http://www.mathrs.net/',
					   'winWidth':930,
					   'winHeight':500
						},
					'win38':{
						'title':'火影忍者漫画动画',
					   'url':'http://www.manmankan.com/',
					   'winWidth':930,
					   'winHeight':500
						},	
					'win39':{
						'title':'潇湘书院',
					   'url':'http://www.xxsy.net/',
					   'winWidth':930,
					   'winHeight':500
						}  
			  };			   
 		   
		  //存储桌面布局元素的jquery对象
		   myLib.desktop.desktopPanel();
 		   
		   //初始化桌面背景
		   myLib.desktop.wallpaper.init("images/blue_glow.jpg");
		   
		   //初始化任务栏
		   myLib.desktop.taskBar.init();
		   
		   //初始化桌面图标
		   myLib.desktop.deskIcon.init(deskIconData);
		   
		   //初始化桌面导航栏
		   myLib.desktop.navBar.init();
		   
		   //初始化侧边栏
		   myLib.desktop.lrBar.init(lrBarIconData);
		   

		   //欢迎窗口
		   myLib.desktop.win.newWin({
			 WindowTitle:'欢迎窗口',
			 iframSrc:"welcome.html",
			 WindowsId:"welcome",
			 WindowAnimation:'none', 
			 WindowWidth:740,
			 WindowHeight:520
			 });
  		  
		  });