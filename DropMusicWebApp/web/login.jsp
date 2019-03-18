<%--
  Created by IntelliJ IDEA.
  User: pedrochicoria
  Date: 13/11/2018
  Time: 09:38
  To change this template use File | Settings | File Templates.
--%>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
    <title>Login </title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="style.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>

<body>
<s:url action="login" var="loginUrl"/>
<s:url action="signup" var="signupUrl"/>
<nav class="navbar navbar-inverse" style="background: dimgrey">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="index.jsp"
               style="color: white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif;font-size: large">DropMusic</a>
        </div>
        <ul class="nav navbar-nav navbar-right">
            <li><a <s:a href="%{signupUrl}"
                        style="color: white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif"><span
                    class="glyphicon glyphicon-user"></span> Sign Up</s:a></li>
            <li><a <s:a href="%{loginUrl}"
                        style="color: white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif"> <span
                    class="glyphicon glyphicon-log-in"></span> Login</s:a></li>
        </ul>
    </div>
</nav>

<div class="container">


    <div id="left" style="float:left; width:100%; color: dimgrey" align="center">
        <h2 style="color:dimgrey">Login</h2><br>
        <s:form action="signin" method="post" theme="simple">

            <h4 style="color: dimgrey">Username:</h4> <s:textfield name="username" label="Name"/>
            <br><br>
            <h4 style="color: dimgrey">Password:</h4> <s:password name="password" label="Password"/>
            <br><br>
            <s:submit type="button" label="Login" cssClass="registerbtn"/>

        </s:form>

        <s:form action="dropboxlogin" method="post" theme="simple">
            <s:submit cssStyle="width: 50%" type="button" label="Login with DropBox"
                      cssClass="registerbtn">Login with DropBox</s:submit>
        </s:form>
    </div>
</div>
</body>
</html>