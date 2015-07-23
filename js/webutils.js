/**
 * 网站工具类js，包含easyui的扩展和常用方法
 */
var yp = $.extend({}, yp);// 定义全局对象

/**
 * 获得项目的根路径 使用方法：yp.bp(); 返回项目的根路径，形如："http://localhost:8080/project"
 * 注，若将项目发布到服务器的根目录下时，可能不需要项目名，而是直接通过IP访问，如此以来，就需要去掉类似“project”这样的项目名称
 */
yp.bp = function() {
	var curWwwPath = window.document.location.href;
	var pathName = window.document.location.pathname;
	var pos = curWwwPath.indexOf(pathName);
	var hostPath = curWwwPath.substring(0, pos);
	var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);// 截取pathName的第一节内容：/hbsi/page/welcome.jsp中的'/hbsi'
	// return (hostPath + projectName);
	// return hostPath;
	if (projectName == '/hbsi') {
		return (hostPath + projectName);
	} else {
		return hostPath;
	}
};

/**
 * 获得项目的根路径 使用方法：yp.getBasePath(); 返回项目的根路径，形如："/project"
 */
yp.getBasePath = function() {
	var path = document.location.pathname;
	var index = path.indexOf("/", 1);
	var basepath = path.substring(0, index);// "/project"
	if (basepath == '/hbsi') {
		return basepath;
	} else {
		return '/';
	}
};

/**
 * 将方法表单中的元素的值序列化成对象，注：JQuery中已经提供了两个类似的方法，分别是：serialize和serializeArray，但这两者分别是将form表单序列化成json字符串和数组，并不能序列化成一个对象
 * 用法 ：var jsonObj=yp.serializeObject(formid);
 */
yp.serializeObject = function(formSelector) {
	var form = $(formSelector);
	var obj = {};
	// var count = 0;
	$.each(form.serializeArray(), function(i, o) {
		var n = o.name, v = o.value;
		// count++;
		obj[n] = obj[n] === undefined ? v : $.isArray(obj[n]) ? obj[n].concat(v) : [ obj[n], v ];
	});
	// obj.nameCounts = count + "";// 表单name个数
	// return JSON.stringify(obj);//返回json字符串
	return obj;// 返回对象
};
/**
 * 删除字符串中的html标签
 */
yp.removeHTMLTag = function(str) {
	str = str.replace(/<\/?[^>]*>/g, ''); // 去除HTML tag
	str = str.replace(/[ | ]*\n/g, '\n'); // 去除行尾空白
	// str = str.replace(/\n[\s| | ]*\r/g,'\n'); //去除多余空行
	str = str.replace(/&nbsp;/ig, '');// 去掉&nbsp;
	return str;
};
/**
 * 将JS对象转变成字符串格式
 */
