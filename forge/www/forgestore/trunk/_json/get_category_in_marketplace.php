<?php
		include "../functions/conf.php";	 
		
		$query_category="SELECT tbl_category_widget.id_category_widget, tbl_category_widget.name_category_widget, COUNT( tbl_widget_match_with_category.id_category_widget ) FROM tbl_widget_match_with_category RIGHT JOIN tbl_category_widget ON tbl_category_widget.id_category_widget = tbl_widget_match_with_category.id_category_widget RIGHT JOIN  tbl_widget_meta_data ON  tbl_widget_meta_data.id_widget_meta_data = tbl_widget_match_with_category.id_widget_meta_data WHERE tbl_widget_meta_data.active_widget_meta_data = 1 GROUP BY tbl_category_widget.id_category_widget ORDER BY tbl_category_widget.id_category_widget";
		//$result_category = mysql_query($query_category);
		$result_category = $connection->query($query_category);
		
		//$num_rows_category = mysql_num_rows($result_category);
		$num_rows_category = $result_category->num_rows;
		
		$count_category=0;
		
		$json_result_category .= '{\"category\":[';
		while($row2 = $result_category->fetch_array())
		{			
			$json_result_category .= '{\"id\":\"'.$row2[0].'\",\"name\":\"'.$row2[1].'\",\"count_cat\":\"'.$row2[2].'\"}';
		
			if($num_rows_category-1>$count_category){
				$json_result_category .=",";
				}
			$count_category++;
		}
		
		$json_result_category .= ']}';
		
		die(msg(0,$json_result_category));
 
		
		function msg($status,$txt)
		{
			return '{"status":'.$status.',"txt":"'.$txt.'"}';
			
		}
	
?>
