
/**----------------------------------------------------------------------------------
* [ Title ] The common JS in System AccessControl.
* [ Invoke ] var jsAccessControl = new JSAccessControl("#ff0000","#ffffff","#eeeeee");
*----------------------------------------------------------------------------------*/


	//Color Control
      	//For 1.onmouseout 2.onmouseover 3.onclick
	function JSAccessControl(heightLightColor,darkLightColor,onMouseOverColor){
                this.heightLightColor	= heightLightColor;
                this.darkLightColor	= darkLightColor;
                this.onMouseOverColor	= onMouseOverColor;

                //action onmouseout
                this.actionOnmouseout 	=
        		function actionOnmouseout(){
				var st = window.event.srcElement.parentElement;
				if(st.style.backgroundColor == this.onMouseOverColor){
					st.style.backgroundColor = this.darkLightColor;
				}
			}

               //action onmouseover
               this.actionOnmouseover 	=
			function actionOnmouseover(){
				var st = window.event.srcElement.parentElement;
				if(st.style.backgroundColor == '' || st.style.backgroundColor == this.darkLightColor){
					st.style.backgroundColor = this.onMouseOverColor;
				}
			}

               // action if a row of the Table is clicked
               // @head int: "Color Control" can't action the first "head" rows (COUNT) of the table.
               // @tail int: "Color Control" can't action the last "tail" rows (COUNT) of the table.
               this.actionOnclick 	=
			function actionOnclick(head,tail){
				var obj = window.event.srcElement.parentElement;
				if(event.ctrlKey && window.event.keyCode == 0){
					obj.style.backgroundColor = this.heightLightColor;			//heightLight Color
				}else{
					var tableObj = obj.parentElement;
					for(var i=head;i<(tableObj.rows.length - tail);i++){
						tableObj.rows[i].style.backgroundColor = this.darkLightColor;	//darkLight Color.
					}
					obj.style.backgroundColor = this.heightLightColor;			//heightLight Color
				}
			}
               this.setBackColor 	=
			function setBackColor(e){
				//if ( tableObj!==null && tableObj.rows!=null && tableObj.rows.length != null ){
				if ( window.event.srcElement.parentElement == e )
				{
					var obj = window.event.srcElement.parentElement;
					var tableObj = obj.parentElement;
					for(var i=1;i<tableObj.rows.length;i++){
						tableObj.rows[i].style.backgroundColor = this.darkLightColor;	//darkLight Color.
					}
					obj.style.backgroundColor = this.heightLightColor;			//heightLight Color
				}
			}
	}

	/**
        * 当radio按钮被选中后:视觉效果改变
        * @param obj - Object - the checkBox object
        * @param darkLightColor  - the checkBox isn't checked
        * @param hightLightColor - the checkBox is checked
        * */
      	function checkColorAction(obj,hightLightColor,darkLightColor) {
        	if(obj.parentElement.parentElement.style.backgroundColor==hightLightColor){
                	obj.parentElement.parentElement.style.backgroundColor=darkLightColor ;
            	}else{
                	obj.parentElement.parentElement.style.backgroundColor=hightLightColor;
            	}
      	}

	/**函数组:----------------------------------------------------------------------------------------------
	* -- 翻页工具栏控制函数
	* @param formName:“翻页工具栏”所在的表单（form）
	* @param actionUrl: “翻页工具栏”所在的页面 url地址
	* */
    	function gotoHead(formName,actionUrl){  /*首页*/
          var objForm = document.all(formName);
          var vCurrentPage = objForm.currentPage.value;
          if((parseInt(vCurrentPage)) > 1 ){
              	objForm.NextPage.value="1";
              	objForm.action= actionUrl + "?flag=1";
        		objForm.submit();
         	 }
          }

        function gotoPre(formName,actionUrl){/*前一页*/
          var objForm = document.all(formName);
          var vCurrentPage = objForm.currentPage.value;
          var vTotalPage = objForm.totalPage.value;
          if((parseInt(vCurrentPage)) > 1){
            objForm.NextPage.value=parseInt(vCurrentPage)-1;
            objForm.action= actionUrl + "?flag=1";
            objForm.submit();
            }
          }

        function gotoNext(formName,actionUrl){/*下一页*/
        	var objForm = document.all(formName);
          	var vCurrentPage = objForm.currentPage.value;
           	var vTotalPage = objForm.totalPage.value;
         	if((parseInt(vCurrentPage)) < (parseInt(vTotalPage))){
        		objForm.NextPage.value=parseInt(vCurrentPage)+1;
            	objForm.action= actionUrl + "?flag=1";
            	objForm.submit();
            }
          }

        function gotoTail(formName,actionUrl){/*尾页*/
          var objForm = document.all(formName);
          var vCurrentPage = objForm.currentPage.value;
          var vTotalPage = objForm.totalPage.value;
          if((parseInt(vCurrentPage)) < (parseInt(vTotalPage))){
        		objForm.NextPage.value=vTotalPage;
            	objForm.action= actionUrl + "?flag=1";
            	objForm.submit();
            }
          }
	/*------翻页工具栏结束-------------------------------------------------------------------------------*/

	/**
	* Make the form's all elementes writable or diswritable
	* @param theForm - the form Object you want to deal with
	* @param isWritable - false: make the form's elementes diswritable,else writable.
	* */
	function isTheFormWritable(theForm,isWritable){
              var elArr = theForm.elements; //elArr数组获得全部表单元素
              for(var i = 0; i < elArr.length; i++)
　　　　　　　　with(elArr[i]){　
			    try{
			    	if (elArr[i].type=="text" || elArr[i].type=="file" || elArr[i].type =="password"){
                                	if(isWritable == true){
                                          	elArr[i].readOnly = false;
                                        }else{
                                          	elArr[i].readOnly = true;
                                    	}
			    	}else if(elArr[i].type=="select-one"){
                                	if(isWritable == true){
                                          	elArr[i].disabled = false;
                                        }else{
                                          	elArr[i].disabled = true;
                                    	}
                                }
			    } catch(e) {}
		    }
	}

        function goto(formName,actionUrl){/*下一页*/
        	//var toPage = "2";
              var toPage = document.getElementById('gotoPage').value;
	       if (!(toPage.match(/[0-9]/)))
	       {
       			alert("页数必须为数字");
    		}
               else{
			var objForm = document.all(formName);
          		var vCurrentPage = objForm.currentPage.value;
           		var vTotalPage = objForm.totalPage.value;
         		if((parseInt(toPage)) < (parseInt(vTotalPage)+1) && (parseInt(toPage)) > 0){
        			objForm.NextPage.value=toPage;
            			objForm.action= actionUrl + "?flag=1";
            			objForm.submit();
         		}

        	}

        }

//删除总按钮行为
function checkAll(totalCheck,checkName){
   var selectAll = document.getElementsByName(totalCheck);
   var o = document.getElementsByName(checkName);
   if(selectAll[0].checked==true){
	   for (var i=0; i<o.length; i++){
      	  if(!o[i].disabled){
      	  	o[i].checked=true;
      	  }
	   }
   }else{
	   for (var i=0; i<o.length; i++){
   	  	  o[i].checked=false;
   	   }
   }
}

//各个删除按钮行为
function checkOne(totalCheck,checkName){
   var selectAll = document.getElementsByName(totalCheck);
   var o = document.getElementsByName(checkName);
	var cbs = true;
	for (var i=0;i<o.length;i++){
		if(!o[i].disabled){
			if (o[i].checked==false){
				cbs=false;
			}
		}
	}
	if(cbs){
		selectAll[0].checked=true;
	}else{
		selectAll[0].checked=false;
	}
}