yp.obj2JsonString = function(obj) {
	if (typeof (obj) == typeof (false)) {
		return obj;
	}
	if (obj == null) {
		return "null";
	}
	if (typeof (obj) == typeof (0)) {
		return obj.toString();
	}
	if (typeof (obj) == typeof ('') || obj instanceof String) {
		obj = obj.toString();
		obj = obj.replace(/\r\n/, '\\r\\n');
		obj = obj.replace(/\n/, '\\n');
		obj = obj.replace(/\"/, '\\"');
		return '"' + obj + '"';
	}
	if (obj instanceof Array) {
		var strRet = "[";
		for (var i = 0; i < obj.length; i++) {
			if (strRet.length > 1)
				strRet += ",";
			strRet += yp.obj2JsonString(obj[i]);
		}
		strRet += "]";
		return strRet;
	}
	if (typeof (obj) == typeof ({})) {
		var strRet = "{";
		for ( var p in obj) {
			if (strRet.length > 1)
				strRet += ",";
			strRet += '"' + p.toString() + '":' + yp.obj2JsonString(obj[p]);
		}
		strRet += "}";
		return strRet;
	}
};
/**
 * 合并对象，用法：yp.mergeObject(obj1,obj2);//其中obj1是要被返回的对象
 */
yp.mergeObject = function(obj1, obj2) {
	var obj = $.extend(true, {}, obj1, obj2);
	return obj;
};

/**
 * 在控制台打印页面中的元素或对象信息
 */
yp.print = function(obj) {
	console.info(obj);
};
/**
 * 获取中文星期数
 */
yp.getChineseDay = function(t) {
	var a = t.getDay();
	var s = "";
	switch (a) {
	case 1:
		s = "一";
		break;
	case 2:
		s = "二";
		break;
	case 3:
		s = "三";
		break;
	case 4:
		s = "四";
		break;
	case 5:
		s = "五";
		break;
	case 6:
		s = "六";
		break;
	case 7:
		s = "日";
		break;
	}
	return s;
}

/**
 * 将json字符串转换成对象
 */
yp.json2obj = function(json) {
	// 使用jquery提供的parseJSON方法，但此方法不接受畸形的json字符串，如属性名应该使用双绰号但却使用了单引号
	var obj = jQuery.parseJSON(json);
	/* var obj = eval("(" + json + ")"); */
	if (obj) {
		return obj;
	} else {
		return null;
	}
};

/**
 * 扩展Date的format方法
 */
Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1,
		"d+" : this.getDate(),
		"h+" : this.getHours(),
		"m+" : this.getMinutes(),
		"s+" : this.getSeconds(),
		"q+" : Math.floor((this.getMonth() + 3) / 3),
		"S" : this.getMilliseconds()
	};
	if (/(y+)/.test(format)) {
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	}
	for ( var k in o) {
		if (new RegExp("(" + k + ")").test(format)) {
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
		}
	}
	return format;
}
/**
 * 转换日期对象为日期字符串
 * 
 * @param date
 *            日期对象
 * @param isFull
 *            是否为完整的日期数据, 为true时, 格式如"2000-03-05 01:05:04" 为false时, 格式如
 *            "2000-03-05"
 * @return 符合要求的日期字符串
 */
function getSmpFormatDate(date, isFull) {
	var pattern = "";
	if (isFull == true || isFull == undefined) {
		pattern = "yyyy-MM-dd hh:mm:ss";
	} else {
		pattern = "yyyy-MM-dd";
	}
	return getFormatDate(date, pattern);
}
/**
 * 转换当前日期对象为日期字符串
 * 
 * @param date
 *            日期对象
 * @param isFull
 *            是否为完整的日期数据, 为true时, 格式如"2000-03-05 01:05:04" 为false时, 格式如
 *            "2000-03-05"
 * @return 符合要求的日期字符串
 */

function getSmpFormatNowDate(isFull) {
	return getSmpFormatDate(new Date(), isFull);
}
/**
 * 转换long值为日期字符串
 * 
 * @param l
 *            long值
 * @param isFull
 *            是否为完整的日期数据, 为true时, 格式如"2000-03-05 01:05:04" 为false时, 格式如
 *            "2000-03-05"
 * @return 符合要求的日期字符串
 */

function getSmpFormatDateByLong(l, isFull) {
	return getSmpFormatDate(new Date(l), isFull);
}
/**
 * 转换long值为日期字符串
 * 
 * @param l
 *            long值
 * @param pattern
 *            格式字符串,例如：yyyy-MM-dd hh:mm:ss
 * @return 符合要求的日期字符串
 */

function getFormatDateByLong(l, pattern) {
	return getFormatDate(new Date(l), pattern);
}
/**
 * 转换日期对象为日期字符串
 * 
 * @param l
 *            long值
 * @param pattern
 *            格式字符串,例如：yyyy-MM-dd hh:mm:ss
 * @return 符合要求的日期字符串
 */
function getFormatDate(date, pattern) {
	if (date == undefined) {
		date = new Date();
	}
	if (pattern == undefined) {
		pattern = "yyyy-MM-dd hh:mm:ss";
	}
	return date.format(pattern);
}

