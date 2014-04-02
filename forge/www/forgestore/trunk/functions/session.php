<?php
	session_start();
	include "conf.php";
	
	if (!isset($_SESSION['SESSION_MARKET'])) include ( "session_init.php");

	if(!empty($_POST['username']) && !empty($_POST['password']))
	{
		$Select_user_query="SELECT id_user,name_user,surname_user,email_user FROM tbl_users WHERE email_user='".addslashes($_POST['username'])."' AND  password_user=MD5('".addslashes($_POST['password'])."') AND active_user=1";		
		
		//$result = mysql_query($Select_user_query);
		$result = $connection->query($Select_user_query);
		//$row = mysql_fetch_row($result);
		while($row = $result->fetch_row())
		{		
			if(!empty($row[0]))
			{
							
				$_SESSION['AUTHENTICATION_MARKET'] = true;
				$_SESSION['USERID_MARKET'] = $row[0];
				$_SESSION['EMAIL_MARKET'] = $row[3];
				$_SESSION['FNAME_MARKET'] = $row[1];
				$_SESSION['LNAME_MARKET'] = $row[2];
				
				$Select_user_role_query="SELECT tbl_role.name_role FROM tbl_role INNER JOIN tbl_user_role ON tbl_role.id_role=tbl_user_role.id_role WHERE tbl_user_role.id_user=".$_SESSION['USERID_MARKET'];	
				//$result_user_role = mysql_query($Select_user_role_query);
				$result_user_role = $connection->query($Select_user_role_query);
				
				//while($row1 = mysql_fetch_array($result_user_role)){
				while($row1 = $result_user_role->fetch_row()){
					$_SESSION['UROLE_MARKET'] = $row1[0];				
				}
				
			}
			else
			{
				$_SESSION['AUTHENTICATION_MARKET'] = "";
			}
		}
	}	
		
?>