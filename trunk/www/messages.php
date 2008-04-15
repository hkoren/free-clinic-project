<?php
include ("includes/config.php"); 
require_once("includes/message.php");
// ADD NEW
if (isset($_POST['add']))
{
	$name = $_POST['name'];
	$messageTypeID = $_POST['messageTypeID'];
	$query = "insert into messages(name,messageTypeID) values('$name',$messageTypeID)";
	$result = mysql_query($query);
	$messageID = mysql_insert_id();
	header("Location : message.php?messageID=$messageID");
}


$page = "Messages";
addJavaScript("MochiKit/MochiKit.js");
addJavaScript("sortable_tables.js");
include ("includes/header.php");



$messageTypeID="";		
$queryString="?";
if (isset($_GET['messageTypeID']) and $_GET['messageTypeID'] != '')
{
	$messageTypeID = $_GET['messageTypeID'];
	$query = "select * from messageType where _id = $messageTypeID";
 	$result = mysql_query($query) or trigger_error(mysql_error());
	$row = mysql_fetch_array($result, MYSQL_ASSOC);
	$messageTypeName = $row['name'];
	$queryString.="messageTypeID=$messageTypeID&";
}
$search='';
if (isset($_GET['search']))
{
	$search = $_GET['search'];
	$queryString.="search=".urlencode($_GET['search'])."&";
}

echo "<p><form action=\"messages.php\" method=\"get\">" .
	"Search:<input type=\"text\" name=\"search\" value=\"$search\">&nbsp;&nbsp;".
	"Type:".messageTypeSelect($messageTypeID)."&nbsp;&nbsp; <input type=\"submit\" value=\"Search\">" .
	"</form></p>" ;

if ($search != "" || $messageTypeID != "")
{
	// First determine where statement for search
	$where ="";
	if ($search != "")
	{
		$where = " messages.name like '%$search%'";
		if ($messageTypeID != "") $where .= " and ";
	}
	if ($messageTypeID != "")
	{
		$where .= " messages.messageTypeID = $messageTypeID";
	}	
	
	// Count the number of records that would be found
	$queryCount ="select count(messages._id) as count from messages";
	
	// Get page of records
	$queryPage ="select messages.*, c1.name as categoryName, c2.name as parentCategoryName " .
			"from messages " .
			"left outer join categories c1 on messages.categoryID = c1._id " . 
			"left outer join categories c2 on c1.parentCategoryID=c2._id";
	// Attach where
	
	if ($where != "")
	{
		$queryPage .= " where $where";
		$queryCount.= " where $where";
	}
	// Pagination limit
	$queryPage .= ' limit ' .($pageNum - 1) * $pageRows .',' .$pageRows;
	
	$messageCount = mysql_query($queryCount) or trigger_error(mysql_error());
	$result = mysql_fetch_assoc($messageCount);
	$rows = $result['count'];
	$messages = mysql_query($queryPage) or trigger_error(mysql_error());
	
	
	showMessageTable($messages, $rows);
}

?>
<h2>Add New</h2>
<form action="messages.php" method="post">
	<input type="hidden" name="add" value="1">
	
	<table>
	<tr><td>Name</td><td><input type="text" name="name" size="55"></td></tr>
	<tr><td>Type</td><td><?=messageTypeSelect('')?></td></tr>
	</table>
	<input type="submit" value="Add New">
	<div class="customParameters">
	(custom parameters go here)
	</div>
</form>
<?
include ("includes/footer.php"); 
?>