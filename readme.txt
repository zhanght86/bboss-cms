o 内容管理修复严重bug
当频道切换模板偶尔会出现发布无法按照新的模板进行发布的问题原因查明：
每个频道的发布时缓存的模板时间戳机制，以临时文件路径为key，模板时间戳为值，所用的临时文件的路径是直接根据频道
发布首页存放的路径+频道首页名称，并没有包含模板id，所以不管怎么切换模板，这个临时文件的路径不会改变，检测
模板有没有发生变化，有发生变化时才重新生成临时文件，按道理讲切换模板后，前后两个模板文件的时间戳会不一样，但是
在linux环境下却出现了一样的情况导致临时文件没有按照新的模板重新生成。

解决办法：修改key的机制为【首页存放的路径+频道首页名称+"_" +模板id】，问题得到解决

o 内网宣传平台  
todolist：
1.评论顺序问题(ok)
2.访问页面来源不准确(ok)
3.定时统计没起作用
4.点播此时统计优化-播放时才计数
5.将视屏播放页面拉入访问统计中(ok)
6.将模板中的publishedtime统一改为docwtime(ok)
7.专题首页、首页高度问题（ok）
8.视屏点击表删除表外键并添加站点、频道、文档字段索引，删除浏览统计表外键并添加站点、频道、文档字段索引，解决回收站中已删除文档无法清空的问题
9.视屏和新闻列表中出现已经删除的文档
/SanyPDP/src-cms/com/frameworkset/platform/cms/documentmanager/document.xml
10.首页图片新闻banner提示不对
/SanyPDP/WebRoot/cms/siteResource/sanyIPP/_template/index.html
11.新建站点时，组件包没有解开
ALTER TABLE TD_CMS_VIDIO_HITS
 DROP CONSTRAINT TD_CMS_VIDIO_HITS_FK_1;

ALTER TABLE TD_CMS_VIDIO_HITS
 DROP CONSTRAINT TD_CMS_VIDIO_HITS_FK_2;

ALTER TABLE TD_CMS_VIDIO_HITS
 DROP CONSTRAINT TD_CMS_VIDIO_HITS_FK_3;

create index vhits_docid on TD_CMS_VIDIO_HITS(doc_id)
create index vhits_channelid on TD_CMS_VIDIO_HITS(CHANNEL_ID)
create index vhits_siteid on TD_CMS_VIDIO_HITS(SITE_ID)

ALTER TABLE TD_CMS_BROWSER_COUNTS
 DROP CONSTRAINT TD_CMS_BROWSER_COUNTS_FK_1;

ALTER TABLE TD_CMS_BROWSER_COUNTS
 DROP CONSTRAINT TD_CMS_BROWSER_COUNTS_FK_2;

ALTER TABLE TD_CMS_BROWSER_COUNTS
 DROP CONSTRAINT TD_CMS_BROWSER_COUNTS_FK_3;
 
 create index brwcounts_docid on TD_CMS_BROWSER_COUNTS(doc_id)
 create index brwcounts_channelid on TD_CMS_BROWSER_COUNTS(CHANNEL_ID)
 create index brwcounts_siteid on TD_CMS_BROWSER_COUNTS(SITE_ID)
12.文档相关附件处理由jspsmartupload切换为common upload机制，解决参数和文件件名中文乱码问题
o 单独打开菜单(不带一级导航条和top页面)的方法：
contextpath + "sanydesktop/frame.page?sany_menupath=dp::menu://sysmenu$root/products$module/xxxx$item"
contextpath + "sanydesktop/webframe.page?sany_menupath=dp::menu://sysmenu$root/products$module/xxxx$item"
o 单独打开菜单的方法：
contextpath + "sanydesktop/index.page?sany_menupath=dp::menu://sysmenu$root/products$module/xxxx$item"
contextpath + "sanydesktop/webindex.page?sany_menupath=dp::menu://sysmenu$root/products$module/xxxx$item"

