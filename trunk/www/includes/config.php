<? 
if (getenv('TZ') === false) {
    putenv('TZ=America/Los_Angeles');
}

$message = '';
$self = basename($_SERVER['PHP_SELF']);
$date = time();
$cookie_date = $date + 99999999;

require('db_connect.php') ;
require('constants.php') ;
require('functions.php') ;

session_start();


$live = FALSE;
$email = 'henry@freeclinicproject.org';

function my_error_handler ($e_number, $e_message, $e_file, $e_line, $e_vars) {
	global $live, $email;
	$message = "An error occurred in script '$e_file' on line $e_line: \n<br />$e_message\n<br />";
	$message .= "Date/Time: " . date('n-j-Y H:i:s') . "\n<br />";
	$message .= "<pre>" . print_r ($e_vars, 1) . "</pre>\n<br />";

	if ($live) { // Don't show the specific error.
		error_log ($message, 1, $email); // Send email.
		if ($e_number != E_NOTICE) {
			echo '<div id="Error">A system error occurred. We apologize for the inconvenience.</div><br />';
		}
	} else { // Development (print the error).
		echo '<div id="Error">' . $message . '</div><br />';
	}
} // End of my_error_handler() definition.

set_error_handler ('my_error_handler');

$site_name = "Clinic";
$site_title = "Clinic - ";
$site_url = "http://www.clinic.com/";
$site_email = "info@clinic.com";
$max_records_found = 50000;

$location_current = 1;
if (isset($_GET["locationID"])) {
	$_SESSION["locationID"]	= (int) $_GET["locationID"];
}
else if (isset($_POST["locationID"])) {
	$_SESSION["locationID"]	= (int) $_POST["locationID"];
}
if (!isset($_SESSION["locationID"])) {
	$_SESSION["locationID"] = 1;
}
if (!isset($_SESSION["locationName"]))
{
	$query = "select * from locations where _id = ". $_SESSION["locationID"];
	$result = mysql_query($query) or trigger_error(mysql_error());
	$row = mysql_fetch_assoc($result);
	$_SESSION["locationName"] = $row["name"];
}


// detect login
$isLoggedIn = (isset($_SESSION['u']) && (int) $_SESSION['u'] > 0) ? true : false;
$userID = ( isset($_SESSION['u']) ) ? (int) $_SESSION['u'] : 0; 
$user = ( isset($_SESSION['e']) ) ? $_SESSION['e'] : "";

if (!isset($page)) $page="";

// detect administrator
$isAdmin = true;
if (isset($_GET['patientID'])) $patientID = (int) $_GET['patientID']; 
elseif (isset($_POST['patientID'])) $patientID = (int) $_POST['patientID'];
else $patientID="";
if ($patientID!="")
{
	$query ="select * from patients where _id=$patientID";
	$result = mysql_query($query) or trigger_error(mysql_error() . " - $query");
	$patient = mysql_fetch_assoc($result);
}

// PAGINATION
##############################
// Determine page we are on
if (isset($_GET['pageNum']))
{
	$pageNum = $_GET['pageNum'];
}
else
{
	$pageNum = 1;
}
//This is the number of results displayed per page
$pageRows = 15;
?>