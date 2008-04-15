<?php
	setcookie ("u", "", time()-300, '/', ''); // destroy auto login info
	$_SESSION = array(); // Destroy the variables.
	@session_destroy(); // Destroy the session itself.
	setcookie (session_name(), '', time()-300, '/', '', 0); // Destroy the cookie.
	header("Location: ./");	
?>
