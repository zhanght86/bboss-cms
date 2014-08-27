		
		/**
		 * 字符处理公共方法
		 * @author          fudk
		 * @company         SANY Heavy Industry Co, Ltd
		 * @creation date   2013-9-23
		 * @version         1.0
		 */
		
		/*
		 *   功能:字符全部替换.
		 *   参数:s1,被替换字符.
		 *   参数:s2,将替换字符.
		 *   返回:.
		 *   var   str   =   'AAABBBCCC';
		 *   var str1 = str.replaceAll('A', 'D');
		 *   str1:'DDDBBBCCC'
		 */
	String.prototype.replaceAll = function(s1, s2) {
		return this.replace(new RegExp(s1, "gm"), s2);
	};
	
	/*
	 *   功能:字符转json格式.
	 *   参数:str,json字符串.
	 *   返回:json对象.
	 */
	function strToJson(str) {
		var json = eval('(' + str + ')');
		return json;
	}
		