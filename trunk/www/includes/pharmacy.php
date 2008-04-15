<?
// Pharmacy Functions


function UPC_checksum($NDC)
{
	$length = strlen($NDC);
	if ($length ==11) {							// Generate the chcksum
		return make_checksum($NDC);	
	}
	elseif ($length ==12) {
		$checksum = (int)$NDC[$length-1];							// Validate the checksum
		return $checksum == make_checksum($NDC);					// Return comparison
	}
	else return -1;
}


function make_checksum($CODE)
{
	$odd_codes = 0;
	$even_codes = 0;
	$lastdig = 0;
	$checksum = 0;
	for($x = 0; $x <=10; $x+=2) {
			$odd_codes += $CODE[$x];
	}
	for($y = 0; $y <=9; $y+=2) {
			$even_codes += $CODE[$y];
		}
	$total = (3 * $odd_codes) + $even_codes;
	
	$lastdig = (int)$total[strlen($total)-1];
	if($lastdig == 0) {
		$checksum = 0 ;
	} else {
		$checksum = 10 - $lastdig;
	}
	return $checksum;
}

function show_NDC($ndc) {

}

function show_LISTING_SEQ_NO($LISTING_SEQ_NO) {

}

function show_ndc_listing_row($cursor) {
	$LISTING_SEQ_NO = $cursor["LISTING_SEQ_NO"];
	$name           = $cursor["TRADENAME"];
	$strength       = $cursor["STRENGTH"];
	$unit           = $cursor["UNIT"];
	$rx_otc         = $cursor["RX_OTC"];
	$prodcode       = $cursor["PRODCODE"];
	echo "<tr><td><a href=\"pharmacy_ndc_drug.php?LISTING_SEQ_NO=$LISTING_SEQ_NO\">$LISTING_SEQ_NO</td><td>$name</td><td>$strength $unit</td></tr>";
}

function show_ndc_listing_div($cursor) {
	$LISTING_SEQ_NO = $cursor["LISTING_SEQ_NO"];
	$name           = $cursor["TRADENAME"];
	$strength       = $cursor["STRENGTH"];
	$unit           = $cursor["UNIT"];
	$rx_otc         = $cursor["RX_OTC"];
	$prodcode       = $cursor["PRODCODE"];
	echo "<div class=\"route\"><a href=\"pharmacy_ndc_drug.php?LISTING_SEQ_NO=$LISTING_SEQ_NO\">$name</a></div>\n";
}

function show_ndc_firm_div($cursor) {
	echo "<div class=\"route\"><a href=\"pharmacy_ndc_firms.php?firm=${cursor["LBLCODE"]}\">${cursor["FIRM_NAME"]}</a></div>\n";
}
?>
