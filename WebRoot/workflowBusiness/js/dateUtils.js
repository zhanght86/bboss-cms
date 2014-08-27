		
		/**
		 * 时间处理公共方法
		 * 
		 * @author          fudk
		 * @company         SANY Heavy Industry Co, Ltd
		 * @creation date   2013-9-23
		 * @version         1.0
		 */
		
		
		/*
		 *   功能:实现VBScript的DateAdd功能.
		 *   参数:interval,字符串表达式，表示要添加的时间间隔.
		 *   参数:number,数值表达式，表示要添加的时间间隔的个数.
		 *   参数:date,时间对象.
		 *   返回:新的时间对象.
		 *   var   now   =   new   Date();
		 *   var   newDate   =   DateAdd( "d ",5,now);
		 */
		function DateAdd(interval, number, date) {
			switch (interval) {
			case "y": {
				date.setFullYear(date.getFullYear() + number);
				return date;
				break;
			}
			case "q": {
				date.setMonth(date.getMonth() + number * 3);
				return date;
				break;
			}
			case "M": {
				date.setMonth(date.getMonth() + number);
				return date;
				break;
			}
			case "w": {
				date.setDate(date.getDate() + number * 7);
				return date;
				break;
			}
			case "d": {
				date.setDate(date.getDate() + number);
				return date;
				break;
			}
			case "h": {
				date.setHours(date.getHours() + number);
				return date;
				break;
			}
			case "m": {
				date.setMinutes(date.getMinutes() + number);
				return date;
				break;
			}
			case "s": {
				date.setSeconds(date.getSeconds() + number);
				return date;
				break;
			}
			default: {
				date.setDate(d.getDate() + number);
				return date;
				break;
			}
			}
		}
		
		/*
		 *   功能:时间格式转换
		 *   参数:format,时间格式.
		 *   返回:按格式返回字符.
		 *   var   now   =   new   Date();
		 *   var   newDate   =   now.format("yyyy-MM-dd ");
		 */
		Date.prototype.format = function(format) {
			var o = {
					"M+" : this.getMonth() + 1, //month 
					"d+" : this.getDate(), //day 
					"h+" : this.getHours(), //hour 
					"m+" : this.getMinutes(), //minute 
					"s+" : this.getSeconds(), //second 
					"q+" : Math.floor((this.getMonth() + 3) / 3), //quarter 
					"S" : this.getMilliseconds()
					//millisecond 
			};
		
			if (/(y+)/.test(format)) {
				format = format.replace(RegExp.$1, (this.getFullYear() + "")
						.substr(4 - RegExp.$1.length));
			}
		
			for ( var k in o) {
				if (new RegExp("(" + k + ")").test(format)) {
					format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
				}
			}
			return format;
		};