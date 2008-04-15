<?

function redirect( $page = '') {
	global $site_url;
	header ("Location: $page");

	exit();
}


function spacer ($height = 10) {
	echo "<table cellpadding=0 cellspacing=0 ><tr><td height=$height ></td></tr></table>";
}

function post_clean ($field = 0) { // 0 for post, 1 for $post_display, 2 for all
	global $post, $post_display;
	if ($field == 0 ||$field == 2) {
		foreach ($_POST as $key => $value) {

			if (is_array($value) ) {
				foreach ($_POST[$key] as $key2 => $value2) {
					$post[$key][$key2] = escape_data(trim($value2));
				}
			}
			else {
				$post[$key] = escape_data(trim($value));
			}
		}
	}
}

function get_clean () {
	foreach ($_GET as $key => $value) {
		global $get;
		$get[$key] = escape_data(trim($value));
	}
}
//foreach ($_GET as $var => $value) {$$var = escape_data($value);}


// runs select mysql queries
function get_info( $table = 'users', $column_query = '*', $where_query = '', $var = '') {
	global $u;
	global $r;
	$var = ($var == '') ? $table : $var;
	$where_query = ($where_query == '') ? "WHERE userID='$u'" : $where_query;
	$query = "SELECT $column_query FROM $table $where_query";
	//echo $query;
	$result = mysql_query ($query) or trigger_error("Query: $query\n<br />MySQL Error: " . mysql_error());
	$r[$var] = mysql_fetch_array( $result, MYSQL_ASSOC );

	if( mysql_num_rows($result) != 0 ) $changed = TRUE;
	else $changed = FALSE;

	return $changed;
} // end function






// update mysql
function update( $table = 'users', $set_query = '', $where_query = '', $limit = '1') {
	global $u;
	global $r;
	$query = "UPDATE $table SET $set_query $where_query LIMIT $limit";
	$result = mysql_query ($query) or trigger_error("Query: $query\n<br />MySQL Error: " . mysql_error());
	$changed = mysql_affected_rows();

	return $changed;
} // end function




// delete mysql
function delete( $table = 'users', $where_query = '', $limit = '1') {
	global $u;
	$where_query = ($where_query == '') ? "WHERE _id='$u'" : $where_query;
	$query = "DELETE FROM $table $where_query LIMIT $limit ";
	$result = mysql_query ($query) or trigger_error("Query: $query\n<br />MySQL Error: " . mysql_error());
	$changed = mysql_affected_rows();

	return $changed;
} // end function







// insert mysql
function insert( $table = 'users', $col_query = '', $value_query = '') {
	$query = "INSERT INTO $table ($col_query) VALUES ($value_query) ";
	$result = mysql_query ($query) or trigger_error("Query: $query\n<br />MySQL Error: " . mysql_error());
	$changed = mysql_affected_rows();

	return $changed;
} // end function


function count_total ( $table = '', $where_query = '', $var = '*', $limit = '' ) {
	global $u;
	$limit = ($limit == '') ? '' : "LIMIT $limit ";
	$query = "SELECT COUNT($var) FROM $table $where_query $limit";
	$result = @mysql_query ($query);
	$r = mysql_fetch_array ($result, MYSQL_NUM);

	return $r[0];
}



// Create a function for escaping the data.
function escape_data ($data) {

	// Address Magic Quotes.
	if (ini_get('magic_quotes_gpc')) {
		$data = stripslashes($data);
	}

	// Check for mysql_real_escape_string() support.
	if (function_exists('mysql_real_escape_string')) {
		global $dbc; // Need the connection.
		$data = mysql_real_escape_string (htmlentities (strip_tags(trim($data))), $dbc);
	} else {
		$data = mysql_escape_string (htmlentities (strip_tags(trim($data))) );
	}

	// Return the escaped value.
	return $data;

}

function addJavaScript($name) {
	global $extraHead;
	$extraHead .= "<script language=\"JavaScript\" type=\"text/javascript\" src=\"js/$name\"></script>";	
}

