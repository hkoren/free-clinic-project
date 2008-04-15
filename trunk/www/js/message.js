function messageReaction()
{
	
}

function selectMessage(outputName, messageCode, messageName, fieldName, fieldNameHints)
{
	eval("document.form1."+outputName).value = messageCode;
	eval("document.form1."+fieldName).value = messageName;
	eval("$(\'" + fieldNameHints + "\')").innerHTML ='';
	return "";
}

var counter = new Array();
var list = new Array();

function messageSearch(fieldName, fieldID, event) {
	
	var messageSearchBox = $(fieldName+'_SearchBox');
	var targetTag;
	
	if (event.keyCode==Event.KEY_RETURN) {
		var notesHints;
		if(counter[fieldName] == -1)
		{
			notesHints = document.checkIn.notesHints;
		}
		else{
			var full = list[fieldName][counter[fieldName]];
			var message = full.split("$$$");
			notesHints = message[1];
			$(fieldName).value = notesHints;
		}
		checkInAddNew( document.checkIn.nameHints, notesHints );
		
		$(fieldName).value = '';
	}
	else if(event.keyCode == Event.KEY_DOWN)
	{
		//while(event.keyCode == Event.KEY_DOWN){
		//$("tag" + fieldName + "1").innerHTML = "error";
		unselectRow(fieldName);
		counter[fieldName]++;
		selectRow(fieldName);
		//}
	}
	else if(event.keyCode == Event.KEY_UP)
	{
		//$("tag" + fieldName + "5").className = "error";
		unselectRow(fieldName);
		counter[fieldName]--;
		selectRow(fieldName);	
	}
	else {
		counter[fieldName] = -1;
		var field = $F(fieldName);
		var string = 'searchAjax.php?type=message&search='+field+String.fromCharCode(event.keyCode);
		new Ajax.Request( string, 
		{ 
			onComplete: function(t) 
			{ 
				if (t.responseText != '')
				{
				
					var messageSearchBox = $(fieldName+'_SearchBox');
					var messageSearchField = $(fieldID);
					
					
					// Position search box below text input field
					messageSearchBox.style.top = messageSearchField.style.top + messageSearchField.style.height;
					messageSearchBox.style.left = messageSearchField.style.left;
					show(fieldID);
					messageSearchBox.innerHTML = parseMessageList(t.responseText, fieldName); 
					 
				}				
			}
		});
	}
}

function unselectRow(fieldName)
{	
	var name = "tag" + fieldName + counter[fieldName];
	try {
		var row = $(name);
		row.className = "";
	}
	catch(ex) {}
}

function selectRow(fieldName)
{
	var name = "tag" + fieldName + counter[fieldName];
	try {
		var row = $(name);
		row.className = "error";
	}
	catch(ex) {}
}

function parseMessageList(messageList, fieldName)
{
	var output = "";
	var messages = messageList.split("$%#");
	var i = 0;
	for (i=0;i<messages.length;i++)
	{
		var note = messages[i];
		var name = note;
		var message = note.split("$$$");
		var funct;
		if (message[1] == "")
		{ 
			funct =  "notesHintSelect(\'" + note + "\')";
		}
		else
		{
			name = message[1];
			funct = "messageHintSelect(\'" + fieldName + "\'," + message[0] + ", \'" + message[1] + "\');"
		}
		output +=  "<div id= \"tag" + fieldName + i + "\"><a href=\"javascript:" + funct +"\">" + name + "</a><br></div>";
	}
	list[fieldName] = messages;
	return output;
}

function messageHintSelect(fieldName, messageID, name)
{
	$(fieldName).value = name;
	$(fieldName + "MessageID").value = messageID;
	$(fieldName + "_SearchBox").innerHTML = "";
}

function addDate()
{
	setTrigger("date");
}

function addConditional()
{
	setTrigger(conditionSelection());
}

function setTrigger(value)
{
	document.getElementById("trigger").innerHTML = value;
}

var conditionTypes=new Array("measurement","demographic","message existence","message status");

function conditionSelection(type)
{
		var output = "";
		for (var i=0;i<conditionTypes.length;i++)
		{
			var condition = conditionTypes[i];
			output += "<label for=\"" + condition + "\">";
			output += "<input type=\"radio\" name=\"conditionType\" value=\"" + condition + "\"  id=\"" + condition + "\" " + (condition==type?" checked":"")+"> " + condition + "</label><br>";
		}
		output += "<input type=\"button\" value=\"Next\" onclick=\"declareCondition()\"";
		return output;
}

function declareCondition()
{
	var conditionType = "";
	for (var i=0; i < conditionTypes.length; i++)
	{
		if (document.getElementById(conditionTypes[i]).checked)
	    {
	    	conditionType = conditionTypes[i];
	    }
	}
	var output = "<h3>Adding \"" + conditionType + "\" condition...</h3>";
	if (conditionType=="measurement")
		output += measurementSelect() + booleanSelect() + valueBox();
	else if (conditionType=="demographic")
		output += demographicSelect() +  booleanSelect() + valueBox();
	else if (conditionType=="message existence")
		output += messageSelect() + " [EXISTS /DOESN'T EXIST] ";
	else if (conditionType=="message status")
		output += messageSelect() + statusSelect();

	output += " <input type=\"button\" value=\"add\" onclick=\"addCondition('" + conditionType + "')\"><br>";
	output += "<input type=\"button\" value=\"back\" onclick=\"setTrigger(conditionSelection('"+conditionType+"'))\">";
	setTrigger(output);
}
/*function messageSearch(event,fieldName,outputName) {
	var fieldNameHints=fieldName + "Hints";
	if (event.keyCode==Event.KEY_RETURN) {
		checkInAddNew( document.checkIn.nameHints, document.checkIn.notesHints );
		$(fieldName).value = '';
		
	}
	else {
		new Ajax.Request( 'searchAjax.php?type=message&search='+$F(fieldName)+String.fromCharCode(event.keyCode), 
		{ 
			onComplete: function(t) 
			{ 
				if (t.responseText != '')
				{
				
					$(fieldNameHints).style.visiblity = 'visible';
					var output = parseMessageList(t.responseText, fieldName,outputName,fieldNameHints); 
					$(fieldNameHints).innerHTML = output;
				}
			}
		});
	}
}*/


function valueBox()
{
	return " <input type=\"text\" name=\"value\"> ";
}
function measurementSelect() 
{
	return " [MEASURMENT SELECT] ";
}
function demographicSelect()
{
	return " [DEMOGRAPHIC SELECT] ";
}
function messageSelect()
{
	return " [MESSAGE SELECT] ";
}
function statusSelect()
{
	return " [STATUS SELECT] ";
}
var booleanStatements=new Array("==","!=", ">", ">=", "<", "<=");
function booleanSelect()
{
	var output = " <select name=\"boolean\">";
	for (var i=0; i<booleanStatements.length; i++)
	{
		var opperand = booleanStatements[i];
		output += "<option value=\""+opperand+"\">"+opperand+"</option>";
	}
	output += "</select> ";
	return output;
}