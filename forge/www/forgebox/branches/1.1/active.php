<?php
include "functions/session.php";
include "functions/functions.php";
include "functions/conf.php";
?>

			<?php
		if (isset($_GET['id']) ) {			
			$user_id = $_GET['id'];
			
			$query_select = "SELECT id_user FROM tbl_users WHERE id_user=".$user_id." AND active_user=1";
		
			//$result_select = mysql_query($query_select) or die(mysql_error());
			$result_select = $connection->query($query_select);
			
			$i_select=0;
			//while($row = mysql_fetch_array($result_select)){
			while($row = $result_select->fetch_array()){
				$i_select++;
			}
			
			if ($i_select>0)
			{
				//Deactivate
				$query_deactivate = "UPDATE tbl_users SET active_user=0 WHERE id_user=".$user_id;
				$result_deactivate = $connection->query($query_deactivate);
				//if(mysql_query($query_deactivate))
				if($result_deactivate)
					echo json_encode(array('success'=>TRUE,'message'=>"Deactivate"));
			}
			else if($i_select<=0)
			{
				//Activate
				$query_activate = "UPDATE tbl_users SET active_user=1 WHERE id_user=".$user_id;
				$result_activate = $connection->query($query_activate);
				//if(mysql_query($query_activate))
				if($result_activate)
					echo json_encode(array('success'=>TRUE,'message'=>"Activate"));
			}
		}
?>

