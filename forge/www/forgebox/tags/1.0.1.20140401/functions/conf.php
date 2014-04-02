<?php

$database_host="150.140.185.181";
$database_username="dbforge";
$database_password="root";

/*
if(mysql_connect($database_host, $database_username, $database_password)){
	$_SESSION["database_connect"]="1";
	$db   = mysql_select_db('forgebox');
}
else
{
	$_SESSION["database_connect"]="0";
}*/

$db_name = "forgebox";
$connection = mysqli_connect($database_host, $database_username, $database_password,$db_name);
if($connection){
	$_SESSION["database_connect"]="1";	
	//$db   = mysql_select_db('marketplace_manage');	
}
else
{
	$_SESSION["database_connect"]="0";	
}


?>
