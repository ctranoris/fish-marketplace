<?php
		include "../functions/conf.php";	 
		
		$query_widget="SELECT id_widget_meta_data, url_widget_meta_data, title_widget_meta_data, sdescription_widget_meta_data, limage_widget_meta_data FROM tbl_widget_meta_data WHERE active_widget_meta_data =1";
		
		//$result_widget = mysql_query($query_widget);
		$result_widget = $connection->query($query_widget);
		
		//$num_rows_widget = mysql_num_rows($result_widget);
		$num_rows_widget = $result_widget->num_rows;
		
		$count_widget=0;
		
		$json_result_widget .= '{\"widget\":[';
		//while($row2 = mysql_fetch_array($result_widget))
		while($row2 = $result_widget->fetch_array())
		{			
			$widget_category="";
			$query_widget_category = "SELECT id_category_widget FROM tbl_widget_match_with_category WHERE id_widget_meta_data =".$row2[0];
			//$result_widget_category = mysql_query($query_widget_category);
			$result_widget_category = $connection->query($query_widget_category);
			
			//while($row3 = mysql_fetch_array($result_widget_category))
			while($row3 = $result_widget_category->fetch_array())
			{	
				$widget_category .= " category".$row3[0];
			}
			
			$widget_installed="";
			$query_widget_installed = "SELECT id_widget_meta_data FROM tbl_install_widget WHERE id_users =".$_POST["userid"]." AND id_widget_meta_data=".$row2[0];
			
			//$result_widget_installed = mysql_query($query_widget_installed);
			$result_widget_installed = $connection->query($query_widget_installed);
			
			//while($row_installed = mysql_fetch_array($result_widget_installed))
			
			while($row_installed = $result_widget_installed->fetch_array()){	
				$widget_installed = $row_installed[0]."-".$_POST["userid"];
			}
			
			$json_result_widget .= '{\"id\":\"'.$row2[0].'\",\"url\":\"'.$row2[1].'\",\"title\":\"'.$row2[2].'\",\"sdescription\":\"'.$row2[3].'\",\"limage\":\"'.$row2[4].'\",\"categories\":\"'.$widget_category.'\",\"widget_installed\":\"'.$widget_installed.'\"}';
		
			if($num_rows_widget-1>$count_widget){
				$json_result_widget .=",";
				}
			$count_widget++;
		}
		
		$json_result_widget .= ']}';
		
		die(msg(0,$json_result_widget));
		
		function msg($status,$txt)
		{
			return '{"status":'.$status.',"txt":"'.$txt.'"}';
		}
	
?>
