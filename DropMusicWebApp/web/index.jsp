<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
	<title>Welcome to DropMusic</title>
	<meta charset="utf-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>


<style>


	.center {
		display: block;
		margin-left: auto;
		margin-right: auto;
		width: 50%;
	}

</style>
<body>

<s:url action="login" var="loginUrl" />
<s:url action="signup" var="signupUrl" />
<nav class="navbar navbar-inverse" style="background: dimgrey">
	<div class="container-fluid">
		<div class="navbar-header">
			<a class="navbar-brand" href="index.jsp" style="color: white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif;font-size: large">DropMusic</a>
		</div>
		<ul class="nav navbar-nav navbar-right">
			<li><a <s:a href="%{signupUrl}" style="color: white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif"><span class="glyphicon glyphicon-user"></span> Sign Up</s:a></a></li>
			<li><a <s:a href="%{loginUrl}" style="color: white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif"> <span class="glyphicon glyphicon-log-in"></span> Login</s:a></a></li>
		</ul>
	</div>
</nav>
<img src="dropmusic.png" class="center" >



</body>
</html>