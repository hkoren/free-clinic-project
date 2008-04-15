<?php
include ("includes/config.php"); 
$page="reports";
include ("includes/header.php"); 
/*
 * Created on May 24, 2007
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
?>
<div>
<table  width=100% border=0 cellspacing=1 cellpadding=2 >  
<thead bgcolor="#ddd">
<tr>
    <td>Report Name</td>
    <td>Description</td>
  </tr>

</thead>

<tbody>
<tr>
    <td><a href="#">Test Nick Name</a> </td>
    <td><a href="#">  Females Over 45 Without Test A </a></td>
  </tr>
  <tr>
    <td><a href="#">Test Nick Name 2 </a></td>
    <td><a href="#">Males Between 21 and 40 With HL </a></td>
  </tr>
  </tbody>
</table>

</div>






<div class=page_spacer>&nbsp;</div>


<div>
<strong>Create New Report</strong>
<table width="100%" border="0" cellspacing="0" cellpadding="4">
  <tr>
    <td>Gender:      
      
      <input name="radiobutton" type="radio" value="radiobutton">
      Male 
      <input name="radiobutton" type="radio" value="radiobutton">
       
      Female 
      <input name="radiobutton" type="radio" value="radiobutton">
      Both</td>
  </tr>
  <tr>
    <td>Age: 
      <input name="textfield" type="text" size="2"> 
      (min) 
      <input name="textfield2" type="text" size="2">
(max)</td>
  </tr>
  <tr>
    <td>Ethnicity: 
      <select name="ethnicity" id="ethnicity">
        <? foreach ($ethnicity_array AS $key => $value) {
		echo "<option value=$key>$value</value>";
	}
	?>
      </select></td>
  </tr>
  <tr>
    <td>High Cholesterol (HL):
      <input type="checkbox" name="checkbox" value="checkbox"></td>
  </tr>
  <tr>
    <td>High Blood Pressure-HT:
      <input type="checkbox" name="checkbox2" value="checkbox"></td>
  </tr>
  <tr>
    <td>Heart Attack:
      <input type="checkbox" name="checkbox3" value="checkbox"></td>
  </tr>
  <tr>
    <td>.<br>
      .<br>
      .</td>
  </tr>
  <tr>
    <td>Coronary Artery Disease: 
      <input type="checkbox" name="checkbox4" value="checkbox"></td>
  </tr>
  <tr>
    <td><input type="submit" name="Submit" value="Submit"></td>
  </tr>
</table>
</div>
<? 
include ("includes/footer.php"); 
?>
