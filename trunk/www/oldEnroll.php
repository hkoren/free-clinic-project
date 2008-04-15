<?
require_once("includes/config.php");
require_once("includes/message.php");
$page = "Enrollment";
addJavaScript("form.js");
addJavaScript("CalendarPopup.js");

$extraHead .="<SCRIPT LANGUAGE=\"JavaScript\">document.write(getCalendarStyles());</SCRIPT>";
require_once("includes/header.php");

require_once("includes/validator.mod.php");


$oValidator = new Validator(); // initialize empty validator

$bValidationFailed = false;
$aFailed = array();
$bReadOnly = false;
$patientID="";
/*
function checkbox($name, $default=""){
	if (isset($_POST[$name]))
	{
		return "";
	}
	else 
	{
		return $default;
	}	
}*/

// echo "\"".$_POST['ln']."\"";

// enrolling new patient
if (isset($_POST['submitted_enroll'])) {

	
	// escape $_POST and save in $post
	post_clean();


	// invoke validation 
	
	
	//addValidator($sFormField, $iValidateType, $sErrorMessage) 

	// validate the person's name
	$oValidator->addValidator("firstName", array("alpha", "nonempty"), "You must enter a valid first name.");
	$oValidator->addValidator("middleName", "alpha", "You must enter a valid middle name.");
	$oValidator->addValidator("lastName", array("alpha", "nonempty"), "You must enter a valid last name.");
	
	// validate birth date
	$oBirthV = &$oValidator->addValidator("bd_m", array("num", "nonempty"), "You must enter a valid birth month.");
	$oBirthV->addValidationType("lt", 12);
	$oBirthV = &$oValidator->addValidator("bd_d", array("num", "nonempty"), "You must enter a valid birth day.");
	$oBirthV->addValidationType("lt", 31);
	$oBirthV = &$oValidator->addValidator("bd_y", array("num", "nonempty"), "You must enter a valid birth year.");
	$oBirthV->addValidationType("gt", 1900);
	$oBirthV->addValidationType("lt", 2099);
	 
	// validate SSN
	$oSSNV = $oValidator->addValidator("SSN",array("ssn","num"),"You must enter a valid social security number.");	
		
	// validate phone 
	$oValidator->addValidator("phone", "phone", "You must enter a valid phone number.");
	 
	// validate address? 
	 
	// validate emergency contact
	$oValidator->addValidator("emergencyName", array("alpha"), "You must enter a valid emergency contact.");
	$oValidator->addValidator("emergencyPhoneNumber", array("phone"), "You must enter a valid emergency contact phone number.");
	$oValidator->addValidator("emergencyRelationship", array("alpha"), "You must enter a valid emergency contact relationship.");

	$oValidator->addValidator("kidsNumber", "num", "You must enter a valid number of kids.");
	$oValidator->addValidator("kidsHome", "num", "You must enter a valid number of kids that live at home.");
	$oValidator->addValidator("adultsHome", "num", "You must enter a valid number of kids that live at home.");
	$oValidator->addValidator("motherDeathAge", "num", "You must enter a valid mother death age");
	$oValidator->addValidator("fatherDeathAge", "num", "You must enter a valid father death age");
	
	$oValidator->addValidator("income", "num", "You must enter a valid monthly income");
	
	
	// further validation later, when they actually store to DB


	// validate all before storing
	$isValid = (count($aFailed = $oValidator->validateAll()) == 0);





	if ($isValid) {	
		// basic patient table
		$SSN = ($_POST['SSN']) ? str_replace('-', '', escape_data($_POST['SSN'])) : "null";
		//$dateOfBirth = ($_POST['dob']) ? date("Y-m-d 01:01:01",  strtotime( str_replace('-','/', $_POST['dob']) ) ) : '';
		//$dateOfBirth = escape_data($_POST['dateOfBirth']);
		$gender = isset($_POST['gender']) ? escape_data($_POST['gender']) : "U";
		
		// need check to make sure patient is not already enrolled
		
		$dateOfBirth = $post['bd_y'] . "-" . $post['bd_m'] . "-" . $post['bd_d']; // two year birth dates are probably a bad idea
		
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
		
		$motherDeathCause = escape_data($_POST['motherDeathCause']);
		$motherDeathAge = escape_data(mysql_int($_POST['motherDeathAge']));
		$fatherDeathCause = escape_data($_POST['fatherDeathCause']);
		$fatherDeathAge = escape_data(mysql_int($_POST['fatherDeathAge']));
		
		
		
		
		// need education, gender, ethnicity, primary language, martial status,
		// documentation, assets, monthly income (strip $ and ,)
		
		
		if (isset($_POST["patientID"]))				// UPDATE DATA
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
				"adultsHome=$adultsHome, " .
				"motherDeathCause='$motherDeathCause', " .
				"fatherDeathCause='$fatherDeathCause', " .
				"motherDeathAge=$motherDeathAge, " .
				"fatherDeathAge=$fatherDeathAge " .
				"where _id=$patientID";
			//echo $query;
			mysql_query($query) or trigger_error(mysql_error());
			echo "Patient Updated.";
			
		}
		else										// INSERT DATA
		{
	//		insert('patients', 'registration_date, ssn, gender, dateOfBirth', " NOW(), '$ssn', '$gender', '{$dateOfBirth}'  ");
			
			insert("patients", "SSN, " .
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
					"adultsHome, " .
					"motherDeathCause, " .
					"fatherDeathCause, " .
					"motherDeathAge, " .
					"fatherDeathAge",
				 	"{$ssn}, " .
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
					"'{$emergencyPhone}', " .
					"'{$emergencyRelationship}', " .
					"NOW(), " .
					"NOW(), " .
					"{$_SESSION['u']},  " .
					"$kidsNumber, " .
					"$kidsHome, " .
					"$adultsHome, " .
					"'$motherDeathCause', " .
					"'$fatherDeathCause', " .
					"$motherDeathAge, " .
					"$fatherDeathAge"); 
		
		
			// new patient id
			$patientID = mysql_insert_id();
			echo "Patient Enrolled.";
		}
		
		// Process Message Adds
		processMessageAdds();
		
	
		
		// need to prevent double submit!
	
	
	
		// redirect;
		//header("Location: index.php?p=patient&id=$ID");
		
		print "<script type=\"text/javascript\"> window.location='clinicVisit.php?patientID={$patientID}'; </script>";
	
	
	} else {
		
		// validation failed!
		
		$bValidationFailed = true;
		
	}
}




