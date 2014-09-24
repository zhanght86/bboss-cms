/*
 * 1、 记录当前跳转页面
 * 2、 重置页面的保存状态
 * 3、 如果点击了暂存，则不提示，否则提示是否保存
 * 4、 监控页面是否编辑，修改页面保存状态
 * 5、 设计通用的保存方法，根据不同的页面调用对应的页面form的保存方法
 * 6、 完善通用的页面跳转
 * 7、 完善通用跳转时，修改跳转按钮
 * 8、取消按钮，调通用的取消方法,根据不同的页面调用该页面的重置方法
 * 
 * 9、 做一个查询页面--
 * 
 */

var pageType = [ 'applicaiton01', 'applicaiton02', 'applicaiton03', 'applicaiton04', 'applicaiton05' ];
var nextName = ['填写人员信息','配置行程资源','费用划分','子任务分配','流程配置']; 
var pageDone = "";

var currIndex='1'; //当前页面

function pageChoose(index) {
	
	if(currIndex!=index)
	{
		//if(window.confirm('是否保存当前页面'))
		//{
		//	pageSave();
		//}	
	}

	$('#nextStep').html(nextName[index-1]);	
	currIndex = index;
	if (pageDone.indexOf(index) < 0) {
		pageDone += index;
	}
	for (var i = 1; i <= pageType.length; i++) {
		var undo = $('<div class="step_undo"></div>');
		var cur = $('<div class="step_cur"></div>');
		var done = $('<div class="step_done"></div>');

		var p = $("#page_" + i).parent().parent();
		
		var html = p.html();
		alert(html)
		if (p.parent().find("li").size() == 0) {
			p = p.parent();
		}
		var step_name = p.find(".step_name").eq(0);
		var step_no = p.find(".step_no").eq(0);

		p.empty();
		if (i < index) {
			if (pageDone.indexOf(i) < 0) {
				undo.append(step_name);
				undo.append(step_no);
				p.append(undo);
			} else {
				done.append(step_name);
				done.append(step_no);
				p.append(done);
			}

			//step_no.before(done);
		} else if (i == index) {
			cur.append(step_name);
			cur.append(step_no);
			p.append(cur);
			//step_no.before(cur);
		} else if (i > index) {
			p.append(step_name);
			p.append(step_no);
			//step_no.before(step_name);		
		}

	}
	reloadCurrtPage();
}
function reloadCurrtPage()
{
	$("#mainId").load("reception01.html")
}