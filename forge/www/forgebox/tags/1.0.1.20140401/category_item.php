<?php include "header.php"; ?>

<div class="container"> 
<h1>
		<a href="index.php">
			<i class="icon-arrow-left-3 fg-darker smaller"></i>
		</a>
		Dashboard
	</h1>
		<h2>Item Categories</h2><br />
		
		<div class="tile bg-dark" onclick="window.location='create_item_category.php';">
			<div class="tile-content icon">
				<i class="icon-new"></i>
			</div>
			<div class="tile-status">
				<span class="name">Create Widget</span>
			</div>
		</div>		
		
		<table id="table1" class="striped"></table>
		
		<script>
		
		    var table, table_data;
     
			table_data = [
				{name:"Application test",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",widget:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>",app:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>",active:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>"},
				{name:"Application test",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",widget:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>",app:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>",active:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>"},
				{name:"Application test",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",widget:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>",app:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>",active:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>"},
				{name:"Application test",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",widget:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>",app:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>",active:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>"},
				{name:"Application test",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",widget:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>",app:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>",active:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>"},
				{name:"Application test",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",widget:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>",app:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>",active:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>"},
				{name:"Application test",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",widget:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>",app:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>",active:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>"},
				{name:"Application test",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",widget:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>",app:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>",active:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>"},
				{name:"Application test",edit:"<a href=\"#\"><i class=\"icon-pencil\"></i></a>",widget:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>",app:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>",active:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>"}
			];
     
			$(function(){
				table = $("#table1").tablecontrol({
					cls: 'table hovered border myClass',
					colModel: [
					{field: 'name', caption: 'Name', width: '', sortable: false, cls: 'text-left', hcls: "text-left"},
					{field: 'edit', caption: 'Edit', width: 60, sortable: false, cls: 'text-center', hcls: "text-center"},										
					{field: 'widget', caption: 'Widget', width: 60, sortable: false, cls: 'text-center', hcls: ""},
					{field: 'app', caption: 'Application', width: 60, sortable: false, cls: 'text-center', hcls: ""},
					{field: 'active', caption: 'Active', width: 60, sortable: false, cls: 'text-center', hcls: ""}
					],
					 
					data: table_data
				});
			});
		
		</script>
		
	</div>

<?php include "footer.php"; ?>