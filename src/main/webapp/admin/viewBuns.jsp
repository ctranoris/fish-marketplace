<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View buns</title>
</head>
<body>

 <a href="index.html">Main</a> 
    <div id="placeholder"></div>
    
    <script src="http://code.jquery.com/jquery-1.7.1.min.js"></script>
    <script>
    
    function getUrlVars() {
        var vars = {};
        var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi, function(m,key,value) {
            vars[key] = value;
        });
        return vars;
    }
    
    var bunsUrl = 'http://127.0.0.1:13000/jaxrs-service/services/baker/repo/buns';
    var userid = getUrlVars()["userid"];
    if (userid){
    	bunsUrl = 'http://127.0.0.1:13000/jaxrs-service/services/baker/repo/users/'+userid+'/buns';
    }
    
    
  $.getJSON(bunsUrl, function(data) {
      var output="<table>"+ 
      		"<tr>" + 
   	  	   "<th>ID</th>" +
    	   "<th>Name</th>" +
    	   "<th>Version</th>" +
    	   "<th>shortDescription</th>" +
    	   "<th>longDescription</th>" +
    	   "<th>uuid</th>" +
    	   "<th>Image</th>" +
    	   "<th>packageLocation</th>" +
    	   "<th>owner</th>" +
    	 	"</tr>";
        for (var i in data) {
            output+="<tr>" + 
            "<td>" + data[i].id + "</td> " + 
            "<td>" + data[i].name + "</td> " + 
            "<td>" + data[i].version + "</td> " + 
            "<td>" + data[i].shortDescription + "</td> " + 
            "<td>" + data[i].longDescription + "</td> " + 
            "<td>" + data[i].uuid + "</td> " + 
            "<td><img width='80px' src='" + data[i].iconsrc + "'</td> " + 
            "<td><a href='" + data[i].packageLocation + "'>Package</a> </td> " + 
            "<td><a href='/jaxrs-service/services/baker/repo/users/" + data[i].owner.id + "'>" + data[i].owner.username + "</a></td> " + 
            "</tr>";
        }

        output+="</table>";
        document.getElementById("placeholder").innerHTML=output;
  });
    </script>
    
</body>


</html>