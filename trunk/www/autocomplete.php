<?
include ('includes/config.php') ;









// returning  name
if (isset($_POST['name'])) {

$name = escape_data($_POST['name']);
$name_parsed = '';

// do IN BOOLEAN MODE by adding '+' to q	
$name_array = explode (' ',$name);
foreach ($name_array AS $value) {

		$name_parsed .= ' +'.$value.'*';

	
}


$query = "SELECT firstName, lastName, _id FROM patients WHERE MATCH (firstName, lastName) AGAINST ('$name_parsed' IN BOOLEAN MODE) ORDER BY fn ASC LIMIT 100";

$result = mysql_query ($query) or trigger_error("Query: $query\n<br />MySQL Error: " . mysql_error());

$print = "<ul>";

//$print .= "<li>$query</li>";


while ($row['t'] = mysql_fetch_array($result, MYSQL_ASSOC)) {
$print .= "<li onMouseOver=this.style.backgroundColor='#a7ffa7' onMouseOut=this.style.backgroundColor='#ffffff' >{$row['t']['fn']} {$row['t']['ln']}</li>";
}

$print .= "</ul>";


echo $print;


}



?>

