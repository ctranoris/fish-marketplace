<?php include "header.php"; ?>

<div class="container"> 
		<h2>Manage Marketplace items</h2><br />
		
		<div class="tab-control" data-role="tab-control">
			<ul class="tabs">
				<li class="active"><a href="#_page_1"><img src="images/widget.ico"/>&nbsp;&nbsp;Widgets</a></li>
				<li><a href="#_page_3"><img src="images/forgebox_services.ico"/>&nbsp;&nbsp;FORGEBox Services</a></li>
				<li><a href="#_page_2"><img src="images/shared_course.ico"/>&nbsp;&nbsp;Shared Cources</a></li>
				<!-- <li class="place-right"><a href="#_page_5"><i class="icon-heart"></i></a></li>
				<li class="place-right"><a href="#_page_4"><i class="icon-cog"></i></a></li> -->
			</ul>

			<div class="frames">
				<div class="frame" id="_page_1">
					<p>
						<div class="tile bg-dark" onclick="window.location='create_item_widget.php';">
							<div class="tile-content icon">
								<i class="icon-new"></i>
							</div>
							<div class="tile-status">
								<span class="name">Create Widget</span>
							</div>
						</div>
						<div class="tile bg-dark" onclick="window.location='list_widget_category.php';">
						<div class="tile-content icon">
							<i class="icon-list"></i>
						</div>
						<div class="tile-status">
							<span class="name">Categories</span>
						</div>
					</div>
					
						<table id="table1" class="striped"></table>
		
						<script>
						
							var table, table_data;
					 
							table_data = [
							<?php
							
								$query_select_categories = "SELECT id_category_widget, name_category_widget FROM tbl_category_widget WHERE active_category_widget=1";
								//$result_select_categories = mysql_query($query_select_categories);
								$result_select_categories = $connection->query($query_select_categories);
								$count_cat =0;
								//while($row = mysql_fetch_array($result_select_categories)){
								while($row = $result_select_categories->fetch_array()){
									$id_category_widget[$count_cat] = $row[0];
									$name_category_widget[$count_cat] = $row[1];
									$count_cat++;
								}
								$query_select_item = "SELECT id_widget_meta_data, title_widget_meta_data, active_widget_meta_data FROM tbl_widget_meta_data ";//active_category_widget=1 AND
								//$result_select_item = mysql_query($query_select_item);
								$result_select_item = $connection->query($query_select_item);
								//$num_rows = mysql_num_rows($result_select_item);
								$num_rows = $result_select_item->num_rows;
								$count_items=0;
								//while($row1 = mysql_fetch_array($result_select_item)){
								while($row1 = $result_select_item->fetch_array()){
									$query_select_cat = "SELECT tbl_category_widget.name_category_widget FROM tbl_widget_match_with_category INNER JOIN tbl_category_widget ON tbl_widget_match_with_category.id_category_widget = tbl_category_widget.id_category_widget INNER JOIN tbl_widget_meta_data ON tbl_widget_match_with_category.id_widget_meta_data = tbl_widget_meta_data.id_widget_meta_data WHERE tbl_widget_meta_data.id_widget_meta_data =".$row1[0];//active_category_widget=1 AND
									//$result_select_cat = mysql_query($query_select_cat);
									$result_select_cat = $connection->query($query_select_cat);
									
									$category_items="";
									
									//while($row2 = mysql_fetch_array($result_select_cat)){
									while($row2 = $result_select_cat->fetch_array()){
										$category_items .=$row2[0]."<br />";									
									}
									
									if($count_items<$num_rows)
									{
										echo '{name:"'.$row1[1].'",edit:"<a href=\"create_item_widget.php?widgetid='.$row1[0].'\"><i class=\"icon-pencil\"></i></a>",category:"'.$category_items.'",active:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>"},';
									}
									else
									{
										echo '{name:"'.$row1[1].'",edit:"<a href=\"create_item_widget.php?widgetid='.$row1[0].'\"><i class=\"icon-pencil\"></i></a>",category:"'.$category_items.'",active:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>"}';
									}
									$count_items++;
								}
								
								
							?>	
								/*{name:"Widget 7",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>"}*/
								/*{name:"Widget 1",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>"},
								{name:"Widget 2",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>"},
								{name:"Widget 3",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>"},
								{name:"Widget 4",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>"},
								{name:"Widget 5",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>"},
								{name:"Widget 6",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>"},
								{name:"Widget 7",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>"}								*/
							];
					 
							$(function(){
								table = $("#table1").tablecontrol({
									cls: 'table hovered border myClass',
									colModel: [
									{field: 'name', caption: 'Name', width: '', sortable: false, cls: 'text-left', hcls: "text-left"},
									{field: 'edit', caption: 'Edit', width: 60, sortable: false, cls: 'text-center', hcls: "text-center"},										
									{field: 'category', caption: 'Category', width: 250, sortable: false, cls: 'text-center', hcls: ""},
									{field: 'active', caption: 'Active', width: 60, sortable: false, cls: 'text-center', hcls: ""}
									],
									 
									data: table_data
								});
							});
						
						</script>
					
					</p>
				</div>
				<div class="frame" id="_page_2">
					<p>
					<div class="tile bg-dark" onclick="window.location='create_item_category.php';">
						<div class="tile-content icon">
							<i class="icon-new"></i>
						</div>
						<div class="tile-status">
							<span class="name">Create Application</span>
						</div>
					</div>
					<div class="tile bg-dark" onclick="window.location='create_item_category.php';">
						<div class="tile-content icon">
							<i class="icon-list"></i>
						</div>
						<div class="tile-status">
							<span class="name">Categories</span>
						</div>
					</div>
					
					<table id="table2" class="striped"></table>
		
						<script>
						
							var table1, table_data1;
					 
							table_data1 = [
								{name:"Application 1",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>"},
								{name:"Application 2",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>"},
								{name:"Application 3",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>"},
								{name:"Application 4",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>"},
								{name:"Application 5",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>"},
								{name:"Application 6",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>"},
								{name:"Application 7",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>"},
								{name:"Application 8",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>"},
								{name:"Application 9",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>"}
							];
					 
							$(function(){
								table1 = $("#table2").tablecontrol({
									cls: 'table hovered border myClass',
									colModel: [
									{field: 'name', caption: 'Name', width: '', sortable: false, cls: 'text-left', hcls: "text-left"},
									{field: 'edit', caption: 'Edit', width: 60, sortable: false, cls: 'text-center', hcls: "text-center"},										
									{field: 'category', caption: 'Category', width: 250, sortable: false, cls: 'text-center', hcls: ""},									
									{field: 'active', caption: 'Active', width: 60, sortable: false, cls: 'text-center', hcls: ""}
									],
									 
									data: table_data1
								});
							});
						
						</script>
					
					</p>
				</div>
				<div class="frame" id="_page_3">
					<p>
					<div class="tile bg-dark" onclick="window.location='create_item_category.php';">
						<div class="tile-content icon">
							<i class="icon-new"></i>
						</div>
						<div class="tile-status">
							<span class="name">Create Services</span>
						</div>
					</div>					
					<div class="tile bg-dark" onclick="window.location='create_item_category.php';">
						<div class="tile-content icon">
							<i class="icon-list"></i>
						</div>
						<div class="tile-status">
							<span class="name">Categories</span>
						</div>
					</div>	
					
					<table id="table3" class="striped"></table>
		
						<script>
						
							var table2, table_data2;
					 
							table_data2 = [
								{name:"Services 1",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>"},
								{name:"Services 2",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>"},
								{name:"Services 3",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>"},
								{name:"Services 4",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>"},
								{name:"Services 5",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>"},
								{name:"Services 6",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",category:"category 1<br/>category 2",active:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>"}
							];
					 
							$(function(){
								table2 = $("#table3").tablecontrol({
									cls: 'table hovered border myClass',
									colModel: [
									{field: 'name', caption: 'Name', width: '', sortable: false, cls: 'text-left', hcls: "text-left"},
									{field: 'edit', caption: 'Edit', width: 60, sortable: false, cls: 'text-center', hcls: "text-center"},										
									{field: 'category', caption: 'Category', width: 250, sortable: false, cls: 'text-center', hcls: ""},									
									{field: 'active', caption: 'Active', width: 60, sortable: false, cls: 'text-center', hcls: ""}
									],
									 
									data: table_data2
								});
							});
						
						</script>
					
					</p>
				</div>
				<!-- <div class="frame" id="_page_4">
					<p>This tab placed right</p>
				</div>
				<div class="frame" id="_page_5">
					<p>This tab also placed right</p>
				</div> -->
			</div>

		</div>
					
				

<?php include "footer.php"; ?>
