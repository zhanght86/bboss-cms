<%@ page session="true" language="java"
	contentType="text/html; charset=utf-8"%>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="${ctx}/sanymbp/common/css/base.css" />
<script src="${ctx}/sanymbp/common/js/jquery-1.6.4.min.js"></script>
<script
	src="${ctx}/sanymbp/common/widgets/jquery.mobile-1.1/jquery.mobile-1.1.0.min.js"></script>
<script type="text/javascript">
	var ctx = '${ctx}';
</script>
<script src="${ctx}/sanymbp/common/js/base.js"></script>
<pg:notempty actual="${pagePath}">
<link rel="stylesheet" href="${ctx}${pagePath}.css" />
<script src="${ctx}${pagePath}.js"></script>
</pg:notempty>
<link rel="stylesheet" href="${ctx}/sanyems/icss/icss.css" /></link>