function httpGetAsync(theUrl, callback)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
            callback(xmlHttp.responseText);
    }
    xmlHttp.open("GET", theUrl, true); // true for asynchronous
    xmlHttp.send(null);
}

function getUploadLink() {
    httpGetAsync("https://translationpricer.appspot.com/upload?reason=getUrl", function(resp){
	//httpGetAsync("http://localhost:8080/upload?reason=getUrl", function(resp){
        document.getElementById('upload').action=resp;

    })

}