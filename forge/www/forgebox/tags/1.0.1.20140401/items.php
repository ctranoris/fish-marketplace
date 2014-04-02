<?php include "header.php"; ?>
<div class="container">
	<h1>
		<a href="marketplace.php">
			<i class="icon-arrow-left-3 fg-darker smaller"></i>
		</a>
		Marketplace
	</h1>
	
	<div class="grid">
		<div class="row">
			<div class="span14">			
				<div class="row">
					<div class="span5">
						<div id="carousel1" class="carousel" data-role="carousel" data-param-duration="300">
							<div class="slides">
								<div class="slide" id="slide1">										
									<h2>Plenty of widget!</h2>
									<p class="bg-color-blueDark padding20 fg-color-white">You can find a lot widget to create with them your application</p>
									<h3>To start: just <strong>get in</strong> for our community</h3>
									<p class="tertiary-info-text">
										bla bla bla bla ........
									</p>
								</div>
								<div class="slide" id="slide2">
									<h2 class="fg-color-darken">Create your own application</h2>
									<p class="bg-color-pink padding20 fg-color-white">
										Use our resourses and make your <strong>own </strong> widget
									</p>
									<div class="span3 place-left">
										<ul class="unstyled sprite-details">
											<li><i class="icon-checkmark"></i> Application feature 1</li>
											<li><i class="icon-checkmark"></i> Application feature 2</li>
										</ul>
									</div>
									<div class="span3 place-left">
										<ul class="unstyled sprite-details">
											<li><i class="icon-checkmark"></i> Application feature 3</li>
											<li><i class="icon-checkmark"></i> Application feature 4</li>
										</ul>
									</div>
								</div>
							</div>
						</div>
					
					</div>
					<div class="span9">
						<h3>Desciption</h3> dsafj kljsdhflksjdhaflkjsdahflkjdsah flksdahl kfdaslkjf hldask hflkasdj hflkjsh alkfjdh salkjdf hlaskdjf hlksadhf lkasdhf lkasdhf lkasdh flkasdh flkjasdjh fds f lh  fdg dfg dsf gsd g
					</div>

				</div>
			</div>
		</div>
		<div class="row">
			<div class="span14">
				<div class="row">
					<div class="span2">
						Total Score :
					</div>
					<div class="span6">
						<div class="rating"  data-role="rating" data-static="true" data-score="3" data-stars="5" data-show-score="true" ></div>
					</div>
					<div class="span3">
						<button class="bg-darkGrey fg-black">Clone</button>
					</div>
					<div class="span3">
						<button class="bg-darkGrey fg-black">Install</button>
					</div>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="span14">
				<div class="row">
					<div class="tab-control" data-role="tab-control">
						<ul class="tabs">
							<li class="active"><a href="#_page_1">Configuration</a></li>
							<li><a href="#_page_2">Help</a></li>
							<li><a href="#_page_3">Review</a></li>							
						</ul>
     
						<div class="frames">
							<div class="frame" id="_page_1">Configuration Details </div>
							<div class="frame" id="_page_2">Help Details <br /> Author ..... <br /> mails .....</div>
							<div class="frame" id="_page_3">
								<br />
								<h3>Your Rate : <div id="rating_1" class="fg-green"></div></h3>
												
								<script>
									$(function(){
										$("#rating_1").rating({
											static: false,
											click: function(value, rating){
											rating.rate(value);},
											score: 0,
											stars: 5,
											showHint: true,
											hints: ['bad', 'poor', 'regular', 'good', 'gorgeous'],
											showScore: true,
											scoreHint: "Your score : ",
										});
									});
								</script>
								<div class="input-control textarea">
									<textarea onclick="" id="notify_btn_1" onblur="if(this.value == '') this.value='Leave your message';" onfocus="if (this.value=='Leave your message') this.value = ''; "  value="Leave your message">Leave your message</textarea>
									<script>
										$('#notify_btn_1').on('click', function(){
											$.Notify({
												content: "You must write something!!!",
												width:"250px",
												height:"50px",
												style: {background: 'red', color: 'white'},
											});
											});
									</script>
								</div>
								<button class="bg-darkRed fg-white">Submit</button>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
<?php include "footer.php"; ?>