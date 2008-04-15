<?php
include ("includes/config.php"); 
$page = "Clinic Visit";
addJavaScript("clinicVisit.js");
addJavaScript("message.js");
include ("includes/header.php"); 
include ("includes/measurement.php"); 
include ("includes/message.php");
include ("includes/location.php");



// in view mode, not enrolling new patient
if ($patientID != "") {
	$query="select * from patients where _id = $patientID";
	$result=mysql_query($query) or trigger_error(mysql_error());
	$patient=mysql_fetch_assoc($result);
	
	$patientIDstr = $patient['firstName'] . " " . $patient['lastName'] . " #" . $patientID; 
	
	// allow editing or just show?
	// check user privilege
	$bReadOnly = (isset($_GET['show']));
}


if ($_SERVER['REQUEST_METHOD']=='POST') {
	
	// Process Vitals
	addMeasurement("Heart rate", $patientID, $_POST["Heart_Rate"]);
	addMeasurement("Temperature", $patientID, $_POST["Temperature"]);
	addMeasurement("Systolic Blood Pressure", $patientID, $_POST["Systolic_Blood_Pressure"]);
	addMeasurement(2, $patientID, $_POST["Diastolic_Blood_Pressure"]);
	addMeasurement("Respiration", $patientID, $_POST["Respiration"]);
	
	// Process New Conditions
	processMessageAdds();
}
?>
  <div id="mainContent">
  <form id="form" name="form" method="post" action="clinicVisit.php?patientID=<?=$patientID?>">
   <table width="100%" border="0">
     <tr>
       <th scope="col">Patient</th>
       <th scope="col">Clinic</th>
       <th scope="col">Date</th>
       <th scope="col">Primary Student</th>
       <th scope="col">Attending</th>
     </tr>
     <tr>
       <td><a href="patient.php?patientID=<?=$patientID?>"><?=$patientIDstr?></a></td>
       <td><?location_changer()?></td>
       <td><?=date("m-d-Y");?></td>
       <td>
         <label for="primaryStudent"></label>
         <input type="text" name="primaryStudent" id="primaryStudent" />
       </td>
       <td>
         <label for="attending"></label>
         <input type="text" name="attending" id="attending" />
       </td>
     </tr>
   </table>
   <h2>
     <!-- end #mainContent -->
     Vitals</h2>
<table width="100%">
<tr>
	<td><? enterMeasurement("Temperature", $patientID) ?></td>
	<td><? enterMeasurement("Heart Rate", $patientID) ?></td>
	<td><? enterMeasurement("Systolic Blood Pressure", $patientID) ?></td>
	<td><? enterMeasurement("Diastolic Blood Pressure", $patientID) ?></td>
	<td><? enterMeasurement("Respiration", $patientID) ?></td>
</tr>
</table>
     
<h2>Problem List</h2>
<table width="100%">
	<tr><td width="50%" valign="top">
	<?=showMessagesToAddress()?>
		</td> 
   		<td width="50%" valign="top">
   
   <div id="addNewPrompt">
   	<a href="javascript:addNewProblem();">Add a new problem</a>
   </div>
   <div id="addNewForm" style="display:none;">
	   <strong>Add New Problem</strong>
	   <p align="right">
   		<div id="diagnoses">
   		</div>
	   <select name="diagnosesList" onchange="eval(this.options[this.selectedIndex].value);">
	   	<option/>
