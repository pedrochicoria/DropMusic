<%--
  Created by IntelliJ IDEA.
  User: pedrochicoria
  Date: 14/11/2018
  Time: 16:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%@taglib  uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <title>Home</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script type="text/javascript">

        var websocket = null;

        window.onload = function() { // URI = ws://10.16.0.165:8080/WebSocket/ws
            connect('ws://' + window.location.host + '/ws');
        }

        function connect(host) { // connect to the host websocket
            if ('WebSocket' in window)
                websocket = new WebSocket(host);
            else if ('MozWebSocket' in window)
                websocket = new MozWebSocket(host);
            else {
                writeToHistory('Get a real browser which supports WebSocket.');
                return;
            }

            websocket.onopen    = onOpen; // set the event listeners below
            websocket.onclose   = onClose;
            websocket.onmessage = onMessage;
            websocket.onerror   = onError;
        }

        function onOpen(event) {
            console.log('Connected to ' + window.location.host + '.');
        }

        function onClose(event) {
            console.log('WebSocket closed.');
        }

        function onMessage(message) { // print the received message
                alert(message.data);
        }

        function onError(event) {
            console.log('WebSocket error (' + event.data + ').');
        }

    </script>
    <link rel="stylesheet" type="text/css" href="style.css">

    <style>

        .center {
            display: block;
            margin-left: auto;
            margin-right: auto;
            width: 50%;
        }

    </style>

</head>
<body>
<s:url action="logout" var="logoutUrl" />
<s:url action="homepage" var="homepageUrl" />
<s:url action="insertmusic" var="insertmusicUrl" />
<s:url action="editmusic" var="editmusicUrl" />
<s:url action="insertalbum" var="insertalbumUrl" />
<s:url action="editalbum" var="editalbumUrl" />
<s:url action="insertartist" var="insertartistUrl" />
<s:url action="editartist" var="editartistUrl" />
<s:url action="deleteartist" var="deleteartistUrl" />
<s:url action="deletealbum" var="deletealbumUrl" />
<s:url action="deletemusic" var="deletemusicUrl" />
<s:url action="listartist" var="listartistUrl" />
<s:url action="listalbums" var="listalbumsUrl" />
<s:url action="listmusics" var="listmusicsUrl" />
<s:url action="insertreview" var="insertreviewUrl" />
<s:url action="giveprivileges" var="grantprivilegeUrl"/>
<s:url action="dropboxassociation" var="associateaccountUrl"/>



