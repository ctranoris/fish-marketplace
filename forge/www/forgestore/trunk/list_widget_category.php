<?php include "header.php"; 

if(isset($_GET["upcategory"]) && !empty($_GET["upcategory"]) )
{
	if($_GET["c1"]=="on")
	{
		$c1=1;
	}
	else
	{
		$c1=0;
	}
	$catid = explode('#', $_GET["catid"], 1);
	
	$query_update = "UPDATE tbl_category_widget SET name_category_widget='".$_GET["upcategory"]."',active_category_widget=".$c1." WHERE id_category_widget=".$catid[0];
	//$result_update = mysql_query($query_update);
	$result_update = $connection->query($query_update);

	if($result_update)
	{
		header('Location: list_widget_category.php');
	}

}

if(isset($_GET["category"]) && !empty($_GET["category"]) )
{
	if($_GET["c1"]=="on")
	{
		$c1=1;
	}
	else
	{
		$c1=0;
	}
	$query_insert = "INSERT INTO tbl_category_widget (name_category_widget, active_category_widget) VALUES ('".$_GET["category"]."', ".$c1.")";
	//$result_insert = mysql_query($query_insert);
	$result_insert = $connection->query($query_insert);
	
	if($result_insert)
	{
		header('Location: list_widget_category.php');
	}
}

?>

	<div class="container">  
		<h2>List of Widget Category</h2>
		<div class="grid fluid">
			<div class="row">
				<?php
					$Select_category="SELECT id_category_widget, name_category_widget, active_category_widget FROM tbl_category_widget ";	
					//$result_category = mysql_query($Select_category);
					$result_category = $connection->query($Select_category);
					$count_cat=0;
					//while($row = mysql_fetch_array($result_category)){
					while($row = $result_category->fetch_array()){
						$id_category_widget[$count_cat] = $row[0];
						$name_category_widget[$count_cat] = $row[1];
						$active_category_widget[$count_cat] = $row[2];

						$count_cat++;
					}	
				
				?>
					<div class="tile bg-dark" id="createCatWindow">
						<div class="tile-content icon">
							<i class="icon-plus"></i>
						</div>
						<div class="tile-status">
							<span class="name">Create Services</span>
						</div>
					</div>
				
