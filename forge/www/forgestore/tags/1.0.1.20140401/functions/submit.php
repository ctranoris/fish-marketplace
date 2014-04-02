<?php
	include "conf.php";
	include "query.php";
	
	// we check if everything is filled in

	if(empty($_POST['fname']) || empty($_POST['lname']) || empty($_POST['uemail']) || empty($_POST['pass']))
	{
		die(msg(0,"All the fields are required!"));		
	}

	// is the sex selected?
	if(!(int)$_POST['sex-select'])
	{
		die(msg(0,"You have to select your sex"));
	}

	// is the email valid?
	if(!(preg_match("/^[\.A-z0-9_\-\+]+[@][A-z0-9_\-]+([.][A-z0-9_\-]+)+[A-z]{1,4}$/", $_POST['uemail'])))
	{
		die(msg(0,"You haven't provided a valid email"));
	}
	// Here you must put your code for validating and escaping all the input data,
	// inserting new records in your DB and echo-ing a message of the type:

	// echo msg(1,"/member-area.php");

	// where member-area.php is the address on your site where registered users are
	// redirected after registration.

	if($_POST['uemail'] != $_POST['v_uemail'])
	{
		die(msg(0,"Verification email doesn't match with the email!"));
	}
	
	if($_POST['pass'] != $_POST['vpass'])
	{
		die(msg(0,"Verification password doesn't match with the password!"));
	}
	
	//$querystring_registration = "INSERT INTO tbl_users (name_user, surname_user, email_user, password_user, active_user,register_date,last_login_date) VALUES ('".$_POST['fname']."', '".$_POST['lname']."','".$_POST['uemail']."',MD5('".$_POST['pass']."'),0,now(),now())";
	//die(msg(0,$querystring_registration));
	//$results_register = mysql_query($querystring_registration);
	$query_check_mail="SELECT id_user FROM tbl_users WHERE email_user = '".$_POST['uemail']."'";
	//$result_check_mail = mysql_query("SELECT id_user FROM tbl_users WHERE email_user = '".$_POST['uemail']."'");
	$result_check_mail = $connection->query($query_check_mail);
	
	if(mysql_num_rows($result_check_mail) == 0)
	{	
		$query_register="INSERT INTO tbl_users (name_user, surname_user, email_user, password_user, active_user,register_date,last_login_date) VALUES ('".$_POST['fname']."', '".$_POST['lname']."','".$_POST['uemail']."',MD5('".$_POST['pass']."'),0,now(),now())";
		//$results_register = mysql_query("INSERT INTO tbl_users (name_user, surname_user, email_user, password_user, active_user,register_date,last_login_date) VALUES ('".$_POST['fname']."', '".$_POST['lname']."','".$_POST['uemail']."',MD5('".$_POST['pass']."'),0,now(),now())");
		$results_register = $connection->query($query_register);
		
		if ($results_register)
		{
			echo msg(1,"registered.php");
		}
		else
		{
			//die(msg(0,$querystring_registration));
			die(msg(0,""));
		}
	}
	else
	{
		die(msg(0,"The email is in use!"));
	}
	
	
	function msg($status,$txt)
	{
		return '{"status":'.$status.',"txt":"'.$txt.'"}';
	}
		
?>