<?php

class Database
{
	private $connection;

	public function __construct()
	{
		$this->connection = mysql_pconnect("localhost:/var/run/mysql/mysql_tp.sock", "battuf", "zsan78hw");
		mysql_query("SET NAMES UTF8");
		mysql_select_db("battuf");
	}

	public function query($query)
	{
		$res = mysql_query($query);
		return $res;
	}
}