<?php
	require_once('lespages/Model/Model.php');
	require_once('lespages/Model/RSOCManager.php');
	$rsoc = new RSOCManager();
	if(isset($_GET['id'])){
		$participants = $rsoc->getParticipantFromId($_GET['id']);
	}
	$row = mysql_fetch_assoc($participants);
?>

<div class="jumbotron my-jumbotron rounded-0">
  <h1><?php echo $row['nom'].' '.$row['prenom'] ?></h1>
</div>

<div class="container">
 	<div class="row">
		<div class="detailParticipant">
			<div class="photo_profil">
			<?php	
				if($row['lienPhoto']!= NULL){     
				  	echo'<img src="'.$row['lienPhoto'].'"class="img-participant" > ';
				}
				else{
				  	echo'<img src="./documents/profile_picture.png" class="img-participant" >';
				  	
				}
			?>
				<div class="lienParticipant">
					<a href='<?php echo $row['lienGit']; ?>' target="_blank">
						<i class="fab fa-github fa-lg text-dark"></i>
					</a>
					<a href='<?php echo $row['lienSocial']; ?>' target="_blank">
						<i class="fab fa-linkedin fa-lg text-dark"></i>
					</a>
				</div>
			</div>
			<div class="description_participant">
				<?php echo $row['description'];		?>
			</div>
		</div>
    </div>
</div>