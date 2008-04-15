var valid;
var errorMessage;
//Removing non-integers from date of birth (month)
function fmtdobm(field)
{
	if(isNaN(field.value) || (field.value > 12) || (field.value == 0))
		field.value = "";
}
//Removing non-integers from date of birth (day)
function fmtdobd(field)
{
	/*re = /\D/g; //removes any characters that are not numbers
	dobdnum=field.value.replace(re,"");
	field.value=dobmnum*/
	if(isNaN(field.value) || (field.value >= 32) || (field.value == 0))
		field.value = "";
}
//Removing non-integers from date of birth year
function fmtdoby(field)
{
	re = /\D/g; //removes any characters that are not numbers
	dobynum=field.value.replace(re,"")
	field.value=dobynum
}
//SSN auto format
function fmtssn(field)
{
	re = /\D/g; // remove any characters that are not numbers
	socnum=field.value.replace(re,"")
	sslen=socnum.length
	if(sslen>3&&sslen<6)
	{
		ssa=socnum.slice(0,3)
		ssb=socnum.slice(3,5)
		field.value=ssa+"-"+ssb
	}
	else
	{
		if(sslen>5)
		{
			ssa=socnum.slice(0,3)
			ssb=socnum.slice(3,5)
			ssc=socnum.slice(5,9)
			field.value=ssa+"-"+ssb+"-"+ssc
		}
	else{field.value=socnum}
	}
}

//Phone Number auto format
function fmtphone(field)
{
	re = /\D/g; // remove any characters that are not numbers
	phonenum=field.value.replace(re,"")
	phonelen=phonenum.length
	if(phonelen>3&&phonelen<7)
	{
		phonea=phonenum.slice(0,3)
		phoneb=phonenum.slice(3,6)
		field.value="("+phonea+") "+phoneb
	}
	else
	{
		if(phonelen>6)
		{
			phonea=phonenum.slice(0,3)
			phoneb=phonenum.slice(3,6)
			phonec=phonenum.slice(6,10)
			field.value="("+phonea+") "+phoneb+"-"+phonec
		}
	else{field.value=phonenum}
	}
}

//Number validator
function isPhone(field,errormessage)
{
	return ValidateChars(field,errormessage, "0123456789()- ");
}

function isSSN(field,errormessage)
{
	return ValidateChars(field,errormessage, "0123456789-");
}

function isInteger(field,errormessage)
{
	return ValidateChars(field,errormessage, "0123456789");
}	

function isFloat(field,errormessage)
{
	return ValidateChars(field,errormessage, "0123456789.,");
}

//Char validator
function ValidateChars(field,errormessage,ValidChars)
{
	var Char;
	var value = $(field + "input").value;
	
	if (value == "")
	{
		HideError(field);
		return true;
	}
	for (i = 0; i < value.length; i++) 
    { 
		Char = value.charAt(i); 
		if (ValidChars.indexOf(Char) == -1) 
        {
			SetError(field, errormessage);
			return false;
        }
    }
	HideError(field);
	return true;
}
//Name validator
function require(field,errormessage)
{
	var t = $(field + "input")
	if (t.value == "")
	{
		SetError(field, errormessage);
		return false;
	}
	else
	{
		HideError(field);
		return true;
	}
}

function require_select(field,errormessage)
{
	var t = $(field + "input")
	if (t.selectedIndex == 0)
	{
		SetError(field, errormessage);
		return false;
	}
	else
	{
		HideError(field);
		return true;
	}
}
//Email validator
function isEmail(field,errormessage)
{
	var t = $(field + "input");
	if(t.value!="")
	{
		apos=t.value.indexOf("@");
		dotpos=t.value.lastIndexOf(".");
		
		if (apos<1||dotpos-apos<2) 
		{
			SetError(field,errormessage);
			return false;
		}
		else 
		{
			HideError(field);
			return true;
		}
	}
}

//Replacement of the $ sign function
function $(field){
	return document.getElementById(field);
}

//This puts error in a div
function SetError(field, errormessage)
{
	var span = $(field); 
	var input = $(field + "input");
	var error = $(field + "error");
	// Add to global error message
	errorMessage += errormessage + "<br>"; 
	// put error message in hidden div
	error.innerHTML = errormessage;

	// show hidden div
	show(field + "error");
		
	// change css style of input span
	span.className = "error";
	valid = false;
//	alert(field + " invalid");
}
//This hides error div
function HideError(field)
{
	var span = $(field); 
	hide(field + "error");
	span.className = "";
}

function initValidation() {
	$("errorMessage").innerHTML = "";
	valid=true;
	errorMessage="";
}
function errorDiv() {
	document.write("<div ID=\"errorMessage\" class=\"error\" style=\"display:none;\"></div>");
}
function formValid()
{
	if (valid)
	{
		return true;
	}
	else 
	{
		// Move to top
		
		// Set error message
		var errorMessageDiv;
		errorMessageDiv = $("errorMessage");
		errorMessageDiv.innerHTML = errorMessage;
		
		show("errorMessage");
		
		// Show error message
		return false;
	}
}
/*
Form validation Example

function ValidateForm(this_form)
{
	valid = true;
	require("fn", "Please enter a First Name");
	require("Ln", "Please enter a Last Name");
	isEmail("email", "Please enter a valid Email");
	isPhone("Phone", "Please enter a valid phone number");
	isSSN("SSN", "Please enter a valid social security number");
	
	return valid;
}
*/
