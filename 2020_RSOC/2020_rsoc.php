<?php

require_once('lespages/Model/Model.php');
require_once('lespages/Model/RSOCManager.php');

$rsoc = new RSOCManager();

?>


<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8"/>
	<title>APP RS-OC 2020</title>

	<!-- Font awesome icons -->
	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.10/css/all.css" integrity="sha384-+d0P83n9kaQMCwj8F4RJB66tzIwOKmrdb46+porD/OvrJ+37WqIM7UoBtwHO6Nlg" crossorigin="anonymous">

	<!-- Bootstrap CSS -->
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css" integrity="sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4" crossorigin="anonymous">
	<link rel="stylesheet" type="text/css" href="lespages/css/style.css">
	<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.10/css/all.css" integrity="sha384-+d0P83n9kaQMCwj8F4RJB66tzIwOKmrdb46+porD/OvrJ+37WqIM7UoBtwHO6Nlg" crossorigin="anonymous">

</head>

<body>

	<!-- Navbar -->
	<nav class="navbar navbar-expand-lg navbar-color">
		<a class="navbar-brand" href="#">PolySkynet</a>
		<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
			<div class="hamburger-icon"><i class="fas fa-bars"></i></div>
		</button>

		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			<ul class="navbar-nav mr-auto">
				<li class="nav-item">
					<a class="nav-link" href="#">Team</a>
				</li>
				<li class="nav-item dropdown">
					<a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						Présentation
					</a>
					<div class="dropdown-menu" aria-labelledby="navbarDropdown">
						<a class="dropdown-item" href="">Cahier des charges</a>
						<a class="dropdown-item" href="#">Démonstration</a>
						<a class="dropdown-item" href="#">Anciens projets</a>
					</div>
				</li>
			</ul>
		</div>
	</nav>


	<!-- Main -->
	<main></main>


	<!-- Footer à finir -->
	<footer class="footer">
		<div class="row">
			<div class="col-md-4">
				<h5 class="text-center">A propos</h5>
			</div>
			<div class="col-md-4">
			</div>
			<div class="col-md-4">
			</div>
		</div>

		<div class="row">
			<div class="col-md-1 offset-md-5">
				<img src="documents/logo.png" width="200px">
			</div>
		</div>
	</footer>

	<!-- Bootstrap JS -->
	<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.0/umd/popper.min.js" integrity="sha384-cs/chFZiN24E4KMATLdqdvsezGxaGsi4hLGOzlXwp5UZB1LY//20VyM2taTB4QvJ" crossorigin="anonymous"></script>
	<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/js/bootstrap.min.js" integrity="sha384-uefMccjFJAIv6A+rW+L4AHf99KvxDjWSu1z9VI8SKNVmz4sk7buKt/6v9KI65qnm" crossorigin="anonymous"></script>

</body>
</html>