if (isset($_GET['name'])) {
	$name_array = explode(' ', $_GET['name'] );
	
	if (count($name_array) == 1) {
		$firstName = $name_array[0];
	} 	
	elseif (count($name_array) == 2) {
		$firstName = $name_array[0];
		$lastName = $name_array[1];
	} elseif (count($name_array) == 3) {
		$firstName = $name_array[0];
		$middleName = $name_array[1];
		$lastName = $name_array[2];
	}
	
}

$patient="";
// in view mode, not enrolling new patient
if ((isset($_GET['patientID']) && $_GET['patientID'] != "")  || (isset($_POST['patientID']) && $_POST['patientID'] != "")) {
	if (isset($_GET['patientID'])) $patientID = (int) $_GET['patientID'];
	if (isset($_POST['patientID'])) $patientID = (int) $_POST['patientID'];
	
	$query = "select UNIX_TIMESTAMP(dateOfBirth) as DOBtimestamp, patients.* from patients WHERE _id = $patientID";
	$patientResults=mysql_query($query) or trigger_error(mysql_error());
	if (mysql_num_rows($patientResults)==0) trigger_error("patientID = $patientID is INVALID");
	$patient=mysql_fetch_assoc($patientResults);
	
	// fill all of these
	
	$SSN = $patient['SSN'];
	$dateOfBirth = $patient['dateOfBirth'];	
	$DOBtimestamp = $patient['DOBtimestamp'];
	$firstName = $patient['firstName'];
	$middleName = $patient['middleName'];
	$lastName = $patient['lastName'];
	$address1 = $patient['address1'];
	$address2 = $patient['address2'];
	$city = $patient['city'];
	$state = $patient['state'];
	$zip = $patient['zip'];
	$bd_m = date("n",$DOBtimestamp);
	$bd_d = date("j",$DOBtimestamp);
	$bd_y = date("Y",$DOBtimestamp);
	
	
	// allow editing or just show?
	// check user privilege
	$bReadOnly = (isset($_GET['show']));
}



// defining echo_input for this page
function echo_input($col, $table = 'patients') {
	global $r;
	echo $r[$table][$col];
}

function valid($name)
{
	isValid($name) ? "" : "BAD";
}

function isValid($name)
{
	global $oValidator;
	return $oValidator->validate($name);
}

?>
<script>
<?
// if mode is show only, disable all input fields with javascript
if ($bReadOnly) { ?>
	
	var nodes = document.getElementsByTagName('input');

	for(i = 0; i < nodes.length; i++){ // doesn't seem to work
		nodes[i].readOnly = true;
	}


<? } ?>

function check_enroll_form() {
	if (document.enroll.fn.value == '') {
		alert ('Enter First Name');
	}
	else if (document.enroll.ln.value == '') {
		alert ('Enter Last Name');
	}
	else if (document.enroll.bd_m.value == '' || document.enroll.bd_d.value == '' ||document.enroll.bd_y.value == '') {
		alert ('Enter Date of Birth');
	}
	else if ( ( document.enroll.gender[0].checked == false )  && ( document.enroll.gender[1].checked == false ) ) {
		alert ('Enter Gender');
	}
	else { 
		document.enroll.submit();
		// form processed on patient page
	}
}

function validate_radio(element) {
	for (i = 0; i < element.length; i++ ) {
		if (element[i].checked != false) {
			return true;
			break;
		}	
	}
	return false;
}

</script>

<? if (isset($_GET['patientID'])) {
	echo "<div class=message_top><a href=\"patient.php?patientID=${_GET['patientID']}\">Go Back to Patient Page</a></div>";
}
?>
<div align="center">
<font color="red">
<? 	if (count($aFailed) > 0) {
?>
<strong>The following errors occured:</strong><BR>
<?
	  foreach($aFailed as $sErrorString) {
	  	echo $sErrorString . "<BR>";        
		} 
		echo "<P>";
    }
?>
</font>   
</div>
<div id=form_container>
<form method="post" name="form1" action="enroll.php" id="form1">

<? if (isset($patientID)) { ?>
<input type="hidden" name="patientID" value="<?=$patientID?>">
<? }?>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_yellow">
  <tr>
    <td width="24%" valign="top"><strong>Basic Info</strong> </td>
    <td width="76%">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="top">
	<table width="100%" border="0" cellspacing="3" cellpadding="0">
        <tr>
          <td>First
            <input name="firstName" type="text" id="firstName" class="<?=valid("firstName")?>" value="<?=field("firstName", isset($firstName) ? $firstName : ""); ?>" size="10" maxlength="60" />
Middle
<input name="middleName" type="text" id="middleName" class="<?=valid("middleName")?>" size="10" maxlength="60" value="<?=field("middleName", isset($middleName) ? $middleName : "");?>" />
Last<input name="lastName" type="text" id="lastName" class="<?=valid("lastName")?>" value="<?=field("lastName", isset($lastName) ?  $lastName : ""); ?>" size="10" maxlength="60" /></td>
          <td align="center">
          <?// if (isset($name_array[1]))  echo $name_array[1]; ?>
	<SCRIPT LANGUAGE="JavaScript">
	var dateOfBirth = new CalendarPopup("testdiv1");
