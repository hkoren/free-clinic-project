<?php
include ("includes/config.php"); 
$page="Users";
include ("includes/header.php"); 
include ("includes/user.php"); 
/*
 * Created on May 24, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
 
 if (isset($_POST['firstName']))
{
	$firstName = escape_data($_POST['firstName']);
	$lastName = escape_data($_POST['lastName']);
	$userTypeID = $_POST['userTypeID'];
	$query = "insert  into users(firstName, lastName, userTypeID) values('$firstName', '$lastName', $userTypeID)";
	$result = mysql_query($query) or trigger_error(mysql_error()); 
	echo "<script language=\"JavaScript\">history.go(-2);</script>";
}

$query="select * from users";
$users=mysql_query($query);
$userCount=mysql_num_rows($users);


echo "$userCount users found<br><br>";
echo "<table id=\"sortable_table\" class=\"datagrid\"><thead>" .
		"<tr><th mochi:format=\"str\">ID</th>".
			"<th mochi:format=\"str\">Email</th>".
			"<th mochi:format=\"str\">Name</th>".
			"<th mochi:format=\"str\">Title</th></tr>" .
		"</thead><tbody>";

while ($user=mysql_fetch_assoc($users))
{
	echo "<tr onclick=\"location.href='user.php?userID=${user['_id']}';\"><td>${user['_id']}</td><td>${user['email']}</td>".
		"<td>${user['firstName']} ${user['lastName']}</a></td>".
		"<td>${user['title']}</td></tr>";
}

echo "</tbody></table><br>";
?>
<h2>Add New</h2>
<form action="users.php" method="post">
	<input type="hidden" name="add" value="1">
	
	<table>
	<tr><td>First Name</td><td><input type="text" name="firstName" size="55"></td></tr>
	<tr><td>Last Name</td><td><input type="text" name="lastName" size="55"></td></tr>
	<tr><td>Type</td><td><?=userTypeSelect('')?></td></tr>
	</table>
	<input type="submit" value="Add New">
</form>
<?
include ("includes/footer.php"); 
?>
