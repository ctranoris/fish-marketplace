<?php
	include "session.php";
	$_SESSION['AUTHENTICATION_MARKET'] = "";
	session_destroy();		
	
	header('Location: ../login.php'); 
	
?>