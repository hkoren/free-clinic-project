<?php
include ("includes/config.php"); 
include ("includes/pharmacy.php"); 
if (isset($_GET["s"]))
{
	$search = $_GET["s"];
}
if (isset($_GET['mode'])) {
	$mode = $_GET['mode'];
}
else {
	$mode = "html";
}


$ndc ="";
$keyword = "";
$error ="";
$header = "";
if ($search != "") {
	
	$len = strlen($search);
	if ($len==10)
	{
		$ndc = $search;
	}
	if(is_numeric($search))
	{
		if ($len==12 && ($search[0]=='3')) {			// It's a NDC code
		
			// TODO: Validate checksum
			$ndc = substr($search,1,10);
		}
		else
		{
			$error = "UPC Not Implemented";
		}
	}	
	else										// It's a keyword
	{
		$keyword = $search;	
	}
	
}
if ($ndc != "") {
	$query = "SELECT NDC_barcodes.*, NDC_firms.* FROM NDC_barcodes inner join NDC_firms on NDC_firms.LBLCODE = NDC_barcodes.LBLCODE WHERE barcode = '$ndc'";
	$header = "Searching for NDC code $ndc";
}
else if ($keyword != "")
{
	$query = "SELECT * FROM NDC_listings WHERE TRADENAME like '%$keyword%'";
	$header = "Searching for keyword '$keyword'";
}
$drugs = mysql_query($query) or die(mysql_error(). " query: $query");
$count = mysql_num_rows($drugs);

if ($count ==1 && $mode == "html")
{ 
	$row=mysql_fetch_assoc($drugs);
	$LISTING_SEQ_NO = $row["LISTING_SEQ_NO"];
	header("Location: pharmacy_ndc_drug.php?LISTING_SEQ_NO=$LISTING_SEQ_NO");
}

$page="Drug Routes";

if ($mode == "html")
{
	include ("includes/header.php");
	include "includes/pharmacy_nav.php";	
} 


// Show Results

if ($search!="")
{
	if ($mode == "html") {
		echo "<h2>$header</h2>";
		if ($error != "") {
			echo "<h1>Error</h1>$error";
		}
		if ($count == 0) {
			echo "no records found";
		} 
		else {
			echo "<table><tr><th>LISTING_SEQ_NO</th><th>Trade Name</th><th>Size</th></tr>";
			while (($row = mysql_fetch_assoc($drugs)) != null ) {
				show_ndc_listing_row($row);
			}
			echo "</table>";
			include "includes/footer.php"; 
		}
	} elseif ($mode == "json")
	{
		$totalRows = array();
		while (($row = mysql_fetch_assoc($drugs)) != null ) {
				array_push($totalRows, $row);
			}
		echo json_encode($totalRows);
	}
}

 
?>