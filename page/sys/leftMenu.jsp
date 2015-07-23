<%@ page language="java" pageEncoding="UTF-8"%>
<style>
a[id^="menubtn"] { width: 79%; height:30px; margin-bottom: 2px;text-align: left;padding-left:20%;}
/* 该对象用户在IE下显示预览图片 */
div[id^='preview_fake_'] {filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);}
/* 该对象只用来在IE下获得图片的原始尺寸，无其它用途 */
div[id^='preview_size_fake_'] {filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=image);}
</style>
<div class="easyui-accordion" data-options="fit:true,border:false">
	<div title="任务管理" data-options="iconCls:'icon-yp_task',selected:'true'">
		<div style="padding:5px 10px;">
		<h3 style="color:#0099FF;"><img src="${pageContext.request.contextPath }${user.info}" width="25px" height="25px" alt="" style="vertical-align:middle;">&nbsp;
		${empty(user.username)?user.name:user.username}，您好！</h3>
		<p>　　您可以查看当前任务，请对需要处理的任务及时的进行处理！</p></div>
		<!-- 注：由于easyui的default主题和gray主题不支持linkbutton的宽度属性，即无法通过width指定按钮宽度，所以这里只能靠空格撑开了 -->
		<a id="menubtn11" href="#" onclick="addTab(this.id)" class="easyui-linkbutton admin" data-options="iconCls:'icon-yp_task'">待 审 核 任 务</a><br>
		<a id="menubtn12" href="#" onclick="addTab(this.id)" class="easyui-linkbutton" data-options="iconCls:'icon-yp_historydoc'" >处 理 中 任 务</a><br>
		<a id="menubtn13" href="#" onclick="addTab(this.id)" class="easyui-linkbutton" data-options="iconCls:'icon-yp_alltask'">所 有 任 务</a>
		<a id="menubtn14" href="#" onclick="addTab(this.id)" class="easyui-linkbutton admin" data-options="iconCls:'icon-add'">新 建 任 务</a><br>
		<a id="menubtn15" href="#" onclick="addTab(this.id)" class="easyui-linkbutton admin" data-options="iconCls:'icon-yp_chart'">统 计 分 析</a><br>
	</div>
	<div title="用户管理" data-options="iconCls:'icon-yp_user'">
		<a id="menubtn21" href="#" onclick="addTab(this.id)" class="easyui-linkbutton admin" data-options="iconCls:'icon-yp_expert'">专 家 用 户 管 理</a> <br>
		<a id="menubtn22" href="#" onclick="addTab(this.id)" class="easyui-linkbutton admin" data-options="iconCls:'icon-yp_sysuser'">系 统 用 户 管 理</a>
		<div class="expert" style="color:#f00;font-size: 16px;padding: 10px 10px">您没有用户管理模块的操作权限</div>
	</div>
</div>
<script>
	/**************添加tabs面板*******************/
	var editSubmit=false;//在taskedit编辑页面，此变量标志表单是否已经提交，若已经提交则关闭编辑框时不再提示
	var searchPage=undefined;//发出高级查找请求的tab页
	  var checkedRow=undefined;//定义成全局变量，以供在任务处理对话框所在页面中的js调用
	function addTab(id) {
		var title = $("#" + id).text().replace(/\s+/g,"");//去掉字符串中全部的任何空白字符
		var tab = $('#mainTabs').tabs('getTab', title);
		if (tab) {
			$('#mainTabs').tabs('select', title);
		} else {
			var url = yp.bp() + "/page/sys/tabs/" + id.substring(id.length - 2, id.length) + ".jsp";
			var icon=$("#" + id).linkbutton('options','iconCls').iconCls;//获得按钮图标
			var p = $("#mainTabs");
				p.tabs('add', {
				title : title,
				href : url,
				iconCls: icon,
				closable : true,
			});
		}
	}
