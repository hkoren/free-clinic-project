<?php
include ("includes/config.php"); 
addJavaScript("message.js");
if (isset($_GET['userID']))
{
	$userID=$_GET['userID'];
} else trigger_error("No userID supplied");
$page="Editing User $userID";
include ("includes/header.php");
require_once("includes/user.php"); 
/*
 * Created on September 25, 2007
 *
 */




if (isset($_POST['firstName']))
{
	$email = escape_data($_POST['email']);
	$firstName = escape_data($_POST['firstName']);
	$lastName = escape_data($_POST['lastName']);
	$title = escape_data($_POST['title']);
	$active = isset($_POST['active'])? 1 : 0;
	$query = "update users set email='$email', title='$title',firstName = '$firstName', lastName='$lastName', active=$active where _id=$userID";
	$result = mysql_query($query) or trigger_error(mysql_error()); 
	# echo "<script language=\"JavaScript\">history.go(-2);</script>";
}

$query="select * from users where _id=$userID";
$result=mysql_query($query);
$user=mysql_fetch_assoc($result);
?>

<form method="post" action="user.php?userID=<?=$userID?>" name="form1">
	<table>
	<tr><td>ID</td><td><?=$user['_id']?></td></tr>
	<tr><td>Email</td><td><input type="text" name="email" size="55" value="<?=$user['email']?>"></td></tr>
	<tr><td>First Name</td><td><input type="text" name="firstName" size="55" value="<?=$user['firstName']?>"></td></tr>
	<tr><td>Last Name</td><td><input type="text" name="lastName" size="55" value="<?=$user['lastName']?>"></td></tr>
	<tr><td>Title</td><td><input type="text" name="title" size="55" value="<?=$user['title']?>"></td></tr>
	<tr><td><label for="active">Active</label></td><td><input type="checkbox" name="active" value="1"<?if ($user['active']) echo " checked"; ?>>
	</table>
<input type="submit" value="update">
</form>
<? 
include ("includes/footer.php"); 
?>
