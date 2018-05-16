<?php
	class RSOCManager extends Model {

		public function getParticipants(){
			$sql = "Select * from participant";
			$results= $this-> executerRequete($sql);
			return $results;
		}
		public function getParticipantFromId($id){
			$sql = "Select * from participant where id = '$id' ";
			$results= $this-> executerRequete($sql);
			return $results;
		}

		/*public function getPage($id)
		{
			$sql = "SELECT * FROM pages WHERE id = '$id'";
			$res = $this->executerRequete($sql);
			return $res;
		}*/

		public function getImage($id)
		{
			$sql = "SELECT fichier FROM images WHERE id = '$id'";
			$res = $this->executerRequete($sql);
			return $res;
		}
		public function addMessage($name,$firstname,$mail,$sujet){
			$sql = "insert into message (nom,prenom,mail,sujet) values ('$name','$firstname', '$mail','$sujet')";
			$res = $this->executerRequete($sql);
		}
		public function getUtilisateurs(){
			$sql = "Select * from utilisateur";
			$results= $this-> executerRequete($sql);
			return $results;
		}

		
	}

?>