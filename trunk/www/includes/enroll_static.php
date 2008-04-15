<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_yellow">
  <tr>
    <td width="24%" valign="top"><strong>Basic Info</strong> </td>
    <td width="76%">&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="top">
	<table width="100%" border="0" cellspacing="3" cellpadding="0">
        <tr>
          <td nowrap="1">
<?

// Splice the name out of the URL passed from Check In Link
if (isset($_GET['name'])) {
	$namelist = split(" ", $_GET['name'],3);
	if (count($namelist) ==1)
	{
		$first=$namelist[0];
	}
	else if(count($namelist)==2)
	{
		$first=$namelist[0];
		$last=$namelist[1];
	}
	if(count($namelist)>2)
	{
		$first=$namelist[0];
		$middle=$namelist[1];
		$lastarray=array_splice($namelist,2);
		$last = "";
		foreach($lastarray as $word)
		{
			$last.=" $word";
		}
		if (count($last)!=0){
			$last = substr($last,1);			
		}
	}
}

textInput("firstName","First Name",(isset($first)?$first:field("firstName")),"compact","10","","");
textInput("middleName","Middle",(isset($middle)?$middle:field("middleName")),"compact","7","","");
textInput("lastName","Last",(isset($last)?$last:field("lastName")),"compact","10","",""); 
?></td>
          <td align="center" nowrap="1">
	<SCRIPT LANGUAGE="JavaScript">
	var dateOfBirth = new CalendarPopup("testdiv1");
dateOfBirth.setReturnFunction("setMultipleValues");
function setMultipleValues(y,m,d) {
	$('bd_y').value=y;
	$('bd_m').value=m;
	$('bd_d').value=d;
     }
	</SCRIPT>
          <table><tr><Td>DOB</td><td>
          <? 
          if (isset($_GET['dateOfBirth'])){
          	list($bd_y,$bd_m,$bd_d) = split("-", $_GET['dateOfBirth'],3);
          }
          elseif (isset($patient))
          {
          	list($bd_y,$bd_m,$bd_d) = split("-", $patient['dateOfBirth'],3);
          }
          dobmInput("bd_m","",staticfield("bd_m",isset($bd_m)?$bd_m:""),"compact","3","",""); 
          echo "</td><td>&nbsp;/&nbsp;</td><td>";
          dobdInput("bd_d","",staticfield("bd_d",isset($bd_d)?$bd_d:""),"compact","3","",""); 
          echo "</td><td>&nbsp;/&nbsp;</td><td>";
          dobyInput("bd_y","",staticfield("bd_y",isset($bd_y)?$bd_y:""),"compact","5","",""); ?></td></tr></table><br>
          ( mm / dd / yyyy )<!--&nbsp<A HREF="#" onClick="dateOfBirth.showCalendar('anchor9'); return false;" TITLE="dateOfBirth.showCalendar('anchor9'); return false;" NAME="anchor9" ID="anchor9">select</A> -->
<!--<A HREF="#" onClick="dateOfBirth.select(document.forms[1].bd_y,'anchor1x','MM/dd/yyyy'); return false;" TITLE="cal1x.select(document.forms[1].bd_y,'anchor1x','MM/dd/yyyy'); return false;" NAME="anchor1x" ID="anchor1x">select</A>-->
   <DIV ID="testdiv1" STYLE="position:absolute;visibility:hidden;background-color:white;"></DIV>
             </td>
          <td align="right"><? ssnInput("SSN","SSN",field('SSN'),"compact","14") ?></td>
        </tr>

      </table></td>
    </tr>
    <tr><td>&nbsp;</td></tr>
  <tr>
    <td colspan="1" valign="top" width="50%">
    <strong>Contact Info</strong>
    <table width="100%" border="0" cellspacing="10" cellpadding="0">
      <tr><td>Address1</td>
		<td><input name="address1" type="text" id="address1" maxlength="255" size="35"  value="<?=field("address1")?>"/></td></tr>
        <tr><td>Address2</td>
        <td><input name="address2" type="text" id="address2" maxlength="255" size="35"  value="<?=field("address2")?>"/></td></tr>
        <tr>
        <td colspan="2" nowrap="1">
        <? textInput("city","City",field("city"),"compact","15","","") ?>
        <? textInput("state","State",field("state"),"compact","2","","") ?>
		<? textInput("zip","Zip",field("zip"),"compact","10","","") ?><br/><br/>
		<? phoneInput("phone","Phone Number",field("phone"),"compact") ?></td></tr>
	  </tr>
    </table></td>
    <td width="50%" valign="top">
    <strong>Emergency Contact</strong>
    <table width="100%" border="0" cellspacing="10" cellpadding="0">
      <tr>
        <td align="right"><? textInput("emergencyName","Name",field("emergencyName"),"15","","") ?></td></tr>
        <tR><td align="right"><? phoneInput("emergencyPhoneNumber","Phone",field("emergencyPhoneNumber")) ?></td></tr>
        <tr><td align="right"><? textInput("emergencyRelationship","Relationship",field("emergencyRelationship"),"15","","") ?></td></tr>
      </tr>
    </table></td>
    </tr>
  <tr>
    <td colspan="2" valign="top"></td>
    </tr>
	</table>
	
	
	
