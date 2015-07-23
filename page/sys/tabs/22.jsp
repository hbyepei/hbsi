<%@ page language="java" pageEncoding="UTF-8"%>
<style>
#editAdmin table tr td:nth-child(odd){text-align: right;}
#editAdmin table tr{line-height: 30px;}
#editAdmin form .need{color:red;}
</style>
    <table id="adminList" class="easyui-datagrid" title="系统用户列表" 
            data-options="
            sortName:'name',
            sortOrder:'desc',
            remoteSort:false,
            rownumbers:true,
            singleSelect:true,
            url:'../../sys/adminAction!getAdmins.action',
            method:'get',
            idField:'id',
            onDblClickRow:function(rowIndex, rowData){
            	openEditAdminDialog(rowIndex);
			},
            toolbar:
	            [{
		            text:'创建新用户',
		            iconCls:'icon-add',
		            handler:function(){
			            yp.fillFormValues('#editAdmin form');
			            $('#createtime_22').val('今天');
			            $('#preview_img_edit').attr('src', '');
			            method='add';
			            $('.tobeHidden').show();
						$('#password_confirm,#password').validatebox('enableValidation');
			            $('#editAdmin').dialog('setTitle','添加新的管理员');
			            $('#editAdmin').dialog('open');
		            }
		        }]
            ">
        <thead>
            <tr>
                <th data-options="field:'image',align:'center',width:80,
					formatter: function(value,row,index){
						return '&lt;a href=\'javascript:void(0);\' title=\''+value+'\' onclick=\'yp.showPic(this.title);\' &gt;&lt;img src=\''+yp.bp()+value+'\' alt=\''+row.name+'\'  style=\'width:30px;height:30px;\'&gt;&lt;a/&gt;';
					}
					">头像</th>
                <th data-options="field:'username',width:80,align:'center',sortable:true">姓名</th>
                <th data-options="field:'name',align:'center',width:80,sortable:true">登录名</th>
                <th data-options="field:'age',width:80,align:'center',sortable:true">年龄</th>
                <th data-options="field:'gender',align:'center',width:80,sortable:true">性别</th>
                <th data-options="field:'createtime',width:150,align:'center',sortable:true">创建时间</th>
                <th data-options="field:'id',width:60,align:'center',
                	formatter: function(value,row,index){
						return '&lt;a href=\'javascript:void(0);\' title=\''+index+'\' onclick=\'openEditAdminDialog(this.title);\'&gt;&lt;img src=\'../../js/easyui/themes/icons/pencil.png\'&gt;&lt;/a&gt;&nbsp;&nbsp;&nbsp;&lt;a href=\'javascript:void(0);\' title=\''+index+'\' onclick=\'deleteAdmin(this.title);\'&gt;&lt;img src=\'../../js/easyui/themes/icons/yp_delete.png\'&gt;&lt;/a&gt;';
					}
                ">操作</th>
            </tr>
        </thead>
    </table>
 <div id="editAdmin" style="background-color: #EFF4FB;padding: 10px 10px;" class="easyui-dialog" data-options="
					title: '编辑',    
				    width: 430,    
				    height: 380,    
				    cache: true,
				    modal: true,
	    		    iconCls: 'icon-edit',
				    closed:true,
					buttons:[
					{
					text:'确定',
					iconCls:'icon-ok',
					handler:function(){editAdmin();}
				},{
					text:'关闭',
					iconCls:'icon-cancel',
					handler:function(){$('#editAdmin').dialog('close');}
				}]
