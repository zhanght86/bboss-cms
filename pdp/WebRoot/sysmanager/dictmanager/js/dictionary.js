/* 
字典管理jquery插件
 */ 


// the actual Flot code

   
jQuery.dictionary =  {
    		orgSelectFinal:function (contextpath,ifid,windowname,fieldName,isUnique,fileTextName){
    			var url = contextpath + "/sysmanager/dictmanager/dictOrgSelectTree.jsp?orgNames="+$("#"+fileTextName).val()
    								+ "&fieldName="+fieldName  + "&isUnique="+isUnique  + "&fileTextName="+fileTextName;
    			 $.dialog({ id:ifid, title:windowname,width:740,height:560, content:'url:'+url});
    			//var valueWin = window.showModalDialog("orgSelectTree.jsp?orgNames="+obj.value,window,"dialogWidth:"+(400)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no");
    			
    		},
    		
    		setSelectOrg:function (valueWin,fieldName,fileTextName,isUnique)
    		{
    			var restr = valueWin;
    			var oldValue = $("#"+fileTextName).val();
    			if(valueWin != "")
    			{
	    			var tokens = valueWin.split('^');
	    			$("#"+fileTextName).val(tokens[0]);
	    			$("#"+fieldName).val(tokens[1]);
	    				
	    			if(isUnique){
	    				if(valueWin && oldValue!=tokens[0])
	    					send_request_name(document.getElementById(fieldName),document.getElementById(fileTextName),'<%=dicttypeId%>');
	    			}
	    		}
	    		else
	    			{
	    				$("#"+fileTextName).val("");
	    			$("#"+fieldName).val("");
	    			}
    		},	
    		
    		userSelectFinal:function (contextpath,ifid,windowname,fieldName,isUnique,fileTextName){
    			var url = contextpath + "/sysmanager/dictmanager/selectexecutor.jsp?displayNameInput=partner&displayValueInput=executors&userNames="+$("#"+fileTextName).val()
    								+ "&fieldName="+fieldName  + "&isUnique="+isUnique  + "&fileTextName="+fileTextName;
    				
    			 $.dialog({ id:ifid, title:windowname,width:740,height:560, content:'url:'+url});
    			//var valueWin = window.showModalDialog("orgSelectTree.jsp?orgNames="+obj.value,window,"dialogWidth:"+(400)+"px;dialogHeight:"+(600)+"px;help:no;scroll:auto;status:no");
    	
			
    		},
    		
    		setSelectUser:function (valueWin,fieldName,fileTextName,isUnique)
    		{
    			var restr = valueWin;
    			
    			if(valueWin != "")
    			{
    				var oldValue = $("#"+fileTextName).val();
	    			var tokens = valueWin.split('^');
	    			$("#"+fileTextName).val(tokens[0]);
	    			$("#"+fieldName).val(tokens[1]);
    				
	    			if(isUnique){
	    				if(valueWin && oldValue!=tokens[0])
	    					send_request_name(document.getElementById(fieldName),document.getElementById(fileTextName),'<%=dicttypeId%>');
	    			}
	    		}
	    		else
	    			{
	    				$("#"+fileTextName).val("");
	    				$("#"+fieldName).val("");
	    			}
	    				
    		}	
 };
    

