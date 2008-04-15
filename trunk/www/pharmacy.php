<?php
include ("includes/config.php"); 
$page="Pharmacy";

include ("includes/header.php"); 
/*
 * Created on Feb 7, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
?>

<IMG id=u0 src="Resources/transparent.gif" style="position:absolute; left:20px; top:20px; width:111px; height:96px"   >

<? include "includes/pharmacy_nav.php"; ?>

<div style="float: right;">

</div>

<DIV class="section"><a href="pharmacy_consult.php">Pharmacy Consultation</a></DIV>
<DIV class="section"><a href="pharmacy_inventory.php">Pharmacy Drug Inventory</a></DIV>
<ul>
<li><a href="pharmacy_add_drug.php?add=1">Add Drug to Inventory</a></li>
<li><a href="pharmacy_inventory_loss.php">File Loss Report</a></li>
<li><a href="pharmacy_inventory_audit.php">Audit Inventory</a></li>
</ul>
<DIV class="section"><a href="pharmacy_expire.php">Expiration Reports</a></DIV>
<div class="alert">! Alert ! drugs running out</div>
<DIV class="section"><a href="pharmacy_reorder.php">Re-Order Reports</a></DIV>
<div class="alert">! Alert ! drugs expiring soon</div>

<? 
include ("includes/footer.php"); 
?>
