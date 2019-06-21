<?php
$hostname = 'localhost';
$username = 'id2263931_frs';
$password = 'frs123';
$dbname = 'id2263931_frs';
//sparks credit management app 
$connection = mysqli_connect($hostname, $username, $password) or die("Cannot connect to " . $hostname . " with username " . $username);

mysqli_select_db($connection, $dbname) or die("Cannot connect to database " . $dbname);
if($connection->connect_error)
{
    die("Error in connction " . mysqli_connect_error());
}
else
{
    $a = $_POST["user_id"];
    $b = $_POST["user_name"];
    $c = $_POST["user_email"];
    $d = $_POST["user_phone"];
    $e = $_POST["user_credits"];

    if (!($a === NULL || $d === NULL || $c === NULL || $b === NULL|| $e === NULL)) {
    $sql = "INSERT INTO `sparks_user`(`user_id`, `user_name`, `user_email`, `user_phone`, `user_credits`) VALUES ('$a','$b','$c','$d','$e')";
    echo $sql;
    if(mysqli_query($connection,$sql))
    echo 'Report Submitted Successfully.';
    else
    echo 'Submission Failed!';
        }else{
            echo "some field is empty!!";
        }
    }
mysqli_close($connection);
?>
