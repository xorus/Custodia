<?php

class Auth
{
	private $session;

	public function __construct($session)
	{
		$this->session = $session;
	}

	public function login($db, $pseudo, $password)
	{
		$res = $db->query("SELECT * FROM user WHERE pseudo = '$pseudo'");
		$user = mysql_fetch_assoc($res);
		if ($user['password'] == crypt($password, $user['password']))
		{
			$this->connect($user);
			header('Location: index.php');
			return $user;
		}
		else
		{
			return false;
		}
	}

	public function connect($user)
	{
		$this->session->write('auth', $user);
		$this->session->setFlash('success', "Vous êtes bien connecté");
	}

	public function logout()
	{
		$this->session->delete('auth');
		$this->session->close();
		header('Location: index.php');
		exit();
	}

	public function restrict()
	{
		if (!$this->session->read('auth'))
		{
			$this->session->setFlash('danger', "Vous devez être connecté pour accéder à cette page");
			header('Location: index.php');
			exit();
		}
	}
}