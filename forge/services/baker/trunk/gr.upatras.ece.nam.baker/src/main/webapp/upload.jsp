<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>JAX-RS Upload Form</title>
</head>
<body>

	<h1>JAX-RS Upload Form</h1>

	<form action="http://localhost:8080/CXFRestUpload/services/rest/uploadFile" method="post" enctype="multipart/form-data">

		<p>
			Select a file : <input type="file" name="uploadedFile" size="50" />
		</p>

		<input type="submit" value="Upload It" />
	</form>


</body>
</html>