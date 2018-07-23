<%@ page pageEncoding="UTF-8" %>
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
            height: 450px;
            display: flex;
            flex-direction: column;
            z-index: 9999;

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
            text-align: center;
            font-family: Calibri;
            color: white;
            font-size: 22px;
        }

        .body {
            width: 100%;
            height: auto;
            z-index: 9999;
        }

        .body .itmSg {
            background: #fff;
            width: 600px;
            text-align: center;
            margin: auto;
            border: 0;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 15px;
            transition: 1s all;
        }

        .itmSg a {
            font-size: 35px;
            text-decoration: none;
            font-family: Calibri;
            font-weight: bold;
        }

        .itmSg span {
            font-size: 15px;
            font-style: italic;
        }

        .itmSg p {
            font-size: 20px;
            font-family: "Calibri Light";
        }

        .overlay {
            background: rgba(0, 0, 0, 0.6);
            height: 1000px;
            width: 100%;
            top: 0;
            display: block;
            text-align: center;
            position: fixed;
            opacity: 0;
            z-index: -1;
            transition: 1s all;
        }

        .overlay img {
            height: 40px;
            width: 40px;
            margin-top: 400px;
        }

        .expand {
            height: 100% !important;
            width: 100% !important;
            background: black !important;
            transition: 1s all;
        }

        .display-show {
            opacity: 1 !important;
            z-index: 9999;
            transition: 1s alls;
        }
    </style>
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
        <input type="text" name="q" id="key" value="${param.key}" onchange="loadXMLDoc()"
               placeholder="Give me some word..."/>
    </div>
    <div class="result"><h3 id="result"></h3></div>

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
    var xsl = "";

    function loadXSL() {
        document.getElementById("overlay").classList.add("display-show");
        var xhttp = new XMLHttpRequest();
        xhttp.onreadystatechange = function () {
            if (this.readyState == 4 && this.status == 200) {
                var parser = new DOMParser();
                xsl = parser.parseFromString(this.response, "text/xml");
                document.getElementById("overlay").classList.remove("display-show");
                init();
            }
        };
        xhttp.open("GET", "http://localhost:8080/xsl/listsong.xsl", true);
        xhttp.send();
    }

    function init() {
        var key = document.getElementById("key").value;
        if (key === null || key === "") {
        } else {
            try {
                document.getElementById("key").value = decodeURIComponent(escape(window.atob(key)));
                key = decodeURIComponent(escape(window.atob(key)));
                document.getElementById("overlay").classList.add("display-show");
            } catch (e) {
                window.location = "song";
            }
            var xhttp = new XMLHttpRequest();

            xhttp.onreadystatechange = function () {
                if (this.readyState == 4 && this.status == 200) {
                    bindXml(this);

                }
            };
            xhttp.open("GET", "http://localhost:8080/song/search?q=" + key, true);
            xhttp.send();
        }
    }

    function loadXMLDoc() {
        var key = document.getElementById("key").value;
        if (key.toLowerCase() == "di"
            || key.toLowerCase() == "li"
            || key.toLowerCase() == "br"
            || key.toLowerCase() == "pa"
            || key.toLowerCase() == "sp"
            || key.toLowerCase() == "spa"
            || key.toLowerCase() == "pan"
            || key.toLowerCase() == "span"
            || key.toLowerCase() == "iv") {
            document.getElementById("result").innerHTML = "<font style='color:red'>This is sensitive word, change another one</font>";
        }
        else if (key === null || key === "" || key.length < 2) {
            document.getElementById("key").style.border = "solid 1px red";
            document.getElementById("result").innerHTML = "<font style='color:red'>Please we need at least a word!</font>";
        } else {
            document.getElementById("overlay").classList.add("display-show");
            var xhttp = new XMLHttpRequest();

            xhttp.onreadystatechange = function () {
                if (this.readyState == 4 && this.status == 200) {
                    bindXml(this);

                }
            };
            xhttp.open("GET", "http://localhost:8080/song/search?q=" + key, true);
            xhttp.send();
        }
    }

    // function getTemplate(id, songName, author, listVerse) {
    //     var template = "";
    //     var key = document.getElementById("key").value;
    //     if (listVerse.childNodes.length != 0) {
    //         template = "<div class='song'><a href='song/" + id + "?callback=" + key + "'>" + songName + "</a><br><span>" + author + "</span>";
    //         for (let i = 0; i < listVerse.childNodes.length; i++) {
    //             if (i > 3) break;
    //             template += "    <p>" + listVerse.childNodes[i].textContent + "</p>\n";
    //         }
    //         template += "    <p>...</p></div>";
    //     }
    //     else {
    //         template = "<a href='song/" + id + "'>" + songName + "</a>- " + author + "<br>\n" +
    //             "    <p>...</p></div>";
    //     }
    //     return template;
    // }

    function bindXml(xml) {
        var xmlDoc = xml.responseXML;
        var listSong = "";
        var x = xmlDoc.getElementsByTagName("songs");
        var key = document.getElementById("key").value;
        document.getElementById("result").innerText = "Have found " + x.length + " songs have keyword '" + key + "'";
        xsltProcessor = new XSLTProcessor();
        xsltProcessor.importStylesheet(xsl);
        xsltProcessor.setParameter(null, "callback", window.btoa(unescape(encodeURIComponent(key))));
        var resultDocument = xsltProcessor.transformToFragment(xmlDoc, document);
        var result = (new XMLSerializer()).serializeToString(resultDocument);
        var keyword = key.split(" ");

        for (var i = 0; i < keyword.length; i++) {
            if (keyword[i].length >= 2) {
                if (keyword[i].toLowerCase() != "di" && keyword[i].toLowerCase() != "li" && keyword[i].toLowerCase() != "br") {
                    result = replaceAll(result, keyword[i])
                }
            }
        }
        document.getElementById("overlay").classList.remove("display-show");
        document.getElementById("songList").innerHTML = result;
    }

    function effectClick(id) {
        obj = document.getElementById(id);
        obj.classList.add("expand");
    }

    function replaceAll(str, find) {
        str = str.split("#039;").join("'");
        str = str.split(find).join("<font style='background:yellow'>" + find + "</font>");
        str = str.split(find.toLowerCase()).join("<font style='background:yellow'>" + find.toLowerCase() + "</font>");
        str = str.split(find.toUpperCase()).join("<font style='background:yellow'>" + find.toUpperCase() + "</font>");
        str = str.split(capitalizeFirstLetter(find)).join("<font style='background:yellow'>" + capitalizeFirstLetter(find) + "</font>");

        return str;
    }

    function capitalizeFirstLetter(string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
    }


</script>
</html>