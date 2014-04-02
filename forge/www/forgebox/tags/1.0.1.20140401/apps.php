<?php include "header.php"; ?>

	<div class="container"> 
	<h1>
		<a href="index.php">
			<i class="icon-arrow-left-3 fg-darker smaller"></i>
		</a>
		Dashboard
	</h1>
		<h2>My Course</h2><br />
		
		<div class="tile bg-dark" onclick="window.location='design_app_logic_view.php';">
			<div class="tile-content icon">
				<i class="icon-new"></i>
			</div>
			<div class="tile-status">
				<span class="name">Create Course</span>
			</div>
		</div>		
		
		<table id="table1" class="striped"></table>
		
		<script>
		
		    var table, table_data;
     
			table_data = [
				{action:"<a href=\"#\"><i class=\"icon-power\" title=\"Run\"></i></a>&nbsp;<a href=\"publish.php\"><i class=\"icon-cube\" title=\"Publish\"></i></a>&nbsp;<a href=\"#\"><i class=\"icon-cancel\" title=\"Delete\"></i></a>",invdate:"2007-04-02",name:"Course test",note:"note",edit_logic:"<a href=\"design_app_logic_view.php\"><i class=\"icon-pencil\"></i></a>",edit_layout:"<a href=\"design_app_layout_view.php\"><i class=\"icon-eye-2\"></i></a>"},
				{action:"<a href=\"#\"><i class=\"icon-power\" title=\"Run\"></i></a>&nbsp;<a href=\"publish.php\"><i class=\"icon-cube\" title=\"Publish\"></i></a>&nbsp;<a href=\"#\"><i class=\"icon-cancel\" title=\"Delete\"></i></a>",invdate:"2007-10-02",name:"Course test2",note:"note2",edit_logic:"<a href=\"design_app_logic_view.php\"><i class=\"icon-pencil\"></i></a>",edit_layout:"<a href=\"design_app_layout_view.php\"><i class=\"icon-eye-2\"></i></a>"},
				{action:"<a href=\"#\"><i class=\"icon-power\" title=\"Run\"></i></a>&nbsp;<a href=\"publish.php\"><i class=\"icon-cube\" title=\"Publish\"></i></a>&nbsp;<a href=\"#\"><i class=\"icon-cancel\" title=\"Delete\"></i></a>",invdate:"2007-09-01",name:"Course test3",note:"note3",edit_logic:"<a href=\"design_app_logic_view.php\"><i class=\"icon-pencil\"></i></a>",edit_layout:"<a href=\"design_app_layout_view.php\"><i class=\"icon-eye-2\"></i></a>"},
				{action:"<a href=\"#\"><i class=\"icon-power\" title=\"Run\"></i></a>&nbsp;<a href=\"publish.php\"><i class=\"icon-cube\" title=\"Publish\"></i></a>&nbsp;<a href=\"#\"><i class=\"icon-cancel\" title=\"Delete\"></i></a>",invdate:"2007-10-04",name:"Course test",note:"note",edit_logic:"<a href=\"design_app_logic_view.php\"><i class=\"icon-pencil\"></i></a>",edit_layout:"<a href=\"design_app_layout_view.php\"><i class=\"icon-eye-2\"></i></a>"},
				{action:"<a href=\"#\"><i class=\"icon-power\" title=\"Run\"></i></a>&nbsp;<a href=\"publish.php\"><i class=\"icon-cube\" title=\"Publish\"></i></a>&nbsp;<a href=\"#\"><i class=\"icon-cancel\" title=\"Delete\"></i></a>",invdate:"2007-10-05",name:"Course test2",note:"note2",edit_logic:"<a href=\"design_app_logic_view.php\"><i class=\"icon-pencil\"></i></a>",edit_layout:"<a href=\"design_app_layout_view.php\"><i class=\"icon-eye-2\"></i></a>"},
				{action:"<a href=\"#\"><i class=\"icon-power\" title=\"Run\"></i></a>&nbsp;<a href=\"publish.php\"><i class=\"icon-cube\" title=\"Publish\"></i></a>&nbsp;<a href=\"#\"><i class=\"icon-cancel\" title=\"Delete\"></i></a>",invdate:"2007-09-06",name:"Course test3",note:"note3",edit_logic:"<a href=\"design_app_logic_view.php\"><i class=\"icon-pencil\"></i></a>",edit_layout:"<a href=\"design_app_layout_view.php\"><i class=\"icon-eye-2\"></i></a>"},
				{action:"<a href=\"#\"><i class=\"icon-power\" title=\"Run\"></i></a>&nbsp;<a href=\"publish.php\"><i class=\"icon-cube\" title=\"Publish\"></i></a>&nbsp;<a href=\"#\"><i class=\"icon-cancel\" title=\"Delete\"></i></a>",invdate:"2007-10-04",name:"Course test",note:"note",edit_logic:"<a href=\"design_app_logic_view.php\"><i class=\"icon-pencil\"></i></a>",edit_layout:"<a href=\"design_app_layout_view.php\"><i class=\"icon-eye-2\"></i></a>"},
				{action:"<a href=\"#\"><i class=\"icon-power\" title=\"Run\"></i></a>&nbsp;<a href=\"publish.php\"><i class=\"icon-cube\" title=\"Publish\"></i></a>&nbsp;<a href=\"#\"><i class=\"icon-cancel\" title=\"Delete\"></i></a>",invdate:"2007-10-03",name:"Course test2",note:"note2",edit_logic:"<a href=\"design_app_logic_view.php\"><i class=\"icon-pencil\"></i></a>",edit_layout:"<a href=\"design_app_layout_view.php\"><i class=\"icon-eye-2\"></i></a>"},
				{action:"<a href=\"#\"><i class=\"icon-power\" title=\"Run\"></i></a>&nbsp;<a href=\"publish.php\"><i class=\"icon-cube\" title=\"Publish\"></i></a>&nbsp;<a href=\"#\"><i class=\"icon-cancel\" title=\"Delete\"></i></a>",invdate:"2007-09-01",name:"Course test3",note:"note3",edit_logic:"<a href=\"design_app_logic_view.php\"><i class=\"icon-pencil\"></i></a>",edit_layout:"<a href=\"design_app_layout_view.php\"><i class=\"icon-eye-2\"></i></a>"}
			];
     
			$(function(){
				table = $("#table1").tablecontrol({
					cls: 'table hovered border myClass',
					colModel: [
					{field: 'action', caption: 'Action', width: 100, sortable: false, cls: 'text-center', hcls: ""},					
					{field: 'name', caption: 'Name', width: '', sortable: false, cls: 'text-left', hcls: "text-left"},
					{field: 'edit_logic', caption: 'Design Logic', width: 120, sortable: false, cls: 'text-center', hcls: "text-right"},					
					{field: 'edit_layout', caption: 'Design Layout', width: 120, sortable: false, cls: 'text-center', hcls: "text-right"},
					{field: 'note', caption: 'Notes', width: 250, sortable: false, cls: 'text-center', hcls: ""},
					{field: 'invdate', caption: 'Date', width: 120, sortable: false, cls: 'text-center', hcls: ""}
					],
					 
					data: table_data
				});
			});
		
		</script>
		
	</div>
	
	
  
<?php include "footer.php"; ?>