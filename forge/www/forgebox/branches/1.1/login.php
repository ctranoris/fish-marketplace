<?php

	error_reporting( E_ALL );
	ini_set('display_errors', 1);
	include "functions/session.php";

	if($_SESSION['AUTHENTICATION']!=null)
	{
		if (!empty($_SESSION['AUTHENTICATION']))
		{		
			header("Location: index.php");			
		}
		else
		{
			header("Location: login.php");	
		}
	}	
?>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
		<meta name="product" content="Forge Box">
		<meta name="description" content="Forge Box App">
		<meta name="author" content="NAM ECE UoP">

		<link href="css/metro-bootstrap.css" rel="stylesheet">
		<link href="css/metro-bootstrap-responsive.css" rel="stylesheet">
		<link href="css/docs.css" rel="stylesheet">
		<link href="js/prettify/prettify.css" rel="stylesheet">

		<!-- Load JavaScript Libraries -->
		<script src="js/jquery/jquery.min.js"></script>
		<script src="js/jquery/jquery.widget.min.js"></script>
		<script src="js/jquery/jquery.mousewheel.js"></script>
		<script src="js/prettify/prettify.js"></script>

		<!-- Metro UI CSS JavaScript plugins -->
		<script src="js/load-metro.js"></script>

		<!-- Local JavaScript -->
		<script src="js/docs.js"></script>
		<script src="js/github.info.js"></script>

		<title>Forge Box App</title>
	</head>
	<body class="metro">
		<div style="border: solid 1px grey; position:absolute; top:50%; left:50%; height: 400px; margin-top: -200px; width: 400px; margin-left: -200px; padding:20px;">
			<h2>Welcome to FORGEBox!</h2>
			<p>Please login with your username and password to access you FORGEBox account</p>
			<br />
			<form action="login.php" method="post">
				<div class="input-control text">
					<input type="text" value="" placeholder="input username" name="username" />
					<button class="btn-clear"></button>
				</div>
				<div class="input-control password">
					<input type="password" value="" placeholder="input password" name="password" />
					<button class="btn-reveal"></button>
				</div>
				
				<br /><br />
				
				<input type="submit" value="Submit"></input> or <a href="register.php">Sign up for ForgeBox</a>
			
			</form>
			<br />
			<a href="http://www.ict-forge.eu/" target="_blank"><img src="images/FORGE_Logo_small.png"/></a>
			<img src="images/eu-commission.png"/>
			<a href="http://www.ict-fire.eu/" target="_blank"><img src="images/FIRE-logo.png"/></a>
		</div>		
	</body>
</html>
