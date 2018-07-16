<!DOCTYPE HTML>
<html>
<head>
    <title>Getting Started: Serving Web Content</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
<h3>Search song</h3>
<input type="text" name="q" id="key"/><input type="submit" onclick="loadXMLDoc()" value="Search">
<div id="songList">

</div>
</body>
<script>
    function loadXMLDoc() {
        var key = document.getElementById("key").value;
        if (key === null) {
            alert("Please give me atleast a letter");
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
        if (listVerse != null) {
            template = "<a href='song/" + id + "'>" + songName + "</a>- " + author + "<br>\n" +
                "    <p style='font-size:10px'>" + listVerse.childNodes[0].textContent + "</p>\n" +
                "    <p style='font-size:10px'>" + listVerse.childNodes[1].textContent + "</p>\n" +
                "    <p style='font-size:10px'>" + listVerse.childNodes[2].textContent + "</p>\n" +
                "    <p style='font-size:10px'>...</p>";
        }
        else {
            template = "<a href='song/" + id + "'>" + songName + "</a>- " + author + "<br>\n" +
                "    <p style='font-size:10px'>...</p>";
        }
        return template;
    }

    function bindXml(xml) {
        var i;
        var xmlDoc = xml.responseXML;
        var listSong = "";
        var x = xmlDoc.getElementsByTagName("songs");
        for (i = 0; i < x.length; i++) {
            listSong += getTemplate(x[i].getAttribute("id"), x[i].getElementsByTagName("name")[0].childNodes[0].nodeValue,
                x[i].getElementsByTagName("artist")[0].childNodes[0].nodeValue,
                x[i].getElementsByTagName("lyric")[0]);

        }

        document.getElementById("songList").innerHTML = listSong;

    }
</script>
</html>