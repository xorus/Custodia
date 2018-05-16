<div class="jumbotron my-jumbotron rounded-0">
  <h1>Contact</h1>
</div>


 <div class="container" id="contenu" align="center">

<style>
* {
    box-sizing: border-box;
}

input[type=text], select, textarea {
    width: 100%;
    padding: 12px;
    border: 1px solid #ccc;
    border-radius: 4px;
    resize: vertical;
}

label {
    padding: 12px 12px 12px 0;
    display: inline-block;
}


.container{
    border-radius: 5px;
    background-color: #f2f2f2;
    padding: 20px;
}
</style>


<?php
  require_once('lespages/Model/Model.php');
  require_once('lespages/Model/RSOCManager.php');
  if(!empty($_POST)){
    $nom = htmlspecialchars($_POST['firstname']);
    $prenom = htmlspecialchars($_POST['lastname']);
    $adresseMail = htmlspecialchars($_POST['mail']);
    $sujet = htmlspecialchars($_POST['subject']);
    $rsoc = new RSOCManager();
    $users = $rsoc->getUtilisateurs();
    $rsoc->addMessage($nom,$prenom,$adresseMail,$sujet);
    $message = wordwrap($sujet, 70, "\r\n");
    while($row = mysql_fetch_assoc($users)){
      mail($row['mail'],'New message from '.$prenom.' '.$nom.' mail '.$adresseMail, $message);
    }
  }

?>

<p>Si vous avez des questions particulières, vous pouvez nous envoyer un mail grâce au formulaire suivant.</br>
Nous vous répondrons le plus vite possible.</p>

<div class="container">
  <form method="post">
    <div class="row">
      <div class="col-25 form-title">
        <label for="fname">Nom</label>
      </div>
      <div class="col-75 form-content">
        <input type="text" id="fname" name="firstname" placeholder="Votre nom..." required>
      </div>
    </div>
    <div class="row">
      <div class="col-25 form-title">
        <label for="lname">Prenom</label>
      </div>
      <div class="col-75 form-content">
        <input type="text" id="lname" name="lastname" placeholder="Votre prénom..." required>
      </div>
    </div>
    <div class="row">
      <div class="col-25 form-title">
        <label for="lname">Adresse mail</label>
      </div>
      <div class="col-75 form-content">
        <input type="text" id="mail" name="mail" placeholder="Votre adresse mail..." required>
      </div>
    </div>
    <div class="row">
      <div class="col-25 form-title">
        <label for="subject">Message</label>
      </div>
      <div class="col-75 form-content">
        <textarea id="subject" name="subject" placeholder="Votre message.." required></textarea>
      </div>
    </div>
    <div class="row">
      <input type="submit" value="Envoyer" class="btn btn-primary send">
    </div>
  </form>
</div>
 </div>