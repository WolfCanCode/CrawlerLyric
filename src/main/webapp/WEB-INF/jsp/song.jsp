<!DOCTYPE HTML>
<html>
<head>
    <title>Search song</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <style>
        html {
            background: url(images/wll.jpg) no-repeat center center fixed;
            -webkit-background-size: cover;
            -moz-background-size: cover;
            -o-background-size: cover;
            background-size: cover;
            overflow-y: scroll;
        }

        body {
            border: 0;
            margin: 0;
            padding: 0;
        }

        .header {
            width: 100%;
            height: 400px;
            display: flex;
            flex-direction: column;

        }

        .header .logo {
            flex: 2;
            display: flex;
            flex-direction: column;
            align-self: center;
            align-items: center;

        }

        .header .logo img {
            height: 100px;
            width: 1000px;
            flex: 5;
        }

        .header .logo span {
            flex: 1;
            color: #ccc;
            font-family: "Calibri Light";
            font-size: 18px;
        }

        .header .search-box {
            flex: 1;
            padding-top: 30px;
            align-self: center;
        }

        .header .search-box input[type='text'] {
            height: 40px;
            width: 500px;
            border-radius: 5px;
            font-size: 25px;
            padding: 10px;
            border: 0;
            background: rgba(255, 255, 255, 0.2);
            color: white;
        }

        .result {
            align-self: center;
            font-family: Calibri;
            color: white;
        }

        .body {
            width: 100%;
            height: auto;
        }

        .body .song {
            background: #fff;
            width: 600px;
            text-align: center;
            margin: auto;
            border: 0;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 15px;
        }

        .song a {
            font-size: 35px;
            text-decoration: none;
            font-family: Calibri;
            font-weight: bold;
        }

        .song span {
            font-size: 15px;
            font-style: italic;
        }

        .song p {
            font-size: 20px;
            font-family: "Calibri Light";
        }

        .overlay {
            background: rgba(0, 0, 0, 0.6);
            height: 1000px;
            width: 100%;
            top: 0;
            display: none;
            text-align: center;
            position: fixed;
        }

        .overlay img {
            height: 40px;
            width: 40px;
            margin-top: 400px;
        }
    </style>
</head>
<body onload="loadXMLDoc()">
<div class="header">
    <div class="logo">
        <img src="/images/remthely.png"/>
        <span>Don't remember the lyric? Use it, it also call <b>Rem</b>ember<b>TheLy</b>ric<br>
        Just give me the clue, it will give you all the thing you need
        </span>
    </div>
    <div class="search-box">
        <input type="text" name="q" id="key" value="${param.key}" onchange="loadXMLDoc()"
               placeholder="Give me some word..."/>
        <div class="result"><h3 id="result"></h3></div>
    </div>
</div>
<div class="body">
    <div id="songList">
    </div>
</div>
<div class="overlay" id="overlay">
    <img src="/images/loading.gif"/>
</div>
</body>
<script>
    function loadXMLDoc() {
        var key = document.getElementById("key").value;
        if (key === null || key === "") {
        } else {
            document.getElementById("overlay").style.display = "block";
            var xhttp = new XMLHttpRequest();

            xhttp.onreadystatechange = function () {
                if (this.readyState == 4 && this.status == 200) {
                    document.getElementById("overlay").style.display = "none";
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
            template = "<div class='song'><a href='song/" + id + "?callback=" + key + "'>" + songName + "</a><br><span>" + author + "</span>";
            for (let i = 0; i < listVerse.childNodes.length; i++) {
                if (i > 3) break;
                template += "    <p>" + listVerse.childNodes[i].textContent + "</p>\n";
            }
            template += "    <p>...</p></div>";
        }
        else {
            template = "<a href='song/" + id + "'>" + songName + "</a>- " + author + "<br>\n" +
                "    <p>...</p></div>";
        }
        return template;
    }

    function bindXml(xml) {
        var xmlDoc = xml.responseXML;
        var listSong = "";
        var x = xmlDoc.getElementsByTagName("songs");
        var key = document.getElementById("key").value;
        document.getElementById("result").innerText = "Have found " + x.length + " songs have keyword '" + key + "'";
        for (i = 0; i < x.length; i++) {
            listSong += getTemplate(x[i].getAttribute("id"), x[i].getElementsByTagName("name")[0].childNodes[0].nodeValue,
                x[i].getElementsByTagName("artist")[0].childNodes[0].nodeValue,
                x[i].getElementsByTagName("lyric")[0]);

        }

        document.getElementById("songList").innerHTML = listSong;

    }
</script>
</html>