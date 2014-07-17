<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View Users</title>
	
	<script src="http://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
	<link rel="stylesheet" href="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.0/themes/smoothness/jquery-ui.css" />
	<script src="//ajax.googleapis.com/ajax/libs/jqueryui/1.11.0/jquery-ui.min.js"></script>	
</head>
<body>

 <a href="index.html">Main</a> 
    <div id="placeholder"></div>
    
	    
    <script>
    
    $(document).ready(function() {
    	loadData();
  	});
    
    function loadData(){
		  $.getJSON('http://127.0.0.1:13000/jaxrs-service/services/baker/repo/users', function(data) {
		      var output="<table>"+ 
		      		"<tr>" + 
		   	  	   "<th>ID</th>" +
		    	   "<th>Name</th>" +
		    	   "<th>organization</th>" +
		    	   "<th>username</th>" +
		    	   "<th>password</th>" +
		    	   "<th>Buns</th>" +
		    	   "<th>Actions</th>" +
		    	 	"</tr>";
		        for (var i in data) {
		            output+="<tr>" + 
		            "<td>" + data[i].id + "</td> " + 
		            "<td>" + data[i].name + "</td> " + 
		            "<td>" + data[i].organization + "</td> " + 
		            "<td>" + data[i].username + "</td> " + 
		            "<td>" + data[i].password + "</td> " +  
		            "<td><a href='viewBuns.jsp?userid=" + data[i].id + "'>Buns</a></td> " + 
		            "<td><a href='addBun.jsp?userid=" + data[i].id + "'>New Bun</a> | "+
		            "<a href='#' onClick='removeItem(" + data[i].id + ")'>Delete</a></td> " + 
		            
		            "</tr>";
		        }
		
		        output+="</table>";
		        document.getElementById("placeholder").innerHTML=output;
		        
		
		        
		  });
		  
		  return false;
    }

  	var dialogMsg = "<p>Remove user?</p>";
  
  	function removeItem(checkID) {
	    //var checkID = $(this).
	    console.log(checkID);
	    
	    $(dialogMsg).dialog( {
	        buttons: {
	            "No" : function () {
	            	 $(this).dialog("destroy");
	            },
	            "Yes": function () {
					$.ajax({
					  url:'http://127.0.0.1:13000/jaxrs-service/services/baker/repo/users/'+checkID,
					  type:"DELETE",
					  success: function(dataX){
						  console.log("result= "+ dataX );
						  $('#placeholder').html("");
						  loadData();
					  }
					}); 
		             $(this).dialog("destroy");  
	            }
	        }
	    });
	};

  
  
  
    </script>
    
</body>


</html>