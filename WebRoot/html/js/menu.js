$.fn.bgiframe = ($.browser.msie && /msie 6\.0/i.test(navigator.userAgent) ? function(s) {
    s = $.extend({
        top     : 'auto', // auto == .currentStyle.borderTopWidth
        left    : 'auto', // auto == .currentStyle.borderLeftWidth
        width   : 'auto', // auto == offsetWidth
        height  : 'auto', // auto == offsetHeight
        opacity : true,
        src     : 'javascript:false;'
    }, s);
    var html = '<iframe class="bgiframe"frameborder="0"tabindex="-1"src="'+s.src+'"'+
                   'style="display:block;position:absolute;z-index:-1;'+
                       (s.opacity !== false?'filter:Alpha(Opacity=\'0\');':'')+
                       'top:'+(s.top=='auto'?'expression(((parseInt(this.parentNode.currentStyle.borderTopWidth)||0)*-1)+\'px\')':prop(s.top))+';'+
                       'left:'+(s.left=='auto'?'expression(((parseInt(this.parentNode.currentStyle.borderLeftWidth)||0)*-1)+\'px\')':prop(s.left))+';'+
                       'width:'+(s.width=='auto'?'expression(this.parentNode.offsetWidth+\'px\')':prop(s.width))+';'+
                       'height:'+(s.height=='auto'?'expression(this.parentNode.offsetHeight+\'px\')':prop(s.height))+';'+
                '"/>';
    return this.each(function() {
        if ( $(this).children('iframe.bgiframe').length === 0 )
            this.insertBefore( document.createElement(html), this.firstChild );
    });
} : function() { return this; });

// old alias
$.fn.bgIframe = $.fn.bgiframe;

function prop(n) {
    return n && n.constructor === Number ? n + 'px' : n;
}

var ddsmoothmenu={
arrowimages: {down:['downarrowclass', '../html/images/down.gif', 23], right:['rightarrowclass', '../html/images/ndot1.gif']},

transition: {overtime:0, outtime:0}, //duration of slide in/ out animation, in milliseconds
shadow: {enable:true, offsetx:5, offsety:5},

detectwebkit: navigator.userAgent.toLowerCase().indexOf("applewebkit")!=-1, //detect WebKit browsers (Safari, Chrome etc)
detectie6: document.all && !window.XMLHttpRequest,

getajaxmenu:function($, setting){ //function to fetch external page containing the panel DIVs
	var $menucontainer=$('#'+setting.contentsource[0]); //reference empty div on page that will hold menu
	$menucontainer.html("Loading Menu...");
	$.ajax({
		url: setting.contentsource[1], //path to external menu file
		async: true,
		error:function(ajaxrequest){
			$menucontainer.html('Error fetching content. Server Response: '+ajaxrequest.responseText);
		},
		success:function(content){
			$menucontainer.html(content);
			ddsmoothmenu.buildmenu($, setting);
		}
	});
},

buildmenu:function($, setting){
	var smoothmenu=ddsmoothmenu;
	var $mainmenu=$("#"+setting.mainmenuid+">ul");//reference main menu UL
	if($mainmenu.parent() && $mainmenu.parent().get(0))
	{
		$mainmenu.parent().get(0).className=setting.classname || "ddsmoothmenu";
	}
	var $headers=$mainmenu.find("ul").parent();
	$headers.hover(
		function(e){
			$(this).children('a:eq(0)').addClass('selected');
		},
		function(e){
			$(this).children('a:eq(0)').removeClass('selected');
		}
	);
	$headers.each(function(i){ //loop through each LI header
		var $curobj=$(this).css({zIndex: 100-i}); //reference current LI header
		var $subul=$(this).find('ul:eq(0)').css({display:'block'});
		this._dimensions={w:this.offsetWidth, h:this.offsetHeight, subulw:$subul.outerWidth(), subulh:$subul.outerHeight()};
		this.istopheader=$curobj.parents("ul").length==1? true : false; //is top level header?
		
		$subul.css({top:this.istopheader && setting.orientation!='v'? this._dimensions.h+"px" : 0});
		
		if(!this.istopheader && setting.orientation!='v'){
			$curobj.children("a:eq(0)").append(
				'<img src="'+ smoothmenu.arrowimages.right[1]				
				+'" class="' + 
				 smoothmenu.arrowimages.right[0]
				+ '" style="border:0;" />'								   
			);
		}
		$curobj.hover(
			function(e){
				var $targetul=$(this).children("ul:eq(0)");
				this._offsets={left:$(this).offset().left, top:$(this).offset().top};
				var menuleft=this.istopheader && setting.orientation!='v'? 0 : this._dimensions.w;
				menuleft=(this._offsets.left+menuleft+this._dimensions.subulw>$(window).width())? (this.istopheader && setting.orientation!='v'? -this._dimensions.subulw+this._dimensions.w : -this._dimensions.w) : menuleft; //calculate this sub menu's offsets from its parent
				if ($targetul.queue().length<=1){ //if 1 or less queued animations
					$targetul.css({left:menuleft+"px", width:this._dimensions.subulw+'px'}).animate({height:'show'}, ddsmoothmenu.transition.overtime);
				}
			},
			function(e){
				var $targetul=$(this).children("ul:eq(0)");
				$targetul.animate({height:'hide'}, ddsmoothmenu.transition.outtime);
			}
		); //end hover
	}); //end $headers.each()
	$mainmenu.find("ul").css({display:'none', visibility:'visible'});
},

init:function(setting){
	if (typeof setting.customtheme=="object" && setting.customtheme.length==2){ //override default menu colors (default/hover) with custom set?
		var mainmenuid='#'+setting.mainmenuid;
		var mainselector=(setting.orientation=="v")? mainmenuid : mainmenuid+', '+mainmenuid;
		document.write('<style type="text/css">\n'
			+mainselector+' ul li a {background:'+setting.customtheme[0]+';}\n'
			+mainmenuid+' ul li a:hover {background:'+setting.customtheme[1]+';}\n'
		+'</style>');
	}
	//this.shadow.enable=(document.all && !window.XMLHttpRequest)? false : this.shadow.enable //in IE6, always disable shadow
	this.shadow.enable=false;
	
	jQuery(document).ready(function($){ //ajax menu?
		$('#menubar ul ul').bgiframe(); 
		if (typeof setting.contentsource=="object"){ //if external ajax menu			
			ddsmoothmenu.getajaxmenu($, setting);
		}
		else{ //else if markup menu		
			ddsmoothmenu.buildmenu($, setting);
		}
		
	});
}

}; //end ddsmoothmenu variable

