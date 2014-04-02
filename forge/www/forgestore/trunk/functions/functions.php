<?php

function is_logged_in()
{
	if($_SESSION['AUTHENTICATION_MARKET'] == "")
	{
		header ( 'Location: login.php' );
	}
}

function session_config()
{
	$_SESSION['AUTHENTICATION_MARKET'] = "";
	$_SESSION['USERID_MARKET'] = 0;
	
	$_SESSION['EMAIL_MARKET'] = "";
	$_SESSION['FNAME_MARKET'] = "";
	$_SESSION['LNAME_MARKET'] = "";
	$_SESSION['UROLE_MARKET'] = "";
	
	$_SESSION['SESSION_MARKET'] = true;
}

function Register_user()
{
	
}


?>