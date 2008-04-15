<?

DEFINE ('DB_USER', 'root');
DEFINE ('DB_PASSWORD', 'srfcdbpw');
DEFINE ('DB_HOST', 'srfc.ucsd.edu');
DEFINE ('DB_NAME', 'clinic');

if ($dbc = mysql_connect (DB_HOST, DB_USER, DB_PASSWORD)) { // Make the connnection.

	if (!mysql_select_db (DB_NAME)) { 
	
		trigger_error("Could not select the database!\n<br />MySQL Error: " . mysql_error());
		exit();
		
	} // End of mysql_select_db IF.
	
} else { // If it couldn't connect to MySQL.

	trigger_error("Could not connect to MySQL!\n<br />MySQL Error: " . mysql_error());
	exit();
	
} // End of $dbc IF.


?>