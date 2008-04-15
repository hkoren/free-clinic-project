<?php
include("includes/config.php");
include("includes/message.php"); 
addJavaScript("message.js");
if (isset($_GET['messageID']))
{
	$messageID=$_GET['messageID'];
} else trigger_error("No messageID supplied");
if (isset($_POST['name']))
{
	$name = escape_data($_POST['name']);
	$shortName = escape_data($_POST['shortName']);
	$messageTypeID = $_POST['messageTypeID'];
	$icd9 = $_POST['icd9'];
	$onEnrollForm = isset($_POST['onEnrollForm'])? 1 : 0;
	$query = "update messages set name = '$name', shortName='$shortName', messageTypeID=$messageTypeID, icd9='$icd9', onEnrollForm=$onEnrollForm where _id=$messageID";
	$result = mysql_query($query) or trigger_error(mysql_error()); 
	
	if ($_POST["newReaction"] != "")
	{
		$reactionMessageID = $_POST["newReactionMessageID"];
		$query = "insert into messageReaction(messageID, reactionMessageID)" .
				" values($messageID, $reactionMessageID)";
		$result = mysql_query($query) or trigger_error(mysql_error());
		echo "<script language=\"JavaScript\">navigate('message.php?messageID=$messageID');</script>";	
	}
	if ($_POST["newReaction"] != "")
	{
		$parentMessageID = $_POST["newParentMessageMessageID"];
		$query = "insert into messageReaction(messageID, reactionMessageID)" .
				" values($messageID, $reactionMessageID)";
		$result = mysql_query($query) or trigger_error(mysql_error());
		echo "<script language=\"JavaScript\">navigate('message.php?messageID=$messageID');</script>";
	}
	
	echo "<script language=\"JavaScript\">history.go(-2);</script>";
}



$query="select m.*, mt.name as typeName from messages m inner join messageType mt on m.messageTypeID=mt._id where m._id=$messageID";
$result=mysql_query($query) or trigger_error(mysql_error());
$message=mysql_fetch_assoc($result);
$messageTypeName=$message['typeName'];
$page="Editing $messageTypeName #$messageID";

include ("includes/header.php");

/*
 * Created on May 24, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
?>

<form method="post" action="message.php?messageID=<?=$messageID?>" name="form1">
	<table>
	<tr><td>ID</td><td><?=$message['_id']?></td></tr>
	<tr><td>Name</td><td><input type="text" name="name" size="55" value="<?=$message['name']?>"></td></tr>
	<tr><td>Short Name</td><td><input type="text" name="shortName" size="55" value="<?=$message['shortName']?>"></td></tr>
	<tr><td>Type</td><td><?=messageTypeSelect($message['messageTypeID'])?></td></tr>
	<tr><td>ICD9 Code</td><td><input type="text" name="icd9" size="10" value="<?=$message['icd9']?>"></td></tr>
	<tr><td>Category</td><td><?=categorySelect($message['categoryID'])?></td></tR>
	<tr><td><label for="onEnrollForm">on Enroll Form</label></td><td><input type="checkbox" name="onEnrollForm" value="1"<?if ($message['onEnrollForm']) echo " checked"; ?>>
	</table>
<input type="submit" value="update">
<h3>Inherit Behavior From</h3>
<? 
messageSearch("newParentMessage","newParentMessage");
?>
<h3>Triggers</h3>
<input type="button" value="Add Date Trigger" onclick="addDate()"> <input type="button" value="Add Conditional Trigger" onclick="addConditional()">
<div id="trigger">
</div>


<h3>Reaction</h3>
<?
$query = " select mr.*, m.* from messageReaction mr" .
		" inner join messages m on m._id = reactionMessageID" .
		" where mr.messageID=$messageID";
$result = mysql_query($query) or die(mysql_error());
$reaction_count = mysql_num_rows($result);

if ($reaction_count > 0) {
	print("<table width=\"50%\" border=\"1\" cellpadding=\"0\"><tr><th>MessageID</th><th>Name</th><th>Schedule</th><th width=\"1%\">Delete</th></tr>");
	
	while($reaction = mysql_fetch_assoc($result))
	{
		print("<tr><td>{$reaction['_id']}</td><td>{$reaction['name']}</td><td>yearly</td><td><input type=\"submit\" name=\"deleteReaction{$reaction['_id']}\" value=\"&gt;&lt;\"></td></tr>");
	}
	print("</table>");
}
messageSearch("newReaction", "newReaction");
?>

<input type="submit" name="addReaction" value="Add Reaction">

<h2></h2>
</form>
<? 
include ("includes/footer.php"); 
?>
