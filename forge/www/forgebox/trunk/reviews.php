<?php include "header.php"; ?>

	<div class="container"> 
	<h1>
		<a href="index.php">
			<i class="icon-arrow-left-3 fg-darker smaller"></i>
		</a>
		Dashboard
	</h1>
		<h1>Reviews</h1><br />

		<table id="table1" class="striped"></table>
		
		<script>
		
		    var table, table_data;
     
			table_data = [
				{action:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>&nbsp;",invdate:"2007-04-02",name:"Application test",description:"Description note for this application is bla description note for this application is bla description note for this application is bla description note for this application is bla",rate:"<div class=\"rating small \"  data-role=\"rating\" data-static=\"false\" data-score=\"3\" data-stars=\"5\" data-show-score=\"true\" data-score-hint=\"Value: \"></div>"},
				{action:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>&nbsp;",invdate:"2007-10-02",name:"Application test2",description:"Description note for this application is bla description note for this application is bla description note for this application is bla description note for this application is bla",rate:"<div class=\"rating small \"  data-role=\"rating\" data-static=\"false\" data-score=\"3\" data-stars=\"5\" data-show-score=\"true\" data-score-hint=\"Value: \"></div>"},
				{action:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>&nbsp;",invdate:"2007-09-01",name:"Application test3",description:"Description note for this application is bla description note for this application is bla description note for this application is bla description note for this application is bla",rate:"<div class=\"rating small \"  data-role=\"rating\" data-static=\"false\" data-score=\"3\" data-stars=\"5\" data-show-score=\"true\" data-score-hint=\"Value: \"></div>"},
				{action:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>&nbsp;",invdate:"2007-10-04",name:"Application test",description:"Description note for this application is bla description note for this application is bla description note for this application is bla description note for this application is bla",rate:"<div class=\"rating small \"  data-role=\"rating\" data-static=\"false\" data-score=\"3\" data-stars=\"5\" data-show-score=\"true\" data-score-hint=\"Value: \"></div>"},
				{action:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>&nbsp;",invdate:"2007-10-05",name:"Application test2",description:"Description note for this application is bla description note for this application is bla description note for this application is bla description note for this application is bla",rate:"<div class=\"rating small \"  data-role=\"rating\" data-static=\"false\" data-score=\"3\" data-stars=\"5\" data-show-score=\"true\" data-score-hint=\"Value: \"></div>"},
				{action:"<a href=\"#\"><i class=\"icon-checkbox\"></i></a>&nbsp;",invdate:"2007-09-06",name:"Application test3",description:"Description note for this application is bla description note for this application is bla description note for this application is bla description note for this application is bla",rate:"<div class=\"rating small \"  data-role=\"rating\" data-static=\"false\" data-score=\"3\" data-stars=\"5\" data-show-score=\"true\" data-score-hint=\"Value: \"></div>"},
				{action:"<a href=\"#\"><i class=\"icon-checkbox-unchecked\"></i></a>&nbsp;",invdate:"-",name:"-",description:"-",rate:"<a href=\"#\">Review now</a>"}
			];
     
			$(function(){
				table = $("#table1").tablecontrol({
					cls: 'table hovered border myClass',
					colModel: [
					{field: 'action', caption: 'Action', width: 60, sortable: false, cls: 'text-center', hcls: ""},
					{field: 'invdate', caption: 'Date', width: 80, sortable: false, cls: 'text-center', hcls: ""},
					{field: 'name', caption: 'Name', width: 100, sortable: false, cls: 'text-left', hcls: "text-left"},
					{field: 'description', caption: 'Description', width: 250, sortable: false, cls: 'text-left', hcls: "text-left"},					
					{field: 'rate', caption: 'Rate', width: 80, sortable: false, cls: 'text-left', hcls: "text-left"}					
					],
					 
					data: table_data
				});
			});
		
		</script>
		
	</div>
	
	
  
<?php include "footer.php"; ?>