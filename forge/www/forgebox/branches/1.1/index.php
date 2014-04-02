<?php

error_reporting( E_ALL );
ini_set('display_errors', 1);

include 'header.php'; 

?>

<div class="container"> 
	<h1>FORGEBox Dashboard</h1><br />

	<div class="grid">
		<div class="row">
			<div class="span9">
				<div class="tile bg-dark" onclick="window.location='interactive_courses.php';">
					<div class="tile-content icon">
						<i class="icon-cube"></i>
					</div>
					<div class="tile-status">
						<span class="name">Interactive Courses</span>
						<span class="badge bg-orange">3</span>
					</div>
				</div>
				<div class="tile bg-dark" onclick="window.location='#';">
					<div class="tile-content icon">
						<i class="icon-new"></i>
					</div>
					<div class="tile-status">
						<span class="name">Create Interactive Course</span>
					</div>
				</div>
				<div class="tile bg-dark" onclick="window.location='#';">
                                        <div class="tile-content icon">
                                                <i class="icon-download-2"></i>
                                        </div>
                                        <div class="tile-status">
                                                <span class="name">Install Widget</span>
                                        </div>
                                </div>

				
				 <div class="tile bg-dark">
                                        <div class="tile-content icon">
                                                <i class="icon-cube"></i>
                                        </div>
                                        <div class="tile-status">
                                                <span class="name">Widgets</span>
                                                <span class="badge bg-orange">15</span>
                                        </div>
                                </div>

				<div class="tile bg-dark" onclick="window.location='#';">
					<div class="tile-content icon">
						<i class="icon-download-2"></i>
					</div>
					<div class="tile-status">
						<span class="name">Install FORGEBox Service</span>
					</div>
				</div>
				<div class="tile bg-dark">
					<div class="tile-content icon">
						<i class="icon-cube"></i>
					</div>
					<div class="tile-status">
						<span class="name">FORGEBox Services</span>
						<span class="badge bg-orange">5</span>
					</div>
				</div>
				<!-- Start Accounts -->
				<div class="tile bg-dark" onclick="window.location='account.php';">
					<div class="tile-content icon">
						<i class="icon-user"><img src="images/userimg/tranoris.png"/></i>
					</div>
					<div class="tile-status">
						<span class="name">Account</span>
					</div>
				</div>
				<!-- End Accounts -->
				
	
				<!-- Start Notifications -->
				<div class="tile bg-dark" onclick="window.location='notification.php';">
					<div class="tile-content icon">
						<i class="icon-comments-4"></i>
					</div>
					<div class="tile-status">
						<span class="name">Notifications</span>
						<span class="badge bg-orange">1</span>
					</div>
				</div>
				<!-- End Notifications -->
	
				<!-- Start Help -->
				<div class="tile bg-dark">
					<div class="tile-content icon">
						<i class="icon-help"></i>
					</div>
					<div class="tile-status">
						<span class="name">Help</span>
					</div>
				</div>
				<!-- End Help -->
	
	
				<div class="tile"></div>
				<div class="tile"></div>
	
	
			</div>
			<div class="span5 right" >
				<div class="accordion" data-role="accordion">
					<div class="accordion-frame">
						<a href="#" class="heading active">Recent Interactive Courses</a>
						<div class="content">
							<p>
								<img src="images/image1.jpg" /> - Small description  <br/>
								<img src="images/image1.jpg" /> - Small description  <br />
								<img src="images/image1.jpg" /> - Small description 
							</p>
						</div>
					</div>
					<div class="accordion-frame">
						<a href="#" class="heading">Installed Widgets</a>
						<div class="content">
							<p>
								<img src="images/image1.jpg" /> - Small description  <br/>
								<img src="images/image1.jpg" /> - Small description  <br />
								<img src="images/image1.jpg" /> - Small description 
							</p>
						</div>
					</div>
					<div class="accordion-frame">
						<a href="#" class="heading">Installed FORGEBox Services</a>
						<div class="content">
							<p>
								<img src="images/image1.jpg" /> - Small description  <br/>
								<img src="images/image1.jpg" /> - Small description  <br />
								<img src="images/image1.jpg" /> - Small description
							</p>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<?php			
include 'footer.php'; 
?>
