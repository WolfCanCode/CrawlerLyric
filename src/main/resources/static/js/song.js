var xsl = "";
var xml = "";
var lengthOnce = 6;
var url = "http://1812c883.ngrok.io";

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
    xhttp.open("GET", url + "/xsl/listsong.xsl", true);
    xhttp.send();
}

function init() {
    var key = document.getElementById("lastkey").value;
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
                xml = this.responseXML;
                onPage(1);
            }
        };
        xhttp.open("GET", url + "/song/search?q=" + key, true);
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
                xml = this.responseXML;
                onPage(1);

            }
        };
        xhttp.open("GET", url + "/song/search?q=" + key, true);
        xhttp.send();
    }
}

function onPage(page) {
    var listSong = "";
    var x = xml.getElementsByTagName("songs");
    var key = document.getElementById("key").value;
    document.getElementById("result").innerText = "Have found " + x.length + " songs have keyword '" + key + "'";
    xsltProcessor = new XSLTProcessor();
    xsltProcessor.importStylesheet(xsl);
    xsltProcessor.setParameter(null, "callback", window.btoa(unescape(encodeURIComponent(key))));
    xsltProcessor.setParameter(null, "page", page);
    var resultDocument = xsltProcessor.transformToFragment(xml, document);
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
    if (x.length != 0) {
        if (page > lengthOnce) {
            lengthOnce += 6;
            console.log(lengthOnce);
        } else if (page < lengthOnce - 5) {
            lengthOnce -= 6;
            console.log(lengthOnce);
        }
        var pageBtn = "<div class='pagination' id='pagination'>";
        if (lengthOnce > 6) {
            pageBtn += "<button onclick='onPage(" + (lengthOnce - 6) + ")'>...</button>";
        }
        var numPage = 0;
        if (x.length % 10 == 0) {
            numPage = x.length / 10;
        }
        else {
            numPage = x.length / 10 + 1;
        }

        for (var i = lengthOnce - 5; i <= numPage; i++) {
            if (i == lengthOnce + 1) break;
            if (i == page) {
                pageBtn += "<button class='active' onclick='onPage(" + i + ")'>" + i + "</button>";
            } else {
                pageBtn += "<button onclick='onPage(" + i + ")'>" + i + "</button>";
            }
        }
        if (numPage >= 7 && lengthOnce < numPage) {
            pageBtn += "<button onclick='onPage(" + (lengthOnce + 1) + ")'>...</button></div>";
        }
        document.getElementById("page").innerHTML = pageBtn;
        scrollToTop();
    } else {
        document.getElementById("page").innerHTML = "";
    }
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

function scrollToTop() {
    var scrollStep = -window.scrollY / (400 / 15),
        scrollInterval = setInterval(function () {
            if (window.scrollY != 0) {
                window.scrollBy(0, scrollStep);
            }
            else clearInterval(scrollInterval);
        }, 15);
}
