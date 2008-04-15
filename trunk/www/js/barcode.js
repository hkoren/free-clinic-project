var ms;
var barCode = "";
var d = new Date();
var lastKeyPressTime = d.getTime();
var lastCharComp = false;
var lastChar = "";
var computerDelayThreshold = 25;       // Computer is faster than this


function setDebug(text)
{
	$('comp').innerHTML += text + '<br>';
}

function distInput(event, input, barcodeHandler)
{
	//alert ("You are UNKNOWN")
	//return startstop(field);
	var n = new Date()
	var timeSinceLastKeyPress = n.getTime() - lastKeyPressTime;
	var thisChar = String.fromCharCode(event.keyCode);
	lastKeyPressTime = n.getTime();
	"test = " + event.keyCode;
	 
	if (timeSinceLastKeyPress > computerDelayThreshold)			// Must be a human if greater than threshold but could be start of computer input
	{
		barCode="";						// Reset the barcode
		lastChar = thisChar;
		//setDebug("Human - " + lastChar + "; " + timeSinceLastKeyPress + " <= " + computerDelayThreshold);
		
		// Pass human entered text into field  
		return true;
	}
	else									// Now we know It's a computer
	{
		event.returnValue=false;
		// Initialize bar code if it's empty
		if (barCode.length == 0)
		{
			barCode += lastChar;		// Start with the last character of input, which we thought was human but was actually the beginning of the code scan
			
			// Remove the last letter from the input because it was actually entered by the machine
			input.value = input.value.substring(0,input.value.length-1);
		}
		
		
		// See if we have entered a code
		if (thisChar.charCodeAt(0) == 13)					// If enter was pressed
		{
			//setDebug("bar code scanned: " + barCode + " Resetting");
			// Mark Barcode Scanned
			barcodeHandler(barCode);
			
			// Reset barcode
			barCode="";
		}
		else		// We must be in the middle of scanning a code
		{				
			//setDebug("Computer - " + thisChar + "; charcode: " + thisChar.charCodeAt(0) + "   Delay: " +timeSinceLastKeyPress + " > " + computerDelayThreshold);  
			barCode += thisChar;							// append this character
		}		
		
		return false;				// Ignore code scan in field
	}
}

// Handle the scanning of a drug using a bar code scanner

function getDrug(barCode)
{
		
	var url = "pharmacy_ndc_search.php?s="+barCode+"&mode=json";
	// setDebug(url);
	hide('drugInfo');
	$('ndcSearchStatus').innerHTML = "Searching... Please Wait.";
	new Ajax.Request( url, 
	{ 
		onComplete: function(t) 
		{ 
			var jsonString = t.responseText;
			// setDebug("Response: " + jsonString);
			if (jsonString != '')
			{
				var drugs;
				// Parse object from string using JSON
				try {
					var drugs = JSON.parse(jsonString);
				}
				catch(err)
				{
					setDebug("Error: " + err);
				}
				

				var drug = drugs[0];
				$('ndcSearchStatus').innerHTML = "Drug Found!";

				// Set Name
				$('drugName').innerHTML = "<a href=\"pharmacy_ndc_drug.php?LISTING_SEQ_NO=" + drug.LISTING_SEQ_NO + "\">" + drug.TRADENAME + "</a>";

				// Set Strength
				var package = drug.STRENGTH + " " + drug.UNIT;
				$('strength').innerHTML = package;
				
				// Set Company
				$('manufacturerName').innerHTML = drug.FIRM_NAME;
				
				// Set NDC
				var ndc = drug.LBLCODE + '-' + drug.PRODCODE + '-' + drug.PKGCODE;

				$('ndcNumber').innerHTML = ndc;
				
				// Set hidden form inputs
				$('LISTING_SEQ_NO').value = drug.LISTING_SEQ_NO;
				$('PKGCODE').value = drug.PKGCODE;
				
				// Now show it
				show('drugInfo');
			}	
		}
	});		
}


/*Use
Are you Man or Machine??<br> 
<input type="text" maxlength ="12" size ="12" onKeyDown="return distInput(event)"><br><br>

*/