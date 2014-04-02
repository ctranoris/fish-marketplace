<?php
include "functions/session.php";
include "functions/functions.php";
include "functions/conf.php";

if (isset($_GET['id']) ) {			
		$cat_id = $_GET['id'];
		$active_cat = $_GET['cat_actv'];
						
		if ($active_cat==1)
		{
			//Deactivate
			$query_deactivate = "UPDATE tbl_category_widget SET active_category_widget=0 WHERE id_category_widget=".$cat_id;
			$result_deactivate = $connection->query($query_deactivate);
			if($result_deactivate)
			echo json_encode(array('success'=>TRUE,'message'=>"Deactivate"));
		}
		else if($active_cat==0)
		{
			//Activate
			$query_activate = "UPDATE tbl_category_widget SET active_category_widget=1 WHERE id_category_widget=".$cat_id;
			$result_activate = $connection->query($query_activate);
			if($result_activate)
				echo json_encode(array('success'=>TRUE,'message'=>"Activate"));
		}
	}

?>