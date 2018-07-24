<%@ page pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <title>RemTheLy</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <link href="/css/song.css" rel="stylesheet"/>
</head>
<body onload="loadXSL()">
<div class="header">
    <div class="logo">
        <img src="/images/remthely.png"/>
        <span>Don't remember the lyric? Use it, it also call <b>Rem</b>ember<b>TheLy</b>ric<br>
        Just give me the clue, it will give you all the thing you need
        </span>
    </div>
    <div class="search-box">
        <input type="text" name="q" id="key" value="" onchange="loadXMLDoc()"
               placeholder="Give me some word..."/>
        <input type="hidden" id="lastkey" value="${param.key}"/>
    </div>
</div>
<div class="result"><h3 id="result"></h3></div>
<div class="body">
    <div id="songList">
    </div>
    <div id="page">

    </div>
</div>
<div class="overlay" id="overlay">
    <img src="/images/loading.gif"/>
</div>
</body>
<script type="text/javascript" src="/js/song.js"></script>
</html>