<?php

function is_logged_in()
{
	if($_SESSION['AUTHENTICATION'] == "")
	{
		header ( 'Location: login.php' );
	}
}

function session_config()
{
	$_SESSION['AUTHENTICATION'] = "";
	$_SESSION['USERID'] = 0;
	
	$_SESSION['EMAIL'] = "";
	$_SESSION['FNAME'] = "";
	$_SESSION['LNAME'] = "";
	$_SESSION['UROLE'] = "";
	
	$_SESSION['SESSION'] = true;
}

function Register_user()
{
	
}


?>