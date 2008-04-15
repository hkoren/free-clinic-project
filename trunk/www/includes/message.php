<?
/*
 * This include defines functions for interacting with the messaging framework
 */


function messageSearch($fieldName, $fieldID, $value="")
{
	?>
	<input id="<?=$fieldID?>" name="<?=$fieldName?>" type="text" autocomplete="off"  
		class="input" value="" onkeydown="messageSearch('<?=$fieldName?>', '<?=$fieldID?>', event)">
		<input type="hidden" id="<?=$fieldName?>MessageID" name="<?=$fieldName?>MessageID" value="">
		<div align = "left" id="<?=$fieldName?>_SearchWrapper">
			<div id="<?=$fieldName?>_SearchBox" class="searchBox" ></div>
		</div>
	<?
}

function addPatientMessage($patientID, $messageID, $configuration)
{
	$query = "insert into patientMessage(patientID, messageID, timeStamp)";
	$result = mysql_query($query);	
}



function changePatientMessageStatus($messageID, $newStatusID)
{
	
}
function messageTypeSelect($selected)
{	
	$query = "select * from messageType order by name asc";
	$messageTypes=mysql_query($query);
	$output = "<select name=\"messageTypeID\"><option></option>";
	while ($messageType = mysql_fetch_array($messageTypes, MYSQL_ASSOC)) {
		$extraText="";
		if ($messageType['_id']==$selected) $extraText=" selected";
		$output .= "<option value=\"${messageType['_id']}\"$extraText>${messageType['name']}</option>";
	}
	$output .= "</select>";
	return $output;
}

function showPatientMessages($patientID)
{
	$query="select pm.*, m.*, c1.name as categoryName, c2.name as parentCategoryName " .
			"from patientMessages pm " .
			"inner join messages m on m._id = pm.messageID " .
			"left outer join categories c1 on m.categoryID = c1._id " . 
			"left outer join categories c2 on c1.parentCategoryID=c2._id " .
			"where pm.patientID=$patientID";
	$messages=mysql_query($query) or trigger_error(mysql_error());
	showMessageTable($messages);
}

function showMessageTable($messages, $rows="", $script="messages.php")
{
	if ($rows == "")
		$rows = mysql_num_rows($messages);
	global $pageNum, $pageRows,$queryString;
	if ($rows==0)
	{
		echo "None Found";
	}
	else
	{
	
		//This tells us the page number of our last page
		$lastPage = ceil($rows/$pageRows);
		
		//this makes sure the page number isn't below one, or more than our maximum pages
		if ($pageNum < 1)
		{
			$pageNum = 1;
		}
		elseif ($pageNum > $lastPage)
		{
			$pageNum = $lastPage;
		}
		$startMsg = $pageRows * ($pageNum-1);
		$endMsg = $startMsg + $pageRows;
		
		// Inform user of results
		echo "<div class=\"resultCount\">Showing messages $startMsg to $endMsg out of $rows found</div>";
		echo "<table id=\"sortable_table\" class=\"datagrid\"><thead>" .
				"<tr><th mochi:format=\"str\">ID</th>".
					"<th mochi:format=\"str\">Name</th>".
					"<th mochi:format=\"str\">ICD9</th>" .
					"<th mochi:format=\"str\">Category</th>".
					"<th mochi:format=\"str\">Parent Category</th></tr>" .
				"</thead><tbody>";
	
		while ($message=mysql_fetch_assoc($messages))
		{
			echo "<tr><td>${message['_id']}</td><Td><a href=\"message.php?messageID=${message['_id']}\">${message['name']}</a></td>";
			echo "<td>${message['icd9']}</td>" .
					"<td>${message['categoryName']}</td><td>${message['parentCategoryName']}</td></tr>";
		}
		
		echo "</tbody></table><br>";
		
		// Show Pagination  ...shamelessly stolen from google
		$bottomPage = max(1,$pageNum-10);
		$topPage = min($pageNum+9,$lastPage);
		echo "</center><div class=\"pagination\"><table align=\"center\"><tr>";
		if ($pageNum >1) {
			echo "<td><a href=\"$script${queryString}pageNum=".($pageNum-1)."\"><strong>Previous</strong></a></td>";
		}
		
		for($i = $bottomPage; $i<=$topPage; $i++)
		{
			if ($i != $pageNum)					// Page they can link to 
			{
				echo "<td><a href=\"$script${queryString}pageNum=$i\"><div class=\"s\">*</div>$i</a></td>";
			}
			else								// Selected page
			{
				echo "<td><div class=\"sSelected\">^</div>$i</a></td>";	
			}
			
		}
		if ($pageNum != $lastPage) {
			echo "<td><a href=\"$script${queryString}&pageNum=".($pageNum+1)."\"><strong>Next</strong></a></td>";
		}
		echo "</tr></table></div></center>";
	}
}

