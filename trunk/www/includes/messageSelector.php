<?
if (!isset($selectorName) || $selectorName=="")
{
	$selectorName="newMessageID";
}
if (!isset($textInputName) || $textInputName=="")
{
	$textInputName="newMessageName";
}
if (!isset($selectorValue))
{
	$selectorValue="";
}

?>
<input type="hidden" name="<?=$selectorName?>" value="<?=$selectorValue?>">
<input id="<?=$textInputName?>" name="<?=$textInputName?>" type="text" autocomplete="off"  class="input" value="" onkeydown="messageSearch(event, '<?=$textInputName?>', '<?=$selectorName?>')">
<DIV ID="<?=$textInputName?>Hints" STYLE="position:absolute;background-color:white;layer-background-color:white;text-align:left;width:300px;"></DIV>
