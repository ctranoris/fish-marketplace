<?php
	include "functions/functions.php";
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
		<div style="border: solid 1px grey; position:absolute; top:50%; left:50%; height: 500px; margin-top: -350px; width: 400px; margin-left: -200px; padding:20px;">
			<h2>Register to ForgeBox</h2>
			<br />
			<form id="regForm" action="functions/submit.php" method="post">
				<div class="input-control text">
					<input type="text" value="" placeholder="input name" name="fname" id="fname" />
					<button class="btn-clear"></button>
				</div>
				<div class="input-control text">
					<input type="text" value="" placeholder="input surname" name="lname" id="lname" />
					<button class="btn-clear"></button>
				</div>
				<div class="input-control text">
					<input type="text" value="" placeholder="input email" name="uemail" id="uemail" />
					<button class="btn-clear"></button>
				</div>
				<div class="input-control text">
					<input type="text" value="" placeholder="input verify email" name="v_uemail" id="v_uemail" />
					<button class="btn-clear"></button>
				</div>
				<div class="input-control password">
					<input type="password" value="" placeholder="input password" name="pass" id="pass" />
					<button class="btn-reveal"></button>
				</div>
				<div class="input-control password">
					<input type="password" value="" placeholder="input verify password" name="vpass" id="vpass" />
					<button class="btn-reveal"></button>
				</div>
				<div class="input-control select" data-transform="input-control">
					<select name="sex-select"  id="">
						<option value="0">select male/female</option>
						<option value="1">Male</option>
						<option value="2">Female</option>
					</select>
				</div>								
				<br />				
				<input type="submit" id="submit" value="Submit"></input> or <a href="login.php">Sign in to ForgeBox</a>	<br />
				<img id="loading" src="images/ajax-loader.gif" alt="working.." />
			</form>
			
			<div id="error">
				&nbsp;
			</div>

			<script>
				hideshow('loading',0);
				
				$(document).ready(function(){
					$('#regForm').submit(function(e) {						
						register();
						e.preventDefault();		
					});
				});

				function register()
				{
					alert($('#regForm').serialize());
					hideshow('loading',1);
					error(0);	
					$.ajax({
						type: "POST",
						url: "functions/submit.php",
						data: $('#regForm').serialize(),
						dataType: "json",
						success: function(msg){
							if(parseInt(msg.status)==1)
							{
								window.location=msg.txt;
							}
							else if(parseInt(msg.status)==0)
							{
								error(1,msg.txt);
							}
							
							hideshow('loading',0);
						}
					});
				}
		
				function hideshow(el,act)
				{
					if(act) $('#'+el).css('visibility','visible');
					else $('#'+el).css('visibility','hidden');
				}

				function error(act,txt)
				{
					hideshow('error',act);
					if(txt) $('#error').html(txt);
				}

			</script>
			<br />
		</div>		
	</body>
</html>