$(function() {
		/**************tabs面板的刷新和关闭按钮*******************/
		var mainTabs = $('#mainTabs');
		mainTabs.tabs({
			fit : true,
			border : false,
			tools : [ {
				text : '刷新页面',
				iconCls : 'icon-reload',
				handler : function() {
					var tab = mainTabs.tabs('getSelected');
					var index = mainTabs.tabs('getTabIndex', tab);
					if(index==0){
						var ifrm=document.getElementById("welcomePage");
						ifrm.src=ifrm.src;
					}else{
						try {
							tab.panel("refresh");
						} catch (e) {
						}
					}
				}
			}, {
				text : '关闭',
				iconCls : 'icon-cancel',
				handler : function() {
					var index = mainTabs.tabs('getTabIndex', mainTabs.tabs('getSelected'));
					var tab = mainTabs.tabs('getTab', index);
					if (tab.panel('options').closable) {
						mainTabs.tabs('close', index);
					} else {
						$.messager.alert('提示', '[' + tab.panel('options').title + ']不可以被关闭！', 'error');
					}
				}
			}, {
				text : '全部关闭',
				iconCls : 'icon-yp_logout',
				handler : function() {
					var tabs = mainTabs.tabs('tabs');
					var index = tabs.length - 1;
					while (index > 0 && tabs[index].panel('options').closable) {
						mainTabs.tabs('close', index);
						index--;
					}
				}
			}, {
				text : '关闭其它',
				iconCls : 'icon-undo',
				handler : function() {
					var tabs = mainTabs.tabs('tabs');
					var index = tabs.length - 1;
					var currentIndex = mainTabs.tabs('getTabIndex', mainTabs.tabs('getSelected'));
					while (index > 0) {
						if (tabs[index].panel('options').closable && index != currentIndex) {
							mainTabs.tabs('close', index);
						}
						index--;
					}
				}
			} ],
		});
	});
</script>
<!-- ==================凭证文件列表窗体============================= -->
<div id="filesList" class="easyui-dialog" data-options="
	    title: '凭证文件',    
	    width: 400,    
	    closed: true,    
	    cache: false,  
	    iconCls:'icon-yp_file',
	    modal: true,
	    onClose:function(){
	    	$('#filesList ul').html('');
	    },
	    buttons:[{text:'关闭',
			handler:function(){$('#filesList').dialog('close');}}]
"><ul></ul></div>  
<!-- ==================用户详情窗体============================= -->
<div id="userDetailInfo" style="background-color: #EFF4FB" class="easyui-dialog" data-options="
					title: '用户详情',    
				    width: 430,    
				    height: 460,    
				    cache: true,
				    modal: true,
	    		    iconCls: 'icon-yp_user',
				    closed:true,
					buttons:[{
					text:'关闭',
					iconCls:'icon-cancel',
					handler:function(){$('#userDetailInfo').dialog('close');}
				}]
