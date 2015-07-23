<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style>
#taskedit_form *{font-size: 12px;}
#taskedit_form fieldset{border-color: #fefefe;border-radius:5px;margin-top: 15px;font-weight: bold;font-size: 14px;}
#taskedit_form fieldset table{width: 100%;}
#taskedit_form fieldset ul li{list-style: lower-roman;}
#taskedit_form fieldset ul li div:hover{background-color:  #D6F4D8;}
#taskedit_form fieldset table tr{line-height: 28px;}
#taskedit_form fieldset table td:nth-child(odd){text-align: right;vertical-align: top;}
#taskedit_form fieldset table td textarea{height: 50px;width: 93%;resize:none}
.need{color:red;font-weight: bolder;margin-right: 5px;}
</style>
<div style="padding: 50px 50px 0 50px;">
	<div style="width: 100%;">
	<form id="taskedit_form" method="post" enctype="multipart/form-data">
		<input id="taskedit_taskid" name="tm.id" type="hidden">
		<span><label class="need">*</label>申请类型：</span>
		<input name="tm.applytype" id="taskedit_applytype_consumer" type="radio"  value="消费者" >
		<label for="taskedit_applytype_consumer">消费者(家用车车主)</label>
		<input name="tm.applytype" id="taskedit_applytype_saler" type="radio"  value="单位用户">
		<label for="taskedit_applytype_saler">修理商/销售商/制造商</label>
		<hr>
		<fieldset style="display: inline-block;width: 45%;margin-right: 10px;">
			<legend>消费者信息</legend>
			<table>
				<tr>
					<td><label class="need">*</label>姓名:</td>
					<td><input name="tm.name_1" class="easyui-validatebox" data-options="required:true"></td>
					<td><label class="need code_1">*</label>身份证号:</td>
					<td><input name="tm.code_1" class="easyui-validatebox" data-options="required:true,validType:'idcard'"></td>
				</tr>
				<tr>
					<td>联系人:</td>
					<td><input name="tm.agent_1" type="text"></td>
					<td><label class="need">*</label>联系方式:</td>
					<td><input name="tm.phone_1" class="easyui-validatebox" data-options="required:true,validType:'phone'"></td>
				</tr>
				<tr>
					<td>E-mail:</td>
					<td colspan="3"><input name="tm.email_1" class="easyui-validatebox" data-options="validType:'email'"></td>
				</tr>
				<tr>
					<td>通信地址:</td>
					<td colspan="3"><textarea name="tm.address_1"></textarea></td>
				</tr>
			</table>
		</fieldset>
		<fieldset style="display: inline-block;width: 45%;">
			<legend>机构信息</legend>
			<table>
				<tr>
					<td><label class="need">*</label>单位名称:</td>
					<td><input name="tm.name_2" class="easyui-validatebox" data-options="required:true"></td>
					<td><label class="need code_2">*</label>机构代码:</td>
					<td><input name="tm.code_2" class="easyui-validatebox"  data-options="required:true,validType:'length[8,9]'"></td>
				</tr>
				<tr>
					<td>联系人:</td>
					<td><input name="tm.agent_2" type="text"></td>
					<td><label class="need">*</label>联系方式:</td>
					<td><input name="tm.phone_2" class="easyui-validatebox" data-options="required:true,validType:'phone'"></td>
				</tr>
				<tr>
					<td>E-mail:</td>
					<td colspan="3"><input name="tm.email_2" class="easyui-validatebox" data-options="validType:'email'"></td>
				</tr>
				<tr>
					<td>通信地址:</td>
					<td colspan="3"><textarea name="tm.address_2"></textarea></td>
				</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend>车辆信息</legend>
			<table>
				<tr>
					<td><label class="need">*</label>车辆品牌:</td>
					<td><input name="tm.brand" class="easyui-validatebox" data-options="required:true"></td>
					<td><label class="need">*</label>车辆型号:</td>
					<td><input name="tm.model" class="easyui-validatebox" data-options="required:true"></td>
					<td>车架号:</td>
					<td><input name="tm.carriage" type="text"></td>
					<td>购车日期:</td>
					<td><input name="tm.dealtime"  class="easyui-datebox" data-options="editable:false"></td>
				</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend><label class="need">*</label>发生事故的部位</legend>
			<table>
				<tr><td style="text-align: left;">
					<span><input id="acd_0" class="ckbx" type="checkbox" name="cap.chesheng" /><label for="acd_0">车身</label></span>&nbsp;&nbsp;
                	<span><input id="acd_1" class="ckbx"  type="checkbox" name="cap.chuandong"/><label for="acd_1">传动系</label></span>&nbsp;&nbsp;
                	<span><input id="acd_2"  class="ckbx" type="checkbox" name="cap.dianqishebei"/><label for="acd_2">电气设备</label></span>&nbsp;&nbsp;
                	<span><input  id="acd_3" class="ckbx" type="checkbox" name="cap.fadongji"/><label for="acd_3">发动机</label></span>&nbsp;&nbsp;
                	<span><input id="acd_4"  class="ckbx" type="checkbox" name="cap.chelun"/><label for="acd_4">车轮和轮胎</label></span>&nbsp;&nbsp;
                	<span><input id="acd_5" class="ckbx"  type="checkbox" name="cap.qinang"/><label for="acd_5">气囊和安全带</label></span>&nbsp;&nbsp;
                	<span><input  id="acd_6"class="ckbx"  type="checkbox" name="cap.xuanjiaxi"/><label for="acd_6">悬架系</label></span>&nbsp;&nbsp;
                	<span><input id="acd_7" class="ckbx"  type="checkbox" name="cap.zhidongxi"/><label for="acd_7">制动系</label></span>&nbsp;&nbsp;
                	<span><input id="acd_8" class="ckbx"  type="checkbox" name="cap.zhuanxiangxi"/><label for="acd_8">转向系</label></span>&nbsp;&nbsp;
                	<span><input  id="acd_9" class="ckbx" type="checkbox" name="cap.fujiashebei"/><label for="acd_9">附加设备</label></span>
				</td></tr>
			</table>
		</fieldset>
		<fieldset>
			<legend><label class="need">*</label>三包责任争议简要情况</legend>
			<table>
				<tr><td style="text-align: left;">
					<textarea name="tm.description" class="easyui-validatebox" data-options="required:true"  style="height: 120px;border: none;"></textarea>
				</td></tr>
			</table>
		</fieldset>
		<fieldset >
			<legend><label class="need">*</label>申请事项</legend>
			<table>
				<tr><td style="text-align: left;">
					<textarea name="tm.matter" class="easyui-validatebox" data-options="required:true" style="border: none;"></textarea></td>
				</tr>
			</table>
		</fieldset>
		<fieldset>
			<legend>附件列表</legend>
			<ul>
			</ul>
			<input type="file" name="upfile" multiple="multiple" id="addattachment" style="margin-left: 30px;"/>
		</fieldset>
		<fieldset>
			<legend>分配专家</legend>
			<input name="tm.needexpert"  type="checkbox"  id="taskedit_needexpert"><label for="taskedit_needexpert">专家介入</label>
			<!-- ==================专家选择窗体============================= -->
		        <select id="taskedit_chooseExperts" class="easyui-combogrid" name="tm.info" style="width:250px" data-options="
			            panelWidth: 600,
			            panelHeight: 300,
			            panelMinWidth: '50%',
			            top:100,
			            idField: 'id',
			            textField: 'username',
			            url: '../../sys/expertAction!getExperts.action?loadtype=combogrid',
			            method: 'get',
			            multiple: true,
			            striped:true,
			            disabled:true,
			            remoteSort:false,
			            fit: true,
			            editable:false,
			            selectOnNavigation:false,
			            columns: [[
			                {field:'id',checkbox:true},
			                {field:'image',title:'头像',width:40,formatter: function(value,row,index){
									return '&lt;a href=\'javascript:void(0);\' title=\''+value+'\' onclick=\'yp.showPic(this.title);\' &gt;&lt;img src=\''+yp.bp()+value+'\' alt=\''+row.name+'\'  style=\'width:20px;height:20px;\'&gt;&lt;a/&gt;';
								}
			                },
			                {field:'username',title:'姓名',width:50,sortable:true},
			                {field:'letterid',title:'聘书编号',width:100,sortable:true},
			              	{field:'gender',title:'性别',width:30,sortable:true},
			              	{field:'currenttasks',title:'正处理的任务数',width:100,align:'center',sortable:true},
			                {field:'technology',title:'技术组',width:80,sortable:true},
			                {field:'brand',title:'现服品牌',width:150,sortable:true},
			                {field:'area',title:'来源地区',width:120}
			          ]],
			        pagination:true,
					pageSize:20, 
					sortName : 'username',
					sortOrder : 'desc',
			        pageList : [ 10, 20, 30, 40, 50, 100, 200],
			        toolbar:'#toolbar_expertcombogrid_11',
			        onChange:function(newValue,oldValue){
			        	if(newValue.length>5){
			        		var arr=oldValue.slice(0,5)
			        		$('#taskedit_chooseExperts').combogrid('setValues',arr);
			        		$.messager.alert('提示','不能选择超过五个专家，系统将只取用前选择的前五个专家！','info');
			        	}
			        }
			       ">
			  </select>
				<div id="toolbar_expertcombogrid_11">
							&nbsp;<img src="../../js/easyui/themes/icons/yp_user.png" style="vertical-align: middle;"/>
							<label style="color: #279FD2">显示组: </label><input class="easyui-combobox"  data-options="
						         url:'../../sys/expertAction!getExpertsGroupComboInfo.action',
						        method:'get',
						        valueField:'id',
				      			textField:'text',
				      			panelHeight:'auto',
				      			formatter:function(row){return '&lt;span style=\'color:#279FD2;cursor:pointer;\'&gt;' + row.text + '&lt;/span&gt;';},
				      			onSelect:function(record){$('#taskedit_chooseExperts').combogrid('grid').datagrid('load',{groupID:record.id}); }
						"></input>
				</div> 
		
		</fieldset>
	</form>
	<div style="text-align: center;">
	<a href="#" id="checkToSubmitBtn" class="easyui-linkbutton" iconCls="icon-ok" style="margin-right: 20px;">&nbsp; 确&nbsp; &nbsp;定</a>
	<a href="#" onclick="yp.fillFormValues('#taskedit_form', null, 'tm.');" class="easyui-linkbutton" iconCls="icon-cancel">&nbsp; 清&nbsp; &nbsp;空</a>
	</div>
	</div>
