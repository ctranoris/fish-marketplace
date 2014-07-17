<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add user Form</title>
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
</head>
<body>

	<a href="index.html">Main</a>
	<h1>Add user Form</h1>

	<form
		action="http://127.0.0.1:13000/jaxrs-service/services/baker/repo/users/"
		id="addUserForm">
		Name : <input type="text" name="name" value="" placeholder="name..." /><br>
		Username : <input type="text" name="username" value="" /><br>
		Password : <input type="text" name="password" value="" /><br>
		Organization : <input type="text" name="organization" value="" /><br>
		<input type="submit" value="Submit" />
	</form>
	<!-- the result of the search will be rendered inside this div -->
<div id="result"></div>

	<script>
		// Attach a submit handler to the form
		$("#addUserForm").submit(
				function(event) {
					// Stop form from submitting normally
					event.preventDefault();
					// Get some values from elements on the page:
					var $form = $(this), 
						vname = $form.find("input[name='name']").val(),
						vusername = $form.find("input[name='username']").val(), 
						vpassword = $form.find("input[name='password']").val(), 
						vorganization = $form.find("input[name='organization']").val(), 
						url = $form.attr("action");
					
					var postData={name : vname,
							username : vusername,
							password : vpassword,
							organization : vorganization};
					// Send the data using post
					$.ajax({
					  url:url,
					  type:"POST",
					  data:JSON.stringify(postData),
					  contentType:"application/json; charset=utf-8",
					  dataType:"json",
					  success: function(dataX){
						  console.log( dataX );
							$("#result").empty().append("Added user: "+dataX.name);
					  }
					});
					
					
					
				});
	</script>
</body>
</html>