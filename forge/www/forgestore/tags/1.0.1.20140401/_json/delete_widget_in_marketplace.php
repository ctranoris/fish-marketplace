 <?php
		include "../functions/conf.php";	 
		
		$query_delete_widget="DELETE FROM tbl_install_widget WHERE id_users=".$_POST['userid']." AND id_widget_meta_data=".$_POST['widgetid'];
		$delete_widget=$connection->query($query_delete_widget);
		if($delete_widget)
		{
			die(msg(1,"Installed"));
		}
		else
		{
			die(msg(0,"error"));
		}
		
		
		
		function msg($status,$txt)
		{
			return '{"status":'.$status.',"txt":"'.$txt.'"}';
		}
	
?>
