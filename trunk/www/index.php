<? 
include ("includes/config.php"); 

// defining this page
$page = '';
if (isset($_GET['p'])) {
	$page = escape_data($_GET['p']);
}

// login
if (!$isLoggedIn) {
	header("Location: login.php");
}
else
{
	header("Location: checkIn.php");		
}

/*
// check_in page
elseif ($page == '') { 
include('_check_in.php'); 
} 


// enrollment page
elseif ( $page == 'enroll' ) { 
include ('_enroll.php');
}

// patient info page
elseif ( $page == 'patient' ) {
include('_patient.php');
}

// show search results 
elseif ( $page == 'search' ) {
include('_search_results.php');
}

// show reports 
elseif ( $page == 'report' ) {
include('_report.php');
}



 include ("includes/footer.php"); 
 */
 ?>