dateOfBirth.setReturnFunction("setMultipleValues");
function setMultipleValues(y,m,d) {
	$('bd_y').value=y;
	$('bd_m').value=m;
	$('bd_d').value=d;
     }
	</SCRIPT>
          <span class="<?=isValid("bd_m") && isValid("bd_y") && isValid("bd_d") ? "" : "BAD"; ?>">DOB</span> 
		
		<INPUT TYPE="text" NAME="bd_m" ID="bd_m" VALUE="<?=field("bd_m",isset($bd_m)?$bd_m:"")?>" SIZE=3> /
		<INPUT TYPE="text" NAME="bd_d" VALUE="<?=field("bd_d",isset($bd_d)?$bd_d:"")?>" SIZE=3> /
<INPUT TYPE="text" NAME="bd_y" ID="bd_y" VALUE="<?=field("bd_y",isset($bd_y)?$bd_y:"")?>" SIZE=5> (m/d/y)
<A HREF="#" onClick="dateOfBirth.showCalendar('anchor9'); return false;" TITLE="dateOfBirth.showCalendar('anchor9'); return false;" NAME="anchor9" ID="anchor9">select</A>
<!--<A HREF="#" onClick="dateOfBirth.select(document.forms[1].bd_y,'anchor1x','MM/dd/yyyy'); return false;" TITLE="cal1x.select(document.forms[1].bd_y,'anchor1x','MM/dd/yyyy'); return false;" NAME="anchor1x" ID="anchor1x">select</A>-->
   <DIV ID="testdiv1" STYLE="position:absolute;visibility:hidden;background-color:white;layer-background-color:white;"></DIV>
             </td>
          <td align="right">SSN
            <input name="SSN" type="text" id="SSN" class="<?=valid("SSN")?>" size="10" maxlength="11" value="<?=field ('SSN'); ?>" /></td>
        </tr>

      </table></td>
    </tr>
    <tr><td>&nbsp;</td></tr>
  <tr>
    <td colspan="1" valign="top" width="50%">
    <strong>Contact Info</strong>
    <table width="100%" border="0" cellspacing="10" cellpadding="0">
      <tr><td>Address1</td>
		<td colspan="5"><input name="address1" type="text" id="address1" class="<?=valid("address1")?>" maxlength="255" size="35"  value="<?=field("address1")?>"/></td></tr>
        <tr><td>Address2</td>
        <td colspan="5"><input name="address2" type="text" id="address2" class="<?=valid("address2")?>" maxlength="255" size="35"  value="<?=field("address2")?>"/></td></tr>
        <tr><td>City</td> 
        <td><input name="city" type="text" id="city" class="<?=valid("city")?>" maxlength="255" size="15"  value="<?=field("city")?>"/></td>
        <td>State</td><td><input name="state" type="text" id="state" class="<?=valid("state")?>" maxlength="2" size="2"  value="<?=field("state")?>"/></td>
		<td>Zip</td><td><input name="zip" type="text" id="zip" class="<?=valid("zip")?>" maxlength="255" size="5"  value="<?=field("zip")?>"/></td></tr>
        <tr><td>Phone</td><td><input name="phone" type="text" id="phone" class="<?=valid("phone")?>" size="15" maxlength="15" value="<?=field("phone") ?>" /></td></tr>
	  </tr>
    </table></td>
    <td width="50%" valign="top">
    <strong>Emergency Contact</strong>
    <table width="100%" border="0" cellspacing="10" cellpadding="0">
      <tr>
        <td align="right">Name</td><td>
          <input name="emergencyName" type="text" id="emergencyName" class="<?=valid("emergencyName")?>" maxlength="255" size="15"  value="<?=field("emergencyName")?>"/></td></tr>
        <tR><td align="right">Phone</td><td>
          <input name="emergencyPhoneNumber" type="text" id="emergencyPhoneNumber" class="<?=valid("emergencyPhoneNumber")?>" maxlength="15" size="15" value="<?=field("emergencyPhoneNumber")?>"/></td></tr>
        <tr><td align="right">Relationship</td><td>
          <input name="emergencyRelationship" type="text" id="emergencyRelationship" class="<?=valid("emergencyRelationship")?>" maxlength="30"  size="15" value="<?=field("emergencyRelationship")?>"/></td></tr>
      </tr>
    </table></td>
    </tr>
  <tr>
    <td colspan="2" valign="top"></td>
    </tr>
	</table>
	
	
	
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_blue">
  <tr>
    <td valign="top"><strong>Demographics</strong></td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="top"><table width="100%" border="0" cellspacing="10" cellpadding="0">
          <tr>
            <td>Gender
              <input type="radio" name="gender" value="M" />
Male 
        &nbsp;
        <input type="radio" name="gender" value="F" />
Female </td>
            <td align="center">Ethnicity
              <select name="ethnicity" id="ethnicity">
                <? foreach ($ethnicity_array AS $key => $value) {
		echo "<option value=$key>$value</value>";
	}
	?>
              </select></td>
            <td align="right">Primary Language
              <select name="primary_lang" id="primary_lang">
                <? foreach ($primary_lang_array AS $key => $value) {
		echo "<option value=$key>$value</value>";
	}
	?>
              </select></td>
          </tr>
        </table>
        <table width="100%" border="0" cellspacing="10" cellpadding="0">
          <tr>
            <td>Level of Education
              <select name="education" id="education">
                <? foreach ($education_array AS $key => $value) {
		echo "<option value=$key>$value</value>";
	}
	?>
              </select></td>
            <td align="center">Homeless
              <input name="homeless" type="checkbox" id="homeless" value="1" />
