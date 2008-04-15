<?php
include ("includes/config.php"); 
$page="Drug Routes";

include ("includes/header.php"); 
/*
 * Created on May 24, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
 
 $route = isset($_GET["route"])?$_GET["route"]:""; 
 
 if ($route == "")
 {
	 $sql = "SELECT * FROM NDC_tblroute order by TRANSLATION ";
 	 $result = mysql_query($sql);
	 while (($row=mysql_fetch_assoc($result)) != null) {
		echo "<div class=\"route\"><a href=\"pharmacy_ndc_routes.php?route=${row["ROUTE"]}\">${row["TRANSLATION"]}</a></div>\n";
	 }
 } 
 else
 {
 	$sql = "SELECT * FROM NDC_tblroute where ROUTE='$route'";
 	$result = mysql_query($sql);
 	if (mysql_num_rows($result) != 0)
 	{
 		$row = mysql_fetch_assoc($result);
 		$translation = $row["TRANSLATION"];
 		echo "<h1>$translation</h1>";
 		
 		// Get the drugs!
 		
 		$sql = "SELECT r.*, l.* from NDC_routes r, NDC_listings l where r.LISTING_SEQ_NO = l.LISTING_SEQ_NO and r.ROUTE_CODE = '$route'";
 		$result = mysql_query($sql);
		while (($row=mysql_fetch_assoc($result)) != null) {
			echo "<div class=\"route\"><a href=\"pharmacy_ndc_drug.php?LISTING_SEQ_NO=${row["LISTING_SEQ_NO"]}\">${row["TRADENAME"]}</a></div>\n";
		}
 	}
 }
include ("includes/footer.php"); 
?>
