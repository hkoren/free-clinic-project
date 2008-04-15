<?php
include ("includes/config.php"); 
$page="Visit Planner";
include ("includes/header.php"); 
/*
 * Created on May 24, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
?>

<table>
<tr>
<td>
<h1>[NAME]</h1>
Patient ID: <?=$patientID?><br>
<br>
(Today's Vitals)<br>
<table>
<tr><td>Weight:</td><td>[Weight]</td></tr>
<tr><td>Pulse:</td><td>[Pulse] BPM</td></tr>
<tr><td>BMI:</td><td>[BMI]</td></tr>
<tr><td>BP:</td><td>[BP]</td></tr>
</table>
</td>
<td>
Age: [AGE]<br>
DOB: [DOB]<br>
Address:<br>
[ADDRESS]<br>
Phone: [PHONE]<br>
Sex: [GENDER]<br>
Language: [ENGLISH]<br>
Homeless: [HOMELESS]<br>
</td>
<td>
<h2>Diagnoses:</h2>
[diagnoses]

<h2>Medications:</h2>
[medications]
</td>
</tr>
</table>
<h2>Action Items</h2>
<h2>Family Profiles</h2>
<h2>Referrals</h2>
<h2>Asthma</h2>
<h2>Diabetes</h2>
<h2>Depression</h2>
<h2>Diabetes</h2>
<h2>Hyperlipidemia</h2>
<? 
include ("includes/footer.php"); 
?>