// alert(getSmpFormatDate(new Date(1279849429000), true));
// alert(getSmpFormatDate(new Date(1279849429000),false));
// alert(getSmpFormatDateByLong(1279829423000, true));
// alert(getSmpFormatDateByLong(1279829423000,false));
// alert(getFormatDateByLong(1279829423000, "yyyy-MM"));
// alert(getFormatDate(new Date(1279829423000), "yy-MM"));
// alert(getFormatDateByLong(1279849429000, "yyyy-MM hh:mm"));
/**
 * 将js对象中的值批量填充至指定的Form表单中，只支持input标签
 * 
 * @param form_selector
 *            要填充的Form的选择器，注：此Form中的各字段的name值应当与obj中对应属性名相同，
 *            或者此Form中的各字段的name值与prefix加上obj中对应的属性名相同。
 * @param obj
 *            要填充的对象，注，如果该对象的某个或某些属性是多个枚举值（对应于Form中的复选框或单选按钮），
 *            则这几个值应该由逗号连接起来，这对应于form中name值相同的input项的值的情况
 * @param prefix
 *            属性名前缀，注，此项的作用是为了防止在一个大的页面中存在name值同名的冲突问题，若不需要，可以指定为""，
 *            因此Form中的属性的name值与obj中对应的属性名均多了一个相同的前缀，例如，指定prefix="form1"则
 *            将obj.feild1填充到form中的name值为form1feild1的字段中
 * @param excludeName
 *            排除的属性数组。排除的属性将不会被填充
 */
yp.fillFormValues = function(form_selector, obj, prefix, excludeName) {
	// 遍历obj
	if (!obj) {
		var inputs = $(form_selector + " input");
		for (var i = 0; i < inputs.length; i++) {
			var input = $(inputs[i]);
			var name = input.attr("name");
			var isin = yp.isInarray(name, excludeName);
			var comboname = input.attr("comboname");
			var isin2 = yp.isInarray(comboname, excludeName);
			if (isin2) {
				i += 3;
				continue;
			} else if (!isin) {// 该属性不在排除的数组中
				var type = input.attr("type");
				if ((type == "checkbox" || type == "radio")) {// 如果是复选框或单选按钮则将obj中的该属性值通过","分隔成多个，然后分别填充
					input.each(function(k, n) {
						$(n).attr("checked", undefined);
					});
				} else {
					input.val('');
				}
			}
		}
	} else {
		for ( var attr in obj) {
			if (typeof (obj[attr]) == 'function') {
				continue;
			}
			var pre = attr;
			if (prefix) {
				pre = prefix + attr;
			}
			var isin = yp.isInarray(pre, excludeName);
			if (!isin) {// 不属于排除的元素
				var input = $(form_selector + " :input[name='" + pre + "']");
				var type = input.attr("type");
				if ((type == "checkbox" || type == "radio")) {// 如果是复选框或单选按钮则将obj中的该属性值通过","分隔成多个，然后分别填充
					var avalues = obj[attr].toString().split(",");// 注，要想使用分隔，就必须转换成字符串，因为有时候对于单选按钮可能使用逻辑型值
					for (var i = 0; i < avalues.length; i++) {// 检查obj中此属性值与哪个radio或checkbox匹配
						input.each(function(k, n) {
							var value = $(n).val();
							if (value.toString() == avalues[i] || (value == 'on' && avalues[i] == 'true')) {
								$(n).attr("checked", "checked");
							}
						});
					}
				} else {
					input.val(obj[attr]);
				}
			}
		}
	}
};
/**
 * 判断元素e是否在数组arr中，若是返回true，否则返回false。若arr或e为null也返回false
 */
yp.isInarray = function(e, arr) {
	if (e && arr) {
		if (arr.length && arr.length > 0) {
			for (var i = 0; i < arr.length; i++) {
				if (arr[i] == e) {
					return true;
				}
			}
		}
	}
	return false;
};
/**
 * 为对象的每个属性前面加上前缀
 * 
 * @param obj
 *            待修改的对象
 * @param prefix
 *            在属性前要添加的前缀
 * @return 属性加了前缀的对象
 */
yp.addPrefix = function(obj, prefix) {
	var o = {};
	for ( var i in obj) {
		o[prefix + i] = obj[i];
	}
	return o;
};
/**
 * 验证身份证号是否合法
 * 
 * @param id
 *            传入的待验证的身份证号
 */
