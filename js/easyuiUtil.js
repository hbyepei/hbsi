/**
 * 页面加载时显示等候信息
 * 
 */

$(function() {
	$.messager.progress({
		text : "加载中……",
		interval : 100,
		style : {
			right : '',
			bottom : ''
		}
	});
});
/**
 * 页面的easyui组件渲染完毕后关闭等待信息
 */
$.parser.onComplete = function() {
	window.setTimeout(function() {
		$.messager.progress("close");
	}, 500);
};

/**
 * 加载面板组件时的等待信息
 */
// $.fn.panel.defaults.loadingMessage = "数据加载中，请稍候…";
/**
 * 加载数据表格组件时的等待信息
 */
// $.fn.datagrid.defaults.loadMsg = "数据加载中，请稍候…";
/**
 * 更换主题
 * 
 * @author yepei
 * @requires jQuery,EasyUI
 * @param themeName
 */
yp.changeTheme = function(themeName) {
	var $easyuiTheme = $('#easyuiTheme');// 主题
	var headerbg = $("#headerbg");// 首页背景图
	var colorWelcome = $("#headerWelcome");// 欢迎字
	var headerLogo = $("#logo");// logo
	var colorTitle = $("#headerTitle");// 标题
	var url = $easyuiTheme.attr('href');
	var href = url.substring(0, url.indexOf('themes')) + 'themes/' + themeName + '/easyui.css';
	$easyuiTheme.attr('href', href);

	// 判断主题类型，如果主题名等于default
	// 、bootstrap、metro、metro-blue则将背景图、欢迎字、标题及logo复原并移除cookie值即可
	if (themeName == "default" || themeName == "bootstrap" || themeName == "metro" || themeName == "metro-blue") {
		var bgurl1 = "url('" + yp.getBasePath() + "/images/bootstrap.gif')";// 回归默认背景
		headerbg.css("backgroundImage", bgurl1);
		yp.cookie("headerbg", null);// 移除背景cookie
	} else {
		// 否则先只将背景更改为"bg_"+themeName，同时将欢迎字颜色、标题颜色和图片复原——
		var bgurl = "url('" + yp.getBasePath() + "/images/bg_" + themeName + ".gif')";
		headerbg.css("backgroundImage", bgurl);// 使用特定背景
		yp.cookie("headerbg", "bg_" + themeName, {
			expires : 7
		});
	}
	colorWelcome.css("color", "#ffffff");// 回归默认的白欢迎字
	colorTitle.css("color", "#020202");// 回归默认的黑色标题
	headerLogo.attr("src", yp.getBasePath() + "/images/logo_black.png");// 回归默认的logo图片
	yp.cookie("headerLogo", null);
	yp.cookie("color_white", null);
	yp.cookie("color_black", null);
	/** ************************************ */
	// 若主题名称为black，则将标题色值更改为0052A3、将logo图片更改为logo_black（颜色为：0052A3），将将标题色、logo图片存入cookie
	if (themeName == "black") {
		colorTitle.css("color", "#0052A3");
		var src1 = yp.getBasePath() + "/images/logo_black.png";
		headerLogo.attr("src", src1);
		yp.cookie("headerbg", "bg_" + themeName, {// 存入背景图片
			expires : 7
		});
		yp.cookie("color_black", "0052A3", {// 存入标题色
			expires : 7
		});
		yp.cookie("headerLogo", "logo_black", {// 存入logo
			expires : 7
		});
	}
	// 若主题名称为ui-dark-hive，则将标题色更改为fff、将logo图片更改为：logo_white（颜色为：fff）
	if (themeName == "ui-dark-hive") {
		colorTitle.css("color", "#ffffff");
		var src2 = yp.getBasePath() + "/images/logo_white.png";
		headerLogo.attr("src", src2);
		yp.cookie("headerbg", "bg_" + themeName, {// 存入背景图片
			expires : 7
		});
		yp.cookie("color_black", "ffffff", {// 存入标题色
			expires : 7
		});
		yp.cookie("headerLogo", "logo_white", {// 存入logo
			expires : 7
		});
	}
	// 将改变的值存入cookie

	var $iframe = $('iframe');
	if ($iframe.length > 0) {
		for (var i = 0; i < $iframe.length; i++) {
			var ifr = $iframe[i];
			try {
				$(ifr).contents().find('#easyuiTheme').attr('href', href);
			} catch (e) {
				try {
					ifr.contentWindow.document.getElementById('easyuiTheme').href = href;
				} catch (e) {
				}
			}
		}
	}

	yp.cookie('easyuiTheme', themeName, {
		expires : 7
	});
};

