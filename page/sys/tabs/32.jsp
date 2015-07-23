<%@ page language="java" pageEncoding="UTF-8"%>
<fieldset style="margin: 20px 20px; border-radius:5px;padding:10px 10px;width: auto">
	<legend style="text-decoration:none;color:#3779C9;font-size:18px;">导出选项</legend>
	<div style="padding:10px 10px 10px 5%;">
			<form>
				<label>日期范围：</label>
				从&nbsp;<input id="date_from" type="text" class="easyui-datebox" name="date_from"/>  
				到&nbsp;<input id="date_to" type="text" class="easyui-datebox" name="date_to"/><br><br>
				<label>导出内容：</label>
				<input type="checkbox" id="product_data" checked="checked" name="product_data"><label for="product_data">缺陷产品信息</label>&nbsp;&nbsp;
				<input type="checkbox" id="task_data"  checked="checked" name="task_data"><label for="task_data">调解任务信息</label>&nbsp;&nbsp;
				<input type="checkbox" id="user_data"  checked="checked" name="user_data"><label for="user_data">用户分类信息信息</label>&nbsp;&nbsp;
				<input type="checkbox" id="expert_data"  checked="checked" name="expert_data"><label for="expert_data">专家参与信息</label>
				<div style="padding-top:30px;padding-left: 100px;">
					<a class="easyui-linkbutton" style="width:60px;">导出</a>&nbsp;&nbsp;
					<a class="easyui-linkbutton" style="width:60px;">取消</a>
				</div>
				
			</form>
	</div>
	
</fieldset>
<script>
$(function(){
	var can1=$("#date_from").datebox({editable:false});
	var can2=$("#date_to").datebox({editable:false});
	var now=new Date();
	var ago=new Date(now-(15*24*3600*1000));
	can1.datebox('setValue',getSmpFormatDate(ago,false));
	can2.datebox('setValue',getSmpFormatDate(now,false));
});
</script>