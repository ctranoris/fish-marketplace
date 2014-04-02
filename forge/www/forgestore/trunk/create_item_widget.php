<?php include "header.php"; 
	
	if(isset($_GET["widgetid"]))
	{
		$query_select_item = "SELECT id_widget_meta_data, url_widget_meta_data, title_widget_meta_data, author_widget_meta_data, sdescription_widget_meta_data, description_widget_meta_data,simage_widget_meta_data,limage_widget_meta_data,	active_widget_meta_data FROM tbl_widget_meta_data WHERE id_widget_meta_data=".$_GET["widgetid"];
		
		//$result_select_item = mysql_query($query_select_item);
		$result_select_item = $connection->query($query_select_item);
		
		//while($row1 = mysql_fetch_array($result_select_item)){
		while($row1 = $result_select_item->fetch_array()){
			$id_widget_meta_data = $row1[0];
			$url_widget_meta_data = $row1[1];
			$title_widget_meta_data = $row1[2];
			$author_widget_meta_data = $row1[3];
			$sdescription_widget_meta_data = $row1[4];
			$description_widget_meta_data = $row1[5];
			$simage_widget_meta_data = $row1[6];
			$limage_widget_meta_data = $row1[7];
			$active_widget_meta_data = $row1[8];			
		}
	}
					
?>

<div class="container"> 
	<?php
	if(isset($_GET["widgetid"]))
	{
	?>
		<h2>Edit Widget</h2>
	<?php
	}
	else
	{
	?>
		<h2>Create Widget</h2>
	<?php
	}
	?>
	<br />
	
	<form id="regForm" name="widget" method="post" action="create_item_widget.php">
		<h6>Title<br /></h6>
		<div class="input-control text size6">
			<input type="text" placeholder="type widget title" value="<?php if(!empty($title_widget_meta_data)){echo $title_widget_meta_data;} ?>"  name="title"/>
		</div>
		<br />
		<h6><br />Author<br /></h6>
		<div class="input-control text size6">
			<input type="text" placeholder="type widget author"  value="<?php if(!empty($author_widget_meta_data)){echo $author_widget_meta_data;} ?>" name="author"/>
		</div>
		<br />
		<h6><br />URL<br /></h6>
		<div class="input-control text size6">
			<input type="text" placeholder="type widget url "  value="<?php if(!empty($url_widget_meta_data)){echo $url_widget_meta_data;} ?>" name="url"/>
		</div>
		<br />		
		<h6><br />Small description<br /></h6>
		<div class="input-control textarea size6" data-role="input-control" >
			<textarea placeholder="type widget small description " name="sdescription"><?php if(!empty($sdescription_widget_meta_data)){echo $sdescription_widget_meta_data;} ?></textarea>
		</div>
		<br />
		<h6><br />Full description<br /></h6>
		<div class="input-control textarea size6" data-role="input-control">
			<textarea placeholder="type widget description "  name="description"><?php if(!empty($description_widget_meta_data)){echo $description_widget_meta_data;} ?></textarea>
		</div>
		<br />
		<h6><br />Categories<br /></h6>
		<div class="input-control select size6" data-role="input-control" data-hint="Help|Press and Hold the control button and choose more than one categories." data-hint-position="right">
				<select name="cat[]" multiple="multiple">
				<?php
					$query_all_categories = "SELECT id_category_widget, name_category_widget FROM tbl_category_widget WHERE active_category_widget=1";
					//$result_all_categories = mysql_query($query_all_categories);
					$result_all_categories = $connection->query($query_all_categories);
					
					while($row = $result_all_categories->fetch_array()){
						$num_rows =0;
						$query_select_categories = "SELECT id_category_widget FROM tbl_widget_match_with_category WHERE id_widget_meta_data=".$id_widget_meta_data." AND id_category_widget=".$row[0];
						//$result_select_categories = mysql_query($query_select_categories);
						$result_select_categories = $connection->query($query_select_categories);
						
						//$num_rows = mysql_num_rows($result_select_categories);
						$num_rows = $result_select_categories->num_rows;
						
						if ($num_rows>0) 
						{						
							echo "<option selected value=\"".$row[0]."\">".$row[1]."</option>";
						}
						else
						{
							echo "<option value=\"".$row[0]."\" >".$row[1]."</option>";
						}
						
					}
				?>
			</select>
		</div>
		<br /><br />
		<div class="size6 text-right">
		<?php
		
			if(isset($_GET["widgetid"]))
			{
				$_edit=$_GET["widgetid"];
			}
			else
			{	
				$_edit=0;
			}			
		?>
		<input type="hidden" name="edit" value="<?php echo $_edit; ?>"/>
		<input type="submit" value="Submit">
		<input type="reset" value="Reset">
		</div>
	</form>
	
	<div id="error">
		&nbsp;
	</div>
			
	<script type="text/javascript">
	
		$(document).ready(function(){
			$('#regForm').submit(function(e) {						
				register();
				e.preventDefault();		
			});
		});
		
		function register()
		{
			hideshow('loading',1);
			error(0);	
			$.ajax({
				type: "POST",
				url: "functions/widget_edit.php",
				data: $('#regForm').serialize(),
				dataType: "json",
				success: function(msg){
					
					if(parseInt(msg.status)==0)
					{
						//error(1,msg.txt);
						window.location.href="category_item.php";
					}	
					else if(parseInt(msg.status)>0)
					{
						//window.location="create_item_widget.php?widgetid="+parseInt(msg.status);
						window.location.href="category_item.php";
					}
					hideshow('loading',0);					
				}
			});
		}
	
		function hideshow(el,act)
		{
			if(act) $('#'+el).css('visibility','visible');
			else $('#'+el).css('visibility','hidden');
		}

		function error(act,txt)
		{
			hideshow('error',act);
			if(txt) $('#error').html(txt);
		}
	</script>
</div>

<?php include "footer.php"; ?>