/**
 * 使用给定的键值对或其它参数创建一个cookie.
 * 
 * @example yp.cookie('the_cookie', 'the_value');
 * @desc 为cookie赋值.
 * @example yp.cookie('the_cookie', 'the_value', { expires: 7, path: '/',
 *          domain: 'jquery.com', secure: true });
 * @desc 使用所有参数创建cookie.
 * @example yp.cookie('the_cookie', 'the_value');
 * @desc 创建一个session域的cookie
 * @example yp.cookie('the_cookie', null);
 * @desc 给定空值以删除一个cookie. 注意，在使用时，给的参数（路径、域）需要和设置时保持一致。
 * 
 * @param String
 *            key cookie名称.
 * @param String
 *            value cookie的值.
 * @param Object
 *            options An object literal containing key/value pairs to provide
 *            optional cookie attributes.
 * @option Number|Date expires Either an integer specifying the expiration date
 *         from now on in days or a Date object. If a negative value is
 *         specified (e.g. a date in the past), the cookie will be deleted. If
 *         set to null or omitted, the cookie will be a session cookie and will
 *         not be retained when the the browser exits.
 * @option String path The value of the path atribute of the cookie (default:
 *         path of page that created the cookie).
 * @option String domain The value of the domain attribute of the cookie
 *         (default: domain of page that created the cookie).
 * @option Boolean secure If true, the secure attribute of the cookie will be
 *         set and the cookie transmission will require a secure protocol (like
 *         HTTPS).
 * @type undefined
 * 
 * @name yp.cookie
 * @cat Plugins/Cookie
 * 
 * Get the value of a cookie with the given key.
 * 
 * @example sy.cookie('the_cookie');
 * @desc Get the value of a cookie.
 * 
 * @param String
 *            key The key of the cookie.
 * @return The value of the cookie.
 * @type String
 * 
 * @name yp.cookie
 * @cat Plugins/Cookie
 * @author Klaus Hartl/klaus.hartl@stilbuero.de
 */
yp.cookie = function(key, value, options) {
	if (arguments.length > 1 && (value === null || typeof value !== "object")) {
		options = $.extend({}, options);
		if (value === null) {
			options.expires = -1;
		}
		if (typeof options.expires === 'number') {
			var days = options.expires, t = options.expires = new Date();
			t.setDate(t.getDate() + days);
		}
		return (document.cookie = [ encodeURIComponent(key), '=', options.raw ? String(value) : encodeURIComponent(String(value)), options.expires ? '; expires=' + options.expires.toUTCString() : '',
				options.path ? '; path=' + options.path : '', options.domain ? '; domain=' + options.domain : '', options.secure ? '; secure' : '' ].join(''));
	}
	options = value || {};
	var result, decode = options.raw ? function(s) {
		return s;
	} : decodeURIComponent;
	return (result = new RegExp('(?:^|; )' + encodeURIComponent(key) + '=([^;]*)').exec(document.cookie)) ? decode(result[1]) : null;
};

/**
 * 扩展validatebox，添加新的验证功能：二次密码须一致
 * 
 * @author yepei
 * 
 * @requires jQuery,EasyUI
 */
