

<?php


$query_string = array();
$query_string['check_email'] = "SELECT id_user FROM tbl_users WHERE email_user = '".$_POST['uemail']."'";
$query_string['insert_registration'] = "INSERT INTO tbl_users (name_user, surname_user, email_user, password_user, active_user,register_date,last_login_date) VALUES ('".$_POST['fname']."', '".$_POST['lname']."','".$_POST['uemail']."',MD5('".$_POST['pass']."'),0,now(),now())";
?>