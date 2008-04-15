<?
include ("includes/config.php");
$page = "Login"; 
// for login
if (isset($_POST['login'])) { 
	// Validate the email address.
	if (!empty($_POST['email'])) {
		$e = escape_data($_POST['email']);
	} else {
		$message .= "You forgot to enter your email address. ";
		$e = FALSE;
	}

	// Validate the password.
	if (!empty($_POST['pass'])) {
		$p = escape_data($_POST['pass']);
	} else {
		$p = FALSE;
		$message .= "You forgot to enter your password. ";
	}

	if ($e && $p) { // If everything's OK.

		$p_hash = md5($p);
		$query = "SELECT _id, firstName, pass, lastName, email FROM users WHERE email='$e' AND pass='$p_hash' AND active IS NULL";
		$result = mysql_query ($query) or trigger_error("Query: $query\n<br />MySQL Error: " . mysql_error());

		if (@mysql_num_rows($result) == 1) { // A match was made.

			// Register the values & redirect.
			$row = mysql_fetch_array ($result, MYSQL_NUM);
			mysql_free_result($result);
			$_SESSION['u'] = $row[0];
			$_COOKIE['u'] = $row[0];
			$_SESSION['e']=$e;
			$_COOKIE['e']=$e;
			$u = $row[0];
			header("Location: ./");

		}

		else { // No match was made.
			$message .= "The email address and password entered do not match. " ;
		}

	}
}
include ("includes/header.php"); 

?>

<form action="login.php" method="post">
  <table width="360" cellpadding="5" cellspacing="0" align=center>
    <tr>
      <td valign="top">&nbsp;</td>
      <td valign="top">&nbsp;</td>
    <tr>
      <td valign="top">&nbsp;</td>
      <td valign="top">&nbsp;</td>
    </tr>
      <td width="140" valign="top"> Username </td>
      <td valign="top"><input type="text" name="email" class="input" style="width:144px;" maxlength="40" value="<?php if (isset($_POST['email'])) echo $_POST['email']; ?>" /></td>
    <tr>
      <td valign="top">Password</td>
      <td valign="top"><input type="password" name="pass" class="input" style="width:144px;" maxlength="20" value="<?php if (isset($_POST['password'])) echo $_POST['password']; ?>" /></td>
    </tr>
    <tr>
      <td colspan="2" valign="top">&nbsp;</td>
    </tr>
    <tr>
      <td colspan="2" valign="top"><input type="submit" name="submit" value="login" class="button"  /> <input type=hidden name=login value=1 /></td>
    </tr>
    <tr>
      <td colspan="2" valign="top">&nbsp;</td>
    </tr>
    <tr>
      <td colspan="2" valign="top">
        user: demo <br />
        pass: xxxx </td>
    </tr>
  </table>
</form>

<?
include ("includes/footer.php"); 
?>
