<?php
include ("includes/config.php"); 
addJavaScript("message.js");
if (isset($_GET['demographicID']))
{
	$demographicID=$_GET['demographicID'];
} else trigger_error("No demographicID supplied");
$page="Editing Demographic $demographicID";
include ("includes/header.php");
require_once("includes/demographic.php"); 
/*
 * Created on September 25, 2007
 *
 */




if (isset($_POST['name']))
{
	$name = escape_data($_POST['name']);
	//$demographicType = escape_data($_POST['demographicType']);
	//$demographicsSection = escape_data($_POST['demographicSection']);
	//$demographicGroup = escape_data($_POST['demographicGroup']);
	$demographicTypeID = escape_data($_POST['demographicTypeID']);
	$demographicGroupID =escape_data($_POST['demographicGroupID']);
	$required = isset($_POST['required'])? 1: 0;
	$active = isset($_POST['active'])? 1 : 0;
	
	$query = "update demographic set name='$name', demographicTypeID=$demographicTypeID, demographicGroupID=$demographicGroupID, active=$active, required=$required where _id=$demographicID";
	
	$result = mysql_query($query) or trigger_error(mysql_error()); 
	# echo "<script language=\"JavaScript\">history.go(-2);</script>";
}

$query="SELECT d.*, dt.name typeName, dt._id typeID, dg.name groupName, dg._id groupID, ds.name sectionName, ds._id sectionID FROM demographic d" .
		" inner join demographicType dt on d.demographicTypeID = dt._id " .
		" inner join demographicGroup dg on d.demographicGroupID = dg._id " .
		" inner join demographicSection ds on ds._id = dg.demographicSectionID " .
		" WHERE d._id = " . $demographicID;	
		//" order by dg._id asc, dg.order asc";
$result=mysql_query($query);
$demographic=mysql_fetch_assoc($result);




?>
<form method="post" action="demographic.php?demographicID=<?=$demographicID?>" name="form1">
	<table>
	<tr><td>ID</td><td><?=$demographic['_id']?></td></tr>
	<tr><td>Name</td><td><input type="text" name="name" size="55" value="<?=$demographic['name']?>"></td></tr>
	<tr><td>Demographic Type</td><td><?=demographicTypeSelect($demographic['typeID'])?></td></tr>
	<tr><td>Demographic Section</td><td><?=$demographic['sectionName']?></td></tr>
	<tr><td>Demographic Group</td><td><?=demographicGroupSelect($demographic['groupName'])?></td></tr>
	
	<tr><td><label for="active">Active</label></td><td><input type="checkbox" name="active" value="1"<?if ($demographic['active']) echo " checked"; ?>>
	</table>
<input type="submit" value="update">
</form>
<? 
include ("includes/footer.php"); 
?>
