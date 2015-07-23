<%@ page language="java" pageEncoding="UTF-8"%>
<table id="a2_table" ></table> 
<script>
$('#a2_table').datagrid({ 
    url:yp.bp()+"/sys/docAnalysisAction!getTableModel.action",
    striped : true,//显示斑马线效果
    fitColumns:true,
    fit:true,
    pagination:true,
    pagePosition:'top',
    pageSize:10,
    pageList:[5,10,15,20],
    sortName : 'dealtime',
	sortOrder : 'desc',
	//idFiled:'formid',
    columns:[[
        {field:'brand',title:'汽车品牌',width:100,sortable:true},    
        {field:'model',title:'汽车型号',width:100,align:'center',sortable:true},    
        {field:'carriage',title:'车架号',width:100,align:'center',sortable:true},
        {field:'dealtime',title:'购车日期',width:300,sortable:true},    
        {field:'description',title:'问题描述',width:300}    
    ]]    
});  
</script>