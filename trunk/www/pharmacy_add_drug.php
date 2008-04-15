<?php
include ("includes/config.php"); 
$page="Add to Drug Inventory";
addJavascript("form.js");
addJavascript("json2.js");
addJavascript("barcode.js");
$onload="document.addDrugForm.quantityinput.focus();";
include ("includes/form.php"); 
include ("includes/header.php"); 
/*
 * Created on Feb 20, 2008
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
 
 /*This is the if statement to input the addDrugForm into the database.*/
if (isset($_POST['LISTING_SEQ_NO']))
{
	$LISTING_SEQ_NO = $_POST['LISTING_SEQ_NO'];
	$PKGCODE = $_POST['PKGCODE'];
	$LotNumber = escape_data($_POST['lotNumber']);
	$expirationMonth = $_POST['expirationMonth'];
	$expirationYear = $_POST['expirationYear'];
	$quantity = $_POST['quantity'];
	$query = "insert into pharmacyInventoryIn(LISTING_SEQ_NO, PKGCODE, quantity, LotNO, expirationMonth, expirationYear, timestamp) values($LISTING_SEQ_NO, $PKGCODE,$quantity,'$LotNumber', $expirationMonth, $expirationYear, NOW())";
	$result = mysql_query($query) or trigger_error(mysql_error()." query: $query"); 
	//echo "<script language=\"JavaScript\">history.go(-2);</script>";
}
 
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
	require("lotNumber", "enter lot number");
	require("expirationMonth", "enter expiration month");
	require("expirationYear", "enter expiration year");
	require("reOrderPoint", "Scan drug or enter NDC number");
	

	
	if (formValid()) {	
		// Populate validation Box
		var confirm = $('confirm');
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
		show("confirm");
		return false;
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
<div id="errorMessage" class="error"></div>
<form name="addDrugForm" method="post" action="pharmacy_add_drug.php" onsubmit="return validate();">

<?echo textInput("quantity", "Quantity", "", "input", 15,"15","","return distInput(event,this,getDrug)");
echo textInput("lotNumber", "Lot Number", "", "input", 15,"15","","return distInput(event,this,getDrug)");
echo textInput("expirationMonth", "Expiration Month", "","compact", 3,2,"","return distInput(event,this,getDrug)");
echo textInput("expirationYear", "Expiration Year", "", "compact", 4,4,"","return distInput(event,this,getDrug)");
echo textInput("reOrderPoint", "Set Re-Order Point", "",  "input", 5,5,"","return distInput(event,this,getDrug)");
?>

    <div align="right">
	    <input value="Add to Inventory" type="submit" name="addToInventoryButton" style="width:10em"/>
    </div>

<? include "includes/drug_scan.php" ?>

<div id="confirm" style="display:none; position: absolute; top:150px; left:150px; background-color: #888; border:1px black;"></div>

  </form>
</table>


<? 
include ("includes/footer.php"); 
?>
