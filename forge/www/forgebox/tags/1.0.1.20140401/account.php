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
	<div class="accordion with-marker span3 place-left margin10" data-role="accordion" data-closeany="false" style="margin:0px; width:100% !important;">
		<div class="accordion-frame">
			<a class="heading active" href="#">User details</a>
			<div class="content">
				<form method="post" action="" name="user_detail">
					<div class="input-control text size3">
						<input type="text" placeholder="input firstname" name="firstname" />
					</div>
					<br />
					<div class="input-control text size3">
						<input type="text" placeholder="input lastname" name="lastname" />
					</div>
					<br />
					<div class="input-control text size3">
						<input type="text" placeholder="input email" name="email" />
					</div>
					<br />
					<div class="input-control text size3">
						<input type="text" placeholder="input sex" name="male" />
					</div>
					<br />
					<div class="input-control text size3">
						<input type="text" placeholder="input country" name="country" />
					</div>
					<br />
					<div class="input-control checkbox">
						<label>
							<input type="checkbox" />
							<span class="check"></span>
							<a href="agreement.php">I agree</a>
						</label>
					</div>
					<br />
					<input type="submit" value="Submit">
					<input type="reset" value="Reset">
				</form>
			</div>
		</div>
		<div class="accordion-frame">
			<a class="heading" href="#">User Avatar</a>
			<div class="content">
				<form method="post" action="" name="upload_avatar">
					<div class="input-control file size3">
						<input type="file" placeholder="upload avatar" name="avatar" />
						<button class="btn-file"></button>
					</div>
					<br />
					<input type="submit" value="Upload">
					<input type="reset" value="Reset">
				</form>
			</div>
		</div>
		<div class="accordion-frame">
			<a class="heading" href="#">Change Password</a>
			<div class="content">
				<form method="post" action="" name="change_password">
					<div class="input-control text size3">
						<input type="text" placeholder="input current password" name="firstname" />
					</div>
					<br />
					<div class="input-control text size3">
						<input type="text" placeholder="input new password" name="lastname" />
					</div>
					<br />
					<div class="input-control text size3">
						<input type="text" placeholder="input confirm new password" name="email" />
					</div>
					<br />
					<input type="submit" value="Change">
					<input type="reset" value="Reset">
				</form>
			</div>
		</div>
		<div class="accordion-frame">
			<a class="heading" href="#">My Dashboard</a>
			<div class="content">
				<table class="table">
                        <thead>
                        <tr>
                            <th class="text-left">Name</th>                            
                            <th class="text-left">Action</th>
                        </tr>
                        </thead>

                        <tbody>
						<tr class="info">
							<td colspan="2" style="text-align:center;"  >
								<strong>Marketplace</strong>							
							</td>
						</tr>
                        <tr>
							<td>
								name1
							</td>
							<td class="right" class="error">
								<div class="input-control switch">
									<label>
										<input type="checkbox" checked />
										<span class="check"></span>
									</label>
								</div>
							</td>
						</tr>
                        <tr>
							<td>
								name1
							</td>
							<td class="right">
								<div class="input-control switch">
									<label>
										<input type="checkbox" />
										<span class="check"></span>
									</label>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								name1
							</td>
							<td class="right">
								<div class="input-control switch">
									<label>
										<input type="checkbox" checked />
										<span class="check"></span>
									</label>
								</div>
							</td>
						</tr>
						<tr>
							<td>
								name1
							</td>
							<td class="right">
								<div class="input-control switch">
									<label>
										<input type="checkbox" />
										<span class="check"></span>
									</label>
								</div>
							</td>
						</tr>
						<tr class="info">
							<td colspan="2" style="text-align:center;">
								<strong>UserDetails</strong>
							</td>
						</tr>
						<tr>
							<td>
								name1
							</td>
							<td class="right">
								<div class="input-control switch">
									<label>
										<input type="checkbox"  checked />
										<span class="check"></span>
									</label>
								</div>
							</td>
						</tr><tr>
							<td>
								name1
							</td>
							<td class="right">
								<div class="input-control switch">
									<label>
										<input type="checkbox" />
										<span class="check"></span>
									</label>
								</div>
							</td>
						</tr>
                        </tbody>

                        <tfoot></tfoot>
                    </table>
			</div>
		</div>
		<div class="accordion-frame">
			<a class="heading" href="#">Personalize</a>
			<div class="content">
				<p>Ut wisi enim ad minim veniam, quis nostrud exerci tation ullamcorper suscipit lobortis nisl ut.</p>
				<button class="primary">Activate the account</button>
			</div>
		</div>
	</div>
</div>

<?php
	include 'footer.php';
?>