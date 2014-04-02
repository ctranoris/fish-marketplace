<?php include "header.php"; ?>

	<div class="container"> 
	<h1>
		<a href="index.php">
			<i class="icon-arrow-left-3 fg-darker smaller"></i>
		</a>
		Dashboard
	</h1>
	
		<h2>Interactive Courses</h2><br />
		
		<div class="tile bg-dark" onclick="window.location='design_app_logic_view.php';">
			<div class="tile-content icon">
				<i class="icon-new"></i>
			</div>
			<div class="tile-status">
				<span class="name">Create Interactive Course</span>
			</div>
		</div>		
		
		<table id="table1" class="striped"></table>
		
		<script>
		
		    var table, table_data;
     
			table_data = [
				{action:"<a href=\"#\"><i class=\"icon-play\" title=\"Run\"></i></a>&nbsp;<a href=\"#\"><i class=\"icon-pencil\" title=\"Edit\"></i></a>&nbsp;<a href=\"#\"><i class=\"icon-link\" title=\"Copy Public Link\"></i></a>&nbsp;<a href=\"publish.php\"><i class=\"icon-broadcast\" title=\"Publish\"></i></a>&nbsp;<a href=\"#\"><i class=\"icon-cancel\" title=\"Delete\"></i></a>", invdate:"2014-03-02",name:"Lab Course 1: Congestion Control Theory",	note:"Concepts and technology of TCP congestion control and TCP congestion controls algorithms",edit_logic:"<a href=\"design_app_logic_view.php\"><i class=\"icon-pencil\"></i></a>", edit_layout:"<a href=\"design_app_layout_view.php\"><i class=\"icon-eye-2\"></i></a>"},
				{action:"<a href=\"#\"><i class=\"icon-play\" title=\"Run\"></i></a>&nbsp;<a href=\"#\"><i class=\"icon-pencil\" title=\"Edit\"></i></a>&nbsp;<a href=\"#\"><i class=\"icon-link\" title=\"Copy Public Link\"></i></a>&nbsp;<a href=\"publish.php\"><i class=\"icon-broadcast\" title=\"Publish\"></i></a>&nbsp;<a href=\"#\"><i class=\"icon-cancel\" title=\"Delete\"></i></a>",invdate:"2014-03-02",name:"Lab Course 2: Congestion Control Introduction",note:"Identify the need for TCP congestion control in today’s networks. Examine the behavior of networks and network protocols in abnormal network conditions.Perform theoretical analysis for the TCP behavior and evaluate the expected results in real experiments.",edit_logic:"<a href=\"design_app_logic_view.php\"><i class=\"icon-pencil\"></i></a>",edit_layout:"<a href=\"design_app_layout_view.php\"><i class=\"icon-eye-2\"></i></a>"},
			];
     
			$(function(){
				table = $("#table1").tablecontrol({
					cls: 'table hovered border myClass',
					colModel: [
					{field: 'action', caption: 'Action', width: 130, sortable: false, cls: 'text-center', hcls: "text-left"},					
					{field: 'name', caption: 'Name', width: 350, sortable: false, cls: 'text-left', hcls: "text-left"},
					{field: 'note', caption: 'Description', width: '', sortable: false, cls: 'text-left', hcls: "text-left"},
					{field: 'invdate', caption: 'Date', width: 120, sortable: false, cls: 'text-left', hcls: "text-left"}
					],
					 
					data: table_data
				});
			});
		
		</script>
		
	</div>
	
	
  
<?php include "footer.php"; ?>
