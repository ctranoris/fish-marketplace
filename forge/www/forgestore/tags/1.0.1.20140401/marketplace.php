<?php include "header.php"; ?>

<div class="container">	

<h1>
		<a href="index.php">
			<i class="icon-arrow-left-3 fg-darker smaller"></i>
		</a>
		Dashboard
	</h1>

	<div class="grid">
		<div class="row">
			<div>
			<div id="carousel1" class="carousel" data-role="carousel" data-duration="500" data-period="6000">
                                        <div class="slides">
                                                <div class="slide" id="slide1" style="background: url(images/slide1.PNG) no-repeat center;">
                                                        <div style="padding-left:25px;padding-right:375px;padding-top:25px;padding-bottom:25px;">
                                                                <h2>FORGE widgets</h2>
                                                                <p class="bg-color-blueDark fg-color-white">A wide range of interactive widget to support your interactive course</p>
                                                                <p class="tertiary-info-text">
                                                                        <a href="category_item.php">Browse</a> our widget marketplace to view a list of possible solutions to include in your interactive course!
                                                                </p>
                                                        </div>
                                                </div>
                                                <div class="slide" id="slide2" style="background: url(images/slide2.PNG) no-repeat center;">
                                                        <div style="padding-left:300px;padding-right:75px;padding-top:25px;padding-bottom:25px;">
                                                                <h2 class="fg-color-darken">Create your own FORGE enabled Courses</h2>
                                                                <p class="bg-color-blueDark fg-color-white">Create your interactive courses, publish them from FORGEBox to your LMS or your eBooks and
                                                                        share them online with other interested learners!</p>

                                                        </div>
                                                </div>
                                                <div class="slide" id="slide3" style="background: url(images/slide3.PNG) no-repeat center;">
                                                        <div style="padding-left:25px;padding-right:375px;padding-top:25px;padding-bottom:25px;">
                                                                <h2>Easy access of FIRE tools and infrastructures!</h2>
                                                                <p class="bg-color-blueDark fg-color-white">Install services inside your FORGEBox to enable easy access to FIRE facilities. Resource
                                                                        configuration, scheduling and user management with a few clicks!</p>
                                                        </div>
                                                </div>
                                        </div>
                                        <a class="controls left"><i class="icon-arrow-left-3"></i></a>
                                        <a class="controls right"><i class="icon-arrow-right-3"></i></a>
                                </div>


			</div>
		</div>
	</div>
	  <div class="grid">
                        <div class="span4">
                                <div class="input-control text size3 ">
                                        <input type="text" class="text Search" />
                                        <button class="btn-search"></button>
                                </div>
                        </div>
        </div>

                <div id="show_categories"></div>

	
	<div class="grid">
		<div class="row">
			<script type="text/javascript">
			
				var userid = 'userid=<?php echo $_SESSION["USERID"]; ?>';
				$(document).ready(function(){
					
					$.ajax({
						type: "POST",
						url: "http://150.140.184.215/marketplace-manage/_json/get_widget_in_marketplace.php",
						//url: "http://localhost:8080/marketplace-manage/_json/get_widget_in_marketplace.php",
						data: userid,
						dataType: "json",
						success: function(msg){

							if(parseInt(msg.status)==1)
							{
								window.location=msg.txt;
							}
							else if(parseInt(msg.status)==0)
							{
								jsonWidget = msg.txt;
								
								var i_row=0;
								var id_widget = new Array();
								var url_widget = new Array();
								var title_widget = new Array();
								var author_widget = new Array();
								var sdescription_widget = new Array();
								var description_widget = new Array();
								var simage_widget = new Array();
								var limage_widget = new Array();
								var active_widget = new Array();
								var category_widget = new Array();
								var widget_installed = new Array();
								
								myData = JSON.parse(jsonWidget, function (key, value) {
									switch(key)
									{
										case "id":
											id_widget[i_row] = value;												
											break;										
										case "title":
											title_widget[i_row] = value;
											break;										
										case "sdescription":
											sdescription_widget[i_row] = value;
											break;
										case "limage":
											limage_widget[i_row] = value;
											break;										
										case "categories":
											category_widget[i_row] = value;
											break;
										case "widget_installed":
											widget_installed[i_row] = value;
											i_row++;
											break;
										default:
											break;
									}

								});
								
									var i=0;
									var i_column=0;
									var html_widgets="";
									html_widgets+="<div class=\"row\">";
									for(i=0;i<i_row;i++)
									{
										
										html_widgets+="<div class=\"mix_cat "+category_widget[i]+"\" style=\"margin-right:-30px;\">";
										//html_widgets+="<a href=\"items.php?itemID="+id_widget[i]+"\" class=\"widget_id_"+id_widget[i]+"\">";
										html_widgets+="<a href=\"#\" class=\"widget widget_id_"+id_widget[i]+"\" data-atr_id=\""+id_widget[i]+"\"  data-atrarrposition=\""+i+"\">";
										
										if(widget_installed[i]=="")
										{
											html_widgets+="<div class=\"tile  double-vertical double \" style=\"margin-left:20px;\">";
										}
										else
										{
											html_widgets+="<div class=\"tile  double-vertical double selected \" style=\"margin-left:20px;\">";
										}
										html_widgets+="<div class=\"tile double-vertical double image-set\">";
										html_widgets+="<img src=\"images/_widget/"+limage_widget[i]+"\">";
										html_widgets+="</div>";
										html_widgets+="<div class=\"brand bg-dark opacity\">";
										html_widgets+="<span class=\"label fg-white\"><strong>"+title_widget[i]+"</strong> <br /><br /> "+sdescription_widget[i]+"</span>";
										html_widgets+="<span class=\"badge bg-orange\">12</span>";
										html_widgets+="</div>";
										html_widgets+="</div>";
										html_widgets+="</a>";	
										html_widgets+="</div>";						
										
										/*if(i_column==3)
										{
											
											i_column=0;
										}
										else
										{
											i_column++;
										}*/
									}
									
									html_widgets+="</div>";
									document.getElementById("show_widget").innerHTML = html_widgets;
									
									$(".widget").click(function () {	
										
										var widget_id = $(this).attr('data-atr_id');	
										var widget_array_position_i = $(this).attr('data-atrarrposition');
										var id = '#dialog';
		
										//Get the screen height and width
										var maskHeight = $(document).height();
										var maskw = $(document).width();
										var maskWidth = $(window).width();
											
										//Set heigth and width to mask to fill up the whole screen
										$('#mask').css({'width':maskWidth,'height':maskHeight});
												
										//transition effect		
										$('#mask').fadeIn(800);	
										$('#mask').fadeTo("slow",0.8);	
										
										//Get the window height and width
										var winH = $(window).height();
										var winW = $(window).width();
												
										//Set the popup window to center
										$(id).css('top',  winH/2-$(id).height()/2 -50);
										$(id).css('left', winW/2-$(id).width()/2);
											
										//transition effect
										$(id).fadeIn(500); 	
											
										//if close button is clicked
										$('.window .close').click(function (e) {
											//Cancel the link behavior
											e.preventDefault();
											$( "#widget_item_popup" ).empty();
											$('#mask').hide();
											$('.window').hide();
										});		
												
										//if mask is clicked
										$('#mask').click(function () {
											$(this).preventDefault();
											$(this).hide();
											$('.window').hide();
										});
										var dt="widget_id="+widget_id;
										$.ajax({
											type: "POST",						
											url: "http://150.140.184.215/marketplace-manage/_json/get_widget_item.php",
											//url: "http://localhost:8080/marketplace-manage/_json/get_widget_item.php",
											data: dt,
											dataType: "json",
											success: function(msg){
												if(parseInt(msg.status)==1)
												{
													alert("Error");
													$('#mask').hide();
													$('.window').hide();
												}
												else if(parseInt(msg.status)==0)
												{
													jsonCategory = msg.txt;																
													var j_row=0;
													
													var url_widget_meta_data = "";
													var title_widget_meta_data = "";
													var author_widget_meta_data = "";
													var sdescription_widget_meta_data = "";
													var description_widget_meta_data = "";
													var simage_widget_meta_data = "";
													var limage_widget_meta_data = "";
													var active_widget_meta_data = "";
	
													myData = JSON.parse(jsonCategory, function (key, value) {
														switch(key)
														{
															case "url":
																url_widget = value;													
																break;
															case "title":
																title_widget = value;
																break;
															case "author":
																author_widget = value;
																break;
															case "description":
																description_widget = value;
																break;
															case "limage":
																limage_widget = value;
																break;
															case "active":
																active_widget = value;
																break;															
															default:
																break;
														}

													});
												
										
													$( "#widget_item_popup" ).empty();
													
    /*
													$( "#widget_item_popup" ).append( "<div class=\"tab-control\" data-role=\"tab-control\">" );
													$( ".tab-control" ).append( "<ul class=\"tabs\">" );
													$( ".tabs" ).append( "<li class=\"active\"><a href=\"#_page_1\">General Info</a></li>" );
													$( ".tabs" ).append( "<li><a href=\"#_page_2\">Configuration details</a></li>" );
													$( ".tabs" ).append( "<li><a href=\"#_page_3\">Review </a></li>" );
													$( ".tab-control" ).append( "<div class=\"frames\">" );
													$( ".frames" ).append( "<div class=\"frame\" id=\"_page_1\">");
													$( "#_page_1" ).append( "<h2>"+title_widget +"</h2>" );
													$( "#_page_1" ).append( "<form name=\"frm_install\" id=\"frm_install\" action=\"#\">" );
													$( "#frm_install" ).append( "<table style=\"width:100%;\" class=\"view_table\">" );
													$( ".view_table" ).append( "<tr class=\"first_tr\">" );
													$( ".first_tr" ).append( "<td style=\"width:30%;\"><img src=\"images/_widget/"+limage_widget+"\" /></td><td style=\"width:70%;\">"+description_widget+"</td>" );													
													$( "#frm_install" ).append( "<input type=\"hidden\" id=\"userid\" data-atruserid=\"<?php echo $_SESSION['USERID']; ?>\" name=\"userid\" value=\"<?php echo $_SESSION['USERID']; ?>\" />" );
													$( "#frm_install" ).append( "<input type=\"hidden\" id=\"widgetid\" data-atrwidgetid=\""+widget_id+"\" name=\"widgetid\" value=\""+widget_id+"\" />" );
													$( ".frames" ).append( "<div class=\"frame\" id=\"_page_2\">");
													$( "#_page_2" ).append( "<h2>Configuration Deatials</h2>" );
													$( ".frames" ).append( "<div class=\"frame\" id=\"_page_3\">");
													$( "#_page_3" ).append( "<h2>Review</h2>" );
													$( "#_page_3" ).append( "<h3>Your Rate : <div id=\"rating_1\" class=\"fg-green\"></div></h3>" );
															
													$( "#_page_3" ).append( "<div class=\"input-control textarea\">" );
													$( "#_page_3" ).append( "<textarea onclick=\"\" id=\"notify_btn_1\" onblur=\"if(this.value == '') this.value='Leave your message';\" onfocus=\"if (this.value=='Leave your message') this.value = ''; \"  value=\"Leave your message\">Leave your message</textarea>" );
													$( "#_page_3" ).append( "<button class=\"bg-darkRed fg-white\">Submit</button>" );
													
													if(widget_installed[widget_array_position_i]=="")
													{
														$( ".view_table" ).append( "<tr class=\"tr_submit\"><td>&nbsp;</td><td><input class=\"install_widget\" type=\"button\" value=\"Install\" /></td>" );	
													}
													else
													{
														$( ".view_table" ).append( "<tr class=\"tr_submit\"><td>&nbsp;</td><td><input class=\"disable_widget\" type=\"button\" value=\"Unistall\" /></td>" );	
													}
													*/
													$( "#widget_item_popup" ).append( "<h2>"+title_widget +"</h2>" );
													$( "#widget_item_popup" ).append( "<table class=\"tbl_pop_data\">");
													$( ".tbl_pop_data" ).append( "<tr class=\"tr_first_pop_data\">");
													$( ".tr_first_pop_data" ).append( "<td class=\"td_first_pop_data\">");
													$( ".td_first_pop_data" ).append( "<div class=\"subheader-secondary\" style=\"float:left;\"><a href=\"#\" id=\"tab11\" style=\"text-decoratiob:none;\">General Details&nbsp;&nbsp;</a></div><div class=\"subheader-secondary\" style=\"float:left;\"><a href=\"#\" id=\"tab22\" style=\"text-decoratiob:none;\">&nbsp; Configuration Information &nbsp;</a></div><div class=\"subheader-secondary\" style=\"float:left;\"><a href=\"#\" id=\"tab33\" style=\"text-decoratiob:none;\">&nbsp; Reviews </a></div>");
													
													$( ".tbl_pop_data" ).append( "<tr class=\"tr_pop_data\">");
													$( ".tr_pop_data" ).append( "<td class=\"td_pop_data\">");
													
													$( ".td_pop_data" ).append( "<div id=\"tab1\" style=\"width:100%;\" class=\"\">");
													
													$( "#tab1" ).append( "<form name=\"frm_install\" id=\"frm_install\" action=\"#\">" );
													$( "#frm_install" ).append( "<table style=\"width:100%;\" class=\"view_table\">" );
													$( ".view_table" ).append( "<tr class=\"first_tr\">" );
													$( ".first_tr" ).append( "<td style=\"width:30%;\"><img src=\"images/_widget/"+limage_widget+"\" /></td><td style=\"width:70%;\">"+description_widget+"</td>" );													
													$( "#frm_install" ).append( "<input type=\"hidden\" id=\"userid\" data-atruserid=\"<?php echo $_SESSION['USERID']; ?>\" name=\"userid\" value=\"<?php echo $_SESSION['USERID']; ?>\" />" );
													$( "#frm_install" ).append( "<input type=\"hidden\" id=\"widgetid\" data-atrwidgetid=\""+widget_id+"\" name=\"widgetid\" value=\""+widget_id+"\" />" );
													if(widget_installed[widget_array_position_i]=="")
													{
														$( ".view_table" ).append( "<tr class=\"tr_submit\"><td>&nbsp;</td><td style=\"float:right;\"><input class=\"install_widget\" type=\"button\" value=\"Install\" /></td>" );	
													}
													else
													{
														$( ".view_table" ).append( "<br /><tr class=\"tr_submit\"><td>&nbsp;</td><td style=\"float:right;\"><input class=\"disable_widget\" type=\"button\" value=\"Unistall\" /></td>" );	
													}
													
													$( ".td_pop_data" ).append( "<div id=\"tab2\" style=\"width:100%;\" class=\"hide\">");
													$( "#tab2" ).append( "<br /><br />Configuration Information");
													$( ".td_pop_data" ).append( "<div id=\"tab3\" style=\"width:100%;\" class=\"hide\">");													
													//$( "#tab3" ).append( "<h2>Review</h2>" );
													$( "#tab3" ).append( "<br /><div id=\"rating_1\" class=\"fg-green\"></div>");
															
													$( "#tab3" ).append( "<br /><div class=\"input-control textarea\">" );
													$( "#tab3" ).append( "<br /><textarea onclick=\"\" id=\"notify_btn_1\" onblur=\"if(this.value == '') this.value='Leave your message';\" onfocus=\"if (this.value=='Leave your message') this.value = ''; \"  value=\"Leave your message\">Leave your message</textarea>" );
													$( "#tab3" ).append( "<br /><br /><br /><br /><button class=\"bg-darkRed fg-white\">Submit</button>" );
													
													$(function(){
														$("#rating_1").rating({
															static: false,
															score: 2,
															stars: 5,
															showHint: true,
															showScore: true,
															click: function(value, rating){
																//alert("Rating clicked with value " + value);
																rating.rate(value);
															}
														});
													});
						
						
						
						
													$('#tab11').click(function () {
													
													  $('#tab1').removeClass('hide');
													  $('#tab2').addClass('hide');
													  $('#tab3').addClass('hide');
													});
													$('#tab22').click(function () {
													 
													  $('#tab1').addClass('hide');
													  $('#tab2').removeClass('hide');
													  $('#tab3').addClass('hide');
													});

													$('#tab33').click(function () {
													 
													  $('#tab1').addClass('hide');
													  $('#tab2').addClass('hide');
													  $('#tab3').removeClass('hide');
													});
													
													$(".install_widget").click(function () {

														var data1 = 'userid='+$("#userid").attr('data-atruserid')+'&widgetid='+$("#widgetid").attr('data-atrwidgetid');

														$.ajax({
															type: "POST",
															url: "http://150.140.184.215/marketplace-manage/_json/insert_widget_in_marketplace.php",
															//url: "http://localhost:8080/marketplace-manage/_json/insert_widget_in_marketplace.php",
															data: data1,
															dataType: "json",
															success: function(msg){
																if(parseInt(msg.status)==1)
																{
																	window.location='marketplace.php';
																}
																else if(parseInt(msg.status)==0)
																{
																	alert('Error');
																}
																
															}										
														});
													});
													
													
													$(".disable_widget").click(function () {

														var data1 = 'userid='+$("#userid").attr('data-atruserid')+'&widgetid='+$("#widgetid").attr('data-atrwidgetid');
														
														$.ajax({
															type: "POST",
															url: "http://150.140.184.215/marketplace-manage/_json/delete_widget_in_marketplace.php",
															//url: "http://localhost:8080/marketplace-manage/_json/delete_widget_in_marketplace.php",
															data: data1,
															dataType: "json",
															success: function(msg){
																if(parseInt(msg.status)==1)
																{
																	window.location='marketplace.php';
																}
																else if(parseInt(msg.status)==0)
																{
																	alert('Error');
																}
																
															}										
														});
													});
													
													
												}
											}
										});
										
									});
									
								
							}
							
							
						}	
									
					});
					
					
					
					$.ajax({
						type: "POST",						
						url: "http://150.140.184.215/marketplace-manage/_json/get_category_in_marketplace.php",
						//url: "http://localhost:8080/marketplace-manage/_json/get_category_in_marketplace.php",
						dataType: "json",
						success: function(msg){
							if(parseInt(msg.status)==1)
							{
								window.location=msg.txt;
							}
							else if(parseInt(msg.status)==0)
							{
								jsonCategory = msg.txt;																
								var j_row=0;
								var id_category = new Array();
								var name_category = new Array();
								var count_category = new Array();								
											
								myData = JSON.parse(jsonCategory, function (key, value) {
									switch(key)
									{
										case "id":
											id_category[j_row] = value;													
											break;
										case "name":
											name_category[j_row] = value;
											break;
										case "count_cat":
											count_category[j_row] = value;
											j_row++;
											break;
										default:
											break;
									}

								});
									
								var j=0;
								
								var html_category="<a href=\"#\" class=\"spLink\" data-atrid=\"all_widget\">All Widgets</a>&nbsp;&nbsp;&nbsp;&nbsp;";
								
								for(j=0;j<j_row;j++)
								{									
									html_category += "<a href=\"#\" class=\"spLink\" data-atrid=\"category"+id_category[j]+"\" data-atrcount=\""+count_category[j]+"\" >"+name_category[j]+"("+count_category[j]+")</a> &nbsp;&nbsp;&nbsp;&nbsp;";
								}								 
								document.getElementById("show_categories").innerHTML = html_category;
								
								$(".spLink").click(function () { 
									var cat_id = $(this).attr('data-atrid');
									
									if(cat_id != "all_widget")
									{
										$(".mix_cat").removeClass('hide');
											
										$(".mix_cat:not(."+cat_id+")").removeClass(cat_id).addClass('hide');
										if($(this).attr('data-atrid')=="0")
										{
											alert('There is no widget in category: '+cat_id);
										}
									}
									else
									{
										
										$(".mix_cat").removeClass('hide');
									}
								});
							}	
						}
					});	
					
				}); 
				
				
				
						
						
				</script>
				<div id="show_widget"></div>
				
		</div> 		
	</div>
</div>
<div style="display: none; position:absolute; left:0; top:0; width:700px; height:500px; display:none; z-index:99999; padding:10px; -moz-border-radius: 10px; -webkit-border-radius: 10px; border-radius: 10px; border: 2px solid #333333; -moz-box-shadow:4px 4px 30px #130507; -webkit-box-shadow:4px 4px 30px #130507; box-shadow:4px 4px 30px #130507; -moz-transition:top 800ms; -o-transition:top 800ms; -webkit-transition:top 800ms;  transition:top 800ms;" id="dialog" class="window">
				<div align="right" style="font-weight:bold; margin:5px 3px 0 0;"><a href="javascript:void()" class="close"><img src="images/close.png" width="16" style="border:none; cursor:pointer;" /></a></div>				
					<div align="center" style="margin:5px 0 5px 0;" id="widget_item_popup" >
						<!-- FORM  -->
						
						
						
						

					</div>	
					
				</div>
				<div style="width: 700px; height: 500px; display: none; opacity: 0.7;position:absolute; left:0; top:0; z-index:99998; background-color: #4D4D4D; display:none;" id="mask"></div>
				
<?php include "footer.php"; ?>