function showMessagesToAddress()
{
	global $patientID;
	$query="select pm.*, m.*, c1.name as categoryName, c2.name as parentCategoryName " .
		"from patientMessages pm " .
		"inner join messages m on m._id = pm.messageID " .
		"left outer join categories c1 on m.categoryID = c1._id " . 
		"left outer join categories c2 on c1.parentCategoryID=c2._id " .
		"where pm.patientID=$patientID and m.onEnrollForm=0";
	$messages = mysql_query($query);
	while($message=mysql_fetch_assoc($messages))
	{
		$selected = "";
		$checkBoxID="addMessage".$message['_id'];
		if (isset($message['selected']) && $message['selected']=="1")
			$selected = " checked";
		echo "<input name=\"addMessages[]\" type=\"checkbox\" id=\"$checkBoxID\" value=\"{$message['_id']}\"$selected/>";
		echo $message['name'];
		echo "<br>";
	}
}

function showMessageAddCheckboxes()
{
	global $patientID;
	$query = "select m._id, IFNULL(m.shortName,m.name) as name, c.name as categoryName ";
	if ($patientID !="")
	{
		$query.=", (select count(*)>0 from patientMessages pm where pm.messageID=m._id and pm.patientID=$patientID) as selected ";		
	}
	
	$query .="from messages m left outer join categories c on c._id = m.categoryID where onEnrollForm = 1 ";
	$query .="order by c.name asc";
	//echo $query;
	$messages=mysql_query($query) or trigger_error(mysql_error());
	showMessageCheckboxes($messages);	
}


function showMessageCheckboxes($messages)
{
	$oldCategory = "";
	$count = 0;
	echo "<table class=\"table_blue\"><tr><td width=\"250\">";
	echo "<font size=\"3\"><b>Diseases</b><br><br></font>";
	while($message=mysql_fetch_assoc($messages))
	{
		$newCategory = $message['categoryName'] != $oldCategory;
		$selected = "";
		$checkBoxID="addMessage".$message['_id'];
		if (isset($message['selected']) && $message['selected']=="1")
			$selected = " checked";

		if ($newCategory)
		{
			echo "<b>".$message['categoryName'] . "</b><br>";
			$count++;
			$oldCategory = $message['categoryName'];
		}
		
		echo "<input name=\"addMessages[]\" type=\"checkbox\" id=\"$checkBoxID\" value=\"{$message['_id']}\"$selected/>";
		echo $message['name'];
		echo "<br>";
		$count++;
			
		if ($count > 20){
			$count = 0;
		    echo "</td><td width=\"250\">";
		}
	}
	echo "</td></tr></table>";
	//echo "</background></h3><hr>";
}

/*
 * This adds messages based on checkboxes
 */

function processMessageAdds()
{
	global $patientID,$userID;
	//echo " adding messages: ". $_POST['addMessages'];
	if (isset($_POST['addMessages']))
	{
		foreach ($_POST['addMessages'] as $messageID)
		{
			echo "Add Message $messageID<br>";
			$query=	"insert into patientMessages(patientID, messageID, created, due, userID) " .
					"select $patientID as patientID, _id as messageID, CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP(),$userID as userID " .
					"from messages where _id = $messageID and _id not in (select messageID as _id from patientMessages where patientID=$patientID)";   // Prevent duplicate messages
			//echo $query."<br>";
			$result = mysql_query($query) or trigger_error("\n<br>".$query ."\n<br>". mysql_error());
		}
	}
}


function categoryLevel($selected, $categoryID="", $level = 0)
{
	$output = "";
	$query = "select * from categories";
	if ($categoryID != "") $query .= " where parentCategoryID=$categoryID";
	else $query .= " where parentCategoryID is null";  
	$query .= " order by name asc";
 	$categories = mysql_query($query);
 	$spacer=str_repeat("&nbsp;&nbsp;&nbsp;&nbsp;",$level);
	while ($category = mysql_fetch_assoc($categories)) {
		$extraText="";
		if ($category['_id']==$selected) $extraText=" selected";
		$output .= "<option value=\"${category['_id']}\"$extraText>$spacer${category['name']}</option>";
		// Recurse
		$output .= categoryLevel($selected,$category['_id'],$level+1);
	}
	return $output;
}
function categorySelect($selected="", $field="categoryID")
{
	$output = "<select name=\"$field\"><option></option>";
	$output .= categoryLevel($selected);
	$output .= "</select>";
	return $output;
}
// This takes a stattement and returns a text SQL query string

function booleanMessage()
{

}


?>