">
		<div style="background-color: #EEE2D1;padding:2px 0; " >
					<div style="margin:2px auto;width:95%;height: auto;">&nbsp;&nbsp;
						<img src="../../js/easyui/themes/icons/tip.png">
						<span style="font-size: 12px;color:#444"><label>提示：若不能正常显示，请关闭此对话框再重新打开！</label></span>
					</div>
			</div>
		<fieldset id="userdtl1" style="border-color: #eee;border-radius:5px;margin-top: 15px;">
			<legend><b>基本信息</b></legend>
					<table style="width:370px; font-size: 12px;">
					<tr>
						  <td style="width: 60px;" id="nameLabel">姓名:</td><td colspan="3"><input id="detail_name" type="text" style="width: 180px;"></td>
						  <td style="width:90px" colspan="2" rowspan="2"><img width="80px" height="80px" src="${pageContext.request.contextPath }/images/userImage.png"></td>
					  </tr>
					<tr>
						  <td >申请次数:</td>
						  <td><input   id="detail_applycount" type="text" style="width: 40px;"></td>
						  <td style="width: 50px; text-align: right;">用户类型:</td>
						  <td><input id="detail_usertype" type="text" style="width: 80px;"/></td>
					  </tr>
					<tr>
					  <td>当前任务编号:</td>
					  <td colspan="4"><input id="detail_currentTask_caseid" type="text" style="width: 180px;"></td>
					 </tr>
					 <tr  style="line-height: 30px;">
					  <td id="idcardLabel">身 份 证 号:</td>
					  <td colspan="4"><input id="detail_idcard" type="text" style="width: 270px;"></td>
					  </tr>
					</table>
				</fieldset>
		<fieldset  id="userdtl2" style="border-color:#efefef;border-radius:5px;margin-top: 10px;">
					<legend><b>联系方式</b></legend>
					<table style="width:370px; font-size: 12px;">
					<tr>
					  <td style="width: 60px;">电话:</td>
					  <td colspan="3"><input id="detail_phone" type="text" style="width: 200px;"></td>
					  <td style="width:90px" colspan="2" rowspan="2"><img width="80px" height="80px" src="${pageContext.request.contextPath }/images/userPhone.png"></td>
					  </tr>
					<tr>
					  <td >E-mail:</td>
					  <td colspan="3"><input id="detail_email" type="text" style="width: 200px;"></td>
					</tr>
					<tr style="line-height: 30px;">
					  <td >通讯地址:</td>
					  <td colspan="5"><input id="detail_address" type="text" style="width:288px;"></td>
					  </tr>
					</table>
			</fieldset>
</div>
<!-- ==================高级查找窗体===========================-->
<div id="advancedSearch" style="background-color: #EFF4FB" class="easyui-dialog" data-options="
				 title: '高级查找',    
			    width: 600,    
			    height: 360,    
			    cache: true,
			    modal: true,
			    maximizable:true,
			    resizable:true,
    		    iconCls: 'icon-yp_detailsearch',
			    closed:true,
				buttons:[{
				text:'清空条件',
				iconCls:'icon-yp_clean',
				handler:function(){yp.fillFormValues('#adSearchForm', null, null,['ad.adstatus']);}
			},{
				text:'查找',
				iconCls:'icon-yp_detailsearch',
				handler:function(){advancedSearch();	}
			},{
				text:'取消',
				iconCls:'icon-cancel',
				handler:function(){$('#advancedSearch').dialog('close');}
			}]
">
		<form id="adSearchForm" method="post">
		<fieldset style="border-radius:5px;margin-top: 15px;">
			<legend><b>精确查询</b></legend>
						<table style="font-size: 12px;">
								<tr>
									<td style="width: 50px;">编号：</td><td><input class="easyui-validatebox" name="ad.adcaseid" style="width: 130px;"></td>
									<!-- url: '${pageContext.request.contextPath }/sys/taskAction!getStateList.action' -->
									<td style="width: 50px;">状态：</td><td ><input id="statuschoose" name="ad.adstatus"style="width: 100px;"></td>
									<td style="width: 70px;">申请主体：</td><td><input class="easyui-combobox" id="atype" name="ad.adapplytype" style="width: 110px;" data-options="editable:false,valueField:'id',textField:'text',data:[{id:'Consumer',text:'消费者申请'},{id:'Saler',text:'商家申请'}], panelHeight:'auto' "></td>
								</tr>
								<tr>
									<td></td><td style="text-align: right;">申请日期：</td>
									<td>从：</td><td><input class="easyui-datebox" name="ad.adstarttime" style="width:100px" data-options="editable:false"> </td>
									<td style="text-align: center;">到</td><td><input class="easyui-datebox" name="ad.adendtime" style="width:110px" data-options="editable:false"> </td>
								</tr>
						</table>
				</fieldset>
				<fieldset style="border-radius:5px;margin-top: 10px;">
					<legend><b>关键字查找</b></legend>
					<div style="padding:10px 0;text-align: center;margin-bottom: 10px;background-color: #EEE2D1;"> 
						<img src="${pageContext.request.contextPath }/js/easyui/themes/icons/tip.png">
				    	<span style="font-size: 12px;color:#444">可以输入检索字段的部分关键字进行查询</span>
					</div>
					<div>
						<span><label>　　申请方：</label><input class="easyui-validatebox" name="ad.adname1"></span>
						<span style="margin-left: 65px;"><label>　　责任方：</label><input class="easyui-validatebox" name="ad.adname2"></span>
					</div>
					<div style="margin-top: 10px;">
						<span><label>　汽车型号：</label><input class="easyui-validatebox" name="ad.admodel"></span>
						<span style="margin-left: 65px;"><label>　申请事项：</label><input class="easyui-validatebox" name="ad.admatter"></span>
					</div>
			</fieldset>
		</form>
