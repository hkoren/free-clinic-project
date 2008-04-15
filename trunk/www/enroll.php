<?php
include ("includes/config.php"); 
require_once("includes/message.php");
addJavaScript("form.js");
addJavaScript("CalendarPopup.js");
include ("includes/form.php");

$onload="fmtssn(document.form1.SSN);";
$page="New Enrollment";
include ("includes/header.php");
$debug = false;
$isEnrolled = $patientID != "";
/*
 * Created on 20. nov.. 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
 
$form_query = "SELECT ";
if ($patientID != "")
	$form_query .= "(select count(_id) as num from patientDemographic pd2 where pd2.demographicID = d._id
and pd2.patientID = $patientID) as num, pd.*, ";
else
	$form_query .= "0 as num,";
$form_query .= "d._id, d.name name, d.demographicTypeID, d.required, dt.name typeName,".
"ds.name sectionName, dg.name groupName FROM demographic d ".
"inner join demographicType dt on d.demographicTypeID = dt._id ".
"inner join demographicGroup dg on d.demographicGroupID = dg._id ".
"inner join demographicSection ds on ds._id = dg.demographicSectionID ";
if ($patientID != "") 
	$form_query .= "left outer join patientDemographic pd on pd.demographicID = d._id" .
			" and pd.patientID = $patientID "; 
$form_query .= " order by ds.name, dg.order asc, d.order asc, d._id asc";

if ($debug) echo $form_query;
$demographics=mysql_query($form_query) or trigger_error(mysql_error(). ": " . $form_query);
 
if (isset($_POST['submit']))
{
	include ("includes/enroll_submit.php"); 
}
$oldSection="";
$oldGroup="";
$oldTypeName="";


$demographicCount=mysql_num_rows($demographics);
//$row = mysql_fetch_assoc($demographics);

//echo $query ;
?>
<a name="top">
</a>
<form method="post" name="form1" action="enroll.php" onSubmit="return ValidateForm();">
<input type="hidden" name="submit" value="1">
<? 
if (isset($patientID)&& $patientID !=0) { 
	echo "<input type=\"hidden\" name=\"patientID\" value=\"$patientID\">";
}
?>
<script type="text/javascript" language="javascript" charset="utf-8">errorDiv();</script>
<?

include("includes/enroll_static.php");

include("includes/enroll_demographics.php");
 ?>
<? // ##################################### DISEASES ###############

showMessageAddCheckboxes();


?>
 
 <input type="submit" value="submit">
 </form>
 

 <script type="text/javascript" language="javascript" charset="utf-8">
 <!--
document.form1.firstName.focus();
 function ValidateForm()
 {
 	
 	initValidation();
 	
 	require("firstName", "Enter a First Name");		// Static field validation
 	require("lastName", "Enter a Last Name");
 	if (require("bd_m", "Enter birth month"))
 		isInteger("bd_m", "Enter valid birth month");	
 	if (require("bd_d", "Enter birth day"))
 	 	isInteger("bd_d", "Enter valid birth day");
	if (require("bd_y", "Enter birth year"))
	 	isInteger("bd_y", "Enter valid birth year");
	
 	isPhone("phone", "Please enter a valid phone number");
	isSSN("SSN", "Please enter a valid social security number");
	
	<?=$validationScript?>						// Demographic Validation Script
	if (!formValid()) {
		document.location = "#top";
	}
 	return formValid();
 }
-->
 </script>


 

<? 
include ("includes/footer.php"); 
?>