<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_blue">
<td><div style="float:right"><a href="javascript:scroll(0,0)">Top</a></div></td>
  <tr>
    <td valign="top"><strong>Demographics</strong></td>
    <td>&nbsp;</td>
  </tr>
  <tr>
    <td colspan="2" valign="top"><table width="100%" border="0" cellspacing="10" cellpadding="0">
          <tr>
            <td>Gender:
<?
//$row = mysql_fetch_assoc($demographics);
$gender = field("gender");
//echo $gender;
echo "\n" .
					"<input name=\"gender\" type=\"radio\" value=\"M\"".($gender=="M"?" checked=\"1\"":"")."/> Male\n" .
					"<input name=\"gender\" type=\"radio\" value=\"F\"".($gender=="F"?" checked=\"1\"":"")."/> Female\n" .
					"<input name=\"gender\" type=\"radio\" value=\"U\"".($gender=="U"?" checked=\"1\"":"")."/> Unspecified</div></p>\n";
?>		
 																				
</td>
            <td align="center">Ethnicity
              <select name="ethnicity" id="ethnicity" >
                <? foreach ($ethnicity_array AS $key => $value) {
		echo "<option value=$key>$value</value>";
	}
	?>
              </select></td>
            <td align="right">Primary Language
              <select name="primary_lang" id="primary_lang">
                <? foreach ($primary_lang_array AS $key => $value) {
		echo "<option value=$key>$value</value>";
	}
	?>
              </select></td>
          </tr>
        </table>
        <table width="100%" border="0" cellspacing="10" cellpadding="0">
          <tr>
            <td>Level of Education
              <select name="education" id="education">
                <? foreach ($education_array AS $key => $value) {
		echo "<option value=$key>$value</value>";
	}
	?>
              </select></td>
            <td align="center">Homeless
              <input name="homeless" type="checkbox" id="homeless" value="1" />
Check for Yes </td>
            <td align="right">Marital Status
              <select name="marital_status" id="marital_status">
                <? foreach ($marital_status_array AS $key => $value) {
		echo "<option value=$key>$value</value>";
	}
	?>
              </select></td>
          </tr>
        </table>
        </p></td>
    </tr>
  <tr>
    <td colspan="2" valign="top"><table width="100%" border="0" cellspacing="10" cellpadding="0">
      <tr>
        <td>Monthly Income
          <input name="income" type="text" id="income" size="15" /></td>
        <td align="center">Number of Kids 
          <input name="kidsNumber" type="text" id="kidsNumber" size="5" maxlength="3" value="<?=field("kidsNumber")?>"/></td>
        <td align="right">Kids at Home
          <input name="kidsHome" type="text" id="kidsHome" size="5" maxlength="3"  value="<?=field("kidsHome")?>"/></td>
      </tr>
    </table></td>
    </tr>
  <tr>
    <td colspan="2" valign="top"><table width="100%" border="0" cellspacing="10" cellpadding="0">
      <tr>
        <td>Adults at Home
          <input name="adultsHome" type="text" id="adultsHome" size="5" maxlength="3" value="<?=field("adultsHome")?>"/></td>
        <td align="center">Documentation
          <input name="documentation" type="checkbox" id="documentation" value="1" />
Check for Yes</td>
        <td align="right">Assets
          <input name="assets" type="checkbox" id="assets" value="1" />
Check for Yes</td>
      </tr>
    </table>
		</a><tr><td>&nbsp;</td><td>&nbsp;</td><td></td></tr>
		</td>
    </tr>
</table>