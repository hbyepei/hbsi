<%@ page language="java" pageEncoding="UTF-8"%>
<script type="text/javascript" src="../../js/easyui/extension/datagrid-groupview.js"></script>
<style>
.emphasize{color:#f00;background:yellow;font-weight:bold;}
.need{font-weight: bolder;color: red;}
#addOrUpdateExpert form table{width: 100%;}
#addOrUpdateExpert form table tr{line-height: 25px;}
#addOrUpdateExpert form table tr *{font-size: 12px;}
#addOrUpdateExpert form table td:nth-child(odd){text-align: right;}
#addOrUpdateExpert form table tr td:nth-child(2){width: 200px;}
#addOrUpdateExpert form table tr td:nth-child(3){width: 60px;}
#addOrUpdateExpert form table tr td:nth-child(2) input{width: 90%;}

</style>
 <table id="expertList_21"></table>
 <div id="toolbar_21" style="border: none;">
 		<table style="display: inline-block;">
 			<tr>
 				<td>
					<a href="javascript:void(0);" class="easyui-linkbutton" onclick="addOrUpdateExpert('add');" data-options="iconCls:'icon-add',plain:true">新增</a>
 				</td>
 				<td>
					<a href="javascript:void(0);" class="easyui-linkbutton" onclick="addOrUpdateExpert('update');"  data-options="iconCls:'icon-edit',plain:true">编辑</a>
 				</td>
 				<td>
					<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-yp_delete',plain:true" onclick="deleteExpert();">删除</a>
 				</td>
 				<td>
					<div class="datagrid-btn-separator"></div>
 				</td>
 				<td>
		    		&nbsp;<img src="../../js/easyui/themes/icons/yp_user.png" style="vertical-align: middle;">
					<label style="color: #279FD2;font-size: 12px;">显示组: </label><input class="easyui-combobox"  data-options="
					                url:'../../sys/expertAction!getExpertsGroupComboInfo.action',
		                  			method:'get',
		                    		valueField:'id',
		                  			textField:'text',
		                  			panelHeight:'auto',
		                  			formatter:function(row){return '&lt;span style=\'color:#279FD2;cursor:pointer;\'&gt;' + row.text + '&lt;/span&gt;';},
		                  			onSelect:function(record){
		                  				$('#expertList_21').datagrid('load',{loadtype:'group',groupID:record.id});
		                  			}
		          ">&nbsp;&nbsp;
 				</td>
 			</tr>
 		</table>
		<div  style="display: inline-block;float: right;">
			<a href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-yp_excelimport',plain:true" onclick="$('#excelupload').click();">批量导入</a>
			<a href="javascript:void(0);" class="easyui-linkbutton" onclick="relatedWork();" data-options="iconCls:'icon-yp_docsearch',plain:true">查看关联任务</a>
			<input id="searchbox_21"  class="easyui-searchbox" data-options="prompt:'搜索专家',menu:'#searchbox_21_menu',
			searcher:searchExpert">
		</div>
</div>
	    <div id="searchbox_21_menu">
	    	  <!-- <div data-options="name:'id',iconCls:'icon-yp_key'">ID</div> -->
		       <div data-options="name:'all',iconCls:'icon-ok'">所有类别</div>
		       <div data-options="name:'username',iconCls:'icon-yp_expert',selected:true">姓名</div>
		       <div data-options="name:'name',iconCls:'icon-yp_key'">登录名</div>
		       <div data-options="name:'letterid',iconCls:'icon-yp_docbook'">聘书编号</div>
		       <div data-options="name:'phone',iconCls:'icon-yp_phone'">电话号码</div>
		       <div data-options="name:'email',iconCls:'icon-yp_email'">电子邮件</div>
		       <div data-options="name:'area',iconCls:'icon-yp_map'">所属地区</div>
		</div>
	<form id="excelImport" enctype="multipart/form-data" method="post"  style="display: none;" >
	    	<input type="file" onchange="importExperts();" id="excelupload" name="upfile"accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"/>
	  </form>
	 
<!-- ===============新增专家窗体================= -->
 <div id="addOrUpdateExpert" style="background-color: #EFF4FB;padding: 10px 10px;" class="easyui-dialog" data-options="
					title: '添加新的专家用户',    
				    width: 550,    
				    height: 520,    
				    cache: true,
				    modal: true,
	    		    iconCls: 'icon-yp_expert',
				    closed:true,
					buttons:[
					{
					text:'确定',
					iconCls:'icon-ok',
					handler:function(){var sub=new addOrUpdateExpert();sub.doSubmit();}
				},{
					text:'关闭',
					iconCls:'icon-cancel',
					handler:function(){$('#addOrUpdateExpert').dialog('close');}
				}]
">
<form enctype="multipart/form-data" method="post">
	<input type="hidden" name="id">
	<table>
		<tr><td>姓名:</td><td><input type="text" name="username" placeholder="专家姓名" class="easyui-validatebox" data-options="required:true,validType:'zh[2,4]'"><span class="need">*</span></td><td rowspan="5" colspan="2" style="text-align: center;">
			<div id="imagePreView_warpper_add" style="width: 135px;height: 150px;overflow: hidden;background-color: #efefef;margin: 0 auto;">
				<div id="preview_fake_add" style="width: 135px;height: 150px;overflow: hidden;">
					<img id="preview_img_add" alt="点击上传图像" width="135px" height="150px" style="cursor: pointer;" onclick="document.getElementById('expert_image_add').click();"/>
					<input type="file" id="expert_image_add" name="upfile"onchange="preImage(this,'_add');" style="width:150px;display: none;">
				</div>
			</div>
		</td></tr>
		<tr><td>登录名:</td><td><input type="text"  name="name" placeholder="用户名" class="easyui-validatebox" data-options="required:true,validType:'password[5,15]'"><span class="need">*</span></td></tr>
		<tr><td>年龄:</td><td><input type="text" name="age"  style="width: 50px;"class="easyui-numberbox" data-options="min:18,precision:0,max:100"></td></tr>
		<tr><td>性别:</td><td>
				<input type="radio"  name="gender"  value="男" id="addExpert_male" style="width: 40px;"><label for="addExpert_male">男</label>
				<input type="radio"  name="gender"  value="女" id="addExpert_female" style="width: 40px;"><label for="addExpert_female">女</label>
		</td></tr>
		<tr><td>聘书编号:</td><td><input type="text" name="letterid" placeholder="聘书编号" class="easyui-validatebox" data-options="required:true,validType:'password[8,15]'"><span class="need">*</span></td></tr>
		<tr><td>技术组别:</td><td><input id="techGroup" type="text" class="easyui-combobox" name="technology" data-options="valueField:'id',textField:'text',url:'../../sys/expertAction!getExpertsGroupComboInfo.action?onlyUsefull=true',required:true,panelHeight:'auto'"><span class="need">*</span></td><td colspan="2">
			<div style="background-color: #EEE2D1;text-align: center;width: 70%;margin: 0 auto;" >
				<span style="font-size: 12px;color:#444"><label>点击图片区域上传图像！</label></span>
			</div>
		</td></tr>
		<tr><td>身份证号:</td><td><input type="text" name="idcard" placeholder="身份证号码" class="easyui-validatebox" data-options="required:true,validType:'idcard'"><span class="need">*</span></td><td>手机号码:</td><td><input type="text" name="phone" placeholder="手机号" class="easyui-validatebox" data-options="required:true,validType:'phone'"><span class="need">*</span></td></tr>
		<tr><td>电子邮件:</td><td><input type="text" name="email" placeholder="电子邮件" class="easyui-validatebox" data-options="validType:'email'"></td><td>所属地区:</td><td><input type="text"  name="area" placeholder="例:武汉市洪山区" class="easyui-validatebox" data-options="required:true"><span class="need">*</span></td></tr>
		<tr><td>来源单位:</td><td colspan="3"><input type="text" name="department_name" placeholder="xxx公司"  style="width: 95%"></td></tr>
		<tr><td>单位类别:</td><td colspan="3"><input type="text" name="department_category" placeholder="例:汽车制造企业"    style="width: 95%"></td></tr>
		<tr><td>现服品牌:</td><td colspan="3"><input type="text"  style="width: 95%" name="brand" placeholder="例:东风风神" class="easyui-validatebox" data-options="required:true"><span class="need">*</span></td></tr>
		<tr><td style="vertical-align: top;">简介:</td><td colspan="3"><textarea name="introduction" placeholder="个人简介"   style="resize:none;width: 95%"></textarea></td></tr>
	</table>
	<div style="background-color: #EEE2D1;text-align: center;width: 90%;margin: 0 auto;" >
		<img src="../../js/easyui/themes/icons/tip.png">
		<span style="font-size: 12px;color:#444"><label>提示：新加用户的登录密码是其身份证号码的后6位，登录后可自行修改！</label></span>
	</div>
</form>
</div>
<div id="relatedWorks" class="easyui-dialog" data-options="
	    title: '关联任务',    
	    width: 500,    
	    closed: true,    
	    cache: false,  
	    iconCls:'icon-yp_file',
	    modal: true,
	    maximizable:true,
	    onClose:function(){
	    	$('#relatedWorks ul').html('');
	    },
	    buttons:[{text:'关闭',handler:function(){$('#relatedWorks').dialog('close');}}]"><ul>
	    </ul></div>  
 <script>
 function relatedWork(){
	 	var expertList= $('#expertList_21');
 		var row = expertList.datagrid('getSelected');
 		if (!row) {
 			$.messager.alert('提示', '请选择行！', 'info');
 		} else {
 			$.messager.progress();
 			$.get(yp.bp()+'/sys/expertAction!getRelatedWorks.action',{id:row.id},function(data){
 				$.messager.progress('close');
 				if(data.ok){
 					var obj=data.object;
 					for ( var i in obj) {//[{id,applytime,name_1,name_2,matter,status},{}...]
 						var o=obj[i];
 						$("#relatedWorks ul").append("<li><a href='javascript:void(0);' onclick='detailWork(\""+o.id+"\");'>["+o.applytime+"] "+o.matter+"("+o.name_1+"-"+o.name_2+") "+o.status+"</a></li>");
 					}
 					$('#relatedWorks').dialog('open');
 				}else{
 					$.messager.alert('提示', data.msg, 'info');
 				}
 			},'json');
 		}
 }
 function detailWork(id){
		window.open(yp.bp() + "/user/applyAction!onlinePrelook.action?looker=admin&docname=applydoc_server&taskid=" +id + "&timestamp=" + Math.random());
 }
 
 /**
 *新增或修改专家
 */
 var emethod=undefined;
 function addOrUpdateExpert(method){
	 var dlg=$('#addOrUpdateExpert');
	 if(method){
		 emethod=method;
	 }
	 if(method&&method=='add'){//修改标题，将图片置空
		 dlg.dialog('setTitle','添加新的专家用户');
		 $('#preview_img_add').attr('src','');
		 yp.fillFormValues('#addOrUpdateExpert form', null, '');
		 $("#addOrUpdateExpert form table tr td textarea[name='introduction']").val('');
		 dlg.dialog('open');
	 }else if(method&&method=='update'){//修改标题，加载图片，填充数据
	 	 var rs=$('#expertList_21').datagrid('getChecked');
	 	 if (rs.length<1) {$.messager.alert('提示','请选择一位要修改的专家！','info');return;
	 	 }else if(rs.length>1){$.messager.alert('提示','选择的条目过多！','info');return;}else{
	 		 var row=rs[0];
			 dlg.dialog('setTitle','修改专家信息');
			 $('#preview_img_add').attr('src', yp.bp() + row.image);
			for(var o in row){if(typeof(row[o])=="string"){row[o]=yp.removeHTMLTag(row[o]);}	}
			yp.fillFormValues('#addOrUpdateExpert form', row, '');//注，此方法无法修改textarea输入框，所以专家简介需要手动修改
			//此外，由于使用搜索后，被搜索字段被加上了额外的html标签，所以应该去掉这些html标签后再进行填充。
			//例如：<label class='emphasize'>叶</label>佩。应当将其先替换成空串
			$("#addOrUpdateExpert form table tr td textarea[name='introduction']").val(row.introduction);
			var tech=row.technology;
			var gid=0;
			if(tech){
				switch (tech) {
				case '电气':gid=1;break;
				case '发动机':gid=2;break;
				case '法律政策':gid=3;break;
				case '车身':gid=4;break;
				case '底盘':gid=5;break;
				default:gid=-1;	break;
				}
			}
			if(gid>0){
				$('#techGroup').combobox('setValue',gid);
			}else{
				$('#techGroup').combobox('clear');
			}
	 		dlg.dialog('open');
		}
	 }
	 
	 this.doSubmit=function(){
		 var url="";
		 if(emethod){
			 url=yp.bp()+'/sys/expertAction!'+emethod+'.action';
		}else{
			 $.messager.alert('提示','未知方法','info');
			 return;
		 }
		 $.messager.progress();
		 $('#addOrUpdateExpert form').form('submit',{
			    url:url,   
			    onSubmit: function(){    
			    	var isValid = $(this).form('validate');
					if (!isValid){
						$.messager.progress('close');
					}
					return isValid; 
			    },    
			    success:function(data){
			    	var obj=yp.json2obj(data);
			    	if(obj.ok){
			    		 $('#expertList_21').datagrid('reload');
			    		 $('#addOrUpdateExpert').dialog('close');
			    		 emethod=undefined;
			    	}
			    	$.messager.progress('close');
			    	$.messager.alert('提示',obj.msg,'info');
			    } 
		 });
	 };
 }
 /**
 *搜索专家
 */
 function searchExpert(value,name){
			var type='';
			switch(name){
				case 'ID':type='id';break;
				case '所有类别':type='all';break;
				case '姓名':type='username';break;
				case '登录名':type='name';break;
				case '聘书编号':type='letterid';break;
				case '电话号码':type='phone';break;
				case '电子邮件':type='email';break;
				case '所属地区':type='area';break;
				default:beak;
			};
		$('#expertList_21').datagrid('uncheckAll').datagrid('load',{loadtype:'search',searchName:type,searchValue:value});
 }
 
 /**
  * 删除专家
  */
  function deleteExpert(){
 	var expertList= $('#expertList_21');
 		var row = expertList.datagrid('getSelected');
 		if (!row) {
 			$.messager.alert('提示', '请选择要删除的行！', 'info');
 		} else {
 			$.messager.confirm('确认删除', '您确定要删除此用户吗？', function(t){
 				if (t){
 					$.messager.progress(); 
 					var url=yp.bp()+'/sys/expertAction!delete.action';
 					var params={id:row.id};
 				    $.post(url,params , function(data) {
 				    	$.messager.progress('close');
 						if (data.ok) {
 							var index=expertList.datagrid('getRowIndex',row);
 							expertList.datagrid('deleteRow',index);
 						} 
 						 $.messager.alert('提示',data.msg, 'info');
 					}, 'json');
 				}
 			});
 		}
 	}
 /**
  * 批量导入专家数据
  */
 function importExperts(){
 	var fileTag=$('#excelImport input')[0];
 	if (!fileTag.value.match(/.xls|.xlsx/i)) {
 		alert('请导入Excel格式的文件！');
 		return false;
 	}
 	$.messager.progress(); 
 	$('#excelImport').form('submit', {    
 		url:yp.bp()+'/sys/expertAction!importExperts.action',
 	    onSubmit: function(){	},    
 	    success:function(data){
 	    	$.messager.progress('close'); 
 	    	var obj=yp.json2obj(data);
 	    	$.messager.alert('提示',obj.msg, 'info');
 	    	if(obj.ok){
 	    		$('#expertList_21').datagrid('reload');
 	    	}
 	    } 
 	}); 
 }
 
 
 $(function(){
	 $('#expertList_21').datagrid({
		 url:yp.bp()+'/sys/expertAction!getExperts.action?loadtype=all',
		sortName : 'technology',
		sortOrder : 'desc',
        view:groupview,
        groupField:'technology',
        groupFormatter:function(value,rows){
            return value + '技术组-(' + rows.length + ')';
        },
        fit:true,
		border:false,
		striped : true,//显示斑马线效果
		rownumbers : true,
		pagination : true,
		pageSize : 100,
		pageList:[20,50,80,100,200,500],
		singleSelect : true,
		checkOnSelect:true,
		selectOnCheck:true,
        autoRowHeight:false,
        nowrap:true,
        collapsible:true,
        remoteSort:false,
        method:'get',
        cache:true,
        idField : 'id',
		loadMsg:'正在加载专家数据，请稍候...',
		onDblClickRow:function(rowIndex, rowData){
			addOrUpdateExpert('update');
		},
		columns : [ 
			   		[
	   		        	{field:'id',checkbox:true},
	   		       		{title : '图像',field:'image',align:'center',width:50, formatter: function(value,row,index){return "<a href='javascript:void(0);' title='"+value+"' onclick='yp.showPic(this.title);' ><img src='"+yp.bp()+value+"' alt='"+row.name+"'  style='width:30px;height:30px;'></a>";}},
	   			        {title : '姓名',field : 'username',sortable : true,align:'center',width:80}, 
	   			        {title : '聘书编号',field : 'letterid',sortable:true,width:120,align:'center'}, 
	   			        {title : '登录名',field:'name',align:'center',sortable:true,width:100},
			   		 	{title : '性别',field:'gender',align:'center',sortable:true,width:40},
			   			{title : '年龄',field:'age',width:40,sortable:true,align:'center'},
			   			{title : '身份证号',field:'idcard',width:150,align:'center'},
			   			{title : '联系电话',field:'phone',width:120,align:'center',sortable:true},
			   			{title : '电子邮件',field:'email',width:150,align:'center'},
			   			{title : '所属地区',field:'area',width:100,sortable:true},
			   			{title : '来源单位',field:'department_name',width:220},
			   			{title : '单位类别',field:'department_category',width:150,sortable:true},
			   			{title : '现服品牌',field:'brand',sortable:true,width:120},
			   			{title : '技术组别',field:'technology',sortable:true,width:100},
			   			{title : '简介',field:'introduction',width:300}
			   			]
			   	],
			toolbar: '#toolbar_21'
	 });
 });
 </script>