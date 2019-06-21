<?php
$hostname = 'localhost';
$username = 'id2263931_frs';
$password = 'frs123';
$dbname = 'id2263931_frs';

$connection = mysqli_connect($hostname, $username, $password) or die("Cannot connect to " . $hostname . " with username " . $username);

mysqli_select_db($connection, $dbname) or die("Cannot connect to database " . $dbname);
if($connection->connect_error)
{
	die("Error in connction " . mysqli_connect_error());
}
else
{
//	echo "<br><h3>Connection success.....</h3>";
}
$sql = "SELECT `user_id`, `user_name`, `user_email`, `user_phone`, `user_credits` FROM `sparks_user` WHERE 1";
$result = mysqli_query($connection,$sql);
$json_array = array();
while($row = mysqli_fetch_array($result))
{
	$json_array[] = array('user_id' => $row['user_id'],'user_name' => $row['user_name'],'user_email' => $row['user_email'],'user_phone' => $row['user_phone'],'user_credits' => $row['user_credits']);
}
echo json_encode($json_array);
return json_encode($json_array);
mysqli_close($connectioin);

?>