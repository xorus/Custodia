<?php

require_once 'autoload.php';
$session = Session::getInstance();

?>

<nav class="navbar navbar-expand-lg">
    <a class="navbar-brand" href="index.php">PolySkynet</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <i class="material-icons text-white">menu</i>
    </button>

    <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav ml-auto">
            <li class="nav-item">
                <a class="nav-link" href="#">Présentation</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="conduite.php">Conduite</a>
            </li>
            <li class="nav-item">
                <a class="nav-link" href="#">Capteurs</a>
            </li>
            <?php if ($session->read('auth')): ?>
                <li class="nav-item">
                    <a class="nav-link" href="logout.php">Déconnexion</a>
                </li>
            <?php else: ?>
                <li class="nav-item">
                    <a class="nav-link" href="login.php">Connexion</a>
                </li>
            <?php endif; ?>       
        </ul>
    </div>
</nav>