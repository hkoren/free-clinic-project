<?php
include ("includes/config.php"); 
$page="Demographics";
include ("includes/header.php"); 
include ("includes/demographic.php"); 
/*
 * Created on May 24, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
 
 if (isset($_POST['name']))
{
	$name = escape_data($_POST['name']);
	$demographicGroupID = $_POST['demographicGroupID'];
	$demographicTypeID = $_POST['demographicTypeID'];
	$query = "insert  into demographic(name, demographicGroupID, demographicTypeID)".
	" values('$name', $demographicGroupID, $demographicTypeID)";
	$result = mysql_query($query) or trigger_error(mysql_error()); 
	//echo "<script language=\"JavaScript\">history.go(-2);</script>";
}

if (isset($_GET['delete']))
{
	$id = $_GET["delete"];
	$query = "delete from demographic where _id = $id";
	$result = mysql_query($query) or trigger_error(mysql_error()); 
	//echo "<script language=\"JavaScript\">history.go(-2);</script>";
}

$query="SELECT d.*, dt.name typeName, dg.name groupName, ds.name sectionName FROM demographic d" .
		" inner join demographicType dt on d.demographicTypeID = dt._id " .
		" inner join demographicGroup dg on d.demographicGroupID = dg._id " .
		" inner join demographicSection ds on ds.ID = dg.demographicSectionID " .
		" order by dg._id asc, dg.order asc";
$demographics=mysql_query($query);
$demographicCount=mysql_num_rows($demographics);


echo "$demographicCount demographics found<br><br>";
echo "<table id=\"sortable_table\" class=\"datagrid\"><thead>" .
		"<tr><th mochi:format=\"str\">ID</th>".
			"<th mochi:format=\"str\">Name</th>".
			"<th mochi:format=\"str\">Type</th>".
			"<th mochi:format=\"str\">Section</th>".
			"<th mochi:format=\"str\">Group</th>".
			"<th mochi:format=\"str\">Delete</th></tr>" .
		"</thead><tbody>";

while ($demographic=mysql_fetch_assoc($demographics))
{
	$link = "demographic.php?demographicID=${demographic['_id']}";
	echo "<tr><td><a href=\"$link\">${demographic['_id']}</a></td><td>${demographic['name']}</td>".
		"<td>${demographic['typeName']}</td> <td> ${demographic['sectionName']}</a></td>".
		"<td>${demographic['groupName']}</td>".
		"<td><a href=\"demographics.php?delete=${demographic['_id']}\"> X </a></td></tr>";
}

echo "</tbody></table><br>";
?>
<h2>Add New</h2>
<form action="demographics.php" method="post">
	<input type="hidden" name="add" value="1">
	
	<table>
	<tr><td>Name</td><td><input type="text" name="name" size="55"></td></tr>
	<tr><td>Type</td><td><?=demographicTypeSelect('')?></td></tr>
	<tr><td>Section</td><td><?=demographicSectionSelect('')?></td></tr>
	<tr><td>Group</td><td><?=demographicGroupSelect('')?></td></tr>
	
	</table>
	<input type="submit" value="Add New">
</form>
<?
include ("includes/footer.php"); 
?>
