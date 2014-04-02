<?php
		error_reporting( E_ALL );
		ini_set('display_errors', 1);
	
		include "../functions/conf.php";	 
		
		//$_POST["userid"]=1;//$_GET["userid"];
		
		$query_widget="SELECT tbl_widget_meta_data.id_widget_meta_data, tbl_widget_meta_data.url_widget_meta_data, tbl_widget_meta_data.title_widget_meta_data, tbl_widget_meta_data.author_widget_meta_data, tbl_widget_meta_data.sdescription_widget_meta_data FROM tbl_widget_meta_data INNER JOIN tbl_install_widget ON tbl_install_widget.id_widget_meta_data = tbl_widget_meta_data.id_widget_meta_data WHERE tbl_install_widget.id_users=".$_POST["userid"];
		//$result_widget = mysql_query($query_widget);
		$result_widget = $connection->query($query_widget);
		
		$lib='{"All": [';
		
		
		//while($row3 = mysql_fetch_array($result_widget))
		while($row3 = $result_widget->fetch_array())
		{	
			$lib.='{"src":"'.$row3[1].'","info":{"title":"'.$row3[2].'","Clock":"'.$row3[3].'","description":"'.$row3[4].'"}},';
		}
		$lib.=']';
		
		$query_category="SELECT id_category_widget,name_category_widget FROM tbl_category_widget";
		//$result_category = mysql_query($query_category);
		$result_category = $connection->query($query_category);
		
		$count_cat=0;
		$count_cat_widget=0;
		
		//while($row2 = mysql_fetch_array($result_category))
		while($row2 =$result_category-> fetch_array())
		{			
			$query_category_widget="SELECT tbl_category_widget.name_category_widget, tbl_widget_meta_data.url_widget_meta_data, tbl_widget_meta_data.title_widget_meta_data, tbl_widget_meta_data.author_widget_meta_data, tbl_widget_meta_data.sdescription_widget_meta_data FROM tbl_widget_match_with_category Inner Join tbl_widget_meta_data ON tbl_widget_meta_data.id_widget_meta_data = tbl_widget_match_with_category.id_widget_meta_data Inner Join tbl_category_widget ON tbl_category_widget.id_category_widget = tbl_widget_match_with_category.id_category_widget Inner Join tbl_install_widget ON tbl_install_widget.id_widget_meta_data = tbl_widget_match_with_category.id_widget_meta_data WHERE tbl_category_widget.id_category_widget = ".$row2[0]." AND tbl_install_widget.id_users = ".$_POST["userid"];
			//$result_category_widget = mysql_query($query_category_widget);
			$result_category_widget = $connection->query($query_category_widget);
			//$num_rows_category_widget = mysql_num_rows($result_category_widget);
			$num_rows_category_widget =$result_category_widget->num_rows;
			
			if($num_rows_category_widget>0)
			{
				if($num_rows_category_widget>0)
				{
					$lib.=',';
					$lib.='"'.$row2[1].'":[';
					
					//while($row = mysql_fetch_array($result_category_widget))
					while($row = $result_category_widget->fetch_array())
					{	
						
						$lib.='{"src":"'.$row[1].'","info":{"title":"'.$row[2].'","author":"'.$row[3].'","description":"'.$row[4].'"}}';						
						
						$lib.=',';
						
						$count_cat_widget++;
					}
					$lib.=']';
					$count_cat++;
				}
			}
		}
		
		$lib.='}';
		$lib1=str_replace ( ',]' , ']',$lib );
		
		die(msg("0", $lib1));
		alert($lib1);
		function msg($status,$txt1)
		{
			$result['status'] = $status;
			$result['payload'] = $txt1;
			
			return json_encode($txt1);
			//return $txt1;
			
		}		
?>
