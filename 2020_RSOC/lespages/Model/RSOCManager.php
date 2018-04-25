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

		public function getPage($id)
		{
			$sql = "SELECT * FROM pages WHERE id = '$id'";
			$res = $this->executerRequete($sql);
			return $res;
		}
	}

?>