Check for Yes </td>
            <td align="right">Marital Status
              <select name="marital_status" id="marital_status">
                <? foreach ($marital_status_array AS $key => $value) {
		echo "<option value=$key>$value</value>";
	}
	?>
              </select></td>
          </tr>
        </table>
        </p></td>
    </tr>
  <tr>
    <td colspan="2" valign="top"><table width="100%" border="0" cellspacing="10" cellpadding="0">
      <tr>
        <td>Monthly Income
          <input name="income" type="text" id="income" size="15" /></td>
        <td align="center">Number of Kids 
          <input name="kidsNumber" type="text" id="kidsNumber" size="5" maxlength="3" class="<?=valid("kidsNumber"); ?>" value="<?=field("kidsNumber")?>"/></td>
        <td align="right">Kids at Home
          <input name="kidsHome" type="text" id="kidsHome" size="5" maxlength="3"  class="<?=valid("kidsHome"); ?>" value="<?=field("kidsHome")?>"/></td>
      </tr>
    </table></td>
    </tr>
  <tr>
    <td colspan="2" valign="top"><table width="100%" border="0" cellspacing="10" cellpadding="0">
      <tr>
        <td>Adults at Home
          <input name="adultsHome" type="text" id="adultsHome" size="5" maxlength="3" class="<?=valid("adultsHome"); ?>"   value="<?=field("adultsHome")?>"/></td>
        <td align="center">Documentation
          <input name="documentation" type="checkbox" id="documentation" value="1" />
Check for Yes</td>
        <td align="right">Assets
          <input name="assets" type="checkbox" id="assets" value="1" />
