<?php
	require_once('lespages/Model/Model.php');
	require_once('lespages/Model/RSOCManager.php');
	$rsoc = new RSOCManager();
	$participants = $rsoc->getParticipants();
?>

<div class="jumbotron my-jumbotron rounded-0">
	<h1>La team</h1>
</div>

<div class="container">
 	<div class="flex-container">
		<?php
			while($row = mysql_fetch_assoc($participants)){
				echo'<div class="col-sm-4 personne">'; 
					if($row['lienPhoto']!= NULL){     
					  	echo'<a href="?page=participant&id='.$row["id"].'"><img src="'.$row['lienPhoto'].'"class="img-cercle" ></a> ';
					}else{
					  	echo'<a href="?page=participant&id='.$row["id"].'"><img src="./documents/profile_picture.png" class="img-cercle" ></a> ';
					  	
					}
					echo $row['nom'].' '.$row['prenom'];
				echo'</div>';
			}
		?> 
	</div>
</div>
