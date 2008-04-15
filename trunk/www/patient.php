<?

include ("includes/config.php"); 
addJavaScript("MochiKit/MochiKit.js");
addJavaScript("sortable_tables.js");
addJavaScript("patient.js");
$page="Patient Information";
include("includes/header.php"); 
include("includes/measurement.php");
include("includes/message.php");

if (isset($_GET['patientID'])) $patientID = (int) $_GET['patientID'];

else { echo "<div class=message>Patient not found.</div>"; exit(); }

$query = "SELECT patients.*, genders.name as genderName from patients " .
		" left outer join genders on patients.gender = genders._id where patients._id=$patientID";
$result = mysql_query ($query) or trigger_error("Query: $query\n<br />MySQL Error: " . mysql_error());
$patients = mysql_fetch_array( $result, MYSQL_ASSOC );

?>

<div class=message_top><a href="javascript:minAll()">Minimize All</a> &nbsp; <a href=javascript:maxAll()>Maximize All</a>  &nbsp; <a href=javascript:resetPlacement()>Reset Placement</a></div>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tbody >
  <tr>
    <td id=column_left width="400" valign="top">
	
<div id=column_1>
<?

// Patient Info


$class = 'table_yellow';
include('patientInfo.php');

// Allergies

// Current Problems

// Chronic Problems

// Procedures

// Hospitalizations

// Surgeries

// Messages
echo "<h3>messages</h3>";
showPatientMessages($patientID);


// Measurements
echo "<h3>measurements</h3>";
showPatientMeasurements($patientID);

?>






	
</div>	
	
	</td>
    <td width="20">&nbsp;</td>
	<td id=column_right valign="top"> 


<div id=column_2>	
	
<?
// Percriptions

// Medication

// Labs

// Health Care Management

// Chronic Disease Maintenence

// Referrals

// Billing

// Documents 


?>


</div>

	
	</td>
  </tr>
</tbody>
</table>



<? include ("includes/footer.php"); ?> 
