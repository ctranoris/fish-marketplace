<?php
	include "session.php";
	$_SESSION['AUTHENTICATION'] = "";
	session_destroy();		
	
	header('Location: ../login.php'); 
	
?>