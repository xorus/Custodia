<?php 
require 'autoload.php';
$db = new Database();
$session = Session::getInstance();
$auth = new Auth($session);

if (!empty($_POST))
{
	$pseudo = htmlspecialchars($_POST['pseudo']);
	$password = htmlspecialchars($_POST['password']);
	$auth->login($db, $pseudo, $password);
	
}

?>
<?php $title = "Se connecter" ?>

<?php ob_start(); ?>

<div class="jumbotron text-center bg-secondary text-white rounded-0">
	<h1 class="display-2">Connexion</h1>
</div>

<div class="container">
	<form method="post">
	  <div class="form-group">
	    <label for="pseudo">Se connecter</label>
	    <input type="text" name="pseudo" class="form-control" id="pseudo" placeholder="Pseudo">
	  </div>
	  <div class="form-group">
	    <label for="password">Mot de passe</label>
	    <input type="password" class="form-control" id="password" name="password" placeholder="Mot de passe">
	  </div>
	  <button type="submit" class="btn btn-secondary btn-raised">Se connecter</button>
	</form>
</div>

<?php $body = ob_get_clean(); ?>

<?php require 'components/template.php' ?>
