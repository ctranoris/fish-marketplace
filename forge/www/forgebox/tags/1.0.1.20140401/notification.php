<?php
include 'header.php'; 
?>
<div class="container">
	<h1>
		<a href="index.php">
			<i class="icon-arrow-left-3 fg-darker smaller"></i>
		</a>
		Dashboard
	</h1>
	
	<h2>Notifications</h2>
	<table id="table1" class="striped"></table>
	
	<script>
		
	    var table, table_data;
     
		table_data = [
			{action:"<a href=\"#\"><i class=\"icon-info\"></i></a>",invdate:"2014-01-11",name:"Application test",note:"note"},
			{action:"<a href=\"#\"><i class=\"icon-info\"></i></a>",invdate:"2014-01-09",name:"Application test2",note:"note2"},
			{action:"<a href=\"#\"><i class=\"icon-info\"></i></a>",invdate:"2014-01-05",name:"Application test3",note:"note3"},
			{action:"<a href=\"#\"><i class=\"icon-info\"></i></a>",invdate:"2013-01-04",name:"Application test",note:"note"},
			{action:"<a href=\"#\"><i class=\"icon-info\"></i></a>",invdate:"2013-12-07",name:"Application test2",note:"note2"},
			{action:"<a href=\"#\"><i class=\"icon-info\"></i></a>",invdate:"2013-12-06",name:"Application test3",note:"note3"},
			{action:"<a href=\"#\"><i class=\"icon-info\"></i></a>",invdate:"2013-11-04",name:"Application test",note:"note"},
			{action:"<a href=\"#\"><i class=\"icon-checkmark\"></i></a>",invdate:"2014-10-03",name:"Application test2",note:"note2"},
			{action:"<a href=\"#\"><i class=\"icon-checkmark\"></i></a>",invdate:"2013-09-01",name:"Application test3",note:"note3"}
		];
     
		$(function(){
			table = $("#table1").tablecontrol({
				cls: 'table hovered border myClass',
				colModel: [
				{field: 'action', caption: 'Action', width: 30, sortable: false, cls: 'text-center', hcls: ""},
				{field: 'name', caption: 'Description', width: '', sortable: false, cls: 'text-left', hcls: "text-left"},
				{field: 'note', caption: 'Notes', width: 50, sortable: false, cls: 'span1', hcls: ""},
				{field: 'invdate', caption: 'Date', width: 50, sortable: true, cls: 'text-center', hcls: ""}
				],
					 
				data: table_data
			});
		});
		
	</script>
</div>

<?php			
include 'footer.php'; 
?>