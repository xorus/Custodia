<?php 
require 'autoload.php';
$session = Session::getInstance();
?>

<?php $title = "Accueil" ?>

<?php ob_start(); ?>

<div class="container">
</div>

<?php $body = ob_get_clean(); ?>

<?php require('components/template.php'); ?>