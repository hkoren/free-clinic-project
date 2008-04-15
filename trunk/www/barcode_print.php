<?php
/*
 * Created on Jan 27, 2008
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
 include ("includes/config.php"); 
 include ("includes/functions.php");
 

?>
<html><head>
<style>
.code {
	width: 300px;
	
}
TD {
	font-family: arial;
	font-weight: bold;
	border: 2px solid #eee;
}
</style>
</head>
<body onLoad = "window.print()"">
<table width="100%" height=\"100%\" cellpadding="0" cellspacing="25"><tr>
<?
$message = "Free Clinic Project";
$key = 217645289;
for ($i=0; $i<15; $i++)
{
	$query = "insert into barcodes (used) values (0)";
	$result = mysql_query($query);
	$resultID = mysql_insert_id();
	$code=($resultID + 16) * $key;
	print("<td width=\"33%\" valign=\"center\" align=\"center\">");
	print("<img src=\"barcode.php?code=".leading_zeros($code,12)."\"><br/>");
	print("$message");
	print("</td>");
	if ((($i-1) % 3) == 1 && $i != 0)
	{
		print("</tr><tr>");
	}
}
?></tr></table>
</html>