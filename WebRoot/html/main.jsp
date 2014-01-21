<%@ page contentType="text/html; charset=UTF-8"%>
<%@ include file="/common/jsp/importtaglib.jsp"%>
<%@ taglib uri="/WEB-INF/dictionary.tld" prefix="dict"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>三一集团开发平台</title>
<link href="stylesheet/index1.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="../include/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="js/ajaxtabs.js"></script>
<script type="text/javascript" src="../include/js/disablebaskspace.js"></script>

<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lhgdialog.js?self=false"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/html/js/dialog/lan/lhgdialog_<pg:locale/>.js"></script>
</head>
<body>
<div class="index_contain">
    <div  class="side_bar">
    <div class="title_right"><img src="images/function.jpg" width="193" height="26" /></div>
    <div class="links">
      <ul id="custommenus">
        
      </ul>
    </div>
    <div class="link_ad">
      <ul>
        <li><img src="images/competition.jpg" /></li>
        <li><img src="images/recommend.jpg" /></li>
        <li><img src="images/advice.jpg" /></li>
      </ul>
    </div>
  </div>
  <div  class="main_contain">
    <div class="box_1" >
      <div  class="content_top">
        <div class="right_top"></div>
        <div class="left_top"></div>
        <div class="more_info"><a target="_news" href="#" id="more"><img src="images/more.gif" border="0" /></a></div>
        新闻公告</div>
      <div class="box_content">
        <div class="right_content"></div>
        <div class="left_content"></div>
        <div class="content_box">
          <div id="picnews" class="left_ad" style="height:180px;">
          	
          </div>
          <div class="module_contain" >
            <ul id="news">
              <li>
                <div class="date">2010-12-12</div>
                <a href="#">5月27号新增岗位 58 个，包括物流管理等</a></li>
              <li>
                <div class="date">2010-12-12</div>
                <a href="#">关于绩效自评提交后汇报关系发生变化</a></li>
              <li>
                <div class="date">2010-12-12</div>
                <a href="vote.html">3月21日HRM系统beta1.2.2版更新公告</a></li>
              <li>
                <div class="date">2010-12-12</div>
                <a href="#">5月18日HRM系统beta2.1.0版更新公告</a></li>
              <li>
                <div class="date">2010-12-12</div>
                <a href="#">关于绩效自评提交后汇报关系发生变化</a></li>
              <li>
                <div class="date">2010-12-12</div>
                <a href="#">3月21日HRM系统beta1.2.2版更新公告</a></li>
              <li>
                <div class="date">2010-12-12</div>
                <a href="#">3月21日HRM系统beta1.2.2版更新公告</a></li>
            </ul>
          </div>
        </div>
      </div>
      <div  class="content_bottom">
        <div class="right_bottom"></div>
        <div class="left_bottom"></div>
      </div>
    </div>
    <div class="box_1">
      <div  class="content_top">
        <div class="right_top"></div>
        <div class="left_top"></div>
        我的待办<span class="Pending">( <span class="pending_num">25</span>条 )</span> </div>
      <div class="box_content" style="padding:0px;">
        <div class="right_content"></div>
        <div class="left_content"></div>
        <div class="tabs">
          <div id="pettabs" class="indentmenu">
            <ul>
              <li><a href="ad/pending2.html" rel="#iframe">绩效管理<span class="num">(12)</span></a></li>
              <li><a href="ad/pending3.html" rel="#iframe">辅导管理<span class="num">(5)</span></a></li>
              <li><a href="ad/pending5.html" rel="#iframe">福利关怀<span class="num">(1)</span></a></li>
              <li><a href="ad/pending6.html" rel="#iframe">人事事务<span class="num">(0)</span></a></li>
              <li><a href="ad/pending7.html" rel="#iframe">系统消息<span class="num">(2)</span></a></li>
            </ul>
          </div>
          <div id="petsdivcontainer"></div>
        </div>
        <script type="text/javascript">

