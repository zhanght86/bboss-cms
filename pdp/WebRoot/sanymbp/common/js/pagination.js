//**_footerid 底部触发分页查询domid
//**_listid 显示ul列表id
//**_formid 显示查询参数form表单id
//**_maxitem 每页显示最大记录数
//**_totalsize 总的记录数
function inintpage(_footerid,_listid,_formid,_url,_maxitem,_totalsize){
		
	$("#"+_formid).append('<input type="hidden" name="offset" id="offset" value="0">'+
	'<input type="hidden" name="submittype" value="ajax">'+
	'<input type="hidden" id="deleteid" value="">');
	
	$('#'+_footerid).before("<b id='end"+_footerid+"' style='display: none;'>已经尾页了！</b>");
	
	$('#'+_footerid).waypoint(function (a, b) {
		var oset = parseInt($("#offset").val()) + parseInt(_maxitem);
		if(oset>=_totalsize){
			//$('#'+_footerid).before("<b>已经尾页了！</b>"); 
			$("#end"+_footerid).fadeIn("slow",function(){
				$("#end"+_footerid).fadeOut("slow"); 
			}); 
			return;
		}else{
			$("#offset").val(oset);
			$.ajax({ 
				type: "POST", 
				url: _url, 
				data: $("form#"+_formid).serialize(), 
				success: function(msg){ 
					if(msg!=""){
						$("#"+_listid).append($(msg));
						$("#"+_listid).listview('refresh');
						$('#'+_footerid).waypoint({ offset: '100%'    });
					}
				} 
			});
		}
	}, { offset: '100%'    });
}
//弹出确认按钮前记录要删除记录的id
function deleteid(_liid){
	$("#deleteid").val(_liid)
}
//ajax删除记录成功，后直接在页面删除记录
function deletedata(_deleteurl){
	var _liid = $("#deleteid").val();
   	$.ajax({ 
   		type: "POST", 
   		url: _deleteurl, 
   		data: "deleteid="+_liid, 
   		success: function(msg){ 
    		if(msg=="success"){
        		$("#"+_liid).detach();
      		}
		} 
	});
	$("#deleteid").val("");
}


