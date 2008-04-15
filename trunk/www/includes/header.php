<?
function navlink($url,$name)
{
		
	echo "<td".(strpos($_SERVER['SCRIPT_NAME'],$url)==false?"":" class=\"selected\"")."><a href=\"$url\">$name</a></td>";
	 	
}

if ($page != "Login" && !$isLoggedIn)
{
	header("Location: login.php");
}
if (!isset($page))
{
	
  $page = "";
}

?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
<head>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<script language="JavaScript" type="text/javascript" src="js/script.js"></script>
<script language="JavaScript" type="text/javascript" src="js/prototype.js"></script>
<script language="JavaScript" type="text/javascript" src="js/scriptaculous.js"></script>
<? if (isset($extraHead)) { echo $extraHead; }?>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>UCSD SRFC - <?=$page?></title>
</head>
<body<?
if (isset($onload))
	echo " onload=\"$onload\""; ?>>
<? if (!isset($noTop)) { ?>
<div id="wrap">
<div id="heading"> 
      <table width="100%" border="0" cellpadding="0" cellspacing="0">
        <tr>
          <td valign="top">
          <div style="font-size:14pt;margin-bottom:8px;">Free&nbsp;Clinic&nbsp;Project</div>
          <h3 style="float:left;margin-top:3px;margin-bottom:6px;font-size:22px;color:#666;">UCSD</h3><div style="font-size:10px;color:#999;line-height:11px;margin-bottom:3px;">Teams In<br>Engineering<br>Service</div></td>
          <td width="75%">
<? if ($isLoggedIn) { ?>
			<table width="100%" class="topNav">
			<tr>	
		  <?
		  	navLink("checkIn.php", "Check-In");
		  	navLink("enroll.php", "Enrollment");
			navLink("pharmacy.php", "Pharmacy"); 
		  	if ($isAdmin) { 
		  		navLink("admin.php","Admin");
			}
			echo "</tr></table>";
		} 
	else {?>Log In<?	} ?>
  
		  </td>
		  
<? if ($isLoggedIn) { ?>		  
          <td align="right"> 
          user: <?=$user?> <a href="logout.php">Log Out</a>
<form name="searchForm" method="get" id="searchForm" action="search.php">
  <input id="name" name="name" type="text" autocomplete="off" maxlength="30" class="input" value="<? if (isset($_GET['name'])) echo stripslashes($_GET['name']); ?>"  /> 
<input type="submit"  value="Search" onclick="" />
</form>
</td>
<? } ?>
</tr>
      </table></div><? if ($patientID != ""){?><table width="100%" cellpadding="0" cellspacing="0">
<tr class="patientBar">
	<td><a href="enroll.php?patientID=<?=$patientID?>">Enrollment</a></td>
	<td><a href="visitPlanner.php?patientID=<?=$patientID?>">Visit Planner</a></td>
	<td><a href="clinicVisit.php?patientID=<?=$patientID?>">Clinic Visit</a></td>
	<td><a href="patient.php?patientID=<?=$patientID?>">Patient Information</a></td>
	<td align="right"><h1><?=$page?></h1></td>
</tr>
</table>
<?}
?>

	<div id="autocomplete" style="display:none;border:1px solid black;background-color:white;height:250px;width:200px;overflow:auto;text-align:left"></div>
<? 
if ($message != '') echo "<div class=message>$message</div>";
?>	
<table width="860" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="300" valign="top"><? } ?>
	<? if ($patientID == "") { echo "<h1>$page</h1>"; } ?>
<!--	Page content starts here-->
<div id="messageReactionBox" style="display:none;" class="searchBox"></div>