</div>
<script>
function checkTosubmit(gridID){
	changeValid('#taskedit_form');
   	$('#taskedit_needexpert').val(document.getElementById('taskedit_needexpert').checked);
	$('#taskedit_form').form('submit', {    
	    url:yp.bp()+"/sys/taskAction!edit.action",    
	    onSubmit: function(){
	    	$.messager.progress();
	    	var isValid = $(this).form('validate');
	    	var echeck=expertCheck('#taskedit_chooseExperts');//专家选择情况检查
	    	var checkOk=checkBoxCheck();//故障部位选择情况检查
	    	if (!isValid){
				$.messager.progress('close');	
	    		$.messager.alert('提示','无效的字段值','info');
				return false;
			}
	    	if(!echeck.ok){
	    		$.messager.progress('close');	
	    		$.messager.alert('提示',echeck.msg,'info');
				return false;
	    	}
	    	if(!checkOk){
	    		$.messager.progress('close');	
	    		$.messager.alert('提示','请勾选“汽车事故的发生部位！”','info');
				return false;
	    	}
	    	return isValid&&echeck.ok&&checkOk;
	    },    
	    success:function(data){//成功后，关闭编辑框
	    	var j=yp.json2obj(data);
	    	$.messager.progress('close');
	    	$.messager.alert('提示',j.msg,'info');
	    	if(j.ok){//编辑成功后关闭窗口
	    		editSubmit=true;
	    		var tab = $('#mainTabs').tabs('getTab', "任务编辑");
	    		var tindex=$('#mainTabs').tabs('getTabIndex',tab);
	    		if(tab){$('#mainTabs').tabs('close',tindex).tabs('select',tindex-1);}
	    		var grids=$("#"+gridID);
	    		if(grids){
	    			grids.datagrid("reload");
	    		}
	    	}
	    }    
	}); 
}
function deleteFile(ele,filename,taskid){
	$.messager.confirm('确认对话框', '您确认要删除此文件吗？', function(r){
		if (r){//记录删除项并隐藏元素
			$.get(yp.bp()+"/sys/taskAction!deleteFile.action",{"id":taskid,'doc':filename},function(data){
				if(data.ok){$(ele).parent("span").parent("div").parent("li").hide("slow");
				}else{	$.messager.alert("提示",data.msg,"info");}
			},"json");
		}
	});
}
function expertCheck(selector){//检查专家选择是否合法，返回一个包含boolean类型和string类型的对象
	var result={'ok':true,'msg':'专家选择！'};
	var setneede= document.getElementById('taskedit_needexpert').checked;//勾选了分配专家选项
	 var expertids=$(selector).combogrid('getValues');
	if(setneede&&expertids.length<1){//若勾选
		result.ok=false;result.msg='若需要分配专家，那么请选择要分配的专家！';
	}	return result;
}
function checkBoxCheck(){
	 var flag=false;
	 for(var i=0;i<10;i++){
		 var ckd=document.getElementById("acd_"+i).checked;
		 flag=flag||ckd;
	 }
	return flag;
}
$(function(){
	$.messager.progress();
	//从审核页面传递过来的两个参数
	var method="${param['method'] }";
	var taskid="${param['taskid'] }";
	var gridID="${param['gridSelector']}";
	if(method=='edit'){
	$.get(yp.bp()+"/sys/taskAction!getTaskInfo.action",{"id":taskid},function(data){
		if(data.ok){
			var obj=data.object;
			var taskModel=obj[0];
			var cap=obj[1];//先前选择的事故部位
			yp.fillFormValues("#taskedit_form",cap, "cap.");
			yp.fillFormValues("#taskedit_form",taskModel, "tm.");
			$("#taskedit_taskid").val(taskid);
			var eids=taskModel.info;
			if(eids){
				var arr=eids.split(',');
				//....选中已经分配的专家。(当然从审核页面传过来的不可能有专家。所以在此略)
			}
			var files=taskModel.upfile;
			if(files){
				for(var i in files){
					var f=files[i];
					var innerLi="<li><div style='display: inline-block;cursor: pointer; min-width: 50%'>"+
					"<span  title='可查看或删除该文件！' style='width: 80%; display: inline-block;color:#5888CC;text-decoration: underline;'>"+f[1]+"</span>"+
					"<a href='#' title='查看文件' onclick='showFile(\""+f[0]+"\")'>"+
					"<img src='../../js/easyui/themes/icons/yp_detailsearch.png'/></a> "+
					"<span class='readOnly'>&nbsp;|&nbsp;"+
					"<a href='#' title='删除文件' onclick='deleteFile(this,\""+f[0]+"\",\""+taskid+"\");'>"+
					"<img src='../../js/easyui/themes/icons/yp_delete.png'/></a></span></div></li>";
					$("#taskedit_form ul").append(innerLi);
				}
			}
		}else{
			$.messager.alert("提示",data.msg,"info");
		}
		$.messager.progress('close');
		changeValid('#taskedit_form');
		$("#taskedit_form input[type='radio'][name='tm.applytype']").attr("disabled",true);
	},"json");}else{
		$("#taskedit_form input[type='radio'][name='tm.applytype']").on('change',function(){changeValid("#taskedit_form");});//修改时，禁止修改申请类型
		$("#taskedit_form input,#taskedit_form textarea").attr({"readOnly":"readOnly"});
		$("#taskedit_form input,#taskedit_form textarea").css({"background":"#F2F5F7","border":"none"});
		$(".readOnly").css({"display":"none"});
	}
	$("#taskedit_needexpert").on('change',function(){
		var neede= document.getElementById('taskedit_needexpert').checked;
		if(neede){$("#taskedit_chooseExperts").combogrid("enable");}else{$("#taskedit_chooseExperts").combogrid("disable").combogrid('clear');}
	});
	$("#checkToSubmitBtn").on('click',function(){
		checkTosubmit(gridID);
	});
	//提示信息
	var tipsDesc=new Array(
			'车驾、车梁、风窗、挂车钩、后视镜、拉手、门锁、座椅', //车身
			'AMT/EMT、DSG/DCT车桥、传动轴、多个车桥、手动变速器、自动变速器、车轮驱动、排气管',//传动系
			'辐射模块、充电系、除雾/除霜、导航、防盗控制、刮水器、集成式拖车制动控制、空调、喇叭、外部灯、线束、仪表、音响',//电气设备
			'柴油机、汽油机、其它燃料机、新能源系统、点火系、进气系统、起动系、水箱',//发动机
			'车轮与轮胎',//车轮和轮胎
			'气囊和安全带',//气囊和安全带
			'前悬架、后悬架、主动悬架控制（钢板、弹簧、控制臂、连杆）',//悬架系
			'ASR/ESP、气压行车制动、液圧行车制动、驻车制动',//制动系
			'齿轮齿条式、电动和电液转向助力、方向盘、循环球式、液压转向助力、转向传动装置、转向管柱',//转向系
			'标识、机械、使用说明书');//附加设备
	var tipParams={
	        guide: 0,//guide: 指引方向（0：上，1：右，2：下，3：左）
	        time: 2,//0表示不自动关闭，若3秒后自动关闭，time: 3即可，默认为0
	        style: ['background-color:#3BAAE3; color:#fff', '#3BAAE3'],
	        maxWidth:240
	    };
	for(var i=0;i<tipsDesc.length;i++){
		var ele=$("#acd_"+i).parent();
			ele.attr('tip',tipsDesc[i]);
			ele.on('mouseover', function(){
			layer.tips($(this).attr("tip"), this,tipParams );	
		});
	}
});

function changeValid(formSelecotr){
	var aptp=$(formSelecotr+" input[type='radio'][name='tm.applytype']:checked").val();
	if(aptp=='消费者'){
		$(formSelecotr+" input[name='tm.code_1']").validatebox('enableValidation'); 
		$(formSelecotr+" input[name='tm.code_2']").validatebox('disableValidation'); 
		$(".code_1").html("*");
		$(".code_2").html("");
		return true;
	}else if(aptp=='单位用户'){
		$(formSelecotr+" input[name='tm.code_1']").validatebox('disableValidation'); 
		$(formSelecotr+" input[name='tm.code_2']").validatebox('enableValidation');
		$(".code_1").html("");
		$(".code_2").html("*");
		return true;
	}else{//未选择时
		$.messager.alert('提示','请选择申请类型！','erro');
		return false;
	}
}
</script>