var mypets=new ddajaxtabs("pettabs", "petsdivcontainer",200)
mypets.setpersist(false)
mypets.setselectedClassTarget("link")
mypets.init()


			
	function addCssByLink(url){  
	    var doc=document;  
	    var link=doc.createElement("link");  
	    link.setAttribute("rel", "stylesheet");  
	    link.setAttribute("type", "text/css");  
	    link.setAttribute("href", url);  
	  
	    var heads = doc.getElementsByTagName("head");  
	    if(heads.length)  
	        heads[0].appendChild(link);  
	    else  
	        doc.documentElement.appendChild(link);  
	}  
		
				
    $(document).ready(function() {
		var url =  '${pageContext.request.contextPath}/desktop/getCustomMenus.page';
		var urltype = '2';
		$.getJSON(url, { urltype:urltype },
            function(data) {   
				 if(data != null)
				{
					var comm = "";
					for (var i=0; i<data.length; i++) {
						var d = data[i];
						
						comm = comm +" <li><a href=\"javascript:void(0)\" onclick=\"opencustom('"+d.pathU+"','"+d.pathPopu+"',"+d.desktop_width+","+d.desktop_height+",'"+d.name+"','"+urltype+"',"+d.option+")\"><img src=\""+d.imageUrl+"\" width=\"45\" height=\"45\" /><br />"+d.name+"</a></li>";
						
					}
					$("#custommenus").empty(); 
             		$("#custommenus").append(comm); 
				}
               
            });
		
		var host = '<dict:itemvalue type="newsservice" itemName="sitehost" defaultItemValue="${pageContext.request.contextPath}"/>';
		if(host.charAt(host.length - 1) != '/')
		{
			host = host + "/";
		}
		var site = '<dict:itemvalue type="newsservice" itemName="site" defaultItemValue="BPIT"/>';
		var newschannel = '<dict:itemvalue type="newsservice" itemName="newschannel" defaultItemValue="news"/>';
		var newscount = '<dict:itemvalue type="newsservice" itemName="newscount" defaultItemValue="7"/>';
		var picnewschannel = '<dict:itemvalue type="newsservice" itemName="picnewschannel" defaultItemValue="importantnews"/>';
		var picnewscount = '<dict:itemvalue type="newsservice" itemName="picnewscount" defaultItemValue="5"/>';
		var getnewsurl =  host + 'document/getNewsList.freepage?jsonp_callback=?';
		
		$.getJSON(getnewsurl, { site:site,channel:newschannel,count:newscount },
	            function(data) {   
					 if(data != null)
					{
						 $("#more").attr("href",data.channelIndex);
						var comm = "";
						var sitedomain = data.sitedomain;
						if(sitedomain.charAt(sitedomain.length - 1) != '/')
						{
							sitedomain = sitedomain + "/";
						}
						for (var i=0; data.news && i<data.news.length; i++) {
							var d = data.news[i];
						
							comm = comm +"  <li>";
							comm = comm +"<div class=\"date\">"+d.docwtime+"</div>";
							//DOCUMENT_AGGRATION = 3;
							if(d.doctype != 3)
							{
								comm = comm +"<a href=\""+d.docpuburl+"\" target='_blank'>";
								if(d.titlecolor =='#000000')
								{
									comm = comm +d.title;
								}
								else
								{
									comm = comm + "<font color=\""+d.titlecolor+"\">" +d.title+"</font>";
								}
								if(d.isNew =='1')
								{
									comm = comm + "<img border=\"0\" src=\""+sitedomain+d.newPicPath+"\"/>";
									
										
										
								}
								comm = comm +"</a>";
							}
							else
							{
								comm = comm +"<Strong>"+d.title +":</Strong>";
								for(var j =0 ; d.compositeDocs && j <　d.compositeDocs.length; j++)
								{
									var dc = d.compositeDocs[j];
									if(j > 0)
										comm = comm +"&nbsp;"
									comm = comm +"<a href=\""+dc.docpuburl+"\" target='_blank'>";
									if(dc.titlecolor =='#000000')
									{
										comm = comm +dc.title;
									}
									else
									{
										comm = comm + "<font color=\""+dc.titlecolor+"\">" +dc.title+"</font>";
									}
									if(dc.isNew =='1')
									{
										comm = comm + "<img border=\"0\" src=\""+sitedomain+dc.newPicPath+"\"/>";
										
											
											
									}
									comm = comm +"</a>";
								}
							}
							
							comm = comm +"</li>";
							
							
						}
						$("#news").empty(); 
	             		$("#news").append(comm); 
					}
	               
	            });
		
		$.getJSON(getnewsurl, { site:site,channel:picnewschannel,count:picnewscount },
            function(data) { 
				var comm = "";
				var sitedomain = data.sitedomain;
				
				if(sitedomain.charAt(sitedomain.length - 1) != '/')
				{
					sitedomain = sitedomain + "/";
				}
				// $("#slide").attr("href",sitedomain + "components/slide/slide-small.css");
				addCssByLink(sitedomain + "components/slide/slide-small.css");
				
               	var arr=new Array();
					var tarr=new Array();
				var ftarr=new Array();
					var slink=new Array();
									
					for (var i=0; data && data.news && i<data.news.length; i++) {
					var d = data.news[i];
					if(d.doctype != 3)
					{
	   					arr.push(sitedomain+d.picPath);
	   					tarr.push(d.subtitle);
						ftarr.push(d.subtitle);								
		 				slink.push(d.docpuburl);
					}
					}
 		    		
					comm = comm + "<table cellspacing=\"0\" cellpadding=\"0\" >";
					comm = comm + "<tbody>";
					comm = comm + "  <tr>";
					comm = comm + "    <td id=\"bimg\" class=\"pic\">";
					comm = comm + "      <div id=\"top_pictures\">";
                         		
          			for (var i=0; i<arr.length; i++) {
          				if (i == 0) {
          					comm = comm + '<div style="" id="top_picture_image_'+i+'" class="dis">';
          				} else {
          					comm = comm + '<div style="display: none;" id="top_picture_image_'+i+'" class="dis">';
          				}
          				comm = comm + '<a title="'+tarr[i]+'" target="_blank" href="'+slink[i]+'"><img src="'+arr[i]+'" alt="'+ftarr[i]+'" ></a>';
          				comm = comm + '</div>';
          			}
                         		
                    comm = comm + "      </div>";
                    comm = comm + "     <table cellspacing=\"0\" cellpadding=\"0\" id=\"font_hd\">";
                    comm = comm + "       <tbody>";
                    comm = comm + "         <tr>";
                    comm = comm + "           <td id=\"info\" class=\"lkff\">";
                               	
                	for (var i=0; i<arr.length; i++) {
                		if (i == 0) {
                			comm = comm + '<div style="" id="top_picture_title_'+i+'" class="dis" >';
          				} else {
          					comm = comm + '<div style="display: none;" id="top_picture_title_'+i+'" class="dis" >';
          				}
                		comm = comm + '<a title="'+tarr[i]+'" target="_blank" href="'+slink[i]+'">'+tarr[i]+'</a>';
                		comm = comm + '</div>';
          			}
                               	
                  comm = comm + "              </td>";
                  comm = comm + "             <td id=\"simg\">";
                  comm = comm + "               <div id=\"simg-wrap\">";
                                 
                                 		for (var i=0; i<arr.length; i++) {
                               			if (i == 0) {
                               				comm = comm + '<div id="top_picture_button_'+i+'" class="dis">'+(i+1)+'</div>';
                         					} else {
                         						comm = comm + '<div id="top_picture_button_'+i+'" class="dis  f1">'+(i+1)+'</div>';
                         					}
                         				}
                                 	
               comm = comm + "                  </div>";
               comm = comm + "                </td>";
               comm = comm + "              </tr>";
               comm = comm + "            </tbody>";
               comm = comm + "          </table>";
               comm = comm + "        </td>";
               comm = comm + "      </tr>";
               comm = comm + "    </tbody>";
               comm = comm + "  </table>";
                
               $("#picnews").empty(); 
        		$("#picnews").append(comm); 
              
               (function(){
                  var total = arr.length;
                  var current = 0;
                  var auto_play_on = true;

                  $('#bimg').mouseover(function() {auto_play_on = false;});
                  $('#bimg').mouseout(function() {auto_play_on = true;});

                  var showPicture = function(index) {
                    if (current !== index) {
                      $('#top_picture_image_' + current).fadeOut('slow');
                      $('#top_picture_image_' + index).fadeIn('slow');
                      $('#top_picture_title_' + current).hide();
                      $('#top_picture_title_' + index).show();
                      $('#top_picture_button_' + current).addClass('f1');
                      $('#top_picture_button_' + index).removeClass('f1');
                      current = index;
                    }
                  };

                  for (var i = 0; i < total; i++) {
                    $('#top_picture_button_' + i).click((function(i) {
                      			return function(ev) {
                        				showPicture(i);
                      			}
                      		})
                      		(i)
                      	);
                  };

                  var autoPlay = function() {
                    if (total !== 0 && auto_play_on) {
                      var next = (current < (total - 1)) ? (current + 1) : 0;
                      showPicture(next);
                    }
                  };
                  setInterval(autoPlay, 5000);
                })();
           });
			
	 });
	 
	 
	 

	 function opencustom(url,popurl,width,height,title,urltype,options)
	 {
		 
		 var ppup = options?options.popup:false;
		 var maxstate = options ?(options.maxstate === true || options.maxstate === false?options.maxstate :true):true;
		 var width = options && options.width ?options.width:777;
		 var height = options && options.height ?options.height:500;
		
		 if(ppup)
			$.dialog({ title:title,width:width,height:height, content:'url:'+popurl,lock: true,maxState:maxstate}); 	
		 else
		 {
			 top.location.href = url;
		 }
	 }


</script>
      </div>
      <div  class="content_bottom">
        <div class="right_bottom2"></div>
        <div class="left_bottom2"></div>
      </div>
    </div>
  </div>
</div>

</body>
</html>
