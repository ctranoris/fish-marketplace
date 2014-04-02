<?php
	session_start();
	include "conf.php";
	
	if (!isset($_SESSION['SESSION'])) include ( "session_init.php");
	
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
							
				$_SESSION['AUTHENTICATION'] = true;
				$_SESSION['USERID'] = $row[0];
				$_SESSION['EMAIL'] = $row[3];
				$_SESSION['FNAME'] = $row[1];
				$_SESSION['LNAME'] = $row[2];
				
				$Select_user_role_query="SELECT tbl_role.name_role FROM tbl_role INNER JOIN tbl_user_role ON tbl_role.id_role=tbl_user_role.id_role WHERE tbl_user_role.id_user=".$_SESSION['USERID'];	
				//$result_user_role = mysql_query($Select_user_role_query);
				$result_user_role = $connection->query($Select_user_role_query);
				$count_roles=0;
				//while($row1 = mysql_fetch_array($result_user_role)){
				while($row1 = $result_user_role->fetch_array()){
					if($count_roles>0)
					{
						$_SESSION['UROLE'] .= "/".$row1[0];	
					}
					else
					{
						$_SESSION['UROLE'] .= $row1[0];	
					}
					$count_roles++;
				}
				
			}
			else
			{
				$_SESSION['AUTHENTICATION'] = "";
			}
		}
	}	
		
?>