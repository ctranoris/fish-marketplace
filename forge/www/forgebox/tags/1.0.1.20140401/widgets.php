<?php include "header.php"; ?>

	<div class="container"> 
	<h1>
		<a href="index.php">
			<i class="icon-arrow-left-3 fg-darker smaller"></i>
		</a>
		Dashboard
	</h1>
		<h2>My Widgets</h2><br />
		
		<div class="tile bg-dark" onclick="window.location='#';">
			<div class="tile-content icon">
				<i class="icon-new"></i>
			</div>
			<div class="tile-status">
				<span class="name">Create Category</span>
			</div>
		</div>		
		
		<table class="table">
			<thead>
				<tr>
					<th class="text-left">Action</th>
					<th class="text-left">Widget name</th>
					<th class="text-left">Description</th>
					<th class="text-left">Author</th>
				</tr>
			</thead>

			<tbody id="table1">
		
		<script>
			
			var userid=0;		
		    
			userid = 'userid=<?php echo $_SESSION["USERID"]; ?>';
			
				$.ajax({
					type: "POST",
					url: "http://150.140.184.215/marketplace-manage/_json/get_your_widget.php",
					data: userid,
					dataType: "json",
					success: function(msg){
						if(parseInt(msg.status)==1)
						{
							window.location=msg.txt;
						}
						else if(parseInt(msg.status)==0)
						{
							var i_row=0;
							var id_widget = new Array();
							var author_widget = new Array();
							var name_widget = new Array();
							var note_widget = new Array();
							
							mydata = JSON.parse(msg.txt, function (key, value){
								switch(key)
								{
									case "id":
										id_widget[i_row] = value;								
										break;
									case "author":
										author_widget[i_row] = value;
										break;
									case "name":
										name_widget[i_row] = value;
										break;
									case "note":
										note_widget[i_row] = value;
										i_row++;
										break;
									default:
										break;
								}
							
							});
							var tr_table='';
							for(var i=0;i<i_row;i++)
							{
								tr_table += "<tr><td><a href=\"\"><i class=\"icon-cancel\"></i></a></td><td class=\"right\">"+name_widget[i]+"</td><td class=\"right\">"+note_widget[i]+"</td><td class=\"right\">"+author_widget[i]+"</td></tr>";
							}
							document.getElementById("table1").innerHTML=tr_table;
							
						}
					}
				});
			
				
		</script>
				</tbody>
            <tfoot></tfoot>
		</table>
	</div>
	
	
  
<?php include "footer.php"; ?>