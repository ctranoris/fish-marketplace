<?php

	include "../functions/conf.php";
	//$_POST['userid']=1;
	$query_your_widget="SELECT tbl_widget_meta_data.title_widget_meta_data, tbl_widget_meta_data.author_widget_meta_data, tbl_widget_meta_data.sdescription_widget_meta_data, tbl_widget_meta_data.id_widget_meta_data FROM tbl_install_widget INNER JOIN tbl_widget_meta_data ON tbl_install_widget.id_widget_meta_data = tbl_widget_meta_data.id_widget_meta_data WHERE tbl_install_widget.id_users = ".$_POST['userid'];
	
	$result_your_widget = $connection->query($query_your_widget);
	
	$json_array='';
	
	//echo $query_your_widget;
	$count_widget=0;
	$json_array .='{"widgets":[';
	while($row = $result_your_widget->fetch_array())
	{
		if($count_widget>0)
		{
			$json_array .=',';
		}
		$json_array .= '{"id":"'.$row[3].'","author":"'.$row[1].'","name":"'.$row[0].'","note":"'.$row[2].'"}';
		$count_widget++;
	}
	$json_array.=']}';
	//echo json_encode($json_array);
	die(msg("0", json_encode($json_array)));
	//die(msg("0", $json_array));
	function msg($status,$txt1)
	{
		//return '{"status":'.$status.',"txt":'.$txt1.'}';
		//$txt = '['.$txt1.']';
		return '{"status":'.$status.',"txt":'.$txt1.'}';
	}

?>