o 单独打开菜单，并且指定打开url的方法
contextpath + "sanydesktop/index.page?sany_menupath=dp::menu://sysmenu$root/products$module/xxxx$item&sany_selecturl=/xxx.page?aaa=xxx"
contextpath + "sanydesktop/webindex.page?sany_menupath=dp::menu://sysmenu$root/products$module/xxxx$item&sany_selecturl=/xxx.page?aaa=xxx"

http://localhost:8081/smc-desktop/sanydesktop/webindex.page?sany_menupath=dp::menu://sysmenu$root/products$module/machine$item&sany_selecturl=%2Fsanydps%2Fnews%2FshowProductMainNews.page%3Fcid%3D38
这里需要对参数&sany_selecturl=/xxx.page?aaa=xxx的值进行url编码，否则无法正常解析,编码方法为：
java.net.URLEncoder.encode("/xxx.page?aaa=xxx");
o module元素增加url属性用来指定打开并点击模块时，默认打开的工作区域地址


alter table td_sm_user add index idx_username(user_name);
o 增加子系统配置注销调整页面的方法 
<subsystem name="内容管理" i18n:en_US="Content Manager" id="cms" template="cms" module="module-content.xml"
	 	logoutredirect="/login.jsp"
o module.xml文件中item元素添加area属性的原因
平台老模块（系统管理，字典管理）中的item需要设置area="main"属性，其他模块不需要
o 构建脚本完善
o 增加左侧菜单标签
增加module和item增加showleftmenu属性，具备传递性，子元素没有指定则继承父元素的属性值,子元素覆盖父元素值
sysmeny元素增加showrootleftmenu属性，具备传递性，子元素没有指定则继承父元素的属性值
module.xml中配置左侧菜单：
配置个性化菜单组全部出现左侧菜单-showrootleftmenu="true"
<sysmenu name="spplatform" left_width="12%" top_height="70"
		showhidden="true" showhidden_width="10" showrootleftmenu="true">
		
配置module中出现左侧菜单-showleftmenu="true"：
<module name="应用台账" id="appbommanagermodule" used="true" showleftmenu="true">

frame.jsp中设置标签：
 <sany:leftmenu target="rightFrame"/>
 
 target属性指定页面链接打开的窗口名称    		

******************************************************
***     请求服务平台    2012.02.02     qian.wang          ***
******************************************************
o 日志查询bug
1.部门管理员查询日志失败，修改程序
/smc-desktop/src-sys/com/frameworkset/platform/sysmgrcore/web/tag/LogSearchList.java
2.日志按模块统计选机构失败
/smc-desktop/WebRoot/sysmanager/dictmanager/orgSelectTree.jsp
******************************************************
***     请求服务平台    2011.09.16     qian.wang          ***
******************************************************
1.desktop的框架中，新增了快捷方式排序功能和用户自定义窗口大小功能
在td_sm_deskmenu表中增加了iter_order字段，用来桌面方式快捷排序
新增了td_sm_menucustom表，用来存储用户自定义窗口大小的字段
修改文件：
CreatorESB-3.0\WebRoot\WEB-INF\conf\commons\bboss-esb-desktop.xml
creatorESBS3.0\CreatorESB-3.0\WebRoot\desktop\setting.jsp
新增文件：
creatorESBS3.0\CreatorESB-3.0\WebRoot\desktop\desktopsort.jsp
creatorESBS3.0\CreatorESB-3.0\WebRoot\desktop-4.0\menucustom\deskitemcustomtree.jsp
creatorESBS3.0\CreatorESB-3.0\WebRoot\desktop-4.0\menucustom\main.jsp
creatorESBS3.0\CreatorESB-3.0\WebRoot\desktop-4.0\menucustom\setdesktopwindowsize.jsp
2. 修改了用户登录按照传统风格和desktop风格登入系统
creatorESBS3.0\CreatorESB-3.0\WebRoot\login.jsp


修改菜单生成控制器
直接使用ResponseBody将List对象返回，然后通过mvc框架将该list转换为json串对象返回给客服端