</div>
		</div>
		<div class="grid fluid">
			<div class="row">
					<div class="span3">
						<div class="listview small" style="width:200px; ">
						<?php
							if($count_cat==0)
							{
								echo "<h2>No Entries</h2>";
							}
							for($i=0;$i<$count_cat;$i++)
							{ 
								if($active_category_widget[$i]==1)
								{
									echo '<a href="#" id="toggle'.$id_category_widget[$i].'" class="list" style="width:200px;"> <div class="list-content" data-artid="'.$id_category_widget[$i].'" data-artcatid="'.$active_category_widget[$i].'">	<div class="data" style="width:170px;min-width: 170px;margin-left: 20px;"> <span class="list-title" style=" margin-left:-20px; width:170px;">'.$name_category_widget[$i].'</span> </div> </div> </a>';
									echo '<div class="place-right" style="margin-right:-60px; margin-top:-40px;"><a href="#" class="editCatWindow" data-name="'.$name_category_widget[$i].'" data-catid="'.$id_category_widget[$i].'"><i class="icon-pencil"></i></a>&nbsp;</div>';
								}
								else if($active_category_widget[$i]==0)
								{
									echo '<a href="#" id="toggle'.$id_category_widget[$i].'" class="list bg-red" style="width:200px;"> <div class="list-content" data-artid="'.$id_category_widget[$i].'" data-artcatid="'.$active_category_widget[$i].'" >	<div class="data" style="width:170px;min-width: 170px;margin-left: 20px;"> <span class="list-title" style=" margin-left:-20px; width:170px;">'.$name_category_widget[$i].'</span> </div> </div> </a>';
									echo '<div class="place-right" style="margin-right:-60px; margin-top:-40px;"><a href="#" class="editCatWindow" data-name="'.$name_category_widget[$i].'" data-catid="'.$id_category_widget[$i].'"><i class="icon-pencil"></i></a>&nbsp;</div>';
								}
							}
						?>
						</div>
					</div>
			</div>
		</div>
		
		<script>
		$(document).ready(function(){
			$("#createCatWindow").on('click', function(){			
				$.Dialog({
					shadow: true,
					overlay: false,
					draggable: true,
					icon: '<span class="icon-new"></span>',
					title: 'Draggable window',
					width: 500,
					padding: 10,
					content: 'This Window is draggable by caption.',
					onShow: function(){
						var content = '<form id="login-form-1">' +
									'<label>Category</label>' +
									'<div class="input-control text"><input type="text" name="category"><button class="btn-clear"></button></div>' +
									'<div class="input-control checkbox"><label><input type="checkbox" name="c1" checked/><span class="check"></span>Active</label></div>'+									
									'<div class="form-actions">' +
									'<button class="button primary">Save</button>&nbsp;'+
									'<button class="button" type="button" onclick="$.Dialog.close()">Cancel</button> '+
									'</div>'+
									'</form>';
									$.Dialog.title("Widget Category");
                                    $.Dialog.content(content);
                                    $.Metro.initInputs('#login-form-1');
					}
				});
			});
						
		});
		$(document).ready(function(){
			$(".editCatWindow").on('click', function(){		
				var elem = $(this);			
				$.Dialog({
					shadow: true,
					overlay: false,
					draggable: true,
					icon: '<span class="icon-new"></span>',
					title: 'Draggable window',
					width: 500,
					padding: 10,
					content: 'This Window is draggable by caption.',
					onShow: function(){
						var content = '<form id="login-form-1">' +
									'<label>Category</label>' +
									'<div class="input-control text"><input type="text" name="upcategory" value="'+elem.attr('data-name')+'"><button class="btn-clear"></button></div>' +
									'<div class="input-control checkbox"><label><input type="checkbox" name="c1" checked/><span class="check"></span>Active</label></div>'+
									'<div class="input-control hidden"><input type="hidden" name="catid" value="'+elem.attr('data-catid')+'" /></div>'+
									'<div class="form-actions">' +
									'<button class="button primary">Save</button>&nbsp;'+
									'<button class="button" type="button" onclick="$.Dialog.close()">Cancel</button> '+
									'</div>'+
									'</form>';
									$.Dialog.title("Widget Category");
                                    $.Dialog.content(content);
                                    $.Metro.initInputs('#login-form-1');
					}
				});
			});
						
		});
		$(function(){
		$(".list-content").on('click', function(){			
				
                var elem = $(this);
                $.ajax({
                    type: "GET",
                    url: "toggle_active_cat.php",
                    data: "id="+elem.attr('data-artid')+"&cat_actv="+elem.attr('data-artcatid'),
                    dataType:"json",  
                    success: function(data) {
                        if(data.success){
							if(data.message=="Activate")
							{
                                $.Notify({
									style: {background: 'green', color: 'white'},
									shadow: true,
                                    position: 'bottom-right',
                                    content: data.message
                                });
								$("#toggle"+elem.attr('data-artid')).removeClass("list bg-red").addClass("list");
								//document.write('$active_category_widget['+elem.attr('data-artid')+']='+elem.attr('data-artcatid')+';');
								window.location.reload();
							}
							else if(data.message=="Deactivate")
							{
								$.Notify({
									style: {background: 'red', color: 'white'},
									shadow: true,
                                    position: 'bottom-right',
                                    content: data.message
                                });
								$("#toggle"+elem.attr('data-artid')).removeClass("list").addClass("list bg-red");
								//document.write('$active_category_widget['+elem.attr('data-artid')+']='+elem.attr('data-artcatid')+';');
								window.location.reload();
							}
                        }
                    }
                });
                return false;
            });
			});
			
			
			$(function(){
		$(".delete_cat").on('click', function(){			
				
                var elem = $(this);
                $.ajax({
                    type: "GET",
                    url: "toggle_active_cat.php",
                    data: "id="+elem.attr('data-artid')+"&cat_actv="+elem.attr('data-artcatid'),
                    dataType:"json",  
                    success: function(data) {
                        if(data.success){
							if(data.message=="Activate")
							{
                                $.Notify({
									style: {background: 'green', color: 'white'},
									shadow: true,
                                    position: 'bottom-right',
                                    content: data.message
                                });
								$("#toggle"+elem.attr('data-artid')).removeClass("list bg-red").addClass("list bg-emerald");
								//document.write('$active_category_widget['+elem.attr('data-artid')+']='+elem.attr('data-artcatid')+';');
								window.location.reload();
							}
							else if(data.message=="Deactivate")
							{
								$.Notify({
									style: {background: 'red', color: 'white'},
									shadow: true,
                                    position: 'bottom-right',
                                    content: data.message
                                });
								$("#toggle"+elem.attr('data-artid')).removeClass("list bg-emerald").addClass("list bg-red");
								//document.write('$active_category_widget['+elem.attr('data-artid')+']='+elem.attr('data-artcatid')+';');
								window.location.reload();
							}
                        }
                    }
                });
                return false;
            });
			});
			
		</script>
	</div>

<?php include "footer.php"; ?>