Check for Yes</td>
      </tr>
    </table></td>
    </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_yellow">
  <tr>
    <td valign="top"><strong>Family History</strong> </td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" align="right" valign="top"><table width="100%" border="0" cellspacing="10" cellpadding="0">
      <tr>
        <td>Mother Cause of Death
          <input name="motherDeathCause" type="text" id="motherDeathCause" size="15" maxlength="255" class="<?=valid("motherDeathCause"); ?>"   value="<?=field("motherDeathCause")?>"/></td>
        <td align="right">Mother Age of Death
          <input name="motherDeathAge" type="text" id="motherDeathAge" size="5" maxlength="3" class="<?=valid("motherDeathAge"); ?>"  value="<?=field("motherDeathAge")?>"/></td>
      </tr>
    </table>      </td>
    </tr>
  <tr>
    <td colspan="2" valign="top"><table width="100%" border="0" cellspacing="10" cellpadding="0">
      <tr>
        <td>Father Cause of Death
          <input name="fatherDeathCause" type="text" id="fatherDeathCause" size="15" maxlength="255" class="<?=valid("fatherDeathCause"); ?>"  value="<?=field("fatherDeathCause")?>"/></td>
        <td align="right">Father Age of Death
          <input name="fatherDeathAge" type="text" id="fatherDeathAge" size="5" maxlength="3" class="<?=valid("fatherDeathAge"); ?>"  value="<?=field("fatherDeathAge")?>"/></td>
      </tr>
    </table></td>
    </tr>
  <tr>
    <td colspan="2" valign="top">&nbsp;</td>
    </tr>
  <tr>
    <td colspan="2"><table width="100%" border="0" cellspacing="10" cellpadding="0">
        <tr>
          <td>&nbsp;</td>
          <td align="center">Mother</td>
          <td align="center">Father</td>
          <td align="center">Siblings</td>
          <td>&nbsp;</td>
          <td align="center">Mother</td>
          <td align="center">Father</td>
          <td align="center">Siblings</td>
        </tr>
        <tr>
          <td>High Cholesterol (HL)</td>
          <td align="center"><input name="high_cholesterol" type="checkbox" id="high_cholesterol" value="1" /></td>
          <td align="center"><input name="high_cholesterol2" type="checkbox" id="high_cholesterol2" value="1" /></td>
          <td align="center"><input name="high_cholesterol3" type="checkbox" id="high_cholesterol3" value="1" /></td>
          <td>Allergies</td>
          <td align="center"><input name="high_cholesterol" type="checkbox" id="high_cholesterol" value="1" /></td>
          <td align="center"><input name="high_cholesterol2" type="checkbox" id="high_cholesterol2" value="1" /></td>
          <td align="center"><input name="high_cholesterol3" type="checkbox" id="high_cholesterol3" value="1" /></td>
        </tr>
        <tr>
          <td>High Blood Pressure-HT</td>
          <td align="center"><input name="high_cholesterol4" type="checkbox" id="high_cholesterol4" value="1" /></td>
          <td align="center"><input name="high_cholesterol5" type="checkbox" id="high_cholesterol5" value="1" /></td>
          <td align="center"><input name="high_cholesterol6" type="checkbox" id="high_cholesterol6" value="1" /></td>
          <td>Breast Cancer</td>
          <td align="center"><input name="high_cholesterol4" type="checkbox" id="high_cholesterol4" value="1" /></td>
          <td align="center"><input name="high_cholesterol5" type="checkbox" id="high_cholesterol5" value="1" /></td>
          <td align="center"><input name="high_cholesterol6" type="checkbox" id="high_cholesterol6" value="1" /></td>
        </tr>
        <tr>
          <td>Heart Attack</td>
          <td align="center"><input name="high_cholesterol9" type="checkbox" id="high_cholesterol9" value="1" /></td>
          <td align="center"><input name="high_cholesterol8" type="checkbox" id="high_cholesterol8" value="1" /></td>
          <td align="center"><input name="high_cholesterol7" type="checkbox" id="high_cholesterol7" value="1" /></td>
          <td>Other Cancer</td>
          <td align="center"><input name="high_cholesterol9" type="checkbox" id="high_cholesterol9" value="1" /></td>
          <td align="center"><input name="high_cholesterol8" type="checkbox" id="high_cholesterol8" value="1" /></td>
          <td align="center"><input name="high_cholesterol7" type="checkbox" id="high_cholesterol7" value="1" /></td>
        </tr>
        <tr>
          <td>Coronary Artery Disease</td>
          <td align="center"><input name="high_cholesterol" type="checkbox" id="high_cholesterol" value="1" /></td>
          <td align="center"><input name="high_cholesterol2" type="checkbox" id="high_cholesterol2" value="1" /></td>
          <td align="center"><input name="high_cholesterol3" type="checkbox" id="high_cholesterol3" value="1" /></td>
          <td>Depression</td>
          <td align="center"><input name="high_cholesterol" type="checkbox" id="high_cholesterol" value="1" /></td>
          <td align="center"><input name="high_cholesterol2" type="checkbox" id="high_cholesterol2" value="1" /></td>
          <td align="center"><input name="high_cholesterol3" type="checkbox" id="high_cholesterol3" value="1" /></td>
        </tr>
        <tr>
          <td>Stroke</td>
          <td align="center"><input name="high_cholesterol4" type="checkbox" id="high_cholesterol4" value="1" /></td>
          <td align="center"><input name="high_cholesterol5" type="checkbox" id="high_cholesterol5" value="1" /></td>
          <td align="center"><input name="high_cholesterol6" type="checkbox" id="high_cholesterol6" value="1" /></td>
          <td>Schizophrenia</td>
          <td align="center"><input name="high_cholesterol4" type="checkbox" id="high_cholesterol4" value="1" /></td>
          <td align="center"><input name="high_cholesterol5" type="checkbox" id="high_cholesterol5" value="1" /></td>
          <td align="center"><input name="high_cholesterol6" type="checkbox" id="high_cholesterol6" value="1" /></td>
        </tr>
        <tr>
          <td>Diabetes</td>
          <td align="center"><input name="high_cholesterol9" type="checkbox" id="high_cholesterol9" value="1" /></td>
          <td align="center"><input name="high_cholesterol8" type="checkbox" id="high_cholesterol8" value="1" /></td>
          <td align="center"><input name="high_cholesterol7" type="checkbox" id="high_cholesterol7" value="1" /></td>
          <td>Alcoholism</td>
          <td align="center"><input name="high_cholesterol9" type="checkbox" id="high_cholesterol9" value="1" /></td>
          <td align="center"><input name="high_cholesterol8" type="checkbox" id="high_cholesterol8" value="1" /></td>
          <td align="center"><input name="high_cholesterol7" type="checkbox" id="high_cholesterol7" value="1" /></td>
        </tr>
        <tr>
          <td>Thyroid</td>
          <td align="center"><input name="high_cholesterol4" type="checkbox" id="high_cholesterol4" value="1" /></td>
          <td align="center"><input name="high_cholesterol5" type="checkbox" id="high_cholesterol5" value="1" /></td>
          <td align="center"><input name="high_cholesterol6" type="checkbox" id="high_cholesterol6" value="1" /></td>
          <td>Clotting Disorder</td>
          <td align="center"><input name="high_cholesterol4" type="checkbox" id="high_cholesterol4" value="1" /></td>
          <td align="center"><input name="high_cholesterol5" type="checkbox" id="high_cholesterol5" value="1" /></td>
          <td align="center"><input name="high_cholesterol6" type="checkbox" id="high_cholesterol6" value="1" /></td>
        </tr>
        <tr>
          <td>Asthma</td>
          <td align="center"><input name="high_cholesterol9" type="checkbox" id="high_cholesterol9" value="1" /></td>
          <td align="center"><input name="high_cholesterol8" type="checkbox" id="high_cholesterol8" value="1" /></td>
          <td align="center"><input name="high_cholesterol7" type="checkbox" id="high_cholesterol7" value="1" /></td>
          <td>Sickle Cell Anemia</td>
          <td align="center"><input name="high_cholesterol9" type="checkbox" id="high_cholesterol9" value="1" /></td>
          <td align="center"><input name="high_cholesterol8" type="checkbox" id="high_cholesterol8" value="1" /></td>
          <td align="center"><input name="high_cholesterol7" type="checkbox" id="high_cholesterol7" value="1" /></td>
        </tr>
      </table>      </td>
    </tr>
	</table>
	
	
	
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_blue">
	
  <tr>
    <td valign="top"><strong>Medical History</strong></td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td valign="top" colspan="2"><table width="100%" border="0" cellspacing="10" cellpadding="0">
      <tr>
        <td width="50%" valign="top">Current Medications<br />
          <input name="medication_title[]" type="text" id="medication_title[]" maxlength="255" style="width:250px;" />
Drug<br />
<textarea name="medication_prescription[]" id="medication_prescription[]" style="width:250px;" ></textarea>
Prescription<br />
<input name="medication_start[]" type="text" id="medication_start[]" maxlength="8" style="width:250px;" />
Start (mm/dd/yy) <br />
<input name="medication_end[]" type="text" id="medication_end[]" maxlength="8" style="width:250px;" />
End (mm/dd/yy) <br />
Add</td>
        <td valign="top">Allergies
          <select name="allergies[]" id="allergies[]">
            <? foreach ($allergies_array AS $key => $value) {
		echo "<option value=$key>$value</value>";
	}
	?>
          </select>
          <br />
Add</td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td colspan="2" valign="top"><br>
      <table width="100%" border="0" cellspacing="10" cellpadding="0">
        <tr>
          <td width="50%" valign="top">Hospitalizations<br />
            <input name="hospitalization_title[]" type="text" id="hospitalization_title[]" maxlength="255" style="width:250px;" />
