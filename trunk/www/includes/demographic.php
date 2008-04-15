<?
# User Includes Library

# User Type Selection
function demographicTypeSelect($selected)
{	
	return typeSelect("demographic", $selected);
}

function demographicGroupSelect( $selected)
{	
	$query = "select * from demographicGroup order by name asc";
	$userTypes=mysql_query($query);
	$output = "<select name=\"demographicGroupID\"><option></option>";
	while ($type = mysql_fetch_array($userTypes, MYSQL_ASSOC)) {
		$extraText="";
		if ($type['name']== $selected) $extraText=" selected";
		$output .= "<option value=\"${type['_id']}\"$extraText>${type['name']}</option>";
	}
	$output .= "</select>";
	return $output;
}

function demographicSectionSelect( $selected)
{	
	
	$query = "select * from demographicSection order by name asc";
	$userTypes=mysql_query($query);
	$output = "<select name=\"demographicSectionID\"><option></option>";
	while ($type = mysql_fetch_array($userTypes, MYSQL_ASSOC)) {
		$extraText="";
		if ($type['name']==$selected) $extraText=" selected";
		$output .= "<option value=\"${type['_id']}\"$extraText>${type['name']}</option>";
	}
	$output .= "</select>";
	return $output;
}
?>