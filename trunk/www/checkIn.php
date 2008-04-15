<?
include ("includes/config.php");
include ("includes/message.php");  
$page = "Check In";
addJavaScript("checkIn.js");
addJavaScript("message.js");
addJavaScript("MochiKit/MochiKit.js");
addJavaScript("sortable_tables.js");
include ("includes/header.php"); 

if (isset($_GET['do']) && isset($_GET['checkInID'])){
	if ($_GET['do']=="remove")
	{
		$query = "delete from checkIns where _id = ${_GET['checkInID']}";
		$result = mysql_query($query) or trigger_error(mysql_error());
	}
}
if (isset($_GET['checkInID']) && isset($_GET['patientID'])) {
	$query = "update checkIns set patientID = ${_GET['patientID']} WHERE _id = ${_GET['checkInID']}";
	$result = mysql_query($query) or trigger_error(mysql_error());
}
//need to use GET
if (isset($_POST['checkInName'])) {
	$name = ucwords(escape_data($_POST['checkInName']));

// TODO: Make date scalable
	if ( get_info('checkIns', '_id', "WHERE name = '$name' AND date > $date - 12*60*60 ")) 
	{ // alert duplicate name, but allow still 
		echo "<div class=w>This Name May Be A Duplicate Entry</div>";
	}
	
	$notes = escape_data($_POST['checkInNotes']);

	insert('checkIns', "name, complaint, date", "'$name', '$notes', NOW() ");
	$checkInID = mysql_insert_id();

	echo "<div class=w>Patient Added</div>";
}
?>

<div style="text-align:center; padding-bottom: 20px;">	
	 <form method="post" action="checkIn.php" onsubmit="return false" name="checkIn">
	 
<table border="0" width="100%">
<tr><td valign="top">Patient Name:</td>
<td><input id="checkInName" name="checkInName" type="text" autocomplete="off"  class="input" value="" onkeydown="nameSearch(event)" style="width:20em;"><br>
<DIV ID="nameHints" STYLE="position:absolute;background-color:white;layer-background-color:white;text-align:left;width:220px;"></DIV>
	<i style="color:#888">e.g. John Smith</i></td> 
<td valign="top">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Chief Complaint:</td>
<td valign="top"><? messageSearch("checkInNotes", "checkInNotes"); ?></td>
<td  valign="top"><input type="button"  value="Check-In" onclick="checkInAddNew( $F('checkInName'), $F('checkInNotes') );" /></td>
	</form>
</table>
</div>
<script language="javascript">document.checkIn.checkInName.focus();</script>
<div >
	<table id="sortable_table" class="datagrid" width="100%" border="0" cellspacing="0" cellpadding="2" >
