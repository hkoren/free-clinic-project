<?php
include ("includes/config.php"); 
if (isset($_GET['measurementTypeID']))
{
	$measurementTypeID=$_GET['measurementTypeID'];
} else trigger_error("No measurementTypeID supplied");
$page="Editing measurementType $measurementTypeID";
include ("includes/header.php");
require_once("includes/measurement.php"); 
/*
 * Created on May 24, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */




if (isset($_POST['name']))
{
	$name = escape_data($_POST['name']);
	$label = escape_data($_POST['label']);
	$size = $_POST['size'];
	$unit = $_POST['unit'];
	$query = "update measurementType set name = '$name', label='$label', size=$size, unit='$unit' where _id=$measurementTypeID";
	$result = mysql_query($query) or trigger_error(mysql_error()); 
	echo "<script language=\"JavaScript\">history.go(-2);</script>";
}

$query="select * from measurementType where _id=$measurementTypeID";
$result=mysql_query($query);
$measurementType=mysql_fetch_assoc($result);
?>

<form method="post" action="measurementType.php?measurementTypeID=<?=$measurementTypeID?>">
	<table>
	<tr><td>ID</td><td><?=$measurementType['_id']?></td></tr>
	<tr><td>Name</td><td><input type="text" name="name" size="55" value="<?=$measurementType['name']?>"></td></tr>
	<tr><td>Label</td><td><input type="text" name="label" size="55" value="<?=$measurementType['label']?>"></td></tr>
	<tr><td>Size</td><td><input type="text" name="size" size="55" value="<?=$measurementType['size']?>"></td></tr>
	<tr><td>Unit</td><td><input type="text" name="unit" size="10" value="<?=$measurementType['unit']?>"></td></tr>
	</table>
<input type="submit" value="update">
</form>

<h2></h2>
<? 
include ("includes/footer.php"); 
?>
