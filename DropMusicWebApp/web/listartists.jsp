<%--
  Created by IntelliJ IDEA.
  User: pedrochicoria
  Date: 14/11/2018
  Time: 16:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%@taglib uri="/struts-tags" prefix="s" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<head>
    <title>List Artists</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <link rel="stylesheet" type="text/css" href="style.css">
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
            if(message.data === "reload"){
                console.log(message.data);
            }else{
                alert(message.data);
            }
        }

        function onError(event) {
            console.log('WebSocket error (' + event.data + ').');
        }

    </script>

    <style>

        .center {
            display: block;
            margin-left: auto;
            margin-right: auto;
            width: 50%;
        }


        table {
            font-family: arial, sans-serif;
            border-collapse: collapse;

            width: 50%;

        }

        td, th {
            border: 1px solid dimgrey;
            text-align: left;
            padding: 8px;
            width: 30%;
        }

        tr:nth-child(even) {
            background-color: white;
        }

    </style>


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

    <table >
        <tr>
            <th>Artists</th>
        </tr>


        <c:forEach items="${artistBean.listArtist}" var="value">
        <tr>
            <td>
                <c:out value="${value}" />
            </td>
        </tr>
        </c:forEach>
    </table>

        <s:form action="detailsofartist" method="post" theme="simple">
            <h2>Consult Details of Artist</h2>
            <br>
            <h4 style="color: dimgrey">Name:</h4> <s:textfield name="artistBean.name" label="Name"/>
            <br><br>
            <s:submit type="button" label="Consult Details" cssClass="registerbtn"/>
        </s:form>
    </div>


</div>


</body>
</html>
