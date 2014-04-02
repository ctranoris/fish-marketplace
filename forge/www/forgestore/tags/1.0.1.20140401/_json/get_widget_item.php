 <?php
		include "../functions/conf.php";	 
		
		$query_widget_item = "SELECT url_widget_meta_data, title_widget_meta_data, author_widget_meta_data, description_widget_meta_data, limage_widget_meta_data, active_widget_meta_data FROM  tbl_widget_meta_data WHERE id_widget_meta_data =".$_POST["widget_id"];
		//$result_widget_item = mysql_query($query_widget_item);
		$result_widget_item = $connection->query($query_widget_item);
		//while($row = mysql_fetch_array($result_widget_item))
		while($row = $result_widget_item->fetch_array())
		{	
			$url_widget_meta_data = $row[0];
			$title_widget_meta_data = $row[1];
			$author_widget_meta_data = $row[2];
			$description_widget_meta_data = $row[3];
			$limage_widget_meta_data = $row[4];
			$active_widget_meta_data = $row[5];
			
			$json_result_widget .= '{\"widget\":[';
			$json_result_widget .= '{\"url\":\"'.$row[0].'\",\"title\":\"'.$row[1].'\",\"author\":\"'.$row[2].'\",\"description\":\"'.$row[3].'\",\"limage\":\"'.$row[4].'\",\"active\":\"'.$row[5].'\"}';
			$json_result_widget .= ']}';
		}	
		
		if(!empty($title_widget_meta_data))
		{
			die(msg(0,$json_result_widget));
		}
		else
		{
			die(msg(1,"error"));
		}
				
		function msg($status,$txt)
		{
			return '{"status":'.$status.',"txt":"'.$txt.'"}';
		}
	
?>