$.extend($.fn.validatebox.defaults.rules, {
	eqPwd : {/* 验证两次密码是否一致功能 */
		validator : function(value, param) {
			return value == $(param[0]).val();
		},
		message : '密码不一致！'
	}
});
/**
 * 扩展validatebox，添加新的验证功能：电话号码必须合法
 * 
 * @author yepei
 * 
 * @requires jQuery,EasyUI
 */
$.extend($.fn.validatebox.defaults.rules, {
	phone : {
		validator : function(value, param) {
			var tel = /(^(\d{3,4}-)?\d{7,8})$|(1[3,4,5,8][0-9]{9}$)/;
			return tel.test(value);
		},
		message : '请输入一个有效的电话号码！'
	}
});

/**
 * 扩展validatebox，添加新的验证功能：身份证号必须合法
 * 
 * @author yepei
 * 
 * @requires jQuery,EasyUI
 */
$.extend($.fn.validatebox.defaults.rules, {
	idcard : {
		validator : function(value, param) {
			var Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 ];// 加权因子;
			var ValideCode = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ];// 身份证验证位值，10代表X;
			value = value.trim();
			if (value.length == 15) {
				return isValidityBrithBy15IdCard(value);
			} else if (value.length == 18) {
				var a_idCard = value.split("");// 得到身份证数组
				if (isValidityBrithBy18IdCard(value) && isTrueValidateCodeBy18IdCard(a_idCard)) {
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
		},
		message : '请输入一个有效的身份证号码！'
	}
});
/**
 * 扩展validatebox，添加新的验证功能：密码只能是数字字母下划线
 * 
 * @author yepei
 * 
 * @requires jQuery,EasyUI
 */
$.extend($.fn.validatebox.defaults.rules, {
	password : {
		validator : function(value, param) {
			var v = value.trim();
			var p = /^[a-zA-Z0-9_]+$/;
			return p.test(v) && v.length >= param[0] && v.length <= param[1];
		},
		message : '{0}-{1}位数字字母下划线！'
	}
});
/**
 * 扩展validatebox，添加新的验证功能：要求输入中文字符
 * 
 * @author yepei
 * @requires jQuery,EasyUI
 * @example <input class="easyui-validatebox"
 *          data-options="validType:'zh[1,4]'">
 */
$.extend($.fn.validatebox.defaults.rules, {
	zh : {
		validator : function(value, param) {
			var v = value.trim();
			var zhong = /^[\u4E00-\u9FA5]+$/;
			return zhong.test(v) && v.length >= param[0] && v.length <= param[1];
		},
		message : '{0}-{1}位中文字符！'
	}
});
/**
 * 扩展validatebox，添加新的验证功能：select下拉框必须选择一个value值不空的项
 * 
 * @author yepei
 * 
 * @requires jQuery,EasyUI
 */
$.extend($.fn.validatebox.defaults.rules, {
	selectbox : {
		validator : function(value, param) {
			return param != "";
		},
		message : '请选择一个有效的条目！'
	}
});

/**
 * 扩展validatebox，添加新的验证功能：数字须符合长度要求
 * 
 * @author yepei
 * 
 * @requires jQuery,EasyUI
 */
$.extend($.fn.validatebox.defaults.rules, {
	number : {
		validator : function(value, param) {
			var p = /\d*/;
			var len = (value.length > param[0] - 1) && (value.length < param[1] + 1);
			return p.test(value) && len;
		},
		message : '请输入一个有效的数据！'
	}
});

/**
 * 防止panel/window/dialog组件超出浏览器边界
 * 
 * @author yepei
 * 
 * @requires jQuery,EasyUI
 */
