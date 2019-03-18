<%--
  Created by IntelliJ IDEA.
  User: pedrochicoria
  Date: 23/11/2018
  Time: 21:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%@taglib uri="/struts-tags" prefix="s" %>
<html>
<head>
    <title>Create Artist Result</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="../style.css">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

</head>
<body>
<s:url action="logout" var="logoutUrl" />
<s:url action="homepage" var="homepageUrl" />

<nav class="navbar navbar-inverse" style="background: dimgrey">
    <div class="container-fluid">
        <ul class="nav navbar-nav">
            <li class="active"><a <s:a href="%{homepageUrl}" style="background:dimgrey;color: white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif;font-size: large"> DropMusic </s:a> </a></li>
        </ul>
        <ul class="nav navbar-nav navbar-right">
            <li><a <s:a href="%{logoutUrl}"
                        style="color: white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif"> <span
                    class="glyphicon glyphicon-log-out"></span> Logout</s:a></li>
        </ul>
    </div>
</nav>

<div class="container">


    <div id="left" style="float:left; width:100%; color: dimgrey" align="center">
        <h2 style="color:dimgrey">Create Artist</h2><br>
<c:choose>
    <c:when test="${artistBean.createArtist == true}">
        <h2 style="color: dimgrey;text-align: center">Artist is created </h2>
    </c:when>
</c:choose>
    </div>
</div>
</body>
</html>
