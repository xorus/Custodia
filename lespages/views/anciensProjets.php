<?php
  require_once('lespages/Model/Model.php');
  require_once('lespages/Model/RSOCManager.php');
  $rsoc = new RSOCManager();
  $image = $rsoc->getImage(2);
  $imageSelected = mysql_result($image, 0);
  $rsoc1 = new RSOCManager();
  $image1 = $rsoc1->getImage(3);
  $imageSelected1 = mysql_result($image1, 0);
?>

<div class="jumbotron my-jumbotron rounded-0">
  <h1>Anciens projets</h1>
</div>

<div class="container" id="contenu">

  <div class="slider">
    <div class="slides">
      <div class="slide"><img src=<?php echo "documents/" .$imageSelected1 ?> > </div>
      <div class="slide"><img src="documents/702x336.jpg"></div>
      <div class="slide"><img src="documents/cool_robots.png"></div>
    </div>
  </div>
<br>
<h5> Voici les 3 derniers projets d'APP précédents le notre. Ces Projets sont orientés Robotique de service.
En cette année 2020, le sujet de ce projet a été modifié et est devenu RS-OC, c'est à dire Robotique de Service et Objets Connectés.
Si vous souhaitez en savoir plus sur ces projets, cliquez sur "En savoir plus."</h5> 

<br>

<div class="row">
  <div class="col-lg-4">
    <div class="card">
      <img class="card-img-top" src="documents/Gnubiquity_Poster.png" alt="Card image cap" width="50" height="300px">
      <div class="card-body">
        <h5 class="card-title">Gnubiquity</h5>
        <p class="card-text">Projet de téléprésence robotisée de la promotion 2019</p>
        <a href="http://tp-epu.univ-savoie.fr/~rs_19/2019_RS/2019_RS.php" class="btn btn-primary">En savoir plus</a>
      </div>
    </div>
  </div>

  <div class="col-lg-4">
    <div class="card" >
      <img class="card-img-top" src="documents/domoteam.png" alt="Card image cap" width="50" height="300px">
      <div class="card-body">
        <h5 class="card-title">Domoteam</h5>
        <p class="card-text">Projet de kit de sécurité connectée de la promotion 2018.</p>
        <a href="documents/crdomoteam.pdf" class="btn btn-primary">En savoir plus</a>
      </div>
    </div>
  </div>

  <div class="col-lg-4">
    <div class="card" >
      <img class="card-img-top" src="<?php echo $imageSelected ?>" alt="Card image cap" width="50" height="300px">
      <div class="card-body">
        <h5 class="card-title">WatchBot</h5>
        <p class="card-text">Projet de télésurveillance robotisée de la promotion 2017.</p>
        <a href="documents/crWatchBot.pdf" class="btn btn-primary">En savoir plus</a>
      </div>
    </div>
  </div>

</div>
</div>