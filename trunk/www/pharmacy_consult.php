<?php
include ("includes/config.php"); 
$page="Pharmacy Consultation";
addJavascript("json2.js");
addJavascript("barcode.js");
include ("includes/form.php");
$onload="document.consultForm.quantityinput.focus();"; 
include ("includes/header.php"); 
?>
<script language="javascript">
function validate() {
	initValidation();

	if ($('LISTING_SEQ_NO').value == "")
	{
		errorMessage += "<p>Scan a drug!</p>";
		valid=false;
	}	
	require("quantity", "Enter Quantity");
	
	
	if (formValid()) {	
		// Populate validation Box
		/* var confirm = $('confirm');
		confirm.innerHTML = 	
		"<b>Add To Inventory Confirmation</b>" +
		"<div style=\"background-color:#fff; margin:20px; padding: 5px; border: 1px black;\"><table width=\"475\"><tr><td align=\"right\"><b>Drug</b></td><td>" + $('drugName').innerHTML + "</td>"+
		"<td align=\"right\"><b>Manufacturer:</b></td><td>" + $('manufacturerName').innerHTML + "</td>" + 
		"<td align=\"right\"><b>NDC:</b></td><td>" + $('ndcNumber').innerHTML + "</td></tr>" + 
		"<tr><td align=\"right\"><b>Strength:</b></td><td>" + $('strength').innerHTML + "</td>"+
		"<td align=\"right\"><b>Quantity:</b></td><td>" + document.addDrugForm.quantity.value + "</td>" + 
		"<td align=\"right\"><b>Lot#</b></td><td>" + document.addDrugForm.lotNumber.value + "</td></tr></table>" +
		"<b>Is this information correct? <a href=\"javascript:submitForm();\">Yes, Print Bottle Label</a> &nbsp; " +
		"<a href=\"javascript:editForm();\">No, Edit Info</a>"
		"</div>"
		// Show Validation Box
		show("confirm");  */
		
		return confirm("Are you sure you wish to prescribe this?");
	}
	return false;
}

function submitForm() {
	document.addDrugForm.submit();
}
function editForm() {
	// Hide Validation Box
	hide("confirm");
}

</script>


<div style="margin-center: 20px; width:33%; float:left; position:relative; left:10px; background-color: #FFFF00; border: 1px solid black;">
<center><h3>Chronic Problems</h3></center>
<center>List of Problems</center>
</div>
<div style="margin-center: 20px; width:33%; float:left; position:relative; left:10px; background-color: #00FFFF; border: 1px solid black;">
<h3><center>Current Meds</center></h3>
<center>List of Meds</center>
</div>
<div style="margin-center: 20px; width:33%; float:left; position:relative; left:10px; background-color: #FF00FF; border: 1px solid black;">
<center><h3>Allergies</h3></center>
<center>List of Allergies</center>
</div>
<br><br><br><br><br>
<a style="float:right" href="">View Complete History</a><br>
<hr>

<h4>Language Selection</h4>
Labels Should Be Printed in: &nbsp;&nbsp;
<INPUT TYPE=CHECKBOX NAME="english">ENGLISH	&nbsp;&nbsp;
<INPUT TYPE=CHECKBOX NAME="spanish">SPANISH
<hr>

<h4>Current Meds</h4><br clear="right">

<table width="700" border="3">
  <tr>
    <td width="80" bgcolor="#FFFFFF"><div align="center"><strong>Current Rx</strong></div></td>
    <td width="70" bgcolor="#FFFFFF"><div align="center"><strong>Notes</strong></div></td>
    <td width="100" bgcolor="#FFFFFF"><div align="center"><strong>Indication</strong></div></td>
    <td width="123" bgcolor="#FFFFFF"><div align="center"><strong>Preferred Strength</strong></div></td>
    <td width="45" bgcolor="#FFFFFF"><div align="center"><strong>Dose</strong></div></td>
    <td width="85" bgcolor="#FFFFFF"><div align="center"><strong>Quantity</strong></div></td>
    <td width="70" bgcolor="#FFFFFF"><div align="center"><strong>Source</strong></div></td>
  </tr>
</table>
<!--Dynamic table starts here-->
<hr>

<h4>Patient Assistance Inventory</h4><br clear="right">

<table width="700" border="3">
  <tr>
    <td width="80" bgcolor="#FFFFFF"><div align="center"><strong>Drug</strong></div></td>
    <td width="70" bgcolor="#FFFFFF"><div align="center"><strong>Strength</strong></div></td>
    <td width="100" bgcolor="#FFFFFF"><div align="center"><strong>Dose</strong></div></td>
    <td width="123" bgcolor="#FFFFFF"><div align="center"><strong>Indication</strong></div></td>
    <td width="45" bgcolor="#FFFFFF"><div align="center"><strong>Availability</strong></div></td>
    <td width="85" bgcolor="#FFFFFF"><div align="center"><strong>Re-Order Date</strong></div></td>
  </tr>
</table>
<!--Dynamic table starts here-->
<hr>

<h4>New Meds</h4><br clear="right">
<form method="post" action="pharmacy_consult.php" name="consultForm" onSubmit="return validate();">

<div style="float: right;">
<? include("includes/drug_scan.php"); ?>
</div>
<?echo textInput("quantity", "Quantity", "", "input", 15,"15","","return distInput(event,this,getDrug)");
?>

<div id="errorMessage" class="error"></div>

<table>
<tr><td width="130"><b>TAKE</b></td>
<td>
<select name="frequency">
<option value="1">1x a day</option>
<option value="2">2x a day</option>
<option value="3">3x a day</option>
<option value="4">4x a day</option>
</select>
</td>
</tr>

<tr><td><b>FOR</b></td>
<td>
<select name="duration">
<option value="1">For 1 day</option>
<option value="2">For 2 days</option>
<option value="3">For 3 days</option>
<option value="4">For 4 days</option>
<option value="5">For 5 days</option>
<option value="6">For 6 days</option>
<option value="7">For 7 days</option>
<option value="14">For 14 days</option>
<option value="21">For 21 days</option>
<option value="30">For 30 days</option>
</select>
</td></tr>
</table>

&nbsp;&nbsp;&nbsp;



Additional Notes:<br><textarea rows="2" cols="40"></textarea> <br><br>
<hr><br><br>
<input type="submit" value="Order Now"></form>
<?
// Current Meds

// Alergies



// Language Selection


// Current Meds





include ("includes/footer.php"); 
?>
