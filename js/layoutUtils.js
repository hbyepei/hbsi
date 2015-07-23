function styleProperties() {
	styleProperties.prototype.header_height = "150px";
	styleProperties.prototype.content_width = "66%";
	styleProperties.prototype.left_width = "16%";
	styleProperties.prototype.leftmargin = "1%";// 对应于content的margin-right属性
	styleProperties.prototype.right_width = "16%";
	styleProperties.prototype.rightmargin = "1%";// 对应于left的margin-right属性
	styleProperties.prototype.footer_height = "50px";
}
/**
 * 布局
 */
function layout(styles) {
	if (!styles) {
		styles = new styleProperties();
	}
	var header = getElementsByClassName("header");
	var left = getElementsByClassName("left");
	var content = getElementsByClassName("content");
	var right = getElementsByClassName("right");
	var footer = getElementsByClassName("footer");
	for ( var s in styles) {
		var value = styles[s];
		var p = s.trim().toLowerCase();
		var name = undefined;
		var obj = undefined;
		if (p == "header_height") {// 检查每一个属性
			obj = header;
		} else if (p == "content_width" || p == "leftmargin") {
			obj = content;
		} else if (p == "left_width" || p == "rightmargin") {
			obj = left;
		} else if (p == "right_width") {
			obj = right;
		} else if (p == "footer_height") {
			var container = getElementsByClassName("container");
			addStyle(container, {
				"marginBottom" : "-" + value + "",
			});
			obj = footer;
		}
		if (obj) {
			var t = p.split("_");
			name = (t.length > 1 ? t[1] : "marginRight");
			var v = eval("({\"" + name + "\":\"" + value + "\"})");
			addStyle(obj, v);
		}
	}
}
/**
 * 根据类名获取DOM元素数组
 */
function getElementsByClassName(n) {
	var classElements = [], allElements = document.getElementsByTagName('*');
	if (allElements.length > 0) {
		for (var i = 0; i < allElements.length; i++) {
			if (allElements[i].className == n) {
				classElements[classElements.length] = allElements[i];
			}
		}
		return classElements;
	}
	return null;
}
/**
 * 为一批DOM元素增加字符串形式的CSS样式
 */
function addStyle(elements, styles) {
	if (elements) {
		for (var i = 0; i < elements.length; i++) {
			for ( var s in styles) {
				elements[i].style[s] = styles[s];
			}
		}
	}
}
/**
 * 检测浏览器的分辨率，自动将给定id的组件进行缩放
 * 
 * @param id
 */
function zoom(id) {
	var w = screen.width;
	var h = screen.height;
	var d = w * h;
	var zoom = 1;
	var e = document.getElementById(id);
	if (d <= 860 * 720) {
		zoom = 0.7;
	} else if (d <= 1024 * 768) {
		zoom = 0.70;
	} else if (d <= 1280 * 768) {
		zoom = 0.85;
	} else if (d <= 1400 * 900) {
		zoom = 0.9;
	} else if (d <= 1600 * 900) {
		zoom = 0.95;
	} else if (d <= 1920 * 1080) {
		zoom = 1;
	} else {
		zoom = 1.2;
	}
	// e.style.zoom=zoom;
	$(e).css({
		"overflow" : "hidden",
		"zoom" : zoom,
		"-moz-transform" : "scale(" + zoom + ")",
		"-moz-transform-origin" : "top left",
		"-o-transform" : "scale(" + zoom + ")",
		"-o-transform-origin" : "top left"
	});
}
// window.onload=layout({
// header_height:"150px",//指定头部的高度
// footer_height : "50px",//指定页脚的高度
// //以下五个占比之和应当为100%
// content_width: "66%",//指定内容区的宽度占比
// left_width : "16%",//指定左区的宽度占比
// leftmargin : "1%",//指定左间隙的宽度占比，对应于content的margin-right属性
// right_width : "16%",//指定右区的宽度占比
// rightmargin : "1%"//指定右间隙的宽度占比，对应于left的margin-right属性
// });
