<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE HTML>
<html>
<head>
    <title id="title"></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body onload="bindXml()">
<a href="/song?key=${param.callback}">Back</a>
<h3 id="songName"></h3>
<div id="songList">
</div>
</body>
<script>
    function bindXml() {
        var xmlString = `${requestScope.songXML}`;
        var parser = new DOMParser();
        var xmlDoc = parser.parseFromString(xmlString, "text/xml");
        var listSong = "";
        var x = xmlDoc.getElementsByTagName("song");
        song = getTemplate(x[0].getAttribute("id"), x[0].getElementsByTagName("name")[0].childNodes[0].nodeValue,
            x[0].getElementsByTagName("artist")[0].childNodes[0].nodeValue,
            x[0].getElementsByTagName("lyric")[0]);
        document.getElementById("songList").innerHTML = song;
    }

    function getTemplate(id, songName, author, listVerse) {
        var template = "";
        document.getElementById("title").innerHTML = songName;
        if (listVerse.childNodes.length != 0) {
            template = "<h1>" + songName + "</h1><br><b> " + author + "</b><br>\n";
            for (let i = 0; i < listVerse.childNodes.length; i++) {
                template += "    <p style='font-size:16px'>" + listVerse.childNodes[i].textContent + "</p>\n";
            }
        }
        else {
            template = "<a href='song/" + id + "'>" + songName + "</a>- " + author + "<br>\n" +
                "    <p style='font-size:10px'>...</p>";
        }
        return template;
    }


</script>
</html>