ddsmoothmenu.init({
mainmenuid: "menubar", //menu DIV id
orientation: 'h', //Horizontal or vertical menu: Set to "h" or "v"
classname: 'ddsmoothmenu', //class added to menu's outer DIV
//customtheme: ["#BFDBFF", "#3783E3"],
contentsource: "markup" //"markup" or ["container_id", "path_to_menu_file"]
});

var sany_old_menuid = "anchor_publicitem";

function navto_sany_MenuItem(tokenurl,menuId,url,target)
{
	pageCounterDetail(appName_log,url.split('?')[0],userName_log,"");//参数：1.系统名称，2.功能的url，3.用户名，4.功能编码
	$.ajax({url:tokenurl, 
		type: "POST",
		success : function(token){
			if(token )
			{
				if(url.indexOf("?") > 0)
				{
					url = url + "&"+token;
				}
				else
				{
					url = url + "?"+token;
				}
			}
			var $targetul=$("#anchor_"+menuId).parent().children("ul:eq(0)");
				
				$targetul.animate({height:'hide'}, ddsmoothmenu.transition.outtime);
			$('#'+target).attr("src",url);
			
			if(sany_old_menuid == "anchor_"+menuId)
			{
				
				
				
				return;
			}
			else
			{
				$('#'+sany_old_menuid).attr("class","");
				sany_old_menuid = 	"anchor_"+menuId;
				$('#'+sany_old_menuid).attr("class","select");
				
			}	
		}
	});		
}

function navto_sany_MenuItem_window(tokenurl,menutitle,menuId,menupath,contextpath,target)
{

	$.ajax({url:tokenurl, 
		type: "POST",
		success : function(token){
			var url = contextpath +"/sanydesktop/index.page?sany_menupath=" + menupath + "&sanyselectedmodule="+menuId ;
			if(token )
			{
				url = url + "&"+token;
			}
			if(sany_old_menuid == "anchor_"+menuId)
			{
				//$.dialog({ title:menutitle,width:740,height:560, content:'url:'+url,lock: true});
				window.open(url);
				return;
			}
			else
			{
				$('#'+sany_old_menuid).attr("class","");
				sany_old_menuid = 	"anchor_"+menuId;
				$('#'+sany_old_menuid).attr("class","select");
				//$.dialog({ title:menutitle,width:740,height:560, content:'url:'+url,lock: true});
				window.open(url);	
			}	
		}
	});
	 	
			
}

function leftnavto_sany_MenuItem(tokenurl,url,target)
{
	pageCounterDetail(appName_log,url.split('?')[0],userName_log,"");//参数：1.系统名称，2.功能的url，3.用户名，4.功能编码
	//"../token/getToken.freepage"
	$.ajax({url:tokenurl, //指定申请令牌的url
			type: "POST",
			success : function(token){//成功申请令牌，token格式为_dt_token_=1518435257
				if(token )//将令牌作为请求参数附加到url中
				{
					if(url.indexOf("?") > 0)
					{
						url = url + "&"+token;
					}
					else
					{
						url = url + "?"+token;
					}
				}
				$('#'+target).attr("src",url);//打开带令牌的url对应的页面
			}
	});
}
//以下代码是拿到用户名和系统名称
var js_log = document.getElementsByTagName("script");  
var userName_log ;
var appName_log ;
for (var i = 0; i < js_log.length; i++) {  
    if (js_log[i].src.indexOf("menu.js") >= 0) {  
        var arraytemp = new Array();  
        arraytemp = js_log[i].src.split('?');  
        arraytemp = arraytemp[1].split('&');
        var param_0 = arraytemp[0].split('=');
        var param_1 = arraytemp[1].split('=');
        
        appName_log = param_0[1];
        userName_log = param_1[1];
        if(arraytemp.length == 3)
        {
	        var param_2 = arraytemp[2].split('=');
	        sany_old_menuid = "anchor_"+param_2[1];
        }
    }  
}  
