<?php include "header.php"; ?>

<br />
<div class="container"> 
<h1>
		<a href="index.php">
			<i class="icon-arrow-left-3 fg-darker smaller"></i>
		</a>
		Dashboard
	</h1>
	
	<div id="notify_btn_1" class="tile double-vertical double"  onclick="window.location='http://www.google.com';" >My application 1</div>
	<div id="notify_btn_2" class="tile double-vertical double" >My application 2</div>
	<div id="notify_btn_3" class="tile double-vertical double" >My application 3</div>
</div>	
<script>
	$('#notify_btn_1').on('mouseover',function(){
		$.Notify({
			content: "Click to Run the application 1"			
		});
	});
	$('#notify_btn_2').on('mouseover',function(){
		$.Notify({
			content: "Click to Run the application 2"			
		});
	});
	$('#notify_btn_3').on('mouseover',function(){
		$.Notify({
			content: "Click to Run the application 3"			
		});
	});
</script>
<?php include "footer.php"; ?>