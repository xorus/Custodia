<?php

require 'autoload.php';

$db = new Database();
$session = Session::getInstance();
$auth = new Auth($session);

//$auth->restrict();

?>

<?php $title = "Surveillance et contrôle" ?>

<?php ob_start(); ?>

<div class="jumbotron text-center bg-secondary text-white rounded-0">
	<h1 class="display-2">Conduite</h1>
	<p class="lead">Surveillance et conduite de Robotino</p>
</div>

<div class="container margin-top">
	<div class="card rounded-0">
		<div class="card-header">
			<h5 class="card-title">Ecran de surveillance Robotino</h5>
		</div>
		<div class="card-body">
			<div class="row">
				<div class="col-lg-8">
					<img class="card-img-top" src="http://via.placeholder.com/1600x900" alt="Card image alt">
				</div>
				<div class="col-lg-4">
					<div id="static" style="height: 344px;"></div>
				</div>
			</div>
		</div>
		<div class="card-footer text-center">
			<div class="btn-group btn-group-toggle" data-toggle="buttons" id="mode">
				<label class="btn btn-info">
					<input type="radio" name="options" id="manuel" autocomplete="off"> Manuel
				</label>
				<label class="btn btn-info">
					<input checked type="radio" name="options" id="automatique" autocomplete="off"> Automatique
				</label>
			</div>
		</div>
	</div>
</div>

<script type="text/javascript" src="assets/js/nipplejs.min.js"></script>
<script type="text/javascript" src="assets/js/joystick.js"></script>

<?php $body = ob_get_clean(); ?>

<?php require('components/template.php'); ?>
