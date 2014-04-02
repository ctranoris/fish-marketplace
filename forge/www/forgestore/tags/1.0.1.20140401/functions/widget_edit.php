<?php
	include "conf.php";	
		
	if(empty($_POST['title']) || empty($_POST['author']) || empty($_POST['url']) || empty($_POST['sdescription']) || empty($_POST['description']) || empty($_POST["cat"]) )
	{	
		die(msg(0,"All the fields are required!"));
	}	
	
	if(isset($_POST['edit']))
	{
		if($_POST['edit']==0)
		{
			//insert
			$query_edit_widget = "INSERT INTO tbl_widget_meta_data (url_widget_meta_data, title_widget_meta_data, author_widget_meta_data, sdescription_widget_meta_data, description_widget_meta_data,simage_widget_meta_data,limage_widget_meta_data,	active_widget_meta_data) VALUES ('".$_POST['url']."','".$_POST['title']."','".$_POST['author']."','".$_POST['sdescription']."','".$_POST['description']."','default.png','default.png',1)";
			//$result_edit_widget = mysql_query($query_edit_widget);
			$result_edit_widget = $connection->query($query_edit_widget);
			
			//$id_item = mysql_insert_id();
			$id_item = $connection->insert_id;
			
	
			if(isset($_POST["cat"]))
			{			
				for($i=0;$i<count($_POST["cat"]);$i++)
				{
					$query_edit_widget_cat = "INSERT INTO tbl_widget_match_with_category (id_category_widget, id_widget_meta_data) VALUES (".$_POST["cat"][$i].",".$id_item.")";
					//$result_edit_widget_cat = mysql_query($query_edit_widget_cat);
					$result_edit_widget_cat = $connection->query($query_edit_widget_cat);
				}				
			}
	
			if($result_edit_widget_cat)
			{
				die(msg($id_item,"Your widget is updated!"));
			}
			else
			{
				die(msg(0,"Your widget is not saved!<br />Try again!"));
			}
		}
		else if($_POST['edit']>0)
		{
			
			// First of all, let's begin a transaction
			//mysql_query("BEGIN");

			// A set of queries; if one fails, an exception should be thrown
			$string_tbl_widget_meta_data="UPDATE tbl_widget_meta_data SET url_widget_meta_data='".$_POST['url']."', title_widget_meta_data='".$_POST['title']."', author_widget_meta_data='".$_POST['author']."', sdescription_widget_meta_data='".$_POST['sdescription']."', description_widget_meta_data='".$_POST['description']."' WHERE id_widget_meta_data=".$_POST['edit'];
			//$objQuery1 = mysql_query($string_tbl_widget_meta_data);
			$objQuery1 = $connection->query($string_tbl_widget_meta_data);
					
			$string_delete_tbl_widget_match_with_category="DELETE FROM tbl_widget_match_with_category WHERE id_widget_meta_data=".$_POST['edit'];					
			//$objQuery2 = mysql_query($string_delete_tbl_widget_match_with_category);
			$objQuery2 = $connection->query($string_delete_tbl_widget_match_with_category);
			$query_edit_widget_cat = "INSERT INTO tbl_widget_match_with_category (id_category_widget, id_widget_meta_data) VALUES ";
			for($i=0;$i<count($_POST["cat"]);$i++)
			{
				$query_edit_widget_cat .= "(".$_POST["cat"][$i].",".$_POST['edit'].")";
				if($i<count($_POST["cat"])-1)
				{
					$query_edit_widget_cat .= ",";
				}
			}
					
			//$objQuery3 = mysql_query($query_edit_widget_cat);
			$objQuery3 = $connection->query($query_edit_widget_cat);

			// If we arrive here, it means that no exception was thrown
			// i.e. no query has failed, and we can commit the transaction
			if(($objQuery1) and ($objQuery2) and ($objQuery3))
			{
				//mysql_query("COMMIT");
				die(msg(0,"Your widget is updated!"));
			}
			else
			{
				//mysql_query("ROLLBACK");
				die(msg(0,"Your widget is not updated!<br />Try again!"));
			}
		}
	}	
	
	function msg($status,$txt)
	{
		return '{"status":'.$status.',"txt":"'.$txt.'"}';
	}
		
?>