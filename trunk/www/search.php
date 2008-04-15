<?
include ("includes/config.php"); 
$page = "Search";
addJavaScript("json2.js");

include ("includes/header.php"); 

$name = escape_data ($_GET['name']);
$name_parsed = '';

// do IN BOOLEAN MODE by adding '+' to q	
$name_array = explode (' ',$name);
foreach ($name_array AS $value) {
	$name_parsed .= ' +'.$value.'*';
}
	

$query = "SELECT patients.* FROM patients WHERE MATCH (firstName, lastName) AGAINST ('$name_parsed' IN BOOLEAN MODE) ORDER BY firstName ASC LIMIT 100";
$result = mysql_query ($query) or trigger_error("Query: $query\n<br />MySQL Error: " . mysql_error());
$counter = 0;
echo "<table id=sortable_table class=datagrid width=100% border=0 cellspacing=1 cellpadding=2 >";


echo "
<thead bgcolor=#ddd> 
<tr> 


<th align=left>ID</th>
<th mochi:format=str align=left>First Name</th>
<th mochi:format=str align=left>Last Name</th>
<th mochi:format=str align=left>Gender</th>
<th mochi:format=isodate align=left>Birth Date</th>
<th align=left>SSN</th>
<th mochi:format=isodate align=left>Reg Date</th>
<th>&nbsp;</th>

</tr> 
</thead> 
";

if (isset($_GET["checkInID"]))
{
	$checkInID = $_GET["checkInID"];
}

echo "<tbody>";

while ($patient = mysql_fetch_array($result, MYSQL_ASSOC)) {
	$ID = $patient['_id'];
	$class = ($counter % 2 == 0) ? "style='background-color:white'" : "style='background-color:white'";

echo "<tr>";
echo "<td class=data_row_{$ID} onmouseover=highlight({$ID}); onmouseout=unhighlight({$ID});>{$ID}</td>";
echo "<td class=data_row_{$ID} onmouseover=highlight({$ID}); onmouseout=unhighlight({$ID});>{$patient['firstName']}</td>";
echo "<td class=data_row_{$ID} onmouseover=highlight({$ID}); onmouseout=unhighlight({$ID});>{$patient['lastName']}</td>";
echo "<td class=data_row_{$ID} onmouseover=highlight({$ID}); onmouseout=unhighlight({$ID});>";
echo ($patient['gender'] == 1) ? 'Male' : 'Female';
echo "</td>";
echo "<td class=data_row_{$ID} onmouseover=highlight({$ID}); onmouseout=unhighlight({$ID});>{$patient['dateOfBirth']}</td>";
echo "<td class=data_row_{$ID} onmouseover=highlight({$ID}); onmouseout=unhighlight({$ID});>";
if ($patient['SSN'] != 0) echo substr($patient['SSN'],0,3) . "-" . substr($patient['SSN'],3,2) . "-" . substr($patient['SSN'],5,4);
else echo "-";
echo  "</td>";
echo "<td class=data_row_{$ID} onmouseover=highlight({$ID}); onmouseout=unhighlight({$ID});>" . date ("Y-m-j", strtotime($patient['createDate'])) . "</td>";
echo "<td class='data_row_{$ID} action_cell' onmouseover=highlight({$ID}); onmouseout=unhighlight({$ID});>";
if (isset($checkInID))
{
	echo "<input type=button onclick=\"location.href=('checkIn.php?checkInID=$checkInID&patientID={$ID}')\" value='Select' />";
}
echo "<input type=button onclick=\"location.href=('patient.php?patientID={$ID}')\" value='View' />";	
echo "</td>";
echo "</tr>";

$counter ++;
}

echo "</tbody> </table>";

if ($counter == 100) echo "<br><br>More than 100 results were found. Please narrow your search.";

if ($counter == 0) echo "<br><br>No patients found. <a href='enroll.php?name=$name'>Click here to enroll $name as a new patient.</a>";
else echo "<br><br><input type=button onclick=\"location.href=('enroll.php?name=$name')\" value='Enroll $name as a New Patient' />";

include ("includes/footer.php"); 

?>