Title <br />
<textarea name="hospitalization_description[]" id="hospitalization_description[]" style="width:250px;" ></textarea>
Description<br />
<input name="hospitalization_start[]2" type="text" id="hospitalization_start[]" maxlength="8" style="width:250px;" />
Start (mm/dd/yy) <br />
<input name="hospitalization_end[]2" type="text" id="hospitalization_end[]" maxlength="8" style="width:250px;" />
End (mm/dd/yy) <br />
Add</td>
          <td valign="top">Surgeries <br />
            <input name="surgery_title[]" type="text" id="surgery_title[]" maxlength="255" style="width:250px;" />
Procedure <br />
<textarea name="surgery_description[]" id="surgery_description[]" style="width:250px;" ></textarea>
Description<br />
<input name="surgery_date[]" type="text" id="surgery_date[]" maxlength="8" style="width:250px;" />
Date (mm/dd/yy) <br />
Add</td>
        </tr>
      </table></td>
    </tr>
  <tr>
    <td colspan="2" valign="top"><h3>Diseases</h3>
    <? // ##################################### DISEASES ###############

showMessageAddCheckboxes();

// Col 1 *************

// Childhood Diseases
// Cancers
// Endocrine

// Col 2 *************

// Hematological
// Psych/Substance Abuse
// Infections

// Col 3 *************
// Neurological
// Ophthalmologic
// Cardio/Neuro Vascular
// Respiratory
// GI

// Col 4 *************
// GU/Renal
// Immunological
// Other Illnesses (freetext)
// Comments (freetext)

