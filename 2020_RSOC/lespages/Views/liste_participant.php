<head>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css" integrity="sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4" crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" href="../css/style.css">
	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.10/css/all.css" integrity="sha384-+d0P83n9kaQMCwj8F4RJB66tzIwOKmrdb46+porD/OvrJ+37WqIM7UoBtwHO6Nlg" crossorigin="anonymous">
</head>

<?php
	require_once('../Model/Model.php');
	require_once('../Model/RSOCManager.php');
	$rsoc = new RSOCManager();
	$participants = $rsoc->getParticipants();
?>

<div class="container">
 	<div class="row">
		<?php
			while($row = mysql_fetch_assoc($participants)){
				echo'<div class="col-sm-4 personne">';       
				  	echo'<a href="?id='.$row["id"].'"><img src="../../documents/profile_picture.png" class="img-cercle" ></a> ';
				  	echo $row['nom'].' '.$row['prenom'];
				echo'</div>';
			}
		?> 
	</div>
</div>
