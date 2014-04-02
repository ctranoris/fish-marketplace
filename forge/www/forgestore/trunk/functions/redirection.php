<?php

function Redirection($url)
{
	
	?>
	<script type="text/javascript">
		window.location = <?php echo $url; ?>;
	</script>
	<?php
}
?>