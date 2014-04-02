<?php
include 'header.php'; 
?>
<div class="container">	
	<h1>
		<a href="index.php">
			<i class="icon-arrow-left-3 fg-darker smaller"></i>
		</a>
		Dashboard
	</h1>
	<h2>User Manage</h2>
	<table id="table1" class="striped"></table>
	
	<?php 
		
			$query_select_role_query = "SELECT id_role , name_role FROM tbl_role WHERE active_role =1";			
			
			//$result_select_role = mysql_query($query_select_role) or die(mysql_error());
			
			
			$result_select_role = $connection->query($query_select_role_query);
			
			$i_select_role=0;
			
			//while($row = mysql_fetch_array($result_select_role)){
			while($row = $result_select_role->fetch_array()){
				$id_role[$i_select_role] = $row[0];
				$name_role[$i_select_role] = $row[1];				
				
				$i_select_role++;
			}
			
			$query_select_users_query = "SELECT DISTINCT tbl_users.id_user, tbl_users.email_user, tbl_users.active_user FROM tbl_user_role RIGHT JOIN tbl_users ON tbl_user_role.id_user = tbl_users.id_user ORDER BY tbl_users.id_user";
			
			//$result_select_users = mysql_query($query_select_users) or die(mysql_error());
			$result_select_users = $connection->query($query_select_users_query);
			
			$i_select_user=0;
			
			while($row = $result_select_users->fetch_row()){
				$id_user[$i_select_user] = $row[0];
				$email_user[$i_select_user] = $row[1];
				$active_user[$i_select_user] = $row[2];
				
				$i_select_user++;
			}
			
			$query_select_role_user_query = "SELECT tbl_user_role.id_user, tbl_user_role.id_role FROM tbl_user_role ORDER BY tbl_user_role.id_user";
			
			//$result_select_role_user = mysql_query($query_select_role_user) or die(mysql_error());
			$query_select_role_user = $connection->query($query_select_role_user_query);
			
			$i_select_user_role=0;
			
			while($row = $query_select_role_user->fetch_row()){
				$id_user_[$i_select_user_role] = $row[0];
				$id_role_[$i_select_user_role] = $row[1];
				
				$i_select_user_role++;
			}
			
		?>

		<script>
		
		var table, table_data;		
		
		table_data = [		
			<?php
				$print_table ='';
				
				for ($i=0;$i<$i_select_user;$i++)
				{
					$print_table .= '{user:"'.$email_user[$i].'",';
					for($k=0;$k<$i_select_role;$k++)
					{
						$print_table .= $name_role[$k].':';
						$user_have_not_role=0;
						$query_select_role_user_query = "SELECT tbl_user_role.id_user, tbl_user_role.id_role FROM tbl_user_role WHERE  tbl_user_role.id_user = ".$id_user[$i]." AND tbl_user_role.id_role = ".$id_role[$k];
			
						//$result_select_role_user = mysql_query($query_select_role_user) or die(mysql_error());
						$result_select_role_user = $connection->query($query_select_role_user_query);
						
						//$num_rows = mysql_num_rows($result_select_role_user);
						$cnum_rows = $result_select_role_user->num_rows;
						
						if($cnum_rows>0)
						{
							$print_table .= '"<a class=\"role\" href=\"#\" data-artid=\"'.$id_role[$k].'\" data-artuserid=\"'.$id_user[$i].'\"><i  id=\"role'.$id_role[$k].$id_user[$i].'\" class=\"icon-checkbox\"></i></a>",';
						}
						else
						{
							$print_table .= '"<a class=\"role\" href=\"#\" data-artid=\"'.$id_role[$k].'\" data-artuserid=\"'.$id_user[$i].'\"><i id=\"role'.$id_role[$k].$id_user[$i].'\" class=\"icon-checkbox-unchecked\"></i></a>",';
						}	
					}
					if($i-1<$i_select_user)
					{
						if($active_user[$i]==1)
						{
							$print_table .= 'active:"<a class=\"activate\" href=\"#\" data-artid=\"'.$id_user[$i].'\"><i id=\"active'.$id_user[$i].'\" class=\"icon-checkbox\"></i></a>"},';
						}
						else
						{
							$print_table .= 'active:"<a class=\"activate\" href=\"#\" data-artid=\"'.$id_user[$i].'\"><i id=\"active'.$id_user[$i].'\" class=\"icon-checkbox-unchecked\"></i></a>"},';
						}
					}
					else
					{
						if($active_user[$i]==1)
						{
							$print_table .= 'active:"<a class=\"activate\" href=\"#\" data-artid=\"'.$id_user[$i].'\"><i id=\"active'.$id_user[$i].'\" class=\"icon-checkbox\"></i></a>"}';
						}
						else
						{
							$print_table .= 'active:"<a class=\"activate\" href=\"#\" data-artid=\"'.$id_user[$i].'\"><i id=\"active'.$id_user[$i].'\" class=\"icon-checkbox-unchecked\"></i></a>"}';
						}
					}
				}
				
				echo $print_table;
			?>			
		];			
		
		$(function(){
			table = $("#table1").tablecontrol({
				cls: 'table hovered border myClass',
				colModel: [
				<?php
				echo "{field: 'user', caption: 'User', width: '', sortable: false, cls: 'text-left', hcls: 'text-left'},";
				for($i=0;$i<$i_select_role;$i++)
				{
					echo "{field: '".$name_role[$i]."', caption: '".$name_role[$i]."', width: 50, sortable: false, cls: 'text-center', hcls: 'text-left'},";
				}
				echo "{field: 'active', caption: 'active', width: 50, sortable: false, cls: 'text-center', hcls: ''}";
				?>
				
				],
				 
				data: table_data
			});
		});
		
	</script>
	
	<script type="text/javascript">
        $(function(){
            $('.role').click(function(){
                var elem = $(this);		

                $.ajax({
                    type: "GET",
                    url: "functions.php",
                    data: "id="+elem.attr('data-artid')+"&userid="+elem.attr('data-artuserid'),
                    dataType:"json",  
                    success: function(data) {						
                        if(data.success){                              
								if(data.message=="Deleted from User Group")
								{
									$.Notify({
										style: {background: 'red', color: 'white'},
										shadow: true,
										position: 'bottom-right',
										content: "Deactivate user role"
									});
									
									$("#role"+elem.attr('data-artid')+elem.attr('data-artuserid')).removeClass("icon-checkbox").addClass("icon-checkbox-unchecked");
								}
								else if(data.message=="Updated User Group")
								{
									$.Notify({
										style: {background: 'green', color: 'white'},
										shadow: true,
										position: 'bottom-right',
										content: "Activate user role"
									});
									
									$("#role"+elem.attr('data-artid')+elem.attr('data-artuserid')).removeClass("icon-checkbox-unchecked").addClass("icon-checkbox");
								}
								
                        }
                    }
                });
                return false;
            });
			
			$('.activate').click(function(){
                var elem = $(this);	
                $.ajax({
                    type: "GET",
                    url: "active.php",
                    data: "id="+elem.attr('data-artid'),
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
								$("#active"+elem.attr('data-artid')).removeClass("icon-checkbox-unchecked").addClass("icon-checkbox");
							}
							else if(data.message=="Deactivate")
							{
								$.Notify({
									style: {background: 'red', color: 'white'},
									shadow: true,
                                    position: 'bottom-right',
                                    content: data.message
                                });
								$("#active"+elem.attr('data-artid')).removeClass("icon-checkbox").addClass("icon-checkbox-unchecked");
							}
                        }
                    }
                });
                return false;
            });
        });
		
        </script>
	<div id="message"></div>
	





</div>
<?php			
include 'footer.php'; 
?>