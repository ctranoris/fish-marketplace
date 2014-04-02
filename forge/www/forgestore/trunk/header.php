<!DOCTYPE html>
<?php

include "functions/session.php";
include "functions/functions.php";
include "functions/conf.php";

?>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="product" content="Forge Box">
    <meta name="description" content="Forge Box App">
    <meta name="author" content="NAM ECE UoP">

    <link href="css/metro-bootstrap.css" rel="stylesheet"> 
    <link href="css/metro-bootstrap-responsive.css" rel="stylesheet"> 
    <link href="css/docs.css" rel="stylesheet"> 
    <link href="js/prettify/prettify.css" rel="stylesheet"> 
	
	<script src="js/jquery.bpopup.min.js"></script>
	
    <!-- Load JavaScript Libraries -->
    <script src="js/jquery/jquery.min.js"></script> 
    <script src="js/jquery/jquery.widget.min.js"></script>
    <script src="js/jquery/jquery.mousewheel.js"></script>
    <script src="js/prettify/prettify.js"></script>

    <!-- Metro UI CSS JavaScript plugins -->
    <script src="js/load-metro.js"></script>

    <!-- Local JavaScript -->
    <script src="js/docs.js"></script>
    <script src="js/github.info.js"></script>
	
	<!-- <link rel="stylesheet" href="css/bootstrap-3.0.3.min.css" type="text/css"> -->
	<link rel="stylesheet" href="css/bootstrap-multiselect.css" type="text/css">
	<!-- <link rel="stylesheet" href="css/prettify.css" type="text/css"> -->
		
	<script src="js/select/bootstrap-3.0.3.min.js"></script>
	<script src="js/select/bootstrap-multiselect.js"></script>
	<!-- <script src="js/select/jquery-2.0.3.min.js"></script> -->
	
	<?php
	if(isset($_GET['lang']))
	{
		switch ($_GET['lang']) 
		{		
			case 'en':
				$lang_file = 'en.php';
				break;
			case 'gr':
				$lang_file = 'gr.php';
				break;
			default:
				$lang_file = 'en.php';
				break;
		}
	}
	?>
    <title>Forge Box App</title>
	
	
</head>
<body class="metro">
	<?php				
		is_logged_in();
	?>
	<!-- start Header -->
    <div class="container" style="z-index:25000;">        

		<div class="navigation-bar dark">
			<div class="navbar-content">

				<a href="index.php" class="element"> Marketplace Manage<sup>v1.0.1</sup></a>
				<span class="element-divider"></span>

				<a class="pull-menu" href="#"></a>
				<ul class="element-menu">
					<li>
						<a class="dropdown-toggle" href="#">Users</a>
						<ul class="dropdown-menu" data-role="dropdown">
							<li><a href="#">New</a></li>
							<li class="divider"></li>
							<li><a href="#">Manage</a></li>
						</ul>
					</li>
					<li><a href="category_item.php">Manage Marketplace Items</a></li>
					<li>
						<a class="dropdown-toggle" href="#">Categories</a>
						<ul class="dropdown-menu" data-role="dropdown">
							<li><a href="list_widget_category.php">Widgets</a></li>
							<li class="divider"></li>
							<li><a href="list_application_category.php">Applications</a></li>
							<li class="divider"></li>
							<li><a href="other_services_category.php">Other Services</a></li>
						</ul>
					</li>
					<li>
						<a class="dropdown-toggle" href="marketplace.php">Marketplaces</a>
						<ul class="dropdown-menu" data-role="dropdown">
							<li><a href="marketplace.php">Widgets</a></li>
							<li class="divider"></li>
							<li><a href="marketplace.php">Shared Cources</a></li>                                 
							<li class="divider"></li>
                                                        <li><a href="marketplace.php">FORGEBox services</a></li>

						</ul>
					</li>					
				</ul>
				
				<div class="no-tablet-portrait">
					<div class="element place-right" style="width:220px;">
						<a class="dropdown-toggle" href="#">						
							<div style="padding-left:0px; margin-top:-9px;"><?php echo $_SESSION['FNAME_MARKET']." ".$_SESSION['LNAME_MARKET'];?> <br /> <?php echo "(".$_SESSION['UROLE_MARKET'].")";?> </div>
						</a>
						<ul class="dropdown-menu place-left" data-role="dropdown">          
							<li><a href="account.php">Account</a></li>
							<li><a href="functions/logout.php">Logout</a></li>
						</ul>
					</div>
					
				</div>
			</div>
		</div>			
		<!--  End Header -->
		<br />
	</div>
			
