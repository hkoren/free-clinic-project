


function nameSearch(event) {
	if (event.keyCode==Event.KEY_RETURN) {
		checkInAddNew( document.checkIn.nameHints, document.checkIn.notesHints );
		$('nameHints').value = '';
		
	}
	else {
		new Ajax.Request( 'searchAjax.php?type=name&search='+$F('checkInName')+String.fromCharCode(event.keyCode), 
		{ 
			onComplete: function(t) 
			{ 
				if (t.responseText != '')
				{
				
					$('nameHints').style.visiblity = 'visible';
					$('nameHints').innerHTML = parseNameList(t.responseText); 
				}				
			}
		});
	}
}
function parseNameList(nameList)
{
	var output = "";
	var names = nameList.split(",");
	for (var i=0;i<names.length;i++)
	{
		output +=  "<a href=\"javascript:nameHintSelect(\'" + names[i] + "\')\">" + names[i] + "</a><br> "
	}
	return output;
}
function parseNotesList(noteList)
{
	var output = "";
	var notes = noteList.split("$%#");
	for (var i=0;i<notes.length;i++)
	{
		output +=  "<a href=\"javascript:notesHintSelect(\'" + notes[i] + "\')\">" + notes[i] + "</a><br> "
	}
	return output;
}
function nameHintSelect(name)
{
	document.checkIn.checkInName.value = name;
	$('nameHints').innerHTML = "";
}

function notesHintSelect(name)
{
	document.checkIn.checkInNotes.value = name;
	$('notesHints').innerHTML = "";
}

function checkInAddNew(name, notes) {
	if (name.length >= 2) { // min name length is 2
	/*
	if ( $('checkInNoneFound') )Element.hide('checkInNoneFound');
	Element.show('checkInTable');
	$('checkInIndicator').innerHTML = 'Loading ...';
	new Ajax.Request( 'action.php?addCheckIn='+name+'&notes='+notes, 
	{ 
		onComplete: function(request) 
		{ 
			$('checkInIndicator').innerHTML = ''; 
			eval(request.responseText);
		}
	});
	*/
	document.checkIn.submit();
	
	}
	else {
		alert('Name Is Too Short');
	}

}

function checkInPatient (name, ID, patientID) {	
	if (!isNaN(patientID))
	{
		location.href = 'clinicVisit.php?patientID='+patientID+'&checkInID='+ID;
	}
	else
	{
		new Ajax.Request('action.php?checkInDone='+ID, { onComplete: function() {
			location.href = ('search.php?name='+name);
		}   } );
	}
}
	