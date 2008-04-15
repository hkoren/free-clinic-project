function addNewProblem()
{
	hide("addNewPrompt");
	show("addNewForm");
}

var showHideState = 'hidden';

function showHide(layer_ref)
{
	if (showHideState == '') {
		showHideState = 'none';
	}
	else {
		showHideState = '';
	}
	setDisplay(layer_ref,showHideState);
}


// ************ Diagnosises ************
function seeAllDiagnoses()
{
	var diagnoses = window.open('diagnoses.php', 'diagnoses', 'toolbar=0,scrollbars=1,location=0,statusbar=0,menubar=0,resizable=1,width=640,height=420,left = 320,top = 302');
}
var diagnoses = new Array();

function showDiagnoses()
{
	var output='<table width="100%">';
	for (var i=0; i<diagnoses.length; i++)
	{
		output += "<tr><td>" + diagnoses[i][0] + "</td><td>" + diagnoses[i][1] + "</td><td><a href=\"javascript:removeDiagnosis("+i+")\">remove</a></td><input type=\"hidden\" name=\"addMessages[]\" value=\""+diagnoses[i][2]+"\"></tr>\n";
	}
	output += '</table>';
	return output;
}

function updateDiagnoses()
{
	document.getElementById("diagnoses").innerHTML = showDiagnoses();
}
function addDiagnosis(code, description, messageID)
{
	var diagnosis = new Array(code,description,messageID);
	diagnoses[diagnoses.length]=diagnosis;

	updateDiagnoses();
}
function removeDiagnosis(index)
{
	diagnoses.splice(index,1);
	updateDiagnoses();
}

function testsYes()
{
	hide("testsPrompt");
	show("testsDisplay");
}

function testsNo()
{

}
function testsOtherYes()
{
	hide("testsOthersPrompt");
	show("testsOthersDisplay");
}

function testsOtherNo()
{

}