function typeSelect($table, $selected)
{	
	$typeTable = $table."Type";
	$IDName = $typeTable."ID";
	$query = "select * from $typeTable order by name asc";
	$userTypes=mysql_query($query);
	$output = "<select name=\"$IDName\"><option></option>";
	while ($type = mysql_fetch_array($userTypes, MYSQL_ASSOC)) {
		$extraText="";
		if ($type['_id']==$selected) $extraText=" selected";
		$output .= "<option value=\"${type['_id']}\"$extraText>${type['name']}</option>";
	}
	$output .= "</select>";
	return $output;
}

function field($name, $default=""){
	global $patient;
	if (isset($_POST[$name]))
	{
		return $_POST[$name];	
	}
	if ($patient != "")
	{
		if (isset($patient[$name]))
		{
			return $patient[$name];
		}
		else
		{
			return $default;
		}
	}	
	else 
	{
		echo "";
		return $default;
	}	
}
function staticfield($name, $default=""){
	global $patient;
	if (isset($_POST[$name]))
	{
		return $_POST[$name];	
	}
	else 
	{
		echo "";
		return $default;
	}	
}

function mysql_int($data)
{
	if ($data =="") return "null"; 
	else return $data;
}

function leading_zeros($value, $places){
// Function written by Marcus L. Griswold (vujsa)
// Can be found at http://www.handyphp.com
// Do not remove this header!

    if(is_numeric($value)){
        $leading = "";
        for($x = 1; $x <= $places; $x++){
            $ceiling = pow(10, $x);
            if($value < $ceiling){
                $zeros = $places - $x;
                for($y = 1; $y <= $zeros; $y++){
                    $leading .= "0";
                }
            $x = $places + 1;
            }
        }
        $output = $leading . $value;
    }
    else{
        $output = $value;
    }
    return $output;
}
function letterlinks($url)
{
	echo"<div class=\"letterlinks\">";
	for ($i=0; $i<26; $i++)	
	{
		$letter = chr(65+$i);
		echo "<a href=\"$url$letter\">$letter</a>&nbsp;";
	}
	echo "</div>";
}

// Paging Functions
// ----------------
// Used for scrolling through multiple pages of results


 function page_top() {		
	global $lastPage, $pageNum, $startItem, $pageRows, $rows;
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
	$startItem = $pageRows * ($pageNum-1);
	$endMsg = $startItem + $pageRows;
 }
 function page_bottom() {
	global $pageNum, $lastPage, $bottomPage, $topPage, $queryString, $script;
	// Show Pagination  ...shamelessly paraphrased from google
	$bottomPage = max(1,$pageNum-10);
	$topPage = min($pageNum+9,$lastPage);
	echo "<div class=\"pagination\"><table align=\"center\"><tr>";
	if ($topPage != 1)
	{
		if ($pageNum >1) 
			echo "<td>$pageNum<a href=\"$script?${queryString}&pageNum=".($pageNum-1)."\"><strong>Previous</strong></a></td>";
		
		for($i = $bottomPage; $i<=$topPage; $i++)
		{
			if ($i != $pageNum)					// Page they can link to 
			{
				echo "<td><a href=\"$script?${queryString}&pageNum=$i\"><div class=\"s\">*</div>$i</a></td>";
			}
			else								// Selected page
			{
				echo "<td><div class=\"sSelected\">^</div>$i</a></td>";	
			}
			
		}
		if ($pageNum != $lastPage) {
			echo "<td><a href=\"".$script."?"."${queryString}&pageNum=".($pageNum+1)."\"><strong>Next</strong></a></td>";
		}
	}
	echo "</tr></table></div></center>";
 	
 }
 
 function page_limit() {
 	global $pageNum, $pageRows;
 	return ' limit ' .($pageNum - 1) * $pageRows .',' .$pageRows;
 }
?>