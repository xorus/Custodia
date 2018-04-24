<?php

namespace AppBundle\Controller;

use Sensio\Bundle\FrameworkExtraBundle\Configuration\Route;
use Symfony\Bundle\FrameworkBundle\Controller\Controller;
use Symfony\Component\HttpFoundation\Request;

class AdminController extends Controller
{

	/**
	 * Affichage de la page d'accueil admin
	 *
	 * @Route("/admin", name="homepage-admin")
	 */
	public function indexAction()
	{
		return $this->render("admin/index.html.twig");
	}
}