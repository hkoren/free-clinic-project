<?php
/*
 * Created on Dec 17, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */


// basic patient table
$SSN = ($_POST['SSN']) ? str_replace('-', '', escape_data($_POST['SSN'])) : "null";
$gender = isset($_POST['gender']) ? escape_data($_POST['gender']) : "U";	
$dateOfBirth = $_POST['bd_y'] . "-" . $_POST['bd_m'] . "-" . $_POST['bd_d']; // two year birth dates are probably a bad idea
$firstName = escape_data($_POST['firstName']);
$middleName = escape_data($_POST['middleName']);	
$lastName = escape_data($_POST['lastName']);
$phoneNumber = escape_data($_POST['phone']);
$emergencyName = escape_data($_POST['emergencyName']);
$emergencyPhoneNumber = escape_data($_POST['emergencyPhoneNumber']);
$emergencyRelationship = escape_data($_POST['emergencyRelationship']);
$address1 = escape_data($_POST['address1']);
$address2 = escape_data($_POST['address2']);
$city = escape_data($_POST['city']);
$state = escape_data($_POST['state']);
$zip = escape_data($_POST['zip']);
$kidsNumber =  escape_data(mysql_int($_POST['kidsNumber']));
$kidsHome =  escape_data(mysql_int($_POST['kidsHome']));
$adultsHome =  escape_data(mysql_int($_POST['adultsHome']));

//determine if this is a new patient or not
$patientExists = isset($_POST["patientID"]);

if ($patientExists)				// UPDATE DATA
{
	$patientID =$_POST["patientID"];
	$query = "update patients set SSN={$SSN}, " .
		"dateOfBirth='{$dateOfBirth}', " .
		"gender='{$gender}', " .
		"firstName='{$firstName}', " .
		"middleName='{$middleName}', " .
		"lastName='{$lastName}',  " .
		"address1='{$address1}', " .
		"address2='{$address2}', " .
		"city='{$city}', " .
		"state='{$state}', " .
		"zip='{$zip}', " .
		"phoneNumber='{$phoneNumber}', " .
		"emergencyName='{$emergencyName}', " .
		"emergencyPhoneNumber='{$emergencyPhoneNumber}', " .
		"emergencyRelationship='{$emergencyRelationship}',  " .
		"kidsNumber=$kidsNumber, " .
		"kidsHome=$kidsHome, " .
		"adultsHome=$adultsHome " .
		"where _id=$patientID";
	mysql_query($query) or trigger_error("Query: $query\n<br />MySQL Error: " . mysql_error());
	echo "Patient Updated.";
	
}
else										// INSERT DATA
{
//		insert('patients', 'registration_date, ssn, gender, dateOfBirth', " NOW(), '$ssn', '$gender', '{$dateOfBirth}'  ");
	
	$query="insert into patients(SSN, " .
			"dateOfBirth, " .
			"gender, " .
			"firstName, " .
			"middleName, " .
			"lastName, " .
			"address1, " .
			"address2, " .
			"city, " .
			"state, " .
			"zip, " .
		 	"phoneNumber, " .
			"emergencyName, " .
			"emergencyPhoneNumber, " .
			"emergencyRelationship, " .
			"createDate, " .
			"modifiedDate, " .
			"createdBy, " .
			"kidsNumber, " .
			"kidsHome, " .
			"adultsHome) values( " .
		 	"{$SSN}, " .
			"'{$dateOfBirth}', " .
			"'{$gender}', " .
			"'{$firstName}', " .
			"'{$middleName}', " .
			"'{$lastName}',  " .
			"'{$address1}', " .
			"'{$address2}', " .
			"'{$city}', " .
			"'{$state}', " .
			"'{$zip}', " .
		 	"'{$phoneNumber}', " .
			"'{$emergencyName}', " .
			"'{$emergencyPhoneNumber}', " .
			"'{$emergencyRelationship}', " .
			"NOW(), " .
			"NOW(), " .
			"{$_SESSION['u']},  " .
			"$kidsNumber, " .
			"$kidsHome, " .
			"$adultsHome)"; 
mysql_query($query) or trigger_error("Query: $query\n<br />MySQL Error: " . mysql_error());

	// new patient id
	$patientID = mysql_insert_id();
	echo "Patient Enrolled.";
}