<c:choose>
    <c:when test="${session.PRIVILEGE == true}">
        <nav class="navbar navbar-inverse" style="background: dimgrey">
            <div class="container-fluid">
                <ul class="nav navbar-nav">
                    <li class="active"><a <s:a href="%{homepageUrl}" style="background:dimgrey;color: white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif;font-size: large"> DropMusic </s:a> </a></li>
                </ul>
                <ul class="nav navbar-nav">
                <li class="dropdown">
                    <a class="dropdown-toggle"  data-toggle="dropdown" style="background:dimgrey;color:white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif">Artists
                        <span class="caret"></span></a>
                    <ul class="dropdown-menu" style="background: dimgrey;">
                        <li><a <s:a href="%{insertartistUrl}" style="background:dimgrey;color: white">Insert Artist </s:a> </a></li>
                        <li><a <s:a href="%{editartistUrl}" style="background:dimgrey;color: white">Edit Artist </s:a> </a></li>
                        <li><a <s:a href="%{deleteartistUrl}" style="background:dimgrey;color: white">Remove Artist </s:a> </a></li>
                        <li><a <s:a href="%{listartistUrl}" style="background:dimgrey;color: white">List Artists </s:a> </a></li>

                    </ul>

                </li>
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#"style="background:dimgrey;color:white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif">Albums
                        <span class="caret"></span></a>
                    <ul class="dropdown-menu" style="background: dimgrey">
                        <li><a  <s:a href="%{insertalbumUrl}" style="background:dimgrey;color: white">Insert Album </s:a> </a></li>
                        <li><a  <s:a href="%{editalbumUrl}" style="background:dimgrey;color: white">Edit Album </s:a> </a></li>
                        <li><a <s:a href="%{deletealbumUrl}" style="background:dimgrey;color: white">Remove Album </s:a> </a></li>
                        <li><a <s:a href="%{listalbumsUrl}" style="background:dimgrey;color: white">List Albums </s:a> </a></li>

                    </ul>

                </li>
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#"style="background:dimgrey;color:white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif">Musics
                        <span class="caret"></span></a>
                    <ul class="dropdown-menu" style="background: dimgrey">
                        <li><a  <s:a href="%{insertmusicUrl}" style="background:dimgrey;color: white">Insert Music </s:a> </a></li>
                        <li><a <s:a href="%{editmusicUrl}" style="background:dimgrey;color: white">Edit Music </s:a> </a></li>
                        <li><a <s:a href="%{deletemusicUrl}" style="background:dimgrey;color: white">Remove Music </s:a> </a></li>
                        <li><a <s:a href="%{listmusicsUrl}" style="background:dimgrey;color: white">List Musics </s:a> </a></li>

                    </ul>

                </li>
                <li class="active"><a <s:a href="%{insertreviewUrl}" style="background:dimgrey;color: white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif"> Reviews </s:a> </a></li>
                <li class="active"><a <s:a href="%{grantprivilegeUrl}" style="background:dimgrey;color: white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif"> Grant Privileges </s:a> </a></li>
                <li class="active"><a <s:a href="%{associateaccountUrl}" style="background:dimgrey;color: white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif"> Associar a Dropbox </s:a> </a></li>

                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a <s:a href="%{logoutUrl}" style="color: white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif">  <span class="glyphicon glyphicon-log-out"></span> Logout</s:a></a></li>
                </ul>
            </div>
        </nav>
        <div class = "container">
            <c:choose>
                <c:when test="${session.LOGGED_IN == true}">
                    <h2 style="color: dimgrey;text-align: center">Welcome to DropMusic, ${session.USERNAME}.</h2>
                </c:when>
            </c:choose>


        </div>
        <img src="dropmusic.png" class="center" >


    </c:when>
    <c:otherwise>
        <nav class="navbar navbar-inverse" style="background: dimgrey">
            <div class="container-fluid">
                <ul class="nav navbar-nav">
                    <li class="active"><a <s:a href="%{homepageUrl}" style="background:dimgrey;color: white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif;font-size: large"> DropMusic </s:a> </a></li>
                </ul>
                <ul class="nav navbar-nav">
                    <li class="dropdown">
                        <a class="dropdown-toggle"  data-toggle="dropdown" style="background:dimgrey;color:white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif">Artists
                            <span class="caret"></span></a>
                        <ul class="dropdown-menu" style="background: dimgrey;">
                            <li><a <s:a href="%{listartistUrl}" style="background:dimgrey;color: white">List Artists </s:a> </a></li>

                        </ul>

                    </li>
                    <li class="dropdown">
                        <a class="dropdown-toggle" data-toggle="dropdown" href="#"style="background:dimgrey;color:white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif">Albums
                            <span class="caret"></span></a>
                        <ul class="dropdown-menu" style="background: dimgrey">
                            <li><a <s:a href="%{listalbumsUrl}" style="background:dimgrey;color: white">List Albums </s:a> </a></li>

                        </ul>

                    </li>
                    <li class="dropdown">
                        <a class="dropdown-toggle" data-toggle="dropdown" href="#"style="background:dimgrey;color:white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif">Musics
                            <span class="caret"></span></a>
                        <ul class="dropdown-menu" style="background: dimgrey">
                            <li><a <s:a href="%{listmusicsUrl}" style="background:dimgrey;color: white">List Musics </s:a> </a></li>
                        </ul>
                    </li>
                    <li class="active"><a <s:a href="%{insertreviewUrl}" style="background:dimgrey;color: white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif"> Reviews </s:a> </a></li>
                    <li class="active"><a <s:a href="%{associateaccountUrl}" style="background:dimgrey;color: white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif"> Associar a Dropbox </s:a> </a></li>

                </ul>
                <ul class="nav navbar-nav navbar-right">
                    <li><a <s:a href="%{logoutUrl}" style="color: white;font-family: 'DejaVu Sans', Arial, Helvetica, sans-serif">  <span class="glyphicon glyphicon-log-out"></span> Logout</s:a></a></li>
                </ul>
            </div>
        </nav>
        <div class = "container">
            <c:choose>
                <c:when test="${session.LOGGED_IN == true}">
                    <h2 style="color: dimgrey;text-align: center">Welcome to DropMusic, ${session.USERNAME}.</h2>
                </c:when>
            </c:choose>

        </div>
        <img src="dropmusic.png" class="center" >

    </c:otherwise>



</c:choose>



</body>
</html>