<?
$query = "select _id,name,icd9 from messages where top=1 order by icd9 asc";
$result = mysql_query($query);
while ($message = mysql_fetch_assoc( $result)) {
	echo "<option value=\"addDiagnosis('".addslashes($message['icd9'])."','".addslashes($message['name'])."',${message['_id']})\">" .
			"${message['icd9']}-${message['name']}</option>\n";
}
?>
	   </select><br>
	   <a href="javascript:seeAllDiagnoses()">See All Diagnoses</a>
   		</p> 
   </div>
   
   </td></tr>
   </table>

   <h2>Procedures and Tests</h2>
   <div id="testsPrompt">
     <p>Did you perform or order any labs or tests today?&nbsp;&nbsp;&nbsp;
	 <a href="javascript:testsYes()">Yes</a>&nbsp;&nbsp;&nbsp;
     <a href="javascript:testsNo()">No</a>
   </div>
   <div id="testsDisplay" style="display:none;">
   <b>Did you perform or order any of the following past-due labs or tests?</b>
   <?
   $query = "select m.* from messageReaction mr inner join messages m on m._id=mr.reactionMessageID " .
   		"inner join patientMessages pm on pm.messageID = mr.messageID where pm.patientID = $patientID";
   	$result = mysql_query($query) or trigger_error(mysql_error());
   	
   	$count = mysql_num_rows($result);
   	echo "<br>$count found:<br>\n";
   	while($reaction = mysql_fetch_assoc($result))
   	{
   		echo "${reaction['name']}<br>";
   	}

// Select due messages
/*
$query = "select messages.*, patientMessage.due from messages " .
		"outer join patientMessage on patientMessage.messageID = messages.ID and patientMesage.patientID=$patientID" .
		"where patientID=$patientID or patientMessage._id is not null";
$result = mysql_query($query);

while ($row = mysql_fetch_array($result, MYSQL_ASSOC))
{
	echo $row['message.name'] ." Due " .$row['patientMessage.due'];
	
	// Do something special depending on what kind of message this is
	echo "<br>";
}*/
?>   			

  
  
   </>
   <div id="testsOthersPrompt">
     <b>Did you perform or order any other labs or tests?</b>&nbsp;&nbsp;&nbsp;
	   <a href="javascript:testsOtherYes()">Yes</a>&nbsp;&nbsp;&nbsp;
       <a href="javascript:testsOtherNo()">No</a>
   </div>
   
   <div id="testsOthersDisplay" style="display:none;">
   	<b>Did you perform or order any other labs or tests?</b>
   	<p>
   	<table width="100%" border="0">
   	<tr>
   		<td width="33%" valign="top">
   			<h3>Point of Care Labs</h3>
   			<input type="checkbox" name="pregnancy"> <label for="pregnancy">Pregnancy Test</label><br>
   			&nbsp;&nbsp;&nbsp;Result: <select name="pregnancyResult"><option/><option value="1">Postiive</option><option value="0">negative</option></select><br>
   			<input type="checkbox" name="glucose"> <label for="glucose">Fingerstick Glucose</label><br>
   			<input type="checkbox" name="strep"> <label for="strep">Strep</label><br>
   			<input type="checkbox" name="urinalysis"> <label for="urinalysis">Fingerstick Glucose</label><br>
   			<input type="checkbox" name="hiv"> <label for="hiv">Rapid HIV</label><br>
   			
   		
   		</td>
   		<td width="33%" valign="top">
   			<h3>Health Care Maintenence</h3>
   		<input type="checkbox" name="flu"> <label for="flu">Flu Shot</label><br>
 		   		<input type="checkbox" name="papsmear"> <label for="flu">Pap Smear</label><br>
   		</td>
   		<td width="33%" valign="top">
   			<h3>Chronic Disease Management</h3>
   			<p><a href="#">Diabetes Tests</a></p>
   			<p><a href="#">Hypertension Tests</a></p>
   			<br>
   			<br>
   			<input type="button" value="Additional Quest Referrals">
   		</td>
   	</tr>
   	</table>
   	</p>
   </div>
   </div>

   <h2>Pharmacy Orders</h2>
     <label for="sendOrder"></label>
     <input type="submit" name="sendOrder" id="sendOrder" value="Send to pharmacy" />
   <h2>Referrals</h2>
     <label for="makeReferral"></label>
     <input type="submit" name="makeReferral" id="makeReferral" value="Make a referral" />
     <label for="viewReferrals"></label>
     <input type="submit" name="viewReferrals" id="viewReferrals" value="View referral history" />
  </div>
  <br />
  <div id="footer">
      <label for="done"></label>
      <div align="center">
        <input name="done" type="submit" class="style1" id="done" value="Submit" />
        </div>
      <!-- end #footer -->
</form>

<!-- end #container --></div>

<? 
include ("includes/footer.php"); 
?>
