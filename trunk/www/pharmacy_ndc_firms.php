<?php
include ("includes/config.php"); 
include ("includes/pharmacy.php"); 
$page="Drug Firms";
global $pageRows;
$pageRows = 30;
$queryString = "1=1";
$script = $_SERVER["SCRIPT_NAME"];
include ("includes/header.php"); 
include "includes/pharmacy_nav.php"; 
/*
 * Created on May 24, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
 
 $firm = isset($_GET["firm"])?leading_zeros($_GET["firm"],6):"";
 
 $startletter = isset($_GET["s"])? $_GET["s"]:""; 
 
 if ($firm == "")
 {
 	 letterlinks("pharmacy_ndc_firms.php?s=");
 	 if ($startletter != "")
 	 {
 	 	$where = "where FIRM_NAME like '$startletter%'";
 	 	$queryString .= "&s=" . urlencode($startletter);
     }
     else $where="";

	 // First determine total number of records
 	 $sql = "SELECT count(LBLCODE) as count FROM NDC_firms $where";
 	 $result = mysql_query($sql);
 	 $row = mysql_fetch_assoc($result);
 	 $rows = $row["count"];
 	 
 	 // Now query the results
 	 $sql = "SELECT LBLCODE, FIRM_NAME FROM NDC_firms $where order by FIRM_NAME  " . page_limit();
 	 $result = mysql_query($sql);
 	 page_top();
	 while (($row=mysql_fetch_assoc($result)) != null) {
		show_ndc_firm_div($row);
	 }
	 page_bottom();
 } 
 else
 {
 	$sql = "SELECT * FROM NDC_firms where LBLCODE='$firm' " . page_limit();
 	$result = mysql_query($sql);
 	if (mysql_num_rows($result) != 0)
 	{
 		$row = mysql_fetch_assoc($result);
 		$name = $row["FIRM_NAME"];
 		$address = $row["ADDR_HEADER"];
 		$street = $row["STREET"];
 		$po_box = $row["PO_BOX"];
 		$foreign_addr = $row["FOREIGN_ADDR"];
 		$city = $row["CITY"];
 		$state = $row["STATE"];
 		$zip = $row["ZIP"];
 		$province = $row["PROVINCE"];
 		$country_name = $row["COUNTRY_NAME"];
 		echo "<h1>$name</h1>$address<br>$street<br>$po_box<br>" .
 				"$foreign_addr<br>$city, $state $zip<br>" .
 				"$province $country_name<br>";
 		
 		
 		// Get the drugs!
 		$where = "where LBLCODE = '$firm'";
 		$queryCount ="select count(LISTING_SEQ_NO) as count from NDC_listings L $where";

 		$queryFirms = "SELECT L.* from NDC_listings L $where order by TRADENAME";
 		$result = mysql_query($queryFirms) or die(mysql_error() + " query: $queryFirms");
 		$rows = mysql_num_rows($result);
 		page_top();
		while (($row=mysql_fetch_assoc($result)) != null) {
			show_ndc_listing_div($row);
		}
		echo "<br clear=\"left\">";
		page_bottom();
 	}
 }
 

 
include ("includes/footer.php"); 
?>
