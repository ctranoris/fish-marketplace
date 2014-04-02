<?php
	include "header.php";
?>
<div class="container">
<h1>
		<a href="index.php">
			<i class="icon-arrow-left-3 fg-darker smaller"></i>
		</a>
		Dashboard
	</h1>
	<div class="wizard" id="wizard">
		<div class="steps">
			<div class="step">
				<h2>Course Details</h2>
				
			</div>
			<div class="step">
				<h2>Insert your code</h2>
				
			</div>
			<div class="step">
				<h2>Upload images & files</h2>
				
			</div>
			<div class="step">
				<h2>Publish your Application</h2>
				<br />
				<button class="command-button primary">
					<i class="icon-share-2 on-left"></i>
					Yes, Publish
					<small>I agree with Forgebox terms and conditions</small>
				</button>
				<br />
				<br />
			</div>
		</div>
	</div>


	<script>
		$(function(){
			$('#wizard').wizard({
				locale: 'en',
				onCancel: function(){
					$.Dialog({
						title: 'Wizard',
						content: 'Cancel button clicked',
						shadow: true,
						padding: 10
					});
				},
				onHelp: function(){
					$.Dialog({
						title: 'Wizard',
						content: 'Help button clicked',
						shadow: true,
						padding: 10
					});
				},
				onFinish: function(){
					$.Dialog({
						title: 'Wizard',
						content: 'Finish button clicked',
						shadow: true,
						padding: 10
					});
				}
			});
		});
	</script>
</div>
	<script src="js/hitua.js"></script>
<?php
	include "footer.php";
?>