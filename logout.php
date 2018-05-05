<?php

require_once 'autoload.php';

$session = Session::getInstance();
$auth = new Auth($session);
$auth->logout();