// Process Message Adds
processMessageAdds();

if ($debug) echo "$query<br/>";



 while ($row = mysql_fetch_assoc($demographics))
{
	$insert_head = "insert into patientDemographic(demographicID, patientID, timestamp, ";
	$insert_ids = ") values(".$row['_id'].", ".$patientID.", now(), ";
	$update_head = "update patientDemographic set timestamp=now(), ";
	$update_ids = " where patientID=$patientID and demographicID=${row['_id']}";
	if(isset($_POST[$row['_id']]))
		$value=$_POST[$row['_id']];
	else $value ="";
	
 	switch($row["typeName"])
	{
		case 'String':
			if($value != "")
			{
				if (!$patientExists|| $row['num'] == 0)
				{
					$query ="$insert_head stringValue".$insert_ids."'".escape_data($value)."')";
				}
				else
				{
					$query = "$update_head stringValue='".escape_data($value)."'".$update_ids;
				}
				mysql_query($query)or trigger_error("Query: $query\n<br />MySQL Error: " . mysql_error());
			}
			break;
		case 'Integer':
			if($value != "")
			{
				if (!$patientExists|| $row['num'] == 0)
				{
					$query ="$insert_head intValue".$insert_ids."$value)";
				}
				else
				{
					$query = "$update_head intValue=".escape_data($value).$update_ids;
				}
				mysql_query($query)or trigger_error("Query: $query\n<br />MySQL Error: " . mysql_error());
			}
			break;
		case 'Boolean':
	
			$value=($value!="" ? "1" : "0");
			if (!$patientExists || $row['num'] == 0)
			{
				$query = "$insert_head intValue".$insert_ids."$value)";
			}
			else
			{
				$query = "$update_head intValue='".escape_data($value)."'".$update_ids;
			}
			mysql_query($query)or trigger_error("Query: $query\n<br />MySQL Error: " . mysql_error());
			break;
		case 'Family History':
			$bitmask = 0;
			if (isset($_POST[$row['_id']."_s"])) 
				$bitmask += 1;
			if (isset($_POST[$row['_id']."_f"])) 
				$bitmask += 2;
			if (isset($_POST[$row['_id']."_m"])) 
				$bitmask += 4;
			
			if (!$patientExists|| $row['num'] == 0)
			{
				$query ="$insert_head intValue".$insert_ids."$bitmask)";
			}
			else
			{
				$query = "$update_head intValue=".$bitmask.$update_ids;
			}
			mysql_query($query)or trigger_error("Query: $query\n<br />MySQL Error: " . mysql_error());	
					
			break;
		case 'Usage':
			if($value!= "")
			{
				if (!$patientExists|| $row['num'] == 0)
				{
					$query ="$insert_head intValue".$insert_ids."$value)";	
				}
				else
				{
					$query = "$update_head intValue=".escape_data($value).$update_ids;
				}
				mysql_query($query)or trigger_error("Query: $query\n<br />MySQL Error: " . mysql_error());
				if ($debug) echo "$query<br/>";
			}
			break;
		case 'Sexual Preference':
			if($value!= "")
			{
				if (!$patientExists || $row['num'] == 0)
				{
					$query ="$insert_head stringValue".$insert_ids."'$value')";
				}
				else
				{
					$query = "$update_head stringValue='".escape_data($value)."'".$update_ids;
				}
				mysql_query($query) or trigger_error("Query: $query\n<br />MySQL Error: " . mysql_error());
				if ($debug) echo "<br/>$query<br/>\n";
			}	
		    break;			
	}
}	
$demographics=mysql_query($form_query) or trigger_error(mysql_error());

?>