yp.onMove = {
	onMove : function(left, top) {
		var l = left;
		var t = top;
		if (l < 1) {
			l = 1;
		}
		if (t < 1) {
			t = 1;
		}
		var width = parseInt($(this).parent().css('width')) + 14;
		var height = parseInt($(this).parent().css('height')) + 14;
		var right = l + width;
		var buttom = t + height;
		var browserWidth = $(window).width();
		var browserHeight = $(window).height();
		if (right > browserWidth) {
			l = browserWidth - width;
		}
		if (buttom > browserHeight) {
			t = browserHeight - height;
		}
		$(this).parent().css({/* 修正面板位置 */
			left : l,
			top : t
		});
	}
};
$.extend($.fn.dialog.defaults, yp.onMove);
$.extend($.fn.window.defaults, yp.onMove);
$.extend($.fn.panel.defaults, yp.onMove);

/**
 * panel关闭时回收内存，主要用于layout使用iframe嵌入网页时的内存泄漏问题
 * 
 * @author yepei
 * 
 * @requires jQuery,EasyUI
 * 
 */
$.extend($.fn.panel.defaults, {
	onBeforeDestroy : function() {
		var frame = $('iframe', this);
		try {
			if (frame.length > 0) {
				for (var i = 0; i < frame.length; i++) {
					frame[i].src = '';
					frame[i].contentWindow.document.write('');
					frame[i].contentWindow.close();
				}
				frame.remove();
				if (navigator.userAgent.indexOf("MSIE") > 0) {// IE特有回收内存方法
					try {
						CollectGarbage();
					} catch (e) {
					}
				}
			}
		} catch (e) {
		}
	}
});

/**
 * 
 * 通用错误提示
 * 
 * 用于datagrid/treegrid/tree/combogrid/combobox/form加载数据出错时的操作
 * 
 * @author yepei
 * 
 * @requires jQuery,EasyUI
 */
yp.onLoadError = {
	onLoadError : function(XMLHttpRequest) {
		if (parent.$ && parent.$.messager) {
			parent.$.messager.progress('close');
			parent.$.messager.alert('错误', XMLHttpRequest.responseText);
		} else {
			$.messager.progress('close');
			$.messager.alert('错误', XMLHttpRequest.responseText);
		}
	}
};
$.extend($.fn.datagrid.defaults, yp.onLoadError);
$.extend($.fn.treegrid.defaults, yp.onLoadError);
$.extend($.fn.tree.defaults, yp.onLoadError);
$.extend($.fn.combogrid.defaults, yp.onLoadError);
$.extend($.fn.combobox.defaults, yp.onLoadError);
$.extend($.fn.form.defaults, yp.onLoadError);

/**
 * 创建一个模式化的dialog
 * 
 * @author yepei
 * 
 * @requires jQuery,EasyUI
 * 
 */
yp.modalDialog = function(options) {
	var opts = $.extend({
		title : '&nbsp;',
		width : 640,
		height : 480,
		modal : true,
		onClose : function() {
			$(this).dialog('destroy');
		}
	}, options);
	opts.modal = true;// 强制此dialog为模式化，无视传递过来的modal参数
	if (options.url) {
		opts.content = '<iframe id="" src="' + options.url + '" allowTransparency="true" scrolling="auto" width="100%" height="98%" frameBorder="0" name=""></iframe>';
	}
	return $('<div/>').dialog(opts);
};

/**
 * 根据下拉列表框中条目的text值获得下拉列表框中的value值， 适用于符合id:"",text:"",格式的combobox
 * 
 * @param inputId
 *            combobox组件的id
 * @param itemText
 *            combobox组件中选项的显示文本
 * @return combobox组件中itemText文本对应的value值
 */
yp.getComboboxValue = function(inputId, itemText) {
	// 如果向服务器获取，则会太慢
	// $.get(yp.bp()+'/sys/rightAction!getTypeValue.action',{'right.description':text},function(data){return
	// data;});
	var data = $("#" + inputId).combobox('getData');
	for (var i = 0; i < data.length; i++) {
		if (data[i].text == itemText) {
			return data[i].id;
		}
	}
	return null;
};
