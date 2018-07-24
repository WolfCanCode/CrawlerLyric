<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <title id="title"></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link href="/css/songById.css" rel="stylesheet"/>
</head>
<body onload="bindXml()">
<div class="container" id="container">
    <a href="/song?key=${param.callback}" onclick="effectClick()" class="btnBack"><</a><br>
    <div id="content">
    </div>
</div>
</body>
<script>var xmlString = `${requestScope.songXML}`;</script>
<script type="text/javascript" src="/js/songById.js"></script>
</html>