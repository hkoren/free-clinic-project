<?php
include ("includes/config.php"); 
$page = "Administrator";
include ("includes/header.php"); 
/*
 * Created on May 24, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
?>
<div style="float:right;">
<h2>Health Care Management</h2>
<ul>
<?
	$query = "select * from messageType order by name asc";
	$messageTypes=mysql_query($query);
	while ($messageType = mysql_fetch_array($messageTypes, MYSQL_ASSOC)) {
		echo "<li><a href=\"messages.php?messageTypeID=${messageType['_id']}\">${messageType['name']}</a></ll>";		
	}
?>
</ul>
</div>
<ul>
<li><a href="measurementTypes.php">Measurement Types</a>
<li><a href="users.php">Users</a>
<li><a href="demographics.php">Demographics</a>
</ul>
<? 
include ("includes/footer.php"); 
?>
