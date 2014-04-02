<?php include "header.php"; ?>

	<div class="container">  
		<?php
		if(isset($_GET["cat_id"]))
		{
			echo "<h2>Edit Widget Category</h2>";
		}
		else
		{
			echo "<h2>Create Widget Category</h2>";
		}
		?>
		
		<form>
			<div class="input-control text">
				<input type="text" value="" placeholder="input text"/>
				<button class="btn-clear"></button>
			</div>
		</form>
		
	</div>

<?php include "footer.php"; ?>