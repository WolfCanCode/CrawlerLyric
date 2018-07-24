function bindXml() {
    // var xmlString = `${requestScope.songXML}`;
    var parser = new DOMParser();
    var xmlDoc = parser.parseFromString(xmlString, "text/xml");
    var listSong = "";
    var x = xmlDoc.getElementsByTagName("song");
    song = getTemplate(x[0].getAttribute("id"), x[0].getElementsByTagName("name")[0].childNodes[0].nodeValue,
        x[0].getElementsByTagName("artist")[0].childNodes[0].nodeValue,
        x[0].getElementsByTagName("lyric")[0]);
    document.getElementById("content").innerHTML = song;
}

function getTemplate(id, songName, author, listVerse) {
    var template = "";
    document.getElementById("title").innerHTML = songName;
    if (listVerse.childNodes.length != 0) {
        template = "<h1>" + songName + "</h1><br><b>--" + author + "--</b><br>\n";
        for (let i = 0; i < listVerse.childNodes.length; i++) {
            template += "    <p>" + listVerse.childNodes[i].textContent + "</p>\n";
        }
    }
    else {
        template = "<a href='song/" + id + "'>" + songName + "</a>- " + author + "<br>\n" +
            "    <p style='font-size:10px'>...</p>";
    }
    return template;
}

function effectClick() {
    document.getElementById("container").classList.add("minimize");
}

