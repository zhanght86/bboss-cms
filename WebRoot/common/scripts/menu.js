var maxTabs = 0;
function showitem(moduleId) {
	$("#framesetid", window.parent.document).attr("rows", "103,*");
	var cuerrentMenu = $('#' + moduleId);
	cuerrentMenu.parent().find("li").removeClass("on");
	cuerrentMenu.parent().find("span").removeClass("nav_01");
	cuerrentMenu.addClass("on");
	cuerrentMenu.find("span").addClass("nav_01");
	var divwindow = parent.mainFrame.frame1.mainFrame;
	divwindow.$('table').first().show("fast");
	var tabHash = divwindow.tabHash;
	if (typeof tabHash == 'undefined' || tabHash.length <= 0) {
		// 删除首页
		divwindow.$("#indexdiv").remove();
		// 创建TAB DIV
		divwindow.jericho.buildTabpanel(maxTabs);
	}
	$(".bj").children().remove();
	
	$.ajax({
		type : "post",
		dataType : "json",
		data : {
			"moduleId" : moduleId
		},
		url : "menu/queryMenu.page",
		cache : false,
		async : false,// ajax同步标志修改
		error : function() {
			window.parent.locaction="logout.jsp";
			//alert("系统错误，请稍后再试！");
		},
		success : function(data) {
			if (data.length > 0) {
				$.each(data, function(i) {
					var tempdata = this.pathU;
					var span = $("<span style=\"cursor:pointer;\" class=\"func\" id=\""
							+ this.id
							+ "Node"
							+ "\" title=\""
							+ this.name
							+ "\" dataType='iframe' dataLink='"
							+ this.pathU
							+ "' iconImg='images/msn.gif'>"
							+ this.name
							+ "</span>").click(function() {
						divwindow.addTabeItem($(this));
							/*alert(tempdata);
						$("#mainFrame", window.parent.document).attr("src",
								tempdata);*/
						});
					var currentdiv = $("<div style=\"width:100px;float:left;margin-left:5px;\"><div style=\"text-align:center;\"><img src=\""
							+ this.imageUrl
							+ "\" width=\"25\" height=\"25\" style=\"cursor:pointer;\" on></div><div style=\"margin-top:1px;text-align:center;color:#000;\" id=\""
							+ this.id + "\"></div></div>");
					currentdiv.find("img").click(function() {
						currentdiv.find("span").click();
							/*alert("1111" + tempdata);
						$("#mainFrame", window.parent.document).attr("src",
								tempdata);*/
						});
					$(".bj").append(currentdiv);
					$("#" + this.id).append(span);
				});
			}
		}
	});
}