</div> 
<script>
/**
 * 打开一个单独的tab来执行编辑功能
 */
function addEditTab(ele, method, gridID) {
	var rows = $("#" + gridID).datagrid('getChecked');
	if (!rows || rows.length != 1) {
		$.messager.alert('提示', '请选择一个需要编辑的对象！', 'info');
	} else {
		checkedRow = rows[0];
		// 以下是打开新tab的代码
		var title = method == 'edit' ? "任务编辑" : "任务信息";
		var tab = $('#mainTabs').tabs('getTab', title);
		if (tab) {
			$.messager.confirm('提示', '当前正有任务处于编辑状态！', function(ok) {
				if (ok) {
					$('#mainTabs').tabs('select', title);
				}
			});
		} else {
			var url = yp.bp() + "/page/sys/tabs/taskedit.jsp?method=" + method + "&taskid=" + checkedRow.id + "&gridSelector=" + gridID;
			var icon = $(ele).linkbutton('options', 'iconCls').iconCls;// 获得按钮图标
			var p = $("#mainTabs");
			p.tabs({
				onBeforeClose : function(t, index) {
					if (t == '任务编辑') {// 若未提交，则弹窗提示，若已经提交，则直接关闭
						if (editSubmit) {// 直接关闭
							closeTab(p, index);
						} else {// 弹窗提示
							$.messager.confirm('确认对话框', '关闭此窗口会丢失正在编辑的数据，确定要关闭？', function(r) {
								if (r) {
									closeTab(p, index);
								}
							});
						}
					} else {// 直接关闭
						closeTab(p, index);
					}
					return false;
				}
			}).tabs('add', {
				title : title,
				href : url,
				iconCls : icon,
				closable : true
			});
			editSubmit = false;// 打开面板时将此属性置为false
		}
	}
}
function closeTab(p, index) {// 直接关闭Tab
	var opts = p.tabs('options');
	var bc = opts.onBeforeClose;
	opts.onBeforeClose = function() {
	}; // 允许现在关闭
	p.tabs('close', index);
	opts.onBeforeClose = bc; // 还原事件函数
}

/**
 * 按日期搜索
 */
function simpleSearch(page, gridSelector, searchboxSelector) {
	var ldtp = undefined;
	var bytime = undefined;
	if (page == '11') {
		ldtp = "toaudit";
		bytime = "toaudit_bytime";
	} else if (page == '12') {
		ldtp = "processing";
		bytime = "processing_bytime";
	}else if(page=='13'){
		ldtp = "all";
		bytime = "all_bytime";
	}
	$(gridSelector).datagrid("clearChecked");
	var stimeSelector = searchboxSelector + " input[id='starttime_" + page + "']";
	var etimeSelector = searchboxSelector + " input[id='endtime_" + page + "']";
	var stime = $(stimeSelector).datebox('getValue');
	var etime = $(etimeSelector).datebox('getValue');
	if (stime.trim() == '' && etime.trim() == '') {
		$(gridSelector).datagrid('reload', {
			loadtype : ldtp
		});
	} else {
		$(gridSelector).datagrid('load', {
			starttime : stime,
			endtime : etime,
			loadtype : bytime
		});
	}
}

