 <?php
		include "../functions/conf.php";	 
		
		$query_insert_widget="INSERT INTO tbl_install_widget  ( id_users, id_widget_meta_data) VALUES (".$_POST['userid'].", ".$_POST['widgetid'].") ";
		$insert_widget = $connection->query($query_insert_widget);
		if($insert_widget)
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