<?
// get all check in for today 
$query = "SELECT * FROM checkIns WHERE date > NOW() - INTERVAL 1 DAY ORDER BY date ASC"; // find checkin from last 12 hours and status is not done
$checkIns = mysql_query ($query) or trigger_error("Query: $query\n<br />MySQL Error: " . mysql_error());
$counter = 0;
if (mysql_num_rows($checkIns) != 0)
{
?>
<thead> 
<tr> 
<th mochi:format="str" width="180" align="left">Name</th>
<th mochi:format="str" width="80" align="left">Birth Date</th>
<th width="80" align="left">Patient ID</th>
<th width="80" align="left">Time</th>
<th mochi:format="str" width="160" align="left">Chief Complaint</th>
<th width="80" align="left">Alerts</th>
<th width="">Actions</th>
<th width="">Visit Status</th>
<th width="">Remove</th>
</tr> 
</thead> 	
<tbody id="checkInTable" bgcolor="#FFFFFF">
<?
	while ($checkIn = mysql_fetch_assoc($checkIns)) {
	
		$checkInID = $checkIn['_id']; // checkin id
		$patientID = $checkIn['patientID']; // checkin id
		$time = $checkIn['date'];
		$status = ($checkIn['status']) ? 'Seen' : 'Incomplete';
		// TBC Alert
		$alert ="";
		
		// now get the patient from database 
		$name_array = explode(" ", $checkIn['name']);
		if (count($name_array) > 2) $name_array[1] = $name_array[count($name_array)-1]; // if they entered middle name(s), look at the last name and first only
		if (!isset($name_array[1])) $name_array[1] = ''; // correct for only one name
		
		if (is_numeric($patientID)) {
			$query = "select * from patients WHERE _id=$patientID ";
		} 
		else {
			$query = "select * from patients WHERE firstName = '{$name_array[0]}' AND lastName = '{$name_array[1]}' ";
		}
		$patientSearch = mysql_query($query);
		$count = mysql_num_rows($patientSearch); // how many of this guy is there?
					
		
		$patient = mysql_fetch_assoc($patientSearch);
		// set this check-ins patient id if it isn't already
		if (!is_numeric($patientID) && ($count ==1)) {
			$query = "update checkIns set patientID = ".$patient['_id']." where _id=$checkInID";
			mysql_query($query) or trigger_error(mysql_error());
		}
		
		$patientID = $patient['_id'];
		
		$action =  "<a href=\"patient.php?patientID=$patientID\">View Patient History</a><br>";
		$notes =$checkIn['complaint'];

		if ($count <= 1) { 
			if ($count==0) {
				$name = $checkIn['name'];
				$action = "<input type=button value='Enroll' onclick=\" location.href=('enroll.php?name={$checkIn['name']}&checkInID={$checkInID}') \" /> ";					  
				$bgcolor="#b8cbe4";
				$patientID = 'New';		
				$dateOfBirth = 'New';
				$patientID="";				
			}
	 		else { // exact match
				$dateOfBirth = $patient['dateOfBirth'];
				$name =$patient['firstName'] ." " .$patient['middleName']. " ". $patient["lastName"];
	 		
	 			$action = "<a href=\"visitPlanner.php?patientID=$patientID\">Print Visit Planner</a><br>" . $action;
				$action .= "<a href=\"clinicVisit.php?patientID=$patientID\">Enter Clinic Visit</a>"; 
		 		if ($counter % 2 == 0)
				{ 
					$bgcolor="#FFFFFF";
				}
				else
				{
					$bgcolor="#f2f2f2";
				}
	 		}
	 		checkInRow($checkInID, $name,$dateOfBirth,$patientID,$time,$notes,$action,$status,$bgcolor,$alert);
		}
		else { // Multiple found found
			$bgcolor="#e3b1ad";
			$firstRow=true;
			while ($patient)
			{
				$name = $patient['firstName'] . " " . $patient['lastName'];
				$dateOfBirth = $patient['dateOfBirth'];
				$checkInID = $checkIn['_id']; // checkin id
				$patientID = $patient['_id']; // checkin id

				$action =  "<a href=\"patient.php?patientID=$patientID\">View Patient History</a><br>";
				$status = "<a href=\"checkIn.php?patientID=$patientID&checkInID=$checkInID\"><strong>Check In</strong></a>";
				
				$time = $checkIn['date'];
				if (!$firstRow)
				{	
					$notes = "";	
				}
				checkInRow($checkInID, $name,$dateOfBirth,$patientID,$time,$notes,$action,$status,$bgcolor,"");
				$firstRow=false;
				$patient = mysql_fetch_assoc($patientSearch);				
			}
		}
		$counter ++;
	}
}
else {
	echo "<tr id=\"checkInNoneFound\"><td colspan=4>No Records Found</td></tr>";
}

function checkInRow($checkInID, $name,$dateOfBirth,$patientID,$time,$notes,$action,$status,$bgcolor,$alerts)
{
	echo "<tr bgcolor=\"$bgcolor\">";
	if (!is_numeric($patientID)) {
		
	}
	echo "<td class=checkInName><a href=\"javascript:checkInPatient('$name',$checkInID,$patientID)\">$name</a></td>";
	echo "<td>{$dateOfBirth}</td>";
	echo "<td>{$patientID}</td>";
	echo "<td>$time</td>";
	echo "<td>$notes</td>"; // visit reason
	echo "<td>$alerts</td>"; // alerts
	echo "<td class=\"action_cell\" nowrap=\"1\">$action</td>"; // all actions in this cell
	echo "<td id=\"CheckStatus$checkInID\">$status</td>";
	echo "<td class=\"action_cell\"><input type=button value='Remove' onclick=\" location.href='checkin.php?do=remove&checkInID=$checkInID'; \" /></td>";
	echo "</tr>";
}
?>

</tbody>
    </table>
	
</div>

<? include ("includes/footer.php"); ?> 
