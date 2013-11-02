
(function ($) { 
    $.fn.extend({ 
        ScrollTable: function (options) { 
            var defaults = { 
                MaxHeight: 120, 
                MaxDataItemIndex: 1 // 固定前MaxDataItemIndex作为固定表头 
            }; 
            options = $.extend(defaults, options); 
  
            return this.each(function () { 
                var $this = $(this); 
  
                // 产生表头部分和身体部分 
                var $cloneTableHeader = $this.clone(); 
                var $cloneTableBody = $this.clone(); 
				
                $cloneTableHeader.find("tr").filter(function (index) { return index >= options.MaxDataItemIndex }).remove(); 
                $cloneTableBody.find("tr").filter(function (index) { return index < options.MaxDataItemIndex }).remove(); 
  
                // 将产生的表头和身体部分放入Container，并做一些微调 
                $this.after("<div class='ScrollTableContainer' style='border:1px solid;'></div>"); 
                $this.next().append($cloneTableHeader); 
                $this.next().append("<div class='ScrollDiv' style='overflow-y:scroll;'></div>"); 
                $this.next().css("width", $this.width() + 20); 
                $this.next().find("div.ScrollDiv").css({ 
                    "max-height": options.MaxHeight, 
                    "width": $this.width() + 20, 
                    "margin-top": -2 
                }); 
                $this.next().find("div.ScrollDiv").append($cloneTableBody); 
                $this.remove(); 
            }); 
        } 
    }); 
})(jQuery); 


