<!DOCTYPE html>
<html>

<head>
    <title>Custodia</title>
    <meta charset="utf-8">
    <link rel="icon" href="documents/favicon.ico" />
    <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.10/css/all.css" integrity="sha384-+d0P83n9kaQMCwj8F4RJB66tzIwOKmrdb46+porD/OvrJ+37WqIM7UoBtwHO6Nlg"
        crossorigin="anonymous">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.0/css/bootstrap.min.css" integrity="sha384-9gVQ4dYFwwWSjIDZnLEWnxCjeSWFphJiwGPXr1jddIhOegiu1FwO5qRGvFXOdJZ4" crossorigin="anonymous">
    <link rel="stylesheet" type="text/css" href="lespages/css/style.css">
    <link rel="stylesheet" type="text/css" href="lespages/css/grid.css">
    <link rel="stylesheet" type="text/css" href="lespages/css/slider.css">
        <link rel="stylesheet" type="text/css" href="lespages/css/slider-accueil.css">


</head>

<body>
    <!-- Navbar -->
    <nav class="menu">
        <ul id="menu">
            <li><a href="?page=accueil">Custodia</a></li>
            <li class="hide"><a href="?page=compteRendu"><i class="fas fa-book"></i>&nbsp; Compte-rendu</a></li>
            <li class="hide"><a href="?page=anciensProjets"><i class="far fa-clock"></i>&nbsp; Anciens projets</a></li>
            <li class="hide"><a href="?page=participants"><i class="fas fa-users"></i>&nbsp; Participants</a></li>
            <li class="hide"><a href="?page=contact"><i class="far fa-envelope"></i>&nbsp; Contact</a></li>
            <li id="menu-toggle"><a href=""><i class="fas fa-bars white"></i></a></li>         
        </ul>
    </nav>
    <main>
        <?php 
            if (!isset($_GET['page']))
            {
                $page = "accueil";
            }
            else
            {
                $page = $_GET['page'];
            }

            include("lespages/views/".$page.".php");
        ?>
    </main>

    <footer class="footer">
        <div class="row">
			<div class="col-lg-4 text-white">
				<h4 class="text-center font-weight-bold">A propos</h4>
				<hr>
				<p class="text-center">Custodia est un projet de télésurveillance robotisée de Polytech Annecy-Chambéry</p>
			</div>
			<br>
			<div class="col-lg-4 text-white text-center">
				<h4 class="font-weight-bold">Participants</h4>
				<hr>
				<p>Franck Battu - Tristan Grut - Fabien Lalande - Thibaud Murtin - Johann Pistorius - Erwan Prospert - Adrien Rybarczyk</p>
			</div>
			<br>
			<div class="col-lg-4 icons">
				<h4 class="font-weight-bold">Liens</h4>
				<hr>
				<a href=""><i class="fab fa-github fa-3x"></i></a>&nbsp;
				<a href=""><i class="fab fa-twitter fa-3x"></i></a>&nbsp;
				<a href=""><i class="fab fa-facebook fa-3x"></i></a>&nbsp;
				<a href=""><i class="fab fa-linkedin fa-3x"></i></a>
			</div>
		</div>
    </footer>

    <script>
        let button = document.querySelector('#menu-toggle');
        let menu = document.querySelector('#menu');
        let hides = document.querySelectorAll('.hide');

        let open = false;

        button.addEventListener('click', (event) => {
            event.preventDefault();
            
            if (!open) {
                menu.className = "show";
                for (i = 0; i < 4; i++) {
                    hides[i].classList.remove('hide');
                }
                open = true;
            }
            else {
                menu.className = "";
                for (i = 0; i < 4; i++) {
                    hides[i].classList.add('hide');
                }
                open = false;
            }
        });
    </script>
</body>

</html>