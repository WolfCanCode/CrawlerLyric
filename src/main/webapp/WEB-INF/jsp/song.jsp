<!DOCTYPE HTML>
<html>
<head>
    <title>Search song</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body onload="loadXMLDoc()">
<h3>Search song</h3>
<input type="text" name="q" id="key" value="${param.key}" onchange="loadXMLDoc()"/><input type="submit" value="Search">
<h3 id="result"></h3>
<div id="songList">

</div>
</body>
<script>
    function loadXMLDoc() {
        var key = document.getElementById("key").value;
        if (key === null || key === "") {
        } else {
            var xhttp = new XMLHttpRequest();

            xhttp.onreadystatechange = function () {
                if (this.readyState == 4 && this.status == 200) {
                    document.getElementById("demo").innerHTML =
                        bindXml(this);

                }
            };
            xhttp.open("GET", "http://localhost:8080/song/search?q=" + key, true);
            xhttp.send();
        }
    }

    function getTemplate(id, songName, author, listVerse) {
        var template = "";
        var key = document.getElementById("key").value;
        if (listVerse.childNodes.length != 0) {
            template = "<a href='song/" + id + "?callback=" + key + "'>#" + id + " -" + songName + "</a>- " + author + "<br>\n";
            for (let i = 0; i < listVerse.childNodes.length; i++) {
                if (i > 3) break;
                template += "    <p style='font-size:10px'>" + listVerse.childNodes[i].textContent + "</p>\n";
            }
            template += "    <p style='font-size:10px'>...</p>";
        }
        else {
            template = "<a href='song/" + id + "'>" + songName + "</a>- " + author + "<br>\n" +
                "    <p style='font-size:10px'>...</p>";
        }
        return template;
    }

    function bindXml(xml) {
        var xmlDoc = xml.responseXML;
        var listSong = "";
        var x = xmlDoc.getElementsByTagName("songs");
        var key = document.getElementById("key").value;
        document.getElementById("result").innerHTML = "Have found " + x.length + " songs have keyword '" + key + "'";
        for (i = 0; i < x.length; i++) {
            listSong += getTemplate(x[i].getAttribute("id"), x[i].getElementsByTagName("name")[0].childNodes[0].nodeValue,
                x[i].getElementsByTagName("artist")[0].childNodes[0].nodeValue,
                x[i].getElementsByTagName("lyric")[0]);

        }

        document.getElementById("songList").innerHTML = listSong;

    }
</script>
</html>