?>
    
    
    <table width="100%" border="0" cellspacing="10" cellpadding="0">
      <tr>
        <td valign="top" style="border-right:1px dotted black; "><strong>Childhood Diseases</strong><br />

            <table width="100%" border="0" cellspacing="10" cellpadding="0">
              <tr>
                <td>Measles                  </td>
                <td><input name="measles" type="checkbox" id="measles" value="1" /></td>
              </tr>
              <tr>
                <td>Mumps</td>
                <td><input name="measles2" type="checkbox" id="measles2" value="1" /></td>
              </tr>
              <tr>
                <td>Rubella</td>
                <td><input name="measles3" type="checkbox" id="measles3" value="1" /></td>
              </tr>
              <tr>
                <td>Whooping Cough</td>
                <td><input name="measles4" type="checkbox" id="measles4" value="1" /></td>
              </tr>
              <tr>
                <td>Rheumatic Fever</td>
                <td><input name="measles3" type="checkbox" id="measles3" value="1" /></td>
              </tr>
              <tr>
                <td>Scarlet</td>
                <td><input name="measles3" type="checkbox" id="measles3" value="1" /></td>
              </tr>
              <tr>
                <td>Polio</td>
                <td><input name="measles4" type="checkbox" id="measles4" value="1" /></td>
              </tr>
            </table>
              <br />
              <strong>Cancers</strong><br />
            <table width="100%" border="0" cellspacing="10" cellpadding="0">
              <tr>
                <td>Colon Cancer</td>
                <td><input name="measles" type="checkbox" id="measles" value="1" /></td>
              </tr>
              <tr>
                <td>Living Cancer</td>
                <td><input name="measles2" type="checkbox" id="measles2" value="1" /></td>
              </tr>
              <tr>
                <td>Lung Cancer</td>
                <td><input name="measles3" type="checkbox" id="measles3" value="1" /></td>
              </tr>
              <tr>
                <td>Melanoma</td>
                <td><input name="measles4" type="checkbox" id="measles4" value="1" /></td>
              </tr>
              <tr>
                <td>Other Skin Cancer</td>
                <td><input name="measles3" type="checkbox" id="measles3" value="1" /></td>
              </tr>
              <tr>
                <td>Breast Cancer</td>
                <td><input name="measles3" type="checkbox" id="measles3" value="1" /></td>
              </tr>
            </table>
			<br />
			<strong>Endocrine</strong><br />
            <table width="100%" border="0" cellspacing="10" cellpadding="0">
              <tr>
                <td>Hyperthyroid</td>
                <td><input name="measles" type="checkbox" id="measles" value="1" /></td>
              </tr>
              <tr>
                <td>Hypothyroid</td>
                <td><input name="measles2" type="checkbox" id="measles2" value="1" /></td>
              </tr>
              <tr>
                <td>Diabetes</td>
                <td><input name="measles3" type="checkbox" id="measles3" value="1" /></td>
              </tr>
              <tr>
                <td>High Cholesterol</td>
                <td><input name="measles4" type="checkbox" id="measles4" value="1" /></td>
              </tr>
            </table>			</td>
        <td valign="top" style="border-right:1px dotted black; "><strong>Hematological</strong><br />
            <table width="100%" border="0" cellspacing="10" cellpadding="0">
              <tr>
                <td>Thalesseima</td>
                <td><input name="measles" type="checkbox" id="measles" value="1" /></td>
              </tr>
              <tr>
                <td>Anemia</td>
                <td><input name="measles2" type="checkbox" id="measles2" value="1" /></td>
              </tr>
              <tr>
                <td>Clotting Disorder</td>
                <td><input name="measles3" type="checkbox" id="measles3" value="1" /></td>
              </tr>
            </table>
          <br />
          <strong>Psych/Substance Abuse</strong><br />
            <table width="100%" border="0" cellspacing="10" cellpadding="0">
              <tr>
                <td>Schizophrenia</td>
                <td><input name="measles" type="checkbox" id="measles" value="1" /></td>
              </tr>
              <tr>
                <td>Bipolar</td>
                <td><input name="measles2" type="checkbox" id="measles2" value="1" /></td>
              </tr>
              <tr>
                <td>Anxiety Disorder</td>
                <td><input name="measles3" type="checkbox" id="measles3" value="1" /></td>
              </tr>
              <tr>
                <td>Alcoholism</td>
                <td><input name="measles4" type="checkbox" id="measles4" value="1" /></td>
              </tr>
              <tr>
                <td>Drug Dependence</td>
                <td><input name="measles3" type="checkbox" id="measles3" value="1" /></td>
              </tr>
            </table>
          <br />
          <strong>Infections</strong><br />
            <table width="100%" border="0" cellspacing="10" cellpadding="0">
              <tr>
                <td>HIV and/or AIDS</td>
                <td><input name="measles" type="checkbox" id="measles" value="1" /></td>
              </tr>
              <tr>
                <td>Chicken Pox</td>
                <td><input name="measles2" type="checkbox" id="measles2" value="1" /></td>
              </tr>
              <tr>
                <td>Hepatitis A</td>
                <td><input name="measles3" type="checkbox" id="measles3" value="1" /></td>
              </tr>
              <tr>
                <td>Hepatitis B</td>
                <td><input name="measles" type="checkbox" id="measles" value="1" /></td>
              </tr>
              <tr>
                <td>Hepatitis C</td>
                <td><input name="measles2" type="checkbox" id="measles2" value="1" /></td>
              </tr>
              <tr>
                <td>Syphilis</td>
                <td><input name="measles3" type="checkbox" id="measles3" value="1" /></td>
              </tr>
              <tr>
                <td>Gonorrhea</td>
                <td><input name="measles" type="checkbox" id="measles" value="1" /></td>
              </tr>
              <tr>
                <td>Chlamydia</td>
                <td><input name="measles2" type="checkbox" id="measles2" value="1" /></td>
              </tr>
              <tr>
                <td>Tuberculosis</td>
                <td><input name="measles3" type="checkbox" id="measles3" value="1" /></td>
              </tr>
          </table></td>
        <td valign="top" style="border-right:1px dotted black; "><strong>Neurological</strong><br />
            <table width="100%" border="0" cellspacing="10" cellpadding="0">
              <tr>
                <td>Parkinson Disease</td>
                <td><input name="measles" type="checkbox" id="measles" value="1" /></td>
              </tr>
              <tr>
                <td>Seizure</td>
                <td><input name="measles2" type="checkbox" id="measles2" value="1" /></td>
              </tr>
              <tr>
                <td>Migraine</td>
                <td><input name="measles3" type="checkbox" id="measles3" value="1" /></td>
              </tr>
            </table>
          <br />
          <strong>Ophthalmologic</strong><br />
            <table width="100%" border="0" cellspacing="10" cellpadding="0">
              <tr>
                <td>Diabetic Retinopathy</td>
                <td><input name="measles" type="checkbox" id="measles" value="1" /></td>
              </tr>
              <tr>
                <td>Glaucoma</td>
                <td><input name="measles2" type="checkbox" id="measles2" value="1" /></td>
              </tr>
              <tr>
                <td>Cataracts</td>
                <td><input name="measles3" type="checkbox" id="measles3" value="1" /></td>
              </tr>
            </table>
          <br />
          <strong>Cardio/Neuro Vascular</strong><br />
            <table width="100%" border="0" cellspacing="10" cellpadding="0">
              <tr>
                <td>Hypertension</td>
                <td><input name="measles" type="checkbox" id="measles" value="1" /></td>
              </tr>
              <tr>
                <td>Emphysema</td>
                <td><input name="measles2" type="checkbox" id="measles2" value="1" /></td>
              </tr>
              <tr>
                <td>Angina</td>
                <td><input name="measles3" type="checkbox" id="measles3" value="1" /></td>
              </tr>
              <tr>
                <td>Heart Failure</td>
                <td><input name="measles" type="checkbox" id="measles" value="1" /></td>
              </tr>
              <tr>
                <td>Stroke (CVD)</td>
                <td><input name="measles2" type="checkbox" id="measles2" value="1" /></td>
              </tr>
              <tr>
                <td>Peripheral Vascular</td>
                <td><input name="measles3" type="checkbox" id="measles3" value="1" /></td>
              </tr>
              <tr>
                <td>Disease</td>
                <td><input name="measles" type="checkbox" id="measles" value="1" /></td>
              </tr>
          </table>
            <br />
            <strong>Respiratory</strong><br />
            <table width="100%" border="0" cellspacing="10" cellpadding="0">
              <tr>
                <td>Emphysema</td>
                <td><input name="measles5" type="checkbox" id="measles5" value="1" /></td>
              </tr>
              <tr>
                <td>Asthma</td>
                <td><input name="measles22" type="checkbox" id="measles22" value="1" /></td>
              </tr>
            </table>
            <br />
            <strong>GI</strong><br />
            <table width="100%" border="0" cellspacing="10" cellpadding="0">
              <tr>
                <td>GERD</td>
                <td><input name="measles6" type="checkbox" id="measles6" value="1" /></td>
              </tr>
              <tr>
                <td>Cirrhosis</td>
                <td><input name="measles23" type="checkbox" id="measles23" value="1" /></td>
              </tr>
            </table></td>
        <td valign="top"><strong>GU/Renal</strong><br />
            <table width="100%" border="0" cellspacing="10" cellpadding="0">
              <tr>
                <td>Renal Failure </td>
                <td><input name="measles" type="checkbox" id="measles" value="1" /></td>
              </tr>
              <tr>
                <td>Benign Prostatic</td>
                <td><input name="measles2" type="checkbox" id="measles2" value="1" /></td>
              </tr>
              <tr>
                <td>Hypertrophy</td>
                <td><input name="measles3" type="checkbox" id="measles3" value="1" /></td>
              </tr>
            </table>
          <br />
          <strong>Immunological</strong><br />
            <table width="100%" border="0" cellspacing="10" cellpadding="0">
              <tr>
                <td>Lupus/SL</td>
                <td><input name="measles" type="checkbox" id="measles" value="1" /></td>
              </tr>
              <tr>
                <td>Arthritic</td>
                <td><input name="measles2" type="checkbox" id="measles2" value="1" /></td>
              </tr>
              <tr>
                <td>Osteoarthritis</td>
                <td><input name="measles3" type="checkbox" id="measles3" value="1" /></td>
              </tr>
            </table>
          <br />
          <strong>Other Illnesses</strong><br />
            <table width="100%" border="0" cellspacing="10" cellpadding="0">
              <tr>
                <td><textarea cols="15" rows="5" name="other_illnesses"><?=field("other_illnesses")?></textarea></td>
                </tr>
            </table>
          <br />
          <strong>Comments</strong><br />
            <table width="100%" border="0" cellspacing="10" cellpadding="0">
              <tr>
                <td><textarea name="comments" cols="15" rows="5"><?=field("Comments")?></textarea></td>
                </tr>
            </table>          </td>
        <td valign="top">&nbsp;</td>
        <td valign="top">&nbsp;</td>
        </tr>
    </table></td>
    </tr>
	</table>
	
	
	

	
	
	
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_yellow" >
  
  
  <tr>
    <td colspan="2"><strong>Social History</strong> </td>
    </tr>
  <tr>
    <td colspan="2"><table width="100%" border="0" cellspacing="10" cellpadding="0">
      <tr>
        <td valign="top"><table width="100%" border="0" cellspacing="10" cellpadding="0">
          <tr>
            <td>&nbsp;</td>
            <td align="center">Past</td>
            <td align="center">Current</td>
            <td align="center">Never</td>
          </tr>
          <tr>  <td >Marijuana</td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
          </tr>
          <tr>  <td >Methamphetamine</td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
          </tr>
          <tr>  <td >Cocaine</td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
          </tr>
          <tr>  <td >Heroin</td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
          </tr>
          <tr>  <td >Designer Drugs/OTC Med </td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
          </tr>
          <tr>  <td >IV Drug Use </td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
          </tr>
          <tr>  <td >Tobacco</td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
          </tr>
          <tr>  <td >Alcohol</td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
            <td align="center"><input type="radio" name="marijuana" value="p" /></td>
          </tr>

        </table></td>
        <td valign="top"><table width="100%" border="0" cellspacing="10" cellpadding="0">
          <tr>
            <td>Sexually Active</td>
            <td><input name="sexually_active" type="checkbox" id="sexually_active" value="1" />