yp.isIDCard = function(id) {
	var Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 ];// 加权因子;
	var ValideCode = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ];// 身份证验证位值，10代表X;
	id = id.trim();
	if (id.length == 15) {
		return isValidityBrithBy15IdCard(id);
	} else if (id.length == 18) {
		var a_idCard = id.split("");// 得到身份证数组
		if (isValidityBrithBy18IdCard(id) && isTrueValidateCodeBy18IdCard(a_idCard)) {
			return true;
		}
		return false;
	}
	return false;

	function isTrueValidateCodeBy18IdCard(a_idCard) {
		var sum = 0; // 声明加权求和变量
		if (a_idCard[17].toLowerCase() == 'x') {
			a_idCard[17] = 10;// 将最后位为x的验证码替换为10方便后续操作
		}
		for (var i = 0; i < 17; i++) {
			sum += Wi[i] * a_idCard[i];// 加权求和
		}
		valCodePosition = sum % 11;// 得到验证码所位置
		if (a_idCard[17] == ValideCode[valCodePosition]) {
			return true;
		}
		return false;
	}

	function isValidityBrithBy18IdCard(idCard18) {
		var year = idCard18.substring(6, 10);
		var month = idCard18.substring(10, 12);
		var day = idCard18.substring(12, 14);
		var temp_date = new Date(year, parseFloat(month) - 1, parseFloat(day));
		// 这里用getFullYear()获取年份，避免千年虫问题
		if (temp_date.getFullYear() != parseFloat(year) || temp_date.getMonth() != parseFloat(month) - 1 || temp_date.getDate() != parseFloat(day)) {
			return false;
		}
		return true;
	}
	function isValidityBrithBy15IdCard(idCard15) {
		var year = idCard15.substring(6, 8);
		var month = idCard15.substring(8, 10);
		var day = idCard15.substring(10, 12);
		var temp_date = new Date(year, parseFloat(month) - 1, parseFloat(day));
		// 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法
		if (temp_date.getYear() != parseFloat(year) || temp_date.getMonth() != parseFloat(month) - 1 || temp_date.getDate() != parseFloat(day)) {
			return false;
		}
		return true;
	}
};
yp.showPic = function(src) {
	$.layer({
		type : 1,
		title : false,
		fix : true,
		offset : [ '200px', '' ],
		area : [ '200px', '200px' ],
		page : {
			html : '<img src="' + yp.bp() + src + '" alt="管理员图像" width="200px" height="200px">'
		}
	});
};
// ======================================
/*
 * @desc 判断浏览器的版本以及浏览器内核 @author wangyanling @date 2014年7月4日
 */
