<?php
	abstract class Model{
		// Exécute une requête SQL éventuellement paramétrée
		protected function executerRequete($sql) {
			include 'lespages/bd_app.php';
			$conn = mysql_connect($mysqlHost.':'.$mysqlSocket,$mysqlUsername,$mysqlPassword);
			mysql_select_db($mysqlDatabase, $conn);
			mysql_query("SET NAMES UTF8");
			$resultat= mysql_query($sql);
			return $resultat;
		}
	}
?>