Check for Yes </td>
          </tr>
          <tr>
            <td>Sexual Preference</td>
            <td><input type="radio" name="sexual_preference" value="m" />
Men 
      &nbsp;
      <input type="radio" name="sexual_preference" value="w" />
Women &nbsp;
<input type="radio" name="sexual_preference" value="b" />
Both</td>
          </tr>
          <tr>
            <td>Lifetime Sexual Partners </td>
            <td><input name="lifetime_sexual_partners" type="text" id="lifetime_sexual_partners" size="5" maxlength="6" /></td>
          </tr>
          <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td><strong>Contraception</strong></td>
            <td>&nbsp;</td>
          </tr>
          <tr>
            <td><input name="contraception[]2" type="checkbox" id="contraception[]2" value="c" />
              Condoms</td>
            <td><input name="contraception[]6" type="checkbox" id="contraception[]6" value="c" />
              Spermicide</td>
          </tr>
          <tr>
            <td><input name="contraception[]3" type="checkbox" id="contraception[]3" value="c" />
              BC Pill</td>
            <td><input name="contraception[]7" type="checkbox" id="contraception[]7" value="c" />
              Vasectomy/Tubal</td>
          </tr>
          <tr>
            <td><input name="contraception[]4" type="checkbox" id="contraception[]4" value="c" />
              Intrauterine Device</td>
            <td><input name="contraception[]8" type="checkbox" id="contraception[]8" value="c" />
              None</td>
          </tr>
          <tr>
            <td><input name="contraception[]5" type="checkbox" id="contraception[]5" value="c" />
              Diaphragm</td>
            <td>&nbsp;</td>
          </tr>

        </table></td>
      </tr>
    </table></td>
    </tr>
  <tr>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2"><table width="100%" border="0" cellspacing="10" cellpadding="0">
      <tr>
        <td valign="top"><strong>Tobacco</strong><br />
          Packs Per Day
            <input name="packs_per_day2" type="text" id="packs_per_day2" size="5" maxlength="5" />
            <br />
            Years of Smoking
            <input name="years_of_smoking" type="text" id="years_of_smoking" size="5" maxlength="5" />
            <br /></td>
        <td valign="top"><strong>Alcohol</strong><br />
          Drinks Per Day
            <input name="lifetime_sexual_partners2" type="text" id="lifetime_sexual_partners2" size="5" maxlength="60" />
            <br /></td>
        <td valign="top"><strong>Domestic Violence</strong><br />
          <input type="radio" name="domestic_violence" value="p" />
Past 
      &nbsp;
      <input type="radio" name="domestic_violence" value="c" />
Current &nbsp;
<input type="radio" name="domestic_violence" value="n" />
Never </td>
      </tr>
      
    </table></td>
    </tr>
  <tr>
    <td><input type="hidden" name="submitted_enroll" value="1" /></td>
    <td>&nbsp;</td>
  </tr>
</table>

<input type="submit"  value="Submit">
	
</form>
</div>

<?
require_once("includes/footer.php");
?>