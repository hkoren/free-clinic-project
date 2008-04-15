<?php
include ("includes/config.php");
$page = "Diagnoses";
if (!isset($_GET["type"])) $type = "short";
else $type = $_GET["type"];
$isShort = strcmp($type,"long")!=0;
$noTop = true;
$where="";
//include ("includes/header.php"); 

if (strcmp($type,"short")==0) $where = "WHERE top=1"; 
echo "<html><head><link href=\"css/style.css\" rel=\"stylesheet\" type=\"text/css\" /><title>Diagnosis</title></head><body bgcolor=\"white\">";
?>
<script language="javascript">
function addDiagnosis(code,description,messageID)
{
	if (window.opener)
	{
		window.opener.addDiagnosis(code,description,messageID);
	}
	window.opener.focus();
	window.close();
}
</script>
<?
	$query="select m.*, c1.name as categoryName, ifnull(c2.name,c1.name) as parentCategoryName " .
			"from messages m " .
			"left outer join categories c1 on m.categoryID = c1._id " . 
			"left outer join categories c2 on c1.parentCategoryID=c2._id $where " .
			"order by parentCategoryName asc, categoryName asc";
			
			//echo $query;
//$query = "select * from message where order by section asc,subSection asc,code asc";
$result = mysql_query($query) or trigger_error("Query: $query\n<br />MySQL Error: " . mysql_error());;

echo (!$isShort?"<a href=\"diagnoses.php?type=short\">":""). "Short List" . (!$isShort?"</a>":""). " - ";
echo ($isShort?"<a href=\"diagnoses.php?type=long\">":""). "Long List" . ($isShort?"</a>":"");



$oldSection = "";
$oldSubSection = "";
echo "<table border=\"1\" \"cellpadding=\"3\" cellspacing=\"0\" style=\"background-color:#fff;\"><tr>";
$sectionCount=0;
while ($row = mysql_fetch_assoc($result)) {
	
	$section = $row['parentCategoryName'];
	$subSection = $row['categoryName'];
	$top = $row['top'];
	$code = $row['icd9'];
	$description = $row['name'];
	$messageID = $row['_id'];
	
	$isNewSection =  strcmp($section,$oldSection) != 0;
	$isNewSubSection = strcmp($subSection,$oldSubSection) != 0 && strcmp($subSection, "–")!=0;
	$isNewRow = ($sectionCount % 3 == 0) && $isNewSection;
	if ($isNewSection && strcmp($oldSection,"")!=0 ) echo "</td>";
	if ($isNewRow ) echo "</tr><tr>\n"; 		
	if ($isNewSection) 
	{
		echo "<td valign=\"top\">";
		if (strcmp($section,"")!=0) echo "<h4>$section</h4>";
		$sectionCount++;
	}
	
	if ($isNewSubSection && strcmp($subSection,"")!=0) 
	{
		echo "<strong>$subSection</strong><br>";
	}
	
	echo "<a href=\"javascript:addDiagnosis('$code', '". addslashes($description)."',$messageID)\">$code - $description</a><br>";
	
	$oldSection = $section;
	$oldSubSection = $subSection;
	
}
mysql_free_result($result);

echo "</body></html>";
?>
 