function openAdvancedSearchDialog(type) {
	searchPage = type;
	var dg = $("#statuschoose");
	var comb = undefined;
	var flag='enable';
	var comboData=undefined;
	if (searchPage == 'toaudit_advanced') {
		comboData=[ {id : 'toaudit',	text : '待审核',selected : true} ];flag='disable';
	} else if (searchPage == 'processing_advanced') {
		comboData=[ 
		            {id : 'consulted',text : '已出咨询意见'}, 
		            {id : 'processing',text : '处理中',selected : true}, 
		            {id : '',text : '两者'}];flag='enable';
		
	}else if('all_advanced'){
		var ut="${logintype}";
		if(ut&&ut=='Expert'){
		comboData=[ 
		            {id : 'consulted',text : '已出咨询意见'}, 
		            {id : 'processing',text : '处理中',selected : true}, 
		            {id : 'terminate',text : '调解终止'}, 
		            {id : 'complete',text : '调解完成'}, 
		            {id : '',text : '全部'}];
		}else{
		comboData=[ 
		            {id : 'consulted',text : '已出咨询意见'}, 
		            {id : 'processing',text : '处理中',selected : true}, 
		            {id : 'toaudit',text : '待审核'} ,
		            {id : 'refused',text : '不予受理'} , 
		            {id : 'terminate',text : '调解终止'}, 
		            {id : 'complete',text : '调解完成'}, 
		            {id : '',text : '全部'}];
		}
		flag='enable';
	}
		comb = dg.combobox({
			editable : false,
			valueField : 'id',
			textField : 'text',
			panelHeight : 'auto',
			data : comboData
		}).combobox(flag);
	$('#advancedSearch').dialog('open');
}
/**
 * 高级查找
 */
function advancedSearch() {
	if (searchPage) {
		var gridSt = undefined;
		if (searchPage == 'toaudit_advanced') {
			gridSt = $('#taskList_11');
		} else if (searchPage == 'processing_advanced') {
			gridSt = $('#taskList_12');
		}else if(searchPage == 'all_advanced'){
			gridSt = $('#taskList_13');
		}
	}
	gridSt.datagrid("clearChecked");
	var isNull = true;// 判断表单字段是不是全为空
	var formArray = $("#adSearchForm").serializeArray();
	$.each(formArray, function(i) {
		if (this.value != "") {
			isNull = false;
			return false;
		}
	});
	if (isNull) {
		$.messager.alert('提示', '搜索条件不能为空！', 'info');
		return;
	}// 如果用户没有填写任何搜索条件，则提示并返回。
	var params = yp.mergeObject(yp.serializeObject('#adSearchForm'), {
		loadtype : searchPage
	});// 合并对象，为表单添加loadtype字段
	var name1 = $("#adSearchForm input[name='ad.adname1']").val();
	var name2 = $("#adSearchForm input[name='ad.adname2']").val();
	var atype = $("#adSearchForm #atype").combobox("getValue");
	if ((name1 != "" || name2 != "") && atype == "") {
		$.messager.alert('提示', '当填写了申请方或责任方搜索条件时，请顺便填写申请主体！', 'info');
	} else {
		gridSt.datagrid('load', params);
		$('#advancedSearch').dialog('close');
	}
}
/**
 * 显示凭证文件
 */
function showFiles(fileinfo) {
	var files = fileinfo.split("#");
	for ( var i in files) {
		var f = files[i].split(",");
		$("#filesList ul").append("<li><a href='javascript:void(0);' onclick='showFile(\"" + f[0] + "\",\""+f[1]+ "\")'>" + f[1] + "</a></li>");
	}
	$('#filesList').dialog('open');
}
/**
 * 通过文件的存储名称显示凭证文件
 */
function showFile(filePath,fileName) {
	$.get(yp.bp() + "/sys/taskAction!fileExist.action?doc=" + filePath, function(r) {
		if (!r.ok) {
			$.messager.alert('提示', r.msg, 'info');
		} else {
			window.location = yp.bp() + "/sys/taskAction!showFile.action?doc=" + filePath+"&sort="+fileName;
		}
	}, 'json');
}
/**
 * 为其它信息连接绑定点击事件
 */
