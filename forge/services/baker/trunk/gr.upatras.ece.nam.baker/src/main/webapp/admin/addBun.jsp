<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>JAX-RS Upload Form</title>
</head>
<body>
	

	<h1>Bun Upload Form</h1>
	
	<form action="http://127.0.0.1:13000/jaxrs-service/services/baker/repo/users/<%= request.getParameter("userid")  %>/buns/" 
			target="viewBuns.jsp"		method="post" enctype="multipart/form-data">
		Bun name : <input type="text" name="bunname" value="BunName!"/><br>
		Version : <input type="text" name="version" value="1.0.2rc1"/><br>
		Short Description : <input type="text" name="shortDescription" value="A shortDescription for this bun"/><br>
		Long Description : <textarea " name="longDescription" >A longDescription for this bun! !!</textarea><br>
		<p>
			Icon File : <input type="file" name="uploadedBunIcon" size="50" />
		</p>

		<p>
			Bun File : <input type="file" name="uploadedBunFile" size="50" />
		</p>

		<input type="submit" value="Submit" />
	</form>


</body>
</html>