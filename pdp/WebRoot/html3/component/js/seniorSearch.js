// JavaScript Document
(function($){
   $.extend({
	  seniorSearch:function(options){
		 var defaults={
			  searchButton:"#search_button",
			  seniorButton:"#senior_search_button",
			  seniorContent:"#senior_search_content"
			 };
			 
		 var opts=$.extend({},defaults,options);
		 
		 var $searchButton=$(opts.searchButton);
		 var $seniorButton=$(opts.seniorButton);
		 var $seniorContent=$(opts.seniorContent);
		
		 var searcbParent=$searchButton.parent();
		 $seniorButton.toggle(
		  function()
		  {   
			   var seniorHeight=$seniorContent.height();
			   $seniorContent.css("display","block")
			   $searchButton.appendTo($seniorContent.find("div:last"));
			   $(this).addClass(".bt_senior_select");
						 
		  },
		  function()
		  {
			   $seniorContent.css("display","none")
			   $searchButton.appendTo(searcbParent);
			   $(this).removeClass(".bt_senior_select");
			  
		  }
		 ) 
	  }  
   });
})(jQuery);




	
	
	
	
	
