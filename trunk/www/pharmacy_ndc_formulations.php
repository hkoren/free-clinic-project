<?php
include ("includes/config.php"); 
$page="Drug formulations";

include ("includes/pharmacy.php"); 
include ("includes/header.php"); 
/*
 * Created on May 24, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
include "includes/pharmacy_nav.php"; 
 
$name = isset($_GET["name"])?$_GET["name"]:""; 
 
$from= " from NDC_formulations F";
if ($name == "")
{
 	 	 // First determine total number of records
 	 $sql = "SELECT count(LBLCODE) as count FROM NDC_formulations $where";
 	 $result = mysql_query($sql);
 	 $row = mysql_fetch_assoc($result);
 	 $rows = $row["count"];
 	 
	$sql = "SELECT distinct INGREDIENT_NAME $from order by INGREDIENT_NAME asc"; 	
	 $result = mysql_query($sql);
	while (($row=mysql_fetch_assoc($result)) != null) {
	echo "<div class=\"route\"><a href=\"pharmacy_ndc_formulations.php?name=${row["INGREDIENT_NAME"]}\">${row["INGREDIENT_NAME"]}</a></div>\n";
	}
} 
else
{
	$where = "where INGREDIENT_NAME like '%$name%'";
	
 	 
 	 // First determine total number of records
 	 $sql = "SELECT count(INGREDIENT_NAME) as count FROM NDC_formulations $where";
 	 $result = mysql_query($sql);
 	 $row = mysql_fetch_assoc($result);
 	 $rows = $row["count"];

 	$sql = "SELECT f.* FROM NDC_formulations f $where";
 	$result = mysql_query($sql);
 	 
 	if (mysql_num_rows($result) != 0)
 	{
 		$row = mysql_fetch_assoc($result);
 		$ingredient_name = $row["INGREDIENT_NAME"];
 		echo "<h1>$ingredient_name</h1>";
 		
 		// Get the drugs!
 		
	 	$sql = "SELECT f.*, l.* FROM NDC_formulations f, NDC_listings l where l.LISTING_SEQ_NO = f.LISTING_SEQ_NO and INGREDIENT_NAME='$name' ORDER BY Tradename asc";
 		$result = mysql_query($sql) or die(mysql_error(). " query: $sql");
 		page_top();
		while (($row=mysql_fetch_assoc($result)) != null) {
			show_ndc_listing_div($row);
		}
		page_bottom();
 	}
 }
include ("includes/footer.php"); 
?>
