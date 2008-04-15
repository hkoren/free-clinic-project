<?php
include ("includes/config.php"); 

addJavascript("barcode.js");
addJavascript("json2.js");
?>

<?php

$LISTING_SEQ_NO = isset($_GET["LISTING_SEQ_NO"])?$_GET["LISTING_SEQ_NO"]:"";
$PKGCODE =  isset($_GET["PKGCODE"])?$_GET["PKGCODE"]:"";
$startdate = isset($_GET["START_DATE"])?$_GET["START_DATE"]:"";
$enddate = isset($_GET["END_DATE"])?$_GET["END_DATE"]:"";


if ($LISTING_SEQ_NO != "")            // We are searching!
{
	if($PKGCODE == "")
		echo "missing PKGCODE";
		
	
	$DrugWhere = "LISTING_SEQ_NO=$LISTING_SEQ_NO and PKGCODE=$PKGCODE";
	
	$beforeRange = "'$startdate' <  timestamp"; 
	$durringRange = "'$startdate' <  timestamp < '$enddate'"; 
		
	$queryTotalInBefore = 
		"SELECT SUM(quantity) as totalIn FROM pharmacyInventoryIn WHERE $DrugWhere and $beforeRange";

	$queryIn = 
		"SELECT * FROM pharmacyInventoryIn WHERE  $DrugWhere and $durringRange";
	
	$queryTotalOutBefore = 
		"SELECT SUM(quantity) as totalOut FROM pharmacyInventoryOut WHERE $DrugWhere and $beforeRange";
	
	$insBefore = mysql_query($queryTotalInBefore)  or die(mysql_error());
	$row = mysql_fetch_assoc($insBefore);
	$insBefore = $row["totalIn"];
	
	$inventoryIns = mysql_query($queryIn) or die(mysql_error());
	
//	$outsBefore = mysql_query($queryTotalOutBefore) or die(mysql_error());
//	$row = mysql_fetch_assoc($outsBefore);
//	$outsBefore = $row["totalOut"];
	
	//$finalTotal = $totalIn - $totalOut;
}	

?>

<?php
include ("includes/header.php"); 
?>
<form action="pharmacy_inventory_audit.php" method="get" name="auditForm">
  <div style="float:right; width:300px;"><?include ("includes/drug_scan.php"); ?></div> <h2> Inventory Search </h2>
	<table cellpadding="5" cellspacing="0" align=center>

 	<tr>
 		<td> Enter Start Date </td>
 		<td> <input type="text" value="<?=$startdate?>" name="END_DATE" action="POST" onKeyDown="return distInput(event,this,getDrug)" /> </td>
 	</tr>
 	<tr>
 		<td> Enter End Date </td>
 		<td> <input type="text" value="<?=$enddate?>" name="START_DATE" action="POST" onKeyDown="return distInput(event,this,getDrug)"/> </td>
 	</tr>
 	<tr><td colspan="2" align="right"><input type="submit"></td></tr>
 	
  </table>

</form>

<? if ($LISTING_SEQ_NO != "") 			// Show Results!!!
{
	echo("Total before $startdate: $insBefore<br>"); 
	if (mysql_num_rows($inventoryIns) != 0) {
	echo "<table><tr><th>Date/Time</th><th>Quantity</th><th>Bottle Number</th><th>Lot No</th><th>Exp No</th><th>PatientID</th></tr>";
	$insTotal = $insBefore;
	while ($row = mysql_fetch_assoc($inventoryIns))
	{
		$insTotal += $row['quantity'];
		echo "<tr><td>${row['timestamp']}</td><td>${row['quantity']}</td><td>${row['bottleNumber']}</td><td>${row['LotNO']}</td><td>${row['expirationMonth']}-${row['expirationYear']}</td>${row['patientID']}</tr>";
	}
	
	echo "</table>";
	echo "<br><br>Grand total inventory in: $insTotal<br> as of $enddate";
	
	}

} 
	?>


<?php
include ("includes/footer.php"); 
?>


