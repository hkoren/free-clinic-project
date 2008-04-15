<?

//For each $row in query results:
$rowNum=0;
$groupNum=0;
$sectionNum=0;
$validationScript="";
$tdtag="<td valign=\"top\" nowrap=\"1\">";
$toplink="<div style=\"float:right;\"><a href=\"javascript:scroll(0,0)\">Top</a></div>";
 while ($row = mysql_fetch_assoc($demographics))
 {
 	$newSection = $row["sectionName"]!= $oldSection;
	$newGroup = $row["groupName"]!= $oldGroup;
	$newFamilyHistory = $row["typeName"] == "Family History" && $oldTypeName != $row["typeName"];
	$newUsage = $row["typeName"] == "Usage" && $oldTypeName != $row["typeName"];
	$firstRow = $row==1 ;
 	$rowNum++;
 	
 	if(!$firstRow && $newGroup)
 	{
 	 	echo "</td>";
 	}	
 	if(!$firstRow && $newSection)
 		echo "</tr></table>";
 		
 	if($newSection)
 	{
 		$tabletag = "$toplink<table width=\"100%\" border=\"0\" class=\"table_". ($sectionNum %2 ? "yellow" : "blue")."\">";
 		$sectionNum++;
 		$groupNum=0;
 		echo "$tabletag<tr>\n";
 		echo "$tdtag<h3>${row['sectionName']}</h3>\n";
 	}	
 	
 	
 	if ($newGroup && !$newSection)
 	{	
 		if ($groupNum % 3 == 0)
 			echo "</tr></table>$tabletag<tr>";
 		echo "$tdtag";
 	}
 	if($newGroup)
 	{
	 	$groupNum++;
 		echo "<h4>${row['groupName']}</h4>"; 
 	}

 	
 	switch($row["typeName"])
    {
		case 'String':
			if ($row['required'])
			{
				$validationScript .= "if (require(\"${row['_id']}\",\"Enter a ${row['name']}\"))";
			}
			textInput($row['_id'],$row['name'],$isEnrolled?$row['stringValue']:"","input","25");
			//echo "<br/>";
			break;
		case 'Integer':
			if ($row['required'])
			{
				$validationScript .= "if (require(\"${row['_id']}\",\"Enter a ${row['name']}\"))\n";
			}		
			$validationScript .= "isInteger(\"${row['_id']}\",\"Enter Integer for ${row['name']}\");\n";
			textInput($row['_id'],$row['name'],$isEnrolled?$row['intValue']:"","input","5");
			//echo "${row['name']} <input type=\"text\" name=\"${row['_id']}\" size=\"5\"><br/><br>";
			//echo "<br/>"; 
			break;
		case 'Boolean':
			checkboxInput($row['_id'],$row['name'],$isEnrolled?$row['intValue']:0);
			break;
		case 'Family History':
			if ($newFamilyHistory)
			{
				echo "<div class=\"inline\"><div class=\"desc\">&nbsp;</div>\n" .
						"<div class=\"check\">Mother</div>\n" .
						"<div class=\"check\">Father</div>\n" .
						"<div class=\"check\">Sibling</div></div>\n";
			}
			$mfs = $isEnrolled?(int)$row['intValue']:(int)0;
			$mother  = ($mfs & 1);
			$father  = ($mfs & 2) >> 1;
			$sibling = ($mfs & 4) >> 2;
			
			echo "<div class=\"inline\"><div class=\"desc\">${row['name']}</div>\n " .
					"<div class=\"check\"><input name=\"${row['_id']}_m\" type=\"checkbox\" value=\"1\"".($mother ?" checked=\"1\"":"")."/></div>\n" .
					"<div class=\"check\"><input name=\"${row['_id']}_f\" type=\"checkbox\" value=\"1\"".($father ?" checked=\"1\"":"")."/></div>\n" .
					"<div class=\"check\"><input name=\"${row['_id']}_s\" type=\"checkbox\" value=\"1\"".($sibling?" checked=\"1\"":"")."/></div></div>\n";			
			break;
		case 'Usage':
			$usage = $isEnrolled?$row['intValue']:-1;
			if ($newUsage)
			{
				echo "<div class=\"inline\"><div class=\"desc\">&nbsp;</div>\n" .
						"<div class=\"check\">Past</div>\n" .
						"<div class=\"check\">Present</div>\n" .
						"<div class=\"check\">Never</div></div>\n";
			}
			echo "<div class=\"inline\"><div class=\"desc\">${row['name']}</div>" .
					"<div class=\"check\"><input name=\"${row['_id']}\" type=\"radio\" value=\"1\"".($usage==1?" checked=\"1\"":"")."/></div>\n" .
					"<div class=\"check\"><input name=\"${row['_id']}\" type=\"radio\" value=\"2\"".($usage==2?" checked=\"1\"":"")."/></div>\n" .
					"<div class=\"check\"><input name=\"${row['_id']}\" type=\"radio\" value=\"0\"".($usage==0?" checked=\"1\"":"")."/></div></div>\n";
			break;
		case 'Sexual Preference':
			$preference = $isEnrolled?$row['stringValue']:"";
			echo "<p>${row['name']}<br/>\n" .
					"<input name=\"${row['_id']}\" type=\"radio\" value=\"m\"".($preference=="m"?" checked=\"1\"":"")."/> Men<br/>" .
					"<input name=\"${row['_id']}\" type=\"radio\" value=\"w\"".($preference=="w"?" checked=\"1\"":"")."/> Women<br/>\n" .
					"<input name=\"${row['_id']}\" type=\"radio\" value=\"b\"".($preference=="b"?" checked=\"1\"":"")."/> Unspecified</div></p>\n";
		   	break;			
	}
	$oldSection = $row["sectionName"];
	$oldGroup = $row["groupName"];	
	$oldTypeName = $row["typeName"];
 }      
 echo "</td></tr></table>";
?>