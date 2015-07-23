<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags"  prefix="s"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
		<img  alt="出错了" src="${pageContext.request.contextPath }/images/icons/error.png">
		<span style="vertical-align: center; white-space: pre-wrap;font-size: 13px;padding-left: 10px;">对不起，访问出错了！</span><br>
		<s:property value="exceptionStack"/>    
         <s:property value="exception.message"/>
</body>
</html>