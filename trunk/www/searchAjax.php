<?php
include ("includes/config.php"); 
/*
 * Created on Jun 9, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
 
$search=$_GET['search'];
$type = $_GET['type'];

if (strlen($search) >= 2)
{
	if ($type == "name")
	{
		// $query = "SELECT DISTINCT firstName, middleName, lastName FROM patients WHERE MATCH (firstName, lastName) AGAINST ('$search' IN BOOLEAN MODE) ORDER BY firstName ASC LIMIT 100";
		$query = "SELECT DISTINCT firstName, middleName, lastName FROM patients WHERE CONCAT(firstName, ' ', lastName) like  '%$search%'  ORDER BY firstName ASC LIMIT 100";
		//echo $query;
		$result = mysql_query ($query) or trigger_error("Query: $query\n<br />MySQL Error: " . mysql_error());
		$output="";
		//if (mysql_num_rows($result)==0) echo "none";
		while ($name=mysql_fetch_assoc($result))
		{
			$output .= $name['firstName'] . " " .$name['middleName'] . " " .$name['lastName']. ",";
		}
		$output=substr($output,0,strlen($output)-1);
		echo $output;
	}
	else if ($type == "notes")
	{
		$results = array();

		// $query = "SELECT DISTINCT firstName, middleName, lastName FROM patients WHERE MATCH (firstName, lastName) AGAINST ('$search' IN BOOLEAN MODE) ORDER BY firstName ASC LIMIT 100";
		$query = "SELECT DISTINCT notes FROM checkIns WHERE notes like '%$search%'  ORDER BY notes ASC LIMIT 100";
		$result = mysql_query ($query) or trigger_error("Query: $query\n<br />MySQL Error: " . mysql_error());
		while ($name=mysql_fetch_assoc($result))
		{
			array_push($results, $name['notes']);
		}
		
		$query = "SELECT _id, name from messages where name like '%$search%'";
		$result = mysql_query($query)  or trigger_error("Query: $query\n<br />MySQL Error: " . mysql_error());
		while ($message=mysql_fetch_assoc($result))
		{
			array_push($results, $message['_id']."\$\$\$".$message['name']);
		}

		sort($results);
		$output="";
		foreach($results as $result)		
		{
			$output .= "" . $result. "\$%#";	
		}
		$output=substr($output,0,strlen($output)-3);
		echo $output;
	}
	if ($type == "message")
	{
		$query = "SELECT name,_id from messages where name like '%$search%'";
		$result = mysql_query($query)  or trigger_error("Query: $query\n<br />MySQL Error: " . mysql_error());
		$output = "";
		while ($message=mysql_fetch_assoc($result))
		{
			$output .= $message['_id']."\$\$\$".$message['name']. "\$%#";
		}
		$output=substr($output,0,strlen($output)-3);
		echo $output;
	}
}
?>