function bindClick(wicth) {
	var uinfo = $("a[id^='rowlink" + wicth + "_']");
	for (var i = 0; i < uinfo.length; i++) {
		// u.click(function (event) { event.preventDefault();});//解绑默认的点击事件
		$(uinfo[i]).click(function(event) {
			showUserInfo(event.target.title);
		});// 添加点击事件并传递参数
	}
}
/**
 * 显示用户详情
 */
function showUserInfo(userid) {
	var info = userid.split("#");
	var ut = info[1];
	var usertype = ut == "消费者" ? "Consumer" : "Saler";
	$.get(yp.bp() + '/sys/taskAction!getUserDetailInfo.action',// 加载用户数据
	{
		id : info[0],
		usertype : usertype
	}, function(r) {
		if (r.ok) {
			var o = r.object;
			if (usertype == 'Consumer') {
				$("#nameLabel").html("姓名：");
				$("#idcardLabel").html("身份证号：");
				$("#detail_idcard").val(o.idcard);
			} else {
				$("#nameLabel").html("单位名称：");
				$("#idcardLabel").html("机构代码：");
				$("#detail_idcard").val(o.code);
			}
			$("#detail_name").val(o.name);
			$("#detail_applycount").val(o.taskcount);
			$("#detail_usertype").val(ut);
			$("#detail_currentTask_caseid").val(o.currentTask_caseid);
			$("#detail_phone").val(o.phone);
			$("#detail_email").val(o.email);
			$("#detail_address").val(o.address);
			var css = {
				"boder" : "none",
				"background" : "#F0F5FB"
			};
			$("#userdtl1 input,#userdtl2 input").css(css);
			$("#userdtl1 input,#userdtl2 input").attr({
				"readonly" : "readonly"
			});
			$.messager.progress('close');
			$("#userDetailInfo").dialog("open"); // 打开户详细信息对话框
		} else {
			$.messager.alert('提示', r.msg, 'info');
		}
	}, 'json');
}

function showdetail(gridSelector) {
	var rows = $(gridSelector).datagrid('getChecked');
	if (rows.length < 1) {
		$.messager.alert('提示', '请选择一个对象！', 'info');
	} else if(rows.length>1){
		$.messager.alert('提示', '选择的条目过多！', 'info');
	}else{
		checkedRow = rows[0];
		window.open(yp.bp() + "/user/applyAction!onlinePrelook.action?looker=admin&docname=applydoc_server&taskid=" + checkedRow.id + "&timestamp=" + Math.random());
	}
}
/**
 *@param fileTag 文件上传域的dom对象，即type="file"的input标签
 *@param targetId 要显示图片的目标元素id
 */
function preImage(fileTag, targetId) {
	if (!fileTag.value.match(/.jpg|.gif|.png|.bmp|.jpeg/i)) {
		alert('图片格式无效！');
		return false;
	}
	var objPreview = document.getElementById('preview_img' + targetId);
	var objPreviewFake = document.getElementById('preview_fake' + targetId);
	if (fileTag.files && fileTag.files[0]) {
		// Firefox 因安全性问题已无法直接通过 input[file].value 获取完整的文件路径  
		//下边这句在FF下不好使,改成下边那句
		//objPreview.src = sender.files[0].getAsDataURL();
		objPreview.src = window.URL.createObjectURL(fileTag.files[0]);
	} else if (objPreviewFake.filters) {
		// IE7,IE8 在设置本地图片地址为 img.src 时出现莫名其妙的后果     
		//（相同环境有时能显示，有时不显示），因此只能用滤镜来解决     
		// IE7, IE8因安全性问题已无法直接通过 input[file].value 获取完整的文件路径     
		fileTag.select();
		var imgSrc = document.selection.createRange().text;
		objPreviewFake.filters.item('DXImageTransform.Microsoft.AlphaImageLoader').src = imgSrc;
		objPreview.style.display = 'none';
	}
}
</script>