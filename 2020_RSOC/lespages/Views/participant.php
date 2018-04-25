<head>
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css" integrity="sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4" crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" href="../css/style.css">
	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.10/css/all.css" integrity="sha384-+d0P83n9kaQMCwj8F4RJB66tzIwOKmrdb46+porD/OvrJ+37WqIM7UoBtwHO6Nlg" crossorigin="anonymous">
</head>

<?php
	require_once('../Model/Model.php');
	require_once('../Model/RSOCManager.php');
	$rsoc = new RSOCManager();
	if(isset($_GET['id'])){
		$participants = $rsoc->getParticipantFromId($_GET['id']);
	}
?>

<div class="container">
 	<div class="row">
		<?php
			while($row = mysql_fetch_assoc($participants)){    
				  echo'<img src="../../documents/profile_picture.png" class="img-cercle" > ';
				  echo $row['nom'].' '.$row['prenom'];
				  echo $row['description'];
				  echo '<a href='.$row['lienGit'].' target="_blank"><i class="fab fa-github fa-lg text-dark"></i></a>
				  <a href='.$row['lienSocial'].' target="_blank"><i class="fab fa-linkedin fa-lg text-dark"></i></a>';
			}
		?> 
	</div>
</div>