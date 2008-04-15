<?php
/*
 * Created on Jan 25, 2008
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
 
function location_changer()
{
	//print "<form method=\"get\" action=\"#\">";
	print "<select name=\"locationID\">";
	$query = "select * from locations";
	$result=mysql_query($query) or trigger_error(mysql_error());
	while (($location = mysql_fetch_assoc($result)) != null) {
		print "<option value=\"${location['_id']}\"".($location["_id"]==$_SESSION["locationID"]?" selected":"").">${location['name']}</option>\n";
	}
	
	print "</select>";
	//"<input type=\"submit\" value=\"change\"></form>";
	
} 
?>
