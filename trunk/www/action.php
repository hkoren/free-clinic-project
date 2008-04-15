<?
// Part of the AJAX framwork

require('includes/config.php') ;

if(isset($_GET['column_1']) ) {
	
	$cookie = '';
	foreach ($_GET['column_1'] AS $value) {
		$cookie .= $value.",";
	}
	setcookie('column_1', $cookie, $cookie_date , '/' );

}



if(isset($_GET['column_2']) ) {
	
	$cookie = '';
	foreach ($_GET['column_2'] AS $value) {
		$cookie .= $value.",";
	}
	setcookie('column_2', $cookie, $cookie_date , '/' );
}






if (isset($_GET['checkInDone'])) {
	$id = (int) $_GET['checkInDone'];
	$patientID = (int) $_GET['checkInDone'];
	
	get_info('checkIns', 'name', "WHERE _id = $id", 't');
	
	$name = $r['t']['name'];

	update('checkIns', 'status = 1, patientID = $patientID', "WHERE _id = $id ");

}
?>