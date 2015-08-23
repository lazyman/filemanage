<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="cn.com.lazyhome.filemanage.FileManager" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
// 计算指定目录下的md5
final String basedir = "/volume1/homes/dch/g";
//basedir = "D:/wiz/temp";
//basedir = "D:/wiz";
//basedir = request.getParameter("path");


new Thread(new Runnable() {
	public void run() {
		FileManager manager = new FileManager();

		manager.setRecordType(FileManager.RECORD_TYPE_DB);
		manager.ananlyze(basedir);
	}
}).start();


%>
</body>
</html>