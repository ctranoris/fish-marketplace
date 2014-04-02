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
	
	<script src="js/jquery.mixitup.min.js"></script>
	
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
    <title>FORGEBox</title>
	
	
</head>
<body class="metro">
	<?php				
		is_logged_in();
	?>
	<!-- start Header -->
    <div class="container" style="z-index:25000;">        

		<div class="navigation-bar dark">
			<div class="navbar-content">
	
				<a href="index.php" class="element"><img src="images/FORGE_Logo_toolbar.png"  style="margin-top:-8px;"/></a>
				<span class="element-divider"></span>

				<a class="pull-menu" href="#"></a>
				<ul class="element-menu">
					<li>
						<a class="dropdown-toggle" href="#">Interactive Course</a>
						<ul class="dropdown-menu" data-role="dropdown">
							<li><a href="design_app_logic_view.php">Create New</a></li>
							<li class="divider"></li>
							<li><a href="interactive_courses.php">Manage</a></li>
							<li class="divider"></li>
                                                        <li><a href="apps.php">Import from repository</a></li>
						</ul>
					</li>
					<li>
						<a class="dropdown-toggle" href="#">Widgets</a>
						<ul class="dropdown-menu" data-role="dropdown">
							<li><a href="marketplace.php">Install New</a></li>
							<li class="divider"></li>
							<li><a href="widgets.php">Manage</a></li>
						</ul>
					</li>
					 <li>
                                                <a class="dropdown-toggle" href="#">FORGEBox Services</a>
                                                <ul class="dropdown-menu" data-role="dropdown">
                                                        <li><a href="marketplace.php">Install New</a></li>
                                                        <li class="divider"></li>
                                                        <li><a href="widgets.php">Manage</a></li>
                                                </ul>
                                        </li>

					<li>
						<a class="dropdown-toggle" href="marketplace.php">Repositories</a>
						<ul class="dropdown-menu" data-role="dropdown">
							<li><a href="marketplace.php">Widgets</a></li>
							<li class="divider"></li>
							 <li><a href="#">FORGEBox Services</a></li>
                                                        <li class="divider"></li>
							 <li><a href="#">Interactive Courses</a></li>
                                                        <li class="divider"></li>
							<li><a href="#">Configure</a></li>
						</ul>
					</li>
					<li>
						<a class="dropdown-toggle" href="#">
							<span class="icon-cog"></span>
							<span>System</span>
						</a>
						<ul class="dropdown-menu place-left" data-role="dropdown">
							<li><a href="users.php">Users Management</a></li>
							<li><a href="functionality.php">Configuration</a></li>
						</ul>
					</li>
				</ul>
					
				<div class="no-tablet-portrait">
					<div class="element place-right" style="width:250px;">
						<a class="dropdown-toggle" href="#">							
							<img src="images/userimg/tranoris.png" style="width:30px; margin:-10px;" />
							<div style="padding-left:35px; margin-top:-19px;"><?php echo $_SESSION['FNAME']." ".$_SESSION['LNAME'];?> <br /> <?php echo "(".$_SESSION['UROLE'].")";?> </div>
							
						</a>
						<ul class="dropdown-menu place-left" data-role="dropdown">          
							<li><a href="account.php">My Account</a></li>
							<li><a href="reviews.php">My Reviews</a></li>
							<li><a href="index.php">My Dashboard</a></li>
							<li><a href="notification.php">Notifications</a></li>
							<li><a href="functions/logout.php">Logout</a></li>
						</ul>

					</div>
					
				</div>
			</div>
		</div>			
		<!--  End Header -->
		<br />
	</div>
			
