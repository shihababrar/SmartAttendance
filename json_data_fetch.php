<?php
$servername = "localhost";
$username = "jachaibd";
$password = "#89g9p@!l2qVA$";
$dbname = "jachaibd_lict_nsu";

// Create connection
$conn = new mysqli($servername, $username, $password,$dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}
else {
$userid= $_POST['userId'];
$userpassword= $_POST['userPassword'];
$sql = " SELECT * FROM SmartAttendance WHERE userId=('$userid') AND userPassword=('$userpassword') ";
$result = mysqli_query($conn,$sql);
$json_array = array();
while ($row = mysqli_fetch_assoc($result)) {
	$json_array[]= $row;
}
echo json_encode(array('user_info' => $json_array));
}
mysqli_close($conn);
?>