yp.browser = function() {
	var agent = navigator.userAgent.toLowerCase();
	var appName = "";// 浏览器名称
	var version = undefined;// 官方版本号
	var vInfo = undefined;
	var wbkt = agent.indexOf(' applewebkit/') > -1;
	var gko = navigator.product == 'Gecko' && !wbkt && !opr && !ie;
	var opr = agent.indexOf("opr") > 0;
	var ie = /(msie\s|trident.*rv:)([\w.]+)/.test(agent);
	var innerCode = 0;// 内核版本。
	var inner = function() {
		var inn = {// 移动终端浏览器版本信息
			trident : ie, // IE内核
			presto : agent.indexOf('Presto') > -1, // opera内核
			webKit : wbkt, // 苹果、谷歌内核
			gecko : gko, // 火狐内核
		};
		if (inn.trident) {
			return "Trident(IE内核)";
		} else if (inn.presto) {
			return "Presto(前opera内核)";
		} else if (inn.webKit) {
			return "AppleWebKit(苹果、谷歌内核)";
		} else if (inn.gecko) {
			return "Gecko( 火狐内核)";
		} else {
			return "未知内核";
		}
	}();
	browser = {
		// isIE : (agent.indexOf("msie") > 0),
		isIE : (ie),
		isFireFox : (agent.indexOf("firefox") > 0),
		isOpera : (opr),
		isChrome : (agent.indexOf("chrome") > 0 && agent.indexOf("opr") < 0),
		isSafari : (agent.indexOf("safari") > 0 && agent.indexOf("chrome") < 0),
		isWebkit : (wbkt),
		isGecko : (gko),
		quirks : (document.compatMode == 'BackCompat'),
		mac : (agent.indexOf('macintosh') > -1)
	};

	if (browser.isIE) {// Internet Explorer 6.0+
		appName = "IE浏览器";
		vInfo = agent.match(/msie [\d.]+;/gi);
		var v1 = agent.match(/(?:msie\s([\w.]+))/);
		var v2 = agent.match(/(?:trident.*rv:([\w.]+))/);
		if (v1 && v2 && v1[1] && v2[1]) {
			version = Math.max(v1[1] * 1, v2[1] * 1);
		} else if (v1 && v1[1]) {
			version = v1[1] * 1;
		} else if (v2 && v2[1]) {
			version = v2[1] * 1;
		} else {
			version = 0;
		}
		browser.ie9Compat = document.documentMode == 9;// 检测浏览器模式是否为 IE9 兼容模式
		browser.ie10Compat = document.documentMode == 10;// 检测浏览器模式是否为IE10兼容模式
		browser.ie11Compat = document.documentMode == 11;// 检测浏览器模式是否为IE11兼容模式
		browser.ie8 = !!document.documentMode; // 检测浏览器是否是IE8浏览器
		browser.ie8Compat = document.documentMode == 8;// 检测浏览器模式是否为 IE8 兼容模式
		browser.ie7Compat = ((version == 7 && !document.documentMode) || document.documentMode == 7);// 检测浏览器模式是否为IE7兼容模式
		browser.ie6Compat = (version < 7 || browser.quirks);// 检测浏览器模式是否为
		// IE6模式或者怪异模式
		browser.ie9above = version > 8;
		browser.ie9below = version < 9;
		innerCode = version;
		vInfo = "IE/" + version;
	} else if (browser.isFireFox) {
		appName = "FireFox(火狐)浏览器";
		vInfo = agent.match(/firefox\/[\d.]+/gi);
	} else if (browser.isOpera) {
		appName = "Opera(欧朋)浏览器";
		vInfo = agent.match(/opr\/[\d.]+/gi);
	} else if (browser.isChrome) {
		appName = "Chrome(谷歌内核)浏览器";
		vInfo = agent.match(/chrome\/[\d.]+/gi);
	} else if (browser.isSafari) {
		appName = "Safari(苹果)浏览器";
		vInfo = agent.match(/safari\/[\d.]+/gi);
	} else {
		appName = "其它浏览器";
	}
	if (vInfo) {
		innerCode = (vInfo + "").replace(/[^0-9.]/ig, "").toString().split(".")[0];// 先记下内核版本号
	}
	var _360se = agent.indexOf('360se') > -1;
	var _360cse = agent.indexOf('360cse') > -1;
	var se = agent.indexOf('se') > -1;
	var aoyou = agent.indexOf('aoyou') > -1;
	var theworld = agent.indexOf('theworld') > -1;
	var worldchrome = agent.indexOf('worldchrome') > -1;
	var greenbrowser = agent.indexOf('greenbrowser') > -1;
	var baidu = agent.indexOf('baidu') > -1;
	var qqbrowser = agent.indexOf('qqbrowser') > -1;
	var lbbrowser = agent.indexOf('lbbrowser') > -1;
	if (_360se) {
		appName = "360安全浏览器";
		vInfo = agent.match(/360se\/[\d.]+/gi);
	} else if (qqbrowser) {
		appName = "QQ浏览器";
		vInfo = agent.match(/qqbrowser\/[\d.]+/gi);
	} else if (baidu) {
		appName = "百度浏览器";
		vInfo = agent.match(/baidu\/[\d.]+/gi);
	} else if (aoyou) {
		appName = "遨游浏览器";
		vInfo = agent.match(/aoyou\/[\d.]+/gi);
	} else if (_360cse) {
		appName = "360极速浏览器";
		vInfo = agent.match(/360cse\/[\d.]+/gi);
	} else if (theworld) {
		appName = "世界之窗浏览器";
		vInfo = agent.match(/theworld\/[\d.]+/gi);
	} else if (worldchrome) {
		appName = "worldchrome浏览器";
		vInfo = agent.match(/worldchrome\/[\d.]+/gi);
	} else if (greenbrowser) {
		appName = "绿色浏览器";
		vInfo = agent.match(/greenbrowser\/[\d.]+/gi);
	} else if (lbbrowser) {
		appName = "猎豹浏览器";
		vInfo = agent.match(/lbbrowser;\s+rv:[\d.]+/gi);
		vInfo = vInfo.toString().split("rv:")[1];
	} else if (se) {
		appName = "搜狗浏览器";
		vInfo = agent.match(/se\s+[\d.]+/gi);
	}
	if (vInfo) {
		version = (vInfo + "").replace(/[^0-9.]/ig, "");
	}
	browser.appName = appName;
	browser.version = version;
	browser.inner = inner;
	browser.agent = agent;
	browser.versionCode = version.toString().split(".")[0];
	browser.innerCode = innerCode;
	return browser;
};

// ======================================
