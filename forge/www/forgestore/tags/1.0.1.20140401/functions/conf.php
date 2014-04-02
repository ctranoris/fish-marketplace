<?php

$database_host="150.140.185.181";
$database_username="dbforge";
$database_password="root";
$db_name = "marketplace_manage";
$connection = mysqli_connect($database_host, $database_username, $database_password,$db_name);
if($connection){
	$_SESSION["database_connect"]="1";	
	//$db   = mysql_select_db('marketplace_manage');	
}
else
{
	$_SESSION["database_connect"]="0";	
}


/*
$mysqli=new mysqli($database_host,$database_username,$database_password,$db);

if($mysqli->connect_error){
	$_SESSION["database_connect"]="0";
	die('Connect Error(' . $mysqli->connect_errno.') '.$mysqli->connect_error);
	
}
else
{
$_SESSION["database_connect"]="1";
}*/

?>