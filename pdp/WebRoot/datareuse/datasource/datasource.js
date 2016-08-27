
/**********************************************************/
/* 在将原来数据源管理新增与修改一些重复的js移到这个文件
/* @auther 刘剑峰
/* @date 2011-05-23 
/**********************************************************/

//驱动和jdbc串一致性验证
function compare(){
	var db_type = $("#db_type").val();
	var jdurl=$("#jdbc_url").val();
	if(db_type == "oracle")
	{
		if(jdurl.indexOf("jdbc:oracle")!=0){
			alert('请填写oracle格式连接串');
			return false;
		}
	}
	else if(db_type == "sqlserver")
	{
		if(jdurl.indexOf("jdbc:sqlserver")!=0){
			alert('请填写sqlserver格式连接串');
			return false;
		}
	}
	else if(db_type == "db2")
	{
		if(jdurl.indexOf("jdbc:db2")!=0){
			alert('请填写db2格式连接串');
			return false;
		}
	}
	else
	{
		if(jdurl.indexOf("jdbc:mysql")!=0){
			alert('请填写mysql格式连接串');
			return false;
		}
	}
	return true;
}
//保存
function save(){
	
		var bl=compare();
		if(!bl){
			return false;
		}
        
		$("theForm").form('validate');
		
		
		if($("#ds_name").val()== null||$("#ds_name").val() =="")
		{
			$.messager.alert("提示","数据源名称不能为空!");
			return;
		}
        
        
	    if($("#connection_type").val() != "custom_jdbc")
	    {
	    	if($("#ext_jndi_name").val()== null||$("#ext_jndi_name").val() =="")
			{
				$.messager.alert("提示","外部JNDI名称不能为空!");
				return;
			}
	    	//$("#driver").val('');
	    	//$("#jdbc_url").val('');
	    }
	    else {
	    	var db_type = $("#db_type").val();
	    	
	    	if ($("#driver").val() == ''){
	    		$.messager.alert("提示","数据库驱动不能为空!");	    		
				return ;
	    	}
	    	
	    	if ($("#jdbc_url").val() == ''){
	    		$.messager.alert("提示","JDBC连接串不能为空!");	    		
				return ;
	    	}
	    	
	    	if (jdbc_driverArray[db_type] && (jdbc_driverArray[db_type].url == $("#jdbc_url").val())){
	    		$.messager.alert("提示","请修改JDBC连接串!");	    		
				return ;
	    	}
	    	
	    	if ($("#use_pool").val() == '1'){
			    //如果使用连接池才需要验证连接池的设置
			    
			    var initial_connections = parseInt($("#initial_connections").val());
			    var minimum_size = parseInt($("#minimum_size").val());
			    var maximum_size = parseInt($("#maximum_size").val());		    
			    
		    	if(initial_connections<0){
		 		   $.messager.alert("提示","初始连接数不能小于0!");
		 		   return;
		 		}	 		
				if( minimum_size < 2){
		 		  $.messager.alert("提示","最小连接数不能小于2!");
		 		  return;
		 		}
				if( minimum_size  > maximum_size){
			 		  $.messager.alert("提示","最小连接数不能大于最大连接数!");
			 		  return;
			 	}
				if(initial_connections > maximum_size){
			 		  $.messager.alert("提示","初始接数不能大于最大连接数!");
			 		  return;
			 	}
	    	}
	    }
	
		document.getElementById("theForm").submit();		
}

function stopDynamicDatasource(){
	$.messager.confirm('提示对话框', '您确定当前活动连接为0吗？否则本操作可能影响其他程序的运行', function(r){
		if (r){
			document.getElementById("theForm").action = 'stopDynamicDatasource.page';
			document.getElementById("theForm").submit();	
		}
	});		
}

//切换连接方式时，控制页面上的显示项
function changedConnectionType(){
	var connection_type = $("#connection_type").val();
	if (connection_type == 'externaljndi'){
		$(".custom_jdbc_content").hide();			
		$("#_ext_jndi_name").show();
		$("#td_ext_jndi_name").show();
	}
	else {
		
		$(".custom_jdbc_content").show();
		$("#td_ext_jndi_name").hide();
		$("#_ext_jndi_name").hide();
		
	}
}

//是否连接池时，控制页面显示项
function use_pool_cg()
{
	var use_pool = $("#use_pool").val();

	if(use_pool==0)
	{
		$("#initial_connections").get(0).disabled=true;
		$("#minimum_size").get(0).disabled=true;
		$("#maximum_size").get(0).disabled=true;
	}
	else
	{
		$("#initial_connections").get(0).disabled=false;
		$("#minimum_size").get(0).disabled=false;
		$("#maximum_size").get(0).disabled=false;
	}
}

//存放数据库类型与驱动、连接串的数组
var jdbc_driverArray = new Array();	

//数据库类型切换时，
function db_type_cg()
{
		
	var db_type = $("#db_type").val();
	var url ;
	var jdbc_driver;
	var sql = "select 1 "
	if(db_type == "oracle")
	{
		url = "jdbc:oracle:thin:@<server>[:<1521>]:<database_name>";
		jdbc_driver = "oracle.jdbc.driver.OracleDriver";
		sql += "from dual" ;
	}
	else if(db_type == "sqlserver")
	{
		url = "jdbc:sqlserver://<server_name>:<port>[;databaseName=<dbname>]";
		jdbc_driver = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
	}
	else if(db_type == "db2")
	{
		url = "jdbc:db2://<host_name>:6789/<dbname>";
		jdbc_driver = "COM.ibm.db2.jdbc.net.DB2Driver";
	}
	else
	{
		//mysql
		url = "jdbc:mysql://<hostname>[<:3306>]/<dbname>";
		jdbc_driver = "com.mysql.jdbc.Driver";
	}
	$("#jdbc_url").val(url);
	$("#driver").val(jdbc_driver);
	$("#validation_query").val(sql);
	
	//切换过的数据库类型都注册到数组中
	if (!jdbc_driverArray[db_type]){
		var jdbc_driverObj = new Object();
		jdbc_driverObj.db_type = db_type;
		jdbc_driverObj.url = url;
		jdbc_driverObj.jdbc_driver = jdbc_driver;
		
		jdbc_driverArray[db_type] = jdbc_driverObj;
	}	
}

//测试数据源是否可以连接
function test()
{
	var url = "test.page";
	var validation_query = $('#validation_query').val();

	var oldUrl = $("#theForm").attr("action");
	
	$("#theForm").form('submit',{
	    "url": url,	   
	    success:function(responseText){				
			var obj;
			try {							
				obj = jQuery.parseJSON(responseText);
			} catch(err){
				$.messager.alert("数据源连接失败" , "异常如下："
						+ responseText, "error");						
				return false;
			}			
			
			if (obj && obj.status){		
				if (obj.status == "success"){						
					if(obj.data == "0")
					{
						if($.trim(validation_query).length==0)
						{
							alert("数据源连接成功");
						}
						else
						{
							alert("数据源连接成功，但是连接测试SQL没有正常执行");
						}
					}
					else if(obj.data =="1")
					{
						if($.trim(validation_query).length==0)
						{
							alert("数据源连接成功");
						}
						else
						{
							alert("数据源连接成功，测试SQL正常执行");
						}
					}    		
		    	}
		    	else {
		    		$.messager.alert("数据源连接失败" , "原因如下："
							+ decodeURI(obj.data), "error");	
		    	}
			}					    			    					    											
	    }
	});	
	
	$("#theForm").attr("action", oldUrl);

}