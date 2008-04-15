<?php
include ("includes/config.php"); 
require_once("includes/measurement.php");
// ADD NEW
if (isset($_POST['add']))
{
	$name = $_POST['name'];
	$query = "insert into measurementType(name) values('$name')";
	$result = mysql_query($query);
	$measurementTypeID = mysql_insert_id();
	header("Location : measurementType.php?measurementTypeID=$measurementTypeID");
}


$page = "Measurement Types";
addJavaScript("MochiKit/MochiKit.js");
addJavaScript("sortable_tables.js");
include ("includes/header.php");

function measurementTypeSelect($selected)
{	
	$query = "select * from measurementType order by name asc";
	$measurementTypes=mysql_query($query);
	$output = "<select name=\"measurementTypeID\"><option></option>";
	while ($measurementType = mysql_fetch_array($measurementTypes, MYSQL_ASSOC)) {
		$extraText="";
		if ($measurementType['_id']==$selected) $extraText=" selected";
		$output .= "<option value=\"${measurementType['_id']}\"$extraText>${measurementType['name']}</option>";
	}
	$output .= "</select>";
	return $output;
}

$measurementTypeID="";		
if (isset($_GET['measurementTypeID']) and $_GET['measurementTypeID'] != '')
{
	$measurementTypeID = $_GET['measurementTypeID'];
	$query = "select * from measurementType where _id = $measurementTypeID";
 	$result = mysql_query($query) or trigger_error(mysql_error());
	$row = mysql_fetch_array($result, MYSQL_ASSOC);
	$measurementTypeName = $row['name'];
}
$search='';
if (isset($_GET['search']))
{
	$search = $_GET['search'];
}

echo "<p><form action=\"measurementTypes.php\" method=\"get\">" .
	"Search:<input type=\"text\" name=\"search\" value=\"$search\">&nbsp;&nbsp;".
	"Type:".measurementTypeSelect($measurementTypeID)."&nbsp;&nbsp; <input type=\"submit\" value=\"Search\">" .
	"</form></p>" ;

	$query ="select measurementType.* " .
			"from measurementType ";
	
	if ($search != '') {
			$query .= " where measurementType.name like '%$search%'";
	}
	
	if ($measurementTypeID != "")
	{
		$query .= " and measurementTypeID = $measurementTypeID";
	}

	$measurementTypes = mysql_query($query) or trigger_error(mysql_error());
	showMeasurementTypes($measurementTypes);


?>
<h2>Add New</h2>
<form action="measurementTypes.php" method="post">
	<input type="hidden" name="add" value="1">
	
	<table>
	<tr><td>Name</td><td><input type="text" name="name" size="55"></td></tr>
	</table>
	<input type="submit" value="Add New">

</form>
<?
include ("includes/footer.php"); 
?>