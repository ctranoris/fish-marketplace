<?php
	error_reporting( E_ALL );
	ini_set('display_errors', 1);
	
	//include "header.php"; 

?>
<script src="js/jquery/jquery.min.js"></script> 
    <script src="js/jquery/jquery.widget.min.js"></script>
<script type="text/javascript">	

	var userid = 'userid=<?php echo $_SESSION["USERID"]; ?>';
	
$(document).ready(function(){
	$.ajax({
		type: "POST",
		url: "http://150.140.184.215/marketplace-manage/_json/get_toolbar_of_widget.php",
		data: userid,
		dataType: "json",
		success: function(msg){
			if(parseInt(msg.status)==1)
			{
				window.location=msg.txt;
			}
			else if(parseInt(msg.status)==0)
			{

				//All the Widget
				
				var count_all_widget=0;
				
				var url_widget_meta_data=new Array();
				var title_widget_meta_data=new Array();
				var author_widget_meta_data=new Array();
				var sdescription_widget_meta_data=new Array();
				
				myData = JSON.parse(msg.payload, function (key, value) {
					switch(key)
					{
						alert(key+' - '+value);
						
						case "url_widget_meta_data":
							url_widget_meta_data[count_all_widget]=value;
							break;
						case "title_widget_meta_data":
							title_widget_meta_data[count_all_widget]=value;
							break;
						case "author_widget_meta_data":
							author_widget_meta_data[count_all_widget]=value;
							break;
						case "sdescription_widget_meta_data":
							sdescription_widget_meta_data[count_all_widget]=value;
							count_all_widget++;
							break;
						default:
							break;
					}
				});
				
				var count_cat=0;
				var count_cat_in_widget = 0;
				var name_category=new Array();
				var url_widget=new Array();
				var title_widget=new Array();
				var author_widget=new Array();
				var sdescription_widget=new Array();
				
				
				//Widget in category
				myData1 = JSON.parse(msg.txt2, function (key1, value1) {
					
					switch(key1)
					{
						case "name_category_widget":							
							//name_category[count_cat]=new Array();
							url_widget[count_cat]=new Array();
							title_widget[count_cat]=new Array();
							author_widget[count_cat]=new Array();
							sdescription_widget[count_cat]=new Array();
							name_category[count_cat] =value1;
							
							break;
						case "url_widget_meta_data":
							url_widget[count_cat][count_cat_in_widget] =value1;
							break;
						case "title_widget_meta_data":
							title_widget[count_cat][count_cat_in_widget] = value1;
							break;
						case "author_widget_meta_data":
							author_widget[count_cat][count_cat_in_widget] = value1;
							break;
						case "sdescription_widget_meta_data":
							sdescription_widget[count_cat][count_cat_in_widget] = value1;
							count_cat_in_widget++;count_cat++;
							break;
						default:							
							break;
					}
				
				});
				
				var get_library = '{"All": [';
	
				var i=0;
				for(i=0;i<count_all_widget;i++)
				{
					get_library += '{"src":"'+url_widget_meta_data[i]+'","info":{"title":"'+title_widget_meta_data[i]+'","author":"'+author_widget_meta_data[i]+'","description":"'+sdescription_widget_meta_data[i]+'"}}';					
					if(i<count_all_widget-1)
					{
						get_library += ',';
					}
				}
				get_library += '],';
				
				
				
				var j=0,k=0;
				
				for(j=0;j<count_cat;j++)
				{
					get_library += '"'+name_category[j]+'": [';
					for(k=0;k<count_cat_in_widget;k++)
					{
						if(url_widget[j][k]!=null)
						{
							get_library += '{"src":"'+url_widget[j][k]+'","info":{"title":"'+title_widget[j][k]+'","author":"'+author_widget[j][k]+'","description":"'+sdescription_widget[j][k]+'"}}';
							if(k<count_cat_in_widget-2)
							{
								get_library += ',';
							}
						}
					}
					get_library += ']';
					if(j<count_cat-1)
					{
						get_library += ',';
					}
				}
				
				get_library += '}; Iframework.loadLibrary(library); });';
				
				document.write('$(function(){ var library = ');
				document.write(get_library);
				//alert('$(function(){ var library = '+get_library);
			}
		}
	});
});
</script>


<!-- 
$(function(){
	var library = {

		"All": [
				{"src":"http://forresto.github.com/meemoo-modules/clock.html","info":{"title":"Clock","author":"meemoo","description":"Meemoo repository"}},
				{"src":"http://forresto.github.com/meemoo-paint/paint.html","info":{"title":"Paint","author":"meemoo","description":"Meemoo repository"}},
				{"src":"http://forresto.github.com/meemoo-modules/audioarray.html","info":{"title":"Audio Array","author":"meemoo","description":"Meemoo repository"}},
				{"src":"http://forresto.github.com/meemoo-speak.js/text2speech.html","info":{"title":"Text to Speech","author":"meemoo","description":"Meemoo repository"}}],
		"Misc": [
			    {"src":"http://forresto.github.com/meemoo-speak.js/text2speech.html","info":{"title":"Text to Speech","author":"meemoo","description":"Meemoo repository"}}],
		"Category5": [
				{"src":"http://forresto.github.com/meemoo-modules/clock.html","info":{"title":"Clock","author":"meemoo","description":"Meemoo repository"}},
				{"src":"http://forresto.github.com/meemoo-speak.js/text2speech.html","info":{"title":"Text to Speech","author":"meemoo","description":"Meemoo repository"}}],
		"Category3": [
				{"src":"http://forresto.github.com/meemoo-modules/audioarray.html","info":{"title":"Audio Array","author":"meemoo","description":"Meemoo repository"}},
				{"src":"http://forresto.github.com/meemoo-modules/clock.html","info":{"title":"Clock","author":"meemoo","description":"Meemoo repository"}}],
		"Category4": [
				{"src":"http://forresto.github.com/meemoo-paint/paint.html","info":{"title":"Paint","author":"meemoo","description":"Meemoo repository"}}],
		"course": [
				{"src":"http://forresto.github.com/meemoo-speak.js/text2speech.html","info":{"title":"Text to Speech","author":"meemoo","description":"Meemoo repository"}},
				{"src":"http://forresto.github.com/meemoo-modules/clock.html","info":{"title":"Clock","author":"meemoo","description":"Meemoo repository"}}]
	};
	
	
	 Iframework.loadLibrary(library);

});
-->
				
<?php //include "footer.php"; ?>