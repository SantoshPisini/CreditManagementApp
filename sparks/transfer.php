<?php
$hostname = 'localhost';
$username = 'id2263931_frs';
$password = 'frs123';
$dbname = 'id2263931_frs';
$conn = mysqli_connect($hostname, $username, $password, $dbname);
if($conn->connect_error){
  die("Connection error :".$conn->connect_error);
}
$f=$_POST["from"];
$to=$_POST["to"];
$c=$_POST["credit"];
echo '<center>Transaction under process ...</center>';

$sql="select user_credits from sparks_user where user_name='".$f."'";
$result = $conn->query($sql);
if ($result-> num_rows > 0) {
      		$json_array = array();
  while ($row = $result->fetch_assoc()) {
  	if((int)$row["user_credits"] >= (int)$c){
  	    $temp=(int)$row["user_credits"] - (int)$c;
  		$sql="UPDATE `sparks_user` SET `user_credits`=".$temp." WHERE user_name='".$f."'";
  		$conn->query($sql);
  		//deduced
  		$sql="select user_credits from sparks_user where user_name='".$to."'";
		$result = $conn->query($sql);
		$row = $result->fetch_assoc();
		$temp=(int)$row["user_credits"] + (int)$c;
		$sql="UPDATE `sparks_user` SET `user_credits`=".$temp." WHERE user_name='".$to."'";
  		$conn->query($sql);
  		//inserted
  		  $json_array[] = array('status' => 'Transaction Success.');

  	}else{
  		//not enough credits
  $json_array[] = array('status' => 'Transaction Failed.Please check if suffucient credits are available.');
  	}
  }
}
$json_array[] = array('status' => 'select error');
echo json_encode($json_array);
return json_encode($json_array);
?>