<?php
/*
 * Created on Nov 27, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */

function errorWrapper($type, $field, $label, $input, $style)
{
	$hasLabel = $label!="";
	echo "<span ID=\"$field\" class=\"$style\">";
	echo "<div ID=\"${field}error\" style=\"display:none\"></div>";
	if ($hasLabel==1) 
	{	
		echo "<p class=\"inline\"><div class=\"desc\">$label</div>";
	}
	echo "<span class=\"text\">" .
			"$input" .
		 "</span>\n";
	if ($hasLabel==1) 
	{	
		echo "</p>";
	}
	echo "</span>";
}

function genInput($type, $field, $label, $value, $style="input", $size=35, $max=255, $keyPress="",$checked=0, $keyDown=""){
	errorWrapper($type, $field, $label, "<input type=\"$type\" ID=\"${field}input\" name=\"$field\" " .
			"value=\"$value\" size=\"$size\" maxLength=\"$max\" " .
			"onKeyPress=\"$keyPress\" onKeyDown=\"$keyDown\" " .
			($checked ? "checked=\"1\"" : "").">", $style);
}
function radioInput($field, $label, $value, $style="input", $size="35"){
	genInput($type="radio", $field, $label, $value, $style, $size,$max=0, $keyPress="");
}
function checkboxInput($field, $label, $value, $style="smallinput"){
	genInput("checkbox", $field, $label, $value, $style, "1", "1", "", $value);
}
function textInput($field, $label, $value, $style="input", $size=35, $max=25, $keyPress="",$keyDown=""){
	genInput($type="text", $field, $label, $value, $style, $size, $max, $keyPress,null,$keyDown);
}
function phoneInput($field, $label, $value, $style="input"){
	genInput($type="text", $field, $label, $value, $style, 11, 14, "fmtphone(this)");
}
function ssnInput($field, $label, $value, $style="input", $size=10){
	genInput($type="text", $field, $label, $value, $style, $size=10, $max=11, $keyPress="fmtssn(this)");
}
function dobdInput($field, $label, $value, $style="input", $size=10){
	genInput($type="text", $field, $label, $value, $style, $size=2, $max=2, $keyPress="fmtdobd(this)");
}
function dobmInput($field, $label, $value, $style="input", $size=10){
	genInput($type="text", $field, $label, $value, $style, $size=2, $max=2, $keyPress="fmtdobm(this)");
}
function dobyInput($field, $label, $value, $style="input", $size=10){
	genInput($type="text", $field, $label, $value, $style, $size=2, $max=4, $keyPress="fmtdoby(this)");
}
function selectBox($field, $label, $names, $values, $selectedIndex, $style="input"){
	$select="<select name=\"$field\">";
	for ($i = 0; $i < count($values); $i++)
	{
		$select .= "<option value=\"" . $values[$i] . "\"". ($i == $selectedIndex ? " selected" : "").">" . $names[$i] . "</option>";
	}
	$select .= "</select>";
	errorWrapper("select", $field, $label, $select, $style);
}
/*

<!-- How to use -->
<!-- Examples -->
<!-- 
c/p this into the head of
<form method="post" name="form1" action="" id="form1" onsubmit="return ValidateForm(this);">
		<? radioInput("radio","Sometimes","S","") ?>
		<? radioInput("radio","Always","A","") ?>
		<? radioInput("radio","Never","N","") ?><br>
		<? checkboxInput("checkbox","Mother","M","") ?>
		<? checkboxInput("checkbox","Father","M","") ?>
		<? checkboxInput("checkbox","Siblings","M","") ?><br>
		<? textInput("fn","First Name","","10","","") ?><br>	
		<? textInput("mn","Middle Name","","10","","") ?><br>
		<? textInput("Ln","Last Name","","10","","") ?><br>
		<? textInput("email","Email","","10","","") ?><br>
		<? phoneInput("Phone","Phone Number","") ?><br>
		<? ssnInput("SSN","Social Security Number","","14") ?><br>
		<input type="submit" value="Submit">
	</form>
	*/
	
?>