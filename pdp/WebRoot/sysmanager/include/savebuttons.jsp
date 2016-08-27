<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<script language="JavaScript">
function topClose() {
    returnValue="cancel";
    top.close();
}
</script>
<TABLE  width="100%" id="idToolbar" cellpadding='0' cellspacing='0'>
  <TR align="left" valign="top">
    <TD width="*" NOWRAP>&nbsp;</TD>
    <TD width="60" NOWRAP> <A class="tbButton" href="javascript:void(0)" title="确定" onclick="if (document.forms[0].onsubmit()) document.forms[0].submit();"><html:img page="/images/dialog-ok.gif" align="absmiddle"  border="0"/> <font color="#0000cc">确定</font></A></td>
        <TD width="10" NOWRAP>&nbsp;</TD>
    <TD width="60" NOWRAP><a class="tbButton" href="javascript:void(0)" onclick="topClose()" title="取消"><html:img page="/images/dialog-cancel.gif" align="absmiddle" border="0"/> <font color="#0000cc">取消</font></a></td>
  </tr>
</table>