">
<form enctype="multipart/form-data" method="post">
	<table style="font-size: 12px;width: 100%">
		<tr><td><span class="need">*</span>用户名：</td><td>
		<input type="text" name="name" placeholder="填写用户名" class="easyui-validatebox"
			data-options="
				required:true,
				validType:'password[4,15]',
				missingMessage:'请填写用户名！'
				"
		></td><td rowspan="4">
				<div id="imagePreView_warpper_edit" style="width: 135px;height: 150px;overflow: hidden;background-color: #efefef;">
					<div id="preview_fake_edit" style="width: 135px;height: 150px;overflow: hidden;">
						<img id="preview_img_edit" alt="点击上传图像" width="135px" height="150px" style="cursor: pointer;" onclick="document.getElementById('ad.image').click();"/>
						<input type="file" id="ad.image" name="upfile"onchange="preImage(this,'_edit');" style="width:150px;display: none;">
					</div>
				</div>	
		</td></tr>
		<tr><td><span class="need">*</span>姓名：</td><td>
			<input id="username_22" type="text" name="username" placeholder="填写姓名"  class="easyui-validatebox"
				data-options="
					required:true,
					validType:'zh[2,4]',
					missingMessage:'请填写姓名！'
					"
			></td></tr>
		<tr class="tobeHidden"><td><span class="need">*</span>密码：</td><td>
			<input id="password" type="password" name="password" placeholder="填写密码"  class="easyui-validatebox"
				data-options="
					required:true,
					validType:'password[4,15]',
					missingMessage:'请填写密码！'
					"
			></td></tr>
		<tr class="tobeHidden"><td><span class="need">*</span>确认密码：</td><td>
			<input id="password_confirm" type="password" placeholder="确认密码"  class="easyui-validatebox"
				data-options="
					required:true,
					validType:'eqPwd[\'#password\']',
					missingMessage:'确认密码！'
					"
			></td></tr>
		<tr><td>年龄：</td><td><input id="age_22" type="text" name="age" style="width: 50px;"  placeholder="年龄" class="easyui-numberbox" data-options="min:15,max:100,precision:0"></td></tr>
		<tr><td>性别：</td><td>
				<input id="gender_male" type="radio" name="gender" value="男" style="width: 30px;"><label for="gender_male">男</label>
				<input id="gender_female" type="radio" name="gender" value="女" style="width: 30px;"><label for="gender_female">女</label>
			</td></tr>
		<tr><td >创建时间：</td><td><input id="createtime_22" class="laydate-icon" type="text" name="createtime" readonly="readonly" disabled="disabled"
			onclick="laydate({format: 'YYYY-MM-DD',max: laydate.now(0)})"
		></td><td>
			<div style="background-color: #EEE2D1;text-align: center;" >
				<img src="../../js/easyui/themes/icons/tip.png">
				<span style="font-size: 12px;color:#444"><label>点击上传！</label></span>
			</div>
		</td></tr>
	</table><br>
	<input type="hidden" name="id">
</form>
</div>
<script>
var method=undefined;
$(function(){
	laydate.skin('yalan');//设置日期选择框的皮肤
});
function openEditAdminDialog(index){
	method='edit';
	var row = $('#adminList').datagrid('selectRow',index).datagrid('getSelected');
	if (!row || row.length < 1) {
		$.messager.alert('提示', '请选择要编辑的行！', 'info');
	} else {
		yp.fillFormValues("#editAdmin form", row);
		var path = yp.bp() + row.image;
		$("#preview_img_edit").attr("src", path);
		$('.tobeHidden').hide();
		$('#password_confirm,#password').validatebox('disableValidation');
		 $('#editAdmin').dialog('setTitle','编辑');
		$('#editAdmin').dialog('open');
	}
}
function editAdmin(){
	var url;
	$.messager.progress(); 
	if(method&&method=='edit'){
		url=yp.bp()+'/sys/adminAction!edit.action';
	}else if(method&&method=='add'){
		url=yp.bp()+'/sys/adminAction!add.action';
	}else{
		$.messager.progress('close');
		alert('未知方法！');
		return;
	}
	$('#editAdmin form').form('submit', {    
		url:url,
	    onSubmit: function(){  
	    	var isValid = $(this).form('validate');
	    	if (!isValid){
	    		$.messager.progress('close');
				return false;
			}
	    	return isValid;
	    },    
	    success:function(data){
	    	$.messager.progress('close');
	    	 var obj=yp.json2obj(data);
	    	 $.messager.alert('提示',obj.msg, 'info');
             if(obj.ok){$("#adminList").datagrid('reload');$('#editAdmin').dialog('close');
             method=undefined;
	    }}  
	});  
}

function deleteAdmin(index){
	var r = $('#adminList').datagrid('selectRow',index).datagrid('getSelected');
	if (!r || r.length < 1) {
		$.messager.alert('提示', '请选择要删除的行！', 'info');
	} else {
		$.messager.confirm('确认删除', '您确定要删除此用户吗？', function(t){
			if (t){
				$.messager.progress(); 
				var url=yp.bp()+'/sys/adminAction!delete.action';
				var params={id:r.id};
			    $.post(url,params , function(data) {
			    	$.messager.progress('close');
					if (data.ok) {
						$("#adminList").datagrid('deleteRow',index);
					} 
					 $.messager.alert('提示',data.msg, 'info');
				}, 'json');
			}
		});
	}
}
</script>