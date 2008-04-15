<?php
include ("includes/config.php"); 
$page="Drug";
$onload="document.drug.s.focus();";
include ("includes/header.php"); 
/*
 * Created on May 24, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
include "includes/pharmacy_nav.php"; 
 
 $LISTING_SEQ_NO = isset($_GET["LISTING_SEQ_NO"])?$_GET["LISTING_SEQ_NO"]:""; 
 
  $startletter = isset($_GET["s"])? $_GET["s"]:""; 
  
 if ($LISTING_SEQ_NO != "")
 {
 	
 	$from = " from NDC_listings L inner join NDC_firms F on F.LBLCODE = TRIM(LEADING '0' from L.LBLCODE) ";
 	$where = " where LISTING_SEQ_NO = $LISTING_SEQ_NO ";
     
     // First determine count
	$sqlCount = "SELECT count(LISTING_SEQ_NO) as count $from $where";
	$result = mysql_query($sqlCount) or die(mysql_error()." query= $sqlCount");
     
	// Now get page 
	$sql = "SELECT L.*, F.* $from $where ";
 	$result = mysql_query($sql) or die(mysql_error()." query= $sql");
	while (($row=mysql_fetch_assoc($result))!=null)
	{		
		$lblcode   = $row["LBLCODE"];
		$prodcode  = $row["PRODCODE"];
		$strength  = $row["STRENGTH"];
		$unit      = $row["UNIT"];
		$rx_otc    = $row["RX_OTC"]=="R"?"Perscription":"Over The Counter";
		$tradename = $row["TRADENAME"];
		?>

<table>
<tr><td>Trade Name</td><td><?=$tradename?></td></tr>
<tr><td>Label Code</td><td><a href="pharmacy_ndc_firms.php?firm=<?=$lblcode?>"><?=$lblcode?></a></td></tr>
<tr><td>Product Code</td><td><?=$prodcode?></td></tr>
<tr><td>Strength</td><td><?=$strength." ".$unit?></td></tr>
<tr><td>Availability</td><td><?=$rx_otc?></td></tr>
</table>
<h1>Packages</h1>
<table>
<tr><th>Code<th>Size</th></tr>
<?
		$sql = "select * from NDC_packages where LISTING_SEQ_NO = $LISTING_SEQ_NO";
		$packages = mysql_query($sql) or die(mysql_error()." query: $sql");
		while (($row = mysql_fetch_assoc($packages))!=null)
		{
			$pkgcode         = $row["PKGCODE"];
			$packsize        = $row["PACKSIZE"];
			$packtype        = $row["PACKTYPE"];
			echo "<tr><td>$pkgcode</td><td>$packsize $packtype</td></tr>";
		}
?>
</table>
<h1>Formulations</h1>
<table>
<tr><th>Ingredient</th><th>Strength</th></tr>
<?
		$sql = "select * from NDC_formulations where LISTING_SEQ_NO = $LISTING_SEQ_NO";
		$formulations = mysql_query($sql) or die(mysql_error()." query: $sql");
		while (($row = mysql_fetch_assoc($formulations))!=null)
		{
			$strength = $row["STRENGTH"];
			$unit = $row["UNIT"];
			$ingredient_name = $row["INGREDIENT_NAME"];
			echo "<tr><td><a href=\"pharmacy_ndc_formulations.php?name=".urlencode($ingredient_name)."\">$ingredient_name</a></td><td>$strength $unit</td></tr>";
		}
		echo "</table>";
		
		$sql = "select * from NDC_applications where LISTING_SEQ_NO = $LISTING_SEQ_NO";
		$apps = mysql_query($sql) or die(mysql_error()." query: $sql");
		if (mysql_num_rows($apps) != 0)
		{
			echo "<h2>FDA Drug Data</h2>";
			while (($row = mysql_fetch_assoc($apps)) != null) {
				$APPL_NO = $row['APPL_NO'];
				$PROD_NO = $row['PROD_NO'];
				// Query Doc Data
				
				$sql = "select * from FDA_AppDoc where ApplNo='$APPL_NO'";
				$docs = mysql_query($sql) or die(mysql_error()." query: $sql");
				$count = mysql_num_rows($docs);
				if ($count == 0)
				{
					echo "No FDA Docs Found for Application Number '$APPL_NO'";
				}
				else {
					echo "$count documents";
					echo "<table><tr><th>AppDocID</th><th>ApplNo</th><th>SeqNo</th><th>DocType</th><th>Doc</th><th>DocDate</th><th>ActionType</th><th>DuplicateCounter</th></tr>";
					while (($doc = mysql_fetch_assoc($docs)) != null) {
						$AppDocID = $doc["AppDocID"];
						$docLink = "<a href=\"${doc["DocURL"]}\">". ($doc["DocTitle"]!=""?$doc["DocTitle"]:$doc["DocURL"])."</a>";
						echo "<tr><td>$AppDocID</td><td>${doc["ApplNo"]}</td><td>${doc["SeqNo"]}</td><td>${doc["DocType"]}</td><td>$docLink</td><td>${doc["DocDate"]}</td><td>${doc["ActionType"]}</td><td>${doc["DuplicateCounter"]}</td></tr>";
					}
					echo "</table>";
				}
			}
		}
	}
}
include ("includes/footer.php"); 
?>