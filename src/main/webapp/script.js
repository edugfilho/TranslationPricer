function httpGetAsync(theUrl, callback)
{
    var xmlHttp = new XMLHttpRequest();
    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 && xmlHttp.status == 200)
            callback(xmlHttp.responseText);
    }
    xmlHttp.open('GET', theUrl, true); // true for asynchronous
    xmlHttp.send(null);
}

function post(data, url, callback) {
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if(request.readyState == 4 && request.status == 200) {
            callback(request.responseText);
        }
    }
	request.open('POST', url, true);
	request.send(data);
}

function getFormData(formElemClass) {
    var elements = document.getElementsByClassName(formElemClass);
    var formData = new FormData(); 
    for(var i=0; i<elements.length; i++){
        formData.append(elements[i].name, elements[i].value);
    }
    return formData;
}

window.onload = function getUploadLink() {
	document.getElementById('submit-file').addEventListener('click', function() {
		var link = "https://translationpricer.appspot.com/upload";
		var files = document.getElementById('doc-input').files;
		if(files[0]) {
			var data = new FormData()
			data.append("doc-input", files[0]);
    		post(data, link, function(result){
        		document.getElementById('result-text').innerHTML = result;
    		});
		}
	});
};
