<?php 

	include "functions/session.php";
	include "functions/functions.php";
	include "functions/conf.php";
	
	is_logged_in();
	
?>
<!DOCTYPE HTML>
<html>
	<head>
		<script type="text/javascript">
			(function(){
				//"use strict";
				window._meemooInitialize = function(compatible) {
					if (!document.getElementById("iframework-info")) {
					// Wait for DOM
						window.setTimeout(function(){
							_meemooInitialize(compatible);
						}, 100);
						return false;
					}
					if (window.Iframework){
						// Already initialized
						return false;
					}
					if (!compatible) {
						document.getElementById("iframework-info").innerHTML = "&raquo; You need a modern browser to run this. ";
						document.getElementById("iframework-info").innerHTML += '<a href="#" onclick="_meemooInitialize(true);return false;">Try anyway?</a>';
						return false;
					} else {
						document.getElementById("iframework-info").innerHTML = "";
					}
				}
				// This tests to see if the browser can do data clone for postMessage
				// We'll assume that if it can do that it can handle the rest
				// Adapted from http://thecssninja.com/demo/sclones/
				if(!!window.postMessage) {
					try {
						// Safari 5.1 will sometimes throw an exception and sometimes won't, lolwut?
						// When it doesn't we capture the message event and check the
						// internal [[Class]] property of the message being passed through.
						// Safari will pass through DOM nodes as Null
						// iOS Safari on the other hand passes it through as DOMWindow
						window.onmessage = function(e){
							var type = Object.prototype.toString.call(e.data);
							var safariCompatible = (type.indexOf("Null") != -1 || type.indexOf("DOMWindow") != -1) ? true : false;
							if (!window.Iframework) {
								_meemooInitialize(safariCompatible);
							}
							// Only keep the onmessage function for the one test
							window.onmessage = null;
						};
						// Spec states you can't transmit DOM nodes and it will throw an error
						// postMessage implimentations that support cloned data will throw.
						window.postMessage(document.createElement("a"),"*");
					} catch(e) {
						// BBOS6 throws but doesn't pass through the correct exception
						// so check error message
						var validCompatible = (e.DATA_CLONE_ERR || e.message == "Cannot post cyclic structures.") ? true : false;
						_meemooInitialize(validCompatible);
					}
				} else {
					_meemooInitialize(false);
				}
			})();
		</script> 
	</head>
	<body>
		<footer style="font-size: 10px; font-family: Monaco, monospace;">	
			<a href="widgets.php" target="_parent">Back to forgebox</a>				
		</footer>
	
		<!-- Libs -->
		<script src="iframework-master/libs/yepnope.min.js"></script>
		<script src="iframework-master/libs/jquery.js"></script>
		<script src="iframework-master/libs/jquery-ui/jquery-ui.js"></script>

		<!-- Iframework packaged -->
		<script src="iframework-master/build/meemoo-iframework.min.js"></script>

		<!-- Examples -->
		<!-- script src="iframework-master/src/examples/module-library.js"></script -->
		<!-- <script src="load_widgets_library.js"> </script>   -->
		<script type="text/javascript">
		
			$.when(ajax1()).done(function(a1){
				var json = JSON.parse(a1);
				Iframework.loadLibrary(json);
				
			});

	/*$(document).ready(function(){*/
	function ajax1() {
		var userid = "userid=<?php echo $_SESSION["USERID"]; ?>";
		
		return $.ajax({
			type: "POST",
			url: "http://150.140.184.215/marketplace-manage/_json/get_toolbar_of_widget.php",
			//url: "http://localhost:8080/marketplace-manage/_json/get_toolbar_of_widget.php",
			data: userid,
			dataType: "json",
			success: function(msg){

			}
		});
	
	}
		</script>
		
		<script src="iframework-master/src/examples/apps.js"></script>

		<!-- Style -->
		<link href="iframework-master/libs/jquery-ui/jquery-ui.css" rel="stylesheet" type="text/css"></link>
		<link href='http://fonts.googleapis.com/css?family=Noto+Sans' rel='stylesheet' type='text/css'>
		<link href="iframework-master/libs/spectrum/spectrum.css" rel="stylesheet" type="text/css"></link>
		<link href="iframework-master/iframework.css" rel="stylesheet" type="text/css"></link>
	</body>
</html>
